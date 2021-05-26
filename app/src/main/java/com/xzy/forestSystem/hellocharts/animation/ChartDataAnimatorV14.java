package com.xzy.forestSystem.hellocharts.animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;

import com.xzy.forestSystem.hellocharts.view.Chart;

@SuppressLint({"NewApi"})
public class ChartDataAnimatorV14 implements ChartDataAnimator, Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    private ChartAnimationListener animationListener = new DummyChartAnimationListener();
    private ValueAnimator animator;
    private final Chart chart;

    public ChartDataAnimatorV14(Chart chart2) {
        this.chart = chart2;
        this.animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.animator.addListener(this);
        this.animator.addUpdateListener(this);
    }

    @Override // lecho.lib.hellocharts.animation.ChartDataAnimator
    public void startAnimation(long duration) {
        if (duration >= 0) {
            this.animator.setDuration(duration);
        } else {
            this.animator.setDuration(500L);
        }
        this.animator.start();
    }

    @Override // lecho.lib.hellocharts.animation.ChartDataAnimator
    public void cancelAnimation() {
        this.animator.cancel();
    }

    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
    public void onAnimationUpdate(ValueAnimator animation) {
        this.chart.animationDataUpdate(animation.getAnimatedFraction());
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animation) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animation) {
        this.chart.animationDataFinished();
        this.animationListener.onAnimationFinished();
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator animation) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animation) {
        this.animationListener.onAnimationStarted();
    }

    @Override // lecho.lib.hellocharts.animation.ChartDataAnimator
    public boolean isAnimationStarted() {
        return this.animator.isStarted();
    }

    @Override // lecho.lib.hellocharts.animation.ChartDataAnimator
    public void setChartAnimationListener(ChartAnimationListener animationListener2) {
        if (animationListener2 == null) {
            this.animationListener = new DummyChartAnimationListener();
        } else {
            this.animationListener = animationListener2;
        }
    }
}
