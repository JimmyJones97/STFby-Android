package  com.xzy.forestSystem.gogisapi.MyControls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class SpinnerList extends LinearLayout {
    private ICallback m_Callback;
    private boolean m_ClickCallback;
    private Context m_Context;
    private List<String> m_DataList;
    private String m_SelectReturnTag;
    private String m_ShowDialogTitle;
    private TextView m_TextView;
    private ICallback pCallback;

    public SpinnerList(Context context) {
        this(context, null);
    }

    public SpinnerList(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.numberProgressBarStyle);
    }

    public SpinnerList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.m_Context = null;
        this.m_TextView = null;
        this.m_DataList = new ArrayList();
        this.m_ShowDialogTitle = "请选择";
        this.m_SelectReturnTag = "OnItemSelected";
        this.m_Callback = null;
        this.m_ClickCallback = false;
        this.pCallback = new ICallback() { // from class: com.xzy.forestSystem.gogisapi.MyControls.SpinnerList.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("ShowSelectedDialog") && SpinnerList.this.m_DataList.size() > 0) {
                    int tmpIndex = SpinnerList.this.m_DataList.indexOf(SpinnerList.this.m_TextView.getText().toString());
                    new AlertDialog.Builder(SpinnerList.this.m_Context, 3).setTitle(SpinnerList.this.m_ShowDialogTitle).setSingleChoiceItems((String[]) SpinnerList.this.m_DataList.toArray(new String[SpinnerList.this.m_DataList.size()]), tmpIndex, new DialogInterface.OnClickListener() { // from class: com.xzy.forestSystem.gogisapi.MyControls.SpinnerList.1.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface arg0, int arg1) {
                            SpinnerList.this.SetText((String) SpinnerList.this.m_DataList.get(arg1));
                            arg0.dismiss();
                        }
                    }).show();
                }
            }
        };
        this.m_Context = context;
        View contentView = LayoutInflater.from(context).inflate(R.layout.spinnerlist_layout, (ViewGroup) this, true);
        contentView.setBackgroundResource(R.drawable.spinnerlist_bg);
        this.m_TextView = (TextView) contentView.findViewById(R.id.tv_spinnerlist);
        contentView.findViewById(R.id.ll_spinnerlistMain).setOnClickListener(new View.OnClickListener() { // from class: com.xzy.forestSystem.gogisapi.MyControls.SpinnerList.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (!SpinnerList.this.isEnabled()) {
                    return;
                }
                if (!SpinnerList.this.m_ClickCallback) {
                    SpinnerList.this.pCallback.OnClick("ShowSelectedDialog", null);
                } else if (SpinnerList.this.m_Callback != null) {
                    SpinnerList.this.m_Callback.OnClick(SpinnerList.this.m_SelectReturnTag, SpinnerList.this.m_TextView.getText().toString());
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

    public void SetText(String currentValue) {
        if (this.m_TextView != null) {
            this.m_TextView.setText(currentValue);
            if (this.m_Callback != null) {
                this.m_Callback.OnClick(this.m_SelectReturnTag, currentValue);
            }
        }
    }

    public void SetTextJust(String currentValue) {
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

    public void SetDataList(List<String> list) {
        if (list == null) {
            this.m_ClickCallback = true;
        } else {
            this.m_DataList = list;
        }
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
}
