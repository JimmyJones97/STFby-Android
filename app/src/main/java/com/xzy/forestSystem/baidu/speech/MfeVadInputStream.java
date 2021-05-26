package com.xzy.forestSystem.baidu.speech;

import com.xzy.forestSystem.baidu.speech.AsrSession;
import com.xzy.forestSystem.baidu.speech.easr.easrNativeJni;
import com.xzy.forestSystem.baidu.voicerecognition.android.LibFactory;
import com.xzy.forestSystem.baidu.voicerecognition.android.LibFactory;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

final class MfeVadInputStream extends FilterInputStream implements AsrSession.VadInputStream {
    private static final String PARAMS_KEY_LEFT = "vad-mfe.params-";
    private static final int RET_NO_SPEECH = 3;
    private static final int RET_SILENCE = 0;
    private static final int RET_SILENCE_TO_SPEECH = 1;
    private static final int RET_SPEECH_TOO_LONG = 5;
    private static final int RET_SPEECH_TOO_SHORT = 4;
    private static final int RET_SPEECH_TO_SILENCE = 2;
    private static final String TAG = "MfeVadInputStream";
    private final ByteBuffer DST = ((ByteBuffer) ByteBuffer.allocate(this.outBuf.length * 10).flip());
    private volatile boolean closed;
    private final int finishDelayCount;
    private final byte[] inBuf = new byte[160];
    private final LibFactory.JNI jni = LibFactory.create(1);
    private int lastStatus = Integer.MIN_VALUE;
    private final Logger logger = Logger.getLogger(TAG);
    private final byte[] mBuffer = new byte[1024];
    private byte[] outBuf = new byte[(this.inBuf.length * 1024)];
    private volatile long remaining = Long.MAX_VALUE;
    private boolean speechBeginCalled;
    private boolean speechEndCalled;
    private final LinkedList<AsrSession.VadInputStream.SpeechStatus> statuses = new LinkedList<>();

    static {
        try {
            System.loadLibrary("BDVoiceRecognitionClient_MFE_V1");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MfeVadInputStream(Map<String, Object> params, InputStream in) throws IOException {
        super(in);
        int val;
        int r;
        this.logger.fine("----MfeVadInputStream(), be calling");
        this.jni.Exit();
        for (int key = 0; key < 100; key++) {
            Integer valTemp = (Integer) params.get(PARAMS_KEY_LEFT + key);
            if (valTemp != null && (r = this.jni.SetParam(key, (val = valTemp.intValue()))) != 0) {
                throw new IOException(String.format("set vad param %s=%s fail, return %s", Integer.valueOf(key), Integer.valueOf(val), Integer.valueOf(r)));
            }
        }
        this.finishDelayCount = ((((Integer) params.get("audio.sample")).intValue() * 2) / easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX) * 800;
        this.jni.Init();
        this.logger.fine("----MfeVadInputStream(), be called");
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, com.baidu.speech.AsrSession.VadInputStream
    public int read() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] buf, int off, int len) throws IOException {
        if (!this.DST.hasRemaining()) {
            refill();
        }
        if (this.DST.hasRemaining()) {
            int x = Math.min(len, this.DST.remaining());
            this.DST.get(buf, off, x);
            return x;
        }
        return this.remaining > 0 ? 0 : -1;
    }

    @Override // com.baidu.speech.AsrSession.VadInputStream
    public AsrSession.VadInputStream.SpeechStatus detect() {
        AsrSession.VadInputStream.SpeechStatus s = this.statuses.poll();
        return s == null ? AsrSession.VadInputStream.SpeechStatus.Default : s;
    }

    private void refill() throws IOException {
        Arrays.fill(this.mBuffer, (byte) 0);
        int count = readFully(this.in, this.mBuffer, 0, this.mBuffer.length);
        if (count < this.mBuffer.length) {
            this.remaining = 0;
        } else {
            this.remaining -= (long) count;
        }
        if (count != 0 && this.remaining >= 0) {
            this.DST.clear();
            short[] data = new short[(count / 2)];
            ByteBuffer buffer = ByteBuffer.wrap(this.mBuffer, 0, count).order(ByteOrder.LITTLE_ENDIAN);
            for (int i = 0; i < data.length; i++) {
                data[i] = buffer.getShort();
            }
            int s = this.jni.SendData(data, data.length);
            if (s < 0) {
                throw new IOException("mfe error, SendData=" + s);
            }
            int d = this.jni.Detect();
            if (d < 0) {
                throw new IOException("mfe error, Detect=" + d);
            } else if (d > 2) {
                switch (d) {
                    case 3:
                        throw new IOException("#6, No speech input. mfe detect " + d + ", no speech");
                    case 4:
                        throw new IOException("#6, No speech input. mfe detect " + d + ", speech too short");
                    case 5:
                        throw new IOException("#7, No recognition result matched. mfe detect " + d + ", speech too long");
                    default:
                        throw new IOException("#7, No recognition result matched. mfe detect " + d + ", unknown error.");
                }
            } else {
                if (d == 2) {
                    this.remaining = 0;
                }
                AsrSession.VadInputStream.SpeechStatus status = null;
                if (this.lastStatus == Integer.MIN_VALUE && d == 0) {
                    status = AsrSession.VadInputStream.SpeechStatus.Ready;
                } else if (this.lastStatus == 0 && 1 == d) {
                    status = this.speechBeginCalled ? AsrSession.VadInputStream.SpeechStatus.Resume : AsrSession.VadInputStream.SpeechStatus.Begin;
                    this.speechBeginCalled = true;
                } else if (this.lastStatus == 1 && d == 0) {
                    status = AsrSession.VadInputStream.SpeechStatus.Pause;
                } else if ((this.lastStatus == 0 || this.lastStatus == 1) && 2 == d) {
                    status = AsrSession.VadInputStream.SpeechStatus.End;
                }
                if (status != null) {
                    this.statuses.offer(status);
                }
                this.lastStatus = d;
                while (true) {
                    int x = this.jni.GetCallbackData(this.outBuf, this.outBuf.length);
                    if (x < 0) {
                        throw new IOException("mfe error, GetCallbackData=" + x);
                    } else if (x == 0) {
                        this.DST.flip();
                        return;
                    } else {
                        this.DST.put(this.outBuf, 0, x);
                    }
                }
            }
        }
    }

    private static int readFully(InputStream in, byte[] b, int off, int len) throws IOException {
        if (len < 0) {
            throw new IndexOutOfBoundsException();
        }
        int n = 0;
        while (n < len) {
            int count = in.read(b, off + n, len - n);
            if (count < 0) {
                break;
            }
            n += count;
        }
        return n;
    }

    @Override // java.io.FilterInputStream, java.io.Closeable, java.lang.AutoCloseable, java.io.InputStream, com.baidu.speech.AsrSession.VadInputStream
    public void close() throws IOException {
        this.logger.fine("----close(), be calling");
        super.close();
        synchronized (this) {
            if (!this.closed) {
                this.in.close();
                this.jni.Exit();
                this.closed = true;
            }
        }
        this.logger.fine("----close(), be called.");
    }

    @Override // com.baidu.speech.AsrSession.VadInputStream
    public void finish() {
        this.remaining = (long) this.finishDelayCount;
    }

    @Override // com.baidu.speech.AsrSession.VadInputStream
    public boolean finished() {
        return this.closed || this.remaining == 0;
    }
}
