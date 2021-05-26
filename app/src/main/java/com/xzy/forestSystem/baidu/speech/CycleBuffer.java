package com.xzy.forestSystem.baidu.speech;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* access modifiers changed from: package-private */
public final class CycleBuffer {
    volatile boolean finished;
    byte[] mBuffer;
    volatile int mLimit;
    OutputStream mOutputStream;

    public CycleBuffer() {
        this(480000);
    }

    public CycleBuffer(int capacity) {
        this.mBuffer = new byte[capacity];
    }

    public void write(byte[] pData, int off, int nLen) {
        int s1 = Math.min(nLen, this.mBuffer.length - (this.mLimit % this.mBuffer.length));
        int s2 = nLen - s1;
        if (s1 > 0) {
            System.arraycopy(pData, 0, this.mBuffer, this.mLimit % this.mBuffer.length, s1);
            this.mLimit += s1;
        }
        if (s2 > 0) {
            System.arraycopy(pData, s1, this.mBuffer, 0, s2);
            this.mLimit += s2;
        }
    }

    public void finish() {
        this.finished = true;
    }

    public OutputStream asOutputStream() {
        OutputStream outputStream;
        synchronized (this) {
            if (this.mOutputStream == null) {
                this.mOutputStream = new OutputStream() { // from class: com.baidu.speech.CycleBuffer.1
                    @Override // java.io.OutputStream
                    public void write(int oneByte) throws IOException {
                        throw new UnsupportedOperationException();
                    }

                    @Override // java.io.OutputStream
                    public void write(byte[] buffer, int offset, int count) throws IOException {
                        if (CycleBuffer.this.finished) {
                            throw new IOException("cycle buffer is closed!");
                        }
                        CycleBuffer.this.write(buffer, offset, count);
                    }

                    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
                    public void close() throws IOException {
                        super.close();
                        CycleBuffer.this.finished = true;
                    }
                };
            }
            outputStream = this.mOutputStream;
        }
        return outputStream;
    }

    public CycleReader reader() {
        return new CycleReader();
    }

    public class CycleReader {
        int position;

        public CycleReader() {
        }

        public int getPosition() {
            return this.position;
        }

        public void setPosition(int p) {
            this.position = p;
        }

        public CycleBuffer getBuffer() {
            return CycleBuffer.this;
        }

        public int available() {
            return Math.max(0, CycleBuffer.this.mLimit - this.position);
        }

        public int read(byte[] buffer, int offset, int count) {
            if (CycleBuffer.this.mLimit < this.position) {
                try {
                    Thread.sleep(1);
                    return 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return 0;
                }
            } else {
                int c = Math.min(count, CycleBuffer.this.mLimit - this.position);
                for (int i = 0; i < c; i++) {
                    int p = this.position + 1;
                    this.position = p;
                    buffer[offset + i] = CycleBuffer.this.mBuffer[p % CycleBuffer.this.mBuffer.length];
                }
                if (c <= 0) {
                    c = 0;
                }
                return c;
            }
        }

        public InputStream asInputStream() {
            return new InputStream() { // from class: com.baidu.speech.CycleBuffer.CycleReader.1
                @Override // java.io.InputStream
                public int read() throws IOException {
                    throw new UnsupportedOperationException();
                }

                @Override // java.io.InputStream
                public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
                    if (CycleReader.this.position == 0) {
                        CycleReader.this.position++;
                    }
                    int x = CycleReader.this.read(buffer, byteOffset, byteCount);
                    if (!CycleBuffer.this.finished || x != 0) {
                        return x;
                    }
                    return -1;
                }
            };
        }
    }
}
