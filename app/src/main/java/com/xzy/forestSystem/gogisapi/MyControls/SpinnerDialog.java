package  com.xzy.forestSystem.gogisapi.MyControls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;

public class SpinnerDialog extends Spinner {
    private ICallback _Callback = null;

    public SpinnerDialog(Context paramContext) {
        super(paramContext);
    }

    public SpinnerDialog(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public SpinnerDialog(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public void SetCallback(ICallback tmpICallback) {
        this._Callback = tmpICallback;
    }

    @Override // android.widget.Spinner, android.view.View
    public boolean performClick() {
        if (this._Callback == null) {
            return false;
        }
        this._Callback.OnClick("SpinnerCallback", null);
        return false;
    }
}
