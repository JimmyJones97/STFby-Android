package  com.xzy.forestSystem.gogisapi.Common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import  com.xzy.forestSystem.gogisapi.Carto.Map;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WGS1984;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Selection;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.Geometry.Point;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import com.xzy.forestSystem.gogisapi.MyControls.AutoInputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.BaseDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.LoadingDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyProgressDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.SpinnerDialog;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import com.stczh.gzforestSystem.R;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.Character;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

/* renamed from:  com.xzy.forestSystem.gogisapi.Common.Common */
public class Common {
    private static String AppPath = "";
    private static String FileNameT = "";
    static final String chinese = "[Α-￥]";
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat dateSmallFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat dateSmallFormat2 = new SimpleDateFormat("yyyyMMdd");
    public static final DecimalFormat doubleFormat = new DecimalFormat(".###");
    public static final DecimalFormat doubleFormat1 = new DecimalFormat("0.0");
    public static final SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
    private static Toast m_toast = null;

    public static void InitialAPP(Context context) {
    }

    public static String CheckSystemFile(Context paramContext) {
        String str1 = GetSDCardPath();
        if (str1.equals("") && "mounted".equals(Environment.getExternalStorageState())) {
            str1 = String.valueOf(Environment.getExternalStorageDirectory().getPath()) + FileSelector_Dialog.sRoot + PubVar.SystemFolderName;
        }
        if (str1.equals("")) {
            return "系统主目录缺失，程序无法正常运行！";
        }
        List<String> localArrayList1 = new ArrayList();
        List<String> localArrayList2 = new ArrayList();
        localArrayList1.add(String.valueOf(str1) + "/Map");
        localArrayList1.add(String.valueOf(str1) + "/Data");
        localArrayList1.add(String.valueOf(str1) + "/Track");
        localArrayList1.add(String.valueOf(str1) + "/Track/数据导出");
        localArrayList1.add(String.valueOf(str1) + "/系统截屏");
        localArrayList1.add(String.valueOf(str1) + "/Others");
        localArrayList1.add(String.valueOf(str1) + "/SysFile");
        localArrayList2.add(String.valueOf(str1) + "/SysFile/Config.dbx," + R.raw.config);
        localArrayList2.add(String.valueOf(str1) + "/SysFile/Project.dbx," + R.raw.project);
        localArrayList2.add(String.valueOf(str1) + "/SysFile/Template.dbx," + R.raw.template);
        localArrayList2.add(String.valueOf(str1) + "/SysFile/UserConfig.dbx," + R.raw.userconfig);
        localArrayList2.add(String.valueOf(str1) + "/SysFile/sldcexporttable.xls," + R.raw.sldcexporttable);
        localArrayList2.add(String.valueOf(str1) + "/SysFile/xbyzdctable.xls," + R.raw.xbyzdctable);
        localArrayList1.add(String.valueOf(str1) + "/备份");
        localArrayList1.add(String.valueOf(str1) + "/小班调查");
        for (String tempFolder : localArrayList1) {
            if (!(ExistFolder(tempFolder) || new File(tempFolder).mkdir())) {
                return "无法创建目录【" + tempFolder + "】，程序无法正常运行！\r\n请确认您的设备具有足够内存和权限!";
            }
        }
        for (String tempStr : localArrayList2) {
            String[] tempStrs = tempStr.split(",");
            String tempFile = tempStrs[0];
            int tempFileResID = Integer.parseInt(tempStrs[1]);
            if (!(CheckExistFile(tempFile) || CopyToFileFromRawID(paramContext, tempFile, tempFileResID))) {
                return "无法创建配置文件【" + tempStr + "】，程序无法正常运行！\r\n请确认您的设备具有足够内存和权限!";
            }
        }
        return "OK";
    }

    public static boolean CheckLocalUpdate(Context mContext) {
        String tmpUpdatePath = String.valueOf(GetSDCardPath()) + "/SysFile/Update.dbx";
        if (CopyToFileFromRawIDMust(mContext, tmpUpdatePath, R.raw.update)) {
            return CheckLocalUpdateInfo(tmpUpdatePath);
        }
        return false;
    }

    public static String GetProjectPath(String projectName) {
        return String.valueOf(String.valueOf(PubVar.m_SystemPath) + "/Data") + FileSelector_Dialog.sRoot + projectName;
    }

    public static boolean CheckLocalUpdateInfo(String updatePath) {
        String tmpStr01;
        try {
            if (CheckExistFile(updatePath)) {
                SQLiteDBHelper tmpSQLiteDBHelper = new SQLiteDBHelper();
                tmpSQLiteDBHelper.setDatabaseName(updatePath);
                int tmpUpdateNumber = 0;
                SQLiteReader tmpReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("Select UpdateInfo From ConfigUpdate Order By ID DESC Limit 1");
                if (tmpReader != null) {
                    if (tmpReader.Read() && (tmpStr01 = tmpReader.GetString(0)) != null && tmpStr01.length() > 0) {
                        tmpUpdateNumber = Integer.parseInt(tmpStr01);
                    }
                    tmpReader.Close();
                }
                boolean tmpBool02 = false;
                String tmpNewVersion = "";
                SQLiteReader tmpReader02 = tmpSQLiteDBHelper.Query("Select VersionNumber,DBType,Command From UpdateInfo Where VersionNumber>" + String.valueOf(tmpUpdateNumber) + " Order By ID");
                if (tmpReader02 != null) {
                    while (tmpReader02.Read()) {
                        tmpNewVersion = String.valueOf(tmpReader02.GetInt32(0));
                        String tmpDbType = tmpReader02.GetString(1);
                        String tmpCommand = tmpReader02.GetString(2);
                        if (!(tmpDbType == null || tmpCommand == null)) {
                            if (tmpDbType.toLowerCase().equals("sysconfig")) {
                                PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().ExecuteSQL(tmpCommand);
                            } else {
                                PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().ExecuteSQL(tmpCommand);
                            }
                            tmpBool02 = true;
                        }
                    }
                    tmpReader02.Close();
                }
                if (tmpBool02 && tmpNewVersion.length() > 0) {
                    PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().ExecuteSQL("Insert Into ConfigUpdate (UpdateInfo,UpdateTime) Values ('" + tmpNewVersion + "','" + dateFormat.format(new Date()) + "')");
                }
                return true;
            }
        } catch (Exception ex) {
            Log("错误", ex.getMessage());
        }
        return false;
    }

    public static boolean CheckProjectExist(String projectName) {
        if (CheckExistFile(String.valueOf(PubVar.m_SystemPath) + "/Data/" + projectName)) {
            return true;
        }
        return false;
    }

