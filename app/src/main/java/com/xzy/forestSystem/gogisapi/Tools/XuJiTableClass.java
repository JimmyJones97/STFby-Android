package  com.xzy.forestSystem.gogisapi.Tools;

import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class XuJiTableClass {
    public Integer MaxTreeHeight = 0;
    public Integer MaxTreeRadius = 0;
    public Integer MinTreeHeight = 0;
    public Integer MinTreeRadius = 0;
    public String Name = "";
    public String Remark = "";
    public String TableID = "";
    public List<Integer[]> TreeCountExtendList = null;
    private List<List<Integer>> TreeCountList = null;
    public List<Integer> TreeHeights = null;
    public List<Integer> TreeRadius = null;
    public List<Double> TreeVolumns = null;
    public String ZoneName = "";

    public List<List<Integer>> getTreeCountList() {
        try {
            if (this.TreeCountList == null && this.TreeRadius != null) {
                this.TreeCountList = new ArrayList();
                this.TreeCountExtendList = new ArrayList();
                String tmpSql = "Select ";
                int count = this.TreeRadius.size();
                for (int i = 0; i < count; i++) {
                    if (i > 0) {
                        tmpSql = String.valueOf(tmpSql) + ",";
                    }
                    tmpSql = String.valueOf(tmpSql) + "F" + String.valueOf(i + 1);
                }
                SQLiteReader tmpSQLiteReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query(String.valueOf(tmpSql) + " From " + this.TableID);
                if (tmpSQLiteReader != null) {
                    while (tmpSQLiteReader.Read()) {
                        List<Integer> tmpList = new ArrayList<>();
                        Integer tmpMax = 0;
                        Integer tmpMin = Integer.MIN_VALUE;
                        for (int i2 = 0; i2 < count; i2++) {
                            int tmpI = tmpSQLiteReader.GetInt32(i2);
                            tmpList.add(Integer.valueOf(tmpI));
                            if (tmpMax.intValue() < tmpI) {
                                tmpMax = Integer.valueOf(tmpI);
                            }
                            if (tmpMin.intValue() > tmpI) {
                                tmpMin = Integer.valueOf(tmpI);
                            }
                        }
                        this.TreeCountList.add(tmpList);
                        this.TreeCountExtendList.add(new Integer[]{tmpMin, tmpMax});
                    }
                    tmpSQLiteReader.Close();
                }
            }
        } catch (Exception e) {
        }
        return this.TreeCountList;
    }

    public void setTreeCountList(List<List<Integer>> treeCountList) {
        this.TreeCountList = treeCountList;
    }

    public static List<String> GetShengList() {
        List<String> result = new ArrayList<>();
        try {
            SQLiteReader tmpSQLiteReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("Select ZoneName From XuJiTable Where TableID <>'' Group By ZoneName");
            if (tmpSQLiteReader != null) {
                while (tmpSQLiteReader.Read()) {
                    String tmpnameString = tmpSQLiteReader.GetString(0).trim();
                    if (tmpnameString.length() > 0) {
                        result.add(tmpnameString);
                    }
                }
                tmpSQLiteReader.Close();
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static List<XuJiTableClass> GetXuJiTablesInfo() {
        String[] tmpStrs;
        String[] tmpStrs2;
        String[] tmpStrs3;
        List<XuJiTableClass> result = new ArrayList<>();
        try {
            SQLiteReader tmpSQLiteReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("Select Name,ZoneName,Remark,TableID,TreeRadiusStr,TreeHeightsStr,TreeVolumnsStr From XuJiTable Where TableID <>'' Order By ZoneName");
            if (tmpSQLiteReader != null) {
                while (tmpSQLiteReader.Read()) {
                    String tmpNameString = tmpSQLiteReader.GetString(0);
                    String tmpZoneName = tmpSQLiteReader.GetString(1);
                    String tmpRemark = tmpSQLiteReader.GetString(2);
                    XuJiTableClass tmpXuJiTable = new XuJiTableClass();
                    tmpXuJiTable.Name = tmpNameString;
                    tmpXuJiTable.ZoneName = tmpZoneName;
                    tmpXuJiTable.Remark = tmpRemark;
                    tmpXuJiTable.TableID = tmpSQLiteReader.GetString(3);
                    tmpXuJiTable.TreeRadius = new ArrayList();
                    String tmpStr01 = tmpSQLiteReader.GetString(4);
                    if (tmpStr01.length() > 0 && (tmpStrs3 = tmpStr01.split(";")) != null && tmpStrs3.length > 0) {
                        Integer tmpMax = 0;
                        Integer tmpMin = Integer.MAX_VALUE;
                        int length = tmpStrs3.length;
                        for (int i = 0; i < length; i++) {
                            Integer tmpInteger = Integer.valueOf(Integer.parseInt(tmpStrs3[i]));
                            tmpXuJiTable.TreeRadius.add(tmpInteger);
                            if (tmpMax.intValue() < tmpInteger.intValue()) {
                                tmpMax = tmpInteger;
                            }
                            if (tmpMin.intValue() > tmpInteger.intValue()) {
                                tmpMin = tmpInteger;
                            }
                        }
                        tmpXuJiTable.MaxTreeRadius = tmpMax;
                        tmpXuJiTable.MinTreeRadius = tmpMin;
                    }
                    tmpXuJiTable.TreeHeights = new ArrayList();
                    String tmpStr012 = tmpSQLiteReader.GetString(5);
                    if (tmpStr012.length() > 0 && (tmpStrs2 = tmpStr012.split(";")) != null && tmpStrs2.length > 0) {
                        Integer tmpMax2 = 0;
                        Integer tmpMin2 = Integer.MAX_VALUE;
                        int length2 = tmpStrs2.length;
                        for (int i2 = 0; i2 < length2; i2++) {
                            Integer tmpInteger2 = Integer.valueOf(Integer.parseInt(tmpStrs2[i2]));
                            tmpXuJiTable.TreeHeights.add(tmpInteger2);
                            if (tmpMax2.intValue() < tmpInteger2.intValue()) {
                                tmpMax2 = tmpInteger2;
                            }
                            if (tmpMin2.intValue() > tmpInteger2.intValue()) {
                                tmpMin2 = tmpInteger2;
                            }
                        }
                        tmpXuJiTable.MaxTreeHeight = tmpMax2;
                        tmpXuJiTable.MinTreeHeight = tmpMin2;
                    }
                    tmpXuJiTable.TreeVolumns = new ArrayList();
                    String tmpStr013 = tmpSQLiteReader.GetString(6);
                    if (tmpStr013.length() > 0 && (tmpStrs = tmpStr013.split(";")) != null && tmpStrs.length > 0) {
                        int length3 = tmpStrs.length;
                        for (int i3 = 0; i3 < length3; i3++) {
                            tmpXuJiTable.TreeVolumns.add(Double.valueOf(Double.parseDouble(tmpStrs[i3])));
                        }
                    }
                    result.add(tmpXuJiTable);
                }
                tmpSQLiteReader.Close();
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static XuJiTableClass GetXuJiTable(String zoneName, String tablename) {
        String[] tmpStrs;
        String[] tmpStrs2;
        String[] tmpStrs3;
        XuJiTableClass resul = null;
        try {
            SQLiteReader tmpSQLiteReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("Select Name,ZoneName,Remark,TableID,TreeRadiusStr,TreeHeightsStr,TreeVolumnsStr From XuJiTable Where Name='" + tablename + "' And ZoneName='" + zoneName + "' And TableID <>''");
            if (tmpSQLiteReader == null) {
                return null;
            }
            if (tmpSQLiteReader.Read()) {
                XuJiTableClass resul2 = new XuJiTableClass();
                try {
                    String tmpNameString = tmpSQLiteReader.GetString(0);
                    String tmpZoneName = tmpSQLiteReader.GetString(1);
                    String tmpRemark = tmpSQLiteReader.GetString(2);
                    resul2.Name = tmpNameString;
                    resul2.ZoneName = tmpZoneName;
                    resul2.Remark = tmpRemark;
                    resul2.TableID = tmpSQLiteReader.GetString(3);
                    resul2.TreeRadius = new ArrayList();
                    String tmpStr01 = tmpSQLiteReader.GetString(4);
                    if (tmpStr01.length() > 0 && (tmpStrs3 = tmpStr01.split(";")) != null && tmpStrs3.length > 0) {
                        Integer tmpMax = 0;
                        Integer tmpMin = Integer.MAX_VALUE;
                        int length = tmpStrs3.length;
                        for (int i = 0; i < length; i++) {
                            Integer tmpInteger = Integer.valueOf(Integer.parseInt(tmpStrs3[i]));
                            resul2.TreeRadius.add(tmpInteger);
                            if (tmpMax.intValue() < tmpInteger.intValue()) {
                                tmpMax = tmpInteger;
                            }
                            if (tmpMin.intValue() > tmpInteger.intValue()) {
                                tmpMin = tmpInteger;
                            }
                        }
                        resul2.MaxTreeRadius = tmpMax;
                        resul2.MinTreeRadius = tmpMin;
                    }
                    resul2.TreeHeights = new ArrayList();
                    String tmpStr012 = tmpSQLiteReader.GetString(5);
                    if (tmpStr012.length() > 0 && (tmpStrs2 = tmpStr012.split(";")) != null && tmpStrs2.length > 0) {
                        Integer tmpMax2 = 0;
                        Integer tmpMin2 = Integer.MAX_VALUE;
                        int length2 = tmpStrs2.length;
                        for (int i2 = 0; i2 < length2; i2++) {
                            Integer tmpInteger2 = Integer.valueOf(Integer.parseInt(tmpStrs2[i2]));
                            resul2.TreeHeights.add(tmpInteger2);
                            if (tmpMax2.intValue() < tmpInteger2.intValue()) {
                                tmpMax2 = tmpInteger2;
                            }
                            if (tmpMin2.intValue() > tmpInteger2.intValue()) {
                                tmpMin2 = tmpInteger2;
                            }
                        }
                        resul2.MaxTreeHeight = tmpMax2;
                        resul2.MinTreeHeight = tmpMin2;
                    }
                    resul2.TreeVolumns = new ArrayList();
                    String tmpStr013 = tmpSQLiteReader.GetString(6);
                    if (tmpStr013.length() > 0 && (tmpStrs = tmpStr013.split(";")) != null && tmpStrs.length > 0) {
                        int length3 = tmpStrs.length;
                        for (int i3 = 0; i3 < length3; i3++) {
                            resul2.TreeVolumns.add(Double.valueOf(Double.parseDouble(tmpStrs[i3])));
                        }
                    }
                    resul = resul2;
                } catch (Exception e) {
                    return resul2;
                }
            }
            tmpSQLiteReader.Close();
            return resul;
        } catch (Exception e2) {
            return null;
        }
    }

    public static boolean DeleteXuJiTable(XuJiTableClass xujiTable) {
        try {
            SQLiteDBHelper tmpSqLiteDBHelper = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase();
            if (tmpSqLiteDBHelper.ExecuteSQL("Delete From XuJiTable Where Name='" + xujiTable.Name + "' And ZoneName='" + xujiTable.ZoneName + "'")) {
                return tmpSqLiteDBHelper.ExecuteSQL("Drop Table " + xujiTable.TableID);
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean SaveXuJiTable(XuJiTableClass xujiTable) {
        String tmpFIDStrings = "";
        try {
            SQLiteDBHelper tmpSqLiteDBHelper = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase();
            String tmpRandomTableID = "XJT" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
            tmpSqLiteDBHelper.ExecuteSQL("Insert Into XuJiTable (Name,ZoneName,tableid,TreeRadiusStr,TreeHeightsStr,TreeVolumnsStr,Remark) Values ('" + xujiTable.Name + "','" + xujiTable.ZoneName + "','" + tmpRandomTableID + "','" + Common.CombineIntegers(";", xujiTable.TreeRadius) + "','" + Common.CombineIntegers(";", xujiTable.TreeHeights) + "','" + Common.CombineStrings(";", xujiTable.TreeVolumns) + "','" + xujiTable.Remark + "')");
            String tmpSql = "Create Table If Not Exists " + tmpRandomTableID + " (ID integer primary key AutoIncrement";
            int count = xujiTable.TreeRadius.size();
            for (int i = 0; i < count; i++) {
                tmpSql = String.valueOf(tmpSql) + ",F" + String.valueOf(i + 1) + " integer Default 0";
                if (tmpFIDStrings.length() > 0) {
                    tmpFIDStrings = String.valueOf(tmpFIDStrings) + ",";
                }
                tmpFIDStrings = String.valueOf(tmpFIDStrings) + "F" + String.valueOf(i + 1);
            }
            tmpSqLiteDBHelper.ExecuteSQL(String.valueOf(tmpSql) + ")");
            for (List<Integer> tmpList01Integers : xujiTable.TreeCountList) {
                if (tmpList01Integers != null && tmpList01Integers.size() > 0) {
                    tmpSqLiteDBHelper.ExecuteSQL("Insert Into " + tmpRandomTableID + " (" + tmpFIDStrings + ") Values (" + Common.CombineIntegers(",", tmpList01Integers) + ")");
                }
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
