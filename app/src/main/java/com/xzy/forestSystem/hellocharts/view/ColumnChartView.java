package com.xzy.forestSystem.hellocharts.view;

import android.content.Context;
import android.util.AttributeSet;

import com.xzy.forestSystem.hellocharts.listener.ColumnChartOnValueSelectListener;
import com.xzy.forestSystem.hellocharts.listener.DummyColumnChartOnValueSelectListener;
import com.xzy.forestSystem.hellocharts.model.ColumnChartData;
import com.xzy.forestSystem.hellocharts.model.SelectedValue;
import com.xzy.forestSystem.hellocharts.provider.ColumnChartDataProvider;
import com.xzy.forestSystem.hellocharts.renderer.ColumnChartRenderer;

public class ColumnChartView extends AbstractChartView implements ColumnChartDataProvider {
    private static final String TAG = "ColumnChartView";
    private ColumnChartData data;
    private ColumnChartOnValueSelectListener onValueTouchListener;

    public ColumnChartView(Context context) {
        this(context, null, 0);
    }

    public ColumnChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColumnChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.onValueTouchListener = new DummyColumnChartOnValueSelectListener();
        setChartRenderer(new ColumnChartRenderer(context, this, this));
        setColumnChartData(ColumnChartData.generateDummyData());
    }

    @Override // lecho.lib.hellocharts.provider.ColumnChartDataProvider
    public ColumnChartData getColumnChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.provider.ColumnChartDataProvider
    public void setColumnChartData(ColumnChartData data2) {
        if (data2 == null) {
            this.data = ColumnChartData.generateDummyData();
        } else {
            this.data = data2;
        }
        super.onChartDataChange();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public ColumnChartData getChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void callTouchListener() {
        SelectedValue selectedValue = this.chartRenderer.getSelectedValue();
        if (selectedValue.isSet()) {
            this.onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), this.data.getColumns().get(selectedValue.getFirstIndex()).getValues().get(selectedValue.getSecondIndex()));
            return;
        }
        this.onValueTouchListener.onValueDeselected();
    }

    public ColumnChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(ColumnChartOnValueSelectListener touchListener) {
        if (touchListener != null) {
            this.onValueTouchListener = touchListener;
        }
    }
}
