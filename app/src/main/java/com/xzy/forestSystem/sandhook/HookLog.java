package com.xzy.forestSystem.sandhook;

import android.util.Log;

public class HookLog {
    public static boolean DEBUG = SandHookConfig.DEBUG;
    public static final String TAG = "SandHook";

    /* renamed from: d */
    public static int m10d(String str) {
        return Log.d(TAG, str);
    }

    /* renamed from: e */
    public static int m9e(String str) {
        return Log.e(TAG, str);
    }

    /* renamed from: e */
    public static int m8e(String str, Throwable th) {
        return Log.e(TAG, str, th);
    }

    /* renamed from: i */
    public static int m7i(String str) {
        return Log.i(TAG, str);
    }

    /* renamed from: v */
    public static int m6v(String str) {
        return Log.v(TAG, str);
    }

    /* renamed from: w */
    public static int m5w(String str) {
        return Log.w(TAG, str);
    }
}
