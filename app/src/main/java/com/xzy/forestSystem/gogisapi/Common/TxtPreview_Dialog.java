package  com.xzy.forestSystem.gogisapi.Common;

import android.content.DialogInterface;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class TxtPreview_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private String m_TxtContent;
    private String m_TxtFilePath;

    public void SetCallback(ICallback pCallback) {
        this.m_Callback = pCallback;
    }

    public TxtPreview_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_TxtFilePath = "";
        this.m_TxtContent = "";
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.txtpreview_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("文本预览");
    }

    public void setTextFilePath(String filepath) {
        this.m_TxtFilePath = filepath;
    }

    public void setTextContent(String content) {
        this.m_TxtContent = content;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void loadTxtFile() {
        try {
            if (this.m_TxtContent.length() > 0) {
                Common.SetTextViewValueOnID(this._Dialog, (int) R.id.et_txtContent, this.m_TxtContent);
            }
            if (this.m_TxtFilePath.length() > 0 && new File(this.m_TxtFilePath).exists()) {
                try {
                    FileInputStream inputStream = new FileInputStream(this.m_TxtFilePath);
                    InputStreamReader inputStreamReader = null;
                    try {
                        inputStreamReader = new InputStreamReader(inputStream, "gbk");
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                    BufferedReader reader = new BufferedReader(inputStreamReader);
                    StringBuffer sb = new StringBuffer("");
                    while (true) {
                        try {
                            String line = reader.readLine();
                            if (line == null) {
                                break;
                            }
                            if (sb.length() > 0) {
                                sb.append("\r\n");
                            }
                            sb.append(line);
                        } catch (IOException e) {
                        }
                    }
                    Common.SetTextViewValueOnID(this._Dialog, (int) R.id.et_txtContent, sb.toString());
                    inputStream.close();
                } catch (Exception e2) {
                }
            }
        } catch (Exception e3) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.TxtPreview_Dialog.1
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                TxtPreview_Dialog.this.loadTxtFile();
            }
        });
        this._Dialog.show();
    }
}
