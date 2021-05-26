package  com.xzy.forestSystem.gogisapi.GPS;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.core.internal.view.SupportMenu;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import com.xzy.forestSystem.PubVar;
import com.stczh.gzforestSystem.R;

public class GPSSatelliteView extends View {
    private static final float radians = 0.017453292f;
    private int baselineY;
    private int cellHeight;
    private int cellSplitWidth;
    private int cellWidth;
    private int centerX;
    private int centerY;
    private int circleSize;
    private float compassAngle;
    private Bitmap compassBMP;
    private int compassHeight;
    private Path compassPath;
    private int compassWidth;
    private Bitmap gpsCHNBMP;
    private GPSDevice gpsDevice;
    private int gpsICONWidth;
    private Bitmap gpsRUSBMP;
    private Bitmap gpsUSABMP;
    private int heightp;
    private ImageView imgView;
    private boolean isInitialBool;
    int orgcircleSize;
    int orgtextSize;
    private int radius;
    private Paint strockpaint;
    public String textLine01;
    public String textLine02;
    public String textLine03;
    public String textLine04;
    private int textSize;
    private Paint txtPaint;
    private Paint txtPaint2;
    private int widthp;

    public GPSSatelliteView(Context context) {
        super(context);
        this.orgtextSize = 12;
        this.textSize = 12;
        this.orgcircleSize = 8;
        this.circleSize = 8;
        this.textLine01 = "正在等待定位...";
        this.textLine02 = "请稍后...";
        this.textLine03 = "精度:0m";
        this.textLine04 = "速度:0";
        this.gpsDevice = null;
        this.txtPaint = null;
        this.txtPaint2 = null;
        this.strockpaint = null;
        this.imgView = null;
        this.compassPath = null;
        this.compassWidth = 34;
        this.compassHeight = 311;
        this.compassBMP = null;
        this.compassAngle = 0.0f;
        this.gpsUSABMP = null;
        this.gpsCHNBMP = null;
        this.gpsRUSBMP = null;
        this.gpsICONWidth = 28;
        this.isInitialBool = false;
        this.heightp = getHeight();
        this.widthp = getWidth();
        this.cellWidth = this.widthp / 33;
        this.cellHeight = 30;
        this.baselineY = ((this.heightp * 2) / 3) + 30;
        this.textSize = (int) (((float) this.textSize) * 1.0f);
        this.circleSize = (int) (((float) this.circleSize) * 1.0f);
        this.strockpaint = new Paint();
        this.strockpaint.setAntiAlias(true);
        this.strockpaint.setStyle(Paint.Style.STROKE);
        this.strockpaint.setColor(-16776961);
        this.strockpaint.setPathEffect(new DashPathEffect(new float[]{5.0f, 5.0f, 5.0f, 5.0f}, 1.0f));
    }

