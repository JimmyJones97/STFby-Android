package com.xzy.forestSystem.mob.tools.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class MultiPartInputStream extends InputStream {
    private int curIs;
    private ArrayList<InputStream> isList = new ArrayList<>();

    MultiPartInputStream() {
    }

    public void addInputStream(InputStream is) throws Throwable {
        this.isList.add(is);
    }

    private boolean isEmpty() {
        return this.isList == null || this.isList.size() <= 0;
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (isEmpty()) {
            return 0;
        }
        return this.isList.get(this.curIs).available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        Iterator<InputStream> it = this.isList.iterator();
        while (it.hasNext()) {
            it.next().close();
        }
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        throw new IOException();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (isEmpty()) {
            return -1;
        }
        int data = this.isList.get(this.curIs).read();
        while (data < 0) {
            this.curIs++;
            if (this.curIs >= this.isList.size()) {
                return data;
            }
            data = this.isList.get(this.curIs).read();
        }
        return data;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int offset, int length) throws IOException {
        if (isEmpty()) {
            return -1;
        }
        int len = this.isList.get(this.curIs).read(b, offset, length);
        while (len < 0) {
            this.curIs++;
            if (this.curIs >= this.isList.size()) {
                return len;
            }
            len = this.isList.get(this.curIs).read(b, offset, length);
        }
        return len;
    }
}
