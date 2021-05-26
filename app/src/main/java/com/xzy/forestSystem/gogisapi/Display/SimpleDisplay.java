package  com.xzy.forestSystem.gogisapi.Display;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;

public class SimpleDisplay extends IRender {
    private ISymbol _Symbol = null;
    private String _SymbolName = "默认";

    public SimpleDisplay(GeoLayer paramGeoLayer) {
        setType(EDisplayType.SIMPLE);
        this._GeoLayer = paramGeoLayer;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public void Dispose2() {
        try {
            if (this._Symbol != null) {
                this._Symbol.Dispose2();
            }
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public void LoadSymbol(FeatureLayer layer) {
        if (!this.isLoad) {
            Object[] tmpObjs = null;
            if (layer != null) {
                tmpObjs = layer.GetLayerRenderData();
            }
            LoadSymbol(layer, tmpObjs);
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public void LoadSymbol(FeatureLayer layer, Object[] renderParas) {
        if (!this.isLoad) {
            boolean tempTag = true;
            if (layer != null) {
                UpdateFromLayer(layer);
                if (renderParas != null && renderParas.length > 0 && renderParas[0] != null && !renderParas[0].toString().equals("")) {
                    if (this._GeoLayer.getType() == EGeoLayerType.POINT) {
                        this._Symbol = SymbolManage.GetPointSymbol(renderParas[0].toString());
                    } else if (this._GeoLayer.getType() == EGeoLayerType.POLYLINE) {
                        this._Symbol = SymbolManage.GetLineSymbol(renderParas[0].toString());
                    } else if (this._GeoLayer.getType() == EGeoLayerType.POLYGON) {
                        this._Symbol = SymbolManage.GetPolySymbol(renderParas[0].toString());
                    }
                    tempTag = false;
                }
            }
            if (tempTag) {
                if (this._GeoLayer.getType() == EGeoLayerType.POINT) {
                    this._Symbol = PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemPointSymbol(this._SymbolName);
                }
                if (this._GeoLayer.getType() == EGeoLayerType.POLYLINE) {
                    this._Symbol = PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemLineSymbol(this._SymbolName);
                }
                if (this._GeoLayer.getType() == EGeoLayerType.POLYGON) {
                    this._Symbol = PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemPolySymbol(this._SymbolName);
                }
            }
            SetSymbolTransparent(this._SymbolTransparent);
            this.isLoad = true;
            if (getIfLabel()) {
                String str = getLabelFont();
                Paint localPaint = new Paint();
                localPaint.setAntiAlias(true);
                localPaint.setTextSize(Float.valueOf(str.split(",")[1]).floatValue() * PubVar.ScaledDensity);
                localPaint.setTypeface(Typeface.create("宋体", 0));
                localPaint.setColor(Color.parseColor(str.split(",")[0]));
                TextSymbol localTextSymbol = new TextSymbol();
                localTextSymbol.setTextFont(localPaint);
                setTextSymbol(localTextSymbol);
            }
        }
    }

    public void SetSymbol(ISymbol symbol) {
        this._Symbol = symbol;
        this._SymbolName = symbol.getName();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public void SetSymbolTransparent(int transparent) {
        this._SymbolTransparent = transparent;
        if (this._Symbol != null) {
            this._Symbol.SetTransparent(this._SymbolTransparent);
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public void UpdateSymbol(AbstractGeometry paramGeometry) {
        paramGeometry.setSymbol(this._Symbol);
    }

    public void UpdateSymbolEx(AbstractGeometry paramGeometry, String paramString) {
    }

    public String getSymbolName() {
        return this._SymbolName;
    }

    public ISymbol getSymbol() {
        return this._Symbol;
    }

    public void setSymbolName(String paramString) {
        this._SymbolName = paramString;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public boolean HasUsedSymbol(String symbolName) {
        if (symbolName.equals(this._SymbolName)) {
            return true;
        }
        return false;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public boolean SaveRender() {
        try {
            if (this._GeoLayer == null || this._Symbol == null || this._GeoLayer.getDataset() == null) {
                return false;
            }
            String tempSql = "Update T_Layer Set RenderType=1, SimpleRender='" + this._Symbol.getConfigParas() + "'  Where LayerID='" + this._GeoLayer.getName() + "'";
            if (this._GeoLayer.getDataset().getDataSource().getEditing()) {
                return PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL(tempSql);
            }
            return this._GeoLayer.getDataset().getDataSource().ExcuteSQL(tempSql);
        } catch (Exception e) {
            return false;
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public boolean SaveRenderForVectorLayer() {
        try {
            if (this._GeoLayer == null || this._Symbol == null || this._GeoLayer.getDataset() == null) {
                return false;
            }
            String tempSql = "Update T_Layer Set RenderType=1, SimpleRender='" + this._Symbol.getConfigParas() + "',Transparent='" + getTransparent() + "',IfLabel='" + this._IfLabel + "',LabelField='" + this._LabelDataField + "',LabelFont='" + this._LabelFont + "',VisibleScaleMin=" + this._VisiableScaleMin + ",VisibleScaleMax=" + this._VisiableScaleMax + "  Where LayerID='" + this._GeoLayer.getName() + "'";
            if (this._GeoLayer.getDataset().getDataSource().getEditing()) {
                return PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL(tempSql);
            }
            return this._GeoLayer.getDataset().getDataSource().ExcuteSQL(tempSql);
        } catch (Exception e) {
            return false;
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public Bitmap GetSymbolFigure() {
        if (this._Symbol != null) {
            return this._Symbol.getSymbolFigure();
        }
        return null;
    }

    public Bitmap DrawSymbolFigure() {
        return this._Symbol.DrawSymbolFigure();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public ISymbol getDefaultSymbol() {
        return this._Symbol;
    }
}
