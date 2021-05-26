package com.xzy.forestSystem.baidu.speech;

import android.util.AndroidRuntimeException;
import com.xzy.forestSystem.baidu.voicerecognition.android.MJNI;
import  com.xzy.forestSystem.gogisapi.Common.Constant;
import com.xzy.forestSystem.baidu.voicerecognition.android.MJNI;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

class Bv32InputStream extends InputStream {
    private static final byte[] HEAD_BV_16K = {4, 0, 0, 0};
    private static final byte[] HEAD_BV_8K = {0, 0, 0, 0};
    private byte[] BV_DST_BUF;
    private byte[] BV_SRC_BUF;
    private ByteBuffer cache;
    private int code_format;
    private boolean finished;

    /* renamed from: in */
    private DataInputStream f200in;
    private MJNI mjni;

    static {
        System.loadLibrary("BDVoiceRecognitionClient_MFE_V1");
    }

    public Bv32InputStream(InputStream in, int sample, boolean head) {
        this.mjni = new MJNI();
        this.BV_SRC_BUF = new byte[160];
        this.BV_DST_BUF = new byte[(this.BV_SRC_BUF.length * 8)];
        this.cache = (ByteBuffer) ByteBuffer.allocate(this.BV_DST_BUF.length * 2).flip();
        this.f200in = new DataInputStream(in);
        switch (sample) {
            case 8000:
                this.code_format = 0;
                if (head) {
                    put(HEAD_BV_8K, 0, 4);
                    break;
                }
                break;
            case Constant.SAMPLE_16K /* 16000 */:
                this.code_format = 4;
                if (head) {
                    put(HEAD_BV_16K, 0, 4);
                    break;
                }
                break;
            default:
                throw new IllegalArgumentException(Constant.EXTRA_SAMPLE + sample);
        }
        int r = this.mjni.bvEncoderInit();
        if (r < 0) {
            throw new IllegalStateException("bvEncoderInit return " + r);
        }
    }

    public Bv32InputStream(InputStream in, int sample) {
        this(in, sample, true);
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer, int off, int len) throws IOException {
        boolean z = false;
        if (!this.finished && !this.cache.hasRemaining()) {
            Arrays.fill(this.BV_SRC_BUF, (byte) 0);
            int srcLen = 0;
            while (true) {
                if (srcLen >= this.BV_SRC_BUF.length) {
                    break;
                }
                int x = this.f200in.read(this.BV_SRC_BUF, srcLen, this.BV_SRC_BUF.length - srcLen);
                if (x < 0) {
                    this.finished = true;
                    break;
                }
                srcLen += x;
            }
            if (srcLen > 0) {
                int size = this.mjni.pcm2bv(this.BV_SRC_BUF, this.BV_SRC_BUF.length, this.BV_DST_BUF, this.BV_DST_BUF.length, this.code_format, false);
                if (size < 0) {
                    throw new AndroidRuntimeException("error, pcm2bv return " + size);
                }
                put(this.BV_DST_BUF, 0, size);
            }
            if (srcLen < this.BV_SRC_BUF.length) {
                z = true;
            }
            this.finished = z;
        }
        if (this.finished && !this.cache.hasRemaining()) {
            return -1;
        }
        int x2 = Math.min(len, this.cache.remaining());
        this.cache.get(buffer, off, x2);
        return x2;
    }

    private void put(byte[] buffer, int offset, int count) {
        this.cache.clear();
        this.cache.put(buffer, offset, count);
        this.cache.flip();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.mjni.bvEncoderExit();
        super.close();
    }
}
