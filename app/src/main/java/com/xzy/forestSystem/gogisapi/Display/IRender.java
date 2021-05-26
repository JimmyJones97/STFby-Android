package  com.xzy.forestSystem.gogisapi.Display;

import android.graphics.Bitmap;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;

public abstract class IRender implements Cloneable {
    GeoLayer _GeoLayer = null;
    boolean _IfLabel = false;
    String _LabelDataField = "";
    String _LabelFont = "#000000,10";
    double _LabelScaleMax = 2.147483647E9d;
    double _LabelScaleMin = 0.0d;
    String _LabelSplitChar = ",";
    int _SymbolTransparent = 0;
    TextSymbol _TextSymbol = null;
    EDisplayType _Type = EDisplayType.SIMPLE;
    double _VisiableScaleMax = 2.147483647E9d;
    double _VisiableScaleMin = 0.0d;
    boolean isLoad = false;
    public boolean needUpdateLableContent = false;
    public boolean needUpdateSymbol = false;

    public abstract void Dispose2();

    public abstract Bitmap GetSymbolFigure();

    public abstract boolean HasUsedSymbol(String str);

    public abstract void LoadSymbol(FeatureLayer featureLayer);

    public abstract void LoadSymbol(FeatureLayer featureLayer, Object[] objArr);

    public abstract boolean SaveRender();

    public abstract boolean SaveRenderForVectorLayer();

    public abstract void SetSymbolTransparent(int i);

    public abstract void UpdateSymbol(AbstractGeometry geometry);

    public abstract ISymbol getDefaultSymbol();

    public void ReLoadSymbol(FeatureLayer layer) {
        this.isLoad = false;
        LoadSymbol(layer);
    }

    public static IRender CreateRender(GeoLayer geoLayer, int rendType) {
        if (rendType == 2) {
            return new ClassifiedRender(geoLayer);
        }
        return new SimpleDisplay(geoLayer);
    }

    public boolean getIfLabel() {
        return this._IfLabel;
    }

    public String getLabelDataField() {
        return this._LabelDataField;
    }

    public String getLabelSplitChar() {
        return this._LabelSplitChar;
    }

    public void SetLabelSplitChar(String splitChar) {
        this._LabelSplitChar = splitChar;
    }

    public String getLabelFont() {
        return this._LabelFont;
    }

    public double getLabelScaleMax() {
        return this._LabelScaleMax;
    }

    public double getLabelScaleMin() {
        return this._LabelScaleMin;
    }

    public double GetMinScale() {
        return this._VisiableScaleMin;
    }

    public double GetMaxScale() {
        return this._VisiableScaleMax;
    }

    public void SetMinScale(double value) {
        this._VisiableScaleMin = value;
    }

    public void SetMaxScale(double value) {
        this._VisiableScaleMax = value;
    }

    public EDisplayType getType() {
        return this._Type;
    }

    public void setIfLabel(boolean paramBoolean) {
        this._IfLabel = paramBoolean;
    }

    public void SetGeoLayer(GeoLayer geoLayer) {
        this._GeoLayer = geoLayer;
    }

    public void setLabelDataField(String paramString) {
        this._LabelDataField = paramString;
    }

    public void setLabelScaleMax(double paramDouble) {
        this._LabelScaleMax = paramDouble;
    }

    public void setLabelScaleMin(double paramDouble) {
        this._LabelScaleMin = paramDouble;
    }

    public void setType(EDisplayType paramlkRenderType) {
        this._Type = paramlkRenderType;
    }

    public void UpdateFromLayer(FeatureLayer layer) {
        this._IfLabel = layer.GetIfLabel();
        this._LabelDataField = layer.GetLabelDataField();
        this._LabelFont = layer.GetLabelFont();
        this._LabelScaleMax = layer.GetLabelScaleMax();
        this._LabelScaleMin = layer.GetLabelScaleMin();
        this._VisiableScaleMax = layer.GetMaxScale();
        this._VisiableScaleMin = layer.GetMinScale();
        this._SymbolTransparent = layer.GetTransparet();
        this._LabelSplitChar = layer.getLabelSplitChar();
    }

    public void UpdateToLayer(FeatureLayer layer) {
        layer.SetIfLabel(this._IfLabel);
        layer.SetLabelDataField(this._LabelDataField);
        layer.SetLabelFont(this._LabelFont);
        layer.SetLabelScaleMax(this._LabelScaleMax);
        layer.SetLabelScaleMin(this._LabelScaleMin);
        layer.SetMaxScale(this._VisiableScaleMax);
        layer.SetMinScale(this._VisiableScaleMin);
        layer.SetTransparent(this._SymbolTransparent);
        layer.SetLabelSplitChar(this._LabelSplitChar);
        if (this._Type == EDisplayType.SIMPLE) {
            layer.SetRenderType(1);
        } else if (this._Type == EDisplayType.CLASSIFIED) {
            layer.SetRenderType(2);
        }
    }

    public TextSymbol getTextSymbol() {
        if (this._TextSymbol == null) {
            this._TextSymbol = new TextSymbol();
        }
        return this._TextSymbol;
    }

    public void setTextSymbol(TextSymbol paramTextSymbol) {
        this._TextSymbol = paramTextSymbol;
    }

    public void setTextSymbolFont(String labelFont) {
        this._LabelFont = labelFont;
        getTextSymbol().SetTextSymbolFont(labelFont);
    }

    public int getTransparent() {
        return this._SymbolTransparent;
    }

    public void CopyParasFrom(IRender pRender) {
        this._IfLabel = pRender._IfLabel;
        this._LabelDataField = pRender._LabelDataField;
        this._LabelFont = pRender._LabelFont;
        this._LabelScaleMax = pRender._LabelScaleMax;
        this._LabelScaleMin = pRender._LabelScaleMin;
        this._TextSymbol = pRender._TextSymbol;
        this._VisiableScaleMax = pRender._VisiableScaleMax;
        this._VisiableScaleMin = pRender._VisiableScaleMin;
        this._SymbolTransparent = pRender._SymbolTransparent;
        this.needUpdateLableContent = pRender.needUpdateLableContent;
        this._LabelSplitChar = pRender._LabelSplitChar;
    }

    public boolean SaveLabelSetting() {
        try {
            if (this._GeoLayer == null || this._GeoLayer.getDataset() == null) {
                return false;
            }
            String tempLayerID = this._GeoLayer.getName();
            if (this._GeoLayer.getDataset().getDataSource().getEditing()) {
                return PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL("Update T_Layer Set IfLabel='" + String.valueOf(this._IfLabel) + "',LabelField='" + this._LabelDataField + "',LabelFont='" + this._LabelFont + "',LabelScaleMin=" + String.valueOf(this._LabelScaleMin) + ",LabelScaleMax=" + String.valueOf(this._LabelScaleMax) + ",F5=" + String.valueOf(this._LabelSplitChar) + "  Where LayerID='" + tempLayerID + "'");
            }
            return this._GeoLayer.getDataset().getDataSource().ExcuteSQL("Update T_Layer Set IfLabel='" + String.valueOf(this._IfLabel) + "',LabelField='" + this._LabelDataField + "',LabelFont='" + this._LabelFont + "',LabelScaleMin=" + String.valueOf(this._LabelScaleMin) + ",LabelScaleMax=" + String.valueOf(this._LabelScaleMax) + "  Where LayerID='" + tempLayerID + "'");
        } catch (Exception e) {
            return false;
        }
    }

    @Override // java.lang.Object
    public IRender clone() throws CloneNotSupportedException {
        return (IRender) super.clone();
    }
}
