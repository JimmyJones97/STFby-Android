package  com.xzy.forestSystem.gogisapi.MyControls;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;

public class XDialogTemplate extends Dialog {
    public Context Context;
    private boolean _AllowedCloseDialog;
    private ICallback _returnCallback;
    private String _returnCommand;

    public XDialogTemplate(Context paramContext) {
        this(paramContext, -1);
    }

    public XDialogTemplate(Context paramContext, int theme) {
        super(paramContext);
        this.Context = null;
        this._returnCallback = null;
        this._returnCommand = "";
        this._AllowedCloseDialog = true;
        setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate.1
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
            }
        });
        this.Context = paramContext;
        requestWindowFeature(1);
        HideSoftInputMode();
        setContentView(R.layout.dialog_template);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = -1;
        layoutParams.height = -1;
        window.setAttributes(layoutParams);
        setCanceledOnTouchOutside(false);
        findViewById(R.id.buttonCancel).setOnClickListener(new ViewClick());
    }

    public void HideKeybroad() {
        getWindow().setSoftInputMode(2);
    }

    public void SetHeightWrapContent() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.height = -2;
        getWindow().setAttributes(layoutParams);
    }

    public void SetWidthHeightWrapContent() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = -2;
        layoutParams.height = -2;
        getWindow().setAttributes(layoutParams);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String paramString) {
        if (paramString.equals("返回")) {
            if (this._returnCallback != null) {
                this._returnCallback.OnClick("返回", "");
            }
            if (this._AllowedCloseDialog) {
                dismiss();
            }
        } else if (paramString.equals("点击标题")) {
            hide();
        }
    }

    public View GetButton(String paramString) {
        int i = -1;
        if (paramString.equals("1")) {
            i = R.id.btn_dialog_template_head04;
        } else if (paramString.equals("2")) {
            i = R.id.btn_dialog_template_head03;
        } else if (paramString.equals("3")) {
            i = R.id.btn_dialog_template_head02;
        } else if (paramString.equals("4")) {
            i = R.id.btn_dialog_template_head01;
        }
        return findViewById(i);
    }

    public void HideSoftInputMode() {
        getWindow().setSoftInputMode(0);
    }

    public void HideHeadBar() {
        findViewById(R.id.rl_maintoolbar).setVisibility(8);
    }

    public void Resize(float widthRatio, float heightRatio) {
        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        Display localDisplay = ((Activity) PubVar._PubCommand.m_Context).getWindowManager().getDefaultDisplay();
        localLayoutParams.x = 0;
        localLayoutParams.y = 0;
        if (PubVar.m_SCREEN_ORIENTATION == 0) {
            if (heightRatio != 0.0f) {
                localLayoutParams.height = (int) (((float) localDisplay.getHeight()) * heightRatio);
            }
            if (widthRatio != 0.0f) {
                localLayoutParams.width = (int) (((float) localDisplay.getWidth()) * widthRatio);
            }
        }
        localLayoutParams.width = (int) (((float) localDisplay.getWidth()) * widthRatio);
        localLayoutParams.height = (int) (((float) localDisplay.getHeight()) * heightRatio);
        getWindow().setAttributes(localLayoutParams);
    }

    public void SetHeadButtons(String paramString, final ICallback tmpICallback) {
        int i = R.id.formtemp_1;
        String[] arrayOfString = paramString.split(",");
        if (arrayOfString[0].equals("1")) {
            i = R.id.btn_dialog_template_head04;
        } else if (arrayOfString[0].equals("2")) {
            i = R.id.btn_dialog_template_head03;
        } else if (arrayOfString[0].equals("3")) {
            i = R.id.btn_dialog_template_head02;
        } else if (arrayOfString[0].equals("4")) {
            i = R.id.btn_dialog_template_head01;
        }
        View localButton = findViewById(i);
        int j = Integer.parseInt(arrayOfString[1]);
        if (j != -1) {
            localButton.setVisibility(0);
            Drawable localDrawable = this.Context.getResources().getDrawable(j);
            localDrawable.setBounds(0, 0, localDrawable.getMinimumWidth(), localDrawable.getMinimumHeight());
            if (localButton instanceof Button) {
                ((Button) localButton).setCompoundDrawables(localDrawable, null, null, null);
            } else if (localButton instanceof ImageButton) {
                ((ImageButton) localButton).setImageDrawable(localDrawable);
            }
        }
        if (localButton instanceof Button) {
            ((Button) localButton).setText(arrayOfString[2]);
        }
        localButton.setTag(arrayOfString[3]);
        localButton.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate.2
            @Override // android.view.View.OnClickListener
            public void onClick(View paramView) {
                Object tempObj;
                if (XDialogTemplate.this != null && (tempObj = paramView.getTag()) != null) {
                    tmpICallback.OnClick(tempObj.toString(), "");
                }
            }
        });
    }

    public void SetCallback(ICallback tmpICallback) {
        this._returnCallback = tmpICallback;
    }

    public void SetCaption(String paramString) {
        ((TextView) findViewById(R.id.headerbar)).setText(" " + paramString);
    }

    public void SetLayoutView(int paramInt) {
        SetView(((LayoutInflater) this.Context.getSystemService("layout_inflater")).inflate(paramInt, (ViewGroup) null, false));
    }

    public void SetView(View paramView) {
        LinearLayout localLinearLayout = (LinearLayout) findViewById(R.id.ll_mainLayout);
        localLinearLayout.addView(paramView, localLinearLayout.getLayoutParams());
    }

    public View getMainView() {
        return findViewById(R.id.ll_dialogForm);
    }

    public void SetAllowedCloseDialog(boolean value) {
        this._AllowedCloseDialog = value;
    }

    public void SetCancelCommand(String command) {
        setCancelable(false);
        this._returnCommand = command;
    }

    public void SetDismissCallback(final ICallback pCallback) {
        if (pCallback != null) {
            setOnDismissListener(new DialogInterface.OnDismissListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate.3
                @Override // android.content.DialogInterface.OnDismissListener
                public void onDismiss(DialogInterface arg0) {
                    pCallback.OnClick("对话框关闭事件", null);
                }
            });
        }
    }

    public void SetDismissCallback(final ICallback pCallback, final String returnTag) {
        if (pCallback != null) {
            setOnDismissListener(new DialogInterface.OnDismissListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate.4
                @Override // android.content.DialogInterface.OnDismissListener
                public void onDismiss(DialogInterface arg0) {
                    pCallback.OnClick(returnTag, null);
                }
            });
        }
    }

    public void SetHeadAndBtnVisible(boolean visible) {
        int tmpV = 8;
        if (visible) {
            tmpV = 0;
        }
        findViewById(R.id.headerbar).setVisibility(tmpV);
        findViewById(R.id.buttonCancel).setVisibility(tmpV);
    }

    public Bitmap GetScreenShot() {
        return Common.GetBitmapFromView(getMainView());
    }

    public String getCaption() {
        return ((TextView) findViewById(R.id.headerbar)).getText().toString();
    }

    public void setFullScreen() {
        getWindow().setLayout(-1, -1);
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View paramView) {
            XDialogTemplate.this.DoCommand(paramView.getTag().toString());
        }
    }
}