    public static List<String> GetExtSDCardPath() {
        String[] arr;
        List<String> lResult = new ArrayList<>();
        try {
            InputStreamReader isr = new InputStreamReader(Runtime.getRuntime().exec("mount").getInputStream());
            BufferedReader br = new BufferedReader(isr);
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                } else if (line.contains("extSdCard") && (arr = line.split(" ")) != null && arr.length > 1) {
                    String path = arr[1];
                    if (new File(path).isDirectory()) {
                        lResult.add(path);
                        break;
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        return lResult;
    }

    @SuppressLint("WrongConstant")
    public static boolean CheckGPSEnable(Context mContext) {
        return ((LocationManager) mContext.getSystemService("location")).isProviderEnabled("gps");
    }

    public static double CalAngleByPoints(Coordinate tmpCoord1, Coordinate tmpCoord2, Coordinate tmpCoord3) {
        double d1 = GetTwoPointDistance(tmpCoord1, tmpCoord2);
        double d2 = GetTwoPointDistance(tmpCoord1, tmpCoord3);
        double d3 = GetTwoPointDistance(tmpCoord2, tmpCoord3);
        return 57.29577951308232d * Math.acos((((d3 * d3) + (d1 * d1)) - (d2 * d2)) / ((2.0d * d3) * d1));
    }

    public static void ArrayClone(byte[] array01, byte[] array02, int paramInt) {
        for (int i = 0; i < array01.length; i++) {
            array02[paramInt + i] = array01[i];
        }
    }

    public static AbstractGeometry ConvertBytesToGeometry(byte[] paramArrayOfByte, EGeoLayerType paramlkGeoLayerType, boolean withGeoCoord) {
        int tempCount;
        int tempCount2;
        int tempCount3;
        int tempCount22;
        AbstractGeometry result = null;
        try {
            if (paramlkGeoLayerType == EGeoLayerType.POINT) {
                int tempNumOfParts = BitConverter.ToInt(paramArrayOfByte, 0);
                List<Integer> tempPartList = new ArrayList<>();
                int tempIJ = 8;
                if (tempNumOfParts > 1) {
                    tempPartList.add(0);
                    for (int i = 1; i < tempNumOfParts; i++) {
                        tempPartList.add(Integer.valueOf(BitConverter.ToInt(paramArrayOfByte, tempIJ)));
                        tempIJ += 4;
                    }
                } else {
                    tempPartList.add(0);
                }
                int tempCount4 = paramArrayOfByte.length - tempIJ;
                if (withGeoCoord) {
                    tempCount3 = tempCount4 / 40;
                } else {
                    tempCount3 = tempCount4 / 24;
                }
                List<Coordinate> localArrayList = new ArrayList<>();
                for (int i2 = 0; i2 < tempCount3; i2++) {
                    double tempX = BitConverter.ToDouble(paramArrayOfByte, tempIJ);
                    int tempIJ2 = tempIJ + 8;
                    double tempY = BitConverter.ToDouble(paramArrayOfByte, tempIJ2);
                    int tempIJ3 = tempIJ2 + 8;
                    double tempZ = BitConverter.ToDouble(paramArrayOfByte, tempIJ3);
                    tempIJ = tempIJ3 + 8;
                    Coordinate tempCoord = new Coordinate(tempX, tempY, tempZ);
                    if (withGeoCoord) {
                        double tempGeoX = BitConverter.ToDouble(paramArrayOfByte, tempIJ);
                        int tempIJ4 = tempIJ + 8;
                        double tempGeoY = BitConverter.ToDouble(paramArrayOfByte, tempIJ4);
                        tempIJ = tempIJ4 + 8;
                        tempCoord.setGeoX(tempGeoX);
                        tempCoord.setGeoY(tempGeoY);
                    }
                    localArrayList.add(tempCoord);
                }
                AbstractGeometry result2 = new Point();
                try {
                    int tempCount5 = tempPartList.size();
                    int tempTid = 0;
                    for (int i3 = 0; i3 < tempCount5; i3++) {
                        List<Coordinate> tempList01 = new ArrayList<>();
                        tempPartList.get(i3).intValue();
                        if (i3 != tempCount5 - 1) {
                            tempCount22 = tempPartList.get(i3).intValue();
                        } else {
                            tempCount22 = localArrayList.size();
                        }
                        for (int j = tempTid; j < tempCount22; j++) {
                            tempList01.add(localArrayList.get(j));
                        }
                        result2.AddPart(tempList01);
                        tempTid = tempCount22;
                    }
                    return result2;
                } catch (Exception e) {
                    return result2;
                }
            } else {
                int tempNumOfParts2 = BitConverter.ToInt(paramArrayOfByte, 0);
                List<Integer> tempPartList2 = new ArrayList<>();
                int tempIJ5 = 8;
                if (tempNumOfParts2 > 1) {
                    tempPartList2.add(0);
                    for (int i4 = 1; i4 < tempNumOfParts2; i4++) {
                        tempPartList2.add(Integer.valueOf(BitConverter.ToInt(paramArrayOfByte, tempIJ5)));
                        tempIJ5 += 4;
                    }
                } else {
                    tempPartList2.add(0);
                }
                int tempCount6 = paramArrayOfByte.length - tempIJ5;
                if (withGeoCoord) {
                    tempCount = tempCount6 / 40;
                } else {
                    tempCount = tempCount6 / 24;
                }
                List<Coordinate> localArrayList2 = new ArrayList<>();
                for (int i5 = 0; i5 < tempCount; i5++) {
                    double tempX2 = BitConverter.ToDouble(paramArrayOfByte, tempIJ5);
                    int tempIJ6 = tempIJ5 + 8;
                    double tempY2 = BitConverter.ToDouble(paramArrayOfByte, tempIJ6);
                    int tempIJ7 = tempIJ6 + 8;
                    double tempZ2 = BitConverter.ToDouble(paramArrayOfByte, tempIJ7);
                    tempIJ5 = tempIJ7 + 8;
                    Coordinate tempCoord2 = new Coordinate(tempX2, tempY2, tempZ2);
                    if (withGeoCoord) {
                        double tempGeoX2 = BitConverter.ToDouble(paramArrayOfByte, tempIJ5);
                        int tempIJ8 = tempIJ5 + 8;
                        double tempGeoY2 = BitConverter.ToDouble(paramArrayOfByte, tempIJ8);
                        tempIJ5 = tempIJ8 + 8;
                        tempCoord2.setGeoX(tempGeoX2);
                        tempCoord2.setGeoY(tempGeoY2);
                    }
                    localArrayList2.add(tempCoord2);
                }
                if (paramlkGeoLayerType == EGeoLayerType.POLYLINE) {
                    result = new Polyline();
                } else if (paramlkGeoLayerType == EGeoLayerType.POLYGON) {
                    result = new Polygon();
                }
                int tmpNumOfPoints = localArrayList2.size();
                int tempCount7 = tempPartList2.size();
                int tempTid2 = 0;
                for (int i6 = 0; i6 < tempCount7; i6++) {
                    List<Coordinate> tempList012 = new ArrayList<>();
                    if (i6 != tempCount7 - 1) {
                        tempCount2 = tempPartList2.get(i6 + 1).intValue();
                    } else {
                        tempCount2 = localArrayList2.size();
                    }
                    if (tempCount2 > tmpNumOfPoints) {
                        tempCount2 = tmpNumOfPoints;
                    }
                    for (int j2 = tempTid2; j2 < tempCount2; j2++) {
                        tempList012.add(localArrayList2.get(j2));
                    }
                    if ((paramlkGeoLayerType != EGeoLayerType.POLYLINE || tempList012.size() >= 2) && (paramlkGeoLayerType != EGeoLayerType.POLYGON || tempList012.size() >= 3)) {
                        result.AddPart(tempList012);
                        tempTid2 = tempCount2;
                    }
                }
                return result;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    public static AbstractGeometry ConvertBytesToGeometry(byte[] paramArrayOfByte, EGeoLayerType paramlkGeoLayerType, boolean withGeoCoord, int geoSptialType) {
        int tempCount;
        int tempCount2;
        int tempCount3;
        int tempCount22;
        AbstractGeometry result = null;
        try {
            if (paramlkGeoLayerType == EGeoLayerType.POINT) {
                int tempNumOfParts = BitConverter.ToInt(paramArrayOfByte, 0);
                List<Integer> tempPartList = new ArrayList<>();
                int tempIJ = 8;
                if (tempNumOfParts > 1) {
                    tempPartList.add(0);
                    for (int i = 1; i < tempNumOfParts; i++) {
                        tempPartList.add(Integer.valueOf(BitConverter.ToInt(paramArrayOfByte, tempIJ)));
                        tempIJ += 4;
                    }
                } else {
                    tempPartList.add(0);
                }
                int tempCount4 = paramArrayOfByte.length - tempIJ;
                if (withGeoCoord) {
                    tempCount3 = tempCount4 / 40;
                } else {
                    tempCount3 = tempCount4 / 24;
                }
                List<Coordinate> localArrayList = new ArrayList<>();
                for (int i2 = 0; i2 < tempCount3; i2++) {
                    double tempX = BitConverter.ToDouble(paramArrayOfByte, tempIJ);
                    int tempIJ2 = tempIJ + 8;
                    double tempY = BitConverter.ToDouble(paramArrayOfByte, tempIJ2);
                    int tempIJ3 = tempIJ2 + 8;
                    double tempZ = BitConverter.ToDouble(paramArrayOfByte, tempIJ3);
                    tempIJ = tempIJ3 + 8;
                    Coordinate tempCoord = new Coordinate(tempX, tempY, tempZ);
                    if (withGeoCoord) {
                        double tempGeoX = BitConverter.ToDouble(paramArrayOfByte, tempIJ);
                        int tempIJ4 = tempIJ + 8;
                        double tempGeoY = BitConverter.ToDouble(paramArrayOfByte, tempIJ4);
                        tempIJ = tempIJ4 + 8;
                        tempCoord.setGeoX(tempGeoX);
                        tempCoord.setGeoY(tempGeoY);
                    }
                    localArrayList.add(tempCoord);
                }
                AbstractGeometry result2 = new Point();
                try {
                    int tempCount5 = tempPartList.size();
                    int tempTid = 0;
                    for (int i3 = 0; i3 < tempCount5; i3++) {
                        List<Coordinate> tempList01 = new ArrayList<>();
                        tempPartList.get(i3).intValue();
                        if (i3 != tempCount5 - 1) {
                            tempCount22 = tempPartList.get(i3).intValue();
                        } else {
                            tempCount22 = localArrayList.size();
                        }
                        for (int j = tempTid; j < tempCount22; j++) {
                            tempList01.add(localArrayList.get(j));
                        }
                        result2.AddPart(tempList01);
                        tempTid = tempCount22;
                    }
                    return result2;
                } catch (Exception e) {
                    return result2;
                }
            } else {
                int tempNumOfParts2 = BitConverter.ToInt(paramArrayOfByte, 0);
                List<Integer> tempPartList2 = new ArrayList<>();
                int tempIJ5 = 8;
                if (tempNumOfParts2 > 1) {
                    tempPartList2.add(0);
                    for (int i4 = 1; i4 < tempNumOfParts2; i4++) {
                        tempPartList2.add(Integer.valueOf(BitConverter.ToInt(paramArrayOfByte, tempIJ5)));
                        tempIJ5 += 4;
                    }
                } else {
                    tempPartList2.add(0);
                }
                int tempCount6 = paramArrayOfByte.length - tempIJ5;
                if (withGeoCoord) {
                    tempCount = tempCount6 / 40;
                } else {
                    tempCount = tempCount6 / 24;
                }
                List<Coordinate> localArrayList2 = new ArrayList<>();
                for (int i5 = 0; i5 < tempCount; i5++) {
                    double tempX2 = BitConverter.ToDouble(paramArrayOfByte, tempIJ5);
                    int tempIJ6 = tempIJ5 + 8;
                    double tempY2 = BitConverter.ToDouble(paramArrayOfByte, tempIJ6);
                    int tempIJ7 = tempIJ6 + 8;
                    double tempZ2 = BitConverter.ToDouble(paramArrayOfByte, tempIJ7);
                    tempIJ5 = tempIJ7 + 8;
                    Coordinate tempCoord2 = new Coordinate(tempX2, tempY2, tempZ2);
                    if (withGeoCoord) {
                        double tempGeoX2 = BitConverter.ToDouble(paramArrayOfByte, tempIJ5);
                        int tempIJ8 = tempIJ5 + 8;
                        double tempGeoY2 = BitConverter.ToDouble(paramArrayOfByte, tempIJ8);
                        tempIJ5 = tempIJ8 + 8;
                        tempCoord2.setGeoX(tempGeoX2);
                        tempCoord2.setGeoY(tempGeoY2);
                    }
                    localArrayList2.add(tempCoord2);
                }
                if (paramlkGeoLayerType == EGeoLayerType.POLYLINE) {
                    result = new Polyline();
                } else if (paramlkGeoLayerType == EGeoLayerType.POLYGON) {
                    result = new Polygon();
                }
                int tempCount7 = tempPartList2.size();
                int tempTid2 = 0;
                for (int i6 = 0; i6 < tempCount7; i6++) {
                    List<Coordinate> tempList012 = new ArrayList<>();
                    if (i6 != tempCount7 - 1) {
                        tempCount2 = tempPartList2.get(i6 + 1).intValue();
                    } else {
                        tempCount2 = localArrayList2.size();
                    }
                    for (int j2 = tempTid2; j2 < tempCount2; j2++) {
                        tempList012.add(localArrayList2.get(j2));
                    }
                    result.AddPart(tempList012);
                    tempTid2 = tempCount2;
                }
                return result;
            }
        } catch (Exception e2) {
            return null;
        }
    }

    public static Byte[] GeometryByteExtend(byte[] bytes, EGeoLayerType _lkGeoLayerType) {
        List<Byte> result = new ArrayList<>();
        if (_lkGeoLayerType == EGeoLayerType.POINT) {
            for (int i = 0; i < 32; i++) {
                result.add(Byte.valueOf(bytes[i]));
            }
            for (int i2 = 32; i2 < 40; i2++) {
                result.add((byte) 0);
            }
        }
        if (result.size() > 0) {
            return (Byte[]) result.toArray(new Byte[0]);
        }
        return null;
    }

    public static AbstractGeometry ByteToGeometryEx(byte[] paramArrayOfByte, EGeoLayerType paramlkGeoLayerType) {
        int i = BitConverter.ToInt(BitConverter.Reverse(paramArrayOfByte, 0, 4), 0);
        int j = 4;
        if (paramlkGeoLayerType == EGeoLayerType.POINT) {
            return new Point(BitConverter.ToDouble(paramArrayOfByte, 8), BitConverter.ToDouble(paramArrayOfByte, 16));
        }
        ArrayList localArrayList1 = null;
        ArrayList localArrayList2 = null;
        int i1 = 0;
        if (i > 1) {
            localArrayList1 = new ArrayList();
            if (1 > i) {
                localArrayList2 = new ArrayList();
                i1 = (paramArrayOfByte.length - 4) / 16;
            }
        }
        for (int i2 = 0; i2 < i1; i2++) {
            int i3 = j + 8;
            j = i3 + 8;
            localArrayList2.add(new Coordinate(BitConverter.ToDouble(paramArrayOfByte, j), BitConverter.ToDouble(paramArrayOfByte, i3)));
        }
        if (paramlkGeoLayerType != EGeoLayerType.POLYGON) {
            Polyline localPolyline = new Polyline();
            if (i > 1) {
                localPolyline.setPartIndex(localArrayList1);
                localPolyline.IsSimple(false);
            }
            localPolyline.SetAllCoordinateList(localArrayList2);
            return localPolyline;
        }
        Polygon localPolygon = new Polygon();
        localPolygon.SetAllCoordinateList(localArrayList2);
        if (i > 1) {
            localPolygon.ConvertToPolyline().setPartIndex(localArrayList1);
            localPolygon.IsSimple(false);
            localPolygon.ConvertToPolyline().IsSimple(false);
        }
        localPolygon.ConvertToPolyline().SetAllCoordinateList(localArrayList2);
        return localPolygon;
    }

    public static int CalStrLength(String value) {
        int valueLength = 0;
        for (int i = 0; i < value.length(); i++) {
            if (value.substring(i, i + 1).matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength++;
            }
        }
        return valueLength;
    }

    public static String ConvertToDigital(String paramString, int paramInt) {
        char[] arrayOfChar = new char[paramInt];
        Arrays.fill(arrayOfChar, '0');
        return new DecimalFormat("0." + new String(arrayOfChar)).format(Double.valueOf(paramString));
    }

    public static String ConvertToDigi(double paramString, int paramInt) {
        return ConvertToDigital(String.valueOf(paramString), paramInt);
    }

    public static float ConvertToFloat(String paramString) {
        if (paramString == null) {
            return 0.0f;
        }
        try {
            return Float.parseFloat(paramString);
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    public static String ConvertToSexagesimal(String paramString) {
        double d1 = Double.parseDouble(paramString);
        int i = (int) Math.floor(Math.abs(d1));
        double d2 = 60.0d * GetdPoint(Math.abs(d1));
        int j = (int) Math.floor(d2);
        int k = (int) Math.floor(1000000.0d * GetdPoint(d2) * 60.0d);
        if (d1 < 0.0d) {
            return "-" + i + "/1," + j + "/1," + k + "/1000000";
        }
        return String.valueOf(i) + "/1," + j + "/1," + k + "/1000000";
    }

    public static boolean CopyFile(String paramString1, String paramString2) {
        File localFile = new File(paramString2);
        if (!localFile.getParentFile().exists()) {
            localFile.getParentFile().mkdirs();
        }
        try {
            FileInputStream localFileInputStream = new FileInputStream(paramString1);
            FileOutputStream localFileOutputStream = new FileOutputStream(paramString2);
            BufferedInputStream localBufferedInputStream = new BufferedInputStream(localFileInputStream);
            BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(localFileOutputStream);
            byte[] arrayOfByte = new byte[8192];
            try {
                for (int i = localBufferedInputStream.read(arrayOfByte); i > 0; i = localBufferedInputStream.read(arrayOfByte)) {
                    localBufferedOutputStream.write(arrayOfByte, 0, i);
                }
                localBufferedInputStream.close();
                localBufferedOutputStream.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        } catch (Exception e2) {
            return false;
        }
    }

    public static boolean CopyToFileFromRawID(Context paramContext, String paramString, int paramInt) {
        if (!CheckExistFile(paramString)) {
        }
        try {
            FileOutputStream localFileOutputStream = new FileOutputStream(new File(paramString));
            InputStream localInputStream = paramContext.getResources().openRawResource(paramInt);
            byte[] arrayOfByte = new byte[localInputStream.available()];
            localInputStream.read(arrayOfByte);
            localFileOutputStream.write(arrayOfByte);
            localInputStream.close();
            localFileOutputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean CopyToFileFromRawIDMust(Context mContext, String savefilepath, int resID) {
        try {
            File tmpFile = new File(savefilepath);
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            FileOutputStream localFileOutputStream = new FileOutputStream(new File(savefilepath));
            InputStream localInputStream = mContext.getResources().openRawResource(resID);
            byte[] arrayOfByte = new byte[localInputStream.available()];
            localInputStream.read(arrayOfByte);
            localFileOutputStream.write(arrayOfByte);
            localInputStream.close();
            localFileOutputStream.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean DeleteAll(File paramFile) {
        if (!paramFile.exists()) {
            return true;
        }
        if (!paramFile.isDirectory()) {
            return paramFile.delete();
        }
        File[] arrayOfFile = paramFile.listFiles();
        if (arrayOfFile != null && arrayOfFile.length > 0) {
            for (File tempFile : arrayOfFile) {
                if (tempFile.isFile()) {
                    tempFile.delete();
                } else {
                    DeleteAll(tempFile);
                }
            }
        }
        return paramFile.delete();
    }

    public static void DeletePhoto(Context paramContext, String paramString, ICallback tmpICallback) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(paramContext);
        localBuilder.setTitle("系统提示");
        localBuilder.setMessage("是否确定删除此照片？");
        localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                paramDialogInterface.dismiss();
            }
        });
        localBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                paramDialogInterface.dismiss();
            }
        });
        localBuilder.show();
    }

    public static boolean CheckExistFile(String filepath) {
        return new File(filepath).exists();
    }

    public static boolean CheckExistFolder(String filepath) {
        File tmpFile = new File(filepath);
        if (!tmpFile.exists() || !tmpFile.isDirectory()) {
            return false;
        }
        return true;
    }

    public static boolean ExistFolder(String folderPath) {
        File tempFile = new File(folderPath);
        if (!tempFile.exists() || !tempFile.isDirectory()) {
            return false;
        }
        return true;
    }

    public static boolean ExistFileEx(String paramString1, String paramString2) {
        FileNameT = paramString2;
        return new File(paramString1).listFiles(new UUIDfileFilter()).length > 0;
    }

    public static byte[] ConvertGeoToBytes(AbstractGeometry geometry) {
        List<Coordinate> paramList = geometry.GetAllCoordinateList();
        int tempNumOfParts = geometry.GetNumberOfParts();
        byte[] result = new byte[(((tempNumOfParts + 1) * 4) + (paramList.size() * 40))];
        ArrayClone(BitConverter.intToByteArray(tempNumOfParts), result, 0);
        int tempTid = 0 + 4;
        if (tempNumOfParts > 1) {
            for (Integer num : geometry.GetPartIndex()) {
                ArrayClone(BitConverter.intToByteArray(num.intValue()), result, tempTid);
                tempTid += 4;
            }
        } else {
            ArrayClone(new byte[4], result, tempTid);
            tempTid += 4;
        }
        for (Coordinate localCoordinate : paramList) {
            byte[] arrayOfByte2 = BitConverter.GetBytes(localCoordinate.getX());
            byte[] arrayOfByte3 = BitConverter.GetBytes(localCoordinate.getY());
            byte[] arrayOfByte4 = BitConverter.GetBytes(localCoordinate.getZ());
            byte[] arrayOfByte5 = BitConverter.GetBytes(localCoordinate.getGeoX());
            byte[] arrayOfByte6 = BitConverter.GetBytes(localCoordinate.getGeoY());
            ArrayClone(arrayOfByte2, result, tempTid);
            ArrayClone(arrayOfByte3, result, tempTid + 8);
            ArrayClone(arrayOfByte4, result, tempTid + 16);
            ArrayClone(arrayOfByte5, result, tempTid + 24);
            ArrayClone(arrayOfByte6, result, tempTid + 32);
            tempTid += 40;
        }
        return result;
    }

    public static byte[] CoordinatesToByte(List<Coordinate> paramList) {
        byte[] arrayOfByte1 = new byte[(paramList.size() * 40)];
        int i = 0;
        for (Coordinate localCoordinate : paramList) {
            byte[] arrayOfByte2 = BitConverter.GetBytes(localCoordinate.getX());
            byte[] arrayOfByte3 = BitConverter.GetBytes(localCoordinate.getY());
            byte[] arrayOfByte4 = BitConverter.GetBytes(localCoordinate.getZ());
            byte[] arrayOfByte5 = BitConverter.GetBytes(localCoordinate.getGeoX());
            byte[] arrayOfByte6 = BitConverter.GetBytes(localCoordinate.getGeoY());
            ArrayClone(arrayOfByte2, arrayOfByte1, i);
            ArrayClone(arrayOfByte3, arrayOfByte1, i + 8);
            ArrayClone(arrayOfByte4, arrayOfByte1, i + 16);
            ArrayClone(arrayOfByte5, arrayOfByte1, i + 24);
            ArrayClone(arrayOfByte6, arrayOfByte1, i + 32);
            i += 40;
        }
        return arrayOfByte1;
    }

    public static boolean GetCheckBoxValueOnID(Dialog paramDialog, int paramInt) {
        return ((CheckBox) paramDialog.findViewById(paramInt)).isChecked();
    }

    public static String GetFileName(String paramString) {
        int i = paramString.lastIndexOf(FileSelector_Dialog.sRoot);
        int j = paramString.lastIndexOf(FileSelector_Dialog.sFolder);
        if (i == -1 || j == -1) {
            return null;
        }
        return paramString.substring(i + 1, j + 4);
    }

    public static String GetFileName_NoEx(String paramString) {
        int i = paramString.lastIndexOf(FileSelector_Dialog.sRoot);
        int j = paramString.lastIndexOf(FileSelector_Dialog.sFolder);
        if (i == -1 || j == -1) {
            return null;
        }
        return paramString.substring(i + 1, j);
    }

    public static String GetFilePath(String paramString) {
        int i = paramString.lastIndexOf(FileSelector_Dialog.sRoot);
        if (i != -1) {
            return paramString.substring(0, i);
        }
        return null;
    }

    public static List<String> GetProjectList() {
        ArrayList localArrayList = new ArrayList();
        ArrayList tmpList = new ArrayList();
        File[] arrayOfFile = new File(String.valueOf(PubVar.m_SystemPath) + "/Data").listFiles();
        for (File localFile : arrayOfFile) {
            if (localFile.isDirectory() && CheckExistFile(localFile + "/Project.dbx") && CheckExistFile(localFile + "/TAData.dbx")) {
                tmpList.add(localFile.getName());
                localArrayList.add(String.valueOf(localFile.getName()) + "," + localFile.lastModified());
            }
        }
        if (localArrayList.size() > 0) {
            try {
                SQLiteReader tempReader = PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().Query("Select ProjectName,CreateTime,Security From T_Project");
                if (tempReader != null) {
                    while (tempReader.Read()) {
                        String tmpPrjName = tempReader.GetString(0);
                        int tmpindex = tmpList.indexOf(tmpPrjName);
                        if (tmpindex >= 0) {
                            String tempStr = String.valueOf(tmpPrjName) + "," + tempReader.GetString(1) + ",";
                            String tmpCode = tempReader.GetString(2);
                            if (tmpCode != null && !tmpCode.equals("")) {
                                tempStr = String.valueOf(tempStr) + tmpCode;
                            }
                            localArrayList.remove(tmpindex);
                            localArrayList.add(tmpindex, tempStr);
                        }
                    }
                    tempReader.Close();
                }
            } catch (Exception e) {
            }
            try {
                Collections.sort(localArrayList, new FileComparator());
            } catch (Exception e2) {
            }
        }
        return localArrayList;
    }

    public static boolean GetProjectEncryptInfo(String projectName, BasicValue returnInfo) {
        String tmpStr;
        try {
            SQLiteReader tempReader = PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().Query("Select Security From T_Project Where ProjectName='" + projectName + "'");
            if (tempReader == null) {
                return false;
            }
            if (!tempReader.Read() || (tmpStr = tempReader.GetString(0)) == null || tmpStr.equals("")) {
                tempReader.Close();
                return false;
            }
            returnInfo.setValue(tmpStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String GetAPPPath() {
        try {
            if (AppPath == "") {
                AppPath = String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + FileSelector_Dialog.sRoot + PubVar.SystemFolderName;
                File dir = new File(AppPath);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                AppPath = dir.getAbsolutePath();
            }
        } catch (Exception e) {
        }
        return AppPath;
    }

    public static String GetSDCardPath() {
        return GetAPPPath();
    }

    public static int GetSelectObjectsCount(Map map) {
        return GetSelectObjectsCount(map, -1, -1, true);
    }

    public static int GetSelectObjectsCount(Map map, int paramInt1, int geoType, boolean considerBKVector) {
        int totalCount = 0;
        int tid = map.getVectorGeoLayers().size();
        for (GeoLayer tmpGeoLayer : map.getGeoLayers().getList()) {
            Selection tmpSelection = map.getSelectionByIndex(tid, false);
            if (tmpSelection != null && tmpSelection.getCount() > 0) {
                if (geoType == -1) {
                    totalCount += tmpSelection.getCount();
                } else if (tmpGeoLayer.getType() == EGeoLayerType.POINT && geoType == 0) {
                    totalCount += tmpSelection.getCount();
                } else if (tmpGeoLayer.getType() == EGeoLayerType.POLYLINE && geoType == 1) {
                    totalCount += tmpSelection.getCount();
                } else if (tmpGeoLayer.getType() == EGeoLayerType.POLYGON && geoType == 2) {
                    totalCount += tmpSelection.getCount();
                }
            }
            tid++;
        }
        if (considerBKVector) {
            int tid2 = 0;
            for (GeoLayer tmpGeoLayer2 : map.getVectorGeoLayers().getList()) {
                Selection tmpSelection2 = map.getSelectionByIndex(tid2, false);
                if (tmpSelection2 != null && tmpSelection2.getCount() > 0) {
                    if (geoType == -1) {
                        totalCount += tmpSelection2.getCount();
                    } else if (tmpGeoLayer2.getType() == EGeoLayerType.POINT && geoType == 0) {
                        totalCount += tmpSelection2.getCount();
                    } else if (tmpGeoLayer2.getType() == EGeoLayerType.POLYLINE && geoType == 1) {
                        totalCount += tmpSelection2.getCount();
                    } else if (tmpGeoLayer2.getType() == EGeoLayerType.POLYGON && geoType == 2) {
                        totalCount += tmpSelection2.getCount();
                    }
                }
                tid2++;
            }
        }
        return totalCount;
    }

    public static double CalculateAngle(double deltaX, double deltaY) {
        if (deltaX == 0.0d) {
            if (deltaY < 0.0d) {
                return 180.0d;
            }
            return 0.0d;
        } else if (deltaY != 0.0d) {
            double result = (Math.atan(deltaX / deltaY) * 180.0d) / 3.141592653589793d;
            if (result <= 0.0d) {
                double result2 = result + 180.0d;
                if (deltaX < 0.0d) {
                    return result2 + 180.0d;
                }
                return result2;
            } else if (deltaX < 0.0d) {
                return result + 180.0d;
            } else {
                return result;
            }
        } else if (deltaX > 0.0d) {
            return 90.0d;
        } else {
            return 270.0d;
        }
    }

    public static List<HashMap<String, Object>> GetSelectObjects(Map map, int paramInt1, int geoType, boolean considerBKVector, BasicValue editGeoCount) {
        Selection pSel;
        List<HashMap<String, Object>> result = new ArrayList<>();
        editGeoCount.setValue(0);
        int tmpEditGeoCount = 0;
        int tid = map.getVectorGeoLayers().size() - 1;
        for (GeoLayer localGeoLayer : map.getGeoLayers().getList()) {
            tid++;
            int j = 0;
            if (paramInt1 == 1) {
                j = 0;
                if (!localGeoLayer.getDataset().getDataSource().getEditing()) {
                    j = 1;
                }
            }
            if (paramInt1 == 2 && localGeoLayer.getDataset().getDataSource().getEditing()) {
                j = 1;
            }
            if (paramInt1 == -1) {
                j = 1;
            }
            if (j != 0) {
                boolean tmpBool = false;
                if (geoType == -1) {
                    tmpBool = true;
                } else if (localGeoLayer.getType() == EGeoLayerType.POINT && geoType == 0) {
                    tmpBool = true;
                } else if (localGeoLayer.getType() == EGeoLayerType.POLYLINE && geoType == 1) {
                    tmpBool = true;
                } else if (localGeoLayer.getType() == EGeoLayerType.POLYGON && geoType == 2) {
                    tmpBool = true;
                }
                if (tmpBool && (pSel = map.getSelectionByIndex(tid, false)) != null && pSel.getCount() > 0) {
                    tmpEditGeoCount += pSel.getCount();
                    for (Integer num : pSel.getGeometryIndexList()) {
                        HashMap<String, Object> tmpHash = new HashMap<>();
                        tmpHash.put("LayerName", localGeoLayer.getLayerName());
                        tmpHash.put("LayerID", localGeoLayer.getLayerID());
                        tmpHash.put("DataSource", localGeoLayer.getDataset().getDataSource().getName());
                        tmpHash.put("Editable", Boolean.valueOf(localGeoLayer.getEditable()));
                        tmpHash.put("GeoType", localGeoLayer.getGeoTypeName());
                        int tmpGeoIndex = num.intValue();
                        tmpHash.put("GeoIndex", Integer.valueOf(tmpGeoIndex));
                        tmpHash.put("Geometry", localGeoLayer.getDataset().GetGeometry(tmpGeoIndex));
                        result.add(tmpHash);
                    }
                }
            }
        }
        editGeoCount.setValue(tmpEditGeoCount);
        if (considerBKVector) {
            int tid2 = -1;
            for (GeoLayer localGeoLayer2 : map.getVectorGeoLayers().getList()) {
                tid2++;
                int j2 = 0;
                if (paramInt1 == 1) {
                    j2 = 0;
                    if (!localGeoLayer2.getDataset().getDataSource().getEditing()) {
                        j2 = 1;
                    }
                }
                if (paramInt1 == 2 && localGeoLayer2.getDataset().getDataSource().getEditing()) {
                    j2 = 1;
                }
                if (paramInt1 == -1) {
                    j2 = 1;
                }
                if (j2 != 0) {
                    boolean tmpBool2 = false;
                    if (geoType == -1) {
                        tmpBool2 = true;
                    } else if (localGeoLayer2.getType() == EGeoLayerType.POINT && geoType == 0) {
                        tmpBool2 = true;
                    } else if (localGeoLayer2.getType() == EGeoLayerType.POLYLINE && geoType == 1) {
                        tmpBool2 = true;
                    } else if (localGeoLayer2.getType() == EGeoLayerType.POLYGON && geoType == 2) {
                        tmpBool2 = true;
                    }
                    if (tmpBool2) {
                        Selection pSel2 = map.getSelectionByIndex(tid2, false);
                        if (pSel2.getCount() > 0) {
                            for (Integer num2 : pSel2.getGeometryIndexList()) {
                                HashMap<String, Object> tmpHash2 = new HashMap<>();
                                tmpHash2.put("LayerName", localGeoLayer2.getLayerName());
                                tmpHash2.put("LayerID", localGeoLayer2.getLayerID());
                                tmpHash2.put("DataSourc", localGeoLayer2.getDataset().getDataSource().getName());
                                tmpHash2.put("Editable", Boolean.valueOf(localGeoLayer2.getEditable()));
                                tmpHash2.put("GeoType", localGeoLayer2.getGeoTypeName());
                                int tmpGeoIndex2 = num2.intValue();
                                tmpHash2.put("GeoIndex", Integer.valueOf(tmpGeoIndex2));
                                tmpHash2.put("Geometry", localGeoLayer2.getDataset().GetGeometry(tmpGeoIndex2));
                                result.add(tmpHash2);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    public static List<HashMap<String, Object>> GetSelectObjectsInLayer(Map map, String layerID, boolean isVectorLayer, BasicValue editGeoCount) {
        List<HashMap<String, Object>> result = new ArrayList<>();
        editGeoCount.setValue(0);
        int tmpEditGeoCount = 0;
        if (!isVectorLayer) {
            Iterator<GeoLayer> tempIter = map.getGeoLayers().getList().iterator();
            int tid = map.getVectorGeoLayers().size() - 1;
            while (true) {
                if (!tempIter.hasNext()) {
                    break;
                }
                GeoLayer localGeoLayer = tempIter.next();
                tid++;
                if (localGeoLayer.getLayerID().equals(layerID)) {
                    Selection pSel = map.getSelectionByIndex(tid, false);
                    if (pSel != null && pSel.getCount() > 0) {
                        tmpEditGeoCount = 0 + pSel.getCount();
                        for (Integer num : pSel.getGeometryIndexList()) {
                            HashMap<String, Object> tmpHash = new HashMap<>();
                            tmpHash.put("LayerName", localGeoLayer.getLayerName());
                            tmpHash.put("LayerID", localGeoLayer.getLayerID());
                            tmpHash.put("DataSource", localGeoLayer.getDataset().getDataSource().getName());
                            tmpHash.put("Editable", Boolean.valueOf(localGeoLayer.getEditable()));
                            tmpHash.put("GeoType", localGeoLayer.getGeoTypeName());
                            int tmpGeoIndex = num.intValue();
                            tmpHash.put("GeoIndex", Integer.valueOf(tmpGeoIndex));
                            tmpHash.put("Geometry", localGeoLayer.getDataset().GetGeometry(tmpGeoIndex));
                            result.add(tmpHash);
                        }
                    }
                }
            }
        } else {
            int tid2 = -1;
            for (GeoLayer localGeoLayer2 : map.getVectorGeoLayers().getList()) {
                tid2++;
                Selection pSel2 = map.getSelectionByIndex(tid2, false);
                if (pSel2.getCount() > 0) {
                    for (Object obj : pSel2.getGeometryIndexList()) {
                        HashMap<String, Object> tmpHash2 = new HashMap<>();
                        tmpHash2.put("LayerName", localGeoLayer2.getLayerName());
                        tmpHash2.put("LayerID", localGeoLayer2.getLayerID());
                        tmpHash2.put("DataSourc", localGeoLayer2.getDataset().getDataSource().getName());
                        tmpHash2.put("Editable", Boolean.valueOf(localGeoLayer2.getEditable()));
                        tmpHash2.put("GeoType", localGeoLayer2.getGeoTypeName());
                        tmpHash2.put("GeoIndex", obj);
                        result.add(tmpHash2);
                    }
                }
            }
        }
        editGeoCount.setValue(tmpEditGeoCount);
        return result;
    }

    public static boolean GetSelectOneObjectInfo(Map map, BasicValue layerIDParam, BasicValue geoIndexParam, BasicValue dataSourceNameParam) {
        return GetSelectOneObjectInfo(map, layerIDParam, geoIndexParam, new BasicValue(), dataSourceNameParam);
    }

    public static boolean GetSelectOneObjectInfo(Map map, BasicValue layerIDParam, BasicValue geoIndexParam, BasicValue geoSYS_IDParam, BasicValue dataSourceNameParam) {
        int i = 0;
        int tid = map.getVectorGeoLayers().size() - 1;
        for (GeoLayer localGeoLayer : map.getGeoLayers().getList()) {
            tid++;
            Selection tmpSel = map.getSelectionByIndex(tid, false);
            if (tmpSel != null) {
                i += tmpSel.getCount();
                if (tmpSel.getCount() == 1) {
                    int tempIndex = tmpSel.getGeometryIndexList().get(0).intValue();
                    AbstractGeometry pGeo = localGeoLayer.getDataset().GetGeometry(tempIndex);
                    if (pGeo != null) {
                        geoSYS_IDParam.setValue(pGeo.GetSYS_ID());
                    } else {
                        geoSYS_IDParam.setValue((String) null);
                    }
                    geoIndexParam.setValue(tempIndex);
                    layerIDParam.setValue(localGeoLayer.getName());
                    dataSourceNameParam.setValue(localGeoLayer.getDataset().getDataSource().getName());
                }
            }
        }
        if (i != 1) {
            int tid2 = -1;
            for (GeoLayer localGeoLayer2 : map.getVectorGeoLayers().getList()) {
                tid2++;
                Selection tmpSel2 = map.getSelectionByIndex(tid2, false);
                if (tmpSel2 != null) {
                    i += tmpSel2.getCount();
                    if (tmpSel2.getCount() == 1) {
                        int tempIndex2 = tmpSel2.getGeometryIndexList().get(0).intValue();
                        AbstractGeometry pGeo2 = localGeoLayer2.getDataset().GetGeometry(tempIndex2);
                        if (pGeo2 != null) {
                            geoSYS_IDParam.setValue(pGeo2.GetSYS_ID());
                        } else {
                            geoSYS_IDParam.setValue((String) null);
                        }
                        geoIndexParam.setValue(tempIndex2);
                        layerIDParam.setValue(localGeoLayer2.getName());
                        dataSourceNameParam.setValue(localGeoLayer2.getDataset().getDataSource().getName());
                    }
                }
            }
        }
        return i > 0;
    }

    public static void SetSpinnerListValue(String value, View view) {
        ((SpinnerList) view).SetTextJust(value);
    }

    public static String GetSpinnerValueOnID(Dialog dialog, int viewID) {
        return GetViewValue(dialog.findViewById(viewID));
    }

    public static void SetSpinnerListData(Dialog paramDialog, String title, List<String> paramList, int viewID) {
        SetSpinnerListData(paramDialog, title, paramList, viewID, (ICallback) null);
    }

    public static void SetSpinnerListData(Dialog paramDialog, String title, List<String> paramList, int viewID, ICallback callback) {
        SetSpinnerListData(paramDialog.getContext(), title, paramList, paramDialog.findViewById(viewID), callback);
    }

    public static void SetSpinnerListData(Dialog paramDialog, String defaultValue, String title, List<String> paramList, int viewID, ICallback callback) {
        SetSpinnerListData(paramDialog, viewID, defaultValue, title, paramList, (String) null, callback);
    }

    public static void SetSpinnerListData(Dialog paramDialog, String title, String[] paramArrayOfString, int viewID) {
        SetSpinnerListData(paramDialog, title, StrArrayToList(paramArrayOfString), viewID);
    }

    public static void SetSpinnerListData(Context paramContext, String title, List<String> paramList, View view) {
        SetSpinnerListData(paramContext, title, paramList, view, (ICallback) null);
    }

    public static void SetSpinnerListData(Context paramContext, String title, List<String> paramList, View view, ICallback callback) {
        SetSpinnerListData(paramContext, view, (String) null, title, paramList, (String) null, callback);
    }

    public static void SetSpinnerListData(Dialog dialog, int viewID, String defaultValue, String title, List<String> listValues, String returnBackTag, ICallback tmpICallback) {
        SetSpinnerListData(dialog.getContext(), dialog.findViewById(viewID), defaultValue, title, listValues, returnBackTag, tmpICallback);
    }

    @SuppressLint("ResourceType")
    public static void SetSpinnerListData(Context context, View view, String defaultValue, String title, List<String> listValues, final String returnBackTag, final ICallback tmpICallback) {
        try {
            String str = view.getClass().getName();
            if (str.equals("android.widget.Spinner") || str.equals(" com.xzy.forestSystem.gogisapi.MyControls.SpinnerDialog")) {
                ArrayList localArrayList = new ArrayList();
                for (String str2 : listValues) {
                    localArrayList.add(str2);
                }
                @SuppressLint("ResourceType") ArrayAdapter localArrayAdapter = new ArrayAdapter(context, 17367048, localArrayList);
                localArrayAdapter.setDropDownViewResource(17367049);
                Spinner localSpinner = (Spinner) view;
                localSpinner.setAdapter((SpinnerAdapter) localArrayAdapter);
                localSpinner.setPrompt(title);
                if (tmpICallback != null) {
                    localSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.3
                        @Override // android.widget.AdapterView.OnItemSelectedListener
                        public void onItemSelected(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
                            if (returnBackTag == null) {
                                tmpICallback.OnClick("OnItemSelected", String.valueOf(paramAdapterView.getSelectedItem()));
                            } else {
                                tmpICallback.OnClick(returnBackTag, String.valueOf(paramAdapterView.getSelectedItem()));
                            }
                        }

                        @Override // android.widget.AdapterView.OnItemSelectedListener
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            } else if (str.equals("com.xzy.forestSystem.gogisapi.MyControls.SpinnerList")) {
                SpinnerList tmpSpinner = (SpinnerList) view;
                if (defaultValue == null) {
                    if (listValues == null || listValues.size() <= 0) {
                        defaultValue = "";
                    } else {
                        defaultValue = listValues.get(0);
                    }
                }
                tmpSpinner.SetDataList(listValues);
                tmpSpinner.SetSelectReturnTag(returnBackTag);
                tmpSpinner.SetDialogTitle(title);
                tmpSpinner.SetCallback(tmpICallback);
                tmpSpinner.SetText(defaultValue);
            }
        } catch (Exception ex) {
            Log("SetSpinnerListData", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    public static String GetSystemDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String GetTextValueOnID(Activity paramActivity, int paramInt) {
        return String.valueOf(((TextView) paramActivity.findViewById(paramInt)).getText());
    }

    public static String GetTextValueOnID(Dialog paramDialog, int paramInt) {
        return String.valueOf(((TextView) paramDialog.findViewById(paramInt)).getText());
    }

    public static String GetEditTextValueOnID(Dialog paramDialog, int paramInt) {
        return String.valueOf(((EditText) paramDialog.findViewById(paramInt)).getText());
    }

    public static TextView GetTextViewOnID(Activity paramActivity, int paramInt) {
        return (TextView) paramActivity.findViewById(paramInt);
    }

    public static TextView GetTextViewOnID(Dialog paramDialog, int paramInt) {
        View tempView = paramDialog.findViewById(paramInt);
        if (tempView != null) {
            return (TextView) tempView;
        }
        return null;
    }

    public static EditText GetEditTextOnID(Dialog paramDialog, int paramInt) {
        return (EditText) paramDialog.findViewById(paramInt);
    }

    public static double GetTwoPointDistance(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        return Math.sqrt(((paramDouble1 - paramDouble3) * (paramDouble1 - paramDouble3)) + ((paramDouble2 - paramDouble4) * (paramDouble2 - paramDouble4)));
    }

    public static double GetTwoPointDistance(Coordinate tmpCoord1, Coordinate tmpCoord2) {
        return GetTwoPointDistance(tmpCoord1.getX(), tmpCoord1.getY(), tmpCoord2.getX(), tmpCoord2.getY());
    }

    public static double GetTwoPointDistanceOfWGS84(Coordinate tmpCoord1, Coordinate tmpCoord2) {
        Coordinate localCoordinate1 = Coordinate_WGS1984.BLToXY(tmpCoord1.getX(), tmpCoord1.getY());
        Coordinate localCoordinate2 = Coordinate_WGS1984.BLToXY(tmpCoord2.getX(), tmpCoord2.getY());
        return Math.sqrt(((localCoordinate1.getX() - localCoordinate2.getX()) * (localCoordinate1.getX() - localCoordinate2.getX())) + ((localCoordinate1.getY() - localCoordinate2.getY()) * (localCoordinate1.getY() - localCoordinate2.getY())));
    }

    public static List<String> GetValidMapSubPath() {
        ArrayList localArrayList = new ArrayList();
        File[] arrayOfFile = new File(String.valueOf(PubVar.m_SystemPath) + "/Map").listFiles();
        if (arrayOfFile != null && arrayOfFile.length > 0) {
            for (File tempFile : arrayOfFile) {
                if (tempFile.isDirectory() && CheckExistFile(String.valueOf(tempFile.getAbsolutePath()) + "/TP.dbx") && CheckExistFile(String.valueOf(tempFile.getAbsolutePath()) + "/TP.idx")) {
                    localArrayList.add(tempFile.getName());
                }
            }
        }
        return localArrayList;
    }

    private static double GetdPoint(double paramDouble) {
        return (double) new BigDecimal(Double.toString(paramDouble)).subtract(new BigDecimal(Integer.toString((int) paramDouble))).floatValue();
    }

    public static String CombineIntegers(String paramString, List<Integer> paramList) {
        String str = "";
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            str = String.valueOf(str) + String.valueOf(localIterator.next()) + paramString;
        }
        return str.substring(0, str.length() - paramString.length());
    }

    public static String CombineStrings(String paramString, List paramList) {
        if (paramList.size() == 0) {
            return "";
        }
        StringBuilder tempSB = new StringBuilder();
        for (Object obj : paramList) {
            if (tempSB.length() > 0) {
                tempSB.append(paramString);
            }
            tempSB.append(String.valueOf(obj));
        }
        return tempSB.toString();
    }

    public static String CombineStrings(String splitChar, Object[] objs) {
        if (objs == null || objs.length == 0) {
            return "";
        }
        StringBuilder tempSB = new StringBuilder();
        for (Object tmpObj : objs) {
            if (tempSB.length() > 0) {
                tempSB.append(splitChar);
            }
            tempSB.append(String.valueOf(tmpObj));
        }
        return tempSB.toString();
    }

    public static String JoinWithKey(String splitChar, HashMap<String, String> hashMap) {
        if (hashMap.size() == 0) {
            return "";
        }
        String str1 = "";
        Iterator iter = hashMap.entrySet().iterator();
        while (iter.hasNext()) {
//            str1 = String.valueOf(str1) + iter.next().getKey().toString() + splitChar;
        }
        return str1.substring(0, str1.length() - splitChar.length());
    }

    public static String JoinWithValue(String splitChar, HashMap<String, String> hashMap) {
        if (hashMap.size() == 0) {
            return "";
        }
        String str1 = "";
        Iterator iter = hashMap.entrySet().iterator();
        while (iter.hasNext()) {
//            str1 = String.valueOf(str1) + iter.next().getValue().toString() + splitChar;
        }
        return str1.substring(0, str1.length() - splitChar.length());
    }

    public static String JoinSQL(String splitChar, List<String> valueList, List<String> typeList) {
        if (valueList.size() == 0 || valueList.size() != typeList.size()) {
            return "";
        }
        String str1 = "";
        Iterator<String> localIterator = valueList.iterator();
        Iterator<String> localIterator2 = typeList.iterator();
        while (localIterator.hasNext() && localIterator2.hasNext()) {
            String str2 = localIterator.next();
            if (str2 == null) {
                str2 = "";
            }
            String tempType = localIterator2.next().toUpperCase();
            if (tempType.equals("INTEGER") || tempType.equals("DOUBLE") || tempType.equals("REAL") || tempType.equals("FLOAT")) {
                str1 = String.valueOf(str1) + str2 + splitChar;
            } else {
                str1 = String.valueOf(str1) + "'" + str2 + "'" + splitChar;
            }
        }
        return str1.substring(0, str1.length() - splitChar.length());
    }

    public static String Joins(String paramString, String[] paramArrayOfString) {
        String str1 = "";
        for (int j = 0; j < paramArrayOfString.length; j++) {
            str1 = String.valueOf(str1) + String.valueOf(paramArrayOfString[j]) + paramString;
        }
        return str1.substring(0, str1.length() - paramString.length());
    }

    public static void ShowProgressDialog(ICallback tmpICallback) {
        ShowProgressDialog("正在加载数据，请稍候...", tmpICallback);
    }

    public static void ShowProgressDialog(String message, final ICallback tmpICallback) {
        final LoadingDialog dialog = new LoadingDialog(PubVar.MainContext);
        dialog.setTitle(message);
        dialog.setCancelable(false);
        dialog.setBindCallback(tmpICallback);
        dialog.show();
        new Handler().postDelayed(new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.4
            @Override // java.lang.Runnable
            public void run() {
//                ICallback.this.OnClick("OK", null);
                dialog.dismiss();
            }
        }, 500);
    }

    public static void ShowProgressDialogWithoutClose(String message, final ICallback tmpICallback) {
        final LoadingDialog dialog = new LoadingDialog(PubVar.MainContext);
        dialog.setTitle(message);
        dialog.setCancelable(false);
        dialog.setBindCallback(tmpICallback);
        dialog.show();
        new Handler().postDelayed(new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.5
            @Override // java.lang.Runnable
            public void run() {
//                ICallback.this.OnClick("OK", dialog.getHandler());
            }
        }, 200);
    }

    public static void ShowProgressDialog3(String message, ICallback tmpICallback) {
        final LoadingDialog dialog = new LoadingDialog(PubVar.MainContext);
        dialog.setTitle(message);
        dialog.setCancelable(false);
        dialog.setBindCallback(tmpICallback);
        dialog.show();
        new Handler().postDelayed(new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.6
            @Override // java.lang.Runnable
            public void run() {
//                Handler tmphander = LoadingDialog.this.getHandler();
//                Message tmpMsg = tmphander.obtainMessage();
//                tmpMsg.what = 2;
//                tmphander.sendMessage(tmpMsg);
            }
        }, 500);
    }

    public static void OpenProgressDialog2(String paramString, final ICallback tmpICallback) {
        final MyProgressDialog localProgressDialog = new MyProgressDialog(PubVar._PubCommand.m_Context);
        localProgressDialog.SetHeadTitle("提示");
        localProgressDialog.SetMessage(paramString);
        localProgressDialog.setCancelable(false);
        localProgressDialog.show();
        new Handler().postDelayed(new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.7
            @Override // java.lang.Runnable
            public void run() {
//                ICallback.this.OnClick("OK", null);
                localProgressDialog.dismiss();
            }
        }, 100);
    }

    public static String PadLeft(String paramString, int paramInt) {
        if (CalStrLength(paramString) >= paramInt) {
            return paramString;
        }
        String str = "";
        for (int i = 1; i <= paramInt - CalStrLength(paramString); i++) {
            str = String.valueOf(str) + "  ";
        }
        return String.valueOf(str) + paramString;
    }

    public static String SimplifyArea(double value, boolean withUnit) {
        if (PubVar.AreaUnitType == 1) {
            if (value < 1000000.0d) {
                DecimalFormat localDecimalFormat1 = new DecimalFormat("0.0");
                if (value < 10.0d) {
                    localDecimalFormat1 = new DecimalFormat("0.0000");
                }
                String result = new StringBuilder(String.valueOf(localDecimalFormat1.format(value))).toString();
                return withUnit ? String.valueOf(result) + "(平方米)" : result;
            }
            String result2 = new StringBuilder(String.valueOf(new DecimalFormat("0.0000").format(value / 1000000.0d))).toString();
            if (withUnit) {
                return String.valueOf(result2) + "(平方公里)";
            }
            return result2;
        } else if (PubVar.AreaUnitType == 2) {
            String result3 = new DecimalFormat("0.0000").format(ConvertAreaOfUnit(value));
            if (withUnit) {
                return String.valueOf(result3) + "(亩)";
            }
            return result3;
        } else if (PubVar.AreaUnitType != 3) {
            return "";
        } else {
            String result4 = new DecimalFormat("0.0000").format(ConvertAreaOfUnit(value));
            if (withUnit) {
                return String.valueOf(result4) + "(公顷)";
            }
            return result4;
        }
    }

    public static double ConvertAreaOfUnit(double area) {
        if (PubVar.AreaUnitType == 2) {
            return area * 0.0015d;
        }
        if (PubVar.AreaUnitType == 3) {
            return area * 1.0E-4d;
        }
        return area;
    }

    public static String SimplifyLength(double paramDouble) {
        return SimplifyLength(paramDouble, true);
    }

    public static String SimplifyLength(double paramDouble, boolean paramBoolean) {
        if (paramDouble < 1000.0d) {
            String result = new StringBuilder(String.valueOf(new DecimalFormat("0.0").format(paramDouble))).toString();
            if (paramBoolean) {
                return String.valueOf(result) + "(米)";
            }
            return result;
        }
        String result2 = new StringBuilder(String.valueOf(new DecimalFormat("0.000").format(paramDouble / 1000.0d))).toString();
        if (paramBoolean) {
            return String.valueOf(result2) + "(公里)";
        }
        return result2;
    }

    public static String SimplifyTime(long time) {
        if (time < 1000) {
            return String.valueOf("") + time + "ms";
        }
        long time2 = time / 1000;
        if (time2 < 60) {
            return String.valueOf("") + time2 + "s";
        }
        long tmpMin = time2 / 60;
        long tmpSec = time2 % 60;
        if (tmpMin < 60) {
            return String.valueOf("") + tmpMin + "分" + tmpSec + "秒";
        }
        return String.valueOf("") + (tmpMin / 60) + "小时" + (tmpMin % 60) + "分" + tmpSec + "秒";
    }

    public static String ReadConfigItem(String paramString) {
        try {
            String str1 = String.valueOf(PubVar._Map.getSystemPath()) + "/SysFile/Config.ini";
            if (!CheckExistFile(str1)) {
                return "";
            }
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader);
            String str2 = "";
            while (true) {
                String str3 = localBufferedReader.readLine();
                if (str3 == null) {
                    localBufferedReader.close();
                    localFileReader.close();
                    return str2;
                } else if (str3.indexOf(paramString) == 0) {
                    str2 = str3.split("=")[1];
                }
            }
        } catch (Exception e) {
            return "";
        }
    }

    @SuppressLint("WrongConstant")
    public static int[] GetViewSize(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        v.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        return new int[]{location[0], location[1] - PubVar.ScreenStatusHeight, v.getMeasuredWidth(), v.getMeasuredHeight()};
    }

    public static boolean RestoreViewExtend() {
        String str = ReadConfigItem("ViewExtend");
        if (str.equals("")) {
            return false;
        }
        String[] arrayOfString = str.split(",");
        if (arrayOfString.length != 4) {
            return false;
        }
        PubVar._Map.setExtend(new Envelope(Double.parseDouble(arrayOfString[0]), Double.parseDouble(arrayOfString[1]), Double.parseDouble(arrayOfString[2]), Double.parseDouble(arrayOfString[3])));
        PubVar._Map.Refresh();
        return true;
    }

    public static double Save3Point(double paramDouble) {
        return Double.valueOf(new DecimalFormat("#.000").format(paramDouble)).doubleValue();
    }

    public static List<String> StrArrayToList(String[] paramArrayOfString) {
        ArrayList localArrayList = new ArrayList();
        for (String str : paramArrayOfString) {
            localArrayList.add(str);
        }
        return localArrayList;
    }

    public static String[] StrListToArray(List<String> paramList) {
        String[] arrayOfString = new String[paramList.size()];
        int i = 0;
        for (String str : paramList) {
            arrayOfString[i] = str;
            i++;
        }
        return arrayOfString;
    }

    public static List<String> SubStringList(List<String> origList, int start) {
        ArrayList localArrayList = new ArrayList();
        int i = origList.size();
        for (int j = start; j < i; j++) {
            localArrayList.add(origList.get(j));
        }
        return localArrayList;
    }

    public static String StrListToStr(List<String> paramList) {
        String str1 = "";
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            str1 = String.valueOf(str1) + localIterator.next() + ",";
        }
        return str1.length() != 0 ? str1.substring(0, str1.length() - 1) : "";
    }

    public static void ShowDialog(Context paramContext, String paramString) {
        ShowDialog(paramContext, paramString, "提示", ViewCompat.MEASURED_STATE_MASK, null);
    }

    public static void ShowDialog(Context paramContext, String msg, String title) {
        ShowDialog(paramContext, msg, title, ViewCompat.MEASURED_STATE_MASK, null);
    }

    public static void ShowDialog(Context context, String msg, String title, ICallback pICallback) {
        ShowDialog(context, msg, title, ViewCompat.MEASURED_STATE_MASK, pICallback);
    }

    public static void ShowDialog(Context context, String msg, String title, int messageColor, final ICallback pICallback) {
        BaseDialog.Builder builder = new BaseDialog.Builder(context);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setMessageColor(messageColor);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                if (ICallback.this != null) {
//                    ICallback.this.OnClick("确定", "");
//                }
                paramDialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public static void ShowDialog(Context context, String msg, String title, int messageColor, final ICallback pICallback, ICallback clickCallback) {
        BaseDialog.Builder builder = new BaseDialog.Builder(context);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setMessageColor(messageColor);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.9
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                if (ICallback.this != null) {
//                    ICallback.this.OnClick("确定", "");
//                }
                paramDialogInterface.dismiss();
            }
        });
        BaseDialog tmpBaseDialog = builder.create();
        builder.SetLongClickTxt(clickCallback);
        tmpBaseDialog.show();
    }

    public static void ShowDialog(String paramString) {
        ShowDialog(PubVar.MainContext, paramString);
    }

    public static void ShowDialog(String msg, String title) {
        ShowDialog(PubVar.MainContext, msg, title);
    }

    public static void ShowDialog(String msg, String title, int messageColor) {
        ShowDialog(PubVar.MainContext, msg, title, messageColor, null);
    }

    @SuppressLint("WrongConstant")
    public static void ShowToast(String msg) {
        try {
            if (m_toast == null) {
                m_toast = Toast.makeText(PubVar.MainContext, msg, 0);
            } else {
                m_toast.setText(msg);
            }
            m_toast.show();
        } catch (Exception e) {
        }
    }

    public static void ShowYesNoDialog(Context context, String msg, ICallback pCallback) {
        ShowYesNoDialog(context, msg, ViewCompat.MEASURED_STATE_MASK, pCallback);
    }

    public static void ShowYesNoDialog(Context context, String msg, int messageColor, String yesMsg, String noMsg, final ICallback ICallback) {
        BaseDialog.Builder builder = new BaseDialog.Builder(context);
        builder.setMessage(msg);
        builder.setTitle("提示");
        builder.setMessageColor(messageColor);
        builder.setPositiveButton(yesMsg, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                if (ICallback.this != null) {
//                    ICallback.this.OnClick("YES", "");
//                }
                paramDialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(noMsg, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.11
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                ICallback.this.OnClick("NO", "");
                paramDialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public static void ShowYesNoDialog(Context context, String msg, int messageColor, String title, String yesMsg, String noMsg, final ICallback ICallback) {
        BaseDialog.Builder builder = new BaseDialog.Builder(context);
        builder.setMessage(msg);
        builder.setTitle(title);
        builder.setMessageColor(messageColor);
        builder.setPositiveButton(yesMsg, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.12
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                if (ICallback.this != null) {
//                    ICallback.this.OnClick("YES", "");
//                }
                paramDialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(noMsg, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.13
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                ICallback.this.OnClick("NO", "");
                paramDialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public static void ShowYesNoDialog(Context context, String msg, int messageColor, final ICallback ICallback) {
        BaseDialog.Builder builder = new BaseDialog.Builder(context);
        builder.setMessage(msg);
        builder.setTitle("提示");
        builder.setMessageColor(messageColor);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.14
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                if (ICallback.this != null) {
//                    ICallback.this.OnClick("YES", "");
//                }
                paramDialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.15
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                ICallback.this.OnClick("NO", "");
                paramDialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public static void ShowYesNoDialogWithAlert(Context context, String msg, boolean isAlert, final ICallback ICallback) {
        BaseDialog.Builder builder = new BaseDialog.Builder(context);
        builder.setMessage(msg);
        builder.setTitle("提示");
        if (isAlert) {
            builder.setIsAlert();
        }
        builder.setMessageColor(ViewCompat.MEASURED_STATE_MASK);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.16
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                if (ICallback.this != null) {
//                    ICallback.this.OnClick("YES", "");
//                }
                paramDialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.17
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                ICallback.this.OnClick("NO", "");
                paramDialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public static void SetTextViewValueOnID(Dialog paramDialog, int paramInt, String paramString) {
        TextView tempView = GetTextViewOnID(paramDialog, paramInt);
        if (tempView != null) {
            tempView.setText(paramString);
        }
    }

    public static void SetTextViewValueOnID(Dialog paramDialog, int paramInt, int paramString) {
        GetTextViewOnID(paramDialog, paramInt).setText(String.valueOf(paramString));
    }

    public static void SetEditTextValueOnID(Dialog paramDialog, int paramInt, String paramString) {
        GetEditTextOnID(paramDialog, paramInt).setText(paramString);
    }

    public static void SetValueToView(String paramString, View paramView) {
        if (paramView != null) {
            String str = paramView.getClass().getName();
            if (str.equals("android.widget.EditText")) {
                ((EditText) paramView).setText(paramString);
            } else if (str.equals("android.widget.TextView")) {
                ((TextView) paramView).setText(paramString);
            } else if (str.equals("android.widget.Spinner")) {
                Spinner localSpinner2 = (Spinner) paramView;
                if (((ArrayAdapter) localSpinner2.getAdapter()) != null) {
                    localSpinner2.setSelection(((ArrayAdapter) localSpinner2.getAdapter()).getPosition(paramString), true);
                }
            }
            if (str.equals("android.widget.Button")) {
                ((Button) paramView).setText(paramString);
            } else if (str.equals(" com.xzy.forestSystem.gogisapi.MyControls.SpinnerDialog")) {
                SpinnerDialog localSpinner22 = (SpinnerDialog) paramView;
                SetSpinnerListData(paramView.getContext(), "", StrArrayToList(new String[]{paramString}), localSpinner22);
                localSpinner22.setSelection(0);
            } else if (str.equals("com.xzy.forestSystem.gogisapi.MyControls.InputSpinner")) {
                ((InputSpinner) paramView).getEditTextView().setText(paramString);
            } else if (str.equals("com.xzy.forestSystem.gogisapi.MyControls.SpinnerList")) {
                ((SpinnerList) paramView).SetText(paramString);
            } else if (str.equals("com.xzy.forestSystem.gogisapi.MyControls.AutoInputSpinner")) {
                ((AutoInputSpinner) paramView).setText(paramString);
            }
        }
    }

    public static boolean IsFloat(String paramString) {
        try {
            Float.parseFloat(paramString);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void Log(String Tag, String msg) {
        Log.v(Tag, msg);
    }

    public static String GetViewValue(Dialog dialog, int viewID) {
        View tmpView = dialog.findViewById(viewID);
        if (tmpView != null) {
            return GetViewValue(tmpView);
        }
        return "";
    }

    public static String GetViewValue(View paramView) {
        if (paramView == null) {
            return "";
        }
        String str = paramView.getClass().getName();
        if (str.equals("android.widget.EditText")) {
            return String.valueOf(((EditText) paramView).getText());
        }
        if (str.equals("android.widget.Spinner")) {
            return String.valueOf(((Spinner) paramView).getSelectedItem());
        }
        if (str.equals(" com.xzy.forestSystem.gogisapi.MyControls.SpinnerDialog")) {
            return String.valueOf(((SpinnerDialog) paramView).getSelectedItem());
        }
        if (str.equals("com.xzy.forestSystem.gogisapi.MyControls.InputSpinner")) {
            return String.valueOf(((InputSpinner) paramView).getEditTextView().getEditableText().toString());
        }
        if (str.equals("android.widget.TextView")) {
            return String.valueOf(((TextView) paramView).getText());
        }
        if (str.equals("com.xzy.forestSystem.gogisapi.MyControls.SpinnerList")) {
            return ((SpinnerList) paramView).getText();
        }
        if (str.equals("com.xzy.forestSystem.gogisapi.MyControls.AutoInputSpinner")) {
            return String.valueOf(((AutoInputSpinner) paramView).getText());
        }
        return "";
    }

    public static boolean SaveImgFile(String filepath, Bitmap bmp) {
        File f = new File(filepath);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e2) {
            e2.printStackTrace();
            return false;
        }
    }

    public static byte[] readStream(String paramString) {
        try {
            FileInputStream localFileInputStream = new FileInputStream(paramString);
            byte[] tempBytes = new byte[1024];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int i = localFileInputStream.read(tempBytes); i > 0; i = localFileInputStream.read(tempBytes)) {
                bos.write(tempBytes, 0, i);
            }
            if (bos.size() <= 0) {
                return null;
            }
            byte[] result = bos.toByteArray();
            bos.close();
            localFileInputStream.close();
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] ConvertBmp2Stream(Bitmap bmp) {
        if (bmp == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 90, baos);
        return baos.toByteArray();
    }

    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes == null) {
            return null;
        }
        if (opts != null) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, opts);
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static void SetCheckValueOnID(Dialog paramDialog, int paramInt, boolean paramBoolean) {
        ((CheckBox) paramDialog.findViewById(paramInt)).setChecked(paramBoolean);
    }

    public static Bitmap CreateBitmap(int width, int height, int transparent) {
        int[] argb = new int[(width * height)];
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).getPixels(argb, 0, width, 0, 0, width, height);
        int transparent2 = (transparent * 255) / 100;
        for (int i = 0; i < argb.length; i++) {
            argb[i] = (transparent2 << 24) | (argb[i] & ViewCompat.MEASURED_SIZE_MASK);
        }
        return Bitmap.createBitmap(argb, width, height, Bitmap.Config.ARGB_8888);
    }

    public static Bitmap GetLocalBitmap(String url) {
        try {
            byte[] data = readStream(url);
            if (data == null) {
                return null;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            return BitmapFactory.decodeByteArray(data, 0, data.length, options);
        } catch (Exception e) {
            return null;
        }
    }

    public static void CoordinateBoundCheck(Coordinate coord) {
        if (coord.getX() < -180.0d) {
            coord.setX(-180.0d);
        }
        if (coord.getX() > 180.0d) {
            coord.setX(180.0d);
        }
        if (coord.getY() < -90.0d) {
            coord.setY(-90.0d);
        }
        if (coord.getY() > 90.0d) {
            coord.setY(90.0d);
        }
    }

    public static Bitmap GetLocalBitmapThumbnail(String filepath, float rotateAngle) {
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inSampleSize = 8;
        Bitmap localBitmap = BitmapFactory.decodeFile(filepath, localOptions);
        if (rotateAngle == 0.0f) {
            return localBitmap;
        }
        Matrix localMatrix = new Matrix();
        int k = localBitmap.getWidth();
        int l = localBitmap.getHeight();
        localMatrix.setRotate(rotateAngle);
        return Bitmap.createBitmap(localBitmap, 0, 0, k, l, localMatrix, true);
    }

    public static Bitmap GetVideoThumbnail(String videoPath, int kind) {
        try {
            return ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap GetVideoThumbnail(String videoPath, int width, int height, int kind) {
        try {
            return ThumbnailUtils.extractThumbnail(ThumbnailUtils.createVideoThumbnail(videoPath, kind), width, height, 2);
        } catch (Exception e) {
            return null;
        }
    }

    public static Intent OpenFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        String end = file.getName().substring(file.getName().lastIndexOf(FileSelector_Dialog.sFolder) + 1, file.getName().length()).toLowerCase();
        if (end.equals("arm") || end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return getAudioFileIntent(filePath);
        }
        if (end.equals("3gp") || end.equals("mp4")) {
            return getVideoFileIntent(filePath);
        }
        if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            return getImageFileIntent(filePath);
        }
        if (end.equals("apk")) {
            return getApkFileIntent(filePath);
        }
        if (end.equals("ppt")) {
            return getPptFileIntent(filePath);
        }
        if (end.equals("xls")) {
            return getExcelFileIntent(filePath);
        }
        if (end.equals("doc")) {
            return getWordFileIntent(filePath);
        }
        if (end.equals("pdf")) {
            return getPdfFileIntent(filePath);
        }
        if (end.equals("chm")) {
            return getChmFileIntent(filePath);
        }
        if (end.equals("txt")) {
            return getTextFileIntent(filePath, false);
        }
        return getAllIntent(filePath);
    }

    public static Intent getAllIntent(String param) {
        Intent intent = new Intent();
//        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(new File(param)), "*/*");
        return intent;
    }

    public static Intent getApkFileIntent(String param) {
        Intent intent = new Intent();
//        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/vnd.android.package-archive");
        return intent;
    }

    public static Intent getVideoFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addFlags(67108864);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(Uri.fromFile(new File(param)), "video/*");
        return intent;
    }

    public static Intent getAudioFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addFlags(67108864);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        intent.setDataAndType(Uri.fromFile(new File(param)), "audio/*");
        return intent;
    }

    public static Intent getHtmlFileIntent(String param) {
        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    public static Intent getImageFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(268435456);
        intent.setDataAndType(Uri.fromFile(new File(param)), "image/*");
        return intent;
    }

    public static Intent getPptFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(268435456);
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/vnd.ms-powerpoint");
        return intent;
    }

    public static Intent getExcelFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(268435456);
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/vnd.ms-excel");
        return intent;
    }

    public static Intent getWordFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(268435456);
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/msword");
        return intent;
    }

    public static Intent getChmFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(268435456);
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/x-chm");
        return intent;
    }

    public static Intent getTextFileIntent(String param, boolean paramBoolean) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(268435456);
        if (paramBoolean) {
            intent.setDataAndType(Uri.parse(param), "text/plain");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(param)), "text/plain");
        }
        return intent;
    }

    public static Intent getPdfFileIntent(String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(268435456);
        intent.setDataAndType(Uri.fromFile(new File(param)), "application/pdf");
        return intent;
    }

    public static String ReadTxtFileContent(String filepath) {
        try {
            FileInputStream inputStream = new FileInputStream(filepath);
            InputStreamReader inputStreamReader = null;
            try {
                inputStreamReader = new InputStreamReader(inputStream, "gbk");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuffer sb = new StringBuffer("");
            while (true) {
                try {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (sb.length() > 0) {
                        sb.append("\r\n");
                    }
                    sb.append(line);
                } catch (IOException e) {
                }
            }
            String result = sb.toString();
            inputStream.close();
            return result;
        } catch (Exception e2) {
            return null;
        }
    }

    public static void SaveHashMapFile(String filepath, HashMap<String, String> hashMap) {
        if (hashMap != null) {
            try {
                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File(filepath)), "gbk");
//                for (Map.Entry<String, String> entry : hashMap.entrySet()) {
//                    osw.write(String.valueOf(entry.getKey().toString()) + ":" + entry.getValue().toString() + "\n");
//                }
                osw.flush();
                osw.close();
            } catch (Exception e) {
            }
        }
    }

    public static void SaveTextFile(String filepath, String msg) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File(filepath)), "gbk");
            osw.write(msg);
            osw.flush();
            osw.close();
        } catch (Exception e) {
            ShowToast("提示:保存文件时" + e.getMessage());
        }
    }

    public static String ConvertDegree(double value, int type) {
        if (type == 1) {
            int tempInt = (int) value;
            return String.valueOf(String.valueOf(tempInt)) + "°" + String.format("%.6f", Double.valueOf((value - ((double) tempInt)) * 60.0d)) + "′";
        } else if (type != 2) {
            return String.valueOf(String.format("%.7f", Double.valueOf(value))) + "°";
        } else {
            int tempInt2 = (int) value;
            String result = String.valueOf(String.valueOf(tempInt2)) + "°";
            double tempD = (value - ((double) tempInt2)) * 60.0d;
            int tempInt3 = (int) tempD;
            return String.valueOf(result) + String.valueOf(tempInt3) + "′" + String.format("%.3f", Double.valueOf((tempD - ((double) tempInt3)) * 60.0d)) + "″";
        }
    }

    public static String ConvertDegree(double value) {
        return ConvertDegree(value, PubVar.GPS_Show_Format_Type);
    }

    public static File GetFileFromServer(String serverURL, ProgressDialog pd) throws Exception {
        if (!Environment.getExternalStorageState().equals("mounted")) {
            return null;
        }
        HttpURLConnection conn = (HttpURLConnection) new URL(serverURL).openConnection();
        conn.setConnectTimeout(5000);
        pd.setMax(conn.getContentLength());
        InputStream is = conn.getInputStream();
        File file = new File(String.valueOf(GetAPPPath()) + "/update.apk");
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        int total = 0;
        while (true) {
            int len = bis.read(buffer);
            if (len == -1) {
                fos.close();
                bis.close();
                is.close();
                return file;
            }
            fos.write(buffer, 0, len);
            total += len;
            pd.setProgress(total);
        }
    }

    public static UpdataInfoClass GetUpdataInfo(InputStream is) throws Exception {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "utf-8");
        UpdataInfoClass info = new UpdataInfoClass();
        for (int type = parser.getEventType(); type != 1; type = parser.next()) {
            switch (type) {
                case 2:
                    if (!"version".equals(parser.getName())) {
                        if (!"url".equals(parser.getName())) {
                            if ("description".equals(parser.getName())) {
                                info.setDescription(parser.nextText());
                                break;
                            } else {
                                break;
                            }
                        } else {
                            info.setUrl(parser.nextText());
                            break;
                        }
                    } else {
                        info.setVersion(parser.nextText());
                        break;
                    }
            }
        }
        return info;
    }

    public static List<String> DecodeJSONArray(String jsonStr, String head) {
        List<String> result = new ArrayList<>();
        try {
            JSONArray tempArray = new JSONObject(jsonStr).getJSONArray(head);
            if (tempArray != null && tempArray.length() > 0) {
                int count = tempArray.length();
                for (int i = 0; i < count; i++) {
                    result.add(tempArray.get(i).toString());
                }
            }
        } catch (JSONException e) {
        }
        return result;
    }

    public static String EnCodeJSONArray(List<String> list, String head) {
        try {
            JSONArray arrayStr = new JSONArray();
            JSONObject j = new JSONObject();
            for (String str : list) {
                arrayStr.put(str);
            }
            j.put(head, arrayStr);
            return j.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static Bitmap ScaleBitmap(Bitmap bitmap, float xRatio, float yRatio) {
        Matrix matrix = new Matrix();
        matrix.postScale(xRatio, yRatio);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap ScaleBitmap(Bitmap bitmap, float xRatio, float yRatio, int dx, int dy) {
        Matrix matrix = new Matrix();
        matrix.postScale(xRatio, yRatio);
        matrix.postTranslate((float) dx, (float) dy);
        Bitmap output = Bitmap.createBitmap((int) ((((float) bitmap.getWidth()) * xRatio) + ((float) dx)), (int) ((((float) bitmap.getHeight()) * yRatio) + ((float) dx)), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawBitmap(bitmap, matrix, paint);
        return output;
    }

    public static String GetGeometryLabelContent(String layerID, String datasourceName, String SYSID) {
        try {
            FeatureLayer tempLayer = PubVar._PubCommand.m_ProjectDB.GetLayerInDataSource(datasourceName, layerID);
            DataSet tempDataset = PubVar.m_Workspace.GetDatasetByName(datasourceName, layerID);
            if (!(tempLayer == null || tempDataset == null)) {
                List<String> tempFieldsArray = new ArrayList<>();
                List<String> tempDataFieldsArray = new ArrayList<>();
                for (LayerField tempLayerField : tempLayer.GetFieldList()) {
                    tempDataFieldsArray.add(tempLayerField.GetDataFieldName());
                    tempFieldsArray.add(tempLayerField.GetFieldName());
                }
                SQLiteReader localSQLiteDataReader = tempDataset.getDataSource().Query(String.format("select SYS_ID,%1$s from %2$s  where (SYS_ID) in (%3$s)", CombineStrings(",", tempDataFieldsArray), tempDataset.getDataTableName(), SYSID));
                if (localSQLiteDataReader != null && localSQLiteDataReader.Read()) {
                    ArrayList tempFieldsValue = new ArrayList();
                    Iterator<String> tempIter04 = tempDataFieldsArray.iterator();
                    Iterator<String> tempIter05 = tempFieldsArray.iterator();
                    while (tempIter04.hasNext() && tempIter05.hasNext()) {
                        String tempFieldName = tempIter05.next();
                        String tempFieldValue = localSQLiteDataReader.GetString(tempIter04.next());
                        if (tempFieldValue != null && !tempFieldValue.equals("")) {
                            tempFieldsValue.add(String.valueOf(tempFieldName) + "=" + tempFieldValue);
                        }
                    }
                    return CombineStrings(",", tempFieldsValue);
                }
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static String ConvertUniqueValueByFields(List<String> fieldsList, List<String> paramList) {
        StringBuilder tempSB = new StringBuilder();
        int tmpTid = 0;
        for (String tempFID : fieldsList) {
            Iterator<String> tempIter2 = paramList.iterator();
            while (true) {
                if (!tempIter2.hasNext()) {
                    break;
                }
                String tempValues = tempIter2.next();
                if (tempValues.contains(tempFID)) {
                    String[] tempStrs = tempValues.split("=");
                    if (tempStrs != null && tempStrs.length > 1) {
                        String tempValue = tempStrs[1];
                        if (tmpTid != 0) {
                            tempSB.append(",");
                        }
                        tempSB.append(tempValue.substring(1, tempValue.length() - 1));
                        tmpTid++;
                    }
                }
            }
        }
        return tempSB.toString();
    }

    public static boolean CellIndexContain(List<Integer> cellIndexList, int rowIndex, int colIndex) {
        int tid = 0;
        int count = cellIndexList.size() / 4;
        for (int i = 0; i < count; i++) {
            if (rowIndex >= cellIndexList.get(tid).intValue() && rowIndex <= cellIndexList.get(tid + 1).intValue() && colIndex >= cellIndexList.get(tid + 2).intValue() && colIndex <= cellIndexList.get(tid + 3).intValue()) {
                return true;
            }
            tid += 4;
        }
        return false;
    }

    public static boolean CellIndexContainPoint(List<Integer> cellIndexList, int rowIndex, int colIndex) {
        int tid = 0;
        int count = cellIndexList.size() / 2;
        for (int i = 0; i < count; i++) {
            if (rowIndex == cellIndexList.get(tid).intValue() && rowIndex == cellIndexList.get(tid + 1).intValue()) {
                return true;
            }
            tid += 2;
        }
        return false;
    }

    @SuppressLint("WrongConstant")
    public static void CopyText(String copyText) {
        ((ClipboardManager) PubVar.MainContext.getSystemService("clipboard")).setText(copyText);
    }

    public static int[] ListIntToArray(List<Integer> list) {
        int[] result = new int[list.size()];
        int tid = 0;
        for (Integer num : list) {
            result[tid] = num.intValue();
            tid++;
        }
        return result;
    }

    public static void GetCurrentNetTime() {
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.18
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    long tmpTime02 = new Date().getTime();
                    long ld = 0;
                    try {
                        URLConnection uc = new URL("http://www.baidu.com").openConnection();
                        uc.connect();
                        ld = uc.getDate();
                        Common.SaveTextFile(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/Android/LogFile.dat", String.valueOf(ld));
                    } catch (Exception e) {
                    }
                    if (ld < tmpTime02) {
                        ld = tmpTime02;
                    }
                    PubVar.CurrentNetTime = new Date(ld);
                    Common.AddTimeRecord(PubVar.CurrentNetTime);
                } catch (Exception ex) {
                    Common.Log("错误", ex.getMessage());
                }
            }
        }.start();
    }

    public static void AddTimeRecord(Date currentTime) {
        String tmpString;
        try {
            if (PubVar._PubCommand != null && PubVar._PubCommand.m_UserConfigDB != null) {
                Date tmpDate = currentTime;
                try {
                    HashMap<String, String> tmpHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_LastLoginDate");
                    if (!(tmpHashMap == null || (tmpString = tmpHashMap.get("F2")) == "")) {
                        Date tmpLastDate = new Date(Long.parseLong(tmpString));
                        if (tmpLastDate.getTime() > currentTime.getTime()) {
                            tmpDate = tmpLastDate;
                        }
                    }
                } catch (Exception e) {
                }
                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_LastLoginDate", Long.valueOf(tmpDate.getTime()));
            }
        } catch (Exception e2) {
        }
    }

    public static long GetLastRecordTime() {
        String tmpString;
        try {
            if (PubVar._PubCommand == null || PubVar._PubCommand.m_UserConfigDB == null) {
                return 0;
            }
            try {
                HashMap<String, String> tmpHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_LastLoginDate");
                if (tmpHashMap == null || (tmpString = tmpHashMap.get("F2")) == "") {
                    return 0;
                }
                return Long.parseLong(tmpString);
            } catch (Exception e) {
                return 0;
            }
        } catch (Exception e2) {
            return 0;
        }
    }

    public static void ConnectServer() {
        try {
            final Handler myHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.19
                @Override // android.os.Handler
                public void handleMessage(Message msg) {
                    if (msg.what == 1) {
                        Common.ShowDialog(PubVar.MainContext, msg.obj.toString(), "提示", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.19.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString, Object pObject) {
                                PubVar._PubCommand.ProcessCommand("开始注册系统");
                            }
                        });
                    } else if (msg.what == 2) {
                        String tmpMsg = msg.obj.toString().replaceAll(";", "\r\n").replaceAll("]:", ":\r\n");
                        InfoDialog tmpInfoDialog = InfoDialog.createDialog(PubVar.MainContext);
                        tmpInfoDialog.SetMessage(tmpMsg);
                        tmpInfoDialog.show();
                    }
                }
            };
            new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.20
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    String tempStr;
                    JSONObject jsonObject;
                    String tmpMsg;
                    boolean result = false;
                    try {
                        HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(String.valueOf(PubVar.ServerURL) + "ConnectHandler.ashx?method=connect&F1=" + PubVar.m_AuthorizeTools.getUserAndroidID() + "&F2=" + PubVar.m_AuthorizeTools.getRegisterMode() + "&F3=" + PubVar.Version + "," + PubVar.m_AuthorizeTools.GetRegisterInfo()));
                        if (httpResponse.getStatusLine().getStatusCode() == 200 && (result = (jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()))).getBoolean("success")) && ((tmpMsg = jsonObject.getString("msg")) == null || !tmpMsg.contains("不允许使用"))) {
                            if (!tmpMsg.contains("连接服务器成功")) {
                                Message msg = myHandler.obtainMessage();
                                msg.what = 2;
                                msg.obj = tmpMsg;
                                myHandler.sendMessage(msg);
                            }
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_Server_ConnectTime", String.valueOf(new Date().getTime()));
                        }
                    } catch (Exception e) {
                    }
                    if (!result) {
                        long tmpLong = 0;
                        try {
                            HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_Server_ConnectTime");
                            if (!(tempHashMap == null || (tempStr = tempHashMap.get("F2")) == null || tempStr.equals(""))) {
                                tmpLong = Long.parseLong(tempStr);
                            }
                            if (tmpLong == 0) {
                                try {
                                    tmpLong = PubVar.MainContext.getPackageManager().getPackageInfo(PubVar.MainContext.getPackageName(), 0).firstInstallTime;
                                } catch (PackageManager.NameNotFoundException e2) {
                                    tmpLong = 1446774487115L;
                                }
                            }
                            long time = new Date().getTime() - tmpLong;
                        } catch (Exception e3) {
                        }
                    }
                }
            }.start();
        } catch (Exception e) {
        }
    }

    public static void DisConnectServer() {
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.21
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    if (new DefaultHttpClient().execute(new HttpGet(String.valueOf(PubVar.ServerURL) + "ConnectHandler.ashx?method=disconnect&F1=" + PubVar.m_AuthorizeTools.getUserAndroidID() + "&F2=" + PubVar.m_AuthorizeTools.getRegisterMode() + "&F3=" + PubVar.Version + "," + PubVar.m_AuthorizeTools.GetRegisterInfo())).getStatusLine().getStatusCode() != 200) {
                    }
                } catch (Exception e) {
                }
            }
        }.start();
        ShareDeviceToServer(PubVar.m_AuthorizeTools.getUserAndroidID(), "", false, null, null);
    }

    public static void ShareDeviceToServer(final String deviceID, final String deviceInfo, final boolean isShare, final Handler netHandler, final Handler progrssbarHandler) {
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.22
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                String httpUrl;
                try {
                    if (isShare) {
                        httpUrl = String.valueOf(PubVar.ServerURL) + "ShareDeviceHandler.ashx?method=connect&F1=" + deviceID + "&F2=" + URLEncoder.encode(deviceInfo, "utf-8");
                    } else {
                        httpUrl = String.valueOf(PubVar.ServerURL) + "ShareDeviceHandler.ashx?method=disconnect&F1=" + deviceID;
                    }
                    HttpGet httpRequest = new HttpGet(httpUrl);
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
                    if (new DefaultHttpClient(httpParameters).execute(httpRequest).getStatusLine().getStatusCode() == 200) {
                        if (isShare) {
                            Message msg = netHandler.obtainMessage();
                            msg.what = 1;
                            netHandler.sendMessage(msg);
                        } else {
                            Message msg2 = netHandler.obtainMessage();
                            msg2.what = 2;
                            netHandler.sendMessage(msg2);
                        }
                    }
                } catch (Exception e) {
                }
                try {
                    if (progrssbarHandler != null) {
                        Message msg3 = progrssbarHandler.obtainMessage();
                        msg3.what = 0;
                        progrssbarHandler.sendMessage(msg3);
                    }
                } catch (Exception e2) {
                }
            }
        }.start();
    }

