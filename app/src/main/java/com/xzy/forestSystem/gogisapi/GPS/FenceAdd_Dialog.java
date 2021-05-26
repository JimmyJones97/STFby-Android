package  com.xzy.forestSystem.gogisapi.GPS;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.InputPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class FenceAdd_Dialog {
    private XDialogTemplate _Dialog;
    private String fenceName;
    private ICallback m_Callback;
    private MyTableFactory m_MyTableFactory;
    private List<HashMap<String, Object>> m_TableDataList;
    private ICallback pCallback;
    private Object returnTag;

    public void SetReturnTag(Object ReturnTag) {
        this.returnTag = ReturnTag;
    }

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public FenceAdd_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.fenceName = null;
        this.returnTag = null;
        this.m_TableDataList = new ArrayList();
        this.m_MyTableFactory = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.GPS.FenceAdd_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                Object[] tmpObjs;
                try {
                    if (command.equals("确定")) {
                        if (FenceAdd_Dialog.this.fenceName == null) {
                            String tmpName = Common.GetEditTextValueOnID(FenceAdd_Dialog.this._Dialog, R.id.et_FenceName);
                            if (tmpName == null || tmpName.trim().equals("")) {
                                Common.ShowDialog("电子围栏名称不能为空.");
                                return;
                            }
                            boolean tmpBool = false;
                            SQLiteReader tmpSQLiteReader = PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().Query("Select ID From FenceData Where Name ='" + tmpName + "'");
                            if (tmpSQLiteReader != null) {
                                while (tmpSQLiteReader.Read()) {
                                    tmpBool = true;
                                }
                                tmpSQLiteReader.Close();
                            }
                            if (tmpBool) {
                                Common.ShowDialog("电子围栏名称【" + tmpName + "】已经存在.\r\n请采用其他的电子围栏名称.");
                            } else if (FenceAdd_Dialog.this.m_TableDataList.size() < 3) {
                                Common.ShowDialog("电子围栏的节点数目不能少于3个.");
                            } else {
                                StringBuilder tmpCoordData = new StringBuilder();
                                for (HashMap<String, Object> tmpHash : FenceAdd_Dialog.this.m_TableDataList) {
                                    tmpCoordData.append(tmpHash.get("D2").toString());
                                    tmpCoordData.append(",");
                                    tmpCoordData.append(tmpHash.get("D3").toString());
                                    tmpCoordData.append(",");
                                }
                                String tmpRemark = Common.GetEditTextValueOnID(FenceAdd_Dialog.this._Dialog, R.id.et_FenceRemark);
                                String tmpTimeStr = Common.dateFormat.format(new Date());
                                PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().ExecuteSQL("Insert Into FenceData (Name,CreateTime,Remark,Data) Values ('" + tmpName + "','" + tmpTimeStr + "','" + tmpRemark + "','" + ((Object) tmpCoordData) + "')");
                                if (FenceAdd_Dialog.this.m_Callback != null) {
                                    FenceAdd_Dialog.this.m_Callback.OnClick("新增电子围栏返回", new Object[]{FenceAdd_Dialog.this.returnTag, tmpName, tmpTimeStr, tmpRemark});
                                }
                                FenceAdd_Dialog.this._Dialog.dismiss();
                            }
                        } else if (FenceAdd_Dialog.this.m_TableDataList.size() < 3) {
                            Common.ShowDialog("电子围栏的节点数目不能少于3个.");
                        } else {
                            StringBuilder tmpCoordData2 = new StringBuilder();
                            for (HashMap<String, Object> tmpHash2 : FenceAdd_Dialog.this.m_TableDataList) {
                                tmpCoordData2.append(tmpHash2.get("D2").toString());
                                tmpCoordData2.append(",");
                                tmpCoordData2.append(tmpHash2.get("D3").toString());
                                tmpCoordData2.append(",");
                            }
                            String tmpRemark2 = Common.GetEditTextValueOnID(FenceAdd_Dialog.this._Dialog, R.id.et_FenceRemark);
                            String tmpTimeStr2 = Common.dateFormat.format(new Date());
                            PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().ExecuteSQL("Update FenceData Set CreateTime='" + tmpTimeStr2 + "',Remark='" + tmpRemark2 + "',Data='" + ((Object) tmpCoordData2) + "' Where Name='" + FenceAdd_Dialog.this.fenceName + "'");
                            if (FenceAdd_Dialog.this.m_Callback != null) {
                                FenceAdd_Dialog.this.m_Callback.OnClick("编辑电子围栏返回", new Object[]{FenceAdd_Dialog.this.returnTag, FenceAdd_Dialog.this.fenceName, tmpTimeStr2, tmpRemark2});
                            }
                            FenceAdd_Dialog.this._Dialog.dismiss();
                        }
                    } else if (command.equals("输入节点坐标返回") && (tmpObjs = (Object[]) object) != null && tmpObjs.length > 0) {
                        Object tmpTag = tmpObjs[0];
                        if (tmpTag == null) {
                            HashMap<String, Object> tempHash = new HashMap<>();
                            tempHash.put("D1", false);
                            tempHash.put("D2", tmpObjs[1].toString());
                            tempHash.put("D3", tmpObjs[2].toString());
                            FenceAdd_Dialog.this.m_TableDataList.add(tempHash);
                            FenceAdd_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                            return;
                        }
                        HashMap<String, Object> tempHash2 = (HashMap) FenceAdd_Dialog.this.m_TableDataList.get(Integer.parseInt(tmpTag.toString()));
                        tempHash2.put("D2", tmpObjs[1].toString());
                        tempHash2.put("D3", tmpObjs[2].toString());
                        FenceAdd_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.fenceadd_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("新增电子围栏");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_MyTableFactory = new MyTableFactory();
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.fence_Points_List), "自定义", "选择,X,Y", "checkbox,text,text", new int[]{-20, -40, -40}, this.pCallback);
        ((Button) this._Dialog.findViewById(R.id.btn_Fence_addDevice)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_Fence_modifyDevice)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_Fence_deleteDevice)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_Fence_SelectAll)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_Fence_UnselectAll)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_GetFromSelected)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_GetFromMeasure)).setOnClickListener(new ViewClick());
    }

    public void SetFenceName(String FenceName) {
        this.fenceName = FenceName;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        String[] tmpStrs;
        if (this.m_TableDataList.size() > 0) {
            this.m_TableDataList.clear();
        }
        if (this.fenceName == null) {
            this._Dialog.SetCaption("新增电子围栏");
        } else {
            this._Dialog.SetCaption("编辑电子围栏");
            EditText tmpETName = (EditText) this._Dialog.findViewById(R.id.et_FenceName);
            tmpETName.setText(this.fenceName);
            tmpETName.setEnabled(false);
            SQLiteReader tmpSQLiteReader = PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().Query("Select Remark,Data From FenceData Where Name='" + this.fenceName + "'");
            if (tmpSQLiteReader != null) {
                if (tmpSQLiteReader.Read()) {
                    Common.SetEditTextValueOnID(this._Dialog, R.id.et_FenceRemark, tmpSQLiteReader.GetString(0));
                    String tmpStr = tmpSQLiteReader.GetString(1);
                    if (tmpStr != null && tmpStr.length() > 0 && (tmpStrs = tmpStr.split(",")) != null && tmpStrs.length > 0) {
                        int count = tmpStrs.length / 2;
                        int tid = 0;
                        for (int i = 0; i < count; i++) {
                            HashMap<String, Object> tempHash = new HashMap<>();
                            tempHash.put("D1", false);
                            int tid2 = tid + 1;
                            tempHash.put("D2", tmpStrs[tid]);
                            tid = tid2 + 1;
                            tempHash.put("D3", tmpStrs[tid2]);
                            this.m_TableDataList.add(tempHash);
                        }
                    }
                }
                tmpSQLiteReader.Close();
            }
        }
        this.m_MyTableFactory.BindDataToListView(this.m_TableDataList, new String[]{"D1", "D2", "D3"}, this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("添加")) {
            InputPoint_Dialog dialog = new InputPoint_Dialog();
            dialog.FormType = 1;
            dialog.setTitle("添加围栏节点");
            dialog.SetCallback(this.pCallback);
            dialog.ShowDialog();
        } else if (command.equals("修改")) {
            int selectedID = -1;
            int tid = -1;
            if (this.m_TableDataList.size() > 0) {
                Iterator<HashMap<String, Object>> tmpiter = this.m_TableDataList.iterator();
                while (true) {
                    if (!tmpiter.hasNext()) {
                        break;
                    }
                    tid++;
                    if (Boolean.parseBoolean(tmpiter.next().get("D1").toString())) {
                        selectedID = tid;
                        break;
                    }
                }
            }
            if (selectedID >= 0) {
                InputPoint_Dialog dialog2 = new InputPoint_Dialog();
                dialog2.FormType = 1;
                dialog2.SetReturnTag(Integer.valueOf(selectedID));
                dialog2.setTitle("编辑围栏节点");
                HashMap<String, Object> tmpHash = this.m_TableDataList.get(selectedID);
                dialog2.setDefaultValue(Double.parseDouble(tmpHash.get("D2").toString()), Double.parseDouble(tmpHash.get("D3").toString()));
                dialog2.SetCallback(this.pCallback);
                dialog2.ShowDialog();
                return;
            }
            Common.ShowDialog("请勾选需要编辑的节点.");
        } else if (command.equals("删除")) {
            int tmpCount = 0;
            if (this.m_TableDataList.size() > 0) {
                for (HashMap<String, Object> tmpHash2 : this.m_TableDataList) {
                    if (Boolean.parseBoolean(tmpHash2.get("D1").toString())) {
                        tmpCount++;
                    }
                }
            }
            if (tmpCount > 0) {
                Common.ShowYesNoDialog(this._Dialog.getContext(), "是否删除选择的" + String.valueOf(tmpCount) + "个节点数据?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.GPS.FenceAdd_Dialog.2
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command2, Object pObject) {
                        for (int i = FenceAdd_Dialog.this.m_TableDataList.size() - 1; i >= 0; i--) {
                            if (Boolean.parseBoolean(((HashMap) FenceAdd_Dialog.this.m_TableDataList.get(i)).get("D1").toString())) {
                                FenceAdd_Dialog.this.m_TableDataList.remove(i);
                            }
                        }
                        FenceAdd_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                    }
                });
            } else {
                Common.ShowDialog("请勾选需要删除的节点.");
            }
        } else if (command.equals("全选")) {
            if (this.m_TableDataList.size() > 0) {
                for (HashMap<String, Object> tmpHash3 : this.m_TableDataList) {
                    tmpHash3.put("D1", true);
                }
                this.m_MyTableFactory.notifyDataSetChanged();
            }
        } else if (command.equals("反选")) {
            if (this.m_TableDataList.size() > 0) {
                for (HashMap<String, Object> tmpHash4 : this.m_TableDataList) {
                    tmpHash4.put("D1", Boolean.valueOf(!Boolean.parseBoolean(tmpHash4.get("D1").toString())));
                }
                this.m_MyTableFactory.notifyDataSetChanged();
            }
        } else if (command.equals("从选择对象")) {
            String tmpMsg = "";
            if (Common.GetSelectObjectsCount(PubVar._Map) > 0) {
                List<HashMap<String, Object>> tempList = Common.GetSelectObjects(PubVar._Map, -1, 2, true, new BasicValue());
                if (tempList.size() > 0) {
                    AbstractGeometry tempGeo = (AbstractGeometry) tempList.get(0).get("Geometry");
                    if (tempGeo == null) {
                        tmpMsg = "选择对象的几何数据错误.";
                    } else if (tempGeo.GetType() == EGeoLayerType.POLYGON) {
                        List<Coordinate> tmpCoords = ((Polygon) tempGeo).GetAllCoordinateList();
                        if (tmpCoords.size() > 2) {
                            if (this.m_TableDataList.size() > 0) {
                                this.m_TableDataList.clear();
                            }
                            for (Coordinate tmpCoord : tmpCoords) {
                                HashMap<String, Object> tempHash2 = new HashMap<>();
                                tempHash2.put("D1", false);
                                tempHash2.put("D2", Double.valueOf(tmpCoord.getX()));
                                tempHash2.put("D3", Double.valueOf(tmpCoord.getY()));
                                this.m_TableDataList.add(tempHash2);
                            }
                            this.m_MyTableFactory.notifyDataSetChanged();
                        } else {
                            tmpMsg = "选择对象的节点数目少于3个.";
                        }
                    }
                } else {
                    tmpMsg = "没有选择面对象.";
                }
            } else {
                tmpMsg = "请选择一个面对象作为电子围栏的边界范围.";
            }
            if (!tmpMsg.equals("")) {
                Common.ShowDialog(tmpMsg);
            }
        } else if (command.equals("从测量获取")) {
            List<Coordinate> tmpCoords2 = PubVar._PubCommand.m_Measure.GetLastCoordinates(-1);
            if (tmpCoords2.size() == 0) {
                Common.ShowDialog("没有绘制测量的线段,请先利用【测量】工具绘制线段.");
            } else if (tmpCoords2.size() < 3) {
                Common.ShowDialog("测量工具绘制的线段节点数目少于3个,不能形成围栏区域.");
            } else {
                if (this.m_TableDataList.size() > 0) {
                    this.m_TableDataList.clear();
                }
                for (Coordinate tmpCoord2 : tmpCoords2) {
                    HashMap<String, Object> tempHash22 = new HashMap<>();
                    tempHash22.put("D1", false);
                    tempHash22.put("D2", Double.valueOf(tmpCoord2.getX()));
                    tempHash22.put("D3", Double.valueOf(tmpCoord2.getY()));
                    this.m_TableDataList.add(tempHash22);
                }
                this.m_MyTableFactory.notifyDataSetChanged();
            }
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.FenceAdd_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                FenceAdd_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                FenceAdd_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
