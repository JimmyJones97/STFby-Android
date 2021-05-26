package com.xzy.forestSystem.hellocharts.view;

import android.content.Context;
import android.util.AttributeSet;

import com.xzy.forestSystem.hellocharts.listener.DummyLineChartOnValueSelectListener;
import com.xzy.forestSystem.hellocharts.listener.LineChartOnValueSelectListener;
import com.xzy.forestSystem.hellocharts.model.ChartData;
import com.xzy.forestSystem.hellocharts.model.LineChartData;
import com.xzy.forestSystem.hellocharts.model.SelectedValue;
import com.xzy.forestSystem.hellocharts.provider.LineChartDataProvider;
import com.xzy.forestSystem.hellocharts.renderer.LineChartRenderer;

public class LineChartView extends AbstractChartView implements LineChartDataProvider {
    private static final String TAG = "LineChartView";
    protected LineChartData data;
    protected LineChartOnValueSelectListener onValueTouchListener;

    public LineChartView(Context context) {
        this(context, null, 0);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.onValueTouchListener = new DummyLineChartOnValueSelectListener();
        setChartRenderer(new LineChartRenderer(context, this, this));
        setLineChartData(LineChartData.generateDummyData());
    }

    @Override // lecho.lib.hellocharts.provider.LineChartDataProvider
    public LineChartData getLineChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.provider.LineChartDataProvider
    public void setLineChartData(LineChartData data2) {
        if (data2 == null) {
            this.data = LineChartData.generateDummyData();
        } else {
            this.data = data2;
        }
        super.onChartDataChange();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public ChartData getChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void callTouchListener() {
        SelectedValue selectedValue = this.chartRenderer.getSelectedValue();
        if (selectedValue.isSet()) {
            this.onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), this.data.getLines().get(selectedValue.getFirstIndex()).getValues().get(selectedValue.getSecondIndex()));
            return;
        }
        this.onValueTouchListener.onValueDeselected();
    }

    public LineChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(LineChartOnValueSelectListener touchListener) {
        if (touchListener != null) {
            this.onValueTouchListener = touchListener;
        }
    }
}
