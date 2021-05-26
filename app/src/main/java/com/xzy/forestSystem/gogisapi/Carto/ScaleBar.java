package  com.xzy.forestSystem.gogisapi.Carto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import androidx.core.view.ViewCompat;
import android.widget.ImageView;
import com.xzy.forestSystem.baidu.speech.easr.easrNativeJni;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;

public class ScaleBar {
    static final int LineColor = -16777216;
    static final float LineWLWidth = PubVar.ScaledDensity;
    static final float LineWidth = (2.0f * PubVar.ScaledDensity);
    static final int TextColor = -16777216;
    static final int TextSize = 14;
    static final float textBias = 1.0f;
    private Paint _BKFont = null;
    private ImageView m_BindImageView = null;
    private Paint m_LinePen = null;
    private Paint m_TextPen = null;
    private Bitmap m_bp = null;
    private Canvas m_g = null;

    private void DrawBar(float paramFloat1, float paramFloat2, String paramString) {
        try {
            String str1 = String.valueOf(paramFloat2);
            if (str1.substring(str1.length() - 2, str1.length()).equals(".0")) {
                str1 = str1.substring(0, str1.length() - 2);
            }
            float tempW = (((float) this.m_g.getWidth()) * paramFloat2) / paramFloat1;
            if (tempW > ((float) this.m_g.getWidth())) {
                tempW = (float) this.m_g.getWidth();
            }
            float tempH = ((float) this.m_g.getHeight()) * 0.8f;
            float tempBaseY = ((float) this.m_g.getHeight()) * 0.2f;
            float tempLYBias = 6.0f * PubVar.ScaledDensity;
            float tempLineWidth = LineWidth + (LineWLWidth * 2.0f);
            float tempLineWidth2 = tempLineWidth / 2.0f;
            this.m_LinePen.setColor(-1);
            this.m_LinePen.setStrokeWidth(tempLineWidth);
            this.m_g.drawLine(0.0f, (tempH - tempLineWidth2) + tempBaseY, tempW, (tempH - tempLineWidth2) + tempBaseY, this.m_LinePen);
            this.m_g.drawLine(tempLineWidth2, tempLYBias + tempBaseY, tempLineWidth2, tempH + tempBaseY, this.m_LinePen);
            this.m_g.drawLine(tempW - tempLineWidth2, tempLYBias + tempBaseY, tempW - tempLineWidth2, tempH + tempBaseY, this.m_LinePen);
            this.m_LinePen.setColor(ViewCompat.MEASURED_STATE_MASK);
            this.m_LinePen.setStrokeWidth(LineWidth);
            float tempSideX = (LineWidth / 2.0f) + LineWLWidth;
            this.m_g.drawLine(LineWLWidth, (tempH - tempSideX) + tempBaseY, tempW - LineWLWidth, (tempH - tempSideX) + tempBaseY, this.m_LinePen);
            this.m_g.drawLine(tempSideX, LineWLWidth + tempLYBias + tempBaseY, tempSideX, (tempH - tempSideX) + tempBaseY, this.m_LinePen);
            this.m_g.drawLine(tempW - tempSideX, LineWLWidth + tempLYBias + tempBaseY, tempW - tempSideX, (tempH - tempSideX) + tempBaseY, this.m_LinePen);
            drawTextContent(String.valueOf(str1) + paramString, (tempW - this.m_TextPen.measureText(String.valueOf(str1) + paramString)) / 2.0f, ((float) this.m_g.getHeight()) - (8.0f * PubVar.ScaledDensity));
        } catch (Exception ex) {
            Common.Log("DrawBar", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    private void drawTextContent(String content, float x, float y) {
        this.m_g.drawText(content, x - 1.0f, y, getBKFont());
        this.m_g.drawText(content, 1.0f + x + 1.0f, y, getBKFont());
        this.m_g.drawText(content, x, y - 1.0f, getBKFont());
        this.m_g.drawText(content, x, 1.0f + y + 1.0f, getBKFont());
        this.m_g.drawText(content, 1.0f + x, 1.0f + y, getBKFont());
        this.m_g.drawText(content, x - 1.0f, y - 1.0f, getBKFont());
        this.m_g.drawText(content, 1.0f + x, y - 1.0f, getBKFont());
        this.m_g.drawText(content, x - 1.0f, 1.0f + y, getBKFont());
        this.m_g.drawText(content, x, y, this.m_TextPen);
    }

    private Paint getBKFont() {
        if (this._BKFont == null) {
            this._BKFont = new Paint();
            this._BKFont.setColor(-1);
            this._BKFont.setAntiAlias(true);
            this._BKFont.setTypeface(Typeface.create("宋体", 0));
            this._BKFont.setAntiAlias(true);
        }
        this._BKFont.setTextSize(this.m_TextPen.getTextSize());
        return this._BKFont;
    }

    public void RefreshScalBar(Map paramMap) {
        String tempShowStr;
        String tempShowStr2;
        if (PubVar.ScaleBar_Visible) {
            try {
                this.m_g.drawColor(-1);
                this.m_bp.eraseColor(0);
                if (PubVar.ScaleBar_ShowType == 0) {
                    String tempUnit = "米";
                    double d = 1.0d;
                    if (paramMap != null) {
                        d = paramMap.getZoomScale();
                    }
                    int i = (int) (((double) this.m_g.getWidth()) * d);
                    if (i == 0) {
                        float f = (float) Common.Save3Point(((double) this.m_g.getWidth()) * d);
                        DrawBar(f, f, tempUnit);
                        return;
                    }
                    String.valueOf(i);
                    if (i >= 1000) {
                        i /= easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX;
                        tempUnit = "公里";
                    }
                    String str2 = String.valueOf(i);
                    if (str2.length() == 1) {
                        DrawBar((float) i, (float) i, tempUnit);
                    } else if (str2.length() == 2) {
                        int j = Integer.parseInt(str2.substring(1, 2));
                        String tempShowStr3 = str2.substring(0, 1);
                        if (j > 5) {
                            tempShowStr2 = String.valueOf(tempShowStr3) + "5";
                        } else {
                            tempShowStr2 = String.valueOf(tempShowStr3) + "5";
                        }
                        DrawBar((float) i, (float) Integer.parseInt(tempShowStr2), tempUnit);
                    } else if (str2.length() == 3) {
                        int j2 = Integer.parseInt(str2.substring(str2.length() - 2, str2.length()));
                        String tempShowStr4 = str2.substring(0, 1);
                        if (j2 < 50) {
                            tempShowStr = String.valueOf(tempShowStr4) + "00";
                        } else {
                            tempShowStr = String.valueOf(tempShowStr4) + "50";
                        }
                        DrawBar((float) i, (float) Integer.parseInt(tempShowStr), tempUnit);
                    } else {
                        DrawBar((float) i, (float) Integer.parseInt(str2), tempUnit);
                    }
                } else {
                    int tempScale = paramMap.getActualScale();
                    if (tempScale < 1) {
                        tempScale = 1;
                    }
                    drawTextContent("1:" + String.valueOf(tempScale), 0.0f, ((float) this.m_g.getHeight()) - (7.0f * PubVar.ScaledDensity));
                }
            } catch (Exception ex) {
                Common.Log("RefreshScalBar", "错误:" + ex.toString() + "-->" + ex.getMessage());
            }
        }
    }

    public void Refresh() {
        if (PubVar.ScaleBar_Visible && this.m_BindImageView != null) {
            this.m_BindImageView.invalidate();
        }
    }

    public void SetImageView(ImageView paramImageView) {
        try {
            int tmpWidth = (int) (80.0f * PubVar.ScaledDensity);
            int tmpHeight = (int) (25.0f * PubVar.ScaledDensity);
            Paint pFont = new Paint();
            pFont.setTextSize(14.0f);
            Rect rect = new Rect();
            pFont.getTextBounds("1:1000000000", 0, 1, rect);
            if (tmpWidth < rect.width()) {
                tmpWidth = rect.width();
            }
            this.m_BindImageView = paramImageView;
            this.m_bp = Bitmap.createBitmap(tmpWidth, tmpHeight, Bitmap.Config.ARGB_4444);
            this.m_g = new Canvas(this.m_bp);
            paramImageView.setImageBitmap(this.m_bp);
            this.m_TextPen = new Paint();
            this.m_TextPen.setColor(ViewCompat.MEASURED_STATE_MASK);
            this.m_TextPen.setAntiAlias(true);
            this.m_TextPen.setTypeface(Typeface.create("宋体", 0));
            this.m_TextPen.setTextSize(PubVar.ScaledDensity * 14.0f);
            this.m_LinePen = new Paint();
            this.m_LinePen.setColor(ViewCompat.MEASURED_STATE_MASK);
            this.m_LinePen.setStyle(Paint.Style.STROKE);
            this.m_LinePen.setAntiAlias(true);
            this.m_LinePen.setStrokeWidth(LineWidth);
        } catch (Exception ex) {
            Common.Log("SetImageView", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }
}
