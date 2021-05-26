package com.xzy.forestSystem.hellocharts.listener;

import com.xzy.forestSystem.hellocharts.model.PointValue;
import com.xzy.forestSystem.hellocharts.model.SubcolumnValue;

public interface ComboLineColumnChartOnValueSelectListener extends OnValueDeselectListener {
    void onColumnValueSelected(int i, int i2, SubcolumnValue subcolumnValue);

    void onPointValueSelected(int i, int i2, PointValue pointValue);
}
