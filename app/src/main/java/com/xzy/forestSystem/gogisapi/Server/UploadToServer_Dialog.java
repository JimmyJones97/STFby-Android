package  com.xzy.forestSystem.gogisapi.Server;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import com.xzy.forestSystem.gogisapi.Geodatabase.StringEncodings;
import  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.gdal.ogr.ogrConstants;

public class UploadToServer_Dialog {
    private boolean IsConnectedServer;
    private boolean IsJustUploadGeo;
    private boolean IsUploadingData;
    private int SuccessUploadCount;
    private int TotalUploadCount;
    private String UploadToSptialLayerName;
    private List<GeoLayer> _AllLayersList;
    private XDialogTemplate _Dialog;
    private boolean _RecvDataFromServer;
    Handler mHandler;
    private ICallback m_Callback;
    private Button m_ShareUserBtn;
    private LinearLayout m_SpatialLayerList;
    Runnable networkTask;
    private ICallback pCallback;
    CustomeProgressDialog progressDialog;
    Runnable recvDataTask;
    private Socket socket;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    @SuppressLint("WrongConstant")
    public UploadToServer_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.socket = null;
        this.m_SpatialLayerList = null;
        this._AllLayersList = new ArrayList();
        this.m_ShareUserBtn = null;
        this.TotalUploadCount = 0;
        this.SuccessUploadCount = 0;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Server.UploadToServer_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("账号")) {
                    LoginSetting_Dialog tmpDialog = new LoginSetting_Dialog();
                    tmpDialog.SetCallback(UploadToServer_Dialog.this.pCallback);
                    tmpDialog.ShowDialog();
                } else if (command.equals("用户登入设置")) {
                    UploadToServer_Dialog.this._Dialog.findViewById(R.id.btn_LoginIn).setVisibility(0);
                } else if (command.equals("OnItemSelected")) {
                    if (Common.GetSpinnerValueOnID(UploadToServer_Dialog.this._Dialog, R.id.sp_ExportType).equals("新建图层")) {
                        UploadToServer_Dialog.this._Dialog.findViewById(R.id.et_NewLayerName).setVisibility(0);
                        UploadToServer_Dialog.this.m_SpatialLayerList.setVisibility(8);
                        return;
                    }
                    UploadToServer_Dialog.this._Dialog.findViewById(R.id.et_NewLayerName).setVisibility(8);
                    UploadToServer_Dialog.this.m_SpatialLayerList.setVisibility(0);
                } else if (command.equals("共享方式选择返回")) {
                    if (Common.GetSpinnerValueOnID(UploadToServer_Dialog.this._Dialog, R.id.sp_ShareType).equals("共享指定用户")) {
                        UploadToServer_Dialog.this._Dialog.findViewById(R.id.btn_ShareUser).setVisibility(0);
                    } else {
                        UploadToServer_Dialog.this._Dialog.findViewById(R.id.btn_ShareUser).setVisibility(8);
                    }
                } else if (command.equals("选择用户返回")) {
                    UploadToServer_Dialog.this.m_ShareUserBtn.setTag(String.valueOf(object));
                }
            }
        };
        this.networkTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Server.UploadToServer_Dialog.2
            @Override // java.lang.Runnable
            public void run() {
                Message msg = new Message();
                if (UploadToServer_Dialog.this.socket != null && UploadToServer_Dialog.this.socket.isConnected()) {
                    try {
                        UploadToServer_Dialog.this.socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        msg.what = 0;
                        msg.obj = e.getMessage();
                    }
                }
                try {
                    UploadToServer_Dialog.this.socket = new Socket(PubVar.HostIP, PubVar.HostPort);
                    UploadToServer_Dialog.this.socket.setSoTimeout(5000);
                    msg.what = 1;
                } catch (Exception e2) {
                    msg.what = 0;
                    msg.obj = e2.getMessage();
                }
                UploadToServer_Dialog.this.mHandler.sendMessage(msg);
            }
        };
        this.mHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Server.UploadToServer_Dialog.3
            @SuppressLint("HandlerLeak")
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                String[] tmpStrs02;
                super.handleMessage(msg);
                try {
                    if (msg.what == 0) {
                        Common.ShowToast("连接服务器错误!" + String.valueOf(msg.obj));
                    } else if (msg.what == 1) {
                        Common.ShowToast("连接服务器成功!");
                        if (!(UploadToServer_Dialog.this.socket == null || !UploadToServer_Dialog.this.socket.isConnected() || UploadToServer_Dialog.this.socket.isOutputShutdown())) {
                            UploadToServer_Dialog.this.SendContentToServer("PHONECONNECT|" + PubVar.HostUserName + "|" + PubVar.HostUserPwd);
                        }
                    } else if (msg.what == 100) {
                        Common.ShowToast(String.valueOf(msg.obj));
                    } else if (msg.what == 101) {
                        Common.ShowDialog(String.valueOf(msg.obj));
                    } else if (msg.what == 11) {
                        Common.ShowToast(String.valueOf(msg.obj));
                        UploadToServer_Dialog.this._Dialog.findViewById(R.id.btn_LoginIn).setVisibility(8);
                    } else if (msg.what == 2) {
                        Common.ShowToast(String.valueOf(msg.obj));
                    } else if (msg.what == 20) {
                        Common.ShowToast(String.valueOf(msg.obj));
                    } else if (msg.what == 30) {
                        ArrayList tempArray = new ArrayList();
                        if (msg.obj != null) {
                            String[] tmpStrs01 = String.valueOf(msg.obj).split(",");
                            if (tmpStrs01 != null) {
                                int length = tmpStrs01.length;
                                for (int i = 0; i < length; i++) {
                                    tempArray.add(tmpStrs01[i]);
                                }
                            }
                        } else {
                            Common.ShowToast("服务器空间数据库中没有任何图层.");
                        }
                        Common.SetSpinnerListData(UploadToServer_Dialog.this._Dialog, "请选择导出至图层:", tempArray, (int) R.id.sp_SpatialLayerList, (ICallback) null);
                    } else if (msg.what == 31) {
                        if (msg.obj != null) {
                            String tmpUsersInfo = String.valueOf(msg.obj);
                            UserList_Dialog tmpDialog = new UserList_Dialog();
                            tmpDialog.SetAllUsersInfo(tmpUsersInfo);
                            tmpDialog.SetAllowMultiSelect(true);
                            tmpDialog.SetCallback(UploadToServer_Dialog.this.pCallback);
                            tmpDialog.SetSelectUsers(String.valueOf(UploadToServer_Dialog.this.m_ShareUserBtn.getTag()));
                            tmpDialog.ShowDialog();
                        }
                    } else if (msg.what == 40) {
                        String tmpLocalLayerName = Common.GetSpinnerValueOnID(UploadToServer_Dialog.this._Dialog, R.id.sp_localLayerList);
                        String tmpLayerID = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerIDByName(tmpLocalLayerName);
                        final FeatureLayer tmpLayer = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(tmpLayerID);
                        if (tmpLayer != null) {
                            final DataSet pDataset = PubVar.m_Workspace.GetDatasetByName(tmpLayerID);
                            String tmpSql = "Select COUNT(*) From " + pDataset.getDataTableName() + " AS A," + pDataset.getIndexTableName() + " As B Where A.SYS_ID = B.SYS_ID ";
                            String tmpConditionSQL = "";
                            String tmpCondition = Common.GetEditTextValueOnID(UploadToServer_Dialog.this._Dialog, R.id.et_condition);
                            if (tmpCondition != null && !tmpCondition.trim().equals("") && (tmpStrs02 = tmpCondition.toLowerCase().split("id")) != null && tmpStrs02.length > 0) {
                                StringBuilder tmpSB01 = new StringBuilder();
                                int length2 = tmpStrs02.length;
                                for (int i2 = 0; i2 < length2; i2++) {
                                    String tmpStr04 = tmpStrs02[i2];
                                    if (!tmpStr04.trim().equals("")) {
                                        tmpSB01.append(" A.SYS_ID ");
                                        tmpSB01.append(tmpStr04);
                                    }
                                }
                                if (tmpSB01.length() > 0) {
                                    tmpConditionSQL = " And " + tmpSB01.toString();
                                }
                            }
                            UploadToServer_Dialog.this.TotalUploadCount = 0;
                            SQLiteReader tmpReader01 = pDataset.getDataSource().Query(String.valueOf(tmpSql) + tmpConditionSQL);
                            if (tmpReader01 != null && tmpReader01.Read()) {
                                UploadToServer_Dialog.this.TotalUploadCount = tmpReader01.GetInt32(0);
                            }
                            if (UploadToServer_Dialog.this.TotalUploadCount == 0) {
                                Common.ShowDialog("当前图层中没有符合条件的导入数据,请检查图层数据或筛选条件.");
                                return;
                            }
                            UploadToServer_Dialog.this.progressDialog = CustomeProgressDialog.createDialog(UploadToServer_Dialog.this._Dialog.getContext());
                            UploadToServer_Dialog.this.progressDialog.SetHeadTitle("上传至云中心");
                            UploadToServer_Dialog.this.progressDialog.SetProgressTitle("上传图层【" + tmpLocalLayerName + "】数据:");
                            UploadToServer_Dialog.this.progressDialog.SetProgressInfo("准备上传数据...");
                            UploadToServer_Dialog.this.progressDialog.show();
                            String finalTmpConditionSQL = tmpConditionSQL;
                            new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Server.UploadToServer_Dialog.3.1
                                @Override // java.lang.Thread, java.lang.Runnable
                                public void run() {
                                    Handler tmpHandler = UploadToServer_Dialog.this.progressDialog.myHandler;
                                    UploadToServer_Dialog.this.IsUploadingData = true;
                                    UploadToServer_Dialog.this.SuccessUploadCount = 0;
                                    int tmpFidCount = 0;
                                    try {
                                        StringBuilder tmpFIDSB = new StringBuilder();
                                        StringBuilder tmpFIDSB2 = new StringBuilder();
                                        if (!UploadToServer_Dialog.this.IsJustUploadGeo) {
                                            for (LayerField layerField : tmpLayer.GetFieldList()) {
                                                tmpFIDSB.append(",");
                                                tmpFidCount++;
                                                String tmpFIDName = layerField.GetDataFieldName();
                                                tmpFIDSB.append("A." + tmpFIDName);
                                                if (tmpFIDSB2.length() > 0) {
                                                    tmpFIDSB2.append(",");
                                                }
                                                tmpFIDSB2.append(tmpFIDName);
                                            }
                                        }
                                        String contentHead = "PHONECOMMAND|" + PubVar.HostUserName + "|UploadData|" + UploadToServer_Dialog.this.UploadToSptialLayerName + "|";
                                        SQLiteReader tmpReader = pDataset.getDataSource().Query("Select A.SYS_ID,B.MinX,B.MinY,B.MaxX,B.MaxY,B.RIndex,B.CIndex,A.SYS_GEO" + tmpFIDSB.toString() + " From " + pDataset.getDataTableName() + " AS A," + pDataset.getIndexTableName() + " As B Where A.SYS_ID = B.SYS_ID " + finalTmpConditionSQL);
                                        if (tmpReader != null) {
                                            int tid = 0;
                                            while (true) {
                                                if (!(tmpReader.Read() && UploadToServer_Dialog.this.IsUploadingData)) {
                                                    break;
                                                }
                                                try {
                                                    int tmpSysID = tmpReader.GetInt32(0);
                                                    tid++;
                                                    Message msg2 = tmpHandler.obtainMessage();
                                                    msg2.what = 5;
                                                    msg2.obj = new Object[]{"上传数据 ID：" + tmpSysID + "  [" + tid + FileSelector_Dialog.sRoot + UploadToServer_Dialog.this.TotalUploadCount + "]", Integer.valueOf((tid * 100) / UploadToServer_Dialog.this.TotalUploadCount)};
                                                    tmpHandler.sendMessage(msg2);
                                                    Object[] tmpObjs = new Object[(tmpFidCount + 7)];
                                                    int tmpTid = 0 + 1;
                                                    tmpObjs[0] = Integer.valueOf(tmpReader.GetInt32(0));
                                                    int tmpTid2 = tmpTid + 1;
                                                    tmpObjs[tmpTid] = Double.valueOf(tmpReader.GetDouble(1));
                                                    int tmpTid3 = tmpTid2 + 1;
                                                    tmpObjs[tmpTid2] = Double.valueOf(tmpReader.GetDouble(2));
                                                    int tmpTid4 = tmpTid3 + 1;
                                                    tmpObjs[tmpTid3] = Double.valueOf(tmpReader.GetDouble(3));
                                                    int tmpTid5 = tmpTid4 + 1;
                                                    tmpObjs[tmpTid4] = Double.valueOf(tmpReader.GetDouble(4));
                                                    int tmpTid6 = tmpTid5 + 1;
                                                    tmpObjs[tmpTid5] = Integer.valueOf(tmpReader.GetInt32(5));
                                                    int tmpTid7 = tmpTid6 + 1;
                                                    tmpObjs[tmpTid6] = Integer.valueOf(tmpReader.GetInt32(6));
                                                    byte[] tmpGeoData = tmpReader.GetBlob(7);
                                                    int i3 = 0;
                                                    while (i3 < tmpFidCount) {
                                                        int tmpTid8 = tmpTid7 + 1;
                                                        tmpObjs[tmpTid7] = tmpReader.GetString(i3 + 8);
                                                        i3++;
                                                        tmpTid7 = tmpTid8;
                                                    }
                                                    UploadToServer_Dialog.this.SendMixContentToServer(String.valueOf(contentHead) + String.valueOf(tmpSysID) + "|" + tmpFIDSB2.toString() + "|" + Common.CombineStrings("|", tmpObjs), tmpGeoData);
                                                    if (UploadToServer_Dialog.this.progressDialog.isCancel) {
                                                        break;
                                                    }
                                                } catch (Exception e) {
                                                }
                                            }
                                            tmpReader.Close();
                                        }
                                    } catch (Exception e2) {
                                    }
                                    UploadToServer_Dialog.this.IsUploadingData = false;
                                    Message msg3 = new Message();
                                    msg3.what = ogrConstants.wkbLinearRing;
                                    msg3.obj = "上传数据结束.\n共上传数据" + String.valueOf(UploadToServer_Dialog.this.TotalUploadCount) + "个.";
                                    UploadToServer_Dialog.this.mHandler.sendMessage(msg3);
                                    UploadToServer_Dialog.this.SendCommandToServer("PHONECOMMAND|" + PubVar.HostUserName + "|updatelayerinfo|" + UploadToServer_Dialog.this.UploadToSptialLayerName);
                                    UploadToServer_Dialog.this.progressDialog.dismiss();
                                    UploadToServer_Dialog.this.progressDialog = null;
                                }
                            }.start();
                        }
                    } else if (msg.what == 41) {
                        Common.ShowYesNoDialog(UploadToServer_Dialog.this._Dialog.getContext(), "两个图层结构进行对比时:\n    " + String.valueOf(msg.obj) + "\n是否继续?继续将只上传几何数据.", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Server.UploadToServer_Dialog.3.2
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String command, Object pObject) {
                                if (command.equals("YES")) {
                                    UploadToServer_Dialog.this.IsJustUploadGeo = true;
                                    Message msg2 = new Message();
                                    msg2.what = 40;
                                    UploadToServer_Dialog.this.mHandler.sendMessage(msg2);
                                }
                            }
                        });
                    }
                } catch (Exception e) {
                }
            }
        };
        this.progressDialog = null;
        this.UploadToSptialLayerName = "";
        this.IsJustUploadGeo = false;
        this._RecvDataFromServer = false;
        this.recvDataTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Server.UploadToServer_Dialog.4
            @Override // java.lang.Runnable
            public void run() {
                int tmpHeadLen;
                while (UploadToServer_Dialog.this._RecvDataFromServer) {
                    try {
                        if (UploadToServer_Dialog.this.socket != null && !UploadToServer_Dialog.this.socket.isClosed() && UploadToServer_Dialog.this.socket.isConnected() && !UploadToServer_Dialog.this.socket.isInputShutdown()) {
                            InputStream inputStream = UploadToServer_Dialog.this.socket.getInputStream();
                            byte[] tmpHeadBytes = new byte[4];
                            if (inputStream.read(tmpHeadBytes) > 0 && (tmpHeadLen = Common.BytesToInt(tmpHeadBytes)) > 0) {
                                byte[] buffer = new byte[tmpHeadLen];
                                if (inputStream.read(buffer) > 0) {
                                    String[] tmpStrs01 = new String(buffer, StringEncodings.UTF8).split("\\|");
                                    if (tmpStrs01 == null || tmpStrs01.length <= 0) {
                                        Message msg = new Message();
                                        msg.what = 20;
                                        msg.obj = "非法数据请求.";
                                        UploadToServer_Dialog.this.mHandler.sendMessage(msg);
                                    } else {
                                        String tmpHead = tmpStrs01[0];
                                        if (tmpHead.equals("XPS")) {
                                            UploadToServer_Dialog.this.ProcessCommand(tmpStrs01, null);
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
                                                UploadToServer_Dialog.this.ProcessCommand(tmpStrs01, tmpFileData);
                                            }
                                        } else {
                                            Message msg2 = new Message();
                                            msg2.what = 20;
                                            msg2.obj = "非法数据请求.";
                                            UploadToServer_Dialog.this.mHandler.sendMessage(msg2);
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
        this.IsUploadingData = false;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.uploadtoserver_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("上传云中心");
        this._Dialog.SetHeadButtons("1,2130837784,账号,账号", this.pCallback);
        ((Button) this._Dialog.findViewById(R.id.btn_RefreshLayers)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_LoginIn)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_OK)).setOnClickListener(new ViewClick());
        this.m_ShareUserBtn = (Button) this._Dialog.findViewById(R.id.btn_ShareUser);
        this.m_ShareUserBtn.setVisibility(8);
        this.m_ShareUserBtn.setTag("");
        this.m_ShareUserBtn.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Server.UploadToServer_Dialog.5
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (UploadToServer_Dialog.this.IsConnectedServer) {
                    UploadToServer_Dialog.this.SendCommandToServer("PHONECOMMAND|" + PubVar.HostUserName + "|getuserslist");
                    return;
                }
                Common.ShowDialog("没有登入服务器,请先登入服务器后再进行此操作.");
            }
        });
        this.m_SpatialLayerList = (LinearLayout) this._Dialog.findViewById(R.id.ll_SpatialLayerList);
        this._RecvDataFromServer = true;
        new Thread(this.recvDataTask).start();
        DoCommand("登入服务器");
        ArrayList tempArray = new ArrayList();
        tempArray.add("新建图层");
        tempArray.add("已有图层");
        Common.SetSpinnerListData(this._Dialog, "请选择导出至图层:", tempArray, (int) R.id.sp_ExportType, this.pCallback);
        Common.SetValueToView("新建图层", this._Dialog.findViewById(R.id.sp_ExportType));
        this.m_SpatialLayerList.setVisibility(8);
        ArrayList tempArray02 = new ArrayList();
        tempArray02.add("不共享");
        tempArray02.add("共享给所有用户");
        tempArray02.add("共享指定用户");
        Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_ShareType, "不共享", "请选择空间数据共享方式:", tempArray02, "共享方式选择返回", this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLocalLayers() {
        try {
            if (this._AllLayersList.size() == 0) {
                Common.ShowDialog("当前地图中没有可上传的矢量数据图层.");
                this._Dialog.dismiss();
                return;
            }
            ArrayList tempArray = new ArrayList();
            for (GeoLayer tmpXLayer : this._AllLayersList) {
                tempArray.add(tmpXLayer.getLayerName());
            }
            Common.SetSpinnerListData(this._Dialog, "请选择导出的图层:", tempArray, (int) R.id.sp_localLayerList, (ICallback) null);
            Common.SetValueToView(String.valueOf(tempArray.get(0)), this._Dialog.findViewById(R.id.sp_localLayerList));
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        String[] tmpStrs02;
        try {
            if (command.equals("登入服务器")) {
                new Thread(this.networkTask).start();
            } else if (command.equals("刷新空间数据库图层")) {
                if (this.IsConnectedServer) {
                    SendCommandToServer("PHONECOMMAND|" + PubVar.HostUserName + "|getlayerlist");
                } else {
                    Common.ShowDialog("没有登入服务器,请先登入服务器后再进行此操作.");
                }
            } else if (command.equals("上传数据")) {
                this.IsJustUploadGeo = false;
                if (this.IsConnectedServer) {
                    String tmpLocalLayerName = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_localLayerList);
                    String tmpLayerID = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerIDByName(tmpLocalLayerName);
                    if (PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(tmpLayerID) != null) {
                        DataSet pDataset = PubVar.m_Workspace.GetDatasetByName(tmpLayerID);
                        String tmpSql = "Select COUNT(*) From " + pDataset.getDataTableName() + " AS A," + pDataset.getIndexTableName() + " As B Where A.SYS_ID = B.SYS_ID ";
                        String tmpConditionSQL = "";
                        String tmpCondition = Common.GetEditTextValueOnID(this._Dialog, R.id.et_condition);
                        if (tmpCondition != null && !tmpCondition.trim().equals("") && (tmpStrs02 = tmpCondition.toLowerCase().split("id")) != null && tmpStrs02.length > 0) {
                            StringBuilder tmpSB01 = new StringBuilder();
                            int length = tmpStrs02.length;
                            for (int i = 0; i < length; i++) {
                                String tmpStr04 = tmpStrs02[i];
                                if (!tmpStr04.trim().equals("")) {
                                    tmpSB01.append(" A.SYS_ID ");
                                    tmpSB01.append(tmpStr04);
                                }
                            }
                            if (tmpSB01.length() > 0) {
                                tmpConditionSQL = " And " + tmpSB01.toString();
                            }
                        }
                        this.TotalUploadCount = 0;
                        SQLiteReader tmpReader01 = pDataset.getDataSource().Query(String.valueOf(tmpSql) + tmpConditionSQL);
                        if (tmpReader01 != null && tmpReader01.Read()) {
                            this.TotalUploadCount = tmpReader01.GetInt32(0);
                        }
                        if (this.TotalUploadCount == 0) {
                            Common.ShowDialog("当前图层中没有符合条件的导入数据,请检查图层数据或筛选条件.");
                        } else if (Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_ExportType).equals("新建图层")) {
                            this.UploadToSptialLayerName = Common.GetEditTextValueOnID(this._Dialog, R.id.et_NewLayerName);
                            if (this.UploadToSptialLayerName == null || this.UploadToSptialLayerName.equals("")) {
                                Common.ShowDialog("没有输入在空间数据库中新建的图层名称.请输入图层名称后再进行此操作.");
                                return;
                            }
                            String tmpContent = String.valueOf("PHONECOMMAND|" + PubVar.HostUserName + "|CREATELAYER|") + this.UploadToSptialLayerName + "|";
                            FeatureLayer tmpLayer = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerIDByName(tmpLocalLayerName));
                            if (tmpLayer != null) {
                                String tmpContent2 = String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(String.valueOf(tmpContent) + tmpLayer.GetLayerTypeName() + "|") + tmpLayer.GetFieldListStr() + "|") + PubVar.m_Workspace.GetCoorSystem().GetName() + "|") + String.valueOf(tmpLayer.getMinX()) + "|") + String.valueOf(tmpLayer.getMinY()) + "|") + String.valueOf(tmpLayer.getMaxX()) + "|") + String.valueOf(tmpLayer.getMaxY()) + "|";
                                int tmpShareTypeID = 0;
                                String tmpShareUsers = "";
                                String tmpShareType = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_ShareType);
                                if (tmpShareType.equals("共享指定用户")) {
                                    tmpShareTypeID = 1;
                                    tmpShareUsers = String.valueOf(this.m_ShareUserBtn.getTag());
                                } else if (tmpShareType.equals("共享给所有用户")) {
                                    tmpShareTypeID = 2;
                                }
                                SendCommandToServer(String.valueOf(String.valueOf(tmpContent2) + String.valueOf(tmpShareTypeID) + "|") + tmpShareUsers + "|");
                                return;
                            }
                            Common.ShowDialog("当前地图中图层【" + tmpLocalLayerName + "】无效.");
                        } else {
                            this.UploadToSptialLayerName = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_SpatialLayerList);
                            if (this.UploadToSptialLayerName == null || this.UploadToSptialLayerName.equals("")) {
                                Common.ShowDialog("没有选择添加至空间数据库中的图层名称.请选择图层后再进行此操作.");
                                return;
                            }
                            String tmpContent3 = String.valueOf("PHONECOMMAND|" + PubVar.HostUserName + "|CHECK2LAYER|") + this.UploadToSptialLayerName + "|";
                            FeatureLayer tmpLayer2 = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerIDByName(tmpLocalLayerName));
                            if (tmpLayer2 != null) {
                                SendCommandToServer(String.valueOf(String.valueOf(String.valueOf(tmpContent3) + tmpLayer2.GetLayerTypeName() + "|") + tmpLayer2.GetFieldListStr() + "|") + PubVar.m_Workspace.GetCoorSystem().GetName());
                            } else {
                                Common.ShowDialog("当前地图中图层【" + tmpLocalLayerName + "】无效.");
                            }
                        }
                    } else {
                        Common.ShowDialog("当前图层无效.");
                    }
                } else {
                    Common.ShowDialog("没有登入服务器,请先登入服务器后再进行此操作.");
                }
            } else {
                command.equals("共享指定用户");
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void SendCommandToServer(String command) {
        if (this.socket != null && this.socket.isConnected() && !this.socket.isOutputShutdown()) {
            SendContentToServer(command);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void ProcessCommand(String[] HeadString, byte[] relateData) {
        try {
            int tmpI01 = 1;
            if (HeadString[0].equals("XPB")) {
                tmpI01 = 2;
            }
            String tmpReturnCmd = HeadString[tmpI01];
            if (tmpReturnCmd.equals("CONNECTED")) {
                if (HeadString[tmpI01 + 1].trim().toLowerCase().equals("true")) {
                    this.IsConnectedServer = true;
                    Message msg = new Message();
                    msg.what = 11;
                    msg.obj = "登入服务器成功.";
                    this.mHandler.sendMessage(msg);
                    return;
                }
                this.IsConnectedServer = false;
                Message msg2 = new Message();
                msg2.what = 2;
                String tmpStr02 = "登入服务器失败.";
                if (HeadString.length > tmpI01 + 2) {
                    tmpStr02 = String.valueOf(tmpStr02) + HeadString[tmpI01 + 2];
                }
                msg2.obj = tmpStr02;
                this.mHandler.sendMessage(msg2);
            } else if (tmpReturnCmd.equals("GETUSERSLIST")) {
                if (HeadString[tmpI01 + 1].trim().toLowerCase().equals("true")) {
                    Message msg3 = new Message();
                    msg3.what = 31;
                    msg3.obj = HeadString[tmpI01 + 2];
                    this.mHandler.sendMessage(msg3);
                    return;
                }
                Message msg4 = new Message();
                msg4.what = 100;
                msg4.obj = HeadString[tmpI01 + 2];
                this.mHandler.sendMessage(msg4);
            } else if (tmpReturnCmd.equals("GETLAYERLIST")) {
                if (HeadString[tmpI01 + 1].trim().toLowerCase().equals("true")) {
                    String tmpStr022 = HeadString[tmpI01 + 2].trim();
                    Message msg5 = new Message();
                    msg5.what = 30;
                    msg5.obj = tmpStr022;
                    this.mHandler.sendMessage(msg5);
                    return;
                }
                Message msg6 = new Message();
                msg6.what = 30;
                msg6.obj = null;
                this.mHandler.sendMessage(msg6);
            } else if (tmpReturnCmd.equals("CREATELAYER")) {
                if (HeadString[tmpI01 + 1].trim().toLowerCase().equals("true")) {
                    Message msg7 = new Message();
                    msg7.what = 40;
                    this.mHandler.sendMessage(msg7);
                    return;
                }
                Message msg8 = new Message();
                msg8.what = ogrConstants.wkbLinearRing;
                msg8.obj = HeadString[tmpI01 + 2];
                this.mHandler.sendMessage(msg8);
            } else if (tmpReturnCmd.equals("CHECK2LAYER")) {
                if (HeadString[tmpI01 + 1].trim().toLowerCase().equals("true")) {
                    Message msg9 = new Message();
                    msg9.what = 40;
                    this.mHandler.sendMessage(msg9);
                } else if (HeadString[tmpI01 + 2].trim().toLowerCase().equals("true")) {
                    Message msg10 = new Message();
                    msg10.what = 41;
                    msg10.obj = HeadString[tmpI01 + 3];
                    this.mHandler.sendMessage(msg10);
                } else {
                    Message msg11 = new Message();
                    msg11.what = ogrConstants.wkbLinearRing;
                    msg11.obj = HeadString[tmpI01 + 3];
                    this.mHandler.sendMessage(msg11);
                }
            } else if (tmpReturnCmd.equals("UPLOADDATA")) {
                String str = HeadString[tmpI01 + 1];
                if (HeadString[tmpI01 + 2].trim().toLowerCase().equals("true")) {
                    this.SuccessUploadCount++;
                } else if (HeadString[tmpI01 + 3].trim().toLowerCase().equals("true")) {
                    Message msg12 = new Message();
                    msg12.what = 100;
                    msg12.obj = HeadString[tmpI01 + 4];
                    this.mHandler.sendMessage(msg12);
                } else {
                    this.IsUploadingData = false;
                    Message msg13 = new Message();
                    msg13.what = ogrConstants.wkbLinearRing;
                    msg13.obj = HeadString[tmpI01 + 4];
                    this.mHandler.sendMessage(msg13);
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

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
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
        this._AllLayersList = PubVar._Map.getGeoLayers().getList();
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Server.UploadToServer_Dialog.6
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                UploadToServer_Dialog.this.refreshLocalLayers();
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
                UploadToServer_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
