package  com.xzy.forestSystem.gogisapi.Config;

import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Display.SymbolManage;
import  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDictionaryManage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SysConfig {
    private static List<String> m_CoorSystemsList = new ArrayList();
    private static List<String> m_TranslateList = new ArrayList();
    private DataDictionaryManage m_DataDictionary = null;
    private SQLiteDBHelper m_SQLiteDatabase = null;
    private HashMap<String, String> m_SpheroidHash = null;
    private List<String> m_SpheroidName = null;
    private SymbolManage m_SymbolManage = null;

    private void OpenDatabase() {
        String str = String.valueOf(PubVar.m_SystemPath) + "/sysfile/config.dbx";
        if (Common.CheckExistFile(str)) {
            this.m_SQLiteDatabase = new SQLiteDBHelper();
            this.m_SQLiteDatabase.setDatabaseName(str);
            if (!GetSQLiteDatabase().CheckColumnExist("ConfigUpdate", "UpdateInfo")) {
                GetSQLiteDatabase().ExecuteSQL("Drop Table ConfigUpdate");
            }
            try {
                GetSQLiteDatabase().ExecuteSQL("Create Table If Not Exists ConfigUpdate (ID integer primary key AutoIncrement, UpdateInfo varchar(255) Default '', UpdateTime varchar(50) Default '',Remark Text Default '')");
            } catch (Exception ex) {
                Common.Log("错误", ex.getMessage());
            }
            try {
                GetSQLiteDatabase().ExecuteSQL("Create Table If Not Exists XuJiTable (ID integer primary key AutoIncrement, Name varchar(255) Default '', ZoneName varchar(50) Default '',tableid varchar(50) Default '',TreeRadiusStr TEXT Default '',TreeHeightsStr TEXT Default '',TreeVolumnsStr TEXT Default '', Remark Text Default '')");
            } catch (Exception ex2) {
                Common.Log("错误", ex2.getMessage());
            }
        }
    }

    public DataDictionaryManage GetDataDictionaryManage() {
        if (this.m_DataDictionary == null) {
            this.m_DataDictionary = new DataDictionaryManage();
        }
        GetSQLiteDatabase();
        this.m_DataDictionary.SetBindProject(this);
        return this.m_DataDictionary;
    }

    public SQLiteDBHelper GetSQLiteDatabase() {
        if (this.m_SQLiteDatabase == null) {
            OpenDatabase();
        }
        return this.m_SQLiteDatabase;
    }

    public SymbolManage GetSymbolManage() {
        if (this.m_SymbolManage == null) {
            this.m_SymbolManage = new SymbolManage();
            GetSQLiteDatabase();
            this.m_SymbolManage.SetBindProject(this);
        }
        return this.m_SymbolManage;
    }

    public List<String> getTranslateList() {
        if (m_TranslateList.size() == 0) {
            m_TranslateList.add("无");
            m_TranslateList.add("平移转换");
            m_TranslateList.add("三参转换");
            m_TranslateList.add("四参转换");
            m_TranslateList.add("七参转换");
        }
        return m_TranslateList;
    }

    public List<String> getCoordSystemsList() {
        if (m_CoorSystemsList.size() == 0) {
            try {
                if (this.m_SQLiteDatabase == null) {
                    OpenDatabase();
                }
                SQLiteReader localSQLiteDataReader = this.m_SQLiteDatabase.Query("select * from T_CoorSystem order by code");
                if (localSQLiteDataReader != null) {
                    while (localSQLiteDataReader.Read()) {
                        m_CoorSystemsList.add(localSQLiteDataReader.GetString("Name"));
                    }
                    localSQLiteDataReader.Close();
                }
            } catch (Exception e) {
            }
        }
        return m_CoorSystemsList;
    }

    public HashMap<String, String> getSpheroidList() {
        if (this.m_SpheroidHash == null || this.m_SpheroidName == null) {
            this.m_SpheroidHash = new HashMap<>();
            this.m_SpheroidName = new ArrayList();
            try {
                if (this.m_SQLiteDatabase == null) {
                    OpenDatabase();
                }
                SQLiteReader localSQLiteDataReader = this.m_SQLiteDatabase.Query("select SpheroidName,sm_a,f from T_Spheroid Order By ID");
                if (localSQLiteDataReader != null) {
                    while (localSQLiteDataReader.Read()) {
                        String tmpname = localSQLiteDataReader.GetString(0);
                        this.m_SpheroidName.add(tmpname);
                        String tmpSmA = localSQLiteDataReader.GetString(1);
                        this.m_SpheroidHash.put(tmpname, String.valueOf(tmpSmA) + "," + localSQLiteDataReader.GetString(2));
                    }
                    localSQLiteDataReader.Close();
                }
            } catch (Exception e) {
            }
        }
        return this.m_SpheroidHash;
    }

    public List<String> getSpheroidNameList() {
        if (this.m_SpheroidHash == null || this.m_SpheroidName == null) {
            this.m_SpheroidHash = new HashMap<>();
            this.m_SpheroidName = new ArrayList();
            try {
                if (this.m_SQLiteDatabase == null) {
                    OpenDatabase();
                }
                SQLiteReader localSQLiteDataReader = this.m_SQLiteDatabase.Query("select SpheroidName,sm_a,f from T_Spheroid Order By ID");
                if (localSQLiteDataReader != null) {
                    while (localSQLiteDataReader.Read()) {
                        String tmpname = localSQLiteDataReader.GetString(0);
                        this.m_SpheroidName.add(tmpname);
                        String tmpSmA = localSQLiteDataReader.GetString(1);
                        this.m_SpheroidHash.put(tmpname, String.valueOf(tmpSmA) + "," + localSQLiteDataReader.GetString(2));
                    }
                    localSQLiteDataReader.Close();
                }
            } catch (Exception e) {
            }
        }
        return this.m_SpheroidName;
    }
}