    public static void GetParamFromServer(final String paramName, final Handler DataFromServerHandler) {
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.23
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    HttpGet httpRequest = new HttpGet(String.valueOf(PubVar.ServerURL) + "GetParamHandler.ashx?param=" + paramName);
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
                    HttpResponse httpResponse = new DefaultHttpClient(httpParameters).execute(httpRequest);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                        if (jsonObject.getBoolean("success")) {
                            String paramValue = jsonObject.getString("msg");
                            Message msg = DataFromServerHandler.obtainMessage();
                            msg.what = 1;
                            msg.obj = new Object[]{paramName, paramValue};
                            DataFromServerHandler.sendMessage(msg);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }.start();
    }

    @SuppressLint("WrongConstant")
    public static void CopyText(Context context, String copyText) {
        ((ClipboardManager) context.getSystemService("clipboard")).setText(copyText);
    }

    @SuppressLint("WrongConstant")
    public static String PasteText(Context context) {
        return (String) ((ClipboardManager) context.getSystemService("clipboard")).getText();
    }

    public static String getCpuName() {
        try {
            String[] array = new BufferedReader(new FileReader("/proc/cpuinfo")).readLine().split(":\\s+", 2);
            for (int i = 0; i < array.length; i++) {
            }
            return array[1];
        } catch (Exception e) {
            return null;
        }
    }

