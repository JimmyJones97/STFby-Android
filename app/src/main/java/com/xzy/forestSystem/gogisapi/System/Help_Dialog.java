package  com.xzy.forestSystem.gogisapi.System;

import android.content.DialogInterface;
import android.webkit.WebView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;

public class Help_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private WebView m_WebView;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public Help_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_WebView = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.System.Help_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("上一页")) {
                    Help_Dialog.this.m_WebView.goBack();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.help_dialog);
        this._Dialog.Resize(1.0f, 1.0f);
        this._Dialog.SetCaption("在线帮助");
        this._Dialog.setCancelable(false);
        this._Dialog.SetHeadButtons("1,2130837741,上一页,上一页", this.pCallback);
        this.m_WebView = (WebView) this._Dialog.findViewById(R.id.webView1);
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.System.Help_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Help_Dialog.this.m_WebView.loadUrl("http://219.139.130.151/ForestSystemServer2/Help/help.html");
            }
        });
        this._Dialog.show();
    }
}
