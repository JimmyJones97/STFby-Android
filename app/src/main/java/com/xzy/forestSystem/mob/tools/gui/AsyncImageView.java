package  com.xzy.forestSystem.mob.tools.gui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.core.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xzy.forestSystem.hellocharts.animation.ChartViewportAnimator;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.gui.BitmapProcessor;
import  com.xzy.forestSystem.mob.tools.utils.BitmapHelper;
import  com.xzy.forestSystem.mob.tools.utils.UIHandler;
import java.lang.ref.WeakReference;
import java.util.Random;

@SuppressLint("AppCompatCustomView")
public class AsyncImageView extends ImageView implements BitmapProcessor.BitmapCallback, Handler.Callback {
    private static final int MSG_IMG_GOT = 1;
    private static final Random RND = new Random();
    private Bitmap defaultBm;
    private int defaultRes;
    private int desiredHeight = 0;
    private int desiredWidth = 0;
    private long diskCacheTime = 0;
    private Bitmap errorBm = null;
    private int errorRes = 0;
    private boolean lastReqIsOk;
    private long maxBytes = 0;
    private Path path;
    private int quality = 0;
    private float[] rect;
    private WeakReference<AsyncImageView> refAiv = null;
    private Bitmap result;
    private boolean scaleToCrop;
    private String url;
    private boolean useDiskCache = true;
    private boolean useRamCache = true;

    public AsyncImageView(Context context) {
        super(context);
        init(context);
    }

