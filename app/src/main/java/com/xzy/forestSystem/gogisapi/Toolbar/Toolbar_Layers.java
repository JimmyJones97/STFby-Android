package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Display.IRender;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Selection;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.XProject.Layer_New_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.Layer_Render_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Toolbar_Layers extends BaseToolbar {
    private List<HashMap<String, Object>> layersDataList = null;
    private Button m_EditLayerBtn = null;
    private Button m_EditLayerFieldBtn = null;
    private SpinnerList m_LayersListSpinner = null;
    private GeoLayer m_SelectLayer = null;
    private CheckBox m_layerVisible = null;
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Layers.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
            String tmpLayerID;
            GeoLayer tempGeoLayer;
            if (paramString.equals("SelectedLayer")) {
                Toolbar_Layers.this.refreshSelectLayer();
            } else if (paramString.contains("渲染")) {
                Object[] tempObjs = (Object[]) pObject;
                if (tempObjs != null && tempObjs.length > 1 && (tempGeoLayer = PubVar._Map.GetGeoLayerByName((tmpLayerID = tempObjs[0].toString()))) != null) {
                    IRender tempRender = (IRender) tempObjs[1];
                    if (tempRender != null) {
                        tempGeoLayer.setRender(tempRender);
                        tempGeoLayer.getRender().SaveRender();
                    }
                    if (tempGeoLayer.getRender().getIfLabel() && tempGeoLayer.getRender().needUpdateLableContent) {
                        tempGeoLayer.getDataset().UpdateLabelContent();
                        tempGeoLayer.getDataset().SaveGeoLabelContent();
                    }
                    if (tempGeoLayer.getRender().needUpdateSymbol) {
                        tempGeoLayer.getDataset().UpdateAllGeometrysSymbol();
                    }
                    FeatureLayer tmpLayer = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(tmpLayerID);
                    if (tmpLayer != null) {
                        if (tempRender != null) {
                            tempRender.UpdateToLayer(tmpLayer);
                        }
                        tmpLayer.SaveLayerInfo();
                        tempGeoLayer.UpdateFromLayer(tmpLayer);
                    }
                    PubVar._Map.Refresh();
                }
            } else if (paramString.contains("编辑图层字段") && Toolbar_Layers.this.m_SelectLayer != null) {
                FeatureLayer tmpLayer2 = (FeatureLayer) pObject;
                boolean tmpBool = false;
                if (!tmpLayer2.GetLayerName().equals(Toolbar_Layers.this.m_SelectLayer.getLayerName())) {
                    tmpBool = true;
                    GeoLayer tempGeoLayer2 = PubVar._Map.GetGeoLayerByName(tmpLayer2.GetLayerID());
                    if (tempGeoLayer2 != null) {
                        tempGeoLayer2.SetLayerName(tmpLayer2.GetLayerName());
                    }
                }
                Toolbar_Layers.this.m_SelectLayer.UpdateFromLayer(tmpLayer2);
                FeatureLayer tmpLayer22 = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(tmpLayer2.GetLayerID());
                if (tmpLayer22 != null) {
                    tmpLayer2.CopyTo(tmpLayer22);
                    tmpLayer22.SaveLayerInfo();
                }
                if (tmpBool) {
                    Toolbar_Layers.this.DoCommand("刷新图层列表");
                }
            }
        }
    };

    public Toolbar_Layers(Context context, MapView mapView) {
        super(context, mapView);
        this.m_ToolbarName = "图层工具栏";
        this.m_AllowdChangeOri = false;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void LoadToolBar(View view, int mainLinearLayoutID) {
        super.LoadToolBar(view, mainLinearLayoutID);
        this.buttonIDs.put("编辑", Integer.valueOf((int) R.id.bt_EditLayer));
        this.buttonIDs.put("渲染", Integer.valueOf((int) R.id.bt_EditLayerRender));
        this.buttonIDs.put("字段", Integer.valueOf((int) R.id.bt_EditLayerField));
        this.m_EditLayerBtn = (Button) this.m_view.findViewById(R.id.bt_EditLayer);
        this.m_LayersListSpinner = (SpinnerList) this.m_view.findViewById(R.id.sp_layersList);
        this.m_EditLayerFieldBtn = (Button) this.m_view.findViewById(R.id.bt_EditLayerField);
        this.m_layerVisible = (CheckBox) this.m_view.findViewById(R.id.ck_layerVisible);
        this.m_view.setOnTouchListener(this.touchListener);
        this.m_baseLayout.setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.tv_textLayer).setOnTouchListener(this.touchListener);
        this.m_LayersListSpinner.setOnTouchListener(this.touchListener);
        this.m_EditLayerBtn.setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_EditLayerRender).setOnTouchListener(this.touchListener);
        this.m_EditLayerFieldBtn.setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.buttonExit).setOnTouchListener(this.touchListener);
        this.m_layerVisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Layers.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                Toolbar_Layers.this.DoCommand("图层可见性");
            }
        });
        this.m_LayersListSpinner.SetSelectReturnTag("SelectedLayer");
        this.m_LayersListSpinner.SetCallback(this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshSelectLayer() {
        int tempIndex = this.m_LayersListSpinner.getSelectedIndex();
        if (tempIndex >= 0) {
            this.m_layerVisible.setEnabled(true);
            this.m_SelectLayer = PubVar._Map.getGeoLayers().GetLayerByName(this.layersDataList.get(tempIndex).get("D2").toString());
            this.m_layerVisible.setChecked(this.m_SelectLayer.getVisible());
            return;
        }
        this.m_layerVisible.setEnabled(false);
        this.m_SelectLayer = null;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Show() {
        super.Show();
        DoCommand("刷新图层列表");
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Hide() {
        DoCommand("直接停止编辑");
        super.Hide();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(String command) {
        boolean tempVisible;
        BaseToolbar tmpToolbar;
        Selection tmpSel;
        try {
            if (command.equals("刷新图层列表")) {
                this.layersDataList = new ArrayList();
                List<String> tempArray = new ArrayList<>();
                int tmpTid = 0;
                String tempLayerName = null;
                for (GeoLayer tmpGeoLayer : PubVar._Map.getGeoLayers().getList()) {
                    tempArray.add(tmpGeoLayer.getLayerName());
                    HashMap<String, Object> tempHash = new HashMap<>();
                    tempHash.put("D1", tmpGeoLayer.getLayerName());
                    tempHash.put("D2", tmpGeoLayer.getLayerID());
                    tempHash.put("D3", tmpGeoLayer.getGeoTypeName());
                    if (this.m_SelectLayer != null && this.m_SelectLayer.getLayerID().equals(tmpGeoLayer.getLayerID())) {
                        tempLayerName = tmpGeoLayer.getLayerName();
                    }
                    tmpTid++;
                    this.layersDataList.add(tempHash);
                }
                Common.SetSpinnerListData(PubVar.MainContext, "图层列表", tempArray, this.m_LayersListSpinner);
                if (tempLayerName != null) {
                    Common.SetValueToView(tempLayerName, this.m_LayersListSpinner);
                }
            } else if (command.equals("编辑图层")) {
                if (this.m_SelectLayer == null) {
                    Common.ShowDialog("请选择有效图层.");
                } else if (this.m_EditLayerBtn.isSelected()) {
                    DoCommand("直接停止编辑");
                } else if (this.m_SelectLayer.getVisible()) {
                    DoCommand("直接开始编辑");
                } else {
                    Common.ShowYesNoDialog(this.m_context, "图层【" + this.m_SelectLayer.getLayerName() + "】当前不可见,是否继续编辑图层?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Layers.3
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (paramString.equals("YES")) {
                                Toolbar_Layers.this.DoCommand("直接开始编辑");
                            }
                        }
                    });
                }
            } else if (command.equals("直接开始编辑")) {
                this.m_EditLayerBtn.setText("停止编辑");
                DoCommand("显示编辑工具条");
                this.m_LayersListSpinner.setEnabled(false);
                this.m_EditLayerFieldBtn.setEnabled(false);
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected(null);
                PubVar._PubCommand.ProcessCommand("自由缩放");
                SetButtonSelectedStatus("编辑", true);
                if (this.m_SelectLayer != null) {
                    Common.ShowToast("开始编辑图层【" + this.m_SelectLayer.getLayerName() + "】.");
                }
            } else if (command.equals("直接停止编辑")) {
                this.m_EditLayerBtn.setText("开始编辑");
                DoCommand("隐藏编辑工具条");
                if (!(this.m_SelectLayer == null || (tmpSel = PubVar._Map.getFeatureSelectionByLayerID(this.m_SelectLayer.getLayerID(), false)) == null)) {
                    tmpSel.RemoveAll();
                }
                this.m_LayersListSpinner.setEnabled(true);
                this.m_EditLayerFieldBtn.setEnabled(true);
                if (this.m_SelectLayer != null && this.m_EditLayerBtn.isSelected()) {
                    Common.ShowToast("停止编辑图层【" + this.m_SelectLayer.getLayerName() + "】.");
                }
                SetButtonSelectedStatus("编辑", false);
            } else if (command.equals("图层渲染")) {
                if (this.m_SelectLayer != null) {
                    GeoLayer tempGeoLayer = PubVar._Map.getGeoLayers().GetLayerByName(this.m_SelectLayer.getLayerID());
                    if (tempGeoLayer != null) {
                        IRender tmpRender = tempGeoLayer.getRender();
                        Layer_Render_Dialog tempDialog = new Layer_Render_Dialog();
                        tempDialog.SetGeoLayer(this.m_SelectLayer);
                        tempDialog.SetRender(tmpRender);
                        tempDialog.SetCallback(this.pCallback);
                        tempDialog.ShowDialog();
                        return;
                    }
                    Common.ShowDialog("请选择有效图层.");
                    return;
                }
                Common.ShowDialog("请选择有效图层.");
            } else if (command.equals("图层字段编辑")) {
                if (this.m_SelectLayer != null) {
                    FeatureLayer tmpLayer = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(this.m_SelectLayer.getLayerID());
                    if (tmpLayer != null) {
                        List<String> tempList = new ArrayList<>();
                        for (HashMap<String, Object> tempHash2 : this.layersDataList) {
                            if (!this.m_SelectLayer.getLayerID().equals(tempHash2.get("D2").toString())) {
                                tempList.add(tempHash2.get("D1").toString());
                            }
                        }
                        Layer_New_Dialog tempLayerDialog = new Layer_New_Dialog();
                        tempLayerDialog.SetHaveLayerList(tempList);
                        tempLayerDialog.SetEditLayer(tmpLayer);
                        tempLayerDialog.SetCallback(this.pCallback);
                        tempLayerDialog.ShowDialog();
                        return;
                    }
                    Common.ShowDialog("地图中没有【" + this.m_SelectLayer.getLayerName() + "】图层对象.");
                    return;
                }
                Common.ShowDialog("请选择有效图层.");
            } else if (command.equals("显示编辑工具条")) {
                if (this.m_SelectLayer == null) {
                    return;
                }
                if (this.m_SelectLayer.getType() == EGeoLayerType.POINT) {
                    PubVar._PubCommand.ShowToolbar("点编辑工具栏");
                    BaseToolbar tmpToolbar2 = PubVar._PubCommand.GetToolbarByName("点编辑工具栏");
                    if (tmpToolbar2 != null) {
                        ((Toolbar_Point) tmpToolbar2).SetEditLayerID(this.m_SelectLayer.getLayerID());
                        ((Toolbar_Point) tmpToolbar2).SetToolbar_Layers(this);
                    }
                } else if (this.m_SelectLayer.getType() == EGeoLayerType.POLYLINE) {
                    PubVar._PubCommand.ShowToolbar("线编辑工具栏");
                    BaseToolbar tmpToolbar3 = PubVar._PubCommand.GetToolbarByName("线编辑工具栏");
                    if (tmpToolbar3 != null) {
                        ((Toolbar_Polyline) tmpToolbar3).SetEditLayerID(this.m_SelectLayer.getLayerID());
                        ((Toolbar_Polyline) tmpToolbar3).SetEditLine(PubVar._PubCommand.GetLineEdit());
                        ((Toolbar_Polyline) tmpToolbar3).SetToolbar_Layers(this);
                    }
                } else if (this.m_SelectLayer.getType() == EGeoLayerType.POLYGON) {
                    PubVar._PubCommand.ShowToolbar("面编辑工具栏");
                    BaseToolbar tmpToolbar4 = PubVar._PubCommand.GetToolbarByName("面编辑工具栏");
                    if (tmpToolbar4 != null) {
                        ((Toolbar_Polygon) tmpToolbar4).SetEditLayerID(this.m_SelectLayer.getLayerID());
                        ((Toolbar_Polygon) tmpToolbar4).SetEditPolygon(PubVar._PubCommand.GetPolygonEdit());
                        ((Toolbar_Polygon) tmpToolbar4).SetToolbar_Layers(this);
                    }
                }
            } else if (command.equals("隐藏编辑工具条")) {
                if (this.m_SelectLayer.getType() == EGeoLayerType.POINT && (tmpToolbar = PubVar._PubCommand.GetToolbarByName("点编辑工具栏")) != null) {
                    ((Toolbar_Point) tmpToolbar).SetEditLayerID(null);
                }
                PubVar._PubCommand.HideToolbar("点编辑工具栏");
                PubVar._PubCommand.HideToolbar("线编辑工具栏");
                PubVar._PubCommand.HideToolbar("面编辑工具栏");
            } else if (command.equals("图层可见性") && this.m_SelectLayer != null && this.m_SelectLayer.getVisible() != (tempVisible = this.m_layerVisible.isChecked())) {
                this.m_SelectLayer.setVisible(tempVisible);
                FeatureLayer tmpLayer2 = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(this.m_SelectLayer.getLayerID());
                if (tmpLayer2 != null) {
                    tmpLayer2.SetVisible(tempVisible);
                    tmpLayer2.SaveLayerInfo();
                }
                PubVar._Map.RefreshFast();
            }
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(View view) {
        Object tempObj = view.getTag();
        if (tempObj != null) {
            String command = tempObj.toString();
            if (command.equals("关闭工具")) {
                Hide();
                SaveConfigDB();
                return;
            }
            DoCommand(command);
        }
    }
}
