package  com.xzy.forestSystem.gogisapi.MyControls;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class PreviewFrameLayout extends ViewGroup {
    private static final int MIN_HORIZONTAL_MARGIN = 10;
    private double mAspectRatio = 1.3333333333333333d;
    private FrameLayout mFrame;
    private final DisplayMetrics mMetrics = new DisplayMetrics();
    private OnSizeChangedListener mSizeListener;

    public interface OnSizeChangedListener {
        void onSizeChanged();
    }

    public PreviewFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(this.mMetrics);
    }

    public void setOnSizeChangedListener(OnSizeChangedListener listener) {
        this.mSizeListener = listener;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onFinishInflate() {
    }

    public void setAspectRatio(double ratio) {
        if (ratio <= 0.0d) {
            throw new IllegalArgumentException();
        } else if (this.mAspectRatio != ratio) {
            this.mAspectRatio = ratio;
            requestLayout();
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int frameWidth = getWidth();
        int frameHeight = getHeight();
        FrameLayout f = this.mFrame;
        int horizontalPadding = f.getPaddingLeft() + f.getPaddingRight();
        int verticalPadding = f.getPaddingBottom() + f.getPaddingTop();
        int previewHeight = frameHeight - verticalPadding;
        int previewWidth = frameWidth - horizontalPadding;
        if (((double) previewWidth) > ((double) previewHeight) * this.mAspectRatio) {
            previewWidth = (int) ((((double) previewHeight) * this.mAspectRatio) + 0.5d);
        } else {
            previewHeight = (int) ((((double) previewWidth) / this.mAspectRatio) + 0.5d);
        }
        int frameWidth2 = previewWidth + horizontalPadding;
        int frameHeight2 = previewHeight + verticalPadding;
        int hSpace = ((r - l) - frameWidth2) / 2;
        int vSpace = ((b - t) - frameHeight2) / 2;
        this.mFrame.measure(View.MeasureSpec.makeMeasureSpec(frameWidth2, 1073741824), View.MeasureSpec.makeMeasureSpec(frameHeight2, 1073741824));
        this.mFrame.layout(l + hSpace, t + vSpace, r - hSpace, b - vSpace);
        if (this.mSizeListener != null) {
            this.mSizeListener.onSizeChanged();
        }
    }
}
