package  com.xzy.forestSystem.gogisapi.Display;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassifiedRender extends IRender {
    private ISymbol DefaultSymbol = null;
    private Bitmap _SymbolFigure = null;
    private List<ISymbol> _SymbolList = new ArrayList();
    private List<String> _UniqueValueField = new ArrayList();
    private List<String> _UniqueValueList = new ArrayList();

    public ClassifiedRender(GeoLayer paramGeoLayer) {
        setType(EDisplayType.CLASSIFIED);
        this._GeoLayer = paramGeoLayer;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public void Dispose2() {
        try {
            if (this.DefaultSymbol != null) {
                this.DefaultSymbol.Dispose2();
            }
            if (this._SymbolList.size() > 0) {
                for (ISymbol tmpISymbol : this._SymbolList) {
                    if (tmpISymbol != null) {
                        tmpISymbol.Dispose2();
                    }
                }
            }
            this._SymbolList.clear();
            this._UniqueValueField.clear();
            this._UniqueValueList.clear();
            if (this._SymbolFigure != null && !this._SymbolFigure.isRecycled()) {
                this._SymbolFigure.recycle();
            }
            this._SymbolFigure = null;
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public void LoadSymbol(FeatureLayer layer) {
        if (!this.isLoad) {
            Object[] tmpObjs = null;
            if (layer != null) {
                tmpObjs = layer.GetLayerRenderData(2);
            }
            LoadSymbol(layer, tmpObjs);
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public void LoadSymbol(FeatureLayer layer, Object[] renderParas) {
        List<String> tmpSymbols;
        try {
            if (!this.isLoad) {
                this._SymbolList = new ArrayList();
                this._UniqueValueField = new ArrayList();
                this._UniqueValueList = new ArrayList();
                boolean tempTag = true;
                if (layer != null) {
                    UpdateFromLayer(layer);
                    if (!(renderParas == null || renderParas.length <= 0 || renderParas[0] == null)) {
                        if (renderParas[0] != null) {
                            this._UniqueValueField = Common.DecodeJSONArray(renderParas[0].toString(), "Data");
                        }
                        if (renderParas[1] != null) {
                            this._UniqueValueList = Common.DecodeJSONArray(renderParas[1].toString(), "Data");
                        }
                        if (renderParas[3] != null) {
                            this.DefaultSymbol = SymbolManage.CreateSymbolByData(renderParas[3].toString(), this._GeoLayer.getType());
                        } else {
                            if (this._GeoLayer.getType() == EGeoLayerType.POINT) {
                                this.DefaultSymbol = PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemPointSymbol("默认");
                            }
                            if (this._GeoLayer.getType() == EGeoLayerType.POLYLINE) {
                                this.DefaultSymbol = PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemLineSymbol("默认");
                            }
                            if (this._GeoLayer.getType() == EGeoLayerType.POLYGON) {
                                this.DefaultSymbol = PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemPolySymbol("默认");
                            }
                        }
                        if (!(renderParas[2] == null || (tmpSymbols = Common.DecodeJSONArray(renderParas[2].toString(), "Data")) == null || tmpSymbols.size() <= 0)) {
                            for (String tempSymStr : tmpSymbols) {
                                if (tempSymStr == null || tempSymStr.equals("")) {
                                    this._SymbolList.add(ISymbol.CreateSymbol(this._GeoLayer.getType()));
                                } else {
                                    this._SymbolList.add(SymbolManage.CreateSymbolByData(tempSymStr, this._GeoLayer.getType()));
                                }
                            }
                        }
                        tempTag = false;
                    }
                }
                if (tempTag) {
                    if (this._GeoLayer.getType() == EGeoLayerType.POINT) {
                        this.DefaultSymbol = PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemPointSymbol("默认");
                    }
                    if (this._GeoLayer.getType() == EGeoLayerType.POLYLINE) {
                        this.DefaultSymbol = PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemLineSymbol("默认");
                    }
                    if (this._GeoLayer.getType() == EGeoLayerType.POLYGON) {
                        this.DefaultSymbol = PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemPolySymbol("默认");
                    }
                    this._SymbolList.add(this.DefaultSymbol);
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
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public void UpdateSymbol(AbstractGeometry paramGeometry) {
        paramGeometry.setSymbol(this.DefaultSymbol);
    }

    public void UpdateSymbol(AbstractGeometry paramGeometry, int symbolIndex) {
        if (symbolIndex > -1) {
            paramGeometry.setSymbol(this._SymbolList.get(symbolIndex));
        } else {
            paramGeometry.setSymbol(this.DefaultSymbol);
        }
    }

    public void UpdateSymbolByTag(AbstractGeometry paramGeometry) {
        UpdateSymbol(paramGeometry, this._UniqueValueList.indexOf(paramGeometry.getTag()));
    }

    public void UpdateSymbolByValue(AbstractGeometry paramGeometry, String value) {
        UpdateSymbol(paramGeometry, this._UniqueValueList.indexOf(value));
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public void SetSymbolTransparent(int transparent) {
        this._SymbolTransparent = transparent;
        if (this.DefaultSymbol != null) {
            this.DefaultSymbol.SetTransparent(this._SymbolTransparent);
            for (ISymbol iSymbol : this._SymbolList) {
                iSymbol.SetTransparent(this._SymbolTransparent);
            }
        }
    }

    public List<String> getUniqueValueField() {
        return this._UniqueValueField;
    }

    public List<String> getUniqueValueList() {
        return this._UniqueValueList;
    }

    public void setUniqueValueList(List<String> values) {
        this._UniqueValueList = values;
    }

    public void setDefaultSymbol(ISymbol symbol) {
        this.DefaultSymbol = symbol;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public boolean HasUsedSymbol(String symbolName) {
        return false;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public boolean SaveRender() {
        try {
            if (this._GeoLayer == null || this._SymbolList == null || this._SymbolList.size() <= 0 || this._GeoLayer.getDataset() == null) {
                return false;
            }
            String tempLayerID = this._GeoLayer.getName();
            StringBuilder tempSB = new StringBuilder();
            tempSB.append("Update T_Layer Set RenderType = 2, UniqueValueField='");
            tempSB.append(Common.EnCodeJSONArray(this._UniqueValueField, "Data"));
            tempSB.append("' , UniqueValueList='");
            tempSB.append(Common.EnCodeJSONArray(this._UniqueValueList, "Data"));
            tempSB.append("' , UniqueSymbolList='");
            List<String> tempSymbolsList = new ArrayList<>();
            for (ISymbol tempSymbol : this._SymbolList) {
                if (tempSymbol != null) {
                    tempSymbolsList.add(tempSymbol.getConfigParas());
                }
            }
            tempSB.append(Common.EnCodeJSONArray(tempSymbolsList, "Data"));
            tempSB.append("' , UniqueDefaultSymbol='");
            tempSB.append(this.DefaultSymbol.getConfigParas());
            tempSB.append("' Where LayerID='" + tempLayerID + "'");
            if (this._GeoLayer.getDataset().getDataSource().getEditing()) {
                return PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL(tempSB.toString());
            }
            return this._GeoLayer.getDataset().getDataSource().ExcuteSQL(tempSB.toString());
        } catch (Exception e) {
            return false;
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public boolean SaveRenderForVectorLayer() {
        try {
            if (this._GeoLayer == null || this._SymbolList == null || this._SymbolList.size() <= 0 || this._GeoLayer.getDataset() == null) {
                return false;
            }
            String tempLayerID = this._GeoLayer.getName();
            StringBuilder tempSB = new StringBuilder();
            tempSB.append("Update T_Layer Set RenderType = 2, UniqueValueField='");
            tempSB.append(Common.EnCodeJSONArray(this._UniqueValueField, "Data"));
            tempSB.append("' , UniqueValueList='");
            tempSB.append(Common.EnCodeJSONArray(this._UniqueValueList, "Data"));
            tempSB.append("' , UniqueSymbolList='");
            List<String> tempSymbolsList = new ArrayList<>();
            for (ISymbol tempSymbol : this._SymbolList) {
                if (tempSymbol != null) {
                    tempSymbolsList.add(tempSymbol.getConfigParas());
                }
            }
            tempSB.append(Common.EnCodeJSONArray(tempSymbolsList, "Data"));
            tempSB.append("' , UniqueDefaultSymbol='");
            tempSB.append(this.DefaultSymbol.getConfigParas());
            tempSB.append("'");
            if (this._GeoLayer.getDataset().getDataSource().getEditing()) {
                tempSB.append(",Transparent='" + getTransparent() + "',IfLabel='" + this._IfLabel + "',LabelField='" + this._LabelDataField + "',LabelFont='" + this._LabelFont + "',F5='" + this._LabelSplitChar + "',VisibleScaleMin=" + this._VisiableScaleMin + ",VisibleScaleMax=" + this._VisiableScaleMax);
                tempSB.append(" Where LayerID='" + tempLayerID + "'");
                return PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL(tempSB.toString());
            }
            tempSB.append(",Transparent='" + getTransparent() + "',IfLabel='" + this._IfLabel + "',LabelField='" + this._LabelDataField + "',LabelFont='" + this._LabelFont + "',VisibleScaleMin=" + this._VisiableScaleMin + ",VisibleScaleMax=" + this._VisiableScaleMax);
            tempSB.append(" Where LayerID='" + tempLayerID + "'");
            return this._GeoLayer.getDataset().getDataSource().ExcuteSQL(tempSB.toString());
        } catch (Exception e) {
            return false;
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public Bitmap GetSymbolFigure() {
        if (this._SymbolFigure == null) {
            this._SymbolFigure = BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.classfiedcolor32);
        }
        return this._SymbolFigure;
    }

    public List<ISymbol> getSymbolList() {
        return this._SymbolList;
    }

    public void setSymbolList(List<ISymbol> symbolList) {
        this._SymbolList = symbolList;
    }

    public void SetUniqueValueField(String datafields) {
        String[] tempStrs;
        if (datafields != null && (tempStrs = datafields.split(",")) != null && tempStrs.length > 0) {
            this._UniqueValueField = Arrays.asList(tempStrs);
        }
    }

    public int findSymbolIndexByValue(String fieldsValue) {
        int result = this._UniqueValueList.indexOf(fieldsValue);
        if (result >= this._SymbolList.size()) {
            return -1;
        }
        return result;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.IRender
    public ISymbol getDefaultSymbol() {
        return this.DefaultSymbol;
    }
}
