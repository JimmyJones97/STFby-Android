package com.xzy.forestSystem;

import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class SampleForm {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public SampleForm() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class: com.xzy.forestSystem.SampleForm.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.test);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("图层管理");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        List<String> tmpList = new ArrayList<>();
        tmpList.add("1");
        tmpList.add("2");
        tmpList.add("3");
        Common.SetSpinnerListData(this._Dialog, (int) R.id.spinnerList1, "测试", "请选择", tmpList, (String) null, this.pCallback);
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.xzy.forestSystem.SampleForm.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
            }
        });
        this._Dialog.show();
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                view.getTag().toString();
            }
        }
    }
}
