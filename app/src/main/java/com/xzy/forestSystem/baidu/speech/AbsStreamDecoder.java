package com.xzy.forestSystem.baidu.speech;

import android.annotation.TargetApi;
import android.content.Context;
import com.xzy.forestSystem.baidu.speech.AsrSession;
import com.xzy.forestSystem.baidu.speech.Results;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/* access modifiers changed from: package-private */
public abstract class AbsStreamDecoder implements AsrSession.Decoder {
    private static final String TAG = "decoder";
    private static final ThreadFactory sThreadFactory = new ThreadFactory() { // from class: com.baidu.speech.AbsStreamDecoder.1
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            return new Thread(r, "AbsStreamDecoder #" + this.mCount.getAndIncrement());
        }
    };
    private volatile boolean closed = false;
    protected final Context context;
    private final ScheduledExecutorService executor;
    private ConcurrentLinkedQueue<Future> futures = new ConcurrentLinkedQueue<>();
    protected final Logger logger = Logger.getLogger(TAG);
    private OutputStream mDebugPcmOut;
    protected final String mGlb;
    protected final Map<String, Object> mParams;
    private final ConcurrentLinkedQueue<Results.Result> results = new ConcurrentLinkedQueue<>();
    protected final int sample;
    private int usingStreamId = 0;
    private Task usingTask;

    /* access modifiers changed from: protected */
    public abstract void onCreate() throws Exception;

    /* access modifiers changed from: protected */
    public abstract void onDestroy() throws Exception;

    /* access modifiers changed from: protected */
    public abstract void onExecute(int i, InputStream[] inputStreamArr) throws Exception;

    @TargetApi(9)
    public AbsStreamDecoder(Context context2, Map<String, Object> params) throws IOException {
        this.context = context2;
        this.mParams = params;
        this.mGlb = UUID.randomUUID().toString();
        this.sample = ((Integer) params.get("audio.sample")).intValue();
        this.executor = Executors.newSingleThreadScheduledExecutor(sThreadFactory);
        String output_dir = (String) params.get("debug.output-dir");
        String runtimeName = (String) params.get("basic.runtime-name");
        String taskName = (String) params.get("basic.task-name");
        if (Boolean.TRUE.equals(params.get("debug.debug")) && output_dir != null) {
            File dest = new File(output_dir, runtimeName);
            dest.mkdirs();
            this.mDebugPcmOut = new FileOutputStream(new File(dest, taskName + ".cut.pcm"));
        }
        this.logger.info("created debug-dir");
        Future<Void> f = this.executor.submit(new StepOnCreate());
        synchronized (this.futures) {
            this.futures.offer(f);
        }
    }

    @Override // com.baidu.speech.AsrSession.Decoder
    public Results.Result read() throws Exception {
        if (this.closed) {
            throw new Exception("closed");
        }
        checkFutures();
        return this.results.poll();
    }

    private void checkFutures() throws ExecutionException, InterruptedException {
        synchronized (this.futures) {
            Iterator i$ = this.futures.iterator();
            while (i$.hasNext()) {
                Future f = (Future) i$.next();
                if (f != null && f.isDone()) {
                    f.get();
                }
            }
        }
    }

    private class StepOnCreate implements Callable<Void> {
        private StepOnCreate() {
        }

        @Override // java.util.concurrent.Callable
        public Void call() throws Exception {
            AbsStreamDecoder.this.onCreate();
            return null;
        }
    }

    private class Task implements Callable<Long> {
        private static final int DEFAULT_NUM_PIPES = 1;

        /* renamed from: id */
        private final int f197id;
        private InputStream[] ins;
        private OutputStream[] outs;

        Task(AbsStreamDecoder absStreamDecoder, int id, boolean eof) throws IOException {
            this(id, eof, 1);
        }

        Task(int id, boolean eof, int numPipes) throws IOException {
            this.f197id = id;
            this.outs = new OutputStream[1];
            this.ins = new InputStream[numPipes];
            if (!eof) {
                CycleBuffer buffer = new CycleBuffer(491520);
                this.outs[0] = buffer.asOutputStream();
                for (int i = 0; i < this.ins.length; i++) {
                    this.ins[i] = buffer.reader().asInputStream();
                }
            }
        }

        public InputStream[] getInputStreams() {
            return this.ins;
        }

        public OutputStream[] getOutputStreams() {
            return this.outs;
        }

        public int getId() {
            return this.f197id;
        }

        @Override // java.util.concurrent.Callable
        public Long call() throws Exception {
            long s = System.currentTimeMillis();
            AbsStreamDecoder.this.onExecute(this.f197id, this.ins);
            return Long.valueOf(System.currentTimeMillis() - s);
        }
    }

    @Override // com.baidu.speech.AsrSession.Decoder
    public final void write(byte[] buffer, int offset, int length, AsrSession.VadInputStream.SpeechStatus status) throws Exception {
        checkFutures();
        if (status == AsrSession.VadInputStream.SpeechStatus.Begin || status == AsrSession.VadInputStream.SpeechStatus.Resume) {
            int i = this.usingStreamId;
            this.usingStreamId = i + 1;
            this.usingTask = new Task(i, false, this instanceof MergedDecoder ? 2 : 1);
            Future<Long> f = this.executor.submit(this.usingTask);
            synchronized (this.futures) {
                this.futures.offer(f);
            }
        }
        Task t = this.usingTask;
        if (length > 0 && t != null) {
            OutputStream[] outs = this.usingTask.getOutputStreams();
            ArrayList<Integer> closedPipes = new ArrayList<>();
            for (int i2 = 0; i2 < outs.length; i2++) {
                OutputStream out = outs[i2];
                if (out != null) {
                    try {
                        out.write(buffer, offset, length);
                    } catch (IOException e) {
                        this.logger.info(String.format("%s, OutputStream[%d] closed: %s", e, Integer.valueOf(i2), out));
                        out.close();
                        closedPipes.add(Integer.valueOf(i2));
                    }
                }
            }
            Iterator i$ = closedPipes.iterator();
            while (i$.hasNext()) {
//                outs[i$.next().intValue()] = null;
            }
        }
        if ((status == AsrSession.VadInputStream.SpeechStatus.Pause || status == AsrSession.VadInputStream.SpeechStatus.End) && this.usingTask != null) {
            OutputStream[] outs2 = this.usingTask.getOutputStreams();
            for (OutputStream out2 : outs2) {
                if (out2 != null) {
                    out2.close();
                }
            }
            this.usingTask = null;
        }
        if (status == AsrSession.VadInputStream.SpeechStatus.End) {
            int i3 = this.usingStreamId;
            this.usingStreamId = i3 + 1;
            this.usingTask = new Task(i3, true, this instanceof MergedDecoder ? 2 : 1);
            Future<Long> f2 = this.executor.submit(this.usingTask);
            synchronized (this.futures) {
                this.futures.offer(f2);
            }
        }
        if (this.mDebugPcmOut != null) {
            this.mDebugPcmOut.write(buffer, offset, length);
            if (status == AsrSession.VadInputStream.SpeechStatus.End) {
                this.mDebugPcmOut.close();
            }
        }
    }

    @Override // com.baidu.speech.AsrSession.Decoder
    public final void close() {
        try {
            onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.closed = true;
        if (this.mDebugPcmOut != null) {
            try {
                this.mDebugPcmOut.close();
            } catch (IOException e2) {
            }
        }
        this.executor.shutdownNow();
    }

    /* access modifiers changed from: protected */
    public final void appendResult(Results.Result r) {
        this.logger.log(Level.INFO, "append result: " + r);
        if (r == null) {
            this.logger.warning("someone appended null result!");
        } else {
            this.results.offer(r);
        }
    }

    public final boolean isClosed() {
        return this.closed;
    }
}
