package com.xzy.forestSystem.baidu.voicerecognition.android.p007ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.xzy.forestSystem.baidu.voicerecognition.android.NoProGuard;
import com.xzy.forestSystem.baidu.voicerecognition.android.NoProGuard;

/* renamed from: com.baidu.voicerecognition.android.ui.SDKAnimationView */
public class SDKAnimationView extends View implements NoProGuard {
    private static final int[] BAIDU_LOGO = {14336, 31744, 31992, 14844, 230300, 509804, 511852, 236552, 8184, 237336, 511980, 509932, 230156, 14844, 31992, 31744, 14336};
    private static final int BAR_DROPOFF_STEP = 1;
    private static final int BEGIN_LOC_X = 27;
    private static final byte[] GROUP1_VOLUME_ARRAY1 = {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
    private static final byte[] GROUP1_VOLUME_ARRAY2 = {3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 5, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3};
    private static final byte[] GROUP1_VOLUME_ARRAY3 = {4, 4, 4, 4, 5, 5, 5, 6, 6, 6, 5, 5, 5, 5, 6, 6, 6, 6, 5, 5, 4, 4, 4, 5, 5, 5, 6, 6, 7, 8, 8, 8, 9, 9, 9, 9, 9, 8, 8, 8, 7, 6, 6, 5, 5, 5, 4, 4, 4, 5, 5, 6, 6, 6, 6, 5, 5, 5, 5, 6, 6, 6, 5, 5, 5, 4, 4, 4, 4};
    private static final byte[] GROUP1_VOLUME_ARRAY4 = {7, 8, 8, 7, 7, 7, 8, 8, 9, 9, 9, 8, 8, 7, 6, 6, 5, 5, 5, 5, 6, 6, 6, 7, 7, 8, 8, 9, 10, 10, 11, 11, 11, 12, 12, 12, 11, 11, 11, 10, 10, 9, 8, 8, 7, 7, 6, 6, 6, 5, 5, 5, 5, 6, 6, 7, 8, 8, 9, 9, 9, 8, 8, 7, 7, 7, 8, 8, 7};
    private static final byte[] GROUP1_VOLUME_ARRAY5 = {9, 9, 9, 10, 10, 11, 11, 12, 12, 12, 12, 11, 11, 10, 10, 9, 9, 9, 8, 8, 8, 8, 9, 9, 9, 10, 10, 11, 12, 12, 13, 13, 14, 14, 14, 14, 14, 13, 13, 12, 12, 11, 10, 10, 9, 9, 9, 8, 8, 8, 8, 9, 9, 9, 10, 10, 11, 11, 12, 12, 12, 12, 11, 11, 10, 10, 9, 9, 9};
    private static final byte[] GROUP1_VOLUME_ARRAY6 = {11, 11, 11, 12, 12, 13, 13, 14, 14, 14, 15, 15, 15, 14, 14, 14, 13, 13, 13, 12, 12, 12, 12, 12, 13, 13, 13, 14, 14, 15, 15, 15, 16, 16, 16, 16, 16, 15, 15, 15, 14, 14, 13, 13, 13, 12, 12, 12, 12, 12, 13, 13, 13, 14, 14, 14, 15, 15, 15, 14, 14, 14, 13, 13, 12, 12, 11, 11, 11};
    private static final byte[] GROUP1_VOLUME_ARRAY7 = {13, 13, 14, 14, 15, 15, 16, 16, 16, 17, 17, 17, 16, 16, 16, 15, 15, 15, 14, 14, 14, 14, 15, 15, 15, 16, 16, 17, 17, 18, 18, 18, 19, 19, 19, 19, 19, 18, 18, 18, 17, 17, 16, 16, 15, 15, 15, 14, 14, 14, 14, 15, 15, 15, 16, 16, 16, 17, 17, 17, 16, 16, 16, 15, 15, 14, 14, 13, 13};
    private static final byte[] GROUP2_VOLUME_ARRAY1 = {3, 3, 3, 3, 4, 4, 4, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5, 4, 4, 4, 4, 3, 3, 3, 3, 4, 4, 4, 3, 3, 3, 3};
    private static final byte[] GROUP2_VOLUME_ARRAY2 = {3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 4, 3, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 4, 3, 3, 3};
    private static final byte[] GROUP2_VOLUME_ARRAY3 = {5, 5, 4, 4, 4, 5, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 9, 9, 9, 8, 8, 7, 7, 6, 6, 5, 5, 5, 4, 4, 4, 5, 5, 6, 6, 6, 5, 5, 4, 4, 4, 5, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 9, 9, 9, 8, 8, 7, 7, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3};
    private static final byte[] GROUP2_VOLUME_ARRAY4 = {5, 5, 5, 6, 6, 6, 7, 7, 8, 8, 9, 10, 10, 11, 11, 11, 12, 12, 12, 11, 11, 11, 10, 10, 9, 8, 8, 7, 7, 6, 6, 6, 5, 5, 5, 5, 5, 6, 6, 6, 7, 7, 8, 8, 9, 10, 10, 11, 11, 11, 12, 12, 12, 11, 11, 11, 10, 10, 9, 8, 8, 7, 7, 6, 6, 6, 5, 5, 5};
    private static final byte[] GROUP2_VOLUME_ARRAY5 = {9, 9, 8, 8, 8, 8, 9, 9, 9, 10, 10, 11, 12, 12, 13, 13, 14, 14, 14, 14, 14, 13, 13, 12, 12, 11, 11, 10, 10, 9, 9, 9, 8, 8, 8, 8, 8, 9, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13, 14, 14, 14, 14, 14, 13, 13, 12, 12, 11, 10, 10, 9, 9, 9, 8, 8, 8, 8, 9, 9};
    private static final byte[] GROUP2_VOLUME_ARRAY6 = {13, 13, 13, 13, 14, 14, 14, 13, 13, 13, 13, 13, 14, 14, 15, 15, 15, 16, 16, 16, 16, 16, 15, 15, 15, 14, 14, 14, 13, 13, 13, 13, 12, 12, 12, 12, 12, 13, 13, 13, 13, 14, 14, 14, 15, 15, 15, 16, 16, 16, 16, 16, 15, 15, 15, 14, 14, 13, 13, 13, 13, 13, 14, 14, 14, 13, 13, 13, 13};
    private static final byte[] GROUP2_VOLUME_ARRAY7 = {15, 15, 14, 14, 14, 14, 15, 15, 15, 16, 16, 17, 17, 18, 18, 18, 19, 19, 19, 19, 19, 18, 18, 18, 17, 17, 16, 16, 15, 15, 15, 14, 14, 14, 14, 14, 14, 14, 15, 15, 15, 16, 16, 17, 17, 18, 18, 18, 19, 19, 19, 19, 19, 18, 18, 18, 17, 17, 16, 16, 15, 15, 15, 14, 14, 14, 14, 15, 15};
    private static final byte[] GROUP3_VOLUME_ARRAY1 = {3, 3, 3, 4, 4, 4, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 4, 4, 4, 4, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 5, 4, 4, 4, 4, 3, 3, 3, 3, 4, 4, 4, 4, 5, 5, 5, 5, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 4, 4, 4, 3, 3, 3};
    private static final byte[] GROUP3_VOLUME_ARRAY2 = {3, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 4, 4, 4, 5, 5, 6, 6, 6, 7, 7, 7, 7, 7, 6, 6, 6, 5, 5, 4, 4, 4, 3, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3};
    private static final byte[] GROUP3_VOLUME_ARRAY3 = {5, 5, 4, 4, 4, 5, 5, 5, 6, 6, 7, 7, 8, 8, 8, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 7, 7, 7, 8, 8, 9, 9, 9, 9, 9, 8, 8, 7, 7, 7, 6, 6, 6, 7, 7, 7, 8, 8, 8, 9, 9, 9, 8, 8, 8, 7, 7, 6, 6, 5, 5, 5, 4, 4, 4, 5, 5};
    private static final byte[] GROUP3_VOLUME_ARRAY4 = {6, 6, 6, 7, 7, 8, 9, 9, 10, 10, 10, 11, 11, 11, 10, 10, 10, 9, 9, 8, 8, 7, 7, 7, 7, 8, 8, 9, 10, 10, 11, 11, 11, 12, 12, 12, 11, 11, 11, 10, 10, 9, 8, 8, 7, 7, 7, 7, 8, 8, 9, 9, 10, 10, 10, 11, 11, 11, 10, 10, 10, 9, 9, 8, 7, 7, 6, 6, 6};
    private static final byte[] GROUP3_VOLUME_ARRAY5 = {8, 8, 8, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13, 13, 13, 12, 12, 11, 11, 10, 10, 9, 9, 9, 9, 10, 10, 11, 12, 12, 13, 13, 14, 14, 14, 14, 14, 13, 13, 12, 12, 11, 10, 10, 9, 9, 9, 9, 10, 10, 11, 11, 12, 12, 13, 13, 13, 13, 12, 12, 11, 11, 10, 10, 9, 9, 8, 8, 8};
    private static final byte[] GROUP3_VOLUME_ARRAY6 = {11, 11, 11, 11, 11, 12, 12, 12, 13, 13, 13, 14, 14, 14, 15, 15, 15, 15, 15, 14, 14, 14, 13, 13, 13, 13, 14, 14, 14, 15, 15, 15, 16, 16, 16, 16, 16, 15, 15, 15, 14, 14, 14, 13, 13, 13, 13, 14, 14, 14, 15, 15, 15, 15, 15, 14, 14, 14, 13, 13, 13, 12, 12, 12, 11, 11, 11, 11, 11};
    private static final byte[] GROUP3_VOLUME_ARRAY7 = {14, 14, 14, 15, 15, 16, 16, 17, 17, 18, 18, 18, 18, 17, 17, 16, 16, 15, 15, 14, 14, 14, 15, 15, 15, 16, 16, 17, 17, 18, 18, 18, 19, 19, 19, 19, 19, 18, 18, 18, 17, 17, 16, 16, 15, 15, 15, 14, 14, 14, 15, 15, 16, 16, 17, 17, 18, 18, 18, 18, 17, 17, 16, 16, 15, 15, 14, 14, 14};
    public static final int INITIALIZING_ANIMATION_STATE = 4;
    private static final int RECT_IN_ROW = 69;

    private static byte[] INIT_VOLUME_ARRAY = new byte[RECT_IN_ROW];
    public static final int NO_ANIMATION_STATE = 0;
    public static final int PREPARING_ANIMATION_STATE = 1;
    private static final int PREPARING_BAIDU_LOGO_TIME = 1200;
    private static byte[] PREPARING_VOLUME_ARRAY = {11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11, 11};
    public static final int RECOGNIZING_ANIMATION_STATE = 3;
    private static final int RECOGNIZING_SCANLINE_SHADOW_NUMBER = 5;
    private static final int RECOGNIZING_WAVE_TRANSLATION_TIME = 20;
    public static final int RECORDING_ANIMATION_STATE = 2;
    private static final int RECT_IN_COLUMN = 21;
    public static final int SAMPE_RATE_VOLUME = 50;
    private static byte[][] VOLUMES_GROUP1 = {GROUP1_VOLUME_ARRAY1, GROUP1_VOLUME_ARRAY2, GROUP1_VOLUME_ARRAY3, GROUP1_VOLUME_ARRAY4, GROUP1_VOLUME_ARRAY5, GROUP1_VOLUME_ARRAY6, GROUP1_VOLUME_ARRAY7};
    private static byte[][] VOLUMES_GROUP2 = {GROUP2_VOLUME_ARRAY1, GROUP2_VOLUME_ARRAY2, GROUP2_VOLUME_ARRAY3, GROUP2_VOLUME_ARRAY4, GROUP2_VOLUME_ARRAY5, GROUP2_VOLUME_ARRAY6, GROUP2_VOLUME_ARRAY7};
    private static byte[][] VOLUMES_GROUP3 = {GROUP3_VOLUME_ARRAY1, GROUP3_VOLUME_ARRAY2, GROUP3_VOLUME_ARRAY3, GROUP3_VOLUME_ARRAY4, GROUP3_VOLUME_ARRAY5, GROUP3_VOLUME_ARRAY6, GROUP3_VOLUME_ARRAY7};
    private byte[] currentVolumeArray = new byte[RECT_IN_ROW];
    private int mAnimationState = -1;
    private Paint mBaiduLogePaint;
    int mBgColor = 0;
    private int mCurrentBar;
    private float mCurrentDBLevelMeter;
    private Paint mGriddingPaint = new Paint();
    private ColorFilter mHsvFilter;
    private Bitmap mHsvFilterBitmap;
    private Canvas mHsvFilterCanvas;
    private Paint mHsvFilterPaint;
    private Runnable mInvalidateTask = new Runnable() { // from class: com.baidu.voicerecognition.android.ui.SDKAnimationView.2
        @Override // java.lang.Runnable
        public void run() {
            SDKAnimationView.this.invalidate();
            SDKAnimationView.this.post(this);
        }
    };
    private Paint mLogoReversePaint;
    private Drawable mMask;
    private long mPreparingBeginTime;
    private long mRecognizingBeginTime;
    private int mRecognizingLineShadowColor1;
    private int mRecognizingLineShadowColor2;
    private int mRecognizingRefreshCount;
    private int mRecognizingWaveIndex;
    private long mRecordingCurrentTime;
    private long mRecordingInterpolationTime;
    private Runnable mRecordingUpdateTask = new Runnable() { // from class: com.baidu.voicerecognition.android.ui.SDKAnimationView.1
        @Override // java.lang.Runnable
        public void run() {
            int bar = (int) (((float) 0) + ((6.0f * SDKAnimationView.this.getCurrentDBLevelMeter()) / 100.0f));
            if (bar > SDKAnimationView.this.mCurrentBar) {
                SDKAnimationView.this.mCurrentBar = bar;
            } else {
                SDKAnimationView.this.mCurrentBar = Math.max(bar, SDKAnimationView.this.mCurrentBar - 1);
            }
            SDKAnimationView.this.mCurrentBar = Math.min(6, SDKAnimationView.this.mCurrentBar);
            if (SDKAnimationView.this.mCurrentBar == 0 && ((int) (Math.random() * 4.0d)) == 0) {
                SDKAnimationView.this.mCurrentBar = 1;
            }
            SDKAnimationView.this.setVolumeLevel(SDKAnimationView.this.mCurrentBar);
            SDKAnimationView.this.removeCallbacks(SDKAnimationView.this.mRecordingUpdateTask);
            SDKAnimationView.this.postDelayed(SDKAnimationView.this.mRecordingUpdateTask, 50);
        }
    };
    private int mVolumeCeilingColor1;
    private int mVolumeCeilingColor2;
    private int mVolumeShadowColor1;
    private int mVolumeShadowColor2;
    private Paint mVolumnCeilingPaint;
    private Paint mVolumnShadowPaint;
    private int mWidth = 0;
    private double sampleSideLength = 0.0d;
    private byte[] targetVolumeArray = new byte[RECT_IN_ROW];
    private byte[][] volumes;

    public SDKAnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mGriddingPaint.setStrokeWidth(1.0f);
        this.mBaiduLogePaint = new Paint();
        this.mLogoReversePaint = new Paint();
        this.mVolumnCeilingPaint = new Paint();
        this.mVolumnShadowPaint = new Paint();
        this.volumes = VOLUMES_GROUP1;
        this.currentVolumeArray = this.volumes[0];
        this.targetVolumeArray = this.volumes[0];
        setThemeStyle(16777217);
    }

