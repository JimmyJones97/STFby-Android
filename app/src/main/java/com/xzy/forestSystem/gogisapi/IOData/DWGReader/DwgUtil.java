package  com.xzy.forestSystem.gogisapi.IOData.DWGReader;

import  com.xzy.forestSystem.qihoo.jiagutracker.C0246Config;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Vector;

public final class DwgUtil {
    public static Vector readExtendedData(int[] data, int offset) throws Exception {
        int bitPos = offset;
        Vector extData = new Vector();
        while (true) {
            int newBitPos = ((Integer) getBitShort(data, bitPos).get(0)).intValue();
            int size = ((Integer) getBitShort(data, bitPos).get(1)).intValue();
            if (size == 0) {
                Vector v = new Vector();
                v.add(new Integer(newBitPos));
                v.add(extData);
                return v;
            }
            int newBitPos2 = ((Integer) getHandle(data, newBitPos).get(0)).intValue();
            ((Integer) getHandle(data, newBitPos).get(1)).intValue();
            bitPos = newBitPos2;
            Vector eedata = new Vector();
            while (size > 0) {
                int newBitPos3 = ((Integer) getRawChar(data, bitPos).get(0)).intValue();
                int cb = ((Integer) getRawChar(data, bitPos).get(1)).intValue();
                bitPos = newBitPos3;
                size--;
                if (cb == 0) {
                    int newBitPos4 = ((Integer) getRawChar(data, bitPos).get(0)).intValue();
                    int len = ((Integer) getRawChar(data, bitPos).get(1)).intValue();
                    int newBitPos5 = ((Integer) getRawShort(data, newBitPos4).get(0)).intValue();
                    ((Integer) getRawShort(data, newBitPos4).get(1)).intValue();
                    bitPos = newBitPos5;
                    Vector chars = new Vector();
                    for (int i = 0; i < len; i++) {
                        bitPos = ((Integer) getRawChar(data, bitPos).get(0)).intValue();
                        chars.add(new Integer(((Integer) getRawChar(data, bitPos).get(1)).intValue()));
                    }
                    eedata.add(chars);
                    size = (size - len) - 3;
                } else if (cb != 1) {
                    if (cb == 2) {
                        int newBitPos6 = ((Integer) getRawChar(data, bitPos).get(0)).intValue();
                        int charr = ((Integer) getRawChar(data, bitPos).get(1)).intValue();
                        bitPos = newBitPos6;
                        if (charr == 0) {
                            eedata.add("{");
                        } else if (charr == 1) {
                            eedata.add("}");
                        }
                        size--;
                    } else if (cb == 3 || cb == 5) {
                        Vector chars2 = new Vector();
                        for (int i2 = 0; i2 < 8; i2++) {
                            bitPos = ((Integer) getRawChar(data, bitPos).get(0)).intValue();
                            chars2.add(new Integer(((Integer) getRawChar(data, bitPos).get(1)).intValue()));
                        }
                        eedata.add(chars2);
                        size -= 8;
                    } else if (cb == 4) {
                        int newBitPos7 = ((Integer) getRawChar(data, bitPos).get(0)).intValue();
                        int len2 = ((Integer) getRawChar(data, bitPos).get(1)).intValue();
                        bitPos = newBitPos7;
                        Vector chars3 = new Vector();
                        for (int i3 = 0; i3 < len2; i3++) {
                            bitPos = ((Integer) getRawChar(data, bitPos).get(0)).intValue();
                            chars3.add(new Integer(((Integer) getRawChar(data, bitPos).get(1)).intValue()));
                        }
                        eedata.add(chars3);
                        size = (size - len2) - 1;
                    } else if (10 <= cb && cb <= 13) {
                        int newBitPos8 = ((Integer) getRawDouble(data, bitPos).get(0)).intValue();
                        double d1 = ((Double) getRawDouble(data, bitPos).get(1)).doubleValue();
                        int newBitPos9 = ((Integer) getRawDouble(data, newBitPos8).get(0)).intValue();
                        double d2 = ((Double) getRawDouble(data, newBitPos8).get(1)).doubleValue();
                        bitPos = ((Integer) getRawDouble(data, newBitPos9).get(0)).intValue();
                        eedata.add(new double[]{d1, d2, ((Double) getRawDouble(data, newBitPos9).get(1)).doubleValue()});
                        size -= 24;
                    } else if (40 <= cb && cb <= 42) {
                        bitPos = ((Integer) getRawDouble(data, bitPos).get(0)).intValue();
                        eedata.add(new Double(((Double) getRawDouble(data, bitPos).get(1)).doubleValue()));
                        size -= 8;
                    } else if (cb == 70) {
                        bitPos = ((Integer) getRawShort(data, bitPos).get(0)).intValue();
                        eedata.add(new Integer(((Integer) getRawShort(data, bitPos).get(1)).intValue()));
                        size -= 2;
                    } else if (cb == 71) {
                        bitPos = ((Integer) getRawLong(data, bitPos).get(0)).intValue();
                        eedata.add(new Integer(((Integer) getRawLong(data, bitPos).get(1)).intValue()));
                        size -= 4;
                    }
                }
            }
            extData.add(new Vector());
        }
    }

