package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.HashValueObject;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.ECoorTransMethod;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.ProjectionCoordinateSystem;
import  com.xzy.forestSystem.gogisapi.Geodatabase.ProjectDatabase;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;

public class Project_DetailInfo_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private HashMap<String, String> m_ProjectInfo;
    private ICallback pCallback;

    public Project_DetailInfo_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_ProjectInfo = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_DetailInfo_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("打开项目") && Project_DetailInfo_Dialog.this.m_Callback != null) {
                    Project_DetailInfo_Dialog.this.m_Callback.OnClick("打开项目", Project_DetailInfo_Dialog.this.m_ProjectInfo.get("Name"));
                }
                Project_DetailInfo_Dialog.this._Dialog.dismiss();
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.project_detailinfo_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetHeadButtons("1,2130837859,打开,打开项目", this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void LoadProjectInfo() {
        try {
            ProjectDatabase tmpProjectDB = new ProjectDatabase();
            if (tmpProjectDB.OpenProject(this.m_ProjectInfo.get("Name"), false)) {
                AbstractC0383CoordinateSystem localCoorSystem = tmpProjectDB.GetProjectManage().GetCoorSystem();
                HashMap<String, Object> tmpHashMap1 = new HashMap<>();
                tmpHashMap1.put("CoorSystem", localCoorSystem.GetName());
                if (localCoorSystem.getIsProjectionCoord()) {
                    tmpHashMap1.put("CenterJX", Float.valueOf(((ProjectionCoordinateSystem) localCoorSystem).GetCenterMeridian()));
                } else {
                    tmpHashMap1.put("CenterJX", 0);
                }
                tmpHashMap1.put("TransMethod", localCoorSystem.GetPMTranslate().GetPMCoorTransMethodName());
                if (localCoorSystem.GetPMTranslate().GetPMCoorTransMethod() == ECoorTransMethod.enThreePara) {
                    tmpHashMap1.put("P1", Double.valueOf(localCoorSystem.GetPMTranslate().GetTransToP31()));
                    tmpHashMap1.put("P2", Double.valueOf(localCoorSystem.GetPMTranslate().GetTransToP32()));
                    tmpHashMap1.put("P3", Double.valueOf(localCoorSystem.GetPMTranslate().GetTransToP33()));
                    tmpHashMap1.put("P4", Double.valueOf(localCoorSystem.GetPMTranslate().GetTransToP34()));
                    tmpHashMap1.put("P5", Common.ConvertToDigi(localCoorSystem.GetPMTranslate().GetTransToP35(), 7));
                    tmpHashMap1.put("P6", "0");
                    tmpHashMap1.put("P7", "0");
                } else if (localCoorSystem.GetPMTranslate().GetPMCoorTransMethod() == ECoorTransMethod.enFourPara) {
                    tmpHashMap1.put("P1", Double.valueOf(localCoorSystem.GetPMTranslate().GetTransToP41()));
                    tmpHashMap1.put("P2", Double.valueOf(localCoorSystem.GetPMTranslate().GetTransToP42()));
                    tmpHashMap1.put("P3", Double.valueOf(localCoorSystem.GetPMTranslate().GetTransToP43()));
                    tmpHashMap1.put("P4", Double.valueOf(localCoorSystem.GetPMTranslate().GetTransToP44()));
                    tmpHashMap1.put("P5", "0");
                    tmpHashMap1.put("P6", "0");
                    tmpHashMap1.put("P7", "0");
                } else if (localCoorSystem.GetPMTranslate().GetPMCoorTransMethod() == ECoorTransMethod.enServenPara) {
                    tmpHashMap1.put("P1", Double.valueOf(localCoorSystem.GetPMTranslate().GetTransToP71()));
                    tmpHashMap1.put("P2", Double.valueOf(localCoorSystem.GetPMTranslate().GetTransToP72()));
                    tmpHashMap1.put("P3", Double.valueOf(localCoorSystem.GetPMTranslate().GetTransToP73()));
                    tmpHashMap1.put("P4", Double.valueOf(localCoorSystem.GetPMTranslate().GetTransToP74()));
                    tmpHashMap1.put("P5", Double.valueOf(localCoorSystem.GetPMTranslate().GetTransToP75()));
                    tmpHashMap1.put("P6", Double.valueOf(localCoorSystem.GetPMTranslate().GetTransToP76()));
                    tmpHashMap1.put("P7", Double.valueOf(localCoorSystem.GetPMTranslate().GetTransToP77()));
                }
                CoordSystem_UndefineSelect.FillPromptInfo(this._Dialog, tmpHashMap1);
                try {
                    new MyTableFactory();
                    MyTableFactory tmpTableFactory = new MyTableFactory();
                    tmpTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.prj_list), "自定义", "图层名称,图层类型,数据数目", "text,text,text", new int[]{-56, -22, -22}, this.pCallback);
                    ArrayList localArrayList = new ArrayList();
                    for (FeatureLayer localv1_Layer : tmpProjectDB.GetLayerManage().GetLayerList()) {
                        HashMap tmpHashMap2 = new HashMap();
                        tmpHashMap2.put("D1", localv1_Layer.GetLayerName());
                        tmpHashMap2.put("D2", localv1_Layer.GetLayerTypeName());
                        tmpHashMap2.put("D3", "0");
                        SQLiteDBHelper localASQLiteDatabase = new SQLiteDBHelper();
                        localASQLiteDatabase.setDatabaseName(tmpProjectDB.GetProjectManage().GetProjectDataFileName());
                        SQLiteReader localSQLiteDataReader = localASQLiteDatabase.Query("select count(*) from " + localv1_Layer.GetLayerID() + "_D where SYS_STATUS<>1");
                        if (localSQLiteDataReader != null) {
                            if (localSQLiteDataReader.Read()) {
                                tmpHashMap2.put("D3", localSQLiteDataReader.GetString(0));
                            }
                            localSQLiteDataReader.Close();
                        }
                        localASQLiteDatabase.Close();
                        localArrayList.add(tmpHashMap2);
                    }
                    tmpTableFactory.BindDataToListView(localArrayList);
                } catch (Exception ex) {
                    Common.Log("LoadProjectInfo", "错误:" + ex.toString() + "-->" + ex.getMessage());
                }
            }
        } catch (Exception e) {
        }
    }

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public void SetProject(HashMap<String, String> paramHashMap) {
        this.m_ProjectInfo = paramHashMap;
        this._Dialog.SetCaption("项目详细信息");
        HashValueObject localHashValueObject = PubVar._ComHashMap.GetValue("Project");
        if (localHashValueObject != null && localHashValueObject.Value.equals(paramHashMap.get("Name"))) {
            this._Dialog.GetButton("1").setEnabled(false);
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_DetailInfo_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Project_DetailInfo_Dialog.this.LoadProjectInfo();
            }
        });
        this._Dialog.show();
    }
}
