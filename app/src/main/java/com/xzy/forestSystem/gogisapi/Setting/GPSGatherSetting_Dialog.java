package  com.xzy.forestSystem.gogisapi.Setting;

import android.content.DialogInterface;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class GPSGatherSetting_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private InputSpinner m_GPSGatherAccuESD;
    private InputSpinner m_GPSGatherDisESD;
    private InputSpinner m_GPSGatherTimeESD;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public GPSGatherSetting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Setting.GPSGatherSetting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("确定")) {
                    try {
                        String tempStr01 = GPSGatherSetting_Dialog.this.m_GPSGatherTimeESD.getText();
                        if (tempStr01.equals("不限")) {
                            PubVar.GPSGatherIntervalTime = 0;
                        } else {
                            if (tempStr01.contains("秒")) {
                                tempStr01 = tempStr01.substring(0, tempStr01.length() - 1);
                            }
                            PubVar.GPSGatherIntervalTime = (long) (Double.parseDouble(tempStr01) * 1000.0d);
                        }
                        String tempStr012 = GPSGatherSetting_Dialog.this.m_GPSGatherDisESD.getText();
                        if (tempStr012.equals("不限")) {
                            PubVar.GPSGatherIntervalDistance = 0.0d;
                        } else {
                            if (tempStr012.contains("m")) {
                                tempStr012 = tempStr012.substring(0, tempStr012.length() - 1);
                            }
                            PubVar.GPSGatherIntervalDistance = Double.parseDouble(tempStr012);
                        }
                        String tempStr013 = GPSGatherSetting_Dialog.this.m_GPSGatherAccuESD.getText();
                        if (tempStr013.equals("不限")) {
                            PubVar.GPS_Gather_Accuracy = 0.0d;
                        } else {
                            if (tempStr013.contains("m")) {
                                tempStr013 = tempStr013.substring(0, tempStr013.length() - 1);
                            }
                            PubVar.GPS_Gather_Accuracy = Double.parseDouble(tempStr013);
                        }
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_GPS_Gather_IntervalTime", Long.valueOf(PubVar.GPSGatherIntervalTime));
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_GPS_Gather_IntervalDistance", Double.valueOf(PubVar.GPSGatherIntervalDistance));
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_GPS_Gather_Accuracy", Double.valueOf(PubVar.GPS_Gather_Accuracy));
                    } catch (Exception e) {
                    }
                    if (GPSGatherSetting_Dialog.this.m_Callback != null) {
                        GPSGatherSetting_Dialog.this.m_Callback.OnClick("GPS数据采集参数设置返回", null);
                    }
                    GPSGatherSetting_Dialog.this._Dialog.dismiss();
                }
            }
        };
        this.m_GPSGatherTimeESD = null;
        this.m_GPSGatherDisESD = null;
        this.m_GPSGatherAccuESD = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.setting_gpsgather_paras);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("GPS采样参数设置");
        this._Dialog.setCanceledOnTouchOutside(true);
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        String tempStr;
        String tempStr2;
        String tempStr3;
        this.m_GPSGatherTimeESD = (InputSpinner) this._Dialog.findViewById(R.id.sp_gpsgatherTime);
        int tempInt = (int) (PubVar.GPSGatherIntervalTime / 1000);
        if (tempInt <= 0) {
            tempStr = "不限";
        } else {
            tempStr = String.valueOf(String.valueOf(tempInt)) + "秒";
        }
        this.m_GPSGatherTimeESD.setText(tempStr);
        this.m_GPSGatherDisESD = (InputSpinner) this._Dialog.findViewById(R.id.sp_gpsgatherDistance);
        if (PubVar.GPSGatherIntervalDistance <= 0.0d) {
            tempStr2 = "不限";
        } else {
            tempStr2 = String.valueOf(String.valueOf(PubVar.GPSGatherIntervalDistance)) + "m";
        }
        this.m_GPSGatherDisESD.setText(tempStr2);
        List tempList01 = new ArrayList();
        tempList01.add("不限");
        tempList01.add("1.0");
        tempList01.add("1.5");
        tempList01.add("2.0");
        tempList01.add("2.5");
        tempList01.add("3.0");
        tempList01.add("5.0");
        tempList01.add("10.0");
        tempList01.add("15.0");
        tempList01.add("30.0");
        tempList01.add("60.0");
        this.m_GPSGatherTimeESD.SetSelectItemList(tempList01);
        this.m_GPSGatherTimeESD.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Setting.GPSGatherSetting_Dialog.2
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.contains("SpinnerSelectCallback")) {
                    GPSGatherSetting_Dialog.this.m_GPSGatherTimeESD.setText(pObject.toString());
                }
            }
        });
        List tempList02 = new ArrayList();
        tempList02.add("不限");
        tempList02.add("1.0");
        tempList02.add("1.5");
        tempList02.add("2.0");
        tempList02.add("2.5");
        tempList02.add("3.0");
        tempList02.add("5.0");
        tempList02.add("10.0");
        tempList02.add("30.0");
        tempList02.add("50.0");
        tempList02.add("100.0");
        this.m_GPSGatherDisESD.SetSelectItemList(tempList02);
        this.m_GPSGatherDisESD.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Setting.GPSGatherSetting_Dialog.3
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.contains("SpinnerSelectCallback")) {
                    GPSGatherSetting_Dialog.this.m_GPSGatherDisESD.setText(pObject.toString());
                }
            }
        });
        this.m_GPSGatherAccuESD = (InputSpinner) this._Dialog.findViewById(R.id.sp_gpsgatherAccuracy);
        if (PubVar.GPS_Gather_Accuracy <= 0.0d) {
            tempStr3 = "不限";
        } else {
            tempStr3 = String.valueOf(String.valueOf(PubVar.GPS_Gather_Accuracy)) + "m";
        }
        this.m_GPSGatherAccuESD.setText(tempStr3);
        List tempList03 = new ArrayList();
        tempList03.add("不限");
        tempList03.add("1.0");
        tempList03.add("3.0");
        tempList03.add("5.0");
        tempList03.add("10.0");
        tempList03.add("30.0");
        tempList03.add("50.0");
        tempList03.add("100.0");
        this.m_GPSGatherAccuESD.SetSelectItemList(tempList03);
        this.m_GPSGatherAccuESD.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Setting.GPSGatherSetting_Dialog.4
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.contains("SpinnerSelectCallback")) {
                    GPSGatherSetting_Dialog.this.m_GPSGatherAccuESD.setText(pObject.toString());
                }
            }
        });
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Setting.GPSGatherSetting_Dialog.5
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                GPSGatherSetting_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
