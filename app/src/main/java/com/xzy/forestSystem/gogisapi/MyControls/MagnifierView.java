package  com.xzy.forestSystem.gogisapi.MyControls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import androidx.core.internal.view.SupportMenu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Map;
import com.xzy.forestSystem.gogisapi.Common.Common;
import com.stczh.gzforestSystem.R;

public class MagnifierView extends View {
    private static final int SideWidth = ((int) (PubVar.ScaledDensity * 2.0f));
    public static float ZOOM_FACTOR = 2.0f;
    public int RADIUS = 250;
    private Rect _CanvasRect = new Rect(0, 0, (int) (PubVar.ScaledDensity * 150.0f), (int) (PubVar.ScaledDensity * 150.0f));
    public boolean _IsVisible = false;
    private Map _Map = null;
    private int _MaxHeight = PubVar.ScreenHeight;
    private int _MaxWidth = PubVar.ScreenWidth;
    private int _xDelta;
    private int _yDelta;
    private Bitmap bitmap;
    private int btnInterval = 20;
    Bitmap closeBmp;
    private int closeBmpX = 0;
    private int closeBmpY = 0;
    private Context context = null;
    private Paint crossPaint = null;
    private Path crossPath = null;
    Bitmap dstbmp = null;
    private float lastX = 0.0f;
    private float lastY = 0.0f;
    private Paint mBitmapPaint = null;
    private BitmapShader mBitmapShader;
    Bitmap mode01Bmp;
    Bitmap mode02Bmp;
    private Paint paint = null;
    private int textSize = 0;

