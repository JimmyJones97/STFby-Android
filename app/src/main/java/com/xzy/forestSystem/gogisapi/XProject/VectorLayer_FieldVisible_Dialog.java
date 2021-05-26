package  com.xzy.forestSystem.gogisapi.XProject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.BaseAdapter;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Others.DataDictionary.ConvertFieldName_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VectorLayer_FieldVisible_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private FeatureLayer m_EditLayer;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_TableFactory;
    private ICallback pCallback;

    public VectorLayer_FieldVisible_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_EditLayer = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayer_FieldVisible_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("确定")) {
                    try {
                        if (VectorLayer_FieldVisible_Dialog.this.m_EditLayer != null) {
                            int tid = -1;
                            for (LayerField tmpLayerField : VectorLayer_FieldVisible_Dialog.this.m_EditLayer.GetFieldList()) {
                                tid++;
                                HashMap<String, Object> temphash = (HashMap) VectorLayer_FieldVisible_Dialog.this.m_MyTableDataList.get(tid);
                                if (temphash != null) {
                                    tmpLayerField.SetFieldVisible(Boolean.parseBoolean(temphash.get("D1").toString()));
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                    if (VectorLayer_FieldVisible_Dialog.this.m_Callback != null) {
                        VectorLayer_FieldVisible_Dialog.this.m_Callback.OnClick("矢量背景图层字段", VectorLayer_FieldVisible_Dialog.this.m_EditLayer);
                    }
                    VectorLayer_FieldVisible_Dialog.this._Dialog.dismiss();
                } else if (paramString.equals("单击选择行")) {
                    if (pObject != null) {
                        Layer_Field_Dialog tempDialog = new Layer_Field_Dialog();
                        tempDialog.SetEditLayer(VectorLayer_FieldVisible_Dialog.this.m_EditLayer);
                        tempDialog.SetEditField((LayerField) ((HashMap) pObject).get("D7"));
                        tempDialog.setAllowEditFieldType(false);
                        tempDialog.SetCallback(VectorLayer_FieldVisible_Dialog.this.pCallback);
                        tempDialog.ShowDialog();
                    }
                } else if (paramString.equals("字段编辑") && pObject != null) {
                    VectorLayer_FieldVisible_Dialog.this.LoadLayerInfo();
                }
            }
        };
        this.m_MyTableDataList = new ArrayList();
        this.m_TableFactory = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.vectorlayer_fieldvisible_dialog);
        this._Dialog.Resize(1.0f, PubVar.DialogHeightRatio);
        this._Dialog.SetCaption("矢量图层字段");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.buttonSelectAll_bkfield).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectDe_bkfield).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_VectorLayerMoreTools).setOnClickListener(new ViewClick());
        Common.SetSpinnerListData(this._Dialog, "图层类型", Common.StrArrayToList(new String[]{"点", "线", "面"}), (int) R.id.sp_type);
        this.m_TableFactory = new MyTableFactory();
        this.m_TableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.in_bklayerFieldsListview), "自定义", "显示,名称,类型,长度", "checkbox,text,text,text", new int[]{-15, -45, -20, -20}, this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String paramString) {
        if (paramString.equals("全部显示")) {
            for (HashMap<String, Object> temphash : this.m_MyTableDataList) {
                temphash.put("D1", true);
            }
            refreshList();
        } else if (paramString.equals("反选显示")) {
            for (HashMap<String, Object> temphash2 : this.m_MyTableDataList) {
                temphash2.put("D1", Boolean.valueOf(!Boolean.parseBoolean(temphash2.get("D1").toString())));
            }
            refreshList();
        } else if (paramString.equals("更多工具")) {
            if (this.m_EditLayer.GetFieldList().size() > 0) {
                new AlertDialog.Builder(this._Dialog.getContext(), 3).setTitle("请选择工具:").setSingleChoiceItems(new String[]{"根据字典转换字段名称为代码", "根据字典转换字段代码为名称", "自动根据字段名称关联字典", "自动根据字段名称(代码)关联字典"}, -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayer_FieldVisible_Dialog.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface arg0, int arg1) {
                        ConvertFieldName_Dialog tmpDiaolg = new ConvertFieldName_Dialog();
                        List<LayerField> tempFieldList = VectorLayer_FieldVisible_Dialog.this.m_EditLayer.GetFieldList();
                        tmpDiaolg.SetConvertType(arg1);
                        tmpDiaolg.SetLayerFields(tempFieldList);
                        tmpDiaolg.SetCallback(VectorLayer_FieldVisible_Dialog.this.pCallback);
                        tmpDiaolg.ShowDialog();
                        arg0.dismiss();
                    }
                }).show();
            } else {
                Common.ShowDialog("当前图层还没有定义任何属性字段.");
            }
        }
    }

    private void refreshList() {
        try {
            ((BaseAdapter) this.m_TableFactory.getListView_Scroll().getAdapter()).notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void LoadLayerInfo() {
        this.m_MyTableDataList.clear();
        if (this.m_EditLayer != null) {
            for (LayerField tempLayer : this.m_EditLayer.GetFieldList()) {
                HashMap tmpHashMap = new HashMap();
                tmpHashMap.put("D1", Boolean.valueOf(tempLayer.GetFieldVisible()));
                tmpHashMap.put("D2", tempLayer.GetFieldName());
                tmpHashMap.put("D3", tempLayer.GetFieldTypeName());
                tmpHashMap.put("D4", Integer.valueOf(tempLayer.GetFieldSize()));
                tmpHashMap.put("D6", tempLayer.GetDataFieldName());
                tmpHashMap.put("D7", tempLayer);
                this.m_MyTableDataList.add(tmpHashMap);
            }
            if (Common.GetTextValueOnID(this._Dialog, (int) R.id.et_name).equals("")) {
                Common.SetTextViewValueOnID(this._Dialog, (int) R.id.et_name, this.m_EditLayer.GetLayerName());
            }
            Common.SetValueToView(this.m_EditLayer.GetLayerTypeName(), this._Dialog.findViewById(R.id.sp_type));
            this._Dialog.findViewById(R.id.sp_type).setEnabled(false);
        }
        this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2", "D3", "D4"}, this.pCallback);
    }

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public void SetEditLayer(FeatureLayer layer) {
        this.m_EditLayer = layer;
        if (layer != null) {
            this._Dialog.SetCaption("【" + layer.GetLayerName() + "】");
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayer_FieldVisible_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                VectorLayer_FieldVisible_Dialog.this.LoadLayerInfo();
            }
        });
        this._Dialog.show();
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View paramView) {
            VectorLayer_FieldVisible_Dialog.this.DoCommand(paramView.getTag().toString());
        }
    }
}
