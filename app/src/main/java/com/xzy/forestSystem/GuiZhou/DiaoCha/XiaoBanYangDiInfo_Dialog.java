package com.xzy.forestSystem.GuiZhou.DiaoCha;

import android.content.DialogInterface;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.widget.EditText;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.GPS.AddGPSPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class XiaoBanYangDiInfo_Dialog {
    private static String RecordUserName = "";
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private String m_DefaultYangdiName;
    private InputSpinner m_PartIndexSpinnerList;
    private SQLiteDBHelper m_SQLiteDBHelper;
    private GeoLayer m_XiaoBanGeoLayer;
    private String m_XiaoBanSYSID;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public XiaoBanYangDiInfo_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_SQLiteDBHelper = null;
        this.m_PartIndexSpinnerList = null;
        this.m_XiaoBanGeoLayer = null;
        this.m_XiaoBanSYSID = "";
        this.m_DefaultYangdiName = "";
        this.pCallback = new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiInfo_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("添加GPS采样点返回")) {
                        Coordinate tempCoord = (Coordinate) object;
                        if (tempCoord != null) {
                            Common.SetEditTextValueOnID(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_X, String.valueOf((int) tempCoord.getX()));
                            Common.SetEditTextValueOnID(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_Y, String.valueOf((int) tempCoord.getY()));
                            Common.ShowToast("设置GPS坐标数据成功!");
                        }
                    } else if (command.equals("确定")) {
                        try {
                            String[] tmpDataStrings = new String[21];
                            String tmpString = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_BasicInput01).trim();
                            if (tmpString.length() == 0) {
                                Common.ShowDialog("请选择县局.");
                                return;
                            }
                            tmpDataStrings[2] = tmpString;
                            String tmpTotalNameString = String.valueOf("") + "_" + tmpString;
                            String tmpString2 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_BasicInput02).trim();
                            if (tmpString2.length() == 0) {
                                Common.ShowDialog("请选择乡镇.");
                                return;
                            }
                            tmpDataStrings[3] = tmpString2;
                            String tmpTotalNameString2 = String.valueOf(tmpTotalNameString) + "_" + tmpString2;
                            String tmpString3 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_BasicInput03).trim();
                            if (tmpString3.length() == 0) {
                                Common.ShowDialog("请选择村.");
                                return;
                            }
                            tmpDataStrings[4] = tmpString3;
                            String tmpTotalNameString3 = String.valueOf(tmpTotalNameString2) + "_" + tmpString3;
                            String tmpString4 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_BasicInput04).trim();
                            if (tmpString4.length() == 0) {
                                Common.ShowDialog("请选择小班.");
                                return;
                            }
                            tmpDataStrings[5] = tmpString4;
                            String tmpTotalNameString4 = String.valueOf(tmpTotalNameString3) + "_" + tmpString4;
                            String tmpString5 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.sp_YangdiIndex).trim();
                            if (tmpString5.length() == 0) {
                                Common.ShowDialog("请选择或输入样地编号.");
                                return;
                            }
                            tmpDataStrings[1] = tmpString5;
                            final String tmpTotalNameString5 = String.valueOf(tmpTotalNameString4) + "_" + tmpString5;
                            tmpDataStrings[0] = tmpTotalNameString5;
                            String tmpString6 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_X).trim();
                            if (tmpString6.length() == 0) {
                                Common.ShowDialog("请输入样地所在的X坐标信息.");
                                return;
                            }
                            tmpDataStrings[8] = tmpString6;
                            String tmpString7 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_Y).trim();
                            if (tmpString7.length() == 0) {
                                Common.ShowDialog("请输入样地所在的Y坐标信息.");
                                return;
                            }
                            tmpDataStrings[9] = tmpString7;
                            String tmpString8 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_YDArea).trim();
                            if (tmpString8.length() == 0) {
                                Common.ShowDialog("请输入样地的面积.");
                                return;
                            }
                            double tmpArea = 0.0d;
                            try {
                                tmpArea = Double.parseDouble(tmpString8);
                            } catch (Exception e) {
                            }
                            if (tmpArea <= 0.0d) {
                                Common.ShowDialog("样地的面积不能小于或等于0.");
                                return;
                            }
                            tmpDataStrings[10] = String.valueOf(tmpArea);
                            int tmpTid = 0;
                            int[] tmpViewIDs = {R.id.et_Input01, R.id.et_Input02, R.id.et_Input03, R.id.et_Input04, R.id.et_Input05, R.id.et_Input06, R.id.et_Input07, R.id.et_Input08, R.id.et_Remark};
                            int length = tmpViewIDs.length;
                            for (int i = 0; i < length; i++) {
                                View tmpView = XiaoBanYangDiInfo_Dialog.this._Dialog.findViewById(tmpViewIDs[i]);
                                tmpTid++;
                                if (tmpView != null) {
                                    tmpDataStrings[tmpTid + 10] = Common.GetViewValue(tmpView).trim();
                                }
                            }
                            XiaoBanYangDiInfo_Dialog.RecordUserName = Common.GetEditTextValueOnID(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_Input08);
                            tmpDataStrings[20] = CommonSetting.DiaoChaDateFormat.format(new Date());
                            String tmpLyrID = XiaoBanYangDiInfo_Dialog.this.m_XiaoBanGeoLayer.getLayerID();
                            String tmpSYSID = XiaoBanYangDiInfo_Dialog.this.m_XiaoBanSYSID;
                            tmpDataStrings[6] = tmpLyrID;
                            tmpDataStrings[7] = tmpSYSID;
                            boolean tmpBool02 = false;
                            SQLiteReader tmpLiteReader = XiaoBanYangDiInfo_Dialog.this.m_SQLiteDBHelper.Query("Select COUNT(*) From T_YangDiInfo Where YangDiName='" + tmpTotalNameString5 + "'");
                            if (tmpLiteReader.Read() && tmpLiteReader.GetInt32(0) > 0) {
                                tmpBool02 = true;
                            }
                            if (tmpBool02) {
                                Common.ShowYesNoDialogWithAlert(XiaoBanYangDiInfo_Dialog.this._Dialog.getContext(), "该样地数据已经存在.\r\n是否编辑该样地数据?", true, new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiInfo_Dialog.1.1
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String command2, Object pObject) {
                                        if (command2.equals("YES")) {
                                            XiaoBanYangDiInfo_Dialog.this.openYangDiTableDialog(tmpTotalNameString5);
                                            XiaoBanYangDiInfo_Dialog.this._Dialog.dismiss();
                                        }
                                    }
                                });
                                return;
                            }
