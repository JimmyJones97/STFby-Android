package  com.xzy.forestSystem.gogisapi.IOData.DWGReader;

import  com.xzy.forestSystem.qihoo.jiagutracker.C0246Config;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgArc;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgAttdef;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgAttrib;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgBlock;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgBlockControl;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgBlockHeader;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgCircle;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgEllipse;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgEndblk;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgInsert;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgLayer;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgLayerControl;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgLine;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgLinearDimension;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgLwPolyline;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgMText;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgPoint;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgPolyline2D;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgPolyline3D;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgSeqend;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgSolid;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgSpline;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgText;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgVertex2D;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgVertex3D;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.Vector;

public class DwgFileV15Reader extends DwgFileReader {
    private DwgFile dwgFile;

    @Override //  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgFileReader
    public boolean read(DwgFile dwgFile2) throws IOException {
        try {
            this.dwgFile = dwgFile2;
            FileChannel fc = new FileInputStream(new File(dwgFile2.getFileName())).getChannel();
            ByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            readDwgSectionOffsets(bb);
            try {
                readDwgObjectOffsets(bb);
            } catch (Exception e) {
            }
            readDwgObjects(bb);
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    private void readDwgSectionOffsets(ByteBuffer bb) {
        bb.position(19);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.getShort();
        int count = bb.getInt();
        for (int i = 0; i < count; i++) {
            byte rec = bb.get();
            int seek = bb.getInt();
            int size = bb.getInt();
            if (rec == 0) {
                this.dwgFile.addDwgSectionOffset("HEADERS", seek, size);
            } else if (rec == 1) {
                this.dwgFile.addDwgSectionOffset("CLASSES", seek, size);
            } else if (rec == 2) {
                this.dwgFile.addDwgSectionOffset("OBJECTS", seek, size);
            } else if (rec == 3) {
                this.dwgFile.addDwgSectionOffset("UNKNOWN", seek, size);
            } else if (rec == 4) {
                this.dwgFile.addDwgSectionOffset("R14DATA", seek, size);
            } else if (rec == 5) {
                this.dwgFile.addDwgSectionOffset("R14REC5", seek, size);
            }
        }
    }

    private void readDwgObjectOffsets(ByteBuffer bb) throws Exception {
        bb.position(this.dwgFile.getDwgSectionOffset("OBJECTS"));
        while (true) {
            bb.order(ByteOrder.BIG_ENDIAN);
            int i = bb.getShort();
            if (i != 2) {
                bb.order(ByteOrder.LITTLE_ENDIAN);
                byte[] dataBytes = new byte[i];
                for (int i2 = 0; i2 < dataBytes.length; i2++) {
                    dataBytes[i2] = bb.get();
                }
                int[] data = DwgUtil.bytesToMachineBytes(dataBytes);
                int lastHandle = 0;
                int lastLoc = 0;
                int bitPos = 0;
                int bitMax = (i - 2) * 8;
                while (bitPos < bitMax) {
                    Vector v = DwgUtil.getModularChar(data, bitPos);
                    int bitPos2 = ((Integer) v.get(0)).intValue();
                    lastHandle += ((Integer) v.get(1)).intValue();
                    Vector v2 = DwgUtil.getModularChar(data, bitPos2);
                    bitPos = ((Integer) v2.get(0)).intValue();
                    lastLoc += ((Integer) v2.get(1)).intValue();
                    this.dwgFile.addDwgObjectOffset(lastHandle, lastLoc);
                }
            } else {
                return;
            }
        }
    }

    private void readDwgClasses(ByteBuffer bb) throws Exception {
        bb.position(this.dwgFile.getDwgSectionOffset("CLASSES") + 16);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        int size = bb.getInt();
        byte[] dataBytes = new byte[size];
        for (int i = 0; i < dataBytes.length; i++) {
            dataBytes[i] = bb.get();
        }
        int[] data = DwgUtil.bytesToMachineBytes(dataBytes);
        for (int i2 = 0; i2 < data.length; i2++) {
            data[i2] = (byte) ByteUtils.getUnsigned((byte) data[i2]);
        }
        bb.position(bb.position() + 2 + 16);
        int maxbit = size * 8;
        for (int bitPos = 0; bitPos + 8 < maxbit; bitPos = ((Integer) DwgUtil.getBitShort(data, ((Integer) DwgUtil.testBit(data, ((Integer) DwgUtil.getTextString(data, ((Integer) DwgUtil.getTextString(data, ((Integer) DwgUtil.getTextString(data, ((Integer) DwgUtil.getBitShort(data, ((Integer) DwgUtil.getBitShort(data, bitPos).get(0)).intValue()).get(0)).intValue()).get(0)).intValue()).get(0)).intValue()).get(0)).intValue()).get(0)).intValue()).get(0)).intValue()) {
        }
    }

    private void readDwgObjects(ByteBuffer bb) {
        for (int i = 0; i < this.dwgFile.getDwgObjectOffsets().size(); i++) {
            DwgObject obj = readDwgObject(bb, ((DwgObjectOffset) this.dwgFile.getDwgObjectOffsets().get(i)).getOffset());
            if (obj != null) {
                this.dwgFile.addDwgObject(obj);
            }
        }
    }

    private DwgObject readDwgObject(ByteBuffer bb, int offset) {
        try {
            bb.position(offset);
            int size = DwgUtil.getModularShort(bb);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            byte[] dataBytes = new byte[size];
            String[] dataMachValString = new String[size];
            int[] data = new int[size];
            for (int i = 0; i < size; i++) {
                dataBytes[i] = bb.get();
                dataMachValString[i] = HexUtil.bytesToHex(new byte[]{dataBytes[i]});
                data[i] = Integer.decode(C0246Config.HEX_NUM_HEAD + dataMachValString[i]).byteValue();
                data[i] = ByteUtils.getUnsigned((byte) data[i]);
            }
            Vector v = DwgUtil.getBitShort(data, 0);
            int bitPos = ((Integer) v.get(0)).intValue();
            int type = ((Integer) v.get(1)).intValue();
            DwgObject obj = new DwgObject();
            if (type == 17) {
                obj = new DwgArc();
                obj.setGraphicsFlag(true);
            } else if (type == 18) {
                obj = new DwgCircle();
                obj.setGraphicsFlag(true);
            } else if (type == 19) {
                obj = new DwgLine();
                obj.setGraphicsFlag(true);
            } else if (type == 27) {
                obj = new DwgPoint();
                obj.setGraphicsFlag(true);
            } else if (type == 15) {
                obj = new DwgPolyline2D();
                obj.setGraphicsFlag(true);
            } else if (type == 16) {
                obj = new DwgPolyline3D();
                obj.setGraphicsFlag(true);
            } else if (type == 10) {
                obj = new DwgVertex2D();
                obj.setGraphicsFlag(true);
            } else if (type == 11) {
                obj = new DwgVertex3D();
                obj.setGraphicsFlag(true);
            } else if (type == 6) {
                obj = new DwgSeqend();
                obj.setGraphicsFlag(true);
            } else if (type == 1) {
                obj = new DwgText();
                obj.setGraphicsFlag(true);
            } else if (type == 2) {
                obj = new DwgAttrib();
                obj.setGraphicsFlag(true);
            } else if (type == 3) {
                obj = new DwgAttdef();
                obj.setGraphicsFlag(true);
            } else if (type == 4) {
                obj = new DwgBlock();
                obj.setGraphicsFlag(true);
            } else if (type == 5) {
                obj = new DwgEndblk();
                obj.setGraphicsFlag(true);
            } else if (type == 48) {
                obj = new DwgBlockControl();
                obj.setGraphicsFlag(false);
            } else if (type == 49) {
                obj = new DwgBlockHeader();
                obj.setGraphicsFlag(false);
            } else if (type == 50) {
                obj = new DwgLayerControl();
                obj.setGraphicsFlag(false);
            } else if (type == 51) {
                obj = new DwgLayer();
                obj.setGraphicsFlag(false);
            } else if (type == 7) {
                obj = new DwgInsert();
                obj.setGraphicsFlag(true);
            } else if (type == 44) {
                obj = new DwgMText();
                obj.setGraphicsFlag(true);
            } else if (type == 31) {
                obj = new DwgSolid();
                obj.setGraphicsFlag(true);
            } else if (type == 35) {
                obj = new DwgEllipse();
                obj.setGraphicsFlag(true);
            } else if (type == 36) {
                obj = new DwgSpline();
                obj.setGraphicsFlag(true);
            } else if (type == 21) {
                obj = new DwgLinearDimension();
                obj.setGraphicsFlag(true);
            } else if (type == 77) {
                obj = new DwgLwPolyline();
                obj.setGraphicsFlag(true);
            } else if (type == 78) {
                obj = new DwgLwPolyline();
                obj.setGraphicsFlag(true);
            } else if (type == 79) {
                obj = new DwgLwPolyline();
                obj.setGraphicsFlag(true);
            } else if (type == 80) {
                obj = new DwgLwPolyline();
                obj.setGraphicsFlag(true);
            } else if (type == 81) {
                obj = new DwgLwPolyline();
                obj.setGraphicsFlag(true);
            } else if (type == 82) {
                obj = new DwgLwPolyline();
                obj.setGraphicsFlag(true);
            } else if (type == 83) {
                obj = new DwgLwPolyline();
                obj.setGraphicsFlag(true);
            }
            obj.setType(type);
            Vector v2 = DwgUtil.getRawLong(data, bitPos);
            int bitPos2 = ((Integer) v2.get(0)).intValue();
            obj.setSizeInBits(((Integer) v2.get(1)).intValue());
            Vector entityHandle = new Vector();
            Vector v3 = DwgUtil.getHandle(data, bitPos2);
            int bitPos3 = ((Integer) v3.get(0)).intValue();
            for (int i2 = 1; i2 < v3.size(); i2++) {
                entityHandle.add(v3.get(i2));
            }
            obj.setHandle(DwgUtil.handleBinToHandleInt(entityHandle));
            Vector v4 = DwgUtil.readExtendedData(data, bitPos3);
            int bitPos4 = ((Integer) v4.get(0)).intValue();
            obj.setExtendedData((Vector) v4.get(1));
            if (obj.isGraphicsFlag()) {
                Vector v5 = DwgUtil.testBit(data, bitPos4);
                bitPos4 = ((Integer) v5.get(0)).intValue();
                if (((Boolean) v5.get(1)).booleanValue()) {
                    Vector v6 = DwgUtil.getRawLong(data, bitPos4);
                    int bitPos5 = ((Integer) v6.get(0)).intValue();
                    int bgSize = ((Integer) v6.get(1)).intValue() * 8;
                    obj.setGraphicData(((Integer) DwgUtil.getBits(data, bgSize, bitPos5)).intValue());
                    bitPos4 = bitPos5 + bgSize;
                }
            }
            readSpecificObject(obj, data, bitPos4);
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    private void readSpecificObject(DwgObject obj, int[] data, int bitPos) throws Exception {
        if (obj.getType() == 17) {
            ((DwgArc) obj).readDwgArcV15(data, bitPos);
        } else if (obj.getType() == 18) {
            ((DwgCircle) obj).readDwgCircleV15(data, bitPos);
        } else if (obj.getType() == 19) {
            ((DwgLine) obj).readDwgLineV15(data, bitPos);
        } else if (obj.getType() == 27) {
            ((DwgPoint) obj).readDwgPointV15(data, bitPos);
        } else if (obj.getType() == 15) {
            ((DwgPolyline2D) obj).readDwgPolyline2DV15(data, bitPos);
        } else if (obj.getType() == 16) {
            ((DwgPolyline3D) obj).readDwgPolyline3DV15(data, bitPos);
        } else if (obj.getType() == 10) {
            ((DwgVertex2D) obj).readDwgVertex2DV15(data, bitPos);
        } else if (obj.getType() == 11) {
            ((DwgVertex3D) obj).readDwgVertex3DV15(data, bitPos);
        } else if (obj.getType() == 6) {
            ((DwgSeqend) obj).readDwgSeqendV15(data, bitPos);
        } else if (obj.getType() == 1) {
            ((DwgText) obj).readDwgTextV15(data, bitPos);
        } else if (obj.getType() == 2) {
            ((DwgAttrib) obj).readDwgAttribV15(data, bitPos);
        } else if (obj.getType() == 3) {
            ((DwgAttdef) obj).readDwgAttdefV15(data, bitPos);
        } else if (obj.getType() == 4) {
            ((DwgBlock) obj).readDwgBlockV15(data, bitPos);
        } else if (obj.getType() == 5) {
            ((DwgEndblk) obj).readDwgEndblkV15(data, bitPos);
        } else if (obj.getType() == 48) {
            ((DwgBlockControl) obj).readDwgBlockControlV15(data, bitPos);
        } else if (obj.getType() == 49) {
            ((DwgBlockHeader) obj).readDwgBlockHeaderV15(data, bitPos);
        } else if (obj.getType() == 50) {
            ((DwgLayerControl) obj).readDwgLayerControlV15(data, bitPos);
        } else if (obj.getType() == 51) {
            ((DwgLayer) obj).readDwgLayerV15(data, bitPos);
        } else if (obj.getType() == 7) {
            ((DwgInsert) obj).readDwgInsertV15(data, bitPos);
        } else if (obj.getType() == 44) {
            ((DwgMText) obj).readDwgMTextV15(data, bitPos);
        } else if (obj.getType() == 31) {
            ((DwgSolid) obj).readDwgSolidV15(data, bitPos);
        } else if (obj.getType() == 35) {
            ((DwgEllipse) obj).readDwgEllipseV15(data, bitPos);
        } else if (obj.getType() == 36) {
            ((DwgSpline) obj).readDwgSplineV15(data, bitPos);
        } else if (obj.getType() != 20 && obj.getType() != 21 && obj.getType() != 22 && obj.getType() != 23 && obj.getType() != 24 && obj.getType() != 25 && obj.getType() != 26) {
            if (obj.getType() == 77) {
                ((DwgLwPolyline) obj).readDwgLwPolylineV15(data, bitPos);
            } else if (obj.getType() == 78) {
                ((DwgLwPolyline) obj).readDwgLwPolylineV15(data, bitPos);
            } else if (obj.getType() == 79) {
                ((DwgLwPolyline) obj).readDwgLwPolylineV15(data, bitPos);
            } else if (obj.getType() == 80) {
                ((DwgLwPolyline) obj).readDwgLwPolylineV15(data, bitPos);
            } else if (obj.getType() == 81) {
                ((DwgLwPolyline) obj).readDwgLwPolylineV15(data, bitPos);
            } else if (obj.getType() == 82) {
                ((DwgLwPolyline) obj).readDwgLwPolylineV15(data, bitPos);
            } else if (obj.getType() == 83) {
                ((DwgLwPolyline) obj).readDwgLwPolylineV15(data, bitPos);
            }
        }
    }
}
