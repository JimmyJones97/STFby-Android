package com.xzy.forestSystem.hellocharts.formatter;

import com.xzy.forestSystem.hellocharts.model.BubbleValue;

public interface BubbleChartValueFormatter {
    int formatChartValue(char[] cArr, BubbleValue bubbleValue);
}
