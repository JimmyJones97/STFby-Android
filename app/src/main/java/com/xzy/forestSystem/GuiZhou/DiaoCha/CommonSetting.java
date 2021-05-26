package com.xzy.forestSystem.GuiZhou.DiaoCha;

import android.content.Context;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonSetting {
    public static final SimpleDateFormat DiaoChaDateFormat = new SimpleDateFormat("yyyy/MM/dd");
    static DecimalFormat XIONGJINGFormat = new DecimalFormat("#.#");
    static DecimalFormat XUJIFormat = new DecimalFormat("#.#");
    static DecimalFormat ZHUSHUFormat = new DecimalFormat("#");
    static List<String> m_CalFieldsNameList = new ArrayList();
    static SQLiteDBHelper m_SQLiteDBHelper = null;
    static HashMap<String, List<String>> m_ShuZhongZuHashMap = new HashMap<>();
    static List<String> m_ShuZhongZuList = new ArrayList();
    static HashMap<String, String> m_XiaoBanLayerMustFieldsName = new HashMap<>();
    static List<String> m_XiaoBanLayerMustFieldsNameList = new ArrayList();
    static List<String> m_XiaoBanLayersList = new ArrayList();

    public static boolean InitialSetting() {
        try {
            InitialShuZhongTable();
            m_XiaoBanLayersList = new ArrayList();
            String[] tmpStrs = GetParaValue("小班图层").split(";");
            if (tmpStrs != null && tmpStrs.length > 0) {
                for (String tmpString2 : tmpStrs) {
                    if (tmpString2.trim().length() > 0) {
                        m_XiaoBanLayersList.add(tmpString2.trim());
                    }
                }
            }
            LoadXiaoLayerMustFields();
            m_CalFieldsNameList = new ArrayList();
            m_CalFieldsNameList.add("散生木树种");
            m_CalFieldsNameList.add("散生木平均胸径");
            m_CalFieldsNameList.add("散生木平均高");
            m_CalFieldsNameList.add("小班散生株数");
            m_CalFieldsNameList.add("小班散生蓄积");
            m_CalFieldsNameList.add("优势树种");
            m_CalFieldsNameList.add("优势树种平均胸径");
            m_CalFieldsNameList.add("优势树种平均高");
            m_CalFieldsNameList.add("优势树种公顷蓄积");
            m_CalFieldsNameList.add("优势树种公顷株数");
            m_CalFieldsNameList.add("主要组成树种");
            m_CalFieldsNameList.add("组成树种平均胸径");
            m_CalFieldsNameList.add("组成树种平均高");
            m_CalFieldsNameList.add("组成树种公顷蓄积");
            m_CalFieldsNameList.add("组成树种公顷株数");
            m_CalFieldsNameList.add("小班林木蓄积");
            m_CalFieldsNameList.add("小班林木株数");
            m_CalFieldsNameList.add("小班蓄积");
            m_CalFieldsNameList.add("树种组成");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static void InitialShuZhongTable() {
        m_ShuZhongZuHashMap = new HashMap<>();
        m_ShuZhongZuList = new ArrayList();
        SQLiteDBHelper tmpSQLiteDBHelper = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase();
        if (tmpSQLiteDBHelper != null && tmpSQLiteDBHelper.IsExistTable("ShuZhongTable")) {
            SQLiteReader tmpSqLiteReader = tmpSQLiteDBHelper.Query("Select Distinct ShuZhongZu From ShuZhongTable");
            if (tmpSqLiteReader != null) {
                while (tmpSqLiteReader.Read()) {
                    String tmpShuZhongZuString = tmpSqLiteReader.GetString(0);
                    m_ShuZhongZuHashMap.put(tmpShuZhongZuString, new ArrayList<>());
                    m_ShuZhongZuList.add(tmpShuZhongZuString);
                }
            }
            if (m_ShuZhongZuHashMap.size() > 0) {
                for (Map.Entry<String, List<String>> tmpEntry : m_ShuZhongZuHashMap.entrySet()) {
                    List<String> tmpList = tmpEntry.getValue();
                    SQLiteReader tmpSqLiteReader2 = tmpSQLiteDBHelper.Query("Select ShuZhong From ShuZhongTable Where ShuZhongZu='" + tmpEntry.getKey() + "'");
                    if (tmpSqLiteReader2 != null) {
                        while (tmpSqLiteReader2.Read()) {
                            tmpList.add(tmpSqLiteReader2.GetString(0));
                        }
                    }
                }
            }
        }
    }

    public static List<String> GetShuZhongZuList() {
        return m_ShuZhongZuList;
    }

    public static List<String> GetCalFieldsNameList() {
        return m_CalFieldsNameList;
    }

    public static SQLiteDBHelper GetSQLiteDBHelper() {
        String tmpPath = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/DiaoCha.dbx";
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
                    m_SQLiteDBHelper.ExecuteSQL("Create Table If Not Exists T_YangDiInfo (ID integer primary key AutoIncrement,YangDiName varchar(1024) NOT NULL Unique,YangDiIndex varchar(10) Default '', Xian varchar(50) Default '',Xiang varchar(50) Default '',Cun varchar(50) Default '',XiaoBan varchar(50) Default '',LayerID varchar(50),SYSID varchar(10), X Double Default 0, Y Double Default 0,YDArea Double Default 0, YouShiShuZhong varchar(50) Default '',ZhuZhongZuCheng varchar(100) Default '',QiYuan varchar(50) Default '',QiaoMuYBD varchar(50) Default '0',GuanMuFGD varchar(50) Default '0',PJNL varchar(50) Default '0',LingZu varchar(50) Default '', PhotoInfo varchar(4096) Default '',DiaoChaRen varchar(50) Default '',DiaoChaTime varchar(50) Default '', Remark TEXT )");
                    m_SQLiteDBHelper.ExecuteSQL("Create Table If Not Exists T_YangDiData (ID integer primary key AutoIncrement,YangDiName varchar(1024) Default '',ShuZhong varchar(10) Default '',ShuZhongZu varchar(10) Default '',XiongJing float Default 0,PJMShuGao float Default 0, X Double Default 0, Y Double Default 0,Z Double Default 0, Remark TEXT )");
                    if (!m_SQLiteDBHelper.CheckColumnExist("T_YangDiData", "X")) {
                        m_SQLiteDBHelper.ExecuteSQL("ALTER TABLE T_YangDiData ADD X Double Default 0");
                        m_SQLiteDBHelper.ExecuteSQL("ALTER TABLE T_YangDiData ADD Y Double Default 0");
                        m_SQLiteDBHelper.ExecuteSQL("ALTER TABLE T_YangDiData ADD Z Double Default 0");
                    }
                    m_SQLiteDBHelper.ExecuteSQL("Create Table If Not Exists T_CeShuYinZiData_ShuZhongZu (ID integer primary key AutoIncrement,YangDiName varchar(1024) Default '',XiangMu varchar(10) Default '',HeJi varchar(20) Default '',SM varchar(20) Default '',MWS varchar(20) Default '',BM varchar(20) Default '',YNS varchar(20) Default '',HSS varchar(20) Default '',RK varchar(20) Default '',YK varchar(20) Default '',MZ varchar(20) Default '', Remark TEXT )");
                    m_SQLiteDBHelper.ExecuteSQL("Create Table If Not Exists T_CeShuYinZiTable_ShuZhong (ID integer primary key AutoIncrement,YangDiName varchar(1024) Default '',ShuZhong varchar(10) Default '',ShuZhongZu varchar(10) Default '',PJXJ varchar(20) Default '',PJSG varchar(20) Default '',YDXJ varchar(20) Default '',GQXJ varchar(20) Default '',YDZS varchar(20) Default '',GQZS varchar(20) Default '',Remark TEXT )");
                    m_SQLiteDBHelper.ExecuteSQL("Create Table If Not Exists T_SanShengMuInfo (ID integer primary key AutoIncrement,YangDiName varchar(1024) NOT NULL Unique,YangDiIndex varchar(10) Default '', Xian varchar(50) Default '',Xiang varchar(50) Default '',Cun varchar(50) Default '',XiaoBan varchar(50) Default '',LayerID varchar(50),SYSID varchar(10), X Double Default 0, Y Double Default 0,ShuZhong varchar(10) Default '',ShuZhongZu varchar(10) Default '',XiongJing varchar(10) Default '',ShuGao varchar(10) Default '',ZhuShu varchar(10) Default '', XuJi varchar(20) Default '', DiaoChaTime varchar(50) Default '',Remark TEXT )");
                }
            } catch (Exception e) {
                Common.Log("错误", e.getLocalizedMessage());
            }
        }
        return m_SQLiteDBHelper;
    }

    public static void LoadXiaoLayerMustFields() {
        String[] tmpStrs02;
        String[] tmpStrs03;
        try {
            m_XiaoBanLayerMustFieldsName = new HashMap<>();
            m_XiaoBanLayerMustFieldsName.put("县", "县");
            m_XiaoBanLayerMustFieldsName.put("乡", "乡");
            m_XiaoBanLayerMustFieldsName.put("村", "村");
            m_XiaoBanLayerMustFieldsName.put("林地落界原小班号", "林地落界原小班号");
            m_XiaoBanLayerMustFieldsName.put("公益林区划界定原小班号", "公益林区划界定原小班号");
            m_XiaoBanLayerMustFieldsName.put("二类调查原小班号", "二类调查原小班号");
            m_XiaoBanLayerMustFieldsName.put("现状小班号", "现状小班号");
            m_XiaoBanLayerMustFieldsName.put("土地用途", "土地用途");
            m_XiaoBanLayerMustFieldsName.put("地类", "地类");
            m_XiaoBanLayerMustFieldsName.put("面积", "面积");
            m_XiaoBanLayerMustFieldsName.put("林地所有权", "林地所有权");
            m_XiaoBanLayerMustFieldsName.put("林木所有权", "林木所有权");
            m_XiaoBanLayerMustFieldsName.put("主体功能区", "主体功能区");
            m_XiaoBanLayerMustFieldsName.put("林地质量等级", "林地质量等级");
            m_XiaoBanLayerMustFieldsName.put("林地保护等级", "林地保护等级");
            m_XiaoBanLayerMustFieldsName.put("工程类别", "工程类别");
            m_XiaoBanLayerMustFieldsName.put("森林类别", "森林类别");
            m_XiaoBanLayerMustFieldsName.put("林种", "林种");
            m_XiaoBanLayerMustFieldsName.put("公益林事权等级", "公益林事权等级");
            m_XiaoBanLayerMustFieldsName.put("公益林保护等级", "公益林保护等级");
            m_XiaoBanLayerMustFieldsName.put("优势树种", "优势树种");
            m_XiaoBanLayerMustFieldsName.put("树种组成", "树种组成");
            m_XiaoBanLayerMustFieldsName.put("起源", "起源");
            m_XiaoBanLayerMustFieldsName.put("平均年龄", "平均年龄");
            m_XiaoBanLayerMustFieldsName.put("龄组", "龄组");
            m_XiaoBanLayerMustFieldsName.put("乔木郁闭度", "乔木郁闭度");
            m_XiaoBanLayerMustFieldsName.put("灌木覆盖度", "灌木覆盖度");
            m_XiaoBanLayerMustFieldsName.put("成活率", "成活率");
            m_XiaoBanLayerMustFieldsName.put("优势树种平均胸径", "优势树种平均胸径");
            m_XiaoBanLayerMustFieldsName.put("优势树种平均高", "优势树种平均高");
            m_XiaoBanLayerMustFieldsName.put("优势树种公顷蓄积", "优势树种公顷蓄积");
            m_XiaoBanLayerMustFieldsName.put("优势树种公顷株数", "优势树种公顷株数");
            m_XiaoBanLayerMustFieldsName.put("主要组成树种", "主要组成树种");
            m_XiaoBanLayerMustFieldsName.put("组成树种平均胸径", "组成树种平均胸径");
            m_XiaoBanLayerMustFieldsName.put("组成树种平均高", "组成树种平均高");
            m_XiaoBanLayerMustFieldsName.put("组成树种公顷蓄积", "组成树种公顷蓄积");
            m_XiaoBanLayerMustFieldsName.put("组成树种公顷株数", "组成树种公顷株数");
            m_XiaoBanLayerMustFieldsName.put("小班林木蓄积", "小班林木蓄积");
            m_XiaoBanLayerMustFieldsName.put("小班林木株数", "小班林木株数");
            m_XiaoBanLayerMustFieldsName.put("散生木树种", "散生木树种");
            m_XiaoBanLayerMustFieldsName.put("散生木平均胸径", "散生木平均胸径");
            m_XiaoBanLayerMustFieldsName.put("散生木平均高", "散生木平均高");
            m_XiaoBanLayerMustFieldsName.put("小班散生株数", "小班散生株数");
            m_XiaoBanLayerMustFieldsName.put("小班散生蓄积", "小班散生蓄积");
            m_XiaoBanLayerMustFieldsName.put("小班蓄积", "小班蓄积");
            m_XiaoBanLayerMustFieldsName.put("林层结构", "林层结构");
            m_XiaoBanLayerMustFieldsName.put("树种结构", "树种结构");
            m_XiaoBanLayerMustFieldsName.put("群落结构", "群落结构");
            m_XiaoBanLayerMustFieldsName.put("小班控制点坐标1", "小班控制点坐标1");
            m_XiaoBanLayerMustFieldsName.put("小班控制点坐标2", "小班控制点坐标2");
            m_XiaoBanLayerMustFieldsName.put("建设内容", "建设内容");
            m_XiaoBanLayerMustFieldsName.put("备注", "备注");
            m_XiaoBanLayerMustFieldsNameList = new ArrayList();
            m_XiaoBanLayerMustFieldsNameList.add("县");
            m_XiaoBanLayerMustFieldsNameList.add("乡");
            m_XiaoBanLayerMustFieldsNameList.add("村");
            m_XiaoBanLayerMustFieldsNameList.add("林地落界原小班号");
            m_XiaoBanLayerMustFieldsNameList.add("公益林区划界定原小班号");
            m_XiaoBanLayerMustFieldsNameList.add("二类调查原小班号");
            m_XiaoBanLayerMustFieldsNameList.add("现状小班号");
            m_XiaoBanLayerMustFieldsNameList.add("土地用途");
            m_XiaoBanLayerMustFieldsNameList.add("地类");
            m_XiaoBanLayerMustFieldsNameList.add("面积");
            m_XiaoBanLayerMustFieldsNameList.add("林地所有权");
            m_XiaoBanLayerMustFieldsNameList.add("林木所有权");
            m_XiaoBanLayerMustFieldsNameList.add("主体功能区");
            m_XiaoBanLayerMustFieldsNameList.add("林地质量等级");
            m_XiaoBanLayerMustFieldsNameList.add("林地保护等级");
            m_XiaoBanLayerMustFieldsNameList.add("工程类别");
            m_XiaoBanLayerMustFieldsNameList.add("森林类别");
            m_XiaoBanLayerMustFieldsNameList.add("林种");
            m_XiaoBanLayerMustFieldsNameList.add("公益林事权等级");
            m_XiaoBanLayerMustFieldsNameList.add("公益林保护等级");
            m_XiaoBanLayerMustFieldsNameList.add("优势树种");
            m_XiaoBanLayerMustFieldsNameList.add("树种组成");
            m_XiaoBanLayerMustFieldsNameList.add("起源");
            m_XiaoBanLayerMustFieldsNameList.add("平均年龄");
            m_XiaoBanLayerMustFieldsNameList.add("龄组");
            m_XiaoBanLayerMustFieldsNameList.add("乔木郁闭度");
            m_XiaoBanLayerMustFieldsNameList.add("灌木覆盖度");
            m_XiaoBanLayerMustFieldsNameList.add("成活率");
            m_XiaoBanLayerMustFieldsNameList.add("优势树种平均胸径");
            m_XiaoBanLayerMustFieldsNameList.add("优势树种平均高");
            m_XiaoBanLayerMustFieldsNameList.add("优势树种公顷蓄积");
            m_XiaoBanLayerMustFieldsNameList.add("优势树种公顷株数");
            m_XiaoBanLayerMustFieldsNameList.add("主要组成树种");
            m_XiaoBanLayerMustFieldsNameList.add("组成树种平均胸径");
            m_XiaoBanLayerMustFieldsNameList.add("组成树种平均高");
            m_XiaoBanLayerMustFieldsNameList.add("组成树种公顷蓄积");
            m_XiaoBanLayerMustFieldsNameList.add("组成树种公顷株数");
            m_XiaoBanLayerMustFieldsNameList.add("小班林木蓄积");
            m_XiaoBanLayerMustFieldsNameList.add("小班林木株数");
            m_XiaoBanLayerMustFieldsNameList.add("散生木树种");
            m_XiaoBanLayerMustFieldsNameList.add("散生木平均胸径");
            m_XiaoBanLayerMustFieldsNameList.add("散生木平均高");
            m_XiaoBanLayerMustFieldsNameList.add("小班散生株数");
            m_XiaoBanLayerMustFieldsNameList.add("小班散生蓄积");
            m_XiaoBanLayerMustFieldsNameList.add("小班蓄积");
            m_XiaoBanLayerMustFieldsNameList.add("林层结构");
            m_XiaoBanLayerMustFieldsNameList.add("树种结构");
            m_XiaoBanLayerMustFieldsNameList.add("群落结构");
            m_XiaoBanLayerMustFieldsNameList.add("小班控制点坐标1");
            m_XiaoBanLayerMustFieldsNameList.add("小班控制点坐标2");
            m_XiaoBanLayerMustFieldsNameList.add("建设内容");
            m_XiaoBanLayerMustFieldsNameList.add("备注");
            String tmpString2 = GetParaValue("小班字段对应");
            if (tmpString2 != null && tmpString2.length() > 0 && (tmpStrs02 = tmpString2.split(";")) != null && tmpStrs02.length > 0) {
                for (String tmpString3 : tmpStrs02) {
                    if (tmpString3.trim().length() > 0 && (tmpStrs03 = tmpString3.split("=")) != null && tmpStrs03.length > 1) {
                        m_XiaoBanLayerMustFieldsName.put(tmpStrs03[0].trim(), tmpStrs03[1].trim());
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public static String GetParaValue(String paraName) {
        String result = "";
        try {
            if (GetSQLiteDBHelper() != null) {
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
            SQLiteDBHelper tmpSqLiteDBHelper = GetSQLiteDBHelper();
            if (tmpSqLiteDBHelper != null) {
                return tmpSqLiteDBHelper.ExecuteSQL("Replace Into T_SysParams (ParaName,ParaValue) Values ('" + paraName + "','" + paraValue + "')");
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<String> getXiaoBanLayersList() {
        return m_XiaoBanLayersList;
    }

    public static void SaveXiaoBanLayersList() {
        StringBuilder tmpStringBuilder = new StringBuilder();
        for (String str : m_XiaoBanLayersList) {
            tmpStringBuilder.append(str);
            tmpStringBuilder.append(";");
        }
        SaveParaValue("小班图层", tmpStringBuilder.toString());
    }

    public static boolean SaveYangMuData(String yangdiName, String shuZhong, String xiongJing, String shuGao, String remark, String x, String y) {
        try {
            SQLiteDBHelper tmpSqLiteDBHelper = GetSQLiteDBHelper();
            if (tmpSqLiteDBHelper != null) {
                return tmpSqLiteDBHelper.ExecuteSQL("Insert Into T_YangDiData (YangDiName,ShuZhong,ShuZhongZu,XiongJing,PJMShuGao,Remark,X,Y) Values ('" + yangdiName + "','" + shuZhong + "','" + ConvertShuZhongToZu(shuZhong) + "'," + xiongJing + "," + shuGao + ",'" + remark + "'," + x + "," + y + ")");
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean UpdateYangMuData(String ID, String yangdiName, String shuZhong, String xiongJing, String shuGao, String remark, String x, String y) {
        try {
            SQLiteDBHelper tmpSqLiteDBHelper = GetSQLiteDBHelper();
            if (tmpSqLiteDBHelper != null) {
                return tmpSqLiteDBHelper.ExecuteSQL("Update T_YangDiData Set YangDiName='" + yangdiName + "',ShuZhong='" + shuZhong + "',ShuZhongZu='" + ConvertShuZhongToZu(shuZhong) + "',XiongJing=" + xiongJing + ",PJMShuGao=" + shuGao + ",Remark='" + remark + "',x=" + x + ",y=" + y + " Where ID=" + ID);
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean UpdateYangMuData_ShuGao(String ID, String shuGao) {
        try {
            SQLiteDBHelper tmpSqLiteDBHelper = GetSQLiteDBHelper();
            if (tmpSqLiteDBHelper != null) {
                return tmpSqLiteDBHelper.ExecuteSQL("Update T_YangDiData Set PJMShuGao=" + shuGao + " Where ID=" + ID);
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean DeleteYangMusData(List<String> IDList) {
        try {
            SQLiteDBHelper tmpSqLiteDBHelper = GetSQLiteDBHelper();
            if (tmpSqLiteDBHelper != null) {
                tmpSqLiteDBHelper.ExecuteSQL("Delete from T_YangDiData Where ID IN (" + Common.CombineStrings(",", IDList) + ")");
                return true;
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
        return false;
    }

    public static boolean DeleteYangDisData(List<String> yangdiNameList) {
        try {
            SQLiteDBHelper tmpSqLiteDBHelper = GetSQLiteDBHelper();
            if (tmpSqLiteDBHelper != null) {
                String tmpSql2 = Common.CombineStrings("','", yangdiNameList);
                tmpSqLiteDBHelper.ExecuteSQL("Delete from T_YangDiInfo Where YangDiName IN ('" + tmpSql2 + "')");
                tmpSqLiteDBHelper.ExecuteSQL("Delete from T_YangDiData Where YangDiName IN ('" + tmpSql2 + "')");
                return true;
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
        return false;
    }

    public static double CalCaiJi_ShanMu(double xiongJing, double shuGao) {
        return 8.8296E-5d * Math.pow(xiongJing, 1.94097d - (0.0044583d * (xiongJing + shuGao))) * Math.pow(shuGao, 0.76012d + (0.0056841d * (xiongJing + shuGao)));
    }

    public static double CalCaiJi_BoMu(double xiongJing, double shuGao) {
        return 8.5626E-5d * Math.pow(xiongJing, 1.9148d - (0.0045828d * (xiongJing + shuGao))) * Math.pow(shuGao, 0.74041d + (0.00668d * (xiongJing + shuGao)));
    }

    public static double CalCaiJi_RuanKuo(double xiongJing, double shuGao) {
        return 7.3624E-5d * Math.pow(xiongJing, 1.89885d) * Math.pow(shuGao, 0.85616d + (6.4635E-4d * (xiongJing + shuGao)));
    }

    public static double CalCaiJi_YingKuo(double xiongJing, double shuGao) {
        return 9.9985E-5d * Math.pow(xiongJing, 1.94225d - (0.0076853d * ((2.0d * shuGao) + xiongJing))) * Math.pow(shuGao, 0.64053d + (0.014257d * (xiongJing + shuGao)));
    }

    public static double CalCaiJi_MaWeiSong(double xiongJing, double shuGao) {
        return 9.4147E-5d * Math.pow(xiongJing, 1.93896d - (0.0042676d * (xiongJing + shuGao))) * Math.pow(shuGao, 0.70998d + (0.0059256d * (xiongJing + shuGao)));
    }

    public static double CalCaiJi_HuaShanSong(double xiongJing, double shuGao) {
        return 1.1996E-4d * Math.pow(xiongJing, 2.019601d - (0.0083683d * (xiongJing + shuGao))) * Math.pow(shuGao, 0.47225d + (0.012475d * (xiongJing + shuGao)));
    }

    public static double CalCaiJi_YunNanSong(double xiongJing, double shuGao) {
        return 1.0729E-4d * Math.pow(xiongJing, 1.95029d - (0.0047643d * (xiongJing + shuGao))) * Math.pow(shuGao, 0.63241d + (0.0075891d * (xiongJing + shuGao)));
    }

    public static List<String[]> GetXiaoBanYangDiMeiMuTable(String yangdiName) {
        List<String[]> result = null;
        try {
            if (GetSQLiteDBHelper() != null) {
                List<String[]> result2 = new ArrayList<>();
                try {
                    SQLiteReader tmpLiteReader = m_SQLiteDBHelper.Query("Select ShuZhong,XiongJing,PJMShuGao,Remark From T_YangDiData Where YangDiName='" + yangdiName + "'");
                    while (tmpLiteReader.Read()) {
                        result2.add(new String[]{tmpLiteReader.GetString(0), String.valueOf(tmpLiteReader.GetDouble(1)), String.valueOf(tmpLiteReader.GetDouble(2)), tmpLiteReader.GetString(3)});
                    }
                    return result2;
                } catch (Exception e) {
                    result = result2;
                }
            }
        } catch (Exception e2) {
        }
        return result;
    }

    public static HashMap<String, String[]> GetXiaoBanYangDiCeShuYinZiTable(String yangdiName) {
        HashMap<String, String[]> result = null;
        try {
            if (GetSQLiteDBHelper() != null) {
                HashMap<String, String[]> result2 = new HashMap<>();
                try {
                    SQLiteReader tmpLiteReader = m_SQLiteDBHelper.Query("Select XiangMu,HeJi,SM,MWS,BM,YNS,HSS,RK,YK,MZ From T_CeShuYinZiData_ShuZhongZu Where YangDiName='" + yangdiName + "'");
                    while (tmpLiteReader.Read()) {
                        String tmpNameString = tmpLiteReader.GetString(0);
                        String[] tmpStrs = new String[9];
                        for (int i = 0; i < 9; i++) {
                            tmpStrs[i] = tmpLiteReader.GetString(i + 1);
                        }
                        result2.put(tmpNameString, tmpStrs);
                    }
                    return result2;
                } catch (Exception e) {
                    result = result2;
                }
            }
        } catch (Exception e2) {
        }
        return result;
    }

    public static List<HashMap<String, String>> GetYangDiCeShuYinZiTable(String yangdiName) {
        List<HashMap<String, String>> result = null;
        try {
            if (GetSQLiteDBHelper() != null) {
                List<HashMap<String, String>> result2 = new ArrayList<>();
                try {
                    SQLiteReader tmpLiteReader = m_SQLiteDBHelper.Query("Select ShuZhong,PJXJ,PJSG,YDXJ,GQXJ,YDZS,GQZS From T_CeShuYinZiTable_ShuZhong Where YangDiName='" + yangdiName + "'");
                    String[] tmpColnames = tmpLiteReader.GetFieldNameList();
                    int count = tmpColnames.length;
                    while (tmpLiteReader.Read()) {
                        HashMap<String, String> tmphasMap = new HashMap<>();
                        for (int i = 0; i < count; i++) {
                            tmphasMap.put(tmpColnames[i], tmpLiteReader.GetString(i));
                        }
                        result2.add(tmphasMap);
                    }
                    return result2;
                } catch (Exception e) {
                    result = result2;
                }
            }
        } catch (Exception e2) {
        }
        return result;
    }

    public static void SaveYangDiMediaInfo(String YangdiName, String mediaInfo) {
        try {
            SQLiteDBHelper tmpSqLiteDBHelper = GetSQLiteDBHelper();
            if (tmpSqLiteDBHelper != null) {
                tmpSqLiteDBHelper.ExecuteSQL("Update T_YangDiInfo Set PhotoInfo='" + mediaInfo + "' Where YangDiName='" + YangdiName + "'");
            }
        } catch (Exception e) {
        }
    }

    public static String GetYangDiMediaInfo(String YangdiName) {
        try {
            if (GetSQLiteDBHelper() == null) {
                return "";
            }
            SQLiteReader tmpLiteReader = m_SQLiteDBHelper.Query("Select PhotoInfo From T_YangDiInfo Where YangDiName='" + YangdiName + "'");
            if (!tmpLiteReader.Read()) {
                return "";
            }
            String result = tmpLiteReader.GetString(0);
            tmpLiteReader.Close();
            return result;
        } catch (Exception e) {
            return "";
        }
    }

    public static String ConvertShuZhongToZu(String shuZhong) {
        for (Map.Entry<String, List<String>> tmpEntry : m_ShuZhongZuHashMap.entrySet()) {
            if (tmpEntry.getValue().contains(shuZhong)) {
                return tmpEntry.getKey();
            }
        }
        return "";
    }

    public static String[] GetXiaoBanLayerAndIDByYangdi(String yangdiName) {
        SQLiteReader tmpReader;
        SQLiteDBHelper tmpSqLiteDBHelper = GetSQLiteDBHelper();
        if (tmpSqLiteDBHelper == null || (tmpReader = tmpSqLiteDBHelper.Query("Select LayerID,SYSID From T_YangDiInfo Where YangDiName='" + yangdiName + "'")) == null || !tmpReader.Read()) {
            return null;
        }
        return new String[]{tmpReader.GetString(0), tmpReader.GetString(1)};
    }

    public static HashMap<String, String> CalXiaoBanYinZiByYangDi(String yangdiName) {
        AbstractGeometry tmpGeometry;
        SQLiteDBHelper tmpSqLiteDBHelper = GetSQLiteDBHelper();
        if (tmpSqLiteDBHelper == null) {
            return null;
        }
        double tmpXiaoBanArea = 1.0d;
        SQLiteReader tmpReader = tmpSqLiteDBHelper.Query("Select YangDiIndex,LayerID,SYSID,YDArea From T_YangDiInfo Where YangDiName='" + yangdiName + "'");
        if (tmpReader == null || !tmpReader.Read()) {
            return null;
        }
        String tmpLyrID = tmpReader.GetString(1);
        String tmpSYSID = tmpReader.GetString(2);
        GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByID(tmpLyrID);
        if (!(tmpGeoLayer == null || (tmpGeometry = tmpGeoLayer.getDataset().GetGeometryBySYSIDMust(tmpSYSID)) == null || !(tmpGeometry instanceof Polygon))) {
            tmpXiaoBanArea = ((Polygon) tmpGeometry).getArea(true) / 10000.0d;
        }
        List<String> tmpYDNameList = new ArrayList<>();
        List<Double> tmpYDAreaList = new ArrayList<>();
        SQLiteReader tmpReader2 = tmpSqLiteDBHelper.Query("Select YangDiName,YDArea From T_YangDiInfo Where LayerID='" + tmpLyrID + "' AND SYSID='" + tmpSYSID + "'");
        if (tmpReader2 != null) {
            while (tmpReader2.Read()) {
                tmpYDNameList.add(tmpReader2.GetString(0));
                tmpYDAreaList.add(Double.valueOf(tmpReader2.GetDouble(1)));
            }
        }
        if (tmpYDNameList.size() <= 0) {
            return null;
        }
        if (tmpYDNameList.size() == 1) {
            return CalXiaoBanYinZiByYangDi(tmpYDNameList.get(0), tmpXiaoBanArea);
        }
        HashMap<String, String> result = new HashMap<>();
        result.put("散生木树种", "");
        result.put("散生木平均胸径", "");
        result.put("散生木平均高", "");
        result.put("小班散生株数", "");
        result.put("小班散生蓄积", "");
        result.put("优势树种", "");
        result.put("优势树种平均胸径", "");
        result.put("优势树种平均高", "");
        result.put("优势树种公顷蓄积", "");
        result.put("优势树种公顷株数", "");
        result.put("主要组成树种", "");
        result.put("组成树种平均胸径", "");
        result.put("组成树种平均高", "");
        result.put("组成树种公顷蓄积", "");
        result.put("组成树种公顷株数", "");
        result.put("小班林木蓄积", "0");
        result.put("小班林木株数", "0");
        result.put("小班蓄积", "0");
        String[] tmpItemNamesStrings = {"优势树种平均胸径", "优势树种平均高", "优势树种公顷蓄积", "优势树种公顷株数", "组成树种平均胸径", "组成树种平均高", "组成树种公顷蓄积", "组成树种公顷株数", "小班林木蓄积", "小班林木株数", "小班蓄积"};
        double[] tmpDs01 = new double[tmpItemNamesStrings.length];
        for (String tmpYDNameString : tmpYDNameList) {
            HashMap<String, String> tmpHashMap = CalXiaoBanYinZiByYangDi(tmpYDNameString, tmpXiaoBanArea);
            if (tmpHashMap != null && tmpHashMap.size() > 0) {
                result.put("优势树种", tmpHashMap.get("优势树种"));
                result.put("主要组成树种", tmpHashMap.get("主要组成树种"));
                int tmpCount01 = tmpItemNamesStrings.length;
                for (int i = 0; i < tmpCount01; i++) {
                    double tmpD01 = 0.0d;
                    String tmpString = tmpHashMap.get(tmpItemNamesStrings[i]);
                    if (tmpString.trim().length() > 0) {
                        try {
                            tmpD01 = Double.parseDouble(tmpString);
                        } catch (Exception e) {
                        }
                    }
                    tmpDs01[i] = tmpDs01[i] + tmpD01;
                }
            }
        }
        int tmpCount = tmpYDNameList.size();
        int tmpCount012 = tmpItemNamesStrings.length;
        for (int i2 = 0; i2 < tmpCount012; i2++) {
            tmpDs01[i2] = tmpDs01[i2] / ((double) tmpCount);
        }
        for (int i3 = 0; i3 < 10; i3++) {
            result.put(tmpItemNamesStrings[i3], String.valueOf(tmpDs01[i3]));
        }
        return result;
    }

    public static HashMap<String, String> CalXiaoBanYinZiByYangDi(String yangdiName, double XiaoBanArea) {
        double tmpXiaoBanLinMuXuJi;
        int tmpXiaoBanLinMuZhuShu;
        SQLiteReader tmpLiteReader;
        HashMap<String, String> result = null;
        List<HashMap<String, String>> tmpList01 = GetYangDiCeShuYinZiTable(yangdiName);
        HashMap<String, String> tmpHashMap02 = null;
        if (tmpList01 != null && tmpList01.size() > 0) {
            double tmpMaxD = 0.0d;
            int tmpI01 = -1;
            int tmpTid01 = -1;
            List<Double> tmpXuJiList = new ArrayList<>();
            List<Integer> tmpXuJiIntList = new ArrayList<>();
            for (HashMap<String, String> tmpHashMap01 : tmpList01) {
                tmpTid01++;
                if (!tmpHashMap01.get("ShuZhong").equals("合计")) {
                    double tmpD01 = 0.0d;
                    try {
                        tmpD01 = Double.parseDouble(tmpHashMap01.get("YDXJ"));
                    } catch (Exception e) {
                    }
                    if (tmpMaxD < tmpD01) {
                        tmpI01 = tmpTid01;
                        tmpMaxD = tmpD01;
                    }
                    tmpXuJiList.add(Double.valueOf(tmpD01));
                    tmpXuJiIntList.add(Integer.valueOf(tmpTid01));
                } else {
                    tmpHashMap02 = tmpHashMap01;
                }
            }
            if (tmpI01 != -1) {
                HashMap<String, String> tmpHashMap012 = tmpList01.get(tmpI01);
                result = new HashMap<>();
                double tmpSSM_XuJi = 0.0d;
                try {
                    result.put("散生木树种", "");
                    result.put("散生木平均胸径", "");
                    result.put("散生木平均高", "");
                    result.put("小班散生株数", "");
                    result.put("小班散生蓄积", "");
                    String tmpXiaoBanFullNameString = yangdiName;
                    int tmpI001 = tmpXiaoBanFullNameString.lastIndexOf("_");
                    if (tmpI001 > 0) {
                        tmpXiaoBanFullNameString = tmpXiaoBanFullNameString.substring(0, tmpI001);
                    }
                    if (!(GetSQLiteDBHelper() == null || (tmpLiteReader = m_SQLiteDBHelper.Query("Select ShuZhong,XiongJing,ShuGao,ZhuShu,XuJi From T_SanShengMuInfo Where YangDiName='" + tmpXiaoBanFullNameString + "'")) == null || !tmpLiteReader.Read())) {
                        result.put("散生木树种", tmpLiteReader.GetString(0).trim());
                        result.put("散生木平均胸径", tmpLiteReader.GetString(1).trim());
                        result.put("散生木平均高", tmpLiteReader.GetString(2).trim());
                        result.put("小班散生株数", tmpLiteReader.GetString(3).trim());
                        String tmpString01 = tmpLiteReader.GetString(4).trim();
                        result.put("小班散生蓄积", tmpString01);
                        if (tmpString01.length() > 0) {
                            tmpSSM_XuJi = Double.parseDouble(tmpString01);
                        }
                        tmpLiteReader.Close();
                    }
                } catch (Exception e2) {
                }
                result.put("优势树种", tmpHashMap012.get("ShuZhong"));
                result.put("优势树种平均胸径", tmpHashMap012.get("PJXJ"));
                result.put("优势树种平均高", tmpHashMap012.get("PJSG"));
                result.put("优势树种公顷蓄积", tmpHashMap012.get("GQXJ"));
                result.put("优势树种公顷株数", tmpHashMap012.get("GQZS"));
                result.put("主要组成树种", "");
                result.put("组成树种平均胸径", "");
                result.put("组成树种平均高", "");
                result.put("组成树种公顷蓄积", "");
                result.put("组成树种公顷株数", "");
                result.put("小班林木蓄积", "0");
                result.put("小班林木株数", "0");
                result.put("小班蓄积", "0");
                if (tmpHashMap02 != null) {
                    if (XiaoBanArea < 0.06667d) {
                        double tmpD012 = 0.0d;
                        try {
                            tmpD012 = Double.parseDouble(tmpHashMap02.get("YDXJ"));
                        } catch (Exception e3) {
                        }
                        tmpXiaoBanLinMuXuJi = tmpD012;
                        try {
                            tmpD012 = Double.parseDouble(tmpHashMap02.get("YDZS"));
                        } catch (Exception e4) {
                        }
                        tmpXiaoBanLinMuZhuShu = (int) tmpD012;
                    } else {
                        double tmpD013 = 0.0d;
                        try {
                            tmpD013 = Double.parseDouble(tmpHashMap02.get("GQXJ"));
                        } catch (Exception e5) {
                        }
                        tmpXiaoBanLinMuXuJi = tmpD013 * XiaoBanArea;
                        try {
                            tmpD013 = Double.parseDouble(tmpHashMap02.get("GQZS"));
                        } catch (Exception e6) {
                        }
                        tmpXiaoBanLinMuZhuShu = (int) (tmpD013 * XiaoBanArea);
                    }
                    result.put("小班林木蓄积", XUJIFormat.format(tmpXiaoBanLinMuXuJi));
                    result.put("小班林木株数", String.valueOf(tmpXiaoBanLinMuZhuShu));
                    result.put("小班蓄积", XUJIFormat.format(tmpXiaoBanLinMuXuJi + tmpSSM_XuJi));
                }
                if (tmpXuJiList.size() > 1) {
                    int tmpI0012 = tmpXuJiIntList.indexOf(Integer.valueOf(tmpI01));
                    if (tmpI0012 > -1) {
                        tmpXuJiList.remove(tmpI0012);
                        tmpXuJiIntList.remove(tmpI01);
                    }
                    if (tmpXuJiList.size() > 0) {
                        double tmpD014 = tmpXuJiList.get(0).doubleValue();
                        int tmpI002 = 0;
                        int count = tmpXuJiList.size();
                        for (int i = 1; i < count; i++) {
                            double tmpD02 = tmpXuJiList.get(i).doubleValue();
                            if (tmpD014 < tmpD02) {
                                tmpD014 = tmpD02;
                                tmpI002 = i;
                            }
                        }
                        HashMap<String, String> tmpHashMap03 = tmpList01.get(tmpXuJiIntList.get(tmpI002).intValue());
                        result.put("主要组成树种", tmpHashMap03.get("ShuZhong"));
                        result.put("组成树种平均胸径", tmpHashMap03.get("PJXJ"));
                        result.put("组成树种平均高", tmpHashMap03.get("PJSG"));
                        result.put("组成树种公顷蓄积", tmpHashMap03.get("GQXJ"));
                        result.put("组成树种公顷株数", tmpHashMap03.get("GQZS"));
                    }
                }
                String tmpStr01 = result.get("优势树种");
                String tmpStr02 = result.get("优势树种公顷蓄积");
                String tmpStr03 = result.get("主要组成树种");
                String tmpStr04 = result.get("组成树种公顷蓄积");
                if (tmpStr01.length() > 0 && tmpStr02.length() > 0) {
                    if (tmpStr03.length() == 0 || tmpStr04.length() == 0) {
                        result.put("树种组成", "");
                    } else {
                        double tmpD015 = 0.0d;
                        double tmpD022 = 0.0d;
                        try {
                            tmpD015 = Double.parseDouble(tmpStr02);
                        } catch (Exception e7) {
                        }
                        if (tmpStr04.length() > 0) {
                            try {
                                tmpD022 = Double.parseDouble(tmpStr04);
                            } catch (Exception e8) {
                            }
                        }
                        if (tmpD015 + tmpD022 != 0.0d) {
                            double tmpD001 = (10.0d * tmpD015) / (tmpD015 + tmpD022);
                            int tmpMInt = (int) tmpD001;
                            if (tmpMInt == 10) {
                                result.put("树种组成", "10" + tmpStr01);
                            } else {
                                result.put("树种组成", tmpD001 >= 9.5d ? "10" + tmpStr01 : String.valueOf(String.valueOf(tmpMInt)) + tmpStr01 + String.valueOf(10 - tmpMInt) + tmpStr03);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static boolean UpdateXiaoBanYinZiDataByYangdiResult(String yangdiName, HashMap<String, String> xiaoBanYinZiData, BasicValue outputMsg) {
        String tmpFIDName2;
        boolean result = false;
        StringBuilder tmpSB = new StringBuilder();
        boolean tmpBool = true;
        String[] tmpStrings = GetXiaoBanLayerAndIDByYangdi(yangdiName);
        if (tmpStrings != null && tmpStrings.length > 1) {
            GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByID(tmpStrings[0]);
            FeatureLayer tmpFeatureLayer = PubVar._PubCommand.m_ProjectDB.getFeatureLayerByID(tmpStrings[0]);
            if (tmpGeoLayer != null) {
                DataSet tmpDataSet = tmpGeoLayer.getDataset();
                HashMap<String, Object> tmpUpdateFVHashMap = new HashMap<>();
                for (Map.Entry<String, String> tmpEntry : xiaoBanYinZiData.entrySet()) {
                    boolean tmpBool01 = true;
                    String tmpFIDName = tmpEntry.getKey();
                    String tmpFValueString = tmpEntry.getValue();
                    if (m_XiaoBanLayerMustFieldsName.containsKey(tmpFIDName)) {
                        tmpFIDName2 = m_XiaoBanLayerMustFieldsName.get(tmpFIDName);
                        String tmpFIDName3 = tmpFeatureLayer.GetDataFieldByFieldName(tmpFIDName2);
                        if (tmpFIDName3.length() > 0) {
                            tmpUpdateFVHashMap.put(tmpFIDName3, tmpFValueString);
                            tmpBool01 = false;
                        }
                    } else {
                        tmpFIDName2 = tmpFIDName;
                    }
                    if (tmpBool01) {
                        if (tmpSB.length() > 0) {
                            tmpSB.append("\r\n");
                        }
                        tmpSB.append("[" + tmpFIDName + "]的值[" + tmpFValueString + "]更新失败,关联字段[" + tmpFIDName2 + "]不存在.");
                    }
                }
                if (tmpUpdateFVHashMap.size() > 0) {
                    tmpDataSet.UpdateFieldsValue(tmpStrings[1], tmpUpdateFVHashMap);
                    tmpBool = false;
                    if (tmpSB.length() > 0) {
                        tmpSB.append("\r\n\r\n");
                    }
                    tmpSB.append("更新图层【" + tmpGeoLayer.getLayerName() + "】中的数据ID【" + tmpStrings[1] + "】相关属性值成功!");
                    result = true;
                } else {
                    tmpBool = false;
                    tmpSB.append("更新图层【" + tmpGeoLayer.getLayerName() + "】中的数据ID【" + tmpStrings[1] + "】相关属性值成功!");
                    result = false;
                }
            }
        }
        if (tmpBool) {
            tmpSB.append("未找到样地【" + yangdiName + "】对应的小班数据.");
        }
        if (outputMsg != null) {
            outputMsg.setValue(tmpSB.toString());
        }
        return result;
    }

    public static boolean CalXiaoBanYinZiAndUpdateData(Context context, final String yangDiName, boolean needShowAlertDialog) {
        try {
            final HashMap<String, String> tmpHashMap = CalXiaoBanYinZiByYangDi(yangDiName);
            if (tmpHashMap != null && tmpHashMap.size() > 0) {
                StringBuilder tmpStringBuilder = new StringBuilder();
                for (Map.Entry<String, String> tmpEntry : tmpHashMap.entrySet()) {
                    tmpStringBuilder.append("\r\n");
                    tmpStringBuilder.append(String.valueOf(tmpEntry.getKey()) + ":" + tmpEntry.getValue());
                }
                if (needShowAlertDialog) {
                    Common.ShowYesNoDialog(context, "已经计算该样地所在小班的小班因子.是否保存至对应的小班数据中?\r\n" + tmpStringBuilder.toString(), new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.CommonSetting.1
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command, Object pObject) {
                            if (command.equals("YES")) {
                                BasicValue tmpOutMsg = new BasicValue();
                                CommonSetting.UpdateXiaoBanYinZiDataByYangdiResult(yangDiName, tmpHashMap, tmpOutMsg);
                                String tmpString = tmpOutMsg.getString();
                                if (tmpString.length() > 0) {
                                    Common.ShowDialog(tmpString);
                                }
                            }
                        }
                    });
                } else {
                    UpdateXiaoBanYinZiDataByYangdiResult(yangDiName, tmpHashMap, new BasicValue());
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static HashMap<String, String> GetXiaoBanLayerMustFieldsName() {
        return m_XiaoBanLayerMustFieldsName;
    }

    public static List<String> GetXiaoBanLayerMustFieldsNameList() {
        return m_XiaoBanLayerMustFieldsNameList;
    }

    public static double CalXuJiValue(String shuZhongName, double xiongJing, double shuGao) {
        if (shuZhongName.equals("杉木")) {
            return CalCaiJi_ShanMu(xiongJing, shuGao);
        }
        if (shuZhongName.equals("马尾松")) {
            return CalCaiJi_MaWeiSong(xiongJing, shuGao);
        }
        if (shuZhongName.equals("柏木")) {
            return CalCaiJi_BoMu(xiongJing, shuGao);
        }
        if (shuZhongName.equals("云南松")) {
            return CalCaiJi_YunNanSong(xiongJing, shuGao);
        }
        if (shuZhongName.equals("华山松")) {
            return CalCaiJi_HuaShanSong(xiongJing, shuGao);
        }
        if (shuZhongName.equals("软阔")) {
            return CalCaiJi_RuanKuo(xiongJing, shuGao);
        }
        if (shuZhongName.equals("硬阔")) {
            return CalCaiJi_YingKuo(xiongJing, shuGao);
        }
        return 0.0d;
    }

    public static HashMap<String, String> GetYangMuInfo(String ID) {
        SQLiteReader tmpSQLiteReader;
        try {
            SQLiteDBHelper tmpSqLiteDBHelper = GetSQLiteDBHelper();
            if (tmpSqLiteDBHelper == null || (tmpSQLiteReader = tmpSqLiteDBHelper.Query("Select YangDiName,ShuZhong,ShuZhongZu,XiongJing,PJMShuGao,Remark,X,Y From T_YangDiData Where ID=" + ID)) == null || !tmpSQLiteReader.Read()) {
                return null;
            }
            HashMap<String, String> result = new HashMap<>();
            try {
                result.put("ID", ID);
                result.put("YangDiName", tmpSQLiteReader.GetString(0));
                result.put("ShuZhong", tmpSQLiteReader.GetString(1));
                result.put("ShuZhongZu", tmpSQLiteReader.GetString(2));
                result.put("XiongJing", String.valueOf(tmpSQLiteReader.GetDouble(3)));
                result.put("PJMShuGao", String.valueOf(tmpSQLiteReader.GetDouble(4)));
                result.put("Remark", String.valueOf(tmpSQLiteReader.GetString(5)));
                result.put("X", String.valueOf(tmpSQLiteReader.GetDouble(6)));
                result.put("Y", String.valueOf(tmpSQLiteReader.GetDouble(7)));
                return result;
            } catch (Exception e) {
                return result;
            }
        } catch (Exception e2) {
            return null;
        }
    }
}
