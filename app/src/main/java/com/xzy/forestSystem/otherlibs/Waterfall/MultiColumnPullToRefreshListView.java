package com.xzy.forestSystem.otherlibs.Waterfall;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.stczh.gzforestSystem.R;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MultiColumnPullToRefreshListView extends MultiColumnListView {

    /* renamed from: $SWITCH_TABLE$com$xzy$otherlibs$Waterfall$MultiColumnPullToRefreshListView$State */
    private static /* synthetic */ int[] f566x79e71b21 = null;
    private static final int BOUNCE_ANIMATION_DELAY = 20;
    private static final int BOUNCE_ANIMATION_DURATION = 215;
    static final int LOADINGBUFFER = 400;
    static final int LOADINGONE = 101;
    static final int LOADINGTHREE = 103;
    static final int LOADINGTWO = 102;
    static final int LOADINGZERO = 100;
    private static final float PULL_RESISTANCE = 3.0f;
    private static final int ROTATE_ARROW_ANIMATION_DURATION = 250;
    private static Handler mLoadingHandler = new Handler() { // from class: com.stczh.otherlibs.Waterfall.MultiColumnPullToRefreshListView.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            TextView tv = (TextView) ((WeakReference) msg.obj).get();
            if (tv != null) {
                switch (msg.what) {
                    case 100:
                        tv.setText("Loading");
                        return;
                    case 101:
                        tv.setText("Loading.");
                        return;
                    case MultiColumnPullToRefreshListView.LOADINGTWO /* 102 */:
                        tv.setText("Loading..");
                        return;
                    case MultiColumnPullToRefreshListView.LOADINGTHREE /* 103 */:
                        tv.setText("Loading...");
                        return;
                    default:
                        return;
                }
            }
        }
    };
    private static int measuredHeaderHeight;
    private TranslateAnimation bounceAnimation;
    private boolean bounceBackHeader;
    private RotateAnimation flipAnimation;
    private boolean hasResetHeader;
    private RelativeLayout header;
    private LinearLayout headerContainer;
    private int headerPadding;
    private ImageView image;
    private boolean isHeaderRefreshing = false;
    private boolean isHeaderShowing = false;
    private boolean isPulling = false;
    private long lastUpdated = -1;
    private SimpleDateFormat lastUpdatedDateFormat = new SimpleDateFormat("dd/MM HH:mm");
    private String lastUpdatedText;
    private TextView lastUpdatedTextView;
    private boolean lockScrollWhileRefreshing;
    private LoadingThread mLoadingThread = null;
    private OnRefreshListener onRefreshListener;
    private float previousY;
    private String pullToRefreshText;
    private String refreshingText;
    private String releaseToRefreshText;
    private RotateAnimation reverseFlipAnimation;
    private boolean scrollbarEnabled;
    private boolean showLastUpdatedText;
    private ProgressBar spinner;
    private State state;
    private TextView text;

    public interface OnRefreshListener {
        void onRefresh();
    }

    /* access modifiers changed from: private */
    public enum State {
        PULL_TO_REFRESH,
        RELEASE_TO_REFRESH,
        REFRESHING
    }

    /* renamed from: $SWITCH_TABLE$com$xzy$otherlibs$Waterfall$MultiColumnPullToRefreshListView$State */
    static /* synthetic */ int[] m0x79e71b21() {
        int[] iArr = f566x79e71b21;
        if (iArr == null) {
            iArr = new int[State.values().length];
            try {
                iArr[State.PULL_TO_REFRESH.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[State.REFRESHING.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[State.RELEASE_TO_REFRESH.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            f566x79e71b21 = iArr;
        }
        return iArr;
    }

    public MultiColumnPullToRefreshListView(Context context) {
        super(context);
        init(context, null);
    }

    public MultiColumnPullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MultiColumnPullToRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener2) {
        this.onRefreshListener = onRefreshListener2;
    }

    public boolean isRefreshing() {
        return this.state == State.REFRESHING;
    }

    public void setLockScrollWhileRefreshing(boolean lockScrollWhileRefreshing2) {
        this.lockScrollWhileRefreshing = lockScrollWhileRefreshing2;
    }

    @SuppressLint("WrongConstant")
    public void setShowLastUpdatedText(boolean showLastUpdatedText2) {
        this.showLastUpdatedText = showLastUpdatedText2;
        if (!showLastUpdatedText2) {
            this.lastUpdatedTextView.setVisibility(8);
        }
    }

    public void setLastUpdatedDateFormat(SimpleDateFormat lastUpdatedDateFormat2) {
        this.lastUpdatedDateFormat = lastUpdatedDateFormat2;
    }

    public void setRefreshing() {
        this.state = State.REFRESHING;
        setUiRefreshing();
    }

    public void onRefreshComplete() {
        this.state = State.PULL_TO_REFRESH;
        resetHeader();
        this.lastUpdated = System.currentTimeMillis();
    }

    @SuppressLint("WrongConstant")
    public void setTextPullToRefresh(String pullToRefreshText2) {
        this.pullToRefreshText = pullToRefreshText2;
        if (this.state == State.PULL_TO_REFRESH) {
            this.text.setText(pullToRefreshText2);
            this.image.setVisibility(0);
            if (this.mLoadingThread != null) {
                this.mLoadingThread.interrupt();
                this.mLoadingThread = null;
            }
            this.isHeaderRefreshing = false;
        }
    }

    @SuppressLint("WrongConstant")
    public void setTextReleaseToRefresh(String releaseToRefreshText2) {
        this.releaseToRefreshText = releaseToRefreshText2;
        if (this.state == State.RELEASE_TO_REFRESH) {
            this.text.setText(releaseToRefreshText2);
            this.image.setVisibility(0);
            if (this.mLoadingThread != null) {
                this.mLoadingThread.interrupt();
                this.mLoadingThread = null;
            }
            this.isHeaderRefreshing = false;
        }
    }

    @SuppressLint("WrongConstant")
    public void setTextRefreshing(String refreshingText2) {
        this.refreshingText = refreshingText2;
        if (this.state == State.REFRESHING) {
            this.text.setText(refreshingText2);
            this.image.setVisibility(4);
            this.mLoadingThread = new LoadingThread(mLoadingHandler);
            this.mLoadingThread.start();
            this.isHeaderRefreshing = true;
        }
    }

    public static float getDimensionDpSize(int id, Context context, AttributeSet attrs) {
        return context.obtainStyledAttributes(attrs, R.styleable.PullToRefreshView).getDimension(id, -1.0f) / (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    private void init(Context context, AttributeSet attrs) {
        setVerticalFadingEdgeEnabled(false);
        this.headerContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.pull_to_refresh_header, (ViewGroup) null);
        this.header = (RelativeLayout) this.headerContainer.findViewById(R.id.ptr_id_header);
        this.text = (TextView) this.header.findViewById(R.id.ptr_id_text);
        this.lastUpdatedTextView = (TextView) this.header.findViewById(R.id.ptr_id_last_updated);
        this.image = (ImageView) this.header.findViewById(R.id.ptr_id_arrow);
        this.spinner = (ProgressBar) this.header.findViewById(R.id.ptr_id_spinner);
        if (attrs == null) {
            this.text.setTextSize(15.0f);
            this.lastUpdatedTextView.setTextSize(12.0f);
            this.image.setPadding(0, 0, 5, 0);
            this.spinner.setPadding(0, 0, 5, 0);
        } else {
            this.text.setTextSize(getDimensionDpSize(3, context, attrs));
            this.lastUpdatedTextView.setTextSize(getDimensionDpSize(4, context, attrs));
            this.image.setPadding(0, 0, (int) getDimensionDpSize(2, context, attrs), 0);
            this.spinner.setPadding(0, 0, (int) getDimensionDpSize(1, context, attrs), 0);
        }
        TextView tv = new TextView(context);
        tv.setText("Loading");
        tv.setTypeface(Typeface.DEFAULT_BOLD);
        tv.setTextSize(getDimensionDpSize(3, context, attrs));
        this.pullToRefreshText = getContext().getString(R.string.ptr_pull_to_refresh);
        this.releaseToRefreshText = getContext().getString(R.string.ptr_release_to_refresh);
        this.refreshingText = getContext().getString(R.string.ptr_loading);
        this.lastUpdatedText = getContext().getString(R.string.ptr_last_updated);
        this.flipAnimation = new RotateAnimation(0.0f, -180.0f, 1, 0.5f, 1, 0.5f);
        this.flipAnimation.setInterpolator(new LinearInterpolator());
        this.flipAnimation.setDuration(250);
        this.flipAnimation.setFillAfter(true);
        this.reverseFlipAnimation = new RotateAnimation(-180.0f, 0.0f, 1, 0.5f, 1, 0.5f);
        this.reverseFlipAnimation.setInterpolator(new LinearInterpolator());
        this.reverseFlipAnimation.setDuration(250);
        this.reverseFlipAnimation.setFillAfter(true);
        addHeaderView(this.headerContainer);
        setState(State.PULL_TO_REFRESH);
        this.scrollbarEnabled = isVerticalScrollBarEnabled();
        this.header.getViewTreeObserver().addOnGlobalLayoutListener(new PTROnGlobalLayoutListener(this, null));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setHeaderPadding(int padding) {
        this.headerPadding = padding;
        MarginLayoutParams mlp = (MarginLayoutParams) this.header.getLayoutParams();
        mlp.setMargins(0, Math.round((float) padding), 0, 0);
        this.header.setLayoutParams(mlp);
    }

    private boolean isPull(MotionEvent event) {
        return this.isPulling;
    }

    @Override // com.stczh.otherlibs.Waterfall.PLA_AbsListView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.isHeaderRefreshing) {
        }
        if (this.lockScrollWhileRefreshing) {
            if (this.state == State.REFRESHING) {
                return true;
            }
            if (getAnimation() != null && !getAnimation().hasEnded()) {
                return true;
            }
        }
        switch (event.getAction()) {
            case 0:
                if (getFirstVisiblePosition() == 0) {
                    this.previousY = event.getY();
                    break;
                }
                break;
            case 1:
            case 3:
                this.isPulling = false;
                break;
            case 2:
                if (getFirstVisiblePosition() != 0 || event.getY() - this.previousY <= 0.0f) {
                    this.isPulling = false;
                    break;
                } else {
                    this.isPulling = true;
                    return true;
                }
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override // com.stczh.otherlibs.Waterfall.PLA_ListView, com.stczh.otherlibs.Waterfall.PLA_AbsListView, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (this.isHeaderRefreshing) {
        }
        if (this.lockScrollWhileRefreshing && (this.state == State.REFRESHING || (getAnimation() != null && !getAnimation().hasEnded()))) {
            return true;
        }
        switch (event.getAction()) {
            case 1:
                if (isPull(event) && (this.state == State.RELEASE_TO_REFRESH || getFirstVisiblePosition() == 0)) {
                    switch (m0x79e71b21()[this.state.ordinal()]) {
                        case 1:
                            resetHeader();
                            break;
                        case 2:
                            setState(State.REFRESHING);
                            bounceBackHeader();
                            break;
                    }
                }
                break;
            case 2:
                if (isPull(event)) {
                    float y = event.getY();
                    float diff = y - this.previousY;
                    if (diff > 0.0f) {
                        diff /= PULL_RESISTANCE;
                    }
                    this.previousY = y;
                    int newHeaderPadding = Math.max(Math.round(((float) this.headerPadding) + diff), -this.header.getHeight());
                    if (!(newHeaderPadding == this.headerPadding || this.state == State.REFRESHING)) {
                        setHeaderPadding(newHeaderPadding);
                        if (this.state != State.PULL_TO_REFRESH || this.headerPadding <= 0) {
                            if (this.state == State.RELEASE_TO_REFRESH && this.headerPadding < 0) {
                                setState(State.PULL_TO_REFRESH);
                                this.image.clearAnimation();
                                this.image.startAnimation(this.reverseFlipAnimation);
                                break;
                            }
                        } else {
                            setState(State.RELEASE_TO_REFRESH);
                            this.image.clearAnimation();
                            this.image.startAnimation(this.flipAnimation);
                            break;
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void bounceBackHeader() {
        int yTranslate;
        if (this.state == State.REFRESHING) {
            yTranslate = this.header.getHeight() - this.headerContainer.getHeight();
        } else {
            yTranslate = (-this.headerContainer.getHeight()) - this.headerContainer.getTop();
        }
        this.bounceAnimation = new TranslateAnimation(0, 0.0f, 0, 0.0f, 0, 0.0f, 0, (float) yTranslate);
        this.bounceAnimation.setDuration(215);
        this.bounceAnimation.setFillEnabled(true);
        this.bounceAnimation.setFillAfter(false);
        this.bounceAnimation.setFillBefore(true);
        this.bounceAnimation.setAnimationListener(new HeaderAnimationListener(yTranslate));
        startAnimation(this.bounceAnimation);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void resetHeader() {
        if (getFirstVisiblePosition() > 0) {
            setHeaderPadding(-this.header.getHeight());
            setState(State.PULL_TO_REFRESH);
        } else if (getAnimation() == null || getAnimation().hasEnded()) {
            bounceBackHeader();
        } else {
            this.bounceBackHeader = true;
        }
    }

    @SuppressLint("WrongConstant")
    private void setUiRefreshing() {
        this.spinner.setVisibility(8);
        this.image.clearAnimation();
        this.image.setVisibility(4);
        this.text.setText(this.refreshingText);
        this.mLoadingThread = new LoadingThread(mLoadingHandler);
        this.mLoadingThread.start();
        this.isHeaderRefreshing = true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    @SuppressLint("WrongConstant")
    private void setState(State state2) {
        this.state = state2;
        switch (m0x79e71b21()[state2.ordinal()]) {
            case 1:
                this.spinner.setVisibility(8);
                this.image.setVisibility(0);
                this.text.setText(this.pullToRefreshText);
                if (this.mLoadingThread != null) {
                    this.mLoadingThread.interrupt();
                    this.mLoadingThread = null;
                }
                this.isHeaderRefreshing = false;
                if (this.showLastUpdatedText && this.lastUpdated != -1) {
                    this.lastUpdatedTextView.setVisibility(0);
                    this.lastUpdatedTextView.setText(String.format(this.lastUpdatedText, this.lastUpdatedDateFormat.format(new Date(this.lastUpdated))));
                    return;
                }
                return;
            case 2:
                this.spinner.setVisibility(8);
                this.image.setVisibility(0);
                this.text.setText(this.releaseToRefreshText);
                if (this.mLoadingThread != null) {
                    this.mLoadingThread.interrupt();
                    this.mLoadingThread = null;
                }
                this.isHeaderRefreshing = false;
                return;
            case 3:
                setUiRefreshing();
                this.lastUpdated = System.currentTimeMillis();
                if (this.onRefreshListener == null) {
                    setState(State.PULL_TO_REFRESH);
                    return;
                } else {
                    this.onRefreshListener.onRefresh();
                    return;
                }
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        Log.i("Vingle", "hasResetHeader : " + this.hasResetHeader + ", t : " + t + ", oldt : " + oldt);
        if (!this.hasResetHeader) {
            if (measuredHeaderHeight > 0 && this.state != State.REFRESHING) {
                setHeaderPadding(-measuredHeaderHeight);
            }
            this.hasResetHeader = true;
        }
    }

    /* access modifiers changed from: private */
    public class HeaderAnimationListener implements Animation.AnimationListener {
        private int height;
        private State stateAtAnimationStart;
        private int translation;

        public HeaderAnimationListener(int translation2) {
            this.translation = translation2;
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationStart(Animation animation) {
            this.stateAtAnimationStart = MultiColumnPullToRefreshListView.this.state;
            ViewGroup.LayoutParams lp = MultiColumnPullToRefreshListView.this.getLayoutParams();
            this.height = lp.height;
            lp.height = MultiColumnPullToRefreshListView.this.getHeight() - this.translation;
            MultiColumnPullToRefreshListView.this.setLayoutParams(lp);
            if (MultiColumnPullToRefreshListView.this.scrollbarEnabled) {
                MultiColumnPullToRefreshListView.this.setVerticalScrollBarEnabled(false);
            }
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationEnd(Animation animation) {
            int top;
            MultiColumnPullToRefreshListView multiColumnPullToRefreshListView = MultiColumnPullToRefreshListView.this;
            if (this.stateAtAnimationStart == State.REFRESHING) {
                top = 0;
            } else {
                top = (-MultiColumnPullToRefreshListView.measuredHeaderHeight) - MultiColumnPullToRefreshListView.this.headerContainer.getTop();
            }
            multiColumnPullToRefreshListView.setHeaderPadding(top);
            ViewGroup.LayoutParams lp = MultiColumnPullToRefreshListView.this.getLayoutParams();
            lp.height = this.height;
            MultiColumnPullToRefreshListView.this.setLayoutParams(lp);
            if (MultiColumnPullToRefreshListView.this.scrollbarEnabled) {
                MultiColumnPullToRefreshListView.this.setVerticalScrollBarEnabled(true);
            }
            if (MultiColumnPullToRefreshListView.this.bounceBackHeader) {
                MultiColumnPullToRefreshListView.this.bounceBackHeader = false;
                MultiColumnPullToRefreshListView.this.postDelayed(new Runnable() { // from class: com.stczh.otherlibs.Waterfall.MultiColumnPullToRefreshListView.HeaderAnimationListener.1
                    @Override // java.lang.Runnable
                    public void run() {
                        MultiColumnPullToRefreshListView.this.resetHeader();
                    }
                }, 20);
            } else if (this.stateAtAnimationStart != State.REFRESHING) {
                MultiColumnPullToRefreshListView.this.setState(State.PULL_TO_REFRESH);
            }
        }

        @Override // android.view.animation.Animation.AnimationListener
        public void onAnimationRepeat(Animation animation) {
        }
    }

    /* access modifiers changed from: private */
    public class PTROnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private PTROnGlobalLayoutListener() {
        }

        /* synthetic */ PTROnGlobalLayoutListener(MultiColumnPullToRefreshListView multiColumnPullToRefreshListView, PTROnGlobalLayoutListener pTROnGlobalLayoutListener) {
            this();
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            int initialHeaderHeight = MultiColumnPullToRefreshListView.this.header.getHeight();
            if (initialHeaderHeight > 0) {
                MultiColumnPullToRefreshListView.measuredHeaderHeight = initialHeaderHeight;
                if (MultiColumnPullToRefreshListView.measuredHeaderHeight > 0 && MultiColumnPullToRefreshListView.this.state != State.REFRESHING) {
                    MultiColumnPullToRefreshListView.this.setHeaderPadding(-MultiColumnPullToRefreshListView.measuredHeaderHeight);
                    MultiColumnPullToRefreshListView.this.requestLayout();
                }
            }
            MultiColumnPullToRefreshListView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }

    /* access modifiers changed from: private */
    public class LoadingThread extends Thread {
        Handler mHandler;

        public LoadingThread(Handler mHandler2) {
            this.mHandler = mHandler2;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (true) {
                try {
                    Message msg_loading_0 = this.mHandler.obtainMessage(100);
                    msg_loading_0.obj = new WeakReference(MultiColumnPullToRefreshListView.this.text);
                    this.mHandler.sendMessage(msg_loading_0);
                    Thread.sleep(400);
                    Message msg_loading_1 = this.mHandler.obtainMessage(101);
                    msg_loading_1.obj = new WeakReference(MultiColumnPullToRefreshListView.this.text);
                    this.mHandler.sendMessage(msg_loading_1);
                    Thread.sleep(400);
                    Message msg_loading_2 = this.mHandler.obtainMessage(MultiColumnPullToRefreshListView.LOADINGTWO);
                    msg_loading_2.obj = new WeakReference(MultiColumnPullToRefreshListView.this.text);
                    this.mHandler.sendMessage(msg_loading_2);
                    Thread.sleep(400);
                    Message msg_loading_3 = this.mHandler.obtainMessage(MultiColumnPullToRefreshListView.LOADINGTHREE);
                    msg_loading_3.obj = new WeakReference(MultiColumnPullToRefreshListView.this.text);
                    this.mHandler.sendMessage(msg_loading_3);
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
