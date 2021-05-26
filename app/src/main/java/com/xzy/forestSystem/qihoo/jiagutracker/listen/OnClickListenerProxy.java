package  com.xzy.forestSystem.qihoo.jiagutracker.listen;

import android.view.View;
import  com.xzy.forestSystem.qihoo.jiagutracker.QVMProtect;
import  com.xzy.forestSystem.stub.StubApp;

@QVMProtect
public class OnClickListenerProxy implements View.OnClickListener {
    private String mClassName;
    private View.OnClickListener mOnClickListener;
    private OnClickListenerCallback mOnClickListenerCallback;

    static {
        StubApp.interface11(3786);
    }

    @Override // android.view.View.OnClickListener
    public native void onClick(View view);

    public OnClickListenerProxy(View.OnClickListener onClickListener, OnClickListenerCallback onClickListenerCallback, String str) {
        this.mOnClickListener = onClickListener;
        this.mOnClickListenerCallback = onClickListenerCallback;
        this.mClassName = str;
    }
}
