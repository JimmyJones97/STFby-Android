package  com.xzy.forestSystem.gogisapi.XProject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.Input_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Others.DataDictionary.ConvertFieldName_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Layer_New_Dialog {
    private XDialogTemplate _Dialog;
    private String _ProjectName;
    private ICallback m_Callback;
    private FeatureLayer m_EditLayer;
    private List<String> m_HaveLayersNameList;
    private Button m_LoadModuleBtn;
    private Button m_LoadShpModule;
    private List<HashMap<String, Object>> m_MyTableDataList;
    FeatureLayer m_OldEditLayer;
    private Button m_SaveModuleBtn;
    private LayerField m_SelectField;
    private MyTableFactory m_TableFactory;
    private ICallback pCallback;

    public Layer_New_Dialog() {
        this._Dialog = null;
        this._ProjectName = "";
        this.m_Callback = null;
        this.m_EditLayer = null;
        this.m_HaveLayersNameList = null;
        this.m_OldEditLayer = null;
        this.m_SelectField = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_New_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                FeatureLayer tmpFeatureLayer;
                int tempIndex;
                try {
                    if (paramString.equals("确定") && Layer_New_Dialog.this.SaveLayerInfo()) {
                        if (Layer_New_Dialog.this.m_OldEditLayer != null) {
                            Layer_New_Dialog.this.m_OldEditLayer = Layer_New_Dialog.this.m_EditLayer;
                            if (Layer_New_Dialog.this.m_Callback != null) {
                                Layer_New_Dialog.this.m_Callback.OnClick("编辑图层字段", Layer_New_Dialog.this.m_OldEditLayer);
                            }
                        } else if (Layer_New_Dialog.this.m_Callback != null) {
                            Layer_New_Dialog.this.m_Callback.OnClick("新建图层", Layer_New_Dialog.this.m_EditLayer);
                        }
                        Layer_New_Dialog.this._Dialog.dismiss();
                    } else if (paramString.equals("字段新建")) {
                        LayerField tempField = (LayerField) pObject;
                        if (tempField != null) {
                            HashMap tmpHashMap = new HashMap();
                            tmpHashMap.put("D1", Boolean.valueOf(tempField.GetFieldVisible()));
                            tmpHashMap.put("D2", tempField.GetFieldName());
                            tmpHashMap.put("D3", tempField.GetFieldTypeName());
                            tmpHashMap.put("D4", Integer.valueOf(tempField.GetFieldSize()));
                            tmpHashMap.put("D5", tempField.GetFieldEnumCode());
                            tmpHashMap.put("D6", tempField.GetDataFieldName());
                            tempField.setFieldIndex(Layer_New_Dialog.this.m_MyTableDataList.size());
                            Layer_New_Dialog.this.m_MyTableDataList.add(tmpHashMap);
                            Layer_New_Dialog.this.refreshList();
                        }
                    } else if (paramString.equals("字段编辑")) {
                        LayerField tempField2 = (LayerField) pObject;
                        if (tempField2 != null && (tempIndex = Layer_New_Dialog.this.findIndexNyDataFieldName(tempField2.GetDataFieldName())) >= 0) {
                            HashMap tmpHashMap2 = (HashMap) Layer_New_Dialog.this.m_MyTableDataList.get(tempIndex);
                            tmpHashMap2.put("D1", Boolean.valueOf(tempField2.GetFieldVisible()));
                            tmpHashMap2.put("D2", tempField2.GetFieldName());
                            tmpHashMap2.put("D3", tempField2.GetFieldTypeName());
                            tmpHashMap2.put("D4", Integer.valueOf(tempField2.GetFieldSize()));
                            tmpHashMap2.put("D5", tempField2.GetFieldEnumCode());
                            tmpHashMap2.put("D6", tempField2.GetDataFieldName());
                            Layer_New_Dialog.this.refreshList();
                        }
                    } else if (paramString.equals("列表选项") && pObject != null) {
                        String tempStr = ((HashMap) pObject).get("D2").toString();
                        for (LayerField tempField3 : Layer_New_Dialog.this.m_EditLayer.GetFieldList()) {
                            if (tempField3.GetFieldName().equals(tempStr)) {
                                Layer_New_Dialog.this.m_SelectField = tempField3;
                                Layer_New_Dialog.this.SetButtonEnable(true);
                                return;
                            }
                        }
                    } else if (paramString.contains("输入参数")) {
                        Object[] tmpObjs = (Object[]) pObject;
                        if (tmpObjs != null && tmpObjs.length >= 1) {
                            String tmplayerName = tmpObjs[1].toString();
                            if (PubVar._PubCommand.m_UserConfigDB.IsExistLayerModule(tmplayerName)) {
                                Common.ShowDialog("图层模板【" + tmplayerName + "】已经存在,请输入其他图层模板名称.");
                            } else if (PubVar._PubCommand.m_UserConfigDB.SaveFeatureLayerModule(tmplayerName, Layer_New_Dialog.this.m_EditLayer)) {
                                Common.ShowDialog("保存图层模板【" + tmplayerName + "】成功.");
                            }
                        }
                    } else if (paramString.contains("图层模板选择返回")) {
                        Layer_New_Dialog.this.loadLayerModule(String.valueOf(pObject));
                    } else if (paramString.contains("选择文件")) {
                        String[] tempPath2 = pObject.toString().split(";");
                        if (tempPath2 != null && tempPath2.length > 1) {
                            SelectShpFields_Dialog tmpDialog = new SelectShpFields_Dialog();
                            tmpDialog.ShpFilePath = tempPath2[1];
                            tmpDialog.SetCallback(Layer_New_Dialog.this.pCallback);
                            tmpDialog.ShowDialog();
                        }
                    } else if (paramString.contains("选择SHP模板返回") && pObject != null) {
                        List<String> tmpHasFields = new ArrayList<>();
                        for (Object object : (List) pObject) {
                            LayerField tempField4 = (LayerField) object;
                            if (tempField4 != null) {
                                if (!Layer_New_Dialog.this.checkHasField(tempField4.GetFieldName())) {
                                    HashMap tmpHashMap3 = new HashMap();
                                    tmpHashMap3.put("D1", Boolean.valueOf(tempField4.GetFieldVisible()));
                                    tmpHashMap3.put("D2", tempField4.GetFieldName());
                                    tmpHashMap3.put("D3", tempField4.GetFieldTypeName());
                                    tmpHashMap3.put("D4", Integer.valueOf(tempField4.GetFieldSize()));
                                    tmpHashMap3.put("D5", tempField4.GetFieldEnumCode());
                                    tmpHashMap3.put("D6", tempField4.GetDataFieldName());
                                    Layer_New_Dialog.this.m_MyTableDataList.add(tmpHashMap3);
                                    tempField4.SetDataFieldName(Layer_New_Dialog.this.getUnUsedDataFieldName());
                                    Layer_New_Dialog.this.m_EditLayer.GetFieldList().add(tempField4);
                                } else {
                                    tmpHasFields.add(tempField4.GetFieldName());
                                }
                            }
                        }
                        Layer_New_Dialog.this.refreshList();
                        if (tmpHasFields.size() > 0) {
                            Common.ShowDialog("以下字段已经存在:\r\n" + Common.CombineStrings("\r\n", tmpHasFields));
                        }
                    } else if (paramString.equals("转换字段名称代码返回")) {
                        if (pObject != null) {
                            String[] tempStrs = String.valueOf(pObject).split(";");
                            if (tempStrs != null && tempStrs.length > 0) {
                                int length = tempStrs.length;
                                for (int i = 0; i < length; i++) {
                                    String[] tmpStrs02 = tempStrs[i].split(",");
                                    if (tmpStrs02 != null && tmpStrs02.length > 1) {
                                        ((HashMap) Layer_New_Dialog.this.m_MyTableDataList.get(Integer.parseInt(tmpStrs02[0]))).put("D2", tmpStrs02[1]);
                                    }
                                }
                            }
                            Layer_New_Dialog.this.refreshList();
                        }
                    } else if (paramString.equals("转换字段名称代码返回2")) {
                        if (pObject != null) {
                            String[] tempStrs2 = String.valueOf(pObject).split(";");
                            if (tempStrs2 != null && tempStrs2.length > 0) {
                                int length2 = tempStrs2.length;
                                for (int i2 = 0; i2 < length2; i2++) {
                                    String[] tmpStrs022 = tempStrs2[i2].split(",");
                                    if (tmpStrs022 != null && tmpStrs022.length > 2) {
                                        HashMap tmpHashMap4 = (HashMap) Layer_New_Dialog.this.m_MyTableDataList.get(Integer.parseInt(tmpStrs022[0]));
                                        tmpHashMap4.put("D2", tmpStrs022[1]);
                                        tmpHashMap4.put("D5", tmpStrs022[2]);
                                    }
                                }
                            }
                            Layer_New_Dialog.this.refreshList();
                        }
                    } else if (paramString.equals("选择图层") && (tmpFeatureLayer = PubVar._PubCommand.m_ProjectDB.getFeatureLayerByID(pObject.toString())) != null) {
                        Common.SetValueToView(tmpFeatureLayer.GetLayerTypeName(), Layer_New_Dialog.this._Dialog.findViewById(R.id.sp_type));
                        for (LayerField tempField5 : tmpFeatureLayer.GetFieldList()) {
                            if (tempField5 != null && !Layer_New_Dialog.this.checkHasField(tempField5.GetFieldName())) {
                                HashMap<String, Object> tmpHashMap5 = new HashMap<>();
                                tmpHashMap5.put("D1", Boolean.valueOf(tempField5.GetFieldVisible()));
                                tmpHashMap5.put("D2", tempField5.GetFieldName());
                                tmpHashMap5.put("D3", tempField5.GetFieldTypeName());
                                tmpHashMap5.put("D4", Integer.valueOf(tempField5.GetFieldSize()));
                                tmpHashMap5.put("D5", tempField5.GetFieldEnumCode());
                                tmpHashMap5.put("D6", tempField5.GetDataFieldName());
                                Layer_New_Dialog.this.m_MyTableDataList.add(tmpHashMap5);
                                tempField5.SetDataFieldName(Layer_New_Dialog.this.getUnUsedDataFieldName());
                                Layer_New_Dialog.this.m_EditLayer.GetFieldList().add(tempField5);
                            }
                        }
                        Layer_New_Dialog.this.refreshList();
                        Common.ShowToast("从图层【" + tmpFeatureLayer.GetLayerName() + "】复制属性字段结构成功!");
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_MyTableDataList = new ArrayList();
        this.m_TableFactory = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.layer_new_dialog);
        this._Dialog.Resize(1.0f, PubVar.DialogHeightRatio);
        this._Dialog.SetCaption("新建图层");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.pln_add).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.pln_edit).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.pln_delete).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectAll_field).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectDe_field).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_LayerNewMoreTools).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.pln_up).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.pln_down).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_FromOtherLayer).setOnClickListener(new ViewClick());
        Common.SetSpinnerListData(this._Dialog, "图层类型", Common.StrArrayToList(new String[]{"点", "线", "面"}), (int) R.id.sp_type);
        SetButtonEnable(false);
        this.m_TableFactory = new MyTableFactory();
        this.m_TableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.in_listview), "自定义", "显示,名称,类型,长度,数据字典", "checkbox,text,text,text,text", new int[]{-15, -35, -15, -15, -20}, this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String paramString) {
        try {
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
            } else if (paramString.equals("增加字段")) {
                Layer_Field_Dialog tempDialog = new Layer_Field_Dialog();
                tempDialog.SetEditLayer(this.m_EditLayer);
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
            } else if (paramString.equals("字段属性")) {
                Layer_Field_Dialog tempDialog2 = new Layer_Field_Dialog();
                tempDialog2.SetEditLayer(this.m_EditLayer);
                tempDialog2.SetEditField(this.m_SelectField);
                tempDialog2.SetCallback(this.pCallback);
                tempDialog2.ShowDialog();
            } else if (paramString.equals("删除字段")) {
                Common.ShowYesNoDialog(this._Dialog.getContext(), "是否删除字段【" + this.m_SelectField.GetFieldName() + "】？", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_New_Dialog.2
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString2, Object pObject) {
                        if (paramString2.equals("YES")) {
                            List<LayerField> tempFieldList = Layer_New_Dialog.this.m_EditLayer.GetFieldList();
                            for (LayerField tmpField : tempFieldList) {
                                if (tmpField.GetFieldName().equals(Layer_New_Dialog.this.m_SelectField.GetFieldName())) {
                                    tempFieldList.remove(tmpField);
                                    int tempIndex = Layer_New_Dialog.this.findIndexNyName(Layer_New_Dialog.this.m_SelectField.GetFieldName());
                                    if (tempIndex >= 0) {
                                        Layer_New_Dialog.this.m_MyTableDataList.remove(tempIndex);
                                        Layer_New_Dialog.this.refreshList();
                                        return;
                                    }
                                    return;
                                }
                            }
                        }
                    }
                });
            } else if (paramString.equals("更多工具")) {
                if (this.m_EditLayer.GetFieldList().size() > 0) {
                    new AlertDialog.Builder(this._Dialog.getContext(), 3).setTitle("请选择工具:").setSingleChoiceItems(new String[]{"根据字典转换字段名称为代码", "根据字典转换字段代码为名称", "自动根据字段名称关联字典", "自动根据字段名称(代码)关联字典"}, -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_New_Dialog.3
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface arg0, int arg1) {
                            ConvertFieldName_Dialog tmpDiaolg = new ConvertFieldName_Dialog();
                            List<LayerField> tempFieldList = Layer_New_Dialog.this.m_EditLayer.GetFieldList();
                            tmpDiaolg.SetConvertType(arg1);
                            tmpDiaolg.SetLayerFields(tempFieldList);
                            tmpDiaolg.SetCallback(Layer_New_Dialog.this.pCallback);
                            tmpDiaolg.ShowDialog();
                            arg0.dismiss();
                        }
                    }).show();
                } else {
                    Common.ShowDialog("当前图层还没有定义任何属性字段.");
                }
            } else if (paramString.equals("字段上移")) {
                if (this.m_SelectField != null) {
                    int tmpIndex = this.m_SelectField.getFieldIndex();
                    if (tmpIndex > 0) {
                        List<LayerField> tmpList = this.m_EditLayer.GetFieldList();
                        tmpList.get(tmpIndex - 1).setFieldIndex(tmpIndex);
                        this.m_SelectField.setFieldIndex(tmpIndex - 1);
                        tmpList.remove(tmpIndex);
                        tmpList.add(tmpIndex - 1, this.m_SelectField);
                        this.m_MyTableDataList.remove(tmpIndex);
                        this.m_MyTableDataList.add(tmpIndex - 1, this.m_MyTableDataList.get(tmpIndex));
                        this.m_TableFactory.SetSelectItemIndex(tmpIndex - 1, null);
                        refreshList();
                        return;
                    }
                    Common.ShowDialog("已经移动到第一行,不能再上移.");
                }
            } else if (paramString.equals("字段下移")) {
                if (this.m_SelectField != null) {
                    int tmpIndex2 = this.m_SelectField.getFieldIndex();
                    List<LayerField> tmpList2 = this.m_EditLayer.GetFieldList();
                    if (tmpIndex2 < tmpList2.size() - 1) {
                        tmpList2.get(tmpIndex2 + 1).setFieldIndex(tmpIndex2);
                        this.m_SelectField.setFieldIndex(tmpIndex2 + 1);
                        tmpList2.remove(tmpIndex2);
                        tmpList2.add(tmpIndex2 + 1, this.m_SelectField);
                        this.m_MyTableDataList.remove(tmpIndex2);
                        this.m_MyTableDataList.add(tmpIndex2 + 1, this.m_MyTableDataList.get(tmpIndex2));
                        this.m_TableFactory.SetSelectItemIndex(tmpIndex2 + 1, null);
                        refreshList();
                        return;
                    }
                    Common.ShowDialog("已经移动到最后,不能再下移.");
                }
            } else if (paramString.equals("加载其他图层模板")) {
                List<XLayer> m_XLayerList = new ArrayList<>();
                m_XLayerList.addAll(PubVar._Map.getGeoLayers().getList());
                m_XLayerList.addAll(PubVar._Map.getVectorGeoLayers().getList());
                Layer_Select_Dialog tempDialog3 = new Layer_Select_Dialog();
                tempDialog3.SetTitle("选择图层");
                tempDialog3.SetLayersList(m_XLayerList);
                tempDialog3.SetLayerSelectType(2);
                tempDialog3.SetCallback(this.pCallback);
                tempDialog3.ShowDialog();
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshList() {
        try {
            ((BaseAdapter) this.m_TableFactory.getListView_Scroll().getAdapter()).notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int findIndexNyName(String fieldName) {
        int result = -1;
        for (HashMap<String, Object> tempHash : this.m_MyTableDataList) {
            result++;
            if (tempHash.get("D2").toString().equals(fieldName)) {
                return result;
            }
        }
        return -1;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int findIndexNyDataFieldName(String datefieldName) {
        int result = -1;
        for (HashMap<String, Object> tempHash : this.m_MyTableDataList) {
            result++;
            if (tempHash.get("D6").toString().equals(datefieldName)) {
                return result;
            }
        }
        return -1;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    @SuppressLint("WrongConstant")
    private void LoadLayerInfo() {
        this.m_SaveModuleBtn = (Button) this._Dialog.findViewById(R.id.btn_SaveFieldModule);
        this.m_SaveModuleBtn.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_New_Dialog.4
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (Layer_New_Dialog.this.m_EditLayer == null) {
                    return;
                }
                if (Layer_New_Dialog.this.m_EditLayer.GetFieldList().size() == 0) {
                    Common.ShowDialog("图层中没有任何属性字段,不能保存为图层模板.");
                    return;
                }
                Input_Dialog tempDialog = new Input_Dialog();
                tempDialog.setValues("保存图层模板", "图层模板名称:", "", "提示:请输入图层模板名称,该名称不能与已有的图层模板名称重复.");
                tempDialog.SetCallback(Layer_New_Dialog.this.pCallback);
                tempDialog.ShowDialog();
            }
        });
        this.m_LoadShpModule = (Button) this._Dialog.findViewById(R.id.btn_FromShp);
        this.m_LoadShpModule.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_New_Dialog.5
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                FileSelector_Dialog tempDialog = new FileSelector_Dialog(".shp;", false);
                Common.ShowToast("请选择SHP文件.");
                tempDialog.SetCallback(Layer_New_Dialog.this.pCallback);
                tempDialog.ShowDialog();
            }
        });
        this.m_MyTableDataList.clear();
        if (this.m_EditLayer != null) {
            this._Dialog.findViewById(R.id.btn_LoadModule).setVisibility(8);
            this._Dialog.SetCaption("【" + this.m_EditLayer.GetLayerName() + "】");
            int tmpTid = -1;
            for (LayerField tempLayer : this.m_EditLayer.GetFieldList()) {
                tmpTid++;
                HashMap tmpHashMap = new HashMap();
                tmpHashMap.put("D1", Boolean.valueOf(tempLayer.GetFieldVisible()));
                tmpHashMap.put("D2", tempLayer.GetFieldName());
                tmpHashMap.put("D3", tempLayer.GetFieldTypeName());
                tmpHashMap.put("D4", Integer.valueOf(tempLayer.GetFieldSize()));
                tmpHashMap.put("D5", tempLayer.GetFieldEnumCode());
                tmpHashMap.put("D6", tempLayer.GetDataFieldName());
                tmpHashMap.put("Index", Integer.valueOf(tmpTid));
                tempLayer.setFieldIndex(tmpTid);
                this.m_MyTableDataList.add(tmpHashMap);
            }
            if (Common.GetTextValueOnID(this._Dialog, (int) R.id.et_name).equals("")) {
                Common.SetTextViewValueOnID(this._Dialog, (int) R.id.et_name, this.m_EditLayer.GetLayerName());
            }
            Common.SetValueToView(this.m_EditLayer.GetLayerTypeName(), this._Dialog.findViewById(R.id.sp_type));
            this._Dialog.findViewById(R.id.sp_type).setEnabled(false);
        } else {
            this.m_EditLayer = new FeatureLayer();
            this.m_EditLayer.SetEditMode(EEditMode.NEW);
            this._Dialog.findViewById(R.id.sp_type).setEnabled(true);
            this.m_LoadModuleBtn = (Button) this._Dialog.findViewById(R.id.btn_LoadModule);
            this.m_LoadModuleBtn.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_New_Dialog.6
                @Override // android.view.View.OnClickListener
                public void onClick(View arg0) {
                    LayerModule_Dialog tmpDialog = new LayerModule_Dialog();
                    tmpDialog.SetCallback(Layer_New_Dialog.this.pCallback);
                    tmpDialog.ShowDialog();
                }
            });
        }
        SetButtonEnable(false);
        this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2", "D3", "D4", "D5"}, this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean SaveLayerInfo() {
        boolean result = false;
        try {
            String tempLayerName = Common.GetTextValueOnID(this._Dialog, (int) R.id.et_name);
            String tempLayerType = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_type);
            if (tempLayerName.equals("")) {
                Common.ShowDialog(this._Dialog.getContext(), "【图层名称】不允许为空值！\r\n");
                return false;
            }
            if (this.m_HaveLayersNameList != null && this.m_HaveLayersNameList.size() > 0) {
                for (String tmpLayerName : this.m_HaveLayersNameList) {
                    if (tmpLayerName.equals(tempLayerName)) {
                        Common.ShowDialog(this._Dialog.getContext(), "【图层名称】不允许重复！\r\n");
                        return false;
                    }
                }
            }
            if (this.m_EditLayer.GetFieldList().size() == 0) {
                Common.ShowDialog(this._Dialog.getContext(), "【字段】数量不允许为0个！\r\n");
                return false;
            }
            if (this.m_EditLayer.GetEditMode() != EEditMode.NEW) {
                this.m_EditLayer.SetEditMode(EEditMode.EDIT);
            }
            try {
                int tid = -1;
                for (LayerField tmpLayerField : this.m_EditLayer.GetFieldList()) {
                    tid++;
                    boolean tempBool = Boolean.parseBoolean(this.m_MyTableDataList.get(tid).get("D1").toString());
                    tmpLayerField.SetFieldVisible(tempBool);
                    tmpLayerField.setFieldIndex(tid);
                }
            } catch (Exception e) {
            }
            this.m_EditLayer.SetLayerName(tempLayerName);
            this.m_EditLayer.SetLayerTypeName(tempLayerType);
            result = true;
            return result;
        } catch (Exception e2) {
        }
        return result;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    @SuppressLint("WrongConstant")
    private void SetButtonEnable(boolean paramBoolean) {
        if (paramBoolean) {
            this._Dialog.findViewById(R.id.pln_edit).setVisibility(0);
            this._Dialog.findViewById(R.id.pln_delete).setVisibility(0);
            this._Dialog.findViewById(R.id.pln_up).setVisibility(0);
            this._Dialog.findViewById(R.id.pln_down).setVisibility(0);
            return;
        }
        this._Dialog.findViewById(R.id.pln_edit).setVisibility(8);
        this._Dialog.findViewById(R.id.pln_delete).setVisibility(8);
        this._Dialog.findViewById(R.id.pln_up).setVisibility(8);
        this._Dialog.findViewById(R.id.pln_down).setVisibility(8);
    }

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public void SetEditLayer(FeatureLayer layer) {
        this.m_OldEditLayer = layer;
        if (layer != null) {
            this.m_EditLayer = layer.Clone();
        } else {
            this.m_EditLayer = null;
        }
    }

    public void SetHaveLayerList(List<String> LayersNameList) {
        this.m_HaveLayersNameList = LayersNameList;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private String getUnUsedDataFieldName() {
        List<LayerField> tmpFields = this.m_EditLayer.GetFieldList();
        if (tmpFields.size() <= 0) {
            return "F1";
        }
        int tmpI = 1;
        while (tmpI < 255) {
            String tmpFID = "F" + String.valueOf(tmpI);
            boolean tmpBool = true;
            Iterator<LayerField> tmpIter = tmpFields.iterator();
            while (true) {
                if (tmpIter.hasNext()) {
                    if (tmpIter.next().GetDataFieldName().equals(tmpFID)) {
                        tmpI++;
                        tmpBool = false;
                        continue;
                    }
                } else {
                    break;
                }
            }
            if (tmpBool) {
                break;
            }
        }
        return "F" + String.valueOf(tmpI);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean checkHasField(String fieldName) {
        List<LayerField> tmpFields = this.m_EditLayer.GetFieldList();
        if (tmpFields.size() > 0) {
            for (LayerField layerField : tmpFields) {
                if (layerField.GetFieldName().equals(fieldName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setAllowEditLayerName(boolean allowEdit) {
        if (allowEdit) {
            this._Dialog.findViewById(R.id.et_name).setEnabled(true);
        } else {
            this._Dialog.findViewById(R.id.et_name).setEnabled(false);
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_New_Dialog.7
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Layer_New_Dialog.this.LoadLayerInfo();
            }
        });
        this._Dialog.show();
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View paramView) {
            Layer_New_Dialog.this.DoCommand(paramView.getTag().toString());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean loadLayerModule(String layerModuleName) {
        FeatureLayer tmpLayer;
        if (layerModuleName != null) {
            try {
                if (!layerModuleName.equals("") && (tmpLayer = PubVar._PubCommand.m_UserConfigDB.GetFeatureLayerModuleByName(layerModuleName)) != null) {
                    this.m_EditLayer.SetVisible(tmpLayer.GetVisible());
                    this.m_EditLayer.SetLayerName(tmpLayer.GetLayerName());
                    this.m_EditLayer.SetLayerTypeName(tmpLayer.GetLayerTypeName());
                    this.m_EditLayer.SetSymbolName(tmpLayer.GetSymbolName());
                    this.m_EditLayer.SetTransparent(tmpLayer.GetTransparet());
                    this.m_EditLayer.SetFieldList(tmpLayer.GetFieldListStr());
                    this.m_EditLayer.SetIfLabel(tmpLayer.GetIfLabel());
                    this.m_EditLayer.SetLabelField(tmpLayer.GetLabelField());
                    this.m_EditLayer.SetLabelFont(tmpLayer.GetLabelFont());
                    this.m_EditLayer.SetLabelSplitChar(tmpLayer.getLabelSplitChar());
                    this.m_EditLayer.SetMaxScale(tmpLayer.GetMaxScale());
                    this.m_EditLayer.SetMinScale(tmpLayer.GetMinScale());
                    this.m_MyTableDataList.clear();
                    for (LayerField tempLayer : this.m_EditLayer.GetFieldList()) {
                        HashMap tmpHashMap = new HashMap();
                        tmpHashMap.put("D1", Boolean.valueOf(tempLayer.GetFieldVisible()));
                        tmpHashMap.put("D2", tempLayer.GetFieldName());
                        tmpHashMap.put("D3", tempLayer.GetFieldTypeName());
                        tmpHashMap.put("D4", Integer.valueOf(tempLayer.GetFieldSize()));
                        tmpHashMap.put("D5", tempLayer.GetFieldEnumCode());
                        tmpHashMap.put("D6", tempLayer.GetDataFieldName());
                        this.m_MyTableDataList.add(tmpHashMap);
                    }
                    if (Common.GetTextValueOnID(this._Dialog, (int) R.id.et_name).equals("")) {
                        Common.SetTextViewValueOnID(this._Dialog, (int) R.id.et_name, this.m_EditLayer.GetLayerName());
                    }
                    Common.SetValueToView(this.m_EditLayer.GetLayerTypeName(), this._Dialog.findViewById(R.id.sp_type));
                    refreshList();
                    return true;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }
}
