package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Config.UserParam;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataQuerySetting_Dialog {
    private XDialogTemplate _Dialog;
    List<LayerField> layerFields;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private ICallback pCallback;
    private MyTableFactory tmpTableFactory;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public DataQuerySetting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuerySetting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("确定")) {
                    String tempStr = Common.GetSpinnerValueOnID(DataQuerySetting_Dialog.this._Dialog, R.id.spinnerLayerList);
                    UserParam tempParam = PubVar._PubCommand.m_UserConfigDB.GetUserParam();
                    HashMap<String, String> tempHashMap = tempParam.GetUserPara("Tag_System_Query_Page_Row");
                    if (tempHashMap == null) {
                        tempHashMap = new HashMap<>();
                    }
                    if (tempStr == null || tempStr.trim().equals("")) {
                        tempStr = "100";
                    }
                    tempHashMap.put("F2", tempStr.trim());
                    tempParam.SaveUserPara("Tag_System_Query_Page_Row", tempHashMap);
                    final List<Integer> selectedList = new ArrayList<>();
                    for (HashMap tmpHashMap : DataQuerySetting_Dialog.this.m_MyTableDataList) {
                        if (Boolean.parseBoolean(tmpHashMap.get("D1").toString())) {
                            selectedList.add(Integer.valueOf(((Integer) tmpHashMap.get("D3")).intValue()));
                        }
                    }
                    if (selectedList.size() == 0) {
                        Common.ShowDialog("请至少选择一个字段.");
                    } else if (selectedList.size() > 20) {
                        String finalTempStr = tempStr;
                        Common.ShowYesNoDialog(DataQuerySetting_Dialog.this._Dialog.getContext(), "为提高显示效率,建议显示字段数目不超过20个.\r\n是否继续?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuerySetting_Dialog.1.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject2) {
                                if (paramString2.equals("YES")) {
                                    if (DataQuerySetting_Dialog.this.layerFields != null) {
                                        int tempTid = 0;
                                        for (LayerField tempField : DataQuerySetting_Dialog.this.layerFields) {
                                            if (selectedList.contains(Integer.valueOf(tempTid))) {
                                                tempField.SetTag("true");
                                            } else {
                                                tempField.SetTag("false");
                                            }
                                            tempTid++;
                                        }
                                    }
                                    if (DataQuerySetting_Dialog.this.m_Callback != null) {
                                        DataQuerySetting_Dialog.this.m_Callback.OnClick("设置返回", finalTempStr);
                                    }
                                    DataQuerySetting_Dialog.this._Dialog.dismiss();
                                }
                            }
                        });
                    } else {
                        if (DataQuerySetting_Dialog.this.layerFields != null) {
                            int tempTid = 0;
                            for (LayerField tempField : DataQuerySetting_Dialog.this.layerFields) {
                                if (selectedList.contains(Integer.valueOf(tempTid))) {
                                    tempField.SetTag("true");
                                } else {
                                    tempField.SetTag("false");
                                }
                                tempTid++;
                            }
                        }
                        if (DataQuerySetting_Dialog.this.m_Callback != null) {
                            DataQuerySetting_Dialog.this.m_Callback.OnClick("设置返回", tempStr);
                        }
                        DataQuerySetting_Dialog.this._Dialog.dismiss();
                    }
                }
            }
        };
        this.layerFields = null;
        this.tmpTableFactory = null;
        this.m_MyTableDataList = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.dataquerysetting_dialog);
        this._Dialog.Resize(1.0f, PubVar.DialogHeightRatio);
        this._Dialog.SetCaption("查询结果设置");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.buttonSelectAll).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectDe).setOnClickListener(new ViewClick());
        ArrayList tempArray = new ArrayList();
        tempArray.add("100");
        tempArray.add("200");
        tempArray.add("300");
        tempArray.add("400");
        tempArray.add("500");
        tempArray.add("1000");
        tempArray.add("3000");
        tempArray.add("5000");
        tempArray.add("全部");
        Common.SetSpinnerListData(this._Dialog, "请选择每页数据条数:", tempArray, (int) R.id.spinnerLayerList);
        HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_Query_Page_Row");
        if (tempHashMap == null) {
            HashMap<String, String> tempHashMap2 = new HashMap<>();
            tempHashMap2.put("F2", "100");
            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_Query_Page_Row", tempHashMap2);
        } else {
            Common.SetValueToView(tempHashMap.get("F2"), this._Dialog.findViewById(R.id.spinnerLayerList));
        }
        this.tmpTableFactory = new MyTableFactory();
        this.tmpTableFactory = new MyTableFactory();
        this.tmpTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.prj_list), "自定义", "选择,字段名称", "checkbox,text", new int[]{-15, -85}, this.pCallback);
    }

    public void SetLayerFields(List<LayerField> fields) {
        this.layerFields = fields;
        refreshData();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshData() {
        this.m_MyTableDataList = new ArrayList();
        if (this.layerFields != null && this.layerFields.size() > 0) {
            int tid = 0;
            for (LayerField tempField : this.layerFields) {
                HashMap tmpHashMap = new HashMap();
                if (tempField.GetTag() == null || !tempField.GetTag().toString().equals("false")) {
                    tmpHashMap.put("D1", true);
                } else {
                    tmpHashMap.put("D1", false);
                }
                tmpHashMap.put("D2", tempField.GetFieldName());
                tmpHashMap.put("D3", Integer.valueOf(tid));
                this.m_MyTableDataList.add(tmpHashMap);
                tid++;
            }
        }
        this.tmpTableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2"}, null);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("全选")) {
            if (this.m_MyTableDataList != null) {
                for (HashMap<String, Object> tmpHashMap : this.m_MyTableDataList) {
                    tmpHashMap.put("D1", true);
                }
                this.tmpTableFactory.notifyDataSetInvalidated();
            }
        } else if (command.equals("反选") && this.m_MyTableDataList != null) {
            for (HashMap<String, Object> tmpHashMap2 : this.m_MyTableDataList) {
                tmpHashMap2.put("D1", Boolean.valueOf(!((Boolean) tmpHashMap2.get("D1")).booleanValue()));
            }
            this.tmpTableFactory.notifyDataSetInvalidated();
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuerySetting_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                DataQuerySetting_Dialog.this.refreshData();
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
                DataQuerySetting_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
