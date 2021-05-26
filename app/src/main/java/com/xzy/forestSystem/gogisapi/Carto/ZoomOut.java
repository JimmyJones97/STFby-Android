package  com.xzy.forestSystem.gogisapi.Carto;

import android.view.MotionEvent;

public class ZoomOut implements ICommand {
    private ICommand _Command;
    private TrackRectangle _TrackRect;
    private MapView _mapView = null;

    public ZoomOut(MapView parammapView) {
        this._TrackRect = new TrackRectangle(parammapView);
        this._Command = this._TrackRect;
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
        if (this._TrackRect.getTrackEnvelope() == null) {
            this._mapView.getMap().setExtend(this._mapView.getMap().getExtend().Scale(2.0d));
        } else {
            this._mapView.getMap().setExtend(this._mapView.getMap().getExtend().Scale(this._mapView.getMap().getExtend().Factor(this._TrackRect.getTrackEnvelope())));
        }
        this._mapView.getMap().Refresh();
    }
}
