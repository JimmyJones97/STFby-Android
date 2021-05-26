package  com.xzy.forestSystem.mob.tools.gui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class PullToRequestBaseAdapter extends BaseAdapter {
    private PullToRequestBaseListAdapter adapter;

    public PullToRequestBaseAdapter(PullToRequestBaseListAdapter adapter2) {
        this.adapter = adapter2;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.adapter.getCount();
    }

    @Override // android.widget.Adapter
    public Object getItem(int position) {
        return this.adapter.getItem(position);
    }

    @Override // android.widget.Adapter
    public long getItemId(int position) {
        return this.adapter.getItemId(position);
    }

    @Override // android.widget.Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        return this.adapter.getView(position, convertView, parent);
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getItemViewType(int position) {
        return this.adapter.getItemViewType(position);
    }

    @Override // android.widget.BaseAdapter, android.widget.Adapter
    public int getViewTypeCount() {
        return this.adapter.getViewTypeCount();
    }
}
