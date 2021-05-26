package  com.xzy.forestSystem.gogisapi.GPS;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.ImageView;

public class CompassClass {

    /* renamed from: R */
    float[] f469R = new float[9];
    public float XAngle = 0.0f;
    public float YAngle = 0.0f;
    public float ZAngle = 0.0f;
    private Sensor aSensor = null;
    float[] accelerometerValues = new float[3];
    private ImageView compassImageView = null;
    private Sensor mSensor = null;
    float[] magneticFieldValues = new float[3];
    final SensorEventListener myListener = new SensorEventListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.CompassClass.1
        private float lastDegree = 0.0f;

        @Override // android.hardware.SensorEventListener
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override // android.hardware.SensorEventListener
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == 1) {
                CompassClass.this.accelerometerValues = event.values;
            } else if (event.sensor.getType() == 2) {
                CompassClass.this.magneticFieldValues = event.values;
            } else if (event.sensor.getType() == 3) {
                float degree = event.values[0];
                CompassClass.this.ZAngle = degree;
                CompassClass.this.XAngle = event.values[1];
                CompassClass.this.YAngle = event.values[2];
                float degree2 = (float) ((int) degree);
                if (this.lastDegree != degree2) {
                    if (CompassClass.this.compassImageView != null) {
                        CompassClass.this.compassImageView.setRotation(-degree2);
                    }
                    this.lastDegree = degree2;
                }
            }
        }
    };

    /* renamed from: sm */
    private SensorManager f470sm = null;
    float[] values = new float[3];

    public CompassClass(Context context) {
        this.f470sm = (SensorManager) context.getSystemService("sensor");
        this.aSensor = this.f470sm.getDefaultSensor(1);
        this.mSensor = this.f470sm.getDefaultSensor(2);
        this.f470sm.registerListener(this.myListener, this.f470sm.getDefaultSensor(3), 0);
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        this.f470sm.unregisterListener(this.myListener);
    }

    public void setCompassImageView(ImageView compassImageView2) {
        this.compassImageView = compassImageView2;
    }
}