    public GPSSatelliteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.orgtextSize = 12;
        this.textSize = 12;
        this.orgcircleSize = 8;
        this.circleSize = 8;
        this.textLine01 = "正在等待定位...";
        this.textLine02 = "请稍后...";
        this.textLine03 = "精度:0m";
        this.textLine04 = "速度:0";
        this.gpsDevice = null;
        this.txtPaint = null;
        this.txtPaint2 = null;
        this.strockpaint = null;
        this.imgView = null;
        this.compassPath = null;
        this.compassWidth = 34;
        this.compassHeight = 311;
        this.compassBMP = null;
        this.compassAngle = 0.0f;
        this.gpsUSABMP = null;
        this.gpsCHNBMP = null;
        this.gpsRUSBMP = null;
        this.gpsICONWidth = 28;
        this.isInitialBool = false;
    }

    public void UpdateGPSDevice(GPSDevice gpsDevice2) {
        this.gpsDevice = gpsDevice2;
        invalidate();
    }

    public void UpdateCompass(float compassAngle2) {
        this.compassAngle = compassAngle2;
        if (this.compassAngle < 0.0f) {
            this.compassAngle += 360.0f;
        }
        if (this.compassAngle > 360.0f) {
            this.compassAngle -= 360.0f;
        }
        invalidate();
    }

    private void isInitial() {
        try {
            if (!this.isInitialBool) {
                this.heightp = getHeight();
                this.widthp = getWidth();
                this.cellWidth = this.widthp / 20;
                this.cellSplitWidth = this.widthp / 100;
                float tempRatio = PubVar.ScaledDensity / 0.75f;
                this.textSize = (int) (((float) this.orgtextSize) * tempRatio);
                this.circleSize = (int) (((float) this.orgcircleSize) * tempRatio);
                int i = this.widthp / 2;
                this.centerX = i;
                this.radius = i;
                this.centerY = this.heightp / 3;
                if (this.centerX > this.centerY) {
                    this.radius = this.centerY;
                }
                this.centerY += this.textSize;
                this.baselineY = this.heightp - this.textSize;
                this.cellHeight = ((this.baselineY - this.centerY) - this.radius) - this.textSize;
                Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.compass);
                this.compassHeight = (int) (1.5f * ((float) this.radius));
                this.compassWidth = (int) (((float) this.compassHeight) * 0.11f);
                this.compassBMP = Bitmap.createScaledBitmap(bmp, this.compassWidth, this.compassHeight, true);
                this.gpsUSABMP = BitmapFactory.decodeResource(getResources(), R.drawable.gps_america);
                this.gpsCHNBMP = BitmapFactory.decodeResource(getResources(), R.drawable.gps_china);
                this.gpsRUSBMP = BitmapFactory.decodeResource(getResources(), R.drawable.gps_russia);
                this.gpsICONWidth = this.gpsUSABMP.getWidth() / 2;
                this.strockpaint = new Paint();
                this.strockpaint.setAntiAlias(true);
                this.strockpaint.setStyle(Paint.Style.STROKE);
                this.strockpaint.setColor(-1);
                this.strockpaint.setPathEffect(new DashPathEffect(new float[]{4.0f, 4.0f}, 1.0f));
                this.txtPaint = new Paint();
                this.txtPaint.setAntiAlias(true);
                this.txtPaint.setTextSize((float) this.textSize);
                this.txtPaint.setColor(-256);
                this.txtPaint.setStyle(Paint.Style.FILL);
                this.txtPaint2 = new Paint();
                this.txtPaint2.setAntiAlias(true);
                this.txtPaint2.setTextSize(((float) this.textSize) * 0.7f);
                this.txtPaint2.setColor(-256);
                this.txtPaint2.setStyle(Paint.Style.FILL);
                this.isInitialBool = false;
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint("ResourceAsColor")
    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        isInitial();
        try {
            canvas.drawColor(R.color.dialog_backColor);
            drawCompass(canvas);
            Paint paint = new Paint();
            paint.setColor(SupportMenu.CATEGORY_MASK);
            paint.setAntiAlias(true);
            paint.setTextSize((float) this.textSize);
            if (this.gpsDevice != null) {
                if (this.gpsDevice.satellites.size() > 12) {
                    this.cellWidth = this.widthp / 30;
                } else {
                    this.cellWidth = this.widthp / 20;
                }
                int tempTid = 1;
                for (SatelliteInfo sateInfo : this.gpsDevice.satellites) {
                    drawSatellite(canvas, sateInfo.Azimuth, sateInfo.Elevation, tempTid, sateInfo.f471ID, sateInfo.Snr);
                    tempTid++;
                }
                float tempLeft = (float) this.textSize;
                float f = ((float) this.baselineY) - (((float) this.cellHeight) * 0.2f);
                float tempY = ((float) this.baselineY) - (((float) this.cellHeight) * 0.2f);
                paint.setColor(getResources().getColor(R.color.gps_snr01_Color));
                canvas.drawRect(tempLeft, tempY, tempLeft + ((float) this.cellWidth), (float) this.baselineY, paint);
                float tempY2 = ((float) this.baselineY) - (((float) this.cellHeight) * 0.4f);
                paint.setColor(getResources().getColor(R.color.gps_snr02_Color));
                canvas.drawRect(tempLeft, tempY2, tempLeft + ((float) this.cellWidth), tempY, paint);
                float tempY3 = ((float) this.baselineY) - (((float) this.cellHeight) * 0.6f);
                paint.setColor(getResources().getColor(R.color.gps_snr03_Color));
                canvas.drawRect(tempLeft, tempY3, tempLeft + ((float) this.cellWidth), tempY2, paint);
                float tempY22 = ((float) this.baselineY) - (((float) this.cellHeight) * 0.8f);
                paint.setColor(getResources().getColor(R.color.gps_snr04_Color));
                canvas.drawRect(tempLeft, tempY22, tempLeft + ((float) this.cellWidth), tempY3, paint);
                float tempY4 = (float) (this.baselineY - this.cellHeight);
                paint.setColor(getResources().getColor(R.color.gps_snr05_Color));
                canvas.drawRect(tempLeft, tempY4, tempLeft + ((float) this.cellWidth), tempY22, paint);
                float tempY5 = ((float) this.baselineY) - (0.35f * ((float) this.textSize));
                canvas.drawText("10", 0.0f, tempY5 - (((float) this.cellHeight) * 0.1f), this.txtPaint2);
                canvas.drawText("20", 0.0f, tempY5 - (((float) this.cellHeight) * 0.3f), this.txtPaint2);
                canvas.drawText("30", 0.0f, tempY5 - (((float) this.cellHeight) * 0.5f), this.txtPaint2);
                canvas.drawText("40", 0.0f, tempY5 - (((float) this.cellHeight) * 0.7f), this.txtPaint2);
                canvas.drawText("50", 0.0f, tempY5 - (((float) this.cellHeight) * 0.9f), this.txtPaint2);
                canvas.drawLine(0.0f, (float) this.baselineY, (float) this.widthp, (float) this.baselineY, paint);
            }
        } catch (Exception e) {
        }
    }

    private void drawCompass(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.gps_canvas_Color));
        canvas.drawCircle((float) this.centerX, (float) this.centerY, (float) this.radius, paint);
        paint.setColor(-1);
        paint.setStyle(Paint.Style.STROKE);
        int tempY = this.centerY - this.radius;
        float tempSinX = (float) (this.radius / 2);
        float tempSinY = 0.866f * ((float) this.radius);
        if (this.compassPath == null) {
            this.compassPath = new Path();
            this.compassPath.moveTo((float) (this.centerX - this.radius), (float) this.centerY);
            this.compassPath.lineTo((float) (this.centerX + this.radius), (float) this.centerY);
            this.compassPath.moveTo((float) this.centerX, (float) tempY);
            this.compassPath.lineTo((float) this.centerX, (float) ((this.radius * 2) + tempY));
            this.compassPath.moveTo(((float) this.centerX) - tempSinX, ((float) this.centerY) - tempSinY);
            this.compassPath.lineTo(((float) this.centerX) + tempSinX, ((float) this.centerY) + tempSinY);
            this.compassPath.moveTo(((float) this.centerX) - tempSinX, ((float) this.centerY) + tempSinY);
            this.compassPath.lineTo(((float) this.centerX) + tempSinX, ((float) this.centerY) - tempSinY);
            this.compassPath.moveTo(((float) this.centerX) - tempSinY, ((float) this.centerY) - tempSinX);
            this.compassPath.lineTo(((float) this.centerX) + tempSinY, ((float) this.centerY) + tempSinX);
            this.compassPath.moveTo(((float) this.centerX) - tempSinY, ((float) this.centerY) + tempSinX);
            this.compassPath.lineTo(((float) this.centerX) + tempSinY, ((float) this.centerY) - tempSinX);
            this.compassPath.addCircle((float) this.centerX, (float) this.centerY, ((float) this.radius) * 0.866f, Path.Direction.CW);
            this.compassPath.addCircle((float) this.centerX, (float) this.centerY, (float) (this.radius / 2), Path.Direction.CW);
        }
        canvas.drawPath(this.compassPath, this.strockpaint);
        canvas.drawText("北", (float) (this.centerX - (this.textSize / 2)), (float) (this.centerY - this.radius), this.txtPaint);
        canvas.drawText("南", (float) (this.centerX - (this.textSize / 2)), (float) (this.centerY + this.radius + this.textSize), this.txtPaint);
        canvas.drawText("东", (float) (this.centerX + this.radius), (float) (this.centerY + (this.textSize / 2)), this.txtPaint);
        canvas.drawText("西", (float) ((this.centerX - this.radius) - this.textSize), (float) (this.centerY + (this.textSize / 2)), this.txtPaint);
        canvas.drawText("30°", ((float) this.centerX) + tempSinX, ((float) this.centerY) - tempSinY, this.txtPaint2);
        canvas.drawText("60°", ((float) this.centerX) + tempSinY, ((float) this.centerY) - tempSinX, this.txtPaint2);
        canvas.drawText("120°", ((float) this.centerX) + tempSinY, ((float) this.centerY) + tempSinX, this.txtPaint2);
        canvas.drawText("150°", ((float) this.centerX) + tempSinX, ((float) this.centerY) + tempSinY + (0.5f * ((float) this.textSize)), this.txtPaint2);
        canvas.drawText("210°", (((float) this.centerX) - tempSinX) - ((float) this.textSize), ((float) this.centerY) + tempSinY + (0.5f * ((float) this.textSize)), this.txtPaint2);
        canvas.drawText("240°", (((float) this.centerX) - tempSinY) - ((float) this.textSize), ((float) this.centerY) + tempSinX + (0.5f * ((float) this.textSize)), this.txtPaint2);
        canvas.drawText("300°", (((float) this.centerX) - tempSinY) - ((float) this.textSize), ((float) this.centerY) - tempSinX, this.txtPaint2);
        canvas.drawText("330°", (((float) this.centerX) - tempSinX) - ((float) this.textSize), ((float) this.centerY) - tempSinY, this.txtPaint2);
        Matrix m = new Matrix();
        m.postTranslate((float) ((-this.compassWidth) / 2), (float) ((-this.compassHeight) / 2));
        m.postRotate(360.0f - this.compassAngle);
        m.postTranslate((float) this.centerX, (float) this.centerY);
        canvas.drawBitmap(this.compassBMP, m, paint);
        paint.setTextSize((float) this.textSize);
        canvas.drawText("可见卫星数", 0.0f, (float) this.textSize, paint);
        canvas.drawText("解算卫星数", (float) (this.widthp - (this.textSize * 5)), (float) this.textSize, paint);
        paint.setTextSize(1.25f * ((float) this.textSize));
        String tempStr = String.valueOf(String.valueOf((int) this.compassAngle)) + "°";
        if (this.compassAngle >= 340.0f || this.compassAngle <= 20.0f) {
            tempStr = "北 " + tempStr;
        } else if (this.compassAngle > 20.0f && this.compassAngle < 70.0f) {
            tempStr = "东北 " + tempStr;
        } else if (this.compassAngle >= 70.0f && this.compassAngle <= 110.0f) {
            tempStr = "东 " + tempStr;
        } else if (this.compassAngle > 110.0f && this.compassAngle < 160.0f) {
            tempStr = "东南 " + tempStr;
        } else if (this.compassAngle >= 160.0f && this.compassAngle <= 200.0f) {
            tempStr = "南 " + tempStr;
        } else if (this.compassAngle > 200.0f && this.compassAngle < 250.0f) {
            tempStr = "西南 " + tempStr;
        } else if (this.compassAngle >= 250.0f && this.compassAngle <= 290.0f) {
            tempStr = "西 " + tempStr;
        } else if (this.compassAngle > 290.0f && this.compassAngle < 340.0f) {
            tempStr = "西北 " + tempStr;
        }
        canvas.drawText(tempStr, 0.0f, (float) (this.centerY + this.radius), paint);
        paint.setTextSize((float) (this.textSize * 2));
        String tempStr2 = "0颗";
        if (!(this.gpsDevice == null || this.gpsDevice.gpsLocation == null)) {
            tempStr2 = String.valueOf(this.gpsDevice.gpsLocation.satellitesCount) + "颗";
        }
        canvas.drawText(tempStr2, 0.0f, (float) (this.textSize * 3), paint);
        int tempY2 = this.widthp - (this.textSize * 4);
        if (!(this.gpsDevice == null || this.gpsDevice.gpsLocation == null)) {
            String tempStr3 = String.valueOf(this.gpsDevice.gpsLocation.usedSateCount) + "颗";
            if (this.gpsDevice.gpsLocation.usedSateCount >= 10) {
                tempY2 -= this.textSize;
            }
        }
        canvas.drawText(String.valueOf(this.gpsDevice.gpsLocation.usedSateCount) + "颗", (float) tempY2, (float) (this.textSize * 3), paint);
    }

    private void drawSatellite(Canvas canvas, float azimuth, float elevation, int index, int prn, float ratio) {
        String tempPNR = null;
        float tempR = (float) (((double) this.radius) * Math.cos((double) (radians * elevation)));
        float deltaX = (float) (((double) this.centerX) + (((double) tempR) * Math.sin((double) (radians * azimuth))));
        float deltaY = (float) (((double) this.centerY) - (((double) tempR) * Math.cos((double) (radians * azimuth))));
        Paint paint = new Paint();
        paint.setColor(-16711936);
        paint.setTextSize((float) this.textSize);
        float tempRatio = 1.0f;
        if (ratio < 50.0f) {
            tempRatio = ratio / 50.0f;
        }
        float tempR2 = ((float) this.circleSize) * tempRatio;
        if (prn < 35) {
//            tempPNR = NDEFRecord.URI_WELL_KNOWN_TYPE + prn;
            canvas.drawBitmap(this.gpsUSABMP, deltaX - ((float) this.gpsICONWidth), deltaY - ((float) this.gpsICONWidth), paint);
        } else if (prn < 100) {
            tempPNR = "R" + prn;
            canvas.drawBitmap(this.gpsRUSBMP, deltaX - ((float) this.gpsICONWidth), deltaY - ((float) this.gpsICONWidth), paint);
        } else {
            tempPNR = "C" + prn;
            canvas.drawBitmap(this.gpsCHNBMP, deltaX - ((float) this.gpsICONWidth), deltaY - ((float) this.gpsICONWidth), paint);
        }
        int tempColor = getResources().getColor(R.color.gps_snr01_Color);
        if (ratio >= 40.0f) {
            tempColor = getResources().getColor(R.color.gps_snr05_Color);
        } else if (ratio >= 30.0f) {
            tempColor = getResources().getColor(R.color.gps_snr04_Color);
        } else if (ratio >= 20.0f) {
            tempColor = getResources().getColor(R.color.gps_snr03_Color);
        } else if (ratio >= 10.0f) {
            tempColor = getResources().getColor(R.color.gps_snr02_Color);
        }
        paint.setColor(tempColor);
        float tempX = (float) ((this.cellWidth * index) + this.textSize + (this.cellSplitWidth * index));
        canvas.drawRect(tempX, ((float) this.baselineY) - (((float) this.cellHeight) * tempRatio), tempX + ((float) this.cellWidth), (float) this.baselineY, paint);
        if (prn >= 100) {
            paint.setTextScaleX(0.4f);
        } else if (prn >= 10) {
            paint.setTextScaleX(0.5f);
        }
        canvas.drawText(String.valueOf(prn), tempX, (float) (this.baselineY + this.textSize), paint);
        paint.setColor(-1);
        canvas.drawText(tempPNR, deltaX + tempR2, deltaY, paint);
    }

    private void drawGPSText(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(-256);
        paint.setTextSize((float) this.textSize);
        canvas.drawText(this.textLine01, 0.0f, (float) (this.baselineY + (this.textSize * 2)), paint);
        canvas.drawText(this.textLine02, 0.0f, (float) (this.baselineY + (this.textSize * 3)), paint);
        canvas.drawText(this.textLine03, 0.0f, (float) ((this.centerY + this.radius) - this.textSize), paint);
        canvas.drawText(this.textLine04, (float) (this.widthp - (this.textSize * 6)), (float) ((this.centerY + this.radius) - this.textSize), paint);
        paint.setTextSize((float) this.textSize);
        paint.setColor(-256);
    }
}