    public static Vector getDefaultDouble(int[] data, int offset, double defVal) throws Exception {
        double val;
        int flags = ((Integer) getBits(data, 2, offset)).intValue();
        int read = 2;
        if (flags == 0) {
            val = defVal;
        } else {
            int _offset = offset + 2;
            if (flags == 3) {
                ByteBuffer bb = ByteBuffer.wrap((byte[]) getBits(data, 64, _offset));
                bb.order(ByteOrder.LITTLE_ENDIAN);
                val = bb.getDouble();
                read = 66;
            } else {
                byte[] dstrArrayAux = new byte[8];
                ByteUtils.doubleToBytes(defVal, dstrArrayAux, new int[1]);
                byte[] dstrArrayAuxx = {dstrArrayAux[7], dstrArrayAux[6], dstrArrayAux[5], dstrArrayAux[4], dstrArrayAux[3], dstrArrayAux[2], dstrArrayAux[1], dstrArrayAux[0]};
                int[] dstrArrayAuxxx = new int[8];
                for (int i = 0; i < dstrArrayAuxxx.length; i++) {
                    dstrArrayAuxxx[i] = ByteUtils.getUnsigned(dstrArrayAuxx[i]);
                }
                byte[] dstrArray = new byte[8];
                for (int i2 = 0; i2 < dstrArray.length; i2++) {
                    dstrArray[i2] = (byte) dstrArrayAuxxx[i2];
                }
                if (flags == 1) {
                    byte[] ddArray = (byte[]) getBits(data, 32, _offset);
                    dstrArray[0] = ddArray[0];
                    dstrArray[1] = ddArray[1];
                    dstrArray[2] = ddArray[2];
                    dstrArray[3] = ddArray[3];
                    read = 34;
                } else {
                    byte[] ddArray2 = (byte[]) getBits(data, 48, _offset);
                    dstrArray[4] = ddArray2[0];
                    dstrArray[5] = ddArray2[1];
                    dstrArray[0] = ddArray2[2];
                    dstrArray[1] = ddArray2[3];
                    dstrArray[2] = ddArray2[4];
                    dstrArray[3] = ddArray2[5];
                    read = 50;
                }
                ByteBuffer bb2 = ByteBuffer.wrap(dstrArray);
                bb2.order(ByteOrder.LITTLE_ENDIAN);
                val = bb2.getDouble();
            }
        }
        Vector v = new Vector();
        v.add(new Integer(offset + read));
        v.add(new Double(val));
        return v;
    }

