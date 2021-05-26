package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class ImportShpSetting_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private InputSpinner m_InputSpinner;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public ImportShpSetting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_InputSpinner = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShpSetting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("确定")) {
                    String tempValue = ImportShpSetting_Dialog.this.m_InputSpinner.getText().toString();
                    if (tempValue == null || tempValue.length() == 0) {
                        Common.ShowDialog("请选择或输入字符编码格式!");
                        return;
                    }
                    CheckBox tmpCheckBox = (CheckBox) ImportShpSetting_Dialog.this._Dialog.findViewById(R.id.ck_isLatLng);
                    if (ImportShpSetting_Dialog.this.m_Callback != null) {
                        ImportShpSetting_Dialog.this.m_Callback.OnClick("字符编码选择返回", new Object[]{tempValue, Boolean.valueOf(tmpCheckBox.isChecked())});
                    }
                    ImportShpSetting_Dialog.this._Dialog.dismiss();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.importshpsetting_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("参数设置");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        initialLayout();
    }

    private void initialLayout() {
        this.m_InputSpinner = new InputSpinner(this._Dialog.getContext());
        ViewGroup.LayoutParams tempLayoutParams = new ViewGroup.LayoutParams(this.m_InputSpinner.getWidth(), this.m_InputSpinner.getHeight());
        tempLayoutParams.width = -1;
        tempLayoutParams.height = -2;
        this.m_InputSpinner.setLayoutParams(tempLayoutParams);
        List<String> tmpList = new ArrayList<>();
        tmpList.add("GB2312");
        tmpList.add(StringEncodings.UTF8);
        tmpList.add("UTF-16");
        tmpList.add("GBK");
        this.m_InputSpinner.SetSelectItemList(tmpList, "请选择编码");
        this.m_InputSpinner.getEditTextView().setEnabled(true);
        this.m_InputSpinner.setEnabled(true);
        this.m_InputSpinner.getEditTextView().setText(tmpList.get(0));
        this.m_InputSpinner.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShpSetting_Dialog.2
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("SpinnerSelectCallback")) {
                    ImportShpSetting_Dialog.this.m_InputSpinner.getEditTextView().setText(String.valueOf(pObject));
                }
            }
        });
        ((LinearLayout) this._Dialog.findViewById(R.id.ll_LinearLayout001)).addView(this.m_InputSpinner);
    }

    public void ShowDialog() {
        this._Dialog.show();
    }
}
