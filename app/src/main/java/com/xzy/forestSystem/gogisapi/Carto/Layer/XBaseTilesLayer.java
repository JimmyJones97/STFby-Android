package  com.xzy.forestSystem.gogisapi.Carto.Layer;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.core.internal.view.SupportMenu;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Map;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.Downloader;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.WebMapTilesDownloader;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WebMercator;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class XBaseTilesLayer extends XLayer {
    public static int InHighLevel = 0;
    public static int MaskBiasXIndex = 1;
    public static int MaskBiasYIndex = 1;
    double OriginalLatitude = 90.0d;
    double OriginalLongitude = -180.0d;
    protected String _CacheFolder = null;
    Paint _CanvasPaint = null;
    private boolean _ConsiderTranslate = false;
    AbstractC0383CoordinateSystem _CoordinateSystem = null;
    private int _CurrentLevel = 1;
    private List<String> _CurrentShowList = null;
    List<Double> _LevelScale = new ArrayList();
    boolean _NeedClearCanvas = false;
    private boolean _NeedDownload = true;
    double _OffsetLatitude = 0.0d;
    double _OffsetLongitude = 0.0d;
    double _OffsetX = 0.0d;
    double _OffsetY = 0.0d;
    protected Canvas _OtherDrawCanvas = null;
    public boolean _PanRefresh = false;
    protected String _TableName = null;
    protected ERasterLayerType _TileLayerType = ERasterLayerType.NONE;
    protected Canvas _TileScaleCanvas = null;
    private WebMapTilesDownloader _TilesDownloader = null;
    protected int _TotalLevel = 20;
    List<BaseTileInfo> cacheTilesArray = new ArrayList();
    private Downloader m_Downloader = null;
    Map m_Map = null;
    private HashMap<String, SQLiteDatabase> m_MySQLiteDBList = new HashMap<>();
    Paint m_RectPaint = null;
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String command, Object pObject) {
            if (command.equals(Downloader.Tag_Downloader_Finish) && pObject != null && (pObject instanceof BaseTileInfo)) {
                XBaseTilesLayer.this.saveTileCache((BaseTileInfo) pObject);
            }
            if (command.equals(Downloader.Tag_Downloader_FinishAndRefresh)) {
                if (pObject != null && (pObject instanceof BaseTileInfo)) {
                    XBaseTilesLayer.this.saveTileCache((BaseTileInfo) pObject);
                    if (XBaseTilesLayer.this.m_Map != null) {
                        XBaseTilesLayer.this.m_Map.RefreshFastNetRasterLayers();
                    }
                }
            } else if (command.equals(Downloader.Tag_Downloader_AllFinish) && pObject != null && (pObject instanceof BaseTileInfo)) {
                XBaseTilesLayer.this.saveTileCache((BaseTileInfo) pObject);
                if (XBaseTilesLayer.this.m_Map != null) {
                    XBaseTilesLayer.this.m_Map.RefreshFastNetRasterLayers();
                }
            }
        }
    };
    ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public XBaseTilesLayer() {
        this._LayerType = ELayerType.ONLINEMAP;
        this._CoordinateSystem = new Coordinate_WebMercator();
        this._MinX = -2.00375083427892E7d;
        this._MinY = -1.9465247E7d;
        this._MaxX = 2.00375083427892E7d;
        this._MaxY = 1.9465247E7d;
        this._Extend = new Envelope(this._MinX, this._MaxY, this._MaxX, this._MinY);
        this._CurrentShowList = new ArrayList();
        this.m_RectPaint = new Paint();
        this.m_RectPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.m_RectPaint.setStrokeWidth(PubVar.ScaledDensity);
        this.m_RectPaint.setStyle(Paint.Style.STROKE);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public void Dispose2() {
        try {
            this._TileScaleCanvas = null;
            this._LevelScale.clear();
        } catch (Exception e) {
        } finally {
            System.gc();
        }
    }

    /* access modifiers changed from: protected */
    public void initLayer() {
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public void SetDrawCanvas(Bitmap bitmap) {
        if (bitmap != null) {
            this._TileScaleCanvas = new Canvas(bitmap);
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public void SetOtherDrawCanvas(Canvas canvas) {
        this._OtherDrawCanvas = canvas;
    }

    public void SetNeedDownload(boolean value) {
        this._NeedDownload = value;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public void setTransparent(int value) {
        this._Transparent = value;
        if (this._Transparent < 0 || this._Transparent > 255) {
            this._Transparent = 255;
        }
        if (this._Transparent == 255) {
            this._CanvasPaint = null;
            return;
        }
        if (this._CanvasPaint == null) {
            this._CanvasPaint = new Paint();
        }
        this._CanvasPaint.setAlpha(this._Transparent);
    }

    public void SetTileType(ERasterLayerType tileLayerType) {
        this._TileLayerType = tileLayerType;
        String tempStr = tileLayerType.name();
        if (tempStr.contains("Label") || tempStr.contains("注记")) {
            this._NeedClearCanvas = true;
        }
        InitialCacheFiles(this);
    }

    public ERasterLayerType GetTileType() {
        return this._TileLayerType;
    }

    public int GetTotalLevel() {
        return this._TotalLevel;
    }

    public int GetCurrentLevel(double cellDistance) {
        int result = -1;
        int tempStart = -1;
        double tempD0 = Double.MAX_VALUE;
        for (Double d : this._LevelScale) {
            double tempD = Math.abs(d.doubleValue() - cellDistance);
            if (tempD < tempD0) {
                result = tempStart;
                tempD0 = tempD;
            }
            tempStart++;
        }
        int result2 = result + InHighLevel;
        if (result2 > this._TotalLevel) {
            result2 = this._TotalLevel;
        }
        if (result2 < 1) {
            return 1;
        }
        return result2;
    }

    /* access modifiers changed from: package-private */
    public List<Double> getLevelScale() {
        return this._LevelScale;
    }

    public String GetMapCachePath() {
        if (this._CacheFolder == null) {
            this._CacheFolder = String.valueOf(PubVar.m_SystemPath) + "/Map/OverMap";
        }
        File localFile = new File(this._CacheFolder);
        if (!localFile.exists()) {
            localFile.mkdir();
        }
        return this._CacheFolder;
    }

    /* access modifiers changed from: package-private */
    public List<Double> GetTileScaleInfo() {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < 22; i++) {
            result.add(Double.valueOf((4.007501668557849E7d / Math.pow(2.0d, (double) i)) / 256.0d));
        }
        return result;
    }

    /* access modifiers changed from: package-private */
    public WebMapTilesDownloader GetOverMapDownloader() {
        if (this._TilesDownloader == null) {
            this._TilesDownloader = new WebMapTilesDownloader(4);
        }
        this._TilesDownloader.setTilesLayer(this);
        return this._TilesDownloader;
    }

    /* access modifiers changed from: package-private */
    public void calMapCorrect(Coordinate coord) {
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public boolean Refresh(Map map) {
        Coordinate localCoordinate3;
        Coordinate localCoordinate4;
        Coordinate tmpTileLeftTop;
        Coordinate tmpTileRightBottom;
        this.m_Map = map;
        try {
            if (this._Transparent == 0 || this._TileLayerType == ERasterLayerType.NONE) {
                return false;
            }
            this._PanRefresh = false;
            if (this._TileScaleCanvas == null) {
                return false;
            }
            this._CurrentLevel = GetCurrentLevel(map.getScale());
            if (this._CurrentLevel > this._TotalLevel) {
                return false;
            }
            if (this._ConsiderTranslate && PubVar.m_Workspace.GetCoorSystem().getIsProjectionCoord() && this._CurrentLevel < 5) {
                return false;
            }
            Envelope tmpMapEnvelope = map.getExtend();
            Coordinate tmpLeftTopCoord = tmpMapEnvelope.getLeftTop().Clone();
            Coordinate tmpRightBotCoord = tmpMapEnvelope.getRightBottom().Clone();
            tmpLeftTopCoord.SetOffset(this._OffsetX, this._OffsetY);
            tmpRightBotCoord.SetOffset(this._OffsetX, this._OffsetY);
            BasicValue localParam1 = new BasicValue();
            BasicValue localParam2 = new BasicValue();
            if (this._ConsiderTranslate) {
                localCoordinate3 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tmpLeftTopCoord, this._CoordinateSystem);
            } else {
                localCoordinate3 = this._CoordinateSystem.ConvertXYToBL(tmpLeftTopCoord);
            }
            if (localCoordinate3.getGeoX() < -180.0d) {
                localCoordinate3.setGeoX(-180.0d);
            }
            if (localCoordinate3.getGeoX() > 180.0d) {
                localCoordinate3.setGeoX(180.0d);
            }
            if (localCoordinate3.getGeoY() < -90.0d) {
                localCoordinate3.setGeoY(-90.0d);
            }
            if (localCoordinate3.getGeoY() > 90.0d) {
                localCoordinate3.setGeoY(90.0d);
            }
            if (this._CurrentLevel > 10) {
                calMapCorrect(localCoordinate3);
            }
            GetTileXY(localCoordinate3.getGeoX(), localCoordinate3.getGeoY(), this._CurrentLevel, localParam1, localParam2);
            BasicValue localParam3 = new BasicValue();
            BasicValue localParam4 = new BasicValue();
            if (this._ConsiderTranslate) {
                localCoordinate4 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tmpRightBotCoord, this._CoordinateSystem);
            } else {
                localCoordinate4 = this._CoordinateSystem.ConvertXYToBL(tmpRightBotCoord);
            }
            if (this._CurrentLevel > 10) {
                double tmpOffsetLongitude = this._OffsetLongitude;
                double tmpOffsetLatitude = this._OffsetLatitude;
                calMapCorrect(localCoordinate4);
                this._OffsetLongitude = (this._OffsetLongitude + tmpOffsetLongitude) / 2.0d;
                this._OffsetLatitude = (this._OffsetLatitude + tmpOffsetLatitude) / 2.0d;
            }
            if (localCoordinate4.getGeoX() < -180.0d) {
                localCoordinate4.setGeoX(-180.0d);
            }
            if (localCoordinate4.getGeoX() > 180.0d) {
                localCoordinate4.setGeoX(180.0d);
            }
            if (localCoordinate4.getGeoY() < -90.0d) {
                localCoordinate4.setGeoY(-90.0d);
            }
            if (localCoordinate4.getGeoY() > 90.0d) {
                localCoordinate4.setGeoY(90.0d);
            }
            GetTileXY(localCoordinate4.getGeoX(), localCoordinate4.getGeoY(), this._CurrentLevel, localParam3, localParam4);
            new ArrayList();
            int minX = localParam1.getInt();
            int maxX = localParam3.getInt();
            int minY = localParam4.getInt();
            int maxY = localParam2.getInt();
            if (minX < 0) {
                minX = 0;
            }
            if (maxX < 0) {
                maxX = 0;
            }
            if (minY < 0) {
                minY = 0;
            }
            if (maxY < 0) {
                maxY = 0;
            }
            if (minX > maxX) {
                minX = maxX;
                maxX = minX;
            }
            if (minY > maxY) {
                minY = maxY;
                maxY = minY;
            }
            int minX2 = minX - MaskBiasXIndex;
            int maxX2 = maxX + MaskBiasXIndex;
            int minY2 = minY - MaskBiasYIndex;
            int maxY2 = maxY + MaskBiasYIndex;
            if (minX2 < 0) {
                minX2 = 0;
            }
            if (minY2 < 0) {
                minY2 = 0;
            }
            int tmpTotalCount = ((maxX2 - minX2) + 1) * ((maxY2 - minY2) + 1);
            if (tmpTotalCount <= 0 || tmpTotalCount > 300) {
                return true;
            }
            BasicValue tmpBasicValue01 = new BasicValue();
            BasicValue tmpBasicValue02 = new BasicValue();
            BasicValue tmpBasicValue03 = new BasicValue();
            BasicValue tmpBasicValue04 = new BasicValue();
            GetTileLL(minX2, minY2, this._CurrentLevel, tmpBasicValue01, tmpBasicValue02);
            GetTileLL(maxX2 + 1, maxY2 + 1, this._CurrentLevel, tmpBasicValue03, tmpBasicValue04);
            if (this._CurrentLevel > 10) {
                tmpBasicValue01.addValue(-this._OffsetLongitude);
                tmpBasicValue02.addValue(-this._OffsetLatitude);
                tmpBasicValue03.addValue(-this._OffsetLongitude);
                tmpBasicValue04.addValue(-this._OffsetLatitude);
            }
            if (this._ConsiderTranslate) {
                tmpTileLeftTop = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(tmpBasicValue01.getDouble(), tmpBasicValue02.getDouble(), 0.0d, this._CoordinateSystem);
                tmpTileRightBottom = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(tmpBasicValue03.getDouble(), tmpBasicValue04.getDouble(), 0.0d, this._CoordinateSystem);
            } else {
                tmpTileLeftTop = this._CoordinateSystem.ConvertBLToXY(tmpBasicValue01.getDouble(), tmpBasicValue02.getDouble());
                tmpTileRightBottom = this._CoordinateSystem.ConvertBLToXY(tmpBasicValue03.getDouble(), tmpBasicValue04.getDouble());
            }
            tmpTileLeftTop.SetOffset(-this._OffsetX, -this._OffsetY);
            tmpTileRightBottom.SetOffset(-this._OffsetX, -this._OffsetY);
            PointF localPoint1 = map.MapToScreenF(tmpTileLeftTop.getX(), tmpTileLeftTop.getY());
            PointF localPoint2 = map.MapToScreenF(tmpTileRightBottom.getX(), tmpTileRightBottom.getY());
            float tmpScreenXMin = localPoint1.x;
            float tmpScreenYMin = localPoint1.y;
            float tmpTileSizeW = (localPoint2.x - tmpScreenXMin) / ((float) ((maxX2 - minX2) + 1));
            float tmpTileSizeH = (localPoint2.y - tmpScreenYMin) / ((float) ((maxY2 - minY2) + 1));
            if (tmpTileSizeW < tmpTileSizeH) {
                tmpTileSizeH = tmpTileSizeW;
            }
            List<String> tmpTilesNameList = new ArrayList<>();
            List<String> tmpNewTilesNameList = new ArrayList<>();
            List<BaseTileInfo> tmpNewTilesList = new ArrayList<>();
            int tmpI01 = -1;
            int tmpCacheCount = 0;
            for (int tempI = minX2; tempI <= maxX2; tempI++) {
                tmpI01++;
                int tmpI02 = -1;
                for (int tempJ = minY2; tempJ <= maxY2; tempJ++) {
                    tmpI02++;
                    BaseTileInfo tempTile = new BaseTileInfo(tempI, tempJ, this._CurrentLevel);
                    boolean tmpAddBool = true;
                    String tmpTileName = tempTile.GetTileName();
                    int tmpTileIndex01 = this._CurrentShowList.indexOf(tmpTileName);
                    if (tmpTileIndex01 > -1) {
                        BaseTileInfo tempTile2 = this.cacheTilesArray.get(tmpTileIndex01);
                        if (tempTile2.Data != null) {
                            tmpCacheCount++;
                            tmpAddBool = false;
                            tempTile2.ScreenXMin = (((float) tmpI01) * tmpTileSizeW) + tmpScreenXMin;
                            tempTile2.ScreenXMax = (((float) (tmpI01 + 1)) * tmpTileSizeW) + tmpScreenXMin;
                            tempTile2.ScreenYMin = (((float) tmpI02) * tmpTileSizeH) + tmpScreenYMin;
                            tempTile2.ScreenYMax = (((float) (tmpI02 + 1)) * tmpTileSizeH) + tmpScreenYMin;
                            tmpNewTilesList.add(tempTile2);
                            tmpTilesNameList.add(tmpTileName);
                        }
                    }
                    if (tmpAddBool) {
                        tempTile.TilesLayer = this;
                        tempTile.ScreenXMin = (((float) tmpI01) * tmpTileSizeW) + tmpScreenXMin;
                        tempTile.ScreenXMax = (((float) (tmpI01 + 1)) * tmpTileSizeW) + tmpScreenXMin;
                        tempTile.ScreenYMin = (((float) tmpI02) * tmpTileSizeH) + tmpScreenYMin;
                        tempTile.ScreenYMax = (((float) (tmpI02 + 1)) * tmpTileSizeH) + tmpScreenYMin;
                        tmpTilesNameList.add(tmpTileName);
                        tmpNewTilesNameList.add(tmpTileName);
                        tmpNewTilesList.add(tempTile);
                    }
                }
            }
            if (tmpNewTilesList.size() > 0) {
                loadTilesData(map, tmpNewTilesList);
            }
            this.cacheTilesArray = tmpNewTilesList;
            this._CurrentShowList = tmpTilesNameList;
            refreshSelf(map);
            return true;
        } catch (Exception ex) {
            Common.Log("XBaseTilesLayer-Refresh", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
        return true;
    }

    public boolean loadTilesData(Map map, List<BaseTileInfo> tiles) {
        BaseTileInfo tmpTileInfo0;
        int tmpTotalCount = tiles.size();
        if (tmpTotalCount > 0) {
            tiles.get(0);
            List<String> tempSYSIDArray = new ArrayList<>();
            HashMap<String, BaseTileInfo> tmpHashMap = new HashMap<>();
            for (BaseTileInfo tmpTileInfo02 : tiles) {
                if (tmpTileInfo02.Data == null) {
                    String tmpString = String.valueOf(String.valueOf(tmpTileInfo02.Row)) + "@" + String.valueOf(tmpTileInfo02.Col) + "@" + String.valueOf(tmpTileInfo02.Level);
                    tempSYSIDArray.add(tmpString);
                    tmpHashMap.put(tmpString, tmpTileInfo02);
                }
            }
            int tmpDBHasCount = 0;
            String tempSql = String.format("select TGEO,Name from %1$s where Name in (%2$s)", this._TableName, "'" + Common.CombineStrings("','", tempSYSIDArray) + "'");
            SQLiteDatabase tmpDatabase = getSQLiteDatabase(String.valueOf(this._CurrentLevel));
            if (tmpDatabase != null) {
                Cursor pCursor = tmpDatabase.rawQuery(tempSql, null);
                while (pCursor.moveToNext()) {
                    byte[] tempBytes = pCursor.getBlob(0);
                    String tmpString2 = pCursor.getString(1);
                    if (!(tempBytes == null || (tmpTileInfo0 = tmpHashMap.get(tmpString2)) == null)) {
                        tmpTileInfo0.Data = tempBytes;
                        tmpDBHasCount++;
                    }
                }
                pCursor.close();
            }
            if (tmpDBHasCount < tmpTotalCount && map.RasterFastResfreshMode && PubVar.m_IsConnectServer) {
                for (BaseTileInfo tempTile : tiles) {
                    if (tempTile.Data == null) {
                        tempTile.Url = CreateTileURL(this._TileLayerType, tempTile.Level, tempTile.Col, tempTile.Row);
                        getDownloader().DownloadTile(tempTile);
                    }
                }
            }
            if (tmpTotalCount == tmpDBHasCount) {
                return true;
            }
        }
        return false;
    }

    public Downloader getDownloader() {
        if (this.m_Downloader == null) {
            this.m_Downloader = new Downloader();
            this.m_Downloader.setCallback(this.pCallback);
        }
        return this.m_Downloader;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean saveTileCacheThread(BaseTileInfo tileInfo) {
        if (tileInfo.Data != null) {
            try {
                SQLiteDatabase tmpSqLiteDatabase = getSQLiteDatabase(String.valueOf(tileInfo.Level));
                if (tmpSqLiteDatabase == null) {
                    return true;
                }
                tmpSqLiteDatabase.execSQL("Replace Into " + this._TableName + " (Name,TGEO) Values (?,?)", new Object[]{String.valueOf(tileInfo.Row) + "@" + tileInfo.Col + "@" + tileInfo.Level, tileInfo.Data});
                return true;
            } catch (Exception e) {
                Common.Log("RasterNetLayer-saveTileCache22", e.getLocalizedMessage());
            }
        }
        return false;
    }

    public boolean saveTileCache(final BaseTileInfo tileInfo) {
        if (tileInfo.Data != null) {
            try {
                this.threadPool.execute(new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer.2
                    @Override // java.lang.Runnable
                    public void run() {
                        XBaseTilesLayer.this.saveTileCacheThread(tileInfo);
                    }
                });
                return true;
            } catch (Exception e) {
                Common.Log("RasterNetLayer-saveTileCache", e.getLocalizedMessage());
            }
        }
        return false;
    }

    private SQLiteDatabase getSQLiteDatabase(String level) {
        SQLiteDatabase result = null;
        if (this.m_MySQLiteDBList.containsKey(level)) {
            return this.m_MySQLiteDBList.get(level);
        }
        File tempDBFile = new File(String.valueOf(this._CacheFolder) + "/MapBase" + level + ".db");
        if (tempDBFile.exists()) {
            result = SQLiteDatabase.openDatabase(tempDBFile.getAbsolutePath(), null, 0);
            this.m_MySQLiteDBList.put(level, result);
        }
        return result;
    }

    public boolean ClearCurrentShow() {
        if (this._CurrentShowList.size() <= 0) {
            return false;
        }
        File tempDBFile = new File(String.valueOf(this._CacheFolder) + "/MapBase" + this._CurrentLevel + ".db");
        if (!tempDBFile.exists()) {
            return false;
        }
        String tmpStr = Common.CombineStrings("','", this._CurrentShowList);
        SQLiteDatabase tempDatabase = SQLiteDatabase.openDatabase(tempDBFile.getAbsolutePath(), null, 0);
        tempDatabase.execSQL("Delete From " + this._TableName + " Where Name in('" + tmpStr + "')");
        tempDatabase.close();
        return true;
    }

    public boolean ClearCacheData(int level, double xmin, double ymin, double xmax, double ymax) {
        try {
            BasicValue localParam1 = new BasicValue();
            BasicValue localParam2 = new BasicValue();
            BasicValue localParam3 = new BasicValue();
            BasicValue localParam4 = new BasicValue();
            GetTileXY(xmin, ymin, level, localParam1, localParam2);
            GetTileXY(xmax, ymax, level, localParam3, localParam4);
            int minX = localParam1.getInt();
            int maxX = localParam3.getInt();
            int minY = localParam4.getInt();
            int maxY = localParam2.getInt();
            if (minX < 0) {
                minX = 0;
            }
            if (maxX < 0) {
                maxX = 0;
            }
            if (minY < 0) {
                minY = 0;
            }
            if (maxY < 0) {
                maxY = 0;
            }
            if (minX > maxX) {
                minX = maxX;
                maxX = minX;
            }
            if (minY > maxY) {
                minY = maxY;
                maxY = minY;
            }
            if (((maxX - minX) + 1) * ((maxY - minY) + 1) <= 0) {
                return false;
            }
            File tempDBFile = new File(String.valueOf(this._CacheFolder) + "/MapBase" + String.valueOf(level) + ".db");
            if (tempDBFile.exists()) {
                SQLiteDatabase tempDatabase = SQLiteDatabase.openDatabase(tempDBFile.getAbsolutePath(), null, 0);
                List<String> tmpList = new ArrayList<>();
                for (int tempI = minX; tempI <= maxX; tempI++) {
                    for (int tempJ = minY; tempJ <= maxY; tempJ++) {
                        BaseTileInfo tempTile = new BaseTileInfo(tempI, tempJ, level);
                        tempTile.Url = CreateTileURL(this._TileLayerType, tempTile.Level, tempTile.Col, tempTile.Row);
                        tmpList.add(tempTile.GetTileName());
                        if (tmpList.size() > 99) {
                            tempDatabase.execSQL("Delete From " + this._TableName + " Where Name in('" + Common.CombineStrings("','", tmpList) + "')");
                            tmpList = new ArrayList<>();
                        }
                    }
                }
                if (tmpList.size() > 0) {
                    tempDatabase.execSQL("Delete From " + this._TableName + " Where Name in('" + Common.CombineStrings("','", tmpList) + "')");
                }
                tempDatabase.close();
                return true;
            }
            return false;
        } catch (Exception ex) {
            Common.Log("错误", ex.getMessage());
        }
        return false;
    }

    public static String[] GetCacheTableName(ERasterLayerType tileLayerType) {
        String[] result = null;
        if (tileLayerType == ERasterLayerType.NONE) {
            return null;
        }
        if (tileLayerType == ERasterLayerType.Google_Satellite) {
            result = new String[]{"g_Sat"};
        } else if (tileLayerType == ERasterLayerType.Google_Street) {
            result = new String[]{"g_Str"};
        } else if (tileLayerType == ERasterLayerType.Google_Terrain) {
            result = new String[]{"g_Ter"};
        } else if (tileLayerType == ERasterLayerType.Google_Address) {
            result = new String[]{"g_Address"};
        } else if (tileLayerType == ERasterLayerType.TianDiTu_Satellite) {
            result = new String[]{"TDT_IMG"};
        } else if (tileLayerType == ERasterLayerType.TianDiTu_Vector) {
            result = new String[]{"TDT_VEC"};
        } else if (tileLayerType == ERasterLayerType.TianDiTu_VectorLabel) {
            result = new String[]{"TDT_CVA"};
        } else if (tileLayerType == ERasterLayerType.TianDiTu_SatelliteLabel) {
            result = new String[]{"TDT_CIA"};
        } else if (tileLayerType == ERasterLayerType.ContourMap) {
            result = new String[]{"contour"};
        }
        return result;
    }

    public static void InitialCacheFiles(XBaseTilesLayer tileLayer) {
        try {
            File file = new File(tileLayer._CacheFolder);
            if (!file.exists()) {
                file.mkdir();
            }
            String[] tempStrs = GetCacheTableName(tileLayer._TileLayerType);
            if (tempStrs != null && tempStrs.length > 0) {
                tileLayer._TableName = tempStrs[0];
                int tempLevel = tileLayer._TotalLevel;
                for (int i = 1; i <= tempLevel; i++) {
                    String tempPath = String.valueOf(tileLayer._CacheFolder) + "/MapBase" + i + ".db";
                    new File(tempPath);
                    SQLiteDatabase tempDatabase = SQLiteDatabase.openOrCreateDatabase(tempPath, (SQLiteDatabase.CursorFactory) null);
                    int length = tempStrs.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        tempDatabase.execSQL("Create Table IF Not EXISTS " + tempStrs[i2] + " (Name text Not NUll UNIQUE,TGEO blob)");
                    }
                    tempDatabase.close();
                }
            }
        } catch (Exception ex) {
            Common.Log("InitialCacheFiles", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    public String CreateTileURL(ERasterLayerType tileLayerType, int level, int col, int row) {
        return "";
    }

    /* access modifiers changed from: package-private */
    public byte[] getTileFromCache(int level, int col, int row) {
        byte[] result = null;
        try {
            File tempFile = new File(String.valueOf(this._CacheFolder) + "/MapBase" + level + ".db");
            if (tempFile.exists()) {
                @SuppressLint("WrongConstant") SQLiteDatabase tempDatabase = SQLiteDatabase.openDatabase(tempFile.getAbsolutePath(), null, 1);
                Cursor pCursor = tempDatabase.rawQuery("Select TGEO From " + this._TableName + " Where Name='" + row + "@" + col + "@" + level + "'", null);
                if (pCursor.moveToFirst()) {
                    result = pCursor.getBlob(0);
                }
                tempDatabase.close();
            }
        } catch (Exception e) {
        }
        return result;
    }

    /* access modifiers changed from: package-private */
    public boolean saveTileCache(int level, int col, int row, byte[] data) {
        return SaveTileCacheData(String.valueOf(this._CacheFolder) + "/MapBase" + level + ".db", this._TableName, level, col, row, data);
    }

    public boolean saveTileCache(BaseTileInfo tile, byte[] data) {
        if (data != null) {
            return SaveTileCacheData(String.valueOf(this._CacheFolder) + "/MapBase" + tile.Level + ".db", this._TableName, tile.Level, tile.Col, tile.Row, data);
        }
        return false;
    }

    public boolean saveTilesCache(List<BaseTileInfo> tiles) {
        try {
            if (tiles.size() > 0) {
                SQLiteDatabase tempDatabase = null;
                int tempLevel = -1;
                for (BaseTileInfo tempTile : tiles) {
                    if (tempTile.TileBitmap != null) {
                        if (tempLevel != tempTile.Level) {
                            if (tempDatabase != null) {
                                tempDatabase.close();
                            }
                            String tempPath = String.valueOf(this._CacheFolder) + "/MapBase" + tempTile.Level + ".db";
                            new File(tempPath);
                            tempDatabase = SQLiteDatabase.openOrCreateDatabase(tempPath, (SQLiteDatabase.CursorFactory) null);
                            tempLevel = tempTile.Level;
                        }
                        byte[] tempBytes = Common.ConvertBmp2Stream(tempTile.TileBitmap);
                        if (tempBytes != null) {
                            tempDatabase.execSQL("Replace Into " + this._TableName + " (Name,TGEO) Values (?,?)", new Object[]{tempTile.GetTileName(), tempBytes});
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Common.Log("saveTilesCache", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
        return true;
    }

    public boolean ShowImage(Map map, BaseTileInfo tile, byte[] data) {
        try {
            if (this._TileScaleCanvas == null) {
                return false;
            }
            if (tile.Level != this._CurrentLevel) {
                return false;
            }
            if (tile.getScreenWidth() > 0.0f && tile.getScreenHeight() > 0.0f) {
                float tmpXMin = tile.ScreenXMin - map.getMaskBiasX();
                float tmpXMax = tile.ScreenXMax - map.getMaskBiasX();
                float tmpYMin = tile.ScreenYMin - map.getMaskBiasY();
                float tmpYMax = tile.ScreenYMax - map.getMaskBiasY();
                if (tmpXMax < 0.0f || tmpXMin > ((float) this._TileScaleCanvas.getWidth()) || tmpYMax < 0.0f || tmpYMin > ((float) this._TileScaleCanvas.getHeight())) {
                    return true;
                }
                if (tile.TileBitmap == null) {
                    if (data.length > 100000) {
                        return false;
                    }
                    tile.TileBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                }
                Bitmap localBitmap = tile.TileBitmap;
                if (localBitmap != null) {
                    this._TileScaleCanvas.drawBitmap(localBitmap, new Rect(0, 0, localBitmap.getWidth(), localBitmap.getHeight()), new RectF(tmpXMin, tmpYMin, tmpXMax, tmpYMax), this._CanvasPaint);
                }
                if (PubVar.RasterLayerShowRect) {
                    this._TileScaleCanvas.drawRect(new RectF(tmpXMin, tmpYMin, tmpXMax, tmpYMax), this.m_RectPaint);
                }
                if (this._OtherDrawCanvas != null) {
                    this._OtherDrawCanvas.drawBitmap(localBitmap, new Rect(0, 0, localBitmap.getWidth(), localBitmap.getHeight()), new RectF(tmpXMin, tmpYMin, tmpXMax, tmpYMax), this._CanvasPaint);
                }
                return true;
            }
            return false;
        } catch (Exception ex) {
            System.gc();
            Common.Log("ShowImage", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
        return false;
    }

    public boolean ShowImageWithCRLevel11(Map map, int col, int row, int level, byte[] paramArrayOfByte) {
        Coordinate localCoordinate1;
        Coordinate localCoordinate2;
        try {
            if (this._TileScaleCanvas == null) {
                return false;
            }
            if (level != this._CurrentLevel) {
                return false;
            }
            BasicValue localParam1 = new BasicValue();
            BasicValue localParam2 = new BasicValue();
            BasicValue localParam3 = new BasicValue();
            BasicValue localParam4 = new BasicValue();
            GetTileLL(col, row, level, localParam1, localParam2);
            GetTileLL(col + 1, row + 1, level, localParam3, localParam4);
            if (level > 10) {
                localParam1.addValue(-this._OffsetLongitude);
                localParam2.addValue(-this._OffsetLatitude);
                localParam3.addValue(-this._OffsetLongitude);
                localParam4.addValue(-this._OffsetLatitude);
            }
            if (this._ConsiderTranslate) {
                localCoordinate1 = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(localParam1.getDouble(), localParam2.getDouble(), 0.0d, this._CoordinateSystem);
                localCoordinate2 = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(localParam3.getDouble(), localParam4.getDouble(), 0.0d, this._CoordinateSystem);
            } else {
                localCoordinate1 = this._CoordinateSystem.ConvertBLToXY(localParam1.getDouble(), localParam2.getDouble());
                localCoordinate2 = this._CoordinateSystem.ConvertBLToXY(localParam3.getDouble(), localParam4.getDouble());
            }
            localCoordinate1.SetOffset(-this._OffsetX, -this._OffsetY);
            localCoordinate2.SetOffset(-this._OffsetX, -this._OffsetY);
            Point localPoint1 = map.MapToScreen(localCoordinate1);
            Point localPoint2 = map.MapToScreen(localCoordinate2);
            try {
                if (!(localPoint1.x == localPoint2.x || localPoint1.y == localPoint2.y)) {
                    Bitmap localBitmap = BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length);
                    this._TileScaleCanvas.drawBitmap(localBitmap, new Rect(0, 0, localBitmap.getWidth(), localBitmap.getHeight()), new Rect(localPoint1.x, localPoint1.y, localPoint2.x, localPoint2.y), this._CanvasPaint);
                    if (this._OtherDrawCanvas != null) {
                        this._OtherDrawCanvas.drawBitmap(localBitmap, new Rect(0, 0, localBitmap.getWidth(), localBitmap.getHeight()), new Rect(localPoint1.x, localPoint1.y, localPoint2.x, localPoint2.y), this._CanvasPaint);
                    }
                    if (localBitmap != null && !localBitmap.isRecycled()) {
                        localBitmap.recycle();
                    }
                    return true;
                }
            } catch (NullPointerException e) {
                System.gc();
            }
            return false;
        } catch (Exception ex) {
            Common.Log("ShowImage", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
        return false;
    }

    public static boolean SaveTileCacheData(String filePath, String tableName, int level, int col, int row, byte[] data) {
        File tempFile = new File(filePath);
        if (tempFile.exists()) {
            SQLiteDatabase tempDatabase = SQLiteDatabase.openDatabase(tempFile.getAbsolutePath(), null, 0);
            tempDatabase.execSQL("Replace Into " + tableName + " (Name,TGEO) Values (?,?)", new Object[]{String.valueOf(row) + "@" + col + "@" + level, data});
            tempDatabase.close();
        }
        return true;
    }

    public static void GetTileLL(int col, int row, int level, BasicValue longitude, BasicValue latitude) {
        double d1 = Math.pow(2.0d, (double) level);
        longitude.setValue(((360.0d / d1) * ((double) col)) - 180.0d);
        double d2 = Math.exp(2.0d * 3.141592653589793d * (1.0d - (((double) (row * 2)) / d1)));
        latitude.setValue((180.0d * Math.asin((d2 - 1.0d) / (1.0d + d2))) / 3.141592653589793d);
    }

    public static void GetTileXY(double longitude, double latitude, int level, BasicValue paramCol, BasicValue paramRow) {
        double d4;
        double tempPowLevel = Math.pow(2.0d, (double) level);
        double d1 = (180.0d + longitude) / (360.0d / tempPowLevel);
        if (latitude > 90.0d) {
            latitude -= 180.0d;
        }
        if (latitude < -90.0d) {
            latitude += 180.0d;
        }
        if (latitude > 89.99999998d) {
            d4 = 1.0d;
        } else if (latitude < -89.9999998d) {
            d4 = 0.0d;
        } else {
            double d2 = (3.141592653589793d * latitude) / 180.0d;
            d4 = tempPowLevel * ((1.0d - ((0.5d * Math.log((1.0d + Math.sin(d2)) / (1.0d - Math.sin(d2)))) / 3.141592653589793d)) / 2.0d);
        }
        paramCol.setValue((int) d1);
        paramRow.setValue((int) d4);
    }

    public void setOffsetX(double value) {
        this._OffsetX = value;
    }

    public void setOffsetY(double value) {
        this._OffsetY = value;
    }

    public double getOffsetX() {
        return this._OffsetX;
    }

    public double getOffsetY() {
        return this._OffsetY;
    }

    public boolean getIsConsiderTranslate() {
        return this._ConsiderTranslate;
    }

    public void setIsConsiderTranslate(boolean considerTranslate) {
        this._ConsiderTranslate = considerTranslate;
    }

    public void refreshSelf() {
        if (this.m_Map != null) {
            refreshSelf(this.m_Map);
        }
    }

    public void refreshSelf(Map map) {
        try {
            if (this.cacheTilesArray.size() > 0) {
                for (BaseTileInfo tmpBaseTileInfo : this.cacheTilesArray) {
                    if (tmpBaseTileInfo.Data != null) {
                        ShowImage(map, tmpBaseTileInfo, tmpBaseTileInfo.Data);
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
