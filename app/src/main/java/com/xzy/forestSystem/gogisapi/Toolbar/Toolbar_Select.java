package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.GraphicSymbolGeometry;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Selection;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.EGeometryType;
import  com.xzy.forestSystem.gogisapi.XProject.Layer_Select_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Toolbar_Select extends BaseToolbar {
    private String m_SelectLayers = "";
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Select.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
            if (paramString.equals("选择图层")) {
                Toolbar_Select.this.m_SelectLayers = pObject.toString();
                PubVar._MapView._Select.SetSelectLayers(Toolbar_Select.this.m_SelectLayers);
                PubVar._PubCommand.m_ProjectDB.GetProjectManage().SaveSelectionLayers(Toolbar_Select.this.m_SelectLayers);
            }
        }
    };

    public Toolbar_Select(Context context, MapView mapView) {
        super(context, mapView);
        this.m_ToolbarName = "选择工具栏";
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void LoadToolBar(View view, int mainLinearLayoutID) {
        super.LoadToolBar(view, mainLinearLayoutID);
        this.buttonIDs.put("选择", Integer.valueOf((int) R.id.buttonSelect_Select));
        this.buttonIDs.put("多选", Integer.valueOf((int) R.id.buttonMultiSelect));
        this.buttonIDs.put("属性", Integer.valueOf((int) R.id.buttonSelect_Prop));
        this.buttonIDs.put("移动", Integer.valueOf((int) R.id.buttonSelectMove));
        this.buttonIDs.put("复制", Integer.valueOf((int) R.id.buttonSelectCopy));
        this.buttonIDs.put("删除", Integer.valueOf((int) R.id.buttonSelectDelete));
        this.buttonIDs.put("清空", Integer.valueOf((int) R.id.buttonSelectClear));
        this.buttonIDs.put("筛选", Integer.valueOf((int) R.id.buttonSelectFilter));
        this.buttonIDs.put("恢复删除", Integer.valueOf((int) R.id.buttonSelect_RestoreDeleteData));
        this.buttonIDs.put("清空复制", Integer.valueOf((int) R.id.buttonSelectClearCopy));
        this.m_view.setOnTouchListener(this.touchListener);
        if (this.m_baseLayout != null) {
            this.m_baseLayout.setOnTouchListener(this.touchListener);
        }
        this.m_view.findViewById(R.id.buttonSelect_Select).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonMultiSelect).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonSelect_Prop).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonSelectClear).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonSelectMove).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonSelectDelete).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonSelectFilter).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonExit).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonSelect_RestoreDeleteData).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonSelectCopy).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonSelectClearCopy).setOnClickListener(this.buttonClickListener);
        ((Button) this.m_view.findViewById(R.id.buttonSelect_Select)).setOnLongClickListener(new View.OnLongClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Select.2
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View arg0) {
                Common.ShowYesNoDialog(Toolbar_Select.this.m_context, "是否关闭该工具栏?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Select.2.1
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        if (paramString.equals("YES")) {
                            Toolbar_Select.this.Hide();
                        }
                    }
                });
                return false;
            }
        });
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Show() {
        super.Show();
        this.m_SelectLayers = PubVar._PubCommand.m_ProjectDB.GetProjectManage().getSelectionLayers();
        PubVar._MapView._Select.SetSelectLayers(this.m_SelectLayers);
        SetButtonSelectedStatus("选择", true);
        PubVar._PubCommand.ProcessCommand("选择");
        PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("地图缩放工具栏;选择工具栏;图层工具栏");
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Hide() {
        super.Hide();
        PubVar._MapView._Select.SetSeletected(false);
        PubVar._PubCommand.ProcessCommand("选择_清空选择集");
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(View view) {
        Object tempObj = view.getTag();
        if (tempObj != null) {
            String command = tempObj.toString();
            if (command.equals("关闭工具")) {
                Hide();
                SaveConfigDB();
            } else if (command.equals("选择")) {
                if (view.isSelected()) {
                    SetButtonSelectedStatus("选择", false);
                    PubVar._PubCommand.ProcessCommand("选择_取消选择");
                } else {
                    SetButtonSelectedStatus("选择", true);
                    PubVar._PubCommand.ProcessCommand("选择");
                }
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("地图缩放工具栏;选择工具栏;图层工具栏");
            } else if (command.equals("多项选择")) {
                if (view.isSelected()) {
                    SetButtonSelectedStatus("多选", false);
                    PubVar._PubCommand.ProcessCommand("选择_取消选择");
                } else {
                    SetButtonSelectedStatus("多选", true);
                    PubVar._PubCommand.ProcessCommand("多项选择");
                }
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("地图缩放工具栏;选择工具栏;图层工具栏");
            } else if (command.equals("属性")) {
                PubVar._PubCommand.ProcessCommand("属性");
            } else if (command.equals("图层筛选")) {
                Layer_Select_Dialog tempDialog = new Layer_Select_Dialog();
                List tmpList = new ArrayList();
                tmpList.addAll(PubVar._Map.getGeoLayers().getList());
                tmpList.addAll(PubVar._Map.getVectorGeoLayers().getList());
                tempDialog.SetLayersList(tmpList);
                tempDialog.SetLayerSelectType(5);
                tempDialog.SetAllowMultiSelect(true);
                tempDialog.SetSelectedLayers(this.m_SelectLayers);
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
            } else if (command.equals("移动选择集")) {
                if (view.isSelected()) {
                    SetButtonSelectedStatus("移动", false);
                    PubVar._PubCommand.ProcessCommand("无工具");
                } else {
                    PubVar._PubCommand.ProcessCommand("移动选择");
                    SetButtonSelectedStatus("移动", true);
                }
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("选择工具栏;图层工具栏");
            } else if (command.equals("删除选择")) {
                int tempCount = PubVar._MapView._Select.GetSelectedCount();
                if (tempCount == 0) {
                    Common.ShowDialog("没有选择任何对象.");
                    return;
                }
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.m_view.getContext());
                localBuilder.setIcon(R.drawable.messageinfo);
                localBuilder.setTitle("系统提示");
                localBuilder.setMessage("共选中" + tempCount + "个对象.\n删除后将无法恢复,是否继续?");
                localBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Select.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        PubVar._PubCommand.ProcessCommand("选择_删除");
                    }
                });
                localBuilder.setNegativeButton("取消", (DialogInterface.OnClickListener) null);
                localBuilder.show();
            } else if (command.equals("清空选择")) {
                PubVar._PubCommand.ProcessCommand("选择_清空选择集");
                SetButtonSelectedStatus("移动", false);
            } else if (command.equals("恢复删除数据")) {
                Common.ShowYesNoDialog(this.m_context, "是否恢复删除的数据?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Select.4
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command2, Object pObject) {
                        if (command2.equals("YES")) {
                            String[] tmpMsgStrings = {""};
                            if (PubVar.m_Workspace.Save(tmpMsgStrings)) {
                                Common.ShowDialog("恢复成功!");
                                PubVar._Map.Refresh();
                            } else if (tmpMsgStrings[0].length() > 0) {
                                Common.ShowDialog(tmpMsgStrings[0]);
                            }
                        }
                    }
                });
            } else if (command.equals("复制选择集")) {
                if (PubVar._MapView._Select.GetSelectedCount() == 0) {
                    Common.ShowDialog("没有选择任何对象.");
                    return;
                }
                PubVar._PubCommand.ClearCopyGraphics();
                int tmpCount = 0;
                try {
                    int tid = -1;
                    for (GeoLayer tempGeoLayer : PubVar._Map.getGeoLayers().getList()) {
                        tid++;
                        Selection tmpSelSelction = PubVar._Map.getFeatureSelectionByIndex(tid, false);
                        if (tmpSelSelction != null && tmpSelSelction.getCount() > 0) {
                            DataSet pDataSet = tempGeoLayer.getDataset();
                            for (Integer num : tmpSelSelction.getGeometryIndexList()) {
                                AbstractGeometry tmpGeometry = pDataSet.GetGeometry(num.intValue());
                                if (tmpGeometry != null) {
                                    GraphicSymbolGeometry tmpGraphicSymbolGeometry = new GraphicSymbolGeometry();
                                    tmpGraphicSymbolGeometry._Geoemtry = tmpGeometry.Clone();
                                    HashMap<String, Object> tmpFieldHashMap = new HashMap<>();
                                    List<HashMap<String, Object>> tmpList2 = pDataSet.getGeometryFieldValues(tmpGeometry.GetSYS_ID(), (List<String>) null);
                                    if (tmpList2 != null && tmpList2.size() > 0) {
                                        for (HashMap<String, Object> tmphashHashMap : tmpList2) {
                                            tmpFieldHashMap.put(String.valueOf(tmphashHashMap.get("name")), String.valueOf(tmphashHashMap.get("value")));
                                        }
                                    }
                                    tmpGraphicSymbolGeometry._AttributeHashMap = tmpFieldHashMap;
                                    PubVar._PubCommand.AddCopyGraphic(tmpGraphicSymbolGeometry);
                                    tmpCount++;
                                }
                            }
                        }
                    }
                    int tid2 = -1;
                    for (GeoLayer tempGeoLayer2 : PubVar._Map.getVectorGeoLayers().getList()) {
                        tid2++;
                        Selection tmpSelSelction2 = PubVar._Map.getSelectionByIndex(tid2, false);
                        if (tmpSelSelction2 != null && tmpSelSelction2.getCount() > 0) {
                            DataSet pDataSet2 = tempGeoLayer2.getDataset();
                            for (Integer num2 : tmpSelSelction2.getGeometryIndexList()) {
                                AbstractGeometry tmpGeometry2 = pDataSet2.GetGeometry(num2.intValue());
                                if (tmpGeometry2 != null) {
                                    GraphicSymbolGeometry tmpGraphicSymbolGeometry2 = new GraphicSymbolGeometry();
                                    tmpGraphicSymbolGeometry2._Geoemtry = tmpGeometry2.Clone();
                                    HashMap<String, Object> tmpFieldHashMap2 = new HashMap<>();
                                    List<HashMap<String, Object>> tmpList3 = pDataSet2.getGeometryFieldValues(tmpGeometry2.GetSYS_ID(), (List<String>) null);
                                    if (tmpList3 != null && tmpList3.size() > 0) {
                                        for (HashMap<String, Object> tmphashHashMap2 : tmpList3) {
                                            tmpFieldHashMap2.put(String.valueOf(tmphashHashMap2.get("name")), String.valueOf(tmphashHashMap2.get("value")));
                                        }
                                    }
                                    tmpGraphicSymbolGeometry2._AttributeHashMap = tmpFieldHashMap2;
                                    PubVar._PubCommand.AddCopyGraphic(tmpGraphicSymbolGeometry2);
                                    tmpCount++;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }
                Common.ShowToast("共复制" + String.valueOf(tmpCount) + "个数据.");
                PubVar._PubCommand.ProcessCommand("选择_清空选择集");
            } else if (command.equals("清空复制")) {
                PubVar._PubCommand.ClearCopyGraphics();
                Common.ShowToast("已清空复制的图形数据.");
            }
        }
    }

    public static void PasteGeometrys(Context m_context, final GeoLayer pasteGeoLayer, final ICallback callback) {
        EGeometryType tmpGeometryType = EGeometryType.NONE;
        if (pasteGeoLayer.getType() == EGeoLayerType.POINT) {
            tmpGeometryType = EGeometryType.POINT;
        } else if (pasteGeoLayer.getType() == EGeoLayerType.POLYLINE) {
            tmpGeometryType = EGeometryType.POLYLINE;
        } else if (pasteGeoLayer.getType() == EGeoLayerType.POLYGON) {
            tmpGeometryType = EGeometryType.POLYGON;
        }
        final List<GraphicSymbolGeometry> tmpList = PubVar._PubCommand.getCopyGraphicsListByType(tmpGeometryType);
        if (tmpList.size() > 0) {
            boolean tmpIsAlert = false;
            String tmpTipMsgString = "是否确定要粘贴" + String.valueOf(tmpList.size()) + "个" + pasteGeoLayer.getGeoTypeName() + "对象到当前图层【" + pasteGeoLayer.getLayerName() + "】中?";
            int tmpHasCount = 0;
            String tmpLyrID = String.valueOf(pasteGeoLayer.getLayerID()) + ";";
            for (GraphicSymbolGeometry tmpGraphicSymbolGeometry : tmpList) {
                HashMap<String, Object> tmpHashMap = tmpGraphicSymbolGeometry._AttributeHashMap;
                if (tmpHashMap != null && tmpHashMap.size() > 0 && tmpHashMap.containsKey("HasCopyLayersID") && String.valueOf(tmpHashMap.get("HasCopyLayersID")).contains(tmpLyrID)) {
                    tmpHasCount++;
                }
            }
            if (tmpHasCount > 0) {
                tmpTipMsgString = String.valueOf(tmpTipMsgString) + "\r\n\r\n且其中" + String.valueOf(tmpHasCount) + "个对象之前已经复制到该图层中.";
                tmpIsAlert = true;
            }
            Common.ShowYesNoDialogWithAlert(m_context, tmpTipMsgString, tmpIsAlert, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Select.5
                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                public void OnClick(String command, Object pObject) {
                    if (command.equals("YES")) {
                        int tmpCount = 0;
                        List<Integer> tmpPasteSysIDList = new ArrayList<>();
                        String tmpHasCopyLayersID = String.valueOf(GeoLayer.getLayerID()) + ";";
                        for (GraphicSymbolGeometry tmpGraphicSymbolGeometry2 : tmpList) {
                            HashMap<String, Object> tmpFieldValueHashMap = new HashMap<>();
                            FeatureLayer tmpFeatureLayer = PubVar._PubCommand.m_ProjectDB.GetLayerInDataSource(GeoLayer.getDataset().getDataSource().getName(), GeoLayer.getLayerID());
                            if (tmpFeatureLayer != null) {
                                String tmpFIDNameString = String.valueOf(tmpFeatureLayer.GetFieldNamesString()) + ",";
                                HashMap<String, Object> tmpHashMap2 = tmpGraphicSymbolGeometry2._AttributeHashMap;
                                if (tmpHashMap2 == null || tmpHashMap2.size() <= 0) {
                                    tmpGraphicSymbolGeometry2._AttributeHashMap = new HashMap<>();
                                    tmpHashMap2 = tmpGraphicSymbolGeometry2._AttributeHashMap;
                                } else {
                                    for (Map.Entry<String, Object> entry : tmpHashMap2.entrySet()) {
                                        if (tmpFIDNameString.contains(String.valueOf(entry.getKey()) + ",")) {
                                            tmpFieldValueHashMap.put(tmpFeatureLayer.GetDataFieldByFieldName(entry.getKey()), entry.getValue());
                                        }
                                    }
                                }
                                int tmpI = GeoLayer.getDataset().AddNewGeometry(tmpGraphicSymbolGeometry2._Geoemtry, "复制数据", true);
                                if (tmpI > -1) {
                                    tmpPasteSysIDList.add(Integer.valueOf(tmpI));
                                    if (tmpFieldValueHashMap.size() > 0) {
                                        GeoLayer.getDataset().UpdateFieldsValue(String.valueOf(tmpI), tmpFieldValueHashMap);
                                    }
                                    if (tmpHashMap2.containsKey("HasCopyLayersID")) {
                                        String tmpString02 = String.valueOf(tmpHashMap2.get("HasCopyLayersID"));
                                        if (!tmpString02.contains(tmpHasCopyLayersID)) {
                                            tmpHasCopyLayersID = String.valueOf(tmpHasCopyLayersID) + tmpString02;
                                        }
                                    }
                                    tmpHashMap2.put("HasCopyLayersID", tmpHasCopyLayersID);
                                    tmpCount++;
                                }
                            }
                        }
                        Common.ShowToast("共成功复制" + String.valueOf(tmpCount) + "个数据.");
                        if (tmpCount > 0) {
                            GeoLayer.getDataset().RefreshTotalCount();
                            GeoLayer.getDataset().BuildMapIndex(true, true);
                            if (callback != null) {
                                callback.OnClick("粘贴数据成功", new Object[]{Integer.valueOf(tmpCount), tmpPasteSysIDList});
                            }
                            PubVar._Map.Refresh();
                        }
                    }
                }
            });
            return;
        }
        Common.ShowToast("没有复制任何对象.\r\n请先选择需要复制的对象,然后点击复制操作.");
    }
}
