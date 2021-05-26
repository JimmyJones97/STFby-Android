package  com.xzy.forestSystem.gogisapi.MyControls;

import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListValues_Dialog {
    public boolean ClickItemReturn;
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private int m_ItemType;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_TableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public ListValues_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_TableFactory = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_ItemType = 0;
        this.ClickItemReturn = false;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.ListValues_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("确定")) {
                        if (ListValues_Dialog.this.m_ItemType == 0) {
                            List tmpList = new ArrayList();
                            if (ListValues_Dialog.this.m_MyTableDataList != null && ListValues_Dialog.this.m_MyTableDataList.size() > 0) {
                                for (HashMap<String, Object> tempHash : ListValues_Dialog.this.m_MyTableDataList) {
                                    if (Boolean.parseBoolean(tempHash.get("D1").toString())) {
                                        tmpList.add(tempHash.get("D2").toString());
                                    }
                                }
                            }
                            if (ListValues_Dialog.this.m_Callback != null) {
                                ListValues_Dialog.this.m_Callback.OnClick("值选择返回", tmpList);
                            }
                            ListValues_Dialog.this._Dialog.dismiss();
                            return;
                        }
                        int tmpIndex = ListValues_Dialog.this.m_TableFactory.getSelectItemIndex();
                        if (tmpIndex < 0 || tmpIndex >= ListValues_Dialog.this.m_MyTableDataList.size()) {
                            Common.ShowDialog("没有选择任何有效值.");
                            return;
                        }
                        HashMap<String, Object> tempHash2 = (HashMap) ListValues_Dialog.this.m_MyTableDataList.get(tmpIndex);
                        if (ListValues_Dialog.this.m_Callback != null) {
                            ListValues_Dialog.this.m_Callback.OnClick("值选择返回", tempHash2.get("D2"));
                        }
                        ListValues_Dialog.this._Dialog.dismiss();
                    } else if (command.equals("单击选择行") && ListValues_Dialog.this.ClickItemReturn) {
                        ListValues_Dialog.this.pCallback.OnClick("确定", null);
                    }
                } catch (Exception e) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.listvalues_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("值列表");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.buttonSelectAll).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectDe).setOnClickListener(new ViewClick());
    }

    public void SetText(String title) {
        this._Dialog.SetCaption(title);
    }

    public void SetItems(List<String> values) {
        SetItems(values, 0, "值");
    }

    public void SetItems(List<String> values, int listType, String columnName) {
        this.m_ItemType = listType;
        if (listType == 0) {
            try {
                this.m_TableFactory = new MyTableFactory();
                this.m_TableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.list_values), "自定义", "选择," + columnName, "checkbox,text", new int[]{-15, -85}, this.pCallback);
                this.m_MyTableDataList = new ArrayList();
                if (values.size() > 0) {
                    for (String str : values) {
                        HashMap tempHash = new HashMap();
                        tempHash.put("D1", false);
                        tempHash.put("D2", str);
                        this.m_MyTableDataList.add(tempHash);
                    }
                }
                this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2"}, this.pCallback);
            } catch (Exception e) {
            }
        } else if (listType == 1) {
            this._Dialog.findViewById(R.id.linearLayoutbottom).setVisibility(8);
            this.m_TableFactory = new MyTableFactory();
            this.m_TableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.list_values), "自定义", columnName, "text", new int[]{-100}, this.pCallback);
            this.m_MyTableDataList = new ArrayList();
            if (values.size() > 0) {
                for (String str2 : values) {
                    HashMap tempHash2 = new HashMap();
                    tempHash2.put("D1", false);
                    tempHash2.put("D2", str2);
                    this.m_MyTableDataList.add(tempHash2);
                }
            }
            this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D2"}, this.pCallback);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("全选")) {
                if (this.m_MyTableDataList != null && this.m_MyTableDataList.size() > 0) {
                    for (HashMap<String, Object> tempHash : this.m_MyTableDataList) {
                        tempHash.put("D1", true);
                    }
                    this.m_TableFactory.notifyDataSetInvalidated();
                }
            } else if (command.equals("反选") && this.m_MyTableDataList != null && this.m_MyTableDataList.size() > 0) {
                for (HashMap<String, Object> tempHash2 : this.m_MyTableDataList) {
                    tempHash2.put("D1", Boolean.valueOf(!Boolean.parseBoolean(tempHash2.get("D1").toString())));
                }
                this.m_TableFactory.notifyDataSetInvalidated();
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.show();
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                ListValues_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
