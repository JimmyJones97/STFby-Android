package  com.xzy.forestSystem.gogisapi.MyControls;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class ExpressEditText extends EditText {
    private final KeyListener defaultKeyListener = getKeyListener();
    private boolean isEditable = false;

    public ExpressEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setKeyListener(null);
        addTextChangedListener(new TextWatcher() { // from class: com.xzy.forestSystem.gogisapi.MyControls.ExpressEditText.1
            private String appendStr;
            private String currentDisplay = "";
            private String replacedStr;
            private String strBeforeAppendIndex;

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable s) {
                if (this.strBeforeAppendIndex != null && this.strBeforeAppendIndex.matches("^.*[-%+×÷/*]\\s*$") && this.appendStr != null && this.appendStr.matches("\\s*[-%+×÷/*,)].*$") && this.replacedStr == null) {
                    int start = this.strBeforeAppendIndex.length();
                    s.delete(start, start + this.appendStr.length());
                }
                if (this.strBeforeAppendIndex != null && this.strBeforeAppendIndex.matches("^.*[.]\\s*$") && this.appendStr != null && this.appendStr.matches("\\s*[^0-9].*$") && this.replacedStr == null) {
                    int start2 = this.strBeforeAppendIndex.length();
                    s.delete(start2, start2 + this.appendStr.length());
                }
                if (this.strBeforeAppendIndex != null && this.strBeforeAppendIndex.matches("^\\s*$") && this.appendStr != null && this.appendStr.matches("^\\s*[%+×÷*/,)].*")) {
                    int start3 = this.strBeforeAppendIndex.length();
                    s.delete(start3, start3 + this.appendStr.length());
                }
                this.currentDisplay = s.toString();
            }

            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sStr = s.toString();
                if (count > 0) {
                    try {
                        this.strBeforeAppendIndex = sStr.substring(0, start);
                        this.appendStr = sStr.substring(start, start + count);
                        if (before > 0) {
                            this.replacedStr = this.currentDisplay.substring(start, start + before);
                        } else {
                            this.replacedStr = null;
                        }
                    } catch (Exception e) {
                        this.strBeforeAppendIndex = null;
                        this.appendStr = null;
                        this.replacedStr = null;
                    }
                } else {
                    this.strBeforeAppendIndex = null;
                    this.appendStr = null;
                    this.replacedStr = null;
                }
            }
        });
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent event) {
        if (this.isEditable) {
            return super.onTouchEvent(event);
        }
        return true;
    }

    public void setEditable(boolean editable) {
        this.isEditable = editable;
        if (editable) {
            setKeyListener(this.defaultKeyListener);
            setCursorVisible(true);
            return;
        }
        setKeyListener(null);
        setCursorVisible(false);
    }
}
