package  com.xzy.forestSystem.gogisapi.Carto;

import android.graphics.Canvas;
import android.view.MotionEvent;

public class ZoomIn implements ICommand, IOnPaint {
    private ICommand _Command;
    private TrackRectangle _TrackTect = null;
    private MapView _mapView = null;

    public ZoomIn(MapView parammapView) {
        this._TrackTect = new TrackRectangle(parammapView);
        this._Command = this._TrackTect;
        this._mapView = parammapView;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseDown(MotionEvent paramMotionEvent) {
        this._Command.MouseDown(paramMotionEvent);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseMove(MotionEvent paramMotionEvent) {
        this._Command.MouseMove(paramMotionEvent);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseUp(MotionEvent paramMotionEvent) {
        this._Command.MouseUp(paramMotionEvent);
        if (this._TrackTect.getTrackEnvelope() == null) {
            this._TrackTect.setTrackEnvelope(this._mapView.getMap().getExtend().Scale(0.5d));
        } else {
            this._mapView.getMap().setExtend(this._TrackTect.getTrackEnvelope());
        }
        this._mapView.getMap().Refresh();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas paramCanvas) {
        this._TrackTect.OnPaint(paramCanvas);
    }
}
