package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EFieldAutoValueType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.CommonProcess;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.MyControls.Input_Dialog;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.XProject.Layer_Field_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class FieldValuesCal_Dialog {
    private XDialogTemplate _Dialog;
    private String m_AutoValueTypeName;
    private String m_CalFieldIDName;
    private String m_CalFieldName;
    private ICallback m_Callback;
    private boolean m_IsSkipHasValue;
    private String m_QueryCondition;
    FeatureLayer m_SelectLayer;
    private String m_StartAutoCode;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public void setFeatureLayer(FeatureLayer featureLayer) {
        this.m_SelectLayer = featureLayer;
    }

    public FieldValuesCal_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_SelectLayer = null;
        this.m_QueryCondition = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FieldValuesCal_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.contains("????????????")) {
                    Object[] tmpObjs = (Object[]) object;
                    if (tmpObjs != null && tmpObjs.length >= 1) {
                        String tempValue = tmpObjs[1].toString().trim();
                        if (tempValue.length() == 0) {
                            tempValue = "1";
                        }
                        FieldValuesCal_Dialog.this.m_StartAutoCode = tempValue;
                        FieldValuesCal_Dialog.this.startCalculate();
                    }
                } else if (command.equals("??????????????????")) {
                    Object[] tmpObjs2 = (Object[]) object;
                    if (tmpObjs2 != null && tmpObjs2.length > 1) {
                        if (FieldValuesCal_Dialog.this.m_SelectLayer != null) {
                            Object tmpTagObj = FieldValuesCal_Dialog.this.m_SelectLayer.getTag();
                            HashMap<String, Object> tmpTagHash = null;
                            if (tmpTagObj instanceof HashMap) {
                                tmpTagHash = (HashMap) tmpTagObj;
                            }
                            if (tmpTagHash == null) {
                                tmpTagHash = new HashMap<>();
                            }
                            tmpTagHash.put("QuerySQLList2", tmpObjs2[1]);
                            FieldValuesCal_Dialog.this.m_SelectLayer.setTag(tmpTagHash);
                        }
                        FieldValuesCal_Dialog.this.m_QueryCondition = tmpObjs2[0].toString();
                        Common.SetSpinnerListValue(FieldValuesCal_Dialog.this.m_QueryCondition, FieldValuesCal_Dialog.this._Dialog.findViewById(R.id.sp_autoCalFieldValue_Filter));
                    }
                } else if (command.equals("OnItemSelected")) {
                    DataQuery_Filter_Dialog tempDialog = new DataQuery_Filter_Dialog();
                    tempDialog.setQueryTag("QuerySQLList2");
                    tempDialog.SetSelectLayer(FieldValuesCal_Dialog.this.m_SelectLayer);
                    tempDialog.SetCallback(FieldValuesCal_Dialog.this.pCallback);
                    tempDialog.setTitle("??????????????????");
                    tempDialog.setHeadButtons("??????");
                    tempDialog.ShowDialog();
                }
            }
        };
        this.m_AutoValueTypeName = "";
        this.m_CalFieldName = "";
        this.m_CalFieldIDName = "";
        this.m_StartAutoCode = "";
        this.m_IsSkipHasValue = false;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.fieldvaluescal_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("???????????????");
        this._Dialog.findViewById(R.id.btn_fieldValues_Cal).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FieldValuesCal_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                FieldValuesCal_Dialog.this.m_IsSkipHasValue = Common.GetCheckBoxValueOnID(FieldValuesCal_Dialog.this._Dialog, R.id.ck_skipHasValue);
                FieldValuesCal_Dialog.this.m_AutoValueTypeName = Common.GetSpinnerValueOnID(FieldValuesCal_Dialog.this._Dialog, R.id.sp_autoCalFieldValueType);
                if (FieldValuesCal_Dialog.this.m_AutoValueTypeName.equals("") || FieldValuesCal_Dialog.this.m_AutoValueTypeName.equals("???")) {
                    Common.ShowDialog("????????????????????????????????????,??????????????????.");
                    return;
                }
                FieldValuesCal_Dialog.this.m_CalFieldName = Common.GetSpinnerValueOnID(FieldValuesCal_Dialog.this._Dialog, R.id.sp_fieldNameList);
                if (FieldValuesCal_Dialog.this.m_CalFieldName.equals("")) {
                    Common.ShowDialog("????????????????????????????????????,??????????????????.");
                } else if (FieldValuesCal_Dialog.this.m_AutoValueTypeName.equals("????????????")) {
                    FieldValuesCal_Dialog.this.m_StartAutoCode = "";
                    Input_Dialog tempDialog = new Input_Dialog();
                    tempDialog.setValues("????????????", "??????:", "", "??????:????????????????????????,???'ZJHZ10001");
                    tempDialog.SetCallback(FieldValuesCal_Dialog.this.pCallback);
                    tempDialog.ShowDialog();
                } else if (FieldValuesCal_Dialog.this.m_AutoValueTypeName.equals("?????????")) {
                    Input_Dialog tempDialog2 = new Input_Dialog();
                    tempDialog2.setValues("???????????????", "???:", "", "??????:???????????????????????????.");
                    tempDialog2.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FieldValuesCal_Dialog.2.1
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command, Object pObject) {
                            Object[] tmpObjs = (Object[]) pObject;
                            if (tmpObjs != null && tmpObjs.length >= 1) {
                                String tempValue = tmpObjs[1].toString().trim();
                                if (tempValue.length() == 0) {
                                    tempValue = "";
                                }
                                FieldValuesCal_Dialog.this.startSetUniqueValue(tempValue);
                            }
                        }
                    });
                    tempDialog2.ShowDialog();
                } else {
                    FieldValuesCal_Dialog.this.startCalculate();
                }
            }
        });
        SpinnerList tmpSpinnerList = (SpinnerList) this._Dialog.findViewById(R.id.sp_autoCalFieldValue_Filter);
        tmpSpinnerList.SetClickCallback(true);
        tmpSpinnerList.SetCallback(this.pCallback);
        this._Dialog.findViewById(R.id.sp_autoCalFieldValue_Filter).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FieldValuesCal_Dialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                FieldValuesCal_Dialog.this.pCallback.OnClick("", null);
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void startCalculate() {
        int tmpStartIndex;
        String tmpStartTag;
        Throwable th;
        try {
            this.m_CalFieldIDName = this.m_SelectLayer.GetDataFieldByFieldName(this.m_CalFieldName);
            GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByID(this.m_SelectLayer.GetLayerID());
            if (tmpGeoLayer != null) {
                DataSet pDataSet = tmpGeoLayer.getDataset();
                SQLiteDBHelper tmpSQLiteDBHelper = pDataSet.getDataSource().GetSQLiteDatabase();
                String tmpLyrTablename = pDataSet.getDataTableName();
                pDataSet.RefreshTotalCount();
                if (pDataSet.getTotalCount() == 0) {
                    Common.ShowDialog("?????????????????????????????????.");
                } else if (this.m_AutoValueTypeName.equals("????????????")) {
                    if (CommonProcess.isDigit(this.m_StartAutoCode)) {
                        tmpStartIndex = Integer.parseInt(this.m_StartAutoCode);
                        tmpStartTag = "";
                    } else {
                        tmpStartIndex = Integer.parseInt(CommonProcess.getNumbers(this.m_StartAutoCode));
                        tmpStartTag = CommonProcess.splitNotNumber(this.m_StartAutoCode);
                    }
                    List<String> tmpList01 = new ArrayList<>();
                    String tmpSql = "Select GROUP_CONCAT(SYS_ID) AS MyID From " + tmpLyrTablename;
                    if (this.m_IsSkipHasValue) {
                        tmpSql = String.valueOf(tmpSql) + " Where " + this.m_CalFieldIDName + "='' OR " + this.m_CalFieldIDName + " IS NULL";
                    }
                    SQLiteReader tempReader = tmpSQLiteDBHelper.Query(tmpSql);
                    if (tempReader != null && tempReader.Read()) {
                        String tempSYSIDs = tempReader.GetString(0);
                        if (tempSYSIDs == null || tempSYSIDs.equals("")) {
                            Common.ShowDialog("??????????????????????????????????????????.");
                            return;
                        }
                        String[] tmpStrs01 = tempSYSIDs.split(",");
                        if (tmpStrs01 == null || tmpStrs01.length <= 0) {
                            Common.ShowDialog("??????????????????????????????????????????.");
                            return;
                        }
                        int length = tmpStrs01.length;
                        for (int i = 0; i < length; i++) {
                            String tmpStr02String = tmpStrs01[i];
                            if (tmpStr02String.length() > 0) {
                                tmpList01.add(tmpStr02String);
                            }
                        }
                        tempReader.Close();
                    }
                    if (tmpList01.size() > 0) {
                        String tmpSQL01String = "Update " + tmpLyrTablename + " Set " + this.m_CalFieldIDName + "='" + tmpStartTag;
                        int tmpTid = 0;
                        tmpSQLiteDBHelper.BeginTransaction();
                        try {
                            Iterator<String> tmpIterator = tmpList01.iterator();
                            while (tmpIterator.hasNext()) {
                                try {
                                    tmpStartIndex++;
                                    tmpSQLiteDBHelper.ExecuteSQLSimple(String.valueOf(tmpSQL01String) + String.valueOf(tmpStartIndex) + "' Where SYS_ID=" + tmpIterator.next());
                                    tmpTid++;
                                } catch (Exception e) {
                                    tmpSQLiteDBHelper.SetTransactionSuccessful();
                                    tmpSQLiteDBHelper.EndTransaction();
                                    Common.ShowDialog("?????????" + String.valueOf(tmpTid) + "?????????.");
                                    return;
                                } catch (Throwable th2) {
                                    th = th2;
                                    tmpSQLiteDBHelper.SetTransactionSuccessful();
                                    tmpSQLiteDBHelper.EndTransaction();
                                    throw th;
                                }
                            }
                            tmpSQLiteDBHelper.SetTransactionSuccessful();
                            tmpSQLiteDBHelper.EndTransaction();
                        } catch (Exception e2) {
                        } catch (Throwable th3) {
                            th = th3;
                            tmpSQLiteDBHelper.SetTransactionSuccessful();
                            tmpSQLiteDBHelper.EndTransaction();
                        }
                        Common.ShowDialog("?????????" + String.valueOf(tmpTid) + "?????????.");
                        return;
                    }
                    Common.ShowDialog("?????????????????????????????????.");
                } else {
                    List<String> tmpList012 = new ArrayList<>();
                    String tmpSql2 = "Select GROUP_CONCAT(SYS_ID) AS MyID From " + tmpLyrTablename;
                    if (this.m_IsSkipHasValue) {
                        tmpSql2 = String.valueOf(tmpSql2) + " Where " + this.m_CalFieldIDName + "='' OR " + this.m_CalFieldIDName + " IS NULL";
                    }
                    SQLiteReader tempReader2 = tmpSQLiteDBHelper.Query(tmpSql2);
                    if (tempReader2 != null && tempReader2.Read()) {
                        String tempSYSIDs2 = tempReader2.GetString(0);
                        if (tempSYSIDs2 == null || tempSYSIDs2.equals("")) {
                            Common.ShowDialog("??????????????????????????????????????????.");
                            return;
                        }
                        String[] tmpStrs012 = tempSYSIDs2.split(",");
                        if (tmpStrs012 == null || tmpStrs012.length <= 0) {
                            Common.ShowDialog("??????????????????????????????????????????.");
                            return;
                        }
                        int length2 = tmpStrs012.length;
                        for (int i2 = 0; i2 < length2; i2++) {
                            String tmpStr02String2 = tmpStrs012[i2];
                            if (tmpStr02String2.length() > 0) {
                                tmpList012.add(tmpStr02String2);
                            }
                        }
                        tempReader2.Close();
                    }
                    if (tmpList012.size() > 0) {
                        EFieldAutoValueType eFieldAutoValueType = EFieldAutoValueType.NONE;
                        EFieldAutoValueType tmpEFieldAutoValueType = Layer_Field_Dialog.ConvertTypeNameToType(this.m_AutoValueTypeName);
                        if (tmpEFieldAutoValueType != EFieldAutoValueType.NONE) {
                            String tmpSQL01String2 = "Update " + tmpLyrTablename + " Set " + this.m_CalFieldIDName + "='";
                            List<String> tmpList02 = new ArrayList<>();
                            for (String tmpSYSIDString : tmpList012) {
                                AbstractGeometry tmpGeometry = pDataSet.GetGeometryBySYSIDMust(tmpSYSIDString);
                                if (tmpGeometry != null) {
                                    String tmpString02 = ConvertGeometryValue(tmpGeometry, tmpEFieldAutoValueType);
                                    if (tmpString02.length() > 0) {
                                        tmpList02.add(String.valueOf(tmpString02) + "' Where SYS_ID=" + tmpSYSIDString);
                                    }
                                }
                            }
                            if (tmpList02.size() > 0) {
                                tmpSQLiteDBHelper.BeginTransaction();
                                try {
                                    Iterator<String> tmpIterator02 = tmpList02.iterator();
                                    while (tmpIterator02.hasNext()) {
                                        tmpSQLiteDBHelper.ExecuteSQLSimple(String.valueOf(tmpSQL01String2) + tmpIterator02.next());
                                    }
                                } catch (Exception e3) {
                                } finally {
                                    tmpSQLiteDBHelper.SetTransactionSuccessful();
                                    tmpSQLiteDBHelper.EndTransaction();
                                }
                                Common.ShowDialog("????????????!?????????" + String.valueOf(tmpList02.size()) + "?????????.");
                                return;
                            }
                            Common.ShowDialog("??????????????????????????????????????????.");
                            return;
                        }
                        Common.ShowDialog("?????????????????????.");
                        return;
                    }
                    Common.ShowDialog("?????????????????????????????????.");
                }
            }
        } catch (Exception e4) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void startSetUniqueValue(String uniqueValue) {
        try {
            this.m_CalFieldIDName = this.m_SelectLayer.GetDataFieldByFieldName(this.m_CalFieldName);
            GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByID(this.m_SelectLayer.GetLayerID());
            if (tmpGeoLayer != null) {
                DataSet pDataSet = tmpGeoLayer.getDataset();
                SQLiteDBHelper tmpSQLiteDBHelper = pDataSet.getDataSource().GetSQLiteDatabase();
                String tmpLyrTablename = pDataSet.getDataTableName();
                pDataSet.RefreshTotalCount();
                if (pDataSet.getTotalCount() == 0) {
                    Common.ShowDialog("?????????????????????????????????.");
                } else if (this.m_AutoValueTypeName.equals("?????????")) {
                    List<String> tmpList01 = new ArrayList<>();
                    String tmpSql = "Select GROUP_CONCAT(SYS_ID) AS MyID From " + tmpLyrTablename + " Where SYS_STATUS=0 ";
                    if (this.m_IsSkipHasValue) {
                        tmpSql = String.valueOf(tmpSql) + " AND (" + this.m_CalFieldIDName + "='' OR " + this.m_CalFieldIDName + " IS NULL)";
                    }
                    if (this.m_QueryCondition != null && !this.m_QueryCondition.equals("")) {
                        tmpSql = String.valueOf(tmpSql) + " And (" + this.m_QueryCondition + ") ";
                    }
                    SQLiteReader tempReader = tmpSQLiteDBHelper.Query(tmpSql);
                    if (tempReader != null && tempReader.Read()) {
                        String tempSYSIDs = tempReader.GetString(0);
                        if (tempSYSIDs == null || tempSYSIDs.equals("")) {
                            Common.ShowDialog("??????????????????????????????????????????.");
                            return;
                        }
                        String[] tmpStrs01 = tempSYSIDs.split(",");
                        if (tmpStrs01 == null || tmpStrs01.length <= 0) {
                            Common.ShowDialog("??????????????????????????????????????????.");
                            return;
                        }
                        int length = tmpStrs01.length;
                        for (int i = 0; i < length; i++) {
                            String tmpStr02String = tmpStrs01[i];
                            if (tmpStr02String.length() > 0) {
                                tmpList01.add(tmpStr02String);
                            }
                        }
                        tempReader.Close();
                    }
                    if (tmpList01.size() > 0) {
                        int tmpTid = 0;
                        String tmpSQL01String = "Update " + tmpLyrTablename + " Set " + this.m_CalFieldIDName + "='";
                        tmpSQLiteDBHelper.BeginTransaction();
                        try {
                            Iterator<String> tmpIterator = tmpList01.iterator();
                            while (tmpIterator.hasNext()) {
                                tmpSQLiteDBHelper.ExecuteSQLSimple(String.valueOf(tmpSQL01String) + uniqueValue + "' Where SYS_ID=" + tmpIterator.next());
                                tmpTid++;
                            }
                        } catch (Exception e) {
                        } finally {
                            tmpSQLiteDBHelper.SetTransactionSuccessful();
                            tmpSQLiteDBHelper.EndTransaction();
                        }
                        Common.ShowDialog("?????????" + String.valueOf(tmpTid) + "?????????.");
                        return;
                    }
                    Common.ShowDialog("?????????????????????????????????.");
                }
            }
        } catch (Exception e2) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        if (this.m_SelectLayer != null) {
            List<String> tmpList = new ArrayList<>();
            for (LayerField tmpLayerField : this.m_SelectLayer.GetFieldList()) {
                tmpList.add(tmpLayerField.GetFieldName());
            }
            Common.SetSpinnerListData(this._Dialog, "????????????????????????:", tmpList, (int) R.id.sp_fieldNameList, (ICallback) null);
            List<String> tmpList2 = new ArrayList<>();
            tmpList2.add("???");
            tmpList2.add("?????????");
            tmpList2.add("????????????");
            tmpList2.add("X??????");
            tmpList2.add("Y??????");
            tmpList2.add("Z??????");
            tmpList2.add("??????");
            tmpList2.add("??????");
            tmpList2.add("??????(??????)");
            tmpList2.add("??????(??????)");
            if (this.m_SelectLayer.GetLayerType() == EGeoLayerType.POLYLINE) {
                tmpList2.add("??????");
            } else if (this.m_SelectLayer.GetLayerType() == EGeoLayerType.POLYGON) {
                tmpList2.add("??????");
                tmpList2.add("??????");
            }
            Common.SetSpinnerListData(this._Dialog, "??????????????????", tmpList2, (int) R.id.sp_autoCalFieldValueType, (ICallback) null);
        }
    }

    public static String ConvertGeometryValue(AbstractGeometry geometry, EFieldAutoValueType valueType) {
        if (valueType == EFieldAutoValueType.Geo_X) {
            return String.format("%f", Double.valueOf(geometry.getPoint(0).getX()));
        }
        if (valueType == EFieldAutoValueType.Geo_Y) {
            return String.format("%f", Double.valueOf(geometry.getPoint(0).getY()));
        }
        if (valueType == EFieldAutoValueType.Geo_Z) {
            return String.format("%f", Double.valueOf(geometry.getPoint(0).getZ()));
        }
        if (valueType == EFieldAutoValueType.Geo_Longitude) {
            return String.format("%f", Double.valueOf(geometry.getPoint(0).getGeoX()));
        }
        if (valueType == EFieldAutoValueType.Geo_Latitude) {
            return String.format("%f", Double.valueOf(geometry.getPoint(0).getGeoY()));
        }
        if (valueType == EFieldAutoValueType.Geo_Longitude_Int9) {
            return String.format("%d", Integer.valueOf((int) (geometry.getPoint(0).getGeoX() * 1000000.0d)));
        }
        if (valueType == EFieldAutoValueType.Geo_Latitude_Int8) {
            return String.format("%d", Integer.valueOf((int) (geometry.getPoint(0).getGeoY() * 1000000.0d)));
        }
        if (valueType == EFieldAutoValueType.GPS_Longitude) {
            return String.format("%f", Double.valueOf(PubVar._PubCommand.m_GpsLocation.getGPSCoordinate2().getX()));
        }
        if (valueType == EFieldAutoValueType.GPS_Latitude) {
            return String.format("%f", Double.valueOf(PubVar._PubCommand.m_GpsLocation.getGPSCoordinate2().getY()));
        }
        if (valueType == EFieldAutoValueType.GPS_Elevation) {
            return String.format("%f", Double.valueOf(PubVar._PubCommand.m_GpsLocation.getGPSCoordinate2().getZ()));
        }
        if (valueType == EFieldAutoValueType.Geo_Length) {
            if (geometry instanceof Polyline) {
                return String.format("%f", Double.valueOf(((Polyline) geometry).getLength(true)));
            }
            if (geometry instanceof Polygon) {
                return String.format("%f", Double.valueOf(((Polygon) geometry).getLength(true)));
            }
            return "0";
        } else if (valueType == EFieldAutoValueType.Geo_Area) {
            if (geometry instanceof Polygon) {
                return Common.SimplifyArea(((Polygon) geometry).getArea(true), false);
            }
            return "0";
        } else if (valueType == EFieldAutoValueType.DateTime_Long) {
            return Common.dateFormat.format(new Date());
        } else {
            if (valueType == EFieldAutoValueType.DateTime_Small) {
                return Common.dateSmallFormat.format(new Date());
            }
            return "";
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FieldValuesCal_Dialog.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                if (FieldValuesCal_Dialog.this.m_SelectLayer == null) {
                    Common.ShowDialog("????????????????????????.");
                    FieldValuesCal_Dialog.this._Dialog.dismiss();
                    return;
                }
                FieldValuesCal_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
