package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xzy.forestSystem.GuiZhou.DiaoCha.CommonSetting;
import com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiInfo_Dialog;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EFieldAutoValueType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.CoordList_Dialog;
import  com.xzy.forestSystem.gogisapi.Common.ECopyPasteType;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import com.xzy.forestSystem.gogisapi.MyControls.AutoInputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.DateSelect_Dailog;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.SpinnerDialog;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDictionaryManage;
import  com.xzy.forestSystem.gogisapi.Others.MediaManag_Dialog;
import com.stczh.gzforestSystem.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class FeatureAttribute_Dialog {
    private static boolean IsDialogShow = false;
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private boolean IsAllowEdit;
    private BaseDataObject _BaseObject;
    private DataSource _DataSource;
    private XDialogTemplate _Dialog;
    private int _EditMode;
    private List<FieldView> _FieldInnerFeauterViewList;
    private List<FieldView> _FieldNameViewList;
    private boolean _IsOpenCommonTools;
    private FeatureLayer _Layer;
    private Button _MediaBtn;
    private ICallback m_Callback;
    private AbstractGeometry m_Geometry;
    private Button m_MoreToolsBtn;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    @SuppressLint("WrongConstant")
    public FeatureAttribute_Dialog() {
        this._BaseObject = null;
        this._FieldInnerFeauterViewList = new ArrayList();
        this._FieldNameViewList = new ArrayList();
        this._Layer = null;
        this._DataSource = null;
        this._Dialog = null;
        this.m_Callback = null;
        this.m_Geometry = null;
        this._MediaBtn = null;
        this.m_MoreToolsBtn = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog.1
            @SuppressLint("WrongConstant")
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                String tmpStr;
                EditText tmpEditTxt;
                try {
                    if (paramString.equals("返回")) {
                        if (!(FeatureAttribute_Dialog.this.m_Callback == null || FeatureAttribute_Dialog.this._BaseObject == null)) {
                            FeatureAttribute_Dialog.this.m_Callback.OnClick("编辑属性窗体返回", Integer.valueOf(FeatureAttribute_Dialog.this._BaseObject.GetSYS_ID()));
                        }
                    } else if (paramString.equals("保存")) {
                        if (FeatureAttribute_Dialog.this._BaseObject != null) {
                            FeatureAttribute_Dialog.this._BaseObject.RefreshViewValueToData();
                            if (FeatureAttribute_Dialog.this._EditMode == 1) {
                                if (!(FeatureAttribute_Dialog.this.m_Callback == null || FeatureAttribute_Dialog.this._BaseObject == null)) {
                                    FeatureAttribute_Dialog.this.m_Callback.OnClick("编辑属性数据返回", FeatureAttribute_Dialog.this._BaseObject);
                                }
                                FeatureAttribute_Dialog.this._Dialog.dismiss();
                            } else if (FeatureAttribute_Dialog.this._EditMode == 2) {
                                FeatureAttribute_Dialog.this._BaseObject.Save();
                                if (!(FeatureAttribute_Dialog.this.m_Callback == null || FeatureAttribute_Dialog.this._BaseObject == null)) {
                                    FeatureAttribute_Dialog.this.m_Callback.OnClick("编辑属性数据返回", FeatureAttribute_Dialog.this._BaseObject);
                                }
                                FeatureAttribute_Dialog.this._Dialog.dismiss();
                            } else {
                                FeatureAttribute_Dialog.this._BaseObject.Save();
                                if (!(FeatureAttribute_Dialog.this.m_Callback == null || FeatureAttribute_Dialog.this._BaseObject == null)) {
                                    FeatureAttribute_Dialog.this.m_Callback.OnClick("编辑属性返回", Integer.valueOf(FeatureAttribute_Dialog.this._BaseObject.GetSYS_ID()));
                                }
                                GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByName(FeatureAttribute_Dialog.this._Layer.GetLayerID());
                                if (tmpGeoLayer != null) {
                                    List<String> tempList = new ArrayList<>();
                                    tempList.add(String.valueOf(FeatureAttribute_Dialog.this._BaseObject.GetSYS_ID()));
                                    tmpGeoLayer.getDataset().UpdateGeometrysSymbol(tempList);
                                }
                                if (FeatureAttribute_Dialog.this._Dialog.findViewById(R.id.btn_xiaobanYangDi).getVisibility() == 0) {
                                    Common.ShowToast("保存属性数据成功!");
                                } else {
                                    FeatureAttribute_Dialog.this._Dialog.dismiss();
                                }
                            }
                        }
                    } else if (paramString.equals("多媒体")) {
                        if (FeatureAttribute_Dialog.this._BaseObject != null) {
                            MediaManag_Dialog tempDialog = new MediaManag_Dialog(FeatureAttribute_Dialog.this._Dialog.getContext());
                            tempDialog.ReSetSize(1.0f, 0.96f);
                            String tmpFilePre = "";
                            if (FeatureAttribute_Dialog.this._Layer != null) {
                                tmpFilePre = String.valueOf(FeatureAttribute_Dialog.this._Layer.GetLayerName()) + "_";
                            }
                            tempDialog.setFileNamePreString(tmpFilePre);
                            tempDialog.SetBasePointObject(FeatureAttribute_Dialog.this._BaseObject);
                            tempDialog.SetCallback(FeatureAttribute_Dialog.this.pCallback);
                            tempDialog.show();
                        }
                    } else if (paramString.equals("多媒体返回")) {
                        FeatureAttribute_Dialog.this.UpdateDialogShowInfo();
                    } else if (paramString.equals("常用工具结果返回")) {
                        FeatureAttribute_Dialog.this._IsOpenCommonTools = false;
                        Object[] tmpObjs = (Object[]) pObject;
                        if (!(tmpObjs == null || tmpObjs.length <= 1 || (tmpStr = String.valueOf(tmpObjs[1])) == null || tmpStr.equals("") || (tmpEditTxt = FeatureAttribute_Dialog.this.findFocuseEditTxt()) == null)) {
                            if (tmpEditTxt.isEnabled()) {
                                String tmpStr2 = String.valueOf(tmpEditTxt.getText().toString()) + tmpStr;
                                tmpEditTxt.setText(tmpStr2);
                                tmpEditTxt.setSelection(tmpStr2.length());
                                return;
                            }
                            Common.ShowDialog("当前输入框不允许输入.");
                        }
                    } else if (paramString.equals("常用快捷工具返回")) {
                        FeatureAttribute_Dialog.this._IsOpenCommonTools = false;
                    } else if (paramString.equals("复制属性工具")) {
                        if (FeatureAttribute_Dialog.this._Layer == null || FeatureAttribute_Dialog.this.m_Geometry == null || FeatureAttribute_Dialog.this.m_Geometry.GetSYS_ID() == null) {
                            Common.ShowDialog("复制对象可能不存在或临时为空.");
                        } else {
                            PubVar.SystemCopyObject.CopyAttribute(FeatureAttribute_Dialog.this._Layer, FeatureAttribute_Dialog.this.m_Geometry.GetSYS_ID());
                        }
                    } else if (paramString.equals("粘贴属性工具")) {
                        if (PubVar.SystemCopyObject.getECopyPasteType() == ECopyPasteType.Attribute) {
                            Object tmpObj = PubVar.SystemCopyObject.getCopyObject();
                            if (tmpObj != null) {
                                Object[] tmpObjs2 = (Object[]) tmpObj;
                                if (tmpObjs2 != null && tmpObjs2.length > 3) {
                                    String tmpMsg = "";
                                    String tmpLayerName = String.valueOf(tmpObjs2[0]);
                                    final String tmpLayerDatasource = String.valueOf(tmpObjs2[1]);
                                    final String tmpLayerID = String.valueOf(tmpObjs2[2]);
                                    final String tmpGeoSysID = String.valueOf(tmpObjs2[3]);
                                    if (!tmpLayerDatasource.equals(FeatureAttribute_Dialog.this._Layer.GetDataSourceName()) || !tmpLayerID.equals(FeatureAttribute_Dialog.this._Layer.GetLayerID())) {
                                        tmpMsg = "复制的图层与当前图层不是同一个图层.\r\n复制的数据图层信息为:\r\n图层名称:【" + tmpLayerName + "】\r\n来源为:" + tmpLayerDatasource + "\r\n\r\n是否继续粘贴属性?";
                                    }
                                    if (!tmpMsg.equals("")) {
                                        Common.ShowYesNoDialog(FeatureAttribute_Dialog.this._Dialog.getContext(), tmpMsg, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog.1.1
                                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                            public void OnClick(String command, Object pObject2) {
                                                if (command.equals("YES")) {
                                                    DataSet pDateset = PubVar.m_Workspace.GetDatasetByName(tmpLayerDatasource, tmpLayerID);
                                                    if (pDateset != null) {
                                                        List<HashMap<String, Object>> tmpFieldValues = pDateset.getGeometryFieldValues(tmpGeoSysID, (List<String>) null);
                                                        if (tmpFieldValues == null || tmpFieldValues.size() <= 0) {
                                                            Common.ShowDialog("获取复制对象的属性数据失败.");
                                                        } else if (FeatureAttribute_Dialog.this._BaseObject != null) {
                                                            int tmpTid = 0;
                                                            for (HashMap<String, Object> tmpHash : tmpFieldValues) {
                                                                if (FeatureAttribute_Dialog.this._BaseObject.SetDataBindItemValueByName(String.valueOf(tmpHash.get("name")), String.valueOf(tmpHash.get("value")))) {
                                                                    tmpTid++;
                                                                }
                                                            }
                                                            FeatureAttribute_Dialog.this._BaseObject.RefreshDataToView();
                                                            if (tmpTid > 0) {
                                                                Common.ShowToast("粘贴属性成功!\r\n共粘贴对应属性值 " + String.valueOf(tmpTid) + " 个.");
                                                            }
                                                        }
                                                    } else {
                                                        Common.ShowDialog("数据集不存在.");
                                                    }
                                                }
                                            }
                                        });
                                        return;
                                    }
                                    DataSet pDateset = FeatureAttribute_Dialog.this._DataSource.GetDatasetByName(FeatureAttribute_Dialog.this._Layer.GetLayerID());
                                    if (pDateset != null) {
                                        List<HashMap<String, Object>> tmpFieldValues = pDateset.getGeometryFieldValues(tmpGeoSysID, (List<String>) null);
                                        if (tmpFieldValues == null || tmpFieldValues.size() <= 0) {
                                            Common.ShowDialog("获取复制对象的属性数据失败.");
                                        } else if (FeatureAttribute_Dialog.this._BaseObject != null) {
                                            for (HashMap<String, Object> tmpHash : tmpFieldValues) {
                                                FeatureAttribute_Dialog.this._BaseObject.SetDataBindItemValue(String.valueOf(tmpHash.get("fieldID")), String.valueOf(tmpHash.get("value")));
                                            }
                                            FeatureAttribute_Dialog.this._BaseObject.RefreshDataToView();
                                            Common.ShowToast("粘贴属性成功!");
                                        }
                                    } else {
                                        Common.ShowDialog("数据集不存在.");
                                    }
                                }
                            } else {
                                Common.ShowDialog("复制的属性数据为空数据.");
                            }
                        } else {
                            Common.ShowDialog("复制的数据不是属性数据.");
                        }
                    } else if (paramString.equals("对话框关闭事件")) {
                        FeatureAttribute_Dialog.IsDialogShow = false;
                    }
                } catch (Exception e) {
                }
            }
        };
        this._IsOpenCommonTools = false;
        this.IsAllowEdit = false;
        this._EditMode = 0;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.featureattribute_dialog);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("属性");
        this._Dialog.SetCallback(this.pCallback);
        this._MediaBtn = (Button) this._Dialog.findViewById(R.id.btn_mediaMang);
        this._MediaBtn.setVisibility(8);
        this._MediaBtn.setOnClickListener(new ViewClick());
        this.m_MoreToolsBtn = (Button) this._Dialog.findViewById(R.id.btn_MoreTools);
        this._Dialog.findViewById(R.id.btn_coordinateList).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (FeatureAttribute_Dialog.this.m_Geometry != null) {
                    CoordList_Dialog tempDialog = new CoordList_Dialog();
                    tempDialog.SetCoordinates(FeatureAttribute_Dialog.this.m_Geometry.GetAllCoordinateList());
                    tempDialog.ShowDialog();
                }
            }
        });
        this.m_MoreToolsBtn.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (FeatureAttribute_Dialog.this.IsAllowEdit) {
                    if (!FeatureAttribute_Dialog.this._IsOpenCommonTools) {
                        PubVar.m_Callback.OnClick("常用快捷工具", FeatureAttribute_Dialog.this.pCallback);
                    }
                    FeatureAttribute_Dialog.this._IsOpenCommonTools = true;
                }
            }
        });
        this._Dialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog.4
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (FeatureAttribute_Dialog.this.IsAllowEdit && (keyCode == 82 || keyCode == 3 || keyCode == 24 || keyCode == 25)) {
                    if (!FeatureAttribute_Dialog.this._IsOpenCommonTools) {
                        PubVar.m_Callback.OnClick("常用快捷工具", FeatureAttribute_Dialog.this.pCallback);
                    }
                    FeatureAttribute_Dialog.this._IsOpenCommonTools = true;
                }
                return FeatureAttribute_Dialog.this._Dialog.onKeyDown(keyCode, event);
            }
        });
    }

    private void CreateForm(FeatureLayer layer) {
        if (layer != null) {
            try {
                String tmpTitle = "【" + layer.GetLayerName() + "】";
                if (this.m_Geometry != null && !tmpTitle.contains(" ")) {
                    tmpTitle = String.valueOf(tmpTitle) + " " + this.m_Geometry.GetSYS_ID();
                }
                this._Dialog.SetCaption(tmpTitle);
                boolean tmpIsEdit = true;
                if (layer.GetEditable()) {
                    tmpIsEdit = true;
                }
                int maxFieldNameLen = 0;
                for (LayerField tempField : layer.GetFieldList()) {
                    int tempStrLen = Common.CalStrLength(tempField.GetFieldName());
                    if (tempStrLen > maxFieldNameLen) {
                        maxFieldNameLen = tempStrLen;
                    }
                }
                LinearLayout tempGeoPropLayout = (LinearLayout) this._Dialog.findViewById(R.id.otherlist);
                ArrayList tempArray01 = new ArrayList();
                if (layer.GetLayerType() == EGeoLayerType.POINT) {
                    tempArray01.add("坐标");
                } else if (layer.GetLayerType() == EGeoLayerType.POLYLINE) {
                    tempArray01.add("长度");
                } else if (layer.GetLayerType() == EGeoLayerType.POLYGON) {
                    tempArray01.add("面积");
                }
                Iterator<String> tempIter02 = tempArray01.iterator();
                while (tempIter02.hasNext()) {
                    String str = tempIter02.next();
                    LinearLayout tempLineLayout = CreateFormRowHeader(tempGeoPropLayout, str, maxFieldNameLen, ViewCompat.MEASURED_STATE_MASK);
                    EditText localEditText1 = new EditText(tempGeoPropLayout.getContext());
                    localEditText1.setFocusable(false);
                    localEditText1.setFocusableInTouchMode(false);
                    localEditText1.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog.5
                        @Override // android.view.View.OnClickListener
                        public void onClick(View arg0) {
                            String tmpString = ((EditText) arg0).getText().toString();
                            int tmpI = tmpString.indexOf("(");
                            if (tmpI > 0) {
                                tmpString = tmpString.substring(0, tmpI);
                            }
                            Common.CopyText(tmpString);
                            Common.ShowToast("复制数据成功.");
                        }
                    });
                    ViewGroup.LayoutParams tempLayputParas = new ViewGroup.LayoutParams(localEditText1.getWidth(), localEditText1.getHeight());
                    tempLayputParas.width = -1;
                    tempLayputParas.height = -2;
                    localEditText1.setLayoutParams(tempLayputParas);
                    tempLineLayout.addView(localEditText1);
                    tempGeoPropLayout.addView(tempLineLayout);
                    FieldView tempFieldView = new FieldView(str, "", null, localEditText1);
                    tempFieldView.FieldView = localEditText1;
                    this._FieldInnerFeauterViewList.add(tempFieldView);
                }
                LinearLayout tempBasePropLayout = (LinearLayout) this._Dialog.findViewById(R.id.baselist);
                List<LayerField> tmpFields = layer.GetFieldList();
                boolean tmpIsYangdiDiaoChaLyr = false;
                if (PubVar.Module_YangdiDiaoCha && this._Layer != null && this._Layer.GetLayerType() == EGeoLayerType.POLYGON && CommonSetting.getXiaoBanLayersList().contains(this._Layer.GetLayerID())) {
                    tmpIsYangdiDiaoChaLyr = true;
                    List<LayerField> tmpFields2 = layer.GetFieldList();
                    tmpFields = new ArrayList<>();
                    List<Integer> tmpIntList = new ArrayList<>();
                    HashMap<String, String> tmphashHashMap = CommonSetting.GetXiaoBanLayerMustFieldsName();
                    List<String> tmpMustList = CommonSetting.GetXiaoBanLayerMustFieldsNameList();
                    if (tmphashHashMap != null && tmphashHashMap.size() > 0 && tmpMustList != null && tmpMustList.size() > 0) {
                        for (String tmpFIDName : tmpMustList) {
                            int tmpFIDIndex = this._Layer.getDataFieldIndexByFieldName(tmphashHashMap.get(tmpFIDName));
                            if (tmpFIDIndex > -1) {
                                tmpFields.add(tmpFields2.get(tmpFIDIndex));
                                tmpIntList.add(Integer.valueOf(tmpFIDIndex));
                            }
                        }
                    }
                    int tmpTid01 = -1;
                    for (LayerField tmpField : tmpFields2) {
                        tmpTid01++;
                        if (!tmpIntList.contains(Integer.valueOf(tmpTid01))) {
                            tmpFields.add(tmpField);
                        }
                    }
                }
                for (final LayerField tempField2 : tmpFields) {
                    if (tempField2.GetFieldVisible()) {
                        boolean tempBoolTag01 = false;
                        int tempInputType = -1;
                        int tmpTxtColor = ViewCompat.MEASURED_STATE_MASK;
                        if (tmpIsYangdiDiaoChaLyr && CommonSetting.GetCalFieldsNameList().contains(tempField2.GetFieldName())) {
                            tmpTxtColor = -16776961;
                        }
                        LinearLayout tempLineLayout2 = CreateFormRowHeader(tempBasePropLayout, tempField2.GetFieldName(), maxFieldNameLen, tmpTxtColor);
                        if (tempField2.GetFieldType() == EFieldType.STRING) {
                            if (tempField2.GetFieldEnumCode().equals("")) {
                                EditText tempEditTxt = new EditText(tempBasePropLayout.getContext());
                                ViewGroup.LayoutParams localLayoutParams8 = new ViewGroup.LayoutParams(tempEditTxt.getWidth(), tempEditTxt.getHeight());
                                localLayoutParams8.width = -1;
                                localLayoutParams8.height = -2;
                                tempEditTxt.setLayoutParams(localLayoutParams8);
                                tempEditTxt.setEnabled(tmpIsEdit);
                                tempLineLayout2.addView(tempEditTxt);
                                FieldView tempFieldView2 = new FieldView(tempField2.GetFieldName(), tempField2.GetDataFieldName(), tempField2, tempEditTxt);
                                tempFieldView2.FieldView = tempEditTxt;
                                this._FieldNameViewList.add(tempFieldView2);
                            } else {
                                tempBoolTag01 = true;
                            }
                        } else if (tempField2.GetFieldType() == EFieldType.DECIMAL) {
                            if (tempField2.GetFieldEnumCode().equals("")) {
                                EditText tempEditTxt2 = new EditText(tempBasePropLayout.getContext());
                                tempEditTxt2.setInputType(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                                ViewGroup.LayoutParams localLayoutParams6 = new ViewGroup.LayoutParams(tempEditTxt2.getWidth(), tempEditTxt2.getHeight());
                                localLayoutParams6.width = -1;
                                localLayoutParams6.height = -2;
                                tempEditTxt2.setLayoutParams(localLayoutParams6);
                                tempEditTxt2.setEnabled(tmpIsEdit);
                                tempLineLayout2.addView(tempEditTxt2);
                                FieldView tempFieldView3 = new FieldView(tempField2.GetFieldName(), tempField2.GetDataFieldName(), tempField2, tempEditTxt2);
                                tempFieldView3.FieldView = tempEditTxt2;
                                this._FieldNameViewList.add(tempFieldView3);
                            } else {
                                tempBoolTag01 = true;
                                tempInputType = FragmentTransaction.TRANSIT_FRAGMENT_CLOSE;
                            }
                        } else if (tempField2.GetFieldType() == EFieldType.INTEGER) {
                            if (tempField2.GetFieldEnumCode().equals("")) {
                                EditText tempEditTxt3 = new EditText(tempBasePropLayout.getContext());
                                tempEditTxt3.setInputType(2);
                                ViewGroup.LayoutParams localLayoutParams4 = new ViewGroup.LayoutParams(tempEditTxt3.getWidth(), tempEditTxt3.getHeight());
                                localLayoutParams4.width = -1;
                                localLayoutParams4.height = -2;
                                tempEditTxt3.setLayoutParams(localLayoutParams4);
                                tempEditTxt3.setEnabled(tmpIsEdit);
                                tempLineLayout2.addView(tempEditTxt3);
                                FieldView tempFieldView4 = new FieldView(tempField2.GetFieldName(), tempField2.GetDataFieldName(), tempField2, tempEditTxt3);
                                tempFieldView4.FieldView = tempEditTxt3;
                                this._FieldNameViewList.add(tempFieldView4);
                            } else {
                                tempBoolTag01 = true;
                                tempInputType = 2;
                            }
                        } else if (tempField2.GetFieldType() == EFieldType.BOOLEAN) {
                            SpinnerList localSpinner = new SpinnerList(tempBasePropLayout.getContext());
                            Common.SetSpinnerListData(this._Dialog.getContext(), "是否", Common.StrArrayToList(new String[]{"是", "否"}), localSpinner);
                            ViewGroup.LayoutParams localLayoutParams2 = new ViewGroup.LayoutParams(localSpinner.getWidth(), localSpinner.getHeight());
                            localLayoutParams2.width = -1;
                            localLayoutParams2.height = -2;
                            localSpinner.setLayoutParams(localLayoutParams2);
                            localSpinner.setEnabled(tmpIsEdit);
                            tempLineLayout2.addView(localSpinner);
                            FieldView tempFieldView5 = new FieldView(tempField2.GetFieldName(), tempField2.GetDataFieldName(), tempField2, localSpinner);
                            tempFieldView5.FieldView = localSpinner;
                            this._FieldNameViewList.add(tempFieldView5);
                        } else if (tempField2.GetFieldType() == EFieldType.DATETIME) {
                            final SpinnerDialog tempSpinner = new SpinnerDialog(tempBasePropLayout.getContext());
                            tempSpinner.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog.6
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String paramString, Object pObject) {
                                    Date tempDate = new Date();
                                    try {
                                        String tempStr = Common.GetViewValue(tempSpinner);
                                        if (tempStr != null && !tempStr.equals("")) {
                                            tempDate = FeatureAttribute_Dialog.dateFormat.parse(tempStr);
                                        }
                                    } catch (Exception e) {
                                        tempDate = new Date();
                                    }
                                    DateSelect_Dailog tempDialog = new DateSelect_Dailog();
                                    tempDialog.SetSelectType(1);
                                    tempDialog.SetDate(tempDate);
                                    final SpinnerDialog spinnerDialog = tempSpinner;
                                    tempDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog.6.1
                                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                        public void OnClick(String paramString2, Object pObject2) {
                                            Object[] tempObjs;
                                            if (paramString2.equals("时间选择返回") && (tempObjs = (Object[]) pObject2) != null && tempObjs.length > 1) {
                                                Common.SetSpinnerListData(FeatureAttribute_Dialog.this._Dialog.getContext(), "选择日期", Common.StrArrayToList(new String[]{FeatureAttribute_Dialog.dateFormat.format((Date) tempObjs[1])}), spinnerDialog);
                                            }
                                        }
                                    });
                                    tempDialog.ShowDialog();
                                }
                            });
                            Common.SetSpinnerListData(this._Dialog.getContext(), "选择日期", Common.StrArrayToList(new String[]{""}), tempSpinner);
                            ViewGroup.LayoutParams localLayoutParams3 = new ViewGroup.LayoutParams(tempSpinner.getWidth(), tempSpinner.getHeight());
                            localLayoutParams3.width = -1;
                            localLayoutParams3.height = -2;
                            tempSpinner.setLayoutParams(localLayoutParams3);
                            tempSpinner.setEnabled(tmpIsEdit);
                            tempLineLayout2.addView(tempSpinner);
                            FieldView tempFieldView6 = new FieldView(tempField2.GetFieldName(), tempField2.GetDataFieldName(), tempField2, tempSpinner);
                            tempFieldView6.FieldView = tempSpinner;
                            this._FieldNameViewList.add(tempFieldView6);
                        }
                        if (tempBoolTag01) {
                            if (tempField2.GetFieldEnumEdit()) {
                                final AutoInputSpinner tempSpinner2 = new AutoInputSpinner(tempBasePropLayout.getContext());
                                ViewGroup.LayoutParams tempLayoutParams = new ViewGroup.LayoutParams(tempSpinner2.getWidth(), tempSpinner2.getHeight());
                                tempLayoutParams.width = -1;
                                tempLayoutParams.height = -2;
                                tempSpinner2.setLayoutParams(tempLayoutParams);
                                tempSpinner2.SetSelectItemList(tempField2.GetFieldEnumList(), "请选择字段【" + tempField2.GetFieldName() + "】的值");
                                tempSpinner2.setTipList(tempField2.GetFieldEnumNamesList());
                                if (tempInputType > 0) {
                                    tempSpinner2.getEditTextView().setInputType(tempInputType);
                                }
                                tempSpinner2.getEditTextView().setEnabled(tempField2.GetFieldEnumEdit());
                                tempSpinner2.setEnabled(tmpIsEdit);
                                if (tmpIsEdit) {
                                    tempSpinner2.SetSelectItemFromTag(true);
                                    tempSpinner2.setTag("");
                                }
                                tempSpinner2.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog.7
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String paramString, Object pObject) {
                                        if (paramString.equals("SpinnerSelectCallback")) {
                                            String tmpResult = String.valueOf(pObject);
                                            if (!tmpResult.equals("")) {
                                                if (tempField2.GetIsEnumOfCode()) {
                                                    DataDictionaryManage tmpManage = PubVar._PubCommand.m_ConfigDB.GetDataDictionaryManage();
                                                    if (tmpManage != null) {
                                                        tmpResult = tmpManage.GetZDCode2(tmpResult);
                                                    }
                                                } else {
                                                    DataDictionaryManage tmpManage2 = PubVar._PubCommand.m_ConfigDB.GetDataDictionaryManage();
                                                    if (tmpManage2 != null) {
                                                        tmpResult = tmpManage2.GetZDValue2(tmpResult);
                                                    }
                                                }
                                            }
                                            tempSpinner2.getEditTextView().setText(tmpResult);
                                            tempSpinner2.setTag(String.valueOf(pObject));
                                        }
                                    }
                                });
                                tempLineLayout2.addView(tempSpinner2);
                                FieldView tempFieldView7 = new FieldView(tempField2.GetFieldName(), tempField2.GetDataFieldName(), tempField2, tempSpinner2);
                                tempFieldView7.FieldView = tempSpinner2;
                                this._FieldNameViewList.add(tempFieldView7);
                            } else {
                                final InputSpinner tempSpinner3 = new InputSpinner(tempBasePropLayout.getContext());
                                ViewGroup.LayoutParams tempLayoutParams2 = new ViewGroup.LayoutParams(tempSpinner3.getWidth(), tempSpinner3.getHeight());
                                tempLayoutParams2.width = -1;
                                tempLayoutParams2.height = -2;
                                tempSpinner3.setLayoutParams(tempLayoutParams2);
                                tempSpinner3.SetSelectItemList(tempField2.GetFieldEnumList(), "请选择字段【" + tempField2.GetFieldName() + "】的值");
                                if (tempInputType > 0) {
                                    tempSpinner3.getEditTextView().setInputType(tempInputType);
                                }
                                tempSpinner3.getEditTextView().setEnabled(tempField2.GetFieldEnumEdit());
                                tempSpinner3.setEnabled(tmpIsEdit);
                                if (tmpIsEdit) {
                                    tempSpinner3.SetSelectItemFromTag(true);
                                    tempSpinner3.setTag("");
                                }
                                tempSpinner3.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog.8
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String paramString, Object pObject) {
                                        if (paramString.equals("SpinnerSelectCallback")) {
                                            String tmpResult = String.valueOf(pObject);
                                            if (!tmpResult.equals("")) {
                                                if (tempField2.GetIsEnumOfCode()) {
                                                    DataDictionaryManage tmpManage = PubVar._PubCommand.m_ConfigDB.GetDataDictionaryManage();
                                                    if (tmpManage != null) {
                                                        tmpResult = tmpManage.GetZDCode2(tmpResult);
                                                    }
                                                } else {
                                                    DataDictionaryManage tmpManage2 = PubVar._PubCommand.m_ConfigDB.GetDataDictionaryManage();
                                                    if (tmpManage2 != null) {
                                                        tmpResult = tmpManage2.GetZDValue2(tmpResult);
                                                    }
                                                }
                                            }
                                            tempSpinner3.getEditTextView().setText(tmpResult);
                                            tempSpinner3.setTag(String.valueOf(pObject));
                                        }
                                    }
                                });
                                tempLineLayout2.addView(tempSpinner3);
                                FieldView tempFieldView8 = new FieldView(tempField2.GetFieldName(), tempField2.GetDataFieldName(), tempField2, tempSpinner3);
                                tempFieldView8.FieldView = tempSpinner3;
                                this._FieldNameViewList.add(tempFieldView8);
                            }
                        }
                        tempBasePropLayout.addView(tempLineLayout2);
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    @SuppressLint("ResourceType")
    private LinearLayout CreateFormRowHeader(LinearLayout paramLinearLayout, String fieldName, int maxFieldLen, int textColor) {
        LinearLayout localLinearLayout = new LinearLayout(paramLinearLayout.getContext());
        try {
            ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(localLinearLayout.getWidth(), localLinearLayout.getHeight());
            localLayoutParams.width = -1;
            localLayoutParams.height = -2;
            localLinearLayout.setGravity(17);
            localLinearLayout.setLayoutParams(localLayoutParams);
            TextView localTextView = new TextView(paramLinearLayout.getContext());
            localTextView.setText(String.valueOf(fieldName) + "：");
            localTextView.setTag(fieldName);
            localTextView.setTextColor(textColor);
            localTextView.setWidth(320);
            localTextView.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
            localTextView.setSingleLine(true);
            localTextView.setTextSize(2, 16.0f);
            localTextView.setGravity(21);
            localTextView.setTextAppearance(paramLinearLayout.getContext(), 16842816);
            localLinearLayout.addView(localTextView);
            localTextView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog.9
                @Override // android.view.View.OnClickListener
                public void onClick(View arg0) {
                    if (arg0.getTag() != null) {
                        Common.ShowToast(String.valueOf(arg0.getTag()));
                    }
                }
            });
        } catch (Exception e) {
        }
        return localLinearLayout;
    }

    public void SetLayerID(String layerID) {
        this._Layer = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(layerID);
        CreateForm(this._Layer);
    }

    public void SetGeometryIndex(String layerID, int geoIndex, String datasourceName) {
        SetDataSourceName(datasourceName);
        this._Layer = PubVar._PubCommand.m_ProjectDB.GetLayerInDataSource(datasourceName, layerID);
        CreateForm(this._Layer);
        if (this._DataSource != null) {
            this.m_Geometry = this._DataSource.GetDatasetByName(layerID).GetGeometry(geoIndex);
            if (this.m_Geometry != null) {
                SetGeometry(this.m_Geometry);
            }
        }
    }

    public void SetGeometryObject(String layerID, String datasourceName, AbstractGeometry geometry) {
        SetDataSourceName(datasourceName);
        this._Layer = PubVar._PubCommand.m_ProjectDB.GetLayerInDataSource(datasourceName, layerID);
        CreateForm(this._Layer);
        if (this._DataSource != null) {
            this.m_Geometry = geometry;
            if (this.m_Geometry != null) {
                SetGeometry(this.m_Geometry);
            }
        }
    }

    @SuppressLint("WrongConstant")
    public void SetDataSourceName(String datasourceName) {
        this._DataSource = PubVar.m_Workspace.GetDataSourceByName(datasourceName);
        if (this._DataSource != null && this._DataSource.getEditing()) {
            this._Dialog.SetHeadButtons("1,2130837920,保存 ,保存", this.pCallback);
            Common.SetValueToView("[0]多媒体", this._MediaBtn);
            this._MediaBtn.setVisibility(0);
            this.m_MoreToolsBtn.setVisibility(0);
        }
    }

    @SuppressLint("WrongConstant")
    public void SetSYS_ID(int SYS_ID) {
        this._DataSource = PubVar.m_Workspace.GetDataSourceByEditing();
        if (this._DataSource.getEditing()) {
            this._Dialog.SetHeadButtons("1,2130837920,保存 ,保存", this.pCallback);
            Common.SetValueToView("[0]多媒体", this._MediaBtn);
            this._MediaBtn.setVisibility(0);
            this.m_MoreToolsBtn.setVisibility(0);
        }
        String tmpTitle = this._Dialog.getCaption();
        if (!tmpTitle.contains(" ")) {
            this._Dialog.SetCaption(String.valueOf(tmpTitle) + " " + SYS_ID);
        }
        SetGeometry(this._DataSource.GetDatasetByName(this._Layer.GetLayerID()).GetGeometryBySYSIDMust(String.valueOf(SYS_ID)));
    }

    @SuppressLint("WrongConstant")
    public void SetGeometry(AbstractGeometry geometry) {
        this.m_Geometry = geometry;
        String tmpTitle = this._Dialog.getCaption();
        if (!tmpTitle.contains(" ")) {
            this._Dialog.SetCaption(String.valueOf(tmpTitle) + " " + this.m_Geometry.GetSYS_ID());
        }
        if (geometry != null) {
            try {
                TextView tmpTextView = (TextView) this._FieldInnerFeauterViewList.get(0).FieldView;
                if (this._Layer.GetLayerType() == EGeoLayerType.POINT) {
                    Coordinate tempCoord = geometry.GetAllCoordinateList().get(0);
                    if (PubVar._PubCommand.m_ProjectDB.GetProjectManage().GetCoorSystem().GetName().equals("WGS-84坐标")) {
                        tmpTextView.setText(String.valueOf(Common.ConvertToDigital(String.valueOf(tempCoord.getX()), 6)) + "," + Common.ConvertToDigital(String.valueOf(tempCoord.getY()), 6) + "," + Common.ConvertToDigital(String.valueOf(tempCoord.getZ()), 6));
                    } else {
                        tmpTextView.setText(tempCoord.ToString());
                    }
                    this._Dialog.findViewById(R.id.btn_coordinateList).setVisibility(8);
                } else if (this._Layer.GetLayerType() == EGeoLayerType.POLYLINE) {
                    tmpTextView.setText(Common.SimplifyLength(((Polyline) geometry).getLength(true), true));
                    this._Dialog.findViewById(R.id.ll_dataTemp_Bottom).setVisibility(0);
                    ((Button) this._Dialog.findViewById(R.id.btn_coordinateList)).setText("坐标点[" + geometry.getCoordsCount() + "]");
                } else if (this._Layer.GetLayerType() == EGeoLayerType.POLYGON) {
                    tmpTextView.setText(Common.SimplifyArea(((Polygon) geometry).getArea(true), true));
                    this._Dialog.findViewById(R.id.ll_dataTemp_Bottom).setVisibility(0);
                    ((Button) this._Dialog.findViewById(R.id.btn_coordinateList)).setText("坐标点[" + geometry.getCoordsCount() + "]");
                }
                this._BaseObject = new BaseDataObject();
                if (this._DataSource != null) {
                    this._BaseObject.SetDataSourceName(this._DataSource.getName());
                }
                this._BaseObject.SetBaseObjectRelateLayerID(this._Layer.GetLayerID());
                if (this._Layer.GetIfLabel()) {
                    this._BaseObject.SetLabelFieldName(this._Layer.GetLabelField());
                }
                this._BaseObject.SetSYS_ID(Integer.parseInt(geometry.GetSYS_ID()));
                for (FieldView localFieldView : this._FieldNameViewList) {
                    this._BaseObject.AddItem(new KeyValueViewDict(localFieldView.DataFieldName, localFieldView.FieldName, "", localFieldView.FieldView));
                }
                this._BaseObject.ReadDataAndBindToView("SYS_ID=" + geometry.GetSYS_ID());
            } catch (Exception e) {
                UpdateDialogShowInfo();
                return;
            } catch (Throwable th) {
                UpdateDialogShowInfo();
                throw th;
            }
        }
        UpdateDialogShowInfo();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    @SuppressLint("WrongConstant")
    private void UpdateDialogShowInfo() {
        if ((this._DataSource == null || this._DataSource.getEditing()) && this._BaseObject != null) {
            Common.SetValueToView("[" + this._BaseObject.GetPhotoCount() + "]多媒体", this._MediaBtn);
            this._MediaBtn.setVisibility(0);
            this.m_MoreToolsBtn.setVisibility(0);
        }
    }

    public void SetEditMode(int mode) {
        this._EditMode = mode;
    }

    @SuppressLint("WrongConstant")
    public void SetBaseDataObject(BaseDataObject object, String layerID) {
        try {
            this._BaseObject = object;
            SetLayerID(layerID);
            this._BaseObject.ClearItems();
            for (FieldView localFieldView : this._FieldNameViewList) {
                Object tempValue = this._BaseObject.getFieldValue(localFieldView.DataFieldName);
                if (tempValue == null) {
                    tempValue = "";
                }
                this._BaseObject.AddItem(new KeyValueViewDict(localFieldView.DataFieldName, localFieldView.FieldName, String.valueOf(tempValue), localFieldView.FieldView));
            }
            if (this._BaseObject != null) {
                int tempInt = this._BaseObject.GetPhotoCount();
                this._Dialog.SetHeadButtons("1,2130837920,保存 ,保存", this.pCallback);
                Common.SetValueToView("[" + tempInt + "]多媒体", this._MediaBtn);
                this._MediaBtn.setVisibility(0);
            }
        } catch (Exception e) {
        } finally {
            this._BaseObject.RefreshDataToView();
        }
    }

    public void ShowDialog() {
        if (IsDialogShow) {
            this._Dialog.dismiss();
            return;
        }
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog.10
            @SuppressLint("WrongConstant")
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                FeatureAttribute_Dialog.this.updateAutoValue();
                if (!PubVar.Module_YangdiDiaoCha) {
                    return;
                }
                if (FeatureAttribute_Dialog.this._Layer == null || FeatureAttribute_Dialog.this._Layer.GetLayerType() != EGeoLayerType.POLYGON || !CommonSetting.getXiaoBanLayersList().contains(FeatureAttribute_Dialog.this._Layer.GetLayerID())) {
                    FeatureAttribute_Dialog.this._Dialog.findViewById(R.id.btn_xiaobanYangDi).setVisibility(8);
                    return;
                }
                FeatureAttribute_Dialog.this._Dialog.findViewById(R.id.btn_xiaobanYangDi).setVisibility(0);
                FeatureAttribute_Dialog.this._Dialog.findViewById(R.id.btn_xiaobanYangDi).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.FeatureAttribute_Dialog.10.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View v) {
                        XiaoBanYangDiInfo_Dialog tmpDialog = new XiaoBanYangDiInfo_Dialog();
                        tmpDialog.setXiaoBan(PubVar._Map.GetGeoLayerByID(FeatureAttribute_Dialog.this._Layer.GetLayerID()), FeatureAttribute_Dialog.this.m_Geometry.GetSYS_ID());
                        tmpDialog.ShowDialog();
                    }
                });
            }
        });
        this._Dialog.show();
        IsDialogShow = true;
        if ((this._DataSource == null || this._DataSource.getEditing()) && this._BaseObject != null) {
            this.IsAllowEdit = true;
        }
        this._Dialog.SetDismissCallback(this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private EditText findFocuseEditTxt() {
        if (this._FieldNameViewList != null && this._FieldNameViewList.size() > 0) {
            for (FieldView tmpFieldView : this._FieldNameViewList) {
                if (tmpFieldView != null && (tmpFieldView.FieldView instanceof EditText)) {
                    EditText tmpEditText = (EditText) tmpFieldView.FieldView;
                    if (tmpEditText.hasFocus()) {
                        return tmpEditText;
                    }
                }
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateAutoValue() {
        try {
            if (!(this._DataSource == null || this._Layer == null || !this._Layer.GetEditable() || this.m_Geometry == null)) {
                DataSet pDataset = this._DataSource.GetDatasetByName(this._Layer.GetLayerID());
                for (FieldView localFieldView : this._FieldNameViewList) {
                    if (localFieldView.LayerField != null) {
                        LayerField tempField = localFieldView.LayerField;
                        if (tempField.AutoValueType != EFieldAutoValueType.NONE) {
                            String tmpValueString = pDataset.AutoGenerateFieldValue(this.m_Geometry, tempField);
                            View tmpView = localFieldView.FieldView;
                            if (Common.GetViewValue(tmpView).length() == 0) {
                                Common.SetValueToView(tmpValueString, tmpView);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                view.getTag().toString();
                FeatureAttribute_Dialog.this.pCallback.OnClick("多媒体", null);
            }
        }
    }

    /* access modifiers changed from: private */
    public class FieldView {
        public String DataFieldName = "";
        public String FieldName = "";
        public View FieldView = null;
        public LayerField LayerField = null;

        public FieldView(String fieldName, String dateFieldName, LayerField layerField, View arg4) {
            this.FieldName = fieldName;
            this.DataFieldName = dateFieldName;
            this.LayerField = layerField;
            this.FieldView = null;
        }
    }
}
