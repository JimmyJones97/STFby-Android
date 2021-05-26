package  com.xzy.forestSystem.gogisapi.Carto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.text.TextPaint;

import androidx.core.internal.view.SupportMenu;

import com.xzy.forestSystem.PubVar;

public class MapCompassPaint implements IOnPaint {
    private Bitmap _MaskBitmap = null;
    private MapView _mapView;
    private float m_Angle = 0.0f;
    private Paint m_BubblePaint = null;
    private float m_BubbleRadius = 0.0f;
    private float m_Height = 0.0f;
    private float m_Height2 = 0.0f;
    private float m_Radius = 0.0f;
    private Paint m_TxtFont = null;
    private float m_Width = 0.0f;
    private float m_Width2 = 0.0f;
    private Paint m_linePaint = null;
    private double m_pitchAngle = 0.0d;
    private double m_rollAngle = 0.0d;

    public MapCompassPaint(MapView mapView) {
        this._mapView = mapView;
        this.m_linePaint = new Paint();
        this.m_linePaint.setStyle(Paint.Style.STROKE);
        this.m_linePaint.setColor(-13523093);
        this.m_linePaint.setStrokeWidth((float) ((int) PubVar.ScaledDensity));
        this.m_linePaint.setAntiAlias(true);
        this.m_BubblePaint = new Paint();
        this.m_BubblePaint.setColor(805371648);
        this.m_BubblePaint.setStyle(Paint.Style.FILL);
        this.m_BubblePaint.setAntiAlias(true);
        this.m_TxtFont = new TextPaint();
        this.m_TxtFont.setColor(SupportMenu.CATEGORY_MASK);
        this.m_TxtFont.setAntiAlias(true);
        this.m_TxtFont.setTextSize(12.0f * PubVar.ScaledDensity);
        this.m_TxtFont.setTypeface(Typeface.create("宋体", 0));
    }

    public void setAngle(float angle) {
        this.m_Angle = angle;
    }

