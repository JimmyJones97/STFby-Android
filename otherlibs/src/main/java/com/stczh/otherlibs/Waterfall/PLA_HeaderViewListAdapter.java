package com.stczh.otherlibs.Waterfall;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;
import com.stczh.otherlibs.Waterfall.PLA_ListView;
import java.util.ArrayList;
import java.util.Iterator;

public class PLA_HeaderViewListAdapter implements WrapperListAdapter, Filterable {
    static final ArrayList<PLA_ListView.FixedViewInfo> EMPTY_INFO_LIST = new ArrayList<>();
    private final ListAdapter mAdapter;
    boolean mAreAllFixedViewsSelectable;
    ArrayList<PLA_ListView.FixedViewInfo> mFooterViewInfos;
    ArrayList<PLA_ListView.FixedViewInfo> mHeaderViewInfos;
    private final boolean mIsFilterable;

    public PLA_HeaderViewListAdapter(ArrayList<PLA_ListView.FixedViewInfo> headerViewInfos, ArrayList<PLA_ListView.FixedViewInfo> footerViewInfos, ListAdapter adapter) {
        this.mAdapter = adapter;
        this.mIsFilterable = adapter instanceof Filterable;
        if (headerViewInfos == null) {
            this.mHeaderViewInfos = EMPTY_INFO_LIST;
        } else {
            this.mHeaderViewInfos = headerViewInfos;
        }
        if (footerViewInfos == null) {
            this.mFooterViewInfos = EMPTY_INFO_LIST;
        } else {
            this.mFooterViewInfos = footerViewInfos;
        }
        this.mAreAllFixedViewsSelectable = areAllListInfosSelectable(this.mHeaderViewInfos) && areAllListInfosSelectable(this.mFooterViewInfos);
    }

    public int getHeadersCount() {
        return this.mHeaderViewInfos.size();
    }

    public int getFootersCount() {
        return this.mFooterViewInfos.size();
    }

    @Override // android.widget.Adapter
    public boolean isEmpty() {
        return this.mAdapter == null || this.mAdapter.isEmpty();
    }

    private boolean areAllListInfosSelectable(ArrayList<PLA_ListView.FixedViewInfo> infos) {
        if (infos != null) {
            Iterator<PLA_ListView.FixedViewInfo> it = infos.iterator();
            while (it.hasNext()) {
                if (!it.next().isSelectable) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean removeHeader(View v) {
        boolean z = false;
        for (int i = 0; i < this.mHeaderViewInfos.size(); i++) {
            if (this.mHeaderViewInfos.get(i).view == v) {
                this.mHeaderViewInfos.remove(i);
                if (areAllListInfosSelectable(this.mHeaderViewInfos) && areAllListInfosSelectable(this.mFooterViewInfos)) {
                    z = true;
                }
                this.mAreAllFixedViewsSelectable = z;
                return true;
            }
        }
        return false;
    }

    public boolean removeFooter(View v) {
        boolean z = false;
        for (int i = 0; i < this.mFooterViewInfos.size(); i++) {
            if (this.mFooterViewInfos.get(i).view == v) {
                this.mFooterViewInfos.remove(i);
                if (areAllListInfosSelectable(this.mHeaderViewInfos) && areAllListInfosSelectable(this.mFooterViewInfos)) {
                    z = true;
                }
                this.mAreAllFixedViewsSelectable = z;
                return true;
            }
        }
        return false;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        if (this.mAdapter != null) {
            return getFootersCount() + getHeadersCount() + this.mAdapter.getCount();
        }
        return getFootersCount() + getHeadersCount();
    }

    @Override // android.widget.ListAdapter
    public boolean areAllItemsEnabled() {
        if (this.mAdapter == null) {
            return true;
        }
        if (!this.mAreAllFixedViewsSelectable || !this.mAdapter.areAllItemsEnabled()) {
            return false;
        }
        return true;
    }

    @Override // android.widget.ListAdapter
    public boolean isEnabled(int position) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return this.mHeaderViewInfos.get(position).isSelectable;
        }
        int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (this.mAdapter == null || adjPosition >= (adapterCount = this.mAdapter.getCount())) {
            return this.mFooterViewInfos.get(adjPosition - adapterCount).isSelectable;
        }
        return this.mAdapter.isEnabled(adjPosition);
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return this.mHeaderViewInfos.get(position).data;
        }
        int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (this.mAdapter == null || adjPosition >= (adapterCount = this.mAdapter.getCount())) {
            return this.mFooterViewInfos.get(adjPosition - adapterCount).data;
        }
        return this.mAdapter.getItem(adjPosition);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        int adjPosition;
        int numHeaders = getHeadersCount();
        if (this.mAdapter == null || position < numHeaders || (adjPosition = position - numHeaders) >= this.mAdapter.getCount()) {
            return -1;
        }
        return this.mAdapter.getItemId(adjPosition);
    }

    @Override // android.widget.Adapter
    public boolean hasStableIds() {
        if (this.mAdapter != null) {
            return this.mAdapter.hasStableIds();
        }
        return false;
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        int numHeaders = getHeadersCount();
        if (position < numHeaders) {
            return this.mHeaderViewInfos.get(position).view;
        }
        int adjPosition = position - numHeaders;
        int adapterCount = 0;
        if (this.mAdapter == null || adjPosition >= (adapterCount = this.mAdapter.getCount())) {
            return this.mFooterViewInfos.get(adjPosition - adapterCount).view;
        }
        return this.mAdapter.getView(adjPosition, convertView, parent);
    }

    @Override // android.widget.Adapter
    public int getItemViewType(int position) {
        int adjPosition;
        int numHeaders = getHeadersCount();
        if (this.mAdapter == null || position < numHeaders || (adjPosition = position - numHeaders) >= this.mAdapter.getCount()) {
            return -2;
        }
        return this.mAdapter.getItemViewType(adjPosition);
    }

    @Override // android.widget.Adapter
    public int getViewTypeCount() {
        if (this.mAdapter != null) {
            return this.mAdapter.getViewTypeCount();
        }
        return 1;
    }

    @Override // android.widget.Adapter
    public void registerDataSetObserver(DataSetObserver observer) {
        if (this.mAdapter != null) {
            this.mAdapter.registerDataSetObserver(observer);
        }
    }

    @Override // android.widget.Adapter
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (this.mAdapter != null) {
            this.mAdapter.unregisterDataSetObserver(observer);
        }
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        if (this.mIsFilterable) {
            return ((Filterable) this.mAdapter).getFilter();
        }
        return null;
    }

    @Override // android.widget.WrapperListAdapter
    public ListAdapter getWrappedAdapter() {
        return this.mAdapter;
    }
}
