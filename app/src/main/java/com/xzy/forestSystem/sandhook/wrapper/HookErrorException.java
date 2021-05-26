package com.xzy.forestSystem.sandhook.wrapper;

public class HookErrorException extends Exception {
    public HookErrorException(String str) {
        super(str);
    }

    public HookErrorException(String str, Throwable th) {
        super(str, th);
    }
}
