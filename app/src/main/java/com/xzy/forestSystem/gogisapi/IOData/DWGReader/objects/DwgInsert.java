package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgInsert extends DwgObject {
    private int blockHeaderHandle;
    private double[] extrusion;
    private int firstAttribHandle;
    private double[] insertionPoint;
    private int lastAttribHandle;
    private double rotation;
    private double[] scale;
    private int seqendHandle;

    public void readDwgInsertV15(int[] data, int offset) throws Exception {
        double z;
        double y;
        double x;
        Vector v = DwgUtil.getBitDouble(data, readObjectHeaderV15(data, offset));
        int bitPos = ((Integer) v.get(0)).intValue();
        double x2 = ((Double) v.get(1)).doubleValue();
        Vector v2 = DwgUtil.getBitDouble(data, bitPos);
        int bitPos2 = ((Integer) v2.get(0)).intValue();
        double y2 = ((Double) v2.get(1)).doubleValue();
        Vector v3 = DwgUtil.getBitDouble(data, bitPos2);
        int bitPos3 = ((Integer) v3.get(0)).intValue();
        this.insertionPoint = new double[]{x2, y2, ((Double) v3.get(1)).doubleValue()};
        int dflag = ((Integer) DwgUtil.getBits(data, 2, bitPos3)).intValue();
        int bitPos4 = bitPos3 + 2;
        if (dflag == 0) {
            Vector v4 = DwgUtil.getRawDouble(data, bitPos4);
            int bitPos5 = ((Integer) v4.get(0)).intValue();
            x = ((Double) v4.get(1)).doubleValue();
            Vector v5 = DwgUtil.getDefaultDouble(data, bitPos5, x);
            int bitPos6 = ((Integer) v5.get(0)).intValue();
            y = ((Double) v5.get(1)).doubleValue();
            Vector v6 = DwgUtil.getDefaultDouble(data, bitPos6, x);
            bitPos4 = ((Integer) v6.get(0)).intValue();
            z = ((Double) v6.get(1)).doubleValue();
        } else if (dflag == 1) {
            x = 1.0d;
            Vector v7 = DwgUtil.getDefaultDouble(data, bitPos4, 1.0d);
            int bitPos7 = ((Integer) v7.get(0)).intValue();
            y = ((Double) v7.get(1)).doubleValue();
            Vector v8 = DwgUtil.getDefaultDouble(data, bitPos7, 1.0d);
            bitPos4 = ((Integer) v8.get(0)).intValue();
            z = ((Double) v8.get(1)).doubleValue();
        } else if (dflag == 2) {
            Vector v9 = DwgUtil.getRawDouble(data, bitPos4);
            bitPos4 = ((Integer) v9.get(0)).intValue();
            x = ((Double) v9.get(1)).doubleValue();
            z = x;
            y = z;
        } else {
            z = 1.0d;
            y = 1.0d;
            x = 1.0d;
        }
        this.scale = new double[]{x, y, z};
        Vector v10 = DwgUtil.getBitDouble(data, bitPos4);
        int bitPos8 = ((Integer) v10.get(0)).intValue();
        this.rotation = ((Double) v10.get(1)).doubleValue();
        Vector v11 = DwgUtil.getBitDouble(data, bitPos8);
        int bitPos9 = ((Integer) v11.get(0)).intValue();
        double x3 = ((Double) v11.get(1)).doubleValue();
        Vector v12 = DwgUtil.getBitDouble(data, bitPos9);
        int bitPos10 = ((Integer) v12.get(0)).intValue();
        double y3 = ((Double) v12.get(1)).doubleValue();
        Vector v13 = DwgUtil.getBitDouble(data, bitPos10);
        int bitPos11 = ((Integer) v13.get(0)).intValue();
        this.extrusion = new double[]{x3, y3, ((Double) v13.get(1)).doubleValue()};
        Vector v14 = DwgUtil.testBit(data, bitPos11);
        int bitPos12 = ((Integer) v14.get(0)).intValue();
        boolean hasattr = ((Boolean) v14.get(1)).booleanValue();
        Vector v15 = DwgUtil.getHandle(data, readObjectTailV15(data, bitPos12));
        int bitPos13 = ((Integer) v15.get(0)).intValue();
        int[] handle = new int[(v15.size() - 1)];
        for (int i = 1; i < v15.size(); i++) {
            handle[i - 1] = ((Integer) v15.get(i)).intValue();
        }
        Vector handleVect = new Vector();
        for (int i2 : handle) {
            handleVect.add(new Integer(i2));
        }
        this.blockHeaderHandle = DwgUtil.handleBinToHandleInt(handleVect);
        if (hasattr) {
            Vector v16 = DwgUtil.getHandle(data, bitPos13);
            int bitPos14 = ((Integer) v16.get(0)).intValue();
            int[] handle2 = new int[(v16.size() - 1)];
            for (int i3 = 1; i3 < v16.size(); i3++) {
                handle2[i3 - 1] = ((Integer) v16.get(i3)).intValue();
            }
            Vector handleVect2 = new Vector();
            for (int i4 : handle2) {
                handleVect2.add(new Integer(i4));
            }
            this.firstAttribHandle = DwgUtil.handleBinToHandleInt(handleVect2);
            Vector v17 = DwgUtil.getHandle(data, bitPos14);
            int bitPos15 = ((Integer) v17.get(0)).intValue();
            int[] handle3 = new int[(v17.size() - 1)];
            for (int i5 = 1; i5 < v17.size(); i5++) {
                handle3[i5 - 1] = ((Integer) v17.get(i5)).intValue();
            }
            Vector handleVect3 = new Vector();
            for (int i6 : handle3) {
                handleVect3.add(new Integer(i6));
            }
            this.lastAttribHandle = DwgUtil.handleBinToHandleInt(handleVect3);
            Vector v18 = DwgUtil.getHandle(data, bitPos15);
            ((Integer) v18.get(0)).intValue();
            int[] handle4 = new int[(v18.size() - 1)];
            for (int i7 = 1; i7 < v18.size(); i7++) {
                handle4[i7 - 1] = ((Integer) v18.get(i7)).intValue();
            }
            Vector handleVect4 = new Vector();
            for (int i8 : handle4) {
                handleVect4.add(new Integer(i8));
            }
            this.seqendHandle = DwgUtil.handleBinToHandleInt(handleVect4);
        }
    }

    public int getBlockHeaderHandle() {
        return this.blockHeaderHandle;
    }

    public void setBlockHeaderHandle(int blockHeaderHandle2) {
        this.blockHeaderHandle = blockHeaderHandle2;
    }

    public int getFirstAttribHandle() {
        return this.firstAttribHandle;
    }

    public void setFirstAttribHandle(int firstAttribHandle2) {
        this.firstAttribHandle = firstAttribHandle2;
    }

    public double[] getInsertionPoint() {
        return this.insertionPoint;
    }

    public void setInsertionPoint(double[] insertionPoint2) {
        this.insertionPoint = insertionPoint2;
    }

    public int getLastAttribHandle() {
        return this.lastAttribHandle;
    }

    public void setLastAttribHandle(int lastAttribHandle2) {
        this.lastAttribHandle = lastAttribHandle2;
    }

    public double getRotation() {
        return this.rotation;
    }

    public void setRotation(double rotation2) {
        this.rotation = rotation2;
    }

    public double[] getScale() {
        return this.scale;
    }

    public void setScale(double[] scale2) {
        this.scale = scale2;
    }

    public double[] getExtrusion() {
        return this.extrusion;
    }

    public void setExtrusion(double[] extrusion2) {
        this.extrusion = extrusion2;
    }

    public int getSeqendHandle() {
        return this.seqendHandle;
    }

    public void setSeqendHandle(int seqendHandle2) {
        this.seqendHandle = seqendHandle2;
    }

    public Object clone() {
        DwgInsert dwgInsert = new DwgInsert();
        dwgInsert.setType(this.type);
        dwgInsert.setHandle(this.handle);
        dwgInsert.setVersion(this.version);
        dwgInsert.setMode(this.mode);
        dwgInsert.setLayerHandle(this.layerHandle);
        dwgInsert.setColor(this.color);
        dwgInsert.setNumReactors(this.numReactors);
        dwgInsert.setNoLinks(this.noLinks);
        dwgInsert.setLinetypeFlags(this.linetypeFlags);
        dwgInsert.setPlotstyleFlags(this.plotstyleFlags);
        dwgInsert.setSizeInBits(this.sizeInBits);
        dwgInsert.setExtendedData(this.extendedData);
        dwgInsert.setGraphicData(this.graphicData);
        dwgInsert.setInsertionPoint(this.insertionPoint);
        dwgInsert.setScale(this.scale);
        dwgInsert.setRotation(this.rotation);
        dwgInsert.setExtrusion(this.extrusion);
        dwgInsert.setBlockHeaderHandle(this.blockHeaderHandle);
        dwgInsert.setFirstAttribHandle(this.firstAttribHandle);
        dwgInsert.setLastAttribHandle(this.lastAttribHandle);
        dwgInsert.setSeqendHandle(this.seqendHandle);
        return dwgInsert;
    }
}
