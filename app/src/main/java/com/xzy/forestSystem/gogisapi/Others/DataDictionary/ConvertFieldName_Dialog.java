package  com.xzy.forestSystem.gogisapi.Others.DataDictionary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConvertFieldName_Dialog {
    private int ConvertType;
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private List<LayerField> m_LayerFields;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public void SetLayerFields(List<LayerField> LayerFields) {
        this.m_LayerFields = LayerFields;
    }

    public void SetConvertType(int convertType) {
        this.ConvertType = convertType;
    }

    public ConvertFieldName_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_LayerFields = null;
        this.ConvertType = 0;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.ConvertFieldName_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                String tmpName2;
                try {
                    if (!command.equals("转换")) {
                        return;
                    }
                    if (ConvertFieldName_Dialog.this.ConvertType == 0 || ConvertFieldName_Dialog.this.ConvertType == 1) {
                        StringBuilder tmpSB = new StringBuilder();
                        for (HashMap<String, Object> tmpHash : ConvertFieldName_Dialog.this.m_MyTableDataList) {
                            if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                                String tmpName22 = String.valueOf(tmpHash.get("D3"));
                                ((LayerField) tmpHash.get("LayerField")).SetFieldName(tmpName22);
                                if (tmpSB.length() > 0) {
                                    tmpSB.append(";");
                                }
                                tmpSB.append(String.valueOf(String.valueOf(tmpHash.get("LayerFieldIndex"))) + "," + tmpName22);
                            }
                        }
                        if (ConvertFieldName_Dialog.this.m_Callback != null) {
                            ConvertFieldName_Dialog.this.m_Callback.OnClick("转换字段名称代码返回", tmpSB.toString());
                        }
                        ConvertFieldName_Dialog.this._Dialog.dismiss();
                    } else if (ConvertFieldName_Dialog.this.ConvertType == 2 || ConvertFieldName_Dialog.this.ConvertType == 3) {
                        boolean tmpConvertBool = Common.GetCheckBoxValueOnID(ConvertFieldName_Dialog.this._Dialog, R.id.ck_ConvertToCode);
                        StringBuilder tmpSB2 = new StringBuilder();
                        for (HashMap<String, Object> tmpHash2 : ConvertFieldName_Dialog.this.m_MyTableDataList) {
                            if (Boolean.parseBoolean(String.valueOf(tmpHash2.get("D1")))) {
                                LayerField tmpLayerField = (LayerField) tmpHash2.get("LayerField");
                                if (tmpConvertBool) {
                                    tmpName2 = String.valueOf(tmpHash2.get("D4"));
                                } else {
                                    tmpName2 = String.valueOf(tmpHash2.get("D3"));
                                }
                                tmpLayerField.SetFieldName(tmpName2);
                                String tmpEnumCode = String.valueOf(tmpHash2.get("D6"));
                                tmpLayerField.SetFieldEnumCode(tmpEnumCode);
                                if (tmpSB2.length() > 0) {
                                    tmpSB2.append(";");
                                }
                                tmpSB2.append(String.valueOf(String.valueOf(tmpHash2.get("LayerFieldIndex"))) + "," + tmpName2 + "," + tmpEnumCode);
                            }
                        }
                        if (ConvertFieldName_Dialog.this.m_Callback != null) {
                            ConvertFieldName_Dialog.this.m_Callback.OnClick("转换字段名称代码返回2", tmpSB2.toString());
                        }
                        ConvertFieldName_Dialog.this._Dialog.dismiss();
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.convertfieldname_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("字段名称转换");
        this._Dialog.SetHeadButtons("1,2130837858,转换,转换", this.pCallback);
        this._Dialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.ConvertFieldName_Dialog.2
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (ConvertFieldName_Dialog.this.m_MyTableDataList.size() > 0 && keyCode == 82) {
                    new AlertDialog.Builder(ConvertFieldName_Dialog.this._Dialog.getContext(), 3).setTitle("选择操作:").setSingleChoiceItems(new String[]{"全选", "反选"}, -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.ConvertFieldName_Dialog.2.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface arg02, int arg1) {
                            if (arg1 == 0) {
                                for (HashMap<String, Object> tmpHash : ConvertFieldName_Dialog.this.m_MyTableDataList) {
                                    tmpHash.put("D1", true);
                                }
                                ConvertFieldName_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                            } else if (arg1 == 1) {
                                for (HashMap<String, Object> tmpHash2 : ConvertFieldName_Dialog.this.m_MyTableDataList) {
                                    tmpHash2.put("D1", Boolean.valueOf(!Boolean.parseBoolean(String.valueOf(tmpHash2.get("D1")))));
                                }
                                ConvertFieldName_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                            }
                            arg02.dismiss();
                        }
                    }).show();
                }
                return ConvertFieldName_Dialog.this._Dialog.onKeyDown(keyCode, event);
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshList() {
        HashMap<String, String> tmpHash2;
        try {
            if (this.ConvertType == 0 || this.ConvertType == 1) {
                if (this.ConvertType == 0) {
                    this._Dialog.SetCaption("转换字段名称为代码");
                } else {
                    this._Dialog.SetCaption("转换字段代码为名称");
                }
                String tempColumns = "选择,字段名称,转换为代码,字典来源,字典编码";
                if (this.ConvertType == 1) {
                    tempColumns = "选择,字段代码,转换为名称,字典来源,字典编码";
                }
                this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.table_fieldConvert), "自定义", tempColumns, "checkbox,text,text,text,text", new int[]{45, 90, 90, 270, 90}, null);
                this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
                if (this.m_LayerFields != null && this.m_LayerFields.size() > 0) {
                    int tmpTid = -1;
                    for (LayerField tmpField : this.m_LayerFields) {
                        tmpTid++;
                        String tmpEnumCode = tmpField.GetFieldEnumCode();
                        if (!tmpEnumCode.equals("") && (tmpHash2 = PubVar._PubCommand.m_ConfigDB.GetDataDictionaryManage().GetZDItem(tmpEnumCode)) != null) {
                            String tmpFieldName = tmpField.GetFieldName();
                            HashMap tmpHash = new HashMap();
                            tmpHash.put("D1", true);
                            tmpHash.put("D2", tmpFieldName);
                            if (this.ConvertType == 0) {
                                tmpHash.put("D3", String.valueOf(tmpHash2.get("ZDNameCode")));
                            } else {
                                tmpHash.put("D3", String.valueOf(tmpHash2.get("ZDName")));
                            }
                            tmpHash.put("D4", String.valueOf(String.valueOf(tmpHash2.get("ZDType"))) + "-" + String.valueOf(tmpHash2.get("ZDSub")) + "-" + String.valueOf(tmpHash2.get("ZDName")));
                            tmpHash.put("D5", tmpEnumCode);
                            tmpHash.put("LayerField", tmpField);
                            tmpHash.put("LayerFieldIndex", Integer.valueOf(tmpTid));
                            this.m_MyTableDataList.add(tmpHash);
                        }
                    }
                    if (this.m_MyTableDataList.size() > 0) {
                        this.m_MyTableFactory.notifyDataSetChanged();
                    }
                }
                if (this.m_MyTableDataList.size() == 0) {
                    Common.ShowDialog("该图层中没有属性字段定义为与数据字典相关联的字段.\r\n如果存在字段名称相同,可先采用\"自动根据字段名称关联字典\"或\"自动根据字段名称(代码)关联字典\".");
                    this._Dialog.dismiss();
                }
            } else if (this.ConvertType == 2 || this.ConvertType == 3) {
                boolean tmpCovertBool = false;
                if (this.ConvertType == 3) {
                    tmpCovertBool = true;
                }
                this._Dialog.findViewById(R.id.ll_bottomLL).setVisibility(0);
                this._Dialog.SetCaption("根据字段名称关联字典");
                this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.table_fieldConvert), "自定义", "选择,字段名称,细类名称,细类代码,字典来源,字典编码", "checkbox,text,text,text,text,text", new int[]{45, 90, 90, 90, 270, 90}, null);
                this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
                if (this.m_LayerFields != null && this.m_LayerFields.size() > 0) {
                    int tmpTid2 = -1;
                    for (LayerField tmpField2 : this.m_LayerFields) {
                        tmpTid2++;
                        String tmpFieldName2 = tmpField2.GetFieldName();
                        List<HashMap<String, String>> tmpLists = PubVar._PubCommand.m_ConfigDB.GetDataDictionaryManage().GetZDItemsBySub(tmpFieldName2, tmpCovertBool);
                        if (tmpLists.size() > 0) {
                            for (HashMap<String, String> tmpHash22 : tmpLists) {
                                HashMap tmpHash3 = new HashMap();
                                tmpHash3.put("D1", true);
                                tmpHash3.put("D2", tmpFieldName2);
                                tmpHash3.put("D3", String.valueOf(tmpHash22.get("ZDName")));
                                tmpHash3.put("D4", String.valueOf(tmpHash22.get("ZDNameCode")));
                                tmpHash3.put("D5", String.valueOf(String.valueOf(tmpHash22.get("ZDType"))) + "-" + String.valueOf(tmpHash22.get("ZDSub")) + "-" + String.valueOf(tmpHash22.get("ZDName")));
                                tmpHash3.put("D6", String.valueOf(tmpHash22.get("ZDBM")));
                                tmpHash3.put("LayerField", tmpField2);
                                tmpHash3.put("LayerFieldIndex", Integer.valueOf(tmpTid2));
                                this.m_MyTableDataList.add(tmpHash3);
                            }
                        }
                    }
                    if (this.m_MyTableDataList.size() > 0) {
                        this.m_MyTableFactory.notifyDataSetChanged();
                    }
                }
                if (this.m_MyTableDataList.size() == 0) {
                    Common.ShowDialog("该图层中没有属性字段名称与数据字典中的细类名称或代码相匹配.");
                    this._Dialog.dismiss();
                }
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.ConvertFieldName_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                ConvertFieldName_Dialog.this.refreshList();
            }
        });
        this._Dialog.show();
    }
}
