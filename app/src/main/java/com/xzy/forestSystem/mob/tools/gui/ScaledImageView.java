package  com.xzy.forestSystem.mob.tools.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.utils.BitmapHelper;

public class ScaledImageView extends ImageView implements View.OnTouchListener {
    private static final int DRAG_1 = 1;
    private static final int DRAG_2 = 2;
    private static final int NONE = 0;
    private static final int ZOOM = 3;
    private Bitmap bitmap;
    private float distSquare;
    private float[] downPoint;
    private int dragScrollMinDistSquare;
    private OnMatrixChangedListener listener;
    private Matrix matrix;
    private int mode;
    private Matrix savedMatrix;

    public interface OnMatrixChangedListener {
        void onMactrixChage(Matrix matrix);
    }

    public ScaledImageView(Context context) {
        super(context);
        init(context);
    }

    public ScaledImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public ScaledImageView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.dragScrollMinDistSquare = ViewConfiguration.get(context).getScaledTouchSlop();
        this.dragScrollMinDistSquare *= this.dragScrollMinDistSquare;
        setOnTouchListener(this);
    }

    public void setBitmap(Bitmap bm) {
        this.bitmap = bm;
        setImageBitmap(bm);
        int[] target = {getWidth(), getHeight()};
        int[] src = {this.bitmap.getWidth(), this.bitmap.getHeight()};
        int[] dst = BitmapHelper.fixRect(src, target);
        int[] centerDel = {(target[0] - dst[0]) / 2, (target[1] - dst[1]) / 2};
        float[] factor = {((float) dst[0]) / ((float) src[0]), ((float) dst[1]) / ((float) src[1])};
        this.matrix = new Matrix();
        this.matrix.set(getImageMatrix());
        this.matrix.postScale(factor[0], factor[1]);
        this.matrix.postTranslate((float) centerDel[0], (float) centerDel[1]);
        if (this.listener != null) {
            this.listener.onMactrixChage(this.matrix);
        }
        setImageMatrix(this.matrix);
    }

    public void setOnMatrixChangedListener(OnMatrixChangedListener l) {
        this.listener = l;
        if (this.matrix != null) {
            if (this.listener != null) {
                this.listener.onMactrixChage(this.matrix);
            }
            setImageMatrix(this.matrix);
        }
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View v, MotionEvent event) {
        try {
            switch (event.getAction()) {
                case 0:
                    this.matrix = new Matrix();
                    this.matrix.set(getImageMatrix());
                    this.savedMatrix = new Matrix();
                    this.savedMatrix.set(this.matrix);
                    this.downPoint = new float[]{event.getX(0), event.getY(0)};
                    this.mode = 1;
                    break;
                case 1:
                    if (this.listener != null) {
                        this.listener.onMactrixChage(this.matrix);
                    }
                    float dx = event.getX(0) - this.downPoint[0];
                    float dy = event.getY(0) - this.downPoint[1];
                    if (this.mode == 1 && (dx * dx) + (dy * dy) <= ((float) this.dragScrollMinDistSquare)) {
                        performClick();
                    }
                    this.mode = 0;
                    break;
                case 2:
                    if (this.mode != 1) {
                        if (this.mode != 2) {
                            if (this.mode == 3) {
                                float[] start = {event.getX(0), event.getY(0)};
                                float[] end = {event.getX(1), event.getY(1)};
                                float dx2 = start[0] - end[0];
                                float dy2 = start[1] - end[1];
                                this.matrix.set(this.savedMatrix);
                                float scale = (float) Math.sqrt((double) (((dx2 * dx2) + (dy2 * dy2)) / this.distSquare));
                                float[] middle = {(start[0] + end[0]) / 2.0f, (start[1] + end[1]) / 2.0f};
                                this.matrix.postScale(scale, scale, middle[0], middle[1]);
                                break;
                            }
                        } else {
                            float[] end2 = {event.getX(1), event.getY(1)};
                            this.matrix.set(this.savedMatrix);
                            this.matrix.postTranslate(end2[0] - this.downPoint[0], end2[1] - this.downPoint[1]);
                            break;
                        }
                    } else {
                        float[] end3 = {event.getX(0), event.getY(0)};
                        this.matrix.set(this.savedMatrix);
                        this.matrix.postTranslate(end3[0] - this.downPoint[0], end3[1] - this.downPoint[1]);
                        break;
                    }
                    break;
                case 5:
                    float[] start2 = {event.getX(0), event.getY(0)};
                    float[] end4 = {event.getX(1), event.getY(1)};
                    float dx3 = start2[0] - end4[0];
                    float dy3 = start2[1] - end4[1];
                    this.distSquare = (dx3 * dx3) + (dy3 * dy3);
                    this.mode = 3;
                    break;
                case 6:
                    this.downPoint = new float[]{event.getX(1), event.getY(1)};
                    this.savedMatrix.set(this.matrix);
                    this.mode = 2;
                    break;
                case 261:
                    float[] start3 = {event.getX(0), event.getY(0)};
                    float[] end5 = {event.getX(1), event.getY(1)};
                    float dx4 = start3[0] - end5[0];
                    float dy4 = start3[1] - end5[1];
                    this.distSquare = (dx4 * dx4) + (dy4 * dy4);
                    this.mode = 3;
                    break;
                case 262:
                    this.downPoint = new float[]{event.getX(0), event.getY(0)};
                    this.savedMatrix.set(this.matrix);
                    this.mode = 1;
                    break;
                default:
                    return false;
            }
            setImageMatrix(this.matrix);
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
        }
        return true;
    }

    public void rotateLeft() {
        try {
            this.matrix = new Matrix();
            float[] matrixValue = {0.0f, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
            this.matrix.setValues(matrixValue);
            Bitmap resizedBitmap = Bitmap.createBitmap(this.bitmap, 0, 0, this.bitmap.getWidth(), this.bitmap.getHeight(), this.matrix, true);
            if (resizedBitmap != null && !resizedBitmap.isRecycled()) {
                this.bitmap.recycle();
                this.bitmap = resizedBitmap;
            }
            setImageBitmap(this.bitmap);
            this.matrix = new Matrix();
            this.matrix.set(getImageMatrix());
            this.matrix.getValues(matrixValue);
            int[] target = {getWidth(), getHeight()};
            float[] src = {matrixValue[0] * ((float) this.bitmap.getWidth()), matrixValue[4] * ((float) this.bitmap.getHeight())};
            float[] centerDel = {(((float) target[0]) - src[0]) / 2.0f, (((float) target[1]) - src[1]) / 2.0f};
            matrixValue[2] = centerDel[0];
            matrixValue[5] = centerDel[1];
            this.matrix.setValues(matrixValue);
            if (this.listener != null) {
                this.listener.onMactrixChage(this.matrix);
            }
            setImageMatrix(this.matrix);
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
        }
    }

    public void rotateRight() {
        try {
            this.matrix = new Matrix();
            float[] matrixValue = {0.0f, -1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f};
            this.matrix.setValues(matrixValue);
            Bitmap resizedBitmap = Bitmap.createBitmap(this.bitmap, 0, 0, this.bitmap.getWidth(), this.bitmap.getHeight(), this.matrix, true);
            if (resizedBitmap != null && !resizedBitmap.isRecycled()) {
                this.bitmap.recycle();
                this.bitmap = resizedBitmap;
            }
            setImageBitmap(this.bitmap);
            this.matrix = new Matrix();
            this.matrix.set(getImageMatrix());
            this.matrix.getValues(matrixValue);
            int[] target = {getWidth(), getHeight()};
            float[] src = {matrixValue[0] * ((float) this.bitmap.getWidth()), matrixValue[4] * ((float) this.bitmap.getHeight())};
            float[] centerDel = {(((float) target[0]) - src[0]) / 2.0f, (((float) target[1]) - src[1]) / 2.0f};
            matrixValue[2] = centerDel[0];
            matrixValue[5] = centerDel[1];
            this.matrix.setValues(matrixValue);
            if (this.listener != null) {
                this.listener.onMactrixChage(this.matrix);
            }
            setImageMatrix(this.matrix);
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
        }
    }

    public void zoomIn() {
        this.matrix = new Matrix();
        this.matrix.set(getImageMatrix());
        this.matrix.postScale(1.072f, 1.072f);
        if (this.listener != null) {
            this.listener.onMactrixChage(this.matrix);
        }
        setImageMatrix(this.matrix);
    }

    public void zoomOut() {
        this.matrix = new Matrix();
        this.matrix.set(getImageMatrix());
        this.matrix.postScale(0.933f, 0.933f);
        if (this.listener != null) {
            this.listener.onMactrixChage(this.matrix);
        }
        setImageMatrix(this.matrix);
    }

    public Bitmap getCropedBitmap(Rect cropRect) {
        try {
            Bitmap bmTmp = BitmapHelper.captureView(this, getWidth(), getHeight());
            if (bmTmp == null) {
                MobLog.getInstance().m58w("ivPhoto.getDrawingCache() returns null", new Object[0]);
                return null;
            }
            Bitmap createBitmap = Bitmap.createBitmap(bmTmp, cropRect.left, cropRect.top, cropRect.width(), cropRect.height());
            bmTmp.recycle();
            return createBitmap;
        } catch (Throwable e) {
            MobLog.getInstance().m57w(e);
            return null;
        }
    }
}
