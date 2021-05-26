package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.content.ContentValues;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EFieldAutoValueType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import  com.xzy.forestSystem.gogisapi.Carto.MapCellIndex;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.CommonProcess;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Display.ClassifiedRender;
import  com.xzy.forestSystem.gogisapi.Display.EDisplayType;
import  com.xzy.forestSystem.gogisapi.Display.IRender;
import  com.xzy.forestSystem.gogisapi.Edit.EGeometryStatus;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.Geometry.ExtraGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Point;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

public class DataSet {
    public boolean HasInitial = false;
    private GeoLayer _BindGeoLayer = null;
    private DataSource _DataSource = null;
    private String _DataTableName = "";
    private boolean _Edited = false;
    private List<ExtraGeometry> _GeometryList = new ArrayList();
    private List<Integer> _GeometrySYSIDList = new ArrayList();
    private String _IndexTableName = "";
    private String _LayerName = "";
    private String _Name = "";
    private double _OffsetX = 0.0d;
    private double _OffsetY = 0.0d;
    private List<Integer> _PurgeObjectIndexList = new ArrayList();
    private List<DataTableField> _TableStruct = null;
    private int _TotalDataCount = 0;
    private EGeoLayerType _Type = EGeoLayerType.NONE;
    private MapCellIndex m_MapCellIndex = null;

    public DataSet(DataSource paramDataSource) {
        this._DataSource = paramDataSource;
        this.m_MapCellIndex = new MapCellIndex();
        GetExtendFromDB();
    }

    public void Dispose2() {
        for (ExtraGeometry tmpExtraGeometry : this._GeometryList) {
            if (tmpExtraGeometry != null) {
                tmpExtraGeometry.Dispose2();
            }
        }
        this._GeometryList.clear();
        this._PurgeObjectIndexList.clear();
        if (this._TableStruct != null) {
            this._TableStruct.clear();
        }
    }

    public MapCellIndex GetMapCellIndex() {
        return this.m_MapCellIndex;
    }

    public void SetMapCellIndex(MapCellIndex paramMapCellIndex) {
        this.m_MapCellIndex = paramMapCellIndex;
    }

    private boolean InViewWindow(Envelope paramEnvelope, int geoDIndex) {
        if (geoDIndex >= this._GeometryList.size()) {
            return false;
        }
        if (getType() != EGeoLayerType.POINT) {
            return paramEnvelope.Intersect(GetGeometryEnvelope(geoDIndex));
        }
        ExtraGeometry localExtraGeometry = this._GeometryList.get(geoDIndex);
        return paramEnvelope.ContainsPoint(localExtraGeometry.getEnvelope().getMinX(), localExtraGeometry.getEnvelope().getMinY());
    }

    public boolean AddExtraGeometry(ExtraGeometry paramExtraGeometry) {
        this._GeometryList.add(paramExtraGeometry);
        this._GeometrySYSIDList.add(Integer.valueOf(paramExtraGeometry.getGeometry().GetSYS_IDIndex()));
        return true;
    }

    public boolean AddGeometry(AbstractGeometry paramGeometry) {
        return AddGeometry(paramGeometry, false, false);
    }

    public boolean AddGeometry(AbstractGeometry paramGeometry, boolean refreshLayer, boolean isNewGeo) {
        boolean bool = true;
        if (isNewGeo) {
            paramGeometry.setOID(UUID.randomUUID().toString());
            bool = this._DataSource.ExcuteSQL("insert into " + getDataTableName() + " (SYS_OID,SYS_ID) values ('{" + paramGeometry.getOID() + "}'," + (this._GeometryList.size() + 1) + ")");
        }
        if (bool) {
            paramGeometry.setIndex(this._GeometryList.size());
        }
        return bool;
    }

    public int AddNewGeometry(AbstractGeometry geometry, String addTypeName, boolean needSelected) {
        Selection tmpSelection;
        try {
            Object[] arrayOfObject1 = {""};
            if (geometry != null) {
                arrayOfObject1 = new Object[]{Common.ConvertGeoToBytes(geometry)};
            }
            String str1 = "insert into " + this._DataTableName + " (SYS_STATUS,SYS_GEO,SYS_TYPE,SYS_DATE,SYS_OID) values (0,?,'%1$s','%2$s','%3$s')";
            Object[] arrayOfObject2 = new Object[6];
            arrayOfObject2[0] = addTypeName;
            arrayOfObject2[1] = Common.GetSystemDate();
            arrayOfObject2[2] = geometry.getOID();
            if (this._DataSource.ExcuteSQL(String.format(str1, arrayOfObject2), arrayOfObject1)) {
                int tmpSysID = -1;
                SQLiteReader localSQLiteDataReader = this._DataSource.Query("select max(SYS_ID) as objectid from " + this._DataTableName);
                if (localSQLiteDataReader.Read()) {
                    tmpSysID = Integer.valueOf(localSQLiteDataReader.GetString(0)).intValue();
                }
                localSQLiteDataReader.Close();
                geometry.SetSYS_ID(String.valueOf(tmpSysID));
                if (tmpSysID != -1) {
                    List<String> tmpList = new ArrayList<>();
                    tmpList.add(geometry.GetSYS_ID());
                    if (QueryGeometrysFromDB(tmpList, true)) {
                        UpdateMapIndex(geometry);
                        SaveGeoIndexToDB(geometry);
                        if (!needSelected || (tmpSelection = PubVar._Map.getFeatureSelectionByLayerID(getName(), false)) == null) {
                            return tmpSysID;
                        }
                        tmpSelection.AddBySysID(String.valueOf(tmpSysID));
                        return tmpSysID;
                    }
                }
            }
        } catch (Error e) {
        }
        return -1;
    }

    public boolean UpdateFieldsValue(String geoemtrySYS_ID, HashMap<String, Object> fieldsValueHashMap) {
        if (fieldsValueHashMap.size() > 0) {
            StringBuilder tmpSBuilder = new StringBuilder();
            for (Map.Entry<String, Object> entry : fieldsValueHashMap.entrySet()) {
                String tmpFieldName = entry.getKey();
                Object val = entry.getValue();
                if (val == null) {
                    val = "";
                }
                if (tmpSBuilder.length() > 0) {
                    tmpSBuilder.append(",");
                }
                tmpSBuilder.append(tmpFieldName);
                tmpSBuilder.append("='");
                tmpSBuilder.append(val);
                tmpSBuilder.append("' ");
            }
            if (tmpSBuilder.length() > 0) {
                return this._DataSource.ExcuteSQL("Update " + this._DataTableName + " Set " + tmpSBuilder.toString() + " Where SYS_ID=" + geoemtrySYS_ID);
            }
        }
        return false;
    }

    public boolean UpdateFieldsValue(String geoemtrySYS_ID, HashMap<String, Object> fieldsValueHashMap, String photoInfo) {
        StringBuilder tmpSBuilder = new StringBuilder();
        tmpSBuilder.append("SYS_PHOTO='");
        if (photoInfo != null) {
            tmpSBuilder.append(photoInfo);
        }
        tmpSBuilder.append("'");
        if (fieldsValueHashMap != null && fieldsValueHashMap.size() > 0) {
            for (Map.Entry<String, Object> entry : fieldsValueHashMap.entrySet()) {
                String tmpFieldName = entry.getKey();
                Object val = entry.getValue();
                if (val == null) {
                    val = "";
                }
                tmpSBuilder.append(",");
                tmpSBuilder.append(tmpFieldName);
                tmpSBuilder.append("='");
                tmpSBuilder.append(val);
                tmpSBuilder.append("' ");
            }
        }
        if (tmpSBuilder.length() > 0) {
            return this._DataSource.ExcuteSQL("Update " + this._DataTableName + " Set " + tmpSBuilder.toString() + " Where SYS_ID=" + geoemtrySYS_ID);
        }
        return false;
    }

    public String getGeoemtryMediaInfoBySYSID(String geoemtrySYS_ID) {
        String result = "";
        SQLiteReader localSQLiteDataReader = this._DataSource.Query("select SYS_PHOTO from " + this._DataTableName + " Where SYS_ID=" + geoemtrySYS_ID);
        if (localSQLiteDataReader != null && localSQLiteDataReader.Read()) {
            result = localSQLiteDataReader.GetString(0);
        }
        localSQLiteDataReader.Close();
        return result;
    }

    public String AutoGenerateFieldValue(AbstractGeometry geometry, LayerField layerField) {
        SQLiteReader localSQLiteDataReader;
        String result = "";
        try {
            if (layerField.AutoValueType == EFieldAutoValueType.Geo_X) {
                return String.format("%f", Double.valueOf(geometry.getPoint(0).getX()));
            }
            if (layerField.AutoValueType == EFieldAutoValueType.Geo_Y) {
                return String.format("%f", Double.valueOf(geometry.getPoint(0).getY()));
            }
            if (layerField.AutoValueType == EFieldAutoValueType.Geo_Z) {
                return String.format("%f", Double.valueOf(geometry.getPoint(0).getZ()));
            }
            if (layerField.AutoValueType == EFieldAutoValueType.Geo_XInt) {
                return String.format("%d", Integer.valueOf((int) geometry.getPoint(0).getX()));
            }
            if (layerField.AutoValueType == EFieldAutoValueType.Geo_YInt) {
                return String.format("%d", Integer.valueOf((int) geometry.getPoint(0).getY()));
            }
            if (layerField.AutoValueType == EFieldAutoValueType.Geo_ZInt) {
                return String.format("%d", Integer.valueOf((int) geometry.getPoint(0).getZ()));
            }
            if (layerField.AutoValueType == EFieldAutoValueType.Geo_Longitude) {
                return String.format("%f", Double.valueOf(geometry.getPoint(0).getGeoX()));
            }
            if (layerField.AutoValueType == EFieldAutoValueType.Geo_Latitude) {
                return String.format("%f", Double.valueOf(geometry.getPoint(0).getGeoY()));
            }
            if (layerField.AutoValueType == EFieldAutoValueType.GPS_Longitude) {
                return String.format("%f", Double.valueOf(PubVar._PubCommand.m_GpsLocation.getGPSCoordinate2().getX()));
            }
            if (layerField.AutoValueType == EFieldAutoValueType.GPS_Latitude) {
                return String.format("%f", Double.valueOf(PubVar._PubCommand.m_GpsLocation.getGPSCoordinate2().getY()));
            }
            if (layerField.AutoValueType == EFieldAutoValueType.GPS_Elevation) {
                return String.format("%f", Double.valueOf(PubVar._PubCommand.m_GpsLocation.getGPSCoordinate2().getZ()));
            }
            if (layerField.AutoValueType == EFieldAutoValueType.Geo_Longitude_Int9) {
                return String.format("%d", Integer.valueOf((int) (geometry.getPoint(0).getGeoX() * 1000000.0d)));
            }
            if (layerField.AutoValueType == EFieldAutoValueType.Geo_Latitude_Int8) {
                return String.format("%d", Integer.valueOf((int) (geometry.getPoint(0).getGeoY() * 1000000.0d)));
            }
            if (layerField.AutoValueType == EFieldAutoValueType.Geo_Length) {
                if (geometry instanceof Polyline) {
                    return String.format("%f", Double.valueOf(((Polyline) geometry).getLength(true)));
                }
                if (geometry instanceof Polygon) {
                    return String.format("%f", Double.valueOf(((Polygon) geometry).getLength(true)));
                }
                return "0";
            } else if (layerField.AutoValueType == EFieldAutoValueType.Geo_Area) {
                if (geometry instanceof Polygon) {
                    return String.format("%f", Double.valueOf(Common.ConvertAreaOfUnit(((Polygon) geometry).getArea(true))));
                }
                return "0";
            } else if (layerField.AutoValueType == EFieldAutoValueType.DateTime_Long) {
                return Common.dateFormat.format(new Date());
            } else {
                if (layerField.AutoValueType == EFieldAutoValueType.DateTime_Small) {
                    return Common.dateSmallFormat.format(new Date());
                }
                if (layerField.AutoValueType != EFieldAutoValueType.Auto_Increase || (localSQLiteDataReader = this._DataSource.Query("Select " + layerField.GetDataFieldName() + " From " + this._DataTableName + " Order By SYS_ID DESC Limit 5")) == null) {
                    return result;
                }
                while (true) {
                    if (!localSQLiteDataReader.Read()) {
                        break;
                    }
                    String tmpString01 = localSQLiteDataReader.GetString(0).trim();
                    if (tmpString01.length() > 0) {
                        if (CommonProcess.isDigit(tmpString01)) {
                            result = String.valueOf(Integer.parseInt(tmpString01) + 1);
                        } else {
                            result = String.valueOf(CommonProcess.splitNotNumber(tmpString01)) + String.valueOf(Integer.parseInt(CommonProcess.getNumbers(tmpString01)) + 1);
                        }
                    }
                }
                localSQLiteDataReader.Close();
                if (result.length() == 0) {
                    return "1";
                }
                return result;
            }
        } catch (Exception e) {
            return result;
        }
    }