    public static Vector getBitDouble(int[] data, int offset) throws Exception {
        Vector v = new Vector();
        int type = ((Integer) getBits(data, 2, offset)).intValue();
        int read = 2;
        double val = 0.0d;
        if (type == 0) {
            ByteBuffer bb = ByteBuffer.wrap((byte[]) getBits(data, 64, offset + 2));
            bb.order(ByteOrder.LITTLE_ENDIAN);
            val = bb.getDouble();
            read = 66;
        } else if (type == 1) {
            val = 1.0d;
        } else if (type == 2) {
            val = 0.0d;
        }
        v.add(new Integer(offset + read));
        v.add(new Double(val));
        return v;
    }

    public static Vector getRawDouble(int[] data, int offset) throws Exception {
        ByteBuffer bb = ByteBuffer.wrap((byte[]) getBits(data, 64, offset));
        bb.order(ByteOrder.LITTLE_ENDIAN);
        double val = bb.getDouble();
        Vector v = new Vector();
        v.add(new Integer(offset + 64));
        v.add(new Double(val));
        return v;
    }

    public static Vector getBitShort(int[] data, int offset) throws Exception {
        Vector v = new Vector();
        int type = ((Integer) getBits(data, 2, offset)).intValue();
        int read = 2;
        int val = 0;
        if (type == 0) {
            ByteBuffer bb = ByteBuffer.wrap((byte[]) getBits(data, 16, offset + 2));
            bb.order(ByteOrder.LITTLE_ENDIAN);
            val = bb.getShort();
            read = 18;
        } else if (type == 1) {
            val = ((Integer) getBits(data, 8, offset + 2)).intValue();
            read = 10;
        } else if (type == 2) {
            val = 0;
        } else if (type == 3) {
            val = 256;
        }
        v.add(new Integer(offset + read));
        v.add(new Integer(val));
        return v;
    }

    public static Vector getRawShort(int[] data, int offset) throws Exception {
        ByteBuffer bb = ByteBuffer.wrap((byte[]) getBits(data, 16, offset));
        bb.order(ByteOrder.LITTLE_ENDIAN);
        int val = bb.getShort();
        Vector v = new Vector();
        v.add(new Integer(offset + 16));
        v.add(new Integer(val));
        return v;
    }

    public static Vector getBitLong(int[] data, int offset) throws Exception {
        int type = ((Integer) getBits(data, 2, offset)).intValue();
        int read = 2;
        int val = 0;
        if (type == 0) {
            ByteBuffer bb = ByteBuffer.wrap((byte[]) getBits(data, 32, offset + 2));
            bb.order(ByteOrder.LITTLE_ENDIAN);
            val = bb.getInt();
            read = 34;
        } else if (type == 1) {
            val = ((Integer) getBits(data, 8, offset + 2)).intValue();
            read = 10;
        } else if (type == 2) {
            val = 0;
        }
        Vector v = new Vector();
        v.add(new Integer(offset + read));
        v.add(new Integer(val));
        return v;
    }

    public static Vector getRawLong(int[] data, int offset) {
        Vector v = new Vector();
        v.add(new Integer(offset + 32));
        v.add(new Integer(0));
        return v;
    }

    public static Vector getRawChar(int[] data, int offset) throws Exception {
        int charr = ((Integer) getBits(data, 8, offset)).intValue();
        Vector v = new Vector();
        v.add(new Integer(offset + 8));
        v.add(new Integer(charr));
        return v;
    }

