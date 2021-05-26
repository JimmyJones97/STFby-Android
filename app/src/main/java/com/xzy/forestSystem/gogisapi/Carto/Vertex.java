package  com.xzy.forestSystem.gogisapi.Carto;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Edit.EDrawType;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import java.util.ArrayList;
import java.util.List;

public class Vertex implements ICommand, IOnPaint {
    private MapView _mapView = null;
    private String m_EditLayerName = "";
    private AbstractGeometry m_Geometry = null;
    Coordinate m_OldCoordinate = null;
    private double m_TolerancePix = (15.0d * ((double) PubVar.ScaledDensity));
    private EVertexEditType m_VertexEditType = EVertexEditType.NONE;
    private int m_VertexMoveIndex = -1;
    private boolean m_VertexMoving = false;

    public enum EVertexEditType {
        NONE,
        MOVE,
        ADD,
        DELETE
    }

    public Vertex(MapView parammapView) {
        this._mapView = parammapView;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseDown(MotionEvent paramMotionEvent) {
        int tmpIndex;
        try {
            if (this.m_Geometry != null && this.m_Geometry.GetAllCoordinateList().size() > 0) {
                Polyline tmpPolyline = null;
                Polygon tmpPolygon = null;
                this.m_VertexMoveIndex = -1;
                EGeoLayerType tmpGeoType = this.m_Geometry.GetType();
                if (tmpGeoType == EGeoLayerType.POLYLINE) {
                    tmpPolyline = (Polyline) this.m_Geometry;
                } else if (tmpGeoType == EGeoLayerType.POLYGON) {
                    tmpPolygon = (Polygon) this.m_Geometry;
                    if (tmpPolygon.IsSimple() || this.m_VertexEditType == EVertexEditType.MOVE) {
                        tmpPolyline = new Polyline();
                        List<Coordinate> tempCoords = new ArrayList<>();
                        tempCoords.addAll(this.m_Geometry.GetAllCoordinateList());
                        tempCoords.add(tempCoords.get(0).Clone());
                        tmpPolyline.SetAllCoordinateList(tempCoords);
                    }
                }
                if (tmpPolyline != null) {
                    Coordinate tmpPoint = this._mapView.getMap().ScreenToMap(paramMotionEvent.getX(), paramMotionEvent.getY());
                    if (this.m_VertexEditType == EVertexEditType.MOVE) {
                        BasicValue localParam1 = new BasicValue();
                        if (tmpPolyline.HitVertexTest(tmpPoint, this._mapView.getMap().ToMapDistance(this.m_TolerancePix), localParam1) && (tmpIndex = localParam1.getInt()) != -1 && tmpIndex < this.m_Geometry.GetAllCoordinateList().size()) {
                            this.m_VertexMoveIndex = tmpIndex;
                            this.m_OldCoordinate = this.m_Geometry.GetAllCoordinateList().get(this.m_VertexMoveIndex).Clone();
                            this.m_VertexMoving = true;
                        }
                    } else if (this.m_VertexEditType == EVertexEditType.ADD) {
                        BasicValue localParam2 = new BasicValue();
                        if (tmpPolyline.HitTestSegment(tmpPoint, this._mapView.getMap().ToMapDistance(this.m_TolerancePix), localParam2)) {
                            this.m_Geometry.SetEdited(true);
                            this.m_Geometry.GetAllCoordinateList().add(localParam2.getInt() + 1, tmpPoint);
                            this._mapView.getMap().getGeoLayers().GetLayerByName(this.m_EditLayerName).getDataset().UpdateMapIndex(this.m_Geometry);
                            this._mapView.getMap().Refresh();
                        }
                    } else if (this.m_VertexEditType == EVertexEditType.DELETE) {
                        BasicValue tempParam = new BasicValue();
                        int tmpCount = this.m_Geometry.GetAllCoordinateList().size();
                        if (tmpGeoType == EGeoLayerType.POLYLINE && tmpCount <= 2) {
                            Common.ShowDialog("编辑线的顶点数目不能少于2个.");
                        } else if (tmpGeoType != EGeoLayerType.POLYGON || tmpCount > 3) {
                            if (tmpPolyline.HitVertexTest(tmpPoint, this._mapView.getMap().ToMapDistance(this.m_TolerancePix), tempParam)) {
                            }
                            int tmpIndex2 = tempParam.getInt();
                            if (tmpIndex2 != -1 && tmpIndex2 < tmpCount) {
                                this.m_Geometry.GetAllCoordinateList().remove(tmpIndex2);
                                this.m_Geometry.SetEdited(true);
                                this._mapView.getMap().getGeoLayers().GetLayerByName(this.m_EditLayerName).getDataset().UpdateMapIndex(this.m_Geometry);
                                this._mapView.getMap().Refresh();
                            }
                        } else {
                            Common.ShowDialog("编辑面的顶点数目不能少于3个.");
                        }
                    }
                } else if (tmpPolygon != null) {
                    Coordinate tmpPoint2 = this._mapView.getMap().ScreenToMap(paramMotionEvent.getX(), paramMotionEvent.getY());
                    if (this.m_VertexEditType == EVertexEditType.DELETE) {
                        BasicValue tmppartValue = new BasicValue();
                        BasicValue tmpindexValue = new BasicValue();
                        if (GetHitVertexInPolygon(tmpPolygon, tmpPoint2, tmppartValue, tmpindexValue)) {
                            this.m_Geometry.SetEdited(true);
                            tmpPolygon.DeletePoint(tmppartValue.getInt(), tmpindexValue.getInt());
                            this._mapView.getMap().getGeoLayers().GetLayerByName(this.m_EditLayerName).getDataset().UpdateMapIndex(this.m_Geometry);
                            DataSet pDataset = this._mapView.getMap().getGeoLayers().GetLayerByName(this.m_EditLayerName).getDataset();
                            if (pDataset != null) {
                                this.m_Geometry.UpdateEnvelope();
                                pDataset.UpdateMapIndex(this.m_Geometry);
                                pDataset.setEdited(true);
                            }
                            this._mapView.getMap().Refresh();
                            return;
                        }
                        return;
                    }
                    EVertexEditType eVertexEditType = EVertexEditType.ADD;
                }
            }
        } catch (Exception e) {
        }
    }

    public boolean GetHitVertexInPolygon(Polygon polygon, Coordinate pointCoord, BasicValue partValue, BasicValue indexValue) {
        int tmpPartCount = polygon.GetNumberOfParts();
        for (int i = 0; i < tmpPartCount; i++) {
            List<Coordinate> tmpList = polygon.GetPartAt(i);
            if (tmpList.size() > 1 && Polyline.HitVertexInList(pointCoord, tmpList, this._mapView.getMap().ToMapDistance(this.m_TolerancePix), indexValue)) {
                partValue.setValue(i);
                return true;
            }
        }
        return false;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseMove(MotionEvent paramMotionEvent) {
        try {
            if (this.m_VertexMoving) {
                Coordinate localCoordinate = this._mapView.getMap().ScreenToMap(paramMotionEvent.getX(), paramMotionEvent.getY());
                this.m_Geometry.GetAllCoordinateList().get(this.m_VertexMoveIndex).setX(localCoordinate.getX());
                this.m_Geometry.GetAllCoordinateList().get(this.m_VertexMoveIndex).setY(localCoordinate.getY());
                this.m_Geometry.SetEdited(true);
                this._mapView.invalidate();
            }
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseUp(MotionEvent paramMotionEvent) {
        try {
            if (this.m_VertexMoving) {
                this.m_VertexMoving = false;
                DataSet pDataset = this._mapView.getMap().getGeoLayers().GetLayerByName(this.m_EditLayerName).getDataset();
                if (pDataset != null) {
                    this.m_Geometry.UpdateEnvelope();
                    pDataset.UpdateMapIndex(this.m_Geometry);
                    pDataset.setEdited(true);
                }
                this._mapView.getMap().Refresh();
            }
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas paramCanvas) {
        try {
            if (this.m_VertexMoving) {
                this.m_Geometry.getSymbol().Draw(this._mapView.getMap(), paramCanvas, this.m_Geometry, 0, 0, EDrawType.EDIT_SEL);
            }
        } catch (Exception e) {
        }
    }

    public void SetVertexEditType(EVertexEditType paramlkVertexEditType) {
        this.m_VertexEditType = paramlkVertexEditType;
        BasicValue localParam1 = new BasicValue();
        BasicValue localParam2 = new BasicValue();
        if (Common.GetSelectOneObjectInfo(this._mapView.getMap(), localParam1, localParam2, new BasicValue())) {
            GeoLayer localGeoLayer = this._mapView.getMap().getGeoLayers().GetLayerByName(localParam1.getString());
            if (localGeoLayer == null || !localGeoLayer.getEditable() || !(localGeoLayer.getType() == EGeoLayerType.POLYLINE || localGeoLayer.getType() == EGeoLayerType.POLYGON)) {
                this.m_Geometry = null;
                return;
            }
            this.m_EditLayerName = localParam1.getString();
            this.m_Geometry = localGeoLayer.getDataset().GetGeometry(localParam2.getInt());
        }
    }

    public void SetVertexEditType(EVertexEditType paramlkVertexEditType, String layerID, AbstractGeometry geometry) {
        this.m_VertexEditType = paramlkVertexEditType;
        this.m_EditLayerName = layerID;
        this.m_Geometry = geometry;
    }
}
