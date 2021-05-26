package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.content.DialogInterface;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoGroupLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import  com.xzy.forestSystem.gogisapi.Carto.Map;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.XProject.Layer_Select_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Data_MultiSelect_Dialog {
    private XDialogTemplate _Dialog;
    private ArrayList _ListDataArray;
    private List<GeoLayer> _SelectLayers;
    private ICallback m_Callback;
    private int m_CurrentModifyRowIndex;
    private Map m_Map;
    private ICallback pCallback;
    private MyTableFactory tmpTableFactory;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public Data_MultiSelect_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_Map = PubVar._Map;
        this.m_CurrentModifyRowIndex = -1;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.Data_MultiSelect_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                try {
                    if (paramString.equals("导出")) {
                        if (Data_MultiSelect_Dialog.this._SelectLayers.size() <= 0) {
                            Common.ShowDialog("没有任何选择图层对象.");
                        } else if (Data_MultiSelect_Dialog.this._SelectLayers.size() == 1) {
                            List<String> tmpGeoSYSIDArray = new ArrayList<>();
                            GeoLayer tempGeoLayer = (GeoLayer) Data_MultiSelect_Dialog.this._SelectLayers.get(0);
                            Selection tmpSelection = Data_MultiSelect_Dialog.this.m_Map.getSelectionByLayerID(tempGeoLayer.getLayerID(), false);
                            if (tmpSelection != null && tmpSelection.getCount() > 0) {
                                for (Integer num : tmpSelection.getGeometryIndexList()) {
                                    AbstractGeometry tmpGeo = tempGeoLayer.getDataset().GetGeometry(num.intValue());
                                    if (tmpGeo != null) {
                                        tmpGeoSYSIDArray.add(tmpGeo.GetSYS_ID());
                                    }
                                }
                            }
                            if (tmpGeoSYSIDArray.size() > 0) {
                                String tempOutIDS = Common.CombineStrings(",", tmpGeoSYSIDArray);
                                DataExportOfSel_Dialog tempDialog = new DataExportOfSel_Dialog();
                                tempDialog.SetLayerID(tempGeoLayer.getName(), tempGeoLayer.getDataset().getDataSource().getName());
                                tempDialog.SetExportDataSYSID(tempOutIDS);
                                tempDialog.ShowDialog();
                                return;
                            }
                            Common.ShowDialog("图层中没有任何选择的对象.");
                        } else {
                            List<Integer> tempTagValues = new ArrayList<>();
                            for (GeoLayer tmpGeoLayer : Data_MultiSelect_Dialog.this._SelectLayers) {
                                Selection tmpSelection2 = Data_MultiSelect_Dialog.this.m_Map.getSelectionByLayerID(tmpGeoLayer.getLayerID(), false);
                                if (tmpSelection2 != null && tmpSelection2.getCount() > 0) {
                                    tempTagValues.add(Integer.valueOf(tmpSelection2.getCount()));
                                }
                            }
                            Layer_Select_Dialog tempDialog2 = new Layer_Select_Dialog();
                            tempDialog2.SetLayersList(Data_MultiSelect_Dialog.this._SelectLayers);
                            tempDialog2.SetLayerSelectType(1);
                            tempDialog2.SetTagObject(tempTagValues);
                            tempDialog2.SetCallback(Data_MultiSelect_Dialog.this.pCallback);
                            tempDialog2.ShowDialog();
                        }
                    } else if (paramString.equals("选择图层")) {
                        String tmpLayerID = pObject.toString();
                        if (!tmpLayerID.equals("")) {
                            GeoLayer tempGeoLayer2 = null;
                            Iterator<GeoLayer> tempIter01 = Data_MultiSelect_Dialog.this._SelectLayers.iterator();
                            while (true) {
                                if (!tempIter01.hasNext()) {
                                    break;
                                }
                                GeoLayer tempGeoLayer22 = tempIter01.next();
                                if (tempGeoLayer22.getName().equals(tmpLayerID)) {
                                    tempGeoLayer2 = tempGeoLayer22;
                                    break;
                                }
                            }
                            if (tempGeoLayer2 != null) {
                                List<String> tmpGeoSYSIDArray2 = new ArrayList<>();
                                Selection tmpSelection3 = Data_MultiSelect_Dialog.this.m_Map.getSelectionByLayerID(tempGeoLayer2.getLayerID(), false);
                                if (tmpSelection3 != null && tmpSelection3.getCount() > 0) {
                                    for (Integer num2 : tmpSelection3.getGeometryIndexList()) {
                                        AbstractGeometry tmpGeo2 = tempGeoLayer2.getDataset().GetGeometry(num2.intValue());
                                        if (tmpGeo2 != null) {
                                            tmpGeoSYSIDArray2.add(tmpGeo2.GetSYS_ID());
                                        }
                                    }
                                }
                                if (tmpGeoSYSIDArray2.size() > 0) {
                                    String tempOutIDS2 = Common.CombineStrings(",", tmpGeoSYSIDArray2);
                                    DataExportOfSel_Dialog tempDialog3 = new DataExportOfSel_Dialog();
                                    tempDialog3.SetLayerID(tempGeoLayer2.getName(), tempGeoLayer2.getDataset().getDataSource().getName());
                                    tempDialog3.SetExportDataSYSID(tempOutIDS2);
                                    tempDialog3.ShowDialog();
                                    return;
                                }
                                Common.ShowDialog("图层中没有任何选择的对象.");
                            }
                        }
                    } else if (paramString.equals("列表选项")) {
                        HashMap tmpHashMap = (HashMap) pObject;
                        Data_MultiSelect_Dialog.this.m_CurrentModifyRowIndex = Integer.parseInt(String.valueOf(tmpHashMap.get("D6")));
                        GeoLayer tempGeoLayer3 = (GeoLayer) tmpHashMap.get("D5");
                        if (tempGeoLayer3 != null) {
                            AbstractGeometry tempGeo = tempGeoLayer3.getDataset().GetGeometryBySYSID(tmpHashMap.get("ObjectID").toString());
                            if (tempGeo != null) {
                                Data_MultiSelect_Dialog.this.ModifyAttribute(tempGeoLayer3.getLayerID(), String.valueOf(tempGeo.getIndex()), String.valueOf(tmpHashMap.get("D4")));
                            } else {
                                Common.ShowDialog("几何对象不存在.");
                            }
                        }
                    } else if (paramString.equals("编辑属性数据返回") || paramString.equals("编辑属性返回")) {
                        if (Data_MultiSelect_Dialog.this._ListDataArray != null && Data_MultiSelect_Dialog.this.m_CurrentModifyRowIndex >= 0) {
                            HashMap tmpHashMap2 = (HashMap) Data_MultiSelect_Dialog.this._ListDataArray.get(Data_MultiSelect_Dialog.this.m_CurrentModifyRowIndex);
                            tmpHashMap2.put("D3", Common.GetGeometryLabelContent(String.valueOf(tmpHashMap2.get("LayerID")), String.valueOf(tmpHashMap2.get("D4")), String.valueOf(tmpHashMap2.get("ObjectID"))));
                            Data_MultiSelect_Dialog.this.tmpTableFactory.notifyDataSetChanged();
                        }
                        if (Data_MultiSelect_Dialog.this.m_Map != null) {
                            Data_MultiSelect_Dialog.this.m_Map.RefreshFast();
                        }
                        Data_MultiSelect_Dialog.this.m_CurrentModifyRowIndex = -1;
                    }
                } catch (Exception e) {
                }
            }
        };
        this._SelectLayers = new ArrayList();
        this.tmpTableFactory = new MyTableFactory();
        this._ListDataArray = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.data_multiselect_dialog);
        this._Dialog.Resize(1.0f, PubVar.DialogHeightRatio);
        this._Dialog.SetCaption("属性列表");
        this._Dialog.SetHeadButtons("1,2130837673,导出,导出", this.pCallback);
    }

    public void setMap(Map map) {
        this.m_Map = map;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void LoadSelectObjectListInfo() {
        this._SelectLayers = new ArrayList();
        this._Dialog.findViewById(R.id.prj_multiSelectlist);
        this.tmpTableFactory = new MyTableFactory();
        this.tmpTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.prj_multiSelectlist), "自定义", "图层,类型,属性", "text,text,text", new int[]{-30, -15, -55}, this.pCallback);
        this._ListDataArray = new ArrayList();
        readFromGeoLayers(this.m_Map.getGeoLayers(), this._ListDataArray);
        readFromGeoLayers(this.m_Map.getVectorGeoLayers(), this._ListDataArray);
        this.tmpTableFactory.BindDataToListView(this._ListDataArray);
    }

    private void readFromGeoLayers(GeoGroupLayer pGeoLayers, ArrayList localArray) {
        int tid = localArray.size();
        for (GeoLayer tempGeoLayer : pGeoLayers.getList()) {
            Selection tmpSelection = this.m_Map.getSelectionByLayerID(tempGeoLayer.getLayerID(), false);
            if (tmpSelection != null && tmpSelection.getCount() > 0) {
                this._SelectLayers.add(tempGeoLayer);
                String tmpDataSourceName = tempGeoLayer.getDataset().getDataSource().getName();
                FeatureLayer tempLayer = PubVar._PubCommand.m_ProjectDB.GetLayerInDataSource(tmpDataSourceName, tempGeoLayer.getName());
                if (tempLayer.GetIfLabel()) {
                    for (Integer num : tmpSelection.getGeometryIndexList()) {
                        AbstractGeometry tmpGeo = tempGeoLayer.getDataset().GetGeometry(num.intValue());
                        if (tmpGeo != null) {
                            HashMap tmpHashMap = new HashMap();
                            tmpHashMap.put("LayerID", tempLayer.GetLayerID());
                            tmpHashMap.put("ObjectID", tmpGeo.GetSYS_ID());
                            tmpHashMap.put("D1", tempLayer.GetLayerName());
                            tmpHashMap.put("D2", tempLayer.GetLayerTypeName());
                            tmpHashMap.put("D3", tmpGeo.getLabelContent());
                            tmpHashMap.put("D4", tmpDataSourceName);
                            tmpHashMap.put("D5", tempGeoLayer);
                            tmpHashMap.put("D6", Integer.valueOf(tid));
                            localArray.add(tmpHashMap);
                            tid++;
                        }
                    }
                } else {
                    List<String> tmpGeoSYSIDArray = new ArrayList<>();
                    for (Integer num2 : tmpSelection.getGeometryIndexList()) {
                        AbstractGeometry tmpGeo2 = tempGeoLayer.getDataset().GetGeometry(num2.intValue());
                        if (tmpGeo2 != null) {
                            tmpGeoSYSIDArray.add(tmpGeo2.GetSYS_ID());
                        }
                    }
                    List<String> tempFieldsArray = new ArrayList<>();
                    List<String> tempDataFieldsArray = new ArrayList<>();
                    if (tempLayer != null) {
                        for (LayerField tempLayerField : tempLayer.GetFieldList()) {
                            tempDataFieldsArray.add(tempLayerField.GetDataFieldName());
                            tempFieldsArray.add(tempLayerField.GetFieldName());
                        }
                    }
                    SQLiteReader localSQLiteDataReader = tempGeoLayer.getDataset().getDataSource().Query(String.format("select SYS_ID,%1$s from %2$s  where (SYS_ID) in (%3$s)", Common.CombineStrings(",", tempDataFieldsArray), tempGeoLayer.getDataset().getDataTableName(), Common.CombineStrings(",", tmpGeoSYSIDArray)));
                    if (localSQLiteDataReader != null) {
                        while (localSQLiteDataReader.Read()) {
                            ArrayList tempFieldsValue = new ArrayList();
                            Iterator<String> tempIter04 = tempDataFieldsArray.iterator();
                            Iterator<String> tempIter05 = tempFieldsArray.iterator();
                            while (tempIter04.hasNext() && tempIter05.hasNext() && tempFieldsValue.size() <= 2) {
                                String tempFieldName = tempIter05.next();
                                String tempFieldValue = localSQLiteDataReader.GetString(tempIter04.next());
                                if (tempFieldValue != null && !tempFieldValue.equals("")) {
                                    tempFieldsValue.add(String.valueOf(tempFieldName) + "=" + tempFieldValue);
                                }
                            }
                            int tempObjID = localSQLiteDataReader.GetInt32(0);
                            HashMap tmpHashMap2 = new HashMap();
                            tmpHashMap2.put("LayerID", tempLayer.GetLayerID());
                            tmpHashMap2.put("ObjectID", Integer.valueOf(tempObjID));
                            tmpHashMap2.put("D1", tempLayer.GetLayerName());
                            tmpHashMap2.put("D2", tempLayer.GetLayerTypeName());
                            tmpHashMap2.put("D3", Common.CombineStrings(",", tempFieldsValue));
                            tmpHashMap2.put("D4", tmpDataSourceName);
                            tmpHashMap2.put("D5", tempGeoLayer);
                            localArray.add(tmpHashMap2);
                            tmpHashMap2.put("D6", Integer.valueOf(tid));
                            tid++;
                        }
                        localSQLiteDataReader.Close();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void ModifyAttribute(String layerID, String geoIndex, String datasourceName) {
        BasicValue layerIDParam = new BasicValue();
        BasicValue geoIndexParam = new BasicValue();
        BasicValue dataSourceNameParam = new BasicValue();
        layerIDParam.setValue(layerID);
        geoIndexParam.setValue(geoIndex);
        dataSourceNameParam.setValue(datasourceName);
        if (PubVar.m_Workspace.GetDataSourceByName(dataSourceNameParam.getString()) != null) {
            FeatureAttribute_Dialog tmpFeatAttrDialog = new FeatureAttribute_Dialog();
            tmpFeatAttrDialog.SetGeometryIndex(layerIDParam.getString(), geoIndexParam.getInt(), dataSourceNameParam.getString());
            tmpFeatAttrDialog.SetCallback(this.pCallback);
            tmpFeatAttrDialog.ShowDialog();
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.Data_MultiSelect_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Data_MultiSelect_Dialog.this.LoadSelectObjectListInfo();
            }
        });
        this._Dialog.show();
    }
}
