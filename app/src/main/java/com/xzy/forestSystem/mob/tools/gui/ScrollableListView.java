package  com.xzy.forestSystem.mob.tools.gui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;
import  com.xzy.forestSystem.mob.tools.gui.Scrollable;

public class ScrollableListView extends ListView implements Scrollable {
    private Scrollable.OnScrollListener osListener;
    private boolean pullEnable;

    public ScrollableListView(Context context) {
        super(context);
        init(context);
    }

    public ScrollableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ScrollableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setCacheColorHint(0);
        setSelector(new ColorDrawable());
        setDividerHeight(0);
        this.pullEnable = true;
        this.osListener = new Scrollable.OnScrollListener() { // from class:  com.xzy.forestSystem.mob.tools.gui.ScrollableListView.1
            @Override //  com.xzy.forestSystem.mob.tools.gui.Scrollable.OnScrollListener
            public void onScrollChanged(Scrollable scrollable, int l, int t, int oldl, int oldt) {
                ScrollableListView.this.pullEnable = t <= 0 && oldt <= 0;
            }
        };
    }

    public boolean isReadyToPull() {
        return this.pullEnable;
    }

    /* access modifiers changed from: protected */
    @Override // android.widget.AbsListView, android.view.View
    public int computeVerticalScrollOffset() {
        int offset = super.computeVerticalScrollOffset();
        if (this.osListener != null) {
            this.osListener.onScrollChanged(this, 0, offset, 0, 0);
        }
        return offset;
    }
}
