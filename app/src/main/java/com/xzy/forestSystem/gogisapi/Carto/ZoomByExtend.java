package  com.xzy.forestSystem.gogisapi.Carto;

import android.graphics.PointF;
import android.view.MotionEvent;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;

public class ZoomByExtend implements ICommand {
    private static final float DoubleClick_Bias = (10.0f * PubVar.ScaledDensity);
    static long DoubleClick_TIME = 400;
    public static double SELECT_CLICK_BIAS = ((double) (30.0f * PubVar.ScaledDensity));
    private float LastX = 0.0f;
    private float LastY = 0.0f;
    private ICallback _Callback = null;
    private ICommand _Command = null;
    private long _LastMouseDownTime = 0;
    private TrackRectangle _TrackRect = null;
    private MapView _mapView = null;

    public ZoomByExtend(MapView parammapView) {
        this._mapView = parammapView;
        this._TrackRect = new TrackRectangle(parammapView);
        this._Command = this._TrackRect;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseDown(MotionEvent paramMotionEvent) {
        this._TrackRect.setTrackEnvelope(null);
        this._Command.MouseDown(paramMotionEvent);
        long tmpIntv = paramMotionEvent.getDownTime() - this._LastMouseDownTime;
        this._LastMouseDownTime = paramMotionEvent.getDownTime();
        float tmpX = Math.abs(paramMotionEvent.getX() - this.LastX);
        float tmpY = Math.abs(paramMotionEvent.getY() - this.LastY);
        if (tmpX >= DoubleClick_Bias || tmpY >= DoubleClick_Bias || tmpIntv >= DoubleClick_TIME) {
            this.LastX = paramMotionEvent.getX();
            this.LastY = paramMotionEvent.getY();
            return;
        }
        this._mapView.getMap().RefreshFast();
        this.LastX = paramMotionEvent.getX();
        this.LastY = paramMotionEvent.getY();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseMove(MotionEvent paramMotionEvent) {
        this._Command.MouseMove(paramMotionEvent);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseUp(MotionEvent paramMotionEvent) {
        this._Command.MouseUp(paramMotionEvent);
        this.LastX = paramMotionEvent.getX();
        this.LastY = paramMotionEvent.getY();
        PointF tmpStartPtn = this._TrackRect.getStartPoint();
        PointF tmpEndPtn = this._TrackRect.getEndPoint();
        if (((double) Math.abs(tmpStartPtn.x - tmpEndPtn.x)) > SELECT_CLICK_BIAS) {
            boolean tmpIsLeft = true;
            if (tmpEndPtn.x < tmpStartPtn.x) {
                tmpIsLeft = false;
            }
            Envelope tmpExtend = this._TrackRect.getTrackEnvelope();
            if (tmpIsLeft) {
                this._mapView.getMap().ZoomToExtend(tmpExtend);
                return;
            }
            Envelope tmpExtend2 = this._mapView.getMap().getExtend();
            double tmpRatio = tmpExtend2.getWidth() / tmpExtend.getWidth();
            double tmpRatio2 = tmpExtend2.getHeight() / tmpExtend.getHeight();
            if (tmpRatio > tmpRatio2) {
                tmpRatio = tmpRatio2;
            }
            this._mapView.getMap().ZoomToExtend(tmpExtend2.Scale(tmpRatio));
        }
    }

    public void SetCallback(ICallback tmpICallback) {
        this._Callback = tmpICallback;
    }
}
