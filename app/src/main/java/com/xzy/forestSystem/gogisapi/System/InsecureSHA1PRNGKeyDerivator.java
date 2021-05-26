package  com.xzy.forestSystem.gogisapi.System;

import androidx.core.internal.view.SupportMenu;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;

public class InsecureSHA1PRNGKeyDerivator {
    private static final int BYTES_OFFSET = 81;
    private static final int COUNTER_BASE = 0;
    private static final int DIGEST_LENGTH = 20;
    private static final int[] END_FLAGS = {Integer.MIN_VALUE, GravityCompat.RELATIVE_LAYOUT_DIRECTION, 32768, 128};
    private static final int EXTRAFRAME_OFFSET = 5;
    private static final int FRAME_LENGTH = 16;
    private static final int FRAME_OFFSET = 21;

    /* renamed from: H0 */
    private static final int f538H0 = 1732584193;

    /* renamed from: H1 */
    private static final int f539H1 = -271733879;

    /* renamed from: H2 */
    private static final int f540H2 = -1732584194;

    /* renamed from: H3 */
    private static final int f541H3 = 271733878;

    /* renamed from: H4 */
    private static final int f542H4 = -1009589776;
    private static final int HASHBYTES_TO_USE = 20;
    private static final int HASHCOPY_OFFSET = 0;
    private static final int HASH_OFFSET = 82;
    private static final int[] LEFT;
    private static final int[] MASK = {-1, ViewCompat.MEASURED_SIZE_MASK, SupportMenu.USER_MASK, 255};
    private static final int MAX_BYTES = 48;
    private static final int NEXT_BYTES = 2;
    private static final int[] RIGHT1;
    private static final int[] RIGHT2;
    private static final int SET_SEED = 1;
    private static final int UNDEFINED = 0;
    private transient int[] copies;
    private transient long counter;
    private transient int nextBIndex;
    private transient byte[] nextBytes;
    private transient int[] seed = new int[87];
    private transient long seedLength;
    private transient int state;

    public static byte[] deriveInsecureKey(byte[] seed2, int keySizeInBytes) {
        InsecureSHA1PRNGKeyDerivator derivator = new InsecureSHA1PRNGKeyDerivator();
        derivator.setSeed(seed2);
        byte[] key = new byte[keySizeInBytes];
        derivator.nextBytes(key);
        return key;
    }

    static {
        int[] iArr = new int[4];
        iArr[1] = 40;
        iArr[2] = 48;
        iArr[3] = 56;
        RIGHT1 = iArr;
        int[] iArr2 = new int[4];
        iArr2[1] = 8;
        iArr2[2] = 16;
        iArr2[3] = 24;
        RIGHT2 = iArr2;
        int[] iArr3 = new int[4];
        iArr3[1] = 24;
        iArr3[2] = 16;
        iArr3[3] = 8;
        LEFT = iArr3;
    }

    private InsecureSHA1PRNGKeyDerivator() {
        this.seed[HASH_OFFSET] = f538H0;
        this.seed[83] = f539H1;
        this.seed[84] = f540H2;
        this.seed[85] = f541H3;
        this.seed[86] = f542H4;
        this.seedLength = 0;
        this.copies = new int[37];
        this.nextBytes = new byte[20];
        this.nextBIndex = 20;
        this.counter = 0;
        this.state = 0;
    }

    private void updateSeed(byte[] bytes) {
        updateHash(this.seed, bytes, 0, bytes.length - 1);
        this.seedLength += (long) bytes.length;
    }

    private void setSeed(byte[] seed2) {
        if (seed2 == null) {
            throw new NullPointerException("seed == null");
        }
        if (this.state == 2) {
            System.arraycopy(this.copies, 0, this.seed, HASH_OFFSET, 5);
        }
        this.state = 1;
        if (seed2.length != 0) {
            updateSeed(seed2);
        }
    }

