package  com.xzy.forestSystem.gogisapi.MyControls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import com.stczh.gzforestSystem.R;
import java.util.Timer;
import java.util.TimerTask;

public class NumberCircleProgressBar extends View {
    private static final int DEFAULT_INITIAL_ANGLE = 270;
    private static final String INSTANCE_CIRCLE_RADIUS = "circle_radius";
    private static final String INSTANCE_FILL_MODE = "full_mode";
    private static final String INSTANCE_MAX = "max";
    private static final String INSTANCE_PREFIX = "prefix";
    private static final String INSTANCE_PROGRESS = "progress";
    private static final String INSTANCE_REACHED_BAR_COLOR = "reached_bar_color";
    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_SUFFIX = "suffix";
    private static final String INSTANCE_TEXT_COLOR = "text_color";
    private static final String INSTANCE_TEXT_SIZE = "text_size";
    private static final String INSTANCE_UNREACHED_BAR_COLOR = "unreached_bar_color";
    private static final double[] PERCENT_TO_ANGLE = {0.0d, 20.87932689970087d, 26.447731823238804d, 30.414375934709003d, 33.61537654454588d, 36.352682410783395d, 38.77400205300625d, 40.964075929114315d, 42.9764755929523d, 44.847469272952004d, 46.60306925301236d, 48.26235502771122d, 49.83999431660394d, 51.34756086715217d, 52.794164708298474d, 54.18731158715907d, 55.53318944792137d, 56.837012206521074d, 58.103077046421646d, 59.33550926374806d, 60.53700176013739d, 61.710992282360436d, 62.85919970380261d, 63.984488813439555d, 65.08857848465665d, 66.1731875908393d, 67.24003500537289d, 68.29026664384769d, 69.32560137964909d, 70.34718512836734d, 71.35559084779759d, 72.35253741132523d, 73.33802481895025d, 74.31377194405803d, 75.28035174444373d, 76.23833717790248d, 77.1888741600245d, 78.13196269080984d, 79.0681757280536d, 79.99923214514119d, 80.92455898427748d, 81.8453021610527d, 82.7614616754669d, 83.6741834431103d, 84.58404042177803d, 85.49045965367499d, 86.39516001218658d, 87.29814149731276d, 88.19940410905353d, 89.1000937629992d, 90.0d, 90.90032715530023d, 91.80044385145077d, 92.70227942098667d, 93.60468794831772d, 94.50938830682928d, 95.41638049652137d, 96.325664517394d, 97.23838628503741d, 98.15511875724673d, 99.07528897622683d, 100.00118877315823d, 100.9316722324507d, 101.86845822748958d, 102.81154675827493d, 103.76151078260183d, 104.71949621606056d, 105.68607601644626d, 106.66182314155404d, 107.64731054917908d, 108.64425711270671d, 109.65266283213694d, 110.67424658085521d, 111.70958131665661d, 112.75981295513141d, 113.82666036966499d, 114.91126947584766d, 116.01535914706473d, 117.1406482567017d, 118.28942863593899d, 119.46284620036691d, 120.66433869675623d, 121.89677091408264d, 123.16300764132176d, 124.4670595830395d, 125.81293744380183d, 127.20579784376484d, 128.65251627647018d, 130.1599682354594d, 131.73789400324964d, 133.3971797779485d, 135.15272246222935d, 137.02342966333148d, 139.03565743983094d, 141.2260750906161d, 143.64739473283896d, 146.3844141201789d, 149.5855293215748d, 153.5521161372655d, 159.12069294814196d, 180.0d};
    private static final double[] PERCENT_TO_ARC = {0.0d, 0.364413d, 0.4616d, 0.530831d, 0.586699d, 0.634474d, 0.676734d, 0.714958d, 0.750081d, 0.782736d, 0.813377d, 0.842337d, 0.869872d, 0.896184d, 0.921432d, 0.945747d, 0.969237d, 0.991993d, 1.01409d, 1.0356d, 1.05657d, 1.07706d, 1.0971d, 1.11674d, 1.13601d, 1.15494d, 1.17356d, 1.19189d, 1.20996d, 1.22779d, 1.24539d, 1.26279d, 1.27999d, 1.29702d, 1.31389d, 1.33061d, 1.3472d, 1.36366d, 1.38d, 1.39625d, 1.4124d, 1.42847d, 1.44446d, 1.46039d, 1.47627d, 1.49209d, 1.50788d, 1.52364d, 1.53937d, 1.55509d, 1.5707963267948966d, 1.58651d, 1.60222d, 1.61796d, 1.63371d, 1.6495d, 1.66533d, 1.6812d, 1.69713d, 1.71313d, 1.72919d, 1.74535d, 1.76159d, 1.77794d, 1.7944d, 1.81098d, 1.8277d, 1.84457d, 1.8616d, 1.8788d, 1.8962d, 1.9138d, 1.93163d, 1.9497d, 1.96803d, 1.98665d, 2.00558d, 2.02485d, 2.04449d, 2.06454d, 2.08502d, 2.10599d, 2.1275d, 2.1496d, 2.17236d, 2.19585d, 2.22016d, 2.24541d, 2.27172d, 2.29926d, 2.32822d, 2.35886d, 2.39151d, 2.42663d, 2.46486d, 2.50712d, 2.55489d, 2.61076d, 2.67999d, 2.77718d, 3.141592653589793d};
    private static final int PROGRESS_TEXT_INVISIBLE = 1;
    private static final int PROGRESS_TEXT_VISIBLE = 0;
    private final float DEFAULT_CIRCLE_RADIUS;
    private final int DEFAULT_FILL_MODE;
    private final int DEFAULT_REACHED_COLOR;
    private final int DEFAULT_TEXT_COLOR;
    private final float DEFAULT_TEXT_SIZE;
    private final int DEFAULT_UNREACHED_COLOR;
    private int animStep;
    private int animToValue;
    private float centerX;
    private float centerY;
    private Handler handler;
    private boolean isFinish;
    private Paint mCirclePaint;
    private float mCircleRadius;
    private RectF mCircleRectF;
    private Context mContext;
    private String mCurrentDrawText;
    private boolean mDrawReachedBar;
    private float mDrawTextEnd;
    private float mDrawTextHeight;
    private float mDrawTextStart;
    private float mDrawTextWidth;
    private int mFillMode;
    private boolean mIfDrawText;
    private int mMax;
    private String mPrefix;
    private int mProgress;
    private int mReachedBarColor;
    private Paint mSectorPaint;
    private String mSuffix;
    private int mTextColor;
    private Paint mTextPaint;
    private float mTextSize;
    private int mUnreachedBarColor;
    private Timer timer;

