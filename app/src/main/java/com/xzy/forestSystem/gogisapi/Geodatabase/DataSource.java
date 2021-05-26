package  com.xzy.forestSystem.gogisapi.Geodatabase;

import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Edit.EGeometryStatus;
import  com.xzy.forestSystem.gogisapi.Encryption.DataAuthority;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.ExtraGeometry;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataSource {
    private DataAuthority _DataAuthority = null;
    private int _DataSourceType = 0;
    private List<DataSet> _Datasets = new ArrayList();
    public SQLiteDBHelper _EDatabase = null;
    private boolean _Editing = false;
    private String _Name = "";

    public DataSource(String datasourceName, String pwd, int datasourceType) {
        this._DataSourceType = datasourceType;
        setName(datasourceName);
        this._DataAuthority = new DataAuthority(datasourceType);
        if (this._DataAuthority.Initial(datasourceName, pwd, new BasicValue())) {
            Open();
        }
    }

    public void Dispose2() {
        try {
            for (DataSet tmpDataSet : this._Datasets) {
                if (tmpDataSet != null) {
                    tmpDataSet.Dispose2();
                }
            }
            this._EDatabase.Close();
            this._EDatabase = null;
        } catch (Exception e) {
        }
    }

    public boolean IsEnable() {
        if (this._DataAuthority != null) {
            return this._DataAuthority.IsEnable();
        }
        return false;
    }

    public boolean InitialDataAuthority(String password) {
        if (this._DataAuthority == null) {
            this._DataAuthority = new DataAuthority(this._DataSourceType);
        }
        return this._DataAuthority.Initial(this._Name, password, new BasicValue());
    }

    public boolean IsAllowWrite() {
        if (this._DataAuthority != null) {
            return this._DataAuthority.IsEnableWrite();
        }
        return false;
    }

    public boolean IsEncrypt() {
        if (this._DataAuthority != null) {
            return this._DataAuthority.IsEncrypt();
        }
        return false;
    }

    public String getPassword() {
        if (this._DataAuthority != null) {
            return this._DataAuthority.getPassword();
        }
        return "";
    }

    public String getDatabaseKey() {
        if (this._DataAuthority != null) {
            return this._DataAuthority.getDataKey();
        }
        return "";
    }

    public boolean Close() {
        if (this._EDatabase == null) {
            return true;
        }
        this._EDatabase.Close();
        return true;
    }

    public void UpdateDataAuthorityInfo(DataAuthority dataAuthority) {
        if (this._DataAuthority == null) {
            this._DataAuthority = new DataAuthority(1);
        }
        this._DataAuthority.CloneFrom(dataAuthority);
    }

    public boolean CreateDataset(String paramString) {
        if (!this._DataAuthority.IsEnable() || !this._DataAuthority.IsEnableWrite()) {
            return false;
        }
        ArrayList localArrayList = new ArrayList();
        localArrayList.add("CREATE TABLE IF NOT EXISTS " + paramString + "_D (");
        localArrayList.add("SYS_ID integer primary key autoincrement  not null default (0),");
        localArrayList.add("SYS_GEO Blob,");
        localArrayList.add("SYS_STATUS int,");
        localArrayList.add("SYS_TYPE varchar(50) DEFAULT '',");
        localArrayList.add("SYS_OID varchar(50),");
        localArrayList.add("SYS_LABEL varchar(50) DEFAULT '',");
        localArrayList.add("SYS_DATE varchar(50) DEFAULT '',");
        localArrayList.add("SYS_PHOTO Text DEFAULT '',");
        localArrayList.add("SYS_Length double DEFAULT 0,");
        localArrayList.add("SYS_Area double DEFAULT 0,");
        localArrayList.add("SYS_BZ1 Text,");
        localArrayList.add("SYS_BZ2 Text,");
        localArrayList.add("SYS_BZ3 Text,");
        localArrayList.add("SYS_BZ4 Text,");
        localArrayList.add("SYS_BZ5 Text,");
        for (int i = 1; i < 255; i++) {
            localArrayList.add("F" + i + " varchar(255) Default '',");
        }
        localArrayList.add("F255 varchar(255) Default '' )");
        ExcuteSQL(Common.CombineStrings("\r\n", localArrayList));
        return ExcuteSQL("CREATE TABLE IF NOT EXISTS " + paramString + "_I (SYS_ID Integer primary key not null UNIQUE,RIndex int,CIndex int,MinX double DEFAULT 0,MinY double DEFAULT 0,MaxX double DEFAULT 0,MaxY double DEFAULT 0)");
    }

    public boolean ExcuteSQL(String paramString) {
        if (!this._DataAuthority.IsEnable() || !this._DataAuthority.IsEnableRead()) {
            return false;
        }
        return this._EDatabase.ExecuteSQL(paramString);
    }

    public boolean ExcuteSQL(String paramString, Object[] paramArrayOfObject) {
        if (!this._DataAuthority.IsEnable() || !this._DataAuthority.IsEnableRead()) {
            return false;
        }
        return this._EDatabase.ExecuteSQL(paramString, paramArrayOfObject);
    }

    public DataSet GetDatasetByName(String paramString) {
        String tmpLyrIDString = paramString.toUpperCase();
        for (DataSet localDataset : getDatasets()) {
            if (localDataset.getName().toUpperCase().equals(tmpLyrIDString)) {
                return localDataset;
            }
        }
        if (paramString.contains("_")) {
            return GetDatasetByName(paramString.substring(0, paramString.lastIndexOf("_")));
        }
        return null;
    }

    public SQLiteDBHelper GetSQLiteDatabase() {
        return this._EDatabase;
    }

    public List<DataTableField> GetTableStruct(String paramString) {
        ArrayList localArrayList = new ArrayList();
        SQLiteReader localSQLiteDataReader = Query("select * from TableStruct where TableName='" + paramString + "'");
        while (localSQLiteDataReader != null && localSQLiteDataReader.Read()) {
            DataTableField localFieldInfo = new DataTableField();
            localFieldInfo.setName(localSQLiteDataReader.GetString("FieldName"));
            localFieldInfo.setCaption(localSQLiteDataReader.GetString("FieldCaption"));
            localArrayList.add(localFieldInfo);
        }
        return localArrayList;
    }

    public boolean Open() {
        this._EDatabase = new SQLiteDBHelper(getName(), this._DataAuthority.getDataKey());
        this._EDatabase.setDatabaseName(getName());
        return true;
    }

    public boolean OpenDatasets() {
        ArrayList localArrayList = new ArrayList();
        SQLiteReader localSQLiteDataReader = this._EDatabase.Query("select name from sqlite_master where type='table' and substr(name,length(name),1) in ('0','1','2') order by substr(name,length(name),1) desc");
        Iterator localIterator = null;
        if (!localSQLiteDataReader.Read()) {
            localSQLiteDataReader.Close();
            localIterator = localArrayList.iterator();
        }
        while (localIterator.hasNext()) {
            String str1 = (String) localIterator.next();
            if (this._EDatabase.ExecuteSQL("update " + str1 + " set SYS_STATUS = 0")) {
                DataSet localDataset = new DataSet(this);
                getDatasets().add(localDataset);
                localDataset.setName(str1);
                String str3 = str1.substring(str1.length() - 1, str1.length());
                if (str3.equals("0")) {
                    localDataset.setType(EGeoLayerType.POINT);
                }
                if (str3.equals("1")) {
                    localDataset.setType(EGeoLayerType.POLYLINE);
                }
                if (str3.equals("2")) {
                    localDataset.setType(EGeoLayerType.POLYGON);
                }
            }
        }
        return true;
    }

    public boolean OpenDatasetsIndex() {
        SQLiteReader localSQLiteDataReader = this._EDatabase.Query("select LT_JD,LT_WD,RB_JD,RB_WD from TableExtend");
        do {
        } while (localSQLiteDataReader.Read());
        localSQLiteDataReader.Close();
        new IndexFileLoader(this).ReadIndexData(String.valueOf(getName().substring(0, getName().lastIndexOf(FileSelector_Dialog.sFolder))) + ".idx");
        return true;
    }

    public SQLiteReader Query(String paramString) {
        return this._EDatabase.Query(paramString);
    }

    public boolean RemoveDataset(String paramString) {
        DataSet tempDataset = GetDatasetByName(paramString);
        if (tempDataset != null) {
            return tempDataset.Remove();
        }
        return true;
    }

    public boolean Save(String[] outMsg) {
        String tempSYSIDs;
        if (!this._DataAuthority.IsEnable() || !this._DataAuthority.IsEnableWrite()) {
            return false;
        }
        try {
            StringBuilder tempOutMsg = new StringBuilder();
            for (DataSet localDataset : getDatasets()) {
                try {
                    if (localDataset.getEdited() && localDataset.getRecordCount() > 0) {
                        for (ExtraGeometry pExtraGeometry : localDataset.getGeometryList()) {
                            if (pExtraGeometry.getGeometry() != null) {
                                AbstractGeometry pGeometry = pExtraGeometry.getGeometry();
                                if (pGeometry.getStatus() == EGeometryStatus.NONE && pGeometry.GetEdited()) {
                                    if (UpdateGeometry(Common.ConvertGeoToBytes(pGeometry), localDataset.getDataTableName(), pGeometry.GetSYS_ID())) {
                                        pGeometry.SetEdited(false);
                                    } else {
                                        tempOutMsg.append("[保存矢量数据错误]:" + localDataset.getLayerName() + ",第" + pGeometry.GetSYS_ID() + "个.\r\n");
                                    }
                                }
                            }
                        }
                        SQLiteReader tempReader = this._EDatabase.Query("Select GROUP_CONCAT(SYS_ID) AS MyID From " + localDataset.getDataTableName() + " Where SYS_STATUS=1 or SYS_GEO is null");
                        if (tempReader != null && tempReader.Read() && (tempSYSIDs = tempReader.GetString(0)) != null && !tempSYSIDs.equals("")) {
                            ExcuteSQL("delete from " + localDataset.getDataTableName() + " where SYS_ID in (" + tempSYSIDs + ")");
                            ExcuteSQL("delete from " + localDataset.getIndexTableName() + " where SYS_ID in (" + tempSYSIDs + ")");
                            localDataset.Purge();
                        }
                    }
                    localDataset.setEdited(false);
                } catch (Exception e) {
                }
            }
            if (outMsg != null && tempOutMsg.length() > 0) {
                outMsg[0] = String.valueOf(outMsg[0]) + tempOutMsg.toString();
            }
        } catch (Exception e2) {
        }
        return true;
    }

    public boolean UpdateGeometry(byte[] geoBytes, String dataTableName, String sysID) {
        if (!this._DataAuthority.IsEnable() || !this._DataAuthority.IsEnableWrite()) {
            Common.ShowDialog("数据文件未授权允许修改.");
        } else {
            try {
                this._EDatabase.ExecuteSQL("update " + dataTableName + " set SYS_GEO=? where SYS_ID=" + sysID, new Object[]{geoBytes});
                return true;
            } catch (Error localError) {
                Common.ShowDialog("矢量图形数据更新失败！" + localError.getMessage());
            }
        }
        return false;
    }

    public boolean UpdateGeometry(byte[] geoBytes, String dataTableName, int sysID) {
        if (!this._DataAuthority.IsEnable() || !this._DataAuthority.IsEnableWrite()) {
            Common.ShowDialog("数据文件未授权允许修改.");
        } else {
            try {
                this._EDatabase.ExecuteSQL("update " + dataTableName + " set SYS_GEO=? where SYS_ID=" + sysID, new Object[]{geoBytes});
                return true;
            } catch (Error localError) {
                Common.ShowDialog("矢量图形数据更新失败！" + localError.getMessage());
            }
        }
        return false;
    }

    public List<DataSet> getDatasets() {
        return this._Datasets;
    }

    public boolean getEdited() {
        for (DataSet dataSet : getDatasets()) {
            if (dataSet.getEdited()) {
                return true;
            }
        }
        return false;
    }

    public boolean getEditing() {
        return this._Editing;
    }

    public String getName() {
        return this._Name;
    }

    public void setEditing(boolean paramBoolean) {
        this._Editing = paramBoolean;
    }

    public void setName(String paramString) {
        this._Name = paramString;
    }

    public boolean ResotoreDeleteData(String[] outMsg) {
        SQLiteReader tempReader;
        String tempSYSIDs;
        if (!this._DataAuthority.IsEnable() || !this._DataAuthority.IsEnableWrite()) {
            return false;
        }
        try {
            StringBuilder tempOutMsg = new StringBuilder();
            for (DataSet localDataset : getDatasets()) {
                try {
                    if (localDataset.getEdited() && localDataset.getRecordCount() > 0 && (tempReader = this._EDatabase.Query("Select GROUP_CONCAT(SYS_ID) AS MyID From " + localDataset.getDataTableName() + " Where SYS_STATUS=1")) != null && tempReader.Read() && (tempSYSIDs = tempReader.GetString(0)) != null && !tempSYSIDs.equals("")) {
                        ExcuteSQL("Update " + localDataset.getDataTableName() + " Set SYS_STATUS=0 where SYS_ID in (" + tempSYSIDs + ")");
                        localDataset.Purge();
                    }
                    localDataset.setEdited(false);
                } catch (Exception e) {
                }
            }
            if (outMsg != null && tempOutMsg.length() > 0) {
                outMsg[0] = String.valueOf(outMsg[0]) + tempOutMsg.toString();
            }
        } catch (Exception e2) {
        }
        return true;
    }
}
