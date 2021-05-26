package  com.xzy.forestSystem.gogisapi.Edit;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Display.DrawPolygonSample;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Clip;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Point2D;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.PolyDefault;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.PolySimple;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PolygonSplitByPoly_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private int m_MainPolyIndex;
    private Polygon m_MainPolygon;
    private MyTableFactory m_MyTableFactory;
    private List<HashMap<String, Object>> m_Polygons;
    private HashMap<String, Object> m_SelectObject;
    private List<HashMap<String, Object>> m_TableDataList;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public PolygonSplitByPoly_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_MainPolyIndex = -1;
        this.m_MainPolygon = null;
        this.m_Polygons = null;
        this.m_SelectObject = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Edit.PolygonSplitByPoly_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                int tmpIndex;
                try {
                    if (command.equals("确定")) {
                        if (PolygonSplitByPoly_Dialog.this.m_SelectObject != null) {
                            String tmpOPTypeIndex = PolygonSplitByPoly_Dialog.this.m_SelectObject.get("D5").toString();
                            List<Polygon> tmpPolyList = (List) PolygonSplitByPoly_Dialog.this.m_SelectObject.get("D3");
                            if (tmpPolyList.size() > 0) {
                                if (!tmpOPTypeIndex.equals("3")) {
                                    int tmpCount = Integer.parseInt(PolygonSplitByPoly_Dialog.this.m_SelectObject.get("D4").toString());
                                    if (tmpCount > 0) {
                                        HashMap<String, Object> tmpModifyHashMap = new HashMap<>();
                                        tmpModifyHashMap.put("Type", "Split");
                                        List<HashMap<String, Object>> tmpList01 = new ArrayList<>();
                                        HashMap<String, Object> tmpHashMapOld = new HashMap<>();
                                        tmpHashMapOld.put("SYS_ID", PolygonSplitByPoly_Dialog.this.m_MainPolygon.GetSYS_ID());
                                        tmpHashMapOld.put("Coords", PolygonSplitByPoly_Dialog.this.m_MainPolygon.GetAllCoordinateList());
                                        tmpList01.add(tmpHashMapOld);
                                        tmpModifyHashMap.put("Old", tmpList01);
                                        List tmpCoordList = new ArrayList();
                                        tmpCoordList.addAll(tmpPolyList.get(0).GetAllCoordinateList());
                                        PolygonSplitByPoly_Dialog.this.m_MainPolygon.ResetData();
                                        PolygonSplitByPoly_Dialog.this.m_MainPolygon.SetAllCoordinateList(tmpCoordList);
                                        PolygonSplitByPoly_Dialog.this.m_MainPolygon.SetEdited(true);
                                        List<HashMap<String, Object>> tmpList02 = new ArrayList<>();
                                        HashMap<String, Object> tmpHashMapNew = new HashMap<>();
                                        tmpHashMapNew.put("SYS_ID", PolygonSplitByPoly_Dialog.this.m_MainPolygon.GetSYS_ID());
                                        tmpHashMapNew.put("Coords", PolygonSplitByPoly_Dialog.this.m_MainPolygon.GetAllCoordinateList());
                                        tmpList02.add(tmpHashMapNew);
                                        tmpModifyHashMap.put("Modify", tmpList02);
                                        HashMap<String, Object> tmpGeoHash = (HashMap) PolygonSplitByPoly_Dialog.this.m_Polygons.get(PolygonSplitByPoly_Dialog.this.m_MainPolyIndex);
                                        String tmpLayerID = String.valueOf(tmpGeoHash.get("LayerID"));
                                        DataSet pDataset = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmpGeoHash.get("DataSource"))).GetDatasetByName(tmpLayerID);
                                        if (pDataset != null) {
                                            pDataset.JustUpdateGeoMapIndex(PolygonSplitByPoly_Dialog.this.m_MainPolygon);
                                            pDataset.SaveGeoIndexToDB(PolygonSplitByPoly_Dialog.this.m_MainPolygon);
                                        }
                                        List<HashMap<String, Object>> tmpList03 = new ArrayList<>();
                                        List<String> tmpFieldValues = pDataset.getGeometryFieldValues(PolygonSplitByPoly_Dialog.this.m_MainPolygon.GetSYS_ID(), true);
                                        for (int i = 1; i < tmpCount; i++) {
                                            Polygon tmpPoly2 = tmpPolyList.get(i);
                                            DataSet.SaveNewPolygon(tmpPoly2, tmpFieldValues, pDataset, tmpLayerID);
                                            HashMap<String, Object> tmpHashMapNew2 = new HashMap<>();
                                            tmpHashMapNew.put("SYS_ID", tmpPoly2.GetSYS_ID());
                                            tmpHashMapNew.put("Coords", tmpPoly2.GetAllCoordinateList());
                                            tmpList03.add(tmpHashMapNew2);
                                        }
                                        tmpModifyHashMap.put("New", tmpList03);
                                        pDataset.setEdited(true);
                                        pDataset.RefreshTotalCount();
                                        if (PolygonSplitByPoly_Dialog.this.m_Callback != null) {
                                            PolygonSplitByPoly_Dialog.this.m_Callback.OnClick("面割返回", tmpModifyHashMap);
                                        }
                                        PubVar._PubCommand.ProcessCommand("选择_仅清空选择集");
                                        PubVar._Map.Refresh();
                                        Clip.GPC_EPSILON = 2.220446049250313E-16d;
                                        PolygonSplitByPoly_Dialog.this._Dialog.dismiss();
                                        return;
                                    }
                                } else {
                                    List<Integer> tmpSplitIndex = (List) PolygonSplitByPoly_Dialog.this.m_SelectObject.get("D4");
                                    List<Integer> tmpOutsideList = new ArrayList<>();
                                    HashMap<String, Object> tmpModifyHashMap2 = new HashMap<>();
                                    tmpModifyHashMap2.put("Type", "Split");
                                    List<HashMap<String, Object>> tmpList012 = new ArrayList<>();
                                    List<HashMap<String, Object>> tmpList022 = new ArrayList<>();
                                    List<HashMap<String, Object>> tmpList032 = new ArrayList<>();
                                    for (HashMap<String, Object> tempHash : PolygonSplitByPoly_Dialog.this.m_Polygons) {
                                        Polygon tmpGeo = (Polygon) tempHash.get("Geometry");
                                        if (tmpGeo != null) {
                                            HashMap<String, Object> tmpHashMapOld2 = new HashMap<>();
                                            tmpHashMapOld2.put("SYS_ID", tmpGeo.GetSYS_ID());
                                            tmpHashMapOld2.put("Coords", tmpGeo.GetAllCoordinateList());
                                            tmpList012.add(tmpHashMapOld2);
                                        }
                                    }
                                    int tid = 0;
                                    for (HashMap<String, Object> tempHash02 : PolygonSplitByPoly_Dialog.this.m_Polygons) {
                                        Polygon tmpPoly = (Polygon) tempHash02.get("Geometry");
                                        int tmpGeoIndex = tmpSplitIndex.indexOf(Integer.valueOf(tid));
                                        if (tmpGeoIndex >= 0) {
                                            Polygon tmpPoly22 = tmpPolyList.get(tmpGeoIndex);
                                            if (Boolean.parseBoolean(tempHash02.get("Editable").toString())) {
                                                DataSet pDataset2 = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tempHash02.get("DataSource"))).GetDatasetByName(String.valueOf(tempHash02.get("LayerID")));
                                                if (pDataset2 != null) {
                                                    List tmpCoordList2 = new ArrayList();
                                                    tmpCoordList2.addAll(tmpPoly22.GetAllCoordinateList());
                                                    tmpPoly.ResetData();
                                                    tmpPoly.SetAllCoordinateList(tmpCoordList2);
                                                    tmpPoly.SetEdited(true);
                                                    HashMap<String, Object> tmpHashMapModify = new HashMap<>();
                                                    tmpHashMapModify.put("SYS_ID", tmpPoly.GetSYS_ID());
                                                    tmpHashMapModify.put("Coords", tmpPoly.GetAllCoordinateList());
                                                    tmpList022.add(tmpHashMapModify);
                                                    pDataset2.JustUpdateGeoMapIndex(tmpPoly);
                                                    pDataset2.SaveGeoIndexToDB(tmpPoly);
                                                }
                                            }
                                            tmpOutsideList.add(Integer.valueOf(tmpGeoIndex));
                                        }
                                        tid++;
                                    }
                                    int tid2 = 0;
                                    for (Polygon tmpPoly23 : tmpPolyList) {
                                        if (!tmpOutsideList.contains(Integer.valueOf(tid2))) {
                                            HashMap<String, Object> tmpGeoHash2 = (HashMap) PolygonSplitByPoly_Dialog.this.m_Polygons.get(tmpSplitIndex.get(tid2).intValue());
                                            if (Boolean.parseBoolean(tmpGeoHash2.get("Editable").toString())) {
                                                String tmpLayerID2 = String.valueOf(tmpGeoHash2.get("LayerID"));
                                                DataSet pDataset3 = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmpGeoHash2.get("DataSource"))).GetDatasetByName(tmpLayerID2);
                                                if (pDataset3 != null) {
                                                    DataSet.SaveNewPolygon(tmpPoly23, pDataset3.getGeometryFieldValues(((Polygon) tmpGeoHash2.get("Geometry")).GetSYS_ID(), true), pDataset3, tmpLayerID2);
                                                    HashMap<String, Object> tmpHashMapNew3 = new HashMap<>();
                                                    tmpHashMapNew3.put("SYS_ID", tmpPoly23.GetSYS_ID());
                                                    tmpHashMapNew3.put("Coords", tmpPoly23.GetAllCoordinateList());
                                                    tmpList032.add(tmpHashMapNew3);
                                                    pDataset3.setEdited(true);
                                                    pDataset3.RefreshTotalCount();
                                                }
                                            }
                                        }
                                        tid2++;
                                    }
                                    tmpModifyHashMap2.put("Old", tmpList012);
                                    tmpModifyHashMap2.put("Modify", tmpList022);
                                    tmpModifyHashMap2.put("New", tmpList032);
                                    if (PolygonSplitByPoly_Dialog.this.m_Callback != null) {
                                        PolygonSplitByPoly_Dialog.this.m_Callback.OnClick("面割返回", tmpModifyHashMap2);
                                    }
                                }
                                PubVar._PubCommand.ProcessCommand("选择_仅清空选择集");
                                PubVar._Map.Refresh();
                                Clip.GPC_EPSILON = 2.220446049250313E-16d;
                                PolygonSplitByPoly_Dialog.this._Dialog.dismiss();
                                return;
                            }
                            Common.ShowDialog("没有任何分析结果.");
                            return;
                        }
                        Common.ShowDialog("没有选择任何操作方式.");
                    } else if (command.equals("单击选择行")) {
                        PolygonSplitByPoly_Dialog.this.m_SelectObject = (HashMap) object;
                    } else if (command.equals("切换编辑面")) {
                        Polygon_SelectEditObject_Dialog tmpDialog = new Polygon_SelectEditObject_Dialog();
                        tmpDialog.SetPolygons(PolygonSplitByPoly_Dialog.this.m_Polygons);
                        tmpDialog.SetCallback(PolygonSplitByPoly_Dialog.this.pCallback);
                        tmpDialog.ShowDialog();
                    } else if (command.equals("选择编辑面对象返回") && (tmpIndex = Integer.parseInt(String.valueOf(object))) >= 0 && tmpIndex < PolygonSplitByPoly_Dialog.this.m_Polygons.size()) {
                        PolygonSplitByPoly_Dialog.this.SetMainPolyIndex(tmpIndex);
                        PolygonSplitByPoly_Dialog.this.refreshLayout();
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_TableDataList = new ArrayList();
        this.m_MyTableFactory = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.polygon_splitbypoly_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("面分割选项");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        Clip.GPC_EPSILON = PubVar.Clip_EPSILON;
        this._Dialog.findViewById(R.id.btn_polyedit_changePoly).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Edit.PolygonSplitByPoly_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                PolygonSplitByPoly_Dialog.this.pCallback.OnClick("切换编辑面", null);
            }
        });
        this.m_MyTableFactory = new MyTableFactory();
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.test_list), "自定义", "处理前,处理后", "image,image", new int[]{-50, -50}, this.pCallback);
    }

    public void SetMainPolyIndex(int index) {
        while (index < this.m_Polygons.size()) {
            try {
                HashMap temphash = this.m_Polygons.get(index);
                if (Boolean.parseBoolean(temphash.get("Editable").toString())) {
                    this.m_MainPolygon = (Polygon) temphash.get("Geometry");
                    this.m_MainPolyIndex = index;
                    return;
                }
                index++;
            } catch (Exception e) {
                return;
            }
        }
    }

    public void SetPolygons(List<HashMap<String, Object>> polygons) {
        this.m_Polygons = polygons;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        this.m_TableDataList = new ArrayList();
        try {
            if (this.m_MainPolygon != null && this.m_Polygons.size() > 1 && this.m_MainPolyIndex >= 0) {
                List<Polygon> tempPolys = new ArrayList<>();
                for (HashMap<String, Object> tempHash : this.m_Polygons) {
                    tempPolys.add((Polygon) tempHash.get("Geometry"));
                }
                Bitmap tmpLeftBmp = DrawPolygonSample.DrawUnionPolygons(tempPolys, 400, 400);
                HashMap<String, Object> tempHash2 = new HashMap<>();
                tempHash2.put("D1", tmpLeftBmp);
                Poly tmpMainPoly = ConvertPolygonToPolyObj(this.m_MainPolygon);
                int tmpTid = 0;
                for (HashMap<String, Object> tmpHash : this.m_Polygons) {
                    if (tmpTid != this.m_MainPolyIndex) {
                        tmpMainPoly = Clip.difference(tmpMainPoly, ConvertPolygonToPolyObj((Polygon) tmpHash.get("Geometry")));
                    }
                    tmpTid++;
                }
                new ArrayList();
                List tmpMainPolyIndexList = new ArrayList();
                List tmpSplitResult = ConvertPolyObjToList(tmpMainPoly);
                if (tmpSplitResult.size() > 0) {
                    int tmpCount = tmpSplitResult.size();
                    for (int i = 0; i < tmpCount; i++) {
                        tmpMainPolyIndexList.add(Integer.valueOf(i));
                    }
                }
                int tmpTid2 = 0;
                for (HashMap<String, Object> tmpHash2 : this.m_Polygons) {
                    if (tmpTid2 != this.m_MainPolyIndex) {
                        tmpSplitResult.add((Polygon) tmpHash2.get("Geometry"));
                    }
                    tmpTid2++;
                }
                tempHash2.put("D2", DrawPolygonSample.DrawPolygons(tmpSplitResult, tmpMainPolyIndexList, 400, 400));
                tempHash2.put("D3", tmpSplitResult);
                tempHash2.put("D4", Integer.valueOf(tmpMainPolyIndexList.size()));
                tempHash2.put("D5", "1");
                this.m_TableDataList.add(tempHash2);
                HashMap<String, Object> tempHash22 = new HashMap<>();
                tempHash22.put("D1", tmpLeftBmp);
                Poly tmpMainPoly2 = ConvertPolygonToPolyObj(this.m_MainPolygon);
                int tmpTid3 = 0;
                List<Poly> tmpResultList = new ArrayList();
                for (HashMap<String, Object> tmpHash3 : this.m_Polygons) {
                    if (tmpTid3 != this.m_MainPolyIndex) {
                        Poly tmpPolyObj02 = ConvertPolygonToPolyObj((Polygon) tmpHash3.get("Geometry"));
                        if (1 != 0) {
                            List tmpResultList01 = ConvertPolyObjToPolySimple(ClipPolygonByPolygon(tmpMainPoly2, tmpPolyObj02));
                            if (tmpResultList01.size() > 0) {
                                tmpResultList.addAll(tmpResultList01);
                            }
                        } else {
                            ArrayList arrayList = new ArrayList();
                            for (Poly tmpPolyObj01 : tmpResultList) {
                                List tmpResultList012 = ConvertPolyObjToPolySimple(ClipPolygonByPolygon(tmpPolyObj01, tmpPolyObj02));
                                if (tmpResultList012.size() > 0) {
                                    arrayList.addAll(tmpResultList012);
                                }
                            }
                            tmpResultList.clear();
                            tmpResultList.addAll(arrayList);
                        }
                    }
                    tmpTid3++;
                }
                List tmpSplitResult2 = new ArrayList();
                List tmpMainPolyIndexList2 = new ArrayList();
                if (tmpResultList.size() > 0) {
                    Iterator tempIter0202 = tmpResultList.iterator();
                    while (tempIter0202.hasNext()) {
                        tmpSplitResult2.add(ConvertPolyObjToPolygon((PolySimple) tempIter0202.next()));
                    }
                }
                if (tmpSplitResult2.size() > 0) {
                    int tmpCount2 = tmpSplitResult2.size();
                    for (int i2 = 0; i2 < tmpCount2; i2++) {
                        tmpMainPolyIndexList2.add(Integer.valueOf(i2));
                    }
                }
                int tmpTid4 = 0;
                for (HashMap<String, Object> tmpHash4 : this.m_Polygons) {
                    if (tmpTid4 != this.m_MainPolyIndex) {
                        tmpSplitResult2.add((Polygon) tmpHash4.get("Geometry"));
                    }
                    tmpTid4++;
                }
                tempHash22.put("D2", DrawPolygonSample.DrawPolygons(tmpSplitResult2, tmpMainPolyIndexList2, 400, 400));
                tempHash22.put("D3", tmpSplitResult2);
                tempHash22.put("D4", Integer.valueOf(tmpMainPolyIndexList2.size()));
                tempHash22.put("D5", "2");
                this.m_TableDataList.add(tempHash22);
                HashMap<String, Object> tempHash23 = new HashMap<>();
                tempHash23.put("D1", tmpLeftBmp);
                List tmpSplitTotalResult = new ArrayList();
                List<Integer> tmpSplitIndexResult = new ArrayList<>();
                Poly tmpMainPoly3 = ConvertPolygonToPolyObj(this.m_MainPolygon);
                int tmpTid5 = 0;
                for (HashMap<String, Object> tmpHash5 : this.m_Polygons) {
                    if (tmpTid5 != this.m_MainPolyIndex) {
                        Polygon tmpPolygon = (Polygon) tmpHash5.get("Geometry");
                        if (Boolean.parseBoolean(tmpHash5.get("Editable").toString())) {
                            Poly tmpPolyObj022 = ConvertPolygonToPolyObj(tmpPolygon);
                            List tmpSplitResult3 = ConvertPolyObjToList(ClipPolygonByPolygon(tmpMainPoly3, tmpPolyObj022));
                            if (tmpSplitResult3.size() > 0) {
                                tmpSplitTotalResult.addAll(tmpSplitResult3);
                                int tmpJ = tmpSplitResult3.size();
                                for (int j = 0; j < tmpJ; j++) {
                                    tmpSplitIndexResult.add(Integer.valueOf(this.m_MainPolyIndex));
                                }
                            }
                            List tmpSplitResult22 = ConvertPolyObjToList(ClipPolygonByPolygon(tmpPolyObj022, tmpMainPoly3));
                            if (tmpSplitResult22.size() > 0) {
                                tmpSplitTotalResult.addAll(tmpSplitResult22);
                                int tmpJ2 = tmpSplitResult22.size();
                                for (int j2 = 0; j2 < tmpJ2; j2++) {
                                    tmpSplitIndexResult.add(Integer.valueOf(tmpTid5));
                                }
                            }
                        }
                    }
                    tmpTid5++;
                }
                List tmpMainPolyIndexList3 = new ArrayList();
                if (tmpSplitTotalResult.size() > 0) {
                    int tmpCount3 = tmpSplitTotalResult.size();
                    for (int i3 = 0; i3 < tmpCount3; i3++) {
                        tmpMainPolyIndexList3.add(Integer.valueOf(i3));
                    }
                }
                tempHash23.put("D2", DrawPolygonSample.DrawPolygons(tmpSplitTotalResult, tmpMainPolyIndexList3, 400, 400));
                tempHash23.put("D3", tmpSplitTotalResult);
                tempHash23.put("D4", tmpSplitIndexResult);
                tempHash23.put("D5", "3");
                this.m_TableDataList.add(tempHash23);
            }
        } catch (Exception ex) {
            Common.ShowToast(ex.getLocalizedMessage());
        }
        this.m_MyTableFactory.BindDataToListView(this.m_TableDataList, new String[]{"D1", "D2"}, this.pCallback);
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Edit.PolygonSplitByPoly_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                PolygonSplitByPoly_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }

    public static Poly ClipPolygonByPolygon(Poly mainPoly, Poly secondPoly) {
        Poly tmpDiff = Clip.difference(mainPoly, secondPoly);
        Poly result = Clip.intersection(mainPoly, secondPoly);
        List tempDiffList = ConvertPolyObjToPolySimple(tmpDiff);
        if (tempDiffList.size() > 0) {
            return MergerPolyObjs(result, tempDiffList);
        }
        return result;
    }

    public static Poly ConvertPolygonToPolyObj(Polygon polygon) {
        int tempCount02;
        Poly result = null;
        List<Coordinate> tempCoords00 = polygon.GetAllCoordinateList();
        PolySimple tmpMainPolySimple = null;
        int tempPtnTID = 0;
        int tempCount00 = polygon.GetNumberOfParts();
        for (int i0 = 0; i0 < tempCount00; i0++) {
            List<Coordinate> tempCoords01 = new ArrayList<>();
            if (i0 != tempCount00 - 1) {
                tempCount02 = polygon.GetPartIndex().get(i0 + 1).intValue();
            } else {
                tempCount02 = tempCoords00.size();
            }
            if (tempPtnTID < tempCount02) {
                tempCoords01 = tempCoords00.subList(tempPtnTID, tempCount02);
            }
            tempPtnTID = tempCount02;
            if (tempCoords01.size() > 2) {
                PolySimple tmpPoly = new PolySimple();
                for (Coordinate tempCoord : tempCoords01) {
                    tmpPoly.add(tempCoord.getX(), tempCoord.getY());
                }
                if (tmpMainPolySimple == null) {
                    tmpMainPolySimple = tmpPoly;
                    result = tmpPoly;
                } else if (polygon.getBorderLine().IsContainPoint(tempCoords01.get(0), true)) {
                    result = Clip.difference(result, tmpPoly);
                } else {
                    result = Clip.union(result, tmpPoly);
                }
            }
        }
        return result;
    }

    public static Polygon ConvertPolyObjToPolygon(PolySimple polyObj) {
        Polygon result = new Polygon();
        for (Point2D tmpPoint : polyObj.getList()) {
            result.GetAllCoordinateList().add(new Coordinate(tmpPoint.getX(), tmpPoint.getY()));
        }
        return result;
    }

    public static Polygon ConvertPolyObjToPolygon(List<PolySimple> polyObjs) {
        Polygon result = new Polygon();
        for (PolySimple polyObj : polyObjs) {
            List<Coordinate> tmpCoordList = new ArrayList<>();
            for (Point2D tmpPoint : polyObj.getList()) {
                tmpCoordList.add(new Coordinate(tmpPoint.getX(), tmpPoint.getY()));
            }
            if (tmpCoordList.size() > 0) {
                result.AddPart(tmpCoordList);
            }
        }
        return result;
    }

    public static List<Polygon> ConvertPolyObjToPolygons(Poly polyObj) {
        List<Polygon> result = new ArrayList<>();
        if (polyObj instanceof PolySimple) {
            Polygon tmpPolygon = new Polygon();
            for (Point2D tmpPoint : ((PolySimple) polyObj).getList()) {
                tmpPolygon.GetAllCoordinateList().add(new Coordinate(tmpPoint.getX(), tmpPoint.getY()));
            }
            result.add(tmpPolygon);
        } else if (polyObj instanceof PolyDefault) {
            PolyDefault georesult = (PolyDefault) polyObj;
            int count = georesult.getList().size();
            for (int i = 0; i < count; i++) {
                Poly tmpP = (Poly) georesult.getList().get(i);
                if (tmpP instanceof PolyDefault) {
                    List<PolySimple> result2 = ConvertPolyObjToPolySimple((PolyDefault) tmpP);
                    if (result2.size() > 0) {
                        Polygon tmpPolygon2 = new Polygon();
                        int tid = 0;
                        List<Integer> tempIntList = new ArrayList<>();
                        List<Coordinate> tempCoordsList = new ArrayList<>();
                        for (PolySimple tmpPolyObj01 : result2) {
                            for (Point2D tmpPtn : tmpPolyObj01.getList()) {
                                tempCoordsList.add(new Coordinate(tmpPtn.getX(), tmpPtn.getY()));
                                tid++;
                            }
                            tempIntList.add(Integer.valueOf(tid));
                        }
                        tmpPolygon2.SetAllCoordinateList(tempCoordsList);
                        tmpPolygon2.SetPartIndex(tempIntList);
                        if (tmpPolygon2.getCoordsCount() > 0) {
                            result.add(tmpPolygon2);
                        }
                    }
                } else if (tmpP instanceof PolySimple) {
                    Polygon tmpPolygon3 = ConvertPolyObjToPolygon((PolySimple) tmpP);
                    if (tmpPolygon3.getCoordsCount() > 0) {
                        result.add(tmpPolygon3);
                    }
                }
            }
        }
        return result;
    }

    private static Poly MergerPolyObjs(Poly polyObj, List<PolySimple> list) {
        Poly result = null;
        if (polyObj instanceof PolyDefault) {
            List<PolySimple> tmpList2 = ConvertPolyObjToPolySimple(polyObj);
            if (tmpList2.size() > 0) {
                result = new PolyDefault();
                for (Poly poly : list) {
                    result.add(poly);
                }
                for (Poly poly2 : tmpList2) {
                    result.add(poly2);
                }
            }
        } else if (polyObj instanceof PolySimple) {
            result = new PolyDefault();
            PolyDefault tmpPolyDefault = new PolyDefault();
            tmpPolyDefault.add(polyObj);
            for (PolySimple polySimple : list) {
                tmpPolyDefault.add(polySimple);
            }
            result.add(tmpPolyDefault);
        }
        return result;
    }

    public static List<PolySimple> ConvertPolyObjToPolySimple(Poly polyObj) {
        List<PolySimple> result = new ArrayList<>();
        if (polyObj instanceof PolyDefault) {
            PolyDefault georesult = (PolyDefault) polyObj;
            if (georesult != null) {
                int count = georesult.getList().size();
                for (int i = 0; i < count; i++) {
                    Poly tmpP = (Poly) georesult.getList().get(i);
                    if (tmpP instanceof PolyDefault) {
                        List<PolySimple> result2 = ConvertPolyObjToPolySimple((PolyDefault) tmpP);
                        if (result2.size() > 0) {
                            result.addAll(result2);
                        }
                    } else if (tmpP instanceof PolySimple) {
                        PolySimple tmpP3 = (PolySimple) tmpP;
                        if (IsConsidePolygon(tmpP3)) {
                            result.add(tmpP3);
                        }
                    }
                }
            }
        } else if (polyObj instanceof PolySimple) {
            result.add((PolySimple) polyObj);
        }
        return result;
    }

    public static List<Coordinate> ConvertPolySimleToList(PolySimple polySimple) {
        List<Coordinate> result = new ArrayList<>();
        if (polySimple.getNumPoints() > 2) {
            for (Point2D tmpPoint : polySimple.getList()) {
                result.add(new Coordinate(tmpPoint.getX(), tmpPoint.getY()));
            }
        }
        return result;
    }

    public static void ConvertPolyDefaultObjToPolgon(PolyDefault polyDefault, List<Coordinate> returnCoordinates, List<Integer> returnPartsList) {
        if (polyDefault != null) {
            int count = polyDefault.getList().size();
            if (count == 1) {
                Poly tmpP = (Poly) polyDefault.getList().get(0);
                if (tmpP instanceof PolyDefault) {
                    ConvertPolyDefaultObjToPolgon((PolyDefault) tmpP, returnCoordinates, returnPartsList);
                } else if (tmpP instanceof PolySimple) {
                    PolySimple tmpP3 = (PolySimple) tmpP;
                    if (IsConsidePolygon(tmpP3)) {
                        List<Coordinate> tmpList01 = ConvertPolySimleToList(tmpP3);
                        if (tmpList01.size() > 2) {
                            returnCoordinates.addAll(tmpList01);
                            returnPartsList.add(Integer.valueOf(returnCoordinates.size()));
                        }
                    }
                }
            } else {
                for (int i = 0; i < count; i++) {
                    Poly tmpP2 = (Poly) polyDefault.getList().get(i);
                    if (!(tmpP2 instanceof PolyDefault) && (tmpP2 instanceof PolySimple)) {
                        PolySimple tmpP32 = (PolySimple) tmpP2;
                        if (IsConsidePolygon(tmpP32)) {
                            List<Coordinate> tmpList012 = ConvertPolySimleToList(tmpP32);
                            if (tmpList012.size() > 2) {
                                returnCoordinates.addAll(tmpList012);
                                returnPartsList.add(Integer.valueOf(returnCoordinates.size()));
                            }
                        }
                    }
                }
            }
        }
    }

    public static Polygon ConvertPolyObjToPolgon(Poly polyObj) {
        List<Coordinate> tmpCoordsList = new ArrayList<>();
        List<Integer> tmpPartsIndexList = new ArrayList<>();
        tmpPartsIndexList.add(0);
        if (polyObj instanceof PolyDefault) {
            PolyDefault georesult = (PolyDefault) polyObj;
            if (georesult != null) {
                int count = georesult.getList().size();
                if (count == 1) {
                    Poly tmpP = (Poly) georesult.getList().get(0);
                    if (tmpP instanceof PolyDefault) {
                        ConvertPolyDefaultObjToPolgon((PolyDefault) tmpP, tmpCoordsList, tmpPartsIndexList);
                    } else if (tmpP instanceof PolySimple) {
                        PolySimple tmpP3 = (PolySimple) tmpP;
                        if (IsConsidePolygon(tmpP3)) {
                            List<Coordinate> tmpList01 = ConvertPolySimleToList(tmpP3);
                            if (tmpList01.size() > 2) {
                                tmpCoordsList.addAll(tmpList01);
                                tmpPartsIndexList.add(Integer.valueOf(tmpCoordsList.size()));
                            }
                        }
                    }
                } else {
                    for (int i = 0; i < count; i++) {
                        Poly tmpP2 = (Poly) georesult.getList().get(i);
                        if (tmpP2 instanceof PolyDefault) {
                            ConvertPolyDefaultObjToPolgon((PolyDefault) tmpP2, tmpCoordsList, tmpPartsIndexList);
                        } else if (tmpP2 instanceof PolySimple) {
                            PolySimple tmpP32 = (PolySimple) tmpP2;
                            if (IsConsidePolygon(tmpP32)) {
                                List<Coordinate> tmpList012 = ConvertPolySimleToList(tmpP32);
                                if (tmpList012.size() > 2) {
                                    tmpCoordsList.addAll(tmpList012);
                                    tmpPartsIndexList.add(Integer.valueOf(tmpCoordsList.size()));
                                }
                            }
                        }
                    }
                }
            }
        } else if (polyObj instanceof PolySimple) {
            PolySimple tmpP33 = (PolySimple) polyObj;
            if (IsConsidePolygon(tmpP33)) {
                List<Coordinate> tmpList013 = ConvertPolySimleToList(tmpP33);
                if (tmpList013.size() > 2) {
                    tmpCoordsList.addAll(tmpList013);
                    tmpPartsIndexList.add(Integer.valueOf(tmpCoordsList.size()));
                }
            }
        }
        if (tmpCoordsList.size() <= 2) {
            return null;
        }
        Polygon resultPolygon = new Polygon();
        resultPolygon.SetAllCoordinateList(tmpCoordsList);
        resultPolygon.SetPartIndex(tmpPartsIndexList);
        return resultPolygon;
    }

    public static List<Polygon> ConvertPolyObjToList(Poly polyObj) {
        List<Polygon> result = new ArrayList<>();
        if (polyObj instanceof PolyDefault) {
            PolyDefault georesult = (PolyDefault) polyObj;
            if (georesult != null) {
                int count = georesult.getList().size();
                for (int i = 0; i < count; i++) {
                    Poly tmpP = (Poly) georesult.getList().get(i);
                    if (tmpP instanceof PolyDefault) {
                        List<Polygon> result2 = ConvertPolyObjToList((PolyDefault) tmpP);
                        if (result2.size() > 0) {
                            result.addAll(result2);
                        }
                    } else if (tmpP instanceof PolySimple) {
                        PolySimple tmpP3 = (PolySimple) tmpP;
                        if (IsConsidePolygon(tmpP3)) {
                            result.add(ConvertPolyObjToPolygon(tmpP3));
                        }
                    }
                }
            }
        } else if (polyObj instanceof PolySimple) {
            PolySimple tmpP32 = (PolySimple) polyObj;
            if (IsConsidePolygon(tmpP32)) {
                result.add(ConvertPolyObjToPolygon(tmpP32));
            }
        }
        return result;
    }

    public static boolean IsConsidePolygon(PolySimple polygon) {
        if (polygon.getNumPoints() > 3) {
            int count = polygon.getNumPoints();
            double tmpX = polygon.getX(0);
            double tmpY = polygon.getY(0);
            int i = 1;
            while (i < count) {
                double tmpX2 = polygon.getX(i);
                double tmpY2 = polygon.getY(i);
                if (((tmpX - tmpX2) * (tmpX - tmpX2)) + ((tmpY - tmpY2) * (tmpY - tmpY2)) < Clip.GPC_EPSILON) {
                    polygon.removeAt(i - 1);
                    i--;
                    count--;
                }
                tmpX = tmpX2;
                tmpY = tmpY2;
                i++;
            }
            if (polygon.getNumPoints() > 2) {
                double tmpX22 = polygon.getX(0);
                double tmpY22 = polygon.getY(0);
                if (((tmpX - tmpX22) * (tmpX - tmpX22)) + ((tmpY - tmpY22) * (tmpY - tmpY22)) < Clip.GPC_EPSILON) {
                    polygon.removeAt(i - 1);
                }
            }
        }
        if (polygon.getNumPoints() <= 2 || polygon.getArea() <= Clip.GPC_EPSILON * 10.0d) {
            return false;
        }
        return true;
    }
}
