package  com.xzy.forestSystem.gogisapi.Display;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Shader;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.CommonProcess;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Config.SysConfig;
import java.util.ArrayList;
import java.util.List;

public class SymbolManage {
    private SysConfig m_ConfigDB = null;

    public ISymbol GetSystemSymbol(String symbolName, EGeoLayerType geoLayerType) {
        if (geoLayerType == EGeoLayerType.POINT) {
            return GetSystemPointSymbol(symbolName);
        }
        if (geoLayerType == EGeoLayerType.POLYLINE) {
            return GetSystemLineSymbol(symbolName);
        }
        if (geoLayerType == EGeoLayerType.POLYGON) {
            return GetSystemPolySymbol(symbolName);
        }
        return null;
    }

    public LineSymbol GetSystemLineSymbol(String symbolName) {
        String[] tempStrs01;
        String[] tempStrs02;
        LineSymbol localLineSymbol = new LineSymbol();
        if (symbolName.equals("")) {
            symbolName = "默认";
        }
        String str1 = "";
        SQLiteReader localSQLiteDataReader = this.m_ConfigDB.GetSQLiteDatabase().Query("Select Symbol from T_LineSymbol where name = '" + symbolName + "'");
        if (localSQLiteDataReader != null) {
            if (localSQLiteDataReader.Read()) {
                str1 = localSQLiteDataReader.GetString("Symbol");
            }
            localSQLiteDataReader.Close();
            localLineSymbol.setConfigParas(str1);
            String[] arrayOfString = str1.split("@");
            ArrayList localArrayList = new ArrayList();
            for (String str3 : arrayOfString) {
                if (str3 != null && !str3.equals("") && (tempStrs01 = str3.split(",")) != null && tempStrs01.length > 1) {
                    Paint localPaint = new Paint();
                    localPaint.setColor(Color.parseColor(tempStrs01[0]));
                    localPaint.setAntiAlias(true);
                    localPaint.setStyle(Paint.Style.STROKE);
                    localPaint.setStrokeWidth(Float.valueOf(tempStrs01[1]).floatValue());
                    if (tempStrs01.length > 2 && (tempStrs02 = tempStrs01[2].split("\\*")) != null && tempStrs02.length > 1) {
                        localPaint.setPathEffect(new DashPathEffect(new float[]{Float.parseFloat(tempStrs02[0]), Float.parseFloat(tempStrs02[1])}, 1.0f));
                    }
                    localArrayList.add(localPaint);
                }
            }
            localLineSymbol.setStyle(localArrayList);
        }
        return localLineSymbol;
    }

    public static LineSymbol GetLineSymbol(String configParam) {
        String[] tempStrs01;
        String[] tempStrs02;
        LineSymbol tempSymbol = new LineSymbol();
        tempSymbol.setConfigParas(configParam);
        String[] arrayOfString = configParam.split("@");
        if (arrayOfString != null && arrayOfString.length > 0) {
            ArrayList localArrayList = new ArrayList();
            for (String str3 : arrayOfString) {
                if (str3 != null && !str3.equals("") && (tempStrs01 = str3.split(",")) != null && tempStrs01.length > 1) {
                    Paint localPaint = new Paint();
                    localPaint.setColor(Color.parseColor(tempStrs01[0]));
                    localPaint.setAntiAlias(true);
                    localPaint.setStyle(Paint.Style.STROKE);
                    localPaint.setStrokeWidth(Float.valueOf(tempStrs01[1]).floatValue());
                    if (tempStrs01.length > 2 && (tempStrs02 = tempStrs01[2].split("\\*")) != null && tempStrs02.length > 1) {
                        localPaint.setPathEffect(new DashPathEffect(new float[]{Float.parseFloat(tempStrs02[0]), Float.parseFloat(tempStrs02[1])}, 1.0f));
                    }
                    localArrayList.add(localPaint);
                }
            }
            tempSymbol.setStyle(localArrayList);
        }
        return tempSymbol;
    }

    public static PointSymbol GetPointSymbol(String configParam) {
        PointSymbol tmpSymbol = new PointSymbol();
        tmpSymbol.setIcon(CommonProcess.Base64ToBitmap(configParam));
        tmpSymbol.setConfigParas(configParam);
        return tmpSymbol;
    }

