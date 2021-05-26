package  com.xzy.forestSystem.gogisapi.MyControls;

import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;
import java.util.List;

public class SelectInput_Dialog {
    public boolean InputEnable;
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private InputSpinner m_InputSpinner;
    private Object m_ReturnTag;
    List<String> m_SelectValues;
    private Object m_TagValue;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public SelectInput_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_ReturnTag = null;
        this.m_InputSpinner = null;
        this.m_TagValue = null;
        this.InputEnable = false;
        this.m_SelectValues = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.SelectInput_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("确定")) {
                    String tempValue = SelectInput_Dialog.this.m_InputSpinner.getText().toString();
                    if (SelectInput_Dialog.this.m_Callback != null) {
                        SelectInput_Dialog.this.m_Callback.OnClick("输入选择返回", new Object[]{SelectInput_Dialog.this.m_ReturnTag, tempValue, SelectInput_Dialog.this.m_TagValue});
                    }
                    SelectInput_Dialog.this._Dialog.dismiss();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.input_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("参数输入");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
    }

    public void SetReturnTag(Object object) {
        this.m_ReturnTag = object;
    }

    public void SetTagValue(Object obj) {
        this.m_TagValue = obj;
    }

    private void initialLayout() {
        this.m_InputSpinner = new InputSpinner(this._Dialog.getContext());
        ViewGroup.LayoutParams tempLayoutParams = new ViewGroup.LayoutParams(this.m_InputSpinner.getWidth(), this.m_InputSpinner.getHeight());
        tempLayoutParams.width = -1;
        tempLayoutParams.height = -2;
        this.m_InputSpinner.setLayoutParams(tempLayoutParams);
        if (this.m_SelectValues != null && this.m_SelectValues.size() > 0) {
            this.m_InputSpinner.SetSelectItemList(this.m_SelectValues, "请选择");
        }
        this.m_InputSpinner.getEditTextView().setEnabled(this.InputEnable);
        this.m_InputSpinner.setEnabled(this.InputEnable);
        this.m_InputSpinner.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.SelectInput_Dialog.2
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("SpinnerSelectCallback")) {
                    SelectInput_Dialog.this.m_InputSpinner.getEditTextView().setText(String.valueOf(pObject));
                    SelectInput_Dialog.this.m_InputSpinner.setTag(String.valueOf(pObject));
                }
            }
        });
        ((LinearLayout) this._Dialog.findViewById(R.id.ll_inputSelect)).addView(this.m_InputSpinner);
    }

    public void setValues(String title, String paraName, String paraValue, String tipInfo) {
        this._Dialog.SetCaption(title);
        ((TextView) this._Dialog.findViewById(R.id.tv_paraName)).setText(paraName);
        this.m_InputSpinner.setText(paraValue);
        if (tipInfo == null || tipInfo.equals("")) {
            ((TextView) this._Dialog.findViewById(R.id.tv_tipInfo)).setVisibility(8);
            return;
        }
        ((TextView) this._Dialog.findViewById(R.id.tv_tipInfo)).setVisibility(0);
        ((TextView) this._Dialog.findViewById(R.id.tv_tipInfo)).setText(tipInfo);
    }

    public void setValues(String title, String paraName, String paraValue, String[] selectValues, String tipInfo) {
        this._Dialog.SetCaption(title);
        ((TextView) this._Dialog.findViewById(R.id.tv_paraName)).setText(paraName);
        this.m_InputSpinner.setText(paraValue);
        if (tipInfo == null || tipInfo.equals("")) {
            ((TextView) this._Dialog.findViewById(R.id.tv_tipInfo)).setVisibility(8);
            return;
        }
        ((TextView) this._Dialog.findViewById(R.id.tv_tipInfo)).setVisibility(0);
        ((TextView) this._Dialog.findViewById(R.id.tv_tipInfo)).setText(tipInfo);
    }

    public void setValues(String title, String paraName, String paraValue) {
        setValues(title, paraName, paraValue, null);
    }

    public void ShowDialog() {
        this._Dialog.show();
    }
}
