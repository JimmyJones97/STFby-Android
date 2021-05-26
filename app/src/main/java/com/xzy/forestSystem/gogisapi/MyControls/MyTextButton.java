package  com.xzy.forestSystem.gogisapi.MyControls;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.Button;
import com.stczh.gzforestSystem.R;

@SuppressLint("AppCompatCustomView")
public class MyTextButton extends Button {
    private String _AttachText = "";
    private float _DrawX01 = 0.0f;
    private float _DrawX02 = 0.0f;
    private float _DrawY = 0.0f;
    private float _Height = 90.0f;
    private String _OrigText = "";
    Paint _TextPaint = null;
    Paint _TextPaint2 = null;
    private float _TextSize = 36.0f;
    private float _Width = 200.0f;

    public MyTextButton(Context context) {
        super(context, null);
    }

    @SuppressLint("ResourceType")
    public MyTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        this._OrigText = getText().toString();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyTextButton);
        int textColor = a.getColor(0, ViewCompat.MEASURED_STATE_MASK);
        @SuppressLint("ResourceType") int textColor2 = a.getColor(1, -16776961);
        @SuppressLint("ResourceType") float textSize = a.getDimension(2, 36.0f);
        this._OrigText = a.getString(3);
        this._Height = a.getDimension(5, 90.0f);
        this._Width = a.getDimension(6, 200.0f);
        SetAttachText(a.getString(4));
        this._TextSize = textSize;
        this._TextPaint = new Paint();
        this._TextPaint.setAntiAlias(true);
        this._TextPaint.setTextSize(textSize);
        this._TextPaint.setColor(textColor);
        Typeface localTypeface = Typeface.create("宋体", 0);
        this._TextPaint.setTypeface(localTypeface);
        this._TextPaint2 = new Paint();
        this._TextPaint2.setAntiAlias(true);
        this._TextPaint2.setTextSize(textSize);
        this._TextPaint2.setColor(textColor2);
        this._TextPaint2.setTypeface(localTypeface);
    }

    public void SetAttachText(String text) {
        this._AttachText = text;
        this._DrawY = (this._Height + this._TextSize) / 2.0f;
        this._DrawX01 = (this._Width - (this._TextSize * ((float) (this._OrigText.length() + this._AttachText.length())))) / 2.0f;
        this._DrawX02 = this._DrawX01 + (this._TextSize * ((float) this._OrigText.length()));
    }

    /* access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this._OrigText.equals("")) {
            canvas.drawText(this._OrigText, this._DrawX01, this._DrawY, this._TextPaint);
        }
        if (!this._AttachText.equals("")) {
            canvas.drawText(this._AttachText, this._DrawX02, this._DrawY, this._TextPaint2);
        }
    }
}
