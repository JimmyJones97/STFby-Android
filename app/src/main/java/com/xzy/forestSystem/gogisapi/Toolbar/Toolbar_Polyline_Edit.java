package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.Context;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Edit.LineUnion_Dialog;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.SpatialRelation;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Toolbar_Polyline_Edit extends BaseToolbar {
    static final double SPLIT_BIAS = 1.0E-7d;

    public Toolbar_Polyline_Edit(Context context, MapView mapView) {
        super(context, mapView);
        this.m_ToolbarName = "线编辑附加工具栏";
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void LoadToolBar(View view, int mainLinearLayoutID) {
        super.LoadToolBar(view, mainLinearLayoutID);
        this.buttonIDs.put("移点", Integer.valueOf((int) R.id.bt_lineEdit_MoveVertex));
        this.buttonIDs.put("加点", Integer.valueOf((int) R.id.bt_lineEdit_AddVertex));
        this.buttonIDs.put("删点", Integer.valueOf((int) R.id.bt_lineEdit_DelVertex));
        this.buttonIDs.put("打断", Integer.valueOf((int) R.id.bt_lineEdit_SplitLine));
        this.buttonIDs.put("转向", Integer.valueOf((int) R.id.bt_lineEdit_Change));
        this.buttonIDs.put("连接", Integer.valueOf((int) R.id.bt_lineEdit_Union));
        this.m_view.setOnTouchListener(this.touchListener);
        this.m_baseLayout.setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.tv_line_edit).setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_lineEdit_MoveVertex).setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_lineEdit_AddVertex).setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_lineEdit_DelVertex).setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_lineEdit_SplitLine).setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_lineEdit_Change).setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_lineEdit_Union).setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.bt_lineEdit_Exit).setOnTouchListener(this.touchListener);
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
                if (Common.GetSelectObjectsCount(PubVar._Map, -1, 1, false) == 1) {
                    PubVar._PubCommand.ProcessCommand("移动节点");
                    SetButtonSelectedStatus("移点", true);
                    return;
                }
                Common.ShowDialog("先选择一个可以编辑的线对象.");
            } else if (command.equals("加点")) {
                if (Common.GetSelectObjectsCount(PubVar._Map, -1, 1, false) == 1) {
                    PubVar._PubCommand.ProcessCommand("添加节点");
                    SetButtonSelectedStatus("加点", true);
                    return;
                }
                Common.ShowDialog("先选择一个可以编辑的线对象.");
            } else if (command.equals("删点")) {
                if (Common.GetSelectObjectsCount(PubVar._Map, -1, 1, false) == 1) {
                    PubVar._PubCommand.ProcessCommand("删除节点");
                    SetButtonSelectedStatus("删点", true);
                    return;
                }
                Common.ShowDialog("先选择一个可以编辑的线对象.");
            } else if (command.equals("线打断")) {
                BasicValue tmpParam = new BasicValue();
                List<HashMap<String, Object>> tmpList = Common.GetSelectObjects(PubVar._Map, -1, 1, false, tmpParam);
                if (tmpParam.getInt() <= 0 || tmpList.size() != 1) {
                    Common.ShowDialog("请在可编辑图层中选择需要打断的一条线.");
                } else if (PubVar._PubCommand.GetLineEdit().getPointsCount() > 1) {
                    HashMap<String, Object> tmphash = tmpList.get(0);
                    Polyline tmpMainPolyline = (Polyline) tmphash.get("Geometry");
                    List<Polyline> tmpSplitResult = SplitPolyline(tmpMainPolyline.GetAllCoordinateList(), PubVar._PubCommand.GetLineEdit().GetAllCoordinates());
                    if (tmpSplitResult.size() > 0) {
                        tmpMainPolyline.ResetData();
                        List<Coordinate> tmpCoordList01 = new ArrayList<>();
                        tmpCoordList01.addAll(tmpSplitResult.get(0).GetAllCoordinateList());
                        tmpMainPolyline.SetAllCoordinateList(tmpCoordList01);
                        tmpMainPolyline.SetEdited(true);
                        String tmpLayerID = String.valueOf(tmphash.get("LayerID"));
                        DataSet pDataset = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmphash.get("DataSource"))).GetDatasetByName(tmpLayerID);
                        if (pDataset != null) {
                            pDataset.JustUpdateGeoMapIndex(tmpMainPolyline);
                            pDataset.SaveGeoIndexToDB(tmpMainPolyline);
                            List<String> tmpFieldValues = pDataset.getGeometryFieldValues(tmpMainPolyline.GetSYS_ID(), true);
                            int tmpCount = tmpSplitResult.size();
                            for (int i = 1; i < tmpCount; i++) {
                                DataSet.SaveNewPolyline(tmpSplitResult.get(i), tmpFieldValues, pDataset, tmpLayerID);
                            }
                            pDataset.setEdited(true);
                            pDataset.RefreshTotalCount();
                        }
                        Common.ShowToast("线打断操作完成.");
                        PubVar._PubCommand.ProcessCommand("选择_仅清空选择集");
                        PubVar._Map.Refresh();
                        return;
                    }
                    Common.ShowToast("勾绘线没有与选择打断线相交.");
                } else {
                    Common.ShowDialog("请绘制一条分割线.");
                }
            } else if (command.equals("转向")) {
                BasicValue tmpParam2 = new BasicValue();
                List<HashMap<String, Object>> tmpList2 = Common.GetSelectObjects(PubVar._Map, -1, 1, false, tmpParam2);
                if (tmpParam2.getInt() <= 0 || tmpList2.size() <= 0) {
                    Common.ShowDialog("请在可编辑图层中选择需要转向的线.");
                    return;
                }
                for (HashMap<String, Object> tmpHash01 : tmpList2) {
                    DataSet pDataset2 = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(tmpHash01.get("DataSource"))).GetDatasetByName(String.valueOf(tmpHash01.get("LayerID")));
                    if (pDataset2 != null) {
                        Polyline tmpGeometry = (Polyline) tmpHash01.get("Geometry");
                        Collections.reverse(tmpGeometry.GetAllCoordinateList());
                        if (!tmpGeometry.IsSimple()) {
                            int tmpEnd = tmpGeometry.GetAllCoordinateList().size();
                            List<Integer> tmpList02 = new ArrayList<>();
                            ListIterator<Integer> tmpIter2 = tmpGeometry.getPartIndex().listIterator();
                            while (tmpIter2.hasNext()) {
                                tmpIter2.next();
                            }
                            int tid = 0;
                            tmpList02.add(0);
                            while (tmpIter2.hasPrevious()) {
                                int tmpStart = tmpIter2.previous().intValue();
                                tid += tmpEnd - tmpStart;
                                tmpList02.add(Integer.valueOf(tid));
                                tmpEnd = tmpStart;
                            }
                            tmpGeometry.setPartIndex(tmpList02);
                        }
                        tmpGeometry.SetEdited(true);
                        pDataset2.setEdited(true);
                    }
                }
                PubVar._Map.RefreshFast();
                Common.ShowToast("线转向完成.");
            } else if (command.equals("连接")) {
                BasicValue tmpParam3 = new BasicValue();
                List<HashMap<String, Object>> tmpList3 = Common.GetSelectObjects(PubVar._Map, -1, 1, false, tmpParam3);
                if (tmpParam3.getInt() <= 0 || tmpList3.size() <= 1) {
                    Common.ShowDialog("请在可编辑图层中选择需要连接的线(线数目必须大于1).");
                    return;
                }
                LineUnion_Dialog tempDialog = new LineUnion_Dialog();
                tempDialog.SetPolylines(tmpList3);
                tempDialog.ShowDialog();
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

    public static List<Polyline> SplitPolyline(List<Coordinate> mainLineCoords, List<Coordinate> splitLineCoords) {
        List<Polyline> result = new ArrayList<>();
        Iterator<Coordinate> tmpIter01 = mainLineCoords.iterator();
        Coordinate tmpCoord01 = tmpIter01.next();
        double tmpX1 = tmpCoord01.getX();
        double tmpY1 = tmpCoord01.getY();
        double[] tmpInterCoord = new double[2];
        List<Coordinate> tmpCoordsList = new ArrayList<>();
        tmpCoordsList.add(tmpCoord01.Clone());
        while (tmpIter01.hasNext()) {
            Coordinate tmpCoord012 = tmpIter01.next();
            double tmpX2 = tmpCoord012.getX();
            double tmpY2 = tmpCoord012.getY();
            if (SpatialRelation.CalLineSegIntersectPoint(tmpX1, tmpY1, tmpX2, tmpY2, splitLineCoords, tmpInterCoord)) {
                tmpCoordsList.add(new Coordinate(tmpInterCoord[0], tmpInterCoord[1]));
                if (tmpCoordsList.size() > 1) {
                    Polyline tmpPolyline = new Polyline();
                    tmpPolyline.SetAllCoordinateList(tmpCoordsList);
                    tmpPolyline.CalEnvelope();
                    result.add(tmpPolyline);
                }
                tmpCoordsList = new ArrayList<>();
                tmpCoordsList.add(new Coordinate(tmpInterCoord[0], tmpInterCoord[1]));
            } else {
                tmpCoordsList.add(tmpCoord012.Clone());
            }
            tmpX1 = tmpX2;
            tmpY1 = tmpY2;
        }
        if (tmpCoordsList.size() > 1) {
            Polyline tmpPolyline2 = new Polyline();
            tmpPolyline2.SetAllCoordinateList(tmpCoordsList);
            tmpPolyline2.CalEnvelope();
            result.add(tmpPolyline2);
        }
        return result;
    }
}
