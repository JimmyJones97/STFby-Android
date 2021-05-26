package com.xzy.forestSystem.hellocharts.listener;

import com.xzy.forestSystem.hellocharts.model.SubcolumnValue;

public class DummyColumnChartOnValueSelectListener implements ColumnChartOnValueSelectListener {
    @Override // lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener
    public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
    }

    @Override // lecho.lib.hellocharts.listener.OnValueDeselectListener
    public void onValueDeselected() {
    }
}
