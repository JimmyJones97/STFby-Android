package  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.LoadingDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.QRCode.EncodingHandler;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class YangDiShare_Dialog {
    private XDialogTemplate _Dialog;
    Handler mTimerHandler;
    private ICallback m_Callback;
    private ImageView m_ImageView;
    private boolean m_NeedLoopCheckInfo;
    private Bitmap m_QRBitmap;
    Runnable m_Runnable;
    private String m_ShareKey;
    private List<String> m_yangdiNameList;
    Handler myHandler;
    private ICallback pCallback;
    LoadingDialog progressDialog;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public YangDiShare_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_yangdiNameList = null;
        this.m_QRBitmap = null;
        this.m_ImageView = null;
        this.m_ShareKey = "";
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiShare_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("分享") && YangDiShare_Dialog.this.m_QRBitmap != null) {
                    String tmpPath = String.valueOf(Common.GetAPPPath()) + "/Others/QR_" + Common.fileDateFormat.format(new Date());
                    if (Common.SaveImgFile(tmpPath, YangDiShare_Dialog.this.m_QRBitmap)) {
                        Uri uri = Uri.fromFile(new File(tmpPath));
                        Intent intent = new Intent("android.intent.action.SEND");
                        intent.setType("image/*");
                        intent.putExtra("android.intent.extra.STREAM", uri);
                        intent.setFlags(268435456);
                        PubVar.MainContext.startActivity(Intent.createChooser(intent, "分享图片"));
                    }
                } else if (command.equals("返回")) {
                    Common.ShowYesNoDialogWithAlert(YangDiShare_Dialog.this._Dialog.getContext(), "正在共享样地数据.\r\n是否取消数据共享?", true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiShare_Dialog.1.1
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command2, Object pObject) {
                            if (command2.equals("YES")) {
                                YangDiShare_Dialog.this.cancelShareInfo();
                                YangDiShare_Dialog.this.m_NeedLoopCheckInfo = false;
                                YangDiShare_Dialog.this._Dialog.dismiss();
                            }
                        }
                    });
                }
            }
        };
        this.m_NeedLoopCheckInfo = false;
        this.mTimerHandler = new Handler();
        this.m_Runnable = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiShare_Dialog.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    YangDiShare_Dialog.this.refreshShareInfo();
                } catch (Exception e) {
                }
                if (YangDiShare_Dialog.this.m_NeedLoopCheckInfo) {
                    YangDiShare_Dialog.this.mTimerHandler.postDelayed(this, 5000);
                }
            }
        };
        this.myHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiShare_Dialog.3
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    Common.ShowDialog(String.valueOf(msg.obj));
                } else if (msg.what == 10) {
                    YangDiShare_Dialog.this.m_NeedLoopCheckInfo = false;
                    Common.ShowDialog(String.valueOf(msg.obj));
                } else if (msg.what == 1) {
                    YangDiShare_Dialog.this.m_ShareKey = msg.obj.toString();
                    YangDiShare_Dialog.this.m_QRBitmap = EncodingHandler.createQRCode(String.valueOf("ForestSystemShareData:") + YangDiShare_Dialog.this.m_ShareKey, 400, 400);
                    if (YangDiShare_Dialog.this.m_QRBitmap != null) {
                        YangDiShare_Dialog.this.m_ImageView.setImageBitmap(YangDiShare_Dialog.this.m_QRBitmap);
                    }
                    YangDiShare_Dialog.this.m_NeedLoopCheckInfo = true;
                    YangDiShare_Dialog.this.mTimerHandler.post(YangDiShare_Dialog.this.m_Runnable);
                    Common.ShowDialog("请将该二维码共享给其他用户!");
                } else if (msg.what == 2) {
                    Common.SetTextViewValueOnID(YangDiShare_Dialog.this._Dialog, (int) R.id.tv_YangdiInfo, "共享样地数据:" + String.valueOf(YangDiShare_Dialog.this.m_yangdiNameList.size()) + "条        共享用户:" + msg.obj.toString());
                }
            }
        };
        this.progressDialog = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.yangdishare_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("样地数据共享");
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetHeadButtons("1,2130837935,分享,分享", this.pCallback);
        this._Dialog.SetAllowedCloseDialog(false);
        this._Dialog.SetCallback(this.pCallback);
        this._Dialog.SetCancelCommand("");
        this.m_ImageView = (ImageView) this._Dialog.findViewById(R.id.img_ShareQRCode);
    }

    public void SetYangDiNameList(List<String> yangdiNameList) {
        this.m_yangdiNameList = yangdiNameList;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void startShareYangDiData() {
        if (this.m_yangdiNameList.size() > 0) {
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_YangdiInfo, "共享样地数据:" + String.valueOf(this.m_yangdiNameList.size()) + "条");
            boolean tmpBool01 = false;
            try {
                String tmpPath = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/SenLinDuCha.dbx";
                if (new File(tmpPath).exists()) {
                    StringBuilder tmpStringBuilder = new StringBuilder();
                    SQLiteDBHelper m_SQLiteDBHelper = new SQLiteDBHelper(tmpPath);
                    List<String> tmpList01 = new ArrayList<>();
                    String tmpSql2 = Common.CombineStrings("','", this.m_yangdiNameList);
                    SQLiteReader tmpSQLiteReader = m_SQLiteDBHelper.Query("Select YangDiName||','||YangDiIndex||','||Sheng||','||Xian||','||Xiang||','||Cun||','||PartIndex||','||LayerID||','||SYSID||','||X||','||Y AS B From T_YangDiInfo Where YangDiName IN ('" + tmpSql2 + "')");
                    while (tmpSQLiteReader.Read()) {
                        tmpList01.add(tmpSQLiteReader.GetString(0));
                    }
                    if (tmpList01.size() > 0) {
                        tmpStringBuilder.append("&yangdiinfo=" + Common.CombineStrings("@", tmpList01));
                        SQLiteReader tmpSQLiteReader2 = m_SQLiteDBHelper.Query("Select YangDiName ||']'|| GROUP_CONCAT(GenJing||','||Shan||','||Ma||','||Kuo, ';') AS B From T_YangDiData Where YangDiName IN ('" + tmpSql2 + "') GROUP BY YangDiName");
                        List<String> tmpList02 = new ArrayList<>();
                        while (tmpSQLiteReader2.Read()) {
                            tmpList02.add(tmpSQLiteReader2.GetString(0));
                        }
                        if (tmpList02.size() > 0) {
                            tmpStringBuilder.append("&yangdidata=" + Common.CombineStrings("@", tmpList02));
                            tmpBool01 = true;
                            this.progressDialog = new LoadingDialog(this._Dialog.getContext());
                            this.progressDialog.setTitle("正在建立共享数据识别码,请稍候...");
                            this.progressDialog.setCancelable(false);
                            this.progressDialog.setBindCallback(this.pCallback, "加载文件");
                            this.progressDialog.showDialog();
                            final String tmpShareYangdiDataString = tmpStringBuilder.toString();
                            new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiShare_Dialog.4
                                @Override // java.lang.Thread, java.lang.Runnable
                                public void run() {
                                    try {
                                        HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(String.valueOf(PubVar.ServerURL) + "YangDiHandler.ashx?method=sharedata&deviceid=" + PubVar.m_AuthorizeTools.getUserAndroidID() + "&" + tmpShareYangdiDataString));
                                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                            JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                                            if (jsonObject.getBoolean("success")) {
                                                String tmpMsg = jsonObject.getString("msg");
                                                Message msg = YangDiShare_Dialog.this.myHandler.obtainMessage();
                                                msg.what = 1;
                                                msg.obj = tmpMsg;
                                                YangDiShare_Dialog.this.myHandler.sendMessage(msg);
                                            } else {
                                                String tmpMsg2 = jsonObject.getString("msg");
                                                Message msg2 = YangDiShare_Dialog.this.myHandler.obtainMessage();
                                                msg2.what = 0;
                                                msg2.obj = tmpMsg2;
                                                YangDiShare_Dialog.this.myHandler.sendMessage(msg2);
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    if (YangDiShare_Dialog.this.progressDialog != null) {
                                        YangDiShare_Dialog.this.progressDialog.dismiss();
                                    }
                                }
                            }.start();
                        }
                    }
                }
                if (!tmpBool01) {
                    Common.ShowDialog("当前还没有样地数据.");
                }
            } catch (Exception e) {
                Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
            }
        } else {
            Common.ShowDialog("没有选择任何共享的数据.");
            this._Dialog.dismiss();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshShareInfo() {
        if (this.m_NeedLoopCheckInfo) {
            new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiShare_Dialog.5
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(String.valueOf(PubVar.ServerURL) + "YangDiHandler.ashx?method=getshareinfo&deviceid=" + PubVar.m_AuthorizeTools.getUserAndroidID() + "&key=" + YangDiShare_Dialog.this.m_ShareKey));
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                            JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                            if (jsonObject.getBoolean("success")) {
                                String tmpShareCountString = new JSONObject(jsonObject.getString("msg")).getString("sharedusercount");
                                Message msg = YangDiShare_Dialog.this.myHandler.obtainMessage();
                                msg.what = 2;
                                msg.obj = tmpShareCountString;
                                YangDiShare_Dialog.this.myHandler.sendMessage(msg);
                            } else {
                                String tmpMsg = jsonObject.getString("msg");
                                Message msg2 = YangDiShare_Dialog.this.myHandler.obtainMessage();
                                msg2.what = 10;
                                msg2.obj = tmpMsg;
                                YangDiShare_Dialog.this.myHandler.sendMessage(msg2);
                            }
                        }
                    } catch (Exception e) {
                    }
                    if (YangDiShare_Dialog.this.progressDialog != null) {
                        YangDiShare_Dialog.this.progressDialog.dismiss();
                    }
                }
            }.start();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void cancelShareInfo() {
        this.m_NeedLoopCheckInfo = false;
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiShare_Dialog.6
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(String.valueOf(PubVar.ServerURL) + "YangDiHandler.ashx?method=cancelshare&deviceid=" + PubVar.m_AuthorizeTools.getUserAndroidID() + "&key=" + YangDiShare_Dialog.this.m_ShareKey));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                        if (jsonObject.getBoolean("success")) {
                            String tmpMsg = jsonObject.getString("msg");
                            Message msg = YangDiShare_Dialog.this.myHandler.obtainMessage();
                            msg.what = 0;
                            YangDiShare_Dialog.this.m_NeedLoopCheckInfo = false;
                            msg.obj = tmpMsg;
                            YangDiShare_Dialog.this.myHandler.sendMessage(msg);
                        } else {
                            String tmpMsg2 = jsonObject.getString("msg");
                            Message msg2 = YangDiShare_Dialog.this.myHandler.obtainMessage();
                            msg2.what = 0;
                            YangDiShare_Dialog.this.m_NeedLoopCheckInfo = false;
                            msg2.obj = tmpMsg2;
                            YangDiShare_Dialog.this.myHandler.sendMessage(msg2);
                        }
                    }
                } catch (Exception e) {
                }
                if (YangDiShare_Dialog.this.progressDialog != null) {
                    YangDiShare_Dialog.this.progressDialog.dismiss();
                }
            }
        }.start();
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiShare_Dialog.7
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                YangDiShare_Dialog.this.startShareYangDiData();
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
