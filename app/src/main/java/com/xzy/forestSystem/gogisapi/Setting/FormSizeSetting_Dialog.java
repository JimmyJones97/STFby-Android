package  com.xzy.forestSystem.gogisapi.Setting;

import android.content.DialogInterface;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;

public class FormSizeSetting_Dialog {
    private XDialogTemplate _Dialog;
    private int _FormType;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public FormSizeSetting_Dialog() {
        this._Dialog = null;
        this._FormType = 0;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Setting.FormSizeSetting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (!command.equals("确定")) {
                    return;
                }
                if (FormSizeSetting_Dialog.this._FormType == 0) {
                    int tmpWidth = Integer.parseInt(Common.GetEditTextValueOnID(FormSizeSetting_Dialog.this._Dialog, R.id.et_dialogWidth));
                    int tmpHeight = Integer.parseInt(Common.GetEditTextValueOnID(FormSizeSetting_Dialog.this._Dialog, R.id.et_dialogHeight));
                    int tmpMaxCount = Integer.parseInt(Common.GetEditTextValueOnID(FormSizeSetting_Dialog.this._Dialog, R.id.et_dialogMaxCount));
                    if (tmpWidth <= 0 || tmpHeight <= 0 || tmpMaxCount <= 0) {
                        Common.ShowDialog("消息窗体的宽度,高度和允许显示行数不能小于或等于0.");
                        return;
                    }
                    PubVar.MessagePanelWidth = tmpWidth;
                    PubVar.MessagePanelHeight = tmpHeight;
                    PubVar.MessageMaxCount = tmpMaxCount;
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_MessagePanelWidth", Integer.valueOf(PubVar.MessagePanelWidth));
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_MessagePanelHeight", Integer.valueOf(PubVar.MessagePanelHeight));
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_MessageMaxCount", Integer.valueOf(PubVar.MessageMaxCount));
                    if (FormSizeSetting_Dialog.this.m_Callback != null) {
                        FormSizeSetting_Dialog.this.m_Callback.OnClick("窗体设置返回", null);
                    }
                    FormSizeSetting_Dialog.this._Dialog.dismiss();
                } else if (FormSizeSetting_Dialog.this._FormType == 1) {
                    int tmpWidth2 = Integer.parseInt(Common.GetEditTextValueOnID(FormSizeSetting_Dialog.this._Dialog, R.id.et_childMapWidth));
                    int tmpHeight2 = Integer.parseInt(Common.GetEditTextValueOnID(FormSizeSetting_Dialog.this._Dialog, R.id.et_childMapHeight));
                    if (tmpWidth2 <= 0 || tmpHeight2 <= 0) {
                        Common.ShowDialog("消息窗体的宽度和高度不能小于或等于0.");
                        return;
                    }
                    PubVar.ChildMapWidth = tmpWidth2;
                    PubVar.ChildMapHeight = tmpHeight2;
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_ChildMapWidth", Integer.valueOf(PubVar.ChildMapWidth));
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_ChildMapHeight", Integer.valueOf(PubVar.ChildMapHeight));
                    if (FormSizeSetting_Dialog.this.m_Callback != null) {
                        FormSizeSetting_Dialog.this.m_Callback.OnClick("分屏窗体尺寸设置返回", null);
                    }
                    FormSizeSetting_Dialog.this._Dialog.dismiss();
                } else if (FormSizeSetting_Dialog.this._FormType == 2) {
                    int tmpI = Integer.parseInt(Common.GetEditTextValueOnID(FormSizeSetting_Dialog.this._Dialog, R.id.et_FormTransparent));
                    if (tmpI < 0 || tmpI > 255) {
                        Common.ShowDialog("窗体的透明度值应该在0~255之间.");
                        return;
                    }
                    int tmpWidth3 = Integer.parseInt(Common.GetEditTextValueOnID(FormSizeSetting_Dialog.this._Dialog, R.id.et_childMapWidth));
                    int tmpHeight3 = Integer.parseInt(Common.GetEditTextValueOnID(FormSizeSetting_Dialog.this._Dialog, R.id.et_childMapHeight));
                    if (tmpWidth3 <= 0 || tmpHeight3 <= 0) {
                        Common.ShowDialog("图层列表窗体的宽度和高度不能小于或等于0.");
                        return;
                    }
                    PubVar.LayersContentAlpha = tmpI;
                    PubVar.LayersContentWidth = tmpWidth3;
                    PubVar.LayersContentHeight = tmpHeight3;
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_LayersContentForm_Width", Integer.valueOf(PubVar.LayersContentWidth));
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_LayersContentForm_Height", Integer.valueOf(PubVar.LayersContentHeight));
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_LayersContentForm_Alpha", Integer.valueOf(PubVar.LayersContentAlpha));
                    if (FormSizeSetting_Dialog.this.m_Callback != null) {
                        FormSizeSetting_Dialog.this.m_Callback.OnClick("图层列表窗体尺寸设置返回", null);
                    }
                    FormSizeSetting_Dialog.this._Dialog.dismiss();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.formsizesetting_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("消息窗体设置");
        this._Dialog.setCanceledOnTouchOutside(true);
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
    }

    public void Set_FormType(int formType) {
        this._FormType = formType;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        if (this._FormType == 0) {
            this._Dialog.SetCaption("消息窗体设置");
            this._Dialog.findViewById(R.id.ll_messageDialogSetting).setVisibility(0);
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_dialogWidth, String.valueOf(PubVar.MessagePanelWidth));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_dialogHeight, String.valueOf(PubVar.MessagePanelHeight));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_dialogMaxCount, String.valueOf(PubVar.MessageMaxCount));
        } else if (this._FormType == 1) {
            this._Dialog.SetCaption("分屏窗体设置");
            this._Dialog.findViewById(R.id.ll_childMapSetting).setVisibility(0);
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_childMapWidth, String.valueOf(PubVar.ChildMapWidth));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_childMapHeight, String.valueOf(PubVar.ChildMapHeight));
        } else if (this._FormType == 2) {
            this._Dialog.SetCaption("图层列表窗体设置");
            this._Dialog.findViewById(R.id.ll_childMapSetting).setVisibility(0);
            this._Dialog.findViewById(R.id.ll_FormTranspSetting).setVisibility(0);
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_childMapWidth, String.valueOf(PubVar.LayersContentWidth));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_childMapHeight, String.valueOf(PubVar.LayersContentHeight));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_FormTransparent, String.valueOf(PubVar.LayersContentAlpha));
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Setting.FormSizeSetting_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                FormSizeSetting_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
