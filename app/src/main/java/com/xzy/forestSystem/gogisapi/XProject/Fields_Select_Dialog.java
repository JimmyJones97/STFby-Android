package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fields_Select_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private FeatureLayer m_Layer;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private String m_SelectFieldNames;
    private MyTableFactory m_TableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public Fields_Select_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_Layer = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Fields_Select_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("确定")) {
                    StringBuilder tempSB = new StringBuilder();
                    if (Fields_Select_Dialog.this.m_MyTableDataList != null) {
                        for (HashMap<String, Object> temphash : Fields_Select_Dialog.this.m_MyTableDataList) {
                            if (Boolean.parseBoolean(temphash.get("D1").toString())) {
                                tempSB.append(temphash.get("D2"));
                                tempSB.append(",");
                            }
                        }
                        if (tempSB.length() > 0) {
                            tempSB = tempSB.deleteCharAt(tempSB.length() - 1);
                        }
                    }
                    if (tempSB.length() > 0) {
                        if (Fields_Select_Dialog.this.m_Callback != null) {
                            Fields_Select_Dialog.this.m_Callback.OnClick("字段组合选择", tempSB.toString());
                        }
                        Fields_Select_Dialog.this._Dialog.dismiss();
                        return;
                    }
                    Common.ShowDialog("没有选择任何字段.");
                }
            }
        };
        this.m_SelectFieldNames = "";
        this.m_MyTableDataList = null;
        this.m_TableFactory = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.fields_select_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("字段组合");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        ((Button) this._Dialog.findViewById(R.id.btn_Up)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Fields_Select_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                int tmpIndex = Fields_Select_Dialog.this.m_TableFactory.getSelectItemIndex();
                if (tmpIndex < 0) {
                    Common.ShowDialog("请按字段名称选择需要上移的字段.");
                } else if (tmpIndex > 0) {
                    Fields_Select_Dialog.this.m_MyTableDataList.remove(tmpIndex - 1);
                    Fields_Select_Dialog.this.m_MyTableDataList.add(tmpIndex, (HashMap) Fields_Select_Dialog.this.m_MyTableDataList.get(tmpIndex - 1));
                    Fields_Select_Dialog.this.m_TableFactory.notifyDataSetChanged();
                } else {
                    Common.ShowDialog("选择的字段在最前面,不能再上移.");
                }
            }
        });
        ((Button) this._Dialog.findViewById(R.id.btn_Down)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Fields_Select_Dialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                int tmpIndex = Fields_Select_Dialog.this.m_TableFactory.getSelectItemIndex();
                if (tmpIndex < 0) {
                    Common.ShowDialog("请按字段名称选择需要上移的字段.");
                } else if (tmpIndex == Fields_Select_Dialog.this.m_MyTableDataList.size() - 1) {
                    Common.ShowDialog("选择的字段在最底下,不能再下移.");
                } else {
                    Fields_Select_Dialog.this.m_MyTableDataList.remove(tmpIndex);
                    Fields_Select_Dialog.this.m_MyTableDataList.add(tmpIndex + 1, (HashMap) Fields_Select_Dialog.this.m_MyTableDataList.get(tmpIndex));
                    Fields_Select_Dialog.this.m_TableFactory.notifyDataSetChanged();
                }
            }
        });
    }

    public void SetLayer(FeatureLayer layer) {
        this.m_Layer = layer;
    }

    public void SetSelectFieldNames(String _SelectFieldNames) {
        this.m_SelectFieldNames = _SelectFieldNames;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshFields() {
        this.m_MyTableDataList = new ArrayList();
        if (this.m_Layer != null) {
            this.m_TableFactory = new MyTableFactory();
            this.m_TableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.fields_list), "自定义", "选择,字段名称", "checkbox,text", new int[]{-20, -80}, this.pCallback);
            boolean tmpBool = true;
            int[] tmpInts = this.m_Layer.GetFieldIndexsArray();
            if (tmpInts != null && tmpInts.length > 0) {
                List<LayerField> tmpFields = this.m_Layer.GetFieldList();
                for (int tmpI : tmpInts) {
                    LayerField tmpLayerField = tmpFields.get(tmpI);
                    HashMap tmpHashMap = new HashMap();
                    if (!this.m_SelectFieldNames.contains(tmpLayerField.GetFieldName())) {
                        tmpHashMap.put("D1", false);
                        tmpHashMap.put("D2", tmpLayerField.GetFieldName());
                        tmpHashMap.put("D3", tmpLayerField.GetDataFieldName());
                        this.m_MyTableDataList.add(tmpHashMap);
                    } else {
                        tmpHashMap.put("D1", true);
                        tmpHashMap.put("D2", tmpLayerField.GetFieldName());
                        tmpHashMap.put("D3", tmpLayerField.GetDataFieldName());
                        this.m_MyTableDataList.add(tmpHashMap);
                    }
                }
                tmpBool = false;
            }
            if (tmpBool) {
                for (Map.Entry entry : this.m_Layer.getFieldsNameArray().entrySet()) {
                    HashMap tmpHashMap2 = new HashMap();
                    String tempKeyStr = (String) entry.getKey();
                    if (!this.m_SelectFieldNames.contains(tempKeyStr)) {
                        tmpHashMap2.put("D1", false);
                        tmpHashMap2.put("D2", tempKeyStr);
                        tmpHashMap2.put("D3", entry.getValue().toString());
                        this.m_MyTableDataList.add(tmpHashMap2);
                    } else {
                        tmpHashMap2.put("D1", true);
                        tmpHashMap2.put("D2", tempKeyStr);
                        tmpHashMap2.put("D3", entry.getValue().toString());
                        this.m_MyTableDataList.add(tmpHashMap2);
                    }
                }
            }
            this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2"}, this.pCallback);
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Fields_Select_Dialog.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Fields_Select_Dialog.this.refreshFields();
            }
        });
        this._Dialog.show();
    }
}
