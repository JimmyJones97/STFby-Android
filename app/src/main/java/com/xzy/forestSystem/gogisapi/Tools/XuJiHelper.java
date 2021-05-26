package  com.xzy.forestSystem.gogisapi.Tools;

import android.content.Context;
import android.widget.EditText;
import com.xzy.forestSystem.gogisapi.Common.Common;

/* compiled from: CalXuJiDialog */
class XuJiHelper {
    private Context _Context;

    public XuJiHelper(Context context) {
        this._Context = context;
    }

    public double getFieldValue(EditText editText, String itemName) {
        try {
            return Double.parseDouble(editText.getText().toString());
        } catch (Exception e) {
            Common.ShowToast("请正确填写" + itemName);
            return 0.0d;
        }
    }

    public void showToast(EditText editText, String text) {
        editText.requestFocus();
        Common.ShowToast(text);
    }

    public void closeSoftKeyBoard() {
    }
}
