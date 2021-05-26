package  com.xzy.forestSystem.gogisapi.Edit;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;
import  com.xzy.forestSystem.gogisapi.Carto.ICommand;
import  com.xzy.forestSystem.gogisapi.Carto.IOnPaint;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Selection;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MoveFeature implements ICommand, IOnPaint {
    private MapView _mapView = null;
    private ICallback m_Callback = null;
    private long m_MouseDownTime = 0;
    private List<String> m_MoveLayers = new ArrayList();
    private boolean m_Moveing = false;
    private float m_OffsetX = 0.0f;
    private float m_OffsetY = 0.0f;
    private PointF m_StartPoint = null;

    public MoveFeature(MapView parammapView) {
        this._mapView = parammapView;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseDown(MotionEvent paramMotionEvent) {
        this.m_StartPoint = new PointF(paramMotionEvent.getX(), paramMotionEvent.getY());
        this.m_MouseDownTime = new Date().getTime();
        if (this.m_Callback != null) {
            List<String> tmpList = null;
            if (this.m_MoveLayers != null && this.m_MoveLayers.size() > 0) {
                tmpList = this._mapView.getMap().getFeatureSelectionByLayerID(this.m_MoveLayers.get(0), false).getSYSIDList();
            }
            this.m_Callback.OnClick("MoveFeatureMouseDown", new Object[]{Long.valueOf(this.m_MouseDownTime), tmpList});
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseMove(MotionEvent paramMotionEvent) {
        if (this.m_StartPoint != null) {
            this.m_OffsetX = paramMotionEvent.getX() - this.m_StartPoint.x;
            this.m_OffsetY = paramMotionEvent.getY() - this.m_StartPoint.y;
            this.m_Moveing = true;
            this._mapView.invalidate();
        }
    }

    public void SetMoveLayer(String layerID) {
        this.m_MoveLayers.clear();
        if (!layerID.equals("")) {
            this.m_MoveLayers.add(layerID);
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseUp(MotionEvent paramMotionEvent) {
        Selection tmpSelection;
        try {
            if (this.m_Moveing) {
                Coordinate localCoordinate1 = this._mapView.getMap().ScreenToMap(this.m_StartPoint.x, this.m_StartPoint.y);
                Coordinate localCoordinate2 = this._mapView.getMap().ScreenToMap(paramMotionEvent.getX(), paramMotionEvent.getY());
                double d1 = localCoordinate2.getX() - localCoordinate1.getX();
                double d2 = localCoordinate2.getY() - localCoordinate1.getY();
                for (String tmpLayerID : this.m_MoveLayers) {
                    GeoLayer tempGeoLayer = this._mapView.getMap().GetGeoLayerByName(tmpLayerID);
                    if (tempGeoLayer != null) {
                        DataSet pDataset = tempGeoLayer.getDataset();
                        if (tempGeoLayer.getVisible() && pDataset.getDataSource().getEditing() && (tmpSelection = this._mapView.getMap().getFeatureSelectionByLayerID(tmpLayerID, false)) != null && tmpSelection.getCount() > 0) {
                            Envelope tempExtend = null;
                            for (Integer num : tmpSelection.getGeometryIndexList()) {
                                int tempI = num.intValue();
                                AbstractGeometry tempGeo = pDataset.GetGeometry(tempI);
                                tempGeo.SetEdited(true);
                                tempGeo.UpdateCoordinate(d1, d2);
                                if (tempGeoLayer.getType() == EGeoLayerType.POLYGON) {
                                    ((Polygon) tempGeo).UpdateInnerPoint();
                                }
                                String str = String.valueOf(tempGeoLayer.getName()) + "," + tempI + "," + d1 + "," + d2;
                                if (tempExtend == null) {
                                    tempExtend = tempGeo.getEnvelope().Clone();
                                } else {
                                    tempExtend = tempExtend.MergeEnvelope(tempGeo.getEnvelope());
                                }
                            }
                            pDataset.setEdited(true);
                            if (!pDataset.UpdateMapIndexByExtend(tempExtend)) {
                                for (Integer num2 : tmpSelection.getGeometryIndexList()) {
                                    pDataset.JustUpdateGeoMapIndex(tempGeoLayer.getDataset().GetGeometry(num2.intValue()));
                                }
                            }
                            for (Integer num3 : tmpSelection.getGeometryIndexList()) {
                                pDataset.SaveGeoIndexToDB(tempGeoLayer.getDataset().GetGeometry(num3.intValue()));
                            }
                            if (this.m_Callback != null) {
                                this.m_Callback.OnClick("MoveFeatureMouseUp", new Object[]{Long.valueOf(this.m_MouseDownTime), tmpSelection.getSYSIDList()});
                            }
                        }
                    }
                }
                this.m_StartPoint = null;
                this.m_Moveing = false;
                this._mapView.getMap().Refresh();
            }
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas paramCanvas) {
        Selection tmpSelection;
        try {
            if (this.m_Moveing) {
                for (String tmpLayerID : this.m_MoveLayers) {
                    GeoLayer tempGeoLayer = this._mapView.getMap().GetGeoLayerByName(tmpLayerID);
                    if (tempGeoLayer != null && tempGeoLayer.getVisible() && tempGeoLayer.getDataset().getDataSource().getEditing() && (tmpSelection = this._mapView.getMap().getFeatureSelectionByLayerID(tmpLayerID, false)) != null && tmpSelection.getCount() > 0) {
                        tempGeoLayer.DrawSelection(tmpSelection, this._mapView.getMap(), paramCanvas, (int) this.m_OffsetX, (int) this.m_OffsetY);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void SetCallback(ICallback pCallback) {
        this.m_Callback = pCallback;
    }
}
