package com.xzy.forestSystem.hellocharts.view;

import android.content.Context;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;

import com.xzy.forestSystem.hellocharts.computator.PreviewChartComputator;
import com.xzy.forestSystem.hellocharts.gesture.PreviewChartTouchHandler;
import com.xzy.forestSystem.hellocharts.model.ColumnChartData;
import com.xzy.forestSystem.hellocharts.renderer.PreviewColumnChartRenderer;

public class PreviewColumnChartView extends ColumnChartView {
    private static final String TAG = "ColumnChartView";
    protected PreviewColumnChartRenderer previewChartRenderer;

    public PreviewColumnChartView(Context context) {
        this(context, null, 0);
    }

    public PreviewColumnChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreviewColumnChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.chartComputator = new PreviewChartComputator();
        this.previewChartRenderer = new PreviewColumnChartRenderer(context, this, this);
        this.touchHandler = new PreviewChartTouchHandler(context, this);
        setChartRenderer(this.previewChartRenderer);
        setColumnChartData(ColumnChartData.generateDummyData());
    }

    public int getPreviewColor() {
        return this.previewChartRenderer.getPreviewColor();
    }

    public void setPreviewColor(int color) {
        this.previewChartRenderer.setPreviewColor(color);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override // lecho.lib.hellocharts.view.AbstractChartView, android.view.View
    public boolean canScrollHorizontally(int direction) {
        int offset = computeHorizontalScrollOffset();
        int range = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
        if (range == 0) {
            return false;
        }
        return direction < 0 ? offset > 0 : offset < range + -1;
    }
}
