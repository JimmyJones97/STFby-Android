package com.xzy.forestSystem.baidu.speech;

import android.content.Context;
import  com.xzy.forestSystem.stub.StubApp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/* access modifiers changed from: package-private */
public class Console {
    private static final String TAG = "Console";
    public static final String TYPE_ASR_MSG_AUDIO = "asr.audio";
    public static final String TYPE_ASR_MSG_BEGIN = "asr.begin";
    public static final String TYPE_ASR_MSG_END = "asr.end";
    public static final String TYPE_ASR_MSG_ENGINE_TYPE = "asr.engine";
    public static final String TYPE_ASR_MSG_ENTER = "enter";
    public static final String TYPE_ASR_MSG_EVENT_ERROR = "asr.event_error";
    public static final String TYPE_ASR_MSG_EXIT = "exit";
    public static final String TYPE_ASR_MSG_FINISH = "asr.finish";
    public static final String TYPE_ASR_MSG_PARTIAL = "asr.partial";
    public static final String TYPE_ASR_MSG_READY = "asr.ready";
    public static final String TYPE_ASR_MSG_VOLUME = "asr.volume";
    public static final String TYPE_WP_MSG_ENTER = "enter";
    public static final String TYPE_WP_MSG_EXIT = "exit";
    public static final String TYPE_WP_MSG_FRAME = "wp.frame";
    public static final String TYPE_WP_MSG_REAY = "wp.ready";
    public static final String TYPE_WP_MSG_WAKEUP = "wp.wakeup";
    private static int lastGroup = 0;
    private static final Logger logger = Logger.getLogger(TAG);
    private static final ThreadFactory sThreadFactory = new ThreadFactory() { // from class: com.baidu.speech.Console.1
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override // java.util.concurrent.ThreadFactory
        public Thread newThread(Runnable r) {
            return new Thread(r, "AsrConsole #" + this.mCount.getAndIncrement());
        }
    };
    private final Context context;
    private final int mConsoleGroup;
    private final HashMap<String, ExecutorService> mExs = new HashMap<>();
    private int mTaskIndex;

    public interface Session extends Runnable {
        void cancel(boolean z);

        Msg msg();
    }

    public Console(Context context2) {
        int i = lastGroup + 1;
        lastGroup = i;
        this.mConsoleGroup = i;
        this.mTaskIndex = 0;
        this.context = StubApp.getOrigApplicationContext(context2.getApplicationContext());
    }

    public Context context() {
        return this.context;
    }

    public int cid() {
        return this.mConsoleGroup;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        for (Map.Entry<String, ExecutorService> e : this.mExs.entrySet()) {
            e.getValue().shutdown();
        }
        super.finalize();
    }

    public Session enter(String args) throws Exception {
        return enter("asr", args);
    }

    public Session enter(String cmd, String args) throws Exception {
        logger.log(Level.INFO, "====== enter ======\n\n");
        this.mTaskIndex++;
        Session session = null;
        if (!"wp".equals(cmd)) {
            session = new AsrSession(this, args);
        }
        synchronized (this.mExs) {
            if (!this.mExs.containsKey(cmd)) {
                this.mExs.put(cmd, Executors.newSingleThreadExecutor(sThreadFactory));
            }
            this.mExs.get(cmd).submit(session);
        }
        return session;
    }

    public static final class Msg {
        private String key;
        private Level level;
        private Object value;

        public Msg(String key2, Object value2) {
            this(key2, value2, Level.INFO);
        }

        public Msg(String key2, Object value2, Level level2) {
            this.key = key2;
            this.value = value2;
            this.level = level2;
        }

        public String getKey() {
            return this.key;
        }

        public Object getValue() {
            return this.value;
        }

        public Level level() {
            return this.level;
        }

        /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x000f: APUT  (r7v0 java.lang.Object[]), (0 ??[int, short, byte, char]), (r5v2 java.lang.Object) */
        public String toString() {
            Object[] objArr = new Object[1];
            objArr[0] = this.value == null ? "-" : this.value;
            String tmp = String.format("%s", objArr);
            if (this.value instanceof byte[]) {
                StringBuilder sb = new StringBuilder();
                byte[] data = (byte[]) this.value;
                int len = Math.min(data.length, 5);
                sb.append("byte[" + data.length + "], ");
                sb.append('{');
                for (int i = 0; i < len; i++) {
                    sb.append((int) data[i]).append(", ");
                }
                sb.append("...}");
                tmp = sb.toString();
            }
            if (this.value instanceof short[]) {
                StringBuilder sb2 = new StringBuilder();
                short[] data2 = (short[]) this.value;
                int len2 = Math.min(data2.length, 5);
                sb2.append("short[" + data2.length + "], ");
                sb2.append('{');
                for (int i2 = 0; i2 < len2; i2++) {
                    sb2.append((int) data2[i2]).append(", ");
                }
                sb2.append("...}");
                tmp = sb2.toString();
            }
            return String.format("%-10s %s", this.key, tmp);
        }
    }
}
