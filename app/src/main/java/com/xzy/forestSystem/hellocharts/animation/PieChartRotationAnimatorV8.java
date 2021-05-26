package com.xzy.forestSystem.hellocharts.animation;

import android.os.Handler;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.xzy.forestSystem.hellocharts.view.PieChartView;

public class PieChartRotationAnimatorV8 implements PieChartRotationAnimator {
    private ChartAnimationListener animationListener;
    final PieChartView chart;
    final long duration;
    final Handler handler;
    final Interpolator interpolator;
    boolean isAnimationStarted;
    private final Runnable runnable;
    long start;
    private float startRotation;
    private float targetRotation;

    public PieChartRotationAnimatorV8(PieChartView chart2) {
        this(chart2, 200);
    }

    public PieChartRotationAnimatorV8(PieChartView chart2, long duration2) {
        this.interpolator = new AccelerateDecelerateInterpolator();
        this.isAnimationStarted = false;
        this.startRotation = 0.0f;
        this.targetRotation = 0.0f;
        this.animationListener = new DummyChartAnimationListener();
        this.runnable = new Runnable() { // from class: lecho.lib.hellocharts.animation.PieChartRotationAnimatorV8.1
            @Override // java.lang.Runnable
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - PieChartRotationAnimatorV8.this.start;
                if (elapsed > PieChartRotationAnimatorV8.this.duration) {
                    PieChartRotationAnimatorV8.this.isAnimationStarted = false;
                    PieChartRotationAnimatorV8.this.handler.removeCallbacks(PieChartRotationAnimatorV8.this.runnable);
                    PieChartRotationAnimatorV8.this.chart.setChartRotation((int) PieChartRotationAnimatorV8.this.targetRotation, false);
                    PieChartRotationAnimatorV8.this.animationListener.onAnimationFinished();
                    return;
                }
                PieChartRotationAnimatorV8.this.chart.setChartRotation((int) ((((PieChartRotationAnimatorV8.this.startRotation + ((PieChartRotationAnimatorV8.this.targetRotation - PieChartRotationAnimatorV8.this.startRotation) * Math.min(PieChartRotationAnimatorV8.this.interpolator.getInterpolation(((float) elapsed) / ((float) PieChartRotationAnimatorV8.this.duration)), 1.0f))) % 360.0f) + 360.0f) % 360.0f), false);
                PieChartRotationAnimatorV8.this.handler.postDelayed(this, 16);
            }
        };
        this.chart = chart2;
        this.duration = duration2;
        this.handler = new Handler();
    }

    @Override // lecho.lib.hellocharts.animation.PieChartRotationAnimator
    public void startAnimation(float startRotation2, float targetRotation2) {
        this.startRotation = ((startRotation2 % 360.0f) + 360.0f) % 360.0f;
        this.targetRotation = ((targetRotation2 % 360.0f) + 360.0f) % 360.0f;
        this.isAnimationStarted = true;
        this.animationListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        this.handler.post(this.runnable);
    }

    @Override // lecho.lib.hellocharts.animation.PieChartRotationAnimator
    public void cancelAnimation() {
        this.isAnimationStarted = false;
        this.handler.removeCallbacks(this.runnable);
        this.chart.setChartRotation((int) this.targetRotation, false);
        this.animationListener.onAnimationFinished();
    }

    @Override // lecho.lib.hellocharts.animation.PieChartRotationAnimator
    public boolean isAnimationStarted() {
        return this.isAnimationStarted;
    }

    @Override // lecho.lib.hellocharts.animation.PieChartRotationAnimator
    public void setChartAnimationListener(ChartAnimationListener animationListener2) {
        if (animationListener2 == null) {
            this.animationListener = new DummyChartAnimationListener();
        } else {
            this.animationListener = animationListener2;
        }
    }
}
