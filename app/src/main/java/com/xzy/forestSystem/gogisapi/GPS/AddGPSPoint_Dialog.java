package  com.xzy.forestSystem.gogisapi.GPS;

import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddGPSPoint_Dialog {
    private boolean _ConsiderPMTrans;
    private XDialogTemplate _Dialog;
    private boolean _ShowCoordXYType;
    private int currentGPSIndex;
    private boolean isThreadIsAlive;
    private double lastGPSX;
    private double lastGPSY;
    private double lastGPSZ;
    private double lastTotalX;
    private double lastTotalY;
    private double lastTotalZ;
    private ICallback m_Callback;
    private Coordinate m_Coordinate;
    private Button m_GatherGPSBtn;
    private TextView m_HasGatherCountTV;
    private Runnable myTask;
    private Handler myhandler;
    private ICallback pCallback;
    private Drawable startBtnDrawable;
    private Drawable stopBtnDrawable;
    private int totalGPSCount;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public AddGPSPoint_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_GatherGPSBtn = null;
        this.startBtnDrawable = null;
        this.stopBtnDrawable = null;
        this.m_HasGatherCountTV = null;
        this.m_Coordinate = new Coordinate(0.0d, 0.0d);
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.GPS.AddGPSPoint_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                try {
                    if (paramString.equals("确定")) {
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_GPS_Gather_PointsCount", Integer.valueOf(AddGPSPoint_Dialog.this.totalGPSCount));
                        AddGPSPoint_Dialog.this.isThreadIsAlive = false;
                        String tempPara01 = Common.GetEditTextValueOnID(AddGPSPoint_Dialog.this._Dialog, R.id.editTextPara01);
                        String tempPara02 = Common.GetEditTextValueOnID(AddGPSPoint_Dialog.this._Dialog, R.id.editTextPara02);
                        if (tempPara01 == null || tempPara01.trim().equals("") || tempPara02 == null || tempPara02.trim().equals("")) {
                            Common.ShowDialog("坐标值不能为空.");
                            return;
                        }
                        double tempX0 = Double.parseDouble(tempPara01);
                        double tempY0 = Double.parseDouble(tempPara02);
                        if (tempX0 == 0.0d && tempY0 == 0.0d) {
                            Common.ShowDialog("GPS采集数据无效.");
                            return;
                        }
                        if (AddGPSPoint_Dialog.this._ShowCoordXYType) {
                            AddGPSPoint_Dialog.this.m_Coordinate.setX(tempX0);
                            AddGPSPoint_Dialog.this.m_Coordinate.setY(tempY0);
                        } else {
                            AddGPSPoint_Dialog.this.m_Coordinate.setGeoX(tempX0);
                            AddGPSPoint_Dialog.this.m_Coordinate.setGeoY(tempY0);
                        }
                        if (AddGPSPoint_Dialog.this.m_Callback != null) {
                            AddGPSPoint_Dialog.this.m_Callback.OnClick("添加GPS采样点返回", AddGPSPoint_Dialog.this.m_Coordinate);
                        }
                        AddGPSPoint_Dialog.this._Dialog.dismiss();
                    }
                } catch (Exception e) {
                }
            }
        };
        this.totalGPSCount = 5;
        this.currentGPSIndex = 0;
        this.myhandler = new Handler();
        this.lastGPSX = 0.0d;
        this.lastGPSY = 0.0d;
        this.lastGPSZ = 0.0d;
        this.lastTotalX = 0.0d;
        this.lastTotalY = 0.0d;
        this.lastTotalZ = 0.0d;
        this.isThreadIsAlive = false;
        this._ConsiderPMTrans = true;
        this._ShowCoordXYType = true;
        this.myTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.GPS.AddGPSPoint_Dialog.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (AddGPSPoint_Dialog.this.isThreadIsAlive) {
                        if (PubVar.GPS_Gather_Accuracy == 0.0d || PubVar._PubCommand.m_GpsLocation.accuracy <= PubVar.GPS_Gather_Accuracy) {
                            double tempX = PubVar._PubCommand.m_GpsLocation.longtitude;
                            double tempY = PubVar._PubCommand.m_GpsLocation.latitude;
                            double tempZ = PubVar._PubCommand.m_GpsLocation.elevation;
                            if (!(AddGPSPoint_Dialog.this.lastGPSX == tempX && AddGPSPoint_Dialog.this.lastGPSY == tempY)) {
                                AddGPSPoint_Dialog.this.lastTotalX += tempX;
                                AddGPSPoint_Dialog.this.lastTotalY += tempY;
                                AddGPSPoint_Dialog.this.lastTotalZ += tempZ;
                                AddGPSPoint_Dialog.this.lastGPSX = tempX;
                                AddGPSPoint_Dialog.this.lastGPSY = tempY;
                                AddGPSPoint_Dialog.this.lastGPSZ = tempZ;
                                AddGPSPoint_Dialog.this.currentGPSIndex++;
                                AddGPSPoint_Dialog.this.m_HasGatherCountTV.setText(String.valueOf(AddGPSPoint_Dialog.this.currentGPSIndex));
                                if (AddGPSPoint_Dialog.this.currentGPSIndex > 0) {
                                    Coordinate tmpCoord = new Coordinate();
                                    tmpCoord.setX(AddGPSPoint_Dialog.this.lastTotalX / ((double) AddGPSPoint_Dialog.this.currentGPSIndex));
                                    tmpCoord.setY(AddGPSPoint_Dialog.this.lastTotalY / ((double) AddGPSPoint_Dialog.this.currentGPSIndex));
                                    tmpCoord.setZ(AddGPSPoint_Dialog.this.lastTotalZ / ((double) AddGPSPoint_Dialog.this.currentGPSIndex));
                                    if (AddGPSPoint_Dialog.this._ConsiderPMTrans) {
                                        AddGPSPoint_Dialog.this.m_Coordinate = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(tmpCoord, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                                    } else {
                                        AddGPSPoint_Dialog.this.m_Coordinate = PubVar.m_Workspace.GetCoorSystem().ConvertBLToXY(tmpCoord);
                                        AddGPSPoint_Dialog.this.m_Coordinate.setGeoX(tmpCoord.getX());
                                        AddGPSPoint_Dialog.this.m_Coordinate.setGeoY(tmpCoord.getY());
                                    }
                                    AddGPSPoint_Dialog.this.DoCommand("显示坐标");
                                }
                            }
                            if (AddGPSPoint_Dialog.this.currentGPSIndex < AddGPSPoint_Dialog.this.totalGPSCount) {
                                AddGPSPoint_Dialog.this.myhandler.postDelayed(AddGPSPoint_Dialog.this.myTask, 1000);
                                return;
                            }
                            AddGPSPoint_Dialog.this.isThreadIsAlive = false;
                            AddGPSPoint_Dialog.this.DoCommand("显示坐标");
                            AddGPSPoint_Dialog.this.DoCommand("停止GPS数据采样");
                            return;
                        }
                        AddGPSPoint_Dialog.this.DoCommand("暂停GPS数据采样");
                        Common.ShowDialog("当前定位设备定位精度约" + String.valueOf(PubVar._PubCommand.m_GpsLocation.accuracy) + "m,大于系统设置的GPS数据采集精度" + String.valueOf(PubVar.GPS_Gather_Accuracy) + "m.\r\n将暂停GPS数据采集.");
                    }
                } catch (Exception e) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.addgpspoint_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("添加GPS采样点");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        Resources res = PubVar.MainContext.getResources();
        this.startBtnDrawable = res.getDrawable(R.drawable.start1148);
        this.startBtnDrawable.setBounds(0, 0, this.startBtnDrawable.getMinimumWidth(), this.startBtnDrawable.getMinimumHeight());
        this.stopBtnDrawable = res.getDrawable(R.drawable.pause48);
        this.stopBtnDrawable.setBounds(0, 0, this.stopBtnDrawable.getMinimumWidth(), this.stopBtnDrawable.getMinimumHeight());
        this.m_GatherGPSBtn = (Button) this._Dialog.findViewById(R.id.btn_AddGPSPtn_StartStop);
        this.m_GatherGPSBtn.setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonCheckPtnExchange).setOnClickListener(new ViewClick());
        ((CheckBox) this._Dialog.findViewById(R.id.ck_consideConvert)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.AddGPSPoint_Dialog.3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                AddGPSPoint_Dialog.this._ConsiderPMTrans = arg1;
            }
        });
        this.m_HasGatherCountTV = (TextView) this._Dialog.findViewById(R.id.tv_hasGatherPtnCount);
        this.m_HasGatherCountTV.setText("0");
        InputSpinner tempSpinnerDialog = (InputSpinner) this._Dialog.findViewById(R.id.sp_gatherGPSCount);
        List tempScaleList = new ArrayList();
        tempScaleList.add("1");
        tempScaleList.add("3");
        tempScaleList.add("5");
        tempScaleList.add("10");
        tempScaleList.add("30");
        tempScaleList.add("50");
        tempSpinnerDialog.getEditTextView().setInputType(4096);
        tempSpinnerDialog.SetSelectItemList(tempScaleList);
        tempSpinnerDialog.setText("5");
        tempSpinnerDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.GPS.AddGPSPoint_Dialog.4
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.contains("SpinnerSelectCallback")) {
                    ((InputSpinner) AddGPSPoint_Dialog.this._Dialog.findViewById(R.id.sp_gatherGPSCount)).setText(pObject.toString());
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        boolean z = false;
        try {
            if (command.equals("开始GPS数据采样")) {
                if (PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                    this.totalGPSCount = Integer.parseInt(((InputSpinner) this._Dialog.findViewById(R.id.sp_gatherGPSCount)).getEditTextView().getText().toString());
                    if (this.totalGPSCount > 0) {
                        this.m_GatherGPSBtn.setCompoundDrawables(this.stopBtnDrawable, null, null, null);
                        this.m_GatherGPSBtn.setTag("停止GPS数据采样");
                        this.isThreadIsAlive = false;
                        this.lastGPSX = 0.0d;
                        this.lastGPSY = 0.0d;
                        this.lastTotalX = 0.0d;
                        this.lastTotalY = 0.0d;
                        this.currentGPSIndex = 0;
                        this.m_HasGatherCountTV.setText(String.valueOf(this.currentGPSIndex));
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        this.isThreadIsAlive = true;
                        this.myhandler.post(this.myTask);
                        return;
                    }
                    Common.ShowDialog("GPS采用点不能少于1个.");
                    return;
                }
                Common.ShowDialog("请先开启GPS设备并定位.");
            } else if (command.equals("停止GPS数据采样")) {
                this.isThreadIsAlive = false;
                this.m_GatherGPSBtn.setCompoundDrawables(this.startBtnDrawable, null, null, null);
                this.m_GatherGPSBtn.setTag("开始GPS数据采样");
                if (PubVar.GPS_Gather_PointAuto) {
                    this.pCallback.OnClick("确定", null);
                }
            } else if (command.equals("显示坐标")) {
                if (this._ShowCoordXYType) {
                    Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara01, String.valueOf(this.m_Coordinate.getX()));
                    Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara02, String.valueOf(this.m_Coordinate.getY()));
                    return;
                }
                Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara01, String.valueOf(this.m_Coordinate.getGeoX()));
                Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara02, String.valueOf(this.m_Coordinate.getGeoY()));
            } else if (command.equals("切换坐标显示")) {
                if (!this._ShowCoordXYType) {
                    z = true;
                }
                this._ShowCoordXYType = z;
                if (this._ShowCoordXYType) {
                    Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_xlabel, "X：");
                    Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_ylabel, "Y：");
                } else {
                    Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_xlabel, "经度(°)：");
                    Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_ylabel, "纬度(°)：");
                }
                DoCommand("显示坐标");
            }
        } catch (Exception e2) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.AddGPSPoint_Dialog.5
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                String tempStr;
                if (!PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                    Common.ShowDialog("请先开启GPS设备并定位.");
                    AddGPSPoint_Dialog.this._Dialog.dismiss();
                    return;
                }
                AddGPSPoint_Dialog.this.totalGPSCount = 5;
                HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_GPS_Gather_PointsCount");
                if (!(tempHashMap == null || (tempStr = tempHashMap.get("F2")) == null || tempStr.equals(""))) {
                    AddGPSPoint_Dialog.this.totalGPSCount = Integer.parseInt(tempStr);
                }
                if (AddGPSPoint_Dialog.this.totalGPSCount < 1) {
                    AddGPSPoint_Dialog.this.totalGPSCount = 5;
                }
                ((InputSpinner) AddGPSPoint_Dialog.this._Dialog.findViewById(R.id.sp_gatherGPSCount)).getEditTextView().setText(String.valueOf(AddGPSPoint_Dialog.this.totalGPSCount));
                if (PubVar.GPS_Gather_PointAuto) {
                    AddGPSPoint_Dialog.this.DoCommand("开始GPS数据采样");
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
                AddGPSPoint_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
