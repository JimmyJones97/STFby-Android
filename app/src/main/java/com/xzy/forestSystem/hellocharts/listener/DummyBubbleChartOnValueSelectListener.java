package com.xzy.forestSystem.hellocharts.listener;

import com.xzy.forestSystem.hellocharts.model.BubbleValue;

public class DummyBubbleChartOnValueSelectListener implements BubbleChartOnValueSelectListener {
    @Override // lecho.lib.hellocharts.listener.BubbleChartOnValueSelectListener
    public void onValueSelected(int bubbleIndex, BubbleValue value) {
    }

    @Override // lecho.lib.hellocharts.listener.OnValueDeselectListener
    public void onValueDeselected() {
    }
}
