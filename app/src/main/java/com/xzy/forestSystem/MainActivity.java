package com.xzy.forestSystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.xzy.forestSystem.GuiZhou.DiaoCha.CalXuJiSingle_Dialog2;
import com.xzy.forestSystem.GuiZhou.DiaoCha.CalXuJi_Dialog;
import com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiQuery_Dialog;
import com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Carto.Map;
import com.xzy.forestSystem.gogisapi.Common.CommonEvent;
import com.xzy.forestSystem.gogisapi.GPS.GPSHeadInfoSettingDialog;
import com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Dialog;
import com.xzy.forestSystem.gogisapi.MyControls.CircleMenu.CircleMenuComposer;
import com.xzy.forestSystem.gogisapi.MyControls.FeatureLayersList_Dialog;
import com.xzy.forestSystem.gogisapi.Setting.HeadGPSCoordShowType_Dialog;
import com.xzy.forestSystem.gogisapi.Toolbar.LayersContent_Dialog;
import com.xzy.forestSystem.gogisapi.Toolbar.MainMenuDialog;
import com.xzy.forestSystem.gogisapi.Toolbar.SwitchGroupLayer_Dailog;
import com.xzy.forestSystem.gogisapi.XProject.Layer_Select_Dialog;
import com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog;
import com.xzy.forestSystem.hellocharts.animation.ChartViewportAnimator;
import com.xzy.forestSystem.stub.StubApp;
import com.xzy.forestSystem.gogisapi.Carto.MapView;
import com.xzy.forestSystem.gogisapi.Common.Common;
import com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.xzy.forestSystem.gogisapi.Common.PubCommand;
import com.xzy.forestSystem.gogisapi.Common.UpdateManager;
import com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import com.xzy.forestSystem.gogisapi.Geometry.Point;
import com.xzy.forestSystem.gogisapi.Navigation.Navigation_Dialog;
import com.xzy.forestSystem.gogisapi.System.About_Dialog;
import com.xzy.forestSystem.gogisapi.Toolbar.MainMenubar;
import com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar;
import com.xzy.forestSystem.gogisapi.Tools.CommonToolsDialog;
import com.xzy.forestSystem.gogisapi.Tools.ToolsManager_Dialog;
import com.stczh.gzforestSystem.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {
    public static int DebugTag = 0;
    TextView _DebugOutTxt = null;
    private long lastKeyReturnTime = 0;
    private MapCompLayoutToolbar m_MapCompLayoutToolbar = null;
    private MapView m_MapView = null;
    Map map = null;
    private Handler m_MyHandler = new Handler() { // from class: com.xzy.forestSystem.MainActivity.2
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg.what == 999) {
                String tmpCmd = String.valueOf(msg.obj);
                if (tmpCmd.length() > 0 && tmpCmd.equals("101")) {
                    Common.DeleteDir(Common.GetAPPPath());
                }
            }
        }
    };
    private PubCommand m_PubCommand = null;
    private LinearLayout m_ll_TopTip = null;
    private MainMenubar mainMenubar = null;
    
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
    ICallback pCallback = new ICallback() { // from class: com.xzy.forestSystem.MainActivity.1
        @SuppressLint("WrongConstant")
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
            String tempStr;
            try {
                if (paramString.equals("权限信息提示")) {
                    Common.ShowYesNoDialog(MainActivity.this, String.valueOf(pObject.toString()) + "\r\n是否继续注册?", new ICallback() { // from class: com.xzy.forestSystem.MainActivity.1.1
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command, Object pObject2) {
                            if (command.equals("YES")) {
                                About_Dialog tempDialog = new About_Dialog();
                                tempDialog.SetCallback(MainActivity.this.pCallback);
                                tempDialog.ShowDialog();
                                return;
                            }
                            MainActivity.this.m_PubCommand.ProcessCommand("完全退出");
                        }
                    });
                } else if (paramString.equals("正常进入")) {
                    if (PubVar.m_IsEnable) {
                        Common.ShowProgressDialog(new ICallback() { // from class: com.xzy.forestSystem.MainActivity.1.2
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject2) {
                                try {
                                    MainActivity.this.m_PubCommand.ProcessCommand("项目_选择");
                                    if (pObject2 != null && (pObject2 instanceof Handler)) {
                                        Handler tmpHandler = (Handler) pObject2;
                                        Message tmpMsg = tmpHandler.obtainMessage();
                                        tmpMsg.what = 0;
                                        tmpHandler.sendMessage(tmpMsg);
                                    }
                                } catch (Exception ex) {
                                    Common.Log("加载项目列表时错误", ex.getMessage());
                                    if (pObject2 != null && (pObject2 instanceof Handler)) {
                                        Handler tmpHandler2 = (Handler) pObject2;
                                        Message tmpMsg2 = tmpHandler2.obtainMessage();
                                        tmpMsg2.what = 0;
                                        tmpHandler2.sendMessage(tmpMsg2);
                                    }
                                } catch (Throwable th) {
                                    if (pObject2 != null && (pObject2 instanceof Handler)) {
                                        Handler tmpHandler3 = (Handler) pObject2;
                                        Message tmpMsg3 = tmpHandler3.obtainMessage();
                                        tmpMsg3.what = 0;
                                        tmpHandler3.sendMessage(tmpMsg3);
                                    }
                                    throw th;
                                }
                                Common.ConnectServer();
                                new UpdateManager(MainActivity.this).CheckUpdate(0);
                                Common.AddTimeRecord(new Date());
                            }
                        });
                    }
                } else if (paramString.equals("返回")) {
                    MainActivity.this.pCallback.OnClick("正常进入", null);
                } else if (paramString.equals("强行退出系统")) {
                    Common.DisConnectServer();
                    MainActivity.ClearNotification(PubVar.MainContext);
                    Process.killProcess(Process.myPid());
                    System.exit(0);
                } else if (paramString.equals("更新顶部工具栏显示")) {
                    if (MainActivity.this.m_ll_TopTip == null) {
                        return;
                    }
                    if (PubVar.HeadTip_Visible) {
                        MainActivity.this.m_ll_TopTip.setVisibility(0);
                    } else {
                        MainActivity.this.m_ll_TopTip.setVisibility(8);
                    }
                } else if (paramString.equals("更新比例尺显示")) {
                    if (PubVar.ScaleBar_Visible) {
                        PubVar._PubCommand.ShowToolbar("地图缩放工具栏");
                    } else {
                        PubVar._PubCommand.HideToolbar("地图缩放工具栏");
                    }
                } else if (paramString.equals("更新指北针显示")) {
                    View tmpView = MainActivity.this.findViewById(R.id.imageButtonCompass);
                    if (tmpView == null) {
                        return;
                    }
                    if (PubVar.Compass_Show) {
                        tmpView.setVisibility(0);
                    } else {
                        tmpView.setVisibility(8);
                    }
                } else if (paramString.equals("保存窗体参数")) {
                    if (MainActivity.this.m_MapCompLayoutToolbar != null) {
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_Form_CiricleMenuShow", Boolean.valueOf(MainActivity.this.m_MapCompLayoutToolbar.getExpandMoreTools().isShow()));
                    }
                } else if (paramString.equals("更新窗体显示")) {
                    HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_Form_CiricleMenuShow");
                    if (tempHashMap != null && (tempStr = tempHashMap.get("F2")) != null && !tempStr.equals("") && Boolean.parseBoolean(tempStr) && MainActivity.this.m_MapCompLayoutToolbar != null && MainActivity.this.m_MapCompLayoutToolbar.getExpandMoreTools() != null) {
                        MainActivity.this.m_MapCompLayoutToolbar.getExpandMoreTools().expand();
                    }
                } else if (paramString.equals("更新主菜单显示")) {
                    if (Boolean.parseBoolean(String.valueOf(pObject))) {
                        MainActivity.this.mainMenubar.SetVisible(true);
                        MainActivity.this.m_MapCompLayoutToolbar.SetMainMenuVisible(false);
                        return;
                    }
                    MainActivity.this.mainMenubar.SetVisible(false);
                    MainActivity.this.m_MapCompLayoutToolbar.SetMainMenuVisible(true);
                } else if (paramString.equals("常用快捷工具")) {
                    FragmentTransaction ft = MainActivity.this.getFragmentManager().beginTransaction();
                    Fragment fragment = MainActivity.this.getFragmentManager().findFragmentByTag("loginDialog");
                    if (fragment != null) {
                        ft.remove(fragment);
                    }
                    CommonToolsDialog dialog = new CommonToolsDialog();
                    if (pObject != null) {
                        if (pObject instanceof Object[]) {
                            Object[] tmpObjs = (Object[]) pObject;
                            if (tmpObjs != null && tmpObjs.length > 0) {
                                if (tmpObjs[0] instanceof ICallback) {
                                    dialog.SetCallback((ICallback) tmpObjs[0]);
                                }
                                if (tmpObjs.length > 1) {
                                    dialog.SetRelateTag(tmpObjs[1]);
                                }
                            }
                        } else if (pObject instanceof ICallback) {
                            dialog.SetCallback((ICallback) pObject);
                        }
                    }
                    dialog.show(ft, "CommonToolsDialog");
                } else if (paramString.equals("系统时间错误")) {
                    AlertDialog.Builder localBuilder = new AlertDialog.Builder(PubVar.MainContext);
                    localBuilder.setIcon(R.drawable.messageinfo);
                    localBuilder.setTitle("安全提示");
                    localBuilder.setMessage(String.valueOf(pObject));
                    localBuilder.setCancelable(false);
                    localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class: com.xzy.forestSystem.MainActivity.1.3
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                            PubVar._PubCommand.ProcessCommand("完全退出");
                        }
                    });
                    localBuilder.show();
                } else if (!paramString.equals("系统截屏") && paramString.equals("MouseLongClickReturn") && pObject != null) {
                    MotionEvent tmpMotionEvent = (MotionEvent) pObject;
                    final Coordinate tmpMouseCoord = MainActivity.this.m_MapView.getMap().ScreenToMap(new PointF(tmpMotionEvent.getX(), tmpMotionEvent.getY()));
                    new AlertDialog.Builder(PubVar.MainContext, 3).setTitle("选择操作").setItems(new String[]{"导航至该点", "上一视图", "放大5倍", "缩小5倍", "在线帮助"}, new DialogInterface.OnClickListener() { // from class: com.xzy.forestSystem.MainActivity.1.4
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface arg0, int arg1) {
                            if (arg1 == 0) {
                                Navigation_Dialog.NavigateByMap(new Point(tmpMouseCoord).getPoint(0));
                            } else if (arg1 == 1) {
                                MainActivity.this.m_MapView.getMap().gotoLastMapExtend();
                            } else if (arg1 == 2) {
                                MainActivity.this.m_MapView.getMap().setExtend(MainActivity.this.m_MapView.getMap().getExtend().Scale(0.2d));
                                MainActivity.this.m_MapView.getMap().Refresh();
                            } else if (arg1 == 3) {
                                MainActivity.this.m_MapView.getMap().setExtend(MainActivity.this.m_MapView.getMap().getExtend().Scale(5.0d));
                                MainActivity.this.m_MapView.getMap().Refresh();
                            } else if (arg1 == 4) {
                                PubVar._PubCommand.ProcessCommand("在线帮助");
                            }
                            arg0.dismiss();
                        }
                    }).show();
                }
            } catch (Exception e) {
            }
        }
    };

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_MapView = new MapView(this);
        map = new Map(m_MapView);
        PubVar pubVar = new PubVar();
        pubVar.MainContext = this;
        this._ReplaceMainMenuLL = (LinearLayout) findViewById(R.id.ll_replaceMainMenu);
        ImageView localImageView = (ImageView) findViewById(R.id.x_scalebar);
