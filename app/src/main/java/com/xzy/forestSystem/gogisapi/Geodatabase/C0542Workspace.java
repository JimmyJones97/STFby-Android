package  com.xzy.forestSystem.gogisapi.Geodatabase;

import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.Encryption.DataAuthority;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from:  com.xzy.forestSystem.gogisapi.Geodatabase.Workspace */
public class C0542Workspace {
    AbstractC0383CoordinateSystem _CoorSystem;
    private List<DataSource> _DataSourceList;
    private String _ProjectPwd;

    public C0542Workspace() {
        this._DataSourceList = null;
        this._CoorSystem = null;
        this._ProjectPwd = "";
        this._DataSourceList = new ArrayList();
    }

    public boolean CloseDataSource(DataSource paramDataSource, boolean paramBoolean) {
        paramDataSource.Dispose2();
        getDataSourceList().remove(paramDataSource);
        return true;
    }

    public void ClearWorkspace() {
        for (DataSource tmpDataSource : this._DataSourceList) {
            if (tmpDataSource != null) {
                tmpDataSource.Dispose2();
            }
        }
        this._DataSourceList.clear();
        this._CoorSystem = null;
        this._ProjectPwd = "";
    }

    public DataSource GetDataSourceByEditing() {
        Iterator localIterator = getDataSourceList().iterator();
        DataSource localDataSource = null;
        while (localIterator.hasNext()) {
            localDataSource = (DataSource) localIterator.next();
            if (localDataSource.getEditing()) {
                break;
            }
        }
        return localDataSource;
    }

    public DataSource GetDataSourceByName(String datasourceName) {
        for (DataSource result : getDataSourceList()) {
            if (result.getName().equals(datasourceName)) {
                return result;
            }
        }
        return null;
    }

    public DataSet GetDatasetByName(String datasourceName, String datasetName) {
        DataSource pDataSource = GetDataSourceByName(datasourceName);
        if (pDataSource != null) {
            return pDataSource.GetDatasetByName(datasetName);
        }
        return null;
    }

    public DataSet GetDatasetByName(String layerID) {
        DataSet result;
        for (DataSource tmpDataSource : this._DataSourceList) {
            if (!(tmpDataSource == null || (result = tmpDataSource.GetDatasetByName(layerID)) == null)) {
                return result;
            }
        }
        return null;
    }

    public DataSource GetDataSourceByUnEditing() {
        for (DataSource localDataSource : getDataSourceList()) {
            if (!localDataSource.getEditing()) {
                return localDataSource;
            }
        }
        return null;
    }

    public DataSource GetDataSourceByUnEditing(String dataSourceName) {
        for (DataSource localDataSource : getDataSourceList()) {
            if (!localDataSource.getEditing() && localDataSource.getName().equals(dataSourceName)) {
                return localDataSource;
            }
        }
        return null;
    }

    public void SetProjectPWD(String pwd) {
        this._ProjectPwd = pwd;
    }

    public String getProjectPassword() {
        return this._ProjectPwd;
    }

    public boolean OpenVectorsDataSource(String filepath, String password) {
        if (!Common.CheckExistFile(filepath)) {
            return false;
        }
        DataSource tmpDataSource = GetDataSourceByName(filepath);
        if (tmpDataSource == null) {
            DataSource tmpDataSource2 = new DataSource(filepath, password, 1);
            tmpDataSource2.setEditing(false);
            this._DataSourceList.add(tmpDataSource2);
            return tmpDataSource2.IsEnable();
        }
        DataAuthority tmpDataAuthority = new DataAuthority(1);
        tmpDataAuthority.Initial(filepath, password, new BasicValue());
        tmpDataSource.UpdateDataAuthorityInfo(tmpDataAuthority);
        boolean result = tmpDataSource.IsEnable();
        if (!result) {
            return result;
        }
        tmpDataSource.Open();
        return result;
    }

    public boolean OpenEditDataSource(String dataFilePath) {
        DataSource localDataSource = new DataSource(dataFilePath, this._ProjectPwd, 0);
        localDataSource.setEditing(true);
        this._DataSourceList.add(localDataSource);
        return true;
    }

    public boolean Save(String[] outMsg) {
        for (DataSource localDataSource : getDataSourceList()) {
            if (localDataSource.getEditing()) {
                localDataSource.Save(outMsg);
            }
        }
        return true;
    }

    public boolean CheckIfNeedSave() {
        for (DataSource localDataSource : getDataSourceList()) {
            if (localDataSource.getEditing() && localDataSource.getEdited()) {
                return true;
            }
        }
        return false;
    }

    public void SetCoorSystemInfo(AbstractC0383CoordinateSystem coorSystem) {
        this._CoorSystem = coorSystem;
    }

    public AbstractC0383CoordinateSystem GetCoorSystem() {
        return this._CoorSystem;
    }

    public List<DataSource> getDataSourceList() {
        return this._DataSourceList;
    }

    public boolean RestoreDeleteData(String[] outMsg) {
        for (DataSource localDataSource : getDataSourceList()) {
            if (localDataSource.getEditing()) {
                localDataSource.ResotoreDeleteData(outMsg);
            }
        }
        return true;
    }
}
