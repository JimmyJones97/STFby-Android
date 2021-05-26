package  com.xzy.forestSystem.gogisapi.Config;

import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class UserConfig_CoordinateSystem {
    private SQLiteDBHelper m_SQLiteDatabase = null;

    public List<HashMap<String, Object>> GetMyCoordinateSystemList() {
        ArrayList localArrayList = new ArrayList();
        try {
            SQLiteReader localSQLiteDataReader = this.m_SQLiteDatabase.Query("select * from T_MyCoordinateSystem order by ID DESC");
            if (localSQLiteDataReader != null) {
                while (localSQLiteDataReader.Read()) {
                    HashMap tmpHashMap = new HashMap();
                    tmpHashMap.put("D1", "false");
                    tmpHashMap.put("D2", localSQLiteDataReader.GetString("Name"));
                    try {
                        JSONObject localJSONObject = (JSONObject) new JSONTokener(new String(localSQLiteDataReader.GetBlob("Para"))).nextValue();
                        tmpHashMap.put("D3", localJSONObject.getString("CoorSystem"));
                        tmpHashMap.put("D4", localJSONObject.getString("CenterJX"));
                        tmpHashMap.put("D5", localJSONObject.getString("TransMethod"));
                        tmpHashMap.put("CoorSystem", tmpHashMap.get("D3"));
                        tmpHashMap.put("CenterJX", tmpHashMap.get("D4"));
                        tmpHashMap.put("TransMethod", tmpHashMap.get("D5"));
                        tmpHashMap.put("P1", localJSONObject.getString("P1"));
                        tmpHashMap.put("P2", localJSONObject.getString("P2"));
                        tmpHashMap.put("P3", localJSONObject.getString("P3"));
                        tmpHashMap.put("P4", localJSONObject.getString("P4"));
                        tmpHashMap.put("P5", localJSONObject.getString("P5"));
                        tmpHashMap.put("P6", localJSONObject.getString("P6"));
                        tmpHashMap.put("P7", localJSONObject.getString("P7"));
                    } catch (Exception e) {
                    }
                    localArrayList.add(tmpHashMap);
                }
                localSQLiteDataReader.Close();
            }
        } catch (Exception e2) {
        }
        return localArrayList;
    }

    public boolean DeleteMyCoordinateSystem(String coordSystemName) {
        try {
            return this.m_SQLiteDatabase.ExecuteSQL("Delete From T_MyCoordinateSystem Where Name='" + coordSystemName + "'");
        } catch (Exception e) {
            return false;
        }
    }

    public String SaveMyCoordinateSystem(String paramString, HashMap<String, Object> paramHashMap, boolean paramBoolean) {
        try {
            JSONObject localJSONObject = new JSONObject();
            localJSONObject.put("CoorSystem", paramHashMap.get("CoorSystem"));
            localJSONObject.put("CenterJX", paramHashMap.get("CenterJX"));
            localJSONObject.put("TransMethod", paramHashMap.get("TransMethod"));
            localJSONObject.put("P1", paramHashMap.get("P1"));
            localJSONObject.put("P2", paramHashMap.get("P2"));
            localJSONObject.put("P3", paramHashMap.get("P3"));
            localJSONObject.put("P4", paramHashMap.get("P4"));
            localJSONObject.put("P5", paramHashMap.get("P5"));
            localJSONObject.put("P6", paramHashMap.get("P6"));
            localJSONObject.put("P7", paramHashMap.get("P7"));
            SQLiteReader localSQLiteDataReader = this.m_SQLiteDatabase.Query("select COUNT(*) as count from T_MyCoordinateSystem where name ='" + paramString + "'");
            int i = 0;
            if (localSQLiteDataReader != null) {
                if (localSQLiteDataReader.Read()) {
                    i = Integer.parseInt(localSQLiteDataReader.GetString("count"));
                }
                localSQLiteDataReader.Close();
            }
            if (i > 0) {
                if (!paramBoolean) {
                    return "我的坐标系名称重复！";
                }
                if (!this.m_SQLiteDatabase.ExecuteSQL("delete from T_MyCoordinateSystem where name='" + paramString + "'")) {
                    return "更新我的坐标系失败！";
                }
            }
            if (this.m_SQLiteDatabase.ExecuteSQL(String.format("insert into T_MyCoordinateSystem (Name,CreateTime,Para) values ('%1$s','%2$s',?)", paramString, Common.GetSystemDate()), new Object[]{localJSONObject.toString().getBytes()})) {
                return "OK";
            }
            return "新增我的坐标系失败！";
        } catch (JSONException localJSONException) {
            throw new RuntimeException(localJSONException);
        }
    }

    public void SetBindDB(SQLiteDBHelper paramASQLiteDatabase) {
        this.m_SQLiteDatabase = paramASQLiteDatabase;
    }
}
