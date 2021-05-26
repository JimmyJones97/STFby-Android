package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.ELayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.ProjectionCoordinateSystem;
import  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DataExport_Dialog {
    private List<GeoLayer> _AllLayersList;
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private String m_ExportFolder;
    private LinearLayout m_LLSystemType;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private List<String> m_Sellayers;
    public Handler myCallHandler;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    @SuppressLint("WrongConstant")
    public DataExport_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this._AllLayersList = new ArrayList();
        this.m_LLSystemType = null;
        this.m_Sellayers = null;
        this.m_ExportFolder = "";
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataExport_Dialog.1
            @SuppressLint("WrongConstant")
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                try {
                    if (paramString.equals("OnItemSelected")) {
                        String tempType = Common.GetSpinnerValueOnID(DataExport_Dialog.this._Dialog, R.id.sp_outputType);
                        if (tempType.equals("Excel(CSV)格式")) {
                            DataExport_Dialog.this.m_LLSystemType.setVisibility(8);
                            DataExport_Dialog.this._Dialog.findViewById(R.id.ll_outputShpCharType).setVisibility(8);
                        } else if (tempType.equals("ArcGIS(Shp)格式")) {
                            DataExport_Dialog.this.m_LLSystemType.setVisibility(0);
                            DataExport_Dialog.this._Dialog.findViewById(R.id.ll_outputShpCharType).setVisibility(0);
                        } else {
                            DataExport_Dialog.this._Dialog.findViewById(R.id.ll_outputShpCharType).setVisibility(8);
                        }
                    } else if (!paramString.equals("导出数据")) {
                        paramString.equals("导出数据完成");
                    } else if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                        Common.ShowDialog("尊敬的用户：\r\n        【公共版】不能导出数据.为保证您能使用本软件的全部功能，请获取正式授权码！\r\n详见【关于系统】！");
                    } else {
                        DataExport_Dialog.this.m_Sellayers = new ArrayList();
                        if (DataExport_Dialog.this.m_MyTableDataList != null) {
                            for (HashMap<String, Object> tempHash : DataExport_Dialog.this.m_MyTableDataList) {
                                if (Boolean.parseBoolean(tempHash.get("D1").toString())) {
                                    DataExport_Dialog.this.m_Sellayers.add(tempHash.get("D5").toString());
                                }
                            }
                        }
                        if (DataExport_Dialog.this.m_Sellayers.size() == 0) {
                            Common.ShowDialog("请选择导出的图层.");
                            return;
                        }
                        String tempFolder = Common.GetEditTextValueOnID(DataExport_Dialog.this._Dialog, R.id.et_outputFolder);
                        if (tempFolder.equals("")) {
                            Common.ShowDialog("导出目录不能为空.");
                            Common.SetEditTextValueOnID(DataExport_Dialog.this._Dialog, R.id.et_outputFolder, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
                            return;
                        }
                        String tempTotalFolder = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/数据导出";
                        if (!Common.ExistFolder(tempTotalFolder)) {
                            new File(tempTotalFolder).mkdir();
                        }
                        DataExport_Dialog.this.m_ExportFolder = String.valueOf(tempTotalFolder) + FileSelector_Dialog.sRoot + tempFolder;
                        if (!Common.ExistFolder(DataExport_Dialog.this.m_ExportFolder)) {
                            new File(DataExport_Dialog.this.m_ExportFolder).mkdir();
                        }
                        final String tempOutputType = Common.GetSpinnerValueOnID(DataExport_Dialog.this._Dialog, R.id.sp_outputType);
                        if (tempOutputType != null) {
                            final CustomeProgressDialog progressDialog = CustomeProgressDialog.createDialog(DataExport_Dialog.this._Dialog.getContext());
                            progressDialog.SetHeadTitle("数据导出");
                            progressDialog.SetProgressTitle("导出图层数据  [0/" + DataExport_Dialog.this.m_Sellayers.size() + "]:");
                            progressDialog.SetProgressInfo("准备导出数据...");
                            progressDialog.show();
                            new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataExport_Dialog.1.1
                                @Override // java.lang.Thread, java.lang.Runnable
                                public void run() {
                                    StringBuilder tmpOutMsg = new StringBuilder();
                                    if (tempOutputType.equals("Excel(CSV)格式")) {
                                        Handler myHandler = progressDialog.myHandler;
                                        int tid = 0;
                                        for (String tmpLayerID : DataExport_Dialog.this.m_Sellayers) {
                                            tid++;
                                            Message msg = myHandler.obtainMessage();
                                            msg.what = 1;
                                            msg.obj = new Object[]{"导出图层数据  [" + tid + FileSelector_Dialog.sRoot + DataExport_Dialog.this.m_Sellayers.size() + "]:"};
                                            myHandler.sendMessage(msg);
                                            FeatureLayer tmpLayer = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(tmpLayerID);
                                            if (tmpLayer != null) {
                                                Message msg2 = myHandler.obtainMessage();
                                                msg2.what = 5;
                                                msg2.obj = new Object[]{"开始导出图层【" + tmpLayer.GetLayerName() + "】数据...", 0};
                                                myHandler.sendMessage(msg2);
                                                if (new ExportCSV(String.valueOf(DataExport_Dialog.this.m_ExportFolder) + FileSelector_Dialog.sRoot + tmpLayer.GetLayerName() + ".csv", tmpLayer.GetDataSourceName(), String.valueOf(tmpLayer.GetLayerID()) + "_D", "SYS_ID", null, tmpLayer.GetDataFieldsString(), tmpLayer.GetFieldNamesString(), PubVar.m_Workspace.getProjectPassword()).Export()) {
                                                    Message msg3 = myHandler.obtainMessage();
                                                    msg3.what = 5;
                                                    msg3.obj = new Object[]{"导出图层【" + tmpLayer.GetLayerName() + "】数据完成.", 100};
                                                    myHandler.sendMessage(msg3);
                                                    tmpOutMsg.append(tmpLayer.GetLayerName());
                                                    tmpOutMsg.append("\r\n");
                                                } else {
                                                    Message msg4 = myHandler.obtainMessage();
                                                    msg4.what = 5;
                                                    msg4.obj = new Object[]{"导出图层【" + tmpLayer.GetLayerName() + "】数据时错误.", 0};
                                                    myHandler.sendMessage(msg4);
                                                }
                                            }
                                            if (progressDialog.isCancel) {
                                                break;
                                            }
                                        }
                                    } else if (tempOutputType.equals("Excel(CSV)格式(带坐标)")) {
                                        Handler myHandler2 = progressDialog.myHandler;
                                        int tid2 = 0;
                                        for (String tmpLayerID2 : DataExport_Dialog.this.m_Sellayers) {
                                            tid2++;
                                            Message msg5 = myHandler2.obtainMessage();
                                            msg5.what = 1;
                                            msg5.obj = new Object[]{"导出图层数据  [" + tid2 + FileSelector_Dialog.sRoot + DataExport_Dialog.this.m_Sellayers.size() + "]:"};
                                            myHandler2.sendMessage(msg5);
                                            FeatureLayer tmpLayer2 = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(tmpLayerID2);
                                            if (tmpLayer2 != null) {
                                                Message msg6 = myHandler2.obtainMessage();
                                                msg6.what = 5;
                                                msg6.obj = new Object[]{"开始导出图层【" + tmpLayer2.GetLayerName() + "】数据...", 0};
                                                myHandler2.sendMessage(msg6);
                                                String tempSavePath = String.valueOf(DataExport_Dialog.this.m_ExportFolder) + FileSelector_Dialog.sRoot + tmpLayer2.GetLayerName() + ".csv";
                                                DataSet pDataset = PubVar._Map.getGeoLayers().GetLayerByName(tmpLayerID2).getDataset();
                                                ExportCSV tempExport = new ExportCSV(tempSavePath, tmpLayer2.GetDataSourceName(), String.valueOf(tmpLayer2.GetLayerID()) + "_D", "SYS_ID", null, tmpLayer2.GetDataFieldsString(), tmpLayer2.GetFieldNamesString(), PubVar.m_Workspace.getProjectPassword());
                                                int tmpGeoSptialType = 0;
                                                if (tmpLayer2.GetLayerName().equals("测线")) {
                                                    tmpGeoSptialType = 1;
                                                }
                                                try {
                                                    if (tempExport.ExportWithGeo(pDataset.getType(), tmpGeoSptialType)) {
                                                        Message msg7 = myHandler2.obtainMessage();
                                                        msg7.what = 5;
                                                        msg7.obj = new Object[]{"导出图层【" + tmpLayer2.GetLayerName() + "】数据完成.", 100};
                                                        myHandler2.sendMessage(msg7);
                                                        tmpOutMsg.append(tmpLayer2.GetLayerName());
                                                        tmpOutMsg.append("\r\n");
                                                    } else {
                                                        Message msg8 = myHandler2.obtainMessage();
                                                        msg8.what = 5;
                                                        msg8.obj = new Object[]{"导出图层【" + tmpLayer2.GetLayerName() + "】数据时错误.", 0};
                                                        myHandler2.sendMessage(msg8);
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            if (progressDialog.isCancel) {
                                                break;
                                            }
                                        }
                                    } else if (tempOutputType.equals("ArcGIS(Shp)格式")) {
                                        Handler myHandler3 = progressDialog.myHandler;
                                        int tid3 = 0;
                                        String tmpOutputCoorType = Common.GetSpinnerValueOnID(DataExport_Dialog.this._Dialog, R.id.sp_outputSystemType);
                                        String tmpOutputCharType = Common.GetSpinnerValueOnID(DataExport_Dialog.this._Dialog, R.id.sp_outputShpCharType);
                                        for (String tmpLayerID3 : DataExport_Dialog.this.m_Sellayers) {
                                            FeatureLayer tmpLayer3 = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(tmpLayerID3);
                                            if (tmpLayer3 != null) {
                                                tid3++;
                                                Message msg9 = myHandler3.obtainMessage();
                                                msg9.what = 6;
                                                msg9.obj = new Object[]{"导出图层【" + tmpLayer3.GetLayerName() + "】数据  [" + tid3 + FileSelector_Dialog.sRoot + DataExport_Dialog.this.m_Sellayers.size() + "]:", 0, "开始导出图层数据..."};
                                                myHandler3.sendMessage(msg9);
                                                String tempSavePath2 = String.valueOf(DataExport_Dialog.this.m_ExportFolder) + FileSelector_Dialog.sRoot + tmpLayer3.GetLayerName();
                                                DataSet pDataset2 = PubVar._Map.getGeoLayers().GetLayerByName(tmpLayerID3).getDataset();
                                                ExportToShp tempExport2 = new ExportToShp();
                                                tempExport2.setBindProgressDialog(progressDialog);
                                                tempExport2.setExportCoordType(tmpOutputCoorType);
                                                tempExport2.CharacterCode = tmpOutputCharType;
                                                if (tempExport2.Export(pDataset2, tempSavePath2, null)) {
                                                    tmpOutMsg.append(tmpLayer3.GetLayerName());
                                                    tmpOutMsg.append("\r\n");
                                                }
                                            }
                                            if (progressDialog.isCancel) {
                                                break;
                                            }
                                        }
                                    } else if (tempOutputType.equals("KML格式")) {
                                        Handler myHandler4 = progressDialog.myHandler;
                                        int tid4 = 0;
                                        for (String tmpLayerID4 : DataExport_Dialog.this.m_Sellayers) {
                                            tid4++;
                                            Message msg10 = myHandler4.obtainMessage();
                                            msg10.what = 1;
                                            msg10.obj = new Object[]{"导出图层数据  [" + tid4 + FileSelector_Dialog.sRoot + DataExport_Dialog.this.m_Sellayers.size() + "]:"};
                                            myHandler4.sendMessage(msg10);
                                            FeatureLayer tmpLayer4 = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(tmpLayerID4);
                                            if (tmpLayer4 != null) {
                                                Message msg11 = myHandler4.obtainMessage();
                                                msg11.what = 5;
                                                msg11.obj = new Object[]{"开始导出图层【" + tmpLayer4.GetLayerName() + "】数据...", 0};
                                                myHandler4.sendMessage(msg11);
                                                try {
                                                    if (new ExportKML(String.valueOf(DataExport_Dialog.this.m_ExportFolder) + FileSelector_Dialog.sRoot + tmpLayer4.GetLayerName() + ".kml", tmpLayer4.GetDataSourceName(), tmpLayer4.GetLayerName(), String.valueOf(tmpLayer4.GetLayerID()) + "_D", "SYS_ID", null, tmpLayer4.GetDataFieldsString(), PubVar.m_Workspace.getProjectPassword(), PubVar._PubCommand.GetCurrentProjectPath(), DataExport_Dialog.this.m_ExportFolder).Export(PubVar._Map.getGeoLayers().GetLayerByName(tmpLayerID4).getDataset().getType())) {
                                                        Message msg12 = myHandler4.obtainMessage();
                                                        msg12.what = 5;
                                                        msg12.obj = new Object[]{"导出图层【" + tmpLayer4.GetLayerName() + "】数据完成.", 100};
                                                        myHandler4.sendMessage(msg12);
                                                        tmpOutMsg.append(tmpLayer4.GetLayerName());
                                                        tmpOutMsg.append("\r\n");
                                                    } else {
                                                        Message msg13 = myHandler4.obtainMessage();
                                                        msg13.what = 5;
                                                        msg13.obj = new Object[]{"导出图层【" + tmpLayer4.GetLayerName() + "】数据时错误.", 0};
                                                        myHandler4.sendMessage(msg13);
                                                    }
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            if (progressDialog.isCancel) {
                                                break;
                                            }
                                        }
                                    }
                                    Message msg14 = DataExport_Dialog.this.myCallHandler.obtainMessage();
                                    msg14.what = 1;
                                    msg14.obj = tmpOutMsg.toString();
                                    DataExport_Dialog.this.myCallHandler.sendMessage(msg14);
                                    progressDialog.dismiss();
                                }
                            }.start();
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        this.myCallHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataExport_Dialog.2
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    String tempMsg = msg.obj.toString();
                    if (tempMsg.equals("")) {
                        Common.ShowDialog("没有成功导出任何图层的数据.");
                        return;
                    }
                    Common.ShowDialog("导出图层数据成功:\r\n" + tempMsg + "\r\n数据导出至文件夹:" + DataExport_Dialog.this.m_ExportFolder);
                    Common.SetEditTextValueOnID(DataExport_Dialog.this._Dialog, R.id.et_outputFolder, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
                }
            }
        };
        this.m_MyTableDataList = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.data_export_dialog);
        this._Dialog.Resize(0.96f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("数据导出");
        this._Dialog.SetHeadButtons("1,2130837673,导出,导出数据", this.pCallback);
        this.m_LLSystemType = (LinearLayout) this._Dialog.findViewById(R.id.ll_outputSystemType);
        ArrayList tempArray = new ArrayList();
        tempArray.add("Excel(CSV)格式");
        tempArray.add("Excel(CSV)格式(带坐标)");
        tempArray.add("ArcGIS(Shp)格式");
        tempArray.add("KML格式");
        Common.SetSpinnerListData(this._Dialog, "请选择导出格式:", tempArray, (int) R.id.sp_outputType, this.pCallback);
        Common.SetValueToView("Excel(CSV)格式", this._Dialog.findViewById(R.id.sp_outputType));
        this.m_LLSystemType.setVisibility(8);
        List<String> tmpList = new ArrayList<>();
        tmpList.add("GB2312");
        tmpList.add(StringEncodings.UTF8);
        tmpList.add("UTF-16");
        tmpList.add("GBK");
        Common.SetSpinnerListData(this._Dialog, "请选择编码:", tmpList, (int) R.id.sp_outputShpCharType, (ICallback) null);
        Common.SetValueToView("GB2312", this._Dialog.findViewById(R.id.sp_outputShpCharType));
        this._Dialog.findViewById(R.id.ll_outputShpCharType).setVisibility(8);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        try {
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_outputFolder, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
            ArrayList tempArray = new ArrayList();
            AbstractC0383CoordinateSystem tmpCoorSystem = PubVar.m_Workspace.GetCoorSystem();
            String tmpCoorName = tmpCoorSystem.GetName();
            tempArray.add(tmpCoorName);
            if (tmpCoorSystem instanceof ProjectionCoordinateSystem) {
                tempArray.add("WGS-84坐标");
            }
            Common.SetSpinnerListData(this._Dialog, "请选择输出的坐标系:", tempArray, (int) R.id.sp_outputSystemType);
            Common.SetValueToView(tmpCoorName, this._Dialog.findViewById(R.id.sp_outputSystemType));
            if (this._AllLayersList.size() != 0) {
                MyTableFactory m_TableFactory = new MyTableFactory();
                m_TableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.list_ExportLayers), "自定义", "选择,图层名称,类型,数量", "checkbox,text,text,text", new int[]{-15, -50, -15, -20}, this.pCallback);
                this.m_MyTableDataList = new ArrayList();
                for (GeoLayer tmpXLayer : this._AllLayersList) {
                    if (tmpXLayer.getLayerType() == ELayerType.FEATURE) {
                        HashMap tmpHashMap = new HashMap();
                        tmpHashMap.put("D1", false);
                        tmpHashMap.put("D2", tmpXLayer.getLayerName());
                        if (tmpXLayer.getType() == EGeoLayerType.POINT) {
                            tmpHashMap.put("D3", "点");
                        } else if (tmpXLayer.getType() == EGeoLayerType.POLYLINE) {
                            tmpHashMap.put("D3", "线");
                        } else if (tmpXLayer.getType() == EGeoLayerType.POLYGON) {
                            tmpHashMap.put("D3", "面");
                        }
                        if (tmpXLayer.getDataset() != null) {
                            tmpHashMap.put("D4", Integer.valueOf(tmpXLayer.getDataset().getValidTotalCount()));
                        } else {
                            tmpHashMap.put("D4", 0);
                        }
                        tmpHashMap.put("D5", tmpXLayer.getLayerID());
                        this.m_MyTableDataList.add(tmpHashMap);
                    }
                }
                m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2", "D3", "D4"}, null);
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._AllLayersList = PubVar._Map.getGeoLayers().getList();
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataExport_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                DataExport_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
