package com.xzy.forestSystem.hellocharts.gesture;

import android.content.Context;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import androidx.core.widget.ScrollerCompat;

import com.xzy.forestSystem.hellocharts.view.PieChartView;

public class PieChartTouchHandler extends ChartTouchHandler {
    public static final int FLING_VELOCITY_DOWNSCALE = 4;
    private boolean isRotationEnabled = true;
    protected PieChartView pieChart;
    protected ScrollerCompat scroller;

    public PieChartTouchHandler(Context context, PieChartView chart) {
        super(context, chart);
        this.pieChart = chart;
        this.scroller = ScrollerCompat.create(context);
        this.gestureDetector = new GestureDetector(context, new ChartGestureListener(this, null));
        this.scaleGestureDetector = new ScaleGestureDetector(context, new ChartScaleGestureListener(this, null));
        this.isZoomEnabled = false;
    }

    @Override // lecho.lib.hellocharts.gesture.ChartTouchHandler
    public boolean computeScroll() {
        if (this.isRotationEnabled && this.scroller.computeScrollOffset()) {
            this.pieChart.setChartRotation(this.scroller.getCurrY(), false);
        }
        return false;
    }

    @Override // lecho.lib.hellocharts.gesture.ChartTouchHandler
    public boolean handleTouchEvent(MotionEvent event) {
        boolean needInvalidate = super.handleTouchEvent(event);
        if (this.isRotationEnabled) {
            return this.gestureDetector.onTouchEvent(event) || needInvalidate;
        }
        return needInvalidate;
    }

    public boolean isRotationEnabled() {
        return this.isRotationEnabled;
    }

    public void setRotationEnabled(boolean isRotationEnabled2) {
        this.isRotationEnabled = isRotationEnabled2;
    }

    private class ChartScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private ChartScaleGestureListener() {
        }

        /* synthetic */ ChartScaleGestureListener(PieChartTouchHandler pieChartTouchHandler, ChartScaleGestureListener chartScaleGestureListener) {
            this();
        }

        @Override // android.view.ScaleGestureDetector.SimpleOnScaleGestureListener, android.view.ScaleGestureDetector.OnScaleGestureListener
        public boolean onScale(ScaleGestureDetector detector) {
            return false;
        }
    }

    private class ChartGestureListener extends GestureDetector.SimpleOnGestureListener {
        private ChartGestureListener() {
        }

        /* synthetic */ ChartGestureListener(PieChartTouchHandler pieChartTouchHandler, ChartGestureListener chartGestureListener) {
            this();
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent e) {
            if (!PieChartTouchHandler.this.isRotationEnabled) {
                return false;
            }
            PieChartTouchHandler.this.scroller.abortAnimation();
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent e) {
            return false;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!PieChartTouchHandler.this.isRotationEnabled) {
                return false;
            }
            RectF circleOval = PieChartTouchHandler.this.pieChart.getCircleOval();
            PieChartTouchHandler.this.pieChart.setChartRotation(PieChartTouchHandler.this.pieChart.getChartRotation() - (((int) vectorToScalarScroll(distanceX, distanceY, e2.getX() - circleOval.centerX(), e2.getY() - circleOval.centerY())) / 4), false);
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (!PieChartTouchHandler.this.isRotationEnabled) {
                return false;
            }
            RectF circleOval = PieChartTouchHandler.this.pieChart.getCircleOval();
            float scrollTheta = vectorToScalarScroll(velocityX, velocityY, e2.getX() - circleOval.centerX(), e2.getY() - circleOval.centerY());
            PieChartTouchHandler.this.scroller.abortAnimation();
            PieChartTouchHandler.this.scroller.fling(0, PieChartTouchHandler.this.pieChart.getChartRotation(), 0, ((int) scrollTheta) / 4, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            return true;
        }

        private float vectorToScalarScroll(float dx, float dy, float x, float y) {
            return ((float) Math.sqrt((double) ((dx * dx) + (dy * dy)))) * Math.signum(((-y) * dx) + (x * dy));
        }
    }
}
