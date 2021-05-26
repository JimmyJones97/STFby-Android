package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Display.ClassifiedRender;
import  com.xzy.forestSystem.gogisapi.Display.ClassifiedRender_Setting_Dialog;
import  com.xzy.forestSystem.gogisapi.Display.IRender;
import  com.xzy.forestSystem.gogisapi.Display.ISymbol;
import  com.xzy.forestSystem.gogisapi.Display.SimpleDisplay;
import  com.xzy.forestSystem.gogisapi.Display.SymbolSelect_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile;
import  com.xzy.forestSystem.gogisapi.Geodatabase.ImportVMXClass;
import  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.LayerListImgBtnListAdapter;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Layers_Manag_Dialog {
    private XDialogTemplate _Dialog;
    private boolean _needSave;
    private LayerListImgBtnListAdapter layerListAdapter;
    private List<HashMap<String, Object>> layersDataList;
    private ICallback m_Callback;
    private List<FeatureLayer> m_LayerList;
    private FeatureLayer m_SelectLayer;
    private Handler myHandler;
    private ListView myListView;
    private ICallback pCallback;
    private int selectLayerIndex;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public void SetLayerList(List<FeatureLayer> paramList) {
        this.m_LayerList = paramList;
    }

    public Layers_Manag_Dialog() {
        this._Dialog = null;
        this.m_LayerList = null;
        this.m_SelectLayer = null;
        this.myListView = null;
        this.layersDataList = null;
        this.layerListAdapter = null;
        this.selectLayerIndex = -1;
        this._needSave = false;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layers_Manag_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                int tempLayerIndex;
                try {
                    if (paramString.contains("图层列表")) {
                        String[] tempStrs = paramString.split(",");
                        if (tempStrs != null && tempStrs.length > 2) {
                            if (tempStrs.length > 3) {
                                tempLayerIndex = Layers_Manag_Dialog.this.findLayerIndex(tempStrs[3]);
                            } else {
                                tempLayerIndex = Integer.parseInt(tempStrs[2]);
                            }
                            if (tempLayerIndex >= 0 && tempLayerIndex < Layers_Manag_Dialog.this.m_LayerList.size()) {
                                Layers_Manag_Dialog.this.m_SelectLayer = (FeatureLayer) Layers_Manag_Dialog.this.m_LayerList.get(tempLayerIndex);
                                Layers_Manag_Dialog.this.DoCommand(tempStrs[1]);
                            }
                        }
                    } else if (paramString.equals("图层")) {
                        Layers_Manag_Dialog.this._needSave = true;
                        Layers_Manag_Dialog.this.m_LayerList.add((FeatureLayer) pObject);
                        Layers_Manag_Dialog.this.LoadLayersInfo();
                    } else if (paramString.equals("编辑图层字段")) {
                        FeatureLayer tmpLayer = (FeatureLayer) pObject;
                        if (tmpLayer != null) {
                            Layers_Manag_Dialog.this._needSave = true;
                            tmpLayer.CopyTo(Layers_Manag_Dialog.this.m_SelectLayer);
                            Iterator<HashMap<String, Object>> tempIter = Layers_Manag_Dialog.this.layersDataList.iterator();
                            while (true) {
                                if (!tempIter.hasNext()) {
                                    break;
                                }
                                HashMap<String, Object> temphashMap = tempIter.next();
                                if (temphashMap.get("LayerID").equals(Layers_Manag_Dialog.this.m_SelectLayer.GetLayerID())) {
                                    temphashMap.put("D2", Layers_Manag_Dialog.this.m_SelectLayer.GetLayerName());
                                    break;
                                }
                            }
                            if (Layers_Manag_Dialog.this.m_SelectLayer.GetEditMode() != EEditMode.NEW) {
                                Layers_Manag_Dialog.this.m_SelectLayer.SetEditMode(EEditMode.EDIT);
                            }
                            Layers_Manag_Dialog.this.updateListViewData();
                        }
                    } else if (paramString.equals("矢量底图管理列表")) {
                        ((Button) Layers_Manag_Dialog.this._Dialog.findViewById(R.id.buttonVectorLayersMang)).setText("矢量底图管理[" + PubVar._Map.getVectorGeoLayers().size() + "]");
                        PubVar._Map.Refresh();
                    } else if (paramString.equals("确定")) {
                        Common.ShowProgressDialog("正在保存图层设置...", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layers_Manag_Dialog.1.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject2) {
                                BaseToolbar tempToolbar;
                                try {
                                    if (Layers_Manag_Dialog.this.SaveLayersInfo()) {
                                        if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                                            Common.ShowDialog("尊敬的用户：\r\n        【公共版】只能显示最后5个矢量图层.为保证您能使用本软件的全部功能，请获取正式授权码！\r\n详见【关于系统】！");
                                        }
                                        if (Layers_Manag_Dialog.this._needSave && (tempToolbar = PubVar._PubCommand.GetToolbarByName("图层工具栏")) != null && tempToolbar.IsVisiable()) {
                                            tempToolbar.DoCommand("刷新图层列表");
                                        }
                                        Layers_Manag_Dialog.this._Dialog.dismiss();
                                    }
                                    if (pObject2 != null && (pObject2 instanceof Handler)) {
                                        Handler tmpHandler = (Handler) pObject2;
                                        Message tmpMsg = tmpHandler.obtainMessage();
                                        tmpMsg.what = 0;
                                        tmpHandler.sendMessage(tmpMsg);
                                    }
                                } catch (Exception e) {
                                    if (pObject2 != null && (pObject2 instanceof Handler)) {
                                        Handler tmpHandler2 = (Handler) pObject2;
                                        Message tmpMsg2 = tmpHandler2.obtainMessage();
                                        tmpMsg2.what = 0;
                                        tmpHandler2.sendMessage(tmpMsg2);
                                    }
                                } catch (Throwable th) {
                                    if (pObject2 != null && (pObject2 instanceof Handler)) {
                                        Handler tmpHandler3 = (Handler) pObject2;
                                        Message tmpMsg3 = tmpHandler3.obtainMessage();
                                        tmpMsg3.what = 0;
                                        tmpHandler3.sendMessage(tmpMsg3);
                                    }
                                    throw th;
                                }
                            }
                        });
                    } else if (paramString.equals("栅格图层管理")) {
                        ((Button) Layers_Manag_Dialog.this._Dialog.findViewById(R.id.buttonRasterLayersMang)).setText("栅格图层管理[" + PubVar._PubCommand.m_ProjectDB.GetRasterLayerManage().GetLayerList().size() + "]");
                    } else if (paramString.contains("符号库")) {
                        if (pObject != null) {
                            HashMap tmpHash = (HashMap) pObject;
                            String tmpLayerID = tmpHash.get("D0").toString();
                            int tmpIndex = Layers_Manag_Dialog.this.findLayerIndexInDataList(tmpLayerID);
                            if (tmpIndex > -1) {
                                Layers_Manag_Dialog.this._needSave = true;
                                HashMap tmpHash0 = (HashMap) Layers_Manag_Dialog.this.layersDataList.get(tmpIndex);
                                Layers_Manag_Dialog.this.layersDataList.remove(tmpHash0);
                                tmpHash0.put("D4", tmpHash.get("D2"));
                                tmpHash0.put("D6", tmpHash.get("D1"));
                                IRender tmpRender = (IRender) tmpHash0.get("D7");
                                if (tmpRender instanceof SimpleDisplay) {
                                    SimpleDisplay tmpRender2 = (SimpleDisplay) tmpRender;
                                    tmpRender2.SetSymbol((ISymbol) tmpHash.get("D3"));
                                    tmpRender2.needUpdateSymbol = true;
                                    tmpHash0.put("D7", tmpRender2);
                                }
                                Layers_Manag_Dialog.this.layersDataList.add(tmpIndex, tmpHash0);
                                int tempIndex02 = Layers_Manag_Dialog.this.findLayerIndex(tmpLayerID);
                                if (tempIndex02 >= 0) {
                                    ((FeatureLayer) Layers_Manag_Dialog.this.m_LayerList.get(tempIndex02)).SetEditMode(EEditMode.EDIT);
                                    Layers_Manag_Dialog.this.LoadLayersInfo();
                                }
                            }
                        }
                    } else if (paramString.contains("多值符号设置")) {
                        Object[] tempObjs = (Object[]) pObject;
                        if (tempObjs != null && tempObjs.length > 1) {
                            String tmpLayerID2 = tempObjs[0].toString();
                            ClassifiedRender tmpRender3 = (ClassifiedRender) tempObjs[1];
                            int tmpIndex2 = Layers_Manag_Dialog.this.findLayerIndexInDataList(tmpLayerID2);
                            if (tmpIndex2 > -1) {
                                Layers_Manag_Dialog.this._needSave = true;
                                HashMap tmpHash02 = (HashMap) Layers_Manag_Dialog.this.layersDataList.get(tmpIndex2);
                                Layers_Manag_Dialog.this.layersDataList.remove(tmpHash02);
                                tmpHash02.put("D4", tmpRender3.GetSymbolFigure());
                                tmpHash02.put("D6", "");
                                IRender tmpRender22 = (IRender) tmpHash02.get("D7");
                                if (tmpRender22 != null) {
                                    tmpRender3.CopyParasFrom(tmpRender22);
                                }
                                tmpRender3.needUpdateSymbol = true;
                                tmpHash02.put("D7", tmpRender3);
                                Layers_Manag_Dialog.this.layersDataList.add(tmpIndex2, tmpHash02);
                                int tempIndex022 = Layers_Manag_Dialog.this.findLayerIndex(tmpLayerID2);
                                if (tempIndex022 >= 0) {
                                    ((FeatureLayer) Layers_Manag_Dialog.this.m_LayerList.get(tempIndex022)).SetEditMode(EEditMode.EDIT);
                                    Layers_Manag_Dialog.this.LoadLayersInfo();
                                }
                            }
                        }
                    } else if (paramString.contains("渲染")) {
                        Object[] tempObjs2 = (Object[]) pObject;
                        if (tempObjs2 != null && tempObjs2.length > 1) {
                            String tmpLayerID3 = tempObjs2[0].toString();
                            IRender tmpRender4 = (IRender) tempObjs2[1];
                            int tmpIndex3 = Layers_Manag_Dialog.this.findLayerIndexInDataList(tmpLayerID3);
                            if (tmpIndex3 > -1) {
                                Layers_Manag_Dialog.this._needSave = true;
                                HashMap tmpHash03 = (HashMap) Layers_Manag_Dialog.this.layersDataList.get(tmpIndex3);
                                Layers_Manag_Dialog.this.layersDataList.remove(tmpHash03);
                                tmpHash03.put("D4", tmpRender4.GetSymbolFigure());
                                tmpHash03.put("D7", tmpRender4);
                                Layers_Manag_Dialog.this.layersDataList.add(tmpIndex3, tmpHash03);
                                int tempIndex023 = Layers_Manag_Dialog.this.findLayerIndex(tmpLayerID3);
                                if (tempIndex023 >= 0) {
                                    ((FeatureLayer) Layers_Manag_Dialog.this.m_LayerList.get(tempIndex023)).SetEditMode(EEditMode.EDIT);
                                    Layers_Manag_Dialog.this.LoadLayersInfo();
                                }
                            }
                        }
                    } else if (paramString.contains("选择文件")) {
                        final String[] tempPath2 = pObject.toString().split(";");
                        if (tempPath2 != null && tempPath2.length > 1) {
                            final CustomeProgressDialog progressDialog = CustomeProgressDialog.createDialog(Layers_Manag_Dialog.this._Dialog.getContext());
                            progressDialog.SetProgressTitle("开始导入数据文件,共有" + (tempPath2.length / 2) + "个文件:");
                            progressDialog.SetProgressInfo("准备导入数据...");
                            progressDialog.show();
                            new Thread() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layers_Manag_Dialog.1.2
                                @Override // java.lang.Thread, java.lang.Runnable
                                public void run() {
                                    Handler tempHandler = progressDialog.myHandler;
                                    int tempCount = tempPath2.length / 2;
                                    for (int tempI = 0; tempI < tempCount; tempI++) {
                                        String tempfilepath = tempPath2[(tempI * 2) + 1];
                                        Message tempMsg = tempHandler.obtainMessage();
                                        tempMsg.what = 4;
                                        tempMsg.obj = new String[]{"导入数据文件 [" + (tempI + 1) + FileSelector_Dialog.sRoot + tempCount + "]:\r\n" + tempfilepath, "开始导入数据..."};
                                        tempHandler.sendMessage(tempMsg);
                                        String tempFileType = tempfilepath.substring(tempfilepath.lastIndexOf(FileSelector_Dialog.sFolder) + 1).toUpperCase();
                                        if (tempFileType.equals("SHP")) {
                                            ImportShapefile shp = new ImportShapefile();
                                            FeatureLayer tempLayer = new FeatureLayer();
                                            tempLayer.SetEditMode(EEditMode.NEW);
                                            if (shp.Import(tempfilepath, tempLayer, progressDialog)) {
                                                Message tempMsg2 = Layers_Manag_Dialog.this.myHandler.obtainMessage();
                                                tempMsg2.what = 1;
                                                tempMsg2.obj = tempLayer;
                                                Layers_Manag_Dialog.this.myHandler.sendMessage(tempMsg2);
                                            }
                                        } else if (tempFileType.equals("VMX")) {
                                            List<FeatureLayer> tempLayersList = new ImportVMXClass().ImportFile2(tempfilepath, progressDialog);
                                            if (tempLayersList.size() > 0) {
                                                Message tempMsg3 = Layers_Manag_Dialog.this.myHandler.obtainMessage();
                                                tempMsg3.what = 2;
                                                tempMsg3.obj = tempLayersList;
                                                Layers_Manag_Dialog.this.myHandler.sendMessage(tempMsg3);
                                            }
                                        }
                                    }
                                    progressDialog.dismiss();
                                }
                            }.start();
                        }
                    } else if (paramString.equals("新建图层")) {
                        Layers_Manag_Dialog.this._needSave = true;
                        FeatureLayer tempLayer = (FeatureLayer) pObject;
                        tempLayer.SetEditMode(EEditMode.EDIT);
                        Layers_Manag_Dialog.this.m_LayerList.add(tempLayer);
                        PubVar.m_Workspace.GetDataSourceByEditing().CreateDataset(tempLayer.GetLayerID());
                        PubVar._PubCommand.m_ProjectDB.GetLayerRenderManage().RenderLayerForNew(tempLayer);
                        Layers_Manag_Dialog.this.LoadLayersInfo();
                    } else if (paramString.contains("导入SHP图层")) {
                        Layers_Manag_Dialog.this._needSave = true;
                        FeatureLayer tempLayer2 = (FeatureLayer) pObject;
                        Layers_Manag_Dialog.this.m_LayerList.add(tempLayer2);
                        PubVar._PubCommand.m_ProjectDB.GetLayerRenderManage().RenderLayerForNew(tempLayer2);
                        DataSet tempDataset = PubVar.m_Workspace.GetDataSourceByEditing().GetDatasetByName(tempLayer2.GetLayerID());
                        if (tempDataset != null) {
                            tempDataset.BuildMapIndex(true, true);
                            Common.ShowToast("图层[" + tempLayer2.GetLayerName() + "]最大可见比例默认为25000.");
                        }
                        Layers_Manag_Dialog.this.LoadLayersInfo();
                    } else if (paramString.contains("导入VMX图层")) {
                        Layers_Manag_Dialog.this._needSave = true;
                        for (Object tempLayer3 : (List) pObject) {
                            Layers_Manag_Dialog.this.m_LayerList.add((FeatureLayer) tempLayer3);
                            PubVar._PubCommand.m_ProjectDB.GetLayerRenderManage().RenderLayerForNew((FeatureLayer) tempLayer3);
                            DataSet tempDataset2 = PubVar.m_Workspace.GetDataSourceByEditing().GetDatasetByName(((FeatureLayer) tempLayer3).GetLayerID());
                            if (tempDataset2 != null) {
                                tempDataset2.BuildMapIndex(true, true);
                            }
                        }
                        Layers_Manag_Dialog.this.LoadLayersInfo();
                    }
                } catch (Exception e) {
                }
            }
        };
        this.myHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layers_Manag_Dialog.2
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Layers_Manag_Dialog.this.pCallback.OnClick("导入SHP图层", (FeatureLayer) msg.obj);
                } else if (msg.what == 2) {
                    Layers_Manag_Dialog.this.pCallback.OnClick("导入VMX图层", msg.obj);
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.layers_manager_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("图层管理");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.buttonVectorLayersMang).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonVectorLayersMang).setTag("矢量底图管理");
        ((Button) this._Dialog.findViewById(R.id.buttonVectorLayersMang)).setText("矢量底图管理[" + PubVar._Map.getVectorGeoLayers().size() + "]");
        int tempCount = PubVar._PubCommand.m_ProjectDB.GetRasterLayerManage().GetLayerList().size();
        this._Dialog.findViewById(R.id.buttonRasterLayersMang).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonRasterLayersMang).setTag("栅格图层管理");
        ((Button) this._Dialog.findViewById(R.id.buttonRasterLayersMang)).setText("栅格图层管理[" + tempCount + "]");
        this._Dialog.findViewById(R.id.buttonLayerNew).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonLayerNew).setTag("新建图层");
        this._Dialog.findViewById(R.id.buttonLoadLayer).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonLoadLayer).setTag("导入图层");
        this.myListView = (ListView) this._Dialog.findViewById(R.id.listViewLayers);
        if (this.myListView != null) {
            int tmpMinWidth = (int) (500.0f * PubVar.ScaledDensity);
            if (tmpMinWidth < PubVar.ScreenWidth) {
                this.myListView.getLayoutParams().width = (int) (((float) PubVar.ScreenWidth) * 0.96f);
                return;
            }
            this.myListView.getLayoutParams().width = tmpMinWidth;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String paramString) {
        try {
            this.selectLayerIndex = -1;
            if (paramString.equals("新建图层")) {
                List<String> tempList = new ArrayList<>();
                for (FeatureLayer featureLayer : this.m_LayerList) {
                    tempList.add(featureLayer.GetLayerName());
                }
                Layer_New_Dialog tempLayerDialog = new Layer_New_Dialog();
                tempLayerDialog.SetHaveLayerList(tempList);
                tempLayerDialog.SetEditLayer(null);
                tempLayerDialog.SetCallback(this.pCallback);
                tempLayerDialog.ShowDialog();
            } else if (paramString.equals("栅格图层管理")) {
                RasterLayers_Manag_Dialog tempDialog = new RasterLayers_Manag_Dialog();
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
            } else if (paramString.equals("导入图层")) {
                FileSelector_Dialog tempDialog2 = new FileSelector_Dialog(".shp;.vxd;.dxf;.vmx;", true);
                Common.ShowToast("请选择SHP,VXD,DXF,VMX文件.");
                tempDialog2.SetCallback(this.pCallback);
                tempDialog2.ShowDialog();
            } else if (paramString.equals("矢量底图管理")) {
                VectorLayers_List_Dialog tempDialog3 = new VectorLayers_List_Dialog();
                tempDialog3.SetCallback(this.pCallback);
                tempDialog3.ShowDialog();
            }
            if (this.m_SelectLayer != null) {
                if (paramString.equals("是否选择")) {
                    this._needSave = true;
                } else if (paramString.equals("重建索引")) {
                    Common.ShowYesNoDialog(this._Dialog.getContext(), "是否对图层【" + this.m_SelectLayer.GetLayerName() + "】的所有数据重新建立索引?\r\n提示:当数据量较大时该过程将耗时较长.\r\n是否继续?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layers_Manag_Dialog.3
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString2, Object pObject) {
                            if (paramString2.equals("YES")) {
                                Common.ShowProgressDialog("正在为图层【" + Layers_Manag_Dialog.this.m_SelectLayer.GetLayerName() + "】建立索引,请稍候...", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layers_Manag_Dialog.3.1
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String paramString3, Object pObject2) {
                                        try {
                                            GeoLayer pGeoLayer = PubVar._Map.GetGeoLayerByName(Layers_Manag_Dialog.this.m_SelectLayer.GetLayerID());
                                            if (!(pGeoLayer == null || pGeoLayer.getDataset() == null)) {
                                                pGeoLayer.getDataset().BuildAllGeosMapIndex();
                                                Common.ShowToast("图层【" + Layers_Manag_Dialog.this.m_SelectLayer.GetLayerName() + "】建立索引完成.");
                                            }
                                            if (pObject2 != null && (pObject2 instanceof Handler)) {
                                                Handler tmpHandler = (Handler) pObject2;
                                                Message tmpMsg = tmpHandler.obtainMessage();
                                                tmpMsg.what = 0;
                                                tmpHandler.sendMessage(tmpMsg);
                                            }
                                        } catch (Exception e) {
                                            if (pObject2 != null && (pObject2 instanceof Handler)) {
                                                Handler tmpHandler2 = (Handler) pObject2;
                                                Message tmpMsg2 = tmpHandler2.obtainMessage();
                                                tmpMsg2.what = 0;
                                                tmpHandler2.sendMessage(tmpMsg2);
                                            }
                                        } catch (Throwable th) {
                                            if (pObject2 != null && (pObject2 instanceof Handler)) {
                                                Handler tmpHandler3 = (Handler) pObject2;
                                                Message tmpMsg3 = tmpHandler3.obtainMessage();
                                                tmpMsg3.what = 0;
                                                tmpHandler3.sendMessage(tmpMsg3);
                                            }
                                            throw th;
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else if (paramString.equals("向上") || paramString.equals("向下")) {
                    HashMap<String, Object> tempLayerHash = null;
                    int tempLayerIndex = -1;
                    int tempCount = this.layersDataList.size();
                    int i = 0;
                    while (true) {
                        if (i >= tempCount) {
                            break;
                        }
                        HashMap<String, Object> tmpHashMap = this.layersDataList.get(i);
                        if (tmpHashMap.get("LayerID").equals(this.m_SelectLayer.GetLayerID())) {
                            tempLayerIndex = i;
                            tempLayerHash = tmpHashMap;
                            break;
                        }
                        i++;
                    }
                    if (tempLayerIndex == -1) {
                        return;
                    }
                    if (tempLayerIndex == 0 && paramString.toString().equals("向上")) {
                        Common.ShowToast("已经在最上层！");
                    } else if (tempLayerIndex != tempCount - 1 || !paramString.toString().equals("向下")) {
                        if (paramString.toString().equals("向上")) {
                            tempLayerIndex--;
                        } else if (paramString.toString().equals("向下")) {
                            tempLayerIndex++;
                        }
                        this.layersDataList.remove(tempLayerHash);
                        this.layersDataList.add(tempLayerIndex, tempLayerHash);
                        this.m_LayerList.remove(this.m_SelectLayer);
                        this.m_LayerList.add(tempLayerIndex, this.m_SelectLayer);
                        this.selectLayerIndex = tempLayerIndex;
                        this._needSave = true;
                        updateListViewData();
                    } else {
                        Common.ShowToast("已经在最下层！");
                    }
                } else if (paramString.equals("符号")) {
                    this.selectLayerIndex = findLayerIndexInDataList(this.m_SelectLayer.GetLayerID());
                    if (this.selectLayerIndex > -1) {
                        this._needSave = true;
                        HashMap tmpHash0 = this.layersDataList.get(this.selectLayerIndex);
                        IRender tmpRender = (IRender) tmpHash0.get("D7");
                        if (tmpRender instanceof SimpleDisplay) {
                            this.m_SelectLayer.SetRenderType(1);
                            HashMap tmpHash = new HashMap();
                            tmpHash.put("D1", tmpHash0.get("D6"));
                            tmpHash.put("D2", tmpHash0.get("D4"));
                            tmpHash.put("D3", ((SimpleDisplay) tmpRender).getSymbol());
                            SymbolSelect_Dialog tempDialog4 = new SymbolSelect_Dialog();
                            tempDialog4.SetCallback(this.pCallback);
                            tempDialog4.SetSymbolTag(this.m_SelectLayer.GetLayerID());
                            tempDialog4.SetSelectedSymbol(tmpHash);
                            tempDialog4.SetGeoLayerType(this.m_SelectLayer.GetLayerType());
                            tempDialog4.ShowDialog();
                        } else if (tmpRender instanceof ClassifiedRender) {
                            this.m_SelectLayer.SetRenderType(2);
                            GeoLayer tempGeoLayer = PubVar._Map.getGeoLayers().GetLayerByName(this.m_SelectLayer.GetLayerID());
                            if (tempGeoLayer != null) {
                                ClassifiedRender_Setting_Dialog tempDialog5 = new ClassifiedRender_Setting_Dialog();
                                tempDialog5.SetLayerAndRender(this.m_SelectLayer, tempGeoLayer, tmpRender);
                                tempDialog5.SetCallback(this.pCallback);
                                tempDialog5.ShowDialog();
                            }
                        }
                    }
                } else if (paramString.equals("渲染")) {
                    this.selectLayerIndex = findLayerIndexInDataList(this.m_SelectLayer.GetLayerID());
                    if (this.selectLayerIndex > -1) {
                        Layer_Render_Dialog tempDialog6 = new Layer_Render_Dialog();
                        tempDialog6.SetEditLayer(this.m_SelectLayer);
                        tempDialog6.SetRender((IRender) this.layersDataList.get(this.selectLayerIndex).get("D7"));
                        tempDialog6.SetCallback(this.pCallback);
                        tempDialog6.ShowDialog();
                    }
                } else if (paramString.equals("属性")) {
                    List<String> tempList2 = new ArrayList<>();
                    for (FeatureLayer tmpLayer : this.m_LayerList) {
                        if (!tmpLayer.GetLayerID().equals(this.m_SelectLayer.GetLayerID())) {
                            tempList2.add(tmpLayer.GetLayerName());
                        }
                    }
                    Layer_New_Dialog tempLayerDialog2 = new Layer_New_Dialog();
                    tempLayerDialog2.SetHaveLayerList(tempList2);
                    tempLayerDialog2.SetEditLayer(this.m_SelectLayer);
                    tempLayerDialog2.SetCallback(this.pCallback);
                    tempLayerDialog2.ShowDialog();
                } else if (paramString.equals("删除")) {
                    Common.ShowYesNoDialog(this._Dialog.getContext(), "是否删除图层【" + this.m_SelectLayer.GetLayerName() + "】?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layers_Manag_Dialog.4
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString2, Object pObject) {
                            int tempLayerIndex2;
                            if (paramString2.equals("YES") && (tempLayerIndex2 = Layers_Manag_Dialog.this.findLayerIndex(Layers_Manag_Dialog.this.m_SelectLayer.GetLayerID())) >= 0) {
                                Layers_Manag_Dialog.this._needSave = true;
                                ((FeatureLayer) Layers_Manag_Dialog.this.m_LayerList.get(tempLayerIndex2)).SetEditMode(EEditMode.DELETE);
                                Layers_Manag_Dialog.this.m_SelectLayer = null;
                                Layers_Manag_Dialog.this.LoadLayersInfo();
                            }
                        }
                    });
                }
            }
        } catch (Exception ex) {
            Common.Log("错误", "图层管理时:" + ex.getMessage());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int findLayerIndexInDataList(String layerID) {
        if (this.layersDataList == null || this.layersDataList.size() <= 0) {
            return -1;
        }
        int tempCount = this.layersDataList.size();
        for (int i = 0; i < tempCount; i++) {
            if (this.layersDataList.get(i).get("LayerID").equals(layerID)) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void LoadLayersInfo() {
        int tempIndex;
        if (this.m_LayerList != null) {
            List<HashMap<String, Object>> tempLayersDatalist = new ArrayList<>();
            for (FeatureLayer tempLayer : this.m_LayerList) {
                if (tempLayer.GetEditMode() != EEditMode.DELETE) {
                    boolean tempTag01 = true;
                    if (tempLayer.GetEditMode() == EEditMode.EDIT && (tempIndex = findLayerIndexInDataList(tempLayer.GetLayerID())) >= 0) {
                        tempLayersDatalist.add(this.layersDataList.get(tempIndex));
                        tempTag01 = false;
                    }
                    if (tempTag01) {
                        HashMap<String, Object> tempHash = new HashMap<>();
                        tempHash.put("LayerID", tempLayer.GetLayerID());
                        tempHash.put("D1", Boolean.valueOf(tempLayer.GetVisible()));
                        tempHash.put("D2", tempLayer.GetLayerName());
                        tempHash.put("D3", tempLayer.GetLayerTypeName());
                        tempHash.put("D5", Integer.valueOf(tempLayer.GetRenderType()));
                        tempHash.put("D6", tempLayer.GetSymbolName());
                        GeoLayer tempGeoLayer = PubVar._Map.getGeoLayers().GetLayerByName(tempLayer.GetLayerID());
                        if (tempGeoLayer != null) {
                            Bitmap tempBmp = tempGeoLayer.getRender().GetSymbolFigure();
                            if (tempBmp != null) {
                                tempHash.put("D4", tempBmp);
                            }
                            tempHash.put("D7", tempGeoLayer.getRender());
                        }
                        tempLayersDatalist.add(tempHash);
                    }
                }
            }
            this.layersDataList = tempLayersDatalist;
            updateListViewData();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateListViewData() {
        if (this.myListView != null) {
            if (this.layersDataList == null || this.layersDataList.size() <= 0) {
                this.myListView.setAdapter((ListAdapter) null);
                return;
            }
            this.layerListAdapter = new LayerListImgBtnListAdapter(this.myListView.getContext(), 0, this.layersDataList, R.layout.layerlistimgbtn_layout);
            this.layerListAdapter.SetCallback(this.pCallback);
            this.myListView.setAdapter((ListAdapter) this.layerListAdapter);
            this.layerListAdapter.SetNeedCheckBoxCheckReturn(true);
            if (this.selectLayerIndex >= 0) {
                this.layerListAdapter.SetSelectItemIndex(this.selectLayerIndex);
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean SaveLayersInfo() {
        HashMap<String, Object> tempHashMap;
        DataSet tempDataset;
        boolean result = false;
        if (!this._needSave) {
            return true;
        }
        int tempI = 0;
        try {
            int count = this.m_LayerList.size();
            this.layersDataList.size();
            String tempDataSourceName = PubVar.m_Workspace.GetDataSourceByEditing().getName();
            int i = 0;
            while (i < count) {
                FeatureLayer tempLayer = this.m_LayerList.get(i);
                if (tempLayer.GetEditMode() == EEditMode.DELETE) {
                    if (PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL("delete from T_Layer where LayerID = '" + tempLayer.GetLayerID() + "'")) {
                        tempLayer.SetEditMode(EEditMode.NONE);
                        PubVar._Map.getGeoLayers().Remove(tempLayer.GetLayerID());
                        PubVar.m_Workspace.GetDataSourceByEditing().RemoveDataset(tempLayer.GetLayerID());
                        this.m_LayerList.remove(i);
                    }
                    i--;
                    count--;
                } else {
                    if (tempLayer.GetEditMode() == EEditMode.NEW) {
                        PubVar.m_Workspace.GetDataSourceByEditing().CreateDataset(tempLayer.GetLayerID());
                        tempLayer.SetEditMode(EEditMode.NONE);
                    }
                    tempLayer.SetLayerIndex(tempI);
                    PubVar._Map.getGeoLayers().MoveTo(tempLayer.GetLayerID(), tempI);
                    tempI++;
                    int tempLyIndex = findLayerIndex(tempLayer.GetLayerID());
                    if (tempLyIndex >= 0 && (tempHashMap = this.layersDataList.get(tempLyIndex)) != null) {
                        IRender tempRender = (IRender) tempHashMap.get("D7");
                        if (tempRender != null) {
                            tempRender.UpdateToLayer(tempLayer);
                            tempRender.SetSymbolTransparent(tempRender.getTransparent());
                        }
                        boolean tempBool = Boolean.parseBoolean(tempHashMap.get("D1").toString());
                        tempLayer.SetVisible(tempBool);
                        GeoLayer tempGeoLayer = PubVar._Map.getGeoLayers().GetLayerByName(tempLayer.GetLayerID());
                        if (tempGeoLayer != null) {
                            tempGeoLayer.setVisible(tempBool);
                            if (tempLayer.GetEditMode() == EEditMode.EDIT) {
                                if (tempRender != null) {
                                    tempGeoLayer.setRender(tempRender);
                                    tempGeoLayer.getRender().SaveRender();
                                }
                                tempGeoLayer.UpdateFromLayer(tempLayer);
                                if (tempGeoLayer.getRender().getIfLabel() && tempGeoLayer.getRender().needUpdateLableContent) {
                                    tempGeoLayer.getDataset().UpdateLabelContent();
                                    tempGeoLayer.getDataset().SaveGeoLabelContent();
                                }
                                if (tempGeoLayer.getRender().needUpdateSymbol) {
                                    tempGeoLayer.getDataset().UpdateAllGeometrysSymbol();
                                }
                            }
                        }
                        if (tempBool && (tempDataset = PubVar.m_Workspace.GetDataSourceByEditing().GetDatasetByName(tempLayer.GetLayerID())) != null) {
                            tempDataset.BuildMapIndex(true, false);
                        }
                    }
                    tempLayer.SetEditMode(EEditMode.NONE);
                    tempLayer.SetDataSourceName(tempDataSourceName);
                    tempLayer.SaveLayerInfo();
                }
                i++;
            }
            PubVar._PubCommand.m_ProjectDB.GetLayerManage().SaveLayerFormLayerList(this.m_LayerList);
            result = true;
        } catch (Exception ex) {
            Common.Log("SaveLayerInfo", "错误:" + ex.toString() + "-->" + ex.getMessage());
        } finally {
            PubVar._Map.Refresh();
        }
        return result;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int findLayerIndex(String layerID) {
        int i = -1;
        for (FeatureLayer tempLayer : this.m_LayerList) {
            i++;
            if (tempLayer != null && tempLayer.GetLayerID().equals(layerID)) {
                return i;
            }
        }
        return -1;
    }

    public void ShowDialog() {
        this.m_LayerList = PubVar._PubCommand.m_ProjectDB.GetLayerManage().CopyLayerList();
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layers_Manag_Dialog.5
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Layers_Manag_Dialog.this.LoadLayersInfo();
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
                Layers_Manag_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
