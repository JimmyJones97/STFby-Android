package  com.xzy.forestSystem.gogisapi.Carto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;

import android.widget.ImageView;

import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.ELayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoGroupLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XRasterFileLayer;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSource;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Selection;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.EGeoDisplayType;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.Geometry.Size;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.SpatialRelation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Map {
    float MaskBiasX = 0.0f;
    float MaskBiasY = 0.0f;
    protected Bitmap MaskBitmap = null;
    protected Bitmap RasterBitmap = null;
    public boolean RasterFastResfreshMode = true;
    private Bitmap ShutterBitmap = null;
    private Bitmap ShutterRasterBitmap = null;
    protected Bitmap VectorBitmap = null;
    protected int _ActualScale = 100;
    private List<Boolean> _BKVectorLayersViList = null;
    protected Coordinate _Center;
    private Bitmap _CompanyBitmap = null;
    public int _DebugMode = 0;
    public StringBuilder _DebugOutSB = new StringBuilder();
    protected MapView _DrawPicture;
    protected Envelope _Extend;
    protected Envelope _ExtendForView = null;
    protected Envelope _FullExtend;
    protected GeoGroupLayer _GeoLayers = new GeoGroupLayer();
    protected Canvas _Graphics;
    List<Envelope> _MapExtendList = new ArrayList();
    protected List<XLayer> _RasterLayers = new ArrayList();
    private List<Boolean> _RasterLayersViList = new ArrayList();
    private ScaleBar _ScaleBar = null;
    protected double _ScaleZoom;
    List<Selection> _SelSelectionList = new ArrayList();
    List<Selection> _ShowSelectionList = new ArrayList();
    protected Size _Size;
    private String _SystemPath = "";
    private Paint _TextBgPaint = null;
    private Paint _TextPaint = null;
    protected GeoGroupLayer _VectorGeoLayers = new GeoGroupLayer();
    protected double _Zoom;
    protected double _ZoomScale;

    /* renamed from: bp */
    private Bitmap f448bp = null;
    List<Point[]> extendVector = null;
    private boolean m_AllowRfresh = true;
    private HashMap<String, Paint> m_TxtPaintList = new HashMap<>();

    public Map(MapView mapView) {
        this._DrawPicture = mapView;
        setSize(new Size(400, 400));
        mapView.setMap(this);
        this._TextPaint = new Paint();
        this._TextPaint.setAntiAlias(true);
        this._TextPaint.setTextSize(PubVar.ScaledDensity * 18.0f);
        this._TextPaint.setColor(SupportMenu.CATEGORY_MASK);
        Typeface localTypeface = Typeface.create("宋体", 0);
        this._TextPaint.setTypeface(localTypeface);
        this._TextPaint.setTextAlign(Paint.Align.CENTER);
        this._TextBgPaint = new Paint();
        this._TextBgPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this._TextBgPaint.setAlpha(100);
        this._TextBgPaint.setAntiAlias(true);
        this._TextBgPaint.setTypeface(localTypeface);
        this._TextBgPaint.setAntiAlias(true);
        this._TextBgPaint.setTextSize(PubVar.ScaledDensity * 18.0f);
        this._TextBgPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void Dispose() {
        if (this.f448bp != null) {
            this.f448bp.recycle();
        }
        this.f448bp = null;
        if (this.MaskBitmap != null) {
            this.MaskBitmap.recycle();
        }
        this.MaskBitmap = null;
        if (this.RasterBitmap != null && !this.RasterBitmap.isRecycled()) {
            this.RasterBitmap.recycle();
        }
        this.RasterBitmap = null;
        if (this.VectorBitmap != null && !this.VectorBitmap.isRecycled()) {
            this.VectorBitmap.recycle();
        }
        this.VectorBitmap = null;
        if (this.ShutterBitmap != null && !this.ShutterBitmap.isRecycled()) {
            this.ShutterBitmap.recycle();
        }
        this.ShutterBitmap = null;
        if (this.ShutterRasterBitmap != null && !this.ShutterRasterBitmap.isRecycled()) {
            this.ShutterRasterBitmap.recycle();
        }
        this.ShutterRasterBitmap = null;
        this._MapExtendList.clear();
        if (this._BKVectorLayersViList != null) {
            this._BKVectorLayersViList.clear();
        }
        this._RasterLayersViList.clear();
        this._Graphics = null;
        System.gc();
    }

    public void Dispose2() {
        if (this.f448bp != null) {
            this.f448bp.recycle();
        }
        this.f448bp = null;
        if (this.MaskBitmap != null) {
            this.MaskBitmap.recycle();
        }
        this.MaskBitmap = null;
        if (this.RasterBitmap != null && !this.RasterBitmap.isRecycled()) {
            this.RasterBitmap.recycle();
        }
        this.RasterBitmap = null;
        if (this.VectorBitmap != null && !this.VectorBitmap.isRecycled()) {
            this.VectorBitmap.recycle();
        }
        this.VectorBitmap = null;
        if (this.ShutterBitmap != null && !this.ShutterBitmap.isRecycled()) {
            this.ShutterBitmap.recycle();
        }
        this.ShutterBitmap = null;
        if (this.ShutterRasterBitmap != null && !this.ShutterRasterBitmap.isRecycled()) {
            this.ShutterRasterBitmap.recycle();
        }
        this.ShutterRasterBitmap = null;
        this._MapExtendList.clear();
        if (this._BKVectorLayersViList != null) {
            this._BKVectorLayersViList.clear();
        }
        this._RasterLayersViList.clear();
        this._GeoLayers.Dispose2();
        this._VectorGeoLayers.Dispose2();
        if (this._RasterLayers.size() > 0) {
            for (XLayer tmpXLayer : this._RasterLayers) {
                if (tmpXLayer != null) {
                    tmpXLayer.Dispose2();
                }
            }
            this._RasterLayers.clear();
        }
        this._Graphics = null;
        System.gc();
    }

    public int SetDPI(float paramFloat) {
        return (int) paramFloat;
    }

    public int SetDPI(int paramInt) {
        return paramInt;
    }

    public double ToMapDistance(double paramDouble) {
        return getZoomScale() * paramDouble;
    }

    public void SetScaleBar(ScaleBar scaleBar) {
        this._ScaleBar = scaleBar;
    }

    public void setSystemPath(String paramString) {
        this._SystemPath = paramString;
    }

    public String getSystemPath() {
        return this._SystemPath;
    }

    public Canvas getDisplayGraphic() {
        return this._Graphics;
    }

    public ImageView getDrawPicture() {
        return this._DrawPicture;
    }

    public double getScale() {
        return getZoomScale();
    }

    public Envelope getFullExtendForView() {
        if (this._ExtendForView == null) {
            return getFullExtend();
        }
        return this._ExtendForView;
    }

    public GeoGroupLayer getGeoLayers() {
        return this._GeoLayers;
    }

    public List<XLayer> getRasterLayers() {
        return this._RasterLayers;
    }

    public GeoGroupLayer getVectorGeoLayers() {
        return this._VectorGeoLayers;
    }

    public boolean getAllowRefreshMap() {
        return this.m_AllowRfresh;
    }

    public void setAllowRefreshMap(boolean allowRefresh) {
        this.m_AllowRfresh = allowRefresh;
    }

    public void ZoomToCenter(Coordinate tmpCoord) {
        ZoomToCenter(tmpCoord.getX(), tmpCoord.getY());
    }

    public void ZoomToCenter(double x, double y) {
        getCenter().setX(x);
        getCenter().setY(y);
        CalExtend();
        Refresh();
    }

    public void ZoomToCenterAndScale(double x, double y, float scale) {
        getCenter().setX(x);
        getCenter().setY(y);
        CalExtend();
        setExtend(getExtend().Scale((double) scale));
        Refresh();
    }

    public void ZoomToExtend(Envelope paramEnvelope) {
        setExtend(paramEnvelope);
        Refresh();
    }

    public void InitialSelections() {
        this._SelSelectionList = new ArrayList();
        this._ShowSelectionList = new ArrayList();
        if (this._VectorGeoLayers.size() > 0) {
            ListIterator<GeoLayer> tempIter01 = this._VectorGeoLayers.getList().listIterator();
            while (tempIter01.hasNext()) {
                GeoLayer tmpLayer = tempIter01.next();
                Selection tmpSelSelection = new Selection();
                Selection tmpShowSelection = new Selection();
                tmpSelSelection.setDataset(tmpLayer.getDataset());
                tmpShowSelection.setDataset(tmpLayer.getDataset());
                tmpSelSelection._LayerName = tmpLayer.getLayerName();
                tmpShowSelection._LayerName = tmpLayer.getLayerName();
                tmpShowSelection.setType(EGeoDisplayType.NORMAL);
                tmpSelSelection.setType(EGeoDisplayType.SELECT);
                this._SelSelectionList.add(tmpSelSelection);
                this._ShowSelectionList.add(tmpShowSelection);
            }
        }
        if (this._GeoLayers.size() > 0) {
            ListIterator<GeoLayer> tempIter02 = this._GeoLayers.getList().listIterator();
            while (tempIter02.hasNext()) {
                GeoLayer tmpLayer2 = tempIter02.next();
                Selection tmpSelSelection2 = new Selection();
                Selection tmpShowSelection2 = new Selection();
                tmpSelSelection2.setDataset(tmpLayer2.getDataset());
                tmpShowSelection2.setDataset(tmpLayer2.getDataset());
                tmpSelSelection2._LayerName = tmpLayer2.getLayerName();
                tmpShowSelection2._LayerName = tmpLayer2.getLayerName();
                tmpShowSelection2.setType(EGeoDisplayType.NORMAL);
                tmpSelSelection2.setType(EGeoDisplayType.SELECT);
                this._SelSelectionList.add(tmpSelSelection2);
                this._ShowSelectionList.add(tmpShowSelection2);
            }
        }
    }

    public Selection getSelectionByLayerID(String layerID, boolean isShow) {
        Selection tmpSel = getFeatureSelectionByLayerID(layerID, isShow);
        if (tmpSel != null) {
            return tmpSel;
        }
        Selection tmpSel2 = getVectorSelectionByLayerID(layerID, isShow);
        if (tmpSel2 != null) {
            return tmpSel2;
        }
        return null;
    }

    public Selection getFeatureSelectionByLayerID(String layerID, boolean isShow) {
        int tid = this._VectorGeoLayers.size();
        if (this._GeoLayers.size() > 0) {
            ListIterator<GeoLayer> tempIter01 = this._GeoLayers.getList().listIterator();
            while (tempIter01.hasNext()) {
                if (!tempIter01.next().getLayerID().equals(layerID)) {
                    tid++;
                } else if (isShow) {
                    return this._ShowSelectionList.get(tid);
                } else {
                    return this._SelSelectionList.get(tid);
                }
            }
        }
        return null;
    }

    public Selection getVectorSelectionByLayerID(String layerID, boolean isShow) {
        int tid = 0;
        if (this._VectorGeoLayers.size() > 0) {
            ListIterator<GeoLayer> tempIter01 = this._VectorGeoLayers.getList().listIterator();
            while (tempIter01.hasNext()) {
                if (!tempIter01.next().getLayerID().equals(layerID)) {
                    tid++;
                } else if (isShow) {
                    return this._ShowSelectionList.get(tid);
                } else {
                    return this._SelSelectionList.get(tid);
                }
            }
        }
        return null;
    }

    public Selection getSelectionByIndex(int layerIndex, boolean isShow) {
        if (layerIndex <= -1 || layerIndex >= this._ShowSelectionList.size()) {
            return null;
        }
        if (isShow) {
            return this._ShowSelectionList.get(layerIndex);
        }
        return this._SelSelectionList.get(layerIndex);
    }

    public Selection getFeatureSelectionByIndex(int layerIndex, boolean isShow) {
        int layerIndex2 = layerIndex + this._VectorGeoLayers.size();
        if (layerIndex2 <= -1 || layerIndex2 >= this._SelSelectionList.size()) {
            return null;
        }
        if (isShow) {
            return this._ShowSelectionList.get(layerIndex2);
        }
        return this._SelSelectionList.get(layerIndex2);
    }

    public Selection getSelection2(String layerID, boolean isShow) {
        int tid = 0;
        if (this._VectorGeoLayers.size() > 0) {
            ListIterator<GeoLayer> tempIter01 = this._VectorGeoLayers.getList().listIterator();
            while (tempIter01.hasNext()) {
                if (!tempIter01.next().getLayerID().equals(layerID)) {
                    tid++;
                } else if (isShow) {
                    return this._ShowSelectionList.get(tid);
                } else {
                    return this._SelSelectionList.get(tid);
                }
            }
        }
        if (this._GeoLayers.size() > 0) {
            ListIterator<GeoLayer> tempIter012 = this._GeoLayers.getList().listIterator();
            while (tempIter012.hasNext()) {
                if (!tempIter012.next().getLayerID().equals(layerID)) {
                    tid++;
                } else if (isShow) {
                    return this._ShowSelectionList.get(tid);
                } else {
                    return this._SelSelectionList.get(tid);
                }
            }
        }
        return null;
    }

    public List<Selection> getShowSelectionList() {
        return this._ShowSelectionList;
    }

    public List<Selection> getSelectSelectionList() {
        return this._SelSelectionList;
    }

    public void RefreshFast() {
        int tmpAllowMaxLayers;
        int tmpAllowMaxLayers2;
        if (this.m_AllowRfresh) {
            boolean tmpHasShutter = PubVar._MapView.m_ShutterTool.getIsShutterMode();
            try {
                this.VectorBitmap.eraseColor(0);
                this._Graphics = new Canvas(this.VectorBitmap);
                Canvas tmpShutterGraphics = null;
                if (tmpHasShutter) {
                    getShutterBitmap().eraseColor(0);
                    tmpShutterGraphics = new Canvas(this.ShutterBitmap);
                }
                if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                    tmpAllowMaxLayers = 5;
                } else {
                    tmpAllowMaxLayers = Integer.MAX_VALUE;
                }
                if (this._VectorGeoLayers.size() > 0) {
                    ListIterator<GeoLayer> tempIter01 = this._VectorGeoLayers.getList().listIterator();
                    while (tempIter01.hasNext()) {
                        tempIter01.next();
                    }
                    int tmpTid = 0;
                    int tmpLayersCount = this._VectorGeoLayers.size();
                    while (tempIter01.hasPrevious() && (tmpTid = tmpTid + 1) <= tmpAllowMaxLayers) {
                        GeoLayer tempGeoLayer = tempIter01.previous();
                        if (tempGeoLayer.getVisible() && tempGeoLayer.getMaxScale() >= ((double) getActualScale()) && tempGeoLayer.getMinScale() <= ((double) getActualScale())) {
                            int tid = tmpLayersCount - tmpTid;
                            Selection tmpShowSelection = getSelectionByIndex(tid, true);
                            if (tmpShowSelection != null && tmpShowSelection.getCount() > 0) {
                                tempGeoLayer.RefreshFast(this, tmpShowSelection);
                            }
                            Selection tmpSelSelection = getSelectionByIndex(tid, false);
                            if (tmpSelSelection != null && tmpSelSelection.getCount() > 0) {
                                tempGeoLayer.DrawSelection(tmpSelSelection, this, this._Graphics);
                            }
                            if (tmpShowSelection != null && tmpShowSelection.getCount() > 0 && tempGeoLayer.getRender().getIfLabel()) {
                                tempGeoLayer.DrawSelectionLabel(tmpShowSelection, this, this._Graphics, 0, 0);
                            }
                            if (tmpHasShutter && PubVar._MapView.m_ShutterTool.m_SelectLayers.contains(tempGeoLayer.getLayerID())) {
                                if (tmpShowSelection != null && tmpShowSelection.getCount() > 0) {
                                    tempGeoLayer.RefreshFast(this, tmpShutterGraphics, tmpShowSelection);
                                }
                                if (tmpSelSelection != null && tmpSelSelection.getCount() > 0) {
                                    tempGeoLayer.DrawSelection(tmpSelSelection, this, tmpShutterGraphics);
                                }
                                if (tmpShowSelection != null && tmpShowSelection.getCount() > 0 && tempGeoLayer.getRender().getIfLabel()) {
                                    tempGeoLayer.DrawSelectionLabel(tmpShowSelection, this, tmpShutterGraphics, 0, 0);
                                }
                            }
                        }
                    }
                }
                if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                    tmpAllowMaxLayers2 = 5;
                } else {
                    tmpAllowMaxLayers2 = Integer.MAX_VALUE;
                }
                if (this._GeoLayers.size() > 0) {
                    ListIterator<GeoLayer> tempIter02 = this._GeoLayers.getList().listIterator();
                    while (tempIter02.hasNext()) {
                        tempIter02.next();
                    }
                    int tmpTid2 = 0;
                    int tmpLayersCount2 = this._GeoLayers.size() + this._VectorGeoLayers.size();
                    while (tempIter02.hasPrevious() && (tmpTid2 = tmpTid2 + 1) <= tmpAllowMaxLayers2) {
                        GeoLayer tempGeoLayer2 = tempIter02.previous();
                        if (tempGeoLayer2.getVisible() && tempGeoLayer2.getMaxScale() >= ((double) getActualScale()) && tempGeoLayer2.getMinScale() <= ((double) getActualScale())) {
                            int tid2 = tmpLayersCount2 - tmpTid2;
                            Selection tmpShowSelection2 = getSelectionByIndex(tid2, true);
                            Selection tmpSelSelection2 = getSelectionByIndex(tid2, false);
                            if (tmpShowSelection2 != null && tmpShowSelection2.getCount() > 0) {
                                tempGeoLayer2.RefreshFast(this, tmpShowSelection2);
                            }
                            if (tmpHasShutter && PubVar._MapView.m_ShutterTool.m_SelectLayers.contains(tempGeoLayer2.getLayerID())) {
                                if (tmpShowSelection2 != null && tmpShowSelection2.getCount() > 0) {
                                    tempGeoLayer2.RefreshFast(this, tmpShutterGraphics, tmpShowSelection2);
                                }
                                if (tmpSelSelection2 != null && tmpSelSelection2.getCount() > 0) {
                                    tempGeoLayer2.DrawSelection(tmpSelSelection2, this, tmpShutterGraphics);
                                }
                                if (tmpShowSelection2 != null && tmpShowSelection2.getCount() > 0 && tempGeoLayer2.getRender().getIfLabel()) {
                                    tempGeoLayer2.DrawSelectionLabel(tmpShowSelection2, this, tmpShutterGraphics, 0, 0);
                                }
                            }
                            if (tmpSelSelection2 != null && tmpSelSelection2.getCount() > 0) {
                                tempGeoLayer2.DrawSelection(tmpSelSelection2, this, this._Graphics);
                            }
                            if (tmpShowSelection2 != null && tmpShowSelection2.getCount() > 0 && tempGeoLayer2.getRender().getIfLabel()) {
                                tempGeoLayer2.DrawSelectionLabel(tmpShowSelection2, this, this._Graphics, 0, 0);
                            }
                        }
                    }
                }
                try {
                    RefreshFast2();
                } catch (Exception e) {
                }
                if (PubVar._PubCommand.m_Magnifer != null && PubVar._PubCommand.m_Magnifer._IsVisible) {
                    PubVar._PubCommand.m_Magnifer.updateMapView();
                    PubVar._PubCommand.m_Magnifer.updateView();
                }
            } catch (Exception e2) {
                try {
                    RefreshFast2();
                } catch (Exception e3) {
                }
                if (PubVar._PubCommand.m_Magnifer != null && PubVar._PubCommand.m_Magnifer._IsVisible) {
                    PubVar._PubCommand.m_Magnifer.updateMapView();
                    PubVar._PubCommand.m_Magnifer.updateView();
                }
            } catch (Throwable th) {
                try {
                    RefreshFast2();
                } catch (Exception e4) {
                }
                if (PubVar._PubCommand.m_Magnifer != null && PubVar._PubCommand.m_Magnifer._IsVisible) {
                    PubVar._PubCommand.m_Magnifer.updateMapView();
                    PubVar._PubCommand.m_Magnifer.updateView();
                }
                throw th;
            }
        }
    }

    public void DrawUnRegisteInfo(Canvas pCanvas) {
        if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
            float tempX = (float) pCanvas.getWidth();
            float tempY = (float) pCanvas.getHeight();
            Paint.FontMetrics fontMetrics = this._TextBgPaint.getFontMetrics();
            float tempX2 = tempX / 2.0f;
            float tempY2 = (tempY / 2.0f) + (fontMetrics.bottom - fontMetrics.top) + 10.0f;
            pCanvas.drawText("试用版", tempX2 - 1.0f, tempY2, this._TextBgPaint);
            pCanvas.drawText("试用版", 1.0f + tempX2 + 1.0f, tempY2, this._TextBgPaint);
            pCanvas.drawText("试用版", tempX2, tempY2 - 1.0f, this._TextBgPaint);
            pCanvas.drawText("试用版", tempX2, 1.0f + tempY2 + 1.0f, this._TextBgPaint);
            pCanvas.drawText("试用版", 1.0f + tempX2, 1.0f + tempY2, this._TextBgPaint);
            pCanvas.drawText("试用版", tempX2 - 1.0f, tempY2 - 1.0f, this._TextBgPaint);
            pCanvas.drawText("试用版", 1.0f + tempX2, tempY2 - 1.0f, this._TextBgPaint);
            pCanvas.drawText("试用版", tempX2 - 1.0f, 1.0f + tempY2, this._TextBgPaint);
            pCanvas.drawText("试用版", tempX2, tempY2, this._TextPaint);
        }
    }

    public void DrawTextWithHola(Canvas pCanvas, String msg, float x, float y, String color) {
        Paint tmpPaint = getTxtPaint(color);
        if (tmpPaint != null) {
            pCanvas.drawText(msg, x - 1.0f, y, this._TextBgPaint);
            pCanvas.drawText(msg, 1.0f + x + 1.0f, y, this._TextBgPaint);
            pCanvas.drawText(msg, x, y - 1.0f, this._TextBgPaint);
            pCanvas.drawText(msg, x, 1.0f + y + 1.0f, this._TextBgPaint);
            pCanvas.drawText(msg, 1.0f + x, 1.0f + y, this._TextBgPaint);
            pCanvas.drawText(msg, x - 1.0f, y - 1.0f, this._TextBgPaint);
            pCanvas.drawText(msg, 1.0f + x, y - 1.0f, this._TextBgPaint);
            pCanvas.drawText(msg, x - 1.0f, 1.0f + y, this._TextBgPaint);
            pCanvas.drawText(msg, x, y, tmpPaint);
        }
    }

    public void DrawText(Canvas pCanvas, String msg, float x, float y, String color) {
        Paint tmpPaint = getTxtPaint(color);
        if (tmpPaint != null) {
            float tempBias = 5.0f * PubVar.ScaledDensity;
            Paint.FontMetrics fontMetrics = this._TextBgPaint.getFontMetrics();
            float tmpTxtsWidth = this._TextBgPaint.measureText(msg);
            pCanvas.drawRoundRect(new RectF((x - (tmpTxtsWidth / 2.0f)) - tempBias, (fontMetrics.top + y) - tempBias, (tmpTxtsWidth / 2.0f) + x + tempBias, fontMetrics.bottom + y + tempBias), 10.0f, 10.0f, this._TextBgPaint);
            pCanvas.drawText(msg, x, y, tmpPaint);
        }
    }

    private Paint getTxtPaint(String color) {
        int tmpColor;
        if (this.m_TxtPaintList.containsKey(color)) {
            return this.m_TxtPaintList.get(color);
        }
        new Paint();
        Paint tmpTxtPaint = new Paint();
        tmpTxtPaint.setAntiAlias(true);
        tmpTxtPaint.setTextSize(18.0f * PubVar.ScaledDensity);
        try {
            tmpColor = Color.parseColor(color);
        } catch (Exception e) {
            tmpColor = SupportMenu.CATEGORY_MASK;
        }
        tmpTxtPaint.setColor(tmpColor);
        tmpTxtPaint.setTypeface(Typeface.create("宋体", 0));
        tmpTxtPaint.setTextAlign(Paint.Align.CENTER);
        this.m_TxtPaintList.put(color, tmpTxtPaint);
        return tmpTxtPaint;
    }

    public void RefreshRasterLayers() {
        int tmpAllowMaxLayers;
        new Canvas(this.RasterBitmap).drawColor(PubVar.MapBgColor);
        boolean tmpHasShutter = PubVar._MapView.m_ShutterTool.getIsShutterMode();
        Canvas tmpShutterCanvas = null;
        if (tmpHasShutter) {
            tmpShutterCanvas = new Canvas(getShutterRasterBitmap());
            tmpShutterCanvas.drawColor(PubVar.MapBgColor);
        }
        if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
            tmpAllowMaxLayers = 1;
        } else {
            tmpAllowMaxLayers = Integer.MAX_VALUE;
        }
        if (this._RasterLayers != null && this._RasterLayers.size() > 0) {
            ListIterator<XLayer> tempIterRaster = this._RasterLayers.listIterator();
            while (tempIterRaster.hasNext()) {
                tempIterRaster.next();
            }
            int tmpTid = 0;
            while (tempIterRaster.hasPrevious() && (tmpTid = tmpTid + 1) <= tmpAllowMaxLayers) {
                try {
                    XLayer tempLayer = tempIterRaster.previous();
                    if (tempLayer != null) {
                        if (tmpHasShutter) {
                            tempLayer.SetDrawCanvas(this.RasterBitmap);
                            if (!tmpHasShutter) {
                                tempLayer.SetOtherDrawCanvas(null);
                            } else if (tempLayer.getLayerType() == ELayerType.RASTERFILE) {
                                if (PubVar._MapView.m_ShutterTool.m_SelectLayers.contains(((XRasterFileLayer) tempLayer).getFilePath())) {
                                    tempLayer.SetOtherDrawCanvas(tmpShutterCanvas);
                                } else {
                                    tempLayer.SetOtherDrawCanvas(null);
                                }
                            } else if (PubVar._MapView.m_ShutterTool.m_SelectLayers.contains(tempLayer.getName())) {
                                tempLayer.SetOtherDrawCanvas(tmpShutterCanvas);
                            } else {
                                tempLayer.SetOtherDrawCanvas(null);
                            }
                            tempLayer.Refresh(this);
                        } else if (tempLayer.getVisible()) {
                            tempLayer.SetDrawCanvas(this.RasterBitmap);
                            tempLayer.SetOtherDrawCanvas(null);
                            tempLayer.Refresh(this);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public void RefreshFastRasterLayers() {
        if (this.m_AllowRfresh) {
            this.RasterFastResfreshMode = false;
            RefreshRasterLayers();
            this.RasterFastResfreshMode = true;
            RefreshFast2();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0091, code lost:
        r4 = th;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0091 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:7:0x0047] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void RefreshFast2() {
        /*
            r10 = this;
            boolean r4 = r10.m_AllowRfresh
            if (r4 != 0) goto L_0x0005
        L_0x0004:
            return
        L_0x0005:
            r0 = 0
            android.graphics.Canvas r1 = new android.graphics.Canvas     // Catch:{ Exception -> 0x0082, all -> 0x008a }
            android.graphics.Bitmap r4 = r10.f448bp     // Catch:{ Exception -> 0x0082, all -> 0x008a }
            r1.<init>(r4)     // Catch:{ Exception -> 0x0082, all -> 0x008a }
            android.graphics.Rect r3 = new android.graphics.Rect     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            float r4 = r10.MaskBiasX     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            int r4 = (int) r4     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            int r4 = -r4
            float r5 = r10.MaskBiasY     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            int r5 = (int) r5     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            int r5 = -r5
            android.graphics.Bitmap r6 = r10.RasterBitmap     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            int r6 = r6.getWidth()     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            float r7 = r10.MaskBiasX     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            int r7 = (int) r7     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            int r6 = r6 + r7
            android.graphics.Bitmap r7 = r10.RasterBitmap     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            int r7 = r7.getHeight()     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            float r8 = r10.MaskBiasY     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            int r8 = (int) r8     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            int r7 = r7 + r8
            r3.<init>(r4, r5, r6, r7)     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            android.graphics.Bitmap r4 = r10.RasterBitmap     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            android.graphics.Rect r5 = new android.graphics.Rect     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            r6 = 0
            r7 = 0
            android.graphics.Bitmap r8 = r10.f448bp     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            int r8 = r8.getWidth()     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            android.graphics.Bitmap r9 = r10.f448bp     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            int r9 = r9.getHeight()     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            r5.<init>(r6, r7, r8, r9)     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
            r6 = 0
            r1.drawBitmap(r4, r3, r5, r6)     // Catch:{ Exception -> 0x009b, all -> 0x0091 }
        L_0x0047:
            android.graphics.Bitmap r4 = r10.VectorBitmap     // Catch:{ Exception -> 0x0099, all -> 0x0091 }
            r5 = 0
            r6 = 0
            r7 = 0
            r1.drawBitmap(r4, r5, r6, r7)     // Catch:{ Exception -> 0x0099, all -> 0x0091 }
        L_0x004f:
            android.graphics.Canvas r2 = new android.graphics.Canvas     // Catch:{ Exception -> 0x0097, all -> 0x0091 }
            android.graphics.Bitmap r4 = r10.MaskBitmap     // Catch:{ Exception -> 0x0097, all -> 0x0091 }
            r2.<init>(r4)     // Catch:{ Exception -> 0x0097, all -> 0x0091 }
            android.graphics.Bitmap r4 = r10.RasterBitmap     // Catch:{ Exception -> 0x0097, all -> 0x0091 }
            r5 = 0
            r6 = 0
            r7 = 0
            r2.drawBitmap(r4, r5, r6, r7)     // Catch:{ Exception -> 0x0097, all -> 0x0091 }
            android.graphics.Bitmap r4 = r10.f448bp     // Catch:{ Exception -> 0x0097, all -> 0x0091 }
            float r5 = r10.MaskBiasX     // Catch:{ Exception -> 0x0097, all -> 0x0091 }
            float r5 = -r5
            float r6 = r10.MaskBiasY     // Catch:{ Exception -> 0x0097, all -> 0x0091 }
            float r6 = -r6
            r7 = 0
            r2.drawBitmap(r4, r5, r6, r7)     // Catch:{ Exception -> 0x0097, all -> 0x0091 }
        L_0x006a:
             com.xzy.forestSystem.gogisapi.Carto.MapView r4 = com.xzy.forestSystem.PubVar._MapView     // Catch:{ Exception -> 0x0094, all -> 0x0091 }
             com.xzy.forestSystem.gogisapi.Carto.ShutterTool r4 = r4.m_ShutterTool     // Catch:{ Exception -> 0x0094, all -> 0x0091 }
            boolean r4 = r4.getIsShutterMode()     // Catch:{ Exception -> 0x0094, all -> 0x0091 }
            if (r4 == 0) goto L_0x007b
             com.xzy.forestSystem.gogisapi.Carto.MapView r4 = com.xzy.forestSystem.PubVar._MapView     // Catch:{ Exception -> 0x0094, all -> 0x0091 }
             com.xzy.forestSystem.gogisapi.Carto.ShutterTool r4 = r4.m_ShutterTool     // Catch:{ Exception -> 0x0094, all -> 0x0091 }
            r4.RefreshMap(r1)     // Catch:{ Exception -> 0x0094, all -> 0x0091 }
        L_0x007b:
             com.xzy.forestSystem.gogisapi.Carto.MapView r4 = r10._DrawPicture
            r4.invalidate()
            r0 = r1
            goto L_0x0004
        L_0x0082:
            r4 = move-exception
        L_0x0083:
             com.xzy.forestSystem.gogisapi.Carto.MapView r4 = r10._DrawPicture
            r4.invalidate()
            goto L_0x0004
        L_0x008a:
            r4 = move-exception
        L_0x008b:
             com.xzy.forestSystem.gogisapi.Carto.MapView r5 = r10._DrawPicture
            r5.invalidate()
            throw r4
        L_0x0091:
            r4 = move-exception
            r0 = r1
            goto L_0x008b
        L_0x0094:
            r4 = move-exception
            r0 = r1
            goto L_0x0083
        L_0x0097:
            r4 = move-exception
            goto L_0x006a
        L_0x0099:
            r4 = move-exception
            goto L_0x004f
        L_0x009b:
            r4 = move-exception
            goto L_0x0047
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.gogisapi.Carto.Map.RefreshFast2():void");
    }

    public void Refresh() {
        Selection tmpShowSelection;
        Selection tmpShowSelection2;
        if (this.m_AllowRfresh) {
            try {
                Envelope tmpCurrentExtend = getExtend();
                boolean tmpIsAddExtend = true;
                int tmpMapExtCount = this._MapExtendList.size();
                if (tmpMapExtCount > 0 && this._MapExtendList.get(tmpMapExtCount - 1).EqualWithBias(tmpCurrentExtend, 0.05d)) {
                    tmpIsAddExtend = false;
                }
                if (tmpIsAddExtend) {
                    this._MapExtendList.add(tmpCurrentExtend);
                }
                if (this._MapExtendList.size() > 10) {
                    this._MapExtendList.remove(0);
                }
                if (this._ScaleBar != null) {
                    this._ScaleBar.RefreshScalBar(this);
                }
                RefreshRasterLayers();
                ClearShowSelections();
                int tid = 0;
                for (GeoLayer layer : this._VectorGeoLayers.getList()) {
                    if (layer.getVisible() && layer.getMaxScale() >= ((double) getActualScale()) && layer.getMinScale() <= ((double) getActualScale()) && (tmpShowSelection2 = getSelectionByIndex(tid, true)) != null) {
                        layer.Refresh(this, tmpShowSelection2);
                    }
                    tid++;
                }
                int tid2 = 0;
                for (GeoLayer layer2 : this._GeoLayers.getList()) {
                    if (layer2.getVisible() && layer2.getMaxScale() >= ((double) getActualScale()) && layer2.getMinScale() <= ((double) getActualScale()) && (tmpShowSelection = getFeatureSelectionByIndex(tid2, true)) != null) {
                        layer2.Refresh(this, tmpShowSelection);
                    }
                    tid2++;
                }
            } catch (Exception e) {
            } finally {
                RefreshFast();
            }
            PubVar._PubCommand.UpdateLockChildMap();
        }
    }

    public void ClearSelection() {
        for (Selection selection : this._SelSelectionList) {
            selection.RemoveAll();
        }
    }

    public void ClearAllSelections() {
        for (Selection selection : this._SelSelectionList) {
            selection.RemoveAll();
        }
        for (Selection selection2 : this._ShowSelectionList) {
            selection2.RemoveAll();
        }
    }

    public void ClearShowSelections() {
        for (Selection selection : this._ShowSelectionList) {
            selection.RemoveAll();
        }
    }

    public void ClearSelSelection() {
        for (Selection selection : this._SelSelectionList) {
            selection.RemoveAll();
        }
    }

    public void UpdateExtendForView(Envelope paramEnvelope) {
        if (paramEnvelope != null) {
            if (this._ExtendForView == null) {
                this._ExtendForView = new Envelope(paramEnvelope.getLeftTop().Clone(), paramEnvelope.getRightBottom().Clone());
            } else {
                this._ExtendForView = this._ExtendForView.MergeEnvelope(paramEnvelope);
            }
        }
    }

    public XLayer GetRasterLayerByID(String layerID) {
        for (XLayer tempLayer : this._RasterLayers) {
            if (tempLayer != null && tempLayer.getName().equals(layerID)) {
                return tempLayer;
            }
        }
        return null;
    }

    public void MoveRasterLayerTo(String layerID, int layerIndex) {
        XLayer tempLayer = GetRasterLayerByID(layerID);
        if (tempLayer != null) {
            this._RasterLayers.remove(tempLayer);
            this._RasterLayers.add(layerIndex, tempLayer);
        }
    }

    public void RemoveRasterLayer(String layerID) {
        XLayer tempLayer = GetRasterLayerByID(layerID);
        if (tempLayer != null) {
            this._RasterLayers.remove(tempLayer);
        }
    }

    public GeoLayer GetGeoLayerByName(String layerID) {
        for (GeoLayer localGeoLayer : this._GeoLayers.getList()) {
            if (localGeoLayer != null && localGeoLayer.getName().indexOf(layerID) >= 0) {
                return localGeoLayer;
            }
        }
        return null;
    }

    public GeoLayer GetGeoLayerByLayerName(String LayerName) {
        for (GeoLayer localGeoLayer : this._GeoLayers.getList()) {
            if (localGeoLayer != null && localGeoLayer.getLayerName().equals(LayerName)) {
                return localGeoLayer;
            }
        }
        return null;
    }

    public GeoLayer GetGeoLayerByDataSource(String dataSourceName, String layerID) {
        DataSet pDataset;
        DataSource tempDataSource = PubVar.m_Workspace.GetDataSourceByName(dataSourceName);
        if (tempDataSource == null || (pDataset = tempDataSource.GetDatasetByName(layerID)) == null) {
            return null;
        }
        return pDataset.getBindGeoLayer();
    }

    public void UpdateBKVectorLayersVisibleList() {
        this._BKVectorLayersViList = new ArrayList();
        for (GeoLayer geoLayer : this._VectorGeoLayers.getList()) {
            this._BKVectorLayersViList.add(Boolean.valueOf(geoLayer.getVisible()));
        }
    }

    public List<Boolean> getBKVectorLayersVisibleList() {
        if (this._BKVectorLayersViList == null) {
            UpdateBKVectorLayersVisibleList();
        }
        return this._BKVectorLayersViList;
    }

    public void UpdateRasterLayersVisibleList() {
        this._RasterLayersViList.clear();
        for (XLayer xLayer : this._RasterLayers) {
            this._RasterLayersViList.add(Boolean.valueOf(xLayer.getVisible()));
        }
    }

    public List<Boolean> getRasterLayersVisibleList() {
        if (this._RasterLayersViList == null) {
            UpdateRasterLayersVisibleList();
        }
        return this._RasterLayersViList;
    }

    public Bitmap getMaskBitmap() {
        return this.MaskBitmap;
    }

    public Bitmap getRasterBitmap() {
        return this.RasterBitmap;
    }

    public Bitmap getShutterBitmap() {
        if (this.ShutterBitmap == null) {
            this.ShutterBitmap = Bitmap.createBitmap(getSize().getWidth(), getSize().getHeight(), Bitmap.Config.ARGB_4444);
        }
        return this.ShutterBitmap;
    }

    public Bitmap getShutterRasterBitmap() {
        if (this.ShutterRasterBitmap == null) {
            this.ShutterRasterBitmap = Bitmap.createBitmap(getSize().getWidth(), getSize().getHeight(), Bitmap.Config.ARGB_4444);
        }
        return this.ShutterRasterBitmap;
    }

    public Point MapToScreen(double paramDouble1, double paramDouble2) {
        PointF localPointF = MapToScreenF(paramDouble1, paramDouble2);
        return new Point((int) localPointF.x, (int) localPointF.y);
    }

    public Point MapToScreen(Coordinate tmpCoord) {
        return MapToScreen(tmpCoord.getX(), tmpCoord.getY());
    }

    public PointF MapToScreenF(double x, double y) {
        return new PointF((float) ((x - (this._Center.getX() - (getMapWidth() * 0.5d))) * this._ScaleZoom), (float) (((this._Center.getY() + (getMapHeight() * 0.5d)) - y) * this._ScaleZoom));
    }

    public double MapToScreenDistance(double distance) {
        return this._ScaleZoom * distance;
    }

    public Coordinate ScreenToMap(float paramFloat1, float paramFloat2) {
        if (getExtend() == null) {
            return null;
        }
        return new Coordinate(getExtend().getMinX() + (((double) paramFloat1) * getZoomScale()), getExtend().getMaxY() - (((double) paramFloat2) * getZoomScale()));
    }

    public Coordinate ScreenToMap(int paramInt1, int paramInt2) {
        return ScreenToMap((float) paramInt1, (float) paramInt2);
    }

    public Coordinate ScreenToMap(Point paramPoint) {
        return ScreenToMap(paramPoint.x, paramPoint.y);
    }

    public Coordinate ScreenToMap(PointF paramPointF) {
        return ScreenToMap(paramPointF.x, paramPointF.y);
    }

    public Coordinate getCenter() {
        return this._Center;
    }

    public Envelope getExtend() {
        if (this._Extend == null) {
            this._Extend = getFullExtend();
            this._Center = this._Extend.getCenter();
        }
        return this._Extend;
    }

    public Envelope getFullExtend() {
        if (this._FullExtend == null) {
            this._FullExtend = new Envelope(7850000.0d, 5650000.0d, 1.48E7d, 1570000.0d);
        }
        return this._FullExtend;
    }

    public Size getSize() {
        return this._Size;
    }

    public List<Point[]> GetExtendVector() {
        if (this.extendVector == null) {
            this.extendVector = new ArrayList();
            this.extendVector.add(new Point[]{new Point(0, 0), new Point(this._Size.getWidth(), 0)});
            this.extendVector.add(new Point[]{new Point(this._Size.getWidth(), 0), new Point(this._Size.getWidth(), this._Size.getHeight())});
            this.extendVector.add(new Point[]{new Point(this._Size.getWidth(), this._Size.getHeight()), new Point(0, this._Size.getHeight())});
            this.extendVector.add(new Point[]{new Point(0, this._Size.getHeight()), new Point(0, 0)});
        }
        return this.extendVector;
    }

    public double getZoom() {
        return this._Zoom;
    }

    public double getZoomScale() {
        return this._ZoomScale;
    }

    public double getScaleZoom() {
        return this._ScaleZoom;
    }

    private void calActualScale() {
        this._ActualScale = (int) ((((double) PubVar.ScreenXDPI) * this._ZoomScale) / 0.0254d);
    }

    public int getActualScale() {
        return this._ActualScale;
    }

    public void setCenter(Coordinate tmpCoord) {
        this._Center = tmpCoord;
    }

    public void setExtend(Envelope paramEnvelope) {
        if (paramEnvelope != null) {
            this._Center = paramEnvelope.getCenter();
            if (paramEnvelope.getHeight() > 0.0d && paramEnvelope.getWidth() > 0.0d) {
                if (paramEnvelope.getHeight() / paramEnvelope.getWidth() >= ((double) (this._Size.getHeight() / this._Size.getWidth()))) {
                    setZoom(paramEnvelope.getHeight());
                } else {
                    setZoom((paramEnvelope.getWidth() * ((double) this._Size.getHeight())) / ((double) this._Size.getWidth()));
                }
            }
            CalExtend();
        }
    }

    public void setFullExtend(Envelope paramEnvelope) {
        this._FullExtend = paramEnvelope;
        setZoom(this._FullExtend.getHeight());
        setCenter(this._FullExtend.getCenter());
    }

    public void setSize(Size mapSize) {
        Dispose();
        this._Size = mapSize;
        this._ZoomScale = this._Zoom / ((double) this._Size.getHeight());
        this._ScaleZoom = 1.0d / this._ZoomScale;
        calActualScale();
        if (this._Size.getWidth() != 0 && this._Size.getHeight() != 0) {
            int tmpWidth = getSize().getWidth();
            int tmpHeight = getSize().getHeight();
            this.f448bp = Bitmap.createBitmap(tmpWidth, tmpHeight, Bitmap.Config.ARGB_8888);
            int abs = (int) ((Math.abs(this.MaskBiasX) + 256.0f) / 256.0f);
            XBaseTilesLayer.MaskBiasXIndex = abs;
            XRasterFileLayer.MaskBiasXIndex = abs;
            int abs2 = (int) ((Math.abs(this.MaskBiasY) + 256.0f) / 256.0f);
            XBaseTilesLayer.MaskBiasYIndex = abs2;
            XRasterFileLayer.MaskBiasYIndex = abs2;
            this.MaskBitmap = Bitmap.createBitmap(tmpWidth - (((int) this.MaskBiasX) * 2), tmpHeight - (((int) this.MaskBiasY) * 2), Bitmap.Config.RGB_565);
            this.RasterBitmap = Bitmap.createBitmap(this.MaskBitmap, 0, 0, this.MaskBitmap.getWidth(), this.MaskBitmap.getHeight());
            this.VectorBitmap = Bitmap.createBitmap(tmpWidth, tmpHeight, Bitmap.Config.ARGB_4444);
            this._DrawPicture.setImageBitmap(this.f448bp);
            this._Graphics = new Canvas(this.VectorBitmap);
        }
    }

    public void setZoom(double paramDouble) {
        this._Zoom = paramDouble;
        this._ZoomScale = this._Zoom / ((double) this._Size.getHeight());
        this._ScaleZoom = 1.0d / this._ZoomScale;
        CalExtend();
        calActualScale();
    }

    public void setZoomScale(double ZoomScale) {
        this._ZoomScale = ZoomScale;
        this._Zoom = this._ZoomScale * ((double) this._Size.getHeight());
        this._ScaleZoom = 1.0d / this._ZoomScale;
        calActualScale();
    }

    public void setActualScale(int ActualScale) {
        if (ActualScale > 0) {
            this._ActualScale = ActualScale;
            this._ZoomScale = (((double) this._ActualScale) * 0.0254d) / ((double) PubVar.ScreenXDPI);
            this._Zoom = this._ZoomScale * ((double) this._Size.getHeight());
            this._ScaleZoom = 1.0d / this._ZoomScale;
            CalExtend();
        }
    }

    private List<Coordinate> ClipSide(List<Coordinate> CoorList, int WhichSide) {
        List<Coordinate> list = new ArrayList<>();
        if (CoorList.size() != 0) {
            Coordinate pt = CoorList.get(CoorList.size() - 1);
            int num = InnerSide(pt, WhichSide);
            for (Coordinate coordinate2 : CoorList) {
                if (InnerSide(coordinate2, WhichSide) == 0) {
                    if (num == 1) {
                        num = 0;
                        list.add(GetSideIntersect(coordinate2, pt, WhichSide));
                    }
                    list.add(coordinate2);
                } else if (num == 0) {
                    num = 1;
                    list.add(GetSideIntersect(coordinate2, pt, WhichSide));
                }
                pt = coordinate2;
            }
        }
        return list;
    }

    private int Codec(double X, double Y) {
        int num = 0;
        if (Y > getClipExtendMaxY()) {
            num = 0 | 8;
        } else if (Y < getClipExtendMinY()) {
            num = 0 | 4;
        }
        if (X > getClipExtendMaxX()) {
            return num | 2;
        }
        if (X < getClipExtendMinX()) {
            num |= 1;
        }
        return num;
    }

    private Coordinate GetSideIntersect(Coordinate tmpCoord1, Coordinate tmpCoord2, int paramInt) {
        double d1 = tmpCoord1.getX();
        double d2 = tmpCoord1.getY();
        double d3 = tmpCoord2.getX();
        double d4 = tmpCoord2.getY();
        double d5 = 0.0d;
        double d6 = 0.0d;
        switch (paramInt) {
            case 1:
                d5 = getClipExtendMinX();
                d6 = d2 + (((d5 - d1) / (d3 - d1)) * (d4 - d2));
                break;
            case 2:
                d6 = getClipExtendMaxY();
                d5 = d1 + (((d6 - d2) / (d4 - d2)) * (d3 - d1));
                break;
            case 3:
                d5 = getClipExtendMaxX();
                d6 = d2 + (((d5 - d1) / (d3 - d1)) * (d4 - d2));
                break;
            case 4:
                d6 = getClipExtendMinY();
                d5 = d1 + (((d6 - d2) / (d4 - d2)) * (d3 - d1));
                break;
        }
        return new Coordinate(d5, d6);
    }

    private int InnerSide(Coordinate Pt, int WhichSide) {
        switch (WhichSide) {
            case 1:
                return Pt.getX() >= getExtend().getMinX() ? 0 : 1;
            case 2:
                return Pt.getY() <= getExtend().getMaxY() ? 0 : 1;
            case 3:
                return Pt.getX() <= getExtend().getMaxX() ? 0 : 1;
            case 4:
                return Pt.getY() >= getExtend().getMinY() ? 0 : 1;
            default:
                return -1;
        }
    }

    private Envelope getClipExtend() {
        Envelope tmpExtend = getExtend().Clone();
        tmpExtend.SetExtendSize(10.0d * getZoomScale());
        return tmpExtend;
    }

    private double getClipExtendMaxX() {
        return getExtend().getMaxX() + (10.0d * getZoomScale());
    }

    private double getClipExtendMaxY() {
        return getExtend().getMaxY() + (10.0d * getZoomScale());
    }

    private double getClipExtendMinX() {
        return getExtend().getMinX() - (10.0d * getZoomScale());
    }

    private double getClipExtendMinY() {
        return getExtend().getMinY() - (10.0d * getZoomScale());
    }

    private double getMapHeight() {
        return this._Zoom;
    }

    private double getMapWidth() {
        return getZoomScale() * ((double) this._Size.getWidth());
    }

    public Envelope CalExtend() {
        Envelope localEnvelope = new Envelope(this._Center.getX() - (getMapWidth() * 0.5d), this._Center.getY() + (getMapHeight() * 0.5d), this._Center.getX() + (getMapWidth() * 0.5d), this._Center.getY() - (getMapHeight() * 0.5d));
        this._Extend = localEnvelope;
        return localEnvelope;
    }

    public Point[] ClipPolygon(List<Coordinate> _MapCoorList) {
        return ClipPolygon(_MapCoorList, 0, 0);
    }

    public Point[] ClipPolygon(List<Coordinate> _MapCoorList, int deltX, int deltY) {
        Point[] pointArray = null;
        try {
            List<Coordinate> coorList = ClipSide(_MapCoorList, 1);
            List<Coordinate> list2 = ClipSide(coorList, 2);
            List<Coordinate> list3 = ClipSide(list2, 3);
            List<Coordinate> list4 = ClipSide(list3, 4);
            coorList.clear();
            list2.clear();
            list3.clear();
            pointArray = new Point[list4.size()];
            int num = 0;
            for (Coordinate coordinate : list4) {
                Point localPoint = MapToScreen(coordinate);
                num++;
                pointArray[num] = new Point(localPoint.x + deltX, localPoint.y + deltY);
            }
            list4.clear();
        } catch (Exception e) {
        }
        return pointArray;
    }

    public List<Point[]> ClipPolyline2(List<Coordinate> paramList, int paramInt1, int paramInt2) {
        List<Point[]> result = new ArrayList<>();
        int tempCount = paramList.size();
        new Coordinate();
        new Coordinate();
        Point lastPoint = null;
        List<Point> tempPoints = new ArrayList<>();
        for (int tempI = 1; tempI < tempCount; tempI++) {
            Coordinate tempClipCoord01 = new Coordinate();
            Coordinate tempClipCoord02 = new Coordinate();
            if (Clipline(paramList.get(tempI - 1), paramList.get(tempI), tempClipCoord01, tempClipCoord02)) {
                Point localPoint1 = MapToScreen(tempClipCoord01);
                localPoint1.x += paramInt1;
                localPoint1.y += paramInt2;
                Point localPoint2 = MapToScreen(tempClipCoord02);
                localPoint2.x += paramInt1;
                localPoint2.y += paramInt2;
                if (lastPoint == null) {
                    lastPoint = localPoint1;
                }
                if (localPoint1.x == lastPoint.x && localPoint1.y == lastPoint.y) {
                    tempPoints.add(localPoint1);
                    lastPoint = localPoint2;
                } else {
                    if (lastPoint != null) {
                        tempPoints.add(lastPoint);
                    }
                    if (tempPoints.size() > 0) {
                        result.add((Point[]) tempPoints.toArray(new Point[0]));
                    }
                    tempPoints.clear();
                    tempPoints = new ArrayList<>();
                    tempPoints.add(localPoint1);
                    lastPoint = localPoint2;
                }
            }
        }
        if (lastPoint != null) {
            tempPoints.add(lastPoint);
        }
        if (tempPoints.size() > 0) {
            result.add((Point[]) tempPoints.toArray(new Point[0]));
        }
        return result;
    }

    public int CalEnCode(double x, double y, Envelope extend) {
        int i = 0;
        if (y > extend.getMaxY()) {
            i = 8;
            if (x > extend.getMaxX()) {
                i = 8 | 2;
            }
        }
        if (x >= extend.getMinX()) {
            return i;
        }
        int i2 = 0;
        if (y < extend.getMinY()) {
            i2 = 4;
        }
        return i2 | 1;
    }

    public boolean ClipLine(Coordinate startCoord, Coordinate endCoord, Envelope extend, Coordinate outStartCoord, Coordinate outEndCoord) {
        int tempIndex01 = CalEnCode(startCoord.getX(), startCoord.getY(), extend);
        int tempIndex02 = CalEnCode(endCoord.getX(), endCoord.getY(), extend);
        if (tempIndex01 == 0 && tempIndex02 == 0) {
            outStartCoord.CopyFrom(startCoord);
            outEndCoord.CopyFrom(endCoord);
            return true;
        }
        if ((tempIndex01 & tempIndex02) != 0) {
            if (tempIndex01 == 0) {
                if (calOneInsideIntersect(tempIndex02, startCoord, endCoord, extend, outEndCoord)) {
                    outStartCoord.CopyFrom(startCoord);
                    return true;
                }
            } else if (tempIndex02 != 0) {
                boolean hasCoord = false;
                double[] coords = new double[4];
                double[] tempCoords = new double[2];
                if (SpatialRelation.Cal2LineSegIntersectPoint(startCoord.getX(), startCoord.getY(), endCoord.getX(), endCoord.getY(), extend.getMinX(), extend.getMinY(), extend.getMaxX(), extend.getMinY(), tempCoords)) {
                    coords[0] = tempCoords[0];
                    coords[1] = tempCoords[1];
                    hasCoord = true;
                }
                if (SpatialRelation.Cal2LineSegIntersectPoint(startCoord.getX(), startCoord.getY(), endCoord.getX(), endCoord.getY(), extend.getMaxX(), extend.getMinY(), extend.getMaxX(), extend.getMaxY(), tempCoords)) {
                    if (!hasCoord) {
                        coords[0] = tempCoords[0];
                        coords[1] = tempCoords[1];
                        hasCoord = true;
                    } else {
                        coords[2] = tempCoords[0];
                        coords[3] = tempCoords[1];
                        SpatialRelation.ArrangLineDirection(startCoord.getX(), startCoord.getY(), endCoord.getX(), endCoord.getY(), coords);
                        outStartCoord.setX(coords[0]);
                        outStartCoord.setY(coords[1]);
                        outEndCoord.setX(coords[2]);
                        outEndCoord.setY(coords[3]);
                        return true;
                    }
                }
                if (SpatialRelation.Cal2LineSegIntersectPoint(startCoord.getX(), startCoord.getY(), endCoord.getX(), endCoord.getY(), extend.getMinX(), extend.getMaxY(), extend.getMaxX(), extend.getMaxY(), tempCoords)) {
                    if (!hasCoord) {
                        coords[0] = tempCoords[0];
                        coords[1] = tempCoords[1];
                        hasCoord = true;
                    } else {
                        coords[2] = tempCoords[0];
                        coords[3] = tempCoords[1];
                        SpatialRelation.ArrangLineDirection(startCoord.getX(), startCoord.getY(), endCoord.getX(), endCoord.getY(), coords);
                        outStartCoord.setX(coords[0]);
                        outStartCoord.setY(coords[1]);
                        outEndCoord.setX(coords[2]);
                        outEndCoord.setY(coords[3]);
                        return true;
                    }
                }
                if (SpatialRelation.Cal2LineSegIntersectPoint(startCoord.getX(), startCoord.getY(), endCoord.getX(), endCoord.getY(), extend.getMinX(), extend.getMinY(), extend.getMinX(), extend.getMaxY(), tempCoords)) {
                    if (!hasCoord) {
                        coords[0] = tempCoords[0];
                        coords[1] = tempCoords[1];
                    } else {
                        coords[2] = tempCoords[0];
                        coords[3] = tempCoords[1];
                        SpatialRelation.ArrangLineDirection(startCoord.getX(), startCoord.getY(), endCoord.getX(), endCoord.getY(), coords);
                        outStartCoord.setX(coords[0]);
                        outStartCoord.setY(coords[1]);
                        outEndCoord.setX(coords[2]);
                        outEndCoord.setY(coords[3]);
                        return true;
                    }
                }
            } else if (calOneInsideIntersect(tempIndex01, endCoord, startCoord, extend, outStartCoord)) {
                outEndCoord.CopyFrom(endCoord);
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean calOneInsideIntersect(int outsidePtnCode, Coordinate startCoord, Coordinate endCoord, Envelope extend, Coordinate outCoord) {
        int tempSide = -1;
        if (outsidePtnCode == 1) {
            tempSide = 0;
        } else if (outsidePtnCode == 4) {
            tempSide = 1;
        } else if (outsidePtnCode != 2) {
            if (outsidePtnCode == 8) {
                tempSide = 3;
            } else if ((endCoord.getY() - startCoord.getY()) / (endCoord.getX() - startCoord.getX()) > 0.0d) {
                double[] tempcoords = new double[2];
                if (SpatialRelation.Cal2LineSegIntersectPoint(startCoord.getX(), startCoord.getY(), endCoord.getX(), endCoord.getY(), extend.getMinX(), extend.getMinY(), extend.getMinX(), extend.getMaxY(), tempcoords)) {
                    outCoord.setX(tempcoords[0]);
                    outCoord.setY(tempcoords[1]);
                    return true;
                } else if (SpatialRelation.Cal2LineSegIntersectPoint(startCoord.getX(), startCoord.getY(), endCoord.getX(), endCoord.getY(), extend.getMinX(), extend.getMaxY(), extend.getMaxX(), extend.getMaxY(), tempcoords)) {
                    outCoord.setX(tempcoords[0]);
                    outCoord.setY(tempcoords[1]);
                    return true;
                }
            } else {
                double[] tempcoords2 = new double[2];
                if (SpatialRelation.Cal2LineSegIntersectPoint(startCoord.getX(), startCoord.getY(), endCoord.getX(), endCoord.getY(), extend.getMinX(), extend.getMinY(), extend.getMaxX(), extend.getMinY(), tempcoords2)) {
                    outCoord.setX(tempcoords2[0]);
                    outCoord.setY(tempcoords2[1]);
                    return true;
                } else if (SpatialRelation.Cal2LineSegIntersectPoint(startCoord.getX(), startCoord.getY(), endCoord.getX(), endCoord.getY(), extend.getMaxX(), extend.getMinY(), extend.getMaxX(), extend.getMaxY(), tempcoords2)) {
                    outCoord.setX(tempcoords2[0]);
                    outCoord.setY(tempcoords2[1]);
                    return true;
                }
            }
        }
        if (tempSide != -1) {
            double tempL02X01 = 0.0d;
            double tempL02Y01 = 0.0d;
            double tempL02X02 = 0.0d;
            double tempL02Y02 = 0.0d;
            if (tempSide == 0) {
                tempL02X01 = extend.getMinX();
                tempL02Y01 = extend.getMinY();
                tempL02X02 = extend.getMinX();
                tempL02Y02 = extend.getMaxY();
            } else if (tempSide == 1) {
                tempL02X01 = extend.getMinX();
                tempL02Y01 = extend.getMinY();
                tempL02X02 = extend.getMaxX();
                tempL02Y02 = extend.getMinY();
            } else if (tempSide == 2) {
                tempL02X01 = extend.getMaxX();
                tempL02Y01 = extend.getMinY();
                tempL02X02 = extend.getMaxX();
                tempL02Y02 = extend.getMaxY();
            } else if (tempSide == 3) {
                tempL02X01 = extend.getMinX();
                tempL02Y01 = extend.getMaxY();
                tempL02X02 = extend.getMaxX();
                tempL02Y02 = extend.getMaxY();
            }
            double[] tempcoords3 = new double[2];
            if (SpatialRelation.Cal2LineSegIntersectPoint(startCoord.getX(), startCoord.getY(), endCoord.getX(), endCoord.getY(), tempL02X01, tempL02Y01, tempL02X02, tempL02Y02, tempcoords3)) {
                outCoord.setX(tempcoords3[0]);
                outCoord.setY(tempcoords3[1]);
                return true;
            }
        }
        return false;
    }

    public Point[] ClipPolyline(List<Coordinate> _MapCoorList, List<Integer> returnPtnIndex, int deltX, int deltY) {
        int tid;
        List<Point> list = new ArrayList<>();
        int count = _MapCoorList.size() - 1;
        int tid2 = 0;
        Point lastPtn = null;
        for (int i = 0; i < count; i++) {
            Coordinate tmpCoord01 = new Coordinate();
            Coordinate tmpCoord02 = new Coordinate();
            if (Clipline(_MapCoorList.get(i), _MapCoorList.get(i + 1), tmpCoord01, tmpCoord02)) {
                Point tmpPtn01 = MapToScreen(tmpCoord01);
                tmpPtn01.x += deltX;
                tmpPtn01.y += deltY;
                Point tmpPtn02 = MapToScreen(tmpCoord02);
                tmpPtn02.x += deltX;
                tmpPtn02.y += deltY;
                if (lastPtn == null) {
                    lastPtn = tmpPtn01;
                }
                if (tmpPtn01.x == lastPtn.x && tmpPtn01.y == lastPtn.y) {
                    list.add(tmpPtn01);
                    tid = tid2 + 1;
                } else {
                    returnPtnIndex.add(Integer.valueOf(tid2));
                    list.add(tmpPtn01);
                    tid = tid2 + 1;
                }
                list.add(tmpPtn02);
                tid2 = tid + 1;
                lastPtn = tmpPtn02;
            }
        }
        if (returnPtnIndex.size() == 0) {
            returnPtnIndex.add(Integer.valueOf(tid2));
        } else if (returnPtnIndex.get(returnPtnIndex.size() - 1).intValue() != tid2) {
            returnPtnIndex.add(Integer.valueOf(tid2));
        }
        return (Point[]) list.toArray(new Point[0]);
    }

    public Point[] ClipPolyline(List<Coordinate> _MapCoorList, int deltX, int deltY) {
        List<Point> list = new ArrayList<>();
        int num = -1;
        try {
            int count = _MapCoorList.size();
            Coordinate coordinate = new Coordinate();
            Coordinate coordinate2 = new Coordinate();
            double tempExtendSide = this._ZoomScale * 10.0d;
            Coordinate pt = ScreenToMap(new Point(0, 0));
            Coordinate coordinate6 = ScreenToMap(new Point(this._Size.getWidth(), 0));
            Coordinate coordinate7 = ScreenToMap(new Point(this._Size.getWidth(), this._Size.getHeight()));
            Coordinate coordinate8 = ScreenToMap(new Point(0, this._Size.getHeight()));
            coordinate7.getX();
            coordinate7.getY();
            pt.getX();
            pt.getY();
            Point point3 = null;
            int tmpLastCorderIndex = -4;
            for (int i = 0; i < count - 1; i++) {
                if (Clipline(_MapCoorList.get(i), _MapCoorList.get(i + 1), coordinate, coordinate2)) {
                    Point item = MapToScreen(coordinate);
                    item.x += deltX;
                    item.y += deltY;
                    Point point2 = MapToScreen(coordinate2);
                    point2.x += deltX;
                    point2.y += deltY;
                    if (point3 == null) {
                        point3 = point2;
                    }
                    if (item.x == point3.x && item.y == point3.y) {
                        list.add(point2);
                    } else {
                        tmpLastCorderIndex = -4;
                        list.add(item);
                        list.add(point2);
                    }
                    point3 = point2;
                    if (num == 1 && list.size() >= 3) {
                        Point screenCoor = list.get(list.size() - 2);
                        Point point5 = list.get(list.size() - 3);
                        if (!(screenCoor.x == point5.x || screenCoor.y == point5.y)) {
                            Coordinate coordinate3 = ScreenToMap(screenCoor);
                            Coordinate coordinate4 = ScreenToMap(point5);
                            Envelope envelope = new Envelope(Math.min(coordinate3.getX(), coordinate4.getX()), Math.max(coordinate3.getY(), coordinate4.getY()), Math.max(coordinate3.getX(), coordinate4.getX()), Math.min(coordinate3.getY(), coordinate4.getY()));
                            double x = -1.0d;
                            double y = -1.0d;
                            int tmpCorder = -4;
                            if (envelope.ContainsPoint(pt)) {
                                x = getClipExtendMinX();
                                y = getClipExtendMaxY();
                                list.add(list.size() - 2, MapToScreen(new Coordinate(x, y)));
                                tmpCorder = 0;
                            } else if (envelope.ContainsPoint(coordinate6)) {
                                x = getClipExtendMaxX();
                                y = getClipExtendMaxY();
                                list.add(list.size() - 2, MapToScreen(new Coordinate(x, y)));
                                tmpCorder = 1;
                            } else if (envelope.ContainsPoint(coordinate7)) {
                                x = getClipExtendMaxX();
                                y = getClipExtendMinY();
                                list.add(list.size() - 2, MapToScreen(new Coordinate(x, y)));
                                tmpCorder = 2;
                            } else if (envelope.ContainsPoint(coordinate8)) {
                                x = getClipExtendMinX();
                                y = getClipExtendMinY();
                                list.add(list.size() - 2, MapToScreen(new Coordinate(x, y)));
                                tmpCorder = 3;
                            }
                            if (!(tmpCorder + tmpLastCorderIndex > 0 && (tmpCorder + tmpLastCorderIndex) % 2 == 0 && tmpCorder == 0)) {
                            }
                            tmpLastCorderIndex = tmpCorder;
                            if (x == -1.0d || y == -1.0d) {
                                if (Math.abs(coordinate3.getY() - coordinate4.getY()) >= Math.abs(coordinate8.getY() - pt.getY())) {
                                    Coordinate pt02 = pt.Clone();
                                    pt02.setX(pt02.getX() - tempExtendSide);
                                    pt02.setY(pt02.getY() + tempExtendSide);
                                    Coordinate coordinate802 = coordinate8.Clone();
                                    coordinate802.setX(coordinate802.getX() - tempExtendSide);
                                    coordinate802.setY(coordinate802.getY() - tempExtendSide);
                                    if (coordinate3.getY() - coordinate4.getY() < 0.0d) {
                                        list.add(list.size() - 2, MapToScreen(pt02));
                                        list.add(list.size() - 2, MapToScreen(coordinate802));
                                    } else {
                                        list.add(list.size() - 2, MapToScreen(coordinate802));
                                        list.add(list.size() - 2, MapToScreen(pt02));
                                    }
                                } else {
                                    Coordinate pt022 = pt.Clone();
                                    pt022.setX(pt022.getX() - tempExtendSide);
                                    pt022.setY(pt022.getY() + tempExtendSide);
                                    Coordinate coordinate602 = coordinate6.Clone();
                                    coordinate602.setX(coordinate602.getX() + tempExtendSide);
                                    coordinate602.setY(coordinate602.getY() + tempExtendSide);
                                    if (coordinate3.getX() - coordinate4.getX() > 0.0d) {
                                        list.add(list.size() - 2, MapToScreen(pt022));
                                        list.add(list.size() - 2, MapToScreen(coordinate602));
                                    } else {
                                        list.add(list.size() - 2, MapToScreen(coordinate602));
                                        list.add(list.size() - 2, MapToScreen(pt022));
                                    }
                                }
                            }
                        }
                    }
                    num = 0;
                } else if (num == 0) {
                    num = 1;
                }
            }
        } catch (Exception e) {
        }
        return (Point[]) list.toArray(new Point[0]);
    }

    public Point[] ClipPolyline4(List<Coordinate> paramList, int biasX, int biasY) {
        ArrayList result = new ArrayList();
        int tempCount = paramList.size();
        new Coordinate();
        new Coordinate();
        Point lastPoint = null;
        Envelope tempExtend = getClipExtend();
        Coordinate tempClipCoord01 = new Coordinate();
        Coordinate tempClipCoord02 = new Coordinate();
        for (int tempI = 1; tempI < tempCount; tempI++) {
            if (ClipLine(paramList.get(tempI - 1), paramList.get(tempI), tempExtend, tempClipCoord01, tempClipCoord02)) {
                Point localPoint1 = MapToScreen(tempClipCoord01);
                localPoint1.x += biasX;
                localPoint1.y += biasY;
                Point localPoint2 = MapToScreen(tempClipCoord02);
                localPoint2.x += biasX;
                localPoint2.y += biasY;
                if (lastPoint == null) {
                    lastPoint = localPoint1;
                }
                if (localPoint1.x == lastPoint.x && localPoint1.y == lastPoint.y) {
                    result.add(localPoint1);
                    lastPoint = localPoint2;
                } else {
                    result.add(localPoint1);
                    lastPoint = localPoint2;
                }
            }
        }
        if (lastPoint != null) {
            result.add(lastPoint);
        }
        return (Point[]) result.toArray(new Point[0]);
    }

    public Point[] ClipPolyline3(List<Coordinate> paramList, int paramInt1, int paramInt2) {
        ArrayList result = new ArrayList();
        int tempCount = paramList.size();
        new Coordinate();
        new Coordinate();
        Point lastPoint = null;
        int tempLastSideIndex = -1;
        for (int tempI = 1; tempI < tempCount; tempI++) {
            Coordinate tempClipCoord01 = new Coordinate();
            Coordinate tempClipCoord02 = new Coordinate();
            int tempSideIndex = CliplineOutSide(paramList.get(tempI - 1), paramList.get(tempI), tempClipCoord01, tempClipCoord02);
            if (tempSideIndex >= 0) {
                Point localPoint1 = MapToScreen(tempClipCoord01);
                localPoint1.x += paramInt1;
                localPoint1.y += paramInt2;
                Point localPoint2 = MapToScreen(tempClipCoord02);
                localPoint2.x += paramInt1;
                localPoint2.y += paramInt2;
                if (lastPoint == null) {
                    lastPoint = localPoint1;
                }
                if (localPoint1.x == lastPoint.x && localPoint1.y == lastPoint.y) {
                    result.add(localPoint1);
                    lastPoint = localPoint2;
                } else if (tempSideIndex == tempLastSideIndex) {
                    result.add(lastPoint);
                    result.add(localPoint1);
                    lastPoint = localPoint2;
                }
            }
            tempLastSideIndex = tempSideIndex;
        }
        if (lastPoint != null) {
            result.add(lastPoint);
        }
        return (Point[]) result.toArray(new Point[0]);
    }

    public int CliplineOutSide(Coordinate tmpCoord1, Coordinate tmpCoord2, Coordinate tmpCoord3, Coordinate tmpCoord4) {
        double[] tempCoords = new double[4];
        int tempSideIndex = SpatialRelation.CalLineEnvelopIntersectPtn(tmpCoord1.getX(), tmpCoord1.getY(), tmpCoord2.getX(), tmpCoord2.getY(), getClipExtendMinX(), getClipExtendMaxX(), getClipExtendMinY(), getClipExtendMaxY(), tempCoords);
        if (tempSideIndex < 0) {
            return -1;
        }
        tmpCoord3.setX(tempCoords[0]);
        tmpCoord3.setY(tempCoords[1]);
        tmpCoord4.setX(tempCoords[2]);
        tmpCoord4.setY(tempCoords[3]);
        return tempSideIndex;
    }

    public boolean Clipline(Coordinate Pt1, Coordinate Pt2, Coordinate returnPtn01, Coordinate returnPtn02) {
        double x = Pt1.getX();
        double y = Pt1.getY();
        double num3 = Pt2.getX();
        double num4 = Pt2.getY();
        boolean flag = false;
        while (true) {
            int num7 = Codec(x, y);
            int num8 = Codec(num3, num4);
            if (num7 == 0 && num8 == 0) {
                if (flag) {
                    x = num3;
                    num3 = x;
                    y = num4;
                    num4 = y;
                }
                returnPtn01.setX(x);
                returnPtn01.setY(y);
                returnPtn02.setX(num3);
                returnPtn02.setY(num4);
                return true;
            } else if ((num7 & num8) != 0) {
                return false;
            } else {
                if (num7 == 0) {
                    flag = true;
                    x = num3;
                    num3 = x;
                    y = num4;
                    num4 = y;
                }
                if ((num7 & 1) != 0) {
                    double clipExtendMinX = getClipExtendMinX();
                    x = clipExtendMinX;
                    y = (((clipExtendMinX - x) / (num3 - x)) * (num4 - y)) + y;
                } else if ((num7 & 2) != 0) {
                    double clipExtendMinX2 = getClipExtendMaxX();
                    x = clipExtendMinX2;
                    y = (((clipExtendMinX2 - x) / (num3 - x)) * (num4 - y)) + y;
                } else if ((num7 & 4) != 0) {
                    double clipExtendMinY = getClipExtendMinY();
                    x = (((clipExtendMinY - y) / (num4 - y)) * (num3 - x)) + x;
                    y = clipExtendMinY;
                } else if ((num7 & 8) != 0) {
                    double clipExtendMinY2 = getClipExtendMaxY();
                    x = (((clipExtendMinY2 - y) / (num4 - y)) * (num3 - x)) + x;
                    y = clipExtendMinY2;
                }
            }
        }
    }

    public boolean Clipline2(Coordinate tmpCoord1, Coordinate tmpCoord2, Coordinate tmpCoord3, Coordinate tmpCoord4) {
        double[] tempCoords = new double[4];
        if (SpatialRelation.CalLineEnvelopIntersectPtn(tmpCoord1.getX(), tmpCoord1.getY(), tmpCoord2.getX(), tmpCoord2.getY(), getClipExtendMinX(), getClipExtendMaxX(), getClipExtendMinY(), getClipExtendMaxY(), tempCoords) < 0) {
            return false;
        }
        tmpCoord3.setX(tempCoords[0]);
        tmpCoord3.setY(tempCoords[1]);
        tmpCoord4.setX(tempCoords[2]);
        tmpCoord4.setY(tempCoords[3]);
        return true;
    }

    public boolean InViewExtend(Coordinate tmpCoord) {
        return tmpCoord.getX() >= getExtend().getMinX() && tmpCoord.getX() <= getExtend().getMaxX() && tmpCoord.getY() >= getExtend().getMinY() && tmpCoord.getY() <= getExtend().getMaxY();
    }

    public Point[] MapPointsToScreePoints(List<Coordinate> paramList) {
        return MapPointsToScreePoints(paramList, true, 0, 0);
    }

    public Point[] MapPointsToScreePoints(List<Coordinate> paramList, boolean paramBoolean) {
        return MapPointsToScreePoints(paramList, paramBoolean, 0, 0);
    }

    public Point[] MapPointsToScreePoints(List<Coordinate> paramList, boolean paramBoolean, int paramInt1, int paramInt2) {
        ArrayList result = new ArrayList();
        int count = paramList.size();
        for (int i = 0; i < count; i++) {
            Point localPoint = MapToScreen(paramList.get(i));
            int tempX = localPoint.x;
            int tempY = localPoint.y;
            boolean tempTag = true;
            if (paramBoolean) {
                tempTag = false;
                int tempI = result.size();
                if (!(tempI > 0 && ((Point) result.get(tempI - 1)).x == tempX && ((Point) result.get(tempI - 1)).y == tempY)) {
                    tempTag = true;
                }
            }
            if (tempTag) {
                result.add(new Point(tempX + paramInt1, tempY + paramInt2));
            }
        }
        return (Point[]) result.toArray(new Point[0]);
    }

    public void RefreshMapExtend() {
        try {
            this._ExtendForView = null;
            for (GeoLayer layer : this._GeoLayers.getList()) {
                if (layer.getVisible() && layer.getDataset().getGeometryCount() > 0 && layer.getMaxScale() >= ((double) getActualScale()) && layer.getMinScale() <= ((double) getActualScale())) {
                    if (this._ExtendForView == null) {
                        this._ExtendForView = layer.getExtend().Clone();
                    } else {
                        UpdateExtendForView(layer.getExtend());
                    }
                }
            }
            if (this._RasterLayers != null && this._RasterLayers.size() > 0) {
                ListIterator<XLayer> tempIterRaster = this._RasterLayers.listIterator();
                while (tempIterRaster.hasNext()) {
                    XLayer tmpLayer = tempIterRaster.next();
                    if (tmpLayer.getVisible()) {
                        if (this._ExtendForView == null) {
                            this._ExtendForView = tmpLayer.getExtend().Clone();
                        } else {
                            UpdateExtendForView(tmpLayer.getExtend());
                        }
                    }
                }
            }
            for (GeoLayer layer2 : this._VectorGeoLayers.getList()) {
                if (layer2.getVisible() && layer2.getDataset().getGeometryCount() > 0 && layer2.getMaxScale() >= ((double) getActualScale()) && layer2.getMinScale() <= ((double) getActualScale())) {
                    if (this._ExtendForView == null) {
                        this._ExtendForView = layer2.getExtend().Clone();
                    } else {
                        UpdateExtendForView(layer2.getExtend());
                    }
                }
            }
        } catch (Exception ex) {
            Common.Log("错误Map2475", ex.getMessage());
        }
    }

    public int GetSelectObjectsCount() {
        return GetSelectObjectsCount(-1, true);
    }

    public int GetSelectObjectsCount(int geoType, boolean considerBKVector) {
        int totalCount = 0;
        try {
            int tid = getVectorGeoLayers().size();
            for (GeoLayer tmpGeoLayer : getGeoLayers().getList()) {
                Selection tmpSelection = getSelectionByIndex(tid, false);
                if (tmpSelection != null && tmpSelection.getCount() > 0) {
                    if (geoType == -1) {
                        totalCount += tmpSelection.getCount();
                    } else if (tmpGeoLayer.getType() == EGeoLayerType.POINT && geoType == 0) {
                        totalCount += tmpSelection.getCount();
                    } else if (tmpGeoLayer.getType() == EGeoLayerType.POLYLINE && geoType == 1) {
                        totalCount += tmpSelection.getCount();
                    } else if (tmpGeoLayer.getType() == EGeoLayerType.POLYGON && geoType == 2) {
                        totalCount += tmpSelection.getCount();
                    }
                }
                tid++;
            }
            if (considerBKVector) {
                int tid2 = 0;
                for (GeoLayer tmpGeoLayer2 : getVectorGeoLayers().getList()) {
                    Selection tmpSelection2 = getSelectionByIndex(tid2, false);
                    if (tmpSelection2 != null && tmpSelection2.getCount() > 0) {
                        if (geoType == -1) {
                            totalCount += tmpSelection2.getCount();
                        } else if (tmpGeoLayer2.getType() == EGeoLayerType.POINT && geoType == 0) {
                            totalCount += tmpSelection2.getCount();
                        } else if (tmpGeoLayer2.getType() == EGeoLayerType.POLYLINE && geoType == 1) {
                            totalCount += tmpSelection2.getCount();
                        } else if (tmpGeoLayer2.getType() == EGeoLayerType.POLYGON && geoType == 2) {
                            totalCount += tmpSelection2.getCount();
                        }
                    }
                    tid2++;
                }
            }
        } catch (Exception e) {
        }
        return totalCount;
    }

    public List<HashMap<String, Object>> GetSelectObjects(int paramInt1, int geoType, boolean considerBKVector, BasicValue editGeoCount) {
        Selection pSel;
        List<HashMap<String, Object>> result = new ArrayList<>();
        editGeoCount.setValue(0);
        try {
            int tmpEditGeoCount = 0;
            int tid = getVectorGeoLayers().size() - 1;
            for (GeoLayer localGeoLayer : getGeoLayers().getList()) {
                tid++;
                int j = 0;
                if (paramInt1 == 1) {
                    j = 0;
                    if (!localGeoLayer.getDataset().getDataSource().getEditing()) {
                        j = 1;
                    }
                }
                if (paramInt1 == 2 && localGeoLayer.getDataset().getDataSource().getEditing()) {
                    j = 1;
                }
                if (paramInt1 == -1) {
                    j = 1;
                }
                if (j != 0) {
                    boolean tmpBool = false;
                    if (geoType == -1) {
                        tmpBool = true;
                    } else if (localGeoLayer.getType() == EGeoLayerType.POINT && geoType == 0) {
                        tmpBool = true;
                    } else if (localGeoLayer.getType() == EGeoLayerType.POLYLINE && geoType == 1) {
                        tmpBool = true;
                    } else if (localGeoLayer.getType() == EGeoLayerType.POLYGON && geoType == 2) {
                        tmpBool = true;
                    }
                    if (tmpBool && (pSel = getSelectionByIndex(tid, false)) != null && pSel.getCount() > 0) {
                        tmpEditGeoCount += pSel.getCount();
                        for (Integer num : pSel.getGeometryIndexList()) {
                            HashMap<String, Object> tmpHash = new HashMap<>();
                            tmpHash.put("LayerName", localGeoLayer.getLayerName());
                            tmpHash.put("LayerID", localGeoLayer.getLayerID());
                            tmpHash.put("DataSource", localGeoLayer.getDataset().getDataSource().getName());
                            tmpHash.put("Editable", Boolean.valueOf(localGeoLayer.getEditable()));
                            tmpHash.put("GeoType", localGeoLayer.getGeoTypeName());
                            int tmpGeoIndex = num.intValue();
                            tmpHash.put("GeoIndex", Integer.valueOf(tmpGeoIndex));
                            tmpHash.put("Geometry", localGeoLayer.getDataset().GetGeometry(tmpGeoIndex));
                            result.add(tmpHash);
                        }
                    }
                }
            }
            editGeoCount.setValue(tmpEditGeoCount);
            if (considerBKVector) {
                int tid2 = -1;
                for (GeoLayer localGeoLayer2 : getVectorGeoLayers().getList()) {
                    tid2++;
                    int j2 = 0;
                    if (paramInt1 == 1) {
                        j2 = 0;
                        if (!localGeoLayer2.getDataset().getDataSource().getEditing()) {
                            j2 = 1;
                        }
                    }
                    if (paramInt1 == 2 && localGeoLayer2.getDataset().getDataSource().getEditing()) {
                        j2 = 1;
                    }
                    if (paramInt1 == -1) {
                        j2 = 1;
                    }
                    if (j2 != 0) {
                        boolean tmpBool2 = false;
                        if (geoType == -1) {
                            tmpBool2 = true;
                        } else if (localGeoLayer2.getType() == EGeoLayerType.POINT && geoType == 0) {
                            tmpBool2 = true;
                        } else if (localGeoLayer2.getType() == EGeoLayerType.POLYLINE && geoType == 1) {
                            tmpBool2 = true;
                        } else if (localGeoLayer2.getType() == EGeoLayerType.POLYGON && geoType == 2) {
                            tmpBool2 = true;
                        }
                        if (tmpBool2) {
                            Selection pSel2 = getSelectionByIndex(tid2, false);
                            if (pSel2.getCount() > 0) {
                                for (Integer num2 : pSel2.getGeometryIndexList()) {
                                    HashMap<String, Object> tmpHash2 = new HashMap<>();
                                    tmpHash2.put("LayerName", localGeoLayer2.getLayerName());
                                    tmpHash2.put("LayerID", localGeoLayer2.getLayerID());
                                    tmpHash2.put("DataSourc", localGeoLayer2.getDataset().getDataSource().getName());
                                    tmpHash2.put("Editable", Boolean.valueOf(localGeoLayer2.getEditable()));
                                    tmpHash2.put("GeoType", localGeoLayer2.getGeoTypeName());
                                    int tmpGeoIndex2 = num2.intValue();
                                    tmpHash2.put("GeoIndex", Integer.valueOf(tmpGeoIndex2));
                                    tmpHash2.put("Geometry", localGeoLayer2.getDataset().GetGeometry(tmpGeoIndex2));
                                    result.add(tmpHash2);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return result;
    }

    public List<HashMap<String, Object>> GetSelectObjectsInLayer(String layerID, boolean isVectorLayer, BasicValue editGeoCount) {
        List<HashMap<String, Object>> result = new ArrayList<>();
        editGeoCount.setValue(0);
        int tmpEditGeoCount = 0;
        if (!isVectorLayer) {
            try {
                Iterator<GeoLayer> tempIter = getGeoLayers().getList().iterator();
                int tid = getVectorGeoLayers().size() - 1;
                while (true) {
                    if (!tempIter.hasNext()) {
                        break;
                    }
                    GeoLayer localGeoLayer = tempIter.next();
                    tid++;
                    if (localGeoLayer.getLayerID().equals(layerID)) {
                        Selection pSel = getSelectionByIndex(tid, false);
                        if (pSel != null && pSel.getCount() > 0) {
                            tmpEditGeoCount = 0 + pSel.getCount();
                            for (Integer num : pSel.getGeometryIndexList()) {
                                HashMap<String, Object> tmpHash = new HashMap<>();
                                tmpHash.put("LayerName", localGeoLayer.getLayerName());
                                tmpHash.put("LayerID", localGeoLayer.getLayerID());
                                tmpHash.put("DataSource", localGeoLayer.getDataset().getDataSource().getName());
                                tmpHash.put("Editable", Boolean.valueOf(localGeoLayer.getEditable()));
                                tmpHash.put("GeoType", localGeoLayer.getGeoTypeName());
                                int tmpGeoIndex = num.intValue();
                                tmpHash.put("GeoIndex", Integer.valueOf(tmpGeoIndex));
                                tmpHash.put("Geometry", localGeoLayer.getDataset().GetGeometry(tmpGeoIndex));
                                result.add(tmpHash);
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        } else {
            int tid2 = -1;
            for (GeoLayer localGeoLayer2 : getVectorGeoLayers().getList()) {
                tid2++;
                Selection pSel2 = getSelectionByIndex(tid2, false);
                if (pSel2.getCount() > 0) {
                    for (Object obj : pSel2.getGeometryIndexList()) {
                        HashMap<String, Object> tmpHash2 = new HashMap<>();
                        tmpHash2.put("LayerName", localGeoLayer2.getLayerName());
                        tmpHash2.put("LayerID", localGeoLayer2.getLayerID());
                        tmpHash2.put("DataSourc", localGeoLayer2.getDataset().getDataSource().getName());
                        tmpHash2.put("Editable", Boolean.valueOf(localGeoLayer2.getEditable()));
                        tmpHash2.put("GeoType", localGeoLayer2.getGeoTypeName());
                        tmpHash2.put("GeoIndex", obj);
                        result.add(tmpHash2);
                    }
                }
            }
        }
        editGeoCount.setValue(tmpEditGeoCount);
        return result;
    }

    public boolean GetSelectOneObjectInfo(BasicValue layerIDParam, BasicValue geoIndexParam, BasicValue dataSourceNameParam) {
        return GetSelectOneObjectInfo(layerIDParam, geoIndexParam, new BasicValue(), dataSourceNameParam);
    }

    public boolean GetSelectOneObjectInfo(BasicValue layerIDParam, BasicValue geoIndexParam, BasicValue geoSYS_IDParam, BasicValue dataSourceNameParam) {
        int i = 0;
        try {
            int tid = getVectorGeoLayers().size() - 1;
            for (GeoLayer localGeoLayer : getGeoLayers().getList()) {
                tid++;
                Selection tmpSel = getSelectionByIndex(tid, false);
                if (tmpSel != null) {
                    i += tmpSel.getCount();
                    if (tmpSel.getCount() == 1) {
                        int tempIndex = tmpSel.getGeometryIndexList().get(0).intValue();
                        AbstractGeometry pGeo = localGeoLayer.getDataset().GetGeometry(tempIndex);
                        if (pGeo != null) {
                            geoSYS_IDParam.setValue(pGeo.GetSYS_ID());
                        } else {
                            geoSYS_IDParam.setValue((String) null);
                        }
                        geoIndexParam.setValue(tempIndex);
                        layerIDParam.setValue(localGeoLayer.getName());
                        dataSourceNameParam.setValue(localGeoLayer.getDataset().getDataSource().getName());
                    }
                }
            }
            if (i != 1) {
                int tid2 = -1;
                for (GeoLayer localGeoLayer2 : getVectorGeoLayers().getList()) {
                    tid2++;
                    Selection tmpSel2 = getSelectionByIndex(tid2, false);
                    if (tmpSel2 != null) {
                        i += tmpSel2.getCount();
                        if (tmpSel2.getCount() == 1) {
                            int tempIndex2 = tmpSel2.getGeometryIndexList().get(0).intValue();
                            AbstractGeometry pGeo2 = localGeoLayer2.getDataset().GetGeometry(tempIndex2);
                            if (pGeo2 != null) {
                                geoSYS_IDParam.setValue(pGeo2.GetSYS_ID());
                            } else {
                                geoSYS_IDParam.setValue((String) null);
                            }
                            geoIndexParam.setValue(tempIndex2);
                            layerIDParam.setValue(localGeoLayer2.getName());
                            dataSourceNameParam.setValue(localGeoLayer2.getDataset().getDataSource().getName());
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return i > 0;
    }

    public Bitmap getMapPicture() {
        return this.f448bp;
    }

    public GeoLayer GetGeoLayerByID(String layerID) {
        for (GeoLayer localGeoLayer : this._GeoLayers.getList()) {
            if (localGeoLayer != null && localGeoLayer.getName().indexOf(layerID) >= 0) {
                return localGeoLayer;
            }
        }
        if (this._VectorGeoLayers.size() > 0) {
            ListIterator<GeoLayer> tempIter01 = this._VectorGeoLayers.getList().listIterator();
            while (tempIter01.hasNext()) {
                GeoLayer tmpLayer = tempIter01.next();
                if (tmpLayer.getLayerID().equals(layerID)) {
                    return tmpLayer;
                }
            }
        }
        return null;
    }

    public void gotoLastMapExtend() {
        int tmpSize = this._MapExtendList.size();
        if (tmpSize > 1) {
            Envelope tmpExtendEnvelope = this._MapExtendList.get(tmpSize - 2);
            ZoomToExtend(tmpExtendEnvelope);
            this._MapExtendList.remove(tmpExtendEnvelope);
        }
    }

    public float getMaskBiasX() {
        return this.MaskBiasX;
    }

    public float getMaskBiasY() {
        return this.MaskBiasY;
    }

    public void setMaskBias(float maskWidth, float maskHeight) {
        this.MaskBiasX = -maskWidth;
        this.MaskBiasY = -maskHeight;
    }

    public void RefreshFastNetRasterLayers() {
        int tmpAllowMaxLayers;
        if (this.m_AllowRfresh) {
            this.RasterFastResfreshMode = false;
            new Canvas(this.RasterBitmap).drawColor(PubVar.MapBgColor);
            boolean tmpHasShutter = PubVar._MapView.m_ShutterTool.getIsShutterMode();
            Canvas tmpShutterCanvas = null;
            if (tmpHasShutter) {
                tmpShutterCanvas = new Canvas(getShutterRasterBitmap());
                tmpShutterCanvas.drawColor(PubVar.MapBgColor);
            }
            if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                tmpAllowMaxLayers = 1;
            } else {
                tmpAllowMaxLayers = Integer.MAX_VALUE;
            }
            if (this._RasterLayers != null && this._RasterLayers.size() > 0) {
                ListIterator<XLayer> tempIterRaster = this._RasterLayers.listIterator();
                while (tempIterRaster.hasNext()) {
                    tempIterRaster.next();
                }
                int tmpTid = 0;
                while (tempIterRaster.hasPrevious() && (tmpTid = tmpTid + 1) <= tmpAllowMaxLayers) {
                    try {
                        XLayer tempLayer = tempIterRaster.previous();
                        if (tempLayer != null) {
                            if (tmpHasShutter) {
                                tempLayer.SetDrawCanvas(this.RasterBitmap);
                                if (!tmpHasShutter) {
                                    tempLayer.SetOtherDrawCanvas(null);
                                } else if (tempLayer.getLayerType() == ELayerType.RASTERFILE) {
                                    if (PubVar._MapView.m_ShutterTool.m_SelectLayers.contains(((XRasterFileLayer) tempLayer).getFilePath())) {
                                        tempLayer.SetOtherDrawCanvas(tmpShutterCanvas);
                                    } else {
                                        tempLayer.SetOtherDrawCanvas(null);
                                    }
                                } else if (PubVar._MapView.m_ShutterTool.m_SelectLayers.contains(tempLayer.getName())) {
                                    tempLayer.SetOtherDrawCanvas(tmpShutterCanvas);
                                } else {
                                    tempLayer.SetOtherDrawCanvas(null);
                                }
                                if (tempLayer instanceof XBaseTilesLayer) {
                                    ((XBaseTilesLayer) tempLayer).refreshSelf(this);
                                } else {
                                    tempLayer.Refresh(this);
                                }
                            } else if (tempLayer.getVisible()) {
                                tempLayer.SetDrawCanvas(this.RasterBitmap);
                                tempLayer.SetOtherDrawCanvas(null);
                                if (tempLayer instanceof XBaseTilesLayer) {
                                    ((XBaseTilesLayer) tempLayer).refreshSelf(this);
                                } else {
                                    tempLayer.Refresh(this);
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
            this.RasterFastResfreshMode = true;
            RefreshFast2();
        }
    }
}
