package  com.xzy.forestSystem.gogisapi.Common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateManager {
    private boolean cancelUpdate = false;
    Handler handler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Common.UpdateManager.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (msg.obj != null) {
                        Common.ShowDialog(msg.obj.toString());
                        return;
                    }
                    return;
                case 1:
                    UpdateManager.this.showUpdataDialog();
                    return;
                case 2:
                    if (UpdateManager.this.infoShowType == 1) {
                        Common.ShowToast("获取服务器更新信息失败");
                        return;
                    }
                    return;
                case 3:
                    Common.ShowToast("下载新版本失败");
                    return;
                default:
                    return;
            }
        }
    };
    private int infoShowType = 0;
    private Context mContext;
    private ProgressDialog mProgress;
    private int progress;
    private UpdataInfoClass updataInfoObj = null;

    public UpdateManager(Context context) {
        this.mContext = context;
    }

    public void CheckUpdate(int InfoShowType) {
        this.infoShowType = InfoShowType;
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Common.UpdateManager.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                new CheckVersionTask().run();
            }
        }.start();
    }

    public class CheckVersionTask implements Runnable {
        public CheckVersionTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(String.valueOf(PubVar.ServerURL) + "update.xml").openConnection();
                conn.setConnectTimeout(5000);
                InputStream is = conn.getInputStream();
                UpdateManager.this.updataInfoObj = Common.GetUpdataInfo(is);
                if (UpdateManager.this.updataInfoObj.getVerionNumber() == 0 || PubVar.VersionNumber < UpdateManager.this.updataInfoObj.getVerionNumber()) {
                    Message msg = new Message();
                    msg.what = 1;
                    UpdateManager.this.handler.sendMessage(msg);
                } else if (UpdateManager.this.infoShowType == 1) {
                    Message msg2 = new Message();
                    msg2.what = 0;
                    msg2.obj = "当前软件已经为最新版本!";
                    UpdateManager.this.handler.sendMessage(msg2);
                }
            } catch (Exception e) {
                Message msg3 = new Message();
                msg3.what = 2;
                UpdateManager.this.handler.sendMessage(msg3);
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void showUpdataDialog() {
        if (this.updataInfoObj != null) {
            new UpdateDialog().ShowDialog();
        }
    }

    /* access modifiers changed from: protected */
    public void downLoadApk() {
        if (this.mProgress == null) {
            this.mProgress = new ProgressDialog(this.mContext);
            this.mProgress.setProgressStyle(1);
            this.mProgress.setMessage("正在下载更新");
            this.mProgress.setCancelable(false);
        }
        this.mProgress.show();
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Common.UpdateManager.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    File file = Common.GetFileFromServer(UpdateManager.this.updataInfoObj.getUrl(), UpdateManager.this.mProgress);
                    sleep(3000);
                    UpdateManager.this.installApk(file);
                } catch (Exception e) {
                    Message msg = new Message();
                    msg.what = 3;
                    UpdateManager.this.handler.sendMessage(msg);
                    e.printStackTrace();
                } finally {
                    UpdateManager.this.mProgress.dismiss();
                }
            }
        }.start();
    }

    /* access modifiers changed from: protected */
    public void installApk(File file) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        this.mContext.startActivity(intent);
    }

    private void LoginMain() {
    }

    /* access modifiers changed from: package-private */
    public class UpdateDialog {
        private XDialogTemplate _Dialog;
        private ICallback m_Callback;
        private ICallback pCallback;

        public void SetCallback(ICallback tmpICallback) {
            this.m_Callback = tmpICallback;
        }

        public UpdateDialog() {
            this._Dialog = null;
            this.m_Callback = null;
            this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Common.UpdateManager.UpdateDialog.1
                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                public void OnClick(String paramString, Object pObject) {
                }
            };
            this._Dialog = new XDialogTemplate(PubVar.MainContext);
            this._Dialog.SetLayoutView(R.layout.updateinfo_dialog);
            this._Dialog.Resize(1.0f, 0.6f);
            this._Dialog.SetCaption("系统版本信息");
            this._Dialog.findViewById(R.id.buttonStartUpdate).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.UpdateManager.UpdateDialog.2
                @Override // android.view.View.OnClickListener
                public void onClick(View arg0) {
                    UpdateManager.this.downLoadApk();
                }
            });
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void RefreshFormInfo() {
            if (UpdateManager.this.updataInfoObj != null) {
                this._Dialog.SetCaption("发现系统更新版本");
                Common.SetValueToView(UpdateManager.this.updataInfoObj.getVersion(), this._Dialog.findViewById(R.id.editTextVersion));
                Common.SetValueToView(UpdateManager.this.updataInfoObj.getDescription(), this._Dialog.findViewById(R.id.editTextDescp));
            }
        }

        public void ShowDialog() {
            this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.UpdateManager.UpdateDialog.3
                @Override // android.content.DialogInterface.OnShowListener
                public void onShow(DialogInterface paramDialogInterface) {
                    UpdateDialog.this.RefreshFormInfo();
                }
            });
            this._Dialog.show();
        }
    }
}
