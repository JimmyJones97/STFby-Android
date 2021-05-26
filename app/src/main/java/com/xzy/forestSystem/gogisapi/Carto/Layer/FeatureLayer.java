package  com.xzy.forestSystem.gogisapi.Carto.Layer;

import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Display.IRender;
import  com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class FeatureLayer {
    private String _DataSourceName = "";
    private String _DefaultSymbol = "";
    private EEditMode _EditMode = EEditMode.NONE;
    private boolean _Editable = true;
    private List<Double> _FParas = new ArrayList();
    private List<LayerField> _FieldList = new ArrayList();
    private boolean _IfLabel = false;
    private String _LabelDataField = "";
    private String _LabelField = "";
    private String _LabelFont = "#000000,10";
    private double _LabelScaleMax = 2.147483647E9d;
    private double _LabelScaleMin = 0.0d;
    private String _LabelSplitChar = ",";
    private String _LayerID = (/*NDEFRecord.TEXT_WELL_KNOWN_TYPE +*/ UUID.randomUUID().toString().replace("-", "").toUpperCase());
    private int _LayerIndex = 0;
    private String _LayerName = "";
    private EGeoLayerType _LayerType = EGeoLayerType.NONE;
    private double _MaxX = 2.147483647E9d;
    private double _MaxY = 2.147483647E9d;
    private double _MinX = 0.0d;
    private double _MinY = 0.0d;
    private int _RenderType = 1;
    private boolean _Selectable = true;
    private boolean _Snapable = true;
    private String _SymbolName = "默认";
    private int _SymbolTransparent = 0;
    private Object _TAGValue = null;
    private double _VisiableScaleMax = 2.147483647E9d;
    private double _VisiableScaleMin = 0.0d;
    private boolean _Visible = true;
    private LinkedHashMap<String, String> _fieldsNameArray = new LinkedHashMap<>();
    private List<String> _fieldsNameList = new ArrayList();

    public void Dispose() {
        this._FieldList.clear();
        this._FParas.clear();
        this._fieldsNameArray.clear();
        this._fieldsNameList.clear();
    }

    public FeatureLayer Clone() {
        FeatureLayer localv1_Layer = new FeatureLayer();
        CopyTo(localv1_Layer);
        return localv1_Layer;
    }

    public void CopyTo(FeatureLayer layer) {
        if (layer != null) {
            layer.SetLayerID(GetLayerID());
            layer.SetDataSourceName(this._DataSourceName);
            layer.SetVisible(GetVisible());
            layer.SetLayerName(GetLayerName());
            layer.SetLayerTypeName(GetLayerTypeName());
            layer.SetSymbolName(GetSymbolName());
            layer.SetTransparent(GetTransparet());
            layer.SetFieldList(GetFieldListStr());
            layer.SetIfLabel(GetIfLabel());
            layer.SetLabelField(GetLabelField());
            layer.SetLabelFont(GetLabelFont());
            layer._LabelSplitChar = this._LabelSplitChar;
            layer.SetMaxScale(this._VisiableScaleMax);
            layer.SetMinScale(this._VisiableScaleMin);
            layer.SetRenderType(this._RenderType);
            layer.SetDefaultSymbol(GetDefaultSymbol());
            layer.SetSymbolName(GetSymbolName());
        }
    }

    public void SetRenderConfig(GeoLayer geoLayer) {
        this._Visible = geoLayer.getVisible();
        this._SymbolTransparent = geoLayer.getTransparent();
        IRender tmpRender = geoLayer.getRender();
        if (tmpRender != null) {
            this._IfLabel = tmpRender.getIfLabel();
            this._LabelDataField = tmpRender.getLabelDataField();
            this._LabelFont = tmpRender.getLabelSplitChar();
            this._LabelSplitChar = tmpRender.getLabelSplitChar();
            this._VisiableScaleMax = tmpRender.GetMaxScale();
            this._VisiableScaleMin = tmpRender.GetMinScale();
        }
    }

    public EEditMode GetEditMode() {
        return this._EditMode;
    }

    public List<LayerField> GetFieldList() {
        return this._FieldList;
    }

    public String GetDataFieldsString() {
        StringBuilder tempSB = new StringBuilder();
        for (LayerField layerField : this._FieldList) {
            if (tempSB.length() > 0) {
                tempSB.append(",");
            }
            tempSB.append(layerField.GetDataFieldName());
        }
        return tempSB.toString();
    }

    public String GetFieldNamesString() {
        StringBuilder tempSB = new StringBuilder();
        for (LayerField layerField : this._FieldList) {
            if (tempSB.length() > 0) {
                tempSB.append(",");
            }
            tempSB.append(layerField.GetFieldName());
        }
        return tempSB.toString();
    }

    public List<String> GetFieldNamesList() {
        List<String> result = new ArrayList<>();
        for (LayerField layerField : this._FieldList) {
            result.add(layerField.GetFieldName());
        }
        return result;
    }

    public String GetFieldListStr() {
        return GetFieldListJSONArray().toString();
    }

    public JSONObject GetFieldListJSONArray() {
        JSONObject result = new JSONObject();
        try {
            JSONArray localObject = new JSONArray();
            for (LayerField tempField : this._FieldList) {
                localObject.put(tempField.GetFieldJSONObject());
            }
            result.put("Data", localObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String GetFieldNameByDataFieldName2(String paramString) {
        LayerField tempFiled = null;
        Iterator<LayerField> localIterator = this._FieldList.iterator();
        while (localIterator.hasNext()) {
            tempFiled = localIterator.next();
            if (tempFiled.GetDataFieldName().equals(paramString)) {
                break;
            }
        }
        return tempFiled.GetFieldName();
    }

    public String GetFieldNameByDataFieldName(String fieldID) {
        if (getFieldsNameArray().containsValue(fieldID)) {
            for (Map.Entry<String, String> entry : this._fieldsNameArray.entrySet()) {
                if (entry.getValue().equals(fieldID)) {
                    return entry.getKey().toString();
                }
            }
        }
        return "";
    }

    public String GetDataFieldByFieldName(String fieldName) {
        if (getFieldsNameArray().containsKey(fieldName)) {
            return this._fieldsNameArray.get(fieldName);
        }
        return "";
    }

    public boolean GetIfLabel() {
        return this._IfLabel;
    }

    public String GetLabelDataField() {
        return this._LabelDataField;
    }

    public String GetLabelField() {
        return this._LabelField;
    }

    public String GetLabelFont() {
        if (this._LabelFont.equals("")) {
            this._LabelFont = "#000000,10";
        }
        return this._LabelFont;
    }

    public String GetLayerID() {
        return this._LayerID;
    }

    public String GetLayerName() {
        return this._LayerName;
    }

    public EGeoLayerType GetLayerType() {
        return this._LayerType;
    }

    public String GetLayerTypeName() {
        if (this._LayerType == EGeoLayerType.POINT) {
            return "点";
        }
        if (this._LayerType == EGeoLayerType.POLYLINE) {
            return "线";
        }
        if (this._LayerType == EGeoLayerType.POLYGON) {
            return "面";
        }
        return "";
    }

    public String GetSymbolName() {
        if (this._SymbolName.equals("")) {
            this._SymbolName = "默认";
        }
        return this._SymbolName;
    }

    public int GetTransparet() {
        return this._SymbolTransparent;
    }

    public boolean GetVisible() {
        return this._Visible;
    }

    public void SetSelectable(boolean value) {
        this._Selectable = value;
    }

    public boolean GetSelectable() {
        return this._Selectable;
    }

    public void SetEditMode(EEditMode paramlkEditMode) {
        this._EditMode = paramlkEditMode;
    }

    public void SetFieldList(String paramString) {
        this._fieldsNameArray = new LinkedHashMap<>();
        this._fieldsNameList = new ArrayList();
        this._FieldList.clear();
        try {
            JSONArray jsonArray = ((JSONObject) new JSONTokener(paramString).nextValue()).getJSONArray("Data");
            if (jsonArray.length() > 0) {
                List<String> tmpFIDsArray = new ArrayList<>();
                boolean tmpBoolean = false;
                int count = jsonArray.length();
                for (int i = 0; i < count; i++) {
                    JSONObject tempJson = jsonArray.getJSONObject(i);
                    LayerField tempField = new LayerField();
                    if (tempField.ReadJsonData(tempJson)) {
                        if (tmpFIDsArray.contains(tempField.GetDataFieldName())) {
                            tempField.SetDataFieldName("");
                            tmpBoolean = true;
                        } else {
                            tmpFIDsArray.add(tempField.GetDataFieldName());
                        }
                        this._FieldList.add(tempField);
                        this._fieldsNameArray.put(tempField.GetFieldName(), tempField.GetDataFieldName());
                        this._fieldsNameList.add(tempField.GetFieldName());
                    }
                }
                if (tmpBoolean) {
                    int tmpTid = this._FieldList.size();
                    for (LayerField tmpLayerField : this._FieldList) {
                        if (tmpLayerField.GetDataFieldName().length() == 0) {
                            String tmpFIDNameString = "F" + String.valueOf(tmpTid);
                            while (tmpFIDsArray.contains(tmpFIDNameString)) {
                                tmpTid++;
                                tmpFIDNameString = "F" + String.valueOf(tmpTid);
                            }
                            tmpFIDsArray.add(tmpFIDNameString);
                            tmpLayerField.SetDataFieldName(tmpFIDNameString);
                            this._fieldsNameArray.put(tmpLayerField.GetFieldName(), tmpFIDNameString);
                            this._fieldsNameList.add(tmpLayerField.GetFieldName());
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, String> getFieldsNameArray() {
        if (this._fieldsNameArray.size() != this._FieldList.size()) {
            this._fieldsNameArray.clear();
            this._fieldsNameList.clear();
            for (LayerField tmpLayerField : this._FieldList) {
                this._fieldsNameArray.put(tmpLayerField.GetFieldName(), tmpLayerField.GetDataFieldName());
                this._fieldsNameList.add(tmpLayerField.GetFieldName());
            }
        }
        return this._fieldsNameArray;
    }

    public void SetFieldList(List<LayerField> paramList) {
        this._FieldList = paramList;
    }

    public void SetIfLabel(boolean paramBoolean) {
        this._IfLabel = paramBoolean;
    }

    public void SetLabelField(String paramString) {
        String[] tempStrs;
        this._LabelField = paramString;
        StringBuilder tempShowFields = new StringBuilder();
        if (!this._LabelField.equals("") && (tempStrs = this._LabelField.split(",")) != null && tempStrs.length > 0) {
            for (String tempStr01 : tempStrs) {
                if (!tempStr01.equals("")) {
                    String tempStr02 = GetDataFieldByFieldName(tempStr01);
                    if (!tempStr02.equals("")) {
                        tempShowFields.append(tempStr02);
                        tempShowFields.append(",");
                    }
                }
            }
            if (tempShowFields.length() > 0 && tempShowFields.lastIndexOf(",") == tempShowFields.length() - 1) {
                tempShowFields = tempShowFields.deleteCharAt(tempShowFields.length() - 1);
            }
        }
        this._LabelDataField = tempShowFields.toString();
    }

    public void SetLabelDataField(String labelDataField) {
        String[] tempStrs01;
        this._LabelDataField = labelDataField;
        StringBuilder tempStrB = new StringBuilder();
        if (!this._LabelDataField.equals("") && (tempStrs01 = this._LabelDataField.split(",")) != null && tempStrs01.length > 0) {
            for (String tempStr001 : tempStrs01) {
                if (!tempStr001.equals("")) {
                    String tempFieldName01 = GetFieldNameByDataFieldName(tempStr001);
                    if (!tempFieldName01.equals("")) {
                        tempStrB.append(tempFieldName01);
                        tempStrB.append(",");
                    }
                }
            }
            if (tempStrB.length() > 0 && tempStrB.lastIndexOf(",") == tempStrB.length() - 1) {
                tempStrB = tempStrB.deleteCharAt(tempStrB.length() - 1);
            }
        }
        this._LabelField = tempStrB.toString();
    }

    public void SetLabelFont(String paramString) {
        this._LabelFont = paramString;
    }

    public void SetSnapable(boolean value) {
        this._Snapable = value;
    }

    public boolean GetSnapable() {
        return this._Snapable;
    }

    public void SetLabelScaleMin(double value) {
        this._LabelScaleMin = value;
    }

    public double GetLabelScaleMin() {
        return this._LabelScaleMin;
    }

    public void SetLabelScaleMax(double value) {
        this._LabelScaleMax = value;
    }

    public double GetLabelScaleMax() {
        return this._LabelScaleMax;
    }

    public void SetLayerID(String paramString) {
        this._LayerID = paramString;
    }

    public void SetLayerName(String paramString) {
        this._LayerName = paramString;
    }

    public void SetLayerTypeName(String paramString) {
        if (paramString.equals("点")) {
            this._LayerType = EGeoLayerType.POINT;
        } else if (paramString.equals("线")) {
            this._LayerType = EGeoLayerType.POLYLINE;
        } else if (paramString.equals("面")) {
            this._LayerType = EGeoLayerType.POLYGON;
        }
    }

    public void SetSymbolName(String paramString) {
        this._SymbolName = paramString;
    }

    public void SetTransparent(int paramInt) {
        this._SymbolTransparent = paramInt;
    }

    public void SetVisible(boolean paramBoolean) {
        this._Visible = paramBoolean;
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

    public int GetRenderType() {
        return this._RenderType;
    }

    public void SetRenderType(int value) {
        this._RenderType = value;
    }

    public boolean GetEditable() {
        return this._Editable;
    }

    public void SetEditable(boolean value) {
        this._Editable = value;
    }

    public void SetLayerIndex(int layerIndex) {
        this._LayerIndex = layerIndex;
    }

    public int GetLayerIndex() {
        return this._LayerIndex;
    }

    public String GetDefaultSymbol() {
        return this._DefaultSymbol;
    }

    public void SetDefaultSymbol(String simpleRender) {
        this._DefaultSymbol = simpleRender;
    }

    public List<Double> GetFParas() {
        return this._FParas;
    }

    public void SetFParas(List<Double> value) {
        this._FParas = value;
    }

    public boolean ReadLayerInfo(SQLiteReader sqlReader) {
        try {
            Arrays.asList(sqlReader.GetFieldNameList());
            String tempStr = sqlReader.GetString("SortId");
            if (tempStr != null && !tempStr.equals("")) {
                this._LayerIndex = Integer.parseInt(tempStr);
            }
            this._LayerName = sqlReader.GetString("Name");
            String tempStr2 = sqlReader.GetString("LayerId");
            if (tempStr2 != null && !tempStr2.equals("")) {
                this._LayerID = tempStr2;
            }
            SetLayerTypeName(sqlReader.GetString("Type"));
            try {
                String tempStr3 = sqlReader.GetString("Visible");
                if (tempStr3 != null && !tempStr3.equals("")) {
                    this._Visible = Boolean.parseBoolean(tempStr3);
                }
                String tempStr4 = sqlReader.GetString("Transparent");
                if (tempStr4 != null && !tempStr4.equals("")) {
                    this._SymbolTransparent = Integer.parseInt(tempStr4);
                }
                String tempStr5 = sqlReader.GetString("IfLabel");
                if (tempStr5 != null && !tempStr5.equals("")) {
                    this._IfLabel = Boolean.parseBoolean(tempStr5);
                }
            } catch (Exception e) {
            }
            try {
                SetFieldList(sqlReader.GetString("FieldList"));
                SetLabelDataField(sqlReader.GetString("LabelField"));
                this._LabelFont = sqlReader.GetString("LabelFont");
            } catch (Exception e2) {
            }
            try {
                String tempStr6 = sqlReader.GetString("LabelScaleMin");
                if (tempStr6 != null && !tempStr6.equals("")) {
                    this._LabelScaleMin = Double.parseDouble(tempStr6);
                }
                String tempStr7 = sqlReader.GetString("LabelScaleMax");
                if (tempStr7 != null && !tempStr7.equals("")) {
                    this._LabelScaleMax = Double.parseDouble(tempStr7);
                }
                String tempStr8 = sqlReader.GetString("MinX");
                if (tempStr8 != null && !tempStr8.equals("")) {
                    this._MinX = Double.parseDouble(tempStr8);
                }
                String tempStr9 = sqlReader.GetString("MinY");
                if (tempStr9 != null && !tempStr9.equals("")) {
                    this._MinY = Double.parseDouble(tempStr9);
                }
                String tempStr10 = sqlReader.GetString("MaxX");
                if (tempStr10 != null && !tempStr10.equals("")) {
                    this._MaxX = Double.parseDouble(tempStr10);
                }
                String tempStr11 = sqlReader.GetString("MaxY");
                if (tempStr11 != null && !tempStr11.equals("")) {
                    this._MaxY = Double.parseDouble(tempStr11);
                }
                String tempStr12 = sqlReader.GetString("VisibleScaleMin");
                if (tempStr12 != null && !tempStr12.equals("")) {
                    this._VisiableScaleMin = Double.parseDouble(tempStr12);
                }
                String tempStr13 = sqlReader.GetString("VisibleScaleMax");
                if (tempStr13 != null && !tempStr13.equals("")) {
                    this._VisiableScaleMax = Double.parseDouble(tempStr13);
                }
                if (this._VisiableScaleMax < this._LabelScaleMin) {
                    this._VisiableScaleMax = this._LabelScaleMin;
                }
            } catch (Exception e3) {
                if (this._VisiableScaleMax < this._LabelScaleMin) {
                    this._VisiableScaleMax = this._LabelScaleMin;
                }
            } catch (Throwable th) {
                if (this._VisiableScaleMax < this._LabelScaleMin) {
                    this._VisiableScaleMax = this._LabelScaleMin;
                }
                throw th;
            }
            try {
                String tempStr14 = sqlReader.GetString("Selectable");
                if (tempStr14 != null && !tempStr14.equals("")) {
                    this._Selectable = Boolean.parseBoolean(tempStr14);
                }
                String tempStr15 = sqlReader.GetString("Editable");
                if (tempStr15 != null && !tempStr15.equals("")) {
                    this._Editable = Boolean.parseBoolean(tempStr15);
                }
                String tempStr16 = sqlReader.GetString("Snapable");
                if (tempStr16 != null && !tempStr16.equals("")) {
                    this._Snapable = Boolean.parseBoolean(tempStr16);
                }
                String tempStr17 = sqlReader.GetString("RenderType");
                if (tempStr17 != null && !tempStr17.equals("")) {
                    this._RenderType = Integer.parseInt(tempStr17);
                }
                if (this._RenderType <= 0) {
                    this._RenderType = 1;
                }
                if (this._RenderType == 1) {
                    SetDefaultSymbol(sqlReader.GetString("SimpleRender"));
                } else {
                    SetDefaultSymbol(sqlReader.GetString("UniqueDefaultSymbol"));
                }
            } catch (Exception e4) {
            }
            try {
                this._LabelSplitChar = sqlReader.GetString("F5");
                if (this._LabelSplitChar == null) {
                    this._LabelSplitChar = ",";
                }
            } catch (Exception e5) {
            }
            return true;
        } catch (Exception ex) {
            Common.Log("ReadLayerInfo", "错误:" + ex.toString() + "-->" + ex.getMessage());
            return false;
        }
    }

    public Object[] GetLayerRenderData() {
        return GetLayerRenderData(PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase(), this._RenderType);
    }

    public Object[] GetLayerRenderData(int renderType) {
        return GetLayerRenderData(PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase(), renderType);
    }

    public void SetExtend(Envelope extend) {
        this._MinX = extend.getMinX();
        this._MinY = extend.getMinY();
        this._MaxX = extend.getMaxX();
        this._MaxY = extend.getMaxY();
    }

    public Object[] GetLayerRenderData(SQLiteDBHelper _ASQLiteDatabase, int renderType) {
        SQLiteReader localSQLiteDataReader = _ASQLiteDatabase.Query("select * from T_Layer Where LayerID='" + this._LayerID + "'");
        if (localSQLiteDataReader == null || !localSQLiteDataReader.Read()) {
            return null;
        }
        if (renderType == 1) {
            return new Object[]{localSQLiteDataReader.GetString("SimpleRender")};
        }
        if (renderType > 1) {
            return new Object[]{localSQLiteDataReader.GetString("UniqueValueField"), localSQLiteDataReader.GetString("UniqueValueList"), localSQLiteDataReader.GetString("UniqueSymbolList"), localSQLiteDataReader.GetString("UniqueDefaultSymbol")};
        }
        return null;
    }

    public boolean SaveLayerInfo() {
        String tempSql;
        try {
            Object[] tempValues = {Integer.valueOf(this._LayerIndex), this._LayerName, this._LayerID, GetLayerTypeName(), Boolean.valueOf(this._Visible), Integer.valueOf(this._SymbolTransparent), Boolean.valueOf(this._IfLabel), this._LabelDataField, this._LabelFont, GetFieldListStr(), Double.valueOf(this._LabelScaleMin), Double.valueOf(this._LabelScaleMax), Double.valueOf(this._MinX), Double.valueOf(this._MinY), Double.valueOf(this._MaxX), Double.valueOf(this._MaxY), Double.valueOf(this._VisiableScaleMin), Double.valueOf(this._VisiableScaleMax), Boolean.valueOf(this._Selectable), Boolean.valueOf(this._Editable), Boolean.valueOf(this._Snapable), Integer.valueOf(this._RenderType), this._DefaultSymbol, this._LabelSplitChar};
            boolean isExist = false;
            SQLiteReader sqlReader = PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().Query("Select LayerId From T_Layer Where LayerId='" + this._LayerID + "'");
            if (sqlReader != null) {
                if (sqlReader.Read()) {
                    isExist = true;
                }
                sqlReader.Close();
            }
            if (isExist) {
                tempSql = String.format("Update T_Layer Set SortId=%1$s,Name='%2$s',Type='%4$s',Visible='%5$s',Transparent='%6$s',IfLabel='%7$s',LabelField='%8$s',LabelFont='%9$s',FieldList='%10$s',LabelScaleMin=%11$s,LabelScaleMax=%12$s,MinX=%13$s,MinY=%14$s,MaxX=%15$s,MaxY=%16$s,VisibleScaleMin=%17$s,VisibleScaleMax=%18$s,Selectable='%19$s',Editable='%20$s',Snapable='%21$s',RenderType='%22$s',F5='%24$s' Where LayerId='%3$s'", tempValues);
            } else {
                tempSql = String.format("insert into T_Layer (SortId,Name,LayerId,Type,Visible,Transparent,IfLabel,LabelField,LabelFont,FieldList,LabelScaleMin,LabelScaleMax,MinX,MinY,MaxX,MaxY,VisibleScaleMin,VisibleScaleMax,Selectable,Editable,Snapable,RenderType,SimpleRender,F5) values (%1$s,'%2$s','%3$s','%4$s','%5$s','%6$s','%7$s','%8$s','%9$s','%10$s',%11$s,%12$s,%13$s,%14$s,%15$s,%16$s,%17$s,%18$s,'%19$s','%20$s','%21$s','%22$s','%23$s','%24$s')", tempValues);
            }
            return PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL(tempSql);
        } catch (Exception ex) {
            Common.Log("SaveLayerInfo", "错误:" + ex.toString() + "-->" + ex.getMessage());
            return false;
        }
    }

    public boolean SaveLayerExtend() {
        try {
            return PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL(String.format("Update T_Layer Set MinX=%2$s,MinY=%3$s,MaxX=%4$s,MaxY=%5$s Where LayerId='%1$s' ", this._LayerID, Double.valueOf(this._MinX), Double.valueOf(this._MinY), Double.valueOf(this._MaxX), Double.valueOf(this._MaxY)));
        } catch (Exception ex) {
            Common.Log("SaveLayerExtend", "错误:" + ex.toString() + "-->" + ex.getMessage());
            return false;
        }
    }

    public String ConvertToDataField(String fieldNames) {
        String[] tempStrs01;
        StringBuilder tempStrB = new StringBuilder();
        if (!fieldNames.equals("") && (tempStrs01 = fieldNames.split(",")) != null && tempStrs01.length > 0) {
            for (String tempStr001 : tempStrs01) {
                if (!tempStr001.equals("")) {
                    String tempFieldName01 = GetDataFieldByFieldName(tempStr001);
                    if (!tempFieldName01.equals("")) {
                        tempStrB.append(tempFieldName01);
                        tempStrB.append(",");
                    }
                }
            }
            if (tempStrB.length() > 0 && tempStrB.lastIndexOf(",") == tempStrB.length() - 1) {
                tempStrB = tempStrB.deleteCharAt(tempStrB.length() - 1);
            }
        }
        return tempStrB.toString();
    }

    public String ConvertDataFieldToName(String dataFieldsName) {
        String[] tempStrs01;
        StringBuilder tempStrB = new StringBuilder();
        if (!dataFieldsName.equals("") && (tempStrs01 = dataFieldsName.split(",")) != null && tempStrs01.length > 0) {
            for (String tempStr001 : tempStrs01) {
                if (!tempStr001.equals("")) {
                    String tempFieldName01 = GetFieldNameByDataFieldName(tempStr001);
                    if (!tempFieldName01.equals("")) {
                        tempStrB.append(tempFieldName01);
                        tempStrB.append(",");
                    }
                }
            }
            if (tempStrB.length() > 0 && tempStrB.lastIndexOf(",") == tempStrB.length() - 1) {
                tempStrB = tempStrB.deleteCharAt(tempStrB.length() - 1);
            }
        }
        return tempStrB.toString();
    }

    public double getMaxX() {
        return this._MaxX;
    }

    public double getMaxY() {
        return this._MaxY;
    }

    public double getMinX() {
        return this._MinX;
    }

    public double getMinY() {
        return this._MinY;
    }

    public void setMaxX(double value) {
        this._MaxX = value;
    }

    public void setMaxY(double value) {
        this._MaxY = value;
    }

    public void setMinX(double value) {
        this._MinX = value;
    }

    public void setMinY(double value) {
        this._MinY = value;
    }

    public Object getTag() {
        return this._TAGValue;
    }

    public void setTag(Object tag) {
        this._TAGValue = tag;
    }

    public void SetDataSourceName(String dataSourceName) {
        this._DataSourceName = dataSourceName;
    }

    public String GetDataSourceName() {
        return this._DataSourceName;
    }

    public void UpdateFieldsVisible(String visibleFieldNames) {
        List list = Arrays.asList(visibleFieldNames.split(","));
        for (LayerField tempField : this._FieldList) {
            if (list.contains(tempField.GetDataFieldName())) {
                tempField.SetFieldVisible(true);
            } else {
                tempField.SetFieldVisible(false);
            }
        }
    }

    public LayerField getLayerFieldByFieldName(String fieldName) {
        for (LayerField result : this._FieldList) {
            if (result.GetFieldName().equals(fieldName)) {
                return result;
            }
        }
        return null;
    }

    public String getLabelSplitChar() {
        return this._LabelSplitChar;
    }

    public void SetLabelSplitChar(String splitChar) {
        this._LabelSplitChar = splitChar;
    }

    public boolean AddField(LayerField newLayerField) {
        if (this._fieldsNameArray.containsKey(newLayerField.GetFieldName())) {
            return false;
        }
        this._FieldList.add(newLayerField);
        this._fieldsNameArray.put(newLayerField.GetFieldName(), newLayerField.GetDataFieldName());
        this._fieldsNameList.add(newLayerField.GetFieldName());
        return true;
    }

    public String GetNewFID() {
        int tmpI = 1;
        while (this._fieldsNameArray.containsValue("F" + String.valueOf(tmpI))) {
            tmpI++;
        }
        return "F" + String.valueOf(tmpI);
    }

    public int getFiledMaxIndex() {
        int result = 0;
        if (this._FieldList != null && this._FieldList.size() > 0) {
            for (LayerField tmpLayerField : this._FieldList) {
                if (result < tmpLayerField.getFieldIndex()) {
                    result = tmpLayerField.getFieldIndex();
                }
            }
        }
        return result;
    }

    public int[] GetFieldIndexsArray() {
        try {
            List<LayerField> tmpFields = GetFieldList();
            List<Integer> tmpList = new ArrayList<>();
            int[] result = new int[tmpFields.size()];
            int tmpTid = -1;
            for (LayerField tmpLayerField : tmpFields) {
                tmpTid++;
                tmpList.add(Integer.valueOf(tmpLayerField.getFieldIndex()));
                result[tmpTid] = tmpTid;
            }
            Integer[] tmpInts = new Integer[tmpList.size()];
            tmpList.toArray(tmpInts);
            for (int i = 0; i < tmpInts.length - 1; i++) {
                for (int j = 0; j < (tmpInts.length - 1) - i; j++) {
                    if (tmpInts[j].intValue() > tmpInts[j + 1].intValue()) {
                        int t = tmpInts[j].intValue();
                        tmpInts[j] = tmpInts[j + 1];
                        tmpInts[j + 1] = Integer.valueOf(t);
                        int t2 = result[j];
                        result[j] = result[j + 1];
                        result[j + 1] = t2;
                    }
                }
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public int getDataFieldIndexByFieldName(String fieldName) {
        return this._fieldsNameList.indexOf(fieldName);
    }
}
