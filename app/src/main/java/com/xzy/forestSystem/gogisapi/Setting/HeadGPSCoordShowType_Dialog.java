package  com.xzy.forestSystem.gogisapi.Setting;

import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.List;

public class HeadGPSCoordShowType_Dialog {
    private XDialogTemplate _Dialog;
    private boolean _hideButtonVisible;
    private boolean isInitial;
    private ICallback m_Callback;
    private SpinnerList m_CoordType;
    private SpinnerList m_ShowFormatSystem;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public HeadGPSCoordShowType_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.isInitial = true;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Setting.HeadGPSCoordShowType_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                String tempStr;
                try {
                    if (command.equals("确定")) {
                        if (Common.GetSpinnerValueOnID(HeadGPSCoordShowType_Dialog.this._Dialog, R.id.sp_showCoordSystemType).equals("GPS经纬度坐标")) {
                            PubVar.GPS_Show_CoorSystem_Type = 0;
                        } else {
                            PubVar.GPS_Show_CoorSystem_Type = 1;
                        }
                        int tempIndex = HeadGPSCoordShowType_Dialog.this.m_ShowFormatSystem.getSelectedIndex();
                        if (tempIndex >= 0 && tempIndex <= 2) {
                            PubVar.GPS_Show_Format_Type = tempIndex;
                        }
                        PubVar.GPS_Show_Elevation = Common.GetCheckBoxValueOnID(HeadGPSCoordShowType_Dialog.this._Dialog, R.id.ck_showElevation);
                        PubVar.GPS_Show_CoordMultiLine = Common.GetCheckBoxValueOnID(HeadGPSCoordShowType_Dialog.this._Dialog, R.id.ck_CoordMultiLine);
                        if (HeadGPSCoordShowType_Dialog.this.m_Callback != null) {
                            HeadGPSCoordShowType_Dialog.this.m_Callback.OnClick("顶部坐标栏显示设置", null);
                        }
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_GPS_Show_CoorSystem_Type", Integer.valueOf(PubVar.GPS_Show_CoorSystem_Type));
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_GPS_Show_Format_Type", Integer.valueOf(PubVar.GPS_Show_Format_Type));
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_GPS_Show_Elevation", Boolean.valueOf(PubVar.GPS_Show_Elevation));
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_GPS_Show_CoordMultiLine", Boolean.valueOf(PubVar.GPS_Show_CoordMultiLine));
                        HeadGPSCoordShowType_Dialog.this._Dialog.dismiss();
                    } else if (command.equals("坐标类型选择")) {
                        if (HeadGPSCoordShowType_Dialog.this.isInitial && (tempStr = String.valueOf(object)) != null) {
                            if (tempStr.equals("GPS经纬度坐标")) {
                                List<String> tempList = Common.StrArrayToList(new String[]{"DD.DDDDDD°", "DD°MM.MMMMMM′", "DD°MM′SS.SSSS″"});
                                Common.SetSpinnerListData(HeadGPSCoordShowType_Dialog.this._Dialog, "坐标格式选择", tempList, (int) R.id.sp_showCoordFormatType, HeadGPSCoordShowType_Dialog.this.pCallback);
                                Common.SetValueToView(tempList.get(PubVar.GPS_Show_Format_Type), HeadGPSCoordShowType_Dialog.this._Dialog.findViewById(R.id.sp_showCoordFormatType));
                                return;
                            }
                            Common.SetSpinnerListData(HeadGPSCoordShowType_Dialog.this._Dialog, "坐标格式选择", Common.StrArrayToList(new String[]{"X=0.000 Y=0.000"}), (int) R.id.sp_showCoordFormatType, HeadGPSCoordShowType_Dialog.this.pCallback);
                        }
                    } else if (command.equals("隐藏")) {
                        PubVar.HeadTip_Visible = false;
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_HeadTip_Visible", Boolean.valueOf(PubVar.HeadTip_Visible));
                        PubVar.m_Callback.OnClick("更新顶部工具栏显示", null);
                        HeadGPSCoordShowType_Dialog.this._Dialog.dismiss();
                    } else if (command.equals("坐标格式选择")) {
                        HeadGPSCoordShowType_Dialog.this.refreshShowFormatSample();
                        HeadGPSCoordShowType_Dialog.this.isInitial = true;
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_ShowFormatSystem = null;
        this.m_CoordType = null;
        this._hideButtonVisible = true;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.setting_gps_coord_show_type_setting_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("坐标栏显示设置");
        this._Dialog.setCanceledOnTouchOutside(true);
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.btn_coord_hide).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Setting.HeadGPSCoordShowType_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                HeadGPSCoordShowType_Dialog.this.pCallback.OnClick("隐藏", null);
            }
        });
        this.m_CoordType = (SpinnerList) this._Dialog.findViewById(R.id.sp_showCoordSystemType);
        this.m_ShowFormatSystem = (SpinnerList) this._Dialog.findViewById(R.id.sp_showCoordFormatType);
    }

    public void setNeedShowHideButton(boolean visible) {
        this._hideButtonVisible = visible;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshParas() {
        List<String> tempCoorTypeList = Common.StrArrayToList(new String[]{"GPS经纬度坐标", "当前投影坐标"});
        Common.SetSpinnerListData(this._Dialog, "坐标类型选择", tempCoorTypeList, (int) R.id.sp_showCoordSystemType, this.pCallback);
        Common.SetValueToView(tempCoorTypeList.get(PubVar.GPS_Show_CoorSystem_Type), this._Dialog.findViewById(R.id.sp_showCoordSystemType));
        if (PubVar.GPS_Show_CoorSystem_Type == 0) {
            List<String> tempList = Common.StrArrayToList(new String[]{"DD.DDDDDD°", "DD°MM.MMMMMM′", "DD°MM′SS.SSSS″"});
            Common.SetSpinnerListData(this._Dialog, "坐标格式选择", tempList, (int) R.id.sp_showCoordFormatType, this.pCallback);
            Common.SetValueToView(tempList.get(PubVar.GPS_Show_Format_Type), this._Dialog.findViewById(R.id.sp_showCoordFormatType));
        } else {
            Common.SetSpinnerListData(this._Dialog, "坐标格式选择", Common.StrArrayToList(new String[]{"X=0.000 Y=0.000"}), (int) R.id.sp_showCoordFormatType, this.pCallback);
            Common.SetValueToView("X=0.000 Y=0.000", this._Dialog.findViewById(R.id.sp_showCoordFormatType));
        }
        this.m_CoordType.SetSelectReturnTag("坐标类型选择");
        this.m_ShowFormatSystem.SetSelectReturnTag("坐标格式选择");
        CheckBox ckshowElevation = (CheckBox) this._Dialog.findViewById(R.id.ck_showElevation);
        ckshowElevation.setChecked(PubVar.GPS_Show_Elevation);
        ckshowElevation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.Setting.HeadGPSCoordShowType_Dialog.3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                HeadGPSCoordShowType_Dialog.this.refreshShowFormatSample();
            }
        });
        ((CheckBox) this._Dialog.findViewById(R.id.ck_CoordMultiLine)).setChecked(PubVar.GPS_Show_CoordMultiLine);
        refreshShowFormatSample();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshShowFormatSample() {
        StringBuilder tempSB = new StringBuilder();
        if (Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_showCoordSystemType).equals("GPS经纬度坐标")) {
            String tempFormat = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_showCoordFormatType);
            if (tempFormat.equals("DD.DDDDDD°")) {
                tempSB.append("E:000.000000° N:00.000000°");
            } else if (tempFormat.equals("DD°MM.MMMMMM′")) {
                tempSB.append("E:000°00.000000′ N:00°00.000000′");
            } else if (tempFormat.equals("DD°MM′SS.SSSS″")) {
                tempSB.append("E:000°00′00.0000″ N:00°00′00.0000″");
            }
        } else {
            tempSB.append("X=0.000 Y=0.000");
        }
        if (Common.GetCheckBoxValueOnID(this._Dialog, R.id.ck_showElevation)) {
            tempSB.append(" H=0.000");
        }
        Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_showGPSCoordType, tempSB.toString());
    }

    public static String GetGPSShowFormatSample() {
        StringBuilder tempSB = new StringBuilder();
        if (PubVar.GPS_Show_CoorSystem_Type != 0) {
            tempSB.append("X=0.000m Y=0.000m");
        } else if (PubVar.GPS_Show_CoorSystem_Type == 0) {
            tempSB.append("E:000.000000° N:00.000000°");
        } else if (PubVar.GPS_Show_CoorSystem_Type == 0) {
            tempSB.append("E:000°00.000000′ N:00°00.000000′");
        } else if (PubVar.GPS_Show_CoorSystem_Type == 0) {
            tempSB.append("E:000°00′00.0000″ N:00°00′00.0000″");
        }
        if (PubVar.GPS_Show_Elevation) {
            tempSB.append(" H=0.000");
        }
        return tempSB.toString();
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Setting.HeadGPSCoordShowType_Dialog.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                if (HeadGPSCoordShowType_Dialog.this._hideButtonVisible) {
                    HeadGPSCoordShowType_Dialog.this._Dialog.findViewById(R.id.rl_bottom).setVisibility(0);
                } else {
                    HeadGPSCoordShowType_Dialog.this._Dialog.findViewById(R.id.rl_bottom).setVisibility(8);
                }
                HeadGPSCoordShowType_Dialog.this.refreshParas();
            }
        });
        this._Dialog.show();
    }
}
