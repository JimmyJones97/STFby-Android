package  com.xzy.forestSystem.gogisapi.Edit;

import android.content.DialogInterface;
import android.widget.LinearLayout;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Display.DrawPolygonSample;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PolygonReshape_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private DataSet m_DataSet;
    private LinearLayout m_MainLayout;
    private Polygon m_MainPolygon;
    private MyTableFactory m_MyTableFactory;
    private List<Polygon> m_Polygons;
    private HashMap<String, Object> m_SelectObject;
    private List<HashMap<String, Object>> m_TableDataList;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public void SetMainPolygon(Polygon polygon) {
        this.m_MainPolygon = polygon;
    }

    public void SetPolygons(List<Polygon> polygons) {
        this.m_Polygons = polygons;
    }

    public void SetDataSet(DataSet dataSet) {
        this.m_DataSet = dataSet;
    }

    public PolygonReshape_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_MainPolygon = null;
        this.m_Polygons = null;
        this.m_DataSet = null;
        this.m_SelectObject = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Edit.PolygonReshape_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("确定")) {
                        if (PolygonReshape_Dialog.this.m_SelectObject != null) {
                            int tmpI = Integer.parseInt(PolygonReshape_Dialog.this.m_SelectObject.get("D1").toString());
                            HashMap<String, Object> tmpModifyHashMap = new HashMap<>();
                            tmpModifyHashMap.put("Type", "Reshape");
                            List<HashMap<String, Object>> tmpListOld01 = new ArrayList<>();
                            List<HashMap<String, Object>> tmpListModify02 = new ArrayList<>();
                            List<HashMap<String, Object>> tmpListNew03 = new ArrayList<>();
                            List<HashMap<String, Object>> tmpListRemove04 = new ArrayList<>();
                            HashMap<String, Object> tmpHashMapOld = new HashMap<>();
                            tmpHashMapOld.put("SYS_ID", PolygonReshape_Dialog.this.m_MainPolygon.GetSYS_ID());
                            tmpHashMapOld.put("Coords", PolygonReshape_Dialog.this.m_MainPolygon.GetAllCoordinateList());
                            tmpListOld01.add(tmpHashMapOld);
                            PolygonReshape_Dialog.this.m_MainPolygon.ResetData();
                            PolygonReshape_Dialog.this.m_MainPolygon.SetAllCoordinateList(((Polygon) PolygonReshape_Dialog.this.m_Polygons.get(tmpI)).GetAllCoordinateList());
                            PolygonReshape_Dialog.this.m_MainPolygon.SetEdited(true);
                            PolygonReshape_Dialog.this.m_DataSet.JustUpdateGeoMapIndex(PolygonReshape_Dialog.this.m_MainPolygon);
                            PolygonReshape_Dialog.this.m_DataSet.SaveGeoIndexToDB(PolygonReshape_Dialog.this.m_MainPolygon);
                            PolygonReshape_Dialog.this.m_DataSet.setEdited(true);
                            PubVar._PubCommand.ProcessCommand("选择_仅清空选择集");
                            PubVar._PubCommand.GetPolygonEdit().Clear();
                            PubVar._Map.Refresh();
                            Common.ShowToast("修边完成.");
                            HashMap<String, Object> tmpHashMapModify = new HashMap<>();
                            tmpHashMapModify.put("SYS_ID", PolygonReshape_Dialog.this.m_MainPolygon.GetSYS_ID());
                            tmpHashMapModify.put("Coords", PolygonReshape_Dialog.this.m_MainPolygon.GetAllCoordinateList());
                            tmpListModify02.add(tmpHashMapModify);
                            tmpModifyHashMap.put("Old", tmpListOld01);
                            tmpModifyHashMap.put("Modify", tmpListModify02);
                            tmpModifyHashMap.put("New", tmpListNew03);
                            tmpModifyHashMap.put("Remove", tmpListRemove04);
                            if (PolygonReshape_Dialog.this.m_Callback != null) {
                                PolygonReshape_Dialog.this.m_Callback.OnClick("修边选择返回", tmpModifyHashMap);
                            }
                            PolygonReshape_Dialog.this._Dialog.dismiss();
                            return;
                        }
                        Common.ShowDialog("没有选择任何结果.");
                    } else if (command.equals("单击选择行")) {
                        PolygonReshape_Dialog.this.m_SelectObject = (HashMap) object;
                        PolygonReshape_Dialog.this.pCallback.OnClick("确定", null);
                    } else if (command.equals("隐藏")) {
                        PolygonReshape_Dialog.this._Dialog.SetHeadButtons("1,2130837858,显示,显示", PolygonReshape_Dialog.this.pCallback);
                        PolygonReshape_Dialog.this.m_MainLayout.setVisibility(8);
                        PolygonReshape_Dialog.this._Dialog.getWindow().setGravity(80);
                    } else if (command.equals("显示")) {
                        PolygonReshape_Dialog.this._Dialog.SetHeadButtons("1,2130837858,隐藏,隐藏", PolygonReshape_Dialog.this.pCallback);
                        PolygonReshape_Dialog.this.m_MainLayout.setVisibility(0);
                        PolygonReshape_Dialog.this._Dialog.getWindow().setGravity(17);
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_TableDataList = new ArrayList();
        this.m_MyTableFactory = null;
        this.m_MainLayout = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.polyreshape_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("面修边选项");
        this._Dialog.SetHeadButtons("1,2130837858,隐藏,隐藏", this.pCallback);
        this.m_MainLayout = (LinearLayout) this._Dialog.findViewById(R.id.ll_polyreshapedialog);
        this.m_MyTableFactory = new MyTableFactory();
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.listview_polygons), "自定义", "修边结果", "image", new int[]{-100}, this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        this.m_TableDataList = new ArrayList();
        try {
            if (this.m_Polygons.size() > 1) {
                int count = this.m_Polygons.size();
                for (int i = 0; i < count; i++) {
                    List<Integer> tmpList = new ArrayList<>();
                    tmpList.add(Integer.valueOf(i));
                    HashMap<String, Object> tempHash2 = new HashMap<>();
                    tempHash2.put("D2", DrawPolygonSample.DrawPolygons(this.m_Polygons, tmpList, 400, 400));
                    tempHash2.put("D1", Integer.valueOf(i));
                    this.m_TableDataList.add(tempHash2);
                }
            }
        } catch (Exception e) {
        }
        this.m_MyTableFactory.BindDataToListView(this.m_TableDataList, new String[]{"D2"}, this.pCallback);
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Edit.PolygonReshape_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                PolygonReshape_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
