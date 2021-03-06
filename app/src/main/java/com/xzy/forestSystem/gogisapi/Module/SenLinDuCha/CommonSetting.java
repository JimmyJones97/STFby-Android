package  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha;

import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonSetting {
    static HashMap<String, String> m_DuChaLayerMustFieldsName = new HashMap<>();
    static List<String> m_DuChaLayerMustFieldsNameList = new ArrayList();
    static HashMap<String, Double[]> m_GenJingCaiJiTable = new HashMap<>();
    static List<String> m_LayersList = new ArrayList();
    static SQLiteDBHelper m_SQLiteDBHelper = null;
    static double m_YangDiArea = 0.0452d;

    public static SQLiteDBHelper GetSenLinDuChaSQLiteDBHelper() {
        String tmpPath = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/SenLinDuCha.dbx";
        if (m_SQLiteDBHelper == null || !m_SQLiteDBHelper.GetDatabasePath().equals(tmpPath)) {
            try {
                if (!new File(tmpPath).exists()) {
                    Common.CopyFile(String.valueOf(Common.GetAPPPath()) + "/SysFile/Template.dbx", tmpPath);
                }
                if (new File(tmpPath).exists()) {
                    m_SQLiteDBHelper = new SQLiteDBHelper(tmpPath);
                }
                if (m_SQLiteDBHelper != null) {
                    m_SQLiteDBHelper.ExecuteSQL("Create Table If Not Exists T_SysParams (ID integer primary key AutoIncrement,ParaName varchar(1024) NOT NULL Unique,ParaValue TEXT Default '')");
                    m_SQLiteDBHelper.ExecuteSQL("Create Table If Not Exists T_CheckPartsInfo (ID integer primary key AutoIncrement,PartName varchar(1024) Default '',Sheng varchar(50) Default '',Xian varchar(50) Default '',Xiang varchar(50) Default '',Cun varchar(50) Default '',PartIndex varchar(50) Default '',LayerID varchar(50),SYSID varchar(10),Remark TEXT )");
                    m_SQLiteDBHelper.ExecuteSQL("Create Table If Not Exists T_YangDiInfo (ID integer primary key AutoIncrement,YangDiName varchar(1024) NOT NULL Unique,YangDiIndex varchar(10) Default '', Sheng varchar(50) Default '',Xian varchar(50) Default '',Xiang varchar(50) Default '',Cun varchar(50) Default '',PartIndex varchar(50) Default '',LayerID varchar(50),SYSID varchar(10), X Double Default 0, Y Double Default 0,PhotoInfo varchar(4096) Default '',YangDiDataID varchar(50) Default '', Remark TEXT )");
                    m_SQLiteDBHelper.ExecuteSQL("Create Table If Not Exists T_YangDiData (ID integer primary key AutoIncrement,YangDiName varchar(1024) Default '',GenJing Integer Default 0,Shan Integer Default 0,Ma Integer Default 0 ,Kuo Integer Default 0, Remark TEXT )");
                }
            } catch (Exception e) {
                Common.Log("??????", e.getLocalizedMessage());
            }
        }
        return m_SQLiteDBHelper;
    }

    public static String GetParaValue(String paraName) {
        String result = "";
        try {
            if (GetSenLinDuChaSQLiteDBHelper() != null) {
                SQLiteReader tmpSQLiteReader = m_SQLiteDBHelper.Query("Select ParaValue From T_SysParams Where ParaName='" + paraName + "'");
                if (tmpSQLiteReader.Read()) {
                    result = tmpSQLiteReader.GetString(0).trim();
                }
                tmpSQLiteReader.Close();
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static boolean SaveParaValue(String paraName, String paraValue) {
        try {
            SQLiteDBHelper tmpSqLiteDBHelper = GetSenLinDuChaSQLiteDBHelper();
            if (tmpSqLiteDBHelper != null) {
                return tmpSqLiteDBHelper.ExecuteSQL("Replace Into T_SysParams (ParaName,ParaValue) Values ('" + paraName + "','" + paraValue + "')");
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean InitialSetting() {
        try {
            m_LayersList = new ArrayList();
            String[] tmpStrs = GetParaValue("????????????").split(";");
            if (tmpStrs != null && tmpStrs.length > 0) {
                for (String tmpString2 : tmpStrs) {
                    if (tmpString2.trim().length() > 0) {
                        m_LayersList.add(tmpString2.trim());
                    }
                }
            }
            LoadDuChaLayerMustFields();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void LoadDuChaLayerMustFields() {
        String[] tmpStrs02;
        String[] tmpStrs03;
        try {
            m_DuChaLayerMustFieldsName = new HashMap<>();
            m_DuChaLayerMustFieldsName.put("???", "???");
            m_DuChaLayerMustFieldsName.put("???", "???");
            m_DuChaLayerMustFieldsName.put("??????", "??????");
            m_DuChaLayerMustFieldsName.put("???", "???");
            m_DuChaLayerMustFieldsName.put("?????????", "?????????");
            m_DuChaLayerMustFieldsName.put("????????????", "????????????");
            m_DuChaLayerMustFieldsName.put("??????????????????", "??????????????????");
            m_DuChaLayerMustFieldsName.put("??????????????????", "??????????????????");
            m_DuChaLayerMustFieldsName.put("?????????", "?????????");
            m_DuChaLayerMustFieldsName.put("?????????", "?????????");
            m_DuChaLayerMustFieldsName.put("????????????", "????????????");
            m_DuChaLayerMustFieldsName.put("??????????????????", "??????????????????");
            m_DuChaLayerMustFieldsName.put("?????????", "?????????");
            m_DuChaLayerMustFieldsName.put("?????????", "?????????");
            m_DuChaLayerMustFieldsName.put("????????????????????????", "????????????????????????");
            m_DuChaLayerMustFieldsName.put("???????????????????????????????????????", "???????????????????????????????????????");
            m_DuChaLayerMustFieldsName.put("???????????????????????????????????????????????????", "???????????????????????????????????????????????????");
            m_DuChaLayerMustFieldsName.put("??????????????????", "??????????????????");
            m_DuChaLayerMustFieldsName.put("??????????????????????????????", "??????????????????????????????");
            m_DuChaLayerMustFieldsName.put("??????????????????", "??????????????????");
            m_DuChaLayerMustFieldsName.put("????????????", "????????????");
            m_DuChaLayerMustFieldsName.put("????????????????????????", "????????????????????????");
            m_DuChaLayerMustFieldsName.put("??????????????????", "??????????????????");
            m_DuChaLayerMustFieldsName.put("??????????????????", "??????????????????");
            m_DuChaLayerMustFieldsName.put("????????????", "????????????");
            m_DuChaLayerMustFieldsName.put("????????????", "????????????");
            m_DuChaLayerMustFieldsNameList = new ArrayList();
            m_DuChaLayerMustFieldsNameList.add("???");
            m_DuChaLayerMustFieldsNameList.add("???");
            m_DuChaLayerMustFieldsNameList.add("??????");
            m_DuChaLayerMustFieldsNameList.add("???");
            m_DuChaLayerMustFieldsNameList.add("?????????");
            m_DuChaLayerMustFieldsNameList.add("????????????");
            m_DuChaLayerMustFieldsNameList.add("??????????????????");
            m_DuChaLayerMustFieldsNameList.add("??????????????????");
            m_DuChaLayerMustFieldsNameList.add("?????????");
            m_DuChaLayerMustFieldsNameList.add("?????????");
            m_DuChaLayerMustFieldsNameList.add("????????????");
            m_DuChaLayerMustFieldsNameList.add("??????????????????");
            m_DuChaLayerMustFieldsNameList.add("?????????");
            m_DuChaLayerMustFieldsNameList.add("?????????");
            m_DuChaLayerMustFieldsNameList.add("????????????????????????");
            m_DuChaLayerMustFieldsNameList.add("???????????????????????????????????????");
            m_DuChaLayerMustFieldsNameList.add("???????????????????????????????????????????????????");
            m_DuChaLayerMustFieldsNameList.add("??????????????????");
            m_DuChaLayerMustFieldsNameList.add("??????????????????????????????");
            m_DuChaLayerMustFieldsNameList.add("??????????????????");
            m_DuChaLayerMustFieldsNameList.add("????????????");
            m_DuChaLayerMustFieldsNameList.add("????????????????????????");
            m_DuChaLayerMustFieldsNameList.add("??????????????????");
            m_DuChaLayerMustFieldsNameList.add("??????????????????");
            m_DuChaLayerMustFieldsNameList.add("????????????");
            m_DuChaLayerMustFieldsNameList.add("????????????");
            String tmpString2 = GetParaValue("??????????????????");
            if (tmpString2 != null && tmpString2.length() > 0 && (tmpStrs02 = tmpString2.split(";")) != null && tmpStrs02.length > 0) {
                for (String tmpString3 : tmpStrs02) {
                    if (tmpString3.trim().length() > 0 && (tmpStrs03 = tmpString3.split("=")) != null && tmpStrs03.length > 1) {
                        m_DuChaLayerMustFieldsName.put(tmpStrs03[0].trim(), tmpStrs03[1].trim());
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public static List<String> getCheckLayersList() {
        return m_LayersList;
    }

    public static void SaveCheckLayersList() {
        StringBuilder tmpStringBuilder = new StringBuilder();
        for (String str : m_LayersList) {
            tmpStringBuilder.append(str);
            tmpStringBuilder.append(";");
        }
        SaveParaValue("????????????", tmpStringBuilder.toString());
    }

    public static boolean IsDuchaLayer(String layerID) {
        if (m_LayersList != null && m_LayersList.size() > 0) {
            for (String str : m_LayersList) {
                if (str.equals(layerID)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static HashMap<String, Double[]> getGenJingCaiJiTable(boolean loadMust) {
        try {
            if (m_GenJingCaiJiTable == null || m_GenJingCaiJiTable.size() == 0 || loadMust) {
                m_GenJingCaiJiTable = new HashMap<>();
                SQLiteReader localSQLiteDataReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("Select GenJing,Shan,Ma,Kuo from T_GenJingTable");
                if (localSQLiteDataReader != null) {
                    while (localSQLiteDataReader.Read()) {
                        m_GenJingCaiJiTable.put(localSQLiteDataReader.GetString(0), new Double[]{Double.valueOf(localSQLiteDataReader.GetDouble(1)), Double.valueOf(localSQLiteDataReader.GetDouble(2)), Double.valueOf(localSQLiteDataReader.GetDouble(3))});
                    }
                    localSQLiteDataReader.Close();
                }
            }
        } catch (Exception e) {
        }
        return m_GenJingCaiJiTable;
    }

    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x002d: APUT  
      (r0v0 'result' double[] A[D('result' double[])])
      (0 ??[int, short, byte, char])
      (r2v1 'tmpD01' double A[D('tmpD01' double)])
     */
    public static double[] CalCaiJi(HashMap<String, Double[]> yangdiTable) {
        double[] result = new double[3];
        double tmpD01 = 0.0d;
        double tmpD02 = 0.0d;
        double tmpD03 = 0.0d;
        HashMap<String, Double[]> tmpGenJinTableHashMap = getGenJingCaiJiTable(false);
        if (yangdiTable != null && yangdiTable.size() > 0 && tmpGenJinTableHashMap != null && tmpGenJinTableHashMap.size() > 0) {
            for (Map.Entry<String, Double[]> tmpEntry : yangdiTable.entrySet()) {
                String tmpGenJingString = tmpEntry.getKey();
                if (tmpGenJinTableHashMap.containsKey(tmpGenJingString)) {
                    Double[] tmpDoubles01 = tmpGenJinTableHashMap.get(tmpGenJingString);
                    Double[] tmpDoubles02 = tmpEntry.getValue();
                    tmpD01 += tmpDoubles01[0].doubleValue() * tmpDoubles02[0].doubleValue();
                    tmpD02 += tmpDoubles01[1].doubleValue() * tmpDoubles02[1].doubleValue();
                    tmpD03 += tmpDoubles01[2].doubleValue() * tmpDoubles02[2].doubleValue();
                }
            }
        }
        result[0] = tmpD01;
        result[1] = tmpD02;
        result[2] = tmpD03;
        return result;
    }

    public static HashMap<String, Integer[]> getYangDiDataTableByYangDiName(String yangdiName) {
        try {
            if (GetSenLinDuChaSQLiteDBHelper() != null) {
                HashMap<String, Integer[]> result = new HashMap<>();
                SQLiteReader localSQLiteDataReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("Select GenJing,Shan,Ma,Kuo from T_YangDiData Where YangDiName='" + yangdiName + "'");
                if (localSQLiteDataReader != null) {
                    while (localSQLiteDataReader.Read()) {
                        result.put(String.valueOf(localSQLiteDataReader.GetInt32(0)), new Integer[]{Integer.valueOf(localSQLiteDataReader.GetInt32(1)), Integer.valueOf(localSQLiteDataReader.GetInt32(2)), Integer.valueOf(localSQLiteDataReader.GetInt32(3))});
                    }
                    localSQLiteDataReader.Close();
                    return result;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static boolean DeleteYangdiData(List<String> yangdiNameList) {
        try {
            SQLiteDBHelper tmpSqLiteDBHelper = GetSenLinDuChaSQLiteDBHelper();
            if (tmpSqLiteDBHelper != null) {
                String tmpSql2 = Common.CombineStrings("','", yangdiNameList);
                tmpSqLiteDBHelper.ExecuteSQL("Delete from T_YangDiInfo Where YangDiName IN ('" + tmpSql2 + "')");
                tmpSqLiteDBHelper.ExecuteSQL("Delete from T_YangDiData Where YangDiName IN ('" + tmpSql2 + "')");
                return true;
            }
        } catch (Exception e) {
            Common.ShowDialog("??????:\r\n" + e.getLocalizedMessage());
        }
        return false;
    }
}
