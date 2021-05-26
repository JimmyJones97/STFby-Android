package  com.xzy.forestSystem.gogisapi.GPS;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class FenceSetting_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private MyTableFactory m_MyTableFactory;
    private List<HashMap<String, Object>> m_TableDataList;
    private ICallback pCallback;
    private List<String> seletedNamesArray;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public FenceSetting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_TableDataList = new ArrayList();
        this.m_MyTableFactory = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.GPS.FenceSetting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                Object[] tmpObjs;
                int tmpI;
                try {
                    if (command.equals("新增电子围栏返回")) {
                        Object[] tmpObjs2 = (Object[]) object;
                        if (tmpObjs2 != null && tmpObjs2.length > 3) {
                            HashMap<String, Object> tempHash = new HashMap<>();
                            tempHash.put("D1", false);
                            tempHash.put("D2", tmpObjs2[1].toString());
                            tempHash.put("D3", tmpObjs2[2].toString());
                            tempHash.put("D4", tmpObjs2[3].toString());
                            FenceSetting_Dialog.this.m_TableDataList.add(tempHash);
                            FenceSetting_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                        }
                    } else if (command.equals("编辑电子围栏返回") && (tmpObjs = (Object[]) object) != null && tmpObjs.length > 3 && (tmpI = Integer.parseInt(tmpObjs[0].toString())) >= 0 && tmpI < FenceSetting_Dialog.this.m_TableDataList.size()) {
                        HashMap<String, Object> tmpHash = (HashMap) FenceSetting_Dialog.this.m_TableDataList.get(tmpI);
                        tmpHash.put("D2", tmpObjs[1].toString());
                        tmpHash.put("D3", tmpObjs[2].toString());
                        tmpHash.put("D4", tmpObjs[3].toString());
                        FenceSetting_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
            }
        };
        this.seletedNamesArray = new ArrayList();
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.fencesetting_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("电子围栏管理");
        this.m_MyTableFactory = new MyTableFactory();
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.fence_List), "自定义", "选择,围栏名称,创建时间,备注", "checkbox,text,text,text", new int[]{-16, -30, -30, -24}, this.pCallback);
        ((Button) this._Dialog.findViewById(R.id.btn_Fence_addDevice)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_Fence_modifyDevice)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_Fence_deleteDevice)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_Fence_SelectAll)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_Fence_UnselectAll)).setOnClickListener(new ViewClick());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshList() {
        if (this.m_TableDataList.size() > 0) {
            this.m_TableDataList.clear();
        }
        SQLiteReader tmpSQLiteReader = PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().Query("Select Name,CreateTime,Remark From FenceData Group By Name");
        if (tmpSQLiteReader != null) {
            while (tmpSQLiteReader.Read()) {
                HashMap<String, Object> tempHash = new HashMap<>();
                tempHash.put("D1", false);
                tempHash.put("D2", tmpSQLiteReader.GetString(0));
                tempHash.put("D3", tmpSQLiteReader.GetString(1));
                tempHash.put("D4", tmpSQLiteReader.GetString(2));
                this.m_TableDataList.add(tempHash);
            }
            tmpSQLiteReader.Close();
        }
        this.m_MyTableFactory.BindDataToListView(this.m_TableDataList, new String[]{"D1", "D2", "D3", "D4"}, this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("添加")) {
            FenceAdd_Dialog dialog = new FenceAdd_Dialog();
            dialog.SetCallback(this.pCallback);
            dialog.ShowDialog();
        } else if (command.equals("修改")) {
            String tmpName = "";
            int tid = -1;
            if (this.m_TableDataList.size() > 0) {
                Iterator<HashMap<String, Object>> tmpiter = this.m_TableDataList.iterator();
                while (true) {
                    if (!tmpiter.hasNext()) {
                        break;
                    }
                    tid++;
                    HashMap<String, Object> tmpHash = tmpiter.next();
                    if (Boolean.parseBoolean(tmpHash.get("D1").toString())) {
                        tmpName = tmpHash.get("D2").toString();
                        break;
                    }
                }
            }
            if (!tmpName.equals("")) {
                FenceAdd_Dialog dialog2 = new FenceAdd_Dialog();
                dialog2.SetFenceName(tmpName);
                dialog2.SetReturnTag(Integer.valueOf(tid));
                dialog2.SetCallback(this.pCallback);
                dialog2.ShowDialog();
                return;
            }
            Common.ShowDialog("请勾选需要编辑的电子围栏.");
        } else if (command.equals("删除")) {
            this.seletedNamesArray = new ArrayList();
            if (this.m_TableDataList.size() > 0) {
                for (HashMap<String, Object> tmpHash2 : this.m_TableDataList) {
                    if (Boolean.parseBoolean(tmpHash2.get("D1").toString())) {
                        this.seletedNamesArray.add(tmpHash2.get("D2").toString());
                    }
                }
            }
            if (this.seletedNamesArray.size() > 0) {
                Common.ShowYesNoDialog(this._Dialog.getContext(), "是否删除选择的" + String.valueOf(this.seletedNamesArray.size()) + "个电子围栏数据?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.GPS.FenceSetting_Dialog.2
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command2, Object pObject) {
                        if (PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().ExecuteSQL("Delete From FenceData Where Name in ('" + Common.CombineStrings("','", FenceSetting_Dialog.this.seletedNamesArray) + "')")) {
                            FenceSetting_Dialog.this.refreshList();
                        }
                    }
                });
            } else {
                Common.ShowDialog("请勾选需要删除的电子围栏.");
            }
        } else if (command.equals("全选")) {
            if (this.m_TableDataList.size() > 0) {
                for (HashMap<String, Object> tmpHash3 : this.m_TableDataList) {
                    tmpHash3.put("D1", true);
                }
                this.m_MyTableFactory.notifyDataSetChanged();
            }
        } else if (command.equals("反选") && this.m_TableDataList.size() > 0) {
            for (HashMap<String, Object> tmpHash4 : this.m_TableDataList) {
                tmpHash4.put("D1", Boolean.valueOf(!Boolean.parseBoolean(tmpHash4.get("D1").toString())));
            }
            this.m_MyTableFactory.notifyDataSetChanged();
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.GPS.FenceSetting_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                FenceSetting_Dialog.this.refreshList();
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
                FenceSetting_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }

    public static List<String> GetFenceNameList() {
        List<String> result = new ArrayList<>();
        SQLiteReader tmpSQLiteReader = PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().Query("Select Distinct Name From FenceData");
        if (tmpSQLiteReader != null) {
            while (tmpSQLiteReader.Read()) {
                result.add(tmpSQLiteReader.GetString(0));
            }
            tmpSQLiteReader.Close();
        }
        return result;
    }

    public static Polyline GetFenceBorder(String fenceName) {
        String[] tmpStrs;
        Polyline result = null;
        SQLiteReader tmpSQLiteReader = PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().Query("Select Data From FenceData Where Name='" + fenceName + "'");
        if (tmpSQLiteReader != null) {
            if (tmpSQLiteReader.Read() && (tmpStrs = tmpSQLiteReader.GetString(0).split(",")) != null && tmpStrs.length > 5) {
                List<Coordinate> tmpList = new ArrayList<>();
                int count = tmpStrs.length / 2;
                int tid = 0;
                for (int i = 0; i < count; i++) {
                    int tid2 = tid + 1;
                    tid = tid2 + 1;
                    tmpList.add(new Coordinate(Double.parseDouble(tmpStrs[tid]), Double.parseDouble(tmpStrs[tid2])));
                }
                if (tmpList.size() > 2) {
                    result = new Polyline();
                    result.SetAllCoordinateList(tmpList);
                }
            }
            tmpSQLiteReader.Close();
        }
        return result;
    }
}
