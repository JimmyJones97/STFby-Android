package com.xzy.forestSystem.hellocharts.view;

import android.content.Context;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;

import androidx.core.view.ViewCompat;

import com.xzy.forestSystem.hellocharts.animation.PieChartRotationAnimator;
import com.xzy.forestSystem.hellocharts.animation.PieChartRotationAnimatorV14;
import com.xzy.forestSystem.hellocharts.animation.PieChartRotationAnimatorV8;
import com.xzy.forestSystem.hellocharts.gesture.PieChartTouchHandler;
import com.xzy.forestSystem.hellocharts.listener.DummyPieChartOnValueSelectListener;
import com.xzy.forestSystem.hellocharts.listener.PieChartOnValueSelectListener;
import com.xzy.forestSystem.hellocharts.model.ChartData;
import com.xzy.forestSystem.hellocharts.model.PieChartData;
import com.xzy.forestSystem.hellocharts.model.SelectedValue;
import com.xzy.forestSystem.hellocharts.model.SliceValue;
import com.xzy.forestSystem.hellocharts.provider.PieChartDataProvider;
import com.xzy.forestSystem.hellocharts.renderer.PieChartRenderer;

public class PieChartView extends AbstractChartView implements PieChartDataProvider {
    private static final String TAG = "PieChartView";
    protected PieChartData data;
    protected PieChartOnValueSelectListener onValueTouchListener;
    protected PieChartRenderer pieChartRenderer;
    protected PieChartRotationAnimator rotationAnimator;

    public PieChartView(Context context) {
        this(context, null, 0);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.onValueTouchListener = new DummyPieChartOnValueSelectListener();
        this.pieChartRenderer = new PieChartRenderer(context, this, this);
        this.touchHandler = new PieChartTouchHandler(context, this);
        setChartRenderer(this.pieChartRenderer);
        if (Build.VERSION.SDK_INT < 14) {
            this.rotationAnimator = new PieChartRotationAnimatorV8(this);
        } else {
            this.rotationAnimator = new PieChartRotationAnimatorV14(this);
        }
        setPieChartData(PieChartData.generateDummyData());
    }

    @Override // lecho.lib.hellocharts.provider.PieChartDataProvider
    public PieChartData getPieChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.provider.PieChartDataProvider
    public void setPieChartData(PieChartData data2) {
        if (data2 == null) {
            this.data = PieChartData.generateDummyData();
        } else {
            this.data = data2;
        }
        super.onChartDataChange();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public ChartData getChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void callTouchListener() {
        SelectedValue selectedValue = this.chartRenderer.getSelectedValue();
        if (selectedValue.isSet()) {
            this.onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), this.data.getValues().get(selectedValue.getFirstIndex()));
            return;
        }
        this.onValueTouchListener.onValueDeselected();
    }

    public PieChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(PieChartOnValueSelectListener touchListener) {
        if (touchListener != null) {
            this.onValueTouchListener = touchListener;
        }
    }

    public RectF getCircleOval() {
        return this.pieChartRenderer.getCircleOval();
    }

    public void setCircleOval(RectF orginCircleOval) {
        this.pieChartRenderer.setCircleOval(orginCircleOval);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public int getChartRotation() {
        return this.pieChartRenderer.getChartRotation();
    }

    public void setChartRotation(int rotation, boolean isAnimated) {
        if (isAnimated) {
            this.rotationAnimator.cancelAnimation();
            this.rotationAnimator.startAnimation((float) this.pieChartRenderer.getChartRotation(), (float) rotation);
        } else {
            this.pieChartRenderer.setChartRotation(rotation);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public boolean isChartRotationEnabled() {
        if (this.touchHandler instanceof PieChartTouchHandler) {
            return ((PieChartTouchHandler) this.touchHandler).isRotationEnabled();
        }
        return false;
    }

    public void setChartRotationEnabled(boolean isRotationEnabled) {
        if (this.touchHandler instanceof PieChartTouchHandler) {
            ((PieChartTouchHandler) this.touchHandler).setRotationEnabled(isRotationEnabled);
        }
    }

    public SliceValue getValueForAngle(int angle, SelectedValue selectedValue) {
        return this.pieChartRenderer.getValueForAngle(angle, selectedValue);
    }

    public float getCircleFillRatio() {
        return this.pieChartRenderer.getCircleFillRatio();
    }

    public void setCircleFillRatio(float fillRatio) {
        this.pieChartRenderer.setCircleFillRatio(fillRatio);
        ViewCompat.postInvalidateOnAnimation(this);
    }
}
