package com.xzy.forestSystem.mob.tools.network;

import java.io.IOException;
import java.io.InputStream;

public class ByteCounterInputStream extends InputStream {

    /* renamed from: is */
    private InputStream f380is;
    private OnReadListener listener;
    private long readBytes;

    public ByteCounterInputStream(InputStream is) {
        this.f380is = is;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int data = this.f380is.read();
        if (data >= 0) {
            this.readBytes++;
            if (this.listener != null) {
                this.listener.onRead(this.readBytes);
            }
        }
        return data;
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        int len = this.f380is.read(buffer, byteOffset, byteCount);
        if (len > 0) {
            this.readBytes += (long) len;
            if (this.listener != null) {
                this.listener.onRead(this.readBytes);
            }
        }
        return len;
    }

    @Override // java.io.InputStream
    public void mark(int readlimit) {
        this.f380is.mark(readlimit);
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return this.f380is.markSupported();
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        this.f380is.reset();
        this.readBytes = 0;
    }

    @Override // java.io.InputStream
    public long skip(long byteCount) throws IOException {
        return this.f380is.skip(byteCount);
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        return this.f380is.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.f380is.close();
    }

    public void setOnInputStreamReadListener(OnReadListener l) {
        this.listener = l;
    }
}
