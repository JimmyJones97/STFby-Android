package com.xzy.forestSystem.hellocharts.provider;

import com.xzy.forestSystem.hellocharts.model.ColumnChartData;

public interface ColumnChartDataProvider {
    ColumnChartData getColumnChartData();

    void setColumnChartData(ColumnChartData columnChartData);
}
