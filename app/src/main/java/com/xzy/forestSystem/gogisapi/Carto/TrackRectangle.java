package  com.xzy.forestSystem.gogisapi.Carto;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.core.internal.view.SupportMenu;
import android.view.MotionEvent;


import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;

public class TrackRectangle implements ICommand, IOnPaint {
    private Paint _BorderPaint = null;
    private PointF _EndPoint;
    private Paint _FillPaint = null;
    private boolean _IsLeftDown = false;
    private PointF _StartPoint;
    private Envelope _TrackEnvelope = null;
    private RectF _TrackRectF = null;
    private MapView _mapView;

    public TrackRectangle(MapView parammapView) {
        this._mapView = parammapView;
        this._BorderPaint = new Paint();
        this._BorderPaint.setStyle(Paint.Style.STROKE);
        this._BorderPaint.setColor(SupportMenu.CATEGORY_MASK);
        this._BorderPaint.setStrokeWidth(PubVar.ScaledDensity * 2.0f);
        this._FillPaint = new Paint();
        this._FillPaint.setStyle(Paint.Style.FILL);
        this._FillPaint.setColor(-2142657113);
    }

    private Rect GetTrackRect(PointF paramPointF1, PointF paramPointF2) {
        if (this._TrackRectF == null) {
            this._TrackRectF = new RectF(Math.min(paramPointF1.x, paramPointF2.x), Math.min(paramPointF1.y, paramPointF2.y), Math.max(paramPointF1.x, paramPointF2.x), Math.max(paramPointF1.y, paramPointF2.y));
        } else {
            this._TrackRectF.left = Math.min(paramPointF1.x, paramPointF2.x);
            this._TrackRectF.top = Math.min(paramPointF1.y, paramPointF2.y);
            this._TrackRectF.right = Math.max(paramPointF1.x, paramPointF2.x);
            this._TrackRectF.bottom = Math.max(paramPointF1.y, paramPointF2.y);
        }
        return new Rect((int) (this._TrackRectF.left - 10.0f), (int) (this._TrackRectF.top - 10.0f), (int) (this._TrackRectF.right + 20.0f), (int) (this._TrackRectF.bottom + 20.0f));
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseDown(MotionEvent paramMotionEvent) {
        this._TrackEnvelope = null;
        this._mapView.SetOnPaint(this);
        this._StartPoint = new PointF(paramMotionEvent.getX(), paramMotionEvent.getY());
        this._IsLeftDown = true;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseMove(MotionEvent paramMotionEvent) {
        if (this._IsLeftDown) {
            this._EndPoint = new PointF(paramMotionEvent.getX(), paramMotionEvent.getY());
            this._mapView.invalidate(GetTrackRect(this._StartPoint, this._EndPoint));
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseUp(MotionEvent paramMotionEvent) {
        this._IsLeftDown = false;
        if (this._TrackRectF != null) {
            this._TrackEnvelope = new Envelope(this._mapView.getMap().ScreenToMap(this._TrackRectF.left, this._TrackRectF.top), this._mapView.getMap().ScreenToMap(this._TrackRectF.right, this._TrackRectF.bottom));
            if (paramMotionEvent.getX() <= this._StartPoint.x) {
                this._TrackEnvelope.setType(false);
            } else {
                this._TrackEnvelope.setType(true);
            }
        }
        this._TrackRectF = null;
        this._mapView.invalidate();
    }

    public PointF getEndPoint() {
        return this._EndPoint;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas paramCanvas) {
        if (this._TrackRectF != null) {
            paramCanvas.drawRect(this._TrackRectF, this._BorderPaint);
            paramCanvas.drawRect(this._TrackRectF, this._FillPaint);
        }
    }

    public void setTrackEnvelope(Envelope paramEnvelope) {
        this._TrackEnvelope = paramEnvelope;
    }

    public Envelope getTrackEnvelope() {
        return this._TrackEnvelope;
    }

    public PointF getStartPoint() {
        return this._StartPoint;
    }
}
