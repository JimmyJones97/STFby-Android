package  com.xzy.forestSystem.gogisapi.Edit;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import androidx.core.internal.view.SupportMenu;
import android.view.MotionEvent;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import  com.xzy.forestSystem.gogisapi.Carto.ZoomInOutPan;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Point;

public class PointEditClass extends ZoomInOutPan {
    private static Paint CenterlinePaint = null;
    private static Paint CenterlinePaint02 = null;
    public boolean IsDrawInMapCenter = false;
    private boolean _AllowSnap = true;
    private double _SnapDistance = (20.0d * ((double) PubVar.ScaledDensity));
    private Toolbar_Point m_Toolbar = null;

    public PointEditClass(MapView mapView) {
        super(mapView);
        RefreshSnap();
    }

    public void RefreshSnap() {
        this._AllowSnap = PubVar.AllowEditSnap;
        this._SnapDistance = PubVar.SnapDistance * ((double) PubVar.ScaledDensity);
        this._SnapDistance *= this._SnapDistance;
    }

    public void SetToolbar(Toolbar_Point toolbar) {
        this.m_Toolbar = toolbar;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ZoomInOutPan,  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas paramCanvas) {
        super.OnPaint(paramCanvas);
    }

    private static Paint getCenterLinePaint() {
        if (CenterlinePaint == null) {
            CenterlinePaint = new Paint();
            CenterlinePaint.setStyle(Paint.Style.STROKE);
            CenterlinePaint.setColor(-16711936);
            CenterlinePaint.setStrokeWidth((float) ((int) PubVar.ScaledDensity));
            CenterlinePaint.setAntiAlias(true);
        }
        return CenterlinePaint;
    }

    private static Paint getCenterLinePaint02() {
        if (CenterlinePaint02 == null) {
            CenterlinePaint02 = new Paint();
            CenterlinePaint02.setStyle(Paint.Style.STROKE);
            CenterlinePaint02.setColor(SupportMenu.CATEGORY_MASK);
            CenterlinePaint02.setStrokeWidth((float) ((int) (2.0f * PubVar.ScaledDensity)));
            CenterlinePaint02.setAntiAlias(true);
        }
        return CenterlinePaint02;
    }

    public static void DrawMapCenterPlus(Canvas canvas) {
        int tmpCenterX = canvas.getWidth() / 2;
        int tmpCenterY = canvas.getHeight() / 2;
        int tmpLen = (int) (10.0f * PubVar.ScaledDensity);
        int tmpS = (int) (2.0f * PubVar.ScaledDensity);
        Paint tmpPaint = getCenterLinePaint02();
        canvas.drawLine((float) ((tmpCenterX - tmpLen) - tmpS), (float) (tmpCenterY - tmpS), (float) (tmpCenterX - tmpS), (float) (tmpCenterY - tmpS), tmpPaint);
        canvas.drawLine((float) (tmpCenterX + tmpS), (float) (tmpCenterY - tmpS), (float) (tmpCenterX + tmpLen + tmpS), (float) (tmpCenterY - tmpS), tmpPaint);
        canvas.drawLine((float) ((tmpCenterX - tmpLen) - tmpS), (float) (tmpCenterY + tmpS), (float) (tmpCenterX - tmpS), (float) (tmpCenterY + tmpS), tmpPaint);
        canvas.drawLine((float) (tmpCenterX + tmpS), (float) (tmpCenterY + tmpS), (float) (tmpCenterX + tmpLen + tmpS), (float) (tmpCenterY + tmpS), tmpPaint);
        canvas.drawLine((float) (tmpCenterX - tmpS), (float) ((tmpCenterY - tmpS) - tmpLen), (float) (tmpCenterX - tmpS), (float) (tmpCenterY - tmpS), tmpPaint);
        canvas.drawLine((float) (tmpCenterX + tmpS), (float) ((tmpCenterY - tmpS) - tmpLen), (float) (tmpCenterX + tmpS), (float) (tmpCenterY - tmpS), tmpPaint);
        canvas.drawLine((float) (tmpCenterX - tmpS), (float) (tmpCenterY + tmpS), (float) (tmpCenterX - tmpS), (float) (tmpCenterY + tmpS + tmpLen), tmpPaint);
        canvas.drawLine((float) (tmpCenterX + tmpS), (float) (tmpCenterY + tmpS), (float) (tmpCenterX + tmpS), (float) (tmpCenterY + tmpS + tmpLen), tmpPaint);
        Paint tmpPaint2 = getCenterLinePaint();
        canvas.drawLine((float) ((tmpCenterX - tmpLen) - tmpS), (float) (tmpCenterY - tmpS), (float) (tmpCenterX - tmpS), (float) (tmpCenterY - tmpS), tmpPaint2);
        canvas.drawLine((float) (tmpCenterX + tmpS), (float) (tmpCenterY - tmpS), (float) (tmpCenterX + tmpLen + tmpS), (float) (tmpCenterY - tmpS), tmpPaint2);
        canvas.drawLine((float) ((tmpCenterX - tmpLen) - tmpS), (float) (tmpCenterY + tmpS), (float) (tmpCenterX - tmpS), (float) (tmpCenterY + tmpS), tmpPaint2);
        canvas.drawLine((float) (tmpCenterX + tmpS), (float) (tmpCenterY + tmpS), (float) (tmpCenterX + tmpLen + tmpS), (float) (tmpCenterY + tmpS), tmpPaint2);
        canvas.drawLine((float) (tmpCenterX - tmpS), (float) ((tmpCenterY - tmpS) - tmpLen), (float) (tmpCenterX - tmpS), (float) (tmpCenterY - tmpS), tmpPaint2);
        canvas.drawLine((float) (tmpCenterX + tmpS), (float) ((tmpCenterY - tmpS) - tmpLen), (float) (tmpCenterX + tmpS), (float) (tmpCenterY - tmpS), tmpPaint2);
        canvas.drawLine((float) (tmpCenterX - tmpS), (float) (tmpCenterY + tmpS), (float) (tmpCenterX - tmpS), (float) (tmpCenterY + tmpS + tmpLen), tmpPaint2);
        canvas.drawLine((float) (tmpCenterX + tmpS), (float) (tmpCenterY + tmpS), (float) (tmpCenterX + tmpS), (float) (tmpCenterY + tmpS + tmpLen), tmpPaint2);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ZoomInOutPan,  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseDown(MotionEvent paramMotionEvent) {
        if (paramMotionEvent.getPointerCount() > 1) {
            Common.ShowToast("");
        }
        super.MouseDown(paramMotionEvent);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ZoomInOutPan,  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseMove(MotionEvent paramMotionEvent) {
        super.MouseMove(paramMotionEvent);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ZoomInOutPan,  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseUp(MotionEvent paramMotionEvent) {
        super.MouseUp(paramMotionEvent);
        if (!this.IsMove && this.m_Toolbar != null) {
            PointF localPointF = new PointF(paramMotionEvent.getX(), paramMotionEvent.getY());
            Coordinate tmpCoord = null;
            if (this._AllowSnap) {
                tmpCoord = PolygonEditClass.CheckSnap(this._mapView, localPointF, this._SnapDistance);
            }
            if (tmpCoord == null) {
                tmpCoord = this._mapView.getMap().ScreenToMap(localPointF);
                Coordinate localCoordinate2 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tmpCoord, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                tmpCoord.setGeoX(localCoordinate2.getGeoX());
                tmpCoord.setGeoY(localCoordinate2.getGeoY());
            }
            if (tmpCoord != null) {
                this.m_Toolbar.AddPoint(tmpCoord, "手绘点位");
            }
        }
    }
}
