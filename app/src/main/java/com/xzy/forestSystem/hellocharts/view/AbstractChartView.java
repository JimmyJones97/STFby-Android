package com.xzy.forestSystem.hellocharts.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.ViewCompat;

import com.xzy.forestSystem.hellocharts.animation.ChartAnimationListener;
import com.xzy.forestSystem.hellocharts.animation.ChartDataAnimator;
import com.xzy.forestSystem.hellocharts.animation.ChartDataAnimatorV14;
import com.xzy.forestSystem.hellocharts.animation.ChartDataAnimatorV8;
import com.xzy.forestSystem.hellocharts.animation.ChartViewportAnimator;
import com.xzy.forestSystem.hellocharts.animation.ChartViewportAnimatorV14;
import com.xzy.forestSystem.hellocharts.animation.ChartViewportAnimatorV8;
import com.xzy.forestSystem.hellocharts.computator.ChartComputator;
import com.xzy.forestSystem.hellocharts.gesture.ChartTouchHandler;
import com.xzy.forestSystem.hellocharts.gesture.ContainerScrollType;
import com.xzy.forestSystem.hellocharts.gesture.ZoomType;
import com.xzy.forestSystem.hellocharts.listener.ViewportChangeListener;
import com.xzy.forestSystem.hellocharts.model.SelectedValue;
import com.xzy.forestSystem.hellocharts.model.Viewport;
import com.xzy.forestSystem.hellocharts.renderer.AxesRenderer;
import com.xzy.forestSystem.hellocharts.renderer.ChartRenderer;
import com.xzy.forestSystem.hellocharts.util.ChartUtils;

public abstract class AbstractChartView extends View implements Chart {
    protected AxesRenderer axesRenderer;
    protected ChartComputator chartComputator;
    protected ChartRenderer chartRenderer;
    protected ContainerScrollType containerScrollType;
    protected ChartDataAnimator dataAnimator;
    protected boolean isContainerScrollEnabled;
    protected boolean isInteractive;
    protected ChartTouchHandler touchHandler;
    protected ChartViewportAnimator viewportAnimator;

    public AbstractChartView(Context context) {
        this(context, null, 0);
    }