//                            if (XiaoBanYangDiInfo_Dialog.this.m_SQLiteDBHelper.ExecuteSQL(String.format("Replace Into T_YangDiInfo (YangDiName,YangDiIndex,Xian,Xiang,Cun,XiaoBan,LayerID,SYSID,X,Y,YDArea,YouShiShuZhong,ZhuZhongZuCheng,QiYuan,QiaoMuYBD,GuanMuFGD,PJNL,LingZu,DiaoChaRen,Remark,DiaoChaTime) Values ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s','%8$s',%9$s,%10$s,%11$s,'%12$s','%13$s','%14$s','%15$s','%16$s','%17$s','%18$s','%19$s','%20$s','%21$s')", tmpDataStrings))) {
//                                XiaoBanYangDiInfo_Dialog.this.openYangDiTableDialog(tmpTotalNameString5);
//                                XiaoBanYangDiInfo_Dialog.this._Dialog.dismiss();
//                                return;
//                            }
                            Common.ShowDialog("保存样地信息失败.");
                        } catch (Exception e2) {
                            Common.ShowDialog("错误:\r\n" + e2.getLocalizedMessage());
                        }
                    } else if (command.equals("保存")) {
                        try {
                            String[] tmpDataStrings2 = new String[20];
                            tmpDataStrings2[0] = XiaoBanYangDiInfo_Dialog.this.m_DefaultYangdiName;
                            String tmpString9 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_X).trim();
                            if (tmpString9.length() == 0) {
                                Common.ShowDialog("请输入样地所在的X坐标信息.");
                                return;
                            }
                            tmpDataStrings2[8] = tmpString9;
                            String tmpString10 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_Y).trim();
                            if (tmpString10.length() == 0) {
                                Common.ShowDialog("请输入样地所在的Y坐标信息.");
                                return;
                            }
                            tmpDataStrings2[9] = tmpString10;
                            String tmpString11 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_YDArea).trim();
                            if (tmpString11.length() == 0) {
                                Common.ShowDialog("请输入样地的面积.");
                                return;
                            }
                            double tmpArea2 = 0.0d;
                            try {
                                tmpArea2 = Double.parseDouble(tmpString11);
                            } catch (Exception e3) {
                            }
                            if (tmpArea2 <= 0.0d) {
                                Common.ShowDialog("样地的面积不能小于或等于0.");
                                return;
                            }
                            tmpDataStrings2[10] = String.valueOf(tmpArea2);
                            int tmpTid2 = 0;
                            int[] tmpViewIDs2 = {R.id.et_Input01, R.id.et_Input02, R.id.et_Input03, R.id.et_Input04, R.id.et_Input05, R.id.et_Input06, R.id.et_Input07, R.id.et_Input08, R.id.et_Remark};
                            int length2 = tmpViewIDs2.length;
                            for (int i2 = 0; i2 < length2; i2++) {
                                View tmpView2 = XiaoBanYangDiInfo_Dialog.this._Dialog.findViewById(tmpViewIDs2[i2]);
                                tmpTid2++;
                                if (tmpView2 != null) {
                                    tmpDataStrings2[tmpTid2 + 10] = Common.GetViewValue(tmpView2).trim();
                                }
                            }
                            XiaoBanYangDiInfo_Dialog.RecordUserName = Common.GetEditTextValueOnID(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_Input08);
