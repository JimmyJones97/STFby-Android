package  com.xzy.forestSystem.mob.tools.gui;

import android.view.View;
import android.view.ViewGroup;

public abstract class PullToRequestBaseListAdapter extends PullToRequestAdatper {
    public abstract int getCount();

    public abstract Object getItem(int i);

    public abstract long getItemId(int i);

    public abstract View getView(int i, View view, ViewGroup viewGroup);

    public abstract boolean isFling();

    public abstract void onScroll(Scrollable scrollable, int i, int i2, int i3);

    public PullToRequestBaseListAdapter(PullToRequestView view) {
        super(view);
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public int getViewTypeCount() {
        return 1;
    }
}
