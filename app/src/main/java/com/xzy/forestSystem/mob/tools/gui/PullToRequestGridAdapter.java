package  com.xzy.forestSystem.mob.tools.gui;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;

public abstract class PullToRequestGridAdapter extends PullToRequestBaseListAdapter {
    private PullToRequestBaseAdapter adapter;
    private boolean fling;
    private ScrollableGridView gridView = onNewGridView(getContext());
    private OnListStopScrollListener osListener;
    private boolean pullUpReady;

    public PullToRequestGridAdapter(PullToRequestView view) {
        super(view);
        this.gridView.setOnScrollListener(new AbsListView.OnScrollListener() { // from class:  com.xzy.forestSystem.mob.tools.gui.PullToRequestGridAdapter.1
            private int firstVisibleItem;
            private int visibleItemCount;

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView view2, int scrollState) {
                PullToRequestGridAdapter.this.fling = scrollState == 2;
                if (scrollState != 0) {
                    return;
                }
                if (PullToRequestGridAdapter.this.osListener != null) {
                    PullToRequestGridAdapter.this.osListener.onListStopScrolling(this.firstVisibleItem, this.visibleItemCount);
                } else if (PullToRequestGridAdapter.this.adapter != null) {
                    PullToRequestGridAdapter.this.adapter.notifyDataSetChanged();
                }
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView view2, int firstVisibleItem2, int visibleItemCount2, int totalItemCount) {
                this.firstVisibleItem = firstVisibleItem2;
                this.visibleItemCount = visibleItemCount2;
                View v = view2.getChildAt(visibleItemCount2 - 1);
                PullToRequestGridAdapter.this.pullUpReady = firstVisibleItem2 + visibleItemCount2 == totalItemCount && v != null && v.getBottom() <= view2.getBottom();
                PullToRequestGridAdapter.this.onScroll(PullToRequestGridAdapter.this.gridView, firstVisibleItem2, visibleItemCount2, totalItemCount);
            }
        });
        this.adapter = new PullToRequestBaseAdapter(this);
        this.gridView.setAdapter((ListAdapter) this.adapter);
    }

    /* access modifiers changed from: protected */
    public ScrollableGridView onNewGridView(Context context) {
        return new ScrollableGridView(context);
    }

    @Override //  com.xzy.forestSystem.mob.tools.gui.PullToRequestAdatper
    public Scrollable getBodyView() {
        return this.gridView;
    }

    @Override //  com.xzy.forestSystem.mob.tools.gui.PullToRequestAdatper
    public boolean isPullDownReady() {
        return this.gridView.isReadyToPull();
    }

    @Override //  com.xzy.forestSystem.mob.tools.gui.PullToRequestAdatper
    public boolean isPullUpReady() {
        return this.pullUpReady;
    }

    public GridView getGridView() {
        return this.gridView;
    }

    @Override //  com.xzy.forestSystem.mob.tools.gui.PullToRequestBaseListAdapter
    public boolean isFling() {
        return this.fling;
    }

    @Override //  com.xzy.forestSystem.mob.tools.gui.PullToRequestBaseListAdapter
    public void onScroll(Scrollable scrollable, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    @Override //  com.xzy.forestSystem.mob.tools.gui.PullToRequestAdatper
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.adapter.notifyDataSetChanged();
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.gridView.setHorizontalSpacing(horizontalSpacing);
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.gridView.setVerticalSpacing(verticalSpacing);
    }

    public void setNumColumns(int numColumns) {
        this.gridView.setNumColumns(numColumns);
    }

    public void setColumnWidth(int columnWidth) {
        this.gridView.setColumnWidth(columnWidth);
    }

    public void setStretchMode(int stretchMode) {
        this.gridView.setStretchMode(stretchMode);
    }
}
