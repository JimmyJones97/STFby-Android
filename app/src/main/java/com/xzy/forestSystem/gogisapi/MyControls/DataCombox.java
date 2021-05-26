package  com.xzy.forestSystem.gogisapi.MyControls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import com.stczh.gzforestSystem.R;

public class DataCombox extends LinearLayout {

    /* renamed from: C */
    private Context f507C;
    private ListAdapter _Adapter = null;
    private boolean _MultiSelect = false;
    private boolean[] _SelectItemBoolean = null;
    private String _Title = "选项";
    private ImageButton ibt;

    /* renamed from: tv */
    private EditText f508tv;

    public DataCombox(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        this.f507C = paramContext;
        setOrientation(0);
        this.f508tv = new EditText(paramContext);
        LinearLayout.LayoutParams localLayoutParams1 = new LinearLayout.LayoutParams(-2, -2);
        localLayoutParams1.weight = 2000.0f;
        addView(this.f508tv, localLayoutParams1);
        this.ibt = new ImageButton(paramContext);
        LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(-2, -2);
        localLayoutParams2.leftMargin = -6;
        localLayoutParams2.weight = 1.0f;
        addView(this.ibt, localLayoutParams2);
        this.ibt.setImageDrawable(getResources().getDrawable(R.drawable.bdspeech_btn_reddeep_pressed));
        this.ibt.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.DataCombox.1
            @Override // android.view.View.OnClickListener
            public void onClick(View paramView) {
                DataCombox.this.ShowOptionMessageBox();
            }
        });
    }

    private void SetEditTextFocus() {
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void ShowOptionMessageBox() {
    }

    public void SetMultiSelectMode(boolean paramBoolean) {
        this._MultiSelect = paramBoolean;
    }

    public void SetNumberDecimalMode() {
        this.f508tv.setInputType(8192);
        this.f508tv.setSingleLine();
    }

    public String getText() {
        return this.f508tv.getText().toString();
    }

    public void setAdapter(ListAdapter paramListAdapter) {
        this._Adapter = paramListAdapter;
    }

    public void setPrompt(String paramString) {
        this._Title = paramString;
    }

    public void setText(String paramString) {
        this.f508tv.setText(paramString);
    }
}
