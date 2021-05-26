package com.stczh.otherlibs.Waterfall;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;

public class MultiColumnListView extends PLA_ListView {
    private static final int DEFAULT_COLUMN_NUMBER = 2;
    private static final String TAG = "MultiColumnListView";
    private int mColumnNumber = 2;
    private int mColumnPaddingLeft = 0;
    private int mColumnPaddingRight = 0;
    private Column[] mColumns = null;
    private Column mFixedColumn = null;
    private Rect mFrameRect = new Rect();
    private SparseIntArray mItems = new SparseIntArray();

    public MultiColumnListView(Context context) {
        super(context);
        init(null);
    }

    public MultiColumnListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MultiColumnListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        getWindowVisibleDisplayFrame(this.mFrameRect);
        if (attrs == null) {
            this.mColumnNumber = 2;
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MultiColumnListView);
            int landColNumber = a.getInteger(1, -1);
            int defColNumber = a.getInteger(0, -1);
            if (this.mFrameRect.width() > this.mFrameRect.height() && landColNumber != -1) {
                this.mColumnNumber = landColNumber;
            } else if (defColNumber != -1) {
                this.mColumnNumber = defColNumber;
            } else {
                this.mColumnNumber = 2;
            }
            this.mColumnPaddingLeft = a.getDimensionPixelSize(2, 0);
            this.mColumnPaddingRight = a.getDimensionPixelSize(3, 0);
            a.recycle();
        }
        this.mColumns = new Column[this.mColumnNumber];
        for (int i = 0; i < this.mColumnNumber; i++) {
            this.mColumns[i] = new Column(i);
        }
        this.mFixedColumn = new FixedColumn();
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_AbsListView, com.stczh.otherlibs.Waterfall.PLA_AdapterView, android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_ListView, com.stczh.otherlibs.Waterfall.PLA_AbsListView, android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = ((((getMeasuredWidth() - this.mListPadding.left) - this.mListPadding.right) - this.mColumnPaddingLeft) - this.mColumnPaddingRight) / this.mColumnNumber;
        for (int index = 0; index < this.mColumnNumber; index++) {
            this.mColumns[index].mColumnWidth = width;
            this.mColumns[index].mColumnLeft = this.mListPadding.left + this.mColumnPaddingLeft + (width * index);
        }
        this.mFixedColumn.mColumnLeft = this.mListPadding.left;
        this.mFixedColumn.mColumnWidth = getMeasuredWidth();
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_ListView
    public void onMeasureChild(View child, int position, int widthMeasureSpec, int heightMeasureSpec) {
        if (isFixedView(child)) {
            child.measure(widthMeasureSpec, heightMeasureSpec);
        } else {
            child.measure(1073741824 | getColumnWidth(position), heightMeasureSpec);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_AbsListView
    public int modifyFlingInitialVelocity(int initialVelocity) {
        return initialVelocity;
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_ListView
    public void onItemAddedToList(int position, boolean flow) {
        super.onItemAddedToList(position, flow);
        if (!isHeaderOrFooterPosition(position)) {
            this.mItems.append(position, getNextColumn(flow, position).getIndex());
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_AbsListView
    public void onLayoutSync(int syncPos) {
        for (Column c : this.mColumns) {
            c.save();
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_AbsListView
    public void onLayoutSyncFinished(int syncPos) {
        for (Column c : this.mColumns) {
            c.clear();
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_ListView
    public void onAdjustChildViews(boolean down) {
        int firstItem = getFirstVisiblePosition();
        if (!down && firstItem == 0) {
            int firstColumnTop = this.mColumns[0].getTop();
            Column[] columnArr = this.mColumns;
            for (Column c : columnArr) {
                c.offsetTopAndBottom(firstColumnTop - c.getTop());
            }
        }
        super.onAdjustChildViews(down);
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_AbsListView
    public int getFillChildBottom() {
        int result = Integer.MAX_VALUE;
        for (Column c : this.mColumns) {
            int bottom = c.getBottom();
            if (result > bottom) {
                result = bottom;
            }
        }
        return result;
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_AbsListView
    public int getFillChildTop() {
        int result = Integer.MIN_VALUE;
        for (Column c : this.mColumns) {
            int top = c.getTop();
            if (result < top) {
                result = top;
            }
        }
        return result;
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_AbsListView
    public int getScrollChildBottom() {
        int result = Integer.MIN_VALUE;
        for (Column c : this.mColumns) {
            int bottom = c.getBottom();
            if (result < bottom) {
                result = bottom;
            }
        }
        return result;
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_AbsListView
    public int getScrollChildTop() {
        int result = Integer.MAX_VALUE;
        for (Column c : this.mColumns) {
            int top = c.getTop();
            if (result > top) {
                result = top;
            }
        }
        return result;
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_ListView
    public int getItemLeft(int pos) {
        if (isHeaderOrFooterPosition(pos)) {
            return this.mFixedColumn.getColumnLeft();
        }
        return getColumnLeft(pos);
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_ListView
    public int getItemTop(int pos) {
        if (isHeaderOrFooterPosition(pos)) {
            return this.mFixedColumn.getBottom();
        }
        int colIndex = this.mItems.get(pos, -1);
        if (colIndex == -1) {
            return getFillChildBottom();
        }
        return this.mColumns[colIndex].getBottom();
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_ListView
    public int getItemBottom(int pos) {
        if (isHeaderOrFooterPosition(pos)) {
            return this.mFixedColumn.getTop();
        }
        int colIndex = this.mItems.get(pos, -1);
        if (colIndex == -1) {
            return getFillChildTop();
        }
        return this.mColumns[colIndex].getTop();
    }

    private Column getNextColumn(boolean flow, int position) {
        int colIndex = this.mItems.get(position, -1);
        if (colIndex != -1) {
            return this.mColumns[colIndex];
        }
        int lastVisiblePos = Math.max(0, Math.max(0, position - getHeaderViewsCount()));
        if (lastVisiblePos < this.mColumnNumber) {
            return this.mColumns[lastVisiblePos];
        }
        if (flow) {
            return gettBottomColumn();
        }
        return getTopColumn();
    }

    private boolean isHeaderOrFooterPosition(int pos) {
        return this.mAdapter.getItemViewType(pos) == -2;
    }

    private Column getTopColumn() {
        Column result = this.mColumns[0];
        Column[] columnArr = this.mColumns;
        for (Column c : columnArr) {
            if (result.getTop() > c.getTop()) {
                result = c;
            }
        }
        return result;
    }

    private Column gettBottomColumn() {
        Column result = this.mColumns[0];
        Column[] columnArr = this.mColumns;
        for (Column c : columnArr) {
            if (result.getBottom() > c.getBottom()) {
                result = c;
            }
        }
        return result;
    }

    private int getColumnLeft(int pos) {
        int colIndex = this.mItems.get(pos, -1);
        if (colIndex == -1) {
            return 0;
        }
        return this.mColumns[colIndex].getColumnLeft();
    }

    private int getColumnWidth(int pos) {
        int colIndex = this.mItems.get(pos, -1);
        if (colIndex == -1) {
            return 0;
        }
        return this.mColumns[colIndex].getColumnWidth();
    }

    /* access modifiers changed from: private */
    public class Column {
        private int mColumnLeft;
        private int mColumnWidth;
        private int mIndex;
        private int mSynchedBottom = 0;
        private int mSynchedTop = 0;

        public Column(int index) {
            this.mIndex = index;
        }

        public int getColumnLeft() {
            return this.mColumnLeft;
        }

        public int getColumnWidth() {
            return this.mColumnWidth;
        }

        public int getIndex() {
            return this.mIndex;
        }

        public int getBottom() {
            int bottom = Integer.MIN_VALUE;
            int childCount = MultiColumnListView.this.getChildCount();
            for (int index = 0; index < childCount; index++) {
                View v = MultiColumnListView.this.getChildAt(index);
                if ((v.getLeft() == this.mColumnLeft || MultiColumnListView.this.isFixedView(v)) && bottom < v.getBottom()) {
                    bottom = v.getBottom();
                }
            }
            if (bottom == Integer.MIN_VALUE) {
                return this.mSynchedBottom;
            }
            return bottom;
        }

        public void offsetTopAndBottom(int offset) {
            if (offset != 0) {
                int childCount = MultiColumnListView.this.getChildCount();
                for (int index = 0; index < childCount; index++) {
                    View v = MultiColumnListView.this.getChildAt(index);
                    if (v.getLeft() == this.mColumnLeft || MultiColumnListView.this.isFixedView(v)) {
                        v.offsetTopAndBottom(offset);
                    }
                }
            }
        }

        public int getTop() {
            int top = Integer.MAX_VALUE;
            int childCount = MultiColumnListView.this.getChildCount();
            for (int index = 0; index < childCount; index++) {
                View v = MultiColumnListView.this.getChildAt(index);
                if ((v.getLeft() == this.mColumnLeft || MultiColumnListView.this.isFixedView(v)) && top > v.getTop()) {
                    top = v.getTop();
                }
            }
            if (top == Integer.MAX_VALUE) {
                return this.mSynchedTop;
            }
            return top;
        }

        public void save() {
            this.mSynchedTop = 0;
            this.mSynchedBottom = getTop();
        }

        public void clear() {
            this.mSynchedTop = 0;
            this.mSynchedBottom = 0;
        }
    }

    /* access modifiers changed from: private */
    public class FixedColumn extends Column {
        public FixedColumn() {
            super(Integer.MAX_VALUE);
        }

        @Override // com.stczh.otherlibs.Waterfall.MultiColumnListView.Column
        public int getBottom() {
            return MultiColumnListView.this.getScrollChildBottom();
        }

        @Override // com.stczh.otherlibs.Waterfall.MultiColumnListView.Column
        public int getTop() {
            return MultiColumnListView.this.getScrollChildTop();
        }
    }
}
