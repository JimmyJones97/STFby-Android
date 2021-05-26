package  com.xzy.forestSystem.gogisapi.Carto;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.GPS.GPSDisplay;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Data_MultiSelect_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog;
import  com.xzy.forestSystem.gogisapi.Geometry.Size;
import  com.xzy.forestSystem.gogisapi.Setting.FormSizeSetting_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.Layer_Select_Dialog;
import com.stczh.gzforestSystem.R;
import com.xzy.forestSystem.hellocharts.animation.ChartViewportAnimator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MapChild_Dialog extends PopupWindow {
    static final long BUTTON_CHANGLE_DIR_TIME = 200;
    static final float BUTTON_CLICK_BIAS = (PubVar.ScaledDensity * 3.0f);
    static final float BUTTON_CLOSE_SPEED = (PubVar.ScaledDensity / 2.0f);
    private int Height = 500;
    private View.OnClickListener ItemClickListener = new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Carto.MapChild_Dialog.3
        @SuppressLint("WrongConstant")
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            try {
                Object tempObj = view.getTag();
                if (tempObj != null) {
                    String tmpCommand = String.valueOf(tempObj);
                    if (tmpCommand.equals("设置")) {
                        FormSizeSetting_Dialog tmpDialog = new FormSizeSetting_Dialog();
                        tmpDialog.SetCallback(MapChild_Dialog.this.pCallback);
                        tmpDialog.ShowDialog();
                    } else if (tmpCommand.equals("关闭")) {
                        PubVar._PubCommand.RemoveChildMap(MapChild_Dialog.this._FormUID);
                        if (MapChild_Dialog.this._HeadDialog != null) {
                            MapChild_Dialog.this._HeadDialog.dismiss();
                        }
                        MapChild_Dialog.this.dismiss();
                    } else if (tmpCommand.equals("隐藏窗体")) {
                        MapChild_Dialog.this.m_Map.SetAllowedRefresh(false);
                        MapChild_Dialog.this._conentView.setVisibility(8);
                        MapChild_HeadDialog tmpDialog2 = MapChild_Dialog.this.getHeadDialog();
                        tmpDialog2.UpdatePosition(MapChild_Dialog.this._LastX, (MapChild_Dialog.this._LastY + MapChild_Dialog.this.Height) - tmpDialog2._HeadHeight);
                    } else if (tmpCommand.equals("显示窗体")) {
                        MapChild_Dialog.this.m_Map.SetAllowedRefresh(true);
                        MapChild_HeadDialog tmpDialog3 = MapChild_Dialog.this.getHeadDialog();
                        tmpDialog3.Hide();
                        MapChild_Dialog.this.UpdatePosition(tmpDialog3._LastX, (tmpDialog3._LastY - MapChild_Dialog.this.Height) + tmpDialog3._HeadHeight);
                    } else {
                        MapChild_Dialog.this.ProcessCommand(tmpCommand);
                    }
                }
            } catch (Exception e) {
            }
        }
    };
    private int StartX = 0;
    private int StartY = 0;
    private int Width = 500;
    private ImageButton _CollapseBtn = null;
    private TextView _CollapseTextView = null;
    private String _FormUID = UUID.randomUUID().toString();
    private MapChild_HeadDialog _HeadDialog = null;
    private boolean _IsInitial = false;
    private boolean _IsMoveToobar = false;
    private long _LastMouseDownTime = 0;
    private int _LastX = 0;
    private int _LastY = 0;
    private boolean _LockMainMap = false;
    private ICallback _MainCallback = null;
    private LinearLayout _MainRelativeLayout = null;
    private RelativeLayout _MapControlLayout = null;
    private List<String> _OutMsgList = new ArrayList();
    private View _ParentView = null;
    private String _Title = "";
    private TextView _TitleTextView = null;
    private View _conentView;
    private Context _context;
    private int _xDelta;
    private int _yDelta;
    private GPSDisplay m_GPSDisplay = null;
    private Map2 m_Map = null;
    private MapView m_mapView = null;
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Carto.MapChild_Dialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String command, Object object) {
            try {
                if (command.equals("窗体设置返回")) {
                    MapChild_Dialog.this.UpdatePosition();
                } else if (command.equals("SelectToolReturn")) {
                    if (object != null && String.valueOf(object).equals("属性")) {
                        MapChild_Dialog.this.ProcessCommand("属性");
                    }
                } else if (command.equals("编辑属性数据返回") || command.equals("编辑属性返回")) {
                    if (object != null) {
                        MapChild_Dialog.this.m_Map.RefreshFast();
                    }
                } else if (command.equals("选择图层")) {
                    String tmpSelectedLayers = String.valueOf(object);
                    if (!tmpSelectedLayers.equals("")) {
                        MapChild_Dialog.this.m_Map.SetSelectedLayers(tmpSelectedLayers);
                        MapChild_Dialog.this.m_Map.Refresh();
                    }
                }
            } catch (Exception e) {
            }
        }
    };
    View.OnTouchListener touchListener = new View.OnTouchListener() { // from class:  com.xzy.forestSystem.gogisapi.Carto.MapChild_Dialog.2
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent event) {
            try {
                int X = (int) event.getRawX();
                int Y = (int) event.getRawY();
                switch (event.getAction()) {
                    case 0:
                        MapChild_Dialog.this._IsMoveToobar = false;
                        MapChild_Dialog.this.StartX = X;
                        MapChild_Dialog.this.StartY = Y;
                        MapChild_Dialog.this._LastMouseDownTime = event.getDownTime();
                        break;
                    case 1:
                        MapChild_Dialog.this._IsMoveToobar = false;
                        MapChild_Dialog.this._xDelta = X - MapChild_Dialog.this.StartX;
                        MapChild_Dialog.this._yDelta = Y - MapChild_Dialog.this.StartY;
                        MapChild_Dialog.this._LastX += MapChild_Dialog.this._xDelta;
                        MapChild_Dialog.this._LastY -= MapChild_Dialog.this._yDelta;
                        if (MapChild_Dialog.this._LastX < 0) {
                            MapChild_Dialog.this._LastX = 0;
                        }
                        if (MapChild_Dialog.this._LastY < 0) {
                            MapChild_Dialog.this._LastY = 0;
                        }
                        MapChild_Dialog.this.UpdatePosition(MapChild_Dialog.this._LastX, MapChild_Dialog.this._LastY);
                        break;
                    case 2:
                        if (MapChild_Dialog.this._IsMoveToobar || (((float) Math.abs(X - MapChild_Dialog.this.StartX)) > MapChild_Dialog.BUTTON_CLICK_BIAS && ((float) Math.abs(Y - MapChild_Dialog.this.StartY)) > MapChild_Dialog.BUTTON_CLICK_BIAS)) {
                            MapChild_Dialog.this._IsMoveToobar = true;
                            MapChild_Dialog.this._xDelta = X - MapChild_Dialog.this.StartX;
                            MapChild_Dialog.this._yDelta = Y - MapChild_Dialog.this.StartY;
                            MapChild_Dialog.this.StartX = X;
                            MapChild_Dialog.this.StartY = Y;
                            MapChild_Dialog.this._LastX += MapChild_Dialog.this._xDelta;
                            MapChild_Dialog.this._LastY -= MapChild_Dialog.this._yDelta;
                            if (MapChild_Dialog.this._LastX < 0) {
                                MapChild_Dialog.this._LastX = 0;
                            }
                            if (MapChild_Dialog.this._LastY < 0) {
                                MapChild_Dialog.this._LastY = 0;
                            }
                            MapChild_Dialog.this.UpdatePosition(MapChild_Dialog.this._LastX, MapChild_Dialog.this._LastY);
                            break;
                        }
                    case 6:
                        MapChild_Dialog.this._IsMoveToobar = false;
                        break;
                }
            } catch (Exception e) {
            }
            return false;
        }
    };

    public MapChild_Dialog(Context context, View parent) {
        this._context = context;
        this.Width = PubVar.ChildMapWidth;
        this.Height = PubVar.ChildMapHeight;
        if (this.Width < 300) {
            this.Width = ChartViewportAnimator.FAST_ANIMATION_DURATION;
        }
        if (this.Height < 300) {
            this.Height = ChartViewportAnimator.FAST_ANIMATION_DURATION;
        }
        OnCreate(parent);
    }

    public void SetCallback(ICallback callback) {
        this._MainCallback = callback;
    }

    @SuppressLint("WrongConstant")
    public void OnCreate(View parent) {
        this._ParentView = parent;
        this._conentView = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(R.layout.mapchild_dialog, (ViewGroup) null);
        setWidth(this.Width);
        setHeight(this.Height);
        setContentView(this._conentView);
        update();
        this._MainRelativeLayout = (LinearLayout) this._conentView.findViewById(R.id.ll_mapchild_dialog);
        this.m_mapView = new MapView(this._context);
        this._MapControlLayout = (RelativeLayout) this._conentView.findViewById(R.id.rl_mapchild_map);
        LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(0, 0);
        localLayoutParams.height = -2;
        localLayoutParams.width = -2;
        this.m_mapView.setLayoutParams(localLayoutParams);
        this._MainRelativeLayout.addView(this.m_mapView, 0);
        this.m_Map = new Map2(this.m_mapView);
        this.m_Map.SetBindMap(PubVar._Map);
        this.m_mapView._Select.SetCallback(this.pCallback);
        this.m_GPSDisplay = new GPSDisplay(this.m_mapView);
        this._conentView.findViewById(R.id.btn_ChildMap_Pan).setOnClickListener(this.ItemClickListener);
        this._conentView.findViewById(R.id.btn_ChildMap_ZoomIn).setOnClickListener(this.ItemClickListener);
        this._conentView.findViewById(R.id.btn_ChildMap_ZoomOut).setOnClickListener(this.ItemClickListener);
        this._conentView.findViewById(R.id.btn_ChildMap_Identify).setOnClickListener(this.ItemClickListener);
        this._conentView.findViewById(R.id.btn_ChildMap_Layers).setOnClickListener(this.ItemClickListener);
        this._conentView.findViewById(R.id.btn_ChildMap_Link).setOnClickListener(this.ItemClickListener);
        this._conentView.findViewById(R.id.btn_ChildMap_Close).setOnClickListener(this.ItemClickListener);
        this._conentView.findViewById(R.id.btn_ChildMap_GPS).setOnClickListener(this.ItemClickListener);
        this._CollapseBtn = (ImageButton) this._conentView.findViewById(R.id.btn_ChildMap_Collapse);
        this._CollapseBtn.setOnClickListener(this.ItemClickListener);
        this._TitleTextView = (TextView) this._conentView.findViewById(R.id.tv_childMap_Title);
        this._CollapseTextView = (TextView) this._conentView.findViewById(R.id.tv_childMap_Title2);
        this._CollapseTextView.setOnTouchListener(this.touchListener);
    }

    public boolean getIsLockMainMap() {
        return this._LockMainMap;
    }

    public void setSize(int width, int height) {
        this.Width = width;
        this.Height = height;
    }

    public void setTitle(String title) {
        this._Title = title;
        if (this._TitleTextView != null) {
            this._TitleTextView.setText(title);
        }
    }

    public String getTitle() {
        return this._Title;
    }

    public Map2 getMap() {
        return this.m_Map;
    }

    public GPSDisplay getGPSDisplay() {
        return this.m_GPSDisplay;
    }

    public String getUID() {
        return this._FormUID;
    }

    public void UpdatePosition() {
        UpdatePosition(this._LastX, this._LastY);
    }

    @SuppressLint("WrongConstant")
    public void UpdatePosition(int x, int y) {
        try {
            this._LastX = x;
            this._LastY = y;
            if (!this._IsInitial) {
                showAtLocation(this._ParentView, 83, this._LastX, this._LastY);
                this._IsInitial = true;
                update(this._LastX, this._LastY, this.Width, this.Height);
                this._IsInitial = true;
                int[] tmpInts = Common.GetViewSize(this._conentView.findViewById(R.id.ll_childmap_head));
                this.m_mapView.setActiveTool(MapTools.FullScreenSize);
                this.m_Map.setSize(new Size(this.Width, this.Height - tmpInts[3]));
                this.m_Map.setExtend(PubVar._Map.getExtend());
                ProcessCommand("自由缩放");
                return;
            }
            if (this._conentView.getVisibility() == 8) {
                this._conentView.setVisibility(0);
            }
            update(this._LastX, this._LastY, this.Width, this.Height);
        } catch (Exception e) {
        }
    }

    public void ProcessCommand(String command) {
        try {
            if (command.equals("无工具")) {
                this.m_mapView.setActiveTool(MapTools.None);
            } else if (command.equals("全屏")) {
                this.m_mapView.setActiveTool(MapTools.FullScreen);
            } else if (command.equals("全屏尺寸")) {
                this.m_mapView.setActiveTool(MapTools.FullScreenSize);
            } else if (command.equals("全图缩放")) {
                this.m_mapView.getMap().RefreshMapExtend();
                this.m_mapView.setActiveTool(MapTools.GlobalMap);
            } else if (command.equals("单击放大")) {
                this.m_mapView.SetZoomIn();
            } else if (command.equals("单击缩小")) {
                this.m_mapView.SetZoomOut();
            } else if (command.equals("自由缩放")) {
                this.m_mapView.setActiveTool(MapTools.ZoomInOutPan);
            } else if (command.equals("点击查询")) {
                this.m_mapView.setActiveTool(MapTools.Select);
                this.m_mapView._Select.SetSeletected(true);
                this.m_mapView._Select.SetIsSingleClick(true);
                this.m_mapView._Select.SetMultiSeletected(false);
            } else if (command.equals("单个属性")) {
                BasicValue layerIDParam = new BasicValue();
                BasicValue geoIndexParam = new BasicValue();
                BasicValue dataSourceNameParam = new BasicValue();
                if (!Common.GetSelectOneObjectInfo(this.m_Map, layerIDParam, geoIndexParam, dataSourceNameParam)) {
                    Common.ShowDialog("请选择需要查询的实体！");
                } else if (PubVar.m_Workspace.GetDataSourceByName(dataSourceNameParam.getString()) != null) {
                    FeatureAttribute_Dialog tmpFeatAttrDialog = new FeatureAttribute_Dialog();
                    tmpFeatAttrDialog.SetCallback(this.pCallback);
                    tmpFeatAttrDialog.SetGeometryIndex(layerIDParam.getString(), geoIndexParam.getInt(), dataSourceNameParam.getString());
                    tmpFeatAttrDialog.ShowDialog();
                }
            } else if (command.equals("属性")) {
                int tempCount = Common.GetSelectObjectsCount(this.m_Map);
                if (tempCount == 0) {
                    Common.ShowDialog("请先选择实体！");
                } else if (tempCount == 1) {
                    ProcessCommand("单个属性");
                } else {
                    Data_MultiSelect_Dialog tempDialog = new Data_MultiSelect_Dialog();
                    tempDialog.setMap(this.m_Map);
                    tempDialog.ShowDialog();
                }
            } else if (command.equals("图层")) {
                Layer_Select_Dialog tempDialog2 = new Layer_Select_Dialog();
                List tmpList = new ArrayList();
                tmpList.addAll(this.m_Map.getGeoLayers().getList());
                tmpList.addAll(this.m_Map.getVectorGeoLayers().getList());
                tmpList.addAll(this.m_Map.getRasterLayers());
                tempDialog2.SetLayersList(tmpList);
                tempDialog2.SetLayerSelectType(5);
                tempDialog2.SetTitle("分屏显示图层");
                tempDialog2.SetAllowMultiSelect(true);
                tempDialog2.SetSelectedLayers(this.m_Map.getSelectedLayers());
                tempDialog2.SetCallback(this.pCallback);
                tempDialog2.ShowDialog();
            } else if (command.equals("关联")) {
                this.m_Map.SetBindMap(PubVar._Map);
                this.m_Map.CloneShowSelections(PubVar._Map);
                this.m_Map.setExtend(PubVar._Map.getExtend());
                this.m_Map.RefreshClone();
            } else if (command.equals("与主地图锁定")) {
                this._LockMainMap = true;
            } else if (command.equals("取消与主地图锁定")) {
                this._LockMainMap = false;
            } else if (command.equals("GPS") && this.m_GPSDisplay != null) {
                if (!PubVar._PubCommand.m_GpsLocation.isOpen) {
                    Common.ShowDialog("GPS设备未开启,请先开启GPS设备.");
                } else if (!PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                    Common.ShowDialog("GPS定位中,请稍候...");
                } else {
                    this.m_GPSDisplay.UpdateGPSStatus(PubVar._PubCommand.m_GpsLocation);
                }
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private MapChild_HeadDialog getHeadDialog() {
        if (this._HeadDialog == null) {
            this._HeadDialog = new MapChild_HeadDialog(PubVar.MainContext, PubVar._PubCommand.m_MainLayout);
            this._HeadDialog.setTitle(this._Title);
        }
        return this._HeadDialog;
    }

    /* access modifiers changed from: package-private */
    public class MapChild_HeadDialog extends PopupWindow {
        private int StartX = 0;
        private int StartY = 0;
        private int _HeadHeight = 20;
        private boolean _IsInitial = false;
        private boolean _IsMoveToobar = false;
        private int _LastX = 0;
        private int _LastY = 0;
        private ICallback _MainCallback = null;
        private View _ParentView = null;
        private TextView _TitleTextView = null;
        private View _conentView;
        private Context _context;
        private int _xDelta;
        private int _yDelta;
        View.OnTouchListener touchListener = new View.OnTouchListener() { // from class:  com.xzy.forestSystem.gogisapi.Carto.MapChild_Dialog.MapChild_HeadDialog.1
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent event) {
                try {
                    int X = (int) event.getRawX();
                    int Y = (int) event.getRawY();
                    switch (event.getAction()) {
                        case 0:
                            MapChild_HeadDialog.this._IsMoveToobar = false;
                            MapChild_HeadDialog.this.StartX = X;
                            MapChild_HeadDialog.this.StartY = Y;
                            MapChild_Dialog.this._LastMouseDownTime = event.getDownTime();
                            break;
                        case 1:
                            MapChild_HeadDialog.this._IsMoveToobar = false;
                            MapChild_HeadDialog.this._xDelta = X - MapChild_HeadDialog.this.StartX;
                            MapChild_HeadDialog.this._yDelta = Y - MapChild_HeadDialog.this.StartY;
                            MapChild_HeadDialog.this._LastX += MapChild_HeadDialog.this._xDelta;
                            MapChild_HeadDialog.this._LastY -= MapChild_HeadDialog.this._yDelta;
                            if (MapChild_HeadDialog.this._LastX < 0) {
                                MapChild_HeadDialog.this._LastX = 0;
                            }
                            if (MapChild_HeadDialog.this._LastY < 0) {
                                MapChild_HeadDialog.this._LastY = 0;
                            }
                            MapChild_HeadDialog.this.UpdatePosition(MapChild_HeadDialog.this._LastX, MapChild_HeadDialog.this._LastY);
                            break;
                        case 2:
                            if (MapChild_HeadDialog.this._IsMoveToobar || (((float) Math.abs(X - MapChild_HeadDialog.this.StartX)) > MapChild_Dialog.BUTTON_CLICK_BIAS && ((float) Math.abs(Y - MapChild_HeadDialog.this.StartY)) > MapChild_Dialog.BUTTON_CLICK_BIAS)) {
                                MapChild_HeadDialog.this._IsMoveToobar = true;
                                MapChild_HeadDialog.this._xDelta = X - MapChild_HeadDialog.this.StartX;
                                MapChild_HeadDialog.this._yDelta = Y - MapChild_HeadDialog.this.StartY;
                                MapChild_HeadDialog.this.StartX = X;
                                MapChild_HeadDialog.this.StartY = Y;
                                MapChild_HeadDialog.this._LastX += MapChild_HeadDialog.this._xDelta;
                                MapChild_HeadDialog.this._LastY -= MapChild_HeadDialog.this._yDelta;
                                if (MapChild_HeadDialog.this._LastX < 0) {
                                    MapChild_HeadDialog.this._LastX = 0;
                                }
                                if (MapChild_HeadDialog.this._LastY < 0) {
                                    MapChild_HeadDialog.this._LastY = 0;
                                }
                                MapChild_HeadDialog.this.UpdatePosition(MapChild_HeadDialog.this._LastX, MapChild_HeadDialog.this._LastY);
                                break;
                            }
                        case 6:
                            MapChild_HeadDialog.this._IsMoveToobar = false;
                            break;
                    }
                } catch (Exception e) {
                }
                return false;
            }
        };

        public MapChild_HeadDialog(Context context, View parent) {
            this._context = context;
            OnCreate(parent);
        }

        public void SetCallback(ICallback callback) {
            this._MainCallback = callback;
        }

        @SuppressLint("WrongConstant")
        public void OnCreate(View parent) {
            this._ParentView = parent;
            this._conentView = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(R.layout.mapchild_head_dialog, (ViewGroup) null);
            setWidth(-2);
            setHeight(-2);
            setContentView(this._conentView);
            update();
            this._conentView.findViewById(R.id.btn_ChildMap2_Close).setOnClickListener(MapChild_Dialog.this.ItemClickListener);
            this._conentView.findViewById(R.id.btn_ChildMap2_Collapse).setOnClickListener(MapChild_Dialog.this.ItemClickListener);
            this._TitleTextView = (TextView) this._conentView.findViewById(R.id.tv_childMap2_Title);
            this._TitleTextView.setOnTouchListener(this.touchListener);
            int[] tmpInts = Common.GetViewSize(this._conentView);
            if (tmpInts != null) {
                this._HeadHeight = tmpInts[3];
            }
            if (this._HeadHeight <= 0) {
                this._HeadHeight = (int) (40.0f * PubVar.ScaledDensity);
            }
        }

        public void setTitle(String title) {
            if (this._TitleTextView != null) {
                this._TitleTextView.setText(title);
            }
        }

        public void UpdatePosition() {
            UpdatePosition(this._LastX, this._LastY);
        }

        @SuppressLint("WrongConstant")
        public void UpdatePosition(int x, int y) {
            try {
                this._LastX = x;
                this._LastY = y;
                if (!this._IsInitial) {
                    showAtLocation(this._ParentView, 83, this._LastX, this._LastY);
                    update(this._LastX, this._LastY, -1, -1);
                    this._IsInitial = true;
                    return;
                }
                if (this._conentView.getVisibility() == 8) {
                    this._conentView.setVisibility(0);
                }
                update(this._LastX, this._LastY, -1, -1);
            } catch (Exception e) {
            }
        }

        @SuppressLint("WrongConstant")
        public void Hide() {
            this._conentView.setVisibility(8);
        }
    }
}