    public static String SimplifyFileSize(double fileSize) {
        if (fileSize <= 1024.0d) {
            return String.valueOf("") + doubleFormat.format(fileSize) + "kb";
        }
        double fileSize2 = fileSize / 1024.0d;
        if (fileSize2 <= 1024.0d) {
            return String.valueOf("") + doubleFormat.format(fileSize2) + "M";
        }
        double fileSize3 = fileSize2 / 1024.0d;
        if (fileSize3 > 1024.0d) {
            return String.valueOf("") + doubleFormat.format(fileSize3)/* + NDEFRecord.TEXT_WELL_KNOWN_TYPE*/;
        }
        return String.valueOf("") + doubleFormat.format(fileSize3) + "G";
    }

    public static byte[] IntToByteArray(int i) {
        byte[] result = new byte[4];
        result[3] = (byte) ((i >> 24) & 255);
        result[2] = (byte) ((i >> 16) & 255);
        result[1] = (byte) ((i >> 8) & 255);
        result[0] = (byte) (i & 255);
        return result;
    }

    public static byte[] IntToByteArray2(int i) {
        return new byte[]{(byte) ((i >> 24) & 255), (byte) ((i >> 16) & 255), (byte) ((i >> 8) & 255), (byte) (i & 255)};
    }

    public static int BytesToInt(byte[] b) {
        int n = 0;
        for (int i = 3; i >= 0; i--) {
            n = (n << 8) | (b[i] & 255);
        }
        return n;
    }

