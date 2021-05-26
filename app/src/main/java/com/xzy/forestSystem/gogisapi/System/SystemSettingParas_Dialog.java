package  com.xzy.forestSystem.gogisapi.System;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XRasterFileLayer;
import  com.xzy.forestSystem.gogisapi.Carto.SelectTool;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Geometry.Size;
import  com.xzy.forestSystem.gogisapi.MyControls.ColorPickerDialog;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.Input_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Setting.FormSizeSetting_Dialog;
import  com.xzy.forestSystem.gogisapi.Setting.GPSGatherSetting_Dialog;
import  com.xzy.forestSystem.gogisapi.Setting.HeadGPSCoordShowType_Dialog;
import  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar;
import  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_ZoomScale;
import  com.xzy.forestSystem.gogisapi.Track.Track_Setting_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.HashMap;

public class SystemSettingParas_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public SystemSettingParas_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.System.SystemSettingParas_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("保存")) {
                        boolean tmpNeedRefresh = false;
                        try {
                            PubVar.GPS_Show_Accuracy = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_showGPSScope);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_GPS_Show_Accuracy", Boolean.valueOf(PubVar.GPS_Show_Accuracy));
                            String tempStr = Common.GetTextValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_gpsAtnHeight);
                            if (tempStr.contains("m")) {
                                tempStr = tempStr.replaceAll("m", "");
                            }
                            PubVar.GPS_Antenna_Height = Double.parseDouble(tempStr);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_GPS_Antenna_Height", Double.valueOf(PubVar.GPS_Antenna_Height));
                            if (Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_showGPSDirection)) {
                                PubVar.GPS_Point_ShowType = 1;
                            } else {
                                PubVar.GPS_Point_ShowType = 0;
                            }
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_GPS_Point_ShowType", Integer.valueOf(PubVar.GPS_Point_ShowType));
                            PubVar.Track_Draw_Date = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_trackDrawDate);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_Track_Draw_Date", Boolean.valueOf(PubVar.Track_Draw_Date));
                            PubVar.Track_Auto_Record = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_trackAutoRecord);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_Track_Auto_Record", Boolean.valueOf(PubVar.Track_Auto_Record));
                            PubVar.Toolbar_Gesture_Close = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_ToolbarGesClose);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_Toolbar_Gesture_Close", Boolean.valueOf(PubVar.Toolbar_Gesture_Close));
                            PubVar.ScreenIsLand = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_ScreenLand);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_SCREEN_ORIENTATION", Boolean.valueOf(PubVar.ScreenIsLand));
                            PubVar.ScreenIsFull = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_ScreenFull);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_SCREEN_FULL", Boolean.valueOf(PubVar.ScreenIsFull));
                            SharedPreferences.Editor tmpEditor = SystemSettingParas_Dialog.this._Dialog.getContext().getSharedPreferences("forestsystem", 0).edit();
                            tmpEditor.putBoolean("ScreenIsFull", PubVar.ScreenIsFull);
                            tmpEditor.commit();
                            boolean tmpBool01 = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_Toolbar_AutoResize);
                            if (PubVar.Toolbar_AutoResize != tmpBool01) {
                                PubVar.Toolbar_AutoResize = tmpBool01;
                                if (PubVar.Toolbar_AutoResize) {
                                    for (BaseToolbar tempToolbar : PubVar._PubCommand.getToolbarList()) {
                                        if (tempToolbar.IsVisiable() && !(tempToolbar instanceof Toolbar_ZoomScale)) {
                                            tempToolbar.UpdateToolbarPosition();
                                        }
                                    }
                                }
                                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_Toolbar_AutoResize", Boolean.valueOf(PubVar.Toolbar_AutoResize));
                            }
                            PubVar.HeadTip_Visible = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_showHeadTipInfo);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_HeadTip_Visible", Boolean.valueOf(PubVar.HeadTip_Visible));
                            PubVar.m_Callback.OnClick("更新顶部工具栏显示", null);
                            PubVar.Compass_Show = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_showCompass);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_Compass_Show", Boolean.valueOf(PubVar.Compass_Show));
                            PubVar.m_Callback.OnClick("更新指北针显示", null);
                            PubVar.ScaleBar_Visible = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_showScalebar);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_ScaleBar_Visible", Boolean.valueOf(PubVar.ScaleBar_Visible));
                            PubVar.m_Callback.OnClick("更新比例尺显示", null);
                            PubVar.MessageDialogAllowShow = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_MessageDialoAllowShow);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_MessageDialogAllowShow", Boolean.valueOf(PubVar.MessageDialogAllowShow));
                            boolean tmpBool012 = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_ToolbarShowMainMenu);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_MainToolbar_Visible", Boolean.valueOf(tmpBool012));
                            PubVar.m_Callback.OnClick("更新主菜单显示", Boolean.valueOf(tmpBool012));
                            PubVar.DataSetQuerySafety = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_ToolbarDatasetSafety);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_DataSet_Safety", Boolean.valueOf(PubVar.DataSetQuerySafety));
                            PubVar.GPS_Gather_PointAuto = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_GPS_Gather_PointAuto);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_GPS_Gather_PointAuto", Boolean.valueOf(PubVar.GPS_Gather_PointAuto));
                            if (Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_RasterInHigh)) {
                                XBaseTilesLayer.InHighLevel = 1;
                                XRasterFileLayer.InHighLevel = 1;
                            } else {
                                XBaseTilesLayer.InHighLevel = 0;
                                XRasterFileLayer.InHighLevel = 0;
                            }
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_RasterInHigh", Integer.valueOf(XBaseTilesLayer.InHighLevel));
                            String tmpAreaUnit = Common.GetSpinnerValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.sp_areaUnitType);
                            if (tmpAreaUnit.contains("亩")) {
                                PubVar.AreaUnitType = 2;
                            } else if (tmpAreaUnit.contains("顷")) {
                                PubVar.AreaUnitType = 3;
                            } else {
                                PubVar.AreaUnitType = 1;
                            }
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_AreaUnitType", Integer.valueOf(PubVar.AreaUnitType));
                            boolean tmpIsComShowInMap = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_showCompassInMap);
                            if (PubVar.m_MapCompassVisible != tmpIsComShowInMap) {
                                PubVar.m_MapCompassVisible = tmpIsComShowInMap;
                                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_Compass_ShowOnMap", Boolean.valueOf(PubVar.m_MapCompassVisible));
                                PubVar._MapView.invalidate();
                            }
                            boolean tmpIsComShowInMap2 = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_MediaInfo_Input_Bool);
                            if (PubVar.MediaInfo_Input_Bool != tmpIsComShowInMap2) {
                                PubVar.MediaInfo_Input_Bool = tmpIsComShowInMap2;
                                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_MediaInfo_Input_Bool", Boolean.valueOf(PubVar.MediaInfo_Input_Bool));
                            }
                            boolean tmpMap_Display_Cache = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_RasterCache);
                            if (tmpMap_Display_Cache != PubVar.Map_Display_Cache) {
                                PubVar.Map_Display_Cache = tmpMap_Display_Cache;
                                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_Map_DisplayCache_Check", Boolean.valueOf(PubVar.Map_Display_Cache));
                                if (PubVar.Map_Display_Cache) {
                                    int tmpWH = PubVar._MapView.getWidth();
                                    if (tmpWH > PubVar._MapView.getHeight()) {
                                        tmpWH = PubVar._MapView.getHeight();
                                    }
                                    PubVar._Map.setMaskBias((float) tmpWH, (float) tmpWH);
                                    PubVar._Map.setSize(new Size(PubVar._MapView.getWidth(), PubVar._MapView.getHeight()));
                                    PubVar._Map.Refresh();
                                } else {
                                    PubVar._Map.setMaskBias(0.0f, 0.0f);
                                    PubVar._Map.setSize(new Size(PubVar._MapView.getWidth(), PubVar._MapView.getHeight()));
                                    PubVar._Map.Refresh();
                                }
                            }
                            PubVar.Draw_Polygon_ShowCurrent = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_DrawPolygonShow);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_Draw_Polygon_ShowCurrent", Boolean.valueOf(PubVar.Draw_Polygon_ShowCurrent));
                            Object tmpObj = ((InputSpinner) SystemSettingParas_Dialog.this._Dialog.findViewById(R.id.sp_systembgcolor)).getEditTextView().getTag();
                            if (tmpObj != null) {
                                PubVar.MapBgColor = Integer.parseInt(tmpObj.toString());
                                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_Map_BgColor", Integer.valueOf(PubVar.MapBgColor));
                                tmpNeedRefresh = true;
                            }
                            PubVar.Edited_ShowAttribute_Bool = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_editShowProp);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_Edited_ShowAttribute_Bool", Boolean.valueOf(PubVar.Edited_ShowAttribute_Bool));
                            PubVar.AllowEditSnap = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_ToolbarAllowSnap);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_Edited_AllowEditSnap", Boolean.valueOf(PubVar.AllowEditSnap));
                            if (PubVar.AllowEditSnap) {
                                PubVar.SnapDistance = Double.parseDouble(Common.GetEditTextValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.et_snapDistance));
                                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_Edited_SnapDistance", Double.valueOf(PubVar.SnapDistance));
                            }
                            double tmpD02 = Double.parseDouble(Common.GetTextValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_selectClickBias));
                            if (tmpD02 < 1.0d || tmpD02 > 200.0d) {
                                tmpD02 = 10.0d;
                            }
                            SelectTool.SELECT_CLICK_BIAS = ((double) PubVar.ScaledDensity) * tmpD02;
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_Select_Click_Bias", Integer.valueOf((int) tmpD02));
                            if (PubVar._PubCommand.GetPolygonEdit() != null) {
                                PubVar._PubCommand.GetPolygonEdit().RefreshSnap();
                            }
                            if (PubVar._PubCommand.GetLineEdit() != null) {
                                PubVar._PubCommand.GetLineEdit().RefreshSnap();
                            }
                            if (PubVar._PubCommand.m_PointEdit != null) {
                                PubVar._PubCommand.m_PointEdit.RefreshSnap();
                            }
                            try {
                                PubVar.Clip_EPSILON = Double.parseDouble(Common.GetEditTextValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.et_SysSetting_ClipEps));
                            } catch (Exception e) {
                            }
                            if (PubVar.Clip_EPSILON <= 0.0d) {
                                PubVar.Clip_EPSILON = 1.0E-4d;
                            }
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_Edited_Clip_EPSILON", Double.valueOf(PubVar.Clip_EPSILON));
                            PubVar.Module_SenLinDuCha = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_ToolbarSenLinDuCha);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_Module_SenLinDuCha", Boolean.valueOf(PubVar.Module_SenLinDuCha));
                            PubVar.Module_YangdiDiaoCha = Common.GetCheckBoxValueOnID(SystemSettingParas_Dialog.this._Dialog, R.id.ck_ToolbarYangDiDiaoCha);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_Module_YangDiDiaoCha", Boolean.valueOf(PubVar.Module_YangdiDiaoCha));
                            PubVar._PubCommand.m_MapCompLayoutToolbar.refreshModuleButtons();
                            if (tmpNeedRefresh) {
                                PubVar._Map.RefreshFastRasterLayers();
                            }
                        } catch (Exception e2) {
                        }
                        SystemSettingParas_Dialog.this._Dialog.dismiss();
                    } else if (command.equals("GPS数据采集参数设置返回")) {
                        int tempInt = (int) (PubVar.GPSGatherIntervalTime / 1000);
                        Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_gpsGatherItvTime, tempInt <= 0 ? "不限" : String.valueOf(String.valueOf(tempInt)) + "秒");
                        Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_gpsGatherItvDistanceSet, PubVar.GPSGatherIntervalDistance <= 0.0d ? "不限" : String.valueOf(String.valueOf(PubVar.GPSGatherIntervalDistance)) + "m");
                        Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_gpsGatherAccuracySet, PubVar.GPS_Gather_Accuracy <= 0.0d ? "不限" : String.valueOf(String.valueOf(PubVar.GPS_Gather_Accuracy)) + "m");
                    } else if (command.equals("足迹记录参数设置返回")) {
                        if (PubVar.Track_Record_Type == 0) {
                            Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_TrackRecordType, "按时间间隔记录");
                            Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_TrackRecordParam, String.valueOf(String.valueOf(PubVar.Track_Record_Param)) + "秒");
                            return;
                        }
                        Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_TrackRecordType, "按距离间隔记录");
                        Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_TrackRecordParam, String.valueOf(String.valueOf(PubVar.Track_Record_Param)) + "m");
                    } else if (command.equals("顶部坐标栏显示设置")) {
                        if (PubVar._PubCommand.m_GpsLocation != null) {
                            PubVar._PubCommand.m_GpsLocation.updateControlsInfo();
                        }
                        Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_gpsCoordTypeSample2, HeadGPSCoordShowType_Dialog.GetGPSShowFormatSample());
                    } else if (command.equals("输入参数")) {
                        Object[] tmpObjs = (Object[]) object;
                        if (tmpObjs != null && tmpObjs.length > 1) {
                            String tempStr2 = String.valueOf(tmpObjs[0]);
                            if (tempStr2.equals("GPS天线高度设置")) {
                                Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_gpsAtnHeight, String.valueOf(String.valueOf(tmpObjs[1])) + "m");
                            } else if (tempStr2.equals("选择时容差设置")) {
                                Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_selectClickBias, String.valueOf(tmpObjs[1]));
                            }
                        }
                    } else if (command.equals("分屏窗体尺寸设置返回")) {
                        Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_childMapWidth, String.valueOf(PubVar.ChildMapWidth));
                        Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_childMapHeight, String.valueOf(PubVar.ChildMapHeight));
                    } else if (command.equals("窗体设置返回")) {
                        Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_OutMsgWidth, String.valueOf(PubVar.ChildMapWidth));
                        Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_OutMsgHeight, String.valueOf(PubVar.ChildMapHeight));
                    } else if (command.equals("图层列表窗体尺寸设置返回")) {
                        Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_LayersContentFormWidth, String.valueOf(PubVar.LayersContentWidth));
                        Common.SetTextViewValueOnID(SystemSettingParas_Dialog.this._Dialog, (int) R.id.tv_LayersContentFormHeight, String.valueOf(PubVar.LayersContentHeight));
                    }
                } catch (Exception e3) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.systemsetting_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("系统设置");
        this._Dialog.SetHeadButtons("1,2130837858,保存,保存", this.pCallback);
        this._Dialog.HideKeybroad();
        this._Dialog.findViewById(R.id.ll_gpsAtnHeightSetting).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.ll_gpsGatherSetting).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.ll_gpsShowCoordSetting).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.ll_trackSetting).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.ll_childMapSetting).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.ll_OutMsgSetting).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_SysToolbarDefault).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.ll_LayersContentFormSetting).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.ll_selectClickBias).setOnClickListener(new ViewClick());
        InputSpinner tempColorSpinnerDialog = (InputSpinner) this._Dialog.findViewById(R.id.sp_systembgcolor);
        tempColorSpinnerDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.System.SystemSettingParas_Dialog.2
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                ColorPickerDialog tempDialog = new ColorPickerDialog();
                int tmpColor = 0;
                Object tmpObj = ((InputSpinner) SystemSettingParas_Dialog.this._Dialog.findViewById(R.id.sp_systembgcolor)).getEditTextView().getTag();
                if (tmpObj != null) {
                    tmpColor = Integer.parseInt(tmpObj.toString());
                }
                tempDialog.setInitialColor(tmpColor);
                tempDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.System.SystemSettingParas_Dialog.2.1
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString2, Object pObject2) {
                        int tmpColor2 = Color.parseColor(pObject2.toString());
                        ((InputSpinner) SystemSettingParas_Dialog.this._Dialog.findViewById(R.id.sp_systembgcolor)).getEditTextView().setBackgroundColor(tmpColor2);
                        ((InputSpinner) SystemSettingParas_Dialog.this._Dialog.findViewById(R.id.sp_systembgcolor)).getEditTextView().setTag(Integer.valueOf(tmpColor2));
                    }
                });
                tempDialog.ShowDialog();
            }
        });
        tempColorSpinnerDialog.getEditTextView().setBackgroundColor(PubVar.MapBgColor);
        tempColorSpinnerDialog.getEditTextView().setTag(Integer.valueOf(PubVar.MapBgColor));
        tempColorSpinnerDialog.getEditTextView().setEnabled(false);
        ((CheckBox) this._Dialog.findViewById(R.id.ck_ToolbarAllowSnap)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.System.SystemSettingParas_Dialog.3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    SystemSettingParas_Dialog.this._Dialog.findViewById(R.id.ll_ToolbarAllowSnap).setVisibility(0);
                } else {
                    SystemSettingParas_Dialog.this._Dialog.findViewById(R.id.ll_ToolbarAllowSnap).setVisibility(4);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        String tempStr;
        String tempStr2;
        String tempStr3;
        String tempStr4;
        try {
            int tempInt = (int) (PubVar.GPSGatherIntervalTime / 1000);
            if (tempInt <= 0) {
                tempStr = "不限";
            } else {
                tempStr = String.valueOf(String.valueOf(tempInt)) + "秒";
            }
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_gpsGatherItvTime, tempStr);
            if (PubVar.GPSGatherIntervalDistance <= 0.0d) {
                tempStr2 = "不限";
            } else {
                tempStr2 = String.valueOf(String.valueOf(PubVar.GPSGatherIntervalDistance)) + "m";
            }
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_gpsGatherItvDistanceSet, tempStr2);
            if (PubVar.GPS_Gather_Accuracy <= 0.0d) {
                tempStr3 = "不限";
            } else {
                tempStr3 = String.valueOf(String.valueOf(PubVar.GPS_Gather_Accuracy)) + "m";
            }
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_gpsGatherAccuracySet, tempStr3);
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_gpsAtnHeight, String.valueOf(String.valueOf(PubVar.GPS_Antenna_Height)) + "m");
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_gpsCoordTypeSample2, HeadGPSCoordShowType_Dialog.GetGPSShowFormatSample());
            if (PubVar.Track_Record_Type == 0) {
                Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_TrackRecordType, "按时间间隔记录");
                Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_TrackRecordParam, String.valueOf(String.valueOf(PubVar.Track_Record_Param)) + "秒");
            } else {
                Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_TrackRecordType, "按距离间隔记录");
                Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_TrackRecordParam, String.valueOf(String.valueOf(PubVar.Track_Record_Param)) + "m");
            }
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_trackDrawDate, PubVar.Track_Draw_Date);
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_trackAutoRecord, PubVar.Track_Auto_Record);
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_showGPSScope, PubVar.GPS_Show_Accuracy);
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_showHeadTipInfo, PubVar.HeadTip_Visible);
            if (PubVar.GPS_Point_ShowType == 1) {
                Common.SetCheckValueOnID(this._Dialog, R.id.ck_showGPSDirection, true);
            } else {
                Common.SetCheckValueOnID(this._Dialog, R.id.ck_showGPSDirection, false);
            }
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_childMapWidth, String.valueOf(PubVar.ChildMapWidth));
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_childMapHeight, String.valueOf(PubVar.ChildMapHeight));
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_OutMsgWidth, String.valueOf(PubVar.MessagePanelWidth));
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_OutMsgHeight, String.valueOf(PubVar.MessagePanelHeight));
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_LayersContentFormWidth, String.valueOf(PubVar.LayersContentWidth));
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_LayersContentFormHeight, String.valueOf(PubVar.LayersContentHeight));
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_showCompass, PubVar.Compass_Show);
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_ToolbarGesClose, PubVar.Toolbar_Gesture_Close);
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_Toolbar_AutoResize, PubVar.Toolbar_AutoResize);
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_showScalebar, PubVar.ScaleBar_Visible);
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_MessageDialoAllowShow, PubVar.MessageDialogAllowShow);
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_editShowProp, PubVar.Edited_ShowAttribute_Bool);
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_ScreenLand, PubVar.ScreenIsLand);
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_ScreenFull, PubVar.ScreenIsFull);
            boolean tmpBool = false;
            HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_MainToolbar_Visible");
            if (!(tempHashMap == null || (tempStr4 = tempHashMap.get("F2")) == null || tempStr4.equals(""))) {
                tmpBool = Boolean.parseBoolean(tempStr4);
            }
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_ToolbarShowMainMenu, tmpBool);
            String tmpAreaUnit = "平方米(平方公里)";
            if (PubVar.AreaUnitType == 2) {
                tmpAreaUnit = "亩";
            } else if (PubVar.AreaUnitType == 3) {
                tmpAreaUnit = "公顷";
            }
            Common.SetSpinnerListData(this._Dialog, "面积单位类型", Common.StrArrayToList(new String[]{"平方米(平方公里)", "亩", "公顷"}), (int) R.id.sp_areaUnitType);
            Common.SetValueToView(tmpAreaUnit, this._Dialog.findViewById(R.id.sp_areaUnitType));
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_ToolbarAllowSnap, PubVar.AllowEditSnap);
            if (!PubVar.AllowEditSnap) {
                this._Dialog.findViewById(R.id.ll_ToolbarAllowSnap).setVisibility(4);
            }
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_snapDistance, String.valueOf(PubVar.SnapDistance));
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_ToolbarDatasetSafety, PubVar.DataSetQuerySafety);
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_GPS_Gather_PointAuto, PubVar.GPS_Gather_PointAuto);
            if (XBaseTilesLayer.InHighLevel > 0) {
                Common.SetCheckValueOnID(this._Dialog, R.id.ck_RasterInHigh, true);
            } else {
                Common.SetCheckValueOnID(this._Dialog, R.id.ck_RasterInHigh, false);
            }
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_showCompassInMap, PubVar.m_MapCompassVisible);
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_DrawPolygonShow, PubVar.Draw_Polygon_ShowCurrent);
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_selectClickBias, String.format("%d", Integer.valueOf((int) (SelectTool.SELECT_CLICK_BIAS / ((double) PubVar.ScaledDensity)))));
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_MediaInfo_Input_Bool, PubVar.MediaInfo_Input_Bool);
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_RasterCache, PubVar.Map_Display_Cache);
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_SysSetting_ClipEps, String.valueOf(PubVar.Clip_EPSILON));
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_ToolbarSenLinDuCha, PubVar.Module_SenLinDuCha);
            Common.SetCheckValueOnID(this._Dialog, R.id.ck_ToolbarYangDiDiaoCha, PubVar.Module_YangdiDiaoCha);
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("GPS天线高度设置")) {
                Input_Dialog tempDialog = new Input_Dialog();
                tempDialog.setValues("GPS天线高度设置", "高度(m)：", String.valueOf(PubVar.GPS_Antenna_Height), "提示:GPS天线高度指测量平面到天线接收器的垂直距离.");
                tempDialog.SetReturnTag("GPS天线高度设置");
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
            } else if (command.equals("GPS数据采集设置")) {
                GPSGatherSetting_Dialog tempDialog2 = new GPSGatherSetting_Dialog();
                tempDialog2.SetCallback(this.pCallback);
                tempDialog2.ShowDialog();
            } else if (command.equals("GPS坐标栏显示设置")) {
                HeadGPSCoordShowType_Dialog tempDialog3 = new HeadGPSCoordShowType_Dialog();
                tempDialog3.setNeedShowHideButton(false);
                tempDialog3.SetCallback(this.pCallback);
                tempDialog3.ShowDialog();
            } else if (command.equals("足迹记录设置")) {
                Track_Setting_Dialog tempDialog4 = new Track_Setting_Dialog();
                tempDialog4.SetCallback(this.pCallback);
                tempDialog4.ShowDialog();
            } else if (command.equals("重置工具栏位置")) {
                int tmpY = 100;
                int tmpBias = (int) (PubVar.ScaledDensity * 60.0f);
                for (BaseToolbar tempToolbar : PubVar._PubCommand.getToolbarList()) {
                    if (!(tempToolbar instanceof Toolbar_ZoomScale)) {
                        tempToolbar.UpdateToolbarPosition(0, tmpY);
                        tempToolbar.SetOrientation(true);
                        tempToolbar.SaveConfigDB();
                        tmpY += tmpBias;
                    }
                }
                Common.ShowToast("重置所有可见的工具栏位置.");
            } else if (command.equals("分屏窗体设置")) {
                FormSizeSetting_Dialog tmpDialog = new FormSizeSetting_Dialog();
                tmpDialog.Set_FormType(1);
                tmpDialog.SetCallback(this.pCallback);
                tmpDialog.ShowDialog();
            } else if (command.equals("消息输出窗体设置")) {
                FormSizeSetting_Dialog tmpDialog2 = new FormSizeSetting_Dialog();
                tmpDialog2.Set_FormType(0);
                tmpDialog2.SetCallback(this.pCallback);
                tmpDialog2.ShowDialog();
            } else if (command.equals("图层列表窗体设置")) {
                FormSizeSetting_Dialog tmpDialog3 = new FormSizeSetting_Dialog();
                tmpDialog3.Set_FormType(2);
                tmpDialog3.SetCallback(this.pCallback);
                tmpDialog3.ShowDialog();
            } else if (command.equals("选择时容差设置")) {
                Input_Dialog tempDialog5 = new Input_Dialog();
                tempDialog5.setValues("地图选择时容差设置", "像素容差：", Common.GetTextValueOnID(this._Dialog, (int) R.id.tv_selectClickBias), "提示:当设备屏幕分辨率较低时或灵敏度较低时,建议设置该值大于10,小于50.");
                tempDialog5.SetReturnTag("选择时容差设置");
                tempDialog5.SetCallback(this.pCallback);
                tempDialog5.ShowDialog();
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.System.SystemSettingParas_Dialog.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                SystemSettingParas_Dialog.this.refreshLayout();
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
                SystemSettingParas_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