    /* access modifiers changed from: protected */
    public synchronized void nextBytes(byte[] bytes) {
        int lastWord;
        int n;
        int j;
        if (bytes == null) {
            throw new NullPointerException("bytes == null");
        }
        if (this.seed[BYTES_OFFSET] == 0) {
            lastWord = 0;
        } else {
            lastWord = (this.seed[BYTES_OFFSET] + 7) >> 2;
        }
        if (this.state == 0) {
            throw new IllegalStateException("No seed supplied!");
        }
        if (this.state == 1) {
            System.arraycopy(this.seed, HASH_OFFSET, this.copies, 0, 5);
            for (int i = lastWord + 3; i < 18; i++) {
                this.seed[i] = 0;
            }
            long bits = (this.seedLength << 3) + 64;
            if (this.seed[BYTES_OFFSET] < 48) {
                this.seed[14] = (int) (bits >>> 32);
                this.seed[15] = (int) (-1 & bits);
            } else {
                this.copies[19] = (int) (bits >>> 32);
                this.copies[20] = (int) (-1 & bits);
            }
            this.nextBIndex = 20;
        }
        this.state = 2;
        if (bytes.length != 0) {
            int nextByteToReturn = 0;
            if (20 - this.nextBIndex < bytes.length - 0) {
                n = 20 - this.nextBIndex;
            } else {
                n = bytes.length - 0;
            }
            if (n > 0) {
                System.arraycopy(this.nextBytes, this.nextBIndex, bytes, 0, n);
                this.nextBIndex += n;
                nextByteToReturn = 0 + n;
            }
            if (nextByteToReturn < bytes.length) {
                int n2 = this.seed[BYTES_OFFSET] & 3;
                do {
                    if (n2 == 0) {
                        this.seed[lastWord] = (int) (this.counter >>> 32);
                        this.seed[lastWord + 1] = (int) (this.counter & -1);
                        this.seed[lastWord + 2] = END_FLAGS[0];
                    } else {
                        int[] iArr = this.seed;
                        iArr[lastWord] = iArr[lastWord] | ((int) ((this.counter >>> RIGHT1[n2]) & ((long) MASK[n2])));
                        this.seed[lastWord + 1] = (int) ((this.counter >>> RIGHT2[n2]) & -1);
                        this.seed[lastWord + 2] = (int) ((this.counter << LEFT[n2]) | ((long) END_FLAGS[n2]));
                    }
                    if (this.seed[BYTES_OFFSET] > 48) {
                        this.copies[5] = this.seed[16];
                        this.copies[6] = this.seed[17];
                    }
                    computeHash(this.seed);
                    if (this.seed[BYTES_OFFSET] > 48) {
                        System.arraycopy(this.seed, 0, this.copies, 21, 16);
                        System.arraycopy(this.copies, 5, this.seed, 0, 16);
                        computeHash(this.seed);
                        System.arraycopy(this.copies, 21, this.seed, 0, 16);
                    }
                    this.counter++;
                    int j2 = 0;
                    for (int i2 = 0; i2 < 5; i2++) {
                        int k = this.seed[i2 + HASH_OFFSET];
                        this.nextBytes[j2] = (byte) (k >>> 24);
                        this.nextBytes[j2 + 1] = (byte) (k >>> 16);
                        this.nextBytes[j2 + 2] = (byte) (k >>> 8);
                        this.nextBytes[j2 + 3] = (byte) k;
                        j2 += 4;
                    }
                    this.nextBIndex = 0;
                    if (20 < bytes.length - nextByteToReturn) {
                        j = 20;
                    } else {
                        j = bytes.length - nextByteToReturn;
                    }
                    if (j > 0) {
                        System.arraycopy(this.nextBytes, 0, bytes, nextByteToReturn, j);
                        nextByteToReturn += j;
                        this.nextBIndex += j;
                    }
                } while (nextByteToReturn < bytes.length);
            }
        }
    }

    private static void computeHash(int[] arrW) {
        int a = arrW[HASH_OFFSET];
        int b = arrW[83];
        int c = arrW[84];
        int d = arrW[85];
        int e = arrW[86];
        for (int t = 16; t < 80; t++) {
            int temp = ((arrW[t - 3] ^ arrW[t - 8]) ^ arrW[t - 14]) ^ arrW[t - 16];
            arrW[t] = (temp << 1) | (temp >>> 31);
        }
        for (int t2 = 0; t2 < 20; t2++) {
            int temp2 = ((a << 5) | (a >>> 27)) + ((b & c) | ((b ^ -1) & d)) + arrW[t2] + e + 1518500249;
            e = d;
            d = c;
            c = (b << 30) | (b >>> 2);
            b = a;
            a = temp2;
        }
        for (int t3 = 20; t3 < 40; t3++) {
            int temp3 = ((a << 5) | (a >>> 27)) + ((b ^ c) ^ d) + arrW[t3] + e + 1859775393;
            e = d;
            d = c;
            c = (b << 30) | (b >>> 2);
            b = a;
            a = temp3;
        }
        for (int t4 = 40; t4 < 60; t4++) {
            int temp4 = ((a << 5) | (a >>> 27)) + ((b & c) | (b & d) | (c & d)) + ((arrW[t4] + e) - 1894007588);
            e = d;
            d = c;
            c = (b << 30) | (b >>> 2);
            b = a;
            a = temp4;
        }
        for (int t5 = 60; t5 < 80; t5++) {
            int temp5 = ((a << 5) | (a >>> 27)) + ((b ^ c) ^ d) + ((arrW[t5] + e) - 899497514);
            e = d;
            d = c;
            c = (b << 30) | (b >>> 2);
            b = a;
            a = temp5;
        }
        arrW[HASH_OFFSET] = arrW[HASH_OFFSET] + a;
        arrW[83] = arrW[83] + b;
        arrW[84] = arrW[84] + c;
        arrW[85] = arrW[85] + d;
        arrW[86] = arrW[86] + e;
    }

    private static void updateHash(int[] intArray, byte[] byteInput, int fromByte, int toByte) {
        int index = intArray[BYTES_OFFSET];
        int i = fromByte;
        int wordIndex = index >> 2;
        int byteIndex = index & 3;
        intArray[BYTES_OFFSET] = (((index + toByte) - fromByte) + 1) & 63;
        if (byteIndex != 0) {
            while (i <= toByte && byteIndex < 4) {
                intArray[wordIndex] = intArray[wordIndex] | ((byteInput[i] & 255) << ((3 - byteIndex) << 3));
                byteIndex++;
                i++;
            }
            if (byteIndex == 4 && (wordIndex = wordIndex + 1) == 16) {
                computeHash(intArray);
                wordIndex = 0;
            }
            if (i > toByte) {
                return;
            }
        }
        int maxWord = ((toByte - i) + 1) >> 2;
        for (int k = 0; k < maxWord; k++) {
            intArray[wordIndex] = ((byteInput[i] & 255) << 24) | ((byteInput[i + 1] & 255) << 16) | ((byteInput[i + 2] & 255) << 8) | (byteInput[i + 3] & 255);
            i += 4;
            wordIndex++;
            if (wordIndex >= 16) {
                computeHash(intArray);
                wordIndex = 0;
            }
        }
        int nBytes = (toByte - i) + 1;
        if (nBytes != 0) {
            int w = (byteInput[i] & 255) << 24;
            if (nBytes != 1) {
                w |= (byteInput[i + 1] & 255) << 16;
                if (nBytes != 2) {
                    w |= (byteInput[i + 2] & 255) << 8;
                }
            }
            intArray[wordIndex] = w;
        }
    }
}