//        PubVar._PubCommand.m_ScaleBar.SetImageView(localImageView);
        localImageView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                PubVar._PubCommand.ProcessCommand("地图比例尺显示设置");
            }
        });
        this.gpsTextView = (TextView) findViewById(R.id.textView_gps);
        TextView gpsStatusInfoText = (TextView) findViewById(R.id.textViewTitleTip);
        ImageView gpsOnOffImgView = (ImageView) findViewById(R.id.imageViewGPSOFF);
        ImageView gpsSNRImgView = (ImageView) findViewById(R.id.imageViewGPSSNR);
        this.gpsTextView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.4
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                HeadGPSCoordShowType_Dialog tempDialog = new HeadGPSCoordShowType_Dialog();
                tempDialog.SetCallback(pCallback);
                tempDialog.ShowDialog();
            }
        });
        gpsStatusInfoText.setOnClickListener(this.myGPSTipClickListener);
        gpsOnOffImgView.setOnClickListener(this.myGPSTipClickListener);
        gpsSNRImgView.setOnClickListener(this.myGPSTipClickListener);
//        if (PubVar._PubCommand.m_GpsLocation != null) {
//            PubVar._PubCommand.m_GpsLocation.setControls(this.gpsTextView, gpsStatusInfoText, gpsOnOffImgView, gpsSNRImgView);
//            PubVar._PubCommand.m_GpsLocation.updateShowTextInfo(this.gpsTextView);
//        }
        ((ImageButton) findViewById(R.id.imageButtonGPS)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.5
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                PubVar._PubCommand.ProcessCommand("开启GPS设备");
            }
        });
        ((ImageButton) findViewById(R.id.imageButtonLayers)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.6
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                FeatureLayersList_Dialog tmpDialog = new FeatureLayersList_Dialog();
                if (m_SelectLayer != null) {
                    tmpDialog.setCurrentEditLyrID(m_SelectLayer.getLayerID());
                }
                tmpDialog.SetCallback(pCallback);
                tmpDialog.ShowDialog();
            }
        });
        this.compassImageView = (ImageButton) findViewById(R.id.imageButtonCompass);
        this.compassImageView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.7
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                m_compassImageViewCount++;
                if (m_compassImageViewCount > 5) {
                    PubVar.RasterLayerShowRect = true;
                }
                if (m_compassImageViewCount > 10) {
                    PubVar.RasterLayerShowRect = false;
                    m_compassImageViewCount = 0;
                }
            }
        });