    public MagnifierView(Context context2, Map map) {
        super(context2);
        this.context = context2;
        this._Map = map;
        this._MaxWidth = map.getSize().getWidth();
        this._MaxHeight = map.getSize().getHeight();
        setClickable(true);
        setFocusable(true);
        initial();
        setOnTouchListener(new View.OnTouchListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.MagnifierView.1
            @SuppressLint("WrongConstant")
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent event) {
                int X = (int) event.getRawX();
                int Y = (int) event.getRawY();
                switch (event.getAction() & 255) {
                    case 0:
                        RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        MagnifierView.this._xDelta = X - lParams.leftMargin;
                        MagnifierView.this._yDelta = Y - lParams.topMargin;
                        break;
                    case 1:
                        int tempX = (int) event.getX();
                        int tempY = (int) event.getY();
                        if (tempX > MagnifierView.this.closeBmpX) {
                            if (tempY >= MagnifierView.this.closeBmpY) {
                                if (tempY >= (MagnifierView.this.closeBmpY * 2) + MagnifierView.this.btnInterval) {
                                    if (tempY < (MagnifierView.this.closeBmpY * 3) + (MagnifierView.this.btnInterval * 2)) {
                                        MagnifierView.ZOOM_FACTOR -= 0.5f;
                                        if (MagnifierView.ZOOM_FACTOR < 1.0f) {
                                            MagnifierView.ZOOM_FACTOR = 1.0f;
                                            Common.ShowToast("放大倍数不能小于1倍.");
                                        }
                                        MagnifierView.this.SetZoomFactor(MagnifierView.ZOOM_FACTOR);
                                        MagnifierView.this.updateView();
                                        Common.ShowToast("放大倍数为" + MagnifierView.ZOOM_FACTOR + FileSelector_Dialog.sFolder);
                                        break;
                                    }
                                } else {
                                    MagnifierView.ZOOM_FACTOR += 0.5f;
                                    if (MagnifierView.ZOOM_FACTOR > 5.0f) {
                                        MagnifierView.ZOOM_FACTOR = 5.0f;
                                        Common.ShowToast("放大倍数不能超过5倍.");
                                    }
                                    MagnifierView.this.SetZoomFactor(MagnifierView.ZOOM_FACTOR);
                                    MagnifierView.this.updateView();
                                    Common.ShowToast("放大倍数为" + MagnifierView.ZOOM_FACTOR + FileSelector_Dialog.sFolder);
                                    break;
                                }
                            } else {
                                MagnifierView.this.setVisibility(8);
                                MagnifierView.this._IsVisible = false;
                                return false;
                            }
                        }
                        break;
                    case 2:
                        int tmpX = X - MagnifierView.this._xDelta;
                        int tmpY = Y - MagnifierView.this._yDelta;
                        if (tmpX < 0) {
                            tmpX = 0;
                        }
                        if (tmpY < 0) {
                            tmpY = 0;
                        }
                        if (MagnifierView.this.getWidth() + tmpX > MagnifierView.this._MaxWidth) {
                            tmpX = MagnifierView.this._MaxWidth - MagnifierView.this.getWidth();
                        }
                        if (MagnifierView.this.getHeight() + tmpY > MagnifierView.this._MaxHeight) {
                            tmpY = MagnifierView.this._MaxHeight - MagnifierView.this.getHeight();
                        }
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        layoutParams.leftMargin = tmpX;
                        layoutParams.topMargin = tmpY;
                        layoutParams.rightMargin = -MagnifierView.this.RADIUS;
                        layoutParams.bottomMargin = -MagnifierView.this.RADIUS;
                        view.setLayoutParams(layoutParams);
                        MagnifierView.this.updateCoord((float) (MagnifierView.this.RADIUS + tmpX), (float) (MagnifierView.this.RADIUS + tmpY));
                        break;
                }
                return false;
            }
        });
    }

    private void initial() {
        this._CanvasRect = new Rect(0, 0, (int) (((float) PubVar.ScreenWidth) * 0.4f), (int) (((float) PubVar.ScreenWidth) * 0.4f));
        this.RADIUS = this._CanvasRect.width() / 2;
        this.mBitmapPaint = new Paint();
        this.mBitmapPaint.setColor(-7829368);
        this.mBitmapPaint.setAntiAlias(true);
        this.paint = new Paint();
        this.paint.setColor(-7829368);
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeWidth((float) SideWidth);
        this.crossPaint = new Paint();
        this.crossPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.crossPaint.setAntiAlias(true);
        this.crossPaint.setStyle(Paint.Style.STROKE);
        this.crossPaint.setStrokeWidth((float) SideWidth);
        this.textSize = (int) (12.0f * PubVar.ScaledDensity);
        this.crossPaint.setTextSize((float) this.textSize);
        this.crossPath = new Path();
        float tempX = ((float) this.RADIUS) * 0.5f;
        this.crossPath.moveTo(((float) this.RADIUS) - tempX, (float) this.RADIUS);
        this.crossPath.lineTo(((float) this.RADIUS) + tempX, (float) this.RADIUS);
        this.crossPath.moveTo((float) this.RADIUS, ((float) this.RADIUS) - tempX);
        this.crossPath.lineTo((float) this.RADIUS, ((float) this.RADIUS) + tempX);
        this.closeBmp = BitmapFactory.decodeResource(getResources(), R.drawable.close0132);
        this.mode01Bmp = BitmapFactory.decodeResource(getResources(), R.drawable.zoomin32);
        this.mode02Bmp = BitmapFactory.decodeResource(getResources(), R.drawable.zoomout32);
        this.closeBmpX = (this.RADIUS * 2) - this.closeBmp.getWidth();
        this.closeBmpY = this.closeBmp.getHeight();
    }

    public void updateMapView() {
        try {
            PubVar._MapView.setDrawingCacheEnabled(true);
            if (this.bitmap != null && !this.bitmap.isRecycled()) {
                this.bitmap.recycle();
                this.bitmap = null;
            }
            Bitmap bmp = PubVar._MapView.getDrawingCache();
            if (bmp != null) {
                this.bitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_4444);
                new Canvas(this.bitmap).drawBitmap(bmp, 0.0f, 0.0f, (Paint) null);
            }
            try {
                PubVar._MapView.setDrawingCacheEnabled(false);
            } catch (Exception e) {
            }
        } catch (Exception e2) {
            try {
                PubVar._MapView.setDrawingCacheEnabled(false);
            } catch (Exception e3) {
            }
        } catch (Throwable th) {
            try {
                PubVar._MapView.setDrawingCacheEnabled(false);
            } catch (Exception e4) {
            }
            throw th;
        }
    }

    public void updateCoord(float x, float y) {
        if (this.lastX != x || this.lastY != y) {
            this.lastX = x;
            this.lastY = y;
            updateView();
        }
    }

    public void updateView() {
        if (this._IsVisible) {
            try {
                if (this.bitmap != null) {
                    float tmpRadius = ((float) this.RADIUS) / ZOOM_FACTOR;
                    float tmpMinX = this.lastX - tmpRadius;
                    float tmpMinY = this.lastY - tmpRadius;
                    float tmpMaxX = this.lastX + tmpRadius;
                    float tmpMaxY = this.lastY + tmpRadius;
                    if (tmpMinX < 0.0f) {
                        tmpMinX = 0.0f;
                    }
                    if (tmpMinY < 0.0f) {
                        tmpMinY = 0.0f;
                    }
                    if (tmpMinX > ((float) this.bitmap.getWidth())) {
                        tmpMinX = (float) this.bitmap.getWidth();
                    }
                    if (tmpMinY > ((float) this.bitmap.getHeight())) {
                        tmpMinY = (float) this.bitmap.getHeight();
                    }
                    if (tmpMaxX > ((float) this.bitmap.getWidth())) {
                        tmpMaxX = (float) this.bitmap.getWidth();
                    }
                    if (tmpMaxY > ((float) this.bitmap.getHeight())) {
                        tmpMaxY = (float) this.bitmap.getHeight();
                    }
                    if (tmpMinX < tmpMaxX && tmpMinY < tmpMaxY) {
                        if (this.dstbmp != null && !this.dstbmp.isRecycled()) {
                            this.dstbmp.recycle();
                        }
                        this.dstbmp = Bitmap.createScaledBitmap(Bitmap.createBitmap(this.bitmap, (int) tmpMinX, (int) tmpMinY, (int) (tmpMaxX - tmpMinX), (int) (tmpMaxY - tmpMinY)), this.RADIUS * 2, this.RADIUS * 2, false);
                    }
                }
            } catch (Exception ex) {
                Common.Log("updateView", "错误:" + ex.getMessage());
            }
            invalidate();
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap2) {
        Bitmap output = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);
        Rect rect = new Rect(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
        Paint tmpPaint = new Paint();
        tmpPaint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        tmpPaint.setColor(-12434878);
        int x = bitmap2.getWidth();
        canvas.drawCircle((float) (x / 2), (float) (x / 2), (float) (x / 2), tmpPaint);
        tmpPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap2, rect, rect, tmpPaint);
        return output;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        if (this._IsVisible) {
            super.onDraw(canvas);
            try {
                if (this.bitmap != null) {
                    Bitmap b = getCircleBitmap(this.dstbmp);
                    canvas.drawBitmap(b, new Rect(0, 0, b.getWidth(), b.getHeight()), this._CanvasRect, (Paint) null);
                    canvas.drawPath(this.crossPath, this.crossPaint);
                    canvas.drawCircle((float) this.RADIUS, (float) this.RADIUS, (float) (this.RADIUS - (SideWidth / 2)), this.paint);
                    canvas.drawBitmap(this.closeBmp, (float) this.closeBmpX, 0.0f, this.paint);
                    canvas.drawBitmap(this.mode01Bmp, (float) this.closeBmpX, (float) (this.closeBmpY + this.btnInterval), this.paint);
                    canvas.drawBitmap(this.mode02Bmp, (float) this.closeBmpX, (float) ((this.closeBmpY + this.btnInterval) * 2), this.paint);
                }
            } catch (Exception e) {
            }
        }
    }

    public void SetZoomFactor(float zoomFactor) {
        ZOOM_FACTOR = zoomFactor;
        this.RADIUS = getWidth() / 2;
    }
}
