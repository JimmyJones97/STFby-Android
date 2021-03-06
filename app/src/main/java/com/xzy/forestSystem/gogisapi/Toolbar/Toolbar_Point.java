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
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.LoadCoordsFile_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.AddNextPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.AddPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.PointEditClass;
import  com.xzy.forestSystem.gogisapi.GPS.AddGPSPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.BaseDataObject;
import  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Selection;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Point;
import com.stczh.gzforestSystem.R;
import java.util.Date;
import java.util.List;

public class Toolbar_Point extends BaseToolbar {
    private Drawable _GPSRecordBg01 = null;
    private Drawable _GPSRecordBg02 = null;
    private boolean _IsRecordByGPS = false;
    private Coordinate _LastAddCoordinate = null;
    private Coordinate _LastCoord = null;
    private long _LastLocTime = 0;
    private GeoLayer m_EditGeoLayer = null;
    private String m_EditLayerID = null;
    private Button m_GPSBtn = null;
    private PointEditClass m_PointEdit = null;
    private Toolbar_Layers m_Toolbar_Layers = null;
    private Handler myGPSRecordHandler = new Handler();
    private Runnable myRecordLineByGPSTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Point.2
        @Override // java.lang.Runnable
        public void run() {
            if (Toolbar_Point.this._IsRecordByGPS) {
                try {
                    Coordinate tempCoord = PubVar._PubCommand.m_GpsLocation.getGPSCoordinate();
                    if (!tempCoord.Equal(Toolbar_Point.this._LastCoord)) {
                        boolean tempBool = false;
                        long tempTime = new Date().getTime();
                        if (Toolbar_Point.this._LastCoord == null) {
                            tempBool = true;
                        } else if (PubVar.GPSGatherIntervalTime == 0 && PubVar.GPSGatherIntervalDistance == 0.0d) {
                            tempBool = true;
                        } else if (PubVar.GPSGatherIntervalTime == 0) {
                            if (Toolbar_Point.this._LastCoord == null) {
                                tempBool = true;
                            } else if (PubVar.GPSGatherIntervalDistance == 0.0d) {
                                tempBool = true;
                            } else if (Common.GetTwoPointDistance(tempCoord, Toolbar_Point.this._LastCoord) >= PubVar.GPSGatherIntervalDistance) {
                                tempBool = true;
                            }
                        } else if (PubVar.GPSGatherIntervalDistance == 0.0d) {
                            if (tempTime - Toolbar_Point.this._LastLocTime >= PubVar.GPSGatherIntervalTime) {
                                tempBool = true;
                            }
                        } else if (tempTime - Toolbar_Point.this._LastLocTime >= PubVar.GPSGatherIntervalTime) {
                            if (Toolbar_Point.this._LastCoord == null) {
                                tempBool = true;
                            } else if (PubVar.GPSGatherIntervalDistance == 0.0d) {
                                tempBool = true;
                            } else if (Common.GetTwoPointDistance(tempCoord, Toolbar_Point.this._LastCoord) >= PubVar.GPSGatherIntervalDistance) {
                                tempBool = true;
                            }
                        }
                        if (tempBool && (PubVar.GPS_Gather_Accuracy == 0.0d || PubVar._PubCommand.m_GpsLocation.accuracy <= PubVar.GPS_Gather_Accuracy)) {
                            Toolbar_Point.this._LastCoord = tempCoord;
                            Toolbar_Point.this._LastLocTime = tempTime;
                            Toolbar_Point.this.AddPoint(tempCoord, "GPS??????");
                        }
                    }
                } catch (Exception e) {
                }
                Toolbar_Point.this.myGPSRecordHandler.postDelayed(Toolbar_Point.this.myRecordLineByGPSTask, 1000);
            }
        }
    };
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Point.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
            GeoLayer tempGeoLayer;
            List<Coordinate> tmpList;
            double[] tmpCoords;
            if (paramString.equals("??????GPS???????????????")) {
                Coordinate tempCoord = (Coordinate) pObject;
                if (tempCoord != null) {
                    Toolbar_Point.this.AddPoint(tempCoord, "GPS??????");
                }
            } else if (paramString.equals("??????????????????")) {
                Coordinate tempCoord2 = (Coordinate) pObject;
                if (tempCoord2 != null) {
                    Toolbar_Point.this.AddPoint(tempCoord2, "????????????");
                }
            } else if (paramString.equals("??????????????????") || paramString.equals("????????????????????????")) {
                int tempIndex = Integer.parseInt(pObject.toString());
                if (tempIndex >= 0 && (tempGeoLayer = PubVar._Map.GetGeoLayerByName(Toolbar_Point.this.m_EditLayerID)) != null) {
                    Selection tmpSel = PubVar._Map.getFeatureSelectionByLayerID(tempGeoLayer.getLayerID(), false);
                    if (tmpSel != null) {
                        tmpSel.RemoveBySysID(tempIndex);
                    }
                    PubVar._Map.RefreshFast();
                }
            } else if (paramString.equals("?????????????????????")) {
                if (!(Toolbar_Point.this._LastAddCoordinate == null || (tmpCoords = (double[]) pObject) == null || tmpCoords.length <= 1)) {
                    Coordinate tmpCoord = Toolbar_Point.this._LastAddCoordinate.Clone();
                    tmpCoord.SetOffset(tmpCoords[0], tmpCoords[1]);
                    Toolbar_Point.this.AddPoint(tmpCoord, "????????????");
                }
            } else if (paramString.equals("????????????????????????") && (tmpList = (List) pObject) != null && tmpList.size() > 0) {
                Coordinate tmpLastCoordinate = null;
                for (Coordinate tmpCoordinate : tmpList) {
                    Toolbar_Point.this.AddPoint(tmpCoordinate, "????????????", false);
                    tmpLastCoordinate = tmpCoordinate;
                }
                if (tmpLastCoordinate != null) {
                    Toolbar_Point.this.m_MapView.getMap().ZoomToCenter(tmpLastCoordinate);
                }
                Common.ShowToast("????????????????????????.");
            }
        }
    };

    public Toolbar_Point(Context context, MapView mapView) {
        super(context, mapView);
        this.m_ToolbarName = "??????????????????";
        this.m_IsAllowedClose = false;
    }

    public void SetEditPoint(PointEditClass editClass) {
        this.m_PointEdit = editClass;
    }

    public void SetToolbar_Layers(Toolbar_Layers toolbar) {
        this.m_Toolbar_Layers = toolbar;
    }

    public void SetEditLayerID(String layerID) {
        try {
            this.m_EditLayerID = layerID;
            this.m_EditGeoLayer = PubVar._Map.GetGeoLayerByName(this.m_EditLayerID);
            if (this.m_EditGeoLayer != null) {
                PubVar._MapView.AddMapTextObject("Toolbar_Point", "???????????????" + this.m_EditGeoLayer.getLayerName() + "???", ((float) (PubVar._MapView.getWidth() / 2)) + (10.0f * PubVar.ScaledDensity), 80.0f * PubVar.ScaledDensity, "#ffffffff");
                PubVar._MapView.invalidate();
            }
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void LoadToolBar(View view) {
        super.LoadToolBar(view);
        this.buttonIDs.put("?????????", Integer.valueOf((int) R.id.bt_AddPointHand));
        this.buttonIDs.put("GPS???", Integer.valueOf((int) R.id.bt_AddPointByGPS));
        this.buttonIDs.put("????????????", Integer.valueOf((int) R.id.bt_AddPointByCoord));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PointMove));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_PointDelete));
        this.buttonIDs.put("?????????", Integer.valueOf((int) R.id.bt_PointByAddNextPoint));
        this.buttonIDs.put("GPS??????", Integer.valueOf((int) R.id.bt_PointAddByGPS));
        this.buttonIDs.put("????????????", Integer.valueOf((int) R.id.bt_Point_Generate));
        this.buttonIDs.put("????????????", Integer.valueOf((int) R.id.bt_Point_FromFile));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.bt_Point_Paste));
        this.m_view.setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_AddPointHand).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_AddPointByGPS).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_AddPointByCoord).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PointMove).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PointDelete).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_AddPoint_Exit).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PointByAddNextPoint).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_Point_FromFile).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_PointAddByGPS).setOnClickListener(this.buttonClickListener);
        this.m_GPSBtn = (Button) this.m_view.findViewById(R.id.bt_PointAddByGPS);
        this.m_view.findViewById(R.id.bt_Point_Generate).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.bt_Point_Paste).setOnClickListener(this.buttonClickListener);
        this._GPSRecordBg01 = PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.poly_gps_start48);
        this._GPSRecordBg01.setBounds(0, 0, this._GPSRecordBg01.getMinimumWidth(), this._GPSRecordBg01.getMinimumHeight());
        this._GPSRecordBg02 = PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.poly_gps_stop48);
        this._GPSRecordBg02.setBounds(0, 0, this._GPSRecordBg02.getMinimumWidth(), this._GPSRecordBg02.getMinimumHeight());
        ((Button) this.m_view.findViewById(R.id.bt_AddPointHand)).setOnLongClickListener(new View.OnLongClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Point.3
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View arg0) {
                Common.ShowYesNoDialog(Toolbar_Point.this.m_context, "?????????????????????????", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Point.3.1
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        if (paramString.equals("YES")) {
                            Toolbar_Point.this.Hide();
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
        SetEditPoint(PubVar._PubCommand.GetPointEdit());
        PubVar._PubCommand.UpdateAllToolbarStatusNotSelected(null);
        PubVar._PubCommand.ProcessCommand("??????_?????????");
        SetButtonSelectedStatus("?????????", true);
        PubVar._PubCommand.GetPointEdit().IsDrawInMapCenter = true;
        PubVar._PubCommand.getMapView().setActiveTools(MapTools.AddPoint, this.m_PointEdit, this.m_PointEdit);
        PubVar._PubCommand.getMapView()._CurrentEditPaint = this.m_PointEdit;
        PubVar._PubCommand.getMapView().invalidate();
        DoCommand("????????????");
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Hide() {
        final GeoLayer tmpGeoLayer;
        super.Hide();
        try {
            PubVar._PubCommand.GetPointEdit().IsDrawInMapCenter = false;
            PubVar._MapView.RemoveMapTextObject("Toolbar_Point");
            PubVar._PubCommand.getMapView().invalidate();
            if (!(this.m_EditLayerID == null || (tmpGeoLayer = PubVar._Map.GetGeoLayerByName(this.m_EditLayerID)) == null || !tmpGeoLayer.getDataset().getEdited())) {
                Common.ShowYesNoDialogWithAlert(PubVar.MainContext, "???????????????????????????.\r\n?????????????", true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Point.4
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
                localBuilder.setPositiveButton("??????", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Point.5
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (PubVar._PubCommand.GetDeleteObject().Delete(Toolbar_Point.this.m_EditLayerID, tmpSYSIDList, true)) {
                            PubVar._MapView._Select.ClearAllSelection();
                            PubVar._Map.RefreshFast();
                        }
                    }
                });
                localBuilder.setNegativeButton("??????", (DialogInterface.OnClickListener) null);
                localBuilder.show();
            } else if (!command.equals("?????????")) {
                DoCommand(command);
            } else if (getButtonIsSelected("?????????")) {
                PubVar._PubCommand.ProcessCommand("????????????");
                Common.ShowToast("???????????????");
            } else {
                DoCommand(command);
            }
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(String command) {
        if (command.equals("?????????")) {
            PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("???????????????");
            SetButtonSelectedStatus("?????????", true);
            PubVar._PubCommand.ProcessCommand("??????_?????????");
        } else if (command.equals("GPS??????")) {
            AddGPSPoint_Dialog tempDialog = new AddGPSPoint_Dialog();
            tempDialog.SetCallback(this.pCallback);
            tempDialog.ShowDialog();
        } else if (command.equals("GPS??????")) {
            if (!PubVar._PubCommand.m_GpsLocation.isOpen) {
                Common.ShowDialog("GPS???????????????,????????????GPS??????.");
            }
            if (!PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                Common.ShowDialog("GPS?????????,?????????...");
            }
            this.m_GPSBtn.setTag("??????GPS??????");
            this.m_GPSBtn.setCompoundDrawables(null, this._GPSRecordBg02, null, null);
            PubVar._PubCommand.ProcessCommand("????????????");
            PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("???????????????");
            SetButtonSelectedStatus("GPS??????", true);
            SetButtonsEnable("GPS??????", false);
            this._IsRecordByGPS = true;
            this.myGPSRecordHandler.post(this.myRecordLineByGPSTask);
        } else if (command.equals("??????GPS??????")) {
            this._IsRecordByGPS = false;
            this._LastCoord = null;
            this._LastLocTime = 0;
            this.m_GPSBtn.setTag("GPS??????");
            this.m_GPSBtn.setCompoundDrawables(null, this._GPSRecordBg01, null, null);
            SetButtonSelectedStatus("GPS??????", false);
            SetButtonsEnable(null, true);
        } else if (command.equals("????????????")) {
            AddPoint_Dialog tempDialog2 = new AddPoint_Dialog();
            tempDialog2.SetCallback(this.pCallback);
            tempDialog2.ShowDialog();
        } else if (command.equals("???????????????")) {
            if (this._LastAddCoordinate != null) {
                AddNextPoint_Dialog tmpDialog = new AddNextPoint_Dialog();
                tmpDialog.SetCallback(this.pCallback);
                tmpDialog.ShowDialog();
                return;
            }
            Common.ShowDialog("??????????????????????????????????????????.");
        } else if (command.equals("???????????????????????????")) {
            Coordinate tmpCoord = PubVar._MapView.getMap().getCenter().Clone();
            Coordinate localCoordinate2 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tmpCoord, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
            tmpCoord.setGeoX(localCoordinate2.getGeoX());
            tmpCoord.setGeoY(localCoordinate2.getGeoY());
            AddPoint(tmpCoord, "??????");
        } else if (command.equals("??????????????????")) {
            LoadCoordsFile_Dialog tmpDialog2 = new LoadCoordsFile_Dialog();
            tmpDialog2.SetCallback(this.pCallback);
            tmpDialog2.ShowDialog();
        } else if (command.equals("????????????")) {
            try {
                Toolbar_Select.PasteGeometrys(this.m_context, this.m_EditGeoLayer, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Point.6
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command2, Object pObject) {
                        Object[] tmpObjs;
                        List<Integer> tmpPasteSysIDList;
                        if (command2.equals("??????????????????") && pObject != null && (tmpObjs = (Object[]) pObject) != null && tmpObjs.length > 1 && Integer.parseInt(String.valueOf(tmpObjs[0])) == 1 && (tmpPasteSysIDList = (List) tmpObjs[1]) != null && tmpPasteSysIDList.size() > 0) {
                            Toolbar_Point.this.ShowEditProp(Toolbar_Point.this.m_EditGeoLayer.getLayerID(), tmpPasteSysIDList.get(0).intValue());
                        }
                    }
                });
            } catch (Exception e) {
                Common.ShowToast(e.getLocalizedMessage());
            }
        }
    }

    public void AddPoint(Coordinate coordinate, String gatherType) {
        this._LastAddCoordinate = coordinate;
        if (this.m_EditLayerID != null && !this.m_EditLayerID.equals("")) {
            Point localPoint = new Point(coordinate);
            BaseDataObject localv1_BaseDataObject = new BaseDataObject();
            localv1_BaseDataObject.SetBaseObjectRelateLayerID(this.m_EditLayerID);
            localv1_BaseDataObject.SetSYS_TYPE(gatherType);
            int i = localv1_BaseDataObject.SaveNewGeoToDb(localPoint);
            if (i >= 0) {
                localv1_BaseDataObject.SetSYS_ID(i);
                this.m_EditGeoLayer.getDataset().RefreshTotalCount();
                if (PubVar.Edited_ShowAttribute_Bool && !this._IsRecordByGPS) {
                    ShowEditProp(this.m_EditLayerID, i);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void AddPoint(Coordinate coordinate, String gatherType, boolean needShowPropEdit) {
        this._LastAddCoordinate = coordinate;
        if (this.m_EditLayerID != null && !this.m_EditLayerID.equals("")) {
            Point localPoint = new Point(coordinate);
            BaseDataObject localv1_BaseDataObject = new BaseDataObject();
            localv1_BaseDataObject.SetBaseObjectRelateLayerID(this.m_EditLayerID);
            localv1_BaseDataObject.SetSYS_TYPE(gatherType);
            int i = localv1_BaseDataObject.SaveNewGeoToDb(localPoint);
            if (i >= 0) {
                this.m_EditGeoLayer.getDataset().RefreshTotalCount();
                localv1_BaseDataObject.SetSYS_ID(i);
                if (needShowPropEdit) {
                    ShowEditProp(this.m_EditLayerID, i);
                }
            }
        }
    }

    public void ShowEditProp(String layerID, int SYS_ID) {
        FeatureAttribute_Dialog tempDialog = new FeatureAttribute_Dialog();
        tempDialog.SetLayerID(layerID);
        tempDialog.SetSYS_ID(SYS_ID);
        tempDialog.SetCallback(this.pCallback);
        tempDialog.ShowDialog();
    }
}
