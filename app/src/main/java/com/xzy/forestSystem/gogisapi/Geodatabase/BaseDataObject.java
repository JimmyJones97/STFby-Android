package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.util.Log;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDataObject {
    private List<KeyValueViewDict> DataBindList = new ArrayList();
    private String LabelFieldName = "";
    private double SYS_Area = 0.0d;
    private String SYS_DATE = "";
    private int SYS_ID = -1;
    private String SYS_LABEL = "";
    private double SYS_Length = 0.0d;
    private String SYS_OID = "";
    private String SYS_PHOTO = "";
    private int SYS_STATUS = 0;
    private String SYS_TYPE = "";
    private String TableName = "Point0";
    private String _DataSourceName = "";
    private EDataSourceType m_DataSourceType = EDataSourceType.EDITING;
    private HashMap<String, Object> m_FieldsValue = new HashMap<>();

    private SQLiteDBHelper GetSQLiteDatabase() {
        if (!this._DataSourceName.equals("")) {
            DataSource tmpDatasource = PubVar.m_Workspace.GetDataSourceByName(this._DataSourceName);
            if (tmpDatasource != null) {
                return tmpDatasource.GetSQLiteDatabase();
            }
            return null;
        } else if (this.m_DataSourceType == EDataSourceType.CONFIG) {
            return PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase();
        } else {
            if (this.m_DataSourceType == EDataSourceType.PROJECT) {
                return PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase();
            }
            if (this.m_DataSourceType == EDataSourceType.EDITING) {
                return PubVar.m_Workspace.GetDataSourceByEditing().GetSQLiteDatabase();
            }
            return null;
        }
    }

    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x00df: APUT  
      (r1v0 'arrayOfObject' java.lang.Object[] A[D('arrayOfObject' java.lang.Object[])])
      (0 ??[int, short, byte, char])
      (wrap: java.lang.String : 0x00db: IGET  (r0v3 java.lang.String) = (r18v0 'this'  com.xzy.forestSystem.gogisapi.Geodatabase.BaseDataObject A[IMMUTABLE_TYPE, THIS])  com.xzy.forestSystem.gogisapi.Geodatabase.BaseDataObject.TableName java.lang.String)
     */
    private boolean SaveNewAdd(List<String> paramList) {
        String[] arrayOfString;
        boolean result = false;
        try {
            ArrayList localArrayList1 = new ArrayList();
            ArrayList localArrayList2 = new ArrayList();
            for (String tempStr : paramList) {
                if (!(tempStr == null || (arrayOfString = tempStr.split("=")) == null || arrayOfString.length != 2)) {
                    localArrayList1.add(arrayOfString[0]);
                    localArrayList2.add(arrayOfString[1]);
                }
            }
            Object[] arrayOfObject = new Object[3];
            if (this.m_DataSourceType == EDataSourceType.EDITING) {
                arrayOfObject[0] = String.valueOf(this.TableName) + "_D";
            } else {
                arrayOfObject[0] = this.TableName;
            }
            arrayOfObject[1] = Common.CombineStrings(",", localArrayList1);
            arrayOfObject[2] = Common.CombineStrings(",", localArrayList2);
            String str3 = String.format("insert into %1$s (%2$s) values (%3$s)", arrayOfObject);
            Log.d("", "正在保存数据[" + str3 + "]");
            if (GetSQLiteDatabase().ExecuteSQL(str3)) {
                result = true;
            }
            if (this.m_DataSourceType != EDataSourceType.EDITING || PubVar.m_Workspace == null) {
                return result;
            }
            DataSource localDataSource = PubVar.m_Workspace.GetDataSourceByEditing();
            if (localDataSource == null) {
                return false;
            }
            SQLiteReader localSQLiteDataReader = localDataSource.Query("select max(SYS_ID) as objectid from " + this.TableName + "_D");
            if (localSQLiteDataReader.Read()) {
                this.SYS_ID = Integer.valueOf(localSQLiteDataReader.GetString(0)).intValue();
            }
            localSQLiteDataReader.Close();
            DataSet localDataset = localDataSource.GetDatasetByName(this.TableName);
            if (localDataset == null) {
                return result;
            }
            List<Integer> tempList = new ArrayList<>();
            tempList.add(Integer.valueOf(this.SYS_ID));
            localDataset.UpdateLabelContent(tempList);
            List<String> tempList2 = new ArrayList<>();
            tempList2.add(String.valueOf(this.SYS_ID));
            localDataset.UpdateGeometrysSymbol(tempList2);
            return result;
        } catch (Exception ex) {
            Log.v("错误", "SaveNewAdd:" + ex.getMessage());
            return false;
        }
    }

    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x00b7: APUT  
      (r0v0 'arrayOfObject' java.lang.Object[] A[D('arrayOfObject' java.lang.Object[])])
      (0 ??[int, short, byte, char])
      (wrap: java.lang.String : 0x00b5: IGET  (r7v2 java.lang.String) = (r10v0 'this'  com.xzy.forestSystem.gogisapi.Geodatabase.BaseDataObject A[IMMUTABLE_TYPE, THIS])  com.xzy.forestSystem.gogisapi.Geodatabase.BaseDataObject.TableName java.lang.String)
     */
    private boolean UpdateFeatrue(List<String> paramList) {
        try {
            DataSource localDataSource = PubVar.m_Workspace.GetDataSourceByName(this._DataSourceName);
            if (localDataSource == null) {
                return false;
            }
            Object[] arrayOfObject = new Object[10];
            if (this.m_DataSourceType == EDataSourceType.EDITING) {
                arrayOfObject[0] = String.valueOf(this.TableName) + "_D";
            } else {
                arrayOfObject[0] = this.TableName;
            }
            arrayOfObject[1] = Integer.valueOf(this.SYS_STATUS);
            arrayOfObject[2] = this.SYS_TYPE;
            arrayOfObject[3] = this.SYS_LABEL;
            arrayOfObject[4] = this.SYS_PHOTO;
            arrayOfObject[5] = Double.valueOf(this.SYS_Length);
            arrayOfObject[6] = Double.valueOf(this.SYS_Area);
            arrayOfObject[7] = Common.dateFormat.format(new Date());
            arrayOfObject[8] = Common.CombineStrings(",", paramList);
            arrayOfObject[9] = Integer.valueOf(this.SYS_ID);
            if (!localDataSource.ExcuteSQL(String.format("update %1$s set SYS_STATUS=%2$s, SYS_TYPE='%3$s',SYS_LABEL='%4$s', SYS_PHOTO='%5$s',SYS_Length=%6$s,SYS_Area=%7$s,SYS_DATE='%8$s', %9$s where SYS_ID=%10$s", arrayOfObject))) {
                return false;
            }
            DataSet localDataset = localDataSource.GetDatasetByName(this.TableName);
            if (localDataset != null) {
                List<Integer> tempList = new ArrayList<>();
                tempList.add(Integer.valueOf(this.SYS_ID));
                localDataset.UpdateLabelContent(tempList);
                List<String> tempList2 = new ArrayList<>();
                tempList2.add(String.valueOf(this.SYS_ID));
                localDataset.UpdateGeometrysSymbol(tempList2);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void UpdateLabelContent() {
        String[] tempStrs01;
        this.SYS_LABEL = "";
        StringBuilder tempStrB = new StringBuilder();
        try {
            List<String> tempFiledsList = new ArrayList<>();
            if (!this.LabelFieldName.equals("") && (tempStrs01 = this.LabelFieldName.split(",")) != null && tempStrs01.length > 0) {
                for (String tempStr01 : tempStrs01) {
                    if (!tempStr01.equals("")) {
                        tempFiledsList.add(tempStr01);
                    }
                }
            }
            for (KeyValueViewDict tempKeyValue : this.DataBindList) {
                if (tempKeyValue != null && tempFiledsList.contains(tempKeyValue.Key)) {
                    tempStrB.append(tempKeyValue.Value);
                    tempStrB.append(",");
                }
            }
            if (tempStrB.length() > 0) {
                tempStrB = tempStrB.deleteCharAt(tempStrB.length() - 1);
            }
        } catch (Exception e) {
        }
        this.SYS_LABEL = tempStrB.toString();
    }

    public void AddItem(KeyValueViewDict paramDataBindOfKeyValue) {
        this.DataBindList.add(paramDataBindOfKeyValue);
    }

    public void ClearItems() {
        this.DataBindList = new ArrayList();
    }

    public void FillFeatureList(List<String> paramList) {
        if (paramList.size() > 0) {
            for (String str : paramList) {
                String[] tempStrs = str.split(",");
                if (tempStrs != null && tempStrs.length == 2) {
                    SetDataBindItemValue(tempStrs[0], tempStrs[1]);
                }
            }
        }
    }

    public List<String> GetFeatureList() {
        String[] tempStrs01;
        List<String> result = new ArrayList<>();
        StringBuilder tempStrB = new StringBuilder();
        List<String> tempFiledsList = new ArrayList<>();
        if (!this.LabelFieldName.equals("") && (tempStrs01 = this.LabelFieldName.split(",")) != null && tempStrs01.length > 0) {
            for (String tempStr01 : tempStrs01) {
                if (!tempStr01.equals("")) {
                    tempFiledsList.add(tempStr01);
                }
            }
        }
        for (KeyValueViewDict tempKeyValue : this.DataBindList) {
            if (tempKeyValue != null) {
                if (tempFiledsList.contains(tempKeyValue.Key)) {
                    tempStrB.append(tempKeyValue.Value);
                    tempStrB.append(",");
                }
                result.add(String.valueOf(tempKeyValue.DataKey) + "='" + tempKeyValue.Value.replaceAll("'", "''") + "'");
            }
        }
        if (tempStrB.length() > 0) {
            tempStrB = tempStrB.deleteCharAt(tempStrB.length() - 1);
        }
        this.SYS_LABEL = tempStrB.toString();
        return result;
    }

    public int GetPhotoCount() {
        if (this.SYS_PHOTO == null || this.SYS_PHOTO.equals("")) {
            return 0;
        }
        return this.SYS_PHOTO.split(",").length;
    }

    public String GetSYS_LABEL() {
        if (this.SYS_LABEL == null) {
            this.SYS_LABEL = "";
        }
        return this.SYS_LABEL;
    }

    public String GetSYS_OID() {
        if (this.SYS_OID == null) {
            this.SYS_OID = "";
        }
        return this.SYS_OID;
    }

    public String GetSYS_PHOTO() {
        if (this.SYS_PHOTO == null) {
            this.SYS_PHOTO = "";
        }
        return this.SYS_PHOTO;
    }

    public String GetLabelFieldName() {
        if (this.LabelFieldName == null) {
            this.LabelFieldName = "";
        }
        return this.LabelFieldName;
    }

    public void ReadDataAndBindToView(String paramString) {
        String[] tempFiledList;
        try {
            List<String> localArrayList = new ArrayList<>();
            SQLiteReader localSQLiteDataReader = GetSQLiteDatabase().Query(this.m_DataSourceType == EDataSourceType.EDITING ? "select * from " + this.TableName + "_D where " + paramString : "select * from " + this.TableName + " where " + paramString);
            if (localSQLiteDataReader != null) {
                if (localSQLiteDataReader.Read() && (tempFiledList = localSQLiteDataReader.GetFieldNameList()) != null && tempFiledList.length > 0) {
                    for (String tempFiledName : tempFiledList) {
                        if (tempFiledName.equals("SYS_ID")) {
                            this.SYS_ID = Integer.parseInt(localSQLiteDataReader.GetString(tempFiledName));
                        } else if (tempFiledName.equals("SYS_STATUS")) {
                            this.SYS_STATUS = localSQLiteDataReader.GetInt32(tempFiledName);
                        } else if (tempFiledName.equals("SYS_OID")) {
                            this.SYS_OID = localSQLiteDataReader.GetString(tempFiledName);
                        } else if (tempFiledName.equals("SYS_LABEL")) {
                            this.SYS_LABEL = localSQLiteDataReader.GetString(tempFiledName);
                        } else if (tempFiledName.equals("SYS_DATE")) {
                            this.SYS_DATE = localSQLiteDataReader.GetString(tempFiledName);
                        } else if (tempFiledName.equals("SYS_PHOTO")) {
                            this.SYS_PHOTO = localSQLiteDataReader.GetString(tempFiledName);
                        } else if (tempFiledName.equals("SYS_Length")) {
                            this.SYS_Length = localSQLiteDataReader.GetDouble(tempFiledName);
                        } else if (tempFiledName.equals("SYS_Area")) {
                            this.SYS_Area = localSQLiteDataReader.GetDouble(tempFiledName);
                        } else if (!tempFiledName.equals("SYS_GEO")) {
                            String tempGeo = localSQLiteDataReader.GetString(tempFiledName);
                            if (tempGeo == null) {
                                tempGeo = "";
                            }
                            localArrayList.add(String.valueOf(tempFiledName) + "," + tempGeo);
                        }
                    }
                }
                localSQLiteDataReader.Close();
                FillFeatureList(localArrayList);
                RefreshDataToView();
            }
        } catch (Exception e) {
        }
    }

    public void RefreshDataToView() {
        try {
            for (KeyValueViewDict tempKeyValue : this.DataBindList) {
                if (!(tempKeyValue == null || tempKeyValue.ViewControl == null)) {
                    Common.SetValueToView(tempKeyValue.Value, tempKeyValue.ViewControl);
                }
            }
        } catch (Exception e) {
        }
    }

    public void RefreshViewValueToData() {
        for (KeyValueViewDict tempKeyValue : this.DataBindList) {
            if (tempKeyValue.ViewControl != null) {
                String tempValue = Common.GetViewValue(tempKeyValue.ViewControl);
                tempKeyValue.Value = tempValue;
                this.m_FieldsValue.put(tempKeyValue.DataKey, tempValue);
            }
        }
    }

    public boolean Save() {
        if (this.SYS_ID == -1) {
            return SaveNewAdd(GetFeatureList());
        }
        return UpdateFeatrue(GetFeatureList());
    }

    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x0051: APUT  
      (r0v0 'arrayOfObject' java.lang.Object[] A[D('arrayOfObject' java.lang.Object[])])
      (0 ??[int, short, byte, char])
      (wrap: java.lang.String : 0x004f: IGET  (r3v4 java.lang.String) = (r6v0 'this'  com.xzy.forestSystem.gogisapi.Geodatabase.BaseDataObject A[IMMUTABLE_TYPE, THIS])  com.xzy.forestSystem.gogisapi.Geodatabase.BaseDataObject.TableName java.lang.String)
     */
    public boolean SaveMediaInfo() {
        DataSource localDataSource;
        if (this.SYS_ID == -1 || (localDataSource = PubVar.m_Workspace.GetDataSourceByName(this._DataSourceName)) == null) {
            return false;
        }
        Object[] arrayOfObject = new Object[3];
        if (localDataSource.getEditing()) {
            arrayOfObject[0] = String.valueOf(this.TableName) + "_D";
        } else {
            arrayOfObject[0] = this.TableName;
        }
        arrayOfObject[1] = this.SYS_PHOTO;
        arrayOfObject[2] = Integer.valueOf(this.SYS_ID);
        if (localDataSource.ExcuteSQL(String.format("update %1$s set SYS_PHOTO='%2$s' where SYS_ID=%3$s", arrayOfObject))) {
            return true;
        }
        return false;
    }

    public int SaveNewGeoToDb(AbstractGeometry paramGeometry) {
        try {
            DataSource localDataSource = PubVar.m_Workspace.GetDataSourceByEditing();
            if (localDataSource == null) {
                return -1;
            }
            Object[] arrayOfObject1 = {""};
            if (paramGeometry != null) {
                arrayOfObject1 = new Object[]{Common.ConvertGeoToBytes(paramGeometry)};
            }
            if (localDataSource.ExcuteSQL(String.format("insert into " + this.TableName + "_D (SYS_STATUS,SYS_GEO,SYS_TYPE,SYS_DATE,SYS_OID,SYS_LABEL,SYS_PHOTO) values (0,?,'%1$s','%2$s','%3$s','%4$s','%5$s')", this.SYS_TYPE, Common.GetSystemDate(), this.SYS_OID, this.SYS_LABEL, this.SYS_PHOTO), arrayOfObject1)) {
                SQLiteReader localSQLiteDataReader = localDataSource.Query("select max(SYS_ID) as objectid from " + this.TableName + "_D");
                if (localSQLiteDataReader.Read()) {
                    this.SYS_ID = Integer.valueOf(localSQLiteDataReader.GetString(0)).intValue();
                }
                localSQLiteDataReader.Close();
                if (this.SYS_ID != -1) {
                    DataSet localDataset = localDataSource.GetDatasetByName(this.TableName);
                    ArrayList localArrayList = new ArrayList();
                    localArrayList.add(String.valueOf(this.SYS_ID));
                    if (localDataset.QueryGeometrysFromDB(localArrayList, true)) {
                        AbstractGeometry localGeometry = localDataset.GetGeometryBySYSID(String.valueOf(this.SYS_ID));
                        if (localGeometry != null) {
                            localDataset.UpdateMapIndex(localGeometry);
                            SaveGeoIndexToDB(localGeometry);
                            PubVar._Map.ClearSelSelection();
                            Selection tmpSelection = PubVar._Map.getFeatureSelectionByLayerID(localDataset.getName(), false);
                            if (tmpSelection != null) {
                                tmpSelection.Add(localGeometry);
                            }
                            PubVar._Map.Refresh();
                        }
                        return this.SYS_ID;
                    }
                }
            }
            return -1;
        } catch (Error e) {
        }
        return -1;
    }

    public int SaveNewGeoToDb(byte[] geoData) {
        try {
            DataSource localDataSource = PubVar.m_Workspace.GetDataSourceByEditing();
            if (localDataSource == null || !localDataSource.ExcuteSQL(String.format("insert into " + this.TableName + "_D (SYS_STATUS,SYS_GEO,SYS_TYPE,SYS_DATE,SYS_OID,SYS_LABEL,SYS_PHOTO) values (0,?,'%1$s','%2$s','%3$s','%4$s','%5$s')", this.SYS_TYPE, Common.GetSystemDate(), this.SYS_OID, this.SYS_LABEL, this.SYS_PHOTO), new Object[]{geoData})) {
                return -1;
            }
            SQLiteReader localSQLiteDataReader = localDataSource.Query("select max(SYS_ID) as objectid from " + this.TableName + "_D");
            if (localSQLiteDataReader.Read()) {
                this.SYS_ID = Integer.valueOf(localSQLiteDataReader.GetString(0)).intValue();
            }
            localSQLiteDataReader.Close();
            if (this.SYS_ID == -1) {
                return -1;
            }
            DataSet localDataset = localDataSource.GetDatasetByName(this.TableName);
            ArrayList localArrayList = new ArrayList();
            localArrayList.add(String.valueOf(this.SYS_ID));
            if (!localDataset.QueryGeometrysFromDB(localArrayList, true)) {
                return -1;
            }
            AbstractGeometry localGeometry = localDataset.GetGeometryBySYSID(String.valueOf(this.SYS_ID));
            if (localGeometry != null) {
                localDataset.UpdateMapIndex(localGeometry);
                SaveGeoIndexToDB(localGeometry);
            }
            return this.SYS_ID;
        } catch (Error e) {
            return -1;
        }
    }

    public boolean SaveGeoIndexToDB(AbstractGeometry geometry) {
        String tempSYSID = geometry.GetSYS_ID();
        if (tempSYSID == null || tempSYSID.equals("")) {
            return false;
        }
        StringBuilder tempSB = new StringBuilder();
        tempSB.append("REPLACE INTO ");
        tempSB.append(this.TableName);
        tempSB.append("_I (SYS_ID,RIndex,CIndex,MinX,MinY,MaxX,MaxY) values (");
        tempSB.append(tempSYSID);
        tempSB.append(",");
        tempSB.append(geometry.GetRowIndex());
        tempSB.append(",");
        tempSB.append(geometry.GetColIndex());
        tempSB.append(",");
        Envelope pEnvelope = geometry.getEnvelope();
        tempSB.append(pEnvelope.getMinX());
        tempSB.append(",");
        tempSB.append(pEnvelope.getMinY());
        tempSB.append(",");
        tempSB.append(pEnvelope.getMaxX());
        tempSB.append(",");
        tempSB.append(pEnvelope.getMaxY());
        tempSB.append(")");
        return GetSQLiteDatabase().ExecuteSQL(tempSB.toString());
    }

    public int SaveGeoToDbOnly(AbstractGeometry paramGeometry, boolean isNew) {
        try {
            DataSource localDataSource = PubVar.m_Workspace.GetDataSourceByEditing();
            if (localDataSource == null) {
                return -1;
            }
            Object[] arrayOfObject1 = {""};
            if (paramGeometry != null) {
                arrayOfObject1 = new Object[]{Common.ConvertGeoToBytes(paramGeometry)};
            }
            if (isNew || this.SYS_ID == -1) {
                String str2 = String.format("insert into " + this.TableName + "_D (SYS_STATUS,SYS_GEO,SYS_TYPE,SYS_DATE,SYS_OID,SYS_LABEL,SYS_PHOTO) values (0,?,'%1$s','%2$s','%3$s','%4$s','%5$s')", this.SYS_TYPE, Common.GetSystemDate(), this.SYS_OID, this.SYS_LABEL, this.SYS_PHOTO);
                Log.d("", "正在保存数据[" + str2 + "]");
                if (localDataSource.ExcuteSQL(str2, arrayOfObject1)) {
                    SQLiteReader localSQLiteDataReader = localDataSource.Query("select max(SYS_ID) as objectid from " + this.TableName + "_D");
                    if (localSQLiteDataReader.Read()) {
                        this.SYS_ID = Integer.valueOf(localSQLiteDataReader.GetString(0)).intValue();
                    }
                    localSQLiteDataReader.Close();
                }
                return this.SYS_ID;
            }
            localDataSource.ExcuteSQL("Update " + this.TableName + " Set SYS_STATUS=0," + this.SYS_TYPE + "='" + this.SYS_TYPE + "',SYS_DATE='" + Common.GetSystemDate() + "',SYS_OID='" + this.SYS_OID + "',SYS_LABEL='" + this.SYS_LABEL + "',SYS_PHOTO='" + this.SYS_PHOTO + "',SYS_GEO=? Where SYS_ID=" + this.SYS_ID, arrayOfObject1);
            return this.SYS_ID;
        } catch (Exception e) {
        }
        return this.SYS_ID;
    }

    public int SaveFieldsAndGeoToDb(AbstractGeometry paramGeometry, List<String> paramList) {
        String tmpSqlString;
        String[] arrayOfString;
        try {
            DataSource localDataSource = PubVar.m_Workspace.GetDataSourceByEditing();
            if (localDataSource == null) {
                return -1;
            }
            Object[] arrayOfObject1 = {""};
            if (paramGeometry != null) {
                arrayOfObject1 = new Object[]{Common.ConvertGeoToBytes(paramGeometry)};
            }
            Object[] arrayOfObject = new Object[3];
            arrayOfObject[0] = String.valueOf(this.TableName) + "_D";
            if (paramList == null || paramList.size() <= 0) {
                tmpSqlString = String.format("insert into %1$s (SYS_GEO) values (?)", arrayOfObject);
            } else {
                ArrayList localArrayList1 = new ArrayList();
                ArrayList localArrayList2 = new ArrayList();
                for (String tempStr : paramList) {
                    if (!(tempStr == null || (arrayOfString = tempStr.split("=")) == null || arrayOfString.length != 2)) {
                        localArrayList1.add(arrayOfString[0]);
                        localArrayList2.add(arrayOfString[1]);
                    }
                }
                arrayOfObject[1] = Common.CombineStrings(",", localArrayList1);
                arrayOfObject[2] = Common.CombineStrings(",", localArrayList2);
                tmpSqlString = String.format("insert into %1$s (SYS_GEO,%2$s) values (?,%3$s)", arrayOfObject);
            }
            if (localDataSource.ExcuteSQL(tmpSqlString, arrayOfObject1)) {
                SQLiteReader localSQLiteDataReader = localDataSource.Query("select max(SYS_ID) as objectid from " + this.TableName + "_D");
                if (localSQLiteDataReader.Read()) {
                    this.SYS_ID = Integer.valueOf(localSQLiteDataReader.GetString(0)).intValue();
                }
                localSQLiteDataReader.Close();
            }
            return this.SYS_ID;
        } catch (Error e) {
        }
        return this.SYS_ID;
    }

    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x009e: APUT  
      (r2v0 'arrayOfObject' java.lang.Object[] A[D('arrayOfObject' java.lang.Object[])])
      (1 ??[boolean, int, float, short, byte, char])
      (wrap: java.lang.String : 0x009a: INVOKE  (r15v16 java.lang.String) = (","), (r6v0 'localArrayList1' java.util.ArrayList A[D('localArrayList1' java.util.ArrayList)]) type: STATIC call:  com.xzy.forestSystem.gogisapi.Common.Common.CombineStrings(java.lang.String, java.util.List):java.lang.String)
     */
    public int SaveFieldsAndGeoToDb2(AbstractGeometry paramGeometry, List<String> paramList) {
        String tmpSqlString;
        String[] arrayOfString;
        try {
            DataSource localDataSource = PubVar.m_Workspace.GetDataSourceByEditing();
            if (localDataSource == null) {
                return -1;
            }
            Object[] arrayOfObject1 = {""};
            if (paramGeometry != null) {
                arrayOfObject1 = new Object[]{Common.ConvertGeoToBytes(paramGeometry)};
            }
            Object[] arrayOfObject = new Object[3];
            arrayOfObject[0] = String.valueOf(this.TableName) + "_D";
            if (paramList == null || paramList.size() <= 0) {
                tmpSqlString = String.format("insert into " + this.TableName + "_D (SYS_STATUS,SYS_GEO,SYS_TYPE,SYS_DATE,SYS_OID,SYS_LABEL,SYS_PHOTO) values (0,?,'%1$s','%2$s','%3$s','%4$s','%5$s')", this.SYS_TYPE, Common.GetSystemDate(), this.SYS_OID, this.SYS_LABEL, this.SYS_PHOTO);
            } else {
                ArrayList localArrayList1 = new ArrayList();
                ArrayList localArrayList2 = new ArrayList();
                localArrayList1.add("SYS_TYPE");
                localArrayList2.add(this.SYS_TYPE);
                localArrayList1.add("SYS_DATE");
                localArrayList2.add(Common.GetSystemDate());
                localArrayList1.add("SYS_OID");
                localArrayList2.add(this.SYS_OID);
                localArrayList1.add("SYS_LABEL");
                localArrayList2.add(this.SYS_LABEL);
                localArrayList1.add("SYS_PHOTO");
                localArrayList2.add(this.SYS_PHOTO);
                for (String tempStr : paramList) {
                    if (!(tempStr == null || (arrayOfString = tempStr.split("=")) == null || arrayOfString.length != 2)) {
                        localArrayList1.add(arrayOfString[0]);
                        localArrayList2.add(arrayOfString[1]);
                    }
                }
                arrayOfObject[1] = Common.CombineStrings(",", localArrayList1);
                arrayOfObject[2] = Common.CombineStrings("','", localArrayList2);
                tmpSqlString = String.format("insert into %1$s (SYS_STATUS,SYS_GEO,%2$s) values (0,?,'%3$s')", arrayOfObject);
            }
            if (localDataSource.ExcuteSQL(tmpSqlString, arrayOfObject1)) {
                SQLiteReader localSQLiteDataReader = localDataSource.Query("select max(SYS_ID) as objectid from " + this.TableName + "_D");
                if (localSQLiteDataReader.Read()) {
                    this.SYS_ID = Integer.valueOf(localSQLiteDataReader.GetString(0)).intValue();
                }
                localSQLiteDataReader.Close();
            }
            return this.SYS_ID;
        } catch (Error e) {
        }
        return this.SYS_ID;
    }

    public String GetSaveFieldsAndGeoToDbSQL(AbstractGeometry paramGeometry, List<String> paramList) {
        String[] arrayOfString;
        try {
            String  Object = "";
            if (paramGeometry != null) {
                Object[] arrayOfObject1 = {Common.ConvertGeoToBytes(paramGeometry)};
            }
            ArrayList localArrayList1 = new ArrayList();
            ArrayList localArrayList2 = new ArrayList();
            for (String tempStr : paramList) {
                if (!(tempStr == null || (arrayOfString = tempStr.split("=")) == null || arrayOfString.length != 2)) {
                    localArrayList1.add(arrayOfString[0]);
                    localArrayList2.add(arrayOfString[1]);
                }
            }
            return String.format("insert into %1$s (SYS_GEO,%2$s) values (?,%3$s)", String.valueOf(this.TableName) + "_D", Common.CombineStrings(",", localArrayList1), Common.CombineStrings(",", localArrayList2));
        } catch (Error e) {
            return null;
        }
    }

    public void SetBaseObjectRelateLayerID(String layerID) {
        this.TableName = layerID;
    }

    public void SetBaseObjectRelateTableName(String tableName) {
        this.TableName = tableName;
    }

    public void SetDataBindItemValue(String dataFieldName, String fieldValue) {
        for (KeyValueViewDict localDataBindOfKeyValue : this.DataBindList) {
            if (localDataBindOfKeyValue != null && localDataBindOfKeyValue.DataKey.equals(dataFieldName)) {
                localDataBindOfKeyValue.Value = fieldValue;
            }
        }
    }

    public boolean SetDataBindItemValueByName(String fieldName, String fieldValue) {
        for (KeyValueViewDict localDataBindOfKeyValue : this.DataBindList) {
            if (localDataBindOfKeyValue != null && localDataBindOfKeyValue.Key.equals(fieldName)) {
                localDataBindOfKeyValue.Value = fieldValue;
                return true;
            }
        }
        return false;
    }

    public void SetDataSourceType(EDataSourceType paramlkDataSourceType) {
        this.m_DataSourceType = paramlkDataSourceType;
    }

    public void SetLabelFieldName(String paramString) {
        this.LabelFieldName = paramString;
    }

    public void SetSYS_DATE(String paramString) {
        this.SYS_DATE = paramString;
    }

    public void SetSYS_ID(int paramInt) {
        this.SYS_ID = paramInt;
    }

    public int GetSYS_ID() {
        return this.SYS_ID;
    }

    public void SetSYS_PHOTO(String paramString) {
        this.SYS_PHOTO = paramString;
    }

    public void SetSYS_TYPE(String paramString) {
        this.SYS_TYPE = paramString;
    }

    public void SetDataSourceName(String dataSourceName) {
        this._DataSourceName = dataSourceName;
    }

    public void AddFieldValue(String fieldID, Object value) {
        this.m_FieldsValue.put(fieldID, value);
    }

    public HashMap<String, Object> getFieldsValue() {
        return this.m_FieldsValue;
    }

    public boolean hasFieldsValue() {
        if (this.m_FieldsValue.size() > 0) {
            return true;
        }
        return false;
    }

    public Object getFieldValue(String fieldID) {
        for (Map.Entry<String, Object> tempEntry : this.m_FieldsValue.entrySet()) {
            if (tempEntry.getKey().equals(fieldID)) {
                return tempEntry.getValue();
            }
        }
        return null;
    }
}
