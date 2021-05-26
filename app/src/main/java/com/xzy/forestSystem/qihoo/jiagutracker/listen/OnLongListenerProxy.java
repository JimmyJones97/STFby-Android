package  com.xzy.forestSystem.qihoo.jiagutracker.listen;

import android.view.View;
import  com.xzy.forestSystem.qihoo.jiagutracker.QVMProtect;
import  com.xzy.forestSystem.stub.StubApp;

@QVMProtect
public class OnLongListenerProxy implements View.OnLongClickListener {
    private String mClassName;
    private View.OnLongClickListener mOnLongClickListener;
    private OnLongClickListenerCallback mOnlongClickProxyListener;

    static {
        StubApp.interface11(3788);
    }

    @Override // android.view.View.OnLongClickListener
    public native boolean onLongClick(View view);

    public OnLongListenerProxy(View.OnLongClickListener onLongClickListener, OnLongClickListenerCallback onLongClickListenerCallback, String str) {
        this.mOnLongClickListener = onLongClickListener;
        this.mOnlongClickProxyListener = onLongClickListenerCallback;
        this.mClassName = str;
    }
}
