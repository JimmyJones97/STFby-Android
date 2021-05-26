package  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.CommonEvent;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Navigation.Navigation_Dialog;
import  com.xzy.forestSystem.gogisapi.Others.MediaManag_Dialog;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TubanInfo_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private FeatureLayer m_FeatureLayer;
    private GeoLayer m_GeoLayer;
    private AbstractGeometry m_Geometry;
    private Button m_MediaButton;
    private String m_MediaInfoString;
    private boolean m_NeedSave;
    private InputSpinner m_PartIndexSpinnerList;
    private SQLiteDBHelper m_SQLiteDBHelper;
    private String m_SYSID;
    private SpinnerList m_SpinnerList01;
    private SpinnerList m_SpinnerList02;
    private SpinnerList m_SpinnerList03;
    private SpinnerList m_SpinnerList04;
    private SpinnerList m_SpinnerList05;
    private int[] m_baseViewIDs;
    private ICallback pCallback;

    public void setGeoLayer(GeoLayer geoLayer) {
        this.m_GeoLayer = geoLayer;
    }

    public void setSYSID(String sysID) {
        this.m_SYSID = sysID;
    }

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public TubanInfo_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_SQLiteDBHelper = null;
        this.m_GeoLayer = null;
        this.m_FeatureLayer = null;
        this.m_SYSID = "";
        this.m_SpinnerList01 = null;
        this.m_SpinnerList02 = null;
        this.m_SpinnerList03 = null;
        this.m_SpinnerList04 = null;
        this.m_SpinnerList05 = null;
        this.m_PartIndexSpinnerList = null;
        this.m_MediaButton = null;
        this.m_NeedSave = false;
        this.m_MediaInfoString = "";
        this.m_Geometry = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.TubanInfo_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                Object[] tmpObjs;
                Object[] tmpObjs2;
                GeoLayer tmpGeoLayer;
                AbstractGeometry tmpGeometry;
                Object[] tmpObjs3;
                GeoLayer tmpGeoLayer2;
                AbstractGeometry tmpGeometry2;
                try {
                    if (command.equals("选择省返回")) {
                        String tmpString = String.valueOf(object);
                        if (tmpString.length() > 0) {
                            List<String> tmpList01 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader = TubanInfo_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct Xian From T_CheckPartsInfo Where Sheng ='" + tmpString + "'");
                            while (tmpSQLiteReader.Read()) {
                                tmpList01.add(tmpSQLiteReader.GetString(0));
                            }
                            if (tmpList01.size() > 0) {
                                TubanInfo_Dialog.this.m_SpinnerList02.setEnabled(true);
                                Common.SetSpinnerListData(TubanInfo_Dialog.this._Dialog, (int) R.id.sp_Yangdi02, "", "请选择县局", tmpList01, "选择县返回", TubanInfo_Dialog.this.pCallback);
                                return;
                            }
                            TubanInfo_Dialog.this.m_SpinnerList02.setEnabled(false);
                        }
                    } else if (command.equals("选择县返回")) {
                        String tmpString2 = String.valueOf(object);
                        if (tmpString2.length() > 0) {
                            String tmpString22 = Common.GetViewValue(TubanInfo_Dialog.this._Dialog, R.id.sp_Yangdi01);
                            List<String> tmpList012 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader2 = TubanInfo_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct Xiang From T_CheckPartsInfo Where Sheng ='" + tmpString22 + "' And Xian='" + tmpString2 + "'");
                            while (tmpSQLiteReader2.Read()) {
                                tmpList012.add(tmpSQLiteReader2.GetString(0));
                            }
                            if (tmpList012.size() > 0) {
                                TubanInfo_Dialog.this.m_SpinnerList03.setEnabled(true);
                                Common.SetSpinnerListData(TubanInfo_Dialog.this._Dialog, (int) R.id.sp_Yangdi03, "", "请选择乡镇", tmpList012, "选择乡镇返回", TubanInfo_Dialog.this.pCallback);
                                return;
                            }
                            TubanInfo_Dialog.this.m_SpinnerList03.setEnabled(false);
                        }
                    } else if (command.equals("选择乡镇返回")) {
                        String tmpString3 = String.valueOf(object);
                        if (tmpString3.length() > 0) {
                            String tmpString23 = Common.GetViewValue(TubanInfo_Dialog.this._Dialog, R.id.sp_Yangdi01);
                            String tmpString32 = Common.GetViewValue(TubanInfo_Dialog.this._Dialog, R.id.sp_Yangdi02);
                            List<String> tmpList013 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader3 = TubanInfo_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct Cun From T_CheckPartsInfo Where Sheng ='" + tmpString23 + "' And Xian='" + tmpString32 + "' And Xiang='" + tmpString3 + "'");
                            while (tmpSQLiteReader3.Read()) {
                                tmpList013.add(tmpSQLiteReader3.GetString(0));
                            }
                            if (tmpList013.size() > 0) {
                                TubanInfo_Dialog.this.m_SpinnerList04.setEnabled(true);
                                Common.SetSpinnerListData(TubanInfo_Dialog.this._Dialog, (int) R.id.sp_Yangdi04, "", "请选择村", tmpList013, "选择村返回", TubanInfo_Dialog.this.pCallback);
                                return;
                            }
                            TubanInfo_Dialog.this.m_SpinnerList04.setEnabled(true);
                        }
                    } else if (command.equals("选择村返回")) {
                        String tmpString4 = String.valueOf(object);
                        if (tmpString4.length() > 0) {
                            String tmpString24 = Common.GetViewValue(TubanInfo_Dialog.this._Dialog, R.id.sp_Yangdi01);
                            String tmpString33 = Common.GetViewValue(TubanInfo_Dialog.this._Dialog, R.id.sp_Yangdi02);
                            String tmpString42 = Common.GetViewValue(TubanInfo_Dialog.this._Dialog, R.id.sp_Yangdi03);
                            List<String> tmpList014 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader4 = TubanInfo_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct PartIndex From T_CheckPartsInfo Where Sheng ='" + tmpString24 + "' And Xian='" + tmpString33 + "' And Xiang='" + tmpString42 + "' And Cun='" + tmpString4 + "'");
                            while (tmpSQLiteReader4.Read()) {
                                tmpList014.add(tmpSQLiteReader4.GetString(0));
                            }
                            if (tmpList014.size() > 0) {
                                TubanInfo_Dialog.this.m_SpinnerList05.setEnabled(true);
                                Common.SetSpinnerListData(TubanInfo_Dialog.this._Dialog, (int) R.id.sp_Yangdi05, "", "请选择图斑", tmpList014, "选择图斑返回", TubanInfo_Dialog.this.pCallback);
                                return;
                            }
                            TubanInfo_Dialog.this.m_SpinnerList05.setEnabled(true);
                        }
                    } else if (command.equals("多媒体返回")) {
                        if (object != null) {
                            String tmpResult = "";
                            String[] tmpStrs = String.valueOf(object).split(",");
                            int tmpI = 0;
                            for (String tmpStr01 : tmpStrs) {
                                if (tmpStr01.trim().length() > 0) {
                                    if (tmpResult.length() > 0) {
                                        tmpResult = String.valueOf(tmpResult) + ",";
                                    }
                                    tmpResult = String.valueOf(tmpResult) + tmpStr01.trim();
                                    tmpI++;
                                }
                            }
                            TubanInfo_Dialog.this.m_MediaInfoString = tmpResult;
                            TubanInfo_Dialog.this.m_MediaButton.setText("[" + String.valueOf(tmpI) + "]照片");
                            TubanInfo_Dialog.this.saveMediaInfo();
                        }
                    } else if (command.equals("确定")) {
                        if (TubanInfo_Dialog.this.saveData()) {
                            if (TubanInfo_Dialog.this.m_Callback != null) {
                                TubanInfo_Dialog.this.m_Callback.OnClick("图斑检查数据编辑返回", TubanInfo_Dialog.this.m_SYSID);
                            }
                            TubanInfo_Dialog.this._Dialog.dismiss();
                        }
                    } else if (command.equals("返回")) {
                        Common.ShowYesNoDialogWithAlert(TubanInfo_Dialog.this._Dialog.getContext(), "是否确定退出?\r\n\r\n提示:退出时将不会保存修改的数据.", true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.TubanInfo_Dialog.1.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String command2, Object pObject) {
                                if (command2.equals("YES")) {
                                    TubanInfo_Dialog.this._Dialog.dismiss();
                                }
                            }
                        });
                    } else if (command.equals("拾取改变林地用途面积返回")) {
                        TubanInfo_Dialog.this._Dialog.show();
                        if (!(object == null || (tmpObjs3 = (Object[]) object) == null || tmpObjs3.length <= 2 || (tmpGeoLayer2 = PubVar._Map.GetGeoLayerByDataSource(String.valueOf(tmpObjs3[2]), String.valueOf(tmpObjs3[0]))) == null || (tmpGeometry2 = tmpGeoLayer2.getDataset().GetGeometryBySYSIDMust(String.valueOf(tmpObjs3[1]))) == null)) {
                            if (tmpGeometry2 instanceof Polygon) {
                                Common.SetEditTextValueOnID(TubanInfo_Dialog.this._Dialog, R.id.et_GaiBianLindiArea, Common.SimplifyArea(((Polygon) tmpGeometry2).getArea(true), false));
                                Common.ShowToast("拾取面积成功.");
                                return;
                            }
                            Common.ShowToast("拾取的不是面对象.");
                        }
                    } else if (command.equals("拾取违法改变林地用途面积返回")) {
                        TubanInfo_Dialog.this._Dialog.show();
                        if (!(object == null || (tmpObjs2 = (Object[]) object) == null || tmpObjs2.length <= 2 || (tmpGeoLayer = PubVar._Map.GetGeoLayerByDataSource(String.valueOf(tmpObjs2[2]), String.valueOf(tmpObjs2[0]))) == null || (tmpGeometry = tmpGeoLayer.getDataset().GetGeometryBySYSIDMust(String.valueOf(tmpObjs2[1]))) == null)) {
                            if (tmpGeometry instanceof Polygon) {
                                Common.SetEditTextValueOnID(TubanInfo_Dialog.this._Dialog, R.id.et_WeiGuiGaiBianLindiArea, Common.SimplifyArea(((Polygon) tmpGeometry).getArea(true), false));
                                Common.ShowToast("拾取面积成功.");
                                return;
                            }
                            Common.ShowToast("拾取的不是面对象.");
                        }
                    } else if (command.equals("计算图斑蓄积返回") && object != null && (object instanceof Object[]) && (tmpObjs = (Object[]) object) != null && tmpObjs.length > 1) {
                        String tmpString5 = String.valueOf(tmpObjs[0]);
                        if (tmpString5.equals("采伐林木蓄积")) {
                            Common.SetEditTextValueOnID(TubanInfo_Dialog.this._Dialog, R.id.et_CaiFaLinMuXuJi, String.valueOf(tmpObjs[1]));
                        } else if (tmpString5.equals("违法违规采伐林木蓄积")) {
                            Common.SetEditTextValueOnID(TubanInfo_Dialog.this._Dialog, R.id.et_WeiFaWeiGuiCaiFaLinMuXuJi, String.valueOf(tmpObjs[1]));
                        }
                    }
                } catch (Exception e) {
                    Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
                }
            }
        };
        this.m_baseViewIDs = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.tubaninfo_dialog);
        this._Dialog.Resize(1.0f, 1.0f);
        this._Dialog.setFullScreen();
        this._Dialog.SetCaption("图斑检查验证");
        this._Dialog.SetHeadButtons("1,2130837858,保存,确定", this.pCallback);
        this._Dialog.SetAllowedCloseDialog(false);
        this._Dialog.SetCallback(this.pCallback);
        this._Dialog.SetCancelCommand("");
        this.m_baseViewIDs = new int[]{R.id.sp_Yangdi01, R.id.sp_Yangdi02, R.id.sp_Yangdi03, R.id.sp_Yangdi04, R.id.et_TuBanHao, R.id.et_DiaoChaNian, R.id.et_QianQiYXTime, R.id.et_HouQiYXTime, R.id.et_X, R.id.et_Y, R.id.et_PanDuArea, R.id.et_PanDuRemark, R.id.sp_QianDiLei, R.id.sp_XianDiLei, R.id.et_ZhongDianQuyuName, R.id.et_GaiBianLindiArea, R.id.et_WeiGuiGaiBianLindiArea, R.id.et_CaiFaLinMuXuJi, R.id.et_WeiFaWeiGuiCaiFaLinMuXuJi, R.id.sp_TubanBianHuaYY, R.id.sp_JianChaClass, R.id.sp_JianChaJieGuoSame, R.id.et_XianDiRemark, R.id.et_JianChaPart, R.id.et_JianChaUser, R.id.et_JianChaTime};
        List<String> tmpList01 = new ArrayList<>();
        tmpList01.add("林地");
        tmpList01.add("非林地");
        Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_QianDiLei, "", "请选择前地类", tmpList01, (String) null, (ICallback) null);
        Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_XianDiLei, "", "请选择现地类", tmpList01, (String) null, (ICallback) null);
        List<String> tmpList02 = new ArrayList<>();
        tmpList02.add("建设项目使用林地(不包括直接为林业生产服务)");
        tmpList02.add("直接为林业生产服务");
        tmpList02.add("毁林(湿)开垦");
        tmpList02.add("土地整理");
        tmpList02.add("林木采伐");
        tmpList02.add("造林、抚育及其他森林经营活动");
        tmpList02.add("森林病虫害、森林火灾");
        tmpList02.add("地质灾害等自然原因");
        tmpList02.add("植物季节性变化");
        tmpList02.add("遥感数据质量");
        tmpList02.add("前地类是非林地变化");
        tmpList02.add("其他原因");
        Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_TubanBianHuaYY, "", "请选择图斑变化原因", tmpList02, (String) null, (ICallback) null);
        List<String> tmpList03 = new ArrayList<>();
        tmpList03.add("县级");
        tmpList03.add("市级");
        tmpList03.add("省级");
        tmpList03.add("直属院");
        tmpList03.add("专员办");
        Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_JianChaClass, "", "请选择检查级别", tmpList03, (String) null, (ICallback) null);
        List<String> tmpList04 = new ArrayList<>();
        tmpList04.add("一致");
        tmpList04.add("不一致");
        tmpList04.add("县级检查");
        Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_JianChaJieGuoSame, "", "请选择检查结果是否一致", tmpList04, (String) null, (ICallback) null);
        this.m_MediaButton = (Button) this._Dialog.findViewById(R.id.btn_mediaMang);
        this.m_MediaButton.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.TubanInfo_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                MediaManag_Dialog tempDialog = new MediaManag_Dialog(TubanInfo_Dialog.this._Dialog.getContext());
                tempDialog.ReSetSize(1.0f, 0.96f);
                tempDialog.SetCaption("图斑");
                tempDialog.SetPhotoInfo(TubanInfo_Dialog.this.m_MediaInfoString);
                tempDialog.SetCallback(TubanInfo_Dialog.this.pCallback);
                tempDialog.show();
            }
        });
        this._Dialog.findViewById(R.id.btn_Nav).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.TubanInfo_Dialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                Coordinate tmpCoordinate;
                if (TubanInfo_Dialog.this.m_Geometry != null) {
                    if (TubanInfo_Dialog.this.m_Geometry instanceof Polygon) {
                        tmpCoordinate = ((Polygon) TubanInfo_Dialog.this.m_Geometry).getInnerPoint();
                    } else {
                        tmpCoordinate = TubanInfo_Dialog.this.m_Geometry.getPoint(0);
                    }
                    if (tmpCoordinate != null) {
                        Navigation_Dialog.NavigateByMap(tmpCoordinate);
                    }
                }
            }
        });
        this._Dialog.findViewById(R.id.btn_More).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.TubanInfo_Dialog.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (TubanInfo_Dialog.this.m_Geometry != null) {
                    FeatureAttribute_Dialog tmpFeatAttrDialog = new FeatureAttribute_Dialog();
                    tmpFeatAttrDialog.SetGeometryObject(TubanInfo_Dialog.this.m_GeoLayer.getLayerID(), TubanInfo_Dialog.this.m_GeoLayer.getDataset().getDataSource().getName(), TubanInfo_Dialog.this.m_Geometry);
                    tmpFeatAttrDialog.ShowDialog();
                    TubanInfo_Dialog.this._Dialog.dismiss();
                }
            }
        });
        this._Dialog.findViewById(R.id.btn_GetArea01).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_GetArea02).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_GetArea03).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_GetArea04).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_CalXuJi).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_CalXuJi2).setOnClickListener(new ViewClick());
    }

    private FeatureLayer getFeatureLayer() {
        if (this.m_FeatureLayer == null && this.m_GeoLayer != null) {
            this.m_FeatureLayer = PubVar._PubCommand.m_ProjectDB.getFeatureLayerByID(this.m_GeoLayer.getLayerID());
        }
        return this.m_FeatureLayer;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshValues() {
        try {
            if (this.m_SYSID.length() > 0 && getFeatureLayer() != null) {
                this.m_MediaInfoString = this.m_GeoLayer.getDataset().getGeoemtryMediaInfoBySYSID(this.m_SYSID);
                String[] tmpStrs02 = this.m_MediaInfoString.split(",");
                if (tmpStrs02 != null && tmpStrs02.length > 0 && tmpStrs02[0].trim().length() > 0) {
                    this.m_MediaButton.setText("[" + String.valueOf(tmpStrs02.length) + "]照片");
                }
                this.m_Geometry = this.m_GeoLayer.getDataset().GetGeometryBySYSIDMust(this.m_SYSID);
                HashMap<String, Object> tmpHashMap = this.m_GeoLayer.getDataset().getGeometryFieldValuesBySYSID(this.m_SYSID, null);
                for (int tmpViewID : this.m_baseViewIDs) {
                    View tmpView = this._Dialog.findViewById(tmpViewID);
                    if (!(tmpView == null || tmpView.getTag() == null)) {
                        String tmpFIDName2 = CommonSetting.m_DuChaLayerMustFieldsName.get(String.valueOf(tmpView.getTag()));
                        if (tmpHashMap.containsKey(tmpFIDName2)) {
                            Object tmpObject = tmpHashMap.get(tmpFIDName2);
                            if (tmpObject == null) {
                                tmpObject = "";
                            }
                            Common.SetValueToView(String.valueOf(tmpObject), tmpView);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        EditText tmpEditText = (EditText) this._Dialog.findViewById(R.id.et_JianChaTime);
        if (tmpEditText.getText().toString().trim().length() == 0) {
            tmpEditText.setText(Common.dateSmallFormat2.format(new Date()));
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean saveData() {
        FeatureLayer tmpFeatureLayer;
        boolean tmpBool = false;
        try {
            if (this.m_SYSID.length() > 0 && (tmpFeatureLayer = getFeatureLayer()) != null) {
                HashMap<String, Object> tmpHashMap = new HashMap<>();
                for (int tmpViewID : this.m_baseViewIDs) {
                    View tmpView = this._Dialog.findViewById(tmpViewID);
                    if (!(tmpView == null || tmpView.getTag() == null)) {
                        String tmpFID = tmpFeatureLayer.GetDataFieldByFieldName(CommonSetting.m_DuChaLayerMustFieldsName.get(String.valueOf(tmpView.getTag())));
                        if (tmpFID.length() > 0) {
                            tmpHashMap.put(tmpFID, Common.GetViewValue(tmpView));
                        }
                    }
                }
                tmpBool = this.m_GeoLayer.getDataset().UpdateFieldsValue(this.m_SYSID, tmpHashMap);
            }
            if (this.m_SQLiteDBHelper != null) {
                String tmpString = ((EditText) this._Dialog.findViewById(R.id.et_JianChaPart)).getEditableText().toString().trim();
                if (tmpString.length() > 0) {
                    this.m_SQLiteDBHelper.ExecuteSQL("Replace Into T_SysParams (ParaName,ParaValue) Values ('检查单位名称','" + tmpString + "')");
                }
                String tmpString2 = ((EditText) this._Dialog.findViewById(R.id.et_JianChaUser)).getEditableText().toString().trim();
                if (tmpString2.length() > 0) {
                    this.m_SQLiteDBHelper.ExecuteSQL("Replace Into T_SysParams (ParaName,ParaValue) Values ('检查人员','" + tmpString2 + "')");
                }
            }
        } catch (Exception e) {
        }
        return tmpBool;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean saveMediaInfo() {
        if (this.m_SYSID.length() > 0) {
            return this.m_GeoLayer.getDataset().UpdateFieldsValue(this.m_SYSID, null, this.m_MediaInfoString);
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshDatabase() {
        try {
            String tmpPath = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/SenLinDuCha.dbx";
            if (new File(tmpPath).exists()) {
                this.m_SQLiteDBHelper = new SQLiteDBHelper(tmpPath);
                new ArrayList();
                SQLiteReader tmpSQLiteReader = this.m_SQLiteDBHelper.Query("Select ParaValue From T_SysParams Where ParaName='检查单位名称'");
                if (tmpSQLiteReader.Read()) {
                    String tmpString01 = tmpSQLiteReader.GetString(0).trim();
                    if (tmpString01.length() > 0) {
                        ((EditText) this._Dialog.findViewById(R.id.et_JianChaPart)).setHint(tmpString01);
                        View tmpView = this._Dialog.findViewById(R.id.tv_JianChaPart);
                        tmpView.setTag(tmpString01);
                        tmpView.setOnLongClickListener(new View.OnLongClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.TubanInfo_Dialog.5
                            @Override // android.view.View.OnLongClickListener
                            public boolean onLongClick(View v) {
                                String tmpNameString = String.valueOf(v.getTag());
                                Common.SetEditTextValueOnID(TubanInfo_Dialog.this._Dialog, R.id.et_JianChaPart, tmpNameString);
                                Common.ShowToast("设置检查单位名称为“" + tmpNameString + "”");
                                return false;
                            }
                        });
                    }
                }
                SQLiteReader tmpSQLiteReader2 = this.m_SQLiteDBHelper.Query("Select ParaValue From T_SysParams Where ParaName='检查人员'");
                if (tmpSQLiteReader2.Read()) {
                    String tmpString012 = tmpSQLiteReader2.GetString(0).trim();
                    if (tmpString012.length() > 0) {
                        ((EditText) this._Dialog.findViewById(R.id.et_JianChaUser)).setHint(tmpString012);
                        View tmpView2 = this._Dialog.findViewById(R.id.tv_JianChaUser);
                        tmpView2.setTag(tmpString012);
                        tmpView2.setOnLongClickListener(new View.OnLongClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.TubanInfo_Dialog.6
                            @Override // android.view.View.OnLongClickListener
                            public boolean onLongClick(View v) {
                                String tmpNameString = String.valueOf(v.getTag());
                                Common.SetEditTextValueOnID(TubanInfo_Dialog.this._Dialog, R.id.et_JianChaUser, tmpNameString);
                                Common.ShowToast("设置检查人员为“" + tmpNameString + "”");
                                return false;
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
    }

    private String getTuBanName() {
        String tmpString = Common.GetViewValue(this._Dialog, R.id.sp_Yangdi01);
        String resultString = String.valueOf(tmpString) + "_" + Common.GetViewValue(this._Dialog, R.id.sp_Yangdi02);
        String resultString2 = String.valueOf(resultString) + "_" + Common.GetViewValue(this._Dialog, R.id.sp_Yangdi03);
        String resultString3 = String.valueOf(resultString2) + "_" + Common.GetViewValue(this._Dialog, R.id.sp_Yangdi04);
        return String.valueOf(resultString3) + "_" + Common.GetViewValue(this._Dialog, R.id.et_TuBanHao);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("获取面积")) {
                if (this.m_Geometry == null) {
                    Common.ShowToast("未找到该图斑的几何对象.");
                } else if (this.m_Geometry instanceof Polygon) {
                    Common.SetEditTextValueOnID(this._Dialog, R.id.et_GaiBianLindiArea, Common.SimplifyArea(((Polygon) this.m_Geometry).getArea(true), false));
                }
            } else if (command.equals("拾取面积")) {
                CommonEvent tmpCommonEvent = new CommonEvent();
                tmpCommonEvent.Dialog = this._Dialog;
                tmpCommonEvent.returnCallback = this.pCallback;
                tmpCommonEvent.returnBackCommand = "拾取改变林地用途面积返回";
                tmpCommonEvent.mEventType = CommonEvent.EventType.Select;
                PubVar._PubCommand.SetCommonEvent(tmpCommonEvent);
                this._Dialog.hide();
                Common.ShowToast("请在地图上拾取需要获取面积的面对象.");
            } else if (command.equals("获取面积2")) {
                if (this.m_Geometry == null) {
                    Common.ShowToast("未找到该图斑的几何对象.");
                } else if (this.m_Geometry instanceof Polygon) {
                    Common.SetEditTextValueOnID(this._Dialog, R.id.et_WeiGuiGaiBianLindiArea, Common.SimplifyArea(((Polygon) this.m_Geometry).getArea(true), false));
                }
            } else if (command.equals("拾取面积2")) {
                CommonEvent tmpCommonEvent2 = new CommonEvent();
                tmpCommonEvent2.Dialog = this._Dialog;
                tmpCommonEvent2.returnCallback = this.pCallback;
                tmpCommonEvent2.returnBackCommand = "拾取违法改变林地用途面积返回";
                tmpCommonEvent2.mEventType = CommonEvent.EventType.Select;
                PubVar._PubCommand.SetCommonEvent(tmpCommonEvent2);
                this._Dialog.hide();
                Common.ShowToast("请在地图上拾取需要获取面积的面对象.");
            } else if (command.equals("计算蓄积")) {
                String tmpTUbanName = getTuBanName();
                if (tmpTUbanName.trim().length() == 0) {
                    Common.ShowDialog("图斑所在的“省、县、乡镇、村、图斑号”信息不能为空.");
                    return;
                }
                double tmpArea = 0.0d;
                try {
                    String tmpString = Common.GetEditTextValueOnID(this._Dialog, R.id.et_GaiBianLindiArea);
                    if (tmpString.trim().length() > 0) {
                        tmpArea = Double.parseDouble(tmpString);
                    }
                } catch (Exception e) {
                }
                if (tmpArea == 0.0d) {
                    Common.ShowDialog("【改变林地用途或采伐林木面积】不能为空或0.\r\n请先填写该面积值后再进行此操作.");
                    return;
                }
                CalTubanYangdiCaiJi_Dialog tmpDialog = new CalTubanYangdiCaiJi_Dialog(tmpTUbanName, tmpArea);
                tmpDialog.SetCallback(this.pCallback);
                tmpDialog.m_ReturnTag = "采伐林木蓄积";
                tmpDialog.ShowDialog();
            } else if (command.equals("计算蓄积2")) {
                String tmpTUbanName2 = getTuBanName();
                if (tmpTUbanName2.trim().length() == 0) {
                    Common.ShowDialog("图斑所在的“省、县、乡镇、村、图斑号”信息不能为空.");
                    return;
                }
                double tmpArea2 = 0.0d;
                try {
                    String tmpString2 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_WeiGuiGaiBianLindiArea);
                    if (tmpString2.trim().length() > 0) {
                        tmpArea2 = Double.parseDouble(tmpString2);
                    }
                } catch (Exception e2) {
                }
                if (tmpArea2 == 0.0d) {
                    Common.ShowDialog("【违法违规改变林地用途或采伐林木面积】不能为空或0.\r\n请先填写该面积值后再进行此操作.");
                    return;
                }
                CalTubanYangdiCaiJi_Dialog tmpDialog2 = new CalTubanYangdiCaiJi_Dialog(tmpTUbanName2, tmpArea2);
                tmpDialog2.SetCallback(this.pCallback);
                tmpDialog2.m_ReturnTag = "违法违规采伐林木蓄积";
                tmpDialog2.ShowDialog();
            }
        } catch (Exception e3) {
            Common.ShowDialog("错误:\r\n" + e3.getLocalizedMessage());
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.TubanInfo_Dialog.7
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                if (PubVar.AreaUnitType != 3) {
                    Common.ShowYesNoDialogWithAlert(TubanInfo_Dialog.this._Dialog.getContext(), "当前系统中设置的面积单位不是公顷.是否设置面积单位为公顷?\r\n\r\n提示:如需自行修改面积单位请在“系统设置”中修改.", true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.TubanInfo_Dialog.7.1
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command, Object pObject) {
                            if (command.equals("YES")) {
                                PubVar.AreaUnitType = 3;
                                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_AreaUnitType", Integer.valueOf(PubVar.AreaUnitType));
                            }
                        }
                    });
                }
                TubanInfo_Dialog.this.refreshValues();
                TubanInfo_Dialog.this.refreshDatabase();
            }
        });
        this._Dialog.show();
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                TubanInfo_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
