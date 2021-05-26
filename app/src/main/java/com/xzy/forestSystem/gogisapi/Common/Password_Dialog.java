package  com.xzy.forestSystem.gogisapi.Common;

import android.content.DialogInterface;
import  com.xzy.forestSystem.qihoo.jiagutracker.C0246Config;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Encryption.ENCRYPT_SET_TYPE;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;

public class Password_Dialog {
    private XDialogTemplate _Dialog = null;
    private ENCRYPT_SET_TYPE _EncryptSetType = ENCRYPT_SET_TYPE.Create;
    private ICallback m_Callback = null;
    private int m_ErrorCount = 0;
    private String m_OrigPassword = "";
    private String m_ReturnTag = "密码设置返回";
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Common.Password_Dialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String command, Object object) {
            try {
                if (command.equals("设置密码")) {
                    String tmpStr01 = Common.GetEditTextValueOnID(Password_Dialog.this._Dialog, R.id.et_encryptSet_Create01);
                    String tmpStr02 = Common.GetEditTextValueOnID(Password_Dialog.this._Dialog, R.id.et_encryptSet_Create02);
                    if (tmpStr01 == null || tmpStr01.equals(C0246Config.EMPTY_STRING)) {
                        Common.ShowDialog("输入密码不能为空.");
                    } else if (!tmpStr01.equals(tmpStr02)) {
                        Common.ShowDialog("两次输入的密码不一致.");
                    } else {
                        if (Password_Dialog.this.m_Callback != null) {
                            Password_Dialog.this.m_Callback.OnClick(Password_Dialog.this.m_ReturnTag, tmpStr01);
                        }
                        Password_Dialog.this._Dialog.dismiss();
                    }
                } else if (command.equals("修改密码")) {
                    String tmpStr012 = Common.GetEditTextValueOnID(Password_Dialog.this._Dialog, R.id.et_encryptSet_Modify02);
                    String tmpStr022 = Common.GetEditTextValueOnID(Password_Dialog.this._Dialog, R.id.et_encryptSet_Modify03);
                    if (tmpStr012 == null || tmpStr012.equals(C0246Config.EMPTY_STRING)) {
                        Common.ShowDialog("输入密码不能为空.");
                    } else if (!tmpStr012.equals(tmpStr022)) {
                        Common.ShowDialog("两次输入的密码不一致.");
                    } else if (Common.GetEditTextValueOnID(Password_Dialog.this._Dialog, R.id.et_encryptSet_Modify01).equals(Password_Dialog.this.m_OrigPassword)) {
                        if (Password_Dialog.this.m_Callback != null) {
                            Password_Dialog.this.m_Callback.OnClick(Password_Dialog.this.m_ReturnTag, tmpStr012);
                        }
                        Password_Dialog.this._Dialog.dismiss();
                    } else {
                        Common.ShowDialog("输入的原始密码不正确.");
                        Password_Dialog.this.m_ErrorCount++;
                        if (Password_Dialog.this.m_ErrorCount >= 3) {
                            Common.ShowDialog("三次输入原始密码错误,系统将返回.");
                            Password_Dialog.this._Dialog.dismiss();
                        }
                    }
                } else if (command.equals("删除密码")) {
                    String tmpOrigPwd = Common.GetEditTextValueOnID(Password_Dialog.this._Dialog, R.id.et_encryptSet_Remove);
                    if (tmpOrigPwd.equals(Password_Dialog.this.m_OrigPassword)) {
                        if (Password_Dialog.this.m_Callback != null) {
                            Password_Dialog.this.m_Callback.OnClick(Password_Dialog.this.m_ReturnTag, tmpOrigPwd);
                        }
                        Password_Dialog.this._Dialog.dismiss();
                        return;
                    }
                    Common.ShowDialog("输入的原始密码不正确.");
                    Password_Dialog.this.m_ErrorCount++;
                    if (Password_Dialog.this.m_ErrorCount >= 3) {
                        Common.ShowDialog("三次输入原始密码错误,系统将返回.");
                        Password_Dialog.this._Dialog.dismiss();
                    }
                }
            } catch (Exception e) {
            }
        }
    };

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public Password_Dialog(ENCRYPT_SET_TYPE encryptSetType) {
        this._EncryptSetType = encryptSetType;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.password_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("密码管理");
    }

    public void SetOrigPassword(String password) {
        this.m_OrigPassword = password;
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
            } else if (this._EncryptSetType == ENCRYPT_SET_TYPE.Remove) {
                this._Dialog.SetCaption("删除密码");
                this._Dialog.SetHeadButtons("1,2130837858,确定,删除密码", this.pCallback);
                this._Dialog.findViewById(R.id.ll_encryptSet_Create).setVisibility(8);
                this._Dialog.findViewById(R.id.ll_encryptSet_Modify).setVisibility(8);
                this._Dialog.findViewById(R.id.ll_encryptSet_Remove).setVisibility(0);
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.Password_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Password_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
