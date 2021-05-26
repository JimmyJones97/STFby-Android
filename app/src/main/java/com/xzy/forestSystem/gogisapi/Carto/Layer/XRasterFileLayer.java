package  com.xzy.forestSystem.gogisapi.Carto.Layer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.core.internal.view.SupportMenu;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Map;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WGS1984;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.ProjectionCoordinateSystem;
import  com.xzy.forestSystem.gogisapi.Encryption.DataAuthority;
import  com.xzy.forestSystem.gogisapi.Encryption.DatabaseHelper;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XRasterFileLayer extends XLayer {
    public static int InHighLevel = 0;
    public static int MaskBiasXIndex = 1;
    public static int MaskBiasYIndex = 1;
    Paint _CanvasPaint = null;
    float _CenterJX = 117.0f;
    String _CoorSystemName = "WGS1984";
    AbstractC0383CoordinateSystem _CoordinateSystem = null;
    private DataAuthority _DataAuthority = null;
    String _FilePath = "";
    List<Double> _LevelScale = new ArrayList();
    private SQLiteDatabase _MyDatabase = null;
    private boolean _NeedCheckIsBlackTile = false;
    boolean _NeedClearCanvas = true;
    double _OffsetX = 0.0d;
    double _OffsetY = 0.0d;
    protected Canvas _OtherDrawCanvas = null;
    private String _Password = "";
    public double _Scale = 1000.0d;
    protected ERasterLayerType _TileLayerType = ERasterLayerType.NONE;
    protected Canvas _TileScaleCanvas = null;
    float _TileSize = 256.0f;
    int _TotalLevel = 20;
    int extendLength = 256;
    Paint m_RectPaint = null;

    public XRasterFileLayer() {
        this._LayerType = ELayerType.RASTERFILE;
        this._DataAuthority = new DataAuthority(1);
        this._TileLayerType = ERasterLayerType.FromFile;
        this._MinX = 0.0d;
        this._MaxX = 180.0d;
        this._MinY = 0.0d;
        this._MaxY = 180.0d;
        this.extendLength = PubVar.ScreenHeight;
        if (PubVar.ScreenWidth > this.extendLength) {
            this.extendLength = PubVar.ScreenWidth;
        }
        this._CoordinateSystem = new Coordinate_WGS1984();
        this.m_RectPaint = new Paint();
        this.m_RectPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.m_RectPaint.setStrokeWidth(PubVar.ScaledDensity);
        this.m_RectPaint.setStyle(Paint.Style.STROKE);
    }

    public void InitialDataAuthority(String password) {
        BasicValue tmpOutMsg = new BasicValue();
        this._Password = password;
        this._DataAuthority.Initial(this._FilePath, this._Password, tmpOutMsg);
    }

    public boolean IsEnable() {
        if (this._DataAuthority != null) {
            return this._DataAuthority.IsEnable();
        }
        return false;
    }

    public boolean IsEncrypt() {
        if (this._DataAuthority != null) {
            return this._DataAuthority.IsEncrypt();
        }
        return false;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public void setTransparent(int value) {
        this._Transparent = value;
        if (this._Transparent < 0 || this._Transparent > 255) {
            this._Transparent = 255;
        }
        if (this._Transparent == 255) {
            this._CanvasPaint = null;
        } else if (this._CanvasPaint == null) {
            this._CanvasPaint = new Paint();
        }
    }

    public boolean ReadLayer(RasterLayer layer) {
        try {
            if (layer.GetLayerType() == ERasterLayerType.FromFile) {
                this._Name = layer.GetLayerID();
                this._LayerName = layer.GetLayerName();
                this._Visible = layer.GetVisible();
                this._FilePath = layer.GetFilePath();
                setTransparent(layer.GetTransparet());
                this._VisiableScaleMax = layer.GetMaxScale();
                this._VisiableScaleMin = layer.GetMinScale();
                this._OffsetX = layer.getOffsetX();
                this._OffsetY = layer.getOffsetY();
                File tempDBFile = new File(this._FilePath);
                if (tempDBFile.exists()) {
                    if (this._DataAuthority.Initial(this._FilePath, this._Password, new BasicValue())) {
                        this._MyDatabase = new DatabaseHelper(PubVar.MainContext, tempDBFile.getAbsolutePath(), null, 1).getReadableDatabase();
                        if (this._MyDatabase != null) {
                            Cursor pCursor = this._MyDatabase.rawQuery("Select CoorType,CenterJX,TileSize,MaxLevel,Min_X,Min_Y,Max_X,Max_Y,Scale From MapInfo", null);
                            if (pCursor.moveToNext()) {
                                this._CoorSystemName = pCursor.getString(0);
                                this._CenterJX = (float) pCursor.getDouble(1);
                                this._CoordinateSystem = AbstractC0383CoordinateSystem.CreateCoordinateSystem(this._CoorSystemName);
                                if (this._CoordinateSystem instanceof ProjectionCoordinateSystem) {
                                    ((ProjectionCoordinateSystem) this._CoordinateSystem).SetCenterMeridian(this._CenterJX);
                                }
                                this._TileSize = (float) pCursor.getDouble(2);
                                this._TotalLevel = pCursor.getInt(3);
                                this._MinX = pCursor.getDouble(4);
                                this._MinY = pCursor.getDouble(5);
                                this._MaxX = pCursor.getDouble(6);
                                this._MaxY = pCursor.getDouble(7);
                                this._Scale = pCursor.getDouble(8);
                                if (!this._CoordinateSystem.getIsProjectionCoord()) {
                                    Coordinate tempCoord01 = this._CoordinateSystem.ConvertBLToXY(this._MinX, this._MinY);
                                    Coordinate tempCoord02 = this._CoordinateSystem.ConvertBLToXY(this._MaxX, this._MaxY);
                                    this._MinX = tempCoord01.getX();
                                    this._MinY = tempCoord01.getY();
                                    this._MaxX = tempCoord02.getX();
                                    this._MaxY = tempCoord02.getY();
                                }
                                this._LevelScale = GetTileScaleInfo();
                                this._Extend = new Envelope(this._MinX, this._MaxY, this._MaxX, this._MinY);
                                if (this._Extend.getHeight() > 0.0d) {
                                    PubVar._Map.UpdateExtendForView(this._Extend);
                                }
                                pCursor.close();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return false;
    }

    public String getFilePath() {
        return this._FilePath;
    }

    public int GetTotalLevel() {
        return this._TotalLevel;
    }

    public void SetTileType(ERasterLayerType tileLayerType) {
        this._TileLayerType = tileLayerType;
    }

    public ERasterLayerType GetTileType() {
        return this._TileLayerType;
    }

    /* access modifiers changed from: package-private */
    public List<Double> GetTileScaleInfo() {
        List<Double> result = new ArrayList<>();
        try {
            if (this._MyDatabase != null) {
                Cursor pCursor = this._MyDatabase.rawQuery("Select LT_X,LT_Y,RB_X,RB_Y From L1 Where SYS_RC='0-0'", null);
                if (pCursor.moveToNext()) {
                    double tmpLTX = pCursor.getDouble(0);
                    double tmpLTY = pCursor.getDouble(1);
                    double tmpRTX = pCursor.getDouble(2);
                    double tmpRTY = pCursor.getDouble(3);
                    pCursor.close();
                    double tmpWidth = Math.abs(tmpRTX - tmpLTX);
                    double tmpWidth2 = Math.abs(tmpLTY - tmpRTY);
                    if (tmpWidth < tmpWidth2) {
                        tmpWidth = tmpWidth2;
                    }
                    for (int i = 0; i <= this._TotalLevel; i++) {
                        result.add(Double.valueOf(tmpWidth / Math.pow(2.0d, (double) i)));
                    }
                }
            }
            if (result.size() == 0) {
                for (int i2 = 0; i2 <= this._TotalLevel; i2++) {
                    result.add(Double.valueOf(((4.007501668557849E7d / Math.pow(2.0d, (double) i2)) / ((double) this._TileSize)) / this._Scale));
                }
            }
        } catch (Exception e) {
            if (result.size() == 0) {
                for (int i3 = 0; i3 <= this._TotalLevel; i3++) {
                    result.add(Double.valueOf(((4.007501668557849E7d / Math.pow(2.0d, (double) i3)) / ((double) this._TileSize)) / this._Scale));
                }
            }
        } catch (Throwable th) {
            if (result.size() == 0) {
                for (int i4 = 0; i4 <= this._TotalLevel; i4++) {
                    result.add(Double.valueOf(((4.007501668557849E7d / Math.pow(2.0d, (double) i4)) / ((double) this._TileSize)) / this._Scale));
                }
            }
            throw th;
        }
        return result;
    }

    private int GetCurrentLevel(double paramDouble) {
        int result = -1;
        int tempStart = 0;
        double tempD0 = Double.MAX_VALUE;
        for (Double d : this._LevelScale) {
            double tempD = Math.abs(d.doubleValue() - paramDouble);
            if (tempD < tempD0) {
                result = tempStart;
                tempD0 = tempD;
            }
            tempStart++;
        }
        int result2 = result + 1 + InHighLevel;
        if (result2 > this._TotalLevel) {
            return this._TotalLevel;
        }
        return result2;
    }

    private void calRCIndex(double x, double y, int level, BasicValue paramRowIndex, BasicValue paramColIndex) {
        double tempSize = this._LevelScale.get(level - 1).doubleValue();
        int tempRIndex = (int) ((this._MaxY - y) / tempSize);
        int tempCIndex = (int) ((x - this._MinX) / tempSize);
        int tmpMaxCount = (int) Math.pow(2.0d, (double) level);
        if (tempRIndex > tmpMaxCount - 1) {
            tempRIndex = tmpMaxCount - 1;
        }
        if (tempRIndex < 0) {
            tempRIndex = 0;
        }
        if (tempCIndex > tmpMaxCount - 1) {
            tempCIndex = tmpMaxCount - 1;
        }
        paramRowIndex.setValue(tempRIndex);
        paramColIndex.setValue(tempCIndex);
    }

    private boolean calRCIndexs(Coordinate leftTopCoord, Coordinate bottomRightCoord, int level, BasicValue RowIndex01, BasicValue ColIndex01, BasicValue RowIndex02, BasicValue ColIndex02) {
        double tempSize = this._LevelScale.get(level - 1).doubleValue();
        int tempRIndex01 = (int) ((this._MaxY - leftTopCoord.getY()) / tempSize);
        int tempCIndex01 = (int) ((leftTopCoord.getX() - this._MinX) / tempSize);
        int tempRIndex02 = (int) ((this._MaxY - bottomRightCoord.getY()) / tempSize);
        int tempCIndex02 = (int) ((bottomRightCoord.getX() - this._MinX) / tempSize);
        int tmpMaxR = (int) (((this._MaxY - this._MinY) / tempSize) + 1.0d);
        int tmpMaxC = (int) (((this._MaxX - this._MinX) / tempSize) + 1.0d);
        if (tempRIndex01 > tmpMaxR - 1) {
            tempRIndex01 = tmpMaxR - 1;
        }
        if (tempRIndex01 < 0) {
            tempRIndex01 = 0;
        }
        if (tempCIndex01 > tmpMaxC - 1) {
            tempCIndex01 = tmpMaxC - 1;
        }
        if (tempCIndex02 < tempCIndex01 || tempRIndex02 < tempRIndex01) {
            return false;
        }
        if (tempCIndex01 < 0) {
            tempCIndex01 = 0;
        }
        if (tempRIndex01 < 0) {
            tempRIndex01 = 0;
        }
        if (tempCIndex02 > tmpMaxC - 1) {
            tempCIndex02 = tmpMaxC - 1;
        }
        if (tempRIndex02 > tmpMaxR - 1) {
            tempRIndex02 = tmpMaxR - 1;
        }
        RowIndex01.setValue(tempRIndex01);
        ColIndex01.setValue(tempCIndex01);
        RowIndex02.setValue(tempRIndex02 + 1);
        ColIndex02.setValue(tempCIndex02 + 1);
        return true;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public void Dispose() {
        System.gc();
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

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public boolean IsInMapExtend(Map map) {
        Envelope tempExtend = map.getExtend();
        double tempScale = map.getScale();
        if (tempScale < this._VisiableScaleMin || tempScale > this._VisiableScaleMax || this._Extend.getLeftTop().getX() - this._OffsetX > tempExtend.getRightBottom().getX() || this._Extend.getLeftTop().getY() - this._OffsetY < tempExtend.getRightBottom().getY() || this._Extend.getRightBottom().getX() - this._OffsetX < tempExtend.getLeftTop().getX() || this._Extend.getRightBottom().getY() - this._OffsetY > tempExtend.getLeftTop().getY()) {
            return false;
        }
        return true;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer
    public boolean Refresh(Map map) {
        try {
            if (!this._DataAuthority.IsEnable() || !this._DataAuthority.IsEnableRead()) {
                return false;
            }
            if (this._MyDatabase == null || !this._MyDatabase.isOpen()) {
                return false;
            }
            if (this._Transparent == 0 || this._TileLayerType != ERasterLayerType.FromFile || !IsInMapExtend(map)) {
                return false;
            }
            if (this._TileScaleCanvas == null) {
                return false;
            }
            int tempLevel = GetCurrentLevel(map.ToMapDistance((double) this._TileSize));
            if (tempLevel <= 0) {
                tempLevel = 1;
            }
            if (tempLevel > this._TotalLevel) {
                tempLevel = this._TotalLevel;
                map.ToMapDistance((double) this.extendLength);
            }
            Coordinate tmpLeftTopCoord = map.getExtend().getLeftTop().Clone();
            Coordinate tmpRightBotCoord = map.getExtend().getRightBottom().Clone();
            tmpLeftTopCoord.SetOffset(this._OffsetX, this._OffsetY);
            tmpRightBotCoord.SetOffset(this._OffsetX, this._OffsetY);
            Coordinate localCoordinate3 = this._CoordinateSystem.ConvertXYToUnKown(tmpLeftTopCoord.getX(), tmpLeftTopCoord.getY());
            Coordinate localCoordinate4 = this._CoordinateSystem.ConvertXYToUnKown(tmpRightBotCoord.getX(), tmpRightBotCoord.getY());
            BasicValue tempRIndex01 = new BasicValue();
            BasicValue tempCIndex01 = new BasicValue();
            BasicValue tempRIndex02 = new BasicValue();
            BasicValue tempCIndex02 = new BasicValue();
            if (!calRCIndexs(localCoordinate3, localCoordinate4, tempLevel, tempRIndex01, tempCIndex01, tempRIndex02, tempCIndex02)) {
                return true;
            }
            int minX = tempRIndex01.getInt();
            int maxX = tempRIndex02.getInt();
            int minY = tempCIndex01.getInt();
            int maxY = tempCIndex02.getInt();
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
            List<String> tempSYSIDArray = new ArrayList<>();
            for (int i = minX2; i <= maxX2; i++) {
                for (int j = minY2; j <= maxY2; j++) {
                    tempSYSIDArray.add(String.valueOf(String.valueOf(i)) + "-" + String.valueOf(j));
                }
            }
            if (tempSYSIDArray.size() == 0) {
                return true;
            }
            String tempSql = "Select LT_X,LT_Y,RB_X,RB_Y,SYS_GEO From L" + String.valueOf(tempLevel) + " Where SYS_RC IN ('" + Common.CombineStrings("','", tempSYSIDArray) + "')";
            if (this._TileScaleCanvas != null && !this._NeedClearCanvas) {
                this._TileScaleCanvas.drawColor(PubVar.MapBgColor);
            }
            Cursor pCursor = this._MyDatabase.rawQuery(tempSql, null);
            while (pCursor.moveToNext()) {
                double tempX0 = pCursor.getDouble(0);
                double tempY1 = pCursor.getDouble(1);
                double tempX1 = pCursor.getDouble(2);
                double tempY0 = pCursor.getDouble(3);
                byte[] tempBytes = pCursor.getBlob(4);
                if (tempBytes != null) {
                    ShowImage(map, tempX0, tempY1, tempX1, tempY0, tempBytes);
                }
            }
            pCursor.close();
            return true;
        } catch (Exception ex) {
            Common.Log("XRasterLayer-Refresh", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
        return true;
    }

    private void changeBitmapColor(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int color = bmp.getPixel(i, j);
                if (color == 0 || color == -16777216) {
                    bmp.setPixel(i, j, 0);
                }
            }
        }
    }

    private boolean checkIsBlackTile(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        if (w <= 1 || h <= 1) {
            return false;
        }
        return bmp.getPixel(1, 1) == -16777216 && bmp.getPixel(1, h + -1) == -16777216 && bmp.getPixel(w + -1, h + -1) == -16777216 && bmp.getPixel(w + -1, 1) == -16777216 && bmp.getPixel(w / 2, h / 2) == -16777216;
    }

    public boolean ShowImage(Map map, double LeftX, double TopY, double RightX, double BottomY, byte[] paramArrayOfByte) {
        try {
            if (this._TileScaleCanvas == null) {
                return false;
            }
            Coordinate localCoordinate1 = this._CoordinateSystem.ConvertUnKownToXY(LeftX, TopY);
            Coordinate localCoordinate2 = this._CoordinateSystem.ConvertUnKownToXY(RightX, BottomY);
            localCoordinate1.SetOffset(-this._OffsetX, -this._OffsetY);
            localCoordinate2.SetOffset(-this._OffsetX, -this._OffsetY);
            Point tmpTileLeftTop = map.MapToScreen(localCoordinate1);
            Point tmpTileRightBottom = map.MapToScreen(localCoordinate2);
            try {
                float tmpXMin = ((float) tmpTileLeftTop.x) - map.getMaskBiasX();
                float tmpXMax = ((float) tmpTileRightBottom.x) - map.getMaskBiasX();
                float tmpYMin = ((float) tmpTileLeftTop.y) - map.getMaskBiasY();
                float tmpYMax = ((float) tmpTileRightBottom.y) - map.getMaskBiasY();
                if (tmpXMax < 0.0f || tmpXMin > ((float) this._TileScaleCanvas.getWidth()) || tmpYMax < 0.0f || tmpYMin > ((float) this._TileScaleCanvas.getHeight())) {
                    return true;
                }
                Bitmap localBitmap = BitmapFactory.decodeByteArray(paramArrayOfByte, 0, paramArrayOfByte.length).copy(Bitmap.Config.ARGB_8888, true);
                if (this._NeedCheckIsBlackTile && checkIsBlackTile(localBitmap)) {
                    return true;
                }
                if (localBitmap != null) {
                    this._TileScaleCanvas.drawBitmap(localBitmap, new Rect(0, 0, localBitmap.getWidth(), localBitmap.getHeight()), new RectF(tmpXMin, tmpYMin, tmpXMax, tmpYMax), this._CanvasPaint);
                }
                if (PubVar.RasterLayerShowRect) {
                    this._TileScaleCanvas.drawRect(new RectF(tmpXMin, tmpYMin, tmpXMax, tmpYMax), this.m_RectPaint);
                }
                if (this._OtherDrawCanvas != null) {
                    this._OtherDrawCanvas.drawBitmap(localBitmap, new Rect(0, 0, localBitmap.getWidth(), localBitmap.getHeight()), new RectF(tmpXMin, tmpYMin, tmpXMax, tmpYMax), this._CanvasPaint);
                }
                if (localBitmap != null && !localBitmap.isRecycled()) {
                    localBitmap.recycle();
                }
                return true;
            } catch (NullPointerException e) {
                System.gc();
                return false;
            }
        } catch (Exception ex) {
            Common.Log("ShowImage", "错误:" + ex.toString() + "-->" + ex.getMessage());
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        try {
            if (this._MyDatabase != null && this._MyDatabase.isOpen()) {
                this._MyDatabase.close();
            }
        } catch (Exception e) {
        }
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
}
