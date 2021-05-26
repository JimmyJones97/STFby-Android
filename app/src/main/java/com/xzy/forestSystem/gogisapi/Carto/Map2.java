package  com.xzy.forestSystem.gogisapi.Carto;

import android.graphics.Canvas;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Selection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class Map2 extends Map {
    private boolean _AllowedRefresh = true;
    private Map _BindMap = null;
    private List<HashMap> _LayersVisList = new ArrayList();
    private List<HashMap> _RasterLayersVisList = new ArrayList();
    private List<HashMap> _VectorLayersVisList = new ArrayList();

    public Map2(MapView parammapView) {
        super(parammapView);
    }

    public void SetBindMap(Map map) {
        this._BindMap = map;
        this._VectorGeoLayers = map._VectorGeoLayers;
        this._GeoLayers = map._GeoLayers;
        this._RasterLayers = map._RasterLayers;
        InitialSelections();
    }

    public void SetAllowedRefresh(boolean value) {
        this._AllowedRefresh = value;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Map
    public void InitialSelections() {
        super.InitialSelections();
        this._LayersVisList = new ArrayList();
        this._VectorLayersVisList = new ArrayList();
        this._RasterLayersVisList = new ArrayList();
        int tid = -1;
        if (this._VectorGeoLayers.size() > 0) {
            ListIterator<GeoLayer> tempIter01 = this._VectorGeoLayers.getList().listIterator();
            while (tempIter01.hasNext()) {
                tid++;
                GeoLayer tmpLayer = tempIter01.next();
                HashMap tmpHash = new HashMap();
                tmpHash.put("Index", Integer.valueOf(tid));
                tmpHash.put("LayerID", tmpLayer.getLayerID());
                tmpHash.put("LayerName", tmpLayer.getLayerName());
                tmpHash.put("Visible", Boolean.valueOf(tmpLayer.getVisible()));
                tmpHash.put("LayerType", tmpLayer.getLayerType());
                tmpHash.put("GeoType", tmpLayer.getGeoTypeName());
                tmpHash.put("DatasourceName", tmpLayer.getDataset().getDataSource().getName());
                this._VectorLayersVisList.add(tmpHash);
            }
        }
        if (this._GeoLayers.size() > 0) {
            ListIterator<GeoLayer> tempIter02 = this._GeoLayers.getList().listIterator();
            while (tempIter02.hasNext()) {
                tid++;
                GeoLayer tmpLayer2 = tempIter02.next();
                HashMap tmpHash2 = new HashMap();
                tmpHash2.put("Index", Integer.valueOf(tid));
                tmpHash2.put("LayerID", tmpLayer2.getLayerID());
                tmpHash2.put("LayerName", tmpLayer2.getLayerName());
                tmpHash2.put("Visible", Boolean.valueOf(tmpLayer2.getVisible()));
                tmpHash2.put("LayerType", tmpLayer2.getLayerType());
                tmpHash2.put("GeoType", tmpLayer2.getGeoTypeName());
                tmpHash2.put("DatasourceName", tmpLayer2.getDataset().getDataSource().getName());
                this._LayersVisList.add(tmpHash2);
            }
        }
        if (this._RasterLayers != null && this._RasterLayers.size() > 0) {
            ListIterator<XLayer> tempIterRaster = this._RasterLayers.listIterator();
            while (tempIterRaster.hasNext()) {
                XLayer tmpLayer3 = tempIterRaster.next();
                tid++;
                HashMap tmpHash3 = new HashMap();
                tmpHash3.put("Index", Integer.valueOf(tid));
                tmpHash3.put("LayerID", tmpLayer3.getName());
                tmpHash3.put("LayerName", tmpLayer3.getLayerName());
                tmpHash3.put("Visible", Boolean.valueOf(tmpLayer3.getVisible()));
                tmpHash3.put("LayerType", tmpLayer3.getLayerType());
                this._RasterLayersVisList.add(tmpHash3);
            }
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Map
    public void Refresh() {
        Selection tmpShowSelection;
        Selection tmpShowSelection2;
        if (this._AllowedRefresh) {
            try {
                RefreshRasterLayers();
                ClearShowSelections();
                int tid = 0;
                for (GeoLayer layer : this._VectorGeoLayers.getList()) {
                    if (Boolean.valueOf(Boolean.parseBoolean(String.valueOf(this._VectorLayersVisList.get(tid).get("Visible")))).booleanValue() && layer.getMaxScale() >= ((double) getActualScale()) && layer.getMinScale() <= ((double) getActualScale()) && (tmpShowSelection2 = getSelectionByIndex(tid, true)) != null) {
                        layer.Refresh(this, tmpShowSelection2);
                    }
                    tid++;
                }
                getExtend();
                int tid2 = 0;
                for (GeoLayer layer2 : this._GeoLayers.getList()) {
                    if (Boolean.valueOf(Boolean.parseBoolean(String.valueOf(this._LayersVisList.get(tid2).get("Visible")))).booleanValue() && layer2.getMaxScale() >= ((double) getActualScale()) && layer2.getMinScale() <= ((double) getActualScale()) && (tmpShowSelection = getFeatureSelectionByIndex(tid2, true)) != null) {
                        layer2.Refresh(this, tmpShowSelection);
                    }
                    tid2++;
                }
            } catch (Exception e) {
            } finally {
                RefreshFast();
            }
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Map
    public void RefreshRasterLayers() {
        int tmpAllowMaxLayers;
        if (this._AllowedRefresh) {
            new Canvas(this.RasterBitmap).drawColor(PubVar.MapBgColor);
            if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                tmpAllowMaxLayers = Integer.MAX_VALUE;
            } else {
                tmpAllowMaxLayers = 1;
            }
            if (this._RasterLayers != null && this._RasterLayers.size() > 0) {
                ListIterator<XLayer> tempIterRaster = this._RasterLayers.listIterator();
                while (tempIterRaster.hasNext()) {
                    tempIterRaster.next();
                }
                int tmpTid = 0;
                int tmpCount = this._RasterLayers.size();
                while (tempIterRaster.hasPrevious() && (tmpTid = tmpTid + 1) <= tmpAllowMaxLayers) {
                    XLayer tempLayer = tempIterRaster.previous();
                    if (tempLayer != null && Boolean.valueOf(Boolean.parseBoolean(String.valueOf(this._RasterLayersVisList.get(tmpCount - tmpTid).get("Visible")))).booleanValue()) {
                        tempLayer.SetDrawCanvas(this.RasterBitmap);
                        tempLayer.SetOtherDrawCanvas(null);
                        tempLayer.Refresh(this);
                    }
                }
            }
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Map
    public void RefreshFast() {
        int tmpAllowMaxLayers;
        int tmpAllowMaxLayers2;
        if (this._AllowedRefresh) {
            try {
                this.VectorBitmap.eraseColor(0);
                this._Graphics = new Canvas(this.VectorBitmap);
                if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                    tmpAllowMaxLayers = Integer.MAX_VALUE;
                } else {
                    tmpAllowMaxLayers = 5;
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
                        int tid = tmpLayersCount - tmpTid;
                        if (Boolean.valueOf(Boolean.parseBoolean(String.valueOf(this._VectorLayersVisList.get(this._VectorGeoLayers.size() - tmpTid).get("Visible")))).booleanValue() && tempGeoLayer.getMaxScale() >= ((double) getActualScale()) && tempGeoLayer.getMinScale() <= ((double) getActualScale())) {
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
                        }
                    }
                }
                if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                    tmpAllowMaxLayers2 = Integer.MAX_VALUE;
                } else {
                    tmpAllowMaxLayers2 = 5;
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
                        int tid2 = tmpLayersCount2 - tmpTid2;
                        if (Boolean.valueOf(Boolean.parseBoolean(String.valueOf(this._LayersVisList.get(this._GeoLayers.size() - tmpTid2).get("Visible")))).booleanValue() && tempGeoLayer2.getMaxScale() >= ((double) getActualScale()) && tempGeoLayer2.getMinScale() <= ((double) getActualScale())) {
                            Selection tmpShowSelection2 = getSelectionByIndex(tid2, true);
                            Selection tmpSelSelection2 = getSelectionByIndex(tid2, false);
                            if (tmpShowSelection2 != null && tmpShowSelection2.getCount() > 0) {
                                tempGeoLayer2.RefreshFast(this, tmpShowSelection2);
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

    public void CloneShowSelections(Map map) {
        this._ShowSelectionList = new ArrayList();
        for (Selection tmpSel : map.getShowSelectionList()) {
            this._ShowSelectionList.add(tmpSel.Clone());
        }
    }

    public void RefreshClone() {
        try {
            RefreshRasterLayers();
        } catch (Exception e) {
        } finally {
            RefreshFast();
        }
    }

    public String getSelectedLayers() {
        StringBuilder tmpSB = new StringBuilder();
        try {
            for (HashMap tmpHash : this._LayersVisList) {
                if (Boolean.valueOf(Boolean.parseBoolean(String.valueOf(tmpHash.get("Visible")))).booleanValue()) {
                    tmpSB.append(String.valueOf(tmpHash.get("LayerID")));
                    tmpSB.append(";");
                }
            }
            for (HashMap tmpHash2 : this._VectorLayersVisList) {
                if (Boolean.valueOf(Boolean.parseBoolean(String.valueOf(tmpHash2.get("Visible")))).booleanValue()) {
                    tmpSB.append(String.valueOf(tmpHash2.get("LayerID")));
                    tmpSB.append(";");
                }
            }
            for (HashMap tmpHash3 : this._RasterLayersVisList) {
                if (Boolean.valueOf(Boolean.parseBoolean(String.valueOf(tmpHash3.get("Visible")))).booleanValue()) {
                    tmpSB.append(String.valueOf(tmpHash3.get("LayerID")));
                    tmpSB.append(";");
                }
            }
        } catch (Exception e) {
        }
        return tmpSB.toString();
    }

    public void SetSelectedLayers(String layerIDs) {
        try {
            for (HashMap tmpHash : this._LayersVisList) {
                if (layerIDs.contains(String.valueOf(tmpHash.get("LayerID")))) {
                    tmpHash.put("Visible", true);
                } else {
                    tmpHash.put("Visible", false);
                }
            }
            for (HashMap tmpHash2 : this._VectorLayersVisList) {
                if (layerIDs.contains(String.valueOf(tmpHash2.get("LayerID")))) {
                    tmpHash2.put("Visible", true);
                } else {
                    tmpHash2.put("Visible", false);
                }
            }
            for (HashMap tmpHash3 : this._RasterLayersVisList) {
                if (layerIDs.contains(String.valueOf(tmpHash3.get("LayerID")))) {
                    tmpHash3.put("Visible", true);
                } else {
                    tmpHash3.put("Visible", false);
                }
            }
        } catch (Exception e) {
        }
    }
}
