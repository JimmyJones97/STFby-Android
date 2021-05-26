package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.MyControls.LayerListItemAdapter;
import  com.xzy.forestSystem.gogisapi.Setting.FormSizeSetting_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.Layer_New_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.Layer_Render_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class LayersContent_Dialog extends PopupWindow {
    static final long BUTTON_CHANGLE_DIR_TIME = 200;
    static final float BUTTON_CLICK_BIAS = (PubVar.ScaledDensity * 3.0f);
    static final float BUTTON_CLOSE_SPEED = (PubVar.ScaledDensity / 2.0f);
    private int StartX = 0;
    private int StartY = 0;
    private View.OnClickListener TipItemClickListener = new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.LayersContent_Dialog.3
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Object tempObj = view.getTag();
            if (tempObj != null) {
                LayersContent_Dialog.this.DoCommand(String.valueOf(tempObj));
            }
        }
    };
    private boolean _IsInitial = false;
    private boolean _IsMoveToobar = false;
    private long _LastMouseDownTime = 0;
    private int _LastX = ((int) (20.0f * PubVar.ScaledDensity));
    private int _LastY = ((int) (60.0f * PubVar.ScaledDensity));
    private ICallback _MainCallback = null;
    private LinearLayout _MainLayout = null;
    private View _ParentView = null;
    private View _conentView;
    private Context _context;
    private int _xDelta;
    private int _yDelta;
    private LayerListItemAdapter layerListAdapter = null;
    private List<HashMap<String, Object>> layersDataList = null;
    private List<FeatureLayer> m_LayerList = null;
    private FeatureLayer m_SelectLayer = null;
    private ListView myListView = null;
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.LayersContent_Dialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
            Object[] tempObjs;
            int tempLayerIndex;
            try {
                if (paramString.equals("窗体设置返回")) {
                    LayersContent_Dialog.this.UpdatePosition();
                } else if (paramString.contains("图层列表")) {
                    String[] tempStrs = paramString.split(",");
                    if (tempStrs != null && tempStrs.length > 2) {
                        if (tempStrs.length > 3) {
                            tempLayerIndex = LayersContent_Dialog.this.findLayerIndex(tempStrs[3]);
                        } else {
                            tempLayerIndex = Integer.parseInt(tempStrs[2]);
                        }
                        if (tempLayerIndex >= 0 && tempLayerIndex < LayersContent_Dialog.this.m_LayerList.size()) {
                            LayersContent_Dialog.this.m_SelectLayer = (FeatureLayer) LayersContent_Dialog.this.m_LayerList.get(tempLayerIndex);
                            LayersContent_Dialog.this.DoCommand(tempStrs[1]);
                        }
                    }
                } else if (paramString.equals("编辑图层字段")) {
                    FeatureLayer tmpLayer = (FeatureLayer) pObject;
                    if (tmpLayer != null) {
                        tmpLayer.CopyTo(LayersContent_Dialog.this.m_SelectLayer);
                        Iterator<HashMap<String, Object>> tempIter = LayersContent_Dialog.this.layersDataList.iterator();
                        while (true) {
                            if (tempIter.hasNext()) {
                                HashMap<String, Object> temphashMap = tempIter.next();
                                if (temphashMap.get("LayerID").equals(LayersContent_Dialog.this.m_SelectLayer.GetLayerID())) {
                                    temphashMap.put("D2", LayersContent_Dialog.this.m_SelectLayer.GetLayerName());
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        tmpLayer.SaveLayerInfo();
                        LayersContent_Dialog.this.layerListAdapter.notifyDataSetChanged();
                        PubVar._Map.Refresh();
                    }
                } else if (paramString.contains("符号库")) {
                    if (pObject != null) {
                        HashMap tmpHash = (HashMap) pObject;
                        String tmpLayerID = tmpHash.get("D0").toString();
                        int tmpIndex = LayersContent_Dialog.this.findLayerIndexInDataList(tmpLayerID);
                        if (tmpIndex > -1) {
                            HashMap tmpHash0 = (HashMap) LayersContent_Dialog.this.layersDataList.get(tmpIndex);
                            LayersContent_Dialog.this.layersDataList.remove(tmpHash0);
                            tmpHash0.put("D4", tmpHash.get("D2"));
                            tmpHash0.put("D6", tmpHash.get("D1"));
                            IRender tmpRender = (IRender) tmpHash0.get("D7");
                            if (tmpRender instanceof SimpleDisplay) {
                                SimpleDisplay tmpRender2 = (SimpleDisplay) tmpRender;
                                tmpRender2.SetSymbol((ISymbol) tmpHash.get("D3"));
                                tmpRender2.needUpdateSymbol = true;
                                tmpHash0.put("D7", tmpRender2);
                            }
                            LayersContent_Dialog.this.layersDataList.add(tmpIndex, tmpHash0);
                            if (tmpRender != null) {
                                tmpRender.UpdateToLayer(LayersContent_Dialog.this.m_SelectLayer);
                                tmpRender.SetSymbolTransparent(tmpRender.getTransparent());
                                tmpRender.SaveRender();
                            }
                            GeoLayer tempGeoLayer = PubVar._Map.getGeoLayers().GetLayerByName(tmpLayerID);
                            if (tempGeoLayer != null) {
                                tempGeoLayer.UpdateFromLayer(LayersContent_Dialog.this.m_SelectLayer);
                                if (tempGeoLayer.getRender().getIfLabel() && tempGeoLayer.getRender().needUpdateLableContent) {
                                    tempGeoLayer.getDataset().UpdateLabelContent();
                                    tempGeoLayer.getDataset().SaveGeoLabelContent();
                                }
                                if (tempGeoLayer.getRender().needUpdateSymbol) {
                                    tempGeoLayer.getDataset().UpdateAllGeometrysSymbol();
                                }
                            }
                            LayersContent_Dialog.this.m_SelectLayer.SaveLayerInfo();
                            LayersContent_Dialog.this.layerListAdapter.notifyDataSetChanged();
                            PubVar._Map.Refresh();
                        }
                    }
                } else if (paramString.contains("多值符号设置")) {
                    Object[] tempObjs2 = (Object[]) pObject;
                    if (tempObjs2 != null && tempObjs2.length > 1) {
                        String tmpLayerID2 = tempObjs2[0].toString();
                        ClassifiedRender tmpRender3 = (ClassifiedRender) tempObjs2[1];
                        int tmpIndex2 = LayersContent_Dialog.this.findLayerIndexInDataList(tmpLayerID2);
                        if (tmpIndex2 > -1) {
                            HashMap tmpHash02 = (HashMap) LayersContent_Dialog.this.layersDataList.get(tmpIndex2);
                            LayersContent_Dialog.this.layersDataList.remove(tmpHash02);
                            tmpHash02.put("D4", tmpRender3.GetSymbolFigure());
                            tmpHash02.put("D6", "");
                            IRender tmpRender22 = (IRender) tmpHash02.get("D7");
                            if (tmpRender22 != null) {
                                tmpRender3.CopyParasFrom(tmpRender22);
                            }
                            tmpRender3.needUpdateSymbol = true;
                            tmpHash02.put("D7", tmpRender3);
                            LayersContent_Dialog.this.layersDataList.add(tmpIndex2, tmpHash02);
                            int tempIndex02 = LayersContent_Dialog.this.findLayerIndex(tmpLayerID2);
                            if (tempIndex02 >= 0) {
                                LayersContent_Dialog.this.m_SelectLayer = (FeatureLayer) LayersContent_Dialog.this.m_LayerList.get(tempIndex02);
                                if (tmpRender3 != null) {
                                    tmpRender3.UpdateToLayer(LayersContent_Dialog.this.m_SelectLayer);
                                    tmpRender3.SetSymbolTransparent(tmpRender3.getTransparent());
                                    tmpRender3.SaveRender();
                                }
                                GeoLayer tempGeoLayer2 = PubVar._Map.getGeoLayers().GetLayerByName(tmpLayerID2);
                                if (tempGeoLayer2 != null) {
                                    tempGeoLayer2.UpdateFromLayer(LayersContent_Dialog.this.m_SelectLayer);
                                    if (tempGeoLayer2.getRender().getIfLabel() && tempGeoLayer2.getRender().needUpdateLableContent) {
                                        tempGeoLayer2.getDataset().UpdateLabelContent();
                                        tempGeoLayer2.getDataset().SaveGeoLabelContent();
                                    }
                                    if (tempGeoLayer2.getRender().needUpdateSymbol) {
                                        tempGeoLayer2.getDataset().UpdateAllGeometrysSymbol();
                                    }
                                }
                                LayersContent_Dialog.this.m_SelectLayer.SaveLayerInfo();
                                LayersContent_Dialog.this.layerListAdapter.notifyDataSetChanged();
                                PubVar._Map.Refresh();
                            }
                        }
                    }
                } else if (paramString.contains("渲染") && (tempObjs = (Object[]) pObject) != null && tempObjs.length > 1) {
                    String tmpLayerID3 = tempObjs[0].toString();
                    IRender tmpRender4 = (IRender) tempObjs[1];
                    int tmpIndex3 = LayersContent_Dialog.this.findLayerIndexInDataList(tmpLayerID3);
                    if (tmpIndex3 > -1) {
                        HashMap tmpHash03 = (HashMap) LayersContent_Dialog.this.layersDataList.get(tmpIndex3);
                        LayersContent_Dialog.this.layersDataList.remove(tmpHash03);
                        tmpHash03.put("D4", tmpRender4.GetSymbolFigure());
                        tmpHash03.put("D7", tmpRender4);
                        LayersContent_Dialog.this.layersDataList.add(tmpIndex3, tmpHash03);
                        int tempIndex022 = LayersContent_Dialog.this.findLayerIndex(tmpLayerID3);
                        if (tempIndex022 >= 0) {
                            LayersContent_Dialog.this.m_SelectLayer = (FeatureLayer) LayersContent_Dialog.this.m_LayerList.get(tempIndex022);
                            if (tmpRender4 != null) {
                                tmpRender4.UpdateToLayer(LayersContent_Dialog.this.m_SelectLayer);
                                tmpRender4.SetSymbolTransparent(tmpRender4.getTransparent());
                                tmpRender4.SaveRender();
                            }
                            GeoLayer tempGeoLayer3 = PubVar._Map.getGeoLayers().GetLayerByName(tmpLayerID3);
                            if (tempGeoLayer3 != null) {
                                tempGeoLayer3.UpdateFromLayer(LayersContent_Dialog.this.m_SelectLayer);
                                if (tempGeoLayer3.getRender().getIfLabel() && tempGeoLayer3.getRender().needUpdateLableContent) {
                                    tempGeoLayer3.getDataset().UpdateLabelContent();
                                    tempGeoLayer3.getDataset().SaveGeoLabelContent();
                                }
                                if (tempGeoLayer3.getRender().needUpdateSymbol) {
                                    tempGeoLayer3.getDataset().UpdateAllGeometrysSymbol();
                                }
                            }
                            LayersContent_Dialog.this.m_SelectLayer.SaveLayerInfo();
                            LayersContent_Dialog.this.layerListAdapter.notifyDataSetChanged();
                            PubVar._Map.Refresh();
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    };
    private int selectLayerIndex = -1;
    View.OnTouchListener touchListener = new View.OnTouchListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.LayersContent_Dialog.2
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent event) {
            int X = (int) event.getRawX();
            int Y = (int) event.getRawY();
            switch (event.getAction()) {
                case 0:
                    LayersContent_Dialog.this._IsMoveToobar = false;
                    LayersContent_Dialog.this.StartX = X;
                    LayersContent_Dialog.this.StartY = Y;
                    LayersContent_Dialog.this._LastMouseDownTime = event.getDownTime();
                    return true;
                case 1:
                    LayersContent_Dialog.this._IsMoveToobar = false;
                    LayersContent_Dialog.this._xDelta = X - LayersContent_Dialog.this.StartX;
                    LayersContent_Dialog.this._yDelta = Y - LayersContent_Dialog.this.StartY;
                    LayersContent_Dialog.this._LastX += LayersContent_Dialog.this._xDelta;
                    LayersContent_Dialog.this._LastY -= LayersContent_Dialog.this._yDelta;
                    if (LayersContent_Dialog.this._LastX < 0) {
                        LayersContent_Dialog.this._LastX = 0;
                    }
                    if (LayersContent_Dialog.this._LastY < 0) {
                        LayersContent_Dialog.this._LastY = 0;
                    }
                    LayersContent_Dialog.this.UpdatePosition(LayersContent_Dialog.this._LastX, LayersContent_Dialog.this._LastY);
                    return true;
                case 2:
                    if (LayersContent_Dialog.this._IsMoveToobar || (((float) Math.abs(X - LayersContent_Dialog.this.StartX)) > LayersContent_Dialog.BUTTON_CLICK_BIAS && ((float) Math.abs(Y - LayersContent_Dialog.this.StartY)) > LayersContent_Dialog.BUTTON_CLICK_BIAS)) {
                        LayersContent_Dialog.this._IsMoveToobar = true;
                        LayersContent_Dialog.this._xDelta = X - LayersContent_Dialog.this.StartX;
                        LayersContent_Dialog.this._yDelta = Y - LayersContent_Dialog.this.StartY;
                        LayersContent_Dialog.this.StartX = X;
                        LayersContent_Dialog.this.StartY = Y;
                        LayersContent_Dialog.this._LastX += LayersContent_Dialog.this._xDelta;
                        LayersContent_Dialog.this._LastY -= LayersContent_Dialog.this._yDelta;
                        if (LayersContent_Dialog.this._LastX < 0) {
                            LayersContent_Dialog.this._LastX = 0;
                        }
                        if (LayersContent_Dialog.this._LastY < 0) {
                            LayersContent_Dialog.this._LastY = 0;
                        }
                        LayersContent_Dialog.this.UpdatePosition(LayersContent_Dialog.this._LastX, LayersContent_Dialog.this._LastY);
                        break;
                    }
                case 6:
                    LayersContent_Dialog.this._IsMoveToobar = false;
                    break;
            }
            return false;
        }
    };

    public LayersContent_Dialog(Context context, View parent) {
        this._context = context;
        OnCreate(parent);
    }

    public void SetCallback(ICallback callback) {
        this._MainCallback = callback;
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

    public void OnCreate(View parent) {
        String tempStr;
        String tempStr2;
        this._ParentView = parent;
        this._conentView = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(R.layout.layerscontent_dialog, (ViewGroup) null);
        setWidth(PubVar.LayersContentWidth);
        setHeight(PubVar.LayersContentHeight);
        setContentView(this._conentView);
        update();
        this._MainLayout = (LinearLayout) this._conentView.findViewById(R.id.ll_layersContentMain);
        this._conentView.findViewById(R.id.btn_LayersContentClose).setOnClickListener(this.TipItemClickListener);
        this._conentView.findViewById(R.id.btn_LayersContentRefresh).setOnClickListener(this.TipItemClickListener);
        this._conentView.setOnTouchListener(this.touchListener);
        this.myListView = (ListView) this._conentView.findViewById(R.id.list_featureLayers);
        if (this.myListView != null) {
            int tmpMinWidth = (int) (500.0f * PubVar.ScaledDensity);
            if (tmpMinWidth < PubVar.ScreenWidth) {
                this.myListView.getLayoutParams().width = (int) (((float) PubVar.ScreenWidth) * 0.96f);
            } else {
                this.myListView.getLayoutParams().width = tmpMinWidth;
            }
        }
        HashMap<String, String> tmpHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_LayersContentForm_X");
        if (!(tmpHashMap == null || (tempStr2 = tmpHashMap.get("F2")) == null || tempStr2.equals(""))) {
            this._LastX = Integer.parseInt(tempStr2);
        }
        HashMap<String, String> tmpHashMap2 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_LayersContentForm_Y");
        if (!(tmpHashMap2 == null || (tempStr = tmpHashMap2.get("F2")) == null || tempStr.equals(""))) {
            this._LastY = Integer.parseInt(tempStr);
        }
        if (this._LastX < 0 || this._LastX >= PubVar.ScreenWidth) {
            this._LastX = (int) (20.0f * PubVar.ScaledDensity);
        }
        if (this._LastY < 0 || this._LastY >= PubVar.ScreenHeight) {
            this._LastY = (int) (60.0f * PubVar.ScaledDensity);
        }
    }

    public void hideTip() {
        this._conentView.setVisibility(8);
    }

    public void showDialog() {
        if (!this._IsInitial) {
            UpdatePosition();
            LoadLayersInfo();
        }
        if (this._conentView.getVisibility() == 8) {
            this._conentView.setVisibility(0);
        }
    }

    public void UpdatePosition() {
        UpdatePosition(this._LastX, this._LastY);
    }

    public void UpdatePosition(int x, int y) {
        this._LastX = x;
        this._LastY = y;
        if (!this._IsInitial) {
            showAtLocation(this._ParentView, 83, this._LastX, this._LastY);
            this._IsInitial = true;
            update(this._LastX, this._LastY, PubVar.LayersContentWidth, PubVar.LayersContentHeight);
            this._IsInitial = true;
        } else {
            update(this._LastX, this._LastY, PubVar.LayersContentWidth, PubVar.LayersContentHeight);
        }
        this._MainLayout.getBackground().setAlpha(PubVar.LayersContentAlpha);
        this._MainLayout.invalidate();
    }

    public void LoadLayersInfo() {
        int tempIndex;
        try {
            this.m_LayerList = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerList();
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
        } catch (Exception e) {
        }
    }

    private void updateListViewData() {
        if (this.myListView != null) {
            if (this.layersDataList == null || this.layersDataList.size() <= 0) {
                this.myListView.setAdapter((ListAdapter) null);
                return;
            }
            this.layerListAdapter = new LayerListItemAdapter(this.myListView.getContext(), 0, this.layersDataList, R.layout.layerlistitem_vector_layout);
            this.layerListAdapter.HideButtonsIndex = "6;7;";
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
    private void DoCommand(String paramString) {
        DataSet tempDataset;
        try {
            this.selectLayerIndex = -1;
            if (paramString.equals("关闭")) {
                PubVar.MyLayersContent = null;
                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_LayersContentForm_X", Integer.valueOf(this._LastX));
                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_LayersContentForm_Y", Integer.valueOf(this._LastY));
                dismiss();
            } else if (paramString.equals("刷新")) {
                LoadLayersInfo();
            } else if (paramString.equals("设置")) {
                FormSizeSetting_Dialog tmpDialog = new FormSizeSetting_Dialog();
                tmpDialog.Set_FormType(0);
                tmpDialog.SetCallback(this.pCallback);
                tmpDialog.ShowDialog();
            }
            if (this.m_SelectLayer != null) {
                if (paramString.equals("图层")) {
                    DataQuery_Dialog tempDialog = new DataQuery_Dialog();
                    tempDialog.SetQueryLayer(this.m_SelectLayer);
                    tempDialog.ShowDialog();
                } else if (paramString.equals("图层类型")) {
                    Common.ShowYesNoDialog(this._context, "是否缩放至图层【" + this.m_SelectLayer.GetLayerName() + "】全图范围?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.LayersContent_Dialog.4
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command, Object pObject) {
                            GeoLayer tmpGeoLayer;
                            DataSet pDataset;
                            Envelope tempExtend;
                            if (command.equals("YES") && (tmpGeoLayer = PubVar._Map.getGeoLayers().GetLayerByName(LayersContent_Dialog.this.m_SelectLayer.GetLayerID())) != null && (pDataset = tmpGeoLayer.getDataset()) != null && (tempExtend = pDataset.GetExtendFromDB()) != null) {
                                PubVar._Map.ZoomToExtend(tempExtend.Scale(2.0d));
                                Common.ShowToast("已经缩放至图层【" + LayersContent_Dialog.this.m_SelectLayer.GetLayerName() + "】全图范围.");
                            }
                        }
                    });
                } else if (paramString.equals("是否选择")) {
                    int tempLyIndex = findLayerIndex(this.m_SelectLayer.GetLayerID());
                    if (tempLyIndex >= 0) {
                        HashMap<String, Object> tempHashMap = this.layersDataList.get(tempLyIndex);
                        if (tempHashMap != null) {
                            boolean tempBool = Boolean.parseBoolean(tempHashMap.get("D1").toString());
                            this.m_SelectLayer.SetVisible(tempBool);
                            GeoLayer tempGeoLayer = PubVar._Map.getGeoLayers().GetLayerByName(this.m_SelectLayer.GetLayerID());
                            if (tempGeoLayer != null) {
                                tempGeoLayer.setVisible(tempBool);
                            }
                            if (tempBool && (tempDataset = PubVar.m_Workspace.GetDataSourceByEditing().GetDatasetByName(this.m_SelectLayer.GetLayerID())) != null) {
                                tempDataset.BuildMapIndex(true, false);
                            }
                        }
                        this.m_SelectLayer.SaveLayerInfo();
                        this.layerListAdapter.notifyDataSetChanged();
                        PubVar._Map.Refresh();
                    }
                } else if (paramString.equals("向上")) {
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
                    if (tempLayerIndex != 0 || !paramString.toString().equals("向上")) {
                        FeatureLayer tmpLayer2 = this.m_LayerList.get(tempLayerIndex - 1);
                        tmpLayer2.SetLayerIndex(tempLayerIndex);
                        this.m_SelectLayer.SetLayerIndex(tempLayerIndex - 1);
                        tmpLayer2.SaveLayerInfo();
                        this.m_SelectLayer.SaveLayerInfo();
                        this.m_LayerList.remove(tempLayerIndex - 1);
                        this.m_LayerList.add(tempLayerIndex, tmpLayer2);
                        this.layersDataList.remove(tempLayerIndex);
                        this.layersDataList.add(tempLayerIndex - 1, tempLayerHash);
                        this.layerListAdapter.notifyDataSetChanged();
                        PubVar._Map.getGeoLayers().MoveTo(this.m_SelectLayer.GetLayerID(), tempLayerIndex - 1);
                        PubVar._Map.Refresh();
                        return;
                    }
                    Common.ShowToast("已经在最上层！");
                } else if (paramString.equals("向下")) {
                    HashMap<String, Object> tempLayerHash2 = null;
                    int tempLayerIndex2 = -1;
                    int tempCount2 = this.layersDataList.size();
                    int i2 = 0;
                    while (true) {
                        if (i2 >= tempCount2) {
                            break;
                        }
                        HashMap<String, Object> tmpHashMap2 = this.layersDataList.get(i2);
                        if (tmpHashMap2.get("LayerID").equals(this.m_SelectLayer.GetLayerID())) {
                            tempLayerIndex2 = i2;
                            tempLayerHash2 = tmpHashMap2;
                            break;
                        }
                        i2++;
                    }
                    if (tempLayerIndex2 == -1) {
                        return;
                    }
                    if (tempLayerIndex2 != tempCount2 - 1 || !paramString.toString().equals("向下")) {
                        FeatureLayer tmpLayer22 = this.m_LayerList.get(tempLayerIndex2 + 1);
                        tmpLayer22.SetLayerIndex(tempLayerIndex2);
                        this.m_SelectLayer.SetLayerIndex(tempLayerIndex2 + 1);
                        tmpLayer22.SaveLayerInfo();
                        this.m_SelectLayer.SaveLayerInfo();
                        this.m_LayerList.remove(tempLayerIndex2 + 1);
                        this.m_LayerList.add(tempLayerIndex2, tmpLayer22);
                        this.layersDataList.remove(tempLayerIndex2);
                        this.layersDataList.add(tempLayerIndex2 + 1, tempLayerHash2);
                        this.layerListAdapter.notifyDataSetChanged();
                        PubVar._Map.getGeoLayers().MoveTo(this.m_SelectLayer.GetLayerID(), tempLayerIndex2 + 1);
                        PubVar._Map.Refresh();
                        return;
                    }
                    Common.ShowToast("已经在最下层！");
                } else if (paramString.equals("符号")) {
                    this.selectLayerIndex = findLayerIndexInDataList(this.m_SelectLayer.GetLayerID());
                    if (this.selectLayerIndex > -1) {
                        HashMap tmpHash0 = this.layersDataList.get(this.selectLayerIndex);
                        IRender tmpRender = (IRender) tmpHash0.get("D7");
                        if (tmpRender instanceof SimpleDisplay) {
                            this.m_SelectLayer.SetRenderType(1);
                            HashMap tmpHash = new HashMap();
                            tmpHash.put("D1", tmpHash0.get("D6"));
                            tmpHash.put("D2", tmpHash0.get("D4"));
                            tmpHash.put("D3", ((SimpleDisplay) tmpRender).getSymbol());
                            SymbolSelect_Dialog tempDialog2 = new SymbolSelect_Dialog();
                            tempDialog2.SetCallback(this.pCallback);
                            tempDialog2.SetSymbolTag(this.m_SelectLayer.GetLayerID());
                            tempDialog2.SetSelectedSymbol(tmpHash);
                            tempDialog2.SetGeoLayerType(this.m_SelectLayer.GetLayerType());
                            tempDialog2.ShowDialog();
                        } else if (tmpRender instanceof ClassifiedRender) {
                            this.m_SelectLayer.SetRenderType(2);
                            GeoLayer tempGeoLayer2 = PubVar._Map.getGeoLayers().GetLayerByName(this.m_SelectLayer.GetLayerID());
                            if (tempGeoLayer2 != null) {
                                ClassifiedRender_Setting_Dialog tempDialog3 = new ClassifiedRender_Setting_Dialog();
                                tempDialog3.SetLayerAndRender(this.m_SelectLayer, tempGeoLayer2, tmpRender);
                                tempDialog3.SetCallback(this.pCallback);
                                tempDialog3.ShowDialog();
                            }
                        }
                    }
                } else if (paramString.equals("渲染")) {
                    this.selectLayerIndex = findLayerIndexInDataList(this.m_SelectLayer.GetLayerID());
                    if (this.selectLayerIndex > -1) {
                        Layer_Render_Dialog tempDialog4 = new Layer_Render_Dialog();
                        tempDialog4.SetEditLayer(this.m_SelectLayer);
                        tempDialog4.SetRender((IRender) this.layersDataList.get(this.selectLayerIndex).get("D7"));
                        tempDialog4.SetCallback(this.pCallback);
                        tempDialog4.ShowDialog();
                    }
                } else if (paramString.equals("属性")) {
                    List<String> tempList = new ArrayList<>();
                    for (FeatureLayer tmpLayer : this.m_LayerList) {
                        if (!tmpLayer.GetLayerID().equals(this.m_SelectLayer.GetLayerID())) {
                            tempList.add(tmpLayer.GetLayerName());
                        }
                    }
                    Layer_New_Dialog tempLayerDialog = new Layer_New_Dialog();
                    tempLayerDialog.SetHaveLayerList(tempList);
                    tempLayerDialog.SetEditLayer(this.m_SelectLayer);
                    tempLayerDialog.SetCallback(this.pCallback);
                    tempLayerDialog.ShowDialog();
                } else if (paramString.equals("删除")) {
                    Common.ShowYesNoDialog(this._context, "是否删除图层【" + this.m_SelectLayer.GetLayerName() + "】?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.LayersContent_Dialog.5
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString2, Object pObject) {
                            int tempLayerIndex3;
                            if (paramString2.equals("YES") && (tempLayerIndex3 = LayersContent_Dialog.this.findLayerIndex(LayersContent_Dialog.this.m_SelectLayer.GetLayerID())) >= 0) {
                                ((FeatureLayer) LayersContent_Dialog.this.m_LayerList.get(tempLayerIndex3)).SetEditMode(EEditMode.DELETE);
                                LayersContent_Dialog.this.m_SelectLayer = null;
                                LayersContent_Dialog.this.LoadLayersInfo();
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
        }
    }
}
