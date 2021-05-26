package  com.xzy.forestSystem.gogisapi.Common;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitConverter {
    public static byte[] GetBytes(double paramDouble) {
        byte[] arrayOfByte = new byte[8];
        long l = Double.doubleToLongBits(paramDouble);
        for (int i = 0; i < arrayOfByte.length; i++) {
            arrayOfByte[i] = new Long(l).byteValue();
            l >>= 8;
        }
        return arrayOfByte;
    }

    public static byte[] Reverse(byte[] paramArrayOfByte) {
        return Reverse(paramArrayOfByte, 0, paramArrayOfByte.length);
    }

    public static byte[] Reverse(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
        byte[] arrayOfByte = new byte[paramInt2];
        for (int i = 0; i < paramInt2; i++) {
            arrayOfByte[i] = paramArrayOfByte[((paramInt1 + paramInt2) - 1) - i];
        }
        return arrayOfByte;
    }

    public static double ToDouble(byte[] paramArrayOfByte) {
        return ToDouble(paramArrayOfByte, 0);
    }

    public static double ToDouble(byte[] b, int paramInt) {
        long l = 0;
        for (int i = 0; i < 8; i++) {
            l = (l << 8) + ((long) (b[(7 - i) + paramInt] & 255));
        }
        return Double.longBitsToDouble(l);
    }

    public static int ToInt(byte[] paramArrayOfByte) {
        return ToInt(paramArrayOfByte, 0);
    }

    public static int ToInt(byte[] paramArrayOfByte, int paramInt) {
        int i = 0;
        for (int j = 0; j < 4; j++) {
            i += (paramArrayOfByte[j + paramInt] & 255) << ((3 - j) * 8);
        }
        return i;
    }

    public static int byteArrayToInt(byte[] b) {
        return (b[3] & 255) | ((b[2] & 255) << 8) | ((b[1] & 255) << 16) | ((b[0] & 255) << 24);
    }

    public static byte[] intToByteArray(int a) {
        return new byte[]{(byte) ((a >> 24) & 255), (byte) ((a >> 16) & 255), (byte) ((a >> 8) & 255), (byte) (a & 255)};
    }

    public static int byteArrayBigToInt(byte[] b) {
        return (b[0] & 255) | ((b[1] & 255) << 8) | ((b[2] & 255) << 16) | ((b[3] & 255) << 24);
    }

    public static byte[] intToByteArrayBig(int a) {
        return new byte[]{(byte) (a & 255), (byte) ((a >> 8) & 255), (byte) ((a >> 16) & 255), (byte) ((a >> 24) & 255)};
    }

    public static void WriteByteForBig(FileOutputStream fo, int value) {
        try {
            fo.write(intToByteArrayBig(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void WriteByteForLittle(FileOutputStream fo, int value) {
        try {
            fo.write(intToByteArray(value));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final int readLittleInt(DataInputStream dis) throws IOException {
        byte[] byteBuffer = new byte[4];
        dis.readFully(byteBuffer, 0, 4);
        return (byteBuffer[3] << 24) | ((byteBuffer[2] & 255) << 16) | ((byteBuffer[1] & 255) << 8) | (byteBuffer[0] & 255);
    }

    public static final float readLittleFloat(DataInputStream dis) throws IOException {
        return Float.intBitsToFloat(readLittleInt(dis));
    }

    public static final short readLittleShort(DataInputStream dis) throws IOException {
        byte[] byteBuffer = new byte[2];
        dis.readFully(byteBuffer, 0, 2);
        return (short) (((byteBuffer[1] & 255) << 8) | (byteBuffer[0] & 255));
    }

    public static final long readLittleLong(DataInputStream dis) throws IOException {
        byte[] byteBuffer = new byte[8];
        dis.readFully(byteBuffer, 0, 8);
        return (((long) byteBuffer[7]) << 56) | (((long) (byteBuffer[6] & 255)) << 48) | (((long) (byteBuffer[5] & 255)) << 40) | (((long) (byteBuffer[4] & 255)) << 32) | (((long) (byteBuffer[3] & 255)) << 24) | (((long) (byteBuffer[2] & 255)) << 16) | (((long) (byteBuffer[1] & 255)) << 8) | ((long) (byteBuffer[0] & 255));
    }

    public static final int readLittleUnsignedShort(DataInputStream dis) throws IOException {
        byte[] byteBuffer = new byte[4];
        dis.readFully(byteBuffer, 0, 2);
        return ((byteBuffer[1] & 255) << 8) | (byteBuffer[0] & 255);
    }

    public static final double readLittleDouble(DataInputStream dis) throws IOException {
        return Double.longBitsToDouble(readLittleLong(dis));
    }
}
