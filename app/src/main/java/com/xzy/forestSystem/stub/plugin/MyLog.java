package  com.xzy.forestSystem.stub.plugin;

import android.text.TextUtils;
import android.util.Log;

public class MyLog {
    private static final String TAG = "STUB";
    private static boolean isDebug = false;

    public static void log(String... strArr) {
        if (isDebug) {
            StringBuilder sb = new StringBuilder();
            for (String str : strArr) {
                if (!TextUtils.isEmpty(str)) {
                    sb.append(str + ", ");
                }
            }
            Log.e(TAG, sb.toString());
        }
    }

    public static void printException(Throwable th) {
        if (th != null && !isDebug) {
            Log.e(TAG, Log.getStackTraceString(th));
        }
    }

    public static void handleException(Throwable th) {
        handleException(Thread.currentThread(), th, true, 19);
    }

    private static void handleException(Thread thread, Throwable th, boolean z, int i) {
        try {
            printException(th);
            Class<?> cls = Class.forName(" com.xzy.forestSystem.qihoo.bugreport.CrashReport");
            Class<?> cls2 = Class.forName(" com.xzy.forestSystem.qihoo.bugreport.javacrash.ExceptionHandleReporter");
            cls2.getDeclaredMethod("uncaughtException", Thread.class, Throwable.class, Boolean.TYPE, Integer.TYPE).invoke(cls.getDeclaredMethod("getExceptionHandlerInstance", new Class[0]).invoke(null, new Object[0]), thread, th, Boolean.valueOf(z), Integer.valueOf(i));
        } catch (Throwable th2) {
        }
    }
}
