package  com.xzy.forestSystem.gogisapi.Edit;

import android.content.DialogInterface;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LineUnion_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private MyTableFactory m_MyTableFactory;
    private List<HashMap<String, Object>> m_Polylines;
    private HashMap<String, Object> m_SelectObject;
    private List<HashMap<String, Object>> m_TableDataList;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public LineUnion_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_Polylines = null;
        this.m_SelectObject = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Edit.LineUnion_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                Polyline tmpPolyline;
                Polyline tmpPolyline2;
                try {
                    if (command.equals("合并")) {
                        if (LineUnion_Dialog.this.m_SelectObject != null) {
                            int tmpIndex = Integer.parseInt(String.valueOf(LineUnion_Dialog.this.m_SelectObject.get("D3")));
                            if (tmpIndex >= 0 && tmpIndex < LineUnion_Dialog.this.m_Polylines.size()) {
                                HashMap<String, Object> tmpMainHash = (HashMap) LineUnion_Dialog.this.m_Polylines.get(tmpIndex);
                                Polyline m_MainPolyline = (Polyline) tmpMainHash.get("Geometry");
                                List<Coordinate> tempCoords = new ArrayList<>();
                                tempCoords.addAll(m_MainPolyline.GetAllCoordinateList());
                                List<Integer> tmpParIndex = m_MainPolyline.getPartIndex();
                                int tmpTidCount = m_MainPolyline.GetAllCoordinateList().size();
                                int tid = -1;
                                for (HashMap<String, Object> tmpHash2 : LineUnion_Dialog.this.m_Polylines) {
                                    tid++;
                                    if (!(tid == tmpIndex || !Boolean.parseBoolean(tmpHash2.get("Editable").toString()) || (tmpPolyline2 = (Polyline) tmpHash2.get("Geometry")) == null)) {
                                        tempCoords.addAll(tmpPolyline2.GetAllCoordinateList());
                                        List<Integer> tmpParIndex2 = tmpPolyline2.getPartIndex();
                                        if (tmpParIndex2 == null || tmpParIndex2.size() <= 1) {
                                            tmpParIndex.add(Integer.valueOf(tmpTidCount));
                                            tmpTidCount += tmpPolyline2.getCoordsCount();
                                        } else {
                                            for (Integer num : tmpParIndex2) {
                                                tmpTidCount += num.intValue();
                                                tmpParIndex.add(Integer.valueOf(tmpTidCount));
                                            }
                                        }
                                    }
                                }
                                m_MainPolyline.ResetData();
                                m_MainPolyline.SetAllCoordinateList(tempCoords);
                                m_MainPolyline.SetPartIndex(tmpParIndex);
                                m_MainPolyline.SetEdited(true);
                                int tid2 = -1;
                                for (HashMap<String, Object> tmpHash22 : LineUnion_Dialog.this.m_Polylines) {
                                    tid2++;
                                    if (!(tid2 == tmpIndex || !Boolean.parseBoolean(tmpHash22.get("Editable").toString()) || (tmpPolyline = (Polyline) tmpHash22.get("Geometry")) == null)) {
                                        tmpPolyline.setStatus(EGeometryStatus.DELETE);
                                        DataSet pDataset = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmpMainHash.get("DataSource"))).GetDatasetByName(String.valueOf(tmpMainHash.get("LayerID")));
                                        if (pDataset != null) {
                                            pDataset.getDataSource().ExcuteSQL("update " + pDataset.getDataTableName() + " set SYS_STATUS=1 where SYS_ID = " + tmpPolyline.GetSYS_ID());
                                            pDataset.setEdited(true);
                                        }
                                    }
                                }
                                DataSet pDataset2 = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmpMainHash.get("DataSource"))).GetDatasetByName(String.valueOf(tmpMainHash.get("LayerID")));
                                if (pDataset2 != null) {
                                    m_MainPolyline.CalEnvelope();
                                    pDataset2.JustUpdateGeoMapIndex(m_MainPolyline);
                                    pDataset2.SaveGeoIndexToDB(m_MainPolyline);
                                    pDataset2.setEdited(true);
                                    pDataset2.RefreshTotalCount();
                                }
                                PubVar._PubCommand.ProcessCommand("选择_仅清空选择集");
                                PubVar._Map.Refresh();
                                if (LineUnion_Dialog.this.m_Callback != null) {
                                    LineUnion_Dialog.this.m_Callback.OnClick("合并线对象返回", Integer.valueOf(tmpIndex));
                                }
                            }
                            LineUnion_Dialog.this._Dialog.dismiss();
                            return;
                        }
                        Common.ShowDialog("没有选择任何对象.");
                    } else if (command.equals("单击选择行")) {
                        LineUnion_Dialog.this.m_SelectObject = (HashMap) object;
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_TableDataList = new ArrayList();
        this.m_MyTableFactory = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.x_lineunion_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("合并线对象");
        this._Dialog.SetHeadButtons("1,2130837858,合并,合并", this.pCallback);
        this.m_MyTableFactory = new MyTableFactory();
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.list_lineUnion), "自定义", "属性信息", "text", new int[]{-100}, this.pCallback);
    }

    public void SetPolylines(List<HashMap<String, Object>> polygons) {
        this.m_Polylines = polygons;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        try {
            if (this.m_Polylines != null && this.m_Polylines.size() > 1) {
                int tid = 0;
                for (HashMap<String, Object> tempHash : this.m_Polylines) {
                    if (Boolean.parseBoolean(tempHash.get("Editable").toString())) {
                        Polyline tmpGeo = (Polyline) tempHash.get("Geometry");
                        new ArrayList<>().add(Integer.valueOf(tid));
                        HashMap<String, Object> tempHash2 = new HashMap<>();
                        StringBuilder tempSB = new StringBuilder();
                        tempSB.append("图层名称:" + String.valueOf(tempHash.get("LayerName")));
                        tempSB.append("\r\n长度:" + Common.SimplifyLength(tmpGeo.getLength(true), true));
                        DataSet pDataset = PubVar.m_Workspace.GetDatasetByName(String.valueOf(tempHash.get("DataSource")), String.valueOf(tempHash.get("LayerID")));
                        if (pDataset != null) {
                            List<HashMap<String, Object>> tempFieldValuesList = pDataset.getGeometryFieldValues(tmpGeo, (List<String>) null);
                            if (tempFieldValuesList.size() > 0) {
                                for (HashMap<String, Object> tmpHash3 : tempFieldValuesList) {
                                    tempSB.append("\r\n");
                                    tempSB.append(String.valueOf(tmpHash3.get("name")));
                                    tempSB.append(":");
                                    tempSB.append(String.valueOf(tmpHash3.get("value")));
                                }
                            }
                        }
                        tempHash2.put("D2", tempSB.toString());
                        tempHash2.put("D3", Integer.valueOf(tid));
                        this.m_TableDataList.add(tempHash2);
                    }
                    tid++;
                }
                this.m_MyTableFactory.BindDataToListView(this.m_TableDataList, new String[]{"D2"}, this.pCallback, (int) (2.5d * ((double) PubVar.TextHeight20)));
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Edit.LineUnion_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                LineUnion_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
