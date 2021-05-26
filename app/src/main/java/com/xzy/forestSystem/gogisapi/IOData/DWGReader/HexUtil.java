package  com.xzy.forestSystem.gogisapi.IOData.DWGReader;

import java.util.BitSet;

public class HexUtil {
    private HexUtil() {
    }

    public static final String bytesToHex(byte[] bs, int off, int length) {
        StringBuffer sb = new StringBuffer(length * 2);
        bytesToHexAppend(bs, off, length, sb);
        return sb.toString();
    }

    public static final void bytesToHexAppend(byte[] bs, int off, int length, StringBuffer sb) {
        sb.ensureCapacity(sb.length() + (length * 2));
        int i = off;
        while (i < off + length && i < bs.length) {
            sb.append(Character.forDigit((bs[i] >>> 4) & 15, 16));
            sb.append(Character.forDigit(bs[i] & 15, 16));
            i++;
        }
    }

    public static final String bytesToHex(byte[] bs) {
        return bytesToHex(bs, 0, bs.length);
    }

    public static final byte[] hexToBytes(String s) {
        return hexToBytes(s, 0);
    }

    public static final byte[] hexToBytes(String s, int off) {
        byte[] bs = new byte[(((s.length() + 1) / 2) + off)];
        hexToBytes(s, bs, off);
        return bs;
    }

    public static final void hexToBytes(String s, byte[] out, int off) throws NumberFormatException, IndexOutOfBoundsException {
        int slen = s.length();
        if (slen % 2 != 0) {
            s = String.valueOf('0') + s;
        }
        if (out.length < (slen / 2) + off) {
            throw new IndexOutOfBoundsException("Output buffer too small for input (" + out.length + "<" + off + (slen / 2) + ")");
        }
        for (int i = 0; i < slen; i += 2) {
            byte b1 = (byte) Character.digit(s.charAt(i), 16);
            byte b2 = (byte) Character.digit(s.charAt(i + 1), 16);
            if (b1 < 0 || b2 < 0) {
                throw new NumberFormatException();
            }
            out[(i / 2) + off] = (byte) ((b1 << 4) | b2);
        }
    }

    public static final byte[] bitsToBytes(BitSet ba, int size) {
        char c;
        byte[] b = new byte[countBytesForBits(size)];
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            short s = 0;
            for (int j = 0; j < 8; j++) {
                int idx = (i * 8) + j;
                boolean val = idx > size ? false : ba.get(idx);
                s = (short) ((val ? 1 << j : 0) | s);
                if (val) {
                    c = '1';
                } else {
                    c = '0';
                }
                sb.append(c);
            }
            if (s > 255) {
                throw new IllegalStateException("WTF? s = " + ((int) s));
            }
            b[i] = (byte) s;
        }
        return b;
    }

    public static final String bitsToHexString(BitSet ba, int size) {
        return bytesToHex(bitsToBytes(ba, size));
    }

    public static int countBytesForBits(int size) {
        return (size % 8 == 0 ? 0 : 1) + (size / 8);
    }

    public static void bytesToBits(byte[] b, BitSet ba, int maxSize) {
        int x = 0;
        for (byte b2 : b) {
            for (int j = 0; j < 8 && x <= maxSize; j++) {
                ba.set(x, (b2 & (1 << j)) != 0);
                x++;
            }
        }
    }

    public static void hexToBits(String s, BitSet ba, int length) {
        bytesToBits(hexToBytes(s), ba, length);
    }
}