    public enum ProgressTextVisibility {
        Visible,
        Invisible
    }

    public NumberCircleProgressBar(Context context) {
        this(context, null);
    }

    public NumberCircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.numberProgressBarStyle);
    }

    /* JADX INFO: finally extract failed */
    @SuppressLint("ResourceType")
    public NumberCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mMax = 100;
        this.mProgress = 0;
        this.mSuffix = "%";
        this.mPrefix = "";
        this.DEFAULT_TEXT_COLOR = Color.rgb(255, 255, 255);
        this.DEFAULT_REACHED_COLOR = Color.rgb(66, 145, 241);
        this.DEFAULT_UNREACHED_COLOR = Color.rgb(204, 204, 204);
        this.centerX = 20.0f;
        this.centerY = 20.0f;
        this.mDrawTextHeight = 10.0f;
        this.mCircleRectF = new RectF(0.0f, 0.0f, 0.0f, 0.0f);
        this.mDrawReachedBar = true;
        this.mIfDrawText = true;
        this.isFinish = false;
        this.animToValue = 0;
        this.animStep = 2;
        this.handler = new Handler() { // from class: com.xzy.forestSystem.gogisapi.MyControls.NumberCircleProgressBar.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1 && !NumberCircleProgressBar.this.isFinish) {
                    NumberCircleProgressBar.this.incrementProgressByAnim(NumberCircleProgressBar.this.animStep);
                    if (NumberCircleProgressBar.this.isAnimFinished()) {
                        NumberCircleProgressBar.this.isFinish = false;
                    }
                }
                super.handleMessage(msg);
            }
        };
        this.mContext = context;
        this.DEFAULT_CIRCLE_RADIUS = dp2px(40.5f);
        this.DEFAULT_TEXT_SIZE = sp2px(15.0f);
        this.DEFAULT_FILL_MODE = 0;
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NumberCircleProgressBar, defStyleAttr, 0);
        try {
            this.mFillMode = attributes.getInt(8, this.DEFAULT_FILL_MODE);
            this.mCircleRadius = attributes.getDimension(6, this.DEFAULT_CIRCLE_RADIUS);
            this.mReachedBarColor = attributes.getColor(3, this.DEFAULT_REACHED_COLOR);
            this.mUnreachedBarColor = attributes.getColor(2, this.DEFAULT_UNREACHED_COLOR);
            this.mTextColor = attributes.getColor(5, this.DEFAULT_TEXT_COLOR);
            this.mTextSize = attributes.getDimension(4, this.DEFAULT_TEXT_SIZE);
            if (attributes.getInt(7, 0) != 0) {
                this.mIfDrawText = false;
            }
            calculateCircleCenter();
            calculateDrawRectF();
            setProgress(attributes.getInt(0, 0));
            setMax(attributes.getInt(1, 100));
            attributes.recycle();
            initializePainters();
        } catch (Throwable th) {
            attributes.recycle();
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public int getSuggestedMinimumWidth() {
        return (int) this.mTextSize;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public int getSuggestedMinimumHeight() {
        return ((int) this.mCircleRadius) * 2;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec, true), measure(heightMeasureSpec, false));
        calculateCircleCenter();
        calculateDrawRectF();
    }

    private int measure(int measureSpec, boolean isWidth) {
        int padding;
        int mode = View.MeasureSpec.getMode(measureSpec);
        int size = View.MeasureSpec.getSize(measureSpec);
        if (isWidth) {
            padding = getPaddingLeft() + getPaddingRight();
        } else {
            padding = getPaddingTop() + getPaddingBottom();
        }
        if (mode == 1073741824) {
            return size;
        }
        int result = getSuggestedMinimumHeight() + padding;
        if (mode == Integer.MIN_VALUE) {
            return Math.min(result, size);
        }
        return result;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.centerX != 0.0f || this.centerY != 0.0f) {
            this.mCirclePaint.setStyle(Paint.Style.FILL);
            this.mSectorPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(this.centerX, this.centerY, this.mCircleRadius, this.mCirclePaint);
            if (this.mDrawReachedBar) {
                switch (this.mFillMode) {
                    case 0:
                        canvas.drawArc(this.mCircleRectF, 270.0f, (float) ((getProgress() * 360) / getMax()), true, this.mSectorPaint);
                        break;
                    case 1:
                        int percent = (getProgress() * 100) / getMax();
                        Path path = new Path();
                        if (percent >= 50) {
                            canvas.drawArc(this.mCircleRectF, (float) (450.0d - PERCENT_TO_ANGLE[percent]), (float) (PERCENT_TO_ANGLE[percent] * 2.0d), true, this.mSectorPaint);
                            float rSin = (float) (((double) this.mCircleRadius) * Math.sin(PERCENT_TO_ARC[percent]));
                            float rCos = (float) (((double) this.mCircleRadius) * Math.cos(PERCENT_TO_ARC[percent]));
                            path.moveTo(this.centerX, this.centerY);
                            path.lineTo(this.centerX + rSin, this.centerY + rCos);
                            path.lineTo(this.centerX - rSin, this.centerY + rCos);
                            path.lineTo(this.centerX, this.centerY);
                            path.close();
                            canvas.drawPath(path, this.mSectorPaint);
                            this.mSectorPaint.setStyle(Paint.Style.STROKE);
                            this.mSectorPaint.setStrokeWidth(1.0f);
                            canvas.drawPath(path, this.mSectorPaint);
                            break;
                        } else {
                            canvas.drawArc(this.mCircleRectF, (float) (90.0d - PERCENT_TO_ANGLE[percent]), (float) (PERCENT_TO_ANGLE[percent] * 2.0d), true, this.mSectorPaint);
                            float rSin2 = (float) (((double) this.mCircleRadius) * Math.sin(PERCENT_TO_ARC[percent]));
                            float rCos2 = (float) (((double) this.mCircleRadius) * Math.cos(PERCENT_TO_ARC[percent]));
                            path.moveTo(this.centerX, this.centerY);
                            path.lineTo(this.centerX + rSin2, this.centerY + rCos2);
                            path.lineTo(this.centerX - rSin2, this.centerY + rCos2);
                            path.lineTo(this.centerX, this.centerY);
                            path.close();
                            canvas.drawPath(path, this.mCirclePaint);
                            this.mCirclePaint.setStyle(Paint.Style.STROKE);
                            this.mCirclePaint.setStrokeWidth(1.0f);
                            canvas.drawPath(path, this.mCirclePaint);
                            break;
                        }
                }
            }
            if (this.mIfDrawText) {
                calculateDrawText();
            }
            canvas.drawText(this.mCurrentDrawText, this.mDrawTextStart, this.mDrawTextEnd, this.mTextPaint);
        }
    }

    private void calculateCircleCenter() {
        this.centerX = (((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) / 2.0f) + ((float) getPaddingLeft());
        this.centerY = (((float) ((getHeight() - getPaddingTop()) - getPaddingBottom())) / 2.0f) + ((float) getPaddingTop());
        if (this.centerX == 0.0f && this.centerY == 0.0f) {
            float f = 1.5f * this.mCircleRadius;
            this.centerY = f;
            this.centerX = f;
        }
    }

    private void calculateDrawRectF() {
        this.mCircleRectF.left = this.centerX - this.mCircleRadius;
        this.mCircleRectF.top = this.centerY - this.mCircleRadius;
        this.mCircleRectF.right = this.centerX + this.mCircleRadius;
        this.mCircleRectF.bottom = this.centerY + this.mCircleRadius;
    }

    private void calculateDrawText() {
        this.mCurrentDrawText = String.format("%d", Integer.valueOf((getProgress() * 100) / getMax()));
        this.mCurrentDrawText = String.valueOf(this.mPrefix) + this.mCurrentDrawText + this.mSuffix;
        Rect rect = new Rect();
        this.mTextPaint.getTextBounds(this.mCurrentDrawText, 0, 1, rect);
        this.mDrawTextWidth = (float) rect.width();
        this.mDrawTextHeight = (float) rect.height();
        this.mDrawTextStart = this.centerX + (this.mDrawTextWidth / 2.0f);
        this.mDrawTextEnd = this.centerY + (this.mDrawTextHeight / 2.0f);
    }

    private void initializePainters() {
        this.mCirclePaint = new Paint(1);
        this.mCirclePaint.setColor(this.mUnreachedBarColor);
        this.mCirclePaint.setStyle(Paint.Style.FILL);
        this.mSectorPaint = new Paint(1);
        this.mSectorPaint.setStyle(Paint.Style.FILL);
        this.mSectorPaint.setColor(this.mReachedBarColor);
        this.mTextPaint = new Paint(1);
        this.mTextPaint.setTextAlign(Paint.Align.CENTER);
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setTextSize(this.mTextSize);
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public float getProgressTextSize() {
        return this.mTextSize;
    }

    public int getUnreachedBarColor() {
        return this.mUnreachedBarColor;
    }

    public int getReachedBarColor() {
        return this.mReachedBarColor;
    }

    public int getProgress() {
        return this.mProgress;
    }

    public int getMax() {
        return this.mMax;
    }

    public void setMax(int Max) {
        if (Max > 0) {
            this.mMax = Max;
            invalidate();
        }
    }

    public void setProgressTextSize(float TextSize) {
        this.mTextSize = TextSize;
        this.mTextPaint.setTextSize(this.mTextSize);
        invalidate();
    }

    public void setProgressTextColor(int TextColor) {
        this.mTextColor = TextColor;
        this.mTextPaint.setColor(this.mTextColor);
        invalidate();
    }

    public void setSuffix(String suffix) {
        if (suffix == null) {
            this.mSuffix = "";
        } else {
            this.mSuffix = suffix;
        }
    }

    public String getSuffix() {
        return this.mSuffix;
    }

    public void setPrefix(String prefix) {
        if (prefix == null) {
            this.mPrefix = "";
        } else {
            this.mPrefix = prefix;
        }
    }

    public String getPrefix() {
        return this.mPrefix;
    }

    public float getCircleRadius() {
        return this.mCircleRadius;
    }

    public void setCircleRadius(float mCircleRadius2) {
        this.mCircleRadius = mCircleRadius2;
    }

    public void incrementProgressBy(int by) {
        if (by > 0) {
            int progress = getProgress() + by;
            if (progress > getMax()) {
                progress = getMax();
            }
            setProgress(progress);
        }
    }

    public boolean isFinished() {
        if (this.mProgress >= this.mMax) {
            return true;
        }
        return false;
    }

    public void setProgress(int Progress) {
        if (Progress <= getMax() && Progress >= 0) {
            this.mProgress = Progress;
            invalidate();
        }
    }

    public float dp2px(float dp) {
        return (dp * getResources().getDisplayMetrics().density) + 0.5f;
    }

    public float sp2px(float sp) {
        return sp * getResources().getDisplayMetrics().scaledDensity;
    }

    public void setProgressTextVisibility(ProgressTextVisibility visibility) {
        if (visibility == ProgressTextVisibility.Visible) {
            this.mIfDrawText = true;
        } else {
            this.mIfDrawText = false;
        }
        invalidate();
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_TEXT_COLOR, getTextColor());
        bundle.putFloat(INSTANCE_TEXT_SIZE, getProgressTextSize());
        bundle.putFloat(INSTANCE_CIRCLE_RADIUS, getCircleRadius());
        bundle.putInt(INSTANCE_REACHED_BAR_COLOR, getReachedBarColor());
        bundle.putInt(INSTANCE_UNREACHED_BAR_COLOR, getUnreachedBarColor());
        bundle.putInt(INSTANCE_MAX, getMax());
        bundle.putInt(INSTANCE_PROGRESS, getProgress());
        bundle.putString(INSTANCE_SUFFIX, getSuffix());
        bundle.putString(INSTANCE_PREFIX, getPrefix());
        return bundle;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.mFillMode = bundle.getInt(INSTANCE_FILL_MODE);
            this.mTextColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            this.mTextSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            this.mCircleRadius = bundle.getFloat(INSTANCE_CIRCLE_RADIUS);
            this.mReachedBarColor = bundle.getInt(INSTANCE_REACHED_BAR_COLOR);
            this.mUnreachedBarColor = bundle.getInt(INSTANCE_UNREACHED_BAR_COLOR);
            initializePainters();
            setMax(bundle.getInt(INSTANCE_MAX));
            setProgress(bundle.getInt(INSTANCE_PROGRESS));
            setPrefix(bundle.getString(INSTANCE_PREFIX));
            setSuffix(bundle.getString(INSTANCE_SUFFIX));
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean isAnimFinished() {
        if (this.mProgress >= this.animToValue) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void incrementProgressByAnim(int by) {
        if (by > 0) {
            int progress = getProgress() + by;
            if (progress > this.animToValue) {
                progress = this.animToValue;
            }
            setProgress(progress);
        }
    }

    public void SetProgressAnim(int value) {
        this.animToValue = value;
        setProgress(0);
        this.animStep = this.animToValue / 20;
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() { // from class: com.xzy.forestSystem.gogisapi.MyControls.NumberCircleProgressBar.2
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                Message message = new Message();
                message.what = 1;
                NumberCircleProgressBar.this.handler.sendMessage(message);
            }
        }, 500, 100);
    }
}
