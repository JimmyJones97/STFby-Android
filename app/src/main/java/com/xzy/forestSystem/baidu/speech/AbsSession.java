package com.xzy.forestSystem.baidu.speech;

import android.annotation.SuppressLint;
import com.xzy.forestSystem.baidu.speech.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressLint({"DefaultLocale"})
abstract class AbsSession implements Console.Session {
    private static int sSid = 0;
    private final String args;
    protected final Console console;

    /* renamed from: f1 */
    private final String f195f1;

    /* renamed from: f2 */
    private final String f196f2;
    public final ArrayList<MsgFilter> filters;
    private final Fsm fsm;
    private boolean isCanceled;
    boolean isForceCancel;
    private final Logger logger;
    private final LinkedList<Console.Msg> messages = new LinkedList<>();
    private final int sid;
    private final String tag;
    private Thread workingThread;

    public interface MsgFilter {
        boolean accept(Console.Msg msg) throws Exception;
    }

    /* access modifiers changed from: protected */
    public abstract void onExecute(Map<String, Object> map, String str);

    public AbsSession(Console console2, String tag2, String f1, String f2, HashMap<String, String[]> map, String args2) {
        int i = sSid + 1;
        sSid = i;
        this.sid = i;
        this.filters = new ArrayList<>();
        this.console = console2;
        this.f195f1 = f1;
        this.f196f2 = f2;
        this.args = args2;
        this.tag = tag2;
        this.logger = Logger.getLogger(tag2);
        this.fsm = map == null ? null : new Fsm("asr", map);
    }

    @Override // com.baidu.speech.Console.Session
    public final Console.Msg msg() {
        Console.Msg poll;
        synchronized (this.messages) {
            poll = this.messages.poll();
        }
        return poll;
    }

    @Override // com.baidu.speech.Console.Session
    public final void cancel(boolean force) {
        Thread t;
        log(Level.INFO, "cancel(), force=false");
        this.isCanceled = true;
        this.isForceCancel = force;
        if (force && (t = this.workingThread) != null) {
            t.interrupt();
        }
        log(Level.INFO, "calling onCancel(force=" + force + ")");
        onCancel(force);
        log(Level.INFO, "called  onCancel(force=" + force + ")");
    }

    @Override // java.lang.Runnable
    public final void run() {
        Thread thread = null;
        if (!isCanceled()) {
            try {
                this.workingThread = Thread.currentThread();
                this.workingThread.isInterrupted();
                appendMsg(new Console.Msg("enter", null));
                Map<String, Object> basicParams = new Args(this.console.context(), this.f195f1, this.f196f2).parse("args", this.args).create();
                if (!isCanceled()) {
                    log(Level.INFO, "calling onExecute()");
                    onExecute(basicParams, this.args);
                    log(Level.INFO, "called  onExecute()");
                }
                appendMsg(new Console.Msg("exit", null));
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    appendMsg(new Console.Msg("exit", e));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } finally {
                this.workingThread = thread;
            }
        }
    }

    public final void appendMsg(Console.Msg msg) throws Exception {
        boolean accept = true;
        Iterator i$ = this.filters.iterator();
        while (i$.hasNext()) {
            MsgFilter f = (MsgFilter) i$.next();
            accept &= f.accept(msg);
            if (!accept) {
                log(Level.FINE, "cancel msg append, cause by: " + f + ".onMsg(): return false");
                return;
            }
        }
        synchronized (this.messages) {
            log(msg.level(), msg.toString());
            if (this.fsm != null) {
                this.fsm.check(msg.getKey());
            }
            this.messages.offer(msg);
        }
    }

    public final void log(Level level, Object msg) {
        this.logger.log(level, String.format("%s-%04d-%08d  %s", nickname(), Integer.valueOf(this.console.cid()), Integer.valueOf(sid()), msg), msg instanceof Throwable ? (Throwable) msg : null);
    }

    public final boolean isCanceled() {
        return this.isCanceled;
    }

    /* access modifiers changed from: protected */
    public void onCancel(boolean force) {
    }

    private String nickname() {
        return this.tag + "";
    }

    /* access modifiers changed from: protected */
    public int sid() {
        return this.sid;
    }

    /* access modifiers changed from: protected */
    public String name() {
        return String.format("%s-%04d-%08d", nickname(), Integer.valueOf(this.console.cid()), Integer.valueOf(sid()));
    }

    protected static final int readFully(InputStream in, byte[] b, int off, int len) throws IOException {
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

    protected static void clean(byte[] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = 0;
        }
    }

    /* access modifiers changed from: protected */
    public long computePower(byte[] buffer, int length) {
        short[] data = new short[(length / 2)];
        for (int i = 0; i < data.length; i++) {
            data[i] = (short) ((buffer[(i * 2) + 1] << 8) | (buffer[(i * 2) + 0] & 255));
        }
        return computePower(data, data.length);
    }

    private static long computePower(short[] voiceBuf, int sizeInShort) {
        if (voiceBuf == null) {
            return 0;
        }
        System.currentTimeMillis();
        int sampleSize = Math.min(sizeInShort / 2, 512);
        if (sampleSize <= 0) {
            return 0;
        }
        long sum = 0;
        for (int i = 0; i < sampleSize; i++) {
            sum += (long) (voiceBuf[i * 2] * voiceBuf[i * 2]);
        }
        return (long) Math.sqrt((double) (sum / ((long) sampleSize)));
    }

    public final void registerMsgFilter(MsgFilter filter) {
        this.filters.add(filter);
    }

    public final void unregisterMsgFilter(MsgFilter filter) {
        this.filters.remove(filter);
    }
}
