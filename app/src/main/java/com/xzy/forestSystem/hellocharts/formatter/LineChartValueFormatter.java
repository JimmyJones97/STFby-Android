package com.xzy.forestSystem.hellocharts.formatter;

import com.xzy.forestSystem.hellocharts.model.PointValue;

public interface LineChartValueFormatter {
    int formatChartValue(char[] cArr, PointValue pointValue);
}
