package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EFieldAutoValueType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import  com.xzy.forestSystem.gogisapi.Geodatabase.EFieldType;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDictionary_Select_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Layer_Field_Dialog {
    private XDialogTemplate _Dialog;
    private boolean m_AllowEditFieldType;
    private ICallback m_Callback;
    private SpinnerList m_DataSpinnerDialog;
    private LayerField m_EditField;
    private FeatureLayer m_EditLayer;
    private EEditMode m_EditMode;
    private ICallback pCallback;

    public Layer_Field_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_EditField = null;
        this.m_EditLayer = null;
        this.m_EditMode = EEditMode.NEW;
        this.m_AllowEditFieldType = true;
        this.m_DataSpinnerDialog = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Field_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                try {
                    if (paramString.equals("确定") && Layer_Field_Dialog.this.SaveFieldDetailInfo()) {
                        if (Layer_Field_Dialog.this.m_EditMode == EEditMode.NEW) {
                            if (Layer_Field_Dialog.this.m_Callback != null) {
                                Layer_Field_Dialog.this.m_Callback.OnClick("字段新建", Layer_Field_Dialog.this.m_EditField);
                            }
                        } else if (Layer_Field_Dialog.this.m_EditMode == EEditMode.EDIT && Layer_Field_Dialog.this.m_Callback != null) {
                            Layer_Field_Dialog.this.m_Callback.OnClick("字段编辑", Layer_Field_Dialog.this.m_EditField);
                        }
                        Layer_Field_Dialog.this._Dialog.dismiss();
                    } else if (paramString.equals("关联值域点击")) {
                        DataDictionary_Select_Dialog tempDialog = new DataDictionary_Select_Dialog();
                        tempDialog.SetCallback(Layer_Field_Dialog.this.pCallback);
                        tempDialog.ShowDialog();
                    } else if (paramString.equals("数据字典选择返回")) {
                        String tempStr = String.valueOf(pObject);
                        if (tempStr.equals("")) {
                            Layer_Field_Dialog.this.m_DataSpinnerDialog.setTag(tempStr);
                            Layer_Field_Dialog.this.m_DataSpinnerDialog.SetTextJust(tempStr);
                            return;
                        }
                        String str = Common.CombineStrings(",", PubVar._PubCommand.m_ConfigDB.GetDataDictionaryManage().GetZDValueList(tempStr));
                        Layer_Field_Dialog.this.m_DataSpinnerDialog.setTag(tempStr);
                        Layer_Field_Dialog.this.m_DataSpinnerDialog.SetTextJust(str);
                    }
                } catch (Exception e) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.fieldedit_dialog);
        this._Dialog.Resize(0.96f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("字段属性");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        Common.SetSpinnerListData(this._Dialog, "字段类型", Common.StrArrayToList(new String[]{"字符串", "整型", "浮点型", "布尔型", "日期型"}), (int) R.id.sp_fieldtype, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Field_Dialog.2
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                String tempStr = Common.GetSpinnerValueOnID(Layer_Field_Dialog.this._Dialog, R.id.sp_fieldtype);
                if (tempStr.equals("布尔型")) {
                    Layer_Field_Dialog.this._Dialog.findViewById(R.id.et_size).setEnabled(false);
                    Common.SetTextViewValueOnID(Layer_Field_Dialog.this._Dialog, (int) R.id.et_size, "2");
                } else if (tempStr.equals("日期型")) {
                    Layer_Field_Dialog.this._Dialog.findViewById(R.id.et_size).setEnabled(false);
                    Common.SetTextViewValueOnID(Layer_Field_Dialog.this._Dialog, (int) R.id.et_size, "8");
                } else {
                    Layer_Field_Dialog.this._Dialog.findViewById(R.id.et_size).setEnabled(true);
                }
                Layer_Field_Dialog.this.refreshAutoValueList();
            }
        });
        this.m_DataSpinnerDialog = (SpinnerList) this._Dialog.findViewById(R.id.sp_valuelist);
        this.m_DataSpinnerDialog.SetClickCallback(this.pCallback);
        this.m_DataSpinnerDialog.SetSelectReturnTag("关联值域点击");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void LoadFieldDetailInfo() {
        if (this.m_EditField != null) {
            this.m_EditMode = EEditMode.EDIT;
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.et_name, this.m_EditField.GetFieldName());
            Common.SetValueToView(this.m_EditField.GetFieldTypeName(), this._Dialog.findViewById(R.id.sp_fieldtype));
            String tempStr = String.valueOf(this.m_EditField.GetFieldSize());
            if (this.m_EditField.GetFieldType() == EFieldType.BOOLEAN) {
                tempStr = "2";
                this._Dialog.findViewById(R.id.et_size).setEnabled(false);
            } else if (this.m_EditField.GetFieldType() == EFieldType.DATETIME) {
                tempStr = "8";
                this._Dialog.findViewById(R.id.et_size).setEnabled(false);
            }
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.et_size, tempStr);
            this.m_DataSpinnerDialog.setTag(this.m_EditField.GetFieldEnumCode());
            this.m_DataSpinnerDialog.SetTextJust(Common.CombineStrings(",", this.m_EditField.GetFieldEnumList()));
            Common.SetCheckValueOnID(this._Dialog, R.id.cb_inputmode, this.m_EditField.GetFieldEnumEdit());
            Common.SetCheckValueOnID(this._Dialog, R.id.cb_inputIsCode, this.m_EditField.GetIsEnumOfCode());
        } else {
            this.m_EditField = new LayerField();
            Iterator<LayerField> tempIter = this.m_EditLayer.GetFieldList().iterator();
            int tempI = 1;
            while (tempIter.hasNext() && tempIter.next().GetDataFieldName().equals("F" + tempI)) {
                tempI++;
            }
            this.m_EditField.SetDataFieldName("F" + tempI);
        }
        refreshAutoValueList();
        Common.SetValueToView(ConvertAutoTypeToName(this.m_EditField.AutoValueType), this._Dialog.findViewById(R.id.sp_autoFieldValueType));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshAutoValueList() {
        String tempFieldType = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_autoFieldValueType);
        List<String> tmpList = new ArrayList<>();
        tmpList.add("无");
        if (tempFieldType.equals("整型")) {
            tmpList.add("自动编号");
            tmpList.add("经度(整型)");
            tmpList.add("纬度(整型)");
            tmpList.add("X坐标(整数)");
            tmpList.add("Y坐标(整数)");
            tmpList.add("Z坐标(整数)");
        } else if (tempFieldType.equals("浮点型")) {
            tmpList.add("X坐标");
            tmpList.add("Y坐标");
            tmpList.add("Z坐标");
            tmpList.add("X坐标(整数)");
            tmpList.add("Y坐标(整数)");
            tmpList.add("Z坐标(整数)");
            tmpList.add("经度");
            tmpList.add("纬度");
            tmpList.add("经度(整型)");
            tmpList.add("纬度(整型)");
            tmpList.add("GPS经度");
            tmpList.add("GPS纬度");
            tmpList.add("GPS高程");
            if (this.m_EditLayer.GetLayerType() != EGeoLayerType.POINT) {
                if (this.m_EditLayer.GetLayerType() == EGeoLayerType.POLYLINE) {
                    tmpList.add("长度");
                } else if (this.m_EditLayer.GetLayerType() == EGeoLayerType.POLYGON) {
                    tmpList.add("长度");
                    tmpList.add("面积");
                }
            }
        } else if (tempFieldType.equals("日期型")) {
            tmpList.add("日期");
        } else {
            tmpList.add("自动编号");
            tmpList.add("X坐标");
            tmpList.add("Y坐标");
            tmpList.add("Z坐标");
            tmpList.add("X坐标(整数)");
            tmpList.add("Y坐标(整数)");
            tmpList.add("Z坐标(整数)");
            tmpList.add("经度");
            tmpList.add("纬度");
            tmpList.add("经度(整型)");
            tmpList.add("纬度(整型)");
            tmpList.add("GPS经度");
            tmpList.add("GPS纬度");
            tmpList.add("GPS高程");
            if (this.m_EditLayer.GetLayerType() != EGeoLayerType.POINT) {
                if (this.m_EditLayer.GetLayerType() == EGeoLayerType.POLYLINE) {
                    tmpList.add("长度");
                } else if (this.m_EditLayer.GetLayerType() == EGeoLayerType.POLYGON) {
                    tmpList.add("长度");
                    tmpList.add("面积");
                }
            }
            tmpList.add("日期");
            tmpList.add("日期时间");
        }
        Common.SetSpinnerListData(this._Dialog, "自动赋值类型", tmpList, (int) R.id.sp_autoFieldValueType, (ICallback) null);
    }

    public static EFieldAutoValueType ConvertTypeNameToType(String typename) {
        if (typename.equals("无") || typename.equals("")) {
            return EFieldAutoValueType.NONE;
        }
        if (typename.equals("自动编号")) {
            return EFieldAutoValueType.Auto_Increase;
        }
        if (typename.equals("X坐标")) {
            return EFieldAutoValueType.Geo_X;
        }
        if (typename.equals("Y坐标")) {
            return EFieldAutoValueType.Geo_Y;
        }
        if (typename.equals("Z坐标")) {
            return EFieldAutoValueType.Geo_Z;
        }
        if (typename.equals("X坐标(整数)")) {
            return EFieldAutoValueType.Geo_XInt;
        }
        if (typename.equals("Y坐标(整数)")) {
            return EFieldAutoValueType.Geo_YInt;
        }
        if (typename.equals("Z坐标(整数)")) {
            return EFieldAutoValueType.Geo_ZInt;
        }
        if (typename.equals("经度")) {
            return EFieldAutoValueType.Geo_Longitude;
        }
        if (typename.equals("纬度")) {
            return EFieldAutoValueType.Geo_Latitude;
        }
        if (typename.equals("GPS经度")) {
            return EFieldAutoValueType.GPS_Longitude;
        }
        if (typename.equals("GPS纬度")) {
            return EFieldAutoValueType.GPS_Latitude;
        }
        if (typename.equals("GPS高程")) {
            return EFieldAutoValueType.GPS_Elevation;
        }
        if (typename.equals("经度(整型)")) {
            return EFieldAutoValueType.Geo_Longitude_Int9;
        }
        if (typename.equals("纬度(整型)")) {
            return EFieldAutoValueType.Geo_Latitude_Int8;
        }
        if (typename.equals("长度")) {
            return EFieldAutoValueType.Geo_Length;
        }
        if (typename.equals("面积")) {
            return EFieldAutoValueType.Geo_Area;
        }
        if (typename.equals("日期")) {
            return EFieldAutoValueType.DateTime_Small;
        }
        if (typename.equals("日期时间")) {
            return EFieldAutoValueType.DateTime_Long;
        }
        return EFieldAutoValueType.NONE;
    }

    public static String ConvertAutoTypeToName(EFieldAutoValueType valueType) {
        if (valueType == EFieldAutoValueType.NONE) {
            return "无";
        }
        if (valueType == EFieldAutoValueType.Auto_Increase) {
            return "自动编号";
        }
        if (valueType == EFieldAutoValueType.Geo_X) {
            return "X坐标";
        }
        if (valueType == EFieldAutoValueType.Geo_Y) {
            return "Y坐标";
        }
        if (valueType == EFieldAutoValueType.Geo_Z) {
            return "Z坐标";
        }
        if (valueType == EFieldAutoValueType.Geo_XInt) {
            return "X坐标(整数)";
        }
        if (valueType == EFieldAutoValueType.Geo_YInt) {
            return "Y坐标(整数)";
        }
        if (valueType == EFieldAutoValueType.Geo_ZInt) {
            return "Z坐标(整数)";
        }
        if (valueType == EFieldAutoValueType.Geo_Longitude_Int9) {
            return "经度(整型)";
        }
        if (valueType == EFieldAutoValueType.Geo_Latitude_Int8) {
            return "纬度(整型)";
        }
        if (valueType == EFieldAutoValueType.Geo_Longitude) {
            return "经度";
        }
        if (valueType == EFieldAutoValueType.Geo_Latitude) {
            return "纬度";
        }
        if (valueType == EFieldAutoValueType.GPS_Longitude) {
            return "GPS经度";
        }
        if (valueType == EFieldAutoValueType.GPS_Latitude) {
            return "GPS纬度";
        }
        if (valueType == EFieldAutoValueType.GPS_Elevation) {
            return "GPS高程";
        }
        if (valueType == EFieldAutoValueType.Geo_Length) {
            return "长度";
        }
        if (valueType == EFieldAutoValueType.Geo_Area) {
            return "面积";
        }
        if (valueType == EFieldAutoValueType.DateTime_Small) {
            return "日期";
        }
        if (valueType == EFieldAutoValueType.DateTime_Long) {
            return "日期时间";
        }
        return "无";
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean SaveFieldDetailInfo() {
        try {
            String tempFieldName = Common.GetTextValueOnID(this._Dialog, (int) R.id.et_name);
            String tempFieldType = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_fieldtype);
            String tempSize = Common.GetTextValueOnID(this._Dialog, (int) R.id.et_size);
            String tempDictTag = "";
            if (this._Dialog.findViewById(R.id.sp_valuelist).getTag() != null) {
                tempDictTag = String.valueOf(this._Dialog.findViewById(R.id.sp_valuelist).getTag());
            }
            boolean tempBool = Common.GetCheckBoxValueOnID(this._Dialog, R.id.cb_inputmode);
            ArrayList tempOutMsg = new ArrayList();
            if (tempFieldName.equals("")) {
                tempOutMsg.add("【字段名称】不能为空值!");
            }
            if (tempSize.equals("")) {
                tempOutMsg.add("【字段长度】不能为空值!");
            }
            if (Integer.parseInt(tempSize) > 255) {
                tempOutMsg.add("【字段长度】应小于等于255!");
            }
            String tempFieldID = this.m_EditField.GetFieldID();
            Iterator<LayerField> tempIter = this.m_EditLayer.GetFieldList().iterator();
            while (true) {
                if (tempIter.hasNext()) {
                    LayerField tempField = tempIter.next();
                    if (tempField.GetDataFieldName().equals(tempFieldName) && !tempField.GetFieldID().equals(tempFieldID)) {
                        tempOutMsg.add("【字段名称】重复！");
                        break;
                    }
                } else {
                    break;
                }
            }
            if (tempOutMsg.size() > 0) {
                Common.ShowDialog(this._Dialog.getContext(), Common.CombineStrings("\r\n", tempOutMsg));
                return false;
            }
            this.m_EditField.AutoValueType = ConvertTypeNameToType(Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_autoFieldValueType));
            this.m_EditField.SetFieldName(tempFieldName);
            this.m_EditField.SetFieldTypeName(tempFieldType);
            this.m_EditField.SetFieldSize(Integer.parseInt(tempSize));
            this.m_EditField.SetFieldEnumCode(tempDictTag);
            this.m_EditField.SetFieldEnumEdit(tempBool);
            this.m_EditField.SetIsEnumOfCode(Common.GetCheckBoxValueOnID(this._Dialog, R.id.cb_inputIsCode));
            if (this.m_EditMode == EEditMode.NEW) {
                this.m_EditLayer.GetFieldList().add(this.m_EditField);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public void SetEditField(LayerField pLayerField) {
        this.m_EditField = pLayerField;
    }

    public void SetEditLayer(FeatureLayer pLayer) {
        this.m_EditLayer = pLayer;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Field_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Layer_Field_Dialog.this.LoadFieldDetailInfo();
            }
        });
        this._Dialog.show();
    }

    public void setAllowEditFieldType(boolean allowEdit) {
        this.m_AllowEditFieldType = allowEdit;
        this._Dialog.findViewById(R.id.sp_fieldtype).setEnabled(this.m_AllowEditFieldType);
    }
}
