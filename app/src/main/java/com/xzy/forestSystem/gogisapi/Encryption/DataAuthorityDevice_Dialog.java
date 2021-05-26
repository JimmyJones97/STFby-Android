package  com.xzy.forestSystem.gogisapi.Encryption;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Others.MediaActivity;
import com.stczh.gzforestSystem.R;
import java.util.HashMap;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class DataAuthorityDevice_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private CheckBox m_CheckBox01;
    private CheckBox m_CheckBox02;
    private EditText m_EditTxt;
    private HashMap m_HashMap;
    Handler m_NetHandler;
    private String m_ReturnTag;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public void SetReturnTag(String tag) {
        this.m_ReturnTag = tag;
    }

    public DataAuthorityDevice_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_HashMap = null;
        this.m_EditTxt = null;
        this.m_ReturnTag = "设备授权管理";
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityDevice_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                boolean tmpBool;
                if (command.equals("确定")) {
                    String tmpDeviceID = DataAuthorityDevice_Dialog.this.m_EditTxt.getText().toString();
                    if (tmpDeviceID == null || tmpDeviceID.equals("")) {
                        Common.ShowDialog("设备ID不能为空.");
                        return;
                    }
                    boolean tmpBool01 = DataAuthorityDevice_Dialog.this.m_CheckBox01.isChecked();
                    boolean tmpBool02 = DataAuthorityDevice_Dialog.this.m_CheckBox02.isChecked();
                    if (tmpBool01 || tmpBool02) {
                        if (DataAuthorityDevice_Dialog.this.m_HashMap == null) {
                            DataAuthorityDevice_Dialog.this.m_HashMap = new HashMap();
                            DataAuthorityDevice_Dialog.this.m_HashMap.put("D1", false);
                            DataAuthorityDevice_Dialog.this.m_HashMap.put("D2", tmpDeviceID);
                        }
                        if (tmpBool01) {
                            DataAuthorityDevice_Dialog.this.m_HashMap.put("D3", "是");
                        } else {
                            DataAuthorityDevice_Dialog.this.m_HashMap.put("D3", "否");
                        }
                        if (tmpBool02) {
                            DataAuthorityDevice_Dialog.this.m_HashMap.put("D4", "是");
                        } else {
                            DataAuthorityDevice_Dialog.this.m_HashMap.put("D4", "否");
                        }
                        if (DataAuthorityDevice_Dialog.this.m_Callback != null) {
                            DataAuthorityDevice_Dialog.this.m_Callback.OnClick(DataAuthorityDevice_Dialog.this.m_ReturnTag, DataAuthorityDevice_Dialog.this.m_HashMap);
                        }
                        DataAuthorityDevice_Dialog.this._Dialog.dismiss();
                        return;
                    }
                    Common.ShowDialog("不能同时设置设备不可读与不可写.");
                } else if (command.equals("QRResult")) {
                    MediaActivity.BindCallbak = null;
                    String tmpStr = String.valueOf(object);
                    if (tmpStr == null || !tmpStr.equals("")) {
                    }
                    if (tmpStr.contains("[GOGIS]\r\n设备ID:")) {
                        tmpBool = true;
                    } else {
                        tmpBool = false;
                    }
                    if (tmpBool) {
                        DataAuthorityDevice_Dialog.this.m_EditTxt.setText(tmpStr.substring("[GOGIS]\r\n设备ID:".length() + tmpStr.indexOf("[GOGIS]\r\n设备ID:")));
                        return;
                    }
                    Common.ShowToast("二维码信息:" + tmpStr + "\r\n无法识别的设备ID信息,请使用[关于系统]-[设备信息]中的二维码.");
                } else if (command.equals("获取到共享设备列表")) {
                    final String[] tmpStrs = String.valueOf(object).split(";");
                    String[] tmpStrs2 = new String[tmpStrs.length];
                    int tid = 0;
                    for (String tmpStr2 : tmpStrs) {
                        int tmpI = tmpStr2.lastIndexOf(",");
                        String tmpStr22 = "[设备名称]:" + tmpStr2;
                        if (tmpI > 0) {
                            tmpStr22 = "[设备名称]:" + tmpStr2.substring(0, tmpI) + "\r\n[设备ID]:" + tmpStr2.substring(tmpI + 1);
                        }
                        tid++;
                        tmpStrs2[tid] = tmpStr22;
                    }
                    new AlertDialog.Builder(DataAuthorityDevice_Dialog.this._Dialog.getContext(), 3).setTitle("请选择当前共享的设备:").setSingleChoiceItems(tmpStrs2, -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityDevice_Dialog.1.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface arg0, int arg1) {
                            if (arg1 >= 0 && arg1 < tmpStrs.length) {
                                String tmpStr3 = tmpStrs[arg1];
                                if (tmpStr3.contains(",")) {
                                    tmpStr3 = tmpStr3.substring(tmpStr3.lastIndexOf(",") + 1);
                                }
                                DataAuthorityDevice_Dialog.this.m_EditTxt.setText(tmpStr3);
                            }
                            arg0.dismiss();
                        }
                    }).show();
                }
            }
        };
        this.m_NetHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityDevice_Dialog.2
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    DataAuthorityDevice_Dialog.this.pCallback.OnClick("获取到共享设备列表", String.valueOf(msg.obj));
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.dataauthoritydevice_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("设备授权管理");
        this._Dialog.setCanceledOnTouchOutside(true);
        this.m_EditTxt = (EditText) this._Dialog.findViewById(R.id.et_dataAuth_Device);
        this.m_CheckBox01 = (CheckBox) this._Dialog.findViewById(R.id.checkBox1);
        this.m_CheckBox02 = (CheckBox) this._Dialog.findViewById(R.id.checkBox2);
        this.m_CheckBox01.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityDevice_Dialog.3
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    DataAuthorityDevice_Dialog.this.m_CheckBox02.setVisibility(0);
                } else {
                    DataAuthorityDevice_Dialog.this.m_CheckBox02.setVisibility(4);
                }
            }
        });
        this._Dialog.findViewById(R.id.imgBtn_dataAuth_ThisDevice).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityDevice_Dialog.4
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                DataAuthorityDevice_Dialog.this.m_EditTxt.setText(PubVar.m_AuthorizeTools.getUserAndroidID());
                Common.ShowToast("设置本设备ID.");
            }
        });
        this._Dialog.findViewById(R.id.imgBtn_dataAuth_Copy).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityDevice_Dialog.5
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                DataAuthorityDevice_Dialog.this.m_EditTxt.setText(Common.PasteText(DataAuthorityDevice_Dialog.this._Dialog.getContext()));
                Common.ShowToast("粘贴设备ID信息.");
            }
        });
        this._Dialog.findViewById(R.id.imgBtn_dataAuth_QRScan).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityDevice_Dialog.6
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                try {
                    Intent localIntent = new Intent(PubVar._PubCommand.m_Context, MediaActivity.class);
                    localIntent.putExtra("Type", 2);
                    MediaActivity.BindCallbak = DataAuthorityDevice_Dialog.this.pCallback;
                    Common.ShowToast("扫描设备二维码.");
                    PubVar._PubCommand.m_Context.startActivity(localIntent);
                } catch (Exception e) {
                }
            }
        });
        this._Dialog.findViewById(R.id.imgBtn_dataAuth_RefreshCloud).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityDevice_Dialog.7
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                try {
                    Common.ShowProgressDialogWithoutClose("正在获取云共享设备信息...", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityDevice_Dialog.7.1
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            final Handler tmpPrgHandler = (Handler) pObject;
                            new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityDevice_Dialog.7.1.1
                                @Override // java.lang.Thread, java.lang.Runnable
                                public void run() {
                                    String tmpMsg;
                                    try {
                                        HttpGet httpRequest = new HttpGet(String.valueOf(PubVar.ServerURL) + FileSelector_Dialog.sRoot + PubVar.AppName + "Server/ShareDeviceHandler.ashx?method=getdevicelist");
                                        HttpParams httpParameters = new BasicHttpParams();
                                        HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
                                        HttpResponse httpResponse = new DefaultHttpClient(httpParameters).execute(httpRequest);
                                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                            JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                                            if (jsonObject.getBoolean("success") && (tmpMsg = jsonObject.getString("msg")) != null && !tmpMsg.equals("")) {
                                                Message msg = DataAuthorityDevice_Dialog.this.m_NetHandler.obtainMessage();
                                                msg.what = 1;
                                                msg.obj = tmpMsg;
                                                DataAuthorityDevice_Dialog.this.m_NetHandler.sendMessage(msg);
                                            }
                                        }
                                    } catch (Exception e) {
                                    }
                                    if (tmpPrgHandler != null) {
                                        Message msg2 = tmpPrgHandler.obtainMessage();
                                        msg2.what = 0;
                                        tmpPrgHandler.sendMessage(msg2);
                                    }
                                }
                            }.start();
                        }
                    });
                } catch (Exception e) {
                }
            }
        });
        this._Dialog.findViewById(R.id.btn_dataauthdevice_OK).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityDevice_Dialog.8
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                DataAuthorityDevice_Dialog.this.pCallback.OnClick("确定", null);
            }
        });
    }

    public void SetDeviceHash(HashMap hashMap) {
        this.m_HashMap = hashMap;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityDevice_Dialog.9
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                if (DataAuthorityDevice_Dialog.this.m_HashMap != null) {
                    DataAuthorityDevice_Dialog.this._Dialog.SetCaption("设备授权修改");
                    DataAuthorityDevice_Dialog.this.m_EditTxt.setEnabled(false);
                    DataAuthorityDevice_Dialog.this.m_EditTxt.setText(String.valueOf(DataAuthorityDevice_Dialog.this.m_HashMap.get("D2")));
                    if (String.valueOf(DataAuthorityDevice_Dialog.this.m_HashMap.get("D3")).equals("是")) {
                        DataAuthorityDevice_Dialog.this.m_CheckBox01.setChecked(true);
                    }
                    if (String.valueOf(DataAuthorityDevice_Dialog.this.m_HashMap.get("D4")).equals("是")) {
                        DataAuthorityDevice_Dialog.this.m_CheckBox02.setChecked(true);
                    }
                    DataAuthorityDevice_Dialog.this._Dialog.findViewById(R.id.ll_dataAuth_DeviceBtn).setVisibility(8);
                    return;
                }
                DataAuthorityDevice_Dialog.this._Dialog.SetCaption("添加授权设备");
            }
        });
        this._Dialog.show();
    }
}
