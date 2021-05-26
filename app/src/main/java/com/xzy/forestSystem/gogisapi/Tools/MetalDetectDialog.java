package  com.xzy.forestSystem.gogisapi.Tools;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.SoundPool;
import android.os.Vibrator;
import androidx.core.internal.view.SupportMenu;
import android.widget.TextView;
import com.xzy.forestSystem.baidu.speech.easr.easrNativeJni;
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


public class MetalDetectDialog {
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
    private TextView m_TextView;
    private TextView m_TextViewX;
    private TextView m_TextViewY;
    private TextView m_TextViewZ;
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
    private SensorManager f559sm;
    private int totalCount;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public MetalDetectDialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.f559sm = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.MetalDetectDialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("对话框关闭事件") && MetalDetectDialog.this.f559sm != null && MetalDetectDialog.this.mySensorListener != null) {
                    MetalDetectDialog.this.f559sm.unregisterListener(MetalDetectDialog.this.mySensorListener);
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
        this.m_TextViewX = null;
        this.m_TextViewY = null;
        this.m_TextViewZ = null;
        this.m_DecimalFormat = null;
        this.m_DecimalFormat2 = null;
        this.mySoundPool = null;
        this.mySoundPMap = null;
        this.totalCount = 0;
        this.mySensorListener = new SensorEventListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.MetalDetectDialog.2
            @Override // android.hardware.SensorEventListener
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            @Override // android.hardware.SensorEventListener
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() != 1 && event.sensor.getType() == 2) {
                    MetalDetectDialog.this.magneticFieldValues = event.values;
                    float tmpAvg = (float) Math.sqrt((double) ((MetalDetectDialog.this.magneticFieldValues[0] * MetalDetectDialog.this.magneticFieldValues[0]) + (MetalDetectDialog.this.magneticFieldValues[1] * MetalDetectDialog.this.magneticFieldValues[1]) + (MetalDetectDialog.this.magneticFieldValues[2] * MetalDetectDialog.this.magneticFieldValues[2])));
                    MetalDetectDialog.this.m_TextView.setText(MetalDetectDialog.this.m_DecimalFormat.format((double) tmpAvg));
                    if (tmpAvg < 60.0f) {
                        MetalDetectDialog.this.m_TextView.setTextColor(-16711936);
                    } else if (tmpAvg < 80.0f) {
                        MetalDetectDialog.this.m_TextView.setTextColor(-16776961);
                    } else if (tmpAvg < 100.0f) {
                        MetalDetectDialog.this.m_TextView.setTextColor(-65281);
                    } else {
                        MetalDetectDialog.this.m_TextView.setTextColor(SupportMenu.CATEGORY_MASK);
                        MetalDetectDialog.this.playSound(1);
                    }
                    MetalDetectDialog.this.m_TextViewX.setText("X:" + MetalDetectDialog.this.m_DecimalFormat2.format((double) MetalDetectDialog.this.magneticFieldValues[0]));
                    MetalDetectDialog.this.m_TextViewY.setText("Y:" + MetalDetectDialog.this.m_DecimalFormat2.format((double) MetalDetectDialog.this.magneticFieldValues[1]));
                    MetalDetectDialog.this.m_TextViewZ.setText("Z:" + MetalDetectDialog.this.m_DecimalFormat2.format((double) MetalDetectDialog.this.magneticFieldValues[2]));
                    int tmpCount = MetalDetectDialog.this.data.getLines().get(0).getValues().size();
                    if (tmpCount > MetalDetectDialog.this.numberOfPoints) {
                        MetalDetectDialog.this.data.getLines().get(0).getValues().remove(0);
                        int tmpCount2 = tmpCount - 1;
                    }
                    MetalDetectDialog.this.data.getLines().get(0).getValues().add(new PointValue((float) MetalDetectDialog.this.totalCount, tmpAvg));
                    MetalDetectDialog.this.totalCount++;
                    MetalDetectDialog.this.resetViewport2(MetalDetectDialog.this.totalCount - MetalDetectDialog.this.numberOfPoints, MetalDetectDialog.this.totalCount);
                    MetalDetectDialog.this.chart.setLineChartData(MetalDetectDialog.this.data);
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.metaldetect_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("磁场感应");
        this.m_TextView = (TextView) this._Dialog.findViewById(R.id.tv_magValue);
        this.m_TextView.setText("0");
        this.m_DecimalFormat = new DecimalFormat("#");
        this.m_DecimalFormat2 = new DecimalFormat("0.0");
        this.m_TextViewX = (TextView) this._Dialog.findViewById(R.id.tv_magValueX);
        this.m_TextViewY = (TextView) this._Dialog.findViewById(R.id.tv_magValueY);
        this.m_TextViewZ = (TextView) this._Dialog.findViewById(R.id.tv_magValueZ);
        this.f559sm = (SensorManager) this._Dialog.getContext().getSystemService("sensor");
        this.chart = (LineChartView) this._Dialog.findViewById(R.id.chart);
        generateData();
        this.chart.setViewportCalculationEnabled(false);
        resetViewport();
    }

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
    private void resetViewport2(int startX, int endX) {
        Viewport v = new Viewport(this.chart.getMaximumViewport());
        v.bottom = 0.0f;
        v.top = 200.0f;
        v.left = (float) startX;
        v.right = (float) endX;
        this.chart.setMaximumViewport(v);
        this.chart.setCurrentViewport(v);
    }

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
                axisY.setName("磁力值(uT)");
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
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.MetalDetectDialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                if (MetalDetectDialog.this.f559sm.getDefaultSensor(2) == null) {
                    Common.ShowDialog("很抱歉,当前使用的设备中【磁场传感器】不可用!无法使用本功能!");
                    MetalDetectDialog.this._Dialog.dismiss();
                    return;
                }
                MetalDetectDialog.this.f559sm.registerListener(MetalDetectDialog.this.mySensorListener, MetalDetectDialog.this.f559sm.getDefaultSensor(2), 3);
                MetalDetectDialog.this._Dialog.SetDismissCallback(MetalDetectDialog.this.pCallback);
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    @SuppressLint("WrongConstant")
    private void playSound(int index) {
        try {
            if (this.mySoundPool == null) {
                this.mySoundPool = new SoundPool(1, 1, 0);
                this.mySoundPMap = new HashMap<>();
                this.mySoundPMap.put(1, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.bbmsg2, 1)));
            }
            this.mySoundPool.play(this.mySoundPMap.get(Integer.valueOf(index)).intValue(), 1.0f, 1.0f, 0, 0, 1.0f);
            if (this.myVibrator == null) {
                this.myVibrator = (Vibrator) this._Dialog.getContext().getSystemService("vibrator");
            }
            this.myVibrator.vibrate((long) (index * easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX));
        } catch (Exception e) {
        }
    }
}
