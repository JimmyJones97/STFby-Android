package  com.xzy.forestSystem.gogisapi.System;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;

public class ContactUS_Dialog {
    private XDialogTemplate _Dialog;
    TextView m_AuthTypeTxt;
    private ICallback m_Callback;
    Button m_RegisterBtn;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public ContactUS_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_RegisterBtn = null;
        this.m_AuthTypeTxt = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.System.ContactUS_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.contactus_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("联系我们");
        this._Dialog.findViewById(R.id.imageView1).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.System.ContactUS_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                ContactUS_Dialog.this.joinQQGroup("s6ehHXlj3Ro6cZYqs6bV2AcN7Hx8rX8H");
            }
        });
    }

    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        try {
            this._Dialog.getContext().startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.System.ContactUS_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
            }
        });
        this._Dialog.show();
    }
}
