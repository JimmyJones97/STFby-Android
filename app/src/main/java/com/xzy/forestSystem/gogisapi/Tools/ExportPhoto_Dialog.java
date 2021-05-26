package  com.xzy.forestSystem.gogisapi.Tools;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.XProject.Fields_Select_Dialog;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExportPhoto_Dialog {
    private XDialogTemplate _Dialog;
    private InputSpinner _LabelFieldSpinnerDialog;
    private ICallback m_Callback;
    private String m_ExportFolder;
    public Handler myCallHandler;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public ExportPhoto_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_ExportFolder = "";
        this._LabelFieldSpinnerDialog = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.ExportPhoto_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                String[] tmpStrs02;
                try {
                    if (paramString.contains("字段组合选择")) {
                        ExportPhoto_Dialog.this._LabelFieldSpinnerDialog.getEditTextView().setText(pObject.toString());
                    } else if (paramString.equals("导出数据")) {
                        if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                            Common.ShowDialog("尊敬的用户：\r\n        【公共版】不能导出数据.为保证您能使用本软件的全部功能，请获取正式授权码！\r\n详见【关于系统】！");
                            return;
                        }
                        String tmpLyrName = Common.GetSpinnerValueOnID(ExportPhoto_Dialog.this._Dialog, R.id.sp_outputLayerName);
                        if (tmpLyrName.trim().length() == 0) {
                            Common.ShowDialog("请选择导出图层!");
                            return;
                        }
                        String tempFieldsName = ExportPhoto_Dialog.this._LabelFieldSpinnerDialog.getText();
                        if (tempFieldsName.trim().length() == 0) {
                            Common.ShowDialog("请选择导出图片名称的字段组合!");
                            return;
                        }
                        String tmpSplitChar = Common.GetEditTextValueOnID(ExportPhoto_Dialog.this._Dialog, R.id.et_outputFieldSplitChar);
                        String tempFolder = Common.GetEditTextValueOnID(ExportPhoto_Dialog.this._Dialog, R.id.et_outputFolder);
                        if (tempFolder.equals("")) {
                            Common.ShowDialog("导出目录不能为空.");
                            Common.SetEditTextValueOnID(ExportPhoto_Dialog.this._Dialog, R.id.et_outputFolder, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
                            return;
                        }
                        String tempTotalFolder = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/图片导出";
                        if (!Common.ExistFolder(tempTotalFolder)) {
                            new File(tempTotalFolder).mkdir();
                        }
                        String m_ExportFolder2 = String.valueOf(tempTotalFolder) + FileSelector_Dialog.sRoot + tempFolder;
                        if (!Common.ExistFolder(m_ExportFolder2)) {
                            new File(m_ExportFolder2).mkdir();
                        }
                        String tmpOrgPhotoFolder = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/Photo/";
                        GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByLayerName(tmpLyrName);
                        if (tmpGeoLayer != null) {
                            int tmpDone = 0;
                            FeatureLayer tmpFeatureLayer = PubVar._PubCommand.m_ProjectDB.getFeatureLayerByID(tmpGeoLayer.getLayerID());
                            if (tmpFeatureLayer != null) {
                                String[] tmpStrs01 = tempFieldsName.split(",");
                                StringBuilder tmpSB = new StringBuilder();
                                int length = tmpStrs01.length;
                                for (int i = 0; i < length; i++) {
                                    String tmpStr01 = tmpStrs01[i];
                                    if (tmpStr01.length() > 0) {
                                        String tmpFIDName = tmpFeatureLayer.GetDataFieldByFieldName(tmpStr01);
                                        if (tmpFIDName.length() > 0) {
                                            if (tmpSB.length() > 0) {
                                                tmpSB.append("||'" + tmpSplitChar + "'||");
                                            }
                                            tmpSB.append(tmpFIDName);
                                        }
                                    }
                                }
                                SQLiteReader tmpSQLiteReader = tmpGeoLayer.getDataset().getDataSource().Query("Select " + tmpSB.toString() + " AS A, SYS_PHOTO From " + tmpGeoLayer.getDataset().getDataTableName() + " Where length(SYS_PHOTO)>0 ");
                                while (tmpSQLiteReader.Read()) {
                                    String tmpPhotoInfoString = tmpSQLiteReader.GetString(1);
                                    String tmpFileName = tmpSQLiteReader.GetString(0);
                                    if (tmpPhotoInfoString != null && tmpPhotoInfoString.length() > 0 && (tmpStrs02 = tmpPhotoInfoString.split(",")) != null && tmpStrs02.length > 0) {
                                        int tmpTid = 1;
                                        int length2 = tmpStrs02.length;
                                        for (int i2 = 0; i2 < length2; i2++) {
                                            String tmpStr03 = tmpStrs02[i2];
                                            if (tmpStr03.length() > 0) {
                                                String tmpFilePath01 = String.valueOf(tmpOrgPhotoFolder) + tmpStr03;
                                                if (new File(tmpFilePath01).exists()) {
                                                    Common.CopyFile(tmpFilePath01, String.valueOf(m_ExportFolder2) + FileSelector_Dialog.sRoot + tmpFileName + tmpSplitChar + String.valueOf(tmpTid) + ".jpg");
                                                    tmpTid++;
                                                    tmpDone++;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (tmpDone > 0) {
                                Common.ShowDialog("导出图层【" + tmpGeoLayer.getLayerName() + "】的图片成功!\r\n共导出" + String.valueOf(tmpDone) + "个文件.\r\n\r\n图片导出至文件夹:" + m_ExportFolder2);
                                Common.SetEditTextValueOnID(ExportPhoto_Dialog.this._Dialog, R.id.et_outputFolder, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
                            }
                        }
                    } else if (paramString.equals("选择图层返回") && Common.GetSpinnerValueOnID(ExportPhoto_Dialog.this._Dialog, R.id.sp_outputLayerName).length() > 0) {
                        ExportPhoto_Dialog.this.refreshlayer();
                    }
                } catch (Exception e) {
                }
            }
        };
        this.myCallHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Tools.ExportPhoto_Dialog.2
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    String tempMsg = msg.obj.toString();
                    if (tempMsg.equals("")) {
                        Common.ShowDialog("没有成功导出任何图层的数据.");
                        return;
                    }
                    Common.ShowDialog("导出图层数据成功:\r\n" + tempMsg + "\r\n数据导出至文件夹:" + ExportPhoto_Dialog.this.m_ExportFolder);
                    Common.SetEditTextValueOnID(ExportPhoto_Dialog.this._Dialog, R.id.et_outputFolder, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.exportphoto_dialog);
        this._Dialog.Resize(0.96f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("图片导出");
        this._Dialog.SetHeadButtons("1,2130837673,导出,导出数据", this.pCallback);
        this._LabelFieldSpinnerDialog = (InputSpinner) this._Dialog.findViewById(R.id.sp_fieldList);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshlayer() {
        final FeatureLayer tmpFeatureLayer;
        GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByLayerName(Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_outputLayerName));
        if (tmpGeoLayer != null && (tmpFeatureLayer = PubVar._PubCommand.m_ProjectDB.getFeatureLayerByID(tmpGeoLayer.getLayerID())) != null) {
            String tempFieldsName = this._LabelFieldSpinnerDialog.getText();
            this._LabelFieldSpinnerDialog.setEditTextEnable(false);
            this._LabelFieldSpinnerDialog.getEditTextView().setText(tempFieldsName);
            this._LabelFieldSpinnerDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.ExportPhoto_Dialog.3
                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                public void OnClick(String paramString, Object pObject) {
                    String tempSelectFieldNames = ExportPhoto_Dialog.this._LabelFieldSpinnerDialog.getEditTextView().getText().toString();
                    Fields_Select_Dialog tempDialog = new Fields_Select_Dialog();
                    tempDialog.SetLayer(tmpFeatureLayer);
                    tempDialog.SetSelectFieldNames(tempSelectFieldNames);
                    tempDialog.SetCallback(ExportPhoto_Dialog.this.pCallback);
                    tempDialog.ShowDialog();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        Common.SetEditTextValueOnID(this._Dialog, R.id.et_outputFolder, new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()));
        List<String> tmpList = new ArrayList<>();
        for (GeoLayer tmpGeoLayer : PubVar._Map.getGeoLayers().getList()) {
            tmpList.add(tmpGeoLayer.getLayerName());
        }
        Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_outputLayerName, "", "选择导出的图层:", tmpList, "选择图层返回", this.pCallback);
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.ExportPhoto_Dialog.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                ExportPhoto_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
