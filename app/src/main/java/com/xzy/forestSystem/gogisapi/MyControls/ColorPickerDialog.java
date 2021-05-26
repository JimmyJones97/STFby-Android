package  com.xzy.forestSystem.gogisapi.MyControls;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;

public class ColorPickerDialog {
    private int _Alpha;

    /* renamed from: _B */
    private int f503_B;
    private XDialogTemplate _Dialog;

    /* renamed from: _G */
    private int f504_G;

    /* renamed from: _R */
    private int f505_R;
    private EditText et_B;
    private EditText et_G;
    private EditText et_R;
    private EditText et_result;
    private int mInitialColor;
    private OnColorChangedListener mListener;
    private ICallback m_Callback;
    private ColorPickerView myColorView;
    private SeekBar myTransSeekBar;
    private ICallback pCallback;
    private String title;

    public interface OnColorChangedListener {
        void colorChanged(int i);
    }

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshResult() {
        this.et_R.setText(String.valueOf(this.f505_R));
        this.et_G.setText(String.valueOf(this.f504_G));
        this.et_B.setText(String.valueOf(this.f503_B));
        this.et_result.setText(ColorToHex(this._Alpha, this.f505_R, this.f504_G, this.f503_B));
    }

    public ColorPickerDialog() {
        this.title = "颜色管理器";
        this.mInitialColor = 0;
        this._Dialog = null;
        this.m_Callback = null;
        this.f505_R = 0;
        this.f504_G = 0;
        this.f503_B = 0;
        this.myColorView = null;
        this.myTransSeekBar = null;
        this._Alpha = 255;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.ColorPickerDialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("确定")) {
                    String str2 = Common.GetTextValueOnID(ColorPickerDialog.this._Dialog, (int) R.id.et_result);
                    boolean tmpBool = false;
                    try {
                        Color.parseColor("#" + str2);
                        tmpBool = true;
                    } catch (Exception e) {
                        Common.ShowToast(e.getLocalizedMessage());
                    }
                    if (tmpBool) {
                        if (ColorPickerDialog.this.m_Callback != null) {
                            ColorPickerDialog.this.m_Callback.OnClick("颜色", "#" + str2);
                        }
                        ColorPickerDialog.this._Dialog.dismiss();
                        return;
                    }
                    Common.ShowDialog("颜色值不正确.请重新选择颜色!");
                } else if (command.equals("选择颜色返回")) {
                    int color = Integer.parseInt(String.valueOf(object));
                    ColorPickerDialog.this.mInitialColor = color;
                    ColorPickerDialog.this.f505_R = Color.red(color);
                    ColorPickerDialog.this.f504_G = Color.green(color);
                    ColorPickerDialog.this.f503_B = Color.blue(color);
                    ColorPickerDialog.this.refreshResult();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.colorpicker_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("颜色管理器");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.et_R = (EditText) this._Dialog.findViewById(R.id.et_r);
        this.et_G = (EditText) this._Dialog.findViewById(R.id.et_g);
        this.et_B = (EditText) this._Dialog.findViewById(R.id.et_b);
        this.et_result = (EditText) this._Dialog.findViewById(R.id.et_result);
        this.myTransSeekBar = (SeekBar) this._Dialog.findViewById(R.id.sb_transp);
        this.myTransSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.ColorPickerDialog.2
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                ColorPickerDialog.this._Alpha = arg1;
                ColorPickerDialog.this.myColorView.SetColorAlpha(ColorPickerDialog.this._Alpha);
                ColorPickerDialog.this.refreshResult();
                if (ColorPickerDialog.this.myColorView != null) {
                    ColorPickerDialog.this.myColorView.invalidate();
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar arg0) {
            }
        });
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.ColorPickerDialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                LinearLayout tmpLinearLayout = (LinearLayout) ColorPickerDialog.this._Dialog.findViewById(R.id.ll_colorpickerView);
                tmpLinearLayout.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
                int width = tmpLinearLayout.getMeasuredWidth();
                int height = tmpLinearLayout.getMeasuredHeight();
                if (width == 0 && height == 0) {
                    width = tmpLinearLayout.getRight() - tmpLinearLayout.getLeft();
                    height = tmpLinearLayout.getBottom() - tmpLinearLayout.getTop();
                    if (width > height) {
                        width = height;
                    } else {
                        height = width;
                    }
                }
                if (width == 0 && height == 0) {
                    WindowManager manager = ColorPickerDialog.this._Dialog.getWindow().getWindowManager();
                    width = manager.getDefaultDisplay().getWidth() / 2;
                    height = manager.getDefaultDisplay().getHeight() / 2;
                    if (width > height) {
                        width = height;
                    } else {
                        height = width;
                    }
                }
                ColorPickerDialog.this.myColorView = new ColorPickerView(PubVar.MainContext, height, width);
                tmpLinearLayout.addView(ColorPickerDialog.this.myColorView);
                ColorPickerDialog.this.myColorView.SetCallback(ColorPickerDialog.this.pCallback);
                ColorPickerDialog.this.myColorView.SetColor(ColorPickerDialog.this.mInitialColor);
                ColorPickerDialog.this._Dialog.SetCaption(ColorPickerDialog.this.title);
                ColorPickerDialog.this.refreshResult();
                ColorPickerDialog.this.myTransSeekBar.setProgress(ColorPickerDialog.this._Alpha);
            }
        });
        this._Dialog.show();
    }

    public static String ColorToHex(int color) {
        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        String str1 = Integer.toHexString(a);
        if (str1.length() == 1) {
            str1 = "0" + str1;
        }
        String str2 = Integer.toHexString(r);
        if (str2.length() == 1) {
            str2 = "0" + str2;
        }
        String str3 = Integer.toHexString(g);
        if (str3.length() == 1) {
            str3 = "0" + str3;
        }
        String str4 = Integer.toHexString(b);
        if (str4.length() == 1) {
            str4 = "0" + str4;
        }
        return "#" + (String.valueOf(str1) + str2 + str3 + str4).toUpperCase();
    }

    public static String ColorToHex(int a, int r, int g, int b) {
        String str1 = Integer.toHexString(a);
        if (str1.length() == 1) {
            str1 = "0" + str1;
        }
        String str2 = Integer.toHexString(r);
        if (str2.length() == 1) {
            str2 = "0" + str2;
        }
        String str3 = Integer.toHexString(g);
        if (str3.length() == 1) {
            str3 = "0" + str3;
        }
        String str4 = Integer.toHexString(b);
        if (str4.length() == 1) {
            str4 = "0" + str4;
        }
        return (String.valueOf(str1) + str2 + str3 + str4).toUpperCase();
    }

    /* access modifiers changed from: private */
    public class ColorPickerView extends View {
        private int _Alpha = 255;
        private float centerRadius;
        private int currentColor = 0;
        private boolean downInCircle = true;
        private boolean downInRect;
        private boolean highlightCenter;
        private boolean highlightCenterLittle;
        private Paint mCenterPaint;
        private Paint mCenterPaint2;
        private final int[] mCircleColors;
        private int mHeight;
        private Paint mLinePaint;
        private Paint mPaint;
        private final int[] mRectColors;
        private Paint mRectPaint;
        private int mWidth;
        private ICallback m_Callback = null;

        /* renamed from: r */
        private float f506r;
        private float rectBottom;
        private float rectLeft;
        private float rectRight;
        private Shader rectShader;
        private float rectTop;

        public void SetCallback(ICallback pCallback) {
            this.m_Callback = pCallback;
        }

        public void SetColor(int color) {
            this.currentColor = color;
            if (this.mCenterPaint != null) {
                this.mCenterPaint.setColor(this.currentColor);
            }
        }

        public ColorPickerView(Context context, int height, int width) {
            super(context);
            this.mHeight = height - 36;
            this.mWidth = width;
            setMinimumHeight(height - 36);
            setMinimumWidth(width);
            this.mCircleColors = new int[]{ViewCompat.MEASURED_STATE_MASK, SupportMenu.CATEGORY_MASK, -65281, -16776961, -16711681, -16711936, -256, -1, ViewCompat.MEASURED_STATE_MASK};
            Shader s = new SweepGradient(0.0f, 0.0f, this.mCircleColors, (float[]) null);
            this.mPaint = new Paint(1);
            this.mPaint.setShader(s);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(100.0f);
            this.f506r = (((float) (width / 2)) * 0.7f) - (this.mPaint.getStrokeWidth() * 0.5f);
            this.mCenterPaint = new Paint(1);
            this.mCenterPaint.setColor(ColorPickerDialog.this.mInitialColor);
            this.mCenterPaint.setStrokeWidth(5.0f);
            this.mCenterPaint.setAlpha(this._Alpha);
            this.centerRadius = (this.f506r - (this.mPaint.getStrokeWidth() / 2.0f)) * 0.7f;
            this.mCenterPaint2 = new Paint(1);
            this.mCenterPaint2.setColor(ViewCompat.MEASURED_STATE_MASK);
            this.mCenterPaint2.setStrokeWidth(5.0f);
            this.mLinePaint = new Paint(1);
            this.mLinePaint.setColor(Color.parseColor("#72A1D1"));
            this.mLinePaint.setStrokeWidth(4.0f);
            this.mRectColors = new int[]{-16777216, this.mCenterPaint.getColor(), -1};
            this.mRectPaint = new Paint(1);
            this.mRectPaint.setStrokeWidth(5.0f);
            this.rectLeft = (-this.f506r) - (this.mPaint.getStrokeWidth() * 0.5f);
            this.rectTop = this.f506r + (this.mPaint.getStrokeWidth() * 0.5f) + (this.mLinePaint.getStrokeMiter() * 0.5f) + 15.0f;
            this.rectRight = this.f506r + (this.mPaint.getStrokeWidth() * 0.5f);
            this.rectBottom = this.rectTop + 100.0f;
        }

        /* access modifiers changed from: protected */
        @Override // android.view.View
        public void onDraw(Canvas canvas) {
            new Paint().setTextSize((float) 50);
            canvas.translate((float) (this.mWidth / 2), (float) ((this.mHeight / 2) - 50));
            canvas.drawLine(-this.centerRadius, 0.0f, this.centerRadius, 0.0f, this.mCenterPaint2);
            canvas.drawLine(0.0f, -this.centerRadius, 0.0f, this.centerRadius, this.mCenterPaint2);
            canvas.drawCircle(0.0f, 0.0f, this.centerRadius, this.mCenterPaint);
            if (this.highlightCenter || this.highlightCenterLittle) {
                int c = this.mCenterPaint.getColor();
                this.mCenterPaint.setStyle(Paint.Style.STROKE);
                if (this.highlightCenter) {
                    this.mCenterPaint.setAlpha(255);
                } else if (this.highlightCenterLittle) {
                    this.mCenterPaint.setAlpha(144);
                }
                canvas.drawCircle(0.0f, 0.0f, this.centerRadius + this.mCenterPaint.getStrokeWidth(), this.mCenterPaint);
                this.mCenterPaint.setStyle(Paint.Style.FILL);
                this.mCenterPaint.setColor(c);
                this.mCenterPaint.setAlpha(this._Alpha);
            }
            canvas.drawOval(new RectF(-this.f506r, -this.f506r, this.f506r, this.f506r), this.mPaint);
            if (this.downInCircle) {
                this.mRectColors[1] = this.mCenterPaint.getColor();
            }
            this.rectShader = new LinearGradient(this.rectLeft, 0.0f, this.rectRight, 0.0f, this.mRectColors, (float[]) null, Shader.TileMode.MIRROR);
            this.mRectPaint.setShader(this.rectShader);
            canvas.drawRect(this.rectLeft, this.rectTop, this.rectRight, this.rectBottom, this.mRectPaint);
            float offset = this.mLinePaint.getStrokeWidth() / 2.0f;
            canvas.drawLine(this.rectLeft - offset, this.rectTop - (2.0f * offset), this.rectLeft - offset, (2.0f * offset) + this.rectBottom, this.mLinePaint);
            canvas.drawLine(this.rectLeft - (2.0f * offset), this.rectTop - offset, (2.0f * offset) + this.rectRight, this.rectTop - offset, this.mLinePaint);
            canvas.drawLine(this.rectRight + offset, this.rectTop - (2.0f * offset), this.rectRight + offset, (2.0f * offset) + this.rectBottom, this.mLinePaint);
            canvas.drawLine(this.rectLeft - (2.0f * offset), this.rectBottom + offset, (2.0f * offset) + this.rectRight, this.rectBottom + offset, this.mLinePaint);
            super.onDraw(canvas);
        }

        /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
        /* JADX WARNING: Removed duplicated region for block: B:20:0x0094  */
        @Override // android.view.View
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean onTouchEvent(android.view.MotionEvent r15) {
            /*
            // Method dump skipped, instructions count: 280
            */
            throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.gogisapi.MyControls.ColorPickerDialog.ColorPickerView.onTouchEvent(android.view.MotionEvent):boolean");
        }

        /* access modifiers changed from: protected */
        @Override // android.view.View
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(this.mWidth, this.mHeight);
        }

        private boolean inColorCircle(float x, float y, float outRadius, float inRadius) {
            double inCircle = 3.141592653589793d * ((double) inRadius) * ((double) inRadius);
            double fingerCircle = 3.141592653589793d * ((double) ((x * x) + (y * y)));
            if (fingerCircle >= 3.141592653589793d * ((double) outRadius) * ((double) outRadius) || fingerCircle <= inCircle) {
                return false;
            }
            return true;
        }

        private boolean inCenter(float x, float y, float centerRadius2) {
            if (3.141592653589793d * ((double) ((x * x) + (y * y))) < ((double) centerRadius2) * 3.141592653589793d * ((double) centerRadius2)) {
                return true;
            }
            return false;
        }

        private boolean inRect(float x, float y) {
            if (x > this.rectRight || x < this.rectLeft || y > this.rectBottom || y < this.rectTop) {
                return false;
            }
            return true;
        }

        private int interpCircleColor(int[] colors, float unit) {
            if (unit <= 0.0f) {
                return colors[0];
            }
            if (unit >= 1.0f) {
                return colors[colors.length - 1];
            }
            float p = unit * ((float) (colors.length - 1));
            int i = (int) p;
            float p2 = p - ((float) i);
            int c0 = colors[i];
            int c1 = colors[i + 1];
            return Color.argb(ave(Color.alpha(c0), Color.alpha(c1), p2), ave(Color.red(c0), Color.red(c1), p2), ave(Color.green(c0), Color.green(c1), p2), ave(Color.blue(c0), Color.blue(c1), p2));
        }

        private int interpRectColor(int[] colors, float x) {
            int c0;
            int c1;
            float p;
            if (x < 0.0f) {
                c0 = colors[0];
                c1 = colors[1];
                p = (this.rectRight + x) / this.rectRight;
            } else {
                c0 = colors[1];
                c1 = colors[2];
                p = x / this.rectRight;
            }
            return Color.argb(ave(Color.alpha(c0), Color.alpha(c1), p), ave(Color.red(c0), Color.red(c1), p), ave(Color.green(c0), Color.green(c1), p), ave(Color.blue(c0), Color.blue(c1), p));
        }

        private int ave(int s, int d, float p) {
            return Math.round(((float) (d - s)) * p) + s;
        }

        public void SetColorAlpha(int alpha) {
            this._Alpha = alpha;
            this.mCenterPaint.setAlpha(alpha);
        }
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public int getInitialColor() {
        return this.mInitialColor;
    }

    public void setInitialColor(int color) {
        if (color != 0) {
            this.mInitialColor = color;
            this.f505_R = Color.red(color);
            this.f504_G = Color.green(color);
            this.f503_B = Color.blue(color);
            this._Alpha = Color.alpha(color);
        }
    }
}
