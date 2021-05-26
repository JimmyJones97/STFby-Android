package  com.xzy.forestSystem.gogisapi.Display;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.ImageSpinnerDialog;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.Input_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.XProject.Fields_Select_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ClassifiedRender_Setting_Dialog {
    private XDialogTemplate _Dialog;
    private InputSpinner _LabelFieldSpinnerDialog;
    private ICallback m_Callback;
    private ISymbol m_DefaultSymbol;
    private FeatureLayer m_EditLayer;
    private GeoLayer m_GeoLayer;
    private MyTableFactory m_LineHeaderListViewFactory;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private ClassifiedRender m_Render;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public ClassifiedRender_Setting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_EditLayer = null;
        this.m_Render = null;
        this.m_DefaultSymbol = null;
        this.m_GeoLayer = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Display.ClassifiedRender_Setting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                Object[] tmpObjs;
                try {
                    if (paramString.equals("确定")) {
                        String tempLabelDataFields = ClassifiedRender_Setting_Dialog.this.m_EditLayer.ConvertToDataField(Common.GetSpinnerValueOnID(ClassifiedRender_Setting_Dialog.this._Dialog, R.id.sp_labelGroup));
                        if (!tempLabelDataFields.equals("")) {
                            ClassifiedRender_Setting_Dialog.this.m_Render.SetUniqueValueField(tempLabelDataFields);
                            ClassifiedRender_Setting_Dialog.this.m_Render.setDefaultSymbol(ClassifiedRender_Setting_Dialog.this.m_DefaultSymbol);
                            List<String> tmpValues = new ArrayList<>();
                            List<ISymbol> tmpSymbols = new ArrayList<>();
                            if (ClassifiedRender_Setting_Dialog.this.m_MyTableDataList.size() > 0) {
                                for (HashMap<String, Object> tempHash : ClassifiedRender_Setting_Dialog.this.m_MyTableDataList) {
                                    if (tempHash != null) {
                                        String tempValue = tempHash.get("D2").toString();
                                        ISymbol tempSymbol = (ISymbol) tempHash.get("D4");
                                        if (!(tempValue == null || tempSymbol == null)) {
                                            tmpValues.add(tempValue);
                                            tmpSymbols.add(tempSymbol);
                                        }
                                    }
                                }
                            }
                            ClassifiedRender_Setting_Dialog.this.m_Render.setUniqueValueList(tmpValues);
                            ClassifiedRender_Setting_Dialog.this.m_Render.setSymbolList(tmpSymbols);
                            if (ClassifiedRender_Setting_Dialog.this.m_Callback != null) {
                                ClassifiedRender_Setting_Dialog.this.m_Callback.OnClick("多值符号设置", new Object[]{ClassifiedRender_Setting_Dialog.this.m_EditLayer.GetLayerID(), ClassifiedRender_Setting_Dialog.this.m_Render});
                            }
                            ClassifiedRender_Setting_Dialog.this._Dialog.dismiss();
                            return;
                        }
                        Common.ShowToast("请选择字段组合.");
                    } else if (paramString.equals("单击选择行")) {
                        HashMap temphash = (HashMap) pObject;
                        SymbolSelect_Dialog tempDialog = new SymbolSelect_Dialog();
                        tempDialog.SetCallback(ClassifiedRender_Setting_Dialog.this.pCallback);
                        HashMap tmpHash = new HashMap();
                        tmpHash.put("D1", "");
                        tmpHash.put("D2", temphash.get("D3"));
                        tmpHash.put("D3", temphash.get("D4"));
                        tempDialog.SetSelectedSymbol(tmpHash);
                        tempDialog.SetSymbolTag(temphash.get("D5").toString());
                        tempDialog.SetGeoLayerType(ClassifiedRender_Setting_Dialog.this.m_EditLayer.GetLayerType());
                        tempDialog.ShowDialog();
                    } else if (paramString.contains("字段组合选择")) {
                        ClassifiedRender_Setting_Dialog.this._LabelFieldSpinnerDialog.getEditTextView().setText(pObject.toString());
                        ClassifiedRender_Setting_Dialog.this.DoCommand("重取值");
                    } else if (paramString.contains("ImageSpinnerCallback")) {
                        SymbolSelect_Dialog tempDialog2 = new SymbolSelect_Dialog();
                        tempDialog2.SetCallback(ClassifiedRender_Setting_Dialog.this.pCallback);
                        HashMap tmpHash2 = new HashMap();
                        tmpHash2.put("D1", "");
                        tmpHash2.put("D2", ClassifiedRender_Setting_Dialog.this.m_DefaultSymbol.getSymbolFigure());
                        tmpHash2.put("D3", ClassifiedRender_Setting_Dialog.this.m_DefaultSymbol);
                        tempDialog2.SetSelectedSymbol(tmpHash2);
                        tempDialog2.SetSymbolTag("-1");
                        tempDialog2.SetGeoLayerType(ClassifiedRender_Setting_Dialog.this.m_EditLayer.GetLayerType());
                        tempDialog2.ShowDialog();
                    } else if (paramString.contains("符号库")) {
                        if (pObject != null) {
                            HashMap tmpHash3 = (HashMap) pObject;
                            String tmpTag01 = tmpHash3.get("D0").toString();
                            if (tmpTag01.equals("-1")) {
                                Bitmap bitmap = (Bitmap) tmpHash3.get("D2");
                                ClassifiedRender_Setting_Dialog.this.m_DefaultSymbol = (ISymbol) tmpHash3.get("D3");
                                if (ClassifiedRender_Setting_Dialog.this.m_DefaultSymbol != null) {
                                    List<SymbolObject> tempSymbolsList = new ArrayList<>();
                                    SymbolObject tmpSymbolObject = new SymbolObject();
                                    tmpSymbolObject.SymbolName = "";
                                    tmpSymbolObject.SymbolFigure = ClassifiedRender_Setting_Dialog.this.m_DefaultSymbol.getSymbolFigure();
                                    tempSymbolsList.add(tmpSymbolObject);
                                    ((ImageSpinnerDialog) ClassifiedRender_Setting_Dialog.this._Dialog.findViewById(R.id.sp_defaultSymbol)).SetImageItemList(tempSymbolsList);
                                    return;
                                }
                                return;
                            }
                            int tempIndex = Integer.parseInt(tmpTag01);
                            if (tempIndex >= 0 && tempIndex < ClassifiedRender_Setting_Dialog.this.m_MyTableDataList.size()) {
                                HashMap tmpHash01 = (HashMap) ClassifiedRender_Setting_Dialog.this.m_MyTableDataList.get(tempIndex);
                                ClassifiedRender_Setting_Dialog.this.m_MyTableDataList.remove(tempIndex);
                                tmpHash01.put("D3", (Bitmap) tmpHash3.get("D2"));
                                tmpHash01.put("D4", (ISymbol) tmpHash3.get("D3"));
                                ClassifiedRender_Setting_Dialog.this.m_MyTableDataList.add(tempIndex, tmpHash01);
                                ClassifiedRender_Setting_Dialog.this.m_LineHeaderListViewFactory.BindDataToListView(ClassifiedRender_Setting_Dialog.this.m_MyTableDataList, new String[]{"D1", "D2", "D3"}, ClassifiedRender_Setting_Dialog.this.pCallback);
                            }
                        }
                    } else if (paramString.contains("输入参数") && (tmpObjs = (Object[]) pObject) != null && tmpObjs.length >= 1) {
                        String tempValue2 = tmpObjs[1].toString();
                        boolean tempBool = false;
                        Iterator<HashMap<String, Object>> tempIter = ClassifiedRender_Setting_Dialog.this.m_MyTableDataList.iterator();
                        while (true) {
                            if (tempIter.hasNext()) {
                                if (tempValue2.equals(tempIter.next().get("D2").toString())) {
                                    tempBool = true;
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                        if (tempBool) {
                            Common.ShowDialog("字段组合值[" + tempValue2 + "]已经存在.");
                            return;
                        }
                        ISymbol tmpSymbol = ISymbol.CreateSymbol(ClassifiedRender_Setting_Dialog.this.m_EditLayer.GetLayerType());
                        HashMap tmpHashMap = new HashMap();
                        tmpHashMap.put("D1", false);
                        tmpHashMap.put("D2", tempValue2);
                        tmpHashMap.put("D3", tmpSymbol.getSymbolFigure());
                        tmpHashMap.put("D4", tmpSymbol);
                        tmpHashMap.put("D5", Integer.valueOf(ClassifiedRender_Setting_Dialog.this.m_MyTableDataList.size()));
                        ClassifiedRender_Setting_Dialog.this.m_MyTableDataList.add(tmpHashMap);
                        ClassifiedRender_Setting_Dialog.this.m_LineHeaderListViewFactory.BindDataToListView(ClassifiedRender_Setting_Dialog.this.m_MyTableDataList, new String[]{"D1", "D2", "D3"}, ClassifiedRender_Setting_Dialog.this.pCallback);
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_MyTableDataList = new ArrayList();
        this.m_LineHeaderListViewFactory = null;
        this._LabelFieldSpinnerDialog = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.classifiedrender_setting_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("多值符号");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        ((Button) this._Dialog.findViewById(R.id.btn_DeleteItem)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_ReValues)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_AutoDefine)).setOnClickListener(new ViewClick());
    }

    public void SetLayerAndRender(FeatureLayer _Layer, GeoLayer _GeoLayer, IRender _Render) {
        this.m_EditLayer = _Layer;
        this.m_GeoLayer = _GeoLayer;
        if (_Render == null || _Render.getType() != EDisplayType.CLASSIFIED) {
            this.m_Render = new ClassifiedRender(this.m_GeoLayer);
            this.m_Render.LoadSymbol(this.m_EditLayer);
        } else {
            this.m_Render = (ClassifiedRender) _Render;
        }
        this.m_DefaultSymbol = this.m_Render.getDefaultSymbol();
        if (this.m_DefaultSymbol == null) {
            this.m_DefaultSymbol = PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemSymbol("", this.m_GeoLayer.getType());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        if (this.m_EditLayer != null && this.m_GeoLayer != null && this.m_Render != null) {
            String tempLabelNames = this.m_EditLayer.ConvertDataFieldToName(Common.CombineStrings(",", this.m_Render.getUniqueValueField()));
            this._LabelFieldSpinnerDialog = (InputSpinner) this._Dialog.findViewById(R.id.sp_labelGroup);
            this._LabelFieldSpinnerDialog.getEditTextView().setEnabled(false);
            this._LabelFieldSpinnerDialog.getEditTextView().setText(tempLabelNames);
            this._LabelFieldSpinnerDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Display.ClassifiedRender_Setting_Dialog.2
                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                public void OnClick(String paramString, Object pObject) {
                    String tempSelectFieldNames = ClassifiedRender_Setting_Dialog.this._LabelFieldSpinnerDialog.getEditTextView().getText().toString();
                    Fields_Select_Dialog tempDialog = new Fields_Select_Dialog();
                    tempDialog.SetLayer(ClassifiedRender_Setting_Dialog.this.m_EditLayer);
                    tempDialog.SetSelectFieldNames(tempSelectFieldNames);
                    tempDialog.SetCallback(ClassifiedRender_Setting_Dialog.this.pCallback);
                    tempDialog.ShowDialog();
                }
            });
            ImageSpinnerDialog typeSpinnerDialog = (ImageSpinnerDialog) this._Dialog.findViewById(R.id.sp_defaultSymbol);
            typeSpinnerDialog.SetIsShowText(false);
            typeSpinnerDialog.SetCallback(this.pCallback);
            if (this.m_DefaultSymbol != null) {
                List<SymbolObject> tempSymbolsList = new ArrayList<>();
                SymbolObject tmpSymbolObject = new SymbolObject();
                tmpSymbolObject.SymbolName = "";
                tmpSymbolObject.SymbolFigure = this.m_DefaultSymbol.getSymbolFigure();
                tempSymbolsList.add(tmpSymbolObject);
                typeSpinnerDialog.SetImageItemList(tempSymbolsList);
            }
            this.m_LineHeaderListViewFactory = new MyTableFactory();
            this.m_LineHeaderListViewFactory.SetHeaderListView(this._Dialog.findViewById(R.id.uniquesymbols_list), "自定义", "选择,名称,符号", "checkbox,text,image", new int[]{-15, -50, -35}, this.pCallback);
            List<ISymbol> tempSymbols = this.m_Render.getSymbolList();
            List<String> tempValuesList = this.m_Render.getUniqueValueList();
            if (tempSymbols != null && tempValuesList != null && tempSymbols.size() == tempValuesList.size()) {
                Iterator<String> tempIter2 = tempValuesList.iterator();
                int tempTid0 = 0;
                for (ISymbol tmpSymbol : tempSymbols) {
                    HashMap tmpHashMap = new HashMap();
                    tmpHashMap.put("D1", false);
                    tmpHashMap.put("D2", tempIter2.next());
                    tmpHashMap.put("D3", tmpSymbol.getSymbolFigure());
                    tmpHashMap.put("D4", tmpSymbol);
                    tmpHashMap.put("D5", Integer.valueOf(tempTid0));
                    this.m_MyTableDataList.add(tmpHashMap);
                    tempTid0++;
                }
                this.m_LineHeaderListViewFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2", "D3"}, this.pCallback);
            } else if (!tempLabelNames.equals("")) {
                DoCommand("重取值");
            }
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Display.ClassifiedRender_Setting_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                ClassifiedRender_Setting_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("重取值")) {
            this.m_MyTableDataList = new ArrayList();
            String tempLabelDataFields = this.m_EditLayer.ConvertToDataField(Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_labelGroup));
            if (!tempLabelDataFields.equals("")) {
                SQLiteReader localSQLiteDataReader = this.m_GeoLayer.getDataset().getDataSource().Query("Select (" + tempLabelDataFields.replaceAll(",", "||','||") + ") AS FValues From " + this.m_GeoLayer.getDataset().getDataTableName() + " Group By FValues");
                if (localSQLiteDataReader != null) {
                    List<ISymbol> tempSymbols = this.m_Render.getSymbolList();
                    int tempTid = 0;
                    int tmpCount = tempSymbols.size();
                    while (localSQLiteDataReader.Read()) {
                        HashMap tmpHashMap = new HashMap();
                        tmpHashMap.put("D1", false);
                        tmpHashMap.put("D2", localSQLiteDataReader.GetString("FValues"));
                        ISymbol tmpSymbol01 = null;
                        if (tempTid < tmpCount) {
                            tmpSymbol01 = tempSymbols.get(tempTid);
                        }
                        if (tmpSymbol01 == null || tmpSymbol01.getConfigParas().equals("")) {
                            tmpSymbol01 = ISymbol.GenerateRandomSymbol(this.m_DefaultSymbol);
                        }
                        if (tmpSymbol01 != null) {
                            tmpHashMap.put("D3", tmpSymbol01.getSymbolFigure());
                        } else {
                            tmpHashMap.put("D3", null);
                        }
                        tmpHashMap.put("D4", tmpSymbol01);
                        tmpHashMap.put("D5", Integer.valueOf(tempTid));
                        tempTid++;
                        this.m_MyTableDataList.add(tmpHashMap);
                    }
                    localSQLiteDataReader.Close();
                }
            } else {
                Common.ShowToast("请选择字段组合.");
            }
            this.m_LineHeaderListViewFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2", "D3"}, this.pCallback);
        } else if (command.equals("删除符号")) {
            List<Integer> tempList = new ArrayList<>();
            int tempTid2 = 0;
            for (HashMap<String, Object> tempHash : this.m_MyTableDataList) {
                if (Boolean.parseBoolean(tempHash.get("D1").toString())) {
                    tempList.add(Integer.valueOf(tempTid2));
                }
                tempTid2++;
            }
            if (tempList.size() > 0) {
                Collections.reverse(tempList);
                for (Integer num : tempList) {
                    this.m_MyTableDataList.remove(num.intValue());
                }
                int tempTid3 = 0;
                for (HashMap<String, Object> tempHash2 : this.m_MyTableDataList) {
                    tempHash2.put("D5", Integer.valueOf(tempTid3));
                    tempTid3++;
                }
                this.m_LineHeaderListViewFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2", "D3"}, this.pCallback);
                return;
            }
            Common.ShowToast("没有选择任何对象.");
        } else if (command.equals("自定义")) {
            Input_Dialog tempDialog = new Input_Dialog();
            tempDialog.setValues("自定义值", "值域:", "", "提示:多个字段值之间用逗号(,)隔开,如'1,浙江省,杭州市'");
            tempDialog.SetCallback(this.pCallback);
            tempDialog.ShowDialog();
        }
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                ClassifiedRender_Setting_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
