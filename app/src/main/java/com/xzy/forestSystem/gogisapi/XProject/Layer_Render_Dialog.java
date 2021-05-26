package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Display.ClassifiedRender;
import  com.xzy.forestSystem.gogisapi.Display.ClassifiedRender_Setting_Dialog;
import  com.xzy.forestSystem.gogisapi.Display.EDisplayType;
import  com.xzy.forestSystem.gogisapi.Display.IRender;
import  com.xzy.forestSystem.gogisapi.Display.ISymbol;
import  com.xzy.forestSystem.gogisapi.Display.SimpleDisplay;
import  com.xzy.forestSystem.gogisapi.Display.SymbolObject;
import  com.xzy.forestSystem.gogisapi.Display.SymbolSelect_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.ColorPickerDialog;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Layer_Render_Dialog {
    private XDialogTemplate _Dialog;
    private InputSpinner _LabelFieldSpinnerDialog;
    private ICallback m_Callback;
    private FeatureLayer m_EditLayer;
    private IRender m_EditRender;
    private GeoLayer m_GeoLayer;
    private IRender m_Render;
    private boolean m_ShowTransparent;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public Layer_Render_Dialog() {
        this._Dialog = null;
        this.m_EditLayer = null;
        this.m_GeoLayer = null;
        this.m_Render = null;
        this.m_EditRender = null;
        this.m_Callback = null;
        this.m_ShowTransparent = true;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Render_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                int tmpRenderType;
                try {
                    if (paramString.contains("确定")) {
                        if (Common.GetSpinnerValueOnID(Layer_Render_Dialog.this._Dialog, R.id.sp_renderType).equals("多值符号")) {
                            tmpRenderType = 2;
                        } else {
                            tmpRenderType = 1;
                        }
                        if (tmpRenderType != Layer_Render_Dialog.this.m_EditLayer.GetRenderType()) {
                            Layer_Render_Dialog.this.m_EditLayer.SetRenderType(tmpRenderType);
                            Layer_Render_Dialog.this.m_EditRender.needUpdateSymbol = true;
                        }
                        Layer_Render_Dialog.this.m_Render = Layer_Render_Dialog.this.m_EditRender;
                        if (Layer_Render_Dialog.this.m_ShowTransparent && Layer_Render_Dialog.this.m_EditLayer.GetLayerType() == EGeoLayerType.POLYGON) {
                            Layer_Render_Dialog.this.m_Render.SetSymbolTransparent(255 - ((SeekBar) Layer_Render_Dialog.this._Dialog.findViewById(R.id.sb_transparent)).getProgress());
                        }
                        if (Common.GetSpinnerValueOnID(Layer_Render_Dialog.this._Dialog, R.id.sp_iflabel).equals("是")) {
                            Layer_Render_Dialog.this.m_Render.setIfLabel(true);
                            String tempDataFields = Layer_Render_Dialog.this.m_EditLayer.ConvertToDataField(Common.GetSpinnerValueOnID(Layer_Render_Dialog.this._Dialog, R.id.sp_fieldList));
                            if (!Layer_Render_Dialog.this.m_Render.getLabelDataField().equals(tempDataFields)) {
                                Layer_Render_Dialog.this.m_Render.setLabelDataField(tempDataFields);
                                Layer_Render_Dialog.this.m_Render.needUpdateLableContent = true;
                            }
                            Layer_Render_Dialog.this.m_Render.setTextSymbolFont(String.valueOf(String.format("#%X", Integer.valueOf(((TextView) Layer_Render_Dialog.this._Dialog.findViewById(R.id.et_label)).getCurrentTextColor()))) + "," + ((EditText) Layer_Render_Dialog.this._Dialog.findViewById(R.id.sp_labelsize1)).getText().toString());
                            String tmpSplitChar = Common.GetEditTextValueOnID(Layer_Render_Dialog.this._Dialog, R.id.sp_labelSplitChar);
                            if (!tmpSplitChar.equals(Layer_Render_Dialog.this.m_Render.getLabelSplitChar())) {
                                Layer_Render_Dialog.this.m_Render.SetLabelSplitChar(tmpSplitChar);
                                Layer_Render_Dialog.this.m_Render.needUpdateLableContent = true;
                            }
                        } else {
                            Layer_Render_Dialog.this.m_Render.setIfLabel(false);
                        }
                        Layer_Render_Dialog.this.m_Render.SetMinScale(Double.parseDouble(((InputSpinner) Layer_Render_Dialog.this._Dialog.findViewById(R.id.sp_minScale)).getEditTextView().getText().toString()));
                        Layer_Render_Dialog.this.m_Render.SetMaxScale(Double.parseDouble(((InputSpinner) Layer_Render_Dialog.this._Dialog.findViewById(R.id.sp_maxScale)).getEditTextView().getText().toString()));
                        if (Layer_Render_Dialog.this.m_Callback != null) {
                            Layer_Render_Dialog.this.m_Callback.OnClick("渲染", new Object[]{Layer_Render_Dialog.this.m_EditLayer.GetLayerID(), Layer_Render_Dialog.this.m_Render});
                        }
                        Layer_Render_Dialog.this._Dialog.dismiss();
                    } else if (paramString.contains("多值符号设置")) {
                        Object[] tempObjs = (Object[]) pObject;
                        if (tempObjs != null && tempObjs.length > 1) {
                            ClassifiedRender tmpRender = (ClassifiedRender) tempObjs[1];
                            tmpRender.CopyParasFrom(Layer_Render_Dialog.this.m_EditRender);
                            Layer_Render_Dialog.this.m_EditRender = tmpRender;
                            Layer_Render_Dialog.this.m_EditRender.needUpdateSymbol = true;
                        }
                    } else if (paramString.contains("字段组合选择")) {
                        Layer_Render_Dialog.this._LabelFieldSpinnerDialog.getEditTextView().setText(pObject.toString());
                    } else if (paramString.contains("符号设置")) {
                        if (Layer_Render_Dialog.this.m_EditRender.getType() == EDisplayType.SIMPLE) {
                            SymbolSelect_Dialog tempDialog = new SymbolSelect_Dialog();
                            tempDialog.SetCallback(Layer_Render_Dialog.this.pCallback);
                            HashMap tmpHash = new HashMap();
                            tmpHash.put("D1", "");
                            tmpHash.put("D2", Layer_Render_Dialog.this.m_EditRender.GetSymbolFigure());
                            tmpHash.put("D3", ((SimpleDisplay) Layer_Render_Dialog.this.m_EditRender).getSymbol());
                            tempDialog.SetSymbolTag(Layer_Render_Dialog.this.m_EditLayer.GetLayerID());
                            tempDialog.SetSelectedSymbol(tmpHash);
                            tempDialog.SetGeoLayerType(Layer_Render_Dialog.this.m_EditLayer.GetLayerType());
                            tempDialog.ShowDialog();
                        } else if (Layer_Render_Dialog.this.m_EditRender.getType() == EDisplayType.CLASSIFIED) {
                            ClassifiedRender_Setting_Dialog tempDialog2 = new ClassifiedRender_Setting_Dialog();
                            tempDialog2.SetLayerAndRender(Layer_Render_Dialog.this.m_EditLayer, Layer_Render_Dialog.this.m_GeoLayer, Layer_Render_Dialog.this.m_EditRender);
                            tempDialog2.SetCallback(Layer_Render_Dialog.this.pCallback);
                            tempDialog2.ShowDialog();
                        }
                    } else if (paramString.contains("符号库") && pObject != null && Layer_Render_Dialog.this.m_EditRender.getType() == EDisplayType.SIMPLE) {
                        Layer_Render_Dialog.this.m_EditRender.needUpdateSymbol = true;
                        ((SimpleDisplay) Layer_Render_Dialog.this.m_EditRender).SetSymbol((ISymbol) ((HashMap) pObject).get("D3"));
                        List<SymbolObject> tempSymbolsList = new ArrayList<>();
                        SymbolObject tmpSymbolObject = new SymbolObject();
                        tmpSymbolObject.SymbolName = "";
                        tmpSymbolObject.SymbolFigure = Layer_Render_Dialog.this.m_EditRender.GetSymbolFigure();
                        tempSymbolsList.add(tmpSymbolObject);
                        ((ImageView) Layer_Render_Dialog.this._Dialog.findViewById(R.id.img_symbolSample)).setImageBitmap(tmpSymbolObject.SymbolFigure);
                    }
                } catch (Exception e) {
                }
            }
        };
        this._LabelFieldSpinnerDialog = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.layer_render_dialog);
        this._Dialog.SetCaption("图层渲染");
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._LabelFieldSpinnerDialog = (InputSpinner) this._Dialog.findViewById(R.id.sp_fieldList);
    }

    public void SetRender(IRender render) {
        this.m_Render = render;
        if (this.m_Render != null) {
            this.m_Render.needUpdateLableContent = false;
        }
        this.m_EditRender = this.m_Render;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void LoadLayerRenderInfo() {
        if (this.m_EditLayer != null) {
            try {
                if (this.m_Render != null) {
                    Common.SetSpinnerListData(this._Dialog, "符号类型", Common.StrArrayToList(new String[]{"单值符号", "多值符号"}), (int) R.id.sp_renderType, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Render_Dialog.2
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            int tempTypeID = 1;
                            int tempTypeID2 = 1;
                            if (Common.GetSpinnerValueOnID(Layer_Render_Dialog.this._Dialog, R.id.sp_renderType).equals("多值符号")) {
                                tempTypeID = 2;
                            }
                            if (Layer_Render_Dialog.this.m_EditRender.getType() == EDisplayType.CLASSIFIED) {
                                tempTypeID2 = 2;
                            }
                            if (tempTypeID != tempTypeID2) {
                                int tempTypeID22 = 1;
                                if (Layer_Render_Dialog.this.m_Render.getType() == EDisplayType.CLASSIFIED) {
                                    tempTypeID22 = 2;
                                }
                                if (tempTypeID != tempTypeID22) {
                                    if (tempTypeID == 1) {
                                        Layer_Render_Dialog.this.m_EditRender = new SimpleDisplay(Layer_Render_Dialog.this.m_GeoLayer);
                                    } else if (tempTypeID == 2) {
                                        Layer_Render_Dialog.this.m_EditRender = new ClassifiedRender(Layer_Render_Dialog.this.m_GeoLayer);
                                    }
                                    if (Layer_Render_Dialog.this.m_EditRender != null) {
                                        Layer_Render_Dialog.this.m_EditRender.LoadSymbol(null);
                                    }
                                } else {
                                    Layer_Render_Dialog.this.m_EditRender = Layer_Render_Dialog.this.m_Render;
                                }
                            }
                            List<SymbolObject> tempSymbolsList = new ArrayList<>();
                            SymbolObject tmpSymbolObject = new SymbolObject();
                            tmpSymbolObject.SymbolName = "";
                            tmpSymbolObject.SymbolFigure = Layer_Render_Dialog.this.m_EditRender.GetSymbolFigure();
                            tempSymbolsList.add(tmpSymbolObject);
                            ((ImageView) Layer_Render_Dialog.this._Dialog.findViewById(R.id.img_symbolSample)).setImageBitmap(tmpSymbolObject.SymbolFigure);
                            Layer_Render_Dialog.this._Dialog.findViewById(R.id.ll_symbolSample).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Render_Dialog.2.1
                                @Override // android.view.View.OnClickListener
                                public void onClick(View arg0) {
                                    Layer_Render_Dialog.this.pCallback.OnClick("符号设置", null);
                                }
                            });
                        }
                    });
                    String tempStr = "单值符号";
                    if (this.m_Render.getType() == EDisplayType.CLASSIFIED) {
                        tempStr = "多值符号";
                    }
                    Common.SetValueToView(tempStr, this._Dialog.findViewById(R.id.sp_renderType));
                    if (this.m_Render != null) {
                        ImageView tmpSymbolImg = (ImageView) this._Dialog.findViewById(R.id.img_symbolSample);
                        Bitmap tmpSymBmp = this.m_Render.GetSymbolFigure();
                        if (tmpSymBmp != null) {
                            tmpSymbolImg.setImageBitmap(tmpSymBmp);
                        }
                    }
                    if (this.m_ShowTransparent && this.m_EditLayer.GetLayerType() == EGeoLayerType.POLYGON) {
                        this._Dialog.findViewById(R.id.ll_transparent).setVisibility(0);
                        ((SeekBar) this._Dialog.findViewById(R.id.sb_transparent)).setProgress(255 - this.m_EditLayer.GetTransparet());
                    }
                    Common.SetSpinnerListData(this._Dialog, "是否标注", Common.StrArrayToList(new String[]{"是", "否"}), (int) R.id.sp_iflabel, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Render_Dialog.3
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (!paramString.equals("OnItemSelected") || !pObject.toString().equals("是")) {
                                Layer_Render_Dialog.this.SetLabelInfoEnable(false);
                            } else {
                                Layer_Render_Dialog.this.SetLabelInfoEnable(true);
                            }
                        }
                    });
                    if (this.m_Render.getIfLabel()) {
                        Common.SetValueToView("是", this._Dialog.findViewById(R.id.sp_iflabel));
                        SetLabelInfoEnable(true);
                    } else {
                        Common.SetValueToView("否", this._Dialog.findViewById(R.id.sp_iflabel));
                        SetLabelInfoEnable(false);
                    }
                    String tempFieldsName = this.m_EditLayer.ConvertDataFieldToName(this.m_Render.getLabelDataField());
                    this._LabelFieldSpinnerDialog.setEditTextEnable(false);
                    this._LabelFieldSpinnerDialog.getEditTextView().setText(tempFieldsName);
                    this._LabelFieldSpinnerDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Render_Dialog.4
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            String tempSelectFieldNames = Layer_Render_Dialog.this._LabelFieldSpinnerDialog.getEditTextView().getText().toString();
                            Fields_Select_Dialog tempDialog = new Fields_Select_Dialog();
                            tempDialog.SetLayer(Layer_Render_Dialog.this.m_EditLayer);
                            tempDialog.SetSelectFieldNames(tempSelectFieldNames);
                            tempDialog.SetCallback(Layer_Render_Dialog.this.pCallback);
                            tempDialog.ShowDialog();
                        }
                    });
                    InputSpinner tempColorSpinnerDialog = (InputSpinner) this._Dialog.findViewById(R.id.sp_labelcolor);
                    tempColorSpinnerDialog.getEditTextView().setEnabled(false);
                    String[] tempLabelFontStrs = this.m_Render.getLabelFont().split(",");
                    int tmpLabelColorInt = Color.parseColor(tempLabelFontStrs[0]);
                    tempColorSpinnerDialog.getEditTextView().setBackgroundColor(tmpLabelColorInt);
                    tempColorSpinnerDialog.getEditTextView().setTag(Integer.valueOf(tmpLabelColorInt));
                    ((TextView) this._Dialog.findViewById(R.id.et_label)).setTextColor(tmpLabelColorInt);
                    tempColorSpinnerDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Render_Dialog.5
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            ColorPickerDialog tempDialog = new ColorPickerDialog();
                            int tmpColor = 0;
                            Object tmpObj = ((InputSpinner) Layer_Render_Dialog.this._Dialog.findViewById(R.id.sp_labelcolor)).getEditTextView().getTag();
                            if (tmpObj != null) {
                                tmpColor = Integer.parseInt(tmpObj.toString());
                            }
                            tempDialog.setInitialColor(tmpColor);
                            tempDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Render_Dialog.5.1
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String paramString2, Object pObject2) {
                                    int tmpColor2 = Color.parseColor(pObject2.toString());
                                    ((InputSpinner) Layer_Render_Dialog.this._Dialog.findViewById(R.id.sp_labelcolor)).getEditTextView().setBackgroundColor(tmpColor2);
                                    ((InputSpinner) Layer_Render_Dialog.this._Dialog.findViewById(R.id.sp_labelcolor)).getEditTextView().setTag(Integer.valueOf(tmpColor2));
                                    ((TextView) Layer_Render_Dialog.this._Dialog.findViewById(R.id.et_label)).setTextColor(tmpColor2);
                                }
                            });
                            tempDialog.ShowDialog();
                        }
                    });
                    Common.SetEditTextValueOnID(this._Dialog, R.id.sp_labelSplitChar, this.m_Render.getLabelSplitChar());
                    EditText localEditText = (EditText) this._Dialog.findViewById(R.id.sp_labelsize1);
                    String tempLabelFontSize = tempLabelFontStrs[1];
                    localEditText.addTextChangedListener(new TextWatcher() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Render_Dialog.6
                        @Override // android.text.TextWatcher
                        public void afterTextChanged(Editable paramEditable) {
                            TextView localTextView = (TextView) Layer_Render_Dialog.this._Dialog.findViewById(R.id.et_label);
                            if (Common.IsFloat(paramEditable.toString())) {
                                localTextView.setTextSize(0, Float.parseFloat(paramEditable.toString()) * PubVar.ScaledDensity);
                            }
                        }

                        @Override // android.text.TextWatcher
                        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                        }

                        @Override // android.text.TextWatcher
                        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                        }
                    });
                    localEditText.setText(tempLabelFontSize);
                    InputSpinner tempMinScaleSpinnerDialog = (InputSpinner) this._Dialog.findViewById(R.id.sp_minScale);
                    InputSpinner tempMaxScaleSpinnerDialog = (InputSpinner) this._Dialog.findViewById(R.id.sp_maxScale);
                    tempMinScaleSpinnerDialog.getEditTextView().setEnabled(true);
                    tempMaxScaleSpinnerDialog.getEditTextView().setEnabled(true);
                    tempMinScaleSpinnerDialog.setText(String.valueOf(this.m_EditLayer.GetMinScale()));
                    double tempDouble = this.m_EditLayer.GetMaxScale();
                    List tempScaleList = new ArrayList();
                    tempScaleList.add("最小可见比例");
                    tempScaleList.add("最大可见比例");
                    tempScaleList.add("当前比例");
                    tempMaxScaleSpinnerDialog.SetSelectItemList(tempScaleList);
                    tempMaxScaleSpinnerDialog.setText(String.valueOf(tempDouble));
                    tempMaxScaleSpinnerDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Render_Dialog.7
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (!paramString.contains("SpinnerSelectCallback")) {
                                return;
                            }
                            if (pObject.toString().equals("最小可见比例")) {
                                ((InputSpinner) Layer_Render_Dialog.this._Dialog.findViewById(R.id.sp_maxScale)).setText("0");
                            } else if (pObject.toString().equals("最大可见比例")) {
                                ((InputSpinner) Layer_Render_Dialog.this._Dialog.findViewById(R.id.sp_maxScale)).setText("2147483647");
                            } else if (pObject.toString().equals("当前比例")) {
                                ((InputSpinner) Layer_Render_Dialog.this._Dialog.findViewById(R.id.sp_maxScale)).setText(String.valueOf(PubVar._Map.getActualScale()));
                            }
                        }
                    });
                    tempMinScaleSpinnerDialog.SetSelectItemList(tempScaleList);
                    tempMinScaleSpinnerDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Render_Dialog.8
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (!paramString.contains("SpinnerSelectCallback")) {
                                return;
                            }
                            if (pObject.toString().equals("最小可见比例")) {
                                ((InputSpinner) Layer_Render_Dialog.this._Dialog.findViewById(R.id.sp_minScale)).setText("0");
                            } else if (pObject.toString().equals("最大可见比例")) {
                                ((InputSpinner) Layer_Render_Dialog.this._Dialog.findViewById(R.id.sp_minScale)).setText("2147483647");
                            } else if (pObject.toString().equals("当前比例")) {
                                ((InputSpinner) Layer_Render_Dialog.this._Dialog.findViewById(R.id.sp_minScale)).setText(String.valueOf(PubVar._Map.getActualScale()));
                            }
                        }
                    });
                }
            } catch (Exception ex) {
                Common.Log("CreateReport", "错误:" + ex.toString() + "-->" + ex.getMessage());
            }
        }
    }

    public void SetShowTransparent(boolean isShow) {
        this.m_ShowTransparent = isShow;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void SetLabelInfoEnable(boolean paramBoolean) {
        this._Dialog.findViewById(R.id.sp_fieldList).setEnabled(paramBoolean);
        this._Dialog.findViewById(R.id.sp_labelcolor).setEnabled(paramBoolean);
        this._Dialog.findViewById(R.id.sp_labelsize1).setEnabled(paramBoolean);
    }

    public void SetEditLayer(FeatureLayer pLayer) {
        this.m_EditLayer = pLayer;
        if (this.m_EditLayer != null) {
            this.m_GeoLayer = PubVar._Map.GetGeoLayerByDataSource(this.m_EditLayer.GetDataSourceName(), this.m_EditLayer.GetLayerID());
        }
    }

    public void SetGeoLayer(GeoLayer geoLayer) {
        this.m_GeoLayer = geoLayer;
        if (this.m_GeoLayer != null) {
            this._Dialog.SetCaption("图层渲染");
            this.m_EditLayer = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerByID(this.m_GeoLayer.getLayerID());
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Render_Dialog.9
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Layer_Render_Dialog.this.LoadLayerRenderInfo();
            }
        });
        this._Dialog.show();
    }
}
