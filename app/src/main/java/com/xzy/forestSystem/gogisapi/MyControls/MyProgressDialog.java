package  com.xzy.forestSystem.gogisapi.MyControls;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.stczh.gzforestSystem.R;

public class MyProgressDialog extends Dialog {
    private static MyProgressDialog _ProgressDialog = null;
    private Context context = null;
    private TextView headTextView = null;
    public boolean isCancel = false;
    private TextView msgTextView = null;
    public Handler myHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.MyProgressDialog.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Object[] tmpObjs;
            if (msg.what == 0) {
                if (MyProgressDialog._ProgressDialog != null) {
                    MyProgressDialog._ProgressDialog.dismiss();
                    MyProgressDialog._ProgressDialog = null;
                }
            } else if (msg.what == 1) {
                MyProgressDialog.this.SetHeadTitle(msg.obj.toString());
            } else if (msg.what == 2) {
                MyProgressDialog.this.SetMessage(msg.obj.toString());
            } else if (msg.what == 3 && (tmpObjs = (Object[]) msg.obj) != null && tmpObjs.length > 1) {
                MyProgressDialog.this.SetHeadTitle(tmpObjs[0].toString());
                MyProgressDialog.this.SetMessage(tmpObjs[1].toString());
            }
        }
    };
    private Handler returnHandler = null;

    public MyProgressDialog(Context context2) {
        super(context2);
        this.context = context2;
        setContentView(R.layout.myprogress_dialog);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = -2;
        layoutParams.height = -2;
        window.setAttributes(layoutParams);
        this.headTextView = (TextView) findViewById(R.id.headerMsg);
        this.msgTextView = (TextView) findViewById(R.id.textViewMsg);
        setCancelable(false);
    }

    public MyProgressDialog(Context context2, int theme) {
        super(context2, theme);
        setContentView(R.layout.myprogress_dialog);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = -1;
        layoutParams.height = -2;
        window.setAttributes(layoutParams);
        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        Display localDisplay = ((Activity) PubVar._PubCommand.m_Context).getWindowManager().getDefaultDisplay();
        localLayoutParams.x = 0;
        localLayoutParams.y = 0;
        if (PubVar.m_SCREEN_ORIENTATION == 0) {
            localLayoutParams.width = (int) (((double) localDisplay.getWidth()) * 0.9d);
        }
        localLayoutParams.width = (int) (((double) localDisplay.getWidth()) * 0.9d);
        getWindow().setAttributes(localLayoutParams);
        setCancelable(false);
        this.headTextView = (TextView) findViewById(R.id.headerMsg);
        this.msgTextView = (TextView) findViewById(R.id.textViewMsg);
    }

    public static MyProgressDialog createDialog(Context context2) {
        _ProgressDialog = new MyProgressDialog(context2, R.style.CustomProgressDialog);
        return _ProgressDialog;
    }

    public void SetReturnHandler(Handler handler) {
        this.returnHandler = handler;
    }

    public void SetHeadTitle(String head) {
        if (this.headTextView != null) {
            this.headTextView.setText(head);
        }
    }

    public void SetMessage(String msg) {
        if (this.msgTextView != null) {
            this.msgTextView.setText(msg);
        }
    }
}
