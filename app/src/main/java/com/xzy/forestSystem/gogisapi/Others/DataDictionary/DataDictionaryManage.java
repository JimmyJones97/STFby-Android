package  com.xzy.forestSystem.gogisapi.Others.DataDictionary;

import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Config.SysConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class DataDictionaryManage {
    private SysConfig m_ConfigDB = null;

    public List<String> GetZDNameList(String zdType, String zdSub) {
        ArrayList localArrayList = new ArrayList();
        SQLiteReader localSQLiteDataReader = this.m_ConfigDB.GetSQLiteDatabase().Query("Select distinct ZDName from T_DataDictionary where ZDType='" + zdType + "' and ZDSub='" + zdSub + "'");
        if (localSQLiteDataReader != null) {
            do {
                localArrayList.add(localSQLiteDataReader.GetString("ZDName"));
            } while (localSQLiteDataReader.Read());
            localSQLiteDataReader.Close();
        }
        return localArrayList;
    }

    public List<String> GetZDSubList(String zdType) {
        ArrayList localArrayList = new ArrayList();
        SQLiteReader localSQLiteDataReader = this.m_ConfigDB.GetSQLiteDatabase().Query("Select distinct ZDSub from T_DataDictionary where ZDType='" + zdType + "'");
        if (localSQLiteDataReader != null) {
            do {
                localArrayList.add(localSQLiteDataReader.GetString("ZDSub"));
            } while (localSQLiteDataReader.Read());
            localSQLiteDataReader.Close();
        }
        return localArrayList;
    }

    public List<String> GetZDTypeList() {
        ArrayList localArrayList = new ArrayList();
        SQLiteReader localSQLiteDataReader = this.m_ConfigDB.GetSQLiteDatabase().Query("Select distinct ZDType from T_DataDictionary ");
        if (localSQLiteDataReader != null) {
            do {
                localArrayList.add(localSQLiteDataReader.GetString("ZDType"));
            } while (localSQLiteDataReader.Read());
            localSQLiteDataReader.Close();
        }
        return localArrayList;
    }

    public List<String> GetZDCodeList() {
        ArrayList localArrayList = new ArrayList();
        SQLiteReader localSQLiteDataReader = this.m_ConfigDB.GetSQLiteDatabase().Query("Select ZDBM from T_DataDictionary ");
        if (localSQLiteDataReader != null) {
            while (localSQLiteDataReader.Read()) {
                String tmpZDBM = localSQLiteDataReader.GetString("ZDBM");
                if (!localArrayList.contains(tmpZDBM)) {
                    localArrayList.add(tmpZDBM);
                }
            }
            localSQLiteDataReader.Close();
        }
        return localArrayList;
    }

    public HashMap<String, Object> GetZDValueList(String zdType, String zdSub, String zdName) {
        String str2;
        HashMap tmpHashMap = new HashMap();
        tmpHashMap.put("ZDBM", "");
        tmpHashMap.put("ZDList", null);
        SQLiteReader localSQLiteDataReader = this.m_ConfigDB.GetSQLiteDatabase().Query("Select distinct ZDBM,ZDList from T_DataDictionary where ZDType='" + zdType + "' and ZDSub='" + zdSub + "' and ZDName='" + zdName + "'");
        if (localSQLiteDataReader != null) {
            if (localSQLiteDataReader.Read() && (str2 = localSQLiteDataReader.GetString("ZDList")) != null) {
                tmpHashMap.put("ZDBM", localSQLiteDataReader.GetString("ZDBM"));
                tmpHashMap.put("ZDList", Common.StrArrayToList(str2.split(",")));
            }
            localSQLiteDataReader.Close();
        }
        return tmpHashMap;
    }

    public HashMap<String, String> GetZDItem(String zdbm) {
        HashMap<String, String> result = null;
        try {
            SQLiteReader localSQLiteDataReader = this.m_ConfigDB.GetSQLiteDatabase().Query("Select distinct ZDType,ZDSub,ZDName,ZDList,ZDNameCode,F1,F2,F3,F4,F5 from T_DataDictionary where ZDBM='" + zdbm + "'");
            if (localSQLiteDataReader == null) {
                return null;
            }
            if (localSQLiteDataReader.Read()) {
                HashMap<String, String> result2 = new HashMap<>();
                try {
                    result2.put("ZDBM", zdbm);
                    result2.put("ZDType", localSQLiteDataReader.GetString("ZDType"));
                    result2.put("ZDSub", localSQLiteDataReader.GetString("ZDSub"));
                    result2.put("ZDName", localSQLiteDataReader.GetString("ZDName"));
                    result2.put("ZDList", localSQLiteDataReader.GetString("ZDList"));
                    result2.put("ZDNameCode", localSQLiteDataReader.GetString("ZDNameCode"));
                    result2.put("F1", localSQLiteDataReader.GetString("F1"));
                    result2.put("F2", localSQLiteDataReader.GetString("F2"));
                    result2.put("F3", localSQLiteDataReader.GetString("F3"));
                    result2.put("F4", localSQLiteDataReader.GetString("F4"));
                    result2.put("F5", localSQLiteDataReader.GetString("F5"));
                    result = result2;
                } catch (Exception e) {
                    return result2;
                }
            }
            localSQLiteDataReader.Close();
            return result;
        } catch (Exception e2) {
            return null;
        }
    }

    public List<HashMap<String, String>> GetZDItemsBySub(String subName, boolean isSubCode) {
        String tmpSql;
        if (isSubCode) {
            try {
                tmpSql = String.valueOf("Select distinct ZDType,ZDSub,ZDName,ZDList,ZDNameCode,ZDBM,F1,F2,F3,F4,F5 from T_DataDictionary where ") + "ZDNameCode='" + subName + "'";
            } catch (Exception e) {
                return null;
            }
        } else {
            tmpSql = String.valueOf("Select distinct ZDType,ZDSub,ZDName,ZDList,ZDNameCode,ZDBM,F1,F2,F3,F4,F5 from T_DataDictionary where ") + "ZDName='" + subName + "'";
        }
        SQLiteReader localSQLiteDataReader = this.m_ConfigDB.GetSQLiteDatabase().Query(tmpSql);
        if (localSQLiteDataReader == null) {
            return null;
        }
        List<HashMap<String, String>> result = new ArrayList<>();
        while (localSQLiteDataReader.Read()) {
            try {
                HashMap<String, String> tmpHash = new HashMap<>();
                tmpHash.put("ZDBM", localSQLiteDataReader.GetString("ZDBM"));
                tmpHash.put("ZDType", localSQLiteDataReader.GetString("ZDType"));
                tmpHash.put("ZDSub", localSQLiteDataReader.GetString("ZDSub"));
                tmpHash.put("ZDName", localSQLiteDataReader.GetString("ZDName"));
                tmpHash.put("ZDList", localSQLiteDataReader.GetString("ZDList"));
                tmpHash.put("ZDNameCode", localSQLiteDataReader.GetString("ZDNameCode"));
                tmpHash.put("F1", localSQLiteDataReader.GetString("F1"));
                tmpHash.put("F2", localSQLiteDataReader.GetString("F2"));
                tmpHash.put("F3", localSQLiteDataReader.GetString("F3"));
                tmpHash.put("F4", localSQLiteDataReader.GetString("F4"));
                tmpHash.put("F5", localSQLiteDataReader.GetString("F5"));
                result.add(tmpHash);
            } catch (Exception e2) {
                return result;
            }
        }
        localSQLiteDataReader.Close();
        return result;
    }

    public List<String> GetZDValueList(String ZDBM) {
        String[] tempStrs;
        List<String> result = new ArrayList<>();
        SQLiteReader tempReader = this.m_ConfigDB.GetSQLiteDatabase().Query("Select ZDList from T_DataDictionary where ZDBM='" + ZDBM + "'");
        if (tempReader != null) {
            if (tempReader.Read() && (tempStrs = tempReader.GetString(0).split(",")) != null && tempStrs.length > 0) {
                result.addAll(Arrays.asList(tempStrs));
            }
            tempReader.Close();
        }
        return result;
    }

    public String GetZDValue2(String ZDItemValue) {
        if (ZDItemValue.contains("(")) {
            return ZDItemValue.substring(ZDItemValue.indexOf("(") + 1, ZDItemValue.lastIndexOf(")"));
        }
        return ZDItemValue;
    }

    public String GetZDCode2(String ZDItemValue) {
        if (ZDItemValue.contains("(")) {
            return ZDItemValue.substring(0, ZDItemValue.indexOf("("));
        }
        return ZDItemValue;
    }

    public String GetZDCode(String ZDBM, String ZDValue) {
        String[] tempStrs;
        String result = ZDValue;
        SQLiteReader tempReader = this.m_ConfigDB.GetSQLiteDatabase().Query("Select ZDList from T_DataDictionary where ZDBM='" + ZDBM + "'");
        if (tempReader != null) {
            if (tempReader.Read() && (tempStrs = tempReader.GetString(0).split(",")) != null && tempStrs.length > 0) {
                int i = 0;
                int count = tempStrs.length;
                while (true) {
                    if (i >= count) {
                        break;
                    } else if (!tempStrs[i].equals(ZDValue)) {
                        i++;
                    } else if (ZDValue.contains("(")) {
                        try {
                            result = String.valueOf(Integer.parseInt(ZDValue.substring(0, ZDValue.indexOf("("))));
                        } catch (Exception e) {
                        }
                    }
                }
            }
            tempReader.Close();
        }
        return result;
    }

    public String GetZDValue(String ZDBM, String ZDCode) {
        String result = ZDCode;
        SQLiteReader tempReader = this.m_ConfigDB.GetSQLiteDatabase().Query("Select ZDList from T_DataDictionary where ZDBM='" + ZDBM + "'");
        if (tempReader != null) {
            if (tempReader.Read()) {
                String tempStr = tempReader.GetString(0);
                result = tempStr;
                String[] tempStrs = tempStr.split(",");
                if (tempStrs != null && tempStrs.length > 0) {
                    String tmpZDCode = String.valueOf(ZDCode) + "(";
                    for (String tmpStr01 : tempStrs) {
                        if (tmpStr01.contains(tmpZDCode)) {
                            result = tmpStr01.substring(tmpStr01.indexOf("(") + 1, tmpStr01.lastIndexOf(""));
                        }
                    }
                }
            }
            tempReader.Close();
        }
        return result;
    }

    public void SetBindProject(SysConfig pConfigDB) {
        this.m_ConfigDB = pConfigDB;
    }

    public List<String> GetZDValueNamesList(String ZDBM) {
        List<String> tmpList = GetZDValueList(ZDBM);
        if (tmpList == null || tmpList.size() <= 0) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for (String tmpString : tmpList) {
            int tmpI01 = tmpString.indexOf(40);
            if (tmpI01 >= 0) {
                tmpString = tmpString.substring(tmpI01 + 1);
            }
            int tmpI012 = tmpString.indexOf(41);
            if (tmpI012 >= 0) {
                tmpString = tmpString.substring(0, tmpI012);
            }
            result.add(tmpString);
        }
        return result;
    }
}
