package  com.xzy.forestSystem.gogisapi.Common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.stczh.gzforestSystem.R;

public class InfoDialog extends Dialog {
    private static InfoDialog MyInfoDialog = null;
    private Context context = null;
    private TextView headTextView = null;
    public boolean isCancel = false;
    private TextView msgTextView = null;
    public Handler myHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Common.InfoDialog.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Object[] tmpObjs;
            if (msg.what == 0) {
                if (InfoDialog.MyInfoDialog != null) {
                    InfoDialog.MyInfoDialog.dismiss();
                    InfoDialog.MyInfoDialog = null;
                }
            } else if (msg.what == 1) {
                InfoDialog.this.SetHeadTitle(msg.obj.toString());
            } else if (msg.what == 2) {
                InfoDialog.this.SetMessage(msg.obj.toString());
            } else if (msg.what == 3 && (tmpObjs = (Object[]) msg.obj) != null && tmpObjs.length > 1) {
                InfoDialog.this.SetHeadTitle(tmpObjs[0].toString());
                InfoDialog.this.SetMessage(tmpObjs[1].toString());
            }
        }
    };
    private Handler returnHandler = null;

    public InfoDialog(Context context2) {
        super(context2);
        this.context = context2;
        setContentView(R.layout.x_info_dialog);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = -2;
        layoutParams.height = -2;
        window.setAttributes(layoutParams);
        this.headTextView = (TextView) findViewById(R.id.tv_headerMsg);
        this.msgTextView = (TextView) findViewById(R.id.tv_textViewMsg);
        ((Button) findViewById(R.id.btn_infoDialog_OK)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.InfoDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                Message msg = InfoDialog.this.myHandler.obtainMessage();
                msg.what = 0;
                InfoDialog.this.myHandler.sendMessage(msg);
            }
        });
    }

    public InfoDialog(Context context2, int theme) {
        super(context2, theme);
        setContentView(R.layout.x_info_dialog);
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
        this.headTextView = (TextView) findViewById(R.id.tv_headerMsg);
        this.msgTextView = (TextView) findViewById(R.id.tv_textViewMsg);
        ((Button) findViewById(R.id.btn_infoDialog_OK)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.InfoDialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                Message msg = InfoDialog.this.myHandler.obtainMessage();
                msg.what = 0;
                InfoDialog.this.myHandler.sendMessage(msg);
            }
        });
    }

    public static InfoDialog createDialog(Context context2) {
        MyInfoDialog = new InfoDialog(context2, R.style.CustomProgressDialog);
        return MyInfoDialog;
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
