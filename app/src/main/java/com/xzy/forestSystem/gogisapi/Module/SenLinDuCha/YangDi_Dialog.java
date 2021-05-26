package  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.GPS.AddGPSPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Selection;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class YangDi_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private InputSpinner m_PartIndexSpinnerList;
    private SQLiteDBHelper m_SQLiteDBHelper;
    private SpinnerList m_SpinnerList01;
    private SpinnerList m_SpinnerList02;
    private SpinnerList m_SpinnerList03;
    private SpinnerList m_SpinnerList04;
    private SpinnerList m_SpinnerList05;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public YangDi_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_SQLiteDBHelper = null;
        this.m_SpinnerList01 = null;
        this.m_SpinnerList02 = null;
        this.m_SpinnerList03 = null;
        this.m_SpinnerList04 = null;
        this.m_SpinnerList05 = null;
        this.m_PartIndexSpinnerList = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDi_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("选择省返回")) {
                        String tmpString = String.valueOf(object);
                        if (tmpString.length() > 0) {
                            List<String> tmpList01 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader = YangDi_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct Xian From T_CheckPartsInfo Where Sheng ='" + tmpString + "'");
                            while (tmpSQLiteReader.Read()) {
                                tmpList01.add(tmpSQLiteReader.GetString(0));
                            }
                            if (tmpList01.size() > 0) {
                                YangDi_Dialog.this.m_SpinnerList02.setEnabled(true);
                                Common.SetSpinnerListData(YangDi_Dialog.this._Dialog, (int) R.id.sp_Yangdi02, "", "请选择县局", tmpList01, "选择县返回", YangDi_Dialog.this.pCallback);
                                return;
                            }
                            YangDi_Dialog.this.m_SpinnerList02.setEnabled(false);
                        }
                    } else if (command.equals("选择县返回")) {
                        String tmpString2 = String.valueOf(object);
                        if (tmpString2.length() > 0) {
                            String tmpString22 = Common.GetViewValue(YangDi_Dialog.this._Dialog, R.id.sp_Yangdi01);
                            List<String> tmpList012 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader2 = YangDi_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct Xiang From T_CheckPartsInfo Where Sheng ='" + tmpString22 + "' And Xian='" + tmpString2 + "'");
                            while (tmpSQLiteReader2.Read()) {
                                tmpList012.add(tmpSQLiteReader2.GetString(0));
                            }
                            if (tmpList012.size() > 0) {
                                YangDi_Dialog.this.m_SpinnerList03.setEnabled(true);
                                Common.SetSpinnerListData(YangDi_Dialog.this._Dialog, (int) R.id.sp_Yangdi03, "", "请选择乡镇", tmpList012, "选择乡镇返回", YangDi_Dialog.this.pCallback);
                                return;
                            }
                            YangDi_Dialog.this.m_SpinnerList03.setEnabled(false);
                        }
                    } else if (command.equals("选择乡镇返回")) {
                        String tmpString3 = String.valueOf(object);
                        if (tmpString3.length() > 0) {
                            String tmpString23 = Common.GetViewValue(YangDi_Dialog.this._Dialog, R.id.sp_Yangdi01);
                            String tmpString32 = Common.GetViewValue(YangDi_Dialog.this._Dialog, R.id.sp_Yangdi02);
                            List<String> tmpList013 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader3 = YangDi_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct Cun From T_CheckPartsInfo Where Sheng ='" + tmpString23 + "' And Xian='" + tmpString32 + "' And Xiang='" + tmpString3 + "'");
                            while (tmpSQLiteReader3.Read()) {
                                tmpList013.add(tmpSQLiteReader3.GetString(0));
                            }
                            if (tmpList013.size() > 0) {
                                YangDi_Dialog.this.m_SpinnerList04.setEnabled(true);
                                Common.SetSpinnerListData(YangDi_Dialog.this._Dialog, (int) R.id.sp_Yangdi04, "", "请选择村", tmpList013, "选择村返回", YangDi_Dialog.this.pCallback);
                                return;
                            }
                            YangDi_Dialog.this.m_SpinnerList04.setEnabled(true);
                        }
                    } else if (command.equals("选择村返回")) {
                        String tmpString4 = String.valueOf(object);
                        if (tmpString4.length() > 0) {
                            String tmpString24 = Common.GetViewValue(YangDi_Dialog.this._Dialog, R.id.sp_Yangdi01);
                            String tmpString33 = Common.GetViewValue(YangDi_Dialog.this._Dialog, R.id.sp_Yangdi02);
                            String tmpString42 = Common.GetViewValue(YangDi_Dialog.this._Dialog, R.id.sp_Yangdi03);
                            List<String> tmpList014 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader4 = YangDi_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct PartIndex From T_CheckPartsInfo Where Sheng ='" + tmpString24 + "' And Xian='" + tmpString33 + "' And Xiang='" + tmpString42 + "' And Cun='" + tmpString4 + "'");
                            while (tmpSQLiteReader4.Read()) {
                                tmpList014.add(tmpSQLiteReader4.GetString(0));
                            }
                            if (tmpList014.size() > 0) {
                                YangDi_Dialog.this.m_SpinnerList05.setEnabled(true);
                                Common.SetSpinnerListData(YangDi_Dialog.this._Dialog, (int) R.id.sp_Yangdi05, "", "请选择图斑", tmpList014, "选择图斑返回", YangDi_Dialog.this.pCallback);
                                return;
                            }
                            YangDi_Dialog.this.m_SpinnerList05.setEnabled(true);
                        }
                    } else if (command.equals("选择图斑返回")) {
                        YangDi_Dialog.this.m_PartIndexSpinnerList.setEnabled(true);
                    } else if (command.equals("添加GPS采样点返回")) {
                        Coordinate tempCoord = (Coordinate) object;
                        if (tempCoord != null) {
                            Common.SetEditTextValueOnID(YangDi_Dialog.this._Dialog, R.id.et_X, String.valueOf(tempCoord.getX()));
                            Common.SetEditTextValueOnID(YangDi_Dialog.this._Dialog, R.id.et_Y, String.valueOf(tempCoord.getY()));
                            Common.ShowToast("设置GPS坐标数据成功!");
                        }
                    } else if (command.equals("确定")) {
                        try {
                            String[] tmpDataStrings = new String[11];
                            String tmpString5 = Common.GetViewValue(YangDi_Dialog.this._Dialog, R.id.sp_Yangdi01).trim();
                            if (tmpString5.length() == 0) {
                                Common.ShowDialog("请选择省区.");
                                return;
                            }
                            tmpDataStrings[2] = tmpString5;
                            String tmpTotalNameString = String.valueOf("") + tmpString5;
                            String tmpString6 = Common.GetViewValue(YangDi_Dialog.this._Dialog, R.id.sp_Yangdi02).trim();
                            if (tmpString6.length() == 0) {
                                Common.ShowDialog("请选择县局.");
                                return;
                            }
                            tmpDataStrings[3] = tmpString6;
                            String tmpTotalNameString2 = String.valueOf(tmpTotalNameString) + "_" + tmpString6;
                            String tmpString7 = Common.GetViewValue(YangDi_Dialog.this._Dialog, R.id.sp_Yangdi03).trim();
                            if (tmpString7.length() == 0) {
                                Common.ShowDialog("请选择乡镇.");
                                return;
                            }
                            tmpDataStrings[4] = tmpString7;
                            String tmpTotalNameString3 = String.valueOf(tmpTotalNameString2) + "_" + tmpString7;
                            String tmpString8 = Common.GetViewValue(YangDi_Dialog.this._Dialog, R.id.sp_Yangdi04).trim();
                            if (tmpString8.length() == 0) {
                                Common.ShowDialog("请选择村.");
                                return;
                            }
                            tmpDataStrings[5] = tmpString8;
                            String tmpTotalNameString4 = String.valueOf(tmpTotalNameString3) + "_" + tmpString8;
                            String tmpString9 = Common.GetViewValue(YangDi_Dialog.this._Dialog, R.id.sp_Yangdi05).trim();
                            if (tmpString9.length() == 0) {
                                Common.ShowDialog("请选择图斑.");
                                return;
                            }
                            tmpDataStrings[6] = tmpString9;
                            String tmpTotalNameString5 = String.valueOf(tmpTotalNameString4) + "_" + tmpString9;
                            String tmpString10 = Common.GetViewValue(YangDi_Dialog.this._Dialog, R.id.sp_Yangdi06).trim();
                            if (tmpString10.length() == 0) {
                                Common.ShowDialog("请选择或输入样地编号.");
                                return;
                            }
                            tmpDataStrings[1] = tmpString10;
                            final String tmpTotalNameString6 = String.valueOf(tmpTotalNameString5) + "_" + tmpString10;
                            tmpDataStrings[0] = tmpTotalNameString6;
                            String tmpString11 = Common.GetViewValue(YangDi_Dialog.this._Dialog, R.id.et_X).trim();
                            if (tmpString11.length() == 0) {
                                Common.ShowDialog("请输入样地所在的X坐标信息.");
                                return;
                            }
                            tmpDataStrings[9] = tmpString11;
                            String tmpString12 = Common.GetViewValue(YangDi_Dialog.this._Dialog, R.id.et_Y).trim();
                            if (tmpString12.length() == 0) {
                                Common.ShowDialog("请输入样地所在的Y坐标信息.");
                                return;
                            }
                            tmpDataStrings[10] = tmpString12;
                            SQLiteReader tmpLiteReader = YangDi_Dialog.this.m_SQLiteDBHelper.Query("Select LayerID,SYSID From T_CheckPartsInfo Where PartName='" + tmpTotalNameString5 + "'");
                            if (tmpLiteReader.Read()) {
                                String tmpLyrID = tmpLiteReader.GetString(0);
                                String tmpSYSID = tmpLiteReader.GetString(1);
                                tmpDataStrings[7] = tmpLyrID;
                                tmpDataStrings[8] = tmpSYSID;
                                boolean tmpBool02 = false;
                                SQLiteReader tmpLiteReader2 = YangDi_Dialog.this.m_SQLiteDBHelper.Query("Select COUNT(*) From T_YangDiData Where YangDiName='" + tmpTotalNameString6 + "'");
                                if (tmpLiteReader2.Read() && tmpLiteReader2.GetInt32(0) > 0) {
                                    tmpBool02 = true;
                                }
                                if (tmpBool02) {
                                    final String tmpMsg = "样地位置:" + tmpTotalNameString6.replace("_", " ") + "\r\n样地编号:" + tmpDataStrings[1];
                                    Common.ShowYesNoDialogWithAlert(YangDi_Dialog.this._Dialog.getContext(), "该样地数据已经存在.\r\n是否编辑该样地数据?", true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDi_Dialog.1.1
                                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                        public void OnClick(String command2, Object pObject) {
                                            if (command2.equals("YES")) {
                                                YangDi_Dialog.this.openYangDi2Dialog(tmpTotalNameString6, tmpMsg);
                                            }
                                        }
                                    });
                                    return;
                                }
//                                if (YangDi_Dialog.this.m_SQLiteDBHelper.ExecuteSQL(String.format("Replace Into T_YangDiInfo (YangDiName,YangDiIndex,Sheng,Xian,Xiang,Cun,PartIndex,LayerID,SYSID,X,Y) Values ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s','%8$s','%9$s',%10$s,%11$s)", tmpDataStrings))) {
//                                    YangDi_Dialog.this.openYangDi2Dialog(tmpTotalNameString6, String.valueOf("样地位置:" + tmpTotalNameString6.replace("_", " ")) + "\r\n样地编号:" + tmpDataStrings[1]);
//                                    return;
//                                }
                                Common.ShowDialog("保存样地信息失败.");
                                return;
                            }
                            Common.ShowDialog("选择的样地对应图斑在督查数据库中不存在,请在“设置”中重新建立督查数据库.");
                        } catch (Exception e) {
                            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
                        }
                    }
                } catch (Exception e2) {
                    Common.ShowDialog("错误:\r\n" + e2.getLocalizedMessage());
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.yangdi_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("样地调查");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_SpinnerList01 = (SpinnerList) this._Dialog.findViewById(R.id.sp_Yangdi01);
        this.m_SpinnerList02 = (SpinnerList) this._Dialog.findViewById(R.id.sp_Yangdi02);
        this.m_SpinnerList02.setEnabled(false);
        this.m_SpinnerList03 = (SpinnerList) this._Dialog.findViewById(R.id.sp_Yangdi03);
        this.m_SpinnerList03.setEnabled(false);
        this.m_SpinnerList04 = (SpinnerList) this._Dialog.findViewById(R.id.sp_Yangdi04);
        this.m_SpinnerList04.setEnabled(false);
        this.m_SpinnerList05 = (SpinnerList) this._Dialog.findViewById(R.id.sp_Yangdi05);
        this.m_SpinnerList05.setEnabled(false);
        this.m_PartIndexSpinnerList = (InputSpinner) this._Dialog.findViewById(R.id.sp_Yangdi06);
        this.m_PartIndexSpinnerList.getEditTextView().setInputType(2);
        List<String> tmpList01 = new ArrayList<>();
        for (int i = 1; i < 16; i++) {
            tmpList01.add(String.valueOf(i));
        }
        this.m_PartIndexSpinnerList.SetSelectItemList(tmpList01);
        this._Dialog.findViewById(R.id.btn_AutoGetInfo).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_FromGPS).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_FromMeasure).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_FromMapCenter).setOnClickListener(new ViewClick());
        refreshList();
    }

    private void refreshList() {
        boolean tmpBool01 = false;
        try {
            String tmpPath = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/SenLinDuCha.dbx";
            if (new File(tmpPath).exists()) {
                this.m_SQLiteDBHelper = new SQLiteDBHelper(tmpPath);
                List<String> tmpList01 = new ArrayList<>();
                SQLiteReader tmpSQLiteReader = this.m_SQLiteDBHelper.Query("Select Distinct Sheng From T_CheckPartsInfo");
                while (tmpSQLiteReader.Read()) {
                    tmpList01.add(tmpSQLiteReader.GetString(0));
                }
                if (tmpList01.size() > 0) {
                    tmpBool01 = true;
                    Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_Yangdi01, "", "请选择省区", tmpList01, "选择省返回", this.pCallback);
                }
            }
            if (!tmpBool01) {
                Common.ShowDialog("督查图斑数据库还未建立!请先在“森林督查”工具栏中选择“设置”,然后添加督查图斑图层,最后建立督查图斑数据库.");
                this._Dialog.dismiss();
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("自动识别图斑信息")) {
                List<String> tmpList = CommonSetting.getCheckLayersList();
                if (tmpList.size() > 0) {
                    List<String> tmpResultList = null;
                    GeoLayer tmpQueryGeoLayer = null;
                    Envelope tmpExtend = PubVar._Map.getExtend();
                    Iterator<String> tmpIterator = tmpList.iterator();
                    while (true) {
                        if (!tmpIterator.hasNext()) {
                            break;
                        }
                        GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByID(tmpIterator.next());
                        if (tmpGeoLayer != null) {
                            Selection tmpSelection = new Selection();
                            tmpSelection.setDataset(tmpGeoLayer.getDataset());
                            if (tmpGeoLayer.getDataset().QueryWithExtend(tmpExtend, tmpSelection) && tmpSelection.getCount() > 0) {
                                tmpResultList = tmpSelection.getSYSIDList();
                                tmpQueryGeoLayer = tmpGeoLayer;
                                break;
                            }
                        }
                    }
                    if (tmpResultList == null || tmpResultList.size() <= 0) {
                        Common.ShowDialog("在督查图层中未查询到与当前地图中心位置相匹配的图斑数据.\r\n请移动地图或采用GPS定位到图斑内.");
                    } else if (tmpResultList.size() > 1) {
                        final List<HashMap<String, Object>> tmpList02 = new ArrayList<>();
                        List<String> tmpMsgList = new ArrayList<>();
                        for (String tmpSYSID : tmpResultList) {
                            HashMap<String, Object> tmpHashMap = tmpQueryGeoLayer.getDataset().getGeometryFieldValuesBySYSID(tmpSYSID, null);
                            if (tmpHashMap != null && tmpHashMap.size() > 0) {
                                HashMap<String, String> tmpHash2 = CommonSetting.m_DuChaLayerMustFieldsName;
                                String tmpMsgString = "";
                                String tmpFID = tmpHash2.get("省");
                                if (tmpHashMap.containsKey(tmpFID)) {
                                    tmpMsgString = String.valueOf(tmpMsgString) + "省:" + String.valueOf(tmpHashMap.get(tmpFID));
                                }
                                String tmpFID2 = tmpHash2.get("县");
                                if (tmpHashMap.containsKey(tmpFID2)) {
                                    tmpMsgString = String.valueOf(tmpMsgString) + " 县:" + String.valueOf(tmpHashMap.get(tmpFID2));
                                }
                                String tmpFID3 = tmpHash2.get("乡镇");
                                if (tmpHashMap.containsKey(tmpFID3)) {
                                    tmpMsgString = String.valueOf(tmpMsgString) + " 乡镇:" + String.valueOf(tmpHashMap.get(tmpFID3));
                                }
                                String tmpFID4 = tmpHash2.get("村");
                                if (tmpHashMap.containsKey(tmpFID4)) {
                                    tmpMsgString = String.valueOf(tmpMsgString) + " 村:" + String.valueOf(tmpHashMap.get(tmpFID4));
                                }
                                String tmpFID5 = tmpHash2.get("图斑号");
                                if (tmpHashMap.containsKey(tmpFID5)) {
                                    tmpMsgString = String.valueOf(tmpMsgString) + " 图斑号:" + String.valueOf(tmpHashMap.get(tmpFID5));
                                }
                                tmpMsgList.add(tmpMsgString);
                                tmpList02.add(tmpHashMap);
                            }
                        }
                        new AlertDialog.Builder(this._Dialog.getContext(), 3).setTitle("请选择图斑:").setSingleChoiceItems((String[]) tmpMsgList.toArray(new String[tmpMsgList.size()]), -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDi_Dialog.2
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface arg0, int arg1) {
                                HashMap<String, Object> tmpHashMap2 = (HashMap) tmpList02.get(arg1);
                                if (tmpHashMap2 != null) {
                                    if (tmpHashMap2 != null && tmpHashMap2.size() > 0) {
                                        HashMap<String, String> tmpHash22 = CommonSetting.m_DuChaLayerMustFieldsName;
                                        String tmpFID6 = tmpHash22.get("省");
                                        if (tmpHashMap2.containsKey(tmpFID6)) {
                                            Common.SetValueToView(String.valueOf(tmpHashMap2.get(tmpFID6)), YangDi_Dialog.this.m_SpinnerList01);
                                        }
                                        String tmpFID7 = tmpHash22.get("县");
                                        if (tmpHashMap2.containsKey(tmpFID7)) {
                                            Common.SetValueToView(String.valueOf(tmpHashMap2.get(tmpFID7)), YangDi_Dialog.this.m_SpinnerList02);
                                        }
                                        String tmpFID8 = tmpHash22.get("乡镇");
                                        if (tmpHashMap2.containsKey(tmpFID8)) {
                                            Common.SetValueToView(String.valueOf(tmpHashMap2.get(tmpFID8)), YangDi_Dialog.this.m_SpinnerList03);
                                        }
                                        String tmpFID9 = tmpHash22.get("村");
                                        if (tmpHashMap2.containsKey(tmpFID9)) {
                                            Common.SetValueToView(String.valueOf(tmpHashMap2.get(tmpFID9)), YangDi_Dialog.this.m_SpinnerList04);
                                        }
                                        String tmpFID10 = tmpHash22.get("图斑号");
                                        if (tmpHashMap2.containsKey(tmpFID10)) {
                                            Common.SetValueToView(String.valueOf(tmpHashMap2.get(tmpFID10)), YangDi_Dialog.this.m_SpinnerList05);
                                        }
                                    }
                                    Common.ShowToast("自动识别图斑信息完成!");
                                }
                                arg0.dismiss();
                            }
                        }).show();
                    } else {
                        HashMap<String, Object> tmpHashMap2 = tmpQueryGeoLayer.getDataset().getGeometryFieldValuesBySYSID(tmpResultList.get(0), null);
                        if (tmpHashMap2 != null && tmpHashMap2.size() > 0) {
                            HashMap<String, String> tmpHash22 = CommonSetting.m_DuChaLayerMustFieldsName;
                            String tmpFID6 = tmpHash22.get("省");
                            if (tmpHashMap2.containsKey(tmpFID6)) {
                                Common.SetValueToView(String.valueOf(tmpHashMap2.get(tmpFID6)), this.m_SpinnerList01);
                            }
                            String tmpFID7 = tmpHash22.get("县");
                            if (tmpHashMap2.containsKey(tmpFID7)) {
                                Common.SetValueToView(String.valueOf(tmpHashMap2.get(tmpFID7)), this.m_SpinnerList02);
                            }
                            String tmpFID8 = tmpHash22.get("乡镇");
                            if (tmpHashMap2.containsKey(tmpFID8)) {
                                Common.SetValueToView(String.valueOf(tmpHashMap2.get(tmpFID8)), this.m_SpinnerList03);
                            }
                            String tmpFID9 = tmpHash22.get("村");
                            if (tmpHashMap2.containsKey(tmpFID9)) {
                                Common.SetValueToView(String.valueOf(tmpHashMap2.get(tmpFID9)), this.m_SpinnerList04);
                            }
                            String tmpFID10 = tmpHash22.get("图斑号");
                            if (tmpHashMap2.containsKey(tmpFID10)) {
                                Common.SetValueToView(String.valueOf(tmpHashMap2.get(tmpFID10)), this.m_SpinnerList05);
                            }
                            Common.ShowToast("自动识别图斑信息完成!");
                        }
                    }
                } else {
                    Common.ShowDialog("未添加督查图层!请先在“森林督查”工具栏中选择“设置”,然后添加督查图斑图层.");
                }
            } else if (command.equals("GPS采样")) {
                AddGPSPoint_Dialog tempDialog = new AddGPSPoint_Dialog();
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
            } else if (command.equals("地图中心")) {
                Coordinate tempCoord = PubVar._Map.getCenter();
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_X, String.valueOf(tempCoord.getX()));
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_Y, String.valueOf(tempCoord.getY()));
            } else if (command.equals("测量点")) {
                Coordinate tempCoord2 = PubVar._PubCommand.m_Measure.GetLastCoordinate();
                if (tempCoord2 == null) {
                    Common.ShowDialog("请先利用【测量】工具测量目标坐标点.");
                    return;
                }
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_X, String.valueOf(tempCoord2.getX()));
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_Y, String.valueOf(tempCoord2.getY()));
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void openYangDi2Dialog(String yangdiName, String yangdiMsg) {
        YangDi2_Dialog tmpDialog = new YangDi2_Dialog();
        tmpDialog.setYangDiName(yangdiName);
        tmpDialog.setYangDiInfo(yangdiMsg);
        tmpDialog.SetCallback(this.m_Callback);
        tmpDialog.ShowDialog();
        this._Dialog.dismiss();
    }

    @SuppressLint("WrongConstant")
    public void SetTubanName(String tubanName) {
        if (tubanName != null && tubanName.length() > 0) {
            String[] tmpStrs = tubanName.split("_");
            if (tmpStrs.length > 4) {
                Common.SetValueToView(tmpStrs[0].trim(), this.m_SpinnerList01);
                Common.SetValueToView(tmpStrs[1].trim(), this.m_SpinnerList02);
                Common.SetValueToView(tmpStrs[2].trim(), this.m_SpinnerList03);
                Common.SetValueToView(tmpStrs[3].trim(), this.m_SpinnerList04);
                Common.SetValueToView(tmpStrs[4].trim(), this.m_SpinnerList05);
                this._Dialog.findViewById(R.id.ll_AutoGetInfo).setVisibility(8);
            }
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDi_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
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
                YangDi_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
