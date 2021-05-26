package  com.xzy.forestSystem.qihoo.jiagutracker.listen;

import android.annotation.SuppressLint;
import android.view.View;
import  com.xzy.forestSystem.qihoo.jiagutracker.QVMProtect;
import  com.xzy.forestSystem.stub.StubApp;

@QVMProtect
public class OnLongClickListenerCallback {
    private static OnLongClickListenerCallback sInstance = null;

    public static native OnLongClickListenerCallback getInstance();

    @SuppressLint({"ResourceType"})
    private native String getViewTree(View view);

    public native void onLongClickProxy(View view, String str);

    static {
        StubApp.interface11(3787);
    }
}