//                            if (XiaoBanYangDiInfo_Dialog.this.m_SQLiteDBHelper.ExecuteSQL(String.format("Update T_YangDiInfo Set X=%9$s,Y=%10$s,YDArea=%11$s,YouShiShuZhong='%12$s',ZhuZhongZuCheng='%13$s',QiYuan='%14$s',QiaoMuYBD='%15$s',GuanMuFGD='%16$s',PJNL='%17$s',LingZu='%18$s',DiaoChaRen='%19$s',Remark='%20$s' Where YangDiName='%1$s'", tmpDataStrings2))) {
//                                C0321Common.ShowDialog("保存样地信息成功!");
//                            } else {
//                                C0321Common.ShowDialog("保存样地信息失败.");
//                            }
                        } catch (Exception e4) {
                            Common.ShowDialog("错误:\r\n" + e4.getLocalizedMessage());
                        }
                    }
                } catch (Exception e5) {
                    Common.ShowDialog("错误:\r\n" + e5.getLocalizedMessage());
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.xiaobanyangdiinfo_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("样地调查");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.btn_YangdiTable).setVisibility(8);
        this._Dialog.findViewById(R.id.btn_CalXiaoBanYinZi).setVisibility(8);
        this.m_PartIndexSpinnerList = (InputSpinner) this._Dialog.findViewById(R.id.sp_YangdiIndex);
        this.m_PartIndexSpinnerList.setEnabled(true);
        this.m_PartIndexSpinnerList.getEditTextView().setInputType(2);
        List<String> tmpList01 = new ArrayList<>();
        for (int i = 1; i < 31; i++) {
            tmpList01.add(String.valueOf(i));
        }
        this.m_PartIndexSpinnerList.SetSelectItemList(tmpList01);
        this._Dialog.findViewById(R.id.btn_FromGPS).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_FromMeasure).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_FromMapCenter).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_AutoGetInfo).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_SanShengMu).setOnClickListener(new ViewClick());
        this.m_SQLiteDBHelper = CommonSetting.GetSQLiteDBHelper();
        if (RecordUserName != null && RecordUserName.trim().length() > 0) {
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_Input08, RecordUserName);
        }
        this.m_PartIndexSpinnerList.getEditTextView().setText("1");
    }

    public XiaoBanYangDiInfo_Dialog(String yangdiName) {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_SQLiteDBHelper = null;
        this.m_PartIndexSpinnerList = null;
        this.m_XiaoBanGeoLayer = null;
        this.m_XiaoBanSYSID = "";
        this.m_DefaultYangdiName = "";
        this.pCallback = new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiInfo_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("添加GPS采样点返回")) {
                        Coordinate tempCoord = (Coordinate) object;
                        if (tempCoord != null) {
                            Common.SetEditTextValueOnID(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_X, String.valueOf((int) tempCoord.getX()));
                            Common.SetEditTextValueOnID(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_Y, String.valueOf((int) tempCoord.getY()));
                            Common.ShowToast("设置GPS坐标数据成功!");
                        }
                    } else if (command.equals("确定")) {
                        try {
                            String[] tmpDataStrings = new String[21];
                            String tmpString = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_BasicInput01).trim();
                            if (tmpString.length() == 0) {
                                Common.ShowDialog("请选择县局.");
                                return;
                            }
                            tmpDataStrings[2] = tmpString;
                            String tmpTotalNameString = String.valueOf("") + "_" + tmpString;
                            String tmpString2 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_BasicInput02).trim();
                            if (tmpString2.length() == 0) {
                                Common.ShowDialog("请选择乡镇.");
                                return;
                            }
                            tmpDataStrings[3] = tmpString2;
                            String tmpTotalNameString2 = String.valueOf(tmpTotalNameString) + "_" + tmpString2;
                            String tmpString3 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_BasicInput03).trim();
                            if (tmpString3.length() == 0) {
                                Common.ShowDialog("请选择村.");
                                return;
                            }
                            tmpDataStrings[4] = tmpString3;
                            String tmpTotalNameString3 = String.valueOf(tmpTotalNameString2) + "_" + tmpString3;
                            String tmpString4 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_BasicInput04).trim();
                            if (tmpString4.length() == 0) {
                                Common.ShowDialog("请选择小班.");
                                return;
                            }
                            tmpDataStrings[5] = tmpString4;
                            String tmpTotalNameString4 = String.valueOf(tmpTotalNameString3) + "_" + tmpString4;
                            String tmpString5 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.sp_YangdiIndex).trim();
                            if (tmpString5.length() == 0) {
                                Common.ShowDialog("请选择或输入样地编号.");
                                return;
                            }
                            tmpDataStrings[1] = tmpString5;
                            final String tmpTotalNameString5 = String.valueOf(tmpTotalNameString4) + "_" + tmpString5;
                            tmpDataStrings[0] = tmpTotalNameString5;
                            String tmpString6 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_X).trim();
                            if (tmpString6.length() == 0) {
                                Common.ShowDialog("请输入样地所在的X坐标信息.");
                                return;
                            }
                            tmpDataStrings[8] = tmpString6;
                            String tmpString7 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_Y).trim();
                            if (tmpString7.length() == 0) {
                                Common.ShowDialog("请输入样地所在的Y坐标信息.");
                                return;
                            }
                            tmpDataStrings[9] = tmpString7;
                            String tmpString8 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_YDArea).trim();
                            if (tmpString8.length() == 0) {
                                Common.ShowDialog("请输入样地的面积.");
                                return;
                            }
                            double tmpArea = 0.0d;
                            try {
                                tmpArea = Double.parseDouble(tmpString8);
                            } catch (Exception e) {
                            }
                            if (tmpArea <= 0.0d) {
                                Common.ShowDialog("样地的面积不能小于或等于0.");
                                return;
                            }
                            tmpDataStrings[10] = String.valueOf(tmpArea);
                            int tmpTid = 0;
                            int[] tmpViewIDs = {R.id.et_Input01, R.id.et_Input02, R.id.et_Input03, R.id.et_Input04, R.id.et_Input05, R.id.et_Input06, R.id.et_Input07, R.id.et_Input08, R.id.et_Remark};
                            int length = tmpViewIDs.length;
                            for (int i = 0; i < length; i++) {
                                View tmpView = XiaoBanYangDiInfo_Dialog.this._Dialog.findViewById(tmpViewIDs[i]);
                                tmpTid++;
                                if (tmpView != null) {
                                    tmpDataStrings[tmpTid + 10] = Common.GetViewValue(tmpView).trim();
                                }
                            }
                            XiaoBanYangDiInfo_Dialog.RecordUserName = Common.GetEditTextValueOnID(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_Input08);
                            tmpDataStrings[20] = CommonSetting.DiaoChaDateFormat.format(new Date());
                            String tmpLyrID = XiaoBanYangDiInfo_Dialog.this.m_XiaoBanGeoLayer.getLayerID();
                            String tmpSYSID = XiaoBanYangDiInfo_Dialog.this.m_XiaoBanSYSID;
                            tmpDataStrings[6] = tmpLyrID;
                            tmpDataStrings[7] = tmpSYSID;
                            boolean tmpBool02 = false;
                            SQLiteReader tmpLiteReader = XiaoBanYangDiInfo_Dialog.this.m_SQLiteDBHelper.Query("Select COUNT(*) From T_YangDiInfo Where YangDiName='" + tmpTotalNameString5 + "'");
                            if (tmpLiteReader.Read() && tmpLiteReader.GetInt32(0) > 0) {
                                tmpBool02 = true;
                            }
                            if (tmpBool02) {
                                Common.ShowYesNoDialogWithAlert(XiaoBanYangDiInfo_Dialog.this._Dialog.getContext(), "该样地数据已经存在.\r\n是否编辑该样地数据?", true, new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiInfo_Dialog.1.1
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String command2, Object pObject) {
                                        if (command2.equals("YES")) {
                                            XiaoBanYangDiInfo_Dialog.this.openYangDiTableDialog(tmpTotalNameString5);
                                            XiaoBanYangDiInfo_Dialog.this._Dialog.dismiss();
                                        }
                                    }
                                });
                                return;
                            }
