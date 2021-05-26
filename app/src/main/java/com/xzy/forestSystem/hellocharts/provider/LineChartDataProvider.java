package com.xzy.forestSystem.hellocharts.provider;

import com.xzy.forestSystem.hellocharts.model.LineChartData;

public interface LineChartDataProvider {
    LineChartData getLineChartData();

    void setLineChartData(LineChartData lineChartData);
}
