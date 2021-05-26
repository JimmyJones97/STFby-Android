package com.xzy.forestSystem.hellocharts.listener;

import com.xzy.forestSystem.hellocharts.model.PointValue;

public interface LineChartOnValueSelectListener extends OnValueDeselectListener {
    void onValueSelected(int i, int i2, PointValue pointValue);
}