//                            if (XiaoBanYangDiInfo_Dialog.this.m_SQLiteDBHelper.ExecuteSQL(String.format("Replace Into T_YangDiInfo (YangDiName,YangDiIndex,Xian,Xiang,Cun,XiaoBan,LayerID,SYSID,X,Y,YDArea,YouShiShuZhong,ZhuZhongZuCheng,QiYuan,QiaoMuYBD,GuanMuFGD,PJNL,LingZu,DiaoChaRen,Remark,DiaoChaTime) Values ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s','%8$s',%9$s,%10$s,%11$s,'%12$s','%13$s','%14$s','%15$s','%16$s','%17$s','%18$s','%19$s','%20$s','%21$s')", tmpDataStrings))) {
//                                XiaoBanYangDiInfo_Dialog.this.openYangDiTableDialog(tmpTotalNameString5);
//                                XiaoBanYangDiInfo_Dialog.this._Dialog.dismiss();
//                                return;
//                            }
                            Common.ShowDialog("保存样地信息失败.");
                        } catch (Exception e2) {
                            Common.ShowDialog("错误:\r\n" + e2.getLocalizedMessage());
                        }
                    } else if (command.equals("保存")) {
                        try {
                            String[] tmpDataStrings2 = new String[20];
                            tmpDataStrings2[0] = XiaoBanYangDiInfo_Dialog.this.m_DefaultYangdiName;
                            String tmpString9 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_X).trim();
                            if (tmpString9.length() == 0) {
                                Common.ShowDialog("请输入样地所在的X坐标信息.");
                                return;
                            }
                            tmpDataStrings2[8] = tmpString9;
                            String tmpString10 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_Y).trim();
                            if (tmpString10.length() == 0) {
                                Common.ShowDialog("请输入样地所在的Y坐标信息.");
                                return;
                            }
                            tmpDataStrings2[9] = tmpString10;
                            String tmpString11 = Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_YDArea).trim();
                            if (tmpString11.length() == 0) {
                                Common.ShowDialog("请输入样地的面积.");
                                return;
                            }
                            double tmpArea2 = 0.0d;
                            try {
                                tmpArea2 = Double.parseDouble(tmpString11);
                            } catch (Exception e3) {
                            }
                            if (tmpArea2 <= 0.0d) {
                                Common.ShowDialog("样地的面积不能小于或等于0.");
                                return;
                            }
                            tmpDataStrings2[10] = String.valueOf(tmpArea2);
                            int tmpTid2 = 0;
                            int[] tmpViewIDs2 = {R.id.et_Input01, R.id.et_Input02, R.id.et_Input03, R.id.et_Input04, R.id.et_Input05, R.id.et_Input06, R.id.et_Input07, R.id.et_Input08, R.id.et_Remark};
                            int length2 = tmpViewIDs2.length;
                            for (int i2 = 0; i2 < length2; i2++) {
                                View tmpView2 = XiaoBanYangDiInfo_Dialog.this._Dialog.findViewById(tmpViewIDs2[i2]);
                                tmpTid2++;
                                if (tmpView2 != null) {
                                    tmpDataStrings2[tmpTid2 + 10] = Common.GetViewValue(tmpView2).trim();
                                }
                            }
                            XiaoBanYangDiInfo_Dialog.RecordUserName = Common.GetEditTextValueOnID(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_Input08);
