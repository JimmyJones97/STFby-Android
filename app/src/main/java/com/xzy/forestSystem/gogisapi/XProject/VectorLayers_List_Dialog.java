package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.VectorLayers;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Display.ClassifiedRender;
import  com.xzy.forestSystem.gogisapi.Display.ClassifiedRender_Setting_Dialog;
import  com.xzy.forestSystem.gogisapi.Display.IRender;
import  com.xzy.forestSystem.gogisapi.Display.ISymbol;
import  com.xzy.forestSystem.gogisapi.Display.SimpleDisplay;
import  com.xzy.forestSystem.gogisapi.Display.SymbolSelect_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSource;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.LayerListImgBtnListAdapter;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Workspace.VectorLayerWorkspace;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VectorLayers_List_Dialog {
    private XDialogTemplate _Dialog;
    private LayerListImgBtnListAdapter layerListAdapter;
    private List<HashMap<String, Object>> layersDataList;
    private List<VectorLayers> m_BKVectorLayers;
    private ICallback m_Callback;
    private boolean m_IsSelectAllOrDe;
    private List<GeoLayer> m_LayerList;
    private GeoLayer m_SelectLayer;
    private ListView myListView;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public VectorLayers_List_Dialog() {
        this._Dialog = null;
        this.m_LayerList = null;
        this.m_SelectLayer = null;
        this.m_BKVectorLayers = null;
        this.myListView = null;
        this.layersDataList = new ArrayList();
        this.layerListAdapter = null;
        this.m_Callback = null;
        this.m_IsSelectAllOrDe = false;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_List_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object object) {
                FeatureLayer tempLayer;
                int tmpIndex;
                int tmpIndex2;
                int tempLayerIndex;
                try {
                    if (paramString.equals("确定")) {
                        try {
                            VectorLayers_List_Dialog.this.removeGeoLayersByList();
                            PubVar._PubCommand.m_ProjectDB.GetBKVectorLayerManage().SaveLayers(VectorLayers_List_Dialog.this.layersDataList);
                            PubVar._PubCommand.m_ProjectDB.GetBKVectorLayerManage().SortLayers();
                            if (VectorLayers_List_Dialog.this.m_Callback != null) {
                                VectorLayers_List_Dialog.this.m_Callback.OnClick("矢量底图管理列表", null);
                            }
                            PubVar._Map.UpdateBKVectorLayersVisibleList();
                            if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                                Common.ShowDialog("尊敬的用户：\r\n        【公共版】只能显示最后5个矢量底图图层.为保证您能使用本软件的全部功能，请获取正式授权码！\r\n详见【关于系统】！");
                            }
                        } catch (Exception e) {
                        } finally {
                            PubVar._PubCommand.UpdateMapSelections();
                            PubVar._Map.Refresh();
                        }
                        VectorLayers_List_Dialog.this._Dialog.dismiss();
                    } else if (paramString.contains("图层列表")) {
                        boolean tempBoolTag = false;
                        if (VectorLayers_List_Dialog.this.m_IsSelectAllOrDe) {
                            VectorLayers_List_Dialog.this.m_IsSelectAllOrDe = false;
                            String[] tempStrs = paramString.split(",");
                            if (tempStrs == null || tempStrs.length <= 2) {
                                VectorLayers_List_Dialog.this.layerListAdapter.ClearSelectIndex();
                                VectorLayers_List_Dialog.this.layerListAdapter.notifyDataSetChanged();
                            } else {
                                String tmpCommand = tempStrs[1];
                                if (tmpCommand.equals("是否选择") && tempStrs.length > 4) {
                                    boolean tempBool = Boolean.parseBoolean(tempStrs[4]);
                                    for (Integer num : VectorLayers_List_Dialog.this.layerListAdapter.GetSelectList()) {
                                        int tempI = num.intValue();
                                        if (tempI < VectorLayers_List_Dialog.this.layersDataList.size()) {
                                            ((HashMap) VectorLayers_List_Dialog.this.layersDataList.get(tempI)).put("D1", Boolean.valueOf(tempBool));
                                        }
                                    }
                                    VectorLayers_List_Dialog.this.layerListAdapter.ClearSelectIndex();
                                    VectorLayers_List_Dialog.this.layerListAdapter.notifyDataSetChanged();
                                } else if (tmpCommand.equals("删除")) {
                                    StringBuilder tempSB = new StringBuilder();
                                    for (Integer num2 : VectorLayers_List_Dialog.this.layerListAdapter.GetSelectList()) {
                                        int tempI2 = num2.intValue();
                                        if (tempI2 < VectorLayers_List_Dialog.this.layersDataList.size()) {
                                            tempSB.append("【");
                                            tempSB.append(((HashMap) VectorLayers_List_Dialog.this.layersDataList.get(tempI2)).get("D2").toString());
                                            tempSB.append("】\r\n");
                                        }
                                    }
                                    if (tempSB.length() > 0) {
                                        Common.ShowYesNoDialog(VectorLayers_List_Dialog.this._Dialog.getContext(), "是否删除以下图层?\r\n" + tempSB.toString(), new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_List_Dialog.1.1
                                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                            public void OnClick(String paramString2, Object pObject) {
                                                if (paramString2.equals("YES")) {
                                                    List<String> tmpLayerIDList = new ArrayList<>();
                                                    for (Integer num3 : VectorLayers_List_Dialog.this.layerListAdapter.GetSelectList()) {
                                                        int tempI3 = num3.intValue();
                                                        if (tempI3 < VectorLayers_List_Dialog.this.layersDataList.size()) {
                                                            tmpLayerIDList.add(((HashMap) VectorLayers_List_Dialog.this.layersDataList.get(tempI3)).get("LayerID").toString());
                                                        }
                                                    }
                                                    if (tmpLayerIDList.size() > 0) {
                                                        for (String tempLayerID : tmpLayerIDList) {
                                                            int tmpIndex3 = VectorLayers_List_Dialog.this.findLayerIndexInDataListByID(tempLayerID);
                                                            if (tmpIndex3 >= 0) {
                                                                VectorLayers_List_Dialog.this.layersDataList.remove(tmpIndex3);
                                                                VectorLayers_List_Dialog.this.m_SelectLayer = null;
                                                            }
                                                        }
                                                    }
                                                    VectorLayers_List_Dialog.this.layerListAdapter.ClearSelectIndex();
                                                    VectorLayers_List_Dialog.this.layerListAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        });
                                    } else {
                                        tempBoolTag = true;
                                    }
                                } else {
                                    VectorLayers_List_Dialog.this.layerListAdapter.ClearSelectIndex();
                                    VectorLayers_List_Dialog.this.layerListAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            tempBoolTag = true;
                        }
                        if (tempBoolTag) {
                            String[] tempStrs2 = paramString.split(",");
                            if (tempStrs2 != null && tempStrs2.length > 2 && (tempLayerIndex = Integer.parseInt(tempStrs2[2])) >= 0 && tempLayerIndex < VectorLayers_List_Dialog.this.m_LayerList.size()) {
                                VectorLayers_List_Dialog.this.m_SelectLayer = (GeoLayer) VectorLayers_List_Dialog.this.m_LayerList.get(tempLayerIndex);
                                VectorLayers_List_Dialog.this.DoCommand(tempStrs2[1], tempLayerIndex);
                            }
                            VectorLayers_List_Dialog.this.layerListAdapter.ClearSelectIndex();
                            VectorLayers_List_Dialog.this.layerListAdapter.notifyDataSetChanged();
                        }
                    } else if (paramString.equals("底图管理")) {
                        VectorLayers_Mang_Dialog tempDialog = new VectorLayers_Mang_Dialog();
                        tempDialog.SetCallback(VectorLayers_List_Dialog.this.pCallback);
                        tempDialog.ShowDialog();
                    } else if (paramString.equals("矢量底图管理")) {
                        Common.ShowProgressDialog("正在更新矢量底图数据,请稍候...", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_List_Dialog.1.2
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject) {
                                try {
                                    VectorLayerWorkspace tmpBKVectorLayerManage = PubVar._PubCommand.m_ProjectDB.GetBKVectorLayerManage();
                                    tmpBKVectorLayerManage.LoadLayers();
                                    tmpBKVectorLayerManage.OpenDataSource();
                                    tmpBKVectorLayerManage.Initial();
                                    VectorLayers_List_Dialog.this.m_BKVectorLayers = tmpBKVectorLayerManage.getVectorLayers();
                                    VectorLayers_List_Dialog.this.LoadLayersInfo();
                                    if (pObject != null && (pObject instanceof Handler)) {
                                        Handler tmpHandler = (Handler) pObject;
                                        Message tmpMsg = tmpHandler.obtainMessage();
                                        tmpMsg.what = 0;
                                        tmpHandler.sendMessage(tmpMsg);
                                    }
                                } catch (Exception e2) {
                                    if (pObject != null && (pObject instanceof Handler)) {
                                        Handler tmpHandler2 = (Handler) pObject;
                                        Message tmpMsg2 = tmpHandler2.obtainMessage();
                                        tmpMsg2.what = 0;
                                        tmpHandler2.sendMessage(tmpMsg2);
                                    }
                                } catch (Throwable th) {
                                    if (pObject != null && (pObject instanceof Handler)) {
                                        Handler tmpHandler3 = (Handler) pObject;
                                        Message tmpMsg3 = tmpHandler3.obtainMessage();
                                        tmpMsg3.what = 0;
                                        tmpHandler3.sendMessage(tmpMsg3);
                                    }
                                    throw th;
                                }
                            }
                        });
                    } else if (paramString.equals("符号库")) {
                        HashMap tmpHash = (HashMap) object;
                        int tmpIndex3 = VectorLayers_List_Dialog.this.findLayerIndexInDataListByID(tmpHash.get("D0").toString());
                        if (tmpIndex3 > -1) {
                            HashMap tmpHash0 = (HashMap) VectorLayers_List_Dialog.this.layersDataList.get(tmpIndex3);
                            tmpHash0.put("D20", tmpHash.get("D2"));
                            IRender tmpRender = (IRender) tmpHash0.get("D30");
                            if (tmpRender instanceof SimpleDisplay) {
                                SimpleDisplay tmpRender2 = (SimpleDisplay) tmpRender;
                                tmpRender2.SetSymbol((ISymbol) tmpHash.get("D3"));
                                tmpHash0.put("D30", tmpRender2);
                                tmpRender2.SaveRender();
                                ((GeoLayer) VectorLayers_List_Dialog.this.m_LayerList.get(tmpIndex3)).getDataset().UpdateAllGeometrysSymbol();
                            }
                            VectorLayers_List_Dialog.this.layerListAdapter.notifyDataSetChanged();
                        }
                    } else if (paramString.contains("多值符号设置")) {
                        Object[] tempObjs = (Object[]) object;
                        if (tempObjs != null && tempObjs.length > 1) {
                            String tmpLayerID = tempObjs[0].toString();
                            ClassifiedRender tmpRender3 = (ClassifiedRender) tempObjs[1];
                            int tmpIndex4 = VectorLayers_List_Dialog.this.findLayerIndexInDataListByID(tmpLayerID);
                            if (tmpIndex4 > -1) {
                                HashMap tmpHash02 = (HashMap) VectorLayers_List_Dialog.this.layersDataList.get(tmpIndex4);
                                tmpHash02.put("D20", tmpRender3.GetSymbolFigure());
                                IRender tmpRender22 = (IRender) tmpHash02.get("D30");
                                if (tmpRender22 != null) {
                                    tmpRender3.CopyParasFrom(tmpRender22);
                                }
                                tmpHash02.put("D30", tmpRender3);
                                GeoLayer tempGeoLayer = (GeoLayer) VectorLayers_List_Dialog.this.m_LayerList.get(tmpIndex4);
                                tmpRender3.SaveRender();
                                tempGeoLayer.setRender(tmpRender3);
                                tempGeoLayer.getDataset().UpdateAllGeometrysSymbol();
                                VectorLayers_List_Dialog.this.layerListAdapter.notifyDataSetChanged();
                            }
                        }
                    } else if (paramString.equals("渲染")) {
                        Object[] tempObjs2 = (Object[]) object;
                        if (tempObjs2 != null && tempObjs2.length > 1) {
                            String tmpLayerID2 = tempObjs2[0].toString();
                            IRender tmpRender4 = (IRender) tempObjs2[1];
                            if (tmpRender4 != null && (tmpIndex2 = VectorLayers_List_Dialog.this.findLayerIndexInDataListByID(tmpLayerID2)) >= 0) {
                                ((HashMap) VectorLayers_List_Dialog.this.layersDataList.get(tmpIndex2)).put("D20", tmpRender4.GetSymbolFigure());
                                GeoLayer tempGeoLayer2 = (GeoLayer) VectorLayers_List_Dialog.this.m_LayerList.get(tmpIndex2);
                                tempGeoLayer2.setRender(tmpRender4);
                                tmpRender4.SaveRenderForVectorLayer();
                                tmpRender4.SaveLabelSetting();
                                FeatureLayer tmpLayer = PubVar._PubCommand.m_ProjectDB.GetBKVectorLayerManage().GetLayerByLayerID(tempGeoLayer2.getLayerID());
                                if (tmpLayer != null) {
                                    tmpLayer.SetRenderConfig(tempGeoLayer2);
                                }
                                if (tempGeoLayer2.getRender().getIfLabel() && tempGeoLayer2.getRender().needUpdateLableContent) {
                                    tempGeoLayer2.getDataset().UpdateLabelContent();
                                    tempGeoLayer2.getDataset().SaveGeoLabelContent();
                                }
                                if (tempGeoLayer2.getRender().needUpdateSymbol) {
                                    tempGeoLayer2.getDataset().UpdateAllGeometrysSymbol();
                                }
                                VectorLayers_List_Dialog.this.layerListAdapter.notifyDataSetChanged();
                            }
                        }
                    } else if (paramString.contains("矢量背景图层字段") && (tempLayer = (FeatureLayer) object) != null && (tmpIndex = VectorLayers_List_Dialog.this.findLayerIndexInDataListByID(tempLayer.GetLayerID())) >= 0) {
                        HashMap tmpHash03 = (HashMap) VectorLayers_List_Dialog.this.layersDataList.get(tmpIndex);
                        StringBuilder tempSB2 = new StringBuilder();
                        for (LayerField tmpLayerField : tempLayer.GetFieldList()) {
                            if (tmpLayerField.GetFieldVisible()) {
                                if (tempSB2.length() > 0) {
                                    tempSB2.append(",");
                                }
                                tempSB2.append(tmpLayerField.GetDataFieldName());
                            }
                        }
                        tmpHash03.put("D40", tempSB2.toString());
                    }
                } catch (Exception e2) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.vectorlayers_list_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("矢量底图管理");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.myListView = (ListView) this._Dialog.findViewById(R.id.listView_vectorLayers);
        this._Dialog.findViewById(R.id.buttonSelectAll).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectDe).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_vectorLayersManag).setOnClickListener(new ViewClick());
        this.myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_List_Dialog.2
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                VectorLayers_List_Dialog.this.layerListAdapter.AddOrRemoveSelectIndex(arg2);
                VectorLayers_List_Dialog.this.layerListAdapter.notifyDataSetChanged();
            }
        });
        if (this.myListView != null) {
            int tmpMinWidth = (int) (520.0f * PubVar.ScaledDensity);
            if (tmpMinWidth < PubVar.ScreenWidth) {
                this.myListView.getLayoutParams().width = (int) (((float) PubVar.ScreenWidth) * 0.96f);
                return;
            }
            this.myListView.getLayoutParams().width = tmpMinWidth;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void LoadLayersInfo() {
        String tempTag;
        String[] tempStrs01;
        try {
            if (this.m_LayerList != null) {
                this.layersDataList = new ArrayList();
                for (GeoLayer tempLayer : this.m_LayerList) {
                    if (!(tempLayer.getTag() == null || (tempStrs01 = (tempTag = tempLayer.getTag().toString()).split(",")) == null || tempStrs01.length <= 0)) {
                        String tempBKVPath = tempStrs01[0];
                        String tempBKVName = tempBKVPath.substring(tempBKVPath.lastIndexOf(FileSelector_Dialog.sRoot) + 1);
                        HashMap<String, Object> tempHash = new HashMap<>();
                        tempHash.put("LayerID", tempLayer.getLayerID());
                        tempHash.put("D1", Boolean.valueOf(tempLayer.getVisible()));
                        String tmpShowStr = String.valueOf(tempLayer.getLayerName()) + "\r\n[" + tempBKVName + "]";
                        SpannableString span = new SpannableString(tmpShowStr);
                        int startIndex = tempLayer.getLayerName().length() + 1;
                        int endIndex = tmpShowStr.length();
                        span.setSpan(new AbsoluteSizeSpan((int) (12.0f * PubVar.ScaledDensity)), startIndex, endIndex, 33);
                        span.setSpan(new ForegroundColorSpan(-16776961), startIndex, endIndex, 34);
                        tempHash.put("D2", span);
                        tempHash.put("D3", Integer.valueOf(255 - tempLayer.getTransparent()));
                        tempHash.put("D4", Integer.valueOf(tempLayer.getLayerIndex()));
                        tempHash.put("D5", tempLayer.getGeoTypeName());
                        tempHash.put("D6", Double.valueOf(tempLayer.getMinX()));
                        tempHash.put("D7", Double.valueOf(tempLayer.getMinY()));
                        tempHash.put("D8", Double.valueOf(tempLayer.getMaxX()));
                        tempHash.put("D9", Double.valueOf(tempLayer.getMaxY()));
                        tempHash.put("D10", tempBKVName);
                        tempHash.put("D11", tempBKVPath);
                        tempHash.put("D12", tempLayer.getLayerName());
                        tempHash.put("D20", tempLayer.getRender().GetSymbolFigure());
                        tempHash.put("TAG", tempTag);
                        tempHash.put("D30", tempLayer.getRender());
                        tempHash.put("GeoLayer", tempLayer);
                        DataSource tmpDataSource = tempLayer.getDataset().getDataSource();
                        tempHash.put("IsEncrypt", Boolean.valueOf(tmpDataSource.IsEncrypt()));
                        tempHash.put("Security", tmpDataSource.getPassword());
                        this.layersDataList.add(tempHash);
                    }
                }
                updateListViewData();
            }
        } catch (Exception e) {
        }
    }

    private void updateListViewData() {
        try {
            if (this.myListView != null) {
                if (this.layersDataList == null || this.layersDataList.size() <= 0) {
                    this.layerListAdapter = new LayerListImgBtnListAdapter(this.myListView.getContext(), 2, this.layersDataList, R.layout.layerlistimgbtn_layout);
                    this.myListView.setAdapter((ListAdapter) this.layerListAdapter);
                    return;
                }
                this.layerListAdapter = new LayerListImgBtnListAdapter(this.myListView.getContext(), 2, this.layersDataList, R.layout.layerlistimgbtn_layout);
                this.layerListAdapter.setIsTransparentAnim(false);
                this.layerListAdapter.setIsNormalText(false);
                this.layerListAdapter.SetCallback(this.pCallback);
                this.myListView.setAdapter((ListAdapter) this.layerListAdapter);
                this.layerListAdapter.SetNeedCheckBoxCheckReturn(true);
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command, final int index) {
        FeatureLayer tmpLayer;
        FeatureLayer tmpLayer2;
        FeatureLayer tmpLayer3;
        try {
            if (command.equals("底图管理")) {
                VectorLayers_Mang_Dialog tempDialog = new VectorLayers_Mang_Dialog();
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
                return;
            }
            this.m_IsSelectAllOrDe = false;
            if (command.equals("全选")) {
                this.m_IsSelectAllOrDe = true;
                int tempCount = this.layersDataList.size();
                for (int i = 0; i < tempCount; i++) {
                    this.layerListAdapter.AddSelectIndex(i);
                }
                this.layerListAdapter.notifyDataSetChanged();
            } else if (command.equals("反选")) {
                this.m_IsSelectAllOrDe = true;
                int count = this.layersDataList.size();
                for (int i2 = 0; i2 < count; i2++) {
                    this.layerListAdapter.AddOrRemoveSelectIndex(i2);
                }
                this.layerListAdapter.notifyDataSetChanged();
            } else if (index >= 0 && index < this.layersDataList.size()) {
                if (command.equals("向上")) {
                    if (index > 0) {
                        this.layersDataList.remove(index);
                        this.layersDataList.add(index - 1, this.layersDataList.get(index));
                        updateListViewData();
                        return;
                    }
                    Common.ShowToast("已经在最上层了.");
                } else if (command.equals("向下")) {
                    if (index < this.layersDataList.size() - 1) {
                        this.layersDataList.remove(index);
                        this.layersDataList.add(index + 1, this.layersDataList.get(index));
                        updateListViewData();
                        return;
                    }
                    Common.ShowToast("已经在最底层了.");
                } else if (command.equals("删除")) {
                    Common.ShowYesNoDialog(this._Dialog.getContext(), "是否删除以下图层?\r\n【" + this.layersDataList.get(index).get("D2").toString() + "】", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_List_Dialog.3
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (paramString.equals("YES")) {
                                VectorLayers_List_Dialog.this.layersDataList.remove(index);
                                VectorLayers_List_Dialog.this.layerListAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                } else if (command.equals("渲染")) {
                    if (!(this.m_SelectLayer == null || (tmpLayer3 = PubVar._PubCommand.m_ProjectDB.GetBKVectorLayerManage().GetLayerByLayerID(this.m_SelectLayer.getLayerID())) == null)) {
                        IRender tmpRender = this.m_SelectLayer.getRender();
                        Layer_Render_Dialog tempDialog2 = new Layer_Render_Dialog();
                        tempDialog2.SetEditLayer(tmpLayer3);
                        tempDialog2.SetRender(tmpRender);
                        tempDialog2.SetShowTransparent(false);
                        tempDialog2.SetCallback(this.pCallback);
                        tempDialog2.ShowDialog();
                    }
                } else if (command.equals("属性")) {
                    if (!(this.m_SelectLayer == null || (tmpLayer2 = PubVar._PubCommand.m_ProjectDB.GetBKVectorLayerManage().GetLayerByLayerID(this.m_SelectLayer.getLayerID())) == null)) {
                        VectorLayer_FieldVisible_Dialog tempDialog3 = new VectorLayer_FieldVisible_Dialog();
                        tempDialog3.SetEditLayer(tmpLayer2);
                        tempDialog3.SetCallback(this.pCallback);
                        tempDialog3.ShowDialog();
                    }
                } else if (this.m_SelectLayer != null && command.equals("重建索引")) {
                    Common.ShowYesNoDialog(this._Dialog.getContext(), "是否对图层【" + this.m_SelectLayer.getLayerName() + "】的所有数据重新建立索引?\r\n提示:当数据量较大时该过程将耗时较长.\r\n是否继续?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_List_Dialog.4
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (paramString.equals("YES")) {
                                Common.ShowProgressDialog("正在为图层【" + VectorLayers_List_Dialog.this.m_SelectLayer.getLayerName() + "】建立索引,请稍候...", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_List_Dialog.4.1
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String paramString2, Object pObject2) {
                                        try {
                                            GeoLayer pGeoLayer = PubVar._Map.GetGeoLayerByDataSource(VectorLayers_List_Dialog.this.m_SelectLayer.getDataset().getDataSource().getName(), VectorLayers_List_Dialog.this.m_SelectLayer.getLayerID());
                                            if (!(pGeoLayer == null || pGeoLayer.getDataset() == null)) {
                                                pGeoLayer.getDataset().BuildAllGeosMapIndex();
                                                Common.ShowToast("图层【" + VectorLayers_List_Dialog.this.m_SelectLayer.getLayerName() + "】建立索引完成.");
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
                } else if (command.equals("符号")) {
                    if (this.m_SelectLayer != null && index > -1) {
                        this.layersDataList.get(index);
                        IRender tmpRender2 = null;
                        try {
                            tmpRender2 = this.m_SelectLayer.getRender();
                        } catch (Exception e) {
                        }
                        if (tmpRender2 instanceof SimpleDisplay) {
                            HashMap tmpHash = new HashMap();
                            tmpHash.put("D1", ((SimpleDisplay) tmpRender2).getSymbolName());
                            tmpHash.put("D2", tmpRender2.GetSymbolFigure());
                            tmpHash.put("D3", ((SimpleDisplay) tmpRender2).getSymbol());
                            SymbolSelect_Dialog tempDialog4 = new SymbolSelect_Dialog();
                            tempDialog4.SetCallback(this.pCallback);
                            tempDialog4.SetSymbolTag(this.m_SelectLayer.getLayerID());
                            tempDialog4.SetSelectedSymbol(tmpHash);
                            tempDialog4.SetGeoLayerType(this.m_SelectLayer.getType());
                            tempDialog4.ShowDialog();
                        } else if ((tmpRender2 instanceof ClassifiedRender) && (tmpLayer = PubVar._PubCommand.m_ProjectDB.GetBKVectorLayerManage().GetLayerByLayerID(this.m_SelectLayer.getLayerID())) != null) {
                            ClassifiedRender_Setting_Dialog tempDialog5 = new ClassifiedRender_Setting_Dialog();
                            tempDialog5.SetLayerAndRender(tmpLayer, this.m_SelectLayer, tmpRender2);
                            tempDialog5.SetCallback(this.pCallback);
                            tempDialog5.ShowDialog();
                        }
                    }
                } else if (command.equals("图层") && index >= 0 && index < this.layersDataList.size()) {
                    HashMap tempHash = this.layersDataList.get(index);
                    StringBuilder tmpSB = new StringBuilder();
                    tmpSB.append("【图层】:" + String.valueOf(tempHash.get("D12")));
                    tmpSB.append("\r\n【类型】:" + String.valueOf(tempHash.get("D5")));
                    tmpSB.append("\r\n【透明度】:" + String.valueOf(tempHash.get("D3")));
                    tmpSB.append("\r\n【文件】:" + String.valueOf(tempHash.get("D11")));
                    tmpSB.append("\r\n【MinX】:" + String.valueOf(tempHash.get("D6")));
                    tmpSB.append("\r\n【MinY】:" + String.valueOf(tempHash.get("D7")));
                    tmpSB.append("\r\n【MaxX】:" + String.valueOf(tempHash.get("D8")));
                    tmpSB.append("\r\n【MaxY】:" + String.valueOf(tempHash.get("D9")));
                    if (Boolean.parseBoolean(String.valueOf(tempHash.get("IsEncrypt")))) {
                        tmpSB.append("\r\n【加密】:已加密");
                    } else {
                        tmpSB.append("\r\n【加密】:未加密");
                    }
                    Common.ShowDialog(tmpSB.toString());
                }
            }
        } catch (Exception e2) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void removeGeoLayersByList() {
        try {
            List<String> tmpHasList = new ArrayList<>();
            for (HashMap<String, Object> temphash : this.layersDataList) {
                tmpHasList.add(temphash.get("TAG").toString());
            }
            List<GeoLayer> tmpDeleteList = new ArrayList<>();
            for (GeoLayer pGeoLayer : this.m_LayerList) {
                if (pGeoLayer.getTag() == null || !tmpHasList.contains(pGeoLayer.getTag())) {
                    tmpDeleteList.add(pGeoLayer);
                } else {
                    HashMap<String, Object> temphash2 = this.layersDataList.get(tmpHasList.indexOf(pGeoLayer.getTag()));
                    pGeoLayer.setVisible(Boolean.parseBoolean(temphash2.get("D1").toString()));
                    int tmpTransp = 255 - Integer.parseInt(temphash2.get("D3").toString());
                    pGeoLayer.setTransparent(tmpTransp);
                    pGeoLayer.getRender().SetSymbolTransparent(tmpTransp);
                }
            }
            if (tmpDeleteList.size() > 0) {
                for (GeoLayer geoLayer : tmpDeleteList) {
                    PubVar._PubCommand.m_ProjectDB.GetBKVectorLayerManage().RemoveGeoLayer(geoLayer);
                }
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this.m_LayerList = PubVar._Map.getVectorGeoLayers().getList();
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_List_Dialog.5
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                VectorLayers_List_Dialog.this.LoadLayersInfo();
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int findLayerIndexInDataListByID(String layerID) {
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

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                String tempTag = view.getTag().toString();
                if (tempTag.equals("底图管理")) {
                    VectorLayers_List_Dialog.this.DoCommand(tempTag, -1);
                    return;
                }
                int tempIndex = -1;
                if (VectorLayers_List_Dialog.this.m_SelectLayer != null) {
                    tempIndex = VectorLayers_List_Dialog.this.findLayerIndexInDataListByID(VectorLayers_List_Dialog.this.m_SelectLayer.getLayerID());
                }
                VectorLayers_List_Dialog.this.DoCommand(tempTag, tempIndex);
            }
        }
    }
}
