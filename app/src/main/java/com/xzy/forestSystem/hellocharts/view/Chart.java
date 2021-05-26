package com.xzy.forestSystem.hellocharts.view;

import com.xzy.forestSystem.hellocharts.animation.ChartAnimationListener;
import com.xzy.forestSystem.hellocharts.computator.ChartComputator;
import com.xzy.forestSystem.hellocharts.gesture.ChartTouchHandler;
import com.xzy.forestSystem.hellocharts.gesture.ContainerScrollType;
import com.xzy.forestSystem.hellocharts.gesture.ZoomType;
import com.xzy.forestSystem.hellocharts.listener.ViewportChangeListener;
import com.xzy.forestSystem.hellocharts.model.ChartData;
import com.xzy.forestSystem.hellocharts.model.SelectedValue;
import com.xzy.forestSystem.hellocharts.model.Viewport;
import com.xzy.forestSystem.hellocharts.renderer.AxesRenderer;
import com.xzy.forestSystem.hellocharts.renderer.ChartRenderer;

public interface Chart {
    void animationDataFinished();

    void animationDataUpdate(float f);

    void callTouchListener();

    void cancelDataAnimation();

    AxesRenderer getAxesRenderer();

    ChartComputator getChartComputator();

    ChartData getChartData();

    ChartRenderer getChartRenderer();

    Viewport getCurrentViewport();

    float getMaxZoom();

    Viewport getMaximumViewport();

    SelectedValue getSelectedValue();

    ChartTouchHandler getTouchHandler();

    float getZoomLevel();

    ZoomType getZoomType();

    boolean isContainerScrollEnabled();

    boolean isInteractive();

    boolean isScrollEnabled();

    boolean isValueSelectionEnabled();

    boolean isValueTouchEnabled();

    boolean isViewportCalculationEnabled();

    boolean isZoomEnabled();

    void moveTo(float f, float f2);

    void moveToWithAnimation(float f, float f2);

    void resetViewports();

    void selectValue(SelectedValue selectedValue);

    void setChartRenderer(ChartRenderer chartRenderer);

    void setContainerScrollEnabled(boolean z, ContainerScrollType containerScrollType);

    void setCurrentViewport(Viewport viewport);

    void setCurrentViewportWithAnimation(Viewport viewport);

    void setCurrentViewportWithAnimation(Viewport viewport, long j);

    void setDataAnimationListener(ChartAnimationListener chartAnimationListener);

    void setInteractive(boolean z);

    void setMaxZoom(float f);

    void setMaximumViewport(Viewport viewport);

    void setScrollEnabled(boolean z);

    void setValueSelectionEnabled(boolean z);

    void setValueTouchEnabled(boolean z);

    void setViewportAnimationListener(ChartAnimationListener chartAnimationListener);

    void setViewportCalculationEnabled(boolean z);

    void setViewportChangeListener(ViewportChangeListener viewportChangeListener);

    void setZoomEnabled(boolean z);

    void setZoomLevel(float f, float f2, float f3);

    void setZoomLevelWithAnimation(float f, float f2, float f3);

    void setZoomType(ZoomType zoomType);

    void startDataAnimation();

    void startDataAnimation(long j);
}
