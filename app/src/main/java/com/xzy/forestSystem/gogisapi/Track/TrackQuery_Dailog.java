package  com.xzy.forestSystem.gogisapi.Track;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.GraphicSymbolGeometry;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WGS1984;
import  com.xzy.forestSystem.gogisapi.Display.ISymbol;
import  com.xzy.forestSystem.gogisapi.Display.SymbolManage;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.MyControls.DateSelect_Dailog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TrackQuery_Dailog {
    private XDialogTemplate _Dialog;
    private Date _queryEndTime;
    private Date _queryStartTime;
    private ICallback m_Callback;
    private SpinnerList m_EndTimeSpinner;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private LinearLayout m_QueryResultLayout;
    private SpinnerList m_StartTimeSpinner;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    @SuppressLint("WrongConstant")
    public TrackQuery_Dailog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_QueryResultLayout = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Track.TrackQuery_Dailog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                Object[] tmpObjs;
                try {
                    if (command.equals("时间选择返回") && (tmpObjs = (Object[]) object) != null && tmpObjs.length > 1) {
                        if (tmpObjs[0].toString().equals("选择起始时间")) {
                            TrackQuery_Dailog.this.m_StartTimeSpinner.SetTextJust(Common.dateFormat.format((Date) tmpObjs[1]));
                        } else {
                            TrackQuery_Dailog.this.m_EndTimeSpinner.SetTextJust(Common.dateFormat.format((Date) tmpObjs[1]));
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        this._queryStartTime = null;
        this._queryEndTime = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.track_query_dialog);
        this._Dialog.Resize(1.0f, PubVar.DialogHeightRatio);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("足迹查询");
        ((Button) this._Dialog.findViewById(R.id.btn_TrackQuery)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_TrackShowInMap)).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_TrackExport).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_TrackDelete).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectAll).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectDe).setOnClickListener(new ViewClick());
        this.m_QueryResultLayout = (LinearLayout) this._Dialog.findViewById(R.id.ll_trackResult);
        this.m_QueryResultLayout.setVisibility(8);
        this.m_StartTimeSpinner = (SpinnerList) this._Dialog.findViewById(R.id.sp_trackQueryStartTime);
        this.m_EndTimeSpinner = (SpinnerList) this._Dialog.findViewById(R.id.sp_trackQueryEndTime);
        this.m_StartTimeSpinner.SetClickCallback(true);
        this.m_EndTimeSpinner.SetClickCallback(true);
        int tmpCellWidth = (int) (15.0f * PubVar.ScaledDensity);
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_dataManag_datalist), "自定义", "选择,起始时间,结束时间,节点数,长度", "checkbox,text,text,text,text", new int[]{tmpCellWidth, tmpCellWidth * 4, tmpCellWidth * 4, (int) (25.0f * PubVar.ScaledDensity), tmpCellWidth * 3}, null);
        this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        try {
            Calendar calendar = Calendar.getInstance();
            String tempStr = Common.dateFormat.format(calendar.getTime());
            this.m_EndTimeSpinner.SetClickCallback(true);
            this.m_EndTimeSpinner.SetTextJust(tempStr);
            this.m_EndTimeSpinner.SetSelectReturnTag("点击选择结束时间");
            this.m_EndTimeSpinner.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Track.TrackQuery_Dailog.2
                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                public void OnClick(String paramString, Object pObject) {
                    try {
                        if (paramString.equals("点击选择结束时间")) {
                            Date tmpStartTime = Common.dateFormat.parse(String.valueOf(pObject));
                            DateSelect_Dailog tempDialog = new DateSelect_Dailog();
                            tempDialog.SetCallback(TrackQuery_Dailog.this.pCallback);
                            tempDialog.SetReturnTag("选择结束时间");
                            tempDialog.SetDate(tmpStartTime);
                            tempDialog.ShowDialog();
                        }
                    } catch (Exception e) {
                    }
                }
            });
            calendar.add(10, -1);
            String tempStr2 = Common.dateFormat.format(calendar.getTime());
            this.m_StartTimeSpinner.SetClickCallback(true);
            this.m_StartTimeSpinner.SetTextJust(tempStr2);
            this.m_StartTimeSpinner.SetSelectReturnTag("点击选择起始时间");
            this.m_StartTimeSpinner.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Track.TrackQuery_Dailog.3
                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                public void OnClick(String paramString, Object pObject) {
                    try {
                        if (paramString.equals("点击选择起始时间")) {
                            Date tmpStartTime = Common.dateFormat.parse(String.valueOf(pObject));
                            DateSelect_Dailog tempDialog = new DateSelect_Dailog();
                            tempDialog.SetCallback(TrackQuery_Dailog.this.pCallback);
                            tempDialog.SetReturnTag("选择起始时间");
                            tempDialog.SetDate(tmpStartTime);
                            tempDialog.ShowDialog();
                        }
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    @SuppressLint("WrongConstant")
    private void DoCommand(String command) {
        try {
            if (command.equals("查询足迹")) {
                this.m_MyTableDataList.clear();
                String tempTime01 = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_trackQueryStartTime);
                String tempTime02 = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_trackQueryEndTime);
                try {
                    Date tmpStartTime = Common.dateFormat.parse(tempTime01);
                    Date tmpEndTime = Common.dateFormat.parse(tempTime02);
                    if (tmpStartTime.getTime() >= tmpEndTime.getTime()) {
                        Common.ShowDialog("查询起始时间大于或等于结束时间.");
                        return;
                    }
                    if (tmpStartTime.getYear() == tmpEndTime.getYear()) {
                        this.m_QueryResultLayout.setVisibility(0);
                        this._queryStartTime = tmpStartTime;
                        this._queryEndTime = tmpEndTime;
                        List<Polyline> tmpPolylines = QueryTrackLineData(this._queryStartTime, this._queryEndTime);
                        if (tmpPolylines.size() > 0) {
                            for (Polyline tmpPolyline : tmpPolylines) {
                                HashMap<String, Object> tmpHash = new HashMap<>();
                                tmpHash.put("D1", false);
                                List<TrackPoint> tmpTrackPoints = (List) tmpPolyline.getTAGObject();
                                tmpHash.put("D2", tmpTrackPoints.get(0)._TimeStr);
                                tmpHash.put("D3", tmpTrackPoints.get(tmpTrackPoints.size() - 1)._TimeStr);
                                tmpHash.put("D4", Integer.valueOf(tmpPolyline.getCoordsCount()));
                                tmpHash.put("D5", Common.SimplifyLength(tmpPolyline.getLength(true)));
                                tmpHash.put("D6", tmpPolyline);
                                this.m_MyTableDataList.add(tmpHash);
                            }
                        } else {
                            Common.ShowDialog("该时间段内未查询到有效足迹线.");
                        }
                    } else {
                        Common.ShowDialog("无法查询跨年度的数据,请分年份进行查询.");
                    }
                    this.m_MyTableFactory.notifyDataSetChanged();
                } catch (Exception e) {
                    Common.ShowDialog("时间格式输入不正确.请采用如下格式:\r\n" + Common.dateFormat.format(new Date()));
                } finally {
                    this.m_MyTableFactory.notifyDataSetChanged();
                }
            } else if (command.equals("显示足迹在地图中")) {
                Envelope tmpExtendEnvelope = null;
                boolean tmpBool = true;
                new ArrayList();
                for (HashMap<String, Object> tmpHash2 : this.m_MyTableDataList) {
                    if (Boolean.parseBoolean(String.valueOf(tmpHash2.get("D1")))) {
                        tmpBool = false;
                        Polyline tmpPolyline2 = (Polyline) tmpHash2.get("D6");
                        List<TrackPoint> tmpTrackPoints2 = (List) tmpPolyline2.getTAGObject();
                        TrackLine tmpTrackLine = PubVar._PubCommand.GetTrackLine();
                        int count = tmpPolyline2.getCoordsCount();
                        for (int i = 0; i < count; i++) {
                            tmpTrackLine.AddSingleTrackPoint(tmpTrackPoints2.get(i), tmpPolyline2.GetAllCoordinateList().get(i));
                        }
                        PubVar._PubCommand.m_ConfigDB.GetSymbolManage();
                        ISymbol tmpSymbol = SymbolManage.GetLineSymbol("#FFFFFF14,8.0@#FFFF001B,3.0");
                        GraphicSymbolGeometry tmpGraphicSymbolGeo = new GraphicSymbolGeometry();
                        tmpGraphicSymbolGeo._Geoemtry = tmpPolyline2;
                        tmpGraphicSymbolGeo._Symbol = tmpSymbol;
                        tmpGraphicSymbolGeo._GeometryType = "TrackLine";
                        PubVar._MapView._GraphicLayer.AddGeometry(tmpGraphicSymbolGeo);
                        if (tmpExtendEnvelope == null) {
                            tmpExtendEnvelope = tmpPolyline2.CalEnvelope();
                        } else {
                            tmpExtendEnvelope.MergeEnvelope2(tmpPolyline2.CalEnvelope());
                        }
                    }
                }
                if (tmpBool) {
                    Common.ShowDialog("没有勾选任何足迹线.");
                    return;
                }
                PubVar._MapView.invalidate();
                Envelope finalTmpExtendEnvelope = tmpExtendEnvelope;
                Common.ShowYesNoDialog(this._Dialog.getContext(), "已绘制选中的足迹线至地图中.\r\n是否缩放地图至足迹线范围内?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Track.TrackQuery_Dailog.4
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command2, Object pObject) {
                        if (command2.equals("YES")) {
                            if (finalTmpExtendEnvelope != null) {
                                PubVar._Map.ZoomToExtend(finalTmpExtendEnvelope);
                            }
                            TrackQuery_Dailog.this._Dialog.dismiss();
                        }
                    }
                });
            } else if (command.equals("删除查询足迹")) {
                final List<Polyline> tmpList = new ArrayList<>();
                final List<Integer> tmpList2 = new ArrayList<>();
                int tmpTid = -1;
                for (HashMap<String, Object> tmpHash3 : this.m_MyTableDataList) {
                    tmpTid++;
                    if (Boolean.parseBoolean(String.valueOf(tmpHash3.get("D1")))) {
                        tmpList.add((Polyline) tmpHash3.get("D6"));
                        tmpList2.add(Integer.valueOf(tmpTid));
                    }
                }
                if (tmpList.size() > 0) {
                    Common.ShowYesNoDialog(this._Dialog.getContext(), "是否删除选择的" + String.valueOf(tmpList.size()) + "条足迹线?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Track.TrackQuery_Dailog.5
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (paramString.equals("YES")) {
                                boolean tempBool = false;
                                for (int j = tmpList.size() - 1; j > -1; j--) {
                                    List<TrackPoint> tmpTrackPoints3 = (List) ((Polyline) tmpList.get(j)).getTAGObject();
                                    Date tmpStartTimeDate = new Date(tmpTrackPoints3.get(0)._Time);
                                    Date tmpEndTimeDate = new Date(tmpTrackPoints3.get(tmpTrackPoints3.size() - 1)._Time);
                                    int startMonth = tmpStartTimeDate.getMonth() + 1;
                                    int endMonth = tmpEndTimeDate.getMonth() + 1;
                                    for (int i2 = startMonth; i2 <= endMonth; i2++) {
                                        if (PubVar._PubCommand.m_ProjectDB.GetTrackManage().GetSQLiteDatabase().ExecuteSQL("Delete From T" + String.format("%02d", Integer.valueOf(i2)) + " Where Time>=" + tmpStartTimeDate.getTime() + " And Time<=" + tmpEndTimeDate.getTime())) {
                                            tempBool = true;
                                        }
                                    }
                                    TrackQuery_Dailog.this.m_MyTableDataList.remove(((Integer) tmpList2.get(j)).intValue());
                                }
                                if (tempBool) {
                                    TrackQuery_Dailog.this.m_MyTableFactory.notifyDataSetChanged();
                                }
                            }
                        }
                    });
                } else {
                    Common.ShowDialog("请勾选要删除的足迹线.");
                }
            } else if (command.equals("导出足迹")) {
                List<Polyline> tmpList3 = new ArrayList<>();
                for (HashMap<String, Object> tmpHash4 : this.m_MyTableDataList) {
                    if (Boolean.parseBoolean(String.valueOf(tmpHash4.get("D1")))) {
                        tmpList3.add((Polyline) tmpHash4.get("D6"));
                    }
                }
                if (tmpList3.size() > 0) {
                    TrackExportDialog tempDialog = new TrackExportDialog();
                    tempDialog.SetExportTrackPolylines(tmpList3);
                    tempDialog.ShowDialog();
                    return;
                }
                Common.ShowDialog("请勾选要导出的足迹线.");
            } else if (command.equals("全选")) {
                for (HashMap<String, Object> tmpHash5 : this.m_MyTableDataList) {
                    tmpHash5.put("D1", true);
                }
                this.m_MyTableFactory.notifyDataSetChanged();
            } else if (command.equals("反选")) {
                for (HashMap<String, Object> tmpHash6 : this.m_MyTableDataList) {
                    tmpHash6.put("D1", Boolean.valueOf(!Boolean.parseBoolean(String.valueOf(tmpHash6.get("D1")))));
                }
                this.m_MyTableFactory.notifyDataSetChanged();
            }
        } catch (Exception e2) {
        }
    }

    public static List<Polyline> QueryTrackLineData(Date queryStartTime, Date queryEndTime) {
        List<Polyline> tempList = new ArrayList<>();
        try {
            int startMonth = queryStartTime.getMonth() + 1;
            int endMonth = queryEndTime.getMonth() + 1;
            for (int i = startMonth; i <= endMonth; i++) {
                SQLiteReader tempReader = PubVar._PubCommand.m_ProjectDB.GetTrackManage().GetSQLiteDatabase().Query("Select Time,JD,WD,GC,ACCURACY,F1,SPEED,Direction From T" + String.format("%02d", Integer.valueOf(i)) + " Where Time>=" + queryStartTime.getTime() + " And Time<=" + queryEndTime.getTime());
                if (tempReader != null) {
                    List<Coordinate> tmpCoords = new ArrayList<>();
                    List<TrackPoint> tmpTrackPtns = new ArrayList<>();
                    while (tempReader.Read()) {
                        TrackPoint tmpPoint = new TrackPoint();
                        tmpPoint._Time = tempReader.GetLong(0);
                        tmpPoint._Longitude = tempReader.GetDouble(1);
                        tmpPoint._Latitude = tempReader.GetDouble(2);
                        tmpPoint._Elevation = tempReader.GetDouble(3);
                        tmpPoint._Accuracy = tempReader.GetDouble(4);
                        tmpPoint._Speed = tempReader.GetDouble(6);
                        tmpPoint._Direction = tempReader.GetDouble(7);
                        tmpPoint._TimeStr = Common.dateFormat.format(new Date(tmpPoint._Time));
                        String tempStr = tempReader.GetString(5);
                        if (tempStr != null && tempStr.equals("1")) {
                            tmpPoint._IsFirst = true;
                            if (tmpCoords.size() > 1) {
                                Polyline tmpPolyline = new Polyline();
                                tmpPolyline.SetAllCoordinateList(tmpCoords);
                                tmpPolyline.SetTAGObject(tmpTrackPtns);
                                tempList.add(tmpPolyline);
                            }
                            tmpCoords = new ArrayList<>();
                            tmpTrackPtns = new ArrayList<>();
                        }
                        tmpCoords.add(PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(tmpPoint._Longitude, tmpPoint._Latitude, tmpPoint._Elevation, Coordinate_WGS1984.Instance()));
                        tmpTrackPtns.add(tmpPoint);
                    }
                    tempReader.Close();
                    if (tmpCoords.size() > 1) {
                        Polyline tmpPolyline2 = new Polyline();
                        tmpPolyline2.SetAllCoordinateList(tmpCoords);
                        tmpPolyline2.SetTAGObject(tmpTrackPtns);
                        tempList.add(tmpPolyline2);
                    }
                }
            }
        } catch (Exception e) {
        }
        return tempList;
    }

    public static List<TrackPoint> QueryTrackData(Date queryStartTime, Date queryEndTime) {
        List<TrackPoint> tempList = new ArrayList<>();
        try {
            int startMonth = queryStartTime.getMonth() + 1;
            int endMonth = queryEndTime.getMonth() + 1;
            for (int i = startMonth; i <= endMonth; i++) {
                SQLiteReader tempReader = PubVar._PubCommand.m_ProjectDB.GetTrackManage().GetSQLiteDatabase().Query("Select Time,JD,WD,GC,ACCURACY,F1 From T" + String.format("%02d", Integer.valueOf(i)) + " Where Time>=" + queryStartTime.getTime() + " And Time<=" + queryEndTime.getTime());
                if (tempReader != null) {
                    while (tempReader.Read()) {
                        TrackPoint tmpPoint = new TrackPoint();
                        tmpPoint._Time = tempReader.GetLong(0);
                        tmpPoint._Longitude = tempReader.GetDouble(1);
                        tmpPoint._Latitude = tempReader.GetDouble(2);
                        tmpPoint._Elevation = tempReader.GetDouble(3);
                        tmpPoint._Accuracy = tempReader.GetDouble(4);
                        tmpPoint._TimeStr = Common.dateFormat.format(new Date(tmpPoint._Time));
                        String tempStr = tempReader.GetString(5);
                        if (tempStr != null && tempStr.equals("1")) {
                            tmpPoint._IsFirst = true;
                        }
                        tempList.add(tmpPoint);
                    }
                    tempReader.Close();
                }
            }
        } catch (Exception e) {
        }
        return tempList;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Track.TrackQuery_Dailog.6
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                TrackQuery_Dailog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                TrackQuery_Dailog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
