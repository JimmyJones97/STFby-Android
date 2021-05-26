package  com.xzy.forestSystem.qihoo.jiagutracker.listen;

import android.view.MotionEvent;
import android.view.View;
import  com.xzy.forestSystem.qihoo.jiagutracker.QVMProtect;
import  com.xzy.forestSystem.stub.StubApp;

@QVMProtect
public class OnTouchListenerProxy implements View.OnTouchListener {
    private String mClassName;
    private View.OnTouchListener mOnTouchListener;
    private OnTouchListenerCallback mOnTouchListenerCallback;

    static {
        StubApp.interface11(3790);
    }

    @Override // android.view.View.OnTouchListener
    public native boolean onTouch(View view, MotionEvent motionEvent);

    public OnTouchListenerProxy(View.OnTouchListener onTouchListener, OnTouchListenerCallback onTouchListenerCallback, String str) {
        this.mOnTouchListener = onTouchListener;
        this.mOnTouchListenerCallback = onTouchListenerCallback;
        this.mClassName = str;
    }
}