//                            if (XiaoBanYangDiInfo_Dialog.this.m_SQLiteDBHelper.ExecuteSQL(String.format("Update T_YangDiInfo Set X=%9$s,Y=%10$s,YDArea=%11$s,YouShiShuZhong='%12$s',ZhuZhongZuCheng='%13$s',QiYuan='%14$s',QiaoMuYBD='%15$s',GuanMuFGD='%16$s',PJNL='%17$s',LingZu='%18$s',DiaoChaRen='%19$s',Remark='%20$s' Where YangDiName='%1$s'", tmpDataStrings2))) {
//                                C0321Common.ShowDialog("保存样地信息成功!");
//                            } else {
//                                C0321Common.ShowDialog("保存样地信息失败.");
//                            }
                        } catch (Exception e4) {
                            Common.ShowDialog("错误:\r\n" + e4.getLocalizedMessage());
                        }
                    }
                } catch (Exception e5) {
                    Common.ShowDialog("错误:\r\n" + e5.getLocalizedMessage());
                }
            }
        };
        this.m_DefaultYangdiName = yangdiName;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.xiaobanyangdiinfo_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("样地调查信息");
        this._Dialog.SetHeadButtons("1,2130837858,保存,保存", this.pCallback);
        this.m_PartIndexSpinnerList = (InputSpinner) this._Dialog.findViewById(R.id.sp_YangdiIndex);
        this.m_PartIndexSpinnerList.setEnabled(false);
        this.m_PartIndexSpinnerList.getEditTextView().setEnabled(false);
        this._Dialog.findViewById(R.id.btn_FromGPS).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_FromMeasure).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_FromMapCenter).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_YangdiTable).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_AutoGetInfo).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_CalXiaoBanYinZi).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_SanShengMu).setOnClickListener(new ViewClick());
        this.m_SQLiteDBHelper = CommonSetting.GetSQLiteDBHelper();
        this._Dialog.findViewById(R.id.et_BasicInput01).setEnabled(false);
        this._Dialog.findViewById(R.id.et_BasicInput02).setEnabled(false);
        this._Dialog.findViewById(R.id.et_BasicInput03).setEnabled(false);
        this._Dialog.findViewById(R.id.et_BasicInput04).setEnabled(false);
        this._Dialog.findViewById(R.id.sp_YangdiIndex).setEnabled(false);
        SQLiteReader tmpLiteReader = this.m_SQLiteDBHelper.Query("Select YangDiIndex,Xian,Xiang,Cun,XiaoBan,X,Y,YDArea,YouShiShuZhong,ZhuZhongZuCheng,QiYuan,QiaoMuYBD,GuanMuFGD,PJNL,LingZu,DiaoChaRen,Remark From T_YangDiInfo Where YangDiName='" + this.m_DefaultYangdiName + "'");
        if (tmpLiteReader.Read()) {
            this.m_PartIndexSpinnerList.setText(tmpLiteReader.GetString(0));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_BasicInput01, tmpLiteReader.GetString(1));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_BasicInput02, tmpLiteReader.GetString(2));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_BasicInput03, tmpLiteReader.GetString(3));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_BasicInput04, tmpLiteReader.GetString(4));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_X, String.valueOf(tmpLiteReader.GetDouble(5)));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_Y, String.valueOf(tmpLiteReader.GetDouble(6)));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_YDArea, String.valueOf(tmpLiteReader.GetDouble(7)));
            int tmpTid = 7;
            for (int tmpViewID : new int[]{R.id.et_Input01, R.id.et_Input02, R.id.et_Input03, R.id.et_Input04, R.id.et_Input05, R.id.et_Input06, R.id.et_Input07, R.id.et_Input08, R.id.et_Remark}) {
                tmpTid++;
                View tmpView = this._Dialog.findViewById(tmpViewID);
                if (tmpView != null) {
                    Common.SetValueToView(tmpLiteReader.GetString(tmpTid), tmpView);
                }
            }
        }
    }

    public void setXiaoBan(GeoLayer geoLayer, String xiaoBanSYSID) {
        this.m_XiaoBanGeoLayer = geoLayer;
        this.m_XiaoBanSYSID = xiaoBanSYSID;
        refreshFieldValuesFromXiaoBan();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("自动获取信息")) {
                if (refreshFieldValuesFromXiaoBan()) {
                    Common.ShowToast("自动从小班图层获取相关属性数据成功!");
                }
            } else if (command.equals("GPS采样")) {
                AddGPSPoint_Dialog tempDialog = new AddGPSPoint_Dialog();
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
            } else if (command.equals("地图中心")) {
                Coordinate tempCoord = PubVar._Map.getCenter();
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_X, String.valueOf((int) tempCoord.getX()));
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_Y, String.valueOf((int) tempCoord.getY()));
            } else if (command.equals("测量点")) {
                Coordinate tempCoord2 = PubVar._PubCommand.m_Measure.GetLastCoordinate();
                if (tempCoord2 == null) {
                    Common.ShowDialog("请先利用【测量】工具测量目标坐标点.");
                    return;
                }
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_X, String.valueOf((int) tempCoord2.getX()));
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_Y, String.valueOf((int) tempCoord2.getY()));
            } else if (command.equals("标准地调查表")) {
                openYangDiTableDialog(this.m_DefaultYangdiName);
            } else if (command.equals("计算小班因子")) {
                CommonSetting.CalXiaoBanYinZiAndUpdateData(this._Dialog.getContext(), this.m_DefaultYangdiName, false);
                Common.ShowYesNoDialog(this._Dialog.getContext(), "小班因子计算已经完成,是否更新本样地与小班相关的属性值?\r\n", new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiInfo_Dialog.2
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command2, Object pObject) {
                        if (command2.equals("YES")) {
                            XiaoBanYangDiInfo_Dialog.this.refreshFieldValuesFromXiaoBan();
                        }
                    }
                });
            } else if (command.equals("散生木调查")) {
                String tmpSanSMNameString = getXiaoBanFullName();
                if (tmpSanSMNameString.trim().length() > 0) {
                    SanShengMu_Dialog tmpDialog = new SanShengMu_Dialog(tmpSanSMNameString);
                    tmpDialog.SetCallback(this.m_Callback);
                    tmpDialog.ShowDialog();
                }
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
    }

    private String getXiaoBanFullName() {
        String tmpString = Common.GetViewValue(this._Dialog, R.id.et_BasicInput01).trim();
        if (tmpString.length() == 0) {
            Common.ShowDialog("请选择县局.");
            return "";
        }
        String result = String.valueOf("") + "_" + tmpString;
        String tmpString2 = Common.GetViewValue(this._Dialog, R.id.et_BasicInput02).trim();
        if (tmpString2.length() == 0) {
            Common.ShowDialog("请选择乡镇.");
            return "";
        }
        String result2 = String.valueOf(result) + "_" + tmpString2;
        String tmpString3 = Common.GetViewValue(this._Dialog, R.id.et_BasicInput03).trim();
        if (tmpString3.length() == 0) {
            Common.ShowDialog("请选择村.");
            return "";
        }
        String result3 = String.valueOf(result2) + "_" + tmpString3;
        String tmpString4 = Common.GetViewValue(this._Dialog, R.id.et_BasicInput04).trim();
        if (tmpString4.length() != 0) {
            return String.valueOf(result3) + "_" + tmpString4;
        }
        Common.ShowDialog("请选择小班.");
        return "";
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void openYangDiTableDialog(String yangdiName) {
        XiaoBanYangDiTable_Dialog tmpDialog = new XiaoBanYangDiTable_Dialog(yangdiName);
        tmpDialog.SetCallback(this.m_Callback);
        tmpDialog.ShowDialog();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean refreshFieldValuesFromXiaoBan() {
        Object tmpObject;
        String[] tmpStrs;
        boolean result = false;
        try {
            if (this.m_XiaoBanGeoLayer == null && this.m_DefaultYangdiName.length() > 0 && (tmpStrs = CommonSetting.GetXiaoBanLayerAndIDByYangdi(this.m_DefaultYangdiName)) != null && tmpStrs.length > 1) {
                this.m_XiaoBanGeoLayer = PubVar._Map.GetGeoLayerByID(tmpStrs[0]);
                this.m_XiaoBanSYSID = tmpStrs[1];
            }
            if (this.m_XiaoBanGeoLayer != null && this.m_XiaoBanSYSID.length() > 0) {
                int[] tmpViewIDs = {R.id.et_BasicInput01, R.id.et_BasicInput02, R.id.et_BasicInput03, R.id.et_BasicInput04, R.id.et_Input01, R.id.et_Input02, R.id.et_Input03, R.id.et_Input04, R.id.et_Input05, R.id.et_Input06, R.id.et_Input07};
                HashMap<String, Object> tmpHashMap = this.m_XiaoBanGeoLayer.getDataset().getGeometryFieldValuesBySYSID(this.m_XiaoBanSYSID, null);
                for (int tmpViewID : tmpViewIDs) {
                    View tmpView = this._Dialog.findViewById(tmpViewID);
                    if (!(tmpView == null || tmpView.getTag() == null)) {
                        boolean tmpBool = true;
                        String tmpFIDName2 = CommonSetting.m_XiaoBanLayerMustFieldsName.get(String.valueOf(tmpView.getTag()));
                        if (tmpHashMap.containsKey(tmpFIDName2) && (tmpObject = tmpHashMap.get(tmpFIDName2)) != null && !String.valueOf(tmpObject).equals("")) {
                            Common.SetValueToView(String.valueOf(tmpObject), tmpView);
                            if (tmpView instanceof EditText) {
                                ((EditText) tmpView).setTextColor(-16776961);
                                tmpBool = false;
                                result = true;
                            }
                        }
                        if (tmpBool && (tmpView instanceof EditText)) {
                            ((EditText) tmpView).setTextColor(ViewCompat.MEASURED_STATE_MASK);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return result;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void autoSetYangDiIndex() {
        String tmpString01 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_BasicInput01);
        String tmpString02 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_BasicInput02);
        String tmpString03 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_BasicInput03);
        SQLiteReader tmpSqLiteReader = this.m_SQLiteDBHelper.Query("Select MAX(YangDiIndex) From T_YangDiInfo Where Xian='" + tmpString01 + "' And Xiang='" + tmpString02 + "' And Cun='" + tmpString03 + "' And XiaoBan='" + Common.GetEditTextValueOnID(this._Dialog, R.id.et_BasicInput04) + "'");
        if (tmpSqLiteReader == null || !tmpSqLiteReader.Read()) {
            this.m_PartIndexSpinnerList.getEditTextView().setText("1");
            return;
        }
        this.m_PartIndexSpinnerList.getEditTextView().setText(String.valueOf(tmpSqLiteReader.GetInt32(0) + 1));
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiInfo_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                try {
                    if (XiaoBanYangDiInfo_Dialog.this.m_DefaultYangdiName != null && XiaoBanYangDiInfo_Dialog.this.m_DefaultYangdiName.length() == 0) {
                        try {
                            String tmpTotalNameString = String.valueOf("") + "_" + Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_BasicInput01).trim();
                            String tmpTotalNameString2 = String.valueOf(tmpTotalNameString) + "_" + Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_BasicInput02).trim();
                            String tmpTotalNameString3 = String.valueOf(tmpTotalNameString2) + "_" + Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_BasicInput03).trim();
                            String tmpTotalNameString4 = String.valueOf(tmpTotalNameString3) + "_" + Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.et_BasicInput04).trim();
                            final String tmpTotalNameString5 = String.valueOf(tmpTotalNameString4) + "_" + Common.GetViewValue(XiaoBanYangDiInfo_Dialog.this._Dialog, R.id.sp_YangdiIndex).trim();
                            boolean tmpBool02 = false;
                            SQLiteReader tmpLiteReader = XiaoBanYangDiInfo_Dialog.this.m_SQLiteDBHelper.Query("Select COUNT(*) From T_YangDiInfo Where YangDiName='" + tmpTotalNameString5 + "'");
                            if (tmpLiteReader.Read() && tmpLiteReader.GetInt32(0) > 0) {
                                tmpBool02 = true;
                            }
                            if (tmpBool02) {
                                Common.ShowYesNoDialogWithAlert(XiaoBanYangDiInfo_Dialog.this._Dialog.getContext(), "该小班已经存在样地数据.\r\n是否编辑该样地数据?\r\n\r\n提示:“确定”将编辑该样地数据,“取消”将继续添加样地数据.", true, new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiInfo_Dialog.3.1
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String command, Object pObject) {
                                        if (command.equals("YES")) {
                                            XiaoBanYangDiInfo_Dialog.this.openYangDiTableDialog(tmpTotalNameString5);
                                            XiaoBanYangDiInfo_Dialog.this._Dialog.dismiss();
                                        }
                                    }
                                });
                            }
                        } catch (Exception e) {
                        }
                        XiaoBanYangDiInfo_Dialog.this.autoSetYangDiIndex();
                    }
                } catch (Exception e2) {
                }
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
                XiaoBanYangDiInfo_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
