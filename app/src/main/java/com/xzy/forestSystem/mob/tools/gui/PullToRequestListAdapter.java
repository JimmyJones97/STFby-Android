package  com.xzy.forestSystem.mob.tools.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

public abstract class PullToRequestListAdapter extends PullToRequestBaseListAdapter {
    private PullToRequestBaseAdapter adapter;
    private boolean fling;
    private ScrollableListView listView = onNewListView(getContext());
    private OnListStopScrollListener osListener;
    private boolean pullUpReady;

    public PullToRequestListAdapter(PullToRequestView view) {
        super(view);
        this.listView.setOnScrollListener(new AbsListView.OnScrollListener() { // from class:  com.xzy.forestSystem.mob.tools.gui.PullToRequestListAdapter.1
            private int firstVisibleItem;
            private int visibleItemCount;

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScrollStateChanged(AbsListView view2, int scrollState) {
                PullToRequestListAdapter.this.fling = scrollState == 2;
                if (scrollState != 0) {
                    return;
                }
                if (PullToRequestListAdapter.this.osListener != null) {
                    PullToRequestListAdapter.this.osListener.onListStopScrolling(this.firstVisibleItem, this.visibleItemCount);
                } else if (PullToRequestListAdapter.this.adapter != null) {
                    PullToRequestListAdapter.this.adapter.notifyDataSetChanged();
                }
            }

            @Override // android.widget.AbsListView.OnScrollListener
            public void onScroll(AbsListView view2, int firstVisibleItem2, int visibleItemCount2, int totalItemCount) {
                this.firstVisibleItem = firstVisibleItem2;
                this.visibleItemCount = visibleItemCount2;
                View v = view2.getChildAt(visibleItemCount2 - 1);
                PullToRequestListAdapter.this.pullUpReady = firstVisibleItem2 + visibleItemCount2 == totalItemCount && v != null && v.getBottom() <= view2.getBottom();
                PullToRequestListAdapter.this.onScroll(PullToRequestListAdapter.this.listView, firstVisibleItem2, visibleItemCount2, totalItemCount);
            }
        });
        this.adapter = new PullToRequestBaseAdapter(this);
        this.listView.setAdapter((ListAdapter) this.adapter);
    }

    /* access modifiers changed from: protected */
    public ScrollableListView onNewListView(Context context) {
        return new ScrollableListView(context);
    }

    @Override //  com.xzy.forestSystem.mob.tools.gui.PullToRequestAdatper
    public Scrollable getBodyView() {
        return this.listView;
    }

    public ListView getListView() {
        return this.listView;
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

    public void setDivider(Drawable divider) {
        this.listView.setDivider(divider);
    }

    public void setDividerHeight(int height) {
        this.listView.setDividerHeight(height);
    }

    @Override //  com.xzy.forestSystem.mob.tools.gui.PullToRequestAdatper
    public boolean isPullDownReady() {
        return this.listView.isReadyToPull();
    }

    @Override //  com.xzy.forestSystem.mob.tools.gui.PullToRequestAdatper
    public boolean isPullUpReady() {
        return this.pullUpReady;
    }
}
