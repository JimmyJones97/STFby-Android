package  com.xzy.forestSystem.gogisapi.MyControls;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.stczh.gzforestSystem.R;

public class CustomeProgressDialog extends Dialog {
    private static CustomeProgressDialog customProgressDialog = null;
    private static ProgressBar progressBar = null;
    private Context context = null;
    private TextView headTextView = null;
    private TextView infoTextView = null;
    public boolean isCancel = false;
    public Handler myHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            Object[] tempObjs;
            if (msg.what == 0) {
                if (CustomeProgressDialog.customProgressDialog != null) {
                    CustomeProgressDialog.customProgressDialog.dismiss();
                    CustomeProgressDialog.customProgressDialog = null;
                }
            } else if (msg.what == 1) {
                CustomeProgressDialog.this.SetProgressTitle(msg.obj.toString());
            } else if (msg.what == 2) {
                CustomeProgressDialog.this.SetProgressInfo(msg.obj.toString());
            } else if (msg.what == 3) {
                CustomeProgressDialog.this.SetProgressValue(Integer.parseInt(msg.obj.toString()));
            } else if (msg.what == 4) {
                Object[] tmpObjs = (Object[]) msg.obj;
                CustomeProgressDialog.this.SetProgressTitle(tmpObjs[0].toString());
                CustomeProgressDialog.this.SetProgressInfo(tmpObjs[1].toString());
            } else if (msg.what == 5) {
                Object[] tempObjs2 = (Object[]) msg.obj;
                if (tempObjs2 != null && tempObjs2.length > 1) {
                    String tempStr = tempObjs2[0].toString();
                    int tempInt = Integer.parseInt(tempObjs2[1].toString());
                    CustomeProgressDialog.this.SetProgressInfo(tempStr);
                    CustomeProgressDialog.this.SetProgressValue(tempInt);
                }
            } else if (msg.what == 6 && (tempObjs = (Object[]) msg.obj) != null && tempObjs.length > 2) {
                CustomeProgressDialog.this.SetProgressTitle(tempObjs[0].toString());
                int tempInt2 = Integer.parseInt(tempObjs[1].toString());
                CustomeProgressDialog.this.SetProgressInfo(tempObjs[2].toString());
                CustomeProgressDialog.this.SetProgressValue(tempInt2);
            }
        }
    };
    private Handler returnHandler = null;
    public int returnHandlerCode = 100;
    private TextView titleTextView = null;

    public CustomeProgressDialog(Context context2) {
        super(context2);
        this.context = context2;
        setContentView(R.layout.customprogressdialog);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = -1;
        layoutParams.height = -2;
        window.setAttributes(layoutParams);
        setCancelable(false);
    }

    public CustomeProgressDialog(Context context2, int theme) {
        super(context2, theme);
        setContentView(R.layout.customprogressdialog);
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
        this.headTextView = (TextView) findViewById(R.id.textViewTitle);
        this.titleTextView = (TextView) findViewById(R.id.textViewProgTitle);
        this.infoTextView = (TextView) findViewById(R.id.textViewProgInfo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setProgress(0);
        ((Button) findViewById(R.id.buttonCancel)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                CustomeProgressDialog.this.isCancel = true;
                if (CustomeProgressDialog.this.returnHandler != null) {
                    Message msg = new Message();
                    msg.what = CustomeProgressDialog.this.returnHandlerCode;
                    CustomeProgressDialog.this.returnHandler.sendMessage(msg);
                }
            }
        });
    }

    public static CustomeProgressDialog createDialog(Context context2) {
        customProgressDialog = new CustomeProgressDialog(context2, R.style.CustomProgressDialog);
        return customProgressDialog;
    }

    public void SetReturnHandler(Handler handler) {
        this.returnHandler = handler;
    }

    public void SetHeadTitle(String head) {
        if (this.headTextView != null) {
            this.headTextView.setText(head);
        }
    }

    public void SetProgressTitle(String title) {
        if (this.titleTextView != null) {
            this.titleTextView.setText(title);
        }
    }

    public void SetProgressInfo(String msg) {
        if (this.infoTextView != null) {
            this.infoTextView.setText(msg);
        }
    }

    public void SetProgressValue(int value) {
        if (progressBar != null) {
            progressBar.setProgress(value);
        }
    }
}
