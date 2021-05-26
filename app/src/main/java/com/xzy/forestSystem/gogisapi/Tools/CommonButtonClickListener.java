package  com.xzy.forestSystem.gogisapi.Tools;

import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import java.util.Arrays;

/* compiled from: CalculateDialog */
class CommonButtonClickListener implements View.OnClickListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$xzy$gogisapi$Tools$ButtonType;
    private String appendStr;
    private ButtonType buttonType;
    private EditText expressText;
    private EditText resultText;

    static /* synthetic */ int[] $SWITCH_TABLE$com$xzy$gogisapi$Tools$ButtonType() {
        int[] iArr = $SWITCH_TABLE$com$xzy$gogisapi$Tools$ButtonType;
        if (iArr == null) {
            iArr = new int[ButtonType.values().length];
            try {
                iArr[ButtonType.BACKSPACE.ordinal()] = 2;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ButtonType.CLEAR.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ButtonType.EQUALS.ordinal()] = 4;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ButtonType.SYNTHETIC_COMMON_APPEND.ordinal()] = 1;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$xzy$gogisapi$Tools$ButtonType = iArr;
        }
        return iArr;
    }

    public CommonButtonClickListener(EditText expressText2, EditText resultTxt, ButtonType buttonType2, String... appendText) throws IllegalArgumentException {
        if (buttonType2 == ButtonType.SYNTHETIC_COMMON_APPEND) {
            if (appendText == null || appendText.length != 1) {
                throw new IllegalArgumentException("ButtonType:" + buttonType2 + ", appendText" + (appendText == null ? null : Arrays.toString(appendText)));
            }
            this.appendStr = appendText[0];
        }
        this.resultText = resultTxt;
        this.expressText = expressText2;
        this.buttonType = buttonType2;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        try {
            Editable text = this.expressText.getEditableText();
            switch ($SWITCH_TABLE$com$xzy$gogisapi$Tools$ButtonType()[this.buttonType.ordinal()]) {
                case 1:
                    text.append((CharSequence) this.appendStr);
                    return;
                case 2:
                    int length = text.length();
                    if (length <= 0) {
                        return;
                    }
                    if (text.toString().startsWith(Calculation.ERROR_PREFIX)) {
                        text.clear();
                        return;
                    } else {
                        text.delete(length - 1, length);
                        return;
                    }
                case 3:
                    text.clear();
                    return;
                case 4:
                    String exp = text.toString();
                    if (exp != null) {
                        String exp2 = exp.trim();
                        if (exp2.length() != 0) {
                            String result = Calculation.cal(ExpressionParse.parse(exp2));
                            if (!result.startsWith(Calculation.ERROR_PREFIX)) {
                                this.resultText.setText(result);
                            } else {
                                this.resultText.setText("计算错误.");
                            }
                            this.resultText.setSelection(result.length());
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
        }
    }
}
