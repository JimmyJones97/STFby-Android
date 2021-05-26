package  com.xzy.forestSystem.gogisapi.XProject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.RasterLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WGS1984;
import  com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSource;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Project_Create_Dialog {
    private int DaiHaoValue;
    private float FenDaiValue;
    private XDialogTemplate _Dialog;
    private AbstractC0383CoordinateSystem _SelectCoordSystem;
    private ICallback m_Callback;
    private SpinnerList m_CenterSpinner;
    private SpinnerList m_CoordSpinner;
    private SpinnerList m_LayersSpinner;
    private SpinnerList m_TransfSpinner;
    private ICallback pCallback;

    public Project_Create_Dialog() {
        String tmpLayersModName;
        this._Dialog = null;
        this.m_Callback = null;
        this._SelectCoordSystem = AbstractC0383CoordinateSystem.CreateCoordinateSystem("WGS-84坐标");
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Create_Dialog.1
            @SuppressLint("WrongConstant")
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                String[] tmpStrs;
                double tmpCenterLineD;
                String tmpStr01;
                String[] tmpStrs01;
                try {
                    if (command.equals("坐标系选择返回")) {
                        int isVisb = 0;
                        String tempCoorName = object.toString();
                        Project_Create_Dialog.this._SelectCoordSystem = AbstractC0383CoordinateSystem.CreateCoordinateSystem(tempCoorName);
                        if (tempCoorName.equals("WGS-84坐标")) {
                            isVisb = 8;
                        }
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.ll_centerjx).setVisibility(isVisb);
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.ll_transmethod).setVisibility(isVisb);
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.ll_param).setVisibility(8);
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.ll_Spheroid01).setVisibility(0);
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.ll_Spheroid02).setVisibility(0);
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.ll_Spheroid03).setVisibility(0);
                        Common.SetValueToView(Project_Create_Dialog.this._SelectCoordSystem.getDefaultSpheroidName(), Project_Create_Dialog.this._Dialog.findViewById(R.id.sp_SpheroidName));
                        String tempTransMtd = Common.GetSpinnerValueOnID(Project_Create_Dialog.this._Dialog, R.id.sp_transmethod);
                        Project_Create_Dialog.this._SelectCoordSystem.GetPMTranslate().SetPMCoorTransMethodName(tempTransMtd);
                        if (tempTransMtd.equals("无")) {
                            Project_Create_Dialog.this._Dialog.findViewById(R.id.ll_param).setVisibility(8);
                            return;
                        }
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.ll_param).setVisibility(0);
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_move).setVisibility(8);
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_3).setVisibility(8);
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_4).setVisibility(8);
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_7).setVisibility(8);
                        if (tempTransMtd.indexOf("平移") >= 0) {
                            Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_move).setVisibility(0);
                        } else if (tempTransMtd.indexOf("三参") >= 0) {
                            Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_3).setVisibility(0);
                            double tmpD01 = Double.parseDouble(Common.GetViewValue(Project_Create_Dialog.this._Dialog, R.id.et_SpheroidA));
                            double tmpD02 = Double.parseDouble(Common.GetViewValue(Project_Create_Dialog.this._Dialog, R.id.et_Spheroidf));
                            Coordinate_WGS1984 tmpCoordSys = new Coordinate_WGS1984();
                            double tmpD012 = tmpCoordSys.GetA() - tmpD01;
                            double tmpD022 = (1.0d / tmpCoordSys.Getf()) - (1.0d / tmpD02);
                            Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp3_p4, String.valueOf(tmpD012));
                            Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp3_p5, Common.ConvertToDigi(tmpD022, 7));
                        } else if (tempTransMtd.indexOf("四参") >= 0) {
                            Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_4).setVisibility(0);
                        } else if (tempTransMtd.indexOf("七参") >= 0) {
                            Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_7).setVisibility(0);
                        }
                    } else if (command.equals("选择投影椭球返回")) {
                        String tmpDefaultSpname = Common.GetSpinnerValueOnID(Project_Create_Dialog.this._Dialog, R.id.sp_SpheroidName);
                        if (!tmpDefaultSpname.equals("无")) {
                            HashMap<String, String> tmpSpheroidHash = PubVar._PubCommand.m_ConfigDB.getSpheroidList();
                            if (tmpSpheroidHash.containsKey(tmpDefaultSpname) && (tmpStr01 = tmpSpheroidHash.get(tmpDefaultSpname)) != null && tmpStr01.length() > 0 && (tmpStrs01 = tmpStr01.split(",")) != null && tmpStrs01.length > 1) {
                                Common.SetEditTextValueOnID(Project_Create_Dialog.this._Dialog, R.id.et_SpheroidA, tmpStrs01[0]);
                                Common.SetEditTextValueOnID(Project_Create_Dialog.this._Dialog, R.id.et_Spheroidf, tmpStrs01[1]);
                            }
                        }
                    } else if (command.equals("点击图层模板选择")) {
                        Project_LayersTemplate_Dialog tmpDialog = new Project_LayersTemplate_Dialog();
                        tmpDialog.SetDefaultTemplateName(Common.GetSpinnerValueOnID(Project_Create_Dialog.this._Dialog, R.id.sp_layertemplate));
                        tmpDialog.SetCallback(Project_Create_Dialog.this.pCallback);
                        tmpDialog.ShowDialog();
                    } else if (command.equals("模板列表")) {
                        Project_Create_Dialog.this.m_LayersSpinner.SetTextJust(String.valueOf(object));
                    } else if (command.equals("点击中央经线选择")) {
                        CoordSystem_CenterLine tmpDialog2 = new CoordSystem_CenterLine();
                        tmpDialog2.SetCallback(Project_Create_Dialog.this.pCallback);
                        tmpDialog2.CurrentCoordName = Common.GetViewValue(Project_Create_Dialog.this._Dialog, R.id.sp_coorsystem);
                        try {
                            tmpCenterLineD = Double.parseDouble(Project_Create_Dialog.this.m_CenterSpinner.getText());
                        } catch (Exception e) {
                            tmpCenterLineD = 120.0d;
                        }
                        tmpDialog2.SetCenterLine(tmpCenterLineD);
                        tmpDialog2.ShowDialog();
                    } else if (command.equals("中央经线选择返回")) {
                        if (object != null && (object instanceof String[]) && (tmpStrs = (String[]) object) != null && tmpStrs.length > 2) {
                            Project_Create_Dialog.this.m_CenterSpinner.SetTextJust(tmpStrs[0]);
                            Project_Create_Dialog.this.FenDaiValue = Float.parseFloat(tmpStrs[1]);
                            Project_Create_Dialog.this.DaiHaoValue = Integer.parseInt(tmpStrs[2]);
                        }
                    } else if (command.equals("转换方法选择返回")) {
                        String tempTransMtd2 = object.toString();
                        Project_Create_Dialog.this._SelectCoordSystem.GetPMTranslate().SetPMCoorTransMethodName(tempTransMtd2);
                        if (tempTransMtd2.equals("无")) {
                            Project_Create_Dialog.this._Dialog.findViewById(R.id.ll_param).setVisibility(8);
                            return;
                        }
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.ll_param).setVisibility(0);
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_move).setVisibility(8);
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_3).setVisibility(8);
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_4).setVisibility(8);
                        Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_7).setVisibility(8);
                        if (tempTransMtd2.indexOf("平移") >= 0) {
                            Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_move).setVisibility(0);
                        } else if (tempTransMtd2.indexOf("三参") >= 0) {
                            Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_3).setVisibility(0);
                            double tmpD013 = Double.parseDouble(Common.GetViewValue(Project_Create_Dialog.this._Dialog, R.id.et_SpheroidA));
                            double tmpD023 = Double.parseDouble(Common.GetViewValue(Project_Create_Dialog.this._Dialog, R.id.et_Spheroidf));
                            Coordinate_WGS1984 tmpCoordSys2 = new Coordinate_WGS1984();
                            double tmpD014 = tmpCoordSys2.GetA() - tmpD013;
                            double tmpD024 = (1.0d / tmpCoordSys2.Getf()) - (1.0d / tmpD023);
                            Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp3_p4, String.valueOf(tmpD014));
                            Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp3_p5, Common.ConvertToDigi(tmpD024, 7));
                        } else if (tempTransMtd2.indexOf("四参") >= 0) {
                            Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_4).setVisibility(0);
                        } else if (tempTransMtd2.indexOf("七参") >= 0) {
                            Project_Create_Dialog.this._Dialog.findViewById(R.id.project_param_7).setVisibility(0);
                        }
                    } else if (command.equals("设置坐标系")) {
                        HashMap tmpHashMap2 = (HashMap) object;
                        Common.SetValueToView((String) tmpHashMap2.get("CoorSystem"), Project_Create_Dialog.this._Dialog.findViewById(R.id.sp_coorsystem));
                        if (!((String) tmpHashMap2.get("CoorSystem")).equals("WGS-84坐标")) {
                            Project_Create_Dialog.this.m_CenterSpinner.SetTextJust((String) tmpHashMap2.get("CenterJX"));
                            String tmpTransf = (String) tmpHashMap2.get("TransMethod");
                            Project_Create_Dialog.this.m_TransfSpinner.SetText(tmpTransf);
                            if (tmpTransf.contains("平移")) {
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.ppmove_p1, (String) tmpHashMap2.get("P1"));
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.ppmove_p2, (String) tmpHashMap2.get("P2"));
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.ppmove_p3, (String) tmpHashMap2.get("P3"));
                            } else if (tmpTransf.contains("三参")) {
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp3_p1, (String) tmpHashMap2.get("P1"));
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp3_p2, (String) tmpHashMap2.get("P2"));
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp3_p3, (String) tmpHashMap2.get("P3"));
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp3_p4, (String) tmpHashMap2.get("P4"));
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp3_p5, (String) tmpHashMap2.get("P5"));
                            } else if (tmpTransf.contains("四参")) {
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp4_p1, (String) tmpHashMap2.get("P1"));
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp4_p2, (String) tmpHashMap2.get("P2"));
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp4_p3, (String) tmpHashMap2.get("P3"));
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp4_p4, (String) tmpHashMap2.get("P4"));
                            } else if (tmpTransf.contains("七参")) {
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp7_p1, (String) tmpHashMap2.get("P1"));
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp7_p2, (String) tmpHashMap2.get("P2"));
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp7_p3, (String) tmpHashMap2.get("P3"));
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp7_p4, (String) tmpHashMap2.get("P4"));
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp7_p5, (String) tmpHashMap2.get("P5"));
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp7_p6, (String) tmpHashMap2.get("P6"));
                                Common.SetTextViewValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp7_p7, (String) tmpHashMap2.get("P7"));
                            }
                        }
                    } else if (command.equals("新建项目")) {
                        String tempPrjName = ((EditText) Project_Create_Dialog.this._Dialog.findViewById(R.id.pn_projectname)).getText().toString();
                        if (tempPrjName.trim().equals("")) {
                            Common.ShowDialog("项目名称不能为空.");
                        } else if (Common.CheckProjectExist(tempPrjName)) {
                            Common.ShowDialog("创建项目失败!项目【" + tempPrjName + "】已经存在.");
                        } else if (!PubVar._PubCommand.m_ProjectDB.CreateProject(tempPrjName)) {
                            Common.ShowDialog("创建项目失败!请检查设备具有足够存储空间以及使用权限.");
                        } else if (!Project_Create_Dialog.this.saveProjectInfo()) {
                            Common.ShowDialog("数据保存失败!请检查系统数据库是否正确或与开发者联系?");
                        } else if (Project_Create_Dialog.CreateLayerByTemplateName(tempPrjName, Common.GetSpinnerValueOnID(Project_Create_Dialog.this._Dialog, R.id.sp_layertemplate))) {
                            if (Common.GetViewValue(Project_Create_Dialog.this._Dialog, R.id.sp_coorsystem).contains("WGS")) {
                                RasterLayer tempLayer = new RasterLayer();
                                tempLayer.SetLayerTypeName("地名注记");
                                tempLayer.SetLayerName("地名注记");
                                tempLayer.SetEditMode(EEditMode.NEW);
                                tempLayer.SaveLayerInfo();
                                RasterLayer tempLayer2 = new RasterLayer();
                                tempLayer2.SetLayerTypeName("Google卫星地图");
                                tempLayer2.SetLayerName("Google卫星地图");
                                tempLayer2.SetEditMode(EEditMode.NEW);
                                tempLayer2.SaveLayerInfo();
                            }
                            if (Project_Create_Dialog.this.m_Callback != null) {
                                Project_Create_Dialog.this.m_Callback.OnClick("OK", null);
                            }
                            Project_Create_Dialog.this._Dialog.dismiss();
                        } else {
                            Common.ShowDialog("根据图层模板创建图层失败！");
                        }
                    }
                } catch (Exception e2) {
                }
            }
        };
        this.FenDaiValue = 3.0f;
        this.DaiHaoValue = 40;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.project_new);
        this._Dialog.Resize(0.96f, 0.96f);
        this._Dialog.SetCaption("新建项目");
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetHeadButtons("1,2130837858,新建,新建项目", this.pCallback);
        this.m_CoordSpinner = (SpinnerList) this._Dialog.findViewById(R.id.sp_coorsystem);
        this.m_CenterSpinner = (SpinnerList) this._Dialog.findViewById(R.id.sp_centerjx);
        this.m_LayersSpinner = (SpinnerList) this._Dialog.findViewById(R.id.sp_layertemplate);
        this.m_TransfSpinner = (SpinnerList) this._Dialog.findViewById(R.id.sp_transmethod);
        this.m_CoordSpinner.SetClickCallback(false);
        List<String> tempCoorSystemList = PubVar._PubCommand.m_ConfigDB.getCoordSystemsList();
        Common.SetSpinnerListData(this._Dialog.getContext(), this.m_CoordSpinner, tempCoorSystemList.size() > 0 ? tempCoorSystemList.get(0) : "WGS84", "选择坐标系统", tempCoorSystemList, "坐标系选择返回", this.pCallback);
        HashMap tmpHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_SysLayerTemplateName");
        if (tmpHashMap == null) {
            tmpLayersModName = PubVar._PubCommand.m_UserConfigDB.GetLayerTemplate().ReadTemplateList("系统").get(0).split("【")[0];
        } else {
            tmpLayersModName = (String) tmpHashMap.get("F2");
        }
        this.m_LayersSpinner.SetClickCallback(true);
        this.m_LayersSpinner.SetSelectReturnTag("点击图层模板选择");
        this.m_LayersSpinner.SetCallback(this.pCallback);
        this.m_LayersSpinner.SetTextJust(tmpLayersModName);
        this.m_CenterSpinner.SetClickCallback(true);
        this.m_CenterSpinner.SetSelectReturnTag("点击中央经线选择");
        this.m_CenterSpinner.SetCallback(this.pCallback);
        this.m_CenterSpinner.SetTextJust("120");
        List<String> tmpTrasfList = PubVar._PubCommand.m_ConfigDB.getTranslateList();
        Common.SetSpinnerListData(this._Dialog.getContext(), this.m_TransfSpinner, tmpTrasfList.size() > 0 ? tmpTrasfList.get(0) : "无", "选择转换方法", tmpTrasfList, "转换方法选择返回", this.pCallback);
        this._Dialog.findViewById(R.id.btn_myCoordSelect).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Create_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                CoordSystem_UndefineSelect tmpCoorSystem = new CoordSystem_UndefineSelect();
                HashMap tmpHashMap1 = new HashMap();
                tmpHashMap1.put("CoorSystem", Common.GetSpinnerValueOnID(Project_Create_Dialog.this._Dialog, R.id.sp_coorsystem));
                tmpHashMap1.put("CenterJX", Common.GetSpinnerValueOnID(Project_Create_Dialog.this._Dialog, R.id.sp_centerjx));
                tmpHashMap1.put("TransMethod", Common.GetSpinnerValueOnID(Project_Create_Dialog.this._Dialog, R.id.sp_transmethod));
                tmpHashMap1.put("P1", "0");
                tmpHashMap1.put("P2", "0");
                tmpHashMap1.put("P3", "0");
                tmpHashMap1.put("P4", "0");
                tmpHashMap1.put("P5", "0");
                tmpHashMap1.put("P6", "0");
                tmpHashMap1.put("P7", "0");
                String str1 = (String) tmpHashMap1.get("TransMethod");
                if (str1.contains("平移")) {
                    tmpHashMap1.put("P1", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.ppmove_p1));
                    tmpHashMap1.put("P2", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.ppmove_p2));
                    tmpHashMap1.put("P3", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.ppmove_p3));
                } else if (str1.contains("三参")) {
                    tmpHashMap1.put("P1", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp3_p1));
                    tmpHashMap1.put("P2", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp3_p2));
                    tmpHashMap1.put("P3", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp3_p3));
                    tmpHashMap1.put("P4", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp3_p4));
                    tmpHashMap1.put("P5", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp3_p5));
                } else if (str1.contains("四参")) {
                    tmpHashMap1.put("P1", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp4_p1));
                    tmpHashMap1.put("P2", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp4_p2));
                    tmpHashMap1.put("P3", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp4_p3));
                    tmpHashMap1.put("P4", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp4_p4));
                } else if (str1.contains("七参")) {
                    tmpHashMap1.put("P1", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp7_p1));
                    tmpHashMap1.put("P2", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp7_p2));
                    tmpHashMap1.put("P3", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp7_p3));
                    tmpHashMap1.put("P4", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp7_p4));
                    tmpHashMap1.put("P5", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp7_p5));
                    tmpHashMap1.put("P6", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp7_p6));
                    tmpHashMap1.put("P7", Common.GetTextValueOnID(Project_Create_Dialog.this._Dialog, (int) R.id.pp7_p7));
                }
                tmpCoorSystem.SetNewCoorSystemPara(tmpHashMap1);
                tmpCoorSystem.SetCallback(Project_Create_Dialog.this.pCallback);
                tmpCoorSystem.ShowDialog();
            }
        });
        SpinnerList tmpSpheroidSpinner = (SpinnerList) this._Dialog.findViewById(R.id.sp_SpheroidName);
        tmpSpheroidSpinner.SetClickCallback(false);
        Common.SetSpinnerListData(this._Dialog.getContext(), tmpSpheroidSpinner, "WGS84", "选择投影椭球", PubVar._PubCommand.m_ConfigDB.getSpheroidNameList(), "选择投影椭球返回", this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        int i = 1;
        while (i < 10000 && Common.CheckExistFile(String.valueOf(String.valueOf(PubVar.m_SystemPath) + "/Data/新建项目") + i)) {
            i++;
        }
        Common.SetTextViewValueOnID(this._Dialog, (int) R.id.pn_projectname, "新建项目" + i);
    }

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean saveProjectInfo() {
        new StringBuilder();
        List<String> tmpList01 = new ArrayList<>();
        List<String> tmpList02 = new ArrayList<>();
        tmpList01.add("ProjectName");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.pn_projectname));
        tmpList01.add("CreateTime");
        tmpList02.add(Common.GetSystemDate());
        tmpList01.add("CoorSystem");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.sp_coorsystem));
        tmpList01.add("PMTransMethod");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.sp_transmethod));
        tmpList01.add("CenterMeridian");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.sp_centerjx));
        tmpList01.add("BiasX");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.ppmove_p1));
        tmpList01.add("BiasY");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.ppmove_p2));
        tmpList01.add("BiasZ");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.ppmove_p3));
        tmpList01.add("P31");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.pp3_p1));
        tmpList01.add("P32");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.pp3_p2));
        tmpList01.add("P33");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.pp3_p3));
        tmpList01.add("P41");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.pp4_p1));
        tmpList01.add("P42");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.pp4_p2));
        tmpList01.add("P43");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.pp4_p3));
        tmpList01.add("P44");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.pp4_p4));
        tmpList01.add("P71");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.pp7_p1));
        tmpList01.add("P72");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.pp7_p2));
        tmpList01.add("P73");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.pp7_p3));
        tmpList01.add("P74");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.pp7_p4));
        tmpList01.add("P75");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.pp7_p5));
        tmpList01.add("P76");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.pp7_p6));
        tmpList01.add("P77");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.pp7_p7));
        tmpList01.add("F1");
        tmpList02.add(String.valueOf(this.FenDaiValue));
        tmpList01.add("F2");
        tmpList02.add(String.valueOf(this.DaiHaoValue));
        tmpList01.add("F3");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.sp_SpheroidName));
        tmpList01.add("F4");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.et_SpheroidA));
        tmpList01.add("F5");
        tmpList02.add(Common.GetViewValue(this._Dialog, R.id.et_Spheroidf));
        return false/*PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL(String.format("insert into %1$s (%2$s) values ('%3$s')", new String[]{"T_Project", C0321Common.CombineStrings(",", tmpList01), C0321Common.CombineStrings("','", tmpList02)}))*/;
    }

    public static boolean CreateProject(String projectName, String cooSystemName, String layerModuleName, String[] outMsg) {
        boolean result = false;
        try {
            outMsg[0] = "";
            if (Common.CheckProjectExist(projectName)) {
                outMsg[0] = "创建项目失败!项目【" + projectName + "】已经存在.";
                return false;
            }
            if (PubVar._PubCommand.m_ProjectDB.CreateProject(projectName)) {
                if (SaveProjectInfo(projectName, cooSystemName)) {
                    if (layerModuleName.trim().length() == 0) {
                        layerModuleName = "系统默认图层模板";
                    }
                    if (CreateLayerByTemplateName(projectName, layerModuleName)) {
                        if (cooSystemName.contains("WGS")) {
                            RasterLayer tempLayer = new RasterLayer();
                            tempLayer.SetLayerTypeName("Google卫星地图");
                            tempLayer.SetLayerName("Google卫星地图");
                            tempLayer.SetEditMode(EEditMode.NEW);
                            tempLayer.SaveLayerInfo();
                        }
                        result = true;
                    } else {
                        outMsg[0] = "根据图层模板创建图层失败.";
                    }
                } else {
                    outMsg[0] = "数据保存失败!请检查系统数据库是否正确或与开发者联系.";
                }
            }
            return result;
        } catch (Exception ex) {
            outMsg[0] = ex.getLocalizedMessage();
        }
        return result;
    }

    public static boolean SaveProjectInfo(String projectName, String coorSystemName) {
        try {
            List<String> tmpList01 = new ArrayList<>();
            List<String> tmpList02 = new ArrayList<>();
            tmpList01.add("ProjectName");
            tmpList02.add(projectName);
            tmpList01.add("CreateTime");
            tmpList02.add(Common.GetSystemDate());
            tmpList01.add("CoorSystem");
            tmpList02.add(coorSystemName);
            tmpList01.add("PMTransMethod");
            tmpList02.add("无");
            tmpList01.add("CenterMeridian");
            tmpList02.add("0");
            tmpList01.add("BiasX");
            tmpList02.add("0");
            tmpList01.add("BiasY");
            tmpList02.add("0");
            tmpList01.add("BiasZ");
            tmpList02.add("0");
            tmpList01.add("P31");
            tmpList02.add("0");
            tmpList01.add("P32");
            tmpList02.add("0");
            tmpList01.add("P33");
            tmpList02.add("0");
            tmpList01.add("P41");
            tmpList02.add("0");
            tmpList01.add("P42");
            tmpList02.add("0");
            tmpList01.add("P43");
            tmpList02.add("0");
            tmpList01.add("P44");
            tmpList02.add("0");
            tmpList01.add("P71");
            tmpList02.add("0");
            tmpList01.add("P72");
            tmpList02.add("0");
            tmpList01.add("P73");
            tmpList02.add("0");
            tmpList01.add("P74");
            tmpList02.add("0");
            tmpList01.add("P75");
            tmpList02.add("0");
            tmpList01.add("P76");
            tmpList02.add("0");
            tmpList01.add("P77");
            tmpList02.add("0");
            tmpList01.add("F1");
            tmpList02.add("0");
            tmpList01.add("F2");
            tmpList02.add("0");
            if (PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL(String.format("insert into %1$s (%2$s) values ('%3$s')", "T_Project", Common.CombineStrings(",", tmpList01), Common.CombineStrings("','", tmpList02)))) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    /* access modifiers changed from: private */
    public static boolean CreateLayerByTemplateName(String projectName, String prjModuleName) {
        boolean result = false;
        try {
            if (prjModuleName.equals("无")) {
                return true;
            }
            List<FeatureLayer> localList = PubVar._PubCommand.m_UserConfigDB.GetLayerTemplate().ReadLayerTemplate(prjModuleName);
            if (localList == null || localList.size() == 0) {
                return false;
            }
            DataSource localDataSource = new DataSource(String.valueOf(PubVar.m_SystemPath) + "/Data/" + projectName + "/TAData.dbx", "", 0);
            for (FeatureLayer tempLayer : localList) {
                if (tempLayer.SaveLayerInfo()) {
                    localDataSource.CreateDataset(tempLayer.GetLayerID());
                }
            }
            result = true;
            return result;
        } catch (Exception ex) {
            Common.Log("错误", "新建项目时(CBTN):" + ex.getMessage());
        }
        return result;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Create_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Project_Create_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
