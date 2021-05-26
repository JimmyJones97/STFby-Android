package  com.xzy.forestSystem.gogisapi.XProject;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.HashMap;

public class CoordSystem_Select {
    private ICallback _Callback;
    private XDialogTemplate _Dialog;
    private ICallback _selectCallback;
    private HashMap<String, Object> m_SelCoorSystemPara;

    public CoordSystem_Select() {
        this._Callback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CoordSystem_Select.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("确定")) {
                    if (CoordSystem_Select.this._selectCallback != null) {
                        CoordSystem_Select.this._selectCallback.OnClick("选择我的坐标系", CoordSystem_Select.this.m_SelCoorSystemPara);
                    }
                    CoordSystem_Select.this._Dialog.dismiss();
                }
            }
        };
        this._Dialog = null;
        this._selectCallback = null;
        this.m_SelCoorSystemPara = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.coordsystem_select);
        this._Dialog.SetCaption("坐标系信息");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this._Callback);
    }

    public void SetCallback(ICallback tmpICallback) {
        this._selectCallback = tmpICallback;
    }

    public void SetNewCoorSystemPara(HashMap<String, Object> paramHashMap) {
        this.m_SelCoorSystemPara = paramHashMap;
        CoordSystem_UndefineSelect.FillPromptInfo(this._Dialog, this.m_SelCoorSystemPara);
    }

    public void ShowDialog() {
        this._Dialog.show();
    }
}
