package  com.xzy.forestSystem.gogisapi.Carto.Layer;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Geodatabase.EFieldType;
import java.util.List;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public class LayerField {
    public EFieldAutoValueType AutoValueType = EFieldAutoValueType.NONE;
    private boolean FieldVisible = true;
    private String _DataFieldName = "";
    private int _FieldDecimal = 0;
    private String _FieldEnumCode = "";
    private boolean _FieldEnumEdit = false;
    private String _FieldName = "";
    private int _FieldSize = 255;
    private EFieldType _FieldType = EFieldType.STRING;
    private String _FieldTypeName = "";
    private boolean _IsEnumOfCode = false;
    private String _LayerID = (/*NDEFRecord.TEXT_WELL_KNOWN_TYPE +*/ UUID.randomUUID().toString().replace("-", "").toUpperCase());
    private String _Remark = "";
    private int m_OrderID = 0;
    private Object tagValue = null;

    public String GetDataFieldName() {
        return this._DataFieldName;
    }

    public int GetFieldDecimal() {
        return this._FieldDecimal;
    }

    public String GetFieldEnumCode() {
        return this._FieldEnumCode;
    }

    public boolean GetFieldEnumEdit() {
        return this._FieldEnumEdit;
    }

    public boolean GetIsEnumOfCode() {
        return this._IsEnumOfCode;
    }

    public List<String> GetFieldEnumList() {
        return PubVar._PubCommand.m_ConfigDB.GetDataDictionaryManage().GetZDValueList(this._FieldEnumCode);
    }

    public List<String> GetFieldEnumNamesList() {
        return PubVar._PubCommand.m_ConfigDB.GetDataDictionaryManage().GetZDValueNamesList(this._FieldEnumCode);
    }

    public String GetFieldID() {
        return this._LayerID;
    }

    public String GetFieldName() {
        return this._FieldName;
    }

    public int GetFieldSize() {
        return this._FieldSize;
    }

    public EFieldType GetFieldType() {
        return this._FieldType;
    }

    public String GetFieldTypeName() {
        return this._FieldTypeName;
    }

    public boolean GetFieldVisible() {
        return this.FieldVisible;
    }

    public void SetDataFieldName(String paramString) {
        this._DataFieldName = paramString;
    }

    public void SetFieldDecimal(int paramInt) {
        this._FieldDecimal = paramInt;
    }

    public void SetFieldEnumCode(String paramString) {
        this._FieldEnumCode = paramString;
    }

    public void SetFieldEnumEdit(boolean paramBoolean) {
        this._FieldEnumEdit = paramBoolean;
    }

    public void SetIsEnumOfCode(boolean isCode) {
        this._IsEnumOfCode = isCode;
    }

    public void SetFieldName(String paramString) {
        this._FieldName = paramString;
    }

    public void SetFieldSize(int paramInt) {
        this._FieldSize = paramInt;
    }

    public void SetFieldVisible(boolean value) {
        this.FieldVisible = value;
    }

    public void SetFieldTypeName(String paramString) {
        this._FieldTypeName = paramString;
        if (paramString.equals("字符串")) {
            this._FieldType = EFieldType.STRING;
        } else if (paramString.equals("整型")) {
            this._FieldType = EFieldType.INTEGER;
        } else if (paramString.equals("浮点型")) {
            this._FieldType = EFieldType.DECIMAL;
        } else if (paramString.equals("布尔型")) {
            this._FieldType = EFieldType.BOOLEAN;
        } else if (paramString.equals("日期型")) {
            this._FieldType = EFieldType.DATETIME;
        }
    }

    public boolean ReadJsonData(JSONObject json) {
        try {
            String tempStr = json.getString("FieldTypeName");
            SetFieldName(json.getString("FieldName"));
            SetFieldTypeName(tempStr);
            SetFieldEnumEdit(json.getBoolean("FieldEnumEdit"));
            SetFieldDecimal(json.getInt("FieldDecimal"));
            SetFieldSize(json.getInt("FieldSize"));
            SetFieldEnumCode(json.getString("FieldEnumCode"));
            SetDataFieldName(json.getString("DataFieldName"));
            if (json.has("FieldVisible")) {
                SetFieldVisible(json.getBoolean("FieldVisible"));
            }
            if (json.has("FieldIsEnumOfCode")) {
                SetIsEnumOfCode(json.getBoolean("FieldIsEnumOfCode"));
            }
            if (json.has("FieldAutoValueType")) {
                this.AutoValueType = EFieldAutoValueType.values()[json.getInt("FieldAutoValueType")];
            }
            if (json.has("Remark")) {
                this._Remark = json.getString("Remark");
            }
            if (json.has("FieldIndex")) {
                try {
                    this.m_OrderID = Integer.parseInt(json.getString("FieldIndex"));
                } catch (Exception e) {
                }
            }
            return true;
        } catch (JSONException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public JSONObject GetFieldJSONObject() {
        JSONObject tempJson = new JSONObject();
        try {
            tempJson.put("FieldTypeName", GetFieldTypeName());
            tempJson.put("FieldVisible", GetFieldVisible());
            tempJson.put("FieldIndex", this.m_OrderID);
            tempJson.put("FieldEnumEdit", GetFieldEnumEdit());
            tempJson.put("FieldDecimal", GetFieldDecimal());
            tempJson.put("FieldSize", GetFieldSize());
            tempJson.put("FieldEnumCode", GetFieldEnumCode());
            tempJson.put("DataFieldName", GetDataFieldName());
            tempJson.put("FieldName", GetFieldName());
            tempJson.put("FieldIsEnumOfCode", GetIsEnumOfCode());
            tempJson.put("FieldAutoValueType", this.AutoValueType.ordinal());
            tempJson.put("Remark", this._Remark);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tempJson;
    }

    public void SetTag(Object tag) {
        this.tagValue = tag;
    }

    public Object GetTag() {
        return this.tagValue;
    }

    public void setRemark(String remark) {
        this._Remark = remark;
    }

    public String getRemark() {
        return this._Remark;
    }

    public int getFieldIndex() {
        return this.m_OrderID;
    }

    public void setFieldIndex(int fieldIndex) {
        this.m_OrderID = fieldIndex;
    }
}
