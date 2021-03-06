package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.MapTools;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Edit.AddPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.PolygonEditClass;
import  com.xzy.forestSystem.gogisapi.Edit.PolygonSplitByPoly_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.PolygonUnion_Dialog;
import  com.xzy.forestSystem.gogisapi.GPS.AddGPSPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.BaseDataObject;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Selection;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
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

public class Toolbar_Polygon2 extends BaseToolbar {
    static final double SPLIT_BIAS = 1.0E-7d;
    private Drawable _GPSRecordBg01 = null;
    private Drawable _GPSRecordBg02 = null;
    private boolean _IsRecordByGPS = false;
    private Coordinate _LastCoord = null;
    private long _LastLocTime = 0;
    private BaseDataObject m_EditBaseDataObject = null;
    private String m_EditLayerID = null;
    private Button m_GPSBtn = null;
    private PolygonEditClass m_PolygonEditClass = null;
    private Toolbar_Layers m_Toolbar_Layers = null;
    private Handler myGPSRecordHandler = new Handler();
    private Runnable myRecordLineByGPSTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon2.2
        @Override // java.lang.Runnable
        public void run() {
            if (Toolbar_Polygon2.this._IsRecordByGPS) {
                try {
                    Coordinate tempCoord = PubVar._PubCommand.m_GpsLocation.getGPSCoordinate();
                    if (!tempCoord.Equal(Toolbar_Polygon2.this._LastCoord)) {
                        boolean tempBool = false;
                        long tempTime = new Date().getTime();
                        if (Toolbar_Polygon2.this._LastCoord == null) {
                            tempBool = true;
                        } else if (PubVar.GPSGatherIntervalTime == 0 && PubVar.GPSGatherIntervalDistance == 0.0d) {
                            tempBool = true;
                        } else if (PubVar.GPSGatherIntervalTime == 0) {
                            if (Toolbar_Polygon2.this._LastCoord == null) {
                                tempBool = true;
                            } else if (PubVar.GPSGatherIntervalDistance == 0.0d) {
                                tempBool = true;
                            } else if (Common.GetTwoPointDistance(tempCoord, Toolbar_Polygon2.this._LastCoord) >= PubVar.GPSGatherIntervalDistance) {
                                tempBool = true;
                            }
                        } else if (PubVar.GPSGatherIntervalDistance == 0.0d) {
                            if (tempTime - Toolbar_Polygon2.this._LastLocTime >= PubVar.GPSGatherIntervalTime) {
                                tempBool = true;
                            }
                        } else if (tempTime - Toolbar_Polygon2.this._LastLocTime >= PubVar.GPSGatherIntervalTime) {
                            if (Toolbar_Polygon2.this._LastCoord == null) {
                                tempBool = true;
                            } else if (PubVar.GPSGatherIntervalDistance == 0.0d) {
                                tempBool = true;
                            } else if (Common.GetTwoPointDistance(tempCoord, Toolbar_Polygon2.this._LastCoord) >= PubVar.GPSGatherIntervalDistance) {
                                tempBool = true;
                            }
                        }
                        if (tempBool && (PubVar.GPS_Gather_Accuracy == 0.0d || PubVar._PubCommand.m_GpsLocation.accuracy <= PubVar.GPS_Gather_Accuracy)) {
                            Toolbar_Polygon2.this._LastCoord = tempCoord;
                            Toolbar_Polygon2.this._LastLocTime = tempTime;
                            if (Toolbar_Polygon2.this.m_PolygonEditClass != null) {
                                Toolbar_Polygon2.this.m_PolygonEditClass.SetCurrentDrawType("GPS");
                                Toolbar_Polygon2.this.m_PolygonEditClass.AddPoint(tempCoord);
                            }
                        }
                    }
                } catch (Exception e) {
                }
                Toolbar_Polygon2.this.myGPSRecordHandler.postDelayed(Toolbar_Polygon2.this.myRecordLineByGPSTask, 1000);
            }
        }
    };
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon2.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
            if (paramString.equals("??????????????????")) {
                Coordinate tempCoord = (Coordinate) pObject;
                if (tempCoord != null && Toolbar_Polygon2.this.m_PolygonEditClass != null) {
                    PubVar._PubCommand.ProcessCommand("??????_?????????");
                    Toolbar_Polygon2.this.m_PolygonEditClass.SetCurrentDrawType("??????");
                    Toolbar_Polygon2.this.m_PolygonEditClass.AddPoint(tempCoord);
                    Common.ShowToast("???????????????????????????.");
                }
            } else if (paramString.equals("??????GPS???????????????")) {
                Coordinate tempCoord2 = (Coordinate) pObject;
                if (tempCoord2 != null && Toolbar_Polygon2.this.m_PolygonEditClass != null) {
                    PubVar._PubCommand.ProcessCommand("??????_?????????");
                    Toolbar_Polygon2.this.m_PolygonEditClass.SetCurrentDrawType("GPS");
                    Toolbar_Polygon2.this.m_PolygonEditClass.AddPoint(tempCoord2);
                    Common.ShowToast("????????????GPS?????????.");
                }
            } else if (paramString.equals("???????????????") && pObject != null) {
                Toolbar_Polygon2.this.DoCommand(String.valueOf(pObject));
            }
        }
    };

    public Toolbar_Polygon2(Context context, MapView mapView) {
        super(context, mapView);
        this.m_ToolbarName = "??????????????????";
        this.m_IsAllowedClose = false;
    }

    public void SetEditLayerID(String layerID) {
        this.m_EditLayerID = layerID;
    }

    public void SetEditPolygon(PolygonEditClass polyEditClass) {
        this.m_PolygonEditClass = polyEditClass;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void LoadToolBar(View view, int mainLinearLayoutID) {
        super.LoadToolBar(view, mainLinearLayoutID);
        this.m_view.findViewById(R.id.bt_PolygonMenu1).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_Exit).setOnClickListener(this.buttonClickListener);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Show() {
        super.Show();
        SetEditPolygon(PubVar._PubCommand.GetPolygonEdit());
        StartEdit();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Hide() {
        DoCommand("??????GPS??????");
        PubVar._PubCommand.HideToolbar("????????????????????????");
        super.Hide();
        if (this.m_PolygonEditClass != null) {
            this.m_PolygonEditClass.Clear();
        }
        this.m_EditLayerID = null;
        super.SaveConfigDB();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(View view) {
        Object tempObj = view.getTag();
        if (tempObj != null) {
            String command = tempObj.toString();
            if (command.equals("??????")) {
                int[] tmpXY = Common.GetViewSize(view);
                Toolbar_MenuItem.ShowMenuItemWindow(PubVar.MainContext, view, new String[]{"??????", "??????", "??????", "??????", "??????", "??????"}, new int[]{R.drawable.polygon11132, R.drawable.polygon11132, R.drawable.polygon11132, R.drawable.polygon11132, R.drawable.polygon11132, R.drawable.polygon11132}, new String[]{"?????????", "???????????????", "???????????????", "???????????????", "??????", "??????"}, (float) tmpXY[0], (float) tmpXY[1], this.pCallback);
            } else if (command.equals("????????????")) {
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
                    return;
                }
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.m_view.getContext());
                localBuilder.setIcon(R.drawable.messageinfo);
                localBuilder.setTitle("????????????");
                localBuilder.setMessage("?????????" + tempCount2 + "?????????.\n????????????????????????,?????????????");
                localBuilder.setPositiveButton("??????", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon2.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (PubVar._PubCommand.GetDeleteObject().Delete(Toolbar_Polygon2.this.m_EditLayerID, tmpSYSIDList, true)) {
                            PubVar._MapView._Select.ClearAllSelection();
                            PubVar._Map.RefreshFast();
                        }
                    }
                });
                localBuilder.setNegativeButton("??????", (DialogInterface.OnClickListener) null);
                localBuilder.show();
            } else if (!command.equals("???????????????")) {
                DoCommand(command);
            } else if (getButtonIsSelected("????????????")) {
                PubVar._PubCommand.ProcessCommand("????????????");
                Common.ShowToast("??????????????????");
            } else {
                DoCommand(command);
            }
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(String command) {
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
                String[] tempOutMsg = new String[1];
                if (!this.m_PolygonEditClass.UndoLastPoint(tempOutMsg)) {
                    Common.ShowToast(tempOutMsg[0]);
                }
            } else if (command.equals("??????")) {
                String[] tempOutMsg2 = new String[1];
                if (!this.m_PolygonEditClass.RedoLastPoint(tempOutMsg2)) {
                    Common.ShowToast(tempOutMsg2[0]);
                }
            } else if (command.equals("?????????")) {
                if (this.m_EditBaseDataObject != null) {
                    FeatureAttribute_Dialog tempDialog3 = new FeatureAttribute_Dialog();
                    tempDialog3.SetBaseDataObject(this.m_EditBaseDataObject, this.m_EditLayerID);
                    tempDialog3.SetEditMode(1);
                    tempDialog3.ShowDialog();
                }
            } else if (command.equals("???????????????")) {
                if (this.m_PolygonEditClass.getPointsCount() > 0) {
                    Common.ShowYesNoDialog(this.m_context, "???????????????????????????????", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon2.4
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (paramString.equals("YES")) {
                                Toolbar_Polygon2.this.StartEdit();
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
                List<HashMap<String, Object>> tmpLineList = Common.GetSelectObjects(PubVar._Map, -1, 1, true, tmpParam);
                if (PubVar._PubCommand.GetPolygonEdit().getPointsCount() > 1) {
                    Polyline tmpPoly = new Polyline();
                    tmpPoly.SetAllCoordinateList(PubVar._PubCommand.GetPolygonEdit().GetAllCoordinates());
                    HashMap<String, Object> tmpHash = new HashMap<>();
                    tmpHash.put("LayerName", "");
                    tmpHash.put("LayerID", "");
                    tmpHash.put("DataSource", "");
                    tmpHash.put("Editable", false);
                    tmpHash.put("GeoType", 1);
                    tmpHash.put("GeoIndex", -1);
                    tmpHash.put("Geometry", tmpPoly);
                    tmpLineList.add(tmpHash);
                }
                if (tmpLineList.size() <= 0) {
                    Common.ShowDialog("??????????????????????????????????????????.");
                    return;
                }
                List<HashMap<String, Object>> tmpPolyList = Common.GetSelectObjects(PubVar._Map, -1, 2, true, tmpParam);
                if (tmpPolyList.size() <= 0) {
                    Common.ShowDialog("??????????????????????????????????????????????????????.");
                } else if (SplitPolygonByLines(tmpPolyList, tmpLineList)) {
                    PubVar._PubCommand.ProcessCommand("??????_??????????????????");
                    PubVar._PubCommand.GetPolygonEdit().Clear();
                    PubVar._Map.Refresh();
                    Common.ShowToast("????????????.");
                }
            } else if (command.equals("??????")) {
                BasicValue tmpParam2 = new BasicValue();
                List<HashMap<String, Object>> tmpList = Common.GetSelectObjects(PubVar._Map, -1, 2, true, tmpParam2);
                if (tmpParam2.getInt() <= 0 || tmpList.size() <= 1) {
                    Common.ShowDialog("???????????????????????????.");
                    return;
                }
                PolygonSplitByPoly_Dialog tempDialog4 = new PolygonSplitByPoly_Dialog();
                tempDialog4.SetPolygons(tmpList);
                tempDialog4.SetMainPolyIndex(0);
                tempDialog4.ShowDialog();
            } else if (command.equals("??????")) {
                if (PubVar._PubCommand.GetPolygonEdit().getPointsCount() > 1) {
                    List<Coordinate> tmpCoords = PubVar._PubCommand.GetPolygonEdit().GetAllCoordinates();
                    BasicValue tmpParam3 = new BasicValue();
                    List<HashMap<String, Object>> tmpList2 = Common.GetSelectObjects(PubVar._Map, -1, 2, false, tmpParam3);
                    if (tmpParam3.getInt() <= 0 || tmpList2.size() <= 0) {
                        Common.ShowDialog("???????????????????????????.");
                        return;
                    }
                    boolean tmpBool = false;
                    for (HashMap<String, Object> tmpHash2 : tmpList2) {
                        if (ReShapeLine((Polygon) tmpHash2.get("Geometry"), tmpCoords, new BasicValue())) {
                            tmpBool = true;
                        }
                    }
                    if (tmpBool) {
                        PubVar._Map.Refresh();
                        return;
                    }
                    return;
                }
                Common.ShowDialog("????????????????????????????????????????????????.");
            } else if (command.equals("??????")) {
                BasicValue tmpParam4 = new BasicValue();
                List<HashMap<String, Object>> tmpList3 = Common.GetSelectObjects(PubVar._Map, -1, 2, false, tmpParam4);
                if (tmpParam4.getInt() <= 0 || tmpList3.size() <= 1) {
                    Common.ShowDialog("???????????????????????????????????????.");
                    return;
                }
                PolygonUnion_Dialog tempDialog5 = new PolygonUnion_Dialog();
                tempDialog5.SetPolygons(tmpList3);
                tempDialog5.ShowDialog();
            }
        } catch (Exception e) {
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
                    Selection tmpSel = PubVar._Map.getFeatureSelectionByLayerID(tempGeoLayer.getLayerID(), false);
                    if (tmpSel != null) {
                        tmpSel.RemoveBySysID(this.m_EditBaseDataObject.GetSYS_ID());
                    }
                    PubVar._Map.RefreshFast();
                }
            }
            StartEdit();
        } catch (Exception e) {
        }
    }

    private boolean SplitPolygonByLines(List<HashMap<String, Object>> polyHashList, List<HashMap<String, Object>> lineList) {
        boolean result = false;
        try {
            Poly tmpMainLineObj = new PolyDefault();
            Clip.GPC_EPSILON = 2.220446049250313E-16d;
            for (HashMap<String, Object> tmpHash : lineList) {
                tmpMainLineObj = Clip.union(tmpMainLineObj, ConvertPolylineToPolyObj((Polyline) tmpHash.get("Geometry")));
            }
            for (HashMap<String, Object> tmpHash2 : polyHashList) {
                if (Boolean.parseBoolean(String.valueOf(tmpHash2.get("Editable")))) {
                    Polygon tmpPoly = (Polygon) tmpHash2.get("Geometry");
                    Poly tmpSplitResult = Clip.difference(PolygonSplitByPoly_Dialog.ConvertPolygonToPolyObj(tmpPoly), tmpMainLineObj);
                    String tmpLayerID = String.valueOf(tmpHash2.get("LayerID"));
                    DataSet pDataset = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmpHash2.get("DataSource"))).GetDatasetByName(tmpLayerID);
                    if (pDataset != null) {
                        List<Polygon> tmpResultList = PolygonSplitByPoly_Dialog.ConvertPolyObjToPolygons(tmpSplitResult);
                        if (tmpResultList.size() > 0) {
                            List tmpCoordList = new ArrayList();
                            tmpCoordList.addAll(tmpResultList.get(0).GetAllCoordinateList());
                            tmpPoly.ResetData();
                            tmpPoly.SetAllCoordinateList(tmpCoordList);
                            tmpPoly.SetEdited(true);
                            pDataset.JustUpdateGeoMapIndex(tmpPoly);
                            pDataset.SaveGeoIndexToDB(tmpPoly);
                            if (tmpResultList.size() > 1) {
                                List<String> tmpFieldValues = pDataset.getGeometryFieldValues(tmpPoly.GetSYS_ID(), true);
                                int tmpJCount = tmpResultList.size();
                                for (int tmpJ = 1; tmpJ < tmpJCount; tmpJ++) {
                                    DataSet.SaveNewPolygon(tmpResultList.get(tmpJ), tmpFieldValues, pDataset, tmpLayerID);
                                }
                            }
                            pDataset.setEdited(true);
                            pDataset.RefreshTotalCount();
                            result = true;
                        }
                    }
                }
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
                    tmpPoly2.add(tempEndCoord.getX(), tempEndCoord.getY() + 1.0E-7d);
                    tmpPoly2.add(tempStartCoord.getX(), tempStartCoord.getY() + 1.0E-7d);
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
        double tmpDis = 1.0E-7d;
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

    private boolean ReShapeLine(Polygon polygon, List<Coordinate> list, BasicValue outMsg) {
        for (int tmpI = 0; tmpI < polygon.getBorderLine().GetAllCoordinateList().size(); tmpI++) {
        }
        return false;
    }

    public void ShowEditProp(String layerID, int SYS_ID) {
        FeatureAttribute_Dialog tempDialog = new FeatureAttribute_Dialog();
        tempDialog.SetLayerID(layerID);
        tempDialog.SetSYS_ID(SYS_ID);
        tempDialog.SetCallback(this.pCallback);
        tempDialog.ShowDialog();
    }
}
