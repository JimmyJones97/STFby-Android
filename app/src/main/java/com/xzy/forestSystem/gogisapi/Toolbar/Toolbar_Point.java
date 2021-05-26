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
                            Toolbar_Point.this.AddPoint(tempCoord, "GPS拐点");
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
            if (paramString.equals("添加GPS采样点返回")) {
                Coordinate tempCoord = (Coordinate) pObject;
                if (tempCoord != null) {
                    Toolbar_Point.this.AddPoint(tempCoord, "GPS采样");
                }
            } else if (paramString.equals("坐标输入返回")) {
                Coordinate tempCoord2 = (Coordinate) pObject;
                if (tempCoord2 != null) {
                    Toolbar_Point.this.AddPoint(tempCoord2, "坐标输入");
                }
            } else if (paramString.equals("编辑属性返回") || paramString.equals("编辑属性窗体返回")) {
                int tempIndex = Integer.parseInt(pObject.toString());
                if (tempIndex >= 0 && (tempGeoLayer = PubVar._Map.GetGeoLayerByName(Toolbar_Point.this.m_EditLayerID)) != null) {
                    Selection tmpSel = PubVar._Map.getFeatureSelectionByLayerID(tempGeoLayer.getLayerID(), false);
                    if (tmpSel != null) {
                        tmpSel.RemoveBySysID(tempIndex);
                    }
                    PubVar._Map.RefreshFast();
                }
            } else if (paramString.equals("输入下一点返回")) {
                if (!(Toolbar_Point.this._LastAddCoordinate == null || (tmpCoords = (double[]) pObject) == null || tmpCoords.length <= 1)) {
                    Coordinate tmpCoord = Toolbar_Point.this._LastAddCoordinate.Clone();
                    tmpCoord.SetOffset(tmpCoords[0], tmpCoords[1]);
                    Toolbar_Point.this.AddPoint(tmpCoord, "坐标输入");
                }
            } else if (paramString.equals("导入节点数据返回") && (tmpList = (List) pObject) != null && tmpList.size() > 0) {
                Coordinate tmpLastCoordinate = null;
                for (Coordinate tmpCoordinate : tmpList) {
                    Toolbar_Point.this.AddPoint(tmpCoordinate, "坐标导入", false);
                    tmpLastCoordinate = tmpCoordinate;
                }
                if (tmpLastCoordinate != null) {
                    Toolbar_Point.this.m_MapView.getMap().ZoomToCenter(tmpLastCoordinate);
                }
                Common.ShowToast("导入坐标数据成功.");
            }
        }
    };

    public Toolbar_Point(Context context, MapView mapView) {
        super(context, mapView);
        this.m_ToolbarName = "点编辑工具栏";
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
                PubVar._MapView.AddMapTextObject("Toolbar_Point", "编辑图层【" + this.m_EditGeoLayer.getLayerName() + "】", ((float) (PubVar._MapView.getWidth() / 2)) + (10.0f * PubVar.ScaledDensity), 80.0f * PubVar.ScaledDensity, "#ffffffff");
                PubVar._MapView.invalidate();
            }
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void LoadToolBar(View view) {
        super.LoadToolBar(view);
        this.buttonIDs.put("手绘点", Integer.valueOf((int) R.id.bt_AddPointHand));
        this.buttonIDs.put("GPS点", Integer.valueOf((int) R.id.bt_AddPointByGPS));
        this.buttonIDs.put("坐标绘点", Integer.valueOf((int) R.id.bt_AddPointByCoord));
        this.buttonIDs.put("移动", Integer.valueOf((int) R.id.bt_PointMove));
        this.buttonIDs.put("删除", Integer.valueOf((int) R.id.bt_PointDelete));
        this.buttonIDs.put("偏移点", Integer.valueOf((int) R.id.bt_PointByAddNextPoint));
        this.buttonIDs.put("GPS拐点", Integer.valueOf((int) R.id.bt_PointAddByGPS));
        this.buttonIDs.put("地图中心", Integer.valueOf((int) R.id.bt_Point_Generate));
        this.buttonIDs.put("导入文件", Integer.valueOf((int) R.id.bt_Point_FromFile));
        this.buttonIDs.put("粘贴", Integer.valueOf((int) R.id.bt_Point_Paste));
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
                Common.ShowYesNoDialog(Toolbar_Point.this.m_context, "是否关闭该工具栏?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Point.3.1
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
        PubVar._PubCommand.ProcessCommand("采集_手绘点");
        SetButtonSelectedStatus("手绘点", true);
        PubVar._PubCommand.GetPointEdit().IsDrawInMapCenter = true;
        PubVar._PubCommand.getMapView().setActiveTools(MapTools.AddPoint, this.m_PointEdit, this.m_PointEdit);
        PubVar._PubCommand.getMapView()._CurrentEditPaint = this.m_PointEdit;
        PubVar._PubCommand.getMapView().invalidate();
        DoCommand("刷新图层");
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
                Common.ShowYesNoDialogWithAlert(PubVar.MainContext, "图层中数据没有保存.\r\n是否保存?", true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Point.4
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
            if (command.equals("关闭工具")) {
                if (this.m_Toolbar_Layers != null) {
                    this.m_Toolbar_Layers.DoCommand("直接停止编辑");
                }
                Hide();
            } else if (command.equals("移动选择")) {
                if (view.isSelected()) {
                    SetButtonSelectedStatus("移动", false);
                    PubVar._PubCommand.ProcessCommand("无工具");
                    return;
                }
                int tempCount = 0;
                Selection tmpSel = PubVar._MapView.getMap().getFeatureSelectionByLayerID(this.m_EditLayerID, false);
                if (tmpSel != null) {
                    tempCount = tmpSel.getCount();
                }
                if (tempCount == 0) {
                    Common.ShowDialog("没有选择该图层中的任何对象.\r\n请先选择需要移动的对象.");
                    return;
                }
                PubVar._MapView.setActiveTool(MapTools.MoveObject);
                PubVar._MapView.getMoveFeature().SetMoveLayer(this.m_EditLayerID);
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("选择工具栏;图层工具栏");
                SetButtonSelectedStatus("移动", true);
            } else if (command.equals("删除选择")) {
                int tempCount2 = 0;
                Selection tmpSel2 = PubVar._MapView.getMap().getFeatureSelectionByLayerID(this.m_EditLayerID, false);
                if (tmpSel2 != null) {
                    tempCount2 = tmpSel2.getCount();
                }
                final List<String> tmpSYSIDList = tmpSel2.getSYSIDList();
                if (tempCount2 == 0) {
                    Common.ShowDialog("没有选择该图层中的任何对象.");
                    return;
                }
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.m_view.getContext());
                localBuilder.setIcon(R.drawable.messageinfo);
                localBuilder.setTitle("系统提示");
                localBuilder.setMessage("共选中" + tempCount2 + "个对象.\n删除后将无法恢复,是否继续?");
                localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Point.5
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        if (PubVar._PubCommand.GetDeleteObject().Delete(Toolbar_Point.this.m_EditLayerID, tmpSYSIDList, true)) {
                            PubVar._MapView._Select.ClearAllSelection();
                            PubVar._Map.RefreshFast();
                        }
                    }
                });
                localBuilder.setNegativeButton("取消", (DialogInterface.OnClickListener) null);
                localBuilder.show();
            } else if (!command.equals("手绘点")) {
                DoCommand(command);
            } else if (getButtonIsSelected("手绘点")) {
                PubVar._PubCommand.ProcessCommand("自由缩放");
                Common.ShowToast("停止手绘点");
            } else {
                DoCommand(command);
            }
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(String command) {
        if (command.equals("手绘点")) {
            PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("图层工具栏");
            SetButtonSelectedStatus("手绘点", true);
            PubVar._PubCommand.ProcessCommand("采集_手绘点");
        } else if (command.equals("GPS采样")) {
            AddGPSPoint_Dialog tempDialog = new AddGPSPoint_Dialog();
            tempDialog.SetCallback(this.pCallback);
            tempDialog.ShowDialog();
        } else if (command.equals("GPS足迹")) {
            if (!PubVar._PubCommand.m_GpsLocation.isOpen) {
                Common.ShowDialog("GPS设备未开启,请先开启GPS设备.");
            }
            if (!PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                Common.ShowDialog("GPS定位中,请稍候...");
            }
            this.m_GPSBtn.setTag("停止GPS足迹");
            this.m_GPSBtn.setCompoundDrawables(null, this._GPSRecordBg02, null, null);
            PubVar._PubCommand.ProcessCommand("自由缩放");
            PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("图层工具栏");
            SetButtonSelectedStatus("GPS拐点", true);
            SetButtonsEnable("GPS拐点", false);
            this._IsRecordByGPS = true;
            this.myGPSRecordHandler.post(this.myRecordLineByGPSTask);
        } else if (command.equals("停止GPS足迹")) {
            this._IsRecordByGPS = false;
            this._LastCoord = null;
            this._LastLocTime = 0;
            this.m_GPSBtn.setTag("GPS足迹");
            this.m_GPSBtn.setCompoundDrawables(null, this._GPSRecordBg01, null, null);
            SetButtonSelectedStatus("GPS拐点", false);
            SetButtonsEnable(null, true);
        } else if (command.equals("坐标绘点")) {
            AddPoint_Dialog tempDialog2 = new AddPoint_Dialog();
            tempDialog2.SetCallback(this.pCallback);
            tempDialog2.ShowDialog();
        } else if (command.equals("添加偏移点")) {
            if (this._LastAddCoordinate != null) {
                AddNextPoint_Dialog tmpDialog = new AddNextPoint_Dialog();
                tmpDialog.SetCallback(this.pCallback);
                tmpDialog.ShowDialog();
                return;
            }
            Common.ShowDialog("请先添加点之后才能进行此操作.");
        } else if (command.equals("地图中心点编辑完成")) {
            Coordinate tmpCoord = PubVar._MapView.getMap().getCenter().Clone();
            Coordinate localCoordinate2 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tmpCoord, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
            tmpCoord.setGeoX(localCoordinate2.getGeoX());
            tmpCoord.setGeoY(localCoordinate2.getGeoY());
            AddPoint(tmpCoord, "采集");
        } else if (command.equals("导入坐标文件")) {
            LoadCoordsFile_Dialog tmpDialog2 = new LoadCoordsFile_Dialog();
            tmpDialog2.SetCallback(this.pCallback);
            tmpDialog2.ShowDialog();
        } else if (command.equals("粘贴数据")) {
            try {
                Toolbar_Select.PasteGeometrys(this.m_context, this.m_EditGeoLayer, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Point.6
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command2, Object pObject) {
                        Object[] tmpObjs;
                        List<Integer> tmpPasteSysIDList;
                        if (command2.equals("粘贴数据成功") && pObject != null && (tmpObjs = (Object[]) pObject) != null && tmpObjs.length > 1 && Integer.parseInt(String.valueOf(tmpObjs[0])) == 1 && (tmpPasteSysIDList = (List) tmpObjs[1]) != null && tmpPasteSysIDList.size() > 0) {
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