    public AbstractChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbstractChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.isInteractive = true;
        this.isContainerScrollEnabled = false;
        this.chartComputator = new ChartComputator();
        this.touchHandler = new ChartTouchHandler(context, this);
        this.axesRenderer = new AxesRenderer(context, this);
        if (Build.VERSION.SDK_INT < 14) {
            this.dataAnimator = new ChartDataAnimatorV8(this);
            this.viewportAnimator = new ChartViewportAnimatorV8(this);
            return;
        }
        this.viewportAnimator = new ChartViewportAnimatorV14(this);
        this.dataAnimator = new ChartDataAnimatorV14(this);
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        this.chartComputator.setContentRect(getWidth(), getHeight(), getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        this.chartRenderer.onChartSizeChanged();
        this.axesRenderer.onChartSizeChanged();
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isEnabled()) {
            this.axesRenderer.drawInBackground(canvas);
            int clipRestoreCount = canvas.save();
            canvas.clipRect(this.chartComputator.getContentRectMinusAllMargins());
            this.chartRenderer.draw(canvas);
            canvas.restoreToCount(clipRestoreCount);
            this.chartRenderer.drawUnclipped(canvas);
            this.axesRenderer.drawInForeground(canvas);
            return;
        }
        canvas.drawColor(ChartUtils.DEFAULT_COLOR);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        boolean needInvalidate;
        super.onTouchEvent(event);
        if (!this.isInteractive) {
            return false;
        }
        if (this.isContainerScrollEnabled) {
            needInvalidate = this.touchHandler.handleTouchEvent(event, getParent(), this.containerScrollType);
        } else {
            needInvalidate = this.touchHandler.handleTouchEvent(event);
        }
        if (needInvalidate) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
        return true;
    }

    @Override // android.view.View
    public void computeScroll() {
        super.computeScroll();
        if (this.isInteractive && this.touchHandler.computeScroll()) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void startDataAnimation() {
        this.dataAnimator.startAnimation(Long.MIN_VALUE);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void startDataAnimation(long duration) {
        this.dataAnimator.startAnimation(duration);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void cancelDataAnimation() {
        this.dataAnimator.cancelAnimation();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void animationDataUpdate(float scale) {
        getChartData().update(scale);
        this.chartRenderer.onChartViewportChanged();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void animationDataFinished() {
        getChartData().finish();
        this.chartRenderer.onChartViewportChanged();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setDataAnimationListener(ChartAnimationListener animationListener) {
        this.dataAnimator.setChartAnimationListener(animationListener);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setViewportAnimationListener(ChartAnimationListener animationListener) {
        this.viewportAnimator.setChartAnimationListener(animationListener);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setViewportChangeListener(ViewportChangeListener viewportChangeListener) {
        this.chartComputator.setViewportChangeListener(viewportChangeListener);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public ChartRenderer getChartRenderer() {
        return this.chartRenderer;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setChartRenderer(ChartRenderer renderer) {
        this.chartRenderer = renderer;
        resetRendererAndTouchHandler();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public AxesRenderer getAxesRenderer() {
        return this.axesRenderer;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public ChartComputator getChartComputator() {
        return this.chartComputator;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public ChartTouchHandler getTouchHandler() {
        return this.touchHandler;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public boolean isInteractive() {
        return this.isInteractive;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setInteractive(boolean isInteractive2) {
        this.isInteractive = isInteractive2;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public boolean isZoomEnabled() {
        return this.touchHandler.isZoomEnabled();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setZoomEnabled(boolean isZoomEnabled) {
        this.touchHandler.setZoomEnabled(isZoomEnabled);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public boolean isScrollEnabled() {
        return this.touchHandler.isScrollEnabled();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setScrollEnabled(boolean isScrollEnabled) {
        this.touchHandler.setScrollEnabled(isScrollEnabled);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void moveTo(float x, float y) {
        setCurrentViewport(computeScrollViewport(x, y));
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void moveToWithAnimation(float x, float y) {
        setCurrentViewportWithAnimation(computeScrollViewport(x, y));
    }

    private Viewport computeScrollViewport(float x, float y) {
        Viewport maxViewport = getMaximumViewport();
        Viewport currentViewport = getCurrentViewport();
        Viewport scrollViewport = new Viewport(currentViewport);
        if (maxViewport.contains(x, y)) {
            float width = currentViewport.width();
            float height = currentViewport.height();
            float left = Math.max(maxViewport.left, Math.min(x - (width / 2.0f), maxViewport.right - width));
            float top = Math.max(maxViewport.bottom + height, Math.min(y + (height / 2.0f), maxViewport.top));
            scrollViewport.set(left, top, left + width, top - height);
        }
        return scrollViewport;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public boolean isValueTouchEnabled() {
        return this.touchHandler.isValueTouchEnabled();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setValueTouchEnabled(boolean isValueTouchEnabled) {
        this.touchHandler.setValueTouchEnabled(isValueTouchEnabled);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public ZoomType getZoomType() {
        return this.touchHandler.getZoomType();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setZoomType(ZoomType zoomType) {
        this.touchHandler.setZoomType(zoomType);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public float getMaxZoom() {
        return this.chartComputator.getMaxZoom();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setMaxZoom(float maxZoom) {
        this.chartComputator.setMaxZoom(maxZoom);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public float getZoomLevel() {
        Viewport maxViewport = getMaximumViewport();
        Viewport currentViewport = getCurrentViewport();
        return Math.max(maxViewport.width() / currentViewport.width(), maxViewport.height() / currentViewport.height());
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setZoomLevel(float x, float y, float zoomLevel) {
        setCurrentViewport(computeZoomViewport(x, y, zoomLevel));
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setZoomLevelWithAnimation(float x, float y, float zoomLevel) {
        setCurrentViewportWithAnimation(computeZoomViewport(x, y, zoomLevel));
    }

    private Viewport computeZoomViewport(float x, float y, float zoomLevel) {
        Viewport maxViewport = getMaximumViewport();
        Viewport zoomViewport = new Viewport(getMaximumViewport());
        if (maxViewport.contains(x, y)) {
            if (zoomLevel < 1.0f) {
                zoomLevel = 1.0f;
            } else if (zoomLevel > getMaxZoom()) {
                zoomLevel = getMaxZoom();
            }
            float newWidth = zoomViewport.width() / zoomLevel;
            float newHeight = zoomViewport.height() / zoomLevel;
            float halfWidth = newWidth / 2.0f;
            float halfHeight = newHeight / 2.0f;
            float left = x - halfWidth;
            float right = x + halfWidth;
            float top = y + halfHeight;
            float bottom = y - halfHeight;
            if (left < maxViewport.left) {
                left = maxViewport.left;
                right = left + newWidth;
            } else if (right > maxViewport.right) {
                right = maxViewport.right;
                left = right - newWidth;
            }
            if (top > maxViewport.top) {
                top = maxViewport.top;
                bottom = top - newHeight;
            } else if (bottom < maxViewport.bottom) {
                bottom = maxViewport.bottom;
                top = bottom + newHeight;
            }
            ZoomType zoomType = getZoomType();
            if (ZoomType.HORIZONTAL_AND_VERTICAL == zoomType) {
                zoomViewport.set(left, top, right, bottom);
            } else if (ZoomType.HORIZONTAL == zoomType) {
                zoomViewport.left = left;
                zoomViewport.right = right;
            } else if (ZoomType.VERTICAL == zoomType) {
                zoomViewport.top = top;
                zoomViewport.bottom = bottom;
            }
        }
        return zoomViewport;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public Viewport getMaximumViewport() {
        return this.chartRenderer.getMaximumViewport();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setMaximumViewport(Viewport maxViewport) {
        this.chartRenderer.setMaximumViewport(maxViewport);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setCurrentViewportWithAnimation(Viewport targetViewport) {
        if (targetViewport != null) {
            this.viewportAnimator.cancelAnimation();
            this.viewportAnimator.startAnimation(getCurrentViewport(), targetViewport);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setCurrentViewportWithAnimation(Viewport targetViewport, long duration) {
        if (targetViewport != null) {
            this.viewportAnimator.cancelAnimation();
            this.viewportAnimator.startAnimation(getCurrentViewport(), targetViewport, duration);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public Viewport getCurrentViewport() {
        return getChartRenderer().getCurrentViewport();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setCurrentViewport(Viewport targetViewport) {
        if (targetViewport != null) {
            this.chartRenderer.setCurrentViewport(targetViewport);
        }
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void resetViewports() {
        this.chartRenderer.setMaximumViewport(null);
        this.chartRenderer.setCurrentViewport(null);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public boolean isViewportCalculationEnabled() {
        return this.chartRenderer.isViewportCalculationEnabled();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setViewportCalculationEnabled(boolean isEnabled) {
        this.chartRenderer.setViewportCalculationEnabled(isEnabled);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public boolean isValueSelectionEnabled() {
        return this.touchHandler.isValueSelectionEnabled();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setValueSelectionEnabled(boolean isValueSelectionEnabled) {
        this.touchHandler.setValueSelectionEnabled(isValueSelectionEnabled);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void selectValue(SelectedValue selectedValue) {
        this.chartRenderer.selectValue(selectedValue);
        callTouchListener();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public SelectedValue getSelectedValue() {
        return this.chartRenderer.getSelectedValue();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public boolean isContainerScrollEnabled() {
        return this.isContainerScrollEnabled;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void setContainerScrollEnabled(boolean isContainerScrollEnabled2, ContainerScrollType containerScrollType2) {
        this.isContainerScrollEnabled = isContainerScrollEnabled2;
        this.containerScrollType = containerScrollType2;
    }

    /* access modifiers changed from: protected */
    public void onChartDataChange() {
        this.chartComputator.resetContentRect();
        this.chartRenderer.onChartDataChanged();
        this.axesRenderer.onChartDataChanged();
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /* access modifiers changed from: protected */
    public void resetRendererAndTouchHandler() {
        this.chartRenderer.resetRenderer();
        this.axesRenderer.resetRenderer();
        this.touchHandler.resetTouchHandler();
    }

    @Override // android.view.View
    public boolean canScrollHorizontally(int direction) {
        if (((double) getZoomLevel()) <= 1.0d) {
            return false;
        }
        Viewport currentViewport = getCurrentViewport();
        Viewport maximumViewport = getMaximumViewport();
        return direction < 0 ? currentViewport.left > maximumViewport.left : currentViewport.right < maximumViewport.right;
    }
}