    public void setRollPitchAngle(double rollAngle, double pitchAngle) {
        this.m_rollAngle = rollAngle;
        this.m_pitchAngle = pitchAngle;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas paramCanvas) {
        try {
            if (PubVar.m_MapCompassVisible) {
                if (this.m_Width == 0.0f || this.m_Height == 0.0f) {
                    this.m_Width = (float) this._mapView.getMap().getSize().getWidth();
                    this.m_Height = (float) this._mapView.getMap().getSize().getHeight();
                    if (this.m_Width > this.m_Height) {
                        this.m_Radius = this.m_Height * 0.3f;
                    } else {
                        this.m_Radius = this.m_Width * 0.3f;
                    }
                    this.m_BubbleRadius = this.m_Radius / 2.0f;
                    this.m_Height2 = this.m_Height / 2.0f;
                    this.m_Width2 = this.m_Width / 2.0f;
                }
                if (this.m_Width != 0.0f && this.m_Height != 0.0f) {
                    paramCanvas.drawLine(0.0f, this.m_Height2, this.m_Width, this.m_Height2, this.m_linePaint);
                    paramCanvas.drawLine(this.m_Width2, 0.0f, this.m_Width2, this.m_Height, this.m_linePaint);
                    paramCanvas.drawCircle(this.m_Width2, this.m_Height2, this.m_BubbleRadius, this.m_linePaint);
                    PointF tmpPtnF = convertBubbleCoordinate(this.m_rollAngle, this.m_pitchAngle, (double) this.m_BubbleRadius);
                    paramCanvas.drawCircle(tmpPtnF.x, tmpPtnF.y, this.m_BubbleRadius, this.m_BubblePaint);
                    Bitmap tmpBitmap = getMaskBitmap();
                    if (tmpBitmap != null) {
                        paramCanvas.save();
                        paramCanvas.translate(this.m_Width2, this.m_Height2);
                        paramCanvas.rotate(-this.m_Angle);
                        paramCanvas.drawBitmap(tmpBitmap, -this.m_Radius, -this.m_Radius, this.m_linePaint);
                        paramCanvas.restore();
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private Bitmap getMaskBitmap() {
        try {
            if (this._MaskBitmap == null && this.m_Radius != 0.0f) {
                this._MaskBitmap = Bitmap.createBitmap(((int) this.m_Radius) * 2, ((int) this.m_Radius) * 2, Bitmap.Config.ARGB_4444);
            }
            if (this._MaskBitmap != null) {
                this._MaskBitmap.eraseColor(0);
                float tmpWidth01 = 3.0f * PubVar.ScaledDensity;
                float tmpHeight01 = 10.0f * PubVar.ScaledDensity;
                Paint tmpLinePaint = new Paint();
                tmpLinePaint.setStyle(Paint.Style.STROKE);
                tmpLinePaint.setColor(-13523093);
                tmpLinePaint.setStrokeWidth((float) ((int) PubVar.ScaledDensity));
                tmpLinePaint.setAntiAlias(true);
                Paint tmpLinePaint2 = new Paint();
                tmpLinePaint2.setStyle(Paint.Style.FILL);
                tmpLinePaint2.setColor(-13523093);
                tmpLinePaint2.setStrokeWidth((float) ((int) tmpWidth01));
                tmpLinePaint2.setAntiAlias(true);
                float tmpCenterX = this.m_Radius;
                Canvas paramCanvas = new Canvas(this._MaskBitmap);
                this.m_TxtFont.setTextSize(12.0f * PubVar.ScaledDensity);
                float textheight = this.m_TxtFont.measureText("东");
                float tmpSpaceHeight = 3.0f * PubVar.ScaledDensity;
                paramCanvas.drawCircle(tmpCenterX, tmpCenterX, (tmpCenterX - textheight) - tmpSpaceHeight, tmpLinePaint);
                paramCanvas.translate(tmpCenterX, tmpCenterX);
                float tmpX = (-textheight) / 2.0f;
                float tmpY = (-tmpCenterX) + textheight;
                paramCanvas.drawLine(0.0f, tmpY - tmpSpaceHeight, 0.0f, (tmpY - tmpSpaceHeight) + tmpHeight01, tmpLinePaint2);
                paramCanvas.drawText("北", tmpX, tmpY, this.m_TxtFont);
                paramCanvas.rotate(30.0f);
                paramCanvas.drawLine(0.0f, tmpY - tmpSpaceHeight, 0.0f, (tmpY - tmpSpaceHeight) + tmpHeight01, tmpLinePaint2);
                paramCanvas.rotate(30.0f);
                paramCanvas.drawLine(0.0f, tmpY - tmpSpaceHeight, 0.0f, (tmpY - tmpSpaceHeight) + tmpHeight01, tmpLinePaint2);
                paramCanvas.rotate(30.0f);
                paramCanvas.drawLine(0.0f, tmpY - tmpSpaceHeight, 0.0f, (tmpY - tmpSpaceHeight) + tmpHeight01, tmpLinePaint2);
                paramCanvas.drawText("东", tmpX, tmpY, this.m_TxtFont);
                paramCanvas.rotate(30.0f);
                paramCanvas.drawLine(0.0f, tmpY - tmpSpaceHeight, 0.0f, (tmpY - tmpSpaceHeight) + tmpHeight01, tmpLinePaint2);
                paramCanvas.rotate(30.0f);
                paramCanvas.drawLine(0.0f, tmpY - tmpSpaceHeight, 0.0f, (tmpY - tmpSpaceHeight) + tmpHeight01, tmpLinePaint2);
                paramCanvas.rotate(30.0f);
                paramCanvas.drawLine(0.0f, tmpY - tmpSpaceHeight, 0.0f, (tmpY - tmpSpaceHeight) + tmpHeight01, tmpLinePaint2);
                paramCanvas.drawText("南", tmpX, tmpY, this.m_TxtFont);
                paramCanvas.rotate(30.0f);
                paramCanvas.drawLine(0.0f, tmpY - tmpSpaceHeight, 0.0f, (tmpY - tmpSpaceHeight) + tmpHeight01, tmpLinePaint2);
                paramCanvas.rotate(30.0f);
                paramCanvas.drawLine(0.0f, tmpY - tmpSpaceHeight, 0.0f, (tmpY - tmpSpaceHeight) + tmpHeight01, tmpLinePaint2);
                paramCanvas.rotate(30.0f);
                paramCanvas.drawLine(0.0f, tmpY - tmpSpaceHeight, 0.0f, (tmpY - tmpSpaceHeight) + tmpHeight01, tmpLinePaint2);
                paramCanvas.drawText("西", tmpX, tmpY, this.m_TxtFont);
                paramCanvas.rotate(30.0f);
                paramCanvas.drawLine(0.0f, tmpY - tmpSpaceHeight, 0.0f, (tmpY - tmpSpaceHeight) + tmpHeight01, tmpLinePaint2);
                paramCanvas.rotate(30.0f);
                paramCanvas.drawLine(0.0f, tmpY - tmpSpaceHeight, 0.0f, (tmpY - tmpSpaceHeight) + tmpHeight01, tmpLinePaint2);
            }
        } catch (Exception e) {
        }
        return this._MaskBitmap;
    }

    private PointF convertBubbleCoordinate(double rollAngle, double pitchAngle, double radius) {
        double scale = radius / Math.toRadians(90.0d);
        double y0 = -(pitchAngle * scale);
        return new PointF((float) (((double) this.m_Width2) - (-(rollAngle * scale))), (float) (((double) this.m_Height2) - y0));
    }
}
