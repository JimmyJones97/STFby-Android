package com.xzy.forestSystem.sandhook;

import android.os.Build;

public class SandHookConfig {
    public static volatile boolean DEBUG = true;
    public static volatile int SDK_INT = Build.VERSION.SDK_INT;
    public static volatile boolean compiler = (SDK_INT < 29);
    public static volatile int curUser = 0;
    public static volatile boolean delayHook = true;
    public static volatile ClassLoader initClassLoader;
    public static volatile LibLoader libLoader = new LibLoader() { // from class: com.xzy.forestSystem.swift.sandhook.SandHookConfig.1
        @Override // com.xzy.forestSystem.swift.sandhook.SandHookConfig.LibLoader
        public void loadLib() {
            if (SandHookConfig.libSandHookPath == null) {
                System.loadLibrary("sandhook-art");
            } else {
                System.load(SandHookConfig.libSandHookPath);
            }
        }
    };
    public static volatile String libSandHookPath;

    public interface LibLoader {
        void loadLib();
    }
}
