package com.xzy.forestSystem.hellocharts.model;

import android.graphics.Typeface;

import com.xzy.forestSystem.hellocharts.util.ChartUtils;

public abstract class AbstractChartData implements ChartData {
    public static final int DEFAULT_TEXT_SIZE_SP = 12;
    protected Axis axisXBottom;
    protected Axis axisXTop;
    protected Axis axisYLeft;
    protected Axis axisYRight;
    protected boolean isValueLabelBackgroundEnabled = true;
    protected boolean isValueLabelBackgrountAuto = true;
    protected int valueLabelBackgroundColor = ChartUtils.darkenColor(ChartUtils.DEFAULT_DARKEN_COLOR);
    protected int valueLabelTextColor = -1;
    protected int valueLabelTextSize = 12;
    protected Typeface valueLabelTypeface;

    public AbstractChartData() {
    }

    public AbstractChartData(AbstractChartData data) {
        if (data.axisXBottom != null) {
            this.axisXBottom = new Axis(data.axisXBottom);
        }
        if (data.axisXTop != null) {
            this.axisXTop = new Axis(data.axisXTop);
        }
        if (data.axisYLeft != null) {
            this.axisYLeft = new Axis(data.axisYLeft);
        }
        if (data.axisYRight != null) {
            this.axisYRight = new Axis(data.axisYRight);
        }
        this.valueLabelTextColor = data.valueLabelTextColor;
        this.valueLabelTextSize = data.valueLabelTextSize;
        this.valueLabelTypeface = data.valueLabelTypeface;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public Axis getAxisXBottom() {
        return this.axisXBottom;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setAxisXBottom(Axis axisX) {
        this.axisXBottom = axisX;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public Axis getAxisYLeft() {
        return this.axisYLeft;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setAxisYLeft(Axis axisY) {
        this.axisYLeft = axisY;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public Axis getAxisXTop() {
        return this.axisXTop;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setAxisXTop(Axis axisX) {
        this.axisXTop = axisX;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public Axis getAxisYRight() {
        return this.axisYRight;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setAxisYRight(Axis axisY) {
        this.axisYRight = axisY;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public int getValueLabelTextColor() {
        return this.valueLabelTextColor;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setValueLabelsTextColor(int valueLabelTextColor2) {
        this.valueLabelTextColor = valueLabelTextColor2;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public int getValueLabelTextSize() {
        return this.valueLabelTextSize;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setValueLabelTextSize(int valueLabelTextSize2) {
        this.valueLabelTextSize = valueLabelTextSize2;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public Typeface getValueLabelTypeface() {
        return this.valueLabelTypeface;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setValueLabelTypeface(Typeface typeface) {
        this.valueLabelTypeface = typeface;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public boolean isValueLabelBackgroundEnabled() {
        return this.isValueLabelBackgroundEnabled;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setValueLabelBackgroundEnabled(boolean isValueLabelBackgroundEnabled2) {
        this.isValueLabelBackgroundEnabled = isValueLabelBackgroundEnabled2;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public boolean isValueLabelBackgroundAuto() {
        return this.isValueLabelBackgrountAuto;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setValueLabelBackgroundAuto(boolean isValueLabelBackgrountAuto2) {
        this.isValueLabelBackgrountAuto = isValueLabelBackgrountAuto2;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public int getValueLabelBackgroundColor() {
        return this.valueLabelBackgroundColor;
    }

    @Override // lecho.lib.hellocharts.model.ChartData
    public void setValueLabelBackgroundColor(int valueLabelBackgroundColor2) {
        this.valueLabelBackgroundColor = valueLabelBackgroundColor2;
    }
}
