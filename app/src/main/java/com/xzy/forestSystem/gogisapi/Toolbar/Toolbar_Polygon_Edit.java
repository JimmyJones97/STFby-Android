package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.Context;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Edit.PolygonSplitByPoly_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.PolygonUnion_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Clip;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.PolyDefault;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.PolySimple;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Toolbar_Polygon_Edit extends BaseToolbar {
    static final double SPLIT_BIAS = 1.0E-7d;

    public Toolbar_Polygon_Edit(Context context, MapView mapView) {
        super(context, mapView);
        this.m_ToolbarName = "面编辑附加工具栏";
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void LoadToolBar(View view, int mainLinearLayoutID) {
        super.LoadToolBar(view, mainLinearLayoutID);
        this.buttonIDs.put("移点", Integer.valueOf((int) R.id.bt_PolyEdit_MoveVertex));
        this.buttonIDs.put("加点", Integer.valueOf((int) R.id.bt_PolyEdit_AddVertex));
        this.buttonIDs.put("删点", Integer.valueOf((int) R.id.bt_PolyEdit_DelVertex));
        this.buttonIDs.put("线割", Integer.valueOf((int) R.id.bt_PolyEdit_SplitLine));
        this.buttonIDs.put("面割", Integer.valueOf((int) R.id.bt_PolyEdit_SplitPoly));
        this.buttonIDs.put("合并", Integer.valueOf((int) R.id.bt_PolyEdit_Union));
        this.m_view.setOnTouchListener(this.touchListener);
        this.m_baseLayout.setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.tv_polygon_edit).setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_MoveVertex).setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_AddVertex).setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_DelVertex).setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_SplitLine).setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_SplitPoly).setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_Union).setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_PolyEdit_Exit).setOnTouchListener(this.touchListener);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Show() {
        super.Show();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Hide() {
        super.Hide();
        PubVar._PubCommand.ProcessCommand("选择_清空选择集");
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(String command) {
        try {
            if (command.equals("移点")) {
                if (Common.GetSelectObjectsCount(PubVar._Map, -1, 2, false) == 1) {
                    PubVar._PubCommand.ProcessCommand("移动节点");
                    SetButtonSelectedStatus("移点", true);
                    return;
                }
                Common.ShowDialog("先选择一个可以编辑的面对象.");
            } else if (command.equals("加点")) {
                if (Common.GetSelectObjectsCount(PubVar._Map, -1, 2, false) == 1) {
                    PubVar._PubCommand.ProcessCommand("添加节点");
                    SetButtonSelectedStatus("加点", true);
                    return;
                }
                Common.ShowDialog("先选择一个可以编辑的面对象.");
            } else if (command.equals("删点")) {
                if (Common.GetSelectObjectsCount(PubVar._Map, -1, 2, false) == 1) {
                    PubVar._PubCommand.ProcessCommand("删除节点");
                    SetButtonSelectedStatus("删点", true);
                    return;
                }
                Common.ShowDialog("先选择一个可以编辑的面对象.");
            } else if (command.equals("线割")) {
                BasicValue tmpParam = new BasicValue();
                List<HashMap<String, Object>> tmpLineList = Common.GetSelectObjects(PubVar._Map, -1, 1, true, tmpParam);
                if (PubVar._PubCommand.GetPolygonEdit().getPointsCount() > 1) {
                    Polyline tmpPoly = new Polyline();
                    tmpPoly.SetAllCoordinateList(PubVar._PubCommand.GetPolygonEdit().GetAllCoordinates());
                    HashMap<String, Object> tmpHash = new HashMap<>();
                    tmpHash.put("LayerName", "");
                    tmpHash.put("LayerID", "");
                    tmpHash.put("DataSource", "");
                    tmpHash.put("Editable", false);
                    tmpHash.put("GeoType", 1);
                    tmpHash.put("GeoIndex", -1);
                    tmpHash.put("Geometry", tmpPoly);
                    tmpLineList.add(tmpHash);
                }
                if (tmpLineList.size() <= 0) {
                    Common.ShowDialog("请选择或勾选一条或多条分割线.");
                    return;
                }
                List<HashMap<String, Object>> tmpPolyList = Common.GetSelectObjects(PubVar._Map, -1, 2, true, tmpParam);
                if (tmpPolyList.size() <= 0) {
                    Common.ShowDialog("请在可编辑图层中选择需要进行分割的面.");
                } else if (SplitPolygonByLines(tmpPolyList, tmpLineList)) {
                    PubVar._PubCommand.ProcessCommand("选择_仅清空选择集");
                    PubVar._PubCommand.GetPolygonEdit().Clear();
                    PubVar._Map.Refresh();
                }
            } else if (command.equals("面割")) {
                BasicValue tmpParam2 = new BasicValue();
                List<HashMap<String, Object>> tmpList = Common.GetSelectObjects(PubVar._Map, -1, 2, true, tmpParam2);
                if (tmpParam2.getInt() <= 0 || tmpList.size() <= 1) {
                    Common.ShowDialog("至少选择两个面对象.");
                    return;
                }
                PolygonSplitByPoly_Dialog tempDialog = new PolygonSplitByPoly_Dialog();
                tempDialog.SetPolygons(tmpList);
                tempDialog.SetMainPolyIndex(0);
                tempDialog.ShowDialog();
            } else if (command.equals("合并")) {
                BasicValue tmpParam3 = new BasicValue();
                List<HashMap<String, Object>> tmpList2 = Common.GetSelectObjects(PubVar._Map, -1, 2, false, tmpParam3);
                if (tmpParam3.getInt() <= 0 || tmpList2.size() <= 1) {
                    Common.ShowDialog("至少选择两个可编辑的面对象.");
                    return;
                }
                PolygonUnion_Dialog tempDialog2 = new PolygonUnion_Dialog();
                tempDialog2.SetPolygons(tmpList2);
                tempDialog2.ShowDialog();
            }
        } catch (Exception e) {
        }
    }

    private boolean SplitPolygonByLines(List<HashMap<String, Object>> polyHashList, List<HashMap<String, Object>> lineList) {
        boolean result = false;
        Poly tmpMainLineObj = new PolyDefault();
        Clip.GPC_EPSILON = 2.220446049250313E-16d;
        for (HashMap<String, Object> tmpHash : lineList) {
            tmpMainLineObj = Clip.union(tmpMainLineObj, ConvertPolylineToPolyObj((Polyline) tmpHash.get("Geometry")));
        }
        for (HashMap<String, Object> tmpHash2 : polyHashList) {
            if (Boolean.parseBoolean(String.valueOf(tmpHash2.get("Editable")))) {
                Polygon tmpPoly = (Polygon) tmpHash2.get("Geometry");
                Poly tmpSplitResult = Clip.difference(PolygonSplitByPoly_Dialog.ConvertPolygonToPolyObj(tmpPoly), tmpMainLineObj);
                String tmpLayerID = String.valueOf(tmpHash2.get("LayerID"));
                DataSet pDataset = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmpHash2.get("DataSource"))).GetDatasetByName(tmpLayerID);
                if (pDataset != null) {
                    List<Polygon> tmpResultList = PolygonSplitByPoly_Dialog.ConvertPolyObjToPolygons(tmpSplitResult);
                    if (tmpResultList.size() > 0) {
                        List tmpCoordList = new ArrayList();
                        tmpCoordList.addAll(tmpResultList.get(0).GetAllCoordinateList());
                        tmpPoly.ResetData();
                        tmpPoly.SetAllCoordinateList(tmpCoordList);
                        tmpPoly.SetEdited(true);
                        pDataset.JustUpdateGeoMapIndex(tmpPoly);
                        pDataset.SaveGeoIndexToDB(tmpPoly);
                        if (tmpResultList.size() > 1) {
                            List<String> tmpFieldValues = pDataset.getGeometryFieldValues(tmpPoly.GetSYS_ID(), true);
                            int tmpJCount = tmpResultList.size();
                            for (int tmpJ = 1; tmpJ < tmpJCount; tmpJ++) {
                                DataSet.SaveNewPolygon(tmpResultList.get(tmpJ), tmpFieldValues, pDataset, tmpLayerID);
                            }
                        }
                        pDataset.setEdited(true);
                        pDataset.RefreshTotalCount();
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    private Poly ConvertPolylineToPolyObj(Polyline polyline) {
        int tempCount02;
        Poly result = new PolyDefault();
        List<Coordinate> tempCoords00 = polyline.GetAllCoordinateList();
        int tempPtnTID = 0;
        int tempCount00 = polyline.GetNumberOfParts();
        for (int i0 = 0; i0 < tempCount00; i0++) {
            new ArrayList();
            if (i0 != tempCount00 - 1) {
                tempCount02 = polyline.GetPartIndex().get(i0 + 1).intValue();
            } else {
                tempCount02 = tempCoords00.size();
            }
            if (tempCount02 - tempPtnTID > 1) {
                int tmpCount3 = tempCount02 - 1;
                for (int i = tempPtnTID; i < tmpCount3; i++) {
                    Coordinate tempStartCoord = tempCoords00.get(i);
                    Coordinate tempEndCoord = tempCoords00.get(i + 1);
                    PolySimple tmpPoly2 = new PolySimple();
                    tmpPoly2.add(tempStartCoord.getX(), tempStartCoord.getY());
                    tmpPoly2.add(tempEndCoord.getX(), tempEndCoord.getY());
                    tmpPoly2.add(tempEndCoord.getX(), tempEndCoord.getY() + 1.0E-7d);
                    tmpPoly2.add(tempStartCoord.getX(), tempStartCoord.getY() + 1.0E-7d);
                    tmpPoly2.add(tempStartCoord.getX(), tempStartCoord.getY());
                    result = (PolyDefault) Clip.union(result, tmpPoly2);
                }
            }
            tempPtnTID += tempCount02;
        }
        return result;
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
