package com.xzy.forestSystem.hellocharts.renderer;

import android.content.Context;
import android.graphics.Canvas;

import com.xzy.forestSystem.hellocharts.model.Viewport;
import com.xzy.forestSystem.hellocharts.view.Chart;

import java.util.ArrayList;
import java.util.List;

public class ComboChartRenderer extends AbstractChartRenderer {
    protected List<ChartRenderer> renderers = new ArrayList();
    protected Viewport unionViewport = new Viewport();

    public ComboChartRenderer(Context context, Chart chart) {
        super(context, chart);
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartSizeChanged() {
        for (ChartRenderer renderer : this.renderers) {
            renderer.onChartSizeChanged();
        }
    }

    @Override // lecho.lib.hellocharts.renderer.AbstractChartRenderer, lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartDataChanged() {
        super.onChartDataChanged();
        for (ChartRenderer renderer : this.renderers) {
            renderer.onChartDataChanged();
        }
        onChartViewportChanged();
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void onChartViewportChanged() {
        if (this.isViewportCalculationEnabled) {
            int rendererIndex = 0;
            for (ChartRenderer renderer : this.renderers) {
                renderer.onChartViewportChanged();
                if (rendererIndex == 0) {
                    this.unionViewport.set(renderer.getMaximumViewport());
                } else {
                    this.unionViewport.union(renderer.getMaximumViewport());
                }
                rendererIndex++;
            }
            this.computator.setMaxViewport(this.unionViewport);
            this.computator.setCurrentViewport(this.unionViewport);
        }
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void draw(Canvas canvas) {
        for (ChartRenderer renderer : this.renderers) {
            renderer.draw(canvas);
        }
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public void drawUnclipped(Canvas canvas) {
        for (ChartRenderer renderer : this.renderers) {
            renderer.drawUnclipped(canvas);
        }
    }

    @Override // lecho.lib.hellocharts.renderer.ChartRenderer
    public boolean checkTouch(float touchX, float touchY) {
        this.selectedValue.clear();
        int rendererIndex = this.renderers.size() - 1;
        while (true) {
            if (rendererIndex < 0) {
                break;
            }
            ChartRenderer renderer = this.renderers.get(rendererIndex);
            if (renderer.checkTouch(touchX, touchY)) {
                this.selectedValue.set(renderer.getSelectedValue());
                break;
            }
            rendererIndex--;
        }
        for (int rendererIndex2 = rendererIndex - 1; rendererIndex2 >= 0; rendererIndex2--) {
            this.renderers.get(rendererIndex2).clearTouch();
        }
        return isTouched();
    }

    @Override // lecho.lib.hellocharts.renderer.AbstractChartRenderer, lecho.lib.hellocharts.renderer.ChartRenderer
    public void clearTouch() {
        for (ChartRenderer renderer : this.renderers) {
            renderer.clearTouch();
        }
        this.selectedValue.clear();
    }
}