    public static int BytesToInt(char[] b) {
        int n = 0;
        for (int i = 3; i >= 0; i--) {
            n = (n << 8) | (b[i] & 255);
        }
        return n;
    }

    public static int BytesToInt2(byte[] b) {
        int n = 0;
        for (int i = 0; i < 4; i++) {
            n = (n << 8) | (b[i] & 255);
        }
        return n;
    }

    @SuppressLint("WrongConstant")
    public static Bitmap GetBitmapFromView(View view) {
        try {
            int[] tmpInts = GetViewSize(view);
            int w = tmpInts[2];
            int h = tmpInts[3];
            if (w <= 0 || h <= 0) {
                return null;
            }
            view.setDrawingCacheEnabled(true);
            view.setDrawingCacheQuality(AccessibilityEventCompat.TYPE_TOUCH_INTERACTION_START);
            Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bmp);
            view.layout(0, 0, w, h);
            view.draw(c);
            view.destroyDrawingCache();
            return bmp;
        } catch (Exception e) {
            return null;
        }
    }

    public static void DeleteFileMust(String filepath) {
        File[] childFile;
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                return;
            }
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory() && (childFile = file.listFiles()) != null && childFile.length != 0) {
                for (File f : childFile) {
                    try {
                        DeleteFileMust(f.getAbsolutePath());
                    } catch (Exception ex) {
                        Log("删除文件错误", ex.getMessage());
                    }
                }
            }
        } catch (Exception ex2) {
            Log("删除文件错误", ex2.getMessage());
        }
    }

    public static double ConvertFormatDegree(String value, String format) {
        double tmpD01;
        try {
            if (format.contains("DDD.DDDDDD")) {
                return Double.parseDouble(value);
            }
            if (!format.contains("DDD.MMSSSS")) {
                return Double.parseDouble(value);
            }
            if (value.length() <= 0) {
                return 0.0d;
            }
            int tmpI = value.indexOf(FileSelector_Dialog.sFolder);
            if (tmpI <= 0) {
                return 0.0d + ((double) Integer.parseInt(value));
            }
            double result = 0.0d + ((double) Integer.parseInt(value.substring(0, tmpI)));
            if (tmpI + 3 > value.length()) {
                return result;
            }
            double result2 = result + ((double) (((float) Integer.parseInt(value.substring(tmpI + 1, tmpI + 3))) / 60.0f));
            if (tmpI + 3 >= value.length()) {
                return result2;
            }
            String tmpStr03 = value.substring(tmpI + 3);
            int tmpI02 = Integer.parseInt(tmpStr03);
            if (tmpI02 > 99) {
                tmpD01 = ((double) tmpI02) / Math.pow(10.0d, (double) (tmpStr03.length() - 2));
            } else {
                tmpD01 = (double) tmpI02;
            }
            return result2 + (tmpD01 / 3600.0d);
        } catch (Exception ex) {
            Log("转换角度错误", ex.getMessage());
            return 0.0d;
        }
    }

    public static Bitmap ReplaceBitmapColor(Bitmap oldBitmap, int oldColor, int newColor) {
        Bitmap mBitmap = oldBitmap.copy(Bitmap.Config.ARGB_8888, true);
        int mBitmapWidth = mBitmap.getWidth();
        int mBitmapHeight = mBitmap.getHeight();
        for (int i = 0; i < mBitmapHeight; i++) {
            for (int j = 0; j < mBitmapWidth; j++) {
                if (mBitmap.getPixel(j, i) == oldColor) {
                    mBitmap.setPixel(j, i, newColor);
                }
            }
        }
        return mBitmap;
    }

    public static boolean IsInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    public static void GoToBaiduMap(Context activity, String x, String y, String title) {
        try {
            String title2 = URLEncoder.encode(title);
            Intent intent = Intent.getIntent("intent://map/marker?location=" + y + "," + x + "&title=" + title2 + "&content=[" + title2 + "]&coord_type=wgs84&output=html#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            if (IsInstallByread("com.baidu.BaiduMap")) {
                activity.startActivity(intent);
            } else {
                ShowDialog(activity, "当前手机中没有安装百度地图,不能进行地图定位.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void GoToGaoDeMap(Context activity, String x, String y, String title) {
        try {
            String title2 = URLEncoder.encode(title);
            if (IsInstallByread("com.autonavi.minimap")) {
                activity.startActivity(Intent.getIntent("androidamap://viewMap?sourceApplication=" + title2 + "&poiname=[" + title2 + "]&lat=" + y + "&lon=" + x + "&dev=1"));
            } else {
                ShowDialog(activity, "当前手机中没有安装百度地图,不能进行地图定位.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void NavigatorBaiduMap(Context activity, String longitude, String latitude) {
        try {
            if (IsInstallByread("com.baidu.BaiduMap")) {
                Intent i1 = new Intent();
                i1.setData(Uri.parse("baidumap://map/navi?coord_type=wgs84&location=" + latitude + "," + longitude));
                activity.startActivity(i1);
                return;
            }
            ShowDialog(activity, "当前手机中没有安装百度地图,不能进行地图定位.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void NavigatorGaodeMap(Context activity, String longitude, String latitude) {
        try {
            if (IsInstallByread("com.autonavi.minimap")) {
                Intent i1 = new Intent();
                i1.setData(Uri.parse("androidamap://navi?sourceApplication=appname&poiname=fangheng&lat=" + latitude + "&lon=" + longitude + "&dev=1&style=2"));
                activity.startActivity(i1);
                return;
            }
            ShowDialog(activity, "当前手机中没有安装百度地图,不能进行地图定位.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void ConnectToServer(final Handler processHandler) {
        StringBuilder tmpSB = new StringBuilder();
        tmpSB.append("F1=");
        tmpSB.append(PubVar.m_AuthorizeTools.getUserAndroidID());
        tmpSB.append("&F2=");
        tmpSB.append(PubVar.m_AuthorizeTools.getDeviceIMEI());
        tmpSB.append("&F3=");
        tmpSB.append(String.valueOf(PubVar.Version) + "," + PubVar.m_AuthorizeTools.GetRegisterInfo());
        tmpSB.append("&F4=");
        tmpSB.append(PubVar.m_AuthorizeTools.getRegisterMode());
        final String tmpString01 = tmpSB.toString();
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Common.Common.24
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    HttpGet httpRequest = new HttpGet(String.valueOf(PubVar.ServerURL) + "ConnectHandler.ashx?method=connectserver&" + tmpString01);
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
                    HttpResponse httpResponse = new DefaultHttpClient(httpParameters).execute(httpRequest);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                        if (jsonObject.getBoolean("success") && processHandler != null) {
                            String paramValue = jsonObject.getString("msg");
                            Message msg = processHandler.obtainMessage();
                            msg.what = 999;
                            msg.obj = paramValue;
                            processHandler.sendMessage(msg);
                        }
                    }
                } catch (Exception e) {
                }
            }
        }.start();
    }

    public static void DeleteDir(String pPath) {
        DeleteDirWithFile(new File(pPath));
    }

    public static void DeleteDirWithFile(File dir) {
        if (dir != null) {
            try {
                if (dir.exists() && dir.isDirectory()) {
                    File[] listFiles = dir.listFiles();
                    for (File file : listFiles) {
                        try {
                            if (file.isFile()) {
                                file.delete();
                            } else if (file.isDirectory()) {
                                DeleteDirWithFile(file);
                            }
                        } catch (Exception e) {
                        }
                    }
                    dir.delete();
                }
            } catch (Exception e2) {
            }
        }
    }

    /* renamed from:  com.xzy.forestSystem.gogisapi.Common.Common$UUIDfileFilter */
    static class UUIDfileFilter implements FilenameFilter {
        UUIDfileFilter() {
        }

        @Override // java.io.FilenameFilter
        public boolean accept(File paramFile, String paramString) {
            return paramString.indexOf(Common.FileNameT) >= 0;
        }
    }

    public static Coordinate ReadTxtFileCoord(String filepath) {
        Coordinate result;
        try {
            FileInputStream inputStream = new FileInputStream(filepath);
            InputStreamReader inputStreamReader = null;
            try {
                inputStreamReader = new InputStreamReader(inputStream, "gbk");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(inputStreamReader);
            double tmpX = 0.0d;
            double tmpY = 0.0d;
            double tmpZ = 0.0d;
            while (true) {
                try {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    int tmpI = line.indexOf("经度：");
                    if (tmpI < 0) {
                        int tmpI2 = line.indexOf("纬度：");
                        if (tmpI2 < 0) {
                            int tmpI3 = line.indexOf("高程：");
                            if (tmpI3 < 0) {
                                if (!(tmpX == 0.0d || tmpY == 0.0d || tmpZ == 0.0d)) {
                                    break;
                                }
                            } else {
                                try {
                                    tmpZ = Double.parseDouble(line.substring("高程：".length() + tmpI3).trim());
                                } catch (Exception e) {
                                }
                            }
                        } else {
                            try {
                                tmpY = Double.parseDouble(line.substring("纬度：".length() + tmpI2).trim());
                            } catch (Exception e2) {
                            }
                        }
                    } else {
                        try {
                            tmpX = Double.parseDouble(line.substring("经度：".length() + tmpI).trim());
                        } catch (Exception e3) {
                        }
                    }
                } catch (IOException e4) {
                    result = null;
                }
            }
            if (tmpX == 0.0d || tmpY == 0.0d || tmpZ == 0.0d) {
                result = null;
            } else {
                result = new Coordinate(tmpX, tmpY, tmpZ);
            }
            try {
                inputStream.close();
                return result;
            } catch (Exception e5) {
                return result;
            }
        } catch (Exception e6) {
            return null;
        }
    }

    public static boolean IsChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    public static boolean IsMessyCode(String strName) {
        char[] ch = Pattern.compile("\\s*|\t*|\r*|\n*").matcher(strName).replaceAll("").replaceAll("\\p{P}", "").trim().toCharArray();
        float chLength = 0.0f;
        float count = 0.0f;
        for (char c : ch) {
            if (!Character.isLetterOrDigit(c)) {
                if (!IsChinese(c)) {
                    count += 1.0f;
                }
                chLength += 1.0f;
            }
        }
        if (((double) (count / chLength)) > 0.4d) {
            return true;
        }
        return false;
    }
}
