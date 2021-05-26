package com.xzy.forestSystem.hellocharts.computator;

import com.xzy.forestSystem.hellocharts.model.Viewport;

public class PreviewChartComputator extends ChartComputator {
    @Override // lecho.lib.hellocharts.computator.ChartComputator
    public float computeRawX(float valueX) {
        return ((float) this.contentRectMinusAllMargins.left) + ((valueX - this.maxViewport.left) * (((float) this.contentRectMinusAllMargins.width()) / this.maxViewport.width()));
    }

    @Override // lecho.lib.hellocharts.computator.ChartComputator
    public float computeRawY(float valueY) {
        return ((float) this.contentRectMinusAllMargins.bottom) - ((valueY - this.maxViewport.bottom) * (((float) this.contentRectMinusAllMargins.height()) / this.maxViewport.height()));
    }

    @Override // lecho.lib.hellocharts.computator.ChartComputator
    public Viewport getVisibleViewport() {
        return this.maxViewport;
    }

    @Override // lecho.lib.hellocharts.computator.ChartComputator
    public void setVisibleViewport(Viewport visibleViewport) {
        setMaxViewport(visibleViewport);
    }

    @Override // lecho.lib.hellocharts.computator.ChartComputator
    public void constrainViewport(float left, float top, float right, float bottom) {
        super.constrainViewport(left, top, right, bottom);
        this.viewportChangeListener.onViewportChanged(this.currentViewport);
    }
}
