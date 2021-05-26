package  com.xzy.forestSystem.gogisapi.Config;

import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserParam {
    private SQLiteDBHelper m_SQLiteDatabase = null;

    public HashMap<String, String> GetUserPara(String paramString) {
        try {
            SQLiteReader localSQLiteDataReader = this.m_SQLiteDatabase.Query("select F1,F2,F3,F4,F5,F6,F7 from T_UserParam where F1='" + paramString + "'");
            if (localSQLiteDataReader == null || !localSQLiteDataReader.Read()) {
                return null;
            }
            HashMap tmpHashMap = new HashMap();
            try {
                String[] arrayOfString = localSQLiteDataReader.GetFieldNameList();
                for (String str2 : arrayOfString) {
                    if (!str2.equals("ID")) {
                        tmpHashMap.put(str2, localSQLiteDataReader.GetString(str2));
                    }
                }
                localSQLiteDataReader.Close();
                return tmpHashMap;
            } catch (Exception e) {
                return tmpHashMap;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    public boolean SaveUserPara(String paraName, Object value) {
        try {
            HashMap<String, String> tempHash = new HashMap<>();
            tempHash.put("F2", String.valueOf(value));
            return SaveUserPara(paraName, tempHash);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean SaveUserPara(String paramString, HashMap<String, String> paramHashMap) {
        String tempSql;
        try {
            boolean tempTag = false;
            SQLiteReader localSQLiteDataReader = this.m_SQLiteDatabase.Query("Select count(*) as TCount from T_UserParam where F1='" + paramString + "'");
            if (localSQLiteDataReader != null && localSQLiteDataReader.Read()) {
                if (Integer.parseInt(localSQLiteDataReader.GetString("TCount")) == 1) {
                    tempTag = true;
                }
                localSQLiteDataReader.Close();
            }
            ArrayList localArrayList1 = new ArrayList();
            ArrayList localArrayList2 = new ArrayList();
            ArrayList localArrayList3 = new ArrayList();
            for (Map.Entry<String, String> localEntry : paramHashMap.entrySet()) {
                localArrayList1.add(localEntry.getKey().toString());
                if (localEntry.getValue() != null) {
                    localArrayList2.add(localEntry.getValue().toString());
                } else {
                    localArrayList2.add("");
                }
                localArrayList3.add(String.valueOf((String) localArrayList1.get(localArrayList1.size() - 1)) + "='" + ((String) localArrayList2.get(localArrayList2.size() - 1)) + "'");
            }
            if (localArrayList1.size() <= 0) {
                return false;
            }
            if (tempTag) {
                tempSql = String.format("update T_UserParam set %2$s where F1='%1$s'", paramString, Common.CombineStrings(",", localArrayList3));
            } else {
                tempSql = String.format("insert into T_UserParam (F1,%2$s) values ('%1$s','%3$s')", paramString, Common.CombineStrings(",", localArrayList1), Common.CombineStrings("','", localArrayList2));
            }
            return this.m_SQLiteDatabase.ExecuteSQL(tempSql);
        } catch (Exception e) {
            return false;
        }
    }

    public void SetBindDB(SQLiteDBHelper paramASQLiteDatabase) {
        this.m_SQLiteDatabase = paramASQLiteDatabase;
    }
}
