package  com.xzy.forestSystem.gogisapi.XProject;

import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.HashMap;

public class CoordSystem_Create {
    private ICallback _Callback;
    private XDialogTemplate _Dialog;
    private ICallback _newCallback;
    private HashMap<String, Object> m_NewCoorSystemPara;

    public CoordSystem_Create() {
        this._Dialog = null;
        this._newCallback = null;
        this.m_NewCoorSystemPara = null;
        this._Callback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CoordSystem_Create.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("确定")) {
                    String str2 = PubVar._PubCommand.m_UserConfigDB.GetMyCoodinateSystem().SaveMyCoordinateSystem(Common.GetTextValueOnID(CoordSystem_Create.this._Dialog, (int) R.id.et_name), CoordSystem_Create.this.m_NewCoorSystemPara, Common.GetCheckBoxValueOnID(CoordSystem_Create.this._Dialog, R.id.cb_overwrite));
                    if (!str2.equals("OK")) {
                        Common.ShowDialog(CoordSystem_Create.this._Dialog.getContext(), "我的坐标系保存失败！\r\n原因：" + str2);
                    } else if (CoordSystem_Create.this._newCallback != null) {
                        CoordSystem_Create.this._newCallback.OnClick("新建我的坐标系", null);
                    }
                    CoordSystem_Create.this._Dialog.dismiss();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.coordsystem_new);
        this._Dialog.SetCaption("我的新坐标系");
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this._Callback);
    }

    public void SetCallback(ICallback tmpICallback) {
        this._newCallback = tmpICallback;
    }

    public void SetNewCoorSystemPara(HashMap<String, Object> paramHashMap) {
        this.m_NewCoorSystemPara = paramHashMap;
        CoordSystem_UndefineSelect.FillPromptInfo(this._Dialog, this.m_NewCoorSystemPara);
    }

    public void ShowDialog() {
        this._Dialog.show();
    }
}
