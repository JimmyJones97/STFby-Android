package  com.xzy.forestSystem.gogisapi.MyControls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class AutoInputSpinner extends LinearLayout {
    private ICallback m_Callback;
    private boolean m_ClickCallback;
    private Context m_Context;
    private List<String> m_DataList;
    private boolean m_SelectItemFromTag;
    private String m_SelectReturnTag;
    private String m_ShowDialogTitle;
    private AutoCompleteTextView m_TextView;
    private ICallback pCallback;

    public AutoInputSpinner(Context context) {
        this(context, null);
    }

    public AutoInputSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.numberProgressBarStyle);
    }

    public AutoInputSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.m_Context = null;
        this.m_TextView = null;
        this.m_DataList = new ArrayList();
        this.m_ShowDialogTitle = "请选择";
        this.m_SelectReturnTag = "SpinnerSelectCallback";
        this.m_Callback = null;
        this.m_ClickCallback = true;
        this.m_SelectItemFromTag = false;
        this.pCallback = new ICallback() { // from class: com.xzy.forestSystem.gogisapi.MyControls.AutoInputSpinner.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                String tmpStr;
                if (command.equals("ShowSelectedDialog") && AutoInputSpinner.this.m_DataList.size() > 0) {
                    String[] tmpArray = (String[]) AutoInputSpinner.this.m_DataList.toArray(new String[AutoInputSpinner.this.m_DataList.size()]);
                    if (AutoInputSpinner.this.m_SelectItemFromTag) {
                        tmpStr = String.valueOf(AutoInputSpinner.this.getTag());
                    } else {
                        tmpStr = AutoInputSpinner.this.m_TextView.getText().toString();
                    }
                    new AlertDialog.Builder(AutoInputSpinner.this.m_Context, 3).setTitle(AutoInputSpinner.this.m_ShowDialogTitle).setSingleChoiceItems(tmpArray, AutoInputSpinner.this.m_DataList.indexOf(tmpStr), new DialogInterface.OnClickListener() { // from class: com.xzy.forestSystem.gogisapi.MyControls.AutoInputSpinner.1.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface arg0, int arg1) {
                            String tmpStr2 = (String) AutoInputSpinner.this.m_DataList.get(arg1);
                            AutoInputSpinner.this.setText(tmpStr2);
                            if (AutoInputSpinner.this.m_Callback != null) {
                                AutoInputSpinner.this.m_Callback.OnClick(AutoInputSpinner.this.m_SelectReturnTag, tmpStr2);
                            }
                            arg0.dismiss();
                        }
                    }).show();
                }
            }
        };
        this.m_Context = context;
        View contentView = LayoutInflater.from(context).inflate(R.layout.autoinputspinner_layout, (ViewGroup) this, true);
        contentView.setBackgroundResource(R.drawable.spinnerlist_bg);
        this.m_TextView = (AutoCompleteTextView) contentView.findViewById(R.id.et_autoinputspinner);
        contentView.findViewById(R.id.ll_inputspinner).setOnClickListener(new View.OnClickListener() { // from class: com.xzy.forestSystem.gogisapi.MyControls.AutoInputSpinner.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (!AutoInputSpinner.this.isEnabled()) {
                    return;
                }
                if (!AutoInputSpinner.this.m_ClickCallback) {
                    AutoInputSpinner.this.pCallback.OnClick("ShowSelectedDialog", null);
                } else if (AutoInputSpinner.this.m_Callback != null) {
                    AutoInputSpinner.this.m_Callback.OnClick(AutoInputSpinner.this.m_SelectReturnTag, AutoInputSpinner.this.m_TextView.getText().toString());
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
            getEditTextView().setOnClickListener(new View.OnClickListener() { // from class: com.xzy.forestSystem.gogisapi.MyControls.AutoInputSpinner.3
                @Override // android.view.View.OnClickListener
                public void onClick(View arg0) {
                    AutoInputSpinner.this.performClick();
                }
            });
        }
    }

    public void setEditTextEnable(boolean enable) {
        if (getEditTextView() != null && !enable) {
            getEditTextView().setInputType(0);
        }
    }

    public void setTipList(List<String> tipList) {
        if (this.m_TextView != null && tipList != null && tipList.size() > 0) {
            this.m_TextView.setAdapter(new ArrayAdapter<>(this.m_Context, 17367050, (String[]) tipList.toArray(new String[tipList.size()])));
        }
    }
}
