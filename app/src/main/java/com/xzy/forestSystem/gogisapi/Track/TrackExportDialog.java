package  com.xzy.forestSystem.gogisapi.Track;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.ProjectionCoordinateSystem;
import  com.xzy.forestSystem.gogisapi.Geodatabase.ExportKML;
import  com.xzy.forestSystem.gogisapi.Geodatabase.ExportToShp;
import com.xzy.forestSystem.gogisapi.Geodatabase.StringEncodings;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrackExportDialog {
    private XDialogTemplate _Dialog;
    public Handler _myHandler;
    private Date _queryEndTime;
    private Date _queryStartTime;
    private List<Polyline> m_ExportTrackPolylines;
    private LinearLayout m_LLSystemType;
    private ICallback pCallback;

    @SuppressLint("WrongConstant")
    public TrackExportDialog() {
        this._Dialog = null;
        this._queryStartTime = null;
        this._queryEndTime = null;
        this.m_LLSystemType = null;
        this.m_ExportTrackPolylines = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Track.TrackExportDialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (!command.equals("??????")) {
                        return;
                    }
                    if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                        Common.ShowDialog("??????????????????\r\n        ?????????????????????????????????.???????????????????????????????????????????????????????????????????????????\r\n???????????????????????????");
                        return;
                    }
                    String tempFileName = Common.GetEditTextValueOnID(TrackExportDialog.this._Dialog, R.id.et_trackExport_Filename);
                    if (tempFileName == null || tempFileName.equals("")) {
                        Common.ShowDialog("????????????????????????????????????.");
                        return;
                    }
                    String tmpOutFileFolder = String.valueOf(Common.GetAPPPath()) + "/Track/????????????";
                    if (!Common.ExistFolder(tmpOutFileFolder)) {
                        new File(tmpOutFileFolder).mkdir();
                    }
                    String tmpOutFilePath = String.valueOf(tmpOutFileFolder) + FileSelector_Dialog.sRoot + tempFileName;
                    String tempType = Common.GetSpinnerValueOnID(TrackExportDialog.this._Dialog, R.id.sp_trackExport_FileType);
                    if (tempType.equals("Excel(CSV)??????")) {
                        if (TrackExportDialog.this.exportCSV(String.valueOf(tmpOutFilePath) + ".csv")) {
                            Common.ShowDialog("??????Excel(CSV)????????????.\r\n???????????????:" + tmpOutFilePath + ".csv");
                            TrackExportDialog.this._Dialog.dismiss();
                        }
                    } else if (tempType.equals("KML??????")) {
                        String tmpExportPath = String.valueOf(tmpOutFilePath) + ".kml";
                        try {
                            List<TrackPoint> tempList = TrackExportDialog.this.geTrackPoints();
                            if (tempList == null || tempList.size() <= 0) {
                                Common.ShowDialog("?????????????????????0.");
                                return;
                            }
                            String tempGeoType = Common.GetSpinnerValueOnID(TrackExportDialog.this._Dialog, R.id.sp_trackExport_DataType);
                            if (tempGeoType.equals("?????????")) {
                                List tempGeoList = TrackPoint.ConvertToPoints(tempList);
                                if (tempGeoList.size() > 0 && ExportKML.ExportKMLFile(tmpExportPath, "?????????", tempGeoList)) {
                                    Common.ShowDialog("??????KML????????????.\r\n???????????????:" + tmpExportPath);
                                    TrackExportDialog.this._Dialog.dismiss();
                                }
                            } else if (tempGeoType.equals("?????????")) {
                                List tempGeoList2 = TrackPoint.ConvertToPolylines(tempList);
                                if (tempGeoList2.size() > 0 && ExportKML.ExportKMLFile(tmpExportPath, "?????????", tempGeoList2)) {
                                    Common.ShowDialog("??????KML????????????.\r\n???????????????:" + tmpExportPath);
                                    TrackExportDialog.this._Dialog.dismiss();
                                }
                            }
                        } catch (Exception e) {
                        }
                    } else if (tempType.equals("ArcGIS(Shp)??????")) {
                        if (TrackExportDialog.this.exportShp(tmpOutFilePath, Common.GetSpinnerValueOnID(TrackExportDialog.this._Dialog, R.id.sp_trackExport_DataType))) {
                            Common.ShowDialog("??????Shapefile????????????.\r\n???????????????:" + tmpOutFilePath + ".shp");
                            TrackExportDialog.this._Dialog.dismiss();
                        }
                    }
                } catch (Exception e2) {
                }
            }
        };
        this._myHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Track.TrackExportDialog.2
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Common.ShowDialog(msg.obj.toString());
                    TrackExportDialog.this._Dialog.dismiss();
                }
                if (msg.what == 0) {
                    Common.ShowDialog(msg.obj.toString());
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.trackexport_dialog);
        this._Dialog.Resize(0.96f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("????????????");
        this._Dialog.SetHeadButtons("1,2130837673,??????,??????", this.pCallback);
        this.m_LLSystemType = (LinearLayout) this._Dialog.findViewById(R.id.ll_outputSystemType);
        List<String> tmpList = new ArrayList<>();
        tmpList.add("GB2312");
        tmpList.add(StringEncodings.UTF8);
        tmpList.add("UTF-16");
        tmpList.add("GBK");
        Common.SetSpinnerListData(this._Dialog, "???????????????:", tmpList, (int) R.id.sp_outputShpCharType, (ICallback) null);
        Common.SetValueToView("GB2312", this._Dialog.findViewById(R.id.sp_outputShpCharType));
        this._Dialog.findViewById(R.id.ll_outputShpCharType).setVisibility(8);
    }

    public void SetExportTime(Date StartTime, Date EndTime) {
        this._queryStartTime = StartTime;
        this._queryEndTime = EndTime;
    }

    public void SetExportTrackPolylines(List<Polyline> exportTrackPolylines) {
        this.m_ExportTrackPolylines = exportTrackPolylines;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        try {
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_trackExport_Filename, "?????????" + Common.fileDateFormat.format(new Date()));
            ArrayList tempArray = new ArrayList();
            tempArray.add("Excel(CSV)??????");
            tempArray.add("ArcGIS(Shp)??????");
            tempArray.add("KML??????");
            Common.SetSpinnerListData(this._Dialog, "?????????????????????:", tempArray, (int) R.id.sp_trackExport_FileType, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Track.TrackExportDialog.3
                @SuppressLint("WrongConstant")
                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                public void OnClick(String command, Object pObject) {
                    try {
                        if (!command.equals("OnItemSelected")) {
                            return;
                        }
                        if (Common.GetSpinnerValueOnID(TrackExportDialog.this._Dialog, R.id.sp_trackExport_FileType).equals("ArcGIS(Shp)??????")) {
                            TrackExportDialog.this.m_LLSystemType.setVisibility(0);
                            TrackExportDialog.this._Dialog.findViewById(R.id.ll_outputShpCharType).setVisibility(0);
                            return;
                        }
                        TrackExportDialog.this.m_LLSystemType.setVisibility(8);
                        TrackExportDialog.this._Dialog.findViewById(R.id.ll_outputShpCharType).setVisibility(8);
                    } catch (Exception e) {
                    }
                }
            });
            Common.SetValueToView("Excel(CSV)??????", this._Dialog.findViewById(R.id.sp_trackExport_FileType));
            ArrayList tempArray2 = new ArrayList();
            tempArray2.add("?????????");
            tempArray2.add("?????????");
            Common.SetSpinnerListData(this._Dialog, "?????????????????????:", tempArray2, (int) R.id.sp_trackExport_DataType, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Track.TrackExportDialog.4
                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                public void OnClick(String command, Object pObject) {
                    try {
                        if (command.equals("OnItemSelected")) {
                            Common.SetEditTextValueOnID(TrackExportDialog.this._Dialog, R.id.et_trackExport_Filename, String.valueOf(Common.GetSpinnerValueOnID(TrackExportDialog.this._Dialog, R.id.sp_trackExport_DataType)) + Common.fileDateFormat.format(new Date()));
                        }
                    } catch (Exception e) {
                    }
                }
            });
            Common.SetValueToView("?????????", this._Dialog.findViewById(R.id.sp_trackExport_DataType));
            ArrayList tempArray3 = new ArrayList();
            AbstractC0383CoordinateSystem tmpCoorSystem = PubVar.m_Workspace.GetCoorSystem();
            tempArray3.add(tmpCoorSystem.GetName());
            if (tmpCoorSystem instanceof ProjectionCoordinateSystem) {
                tempArray3.add("WGS-84??????");
            }
            Common.SetSpinnerListData(this._Dialog, "???????????????????????????:", tempArray3, (int) R.id.sp_outputSystemType);
            Common.SetValueToView("WGS-84??????", this._Dialog.findViewById(R.id.sp_outputSystemType));
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Track.TrackExportDialog.5
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                TrackExportDialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private List<TrackPoint> geTrackPoints() {
        if (this.m_ExportTrackPolylines != null && this.m_ExportTrackPolylines.size() > 0) {
            List<TrackPoint> tempList = new ArrayList<>();
            for (Polyline tmpPolyline : this.m_ExportTrackPolylines) {
                List<TrackPoint> tmpTrackPoints = (List) tmpPolyline.getTAGObject();
                if (tmpTrackPoints != null && tmpTrackPoints.size() > 0) {
                    tempList.addAll(tmpTrackPoints);
                }
            }
            return tempList;
        } else if (this._queryStartTime == null || this._queryEndTime == null) {
            return null;
        } else {
            if (this._queryStartTime.getYear() == this._queryEndTime.getYear()) {
                return TrackQuery_Dailog.QueryTrackData(this._queryStartTime, this._queryEndTime);
            }
            Common.ShowDialog("??????????????????????????????,????????????????????????.");
            return null;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean exportCSV(String _outputPath) {
        try {
            List<TrackPoint> tempList = geTrackPoints();
            if (tempList == null || tempList.size() <= 0) {
                Common.ShowDialog("?????????????????????0.");
                return false;
            }
            OutputStreamWriter osw = null;
            try {
                OutputStreamWriter osw2 = new OutputStreamWriter(new FileOutputStream(new File(_outputPath)), "gbk");
                try {
                    osw2.write("??????,??????,??????(??),??????(??),??????(m),??????(m),??????(??),??????(m/s),X,Y\r\n");
                    int tid = 1;
                    for (TrackPoint tmpPtn : tempList) {
                        StringBuilder tempSB = new StringBuilder();
                        tid++;
                        tempSB.append(tid);
                        tempSB.append(",");
                        tempSB.append(tmpPtn._TimeStr);
                        tempSB.append(",");
                        tempSB.append(tmpPtn._Longitude);
                        tempSB.append(",");
                        tempSB.append(tmpPtn._Latitude);
                        tempSB.append(",");
                        tempSB.append(tmpPtn._Elevation);
                        tempSB.append(",");
                        tempSB.append(tmpPtn._Accuracy);
                        tempSB.append(",");
                        tempSB.append(tmpPtn._Direction);
                        tempSB.append(",");
                        tempSB.append(tmpPtn._Speed);
                        Coordinate tmpXYCoord = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tmpPtn._Longitude, tmpPtn._Latitude, tmpPtn._Elevation, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                        tempSB.append(",");
                        tempSB.append(tmpXYCoord.getX());
                        tempSB.append(",");
                        tempSB.append(tmpXYCoord.getY());
                        tempSB.append("\r\n");
                        osw2.write(tempSB.toString());
                    }
                    osw2.flush();
                    osw2.close();
                    return true;
                } catch (Exception e) {
                    osw = osw2;
                    try {
                        osw.close();
                        return true;
                    } catch (Exception e2) {
                    }
                }
            } catch (Exception e3) {
                osw.close();
                return true;
            }
        } catch (Exception e4) {
        }
        return true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean exportShp(final String _outputPath, final String geoType) {
        try {
            final List<TrackPoint> tempList = geTrackPoints();
            if (tempList == null || tempList.size() <= 0) {
                Common.ShowDialog("?????????????????????0.");
                return false;
            }
            final CustomeProgressDialog progressDialog = CustomeProgressDialog.createDialog(this._Dialog.getContext());
            progressDialog.SetHeadTitle("????????????");
            progressDialog.SetProgressTitle("????????????ArcGIS(Shp)????????????:");
            progressDialog.SetProgressInfo("??????????????????...");
            progressDialog.show();
            new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Track.TrackExportDialog.6
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        double tmpMaxX = ((TrackPoint) tempList.get(0))._Longitude;
                        double tmpMinX = tmpMaxX;
                        double tmpMaxY = ((TrackPoint) tempList.get(0))._Latitude;
                        double tmpMinY = tmpMaxY;
                        for (TrackPoint tmpTrackPtn01 : tempList) {
                            if (tmpMinX > tmpTrackPtn01._Longitude) {
                                tmpMinX = tmpTrackPtn01._Longitude;
                            }
                            if (tmpMaxX < tmpTrackPtn01._Longitude) {
                                tmpMaxX = tmpTrackPtn01._Longitude;
                            }
                            if (tmpMinY > tmpTrackPtn01._Latitude) {
                                tmpMinY = tmpTrackPtn01._Latitude;
                            }
                            if (tmpMaxY < tmpTrackPtn01._Latitude) {
                                tmpMaxY = tmpTrackPtn01._Latitude;
                            }
                        }
                        Envelope tmpExtend = new Envelope(tmpMinX, tmpMaxY, tmpMaxX, tmpMinY);
                        String tmpOutputCharType = Common.GetSpinnerValueOnID(TrackExportDialog.this._Dialog, R.id.sp_outputShpCharType);
                        if (geoType.equals("?????????")) {
                            List<AbstractGeometry> tempGeoList = TrackPoint.ConvertToPoints(tempList);
                            if (tempGeoList.size() > 0) {
                                ExportToShp tempExport = new ExportToShp();
                                tempExport.setBindProgressDialog(progressDialog);
                                String tmpOutputCoorType = Common.GetSpinnerValueOnID(TrackExportDialog.this._Dialog, R.id.sp_outputSystemType);
                                tempExport.setExportCoordType(tmpOutputCoorType);
                                tempExport.CharacterCode = tmpOutputCharType;
                                boolean tmpConsideConvert = false;
                                if (!tmpOutputCoorType.contains("WGS-84??????")) {
                                    tmpConsideConvert = true;
                                }
                                tempExport.ToPrj(String.valueOf(_outputPath) + ".prj");
                                if (tempExport.ExportToShp(String.valueOf(_outputPath) + ".shp", tempGeoList, tmpExtend, tmpConsideConvert)) {
                                    List<LayerField> tmpfields = new ArrayList<>();
                                    LayerField tmpField00 = new LayerField();
                                    tmpField00.SetFieldName("??????");
                                    tmpField00.SetFieldTypeName("??????");
                                    tmpfields.add(tmpField00);
                                    LayerField tmpField002 = new LayerField();
                                    tmpField002.SetFieldName("??????");
                                    tmpField002.SetFieldTypeName("?????????");
                                    tmpfields.add(tmpField002);
                                    LayerField tmpField01 = new LayerField();
                                    tmpField01.SetFieldName("??????");
                                    tmpField01.SetFieldTypeName("?????????");
                                    tmpfields.add(tmpField01);
                                    LayerField tmpField02 = new LayerField();
                                    tmpField02.SetFieldName("??????");
                                    tmpField02.SetFieldTypeName("?????????");
                                    tmpfields.add(tmpField02);
                                    LayerField tmpField03 = new LayerField();
                                    tmpField03.SetFieldName("??????");
                                    tmpField03.SetFieldTypeName("?????????");
                                    tmpfields.add(tmpField03);
                                    LayerField tmpField04 = new LayerField();
                                    tmpField04.SetFieldName("??????");
                                    tmpField04.SetFieldTypeName("?????????");
                                    tmpfields.add(tmpField04);
                                    LayerField tmpField05 = new LayerField();
                                    tmpField05.SetFieldName("??????");
                                    tmpField05.SetFieldTypeName("?????????");
                                    tmpfields.add(tmpField05);
                                    LayerField tmpField06 = new LayerField();
                                    tmpField06.SetFieldName("??????");
                                    tmpField06.SetFieldTypeName("?????????");
                                    tmpfields.add(tmpField06);
                                    List<ArrayList> tmpValuesList = new ArrayList<>();
                                    int tid = 1;
                                    for (AbstractGeometry tmpPtn01 : tempGeoList) {
                                        ArrayList tmpArrayList01 = new ArrayList();
                                        tid++;
                                        tmpArrayList01.add(Integer.valueOf(tid));
                                        Object tempObj = tmpPtn01.getTAGObject();
                                        if (tempObj == null || !(tempObj instanceof TrackPoint)) {
                                            tmpArrayList01.add("");
                                            tmpArrayList01.add("");
                                            tmpArrayList01.add("");
                                            tmpArrayList01.add("");
                                            tmpArrayList01.add("");
                                            tmpArrayList01.add("");
                                            tmpArrayList01.add("");
                                        } else {
                                            TrackPoint tmpTrackPoint = (TrackPoint) tempObj;
                                            tmpArrayList01.add(tmpTrackPoint._TimeStr);
                                            tmpArrayList01.add(Double.valueOf(tmpTrackPoint._Longitude));
                                            tmpArrayList01.add(Double.valueOf(tmpTrackPoint._Latitude));
                                            tmpArrayList01.add(Double.valueOf(tmpTrackPoint._Elevation));
                                            tmpArrayList01.add(Double.valueOf(tmpTrackPoint._Accuracy));
                                            tmpArrayList01.add(Double.valueOf(tmpTrackPoint._Direction));
                                            tmpArrayList01.add(Double.valueOf(tmpTrackPoint._Speed));
                                        }
                                        tmpValuesList.add(tmpArrayList01);
                                    }
                                    if (tempExport.ExportToDbf(String.valueOf(_outputPath) + ".dbf", tmpfields, tmpValuesList) && tempExport.ExportToShx(String.valueOf(_outputPath) + ".shx", tempGeoList, tmpExtend, false)) {
                                        Message msg2 = TrackExportDialog.this._myHandler.obtainMessage();
                                        msg2.what = 1;
                                        msg2.obj = "??????????????????!\r\n?????????:" + _outputPath + ".shp";
                                        TrackExportDialog.this._myHandler.sendMessage(msg2);
                                    }
                                }
                            }
                        } else if (geoType.equals("?????????")) {
                            List<AbstractGeometry> tempGeoList2 = TrackPoint.ConvertToPolylines(tempList);
                            if (tempGeoList2.size() > 0) {
                                ExportToShp tempExport2 = new ExportToShp();
                                tempExport2.setBindProgressDialog(progressDialog);
                                String tmpOutputCoorType2 = Common.GetSpinnerValueOnID(TrackExportDialog.this._Dialog, R.id.sp_outputSystemType);
                                tempExport2.setExportCoordType(tmpOutputCoorType2);
                                tempExport2.CharacterCode = tmpOutputCharType;
                                boolean tmpConsideConvert2 = false;
                                if (!tmpOutputCoorType2.contains("WGS-84??????")) {
                                    tmpConsideConvert2 = true;
                                }
                                tempExport2.ToPrj(String.valueOf(_outputPath) + ".prj");
                                if (tempExport2.ExportToShp(String.valueOf(_outputPath) + ".shp", tempGeoList2, tmpExtend, tmpConsideConvert2)) {
                                    List<LayerField> tmpfields2 = new ArrayList<>();
                                    LayerField tmpField003 = new LayerField();
                                    tmpField003.SetFieldName("??????");
                                    tmpField003.SetFieldTypeName("??????");
                                    tmpfields2.add(tmpField003);
                                    LayerField tmpField0022 = new LayerField();
                                    tmpField0022.SetFieldName("????????????");
                                    tmpField0022.SetFieldTypeName("?????????");
                                    tmpfields2.add(tmpField0022);
                                    LayerField tmpField0032 = new LayerField();
                                    tmpField0032.SetFieldName("????????????");
                                    tmpField0032.SetFieldTypeName("?????????");
                                    tmpfields2.add(tmpField0032);
                                    List<ArrayList> tmpValuesList2 = new ArrayList<>();
                                    int tid2 = 1;
                                    for (AbstractGeometry tmpPoly : tempGeoList2) {
                                        ArrayList tmpArrayList012 = new ArrayList();
                                        tid2++;
                                        tmpArrayList012.add(Integer.valueOf(tid2));
                                        Object tempObj2 = tmpPoly.getTAGObject();
                                        if (tempObj2 != null) {
                                            String[] tempStrs = tempObj2.toString().split(";");
                                            if (tempStrs == null || tempStrs.length <= 1) {
                                                tmpArrayList012.add("");
                                                tmpArrayList012.add("");
                                            } else {
                                                tmpArrayList012.add(tempStrs[0]);
                                                tmpArrayList012.add(tempStrs[1]);
                                            }
                                        } else {
                                            tmpArrayList012.add("");
                                            tmpArrayList012.add("");
                                        }
                                        tmpValuesList2.add(tmpArrayList012);
                                    }
                                    if (tempExport2.ExportToDbf(String.valueOf(_outputPath) + ".dbf", tmpfields2, tmpValuesList2) && tempExport2.ExportToShx(String.valueOf(_outputPath) + ".shx", /*tempGeoList2*/null, tmpExtend, false)) {
                                        Message msg22 = TrackExportDialog.this._myHandler.obtainMessage();
                                        msg22.what = 1;
                                        msg22.obj = "??????????????????!\r\n?????????:" + _outputPath + ".shp";
                                        TrackExportDialog.this._myHandler.sendMessage(msg22);
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        Message msg23 = TrackExportDialog.this._myHandler.obtainMessage();
                        msg23.what = 0;
                        msg23.obj = "?????????????????????.\r\n" + ex.getMessage();
                        TrackExportDialog.this._myHandler.sendMessage(msg23);
                    } finally {
                        progressDialog.dismiss();
                    }
                }
            }.start();
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
