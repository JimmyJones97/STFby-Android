package  com.xzy.forestSystem.gogisapi.MyControls;

import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;

public class Input2_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private AutoCompleteTextView m_InputEditTxt;
    private AutoCompleteTextView m_InputEditTxt2;
    private Object m_ReturnTag;
    private Object m_TagValue;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public Input2_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_ReturnTag = null;
        this.m_InputEditTxt = null;
        this.m_InputEditTxt2 = null;
        this.m_TagValue = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.Input2_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("确定")) {
                    String tempValue = Input2_Dialog.this.m_InputEditTxt.getText().toString();
                    String tempValue2 = Input2_Dialog.this.m_InputEditTxt2.getText().toString();
                    if (Input2_Dialog.this.m_Callback != null) {
                        Input2_Dialog.this.m_Callback.OnClick("输入参数", new Object[]{Input2_Dialog.this.m_ReturnTag, new String[]{tempValue, tempValue2}, Input2_Dialog.this.m_TagValue});
                    }
                    Input2_Dialog.this._Dialog.dismiss();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.input2_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("参数输入");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_InputEditTxt = (AutoCompleteTextView) this._Dialog.findViewById(R.id.ed_inputpara);
        this.m_InputEditTxt2 = (AutoCompleteTextView) this._Dialog.findViewById(R.id.ed_inputpara2);
    }

    public void SetReturnTag(Object object) {
        this.m_ReturnTag = object;
    }

    public void SetTagValue(Object obj) {
        this.m_TagValue = obj;
    }

    public void setValues(String title, String paraName, String paraValue, String paraName2, String paraValue2, String tipInfo) {
        this._Dialog.SetCaption(title);
        ((TextView) this._Dialog.findViewById(R.id.tv_paraName)).setText(paraName);
        ((TextView) this._Dialog.findViewById(R.id.tv_paraName2)).setText(paraName2);
        this.m_InputEditTxt.setText(paraValue);
        this.m_InputEditTxt2.setText(paraValue2);
        if (tipInfo == null || tipInfo.equals("")) {
            ((TextView) this._Dialog.findViewById(R.id.tv_tipInfo)).setVisibility(8);
            return;
        }
        ((TextView) this._Dialog.findViewById(R.id.tv_tipInfo)).setVisibility(0);
        ((TextView) this._Dialog.findViewById(R.id.tv_tipInfo)).setText(tipInfo);
    }

    public void setValues(String title, String paraName, String paraValue, String paraName2, String paraValue2) {
        setValues(title, paraName, paraValue, paraName2, paraValue, null);
    }

    public void setInputType(int type) {
        this.m_InputEditTxt.setInputType(type);
    }

    public void ShowDialog() {
        this._Dialog.show();
    }
}
