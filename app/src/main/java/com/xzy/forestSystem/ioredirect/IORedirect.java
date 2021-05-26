package com.xzy.forestSystem.ioredirect;

public class IORedirect {
    private static boolean loadOK;

    static {
        try {
            System.loadLibrary("IOHook");
            loadOK = init();
        } catch (Throwable th) {
        }
    }

    public static native void add(String str, String str2);

    private static native boolean init();

    public static native void remove(String str);
}
