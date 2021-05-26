package  com.xzy.forestSystem.gogisapi.XProject;

import android.os.Handler;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.List;

public class CheckControlPtnCal_Dialog {
    private XDialogTemplate _Dialog;
    private int currentGPSIndex;
    private boolean isThreadIsAlive;
    private double lastGPSX;
    private double lastGPSY;
    private double lastTotalX;
    private double lastTotalY;
    private ICallback m_Callback;
    private Runnable myTask;
    private Handler myhandler;
    private ICallback pCallback;
    private int totalGPSCount;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public CheckControlPtnCal_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CheckControlPtnCal_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                try {
                    if (paramString.equals("确定")) {
                        String tempPara01 = Common.GetEditTextValueOnID(CheckControlPtnCal_Dialog.this._Dialog, R.id.editTextPara01);
                        String tempPara02 = Common.GetEditTextValueOnID(CheckControlPtnCal_Dialog.this._Dialog, R.id.editTextPara02);
                        String tempPara03 = Common.GetEditTextValueOnID(CheckControlPtnCal_Dialog.this._Dialog, R.id.editTextPara04);
                        String tempPara04 = Common.GetEditTextValueOnID(CheckControlPtnCal_Dialog.this._Dialog, R.id.editTextPara05);
                        double tempX0 = Double.parseDouble(tempPara01);
                        double tempY0 = Double.parseDouble(tempPara02);
                        final double tempTransX = Double.parseDouble(tempPara03) - tempX0;
                        final double tempTransY = Double.parseDouble(tempPara04) - tempY0;
                        Common.ShowYesNoDialog(CheckControlPtnCal_Dialog.this._Dialog.getContext(), "校准点误差值如下,是否确认转换参数?\r\nX平移(m):" + tempTransX + "\r\nY平移(m):" + tempTransY, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CheckControlPtnCal_Dialog.1.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject2) {
                                if (paramString2.equals("YES")) {
                                    if (CheckControlPtnCal_Dialog.this.m_Callback != null) {
                                        CheckControlPtnCal_Dialog.this.m_Callback.OnClick("校准点", new double[]{tempTransX, tempTransY});
                                    }
                                    CheckControlPtnCal_Dialog.this._Dialog.dismiss();
                                }
                            }
                        });
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
        this.lastTotalX = 0.0d;
        this.lastTotalY = 0.0d;
        this.isThreadIsAlive = false;
        this.myTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.XProject.CheckControlPtnCal_Dialog.2
            @Override // java.lang.Runnable
            public void run() {
                if (CheckControlPtnCal_Dialog.this.isThreadIsAlive) {
                    double tempX = PubVar._PubCommand.m_GpsLocation.longtitude;
                    double tempY = PubVar._PubCommand.m_GpsLocation.latitude;
                    double d = PubVar._PubCommand.m_GpsLocation.elevation;
                    if (!(CheckControlPtnCal_Dialog.this.lastGPSX == tempX && CheckControlPtnCal_Dialog.this.lastGPSY == tempY)) {
                        Coordinate tempCoord = PubVar._PubCommand.m_GpsLocation.getGPSCoordinate();
                        CheckControlPtnCal_Dialog.this.lastTotalX += tempCoord.getX();
                        CheckControlPtnCal_Dialog.this.lastTotalY += tempCoord.getY();
                        CheckControlPtnCal_Dialog.this.lastGPSX = tempX;
                        CheckControlPtnCal_Dialog.this.lastGPSY = tempY;
                        CheckControlPtnCal_Dialog.this.currentGPSIndex++;
                        Common.SetEditTextValueOnID(CheckControlPtnCal_Dialog.this._Dialog, R.id.editTextPara01, String.valueOf(CheckControlPtnCal_Dialog.this.lastTotalX / ((double) CheckControlPtnCal_Dialog.this.currentGPSIndex)));
                        Common.SetEditTextValueOnID(CheckControlPtnCal_Dialog.this._Dialog, R.id.editTextPara02, String.valueOf(CheckControlPtnCal_Dialog.this.lastTotalY / ((double) CheckControlPtnCal_Dialog.this.currentGPSIndex)));
                    }
                    if (CheckControlPtnCal_Dialog.this.currentGPSIndex < CheckControlPtnCal_Dialog.this.totalGPSCount) {
                        CheckControlPtnCal_Dialog.this.myhandler.postDelayed(CheckControlPtnCal_Dialog.this.myTask, 1000);
                    } else {
                        CheckControlPtnCal_Dialog.this.isThreadIsAlive = false;
                    }
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.checkpointcal_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("校准点");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.buttonCheckBtn01).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonCheckBtn02).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonCheckPtnFromLine).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonCheckPtnExchange).setOnClickListener(new ViewClick());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
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
        } else if (command.equals("调取")) {
            Coordinate tempCoord = PubVar._PubCommand.m_Measure.GetLastCoordinate();
            if (tempCoord == null) {
                Common.ShowDialog("请先利用【测量】工具测量目标坐标点.");
                return;
            }
            Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara04, String.valueOf(tempCoord.getX()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara05, String.valueOf(tempCoord.getY()));
        } else if (command.equals("从测量直线获取")) {
            List<Coordinate> tempCoords = PubVar._PubCommand.m_Measure.GetLastCoordinates(2);
            if (tempCoords == null || tempCoords.size() < 2) {
                Common.ShowDialog("没有绘制测量的直线,请先利用【测量】工具测量直线.");
                return;
            }
            Coordinate tempCoord2 = tempCoords.get(0);
            Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara01, String.valueOf(tempCoord2.getX()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara02, String.valueOf(tempCoord2.getY()));
            Coordinate tempCoord3 = tempCoords.get(1);
            Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara04, String.valueOf(tempCoord3.getX()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara05, String.valueOf(tempCoord3.getY()));
        } else if (command.equals("交换校正点坐标")) {
            String tempStr1 = Common.GetEditTextValueOnID(this._Dialog, R.id.editTextPara01);
            Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara01, Common.GetEditTextValueOnID(this._Dialog, R.id.editTextPara04));
            Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara04, tempStr1);
            String tempStr12 = Common.GetEditTextValueOnID(this._Dialog, R.id.editTextPara02);
            Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara02, Common.GetEditTextValueOnID(this._Dialog, R.id.editTextPara05));
            Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara05, tempStr12);
            Common.ShowToast("交换坐标数据完成.");
        }
    }

    public void ShowDialog() {
        this._Dialog.show();
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                CheckControlPtnCal_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
