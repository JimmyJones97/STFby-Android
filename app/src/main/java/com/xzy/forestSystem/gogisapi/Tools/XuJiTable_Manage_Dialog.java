package  com.xzy.forestSystem.gogisapi.Tools;

import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XuJiTable_Manage_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public XuJiTable_Manage_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.XuJiTable_Manage_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("导入蓄积量计算表返回")) {
                    XuJiTable_Manage_Dialog.this.refreshTable();
                } else if (command.equals("返回") && XuJiTable_Manage_Dialog.this.m_Callback != null) {
                    XuJiTable_Manage_Dialog.this.m_Callback.OnClick("蓄积计算表格管理返回", null);
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.xujitable_manage_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("蓄积量计算表管理");
        this._Dialog.findViewById(R.id.btn_dataManag_add).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_dataManag_delete).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectAll).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectDe).setOnClickListener(new ViewClick());
        this._Dialog.SetCallback(this.pCallback);
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_xujitableManage_datalist), "自定义", "选择,省份名称,树木名称", "checkbox,text,text", new int[]{-30, -30, -40}, null);
        this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("添加")) {
            XuJiTable_Import_Dialog tmpDialog = new XuJiTable_Import_Dialog();
            tmpDialog.SetCallback(this.pCallback);
            tmpDialog.ShowDialog();
        } else if (command.equals("删除")) {
            final List<Object> tmpList = new ArrayList<>();
            for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
                if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                    tmpList.add(tmpHash.get("D4"));
                }
            }
            if (tmpList.size() > 0) {
                Common.ShowYesNoDialog(this._Dialog.getContext(), "是否删除选择的" + String.valueOf(tmpList.size()) + "个计算表?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.XuJiTable_Manage_Dialog.2
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        for (int i = tmpList.size() - 1; i > -1; i--) {
                            Object tmpObject = tmpList.get(i);
                            if (tmpObject != null && (tmpObject instanceof XuJiTableClass)) {
                                XuJiTableClass.DeleteXuJiTable((XuJiTableClass) tmpObject);
                            }
                        }
                        XuJiTable_Manage_Dialog.this.refreshTable();
                    }
                });
            } else {
                Common.ShowDialog("没有勾选任何计算表.");
            }
        } else if (command.equals("全选")) {
            for (HashMap<String, Object> tmpHash2 : this.m_MyTableDataList) {
                tmpHash2.put("D1", true);
            }
            this.m_MyTableFactory.notifyDataSetChanged();
        } else if (command.equals("反选")) {
            for (HashMap<String, Object> tmpHash3 : this.m_MyTableDataList) {
                tmpHash3.put("D1", Boolean.valueOf(!Boolean.parseBoolean(String.valueOf(tmpHash3.get("D1")))));
            }
            this.m_MyTableFactory.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshTable() {
        this.m_MyTableDataList.clear();
        List<XuJiTableClass> tmpList = XuJiTableClass.GetXuJiTablesInfo();
        if (tmpList != null && tmpList.size() > 0) {
            for (XuJiTableClass tmpXuJiTableClass : tmpList) {
                HashMap tmpHash = new HashMap();
                tmpHash.put("D1", false);
                tmpHash.put("D2", tmpXuJiTableClass.ZoneName);
                tmpHash.put("D3", tmpXuJiTableClass.Name);
                tmpHash.put("D4", tmpXuJiTableClass);
                this.m_MyTableDataList.add(tmpHash);
            }
        }
        this.m_MyTableFactory.notifyDataSetChanged();
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.XuJiTable_Manage_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                XuJiTable_Manage_Dialog.this.refreshTable();
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
                XuJiTable_Manage_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
