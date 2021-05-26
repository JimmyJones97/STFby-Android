package  com.xzy.forestSystem.mob.tools.log;

import android.content.Context;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.HashMap;

public abstract class NLog {
    private static boolean disable;
    private static HashMap<String, NLog> loggers = new HashMap<>();
    private static LogPrinter printer = new LogPrinter();

    /* access modifiers changed from: protected */
    public abstract String getSDKTag();

    public static void disable() {
        disable = true;
    }

    static {
        MobUncaughtExceptionHandler.register();
    }

    public NLog() {
        loggers.put(getSDKTag(), this);
        if (loggers.size() == 1) {
            loggers.put("__FIRST__", this);
        }
    }

    public static void setContext(Context context) {
        if (context != null) {
            printer.setContext(context);
            NativeErrorHandler.prepare(context);
        }
    }

    public static void setCollector(String sdkTag, LogCollector collector) {
        printer.setCollector(sdkTag, collector);
    }

    protected static final NLog getInstanceForSDK(final String sdkTag, boolean createNew) {
        NLog instance = loggers.get(sdkTag);
        if (instance == null) {
            instance = loggers.get("__FIRST__");
        }
        if (instance != null || !createNew) {
            return instance;
        }
        return new NLog() { // from class:  com.xzy.forestSystem.mob.tools.log.NLog.1
            /* access modifiers changed from: protected */
            @Override //  com.xzy.forestSystem.mob.tools.log.NLog
            public String getSDKTag() {
                return sdkTag;
            }
        };
    }

    /* renamed from: v */
    public final int m60v(Throwable t) {
        if (disable) {
            return 0;
        }
        return printer.println(getSDKTag(), 2, 0, getStackTraceString(t));
    }

    /* renamed from: v */
    public final int m61v(Object format, Object... args) {
        String message;
        if (disable) {
            return 0;
        }
        String s = format.toString();
        if (args.length > 0) {
            message = String.format(s, args);
        } else {
            message = s;
        }
        return printer.println(getSDKTag(), 2, 0, message);
    }

    /* renamed from: v */
    public final int m59v(Throwable throwable, Object format, Object... args) {
        if (disable) {
            return 0;
        }
        String s = format.toString();
        StringBuilder sb = new StringBuilder();
        if (args.length > 0) {
            s = String.format(s, args);
        }
        return printer.println(getSDKTag(), 2, 0, sb.append(s).append('\n').append(getStackTraceString(throwable)).toString());
    }

    /* renamed from: d */
    public final int m69d(Throwable t) {
        if (disable) {
            return 0;
        }
        return printer.println(getSDKTag(), 3, 0, getStackTraceString(t));
    }

    /* renamed from: d */
    public final int m70d(Object format, Object... args) {
        String message;
        if (disable) {
            return 0;
        }
        String s = format.toString();
        if (args.length > 0) {
            message = String.format(s, args);
        } else {
            message = s;
        }
        return printer.println(getSDKTag(), 3, 0, message);
    }

    /* renamed from: d */
    public final int m68d(Throwable throwable, Object format, Object... args) {
        if (disable) {
            return 0;
        }
        String s = format.toString();
        StringBuilder sb = new StringBuilder();
        if (args.length > 0) {
            s = String.format(s, args);
        }
        return printer.println(getSDKTag(), 3, 0, sb.append(s).append('\n').append(getStackTraceString(throwable)).toString());
    }

    /* renamed from: i */
    public final int m63i(Throwable t) {
        if (disable) {
            return 0;
        }
        return printer.println(getSDKTag(), 4, 0, getStackTraceString(t));
    }

    /* renamed from: i */
    public final int m64i(Object format, Object... args) {
        String message;
        if (disable) {
            return 0;
        }
        String s = format.toString();
        if (args.length > 0) {
            message = String.format(s, args);
        } else {
            message = s;
        }
        return printer.println(getSDKTag(), 4, 0, message);
    }

    /* renamed from: i */
    public final int m62i(Throwable throwable, Object format, Object... args) {
        if (disable) {
            return 0;
        }
        String s = format.toString();
        StringBuilder sb = new StringBuilder();
        if (args.length > 0) {
            s = String.format(s, args);
        }
        return printer.println(getSDKTag(), 4, 0, sb.append(s).append('\n').append(getStackTraceString(throwable)).toString());
    }

    /* renamed from: w */
    public final int m57w(Throwable t) {
        if (disable) {
            return 0;
        }
        return printer.println(getSDKTag(), 5, 0, getStackTraceString(t));
    }

    /* renamed from: w */
    public final int m58w(Object format, Object... args) {
        String message;
        if (disable) {
            return 0;
        }
        String s = format.toString();
        if (args.length > 0) {
            message = String.format(s, args);
        } else {
            message = s;
        }
        return printer.println(getSDKTag(), 5, 0, message);
    }

    /* renamed from: w */
    public final int m56w(Throwable throwable, Object format, Object... args) {
        if (disable) {
            return 0;
        }
        String s = format.toString();
        StringBuilder sb = new StringBuilder();
        if (args.length > 0) {
            s = String.format(s, args);
        }
        return printer.println(getSDKTag(), 5, 0, sb.append(s).append('\n').append(getStackTraceString(throwable)).toString());
    }

    /* renamed from: e */
    public final int m66e(Throwable t) {
        if (disable) {
            return 0;
        }
        return printer.println(getSDKTag(), 6, 0, getStackTraceString(t));
    }

    /* renamed from: e */
    public final int m67e(Object format, Object... args) {
        String message;
        if (disable) {
            return 0;
        }
        String s = format.toString();
        if (args.length > 0) {
            message = String.format(s, args);
        } else {
            message = s;
        }
        return printer.println(getSDKTag(), 6, 0, message);
    }

    /* renamed from: e */
    public final int m65e(Throwable throwable, Object format, Object... args) {
        if (disable) {
            return 0;
        }
        String s = format.toString();
        StringBuilder sb = new StringBuilder();
        if (args.length > 0) {
            s = String.format(s, args);
        }
        return printer.println(getSDKTag(), 6, 0, sb.append(s).append('\n').append(getStackTraceString(throwable)).toString());
    }

    public final int crash(Throwable t) {
        if (disable) {
            return 0;
        }
        return printer.println(getSDKTag(), 6, 1, getStackTraceString(t));
    }

    public final void nativeCrashLog(String log) {
        if (!disable) {
            printer.nativeCrashLog(getSDKTag(), log);
        }
    }

    private final String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        for (Throwable t = tr; t != null; t = t.getCause()) {
            if (t instanceof UnknownHostException) {
                return "";
            }
        }
        StringWriter sw = new StringWriter();
        tr.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
