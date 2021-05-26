package  com.xzy.forestSystem.gogisapi.MyControls;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Display.SymbolObject;
import java.util.ArrayList;
import java.util.List;

public class ImageSpinnerDialog extends Spinner {
    private ImageSpinnerDialogAdpter m_Adpter = null;
    private ICallback m_Callback = null;
    private List<SymbolObject> m_ItemList = new ArrayList();

    public ImageSpinnerDialog(Context paramContext) {
        super(paramContext);
        Inti(paramContext);
    }

    public ImageSpinnerDialog(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        Inti(paramContext);
    }

    public ImageSpinnerDialog(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        Inti(paramContext);
    }

    private void Inti(Context paramContext) {
        this.m_Adpter = new ImageSpinnerDialogAdpter(paramContext);
        ArrayList localArrayList = new ArrayList();
        localArrayList.add(null);
        this.m_Adpter.SetDataList(localArrayList);
        setAdapter((SpinnerAdapter) this.m_Adpter);
    }

    public SymbolObject GetSelectSymbolObject() {
        if (this.m_ItemList.size() == 0) {
            return null;
        }
        return this.m_ItemList.get(0);
    }

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public void SetIsShowText(boolean ShowText) {
        if (this.m_Adpter != null) {
            this.m_Adpter.SetIsShowText(ShowText);
        }
    }

    public void SetImageItemList(List<SymbolObject> paramList) {
        this.m_ItemList = paramList;
        this.m_Adpter.SetDataList(paramList);
        setAdapter((SpinnerAdapter) this.m_Adpter);
    }

    @Override // android.widget.Spinner, android.view.View
    public boolean performClick() {
        if (this.m_Callback == null) {
            return false;
        }
        this.m_Callback.OnClick("ImageSpinnerCallback", null);
        return false;
    }
}
