package com.xzy.forestSystem.hellocharts.renderer;

import android.graphics.Canvas;

import com.xzy.forestSystem.hellocharts.model.SelectedValue;
import com.xzy.forestSystem.hellocharts.model.Viewport;

public interface ChartRenderer {
    boolean checkTouch(float f, float f2);

    void clearTouch();

    void draw(Canvas canvas);

    void drawUnclipped(Canvas canvas);

    Viewport getCurrentViewport();

    Viewport getMaximumViewport();

    SelectedValue getSelectedValue();

    boolean isTouched();

    boolean isViewportCalculationEnabled();

    void onChartDataChanged();

    void onChartSizeChanged();

    void onChartViewportChanged();

    void resetRenderer();

    void selectValue(SelectedValue selectedValue);

    void setCurrentViewport(Viewport viewport);

    void setMaximumViewport(Viewport viewport);

    void setViewportCalculationEnabled(boolean z);
}
