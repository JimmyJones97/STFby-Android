package  com.xzy.forestSystem.qihoo.jiagutracker.listen;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import  com.xzy.forestSystem.qihoo.jiagutracker.QVMProtect;
import  com.xzy.forestSystem.stub.StubApp;

@QVMProtect
public class OnTouchListenerCallback {
    private static OnTouchListenerCallback sInstance = null;

    public static native OnTouchListenerCallback getInstance();

    @SuppressLint({"ResourceType"})
    private native String getViewTree(View view);

    public native void onTouch(View view, MotionEvent motionEvent, String str);

    static {
        StubApp.interface11(3789);
    }
}
