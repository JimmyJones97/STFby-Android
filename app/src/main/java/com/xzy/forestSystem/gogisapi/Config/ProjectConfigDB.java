package  com.xzy.forestSystem.gogisapi.Config;

import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProjectConfigDB {
    private SQLiteDBHelper m_SQLiteDatabase = null;

    public HashMap<String, String> GetUserPara(String paraName) {
        HashMap<String, String> tmpHashMap = null;
        SQLiteReader localSQLiteDataReader = this.m_SQLiteDatabase.Query("select * from T_ProjectUserConfig where name='" + paraName + "'");
        if (localSQLiteDataReader != null && localSQLiteDataReader.Read()) {
            tmpHashMap = new HashMap<>();
            String[] arrayOfString = localSQLiteDataReader.GetFieldNameList();
            for (String str2 : arrayOfString) {
                if (!str2.equals("ID")) {
                    tmpHashMap.put(str2, localSQLiteDataReader.GetString(str2));
                }
            }
            localSQLiteDataReader.Close();
        }
        return tmpHashMap;
    }

    public boolean SaveUserPara(String paramString, HashMap<String, String> paramHashMap) {
        String tempSql;
        try {
            boolean tempTag = false;
            SQLiteReader localSQLiteDataReader = this.m_SQLiteDatabase.Query("Select count(*) as TCount from T_ProjectUserConfig where Name='" + paramString + "'");
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
                localArrayList2.add(localEntry.getValue().toString());
                localArrayList3.add(String.valueOf((String) localArrayList1.get(localArrayList1.size() - 1)) + "='" + ((String) localArrayList2.get(localArrayList2.size() - 1)) + "'");
            }
            if (localArrayList1.size() <= 0) {
                return false;
            }
            if (tempTag) {
                tempSql = String.format("update T_ProjectUserConfig set %2$s where Name='%1$s'", paramString, Common.CombineStrings(",", localArrayList3));
            } else {
                tempSql = String.format("insert into T_ProjectUserConfig (Name,%2$s) values ('%1$s','%3$s')", paramString, Common.CombineStrings(",", localArrayList1), Common.CombineStrings("','", localArrayList2));
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
