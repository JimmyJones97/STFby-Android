package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiQuery_Dialog;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.CommonEvent;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Edit.DeleteObject;
import  com.xzy.forestSystem.gogisapi.GPS.GPSHeadInfoSettingDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.CircleMenu.CircleMenuComposer;
import  com.xzy.forestSystem.gogisapi.MyControls.FeatureLayersList_Dialog;
import  com.xzy.forestSystem.gogisapi.Setting.HeadGPSCoordShowType_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.Layer_Select_Dialog;
import com.stczh.gzforestSystem.R;
import com.xzy.forestSystem.hellocharts.animation.ChartViewportAnimator;

import java.util.ArrayList;
import java.util.List;

public class MapCompLayoutToolbar {
    private CircleMenuComposer _MBExpandMoreTools = null;
    private ImageButton _MainMenuImageButton = null;
    private View _MainView = null;
    private LinearLayout _ReplaceMainMenuLL = null;
    private ImageView compassImageView = null;
    private TextView gpsTextView = null;
    private ImageButton m_CommonEventButton = null;
    private ImageButton m_GPSLocalImgButton = null;
    private GeoLayer m_SelectLayer = null;
    private int m_compassImageViewCount = 0;
    private final View.OnClickListener myGPSTipClickListener = new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.2
        @Override // android.view.View.OnClickListener
        public void onClick(View arg0) {
            new GPSHeadInfoSettingDialog(PubVar.MainContext).ShowDialog();
        }
    };
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String command, Object object) {
            GeoLayer tmpGeoLayer;
            if (command.equals("顶部坐标栏显示设置")) {
                PubVar._PubCommand.m_GpsLocation.updateShowTextInfo(MapCompLayoutToolbar.this.gpsTextView);
            } else if (command.equals("选择图层")) {
                String m_SelectLayers = object.toString();
                PubVar._MapView._Select.SetSelectLayers(m_SelectLayers);
                PubVar._PubCommand.m_ProjectDB.GetProjectManage().SaveSelectionLayers(m_SelectLayers);
            } else if (command.equals("开始编辑图层") && (tmpGeoLayer = PubVar._Map.GetGeoLayerByID(String.valueOf(object))) != null) {
                MapCompLayoutToolbar.this.m_SelectLayer = tmpGeoLayer;
                MapCompLayoutToolbar.this.DoCommand("显示编辑工具条");
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected(null);
                PubVar._PubCommand.ProcessCommand("自由缩放");
                Common.ShowToast("开始编辑图层【" + MapCompLayoutToolbar.this.m_SelectLayer.getLayerName() + "】.");
            }
        }
    };

    public void RefreshGPSInfoHeadHeight() {
        if (!PubVar.HeadTip_Visible) {
            PubVar._PubCommand.GPSInfoPanelHeight = 0;
            return;
        }
        int[] tmpInts = Common.GetViewSize(this.compassImageView);
        PubVar._PubCommand.GPSInfoPanelHeight = tmpInts[1];
    }

    @SuppressLint("WrongConstant")
    public void LoadToolbar(View view) {
        this._MainView = view;
        this._ReplaceMainMenuLL = (LinearLayout) view.findViewById(R.id.ll_replaceMainMenu);
        ImageView localImageView = (ImageView) view.findViewById(R.id.x_scalebar);
        PubVar._PubCommand.m_ScaleBar.SetImageView(localImageView);
        localImageView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                PubVar._PubCommand.ProcessCommand("地图比例尺显示设置");
            }
        });
        this.gpsTextView = (TextView) view.findViewById(R.id.textView_gps);
        TextView gpsStatusInfoText = (TextView) view.findViewById(R.id.textViewTitleTip);
        ImageView gpsOnOffImgView = (ImageView) view.findViewById(R.id.imageViewGPSOFF);
        ImageView gpsSNRImgView = (ImageView) view.findViewById(R.id.imageViewGPSSNR);
        this.gpsTextView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.4
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                HeadGPSCoordShowType_Dialog tempDialog = new HeadGPSCoordShowType_Dialog();
                tempDialog.SetCallback(MapCompLayoutToolbar.this.pCallback);
                tempDialog.ShowDialog();
            }
        });
        gpsStatusInfoText.setOnClickListener(this.myGPSTipClickListener);
        gpsOnOffImgView.setOnClickListener(this.myGPSTipClickListener);
        gpsSNRImgView.setOnClickListener(this.myGPSTipClickListener);
        if (PubVar._PubCommand.m_GpsLocation != null) {
            PubVar._PubCommand.m_GpsLocation.setControls(this.gpsTextView, gpsStatusInfoText, gpsOnOffImgView, gpsSNRImgView);
            PubVar._PubCommand.m_GpsLocation.updateShowTextInfo(this.gpsTextView);
        }
        ((ImageButton) view.findViewById(R.id.imageButtonGPS)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.5
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                PubVar._PubCommand.ProcessCommand("开启GPS设备");
            }
        });
        ((ImageButton) view.findViewById(R.id.imageButtonLayers)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.6
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                FeatureLayersList_Dialog tmpDialog = new FeatureLayersList_Dialog();
                if (MapCompLayoutToolbar.this.m_SelectLayer != null) {
                    tmpDialog.setCurrentEditLyrID(MapCompLayoutToolbar.this.m_SelectLayer.getLayerID());
                }
                tmpDialog.SetCallback(MapCompLayoutToolbar.this.pCallback);
                tmpDialog.ShowDialog();
            }
        });
        this.compassImageView = (ImageButton) view.findViewById(R.id.imageButtonCompass);
        this.compassImageView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.7
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                MapCompLayoutToolbar.this.m_compassImageViewCount++;
                if (MapCompLayoutToolbar.this.m_compassImageViewCount > 5) {
                    PubVar.RasterLayerShowRect = true;
                }
                if (MapCompLayoutToolbar.this.m_compassImageViewCount > 10) {
                    PubVar.RasterLayerShowRect = false;
                    MapCompLayoutToolbar.this.m_compassImageViewCount = 0;
                }
            }
        });
        if (PubVar._PubCommand.m_Compass != null) {
            PubVar._PubCommand.m_Compass.setCompassImageView(this.compassImageView);
        }
        if (PubVar.Compass_Show) {
            this.compassImageView.setVisibility(0);
        } else {
            this.compassImageView.setVisibility(8);
        }
        ((ImageButton) view.findViewById(R.id.imageButtonSwitchLayers)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.8
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                new SwitchGroupLayer_Dailog().ShowDialog();
            }
        });
        ((ImageButton) view.findViewById(R.id.imageButtonLayersContent)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.9
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (PubVar.MyLayersContent == null) {
                    LayersContent_Dialog tmpLayersContent_Dialog = new LayersContent_Dialog(PubVar.MainContext, PubVar._PubCommand.m_MainLayout);
                    PubVar.MyLayersContent = tmpLayersContent_Dialog;
                    tmpLayersContent_Dialog.showDialog();
                }
            }
        });
        this._MainMenuImageButton = (ImageButton) view.findViewById(R.id.imageButtonMainMenu);
        this._MainMenuImageButton.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.10
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                MainMenuDialog.ShowMenu(PubVar.MainContext, MapCompLayoutToolbar.this._MainMenuImageButton.getX(), MapCompLayoutToolbar.this._MainMenuImageButton.getY());
            }
        });
        this._MBExpandMoreTools = (CircleMenuComposer) view.findViewById(R.id.cmc_expandMore);
        this._MBExpandMoreTools.init(new int[]{R.drawable.mb_pan, R.drawable.mb_filter48, R.drawable.mb_info, R.drawable.mb_select, R.drawable.mb_attrib, R.drawable.mb_query}, new String[]{"自由缩放", "选择图层筛选", "单击选择", "选择", "查看属性", "缩放至图层窗体"}, R.drawable.mb_more, R.drawable.mb_more, CircleMenuComposer.RIGHTCENTER, (int) (65.0f * PubVar.ScaledDensity), ChartViewportAnimator.FAST_ANIMATION_DURATION, 100, R.drawable.mb_button_selector);
        this._MBExpandMoreTools.SetClickItemCollapse(false);
        this._MBExpandMoreTools.setButtonsOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.11
            @SuppressLint("ResourceType")
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (v.getId() == 100) {
                    PubVar._PubCommand.ProcessCommand("自由缩放");
                    Common.ShowToast("自由缩放地图");
                } else if (v.getId() == 101) {
                    Layer_Select_Dialog tempDialog = new Layer_Select_Dialog();
                    List tmpList = new ArrayList();
                    tmpList.addAll(PubVar._Map.getGeoLayers().getList());
                    tmpList.addAll(PubVar._Map.getVectorGeoLayers().getList());
                    tempDialog.SetLayersList(tmpList);
                    tempDialog.SetLayerSelectType(5);
                    tempDialog.SetSelectedLayers(PubVar._MapView._Select.getSelectLayers());
                    tempDialog.SetAllowMultiSelect(true);
                    tempDialog.SetCallback(MapCompLayoutToolbar.this.pCallback);
                    tempDialog.ShowDialog();
                } else if (v.getId() == 102) {
                    PubVar._PubCommand.ProcessCommand("单击选择");
                    PubVar._PubCommand.JustUpdateToolbarBtnIsSelected("地图缩放工具栏", "单击选择", "图层工具栏");
                    Common.ShowToast("点击查询");
                } else if (v.getId() == 103) {
                    PubVar._MapView._Select.ClearAllSelection();
                    PubVar._MapView._GraphicLayer.Clear();
                    PubVar._MapView.invalidate();
                    PubVar._PubCommand.ProcessCommand("选择");
                    PubVar._PubCommand.JustUpdateToolbarBtnIsSelected("地图缩放工具栏", "选择", "图层工具栏");
                    PubVar._MapView._Select.SetMultiSeletected(true);
                    Common.ShowToast("选择对象");
                } else if (v.getId() == 104) {
                    PubVar._PubCommand.ProcessCommand("属性");
                } else if (v.getId() == 105) {
                    PubVar._PubCommand.ProcessCommand("缩放至图层窗体");
                }
            }
        });
        this.m_GPSLocalImgButton = (ImageButton) view.findViewById(R.id.imageButtonGPSLoc);
        this.m_GPSLocalImgButton.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.12
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (v.getTag() == null) {
                    return;
                }
                if (!PubVar._PubCommand.m_GpsLocation.isOpen) {
                    PubVar._PubCommand.ProcessCommand("开启GPS设备");
                    return;
                }
                String command = String.valueOf(v.getTag());
                if (command.equals("解锁GPS")) {
                    MapCompLayoutToolbar.this.m_GPSLocalImgButton.setImageResource(R.drawable.mb_gpslock);
                    v.setTag("锁定GPS");
                    PubVar.AutoPan = false;
                    Common.ShowToast("GPS跟踪解锁");
                } else if (command.equals("锁定GPS")) {
                    MapCompLayoutToolbar.this.m_GPSLocalImgButton.setImageResource(R.drawable.mb_gpsarrow);
                    v.setTag("解锁GPS");
                    PubVar.AutoPan = true;
                    if (!PubVar._PubCommand.m_GpsLocation.isOpen) {
                        PubVar._PubCommand.ProcessCommand("开启GPS设备");
                    } else if (PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                        Common.ShowToast("当前GPS设备还没有定位,请在开阔地带等待卫星信号.");
                    } else {
                        Common.ShowToast("锁定GPS跟踪");
                    }
                }
            }
        });
        ImageButton tmpIbtn01 = (ImageButton) view.findViewById(R.id.imageButtonZoomByExtend);
        this._MBExpandMoreTools.addButtonTag("框选缩放", R.id.imageButtonZoomByExtend, tmpIbtn01);
        tmpIbtn01.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.13
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PubVar._PubCommand.ProcessCommand("框选缩放");
            }
        });
        this.m_CommonEventButton = (ImageButton) view.findViewById(R.id.imageButton_Common);
        this.m_CommonEventButton.setVisibility(8);
        this.m_CommonEventButton.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.14
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (PubVar._PubCommand.getCommonEvent() != null) {
                    CommonEvent tmpCommonEvent = PubVar._PubCommand.getCommonEvent();
                    if (tmpCommonEvent.Dialog != null) {
                        tmpCommonEvent.Dialog.show();
                        PubVar._PubCommand.SetCommonEvent(null);
                        MapCompLayoutToolbar.this.m_CommonEventButton.setVisibility(8);
                    }
                }
            }
        });
        ((ImageButton) view.findViewById(R.id.imageButtonYangdiDiaocha)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.15
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                new XiaoBanYangDiQuery_Dialog().ShowDialog();
            }
        });
        refreshModuleButtons();
    }

    public void DoCommand(String command) {
        BaseToolbar tmpToolbar;
        if (command.equals("显示编辑工具条")) {
            GeoLayer tmpGeoLayer = this.m_SelectLayer;
            if (this.m_SelectLayer != null) {
                DoCommand("隐藏编辑工具条");
                this.m_SelectLayer = tmpGeoLayer;
                if (this.m_SelectLayer.getType() == EGeoLayerType.POINT) {
                    PubVar._PubCommand.ShowToolbar("点编辑工具栏");
                    BaseToolbar tmpToolbar2 = PubVar._PubCommand.GetToolbarByName("点编辑工具栏");
                    if (tmpToolbar2 != null) {
                        ((Toolbar_Point) tmpToolbar2).SetEditLayerID(this.m_SelectLayer.getLayerID());
                    }
                } else if (this.m_SelectLayer.getType() == EGeoLayerType.POLYLINE) {
                    PubVar._PubCommand.ShowToolbar("线编辑工具栏");
                    BaseToolbar tmpToolbar3 = PubVar._PubCommand.GetToolbarByName("线编辑工具栏");
                    if (tmpToolbar3 != null) {
                        ((Toolbar_Polyline) tmpToolbar3).SetEditLayerID(this.m_SelectLayer.getLayerID());
                        ((Toolbar_Polyline) tmpToolbar3).SetEditLine(PubVar._PubCommand.GetLineEdit());
                    }
                } else if (this.m_SelectLayer.getType() == EGeoLayerType.POLYGON) {
                    PubVar._PubCommand.ShowToolbar("面编辑工具栏");
                    BaseToolbar tmpToolbar4 = PubVar._PubCommand.GetToolbarByName("面编辑工具栏");
                    if (tmpToolbar4 != null) {
                        ((Toolbar_Polygon) tmpToolbar4).SetEditLayerID(this.m_SelectLayer.getLayerID());
                        ((Toolbar_Polygon) tmpToolbar4).SetEditPolygon(PubVar._PubCommand.GetPolygonEdit());
                    }
                }
            }
        } else if (command.equals("隐藏编辑工具条")) {
            if (this.m_SelectLayer.getType() == EGeoLayerType.POINT && (tmpToolbar = PubVar._PubCommand.GetToolbarByName("点编辑工具栏")) != null) {
                ((Toolbar_Point) tmpToolbar).SetEditLayerID(null);
            }
            PubVar._PubCommand.HideToolbar("点编辑工具栏");
            PubVar._PubCommand.HideToolbar("线编辑工具栏");
            PubVar._PubCommand.HideToolbar("面编辑工具栏");
        } else if (command.equals("解锁GPS")) {
            this._MBExpandMoreTools.UpdateItemImage(2, R.drawable.mb_gpslock);
            this._MBExpandMoreTools.SetItemTag(2, "锁定GPS");
            PubVar.AutoPan = false;
            Common.ShowToast("GPS跟踪解锁");
        } else if (command.equals("锁定GPS")) {
            this._MBExpandMoreTools.UpdateItemImage(2, R.drawable.mb_gpsarrow);
            this._MBExpandMoreTools.SetItemTag(2, "解锁GPS");
            PubVar.AutoPan = true;
            Common.ShowToast("锁定GPS跟踪");
        } else if (command.equals("删除选中对象")) {
            int tmpCount = PubVar._Map.GetSelectObjectsCount(-1, false);
            if (tmpCount > 0) {
                Common.ShowYesNoDialog(PubVar.MainContext, "是否删除选择的" + String.valueOf(tmpCount) + "个对象?\r\n提示:删除该数据后将无法恢复.", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.16
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command2, Object pObject) {
                        if (command2.equals("YES")) {
                            new DeleteObject().DeleteAllSelected();
                            PubVar._PubCommand.ProcessCommand("选择_仅清空选择集");
                            PubVar._Map.Refresh();
                        }
                    }
                });
            } else {
                Common.ShowDialog("当前没有可删除的选择对象,请用选择工具在地图上选择采集图层中的对象后再进行此操作.");
            }
        }
    }

    public CircleMenuComposer getExpandMoreTools() {
        return this._MBExpandMoreTools;
    }

    @SuppressLint("WrongConstant")
    public void SetMainMenuVisible(boolean visible) {
        if (visible) {
            this._MainMenuImageButton.setVisibility(0);
            this._ReplaceMainMenuLL.setVisibility(8);
            return;
        }
        this._MainMenuImageButton.setVisibility(4);
        this._ReplaceMainMenuLL.setVisibility(0);
    }

    public CircleMenuComposer getMBExpandMoreTools() {
        return this._MBExpandMoreTools;
    }

    public GeoLayer getEditFeatureayer() {
        return this.m_SelectLayer;
    }

    public void clearEditFeatureayer() {
        this.m_SelectLayer = null;
    }

    @SuppressLint("WrongConstant")
    public void setCommonEventButtonVisible(boolean visible) {
        if (visible) {
            this.m_CommonEventButton.setVisibility(0);
        } else {
            this.m_CommonEventButton.setVisibility(8);
        }
    }

    @SuppressLint("WrongConstant")
    public void refreshModuleButtons() {
        ImageButton tmpYangDiQuery = (ImageButton) this._MainView.findViewById(R.id.imageButtonYangdiDiaocha);
        if (PubVar.Module_YangdiDiaoCha) {
            tmpYangDiQuery.setVisibility(0);
        } else {
            tmpYangDiQuery.setVisibility(8);
        }
    }
}