//        if (PubVar._PubCommand.m_Compass != null) {
//            PubVar._PubCommand.m_Compass.setCompassImageView(this.compassImageView);
//        }
        if (PubVar.Compass_Show) {
            this.compassImageView.setVisibility(0);
        } else {
            this.compassImageView.setVisibility(8);
        }
        ((ImageButton) findViewById(R.id.imageButtonSwitchLayers)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.8
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                new SwitchGroupLayer_Dailog().ShowDialog();
            }
        });
        ((ImageButton) findViewById(R.id.imageButtonLayersContent)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.9
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (PubVar.MyLayersContent == null) {
                    LayersContent_Dialog tmpLayersContent_Dialog = new LayersContent_Dialog(PubVar.MainContext, PubVar._PubCommand.m_MainLayout);
                    PubVar.MyLayersContent = tmpLayersContent_Dialog;
                    tmpLayersContent_Dialog.showDialog();
                }
            }
        });
        this._MainMenuImageButton = (ImageButton) findViewById(R.id.imageButtonMainMenu);
        this._MainMenuImageButton.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.10
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                MainMenuDialog.ShowMenu(MainActivity.this, _MainMenuImageButton.getX(), _MainMenuImageButton.getY());
            }
        });
        this._MBExpandMoreTools = (CircleMenuComposer) findViewById(R.id.cmc_expandMore);
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
                    tempDialog.SetCallback(pCallback);
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
        this.m_GPSLocalImgButton = (ImageButton) findViewById(R.id.imageButtonGPSLoc);
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
                    m_GPSLocalImgButton.setImageResource(R.drawable.mb_gpslock);
                    v.setTag("锁定GPS");
                    PubVar.AutoPan = false;
                    Common.ShowToast("GPS跟踪解锁");
                } else if (command.equals("锁定GPS")) {
                    m_GPSLocalImgButton.setImageResource(R.drawable.mb_gpsarrow);
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
        ImageButton tmpIbtn01 = (ImageButton) findViewById(R.id.imageButtonZoomByExtend);
        this._MBExpandMoreTools.addButtonTag("框选缩放", R.id.imageButtonZoomByExtend, tmpIbtn01);
        tmpIbtn01.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.13
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PubVar._PubCommand.ProcessCommand("框选缩放");
            }
        });
        this.m_CommonEventButton = (ImageButton) findViewById(R.id.imageButton_Common);
        this.m_CommonEventButton.setVisibility(8);
        this.m_CommonEventButton.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.14
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (PubVar._PubCommand.getCommonEvent() != null) {
                    CommonEvent tmpCommonEvent = PubVar._PubCommand.getCommonEvent();
                    if (tmpCommonEvent.Dialog != null) {
                        tmpCommonEvent.Dialog.show();
                        PubVar._PubCommand.SetCommonEvent(null);
                        m_CommonEventButton.setVisibility(8);
                    }
                }
            }
        });
        ((ImageButton) findViewById(R.id.imageButtonYangdiDiaocha)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MapCompLayoutToolbar.15
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                new XiaoBanYangDiQuery_Dialog().ShowDialog();
            }
        });
    }


    public void BindEvent(int paramInt, ViewClick paramViewClick) {
        View localView = findViewById(paramInt);
        if (localView != null) {
            localView.setOnClickListener(paramViewClick);
        }
    }

    public ViewClick GetViewClick() {
        return new ViewClick();
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration paramConfiguration) {
        super.onConfigurationChanged(paramConfiguration);
        if (getResources().getConfiguration().orientation == 2) {
        }
    }

    private void initialForm() {
        String tmpStr;
        if (pCallback == null) {
            return;
        }
        pCallback.OnClick("更新顶部工具栏显示", null);
        pCallback.OnClick("更新比例尺显示", null);
        boolean tmpBool = false;
        HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_MainToolbar_Visible");
        if (!(tempHashMap == null || (tmpStr = tempHashMap.get("F2")) == null || tmpStr.equals(""))) {
            tmpBool = Boolean.parseBoolean(tmpStr);
        }
        pCallback.OnClick("更新主菜单显示", Boolean.valueOf(tmpBool));
    }

    @SuppressLint("WrongConstant")
    public static void ClearNotification(Context paramContext) {
        ((NotificationManager) paramContext.getSystemService("notification")).cancelAll();
    }

    @SuppressLint({"NewApi", "WrongConstant"})
    public void AddNotification() {
        try {
            if (Build.VERSION.SDK_INT < 16) {
                Notification n = new Notification();
                String appName = PubVar.AppName;
                long when = System.currentTimeMillis();
                n.icon = R.drawable.ic_launcher;
                n.tickerText = appName;
                n.when = when;
                n.flags = 2;
                Intent intent = new Intent(this, PubVar.class);
                intent.setFlags(67108864);
//                n.setLatestEventInfo(this, appName, "点击切换到系统主界面.", PendingIntent.getActivity(this, 0, intent, 134217728));
                ((NotificationManager) getSystemService("notification")).notify(0, n);
                return;
            }
            Notification notify3 = new Notification.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContentTitle(PubVar.AppName).setContentText("点击切换到系统主界面.").setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0)).build();
            notify3.flags |= 32;
            ((NotificationManager) getSystemService("notification")).notify(0, notify3);
        } catch (Exception e) {
        }
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            if (event.getDownTime() - this.lastKeyReturnTime < 1000) {
                this.m_PubCommand.ProcessCommand("退出系统");
            } else {
                Common.ShowToast("再次按键退出系统.");
            }
            this.lastKeyReturnTime = event.getDownTime();
            return true;
        } else if (keyCode == 25) {
            this.m_PubCommand.ProcessCommand("单击缩小");
            return true;
        } else if (keyCode == 24) {
            this.m_PubCommand.ProcessCommand("单击放大");
            return true;
        } else {
            if (keyCode == 82) {
                new ToolsManager_Dialog().ShowDialog();
            }
            return super.onKeyDown(keyCode, event);
        }
    }

    private void start() {
        startActivityForResult(new Intent(), 1234);
    }

    /* access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class ViewClick implements View.OnClickListener {
        public ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View paramView) {
            PubVar._PubCommand.ProcessCommand(paramView.getTag().toString());
        }
    }
}
