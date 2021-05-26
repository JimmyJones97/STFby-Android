package  com.xzy.forestSystem.gogisapi.Common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.xzy.forestSystem.GuiZhou.DiaoCha.CommonSetting;
import com.xzy.forestSystem.GuiZhou.DiaoCha.YangMu2_Dialog;
import com.xzy.forestSystem.MainActivity;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.GraphicSymbolGeometry;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.RasterLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Map;
import  com.xzy.forestSystem.gogisapi.Carto.MapChild_Dialog;
import  com.xzy.forestSystem.gogisapi.Carto.MapTools;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import  com.xzy.forestSystem.gogisapi.Carto.MeasureTool;
import  com.xzy.forestSystem.gogisapi.Carto.ScaleBar;
import  com.xzy.forestSystem.gogisapi.Config.SysConfig;
import  com.xzy.forestSystem.gogisapi.Config.UserConfig;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.TranslateSetting_Dialog;
import  com.xzy.forestSystem.gogisapi.Display.TextPositionAdjust;
import  com.xzy.forestSystem.gogisapi.Edit.DeleteObject;
import  com.xzy.forestSystem.gogisapi.Edit.PointEditClass;
import  com.xzy.forestSystem.gogisapi.Edit.PolygonEditClass;
import  com.xzy.forestSystem.gogisapi.Edit.PolylineEditClass;
import  com.xzy.forestSystem.gogisapi.GPS.BluetoothGPSSelectDialog;
import  com.xzy.forestSystem.gogisapi.GPS.CompassClass;
import  com.xzy.forestSystem.gogisapi.GPS.GPSDevice;
import  com.xzy.forestSystem.gogisapi.GPS.GPSDisplay;
import  com.xzy.forestSystem.gogisapi.GPS.GPSInfoDialog;
import  com.xzy.forestSystem.gogisapi.GPS.GPSLocationClass;
import  com.xzy.forestSystem.gogisapi.Geodatabase.C0542Workspace;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataExport_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Data_MultiSelect_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.ProjectDatabase;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.EGeometryType;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.TubanInfo_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.CircleMenu.CircleMenuComposer;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MagnifierView;
import  com.xzy.forestSystem.gogisapi.Navigation.C0706Navigation;
import  com.xzy.forestSystem.gogisapi.Setting.ScaleSetting_Dialog;
import  com.xzy.forestSystem.gogisapi.System.About_Dialog;
import  com.xzy.forestSystem.gogisapi.System.Help_Dialog;
import  com.xzy.forestSystem.gogisapi.System.SystemSettingParas_Dialog;
import  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar;
import  com.xzy.forestSystem.gogisapi.Toolbar.MainMenubar;
import  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar;
import  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Cloud;
import  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Measure;
import  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Point;
import  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polygon;
import  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Polyline;
import  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Select;
import  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_SenLinDuCha;
import  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Tools;
import  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Track;
import  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_ZoomScale;
import  com.xzy.forestSystem.gogisapi.Toolbar.ZoomToLayer_Dailog;
import  com.xzy.forestSystem.gogisapi.Tools.ToolsManager_Dialog;
import  com.xzy.forestSystem.gogisapi.Track.TrackLine;
import  com.xzy.forestSystem.gogisapi.Workspace.LayerRenderWorkspace;
import  com.xzy.forestSystem.gogisapi.Workspace.LayerWorkspace;
import  com.xzy.forestSystem.gogisapi.Workspace.ProjectWorkspace;
import  com.xzy.forestSystem.gogisapi.Workspace.RasterLayerWorkspace;
import  com.xzy.forestSystem.gogisapi.Workspace.VectorLayerWorkspace;
import  com.xzy.forestSystem.gogisapi.XProject.FeatureLayers_Manage_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.Layers_Manag_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Select_Dialog;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PubCommand {
    public int GPSInfoPanelHeight;
    List<String> ObjectList;
    private TextPositionAdjust TextPositionAdjustT;
    private ICallback _Callback;
    private List<MapChild_Dialog> m_ChildMapList;
    CommonEvent m_CommonEvent;
    public CompassClass m_Compass;
    public SysConfig m_ConfigDB;
    public Context m_Context;
    private List<GraphicSymbolGeometry> m_CopyGraphicsList;
    private DeleteObject m_Delete;
    public View m_EditBar;
    public GPSDevice m_GPSDevice;
    private GPSDisplay m_GPSDisplay;
    public GPSLocationClass m_GpsLocation;
    private PolylineEditClass m_LineEdit;
    public CircleMenuComposer m_MBExpandMoreTools;
    public MagnifierView m_Magnifer;
    public RelativeLayout m_MainLayout;
    public MainMenubar m_MainMenubar;
    public MapCompLayoutToolbar m_MapCompLayoutToolbar;
    public MeasureTool m_Measure;
    public C0706Navigation m_Navigation;
    private int m_OpenProjectMode;
    public PointEditClass m_PointEdit;
    private PolygonEditClass m_PolygonEdit;
    public ProjectDatabase m_ProjectDB;
    public int m_PubVarID;
    public ScaleBar m_ScaleBar;
    public List<String> m_ToolbarNames;
    public List<BaseToolbar> m_Toolbars;
    private TrackLine m_TrackLine;
    public UserConfig m_UserConfigDB;
    private Toolbar_ZoomScale m_ZoomScaleToolbar;
    private MapView m_mapView;

    public PubCommand(Context context) {
        this.ObjectList = null;
        this.m_ConfigDB = null;
        this.m_Context = null;
        this.m_Delete = null;
        this.m_EditBar = null;
        this.m_GpsLocation = null;
        this.m_GPSDevice = null;
        this.m_Compass = null;
        this.m_PubVarID = -1;
        this.m_mapView = null;
        this.m_GPSDisplay = null;
        this.m_Measure = null;
        this.m_PointEdit = null;
        this.m_LineEdit = null;
        this.m_PolygonEdit = null;
        this.m_TrackLine = null;
        this.m_Navigation = null;
        this.m_Magnifer = null;
        this.m_ProjectDB = null;
        this.m_ScaleBar = null;
        this.m_UserConfigDB = null;
        this.m_MainMenubar = null;
        this.m_MainLayout = null;
        this.m_MBExpandMoreTools = null;
        this.m_MapCompLayoutToolbar = null;
        this.m_Toolbars = null;
        this.m_ToolbarNames = null;
        this.m_ZoomScaleToolbar = null;
        this.TextPositionAdjustT = new TextPositionAdjust();
        this.m_OpenProjectMode = 0;
        this.m_ChildMapList = new ArrayList();
        this.GPSInfoPanelHeight = 0;
        this.m_CopyGraphicsList = new ArrayList();
        this.m_CommonEvent = null;
        this._Callback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Common.PubCommand.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                int tmpI;
                GraphicSymbolGeometry tmpGraphicSymbolGeometry;
                HashMap<String, Object> tmphasMap;
                Intent tempIntent;
                try {
                    if (paramString.equals("SelectToolReturn")) {
                        if (pObject != null && String.valueOf(pObject).equals("属性")) {
                            PubVar._PubCommand.ProcessCommand("属性");
                        }
                    } else if (paramString.equals("编辑属性数据返回") || paramString.equals("编辑属性返回")) {
                        if (pObject != null) {
                            PubVar._Map.RefreshFast();
                        }
                    } else if (paramString.equals("对话框关闭事件")) {
                        if (pObject != null) {
                            String.valueOf(pObject).equals("注册系统返回");
                        }
                    } else if (!paramString.equals("SelectToolReturnGraphicLayer")) {
                        PubCommand.this.ProcessCommand("SelectEndCallBack");
                    } else if (pObject != null && (tmpI = Integer.parseInt(String.valueOf(pObject))) > -1 && tmpI < PubCommand.this.m_mapView._GraphicLayer.getGeoemtrysCount() && (tmpGraphicSymbolGeometry = PubCommand.this.m_mapView._GraphicLayer.getGeometryByIndex(tmpI)) != null && tmpGraphicSymbolGeometry._GeometryType.length() > 0) {
                        if (tmpGraphicSymbolGeometry._GeometryType.equals("图片对象")) {
                            HashMap<String, Object> tmphashHashMap = tmpGraphicSymbolGeometry._AttributeHashMap;
                            if (tmphashHashMap != null && tmphashHashMap.containsKey("Path")) {
                                File tmpFile = new File(String.valueOf(tmphashHashMap.get("Path")));
                                if (tmpFile.exists() && (tempIntent = Common.OpenFile(tmpFile.getAbsolutePath())) != null) {
                                    PubCommand.this.m_Context.startActivity(tempIntent);
                                }
                            }
                        } else if (tmpGraphicSymbolGeometry._GeometryType.equals("样木对象") && (tmphasMap = tmpGraphicSymbolGeometry._AttributeHashMap) != null && tmphasMap.containsKey("YangDiName")) {
                            final String tmpYDName = String.valueOf(tmphasMap.get("YangDiName"));
                            String tmpModifyID = String.valueOf(tmphasMap.get("ID"));
                            if (tmpModifyID != null && tmpModifyID.length() > 0) {
                                HashMap<String, String> tmphasMap2 = CommonSetting.GetYangMuInfo(tmpModifyID);
                                if (tmphasMap2 == null || tmphasMap2.size() <= 0) {
                                    Common.ShowToast("未查询到该样木的信息.");
                                    return;
                                }
                                YangMu2_Dialog tmpDialog = new YangMu2_Dialog();
                                tmpDialog.SetTitle(String.valueOf(tmpYDName) + "样木信息");
                                tmpDialog.setReturnTag(tmpModifyID);
                                tmpDialog.SetDefaultData(String.valueOf(tmphasMap2.get("ShuZhong")), String.valueOf(tmphasMap2.get("XiongJing")), String.valueOf(tmphasMap2.get("ShuGao")), String.valueOf(tmphasMap2.get("Remark")), String.valueOf(tmphasMap2.get("X")), String.valueOf(tmphasMap2.get("Y")));
                                tmpDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Common.PubCommand.1.1
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String command, Object pObject2) {
                                        Object[] tmpObjs01;
                                        if (command.equals("样木信息返回") && (tmpObjs01 = (Object[]) pObject2) != null && tmpObjs01.length > 1) {
                                            String tmpID = String.valueOf(tmpObjs01[0]);
                                            Object[] tmpObjs02 = (Object[]) tmpObjs01[1];
                                            if (tmpObjs02 != null && tmpObjs02.length > 5 && CommonSetting.UpdateYangMuData(tmpID, tmpYDName, String.valueOf(tmpObjs02[0]), String.valueOf(tmpObjs02[1]), String.valueOf(tmpObjs02[2]), String.valueOf(tmpObjs02[3]), String.valueOf(tmpObjs02[4]), String.valueOf(tmpObjs02[5]))) {
                                                Common.ShowToast("修改样木信息成功");
                                            }
                                        }
                                    }
                                });
                                tmpDialog.ShowDialog();
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_mapView = PubVar._MapView;
        this.m_mapView._Select.SetCallback(this._Callback);
        this.m_GPSDisplay = new GPSDisplay(PubVar._MapView);
        PubVar._GPSDisplay = this.m_GPSDisplay;
        this.m_Context = context;
        PubVar._PubCommand = this;
        this.m_Measure = new MeasureTool();
        this.m_ScaleBar = new ScaleBar();
        this.m_TrackLine = new TrackLine();
        this.m_Toolbars = new ArrayList();
        this.m_ToolbarNames = new ArrayList();
        this.m_ConfigDB = new SysConfig();
        this.m_UserConfigDB = new UserConfig();
        this.m_UserConfigDB.LoadSystemConfig();
        this.m_ProjectDB = new ProjectDatabase();
        this.m_Delete = new DeleteObject();
        this.m_Compass = new CompassClass(this.m_Context);
        this.m_GpsLocation = new GPSLocationClass(this.m_Context);
    }

    public void SetOpenProjectMode(int mode) {
        this.m_OpenProjectMode = mode;
    }

    private void IntialMap() {
        if (PubVar.m_Workspace != null) {
            PubVar.m_Workspace.ClearWorkspace();
            PubVar.m_Workspace = null;
            PubVar._Map.Dispose();
            PubVar._Map = null;
            System.gc();
        }
        PubVar.m_Workspace = new C0542Workspace();
        String str1 = Common.GetSDCardPath();
        if (str1.equals("")) {
            Common.ShowDialog("系统目录：【" + PubVar.AppName + "】不存在，无法启动程序！");
            return;
        }
        Map localMap = new Map(this.m_mapView);
        localMap.setSystemPath(str1);
        PubVar._Map = localMap;
        String str2 = Common.ReadConfigItem("MapSubPath");
        if (!Common.CheckExistFile(String.valueOf(str1) + "/Map/" + str2 + "/TP.dbx")) {
            str2 = "";
        }
        if (str2.equals("")) {
            List localList = Common.GetValidMapSubPath();
            if (localList.size() >= 1) {
                str2 = (String) localList.get(0);
            }
        }
        String str3 = String.valueOf(str1) + "/Data/" + str2 + FileSelector_Dialog.sRoot + PubVar.m_SysDataName + ".dbx";
        if (!Common.CheckExistFile(str3)) {
            Common.CopyFile(String.valueOf(str1) + "/SysFile/Template.dbx", str3);
        }
        this.m_mapView.invalidate();
    }

    public void SetZoomScaleToolbar(Toolbar_ZoomScale toolbar) {
        this.m_ZoomScaleToolbar = toolbar;
        int tempIndex = this.m_ToolbarNames.indexOf("地图缩放工具栏");
        if (tempIndex < 0) {
            this.m_ToolbarNames.add("地图缩放工具栏");
            this.m_Toolbars.add(this.m_ZoomScaleToolbar);
            return;
        }
        this.m_Toolbars.set(tempIndex, this.m_ZoomScaleToolbar);
    }

    private void SetButtomImageStatus(String paramString) {
        ArrayList localArrayList = new ArrayList();
        localArrayList.add("MenuItem1,menu_select");
        localArrayList.add("MenuItem2,menu_feature");
        localArrayList.add("MenuItem3,menu_line");
        localArrayList.add("MenuItem4,menu_point");
        localArrayList.add("MenuItem5,menu_otherpoint");
        Activity localActivity = (Activity) this.m_Context;
        Iterator localIterator = localArrayList.iterator();
        while (localIterator.hasNext()) {
            String str1 = (String) localIterator.next();
            String str2 = str1.split(",")[0];
            String str3 = str1.split(",")[1];
            int i = this.m_Context.getResources().getIdentifier(str2, "id", this.m_Context.getPackageName());
            int j = this.m_Context.getResources().getIdentifier(str3, "drawable", this.m_Context.getPackageName());
            int k = this.m_Context.getResources().getIdentifier(String.valueOf(str3) + "_0", "drawable", this.m_Context.getPackageName());
            ImageView localImageView = (ImageView) localActivity.findViewById(i);
            if (str2.equals(paramString)) {
                ViewGroup.LayoutParams localLayoutParams2 = localImageView.getLayoutParams();
                localLayoutParams2.width = 100;
                localLayoutParams2.height = 64;
                localImageView.setLayoutParams(localLayoutParams2);
                localImageView.setImageResource(k);
            }
            ViewGroup.LayoutParams localLayoutParams1 = localImageView.getLayoutParams();
            localLayoutParams1.width = 74;
            localLayoutParams1.height = 48;
            localImageView.setLayoutParams(localLayoutParams1);
            localImageView.setImageResource(j);
        }
    }

    public boolean AlwaysOpenProject() {
        if (!this.m_ProjectDB.AlwaysOpenProject()) {
            Common.ShowDialog(this.m_Context, "系统没有加载任何项目信息，无法完成操作！");
        }
        return this.m_ProjectDB.AlwaysOpenProject();
    }

    public void ProcessCommand(String paramString) {
        String tmpTipMsg;
        String[] arrayOfString = paramString.split("_");
        if (arrayOfString != null) {
            if (arrayOfString.length == 1) {
                if (paramString.equals("加载")) {
                    IntialMap();
                    this.m_mapView.setActiveTool(MapTools.FullScreenSize);
                    if (!Common.RestoreViewExtend()) {
                        this.m_mapView.setActiveTool(MapTools.FullScreen);
                    }
                    this.m_mapView.setActiveTool(MapTools.ZoomInOutPan);
                } else if (!paramString.equals("加载项目")) {
                    if (paramString.equals("无工具")) {
                        this.m_mapView.setActiveTool(MapTools.None);
                        UpdateAllToolbarStatusNotSelected(null);
                    } else if (paramString.equals("全屏")) {
                        this.m_mapView.setActiveTool(MapTools.FullScreen);
                    } else if (paramString.equals("全屏尺寸")) {
                        this.m_mapView.setActiveTool(MapTools.FullScreenSize);
                    } else if (paramString.equals("全图缩放")) {
                        this.m_mapView.getMap().RefreshMapExtend();
                        this.m_mapView.setActiveTool(MapTools.GlobalMap);
                    } else if (paramString.equals("放大")) {
                        this.m_mapView.setActiveTool(MapTools.ZoomIn);
                    } else if (paramString.equals("缩小")) {
                        this.m_mapView.setActiveTool(MapTools.ZoomOut);
                    } else if (paramString.equals("单击放大")) {
                        this.m_mapView.SetZoomIn();
                    } else if (paramString.equals("单击缩小")) {
                        this.m_mapView.SetZoomOut();
                    } else if (paramString.equals("自由缩放")) {
                        this.m_mapView.setActiveTool(MapTools.ZoomInOutPan);
                        JustUpdateToolbarBtnIsSelected("地图缩放工具栏", "自由缩放", "图层工具栏");
                    } else if (paramString.equals("解锁GPS")) {
                        this.m_MBExpandMoreTools.UpdateItemImage(2, R.drawable.mb_gpslock);
                        this.m_MBExpandMoreTools.SetItemTag(2, "锁定GPS");
                        PubVar.AutoPan = false;
                    } else if (paramString.equals("锁定GPS")) {
                        this.m_MBExpandMoreTools.UpdateItemImage(2, R.drawable.mb_gpsarrow);
                        this.m_MBExpandMoreTools.SetItemTag(2, "解锁GPS");
                        PubVar.AutoPan = true;
                    } else if (paramString.equals("开启GPS设备")) {
                        if (PubVar.GPS_Device_Type != 0) {
                            if (PubVar._PubCommand.m_GPSDevice == null || PubVar._PubCommand.m_GPSDevice.isDisposed) {
                                PubVar._PubCommand.CreateGPSDevice();
                            }
                            new GPSInfoDialog(PubVar.MainContext).ShowDialog();
                        } else if (Common.CheckGPSEnable(PubVar.MainContext)) {
                            if (PubVar._PubCommand.m_GPSDevice == null || PubVar._PubCommand.m_GPSDevice.isDisposed || !PubVar._PubCommand.m_GpsLocation.isOpen || !PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                                PubVar._PubCommand.CreateGPSDevice();
                            }
                            new GPSInfoDialog(PubVar.MainContext).ShowDialog();
                        } else {
                            Common.ShowYesNoDialog(PubVar.MainContext, "GPS设备没有开启.\r\n是否打开GPS设置界面?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Common.PubCommand.2
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String paramString2, Object pObject) {
                                    if (paramString2.equals("YES")) {
                                        PubVar.MainContext.startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                                    }
                                }
                            });
                        }
                    } else if (paramString.equals("缩放至图层窗体")) {
                        new ZoomToLayer_Dailog().ShowDialog();
                    } else if (paramString.equals("选择")) {
                        this.m_mapView.setActiveTool(MapTools.Select);
                        this.m_mapView._Select.SetSeletected(true);
                        this.m_mapView._Select.SetIsSingleClick(false);
                        this.m_mapView._Select.SetMultiSeletected(false);
                    } else if (paramString.equals("多项选择")) {
                        this.m_mapView.setActiveTool(MapTools.Select);
                        this.m_mapView._Select.SetSeletected(true);
                        this.m_mapView._Select.SetIsSingleClick(false);
                        this.m_mapView._Select.SetMultiSeletected(true);
                    } else if (paramString.equals("单击选择")) {
                        this.m_mapView.setActiveTool(MapTools.Select);
                        this.m_mapView._Select.SetSeletected(true);
                        this.m_mapView._Select.SetIsSingleClick(true);
                        this.m_mapView._Select.SetMultiSeletected(false);
                    } else if (paramString.equals("快速刷新地图")) {
                        PubVar._Map.RefreshFast();
                    } else if (paramString.equals("刷新地图")) {
                        PubVar._Map.Refresh();
                    } else if (paramString.equals("卷帘开启")) {
                        this.m_mapView.setActiveTool(MapTools.ShutterTool);
                        this.m_mapView.m_ShutterTool.SetIsShutterMode(true);
                    } else if (paramString.equals("卷帘关闭")) {
                        this.m_mapView.m_ShutterTool.SetIsShutterMode(false);
                        PubVar._Map.RefreshFast2();
                        ProcessCommand("自由缩放");
                    } else if (paramString.equals("框选缩放")) {
                        this.m_mapView.setActiveTool(MapTools.ZoomByExtend);
                        JustUpdateToolbarBtnIsSelected("地图缩放工具栏", "框选缩放", "");
                        Common.ShowToast("开始在地图上框选缩放.\n从左往右框选为放大操作.\n从右往左为缩小操作.");
                    } else if (paramString.equals("SelectEndCallBack")) {
                        int tempCount = this.m_mapView._Select.GetSelectedCount();
                        if (this.m_MainMenubar != null) {
                            this.m_MainMenubar.UpdateSeletedCount(tempCount);
                        }
                    } else if (paramString.equals("移动选择")) {
                        PubVar._MapView.setActiveTool(MapTools.MoveObject);
                    } else if (paramString.equals("图层")) {
                        new Layers_Manag_Dialog().ShowDialog();
                    } else if (paramString.equals("单个属性")) {
                        BasicValue layerIDParam = new BasicValue();
                        BasicValue geoIndexParam = new BasicValue();
                        BasicValue geoSYSIDParam = new BasicValue();
                        BasicValue dataSourceNameParam = new BasicValue();
                        if (!Common.GetSelectOneObjectInfo(PubVar._Map, layerIDParam, geoIndexParam, geoSYSIDParam, dataSourceNameParam)) {
                            Common.ShowDialog("请选择需要查询的实体！");
                        } else if (PubVar.m_Workspace.GetDataSourceByName(dataSourceNameParam.getString()) != null) {
                            if (this.m_CommonEvent != null) {
                                if (this.m_CommonEvent.returnCallback != null) {
                                    this.m_CommonEvent.returnCallback.OnClick(this.m_CommonEvent.returnBackCommand, new Object[]{layerIDParam.getString(), geoSYSIDParam.getString(), dataSourceNameParam.getString()});
                                }
                                SetCommonEvent(null);
                                return;
                            }
                            boolean tmpBool = true;
                            if (PubVar.Module_SenLinDuCha &&  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.CommonSetting.IsDuchaLayer(layerIDParam.getString())) {
                                tmpBool = false;
                                TubanInfo_Dialog tmpDialog = new TubanInfo_Dialog();
                                tmpDialog.setGeoLayer(PubVar._Map.GetGeoLayerByID(layerIDParam.getString()));
                                tmpDialog.setSYSID(geoSYSIDParam.getString());
                                tmpDialog.ShowDialog();
                            }
                            if (tmpBool) {
                                FeatureAttribute_Dialog tmpFeatAttrDialog = new FeatureAttribute_Dialog();
                                tmpFeatAttrDialog.SetGeometryIndex(layerIDParam.getString(), geoIndexParam.getInt(), dataSourceNameParam.getString());
                                tmpFeatAttrDialog.SetCallback(this._Callback);
                                tmpFeatAttrDialog.ShowDialog();
                            }
                        }
                    } else if (paramString.equals("属性")) {
                        int tempCount2 = Common.GetSelectObjectsCount(PubVar._Map);
                        if (tempCount2 == 0) {
                            Common.ShowDialog("请先选择实体！");
                        } else if (tempCount2 == 1) {
                            ProcessCommand("单个属性");
                        } else {
                            new Data_MultiSelect_Dialog().ShowDialog();
                        }
                    } else if (paramString.equals("查询")) {
                        new DataQuery_Dialog().ShowDialog();
                    } else if (paramString.equals("导出数据")) {
                        new DataExport_Dialog().ShowDialog();
                    } else if (paramString.equals("保存")) {
                        Common.ShowProgressDialog("正在保存项目...", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Common.PubCommand.3
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject) {
                                try {
                                    String[] tempOutMsg = {""};
                                    PubVar.m_Workspace.Save(tempOutMsg);
                                    PubCommand.this.SaveToolbarPosition();
                                    if (tempOutMsg[0].equals("")) {
                                        PubCommand.this.m_mapView._Select.ClearAllSelection();
                                        PubVar._Map.Refresh();
                                        Common.SaveImgFile(PubVar._PubCommand.m_ProjectDB.GetProjectManage().GetProjectDataPreviewImageName(), PubVar._Map.getMaskBitmap());
                                        PubCommand.this.ProcessCommand("项目_保存");
                                        Common.ShowDialog("项目保存成功.");
                                    } else {
                                        Common.ShowDialog(tempOutMsg[0]);
                                    }
                                    if (pObject != null && (pObject instanceof Handler)) {
                                        Handler tmpHandler = (Handler) pObject;
                                        Message tmpMsg = tmpHandler.obtainMessage();
                                        tmpMsg.what = 0;
                                        tmpHandler.sendMessage(tmpMsg);
                                    }
                                } catch (Exception e) {
                                    if (pObject != null && (pObject instanceof Handler)) {
                                        Handler tmpHandler2 = (Handler) pObject;
                                        Message tmpMsg2 = tmpHandler2.obtainMessage();
                                        tmpMsg2.what = 0;
                                        tmpHandler2.sendMessage(tmpMsg2);
                                    }
                                } catch (Throwable th) {
                                    if (pObject != null && (pObject instanceof Handler)) {
                                        Handler tmpHandler3 = (Handler) pObject;
                                        Message tmpMsg3 = tmpHandler3.obtainMessage();
                                        tmpMsg3.what = 0;
                                        tmpHandler3.sendMessage(tmpMsg3);
                                    }
                                    throw th;
                                }
                            }
                        });
                    } else if (paramString.equals("退出系统")) {
                        boolean tmpIsAlert = false;
                        if (PubVar.m_Workspace.CheckIfNeedSave()) {
                            tmpTipMsg = "项目中数据已经编辑,但没有保存.\r\n\r\n是否确定退出系统?";
                            tmpIsAlert = true;
                        } else {
                            tmpTipMsg = "是否确定退出系统?";
                        }
                        Common.ShowYesNoDialogWithAlert(this.m_Context, tmpTipMsg, tmpIsAlert, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Common.PubCommand.4
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject) {
                                if (paramString2.equals("YES")) {
                                    PubCommand.this.ProcessCommand("完全退出");
                                }
                            }
                        });
                    } else if (paramString.equals("开始注册系统")) {
                        About_Dialog tempDialog = new About_Dialog();
                        tempDialog.setReturnTag(this._Callback, "注册系统返回");
                        tempDialog.ShowDialog();
                    } else if (paramString.equals("完全退出")) {
                        Common.DisConnectServer();
                        Common.ShowProgressDialog("正在准备退出...", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Common.PubCommand.5
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject) {
                                try {
                                    if (PubVar._Map != null) {
                                        PubVar._Map.Refresh();
                                        Common.SaveImgFile(PubVar._PubCommand.m_ProjectDB.GetProjectManage().GetProjectDataPreviewImageName(), PubVar._Map.getMaskBitmap());
                                        PubCommand.this.ProcessCommand("项目_保存");
                                    }
                                    if (pObject != null && (pObject instanceof Handler)) {
                                        Handler tmpHandler = (Handler) pObject;
                                        Message tmpMsg = tmpHandler.obtainMessage();
                                        tmpMsg.what = 0;
                                        tmpHandler.sendMessage(tmpMsg);
                                    }
                                    MainActivity.ClearNotification(PubVar.MainContext);
                                    Process.killProcess(Process.myPid());
                                } catch (Exception e) {
                                    if (pObject != null && (pObject instanceof Handler)) {
                                        Handler tmpHandler2 = (Handler) pObject;
                                        Message tmpMsg2 = tmpHandler2.obtainMessage();
                                        tmpMsg2.what = 0;
                                        tmpHandler2.sendMessage(tmpMsg2);
                                    }
                                    MainActivity.ClearNotification(PubVar.MainContext);
                                    Process.killProcess(Process.myPid());
                                } catch (Throwable th) {
                                    if (pObject != null && (pObject instanceof Handler)) {
                                        Handler tmpHandler3 = (Handler) pObject;
                                        Message tmpMsg3 = tmpHandler3.obtainMessage();
                                        tmpMsg3.what = 0;
                                        tmpHandler3.sendMessage(tmpMsg3);
                                    }
                                    MainActivity.ClearNotification(PubVar.MainContext);
                                    Process.killProcess(Process.myPid());
                                    throw th;
                                }
                            }
                        });
                    } else if (paramString.equals("更多设置")) {
                        new ToolsManager_Dialog().ShowDialog();
                    } else if (paramString.equals("系统更新")) {
                        final UpdateManager tempUpdateManager = new UpdateManager(this.m_Context);
                        Common.ShowProgressDialog("正在检测软件最新版本...", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Common.PubCommand.6
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject) {
                                try {
                                    tempUpdateManager.CheckUpdate(1);
                                    if (pObject != null && (pObject instanceof Handler)) {
                                        Handler tmpHandler = (Handler) pObject;
                                        Message tmpMsg = tmpHandler.obtainMessage();
                                        tmpMsg.what = 0;
                                        tmpHandler.sendMessage(tmpMsg);
                                    }
                                } catch (Exception e) {
                                    if (pObject != null && (pObject instanceof Handler)) {
                                        Handler tmpHandler2 = (Handler) pObject;
                                        Message tmpMsg2 = tmpHandler2.obtainMessage();
                                        tmpMsg2.what = 0;
                                        tmpHandler2.sendMessage(tmpMsg2);
                                    }
                                } catch (Throwable th) {
                                    if (pObject != null && (pObject instanceof Handler)) {
                                        Handler tmpHandler3 = (Handler) pObject;
                                        Message tmpMsg3 = tmpHandler3.obtainMessage();
                                        tmpMsg3.what = 0;
                                        tmpHandler3.sendMessage(tmpMsg3);
                                    }
                                    throw th;
                                }
                            }
                        });
                    } else if (paramString.equals("地图缓存")) {
                        RasterLayers_Select_Dialog tempDialog2 = new RasterLayers_Select_Dialog();
                        tempDialog2.SetFormType(1);
                        tempDialog2.ShowDialog();
                    } else if (paramString.equals("参数校正")) {
                        TranslateSetting_Dialog tempDialog3 = new TranslateSetting_Dialog();
                        tempDialog3.SetPMTranslate(PubVar.m_Workspace.GetCoorSystem().GetPMTranslate());
                        tempDialog3.ShowDialog();
                    } else if (paramString.equals("关于系统")) {
                        new About_Dialog().ShowDialog();
                    } else if (paramString.equals("蓝牙设置")) {
                        new BluetoothGPSSelectDialog(this.m_Context).ShowDialog();
                    } else if (paramString.equals("系统设置")) {
                        new SystemSettingParas_Dialog().ShowDialog();
                    } else if (paramString.equals("地图比例尺显示设置")) {
                        new ScaleSetting_Dialog().ShowDialog();
                    } else if (paramString.equals("添加节点")) {
                        this.m_mapView.setActiveTool(MapTools.AddVertex);
                    } else if (paramString.equals("移动节点")) {
                        this.m_mapView.setActiveTool(MapTools.MoveVertex);
                    } else if (paramString.equals("删除节点")) {
                        this.m_mapView.setActiveTool(MapTools.DelVertex);
                    } else if (paramString.equals("显示放大镜")) {
                        ShowMagnifier();
                    } else if (paramString.equals("关闭放大镜")) {
                        CloseMagnifier();
                    } else if (paramString.equals("采集图层管理")) {
                        new FeatureLayers_Manage_Dialog().ShowDialog();
                    } else if (paramString.equals("在线帮助")) {
                        new Help_Dialog().ShowDialog();
                    } else if (paramString.equals("森林督查模块")) {
                        PubVar._PubCommand.ShowToolbar("森林督查工具栏");
                    }
                }
            } else if (arrayOfString.length == 2) {
                String tempCommand01 = arrayOfString[0];
                String tempCommand02 = arrayOfString[1];
                if (tempCommand01.equals("项目")) {
                    if (tempCommand02.equals("选择")) {
                        new Project_Select_Dialog().ShowDialog();
                    } else if (tempCommand02.equals("打开")) {
                        if (PubVar.m_IsEnable) {
                            VectorLayerWorkspace tmpBKVLayerManage = this.m_ProjectDB.GetBKVectorLayerManage();
                            LayerWorkspace tmpLayerManage = this.m_ProjectDB.GetLayerManage();
                            LayerRenderWorkspace tmpLayerRenderManage = this.m_ProjectDB.GetLayerRenderManage();
                            ProjectWorkspace tmpProjectManage = this.m_ProjectDB.GetProjectManage();
                            C0542Workspace tmpWorkspace = new C0542Workspace();
                            PubVar.m_Workspace = tmpWorkspace;
                            tmpWorkspace.SetProjectPWD(PubVar._PubCommand.m_ProjectDB.getPassword());
                            tmpWorkspace.SetCoorSystemInfo(tmpProjectManage.GetCoorSystem());
                            tmpWorkspace.OpenEditDataSource(tmpProjectManage.GetProjectDataFileName());
                            Map localMap = new Map(PubVar._MapView);
                            PubVar._Map = localMap;
                            localMap.SetScaleBar(PubVar._PubCommand.m_ScaleBar);
                            if (PubVar.Map_Display_Cache) {
                                int tmpWH = PubVar._MapView.getWidth();
                                if (tmpWH > PubVar._MapView.getHeight()) {
                                    tmpWH = PubVar._MapView.getHeight();
                                }
                                PubVar._Map.setMaskBias((float) (tmpWH / 2), (float) (tmpWH / 2));
                                PubVar._Map.setSize(PubVar._MapView.getMapSize());
                                PubVar._Map.Refresh();
                            } else {
                                PubVar._Map.setMaskBias(0.0f, 0.0f);
                                PubVar._Map.setSize(PubVar._MapView.getMapSize());
                                PubVar._Map.Refresh();
                            }
                            tmpBKVLayerManage.OpenDataSource();
                            RasterLayerWorkspace rasterLayerManage = PubVar._PubCommand.m_ProjectDB.GetRasterLayerManage();
                            for (RasterLayer rasterLayer : rasterLayerManage.GetLayerList()) {
                                rasterLayerManage.RenderLayerForAdd(rasterLayer);
                            }
                            tmpBKVLayerManage.Initial();
                            for (FeatureLayer featureLayer : tmpLayerManage.GetLayerList()) {
                                tmpLayerRenderManage.RenderLayerForNew(featureLayer);
                            }
                            for (DataSet tempDataset : tmpWorkspace.GetDataSourceByEditing().getDatasets()) {
                                tempDataset.BuildMapIndex(true, false);
                            }
                            localMap.InitialSelections();
                            ProcessCommand("全屏尺寸");
                            HashMap tempHash = this.m_ProjectDB.GetProjectConfigDB().GetUserPara("上次视图范围");
                            if (tempHash != null) {
                                String[] tempStrs = tempHash.get("F3").toString().split(",");
                                if (tempStrs != null && tempStrs.length > 3) {
                                    PubVar._Map.setFullExtend(new Envelope(Double.parseDouble(tempStrs[0]), Double.parseDouble(tempStrs[1]), Double.parseDouble(tempStrs[2]), Double.parseDouble(tempStrs[3])));
                                }
                                String[] tempStrs2 = tempHash.get("F1").toString().split(",");
                                if (tempStrs2 != null && tempStrs2.length > 3) {
                                    double tmpXMin = Double.parseDouble(tempStrs2[0]);
                                    double tmpYMax = Double.parseDouble(tempStrs2[1]);
                                    double tmpXMax = Double.parseDouble(tempStrs2[2]);
                                    double tmpYMin = Double.parseDouble(tempStrs2[3]);
                                    if (this.m_OpenProjectMode == 1) {
                                        tmpYMax = tmpYMin + 1.0d;
                                        tmpXMax = tmpXMin + 1.0d;
                                    }
                                    PubVar._Map.setExtend(new Envelope(tmpXMin, tmpYMax, tmpXMax, tmpYMin));
                                }
                            }
                            ProcessCommand("刷新地图");
                            ProcessCommand("自由缩放");
                            if (PubVar.Module_SenLinDuCha) {
                                 com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.CommonSetting.InitialSetting();
                            }
                            CommonSetting.InitialSetting();
                            PubVar.m_Callback.OnClick("更新窗体显示", null);
                        }
                    } else if (tempCommand02.equals("保存")) {
                        new HashMap();
                        Coordinate tempCoord01 = PubVar._Map.getExtend().getLeftTop();
                        Coordinate tempCoord02 = PubVar._Map.getExtend().getRightBottom();
                        HashMap tempHash2 = new HashMap();
                        tempHash2.put("F1", String.valueOf(String.valueOf(tempCoord01.getX())) + "," + String.valueOf(tempCoord01.getY()) + "," + String.valueOf(tempCoord02.getX()) + "," + String.valueOf(tempCoord02.getY()));
                        Coordinate tempCoord012 = PubVar._Map.getFullExtend().getLeftTop();
                        Coordinate tempCoord022 = PubVar._Map.getFullExtend().getRightBottom();
                        tempHash2.put("F3", String.valueOf(String.format("%f", Double.valueOf(tempCoord012.getX()))) + "," + String.format("%f", Double.valueOf(tempCoord012.getY())) + "," + String.format("%f", Double.valueOf(tempCoord022.getX())) + "," + String.format("%f", Double.valueOf(tempCoord022.getY())));
                        this.m_ProjectDB.GetProjectConfigDB().SaveUserPara("上次视图范围", tempHash2);
                        this.m_ProjectDB.GetProjectManage().SaveProjectInfo();
                        PubVar.m_Callback.OnClick("保存窗体参数", null);
                    }
                } else if (tempCommand01.equals("GPS")) {
                    if (tempCommand02.equals("打开GPS")) {
                        CreateGPSDevice();
                    } else if (tempCommand02.equals("关闭GPS")) {
                        PubVar._PubCommand.m_GPSDevice.StopGPSDevice();
                    }
                } else if (tempCommand01.equals("选择")) {
                    if (tempCommand02.equals("删除")) {
                        boolean tempBool01 = this.m_mapView._Select.getSeletected();
                        this.m_mapView._Select.SetSeletected(false);
                        if (this.m_Delete == null) {
                            this.m_Delete = new DeleteObject();
                        }
                        if (this.m_Delete.DeleteAllSelected()) {
                            this.m_mapView._Select.ClearAllSelection();
                            PubVar._Map.RefreshFast();
                        }
                        this.m_mapView._Select.SetSeletected(tempBool01);
                    } else if (tempCommand02.equals("清空选择集")) {
                        this.m_mapView._Select.ClearAllSelection();
                        PubVar._Map.RefreshFast();
                    } else if (tempCommand02.equals("仅清空选择集")) {
                        this.m_mapView._Select.ClearAllSelection();
                    } else if (tempCommand02.equals("取消选择")) {
                        this.m_mapView.setActiveTool(MapTools.None);
                        UpdateAllToolbarStatusNotSelected("地图缩放工具栏;图层工具栏");
                    }
                } else if (tempCommand01.equals("测量")) {
                    if (tempCommand02.equals("点坐标")) {
                        this.m_Measure.Clear();
                        this.m_Measure.SetMode(0);
                        this.m_mapView.setActiveTools(MapTools.AddPolyline, this.m_Measure, this.m_Measure);
                        this.m_mapView.invalidate();
                    } else if (tempCommand02.equals("长度与面积")) {
                        this.m_Measure.SetMode(1);
                        this.m_mapView.setActiveTools(MapTools.AddPolyline, this.m_Measure, this.m_Measure);
                        this.m_mapView.invalidate();
                    } else if (tempCommand02.equals("自由绘制")) {
                        this.m_Measure.SetMode(2);
                        this.m_mapView.setActiveTools(MapTools.AddPolyline, this.m_Measure, this.m_Measure);
                        this.m_mapView.invalidate();
                    } else if (tempCommand02.equals("取消上一点")) {
                        String[] tempOutMsg = new String[1];
                        if (this.m_Measure.UndoLastPoint(tempOutMsg)) {
                            this.m_mapView.invalidate();
                        } else {
                            Common.ShowToast(tempOutMsg[0]);
                        }
                    } else if (tempCommand02.equals("重做上一点")) {
                        String[] tempOutMsg2 = new String[1];
                        if (this.m_Measure.RedoLastPoint(tempOutMsg2)) {
                            this.m_mapView.invalidate();
                        } else {
                            Common.ShowToast(tempOutMsg2[0]);
                        }
                    } else if (tempCommand02.equals("清空")) {
                        this.m_Measure.Clear();
                        this.m_mapView.setActiveTools(MapTools.AddPolyline, this.m_Measure, this.m_Measure);
                        this.m_mapView.invalidate();
                    }
                } else if (tempCommand01.equals("采集")) {
                    if (tempCommand02.equals("手绘点")) {
                        BaseToolbar tmpToolbar = GetToolbarByName("点编辑工具栏");
                        if (tmpToolbar != null) {
                            if (this.m_PointEdit == null) {
                                this.m_PointEdit = GetPointEdit();
                            }
                            this.m_PointEdit.SetToolbar((Toolbar_Point) tmpToolbar);
                            this.m_mapView.setActiveTools(MapTools.AddPoint, this.m_PointEdit, this.m_PointEdit);
                            this.m_mapView._CurrentEditPaint = this.m_PointEdit;
                        }
                    } else if (tempCommand02.equals("采集线")) {
                        BaseToolbar tmpToolbar2 = GetToolbarByName("线编辑工具栏");
                        if (tmpToolbar2 != null) {
                            ((Toolbar_Polyline) tmpToolbar2).SetEditLine(GetLineEdit());
                            this.m_mapView.setActiveTools(MapTools.AddPolyline, this.m_LineEdit, this.m_LineEdit);
                            this.m_mapView._CurrentEditPaint = this.m_LineEdit;
                        }
                    } else if (tempCommand02.equals("采集面")) {
                        BaseToolbar tmpToolbar3 = GetToolbarByName("面编辑工具栏");
                        if (tmpToolbar3 != null) {
                            ((Toolbar_Polygon) tmpToolbar3).SetEditPolygon(GetPolygonEdit());
                            this.m_mapView.setActiveTools(MapTools.AddPolygon, this.m_PolygonEdit, this.m_PolygonEdit);
                            this.m_mapView._CurrentEditPaint = this.m_PolygonEdit;
                        }
                    } else if (tempCommand02.equals("结束")) {
                        this.m_mapView._CurrentEditPaint = null;
                    }
                }
            } else {
                int length = arrayOfString.length;
            }
        }
    }

    public void CreateGPSDevice() {
        try {
            if (this.m_GPSDevice != null && !this.m_GPSDevice.isDisposed) {
                this.m_GPSDevice.StopGPSDevice();
            }
            if (this.m_GPSDevice != null && this.m_GPSDevice.isDisposed) {
                this.m_GPSDevice = null;
            }
            if (this.m_GPSDevice == null) {
                this.m_GPSDevice = GPSDevice.OpenGPSDevice(this.m_Context, this.m_GpsLocation);
            }
        } catch (Exception e) {
        }
    }

    public void UpdateAllToolbarStatusNotSelected(String exceptToolbar) {
        if (exceptToolbar == null || exceptToolbar.equals("")) {
            if (this.m_Toolbars.size() > 0) {
                for (BaseToolbar tempToolbar : this.m_Toolbars) {
                    tempToolbar.SetButtonSelectedStatus(null, false);
                }
            }
            if (this.m_MBExpandMoreTools != null) {
                this.m_MBExpandMoreTools.SetButtonsSelectedStatus("解锁GPS", false);
                this.m_MBExpandMoreTools.SetButtonsSelectedStatus("框选缩放", false);
            }
        } else if (this.m_Toolbars.size() > 0) {
            for (BaseToolbar tempToolbar2 : this.m_Toolbars) {
                if (!exceptToolbar.contains(tempToolbar2.getToolbarName())) {
                    tempToolbar2.SetButtonSelectedStatus(null, false);
                }
            }
        }
    }

    public void UpdateToolbarBtnSelected(String toolbar, String btnName, boolean isSelected) {
        if (!toolbar.equals("地图缩放工具栏")) {
            BaseToolbar tempToolbar = GetToolbarByName(toolbar);
            if (tempToolbar != null) {
                tempToolbar.SetButtonSelectedStatus(btnName, isSelected);
            }
        } else if (this.m_MBExpandMoreTools != null) {
            this.m_MBExpandMoreTools.SetButtonSelectedStatus(btnName, isSelected);
        }
    }

    public void JustUpdateToolbarBtnIsSelected(String toolbar, String btnName, String exceptToolbar) {
        UpdateAllToolbarStatusNotSelected(exceptToolbar);
        UpdateToolbarBtnSelected(toolbar, btnName, true);
    }

    public void ShowToolbar(String toolbarName) {
        try {
            int tempIndex = this.m_ToolbarNames.indexOf(toolbarName);
            if (tempIndex >= 0) {
                this.m_Toolbars.get(tempIndex).Show();
                return;
            }
            BaseToolbar tempToolbar = null;
            if (toolbarName.equals("选择工具栏")) {
                tempToolbar = new Toolbar_Select(this.m_Context, PubVar._MapView);
                View view = LayoutInflater.from(this.m_Context).inflate(R.layout.x_toolbar_select, (ViewGroup) null);
                ((LinearLayout) this.m_MainLayout.findViewById(R.id.ll_toolbarGroup)).addView(view, 0);
                tempToolbar.LoadToolBar(view.findViewById(R.id.toolbar_select), R.id.ll_toolbar_select);
                tempToolbar.Show();
            } else if (toolbarName.equals("测量工具栏")) {
                tempToolbar = new Toolbar_Measure(this.m_Context, PubVar._MapView);
                View view2 = LayoutInflater.from(this.m_Context).inflate(R.layout.x_toolbar_measure, (ViewGroup) null);
                ((LinearLayout) this.m_MainLayout.findViewById(R.id.ll_toolbarGroup)).addView(view2, 0);
                tempToolbar.LoadToolBar(view2.findViewById(R.id.toolbar_measure), R.id.ll_toolbar_measure);
                tempToolbar.Show();
            } else if (!toolbarName.equals("图层工具栏")) {
                if (toolbarName.equals("点编辑工具栏")) {
                    tempToolbar = new Toolbar_Point(this.m_Context, PubVar._MapView);
                    View view3 = LayoutInflater.from(this.m_Context).inflate(R.layout.x_toolbar_point, (ViewGroup) null);
                    ((LinearLayout) this.m_MainLayout.findViewById(R.id.ll_toolbarGroup)).addView(view3, 0);
                    tempToolbar.LoadToolBar(view3.findViewById(R.id.toolbar_point));
                    tempToolbar.Show();
                } else if (toolbarName.equals("线编辑工具栏")) {
                    tempToolbar = new Toolbar_Polyline(this.m_Context, PubVar._MapView);
                    View view4 = LayoutInflater.from(this.m_Context).inflate(R.layout.x_toolbar_line, (ViewGroup) null);
                    ((LinearLayout) this.m_MainLayout.findViewById(R.id.ll_toolbarGroup)).addView(view4, 0);
                    tempToolbar.LoadToolBar(view4.findViewById(R.id.toolbar_line), R.id.ll_toolbar_line);
                    tempToolbar.Show();
                } else if (toolbarName.equals("面编辑工具栏")) {
                    tempToolbar = new Toolbar_Polygon(this.m_Context, PubVar._MapView);
                    View view5 = LayoutInflater.from(this.m_Context).inflate(R.layout.x_toolbar_polygon, (ViewGroup) null);
                    ((LinearLayout) this.m_MainLayout.findViewById(R.id.ll_toolbarGroup)).addView(view5, 0);
                    tempToolbar.LoadToolBar(view5.findViewById(R.id.toolbar_polygon), R.id.ll_toolbar_polygon);
                    tempToolbar.Show();
                } else if (toolbarName.equals("足迹工具栏")) {
                    tempToolbar = new Toolbar_Track(this.m_Context, PubVar._MapView);
                    View view6 = LayoutInflater.from(this.m_Context).inflate(R.layout.x_toolbar_track, (ViewGroup) null);
                    ((LinearLayout) this.m_MainLayout.findViewById(R.id.ll_toolbarGroup)).addView(view6, 0);
                    tempToolbar.LoadToolBar(view6.findViewById(R.id.toolbar_track), R.id.ll_toolbar_track);
                    tempToolbar.Show();
                } else if (!toolbarName.equals("面编辑附加工具栏") && !toolbarName.equals("线编辑附加工具栏")) {
                    if (toolbarName.equals("工具工具栏")) {
                        tempToolbar = new Toolbar_Tools(this.m_Context, PubVar._MapView);
                        View view7 = LayoutInflater.from(this.m_Context).inflate(R.layout.x_toolbar_tools, (ViewGroup) null);
                        ((LinearLayout) this.m_MainLayout.findViewById(R.id.ll_toolbarGroup)).addView(view7, 0);
                        tempToolbar.LoadToolBar(view7.findViewById(R.id.toolbar_tools), R.id.ll_toolbar_tools);
                        tempToolbar.Show();
                    } else if (toolbarName.equals("工具云中心")) {
                        tempToolbar = new Toolbar_Cloud(this.m_Context, PubVar._MapView);
                        View view8 = LayoutInflater.from(this.m_Context).inflate(R.layout.x_toolbar_cloud, (ViewGroup) null);
                        ((LinearLayout) this.m_MainLayout.findViewById(R.id.ll_toolbarGroup)).addView(view8, 0);
                        tempToolbar.LoadToolBar(view8.findViewById(R.id.toolbar_cloud), R.id.ll_toolbar_cloud);
                        tempToolbar.Show();
                    } else if (toolbarName.equals("森林督查工具栏")) {
                        tempToolbar = new Toolbar_SenLinDuCha(this.m_Context, PubVar._MapView);
                        View view9 = LayoutInflater.from(this.m_Context).inflate(R.layout.x_toolbar_senlinducha, (ViewGroup) null);
                        ((LinearLayout) this.m_MainLayout.findViewById(R.id.ll_toolbarGroup)).addView(view9, 0);
                        tempToolbar.LoadToolBar(view9.findViewById(R.id.toolbar_senlinducha), R.id.ll_toolbar_senlinducha);
                        tempToolbar.Show();
                    }
                }
            }
            if (tempToolbar != null) {
                this.m_Toolbars.add(tempToolbar);
                this.m_ToolbarNames.add(toolbarName);
            }
        } catch (Exception ex) {
            Common.Log("ShowToolbar", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    public void RemoveToolbar(String toolbarName) {
        int tempIndex = this.m_ToolbarNames.indexOf(toolbarName);
        if (tempIndex >= 0) {
            this.m_Toolbars.remove(tempIndex);
            this.m_ToolbarNames.remove(tempIndex);
        }
    }

    public void HideToolbar(String toolbarName) {
        int tempIndex = this.m_ToolbarNames.indexOf(toolbarName);
        if (tempIndex >= 0) {
            this.m_Toolbars.get(tempIndex).Hide();
        }
    }

    public void SetToolbarEnable(String toolbarName, boolean isEnable) {
        int tempIndex = this.m_ToolbarNames.indexOf(toolbarName);
        if (tempIndex < 0) {
            return;
        }
        if (isEnable) {
            this.m_Toolbars.get(tempIndex).Enable();
        } else {
            this.m_Toolbars.get(tempIndex).Disable();
        }
    }

    public BaseToolbar GetToolbarByName(String toolbarName) {
        int tempIndex = this.m_ToolbarNames.indexOf(toolbarName);
        if (tempIndex >= 0) {
            return this.m_Toolbars.get(tempIndex);
        }
        return null;
    }

    public boolean IsToolbarShow(String toolbarName) {
        int tempIndex = this.m_ToolbarNames.indexOf(toolbarName);
        if (tempIndex >= 0) {
            return this.m_Toolbars.get(tempIndex).IsVisiable();
        }
        return false;
    }

    public void SaveToolbarPosition() {
        try {
            for (BaseToolbar baseToolbar : this.m_Toolbars) {
                baseToolbar.SaveConfigDB();
            }
        } catch (Exception e) {
        }
    }

    public List<BaseToolbar> getToolbarList() {
        return this.m_Toolbars;
    }

    public String GetCurrentProjectPath() {
        return this.m_ProjectDB.ProjectPath;
    }

    public PointEditClass GetPointEdit() {
        if (this.m_PointEdit == null) {
            this.m_PointEdit = new PointEditClass(PubVar._MapView);
        }
        return this.m_PointEdit;
    }

    public PolylineEditClass GetLineEdit() {
        if (this.m_LineEdit == null) {
            this.m_LineEdit = new PolylineEditClass(PubVar._MapView);
        }
        return this.m_LineEdit;
    }

    public PolygonEditClass GetPolygonEdit() {
        if (this.m_PolygonEdit == null) {
            this.m_PolygonEdit = new PolygonEditClass(PubVar._MapView);
        }
        return this.m_PolygonEdit;
    }

    public TrackLine GetTrackLine() {
        if (this.m_TrackLine == null) {
            this.m_TrackLine = new TrackLine();
        }
        return this.m_TrackLine;
    }

    public C0706Navigation GetNavigation() {
        if (this.m_Navigation == null) {
            this.m_Navigation = new C0706Navigation();
        }
        return this.m_Navigation;
    }

    public DeleteObject GetDeleteObject() {
        if (this.m_Delete == null) {
            this.m_Delete = new DeleteObject();
        }
        return this.m_Delete;
    }

    public TextPositionAdjust TextPositionAdjustT() {
        return this.TextPositionAdjustT;
    }

    public void RefreshMagniferMapView() {
        if (this.m_Magnifer != null && this.m_Magnifer._IsVisible) {
            this.m_Magnifer.updateMapView();
        }
    }

    @SuppressLint("WrongConstant")
    public void ShowMagnifier() {
        if (this.m_Magnifer == null) {
            this.m_Magnifer = new MagnifierView(PubVar.MainContext, PubVar._Map);
            int tmpWidth = (int) (((float) PubVar.ScreenWidth) * 0.4f);
            this.m_Magnifer.setLayoutParams(new RelativeLayout.LayoutParams(tmpWidth, tmpWidth));
            this.m_Magnifer.setX(0.0f);
            this.m_Magnifer.setY(0.0f);
            this.m_Magnifer.setVisibility(0);
            PubVar._PubCommand.m_MainLayout.addView(this.m_Magnifer, 1);
            this.m_Magnifer._IsVisible = true;
            this.m_Magnifer.updateMapView();
            this.m_Magnifer.updateView();
            return;
        }
        this.m_Magnifer.setVisibility(0);
        this.m_Magnifer._IsVisible = true;
        this.m_Magnifer.updateMapView();
        this.m_Magnifer.updateView();
    }

    @SuppressLint("WrongConstant")
    public void CloseMagnifier() {
        if (this.m_Magnifer != null) {
            this.m_Magnifer.setVisibility(8);
            this.m_Magnifer._IsVisible = false;
        }
    }

    public List<MapChild_Dialog> getChildMapList() {
        return this.m_ChildMapList;
    }

    public void RemoveChildMap(String uid) {
        for (MapChild_Dialog tmpDialog : this.m_ChildMapList) {
            if (tmpDialog != null && tmpDialog.getUID().equals(uid)) {
                this.m_ChildMapList.remove(tmpDialog);
                return;
            }
        }
    }

    public void UpdateMapSelections() {
        try {
            PubVar._Map.InitialSelections();
            if (this.m_ChildMapList.size() > 0) {
                for (MapChild_Dialog tmpDialog : this.m_ChildMapList) {
                    if (tmpDialog != null) {
                        tmpDialog.getMap().InitialSelections();
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void UpdateGPSStatus(GPSLocationClass gpsLoc) {
        try {
            if (PubVar._GPSDisplay != null) {
                PubVar._GPSDisplay.UpdateGPSStatus(gpsLoc);
            }
            if (this.m_ChildMapList.size() > 0) {
                for (MapChild_Dialog tmpDialog : this.m_ChildMapList) {
                    if (tmpDialog != null) {
                        tmpDialog.getGPSDisplay().UpdateGPSStatus(gpsLoc);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void UpdateLockChildMap() {
        try {
            if (this.m_ChildMapList.size() > 0) {
                for (MapChild_Dialog tmpDialog : this.m_ChildMapList) {
                    if (tmpDialog != null && tmpDialog.getIsLockMainMap()) {
                        tmpDialog.getMap().Refresh();
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public MapView getMapView() {
        return this.m_mapView;
    }

    public void AddCopyGraphic(GraphicSymbolGeometry graphicGeoemtry) {
        this.m_CopyGraphicsList.add(graphicGeoemtry);
    }

    public void ClearCopyGraphics() {
        this.m_CopyGraphicsList.clear();
    }

    public int getCopyGraphicsCount() {
        return this.m_CopyGraphicsList.size();
    }

    public List<GraphicSymbolGeometry> getCopyGraphicsList() {
        return this.m_CopyGraphicsList;
    }

    public List<GraphicSymbolGeometry> getCopyGraphicsListByType(EGeometryType geometryType) {
        List<GraphicSymbolGeometry> result = new ArrayList<>();
        for (GraphicSymbolGeometry tmpGraphicSymbolGeometry : this.m_CopyGraphicsList) {
            if (tmpGraphicSymbolGeometry._Geoemtry != null && tmpGraphicSymbolGeometry._Geoemtry.GetGeometryType() == geometryType) {
                result.add(tmpGraphicSymbolGeometry);
            }
        }
        return result;
    }

    public void SetCommonEvent(CommonEvent commonEvent) {
        this.m_CommonEvent = commonEvent;
        if (this.m_CommonEvent != null) {
            this.m_MapCompLayoutToolbar.setCommonEventButtonVisible(true);
        } else {
            this.m_MapCompLayoutToolbar.setCommonEventButtonVisible(false);
        }
    }

    public CommonEvent getCommonEvent() {
        return this.m_CommonEvent;
    }
}
