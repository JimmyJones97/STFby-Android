package com.xzy.forestSystem.hellocharts.listener;

import com.xzy.forestSystem.hellocharts.model.BubbleValue;

public interface BubbleChartOnValueSelectListener extends OnValueDeselectListener {
    void onValueSelected(int i, BubbleValue bubbleValue);
}
