package  com.xzy.forestSystem.qihoo.bugreport;

import android.content.Context;
import  com.xzy.forestSystem.qihoo.bugreport.javacrash.ExceptionHandleReporter;
import java.lang.Thread;

public class CrashReport {

    /* renamed from: a */
    private static Thread.UncaughtExceptionHandler f385a;

    /* renamed from: b */
    private static ExceptionHandleReporter f386b;

    /* renamed from: c */
    private static Context f387c;

    public static void prepareInit() {
        if (f385a == null) {
            f385a = Thread.getDefaultUncaughtExceptionHandler();
        }
    }

    public static void init(Context context) {
        if (context != null) {
            try {
                if (f385a == null) {
                    return;
                }
                if (f387c == null || f386b == null) {
                    f387c = context;
                    f386b = ExceptionHandleReporter.m45a(context, f385a);
                }
            } catch (Throwable th) {
            }
        }
    }

    public static ExceptionHandleReporter getExceptionHandlerInstance() {
        return f386b;
    }

    /* renamed from: a */
    public static Context m55a() {
        return f387c;
    }
}
