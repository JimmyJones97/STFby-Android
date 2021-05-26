package  com.xzy.forestSystem.gogisapi.QRCode;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.google.zxing.ResultPoint;
import com.stczh.gzforestSystem.R;
import com.xzy.forestSystem.otherlibs.Waterfall.TransportMediator;

import java.util.Collection;
import java.util.HashSet;

public final class ViewfinderView extends View {
    private static final long ANIMATION_DELAY = 5;
    private static final int CORNER_WIDTH = 50;
    private static final int MIDDLE_LINE_PADDING = 5;
    private static final int MIDDLE_LINE_WIDTH = 6;
    private static final int OPAQUE = 255;
    private static final int[] SCANNER_ALPHA;
    private static final int SPEEN_DISTANCE = 10;
    private int ScreenRate = 10;
    private final int frameColor;
    boolean isFirst;
    private final int laserColor;
    private Collection<ResultPoint> lastPossibleResultPoints;
    private final int maskColor;
    private final Paint paint = new Paint();
    private Collection<ResultPoint> possibleResultPoints;
    private Bitmap resultBitmap;
    private final int resultColor;
    private final int resultPointColor;
    private int scannerAlpha;
    private int slideBottom;
    private int slideTop;

    static {
        int[] iArr = new int[8];
        iArr[1] = 64;
        iArr[2] = 128;
        iArr[3] = 192;
        iArr[4] = 255;
        iArr[5] = 192;
        iArr[6] = 128;
        iArr[7] = 64;
        SCANNER_ALPHA = iArr;
    }

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources resources = getResources();
        this.maskColor = resources.getColor(R.color.viewfinder_mask);
        this.resultColor = resources.getColor(R.color.result_view);
        this.frameColor = resources.getColor(R.color.viewfinder_frame);
        this.laserColor = resources.getColor(R.color.viewfinder_laser);
        this.resultPointColor = resources.getColor(R.color.possible_result_points);
        this.scannerAlpha = 0;
        this.possibleResultPoints = new HashSet(5);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        Rect frame = CameraManager.get().getFramingRect();
        if (frame != null) {
            if (!this.isFirst) {
                this.isFirst = true;
                this.slideTop = frame.top;
                this.slideBottom = frame.bottom;
            }
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            this.paint.setColor(this.resultBitmap != null ? this.resultColor : this.maskColor);
            canvas.drawRect(0.0f, 0.0f, (float) width, (float) frame.top, this.paint);
            canvas.drawRect(0.0f, (float) frame.top, (float) frame.left, (float) (frame.bottom + 1), this.paint);
            canvas.drawRect((float) (frame.right + 1), (float) frame.top, (float) width, (float) (frame.bottom + 1), this.paint);
            canvas.drawRect(0.0f, (float) (frame.bottom + 1), (float) width, (float) height, this.paint);
            if (this.resultBitmap != null) {
                this.paint.setAlpha(255);
                canvas.drawBitmap(this.resultBitmap, (float) frame.left, (float) frame.top, this.paint);
                return;
            }
            this.paint.setColor(-16711936);
            canvas.drawRect((float) frame.left, (float) frame.top, (float) (frame.left + this.ScreenRate), (float) (frame.top + 50), this.paint);
            canvas.drawRect((float) frame.left, (float) frame.top, (float) (frame.left + 50), (float) (frame.top + this.ScreenRate), this.paint);
            canvas.drawRect((float) (frame.right - this.ScreenRate), (float) frame.top, (float) frame.right, (float) (frame.top + 50), this.paint);
            canvas.drawRect((float) (frame.right - 50), (float) frame.top, (float) frame.right, (float) (frame.top + this.ScreenRate), this.paint);
            canvas.drawRect((float) frame.left, (float) (frame.bottom - 50), (float) (frame.left + this.ScreenRate), (float) frame.bottom, this.paint);
            canvas.drawRect((float) frame.left, (float) (frame.bottom - this.ScreenRate), (float) (frame.left + 50), (float) frame.bottom, this.paint);
            canvas.drawRect((float) (frame.right - this.ScreenRate), (float) (frame.bottom - 50), (float) frame.right, (float) frame.bottom, this.paint);
            canvas.drawRect((float) (frame.right - 50), (float) (frame.bottom - this.ScreenRate), (float) frame.right, (float) frame.bottom, this.paint);
            this.slideTop += 10;
            if (this.slideTop >= frame.bottom) {
                this.slideTop = frame.top;
            }
            canvas.drawRect((float) (frame.left + 5), (float) (this.slideTop - 3), (float) (frame.right - 5), (float) (this.slideTop + 3), this.paint);
            Collection<ResultPoint> currentPossible = this.possibleResultPoints;
            Collection<ResultPoint> currentLast = this.lastPossibleResultPoints;
            if (currentPossible.isEmpty()) {
                this.lastPossibleResultPoints = null;
            } else {
                this.possibleResultPoints = new HashSet(5);
                this.lastPossibleResultPoints = currentPossible;
                this.paint.setAlpha(255);
                this.paint.setColor(this.resultPointColor);
                for (ResultPoint point : currentPossible) {
                    canvas.drawCircle(((float) frame.left) + point.getX(), ((float) frame.top) + point.getY(), 6.0f, this.paint);
                }
            }
            if (currentLast != null) {
                this.paint.setAlpha(TransportMediator.KEYCODE_MEDIA_PAUSE);
                this.paint.setColor(this.resultPointColor);
                for (ResultPoint point2 : currentLast) {
                    canvas.drawCircle(((float) frame.left) + point2.getX(), ((float) frame.top) + point2.getY(), 3.0f, this.paint);
                }
            }
            postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top, frame.right, frame.bottom);
        }
    }

    public void drawViewfinder() {
        this.resultBitmap = null;
        invalidate();
    }

    public void drawResultBitmap(Bitmap barcode) {
        this.resultBitmap = barcode;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        this.possibleResultPoints.add(point);
    }
}
