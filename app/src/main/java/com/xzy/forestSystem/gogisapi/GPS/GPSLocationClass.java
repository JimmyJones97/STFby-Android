package  com.xzy.forestSystem.gogisapi.GPS;

import android.content.Context;
import android.media.SoundPool;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WGS1984;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.Setting.HeadGPSCoordShowType_Dialog;
import com.stczh.gzforestSystem.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class GPSLocationClass {
    public static boolean CoordShowSingleLine = false;
    public static int CoordShowType = 0;
    public static int GPSLinkTimeOut = 30000;
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int FenceAlarmTimer = 0;
    private Polyline FenceBorder = null;
    public float HDOP = 10.0f;
    public boolean IsOpenFenceAlarm = false;
    public float PDOP = 100.0f;
    public float VDOP = 100.0f;
    private AbstractC0383CoordinateSystem _CoordinateSystem = null;
    private boolean _isLocation = false;
    public double accuracy = 2000.0d;
    public float bearing = 0.0f;
    private Context context = null;
    public long currentRecDataLine = 0;
    public double elevation = 0.0d;
    private ImageView gpsSNRImgView = null;
    private ImageView gpsStatusImgView = null;
    private boolean[] gpsStatusInfoBool = new boolean[6];
    private TextView infoTextView = null;
    public boolean isOpen = false;
    public String lastRecData = "";
    private String lastSimpleLocString = "";
    private long lastTimeInterval = 0;
    public double latitude = 0.0d;
    public long loctime = 0;
    public double longtitude = 0.0d;
    private Coordinate m_TranpCoordinate = null;
    private HashMap<Integer, Integer> mySoundPMap = null;
    private SoundPool mySoundPool = null;
    private Runnable myTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.GPS.GPSLocationClass.1
        @Override // java.lang.Runnable
        public void run() {
            try {
                if ((new Date().getTime() - GPSLocationClass.this.loctime) - GPSLocationClass.this.lastTimeInterval >= ((long) GPSLocationClass.GPSLinkTimeOut)) {
                    GPSLocationClass gPSLocationClass = GPSLocationClass.this;
                    GPSLocationClass.this.usedSateCount = 0;
                    gPSLocationClass.satellitesCount = 0;
                    if (!GPSLocationClass.this.status.equals("未定位")) {
                        GPSLocationClass.this.updateControlsInfo();
                    }
                    if (GPSLocationClass.this._isLocation && !GPSLocationClass.this.status.equals("未定位")) {
                        GPSLocationClass.this.mySoundPool.play(((Integer) GPSLocationClass.this.mySoundPMap.get(1)).intValue(), 1.0f, 1.0f, 0, 0, 1.0f);
                    }
                    GPSLocationClass.this.status = "未定位";
                    GPSLocationClass.this._isLocation = false;
                }
            } catch (Exception e) {
            }
            if (GPSLocationClass.this.FenceAlarmTimer == 3) {
                GPSLocationClass.this.mySoundPool.play(((Integer) GPSLocationClass.this.mySoundPMap.get(3)).intValue(), 1.0f, 1.0f, 0, 0, 1.0f);
            }
            if (GPSLocationClass.this.FenceAlarmTimer != 0) {
                GPSLocationClass gPSLocationClass2 = GPSLocationClass.this;
                gPSLocationClass2.FenceAlarmTimer--;
            }
            GPSLocationClass.this.myhandler.postDelayed(GPSLocationClass.this.myTask, 1000);
        }
    };
    private Handler myhandler = new Handler();
    public double rate = 0.0d;
    public int satellitesCount = 0;
    public String status = "未定位";
    private TextView tipTextView = null;
    public int usedSateCount = 0;

    public GPSLocationClass(Context context2) {
        this.context = context2;
        this._CoordinateSystem = new Coordinate_WGS1984();
        this.gpsStatusInfoBool[0] = true;
        boolean[] zArr = this.gpsStatusInfoBool;
        boolean[] zArr2 = this.gpsStatusInfoBool;
        boolean[] zArr3 = this.gpsStatusInfoBool;
        boolean[] zArr4 = this.gpsStatusInfoBool;
        this.gpsStatusInfoBool[5] = false;
        zArr4[4] = false;
        zArr3[3] = false;
        zArr2[2] = false;
        zArr[1] = false;
        updateGPSConfig();
        this.myhandler.post(this.myTask);
        this.mySoundPool = new SoundPool(3, 1, 0);
        this.mySoundPMap = new HashMap<>();
        this.mySoundPMap.put(1, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.gpson, 1)));
        this.mySoundPMap.put(2, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.gpsoff, 1)));
        this.mySoundPMap.put(3, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.outfence, 1)));
    }

    public boolean IsLocation() {
        if (!this.isOpen || this.loctime == 0 || (this.longtitude == 0.0d && this.latitude == 0.0d)) {
            this.satellitesCount = 0;
            this.usedSateCount = 0;
            if (this._isLocation) {
                this.mySoundPool.play(this.mySoundPMap.get(2).intValue(), 1.0f, 1.0f, 0, 0, 1.0f);
            }
            this._isLocation = false;
            return false;
        } else if ((new Date().getTime() - this.loctime) - this.lastTimeInterval > ((long) GPSLinkTimeOut)) {
            this.satellitesCount = 0;
            this.usedSateCount = 0;
            if (this._isLocation) {
                this.mySoundPool.play(this.mySoundPMap.get(2).intValue(), 1.0f, 1.0f, 0, 0, 1.0f);
            }
            this._isLocation = false;
            return false;
        } else {
            if (!this._isLocation) {
                this.mySoundPool.play(this.mySoundPMap.get(1).intValue(), 1.0f, 1.0f, 0, 0, 1.0f);
            }
            this._isLocation = true;
            return true;
        }
    }

    public void updateGPSConfig() {
        try {
            HashMap<String, String> tempHashMap = GetGPSSettingInfo();
            if (tempHashMap.containsKey("CoordShowType")) {
                CoordShowType = Integer.parseInt(tempHashMap.get("CoordShowType"));
            }
            if (tempHashMap.containsKey("gpsinfoshowsingleline")) {
                if (tempHashMap.get("gpsinfoshowsingleline").equals("true")) {
                    CoordShowSingleLine = true;
                } else {
                    CoordShowSingleLine = false;
                }
                if (this.infoTextView != null) {
                    if (CoordShowSingleLine) {
                        this.infoTextView.setEllipsize(TextUtils.TruncateAt.END);
                        this.infoTextView.setSingleLine(true);
                    } else {
                        this.infoTextView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                        this.infoTextView.setSingleLine(false);
                    }
                }
            }
            if (tempHashMap.containsKey("gpsstatusinfobool")) {
                this.gpsStatusInfoBool = getGPSTipSetting(tempHashMap.get("gpsstatusinfobool"));
            }
        } catch (Exception ex) {
            Common.Log("updateGPSConfig", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    public static void SaveGPSDeviceInfo(String gpsType, String blutoothGPSDeice) {
        try {
            HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_GPS");
            if (tempHashMap != null) {
                if (gpsType != null) {
                    tempHashMap.put("F3", gpsType);
                    PubVar.GPS_Device_Type = Integer.parseInt(gpsType);
                }
                if (blutoothGPSDeice != null) {
                    tempHashMap.put("F4", blutoothGPSDeice);
                }
                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_GPS", tempHashMap);
            }
        } catch (Exception ex) {
            Common.Log("SaveGPSDeviceInfo", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    public static HashMap<String, String> GetGPSSettingInfo() {
        String tempValue;
        String[] tempStrs;
        HashMap<String, String> result = new HashMap<>();
        try {
            HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_GPS");
            String tempValue2 = tempHashMap.get("F2");
            if (tempValue2 != null) {
                try {
                    String[] tempStrs2 = tempValue2.split(";");
                    if (tempStrs2 != null) {
                        int tempLen = tempStrs2.length;
                        if (tempLen > 0) {
                            result.put("CoordShowType", tempStrs2[0]);
                        }
                        if (tempLen > 1) {
                            result.put("gpsinfoshowsingleline", tempStrs2[1]);
                        }
                        if (tempLen > 2) {
                            result.put("gpsstatusinfobool", tempStrs2[2]);
                        }
                    }
                } catch (Exception e) {
                }
            }
            String tempValue3 = tempHashMap.get("F3");
            if (tempValue3 != null) {
                result.put("gpstype", tempValue3);
                if (tempValue3.equals("1") && (tempValue = tempHashMap.get("F4")) != null && (tempStrs = tempValue.split(";")) != null && tempStrs.length > 1) {
                    result.put("bluetoothdevicename", tempStrs[0]);
                    result.put("bluetoothdeviceaddress", tempStrs[1]);
                }
            }
        } catch (Exception ex) {
            Common.Log("GetGPSSettingInfo", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
        return result;
    }

    public static void SaveGPSSettingInfo(HashMap<String, String> hashData, boolean saveWithNO) {
        String tempValue;
        String tempValue2;
        String tempValue3;
        String tempValue4;
        try {
            HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_GPS");
            if (tempHashMap == null) {
                tempHashMap = new HashMap<>();
            }
            boolean tempTag = false;
            if (saveWithNO) {
                tempTag = true;
            }
            if (hashData.containsKey("CoordShowType")) {
                tempValue = String.valueOf("") + hashData.get("CoordShowType") + ";";
                tempTag = true;
            } else {
                tempValue = String.valueOf("") + CoordShowType + ";";
            }
            if (hashData.containsKey("gpsinfoshowsingleline")) {
                tempValue2 = String.valueOf(tempValue) + hashData.get("gpsinfoshowsingleline") + ";";
                tempTag = true;
            } else {
                tempValue2 = String.valueOf(tempValue) + CoordShowSingleLine + ";";
            }
            if (hashData.containsKey("gpsstatusinfobool")) {
                tempValue3 = String.valueOf(tempValue2) + hashData.get("gpsstatusinfobool") + ";";
                tempTag = true;
            } else {
                boolean[] gpsStatusInfoBool2 = new boolean[6];
                gpsStatusInfoBool2[0] = true;
                for (int i = 0; i < gpsStatusInfoBool2.length; i++) {
                    tempValue2 = String.valueOf(tempValue2) + gpsStatusInfoBool2[i] + ",";
                }
                tempValue3 = String.valueOf(tempValue2) + ";";
            }
            if (tempTag) {
                tempHashMap.put("F2", tempValue3);
            }
            boolean tempTag2 = false;
            if (saveWithNO) {
                tempTag2 = true;
            }
            if (hashData.containsKey("gpstype")) {
                tempValue4 = String.valueOf("") + hashData.get("gpstype");
                tempTag2 = true;
            } else {
                tempValue4 = String.valueOf("") + "0";
            }
            if (tempTag2) {
                tempHashMap.put("F3", tempValue4);
            }
            boolean tempTag3 = false;
            String tempValue5 = "";
            if (saveWithNO) {
                tempTag3 = true;
            }
            if (hashData.containsKey("bluetoothdevicename")) {
                tempValue5 = String.valueOf(tempValue5) + hashData.get("bluetoothdevicename") + ";";
                tempTag3 = true;
            }
            if (hashData.containsKey("bluetoothdeviceaddress")) {
                tempValue5 = String.valueOf(tempValue5) + hashData.get("bluetoothdeviceaddress") + ";";
                tempTag3 = true;
            }
            if (tempTag3) {
                tempHashMap.put("F4", tempValue5);
            }
            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_GPS", tempHashMap);
        } catch (Exception ex) {
            Common.Log("CreateReport", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    public static boolean[] getGPSTipSetting(String data) {
        boolean[] result = new boolean[6];
        if (data != null) {
            try {
                String[] tempStrs = data.split(",");
                if (tempStrs != null) {
                    int tempLen = tempStrs.length;
                    if (tempLen > 0) {
                        result[0] = Boolean.parseBoolean(tempStrs[0]);
                    }
                    if (tempLen > 1) {
                        result[1] = Boolean.parseBoolean(tempStrs[1]);
                    }
                    if (tempLen > 2) {
                        result[2] = Boolean.parseBoolean(tempStrs[2]);
                    }
                    if (tempLen > 3) {
                        result[3] = Boolean.parseBoolean(tempStrs[3]);
                    }
                    if (tempLen > 4) {
                        result[4] = Boolean.parseBoolean(tempStrs[4]);
                    }
                    if (tempLen > 5) {
                        result[5] = Boolean.parseBoolean(tempStrs[5]);
                    }
                }
            } catch (Exception e) {
            }
        }
        return result;
    }

    public void UpdateLocation(long loctime2, double longtitude2, double latitude2, double elevation2, double accuracy2, double rate2, float bearing2) {
        this.isOpen = true;
        this.loctime = loctime2;
        this.lastTimeInterval = new Date().getTime() - loctime2;
        boolean tmpNeedUpdate = true;
        if (this.longtitude == longtitude2 && this.latitude == latitude2 && this.elevation == elevation2 && this.accuracy == accuracy2) {
            tmpNeedUpdate = false;
        }
        this.longtitude = longtitude2;
        this.latitude = latitude2;
        if (elevation2 != -10000.0d) {
            this.elevation = elevation2 - PubVar.GPS_Antenna_Height;
        }
        if (accuracy2 >= 0.0d) {
            this.accuracy = accuracy2;
        }
        if (rate2 >= 0.0d) {
            this.rate = rate2;
        }
        if (bearing2 >= 0.0f) {
            this.bearing = bearing2;
        }
        if (this.PDOP == 100.0f) {
            this.HDOP = ((float) ((int) accuracy2)) / 5.0f;
            this.PDOP = 2.236f * this.HDOP;
            if (this.VDOP == 100.0f) {
                this.VDOP = 2.0f * this.HDOP;
            }
        }
        if (PubVar.m_Workspace.GetCoorSystem() != null) {
            this.m_TranpCoordinate = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(this.longtitude, this.latitude, this.elevation, this._CoordinateSystem);
        }
        if (tmpNeedUpdate) {
            Common.AddTimeRecord(new Date(this.loctime));
            updateControlsInfo();
        }
        if (this.IsOpenFenceAlarm && this.m_TranpCoordinate != null && !this.FenceBorder.IsContainPoint(this.m_TranpCoordinate, true) && this.FenceAlarmTimer == 0) {
            this.FenceAlarmTimer = 3;
        }
    }

    public void UpdateLocation(long loctime2, double longtitude2, double latitude2, double elevation2, double accuracy2, double rate2, float bearing2, float PDOP2) {
        this.PDOP = PDOP2;
        UpdateLocation(loctime2, longtitude2, latitude2, elevation2, accuracy2, rate2, bearing2);
        updateControlsInfo();
    }

    public void UpdateLocation(long loctime2, double longtitude2, double latitude2, double elevation2, double accuracy2, double rate2, float bearing2, int satellitesCount2, int usedSateCount2, float PDOP2) {
        this.satellitesCount = satellitesCount2;
        this.usedSateCount = usedSateCount2;
        this.PDOP = PDOP2;
        UpdateLocation(loctime2, longtitude2, latitude2, elevation2, accuracy2, rate2, bearing2);
        updateControlsInfo();
    }

    public void setTexView(TextView textView) {
        this.infoTextView = textView;
    }

    public void setControls(TextView TitleTextView, TextView TipTextView, ImageView GpsStatusImgView, ImageView GpsSNRImgView) {
        this.infoTextView = TitleTextView;
        this.tipTextView = TipTextView;
        this.gpsStatusImgView = GpsStatusImgView;
        this.gpsSNRImgView = GpsSNRImgView;
    }

    public void updateShowTextInfo(TextView textView) {
        if (PubVar.HeadTip_Visible && textView != null) {
            textView.setText(GetGPSCoordinateShow());
        }
    }

    public void updateRecData(String msg) {
        String[] tempStrs;
        this.currentRecDataLine++;
        this.lastRecData = msg;
        try {
            if (msg.contains("GPGSA") && (tempStrs = msg.split(",")) != null && tempStrs.length > 17) {
                String tempStr = tempStrs[15];
                if (tempStr != null) {
                    this.PDOP = Float.parseFloat(tempStr);
                }
                String tempStr2 = tempStrs[16];
                if (tempStr2 != null) {
                    this.HDOP = Float.parseFloat(tempStr2);
                }
                String tempStr3 = tempStrs[17];
                if (tempStr3 != null) {
                    this.VDOP = Float.parseFloat(tempStr3);
                }
            }
        } catch (Exception e) {
        }
    }

    public String getGPSStautsInfoStr() {
        String result = "";
        try {
            if (this.status.equals("未定位")) {
                return "未定位";
            }
            if (this.gpsStatusInfoBool[0]) {
                result = String.valueOf(this.status) + "-";
            }
            if (this.gpsStatusInfoBool[1]) {
                result = String.valueOf(result) + String.format("%.1f", Double.valueOf(this.accuracy)) + "m-";
            }
            if (this.gpsStatusInfoBool[2]) {
                result = String.valueOf(result) + String.format("%.1f", Float.valueOf(this.PDOP)) + "-";
            }
            if (this.gpsStatusInfoBool[3]) {
                result = String.valueOf(result) + String.format("%.1f", Double.valueOf(this.rate)) + "km/h-";
            }
            if (this.gpsStatusInfoBool[4]) {
                result = String.valueOf(result) + String.format("%.1f", Float.valueOf(this.VDOP)) + "-";
            }
            if (this.gpsStatusInfoBool[5]) {
                result = String.valueOf(result) + this.satellitesCount + FileSelector_Dialog.sRoot + this.usedSateCount + "-";
            }
            if (!result.equals("")) {
                return result.substring(0, result.length() - 1);
            }
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    public void updateControlsInfo() {
        try {
            if (PubVar._PubCommand.m_Navigation != null) {
                PubVar._PubCommand.m_Navigation.UpdateGPSLocation();
            }
            PubVar._PubCommand.UpdateGPSStatus(this);
            if (PubVar.HeadTip_Visible) {
                String tempStr = GetGPSCoordinateShow();
                if (!this.lastSimpleLocString.equals(tempStr)) {
                    this.lastSimpleLocString = tempStr;
                    if (this.infoTextView != null) {
                        this.infoTextView.setText(this.lastSimpleLocString);
                    }
                }
                if (this.status.equals("未定位")) {
                    if (this.tipTextView != null) {
                        this.tipTextView.setText("--");
                    }
                    if (this.gpsSNRImgView != null) {
                        this.gpsSNRImgView.setImageResource(R.drawable.gpssxh_close);
                    }
                } else {
                    if (this.tipTextView != null) {
                        this.tipTextView.setText(getGPSStautsInfoStr());
                    }
                    if (this.gpsSNRImgView != null) {
                        if (this.satellitesCount <= 4) {
                            this.gpsSNRImgView.setImageResource(R.drawable.gpsxh0);
                        } else if (this.satellitesCount <= 6) {
                            this.gpsSNRImgView.setImageResource(R.drawable.gpsxh1);
                        } else if (this.satellitesCount <= 8) {
                            this.gpsSNRImgView.setImageResource(R.drawable.gpsxh2);
                        } else if (this.satellitesCount <= 12) {
                            this.gpsSNRImgView.setImageResource(R.drawable.gpsxh3);
                        } else {
                            this.gpsSNRImgView.setImageResource(R.drawable.gpsxh4);
                        }
                    }
                }
                if (this.gpsStatusImgView != null) {
                    UpdateGPSTopICON();
                }
            }
        } catch (Exception ex) {
            Common.Log("updateControlsInfo", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    public void UpdateGPSTopICON() {
        boolean tempBool = false;
        if (this.isOpen) {
            if (PubVar._PubCommand.m_GPSDevice != null && this._isLocation) {
                if (PubVar._PubCommand.m_GPSDevice instanceof BluetoothGPSDevice) {
                    if (this.IsOpenFenceAlarm) {
                        this.gpsStatusImgView.setImageResource(R.drawable.gpsbluet_fence32);
                    } else {
                        this.gpsStatusImgView.setImageResource(R.drawable.gpsbluet32);
                    }
                } else if (this.IsOpenFenceAlarm) {
                    this.gpsStatusImgView.setImageResource(R.drawable.gps_fence32);
                } else {
                    this.gpsStatusImgView.setImageResource(R.drawable.gps32);
                }
                tempBool = true;
            }
            if (!tempBool) {
                this.gpsStatusImgView.setImageResource(R.drawable.gps232);
                tempBool = true;
            }
        }
        if (!tempBool) {
            this.gpsStatusImgView.setImageResource(R.drawable.gps232);
        }
    }

    public void setGPSOpenClose(boolean open) {
        if (open) {
            this.isOpen = true;
        } else {
            this.isOpen = false;
            this.status = "未定位";
        }
        updateControlsInfo();
    }

    public Coordinate getGPSCoordinate() {
        return this.m_TranpCoordinate;
    }

    public Coordinate getGPSCoordinate2() {
        return new Coordinate(this.longtitude, this.latitude, this.elevation);
    }

    public AbstractC0383CoordinateSystem GetCoordinateSystem() {
        return this._CoordinateSystem;
    }

    public String GetGPSCoordinateShow() {
        StringBuilder result = new StringBuilder();
        try {
            if (!this.isOpen || !IsLocation()) {
                return HeadGPSCoordShowType_Dialog.GetGPSShowFormatSample();
            }
            if (PubVar.GPS_Show_CoorSystem_Type == 0) {
                result.append("E:");
                result.append(ConvertCoordValue(this.longtitude));
                if (PubVar.GPS_Show_CoordMultiLine) {
                    result.append("\r\nN:");
                } else {
                    result.append("  N:");
                }
                result.append(ConvertCoordValue(this.latitude));
            } else {
                Coordinate tempCoord = getGPSCoordinate();
                if (tempCoord != null) {
                    result.append("X:" + String.format("%.3f", Double.valueOf(tempCoord.getX())));
                    if (PubVar.GPS_Show_CoordMultiLine) {
                        result.append("\r\nY:");
                    } else {
                        result.append("  Y:");
                    }
                    result.append(String.format("%.3f", Double.valueOf(tempCoord.getY())));
                }
            }
            if (PubVar.GPS_Show_Elevation) {
                if (PubVar.GPS_Show_CoordMultiLine) {
                    result.append("\r\nH:");
                } else {
                    result.append("  H:");
                }
                result.append(String.format("%.3f", Double.valueOf(this.elevation)));
            }
            return result.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String ConvertCoordValue(double value) {
        if (PubVar.GPS_Show_CoorSystem_Type == 1) {
            return String.format("%.3f", Double.valueOf(value));
        }
        if (PubVar.GPS_Show_Format_Type == 0) {
            return String.valueOf(String.format("%.7f", Double.valueOf(value))) + "°";
        }
        return Common.ConvertDegree(value);
    }

    public void SetFenceBorder(Polyline fenceBorder) {
        this.FenceBorder = fenceBorder;
    }
}
