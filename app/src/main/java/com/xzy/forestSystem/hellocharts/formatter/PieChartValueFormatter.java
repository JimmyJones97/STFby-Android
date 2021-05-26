package com.xzy.forestSystem.hellocharts.formatter;

import com.xzy.forestSystem.hellocharts.model.SliceValue;

public interface PieChartValueFormatter {
    int formatChartValue(char[] cArr, SliceValue sliceValue);
}
