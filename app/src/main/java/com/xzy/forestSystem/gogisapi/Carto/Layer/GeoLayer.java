package  com.xzy.forestSystem.gogisapi.Carto.Layer;

import android.graphics.Canvas;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Map;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Display.ClassifiedRender;
import  com.xzy.forestSystem.gogisapi.Display.IRender;
import  com.xzy.forestSystem.gogisapi.Display.SimpleDisplay;
import  com.xzy.forestSystem.gogisapi.Edit.EDrawType;
import  com.xzy.forestSystem.gogisapi.Edit.EGeometryStatus;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Selection;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.EGeoDisplayType;
import java.util.Iterator;

public class GeoLayer extends XLayer {
    private static DataSet _Dataset;
    private static String _Name;
    String _DisplayFilter = "";
    boolean _Editable = true;
    IRender _Render = null;
    int _RenderType = 1;
    boolean _Selectable = true;
    boolean _Snapable = true;
    EGeoLayerType _Type = EGeoLayerType.NONE;
    boolean _Visible = true;

    public GeoLayer() {
        this._LayerType = ELayerType.FEATURE;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public void Dispose2() {
        super.Dispose2();
        if (this._Render != null) {
            this._Render.Dispose2();
        }
        this._Render = null;
        this._Dataset.Dispose2();
    }

    private void AddIndex(AbstractGeometry paramGeometry) {
    }

    private void RemoveIndex(AbstractGeometry paramGeometry) {
    }

    public int AddGeometry(AbstractGeometry paramGeometry) {
        return AddGeometry(paramGeometry, false);
    }

    public int AddGeometry(AbstractGeometry paramGeometry, boolean isNewGeo) {
        if (this._Dataset.AddGeometry(paramGeometry, true, isNewGeo) && isNewGeo) {
            this._Extend = this._Extend.MergeEnvelope(paramGeometry.CalEnvelope());
        }
        return paramGeometry.getIndex();
    }

    public void AddIndex(AbstractGeometry paramGeometry, int paramInt) {
    }

    public void DrawSelection(Selection paramSelection, Map paramMap, Canvas canvas) {
        DrawSelection(paramSelection, paramMap, canvas, 0, 0);
    }

    public void DrawSelection(Selection paramSelection, Map paramMap, Canvas paramCanvas, int paramInt1, int paramInt2) {
        try {
            if (paramSelection.getCount() > 0) {
                Iterator localIterator = paramSelection.getGeometryIndexList().iterator();
                if (localIterator.hasNext()) {
                    boolean tempBool = true;
                    if (paramSelection.getType() == EGeoDisplayType.NORMAL) {
                        tempBool = true;
                    } else if (paramSelection.getType() == EGeoDisplayType.SELECT) {
                        tempBool = false;
                    }
                    while (localIterator.hasNext()) {
                        AbstractGeometry localGeometry = this._Dataset.GetGeometry(localIterator.next().hashCode());
                        if (!(localGeometry == null || localGeometry.getStatus() == EGeometryStatus.DELETE)) {
                            if (tempBool) {
                                if (localGeometry.getSymbol() == null) {
                                    localGeometry.setSymbol(getRender().getDefaultSymbol());
                                }
                                if (localGeometry.getSymbol() != null) {
                                    localGeometry.getSymbol().Draw(paramMap, paramCanvas, localGeometry, paramInt1, paramInt2, EDrawType.NONE);
                                }
                            } else {
                                if (localGeometry.getSymbol() == null) {
                                    localGeometry.setSymbol(getRender().getDefaultSymbol());
                                }
                                if (localGeometry.getSymbol() != null) {
                                    localGeometry.getSymbol().Draw(paramMap, paramCanvas, localGeometry, paramInt1, paramInt2, EDrawType.NON_EDIT_SEL);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Common.Log("DrawSelection", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    public void DrawSelectionLabel(Selection paramSelection, Map paramMap, Canvas paramCanvas, int paramInt1, int paramInt2) {
        try {
            if (paramSelection.getCount() > 0) {
                for (Integer num : paramSelection.getGeometryIndexList()) {
                    AbstractGeometry localGeometry = this._Dataset.GetGeometry(num.intValue());
                    if (!(localGeometry == null || localGeometry.getStatus() == EGeometryStatus.DELETE)) {
                        if (localGeometry.getSymbol() != null) {
                            localGeometry.getSymbol().DrawLabel(paramMap, this._Render, paramCanvas, localGeometry, paramInt1, paramInt2, paramSelection.getType());
                        } else {
                            return;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Common.Log("CreateReport", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    public void RefreshFast(Map paramMap, Canvas canvas, Selection showSelection) {
        DrawSelection(showSelection, paramMap, canvas);
    }

    public void RefreshFast(Map paramMap, Selection showSelection) {
        DrawSelection(showSelection, paramMap, paramMap.getDisplayGraphic());
    }

    public int GetFieldIndex(String paramString) {
        return -1;
    }

    public AbstractGeometry GetObject(int paramInt) {
        return null;
    }

    public boolean Refresh(Map paramMap, Selection showSelection) {
        return this._Dataset.QueryWithExtend(paramMap.getExtend(), showSelection);
    }

    public void UpdateGeometry(AbstractGeometry paramGeometry) {
    }

    public static DataSet getDataset() {
        return _Dataset;
    }

    public String getDispplayFilter() {
        return this._DisplayFilter;
    }

    public boolean getEditable() {
        return this._Editable;
    }

    public IRender getRender() {
        if (this._Render == null) {
            FeatureLayer tmpLayer = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(this._Name);
            if (tmpLayer != null) {
                this._Render = IRender.CreateRender(this, tmpLayer.GetRenderType());
            } else {
                this._Render = IRender.CreateRender(this, this._RenderType);
            }
        }
        return this._Render;
    }

    public boolean getSelectable() {
        return this._Selectable;
    }

    public boolean getSnapable() {
        return this._Snapable;
    }

    public EGeoLayerType getType() {
        return this._Type;
    }

    public String getGeoTypeName() {
        if (this._Type == EGeoLayerType.POINT) {
            return "点";
        }
        if (this._Type == EGeoLayerType.POLYLINE) {
            return "线";
        }
        if (this._Type == EGeoLayerType.POLYGON) {
            return "面";
        }
        return "无";
    }

    public void setDataset(DataSet paramDataset) {
        this._Dataset = paramDataset;
        this._Dataset.setBindGeoLayer(this);
    }

    public void setEditable(boolean paramBoolean) {
        this._Editable = paramBoolean;
    }

    public void setRender(IRender paramIRender) {
        this._Render = paramIRender;
        this._Render.SetGeoLayer(this);
        if (this._Render instanceof SimpleDisplay) {
            this._RenderType = 1;
        } else if (this._Render instanceof ClassifiedRender) {
            this._RenderType = 2;
        }
        this._VisiableScaleMax = paramIRender.GetMaxScale();
        this._VisiableScaleMin = paramIRender.GetMinScale();
        this._Transparent = paramIRender.getTransparent();
    }

    public void setRenderType(int renderType) {
        this._RenderType = renderType;
    }

    public int getRenderType() {
        return this._RenderType;
    }

    public void setSelectable(boolean paramBoolean) {
        this._Selectable = paramBoolean;
    }

    public void setSnapable(boolean paramBoolean) {
        this._Snapable = paramBoolean;
    }

    public void setType(EGeoLayerType paramlkGeoLayerType) {
        this._Type = paramlkGeoLayerType;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public String getName() {
        return this._Name;
    }

    public static String getLayerID() {
        return _Name;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public void setName(String paramString) {
        this._Name = paramString;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public boolean getVisible() {
        return this._Visible;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public void setVisible(boolean paramBoolean) {
        this._Visible = paramBoolean;
    }

    public void UpdateFromLayer(FeatureLayer layer) {
        this._LayerName = layer.GetLayerName();
        if (this._Dataset != null) {
            this._Dataset.setLayerName(this._LayerName);
        }
        this._Visible = layer.GetVisible();
        this._VisiableScaleMax = layer.GetMaxScale();
        this._VisiableScaleMin = layer.GetMinScale();
        this._Selectable = layer.GetSelectable();
        this._Editable = layer.GetEditable();
        this._Transparent = layer.GetTransparet();
        this._MinX = layer.getMinX();
        this._MaxX = layer.getMaxX();
        this._MinY = layer.getMinY();
        this._MaxY = layer.getMaxY();
        updateExtend();
    }

    public void UpdateToLayer(FeatureLayer layer) {
        layer.SetLayerName(this._LayerName);
        layer.SetVisible(this._Visible);
        layer.SetMaxScale(this._VisiableScaleMax);
        layer.SetMinScale(this._VisiableScaleMin);
        layer.SetSelectable(this._Selectable);
        layer.SetEditable(this._Editable);
        layer.SetTransparent(this._Transparent);
        layer.setMaxX(this._MaxX);
        layer.setMinX(this._MinX);
        layer.setMaxY(this._MaxY);
        layer.setMinY(this._MinY);
        if (this._Render != null) {
            layer.SetIfLabel(this._Render.getIfLabel());
            layer.SetLabelFont(this._Render.getLabelFont());
            layer.SetLabelScaleMax(this._Render.getLabelScaleMax());
            layer.SetLabelScaleMin(this._Render.getLabelScaleMin());
            layer.SetLabelDataField(this._Render.getLabelDataField());
        }
    }
}
