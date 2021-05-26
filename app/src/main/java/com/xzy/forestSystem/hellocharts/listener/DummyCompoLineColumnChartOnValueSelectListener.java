package com.xzy.forestSystem.hellocharts.listener;

import com.xzy.forestSystem.hellocharts.model.PointValue;
import com.xzy.forestSystem.hellocharts.model.SubcolumnValue;

public class DummyCompoLineColumnChartOnValueSelectListener implements ComboLineColumnChartOnValueSelectListener {
    @Override // lecho.lib.hellocharts.listener.ComboLineColumnChartOnValueSelectListener
    public void onColumnValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
    }

    @Override // lecho.lib.hellocharts.listener.ComboLineColumnChartOnValueSelectListener
    public void onPointValueSelected(int lineIndex, int pointIndex, PointValue value) {
    }

    @Override // lecho.lib.hellocharts.listener.OnValueDeselectListener
    public void onValueDeselected() {
    }
}
