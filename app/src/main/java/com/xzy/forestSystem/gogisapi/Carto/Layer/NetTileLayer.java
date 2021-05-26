package  com.xzy.forestSystem.gogisapi.Carto.Layer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import  com.xzy.forestSystem.gogisapi.Common.WebMapTilesDownloader;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WebMercator;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class NetTileLayer extends XBaseTilesLayer {
    public static int InHighLevel = 0;
    double OriginalLatitude = 90.0d;
    double OriginalLongitude = -180.0d;
    int StartLevel = 0;
    String _CacheFolder = null;
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
    public boolean _PanRefresh = false;
    protected ERasterLayerType _TileLayerType = ERasterLayerType.NONE;
    private WebMapTilesDownloader _TilesDownloader = null;
    protected int _TotalLevel = 20;
    String m_ConfigURLString = "http://219.142.81.39/newgsigrid/CGSGeoData/Handler.ashx?url=10.90.3.198:6163/igs/rest/mrms/tile/chs5_/{$z}/{$y}/{$x}";
    double m_MapLatMax = 90.0d;
    double m_MapLatMin = -90.0d;
    double m_MapLngMax = 180.0d;
    double m_MapLngMin = -180.0d;
    List<Integer[]> m_MapTileMatrixList = new ArrayList();
    Paint m_RectPaint = null;
    List<Double> m_ResolutionList = new ArrayList();
    private SQLiteDatabase m_SQLiteDatabase = null;
    String m_ServerURLString = "";

    public NetTileLayer() {
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

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer,  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public void Dispose2() {
        try {
            this._TileScaleCanvas = null;
            this._LevelScale.clear();
        } catch (Exception e) {
        } finally {
            System.gc();
        }
    }

    public void initLayer(RasterLayer layer) {
        this._LayerName = layer.GetLayerName();
        this._Name = layer.GetLayerID();
        this._LayerIndex = layer.GetLayerIndex();
        setTransparent(layer.GetTransparet());
        setCoordinateSystem(layer.GetCoorSystem());
        setTileType(layer.GetLayerType());
        setOffsetX(layer._OffsetX);
        setOffsetY(layer._OffsetY);
        this.StartLevel = layer.StartLevel;
        setIsConsiderTranslate(layer.getIsConsiderTranslate());
        this._LevelScale = GetTileScaleInfo();
        String tmpURL = layer.GetFilePath();
        if (tmpURL.length() > 0) {
            setMapServer(tmpURL);
        } else {
            setMapServer("http://219.142.81.39/newgsigrid/CGSGeoData/Handler.ashx?url=10.90.3.198:6163/igs/rest/mrms/tile/chs5_/{$z}/{$y}/{$x}");
        }
        setNetMapConfig(layer.MapConfigString);
    }

    public void setMaxLevel(int maxLevel) {
        this._TotalLevel = maxLevel;
        this._LevelScale = GetTileScaleInfo();
    }

    public void setMapServer(String url) {
        this.m_ConfigURLString = url;
        builderServerURL();
    }

    public void setCoordinateSystem(String coordinateName) {
        this._CoordinateSystem = AbstractC0383CoordinateSystem.CreateCoordinateSystem(coordinateName);
    }

    public AbstractC0383CoordinateSystem getCoordinateSystem() {
        return this._CoordinateSystem;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer,  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
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

    public void setTileType(ERasterLayerType tileLayerType) {
        this._TileLayerType = tileLayerType;
        InitialCacheFiles(this);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
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

    public void setNetMapConfig(String jsonCoinfig) {
        JSONObject jsonObject00;
        JSONObject jsonObject03;
        JSONArray jsonArray01;
        this.m_MapTileMatrixList = new ArrayList();
        try {
            JSONObject jsonObject = new JSONObject(jsonCoinfig);
            if (!(jsonObject == null || (jsonObject00 = jsonObject.getJSONObject("Layer")) == null)) {
                JSONObject jsonObject01 = jsonObject00.getJSONObject("ows:BoundingBox");
                String[] tmpStrs01 = jsonObject01.getString("ows:LowerCorner").split(" ");
                if (tmpStrs01 != null && tmpStrs01.length > 1) {
                    this.m_MapLngMin = Double.parseDouble(tmpStrs01[0]);
                    this.m_MapLatMin = Double.parseDouble(tmpStrs01[1]);
                }
                String[] tmpStrs012 = jsonObject01.getString("ows:UpperCorner").split(" ");
                if (tmpStrs012 != null && tmpStrs012.length > 1) {
                    this.m_MapLngMax = Double.parseDouble(tmpStrs012[0]);
                    this.m_MapLatMax = Double.parseDouble(tmpStrs012[1]);
                }
                JSONObject jsonObject02 = jsonObject00.getJSONObject("TileMatrixSetLink");
                if (!(jsonObject02 == null || (jsonObject03 = jsonObject02.getJSONObject("TileMatrixSetLimits")) == null || (jsonArray01 = jsonObject03.getJSONArray("TileMatrixLimits")) == null)) {
                    int count = jsonArray01.length();
                    for (int i = 0; i < count; i++) {
                        Integer[] tmpIntegers = {0, 0, 0, 0};
                        try {
                            JSONObject jsonObject04 = (JSONObject) jsonArray01.get(i);
                            tmpIntegers[0] = Integer.valueOf(Integer.parseInt(jsonObject04.getString("MinTileCol")));
                            tmpIntegers[1] = Integer.valueOf(Integer.parseInt(jsonObject04.getString("MinTileRow")));
                            tmpIntegers[2] = Integer.valueOf(Integer.parseInt(jsonObject04.getString("MaxTileCol")));
                            tmpIntegers[3] = Integer.valueOf(Integer.parseInt(jsonObject04.getString("MaxTileRow")));
                        } catch (Exception e) {
                        }
                        this.m_MapTileMatrixList.add(tmpIntegers);
                    }
                }
            }
        } catch (Exception e2) {
        }
        this._TotalLevel = this.m_MapTileMatrixList.size();
        this._LevelScale = GetTileScaleInfo();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public String GetMapCachePath() {
        if (this._CacheFolder == null) {
            this._CacheFolder = String.valueOf(PubVar.m_SystemPath) + "/Map/" + this._Name + ".db";
        }
        return this._CacheFolder;
    }

    /* access modifiers changed from: package-private */
    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public List<Double> GetTileScaleInfo() {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i <= this._TotalLevel; i++) {
            result.add(Double.valueOf((4.007501668557849E7d / Math.pow(2.0d, (double) i)) / 256.0d));
        }
        return result;
    }

    /* access modifiers changed from: package-private */
    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public WebMapTilesDownloader GetOverMapDownloader() {
        if (this._TilesDownloader == null) {
            this._TilesDownloader = new WebMapTilesDownloader(2);
        }
        this._TilesDownloader.setTilesLayer(this);
        return this._TilesDownloader;
    }

    public static void InitialCacheFiles(NetTileLayer tileLayer) {
        try {
            File file = new File(tileLayer.GetMapCachePath());
            if (!file.exists()) {
                SQLiteDatabase tempDatabase = SQLiteDatabase.openOrCreateDatabase(file.getAbsolutePath(), (SQLiteDatabase.CursorFactory) null);
                int tempLevel = tileLayer._TotalLevel;
                for (int i = 1; i <= tempLevel; i++) {
                    tempDatabase.execSQL("Create Table IF Not EXISTS Level" + String.valueOf(i) + " (Name text Not NUll UNIQUE, RowIndex Int Default 0, CowIndex Integer Default 0, TGEO blob)");
                }
                tempDatabase.close();
            }
        } catch (Exception ex) {
            Common.Log("InitialCacheFiles", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    private String builderServerURL() {
        if (this.m_ServerURLString.length() == 0) {
            this.m_ServerURLString = this.m_ConfigURLString.replace("{$z}", "%1$s");
            this.m_ServerURLString = this.m_ServerURLString.replace("{$x}", "%2$s");
            this.m_ServerURLString = this.m_ServerURLString.replace("{$y}", "%3$s");
        }
        return this.m_ServerURLString;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public String CreateTileURL(ERasterLayerType tileLayerType, int level, int col, int row) {
        return String.format(builderServerURL(), Integer.valueOf(level - this.StartLevel), Integer.valueOf(col), Integer.valueOf(row));
    }

    public boolean saveTileCacheData(String tableName, int level, int col, int row, byte[] data) {
        try {
            SQLiteDatabase tempDatabase = getSQLiteDatabase();
            if (tempDatabase == null) {
                return false;
            }
            tempDatabase.execSQL("Replace Into " + tableName + " (Name,TGEO) Values (?,?)", new Object[]{String.valueOf(row) + "@" + col + "@" + String.valueOf(level), data});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public byte[] getTileFromCache(int level, int col, int row) {
        try {
            SQLiteDatabase tempDatabase = getSQLiteDatabase();
            if (tempDatabase == null) {
                return null;
            }
            Cursor pCursor = tempDatabase.rawQuery("Select TGEO From Level" + String.valueOf(level) + " Where Name='" + row + "@" + col + "@" + level + "'", null);
            if (pCursor.moveToFirst()) {
                return pCursor.getBlob(0);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private void getTileLL(int col, int row, int level, BasicValue longitude, BasicValue latitude) {
        double d1 = Math.pow(2.0d, (double) level);
        double tmpLong = ((360.0d / d1) * ((double) col)) - 180.0d;
        if (tmpLong > 180.0d) {
            tmpLong = 180.0d;
        }
        if (tmpLong < -180.0d) {
            tmpLong = -180.0d;
        }
        longitude.setValue(tmpLong);
        double d2 = Math.exp(2.0d * 3.141592653589793d * (1.0d - (((double) (row * 2)) / d1)));
        latitude.setValue((180.0d * Math.asin((d2 - 1.0d) / (1.0d + d2))) / 3.141592653589793d);
    }

    private void getTileXY(double longitude, double latitude, int level, BasicValue paramCol, BasicValue paramRow) {
        double d4;
        double tempPowLevel = Math.pow(2.0d, (double) level);
        double xtile = ((180.0d + longitude) / 360.0d) * tempPowLevel;
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
        paramCol.setValue((int) xtile);
        paramRow.setValue((int) d4);
    }

    private void checkTileExtend(BasicValue minX, BasicValue minY, BasicValue maxX, BasicValue maxY, int level) {
        int tmpLevel = level - this.StartLevel;
        if (tmpLevel < this.m_MapTileMatrixList.size()) {
            Integer[] tmpInts = this.m_MapTileMatrixList.get(tmpLevel);
            if (minX.getInt() > maxX.getInt()) {
                int tmpI = minX.getInt();
                minX.setValue(maxX.getInt());
                maxX.setValue(tmpI);
            }
            if (minY.getInt() > maxY.getInt()) {
                int tmpI2 = minY.getInt();
                minY.setValue(maxY.getInt());
                maxY.setValue(tmpI2);
            }
            if (minX.getInt() < tmpInts[0].intValue()) {
                minX.setValue(tmpInts[0].intValue());
            }
            if (minY.getInt() < tmpInts[1].intValue()) {
                minY.setValue(tmpInts[1].intValue());
            }
            if (maxX.getInt() > tmpInts[2].intValue()) {
                maxX.setValue(tmpInts[2].intValue());
            }
            if (maxY.getInt() > tmpInts[3].intValue()) {
                maxY.setValue(tmpInts[3].intValue());
            }
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer,  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public boolean Refresh(Map map) {
        Coordinate localCoordinate3;
        Coordinate localCoordinate4;
        Coordinate tmpTileLeftTop;
        Coordinate tmpTileRightBottom;
        this._CurrentShowList.clear();
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
            getTileXY(localCoordinate3.getGeoX(), localCoordinate3.getGeoY(), this._CurrentLevel, localParam1, localParam2);
            BasicValue localParam3 = new BasicValue();
            BasicValue localParam4 = new BasicValue();
            if (this._ConsiderTranslate) {
                localCoordinate4 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tmpRightBotCoord, this._CoordinateSystem);
            } else {
                localCoordinate4 = this._CoordinateSystem.ConvertXYToBL(tmpRightBotCoord);
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
            getTileXY(localCoordinate4.getGeoX(), localCoordinate4.getGeoY(), this._CurrentLevel, localParam3, localParam4);
            List<BaseTileInfo> tempTilesArray = new ArrayList<>();
            checkTileExtend(localParam1, localParam4, localParam3, localParam2, this._CurrentLevel);
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
            int tmpTotalCount = ((maxX - minX) + 1) * ((maxY - minY) + 1);
            if (tmpTotalCount <= 0 || tmpTotalCount > 100) {
                return true;
            }
            BasicValue tmpBasicValue01 = new BasicValue();
            BasicValue tmpBasicValue02 = new BasicValue();
            BasicValue tmpBasicValue03 = new BasicValue();
            BasicValue tmpBasicValue04 = new BasicValue();
            getTileLL(minX, minY, this._CurrentLevel, tmpBasicValue01, tmpBasicValue02);
            getTileLL(maxX + 1, maxY + 1, this._CurrentLevel, tmpBasicValue03, tmpBasicValue04);
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
            float tmpTileSizeW = (localPoint2.x - tmpScreenXMin) / ((float) ((maxX - minX) + 1));
            float tmpTileSizeH = (localPoint2.y - tmpScreenYMin) / ((float) ((maxY - minY) + 1));
            if (tmpTileSizeW < tmpTileSizeH) {
                tmpTileSizeW = tmpTileSizeH;
            } else {
                tmpTileSizeH = tmpTileSizeW;
            }
            int tmpI01 = -1;
            for (int tempI = minX; tempI <= maxX; tempI++) {
                tmpI01++;
                int tmpI02 = -1;
                for (int tempJ = minY; tempJ <= maxY; tempJ++) {
                    tmpI02++;
                    BaseTileInfo tempTile = new BaseTileInfo(tempI, tempJ, this._CurrentLevel);
                    tempTile.Url = CreateTileURL(this._TileLayerType, tempTile.Level, tempTile.Col, tempTile.Row);
                    tempTile.ScreenXMin = (((float) tmpI01) * tmpTileSizeW) + tmpScreenXMin;
                    tempTile.ScreenXMax = (((float) (tmpI01 + 1)) * tmpTileSizeW) + tmpScreenXMin;
                    tempTile.ScreenYMin = (((float) tmpI02) * tmpTileSizeH) + tmpScreenYMin;
                    tempTile.ScreenYMax = (((float) (tmpI02 + 1)) * tmpTileSizeH) + tmpScreenYMin;
                    this._CurrentShowList.add(tempTile.GetTileName());
                    tempTilesArray.add(tempTile);
                }
            }
            if (this._CurrentShowList.size() > 0) {
                List<String> tempHasDownloadNames = new ArrayList<>();
                String tempSql = String.format("select Name,TGEO from %1$s where Name in (%2$s)", "Level" + String.valueOf(this._CurrentLevel), "'" + Common.CombineStrings("','", this._CurrentShowList) + "'");
                SQLiteDatabase tempDatabase = getSQLiteDatabase();
                if (tempDatabase != null) {
                    Cursor pCursor = tempDatabase.rawQuery(tempSql, null);
                    while (pCursor.moveToNext()) {
                        String tempTileName = pCursor.getString(0);
                        int tempIndex = this._CurrentShowList.indexOf(tempTileName);
                        if (tempIndex >= 0) {
                            BaseTileInfo tempTile2 = tempTilesArray.get(tempIndex);
                            byte[] tempBytes = pCursor.getBlob(1);
                            if (tempBytes == null || ShowImage(map, tempTile2, tempBytes)) {
                                tempHasDownloadNames.add(tempTileName);
                            }
                        }
                    }
                    pCursor.close();
                }
                if (this._NeedDownload && map.RasterFastResfreshMode && PubVar.m_IsConnectServer && tempHasDownloadNames.size() < tempTilesArray.size()) {
                    List<BaseTileInfo> tempNeedDownTiles = new ArrayList<>();
                    for (BaseTileInfo tempTile3 : tempTilesArray) {
                        if (!tempHasDownloadNames.contains(tempTile3.GetTileName())) {
                            tempNeedDownTiles.add(tempTile3);
                        }
                    }
                    if (tempNeedDownTiles.size() > 0) {
                        GetOverMapDownloader().setDownloadFileList(tempNeedDownTiles);
                        GetOverMapDownloader().StartUpLoad();
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            Common.Log("XBaseTilesLayer-Refresh", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
        return true;
    }

    private SQLiteDatabase getSQLiteDatabase() {
        if (this.m_SQLiteDatabase == null) {
            this.m_SQLiteDatabase = SQLiteDatabase.openDatabase(GetMapCachePath(), null, 0);
        }
        return this.m_SQLiteDatabase;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public boolean ClearCurrentShow() {
        SQLiteDatabase tempDatabase;
        if (this._CurrentShowList.size() <= 0 || (tempDatabase = getSQLiteDatabase()) == null) {
            return false;
        }
        tempDatabase.execSQL("Delete From Level" + String.valueOf(this._CurrentLevel) + " Where Name in('" + Common.CombineStrings("','", this._CurrentShowList) + "')");
        return true;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public boolean ClearCacheData(int level, double xmin, double ymin, double xmax, double ymax) {
        SQLiteDatabase tempDatabase;
        try {
            BasicValue localParam1 = new BasicValue();
            BasicValue localParam2 = new BasicValue();
            BasicValue localParam3 = new BasicValue();
            BasicValue localParam4 = new BasicValue();
            getTileXY(xmin, ymin, level, localParam1, localParam2);
            getTileXY(xmax, ymax, level, localParam3, localParam4);
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
            if (((maxX - minX) + 1) * ((maxY - minY) + 1) > 0 && (tempDatabase = getSQLiteDatabase()) != null) {
                List<String> tmpList = new ArrayList<>();
                for (int tempI = minX; tempI <= maxX; tempI++) {
                    for (int tempJ = minY; tempJ <= maxY; tempJ++) {
                        BaseTileInfo tempTile = new BaseTileInfo(tempI, tempJ, level);
                        tempTile.Url = CreateTileURL(this._TileLayerType, tempTile.Level, tempTile.Col, tempTile.Row);
                        tmpList.add(tempTile.GetTileName());
                        if (tmpList.size() > 99) {
                            tempDatabase.execSQL("Delete From Level" + String.valueOf(level) + " Where Name in('" + Common.CombineStrings("','", tmpList) + "')");
                            tmpList = new ArrayList<>();
                        }
                    }
                }
                if (tmpList.size() > 0) {
                    tempDatabase.execSQL("Delete From Level" + String.valueOf(level) + " Where Name in('" + Common.CombineStrings("','", tmpList) + "')");
                }
                return true;
            }
        } catch (Exception ex) {
            Common.Log("错误", ex.getMessage());
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public boolean saveTileCache(int level, int col, int row, byte[] data) {
        return saveTileCacheData("Level" + String.valueOf(level), level, col, row, data);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public boolean saveTileCache(BaseTileInfo tile, byte[] data) {
        if (data != null) {
            return saveTileCacheData("Level" + String.valueOf(tile.Level), tile.Level, tile.Col, tile.Row, data);
        }
        return false;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public boolean saveTilesCache(List<BaseTileInfo> tiles) {
        byte[] tempBytes;
        try {
            SQLiteDatabase tempDatabase = getSQLiteDatabase();
            if (tempDatabase != null && tiles.size() > 0) {
                for (BaseTileInfo tempTile : tiles) {
                    if (!(tempTile.TileBitmap == null || (tempBytes = Common.ConvertBmp2Stream(tempTile.TileBitmap)) == null)) {
                        tempDatabase.execSQL("Replace Into Level" + String.valueOf(tempTile.Level) + " (Name,TGEO) Values (?,?)", new Object[]{tempTile.GetTileName(), tempBytes});
                    }
                }
            }
        } catch (Exception ex) {
            Common.Log("saveTilesCache", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
        return true;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public boolean ShowImage(Map map, BaseTileInfo tile, byte[] data) {
        try {
            if (this._TileScaleCanvas == null || tile.Level != this._CurrentLevel || tile.getScreenWidth() <= 0.0f || tile.getScreenHeight() <= 0.0f) {
                return false;
            }
            Bitmap localBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            this._TileScaleCanvas.drawBitmap(localBitmap, new Rect(0, 0, localBitmap.getWidth(), localBitmap.getHeight()), new RectF(tile.ScreenXMin, tile.ScreenYMin, tile.ScreenXMax, tile.ScreenYMax), this._CanvasPaint);
            if (PubVar.RasterLayerShowRect) {
                this._TileScaleCanvas.drawRect(new RectF(tile.ScreenXMin, tile.ScreenYMin, tile.ScreenXMax, tile.ScreenYMax), this.m_RectPaint);
            }
            if (this._OtherDrawCanvas != null) {
                this._OtherDrawCanvas.drawBitmap(localBitmap, new Rect(0, 0, localBitmap.getWidth(), localBitmap.getHeight()), new RectF(tile.ScreenXMin, tile.ScreenYMin, tile.ScreenXMax, tile.ScreenYMax), this._CanvasPaint);
            }
            if (localBitmap != null && !localBitmap.isRecycled()) {
                localBitmap.recycle();
            }
            return true;
        } catch (Exception ex) {
            System.gc();
            Common.Log("ShowImage", "错误:" + ex.toString() + "-->" + ex.getMessage());
            return false;
        }
    }

    public boolean ShowImageWithCRLevel(Map map, int col, int row, int level, byte[] paramArrayOfByte) {
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
            getTileLL(col, row, level, localParam1, localParam2);
            getTileLL(col + 1, row + 1, level, localParam3, localParam4);
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

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public void setOffsetX(double value) {
        this._OffsetX = value;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public void setOffsetY(double value) {
        this._OffsetY = value;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public double getOffsetX() {
        return this._OffsetX;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public double getOffsetY() {
        return this._OffsetY;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public boolean getIsConsiderTranslate() {
        return this._ConsiderTranslate;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public void setIsConsiderTranslate(boolean considerTranslate) {
        this._ConsiderTranslate = considerTranslate;
    }
}
