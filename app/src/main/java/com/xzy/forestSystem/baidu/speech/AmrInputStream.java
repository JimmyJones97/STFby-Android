package com.xzy.forestSystem.baidu.speech;

import  com.xzy.forestSystem.gogisapi.Common.Constant;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

class AmrInputStream extends InputStream {
    private static final byte[] HEAD_AMR_16K = {7, 0, 0, 0};
    private static final byte[] HEAD_AMR_8K = {3, 0, 0, 0};
    private ByteBuffer cache = ((ByteBuffer) ByteBuffer.allocate(1024).flip());

    /* renamed from: in */
    private InputStream f198in;

    public AmrInputStream(InputStream in, int sample) {
        this.f198in = createAmrInputStream(in);
        this.cache.clear();
        switch (sample) {
            case 8000:
                this.cache.put(HEAD_AMR_8K).flip();
                return;
            case Constant.SAMPLE_16K /* 16000 */:
                this.cache.put(HEAD_AMR_16K).flip();
                return;
            default:
                throw new IllegalArgumentException(Constant.EXTRA_SAMPLE + sample);
        }
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.cache.hasRemaining()) {
            return this.cache.get() & 255;
        }
        return this.f198in.read();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
    }

    private static InputStream createAmrInputStream(InputStream in) {
        try {
            return (InputStream) Class.forName("android.media.AmrInputStream").getConstructor(InputStream.class).newInstance(in);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
