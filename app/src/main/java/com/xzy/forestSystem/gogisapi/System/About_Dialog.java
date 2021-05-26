package  com.xzy.forestSystem.gogisapi.System;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import androidx.core.internal.view.SupportMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.Date;

public class About_Dialog {
    private XDialogTemplate _Dialog;
    TextView m_AuthTypeTxt;
    private ICallback m_Callback;
    Button m_RegisterBtn;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public About_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_RegisterBtn = null;
        this.m_AuthTypeTxt = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.System.About_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("分享")) {
                    Intent intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    intent.putExtra("android.intent.extra.SUBJECT", "Share");
                    intent.putExtra("android.intent.extra.TEXT", "我分享一个功能强大的专业移动GIS软件给大家.\r\n" + PubVar.ServerURL + FileSelector_Dialog.sRoot + PubVar.AppName + ".apk");
                    intent.setFlags(268435456);
                    PubVar.MainContext.startActivity(Intent.createChooser(intent, "分享 " + PubVar.AppName));
                } else if (paramString.equals("注册返回")) {
                    About_Dialog.this.refreshRegInfo();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.aboutsystem_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("关于系统");
        this._Dialog.SetHeadButtons("1,2130837935,分享,分享", this.pCallback);
        ((Button) this._Dialog.findViewById(R.id.bt_aboutSystem_DeviceInfo)).setOnClickListener(new ViewClick());
        this.m_RegisterBtn = (Button) this._Dialog.findViewById(R.id.bt_register);
        this.m_RegisterBtn.setOnClickListener(new ViewClick());
        this.m_AuthTypeTxt = (TextView) this._Dialog.findViewById(R.id.tv_authourType);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshRegInfo() {
        StringBuilder tempSB = new StringBuilder();
        tempSB.append("版本号: ");
        tempSB.append(PubVar.Version);
        tempSB.append("\r\n贵州晟泰工程咨询有限公司");
        tempSB.append("\r\n目    录: " + PubVar.m_SystemPath);
        tempSB.append("\r\n型    号: ");
        tempSB.append(Build.MANUFACTURER);
        tempSB.append("," + Build.MODEL + "," + Build.VERSION.RELEASE);
        tempSB.append("\r\n设备ID: ");
        tempSB.append(PubVar.m_AuthorizeTools.getUserAndroidID());
        int tmpRegMode = PubVar.m_AuthorizeTools.getRegisterMode();
        if (tmpRegMode == 2) {
            this.m_AuthTypeTxt.setText(String.valueOf("授权类型：已注册") + "【使用至" + Common.dateFormat.format(new Date(PubVar.m_AuthorizeTools.GetExpireDate())) + "】");
            this.m_AuthTypeTxt.setTextColor(-16776961);
            this.m_RegisterBtn.setText("重新注册");
            this.m_RegisterBtn.setTag("重新注册");
        } else if (tmpRegMode == 0) {
            this.m_AuthTypeTxt.setText("授权类型：未授权(试用版)");
            this.m_AuthTypeTxt.setTextColor(SupportMenu.CATEGORY_MASK);
            this.m_RegisterBtn.setText("注册");
            this.m_RegisterBtn.setTag("注册");
        } else if (tmpRegMode == 1) {
            String tmpMsg = "授权类型：网络注册版";
            int tmpLimHours = PubVar.m_AuthorizeTools.getNetRegLimitHour();
            if (tmpLimHours > 0) {
                if (tmpLimHours > 24) {
                    tmpMsg = String.valueOf(tmpMsg) + "【可使用" + String.valueOf(tmpLimHours / 24) + "天】";
                } else {
                    tmpMsg = String.valueOf(tmpMsg) + "【可使用" + String.valueOf(tmpLimHours) + "小时】";
                }
            }
            this.m_AuthTypeTxt.setText(tmpMsg);
            this.m_AuthTypeTxt.setTextColor(SupportMenu.CATEGORY_MASK);
            this.m_RegisterBtn.setText("授权码注册");
            this.m_RegisterBtn.setTag("授权码注册");
        }
        Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_systemInfo, tempSB.toString());
    }

    public void setReturnTag(ICallback pCallback2, String returnTag) {
        this._Dialog.SetDismissCallback(pCallback2, returnTag);
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.System.About_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                if (PubVar.m_AuthorizeTools.m_HasNetReg && PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                    PubVar.m_AuthorizeTools.NetUserConnectServer();
                }
                About_Dialog.this.refreshRegInfo();
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("检查最新版本")) {
            PubVar._PubCommand.ProcessCommand("系统更新");
        } else if (command.equals("重新注册")) {
            Register_Dialog tempDialog = new Register_Dialog();
            tempDialog.SetCallback(this.pCallback);
            tempDialog.ShowDialog();
        } else if (command.equals("授权码注册")) {
            Register_Dialog tempDialog2 = new Register_Dialog();
            tempDialog2.SetCallback(this.pCallback);
            tempDialog2.ShowDialog();
        } else if (command.equals("设备信息共享")) {
            new DeviceInfo_Dialog().ShowDialog();
        } else if (command.equals("注册")) {
            Register_Dialog tempDialog3 = new Register_Dialog();
            tempDialog3.SetCallback(this.pCallback);
            tempDialog3.ShowDialog();
        } else if (command.equals("联系我们")) {
            ContactUS_Dialog tempDialog4 = new ContactUS_Dialog();
            tempDialog4.SetCallback(this.pCallback);
            tempDialog4.ShowDialog();
        }
    }

    public void SetReturnCallback(ICallback _Callback) {
        this._Dialog.setCancelable(false);
        this._Dialog.SetCallback(_Callback);
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                About_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
