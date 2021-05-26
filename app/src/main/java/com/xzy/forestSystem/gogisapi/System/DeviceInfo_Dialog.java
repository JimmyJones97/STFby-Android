package  com.xzy.forestSystem.gogisapi.System;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.QRCode.EncodingHandler;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.net.URLEncoder;

public class DeviceInfo_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private String m_DeviceBrandInfo;
    private String m_DeviceID;
    private Bitmap m_DeviceQRBmp;
    private Handler m_NetHandler;
    private Button m_ShareButton;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public DeviceInfo_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_ShareButton = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.System.DeviceInfo_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("分享")) {
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("text/plain");
                        intent.putExtra("android.intent.extra.SUBJECT", "Share");
                        intent.putExtra("android.intent.extra.TEXT", "分享我的设备信息.\r\n" + Common.GetTextValueOnID(DeviceInfo_Dialog.this._Dialog, (int) R.id.tv_deviceInfo));
                        intent.setFlags(268435456);
                        PubVar.MainContext.startActivity(Intent.createChooser(intent, "分享我的设备信息"));
                    } else if (command.equals("设备信息共享")) {
                        DeviceInfo_Dialog.this.ShareDeviceToServer(true);
                    } else if (command.equals("设备信息停止共享")) {
                        DeviceInfo_Dialog.this.ShareDeviceToServer(false);
                    } else if (command.equals("设备二维码")) {
                        if (DeviceInfo_Dialog.this.m_DeviceQRBmp != null) {
                            String tmpPath = String.valueOf(Common.GetAPPPath()) + "/DeviceQR.png";
                            if (Common.SaveImgFile(tmpPath, DeviceInfo_Dialog.this.m_DeviceQRBmp)) {
                                Common.ShowToast("保存设备的二维码图片至:\r\n" + tmpPath);
                                File f = new File(tmpPath);
                                if (f != null && f.exists()) {
                                    Intent intent2 = new Intent("android.intent.action.SEND");
                                    intent2.setType("image/jpg");
                                    intent2.putExtra("android.intent.extra.STREAM", Uri.fromFile(f));
                                    intent2.setFlags(268435456);
                                    PubVar.MainContext.startActivity(Intent.createChooser(intent2, "[GoGIS] 分享我的设备二维码信息"));
                                    return;
                                }
                                return;
                            }
                            return;
                        }
                        Common.ShowToast("没有生成设备的二维码信息.");
                    } else if (command.equals("返回")) {
                        DeviceInfo_Dialog.this.ShareDeviceToServer(false);
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_DeviceID = "";
        this.m_DeviceBrandInfo = "";
        this.m_DeviceQRBmp = null;
        this.m_NetHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.System.DeviceInfo_Dialog.2
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    DeviceInfo_Dialog.this.m_ShareButton.setText("设备信息云共享中...");
                    DeviceInfo_Dialog.this.m_ShareButton.setTag("设备信息停止共享");
                } else if (msg.what == 2) {
                    DeviceInfo_Dialog.this.m_ShareButton.setText("设备信息云共享");
                    DeviceInfo_Dialog.this.m_ShareButton.setTag("设备信息共享");
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.deviceinfo_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("设备信息");
        this._Dialog.SetHeadButtons("1,2130837935,分享,分享", this.pCallback);
        this._Dialog.setCanceledOnTouchOutside(false);
        this._Dialog.setCancelable(false);
        this.m_ShareButton = (Button) this._Dialog.findViewById(R.id.bt_deviceInfo_Share);
        this.m_ShareButton.setOnClickListener(new ViewClick());
        this._Dialog.SetCallback(this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        try {
            this.m_DeviceID = PubVar.m_AuthorizeTools.getUserAndroidID();
            this.m_DeviceBrandInfo = String.valueOf(Build.BRAND) + " " + Build.MODEL;
            StringBuilder tmpSB = new StringBuilder();
            tmpSB.append("设备ID:" + this.m_DeviceID);
            tmpSB.append("\r\n设备型号:" + Build.MODEL);
            tmpSB.append("\r\n系统版本:" + Build.VERSION.RELEASE);
            tmpSB.append("\r\n设备制造商:" + Build.BRAND);
            String tmpCPU = Common.getCpuName();
            if (tmpCPU != null) {
                tmpSB.append("\r\nCPU:" + tmpCPU);
            }
            tmpSB.append("\r\n屏幕宽度:" + PubVar.ScreenWidth);
            tmpSB.append("\r\n屏幕高度:" + PubVar.ScreenHeight);
            tmpSB.append("\r\nDPI:" + PubVar.ScaledDensity);
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_deviceInfo, tmpSB.toString());
            this.m_DeviceQRBmp = EncodingHandler.createQRCode("[GOGIS]\r\n设备ID:" + this.m_DeviceID, 400, 400);
            if (this.m_DeviceQRBmp != null) {
                ImageView tmpImageView = (ImageView) this._Dialog.findViewById(R.id.img_deviceID_QR);
                tmpImageView.setImageBitmap(this.m_DeviceQRBmp);
                tmpImageView.setOnClickListener(new ViewClick());
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void ShareDeviceToServer(final boolean isShare) {
        String tmpShowMsg = "正在请求云共享设备信息...";
        if (!isShare) {
            tmpShowMsg = "正在取消云共享设备信息...";
        }
        try {
            Common.ShowProgressDialogWithoutClose(tmpShowMsg, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.System.DeviceInfo_Dialog.3
                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                public void OnClick(String paramString, Object pObject) {
                    Handler tmpPrgHandler = (Handler) pObject;
                    String tmpDeviceInfo = "";
                    try {
                        tmpDeviceInfo = URLEncoder.encode(DeviceInfo_Dialog.this.m_DeviceBrandInfo, "utf-8");
                    } catch (Exception e) {
                    }
                    Common.ShareDeviceToServer(DeviceInfo_Dialog.this.m_DeviceID, tmpDeviceInfo, isShare, DeviceInfo_Dialog.this.m_NetHandler, tmpPrgHandler);
                }
            });
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.System.DeviceInfo_Dialog.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                DeviceInfo_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: package-private */
    public class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                DeviceInfo_Dialog.this.pCallback.OnClick(view.getTag().toString(), null);
            }
        }
    }
}
