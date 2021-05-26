package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.ListValues_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataQuery_Filter_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_ConditionDataItemList;
    private Bitmap m_DeleteImage;
    private InputSpinner m_ESDFilter;
    private MyTableFactory m_MyTableFactory;
    private FeatureLayer m_SelectLayer;
    private String m_queryTag;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public void setQueryTag(String value) {
        this.m_queryTag = value;
    }

    public void SetSelectLayer(FeatureLayer layer) {
        List<HashMap<String, Object>> tmpList;
        try {
            this.m_SelectLayer = layer;
            if (layer != null) {
                this._Dialog.SetCaption("查询条件");
            }
            HashMap<String, Object> tmpTagHash = (HashMap) this.m_SelectLayer.getTag();
            if (tmpTagHash != null && (tmpList = (List) tmpTagHash.get(this.m_queryTag)) != null && tmpList.size() > 0) {
                this.m_ConditionDataItemList = tmpList;
            }
        } catch (Exception e) {
        }
    }

    public DataQuery_Filter_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_SelectLayer = null;
        this.m_ESDFilter = null;
        this.m_MyTableFactory = null;
        this.m_DeleteImage = null;
        this.m_ConditionDataItemList = new ArrayList();
        this.m_queryTag = "QuerySQLList";
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Filter_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                String[] tempStrs;
                String[] tempStrs2;
                String tmpOper;
                try {
                    if (command.equals("重置查询条件")) {
                        DataQuery_Filter_Dialog.this.m_ConditionDataItemList.clear();
                        DataQuery_Filter_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                    } else if (command.equals("值选择返回")) {
                        String result = "";
                        List tmpList = (List) object;
                        if (tmpList != null) {
                            result = Common.CombineStrings(",", tmpList);
                        }
                        DataQuery_Filter_Dialog.this.m_ESDFilter.getEditTextView().setText(result);
                    } else if (command.equals("单击单元格")) {
                        if (object != null && (tempStrs2 = object.toString().split(",")) != null && tempStrs2.length > 0) {
                            final int tmpRowIndex = Integer.parseInt(tempStrs2[0]);
                            int tmpColIndex = Integer.parseInt(tempStrs2[1]);
                            if (tmpColIndex == 2) {
                                HashMap<String, Object> tempHash = (HashMap) DataQuery_Filter_Dialog.this.m_ConditionDataItemList.get(tmpRowIndex);
                                if (tempHash.get("D3").toString().equals("并且")) {
                                    tmpOper = "或";
                                } else {
                                    tmpOper = "并且";
                                }
                                tempHash.put("D3", tmpOper);
                                DataQuery_Filter_Dialog.this.m_MyTableFactory.BindDataToListView(DataQuery_Filter_Dialog.this.m_ConditionDataItemList, new String[]{"D1", "D2", "D3", "D4"}, DataQuery_Filter_Dialog.this.pCallback);
                                DataQuery_Filter_Dialog.this.m_MyTableFactory.SetClickItemReturnColumns("2,3,");
                            } else if (tmpColIndex == 3) {
                                Common.ShowYesNoDialog(PubVar.MainContext, "是否删除以下查询条件?\r\n" + ((HashMap) DataQuery_Filter_Dialog.this.m_ConditionDataItemList.get(tmpRowIndex)).get("D2").toString(), new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Filter_Dialog.1.1
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String paramString, Object pObject) {
                                        if (paramString.equals("YES") && tmpRowIndex < DataQuery_Filter_Dialog.this.m_ConditionDataItemList.size()) {
                                            DataQuery_Filter_Dialog.this.m_ConditionDataItemList.remove(tmpRowIndex);
                                            DataQuery_Filter_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    } else if (command.equals("增加")) {
                        String tmpField = Common.GetSpinnerValueOnID(DataQuery_Filter_Dialog.this._Dialog, R.id.sp_filter_field);
                        String tmpOper2 = Common.GetSpinnerValueOnID(DataQuery_Filter_Dialog.this._Dialog, R.id.sp_filter_operation);
                        String tmpCondition = String.valueOf(tmpField) + " " + tmpOper2.substring(tmpOper2.indexOf("(") + 1, tmpOper2.lastIndexOf(")")) + " (" + DataQuery_Filter_Dialog.this.m_ESDFilter.getEditTextView().getEditableText().toString() + ")";
                        if (DataQuery_Filter_Dialog.this.IsConditionExist(tmpCondition)) {
                            Common.ShowDialog("查询条件 [" + tmpCondition + "] 已经存在.");
                            return;
                        }
                        HashMap<String, Object> tempHash2 = new HashMap<>();
                        tempHash2.put("D1", false);
                        tempHash2.put("D2", tmpCondition);
                        tempHash2.put("D3", "并且");
                        tempHash2.put("D4", DataQuery_Filter_Dialog.this.m_DeleteImage);
                        DataQuery_Filter_Dialog.this.m_ConditionDataItemList.add(tempHash2);
                        DataQuery_Filter_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                    } else if (command.equals("查询")) {
                        if (DataQuery_Filter_Dialog.this.m_ConditionDataItemList.size() == 0) {
                            Common.ShowDialog("没有设置查询条件.");
                            return;
                        }
                        StringBuilder tempSB = new StringBuilder();
                        String tmpLastUnion = "";
                        for (HashMap<String, Object> tempHash3 : DataQuery_Filter_Dialog.this.m_ConditionDataItemList) {
                            String tmpCondition2 = tempHash3.get("D2").toString();
                            int tmpIndex01 = tmpCondition2.indexOf(" ");
                            String tmpFieldName = tmpCondition2.substring(0, tmpIndex01);
                            int tmpIndex02 = tmpCondition2.indexOf(" (");
                            String tmpOper3 = tmpCondition2.substring(tmpIndex01 + 1, tmpIndex02).trim();
                            LayerField tmpDateField = DataQuery_Filter_Dialog.this.m_SelectLayer.getLayerFieldByFieldName(tmpFieldName);
                            if (!(tmpDateField == null || (tempStrs = tmpCondition2.substring(tmpIndex02 + 2, tmpCondition2.length() - 1).trim().split(",")) == null || tempStrs.length <= 0)) {
                                if (tmpOper3.equals("=")) {
                                    tmpOper3 = " IN";
                                }
                                tempSB.append(tmpLastUnion);
                                tempSB.append(" ");
                                tempSB.append(tmpDateField.GetDataFieldName());
                                tempSB.append(" ");
                                tempSB.append(tmpOper3);
                                tempSB.append(" (");
                                String tempJoinS = Common.Joins("','", tempStrs);
                                tempSB.append("'");
                                if (tmpOper3.equals("like")) {
                                    tempSB.append("%");
                                }
                                tempSB.append(tempJoinS);
                                if (tmpOper3.equals("like")) {
                                    tempSB.append("%");
                                }
                                tempSB.append("'");
                                tempSB.append(") ");
                                if (tempHash3.get("D3").toString().equals("并且")) {
                                    tmpLastUnion = " AND ";
                                } else {
                                    tmpLastUnion = " OR ";
                                }
                            }
                        }
                        if (tempSB.length() == 0) {
                            tempSB.append(" 1=1 ");
                        }
                        if (DataQuery_Filter_Dialog.this.m_Callback != null) {
                            DataQuery_Filter_Dialog.this.m_Callback.OnClick("根据条件查询", new Object[]{tempSB.toString(), DataQuery_Filter_Dialog.this.m_ConditionDataItemList});
                        }
                        DataQuery_Filter_Dialog.this._Dialog.dismiss();
                    }
                } catch (Exception e) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.dataquery_filter_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("查询条件");
        this._Dialog.SetHeadButtons("1,2130837634,查询,查询", this.pCallback);
        this._Dialog.findViewById(R.id.bt_filter_add).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.bt_filter_clear).setOnClickListener(new ViewClick());
        this.m_DeleteImage = BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.deleteobject);
    }

    public void setTitle(String title) {
        this._Dialog.SetCaption(title);
    }

    public XDialogTemplate getDialog() {
        return this._Dialog;
    }

    public void setHeadButtons(String name) {
        this._Dialog.SetHeadButtons("1,2130837858," + name + ",查询", this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        try {
            if (this.m_SelectLayer != null) {
                List tmpList = new ArrayList();
                for (LayerField tmpField : this.m_SelectLayer.GetFieldList()) {
                    tmpList.add(tmpField.GetFieldName());
                }
                Common.SetSpinnerListData(this._Dialog, "请选择查询字段", tmpList, (int) R.id.sp_filter_field);
                List tmpList2 = new ArrayList();
                tmpList2.add("等于(=)");
                tmpList2.add("包含(like)");
                Common.SetSpinnerListData(this._Dialog, "请选择操作符", tmpList2, (int) R.id.sp_filter_operation);
                this.m_ESDFilter = (InputSpinner) this._Dialog.findViewById(R.id.esd_filter_value);
                this.m_ESDFilter.getEditTextView().setEnabled(true);
                this.m_ESDFilter.SetCallback(this.pCallback);
                this.m_ESDFilter.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Filter_Dialog.2
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        List tmpList3 = new ArrayList();
                        if (DataQuery_Filter_Dialog.this.m_SelectLayer != null) {
                            ListValues_Dialog tempDialog = new ListValues_Dialog();
                            StringBuilder tempSB = new StringBuilder();
                            tempSB.append("Select Distinct ");
                            tempSB.append(DataQuery_Filter_Dialog.this.m_SelectLayer.ConvertToDataField(Common.GetSpinnerValueOnID(DataQuery_Filter_Dialog.this._Dialog, R.id.sp_filter_field)));
                            tempSB.append(" From " + DataQuery_Filter_Dialog.this.m_SelectLayer.GetLayerID() + "_D");
                            DataSource pDataSource = PubVar.m_Workspace.GetDataSourceByName(DataQuery_Filter_Dialog.this.m_SelectLayer.GetDataSourceName());
                            if (pDataSource != null) {
                                boolean tmpHasNull = false;
                                SQLiteReader tempReader = pDataSource.Query(tempSB.toString());
                                if (tempReader != null) {
                                    while (tempReader.Read()) {
                                        String tempStr = tempReader.GetString(0);
                                        if (tempStr == null) {
                                            tmpHasNull = true;
                                        } else {
                                            tmpList3.add(tempStr);
                                        }
                                    }
                                    tempReader.Close();
                                }
                                if (tmpHasNull && !tmpList3.contains("")) {
                                    tmpList3.add("");
                                }
                            }
                            tempDialog.SetItems(tmpList3);
                            tempDialog.SetCallback(DataQuery_Filter_Dialog.this.pCallback);
                            tempDialog.ShowDialog();
                        }
                    }
                });
                this.m_MyTableFactory = new MyTableFactory();
                this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.in_listview_condition), "自定义", "选择,查询条件,关系,操作", "checkbox,text,text,image", new int[]{-15, -45, -20, -20}, this.pCallback);
                this.m_MyTableFactory.BindDataToListView(this.m_ConditionDataItemList, new String[]{"D1", "D2", "D3", "D4"}, this.pCallback);
                this.m_MyTableFactory.SetClickItemReturnColumns("2,3,");
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean IsConditionExist(String condition) {
        for (HashMap<String, Object> tempHash : this.m_ConditionDataItemList) {
            if (condition.equals(tempHash.get("D2").toString())) {
                return true;
            }
        }
        return false;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Filter_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                DataQuery_Filter_Dialog.this.refreshLayout();
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
                DataQuery_Filter_Dialog.this.pCallback.OnClick(view.getTag().toString(), null);
            }
        }
    }
}
