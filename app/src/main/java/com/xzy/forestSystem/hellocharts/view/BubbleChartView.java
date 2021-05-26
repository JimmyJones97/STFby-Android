package com.xzy.forestSystem.hellocharts.view;

import android.content.Context;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;

import com.xzy.forestSystem.hellocharts.listener.BubbleChartOnValueSelectListener;
import com.xzy.forestSystem.hellocharts.listener.DummyBubbleChartOnValueSelectListener;
import com.xzy.forestSystem.hellocharts.model.BubbleChartData;
import com.xzy.forestSystem.hellocharts.model.ChartData;
import com.xzy.forestSystem.hellocharts.model.SelectedValue;
import com.xzy.forestSystem.hellocharts.provider.BubbleChartDataProvider;
import com.xzy.forestSystem.hellocharts.renderer.BubbleChartRenderer;

public class BubbleChartView extends AbstractChartView implements BubbleChartDataProvider {
    private static final String TAG = "BubbleChartView";
    protected BubbleChartRenderer bubbleChartRenderer;
    protected BubbleChartData data;
    protected BubbleChartOnValueSelectListener onValueTouchListener;

    public BubbleChartView(Context context) {
        this(context, null, 0);
    }

    public BubbleChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleChartView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.onValueTouchListener = new DummyBubbleChartOnValueSelectListener();
        this.bubbleChartRenderer = new BubbleChartRenderer(context, this, this);
        setChartRenderer(this.bubbleChartRenderer);
        setBubbleChartData(BubbleChartData.generateDummyData());
    }

    @Override // lecho.lib.hellocharts.provider.BubbleChartDataProvider
    public BubbleChartData getBubbleChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.provider.BubbleChartDataProvider
    public void setBubbleChartData(BubbleChartData data2) {
        if (data2 == null) {
            this.data = BubbleChartData.generateDummyData();
        } else {
            this.data = data2;
        }
        super.onChartDataChange();
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public ChartData getChartData() {
        return this.data;
    }

    @Override // lecho.lib.hellocharts.view.Chart
    public void callTouchListener() {
        SelectedValue selectedValue = this.chartRenderer.getSelectedValue();
        if (selectedValue.isSet()) {
            this.onValueTouchListener.onValueSelected(selectedValue.getFirstIndex(), this.data.getValues().get(selectedValue.getFirstIndex()));
            return;
        }
        this.onValueTouchListener.onValueDeselected();
    }

    public BubbleChartOnValueSelectListener getOnValueTouchListener() {
        return this.onValueTouchListener;
    }

    public void setOnValueTouchListener(BubbleChartOnValueSelectListener touchListener) {
        if (touchListener != null) {
            this.onValueTouchListener = touchListener;
        }
    }

    public void removeMargins() {
        this.bubbleChartRenderer.removeMargins();
        ViewCompat.postInvalidateOnAnimation(this);
    }
}
