package com.xzy.forestSystem.hellocharts.view;

import android.content.Context;
import android.util.AttributeSet;

import com.xzy.forestSystem.hellocharts.listener.ComboLineColumnChartOnValueSelectListener;
import com.xzy.forestSystem.hellocharts.listener.DummyCompoLineColumnChartOnValueSelectListener;
import com.xzy.forestSystem.hellocharts.model.ChartData;
import com.xzy.forestSystem.hellocharts.model.ColumnChartData;
import com.xzy.forestSystem.hellocharts.model.ComboLineColumnChartData;
import com.xzy.forestSystem.hellocharts.model.LineChartData;
import com.xzy.forestSystem.hellocharts.model.SelectedValue;
import com.xzy.forestSystem.hellocharts.provider.ColumnChartDataProvider;
import com.xzy.forestSystem.hellocharts.provider.ComboLineColumnChartDataProvider;
import com.xzy.forestSystem.hellocharts.provider.LineChartDataProvider;
import com.xzy.forestSystem.hellocharts.renderer.ColumnChartRenderer;
import com.xzy.forestSystem.hellocharts.renderer.ComboLineColumnChartRenderer;
import com.xzy.forestSystem.hellocharts.renderer.LineChartRenderer;

public class ComboLineColumnChartView extends AbstractChartView implements ComboLineColumnChartDataProvider {
    private static final String TAG = "ComboLineColumnChartView";
    protected ColumnChartDataProvider columnChartDataProvider;
    protected ComboLineColumnChartData data;
    protected LineChartDataProvider lineChartDataProvider;
    protected ComboLineColumnChartOnValueSelectListener onValueTouchListener;

    public ComboLineColumnChartView(Context context) {
        this(context, null, 0);
    }

    public ComboLineColumnChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ComboLineColumnChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.columnChartDataProvider = new ComboColumnChartDataProvider(this, null);
        this.lineChartDataProvider = new ComboLineChartDataProvider(this, null);
        this.onValueTouchListener = new DummyCompoLineColumnChartOnValueSelectListener();
        setChartRenderer(new ComboLineColumnChartRenderer(context, this, this.columnChartDataProvider, this.lineChartDataProvider));
        setComboLineColumnChartData(ComboLineColumnChartData.generateDummyData());
    }

    @Override // lecho.lib.hellocharts.provider.ComboLineColumnChartDataProvider
    public ComboLineColumnChartData getComboLineColumnChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.provider.ComboLineColumnChartDataProvider
    public void setComboLineColumnChartData(ComboLineColumnChartData data2) {
        if (data2 == null) {
            this.data = null;
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
        if (!selectedValue.isSet()) {
            this.onValueTouchListener.onValueDeselected();
        } else if (SelectedValue.SelectedValueType.COLUMN.equals(selectedValue.getType())) {
            this.onValueTouchListener.onColumnValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), this.data.getColumnChartData().getColumns().get(selectedValue.getFirstIndex()).getValues().get(selectedValue.getSecondIndex()));
        } else if (SelectedValue.SelectedValueType.LINE.equals(selectedValue.getType())) {
            this.onValueTouchListener.onPointValueSelected(selectedValue.getFirstIndex(), selectedValue.getSecondIndex(), this.data.getLineChartData().getLines().get(selectedValue.getFirstIndex()).getValues().get(selectedValue.getSecondIndex()));
        } else {
            throw new IllegalArgumentException("Invalid selected value type " + selectedValue.getType().name());
        }
    }

    public ComboLineColumnChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(ComboLineColumnChartOnValueSelectListener touchListener) {
        if (touchListener != null) {
            this.onValueTouchListener = touchListener;
        }
    }

    public void setColumnChartRenderer(Context context, ColumnChartRenderer columnChartRenderer) {
        setChartRenderer(new ComboLineColumnChartRenderer(context, this, columnChartRenderer, this.lineChartDataProvider));
    }

    public void setLineChartRenderer(Context context, LineChartRenderer lineChartRenderer) {
        setChartRenderer(new ComboLineColumnChartRenderer(context, this, this.columnChartDataProvider, lineChartRenderer));
    }

    private class ComboLineChartDataProvider implements LineChartDataProvider {
        private ComboLineChartDataProvider() {
        }

        /* synthetic */ ComboLineChartDataProvider(ComboLineColumnChartView comboLineColumnChartView, ComboLineChartDataProvider comboLineChartDataProvider) {
            this();
        }

        @Override // lecho.lib.hellocharts.provider.LineChartDataProvider
        public LineChartData getLineChartData() {
            return ComboLineColumnChartView.this.data.getLineChartData();
        }

        @Override // lecho.lib.hellocharts.provider.LineChartDataProvider
        public void setLineChartData(LineChartData data) {
            ComboLineColumnChartView.this.data.setLineChartData(data);
        }
    }

    private class ComboColumnChartDataProvider implements ColumnChartDataProvider {
        private ComboColumnChartDataProvider() {
        }

        /* synthetic */ ComboColumnChartDataProvider(ComboLineColumnChartView comboLineColumnChartView, ComboColumnChartDataProvider comboColumnChartDataProvider) {
            this();
        }

        @Override // lecho.lib.hellocharts.provider.ColumnChartDataProvider
        public ColumnChartData getColumnChartData() {
            return ComboLineColumnChartView.this.data.getColumnChartData();
        }

        @Override // lecho.lib.hellocharts.provider.ColumnChartDataProvider
        public void setColumnChartData(ColumnChartData data) {
            ComboLineColumnChartView.this.data.setColumnChartData(data);
        }
    }
}
