package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataExportOfSel_Dialog {
    private String _DataSourceName;
    private XDialogTemplate _Dialog;
    private int _ExportDataCount;
    private String _ExportDataSYSIDs;
    private String _LayerID;
    private String _LayerName;
    public Handler _myHandler;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public DataExportOfSel_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this._myHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataExportOfSel_Dialog.1
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Common.ShowDialog(msg.obj.toString());
                    DataExportOfSel_Dialog.this._Dialog.dismiss();
                }
            }
        };
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataExportOfSel_Dialog.2
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                final String tmpDatabaseKey;
                final FeatureLayer m_SelectLayer;
                if (!paramString.equals("导出数据")) {
                    return;
                }
                if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                    Common.ShowDialog("尊敬的用户：\r\n        【公共版】不能导出数据.为保证您能使用本软件的全部功能，请获取正式授权码！\r\n详见【关于系统】！");
                } else if (DataExportOfSel_Dialog.this._ExportDataCount <= 0) {
                    Common.ShowDialog("可以导出的数据数目少于1个.");
                } else {
                    String tempFolder = Common.GetEditTextValueOnID(DataExportOfSel_Dialog.this._Dialog, R.id.et_outputFolder);
                    if (tempFolder.equals("")) {
                        Common.ShowDialog("导出目录不能为空.");
                        Common.SetEditTextValueOnID(DataExportOfSel_Dialog.this._Dialog, R.id.et_outputFolder, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
                        return;
                    }
                    DataSource tmpDataSource = PubVar.m_Workspace.GetDataSourceByName(DataExportOfSel_Dialog.this._DataSourceName);
                    if (tmpDataSource == null) {
                        Common.ShowDialog("项目中数据源存在错误.\r\n" + DataExportOfSel_Dialog.this._DataSourceName);
                    } else if (!tmpDataSource.IsEnable()) {
                        Common.ShowDialog("项目中数据源不可用.\r\n" + DataExportOfSel_Dialog.this._DataSourceName);
                    } else if (!tmpDataSource.IsAllowWrite()) {
                        Common.ShowDialog("该数据未授权在本设备上使用.\r\n" + DataExportOfSel_Dialog.this._DataSourceName);
                    } else {
                        if (tmpDataSource.getEditing()) {
                            tmpDatabaseKey = PubVar.m_Workspace.getProjectPassword();
                        } else {
                            tmpDatabaseKey = tmpDataSource.getDatabaseKey();
                        }
                        String tempTotalFolder = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/数据导出";
                        if (!Common.ExistFolder(tempTotalFolder)) {
                            new File(tempTotalFolder).mkdir();
                        }
                        final String tempTotalSaveFolder = String.valueOf(tempTotalFolder) + FileSelector_Dialog.sRoot + tempFolder;
                        if (!Common.ExistFolder(tempTotalSaveFolder)) {
                            new File(tempTotalSaveFolder).mkdir();
                        }
                        String tempType = Common.GetSpinnerValueOnID(DataExportOfSel_Dialog.this._Dialog, R.id.sp_outputType);
                        if (tempType != null && (m_SelectLayer = PubVar._PubCommand.m_ProjectDB.GetLayerInDataSource(DataExportOfSel_Dialog.this._DataSourceName, DataExportOfSel_Dialog.this._LayerID)) != null) {
                            if (tempType.equals("Excel(CSV)格式")) {
                                if (m_SelectLayer != null) {
                                    Common.ShowProgressDialog("正在导出数据,请稍候...", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataExportOfSel_Dialog.2.1
                                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                        public void OnClick(String paramString2, Object pObject2) {
                                            Throwable th;
                                            String[] tmpStrs;
                                            List<String> tmpSYSIDList = null;
                                            try {
                                                if (DataExportOfSel_Dialog.this._ExportDataSYSIDs != null && DataExportOfSel_Dialog.this._ExportDataSYSIDs.length() > 0 && (tmpStrs = DataExportOfSel_Dialog.this._ExportDataSYSIDs.split(",")) != null && tmpStrs.length > 0) {
                                                    List<String> tmpSYSIDList2 = new ArrayList<>();
                                                    try {
                                                        for (String tmpString : tmpStrs) {
                                                            if (tmpString.length() > 0) {
                                                                tmpSYSIDList2.add(tmpString);
                                                            }
                                                        }
                                                        tmpSYSIDList = tmpSYSIDList2;
                                                    } catch (Exception e) {
                                                        if (pObject2 != null) {
                                                            return;
                                                        }
                                                    } catch (Throwable th2) {
                                                        th = th2;
                                                        Handler tmpHandler = (Handler) pObject2;
                                                        Message tmpMsg = tmpHandler.obtainMessage();
                                                        tmpMsg.what = 0;
                                                        tmpHandler.sendMessage(tmpMsg);
                                                        throw th;
                                                    }
                                                }
                                                Common.CombineStrings(",", tmpSYSIDList);
                                                String tempSavePath = String.valueOf(tempTotalSaveFolder) + FileSelector_Dialog.sRoot + m_SelectLayer.GetLayerName() + ".csv";
                                                if (new ExportCSV(tempSavePath, DataExportOfSel_Dialog.this._DataSourceName, String.valueOf(m_SelectLayer.GetLayerID()) + "_D", "SYS_ID", DataExportOfSel_Dialog.this._ExportDataSYSIDs, m_SelectLayer.GetDataFieldsString(), m_SelectLayer.GetFieldNamesString(), tmpDatabaseKey).Export()) {
                                                    Common.ShowDialog("数据导出成功!\r\n存储在:" + tempSavePath);
                                                    DataExportOfSel_Dialog.this._Dialog.dismiss();
                                                } else {
                                                    Common.ShowDialog("导出数据错误!");
                                                }
                                                if (pObject2 != null && (pObject2 instanceof Handler)) {
                                                    Handler tmpHandler2 = (Handler) pObject2;
                                                    Message tmpMsg2 = tmpHandler2.obtainMessage();
                                                    tmpMsg2.what = 0;
                                                    tmpHandler2.sendMessage(tmpMsg2);
                                                }
                                            } catch (Exception e2) {
                                                if (pObject2 != null && (pObject2 instanceof Handler)) {
                                                    Handler tmpHandler3 = (Handler) pObject2;
                                                    Message tmpMsg3 = tmpHandler3.obtainMessage();
                                                    tmpMsg3.what = 0;
                                                    tmpHandler3.sendMessage(tmpMsg3);
                                                }
                                            } catch (Throwable th3) {
                                                th = th3;
                                                if (pObject2 != null && (pObject2 instanceof Handler)) {
                                                    Handler tmpHandler = (Handler) pObject2;
                                                    Message tmpMsg = tmpHandler.obtainMessage();
                                                    tmpMsg.what = 0;
                                                    tmpHandler.sendMessage(tmpMsg);
                                                }
                                            }
                                        }
                                    });
                                } else {
                                    Common.ShowDialog("图层对象在地图中不存在!");
                                }
                            } else if (tempType.equals("ArcGIS(Shp)格式") && m_SelectLayer != null) {
                                final GeoLayer pGeoLayer = PubVar._Map.GetGeoLayerByDataSource(m_SelectLayer.GetDataSourceName(), m_SelectLayer.GetLayerID());
                                if (pGeoLayer == null || pGeoLayer.getDataset() == null) {
                                    Common.ShowDialog("图层【" + m_SelectLayer.GetLayerName() + "】导出数据时错误!");
                                    return;
                                }
                                final CustomeProgressDialog progressDialog = CustomeProgressDialog.createDialog(DataExportOfSel_Dialog.this._Dialog.getContext());
                                progressDialog.SetHeadTitle("数据导出");
                                progressDialog.SetProgressTitle("开始导出ArcGIS(Shp)格式文件:");
                                progressDialog.SetProgressInfo("准备导出数据...");
                                progressDialog.show();
                                new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataExportOfSel_Dialog.2.2
                                    @Override // java.lang.Thread, java.lang.Runnable
                                    public void run() {
                                        String[] tmpStrs;
                                        String tempSavePath = String.valueOf(tempTotalSaveFolder) + FileSelector_Dialog.sRoot + m_SelectLayer.GetLayerName();
                                        DataSet pDataset = pGeoLayer.getDataset();
                                        if (pGeoLayer.getDataset() != null) {
                                            Handler myHandler = progressDialog.myHandler;
                                            Message msg = myHandler.obtainMessage();
                                            msg.what = 1;
                                            msg.obj = "正在导出图层【" + pDataset.getLayerName() + "】的数据:";
                                            myHandler.sendMessage(msg);
                                            List<String> tmpSYSIDList = null;
                                            if (DataExportOfSel_Dialog.this._ExportDataSYSIDs != null && DataExportOfSel_Dialog.this._ExportDataSYSIDs.length() > 0 && (tmpStrs = DataExportOfSel_Dialog.this._ExportDataSYSIDs.split(",")) != null && tmpStrs.length > 0) {
                                                tmpSYSIDList = new ArrayList<>();
                                                for (String tmpString : tmpStrs) {
                                                    if (tmpString.length() > 0) {
                                                        tmpSYSIDList.add(tmpString);
                                                    }
                                                }
                                            }
                                            ExportToShp tempExport = new ExportToShp();
                                            tempExport.setBindProgressDialog(progressDialog);
                                            tempExport.setExportCoordType(PubVar.m_Workspace.GetCoorSystem().GetName());
                                            if (tempExport.Export(pDataset, tempSavePath, tmpSYSIDList)) {
                                                Message msg2 = DataExportOfSel_Dialog.this._myHandler.obtainMessage();
                                                msg2.what = 1;
                                                msg2.obj = "数据导出成功!\r\n存储在:" + tempSavePath;
                                                DataExportOfSel_Dialog.this._myHandler.sendMessage(msg2);
                                            } else {
                                                Message msg22 = DataExportOfSel_Dialog.this._myHandler.obtainMessage();
                                                msg22.what = 1;
                                                msg22.obj = "数据导出失败!";
                                                DataExportOfSel_Dialog.this._myHandler.sendMessage(msg22);
                                            }
                                        }
                                        progressDialog.dismiss();
                                    }
                                }.start();
                            }
                        }
                    }
                }
            }
        };
        this._ExportDataSYSIDs = "";
        this._ExportDataCount = 0;
        this._LayerID = "";
        this._LayerName = "";
        this._DataSourceName = "";
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.data_exportofsel_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("数据导出");
        this._Dialog.SetHeadButtons("1,2130837673,导出,导出数据", this.pCallback);
        ArrayList tempArray = new ArrayList();
        tempArray.add("Excel(CSV)格式");
        tempArray.add("ArcGIS(Shp)格式");
        Common.SetSpinnerListData(this._Dialog, "请选择导出格式:", tempArray, (int) R.id.sp_outputType);
        Common.SetValueToView("Excel(CSV)格式", this._Dialog.findViewById(R.id.sp_outputType));
    }

    public void SetExportDataSYSID(String value) {
        this._ExportDataSYSIDs = value;
        String[] tempStrs = value.split(",");
        if (tempStrs != null) {
            this._ExportDataCount = tempStrs.length;
        } else {
            this._ExportDataCount = 0;
        }
    }

    public void SetLayerID(String layerID, String dataSourceName) {
        this._LayerID = layerID;
        this._DataSourceName = dataSourceName;
        FeatureLayer m_SelectLayer = PubVar._PubCommand.m_ProjectDB.GetLayerInDataSource(dataSourceName, layerID);
        if (m_SelectLayer != null) {
            this._Dialog.SetCaption("数据导出");
            this._LayerName = m_SelectLayer.GetLayerName();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_outputDataInfo, "导出图层:" + this._LayerName + "\r\n可导出实体数目:" + this._ExportDataCount);
        Common.SetEditTextValueOnID(this._Dialog, R.id.et_outputFolder, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataExportOfSel_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                DataExportOfSel_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
