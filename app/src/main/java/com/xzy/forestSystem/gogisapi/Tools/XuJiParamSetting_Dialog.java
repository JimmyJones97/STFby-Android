package  com.xzy.forestSystem.gogisapi.Tools;

import android.content.DialogInterface;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;

public class XuJiParamSetting_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public XuJiParamSetting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.XuJiParamSetting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("确定")) {
                        String tmpString = Common.GetEditTextValueOnID(XuJiParamSetting_Dialog.this._Dialog, R.id.et_xujiparam_01).trim();
                        if (tmpString.length() == 0) {
                            Common.ShowToast("请输入正确的参数.");
                            return;
                        }
                        double tmpD01 = Double.parseDouble(tmpString);
                        if (tmpD01 <= 0.0d || tmpD01 >= 1.0d) {
                            Common.ShowToast("请输入正确的参数.");
                            return;
                        }
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_XuJi_Param_Xinshu01", Double.valueOf(tmpD01));
                        String tmpString2 = Common.GetEditTextValueOnID(XuJiParamSetting_Dialog.this._Dialog, R.id.et_xujiparam_02).trim();
                        if (tmpString2.length() == 0) {
                            Common.ShowToast("请输入正确的参数.");
                            return;
                        }
                        double tmpD02 = Double.parseDouble(tmpString2);
                        if (tmpD02 <= 0.0d || tmpD02 >= 1.0d) {
                            Common.ShowToast("请输入正确的参数.");
                            return;
                        }
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_XuJi_Param_Xinshu02", Double.valueOf(tmpD02));
                        String tmpString3 = Common.GetEditTextValueOnID(XuJiParamSetting_Dialog.this._Dialog, R.id.et_xujiparam_03).trim();
                        if (tmpString3.length() == 0) {
                            Common.ShowToast("请输入正确的参数.");
                            return;
                        }
                        double tmpD03 = Double.parseDouble(tmpString3);
                        if (tmpD03 <= 0.0d || tmpD03 >= 1.0d) {
                            Common.ShowToast("请输入正确的参数.");
                            return;
                        }
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_XuJi_Param_Xinshu03", Double.valueOf(tmpD03));
                        String tmpString4 = Common.GetEditTextValueOnID(XuJiParamSetting_Dialog.this._Dialog, R.id.et_xujiparam_04).trim();
                        if (tmpString4.length() == 0) {
                            Common.ShowToast("请输入正确的参数.");
                            return;
                        }
                        double tmpD04 = Double.parseDouble(tmpString4);
                        if (tmpD04 <= 0.0d || tmpD04 >= 1.0d) {
                            Common.ShowToast("请输入正确的参数.");
                            return;
                        }
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_XuJi_Param_Xinshu04", Double.valueOf(tmpD04));
                        if (XuJiParamSetting_Dialog.this.m_Callback != null) {
                            XuJiParamSetting_Dialog.this.m_Callback.OnClick("蓄积量实验形数设置返回", new double[]{tmpD01, tmpD02, tmpD03, tmpD04});
                        }
                        XuJiParamSetting_Dialog.this._Dialog.dismiss();
                    }
                } catch (Exception e) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.xujiparamsetting_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("实验形数设置");
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
    }

    public void setParam(double[] params) {
        if (params != null && params.length > 3) {
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_xujiparam_01, String.valueOf(params[0]));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_xujiparam_02, String.valueOf(params[1]));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_xujiparam_03, String.valueOf(params[2]));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_xujiparam_04, String.valueOf(params[3]));
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.XuJiParamSetting_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
            }
        });
        this._Dialog.show();
    }
}
