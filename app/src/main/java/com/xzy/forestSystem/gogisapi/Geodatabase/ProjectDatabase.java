package  com.xzy.forestSystem.gogisapi.Geodatabase;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Config.ProjectConfigDB;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.Track.TrackManage;
import  com.xzy.forestSystem.gogisapi.Workspace.LayerRenderWorkspace;
import  com.xzy.forestSystem.gogisapi.Workspace.LayerWorkspace;
import  com.xzy.forestSystem.gogisapi.Workspace.ProjectWorkspace;
import  com.xzy.forestSystem.gogisapi.Workspace.RasterLayerWorkspace;
import  com.xzy.forestSystem.gogisapi.Workspace.VectorLayerWorkspace;
import java.io.File;
import java.util.Date;
import java.util.HashMap;

public class ProjectDatabase {
    public String ProjectPath;
    private boolean m_AlwaysOpenProject;
    private VectorLayerWorkspace m_BKVLayerManage;
    private LayerWorkspace m_LayerManage;
    private LayerRenderWorkspace m_LayerRenderManage;
    private String m_Password;
    private ProjectConfigDB m_ProjectConfigDB;
    private ProjectWorkspace m_ProjectManage;
    private RasterLayerWorkspace m_RasterLayerManage;
    private SQLiteDBHelper m_SQLiteDatabase;
    private TrackManage m_TrackManage;
    private String m_projectName;

    public ProjectDatabase() {
        this.m_AlwaysOpenProject = false;
        this.m_BKVLayerManage = null;
        this.m_LayerManage = null;
        this.m_RasterLayerManage = null;
        this.m_LayerRenderManage = null;
        this.m_ProjectManage = null;
        this.m_ProjectConfigDB = null;
        this.m_SQLiteDatabase = null;
        this.m_TrackManage = null;
        this.ProjectPath = "";
        this.m_Password = "";
        this.m_projectName = "";
        this.m_TrackManage = new TrackManage();
    }

    public void CloseProject() {
        if (this.m_LayerManage != null) {
            this.m_LayerManage.Dispose();
        }
        this.m_LayerManage = null;
        if (this.m_BKVLayerManage != null) {
            this.m_BKVLayerManage.Dispose();
        }
        this.m_BKVLayerManage = null;
        if (this.m_RasterLayerManage != null) {
            this.m_RasterLayerManage.Dispose();
        }
        this.m_RasterLayerManage = null;
        this.m_LayerRenderManage = null;
        this.m_ProjectManage = null;
        if (this.m_SQLiteDatabase != null) {
            this.m_SQLiteDatabase.Close();
        }
        this.m_SQLiteDatabase = null;
        this.ProjectPath = "";
        this.m_Password = "";
    }

    public void SetPassword(String pwd) {
        this.m_Password = pwd;
    }

    public String getPassword() {
        return this.m_Password;
    }

