package  com.xzy.forestSystem.gogisapi.Display;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.view.ViewCompat;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Map;
import  com.xzy.forestSystem.gogisapi.Common.CommonProcess;
import  com.xzy.forestSystem.gogisapi.Edit.EDrawType;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.EGeoDisplayType;
import  com.xzy.forestSystem.gogisapi.MyControls.ColorPickerDialog;
import com.stczh.gzforestSystem.R;
import java.util.Random;

public abstract class ISymbol {
    String _ConfigParas = "";
    private String _Name = "";
    Bitmap _SymbolFigure = null;
    int _Transparent = 0;

    public abstract void Dispose2();

    public abstract void Draw(Map map, Canvas canvas, AbstractGeometry geometry, int i, int i2, EDrawType eDrawType);

    public abstract void Draw(Map map, AbstractGeometry geometry);

    public abstract void DrawLabel(Map map, IRender iRender, Canvas canvas, AbstractGeometry geometry, int i, int i2, EGeoDisplayType eGeoDisplayType);

    public abstract Bitmap DrawSymbolFigure();

    public abstract Bitmap getSymbolFigure();

    public String getName() {
        return this._Name;
    }

    public void setConfigParas(String configParas) {
        this._ConfigParas = configParas;
    }

    public void setName(String paramString) {
        this._Name = paramString;
    }

    public String getConfigParas() {
        return this._ConfigParas;
    }

    public void SetTransparent(int transparent) {
        this._Transparent = transparent;
        UpdateTransparent();
    }

    public void UpdateTransparent() {
    }

    public ISymbol Clone() {
        if (this instanceof PointSymbol) {
            ISymbol result = SymbolManage.GetPointSymbol(this._ConfigParas);
            result.setName(this._Name);
            result.SetTransparent(this._Transparent);
            return result;
        } else if (this instanceof LineSymbol) {
            ISymbol result2 = SymbolManage.GetLineSymbol(this._ConfigParas);
            result2.setName(this._Name);
            result2.SetTransparent(this._Transparent);
            return result2;
        } else if (!(this instanceof PolygonSymbol)) {
            return null;
        } else {
            ISymbol result3 = SymbolManage.GetPolySymbol(this._ConfigParas);
            result3.setName(this._Name);
            result3.SetTransparent(this._Transparent);
            return result3;
        }
    }

    public static ISymbol CreateSymbol(EGeoLayerType _lkGeoLayerType) {
        if (_lkGeoLayerType == EGeoLayerType.POINT) {
            ISymbol result = new PointSymbol();
            ((PointSymbol) result).setIcon(BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.point_symbol_circle48));
            return result;
        } else if (_lkGeoLayerType == EGeoLayerType.POLYLINE) {
            return new LineSymbol();
        } else {
            if (_lkGeoLayerType == EGeoLayerType.POLYGON) {
                return new PolygonSymbol();
            }
            return null;
        }
    }

    public static ISymbol GenerateRandomSymbol(ISymbol symbol) {
        if (symbol instanceof PointSymbol) {
            Bitmap tmpBmp = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888);
            Canvas tmpCanvas = new Canvas(tmpBmp);
            Paint tmpPaint = new Paint();
            int ranColor = -16777216 | new Random().nextInt(ViewCompat.MEASURED_SIZE_MASK);
            tmpPaint.setColor(ranColor);
            tmpPaint.setStyle(Paint.Style.FILL);
            tmpPaint.setAntiAlias(true);
            tmpCanvas.drawCircle(24.0f, 24.0f, 24.0f, tmpPaint);
            ISymbol result = SymbolManage.GetPointSymbol(CommonProcess.BitmapToBase64(tmpBmp));
            result.setName(String.valueOf(symbol._Name) + String.valueOf(ranColor));
            result.SetTransparent(symbol._Transparent);
            return result;
        } else if (symbol instanceof LineSymbol) {
            int ranColor2 = -16777216 | new Random().nextInt(ViewCompat.MEASURED_SIZE_MASK);
            ISymbol result2 = SymbolManage.GetLineSymbol(String.valueOf(ColorPickerDialog.ColorToHex(ranColor2)) + ",3.0");
            result2.setName(String.valueOf(symbol._Name) + String.valueOf(ranColor2));
            result2.SetTransparent(symbol._Transparent);
            return result2;
        } else if (!(symbol instanceof PolygonSymbol)) {
            return null;
        } else {
            Random myRandom = new Random();
            ISymbol result3 = SymbolManage.GetPolySymbol(String.valueOf(ColorPickerDialog.ColorToHex(-16777216 | myRandom.nextInt(ViewCompat.MEASURED_SIZE_MASK))) + "," + ColorPickerDialog.ColorToHex(-16777216 | myRandom.nextInt(ViewCompat.MEASURED_SIZE_MASK)) + ",1.0");
            result3.setName(symbol._Name);
            result3.SetTransparent(symbol._Transparent);
            return result3;
        }
    }
}
