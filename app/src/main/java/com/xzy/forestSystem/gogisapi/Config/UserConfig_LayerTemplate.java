package  com.xzy.forestSystem.gogisapi.Config;

import android.util.Log;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class UserConfig_LayerTemplate {
    private SQLiteDBHelper m_SQLiteDatabase = null;

    public static List<FeatureLayer> JSONObjectToLayerList(String paramString) {
        ArrayList localArrayList = new ArrayList();
        try {
            JSONArray jsonArray = ((JSONObject) new JSONTokener(paramString).nextValue()).getJSONArray("AllLayer");
            if (jsonArray.length() > 0) {
                int count = jsonArray.length();
                for (int i = 0; i < count; i++) {
                    FeatureLayer tempLayer = new FeatureLayer();
                    tempLayer.SetLayerIndex(i);
                    try {
                        JSONObject json = jsonArray.getJSONObject(i);
                        String tmpLyrID = "";
                        if (json.has("LayerId")) {
                            tmpLyrID = json.getString("LayerId");
                        }
                        if (tmpLyrID == null || tmpLyrID.trim().length() == 0) {
                            tmpLyrID = /*NDEFRecord.TEXT_WELL_KNOWN_TYPE +*/ UUID.randomUUID().toString().replace("-", "").toUpperCase();
                        }
                        tempLayer.SetLayerID(tmpLyrID);
                        if (json.has("LabelScaleMin")) {
                            tempLayer.SetLabelScaleMin(Double.parseDouble(json.getString("LabelScaleMin")));
                        }
                        if (json.has("LabelScaleMax")) {
                            tempLayer.SetLabelScaleMax(Double.parseDouble(json.getString("LabelScaleMax")));
                        }
                        if (json.has("VisibleScaleMax")) {
                            tempLayer.SetMaxScale(Double.parseDouble(json.getString("VisibleScaleMax")));
                        }
                        if (json.has("VisibleScaleMin")) {
                            tempLayer.SetMinScale(Double.parseDouble(json.getString("VisibleScaleMin")));
                        }
                        if (json.has("Visible")) {
                            tempLayer.SetVisible(json.getBoolean("Visible"));
                        }
                        tempLayer.SetLayerTypeName(json.getString("Type"));
                        if (json.has("Snapable")) {
                            tempLayer.SetSnapable(json.getBoolean("Snapable"));
                        }
                        if (json.has("LabelFont")) {
                            tempLayer.SetLabelFont(json.getString("LabelFont"));
                        }
                        if (json.has("Selectable")) {
                            tempLayer.SetSelectable(json.getBoolean("Selectable"));
                        }
                        if (json.has("LabelField")) {
                            tempLayer.SetLabelDataField(json.getString("LabelField"));
                        }
                        if (json.has("Editable")) {
                            tempLayer.SetEditable(json.getBoolean("Editable"));
                        }
                        tempLayer.SetLayerName(json.getString("Name"));
                        if (json.has("RenderType")) {
                            tempLayer.SetRenderType(Integer.parseInt(json.getString("RenderType")));
                        }
                        if (json.has("SimpleRender")) {
                            tempLayer.SetDefaultSymbol(json.getString("SimpleRender"));
                        }
                        if (json.has("UniqueDefaultSymbol")) {
                            tempLayer.SetSymbolName(json.getString("UniqueDefaultSymbol"));
                        }
                        if (json.has("IfLabel")) {
                            tempLayer.SetIfLabel(json.getBoolean("IfLabel"));
                        }
                        if (json.has("Transparent")) {
                            tempLayer.SetTransparent(Integer.parseInt(json.getString("Transparent")));
                        }
                        if (json.has("FieldList")) {
                            try {
                                JSONObject json01 = json.getJSONObject("FieldList");
                                if (json01.has("Data")) {
                                    JSONArray jsonArray02 = json01.getJSONArray("Data");
                                    if (jsonArray02.length() > 0) {
                                        List<LayerField> tempFieldsList = new ArrayList<>();
                                        int count2 = jsonArray02.length();
                                        for (int j = 0; j < count2; j++) {
                                            LayerField tempLayerField = new LayerField();
                                            if (tempLayerField.ReadJsonData(jsonArray02.getJSONObject(j))) {
                                                tempFieldsList.add(tempLayerField);
                                            }
                                        }
                                        if (tempFieldsList.size() > 0) {
                                            tempLayer.SetFieldList(tempFieldsList);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                List tmpList = tempLayer.GetFieldList();
                                if (tmpList.size() == 0) {
                                    LayerField localv1_LayerField1 = new LayerField();
                                    localv1_LayerField1.SetFieldName("名称");
                                    localv1_LayerField1.SetDataFieldName("F1");
                                    localv1_LayerField1.SetFieldTypeName("字符串");
                                    localv1_LayerField1.SetFieldSize(254);
                                    tmpList.add(localv1_LayerField1);
                                    LayerField localv1_LayerField2 = new LayerField();
                                    localv1_LayerField2.SetFieldName("备注");
                                    localv1_LayerField2.SetDataFieldName("F2");
                                    localv1_LayerField2.SetFieldTypeName("字符串");
                                    localv1_LayerField2.SetFieldSize(254);
                                    tmpList.add(localv1_LayerField2);
                                }
                            }
                        }
                    } catch (JSONException localJSONException) {
                        Log.v("错误", "JSONObjectToLayerList:" + localJSONException.getMessage());
                    }
                    localArrayList.add(tempLayer);
                }
            }
        } catch (Exception localJSONException2) {
            Log.v("错误", "JSONObjectToLayerList:" + localJSONException2.getMessage());
        }
        return localArrayList;
    }

    public static JSONObject LayerListToJSONObject(List<FeatureLayer> paramList) {
        JSONObject result = new JSONObject();
        JSONArray localObject = new JSONArray();
        try {
            for (FeatureLayer tempLayer : paramList) {
                JSONObject localJSONObject2 = new JSONObject();
                localJSONObject2.put("LayerId", tempLayer.GetLayerID());
                localJSONObject2.put("Name", tempLayer.GetLayerName());
                localJSONObject2.put("LabelScaleMin", tempLayer.GetLabelScaleMin());
                localJSONObject2.put("LabelScaleMax", tempLayer.GetLabelScaleMax());
                localJSONObject2.put("VisibleScaleMin", tempLayer.GetMinScale());
                localJSONObject2.put("VisibleScaleMax", tempLayer.GetMaxScale());
                localJSONObject2.put("Visible", tempLayer.GetVisible());
                localJSONObject2.put("Type", tempLayer.GetLayerTypeName());
                localJSONObject2.put("FieldList", tempLayer.GetFieldListJSONArray());
                localJSONObject2.put("Snapable", tempLayer.GetSnapable());
                localJSONObject2.put("LabelFont", tempLayer.GetLabelFont());
                localJSONObject2.put("LabelField", tempLayer.GetLabelDataField());
                localJSONObject2.put("Selectable", tempLayer.GetSelectable());
                localJSONObject2.put("LabelField", tempLayer.GetLabelDataField());
                localJSONObject2.put("Editable", tempLayer.GetEditable());
                localJSONObject2.put("RenderType", tempLayer.GetRenderType());
                localJSONObject2.put("SimpleRender", tempLayer.GetDefaultSymbol());
                localJSONObject2.put("UniqueDefaultSymbol", tempLayer.GetDefaultSymbol());
                localJSONObject2.put("IfLabel", tempLayer.GetIfLabel());
                localJSONObject2.put("Transparent", tempLayer.GetTransparet());
                localObject.put(localJSONObject2);
            }
        } catch (Exception e) {
        }
        try {
            result.put("AllLayer", localObject);
        } catch (Exception e2) {
        }
        return result;
    }

    public boolean DeleteTemplateByName(String paramString) {
        return this.m_SQLiteDatabase.ExecuteSQL("delete from T_LayerTemplate where name='" + paramString + "'");
    }

    public List<FeatureLayer> ReadLayerTemplate(String paramString) {
        SQLiteReader localSQLiteDataReader = this.m_SQLiteDatabase.Query("select * from T_LayerTemplate where name='" + paramString + "'");
        if (localSQLiteDataReader == null) {
            return null;
        }
        String str2 = null;
        if (localSQLiteDataReader.Read()) {
            str2 = new String(localSQLiteDataReader.GetBlob("layerlist"));
        }
        localSQLiteDataReader.Close();
        if (str2 != null) {
            return JSONObjectToLayerList(str2);
        }
        return null;
    }

    public List<String> ReadTemplateList(String paramString) {
        String str1 = "1=1";
        if (paramString.equals("系统")) {
            str1 = "name='系统默认图层模板'";
        } else if (paramString.equals("用户")) {
            str1 = "name<>'系统默认图层模板'";
        }
        ArrayList localArrayList = new ArrayList();
        SQLiteReader localSQLiteDataReader = this.m_SQLiteDatabase.Query(String.format("select name,createtime from T_LayerTemplate where %1$s order by id desc", str1));
        if (localSQLiteDataReader != null) {
            while (localSQLiteDataReader.Read()) {
                localArrayList.add(String.valueOf(localSQLiteDataReader.GetString("name")) + "【" + localSQLiteDataReader.GetString("createtime") + "】");
            }
            localSQLiteDataReader.Close();
        }
        return localArrayList;
    }

    public String SaveLayerTemplate(HashMap<String, Object> paramHashMap) {
        String str1 = paramHashMap.get("Name").toString();
        String str2 = paramHashMap.get("CreateTime").toString();
        boolean bool1 = Boolean.parseBoolean(paramHashMap.get("OverWrite").toString());
        String str3 = LayerListToJSONObject((List) paramHashMap.get("LayerList")).toString();
        SQLiteReader localSQLiteDataReader = this.m_SQLiteDatabase.Query("select COUNT(*) as count from T_LayerTemplate where name ='" + str1 + "'");
        int i = 0;
        if (localSQLiteDataReader != null) {
            i = 0;
            if (localSQLiteDataReader.Read()) {
                i = Integer.parseInt(localSQLiteDataReader.GetString("count"));
            }
        }
        localSQLiteDataReader.Close();
        if (i > 0) {
            if (!bool1) {
                return "已存在同名模板！";
            }
            if (!this.m_SQLiteDatabase.ExecuteSQL("delete from T_LayerTemplate where name='" + str1 + "'")) {
                return "更新同名模板失败！";
            }
        }
        if (this.m_SQLiteDatabase.ExecuteSQL(String.format("insert into T_LayerTemplate (name,createtime,layerlist) values ('%1$s','%2$s',?)", str1, str2), new Object[]{str3.getBytes()})) {
            return "OK";
        }
        return "新增模板失败！";
    }

    public void SetBindDB(SQLiteDBHelper paramASQLiteDatabase) {
        this.m_SQLiteDatabase = paramASQLiteDatabase;
    }

    public void CreateDefaultLayerTemplate() {
        HashMap tmpHashMap = new HashMap();
        tmpHashMap.put("Name", "系统默认图层模板");
        tmpHashMap.put("CreateTime", Common.GetSystemDate());
        tmpHashMap.put("OverWrite", "true");
        ArrayList localArrayList = new ArrayList();
        String[] arrayOfString = {"采集点", "采集线", "采集面"};
        String[] arrayOfString2 = {"点", "线", "面"};
        int j = arrayOfString.length;
        for (int i = 0; i < j; i++) {
            String tmpLyrname = arrayOfString[i];
            FeatureLayer localv1_Layer = new FeatureLayer();
            localv1_Layer.SetLayerName(tmpLyrname);
            localv1_Layer.SetLayerTypeName(arrayOfString2[i]);
            LayerField localv1_LayerField1 = new LayerField();
            localv1_LayerField1.SetFieldName("名称");
            localv1_LayerField1.SetDataFieldName("F1");
            localv1_LayerField1.SetFieldTypeName("字符串");
            localv1_LayerField1.SetFieldSize(254);
            localv1_Layer.GetFieldList().add(localv1_LayerField1);
            LayerField localv1_LayerField2 = new LayerField();
            localv1_LayerField2.SetFieldName("备注");
            localv1_LayerField2.SetDataFieldName("F2");
            localv1_LayerField2.SetFieldTypeName("字符串");
            localv1_LayerField2.SetFieldSize(254);
            localv1_Layer.GetFieldList().add(localv1_LayerField2);
            localArrayList.add(localv1_Layer);
        }
        tmpHashMap.put("LayerList", localArrayList);
        SaveLayerTemplate(tmpHashMap);
    }
}
