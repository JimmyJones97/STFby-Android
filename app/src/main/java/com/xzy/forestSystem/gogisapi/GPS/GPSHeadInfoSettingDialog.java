package  com.xzy.forestSystem.gogisapi.GPS;

import android.content.Context;
import android.widget.CheckBox;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.HashMap;

public class GPSHeadInfoSettingDialog {
    private XDialogTemplate _Dialog = null;
    private CheckBox checkBox01;
    private CheckBox checkBox02;
    private CheckBox checkBox03;
    private CheckBox checkBox04;
    private CheckBox checkBox05;
    private CheckBox checkBox06;
    private CheckBox checkBox07;
    private ICallback m_Callback = null;
    private Context m_Context = null;
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.GPS.GPSHeadInfoSettingDialog.1
        /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x00a2: APUT  (r1v0 'tempBools' boolean[] A[D('tempBools' boolean[]), IMMUTABLE_TYPE]), (0 ??[int, short, byte, char]), false */
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
            if (paramString.equals("确定")) {
                boolean[] tempBools = new boolean[6];
                if (GPSHeadInfoSettingDialog.this.checkBox01.isChecked()) {
                    tempBools[0] = true;
                } else {
                    tempBools[0] = false;
                }
                if (GPSHeadInfoSettingDialog.this.checkBox02.isChecked()) {
                    tempBools[1] = true;
                } else {
                    tempBools[1] = false;
                }
                if (GPSHeadInfoSettingDialog.this.checkBox03.isChecked()) {
                    tempBools[2] = true;
                } else {
                    tempBools[2] = false;
                }
                if (GPSHeadInfoSettingDialog.this.checkBox04.isChecked()) {
                    tempBools[3] = true;
                } else {
                    tempBools[3] = false;
                }
                if (GPSHeadInfoSettingDialog.this.checkBox05.isChecked()) {
                    tempBools[4] = true;
                } else {
                    tempBools[4] = false;
                }
                if (GPSHeadInfoSettingDialog.this.checkBox06.isChecked()) {
                    tempBools[5] = true;
                } else {
                    tempBools[5] = false;
                }
                String tempValue = "";
                for (int i = 0; i < tempBools.length; i++) {
                    tempValue = String.valueOf(tempValue) + tempBools[i] + ",";
                }
                HashMap<String, String> tempData = new HashMap<>();
                tempData.put("gpsstatusinfobool", tempValue);
                if (GPSHeadInfoSettingDialog.this.checkBox07.isChecked()) {
                    tempData.put("gpsinfoshowsingleline", "true");
                } else {
                    tempData.put("gpsinfoshowsingleline", "false");
                }
                GPSLocationClass.SaveGPSSettingInfo(tempData, false);
                if (PubVar._PubCommand.m_GpsLocation != null) {
                    PubVar._PubCommand.m_GpsLocation.updateGPSConfig();
                }
            }
            GPSHeadInfoSettingDialog.this._Dialog.dismiss();
        }
    };

    public GPSHeadInfoSettingDialog(Context context) {
        boolean[] tempBools;
        this.m_Context = context;
        this._Dialog = new XDialogTemplate(this.m_Context);
        this._Dialog.SetLayoutView(R.layout.gps_headinfosetting);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("GPS状态信息设置");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.checkBox01 = (CheckBox) this._Dialog.findViewById(R.id.checkBox1);
        this.checkBox02 = (CheckBox) this._Dialog.findViewById(R.id.checkBox2);
        this.checkBox03 = (CheckBox) this._Dialog.findViewById(R.id.checkBox3);
        this.checkBox04 = (CheckBox) this._Dialog.findViewById(R.id.checkBox4);
        this.checkBox05 = (CheckBox) this._Dialog.findViewById(R.id.checkBox5);
        this.checkBox06 = (CheckBox) this._Dialog.findViewById(R.id.checkBox6);
        this.checkBox07 = (CheckBox) this._Dialog.findViewById(R.id.checkBox7);
        HashMap<String, String> tempHash = GPSLocationClass.GetGPSSettingInfo();
        if (tempHash != null) {
            if (tempHash.containsKey("gpsinfoshowsingleline") && tempHash.get("gpsinfoshowsingleline").equals("true")) {
                this.checkBox07.setChecked(true);
            }
            if (tempHash.containsKey("gpsstatusinfobool") && (tempBools = GPSLocationClass.getGPSTipSetting(tempHash.get("gpsstatusinfobool"))) != null && tempBools.length == 6) {
                this.checkBox01.setChecked(tempBools[0]);
                this.checkBox02.setChecked(tempBools[1]);
                this.checkBox03.setChecked(tempBools[2]);
                this.checkBox04.setChecked(tempBools[3]);
                this.checkBox05.setChecked(tempBools[4]);
                this.checkBox06.setChecked(tempBools[5]);
            }
        }
    }

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public void ShowDialog() {
        this._Dialog.show();
    }
}
