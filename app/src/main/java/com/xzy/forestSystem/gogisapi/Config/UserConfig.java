package  com.xzy.forestSystem.gogisapi.Config;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XRasterFileLayer;
import  com.xzy.forestSystem.gogisapi.Carto.SelectTool;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.GPS.GPSLocationClass;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class UserConfig {
    private UserConfig_LayerTemplate m_LayerTemplate = null;
    private UserConfig_CoordinateSystem m_MyCoordinateSystem = null;
    private SQLiteDBHelper m_SQLiteDatabase = null;
    private UserParam m_UserParam = null;

    public UserConfig() {
        GetSQLiteDatabase().ExecuteSQL("Create Table If Not Exists T_Project (ID integer primary key AutoIncrement,ProjectName varchar(255) NOT NULL Unique,CreateTime varchar(50),ModifyTime varchar(50),F1 varchar(20),F2 varchar(20),F3 varchar(20),F4 varchar(20),F5 varchar(20),F6 varchar(20),F7 varchar(20),F8 varchar(20),F9 varchar(20),F10 varchar(20),Security TEXT )");
        if (!GetSQLiteDatabase().CheckColumnExist("T_Project", "Security")) {
            GetSQLiteDatabase().ExecuteSQL("ALTER TABLE T_Project ADD Security TEXT");
        }
        GetSQLiteDatabase().ExecuteSQL("Create Table If Not Exists Layer_Template (ID integer primary key AutoIncrement,LayerName varchar(255) NOT NULL Unique,CreateTime varchar(50),Type Text,Transparent Text,IfLabel Text, LabelField Text, LabelFont Text,LabelSplitChar Text, FieldList Text,LabelScaleMin Double, LabelScaleMax Double,VisibleScaleMin Double,VisibleScaleMax Double,Selectable Text, Editable Text,Snapable Text,RenderType Text,SimpleRender Text,UniqueValueField Text,UniqueValueList Text,UniqueSymbolList Text, UniqueDefaultSymbol Text,F1 varchar(20),F2 varchar(20),F3 varchar(20),F4 varchar(20),F5 varchar(20),F6 varchar(20),F7 varchar(20),F8 varchar(20),F9 varchar(20),F10 varchar(20))");
        GetSQLiteDatabase().ExecuteSQL("Create Table If Not Exists FenceData (ID integer primary key AutoIncrement,Name varchar(255) NOT NULL Unique,CreateTime varchar(20),Remark Text Default '',Data TEXT Default '' )");
    }

    private boolean CheckAndCreateTable(String paramString) {
        if (IsExistTable(paramString)) {
            return true;
        }
        ArrayList localArrayList = new ArrayList();
        localArrayList.add("CREATE TABLE If Not Exists " + paramString + " (");
        localArrayList.add("ID integer primary key autoincrement  not null default (0),");
        if (paramString.equals("T_LayerTemplate")) {
            localArrayList.add("name text,");
            localArrayList.add("createtime text,");
            localArrayList.add("layerlist binary");
        }
        if (paramString.equals("T_MyCoordinateSystem")) {
            localArrayList.add("Name text,");
            localArrayList.add("CreateTime text,");
            localArrayList.add("Para binary");
        }
        if (paramString.equals("T_UserParam")) {
        }
        for (int i = 1; i <= 49; i++) {
            localArrayList.add("F" + i + " text,");
        }
        localArrayList.add("F50 text");
        localArrayList.add(")");
        return GetSQLiteDatabase().ExecuteSQL(Common.CombineStrings("\r\n", localArrayList));
    }

    public SQLiteDBHelper GetSQLiteDatabase() {
        if (this.m_SQLiteDatabase == null) {
            OpenDatabase();
        }
        return this.m_SQLiteDatabase;
    }

    public boolean IsExistTable(String tableName) {
        boolean result = false;
        SQLiteReader localSQLiteDataReader = GetSQLiteDatabase().Query("SELECT COUNT(*) as count FROM sqlite_master WHERE type='table' and name= '" + tableName + "'");
        if (localSQLiteDataReader != null && localSQLiteDataReader.Read()) {
            if (Integer.parseInt(localSQLiteDataReader.GetString("count")) > 0) {
                result = true;
            }
            localSQLiteDataReader.Close();
        }
        return result;
    }

    private void OpenDatabase() {
        String str = String.valueOf(PubVar.m_SystemPath) + "/sysfile/UserConfig.dbx";
        if (Common.CheckExistFile(str)) {
            this.m_SQLiteDatabase = new SQLiteDBHelper();
            this.m_SQLiteDatabase.setDatabaseName(str);
        }
    }

    public UserConfig_LayerTemplate GetLayerTemplate() {
        if (this.m_LayerTemplate == null && CheckAndCreateTable("T_LayerTemplate")) {
            this.m_LayerTemplate = new UserConfig_LayerTemplate();
            this.m_LayerTemplate.SetBindDB(GetSQLiteDatabase());
            if (this.m_LayerTemplate.ReadTemplateList("系统默认图层模板").size() == 0) {
                this.m_LayerTemplate.CreateDefaultLayerTemplate();
            }
        }
        return this.m_LayerTemplate;
    }

    public UserConfig_CoordinateSystem GetMyCoodinateSystem() {
        if (this.m_MyCoordinateSystem == null && CheckAndCreateTable("T_MyCoordinateSystem")) {
            this.m_MyCoordinateSystem = new UserConfig_CoordinateSystem();
            this.m_MyCoordinateSystem.SetBindDB(GetSQLiteDatabase());
        }
        return this.m_MyCoordinateSystem;
    }

    public UserParam GetUserParam() {
        if (this.m_UserParam == null && CheckAndCreateTable("T_UserParam")) {
            this.m_UserParam = new UserParam();
            this.m_UserParam.SetBindDB(GetSQLiteDatabase());
        }
        return this.m_UserParam;
    }

    public void LoadSystemConfig() {
        try {
            HashMap<String, String> tempHashMap = GetUserParam().GetUserPara("Tag_System_Map_BgColor");
            if (tempHashMap != null) {
                String tempStr = tempHashMap.get("F2");
                if (tempStr != null && !tempStr.equals("")) {
                    PubVar.MapBgColor = Integer.parseInt(tempStr);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_Map_BgColor", Integer.valueOf(PubVar.MapBgColor));
            }
            HashMap<String, String> tempGPSHashMap = GetUserParam().GetUserPara("Tag_System_GPS");
            if (tempGPSHashMap == null) {
                HashMap<String, String> tempGPSHashMap2 = new HashMap<>();
                String tempValue = String.valueOf(GPSLocationClass.CoordShowType) + ";false;";
                boolean[] gpsStatusInfoBool = new boolean[6];
                gpsStatusInfoBool[0] = true;
                for (int i = 0; i < gpsStatusInfoBool.length; i++) {
                    tempValue = String.valueOf(tempValue) + gpsStatusInfoBool[i] + ",";
                }
                tempGPSHashMap2.put("F2", String.valueOf(tempValue) + ";");
                tempGPSHashMap2.put("F3", "0");
                GetUserParam().SaveUserPara("Tag_System_GPS", tempGPSHashMap2);
            } else {
                String tempStr2 = tempGPSHashMap.get("F3");
                if (tempStr2 != null && !tempStr2.equals("")) {
                    PubVar.GPS_Device_Type = Integer.parseInt(tempStr2);
                }
            }
            HashMap<String, String> tempHash01 = GetUserParam().GetUserPara("Tag_System_ScaleBar_ShowType");
            if (tempHash01 != null) {
                String tempStr3 = tempHash01.get("F2");
                if (tempStr3 != null && !tempStr3.equals("")) {
                    PubVar.ScaleBar_ShowType = Integer.parseInt(tempStr3);
                }
            } else {
                HashMap<String, String> tempHash012 = new HashMap<>();
                tempHash012.put("F2", String.valueOf(PubVar.ScaleBar_ShowType));
                GetUserParam().SaveUserPara("Tag_System_ScaleBar_ShowType", tempHash012);
            }
            HashMap<String, String> tempHash02 = GetUserParam().GetUserPara("Tag_System_GPS_Gather_IntervalTime");
            if (tempHash02 != null) {
                String tempStr4 = tempHash02.get("F2");
                if (tempStr4 != null && !tempStr4.equals("")) {
                    PubVar.GPSGatherIntervalTime = Long.parseLong(tempStr4);
                }
            } else {
                HashMap<String, String> tempHash022 = new HashMap<>();
                tempHash022.put("F2", String.valueOf(PubVar.GPSGatherIntervalTime));
                GetUserParam().SaveUserPara("Tag_System_GPS_Gather_IntervalTime", tempHash022);
            }
            HashMap<String, String> tempHash03 = GetUserParam().GetUserPara("Tag_System_GPS_Gather_IntervalDistance");
            if (tempHash03 != null) {
                String tempStr5 = tempHash03.get("F2");
                if (tempStr5 != null && !tempStr5.equals("")) {
                    PubVar.GPSGatherIntervalDistance = Double.parseDouble(tempStr5);
                }
            } else {
                HashMap<String, String> tempHash032 = new HashMap<>();
                tempHash032.put("F2", String.valueOf(PubVar.GPSGatherIntervalDistance));
                GetUserParam().SaveUserPara("Tag_System_GPS_Gather_IntervalDistance", tempHash032);
            }
            HashMap<String, String> tempHashMap2 = GetUserParam().GetUserPara("Tag_System_GPS_Gather_Accuracy");
            if (tempHashMap2 != null) {
                String tempStr6 = tempHashMap2.get("F2");
                if (tempStr6 != null && !tempStr6.equals("")) {
                    PubVar.GPS_Gather_Accuracy = Double.parseDouble(tempStr6);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_GPS_Gather_Accuracy", Double.valueOf(PubVar.GPS_Gather_Accuracy));
            }
            HashMap<String, String> tempHash04 = GetUserParam().GetUserPara("Tag_System_GPS_Show_CoorSystem_Type");
            if (tempHash04 != null) {
                String tempStr7 = tempHash04.get("F2");
                if (tempStr7 != null && !tempStr7.equals("")) {
                    PubVar.GPS_Show_CoorSystem_Type = Integer.parseInt(tempStr7);
                }
            } else {
                HashMap<String, String> tempHash042 = new HashMap<>();
                tempHash042.put("F2", String.valueOf(PubVar.GPS_Show_CoorSystem_Type));
                GetUserParam().SaveUserPara("Tag_System_GPS_Show_CoorSystem_Type", tempHash042);
            }
            HashMap<String, String> tempHash05 = GetUserParam().GetUserPara("Tag_System_GPS_Show_Format_Type");
            if (tempHash05 != null) {
                String tempStr8 = tempHash05.get("F2");
                if (tempStr8 != null && !tempStr8.equals("")) {
                    PubVar.GPS_Show_Format_Type = Integer.parseInt(tempStr8);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_GPS_Show_Format_Type", Integer.valueOf(PubVar.GPS_Show_Format_Type));
            }
            HashMap<String, String> tempHash06 = GetUserParam().GetUserPara("Tag_System_GPS_Show_Elevation");
            if (tempHash06 != null) {
                String tempStr9 = tempHash06.get("F2");
                if (tempStr9 != null && !tempStr9.equals("")) {
                    PubVar.GPS_Show_Elevation = Boolean.parseBoolean(tempStr9);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_GPS_Show_Elevation", Boolean.valueOf(PubVar.GPS_Show_Elevation));
            }
            HashMap<String, String> tempHashMap3 = GetUserParam().GetUserPara("Tag_System_GPS_Show_Accuracy");
            if (tempHashMap3 != null) {
                String tempStr10 = tempHashMap3.get("F2");
                if (tempStr10 != null && !tempStr10.equals("")) {
                    PubVar.GPS_Show_Accuracy = Boolean.parseBoolean(tempStr10);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_GPS_Show_Accuracy", Boolean.valueOf(PubVar.GPS_Show_Accuracy));
            }
            HashMap<String, String> tempHashMap4 = GetUserParam().GetUserPara("Tag_System_GPS_Antenna_Height");
            if (tempHashMap4 != null) {
                String tempStr11 = tempHashMap4.get("F2");
                if (tempStr11 != null && !tempStr11.equals("")) {
                    PubVar.GPS_Antenna_Height = Double.parseDouble(tempStr11);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_GPS_Antenna_Height", Double.valueOf(PubVar.GPS_Antenna_Height));
            }
            HashMap<String, String> tempHashMap5 = GetUserParam().GetUserPara("Tag_System_Map_ScaleBar_ShowType");
            if (tempHashMap5 != null) {
                String tempStr12 = tempHashMap5.get("F2");
                if (tempStr12 != null && !tempStr12.equals("")) {
                    PubVar.ScaleBar_ShowType = Integer.parseInt(tempStr12);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_Map_ScaleBar_ShowType", Integer.valueOf(PubVar.ScaleBar_ShowType));
            }
            HashMap<String, String> tempHashMap6 = GetUserParam().GetUserPara("Tag_System_HeadTip_Visible");
            if (tempHashMap6 != null) {
                String tempStr13 = tempHashMap6.get("F2");
                if (tempStr13 != null && !tempStr13.equals("")) {
                    PubVar.HeadTip_Visible = Boolean.parseBoolean(tempStr13);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_HeadTip_Visible", Boolean.valueOf(PubVar.HeadTip_Visible));
            }
            HashMap<String, String> tempHashMap7 = GetUserParam().GetUserPara("Tag_System_GPS_Point_ShowType");
            if (tempHashMap7 != null) {
                String tempStr14 = tempHashMap7.get("F2");
                if (tempStr14 != null && !tempStr14.equals("")) {
                    PubVar.GPS_Point_ShowType = Integer.parseInt(tempStr14);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_GPS_Point_ShowType", Integer.valueOf(PubVar.GPS_Point_ShowType));
            }
            HashMap<String, String> tempHashMap8 = GetUserParam().GetUserPara("Tag_System_Track_Auto_Record");
            if (tempHashMap8 != null) {
                String tempStr15 = tempHashMap8.get("F2");
                if (tempStr15 != null && !tempStr15.equals("")) {
                    PubVar.Track_Auto_Record = Boolean.parseBoolean(tempStr15);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_Track_Auto_Record", Boolean.valueOf(PubVar.Track_Auto_Record));
            }
            HashMap<String, String> tempHashMap9 = GetUserParam().GetUserPara("Tag_System_Track_Record_Type");
            if (tempHashMap9 != null) {
                String tempStr16 = tempHashMap9.get("F2");
                if (tempStr16 != null && !tempStr16.equals("")) {
                    PubVar.Track_Record_Type = Integer.parseInt(tempStr16);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_Track_Record_Type", Integer.valueOf(PubVar.Track_Record_Type));
            }
            HashMap<String, String> tempHashMap10 = GetUserParam().GetUserPara("Tag_System_Track_Record_Param");
            if (tempHashMap10 != null) {
                String tempStr17 = tempHashMap10.get("F2");
                if (tempStr17 != null && !tempStr17.equals("")) {
                    PubVar.Track_Record_Param = Double.parseDouble(tempStr17);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_Track_Record_Param", Double.valueOf(PubVar.Track_Record_Param));
            }
            HashMap<String, String> tempHashMap11 = GetUserParam().GetUserPara("Tag_System_Track_Draw_Date");
            if (tempHashMap11 != null) {
                String tempStr18 = tempHashMap11.get("F2");
                if (tempStr18 != null && !tempStr18.equals("")) {
                    PubVar.Track_Draw_Date = Boolean.parseBoolean(tempStr18);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_Track_Draw_Date", Boolean.valueOf(PubVar.Track_Draw_Date));
            }
            HashMap<String, String> tempHashMap12 = GetUserParam().GetUserPara("Tag_System_Compass_Show");
            if (tempHashMap12 != null) {
                String tempStr19 = tempHashMap12.get("F2");
                if (tempStr19 != null && !tempStr19.equals("")) {
                    PubVar.Compass_Show = Boolean.parseBoolean(tempStr19);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_Compass_Show", Boolean.valueOf(PubVar.Compass_Show));
            }
            HashMap<String, String> tempHashMap13 = GetUserParam().GetUserPara("Tag_System_Toolbar_Gesture_Close");
            if (tempHashMap13 != null) {
                String tempStr20 = tempHashMap13.get("F2");
                if (tempStr20 != null && !tempStr20.equals("")) {
                    PubVar.Toolbar_Gesture_Close = Boolean.parseBoolean(tempStr20);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_Toolbar_Gesture_Close", Boolean.valueOf(PubVar.Toolbar_Gesture_Close));
            }
            HashMap<String, String> tempHashMap14 = GetUserParam().GetUserPara("Tag_System_Toolbar_AutoResize");
            if (tempHashMap14 != null) {
                String tempStr21 = tempHashMap14.get("F2");
                if (tempStr21 != null && !tempStr21.equals("")) {
                    PubVar.Toolbar_AutoResize = Boolean.parseBoolean(tempStr21);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_Toolbar_AutoResize", Boolean.valueOf(PubVar.Toolbar_AutoResize));
            }
            HashMap<String, String> tempHashMap15 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_GPS_Show_CoordMultiLine");
            if (tempHashMap15 != null) {
                String tempStr22 = tempHashMap15.get("F2");
                if (tempStr22 != null && !tempStr22.equals("")) {
                    PubVar.GPS_Show_CoordMultiLine = Boolean.parseBoolean(tempStr22);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_GPS_Show_CoordMultiLine", Boolean.valueOf(PubVar.GPS_Show_CoordMultiLine));
            }
            HashMap<String, String> tempHashMap16 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_ScaleBar_Visible");
            if (tempHashMap16 != null) {
                String tempStr23 = tempHashMap16.get("F2");
                if (tempStr23 != null && !tempStr23.equals("")) {
                    PubVar.ScaleBar_Visible = Boolean.parseBoolean(tempStr23);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_ScaleBar_Visible", Boolean.valueOf(PubVar.ScaleBar_Visible));
            }
            HashMap<String, String> tempHashMap17 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_MessageDialogAllowShow");
            if (tempHashMap17 != null) {
                String tempStr24 = tempHashMap17.get("F2");
                if (tempStr24 != null && !tempStr24.equals("")) {
                    PubVar.MessageDialogAllowShow = Boolean.parseBoolean(tempStr24);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_MessageDialogAllowShow", Boolean.valueOf(PubVar.MessageDialogAllowShow));
            }
            HashMap<String, String> tempHashMap18 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_MessageMaxCount");
            if (tempHashMap18 != null) {
                String tempStr25 = tempHashMap18.get("F2");
                if (tempStr25 != null && !tempStr25.equals("")) {
                    PubVar.MessageMaxCount = Integer.parseInt(tempStr25);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_MessageMaxCount", Integer.valueOf(PubVar.MessageMaxCount));
            }
            HashMap<String, String> tempHashMap19 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_MessagePanelWidth");
            if (tempHashMap19 != null) {
                String tempStr26 = tempHashMap19.get("F2");
                if (tempStr26 != null && !tempStr26.equals("")) {
                    PubVar.MessagePanelWidth = Integer.parseInt(tempStr26);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_MessagePanelWidth", Integer.valueOf(PubVar.MessagePanelWidth));
            }
            HashMap<String, String> tempHashMap20 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_MessagePanelHeight");
            if (tempHashMap20 != null) {
                String tempStr27 = tempHashMap20.get("F2");
                if (tempStr27 != null && !tempStr27.equals("")) {
                    PubVar.MessagePanelHeight = Integer.parseInt(tempStr27);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_MessagePanelHeight", Integer.valueOf(PubVar.MessagePanelHeight));
            }
            HashMap<String, String> tempHashMap21 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_ChildMapWidth");
            if (tempHashMap21 != null) {
                String tempStr28 = tempHashMap21.get("F2");
                if (tempStr28 != null && !tempStr28.equals("")) {
                    PubVar.ChildMapWidth = Integer.parseInt(tempStr28);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_ChildMapWidth", Integer.valueOf(PubVar.ChildMapWidth));
            }
            HashMap<String, String> tempHashMap22 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_ChildMapHeight");
            if (tempHashMap22 != null) {
                String tempStr29 = tempHashMap22.get("F2");
                if (tempStr29 != null && !tempStr29.equals("")) {
                    PubVar.ChildMapHeight = Integer.parseInt(tempStr29);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_ChildMapHeight", Integer.valueOf(PubVar.ChildMapHeight));
            }
            HashMap<String, String> tempHashMap23 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_Edited_ShowAttribute_Bool");
            if (tempHashMap23 != null) {
                String tempStr30 = tempHashMap23.get("F2");
                if (tempStr30 != null && !tempStr30.equals("")) {
                    PubVar.Edited_ShowAttribute_Bool = Boolean.parseBoolean(tempStr30);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_Edited_ShowAttribute_Bool", Boolean.valueOf(PubVar.Edited_ShowAttribute_Bool));
            }
            HashMap<String, String> tempHashMap24 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_AreaUnitType");
            if (tempHashMap24 != null) {
                String tempStr31 = tempHashMap24.get("F2");
                if (tempStr31 != null && !tempStr31.equals("")) {
                    PubVar.AreaUnitType = Integer.parseInt(tempStr31);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_AreaUnitType", Integer.valueOf(PubVar.AreaUnitType));
            }
            HashMap<String, String> tempHashMap25 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_Edited_AllowEditSnap");
            if (tempHashMap25 != null) {
                String tempStr32 = tempHashMap25.get("F2");
                if (tempStr32 != null && !tempStr32.equals("")) {
                    PubVar.AllowEditSnap = Boolean.parseBoolean(tempStr32);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_Edited_AllowEditSnap", Boolean.valueOf(PubVar.AllowEditSnap));
            }
            HashMap<String, String> tempHashMap26 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_Edited_SnapDistance");
            if (tempHashMap26 != null) {
                String tempStr33 = tempHashMap26.get("F2");
                if (tempStr33 != null && !tempStr33.equals("")) {
                    PubVar.SnapDistance = Double.parseDouble(tempStr33);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_Edited_SnapDistance", Double.valueOf(PubVar.SnapDistance));
            }
            HashMap<String, String> tempHashMap27 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_DataSet_Safety");
            if (tempHashMap27 != null) {
                String tempStr34 = tempHashMap27.get("F2");
                if (tempStr34 != null && !tempStr34.equals("")) {
                    PubVar.DataSetQuerySafety = Boolean.parseBoolean(tempStr34);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_DataSet_Safety", Boolean.valueOf(PubVar.DataSetQuerySafety));
            }
            HashMap<String, String> tempHashMap28 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_GPS_Gather_PointAuto");
            if (tempHashMap28 != null) {
                String tempStr35 = tempHashMap28.get("F2");
                if (tempStr35 != null && !tempStr35.equals("")) {
                    PubVar.GPS_Gather_PointAuto = Boolean.parseBoolean(tempStr35);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_GPS_Gather_PointAuto", Boolean.valueOf(PubVar.GPS_Gather_PointAuto));
            }
            HashMap<String, String> tempHashMap29 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_LayersContentForm_Width");
            if (tempHashMap29 != null) {
                String tempStr36 = tempHashMap29.get("F2");
                if (tempStr36 != null && !tempStr36.equals("")) {
                    PubVar.LayersContentWidth = Integer.parseInt(tempStr36);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_LayersContentForm_Width", Integer.valueOf(PubVar.LayersContentWidth));
            }
            HashMap<String, String> tempHashMap30 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_LayersContentForm_Height");
            if (tempHashMap30 != null) {
                String tempStr37 = tempHashMap30.get("F2");
                if (tempStr37 != null && !tempStr37.equals("")) {
                    PubVar.LayersContentHeight = Integer.parseInt(tempStr37);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_LayersContentForm_Height", Integer.valueOf(PubVar.LayersContentHeight));
            }
            if (PubVar.LayersContentWidth >= PubVar.ScreenWidth) {
                PubVar.LayersContentWidth = (int) (((double) PubVar.ScreenWidth) * 0.5d);
            }
            if (PubVar.LayersContentHeight >= PubVar.ScreenHeight) {
                PubVar.LayersContentHeight = (int) (((double) PubVar.ScreenHeight) * 0.3d);
            }
            HashMap<String, String> tempHashMap31 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_LayersContentForm_Alpha");
            if (tempHashMap31 != null) {
                String tempStr38 = tempHashMap31.get("F2");
                if (tempStr38 != null && !tempStr38.equals("")) {
                    PubVar.LayersContentAlpha = Integer.parseInt(tempStr38);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_LayersContentForm_Alpha", Integer.valueOf(PubVar.LayersContentAlpha));
            }
            HashMap<String, String> tempHashMap32 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_HostIP");
            if (tempHashMap32 != null) {
                String tempStr39 = tempHashMap32.get("F2");
                if (tempStr39 != null && !tempStr39.equals("")) {
                    PubVar.HostIP = tempStr39;
                }
            } else {
                GetUserParam().SaveUserPara("Tag_HostIP", PubVar.HostIP);
            }
            HashMap<String, String> tempHashMap33 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_HostPort");
            if (tempHashMap33 != null) {
                String tempStr40 = tempHashMap33.get("F2");
                if (tempStr40 != null && !tempStr40.equals("")) {
                    PubVar.HostPort = Integer.parseInt(tempStr40);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_HostPort", Integer.valueOf(PubVar.HostPort));
            }
            HashMap<String, String> tempHashMap34 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_HostUserName");
            if (tempHashMap34 != null) {
                String tempStr41 = tempHashMap34.get("F2");
                if (tempStr41 != null && !tempStr41.equals("")) {
                    PubVar.HostUserName = tempStr41;
                }
            } else {
                GetUserParam().SaveUserPara("Tag_HostUserName", PubVar.HostUserName);
            }
            HashMap<String, String> tempHashMap35 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_HostUserPwd");
            if (tempHashMap35 != null) {
                String tempStr42 = tempHashMap35.get("F2");
                if (tempStr42 != null && !tempStr42.equals("")) {
                    PubVar.HostUserPwd = tempStr42;
                }
            } else {
                GetUserParam().SaveUserPara("Tag_HostUserPwd", PubVar.HostUserPwd);
            }
            HashMap<String, String> tempHashMap36 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_SCREEN_ORIENTATION");
            if (tempHashMap36 != null) {
                String tempStr43 = tempHashMap36.get("F2");
                if (tempStr43 != null && !tempStr43.equals("")) {
                    PubVar.ScreenIsLand = Boolean.parseBoolean(tempStr43);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_SCREEN_ORIENTATION", Boolean.valueOf(PubVar.ScreenIsLand));
            }
            HashMap<String, String> tempHashMap37 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_SCREEN_FULL");
            if (tempHashMap37 != null) {
                String tempStr44 = tempHashMap37.get("F2");
                if (tempStr44 != null && !tempStr44.equals("")) {
                    PubVar.ScreenIsFull = Boolean.parseBoolean(tempStr44);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_SCREEN_FULL", Boolean.valueOf(PubVar.ScreenIsFull));
            }
            HashMap<String, String> tempHashMap38 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_RasterInHigh");
            if (tempHashMap38 != null) {
                String tempStr45 = tempHashMap38.get("F2");
                if (tempStr45 != null && !tempStr45.equals("")) {
                    XBaseTilesLayer.InHighLevel = Integer.parseInt(tempStr45);
                    XRasterFileLayer.InHighLevel = Integer.parseInt(tempStr45);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_RasterInHigh", Integer.valueOf(XBaseTilesLayer.InHighLevel));
            }
            HashMap<String, String> tempHashMap39 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_Compass_ShowOnMap");
            if (tempHashMap39 != null) {
                String tempStr46 = tempHashMap39.get("F2");
                if (tempStr46 != null && !tempStr46.equals("")) {
                    PubVar.m_MapCompassVisible = Boolean.parseBoolean(tempStr46);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_Compass_ShowOnMap", Boolean.valueOf(PubVar.m_MapCompassVisible));
            }
            HashMap<String, String> tempHashMap40 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_Draw_Polygon_ShowCurrent");
            if (tempHashMap40 != null) {
                String tempStr47 = tempHashMap40.get("F2");
                if (tempStr47 != null && !tempStr47.equals("")) {
                    PubVar.Draw_Polygon_ShowCurrent = Boolean.parseBoolean(tempStr47);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_Draw_Polygon_ShowCurrent", Boolean.valueOf(PubVar.Draw_Polygon_ShowCurrent));
            }
            HashMap<String, String> tempHashMap41 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_Select_Click_Bias");
            if (tempHashMap41 != null) {
                String tempStr48 = tempHashMap41.get("F2");
                if (tempStr48 != null && !tempStr48.equals("")) {
                    SelectTool.SELECT_CLICK_BIAS = Double.parseDouble(tempStr48) * ((double) PubVar.ScaledDensity);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_Select_Click_Bias", Integer.valueOf((int) (SelectTool.SELECT_CLICK_BIAS / ((double) PubVar.ScaledDensity))));
            }
            HashMap<String, String> tempHashMap42 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_MediaInfo_Input_Bool");
            if (tempHashMap42 != null) {
                String tempStr49 = tempHashMap42.get("F2");
                if (tempStr49 != null && !tempStr49.equals("")) {
                    PubVar.MediaInfo_Input_Bool = Boolean.parseBoolean(tempStr49);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_MediaInfo_Input_Bool", Boolean.valueOf(PubVar.MediaInfo_Input_Bool));
            }
            HashMap<String, String> tempHashMap43 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_Map_DisplayCache_Check");
            if (tempHashMap43 != null) {
                String tempStr50 = tempHashMap43.get("F2");
                if (tempStr50 != null && !tempStr50.equals("")) {
                    PubVar.Map_Display_Cache = Boolean.parseBoolean(tempStr50);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_Map_DisplayCache_Check", Boolean.valueOf(PubVar.Map_Display_Cache));
            }
            HashMap<String, String> tempHashMap44 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_Edited_Clip_EPSILON");
            if (tempHashMap44 != null) {
                String tempStr51 = tempHashMap44.get("F2");
                if (tempStr51 != null && !tempStr51.equals("")) {
                    PubVar.Clip_EPSILON = Double.parseDouble(tempStr51);
                }
                if (PubVar.Clip_EPSILON <= 0.0d) {
                    PubVar.Clip_EPSILON = 1.0E-4d;
                }
            } else {
                GetUserParam().SaveUserPara("Tag_Edited_Clip_EPSILON", Double.valueOf(PubVar.Clip_EPSILON));
            }
            HashMap<String, String> tempHashMap45 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_Module_SenLinDuCha");
            if (tempHashMap45 != null) {
                String tempStr52 = tempHashMap45.get("F2");
                if (tempStr52 != null && !tempStr52.equals("")) {
                    PubVar.Module_SenLinDuCha = Boolean.parseBoolean(tempStr52);
                }
            } else {
                GetUserParam().SaveUserPara("Tag_System_Module_SenLinDuCha", Boolean.valueOf(PubVar.Module_SenLinDuCha));
            }
            HashMap<String, String> tempHashMap46 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_Module_YangDiDiaoCha");
            if (tempHashMap46 != null) {
                String tempStr53 = tempHashMap46.get("F2");
                if (!(tempStr53 == null || tempStr53.equals(""))) {
                    PubVar.Module_YangdiDiaoCha = Boolean.parseBoolean(tempStr53);
                    return;
                }
                return;
            }
            GetUserParam().SaveUserPara("Tag_System_Module_YangDiDiaoCha", Boolean.valueOf(PubVar.Module_YangdiDiaoCha));
        } catch (Exception e) {
        }
    }

    public List<String> GetLayerModulesName() {
        List<String> result = new ArrayList<>();
        SQLiteReader tmpReader = GetSQLiteDatabase().Query("Select Distinct LayerName From Layer_Template");
        if (tmpReader != null) {
            while (tmpReader.Read()) {
                result.add(tmpReader.GetString(0));
            }
            tmpReader.Close();
        }
        return result;
    }

    public boolean IsExistLayerModule(String layerName) {
        SQLiteReader tmpReader = GetSQLiteDatabase().Query("Select LayerName From Layer_Template Where LayerName='" + layerName + "'");
        if (tmpReader == null || !tmpReader.Read()) {
            return false;
        }
        return true;
    }

    public FeatureLayer GetFeatureLayerModuleByName(String layerName) {
        SQLiteReader tmpReader = GetSQLiteDatabase().Query("Select * From Layer_Template Where LayerName='" + layerName + "'");
        if (tmpReader != null) {
            if (tmpReader.Read()) {
                FeatureLayer layer = new FeatureLayer();
                layer.SetLayerName(tmpReader.GetString("LayerName"));
                layer.SetLayerTypeName(tmpReader.GetString("Type"));
                try {
                    String tempStr = tmpReader.GetString("Transparent");
                    if (tempStr != null && !tempStr.equals("")) {
                        layer.SetTransparent(Integer.parseInt(tempStr));
                    }
                    String tempStr2 = tmpReader.GetString("IfLabel");
                    if (tempStr2 != null && !tempStr2.equals("")) {
                        layer.SetIfLabel(Boolean.parseBoolean(tempStr2));
                    }
                } catch (Exception e) {
                }
                try {
                    layer.SetFieldList(tmpReader.GetString("FieldList"));
                    layer.SetLabelDataField(tmpReader.GetString("LabelField"));
                    layer.SetLabelFont(tmpReader.GetString("LabelFont"));
                    layer.SetLabelSplitChar(tmpReader.GetString("LabelSplitChar"));
                } catch (Exception e2) {
                }
                try {
                    String tempStr3 = tmpReader.GetString("LabelScaleMin");
                    if (tempStr3 != null && !tempStr3.equals("")) {
                        layer.SetLabelScaleMin(Double.parseDouble(tempStr3));
                    }
                    String tempStr4 = tmpReader.GetString("LabelScaleMax");
                    if (tempStr4 != null && !tempStr4.equals("")) {
                        layer.SetLabelScaleMin(Double.parseDouble(tempStr4));
                    }
                    String tempStr5 = tmpReader.GetString("VisibleScaleMin");
                    if (tempStr5 != null && !tempStr5.equals("")) {
                        layer.SetMinScale(Double.parseDouble(tempStr5));
                    }
                    String tempStr6 = tmpReader.GetString("VisibleScaleMax");
                    if (tempStr6 != null && !tempStr6.equals("")) {
                        layer.SetMaxScale(Double.parseDouble(tempStr6));
                    }
                } catch (Exception e3) {
                }
                try {
                    String tempStr7 = tmpReader.GetString("Selectable");
                    if (tempStr7 != null && !tempStr7.equals("")) {
                        layer.SetSelectable(Boolean.parseBoolean(tempStr7));
                    }
                    String tempStr8 = tmpReader.GetString("Editable");
                    if (tempStr8 != null && !tempStr8.equals("")) {
                        layer.SetEditable(Boolean.parseBoolean(tempStr8));
                    }
                    String tempStr9 = tmpReader.GetString("Snapable");
                    if (tempStr9 != null && !tempStr9.equals("")) {
                        layer.SetSnapable(Boolean.parseBoolean(tempStr9));
                    }
                    String tempStr10 = tmpReader.GetString("RenderType");
                    if (tempStr10 != null && !tempStr10.equals("")) {
                        layer.SetRenderType(Integer.parseInt(tempStr10));
                    }
                    layer.SetDefaultSymbol(tmpReader.GetString("SimpleRender"));
                } catch (Exception e4) {
                }
                tmpReader.Close();
                return layer;
            }
            tmpReader.Close();
        }
        return null;
    }

    public boolean SaveFeatureLayerModule(String layerModuleName, FeatureLayer layer) {
        try {
            return GetSQLiteDatabase().ExecuteSQL(String.format("insert into Layer_Template (LayerName,CreateTime,Type,Transparent,IfLabel,LabelFont,LabelSplitChar,FieldList,LabelScaleMin,LabelScaleMax,VisibleScaleMin,VisibleScaleMax,Selectable,Editable,Snapable,RenderType,LabelField) values ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s','%8$s',%9$s,%10$s,%11$s,%12$s,'%13$s','%14$s','%15$s','%16$s','%17$s')", layerModuleName, Common.dateFormat.format(new Date()), layer.GetLayerTypeName(), Integer.valueOf(layer.GetTransparet()), Boolean.valueOf(layer.GetIfLabel()), layer.GetLabelFont(), layer.getLabelSplitChar(), layer.GetFieldListStr(), Double.valueOf(layer.GetLabelScaleMin()), Double.valueOf(layer.GetLabelScaleMax()), Double.valueOf(layer.GetMinScale()), Double.valueOf(layer.GetMaxScale()), Boolean.valueOf(layer.GetSelectable()), Boolean.valueOf(layer.GetEditable()), Boolean.valueOf(layer.GetSnapable()), Integer.valueOf(layer.GetRenderType()), layer.GetLabelDataField()));
        } catch (Exception ex) {
            Common.Log("SaveFeatureLayerModule", "错误:" + ex.toString() + "-->" + ex.getMessage());
            return false;
        }
    }
}
