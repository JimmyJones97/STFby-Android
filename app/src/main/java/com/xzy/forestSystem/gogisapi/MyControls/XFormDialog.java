package  com.xzy.forestSystem.gogisapi.MyControls;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;

public class XFormDialog {
    private XDialogTemplate _Dialog;
    ICallback m_Callback;
    ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public XFormDialog(int layoutID) {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(layoutID);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("窗体");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
    }

    public void ShowDialog() {
        this._Dialog.show();
    }
}
