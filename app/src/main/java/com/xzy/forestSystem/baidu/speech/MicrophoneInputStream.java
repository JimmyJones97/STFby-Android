package com.xzy.forestSystem.baidu.speech;

import android.media.AudioRecord;
import  com.xzy.forestSystem.gogisapi.Common.Constant;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.logging.Logger;

/* access modifiers changed from: package-private */
public final class MicrophoneInputStream extends InputStream {
    private static final String TAG = "MicrophoneInputStream";
    private static final Logger logger = Logger.getLogger(TAG);

    /* renamed from: in */
    private final InputStream f201in;
    private final PrivateMicrophoneInputStream micIn;
    private final int sample;

    public MicrophoneInputStream(int sample2) throws IOException {
        this(sample2, null);
    }

    public MicrophoneInputStream(int sample2, InputStream source) throws IOException {
        this.sample = sample2;
        this.micIn = new PrivateMicrophoneInputStream(Constant.SAMPLE_16K, source);
        if (sample2 == 16000) {
            this.f201in = this.micIn;
        } else if (sample2 == 8000) {
            this.f201in = createResampleInputStream(this.micIn, Constant.SAMPLE_16K, sample2);
        } else {
            throw new UnsupportedOperationException("bad sample, " + sample2);
        }
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer) throws IOException {
        return this.f201in.read(buffer, 0, buffer.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer, int byteOffset, int byteCount) throws IOException {
        return this.f201in.read(buffer, byteOffset, byteCount);
    }

    public long position() {
        long p = this.micIn.position();
        return 8000 == this.sample ? p / 2 : p;
    }

    public MicrophoneInputStream position(long p) {
        PrivateMicrophoneInputStream privateMicrophoneInputStream = this.micIn;
        if (8000 == this.sample) {
            p *= 2;
        }
        privateMicrophoneInputStream.position(p);
        return this;
    }

    public int globalPosition() {
        return this.micIn.globalPosition() / (Constant.SAMPLE_16K / this.sample);
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.f201in.close();
    }

    private static InputStream createResampleInputStream(InputStream in, int rateIn, int rateOut) {
        try {
            return (InputStream) Class.forName("android.media.ResampleInputStream").getConstructor(InputStream.class, Integer.TYPE, Integer.TYPE).newInstance(in, Integer.valueOf(rateIn), Integer.valueOf(rateOut));
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    /* access modifiers changed from: private */
    public static class PrivateMicrophoneInputStream extends InputStream implements Runnable {
        private static final int DEFAULT_BUFFER_SIZE = 163840;
        private static final byte[] sData = new byte[1966080];
        private static InnerSourceInputStream sInnerSourceInputStream;
        private static int sLimit;
        private static int sUsingCount;
        private static IOException throwedException;
        private volatile boolean closed;
        int debugCount;
        int debugLast;
        private long position;
        private int sample;

        public PrivateMicrophoneInputStream(int sample2) throws IOException {
            this(sample2, null);
        }

        public PrivateMicrophoneInputStream(int sample2, InputStream source) throws IOException {
            this.debugCount = 0;
            this.debugLast = 0;
            this.sample = sample2;
            synchronized (PrivateMicrophoneInputStream.class) {
                if (sInnerSourceInputStream == null) {
                    if (source == null) {
                        AudioRecord r = new AudioRecord(1, sample2, 16, 2, DEFAULT_BUFFER_SIZE);
                        r.startRecording();
                        if (r.getRecordingState() != 3) {
                            r.release();
                            throw new IOException("recorder start failed, RecordingState=" + r.getRecordingState());
                        }
                        sInnerSourceInputStream = new InnerSourceInputStream(r);
                    } else {
                        sInnerSourceInputStream = new InnerSourceInputStream(source);
                    }
                    new Thread(this, "glb-record").start();
                }
            }
            sUsingCount++;
            position((long) sLimit);
            MicrophoneInputStream.logger.info("new instance(), sUsingCount=" + sUsingCount + ", sInnerSourceInputStream=" + sInnerSourceInputStream);
        }

        public long position() {
            long p = this.position;
            while (p % 4 != 0) {
                p--;
            }
            return p;
        }

        public PrivateMicrophoneInputStream position(long p) {
            long dstP = p;
            if (dstP < 0) {
                dstP = 0;
                MicrophoneInputStream.logger.warning("error position: " + p);
            }
            while (dstP % 4 != 0) {
                dstP--;
            }
            this.position = dstP;
            MicrophoneInputStream.logger.info("position to: " + dstP + ", by raw postion: " + p);
            return this;
        }

        @Override // java.io.InputStream
        public int read() throws IOException {
            throw new UnsupportedOperationException();
        }

        @Override // java.io.InputStream
        public int read(byte[] buffer, int off, int len) throws IOException {
            if (len > sData.length) {
                throw new IOException("buffer too long");
            } else if (throwedException != null) {
                throw throwedException;
            } else if (this.closed) {
                throw new IOException("mic stream closed");
            } else {
                for (int i = 0; i < 30 && ((long) sLimit) - this.position < ((long) len); i++) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        throw new InterruptedIOException("" + e);
                    }
                }
                int x = 0;
                if (((long) sLimit) - this.position >= ((long) len)) {
                    int p = (int) (this.position % ((long) sData.length));
                    int s1 = Math.min(len, sData.length - p);
                    int s2 = len - s1;
                    System.arraycopy(sData, p, buffer, off, s1);
                    if (s2 > 0) {
                        System.arraycopy(sData, 0, buffer, off + s1, s2);
                    }
                    x = s1 + s2;
                    this.position += (long) x;
                }
                this.debugCount += x;
                if (this.debugCount > this.debugLast) {
                    MicrophoneInputStream.logger.fine("mic:" + this.debugCount);
                    this.debugLast += 360;
                }
                return x;
            }
        }

        @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
        public void close() throws IOException {
            super.close();
            synchronized (this) {
                if (!this.closed) {
                    synchronized (PrivateMicrophoneInputStream.class) {
                        sUsingCount--;
                        if (sUsingCount == 0) {
                            sInnerSourceInputStream.close();
                            sInnerSourceInputStream = null;
                            sLimit = 0;
                            throwedException = null;
                        }
                    }
                    MicrophoneInputStream.logger.info("close(), sUsingCount=" + sUsingCount + ", sInnerSourceInputStream=" + sInnerSourceInputStream);
                }
                this.closed = true;
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            while (sUsingCount > 0) {
                try {
                    ready();
                } catch (IOException e) {
                    throwedException = e;
                }
            }
        }

        private void ready() throws IOException {
            byte[] buffer = new byte[320];
            int x = sInnerSourceInputStream.read(buffer);
            int lmt = sLimit % sData.length;
            int count1 = Math.min(sData.length - lmt, buffer.length);
            int count2 = buffer.length - count1;
            if (count1 > 0) {
                System.arraycopy(buffer, 0, sData, lmt, count1);
            }
            if (count2 > 0) {
                System.arraycopy(buffer, 0, sData, 0, count2);
            }
            sLimit += x;
            MicrophoneInputStream.logger.finer("called ready(), sLimit=" + sLimit);
        }

        public PrivateMicrophoneInputStream branch() throws IOException {
            MicrophoneInputStream.logger.info("called branch():MicrophoneInputStream");
            return new PrivateMicrophoneInputStream(this.sample, null).position(position());
        }

        public int globalPosition() {
            return sLimit;
        }
    }

    /* access modifiers changed from: private */
    public static class InnerSourceInputStream {
        private static AudioRecord audioRecorder;
        int debugCount = 0;
        int debugLast = 0;
        private InputStream source;

        InnerSourceInputStream(InputStream source2) {
            this.source = source2;
        }

        InnerSourceInputStream(AudioRecord audioRecorder2) {
            audioRecorder = audioRecorder2;
        }

        public int read(byte[] buffer) throws IOException {
            if (this.source != null) {
                return this.source.read(buffer);
            }
            int x = audioRecorder.read(buffer, 0, buffer.length);
            if (x < 0) {
                throw new IOException("recorder error #" + x);
            }
            this.debugCount += x;
            if (this.debugCount <= this.debugLast) {
                return x;
            }
            MicrophoneInputStream.logger.fine("mic:" + this.debugCount);
            this.debugLast += 360;
            return x;
        }

        public void close() throws IOException {
            if (this.source != null) {
                this.source.close();
            } else {
                audioRecorder.release();
            }
        }
    }
}
