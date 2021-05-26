package  com.xzy.forestSystem.gogisapi.Display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Map;
import  com.xzy.forestSystem.gogisapi.Edit.EDrawType;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.EGeoDisplayType;
import  com.xzy.forestSystem.gogisapi.Geometry.Point;

public class PointSymbol extends ISymbol {
    static final int SelectedColor = -349389;
    static final int SelectedColor2 = -65536;
    static final int SelectedRecSize = ((int) (6.0f * PubVar.ScaledDensity));
    private Paint _DrawPaint;
    private Bitmap _Icon;

    public PointSymbol() {
        this._Icon = null;
        this._DrawPaint = null;
        this._DrawPaint = new Paint();
        this._DrawPaint.setAntiAlias(true);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void Dispose2() {
        try {
            if (this._SymbolFigure != null && !this._SymbolFigure.isRecycled()) {
                this._SymbolFigure.recycle();
            }
            this._SymbolFigure = null;
            if (this._Icon != null && !this._Icon.isRecycled()) {
                this._Icon.recycle();
            }
            this._Icon = null;
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public Bitmap getSymbolFigure() {
        return this._Icon;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public Bitmap DrawSymbolFigure() {
        return this._Icon;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void Draw(Map paramMap, Canvas paramCanvas, AbstractGeometry paramGeometry, int paramInt1, int paramInt2, EDrawType paramlkDrawType) {
        Point localPoint = (Point) paramGeometry;
        Draw(paramMap, paramCanvas, localPoint.getX(), localPoint.getY(), paramInt1, paramInt2, paramlkDrawType);
    }

    public void Draw(Map paramMap, Canvas paramCanvas, double coordX, double coordY, int paramInt1, int paramInt2, EDrawType paramlkDrawType) {
        int i = paramMap.SetDPI(this._Icon.getWidth()) / 2;
        android.graphics.Point localPoint1 = paramMap.MapToScreen(coordX, coordY);
        int j = localPoint1.x;
        int k = localPoint1.y;
        int l = paramInt1 + (j - i);
        int i1 = paramInt2 + (k - i);
        if (l > -1 && l < PubVar.ScreenWidth && i1 > -1 && i1 < PubVar.ScreenHeight) {
            if (this._Icon != null) {
                paramCanvas.drawBitmap(this._Icon, (float) l, (float) i1, this._DrawPaint);
            }
            if (paramlkDrawType != EDrawType.NONE) {
                Paint localPaint = new Paint();
                localPaint.setColor(SelectedColor);
                int tempDPI = paramMap.SetDPI(SelectedRecSize) / 2;
                int tempDPI2 = tempDPI * 4;
                int i4 = (j + paramInt1) - tempDPI2;
                int i5 = (k + paramInt2) - tempDPI2;
                int i6 = j + paramInt1 + tempDPI2;
                int i7 = k + paramInt2 + tempDPI2;
                paramCanvas.drawRect((float) (i4 - tempDPI), (float) (i5 - tempDPI), (float) (i4 + tempDPI), (float) (i5 + tempDPI), localPaint);
                paramCanvas.drawRect((float) (i4 - tempDPI), (float) (i7 - tempDPI), (float) (i4 + tempDPI), (float) (i7 + tempDPI), localPaint);
                paramCanvas.drawRect((float) (i6 - tempDPI), (float) (i7 - tempDPI), (float) (i6 + tempDPI), (float) (i7 + tempDPI), localPaint);
                paramCanvas.drawRect((float) (i6 - tempDPI), (float) (i5 - tempDPI), (float) (i6 + tempDPI), (float) (i5 + tempDPI), localPaint);
                localPaint.setColor(-65536);
                paramCanvas.drawCircle((float) j, (float) k, (float) SelectedRecSize, localPaint);
            }
        }
    }

    public void Draw(Map paramMap, Canvas paramCanvas, int pointX, int pointY) {
        if (this._Icon != null) {
            paramCanvas.drawBitmap(this._Icon, (float) pointX, (float) pointY, this._DrawPaint);
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void Draw(Map paramMap, AbstractGeometry paramGeometry) {
        Draw(paramMap, paramMap.getDisplayGraphic(), paramGeometry, 0, 0, EDrawType.NONE);
    }

    public void Draw(Map paramMap, double x, double y) {
        Draw(paramMap, paramMap.getDisplayGraphic(), x, y, 0, 0, EDrawType.NONE);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void DrawLabel(Map paramMap, IRender pRender, Canvas paramCanvas, AbstractGeometry paramGeometry, int paramInt1, int paramInt2, EGeoDisplayType paramlkSelectionType) {
        String str = paramGeometry.getLabelContent();
        if (str != null && !str.equals("")) {
            int i = paramMap.SetDPI(this._Icon.getWidth()) / 2;
            Point localPoint = (Point) paramGeometry;
            android.graphics.Point localPoint1 = paramMap.MapToScreen(localPoint.getX(), localPoint.getY());
            float f2 = (float) ((localPoint1.y - i) + 2);
            float f3 = pRender.getTextSymbol().getTextFont().getTextSize() / 2.0f;
            TextSymbol textSymbol = pRender.getTextSymbol();
            textSymbol.Draw(paramCanvas, ((float) ((localPoint1.x + i) - 2)) + f3, f2 + f3, str, ETextPosition.LEFT_ALIGIN, paramlkSelectionType);
        }
    }

    public void DrawLabel(Map paramMap, TextSymbol pTextSymbol, Canvas paramCanvas, AbstractGeometry paramGeometry, int paramInt1, int paramInt2, EGeoDisplayType paramlkSelectionType) {
        String str = paramGeometry.getLabelContent();
        if (str != null && !str.equals("")) {
            int i = paramMap.SetDPI(this._Icon.getWidth()) / 2;
            Point localPoint = (Point) paramGeometry;
            android.graphics.Point localPoint1 = paramMap.MapToScreen(localPoint.getX(), localPoint.getY());
            float f2 = (float) ((localPoint1.y - i) + 2);
            float f3 = pTextSymbol.getTextFont().getTextSize() / 2.0f;
            pTextSymbol.Draw(paramCanvas, ((float) ((localPoint1.x + i) - 2)) + f3, f2 + f3, str, ETextPosition.LEFT_ALIGIN, paramlkSelectionType);
        }
    }

    public Bitmap getIcon() {
        return this._Icon;
    }

    public void setIcon(Bitmap paramBitmap) {
        this._Icon = paramBitmap;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void UpdateTransparent() {
        super.UpdateTransparent();
        if (this._DrawPaint != null) {
            this._DrawPaint.setAlpha(255 - this._Transparent);
        }
    }

    public int getICONWidth() {
        return this._Icon.getWidth();
    }
}