    public boolean BuildSpatialIndex() {
        BuildMapIndex(true, false);
        int tid = 0;
        for (ExtraGeometry tempExtGeo : this._GeometryList) {
            tempExtGeo.setDIndex(tid);
            JustUpdateGeoMapIndex(tempExtGeo.getGeometry());
            tid++;
        }
        return true;
    }

    public void ClearGeometry(List<Integer> paramList, Selection selection, boolean paramBoolean) {
        paramList.size();
    }

    public int getGeometryCount() {
        return this._GeometryList.size();
    }

    public AbstractGeometry GetGeometry(int index) {
        return this._GeometryList.get(index).getGeometry();
    }

    public AbstractGeometry GetGeometryByOID(String sysOID) {
        for (ExtraGeometry tempGeo : this._GeometryList) {
            if (tempGeo.getGeometry().getOID().equals(sysOID)) {
                return tempGeo.getGeometry();
            }
        }
        return null;
    }

    public AbstractGeometry GetGeometryBySYSID(String sysID) {
        for (ExtraGeometry tempGeo : this._GeometryList) {
            if (tempGeo.getGeometry().GetSYS_ID().equals(sysID)) {
                return tempGeo.getGeometry();
            }
        }
        return null;
    }

    public AbstractGeometry GetGeometryBySYSIDMust(String sysID) {
        AbstractGeometry result = null;
        Iterator<ExtraGeometry> tempIter = this._GeometryList.iterator();
        while (true) {
            if (tempIter.hasNext()) {
                ExtraGeometry tempGeo = tempIter.next();
                if (tempGeo.getGeometry().GetSYS_ID().equals(sysID)) {
                    result = tempGeo.getGeometry();
                    break;
                }
            } else {
                break;
            }
        }
        if (result == null) {
            return QueryGeometryFromDBBy_SYSID(sysID);
        }
        return result;
    }

    public void refreshAllGeometryIndex() {
        int tid = 0;
        for (ExtraGeometry tempExtGeo : this._GeometryList) {
            tempExtGeo.setDIndex(tid);
            tid++;
        }
    }

    public int GetGeometryDIndex(int paramInt) {
        return this._GeometryList.get(paramInt).getDIndex();
    }

    public Envelope GetGeometryEnvelope(int paramInt) {
        return this._GeometryList.get(paramInt).getEnvelope();
    }

    public AbstractGeometry GetGeometryFromDIndex(int paramInt) {
        for (ExtraGeometry localExtraGeometry : this._GeometryList) {
            if (localExtraGeometry.getDIndex() == paramInt) {
                return localExtraGeometry.getGeometry();
            }
        }
        return null;
    }

    public List<String> GetSelectObject(Coordinate tmpCoord, double paramDouble) {
        return new ArrayList();
    }