    private void OpenDatabase(String paramString) {
        try {
            if (Common.CheckExistFile(paramString)) {
                this.ProjectPath = paramString;
                if (this.ProjectPath.contains(FileSelector_Dialog.sRoot)) {
                    this.ProjectPath = this.ProjectPath.substring(0, this.ProjectPath.lastIndexOf(FileSelector_Dialog.sRoot));
                }
                if (this.m_SQLiteDatabase != null) {
                    this.m_SQLiteDatabase.Close();
                }
                this.m_SQLiteDatabase = new SQLiteDBHelper();
                this.m_SQLiteDatabase.SetPassword(this.m_Password);
                this.m_SQLiteDatabase.setDatabaseName(paramString);
                String tempFolder = String.valueOf(this.ProjectPath) + "/Photo";
                if (!Common.CheckExistFile(tempFolder)) {
                    new File(tempFolder).mkdirs();
                }
                if (!GetSQLiteDatabase().CheckColumnExist("T_BKVectorLayer", "Fields")) {
                    GetSQLiteDatabase().ExecuteSQL("ALTER TABLE T_BKVectorLayer ADD Fields TEXT");
                }
                if (!GetSQLiteDatabase().CheckColumnExist("T_BKVectorLayer", "Security")) {
                    GetSQLiteDatabase().ExecuteSQL("ALTER TABLE T_BKVectorLayer ADD Security TEXT Default ''");
                }
                if (!GetSQLiteDatabase().CheckColumnExist("T_RasterLayer", "Security")) {
                    GetSQLiteDatabase().ExecuteSQL("ALTER TABLE T_RasterLayer ADD Security TEXT Default ''");
                }
                if (!GetSQLiteDatabase().CheckColumnExist("T_RasterLayer", "ConsiderTranslate")) {
                    GetSQLiteDatabase().ExecuteSQL("ALTER TABLE T_RasterLayer ADD ConsiderTranslate TEXT Default 'false'");
                }
                if (!GetSQLiteDatabase().CheckColumnExist("T_RasterLayer", "CoorSystemConfig")) {
                    GetSQLiteDatabase().ExecuteSQL("ALTER TABLE T_RasterLayer ADD CoorSystemConfig TEXT Default ''");
                }
                if (!GetSQLiteDatabase().CheckColumnExist("T_RasterLayer", "MapConfig")) {
                    GetSQLiteDatabase().ExecuteSQL("ALTER TABLE T_RasterLayer ADD MapConfig TEXT Default ''");
                }
                if (!GetSQLiteDatabase().CheckColumnExist("T_RasterLayer", "StartLevel")) {
                    GetSQLiteDatabase().ExecuteSQL("ALTER TABLE T_RasterLayer ADD StartLevel Integer Default 0");
                }
                if (!GetSQLiteDatabase().CheckColumnExist("T_RasterLayer", "BiasX")) {
                    GetSQLiteDatabase().ExecuteSQL("ALTER TABLE T_RasterLayer ADD BiasX Double Default 0");
                }
                if (!GetSQLiteDatabase().CheckColumnExist("T_RasterLayer", "BiasY")) {
                    GetSQLiteDatabase().ExecuteSQL("ALTER TABLE T_RasterLayer ADD BiasY Double Default 0");
                }
                if (!GetSQLiteDatabase().CheckColumnExist("T_Project", "BiasX")) {
                    GetSQLiteDatabase().ExecuteSQL("ALTER TABLE T_Project ADD BiasX TEXT Default ''");
                }
                if (!GetSQLiteDatabase().CheckColumnExist("T_Project", "BiasY")) {
                    GetSQLiteDatabase().ExecuteSQL("ALTER TABLE T_Project ADD BiasY TEXT Default ''");
                }
                if (!GetSQLiteDatabase().CheckColumnExist("T_Project", "BiasZ")) {
                    GetSQLiteDatabase().ExecuteSQL("ALTER TABLE T_Project ADD BiasZ TEXT Default ''");
                }
                GetSQLiteDatabase().ExecuteSQL("Create Table If Not Exists Navigator (ID integer primary key AutoIncrement, LineName varchar(255) NOT NULL, CheckTime varchar(255) NOT NULL,CheckTimeLong long Default 0, PointIndex integer Default 0, X Real Default 0,Y Real Default 0,Z Real Default 0,GeoX Real Default 0,GeoY Real Default 0, IsMark Integer Default 0,MarkTimeLong long Default 0, Remark Text Default '')");
                GetSQLiteDatabase().ExecuteSQL("Create Table If Not Exists NavigatorRecord (ID integer primary key AutoIncrement, LineName varchar(255) NOT NULL, UUID varchar(255) Default '', StartTimeLong long Default 0,EndTimeLong long Default 0,IsDone Integer Default 0, Remark Text Default '')");
            }
        } catch (Exception e) {
        }
    }

    public boolean AlwaysOpenProject() {
        return this.m_AlwaysOpenProject;
    }

    public boolean CreateProject(String projectName) {
        try {
            String tmpPrjsFolder = Common.GetProjectPath(projectName);
            this.m_projectName = projectName;
            if (Common.CheckExistFolder(tmpPrjsFolder) || !new File(tmpPrjsFolder).mkdirs()) {
                return false;
            }
            Common.CopyFile(String.valueOf(PubVar.m_SystemPath) + "/sysfile/Template.dbx", String.valueOf(tmpPrjsFolder) + "/TAData.dbx");
            Common.CopyFile(String.valueOf(PubVar.m_SystemPath) + "/sysfile/Project.dbx", String.valueOf(tmpPrjsFolder) + "/Project.dbx");
            OpenDatabase(String.valueOf(tmpPrjsFolder) + "/Project.dbx");
            String tempTimeStr = String.valueOf(new Date().getTime());
            PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().ExecuteSQL("Replace Into T_Project (ProjectName,CreateTime,ModifyTime) Values ('" + projectName + "','" + tempTimeStr + "','" + tempTimeStr + "')");
            return true;
        } catch (Exception ex) {
            Common.Log("错误", "CreateProject:" + ex.getMessage());
            return false;
        }
    }

