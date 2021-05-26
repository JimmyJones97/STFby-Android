package  com.xzy.forestSystem.gogisapi.IOData;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgFile;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgLine;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgLwPolyline;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgMText;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgPoint;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgPolyline2D;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgText;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.DxfReader;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.LwPolyLine;
import  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.LoadingDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ImportCAD_Dialog {
    private DxfReader DXFFileReader;
    private XDialogTemplate _Dialog;
    private List<DwgObject> dwgLineList;
    private List<DwgObject> dwgPointList;
    private List<DwgObject> dwgPolygonList;
    private List<DwgObject> dwgTextList;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private Handler myHandler;
    private ICallback pCallback;
    private LoadingDialog progressDialog;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void ImportDWFObjects(final List<Integer> indexList) {
        final CustomeProgressDialog progressDialog2 = CustomeProgressDialog.createDialog(this._Dialog.getContext());
        progressDialog2.SetProgressTitle("开始导入CAD数据文件,共有" + indexList.size() + "种图形需要导入:");
        progressDialog2.SetProgressInfo("准备导入数据...");
        progressDialog2.show();
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.IOData.ImportCAD_Dialog.3
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                Handler tempHandler = progressDialog2.myHandler;
                List<FeatureLayer> tmpLayerList = new ArrayList<>();
                try {
                    String tmpFileName = Common.GetEditTextValueOnID(ImportCAD_Dialog.this._Dialog, R.id.et_importDWG_filepath);
                    String tmpFileName2 = tmpFileName.substring(tmpFileName.lastIndexOf(FileSelector_Dialog.sRoot) + 1);
                    String tmpFileType = tmpFileName2.substring(tmpFileName2.lastIndexOf(FileSelector_Dialog.sFolder) + 1).trim().toLowerCase();
                    if (tmpFileType.equals("dwg")) {
                        int count = indexList.size();
                        for (int i = 0; i < count; i++) {
                            int tmpI = ((Integer) indexList.get(i)).intValue();
                            String tmpLayerName = tmpFileName2;
                            List<DwgObject> tmpDwgObjsList = null;
                            if (tmpI == 1) {
                                if (ImportCAD_Dialog.this.dwgPointList.size() > 0) {
                                    tmpDwgObjsList = ImportCAD_Dialog.this.dwgPointList;
                                    tmpLayerName = String.valueOf(tmpLayerName) + "_点";
                                }
                            } else if (tmpI == 2) {
                                if (ImportCAD_Dialog.this.dwgLineList.size() > 0) {
                                    tmpDwgObjsList = ImportCAD_Dialog.this.dwgLineList;
                                    tmpLayerName = String.valueOf(tmpLayerName) + "_线";
                                }
                            } else if (tmpI == 3) {
                                if (ImportCAD_Dialog.this.dwgPolygonList.size() > 0) {
                                    tmpDwgObjsList = ImportCAD_Dialog.this.dwgPolygonList;
                                    tmpLayerName = String.valueOf(tmpLayerName) + "_面";
                                }
                            } else if (tmpI == 4 && ImportCAD_Dialog.this.dwgTextList.size() > 0) {
                                tmpDwgObjsList = ImportCAD_Dialog.this.dwgTextList;
                                tmpLayerName = String.valueOf(tmpLayerName) + "_文字";
                            }
                            if (tmpDwgObjsList != null) {
                                Message tempMsg = tempHandler.obtainMessage();
                                tempMsg.what = 4;
                                tempMsg.obj = new String[]{"导入数据文件 [" + (i + 1) + FileSelector_Dialog.sRoot + count + "]:\r\n", "开始导入数据..."};
                                tempHandler.sendMessage(tempMsg);
                                ImportDWGClass tmpImport = new ImportDWGClass();
                                FeatureLayer tempLayer = new FeatureLayer();
                                tempLayer.SetEditMode(EEditMode.NEW);
                                if (tmpImport.Import(tempLayer, tmpLayerName, tmpI, tmpDwgObjsList, progressDialog2)) {
                                    tmpLayerList.add(tempLayer);
                                    Message msg = ImportCAD_Dialog.this.myHandler.obtainMessage();
                                    msg.what = 5;
                                    msg.obj = new Object[]{"导入数据 完成,正在建立空间数据,请稍候...", 100};
                                    ImportCAD_Dialog.this.myHandler.sendMessage(msg);
                                    Message tempMsg2 = ImportCAD_Dialog.this.myHandler.obtainMessage();
                                    tempMsg2.what = 1;
                                    tempMsg2.obj = tempLayer;
                                    ImportCAD_Dialog.this.myHandler.sendMessage(tempMsg2);
                                }
                            }
                        }
                        Message tempMsg22 = ImportCAD_Dialog.this.myHandler.obtainMessage();
                        tempMsg22.what = 2;
                        ImportCAD_Dialog.this.myHandler.sendMessage(tempMsg22);
                    } else if (tmpFileType.equals("dxf") && ImportCAD_Dialog.this.DXFFileReader != null) {
                        int count2 = indexList.size();
                        for (int i2 = 0; i2 < count2; i2++) {
                            int tmpI2 = ((Integer) indexList.get(i2)).intValue();
                            int tmpCount = Integer.parseInt(String.valueOf(((HashMap) ImportCAD_Dialog.this.m_MyTableDataList.get(tmpI2 - 1)).get("D3")));
                            String tmpLayerName2 = tmpFileName2;
                            if (tmpI2 == 1) {
                                if (tmpCount > 0) {
                                    tmpLayerName2 = String.valueOf(tmpLayerName2) + "_点";
                                }
                            } else if (tmpI2 == 2) {
                                if (tmpCount > 0) {
                                    tmpLayerName2 = String.valueOf(tmpLayerName2) + "_线";
                                }
                            } else if (tmpI2 == 3) {
                                if (tmpCount > 0) {
                                    tmpLayerName2 = String.valueOf(tmpLayerName2) + "_面";
                                }
                            } else if (tmpI2 == 4 && tmpCount > 0) {
                                tmpLayerName2 = String.valueOf(tmpLayerName2) + "_文字";
                            }
                            if (tmpCount > 0) {
                                Message tempMsg3 = tempHandler.obtainMessage();
                                tempMsg3.what = 4;
                                tempMsg3.obj = new String[]{"导入数据文件 [" + (i2 + 1) + FileSelector_Dialog.sRoot + count2 + "]:\r\n", "开始导入数据..."};
                                tempHandler.sendMessage(tempMsg3);
                                ImportDXFClass tmpImport2 = new ImportDXFClass(ImportCAD_Dialog.this.DXFFileReader);
                                FeatureLayer tempLayer2 = new FeatureLayer();
                                tempLayer2.SetEditMode(EEditMode.NEW);
                                if (tmpImport2.Import(tempLayer2, tmpLayerName2, tmpI2, tmpCount, progressDialog2)) {
                                    tmpLayerList.add(tempLayer2);
                                    Message msg2 = ImportCAD_Dialog.this.myHandler.obtainMessage();
                                    msg2.what = 5;
                                    msg2.obj = new Object[]{"导入数据 完成,正在建立空间数据,请稍候...", 100};
                                    ImportCAD_Dialog.this.myHandler.sendMessage(msg2);
                                    Message tempMsg23 = ImportCAD_Dialog.this.myHandler.obtainMessage();
                                    tempMsg23.what = 1;
                                    tempMsg23.obj = tempLayer2;
                                    ImportCAD_Dialog.this.myHandler.sendMessage(tempMsg23);
                                }
                            }
                        }
                        Message tempMsg24 = ImportCAD_Dialog.this.myHandler.obtainMessage();
                        tempMsg24.what = 2;
                        ImportCAD_Dialog.this.myHandler.sendMessage(tempMsg24);
                    }
                } catch (Exception e) {
                }
                progressDialog2.dismiss();
            }
        }.start();
    }

    public ImportCAD_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.IOData.ImportCAD_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("选择文件")) {
                        String[] tempPath2 = object.toString().split(";");
                        if (tempPath2 != null && tempPath2.length > 1) {
                            Common.SetEditTextValueOnID(ImportCAD_Dialog.this._Dialog, R.id.et_importDWG_filepath, tempPath2[1]);
                            ImportCAD_Dialog.this.LoadFileInfo();
                        }
                    } else if (command.equals("加载文件")) {
                        ImportCAD_Dialog.this.LoadFileThread();
                    } else if (command.equals("确定")) {
                        if (ImportCAD_Dialog.this.m_MyTableDataList.size() > 0) {
                            List<Integer> tmpList = new ArrayList<>();
                            int tid = 0;
                            for (HashMap<String, Object> tmpHash : ImportCAD_Dialog.this.m_MyTableDataList) {
                                tid++;
                                if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                                    tmpList.add(Integer.valueOf(tid));
                                }
                            }
                            if (tmpList.size() == 0) {
                                Common.ShowDialog("没有勾选需要导入的数据类型.");
                                return;
                            }
                            StringBuilder tmpSB = new StringBuilder();
                            int count = tmpList.size();
                            for (int i = 0; i < count; i++) {
                                int tmpI = tmpList.get(i).intValue();
                                int tmpCount = Integer.parseInt(String.valueOf(((HashMap) ImportCAD_Dialog.this.m_MyTableDataList.get(tmpI - 1)).get("D3")));
                                if (tmpI == 1) {
                                    if (tmpCount == 0) {
                                        tmpSB.append("勾选的点数据数目为0.\r\n");
                                    }
                                } else if (tmpI == 2) {
                                    if (tmpCount == 0) {
                                        tmpSB.append("勾选的线数据数目为0.\r\n");
                                    }
                                } else if (tmpI == 3) {
                                    if (tmpCount == 0) {
                                        tmpSB.append("勾选的面数据数目为0.\r\n");
                                    }
                                } else if (tmpI == 4 && tmpCount == 0) {
                                    tmpSB.append("勾选的文字数据数目为0.\r\n");
                                }
                            }
                            if (tmpSB.length() > 0) {
                                Common.ShowDialog("选择导入的CAD文件中存在以下问题:\r\n" + tmpSB.toString() + "是否继续导入?");
                            } else {
                                ImportCAD_Dialog.this.ImportDWFObjects(tmpList);
                            }
                        } else {
                            Common.ShowDialog("当前CAD文件无效.");
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        this.myHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.IOData.ImportCAD_Dialog.2
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    if (ImportCAD_Dialog.this.m_Callback != null) {
                        ImportCAD_Dialog.this.m_Callback.OnClick("导入CAD图层", (FeatureLayer) msg.obj);
                    }
                } else if (msg.what == 2) {
                    ImportCAD_Dialog.this._Dialog.dismiss();
                } else if (msg.what == 5) {
                    Common.ShowDialog(String.valueOf(msg));
                } else if (msg.what == 6) {
                    ImportCAD_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                    if (ImportCAD_Dialog.this.progressDialog != null) {
                        ImportCAD_Dialog.this.progressDialog.dismiss();
                    }
                    ImportCAD_Dialog.this.progressDialog = null;
                }
            }
        };
        this.DXFFileReader = null;
        this.progressDialog = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.importdwg_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("导入CAD数据");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.btn_importDwg_selectFile).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_importDwg_SelectAll).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_importDwg_UnSelectAll).setOnClickListener(new ViewClick());
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_importDwg), "自定义", "选择,数据类型,数目", "checkbox,text,text", new int[]{-20, -40, -40}, null);
        this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
    }

    public void SetCADFilePath(String filepath) {
        Common.SetEditTextValueOnID(this._Dialog, R.id.et_importDWG_filepath, filepath);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void LoadFileThread() {
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.IOData.ImportCAD_Dialog.4
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                String tmpOutMsg = "";
                try {
                    String tmpFilePath = Common.GetEditTextValueOnID(ImportCAD_Dialog.this._Dialog, R.id.et_importDWG_filepath);
                    if (new File(tmpFilePath).exists()) {
                        String tmpFileType = tmpFilePath.substring(tmpFilePath.lastIndexOf(FileSelector_Dialog.sFolder) + 1).trim().toLowerCase();
                        if (tmpFileType.equals("dwg")) {
                            DwgFile dwg = new DwgFile(tmpFilePath);
                            try {
                                String[] tmpOutMsg2 = new String[1];
                                if (dwg.read(tmpOutMsg2)) {
                                    dwg.initializeLayerTable();
                                    dwg.calculateCadModelDwgPolylines();
                                    dwg.blockManagement();
                                    if (dwg.getDwgObjects().capacity() > 0) {
                                        int tmpCount01 = 0;
                                        int tmpCount02 = 0;
                                        int tmpCount03 = 0;
                                        int tmpCount04 = 0;
                                        Iterator tmpIter = dwg.getDwgObjects().iterator();
                                        while (tmpIter.hasNext()) {
                                            DwgObject dwgObject = (DwgObject) tmpIter.next();
                                            if (dwgObject != null) {
                                                if (dwgObject instanceof DwgPoint) {
                                                    tmpCount01++;
                                                    ImportCAD_Dialog.this.dwgPointList.add(dwgObject);
                                                } else if (dwgObject instanceof DwgLwPolyline) {
                                                    DwgLwPolyline tmpPLine = (DwgLwPolyline) dwgObject;
                                                    if (tmpPLine.getVertices() != null) {
                                                        tmpCount02++;
                                                        ImportCAD_Dialog.this.dwgLineList.add(tmpPLine);
                                                        if (tmpPLine.getVertices().length > 3 && tmpPLine.getVertices()[0].IsEqual(tmpPLine.getVertices()[tmpPLine.getVertices().length - 1])) {
                                                            tmpCount03++;
                                                            ImportCAD_Dialog.this.dwgPolygonList.add(tmpPLine);
                                                        }
                                                    }
                                                } else if (dwgObject instanceof DwgPolyline2D) {
                                                    DwgPolyline2D tmpPLine2 = (DwgPolyline2D) dwgObject;
                                                    if (tmpPLine2.getPts() != null) {
                                                        tmpCount02++;
                                                        ImportCAD_Dialog.this.dwgLineList.add(tmpPLine2);
                                                        if (tmpPLine2.getPts().length > 3 && tmpPLine2.getPts()[0].IsEqual(tmpPLine2.getPts()[tmpPLine2.getPts().length - 1])) {
                                                            tmpCount03++;
                                                            ImportCAD_Dialog.this.dwgPolygonList.add(tmpPLine2);
                                                        }
                                                    }
                                                } else if (dwgObject instanceof DwgLine) {
                                                    tmpCount02++;
                                                    ImportCAD_Dialog.this.dwgLineList.add(dwgObject);
                                                } else if ((dwgObject instanceof DwgMText) || (dwgObject instanceof DwgText)) {
                                                    tmpCount04++;
                                                    ImportCAD_Dialog.this.dwgTextList.add(dwgObject);
                                                }
                                            }
                                        }
                                        HashMap tmpHash = new HashMap();
                                        tmpHash.put("D1", false);
                                        tmpHash.put("D2", "点");
                                        tmpHash.put("D3", String.valueOf(tmpCount01));
                                        ImportCAD_Dialog.this.m_MyTableDataList.add(tmpHash);
                                        HashMap tmpHash2 = new HashMap();
                                        tmpHash2.put("D1", false);
                                        tmpHash2.put("D2", "线");
                                        tmpHash2.put("D3", String.valueOf(tmpCount02));
                                        ImportCAD_Dialog.this.m_MyTableDataList.add(tmpHash2);
                                        HashMap tmpHash3 = new HashMap();
                                        tmpHash3.put("D1", false);
                                        tmpHash3.put("D2", "面");
                                        tmpHash3.put("D3", String.valueOf(tmpCount03));
                                        ImportCAD_Dialog.this.m_MyTableDataList.add(tmpHash3);
                                        HashMap tmpHash4 = new HashMap();
                                        tmpHash4.put("D1", false);
                                        tmpHash4.put("D2", "文字");
                                        tmpHash4.put("D3", String.valueOf(tmpCount04));
                                        ImportCAD_Dialog.this.m_MyTableDataList.add(tmpHash4);
                                    } else {
                                        tmpOutMsg = "CAD文件中几何对象数目为0.";
                                    }
                                } else if (!tmpOutMsg2[0].equals("")) {
                                    tmpOutMsg = String.valueOf(tmpOutMsg2[0]) + "\r\n目前仅支持AutoCAD 2000版dwg文件.";
                                }
                            } catch (IOException e) {
                                tmpOutMsg = "读取CAD文件时错误:" + e.getMessage() + FileSelector_Dialog.sFolder;
                            }
                        } else if (tmpFileType.equals("dxf")) {
                            ImportCAD_Dialog.this.DXFFileReader = new DxfReader(tmpFilePath);
                            HashMap tmpHash5 = new HashMap();
                            tmpHash5.put("D1", false);
                            tmpHash5.put("D2", "点");
                            tmpHash5.put("D3", String.valueOf(ImportCAD_Dialog.this.DXFFileReader.getPointList().size()));
                            ImportCAD_Dialog.this.m_MyTableDataList.add(tmpHash5);
                            HashMap tmpHash6 = new HashMap();
                            tmpHash6.put("D1", false);
                            tmpHash6.put("D2", "线");
                            tmpHash6.put("D3", String.valueOf(ImportCAD_Dialog.this.DXFFileReader.getLineList().size() + ImportCAD_Dialog.this.DXFFileReader.getLwpolylineList().size() + ImportCAD_Dialog.this.DXFFileReader.getPolylineList().size()));
                            ImportCAD_Dialog.this.m_MyTableDataList.add(tmpHash6);
                            int tmpCount032 = 0;
                            Iterator<LwPolyLine> tmpIter2 = ImportCAD_Dialog.this.DXFFileReader.getLwpolylineList().iterator();
                            while (tmpIter2.hasNext()) {
                                if (tmpIter2.next().Flag == 1) {
                                    tmpCount032++;
                                }
                            }
                            HashMap tmpHash7 = new HashMap();
                            tmpHash7.put("D1", false);
                            tmpHash7.put("D2", "面");
                            tmpHash7.put("D3", String.valueOf(tmpCount032));
                            ImportCAD_Dialog.this.m_MyTableDataList.add(tmpHash7);
                            HashMap tmpHash8 = new HashMap();
                            tmpHash8.put("D1", false);
                            tmpHash8.put("D2", "文字");
                            tmpHash8.put("D3", String.valueOf(ImportCAD_Dialog.this.DXFFileReader.getTextList().size() + ImportCAD_Dialog.this.DXFFileReader.getMTextList().size()));
                            ImportCAD_Dialog.this.m_MyTableDataList.add(tmpHash8);
                        }
                    }
                } catch (Exception e2) {
                }
                if (tmpOutMsg.length() > 0) {
                    Message tempMsg2 = ImportCAD_Dialog.this.myHandler.obtainMessage();
                    tempMsg2.what = 5;
                    tempMsg2.obj = tmpOutMsg;
                    ImportCAD_Dialog.this.myHandler.sendMessage(tempMsg2);
                }
                Message tempMsg22 = ImportCAD_Dialog.this.myHandler.obtainMessage();
                tempMsg22.what = 6;
                ImportCAD_Dialog.this.myHandler.sendMessage(tempMsg22);
            }
        }.start();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void LoadFileInfo() {
        this.DXFFileReader = null;
        this.m_MyTableDataList.clear();
        this.dwgPointList = new ArrayList();
        this.dwgLineList = new ArrayList();
        this.dwgPolygonList = new ArrayList();
        this.dwgTextList = new ArrayList();
        this.progressDialog = new LoadingDialog(PubVar.MainContext);
        this.progressDialog.setTitle("正在读取CAD文件数据,请稍候...");
        this.progressDialog.setCancelable(false);
        this.progressDialog.setBindCallback(this.pCallback, "加载文件");
        this.progressDialog.showDialog();
        this.m_MyTableFactory.notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("选择文件")) {
                FileSelector_Dialog tempDialog = new FileSelector_Dialog(".dwg;.dxf;", false);
                Common.ShowToast("请选择CAD数据文件.");
                tempDialog.SetTitle("选择文件");
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
            } else if (command.equals("全选")) {
                if (this.m_MyTableDataList.size() > 0) {
                    for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
                        tmpHash.put("D1", true);
                    }
                    this.m_MyTableFactory.notifyDataSetChanged();
                }
            } else if (command.equals("反选") && this.m_MyTableDataList.size() > 0) {
                for (HashMap<String, Object> tmpHash2 : this.m_MyTableDataList) {
                    tmpHash2.put("D1", Boolean.valueOf(!Boolean.parseBoolean(String.valueOf(tmpHash2.get("D1")))));
                }
                this.m_MyTableFactory.notifyDataSetChanged();
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.IOData.ImportCAD_Dialog.5
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                ImportCAD_Dialog.this.LoadFileInfo();
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
                ImportCAD_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
