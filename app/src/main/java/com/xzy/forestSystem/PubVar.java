package com.xzy.forestSystem;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.xzy.forestSystem.gogisapi.Carto.Map;
import com.xzy.forestSystem.gogisapi.Carto.MapView;
import com.xzy.forestSystem.gogisapi.Common.Common;
import com.xzy.forestSystem.gogisapi.Common.CopyPasteObject;
import com.xzy.forestSystem.gogisapi.Common.HashValueObject;
import com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.xzy.forestSystem.gogisapi.Common.NetWorkUtils;
import com.xzy.forestSystem.gogisapi.Common.PubCommand;
import com.xzy.forestSystem.gogisapi.Common.XHashMap;
import com.xzy.forestSystem.gogisapi.GPS.GPSDisplay;
import com.xzy.forestSystem.gogisapi.Geodatabase.C0542Workspace;
import com.xzy.forestSystem.gogisapi.MyControls.MessagePanel_Dialog;
import com.xzy.forestSystem.gogisapi.System.AuthorizeTools;
import com.xzy.forestSystem.gogisapi.Toolbar.LayersContent_Dialog;
import com.xzy.forestSystem.hellocharts.animation.ChartViewportAnimator;
import com.xzy.forestSystem.hellocharts.animation.PieChartRotationAnimator;

import java.util.Date;

public class PubVar {
    public static boolean AllowEditSnap = true;
    public static String AppName = "采集系统";
    public static String AppNameEN = "GZForestSystem";
    public static int AreaUnitType = 1;
    public static boolean AutoPan = true;
    public static int ChildMapHeight = ChartViewportAnimator.FAST_ANIMATION_DURATION;
    public static int ChildMapWidth = 400;
    public static double Clip_EPSILON = 1.0E-4d;
    public static boolean Compass_Show = true;
    public static Date CurrentNetTime = new Date();
    public static boolean DataSetQuerySafety = false;
    public static int DebugTag = 0;
    public static float DialogHeightRatio = 0.9f;
    public static boolean Draw_Polygon_ShowCurrent = false;
    public static boolean Edited_ShowAttribute_Bool = true;
    public static double GPSGatherIntervalDistance;
    public static long GPSGatherIntervalTime;
    public static double GPS_Antenna_Height = 0.0d;
    public static int GPS_Device_Type = 0;
    public static double GPS_Gather_Accuracy = 10.0d;
    public static boolean GPS_Gather_PointAuto = true;
    public static int GPS_Point_ShowType = 0;
    public static boolean GPS_Show_Accuracy = false;
    public static int GPS_Show_CoorSystem_Type = 0;
    public static boolean GPS_Show_CoordMultiLine = false;
    public static boolean GPS_Show_Elevation = true;
    public static int GPS_Show_Format_Type = 0;
    public static boolean HeadTip_Visible = true;
    public static String HostIP = "210.42.38.233";
    public static int HostPort = 20;
    public static String HostUserName = "";
    public static String HostUserPwd = "";
    public static int LayersContentAlpha = PieChartRotationAnimator.FAST_ANIMATION_DURATION;
    public static int LayersContentHeight = 600;
    public static int LayersContentWidth = 800;
    public static Context MainContext;
    public static int MapBgColor = -1;
    public static int MapTile_Download_ThreadCount = 20;
    public static boolean Map_Display_Cache = false;
    public static boolean Map_Text_AutoAdjustPos = true;
    public static boolean MediaInfo_Input_Bool = false;
    public static MessagePanel_Dialog MessageDialog = null;
    public static boolean MessageDialogAllowShow = false;
    public static int MessageMaxCount = 3;
    public static int MessagePanelHeight = ChartViewportAnimator.FAST_ANIMATION_DURATION;
    public static int MessagePanelWidth = 400;
    public static boolean Module_SenLinDuCha = false;
    public static boolean Module_YangdiDiaoCha = true;
    public static LayersContent_Dialog MyLayersContent = null;
    public static boolean RasterLayerShowRect = false;
    public static int ScaleBar_ShowType = 1;
    public static boolean ScaleBar_Visible = true;
    public static float ScaledDensity = 1.0f;
    public static int ScreenHeight;
    public static boolean ScreenIsFull = false;
    public static boolean ScreenIsLand = false;
    public static int ScreenStatusHeight = 0;
    public static int ScreenWidth;
    public static float ScreenXDPI = 3.0f;
    public static String ServerURL = ""/*http://www.milliontechnology.com/GZForestServer/*/;
    public static boolean ShowToolbarAnim = true;
    public static double SnapDistance = 20.0d;
    public static CopyPasteObject SystemCopyObject = null;
    public static String SystemFolderName = "贵州林业";
    public static int TextHeight20 = 20;
    public static boolean Toolbar_AutoResize = true;
    public static boolean Toolbar_Gesture_Close = false;
    public static boolean Track_Auto_Record = false;
    public static boolean Track_Draw_Date = false;
    public static int Track_Point_MaxCount = 100;
    public static double Track_Record_Param = 5.0d;
    public static int Track_Record_Type = 1;
    public static String Version = "V1.09";
    public static int VersionNumber = 109;
    public static XHashMap _ComHashMap = new XHashMap();
    public static GPSDisplay _GPSDisplay = null;
    public static Map _Map = null;
    public static MapView _MapView = null;
    public static PubCommand _PubCommand = null;
    public static AuthorizeTools m_AuthorizeTools = null;
    public static ICallback m_Callback = null;
    public static boolean m_IsConnectServer = true;
    public static boolean m_IsEnable = true;
    public static boolean m_MapCompassVisible = false;
    public static int m_SCREEN_ORIENTATION = 0;
    public static String m_SysDataName = "TAData";
    public static String m_SystemPath;
    public static C0542Workspace m_Workspace = null;

