package  com.xzy.forestSystem.gogisapi.GPS;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.IOnPaint;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import com.stczh.gzforestSystem.R;

public class GPSDisplay implements IOnPaint {
    private Point LastDrawPtn = new Point(-100, -100);
    private Bitmap _GPSPointDirICON = null;
    private Bitmap _GpsPointICON = null;
    private Paint _TextFont = null;
    private Paint _scopePaint = null;
    private GPSLocationClass m_GPSLocation = null;
    private MapView m_mapView = null;

    public GPSDisplay(MapView mapView) {
        this.m_mapView = mapView;
        this.m_mapView._GPSDisplayPaint = this;
        this._scopePaint = new Paint();
        this._scopePaint.setAntiAlias(true);
    }

    private Paint GetTextFont() {
        if (this._TextFont == null) {
            this._TextFont = new Paint();
            this._TextFont.setAntiAlias(true);
            this._TextFont.setTextSize(10.0f * PubVar.ScaledDensity);
            this._TextFont.setColor(-16776961);
            this._TextFont.setTypeface(Typeface.create("宋体", 1));
            this._TextFont.setShadowLayer(20.0f, 0.0f, 0.0f, -1);
        }
        return this._TextFont;
    }

    private void drawScope(Canvas canvas, float radius, float centerX, float centerY) {
        if (radius >= 1.0f) {
            this._scopePaint.setShader(new RadialGradient(centerX, centerY, radius, 16711680, -2011262721, Shader.TileMode.REPEAT));
            canvas.drawCircle(centerX, centerY, radius, this._scopePaint);
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas paramCanvas) {
        try {
            if (this.m_mapView.getMap() != null && this.m_GPSLocation != null && this.m_GPSLocation.isOpen && this.m_GPSLocation.IsLocation()) {
                Coordinate tempCoord = this.m_GPSLocation.getGPSCoordinate();
                Point localPoint = this.m_mapView.getMap().MapToScreen(tempCoord);
                if (PubVar.AutoPan && (localPoint.x <= 0 || localPoint.x >= PubVar.ScreenWidth || localPoint.y <= 0 || localPoint.y >= PubVar.ScreenHeight)) {
                    this.m_mapView._Pan.SetNewCenter(tempCoord);
                }
                if (PubVar.GPS_Show_Accuracy) {
                    drawScope(paramCanvas, (float) this.m_mapView.getMap().MapToScreenDistance(this.m_GPSLocation.accuracy), (float) localPoint.x, (float) localPoint.y);
                }
                DrawGPSPointCenter(paramCanvas, (float) localPoint.x, (float) localPoint.y);
                this.LastDrawPtn = localPoint;
            }
        } catch (Exception e) {
        }
    }

    private void DrawGPSPointCenter(Canvas canvas, float x, float y) {
        if (PubVar.GPS_Point_ShowType == 0) {
            if (this._GpsPointICON == null) {
                this._GpsPointICON = ((BitmapDrawable) PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.gpsmap_location)).getBitmap();
            }
            canvas.drawBitmap(this._GpsPointICON, x - ((float) (this._GpsPointICON.getWidth() / 2)), y - ((float) (this._GpsPointICON.getHeight() / 2)), (Paint) null);
            return;
        }
        if (this._GPSPointDirICON == null) {
            this._GPSPointDirICON = ((BitmapDrawable) PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.gpsmaptrack40)).getBitmap();
        }
        Matrix matrix = new Matrix();
        int tmpH = this._GPSPointDirICON.getWidth() / 2;
        float tmpAngle = this.m_GPSLocation.bearing;
        if (tmpAngle == 0.0f) {
            tmpAngle = PubVar._PubCommand.m_Compass.ZAngle;
        }
        matrix.setRotate(tmpAngle, (float) tmpH, (float) tmpH);
        matrix.postTranslate(x - ((float) tmpH), y - ((float) tmpH));
        canvas.drawBitmap(this._GPSPointDirICON, matrix, null);
    }

    public void UpdateGPSStatus(GPSLocationClass gpsLocationClass) {
        this.m_GPSLocation = gpsLocationClass;
        if (this.m_GPSLocation.IsLocation() && judgeNeedRefreshMap()) {
            this.m_mapView.invalidate();
        }
    }

    private boolean judgeNeedRefreshMap() {
        if (this.m_mapView.getMap() == null || this.m_GPSLocation == null || !this.m_GPSLocation.isOpen) {
            return false;
        }
        if (PubVar.AutoPan) {
            return true;
        }
        if (this.m_mapView.getMap().getExtend().ContainsPoint(this.m_GPSLocation.getGPSCoordinate())) {
            return true;
        }
        return false;
    }
}
