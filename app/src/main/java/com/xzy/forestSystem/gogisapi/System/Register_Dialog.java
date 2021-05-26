package  com.xzy.forestSystem.gogisapi.System;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.Img_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Others.MediaActivity;
import  com.xzy.forestSystem.gogisapi.QRCode.EncodingHandler;
import com.stczh.gzforestSystem.R;
import java.util.Date;
import java.util.HashMap;

public class Register_Dialog {
    private XDialogTemplate _Dialog;
    private int m_AllowTryCheckTime;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public Register_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.System.Register_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                try {
                    if (paramString.contains("选择文件")) {
                        String[] tempPath2 = pObject.toString().split(";");
                        if (tempPath2 != null && tempPath2.length > 1) {
                            String tempCode2 = Common.ReadTxtFileContent(tempPath2[1]);
                            if (tempCode2 == null || tempCode2.equals("")) {
                                Common.ShowDialog("授权信息无效.");
                                return;
                            }
                            Common.SetEditTextValueOnID(Register_Dialog.this._Dialog, R.id.et_registerKey, tempCode2);
                            Register_Dialog.this.DoCommand("开始注册");
                        }
                    } else if (paramString.equals("分享")) {
                        Register_Dialog.this.DoCommand("分享注册号");
                    } else if (paramString.equals("QRResult")) {
                        String tempkey = pObject.toString();
                        String[] tmpStrings = tempkey.split(":");
                        String tmpResultString = "";
                        if (tmpStrings != null) {
                            if (tmpStrings.length > 1) {
                                tmpResultString = tmpStrings[1];
                            } else if (tmpStrings.length == 1) {
                                tmpResultString = tmpStrings[0];
                            }
                        }
                        if (tmpResultString.length() == 0) {
                            tmpResultString = tempkey;
                        }
                        Common.SetEditTextValueOnID(Register_Dialog.this._Dialog, R.id.et_registerKey, tmpResultString);
                        Register_Dialog.this.DoCommand("开始注册");
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_AllowTryCheckTime = 3;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.register_dialog);
        this._Dialog.Resize(0.96f, PubVar.DialogHeightRatio);
        this._Dialog.SetCaption("系统注册");
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetHeadButtons("1,2130837935,分享,分享", this.pCallback);
        this._Dialog.findViewById(R.id.bt_saveRegisterCode).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.bt_copyRegisterCode).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.bt_startRegister).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.bt_loadRegister).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.bt_buildQRCode).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.bt_buildQRCode2).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.et_registerCode).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.System.Register_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Common.CopyText(Common.GetEditTextValueOnID(Register_Dialog.this._Dialog, R.id.et_registerCode));
                Common.ShowToast("复制注册码成功!");
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        Common.SetEditTextValueOnID(this._Dialog, R.id.et_registerCode, PubVar.m_AuthorizeTools.GetRegisterCode());
        Common.AddTimeRecord(new Date());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("保存注册号")) {
            String tmpPath = String.valueOf(PubVar.m_SystemPath) + FileSelector_Dialog.sRoot + PubVar.AppName + "V" + PubVar.VersionNumber + "_注册号_" + Common.fileDateFormat.format(new Date()) + ".reg";
            Common.SaveTextFile(tmpPath, Common.GetEditTextValueOnID(this._Dialog, R.id.et_registerCode));
            Common.ShowDialog("已保存注册号信息到:\r\n" + tmpPath);
        } else if (command.equals("复制注册号")) {
            Common.CopyText(Common.GetEditTextValueOnID(this._Dialog, R.id.et_registerCode));
            Common.ShowToast("已经将注册号复制到粘贴板中.");
        } else if (command.equals("分享注册号")) {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType("text/plain");
            intent.putExtra("android.intent.extra.SUBJECT", "Share");
            intent.putExtra("android.intent.extra.TEXT", Common.GetEditTextValueOnID(this._Dialog, R.id.et_registerCode));
            intent.setFlags(268435456);
            PubVar.MainContext.startActivity(Intent.createChooser(intent, "我的注册号 " + PubVar.AppName));
        } else if (command.equals("生成二维码")) {
            Bitmap tmpBitmap = EncodingHandler.createQRCode(Common.GetEditTextValueOnID(this._Dialog, R.id.et_registerCode), 400, 400);
            if (tmpBitmap != null) {
                Img_Dialog tmpDialog = new Img_Dialog();
                tmpDialog.SetTitle("注册二维码");
                tmpDialog.SetImage(tmpBitmap);
                tmpDialog.ShowDialog();
            }
        } else if (command.equals("扫描二维码")) {
            Intent intent2 = new Intent();
            intent2.setClass(this._Dialog.getContext(), MediaActivity.class);
            intent2.putExtra("Type", 2);
            intent2.putExtra("title", "扫描授权码");
            MediaActivity.BindCallbak = this.pCallback;
            this._Dialog.getContext().startActivity(intent2);
        } else if (command.equals("开始注册")) {
            String tempkey = Common.GetEditTextValueOnID(this._Dialog, R.id.et_registerKey);
            if (tempkey == null || tempkey.equals("")) {
                Common.ShowDialog("授权码不能为空.");
            } else {
                String[] tempOutMsg = new String[1];
                PubVar.m_AuthorizeTools.CheckRegister(tempkey, tempOutMsg);
                if (PubVar.m_AuthorizeTools.getRegisterMode() == 2) {
                    Common.AddTimeRecord(new Date());
                    SharedPreferences.Editor pEditor = PreferenceManager.getDefaultSharedPreferences(PubVar.MainContext).edit();
                    pEditor.putString("Values", tempkey);
                    pEditor.commit();
                    HashMap tempHash01 = new HashMap();
                    tempHash01.put("F2", tempkey);
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_LIC", (HashMap<String, String>) tempHash01);
                    Common.ShowDialog("恭喜您注册成功!\r\n软件可使用至" + Common.dateFormat.format(new Date(PubVar.m_AuthorizeTools.GetExpireDate())));
                    if (this.m_Callback != null) {
                        this.m_Callback.OnClick("注册返回", null);
                    }
                    this._Dialog.dismiss();
                } else {
                    Common.ShowDialog("注册失败!\r\n" + tempOutMsg[0]);
                }
            }
            if (this.m_AllowTryCheckTime == 0) {
                this._Dialog.dismiss();
            }
            this.m_AllowTryCheckTime--;
        } else if (command.equals("导入授权码")) {
            FileSelector_Dialog tempDialog = new FileSelector_Dialog(".lic;", false);
            Common.ShowToast("请选择授权码文件.");
            tempDialog.SetTitle("请选择授权码文件");
            tempDialog.SetCallback(this.pCallback);
            tempDialog.ShowDialog();
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.System.Register_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Register_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                Register_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