    public PointSymbol GetSystemPointSymbol(String symbolName) {
        PointSymbol tmpSymbol = new PointSymbol();
        if (symbolName.equals("")) {
            symbolName = "默认";
        }
        byte[] arrayOfByte = null;
        SQLiteReader localSQLiteDataReader = this.m_ConfigDB.GetSQLiteDatabase().Query("Select Symbol from T_PointSymbol where name = '" + symbolName + "'");
        if (localSQLiteDataReader != null) {
            if (localSQLiteDataReader.Read()) {
                arrayOfByte = localSQLiteDataReader.GetBlob("Symbol");
            }
            localSQLiteDataReader.Close();
        }
        if (arrayOfByte == null) {
            return GetSystemPointSymbol("默认");
        }
        Bitmap tempBmp = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
        tmpSymbol.setIcon(tempBmp);
        tmpSymbol.setConfigParas(CommonProcess.BitmapToBase64(tempBmp));
        return tmpSymbol;
    }

    public PolygonSymbol GetSystemPolySymbol(String symbolName) {
        PolygonSymbol tempSymbol = new PolygonSymbol();
        if (symbolName.equals("")) {
            symbolName = "默认";
        }
        SQLiteReader localSQLiteDataReader = this.m_ConfigDB.GetSQLiteDatabase().Query("Select * from T_PolySymbol where name = '" + symbolName + "'");
        if (localSQLiteDataReader != null) {
            if (localSQLiteDataReader.Read()) {
                String str2 = localSQLiteDataReader.GetString("PColor");
                String str3 = localSQLiteDataReader.GetString("LColor");
                String str4 = localSQLiteDataReader.GetString("LWidth");
                float f = Float.parseFloat(str4);
                Paint localPaint1 = new Paint();
                Paint localPaint2 = new Paint();
                localPaint1.setStyle(Paint.Style.FILL);
                localPaint1.setAntiAlias(true);
                try {
                    if (str2.startsWith("#")) {
                        localPaint1.setColor(Color.parseColor(str2));
                    } else {
                        localPaint1.setShader(new BitmapShader(CommonProcess.Base64ToBitmap(str2), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
                    }
                } catch (Exception e) {
                    localPaint1.setColor(-3355444);
                }
                localPaint2.setColor(Color.parseColor(str3));
                localPaint2.setStyle(Paint.Style.STROKE);
                localPaint2.setAntiAlias(true);
                localPaint2.setStrokeWidth(f);
                tempSymbol.setPStyle(localPaint1);
                tempSymbol.setLStyle(localPaint2);
                tempSymbol.setConfigParas(String.valueOf(str2) + "," + str3 + "," + str4);
            }
            localSQLiteDataReader.Close();
        }
        return tempSymbol;
    }

    public static PolygonSymbol GetPolySymbol(String configParam) {
        String[] tempStrs;
        PolygonSymbol tempSymbol = new PolygonSymbol();
        tempSymbol.setConfigParas(configParam);
        if (!configParam.equals("") && (tempStrs = configParam.split(",")) != null && tempStrs.length > 2) {
            String str2 = tempStrs[0];
            String str3 = tempStrs[1];
            float f = Float.parseFloat(tempStrs[2]);
            Paint localPaint1 = new Paint();
            Paint localPaint2 = new Paint();
            localPaint1.setStyle(Paint.Style.FILL);
            localPaint1.setAntiAlias(true);
            try {
                if (str2.startsWith("#")) {
                    localPaint1.setColor(Color.parseColor(str2));
                } else {
                    localPaint1.setShader(new BitmapShader(CommonProcess.Base64ToBitmap(str2), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
                }
            } catch (Exception e) {
                localPaint1.setColor(-1);
            }
            localPaint2.setColor(Color.parseColor(str3));
            localPaint2.setStyle(Paint.Style.STROKE);
            localPaint2.setAntiAlias(true);
            localPaint2.setStrokeWidth(f);
            tempSymbol.setPStyle(localPaint1);
            tempSymbol.setLStyle(localPaint2);
        }
        return tempSymbol;
    }

    public Bitmap GetSymbolFigure(String paramString, EGeoLayerType paramlkGeoLayerType) {
        if (paramString.equals("")) {
            paramString = "默认";
        }
        List<SymbolObject> localList = GetSystemSymbolFigure(new String[]{paramString}, paramlkGeoLayerType);
        if (localList == null || localList.size() <= 0) {
            return null;
        }
        return localList.get(0).SymbolFigure;
    }

    public List<SymbolObject> GetSystemSymbolFigure(String[] paramArrayOfString, EGeoLayerType paramlkGeoLayerType) {
        SQLiteReader localSQLiteDataReader3;
        String[] tempStrs02;
        String str1 = "1=1";
        try {
            if (paramArrayOfString.length > 0) {
                str1 = "name in ('" + Common.Joins("','", paramArrayOfString) + "')";
            }
            if (paramlkGeoLayerType == EGeoLayerType.POINT) {
                SQLiteReader localSQLiteDataReader1 = this.m_ConfigDB.GetSQLiteDatabase().Query("Select Name,SymbolFigure from T_PointSymbol where " + str1);
                if (localSQLiteDataReader1 == null) {
                    return null;
                }
                List<SymbolObject> result = new ArrayList<>();
                while (localSQLiteDataReader1.Read()) {
                    try {
                        SymbolObject localSymbolObject1 = new SymbolObject();
                        byte[] arrayOfByte = localSQLiteDataReader1.GetBlob("SymbolFigure");
                        localSymbolObject1.SymbolName = localSQLiteDataReader1.GetString("Name");
                        if (arrayOfByte != null) {
                            Bitmap localBitmap1 = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
                            Bitmap localBitmap2 = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
                            Canvas localCanvas1 = new Canvas(localBitmap2);
                            if (localBitmap1.getWidth() <= localBitmap2.getWidth() && localBitmap1.getHeight() <= localBitmap2.getHeight()) {
                                localCanvas1.drawBitmap(localBitmap1, (float) ((localBitmap2.getWidth() - localBitmap1.getWidth()) / 2), (float) ((localBitmap2.getHeight() - localBitmap1.getHeight()) / 2), (Paint) null);
                            }
                            localSymbolObject1.SymbolFigure = localBitmap2;
                        }
                        result.add(localSymbolObject1);
                    } catch (Exception e) {
                        return result;
                    }
                }
                localSQLiteDataReader1.Close();
                return result;
            } else if (paramlkGeoLayerType == EGeoLayerType.POLYLINE) {
                SQLiteReader localSQLiteDataReader2 = this.m_ConfigDB.GetSQLiteDatabase().Query("Select Name,Symbol from T_LineSymbol where " + str1);
                if (localSQLiteDataReader2 == null) {
                    return null;
                }
                List<SymbolObject> result2 = new ArrayList<>();
                while (localSQLiteDataReader2.Read()) {
                    String str4 = localSQLiteDataReader2.GetString("Symbol");
                    if (!str4.equals("")) {
                        SymbolObject localSymbolObject2 = new SymbolObject();
                        localSymbolObject2.SymbolName = localSQLiteDataReader2.GetString("Name");
                        Bitmap localBitmap3 = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
                        Canvas localCanvas2 = new Canvas(localBitmap3);
                        String[] arrayOfString = str4.split("@");
                        int tempCount = arrayOfString.length;
                        for (int i = 0; i < tempCount; i++) {
                            String[] tempStrs01 = arrayOfString[i].split(",");
                            if (tempStrs01 != null && tempStrs01.length > 1) {
                                Paint localPaint = new Paint();
                                localPaint.setColor(Color.parseColor(tempStrs01[0]));
                                localPaint.setAntiAlias(true);
                                localPaint.setStyle(Paint.Style.STROKE);
                                localPaint.setStrokeWidth(Float.valueOf(tempStrs01[1]).floatValue());
                                if (tempStrs01.length > 2 && (tempStrs02 = tempStrs01[2].split("\\*")) != null && tempStrs02.length > 1) {
                                    localPaint.setPathEffect(new DashPathEffect(new float[]{Float.parseFloat(tempStrs02[0]), Float.parseFloat(tempStrs02[1])}, 1.0f));
                                }
                                localCanvas2.drawLine(0.0f, 32.0f, 64.0f, 32.0f, localPaint);
                            }
                        }
                        localSymbolObject2.SymbolFigure = localBitmap3;
                        result2.add(localSymbolObject2);
                    }
                }
                localSQLiteDataReader2.Close();
                return result2;
            } else if (paramlkGeoLayerType != EGeoLayerType.POLYGON || (localSQLiteDataReader3 = this.m_ConfigDB.GetSQLiteDatabase().Query("Select Name,PColor,LColor,LWidth from T_PolySymbol where " + str1)) == null) {
                return null;
            } else {
                List<SymbolObject> result3 = new ArrayList<>();
                while (localSQLiteDataReader3.Read()) {
                    SymbolObject localSymbolObject3 = new SymbolObject();
                    String str7 = localSQLiteDataReader3.GetString("PColor");
                    String str8 = localSQLiteDataReader3.GetString("LColor");
                    String str9 = localSQLiteDataReader3.GetString("LWidth");
                    localSymbolObject3.SymbolName = localSQLiteDataReader3.GetString("Name");
                    Bitmap localBitmap4 = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
                    Canvas localCanvas3 = new Canvas(localBitmap4);
                    Paint localPaint2 = new Paint();
                    localPaint2.setStyle(Paint.Style.FILL);
                    localPaint2.setAntiAlias(true);
                    try {
                        if (str7.startsWith("#")) {
                            localPaint2.setColor(Color.parseColor(str7));
                        } else {
                            localPaint2.setShader(new BitmapShader(CommonProcess.Base64ToBitmap(str7), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
                        }
                    } catch (Exception e2) {
                        localPaint2.setColor(-1);
                    }
                    float tmpWidth = Float.valueOf(str9).floatValue();
                    localCanvas3.drawRect(0.0f, 0.0f, 64.0f, 64.0f, localPaint2);
                    Paint localPaint3 = new Paint();
                    localPaint3.setColor(Color.parseColor(str8));
                    localPaint3.setStyle(Paint.Style.STROKE);
                    localPaint3.setStrokeWidth(tmpWidth);
                    localCanvas3.drawRect(0.0f, 0.0f, 64.0f - tmpWidth, 64.0f - tmpWidth, localPaint3);
                    localSymbolObject3.SymbolFigure = localBitmap4;
                    result3.add(localSymbolObject3);
                }
                localSQLiteDataReader3.Close();
                return result3;
            }
        } catch (Exception e3) {
            return null;
        }
    }

    public void SetBindProject(SysConfig paramv1_ConfigDB) {
        this.m_ConfigDB = paramv1_ConfigDB;
    }

    private void DeleteSymbol(String symbolName, EGeoLayerType geoLayerType) {
        if (geoLayerType == EGeoLayerType.POINT) {
            this.m_ConfigDB.GetSQLiteDatabase().ExecuteSQL("Delete from T_PointSymbol where Name='" + symbolName + "'");
        } else if (geoLayerType == EGeoLayerType.POLYLINE) {
            this.m_ConfigDB.GetSQLiteDatabase().ExecuteSQL("Delete from T_LineSymbol where Name='" + symbolName + "'");
        } else if (geoLayerType == EGeoLayerType.POLYGON) {
            this.m_ConfigDB.GetSQLiteDatabase().ExecuteSQL("Delete from T_PolySymbol where Name='" + symbolName + "'");
        }
    }

    public boolean IsSymbolExist(String symbolName, EGeoLayerType geoLayerType) {
        String tmpSql = "";
        if (geoLayerType == EGeoLayerType.POINT) {
            tmpSql = "Select * from T_PointSymbol where Name='" + symbolName + "'";
        } else if (geoLayerType == EGeoLayerType.POLYLINE) {
            tmpSql = "Select * from T_LineSymbol where Name='" + symbolName + "'";
        } else if (geoLayerType == EGeoLayerType.POLYGON) {
            tmpSql = "Select * from T_PolySymbol where Name='" + symbolName + "'";
        }
        SQLiteReader tempReader = this.m_ConfigDB.GetSQLiteDatabase().Query(tmpSql);
        if (tempReader == null || !tempReader.Read()) {
            return false;
        }
        return true;
    }

    public boolean SaveSymbolInSystem(String symbolName, ISymbol symbol, String[] outMsg) {
        outMsg[0] = "";
        int tempType = 0;
        if (symbol instanceof PointSymbol) {
            tempType = 1;
        } else if (symbol instanceof PolygonSymbol) {
            tempType = 2;
        } else if (symbol instanceof LineSymbol) {
            tempType = 3;
        }
        return SaveSymbolInSystem(symbolName, tempType, symbol.getConfigParas(), outMsg);
    }

    public boolean SaveSymbolInSystem(String symbolName, int symbolType, String configParas, String[] outMsg) {
        byte[] arrayOfByte;
        boolean result = false;
        outMsg[0] = "";
        if (configParas.equals("")) {
            outMsg[0] = "符号数据错误.";
            return false;
        }
        if (symbolType == 1) {
            if (!IsSymbolExist(symbolName, EGeoLayerType.POINT)) {
                String tempSql = "Insert Into T_PointSymbol (Name,SymbolFigure,Symbol) Values ('" + symbolName + "',?,?)";
                Bitmap tmpBmp = CommonProcess.Base64ToBitmap(configParas);
                if (!(tmpBmp == null || (arrayOfByte = Common.ConvertBmp2Stream(tmpBmp)) == null || arrayOfByte.length <= 0)) {
                    result = this.m_ConfigDB.GetSQLiteDatabase().ExecuteSQL(tempSql, new Object[]{arrayOfByte, arrayOfByte});
                }
            } else {
                outMsg[0] = "符号[" + symbolName + "]已经存在.";
            }
        } else if (symbolType == 2) {
            if (!IsSymbolExist(symbolName, EGeoLayerType.POLYGON)) {
                String[] tempStrs = configParas.split(",");
                if (tempStrs != null && tempStrs.length > 2) {
                    result = this.m_ConfigDB.GetSQLiteDatabase().ExecuteSQL("Insert Into T_PolySymbol (Name,PColor,LColor,LWidth) Values ('" + symbolName + "','" + tempStrs[0] + "','" + tempStrs[1] + "','" + tempStrs[2] + "')");
                }
            } else {
                outMsg[0] = "符号[" + symbolName + "]已经存在.";
            }
        } else if (symbolType == 3 && !IsSymbolExist(symbolName, EGeoLayerType.POLYLINE)) {
            result = this.m_ConfigDB.GetSQLiteDatabase().ExecuteSQL("Insert Into T_LineSymbol (Name,Symbol) Values ('" + symbolName + "','" + configParas + "')");
        }
        return result;
    }

    public boolean DeleteSymbol(String symbolName, EGeoLayerType geoLayerType, String[] outMsg) {
        outMsg[0] = "";
        if (symbolName.equals("默认")) {
            outMsg[0] = "默认符号不能删除.";
            return false;
        }
        for (GeoLayer tmpLayer : PubVar._Map.getGeoLayers().getList()) {
            if (tmpLayer != null && tmpLayer.getType() == geoLayerType && tmpLayer.getRender().HasUsedSymbol(symbolName)) {
                outMsg[0] = "符号【" + symbolName + "】正在被使用.";
                return false;
            }
        }
        DeleteSymbol(symbolName, geoLayerType);
        return true;
    }

    public static ISymbol CreateSymbolByData(String symbolParas, EGeoLayerType geoLayerType) {
        if (geoLayerType == EGeoLayerType.POINT) {
            return GetPointSymbol(symbolParas);
        }
        if (geoLayerType == EGeoLayerType.POLYLINE) {
            return GetLineSymbol(symbolParas);
        }
        if (geoLayerType == EGeoLayerType.POLYGON) {
            return GetPolySymbol(symbolParas);
        }
        return null;
    }

    public static PointSymbol GetSystemPointSymbol(String symbolName, SysConfig m_ConfigDB2) {
        PointSymbol tmpSymbol = new PointSymbol();
        if (symbolName.equals("")) {
            symbolName = "默认";
        }
        byte[] arrayOfByte = null;
        SQLiteReader localSQLiteDataReader = m_ConfigDB2.GetSQLiteDatabase().Query("Select Symbol from T_PointSymbol where name = '" + symbolName + "'");
        if (localSQLiteDataReader != null) {
            if (localSQLiteDataReader.Read()) {
                arrayOfByte = localSQLiteDataReader.GetBlob("Symbol");
            }
            localSQLiteDataReader.Close();
        }
        if (arrayOfByte == null) {
            return GetSystemPointSymbol("默认", m_ConfigDB2);
        }
        Bitmap tempBmp = BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
        tmpSymbol.setIcon(tempBmp);
        tmpSymbol.setConfigParas(CommonProcess.BitmapToBase64(tempBmp));
        return tmpSymbol;
    }
}
