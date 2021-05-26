package com.xzy.forestSystem.hellocharts.provider;

import com.xzy.forestSystem.hellocharts.model.PieChartData;

public interface PieChartDataProvider {
    PieChartData getPieChartData();

    void setPieChartData(PieChartData pieChartData);
}
