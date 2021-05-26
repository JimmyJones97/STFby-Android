package  com.xzy.forestSystem.gogisapi.Server;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.xzy.forestSystem.gogisapi.Geodatabase.StringEncodings;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class LoginSetting_Dialog {
    private boolean IsConnectedServer;
    String Password;
    String ServerIP;
    int ServerPort;
    String UserName;
    private XDialogTemplate _Dialog;
    private boolean _RecvDataFromServer;
    Handler mHandler;
    private ICallback m_Callback;
    Runnable networkTask;
    private ICallback pCallback;
    Runnable recvDataTask;
    Socket socket;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public LoginSetting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Server.LoginSetting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
            }
        };
        this.socket = null;
        this.networkTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Server.LoginSetting_Dialog.2
            @Override // java.lang.Runnable
            public void run() {
                Message msg = new Message();
                if (LoginSetting_Dialog.this.socket != null && LoginSetting_Dialog.this.socket.isConnected()) {
                    try {
                        LoginSetting_Dialog.this.socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        msg.what = 0;
                        msg.obj = e.getMessage();
                    }
                }
                try {
                    LoginSetting_Dialog.this.socket = new Socket(LoginSetting_Dialog.this.ServerIP, LoginSetting_Dialog.this.ServerPort);
                    LoginSetting_Dialog.this.socket.setSoTimeout(5000);
                    msg.what = 1;
                } catch (Exception e2) {
                    msg.what = 0;
                    msg.obj = e2.getMessage();
                }
                LoginSetting_Dialog.this.mHandler.sendMessage(msg);
            }
        };
        this.mHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Server.LoginSetting_Dialog.3
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    if (msg.what == 0) {
                        Common.ShowToast("连接服务器错误!" + String.valueOf(msg.obj));
                    } else if (msg.what == 1) {
                        Common.ShowToast("连接服务器成功!");
                        if (LoginSetting_Dialog.this.socket != null && LoginSetting_Dialog.this.socket.isConnected() && !LoginSetting_Dialog.this.socket.isOutputShutdown()) {
                            LoginSetting_Dialog.this.SendContentToServer("PHONECONNECT|" + LoginSetting_Dialog.this.UserName + "|" + LoginSetting_Dialog.this.Password);
                        }
                    } else if (msg.what == 2) {
                        Common.ShowToast(String.valueOf(msg.obj));
                    } else if (msg.what == 20) {
                        Common.ShowToast(String.valueOf(msg.obj));
                    }
                } catch (Exception e) {
                }
            }
        };
        this._RecvDataFromServer = false;
        this.recvDataTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Server.LoginSetting_Dialog.4
            @Override // java.lang.Runnable
            public void run() {
                int tmpHeadLen;
                while (LoginSetting_Dialog.this._RecvDataFromServer) {
                    try {
                        if (LoginSetting_Dialog.this.socket != null && !LoginSetting_Dialog.this.socket.isClosed() && LoginSetting_Dialog.this.socket.isConnected() && !LoginSetting_Dialog.this.socket.isInputShutdown()) {
                            InputStream inputStream = LoginSetting_Dialog.this.socket.getInputStream();
                            byte[] tmpHeadBytes = new byte[4];
                            if (inputStream.read(tmpHeadBytes) > 0 && (tmpHeadLen = Common.BytesToInt(tmpHeadBytes)) > 0) {
                                byte[] buffer = new byte[tmpHeadLen];
                                if (inputStream.read(buffer) > 0) {
                                    String[] tmpStrs01 = new String(buffer, StringEncodings.UTF8).split("\\|");
                                    if (tmpStrs01 == null || tmpStrs01.length <= 0) {
                                        Message msg = new Message();
                                        msg.what = 20;
                                        msg.obj = "非法数据请求.";
                                        LoginSetting_Dialog.this.mHandler.sendMessage(msg);
                                    } else {
                                        String tmpHead = tmpStrs01[0];
                                        if (tmpHead.equals("XPS")) {
                                            LoginSetting_Dialog.this.ProcessCommand(tmpStrs01, null);
                                        } else if (tmpHead.equals("XPB")) {
                                            int tmpDataLen = Integer.parseInt(tmpStrs01[1]);
                                            if (tmpDataLen > 0) {
                                                byte[] tmpFileData = new byte[tmpDataLen];
                                                int tmpStartI = 0;
                                                while (true) {
                                                    int tmpCount = inputStream.read(tmpFileData, tmpStartI, 1024);
                                                    if (tmpCount <= 0) {
                                                        break;
                                                    }
                                                    tmpStartI += tmpCount;
                                                }
                                                LoginSetting_Dialog.this.ProcessCommand(tmpStrs01, tmpFileData);
                                            }
                                        } else {
                                            Message msg2 = new Message();
                                            msg2.what = 20;
                                            msg2.obj = "非法数据请求.";
                                            LoginSetting_Dialog.this.mHandler.sendMessage(msg2);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        };
        this.IsConnectedServer = false;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.login_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("账号设置");
        ((Button) this._Dialog.findViewById(R.id.btn_OK)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_Test)).setOnClickListener(new ViewClick());
        Common.SetEditTextValueOnID(this._Dialog, R.id.et_serverIP, PubVar.HostIP);
        Common.SetEditTextValueOnID(this._Dialog, R.id.et_serverPort, String.valueOf(PubVar.HostPort));
        Common.SetEditTextValueOnID(this._Dialog, R.id.et_username, PubVar.HostUserName);
        Common.SetEditTextValueOnID(this._Dialog, R.id.et_password, PubVar.HostUserPwd);
        this._RecvDataFromServer = true;
        new Thread(this.recvDataTask).start();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("连接测试")) {
                this.ServerIP = Common.GetEditTextValueOnID(this._Dialog, R.id.et_serverIP);
                this.ServerPort = Integer.parseInt(Common.GetEditTextValueOnID(this._Dialog, R.id.et_serverPort));
                this.UserName = Common.GetEditTextValueOnID(this._Dialog, R.id.et_username);
                this.Password = Common.GetEditTextValueOnID(this._Dialog, R.id.et_password);
                new Thread(this.networkTask).start();
            } else if (command.equals("保存设置")) {
                String tmpServerIP = Common.GetEditTextValueOnID(this._Dialog, R.id.et_serverIP);
                String tmpServerPort = Common.GetEditTextValueOnID(this._Dialog, R.id.et_serverPort);
                String tmpUserName = Common.GetEditTextValueOnID(this._Dialog, R.id.et_username);
                String tmpPassword = Common.GetEditTextValueOnID(this._Dialog, R.id.et_password);
                PubVar.HostIP = tmpServerIP;
                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_HostIP", PubVar.HostIP);
                PubVar.HostPort = Integer.parseInt(tmpServerPort);
                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_HostPort", Integer.valueOf(PubVar.HostPort));
                PubVar.HostUserName = tmpUserName;
                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_HostUserName", PubVar.HostUserName);
                PubVar.HostUserPwd = tmpPassword;
                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_HostUserPwd", PubVar.HostUserPwd);
                Common.ShowToast("保存设置成功.");
                if (this.m_Callback != null) {
                    this.m_Callback.OnClick("用户登入设置", null);
                }
                this._Dialog.dismiss();
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void ProcessCommand(String[] HeadString, byte[] relateData) {
        try {
            String str = HeadString[0];
            if (!HeadString[1].equals("CONNECTED")) {
                return;
            }
            if (HeadString[2].trim().toLowerCase().equals("true")) {
                this.IsConnectedServer = true;
                Message msg = new Message();
                msg.what = 2;
                msg.obj = "登入服务器成功.";
                this.mHandler.sendMessage(msg);
                return;
            }
            this.IsConnectedServer = false;
            Message msg2 = new Message();
            msg2.what = 2;
            String tmpStr02 = "登入服务器失败.";
            if (HeadString.length > 3) {
                tmpStr02 = String.valueOf(tmpStr02) + HeadString[3];
            }
            msg2.obj = tmpStr02;
            this.mHandler.sendMessage(msg2);
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean SendContentToServer(String content) {
        try {
            byte[] myHeadBf = "XPS|".getBytes();
            byte[] tempMsgBs = content.getBytes();
            int tempLen = tempMsgBs.length;
            byte[] tempLenBs = Common.IntToByteArray(tempLen);
            int count = myHeadBf.length;
            byte[] outBuffer = new byte[(tempLenBs.length + count + tempLen)];
            int i = 0;
            int tid = 0;
            while (i < count) {
                outBuffer[tid] = myHeadBf[i];
                i++;
                tid++;
            }
            int count2 = tempLenBs.length;
            int i2 = 0;
            while (i2 < count2) {
                outBuffer[tid] = tempLenBs[i2];
                i2++;
                tid++;
            }
            int i3 = 0;
            while (i3 < tempLen) {
                int tid2 = tid + 1;
                outBuffer[tid] = tempMsgBs[i3];
                i3++;
                tid = tid2;
            }
            OutputStream outsocket = this.socket.getOutputStream();
            outsocket.write(outBuffer);
            outsocket.flush();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Server.LoginSetting_Dialog.5
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
                LoginSetting_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
