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
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Polygon_SelectEditObject_Dialog {
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

    public Polygon_SelectEditObject_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_MainPolyIndex = -1;
        this.m_MainPolygon = null;
        this.m_Polygons = null;
        this.m_SelectObject = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Edit.Polygon_SelectEditObject_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("确定")) {
                        if (Polygon_SelectEditObject_Dialog.this.m_SelectObject != null) {
                            int tmpIndex = Integer.parseInt(String.valueOf(Polygon_SelectEditObject_Dialog.this.m_SelectObject.get("D3")));
                            if (tmpIndex >= 0 && tmpIndex < Polygon_SelectEditObject_Dialog.this.m_Polygons.size() && Polygon_SelectEditObject_Dialog.this.m_Callback != null) {
                                Polygon_SelectEditObject_Dialog.this.m_Callback.OnClick("选择编辑面对象返回", Integer.valueOf(tmpIndex));
                            }
                            Polygon_SelectEditObject_Dialog.this._Dialog.dismiss();
                            return;
                        }
                        Common.ShowDialog("没有选择任何对象.");
                    } else if (command.equals("单击选择行")) {
                        Polygon_SelectEditObject_Dialog.this.m_SelectObject = (HashMap) object;
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_TableDataList = new ArrayList();
        this.m_MyTableFactory = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.x_polygon_selectioneditobj_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("选择可编辑对象");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_MyTableFactory = new MyTableFactory();
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.list_polygonSelectionObjects), "自定义", "示意图,属性信息", "image,text", new int[]{-50, -50}, this.pCallback);
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
                    tempPolys.add((Polygon) tempHash.get("Geometry"));
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
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Edit.Polygon_SelectEditObject_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Polygon_SelectEditObject_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