    public static Vector getModularChar(int[] data, int offset) throws Exception {
        int val = 0;
        Vector bytes = new Vector();
        boolean read = true;
        int offsett = offset;
        int fac = 1;
        while (read) {
            int bytee = ((Integer) getBits(data, 8, offsett)).intValue();
            offsett += 8;
            if ((bytee & 128) == 0) {
                read = false;
                if ((bytee & 64) > 0) {
                    fac = -1;
                    bytee &= 191;
                }
            }
//            bytes.add(new Integer(bytee & TransportMediator.KEYCODE_MEDIA_PAUSE));
        }
        if (bytes.size() == 1) {
            val = ((Integer) bytes.get(0)).intValue();
        } else if (bytes.size() == 2) {
            val = ((Integer) bytes.get(0)).intValue() | (((Integer) bytes.get(1)).intValue() << 7);
        } else if (bytes.size() == 3) {
            val = ((Integer) bytes.get(0)).intValue() | (((Integer) bytes.get(1)).intValue() << 7) | (((Integer) bytes.get(2)).intValue() << 14);
        } else if (bytes.size() == 4) {
            val = ((Integer) bytes.get(0)).intValue() | (((Integer) bytes.get(1)).intValue() << 7) | (((Integer) bytes.get(2)).intValue() << 14) | (((Integer) bytes.get(3)).intValue() << 21);
        }
        Vector v = new Vector();
        v.add(new Integer(offsett));
        v.add(new Integer(fac * val));
        return v;
    }

    public static Vector getTextString(int[] data, int offset) throws Exception {
        String string;
        int newBitPos = ((Integer) getBitShort(data, offset).get(0)).intValue();
        int bitLen = ((Integer) getBitShort(data, offset).get(1)).intValue() * 8;
        Object cosa = getBits(data, bitLen, newBitPos);
        if (cosa instanceof byte[]) {
            string = new String((byte[]) cosa, "gb2312");
        } else {
            string = new String(new byte[]{((Integer) cosa).byteValue()}, "gb2312");
        }
        String string2 = string.trim();
        Vector v = new Vector();
        v.add(new Integer(newBitPos + bitLen));
        v.add(string2);
        return v;
    }

    public static Vector getHandle(int[] data, int offset) throws Exception {
        Vector v = new Vector();
        int code = ((Integer) getBits(data, 4, offset)).intValue();
        int counter = ((Integer) getBits(data, 4, offset + 4)).intValue();
        int read = 8;
        Vector hlist = new Vector();
        if (counter > 0) {
            int hlen = counter * 8;
            Object handle = getBits(data, hlen, offset + 8);
            read = 8 + hlen;
            if (hlen > 8) {
                byte[] handleBytes = (byte[]) handle;
                int[] handleInts = new int[handleBytes.length];
                for (int i = 0; i < handleBytes.length; i++) {
                    handleInts[i] = ByteUtils.getUnsigned(handleBytes[i]);
                }
                for (int i2 : handleInts) {
                    hlist.add(new Integer(i2));
                }
            } else {
                hlist.add(handle);
            }
        }
        v.add(new Integer(offset + read));
        v.add(new Integer(code));
        v.add(new Integer(counter));
        for (int i3 = 0; i3 < hlist.size(); i3++) {
            v.add(hlist.get(i3));
        }
        return v;
    }

    public static int getModularShort(ByteBuffer bb) {
        Vector shorts = new Vector();
        bb.order(ByteOrder.BIG_ENDIAN);
        short shortt = bb.getShort();
        while ((shortt & 128) > 0) {
            shorts.add(new Short(shortt));
            shortt = bb.getShort();
        }
        shorts.add(new Short(shortt));
        for (int i = 0; i < shorts.size(); i++) {
            short shortt2 = ((Short) shorts.get(i)).shortValue();
            shorts.set(i, new Integer(((65280 & shortt2) >> 8) | ((shortt2 & 255) << 8)));
        }
        int slen = shorts.size();
        if (slen == 1) {
            return ((Integer) shorts.get(0)).intValue() & 32767;
        }
        if (slen != 2) {
            return 0;
        }
        int tmp = ((Integer) shorts.get(0)).intValue();
        shorts.set(0, shorts.get(1));
        shorts.set(1, new Integer(tmp));
        return ((((Integer) shorts.get(0)).intValue() & 32767) << 15) | (((Integer) shorts.get(1)).intValue() & 32767);
    }

