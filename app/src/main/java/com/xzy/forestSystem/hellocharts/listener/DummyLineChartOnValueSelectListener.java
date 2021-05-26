package com.xzy.forestSystem.hellocharts.listener;

import com.xzy.forestSystem.hellocharts.model.PointValue;

public class DummyLineChartOnValueSelectListener implements LineChartOnValueSelectListener {
    @Override // lecho.lib.hellocharts.listener.LineChartOnValueSelectListener
    public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
    }

    @Override // lecho.lib.hellocharts.listener.OnValueDeselectListener
    public void onValueDeselected() {
    }
}
