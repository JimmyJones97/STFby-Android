package com.xzy.forestSystem.baidu.voicerecognition.android.p007ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/* renamed from: com.baidu.voicerecognition.android.ui.SDKProgressBar */
public class SDKProgressBar extends View {
    private final int[] COLOR_BLUE_DEEPBG = {-15584414, -15645323, -15708289, -15770997, -15832934, -15829850, -15892302, -15954753, -16017976, -16014892, -16077600, -16140051, -16140051};
    private final int[] COLOR_BLUE_LIGHTBG = {-4725762, -6892035, -8138755, -8992259, -10108420, -10371589, -10701318, -11031047, -11360520, -11427850, -11627534, -11760142, -11892239};
    private int barLength;
    private int barX;
    private int barY;
    private int[] colors;
    private ColorFilter mHsvFilter;
    private int mProgress;
    private Paint paint = new Paint();
    private int rectHeight;
    private int rectWidth;

    public SDKProgressBar(Context context) {
        super(context);
        initView();
    }

    public SDKProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        this.barX = getLeft();
        this.barY = getTop();
    }

    public void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > 80) {
            progress = 80;
        }
        this.mProgress = progress;
        invalidate();
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.barLength = View.MeasureSpec.getSize(widthMeasureSpec);
        this.rectHeight = this.barLength / 80;
        this.rectWidth = this.rectHeight;
        setMeasuredDimension(this.barLength, this.rectHeight);
    }

    public void setHsvFilter(ColorFilter filter) {
        this.mHsvFilter = filter;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.paint.setColorFilter(this.mHsvFilter);
        for (int i = 0; i <= this.mProgress; i++) {
            if (this.mProgress <= 12) {
                this.paint.setColor(this.colors[12 - (this.mProgress - i)]);
            } else if (i <= this.mProgress - 12) {
                this.paint.setColor(this.colors[0]);
            } else {
                this.paint.setColor(this.colors[12 - (this.mProgress - i)]);
            }
            canvas.drawRect((float) (this.barX + (this.rectWidth * i) + i), (float) this.barY, (float) (this.barX + (this.rectWidth * i) + i + this.rectWidth), (float) (this.barY + this.rectHeight), this.paint);
        }
    }

    public void setTheme(int theme) {
        this.colors = BaiduASRDialogTheme.isDeepStyle(theme) ? this.COLOR_BLUE_DEEPBG : this.COLOR_BLUE_LIGHTBG;
    }
}
