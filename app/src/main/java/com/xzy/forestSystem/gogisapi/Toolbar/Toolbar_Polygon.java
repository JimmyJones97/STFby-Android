package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.MapTools;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.CommonMath;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.LoadCoordsFile_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.AddNextPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.AddPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.DeleteObject;
import  com.xzy.forestSystem.gogisapi.Edit.PolygonEditClass;
import  com.xzy.forestSystem.gogisapi.Edit.PolygonReshape_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.PolygonSplitByPoly_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.PolygonUnion_Dialog;
import  com.xzy.forestSystem.gogisapi.GPS.AddGPSPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.BaseDataObject;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSource;
import  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Selection;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.MyControls.Input2_Dialog;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Clip;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.PolyDefault;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.PolySimple;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.SpatialRelation;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Toolbar_Polygon extends BaseToolbar {
    static final double SPLIT_BIAS = 0.001d;
    private Drawable _GPSRecordBg01 = null;
    private Drawable _GPSRecordBg02 = null;
    private boolean _IsRecordByGPS = false;
    private Coordinate _LastCoord = null;
    private long _LastLocTime = 0;
    private DataSet m_DataSet = null;
    private BaseDataObject m_EditBaseDataObject = null;
    private GeoLayer m_EditGeoLayer = null;
    private String m_EditLayerID = null;
    private Button m_GPSBtn = null;
    private List<HashMap<String, Object>> m_HistoryList = new ArrayList();
    private List<HashMap<String, Object>> m_HistoryList2 = new ArrayList();
    private PolygonEditClass m_PolygonEditClass = null;
    private Toolbar_Layers m_Toolbar_Layers = null;
    private Handler myGPSRecordHandler = new Handler();
    private Runnable myRecordLineByGPSTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon.2
        @Override // java.lang.Runnable
        public void run() {
            if (Toolbar_Polygon.this._IsRecordByGPS) {
                try {
                    Coordinate tempCoord = PubVar._PubCommand.m_GpsLocation.getGPSCoordinate();
                    if (!tempCoord.Equal(Toolbar_Polygon.this._LastCoord)) {
                        boolean tempBool = false;
                        long tempTime = new Date().getTime();
                        if (Toolbar_Polygon.this._LastCoord == null) {
                            tempBool = true;
                        } else if (PubVar.GPSGatherIntervalTime == 0 && PubVar.GPSGatherIntervalDistance == 0.0d) {
                            tempBool = true;
                        } else if (PubVar.GPSGatherIntervalTime == 0) {
                            if (Toolbar_Polygon.this._LastCoord == null) {
                                tempBool = true;
                            } else if (PubVar.GPSGatherIntervalDistance == 0.0d) {
                                tempBool = true;
                            } else if (Common.GetTwoPointDistance(tempCoord, Toolbar_Polygon.this._LastCoord) >= PubVar.GPSGatherIntervalDistance) {
                                tempBool = true;
                            }
                        } else if (PubVar.GPSGatherIntervalDistance == 0.0d) {
                            if (tempTime - Toolbar_Polygon.this._LastLocTime >= PubVar.GPSGatherIntervalTime) {
                                tempBool = true;
                            }
                        } else if (tempTime - Toolbar_Polygon.this._LastLocTime >= PubVar.GPSGatherIntervalTime) {
                            if (Toolbar_Polygon.this._LastCoord == null) {
                                tempBool = true;
                            } else if (PubVar.GPSGatherIntervalDistance == 0.0d) {
                                tempBool = true;
                            } else if (Common.GetTwoPointDistance(tempCoord, Toolbar_Polygon.this._LastCoord) >= PubVar.GPSGatherIntervalDistance) {
                                tempBool = true;
                            }
                        }
                        if (tempBool && (PubVar.GPS_Gather_Accuracy == 0.0d || PubVar._PubCommand.m_GpsLocation.accuracy <= PubVar.GPS_Gather_Accuracy)) {
                            Toolbar_Polygon.this._LastCoord = tempCoord;
                            Toolbar_Polygon.this._LastLocTime = tempTime;
                            if (Toolbar_Polygon.this.m_PolygonEditClass != null) {
                                Toolbar_Polygon.this.m_PolygonEditClass.SetCurrentDrawType("GPS");
                                Toolbar_Polygon.this.m_PolygonEditClass.AddPoint(tempCoord);
                            }
                        }
                    }
                } catch (Exception e) {
                }
                Toolbar_Polygon.this.myGPSRecordHandler.postDelayed(Toolbar_Polygon.this.myRecordLineByGPSTask, 1000);
            }
        }
    };
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
            List<Coordinate> tmpList;
            Object[] tmpObjs;
            Object[] tmpObjs2;
            try {
                if (paramString.equals("??????????????????")) {
                    Coordinate tempCoord = (Coordinate) pObject;
                    if (!(tempCoord == null || Toolbar_Polygon.this.m_PolygonEditClass == null)) {
                        PubVar._PubCommand.ProcessCommand("??????_?????????");
                        Toolbar_Polygon.this.m_PolygonEditClass.SetCurrentDrawType("??????");
                        Toolbar_Polygon.this.m_PolygonEditClass.AddPoint(tempCoord);
                        Common.ShowToast("???????????????????????????.");
                    }
                } else if (paramString.equals("??????GPS???????????????")) {
                    Coordinate tempCoord2 = (Coordinate) pObject;
                    if (!(tempCoord2 == null || Toolbar_Polygon.this.m_PolygonEditClass == null)) {
                        PubVar._PubCommand.ProcessCommand("??????_?????????");
                        Toolbar_Polygon.this.m_PolygonEditClass.SetCurrentDrawType("GPS");
                        Toolbar_Polygon.this.m_PolygonEditClass.AddPoint(tempCoord2);
                        Common.ShowToast("????????????GPS?????????.");
                    }
                } else if (paramString.equals("?????????????????????")) {
                    double[] tmpCoords = (double[]) pObject;
                    if (tmpCoords != null && tmpCoords.length > 1 && Toolbar_Polygon.this.m_PolygonEditClass != null && Toolbar_Polygon.this.m_PolygonEditClass.getPointsCount() > 0) {
                        Coordinate tmpCoord = Toolbar_Polygon.this.m_PolygonEditClass.GetLastCoordinate().Clone();
                        tmpCoord.SetOffset(tmpCoords[0], tmpCoords[1]);
                        PubVar._PubCommand.ProcessCommand("??????_?????????");
                        Toolbar_Polygon.this.m_PolygonEditClass.SetCurrentDrawType("??????");
                        Toolbar_Polygon.this.m_PolygonEditClass.AddPoint(tmpCoord);
                        Common.ShowToast("???????????????????????????.");
                    }
                } else if (paramString.equals("MoveFeatureMouseDown")) {
                    if (!(pObject == null || (tmpObjs2 = (Object[]) pObject) == null || tmpObjs2.length <= 1 || tmpObjs2[1] == null)) {
                        Object tmpTimeString01 = String.valueOf(tmpObjs2[0]);
                        HashMap<String, Object> tmpModifyHashMap = new HashMap<>();
                        tmpModifyHashMap.put("Type", "Move");
                        tmpModifyHashMap.put("Time", tmpTimeString01);
                        List<HashMap<String, Object>> tmpListOld01 = new ArrayList<>();
                        for (Object tmpSYSID : (List) tmpObjs2[1]) {
                            AbstractGeometry tmpGeometry = Toolbar_Polygon.this.m_DataSet.GetGeometryBySYSID((String) tmpSYSID);
                            if (tmpGeometry != null) {
                                List<Coordinate> tmpLsit01 = new ArrayList<>();
                                for (Coordinate coordinate : tmpGeometry.GetAllCoordinateList()) {
                                    tmpLsit01.add(coordinate.Clone());
                                }
                                HashMap<String, Object> tmpHashMapOld = new HashMap<>();
                                tmpHashMapOld.put("SYS_ID", tmpSYSID);
                                tmpHashMapOld.put("Coords", tmpLsit01);
                                tmpListOld01.add(tmpHashMapOld);
                            }
                        }
                        tmpModifyHashMap.put("Old", tmpListOld01);
                        Toolbar_Polygon.this.m_HistoryList.add(tmpModifyHashMap);
                    }
                } else if (paramString.equals("MoveFeatureMouseUp")) {
                    if (!(pObject == null || (tmpObjs = (Object[]) pObject) == null || tmpObjs.length <= 1 || tmpObjs[1] == null)) {
                        String tmpTimeLongString = String.valueOf(tmpObjs[0]);
                        if (Toolbar_Polygon.this.m_HistoryList.size() > 0) {
                            HashMap<String, Object> tmpModifyHashMap2 = (HashMap) Toolbar_Polygon.this.m_HistoryList.get(Toolbar_Polygon.this.m_HistoryList.size() - 1);
                            if (String.valueOf(tmpModifyHashMap2.get("Type")).equals("Move") && tmpTimeLongString.equals(String.valueOf(tmpModifyHashMap2.get("Time")))) {
                                List<HashMap<String, Object>> tmpListModify02 = new ArrayList<>();
                                for (Object tmpSYSID2 : (List) tmpObjs[1]) {
                                    AbstractGeometry tmpGeometry2 = Toolbar_Polygon.this.m_DataSet.GetGeometryBySYSID((String) tmpSYSID2);
                                    if (tmpGeometry2 != null) {
                                        HashMap<String, Object> tmpHashMapModify = new HashMap<>();
                                        tmpHashMapModify.put("SYS_ID", tmpSYSID2);
                                        tmpHashMapModify.put("Coords", tmpGeometry2.GetAllCoordinateList());
                                        tmpListModify02.add(tmpHashMapModify);
                                    }
                                }
                                tmpModifyHashMap2.put("Modify", tmpListModify02);
                            }
                        }
                    }
                } else if (paramString.equals("????????????")) {
                    HashMap<String, Object> tmpModifyHashMap3 = (HashMap) pObject;
                    if (tmpModifyHashMap3 != null) {
                        Toolbar_Polygon.this.m_HistoryList.add(tmpModifyHashMap3);
                        Toolbar_Polygon.this.m_HistoryList2.clear();
                    }
                } else if (paramString.equals("?????????????????????")) {
                    HashMap<String, Object> tmpModifyHashMap4 = (HashMap) pObject;
                    if (tmpModifyHashMap4 != null) {
                        Toolbar_Polygon.this.m_HistoryList.add(tmpModifyHashMap4);
                        Toolbar_Polygon.this.m_HistoryList2.clear();
                    }
                } else if (paramString.equals("??????????????????")) {
                    HashMap<String, Object> tmpModifyHashMap5 = (HashMap) pObject;
                    if (tmpModifyHashMap5 != null) {
                        Toolbar_Polygon.this.m_HistoryList.add(tmpModifyHashMap5);
                        Toolbar_Polygon.this.m_HistoryList2.clear();
                    }
                } else if (paramString.equals("????????????????????????") && (tmpList = (List) pObject) != null && tmpList.size() > 0 && Toolbar_Polygon.this.m_PolygonEditClass != null) {
                    PubVar._PubCommand.ProcessCommand("??????_?????????");
                    Toolbar_Polygon.this.m_PolygonEditClass.SetCurrentDrawType("????????????");
                    Coordinate tmpLastCoordinate = null;
                    for (Coordinate tmpCoordinate : tmpList) {
                        Toolbar_Polygon.this.m_PolygonEditClass.AddPoint(tmpCoordinate);
                        tmpLastCoordinate = tmpCoordinate;
                    }
                    if (tmpLastCoordinate != null) {
                        Toolbar_Polygon.this.m_MapView.getMap().ZoomToCenter(tmpLastCoordinate);
                    }
                    Common.ShowToast("????????????????????????.");
                }
            } catch (Exception e) {
            }
        }
    };

    public Toolbar_Polygon(Context context, MapView mapView) {
        super(context, mapView);
        this.m_ToolbarName = "??????????????????";
        this.m_IsAllowedClose = false;
    }

    public void SetToolbar_Layers(Toolbar_Layers toolbar) {
        this.m_Toolbar_Layers = toolbar;
    }

    public void SetEditLayerID(String layerID) {
        try {
            this.m_EditLayerID = layerID;
            this.m_DataSet = PubVar.m_Workspace.GetDataSourceByEditing().GetDatasetByName(this.m_EditLayerID);
            this.m_EditGeoLayer = PubVar._Map.GetGeoLayerByName(this.m_EditLayerID);
            if (this.m_EditGeoLayer != null) {
                PubVar._MapView.AddMapTextObject("Toolbar_Polygon", "???????????????" + this.m_EditGeoLayer.getLayerName() + "???", ((float) (PubVar._MapView.getWidth() / 2)) + (10.0f * PubVar.ScaledDensity), 80.0f * PubVar.ScaledDensity, "#ffffffff");
            }
        } catch (Exception e) {
        }
    }

    public void SetEditPolygon(PolygonEditClass polyEditClass) {
        this.m_PolygonEditClass = polyEditClass;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void LoadToolBar(View view, int mainLinearLayoutID) {
        super.LoadToolBar(view, mainLinearLayoutID);
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolygonAddHand));
        this.buttonIDs.put("????????????", Integer.valueOf((int) R.id.bt_PolygonAddFreeHand));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolygonChangeDir));
        this.buttonIDs.put("GPS", Integer.valueOf((int) R.id.bt_PolygonAddByGPS));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolygonByCoord));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_Polygon_AddGPSPtn));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolygonPtnUndo));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolygonPtnRedo));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolygonAttribute));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolygonCancel));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolygonGenerate));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolyEdit_MoveVertex));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolyEdit_AddVertex));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolyEdit_DelVertex));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolyEdit_SplitLine));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolyEdit_SplitPoly));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolyEdit_Union));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolyEdit_Reshape));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolygonMove));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolygonDelete));
        this.buttonIDs.put("?????????", Integer.valueOf((int) R.id.bt_PolygonByAddNextPoint));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolygonHole));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolyEdit_UnionLineToPoly));
        this.buttonIDs.put("?????????", Integer.valueOf((int) R.id.bt_PolygonPtnUndoPoint));
        this.buttonIDs.put("?????????", Integer.valueOf((int) R.id.bt_PolygonPtnRedoPoint));
        this.buttonIDs.put("????????????", Integer.valueOf((int) R.id.bt_Polygon_FromFile));
        this.buttonIDs.put("????????????", Integer.valueOf((int) R.id.bt_PolyEdit_DrawRect));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_Polygon_Paste));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolygonExplode));
        this.m_GPSBtn = (Button) this.m_view.findViewById(R.id.bt_PolygonAddByGPS);
        this.m_view.setOnTouchListener(this.touchListener);
        this.m_baseLayout.setOnTouchListener(this.touchListener);
        this.buttonIDs.put("????????????", Integer.valueOf((int) R.id.bt_ToolbarPolygon_AddInCenter));
        this.m_view.findViewById(R.id.bt_ToolbarPolygon_AddInCenter).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonAddHand).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonAddFreeHand).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonChangeDir).setOnClickListener(this.buttonClickListener);
        this.m_GPSBtn.setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonByCoord).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_Polygon_AddGPSPtn).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonPtnUndo).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonPtnRedo).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonAttribute).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonCancel).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonGenerate).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_MoveVertex).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_AddVertex).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_DelVertex).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_SplitLine).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_SplitPoly).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_Union).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_Exit).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_Reshape).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonMove).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonDelete).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonByAddNextPoint).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonHole).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_UnionLineToPoly).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonPtnUndoPoint).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonPtnRedoPoint).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_Polygon_FromFile).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_DrawRect).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_Polygon_Paste).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolygonExplode).setOnClickListener(this.buttonClickListener);
        this._GPSRecordBg01 = PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.poly_gps_start48);
        this._GPSRecordBg01.setBounds(0, 0, this._GPSRecordBg01.getMinimumWidth(), this._GPSRecordBg01.getMinimumHeight());
        this._GPSRecordBg02 = PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.poly_gps_stop48);
        this._GPSRecordBg02.setBounds(0, 0, this._GPSRecordBg02.getMinimumWidth(), this._GPSRecordBg02.getMinimumHeight());
        ((Button) this.m_view.findViewById(R.id.bt_PolygonAddHand)).setOnLongClickListener(new View.OnLongClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon.3
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View arg0) {
                Common.ShowYesNoDialog(Toolbar_Polygon.this.m_context, "?????????????????????????", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon.3.1
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        if (paramString.equals("YES")) {
                            Toolbar_Polygon.this.Hide();
                        }
                    }
                });
                return false;
            }
        });
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Show() {
        super.Show();
        SetEditPolygon(PubVar._PubCommand.GetPolygonEdit());
        StartEdit();
        this.m_PolygonEditClass.IsDrawInMapCenter = true;
        PubVar._PubCommand.getMapView().setActiveTools(MapTools.AddPolygon, this.m_PolygonEditClass, this.m_PolygonEditClass);
        PubVar._PubCommand.getMapView()._CurrentEditPaint = this.m_PolygonEditClass;
        PubVar._PubCommand.getMapView().invalidate();
        this.m_HistoryList = new ArrayList();
        this.m_HistoryList2 = new ArrayList();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Hide() {
        DoCommand("??????GPS??????");
        this.m_PolygonEditClass.IsDrawInMapCenter = false;
        PubVar._PubCommand.HideToolbar("????????????????????????");
        super.Hide();
        if (this.m_PolygonEditClass != null) {
            this.m_PolygonEditClass.Clear();
        }
        PubVar._MapView.RemoveMapTextObject("Toolbar_Polygon");
        try {
            if (this.m_EditLayerID != null) {
                final GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByName(this.m_EditLayerID);
                if (tmpGeoLayer == null || !tmpGeoLayer.getDataset().getEdited()) {
                    if (this.m_HistoryList.size() > 0) {
                        this.m_HistoryList.clear();
                    }
                    if (this.m_HistoryList2.size() > 0) {
                        this.m_HistoryList2.clear();
                    }
                } else {
                    Common.ShowYesNoDialogWithAlert(PubVar.MainContext, "???????????????????????????.\r\n?????????????", true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon.4
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            try {
                                if (paramString.equals("YES")) {
                                    if (tmpGeoLayer != null) {
                                        String[] tmpoutMsg = new String[1];
                                        if (!tmpGeoLayer.getDataset().Save(tmpoutMsg)) {
                                            Common.ShowDialog(tmpoutMsg[0]);
                                        }
                                    }
                                } else if (paramString.equals("NO")) {
                                    while (Toolbar_Polygon.this.m_HistoryList.size() > 0) {
                                        try {
                                            Toolbar_Polygon.this.cancelOperator();
                                        } catch (Exception e) {
                                        }
                                    }
                                    String[] tmpoutMsg2 = new String[1];
                                    if (!tmpGeoLayer.getDataset().Save(tmpoutMsg2)) {
                                        Common.ShowToast(tmpoutMsg2[0]);
                                    }
                                    PubVar._Map.Refresh();
                                }
                            } catch (Exception e2) {
                            }
                            if (Toolbar_Polygon.this.m_HistoryList.size() > 0) {
                                Toolbar_Polygon.this.m_HistoryList.clear();
                            }
                            if (Toolbar_Polygon.this.m_HistoryList2.size() > 0) {
                                Toolbar_Polygon.this.m_HistoryList2.clear();
                            }
                        }
                    });
                }
            } else {
                if (this.m_HistoryList.size() > 0) {
                    this.m_HistoryList.clear();
                }
                if (this.m_HistoryList2.size() > 0) {
                    this.m_HistoryList2.clear();
                }
            }
        } catch (Exception e) {
        }
        this.m_EditLayerID = null;
        PubVar._PubCommand.m_MapCompLayoutToolbar.clearEditFeatureayer();
        super.SaveConfigDB();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean cancelOperator() {
        List<HashMap<String, Object>> tmpListRemove;
        List<HashMap<String, Object>> tmpListNew;
        List<HashMap<String, Object>> tmpListOld;
        if (this.m_HistoryList.size() <= 0) {
            return false;
        }
        int tmpRecordCount = this.m_HistoryList.size() - 1;
        HashMap<String, Object> tmpHashMap = this.m_HistoryList.get(tmpRecordCount);
        if (tmpHashMap.containsKey("Old") && (tmpListOld = (List) tmpHashMap.get("Old")) != null && tmpListOld.size() > 0) {
            for (HashMap<String, Object> tmpHashMap2 : tmpListOld) {
                Polygon tmpPoly = (Polygon) this.m_DataSet.GetGeometryBySYSID(String.valueOf(tmpHashMap2.get("SYS_ID")));
                if (tmpPoly != null) {
                    tmpPoly.ResetData();
                    tmpPoly.SetAllCoordinateList((List) tmpHashMap2.get("Coords"));
                }
            }
        }
        if (tmpHashMap.containsKey("New") && (tmpListNew = (List) tmpHashMap.get("New")) != null && tmpListNew.size() > 0) {
            for (HashMap<String, Object> tmpHashMap22 : tmpListNew) {
                Polygon tmpPoly2 = (Polygon) this.m_DataSet.GetGeometryBySYSID(String.valueOf(tmpHashMap22.get("SYS_ID")));
                if (tmpPoly2 != null) {
                    DeleteObject.DeleteGeoemtryInDataset(this.m_DataSet, tmpPoly2);
                }
            }
        }
        if (tmpHashMap.containsKey("Remove") && (tmpListRemove = (List) tmpHashMap.get("Remove")) != null && tmpListRemove.size() > 0) {
            for (HashMap<String, Object> tmpHashMap23 : tmpListRemove) {
                Polygon tmpPoly3 = (Polygon) this.m_DataSet.GetGeometryBySYSID(String.valueOf(tmpHashMap23.get("SYS_ID")));
                if (tmpPoly3 != null) {
                    DeleteObject.UnDeleteGeoemtryInDataset(this.m_DataSet, tmpPoly3);
                }
            }
        }
        this.m_HistoryList2.add(tmpHashMap);
        this.m_HistoryList.remove(tmpRecordCount);
        return true;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(View view) {
        Object tempObj = view.getTag();
        if (tempObj != null) {
            String command = tempObj.toString();
            if (command.equals("????????????")) {
                if (this.m_Toolbar_Layers != null) {
                    this.m_Toolbar_Layers.DoCommand("??????????????????");
                }
                Hide();
            } else if (command.equals("????????????")) {
                if (view.isSelected()) {
                    SetButtonSelectedStatus("??????", false);
                    PubVar._PubCommand.ProcessCommand("?????????");
                    return;
                }
                int tempCount = 0;
                Selection tmpSel = PubVar._MapView.getMap().getFeatureSelectionByLayerID(this.m_EditLayerID, false);
                if (tmpSel != null) {
                    tempCount = tmpSel.getCount();
                }
                if (tempCount == 0) {
                    Common.ShowDialog("???????????????????????????????????????.\r\n?????????????????????????????????.");
                    return;
                }
                PubVar._MapView.setActiveTool(MapTools.MoveObject);
                PubVar._MapView.getMoveFeature().SetMoveLayer(this.m_EditLayerID);
                PubVar._MapView.getMoveFeature().SetCallback(this.pCallback);
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("???????????????;???????????????");
                SetButtonSelectedStatus("??????", true);
            } else if (command.equals("????????????")) {
                int tempCount2 = 0;
                Selection tmpSel2 = PubVar._MapView.getMap().getFeatureSelectionByLayerID(this.m_EditLayerID, false);
                if (tmpSel2 != null) {
                    tempCount2 = tmpSel2.getCount();
                }
                final List<String> tmpSYSIDList = tmpSel2.getSYSIDList();
                if (tempCount2 == 0) {
                    Common.ShowDialog("???????????????????????????????????????.");
                } else {
                    Common.ShowYesNoDialog(this.m_context, "?????????" + tempCount2 + "?????????.\n????????????????????????,?????????????", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon.5
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command2, Object pObject) {
                            if (command2.equals("YES") && PubVar._PubCommand.GetDeleteObject().Delete(Toolbar_Polygon.this.m_EditLayerID, tmpSYSIDList, false)) {
                                HashMap<String, Object> tmpModifyHashMap = new HashMap<>();
                                tmpModifyHashMap.put("Type", "Delete");
                                List<HashMap<String, Object>> tmpListOld01 = new ArrayList<>();
                                List<HashMap<String, Object>> tmpListModify02 = new ArrayList<>();
                                List<HashMap<String, Object>> tmpListNew03 = new ArrayList<>();
                                List<HashMap<String, Object>> tmpListRemove04 = new ArrayList<>();
                                for (String tmpSysID : tmpSYSIDList) {
                                    HashMap<String, Object> tmpHashMapOld = new HashMap<>();
                                    tmpHashMapOld.put("SYS_ID", tmpSysID);
                                    tmpListRemove04.add(tmpHashMapOld);
                                }
                                tmpModifyHashMap.put("Old", tmpListOld01);
                                tmpModifyHashMap.put("Modify", tmpListModify02);
                                tmpModifyHashMap.put("New", tmpListNew03);
                                tmpModifyHashMap.put("Remove", tmpListRemove04);
                                Toolbar_Polygon.this.m_HistoryList.add(tmpModifyHashMap);
                                PubVar._MapView._Select.ClearAllSelection();
                                PubVar._Map.RefreshFast();
                            }
                        }
                    });
                }
            } else if (command.equals("???????????????")) {
                if (getButtonIsSelected("????????????")) {
                    this.m_PolygonEditClass.SetHandFreeMode(false);
                    PubVar._PubCommand.ProcessCommand("????????????");
                    Common.ShowToast("??????????????????");
                    return;
                }
                DoCommand(command);
            } else if (command.equals("?????????")) {
                if (getButtonIsSelected("??????")) {
                    PubVar._PubCommand.ProcessCommand("????????????");
                    Common.ShowToast("???????????????");
                    return;
                }
                DoCommand(command);
            } else if (command.equals("??????")) {
                int tmpInt01 = 0;
                int tmpInt02 = 0;
                final List<HashMap<String, Object>> tmpPolyList = Common.GetSelectObjectsInLayer(PubVar._Map, this.m_EditLayerID, false, new BasicValue());
                if (tmpPolyList.size() <= 0) {
                    Common.ShowDialog("?????????????????????????????????????????????????????????.");
                    return;
                }
                for (HashMap<String, Object> tmpHash : tmpPolyList) {
                    if (Boolean.parseBoolean(String.valueOf(tmpHash.get("Editable")))) {
                        Polygon tmpPoly = (Polygon) tmpHash.get("Geometry");
                        if (!tmpPoly.IsSimple()) {
                            tmpInt01 += tmpPoly.getPartCount();
                            tmpInt02++;
                        }
                    }
                }
                if (tmpInt01 == 0) {
                    Common.ShowDialog("??????????????????????????????????????????,?????????????????????Part????????????.");
                } else {
                    Common.ShowYesNoDialogWithAlert(this.m_context, "?????????" + String.valueOf(tmpInt02) + "????????????????????????.\r\n?????????????????????" + String.valueOf(tmpInt01) + "?????????.\r\n\r\n????????????????????????,?????????????", true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon.6
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command2, Object pObject) {
                            if (command2.equals("YES")) {
                                DataSet pDataset = PubVar.m_Workspace.GetDatasetByName(Toolbar_Polygon.this.m_EditLayerID);
                                for (HashMap<String, Object> tmpHash2 : tmpPolyList) {
                                    if (Boolean.parseBoolean(String.valueOf(tmpHash2.get("Editable")))) {
                                        Polygon tmpPoly2 = (Polygon) tmpHash2.get("Geometry");
                                        if (!tmpPoly2.IsSimple()) {
                                            List<Polygon> tmpPolygons = Polygon.ExplodePolygons(tmpPoly2);
                                            if (tmpPolygons.size() > 1) {
                                                List<String> tmpFieldValues = pDataset.getGeometryFieldValues(tmpPoly2.GetSYS_ID(), true);
                                                HashMap<String, Object> tmpModifyHashMap = new HashMap<>();
                                                tmpModifyHashMap.put("Type", "Modify");
                                                List<HashMap<String, Object>> tmpListOld01 = new ArrayList<>();
                                                List<HashMap<String, Object>> tmpListModify02 = new ArrayList<>();
                                                List<HashMap<String, Object>> tmpListNew03 = new ArrayList<>();
                                                List<HashMap<String, Object>> tmpListRemove04 = new ArrayList<>();
                                                for (Polygon tmpPolygon : tmpPolygons) {
                                                    DataSet.SaveNewPolygon(tmpPolygon, tmpFieldValues, pDataset, Toolbar_Polygon.this.m_EditLayerID);
                                                    HashMap<String, Object> tmpHashMapNew = new HashMap<>();
                                                    tmpHashMapNew.put("SYS_ID", tmpPolygon.GetSYS_ID());
                                                    tmpHashMapNew.put("Coords", tmpPolygon.GetAllCoordinateList());
                                                    tmpListNew03.add(tmpHashMapNew);
                                                }
                                                DeleteObject.DeleteGeoemtryInDataset(Toolbar_Polygon.this.m_DataSet, tmpPoly2);
                                                HashMap<String, Object> tmpHashMapRemove = new HashMap<>();
                                                tmpHashMapRemove.put("SYS_ID", tmpPoly2.GetSYS_ID());
                                                tmpHashMapRemove.put("Coords", tmpPoly2.GetAllCoordinateList());
                                                tmpListRemove04.add(tmpHashMapRemove);
                                                tmpModifyHashMap.put("Old", tmpListOld01);
                                                tmpModifyHashMap.put("Modify", tmpListModify02);
                                                tmpModifyHashMap.put("New", tmpListNew03);
                                                tmpModifyHashMap.put("Remove", tmpListRemove04);
                                                Toolbar_Polygon.this.m_HistoryList.add(tmpModifyHashMap);
                                            }
                                        }
                                    }
                                }
                                PubVar._MapView._Select.ClearAllSelection();
                                PubVar._Map.Refresh();
                            }
                        }
                    });
                }
            } else {
                DoCommand(command);
            }
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(String command) {
        GeoLayer tempGeoLayer;
        DataSource tmpDataSource;
        List<HashMap<String, Object>> tmpListRemove;
        List<HashMap<String, Object>> tmpListNew;
        List<HashMap<String, Object>> tmpListOld;
        List<HashMap<String, Object>> tmpListRemove2;
        List<HashMap<String, Object>> tmpListNew2;
        List<HashMap<String, Object>> tmpListOld2;
        try {
            if (command.equals("?????????")) {
                DoCommand("??????GPS??????");
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected(null);
                PubVar._PubCommand.ProcessCommand("??????_?????????");
                SetButtonSelectedStatus("??????", true);
                this.m_PolygonEditClass.SetIsEnabelDraw(true);
                this.m_PolygonEditClass.SetHandFreeMode(false);
            } else if (command.equals("???????????????")) {
                DoCommand("??????GPS??????");
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected(null);
                PubVar._PubCommand.ProcessCommand("??????_?????????");
                SetButtonSelectedStatus("????????????", true);
                this.m_PolygonEditClass.SetIsEnabelDraw(true);
                this.m_PolygonEditClass.SetHandFreeMode(true);
            } else if (command.equals("?????????")) {
                if (this.m_PolygonEditClass.ReversCoords()) {
                    PubVar._Map.ZoomToCenter(this.m_PolygonEditClass.GetLastCoordinate());
                }
            } else if (command.equals("GPS??????")) {
                if (!PubVar._PubCommand.m_GpsLocation.isOpen) {
                    Common.ShowDialog("GPS???????????????,????????????GPS??????.");
                }
                if (!PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                    Common.ShowDialog("GPS?????????,?????????...");
                }
                this.m_GPSBtn.setTag("??????GPS??????");
                this.m_GPSBtn.setCompoundDrawables(null, this._GPSRecordBg02, null, null);
                PubVar._PubCommand.ProcessCommand("??????_?????????");
                this.m_PolygonEditClass.SetIsEnabelDraw(false);
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("???????????????");
                SetButtonSelectedStatus("GPS", true);
                this._IsRecordByGPS = true;
                this.myGPSRecordHandler.post(this.myRecordLineByGPSTask);
            } else if (command.equals("??????GPS??????")) {
                this._IsRecordByGPS = false;
                this._LastCoord = null;
                this._LastLocTime = 0;
                this.m_GPSBtn.setTag("GPS??????");
                this.m_GPSBtn.setCompoundDrawables(null, this._GPSRecordBg01, null, null);
                SetButtonSelectedStatus("GPS", false);
            } else if (command.equals("????????????")) {
                AddPoint_Dialog tempDialog = new AddPoint_Dialog();
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
            } else if (command.equals("???????????????")) {
                if (this.m_PolygonEditClass == null || this.m_PolygonEditClass.getPointsCount() <= 0) {
                    Common.ShowDialog("??????????????????????????????????????????.");
                    return;
                }
                AddNextPoint_Dialog tmpDialog = new AddNextPoint_Dialog();
                tmpDialog.SetCallback(this.pCallback);
                tmpDialog.ShowDialog();
            } else if (command.equals("GPS??????")) {
                if (!PubVar._PubCommand.m_GpsLocation.isOpen) {
                    Common.ShowDialog("GPS???????????????,????????????GPS??????.");
                } else if (!PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                    Common.ShowDialog("GPS?????????,?????????...");
                } else {
                    AddGPSPoint_Dialog tempDialog2 = new AddGPSPoint_Dialog();
                    tempDialog2.SetCallback(this.pCallback);
                    tempDialog2.ShowDialog();
                }
            } else if (command.equals("??????")) {
                if (this.m_HistoryList.size() > 0) {
                    int tmpRecordCount = this.m_HistoryList.size() - 1;
                    HashMap<String, Object> tmpHashMap = this.m_HistoryList.get(tmpRecordCount);
                    if (tmpHashMap.containsKey("Old") && (tmpListOld2 = (List) tmpHashMap.get("Old")) != null && tmpListOld2.size() > 0) {
                        for (HashMap<String, Object> tmpHashMap2 : tmpListOld2) {
                            Polygon tmpPoly = (Polygon) this.m_DataSet.GetGeometryBySYSID(String.valueOf(tmpHashMap2.get("SYS_ID")));
                            if (tmpPoly != null) {
                                tmpPoly.ResetData();
                                tmpPoly.SetAllCoordinateList((List) tmpHashMap2.get("Coords"));
                            }
                        }
                    }
                    if (tmpHashMap.containsKey("New") && (tmpListNew2 = (List) tmpHashMap.get("New")) != null && tmpListNew2.size() > 0) {
                        for (HashMap<String, Object> tmpHashMap22 : tmpListNew2) {
                            Polygon tmpPoly2 = (Polygon) this.m_DataSet.GetGeometryBySYSID(String.valueOf(tmpHashMap22.get("SYS_ID")));
                            if (tmpPoly2 != null) {
                                DeleteObject.DeleteGeoemtryInDataset(this.m_DataSet, tmpPoly2);
                            }
                        }
                    }
                    if (tmpHashMap.containsKey("Remove") && (tmpListRemove2 = (List) tmpHashMap.get("Remove")) != null && tmpListRemove2.size() > 0) {
                        for (HashMap<String, Object> tmpHashMap23 : tmpListRemove2) {
                            Polygon tmpPoly3 = (Polygon) this.m_DataSet.GetGeometryBySYSID(String.valueOf(tmpHashMap23.get("SYS_ID")));
                            if (tmpPoly3 != null) {
                                DeleteObject.UnDeleteGeoemtryInDataset(this.m_DataSet, tmpPoly3);
                            }
                        }
                    }
                    this.m_HistoryList2.add(tmpHashMap);
                    this.m_HistoryList.remove(tmpRecordCount);
                    PubVar._Map.Refresh();
                    return;
                }
                Common.ShowToast("????????????????????????.");
            } else if (command.equals("??????")) {
                if (this.m_HistoryList2.size() > 0) {
                    int tmpRecordCount2 = this.m_HistoryList2.size() - 1;
                    HashMap<String, Object> tmpHashMap3 = this.m_HistoryList2.get(tmpRecordCount2);
                    String.valueOf(tmpHashMap3.get("Type"));
                    if (tmpHashMap3.containsKey("Modify") && (tmpListOld = (List) tmpHashMap3.get("Modify")) != null && tmpListOld.size() > 0) {
                        for (HashMap<String, Object> tmpHashMap24 : tmpListOld) {
                            Polygon tmpPoly4 = (Polygon) this.m_DataSet.GetGeometryBySYSID(String.valueOf(tmpHashMap24.get("SYS_ID")));
                            if (tmpPoly4 != null) {
                                tmpPoly4.ResetData();
                                tmpPoly4.SetAllCoordinateList((List) tmpHashMap24.get("Coords"));
                            }
                        }
                    }
                    if (tmpHashMap3.containsKey("New") && (tmpListNew = (List) tmpHashMap3.get("New")) != null && tmpListNew.size() > 0) {
                        for (HashMap<String, Object> tmpHashMap25 : tmpListNew) {
                            Polygon tmpPoly5 = (Polygon) this.m_DataSet.GetGeometryBySYSID(String.valueOf(tmpHashMap25.get("SYS_ID")));
                            if (tmpPoly5 != null) {
                                DeleteObject.UnDeleteGeoemtryInDataset(this.m_DataSet, tmpPoly5);
                            }
                        }
                    }
                    if (tmpHashMap3.containsKey("Remove") && (tmpListRemove = (List) tmpHashMap3.get("Remove")) != null && tmpListRemove.size() > 0) {
                        for (HashMap<String, Object> tmpHashMap26 : tmpListRemove) {
                            Polygon tmpPoly6 = (Polygon) this.m_DataSet.GetGeometryBySYSID(String.valueOf(tmpHashMap26.get("SYS_ID")));
                            if (tmpPoly6 != null) {
                                DeleteObject.DeleteGeoemtryInDataset(this.m_DataSet, tmpPoly6);
                            }
                        }
                    }
                    this.m_HistoryList.add(tmpHashMap3);
                    this.m_HistoryList2.remove(tmpRecordCount2);
                    PubVar._Map.Refresh();
                    return;
                }
                Common.ShowToast("????????????????????????.");
            } else if (command.equals("?????????")) {
                if (this.m_EditBaseDataObject != null) {
                    FeatureAttribute_Dialog tempDialog3 = new FeatureAttribute_Dialog();
                    tempDialog3.SetBaseDataObject(this.m_EditBaseDataObject, this.m_EditLayerID);
                    tempDialog3.SetEditMode(1);
                    tempDialog3.ShowDialog();
                }
            } else if (command.equals("???????????????")) {
                if (this.m_PolygonEditClass.getPointsCount() > 0) {
                    Common.ShowYesNoDialog(this.m_context, "???????????????????????????????", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon.7
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (paramString.equals("YES")) {
                                Toolbar_Polygon.this.StartEdit();
                            }
                        }
                    });
                }
            } else if (command.equals("???????????????")) {
                if (this.m_PolygonEditClass.getPointsCount() > 1) {
                    FinishEdit();
                } else {
                    Common.ShowDialog("??????????????????????????????2???.");
                }
            } else if (command.equals("????????????")) {
                PubVar._PubCommand.ShowToolbar("????????????????????????");
            } else if (command.equals("??????")) {
                if (Common.GetSelectObjectsCount(PubVar._Map, -1, 2, false) == 1) {
                    PubVar._PubCommand.ProcessCommand("????????????");
                    SetButtonSelectedStatus("??????", true);
                    return;
                }
                Common.ShowDialog("???????????????????????????????????????.");
            } else if (command.equals("??????")) {
                if (Common.GetSelectObjectsCount(PubVar._Map, -1, 2, false) == 1) {
                    PubVar._PubCommand.ProcessCommand("????????????");
                    SetButtonSelectedStatus("??????", true);
                    return;
                }
                Common.ShowDialog("???????????????????????????????????????.");
            } else if (command.equals("??????")) {
                if (Common.GetSelectObjectsCount(PubVar._Map, -1, 2, false) == 1) {
                    PubVar._PubCommand.ProcessCommand("????????????");
                    SetButtonSelectedStatus("??????", true);
                    return;
                }
                Common.ShowDialog("???????????????????????????????????????.");
            } else if (command.equals("??????")) {
                BasicValue tmpParam = new BasicValue();
                Polyline tmpSpiltPolyline = null;
                if (PubVar._PubCommand.GetPolygonEdit().getPointsCount() > 1) {
                    tmpSpiltPolyline = new Polyline();
                    tmpSpiltPolyline.SetAllCoordinateList(PubVar._PubCommand.GetPolygonEdit().GetAllCoordinates());
                }
                if (tmpSpiltPolyline == null || tmpSpiltPolyline.getCoordsCount() == 0) {
                    Common.ShowDialog("???????????????????????????.");
                    return;
                }
                List<HashMap<String, Object>> tmpPolyList = Common.GetSelectObjectsInLayer(PubVar._Map, this.m_EditLayerID, false, tmpParam);
                if (tmpPolyList.size() <= 0) {
                    Common.ShowDialog("??????????????????????????????????????????????????????.");
                    return;
                }
                boolean tmpBool = false;
                for (HashMap<String, Object> tmpHash : tmpPolyList) {
                    if (Boolean.parseBoolean(String.valueOf(tmpHash.get("Editable")))) {
                        Polygon tmpPoly7 = (Polygon) tmpHash.get("Geometry");
                        if (tmpPoly7 == null || !tmpPoly7.IsSimple()) {
                            Common.ShowDialog("??????????????????????????????Part??????.");
                            return;
                        }
                        List<Polygon> tmpResult = new ArrayList<>();
                        BasicValue tmpOutMsg01 = new BasicValue();
                        if (SplitPolygonByPolyline(tmpPoly7, tmpSpiltPolyline.GetAllCoordinateList(), tmpResult, tmpOutMsg01)) {
                            String tmpLayerID = String.valueOf(tmpHash.get("LayerID"));
                            DataSet pDataset = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmpHash.get("DataSource"))).GetDatasetByName(tmpLayerID);
                            if (pDataset != null && tmpResult.size() > 0) {
                                tmpBool = true;
                                HashMap<String, Object> tmpModifyHashMap = new HashMap<>();
                                tmpModifyHashMap.put("Type", "Split");
                                List<HashMap<String, Object>> tmpList01 = new ArrayList<>();
                                HashMap<String, Object> tmpHashMapOld = new HashMap<>();
                                tmpHashMapOld.put("SYS_ID", tmpPoly7.GetSYS_ID());
                                tmpHashMapOld.put("Coords", tmpPoly7.GetAllCoordinateList());
                                tmpList01.add(tmpHashMapOld);
                                tmpModifyHashMap.put("Old", tmpList01);
                                List tmpCoordList = new ArrayList();
                                tmpCoordList.addAll(tmpResult.get(0).GetAllCoordinateList());
                                tmpPoly7.ResetData();
                                tmpPoly7.SetAllCoordinateList(tmpCoordList);
                                byte[] tmpGeoBytes = Common.ConvertGeoToBytes(tmpPoly7);
                                if (tmpGeoBytes != null) {
                                    pDataset.UpdateGeometryData(tmpGeoBytes, tmpPoly7.GetSYS_ID());
                                }
                                pDataset.JustUpdateGeoMapIndex(tmpPoly7);
                                pDataset.SaveGeoIndexToDB(tmpPoly7);
                                List<HashMap<String, Object>> tmpList02 = new ArrayList<>();
                                HashMap<String, Object> tmpHashMapNew = new HashMap<>();
                                tmpHashMapNew.put("SYS_ID", tmpPoly7.GetSYS_ID());
                                tmpHashMapNew.put("Coords", tmpPoly7.GetAllCoordinateList());
                                tmpList02.add(tmpHashMapNew);
                                tmpModifyHashMap.put("Modify", tmpList02);
                                if (tmpResult.size() > 1) {
                                    List<String> tmpFieldValues = pDataset.getGeometryFieldValues(tmpPoly7.GetSYS_ID(), true);
                                    List<HashMap<String, Object>> tmpList03 = new ArrayList<>();
                                    int tmpJCount = tmpResult.size();
                                    for (int tmpJ = 1; tmpJ < tmpJCount; tmpJ++) {
                                        Polygon tmpPoly32 = tmpResult.get(tmpJ);
                                        DataSet.SaveNewPolygon(tmpPoly32, tmpFieldValues, pDataset, tmpLayerID);
                                        HashMap<String, Object> tmpHashMapNew2 = new HashMap<>();
                                        tmpHashMapNew2.put("SYS_ID", tmpPoly32.GetSYS_ID());
                                        tmpHashMapNew2.put("Coords", tmpPoly32.GetAllCoordinateList());
                                        tmpList03.add(tmpHashMapNew2);
                                    }
                                    tmpModifyHashMap.put("New", tmpList03);
                                }
                                pDataset.setEdited(true);
                                pDataset.RefreshTotalCount();
                                this.m_HistoryList.add(tmpModifyHashMap);
                            }
                        } else {
                            String tmpmsg01 = tmpOutMsg01.getString();
                            if (tmpmsg01.length() > 0) {
                                Common.ShowDialog("?????????:\r\n" + tmpmsg01);
                                return;
                            }
                        }
                    }
                }
                if (tmpBool) {
                    PubVar._PubCommand.ProcessCommand("??????_??????????????????");
                    PubVar._PubCommand.GetPolygonEdit().Clear();
                    PubVar._Map.Refresh();
                    Common.ShowToast("????????????.");
                }
            } else if (command.equals("??????")) {
                BasicValue tmpParam2 = new BasicValue();
                List<HashMap<String, Object>> tmpList = Common.GetSelectObjectsInLayer(PubVar._Map, this.m_EditLayerID, false, tmpParam2);
                if (tmpParam2.getInt() <= 0 || tmpList.size() <= 1) {
                    Common.ShowDialog("???????????????????????????.");
                    return;
                }
                boolean tmpBool2 = true;
                String tmpMsgString = "";
                Iterator<HashMap<String, Object>> tempiter02 = tmpList.iterator();
                while (true) {
                    if (!tempiter02.hasNext()) {
                        break;
                    }
                    HashMap<String, Object> tmpHash2 = tempiter02.next();
                    if (Boolean.parseBoolean(String.valueOf(tmpHash2.get("Editable"))) && !((Polygon) tmpHash2.get("Geometry")).IsSimple()) {
                        tmpMsgString = "???????????????????????????Part.\r\n?????????????????????????????????.";
                        tmpBool2 = false;
                        break;
                    }
                }
                if (!tmpBool2) {
                    Common.ShowDialog(tmpMsgString);
                    return;
                }
                PolygonSplitByPoly_Dialog tempDialog4 = new PolygonSplitByPoly_Dialog();
                tempDialog4.SetPolygons(tmpList);
                tempDialog4.SetMainPolyIndex(0);
                tempDialog4.SetCallback(this.pCallback);
                tempDialog4.ShowDialog();
            } else if (command.equals("??????")) {
                if (PubVar._PubCommand.GetPolygonEdit().getPointsCount() > 1) {
                    BasicValue tmpParam3 = new BasicValue();
                    final List<HashMap<String, Object>> tmpList2 = Common.GetSelectObjectsInLayer(PubVar._Map, this.m_EditLayerID, false, tmpParam3);
                    if (tmpParam3.getInt() <= 0 || tmpList2.size() <= 0) {
                        Common.ShowDialog("?????????????????????????????????????????????.");
                    } else {
                        Common.ShowYesNoDialog(this.m_context, "???????????????????????????????", ViewCompat.MEASURED_STATE_MASK, "??????", "?????????", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon.8
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String command2, Object pObject) {
                                boolean tmpSaveHole = false;
                                if (command2.equals("YES")) {
                                    tmpSaveHole = true;
                                }
                                Poly tmpSecondPoly = PolygonSplitByPoly_Dialog.ConvertPolygonToPolyObj(Toolbar_Polygon.ConvertPointsToPolygon(PubVar._PubCommand.GetPolygonEdit().GetAllCoordinates()));
                                boolean tmpNeedRefreshMap = false;
                                DataSet pDataset2 = null;
                                for (HashMap<String, Object> tmpHash3 : tmpList2) {
                                    try {
                                        Polygon tmpGeo = (Polygon) tmpHash3.get("Geometry");
                                        Poly tmpMainPoly = PolygonSplitByPoly_Dialog.ConvertPolygonToPolyObj(tmpGeo);
                                        Polygon tmpNewShape = PolygonSplitByPoly_Dialog.ConvertPolyObjToPolygon(PolygonSplitByPoly_Dialog.ConvertPolyObjToPolySimple(Clip.difference(tmpMainPoly, tmpSecondPoly)));
                                        tmpGeo.ResetData();
                                        tmpGeo.SetAllCoordinateList(tmpNewShape.GetAllCoordinateList());
                                        tmpGeo.SetPartIndex(tmpNewShape.GetPartIndex());
                                        tmpGeo.SetEdited(true);
                                        tmpNeedRefreshMap = true;
                                        if (pDataset2 == null) {
                                            DataSource tmpDataSource2 = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmpHash3.get("DataSource")));
                                            if (tmpDataSource2 != null) {
                                                pDataset2 = tmpDataSource2.GetDatasetByName(Toolbar_Polygon.this.m_EditLayerID);
                                            }
                                            pDataset2.JustUpdateGeoMapIndex(tmpGeo);
                                            pDataset2.SaveGeoIndexToDB(tmpGeo);
                                            pDataset2.setEdited(true);
                                            if (tmpSaveHole) {
                                                Poly tmpHolePoly = Clip.intersection(tmpMainPoly, tmpSecondPoly);
                                                if (!tmpHolePoly.isEmpty()) {
                                                    Polygon tmpNewShape2 = PolygonSplitByPoly_Dialog.ConvertPolyObjToPolygon(PolygonSplitByPoly_Dialog.ConvertPolyObjToPolySimple(tmpHolePoly));
                                                    if (tmpNewShape2.GetNumberOfPoints() > 2) {
                                                        DataSet.SaveNewPolygon(tmpNewShape2, pDataset2.getGeometryFieldValues(tmpGeo.GetSYS_ID(), true), pDataset2, Toolbar_Polygon.this.m_EditLayerID);
                                                    }
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                                if (tmpNeedRefreshMap) {
                                    PubVar._PubCommand.ProcessCommand("??????_??????????????????");
                                    PubVar._PubCommand.GetPolygonEdit().Clear();
                                    PubVar._Map.Refresh();
                                    Common.ShowToast("??????????????????.");
                                }
                            }
                        });
                    }
                } else {
                    Common.ShowDialog("????????????????????????????????????????????????.");
                }
            } else if (command.equals("??????")) {
                if (PubVar._PubCommand.GetPolygonEdit().getPointsCount() > 1) {
                    List<Coordinate> tmpCoords = PubVar._PubCommand.GetPolygonEdit().GetAllCoordinates();
                    BasicValue tmpParam4 = new BasicValue();
                    List<HashMap<String, Object>> tmpList3 = Common.GetSelectObjectsInLayer(PubVar._Map, this.m_EditLayerID, false, tmpParam4);
                    if (tmpParam4.getInt() <= 0 || tmpList3.size() <= 0) {
                        Common.ShowDialog("???????????????????????????.");
                        return;
                    }
                    boolean tmpBool3 = false;
                    DataSet pDataset2 = null;
                    for (HashMap<String, Object> tmpHash3 : tmpList3) {
                        Polygon tmpGeo = (Polygon) tmpHash3.get("Geometry");
                        if (pDataset2 == null && (tmpDataSource = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmpHash3.get("DataSource")))) != null) {
                            pDataset2 = tmpDataSource.GetDatasetByName(this.m_EditLayerID);
                        }
                        BasicValue outMsg = new BasicValue();
                        List<Polygon> tmpReshapes = new ArrayList<>();
                        if (ReShapeLines(tmpGeo, tmpCoords, tmpReshapes, outMsg)) {
                            if (tmpReshapes.size() == 1) {
                                HashMap<String, Object> tmpModifyHashMap2 = new HashMap<>();
                                tmpModifyHashMap2.put("Type", "Reshape");
                                List<HashMap<String, Object>> tmpListOld01 = new ArrayList<>();
                                List<HashMap<String, Object>> tmpListModify02 = new ArrayList<>();
                                List<HashMap<String, Object>> tmpListNew03 = new ArrayList<>();
                                List<HashMap<String, Object>> tmpListRemove04 = new ArrayList<>();
                                HashMap<String, Object> tmpHashMapOld2 = new HashMap<>();
                                tmpHashMapOld2.put("SYS_ID", tmpGeo.GetSYS_ID());
                                tmpHashMapOld2.put("Coords", tmpGeo.GetAllCoordinateList());
                                tmpListOld01.add(tmpHashMapOld2);
                                tmpGeo.ResetData();
                                tmpGeo.SetAllCoordinateList(tmpReshapes.get(0).GetAllCoordinateList());
                                tmpGeo.SetEdited(true);
                                HashMap<String, Object> tmpHashMapModify = new HashMap<>();
                                tmpHashMapModify.put("SYS_ID", tmpGeo.GetSYS_ID());
                                tmpHashMapModify.put("Coords", tmpGeo.GetAllCoordinateList());
                                tmpListModify02.add(tmpHashMapModify);
                                tmpModifyHashMap2.put("Old", tmpListOld01);
                                tmpModifyHashMap2.put("Modify", tmpListModify02);
                                tmpModifyHashMap2.put("New", tmpListNew03);
                                tmpModifyHashMap2.put("Remove", tmpListRemove04);
                                this.m_HistoryList.add(tmpModifyHashMap2);
                                pDataset2.JustUpdateGeoMapIndex(tmpGeo);
                                pDataset2.SaveGeoIndexToDB(tmpGeo);
                                pDataset2.setEdited(true);
                                tmpBool3 = true;
                            } else if (tmpReshapes.size() == 2) {
                                PolygonReshape_Dialog tmpDialog2 = new PolygonReshape_Dialog();
                                tmpDialog2.SetDataSet(pDataset2);
                                tmpDialog2.SetMainPolygon(tmpGeo);
                                tmpDialog2.SetPolygons(tmpReshapes);
                                tmpDialog2.SetCallback(this.pCallback);
                                tmpDialog2.ShowDialog();
                            }
                        }
                    }
                    if (tmpBool3) {
                        PubVar._PubCommand.ProcessCommand("??????_??????????????????");
                        PubVar._PubCommand.GetPolygonEdit().Clear();
                        PubVar._Map.Refresh();
                        Common.ShowToast("????????????.");
                        return;
                    }
                    return;
                }
                Common.ShowDialog("????????????????????????????????????????????????.");
            } else if (command.equals("??????")) {
                BasicValue tmpParam5 = new BasicValue();
                List<HashMap<String, Object>> tmpList4 = Common.GetSelectObjectsInLayer(PubVar._Map, this.m_EditLayerID, false, tmpParam5);
                if (tmpParam5.getInt() <= 0 || tmpList4.size() <= 1) {
                    Common.ShowDialog("???????????????????????????????????????.");
                    return;
                }
                PolygonUnion_Dialog tempDialog5 = new PolygonUnion_Dialog();
                tempDialog5.SetPolygons(tmpList4);
                tempDialog5.SetCallback(this.pCallback);
                tempDialog5.ShowDialog();
            } else if (command.equals("??????")) {
                if (PubVar._PubCommand.GetPolygonEdit().getPointsCount() > 1) {
                    List<Coordinate> tmpCoords2 = PubVar._PubCommand.GetPolygonEdit().GetAllCoordinates();
                    BasicValue tmpParam6 = new BasicValue();
                    List<HashMap<String, Object>> tmpList5 = PubVar._Map.GetSelectObjects(-1, 2, true, tmpParam6);
                    if (tmpParam6.getInt() <= 0 || tmpList5.size() <= 0) {
                        Common.ShowDialog("???????????????????????????.");
                        return;
                    }
                    boolean tmpBool4 = false;
                    Iterator<HashMap<String, Object>> tmpIter = tmpList5.iterator();
                    if (tmpIter.hasNext()) {
                        BasicValue outMsg2 = new BasicValue();
                        List<Polygon> tmpReshapes2 = new ArrayList<>();
                        if (ReShapeLines3((Polygon) tmpIter.next().get("Geometry"), tmpCoords2, tmpReshapes2, outMsg2) && tmpReshapes2.size() == 1) {
                            Polygon tmpNewShape = tmpReshapes2.get(0);
                            try {
                                if (this.m_EditBaseDataObject != null) {
                                    this.m_EditBaseDataObject.SetBaseObjectRelateLayerID(this.m_EditLayerID);
                                    Polygon tempGeo = new Polygon();
                                    List<Coordinate> tempCoords = new ArrayList<>();
                                    tempCoords.addAll(tmpNewShape.GetAllCoordinateList());
                                    tempGeo.SetAllCoordinateList(tempCoords);
                                    tempGeo.getBorderLine();
                                    int tempSYS_ID = this.m_EditBaseDataObject.SaveNewGeoToDb(tempGeo);
                                    if (tempSYS_ID >= 0) {
                                        this.m_EditBaseDataObject.SetSYS_ID(tempSYS_ID);
                                        if (PubVar.Edited_ShowAttribute_Bool) {
                                            if (this.m_EditBaseDataObject.hasFieldsValue()) {
                                                FeatureAttribute_Dialog tempDialog6 = new FeatureAttribute_Dialog();
                                                tempDialog6.SetBaseDataObject(this.m_EditBaseDataObject, this.m_EditLayerID);
                                                tempDialog6.SetEditMode(2);
                                                tempDialog6.ShowDialog();
                                            } else {
                                                ShowEditProp(this.m_EditLayerID, tempSYS_ID);
                                            }
                                        }
                                    }
                                    if (this.m_EditBaseDataObject.GetSYS_ID() >= 0 && (tempGeoLayer = PubVar._Map.GetGeoLayerByName(this.m_EditLayerID)) != null) {
                                        Selection tmpSel = PubVar._Map.getFeatureSelectionByLayerID(tempGeoLayer.getLayerID(), false);
                                        if (tmpSel != null) {
                                            tmpSel.RemoveBySysID(this.m_EditBaseDataObject.GetSYS_ID());
                                        }
                                        PubVar._Map.RefreshFast();
                                    }
                                }
                                this.m_PolygonEditClass.Clear();
                                this.m_EditBaseDataObject = new BaseDataObject();
                                this.m_EditBaseDataObject.SetBaseObjectRelateLayerID(this.m_EditLayerID);
                                this.m_EditBaseDataObject.SetSYS_TYPE("?????????");
                                PubVar._PubCommand.ProcessCommand("????????????");
                                PubVar._PubCommand.getMapView()._CurrentEditPaint = null;
                            } catch (Exception e) {
                            }
                            tmpBool4 = true;
                        }
                    }
                    if (tmpBool4) {
                        PubVar._PubCommand.ProcessCommand("??????_??????????????????");
                        PubVar._PubCommand.GetPolygonEdit().Clear();
                        PubVar._Map.Refresh();
                        Common.ShowToast("?????????????????????.");
                        return;
                    }
                    return;
                }
                Common.ShowDialog("????????????????????????????????????????????????.");
            } else if (command.equals("???????????????????????????")) {
                Coordinate tmpCoord = PubVar._MapView.getMap().getCenter().Clone();
                Coordinate localCoordinate2 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tmpCoord, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                tmpCoord.setGeoX(localCoordinate2.getGeoX());
                tmpCoord.setGeoY(localCoordinate2.getGeoY());
                this.m_PolygonEditClass.AddPoint(tmpCoord);
                Common.ShowToast("??????????????????");
            } else if (command.equals("?????????")) {
                String[] tempOutMsg = new String[1];
                if (!this.m_PolygonEditClass.UndoLastPoint(tempOutMsg)) {
                    Common.ShowToast(tempOutMsg[0]);
                }
            } else if (command.equals("?????????")) {
                String[] tempOutMsg2 = new String[1];
                if (!this.m_PolygonEditClass.RedoLastPoint(tempOutMsg2)) {
                    Common.ShowToast(tempOutMsg2[0]);
                }
            } else if (command.equals("??????????????????")) {
                LoadCoordsFile_Dialog tmpDialog3 = new LoadCoordsFile_Dialog();
                tmpDialog3.SetCallback(this.pCallback);
                tmpDialog3.ShowDialog();
            } else if (command.equals("????????????")) {
                if (this.m_PolygonEditClass.getPointsCount() > 0) {
                    Input2_Dialog tmpDilalog = new Input2_Dialog();
                    tmpDilalog.setValues("?????????????????????", "??????(m):", "0", "??????(m):", "0");
                    tmpDilalog.setInputType(8192);
                    tmpDilalog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon.9
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command2, Object pObject) {
                            Object[] tmpObjs;
                            Object[] tmpObjs2;
                            if (command2.equals("????????????") && (tmpObjs = (Object[]) pObject) != null && tmpObjs.length > 2) {
                                Object tmpObj2 = tmpObjs[1];
                                if ((tmpObj2 instanceof Object[]) && (tmpObjs2 = (Object[]) tmpObj2) != null && tmpObjs2.length > 1) {
                                    double tmpW = 0.0d;
                                    double tmpH = 0.0d;
                                    try {
                                        tmpW = Double.parseDouble(String.valueOf(tmpObjs2[0]));
                                        tmpH = Double.parseDouble(String.valueOf(tmpObjs2[1]));
                                    } catch (Exception e2) {
                                    }
                                    if (tmpW <= 0.0d || tmpH <= 0.0d) {
                                        Common.ShowDialog("???????????????????????????0?????????0!");
                                        return;
                                    }
                                    Coordinate tmpCoordinate = Toolbar_Polygon.this.m_PolygonEditClass.GetLastCoordinate().Clone();
                                    Toolbar_Polygon.this.m_PolygonEditClass.Clear();
                                    Toolbar_Polygon.this.m_PolygonEditClass.AddPoint(tmpCoordinate);
                                    Coordinate tmpCoordinate2 = tmpCoordinate.Clone();
                                    tmpCoordinate2.SetOffset(tmpW, 0.0d);
                                    Toolbar_Polygon.this.m_PolygonEditClass.AddPoint(tmpCoordinate2);
                                    Coordinate tmpCoordinate3 = tmpCoordinate2.Clone();
                                    tmpCoordinate3.SetOffset(0.0d, tmpH);
                                    Toolbar_Polygon.this.m_PolygonEditClass.AddPoint(tmpCoordinate3);
                                    Coordinate tmpCoordinate4 = tmpCoordinate3.Clone();
                                    tmpCoordinate4.SetOffset(-tmpW, 0.0d);
                                    Toolbar_Polygon.this.m_PolygonEditClass.AddPoint(tmpCoordinate4);
                                    Toolbar_Polygon.this.m_PolygonEditClass.AddPoint(tmpCoordinate.Clone());
                                    Toolbar_Polygon.this.FinishEdit();
                                }
                            }
                        }
                    });
                    tmpDilalog.ShowDialog();
                    return;
                }
                Common.ShowDialog("??????????????????????????????????????????????????????????????????!");
            } else if (command.equals("????????????")) {
                try {
                    Toolbar_Select.PasteGeometrys(this.m_context, this.m_EditGeoLayer, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon.10
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command2, Object pObject) {
                            Object[] tmpObjs;
                            List<Integer> tmpPasteSysIDList;
                            if (command2.equals("??????????????????") && pObject != null && (tmpObjs = (Object[]) pObject) != null && tmpObjs.length > 1 && Integer.parseInt(String.valueOf(tmpObjs[0])) == 1 && (tmpPasteSysIDList = (List) tmpObjs[1]) != null && tmpPasteSysIDList.size() > 0) {
                                Toolbar_Polygon.this.ShowEditProp(Toolbar_Polygon.this.m_EditGeoLayer.getLayerID(), tmpPasteSysIDList.get(0).intValue());
                            }
                        }
                    });
                } catch (Exception e2) {
                    Common.ShowToast(e2.getLocalizedMessage());
                }
            }
        } catch (Exception ex) {
            Common.Log("ERROR", ex.getMessage());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void StartEdit() {
        this.m_PolygonEditClass.Clear();
        this.m_EditBaseDataObject = new BaseDataObject();
        this.m_EditBaseDataObject.SetBaseObjectRelateLayerID(this.m_EditLayerID);
        this.m_EditBaseDataObject.SetSYS_TYPE("?????????");
        if (getButtonIsSelected("????????????")) {
            DoCommand("???????????????");
        } else {
            DoCommand("?????????");
        }
    }

    private List<Coordinate> convertDrawPolygon(List<Coordinate> origCoords) {
        List<Coordinate> result = new ArrayList<>();
        try {
            int tmpCount2 = origCoords.size() - 1;
            int tmpStartI = -1;
            int tmpEndI = -1;
            Coordinate tmpInterSectCoordinate = null;
            for (int i = 0; i < tmpCount2; i++) {
                Coordinate tmpP1 = origCoords.get(i);
                Coordinate tmpQ1 = origCoords.get(i + 1);
                int j = i + 2;
                while (true) {
                    if (j >= tmpCount2) {
                        break;
                    }
                    Coordinate tmpP2 = origCoords.get(j);
                    Coordinate tmpQ2 = origCoords.get(j + 1);
                    if (CommonMath.JudgeLineSegIntersect(tmpP1, tmpQ1, tmpP2, tmpQ2) && (tmpInterSectCoordinate = CommonMath.CalLineSegIntersection(tmpP1, tmpQ1, tmpP2, tmpQ2)) != null) {
                        tmpStartI = i + 1;
                        tmpEndI = j + 1;
                        break;
                    }
                    j++;
                }
                if (tmpStartI > -1 && tmpEndI > -1) {
                    break;
                }
            }
            if (tmpStartI <= -1 || tmpEndI <= -1) {
                result.addAll(origCoords);
                return result;
            }
            result.add(tmpInterSectCoordinate);
            for (int i2 = tmpStartI; i2 < tmpEndI; i2++) {
                result.add(origCoords.get(i2).Clone());
            }
            return result;
        } catch (Exception e) {
            result.addAll(origCoords);
        }
        return result;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void FinishEdit() {
        GeoLayer tempGeoLayer;
        try {
            if (this.m_EditBaseDataObject != null) {
                this.m_EditBaseDataObject.SetBaseObjectRelateLayerID(this.m_EditLayerID);
                Polygon tempGeo = new Polygon();
                List<Coordinate> tempCoords = new ArrayList<>();
                tempCoords.addAll(this.m_PolygonEditClass.GetAllCoordinates());
                tempGeo.SetAllCoordinateList(tempCoords);
                tempGeo.getBorderLine();
                int tempSYS_ID = this.m_EditBaseDataObject.SaveNewGeoToDb(tempGeo);
                if (tempSYS_ID >= 0) {
                    this.m_EditBaseDataObject.SetSYS_ID(tempSYS_ID);
                    if (PubVar.Edited_ShowAttribute_Bool) {
                        if (this.m_EditBaseDataObject.hasFieldsValue()) {
                            FeatureAttribute_Dialog tempDialog = new FeatureAttribute_Dialog();
                            tempDialog.SetBaseDataObject(this.m_EditBaseDataObject, this.m_EditLayerID);
                            tempDialog.SetEditMode(2);
                            tempDialog.ShowDialog();
                        } else {
                            ShowEditProp(this.m_EditLayerID, tempSYS_ID);
                        }
                    }
                }
                if (this.m_EditBaseDataObject.GetSYS_ID() >= 0 && (tempGeoLayer = PubVar._Map.GetGeoLayerByName(this.m_EditLayerID)) != null) {
                    tempGeoLayer.getDataset().RefreshTotalCount();
                    Selection tmpSel = PubVar._Map.getFeatureSelectionByLayerID(tempGeoLayer.getLayerID(), false);
                    if (tmpSel != null) {
                        tmpSel.RemoveBySysID(this.m_EditBaseDataObject.GetSYS_ID());
                    }
                    PubVar._Map.RefreshFast();
                }
            }
            this.m_PolygonEditClass.Clear();
            this.m_EditBaseDataObject = new BaseDataObject();
            this.m_EditBaseDataObject.SetBaseObjectRelateLayerID(this.m_EditLayerID);
            this.m_EditBaseDataObject.SetSYS_TYPE("?????????");
        } catch (Exception e) {
        }
        PubVar._PubCommand.ProcessCommand("????????????");
    }

    public static boolean SplitPolygonByPolyline(Polygon mainPolygon, List<Coordinate> newBoundaryCoords, List<Polygon> splitPolygonsList, BasicValue outMsg) {
        try {
            List<Coordinate> tmpPoltCoords1 = mainPolygon.getBorderLine().GetAllCoordinateList();
            List<Coordinate> tmpPolyCoords = new ArrayList<>();
            for (Coordinate coordinate : tmpPoltCoords1) {
                tmpPolyCoords.add(coordinate.Clone());
            }
            tmpPolyCoords.add(tmpPoltCoords1.get(0).Clone());
            List<Coordinate> tmpSplitCoords = new ArrayList<>();
            List<Integer> tmpSplitIndex = new ArrayList<>();
            Coordinate tmpFirstInterCoord = null;
            Coordinate tmpLastInterCoord = null;
            Coordinate tmpCoord01 = newBoundaryCoords.iterator().next();
            double tmpX1 = tmpCoord01.getX();
            double tmpY1 = tmpCoord01.getY();
            int tmpK = -1;
            int tmpM = -1;
            int tmpCount02 = newBoundaryCoords.size();
            int tmpJ = 1;
            while (true) {
                if (tmpJ >= tmpCount02) {
                    break;
                }
                Coordinate tmpCoord012 = newBoundaryCoords.get(tmpJ);
                double tmpX2 = tmpCoord012.getX();
                double tmpY2 = tmpCoord012.getY();
                List<double[]> tmpCoordsList = new ArrayList<>();
                List<Integer> tmpIntersectIntList = new ArrayList<>();
                if (SpatialRelation.CalLineSegIntersectPoints(tmpX1, tmpY1, tmpX2, tmpY2, tmpPolyCoords, tmpCoordsList, tmpIntersectIntList)) {
                    if (tmpFirstInterCoord != null) {
                        double[] tmpD01 = tmpCoordsList.get(0);
                        tmpLastInterCoord = new Coordinate(tmpD01[0], tmpD01[1]);
                        tmpSplitIndex.add(tmpIntersectIntList.get(0));
                        tmpM = tmpJ;
                        break;
                    }
                    double[] tmpD012 = tmpCoordsList.get(0);
                    tmpFirstInterCoord = new Coordinate(tmpD012[0], tmpD012[1]);
                    tmpSplitIndex.add(tmpIntersectIntList.get(0));
                    tmpK = tmpJ;
                    if (tmpCoordsList.size() > 1) {
                        double[] tmpD013 = tmpCoordsList.get(1);
                        tmpLastInterCoord = new Coordinate(tmpD013[0], tmpD013[1]);
                        tmpSplitIndex.add(tmpIntersectIntList.get(1));
                        tmpM = tmpJ;
                        break;
                    }
                }
                tmpX1 = tmpX2;
                tmpY1 = tmpY2;
                tmpJ++;
            }
            if (tmpSplitIndex.size() == 2) {
                if (tmpM >= tmpK) {
                    tmpSplitCoords.add(tmpFirstInterCoord);
                    for (int tmpJ2 = tmpK; tmpJ2 < tmpM; tmpJ2++) {
                        tmpSplitCoords.add(newBoundaryCoords.get(tmpJ2).Clone());
                    }
                    tmpSplitCoords.add(tmpLastInterCoord);
                }
                int tmpStart = tmpSplitIndex.get(0).intValue();
                int tmpEnd = tmpSplitIndex.get(1).intValue();
                List<Coordinate> tmpPoly01 = new ArrayList<>();
                List<Coordinate> tmpPoly02 = new ArrayList<>();
                int tmpStart2 = tmpStart + 1;
                int tmpEnd2 = tmpEnd + 1;
                if (tmpEnd2 > tmpStart2) {
                    tmpPoly01.add(tmpFirstInterCoord.Clone());
                    for (int i = tmpStart2; i < tmpEnd2; i++) {
                        tmpPoly01.add(tmpPolyCoords.get(i).Clone());
                    }
                    tmpPoly01.add(tmpLastInterCoord.Clone());
                    for (int i2 = tmpSplitCoords.size() - 2; i2 > 0; i2--) {
                        tmpPoly01.add(tmpSplitCoords.get(i2).Clone());
                    }
                    tmpPoly01.add(tmpFirstInterCoord.Clone());
                    tmpPoly02.add(tmpLastInterCoord.Clone());
                    int tmpCount = tmpPolyCoords.size() - 1;
                    for (int i3 = tmpEnd2; i3 < tmpCount; i3++) {
                        tmpPoly02.add(tmpPolyCoords.get(i3).Clone());
                    }
                    for (int i4 = 0; i4 < tmpStart2; i4++) {
                        tmpPoly02.add(tmpPolyCoords.get(i4).Clone());
                    }
                    int tmpCount2 = tmpSplitCoords.size();
                    for (int i5 = 0; i5 < tmpCount2; i5++) {
                        tmpPoly02.add(tmpSplitCoords.get(i5).Clone());
                    }
                } else if (tmpEnd2 == tmpStart2) {
                    Coordinate tmpCoord003 = tmpPolyCoords.get(tmpEnd2 - 1);
                    if (Common.GetTwoPointDistance(tmpLastInterCoord, tmpCoord003) > Common.GetTwoPointDistance(tmpFirstInterCoord, tmpCoord003)) {
                        for (int i6 = 0; i6 < tmpStart2; i6++) {
                            tmpPoly01.add(tmpPolyCoords.get(i6).Clone());
                        }
                        for (Coordinate tmpCoord004 : tmpSplitCoords) {
                            tmpPoly01.add(tmpCoord004.Clone());
                        }
                        int tmpCount003 = tmpPolyCoords.size();
                        for (int i7 = tmpStart2; i7 < tmpCount003; i7++) {
                            tmpPoly01.add(tmpPolyCoords.get(i7).Clone());
                        }
                        for (Coordinate tmpCoord0042 : tmpSplitCoords) {
                            tmpPoly02.add(tmpCoord0042.Clone());
                        }
                        tmpPoly02.add(tmpFirstInterCoord.Clone());
                    } else {
                        for (int i8 = 0; i8 < tmpStart2; i8++) {
                            tmpPoly01.add(tmpPolyCoords.get(i8).Clone());
                        }
                        for (int i9 = tmpSplitCoords.size() - 1; i9 > -1; i9--) {
                            tmpPoly01.add(tmpSplitCoords.get(i9).Clone());
                        }
                        int tmpCount0032 = tmpPolyCoords.size();
                        for (int i10 = tmpStart2; i10 < tmpCount0032; i10++) {
                            tmpPoly01.add(tmpPolyCoords.get(i10).Clone());
                        }
                        for (Coordinate tmpCoord0043 : tmpSplitCoords) {
                            tmpPoly02.add(tmpCoord0043.Clone());
                        }
                        tmpPoly02.add(tmpFirstInterCoord.Clone());
                    }
                } else {
                    tmpPoly01.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                    for (int i11 = tmpEnd2; i11 < tmpStart2; i11++) {
                        tmpPoly01.add(tmpPolyCoords.get(i11).Clone());
                    }
                    tmpPoly01.add(tmpSplitCoords.get(0).Clone());
                    int tmpCount3 = tmpSplitCoords.size() - 1;
                    for (int i12 = 1; i12 < tmpCount3; i12++) {
                        tmpPoly01.add(tmpSplitCoords.get(i12).Clone());
                    }
                    tmpPoly02.add(tmpSplitCoords.get(0).Clone());
                    int tmpCount4 = tmpPolyCoords.size();
                    for (int i13 = tmpStart2; i13 < tmpCount4; i13++) {
                        tmpPoly02.add(tmpPolyCoords.get(i13).Clone());
                    }
                    for (int i14 = 0; i14 < tmpEnd2; i14++) {
                        tmpPoly02.add(tmpPolyCoords.get(i14).Clone());
                    }
                    tmpPoly02.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                    for (int i15 = tmpSplitCoords.size() - 2; i15 > 0; i15--) {
                        tmpPoly02.add(tmpSplitCoords.get(i15).Clone());
                    }
                }
                if (tmpPoly01.size() > 2) {
                    Polygon tmpPoly001 = new Polygon();
                    tmpPoly001.SetAllCoordinateList(tmpPoly01);
                    splitPolygonsList.add(tmpPoly001);
                }
                if (tmpPoly02.size() > 2) {
                    Polygon tmpPoly0012 = new Polygon();
                    tmpPoly0012.SetAllCoordinateList(tmpPoly02);
                    splitPolygonsList.add(tmpPoly0012);
                }
                if (splitPolygonsList.size() > 0) {
                    return true;
                }
            } else {
                outMsg.setValue("??????????????????????????????????????????????????????.");
            }
        } catch (Exception e) {
            outMsg.setValue(e.getLocalizedMessage());
        }
        return false;
    }

    private boolean SplitPolygonByLines(List<HashMap<String, Object>> polyHashList, List<HashMap<String, Object>> lineList) {
        boolean result = false;
        try {
            Poly tmpMainLineObj = new PolyDefault();
            Clip.GPC_EPSILON = 2.220446049250313E-16d;
            double[] tmpSplitStartPtn = null;
            double[] tmpSplitEndPtn = null;
            int tmpSplitLinesTid = 0;
            int tmpSplitLinesCount = lineList.size();
            for (HashMap<String, Object> tmpHash : lineList) {
                tmpSplitLinesTid++;
                Polyline tmpLine = (Polyline) tmpHash.get("Geometry");
                tmpMainLineObj = Clip.union(tmpMainLineObj, ConvertPolylineToPolyObj(tmpLine));
                if (tmpSplitStartPtn == null) {
                    Coordinate tmpCoord01 = tmpLine.GetAllCoordinateList().get(0);
                    tmpSplitStartPtn = new double[]{tmpCoord01.getX(), tmpCoord01.getY()};
                }
                if (tmpSplitLinesTid == tmpSplitLinesCount) {
                    Coordinate tmpCoord012 = tmpLine.GetAllCoordinateList().get(tmpLine.GetAllCoordinateList().size() - 1);
                    tmpSplitEndPtn = new double[]{tmpCoord012.getX(), tmpCoord012.getY()};
                }
            }
            StringBuilder tmpMsgSB = new StringBuilder();
            for (HashMap<String, Object> tmpHash2 : polyHashList) {
                if (Boolean.parseBoolean(String.valueOf(tmpHash2.get("Editable")))) {
                    Polygon tmpPoly = (Polygon) tmpHash2.get("Geometry");
                    if (tmpPoly == null || !tmpPoly.IsSimple()) {
                        tmpMsgSB.append("??????????????????????????????Part??????.");
                    } else {
                        if (!(tmpSplitStartPtn == null || tmpSplitEndPtn == null)) {
                            if (SpatialRelation.ContainPoint(tmpSplitStartPtn[0], tmpSplitStartPtn[1], tmpPoly.GetAllCoordinateList())) {
                                tmpMsgSB.append("??????????????????????????????????????????.");
                            } else if (SpatialRelation.ContainPoint(tmpSplitEndPtn[0], tmpSplitEndPtn[1], tmpPoly.GetAllCoordinateList())) {
                                tmpMsgSB.append("??????????????????????????????????????????.");
                            }
                        }
                        Poly tmpPolyObj = PolygonSplitByPoly_Dialog.ConvertPolygonToPolyObj(tmpPoly);
                        Clip.GPC_EPSILON = 2.220446049250313E-16d;
                        Poly tmpSplitResult = Clip.difference(tmpPolyObj, tmpMainLineObj);
                        String tmpLayerID = String.valueOf(tmpHash2.get("LayerID"));
                        DataSet pDataset = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmpHash2.get("DataSource"))).GetDatasetByName(tmpLayerID);
                        if (pDataset != null) {
                            List<Polygon> tmpResultList = PolygonSplitByPoly_Dialog.ConvertPolyObjToPolygons(tmpSplitResult);
                            if (tmpResultList.size() > 0) {
                                HashMap<String, Object> tmpModifyHashMap = new HashMap<>();
                                tmpModifyHashMap.put("Type", "Split");
                                List<HashMap<String, Object>> tmpList01 = new ArrayList<>();
                                HashMap<String, Object> tmpHashMapOld = new HashMap<>();
                                tmpHashMapOld.put("SYS_ID", tmpPoly.GetSYS_ID());
                                tmpHashMapOld.put("Coords", tmpPoly.GetAllCoordinateList());
                                tmpList01.add(tmpHashMapOld);
                                tmpModifyHashMap.put("Old", tmpList01);
                                List tmpCoordList = new ArrayList();
                                tmpCoordList.addAll(tmpResultList.get(0).GetAllCoordinateList());
                                tmpPoly.ResetData();
                                tmpPoly.SetAllCoordinateList(tmpCoordList);
                                byte[] tmpGeoBytes = Common.ConvertGeoToBytes(tmpPoly);
                                if (tmpGeoBytes != null) {
                                    pDataset.UpdateGeometryData(tmpGeoBytes, tmpPoly.GetSYS_ID());
                                }
                                pDataset.JustUpdateGeoMapIndex(tmpPoly);
                                pDataset.SaveGeoIndexToDB(tmpPoly);
                                List<HashMap<String, Object>> tmpList02 = new ArrayList<>();
                                HashMap<String, Object> tmpHashMapNew = new HashMap<>();
                                tmpHashMapNew.put("SYS_ID", tmpPoly.GetSYS_ID());
                                tmpHashMapNew.put("Coords", tmpPoly.GetAllCoordinateList());
                                tmpList02.add(tmpHashMapNew);
                                tmpModifyHashMap.put("Modify", tmpList02);
                                if (tmpResultList.size() > 1) {
                                    List<String> tmpFieldValues = pDataset.getGeometryFieldValues(tmpPoly.GetSYS_ID(), true);
                                    List<HashMap<String, Object>> tmpList03 = new ArrayList<>();
                                    int tmpJCount = tmpResultList.size();
                                    for (int tmpJ = 1; tmpJ < tmpJCount; tmpJ++) {
                                        Polygon tmpPoly3 = tmpResultList.get(tmpJ);
                                        DataSet.SaveNewPolygon(tmpPoly3, tmpFieldValues, pDataset, tmpLayerID);
                                        HashMap<String, Object> tmpHashMapNew2 = new HashMap<>();
                                        tmpHashMapNew2.put("SYS_ID", tmpPoly3.GetSYS_ID());
                                        tmpHashMapNew2.put("Coords", tmpPoly3.GetAllCoordinateList());
                                        tmpList03.add(tmpHashMapNew2);
                                    }
                                    tmpModifyHashMap.put("New", tmpList03);
                                }
                                pDataset.setEdited(true);
                                pDataset.RefreshTotalCount();
                                this.m_HistoryList.add(tmpModifyHashMap);
                                result = true;
                            }
                        }
                    }
                }
            }
            if (tmpMsgSB.length() > 0) {
                Common.ShowDialog(tmpMsgSB.toString());
            }
        } catch (Exception e) {
        }
        return result;
    }

    private Poly ConvertPolylineToPolyObj2(Polyline polyline) {
        int tempCount02;
        Poly result = new PolyDefault();
        List<Coordinate> tempCoords00 = polyline.GetAllCoordinateList();
        int tempPtnTID = 0;
        int tempCount00 = polyline.GetNumberOfParts();
        for (int i0 = 0; i0 < tempCount00; i0++) {
            new ArrayList();
            if (i0 != tempCount00 - 1) {
                tempCount02 = polyline.GetPartIndex().get(i0 + 1).intValue();
            } else {
                tempCount02 = tempCoords00.size();
            }
            if (tempCount02 - tempPtnTID > 1) {
                int tmpCount3 = tempCount02 - 1;
                for (int i = tempPtnTID; i < tmpCount3; i++) {
                    Coordinate tempStartCoord = tempCoords00.get(i);
                    Coordinate tempEndCoord = tempCoords00.get(i + 1);
                    PolySimple tmpPoly2 = new PolySimple();
                    tmpPoly2.add(tempStartCoord.getX(), tempStartCoord.getY());
                    tmpPoly2.add(tempEndCoord.getX(), tempEndCoord.getY());
                    tmpPoly2.add(tempEndCoord.getX(), tempEndCoord.getY() + SPLIT_BIAS);
                    tmpPoly2.add(tempStartCoord.getX(), tempStartCoord.getY() + SPLIT_BIAS);
                    tmpPoly2.add(tempStartCoord.getX(), tempStartCoord.getY());
                    result = (PolyDefault) Clip.union(result, tmpPoly2);
                }
            }
            tempPtnTID += tempCount02;
        }
        return result;
    }

    private Poly ConvertPolylineToPolyObj(Polyline polyline) {
        int tempCount02;
        Poly result = new PolyDefault();
        double tmpDis = SPLIT_BIAS;
        List<Coordinate> tempCoords00 = polyline.GetAllCoordinateList();
        int tempPtnTID = 0;
        int tempCount00 = polyline.GetNumberOfParts();
        for (int i0 = 0; i0 < tempCount00; i0++) {
            new ArrayList();
            if (i0 != tempCount00 - 1) {
                tempCount02 = polyline.GetPartIndex().get(i0 + 1).intValue();
            } else {
                tempCount02 = tempCoords00.size();
            }
            if (tempCount02 - tempPtnTID > 1) {
                if (tempCount02 - tempPtnTID == 2) {
                    Coordinate tempStartCoord = tempCoords00.get(tempPtnTID);
                    Coordinate tempEndCoord = tempCoords00.get(tempPtnTID + 1);
                    PolySimple tmpPoly2 = new PolySimple();
                    Coordinate[] tmpCoord04 = calVerticalCoords(tempStartCoord, tempEndCoord, tmpDis);
                    tmpPoly2.add(tempStartCoord.getX(), tempStartCoord.getY());
                    tmpPoly2.add(tempEndCoord.getX(), tempEndCoord.getY());
                    tmpPoly2.add(tmpCoord04[1].getX(), tmpCoord04[1].getY());
                    tmpPoly2.add(tmpCoord04[0].getX(), tmpCoord04[0].getY());
                    tmpPoly2.add(tempStartCoord.getX(), tempStartCoord.getY());
                    result = (PolyDefault) Clip.union(result, tmpPoly2);
                } else {
                    List<Coordinate> tmpArray01 = new ArrayList<>();
                    Coordinate tmpCoord01 = tempCoords00.get(tempPtnTID);
                    Coordinate tmpCoord02 = tempCoords00.get(tempPtnTID + 1);
                    Coordinate tmpCoord03 = tempCoords00.get(tempPtnTID + 2);
                    if (calDirection(tmpCoord01, tmpCoord02, tmpCoord03)) {
                        Coordinate[] tmpCoord042 = calVerticalCoords(tmpCoord01, tmpCoord02, tmpDis);
                        if (tmpCoord042 != null) {
                            tmpArray01.add(tmpCoord042[0]);
                            tmpArray01.add(tmpCoord042[1]);
                        }
                    } else {
                        tmpDis = -tmpDis;
                        Coordinate[] tmpCoord043 = calVerticalCoords(tmpCoord01, tmpCoord02, tmpDis);
                        if (tmpCoord043 != null) {
                            tmpArray01.add(tmpCoord043[0]);
                            tmpArray01.add(tmpCoord043[1]);
                        }
                    }
                    int tmpCount3 = tempCount02 - 2;
                    for (int i = tempPtnTID + 1; i < tmpCount3; i++) {
                        tmpCoord02 = tempCoords00.get(i + 1);
                        tmpCoord03 = tempCoords00.get(i + 2);
                        Coordinate[] tmpCoord044 = calVerticalCoords(tempCoords00.get(i), tmpCoord02, tmpDis);
                        Coordinate[] tmpCoord05 = calVerticalCoords(tmpCoord02, tmpCoord03, tmpDis);
                        if (!(tmpCoord044 == null || tmpCoord05 == null)) {
                            double[] tmpInterCoord = new double[2];
                            if (SpatialRelation.Cal2LineSegIntersectPoint(tmpCoord044[0].getX(), tmpCoord044[0].getY(), tmpCoord044[1].getX(), tmpCoord044[1].getY(), tmpCoord05[0].getX(), tmpCoord05[0].getY(), tmpCoord05[1].getX(), tmpCoord05[1].getY(), tmpInterCoord)) {
                                tmpArray01.add(new Coordinate(tmpInterCoord[0], tmpInterCoord[1]));
                            } else {
                                tmpArray01.add(tmpCoord044[1]);
                                tmpArray01.add(tmpCoord05[0]);
                            }
                        }
                    }
                    Coordinate[] tmpCoord045 = calVerticalCoords(tmpCoord02, tmpCoord03, tmpDis);
                    if (tmpCoord045 != null) {
                        tmpArray01.add(tmpCoord045[1]);
                    }
                    int tmpCount32 = tempPtnTID - 1;
                    for (int i2 = tempCount02 - 1; i2 > tmpCount32; i2--) {
                        tmpArray01.add(tempCoords00.get(i2));
                    }
                    if (tmpArray01.size() > 0) {
                        Iterator<Coordinate> tmpIter = tmpArray01.iterator();
                        Coordinate tmpCoord052 = tmpIter.next();
                        double tmpLastX = tmpCoord052.getX();
                        double tmpLastY = tmpCoord052.getY();
                        result.add(tmpLastX, tmpLastY);
                        while (tmpIter.hasNext()) {
                            Coordinate tmpCoord053 = tmpIter.next();
                            if (Math.abs(tmpCoord053.getX() - tmpLastX) > 1.0E-5d || Math.abs(tmpCoord053.getY() - tmpLastY) > 1.0E-5d) {
                                tmpLastX = tmpCoord053.getX();
                                tmpLastY = tmpCoord053.getY();
                                result.add(tmpLastX, tmpLastY);
                            } else {
                                tmpLastX = tmpCoord053.getX();
                                tmpLastY = tmpCoord053.getY();
                            }
                        }
                    }
                }
            }
            tempPtnTID += tempCount02;
        }
        return result;
    }

    private Polygon ConvertPolyObjToPolygon(Poly poly) {
        Polygon polyline = new Polygon();
        int count = poly.getNumPoints();
        for (int i = 0; i < count; i++) {
            polyline.GetAllCoordinateList().add(new Coordinate(poly.getX(i), poly.getY(i)));
        }
        return polyline;
    }

    private boolean calDirection(Coordinate tmpCoord01, Coordinate tmpCoord02, Coordinate tmpCoord03) {
        double a1 = tmpCoord02.getX() - tmpCoord01.getX();
        double a2 = tmpCoord03.getX() - tmpCoord02.getX();
        if ((a1 * (tmpCoord03.getY() - tmpCoord02.getY())) - ((tmpCoord02.getY() - tmpCoord01.getY()) * a2) > 0.0d) {
            return true;
        }
        return false;
    }

    private Coordinate calVerticalCoord(Coordinate tmpCoord01, Coordinate tmpCoord02, double distance) {
        Coordinate result = new Coordinate();
        double tmpL01 = ((tmpCoord01.getX() - tmpCoord02.getX()) * (tmpCoord01.getX() - tmpCoord02.getX())) + ((tmpCoord01.getY() - tmpCoord02.getY()) * (tmpCoord01.getY() - tmpCoord02.getY()));
        if (tmpL01 == 0.0d) {
            return null;
        }
        double tmpL012 = distance / Math.sqrt(tmpL01);
        result.setX(tmpCoord01.getX() - ((tmpCoord02.getY() - tmpCoord01.getY()) * tmpL012));
        result.setY(tmpCoord01.getY() - ((tmpCoord01.getX() - tmpCoord02.getX()) * tmpL012));
        return result;
    }

    private Coordinate[] calVerticalCoords(Coordinate tmpCoord01, Coordinate tmpCoord02, double distance) {
        Coordinate[] result = new Coordinate[2];
        double tmpL01 = ((tmpCoord01.getX() - tmpCoord02.getX()) * (tmpCoord01.getX() - tmpCoord02.getX())) + ((tmpCoord01.getY() - tmpCoord02.getY()) * (tmpCoord01.getY() - tmpCoord02.getY()));
        if (tmpL01 == 0.0d) {
            return null;
        }
        double tmpL012 = distance / Math.sqrt(tmpL01);
        result[0] = new Coordinate();
        double tmpX = tmpL012 * (tmpCoord02.getY() - tmpCoord01.getY());
        double tmpY = tmpL012 * (tmpCoord01.getX() - tmpCoord02.getX());
        result[0].setX(tmpCoord01.getX() - tmpX);
        result[0].setY(tmpCoord01.getY() - tmpY);
        result[1] = new Coordinate();
        result[1].setX(tmpCoord02.getX() - tmpX);
        result[1].setY(tmpCoord02.getY() - tmpY);
        return result;
    }

    private boolean ReShapeLine(Polygon polygon, List<Coordinate> newBoundaryCoords, DataSet pDataset, BasicValue outMsg) {
        List<Coordinate> tmpPoltCoords1 = polygon.getBorderLine().GetAllCoordinateList();
        List<Coordinate> tmpPolyCoords = new ArrayList<>();
        for (Coordinate coordinate : tmpPoltCoords1) {
            tmpPolyCoords.add(coordinate.Clone());
        }
        tmpPolyCoords.add(tmpPoltCoords1.get(0).Clone());
        List<Coordinate> tmpSplitCoords = new ArrayList<>();
        double[] tmpInterCoord = new double[2];
        List<Integer> tmpSplitIndex = new ArrayList<>();
        boolean tmpHasSplit = false;
        Coordinate tmpFirstInterCoord = null;
        Coordinate tmpLastInterCoord = null;
        Coordinate tmpCoord01 = newBoundaryCoords.iterator().next();
        double tmpX1 = tmpCoord01.getX();
        double tmpY1 = tmpCoord01.getY();
        int tmpK = -1;
        int tmpM = -1;
        int tmpCount02 = newBoundaryCoords.size();
        int tmpJ = 1;
        while (true) {
            if (tmpJ >= tmpCount02) {
                break;
            }
            Coordinate tmpCoord012 = newBoundaryCoords.get(tmpJ);
            double tmpX2 = tmpCoord012.getX();
            double tmpY2 = tmpCoord012.getY();
            BasicValue tmpIntersectInt = new BasicValue();
            if (SpatialRelation.CalLineSegIntersectPoint(tmpX1, tmpY1, tmpX2, tmpY2, tmpPolyCoords, tmpInterCoord, tmpIntersectInt)) {
                tmpHasSplit = true;
                if (tmpSplitIndex.size() == 0) {
                    tmpFirstInterCoord = new Coordinate(tmpInterCoord[0], tmpInterCoord[1]);
                    tmpSplitIndex.add(Integer.valueOf(tmpIntersectInt.getInt()));
                    tmpK = tmpJ;
                    break;
                }
            }
            if (tmpHasSplit) {
                tmpSplitCoords.add(new Coordinate(tmpX2, tmpY2));
            }
            tmpX1 = tmpX2;
            tmpY1 = tmpY2;
            tmpJ++;
        }
        if (tmpHasSplit) {
            Coordinate tmpCoord013 = newBoundaryCoords.get(tmpCount02 - 1);
            double tmpX12 = tmpCoord013.getX();
            double tmpY12 = tmpCoord013.getY();
            int tmpJ2 = tmpCount02 - 1;
            while (true) {
                if (tmpJ2 < tmpK) {
                    break;
                }
                Coordinate tmpCoord014 = newBoundaryCoords.get(tmpJ2);
                double tmpX22 = tmpCoord014.getX();
                double tmpY22 = tmpCoord014.getY();
                BasicValue tmpIntersectInt2 = new BasicValue();
                if (SpatialRelation.CalLineSegIntersectPoint(tmpX12, tmpY12, tmpX22, tmpY22, tmpPolyCoords, tmpInterCoord, tmpIntersectInt2)) {
                    tmpLastInterCoord = new Coordinate(tmpInterCoord[0], tmpInterCoord[1]);
                    tmpSplitIndex.add(Integer.valueOf(tmpIntersectInt2.getInt()));
                    tmpM = tmpJ2;
                    break;
                }
                tmpJ2--;
            }
        }
        if (tmpM >= tmpK) {
            tmpSplitCoords.add(tmpFirstInterCoord);
            for (int tmpJ3 = tmpK; tmpJ3 <= tmpM; tmpJ3++) {
                tmpSplitCoords.add(newBoundaryCoords.get(tmpJ3).Clone());
            }
            tmpSplitCoords.add(tmpLastInterCoord);
        }
        if (tmpSplitIndex.size() == 2) {
            int tmpStart = tmpSplitIndex.get(0).intValue();
            int tmpEnd = tmpSplitIndex.get(1).intValue();
            List<Coordinate> tmpPolyResult = new ArrayList<>();
            int tmpStart2 = tmpStart + 1;
            int tmpEnd2 = tmpEnd + 1;
            if (tmpEnd2 > tmpStart2) {
                if (tmpEnd2 - tmpStart2 > (tmpPolyCoords.size() + tmpStart2) - tmpEnd2) {
                    tmpPolyResult.add(tmpSplitCoords.get(0).Clone());
                    for (int i = tmpStart2; i < tmpEnd2; i++) {
                        tmpPolyResult.add(tmpPolyCoords.get(i).Clone());
                    }
                    tmpPolyResult.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                    for (int i2 = tmpSplitCoords.size() - 2; i2 > 0; i2--) {
                        tmpPolyResult.add(tmpSplitCoords.get(i2).Clone());
                    }
                } else {
                    tmpPolyResult.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                    int tmpCount = tmpPolyCoords.size();
                    for (int i3 = tmpEnd2; i3 < tmpCount; i3++) {
                        tmpPolyResult.add(tmpPolyCoords.get(i3).Clone());
                    }
                    for (int i4 = 0; i4 < tmpStart2; i4++) {
                        tmpPolyResult.add(tmpPolyCoords.get(i4).Clone());
                    }
                    tmpPolyResult.add(tmpSplitCoords.get(0).Clone());
                    int tmpCount2 = tmpSplitCoords.size() - 1;
                    for (int i5 = 1; i5 < tmpCount2; i5++) {
                        tmpPolyResult.add(tmpSplitCoords.get(i5).Clone());
                    }
                }
            } else if (tmpStart2 - tmpEnd2 > (tmpPolyCoords.size() + tmpEnd2) - tmpStart2) {
                tmpPolyResult.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                for (int i6 = tmpEnd2; i6 < tmpStart2; i6++) {
                    tmpPolyResult.add(tmpPolyCoords.get(i6).Clone());
                }
                tmpPolyResult.add(tmpSplitCoords.get(0).Clone());
                int tmpCount3 = tmpSplitCoords.size() - 1;
                for (int i7 = 1; i7 < tmpCount3; i7++) {
                    tmpPolyResult.add(tmpSplitCoords.get(i7).Clone());
                }
            } else {
                tmpPolyResult.add(tmpSplitCoords.get(0).Clone());
                int tmpCount4 = tmpPolyCoords.size();
                for (int i8 = tmpStart2; i8 < tmpCount4; i8++) {
                    tmpPolyResult.add(tmpPolyCoords.get(i8).Clone());
                }
                for (int i9 = 0; i9 < tmpEnd2; i9++) {
                    tmpPolyResult.add(tmpPolyCoords.get(i9).Clone());
                }
                tmpPolyResult.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                for (int i10 = tmpSplitCoords.size() - 2; i10 > 0; i10--) {
                    tmpPolyResult.add(tmpSplitCoords.get(i10).Clone());
                }
            }
            if (tmpPolyResult.size() > 2) {
                polygon.ResetData();
                polygon.SetAllCoordinateList(tmpPolyResult);
                polygon.SetEdited(true);
                pDataset.JustUpdateGeoMapIndex(polygon);
                pDataset.SaveGeoIndexToDB(polygon);
                pDataset.setEdited(true);
                return true;
            }
        } else {
            outMsg.setValue("??????????????????????????????????????????????????????.");
        }
        return false;
    }

    private boolean ReShapeLine2(Polygon polygon, List<Coordinate> newBoundaryCoords, DataSet pDataset, BasicValue outMsg) {
        List<Coordinate> tmpPoltCoords1 = polygon.getBorderLine().GetAllCoordinateList();
        List<Coordinate> tmpPolyCoords = new ArrayList<>();
        for (Coordinate coordinate : tmpPoltCoords1) {
            tmpPolyCoords.add(coordinate.Clone());
        }
        tmpPolyCoords.add(tmpPoltCoords1.get(0).Clone());
        List<Coordinate> tmpSplitCoords = new ArrayList<>();
        double[] tmpInterCoord = new double[2];
        List<Integer> tmpSplitIndex = new ArrayList<>();
        boolean tmpHasSplit = false;
        Iterator<Coordinate> tmpIter001 = newBoundaryCoords.iterator();
        Coordinate tmpCoord01 = tmpIter001.next();
        double tmpX1 = tmpCoord01.getX();
        double tmpY1 = tmpCoord01.getY();
        while (true) {
            if (!tmpIter001.hasNext()) {
                break;
            }
            Coordinate tmpCoord012 = tmpIter001.next();
            double tmpX2 = tmpCoord012.getX();
            double tmpY2 = tmpCoord012.getY();
            BasicValue tmpIntersectInt = new BasicValue();
            if (SpatialRelation.CalLineSegIntersectPoint(tmpX1, tmpY1, tmpX2, tmpY2, tmpPolyCoords, tmpInterCoord, tmpIntersectInt)) {
                if (tmpSplitIndex.size() != 0) {
                    tmpSplitCoords.add(new Coordinate(tmpInterCoord[0], tmpInterCoord[1]));
                    tmpSplitIndex.add(Integer.valueOf(tmpIntersectInt.getInt()));
                    break;
                }
                tmpSplitCoords.add(new Coordinate(tmpInterCoord[0], tmpInterCoord[1]));
                tmpSplitIndex.add(Integer.valueOf(tmpIntersectInt.getInt()));
                tmpHasSplit = true;
            }
            if (tmpHasSplit) {
                tmpSplitCoords.add(new Coordinate(tmpX2, tmpY2));
            }
            tmpX1 = tmpX2;
            tmpY1 = tmpY2;
        }
        if (tmpSplitIndex.size() == 2) {
            int tmpStart = tmpSplitIndex.get(0).intValue();
            int tmpEnd = tmpSplitIndex.get(1).intValue();
            List<Coordinate> tmpPolyResult = new ArrayList<>();
            int tmpStart2 = tmpStart + 1;
            int tmpEnd2 = tmpEnd + 1;
            if (tmpEnd2 > tmpStart2) {
                if (tmpEnd2 - tmpStart2 > (tmpPolyCoords.size() + tmpStart2) - tmpEnd2) {
                    tmpPolyResult.add(tmpSplitCoords.get(0).Clone());
                    for (int i = tmpStart2; i < tmpEnd2; i++) {
                        tmpPolyResult.add(tmpPolyCoords.get(i).Clone());
                    }
                    tmpPolyResult.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                    for (int i2 = tmpSplitCoords.size() - 2; i2 > 0; i2--) {
                        tmpPolyResult.add(tmpSplitCoords.get(i2).Clone());
                    }
                } else {
                    tmpPolyResult.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                    int tmpCount = tmpPolyCoords.size();
                    for (int i3 = tmpEnd2; i3 < tmpCount; i3++) {
                        tmpPolyResult.add(tmpPolyCoords.get(i3).Clone());
                    }
                    for (int i4 = 0; i4 < tmpStart2; i4++) {
                        tmpPolyResult.add(tmpPolyCoords.get(i4).Clone());
                    }
                    tmpPolyResult.add(tmpSplitCoords.get(0).Clone());
                    int tmpCount2 = tmpSplitCoords.size() - 1;
                    for (int i5 = 1; i5 < tmpCount2; i5++) {
                        tmpPolyResult.add(tmpSplitCoords.get(i5).Clone());
                    }
                }
            } else if (tmpStart2 - tmpEnd2 > (tmpPolyCoords.size() + tmpEnd2) - tmpStart2) {
                tmpPolyResult.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                for (int i6 = tmpEnd2; i6 < tmpStart2; i6++) {
                    tmpPolyResult.add(tmpPolyCoords.get(i6).Clone());
                }
                tmpPolyResult.add(tmpSplitCoords.get(0).Clone());
                int tmpCount3 = tmpSplitCoords.size() - 1;
                for (int i7 = 1; i7 < tmpCount3; i7++) {
                    tmpPolyResult.add(tmpSplitCoords.get(i7).Clone());
                }
            } else {
                tmpPolyResult.add(tmpSplitCoords.get(0).Clone());
                int tmpCount4 = tmpPolyCoords.size();
                for (int i8 = tmpStart2; i8 < tmpCount4; i8++) {
                    tmpPolyResult.add(tmpPolyCoords.get(i8).Clone());
                }
                for (int i9 = 0; i9 < tmpEnd2; i9++) {
                    tmpPolyResult.add(tmpPolyCoords.get(i9).Clone());
                }
                tmpPolyResult.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                for (int i10 = tmpSplitCoords.size() - 2; i10 > 0; i10--) {
                    tmpPolyResult.add(tmpSplitCoords.get(i10).Clone());
                }
            }
            if (tmpPolyResult.size() > 2) {
                polygon.ResetData();
                polygon.SetAllCoordinateList(tmpPolyResult);
                polygon.SetEdited(true);
                pDataset.JustUpdateGeoMapIndex(polygon);
                pDataset.SaveGeoIndexToDB(polygon);
                return true;
            }
        } else {
            outMsg.setValue("??????????????????????????????????????????????????????.");
        }
        return false;
    }

    private boolean ReShapeLines(Polygon polygon, List<Coordinate> newBoundaryCoords, List<Polygon> reshapePolygon, BasicValue outMsg) {
        List<Coordinate> tmpPoltCoords1 = polygon.getBorderLine().GetAllCoordinateList();
        List<Coordinate> tmpPolyCoords = new ArrayList<>();
        for (Coordinate coordinate : tmpPoltCoords1) {
            tmpPolyCoords.add(coordinate.Clone());
        }
        tmpPolyCoords.add(tmpPoltCoords1.get(0).Clone());
        List<Coordinate> tmpSplitCoords = new ArrayList<>();
        double[] tmpInterCoord = new double[2];
        List<Integer> tmpSplitIndex = new ArrayList<>();
        boolean tmpHasSplit = false;
        Coordinate tmpFirstInterCoord = null;
        Coordinate tmpLastInterCoord = null;
        Coordinate tmpCoord01 = newBoundaryCoords.iterator().next();
        double tmpX1 = tmpCoord01.getX();
        double tmpY1 = tmpCoord01.getY();
        int tmpK = -1;
        int tmpM = -1;
        int tmpCount02 = newBoundaryCoords.size();
        int tmpJ = 1;
        while (true) {
            if (tmpJ >= tmpCount02) {
                break;
            }
            Coordinate tmpCoord012 = newBoundaryCoords.get(tmpJ);
            double tmpX2 = tmpCoord012.getX();
            double tmpY2 = tmpCoord012.getY();
            BasicValue tmpIntersectInt = new BasicValue();
            if (SpatialRelation.CalLineSegIntersectPoint(tmpX1, tmpY1, tmpX2, tmpY2, tmpPolyCoords, tmpInterCoord, tmpIntersectInt)) {
                tmpHasSplit = true;
                if (tmpSplitIndex.size() == 0) {
                    tmpFirstInterCoord = new Coordinate(tmpInterCoord[0], tmpInterCoord[1]);
                    tmpSplitIndex.add(Integer.valueOf(tmpIntersectInt.getInt()));
                    tmpK = tmpJ;
                    break;
                }
            }
            if (tmpHasSplit) {
                tmpSplitCoords.add(new Coordinate(tmpX2, tmpY2));
            }
            tmpX1 = tmpX2;
            tmpY1 = tmpY2;
            tmpJ++;
        }
        if (tmpHasSplit) {
            Coordinate tmpCoord013 = newBoundaryCoords.get(tmpCount02 - 1);
            double tmpX12 = tmpCoord013.getX();
            double tmpY12 = tmpCoord013.getY();
            int tmpJ2 = tmpCount02 - 1;
            while (true) {
                if (tmpJ2 < tmpK) {
                    break;
                }
                Coordinate tmpCoord014 = newBoundaryCoords.get(tmpJ2);
                double tmpX22 = tmpCoord014.getX();
                double tmpY22 = tmpCoord014.getY();
                BasicValue tmpIntersectInt2 = new BasicValue();
                if (SpatialRelation.CalLineSegIntersectPoint(tmpX12, tmpY12, tmpX22, tmpY22, tmpPolyCoords, tmpInterCoord, tmpIntersectInt2)) {
                    tmpLastInterCoord = new Coordinate(tmpInterCoord[0], tmpInterCoord[1]);
                    tmpSplitIndex.add(Integer.valueOf(tmpIntersectInt2.getInt()));
                    tmpM = tmpJ2;
                    break;
                }
                tmpJ2--;
            }
        }
        if (tmpM >= tmpK) {
            tmpSplitCoords.add(tmpFirstInterCoord);
            for (int tmpJ3 = tmpK; tmpJ3 <= tmpM; tmpJ3++) {
                tmpSplitCoords.add(newBoundaryCoords.get(tmpJ3).Clone());
            }
            tmpSplitCoords.add(tmpLastInterCoord);
        }
        if (tmpSplitIndex.size() == 2) {
            int tmpStart = tmpSplitIndex.get(0).intValue();
            int tmpEnd = tmpSplitIndex.get(1).intValue();
            List<Coordinate> tmpPoly01 = new ArrayList<>();
            List<Coordinate> tmpPoly02 = new ArrayList<>();
            int tmpStart2 = tmpStart + 1;
            int tmpEnd2 = tmpEnd + 1;
            if (tmpEnd2 > tmpStart2) {
                tmpPoly01.add(tmpSplitCoords.get(0).Clone());
                for (int i = tmpStart2; i < tmpEnd2; i++) {
                    tmpPoly01.add(tmpPolyCoords.get(i).Clone());
                }
                tmpPoly01.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                for (int i2 = tmpSplitCoords.size() - 2; i2 > 0; i2--) {
                    tmpPoly01.add(tmpSplitCoords.get(i2).Clone());
                }
                tmpPoly02.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                int tmpCount = tmpPolyCoords.size();
                for (int i3 = tmpEnd2; i3 < tmpCount; i3++) {
                    tmpPoly02.add(tmpPolyCoords.get(i3).Clone());
                }
                for (int i4 = 0; i4 < tmpStart2; i4++) {
                    tmpPoly02.add(tmpPolyCoords.get(i4).Clone());
                }
                tmpPoly02.add(tmpSplitCoords.get(0).Clone());
                int tmpCount2 = tmpSplitCoords.size() - 1;
                for (int i5 = 1; i5 < tmpCount2; i5++) {
                    tmpPoly02.add(tmpSplitCoords.get(i5).Clone());
                }
            } else if (tmpEnd2 == tmpStart2) {
                Coordinate tmpCoord003 = tmpPolyCoords.get(tmpEnd2 - 1);
                if (Common.GetTwoPointDistance(tmpLastInterCoord, tmpCoord003) > Common.GetTwoPointDistance(tmpFirstInterCoord, tmpCoord003)) {
                    for (int i6 = 0; i6 < tmpStart2; i6++) {
                        tmpPoly01.add(tmpPolyCoords.get(i6).Clone());
                    }
                    for (Coordinate tmpCoord004 : tmpSplitCoords) {
                        tmpPoly01.add(tmpCoord004.Clone());
                    }
                    int tmpCount003 = tmpPolyCoords.size();
                    for (int i7 = tmpStart2; i7 < tmpCount003; i7++) {
                        tmpPoly01.add(tmpPolyCoords.get(i7).Clone());
                    }
                } else {
                    for (int i8 = 0; i8 < tmpStart2; i8++) {
                        tmpPoly01.add(tmpPolyCoords.get(i8).Clone());
                    }
                    for (int i9 = tmpSplitCoords.size() - 1; i9 > -1; i9--) {
                        tmpPoly01.add(tmpSplitCoords.get(i9).Clone());
                    }
                    int tmpCount0032 = tmpPolyCoords.size();
                    for (int i10 = tmpStart2; i10 < tmpCount0032; i10++) {
                        tmpPoly01.add(tmpPolyCoords.get(i10).Clone());
                    }
                }
            } else {
                tmpPoly01.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                for (int i11 = tmpEnd2; i11 < tmpStart2; i11++) {
                    tmpPoly01.add(tmpPolyCoords.get(i11).Clone());
                }
                tmpPoly01.add(tmpSplitCoords.get(0).Clone());
                int tmpCount3 = tmpSplitCoords.size() - 1;
                for (int i12 = 1; i12 < tmpCount3; i12++) {
                    tmpPoly01.add(tmpSplitCoords.get(i12).Clone());
                }
                tmpPoly02.add(tmpSplitCoords.get(0).Clone());
                int tmpCount4 = tmpPolyCoords.size();
                for (int i13 = tmpStart2; i13 < tmpCount4; i13++) {
                    tmpPoly02.add(tmpPolyCoords.get(i13).Clone());
                }
                for (int i14 = 0; i14 < tmpEnd2; i14++) {
                    tmpPoly02.add(tmpPolyCoords.get(i14).Clone());
                }
                tmpPoly02.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                for (int i15 = tmpSplitCoords.size() - 2; i15 > 0; i15--) {
                    tmpPoly02.add(tmpSplitCoords.get(i15).Clone());
                }
            }
            if (tmpPoly01.size() > 2) {
                Polygon tmpPoly001 = new Polygon();
                tmpPoly001.SetAllCoordinateList(tmpPoly01);
                reshapePolygon.add(tmpPoly001);
            }
            if (tmpPoly02.size() > 2) {
                Polygon tmpPoly0012 = new Polygon();
                tmpPoly0012.SetAllCoordinateList(tmpPoly02);
                reshapePolygon.add(tmpPoly0012);
            }
            if (reshapePolygon.size() > 0) {
                return true;
            }
        } else {
            outMsg.setValue("??????????????????????????????????????????????????????.");
        }
        return false;
    }

    private boolean ReShapeLines2(Polygon polygon, List<Coordinate> newBoundaryCoords, List<Polygon> reshapePolygon, BasicValue outMsg) {
        List<Coordinate> tmpPoltCoords1 = polygon.getBorderLine().GetAllCoordinateList();
        List<Coordinate> tmpPolyCoords = new ArrayList<>();
        for (Coordinate coordinate : tmpPoltCoords1) {
            tmpPolyCoords.add(coordinate.Clone());
        }
        tmpPolyCoords.add(tmpPoltCoords1.get(0).Clone());
        List<Coordinate> tmpSplitCoords = new ArrayList<>();
        double[] tmpInterCoord = new double[2];
        List<Integer> tmpSplitIndex = new ArrayList<>();
        boolean tmpHasSplit = false;
        Iterator<Coordinate> tmpIter001 = newBoundaryCoords.iterator();
        Coordinate tmpCoord01 = tmpIter001.next();
        double tmpX1 = tmpCoord01.getX();
        double tmpY1 = tmpCoord01.getY();
        while (true) {
            if (!tmpIter001.hasNext()) {
                break;
            }
            Coordinate tmpCoord012 = tmpIter001.next();
            double tmpX2 = tmpCoord012.getX();
            double tmpY2 = tmpCoord012.getY();
            BasicValue tmpIntersectInt = new BasicValue();
            if (SpatialRelation.CalLineSegIntersectPoint(tmpX1, tmpY1, tmpX2, tmpY2, tmpPolyCoords, tmpInterCoord, tmpIntersectInt)) {
                if (tmpSplitIndex.size() != 0) {
                    tmpSplitCoords.add(new Coordinate(tmpInterCoord[0], tmpInterCoord[1]));
                    tmpSplitIndex.add(Integer.valueOf(tmpIntersectInt.getInt()));
                    break;
                }
                tmpSplitCoords.add(new Coordinate(tmpInterCoord[0], tmpInterCoord[1]));
                tmpSplitIndex.add(Integer.valueOf(tmpIntersectInt.getInt()));
                tmpHasSplit = true;
            }
            if (tmpHasSplit) {
                tmpSplitCoords.add(new Coordinate(tmpX2, tmpY2));
            }
            tmpX1 = tmpX2;
            tmpY1 = tmpY2;
        }
        if (tmpSplitIndex.size() == 2) {
            int tmpStart = tmpSplitIndex.get(0).intValue();
            int tmpEnd = tmpSplitIndex.get(1).intValue();
            List<Coordinate> tmpPoly01 = new ArrayList<>();
            List<Coordinate> tmpPoly02 = new ArrayList<>();
            int tmpStart2 = tmpStart + 1;
            int tmpEnd2 = tmpEnd + 1;
            if (tmpEnd2 > tmpStart2) {
                tmpPoly01.add(tmpSplitCoords.get(0).Clone());
                for (int i = tmpStart2; i < tmpEnd2; i++) {
                    tmpPoly01.add(tmpPolyCoords.get(i).Clone());
                }
                tmpPoly01.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                for (int i2 = tmpSplitCoords.size() - 2; i2 > 0; i2--) {
                    tmpPoly01.add(tmpSplitCoords.get(i2).Clone());
                }
                tmpPoly02.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                int tmpCount = tmpPolyCoords.size();
                for (int i3 = tmpEnd2; i3 < tmpCount; i3++) {
                    tmpPoly02.add(tmpPolyCoords.get(i3).Clone());
                }
                for (int i4 = 0; i4 < tmpStart2; i4++) {
                    tmpPoly02.add(tmpPolyCoords.get(i4).Clone());
                }
                tmpPoly02.add(tmpSplitCoords.get(0).Clone());
                int tmpCount2 = tmpSplitCoords.size() - 1;
                for (int i5 = 1; i5 < tmpCount2; i5++) {
                    tmpPoly02.add(tmpSplitCoords.get(i5).Clone());
                }
            } else {
                tmpPoly01.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                for (int i6 = tmpEnd2; i6 < tmpStart2; i6++) {
                    tmpPoly01.add(tmpPolyCoords.get(i6).Clone());
                }
                tmpPoly01.add(tmpSplitCoords.get(0).Clone());
                int tmpCount3 = tmpSplitCoords.size() - 1;
                for (int i7 = 1; i7 < tmpCount3; i7++) {
                    tmpPoly01.add(tmpSplitCoords.get(i7).Clone());
                }
                tmpPoly02.add(tmpSplitCoords.get(0).Clone());
                int tmpCount4 = tmpPolyCoords.size();
                for (int i8 = tmpStart2; i8 < tmpCount4; i8++) {
                    tmpPoly02.add(tmpPolyCoords.get(i8).Clone());
                }
                for (int i9 = 0; i9 < tmpEnd2; i9++) {
                    tmpPoly02.add(tmpPolyCoords.get(i9).Clone());
                }
                tmpPoly02.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                for (int i10 = tmpSplitCoords.size() - 2; i10 > 0; i10--) {
                    tmpPoly02.add(tmpSplitCoords.get(i10).Clone());
                }
            }
            if (tmpPoly01.size() > 2) {
                Polygon tmpPoly001 = new Polygon();
                tmpPoly001.SetAllCoordinateList(tmpPoly01);
                reshapePolygon.add(tmpPoly001);
            }
            if (tmpPoly02.size() > 2) {
                Polygon tmpPoly0012 = new Polygon();
                tmpPoly0012.SetAllCoordinateList(tmpPoly02);
                reshapePolygon.add(tmpPoly0012);
            }
            if (reshapePolygon.size() > 0) {
                return true;
            }
        } else {
            outMsg.setValue("??????????????????????????????????????????????????????.");
        }
        return false;
    }

    public static Polygon ConvertPointsToPolygon(List<Coordinate> coords) {
        Polygon result = new Polygon();
        boolean tmpBool = false;
        Iterator<Coordinate> tmpIter001 = coords.iterator();
        Coordinate tmpCoord01 = tmpIter001.next();
        double tmpX1 = tmpCoord01.getX();
        double tmpY1 = tmpCoord01.getY();
        int tid = 1;
        int tmpCount = coords.size();
        while (tmpIter001.hasNext() && !tmpBool) {
            tid++;
            Coordinate tmpCoord012 = tmpIter001.next();
            double tmpX2 = tmpCoord012.getX();
            double tmpY2 = tmpCoord012.getY();
            if (tid < tmpCount - 1) {
                Coordinate tmpCoord03 = coords.get(tid);
                double line02X1 = tmpCoord03.getX();
                double line02Y1 = tmpCoord03.getY();
                int i = tid + 1;
                while (true) {
                    if (i >= tmpCount) {
                        break;
                    }
                    Coordinate tmpCoord032 = coords.get(i);
                    double line02X2 = tmpCoord032.getX();
                    double line02Y2 = tmpCoord032.getY();
                    if (SpatialRelation.Judge2LineIntersect(tmpX1, tmpY1, tmpX2, tmpY2, line02X1, line02Y1, line02X2, line02Y2)) {
                        double[] tmpInterCoords = new double[2];
                        if (SpatialRelation.Cal2LineSegIntersectPoint(tmpX1, tmpY1, tmpX2, tmpY2, line02X1, line02Y1, line02X2, line02Y2, tmpInterCoords)) {
                            List<Coordinate> tmpList2 = new ArrayList<>();
                            tmpList2.add(new Coordinate(tmpInterCoords[0], tmpInterCoords[1]));
                            for (int j = tid - 1; j < i; j++) {
                                tmpList2.add(coords.get(j).Clone());
                            }
                            if (tmpList2.size() > 2) {
                                tmpBool = true;
                                result.SetAllCoordinateList(tmpList2);
                                break;
                            }
                        } else {
                            continue;
                        }
                    }
                    line02X1 = line02X2;
                    line02Y1 = line02Y2;
                    i++;
                }
            }
        }
        if (!tmpBool) {
            result.SetAllCoordinateList(coords);
        }
        return result;
    }

    public void ShowEditProp(String layerID, int SYS_ID) {
        FeatureAttribute_Dialog tempDialog = new FeatureAttribute_Dialog();
        tempDialog.SetLayerID(layerID);
        tempDialog.SetSYS_ID(SYS_ID);
        tempDialog.SetCallback(this.pCallback);
        tempDialog.ShowDialog();
    }

    private boolean ReShapeLines3(Polygon polygon, List<Coordinate> newBoundaryCoords, List<Polygon> reshapePolygon, BasicValue outMsg) {
        List<Coordinate> tmpPoltCoords1 = polygon.getBorderLine().GetAllCoordinateList();
        List<Coordinate> tmpPolyCoords = new ArrayList<>();
        for (Coordinate coordinate : tmpPoltCoords1) {
            tmpPolyCoords.add(coordinate.Clone());
        }
        tmpPolyCoords.add(tmpPoltCoords1.get(0).Clone());
        List<Coordinate> tmpSplitCoords = new ArrayList<>();
        double[] tmpInterCoord = new double[2];
        List<Integer> tmpSplitIndex = new ArrayList<>();
        boolean tmpHasSplit = false;
        Coordinate tmpFirstInterCoord = null;
        Coordinate tmpLastInterCoord = null;
        Coordinate tmpCoord01 = newBoundaryCoords.iterator().next();
        double tmpX1 = tmpCoord01.getX();
        double tmpY1 = tmpCoord01.getY();
        int tmpK = -1;
        int tmpM = -1;
        int tmpCount02 = newBoundaryCoords.size();
        int tmpJ = 1;
        while (true) {
            if (tmpJ >= tmpCount02) {
                break;
            }
            Coordinate tmpCoord012 = newBoundaryCoords.get(tmpJ);
            double tmpX2 = tmpCoord012.getX();
            double tmpY2 = tmpCoord012.getY();
            BasicValue tmpIntersectInt = new BasicValue();
            if (SpatialRelation.CalLineSegIntersectPoint(tmpX1, tmpY1, tmpX2, tmpY2, tmpPolyCoords, tmpInterCoord, tmpIntersectInt)) {
                tmpHasSplit = true;
                if (tmpSplitIndex.size() == 0) {
                    tmpFirstInterCoord = new Coordinate(tmpInterCoord[0], tmpInterCoord[1]);
                    tmpSplitIndex.add(Integer.valueOf(tmpIntersectInt.getInt()));
                    tmpK = tmpJ;
                    break;
                }
            }
            if (tmpHasSplit) {
                tmpSplitCoords.add(new Coordinate(tmpX2, tmpY2));
            }
            tmpX1 = tmpX2;
            tmpY1 = tmpY2;
            tmpJ++;
        }
        if (tmpHasSplit) {
            Coordinate tmpCoord013 = newBoundaryCoords.get(tmpCount02 - 1);
            double tmpX12 = tmpCoord013.getX();
            double tmpY12 = tmpCoord013.getY();
            int tmpJ2 = tmpCount02 - 1;
            while (true) {
                if (tmpJ2 < tmpK) {
                    break;
                }
                Coordinate tmpCoord014 = newBoundaryCoords.get(tmpJ2);
                double tmpX22 = tmpCoord014.getX();
                double tmpY22 = tmpCoord014.getY();
                BasicValue tmpIntersectInt2 = new BasicValue();
                if (SpatialRelation.CalLineSegIntersectPoint(tmpX12, tmpY12, tmpX22, tmpY22, tmpPolyCoords, tmpInterCoord, tmpIntersectInt2)) {
                    tmpLastInterCoord = new Coordinate(tmpInterCoord[0], tmpInterCoord[1]);
                    tmpSplitIndex.add(Integer.valueOf(tmpIntersectInt2.getInt()));
                    tmpM = tmpJ2;
                    break;
                }
                tmpJ2--;
            }
        }
        if (tmpM >= tmpK) {
            tmpSplitCoords.add(tmpFirstInterCoord);
            for (int tmpJ3 = tmpK; tmpJ3 <= tmpM; tmpJ3++) {
                tmpSplitCoords.add(newBoundaryCoords.get(tmpJ3).Clone());
            }
            tmpSplitCoords.add(tmpLastInterCoord);
        }
        if (tmpSplitIndex.size() == 2) {
            int tmpStart = tmpSplitIndex.get(0).intValue();
            int tmpEnd = tmpSplitIndex.get(1).intValue();
            List<Coordinate> tmpPoly01 = new ArrayList<>();
            List<Coordinate> tmpPoly02 = new ArrayList<>();
            int tmpStart2 = tmpStart + 1;
            int tmpEnd2 = tmpEnd + 1;
            if (tmpEnd2 > tmpStart2) {
                tmpPoly01.add(tmpSplitCoords.get(0).Clone());
                for (int i = tmpStart2; i < tmpEnd2; i++) {
                    tmpPoly01.add(tmpPolyCoords.get(i).Clone());
                }
                tmpPoly01.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                for (int i2 = tmpSplitCoords.size() - 2; i2 > 0; i2--) {
                    tmpPoly01.add(tmpSplitCoords.get(i2).Clone());
                }
                tmpPoly02.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                int tmpCount = tmpPolyCoords.size();
                for (int i3 = tmpEnd2; i3 < tmpCount; i3++) {
                    tmpPoly02.add(tmpPolyCoords.get(i3).Clone());
                }
                for (int i4 = 0; i4 < tmpStart2; i4++) {
                    tmpPoly02.add(tmpPolyCoords.get(i4).Clone());
                }
                tmpPoly02.add(tmpSplitCoords.get(0).Clone());
                int tmpCount2 = tmpSplitCoords.size() - 1;
                for (int i5 = 1; i5 < tmpCount2; i5++) {
                    tmpPoly02.add(tmpSplitCoords.get(i5).Clone());
                }
            } else if (tmpEnd2 == tmpStart2) {
                Coordinate tmpCoord003 = tmpPolyCoords.get(tmpEnd2 - 1);
                if (Common.GetTwoPointDistance(tmpLastInterCoord, tmpCoord003) > Common.GetTwoPointDistance(tmpFirstInterCoord, tmpCoord003)) {
                    for (int i6 = 0; i6 < tmpStart2; i6++) {
                        tmpPoly01.add(tmpPolyCoords.get(i6).Clone());
                    }
                    for (Coordinate tmpCoord004 : tmpSplitCoords) {
                        tmpPoly01.add(tmpCoord004.Clone());
                    }
                    int tmpCount003 = tmpPolyCoords.size();
                    for (int i7 = tmpStart2; i7 < tmpCount003; i7++) {
                        tmpPoly01.add(tmpPolyCoords.get(i7).Clone());
                    }
                } else {
                    for (int i8 = 0; i8 < tmpStart2; i8++) {
                        tmpPoly01.add(tmpPolyCoords.get(i8).Clone());
                    }
                    for (int i9 = tmpSplitCoords.size() - 1; i9 > -1; i9--) {
                        tmpPoly01.add(tmpSplitCoords.get(i9).Clone());
                    }
                    int tmpCount0032 = tmpPolyCoords.size();
                    for (int i10 = tmpStart2; i10 < tmpCount0032; i10++) {
                        tmpPoly01.add(tmpPolyCoords.get(i10).Clone());
                    }
                }
            } else {
                tmpPoly01.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                for (int i11 = tmpEnd2; i11 < tmpStart2; i11++) {
                    tmpPoly01.add(tmpPolyCoords.get(i11).Clone());
                }
                tmpPoly01.add(tmpSplitCoords.get(0).Clone());
                int tmpCount3 = tmpSplitCoords.size() - 1;
                for (int i12 = 1; i12 < tmpCount3; i12++) {
                    tmpPoly01.add(tmpSplitCoords.get(i12).Clone());
                }
                tmpPoly02.add(tmpSplitCoords.get(0).Clone());
                int tmpCount4 = tmpPolyCoords.size();
                for (int i13 = tmpStart2; i13 < tmpCount4; i13++) {
                    tmpPoly02.add(tmpPolyCoords.get(i13).Clone());
                }
                for (int i14 = 0; i14 < tmpEnd2; i14++) {
                    tmpPoly02.add(tmpPolyCoords.get(i14).Clone());
                }
                tmpPoly02.add(tmpSplitCoords.get(tmpSplitCoords.size() - 1).Clone());
                for (int i15 = tmpSplitCoords.size() - 2; i15 > 0; i15--) {
                    tmpPoly02.add(tmpSplitCoords.get(i15).Clone());
                }
            }
            if (tmpPoly01.size() > 2) {
                Polygon tmpPoly001 = new Polygon();
                tmpPoly001.SetAllCoordinateList(tmpPoly01);
                reshapePolygon.add(tmpPoly001);
            }
            if (reshapePolygon.size() > 0) {
                return true;
            }
        } else {
            outMsg.setValue("??????????????????????????????????????????????????????.");
        }
        return false;
    }
}
