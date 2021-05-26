package  com.xzy.forestSystem.gogisapi.Carto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import  com.xzy.forestSystem.gogisapi.Display.ETextPosition;
import  com.xzy.forestSystem.gogisapi.Display.TextSymbol;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.EGeoDisplayType;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import java.util.ArrayList;
import java.util.List;

public class GraphicsLayer implements IOnPaint {
    private Bitmap _MaskBitmap = null;
    private MapView _mapView;
    private List<GraphicSymbolGeometry> m_GeoList = null;
    private List<String> m_GeoUIDList = null;
    private TextSymbol m_TextSymbol = null;

    public GraphicsLayer(MapView parammapView) {
        this._mapView = parammapView;
        this.m_GeoList = new ArrayList();
        this.m_GeoUIDList = new ArrayList();
        this.m_TextSymbol = new TextSymbol();
        this.m_TextSymbol.SetTextSymbolFont("#FF0000FF,12");
    }

    public void Clear() {
        RemoveGeometrysExceptType("TrackLine");
    }

    public void AddGeometry(GraphicSymbolGeometry sysbolGeometry) {
        String tmpUIDString = sysbolGeometry._UID;
        if (!this.m_GeoUIDList.contains(tmpUIDString)) {
            this.m_GeoList.add(sysbolGeometry);
            this.m_GeoUIDList.add(tmpUIDString);
        }
    }

    public List<GraphicSymbolGeometry> getGeoemtryList() {
        return this.m_GeoList;
    }

    public void DeleteGeometry(GraphicSymbolGeometry sysbolGeometry) {
        this.m_GeoList.remove(sysbolGeometry);
        this.m_GeoUIDList.remove(sysbolGeometry._UID);
    }

    public boolean DeleteGeometry(String uid) {
        int tmpI = this.m_GeoUIDList.indexOf(uid);
        if (tmpI <= -1) {
            return false;
        }
        this.m_GeoList.remove(tmpI);
        this.m_GeoUIDList.remove(tmpI);
        return true;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas paramCanvas) {
        if (this.m_GeoList.size() > 0) {
            paramCanvas.drawBitmap(getMaskBitmap(), 0.0f, 0.0f, (Paint) null);
        }
    }

    private Bitmap getMaskBitmap() {
        try {
            Map pMap = this._mapView.getMap();
            if (this._MaskBitmap == null) {
                this._MaskBitmap = Bitmap.createBitmap(pMap.getSize().getWidth(), pMap.getSize().getHeight(), Bitmap.Config.ARGB_4444);
            }
            this._MaskBitmap.eraseColor(0);
            Canvas paramCanvas = new Canvas(this._MaskBitmap);
            for (GraphicSymbolGeometry tmpGeo : this.m_GeoList) {
                if (!(tmpGeo._Geoemtry == null || tmpGeo._Symbol == null)) {
                    tmpGeo._Symbol.Draw(pMap, paramCanvas, tmpGeo._Geoemtry, 0, 0, tmpGeo._DrawMode);
                    if (!tmpGeo._Geoemtry.getLabelContent().equals("")) {
                        Coordinate tmpCoord = tmpGeo._Geoemtry.getPoint(0);
                        if (tmpGeo._Geoemtry instanceof Polygon) {
                            tmpCoord = ((Polygon) tmpGeo._Geoemtry).getInnerPoint();
                        }
                        Point localPoint1 = this._mapView.getMap().MapToScreen(tmpCoord.getX(), tmpCoord.getY());
                        int j = localPoint1.x;
                        int k = localPoint1.y;
                        if (j > -1 && j < PubVar.ScreenWidth && k > -1 && k < PubVar.ScreenHeight) {
                            this.m_TextSymbol.Draw(paramCanvas, (float) (j + 20), (float) (k + 8), tmpGeo._Geoemtry.getLabelContent(), ETextPosition.LEFT_ALIGIN, EGeoDisplayType.NORMAL);
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return this._MaskBitmap;
    }

    public boolean HitTest(Coordinate clickCoordinate, double distance, BasicValue returnSelectIndex) {
        if (this.m_GeoList.size() > 0) {
            int tmpTid = -1;
            for (GraphicSymbolGeometry tmpGraphicSymbolGeometry : this.m_GeoList) {
                tmpTid++;
                if (!(tmpGraphicSymbolGeometry == null || tmpGraphicSymbolGeometry._Geoemtry == null || !tmpGraphicSymbolGeometry._Geoemtry.HitTest(clickCoordinate, distance))) {
                    returnSelectIndex.setValue(tmpTid);
                    return true;
                }
            }
        }
        return false;
    }

    public GraphicSymbolGeometry getGeometryByIndex(int index) {
        if (index <= -1 || index >= this.m_GeoList.size()) {
            return null;
        }
        return this.m_GeoList.get(index);
    }

    public void DeleteGeometry(int geoIndex) {
        if (geoIndex > -1 && geoIndex < this.m_GeoList.size()) {
            this.m_GeoList.remove(geoIndex);
            this.m_GeoUIDList.remove(geoIndex);
        }
    }

    public int getGeoemtrysCount() {
        return this.m_GeoList.size();
    }

    public void RemoveGeometrysByType(String GeometryType) {
        int i = 0;
        int count = this.m_GeoList.size();
        while (i < count) {
            GraphicSymbolGeometry tmpGraphicSymbolGeometry = this.m_GeoList.get(i);
            if (tmpGraphicSymbolGeometry != null && tmpGraphicSymbolGeometry._GeometryType.equals(GeometryType)) {
                this.m_GeoList.remove(i);
                this.m_GeoUIDList.remove(i);
                count = this.m_GeoList.size();
                i--;
            }
            i++;
        }
    }

    public void RemoveGeometrysExceptType(String GeometryType) {
        int i = 0;
        int count = this.m_GeoList.size();
        while (i < count) {
            GraphicSymbolGeometry tmpGraphicSymbolGeometry = this.m_GeoList.get(i);
            if (tmpGraphicSymbolGeometry != null && !tmpGraphicSymbolGeometry._GeometryType.equals(GeometryType)) {
                this.m_GeoList.remove(i);
                this.m_GeoUIDList.remove(i);
                count = this.m_GeoList.size();
                i--;
            }
            i++;
        }
    }
}
