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
import  com.xzy.forestSystem.gogisapi.Common.LoadCoordsFile_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.AddNextPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.AddPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.LineUnion_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.PolylineEditClass;
import  com.xzy.forestSystem.gogisapi.GPS.AddGPSPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.BaseDataObject;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Selection;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.SpatialRelation;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Toolbar_Polyline extends BaseToolbar {
    static final double SPLIT_BIAS = 1.0E-7d;
    private Drawable _GPSRecordBg01 = null;
    private Drawable _GPSRecordBg02 = null;
    private boolean _IsRecordByGPS = false;
    private Coordinate _LastCoord = null;
    private long _LastLocTime = 0;
    private BaseDataObject m_EditBaseDataObject = null;
    private GeoLayer m_EditGeoLayer = null;
    private String m_EditLayerID = null;
    private Button m_GPSBtn = null;
    private PolylineEditClass m_LineEditClass = null;
    private Toolbar_Layers m_Toolbar_Layers = null;
    private Handler myGPSRecordHandler = new Handler();
    private Runnable myRecordLineByGPSTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polyline.2
        @Override // java.lang.Runnable
        public void run() {
            if (Toolbar_Polyline.this._IsRecordByGPS) {
                try {
                    Coordinate tempCoord = PubVar._PubCommand.m_GpsLocation.getGPSCoordinate();
                    if (!tempCoord.Equal(Toolbar_Polyline.this._LastCoord)) {
                        boolean tempBool = false;
                        long tempTime = new Date().getTime();
                        if (Toolbar_Polyline.this._LastCoord == null) {
                            tempBool = true;
                        } else if (PubVar.GPSGatherIntervalTime == 0 && PubVar.GPSGatherIntervalDistance == 0.0d) {
                            tempBool = true;
                        } else if (PubVar.GPSGatherIntervalTime == 0) {
                            if (Toolbar_Polyline.this._LastCoord == null) {
                                tempBool = true;
                            } else if (PubVar.GPSGatherIntervalDistance == 0.0d) {
                                tempBool = true;
                            } else if (Common.GetTwoPointDistance(tempCoord, Toolbar_Polyline.this._LastCoord) >= PubVar.GPSGatherIntervalDistance) {
                                tempBool = true;
                            }
                        } else if (PubVar.GPSGatherIntervalDistance == 0.0d) {
                            if (tempTime - Toolbar_Polyline.this._LastLocTime >= PubVar.GPSGatherIntervalTime) {
                                tempBool = true;
                            }
                        } else if (tempTime - Toolbar_Polyline.this._LastLocTime >= PubVar.GPSGatherIntervalTime) {
                            if (Toolbar_Polyline.this._LastCoord == null) {
                                tempBool = true;
                            } else if (PubVar.GPSGatherIntervalDistance == 0.0d) {
                                tempBool = true;
                            } else if (Common.GetTwoPointDistance(tempCoord, Toolbar_Polyline.this._LastCoord) >= PubVar.GPSGatherIntervalDistance) {
                                tempBool = true;
                            }
                        }
                        if (tempBool && (PubVar.GPS_Gather_Accuracy == 0.0d || PubVar._PubCommand.m_GpsLocation.accuracy <= PubVar.GPS_Gather_Accuracy)) {
                            Toolbar_Polyline.this._LastCoord = tempCoord;
                            Toolbar_Polyline.this._LastLocTime = tempTime;
                            if (Toolbar_Polyline.this.m_LineEditClass != null) {
                                Toolbar_Polyline.this.m_LineEditClass.SetCurrentDrawType("GPS");
                                Toolbar_Polyline.this.m_LineEditClass.AddPoint(tempCoord);
                            }
                        }
                    }
                } catch (Exception e) {
                }
                Toolbar_Polyline.this.myGPSRecordHandler.postDelayed(Toolbar_Polyline.this.myRecordLineByGPSTask, 1000);
            }
        }
    };
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polyline.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
            List<Coordinate> tmpList;
            if (paramString.equals("??????????????????")) {
                Coordinate tempCoord = (Coordinate) pObject;
                if (!(tempCoord == null || Toolbar_Polyline.this.m_LineEditClass == null)) {
                    PubVar._PubCommand.ProcessCommand("??????_?????????");
                    Toolbar_Polyline.this.m_LineEditClass.SetCurrentDrawType("??????");
                    Toolbar_Polyline.this.m_LineEditClass.AddPoint(tempCoord);
                    Common.ShowToast("???????????????????????????.");
                }
            } else if (paramString.equals("??????GPS???????????????")) {
                Coordinate tempCoord2 = (Coordinate) pObject;
                if (!(tempCoord2 == null || Toolbar_Polyline.this.m_LineEditClass == null)) {
                    PubVar._PubCommand.ProcessCommand("??????_?????????");
                    Toolbar_Polyline.this.m_LineEditClass.SetCurrentDrawType("GPS");
                    Toolbar_Polyline.this.m_LineEditClass.AddPoint(tempCoord2);
                    Common.ShowToast("????????????GPS?????????.");
                }
            } else if (paramString.equals("?????????????????????")) {
                double[] tmpCoords = (double[]) pObject;
                if (tmpCoords != null && tmpCoords.length > 1 && Toolbar_Polyline.this.m_LineEditClass != null && Toolbar_Polyline.this.m_LineEditClass.getPointsCount() > 0) {
                    Coordinate tmpCoord = Toolbar_Polyline.this.m_LineEditClass.GetLastCoordinate().Clone();
                    tmpCoord.SetOffset(tmpCoords[0], tmpCoords[1]);
                    PubVar._PubCommand.ProcessCommand("??????_?????????");
                    Toolbar_Polyline.this.m_LineEditClass.SetCurrentDrawType("??????");
                    Toolbar_Polyline.this.m_LineEditClass.AddPoint(tmpCoord);
                    Common.ShowToast("???????????????????????????.");
                }
            } else if (paramString.equals("????????????????????????") && (tmpList = (List) pObject) != null && tmpList.size() > 0 && Toolbar_Polyline.this.m_LineEditClass != null) {
                PubVar._PubCommand.ProcessCommand("??????_?????????");
                Toolbar_Polyline.this.m_LineEditClass.SetCurrentDrawType("????????????");
                Coordinate tmpLastCoordinate = null;
                for (Coordinate tmpCoordinate : tmpList) {
                    Toolbar_Polyline.this.m_LineEditClass.AddPoint(tmpCoordinate);
                    tmpLastCoordinate = tmpCoordinate;
                }
                if (tmpLastCoordinate != null) {
                    Toolbar_Polyline.this.m_MapView.getMap().ZoomToCenter(tmpLastCoordinate);
                }
                Common.ShowToast("????????????????????????.");
            }
        }
    };

    public Toolbar_Polyline(Context context, MapView mapView) {
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
            this.m_EditGeoLayer = PubVar._Map.GetGeoLayerByName(this.m_EditLayerID);
            if (this.m_EditGeoLayer != null) {
                PubVar._MapView.AddMapTextObject("Toolbar_Polyline", "???????????????" + this.m_EditGeoLayer.getLayerName() + "???", ((float) (PubVar._MapView.getWidth() / 2)) + (10.0f * PubVar.ScaledDensity), 80.0f * PubVar.ScaledDensity, "#ffffffff");
                PubVar._MapView.invalidate();
            }
        } catch (Exception e) {
        }
    }

    public void SetEditLine(PolylineEditClass lineEditClass) {
        this.m_LineEditClass = lineEditClass;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void LoadToolBar(View view, int mainLinearLayoutID) {
        super.LoadToolBar(view, mainLinearLayoutID);
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_LineAddHand));
        this.buttonIDs.put("????????????", Integer.valueOf((int) R.id.bt_LineAddFreeHand));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_LineChangeDir));
        this.buttonIDs.put("GPS", Integer.valueOf((int) R.id.bt_LineAddByGPS));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_LineByCoord));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_Line_AddGPSPtn));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_LinePtnUndo));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_LinePtnRedo));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_LineAttribute));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_LineCancel));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_LineGenerate));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_lineEdit_MoveVertex));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_lineEdit_AddVertex));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_lineEdit_DelVertex));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_lineEdit_SplitLine));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_lineEdit_Change));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_lineEdit_Union));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolylineMove));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PolylineDelete));
        this.buttonIDs.put("?????????", Integer.valueOf((int) R.id.bt_LineByAddNextPoint));
        this.buttonIDs.put("????????????", Integer.valueOf((int) R.id.bt_Line_FromFile));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_Line_Paste));
        this.buttonIDs.put("????????????", Integer.valueOf((int) R.id.bt_ToolbarPolyline_AddInCenter));
        this.m_view.findViewById(R.id.bt_ToolbarPolyline_AddInCenter).setOnClickListener(this.buttonClickListener);
        this.m_GPSBtn = (Button) this.m_view.findViewById(R.id.bt_LineAddByGPS);
        this.m_view.setOnTouchListener(this.touchListener);
        this.m_baseLayout.setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_LineAddHand).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_LineAddFreeHand).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_LineChangeDir).setOnClickListener(this.buttonClickListener);
        this.m_GPSBtn.setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_LineByCoord).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_Line_AddGPSPtn).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_LinePtnUndo).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_LinePtnRedo).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_LineAttribute).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_LineCancel).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_LineGenerate).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_lineEdit_MoveVertex).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_lineEdit_AddVertex).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_lineEdit_DelVertex).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_lineEdit_SplitLine).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_lineEdit_Change).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_lineEdit_Union).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_lineEdit_Exit).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolylineMove).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PolylineDelete).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_LineByAddNextPoint).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_Line_FromFile).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_Line_Paste).setOnClickListener(this.buttonClickListener);
        this._GPSRecordBg01 = PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.gps_track_line_start48);
        this._GPSRecordBg01.setBounds(0, 0, this._GPSRecordBg01.getMinimumWidth(), this._GPSRecordBg01.getMinimumHeight());
        this._GPSRecordBg02 = PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.gps_track_line_stop48);
        this._GPSRecordBg02.setBounds(0, 0, this._GPSRecordBg02.getMinimumWidth(), this._GPSRecordBg02.getMinimumHeight());
        ((Button) this.m_view.findViewById(R.id.bt_LineAddHand)).setOnLongClickListener(new View.OnLongClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polyline.3
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View arg0) {
                Common.ShowYesNoDialog(Toolbar_Polyline.this.m_context, "?????????????????????????", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polyline.3.1
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        if (paramString.equals("YES")) {
                            Toolbar_Polyline.this.Hide();
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
        SetEditLine(PubVar._PubCommand.GetLineEdit());
        this.m_LineEditClass.IsDrawInMapCenter = true;
        StartEdit();
        this.m_LineEditClass.IsDrawInMapCenter = true;
        PubVar._PubCommand.getMapView().setActiveTools(MapTools.AddPolyline, this.m_LineEditClass, this.m_LineEditClass);
        PubVar._PubCommand.getMapView()._CurrentEditPaint = this.m_LineEditClass;
        PubVar._PubCommand.getMapView().invalidate();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Hide() {
        final GeoLayer tmpGeoLayer;
        this.m_LineEditClass.IsDrawInMapCenter = false;
        DoCommand("??????GPS??????");
        super.Hide();
        if (this.m_LineEditClass != null) {
            this.m_LineEditClass.Clear();
        }
        try {
            PubVar._MapView.RemoveMapTextObject("Toolbar_Polyline");
            if (!(this.m_EditLayerID == null || (tmpGeoLayer = PubVar._Map.GetGeoLayerByName(this.m_EditLayerID)) == null || !tmpGeoLayer.getDataset().getEdited())) {
                Common.ShowYesNoDialogWithAlert(PubVar.MainContext, "???????????????????????????.\r\n?????????????", true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polyline.4
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        if (paramString.equals("YES") && tmpGeoLayer != null) {
                            String[] tmpoutMsg = new String[1];
                            if (!tmpGeoLayer.getDataset().Save(tmpoutMsg)) {
                                Common.ShowDialog(tmpoutMsg[0]);
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
        }
        this.m_EditLayerID = null;
        PubVar._PubCommand.m_MapCompLayoutToolbar.clearEditFeatureayer();
        super.SaveConfigDB();
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
                localBuilder.setPositiveButton("??????", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polyline.5
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (PubVar._PubCommand.GetDeleteObject().Delete(Toolbar_Polyline.this.m_EditLayerID, tmpSYSIDList, true)) {
                            PubVar._MapView._Select.ClearAllSelection();
                            PubVar._Map.RefreshFast();
                        }
                    }
                });
                localBuilder.setNegativeButton("??????", (DialogInterface.OnClickListener) null);
                localBuilder.show();
            } else if (command.equals("?????????")) {
                if (getButtonIsSelected("??????")) {
                    PubVar._PubCommand.ProcessCommand("????????????");
                    Common.ShowToast("???????????????");
                    return;
                }
                DoCommand(command);
            } else if (!command.equals("???????????????")) {
                DoCommand(command);
            } else if (getButtonIsSelected("????????????")) {
                this.m_LineEditClass.SetHandFreeMode(false);
                PubVar._PubCommand.ProcessCommand("????????????");
                Common.ShowToast("?????????????????????");
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
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("???????????????");
                PubVar._PubCommand.ProcessCommand("??????_?????????");
                SetButtonSelectedStatus("??????", true);
                this.m_LineEditClass.SetIsEnabelDraw(true);
                this.m_LineEditClass.SetHandFreeMode(false);
            } else if (command.equals("???????????????")) {
                DoCommand("??????GPS??????");
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("???????????????");
                PubVar._PubCommand.ProcessCommand("??????_?????????");
                SetButtonSelectedStatus("????????????", true);
                this.m_LineEditClass.SetIsEnabelDraw(true);
                this.m_LineEditClass.SetHandFreeMode(true);
            } else if (command.equals("?????????")) {
                if (this.m_LineEditClass.ReversCoords()) {
                    PubVar._Map.ZoomToCenter(this.m_LineEditClass.GetLastCoordinate());
                }
            } else if (command.equals("GPS??????")) {
                if (!PubVar._PubCommand.m_GpsLocation.isOpen) {
                    Common.ShowDialog("GPS???????????????,????????????GPS??????.");
                }
                if (!PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                    Common.ShowDialog("GPS?????????,?????????...");
                }
                PubVar._PubCommand.ProcessCommand("??????_?????????");
                this.m_GPSBtn.setTag("??????GPS??????");
                this.m_GPSBtn.setCompoundDrawables(null, this._GPSRecordBg02, null, null);
                this.m_LineEditClass.SetIsEnabelDraw(false);
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
                if (this.m_LineEditClass == null || this.m_LineEditClass.getPointsCount() <= 0) {
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
                String[] tempOutMsg = new String[1];
                if (!this.m_LineEditClass.UndoLastPoint(tempOutMsg)) {
                    Common.ShowToast(tempOutMsg[0]);
                }
            } else if (command.equals("??????")) {
                String[] tempOutMsg2 = new String[1];
                if (!this.m_LineEditClass.RedoLastPoint(tempOutMsg2)) {
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
                if (this.m_LineEditClass.getPointsCount() > 0) {
                    Common.ShowYesNoDialog(this.m_context, "??????????????????????????????????", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polyline.6
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (paramString.equals("YES")) {
                                Toolbar_Polyline.this.StartEdit();
                            }
                        }
                    });
                }
            } else if (command.equals("???????????????")) {
                if (this.m_LineEditClass.getPointsCount() > 1) {
                    FinishEdit();
                } else {
                    Common.ShowDialog("??????????????????????????????2???.");
                }
            } else if (command.equals("????????????")) {
                PubVar._PubCommand.ShowToolbar("????????????????????????");
            } else if (command.equals("??????")) {
                if (Common.GetSelectObjectsCount(PubVar._Map, -1, 1, false) == 1) {
                    PubVar._PubCommand.ProcessCommand("????????????");
                    SetButtonSelectedStatus("??????", true);
                    return;
                }
                Common.ShowDialog("???????????????????????????????????????.");
            } else if (command.equals("??????")) {
                if (Common.GetSelectObjectsCount(PubVar._Map, -1, 1, false) == 1) {
                    PubVar._PubCommand.ProcessCommand("????????????");
                    SetButtonSelectedStatus("??????", true);
                    return;
                }
                Common.ShowDialog("???????????????????????????????????????.");
            } else if (command.equals("??????")) {
                if (Common.GetSelectObjectsCount(PubVar._Map, -1, 1, false) == 1) {
                    PubVar._PubCommand.ProcessCommand("????????????");
                    SetButtonSelectedStatus("??????", true);
                    return;
                }
                Common.ShowDialog("???????????????????????????????????????.");
            } else if (command.equals("?????????")) {
                BasicValue tmpParam = new BasicValue();
                List<HashMap<String, Object>> tmpList = Common.GetSelectObjectsInLayer(PubVar._Map, this.m_EditLayerID, false, tmpParam);
                if (tmpParam.getInt() <= 0 || tmpList.size() != 1) {
                    Common.ShowDialog("??????????????????????????????????????????????????????.");
                } else if (PubVar._PubCommand.GetLineEdit().getPointsCount() > 1) {
                    HashMap<String, Object> tmphash = tmpList.get(0);
                    Polyline tmpMainPolyline = (Polyline) tmphash.get("Geometry");
                    List<Polyline> tmpSplitResult = SplitPolyline(tmpMainPolyline.GetAllCoordinateList(), PubVar._PubCommand.GetLineEdit().GetAllCoordinates());
                    if (tmpSplitResult.size() > 0) {
                        tmpMainPolyline.ResetData();
                        List<Coordinate> tmpCoordList01 = new ArrayList<>();
                        tmpCoordList01.addAll(tmpSplitResult.get(0).GetAllCoordinateList());
                        tmpMainPolyline.SetAllCoordinateList(tmpCoordList01);
                        tmpMainPolyline.SetEdited(true);
                        String tmpLayerID = String.valueOf(tmphash.get("LayerID"));
                        DataSet pDataset = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmphash.get("DataSource"))).GetDatasetByName(tmpLayerID);
                        if (pDataset != null) {
                            pDataset.JustUpdateGeoMapIndex(tmpMainPolyline);
                            pDataset.SaveGeoIndexToDB(tmpMainPolyline);
                            List<String> tmpFieldValues = pDataset.getGeometryFieldValues(tmpMainPolyline.GetSYS_ID(), true);
                            int tmpCount = tmpSplitResult.size();
                            for (int i = 1; i < tmpCount; i++) {
                                DataSet.SaveNewPolyline(tmpSplitResult.get(i), tmpFieldValues, pDataset, tmpLayerID);
                            }
                            pDataset.setEdited(true);
                            pDataset.RefreshTotalCount();
                        }
                        Common.ShowToast("?????????????????????.");
                        PubVar._PubCommand.ProcessCommand("??????_??????????????????");
                        PubVar._Map.Refresh();
                        return;
                    }
                    Common.ShowToast("???????????????????????????????????????.");
                } else {
                    Common.ShowDialog("????????????????????????.");
                }
            } else if (command.equals("??????")) {
                BasicValue tmpParam2 = new BasicValue();
                List<HashMap<String, Object>> tmpList2 = Common.GetSelectObjectsInLayer(PubVar._Map, this.m_EditLayerID, false, tmpParam2);
                if (tmpParam2.getInt() <= 0 || tmpList2.size() <= 0) {
                    Common.ShowDialog("????????????????????????????????????????????????.");
                    return;
                }
                for (HashMap<String, Object> tmpHash01 : tmpList2) {
                    DataSet pDataset2 = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmpHash01.get("DataSource"))).GetDatasetByName(String.valueOf(tmpHash01.get("LayerID")));
                    if (pDataset2 != null) {
                        Polyline tmpGeometry = (Polyline) tmpHash01.get("Geometry");
                        Collections.reverse(tmpGeometry.GetAllCoordinateList());
                        if (!tmpGeometry.IsSimple()) {
                            int tmpEnd = tmpGeometry.GetAllCoordinateList().size();
                            List<Integer> tmpList02 = new ArrayList<>();
                            ListIterator<Integer> tmpIter2 = tmpGeometry.getPartIndex().listIterator();
                            while (tmpIter2.hasNext()) {
                                tmpIter2.next();
                            }
                            int tid = 0;
                            tmpList02.add(0);
                            while (tmpIter2.hasPrevious()) {
                                int tmpStart = tmpIter2.previous().intValue();
                                tid += tmpEnd - tmpStart;
                                tmpList02.add(Integer.valueOf(tid));
                                tmpEnd = tmpStart;
                            }
                            tmpGeometry.setPartIndex(tmpList02);
                        }
                        tmpGeometry.SetEdited(true);
                        pDataset2.setEdited(true);
                    }
                }
                PubVar._Map.RefreshFast();
                Common.ShowToast("???????????????.");
            } else if (command.equals("??????")) {
                BasicValue tmpParam3 = new BasicValue();
                List<HashMap<String, Object>> tmpList3 = Common.GetSelectObjectsInLayer(PubVar._Map, this.m_EditLayerID, false, tmpParam3);
                if (tmpParam3.getInt() <= 0 || tmpList3.size() <= 1) {
                    Common.ShowDialog("????????????????????????????????????????????????(?????????????????????1).");
                    return;
                }
                LineUnion_Dialog tempDialog4 = new LineUnion_Dialog();
                tempDialog4.SetPolylines(tmpList3);
                tempDialog4.ShowDialog();
            } else if (command.equals("???????????????????????????")) {
                Coordinate tmpCoord = PubVar._MapView.getMap().getCenter().Clone();
                Coordinate localCoordinate2 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tmpCoord, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                tmpCoord.setGeoX(localCoordinate2.getGeoX());
                tmpCoord.setGeoY(localCoordinate2.getGeoY());
                this.m_LineEditClass.AddPoint(tmpCoord);
                Common.ShowToast("??????????????????");
            } else if (command.equals("??????????????????")) {
                LoadCoordsFile_Dialog tmpDialog2 = new LoadCoordsFile_Dialog();
                tmpDialog2.SetCallback(this.pCallback);
                tmpDialog2.ShowDialog();
            } else if (command.equals("????????????")) {
                try {
                    Toolbar_Select.PasteGeometrys(this.m_context, this.m_EditGeoLayer, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polyline.7
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command2, Object pObject) {
                            Object[] tmpObjs;
                            List<Integer> tmpPasteSysIDList;
                            if (command2.equals("??????????????????") && pObject != null && (tmpObjs = (Object[]) pObject) != null && tmpObjs.length > 1 && Integer.parseInt(String.valueOf(tmpObjs[0])) == 1 && (tmpPasteSysIDList = (List) tmpObjs[1]) != null && tmpPasteSysIDList.size() > 0) {
                                Toolbar_Polyline.this.ShowEditProp(Toolbar_Polyline.this.m_EditGeoLayer.getLayerID(), tmpPasteSysIDList.get(0).intValue());
                            }
                        }
                    });
                } catch (Exception e) {
                    Common.ShowToast(e.getLocalizedMessage());
                }
            }
        } catch (Exception e2) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void StartEdit() {
        this.m_LineEditClass.Clear();
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
                Polyline tempGeo = new Polyline();
                List<Coordinate> tempCoords = new ArrayList<>();
                tempCoords.addAll(this.m_LineEditClass.GetAllCoordinates());
                tempGeo.SetAllCoordinateList(tempCoords);
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
            this.m_LineEditClass.Clear();
            this.m_EditBaseDataObject = new BaseDataObject();
            this.m_EditBaseDataObject.SetBaseObjectRelateLayerID(this.m_EditLayerID);
            this.m_EditBaseDataObject.SetSYS_TYPE("?????????");
        } catch (Exception e) {
        }
        PubVar._PubCommand.ProcessCommand("????????????");
    }

    public static List<Polyline> SplitPolyline(List<Coordinate> mainLineCoords, List<Coordinate> splitLineCoords) {
        List<Polyline> result = new ArrayList<>();
        Iterator<Coordinate> tmpIter01 = mainLineCoords.iterator();
        Coordinate tmpCoord01 = tmpIter01.next();
        double tmpX1 = tmpCoord01.getX();
        double tmpY1 = tmpCoord01.getY();
        double[] tmpInterCoord = new double[2];
        List<Coordinate> tmpCoordsList = new ArrayList<>();
        tmpCoordsList.add(tmpCoord01.Clone());
        while (tmpIter01.hasNext()) {
            Coordinate tmpCoord012 = tmpIter01.next();
            double tmpX2 = tmpCoord012.getX();
            double tmpY2 = tmpCoord012.getY();
            if (SpatialRelation.CalLineSegIntersectPoint(tmpX1, tmpY1, tmpX2, tmpY2, splitLineCoords, tmpInterCoord)) {
                tmpCoordsList.add(new Coordinate(tmpInterCoord[0], tmpInterCoord[1]));
                if (tmpCoordsList.size() > 1) {
                    Polyline tmpPolyline = new Polyline();
                    tmpPolyline.SetAllCoordinateList(tmpCoordsList);
                    tmpPolyline.CalEnvelope();
                    result.add(tmpPolyline);
                }
                tmpCoordsList = new ArrayList<>();
                tmpCoordsList.add(new Coordinate(tmpInterCoord[0], tmpInterCoord[1]));
            } else {
                tmpCoordsList.add(tmpCoord012.Clone());
            }
            tmpX1 = tmpX2;
            tmpY1 = tmpY2;
        }
        if (tmpCoordsList.size() > 1) {
            Polyline tmpPolyline2 = new Polyline();
            tmpPolyline2.SetAllCoordinateList(tmpCoordsList);
            tmpPolyline2.CalEnvelope();
            result.add(tmpPolyline2);
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
}
