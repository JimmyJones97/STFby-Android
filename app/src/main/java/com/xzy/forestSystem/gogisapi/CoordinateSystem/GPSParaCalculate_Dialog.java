package  com.xzy.forestSystem.gogisapi.CoordinateSystem;

import android.content.DialogInterface;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GPSParaCalculate_Dialog {
    private Coordinate_WGS1984 WGS1984;
    private XDialogTemplate _Dialog;
    private int currentGPSIndex;

    /* renamed from: df */
    private DecimalFormat f466df;
    private DecimalFormat df2;
    private boolean isThreadIsAlive;
    private double lastGPSX;
    private double lastGPSY;
    private double lastGPSZ;
    private double lastTotalX;
    private double lastTotalY;
    private double lastTotalZ;
    private ICallback m_Callback;
    private EditText m_ResultEdit;
    private Runnable myTask;
    private Handler myhandler;
    private ICallback pCallback;
    private int totalGPSCount;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public GPSParaCalculate_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_ResultEdit = null;
        this.WGS1984 = new Coordinate_WGS1984();
        this.f466df = new DecimalFormat("0.000");
        this.df2 = new DecimalFormat("0.000000");
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.CoordinateSystem.GPSParaCalculate_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
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
        this.myTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.CoordinateSystem.GPSParaCalculate_Dialog.2
            @Override // java.lang.Runnable
            public void run() {
                if (GPSParaCalculate_Dialog.this.isThreadIsAlive) {
                    double tempX = PubVar._PubCommand.m_GpsLocation.longtitude;
                    double tempY = PubVar._PubCommand.m_GpsLocation.latitude;
                    double tempZ = PubVar._PubCommand.m_GpsLocation.elevation;
                    if (!(GPSParaCalculate_Dialog.this.lastGPSX == tempX && GPSParaCalculate_Dialog.this.lastGPSY == tempY)) {
                        Coordinate tempCoord = PubVar._PubCommand.m_GpsLocation.getGPSCoordinate();
                        GPSParaCalculate_Dialog.this.lastTotalX += tempCoord.getX();
                        GPSParaCalculate_Dialog.this.lastTotalY += tempCoord.getY();
                        GPSParaCalculate_Dialog.this.lastTotalZ += tempCoord.getZ();
                        GPSParaCalculate_Dialog.this.lastGPSX = tempX;
                        GPSParaCalculate_Dialog.this.lastGPSY = tempY;
                        GPSParaCalculate_Dialog.this.lastGPSZ = tempZ;
                        GPSParaCalculate_Dialog.this.currentGPSIndex++;
                        Common.SetEditTextValueOnID(GPSParaCalculate_Dialog.this._Dialog, R.id.et_GPSPara_GPS_X, String.valueOf(GPSParaCalculate_Dialog.this.lastTotalX / ((double) GPSParaCalculate_Dialog.this.currentGPSIndex)));
                        Common.SetEditTextValueOnID(GPSParaCalculate_Dialog.this._Dialog, R.id.et_GPSPara_GPS_Y, String.valueOf(GPSParaCalculate_Dialog.this.lastTotalY / ((double) GPSParaCalculate_Dialog.this.currentGPSIndex)));
                        Common.SetEditTextValueOnID(GPSParaCalculate_Dialog.this._Dialog, R.id.et_GPSPara_GPS_Z, String.valueOf(GPSParaCalculate_Dialog.this.lastTotalZ / ((double) GPSParaCalculate_Dialog.this.currentGPSIndex)));
                    }
                    if (GPSParaCalculate_Dialog.this.currentGPSIndex < GPSParaCalculate_Dialog.this.totalGPSCount) {
                        GPSParaCalculate_Dialog.this.myhandler.postDelayed(GPSParaCalculate_Dialog.this.myTask, 1000);
                    } else {
                        GPSParaCalculate_Dialog.this.isThreadIsAlive = false;
                    }
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.gpsparacalculate_dialog);
        this._Dialog.Resize(0.96f, 0.96f);
        this._Dialog.SetCaption("参数计算");
        this._Dialog.findViewById(R.id.btn_GPSParaGetGPS).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_GPSParaCal).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_gpsparm_getFromMeasure).setOnClickListener(new ViewClick());
        this.m_ResultEdit = (EditText) this._Dialog.findViewById(R.id.et_CalResult);
        this.m_ResultEdit.setVisibility(8);
        List<String> tmpList = new ArrayList<>();
        tmpList.add("北京54坐标");
        tmpList.add("西安80坐标");
        tmpList.add("2000国家大地坐标系");
        Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_toTranslateMethod, "2000国家大地坐标系", "请选择坐标系", tmpList, (String) null, this.pCallback);
        List<String> tmpList2 = new ArrayList<>();
        tmpList2.add("三参数");
        tmpList2.add("五参数");
        Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_GPSParaType, "三参数", "请选择计算参数", tmpList2, (String) null, this.pCallback);
        List<String> tmpList3 = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            tmpList3.add(String.valueOf(i * 3));
        }
        Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_GPSParaCenterLine, "120", "请选择中央经线", tmpList3, (String) null, this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("GPS")) {
                if (PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                    this.totalGPSCount = Integer.parseInt(Common.GetEditTextValueOnID(this._Dialog, R.id.editTextPara03));
                    if (this.totalGPSCount > 0) {
                        this.isThreadIsAlive = false;
                        this.lastGPSX = 0.0d;
                        this.lastGPSY = 0.0d;
                        this.lastTotalX = 0.0d;
                        this.lastTotalY = 0.0d;
                        this.currentGPSIndex = 0;
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
            } else if (command.equals("计算参数")) {
                String tmpGPSXStr = Common.GetEditTextValueOnID(this._Dialog, R.id.et_GPSPara_GPS_X);
                String tmpGPSYStr = Common.GetEditTextValueOnID(this._Dialog, R.id.et_GPSPara_GPS_Y);
                String tmpGPSZStr = Common.GetEditTextValueOnID(this._Dialog, R.id.et_GPSPara_GPS_Z);
                String tmpXStr = Common.GetEditTextValueOnID(this._Dialog, R.id.et_gpsParaCal_X);
                String tmpYStr = Common.GetEditTextValueOnID(this._Dialog, R.id.et_gpsParaCal_Y);
                String tmpZStr = Common.GetEditTextValueOnID(this._Dialog, R.id.et_gpsParaCal_Z);
                double tmpGPSX = Double.parseDouble(tmpGPSXStr);
                double tmpGPSY = Double.parseDouble(tmpGPSYStr);
                double tmpGPSZ = Double.parseDouble(tmpGPSZStr);
                double tmpX = Double.parseDouble(tmpXStr);
                double tmpY = Double.parseDouble(tmpYStr);
                double tmpZ = Double.parseDouble(tmpZStr);
                String tmpCoorSysName = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_toTranslateMethod);
                float tmpCenterLine = Float.parseFloat(Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_GPSParaCenterLine));
                AbstractC0383CoordinateSystem tmpCoorSystem = AbstractC0383CoordinateSystem.CreateCoordinateSystem(tmpCoorSysName);
                ((ProjectionCoordinateSystem) tmpCoorSystem).SetCenterMeridian(tmpCenterLine);
                StringBuilder tmpMsg = new StringBuilder();
                double[] tmpXYZ01 = Projection_XYZ.ConvertBLHToXYZ(tmpGPSX, tmpGPSY, tmpGPSZ, this.WGS1984);
                Coordinate tmpCoorXY = tmpCoorSystem.ConvertXYToBL(tmpX, tmpY);
                double[] tmpXYZ02 = Projection_XYZ.ConvertBLHToXYZ(tmpCoorXY.getGeoX(), tmpCoorXY.getGeoY(), tmpZ, tmpCoorSystem);
                final double tmpDelteX = tmpXYZ01[0] - tmpXYZ02[0];
                tmpMsg.append("DX:" + this.f466df.format(tmpDelteX) + "\r\n");
                final double tmpDelteY = tmpXYZ01[1] - tmpXYZ02[1];
                tmpMsg.append("DY:" + this.f466df.format(tmpDelteY) + "\r\n");
                final double tmpDelteZ = tmpXYZ01[2] - tmpXYZ02[2];
                tmpMsg.append("DZ:" + this.f466df.format(tmpDelteZ) + "\r\n");
                if (Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_GPSParaType).equals("五参数")) {
                    tmpMsg.append("DA:" + this.f466df.format(this.WGS1984.f462_A - tmpCoorSystem.f462_A) + "\r\n");
                    tmpMsg.append("DF:" + this.df2.format((1.0d / this.WGS1984.f464_f) - (1.0d / tmpCoorSystem.f464_f)) + "\r\n");
                }
                this.m_ResultEdit.setVisibility(0);
                this.m_ResultEdit.setText(tmpMsg.toString());
                Common.ShowYesNoDialog(this._Dialog.getContext(), "计算结果为:\r\n" + tmpMsg.toString() + "\r\n是否设置为当前项目的参数转换模型为三参数,参数采用以上计算结果?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.CoordinateSystem.GPSParaCalculate_Dialog.3
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command2, Object pObject) {
                        if (command2.equals("YES")) {
                            PMTranslate _PMTranslate = PubVar.m_Workspace.GetCoorSystem().GetPMTranslate();
                            _PMTranslate.SetPMCoorTransMethodName("三参转换");
                            _PMTranslate.SetTransToP31(String.valueOf(tmpDelteX));
                            _PMTranslate.SetTransToP32(String.valueOf(tmpDelteY));
                            _PMTranslate.SetTransToP33(String.valueOf(tmpDelteZ));
                            if (GPSParaCalculate_Dialog.this.m_Callback != null) {
                                GPSParaCalculate_Dialog.this.m_Callback.OnClick("平面参数转换返回", _PMTranslate);
                            }
                            PubVar._PubCommand.m_ProjectDB.GetProjectManage().SaveProjectInfo();
                            GPSParaCalculate_Dialog.this._Dialog.dismiss();
                        }
                    }
                });
            } else if (command.equals("调取")) {
                Coordinate tempCoord = PubVar._PubCommand.m_Measure.GetLastCoordinate();
                if (tempCoord == null) {
                    Common.ShowDialog("请先利用【测量】工具测量目标坐标点.");
                    return;
                }
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_gpsParaCal_X, String.valueOf(tempCoord.getX()));
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_gpsParaCal_Y, String.valueOf(tempCoord.getY()));
            }
        } catch (Exception e2) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.CoordinateSystem.GPSParaCalculate_Dialog.4
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
                GPSParaCalculate_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
