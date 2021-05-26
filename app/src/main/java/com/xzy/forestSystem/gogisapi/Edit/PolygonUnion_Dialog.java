package  com.xzy.forestSystem.gogisapi.Edit;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Display.DrawPolygonSample;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Clip;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.PolySimple;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PolygonUnion_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private MyTableFactory m_MyTableFactory;
    private List<HashMap<String, Object>> m_Polygons;
    private HashMap<String, Object> m_SelectObject;
    private List<HashMap<String, Object>> m_TableDataList;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public PolygonUnion_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_Polygons = null;
        this.m_SelectObject = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Edit.PolygonUnion_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("合并")) {
                        if (PolygonUnion_Dialog.this.m_SelectObject != null) {
                            int tmpIndex = Integer.parseInt(String.valueOf(PolygonUnion_Dialog.this.m_SelectObject.get("D3")));
                            if (tmpIndex >= 0 && tmpIndex < PolygonUnion_Dialog.this.m_Polygons.size()) {
                                Clip.GPC_EPSILON = PubVar.Clip_EPSILON;
                                HashMap<String, Object> tmpModifyHashMap = new HashMap<>();
                                tmpModifyHashMap.put("Type", "Union");
                                List<HashMap<String, Object>> tmpListOld01 = new ArrayList<>();
                                List<HashMap<String, Object>> tmpListModify02 = new ArrayList<>();
                                List<HashMap<String, Object>> tmpListNew03 = new ArrayList<>();
                                List<HashMap<String, Object>> tmpListRemove04 = new ArrayList<>();
                                Poly tmpMainPoly = null;
                                for (HashMap<String, Object> tmpHash2 : PolygonUnion_Dialog.this.m_Polygons) {
                                    if (Boolean.parseBoolean(tmpHash2.get("Editable").toString())) {
                                        Polygon tmpPolygon = (Polygon) tmpHash2.get("Geometry");
                                        HashMap<String, Object> tmpHashMapOld = new HashMap<>();
                                        tmpHashMapOld.put("SYS_ID", tmpPolygon.GetSYS_ID());
                                        tmpHashMapOld.put("Coords", tmpPolygon.GetAllCoordinateList());
                                        tmpListOld01.add(tmpHashMapOld);
                                        if (tmpMainPoly == null) {
                                            tmpMainPoly = PolygonSplitByPoly_Dialog.ConvertPolygonToPolyObj(tmpPolygon);
                                        } else {
                                            tmpMainPoly = Clip.union(tmpMainPoly, PolygonSplitByPoly_Dialog.ConvertPolygonToPolyObj(tmpPolygon));
                                        }
                                    }
                                }
                                if (tmpMainPoly != null) {
                                    List<PolySimple> tempPolyList = PolygonSplitByPoly_Dialog.ConvertPolyObjToPolySimple(tmpMainPoly);
                                    if (tempPolyList.size() > 0) {
                                        Polygon tmpResultPoly = PolygonSplitByPoly_Dialog.ConvertPolyObjToPolygon(tempPolyList);
                                        HashMap<String, Object> tmpMainHash = (HashMap) PolygonUnion_Dialog.this.m_Polygons.get(tmpIndex);
                                        Polygon m_MainPolygon = (Polygon) tmpMainHash.get("Geometry");
                                        m_MainPolygon.ResetData();
                                        m_MainPolygon.SetAllCoordinateList(tmpResultPoly.GetAllCoordinateList());
                                        m_MainPolygon.SetPartIndex(tmpResultPoly.GetPartIndex());
                                        m_MainPolygon.SetEdited(true);
                                        HashMap<String, Object> tmpHashMapModify = new HashMap<>();
                                        tmpHashMapModify.put("SYS_ID", m_MainPolygon.GetSYS_ID());
                                        tmpHashMapModify.put("Coords", m_MainPolygon.GetAllCoordinateList());
                                        tmpListModify02.add(tmpHashMapModify);
                                        int tid = 0;
                                        for (HashMap<String, Object> tmpHash3 : PolygonUnion_Dialog.this.m_Polygons) {
                                            if (Boolean.parseBoolean(tmpHash3.get("Editable").toString()) && tmpIndex != tid) {
                                                Polygon tmpPolygon2 = (Polygon) tmpHash3.get("Geometry");
                                                tmpPolygon2.setStatus(EGeometryStatus.DELETE);
                                                DataSet pDataset = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmpMainHash.get("DataSource"))).GetDatasetByName(String.valueOf(tmpMainHash.get("LayerID")));
                                                if (pDataset != null) {
                                                    pDataset.getDataSource().ExcuteSQL("update " + pDataset.getDataTableName() + " set SYS_STATUS=1 where SYS_ID = " + tmpPolygon2.GetSYS_ID());
                                                    pDataset.setEdited(true);
                                                }
                                                HashMap<String, Object> tmpHashMapRemove = new HashMap<>();
                                                tmpHashMapRemove.put("SYS_ID", tmpPolygon2.GetSYS_ID());
                                                tmpListRemove04.add(tmpHashMapRemove);
                                            }
                                            tid++;
                                        }
                                        DataSet pDataset2 = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmpMainHash.get("DataSource"))).GetDatasetByName(String.valueOf(tmpMainHash.get("LayerID")));
                                        if (pDataset2 != null) {
                                            m_MainPolygon.CalEnvelope();
                                            pDataset2.JustUpdateGeoMapIndex(m_MainPolygon);
                                            pDataset2.SaveGeoIndexToDB(m_MainPolygon);
                                            pDataset2.setEdited(true);
                                            pDataset2.RefreshTotalCount();
                                        }
                                        PubVar._PubCommand.ProcessCommand("选择_仅清空选择集");
                                        PubVar._Map.Refresh();
                                    }
                                    tmpModifyHashMap.put("Old", tmpListOld01);
                                    tmpModifyHashMap.put("Modify", tmpListModify02);
                                    tmpModifyHashMap.put("New", tmpListNew03);
                                    tmpModifyHashMap.put("Remove", tmpListRemove04);
                                    if (PolygonUnion_Dialog.this.m_Callback != null) {
                                        PolygonUnion_Dialog.this.m_Callback.OnClick("合并面对象返回", tmpModifyHashMap);
                                    }
                                } else {
                                    Common.ShowDialog("没有选择任何可以编辑的面对象.");
                                }
                            }
                            Clip.GPC_EPSILON = 2.220446049250313E-16d;
                            PolygonUnion_Dialog.this._Dialog.dismiss();
                            return;
                        }
                        Common.ShowDialog("没有选择任何对象.");
                    } else if (command.equals("单击选择行")) {
                        PolygonUnion_Dialog.this.m_SelectObject = (HashMap) object;
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_TableDataList = new ArrayList();
        this.m_MyTableFactory = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.x_polygonunion_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("合并对象");
        this._Dialog.SetHeadButtons("1,2130837858,合并,合并", this.pCallback);
        this.m_MyTableFactory = new MyTableFactory();
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.list_polygonUnion), "自定义", "示意图,属性信息", "image,text", new int[]{-50, -50}, this.pCallback);
    }

    public void SetPolygons(List<HashMap<String, Object>> polygons) {
        this.m_Polygons = polygons;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        try {
            if (this.m_Polygons != null && this.m_Polygons.size() > 1) {
                List<Polygon> tempPolys = new ArrayList<>();
                for (HashMap<String, Object> tempHash : this.m_Polygons) {
                    if (Boolean.parseBoolean(tempHash.get("Editable").toString())) {
                        tempPolys.add((Polygon) tempHash.get("Geometry"));
                    }
                }
                int tid = 0;
                for (HashMap<String, Object> tempHash2 : this.m_Polygons) {
                    if (Boolean.parseBoolean(tempHash2.get("Editable").toString())) {
                        Polygon tmpGeo = (Polygon) tempHash2.get("Geometry");
                        List<Integer> tmpIndexList = new ArrayList<>();
                        tmpIndexList.add(Integer.valueOf(tid));
                        Bitmap tmpBmp = DrawPolygonSample.DrawPolygons(tempPolys, tmpIndexList, 400, 400);
                        HashMap<String, Object> tempHash22 = new HashMap<>();
                        tempHash22.put("D1", tmpBmp);
                        StringBuilder tempSB = new StringBuilder();
                        tempSB.append("图层名称:" + String.valueOf(tempHash2.get("LayerName")));
                        tempSB.append("\r\n面积:" + Common.SimplifyArea(tmpGeo.getArea(false), true));
                        DataSet pDataset = PubVar.m_Workspace.GetDatasetByName(String.valueOf(tempHash2.get("DataSource")), String.valueOf(tempHash2.get("LayerID")));
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
                        tempHash22.put("D2", tempSB.toString());
                        tempHash22.put("D3", Integer.valueOf(tid));
                        this.m_TableDataList.add(tempHash22);
                    }
                    tid++;
                }
                this.m_MyTableFactory.BindDataToListView(this.m_TableDataList, new String[]{"D1", "D2"}, this.pCallback);
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Edit.PolygonUnion_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                PolygonUnion_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
