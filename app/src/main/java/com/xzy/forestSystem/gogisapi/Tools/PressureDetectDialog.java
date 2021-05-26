package  com.xzy.forestSystem.gogisapi.Tools;

import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.SoundPool;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import com.xzy.forestSystem.hellocharts.model.Axis;
import com.xzy.forestSystem.hellocharts.model.Line;
import com.xzy.forestSystem.hellocharts.model.LineChartData;
import com.xzy.forestSystem.hellocharts.model.PointValue;
import com.xzy.forestSystem.hellocharts.model.ValueShape;
import com.xzy.forestSystem.hellocharts.model.Viewport;
import com.xzy.forestSystem.hellocharts.util.ChartUtils;
import com.xzy.forestSystem.hellocharts.view.LineChartView;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PressureDetectDialog {
    private int UnitType;
    private XDialogTemplate _Dialog;
    private LineChartView chart;
    private LineChartData data;
    private boolean hasAxes;
    private boolean hasAxesNames;
    private boolean hasLabelForSelected;
    private boolean hasLabels;
    private boolean hasLines;
    private boolean hasPoints;
    private boolean isCubic;
    private boolean isFilled;
    private ICallback m_Callback;
    private DecimalFormat m_DecimalFormat;
    private DecimalFormat m_DecimalFormat2;
    private float m_MaxY;
    private float m_MinY;
    private TextView m_TextView;
    private TextView m_TextViewH;
    private TextView m_TextViewUnit;
    private float[] magneticFieldValues;
    private int maxNumberOfLines;
    final SensorEventListener mySensorListener;
    private HashMap<Integer, Integer> mySoundPMap;
    private SoundPool mySoundPool;
    private Vibrator myVibrator;
    private int numberOfLines;
    private int numberOfPoints;
    private ICallback pCallback;
    private boolean pointsHaveDifferentColor;
    float[][] randomNumbersTab;
    private ValueShape shape;

    /* renamed from: sm */
    private SensorManager f560sm;
    private int totalCount;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public PressureDetectDialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.f560sm = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.PressureDetectDialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("对话框关闭事件") && PressureDetectDialog.this.f560sm != null && PressureDetectDialog.this.mySensorListener != null) {
                    PressureDetectDialog.this.f560sm.unregisterListener(PressureDetectDialog.this.mySensorListener);
                }
            }
        };
        this.numberOfLines = 1;
        this.maxNumberOfLines = 1;
        this.numberOfPoints = 30;
        this.randomNumbersTab = (float[][]) Array.newInstance(Float.TYPE, this.maxNumberOfLines, this.numberOfPoints);
        this.hasAxes = true;
        this.hasAxesNames = true;
        this.hasLines = true;
        this.hasPoints = false;
        this.shape = ValueShape.CIRCLE;
        this.isFilled = false;
        this.hasLabels = false;
        this.isCubic = false;
        this.hasLabelForSelected = false;
        this.magneticFieldValues = null;
        this.m_TextView = null;
        this.m_TextViewH = null;
        this.m_TextViewUnit = null;
        this.m_DecimalFormat = null;
        this.m_DecimalFormat2 = null;
        this.mySoundPool = null;
        this.mySoundPMap = null;
        this.UnitType = 0;
        this.totalCount = 0;
        this.m_MinY = Float.MAX_VALUE;
        this.m_MaxY = Float.MIN_VALUE;
        this.mySensorListener = new SensorEventListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.PressureDetectDialog.2
            @Override // android.hardware.SensorEventListener
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            @Override // android.hardware.SensorEventListener
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == 6) {
                    float tmpSPV = event.values[0];
                    float sPV = event.values[0];
                    if (PressureDetectDialog.this.m_MinY > tmpSPV) {
                        PressureDetectDialog.this.m_MinY = (float) (((int) tmpSPV) - 1);
                    }
                    if (PressureDetectDialog.this.m_MaxY < tmpSPV) {
                        PressureDetectDialog.this.m_MaxY = (float) (((int) tmpSPV) + 1);
                    }
                    double height = 4.433E7d * (1.0d - Math.pow(((double) sPV) / 1013.25d, 1.9029495718363463E-4d));
                    if (PressureDetectDialog.this.UnitType == 0) {
                        PressureDetectDialog.this.m_TextView.setText(PressureDetectDialog.this.m_DecimalFormat.format((double) sPV));
                        PressureDetectDialog.this.m_TextViewUnit.setText("hPa");
                    } else if (PressureDetectDialog.this.UnitType == 1) {
                        PressureDetectDialog.this.m_TextView.setText(PressureDetectDialog.this.m_DecimalFormat.format((double) (sPV / 10.0f)));
                        PressureDetectDialog.this.m_TextViewUnit.setText("kPa");
                    } else if (PressureDetectDialog.this.UnitType == 2) {
                        PressureDetectDialog.this.m_TextView.setText(PressureDetectDialog.this.m_DecimalFormat.format((double) (sPV / 1000.0f)));
                        PressureDetectDialog.this.m_TextViewUnit.setText("bar");
                    } else if (PressureDetectDialog.this.UnitType == 3) {
                        PressureDetectDialog.this.m_TextView.setText(PressureDetectDialog.this.m_DecimalFormat.format((double) ((14.5f * sPV) / 1000.0f)));
                        PressureDetectDialog.this.m_TextViewUnit.setText("psi");
                    } else if (PressureDetectDialog.this.UnitType == 4) {
                        PressureDetectDialog.this.m_TextView.setText(PressureDetectDialog.this.m_DecimalFormat.format((double) ((750.0f * sPV) / 1000.0f)));
                        PressureDetectDialog.this.m_TextViewUnit.setText("mmHg");
                    }
                    PressureDetectDialog.this.m_TextViewH.setText(PressureDetectDialog.this.m_DecimalFormat.format(height));
                    int tmpCount = PressureDetectDialog.this.data.getLines().get(0).getValues().size();
                    if (tmpCount > PressureDetectDialog.this.numberOfPoints) {
                        PressureDetectDialog.this.data.getLines().get(0).getValues().remove(0);
                        int tmpCount2 = tmpCount - 1;
                    }
                    PressureDetectDialog.this.data.getLines().get(0).getValues().add(new PointValue((float) PressureDetectDialog.this.totalCount, tmpSPV));
                    PressureDetectDialog.this.totalCount++;
                    PressureDetectDialog.this.resetViewport(PressureDetectDialog.this.totalCount - PressureDetectDialog.this.numberOfPoints, PressureDetectDialog.this.totalCount, PressureDetectDialog.this.m_MinY, PressureDetectDialog.this.m_MaxY);
                    PressureDetectDialog.this.chart.setLineChartData(PressureDetectDialog.this.data);
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.pressuredetect_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("气压监测");
        this.m_TextView = (TextView) this._Dialog.findViewById(R.id.tv_pressureValue);
        this.m_TextView.setText("0");
        this.m_DecimalFormat = new DecimalFormat("0.0");
        this.m_TextViewH = (TextView) this._Dialog.findViewById(R.id.tv_elevationValue);
        this.m_TextViewUnit = (TextView) this._Dialog.findViewById(R.id.tv_pressureUnit);
        this.chart = (LineChartView) this._Dialog.findViewById(R.id.chart);
        this.m_TextView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.PressureDetectDialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                PressureDetectDialog.this.UnitType++;
                if (PressureDetectDialog.this.UnitType > 4) {
                    PressureDetectDialog.this.UnitType = 0;
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void resetViewport() {
        Viewport v = new Viewport(this.chart.getMaximumViewport());
        v.bottom = 0.0f;
        v.top = 200.0f;
        v.left = 0.0f;
        v.right = (float) (this.numberOfPoints - 1);
        this.chart.setMaximumViewport(v);
        this.chart.setCurrentViewport(v);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void resetViewport(int startX, int endX, float minY, float maxY) {
        Viewport v = new Viewport(this.chart.getMaximumViewport());
        v.bottom = minY;
        v.top = maxY;
        v.left = (float) startX;
        v.right = (float) endX;
        this.chart.setMaximumViewport(v);
        this.chart.setCurrentViewport(v);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void generateData() {
        List<Line> lines = new ArrayList<>();
        for (int i = 0; i < this.numberOfLines; i++) {
            Line line = new Line(new ArrayList<>());
            line.setColor(ChartUtils.COLORS[i]);
            line.setShape(this.shape);
            line.setCubic(this.isCubic);
            line.setFilled(this.isFilled);
            line.setHasLabels(this.hasLabels);
            line.setHasLabelsOnlyForSelected(this.hasLabelForSelected);
            line.setHasLines(this.hasLines);
            line.setHasPoints(this.hasPoints);
            if (this.pointsHaveDifferentColor) {
                line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
            }
            lines.add(line);
        }
        this.data = new LineChartData(lines);
        if (this.hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (this.hasAxesNames) {
                axisX.setName("时间(s)");
                axisY.setName("压力值(hPa)");
            }
            this.data.setAxisYLeft(axisY);
        } else {
            this.data.setAxisXBottom(null);
            this.data.setAxisYLeft(null);
        }
        this.data.setBaseValue(Float.NEGATIVE_INFINITY);
        this.chart.setLineChartData(this.data);
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.PressureDetectDialog.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                PressureDetectDialog.this.f560sm = (SensorManager) PressureDetectDialog.this._Dialog.getContext().getSystemService("sensor");
                if (PressureDetectDialog.this.f560sm.getDefaultSensor(6) == null) {
                    Common.ShowDialog("很抱歉,当前使用的设备中【气压传感器】不可用!无法使用本功能!");
                    PressureDetectDialog.this._Dialog.dismiss();
                    return;
                }
                PressureDetectDialog.this.f560sm.registerListener(PressureDetectDialog.this.mySensorListener, PressureDetectDialog.this.f560sm.getDefaultSensor(6), 3);
                PressureDetectDialog.this.generateData();
                PressureDetectDialog.this.chart.setViewportCalculationEnabled(false);
                PressureDetectDialog.this.resetViewport();
                PressureDetectDialog.this._Dialog.SetDismissCallback(PressureDetectDialog.this.pCallback);
            }
        });
        this._Dialog.show();
    }
}
