package  com.xzy.forestSystem.gogisapi.Others;

import android.content.DialogInterface;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class MediaFileInfo_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private String m_TxtFilePath;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public MediaFileInfo_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_TxtFilePath = "";
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.MediaFileInfo_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("确定")) {
                    String tmpHead = Common.GetTextValueOnID(MediaFileInfo_Dialog.this._Dialog, (int) R.id.tv_Content);
                    Common.SaveTextFile(MediaFileInfo_Dialog.this.m_TxtFilePath, String.valueOf(tmpHead) + "\r\n备注:" + Common.GetEditTextValueOnID(MediaFileInfo_Dialog.this._Dialog, R.id.et_txtContent));
                    MediaFileInfo_Dialog.this._Dialog.dismiss();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.mediafileinfo_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("文件信息");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
    }

    public void setTextFilePath(String filepath) {
        this.m_TxtFilePath = filepath;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void loadTxtFile() {
        try {
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
                    StringBuffer tmpMsgBuider = new StringBuffer("");
                    boolean tmpIsHead = true;
                    while (true) {
                        try {
                            String line = reader.readLine();
                            if (line == null) {
                                break;
                            } else if (!tmpIsHead) {
                                tmpMsgBuider.append("\r\n");
                                tmpMsgBuider.append(line);
                            } else if (line.indexOf("备注:") == 0) {
                                tmpIsHead = false;
                                tmpMsgBuider.append(line.substring("备注:".length()));
                            } else {
                                if (sb.length() > 0) {
                                    sb.append("\r\n");
                                }
                                sb.append(line);
                            }
                        } catch (IOException e) {
                        }
                    }
                    Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_Content, sb.toString());
                    Common.SetEditTextValueOnID(this._Dialog, R.id.et_txtContent, tmpMsgBuider.toString());
                    inputStream.close();
                } catch (Exception e2) {
                }
            }
        } catch (Exception e3) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Others.MediaFileInfo_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                MediaFileInfo_Dialog.this.loadTxtFile();
            }
        });
        this._Dialog.show();
    }
}
