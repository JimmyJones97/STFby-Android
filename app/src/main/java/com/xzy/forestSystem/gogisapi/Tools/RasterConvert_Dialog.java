package com.xzy.forestSystem.gogisapi.Tools;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Carto.Layer.RasterLayer;
import com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import com.xzy.forestSystem.gogisapi.Geodatabase.ConvertTifRasterClass;
import com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import com.xzy.forestSystem.hellocharts.animation.PieChartRotationAnimator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RasterConvert_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private EditText m_FileEditTxt;
    Handler m_Handler;
    private Button m_StartConvertBtn;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    @SuppressLint("WrongConstant")
    public RasterConvert_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_FileEditTxt = null;
        this.m_StartConvertBtn = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.RasterConvert_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                String[] tempPath2;
                if (command.equals("选择文件") && (tempPath2 = object.toString().split(";")) != null && tempPath2.length > 1) {
                    StringBuilder tmpStringBuilder = new StringBuilder();
                    int count = tempPath2.length / 2;
                    for (int i = 0; i < count; i++) {
                        if (tmpStringBuilder.length() > 0) {
                            tmpStringBuilder.append(";");
                        }
                        tmpStringBuilder.append(tempPath2[(i * 2) + 1]);
                    }
                    RasterConvert_Dialog.this.m_FileEditTxt.setText(tmpStringBuilder.toString());
                    RasterConvert_Dialog.this.DoCommand("加载文件");
                }
            }
        };
        this.m_Handler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Tools.RasterConvert_Dialog.2
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Common.ShowToast(String.valueOf(msg.obj));
                } else if (msg.what == 100) {
                    final Object[] tmpObjs = (Object[]) msg.obj;
                    Common.ShowYesNoDialog(RasterConvert_Dialog.this._Dialog.getContext(), "栅格数据转换成功!\r\n" + String.valueOf(tmpObjs[1]) + "\r\n是否加载至地图中?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.RasterConvert_Dialog.2.1
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command, Object pObject) {
                            if (command.equals("YES")) {
                                RasterLayer tempLayer = new RasterLayer();
                                tempLayer.SetEditMode(EEditMode.NEW);
                                tempLayer.SetLayerName(String.valueOf(tmpObjs[0]));
                                tempLayer.SetFilePath(String.valueOf(tmpObjs[1]));
                                tempLayer.SetLayerTypeName("文件");
                                PubVar._PubCommand.m_ProjectDB.GetRasterLayerManage().AddRasterLayer(tempLayer, 0);
                                PubVar._PubCommand.m_ProjectDB.GetRasterLayerManage().RenderLayerForAdd(tempLayer, 0);
                                tempLayer.SetLayerIndex(0);
                                PubVar._Map.MoveRasterLayerTo(tempLayer.GetLayerID(), 0);
                                tempLayer.SaveLayerInfo();
                                PubVar._Map.UpdateRasterLayersVisibleList();
                                XLayer tempLayer2 = PubVar._Map.GetRasterLayerByID(tempLayer.GetLayerID());
                                if (tempLayer2 != null) {
                                    final Envelope finalNewZoomExtend = tempLayer2.getExtend();
                                    Common.ShowYesNoDialog(RasterConvert_Dialog.this._Dialog.getContext(), "是否缩放至该图层范围内?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.RasterConvert_Dialog.2.1.1
                                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                        public void OnClick(String command2, Object pObject2) {
                                            if (command2.equals("YES")) {
                                                PubVar._Map.ZoomToExtend(finalNewZoomExtend);
                                                RasterConvert_Dialog.this._Dialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                } else if (msg.what == 200) {
                    final List<String[]> tmpList = (List) msg.obj;
                    Common.ShowYesNoDialog(RasterConvert_Dialog.this._Dialog.getContext(), "成功转换" + String.valueOf(tmpList.size()) + "个文件.\r\n是否加载至地图中?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.RasterConvert_Dialog.2.2
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command, Object pObject) {
                            if (command.equals("YES")) {
                                for (String[] tmpStrs : tmpList) {
                                    RasterLayer tempLayer = new RasterLayer();
                                    tempLayer.SetEditMode(EEditMode.NEW);
                                    tempLayer.SetLayerName(String.valueOf(tmpStrs[0]));
                                    tempLayer.SetFilePath(String.valueOf(tmpStrs[1]));
                                    tempLayer.SetLayerTypeName("文件");
                                    PubVar._PubCommand.m_ProjectDB.GetRasterLayerManage().AddRasterLayer(tempLayer, 0);
                                    PubVar._PubCommand.m_ProjectDB.GetRasterLayerManage().RenderLayerForAdd(tempLayer, 0);
                                    tempLayer.SetLayerIndex(0);
                                    PubVar._Map.MoveRasterLayerTo(tempLayer.GetLayerID(), 0);
                                    tempLayer.SaveLayerInfo();
                                    PubVar._Map.UpdateRasterLayersVisibleList();
                                }
                                RasterConvert_Dialog.this._Dialog.dismiss();
                            }
                        }
                    });
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.rasterconvert_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("栅格数据压缩工具");
        this.m_FileEditTxt = (EditText) this._Dialog.findViewById(R.id.et_rasterconvert_filepath);
        this._Dialog.findViewById(R.id.btn_rasterconvert_file).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_rasterconvert_LoadFile).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_rasterconvert_StartConvert).setOnClickListener(new ViewClick());
        this.m_StartConvertBtn = (Button) this._Dialog.findViewById(R.id.btn_rasterconvert_StartConvert);
        this.m_StartConvertBtn.setVisibility(4);
        this._Dialog.findViewById(R.id.tv_compressQuality).setVisibility(4);
        this._Dialog.findViewById(R.id.et_compressQuality).setVisibility(4);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    @SuppressLint("WrongConstant")
    private void DoCommand(String command) {
        try {
            if (command.equals("选择文件")) {
                FileSelector_Dialog tempDialog = new FileSelector_Dialog(".tif;.tiff;.jpg;.png;", true);
                Common.ShowToast("请选择栅格数据文件(.TIFF)等.");
                tempDialog.SetTitle("选择文件");
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
            } else if (command.equals("加载文件")) {
                this.m_StartConvertBtn.setVisibility(4);
                this._Dialog.findViewById(R.id.tv_compressQuality).setVisibility(4);
                this._Dialog.findViewById(R.id.et_compressQuality).setVisibility(4);
                StringBuilder tmpSB = new StringBuilder();
                String[] tmpStrs = this.m_FileEditTxt.getText().toString().split(";");
                if (tmpStrs != null && tmpStrs.length > 0) {
                    boolean tmpBool = false;
                    for (String tmpString : tmpStrs) {
                        File tmpFile = new File(tmpString);
                        if (!tmpFile.exists()) {
                            Common.ShowToast("文件:" + tmpString + " 不存在.");
                        } else {
                            String[] tmpMsgStrings = new String[1];
                            if (ConvertTifRasterClass.ReadRasterFileInfo(tmpFile.getAbsolutePath(), tmpMsgStrings)) {
                                tmpBool = true;
                            }
                            if (tmpMsgStrings[0] != null) {
                                tmpSB.append("文件:" + tmpString + "\r\n");
                                tmpSB.append(tmpMsgStrings[0]);
                                tmpSB.append("\r\n\r\n");
                            }
                        }
                    }
                    if (tmpBool) {
                        this.m_StartConvertBtn.setVisibility(0);
                        this._Dialog.findViewById(R.id.tv_compressQuality).setVisibility(0);
                        this._Dialog.findViewById(R.id.et_compressQuality).setVisibility(0);
                    }
                }
                Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_rasterconvert_Fileinfo, tmpSB.toString());
            } else if (command.equals("开始转换")) {
                int tmpCompQual = 70;
                try {
                    tmpCompQual = Integer.parseInt(Common.GetViewValue(this._Dialog, R.id.et_compressQuality));
                } catch (Exception e) {
                }
                if (tmpCompQual <= 0 || tmpCompQual > 100) {
                    tmpCompQual = 70;
                }
                ConvertTifRasterClass.CompressQuality = tmpCompQual;
                String[] tmpStrs2 = this.m_FileEditTxt.getText().toString().split(";");
                final List<String> tmpFilePathsList = new ArrayList<>();
                if (tmpStrs2 != null && tmpStrs2.length > 0) {
                    for (String tmpString2 : tmpStrs2) {
                        File tmpFile2 = new File(tmpString2);
                        if (tmpFile2.exists()) {
                            tmpFilePathsList.add(tmpFile2.getAbsolutePath());
                        }
                    }
                }
                if (tmpFilePathsList.size() == 1) {
                    String tmpFileNameString = new File(tmpFilePathsList.get(0)).getName();
                    String tmpSaveFilePath = String.valueOf(Common.GetAPPPath()) + FileSelector_Dialog.sRoot + tmpFileNameString + ".rxd";
                    while (new File(tmpSaveFilePath).exists()) {
                        tmpFileNameString = String.valueOf(tmpFileNameString) + "_1";
                        tmpSaveFilePath = String.valueOf(Common.GetAPPPath()) + FileSelector_Dialog.sRoot + tmpFileNameString + ".rxd";
                    }
                    final String tmpFileNameString0 = tmpFilePathsList.get(0);
                    final CustomeProgressDialog progressDialog = CustomeProgressDialog.createDialog(this._Dialog.getContext());
                    progressDialog.SetHeadTitle("栅格数据转换");
                    progressDialog.SetProgressTitle("开始转换栅格数据文件:");
                    progressDialog.SetProgressInfo("准备转换数据...");
                    progressDialog.show();
                    String finalTmpSaveFilePath = tmpSaveFilePath;
                    String finalTmpFileNameString = tmpFileNameString;
                    new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Tools.RasterConvert_Dialog.3
                        @Override // java.lang.Thread, java.lang.Runnable
                        public void run() {
                            if (ConvertTifRasterClass.Convert(tmpFileNameString0, finalTmpSaveFilePath, progressDialog)) {
                                Message tmpMsg = RasterConvert_Dialog.this.m_Handler.obtainMessage();
                                tmpMsg.what = 100;
                                tmpMsg.obj = new Object[]{finalTmpFileNameString, finalTmpSaveFilePath};
                                RasterConvert_Dialog.this.m_Handler.sendMessage(tmpMsg);
                            }
                            progressDialog.dismiss();
                        }
                    }.start();
                } else if (tmpFilePathsList.size() > 1) {
                    final CustomeProgressDialog progressDialog2 = CustomeProgressDialog.createDialog(this._Dialog.getContext());
                    progressDialog2.SetHeadTitle("栅格数据转换");
                    progressDialog2.SetProgressTitle("开始转换栅格数据文件:");
                    progressDialog2.SetProgressInfo("准备转换数据...");
                    progressDialog2.show();
                    new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Tools.RasterConvert_Dialog.4
                        @Override // java.lang.Thread, java.lang.Runnable
                        public void run() {
                            RasterConvert_Dialog.this.convertMultiFiles(tmpFilePathsList, progressDialog2);
                            progressDialog2.dismiss();
                        }
                    }.start();
                }
            }
        } catch (Exception e2) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void convertMultiFiles(List<String> filePathList, CustomeProgressDialog progressDialog) {
        if (filePathList != null && filePathList.size() > 0) {
            List<String[]> tmpList = new ArrayList<>();
            int tmpCount = filePathList.size();
            int tmpTid = 0;
            for (String tmpFilePath : filePathList) {
                tmpTid++;
                File tmpFile = new File(tmpFilePath);
                if (tmpFile.exists()) {
                    String tmpFileNameString = tmpFile.getName();
                    String tmpSaveFilePath = String.valueOf(Common.GetAPPPath()) + FileSelector_Dialog.sRoot + tmpFileNameString + ".rxd";
                    while (new File(tmpSaveFilePath).exists()) {
                        tmpFileNameString = String.valueOf(tmpFileNameString) + "_1";
                        tmpSaveFilePath = String.valueOf(Common.GetAPPPath()) + FileSelector_Dialog.sRoot + tmpFileNameString + ".rxd";
                    }
                    if (progressDialog != null) {
                        Message tmpMsg = progressDialog.myHandler.obtainMessage();
                        tmpMsg.what = 1;
                        tmpMsg.obj = "正在转换文件[" + String.valueOf(tmpTid) + FileSelector_Dialog.sRoot + String.valueOf(tmpCount) + "]:" + tmpFilePath;
                        progressDialog.myHandler.sendMessage(tmpMsg);
                    }
                    if (ConvertTifRasterClass.Convert(tmpFilePath, tmpSaveFilePath, progressDialog)) {
                        Message tmpMsg2 = this.m_Handler.obtainMessage();
                        tmpMsg2.what = 1;
                        tmpMsg2.obj = "压缩转换" + tmpFilePath + "成功";
                        this.m_Handler.sendMessage(tmpMsg2);
                        tmpList.add(new String[]{tmpFileNameString, tmpSaveFilePath});
                    }
                }
            }
            if (tmpList.size() > 0) {
                Message tmpMsg3 = this.m_Handler.obtainMessage();
                tmpMsg3.what = PieChartRotationAnimator.FAST_ANIMATION_DURATION;
                tmpMsg3.obj = tmpList;
                this.m_Handler.sendMessage(tmpMsg3);
            }
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.RasterConvert_Dialog.5
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
                RasterConvert_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
