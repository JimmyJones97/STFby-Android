package  com.xzy.forestSystem.gogisapi.Encryption;

import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import  com.xzy.forestSystem.qihoo.jiagutracker.C0246Config;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.System.AuthorizeTools;
import com.stczh.gzforestSystem.R;
import java.io.File;

public class Encrypt_Setting_Dialog {
    private XDialogTemplate _Dialog = null;
    private ENCRYPT_SET_TYPE _EncryptSetType = ENCRYPT_SET_TYPE.Create;
    private String _ProjectName = "";
    private String _ProjectPath = "";
    private ICallback m_Callback = null;
    private String m_ReturnTag = "";
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.Encrypt_Setting_Dialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String command, Object object) {
            try {
                if (command.equals("设置密码")) {
                    String tmpStr01 = Common.GetEditTextValueOnID(Encrypt_Setting_Dialog.this._Dialog, R.id.et_encryptSet_Create01);
                    String tmpStr02 = Common.GetEditTextValueOnID(Encrypt_Setting_Dialog.this._Dialog, R.id.et_encryptSet_Create02);
                    if (tmpStr01 == null || tmpStr01.equals(C0246Config.EMPTY_STRING)) {
                        Common.ShowDialog("输入密码不能为空.");
                    } else if (!tmpStr01.equals(tmpStr02)) {
                        Common.ShowDialog("两次输入的密码不一致.");
                    } else {
                        String tmpFilePath01 = String.valueOf(Encrypt_Setting_Dialog.this._ProjectPath) + "/Project.dbx";
                        String tmpFilePath02 = String.valueOf(Encrypt_Setting_Dialog.this._ProjectPath) + "/TAData.dbx";
                        File tmpFile01 = new File(tmpFilePath01);
                        File tmpFile02 = new File(tmpFilePath02);
                        if (!tmpFile01.exists() || !tmpFile02.exists()) {
                            Common.ShowDialog("项目文件不存在.\r\n" + tmpFilePath01 + "\r\n" + tmpFilePath02);
                            Encrypt_Setting_Dialog.this._Dialog.dismiss();
                            return;
                        }
                        String tmpFilePath012 = String.valueOf(Encrypt_Setting_Dialog.this._ProjectPath) + "/Project.dbxbak";
                        String tmpFilePath022 = String.valueOf(Encrypt_Setting_Dialog.this._ProjectPath) + "/TAData.dbxbak";
                        File tmpFile03 = new File(tmpFilePath012);
                        File tmpFile04 = new File(tmpFilePath022);
                        if (tmpFile03.exists()) {
                            tmpFile03.delete();
                        }
                        if (tmpFile04.exists()) {
                            tmpFile04.delete();
                        }
                        Common.CopyFile(tmpFilePath01, tmpFilePath012);
                        Common.CopyFile(tmpFilePath02, tmpFilePath022);
                        boolean tmpBool01 = C0433Common.CreatePassword(PubVar.MainContext, tmpFilePath01, tmpStr01);
                        boolean tmpBool02 = C0433Common.CreatePassword(PubVar.MainContext, tmpFilePath02, tmpStr01);
                        if (!tmpBool01 || !tmpBool02) {
                            Common.ShowDialog("项目数据加密失败.");
                            if (tmpFile03.exists()) {
                                if (tmpFile01.exists()) {
                                    tmpFile01.delete();
                                }
                                tmpFile03.renameTo(new File(tmpFilePath01));
                            }
                            if (tmpFile04.exists()) {
                                if (tmpFile02.exists()) {
                                    tmpFile02.delete();
                                }
                                tmpFile04.renameTo(new File(tmpFilePath02));
                            }
                            Encrypt_Setting_Dialog.this._Dialog.dismiss();
                            return;
                        }
                        PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().ExecuteSQL("Update T_Project Set Security='" + AuthorizeTools.EncryptPrjPwd(tmpStr01, Common.GetCheckBoxValueOnID(Encrypt_Setting_Dialog.this._Dialog, R.id.ck_encryptSet_Modify)) + "' Where ProjectName='" + Encrypt_Setting_Dialog.this._ProjectName + "'");
                        Common.ShowDialog("项目数据加密成功.");
                        File tmpFile032 = new File(tmpFilePath012);
                        File tmpFile042 = new File(tmpFilePath022);
                        if (tmpFile032.exists()) {
                            tmpFile032.delete();
                        }
                        if (tmpFile042.exists()) {
                            tmpFile042.delete();
                        }
                        if (Encrypt_Setting_Dialog.this.m_Callback != null) {
                            Encrypt_Setting_Dialog.this.m_Callback.OnClick(Encrypt_Setting_Dialog.this.m_ReturnTag, null);
                        }
                        Encrypt_Setting_Dialog.this._Dialog.dismiss();
                    }
                } else if (command.equals("修改密码")) {
                    String tmpStr012 = Common.GetEditTextValueOnID(Encrypt_Setting_Dialog.this._Dialog, R.id.et_encryptSet_Modify02);
                    String tmpStr022 = Common.GetEditTextValueOnID(Encrypt_Setting_Dialog.this._Dialog, R.id.et_encryptSet_Modify03);
                    if (tmpStr012 == null || tmpStr012.equals(C0246Config.EMPTY_STRING)) {
                        Common.ShowDialog("输入密码不能为空.");
                    } else if (!tmpStr012.equals(tmpStr022)) {
                        Common.ShowDialog("两次输入的密码不一致.");
                    } else {
                        String tmpFilePath013 = String.valueOf(Encrypt_Setting_Dialog.this._ProjectPath) + "/Project.dbx";
                        String tmpFilePath023 = String.valueOf(Encrypt_Setting_Dialog.this._ProjectPath) + "/TAData.dbx";
                        File tmpFile012 = new File(tmpFilePath013);
                        File tmpFile022 = new File(tmpFilePath023);
                        if (!tmpFile012.exists() || !tmpFile022.exists()) {
                            Common.ShowDialog("项目文件不存在.\r\n" + tmpFilePath013 + "\r\n" + tmpFilePath023);
                            Encrypt_Setting_Dialog.this._Dialog.dismiss();
                            return;
                        }
                        String tmpOrigPwd = Common.GetEditTextValueOnID(Encrypt_Setting_Dialog.this._Dialog, R.id.et_encryptSet_Modify01);
                        BasicValue tmpKeyInfo = new BasicValue();
                        if (Common.GetProjectEncryptInfo(Encrypt_Setting_Dialog.this._ProjectName, tmpKeyInfo)) {
                            BasicValue tmpOutMsg = new BasicValue();
                            BasicValue tmpPwd = new BasicValue();
                            if (!AuthorizeTools.DecryptPrjPwd(tmpKeyInfo.getString(), tmpOutMsg, tmpPwd)) {
                                Common.ShowDialog(tmpOutMsg.getString());
                                Encrypt_Setting_Dialog.this._Dialog.dismiss();
                            } else if (tmpOrigPwd.equals(tmpPwd.getString())) {
                                boolean tmpBool012 = C0433Common.ModifyPassword(PubVar.MainContext, tmpFilePath013, tmpOrigPwd, tmpStr012);
                                boolean tmpBool022 = C0433Common.ModifyPassword(PubVar.MainContext, tmpFilePath023, tmpOrigPwd, tmpStr012);
                                if (!tmpBool012 || !tmpBool022) {
                                    Encrypt_Setting_Dialog.this._Dialog.dismiss();
                                    Common.ShowDialog("项目数据加密失败.");
                                    return;
                                }
                                PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().ExecuteSQL("Update T_Project Set Security='" + AuthorizeTools.EncryptPrjPwd(tmpStr012, Common.GetCheckBoxValueOnID(Encrypt_Setting_Dialog.this._Dialog, R.id.ck_encryptSet_Modify)) + "' Where ProjectName='" + Encrypt_Setting_Dialog.this._ProjectName + "'");
                                Common.ShowDialog("项目数据加密密码修改成功.");
                                if (Encrypt_Setting_Dialog.this.m_Callback != null) {
                                    Encrypt_Setting_Dialog.this.m_Callback.OnClick(Encrypt_Setting_Dialog.this.m_ReturnTag, null);
                                }
                                Encrypt_Setting_Dialog.this._Dialog.dismiss();
                            } else {
                                Common.ShowDialog("输入的原始密码不正确.");
                                Encrypt_Setting_Dialog.this._Dialog.dismiss();
                            }
                        } else {
                            Common.ShowDialog("获取项目【" + Encrypt_Setting_Dialog.this._ProjectName + "】的加密信息失败.该项目可能已经删除或数据未加密.");
                            Encrypt_Setting_Dialog.this._Dialog.dismiss();
                        }
                    }
                } else if (command.equals("删除密码")) {
                    String tmpOrigPwd2 = Common.GetEditTextValueOnID(Encrypt_Setting_Dialog.this._Dialog, R.id.et_encryptSet_Remove);
                    String tmpFilePath014 = String.valueOf(Encrypt_Setting_Dialog.this._ProjectPath) + "/Project.dbx";
                    String tmpFilePath024 = String.valueOf(Encrypt_Setting_Dialog.this._ProjectPath) + "/TAData.dbx";
                    File tmpFile013 = new File(tmpFilePath014);
                    File tmpFile023 = new File(tmpFilePath024);
                    if (!tmpFile013.exists() || !tmpFile023.exists()) {
                        Common.ShowDialog("项目文件不存在.\r\n" + tmpFilePath014 + "\r\n" + tmpFilePath024);
                        Encrypt_Setting_Dialog.this._Dialog.dismiss();
                        return;
                    }
                    String tmpFilePath0122 = String.valueOf(Encrypt_Setting_Dialog.this._ProjectPath) + "/Project.dbxbak2";
                    String tmpFilePath0222 = String.valueOf(Encrypt_Setting_Dialog.this._ProjectPath) + "/TAData.dbxbak2";
                    File tmpFile033 = new File(tmpFilePath0122);
                    File tmpFile043 = new File(tmpFilePath0222);
                    if (tmpFile033.exists()) {
                        tmpFile033.delete();
                    }
                    if (tmpFile043.exists()) {
                        tmpFile043.delete();
                    }
                    BasicValue tmpKeyInfo2 = new BasicValue();
                    if (Common.GetProjectEncryptInfo(Encrypt_Setting_Dialog.this._ProjectName, tmpKeyInfo2)) {
                        BasicValue tmpOutMsg2 = new BasicValue();
                        BasicValue tmpPwd2 = new BasicValue();
                        if (!AuthorizeTools.DecryptPrjPwd(tmpKeyInfo2.getString(), tmpOutMsg2, tmpPwd2)) {
                            Common.ShowDialog(tmpOutMsg2.getString());
                            Encrypt_Setting_Dialog.this._Dialog.dismiss();
                        } else if (tmpOrigPwd2.equals(tmpPwd2.getString())) {
                            Common.CopyFile(tmpFilePath014, tmpFilePath0122);
                            Common.CopyFile(tmpFilePath024, tmpFilePath0222);
                            boolean tmpBool013 = C0433Common.RemovePassword(PubVar.MainContext, tmpFilePath014, tmpOrigPwd2);
                            boolean tmpBool023 = C0433Common.RemovePassword(PubVar.MainContext, tmpFilePath024, tmpOrigPwd2);
                            if (!tmpBool013 || !tmpBool023) {
                                Common.ShowDialog("项目数据解除加密失败.");
                                if (tmpFile033.exists()) {
                                    if (tmpFile013.exists()) {
                                        tmpFile013.delete();
                                    }
                                    tmpFile033.renameTo(new File(tmpFilePath014));
                                }
                                if (tmpFile043.exists()) {
                                    if (tmpFile023.exists()) {
                                        tmpFile023.delete();
                                    }
                                    tmpFile043.renameTo(new File(tmpFilePath024));
                                }
                                Encrypt_Setting_Dialog.this._Dialog.dismiss();
                                return;
                            }
                            PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().ExecuteSQL("Update T_Project Set Security='' Where ProjectName='" + Encrypt_Setting_Dialog.this._ProjectName + "'");
                            Common.ShowDialog("项目数据解除加密成功.");
                            File tmpFile034 = new File(tmpFilePath0122);
                            File tmpFile044 = new File(tmpFilePath0222);
                            if (tmpFile034.exists()) {
                                tmpFile034.delete();
                            }
                            if (tmpFile044.exists()) {
                                tmpFile044.delete();
                            }
                            if (Encrypt_Setting_Dialog.this.m_Callback != null) {
                                Encrypt_Setting_Dialog.this.m_Callback.OnClick(Encrypt_Setting_Dialog.this.m_ReturnTag, null);
                            }
                            Encrypt_Setting_Dialog.this._Dialog.dismiss();
                        } else {
                            Common.ShowDialog("输入的原始密码不正确.");
                            Encrypt_Setting_Dialog.this._Dialog.dismiss();
                        }
                    } else {
                        Common.ShowDialog("获取项目【" + Encrypt_Setting_Dialog.this._ProjectName + "】的加密信息失败.该项目可能已经删除或数据未加密.");
                        Encrypt_Setting_Dialog.this._Dialog.dismiss();
                    }
                }
            } catch (Exception e) {
                Encrypt_Setting_Dialog.this._Dialog.dismiss();
            }
        }
    };

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public Encrypt_Setting_Dialog(String projectName, String projectPath, ENCRYPT_SET_TYPE encryptSetType) {
        this._ProjectName = projectName;
        this._ProjectPath = projectPath;
        this._EncryptSetType = encryptSetType;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.encrypt_setting_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("加密管理");
    }

    public void SetReturnTag(String tag) {
        this.m_ReturnTag = tag;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        try {
            if (this._EncryptSetType == ENCRYPT_SET_TYPE.Create) {
                this._Dialog.SetCaption("设置密码");
                this._Dialog.SetHeadButtons("1,2130837858,确定,设置密码", this.pCallback);
                this._Dialog.findViewById(R.id.ll_encryptSet_Create).setVisibility(0);
                this._Dialog.findViewById(R.id.ll_encryptSet_Modify).setVisibility(8);
                this._Dialog.findViewById(R.id.ll_encryptSet_Remove).setVisibility(8);
            } else if (this._EncryptSetType == ENCRYPT_SET_TYPE.Modify) {
                this._Dialog.SetCaption("修改密码");
                this._Dialog.SetHeadButtons("1,2130837858,修改,修改密码", this.pCallback);
                this._Dialog.findViewById(R.id.ll_encryptSet_Create).setVisibility(8);
                this._Dialog.findViewById(R.id.ll_encryptSet_Modify).setVisibility(0);
                this._Dialog.findViewById(R.id.ll_encryptSet_Remove).setVisibility(8);
                BasicValue tmpKeyInfo = new BasicValue();
                if (Common.GetProjectEncryptInfo(this._ProjectName, tmpKeyInfo)) {
                    BasicValue tmpOutMsg = new BasicValue();
                    if (AuthorizeTools.DecryptPrjPwd(tmpKeyInfo.getString(), tmpOutMsg, new BasicValue()) && tmpOutMsg.getString().equals("LOCAL")) {
                        ((CheckBox) this._Dialog.findViewById(R.id.ck_encryptSet_Modify)).setChecked(true);
                    }
                }
            } else if (this._EncryptSetType == ENCRYPT_SET_TYPE.Remove) {
                this._Dialog.SetCaption("项目解锁");
                this._Dialog.SetHeadButtons("1,2130837858,确定,删除密码", this.pCallback);
                this._Dialog.findViewById(R.id.ll_encryptSet_Create).setVisibility(8);
                this._Dialog.findViewById(R.id.ll_encryptSet_Modify).setVisibility(8);
                this._Dialog.findViewById(R.id.ll_encryptSet_Remove).setVisibility(0);
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.Encrypt_Setting_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Encrypt_Setting_Dialog.this.refreshLayout();
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
                view.getTag().toString();
            }
        }
    }
}