    static {
        MainContext = null;
        GPSGatherIntervalDistance = 0.0d;
        GPSGatherIntervalTime = 0;
        ScreenWidth = 480;
        ScreenHeight = 800;
        MainContext = null;
        GPSGatherIntervalDistance = 0.0d;
        GPSGatherIntervalTime = 0;
        ScreenWidth = 480;
        ScreenHeight = 800;
    }

    @SuppressLint("WrongConstant")
    public static void Initial(Context context) {
        m_SystemPath = Common.GetAPPPath();
        Common.InitialAPP(context);
        _PubCommand = new PubCommand(context);
        HashValueObject localHashValueObject = new HashValueObject();
        localHashValueObject.Value = "采集数据";
        _ComHashMap.AddValue("ModuleName", localHashValueObject);
        SystemCopyObject = new CopyPasteObject();
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(dm);
        ScreenWidth = dm.widthPixels;
        ScreenHeight = dm.heightPixels - ScreenStatusHeight;
        ScaledDensity = dm.scaledDensity;
        ScreenXDPI = dm.xdpi;
        if (ScaledDensity <= 0.0f) {
            ScaledDensity = 1.0f;
        }
        LayersContentWidth = (int) (((double) ScreenWidth) * 0.5d);
        LayersContentHeight = ScreenHeight / 3;
        Paint tmpPaint = new Paint();
        tmpPaint.setTextSize(20.0f);
        Paint.FontMetrics fm = tmpPaint.getFontMetrics();
        TextHeight20 = (int) ((Math.ceil((double) (fm.descent - fm.ascent)) * ((double) ScaledDensity)) + 2.0d);
        MessagePanelWidth = (int) (((double) ScreenWidth) * 0.8d);
        MessagePanelHeight = (int) (ScaledDensity * 30.0f);
        ChildMapWidth = (int) (((double) ScreenWidth) * 0.5d);
        ChildMapHeight = (int) (((double) ScreenHeight) * 0.5d);
        m_IsConnectServer = NetWorkUtils.isNetworkConnected(context);
    }

    public static void OutputMessage(String msg) {
        if (MessageDialog == null && MessageDialogAllowShow) {
            MessageDialog = new MessagePanel_Dialog(MainContext, _PubCommand.m_MainLayout);
        }
        if (MessageDialog != null && MessageDialogAllowShow) {
            MessageDialog.showMsg(msg);
        }
    }
}
