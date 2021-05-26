package  com.xzy.forestSystem.gogisapi.GPS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GPSInfoDialog {
    private static boolean m_ShowAccuracy = false;
    private XDialogTemplate _Dialog = null;
    private int deviceCurrentSeletedIndex = 0;
    private int deviceSeletedIndex = 0;
    private GPSSatelliteView gpsView = null;
    private boolean isOutBool = false;
    private boolean isShowMsg = false;
    private int lastCompassAngle = -1;
    private long lastGetMsgIndex = 0;
    private long lastLocTime = 0;
    private ICallback m_Callback = null;
    private Context m_Context = null;
    LinearLayout myLayout01;
    LinearLayout myLayout02;
    private Runnable myTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.GPS.GPSInfoDialog.2
        @Override // java.lang.Runnable
        public void run() {
            if (!GPSInfoDialog.this.isOutBool) {
                int tempAngle = -1;
                try {
                    if (!(PubVar._PubCommand.m_GPSDevice == null || PubVar._PubCommand.m_GPSDevice.gpsLocation == null)) {
                        tempAngle = (int) PubVar._PubCommand.m_GPSDevice.gpsLocation.bearing;
                        long tempLocTime = PubVar._PubCommand.m_GPSDevice.gpsLocation.loctime;
                        if (!(tempLocTime == 0 || tempLocTime == GPSInfoDialog.this.lastLocTime)) {
                            GPSInfoDialog.this.RefreshInfo();
                            long tempLocTime2 = GPSInfoDialog.this.lastLocTime;
                            if (!GPSInfoDialog.this.isShowMsg) {
                                GPSInfoDialog.this.gpsView.UpdateGPSDevice(PubVar._PubCommand.m_GPSDevice);
                                GPSInfoDialog.this.gpsView.UpdateCompass(PubVar._PubCommand.m_Compass.ZAngle);
                            } else if (GPSInfoDialog.this.isShowMsg && GPSInfoDialog.this.lastGetMsgIndex < PubVar._PubCommand.m_GPSDevice.gpsLocation.currentRecDataLine) {
                                GPSInfoDialog.this.txtMsg.append(String.valueOf(PubVar._PubCommand.m_GPSDevice.gpsLocation.lastRecData) + "\n");
                                GPSInfoDialog.this.lastGetMsgIndex = PubVar._PubCommand.m_GPSDevice.gpsLocation.currentRecDataLine;
                                if (GPSInfoDialog.this.txtMsg.getLineCount() > 100) {
                                    GPSInfoDialog.this.txtMsg.setText("");
                                }
                                GPSInfoDialog.this.scrollViewMsg.scrollTo(0, GPSInfoDialog.this.txtMsg.getHeight());
                            }
                        }
                    }
                    if (tempAngle != -1 && (GPSInfoDialog.this.lastCompassAngle == -1 || GPSInfoDialog.this.lastCompassAngle != tempAngle)) {
                        GPSInfoDialog.this.lastCompassAngle = tempAngle;
                        GPSInfoDialog.this.gpsView.UpdateCompass((float) tempAngle);
                    }
                } catch (Exception e) {
                }
                GPSInfoDialog.this.myhandler.postDelayed(GPSInfoDialog.this.myTask, 200);
            }
        }
    };
    private Handler myhandler = new Handler();
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.GPS.GPSInfoDialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
            String tempStr;
            if (paramString.equals("打开GPS")) {
                PubVar._PubCommand.m_GpsLocation.isOpen = true;
                GPSInfoDialog.this._Dialog.dismiss();
                GPSInfoDialog.this.isOutBool = true;
            } else if (paramString.equals("关闭GPS")) {
                PubVar._PubCommand.ProcessCommand("GPS_关闭GPS");
                GPSInfoDialog.this._Dialog.dismiss();
                GPSInfoDialog.this.isOutBool = true;
            } else if (paramString.equals("选择GPS设备")) {
                PubVar._PubCommand.m_GpsLocation.isOpen = true;
                int tempdeviceSeletedIndex = 0;
                HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_GPS");
                if (!(tempHashMap == null || (tempStr = tempHashMap.get("F3")) == null || !tempStr.equals("1"))) {
                    tempdeviceSeletedIndex = 1;
                }
                GPSInfoDialog.this.deviceCurrentSeletedIndex = tempdeviceSeletedIndex;
                int finalTempdeviceSeletedIndex = tempdeviceSeletedIndex;
                new AlertDialog.Builder(PubVar.MainContext).setTitle("选择GPS设备").setSingleChoiceItems(new String[]{"内置GPS设备", "蓝牙GPS设备"}, GPSInfoDialog.this.deviceCurrentSeletedIndex, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.GPSInfoDialog.1.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        GPSInfoDialog.this.deviceCurrentSeletedIndex = which;
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.GPSInfoDialog.1.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialog, int which) {
                        if (GPSInfoDialog.this.deviceCurrentSeletedIndex == 0 && finalTempdeviceSeletedIndex != GPSInfoDialog.this.deviceCurrentSeletedIndex) {
                            GPSLocationClass.SaveGPSDeviceInfo("0", null);
                            PubVar._PubCommand.ProcessCommand("GPS_打开GPS");
                        } else if (GPSInfoDialog.this.deviceCurrentSeletedIndex == 1) {
                            BluetoothGPSSelectDialog tempDialog = new BluetoothGPSSelectDialog(GPSInfoDialog.this.m_Context);
                            tempDialog.SetCallback(GPSInfoDialog.this.pCallback);
                            tempDialog.ShowDialog();
                        }
                        dialog.dismiss();
                        GPSInfoDialog.this._Dialog.dismiss();
                    }
                }).setNegativeButton("取消", (DialogInterface.OnClickListener) null).show();
            } else {
                GPSInfoDialog.this._Dialog.dismiss();
                GPSInfoDialog.this.isOutBool = true;
            }
        }
    };
    ScrollView scrollViewMsg = null;
    TextView txt01;
    TextView txt02;
    TextView txt03;
    TextView txt04;
    TextView txt05;
    TextView txt06;
    TextView txtMsg;
    TextView txtTitle;
    TextView txttime;

    public GPSInfoDialog(Context context) {
        this.m_Context = context;
        this._Dialog = new XDialogTemplate(this.m_Context);
        this._Dialog.SetLayoutView(R.layout.gps_info_dialog);
        this._Dialog.SetCaption("GPS信息");
        if (!(PubVar._PubCommand.m_GPSDevice == null || PubVar._PubCommand.m_GPSDevice.gpsLocation == null)) {
            if (PubVar._PubCommand.m_GPSDevice.gpsLocation.isOpen) {
                this._Dialog.SetHeadButtons("1,2130837613,关闭,关闭GPS", this.pCallback);
            } else {
                this._Dialog.SetHeadButtons("1,2130837709,开启,打开GPS", this.pCallback);
            }
        }
        this._Dialog.findViewById(R.id.btn_gpsinfo_ChangeDevice).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.GPSInfoDialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                GPSInfoDialog.this.pCallback.OnClick("选择GPS设备", null);
            }
        });
        this.txttime = (TextView) this._Dialog.findViewById(R.id.textViewTime);
        this.txt01 = (TextView) this._Dialog.findViewById(R.id.textView01);
        this.txt02 = (TextView) this._Dialog.findViewById(R.id.textView02);
        this.txt03 = (TextView) this._Dialog.findViewById(R.id.textView03);
        this.txt04 = (TextView) this._Dialog.findViewById(R.id.textView04);
        this.txt05 = (TextView) this._Dialog.findViewById(R.id.textView05);
        this.txt06 = (TextView) this._Dialog.findViewById(R.id.textView06);
        this.myLayout01 = (LinearLayout) this._Dialog.findViewById(R.id.linearlayout01);
        this.myLayout02 = (LinearLayout) this._Dialog.findViewById(R.id.linearlayout02);
        this.txtMsg = (TextView) this._Dialog.findViewById(R.id.textViewRecData);
        this.txtMsg.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.txtMsg.setOnTouchListener(new View.OnTouchListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.GPSInfoDialog.4
            @SuppressLint("WrongConstant")
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View arg0, MotionEvent arg1) {
                GPSInfoDialog.this.myLayout01.setVisibility(0);
                GPSInfoDialog.this.myLayout02.setVisibility(8);
                GPSInfoDialog.this.isShowMsg = false;
                arg1.setAction(3);
                return false;
            }
        });
        this.scrollViewMsg = (ScrollView) this._Dialog.findViewById(R.id.sv_show);
        if (PubVar._PubCommand.m_GPSDevice != null) {
            Common.ShowToast("当前定位设备: " + PubVar._PubCommand.m_GPSDevice.deviceName);
        }
        this.gpsView = (GPSSatelliteView) this._Dialog.findViewById(R.id.gPSSatelliteView1);
        this.gpsView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.GPSInfoDialog.5
            @SuppressLint("WrongConstant")
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                GPSInfoDialog.this.isShowMsg = true;
                GPSInfoDialog.this.myLayout02.setVisibility(0);
                GPSInfoDialog.this.myLayout01.setVisibility(8);
            }
        });
        ((Button) this._Dialog.findViewById(R.id.buttonRasterLayersMang)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.GPSInfoDialog.6
            @SuppressLint("WrongConstant")
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                GPSInfoDialog.this.myLayout01.setVisibility(0);
                GPSInfoDialog.this.myLayout02.setVisibility(8);
                GPSInfoDialog.this.isShowMsg = false;
            }
        });
        ((Button) this._Dialog.findViewById(R.id.buttonSaveMsg)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.GPSInfoDialog.7
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                try {
                    String tempMsg = GPSInfoDialog.this.txtMsg.getText().toString();
                    String fileName = String.valueOf(PubVar.m_SystemPath) + "/Files/" + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) + ".txt";
                    FileOutputStream fos = new FileOutputStream(new File(fileName));
                    fos.write(tempMsg.getBytes());
                    fos.close();
                    Common.ShowToast("保存文件成功\n:" + fileName);
                } catch (Exception e) {
                    Common.ShowToast("保存文件时错误:" + e.getMessage());
                }
            }
        });
        this.txt05.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.GPSInfoDialog.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                GPSInfoDialog.m_ShowAccuracy = !GPSInfoDialog.m_ShowAccuracy;
            }
        });
    }

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.GPSInfoDialog.9
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                GPSInfoDialog.this.myhandler.post(GPSInfoDialog.this.myTask);
            }
        });
        this._Dialog.show();
    }

    public static void scroll2Bottom(final ScrollView scroll, final View inner) {
        new Handler().post(new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.GPS.GPSInfoDialog.10
            @Override // java.lang.Runnable
            public void run() {
                if (scroll != null && inner != null) {
                    int offset = inner.getMeasuredHeight() - scroll.getMeasuredHeight();
                    if (offset < 0) {
                        offset = 0;
                    }
                    scroll.scrollTo(0, offset);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void RefreshInfo() {
        if (PubVar._PubCommand.m_GPSDevice != null && PubVar._PubCommand.m_GPSDevice.gpsLocation != null) {
            this.txttime.setText("时间:" + GPSLocationClass.dateFormat.format(new Date(PubVar._PubCommand.m_GPSDevice.gpsLocation.loctime)));
            if (!PubVar._PubCommand.m_GPSDevice.gpsLocation.status.equals("未定位")) {
                this.txt01.setText("经度:" + Common.ConvertDegree(PubVar._PubCommand.m_GPSDevice.gpsLocation.longtitude));
                this.txt02.setText("纬度:" + Common.ConvertDegree(PubVar._PubCommand.m_GPSDevice.gpsLocation.latitude));
                this.txt03.setText("速度:" + String.format("%.2f", Double.valueOf(PubVar._PubCommand.m_GPSDevice.gpsLocation.rate)) + "km/h");
                this.txt04.setText("高程:" + String.format("%.2f", Double.valueOf(PubVar._PubCommand.m_GPSDevice.gpsLocation.elevation)));
                if (m_ShowAccuracy) {
                    this.txt05.setText("精度(m):" + String.format("%.2f", Double.valueOf(PubVar._PubCommand.m_GPSDevice.gpsLocation.accuracy)));
                } else {
                    this.txt05.setText("PDOP:" + String.format("%.2f", Float.valueOf(PubVar._PubCommand.m_GPSDevice.gpsLocation.PDOP)));
                }
                this.txt06.setText("状态:" + PubVar._PubCommand.m_GPSDevice.gpsLocation.status);
                return;
            }
            PubVar._PubCommand.m_GPSDevice.satellites = new ArrayList();
            this.txt01.setText("经度:---");
            this.txt02.setText("纬度:---");
            this.txt03.setText("速度:---");
            this.txt04.setText("高程:---");
            this.txt05.setText("PDOP:---");
            this.txt06.setText("状态:" + PubVar._PubCommand.m_GPSDevice.gpsLocation.status);
        }
    }
}
