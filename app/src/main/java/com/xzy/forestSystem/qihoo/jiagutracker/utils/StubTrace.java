package  com.xzy.forestSystem.qihoo.jiagutracker.utils;

import  com.xzy.forestSystem.qihoo.jiagutracker.QVMProtect;
import  com.xzy.forestSystem.stub.StubApp;

@QVMProtect
public class StubTrace {
    public static final String LOG_TAG = "StubTrace";
    public static final boolean QIHOO_LOG = true;
    public static int logLevel = 2;

    /* renamed from: d */
    public static native void m29d(String str);

    /* renamed from: d */
    public static native void m28d(String str, String str2);

    /* renamed from: e */
    public static native void m27e(String str);

    /* renamed from: e */
    public static native void m26e(String str, String str2);

    private static native void handleException(Thread thread, Throwable th, boolean z, int i);

    public static native void handleException(Throwable th);

    /* renamed from: i */
    public static native void m25i(String str);

    /* renamed from: i */
    public static native void m24i(String str, String str2);

    /* renamed from: v */
    public static native void m23v(String str);

    /* renamed from: v */
    public static native void m22v(String str, String str2);

    /* renamed from: w */
    public static native void m21w(String str);

    /* renamed from: w */
    public static native void m20w(String str, String str2);

    static {
        StubApp.interface11(3793);
    }
}
