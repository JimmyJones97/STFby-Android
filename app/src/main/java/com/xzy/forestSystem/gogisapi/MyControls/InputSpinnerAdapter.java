package  com.xzy.forestSystem.gogisapi.MyControls;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import com.xzy.forestSystem.gogisapi.Common.Common;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class InputSpinnerAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context m_Context = null;
    private List<String> m_DataList = new ArrayList();
    private EditText m_EditText = null;

    public InputSpinnerAdapter(Context context) {
        this.m_Context = context;
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(context);
        }
    }

    public EditText GetEditTextView() {
        if (this.m_EditText == null) {
            this.m_EditText = new EditText(this.m_Context);
            this.m_EditText.setTextColor(this.m_Context.getResources().getColor(R.color.dialog_txt_color));
            this.m_EditText.setBackgroundDrawable(this.m_Context.getResources().getDrawable(R.drawable.edittextstyle));
            this.m_EditText.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            this.m_EditText.setOnClickListener(new View.OnClickListener() { // from class: com.xzy.forestSystem.gogisapi.MyControls.InputSpinnerAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View arg0) {
                    InputSpinnerAdapter.this.m_EditText.setFocusable(true);
                    InputSpinnerAdapter.this.m_EditText.setFocusableInTouchMode(true);
                    InputSpinnerAdapter.this.m_EditText.requestFocus();
                }
            });
            this.m_EditText.addTextChangedListener(new TextWatcher() { // from class: com.xzy.forestSystem.gogisapi.MyControls.InputSpinnerAdapter.2
                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable arg0) {
                    InputSpinnerAdapter.this.SetDataList(Common.StrArrayToList(new String[]{arg0.toString()}));
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }
            });
        }
        return this.m_EditText;
    }

    public void SetDataList(List<String> paramList) {
        this.m_DataList = paramList;
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.m_DataList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int paramInt) {
        return this.m_DataList.get(paramInt);
    }

    @Override // android.widget.Adapter
    public long getItemId(int paramInt) {
        return 0;
    }

    @Override // android.widget.Adapter
    public View getView(int paramInt, View paramView, ViewGroup parent) {
        if (paramView == null) {
            this.m_EditText = (EditText) this.mInflater.inflate(R.layout.inputspinner_layout, parent, false).findViewById(R.id.et_inputspinner);
        }
        this.m_EditText.setText(this.m_DataList.get(0).toString());
        return GetEditTextView();
    }
}
