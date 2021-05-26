package com.xzy.forestSystem.otherlibs.Waterfall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ListAdapter;
import android.widget.Scroller;

import androidx.core.view.MotionEventCompat;

import com.stczh.gzforestSystem.R;
import com.xzy.forestSystem.baidu.speech.easr.easrNativeJni;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class PLA_AbsListView extends PLA_AdapterView<ListAdapter> implements ViewTreeObserver.OnGlobalLayoutListener, ViewTreeObserver.OnTouchModeChangeListener {
    private static final int INVALID_POINTER = -1;
    static final int LAYOUT_FORCE_BOTTOM = 3;
    static final int LAYOUT_FORCE_TOP = 1;
    static final int LAYOUT_MOVE_SELECTION = 6;
    static final int LAYOUT_NORMAL = 0;
    static final int LAYOUT_SET_SELECTION = 2;
    static final int LAYOUT_SPECIFIC = 4;
    static final int LAYOUT_SYNC = 5;
    private static final boolean PROFILE_FLINGING = false;
    private static final boolean PROFILE_SCROLLING = false;
    protected static final int TOUCH_MODE_DONE_WAITING = 2;
    protected static final int TOUCH_MODE_DOWN = 0;
    protected static final int TOUCH_MODE_FLING = 4;
    private static final int TOUCH_MODE_OFF = 1;
    private static final int TOUCH_MODE_ON = 0;
    static final int TOUCH_MODE_REST = -1;
    protected static final int TOUCH_MODE_SCROLL = 3;
    protected static final int TOUCH_MODE_TAP = 1;
    private static final int TOUCH_MODE_UNKNOWN = -1;
    public static final int TRANSCRIPT_MODE_ALWAYS_SCROLL = 2;
    public static final int TRANSCRIPT_MODE_DISABLED = 0;
    public static final int TRANSCRIPT_MODE_NORMAL = 1;
    private int mActivePointerId;
    protected ListAdapter mAdapter;
    private int mCacheColorHint;
    protected boolean mCachingStarted;
    private Runnable mClearScrollingCache;
    private ContextMenu.ContextMenuInfo mContextMenuInfo;
    AdapterDataSetObserver mDataSetObserver;
    boolean mDrawSelectorOnTop;
    private boolean mFlingProfilingStarted;
    private FlingRunnable mFlingRunnable;
    private boolean mIsChildViewEnabled;
    final boolean[] mIsScrap;
    private int mLastScrollState;
    private int mLastTouchMode;
    int mLastY;
    int mLayoutMode;
    protected Rect mListPadding;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    int mMotionCorrection;
    protected int mMotionPosition;
    int mMotionViewNewTop;
    int mMotionViewOriginalTop;
    int mMotionX;
    int mMotionY;
    private OnScrollListener mOnScrollListener;
    private Runnable mPendingCheckForTap;
    private PerformClick mPerformClick;
    PositionScroller mPositionScroller;
    final RecycleBin mRecycler;
    int mResurrectToPosition;
    private boolean mScrollProfilingStarted;
    boolean mScrollingCacheEnabled;
    int mSelectedTop;
    int mSelectionBottomPadding;
    int mSelectionLeftPadding;
    int mSelectionRightPadding;
    int mSelectionTopPadding;
    Drawable mSelector;
    Rect mSelectorRect;
    private boolean mSmoothScrollbarEnabled;
    boolean mStackFromBottom;
    private Rect mTouchFrame;
    protected int mTouchMode;
    private int mTouchSlop;
    private int mTranscriptMode;
    private VelocityTracker mVelocityTracker;
    protected int mWidthMeasureSpec;

    public interface OnScrollListener {
        public static final int SCROLL_STATE_FLING = 2;
        public static final int SCROLL_STATE_IDLE = 0;
        public static final int SCROLL_STATE_TOUCH_SCROLL = 1;

        void onScroll(PLA_AbsListView pLA_AbsListView, int i, int i2, int i3);

        void onScrollStateChanged(PLA_AbsListView pLA_AbsListView, int i);
    }

    public interface RecyclerListener {
        void onMovedToScrapHeap(View view);
    }

    /* access modifiers changed from: package-private */
    public abstract void fillGap(boolean z);

    /* access modifiers changed from: package-private */
    public abstract int findMotionRow(int i);

    public PLA_AbsListView(Context context) {
        super(context);
        this.mLayoutMode = 0;
        this.mDrawSelectorOnTop = false;
        this.mSelectorRect = new Rect();
        this.mRecycler = new RecycleBin();
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mListPadding = new Rect();
        this.mWidthMeasureSpec = 0;
        this.mTouchMode = -1;
        this.mSelectedTop = 0;
        this.mSmoothScrollbarEnabled = true;
        this.mResurrectToPosition = -1;
        this.mContextMenuInfo = null;
        this.mLastTouchMode = -1;
        this.mScrollProfilingStarted = false;
        this.mFlingProfilingStarted = false;
        this.mLastScrollState = 0;
        this.mIsScrap = new boolean[1];
        this.mActivePointerId = -1;
        initAbsListView();
        setVerticalScrollBarEnabled(true);
        TypedArray a = context.obtainStyledAttributes(R.styleable.View);
        initializeScrollbars(a);
        a.recycle();
    }

    protected abstract void initializeScrollbars(TypedArray a);


    public PLA_AbsListView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.absListViewStyle);
    }

    @SuppressLint("ResourceType")
    public PLA_AbsListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mLayoutMode = 0;
        this.mDrawSelectorOnTop = false;
        this.mSelectorRect = new Rect();
        this.mRecycler = new RecycleBin();
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mListPadding = new Rect();
        this.mWidthMeasureSpec = 0;
        this.mTouchMode = -1;
        this.mSelectedTop = 0;
        this.mSmoothScrollbarEnabled = true;
        this.mResurrectToPosition = -1;
        this.mContextMenuInfo = null;
        this.mLastTouchMode = -1;
        this.mScrollProfilingStarted = false;
        this.mFlingProfilingStarted = false;
        this.mLastScrollState = 0;
        this.mIsScrap = new boolean[1];
        this.mActivePointerId = -1;
        initAbsListView();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsListView, defStyle, 0);
        Drawable d = a.getDrawable(0);
        if (d != null) {
            setSelector(d);
        }
        this.mDrawSelectorOnTop = a.getBoolean(1, false);
        setStackFromBottom(a.getBoolean(2, false));
        setScrollingCacheEnabled(a.getBoolean(3, true));
        setTranscriptMode(a.getInt(5, 0));
        setCacheColorHint(a.getColor(6, 0));
        setSmoothScrollbarEnabled(a.getBoolean(8, true));
        a.recycle();
    }

    private void initAbsListView() {
        setClickable(true);
        setFocusableInTouchMode(true);
        setWillNotDraw(false);
        setAlwaysDrawnWithCacheEnabled(false);
        setScrollingCacheEnabled(true);
        ViewConfiguration configuration = ViewConfiguration.get(getContext());
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    public void setSmoothScrollbarEnabled(boolean enabled) {
        this.mSmoothScrollbarEnabled = enabled;
    }

    @ViewDebug.ExportedProperty
    public boolean isSmoothScrollbarEnabled() {
        return this.mSmoothScrollbarEnabled;
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.mOnScrollListener = l;
        invokeOnItemScrollListener();
    }

    /* access modifiers changed from: package-private */
    public void invokeOnItemScrollListener() {
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScroll(this, this.mFirstPosition, getChildCount(), this.mItemCount);
        }
    }

    @ViewDebug.ExportedProperty
    public boolean isScrollingCacheEnabled() {
        return this.mScrollingCacheEnabled;
    }

    public void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled && !enabled) {
            clearScrollingCache();
        }
        this.mScrollingCacheEnabled = enabled;
    }

    @Override // android.view.View
    public void getFocusedRect(Rect r) {
        View view = getSelectedView();
        if (view == null || view.getParent() != this) {
            super.getFocusedRect(r);
            return;
        }
        view.getFocusedRect(r);
        offsetDescendantRectToMyCoords(view, r);
    }

    @SuppressLint("ResourceType")
    private void useDefaultSelector() {
        setSelector(getResources().getDrawable(17301602));
    }

    @ViewDebug.ExportedProperty
    public boolean isStackFromBottom() {
        return this.mStackFromBottom;
    }

    public void setStackFromBottom(boolean stackFromBottom) {
        if (this.mStackFromBottom != stackFromBottom) {
            this.mStackFromBottom = stackFromBottom;
            requestLayoutIfNecessary();
        }
    }

    /* access modifiers changed from: package-private */
    public void requestLayoutIfNecessary() {
        if (getChildCount() > 0) {
            resetList();
            requestLayout();
            invalidate();
        }
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        DebugUtil.LogDebug("data changed by onRestoreInstanceState()");
        this.mDataChanged = true;
        requestLayout();
    }

    @Override // android.view.ViewParent, android.view.View
    public void requestLayout() {
        if (!this.mBlockLayoutRequests && !this.mInLayout) {
            super.requestLayout();
        }
    }

    /* access modifiers changed from: package-private */
    public void resetList() {
        removeAllViewsInLayout();
        this.mFirstPosition = 0;
        this.mDataChanged = false;
        this.mNeedSync = false;
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        this.mSelectedTop = 0;
        this.mSelectorRect.setEmpty();
        invalidate();
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public int computeVerticalScrollExtent() {
        int count = getChildCount();
        if (count <= 0) {
            return 0;
        }
        if (!this.mSmoothScrollbarEnabled) {
            return 1;
        }
        int extent = count * 100;
        View view = getChildAt(0);
        int top = getFillChildTop();
        int height = view.getHeight();
        if (height > 0) {
            extent += (top * 100) / height;
        }
        View view2 = getChildAt(count - 1);
        int bottom = getScrollChildBottom();
        int height2 = view2.getHeight();
        if (height2 > 0) {
            return extent - (((bottom - getHeight()) * 100) / height2);
        }
        return extent;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public int computeVerticalScrollOffset() {
        int index;
        int firstPosition = this.mFirstPosition;
        int childCount = getChildCount();
        if (firstPosition < 0 || childCount <= 0) {
            return 0;
        }
        if (this.mSmoothScrollbarEnabled) {
            View view = getChildAt(0);
            int top = getFillChildTop();
            int height = view.getHeight();
            if (height > 0) {
                return Math.max(((firstPosition * 100) - ((top * 100) / height)) + ((int) ((((float) getScrollY()) / ((float) getHeight())) * ((float) this.mItemCount) * 100.0f)), 0);
            }
            return 0;
        }
        int count = this.mItemCount;
        if (firstPosition == 0) {
            index = 0;
        } else if (firstPosition + childCount == count) {
            index = count;
        } else {
            index = firstPosition + (childCount / 2);
        }
        return (int) (((float) firstPosition) + (((float) childCount) * (((float) index) / ((float) count))));
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public int computeVerticalScrollRange() {
        if (this.mSmoothScrollbarEnabled) {
            return Math.max(this.mItemCount * 100, 0);
        }
        return this.mItemCount;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public float getTopFadingEdgeStrength() {
        int count = getChildCount();
        float fadeEdge = super.getTopFadingEdgeStrength();
        if (count == 0) {
            return fadeEdge;
        }
        if (this.mFirstPosition > 0) {
            return 1.0f;
        }
        int top = getChildAt(0).getTop();
        return top < getPaddingTop() ? ((float) (-(top - getPaddingTop()))) / ((float) getVerticalFadingEdgeLength()) : fadeEdge;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public float getBottomFadingEdgeStrength() {
        int count = getChildCount();
        float fadeEdge = super.getBottomFadingEdgeStrength();
        if (count == 0) {
            return fadeEdge;
        }
        if ((this.mFirstPosition + count) - 1 < this.mItemCount - 1) {
            return 1.0f;
        }
        int bottom = getChildAt(count - 1).getBottom();
        int height = getHeight();
        return bottom > height - getPaddingBottom() ? ((float) ((bottom - height) + getPaddingBottom())) / ((float) getVerticalFadingEdgeLength()) : fadeEdge;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mSelector == null) {
            useDefaultSelector();
        }
        Rect listPadding = this.mListPadding;
        listPadding.left = this.mSelectionLeftPadding + getPaddingLeft();
        listPadding.top = this.mSelectionTopPadding + getPaddingTop();
        listPadding.right = this.mSelectionRightPadding + getPaddingRight();
        listPadding.bottom = this.mSelectionBottomPadding + getPaddingBottom();
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_AdapterView, android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mInLayout = true;
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).forceLayout();
            }
            this.mRecycler.markChildrenDirty();
        }
        DebugUtil.m1i("onLayout");
        layoutChildren();
        this.mInLayout = false;
    }

    /* access modifiers changed from: protected */
    public void layoutChildren() {
    }

    @Override // com.stczh.otherlibs.Waterfall.PLA_AdapterView
    @ViewDebug.ExportedProperty
    public View getSelectedView() {
        return null;
    }

    public int getListPaddingTop() {
        return this.mListPadding.top;
    }

    public int getListPaddingBottom() {
        return this.mListPadding.bottom;
    }

    public int getListPaddingLeft() {
        return this.mListPadding.left;
    }

    public int getListPaddingRight() {
        return this.mListPadding.right;
    }

    /* access modifiers changed from: package-private */
    public View obtainView(int position, boolean[] isScrap) {
        View child;
        isScrap[0] = false;
        View scrapView = this.mRecycler.getScrapView(position);
        if (scrapView != null) {
            child = this.mAdapter.getView(position, scrapView, this);
            if (child != scrapView) {
                DebugUtil.m1i("obtainView");
                this.mRecycler.addScrapView(scrapView);
                if (this.mCacheColorHint != 0) {
                    child.setDrawingCacheBackgroundColor(this.mCacheColorHint);
                }
            } else {
                isScrap[0] = true;
                dispatchFinishTemporaryDetach(child);
            }
        } else {
            DebugUtil.m1i("makeView:" + position);
            child = this.mAdapter.getView(position, null, this);
            if (this.mCacheColorHint != 0) {
                child.setDrawingCacheBackgroundColor(this.mCacheColorHint);
            }
        }
        return child;
    }

    /* access modifiers changed from: package-private */
    public void positionSelector(View sel) {
        Rect selectorRect = this.mSelectorRect;
        selectorRect.set(sel.getLeft(), sel.getTop(), sel.getRight(), sel.getBottom());
        positionSelector(selectorRect.left, selectorRect.top, selectorRect.right, selectorRect.bottom);
        boolean isChildViewEnabled = this.mIsChildViewEnabled;
        if (sel.isEnabled() != isChildViewEnabled) {
            this.mIsChildViewEnabled = !isChildViewEnabled;
            refreshDrawableState();
        }
    }

    private void positionSelector(int l, int t, int r, int b) {
        this.mSelectorRect.set(l - this.mSelectionLeftPadding, t - this.mSelectionTopPadding, this.mSelectionRightPadding + r, this.mSelectionBottomPadding + b);
    }

    /* access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        boolean drawSelectorOnTop = this.mDrawSelectorOnTop;
        if (!drawSelectorOnTop) {
            drawSelector(canvas);
        }
        super.dispatchDraw(canvas);
        if (drawSelectorOnTop) {
            drawSelector(canvas);
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (getChildCount() > 0) {
            DebugUtil.LogDebug("data changed by onSizeChanged()");
            this.mDataChanged = true;
            rememberSyncState();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean touchModeDrawsInPressedState() {
        switch (this.mTouchMode) {
            case 1:
            case 2:
                return true;
            default:
                return false;
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldShowSelector() {
        return (hasFocus() && !isInTouchMode()) || touchModeDrawsInPressedState();
    }

    private void drawSelector(Canvas canvas) {
        if (shouldShowSelector() && this.mSelectorRect != null && !this.mSelectorRect.isEmpty()) {
            Drawable selector = this.mSelector;
            selector.setBounds(this.mSelectorRect);
            selector.draw(canvas);
        }
    }

    public void setDrawSelectorOnTop(boolean onTop) {
        this.mDrawSelectorOnTop = onTop;
    }

    public void setSelector(int resID) {
        setSelector(getResources().getDrawable(resID));
    }

    public void setSelector(Drawable sel) {
        if (this.mSelector != null) {
            this.mSelector.setCallback(null);
            unscheduleDrawable(this.mSelector);
        }
        this.mSelector = sel;
        Rect padding = new Rect();
        sel.getPadding(padding);
        this.mSelectionLeftPadding = padding.left;
        this.mSelectionTopPadding = padding.top;
        this.mSelectionRightPadding = padding.right;
        this.mSelectionBottomPadding = padding.bottom;
        sel.setCallback(this);
        sel.setState(getDrawableState());
    }

    public Drawable getSelector() {
        return this.mSelector;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mSelector != null) {
            this.mSelector.setState(getDrawableState());
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public int[] onCreateDrawableState(int extraSpace) {
        if (this.mIsChildViewEnabled) {
            return super.onCreateDrawableState(extraSpace);
        }
        int enabledState = ENABLED_STATE_SET[0];
        int[] state = super.onCreateDrawableState(extraSpace + 1);
        int enabledPos = -1;
        int i = state.length - 1;
        while (true) {
            if (i < 0) {
                break;
            } else if (state[i] == enabledState) {
                enabledPos = i;
                break;
            } else {
                i--;
            }
        }
        if (enabledPos < 0) {
            return state;
        }
        System.arraycopy(state, enabledPos + 1, state, enabledPos, (state.length - enabledPos) - 1);
        return state;
    }

    @Override // android.view.View
    public boolean verifyDrawable(Drawable dr) {
        return this.mSelector == dr || super.verifyDrawable(dr);
    }

    /* access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewTreeObserver treeObserver = getViewTreeObserver();
        if (treeObserver != null) {
            treeObserver.addOnTouchModeChangeListener(this);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_AdapterView, android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mRecycler.clear();
        ViewTreeObserver treeObserver = getViewTreeObserver();
        if (treeObserver != null) {
            treeObserver.removeOnTouchModeChangeListener(this);
        }
    }

    @Override // android.view.View
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        DebugUtil.m1i("onWindowFocusChanged");
        int touchMode = isInTouchMode() ? 0 : 1;
        if (!hasWindowFocus) {
            setChildrenDrawingCacheEnabled(false);
            if (this.mFlingRunnable != null) {
                removeCallbacks(this.mFlingRunnable);
                this.mFlingRunnable.endFling();
                if (getScrollY() != 0) {
                    scrollTo(getScrollX(), 0);
                    invalidate();
                }
            }
        } else if (!(touchMode == this.mLastTouchMode || this.mLastTouchMode == -1)) {
            this.mLayoutMode = 0;
            DebugUtil.m1i("onWindowFocusChanged");
            layoutChildren();
        }
        this.mLastTouchMode = touchMode;
    }

    /* access modifiers changed from: package-private */
    public ContextMenu.ContextMenuInfo createContextMenuInfo(View view, int position, long id) {
        return new AdapterContextMenuInfo(view, position, id);
    }

    private class WindowRunnnable {
        private int mOriginalAttachCount;

        private WindowRunnnable() {
        }

        /* synthetic */ WindowRunnnable(PLA_AbsListView pLA_AbsListView, WindowRunnnable windowRunnnable) {
            this();
        }

        public void rememberWindowAttachCount() {
            this.mOriginalAttachCount = PLA_AbsListView.this.getWindowAttachCount();
        }

        public boolean sameWindow() {
            return PLA_AbsListView.this.hasWindowFocus() && PLA_AbsListView.this.getWindowAttachCount() == this.mOriginalAttachCount;
        }
    }

    /* access modifiers changed from: private */
    public class PerformClick extends WindowRunnnable implements Runnable {
        View mChild;
        int mClickMotionPosition;

        private PerformClick() {
            super(PLA_AbsListView.this, null);
        }

        /* synthetic */ PerformClick(PLA_AbsListView pLA_AbsListView, PerformClick performClick) {
            this();
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!PLA_AbsListView.this.mDataChanged) {
                ListAdapter adapter = PLA_AbsListView.this.mAdapter;
                int motionPosition = this.mClickMotionPosition;
                if (adapter != null && PLA_AbsListView.this.mItemCount > 0 && motionPosition != -1 && motionPosition < adapter.getCount() && sameWindow()) {
                    PLA_AbsListView.this.performItemClick(this.mChild, motionPosition, adapter.getItemId(motionPosition));
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return this.mContextMenuInfo;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public boolean showContextMenuForChild(View originalView) {
        int longPressPosition = getPositionForView(originalView);
        if (longPressPosition < 0) {
            return false;
        }
        long longPressId = this.mAdapter.getItemId(longPressPosition);
        boolean handled = false;
        if (this.mOnItemLongClickListener != null) {
            handled = this.mOnItemLongClickListener.onItemLongClick(this, originalView, longPressPosition, longPressId);
        }
        if (handled) {
            return handled;
        }
        this.mContextMenuInfo = createContextMenuInfo(getChildAt(longPressPosition - this.mFirstPosition), longPressPosition, longPressId);
        return super.showContextMenuForChild(originalView);
    }

    @Override // android.view.KeyEvent.Callback, android.view.View
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void dispatchSetPressed(boolean pressed) {
    }

    @SuppressLint("WrongConstant")
    public int pointToPosition(int x, int y) {
        Rect frame = this.mTouchFrame;
        if (frame == null) {
            this.mTouchFrame = new Rect();
            frame = this.mTouchFrame;
        }
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return this.mFirstPosition + i;
                }
            }
        }
        return -1;
    }

    public long pointToRowId(int x, int y) {
        int position = pointToPosition(x, y);
        if (position >= 0) {
            return this.mAdapter.getItemId(position);
        }
        return Long.MIN_VALUE;
    }

    /* access modifiers changed from: package-private */
    public final class CheckForTap implements Runnable {
        CheckForTap() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Drawable d;
            if (PLA_AbsListView.this.mTouchMode == 0) {
                PLA_AbsListView.this.mTouchMode = 1;
                View child = PLA_AbsListView.this.getChildAt(PLA_AbsListView.this.mMotionPosition - PLA_AbsListView.this.mFirstPosition);
                if (child != null && !child.hasFocusable()) {
                    PLA_AbsListView.this.mLayoutMode = 0;
                    if (!PLA_AbsListView.this.mDataChanged) {
                        PLA_AbsListView.this.layoutChildren();
                        child.setPressed(true);
                        PLA_AbsListView.this.positionSelector(child);
                        PLA_AbsListView.this.setPressed(true);
                        int longPressTimeout = ViewConfiguration.getLongPressTimeout();
                        boolean longClickable = PLA_AbsListView.this.isLongClickable();
                        if (!(PLA_AbsListView.this.mSelector == null || (d = PLA_AbsListView.this.mSelector.getCurrent()) == null || !(d instanceof TransitionDrawable))) {
                            if (longClickable) {
                                ((TransitionDrawable) d).startTransition(longPressTimeout);
                            } else {
                                ((TransitionDrawable) d).resetTransition();
                            }
                        }
                        if (!longClickable) {
                            PLA_AbsListView.this.mTouchMode = 2;
                            return;
                        }
                        return;
                    }
                    PLA_AbsListView.this.mTouchMode = 2;
                }
            }
        }
    }

    private boolean startScrollIfNeeded(int deltaY) {
        if (Math.abs(deltaY) <= this.mTouchSlop) {
            return false;
        }
        createScrollingCache();
        this.mTouchMode = 3;
        this.mMotionCorrection = deltaY;
        setPressed(false);
        View motionView = getChildAt(this.mMotionPosition - this.mFirstPosition);
        if (motionView != null) {
            motionView.setPressed(false);
        }
        reportScrollStateChange(1);
        requestDisallowInterceptTouchEvent(true);
        return true;
    }

    @Override // android.view.ViewTreeObserver.OnTouchModeChangeListener
    public void onTouchModeChanged(boolean isInTouchMode) {
        if (isInTouchMode && getHeight() > 0 && getChildCount() > 0) {
            layoutChildren();
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent ev) {
        Drawable d;
        int incrementalDeltaY;
        if (isEnabled()) {
            int action = ev.getAction();
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(ev);
            switch (action & 255) {
                case 0:
                    this.mActivePointerId = ev.getPointerId(0);
                    int x = (int) ev.getX();
                    int y = (int) ev.getY();
                    int motionPosition = pointToPosition(x, y);
                    if (!this.mDataChanged) {
                        if (this.mTouchMode != 4 && motionPosition >= 0 && ((ListAdapter) getAdapter()).isEnabled(motionPosition)) {
                            this.mTouchMode = 0;
                            if (this.mPendingCheckForTap == null) {
                                this.mPendingCheckForTap = new CheckForTap();
                            }
                            postDelayed(this.mPendingCheckForTap, (long) ViewConfiguration.getTapTimeout());
                        } else if (ev.getEdgeFlags() != 0 && motionPosition < 0) {
                            return false;
                        } else {
                            if (this.mTouchMode == 4) {
                                createScrollingCache();
                                this.mTouchMode = 3;
                                this.mMotionCorrection = 0;
                                motionPosition = findMotionRow(y);
                                reportScrollStateChange(1);
                            }
                        }
                    }
                    if (motionPosition >= 0) {
                        this.mMotionViewOriginalTop = getChildAt(motionPosition - this.mFirstPosition).getTop();
                    }
                    this.mMotionX = x;
                    this.mMotionY = y;
                    this.mMotionPosition = motionPosition;
                    this.mLastY = Integer.MIN_VALUE;
                    break;
                case 1:
                    switch (this.mTouchMode) {
                        case 0:
                        case 1:
                        case 2:
                            int motionPosition2 = this.mMotionPosition;
                            final View child = getChildAt(motionPosition2 - this.mFirstPosition);
                            if (child != null && !child.hasFocusable()) {
                                if (this.mTouchMode != 0) {
                                    child.setPressed(false);
                                }
                                if (this.mPerformClick == null) {
                                    this.mPerformClick = new PerformClick(this, null);
                                }
                                final PerformClick performClick = this.mPerformClick;
                                performClick.mChild = child;
                                performClick.mClickMotionPosition = motionPosition2;
                                performClick.rememberWindowAttachCount();
                                this.mResurrectToPosition = motionPosition2;
                                if (this.mTouchMode == 0 || this.mTouchMode == 1) {
                                    this.mLayoutMode = 0;
                                    if (this.mDataChanged || !this.mAdapter.isEnabled(motionPosition2)) {
                                        this.mTouchMode = -1;
                                    } else {
                                        this.mTouchMode = 1;
                                        layoutChildren();
                                        child.setPressed(true);
                                        positionSelector(child);
                                        setPressed(true);
                                        if (!(this.mSelector == null || (d = this.mSelector.getCurrent()) == null || !(d instanceof TransitionDrawable))) {
                                            ((TransitionDrawable) d).resetTransition();
                                        }
                                        postDelayed(new Runnable() { // from class: com.stczh.otherlibs.Waterfall.PLA_AbsListView.1
                                            @Override // java.lang.Runnable
                                            public void run() {
                                                child.setPressed(false);
                                                PLA_AbsListView.this.setPressed(false);
                                                if (!PLA_AbsListView.this.mDataChanged) {
                                                    PLA_AbsListView.this.post(performClick);
                                                }
                                                PLA_AbsListView.this.mTouchMode = -1;
                                            }
                                        }, (long) ViewConfiguration.getPressedStateDuration());
                                    }
                                    return true;
                                } else if (!this.mDataChanged && this.mAdapter.isEnabled(motionPosition2)) {
                                    post(performClick);
                                }
                            }
                            this.mTouchMode = -1;
                            break;
                        case 3:
                            int childCount = getChildCount();
                            if (childCount <= 0) {
                                this.mTouchMode = -1;
                                reportScrollStateChange(0);
                                break;
                            } else {
                                int top = getFillChildTop();
                                int bottom = getFillChildBottom();
                                if (this.mFirstPosition == 0 && top >= this.mListPadding.top && this.mFirstPosition + childCount < this.mItemCount && bottom <= getHeight() - this.mListPadding.bottom) {
                                    this.mTouchMode = -1;
                                    reportScrollStateChange(0);
                                    break;
                                } else {
                                    VelocityTracker velocityTracker = this.mVelocityTracker;
                                    velocityTracker.computeCurrentVelocity(easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX, (float) this.mMaximumVelocity);
                                    int initialVelocity = (int) velocityTracker.getYVelocity(this.mActivePointerId);
                                    if (Math.abs(initialVelocity) <= this.mMinimumVelocity) {
                                        this.mTouchMode = -1;
                                        reportScrollStateChange(0);
                                        break;
                                    } else {
                                        if (this.mFlingRunnable == null) {
                                            this.mFlingRunnable = new FlingRunnable();
                                        }
                                        reportScrollStateChange(2);
                                        this.mFlingRunnable.start(-initialVelocity);
                                        break;
                                    }
                                }
                            }
                    }
                    setPressed(false);
                    invalidate();
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                    }
                    this.mActivePointerId = -1;
                    break;
                case 2:
                    int y2 = (int) ev.getY(ev.findPointerIndex(this.mActivePointerId));
                    int deltaY = y2 - this.mMotionY;
                    switch (this.mTouchMode) {
                        case 0:
                        case 1:
                        case 2:
                            startScrollIfNeeded(deltaY);
                            break;
                        case 3:
                            if (y2 != this.mLastY) {
                                int deltaY2 = deltaY - this.mMotionCorrection;
                                if (this.mLastY != Integer.MIN_VALUE) {
                                    incrementalDeltaY = y2 - this.mLastY;
                                } else {
                                    incrementalDeltaY = deltaY2;
                                }
                                boolean atEdge = false;
                                if (incrementalDeltaY != 0) {
                                    atEdge = trackMotionScroll(deltaY2, incrementalDeltaY);
                                }
                                if (atEdge && getChildCount() > 0) {
                                    int motionPosition3 = findMotionRow(y2);
                                    if (motionPosition3 >= 0) {
                                        this.mMotionViewOriginalTop = getChildAt(motionPosition3 - this.mFirstPosition).getTop();
                                    }
                                    this.mMotionY = y2;
                                    this.mMotionPosition = motionPosition3;
                                    invalidate();
                                }
                                this.mLastY = y2;
                                break;
                            }
                            break;
                    }
                case 3:
                    this.mTouchMode = -1;
                    setPressed(false);
                    View motionView = getChildAt(this.mMotionPosition - this.mFirstPosition);
                    if (motionView != null) {
                        motionView.setPressed(false);
                    }
                    clearScrollingCache();
                    if (this.mVelocityTracker != null) {
                        this.mVelocityTracker.recycle();
                        this.mVelocityTracker = null;
                    }
                    this.mActivePointerId = -1;
                    break;
                case 6:
                    onSecondaryPointerUp(ev);
                    int x2 = this.mMotionX;
                    int y3 = this.mMotionY;
                    int motionPosition4 = pointToPosition(x2, y3);
                    if (motionPosition4 >= 0) {
                        this.mMotionViewOriginalTop = getChildAt(motionPosition4 - this.mFirstPosition).getTop();
                        this.mMotionPosition = motionPosition4;
                    }
                    this.mLastY = y3;
                    break;
            }
            return true;
        } else if (isClickable() || isLongClickable()) {
            return true;
        } else {
            return false;
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction() & 255) {
            case 0:
                int touchMode = this.mTouchMode;
                int x = (int) ev.getX();
                int y = (int) ev.getY();
                this.mActivePointerId = ev.getPointerId(0);
                int motionPosition = findMotionRow(y);
                if (touchMode != 4 && motionPosition >= 0) {
                    this.mMotionViewOriginalTop = getChildAt(motionPosition - this.mFirstPosition).getTop();
                    this.mMotionX = x;
                    this.mMotionY = y;
                    this.mMotionPosition = motionPosition;
                    this.mTouchMode = 0;
                    clearScrollingCache();
                }
                this.mLastY = Integer.MIN_VALUE;
                if (touchMode == 4) {
                    return true;
                }
                break;
            case 1:
                this.mTouchMode = -1;
                this.mActivePointerId = -1;
                reportScrollStateChange(0);
                break;
            case 2:
                switch (this.mTouchMode) {
                    case 0:
                        if (startScrollIfNeeded(((int) ev.getY(ev.findPointerIndex(this.mActivePointerId))) - this.mMotionY)) {
                            return true;
                        }
                        break;
                }
            case 6:
                onSecondaryPointerUp(ev);
                break;
        }
        return false;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & MotionEventCompat.ACTION_POINTER_INDEX_MASK) >> 8;
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mMotionX = (int) ev.getX(newPointerIndex);
            this.mMotionY = (int) ev.getY(newPointerIndex);
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.clear();
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void addTouchables(ArrayList<View> views) {
        int count = getChildCount();
        int firstPosition = this.mFirstPosition;
        ListAdapter adapter = this.mAdapter;
        if (adapter != null) {
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (adapter.isEnabled(firstPosition + i)) {
                    views.add(child);
                }
                child.addTouchables(views);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void reportScrollStateChange(int newState) {
        if (newState != this.mLastScrollState && this.mOnScrollListener != null) {
            this.mOnScrollListener.onScrollStateChanged(this, newState);
            this.mLastScrollState = newState;
        }
    }

    /* access modifiers changed from: private */
    public class FlingRunnable implements Runnable {
        private int mLastFlingY;
        private final Scroller mScroller;

        FlingRunnable() {
            this.mScroller = new Scroller(PLA_AbsListView.this.getContext());
        }

        /* access modifiers changed from: package-private */
        public void start(int initialVelocity) {
            int initialY;
            int initialVelocity2 = PLA_AbsListView.this.modifyFlingInitialVelocity(initialVelocity);
            if (initialVelocity2 < 0) {
                initialY = Integer.MAX_VALUE;
            } else {
                initialY = 0;
            }
            this.mLastFlingY = initialY;
            this.mScroller.fling(0, initialY, 0, initialVelocity2, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
            PLA_AbsListView.this.mTouchMode = 4;
            PLA_AbsListView.this.post(this);
        }

        /* access modifiers changed from: package-private */
        public void startScroll(int distance, int duration) {
            int initialY;
            if (distance < 0) {
                initialY = Integer.MAX_VALUE;
            } else {
                initialY = 0;
            }
            this.mLastFlingY = initialY;
            this.mScroller.startScroll(0, initialY, 0, distance, duration);
            PLA_AbsListView.this.mTouchMode = 4;
            PLA_AbsListView.this.post(this);
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void endFling() {
            this.mLastFlingY = 0;
            PLA_AbsListView.this.mTouchMode = -1;
            PLA_AbsListView.this.reportScrollStateChange(0);
            PLA_AbsListView.this.clearScrollingCache();
            PLA_AbsListView.this.removeCallbacks(this);
            if (PLA_AbsListView.this.mPositionScroller != null) {
                PLA_AbsListView.this.removeCallbacks(PLA_AbsListView.this.mPositionScroller);
            }
            this.mScroller.forceFinished(true);
        }

        @Override // java.lang.Runnable
        public void run() {
            int delta;
            switch (PLA_AbsListView.this.mTouchMode) {
                case 4:
                    if (PLA_AbsListView.this.mItemCount == 0 || PLA_AbsListView.this.getChildCount() == 0) {
                        endFling();
                        return;
                    }
                    Scroller scroller = this.mScroller;
                    boolean more = scroller.computeScrollOffset();
                    int y = scroller.getCurrY();
                    int delta2 = this.mLastFlingY - y;
                    if (delta2 > 0) {
                        PLA_AbsListView.this.mMotionPosition = PLA_AbsListView.this.mFirstPosition;
                        PLA_AbsListView.this.mMotionViewOriginalTop = PLA_AbsListView.this.getScrollChildTop();
                        delta = Math.min(((PLA_AbsListView.this.getHeight() - PLA_AbsListView.this.getPaddingBottom()) - PLA_AbsListView.this.getPaddingTop()) - 1, delta2);
                    } else {
                        PLA_AbsListView.this.mMotionPosition = PLA_AbsListView.this.mFirstPosition + (PLA_AbsListView.this.getChildCount() - 1);
                        PLA_AbsListView.this.mMotionViewOriginalTop = PLA_AbsListView.this.getScrollChildBottom();
                        delta = Math.max(-(((PLA_AbsListView.this.getHeight() - PLA_AbsListView.this.getPaddingBottom()) - PLA_AbsListView.this.getPaddingTop()) - 1), delta2);
                    }
                    boolean atEnd = PLA_AbsListView.this.trackMotionScroll(delta, delta);
                    if (!more || atEnd) {
                        endFling();
                        return;
                    }
                    PLA_AbsListView.this.invalidate();
                    this.mLastFlingY = y;
                    PLA_AbsListView.this.post(this);
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public class PositionScroller implements Runnable {
        private static final int MOVE_DOWN_BOUND = 3;
        private static final int MOVE_DOWN_POS = 1;
        private static final int MOVE_UP_BOUND = 4;
        private static final int MOVE_UP_POS = 2;
        private static final int SCROLL_DURATION = 400;
        private int mBoundPos;
        private final int mExtraScroll;
        private int mLastSeenPos;
        private int mMode;
        private int mScrollDuration;
        private int mTargetPos;

        PositionScroller() {
            this.mExtraScroll = ViewConfiguration.get(PLA_AbsListView.this.getContext()).getScaledFadingEdgeLength();
        }

        /* access modifiers changed from: package-private */
        public void start(int position) {
            int viewTravelCount;
            int firstPos = PLA_AbsListView.this.mFirstPosition;
            int lastPos = (PLA_AbsListView.this.getChildCount() + firstPos) - 1;
            if (position <= firstPos) {
                viewTravelCount = (firstPos - position) + 1;
                this.mMode = 2;
            } else if (position >= lastPos) {
                viewTravelCount = (position - lastPos) + 1;
                this.mMode = 1;
            } else {
                return;
            }
            if (viewTravelCount > 0) {
                this.mScrollDuration = SCROLL_DURATION / viewTravelCount;
            } else {
                this.mScrollDuration = SCROLL_DURATION;
            }
            this.mTargetPos = position;
            this.mBoundPos = -1;
            this.mLastSeenPos = -1;
            PLA_AbsListView.this.post(this);
        }

        /* access modifiers changed from: package-private */
        public void start(int position, int boundPosition) {
            int boundPosFromFirst;
            int viewTravelCount;
            if (boundPosition == -1) {
                start(position);
                return;
            }
            int firstPos = PLA_AbsListView.this.mFirstPosition;
            int lastPos = (PLA_AbsListView.this.getChildCount() + firstPos) - 1;
            if (position <= firstPos) {
                int boundPosFromLast = lastPos - boundPosition;
                if (boundPosFromLast >= 1) {
                    int posTravel = (firstPos - position) + 1;
                    int boundTravel = boundPosFromLast - 1;
                    if (boundTravel < posTravel) {
                        viewTravelCount = boundTravel;
                        this.mMode = 4;
                    } else {
                        viewTravelCount = posTravel;
                        this.mMode = 2;
                    }
                } else {
                    return;
                }
            } else if (position >= lastPos && (boundPosFromFirst = boundPosition - firstPos) >= 1) {
                int posTravel2 = (position - lastPos) + 1;
                int boundTravel2 = boundPosFromFirst - 1;
                if (boundTravel2 < posTravel2) {
                    viewTravelCount = boundTravel2;
                    this.mMode = 3;
                } else {
                    viewTravelCount = posTravel2;
                    this.mMode = 1;
                }
            } else {
                return;
            }
            if (viewTravelCount > 0) {
                this.mScrollDuration = SCROLL_DURATION / viewTravelCount;
            } else {
                this.mScrollDuration = SCROLL_DURATION;
            }
            this.mTargetPos = position;
            this.mBoundPos = boundPosition;
            this.mLastSeenPos = -1;
            PLA_AbsListView.this.post(this);
        }

        /* access modifiers changed from: package-private */
        public void stop() {
            PLA_AbsListView.this.removeCallbacks(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            int extraScroll;
            int extraScroll2;
            int listHeight = PLA_AbsListView.this.getHeight();
            int firstPos = PLA_AbsListView.this.mFirstPosition;
            switch (this.mMode) {
                case 1:
                    int lastViewIndex = PLA_AbsListView.this.getChildCount() - 1;
                    int lastPos = firstPos + lastViewIndex;
                    if (lastViewIndex < 0) {
                        return;
                    }
                    if (lastPos == this.mLastSeenPos) {
                        PLA_AbsListView.this.post(this);
                        return;
                    }
                    View lastView = PLA_AbsListView.this.getChildAt(lastViewIndex);
                    int lastViewHeight = lastView.getHeight();
                    int lastViewPixelsShowing = listHeight - lastView.getTop();
                    if (lastPos < PLA_AbsListView.this.mItemCount - 1) {
                        extraScroll2 = this.mExtraScroll;
                    } else {
                        extraScroll2 = PLA_AbsListView.this.mListPadding.bottom;
                    }
                    PLA_AbsListView.this.smoothScrollBy((lastViewHeight - lastViewPixelsShowing) + extraScroll2, this.mScrollDuration);
                    this.mLastSeenPos = lastPos;
                    if (lastPos < this.mTargetPos) {
                        PLA_AbsListView.this.post(this);
                        return;
                    }
                    return;
                case 2:
                    if (firstPos == this.mLastSeenPos) {
                        PLA_AbsListView.this.post(this);
                        return;
                    }
                    View firstView = PLA_AbsListView.this.getChildAt(0);
                    if (firstView != null) {
                        int firstViewTop = firstView.getTop();
                        if (firstPos > 0) {
                            extraScroll = this.mExtraScroll;
                        } else {
                            extraScroll = PLA_AbsListView.this.mListPadding.top;
                        }
                        PLA_AbsListView.this.smoothScrollBy(firstViewTop - extraScroll, this.mScrollDuration);
                        this.mLastSeenPos = firstPos;
                        if (firstPos > this.mTargetPos) {
                            PLA_AbsListView.this.post(this);
                            return;
                        }
                        return;
                    }
                    return;
                case 3:
                    int childCount = PLA_AbsListView.this.getChildCount();
                    if (firstPos != this.mBoundPos && childCount > 1 && firstPos + childCount < PLA_AbsListView.this.mItemCount) {
                        int nextPos = firstPos + 1;
                        if (nextPos == this.mLastSeenPos) {
                            PLA_AbsListView.this.post(this);
                            return;
                        }
                        View nextView = PLA_AbsListView.this.getChildAt(1);
                        int nextViewHeight = nextView.getHeight();
                        int nextViewTop = nextView.getTop();
                        int extraScroll3 = this.mExtraScroll;
                        if (nextPos < this.mBoundPos) {
                            PLA_AbsListView.this.smoothScrollBy(Math.max(0, (nextViewHeight + nextViewTop) - extraScroll3), this.mScrollDuration);
                            this.mLastSeenPos = nextPos;
                            PLA_AbsListView.this.post(this);
                            return;
                        } else if (nextViewTop > extraScroll3) {
                            PLA_AbsListView.this.smoothScrollBy(nextViewTop - extraScroll3, this.mScrollDuration);
                            return;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                case 4:
                    int lastViewIndex2 = PLA_AbsListView.this.getChildCount() - 2;
                    if (lastViewIndex2 >= 0) {
                        int lastPos2 = firstPos + lastViewIndex2;
                        if (lastPos2 == this.mLastSeenPos) {
                            PLA_AbsListView.this.post(this);
                            return;
                        }
                        View lastView2 = PLA_AbsListView.this.getChildAt(lastViewIndex2);
                        int lastViewHeight2 = lastView2.getHeight();
                        int lastViewTop = lastView2.getTop();
                        int lastViewPixelsShowing2 = listHeight - lastViewTop;
                        this.mLastSeenPos = lastPos2;
                        if (lastPos2 > this.mBoundPos) {
                            PLA_AbsListView.this.smoothScrollBy(-(lastViewPixelsShowing2 - this.mExtraScroll), this.mScrollDuration);
                            PLA_AbsListView.this.post(this);
                            return;
                        }
                        int bottom = listHeight - this.mExtraScroll;
                        int lastViewBottom = lastViewTop + lastViewHeight2;
                        if (bottom > lastViewBottom) {
                            PLA_AbsListView.this.smoothScrollBy(-(bottom - lastViewBottom), this.mScrollDuration);
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void smoothScrollToPosition(int position) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = new PositionScroller();
        }
        this.mPositionScroller.start(position);
    }

    public void smoothScrollToPosition(int position, int boundPosition) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = new PositionScroller();
        }
        this.mPositionScroller.start(position, boundPosition);
    }

    public void smoothScrollBy(int distance, int duration) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new FlingRunnable();
        } else {
            this.mFlingRunnable.endFling();
        }
        this.mFlingRunnable.startScroll(distance, duration);
    }

    private void createScrollingCache() {
        if (this.mScrollingCacheEnabled && !this.mCachingStarted) {
            setChildrenDrawnWithCacheEnabled(true);
            setChildrenDrawingCacheEnabled(true);
            this.mCachingStarted = true;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void clearScrollingCache() {
        if (this.mClearScrollingCache == null) {
            this.mClearScrollingCache = new Runnable() { // from class: com.stczh.otherlibs.Waterfall.PLA_AbsListView.2
                @Override // java.lang.Runnable
                public void run() {
                    if (PLA_AbsListView.this.mCachingStarted) {
                        PLA_AbsListView.this.mCachingStarted = false;
                        PLA_AbsListView.this.setChildrenDrawnWithCacheEnabled(false);
                        if ((PLA_AbsListView.this.getPersistentDrawingCache() & 2) == 0) {
                            PLA_AbsListView.this.setChildrenDrawingCacheEnabled(false);
                        }
                        if (!PLA_AbsListView.this.isAlwaysDrawnWithCacheEnabled()) {
                            PLA_AbsListView.this.invalidate();
                        }
                    }
                }
            };
        }
        post(this.mClearScrollingCache);
    }

    /* access modifiers changed from: package-private */
    public boolean trackMotionScroll(int deltaY, int incrementalDeltaY) {
        int deltaY2;
        int incrementalDeltaY2;
        int childCount = getChildCount();
        if (childCount == 0) {
            return true;
        }
        int firstTop = getScrollChildTop();
        int lastBottom = getScrollChildBottom();
        Rect listPadding = this.mListPadding;
        int end = getHeight() - listPadding.bottom;
        int spaceAbove = listPadding.top - getFillChildTop();
        int spaceBelow = getFillChildBottom() - end;
        int height = (getHeight() - getPaddingBottom()) - getPaddingTop();
        if (deltaY < 0) {
            deltaY2 = Math.max(-(height - 1), deltaY);
        } else {
            deltaY2 = Math.min(height - 1, deltaY);
        }
        if (incrementalDeltaY < 0) {
            incrementalDeltaY2 = Math.max((-(height - 1)) / 2, incrementalDeltaY);
        } else {
            incrementalDeltaY2 = Math.min((height - 1) / 2, incrementalDeltaY);
        }
        int firstPosition = this.mFirstPosition;
        if (firstPosition == 0 && firstTop >= listPadding.top && deltaY2 >= 0) {
            return true;
        }
        if (firstPosition + childCount == this.mItemCount && lastBottom <= end && deltaY2 <= 0) {
            return true;
        }
        boolean down = incrementalDeltaY2 < 0;
        int headerViewsCount = getHeaderViewsCount();
        int footerViewsStart = this.mItemCount - getFooterViewsCount();
        int start = 0;
        int count = 0;
        if (!down) {
            int bottom = (getHeight() - listPadding.bottom) - incrementalDeltaY2;
            for (int i = childCount - 1; i >= 0; i--) {
                View child = getChildAt(i);
                if (child.getTop() <= bottom) {
                    break;
                }
                start = i;
                count++;
                int position = firstPosition + i;
                if (position >= headerViewsCount && position < footerViewsStart) {
                    this.mRecycler.addScrapView(child);
                }
            }
        } else {
            int top = listPadding.top - incrementalDeltaY2;
            for (int i2 = 0; i2 < childCount; i2++) {
                View child2 = getChildAt(i2);
                if (child2.getBottom() >= top) {
                    break;
                }
                count++;
                int position2 = firstPosition + i2;
                if (position2 >= headerViewsCount && position2 < footerViewsStart) {
                    this.mRecycler.addScrapView(child2);
                }
            }
        }
        this.mMotionViewNewTop = this.mMotionViewOriginalTop + deltaY2;
        this.mBlockLayoutRequests = true;
        if (count > 0) {
            detachViewsFromParent(start, count);
        }
        tryOffsetChildrenTopAndBottom(incrementalDeltaY2);
        if (down) {
            this.mFirstPosition += count;
        }
        invalidate();
        int absIncrementalDeltaY = Math.abs(incrementalDeltaY2);
        if (spaceAbove < absIncrementalDeltaY || spaceBelow < absIncrementalDeltaY) {
            fillGap(down);
        }
        this.mBlockLayoutRequests = false;
        invokeOnItemScrollListener();
        awakenScrollBars();
        return false;
    }

    /* access modifiers changed from: protected */
    public void tryOffsetChildrenTopAndBottom(int offset) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).offsetTopAndBottom(offset);
        }
    }

    /* access modifiers changed from: package-private */
    public int getHeaderViewsCount() {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int getFooterViewsCount() {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public int reconcileSelectedPosition() {
        int position = this.mSelectedPosition;
        if (position < 0) {
            position = this.mResurrectToPosition;
        }
        return Math.min(Math.max(0, position), this.mItemCount - 1);
    }

    /* access modifiers changed from: package-private */
    public int findClosestMotionRow(int y) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return -1;
        }
        int motionRow = findMotionRow(y);
        return motionRow == -1 ? (this.mFirstPosition + childCount) - 1 : motionRow;
    }

    public void invalidateViews() {
        DebugUtil.LogDebug("data changed by invalidateViews()");
        this.mDataChanged = true;
        rememberSyncState();
        requestLayout();
        invalidate();
    }

    /* access modifiers changed from: protected */
    @Override // com.stczh.otherlibs.Waterfall.PLA_AdapterView
    public void handleDataChanged() {
        int i = 3;
        int count = this.mItemCount;
        if (count > 0) {
            if (this.mNeedSync) {
                this.mNeedSync = false;
                if (this.mTranscriptMode == 2 || (this.mTranscriptMode == 1 && this.mFirstPosition + getChildCount() >= this.mOldItemCount)) {
                    this.mLayoutMode = 3;
                    return;
                }
                switch (this.mSyncMode) {
                    case 1:
                        this.mLayoutMode = 5;
                        this.mSyncPosition = Math.min(Math.max(0, this.mSyncPosition), count - 1);
                        return;
                }
            }
            if (!isInTouchMode()) {
                int newPos = getSelectedItemPosition();
                if (newPos >= count) {
                    newPos = count - 1;
                }
                if (newPos < 0) {
                    newPos = 0;
                }
                lookForSelectablePosition(newPos, true);
                if (lookForSelectablePosition(newPos, false) >= 0) {
                    return;
                }
            } else if (this.mResurrectToPosition >= 0) {
                return;
            }
        }
        if (!this.mStackFromBottom) {
            i = 1;
        }
        this.mLayoutMode = i;
        this.mNeedSync = false;
    }

    /* access modifiers changed from: protected */
    public void onLayoutSync(int syncPosition) {
    }

    /* access modifiers changed from: protected */
    public void onLayoutSyncFinished(int syncPosition) {
    }

    static int getDistance(Rect source, Rect dest, int direction) {
        int sX;
        int sY;
        int dX;
        int dY;
        switch (direction) {
            case 17:
                sX = source.left;
                sY = source.top + (source.height() / 2);
                dX = dest.right;
                dY = dest.top + (dest.height() / 2);
                break;
            case 33:
                sX = source.left + (source.width() / 2);
                sY = source.top;
                dX = dest.left + (dest.width() / 2);
                dY = dest.bottom;
                break;
            case /*R.styleable.View_accessibilityFocusable*/ 66/* 66 */:
                sX = source.right;
                sY = source.top + (source.height() / 2);
                dX = dest.left;
                dY = dest.top + (dest.height() / 2);
                break;
            case TransportMediator.KEYCODE_MEDIA_RECORD /* 130 */:
                sX = source.left + (source.width() / 2);
                sY = source.bottom;
                dX = dest.left + (dest.width() / 2);
                dY = dest.top;
                break;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT}.");
        }
        int deltaX = dX - sX;
        int deltaY = dY - sY;
        return (deltaY * deltaY) + (deltaX * deltaX);
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
    }

    /* access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override // android.view.ViewGroup
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* access modifiers changed from: protected */
    @Override // android.view.ViewGroup
    public boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public void setTranscriptMode(int mode) {
        this.mTranscriptMode = mode;
    }

    public int getTranscriptMode() {
        return this.mTranscriptMode;
    }

    @Override // android.view.View
    public int getSolidColor() {
        return this.mCacheColorHint;
    }

    public void setCacheColorHint(int color) {
        if (color != this.mCacheColorHint) {
            this.mCacheColorHint = color;
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).setDrawingCacheBackgroundColor(color);
            }
            this.mRecycler.setCacheColorHint(color);
        }
    }

    public int getCacheColorHint() {
        return this.mCacheColorHint;
    }

    public void reclaimViews(List<View> views) {
        int childCount = getChildCount();
        RecyclerListener listener = this.mRecycler.mRecyclerListener;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp != null && this.mRecycler.shouldRecycleViewType(lp.viewType)) {
                views.add(child);
                if (listener != null) {
                    listener.onMovedToScrapHeap(child);
                }
            }
        }
        this.mRecycler.reclaimScrapViews(views);
        removeAllViewsInLayout();
    }

    public void setRecyclerListener(RecyclerListener listener) {
        this.mRecycler.mRecyclerListener = listener;
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        @ViewDebug.ExportedProperty
        public boolean forceAdd;
        @ViewDebug.ExportedProperty
        public boolean recycledHeaderFooter;
        public int scrappedFromPosition;
        @ViewDebug.ExportedProperty(mapping = {@ViewDebug.IntToString(from = -1, to = "ITEM_VIEW_TYPE_IGNORE"), @ViewDebug.IntToString(from = -2, to = "ITEM_VIEW_TYPE_HEADER_OR_FOOTER")})
        public int viewType;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(int w, int h, int viewType2) {
            super(w, h);
            this.viewType = viewType2;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    /* access modifiers changed from: package-private */
    public class RecycleBin {
        private View[] mActiveViews = new View[0];
        private Stack<View> mCurrentScrap;
        private int mFirstActivePosition;
        private RecyclerListener mRecyclerListener;
        private Stack<View>[] mScrapViews;
        private int mViewTypeCount;

        RecycleBin() {
        }

        public void setViewTypeCount(int viewTypeCount) {
            if (viewTypeCount < 1) {
                throw new IllegalArgumentException("Can't have a viewTypeCount < 1");
            }
            Stack[] scrapViews = new Stack[viewTypeCount];
            for (int i = 0; i < viewTypeCount; i++) {
                scrapViews[i] = new Stack();
            }
            this.mViewTypeCount = viewTypeCount;
            this.mCurrentScrap = scrapViews[0];
            this.mScrapViews = scrapViews;
        }

        public void markChildrenDirty() {
            if (this.mViewTypeCount == 1) {
                Stack<View> scrap = this.mCurrentScrap;
                int scrapCount = scrap.size();
                for (int i = 0; i < scrapCount; i++) {
                    scrap.get(i).forceLayout();
                }
                return;
            }
            int typeCount = this.mViewTypeCount;
            for (int i2 = 0; i2 < typeCount; i2++) {
                Stack<View> scrap2 = this.mScrapViews[i2];
                int scrapCount2 = scrap2.size();
                for (int j = 0; j < scrapCount2; j++) {
                    scrap2.get(j).forceLayout();
                }
            }
        }

        public boolean shouldRecycleViewType(int viewType) {
            return viewType >= 0;
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            if (this.mViewTypeCount == 1) {
                Stack<View> scrap = this.mCurrentScrap;
                int scrapCount = scrap.size();
                for (int i = 0; i < scrapCount; i++) {
                    PLA_AbsListView.this.removeDetachedView(scrap.remove((scrapCount - 1) - i), false);
                }
                return;
            }
            int typeCount = this.mViewTypeCount;
            for (int i2 = 0; i2 < typeCount; i2++) {
                Stack<View> scrap2 = this.mScrapViews[i2];
                int scrapCount2 = scrap2.size();
                for (int j = 0; j < scrapCount2; j++) {
                    PLA_AbsListView.this.removeDetachedView(scrap2.remove((scrapCount2 - 1) - j), false);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void fillActiveViews(int childCount, int firstActivePosition) {
            if (this.mActiveViews.length < childCount) {
                this.mActiveViews = new View[childCount];
            }
            this.mFirstActivePosition = firstActivePosition;
            View[] activeViews = this.mActiveViews;
            for (int i = 0; i < childCount; i++) {
                View child = PLA_AbsListView.this.getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (!(lp == null || lp.viewType == -2)) {
                    activeViews[i] = child;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public View getActiveView(int position) {
            int index = position - this.mFirstActivePosition;
            View[] activeViews = this.mActiveViews;
            if (index < 0 || index >= activeViews.length) {
                return null;
            }
            View match = activeViews[index];
            activeViews[index] = null;
            return match;
        }

        /* access modifiers changed from: package-private */
        public View getScrapView(int position) {
            Stack<View> scrapViews;
            DebugUtil.m1i("getFromScrap: " + position);
            if (PLA_AbsListView.this.getHeaderViewsCount() > position) {
                return null;
            }
            if (this.mViewTypeCount == 1) {
                scrapViews = this.mCurrentScrap;
            } else {
                int whichScrap = PLA_AbsListView.this.mAdapter.getItemViewType(position);
                if (whichScrap < 0 || whichScrap >= this.mScrapViews.length) {
                    return null;
                }
                scrapViews = this.mScrapViews[whichScrap];
            }
            int size = scrapViews.size();
            for (int i = size - 1; i >= 0; i--) {
                if (((LayoutParams) scrapViews.get(i).getLayoutParams()).scrappedFromPosition == position) {
                    return scrapViews.remove(i);
                }
            }
            if (size > 0) {
                return scrapViews.remove(0);
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public void addScrapView(View scrap) {
            DebugUtil.m1i("addToScrap");
            LayoutParams lp = (LayoutParams) scrap.getLayoutParams();
            if (lp != null) {
                int viewType = lp.viewType;
                if (shouldRecycleViewType(viewType)) {
                    if (this.mViewTypeCount == 1) {
                        PLA_AbsListView.this.dispatchFinishTemporaryDetach(scrap);
                        this.mCurrentScrap.add(scrap);
                    } else {
                        PLA_AbsListView.this.dispatchFinishTemporaryDetach(scrap);
                        this.mScrapViews[viewType].push(scrap);
                    }
                    if (this.mRecyclerListener != null) {
                        this.mRecyclerListener.onMovedToScrapHeap(scrap);
                    }
                } else if (viewType != -2) {
                    PLA_AbsListView.this.removeDetachedView(scrap, false);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void scrapActiveViews() {
            View[] activeViews = this.mActiveViews;
            boolean hasListener = this.mRecyclerListener != null;
            boolean multipleScraps = this.mViewTypeCount > 1;
            Stack<View> scrapViews = this.mCurrentScrap;
            for (int i = activeViews.length - 1; i >= 0; i--) {
                View victim = activeViews[i];
                if (victim != null) {
                    int whichScrap = ((LayoutParams) victim.getLayoutParams()).viewType;
                    activeViews[i] = null;
                    if (shouldRecycleViewType(whichScrap)) {
                        if (multipleScraps) {
                            scrapViews = this.mScrapViews[whichScrap];
                        }
                        PLA_AbsListView.this.dispatchFinishTemporaryDetach(victim);
                        DebugUtil.m1i("addToScrap from scrapActiveViews");
                        scrapViews.add(victim);
                        if (hasListener) {
                            this.mRecyclerListener.onMovedToScrapHeap(victim);
                        }
                    } else if (whichScrap != -2) {
                        PLA_AbsListView.this.removeDetachedView(victim, false);
                    }
                }
            }
            pruneScrapViews();
        }

        private void pruneScrapViews() {
            int maxViews = this.mActiveViews.length;
            int viewTypeCount = this.mViewTypeCount;
            Stack<View>[] scrapViews = this.mScrapViews;
            for (int i = 0; i < viewTypeCount; i++) {
                Stack<View> scrapPile = scrapViews[i];
                int size = scrapPile.size();
                int extras = size - maxViews;
                int j = 0;
                int size2 = size - 1;
                while (j < extras) {
                    DebugUtil.m1i("remove scarp views from pruneScrapViews");
                    PLA_AbsListView.this.removeDetachedView(scrapPile.remove(size2), false);
                    j++;
                    size2--;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void reclaimScrapViews(List<View> views) {
            if (this.mViewTypeCount == 1) {
                views.addAll(this.mCurrentScrap);
                return;
            }
            int viewTypeCount = this.mViewTypeCount;
            Stack<View>[] scrapViews = this.mScrapViews;
            for (int i = 0; i < viewTypeCount; i++) {
                Stack<View> scrapPile = scrapViews[i];
                DebugUtil.m1i("add scarp views from reclaimScrapViews");
                views.addAll(scrapPile);
            }
        }

        /* access modifiers changed from: package-private */
        public void setCacheColorHint(int color) {
            if (this.mViewTypeCount == 1) {
                Stack<View> scrap = this.mCurrentScrap;
                int scrapCount = scrap.size();
                for (int i = 0; i < scrapCount; i++) {
                    scrap.get(i).setDrawingCacheBackgroundColor(color);
                }
            } else {
                int typeCount = this.mViewTypeCount;
                for (int i2 = 0; i2 < typeCount; i2++) {
                    Stack<View> scrap2 = this.mScrapViews[i2];
                    int scrapCount2 = scrap2.size();
                    for (int j = 0; j < scrapCount2; j++) {
                        scrap2.get(i2).setDrawingCacheBackgroundColor(color);
                    }
                }
            }
            View[] activeViews = this.mActiveViews;
            for (View victim : activeViews) {
                if (victim != null) {
                    victim.setDrawingCacheBackgroundColor(color);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void dispatchFinishTemporaryDetach(View v) {
        if (v != null) {
            v.onFinishTemporaryDetach();
            if (v instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) v;
                int count = group.getChildCount();
                for (int i = 0; i < count; i++) {
                    dispatchFinishTemporaryDetach(group.getChildAt(i));
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public int modifyFlingInitialVelocity(int initialVelocity) {
        return initialVelocity;
    }

    /* access modifiers changed from: protected */
    public int getScrollChildTop() {
        if (getChildCount() == 0) {
            return 0;
        }
        return getChildAt(0).getTop();
    }

    /* access modifiers changed from: protected */
    public int getFirstChildTop() {
        if (getChildCount() == 0) {
            return 0;
        }
        return getChildAt(0).getTop();
    }

    /* access modifiers changed from: protected */
    public int getFillChildTop() {
        if (getChildCount() == 0) {
            return 0;
        }
        return getChildAt(0).getTop();
    }

    /* access modifiers changed from: protected */
    public int getFillChildBottom() {
        int count = getChildCount();
        if (count == 0) {
            return 0;
        }
        return getChildAt(count - 1).getBottom();
    }

    /* access modifiers changed from: protected */
    public int getScrollChildBottom() {
        int count = getChildCount();
        if (count == 0) {
            return 0;
        }
        return getChildAt(count - 1).getBottom();
    }
}
