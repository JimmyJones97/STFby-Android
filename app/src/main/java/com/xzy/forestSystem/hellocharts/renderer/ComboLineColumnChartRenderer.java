package com.xzy.forestSystem.hellocharts.renderer;

import android.content.Context;

import com.xzy.forestSystem.hellocharts.provider.ColumnChartDataProvider;
import com.xzy.forestSystem.hellocharts.provider.LineChartDataProvider;
import com.xzy.forestSystem.hellocharts.view.Chart;

public class ComboLineColumnChartRenderer extends ComboChartRenderer {
    private ColumnChartRenderer columnChartRenderer;
    private LineChartRenderer lineChartRenderer;

    public ComboLineColumnChartRenderer(Context context, Chart chart, ColumnChartDataProvider columnChartDataProvider, LineChartDataProvider lineChartDataProvider) {
        this(context, chart, new ColumnChartRenderer(context, chart, columnChartDataProvider), new LineChartRenderer(context, chart, lineChartDataProvider));
    }

    public ComboLineColumnChartRenderer(Context context, Chart chart, ColumnChartRenderer columnChartRenderer2, LineChartDataProvider lineChartDataProvider) {
        this(context, chart, columnChartRenderer2, new LineChartRenderer(context, chart, lineChartDataProvider));
    }

    public ComboLineColumnChartRenderer(Context context, Chart chart, ColumnChartDataProvider columnChartDataProvider, LineChartRenderer lineChartRenderer2) {
        this(context, chart, new ColumnChartRenderer(context, chart, columnChartDataProvider), lineChartRenderer2);
    }

    public ComboLineColumnChartRenderer(Context context, Chart chart, ColumnChartRenderer columnChartRenderer2, LineChartRenderer lineChartRenderer2) {
        super(context, chart);
        this.columnChartRenderer = columnChartRenderer2;
        this.lineChartRenderer = lineChartRenderer2;
        this.renderers.add(this.columnChartRenderer);
        this.renderers.add(this.lineChartRenderer);
    }
}
