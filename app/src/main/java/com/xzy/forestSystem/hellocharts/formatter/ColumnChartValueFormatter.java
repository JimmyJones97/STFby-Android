package com.xzy.forestSystem.hellocharts.formatter;

import com.xzy.forestSystem.hellocharts.model.SubcolumnValue;

public interface ColumnChartValueFormatter {
    int formatChartValue(char[] cArr, SubcolumnValue subcolumnValue);
}