    public void setThemeStyle(int themeStyle) {
        int i = 343434;
        int i2 = -1;
        int i3 = -14869219;
        int i4 = -16433782;
        boolean isDeepStyle = BaiduASRDialogTheme.isDeepStyle(themeStyle);
        this.mBgColor = isDeepStyle ? -14869219 : -592138;
        Paint paint = this.mGriddingPaint;
        if (!isDeepStyle) {
            i3 = -1;
        }
        paint.setColor(i3);
        this.mBaiduLogePaint.setColor(isDeepStyle ? -13684945 : -1250068);
        this.mLogoReversePaint.setColor(isDeepStyle ? -16499830 : -2428673);
        this.mVolumeCeilingColor1 = isDeepStyle ? -12221715 : -8015623;
        this.mVolumeCeilingColor2 = isDeepStyle ? 4555501 : -6628366;
        this.mVolumeShadowColor1 = isDeepStyle ? -16433782 : -3216385;
        if (isDeepStyle) {
            i2 = 343434;
        }
        this.mVolumeShadowColor2 = i2;
        if (!isDeepStyle) {
            i4 = -3282177;
        }
        this.mRecognizingLineShadowColor1 = i4;
        if (!isDeepStyle) {
            i = 13495039;
        }
        this.mRecognizingLineShadowColor2 = i;
        this.mMask = getResources().getDrawable(isDeepStyle ? getContext().getResources().getIdentifier("bdspeech_mask_deep", "drawable", getContext().getPackageName()) : getContext().getResources().getIdentifier("bdspeech_mask_light", "drawable", getContext().getPackageName()));
    }

