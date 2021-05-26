package  com.xzy.forestSystem.gogisapi.Server;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import  com.xzy.forestSystem.gogisapi.Geodatabase.BaseDataObject;
import com.xzy.forestSystem.gogisapi.Geodatabase.StringEncodings;
import  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.gdal.ogr.ogrConstants;

public class DownloadFromServer_Dialog {
    private boolean IsConnectedServer;
    private boolean IsUploadingData;
    private int SuccessDownloadCount;
    private int TotalDownloadCount;
    private XDialogTemplate _Dialog;
    private boolean _RecvDataFromServer;
    private HashMap downloadLayersMap;
    Handler mHandler;
    private ICallback m_Callback;
    private BaseDataObject m_EditBaseDataObject;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_TableFactory;
    Runnable networkTask;
    private ICallback pCallback;
    CustomeProgressDialog progressDialog;
    Runnable recvDataTask;
    private Socket socket;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public DownloadFromServer_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.socket = null;
        this.m_TableFactory = null;
        this.m_MyTableDataList = null;
        this.TotalDownloadCount = 0;
        this.SuccessDownloadCount = 0;
        this.downloadLayersMap = new HashMap();
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Server.DownloadFromServer_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("账号")) {
                    LoginSetting_Dialog tmpDialog = new LoginSetting_Dialog();
                    tmpDialog.SetCallback(DownloadFromServer_Dialog.this.pCallback);
                    tmpDialog.ShowDialog();
                }
            }
        };
        this.m_EditBaseDataObject = null;
        this.networkTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Server.DownloadFromServer_Dialog.2
            @Override // java.lang.Runnable
            public void run() {
                Message msg = new Message();
                if (DownloadFromServer_Dialog.this.socket != null && DownloadFromServer_Dialog.this.socket.isConnected()) {
                    try {
                        DownloadFromServer_Dialog.this.socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        msg.what = 0;
                        msg.obj = e.getMessage();
                    }
                }
                try {
                    DownloadFromServer_Dialog.this.socket = new Socket(PubVar.HostIP, PubVar.HostPort);
                    DownloadFromServer_Dialog.this.socket.setSoTimeout(5000);
                    msg.what = 1;
                } catch (Exception e2) {
                    msg.what = 0;
                    msg.obj = e2.getMessage();
                }
                DownloadFromServer_Dialog.this.mHandler.sendMessage(msg);
            }
        };
        this.mHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Server.DownloadFromServer_Dialog.3
            @SuppressLint("WrongConstant")
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try {
                    if (msg.what == 0) {
                        Common.ShowToast("连接服务器错误!" + String.valueOf(msg.obj));
                    } else if (msg.what == 1) {
                        Common.ShowToast("连接服务器成功!");
                        if (!(DownloadFromServer_Dialog.this.socket == null || !DownloadFromServer_Dialog.this.socket.isConnected() || DownloadFromServer_Dialog.this.socket.isOutputShutdown())) {
                            DownloadFromServer_Dialog.this.SendContentToServer("PHONECONNECT|" + PubVar.HostUserName + "|" + PubVar.HostUserPwd);
                        }
                    } else if (msg.what == 100) {
                        Common.ShowToast(String.valueOf(msg.obj));
                    } else if (msg.what == 101) {
                        Common.ShowDialog(String.valueOf(msg.obj));
                    } else if (msg.what == 11) {
                        Common.ShowToast(String.valueOf(msg.obj));
                        DownloadFromServer_Dialog.this._Dialog.findViewById(R.id.btn_LoginIn).setVisibility(8);
                    } else if (msg.what == 30) {
                        Object[] tmpObjs = (Object[]) msg.obj;
                        if (tmpObjs != null && tmpObjs.length > 0) {
                            int tmpStartI = Integer.parseInt(String.valueOf(tmpObjs[0]));
                            String[] tmpMessages = (String[]) tmpObjs[1];
                            int tmpCount = ((tmpMessages.length - tmpStartI) + 1) / 8;
                            DownloadFromServer_Dialog.this.m_TableFactory = new MyTableFactory();
                            DownloadFromServer_Dialog.this.m_TableFactory.SetHeaderListView(DownloadFromServer_Dialog.this._Dialog.findViewById(R.id.list_layersList), "自定义", "选择,图层名称,图层类型,数据数目,创建用户,创建时间,修改时间,坐标系,共享类型", "checkbox,text,text,text,text,text,text,text,text", new int[]{50, 100, 100, 100, 100, 100, 100, 100, 100}, DownloadFromServer_Dialog.this.pCallback);
                            DownloadFromServer_Dialog.this.m_MyTableDataList = new ArrayList();
                            for (int i = 0; i < tmpCount; i++) {
                                int tmpI002 = tmpStartI + (i * 8);
                                try {
                                    HashMap tmpHashMap = new HashMap();
                                    tmpHashMap.put("D1", false);
                                    int tmpI0022 = tmpI002 + 1;
                                    try {
                                        tmpHashMap.put("D2", tmpMessages[tmpI002]);
                                        int tmpI0023 = tmpI0022 + 1;
                                        tmpHashMap.put("D3", tmpMessages[tmpI0022]);
                                        int tmpI0024 = tmpI0023 + 1;
                                        tmpHashMap.put("D4", tmpMessages[tmpI0023]);
                                        int tmpI0025 = tmpI0024 + 1;
                                        tmpHashMap.put("D5", tmpMessages[tmpI0024]);
                                        int tmpI0026 = tmpI0025 + 1;
                                        tmpHashMap.put("D6", tmpMessages[tmpI0025]);
                                        int tmpI0027 = tmpI0026 + 1;
                                        tmpHashMap.put("D7", tmpMessages[tmpI0026]);
                                        int tmpI0028 = tmpI0027 + 1;
                                        tmpHashMap.put("D8", tmpMessages[tmpI0027]);
                                        int i2 = tmpI0028 + 1;
                                        String tmpShareType = tmpMessages[tmpI0028];
                                        if (tmpShareType.equals("0")) {
                                            tmpShareType = "不共享";
                                        } else if (tmpShareType.equals("1")) {
                                            tmpShareType = "授权共享";
                                        } else if (tmpShareType.equals("2")) {
                                            tmpShareType = "全部共享";
                                        }
                                        tmpHashMap.put("D9", tmpShareType);
                                        DownloadFromServer_Dialog.this.m_MyTableDataList.add(tmpHashMap);
                                    } catch (Exception e) {
                                    }
                                } catch (Exception e2) {
                                }
                            }
                            DownloadFromServer_Dialog.this.m_TableFactory.BindDataToListView(DownloadFromServer_Dialog.this.m_MyTableDataList, new String[]{"D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8", "D9"}, DownloadFromServer_Dialog.this.pCallback);
                            Common.ShowToast("刷新空间数据库图层完成.");
                        }
                    } else if (msg.what == 40) {
                        DownloadFromServer_Dialog.this.TotalDownloadCount = 0;
                        DownloadFromServer_Dialog.this.SuccessDownloadCount = 0;
                        final List<String> tmpList = (List) msg.obj;
                        DownloadFromServer_Dialog.this.progressDialog = CustomeProgressDialog.createDialog(DownloadFromServer_Dialog.this._Dialog.getContext());
                        DownloadFromServer_Dialog.this.progressDialog.returnHandlerCode = 41;
                        DownloadFromServer_Dialog.this.progressDialog.SetReturnHandler(DownloadFromServer_Dialog.this.mHandler);
                        DownloadFromServer_Dialog.this.progressDialog.SetHeadTitle("云中心下载");
                        DownloadFromServer_Dialog.this.progressDialog.SetProgressTitle("下载图层数据:");
                        DownloadFromServer_Dialog.this.progressDialog.SetProgressInfo("准备下载数据...");
                        DownloadFromServer_Dialog.this.progressDialog.show();
                        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Server.DownloadFromServer_Dialog.3.1
                            @Override // java.lang.Thread, java.lang.Runnable
                            public void run() {
                                Handler tmpHandler = DownloadFromServer_Dialog.this.progressDialog.myHandler;
                                int tmpSuccLyrCount = 0;
                                int tmpAllDownloadCount = 0;
                                DownloadFromServer_Dialog.this.IsUploadingData = true;
                                try {
                                    if (tmpList.size() > 0) {
                                        int tmpLyrCount = tmpList.size() / 6;
                                        for (int i3 = 0; i3 < tmpLyrCount && DownloadFromServer_Dialog.this.IsUploadingData; i3++) {
                                            int tmpI01 = i3 * 6;
                                            String tmpLayerName = (String) tmpList.get(tmpI01);
                                            String tmpCreateUser = (String) tmpList.get(tmpI01 + 1);
                                            String tmpLayerType = (String) tmpList.get(tmpI01 + 2);
                                            String tmpFieldsInfoStr = (String) tmpList.get(tmpI01 + 3);
                                            Integer.parseInt((String) tmpList.get(tmpI01 + 4));
                                            int tmpTotalCount = Integer.parseInt((String) tmpList.get(tmpI01 + 5));
                                            Message msg2 = tmpHandler.obtainMessage();
                                            msg2.what = 1;
                                            msg2.obj = "下载图层【" + tmpLayerName + "】数据  [" + String.valueOf(i3 + 1) + FileSelector_Dialog.sRoot + String.valueOf(tmpLyrCount) + "]:";
                                            tmpHandler.sendMessage(msg2);
                                            String tmpLayerNameStr2 = tmpLayerName;
                                            String tmpLyrID = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerIDByName(tmpLayerNameStr2);
                                            while (!tmpLyrID.equals("")) {
                                                tmpLayerNameStr2 = String.valueOf(tmpLayerNameStr2) + "_1";
                                                tmpLyrID = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerIDByName(tmpLayerNameStr2);
                                            }
                                            FeatureLayer m_EditLayer = new FeatureLayer();
                                            m_EditLayer.SetEditMode(EEditMode.NEW);
                                            m_EditLayer.SetLayerName(tmpLayerNameStr2);
                                            m_EditLayer.SetLayerTypeName(tmpLayerType);
                                            m_EditLayer.SetFieldList(tmpFieldsInfoStr);
                                            m_EditLayer.SetEditMode(EEditMode.EDIT);
                                            if (PubVar.m_Workspace.GetDataSourceByEditing().CreateDataset(m_EditLayer.GetLayerID())) {
                                                boolean tmpBool01 = false;
                                                if (PubVar._PubCommand.m_ProjectDB.GetLayerRenderManage().RenderLayerForNew(m_EditLayer)) {
                                                    m_EditLayer.SetLayerIndex(0);
                                                    PubVar._Map.getGeoLayers().MoveTo(m_EditLayer.GetLayerID(), 0);
                                                    if (m_EditLayer.SaveLayerInfo()) {
                                                        PubVar._PubCommand.m_ProjectDB.GetLayerManage().InsertNewLayer(m_EditLayer, 0);
                                                        tmpBool01 = true;
                                                    }
                                                }
                                                if (tmpBool01) {
                                                    if (tmpTotalCount > 0) {
                                                        DownloadFromServer_Dialog.this.TotalDownloadCount = tmpTotalCount;
                                                        String tmpFIDStr = m_EditLayer.GetDataFieldsString();
                                                        DownloadFromServer_Dialog.this.downloadLayersMap.put(String.valueOf(tmpLayerName) + "_" + tmpCreateUser, new Object[]{tmpLayerNameStr2, m_EditLayer.GetLayerID(), tmpFIDStr});
                                                        DownloadFromServer_Dialog.this.SuccessDownloadCount = 0;
                                                        int tmpTotalCount2 = tmpTotalCount + 1;
                                                        for (int tmpI05 = 1; tmpI05 < tmpTotalCount2; tmpI05++) {
                                                            DownloadFromServer_Dialog.this.SendContentToServer("PHONECOMMAND|" + PubVar.HostUserName + "|downloaddata|" + tmpLayerName + "|" + tmpCreateUser + "|" + String.valueOf(tmpI05) + "|" + tmpFIDStr + "|");
                                                        }
                                                        while (DownloadFromServer_Dialog.this.SuccessDownloadCount != DownloadFromServer_Dialog.this.TotalDownloadCount && DownloadFromServer_Dialog.this.IsUploadingData) {
                                                            Thread.sleep(500);
                                                            Message msg3 = tmpHandler.obtainMessage();
                                                            msg3.what = 5;
                                                            msg3.obj = new Object[]{"下载数据：[" + DownloadFromServer_Dialog.this.SuccessDownloadCount + FileSelector_Dialog.sRoot + DownloadFromServer_Dialog.this.TotalDownloadCount + "]", Integer.valueOf((DownloadFromServer_Dialog.this.SuccessDownloadCount * 100) / DownloadFromServer_Dialog.this.TotalDownloadCount)};
                                                            tmpHandler.sendMessage(msg3);
                                                        }
                                                        tmpAllDownloadCount += DownloadFromServer_Dialog.this.SuccessDownloadCount;
                                                        tmpSuccLyrCount++;
                                                    } else {
                                                        Common.ShowToast("空间数据库中图层【" + tmpLayerName + "】中的数据数目为0.");
                                                        tmpSuccLyrCount++;
                                                    }
                                                } else if (PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL("delete from T_Layer where LayerID = '" + m_EditLayer.GetLayerID() + "'")) {
                                                    PubVar._Map.getGeoLayers().Remove(m_EditLayer.GetLayerID());
                                                    PubVar.m_Workspace.GetDataSourceByEditing().RemoveDataset(m_EditLayer.GetLayerID());
                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e3) {
                                }
                                DownloadFromServer_Dialog.this.IsUploadingData = false;
                                Message msg4 = new Message();
                                msg4.what = ogrConstants.wkbLinearRing;
                                msg4.obj = "下载数据结束.\n共下载图层" + String.valueOf(tmpSuccLyrCount) + "个,数据" + String.valueOf(tmpAllDownloadCount) + "条.";
                                DownloadFromServer_Dialog.this.mHandler.sendMessage(msg4);
                                DownloadFromServer_Dialog.this.progressDialog.dismiss();
                                DownloadFromServer_Dialog.this.progressDialog = null;
                            }
                        }.start();
                    } else if (msg.what == 41) {
                        DownloadFromServer_Dialog.this.IsUploadingData = false;
                    }
                } catch (Exception e3) {
                }
            }
        };
        this.progressDialog = null;
        this._RecvDataFromServer = false;
        this.recvDataTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Server.DownloadFromServer_Dialog.4
            @Override // java.lang.Runnable
            public void run() {
                int tmpHeadLen;
                int tmpDataLen;
                while (DownloadFromServer_Dialog.this._RecvDataFromServer) {
                    if (DownloadFromServer_Dialog.this.socket != null && !DownloadFromServer_Dialog.this.socket.isClosed() && DownloadFromServer_Dialog.this.socket.isConnected() && !DownloadFromServer_Dialog.this.socket.isInputShutdown()) {
                        InputStream inputStream = null;
                        try {
                            inputStream = DownloadFromServer_Dialog.this.socket.getInputStream();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        byte[] tmpHeadBytes = new byte[4];
                        try {
                            if (inputStream.read(tmpHeadBytes) > 0 && (tmpHeadLen = Common.BytesToInt(tmpHeadBytes)) > 0 && tmpHeadLen < 100000) {
                                byte[] buffer = new byte[tmpHeadLen];
                                try {
                                    if (inputStream.read(buffer) > 0) {
                                        String[] tmpStrs01 = new String(buffer, StringEncodings.UTF8).split("\\|");
                                        if (tmpStrs01 == null || tmpStrs01.length <= 0) {
                                            Message msg = new Message();
                                            msg.what = 20;
                                            msg.obj = "非法数据请求.";
                                            DownloadFromServer_Dialog.this.mHandler.sendMessage(msg);
                                        } else {
                                            String tmpHead = tmpStrs01[0];
                                            if (tmpHead.equals("XPS")) {
                                                DownloadFromServer_Dialog.this.ProcessCommand(tmpStrs01, null);
                                            } else if (tmpHead.equals("XPB")) {
                                                byte[] tmpFileLenBytes = new byte[4];
                                                if (inputStream.read(tmpFileLenBytes) > 0 && (tmpDataLen = Common.BytesToInt(tmpFileLenBytes)) > 0 && tmpDataLen < 20000) {
                                                    byte[] tmpFileData = new byte[tmpDataLen];
                                                    int tmpStartI = 0;
                                                    int tmpBlockSize = 1024;
                                                    if (tmpDataLen < 1024) {
                                                        tmpBlockSize = tmpDataLen;
                                                    }
                                                    while (true) {
                                                        int tmpCount = inputStream.read(tmpFileData, tmpStartI, tmpBlockSize);
                                                        if (tmpCount <= 0 || tmpStartI >= tmpDataLen) {
                                                            break;
                                                        }
                                                        tmpStartI += tmpCount;
                                                        if (tmpStartI + tmpBlockSize > tmpDataLen) {
                                                            tmpBlockSize = tmpDataLen - tmpStartI;
                                                        }
                                                    }
                                                    DownloadFromServer_Dialog.this.ProcessCommand(tmpStrs01, tmpFileData);
                                                }
                                            } else {
                                                Message msg2 = new Message();
                                                msg2.what = 20;
                                                msg2.obj = "非法数据请求.";
                                                DownloadFromServer_Dialog.this.mHandler.sendMessage(msg2);
                                            }
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        this.IsConnectedServer = false;
        this.IsUploadingData = false;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.downloadfromserver_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("云中心下载");
        this._Dialog.SetHeadButtons("1,2130837784,账号,账号", this.pCallback);
        ((Button) this._Dialog.findViewById(R.id.btn_RefreshLayers)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_LoginIn)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_OK)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.buttonSelectAll)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Server.DownloadFromServer_Dialog.5
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (DownloadFromServer_Dialog.this.m_MyTableDataList != null && DownloadFromServer_Dialog.this.m_MyTableDataList.size() > 0) {
                    for (HashMap<String, Object> tempHash : DownloadFromServer_Dialog.this.m_MyTableDataList) {
                        tempHash.put("D1", true);
                    }
                    DownloadFromServer_Dialog.this.m_TableFactory.notifyDataSetInvalidated();
                }
            }
        });
        ((Button) this._Dialog.findViewById(R.id.buttonSelectDe)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Server.DownloadFromServer_Dialog.6
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (DownloadFromServer_Dialog.this.m_MyTableDataList != null && DownloadFromServer_Dialog.this.m_MyTableDataList.size() > 0) {
                    for (HashMap<String, Object> tempHash : DownloadFromServer_Dialog.this.m_MyTableDataList) {
                        tempHash.put("D1", Boolean.valueOf(!Boolean.parseBoolean(tempHash.get("D1").toString())));
                    }
                    DownloadFromServer_Dialog.this.m_TableFactory.notifyDataSetInvalidated();
                }
            }
        });
        this._RecvDataFromServer = true;
        new Thread(this.recvDataTask).start();
        DoCommand("登入服务器");
        this.m_EditBaseDataObject = new BaseDataObject();
        this.m_EditBaseDataObject.SetSYS_TYPE("云中心下载");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("登入服务器")) {
                new Thread(this.networkTask).start();
            } else if (command.equals("刷新空间数据库图层")) {
                if (this.IsConnectedServer) {
                    SendCommandToServer("PHONECOMMAND|" + PubVar.HostUserName + "|getsharelayersimpleinfo");
                } else {
                    Common.ShowDialog("没有登入服务器,请先登入服务器后再进行此操作.");
                }
            } else if (command.equals("下载数据")) {
                if (this.IsConnectedServer) {
                    StringBuilder tmpDownloadInfo = new StringBuilder();
                    if (this.m_MyTableDataList != null && this.m_MyTableDataList.size() > 0) {
                        for (HashMap<String, Object> temphash : this.m_MyTableDataList) {
                            if (Boolean.parseBoolean(temphash.get("D1").toString())) {
                                if (tmpDownloadInfo.length() > 0) {
                                    tmpDownloadInfo.append(";");
                                }
                                tmpDownloadInfo.append(String.valueOf(temphash.get("D2").toString()) + "," + temphash.get("D5").toString());
                            }
                        }
                    }
                    if (tmpDownloadInfo.length() == 0) {
                        Common.ShowDialog("没有选择任何图层,请先选择需要下载的图层后再进行此操作.");
                    } else {
                        SendCommandToServer("PHONECOMMAND|" + PubVar.HostUserName + "|getlayersfieldinfo|" + ((Object) tmpDownloadInfo));
                    }
                } else {
                    Common.ShowDialog("没有登入服务器,请先登入服务器后再进行此操作.");
                }
            }
        } catch (Exception e) {
        }
    }

    private void SendCommandToServer(String command) {
        if (this.socket != null && this.socket.isConnected() && !this.socket.isOutputShutdown()) {
            SendContentToServer(command);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void ProcessCommand(String[] HeadString, byte[] fileData) {
        String[] tmpStrs03;
        try {
            String str = HeadString[0];
            String tmpReturnCmd = HeadString[1];
            if (tmpReturnCmd.equals("CONNECTED")) {
                if (HeadString[2].trim().toLowerCase().equals("true")) {
                    this.IsConnectedServer = true;
                    Message msg = new Message();
                    msg.what = 11;
                    msg.obj = "登入服务器成功.";
                    this.mHandler.sendMessage(msg);
                    DoCommand("刷新空间数据库图层");
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
            } else if (tmpReturnCmd.equals("GETSHARELAYERSIMPLEINFO")) {
                if (HeadString[2].toLowerCase().equals("true")) {
                    Message msg3 = new Message();
                    msg3.what = 30;
                    msg3.obj = new Object[]{3, HeadString};
                    this.mHandler.sendMessage(msg3);
                    return;
                }
                Message msg4 = new Message();
                msg4.what = 100;
                msg4.obj = HeadString[3];
                this.mHandler.sendMessage(msg4);
            } else if (tmpReturnCmd.equals("GETLAYERSFIELDINFO")) {
                if (HeadString[2].toLowerCase().equals("true")) {
                    List<String> tmpList = new ArrayList<>();
                    int count = HeadString.length;
                    for (int i = 1 + 2; i < count; i++) {
                        tmpList.add(HeadString[i]);
                    }
                    Message msg5 = new Message();
                    msg5.what = 40;
                    msg5.obj = tmpList;
                    this.mHandler.sendMessage(msg5);
                    return;
                }
                Message msg6 = new Message();
                msg6.what = 100;
                msg6.obj = HeadString[3];
                this.mHandler.sendMessage(msg6);
            } else if (tmpReturnCmd.equals("DOWNLOADDATA")) {
                this.SuccessDownloadCount++;
                String tmpLyrName = HeadString[2];
                String tmpCreateUser = HeadString[3];
                if (!HeadString[4].toLowerCase().equals("true")) {
                    Message msg7 = new Message();
                    msg7.what = 100;
                    msg7.obj = HeadString[3];
                    this.mHandler.sendMessage(msg7);
                } else if (this.downloadLayersMap.containsKey(String.valueOf(tmpLyrName) + "_" + tmpCreateUser)) {
                    Object[] tmpObjs02 = (Object[]) this.downloadLayersMap.get(String.valueOf(tmpLyrName) + "_" + tmpCreateUser);
                    String.valueOf(tmpObjs02[0]);
                    String m_EditLayerID = String.valueOf(tmpObjs02[1]);
                    String tmpDownID = HeadString[5];
                    this.m_EditBaseDataObject.SetBaseObjectRelateLayerID(m_EditLayerID);
                    int tempSYS_ID = this.m_EditBaseDataObject.SaveNewGeoToDb(fileData);
                    if (tempSYS_ID > -1) {
                        String tmpFIDNames = String.valueOf(tmpObjs02[2]);
                        int tmpFIDCount = HeadString.length - 6;
                        if (tmpFIDCount > 0 && (tmpStrs03 = tmpFIDNames.split(",")) != null && tmpStrs03.length == tmpFIDCount) {
                            String tmpSql = "Update " + m_EditLayerID + "_D Set ";
                            String tmpSql2 = "";
                            for (int i2 = 0; i2 < tmpFIDCount; i2++) {
                                if (tmpSql2.length() > 0) {
                                    tmpSql2 = String.valueOf(tmpSql2) + ",";
                                }
                                tmpSql2 = String.valueOf(tmpSql2) + tmpStrs03[i2] + "='" + HeadString[i2 + 6] + "' ";
                            }
                            PubVar.m_Workspace.GetDataSourceByEditing().ExcuteSQL(String.valueOf(tmpSql) + tmpSql2 + " WHERE SYS_ID=" + tempSYS_ID);
                            return;
                        }
                        return;
                    }
                    Message msg8 = new Message();
                    msg8.what = 100;
                    msg8.obj = "下载数据后保存时错误:图层【" + String.valueOf(tmpObjs02[0]) + "】 ID=" + tmpDownID;
                    this.mHandler.sendMessage(msg8);
                } else {
                    Message msg9 = new Message();
                    msg9.what = 100;
                    msg9.obj = "当前地图中图层【" + tmpLyrName + "】(" + tmpCreateUser + ")不存在.";
                    this.mHandler.sendMessage(msg9);
                }
            }
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

    private boolean SendMixContentToServer(String content, byte[] fileData) {
        if (this.socket == null || !this.socket.isConnected() || this.socket.isOutputShutdown()) {
            return false;
        }
        try {
            byte[] myHeadBf = "XPB|".getBytes();
            byte[] tempMsgBs = content.getBytes();
            int tempLen = tempMsgBs.length;
            byte[] tempLenBs = Common.IntToByteArray(tempLen);
            int tempFileLen = 0;
            if (fileData != null) {
                tempFileLen = fileData.length;
            }
            byte[] tempFLenBs = Common.IntToByteArray(tempFileLen);
            int count = myHeadBf.length;
            byte[] outBuffer = new byte[(tempLenBs.length + count + tempFLenBs.length + tempLen + tempFileLen)];
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
            int count3 = tempFLenBs.length;
            int i3 = 0;
            while (i3 < count3) {
                outBuffer[tid] = tempFLenBs[i3];
                i3++;
                tid++;
            }
            int i4 = 0;
            while (i4 < tempLen) {
                outBuffer[tid] = tempMsgBs[i4];
                i4++;
                tid++;
            }
            int i5 = 0;
            while (i5 < tempFileLen) {
                int tid2 = tid + 1;
                outBuffer[tid] = fileData[i5];
                i5++;
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
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Server.DownloadFromServer_Dialog.7
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
                DownloadFromServer_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
