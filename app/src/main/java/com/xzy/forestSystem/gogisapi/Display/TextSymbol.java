package  com.xzy.forestSystem.gogisapi.Display;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import  com.xzy.forestSystem.gogisapi.Geometry.EGeoDisplayType;

public class TextSymbol {
    private Paint _BKFont = null;
    private Paint _Font = null;

    private Paint getBKFont() {
        if (this._BKFont == null) {
            this._BKFont = new Paint();
            this._BKFont.setColor(-1);
            this._BKFont.setAntiAlias(true);
            this._BKFont.setTextSize(20.0f * PubVar.ScaledDensity);
            this._BKFont.setTypeface(Typeface.create("宋体", 0));
            this._BKFont.setAntiAlias(true);
        }
        this._BKFont.setTextSize(getTextFont().getTextSize());
        return this._BKFont;
    }

    public void Draw(Canvas paramCanvas, float TextX, float TextY, String Text, ETextPosition lkTP, EGeoDisplayType pSelectionType) {
        Draw(paramCanvas, 0.0f, 0.0f, TextX, TextY, Text, "", lkTP, pSelectionType, false);
    }

    public void Draw(Canvas paramCanvas, float PointX, float PointY, float TextX, float TextY, String text, String UID, ETextPosition paramlkTextPosition, EGeoDisplayType paramlkSelectionType, boolean AutoAdjust) {
        if (text != null) {
            if (paramlkTextPosition == ETextPosition.CENTER_ALIGIN) {
                TextX -= getTextFont().measureText(text) / 2.0f;
            }
            if (AutoAdjust) {
                PubVar._PubCommand.TextPositionAdjustT().AdjustPosition(new RectangleF(TextX, TextY, getTextFont().measureText(text), getTextFont().getTextSize()), UID, PointX, PointY, new BasicValue(), new BasicValue());
            }
            DrawBorderText(paramCanvas, TextX, TextY, text);
            paramCanvas.drawText(text, TextX, TextY, getTextFont());
        }
    }

    private void DrawBorderText(Canvas canvas, float TextX, float TextY, String Text) {
        canvas.drawText(Text, TextX - 1.0f, TextY, getBKFont());
        canvas.drawText(Text, TextX + 1.0f, TextY, getBKFont());
        canvas.drawText(Text, TextX, TextY - 1.0f, getBKFont());
        canvas.drawText(Text, TextX, TextY + 1.0f, getBKFont());
        canvas.drawText(Text, TextX + 1.0f, TextY + 1.0f, getBKFont());
        canvas.drawText(Text, TextX - 1.0f, TextY - 1.0f, getBKFont());
        canvas.drawText(Text, TextX + 1.0f, TextY - 1.0f, getBKFont());
        canvas.drawText(Text, TextX - 1.0f, TextY + 1.0f, getBKFont());
    }

    public int getColor() {
        return getTextFont().getColor();
    }

    public Paint getTextFont() {
        if (this._Font == null) {
            this._Font = new Paint();
            this._Font.setAntiAlias(true);
            this._Font.setTextSize(20.0f * PubVar.ScaledDensity);
            this._Font.setTypeface(Typeface.create("宋体", 0));
        }
        this._Font.setAntiAlias(true);
        return this._Font;
    }

    public void setColor(int paramInt) {
        getTextFont().setColor(paramInt);
    }

    public void setTextFont(Paint paramPaint) {
        this._Font = paramPaint;
    }

    public void SetTextSymbolFont(String labelFont) {
        String[] tempStrs = labelFont.split(",");
        if (tempStrs != null && tempStrs.length > 1) {
            int tmpColor = Color.parseColor(tempStrs[0]);
            float tmpSize = Float.parseFloat(tempStrs[1]) * PubVar.ScaledDensity;
            this._Font = new Paint();
            this._Font.setAntiAlias(true);
            this._Font.setTextSize(tmpSize);
            this._Font.setTypeface(Typeface.create("宋体", 0));
            this._Font.setColor(tmpColor);
        }
    }
}
