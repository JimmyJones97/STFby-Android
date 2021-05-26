package  com.xzy.forestSystem.gogisapi.MyControls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class InputSpinner extends LinearLayout {
    private ICallback m_Callback;
    private boolean m_ClickCallback;
    private Context m_Context;
    private List<String> m_DataList;
    private boolean m_SelectItemFromTag;
    private String m_SelectReturnTag;
    private String m_ShowDialogTitle;
    private EditText m_TextView;
    private ICallback pCallback;

    public InputSpinner(Context context) {
        this(context, null);
    }

    public InputSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.numberProgressBarStyle);
    }

    public InputSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.m_Context = null;
        this.m_TextView = null;
        this.m_DataList = new ArrayList();
        this.m_ShowDialogTitle = "请选择";
        this.m_SelectReturnTag = "SpinnerSelectCallback";
        this.m_Callback = null;
        this.m_ClickCallback = true;
        this.m_SelectItemFromTag = false;
        this.pCallback = new ICallback() { // from class: com.xzy.forestSystem.gogisapi.MyControls.InputSpinner.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                String tmpStr;
                if (command.equals("ShowSelectedDialog") && InputSpinner.this.m_DataList.size() > 0) {
                    String[] tmpArray = (String[]) InputSpinner.this.m_DataList.toArray(new String[InputSpinner.this.m_DataList.size()]);
                    if (InputSpinner.this.m_SelectItemFromTag) {
                        tmpStr = String.valueOf(InputSpinner.this.getTag());
                    } else {
                        tmpStr = InputSpinner.this.m_TextView.getText().toString();
                    }
                    new AlertDialog.Builder(InputSpinner.this.m_Context, 3).setTitle(InputSpinner.this.m_ShowDialogTitle).setSingleChoiceItems(tmpArray, InputSpinner.this.m_DataList.indexOf(tmpStr), new DialogInterface.OnClickListener() { // from class: com.xzy.forestSystem.gogisapi.MyControls.InputSpinner.1.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface arg0, int arg1) {
                            String tmpStr2 = (String) InputSpinner.this.m_DataList.get(arg1);
                            InputSpinner.this.setText(tmpStr2);
                            if (InputSpinner.this.m_Callback != null) {
                                InputSpinner.this.m_Callback.OnClick(InputSpinner.this.m_SelectReturnTag, tmpStr2);
                            }
                            arg0.dismiss();
                        }
                    }).show();
                }
            }
        };
        this.m_Context = context;
        View contentView = LayoutInflater.from(context).inflate(R.layout.inputspinner_layout, (ViewGroup) this, true);
        contentView.setBackgroundResource(R.drawable.spinnerlist_bg);
        this.m_TextView = (EditText) contentView.findViewById(R.id.et_inputspinner);
        contentView.findViewById(R.id.ll_inputspinner).setOnClickListener(new View.OnClickListener() { // from class: com.xzy.forestSystem.gogisapi.MyControls.InputSpinner.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (!InputSpinner.this.isEnabled()) {
                    return;
                }
                if (!InputSpinner.this.m_ClickCallback) {
                    InputSpinner.this.pCallback.OnClick("ShowSelectedDialog", null);
                } else if (InputSpinner.this.m_Callback != null) {
                    InputSpinner.this.m_Callback.OnClick(InputSpinner.this.m_SelectReturnTag, InputSpinner.this.m_TextView.getText().toString());
                }
            }
        });
    }

    public void SetCallback(ICallback pCallback2) {
        if (pCallback2 != null) {
            this.m_Callback = pCallback2;
        }
    }

    public void SetClickCallback(boolean clickCallback) {
        this.m_ClickCallback = clickCallback;
    }

    public void SetClickCallback(ICallback pICallback) {
        this.m_ClickCallback = true;
        this.m_Callback = pICallback;
    }

    public void SetSelectItemFromTag(boolean value) {
        this.m_SelectItemFromTag = value;
    }

    public EditText getEditTextView() {
        return this.m_TextView;
    }

    public void setText(String currentValue) {
        if (this.m_TextView != null) {
            this.m_TextView.setText(currentValue);
        }
    }

    public String getText() {
        if (this.m_TextView != null) {
            return this.m_TextView.getText().toString();
        }
        return "";
    }

    public void SetSelectItemList(List<String> list) {
        if (list == null) {
            this.m_ClickCallback = true;
            return;
        }
        this.m_DataList = list;
        this.m_ClickCallback = false;
    }

    public void SetSelectItemList(List<String> paramList, String title) {
        SetSelectItemList(paramList);
        this.m_ShowDialogTitle = title;
    }

    public void SetDialogTitle(String title) {
        this.m_ShowDialogTitle = title;
    }

    public void SetSelectReturnTag(String tag) {
        if (tag != null) {
            this.m_SelectReturnTag = tag;
        }
    }

    public int getSelectedIndex() {
        return this.m_DataList.indexOf(this.m_TextView.getText().toString());
    }

    @Override // android.view.View
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setBackgroundResource(R.drawable.spinnerlist_bg);
        } else {
            setBackgroundResource(R.drawable.spinnerlist_disable_bg);
        }
    }

    public void AddTextClick() {
        if (getEditTextView() != null) {
            getEditTextView().setOnClickListener(new View.OnClickListener() { // from class: com.xzy.forestSystem.gogisapi.MyControls.InputSpinner.3
                @Override // android.view.View.OnClickListener
                public void onClick(View arg0) {
                    InputSpinner.this.performClick();
                }
            });
        }
    }

    public void setEditTextEnable(boolean enable) {
        if (getEditTextView() != null && !enable) {
            getEditTextView().setInputType(0);
        }
    }
}
