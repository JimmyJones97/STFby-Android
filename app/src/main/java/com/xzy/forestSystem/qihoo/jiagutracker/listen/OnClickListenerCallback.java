package  com.xzy.forestSystem.qihoo.jiagutracker.listen;

import android.annotation.SuppressLint;
import android.view.View;
import  com.xzy.forestSystem.qihoo.jiagutracker.QVMProtect;
import  com.xzy.forestSystem.stub.StubApp;

@QVMProtect
public class OnClickListenerCallback {
    private static OnClickListenerCallback sInstance;

    static {
        StubApp.interface11(3785);
    }

    public static native OnClickListenerCallback getInstance();

    @SuppressLint({"ResourceType"})
    private native String getViewTree(View view);

    public native void onClickProxy(View view, String str);
}
