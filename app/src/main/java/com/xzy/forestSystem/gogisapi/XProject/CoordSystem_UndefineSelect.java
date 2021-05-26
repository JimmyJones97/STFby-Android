package  com.xzy.forestSystem.gogisapi.XProject;

import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Config.UserConfig_CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CoordSystem_UndefineSelect {
    private XDialogTemplate _Dialog;
    private ICallback _selectCallback;
    private List<HashMap<String, Object>> m_MyCoorSystems;
    private HashMap<String, Object> m_NewCoorSystemPara;
    private ICallback pCallback;

    public CoordSystem_UndefineSelect() {
        this._Dialog = null;
        this._selectCallback = null;
        this.m_NewCoorSystemPara = null;
        this.m_MyCoorSystems = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CoordSystem_UndefineSelect.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("删除")) {
                    if (CoordSystem_UndefineSelect.this.m_MyCoorSystems == null || CoordSystem_UndefineSelect.this.m_MyCoorSystems.size() == 0) {
                        Common.ShowDialog("系统中没有保存任何我的坐标系.");
                        return;
                    }
                    final ArrayList localArrayList = new ArrayList();
                    for (HashMap tmpHashMap : CoordSystem_UndefineSelect.this.m_MyCoorSystems) {
                        if (Boolean.parseBoolean(tmpHashMap.get("D1").toString())) {
                            localArrayList.add(tmpHashMap.get("D2").toString());
                        }
                    }
                    if (localArrayList.size() > 0) {
                        Common.ShowYesNoDialog(CoordSystem_UndefineSelect.this._Dialog.getContext(), "是否要删除以下我的坐标系？\r\n\r\n名称：" + Common.CombineStrings("\r\n名称：", localArrayList), new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CoordSystem_UndefineSelect.1.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject2) {
                                if (paramString2.equals("YES")) {
                                    UserConfig_CoordinateSystem tempConfigDB = PubVar._PubCommand.m_UserConfigDB.GetMyCoodinateSystem();
                                    Iterator localIterator = localArrayList.iterator();
                                    while (localIterator.hasNext()) {
                                        tempConfigDB.DeleteMyCoordinateSystem((String) localIterator.next());
                                    }
                                    CoordSystem_UndefineSelect.this.LoadMyCoordinateSystemInfo();
                                }
                            }
                        });
                    } else {
                        Common.ShowDialog("请勾选需要删除的坐标系！");
                    }
                } else if (paramString.equals("确定")) {
                    if (CoordSystem_UndefineSelect.this._selectCallback != null) {
                        CoordSystem_UndefineSelect.this._selectCallback.OnClick("设置坐标系", CoordSystem_UndefineSelect.this.m_NewCoorSystemPara);
                    }
                    CoordSystem_UndefineSelect.this._Dialog.dismiss();
                } else if (paramString.equals("保存")) {
                    CoordSystem_Create localv1_project_mycoordinatesystem_new = new CoordSystem_Create();
                    localv1_project_mycoordinatesystem_new.SetNewCoorSystemPara(CoordSystem_UndefineSelect.this.m_NewCoorSystemPara);
                    localv1_project_mycoordinatesystem_new.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CoordSystem_UndefineSelect.1.2
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString2, Object pObject2) {
                            if (paramString2.equals("新建我的坐标系")) {
                                CoordSystem_UndefineSelect.this.LoadMyCoordinateSystemInfo();
                            }
                        }
                    });
                    localv1_project_mycoordinatesystem_new.ShowDialog();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.coordsystem_undefineselect);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("我的坐标系");
        this._Dialog.SetHeadButtons("1,2130837637,删除  ,删除", this.pCallback);
        this._Dialog.findViewById(R.id.buttonSaveSystem).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CoordSystem_UndefineSelect.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                CoordSystem_UndefineSelect.this.pCallback.OnClick("保存", null);
            }
        });
    }

    public static void FillPromptInfo(Dialog paramDialog, HashMap<String, Object> paramHashMap) {
        try {
            if (paramHashMap.containsKey("CoorSystem")) {
                String tmpCoordSys = paramHashMap.get("CoorSystem").toString();
                paramDialog.findViewById(R.id.ll_coorsystem).setVisibility(0);
                Common.SetTextViewValueOnID(paramDialog, (int) R.id.tv_coorsystem, tmpCoordSys);
                if (tmpCoordSys.equals("WGS-84坐标")) {
                    paramHashMap.put("CenterJX", "");
                    paramHashMap.put("TransMethod", "无");
                }
                paramDialog.findViewById(R.id.ll_centerjx).setVisibility(0);
                Object tempOBj = paramHashMap.get("CenterJX");
                if (tempOBj == null) {
                    tempOBj = "0";
                }
                Common.SetTextViewValueOnID(paramDialog, (int) R.id.tv_centerjx, tempOBj.toString());
                paramDialog.findViewById(R.id.ll_transmethod).setVisibility(0);
                Common.SetTextViewValueOnID(paramDialog, (int) R.id.tv_transmethod, paramHashMap.get("TransMethod").toString());
                String str = (String) paramHashMap.get("TransMethod");
                if (str.contains("三参")) {
                    paramHashMap.put("TransParam", "X平移(DX)=" + paramHashMap.get("P1").toString() + ",Y平移(DY)=" + paramHashMap.get("P2").toString() + ",Z平移(DZ)=" + paramHashMap.get("P3").toString() + ",长轴差(DA)=" + paramHashMap.get("P4").toString() + ",扁率差(DF)=" + paramHashMap.get("P5").toString());
                } else if (str.contains("平移")) {
                    paramHashMap.put("TransParam", "X平移(DX)=" + paramHashMap.get("P1").toString() + ",Y平移(DY)=" + paramHashMap.get("P2").toString() + ",Z平移(DZ)=" + paramHashMap.get("P3").toString());
                } else if (str.contains("四参")) {
                    paramHashMap.put("TransParam", "X平移(△X)=" + paramHashMap.get("P1").toString() + ",Y平移(△Y)=" + paramHashMap.get("P2").toString() + ",旋转角度(T)=" + paramHashMap.get("P3").toString() + ",尺度因子(K)=" + paramHashMap.get("P4").toString());
                } else if (str.contains("七参")) {
                    paramHashMap.put("TransParam", "X平移(△X)=" + paramHashMap.get("P1").toString() + ",Y平移(△Y)=" + paramHashMap.get("P2").toString() + ",Z平移(△Z)=" + paramHashMap.get("P3").toString() + ",X旋转(△α)=" + paramHashMap.get("P4").toString() + ",Y旋转(△β)=" + paramHashMap.get("P5").toString() + ",Z旋转(△y)=" + paramHashMap.get("P6").toString() + ",尺度因子(K)=" + paramHashMap.get("P7").toString());
                } else {
                    paramHashMap.put("TransParam", "无");
                }
                if (paramHashMap.containsKey("TransParam")) {
                    paramDialog.findViewById(R.id.ll_transparam).setVisibility(0);
                    Common.SetTextViewValueOnID(paramDialog, (int) R.id.tv_transparam, (String) paramHashMap.get("TransParam"));
                }
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void LoadMyCoordinateSystemInfo() {
        new MyTableFactory();
        MyTableFactory tmpTableFactory = new MyTableFactory();
        tmpTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.prj_list), "自定义", "选择,名称,坐标系统,中央经线,转换方法", "checkbox,text,text,text,text", new int[]{-10, -30, -30, -15, -15}, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CoordSystem_UndefineSelect.3
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("列表选项")) {
                    CoordSystem_Select localv1_project_mycoordinatesystem_select = new CoordSystem_Select();
                    localv1_project_mycoordinatesystem_select.SetNewCoorSystemPara((HashMap) pObject);
                    localv1_project_mycoordinatesystem_select.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CoordSystem_UndefineSelect.3.1
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString2, final Object pObject2) {
                            if (paramString2.equals("选择我的坐标系")) {
                                Common.ShowYesNoDialog(CoordSystem_UndefineSelect.this._Dialog.getContext(), "是否覆盖项目当前的坐标系？", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CoordSystem_UndefineSelect.3.1.1
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String paramString3, Object pObject22) {
                                        if (paramString3.equals("YES")) {
                                            CoordSystem_UndefineSelect.this.SetNewCoorSystemPara((HashMap) pObject2);
                                            CoordSystem_UndefineSelect.this.pCallback.OnClick("确定", null);
                                        }
                                    }
                                });
                            }
                        }
                    });
                    localv1_project_mycoordinatesystem_select.ShowDialog();
                }
            }
        });
        this.m_MyCoorSystems = PubVar._PubCommand.m_UserConfigDB.GetMyCoodinateSystem().GetMyCoordinateSystemList();
        tmpTableFactory.BindDataToListView(this.m_MyCoorSystems);
    }

    public void SetCallback(ICallback tmpICallback) {
        this._selectCallback = tmpICallback;
    }

    public void SetNewCoorSystemPara(HashMap<String, Object> paramHashMap) {
        this.m_NewCoorSystemPara = paramHashMap;
        FillPromptInfo(this._Dialog, this.m_NewCoorSystemPara);
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CoordSystem_UndefineSelect.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                CoordSystem_UndefineSelect.this.LoadMyCoordinateSystemInfo();
            }
        });
        this._Dialog.show();
    }
}
