package  com.xzy.forestSystem.gogisapi.MyControls;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;

public class LoadingDialog extends Dialog {
    private static final int CHANGE_TITLE_WHAT = 1;
    private static final int CHNAGE_TITLE_DELAYMILLIS = 300;
    private static final int MAX_SUFFIX_NUMBER = 3;
    private static final char SUFFIX = '.';
    private ICallback _BindCallback = null;
    private String _BindCommand = "OK";
    private boolean cancelable = true;
    private Handler handler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.LoadingDialog.1
        private int num = 0;

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                StringBuilder builder = new StringBuilder();
                if (this.num >= 3) {
                    this.num = 0;
                }
                this.num++;
                for (int i = 0; i < this.num; i++) {
                    builder.append(LoadingDialog.SUFFIX);
                }
                LoadingDialog.this.tv_point.setText(builder.toString());
                if (LoadingDialog.this.isShowing()) {
                    LoadingDialog.this.handler.sendEmptyMessageDelayed(1, 300);
                } else {
                    this.num = 0;
                }
            } else if (msg.what == 2) {
                if (LoadingDialog.this._BindCallback != null) {
                    LoadingDialog.this._BindCallback.OnClick(LoadingDialog.this._BindCommand, LoadingDialog.this.handler);
                }
            } else if (msg.what == 0) {
                LoadingDialog.this.dismiss();
            } else if (msg.what == 3 && msg.obj != null) {
                LoadingDialog.this.f509tv.setText(String.valueOf(msg.obj));
            }
        }
    };
    private ImageView iv_route;
    private RotateAnimation mAnim;

    /* renamed from: tv */
    private TextView f509tv;
    private TextView tv_point;

    public void setBindCallback(ICallback callback) {
        this._BindCallback = callback;
    }

    public void setBindCallback(ICallback callback, String bindCommand) {
        this._BindCallback = callback;
        this._BindCommand = bindCommand;
    }

    public Handler getHandler() {
        return this.handler;
    }

    public LoadingDialog(Context context) {
        super(context, R.style.Loading_Dialog);
        init();
    }

    @SuppressLint("ResourceType")
    private void init() {
        setContentView(View.inflate(getContext(), R.layout.loadingdialog, null));
        setCanceledOnTouchOutside(false);
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams params = dialogWindow.getAttributes();
        params.width = -2;
        params.height = -2;
        dialogWindow.setAttributes(params);
        this.iv_route = (ImageView) findViewById(R.id.iv_route);
//        this.f509tv = (TextView) findViewById(R.id.f565tv);
        this.tv_point = (TextView) findViewById(R.id.tv_point);
        initAnim();
        getWindow().setWindowAnimations(R.anim.loadingdialog_anim);
    }

    private void initAnim() {
        this.mAnim = new RotateAnimation(360.0f, 0.0f, 1, 0.5f, 1, 0.5f);
        this.mAnim.setDuration(1000);
        this.mAnim.setRepeatCount(-1);
        this.mAnim.setRepeatMode(1);
        this.mAnim.setStartTime(-1);
    }

    @Override // android.app.Dialog
    public void show() {
        this.iv_route.startAnimation(this.mAnim);
        this.handler.sendEmptyMessage(1);
        super.show();
    }

    public void showDialog() {
        this.iv_route.startAnimation(this.mAnim);
        this.handler.sendEmptyMessage(1);
        setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.LoadingDialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Message tempMsg2 = LoadingDialog.this.handler.obtainMessage();
                tempMsg2.what = 2;
                LoadingDialog.this.handler.sendMessage(tempMsg2);
            }
        });
        super.show();
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public void dismiss() {
        this.mAnim.cancel();
        super.dismiss();
    }

    @Override // android.app.Dialog
    public void setCancelable(boolean flag) {
        this.cancelable = flag;
        super.setCancelable(flag);
    }

    @Override // android.app.Dialog
    public void setTitle(CharSequence title) {
//        this.f509tv.setText(title);
    }

    @Override // android.app.Dialog
    public void setTitle(int titleId) {
        setTitle(getContext().getString(titleId));
    }
}