    public VectorLayerWorkspace GetBKVectorLayerManage() {
        return this.m_BKVLayerManage;
    }

    public LayerWorkspace GetLayerManage() {
        return this.m_LayerManage;
    }

    public RasterLayerWorkspace GetRasterLayerManage() {
        return this.m_RasterLayerManage;
    }

    public LayerRenderWorkspace GetLayerRenderManage() {
        if (this.m_LayerRenderManage == null) {
            this.m_LayerRenderManage = new LayerRenderWorkspace();
        }
        return this.m_LayerRenderManage;
    }

    public ProjectWorkspace GetProjectManage() {
        return this.m_ProjectManage;
    }

    public TrackManage GetTrackManage() {
        return this.m_TrackManage;
    }

    public ProjectConfigDB GetProjectConfigDB() {
        return this.m_ProjectConfigDB;
    }

    public SQLiteDBHelper GetSQLiteDatabase() {
        return this.m_SQLiteDatabase;
    }

    public boolean OpenProject(String projectName) {
        return OpenProject(projectName, true);
    }

    public boolean OpenProject(String projectName, boolean paramBoolean) {
        try {
            this.m_projectName = projectName;
            String str = String.valueOf(Common.GetProjectPath(projectName)) + "/Project.dbx";
            if (!Common.CheckExistFile(str)) {
                return false;
            }
            OpenDatabase(str);
            if (this.m_ProjectManage == null) {
                this.m_ProjectManage = new ProjectWorkspace();
            }
            this.m_ProjectManage.SetBindProject(this);
            this.m_ProjectManage.SetProjectName(projectName);
            this.m_ProjectManage.LoadProjectInfo();
            if (this.m_LayerManage == null) {
                this.m_LayerManage = new LayerWorkspace();
            }
            this.m_LayerManage.SetBindProject(this);
            this.m_LayerManage.LoadLayer();
            if (this.m_BKVLayerManage == null) {
                this.m_BKVLayerManage = new VectorLayerWorkspace();
            }
            this.m_BKVLayerManage.SetBindProject(this);
            this.m_BKVLayerManage.LoadLayers();
            if (this.m_RasterLayerManage == null) {
                this.m_RasterLayerManage = new RasterLayerWorkspace();
            }
            this.m_RasterLayerManage.SetBindProject(this);
            this.m_RasterLayerManage.LoadLayers();
            this.m_AlwaysOpenProject = true;
            if (this.m_ProjectConfigDB == null) {
                this.m_ProjectConfigDB = new ProjectConfigDB();
            }
            this.m_ProjectConfigDB.SetBindDB(this.m_SQLiteDatabase);
            if (paramBoolean) {
                HashMap tmpHashMap = new HashMap();
                tmpHashMap.put("F2", projectName);
                tmpHashMap.put("F3", Common.GetSystemDate());
                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_BeforeOpenProject", (HashMap<String, String>) tmpHashMap);
            }
            return true;
        } catch (Exception ex) {
            Common.Log("错误", "OpenProject:" + ex.toString() + "-->" + ex.getMessage());
            return false;
        }
    }

    public FeatureLayer GetLayerInDataSource(String dataSourceName, String layerID) {
        if (PubVar.m_Workspace.GetDataSourceByEditing().getName().equals(dataSourceName)) {
            return GetLayerManage().GetLayerByID(layerID);
        }
        return GetBKVectorLayerManage().GetLayerByLayerID(layerID);
    }

    public FeatureLayer getFeatureLayerByID(String layerID) {
        FeatureLayer result = GetLayerManage().GetLayerByID(layerID);
        if (result == null) {
            return GetBKVectorLayerManage().GetLayerByLayerID(layerID);
        }
        return result;
    }
}