    public void startInitializingAnimation() {
        this.mAnimationState = 4;
        this.mPreparingBeginTime = System.currentTimeMillis();
        removeCallbacks(this.mInvalidateTask);
        removeCallbacks(this.mRecordingUpdateTask);
        post(this.mInvalidateTask);
    }

    public void startPreparingAnimation() {
        this.mAnimationState = 1;
        this.mPreparingBeginTime = System.currentTimeMillis();
        removeCallbacks(this.mInvalidateTask);
        removeCallbacks(this.mRecordingUpdateTask);
        post(this.mInvalidateTask);
    }

    public void startRecordingAnimation() {
        this.mAnimationState = 2;
        removeCallbacks(this.mInvalidateTask);
        removeCallbacks(this.mRecordingUpdateTask);
        post(this.mInvalidateTask);
        post(this.mRecordingUpdateTask);
    }

    public void startRecognizingAnimation() {
        this.mAnimationState = 3;
        this.mRecognizingBeginTime = System.currentTimeMillis();
        this.mRecognizingWaveIndex = 0;
        this.mRecognizingRefreshCount = 0;
        removeCallbacks(this.mInvalidateTask);
        removeCallbacks(this.mRecordingUpdateTask);
        post(this.mInvalidateTask);
    }

    public void resetAnimation() {
        removeCallbacks(this.mInvalidateTask);
        removeCallbacks(this.mRecordingUpdateTask);
        this.mAnimationState = 0;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setVolumeLevel(int level) {
        if (this.volumes != null && level >= 0 && level < this.volumes.length) {
            this.currentVolumeArray = this.targetVolumeArray;
            this.mRecordingInterpolationTime = System.currentTimeMillis();
            switch ((int) (2.0d * Math.random())) {
                case 0:
                    this.volumes = VOLUMES_GROUP1;
                    break;
                case 1:
                    this.volumes = VOLUMES_GROUP2;
                    break;
                case 2:
                    this.volumes = VOLUMES_GROUP3;
                    break;
                default:
                    this.volumes = VOLUMES_GROUP1;
                    break;
            }
            this.targetVolumeArray = this.volumes[level];
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.mWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        this.sampleSideLength = ((double) this.mWidth) / 69.0d;
        setMeasuredDimension(this.mWidth, (int) (this.sampleSideLength * 21.0d));
    }

    public void setHsvFilter(ColorFilter filter) {
        this.mHsvFilter = filter;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mHsvFilterBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        this.mHsvFilterCanvas = new Canvas(this.mHsvFilterBitmap);
        this.mHsvFilterPaint = new Paint();
        this.mHsvFilterPaint.setColorFilter(this.mHsvFilter);
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        realOnDraw(this.mHsvFilterCanvas);
        canvas.drawBitmap(this.mHsvFilterBitmap, 0.0f, 0.0f, this.mHsvFilterPaint);
    }

    /* access modifiers changed from: protected */
    public void realOnDraw(Canvas canvas) {
        int alpha;
        int alpha2;
        canvas.drawColor(this.mBgColor);
        switch (this.mAnimationState) {
            case 1:
                this.currentVolumeArray = PREPARING_VOLUME_ARRAY;
                this.targetVolumeArray = PREPARING_VOLUME_ARRAY;
                long time_interval = System.currentTimeMillis() - this.mPreparingBeginTime;
                if (time_interval < 1200) {
                    alpha = (int) ((((double) ((int) ((System.currentTimeMillis() - this.mPreparingBeginTime) % 1200))) / 1200.0d) * 255.0d);
                } else {
                    int duration = (int) (time_interval % 1200);
                    if (duration < 600) {
                        alpha = (int) ((1.0d - ((((double) duration) / 600.0d) * 0.800000011920929d)) * 255.0d);
                    } else {
                        alpha = (int) ((1.0d - ((((double) (1200 - duration)) / 600.0d) * 0.800000011920929d)) * 255.0d);
                    }
                }
                drawVoiceVolumn(canvas, alpha);
                drawBaiduLogo(canvas, alpha);
                break;
            case 2:
                this.mRecordingCurrentTime = System.currentTimeMillis();
                drawVoiceVolumn(canvas, 255);
                drawBaiduLogo(canvas, 255);
                break;
            case 3:
                if (System.currentTimeMillis() - this.mRecognizingBeginTime > 20) {
                    this.mRecognizingBeginTime = System.currentTimeMillis();
                    if (this.mRecognizingRefreshCount == 0) {
                        this.mRecognizingWaveIndex++;
                        if (this.mRecognizingWaveIndex >= 26) {
                            this.mRecognizingRefreshCount = 1;
                        }
                    } else {
                        this.mRecognizingWaveIndex--;
                        if (this.mRecognizingWaveIndex <= -5) {
                            this.mRecognizingRefreshCount = 0;
                        }
                    }
                }
                drawRecognizingLine(canvas);
                drawRecognizingBaiduLogo(canvas);
                break;
            case 4:
                this.currentVolumeArray = INIT_VOLUME_ARRAY;
                this.targetVolumeArray = INIT_VOLUME_ARRAY;
                long time_interval2 = System.currentTimeMillis() - this.mPreparingBeginTime;
                if (time_interval2 < 1200) {
                    alpha2 = (int) ((((double) ((int) ((System.currentTimeMillis() - this.mPreparingBeginTime) % 1200))) / 1200.0d) * 255.0d);
                } else {
                    int duration2 = (int) (time_interval2 % 1200);
                    if (duration2 < 600) {
                        alpha2 = (int) ((1.0d - ((((double) duration2) / 600.0d) * 0.800000011920929d)) * 255.0d);
                    } else {
                        alpha2 = (int) ((1.0d - ((((double) (1200 - duration2)) / 600.0d) * 0.800000011920929d)) * 255.0d);
                    }
                }
                drawBaiduLogo(canvas, alpha2);
                break;
        }
        drawGridding(canvas);
        drawMask(canvas);
    }

    private void drawBaiduLogo(Canvas canvas, int alpha) {
        this.mBaiduLogePaint.setAlpha(alpha);
        this.mLogoReversePaint.setAlpha(alpha);
        for (int i = 27; i < BAIDU_LOGO.length + 27; i++) {
            for (int j = 0; j < 21; j++) {
                if (((BAIDU_LOGO[i - 27] >> j) & 1) == 1) {
                    int interval_time = (int) (this.mRecordingCurrentTime - this.mRecordingInterpolationTime);
                    if (interval_time > 50) {
                        interval_time = 50;
                    }
                    int volume = (int) (((double) this.currentVolumeArray[i - 1]) + ((((double) (this.targetVolumeArray[i - 1] - this.currentVolumeArray[i - 1])) * ((double) interval_time)) / 50.0d));
                    if (j >= volume) {
                        canvas.drawRect((float) ((int) (this.sampleSideLength * ((double) (i - 1)))), (float) ((int) (this.sampleSideLength * ((double) ((21 - j) - 1)))), (float) ((int) (this.sampleSideLength * ((double) i))), (float) ((int) (this.sampleSideLength * ((double) (21 - j)))), this.mBaiduLogePaint);
                    } else if (j < volume - 1) {
                        canvas.drawRect((float) ((int) (this.sampleSideLength * ((double) (i - 1)))), (float) ((int) (this.sampleSideLength * ((double) ((21 - j) - 1)))), (float) ((int) (this.sampleSideLength * ((double) i))), (float) ((int) (this.sampleSideLength * ((double) (21 - j)))), this.mLogoReversePaint);
                    }
                }
            }
        }
    }

    private void drawGridding(Canvas canvas) {
        for (int col = 0; col <= 21; col++) {
            canvas.drawLine(0.0f, (float) ((int) (this.sampleSideLength * ((double) col))), (float) this.mWidth, (float) ((int) (this.sampleSideLength * ((double) col))), this.mGriddingPaint);
        }
        for (int row = 0; row <= RECT_IN_ROW; row++) {
            canvas.drawLine((float) ((int) (this.sampleSideLength * ((double) row))), 0.0f, (float) ((int) (this.sampleSideLength * ((double) row))), (float) getHeight(), this.mGriddingPaint);
        }
    }

    private void drawMask(Canvas canvas) {
        if (this.mMask != null) {
            this.mMask.setBounds(0, 0, this.mWidth, getHeight());
            this.mMask.draw(canvas);
        }
    }

    private void drawVoiceVolumn(Canvas canvas, int alpha) {
        this.mVolumnShadowPaint.setShader(new LinearGradient(0.0f, 1.0f, 0.0f, (float) ((getHeight() * 2) / 3), this.mVolumeShadowColor1, this.mVolumeShadowColor2, Shader.TileMode.CLAMP));
        this.mVolumnShadowPaint.setAlpha(alpha);
        for (int i = 0; i < RECT_IN_ROW; i++) {
            int interval_time = (int) (this.mRecordingCurrentTime - this.mRecordingInterpolationTime);
            if (interval_time > 50) {
                interval_time = 50;
            }
            int volume = (int) (((double) this.currentVolumeArray[i]) + ((((double) (this.targetVolumeArray[i] - this.currentVolumeArray[i])) * ((double) interval_time)) / 50.0d));
            canvas.save();
            canvas.translate((float) ((int) (this.sampleSideLength * ((double) i))), (float) ((int) (this.sampleSideLength * ((double) (21 - volume)))));
            canvas.drawRect(0.0f, 0.0f, (float) ((int) this.sampleSideLength), (float) (getHeight() - ((int) (this.sampleSideLength * ((double) (21 - volume))))), this.mVolumnShadowPaint);
            canvas.restore();
            int a = (int) (((double) ((this.mVolumeCeilingColor1 >> 24) & 255)) + (((double) (((this.mVolumeCeilingColor2 >> 24) & 255) - ((this.mVolumeCeilingColor1 >> 24) & 255))) * (Math.abs(((double) i) - 34.5d) / 34.5d)));
            this.mVolumnCeilingPaint.setColor((a << 24) | ((((int) (((double) ((this.mVolumeCeilingColor1 >> 16) & 255)) + (((double) (((this.mVolumeCeilingColor2 >> 16) & 255) - ((this.mVolumeCeilingColor1 >> 16) & 255))) * (Math.abs(((double) i) - 34.5d) / 34.5d)))) & 255) << 16) | ((((int) (((double) ((this.mVolumeCeilingColor1 >> 8) & 255)) + (((double) (((this.mVolumeCeilingColor2 >> 8) & 255) - ((this.mVolumeCeilingColor1 >> 8) & 255))) * (Math.abs(((double) i) - 34.5d) / 34.5d)))) & 255) << 8) | (((int) (((double) (this.mVolumeCeilingColor1 & 255)) + (((double) ((this.mVolumeCeilingColor2 & 255) - (this.mVolumeCeilingColor1 & 255))) * (Math.abs(((double) i) - 34.5d) / 34.5d)))) & 255));
            this.mVolumnCeilingPaint.setAlpha((int) ((((double) alpha) * ((double) a)) / 255.0d));
            canvas.drawRect((float) ((int) (this.sampleSideLength * ((double) i))), (float) ((int) (this.sampleSideLength * ((double) (21 - volume)))), (float) ((int) (this.sampleSideLength * ((double) (i + 1)))), (float) ((int) (this.sampleSideLength * ((double) ((21 - volume) + 1)))), this.mVolumnCeilingPaint);
        }
    }

    private void drawRecognizingBaiduLogo(Canvas canvas) {
        this.mBaiduLogePaint.setAlpha(255);
        this.mLogoReversePaint.setAlpha(255);
        for (int i = 27; i < BAIDU_LOGO.length + 27; i++) {
            for (int j = 0; j < 21; j++) {
                if (((BAIDU_LOGO[i - 27] >> j) & 1) == 1) {
                    int volume = this.mRecognizingWaveIndex;
                    if (j >= volume) {
                        canvas.drawRect((float) ((int) (this.sampleSideLength * ((double) (i - 1)))), (float) ((int) (this.sampleSideLength * ((double) ((21 - j) - 1)))), (float) ((int) (this.sampleSideLength * ((double) i))), (float) ((int) (this.sampleSideLength * ((double) (21 - j)))), this.mBaiduLogePaint);
                    } else if (j < volume - 1) {
                        canvas.drawRect((float) ((int) (this.sampleSideLength * ((double) (i - 1)))), (float) ((int) (this.sampleSideLength * ((double) ((21 - j) - 1)))), (float) ((int) (this.sampleSideLength * ((double) i))), (float) ((int) (this.sampleSideLength * ((double) (21 - j)))), this.mLogoReversePaint);
                    }
                }
            }
        }
    }

    private void drawRecognizingLine(Canvas canvas) {
        if (this.mRecognizingRefreshCount == 0) {
            this.mVolumnShadowPaint.setShader(new LinearGradient(0.0f, 1.0f, 0.0f, (float) ((int) (5.0d * this.sampleSideLength)), this.mRecognizingLineShadowColor1, this.mRecognizingLineShadowColor2, Shader.TileMode.MIRROR));
            canvas.save();
            canvas.translate(0.0f, (float) ((int) (this.sampleSideLength * ((double) (21 - (this.mRecognizingWaveIndex - 1))))));
            canvas.drawRect(0.0f, 0.0f, (float) this.mWidth, (float) ((int) (this.sampleSideLength * 5.0d)), this.mVolumnShadowPaint);
            canvas.restore();
        } else {
            this.mVolumnShadowPaint.setShader(new LinearGradient(0.0f, 1.0f, 0.0f, (float) ((int) (5.0d * this.sampleSideLength)), this.mRecognizingLineShadowColor2, this.mRecognizingLineShadowColor1, Shader.TileMode.MIRROR));
            canvas.save();
            canvas.translate(0.0f, (float) ((int) (this.sampleSideLength * ((double) (21 - (this.mRecognizingWaveIndex + 5))))));
            canvas.drawRect(0.0f, 0.0f, (float) this.mWidth, (float) ((int) (5.0d * this.sampleSideLength)), this.mVolumnShadowPaint);
            canvas.restore();
        }
        for (int i = 0; i < RECT_IN_ROW; i++) {
            this.mVolumnCeilingPaint.setColor((((int) (((double) ((this.mVolumeCeilingColor1 >> 24) & 255)) + (((double) (((this.mVolumeCeilingColor2 >> 24) & 255) - ((this.mVolumeCeilingColor1 >> 24) & 255))) * (Math.abs(((double) i) - 34.5d) / 34.5d)))) << 24) | ((((int) (((double) ((this.mVolumeCeilingColor1 >> 16) & 255)) + (((double) (((this.mVolumeCeilingColor2 >> 16) & 255) - ((this.mVolumeCeilingColor1 >> 16) & 255))) * (Math.abs(((double) i) - 34.5d) / 34.5d)))) & 255) << 16) | ((((int) (((double) ((this.mVolumeCeilingColor1 >> 8) & 255)) + (((double) (((this.mVolumeCeilingColor2 >> 8) & 255) - ((this.mVolumeCeilingColor1 >> 8) & 255))) * (Math.abs(((double) i) - 34.5d) / 34.5d)))) & 255) << 8) | (((int) (((double) (this.mVolumeCeilingColor1 & 255)) + (((double) ((this.mVolumeCeilingColor2 & 255) - (this.mVolumeCeilingColor1 & 255))) * (Math.abs(((double) i) - 34.5d) / 34.5d)))) & 255));
            canvas.drawRect((float) ((int) (this.sampleSideLength * ((double) i))), (float) ((int) (this.sampleSideLength * ((double) (21 - this.mRecognizingWaveIndex)))), (float) ((int) (this.sampleSideLength * ((double) (i + 1)))), (float) ((int) (this.sampleSideLength * ((double) ((21 - this.mRecognizingWaveIndex) + 1)))), this.mVolumnCeilingPaint);
        }
    }

    public void startVoiceAnimation(int state) {
        switch (state) {
            case 0:
                resetAnimation();
                return;
            case 1:
                startPreparingAnimation();
                return;
            case 2:
                startRecordingAnimation();
                return;
            case 3:
                startRecognizingAnimation();
                return;
            case 4:
                startInitializingAnimation();
                return;
            default:
                resetAnimation();
                return;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private float getCurrentDBLevelMeter() {
        return this.mCurrentDBLevelMeter;
    }

    public void setCurrentDBLevelMeter(float rmsDb) {
        this.mCurrentDBLevelMeter = rmsDb;
    }
}
