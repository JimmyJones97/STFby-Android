package  com.xzy.forestSystem.gogisapi.IOData.DWGReader;

import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.math.BigInteger;

public class ByteUtils {
    public static final int SIZE_BOOL = 1;
    public static final int SIZE_DOUBLE = 8;
    public static final int SIZE_INT = 4;
    public static final int SIZE_LONG = 8;
    public static final int SIZE_SHORT = 2;
    public static final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static final int bytesToInt(byte[] data, int[] offset) {
        int result = 0;
        for (int i = 0; i < 4; i++) {
            int i2 = offset[0];
            offset[0] = i2 + 1;
            result = (result << 8) | byteToUnsignedInt(data[i2]);
        }
        return result;
    }

    public static final void intToBytes(int i, byte[] data, int[] offset) {
        if (data != null) {
            for (int j = (offset[0] + 4) - 1; j >= offset[0]; j--) {
                data[j] = (byte) i;
                i >>= 8;
            }
        }
        offset[0] = offset[0] + 4;
    }

    public static final short bytesToShort(byte[] data, int[] offset) {
        short result = 0;
        for (int i = 0; i < 2; i++) {
            int i2 = offset[0];
            offset[0] = i2 + 1;
            result = (short) (((short) byteToUnsignedInt(data[i2])) | ((short) (result << 8)));
        }
        return result;
    }

    public static final void shortToBytes(short s, byte[] data, int[] offset) {
        if (data != null) {
            data[offset[0] + 1] = (byte) s;
            data[offset[0]] = (byte) (s >> 8);
        }
        offset[0] = offset[0] + 2;
    }

