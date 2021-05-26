package com.xzy.forestSystem.hellocharts.listener;

import com.xzy.forestSystem.hellocharts.model.SliceValue;

public class DummyPieChartOnValueSelectListener implements PieChartOnValueSelectListener {
    @Override // lecho.lib.hellocharts.listener.PieChartOnValueSelectListener
    public void onValueSelected(int arcIndex, SliceValue value) {
    }

    @Override // lecho.lib.hellocharts.listener.OnValueDeselectListener
    public void onValueDeselected() {
    }
}
