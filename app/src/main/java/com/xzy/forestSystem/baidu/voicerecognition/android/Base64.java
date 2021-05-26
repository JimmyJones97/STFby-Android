package com.xzy.forestSystem.baidu.voicerecognition.android;

import java.io.UnsupportedEncodingException;

public final class Base64 {
    private static final byte[] MAP = {65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};

    public static byte[] decode(byte[] paramArrayOfByte) {
        return decode(paramArrayOfByte, paramArrayOfByte.length);
    }

    public static byte[] decode(byte[] paramArrayOfByte, int paramInt) {
        int m;
        int m2;
        int i1;
        int i = (paramInt / 4) * 3;
        if (i == 0) {
            return new byte[0];
        }
        byte[] arrayOfByte1 = new byte[i];
        int j = 0;
        while (true) {
            byte b = paramArrayOfByte[paramInt - 1];
            if (!(b == 10 || b == 13 || b == 32 || b == 9)) {
                if (b != 61) {
                    break;
                }
                j++;
            }
            paramInt--;
        }
        int n = 0;
        int i2 = 0;
        int i3 = 0;
        int m3 = 0;
        while (i3 < paramInt) {
            byte b2 = paramArrayOfByte[i3];
            if (b2 == 10 || b2 == 13 || b2 == 32 || b2 == 9) {
                m2 = m3;
            } else {
                if (b2 >= 65 && b2 <= 90) {
                    i1 = b2 - 65;
                } else if (b2 >= 97 && b2 <= 122) {
                    i1 = b2 - 71;
                } else if (b2 >= 48 && b2 <= 57) {
                    i1 = b2 + 4;
                } else if (b2 == 43) {
                    i1 = 62;
                } else if (b2 != 47) {
                    return null;
                } else {
                    i1 = 63;
                }
                i2 = (i2 << 6) | ((byte) i1);
                if (n % 4 == 3) {
                    int m4 = m3 + 1;
                    arrayOfByte1[m3] = (byte) ((16711680 & i2) >> 16);
                    int m5 = m4 + 1;
                    arrayOfByte1[m4] = (byte) ((65280 & i2) >> 8);
                    m2 = m5 + 1;
                    arrayOfByte1[m5] = (byte) (i2 & 255);
                } else {
                    m2 = m3;
                }
                n++;
            }
            i3++;
            m3 = m2;
        }
        if (j > 0) {
            int i22 = i2 << (j * 6);
            m = m3 + 1;
            arrayOfByte1[m3] = (byte) ((16711680 & i22) >> 16);
            if (j == 1) {
                m3 = m + 1;
                arrayOfByte1[m] = (byte) ((65280 & i22) >> 8);
            }
            byte[] arrayOfByte2 = new byte[m];
            System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, m);
            return arrayOfByte2;
        }
        m = m3;
        byte[] arrayOfByte2 = new byte[m];
        System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, m);
        return arrayOfByte2;
    }

    public static String encode(byte[] paramArrayOfByte, String paramString) throws UnsupportedEncodingException {
        int j;
        int j2;
        int i = (paramArrayOfByte.length * 4) / 3;
        byte[] arrayOfByte = new byte[(i + (i / 76) + 3)];
        int m = 0;
        int n = paramArrayOfByte.length - (paramArrayOfByte.length % 3);
        int k = 0;
        int j3 = 0;
        while (k < n) {
            int j4 = j3 + 1;
            arrayOfByte[j3] = MAP[(paramArrayOfByte[k] & 255) >> 2];
            int j5 = j4 + 1;
            arrayOfByte[j4] = MAP[((paramArrayOfByte[k] & 3) << 4) | ((paramArrayOfByte[k + 1] & 255) >> 4)];
            int j6 = j5 + 1;
            arrayOfByte[j5] = MAP[((paramArrayOfByte[k + 1] & 15) << 2) | ((paramArrayOfByte[k + 2] & 255) >> 6)];
            int j7 = j6 + 1;
            arrayOfByte[j6] = MAP[paramArrayOfByte[k + 2] & 63];
            if ((j7 - m) % 76 != 0 || j7 == 0) {
                j2 = j7;
            } else {
                j2 = j7 + 1;
                arrayOfByte[j7] = 10;
                m++;
            }
            k += 3;
            j3 = j2;
        }
        switch (paramArrayOfByte.length % 3) {
            case 1:
                int j8 = j3 + 1;
                arrayOfByte[j3] = MAP[(paramArrayOfByte[n] & 255) >> 2];
                int j9 = j8 + 1;
                arrayOfByte[j8] = MAP[(paramArrayOfByte[n] & 3) << 4];
                int j10 = j9 + 1;
                arrayOfByte[j9] = 61;
                arrayOfByte[j10] = 61;
                j = j10 + 1;
                break;
            case 2:
                int j11 = j3 + 1;
                arrayOfByte[j3] = MAP[(paramArrayOfByte[n] & 255) >> 2];
                int j12 = j11 + 1;
                arrayOfByte[j11] = MAP[((paramArrayOfByte[n] & 3) << 4) | ((paramArrayOfByte[n + 1] & 255) >> 4)];
                int j13 = j12 + 1;
                arrayOfByte[j12] = MAP[(paramArrayOfByte[n + 1] & 15) << 2];
                j3 = j13 + 1;
                arrayOfByte[j13] = 61;
            default:
                j = j3;
                break;
        }
        return new String(arrayOfByte, 0, j, paramString);
    }
}