    public static Object getBits(int[] data, int count, int offset) throws Exception {
        int b1;
        int idx = offset / 8;
        int bitidx = offset % 8;
        int[] iArr = new int[4];
        iArr[0] = 255;
        int[][] maskTable = {iArr, new int[]{/*TransportMediator.KEYCODE_MEDIA_PAUSE,*/ 1, 128, 7}, new int[]{63, 2, 192, 6}, new int[]{31, 3, 224, 5}, new int[]{15, 4, 240, 4}, new int[]{7, 5, 248, 3}, new int[]{3, 6, 252, 2}, new int[]{1, 7, 254, 1}};
        int mask1 = maskTable[bitidx][0];
        int lsh = maskTable[bitidx][1];
        int mask2 = maskTable[bitidx][2];
        int rsh = maskTable[bitidx][3];
        int binc = 8 - bitidx;
        int read = 0;
        int rem = count;
        int bytee = 0;
        Vector bytes = new Vector();
        while (read < count) {
            if (rem > binc) {
                b1 = data[idx] & mask1;
                read += binc;
                rem -= binc;
            } else {
                b1 = (data[idx] & mask1) >> ((8 - bitidx) - rem);
                bytee = b1;
                read += rem;
                rem = 0;
            }
            if (read < count) {
                idx++;
                if (rem > bitidx) {
                    bytee = (b1 << lsh) | ((data[idx] & mask2) >> rsh);
                    read += bitidx;
                    rem -= bitidx;
                } else {
                    bytee = (b1 << rem) | ((data[idx] & maskTable[rem][2]) >> (8 - rem));
                    read += rem;
                    rem = 0;
                }
            }
            if (count > 8) {
                bytes.add(new Integer(bytee));
            }
        }
        if (bytes.size() <= 0) {
            return new Integer(bytee);
        }
        byte[] newBytes = new byte[bytes.size()];
        for (int i = 0; i < newBytes.length; i++) {
            newBytes[i] = ((Integer) bytes.get(i)).byteValue();
        }
        return newBytes;
    }

    public static Vector testBit(int[] data, int offset) {
        boolean val = false;
        if ((data[offset / 8] & (1 << (7 - (offset % 8)))) > 0) {
            val = true;
        }
        Vector v = new Vector();
        v.add(new Integer(offset + 1));
        v.add(new Boolean(val));
        return v;
    }

    public static int[] bytesToMachineBytes(byte[] data) {
        String[] dataString = new String[data.length];
        int[] dataOut = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            dataString[i] = HexUtil.bytesToHex(new byte[]{data[i]});
            dataOut[i] = Integer.decode(C0246Config.HEX_NUM_HEAD + dataString[i]).intValue();
        }
        return dataOut;
    }

    public static int handleBinToHandleInt(Vector layerHandle) {
        byte[] layerBytes = new byte[4];
        if (layerHandle.size() > 2) {
            layerBytes[3] = (byte) ((Integer) layerHandle.get(2)).intValue();
        }
        if (layerHandle.size() > 3) {
            layerBytes[3] = (byte) ((Integer) layerHandle.get(3)).intValue();
            layerBytes[2] = (byte) ((Integer) layerHandle.get(2)).intValue();
        }
        if (layerHandle.size() > 4) {
            layerBytes[3] = (byte) ((Integer) layerHandle.get(4)).intValue();
            layerBytes[2] = (byte) ((Integer) layerHandle.get(3)).intValue();
            layerBytes[1] = (byte) ((Integer) layerHandle.get(2)).intValue();
        }
        if (layerHandle.size() > 5) {
            layerBytes[3] = (byte) ((Integer) layerHandle.get(5)).intValue();
            layerBytes[2] = (byte) ((Integer) layerHandle.get(4)).intValue();
            layerBytes[1] = (byte) ((Integer) layerHandle.get(3)).intValue();
            layerBytes[0] = (byte) ((Integer) layerHandle.get(2)).intValue();
        }
        return ByteUtils.bytesToInt(layerBytes, new int[1]);
    }
}
