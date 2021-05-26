package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;

public class SimpleRender_Settting_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public SimpleRender_Settting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.SimpleRender_Settting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.layers_manager_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("【" + PubVar._ComHashMap.GetValue("Project").Value + "】图层管理");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.SimpleRender_Settting_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
            }
        });
        this._Dialog.show();
    }
}
