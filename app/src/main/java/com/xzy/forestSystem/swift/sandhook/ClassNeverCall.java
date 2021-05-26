package  com.xzy.forestSystem.swift.sandhook;

import android.util.Log;

public class ClassNeverCall {
    private void neverCall() {
    }

    private void neverCall2() {
        Log.e("ClassNeverCall", "ClassNeverCall2");
    }

    private native void neverCallNative();

    private native void neverCallNative2();

    private static void neverCallStatic() {
    }
}