    public AsyncImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AsyncImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        if (isInEditMode()) {
            setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        } else {
            BitmapProcessor.prepare(context);
        }
    }

    public void setRound(float radius) {
        setRound(radius, radius, radius, radius);
    }

    public void setRound(float leftTop, float rightTop, float rightBottom, float leftBottom) {
        this.rect = new float[]{leftTop, leftTop, rightTop, rightTop, rightBottom, rightBottom, leftBottom, leftBottom};
    }

    public void setScaleToCropCenter(boolean scaleToCrop2) {
        this.scaleToCrop = scaleToCrop2;
    }

    public void setCompressOptions(int desiredWidth2, int desiredHeight2, int quality2, long maxBytes2) {
        this.desiredWidth = desiredWidth2;
        this.desiredHeight = desiredHeight2;
        this.quality = quality2;
        this.maxBytes = maxBytes2;
    }

    public void setUseCacheOption(boolean useRamCache2, boolean useDiskCache2) {
        setUseCacheOption(useRamCache2, useDiskCache2, 0);
    }

    public void setUseCacheOption(boolean useRamCache2, boolean useDiskCache2, long diskCacheTime2) {
        this.useRamCache = useRamCache2;
        this.useDiskCache = useDiskCache2;
        if (useDiskCache2) {
            this.diskCacheTime = diskCacheTime2;
        }
    }

    public void removeRamCache(String url2) {
        if (this.useRamCache) {
            BitmapProcessor.removeBitmapFromRamCache(url2, getBitmapDesiredOptions());
        }
    }

    public void deleteCachedFile(String url2) {
        BitmapProcessor.deleteCachedFile(url2, getBitmapDesiredOptions());
    }

    public void execute(String url2, int defaultRes2) {
        execute(url2, defaultRes2, 0);
    }

    public void execute(String url2, int defaultRes2, int errorRes2) {
        Bitmap bm;
        if ((!this.useRamCache && !this.useDiskCache) || !this.lastReqIsOk || TextUtils.isEmpty(url2) || !url2.equals(this.url)) {
            this.lastReqIsOk = false;
            this.url = url2;
            this.result = null;
            this.defaultRes = defaultRes2;
            this.errorRes = errorRes2;
            if (TextUtils.isEmpty(url2)) {
                if (errorRes2 != 0) {
                    defaultRes2 = errorRes2;
                }
                setImageResource(defaultRes2);
                return;
            }
            BitmapProcessor.BitmapDesiredOptions options = getBitmapDesiredOptions();
            if (!this.useRamCache || (bm = BitmapProcessor.getBitmapFromCache(url2, options)) == null || bm.isRecycled()) {
                if (defaultRes2 > 0) {
                    setImageResource(defaultRes2);
                }
                if (this.refAiv == null || this.refAiv.get() == null) {
                    this.refAiv = new WeakReference<>(this);
                }
                BitmapProcessor.process(url2, options, this.useRamCache, this.useDiskCache, this.diskCacheTime, this);
                return;
            }
            setBitmap(bm);
            this.lastReqIsOk = true;
        }
    }

    public void execute(String url2, Bitmap defaultBm2) {
        execute(url2, defaultBm2, (Bitmap) null);
    }

    public void execute(String url2, Bitmap defaultBm2, Bitmap errorBm2) {
        Bitmap bm;
        if ((!this.useRamCache && !this.useDiskCache) || !this.lastReqIsOk || TextUtils.isEmpty(url2) || !url2.equals(this.url)) {
            this.lastReqIsOk = false;
            this.url = url2;
            this.result = null;
            this.defaultBm = defaultBm2;
            this.errorBm = errorBm2;
            if (TextUtils.isEmpty(url2)) {
                if (errorBm2 != null) {
                    defaultBm2 = errorBm2;
                }
                setImageBitmap(defaultBm2);
                return;
            }
            BitmapProcessor.BitmapDesiredOptions options = getBitmapDesiredOptions();
            if (!this.useRamCache || (bm = BitmapProcessor.getBitmapFromCache(url2, options)) == null || bm.isRecycled()) {
                if (defaultBm2 != null && !defaultBm2.isRecycled()) {
                    setImageBitmap(defaultBm2);
                }
                if (this.refAiv == null || this.refAiv.get() == null) {
                    this.refAiv = new WeakReference<>(this);
                }
                BitmapProcessor.process(url2, options, this.useRamCache, this.useDiskCache, this.diskCacheTime, this);
                return;
            }
            setBitmap(bm);
            this.lastReqIsOk = true;
        }
    }

    private BitmapProcessor.BitmapDesiredOptions getBitmapDesiredOptions() {
        if ((this.desiredWidth <= 1 || this.desiredHeight <= 1) && this.maxBytes < 10240 && this.quality <= 0) {
            return null;
        }
        BitmapProcessor.BitmapDesiredOptions options = new BitmapProcessor.BitmapDesiredOptions();
        options.desiredWidth = this.desiredWidth;
        options.desiredHeight = this.desiredHeight;
        options.maxBytes = this.maxBytes;
        options.quality = this.quality;
        return options;
    }

    public void setBitmap(Bitmap bm) {
        if (this.scaleToCrop) {
            bm = goCrop(bm);
        }
        setImageBitmap(bm);
        this.result = bm;
    }

    @Override //  com.xzy.forestSystem.mob.tools.gui.BitmapProcessor.BitmapCallback
    public void onImageGot(String url2, Bitmap bm) {
        if (this.refAiv != null && this.refAiv.get() != null) {
            Bitmap shownImage = null;
            if (url2 != null && url2.trim().length() > 0 && url2.equals(this.url)) {
                shownImage = bm;
            }
            if (shownImage != null && this.scaleToCrop) {
                shownImage = goCrop(shownImage);
            }
            Message msg = new Message();
            msg.what = 1;
            msg.obj = new Object[]{url2, shownImage};
            UIHandler.sendMessageDelayed(msg, (long) RND.nextInt(ChartViewportAnimator.FAST_ANIMATION_DURATION), this);
        }
    }

    private Bitmap goCrop(Bitmap bm) {
        float width = (float) bm.getWidth();
        float height = (float) bm.getHeight();
        if (width == 0.0f || height == 0.0f) {
            return bm;
        }
        int[] size = getSize();
        if (size[0] == 0 || size[1] == 0) {
            return bm;
        }
        float respHeight = (((float) size[1]) * width) / ((float) size[0]);
        if (respHeight == height) {
            return bm;
        }
        int[] rect2 = new int[4];
        if (respHeight < height) {
            rect2[1] = (int) ((height - respHeight) / 2.0f);
            rect2[3] = rect2[1];
        } else {
            rect2[0] = (int) ((width - ((((float) size[0]) * height) / ((float) size[1]))) / 2.0f);
            rect2[2] = rect2[0];
        }
        try {
            bm = BitmapHelper.cropBitmap(bm, rect2[0], rect2[1], rect2[2], rect2[3]);
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
        }
        return bm;
    }

    private int[] getSize() {
        ViewGroup.LayoutParams lp;
        int width = getWidth();
        int height = getHeight();
        if ((width == 0 || height == 0) && (lp = getLayoutParams()) != null) {
            width = lp.width;
            height = lp.height;
        }
        if (width == 0 || height == 0) {
            measure(0, 0);
            width = getMeasuredWidth();
            height = getMeasuredHeight();
        }
        return new int[]{width, height};
    }

    @Override // android.view.View
    public void setPadding(int left, int top, int right, int bottom) {
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message msg) {
        if (!(msg.what != 1 || this.refAiv == null || this.refAiv.get() == null)) {
            try {
                Object url2 = ((Object[]) msg.obj)[0];
                Object bm = ((Object[]) msg.obj)[1];
                if (bm != null && url2 != null && url2.equals(this.url)) {
                    this.result = (Bitmap) bm;
                    this.refAiv.get().setImageBitmap(this.result);
                    this.lastReqIsOk = true;
                } else if (this.errorRes > 0) {
                    this.refAiv.get().setImageResource(this.errorRes);
                } else if (this.errorBm != null && !this.errorBm.isRecycled()) {
                    this.refAiv.get().setImageBitmap(this.errorBm);
                } else if (this.defaultBm == null || this.defaultBm.isRecycled()) {
                    this.refAiv.get().setImageResource(this.defaultRes);
                } else {
                    this.refAiv.get().setImageBitmap(this.defaultBm);
                }
            } catch (Throwable t) {
                MobLog.getInstance().m69d(t);
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        if (getDrawable() != null && getDrawable().getIntrinsicWidth() != 0 && getDrawable().getIntrinsicHeight() != 0) {
            Matrix drawMatrix = getImageMatrix();
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int saveCount = canvas.getSaveCount();
            canvas.save();
            if (this.result != null) {
                myClip(canvas);
                canvas.drawBitmap(this.result, drawMatrix, new Paint(6));
            } else {
                if (!(drawMatrix == null && paddingLeft == 0 && paddingTop == 0)) {
                    if (Build.VERSION.SDK_INT >= 16 && getCropToPadding()) {
                        int scrollX = getScrollX();
                        int scrollY = getScrollY();
                        canvas.clipRect(scrollX + paddingLeft, scrollY + paddingTop, ((getRight() + scrollX) - getLeft()) - getPaddingRight(), ((getBottom() + scrollY) - getTop()) - getPaddingBottom());
                    }
                    canvas.translate((float) paddingLeft, (float) paddingTop);
                    if (drawMatrix != null) {
                        canvas.concat(drawMatrix);
                    }
                }
                getDrawable().draw(canvas);
            }
            canvas.restoreToCount(saveCount);
        }
    }

    private void myClip(Canvas canvas) {
        if (this.rect != null) {
            if (this.path == null) {
                int width = getWidth();
                int height = getHeight();
                this.path = new Path();
                this.path.addRoundRect(new RectF(0.0f, 0.0f, (float) width, (float) height), this.rect, Path.Direction.CW);
            }
            canvas.clipPath(this.path);
        }
    }
}