    public static final long bytesToLong(byte[] data, int[] offset) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            int i2 = offset[0];
            offset[0] = i2 + 1;
            result = (result << 8) | ((long) byteToUnsignedInt(data[i2]));
        }
        return result;
    }

    public static final void longToBytes(long l, byte[] data, int[] offset) {
        if (data != null) {
            for (int j = (offset[0] + 8) - 1; j >= offset[0]; j--) {
                data[j] = (byte) ((int) l);
                l >>= 8;
            }
        }
        offset[0] = offset[0] + 8;
    }

    public static final double bytesToDouble(byte[] data, int[] offset) {
        return Double.longBitsToDouble(bytesToLong(data, offset));
    }

    public static final void doubleToBytes(double d, byte[] data, int[] offset) {
        longToBytes(Double.doubleToLongBits(d), data, offset);
    }

    public static final String bytesToString(byte[] data, int[] offset) {
        String st;
        offset[0] = 0;
        int length = bytesToInt(data, offset);
        if (length < 0 || length > data.length) {
            st = new String(data);
        } else {
            st = new String(data, offset[0], length);
        }
        offset[0] = offset[0] + length;
        return st;
    }

    public static final void stringToBytes(String s, byte[] data, int[] offset) {
        byte[] s_bytes = s.getBytes();
        if (data != null) {
            intToBytes(s_bytes.length, data, offset);
            memcpy(data, offset[0], s_bytes, 0, s_bytes.length);
        } else {
            offset[0] = offset[0] + 4;
        }
        offset[0] = offset[0] + s_bytes.length;
    }

    public static final boolean bytesToBool(byte[] data, int[] offset) {
        boolean result = true;
        if (data[offset[0]] == 0) {
            result = false;
        }
        offset[0] = offset[0] + 1;
        return result;
    }

    public static final void boolToBytes(boolean b, byte[] data, int[] offset) {
        int i;
        if (data != null) {
            int i2 = offset[0];
            if (b) {
                i = 1;
            } else {
                i = 0;
            }
            data[i2] = (byte) i;
        }
        offset[0] = offset[0] + 1;
    }

    public static final BigInteger bytesToBigInteger(byte[] data, int[] offset) {
        int length = bytesToInt(data, offset);
        byte[] bytes = new byte[length];
        offset[0] = offset[0] + memcpy(bytes, 0, data, offset[0], length);
        return new BigInteger(bytes);
    }

    public static final void bigIntegerToBytes(BigInteger n, byte[] data, int[] offset) {
        byte[] bytes = n.toByteArray();
        intToBytes(bytes.length, data, offset);
        offset[0] = offset[0] + memcpy(data, offset[0], bytes, 0, bytes.length);
    }

    public static final void bytesToInts(int[] dst, int dst_offset, byte[] src, int src_offset, int length) {
        String str;
        if (src == null || dst == null || src_offset + length > src.length || dst_offset + length > dst.length * 4 || dst_offset % 4 != 0 || length % 4 != 0) {
            StringBuilder append = new StringBuilder("bytesToInts parameters are invalid src==").append(src).append(" dst==").append(dst);
            if (src == null || dst == null) {
                str = " ";
            } else {
                str = " (src_offset+length)>src.length==" + (src_offset + length) + ">" + src.length + " (dst_offset+length)>(dst.length*4)==" + (dst_offset + length) + ">" + (dst.length * 4) + " (dst_offset%4)==" + (dst_offset % 4) + " (length%4)==" + (length % 4) + " dest.length==" + dst.length + " length==" + length;
            }
            croak(append.append(str).toString());
        }
        int[] offset = {src_offset};
        int int_dst_offset = dst_offset / 4;
        int i = 0;
        while (i < length / 4) {
            dst[int_dst_offset] = bytesToInt(src, offset);
            i++;
            int_dst_offset++;
        }
    }

    public static final void intsToBytes(byte[] dst, int dst_offset, int[] src, int src_offset, int length) {
        int length2;
        if (src == null || dst == null || dst_offset + length > dst.length || src_offset + length > src.length * 4 || src_offset % 4 != 0 || length % 4 != 0) {
            StringBuilder append = new StringBuilder("intsToBytes parameters are invalid: src=").append(src).append(" dst=").append(dst).append(" (dst_offset=").append(dst_offset).append(" + length=").append(length).append(")=").append(dst_offset + length).append(" > dst.length=").append(dst == null ? 0 : dst.length).append(" (src_offset=").append(src_offset).append(" + length=").append(length).append(")=").append(src_offset + length).append(" > (src.length=").append(src == null ? 0 : src.length).append("*4)=");
            if (src == null) {
                length2 = 0;
            } else {
                length2 = src.length * 4;
            }
            croak(append.append(length2).append(" (src_offset=").append(src_offset).append(" % 4)=").append(src_offset % 4).append(" != 0").append(" (length=").append(length).append(" % 4)=").append(length % 4).append(" != 0").toString());
        }
        int[] offset = {dst_offset};
        int int_src_offset = src_offset / 4;
        int i = 0;
        while (i < length / 4) {
            intToBytes(src[int_src_offset], dst, offset);
            i++;
            int_src_offset++;
        }
    }

    public static final int byteToUnsignedInt(byte b) {
        return b & 255;
    }

    public static int memcpy(byte[] dst, int dst_offset, byte[] src, int src_offset, int length) {
        if (!(dst == null || src == null)) {
            if (dst.length < dst_offset + length) {
                croak("dst.length = " + dst.length + ", but dst_offset = " + dst_offset + " and length = " + length + FileSelector_Dialog.sFolder);
            }
            if (src.length < src_offset + length) {
                croak("src.length = " + src.length + ", but src_offset = " + src_offset + " and length = " + length + FileSelector_Dialog.sFolder);
            }
            int i = 0;
            while (i < length) {
                dst[dst_offset] = src[src_offset];
                i++;
                dst_offset++;
                src_offset++;
            }
        }
        return length;
    }

    public static boolean memcmp(byte[] a, int a_offset, byte[] b, int b_offset, int length) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        int i = 0;
        while (i < length) {
            if (a[a_offset] != b[b_offset]) {
                return false;
            }
            i++;
            a_offset++;
            b_offset++;
        }
        return true;
    }

    public static void memclr(byte[] array, int offset, int length) {
        int i = 0;
        while (i < length) {
            array[offset] = 0;
            i++;
            offset++;
        }
    }

    public static int round_up(int value, int multiple) {
        return (((value - 1) / multiple) + 1) * multiple;
    }

    public static byte[] zero_pad(byte[] original, int block_size) {
        if (original.length % block_size == 0) {
            return original;
        }
        byte[] result = new byte[round_up(original.length, block_size)];
        memcpy(result, 0, original, 0, original.length);
        return result;
    }

    public static boolean equals(byte[] b1, byte[] b2) {
        if (b1 == b2) {
            return true;
        }
        if (b1 == null || b2 == null) {
            return false;
        }
        if (b1.length != b2.length) {
            return false;
        }
        for (int i = 0; i < b1.length; i++) {
            if (b1[i] != b2[i]) {
                return false;
            }
        }
        return true;
    }

    public static String print_bytes(byte[] data, int offset, int length) {
        int size = length * 2;
        char[] buf = new char[(size + (size / 8) + (size / 64))];
        int buf_pos = 0;
        int j = 0;
        for (int i = offset; i < offset + length; i++) {
            byte b = data[i];
            int buf_pos2 = buf_pos + 1;
            buf[buf_pos] = digits[(240 & b) >> 4];
            int buf_pos3 = buf_pos2 + 1;
            buf[buf_pos2] = digits[15 & b];
            if (j % 4 == 3) {
                buf_pos = buf_pos3 + 1;
                buf[buf_pos3] = ' ';
            } else {
                buf_pos = buf_pos3;
            }
            if (j % 32 == 31) {
                buf[buf_pos] = '\n';
                buf_pos++;
            }
            j++;
        }
        return new String(buf);
    }

    public static String print_bytes_exact(byte[] data, int offset, int length) {
        char[] buf = new char[(length * 2)];
        int j = 0;
        int buf_pos = 0;
        for (int i = offset; i < offset + length; i++) {
            byte b = data[i];
            int buf_pos2 = buf_pos + 1;
            buf[buf_pos] = digits[(240 & b) >> 4];
            buf_pos = buf_pos2 + 1;
            buf[buf_pos2] = digits[15 & b];
            j++;
        }
        return new String(buf);
    }

    public static String print_bytes(byte[] data) {
        return print_bytes(data, 0, data.length);
    }

    private static void croak(String msg) {
    }

    public static int getUnsigned(byte b) {
        return b & 255;
    }
}