    public boolean HitTest(Coordinate tmpCoord, double distance, boolean isMultSelect, Selection paramSelection, Selection _showSelection) {
        boolean result = false;
        try {
            if (this._GeometryList.size() == 0) {
                return false;
            }
            Envelope tempExtend = new Envelope(tmpCoord.getX() - distance, tmpCoord.getY() + distance, tmpCoord.getX() + distance, tmpCoord.getY() - distance);
            List<Integer> tempCellExtList = this.m_MapCellIndex.CalCellIndexExtend(tempExtend);
            List<Integer> tempList = new ArrayList<>();
            if (tempCellExtList.size() > 0) {
                for (Integer num : _showSelection.getGeometryIndexList()) {
                    AbstractGeometry tempGeo = GetGeometry(num.intValue());
                    if (Common.CellIndexContain(tempCellExtList, tempGeo.GetRowIndex(), tempGeo.GetColIndex())) {
                        tempList.add(Integer.valueOf(tempGeo.getIndex()));
                    }
                }
            }
            if (tempList.size() > 0) {
                List<Integer> tmpLastResult = new ArrayList<>();
                List<Integer> tmpIsErrorGeoList = new ArrayList<>();
                for (Integer num2 : tempList) {
                    int tempI = num2.intValue();
                    if (InViewWindow(tempExtend, tempI)) {
                        if (GetGeometry(tempI) != null) {
                            tmpLastResult.add(Integer.valueOf(tempI));
                        } else if (tmpIsErrorGeoList.indexOf(Integer.valueOf(tempI)) < 0) {
                            tmpIsErrorGeoList.add(Integer.valueOf(tempI));
                        }
                    }
                }
                if (tmpLastResult.size() > 0) {
                    ListIterator<Integer> tempIter03 = tmpLastResult.listIterator();
                    while (tempIter03.hasNext()) {
                        tempIter03.next();
                    }
                    AbstractGeometry selectExtendPoly = tempExtend.ConvertToPolyline();
                    while (tempIter03.hasPrevious()) {
                        int i = tempIter03.previous().intValue();
                        AbstractGeometry localGeometry = GetGeometry(i);
                        if (!(localGeometry == null || _showSelection.getGeometryIndexList().indexOf(Integer.valueOf(i)) < 0 || localGeometry.getStatus() == EGeometryStatus.DELETE)) {
                            if (getType() == EGeoLayerType.POINT) {
                                if (((Point) localGeometry).HitTest(tmpCoord, distance)) {
                                    paramSelection.Add(localGeometry);
                                    if (!isMultSelect) {
                                        return true;
                                    }
                                } else {
                                    continue;
                                }
                            } else if (getType() == EGeoLayerType.POLYLINE) {
                                if (((Polyline) localGeometry).getSpatialRelation().JudgeIntersect(selectExtendPoly)) {
                                    paramSelection.Add(localGeometry);
                                    if (!isMultSelect) {
                                        return true;
                                    }
                                } else {
                                    continue;
                                }
                            } else if (getType() == EGeoLayerType.POLYGON && ((Polygon) localGeometry).HitTest(tmpCoord, distance)) {
                                paramSelection.Add(localGeometry);
                                if (!isMultSelect) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            if (paramSelection.getCount() > 0) {
                result = true;
            }
            return result;
        } catch (Exception e) {
        }
        return result;
    }

    public boolean HitTest(Coordinate tmpCoord, double distance, Selection paramSelection, Selection _showSelection) {
        return HitTest(tmpCoord, distance, false, paramSelection, _showSelection);
    }

    public boolean Purge() {
        for (int i = getRecordCount() - 1; i >= 0; i--) {
            ExtraGeometry localExtraGeometry = this._GeometryList.get(i);
            if (localExtraGeometry.getGeometry().getStatus() == EGeometryStatus.DELETE) {
                localExtraGeometry.setGeometry(null);
                this._GeometryList.remove(i);
                this._GeometrySYSIDList.remove(i);
            }
        }
        return BuildSpatialIndex();
    }

    public boolean QueryAllGeometryFromDB() {
        this._GeometryList.clear();
        return QueryGeometrysFromDB(null, true);
    }

    public boolean QueryGeometrysFromDB(List<String> sysIDList, boolean needUpdateAll) {
        SQLiteReader tmpReader;
        boolean result = false;
        if (!PubVar.DataSetQuerySafety) {
            return QueryGeometrysFromDB_Normal(sysIDList, needUpdateAll, true);
        }
        if (sysIDList == null && (tmpReader = getDataSource().Query("Select GROUP_CONCAT(SYS_ID) From " + getDataTableName() + " Where SYS_Status=0")) != null && tmpReader.Read()) {
            String[] tmpStrs = tmpReader.GetString(0).split(",");
            if (tmpStrs != null && tmpStrs.length > 0) {
                sysIDList = Common.StrArrayToList(tmpStrs);
            }
            tmpReader.Close();
        }
        if (sysIDList != null && sysIDList.size() > 0) {
            if (this._TotalDataCount == 0) {
                RefreshTotalCount();
            }
            BasicValue tmpBasicValue = new BasicValue();
            QueryGeometrysFromDB_Normal2(sysIDList, needUpdateAll, tmpBasicValue);
            if (sysIDList.size() > tmpBasicValue.getInt()) {
                List<String> sysIDList2 = sysIDList;
                while (sysIDList2.size() > tmpBasicValue.getInt()) {
                    int tmpI01 = tmpBasicValue.getInt();
                    if (tmpI01 == 0) {
                        tmpI01++;
                        QueryGeometryFromDBBy_SYSID(sysIDList2.get(0));
                    }
                    sysIDList2 = Common.SubStringList(sysIDList2, tmpI01);
                    if (sysIDList2.size() > 0) {
                        QueryGeometrysFromDB_Normal2(sysIDList2, needUpdateAll, tmpBasicValue);
                    }
                }
            }
            UpdateGeometrysSymbol(sysIDList);
            result = true;
        }
        return result;
    }

    public boolean QueryGeometrysFromDB_Normal(List<String> sysIDList, boolean needUpdateAll) {
        return QueryGeometrysFromDB_Normal(sysIDList, needUpdateAll, true);
    }

    public boolean QueryGeometrysFromDB_Normal(List<String> sysIDList, boolean needUpdateAll, boolean needUpdateSymbol) {
        try {
            if (this._TotalDataCount == 0) {
                RefreshTotalCount();
            }
            String tempSql = "1=1";
            if (sysIDList != null) {
                tempSql = "SYS_ID in (" + Common.CombineStrings(",", sysIDList) + ")";
            }
            String str4 = "SYS_GEO,SYS_ID,SYS_OID,SYS_LABEL";
            if (this._BindGeoLayer.getRender().getType() == EDisplayType.CLASSIFIED) {
                str4 = String.valueOf(str4) + "," + ((ClassifiedRender) this._BindGeoLayer.getRender()).getUniqueValueField() + " as UniqueValueField";
            }
            boolean tmpIsHasLabel = false;
            if (this._BindGeoLayer.getRender().getIfLabel()) {
                String tempLableField = this._BindGeoLayer.getRender().getLabelDataField().trim();
                if (!tempLableField.equals("")) {
                    String[] tempStrs = tempLableField.split(",");
                    StringBuilder tempFieldStr = new StringBuilder();
                    if (tempStrs == null || tempStrs.length <= 1) {
                        tempFieldStr.append(tempLableField);
                    } else {
                        String tmpLabelSplitChar = this._BindGeoLayer.getRender().getLabelSplitChar();
                        int length = tempStrs.length;
                        for (int i = 0; i < length; i++) {
                            tempFieldStr.append(tempStrs[i]);
                            tempFieldStr.append("||'" + tmpLabelSplitChar + "'||");
                        }
                        int tempLen = tempFieldStr.length();
                        int tmpLen2 = ("||'" + tmpLabelSplitChar + "'||").length();
                        if (tempLen > tmpLen2) {
                            tempFieldStr = tempFieldStr.delete(tempLen - tmpLen2, tempLen);
                        }
                    }
                    str4 = String.valueOf(str4) + ",(" + tempFieldStr.toString() + ") as MyLabelField ";
                    tmpIsHasLabel = true;
                }
            }
            SQLiteReader localSQLiteDataReader = getDataSource().Query("select " + str4 + " from " + getDataTableName() + " where SYS_STATUS=0 AND " + tempSql + " order by SYS_ID");
            if (localSQLiteDataReader == null) {
                return false;
            }
            int tempLabelFieldIndex = -1;
            if (tmpIsHasLabel && (tempLabelFieldIndex = localSQLiteDataReader.GetFieldIndex("MyLabelField")) < 0) {
                tmpIsHasLabel = false;
            }
            int tempTid = this._GeometryList.size();
            while (localSQLiteDataReader.Read()) {
                try {
                    int tmpSysID = localSQLiteDataReader.GetInt32(1);
                    byte[] arrayOfByte = localSQLiteDataReader.GetBlob(0);
                    if (arrayOfByte != null) {
                        AbstractGeometry localGeometry = Common.ConvertBytesToGeometry(arrayOfByte, getType(), getDataSource().getEditing());
                        if (!localGeometry.GetAllCoordinateList().get(0).IsValid()) {
                            localGeometry.setOID(localSQLiteDataReader.GetString(2));
                            localGeometry.SetSYS_ID(String.valueOf(tmpSysID));
                            localGeometry.setIndex(tempTid);
                            if (needUpdateAll) {
                                ExtraGeometry localExtraGeometry = new ExtraGeometry();
                                localExtraGeometry.setDIndex(tempTid);
                                if (tmpIsHasLabel) {
                                    localGeometry.setLabelContent(localSQLiteDataReader.GetString(tempLabelFieldIndex));
                                }
                                if (!(this._OffsetX == 0.0d && this._OffsetY == 0.0d)) {
                                    localGeometry.UpdateCoordinate(this._OffsetX, this._OffsetY);
                                }
                                localExtraGeometry.setGeometry(localGeometry);
                                AddExtraGeometry(localExtraGeometry);
                            }
                            tempTid++;
                        }
                    }
                } catch (Exception e) {
                }
            }
            localSQLiteDataReader.Close();
            if (needUpdateSymbol) {
                UpdateGeometrysSymbol(sysIDList);
            }
            if (!this.HasInitial && sysIDList == null) {
                BuildMapIndex(true, false);
            }
            this.HasInitial = true;
            return true;
        } catch (Exception ex) {
            Common.Log("QueryGeometryFromDB", "错误:" + ex.toString() + "-->" + ex.getMessage());
            return false;
        }
    }

    private boolean QueryGeometrysFromDB_Normal2(List<String> sysIDList, boolean needUpdateAll, BasicValue hasQueryCount) {
        boolean result = false;
        int tmpHasQueryCount = 0;
        SQLiteReader localSQLiteDataReader = null;
        String tempSql = "1=1";
        if (sysIDList != null) {
            try {
                tempSql = "SYS_ID in (" + Common.CombineStrings(",", sysIDList) + ")";
            } catch (Exception ex) {
                Common.Log("QueryGeometryFromDB", "错误:" + ex.toString() + "-->" + ex.getMessage());
                if (0 != 0) {
                    localSQLiteDataReader.Close();
                }
            } catch (Throwable th) {
                if (0 != 0) {
                    localSQLiteDataReader.Close();
                }
                throw th;
            }
        }
        String str4 = "SYS_ID,SYS_GEO,SYS_OID,SYS_LABEL";
        if (this._BindGeoLayer.getRender().getType() == EDisplayType.CLASSIFIED) {
            str4 = String.valueOf(str4) + "," + ((ClassifiedRender) this._BindGeoLayer.getRender()).getUniqueValueField() + " as UniqueValueField";
        }
        boolean tmpIsHasLabel = false;
        if (this._BindGeoLayer.getRender().getIfLabel()) {
            String tempLableField = this._BindGeoLayer.getRender().getLabelDataField().trim();
            if (!tempLableField.equals("")) {
                String[] tempStrs = tempLableField.split(",");
                StringBuilder tempFieldStr = new StringBuilder();
                if (tempStrs == null || tempStrs.length <= 1) {
                    tempFieldStr.append(tempLableField);
                } else {
                    String tmpLabelSplitChar = this._BindGeoLayer.getRender().getLabelSplitChar();
                    int length = tempStrs.length;
                    for (int i = 0; i < length; i++) {
                        tempFieldStr.append(tempStrs[i]);
                        tempFieldStr.append("||'" + tmpLabelSplitChar + "'||");
                    }
                    int tempLen = tempFieldStr.length();
                    int tmpLen2 = ("||'" + tmpLabelSplitChar + "'||").length();
                    if (tempLen > tmpLen2) {
                        tempFieldStr = tempFieldStr.delete(tempLen - tmpLen2, tempLen);
                    }
                }
                str4 = String.valueOf(str4) + ",(" + tempFieldStr.toString() + ") as MyLabelField ";
                tmpIsHasLabel = true;
            }
        }
        SQLiteReader localSQLiteDataReader2 = getDataSource().Query("select " + str4 + " from " + getDataTableName() + " where SYS_STATUS=0 AND " + tempSql + " order by SYS_ID");
        if (localSQLiteDataReader2 != null) {
            int tempLabelFieldIndex = -1;
            if (tmpIsHasLabel && (tempLabelFieldIndex = localSQLiteDataReader2.GetFieldIndex("MyLabelField")) < 0) {
                tmpIsHasLabel = false;
            }
            int tempTid = this._GeometryList.size();
            while (localSQLiteDataReader2.Read()) {
                int i2 = localSQLiteDataReader2.GetInt32(0);
                byte[] arrayOfByte = localSQLiteDataReader2.GetBlob(1);
                if (arrayOfByte != null) {
                    AbstractGeometry localGeometry = Common.ConvertBytesToGeometry(arrayOfByte, getType(), getDataSource().getEditing());
                    if (!localGeometry.GetAllCoordinateList().get(0).IsValid()) {
                        localGeometry.setOID(localSQLiteDataReader2.GetString(2));
                        localGeometry.SetSYS_ID(String.valueOf(i2));
                        localGeometry.setIndex(tempTid);
                        tmpHasQueryCount++;
                        if (needUpdateAll) {
                            ExtraGeometry localExtraGeometry = new ExtraGeometry();
                            localExtraGeometry.setDIndex(tempTid);
                            if (tmpIsHasLabel) {
                                localGeometry.setLabelContent(localSQLiteDataReader2.GetString(tempLabelFieldIndex));
                            }
                            if (!(this._OffsetX == 0.0d && this._OffsetY == 0.0d)) {
                                localGeometry.UpdateCoordinate(this._OffsetX, this._OffsetY);
                            }
                            localExtraGeometry.setGeometry(localGeometry);
                            AddExtraGeometry(localExtraGeometry);
                        }
                        tempTid++;
                    }
                }
            }
            localSQLiteDataReader2.Close();
            localSQLiteDataReader2 = null;
            if (!this.HasInitial) {
                BuildMapIndex(true, false);
            }
            this.HasInitial = true;
            result = true;
        }
        if (localSQLiteDataReader2 != null) {
            localSQLiteDataReader2.Close();
        }
        hasQueryCount.setValue(tmpHasQueryCount);
        return result;
    }

    public AbstractGeometry QueryGeometryFromDBBy_SYSID(String SYS_ID) {
        int i;
        AbstractGeometry result = null;
        SQLiteReader localSQLiteDataReader = null;
        try {
            String tempSql = "SYS_ID =" + SYS_ID;
            String str4 = "SYS_ID,SYS_GEO,SYS_OID,SYS_LABEL";
            if (this._BindGeoLayer.getRender().getType() == EDisplayType.CLASSIFIED) {
                str4 = String.valueOf(str4) + "," + ((ClassifiedRender) this._BindGeoLayer.getRender()).getUniqueValueField() + " as UniqueValueField";
            }
            boolean tmpIsHasLabel = false;
            if (this._BindGeoLayer.getRender().getIfLabel()) {
                String tempLableField = this._BindGeoLayer.getRender().getLabelDataField().trim();
                if (!tempLableField.equals("")) {
                    String[] tempStrs = tempLableField.split(",");
                    StringBuilder tempFieldStr = new StringBuilder();
                    if (tempStrs == null || tempStrs.length <= 1) {
                        tempFieldStr.append(tempLableField);
                    } else {
                        String tmpLabelSplitChar = this._BindGeoLayer.getRender().getLabelSplitChar();
                        int length = tempStrs.length;
                        for (int i2 = 0; i2 < length; i2++) {
                            tempFieldStr.append(tempStrs[i2]);
                            tempFieldStr.append("||'" + tmpLabelSplitChar + "'||");
                        }
                        int tempLen = tempFieldStr.length();
                        int tmpLen2 = ("||'" + tmpLabelSplitChar + "'||").length();
                        if (tempLen > tmpLen2) {
                            tempFieldStr = tempFieldStr.delete(tempLen - tmpLen2, tempLen);
                        }
                    }
                    str4 = String.valueOf(str4) + ",(" + tempFieldStr.toString() + ") as MyLabelField ";
                    tmpIsHasLabel = true;
                }
            }
            SQLiteReader localSQLiteDataReader2 = getDataSource().Query("select " + str4 + ",SYS_BZ1 from " + getDataTableName() + " where SYS_STATUS=0 AND " + tempSql + " order by SYS_ID");
            if (localSQLiteDataReader2 != null) {
                int tempLabelFieldIndex = -1;
                if (tmpIsHasLabel && (tempLabelFieldIndex = localSQLiteDataReader2.GetFieldIndex("MyLabelField")) < 0) {
                    tmpIsHasLabel = false;
                }
                if (localSQLiteDataReader2.Read()) {
                    byte[] arrayOfByte = null;
                    try {
                        i = localSQLiteDataReader2.GetInt32(0);
                        arrayOfByte = localSQLiteDataReader2.GetBlob(1);
                    } catch (Exception e) {
                        localSQLiteDataReader2 = getDataSource().Query("select " + str4.replace("SYS_GEO", "length(hex(SYS_GEO))/2 As AAA") + ",SYS_BZ1 from " + getDataTableName() + " where SYS_STATUS=0 AND " + tempSql + " order by SYS_ID");
                        if (!localSQLiteDataReader2.Read()) {
                            if (localSQLiteDataReader2 != null) {
                                localSQLiteDataReader2.Close();
                            }
                            return null;
                        }
                        i = localSQLiteDataReader2.GetInt32(0);
                        int tmpLen = localSQLiteDataReader2.GetInt32(1);
                        if (tmpLen > 0) {
                            List<byte[]> tmpBytesList = new ArrayList<>();
                            int tmpBytesCount = 0;
                            if (tmpLen > 0) {
                                int tmpFileBlockSize = 1000000;
                                for (int tmpI01 = 0; tmpI01 < tmpLen; tmpI01 += tmpFileBlockSize) {
                                    if (tmpLen - tmpI01 < tmpFileBlockSize) {
                                        tmpFileBlockSize = tmpLen - tmpI01;
                                    }
                                    SQLiteReader tmpReader = getDataSource().Query("SELECT substr(SYS_GEO," + String.valueOf(tmpI01 + 1) + "," + String.valueOf(tmpFileBlockSize) + ") As A From " + getDataTableName() + " where SYS_STATUS=0 AND SYS_ID =" + SYS_ID);
                                    if (tmpReader.Read()) {
                                        byte[] tmpBytes02 = tmpReader.GetBlob(0);
                                        if (tmpBytes02 != null && tmpBytes02.length > 0) {
                                            tmpBytesList.add(tmpBytes02);
                                            tmpBytesCount += tmpBytes02.length;
                                        }
                                        tmpReader.Close();
                                    }
                                }
                                if (tmpBytesCount > 0) {
                                    arrayOfByte = new byte[tmpBytesCount];
                                    int tid = 0;
                                    for (byte[] tmpBytes022 : tmpBytesList) {
                                        System.arraycopy(tmpBytes022, 0, arrayOfByte, tid, tmpBytes022.length);
                                        tid += tmpBytes022.length;
                                    }
                                }
                            }
                        }
                    }
                    localSQLiteDataReader2.GetFieldIndex("SYS_BZ1");
                    if (arrayOfByte != null) {
                        AbstractGeometry localGeometry = Common.ConvertBytesToGeometry(arrayOfByte, getType(), getDataSource().getEditing());
                        if (!localGeometry.GetAllCoordinateList().get(0).IsValid()) {
                            localGeometry.setOID(localSQLiteDataReader2.GetString(2));
                            localGeometry.SetSYS_ID(String.valueOf(i));
                            localGeometry.setIndex(this._GeometryList.size());
                            ExtraGeometry localExtraGeometry = new ExtraGeometry();
                            localExtraGeometry.setDIndex(this._GeometryList.size());
                            if (tmpIsHasLabel) {
                                localGeometry.setLabelContent(localSQLiteDataReader2.GetString(tempLabelFieldIndex));
                            }
                            if (!(this._OffsetX == 0.0d && this._OffsetY == 0.0d)) {
                                localGeometry.UpdateCoordinate(this._OffsetX, this._OffsetY);
                            }
                            JustUpdateGeoMapIndex(localGeometry);
                            localExtraGeometry.setGeometry(localGeometry);
                            AddExtraGeometry(localExtraGeometry);
                            List<String> tempGeosList = new ArrayList<>();
                            tempGeosList.add(String.valueOf(i));
                            UpdateGeometrysSymbol(tempGeosList);
                            result = localGeometry;
                        }
                    }
                }
                localSQLiteDataReader2.Close();
            }
            if (localSQLiteDataReader2 != null) {
                localSQLiteDataReader2.Close();
            }
        } catch (Exception ex) {
            Common.Log("QueryGeometryFromDB", "错误:" + ex.toString() + "-->" + ex.getMessage());
            if (0 != 0) {
                localSQLiteDataReader.Close();
            }
        } catch (Throwable th) {
            if (0 != 0) {
                localSQLiteDataReader.Close();
            }
            throw th;
        }
        return result;
    }

    public AbstractGeometry QueryGeometryFromDBBy_SYSIDOnly(String SYS_ID) {
        AbstractGeometry result = null;
        try {
            SQLiteReader localSQLiteDataReader = getDataSource().Query("select SYS_GEO,SYS_BZ1 from " + getDataTableName() + " where SYS_STATUS=0 AND SYS_ID ='" + SYS_ID + "'");
            if (localSQLiteDataReader != null) {
                if (localSQLiteDataReader.Read()) {
                    localSQLiteDataReader.GetFieldIndex("SYS_BZ1");
                    Integer.parseInt(SYS_ID);
                    byte[] arrayOfByte = localSQLiteDataReader.GetBlob(0);
                    if (arrayOfByte != null) {
                        AbstractGeometry localGeometry = Common.ConvertBytesToGeometry(arrayOfByte, getType(), getDataSource().getEditing());
                        if (!localGeometry.GetAllCoordinateList().get(0).IsValid()) {
                            localGeometry.SetSYS_ID(SYS_ID);
                            result = localGeometry;
                        }
                    }
                }
                localSQLiteDataReader.Close();
            }
        } catch (Exception e) {
        }
        return result;
    }

    public void UpdateLabelContent(List<Integer> list) {
        if (list == null) {
            try {
                UpdateLabelContent();
            } catch (Exception e) {
            }
        } else if (list.size() != 0 && this._BindGeoLayer.getRender().getIfLabel()) {
            String tempLableField = this._BindGeoLayer.getRender().getLabelDataField().trim();
            if (!tempLableField.equals("")) {
                StringBuilder tempFieldStr = new StringBuilder();
                String[] tempStrs = tempLableField.split(",");
                if (tempStrs != null && tempStrs.length > 0) {
                    String tmpLabelSplitChar = this._BindGeoLayer.getRender().getLabelSplitChar();
                    int length = tempStrs.length;
                    for (int i = 0; i < length; i++) {
                        tempFieldStr.append(tempStrs[i]);
                        tempFieldStr.append("||'" + tmpLabelSplitChar + "'||");
                    }
                    int tempLen = tempFieldStr.length();
                    int tmpLen2 = ("||'" + tmpLabelSplitChar + "'||").length();
                    if (tempLen > tmpLen2) {
                        tempFieldStr = tempFieldStr.delete(tempLen - tmpLen2, tempLen);
                    }
                }
                if (tempFieldStr.length() > 0) {
                    HashMap<String, String> tempValues = new HashMap<>();
                    SQLiteReader localSQLiteDataReader = getDataSource().Query("select SYS_ID, (" + tempFieldStr.toString() + ") As FieldContent from " + getDataTableName() + " where SYS_STATUS=0 AND " + ("SYS_ID in (" + Common.CombineIntegers(",", list) + ")") + " order by SYS_ID");
                    if (localSQLiteDataReader != null) {
                        while (localSQLiteDataReader.Read()) {
                            tempValues.put(String.valueOf(localSQLiteDataReader.GetInt32(0)), localSQLiteDataReader.GetString(1));
                        }
                    }
                    if (tempValues.size() > 0) {
                        for (Map.Entry<String, String> tempEntry : tempValues.entrySet()) {
                            String tempLabelStr = tempEntry.getValue();
                            if (tempLabelStr == null) {
                                tempLabelStr = "";
                            }
                            AbstractGeometry tempGeo = GetGeometryBySYSID(tempEntry.getKey());
                            if (tempGeo != null) {
                                tempGeo.setLabelContent(tempLabelStr);
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean BuildMapIndex(boolean needRefreshExtend, boolean needSaveAllIToDB) {
        int[] tempRCIndex;
        if (this._TotalDataCount == 0) {
            return false;
        }
        boolean tmpNeedSaveAll = false;
        if (needRefreshExtend) {
            boolean tempBool = true;
            if (1 != 0) {
                tempBool = false;
                try {
                    SQLiteReader tempReader = getDataSource().Query("select Min(MinX),Min(MinY),Max(MaxX),Max(MaxY) from " + getIndexTableName());
                    if (tempReader != null) {
                        if (tempReader.Read()) {
                            double tmpMinX = tempReader.GetDouble(0);
                            double tmpMinY = tempReader.GetDouble(1);
                            double tmpMaxX = tempReader.GetDouble(2);
                            double tmpMaxY = tempReader.GetDouble(3);
                            if (!(tmpMinX == tmpMaxX || tmpMaxX == tmpMaxY)) {
                                this._BindGeoLayer.setMinX(tmpMinX);
                                this._BindGeoLayer.setMinY(tmpMinY);
                                this._BindGeoLayer.setMaxX(tmpMaxX);
                                this._BindGeoLayer.setMaxY(tmpMaxY);
                                this._BindGeoLayer.updateExtend();
                                tempBool = true;
                            }
                        }
                        tempReader.Close();
                    }
                } catch (Exception e) {
                    tempBool = false;
                }
            }
            if (!tempBool) {
                Envelope tempExtend = null;
                Iterator<ExtraGeometry> tempIter01 = this._GeometryList.iterator();
                if (tempIter01.hasNext()) {
                    tempExtend = tempIter01.next().getEnvelope();
                }
                while (tempIter01.hasNext()) {
                    tempExtend = tempExtend.MergeEnvelope(tempIter01.next().getEnvelope());
                }
                if (tempExtend != null) {
                    this._BindGeoLayer.SetExtend(tempExtend);
                }
            }
            if (!this.m_MapCellIndex.getBigCell().Equal(this._BindGeoLayer.getExtend())) {
                this.m_MapCellIndex.setBigCell(this._BindGeoLayer.getExtend());
                tmpNeedSaveAll = true;
            }
        }
        for (ExtraGeometry tempExtGeo : this._GeometryList) {
            if (!(tempExtGeo == null || (tempRCIndex = this.m_MapCellIndex.CalCellIndexsOne(tempExtGeo.getEnvelope())) == null)) {
                tempExtGeo.getGeometry().SetRowIndex(tempRCIndex[0]);
                tempExtGeo.getGeometry().SetColIndex(tempRCIndex[1]);
            }
        }
        if (this._DataSource.getEditing() && this.HasInitial && (tmpNeedSaveAll || needSaveAllIToDB)) {
            SaveAllMapIndex();
        }
        return true;
    }

    public boolean BuildAllGeosMapIndex() {
        try {
            RefreshTotalCount();
            if (this._TotalDataCount == 0) {
                return false;
            }
            boolean tempBool = true;
            if (1 != 0) {
                tempBool = false;
                try {
                    SQLiteReader tempReader = getDataSource().Query("select Min(MinX),Min(MinY),Max(MaxX),Max(MaxY) from " + getIndexTableName());
                    if (tempReader != null) {
                        if (tempReader.Read()) {
                            double tmpMinX = tempReader.GetDouble(0);
                            double tmpMinY = tempReader.GetDouble(1);
                            double tmpMaxX = tempReader.GetDouble(2);
                            double tmpMaxY = tempReader.GetDouble(3);
                            if (!(tmpMinX == tmpMaxX || tmpMaxX == tmpMaxY)) {
                                this._BindGeoLayer.setMinX(tmpMinX);
                                this._BindGeoLayer.setMinY(tmpMinY);
                                this._BindGeoLayer.setMaxX(tmpMaxX);
                                this._BindGeoLayer.setMaxY(tmpMaxY);
                                this._BindGeoLayer.updateExtend();
                                tempBool = true;
                            }
                        }
                        tempReader.Close();
                    }
                } catch (Exception e) {
                    tempBool = false;
                }
            }
            if (!tempBool) {
                Envelope tempExtend = null;
                Iterator<ExtraGeometry> tempIter01 = this._GeometryList.iterator();
                if (tempIter01.hasNext()) {
                    tempExtend = tempIter01.next().getEnvelope();
                }
                while (tempIter01.hasNext()) {
                    tempExtend = tempExtend.MergeEnvelope(tempIter01.next().getEnvelope());
                }
                if (tempExtend != null) {
                    this._BindGeoLayer.SetExtend(tempExtend);
                }
            }
            if (!this.m_MapCellIndex.getBigCell().Equal(this._BindGeoLayer.getExtend())) {
                this.m_MapCellIndex.setBigCell(this._BindGeoLayer.getExtend());
            }
            List<IndexStruct> tmpList2 = new ArrayList<>();
            SQLiteDBHelper tmpSqLiteDBHelper = getDataSource()._EDatabase;
            SQLiteReader tempReader2 = tmpSqLiteDBHelper.Query("select SYS_ID,MinX,MinY,MaxX,MaxY from " + getIndexTableName());
            if (tempReader2 != null) {
                while (tempReader2.Read()) {
                    int tmpSysID = tempReader2.GetInt32(0);
                    double tmpMinX2 = tempReader2.GetDouble(1);
                    double tmpMinY2 = tempReader2.GetDouble(2);
                    double tmpMaxX2 = tempReader2.GetDouble(3);
                    double tmpMaxY2 = tempReader2.GetDouble(4);
                    int[] tempRCIndex = this.m_MapCellIndex.CalCellIndexsOne(tmpMinX2, tmpMinY2, tmpMaxX2, tmpMaxY2);
                    if (tempRCIndex != null) {
                        IndexStruct tmpIndexStruct = new IndexStruct();
                        tmpIndexStruct.SYSID = tmpSysID;
                        tmpIndexStruct.MinX = tmpMinX2;
                        tmpIndexStruct.MinY = tmpMinY2;
                        tmpIndexStruct.MaxX = tmpMaxX2;
                        tmpIndexStruct.MaxY = tmpMaxY2;
                        tmpIndexStruct.RIndex = tempRCIndex[0];
                        tmpIndexStruct.CIndex = tempRCIndex[1];
                        tmpList2.add(tmpIndexStruct);
                    }
                }
                tempReader2.Close();
            }
            int tmpTid = 0;
            if (tmpList2.size() > 0) {
                tmpSqLiteDBHelper.BeginTransaction();
                int tmpCurrentTid = 0;
                for (IndexStruct tmpIndexStruct2 : tmpList2) {
                    tmpTid++;
                    tmpCurrentTid++;
                    ContentValues cv = new ContentValues();
                    cv.put("SYS_ID", Integer.valueOf(tmpIndexStruct2.SYSID));
                    cv.put("RIndex", Integer.valueOf(tmpIndexStruct2.RIndex));
                    cv.put("CIndex", Integer.valueOf(tmpIndexStruct2.CIndex));
                    cv.put("MinX", Double.valueOf(tmpIndexStruct2.MinX));
                    cv.put("MinY", Double.valueOf(tmpIndexStruct2.MinY));
                    cv.put("MaxX", Double.valueOf(tmpIndexStruct2.MaxX));
                    cv.put("MaxY", Double.valueOf(tmpIndexStruct2.MaxY));
                    tmpSqLiteDBHelper.ReplaceWithOnConflict(this._IndexTableName, cv);
                }
                tmpSqLiteDBHelper.SetTransactionSuccessful();
                tmpSqLiteDBHelper.EndTransaction();
            }
            return true;
        } catch (Exception e2) {
        }
        return true;
    }

    class IndexStruct {
        int CIndex;
        double MaxX;
        double MaxY;
        double MinX;
        double MinY;
        int RIndex;
        int SYSID;

        IndexStruct() {
        }
    }

    public Envelope GetExtendFromDB() {
        SQLiteReader tempReader;
        try {
            if (this._IndexTableName.length() <= 0 || (tempReader = getDataSource().Query("select Min(MinX),Min(MinY),Max(MaxX),Max(MaxY) from " + getIndexTableName())) == null || !tempReader.Read()) {
                return null;
            }
            Envelope result = new Envelope(tempReader.GetDouble(0), tempReader.GetDouble(3), tempReader.GetDouble(2), tempReader.GetDouble(1));
            try {
                if (result.getWidth() <= 0.0d || result.getHeight() <= 0.0d) {
                    return result;
                }
                this.m_MapCellIndex.setBigCell(result);
                return result;
            } catch (Exception e) {
                return result;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    public boolean SaveAllMapIndex() {
        boolean result;
        if (this._GeometryList.size() > 0) {
            for (ExtraGeometry extraGeometry : this._GeometryList) {
                SaveGeoIndexToDB(extraGeometry.getGeometry());
            }
            result = true;
        } else {
            result = true;
        }
        FeatureLayer tmpLayer = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(this._Name);
        if (tmpLayer != null) {
            tmpLayer.SetExtend(this.m_MapCellIndex.getBigCell());
            tmpLayer.SaveLayerExtend();
        }
        return result;
    }

    public boolean SaveGeoIndexToDB(AbstractGeometry geometry) {
        String tempSYSID = geometry.GetSYS_ID();
        if (tempSYSID == null || tempSYSID.equals("")) {
            return false;
        }
        StringBuilder tempSB = new StringBuilder();
        tempSB.append("REPLACE INTO ");
        tempSB.append(this._IndexTableName);
        tempSB.append(" (SYS_ID,RIndex,CIndex,MinX,MinY,MaxX,MaxY) values (");
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
        return getDataSource().ExcuteSQL(tempSB.toString());
    }

    public boolean SaveGeoIndexToDB(String sysID, int RIndex, int CIndex, double minX, double minY, double maxX, double maxY) {
        if (sysID == null || sysID.equals("")) {
            return false;
        }
        return getDataSource().ExcuteSQL("REPLACE INTO " + this._IndexTableName + " (SYS_ID,RIndex,CIndex,MinX,MinY,MaxX,MaxY) values (" + sysID + "," + String.valueOf(RIndex) + "," + String.valueOf(CIndex) + "," + String.valueOf(minX) + "," + String.valueOf(minY) + "," + String.valueOf(maxX) + "," + String.valueOf(maxY) + ")");
    }

    public void UpdateMapIndex(AbstractGeometry geometry) {
        boolean tempBool = false;
        Envelope tempExtend = geometry.getEnvelope();
        if (this._GeometryList.size() == 1) {
            this.m_MapCellIndex.setBigCell(tempExtend);
            tempBool = true;
        } else if (this.m_MapCellIndex.getBigCell().Contains(tempExtend)) {
            int[] tempRCIndex = this.m_MapCellIndex.CalCellIndexsOne(tempExtend);
            if (tempRCIndex != null) {
                geometry.SetRowIndex(tempRCIndex[0]);
                geometry.SetColIndex(tempRCIndex[1]);
            }
            SaveGeoIndexToDB(geometry);
        } else {
            this.m_MapCellIndex.setBigCell(this.m_MapCellIndex.getBigCell().MergeEnvelope(tempExtend));
            tempBool = true;
        }
        if (tempBool) {
            BuildMapIndex(false, true);
        }
    }

    public void JustUpdateGeoMapIndex(AbstractGeometry geometry) {
        int[] tempRCIndex;
        if (geometry != null && (tempRCIndex = this.m_MapCellIndex.CalCellIndexsOne(geometry.getEnvelope())) != null) {
            geometry.SetRowIndex(tempRCIndex[0]);
            geometry.SetColIndex(tempRCIndex[1]);
        }
    }

    public boolean UpdateMapIndexByExtend(Envelope extend) {
        boolean tempBool;
        if (this._GeometryList.size() == 1) {
            this.m_MapCellIndex.setBigCell(extend);
            tempBool = true;
        } else if (this.m_MapCellIndex.getBigCell().Contains(extend)) {
            tempBool = false;
        } else {
            this.m_MapCellIndex.setBigCell(this.m_MapCellIndex.getBigCell().MergeEnvelope(extend));
            tempBool = true;
        }
        if (tempBool) {
            BuildMapIndex(false, true);
        }
        return tempBool;
    }

    public void UpdateAllGeometrysSymbol() {
        if (this._GeometryList.size() != 0) {
            IRender tempRender = this._BindGeoLayer.getRender();
            if (tempRender.getType() == EDisplayType.SIMPLE) {
                for (ExtraGeometry tempExtraGeo : this._GeometryList) {
                    if (!(tempExtraGeo == null || tempExtraGeo.getGeometry() == null)) {
                        tempRender.UpdateSymbol(tempExtraGeo.getGeometry());
                    }
                }
            } else if (tempRender.getType() == EDisplayType.CLASSIFIED) {
                ClassifiedRender tempRender2 = (ClassifiedRender) tempRender;
                String tempFields = Common.CombineStrings("||','||", tempRender2.getUniqueValueField());
                if (!tempFields.equals("")) {
                    List<Integer> tmpList = new ArrayList<>();
                    List<String> tmpList2 = new ArrayList<>();
                    SQLiteReader localSQLiteDataReader = getDataSource().Query("Select (" + tempFields + ") AS FValue,GROUP_CONCAT(SYS_ID) AS MyID From " + getDataTableName() + " Group By FValue ");
                    if (localSQLiteDataReader != null) {
                        while (localSQLiteDataReader.Read()) {
                            String tempValue = localSQLiteDataReader.GetString(0);
                            String tempIDs = localSQLiteDataReader.GetString(1);
                            int tempIndex = tempRender2.findSymbolIndexByValue(tempValue);
                            if (tempIndex >= 0) {
                                tmpList.add(Integer.valueOf(tempIndex));
                                tmpList2.add("," + tempIDs + ",");
                            }
                        }
                        localSQLiteDataReader.Close();
                    }
                    boolean tempTag001 = true;
                    if (tmpList2.size() == 0) {
                        tempTag001 = false;
                    }
                    for (ExtraGeometry tempExtraGeo2 : this._GeometryList) {
                        if (!(tempExtraGeo2 == null || tempExtraGeo2.getGeometry() == null)) {
                            boolean tempBool01 = false;
                            if (tempTag001) {
                                String tempIndex2 = "," + tempExtraGeo2.getGeometry().GetSYS_ID() + ",";
                                int tempTid = 0;
                                Iterator<String> tempIter2 = tmpList2.iterator();
                                while (true) {
                                    if (!tempIter2.hasNext()) {
                                        break;
                                    } else if (tempIter2.next().contains(tempIndex2)) {
                                        tempRender2.UpdateSymbol(tempExtraGeo2.getGeometry(), tmpList.get(tempTid).intValue());
                                        tempBool01 = true;
                                        break;
                                    } else {
                                        tempTid++;
                                    }
                                }
                            }
                            if (!tempBool01) {
                                tempRender2.UpdateSymbol(tempExtraGeo2.getGeometry(), -1);
                            }
                        }
                    }
                }
            }
        }
    }

    public void UpdateGeometrysSymbol(List<String> sysIDList) {
        AbstractGeometry tempGeo;
        if (sysIDList == null) {
            try {
                UpdateAllGeometrysSymbol();
            } catch (Exception e) {
            }
        } else if (sysIDList.size() != 0) {
            IRender tempRender = this._BindGeoLayer.getRender();
            if (tempRender.getType() == EDisplayType.SIMPLE) {
                for (ExtraGeometry tmpExtGeo : this._GeometryList) {
                    AbstractGeometry tempGeo2 = tmpExtGeo.getGeometry();
                    if (tempGeo2 != null && sysIDList.contains(tempGeo2.GetSYS_ID())) {
                        tempRender.UpdateSymbol(tempGeo2);
                    }
                }
            } else if (tempRender.getType() == EDisplayType.CLASSIFIED) {
                ClassifiedRender tempRender2 = (ClassifiedRender) tempRender;
                String tempFields = Common.CombineStrings("||','||", tempRender2.getUniqueValueField());
                if (!tempFields.equals("")) {
                    List<Integer> tmpList = new ArrayList<>();
                    List<String> tmpList2 = new ArrayList<>();
                    SQLiteReader localSQLiteDataReader = getDataSource().Query("Select (" + tempFields + ") AS FValue,GROUP_CONCAT(SYS_ID) AS MyID From " + getDataTableName() + " Where SYS_ID in (" + Common.CombineStrings(",", sysIDList) + ") Group By FValue ");
                    if (localSQLiteDataReader != null) {
                        while (localSQLiteDataReader.Read()) {
                            String tempValue = localSQLiteDataReader.GetString(0);
                            String tempIDs = localSQLiteDataReader.GetString(1);
                            int tempIndex = tempRender2.findSymbolIndexByValue(tempValue);
                            if (tempIndex >= 0) {
                                tmpList.add(Integer.valueOf(tempIndex));
                                tmpList2.add("," + tempIDs + ",");
                            }
                        }
                        localSQLiteDataReader.Close();
                    }
                    boolean tempTag001 = true;
                    if (tmpList2.size() == 0) {
                        tempTag001 = false;
                    }
                    for (String tmpSysID : sysIDList) {
                        int tmpIndex02 = this._GeometrySYSIDList.indexOf(Integer.valueOf(Integer.parseInt(tmpSysID)));
                        if (tmpIndex02 > -1 && (tempGeo = this._GeometryList.get(tmpIndex02).getGeometry()) != null) {
                            boolean tempBool01 = false;
                            if (tempTag001) {
                                String tempIndex2 = "," + tmpSysID + ",";
                                int tempTid = 0;
                                Iterator<String> tempIter2 = tmpList2.iterator();
                                while (true) {
                                    if (!tempIter2.hasNext()) {
                                        break;
                                    } else if (tempIter2.next().contains(tempIndex2)) {
                                        tempRender2.UpdateSymbol(tempGeo, tmpList.get(tempTid).intValue());
                                        tempBool01 = true;
                                        break;
                                    } else {
                                        tempTid++;
                                    }
                                }
                            }
                            if (!tempBool01) {
                                tempRender2.UpdateSymbol(tempGeo, -1);
                            }
                        }
                    }
                }
            }
        }
    }

    public void UpdateLabelContent() {
        String tempLabelStr;
        try {
            if (this._BindGeoLayer.getRender().getIfLabel()) {
                String tempLableField = this._BindGeoLayer.getRender().getLabelDataField().trim();
                if (!tempLableField.equals("")) {
                    String tmpLabelSplitChar = ",";
                    StringBuilder tempFieldStr = new StringBuilder();
                    String[] tempStrs = tempLableField.split(",");
                    if (tempStrs != null && tempStrs.length > 0) {
                        tmpLabelSplitChar = this._BindGeoLayer.getRender().getLabelSplitChar();
                        int length = tempStrs.length;
                        for (int i = 0; i < length; i++) {
                            tempFieldStr.append(tempStrs[i]);
                            tempFieldStr.append("||'" + tmpLabelSplitChar + "'||");
                        }
                        int tempLen = tempFieldStr.length();
                        int tmpLen2 = ("||'" + tmpLabelSplitChar + "'||").length();
                        if (tempLen > tmpLen2) {
                            tempFieldStr = tempFieldStr.delete(tempLen - tmpLen2, tempLen);
                        }
                    }
                    if (tempFieldStr.length() > 0) {
                        List<String> tempGeoList = new ArrayList<>();
                        for (ExtraGeometry tempExtraGeo : this._GeometryList) {
                            if (!(tempExtraGeo == null || tempExtraGeo.getGeometry() == null)) {
                                tempGeoList.add(tempExtraGeo.getGeometry().GetSYS_ID());
                            }
                        }
                        if (tempGeoList.size() > 0) {
                            HashMap<String, String> tempValues = new HashMap<>();
                            SQLiteReader localSQLiteDataReader = getDataSource().Query("select SYS_ID, (" + tempFieldStr.toString() + ") As FieldContent from " + getDataTableName() + " where SYS_STATUS=0 AND " + ("SYS_ID in (" + Common.CombineStrings(",", tempGeoList) + ")") + " order by SYS_ID");
                            if (localSQLiteDataReader != null) {
                                while (localSQLiteDataReader.Read()) {
                                    tempValues.put(String.valueOf(localSQLiteDataReader.GetInt32(0)), localSQLiteDataReader.GetString(1));
                                }
                            }
                            if (tempValues.size() > 0) {
                                String tmpLabelSplitChar2 = String.valueOf(tmpLabelSplitChar) + tmpLabelSplitChar;
                                for (ExtraGeometry tempExtraGeo2 : this._GeometryList) {
                                    if (!(tempExtraGeo2 == null || tempExtraGeo2.getGeometry() == null)) {
                                        String tempSYSID = tempExtraGeo2.getGeometry().GetSYS_ID();
                                        if (tempValues.containsKey(tempSYSID)) {
                                            String tempLabelStr2 = tempValues.get(tempSYSID);
                                            if (tempLabelStr2 == null) {
                                                tempLabelStr = "";
                                            } else {
                                                tempLabelStr = tempLabelStr2.replace(tmpLabelSplitChar2, tmpLabelSplitChar);
                                            }
                                            if (tempLabelStr.equals(tmpLabelSplitChar)) {
                                                tempLabelStr = "";
                                            }
                                            tempExtraGeo2.getGeometry().setLabelContent(tempLabelStr);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void SaveGeoLabelContent() {
        try {
            if (this._BindGeoLayer.getRender().getIfLabel()) {
                SQLiteDBHelper tempSqlDatabase = getDataSource()._EDatabase;
                tempSqlDatabase.BeginTransaction();
                try {
                    for (ExtraGeometry tempExtraGeo : this._GeometryList) {
                        if (!(tempExtraGeo == null || tempExtraGeo.getGeometry() == null)) {
                            AbstractGeometry tempGeo = tempExtraGeo.getGeometry();
                            String tempLabelContent = tempGeo.getLabelContent();
                            if (tempLabelContent == null) {
                                tempLabelContent = "";
                            }
                            tempSqlDatabase.ExecuteSQL("Update " + getDataTableName() + " Set SYS_LABEL='" + tempLabelContent + "' Where SYS_ID=" + tempGeo.GetSYS_ID());
                        }
                    }
                    tempSqlDatabase.SetTransactionSuccessful();
                } catch (Exception e) {
                } finally {
                    tempSqlDatabase.EndTransaction();
                }
            }
        } catch (Exception ex) {
            Common.Log("SaveGeoLabelContent", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    public boolean QueryWithExtend(Envelope extend, Selection paramSelection) {
        if (extend == null || this._TotalDataCount == 0) {
            return false;
        }
        try {
            List<String> tempList = new ArrayList<>();
            Envelope tmpMapCellExtent = this.m_MapCellIndex.getBigCell2();
            if (tmpMapCellExtent == null) {
                GetExtendFromDB();
            }
            if (!tmpMapCellExtent.Intersect(extend)) {
                return true;
            }
            SQLiteReader tempReader = this._DataSource.Query("Select SYS_ID From " + this._IndexTableName + " Where (" + this.m_MapCellIndex.CalCellIndex(extend) + ") AND not (max(minx," + extend.getMinX() + ")>min(maxx," + extend.getMaxX() + ") or max(miny," + extend.getMinY() + ")>min(maxy," + extend.getMaxY() + "))");
            if (tempReader != null) {
                while (tempReader.Read()) {
                    tempList.add(String.valueOf(tempReader.GetInt32(0)));
                }
                tempReader.Close();
            }
            if (tempList.size() > 0) {
                List<String> tmpHasNoList = new ArrayList<>();
                if (this._GeometryList.size() > 0) {
                    for (String str : tempList) {
                        String tempSYSID = String.valueOf(str);
                        int tmpIndex02 = this._GeometrySYSIDList.indexOf(Integer.valueOf(Integer.parseInt(tempSYSID)));
                        if (tmpIndex02 >= 0) {
                            AbstractGeometry localGeometry2 = this._GeometryList.get(tmpIndex02).getGeometry();
                            if (localGeometry2 == null || localGeometry2.getStatus() == EGeometryStatus.DELETE) {
                                tmpHasNoList.add(tempSYSID);
                            } else {
                                if (localGeometry2.GetColIndex() == 0 && localGeometry2.GetRowIndex() == 0) {
                                    JustUpdateGeoMapIndex(localGeometry2);
                                }
                                paramSelection.Add(localGeometry2.getIndex());
                            }
                        } else {
                            tmpHasNoList.add(tempSYSID);
                        }
                    }
                } else {
                    tmpHasNoList = tempList;
                }
                if (tmpHasNoList.size() > 0) {
                    int tmpCurrentTid = this._GeometryList.size();
                    if (QueryGeometrysFromDB(tmpHasNoList, true)) {
                        Iterator<ExtraGeometry> tempIter02 = this._GeometryList.iterator();
                        while (tmpCurrentTid > 0) {
                            tmpCurrentTid--;
                            tempIter02.next();
                        }
                        while (tempIter02.hasNext()) {
                            ExtraGeometry tempGeo = tempIter02.next();
                            String tempSYSID2 = tempGeo.getGeometry().GetSYS_ID();
                            if (tempList.contains(tempSYSID2)) {
                                tempList.remove(tempSYSID2);
                                AbstractGeometry localGeometry22 = tempGeo.getGeometry();
                                if (!(localGeometry22 == null || localGeometry22.getStatus() == EGeometryStatus.DELETE)) {
                                    if (localGeometry22.GetColIndex() == 0 && localGeometry22.GetRowIndex() == 0) {
                                        JustUpdateGeoMapIndex(localGeometry22);
                                    }
                                    paramSelection.Add(localGeometry22);
                                }
                            }
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
        }
        return true;
    }

    public boolean QueryWithSelEnvelope(Envelope extend, Selection _selection, Selection _showSelection, boolean isMustContain) {
        if (extend == null || this._GeometryList.size() == 0) {
            return false;
        }
        try {
            List<Integer> tempCellExtList = this.m_MapCellIndex.CalCellIndexExtend(extend);
            List<Integer> tempList = new ArrayList<>();
            if (tempCellExtList.size() > 0) {
                for (Integer num : _showSelection.getGeometryIndexList()) {
                    AbstractGeometry tempGeo = GetGeometry(num.intValue());
                    if (Common.CellIndexContain(tempCellExtList, tempGeo.GetRowIndex(), tempGeo.GetColIndex())) {
                        tempList.add(Integer.valueOf(tempGeo.getIndex()));
                    }
                }
            }
            if (tempList.size() <= 0) {
                return false;
            }
            List<Integer> tmpLastResult = new ArrayList<>();
            List<String> tmpIsErrorGeoList = new ArrayList<>();
            for (Integer num2 : tempList) {
                int tempI = num2.intValue();
                if (InViewWindow(extend, tempI)) {
                    if (GetGeometry(tempI) != null) {
                        tmpLastResult.add(Integer.valueOf(tempI));
                    } else if (tmpIsErrorGeoList.indexOf(Integer.valueOf(tempI)) < 0) {
                        tmpIsErrorGeoList.add(String.valueOf(tempI));
                    }
                }
            }
            if (tmpLastResult.size() <= 0) {
                return false;
            }
            AbstractGeometry selectExtendPoly = extend.ConvertToPolyline();
            for (Integer num3 : tmpLastResult) {
                int i = num3.intValue();
                AbstractGeometry localGeometry = GetGeometry(i);
                if (!(localGeometry == null || _showSelection.getGeometryIndexList().indexOf(Integer.valueOf(i)) < 0 || localGeometry.getStatus() == EGeometryStatus.DELETE)) {
                    if (this._Type == EGeoLayerType.POINT) {
                        if (extend.ContainsPoint(((Point) localGeometry).GetPart(0))) {
                            _selection.Add(localGeometry);
                        }
                    } else if (this._Type == EGeoLayerType.POLYLINE) {
                        Polyline tempPolyline = (Polyline) localGeometry;
                        if (extend.Contains(tempPolyline.getEnvelope())) {
                            _selection.Add(localGeometry);
                        } else if (!isMustContain && tempPolyline.getSpatialRelation().JudgeIntersect(selectExtendPoly)) {
                            _selection.Add(localGeometry);
                        }
                    } else if (this._Type == EGeoLayerType.POLYGON) {
                        Polygon tempPolygon = (Polygon) localGeometry;
                        if (extend.Contains(tempPolygon.getEnvelope())) {
                            _selection.Add(localGeometry);
                        } else if (!isMustContain && tempPolygon.IsIntersectWithGeoemtry(selectExtendPoly)) {
                            _selection.Add(localGeometry);
                        }
                    }
                }
            }
            return false;
        } catch (Exception ex) {
            Common.Log("DS_QWSE", ex.getMessage());
            return false;
        }
    }

    public boolean Remove() {
        this._DataSource.getDatasets().remove(this);
        this._DataSource.ExcuteSQL("Drop Table " + getDataTableName());
        this._DataSource.ExcuteSQL("Drop Table " + getIndexTableName());
        return true;
    }

    public boolean UpdateGeometryStatusForLKD(AbstractGeometry paramGeometry) {
        int i = 0;
        if (paramGeometry.getStatus() == EGeometryStatus.DELETE) {
            i = 1;
        }
        return this._DataSource.ExcuteSQL("Update " + getDataTableName() + " Set SYS_STATUS=" + String.valueOf(i) + " where [SYS_ID] =" + String.valueOf(paramGeometry.getIndex() + 1));
    }

    public GeoLayer getBindGeoLayer() {
        return this._BindGeoLayer;
    }

    public DataSource getDataSource() {
        return this._DataSource;
    }

    public boolean getEdited() {
        return this._Edited;
    }

    public String getName() {
        return this._Name;
    }

    public List<Integer> getPurgeObjectIndexList() {
        return this._PurgeObjectIndexList;
    }

    public int getRecordCount() {
        return this._GeometryList.size();
    }

    public List<ExtraGeometry> getGeometryList() {
        return this._GeometryList;
    }

    public String getLayerName() {
        return this._LayerName;
    }

    public String getDataTableName() {
        return this._DataTableName;
    }

    public String getIndexTableName() {
        return this._IndexTableName;
    }

    public List<DataTableField> getTableStruct() {
        if (this._TableStruct == null) {
            this._TableStruct = this._DataSource.GetTableStruct(getDataTableName());
        }
        return this._TableStruct;
    }

    public EGeoLayerType getType() {
        return this._Type;
    }

    public void setBindGeoLayer(GeoLayer paramGeoLayer) {
        this._BindGeoLayer = paramGeoLayer;
    }

    public void setEdited(boolean paramBoolean) {
        this._Edited = paramBoolean;
    }

    public void setName(String paramString) {
        this._Name = paramString;
        this._DataTableName = String.valueOf(paramString) + "_D";
        this._IndexTableName = String.valueOf(paramString) + "_I";
        RefreshTotalCount();
    }

    public int getTotalCount() {
        return this._TotalDataCount;
    }

    public void RefreshTotalCount() {
        SQLiteReader tempReader;
        this._TotalDataCount = 0;
        if (this._DataSource != null && (tempReader = this._DataSource.GetSQLiteDatabase().Query("Select Count(SYS_ID) From " + this._DataTableName + " Where SYS_STATUS=0")) != null && tempReader.Read()) {
            this._TotalDataCount = tempReader.GetInt32(0);
            tempReader.Close();
        }
    }

    public int getValidTotalCount() {
        SQLiteReader tempReader;
        if (this._DataSource == null || (tempReader = this._DataSource.GetSQLiteDatabase().Query("Select Count(SYS_ID) From " + this._DataTableName + " Where SYS_STATUS=0")) == null || !tempReader.Read()) {
            return 0;
        }
        int result = tempReader.GetInt32(0);
        tempReader.Close();
        return result;
    }

    public void setLayerName(String layerName) {
        this._LayerName = layerName;
    }

    public void setType(EGeoLayerType paramlkGeoLayerType) {
        this._Type = paramlkGeoLayerType;
    }

    public void SetOffset(double biasX, double biasY) {
        this._OffsetX = biasX;
        this._OffsetY = biasY;
        for (ExtraGeometry extraGeometry : this._GeometryList) {
            extraGeometry.getGeometry().UpdateCoordinate(biasX, biasY);
        }
    }

    public List<String> getGeometryFieldValues(String sys_ID, boolean hasBasicProp) {
        List<String> result = new ArrayList<>();
        if (sys_ID != null) {
            try {
                if (!sys_ID.equals("")) {
                    StringBuilder tempSB = new StringBuilder();
                    tempSB.append("select ");
                    if (hasBasicProp) {
                        tempSB.append("SYS_STATUS,SYS_TYPE,SYS_OID,SYS_LABEL,SYS_DATE,SYS_PHOTO,SYS_Length,SYS_Area,SYS_BZ1,SYS_BZ3,SYS_BZ3,SYS_BZ4,SYS_BZ5");
                    }
                    for (int i = 1; i < 256; i++) {
                        tempSB.append(",F" + i);
                    }
                    tempSB.append(" From ");
                    tempSB.append(getDataTableName());
                    tempSB.append(" Where SYS_ID=");
                    tempSB.append(sys_ID);
                    SQLiteReader tempReader = getDataSource().Query(tempSB.toString());
                    if (tempReader != null && tempReader.Read()) {
                        int tid = 0;
                        if (hasBasicProp) {
                            int tid2 = 0 + 1;
                            result.add("SYS_STATUS=" + tempReader.GetInt32(0));
                            int tid3 = tid2 + 1;
                            result.add("SYS_TYPE='" + tempReader.GetString(tid2) + "'");
                            int tid4 = tid3 + 1;
                            result.add("SYS_OID='" + tempReader.GetString(tid3) + "'");
                            int tid5 = tid4 + 1;
                            result.add("SYS_LABEL='" + tempReader.GetString(tid4) + "'");
                            int tid6 = tid5 + 1;
                            result.add("SYS_DATE='" + tempReader.GetString(tid5) + "'");
                            int tid7 = tid6 + 1;
                            result.add("SYS_PHOTO='" + tempReader.GetString(tid6) + "'");
                            int tid8 = tid7 + 1;
                            result.add("SYS_Length=" + tempReader.GetDouble(tid7));
                            int tid9 = tid8 + 1;
                            result.add("SYS_Area=" + tempReader.GetDouble(tid8));
                            int tid10 = tid9 + 1;
                            result.add("SYS_BZ1='" + tempReader.GetString(tid9) + "'");
                            int tid11 = tid10 + 1;
                            result.add("SYS_BZ2='" + tempReader.GetString(tid10) + "'");
                            int tid12 = tid11 + 1;
                            result.add("SYS_BZ3='" + tempReader.GetString(tid11) + "'");
                            int tid13 = tid12 + 1;
                            result.add("SYS_BZ4='" + tempReader.GetString(tid12) + "'");
                            result.add("SYS_BZ5='" + tempReader.GetString(tid13) + "'");
                            tid = tid13 + 1;
                        }
                        int i2 = 1;
                        int tid14 = tid;
                        while (i2 < 256) {
                            int tid15 = tid14 + 1;
                            result.add("F" + i2 + "='" + tempReader.GetString(tid14) + "'");
                            i2++;
                            tid14 = tid15;
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return result;
    }

    public List<HashMap<String, Object>> getGeometryFieldValues(AbstractGeometry geometry, List<String> fieldNamesList) {
        List<HashMap<String, Object>> result = new ArrayList<>();
        if (geometry == null) {
            return result;
        }
        try {
            if (geometry.GetSYS_ID() != null) {
                return getGeometryFieldValues(geometry.GetSYS_ID(), fieldNamesList);
            }
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    public List<HashMap<String, Object>> getGeometryFieldValues(String SYSID, List<String> fieldNamesList) {
        FeatureLayer tmpLayer;
        String[] tmpFileds;
        List<HashMap<String, Object>> result = new ArrayList<>();
        if (SYSID != null) {
            try {
                if (!SYSID.equals("") && (tmpLayer = PubVar._PubCommand.m_ProjectDB.GetLayerInDataSource(this._DataSource.getName(), this._Name)) != null) {
                    StringBuilder tempSB = new StringBuilder();
                    boolean tmpBool = false;
                    if (fieldNamesList == null || fieldNamesList.size() == 0) {
                        tempSB.append(tmpLayer.GetDataFieldsString());
                        tmpBool = true;
                    } else {
                        for (String str : fieldNamesList) {
                            String tmpFID = tmpLayer.GetDataFieldByFieldName(str);
                            if (!tmpFID.equals("")) {
                                tempSB.append(tmpFID);
                                tempSB.append(",");
                                tmpBool = true;
                            }
                        }
                        if (tmpBool) {
                            tempSB = tempSB.deleteCharAt(tempSB.length() - 1);
                        }
                    }
                    if (tmpBool && (tmpFileds = tempSB.toString().split(",")) != null && tmpFileds.length > 0) {
                        SQLiteReader tempReader = getDataSource().Query("select " + ((CharSequence) tempSB) + " From " + getDataTableName() + " Where SYS_ID=" + SYSID);
                        if (tempReader != null && tempReader.Read()) {
                            int tid = 0;
                            int length = tmpFileds.length;
                            for (int i = 0; i < length; i++) {
                                String tmpFieldID = tmpFileds[i];
                                HashMap<String, Object> tempHash = new HashMap<>();
                                tempHash.put("name", tmpLayer.GetFieldNameByDataFieldName(tmpFieldID));
                                tempHash.put("fieldID", tmpFieldID);
                                tid++;
                                tempHash.put("value", tempReader.GetString(tid));
                                result.add(tempHash);
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return result;
    }

    public HashMap<String, Object> getGeometryFieldValuesBySYSID(String SYSID, List<String> fieldNamesList) {
        FeatureLayer tmpLayer;
        String[] tmpFileds;
        HashMap<String, Object> result = new HashMap<>();
        if (SYSID != null) {
            try {
                if (!SYSID.equals("") && (tmpLayer = PubVar._PubCommand.m_ProjectDB.GetLayerInDataSource(this._DataSource.getName(), this._Name)) != null) {
                    StringBuilder tempSB = new StringBuilder();
                    boolean tmpBool = false;
                    if (fieldNamesList == null || fieldNamesList.size() == 0) {
                        tempSB.append(tmpLayer.GetDataFieldsString());
                        tmpBool = true;
                    } else {
                        for (String str : fieldNamesList) {
                            String tmpFID = tmpLayer.GetDataFieldByFieldName(str);
                            if (!tmpFID.equals("")) {
                                tempSB.append(tmpFID);
                                tempSB.append(",");
                                tmpBool = true;
                            }
                        }
                        if (tmpBool) {
                            tempSB = tempSB.deleteCharAt(tempSB.length() - 1);
                        }
                    }
                    if (tmpBool && (tmpFileds = tempSB.toString().split(",")) != null && tmpFileds.length > 0) {
                        SQLiteReader tempReader = getDataSource().Query("select " + ((CharSequence) tempSB) + " From " + getDataTableName() + " Where SYS_ID=" + SYSID);
                        if (tempReader != null && tempReader.Read()) {
                            int length = tmpFileds.length;
                            int i = 0;
                            int tid = 0;
                            while (i < length) {
                                int tid2 = tid + 1;
                                result.put(tmpLayer.GetFieldNameByDataFieldName(tmpFileds[i]), tempReader.GetString(tid));
                                i++;
                                tid = tid2;
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return result;
    }

    public static void SaveNewPolygon(Polygon polygon, List<String> geoFieldValues, DataSet pDataset, String layerID) {
        try {
            BaseDataObject tmpBaseObject = new BaseDataObject();
            tmpBaseObject.SetBaseObjectRelateLayerID(layerID);
            tmpBaseObject.SetSYS_TYPE("编辑面");
            int tempSYS_ID = tmpBaseObject.SaveFieldsAndGeoToDb(polygon, geoFieldValues);
            if (tempSYS_ID >= 0) {
                tmpBaseObject.SetSYS_ID(tempSYS_ID);
                polygon.SetSYS_ID(String.valueOf(tempSYS_ID));
                polygon.CalEnvelope();
                pDataset.JustUpdateGeoMapIndex(polygon);
                pDataset.SaveGeoIndexToDB(polygon);
            }
            polygon.SetEdited(false);
        } catch (Exception e) {
        }
    }

    public static void SaveNewPolyline(Polyline polyline, List<String> geoFieldValues, DataSet pDataset, String layerID) {
        try {
            BaseDataObject tmpBaseObject = new BaseDataObject();
            tmpBaseObject.SetBaseObjectRelateLayerID(layerID);
            tmpBaseObject.SetSYS_TYPE("编辑线");
            int tempSYS_ID = tmpBaseObject.SaveFieldsAndGeoToDb(polyline, geoFieldValues);
            if (tempSYS_ID >= 0) {
                tmpBaseObject.SetSYS_ID(tempSYS_ID);
                polyline.SetSYS_ID(String.valueOf(tempSYS_ID));
                polyline.CalEnvelope();
                pDataset.JustUpdateGeoMapIndex(polyline);
                pDataset.SaveGeoIndexToDB(polyline);
            }
            polyline.SetEdited(false);
        } catch (Exception e) {
        }
    }

    public boolean Save(String[] outMsg) {
        String tempSYSIDs;
        boolean result = false;
        StringBuilder tempOutMsg = new StringBuilder();
        try {
            if (getEdited() && getRecordCount() > 0) {
                for (ExtraGeometry pExtraGeometry : getGeometryList()) {
                    if (pExtraGeometry.getGeometry() != null) {
                        AbstractGeometry pGeometry = pExtraGeometry.getGeometry();
                        if (pGeometry.getStatus() == EGeometryStatus.NONE && pGeometry.GetEdited()) {
                            if (UpdateGeometryData(Common.ConvertGeoToBytes(pGeometry), pGeometry.GetSYS_ID())) {
                                pGeometry.SetEdited(false);
                            } else {
                                tempOutMsg.append("[保存矢量数据错误]:" + getLayerName() + ",第" + pGeometry.GetSYS_ID() + "个.\r\n");
                            }
                        }
                    }
                }
                SQLiteReader tempReader = this._DataSource.Query("Select GROUP_CONCAT(SYS_ID) AS MyID From " + getDataTableName() + " Where SYS_STATUS=1 or SYS_GEO is null");
                if (tempReader != null && tempReader.Read() && (tempSYSIDs = tempReader.GetString(0)) != null && !tempSYSIDs.equals("")) {
                    this._DataSource.ExcuteSQL("delete from " + getDataTableName() + " where SYS_ID in (" + tempSYSIDs + ")");
                    this._DataSource.ExcuteSQL("delete from " + getIndexTableName() + " where SYS_ID in (" + tempSYSIDs + ")");
                    Purge();
                }
            }
            setEdited(false);
            result = true;
        } catch (Exception ex) {
            tempOutMsg.append("[保存矢量数据错误]:" + ex.getMessage() + "\r\n");
        }
        if (outMsg != null && outMsg.length > 0) {
            outMsg[0] = tempOutMsg.toString();
        }
        return result;
    }

    public boolean UpdateGeometryData(byte[] geoBytes, String sysID) {
        try {
            this._DataSource.GetSQLiteDatabase().ExecuteSQL("update " + this._DataTableName + " set SYS_GEO=? where SYS_ID=" + sysID, new Object[]{geoBytes});
            return true;
        } catch (Error e) {
            return false;
        }
    }

    public List<HashMap<String, String>> getFieldsValueByCondition(String FIDString, String condition) {
        List<HashMap<String, String>> result = new ArrayList<>();
        try {
            String[] tmpFIDs = FIDString.split(",");
            if (tmpFIDs != null && tmpFIDs.length > 0) {
                int count = tmpFIDs.length;
                String tmpSql = "Select SYS_ID," + FIDString + " From " + this._DataTableName;
                if (condition != null && condition.length() > 0) {
                    tmpSql = String.valueOf(tmpSql) + " Where " + condition;
                }
                SQLiteReader tmpSqLiteReader = this._DataSource.GetSQLiteDatabase().Query(tmpSql);
                if (tmpSqLiteReader != null) {
                    while (tmpSqLiteReader.Read()) {
                        HashMap<String, String> tmpHashMap = new HashMap<>();
                        tmpHashMap.put("SYS_ID", String.valueOf(tmpSqLiteReader.GetInt32(0)));
                        for (int i = 0; i < count; i++) {
                            tmpHashMap.put(tmpFIDs[i], tmpSqLiteReader.GetString(i + 1));
                        }
                        result.add(tmpHashMap);
                    }
                    tmpSqLiteReader.Close();
                }
            }
        } catch (Error e) {
        }
        return result;
    }

    public boolean Remove(String SYS_ID) {
        try {
            this._DataSource.ExcuteSQL("Delete From " + this._DataTableName + " Where SYS_ID=" + SYS_ID);
            this._DataSource.ExcuteSQL("Delete From " + this._IndexTableName + " Where SYS_ID=" + SYS_ID);
            int tmpTid = -1;
            int tmpGeoIndex = -1;
            Iterator<ExtraGeometry> tempIter = this._GeometryList.iterator();
            while (true) {
                if (tempIter.hasNext()) {
                    tmpTid++;
                    if (tempIter.next().getGeometry().GetSYS_ID().equals(SYS_ID)) {
                        tmpGeoIndex = tmpTid;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (tmpGeoIndex != -1) {
                this._GeometryList.remove(tmpGeoIndex);
                this._GeometrySYSIDList.remove(tmpGeoIndex);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean Remove(List<String> mSys_IDList) {
        if (mSys_IDList == null || mSys_IDList.size() <= 0) {
            return false;
        }
        try {
            String tmpSYSIDString = Common.CombineStrings(",", mSys_IDList);
            this._DataSource.ExcuteSQL("Delete From " + this._DataTableName + " Where SYS_ID in (" + tmpSYSIDString + ")");
            this._DataSource.ExcuteSQL("Delete From " + this._IndexTableName + " Where SYS_ID IN (" + tmpSYSIDString + ")");
            int tmpTid = -1;
            int tmpGeoIndex = -1;
            String tmpSYSIDString2 = String.valueOf(tmpSYSIDString) + ",";
            Iterator<ExtraGeometry> tempIter = this._GeometryList.iterator();
            while (true) {
                if (tempIter.hasNext()) {
                    tmpTid++;
                    if (tmpSYSIDString2.contains(String.valueOf(tempIter.next().getGeometry().GetSYS_ID()) + ",")) {
                        tmpGeoIndex = tmpTid;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (tmpGeoIndex != -1) {
                this._GeometryList.remove(tmpGeoIndex);
                this._GeometrySYSIDList.remove(tmpGeoIndex);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void removeGeometry(String sysID) {
        int tmpTid = -1;
        int tmpGeoIndex = -1;
        Iterator<ExtraGeometry> tempIter = this._GeometryList.iterator();
        while (true) {
            if (tempIter.hasNext()) {
                tmpTid++;
                if (tempIter.next().getGeometry().GetSYS_ID().equals(sysID)) {
                    tmpGeoIndex = tmpTid;
                    break;
                }
            } else {
                break;
            }
        }
        if (tmpGeoIndex != -1) {
            this._GeometryList.remove(tmpGeoIndex);
            this._GeometrySYSIDList.remove(tmpGeoIndex);
        }
    }

    public List<Integer> GetAllGeometrysSYSIDList() {
        List<Integer> result = new ArrayList<>();
        SQLiteReader tmpSqLiteReader = this._DataSource.GetSQLiteDatabase().Query("Select SYS_ID FROM " + getDataTableName());
        if (tmpSqLiteReader != null) {
            while (tmpSqLiteReader.Read()) {
                result.add(Integer.valueOf(tmpSqLiteReader.GetInt32(0)));
            }
        }
        return result;
    }
}
