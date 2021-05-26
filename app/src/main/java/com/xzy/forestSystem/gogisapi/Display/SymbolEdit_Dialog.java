package  com.xzy.forestSystem.gogisapi.Display;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import androidx.core.internal.view.SupportMenu;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.CommonProcess;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.ColorPickerDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.ImageSpinnerDialog;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.Input_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SymbolEdit_Dialog {
    private XDialogTemplate _Dialog;
    private int _SelectSymbolType;
    private String _SymbolParas;
    private int _SymbolType;
    private HashMap<String, Object> _lineSelectSymbol;
    private ICallback m_Callback;
    private MyTableFactory m_LineHeaderListViewFactory;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private HashMap<String, Object> m_SelectItem;
    private ICallback pCallback;
    private RadioGroup polyFillTypeRadioGroup;
    private ImageView symbolImageView;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public SymbolEdit_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_SelectItem = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                String[] tempPath2;
                String[] tempStrs;
                if (paramString.equals("确定")) {
                    if (SymbolEdit_Dialog.this._SymbolType == 1) {
                        if (SymbolEdit_Dialog.this.symbolImageView.getTag() != null) {
                            Bitmap tmpBmp = (Bitmap) SymbolEdit_Dialog.this.symbolImageView.getTag();
                            PointSymbol tmpSymbol = (PointSymbol) SymbolEdit_Dialog.this.m_SelectItem.get("D3");
                            tmpSymbol.setIcon(tmpBmp);
                            tmpSymbol.setConfigParas(SymbolEdit_Dialog.this._SymbolParas);
                            SymbolEdit_Dialog.this.m_SelectItem.put("D2", tmpBmp);
                            SymbolEdit_Dialog.this.m_SelectItem.put("D3", tmpSymbol);
                            if (SymbolEdit_Dialog.this.m_Callback != null) {
                                SymbolEdit_Dialog.this.m_Callback.OnClick("编辑符号", SymbolEdit_Dialog.this.m_SelectItem);
                            }
                            SymbolEdit_Dialog.this._Dialog.dismiss();
                        }
                    } else if (SymbolEdit_Dialog.this._SymbolType == 2) {
                        if (!(SymbolEdit_Dialog.this._SymbolParas.equals("") || (tempStrs = SymbolEdit_Dialog.this._SymbolParas.split(",")) == null || tempStrs.length <= 2)) {
                            PolygonSymbol tmpSymbol2 = (PolygonSymbol) SymbolEdit_Dialog.this.m_SelectItem.get("D3");
                            if (SymbolEdit_Dialog.this.polyFillTypeRadioGroup.getCheckedRadioButtonId() == R.id.rg_polygonFillType_R01) {
                                tmpSymbol2.setPSColor(Color.parseColor(tempStrs[0]));
                                tmpSymbol2.getPStyle().setShader(null);
                            } else {
                                tmpSymbol2.getPStyle().setShader(new BitmapShader(CommonProcess.Base64ToBitmap(tempStrs[0]), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
                            }
                            tmpSymbol2.setLSColor(Color.parseColor(tempStrs[1]));
                            tmpSymbol2.setLSWidth(Float.parseFloat(tempStrs[2]));
                            tmpSymbol2.setConfigParas(SymbolEdit_Dialog.this._SymbolParas);
                            SymbolEdit_Dialog.this.m_SelectItem.put("D2", tmpSymbol2.DrawSymbolFigure());
                            SymbolEdit_Dialog.this.m_SelectItem.put("D3", tmpSymbol2);
                            if (SymbolEdit_Dialog.this.m_Callback != null) {
                                SymbolEdit_Dialog.this.m_Callback.OnClick("编辑符号", SymbolEdit_Dialog.this.m_SelectItem);
                            }
                            SymbolEdit_Dialog.this._Dialog.dismiss();
                        }
                    } else if (SymbolEdit_Dialog.this._SymbolType == 3 && SymbolEdit_Dialog.this.symbolImageView.getTag() != null) {
                        StringBuilder tempSB = new StringBuilder();
                        if (SymbolEdit_Dialog.this.m_MyTableDataList.size() > 0) {
                            int tmpTid = 0;
                            for (HashMap<String, Object> tempHash : SymbolEdit_Dialog.this.m_MyTableDataList) {
                                if (tempHash != null) {
                                    if (tmpTid != 0) {
                                        tempSB.append("@");
                                    }
                                    tempSB.append(tempHash.get("D3").toString());
                                }
                                tmpTid++;
                            }
                        } else {
                            tempSB.append("#FF0000,2");
                        }
                        SymbolEdit_Dialog.this._SymbolParas = tempSB.toString();
                        LineSymbol tmpSymbol3 = (LineSymbol) SymbolEdit_Dialog.this.m_SelectItem.get("D3");
                        tmpSymbol3.setConfigParas(SymbolEdit_Dialog.this._SymbolParas);
                        SymbolEdit_Dialog.this.m_SelectItem.put("D2", tmpSymbol3.DrawSymbolFigure());
                        SymbolEdit_Dialog.this.m_SelectItem.put("D3", tmpSymbol3);
                        if (SymbolEdit_Dialog.this.m_Callback != null) {
                            SymbolEdit_Dialog.this.m_Callback.OnClick("编辑符号", SymbolEdit_Dialog.this.m_SelectItem);
                        }
                        SymbolEdit_Dialog.this._Dialog.dismiss();
                    }
                } else if (paramString.contains("简单符号样式选择")) {
                    HashMap tempHash2 = (HashMap) pObject;
                    if (tempHash2 != null) {
                        SymbolEdit_Dialog.this._SelectSymbolType = Integer.parseInt(tempHash2.get("D2").toString());
                        if (SymbolEdit_Dialog.this._SymbolType == 1) {
                            Bitmap tmpBmp2 = (Bitmap) tempHash2.get("D1");
                            List<SymbolObject> tempSymbolsList = new ArrayList<>();
                            SymbolObject tmpSymbolObject = new SymbolObject();
                            tmpSymbolObject.SymbolName = "";
                            tmpSymbolObject.SymbolFigure = tmpBmp2;
                            tempSymbolsList.add(tmpSymbolObject);
                            ((ImageSpinnerDialog) SymbolEdit_Dialog.this._Dialog.findViewById(R.id.sp_pointType)).SetImageItemList(tempSymbolsList);
                            SymbolEdit_Dialog.this.symbolImageView.setImageBitmap(tmpBmp2);
                        } else if (SymbolEdit_Dialog.this._SymbolType == 3) {
                            SymbolEdit_Dialog.this.DrawSymbolType();
                        }
                    }
                } else if (paramString.contains("ImageSpinnerCallback")) {
                    if (SymbolEdit_Dialog.this._SymbolType == 1 || SymbolEdit_Dialog.this._SymbolType == 3) {
                        SimpleSymbol_Select_Dialog tempDialog = new SimpleSymbol_Select_Dialog();
                        tempDialog.setSymbolType(SymbolEdit_Dialog.this._SymbolType);
                        tempDialog.SetCallback(SymbolEdit_Dialog.this.pCallback);
                        tempDialog.ShowDialog();
                    }
                } else if (paramString.equals("另存")) {
                    if (!SymbolEdit_Dialog.this._SymbolParas.equals("")) {
                        Input_Dialog tempDialog2 = new Input_Dialog();
                        tempDialog2.setValues("新符号名称", "新符号名称:", "");
                        tempDialog2.SetCallback(SymbolEdit_Dialog.this.pCallback);
                        tempDialog2.ShowDialog();
                        return;
                    }
                    Common.ShowDialog("符号没有定义,不能保存.");
                } else if (paramString.equals("输入参数")) {
                    if (!SymbolEdit_Dialog.this._SymbolParas.equals("")) {
                        Object[] tmpObjs = (Object[]) pObject;
                        if (tmpObjs != null && tmpObjs.length >= 1) {
                            String tempName = tmpObjs[1].toString();
                            if (tempName == null || tempName.equals("")) {
                                Common.ShowDialog("新符号名称无效.");
                                return;
                            }
                            if (SymbolEdit_Dialog.this._SymbolType == 3) {
                                StringBuilder tempSB2 = new StringBuilder();
                                if (SymbolEdit_Dialog.this.m_MyTableDataList.size() > 0) {
                                    int tmpTid2 = 0;
                                    for (HashMap<String, Object> tempHash3 : SymbolEdit_Dialog.this.m_MyTableDataList) {
                                        if (tempHash3 != null) {
                                            if (tmpTid2 != 0) {
                                                tempSB2.append("@");
                                            }
                                            tempSB2.append(tempHash3.get("D3").toString());
                                        }
                                        tmpTid2++;
                                    }
                                } else {
                                    tempSB2.append("#FF0000,2");
                                }
                                SymbolEdit_Dialog.this._SymbolParas = tempSB2.toString();
                            }
                            String[] tmpOutMsg = new String[1];
                            if (PubVar._PubCommand.m_ConfigDB.GetSymbolManage().SaveSymbolInSystem(tempName, SymbolEdit_Dialog.this._SymbolType, SymbolEdit_Dialog.this._SymbolParas, tmpOutMsg)) {
                                Common.ShowToast("保存新符号[" + tempName + "]成功.");
                            } else if (!tmpOutMsg[0].equals("")) {
                                Common.ShowDialog(tmpOutMsg[0]);
                            }
                        }
                    } else {
                        Common.ShowDialog("符号没有定义,不能保存.");
                    }
                } else if (paramString.equals("单击选择行")) {
                    SymbolEdit_Dialog.this._lineSelectSymbol = (HashMap) pObject;
                    SymbolEdit_Dialog.this.refreshLineLayoutView(SymbolEdit_Dialog.this._lineSelectSymbol);
                } else if (paramString.contains("选择文件_选择面填充图案") && (tempPath2 = pObject.toString().split(";")) != null && tempPath2.length > 1) {
                    String str = tempPath2[0];
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(tempPath2[1]));
                        String tempParas = CommonProcess.BitmapToBase64(bitmap);
                        ImageView tmpFillImg = (ImageView) SymbolEdit_Dialog.this._Dialog.findViewById(R.id.imgView_polygonFill);
                        tmpFillImg.setImageBitmap(bitmap);
                        tmpFillImg.setTag(tempParas);
                        SymbolEdit_Dialog.this.DrawSymbolType();
                    } catch (Exception e) {
                    }
                }
            }
        };
        this.symbolImageView = null;
        this._SymbolType = 0;
        this._SymbolParas = "";
        this._SelectSymbolType = -1;
        this.polyFillTypeRadioGroup = null;
        this.m_MyTableDataList = new ArrayList();
        this._lineSelectSymbol = null;
        this.m_LineHeaderListViewFactory = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.symboledit_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("编辑符号");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.symbolImageView = (ImageView) this._Dialog.findViewById(R.id.imageViewSymbol);
        this.symbolImageView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                SymbolEdit_Dialog.this.DrawSymbolType();
            }
        });
        this._Dialog.findViewById(R.id.btn_symboledit_saveas).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                SymbolEdit_Dialog.this.pCallback.OnClick("另存", null);
            }
        });
    }

    public void SetSelectedSymbol(HashMap<String, Object> object) {
        this.m_SelectItem = object;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshSymbol() {
        ISymbol tmpSymbol;
        if (this.m_SelectItem != null && (tmpSymbol = (ISymbol) this.m_SelectItem.get("D3")) != null) {
            this._SymbolParas = tmpSymbol.getConfigParas();
            this.symbolImageView.setImageBitmap(tmpSymbol.getSymbolFigure());
            if (tmpSymbol instanceof PointSymbol) {
                this._Dialog.SetHeightWrapContent();
                this._SymbolType = 1;
                this._Dialog.SetCaption("编辑点符号");
                ((LinearLayout) this._Dialog.findViewById(R.id.pointSymbolLyt)).setVisibility(0);
                ((LinearLayout) this._Dialog.findViewById(R.id.polygonSymbolLyt)).setVisibility(8);
                ((LinearLayout) this._Dialog.findViewById(R.id.lineSymbolLyt)).setVisibility(8);
                Bitmap tmpBmp = ((PointSymbol) tmpSymbol).getIcon();
                if (tmpBmp == null) {
                    this._Dialog.dismiss();
                    return;
                }
                this.symbolImageView.setTag(tmpBmp);
                final int tmpSymbolSize = tmpBmp.getHeight();
                String tmpSize = String.valueOf(tmpSymbolSize);
                EditText localEditText = (EditText) this._Dialog.findViewById(R.id.sp_pointSize);
                localEditText.addTextChangedListener(new TextWatcher() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.4
                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable paramEditable) {
                        if (Common.IsFloat(paramEditable.toString())) {
                            float tempSize = Float.parseFloat(paramEditable.toString());
                            if (tempSize > 0.0f && tempSize <= 512.0f) {
                                if (SymbolEdit_Dialog.this._SelectSymbolType == -1) {
                                    float tempRatio = tempSize / ((float) tmpSymbolSize);
                                    if (tempRatio != 1.0f) {
                                        Bitmap tmpBmp2 = Common.ScaleBitmap(((PointSymbol) SymbolEdit_Dialog.this.m_SelectItem.get("D3")).getIcon(), tempRatio, tempRatio);
                                        SymbolEdit_Dialog.this.symbolImageView.setImageBitmap(tmpBmp2);
                                        SymbolEdit_Dialog.this.symbolImageView.setTag(tmpBmp2);
                                        SymbolEdit_Dialog.this._SymbolParas = CommonProcess.BitmapToBase64(tmpBmp2);
                                        return;
                                    }
                                    return;
                                }
                                SymbolEdit_Dialog.this.DrawSymbolType();
                            }
                        }
                    }

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    }
                });
                localEditText.setText(tmpSize);
                ImageSpinnerDialog typeSpinnerDialog = (ImageSpinnerDialog) this._Dialog.findViewById(R.id.sp_pointType);
                List<SymbolObject> tempSymbolsList = new ArrayList<>();
                SymbolObject tmpSymbolObject = new SymbolObject();
                tmpSymbolObject.SymbolName = "";
                tmpSymbolObject.SymbolFigure = tmpBmp;
                tempSymbolsList.add(tmpSymbolObject);
                typeSpinnerDialog.SetImageItemList(tempSymbolsList);
                typeSpinnerDialog.SetIsShowText(false);
                typeSpinnerDialog.SetCallback(this.pCallback);
                InputSpinner tempColorSpinnerDialog = (InputSpinner) this._Dialog.findViewById(R.id.sp_pointcolor);
                tempColorSpinnerDialog.getEditTextView().setEnabled(false);
                tempColorSpinnerDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.5
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        if (SymbolEdit_Dialog.this._SelectSymbolType != -1) {
                            ColorPickerDialog tempDialog = new ColorPickerDialog();
                            int tmpColor = 0;
                            Object tmpObj = ((InputSpinner) SymbolEdit_Dialog.this._Dialog.findViewById(R.id.sp_pointcolor)).getEditTextView().getTag();
                            if (tmpObj != null) {
                                tmpColor = Integer.parseInt(tmpObj.toString());
                            }
                            tempDialog.setInitialColor(tmpColor);
                            tempDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.5.1
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String paramString2, Object pObject2) {
                                    int tmpColor2 = Color.parseColor(pObject2.toString());
                                    ((InputSpinner) SymbolEdit_Dialog.this._Dialog.findViewById(R.id.sp_pointcolor)).getEditTextView().setBackgroundColor(tmpColor2);
                                    ((InputSpinner) SymbolEdit_Dialog.this._Dialog.findViewById(R.id.sp_pointcolor)).getEditTextView().setTag(Integer.valueOf(tmpColor2));
                                    SymbolEdit_Dialog.this.DrawSymbolType();
                                }
                            });
                            tempDialog.ShowDialog();
                        }
                    }
                });
                tempColorSpinnerDialog.getEditTextView().setBackgroundColor(SupportMenu.CATEGORY_MASK);
                tempColorSpinnerDialog.getEditTextView().setTag(Integer.valueOf((int) SupportMenu.CATEGORY_MASK));
            } else if (tmpSymbol instanceof PolygonSymbol) {
                this._Dialog.SetHeightWrapContent();
                this._SymbolType = 2;
                this._Dialog.SetCaption("编辑面符号");
                ((LinearLayout) this._Dialog.findViewById(R.id.pointSymbolLyt)).setVisibility(8);
                ((LinearLayout) this._Dialog.findViewById(R.id.polygonSymbolLyt)).setVisibility(0);
                ((LinearLayout) this._Dialog.findViewById(R.id.lineSymbolLyt)).setVisibility(8);
                String[] tempStrs = this._SymbolParas.split(",");
                if (tempStrs != null && tempStrs.length > 2) {
                    String tmpFillColorStr = tempStrs[0];
                    this.polyFillTypeRadioGroup = (RadioGroup) this._Dialog.findViewById(R.id.rg_polygonFillType);
                    try {
                        if (tmpFillColorStr.startsWith("#")) {
                            int tmpColor01 = Color.parseColor(tmpFillColorStr);
                            ((RadioButton) this._Dialog.findViewById(R.id.rg_polygonFillType_R01)).setChecked(true);
                            this._Dialog.findViewById(R.id.ll_polyFillColor).setVisibility(0);
                            this._Dialog.findViewById(R.id.ll_polyFillImage).setVisibility(8);
                            ((InputSpinner) this._Dialog.findViewById(R.id.sp_polyColor)).getEditTextView().setBackgroundColor(tmpColor01);
                            ((InputSpinner) this._Dialog.findViewById(R.id.sp_polyColor)).getEditTextView().setTag(Integer.valueOf(tmpColor01));
                        } else {
                            Bitmap tempBmp = CommonProcess.Base64ToBitmap(tmpFillColorStr);
                            ((RadioButton) this._Dialog.findViewById(R.id.rg_polygonFillType_R02)).setChecked(true);
                            this._Dialog.findViewById(R.id.ll_polyFillImage).setVisibility(0);
                            this._Dialog.findViewById(R.id.ll_polyFillColor).setVisibility(8);
                            ImageView tmpFillImg = (ImageView) this._Dialog.findViewById(R.id.imgView_polygonFill);
                            tmpFillImg.setImageBitmap(tempBmp);
                            tmpFillImg.setTag(tmpFillColorStr);
                            ((InputSpinner) this._Dialog.findViewById(R.id.sp_polyColor)).getEditTextView().setBackgroundColor(-1);
                            ((InputSpinner) this._Dialog.findViewById(R.id.sp_polyColor)).getEditTextView().setTag(-1);
                        }
                    } catch (Exception e) {
                    }
                    this.polyFillTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.6
                        @Override // android.widget.RadioGroup.OnCheckedChangeListener
                        public void onCheckedChanged(RadioGroup arg0, int arg1) {
                            if (arg1 == R.id.rg_polygonFillType_R01) {
                                SymbolEdit_Dialog.this._Dialog.findViewById(R.id.ll_polyFillColor).setVisibility(0);
                                SymbolEdit_Dialog.this._Dialog.findViewById(R.id.ll_polyFillImage).setVisibility(8);
                            } else {
                                SymbolEdit_Dialog.this._Dialog.findViewById(R.id.ll_polyFillImage).setVisibility(0);
                                SymbolEdit_Dialog.this._Dialog.findViewById(R.id.ll_polyFillColor).setVisibility(8);
                            }
                            SymbolEdit_Dialog.this.DrawSymbolType();
                        }
                    });
                    int tmpColor02 = Color.parseColor(tempStrs[1]);
                    ((InputSpinner) this._Dialog.findViewById(R.id.sp_polyBorderColor)).getEditTextView().setBackgroundColor(tmpColor02);
                    ((InputSpinner) this._Dialog.findViewById(R.id.sp_polyBorderColor)).getEditTextView().setTag(Integer.valueOf(tmpColor02));
                    ((EditText) this._Dialog.findViewById(R.id.sp_polyLineWidthSize)).setText(tempStrs[2]);
                    ((InputSpinner) this._Dialog.findViewById(R.id.sp_polyColor)).getEditTextView().setEnabled(false);
                    ((InputSpinner) this._Dialog.findViewById(R.id.sp_polyBorderColor)).getEditTextView().setEnabled(false);
                }
                ((Button) this._Dialog.findViewById(R.id.btn_symboledit_LoadImgFile)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.7
                    @Override // android.view.View.OnClickListener
                    public void onClick(View arg0) {
                        FileSelector_Dialog tempDialog = new FileSelector_Dialog(".png;", false);
                        Common.ShowToast("请选择PNG文件作为面填充图案.");
                        tempDialog.SetTag("选择面填充图案");
                        tempDialog.SetCallback(SymbolEdit_Dialog.this.pCallback);
                        tempDialog.ShowDialog();
                    }
                });
                ((InputSpinner) this._Dialog.findViewById(R.id.sp_polyColor)).SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.8
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        ColorPickerDialog tempDialog = new ColorPickerDialog();
                        int tmpColor = 0;
                        try {
                            Object tmpObj = ((InputSpinner) SymbolEdit_Dialog.this._Dialog.findViewById(R.id.sp_polyColor)).getEditTextView().getTag();
                            if (tmpObj != null) {
                                tmpColor = Integer.parseInt(tmpObj.toString());
                            }
                        } catch (Exception e2) {
                        }
                        tempDialog.setInitialColor(tmpColor);
                        tempDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.8.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject2) {
                                int tmpColor2 = Color.parseColor(pObject2.toString());
                                ((InputSpinner) SymbolEdit_Dialog.this._Dialog.findViewById(R.id.sp_polyColor)).getEditTextView().setBackgroundColor(tmpColor2);
                                ((InputSpinner) SymbolEdit_Dialog.this._Dialog.findViewById(R.id.sp_polyColor)).getEditTextView().setTag(Integer.valueOf(tmpColor2));
                                SymbolEdit_Dialog.this.DrawSymbolType();
                            }
                        });
                        tempDialog.ShowDialog();
                    }
                });
                ((InputSpinner) this._Dialog.findViewById(R.id.sp_polyBorderColor)).SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.9
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        ColorPickerDialog tempDialog = new ColorPickerDialog();
                        int tmpColor = 0;
                        try {
                            Object tmpObj = ((InputSpinner) SymbolEdit_Dialog.this._Dialog.findViewById(R.id.sp_polyBorderColor)).getEditTextView().getTag();
                            if (tmpObj != null) {
                                tmpColor = Integer.parseInt(tmpObj.toString());
                            }
                        } catch (Exception e2) {
                        }
                        tempDialog.setInitialColor(tmpColor);
                        tempDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.9.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject2) {
                                int tmpColor2 = Color.parseColor(pObject2.toString());
                                ((InputSpinner) SymbolEdit_Dialog.this._Dialog.findViewById(R.id.sp_polyBorderColor)).getEditTextView().setBackgroundColor(tmpColor2);
                                ((InputSpinner) SymbolEdit_Dialog.this._Dialog.findViewById(R.id.sp_polyBorderColor)).getEditTextView().setTag(Integer.valueOf(tmpColor2));
                                SymbolEdit_Dialog.this.DrawSymbolType();
                            }
                        });
                        tempDialog.ShowDialog();
                    }
                });
                ((EditText) this._Dialog.findViewById(R.id.sp_polyLineWidthSize)).addTextChangedListener(new TextWatcher() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.10
                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable paramEditable) {
                        if (Common.IsFloat(paramEditable.toString())) {
                            SymbolEdit_Dialog.this.DrawSymbolType();
                        }
                    }

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    }
                });
            } else if (tmpSymbol instanceof LineSymbol) {
                this._SymbolType = 3;
                this._SelectSymbolType = 0;
                this._Dialog.SetCaption("编辑线符号");
                ((LinearLayout) this._Dialog.findViewById(R.id.pointSymbolLyt)).setVisibility(8);
                ((LinearLayout) this._Dialog.findViewById(R.id.polygonSymbolLyt)).setVisibility(8);
                ((LinearLayout) this._Dialog.findViewById(R.id.lineSymbolLyt)).setVisibility(0);
                this.m_MyTableDataList = new ArrayList();
                String[] tempStrs2 = this._SymbolParas.split("@");
                if (tempStrs2 != null && tempStrs2.length > 0) {
                    int tmpTid = 0;
                    int length = tempStrs2.length;
                    for (int i = 0; i < length; i++) {
                        String tmpLineStr = tempStrs2[i];
                        if (!tmpLineStr.equals("")) {
                            HashMap tmpHashMap = new HashMap();
                            Bitmap tmpBmp2 = drawLineSymbol(tmpLineStr);
                            tmpHashMap.put("D1", false);
                            tmpHashMap.put("D2", tmpBmp2);
                            tmpHashMap.put("D3", tmpLineStr);
                            tmpTid++;
                            tmpHashMap.put("D4", Integer.valueOf(tmpTid));
                            this.m_MyTableDataList.add(tmpHashMap);
                        } else {
                            tmpTid = tmpTid;
                        }
                    }
                }
                drawLinesSymbol();
                refreshLineSymbols();
                ((Button) this._Dialog.findViewById(R.id.btn_New)).setOnClickListener(new ViewClick());
                ((Button) this._Dialog.findViewById(R.id.btn_Up)).setOnClickListener(new ViewClick());
                ((Button) this._Dialog.findViewById(R.id.btn_Down)).setOnClickListener(new ViewClick());
                ((Button) this._Dialog.findViewById(R.id.btn_Delete)).setOnClickListener(new ViewClick());
                EditText localEditText2 = (EditText) this._Dialog.findViewById(R.id.sp_lineWidth);
                localEditText2.addTextChangedListener(new TextWatcher() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.11
                    @Override // android.text.TextWatcher
                    public void afterTextChanged(Editable paramEditable) {
                        if (Common.IsFloat(paramEditable.toString())) {
                            SymbolEdit_Dialog.this.DrawSymbolType();
                        }
                    }

                    @Override // android.text.TextWatcher
                    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    }

                    @Override // android.text.TextWatcher
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    }
                });
                localEditText2.setText("2.0");
                ImageSpinnerDialog typeSpinnerDialog2 = (ImageSpinnerDialog) this._Dialog.findViewById(R.id.sp_lineType);
                typeSpinnerDialog2.SetIsShowText(false);
                typeSpinnerDialog2.SetCallback(this.pCallback);
                InputSpinner tempColorSpinnerDialog2 = (InputSpinner) this._Dialog.findViewById(R.id.sp_linecolor);
                tempColorSpinnerDialog2.getEditTextView().setEnabled(false);
                tempColorSpinnerDialog2.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.12
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        ColorPickerDialog tempDialog = new ColorPickerDialog();
                        int tmpColor = 0;
                        try {
                            Object tmpObj = ((InputSpinner) SymbolEdit_Dialog.this._Dialog.findViewById(R.id.sp_linecolor)).getEditTextView().getTag();
                            if (tmpObj != null) {
                                tmpColor = Integer.parseInt(tmpObj.toString());
                            }
                        } catch (Exception e2) {
                        }
                        tempDialog.setInitialColor(tmpColor);
                        tempDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.12.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject2) {
                                int tmpColor2 = Color.parseColor(pObject2.toString());
                                ((InputSpinner) SymbolEdit_Dialog.this._Dialog.findViewById(R.id.sp_linecolor)).getEditTextView().setBackgroundColor(tmpColor2);
                                ((InputSpinner) SymbolEdit_Dialog.this._Dialog.findViewById(R.id.sp_linecolor)).getEditTextView().setTag(Integer.valueOf(tmpColor2));
                                SymbolEdit_Dialog.this.DrawSymbolType();
                            }
                        });
                        tempDialog.ShowDialog();
                    }
                });
                if (this.m_MyTableDataList.size() > 0) {
                    this._lineSelectSymbol = this.m_MyTableDataList.get(0);
                    refreshLineLayoutView(this._lineSelectSymbol);
                }
            }
        }
    }

    private void refreshLineSymbols() {
        if (this._SymbolType == 3) {
            this.m_LineHeaderListViewFactory = new MyTableFactory();
            this.m_LineHeaderListViewFactory.SetHeaderListView(this._Dialog.findViewById(R.id.linesymbols_list), "自定义", "选择,符号", "checkbox,image", new int[]{-20, -80}, this.pCallback);
            this.m_LineHeaderListViewFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2"}, this.pCallback);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLineLayoutView(HashMap<String, Object> hashMap) {
        if (hashMap != null) {
            String[] tempStrs01 = hashMap.get("D3").toString().split(",");
            if (tempStrs01 != null && tempStrs01.length > 1) {
                int tempColor = Color.parseColor(tempStrs01[0]);
                InputSpinner tempColorSpinnerDialog = (InputSpinner) this._Dialog.findViewById(R.id.sp_linecolor);
                tempColorSpinnerDialog.getEditTextView().setBackgroundColor(tempColor);
                tempColorSpinnerDialog.getEditTextView().setTag(Integer.valueOf(tempColor));
                if (tempStrs01.length <= 2) {
                    this._SelectSymbolType = 0;
                } else if (tempStrs01[2].equals("10*5")) {
                    this._SelectSymbolType = 1;
                } else {
                    this._SelectSymbolType = 2;
                }
                ((EditText) this._Dialog.findViewById(R.id.sp_lineWidth)).setText(tempStrs01[1]);
            }
            List<SymbolObject> tempSymbolsList = new ArrayList<>();
            SymbolObject tmpSymbolObject = new SymbolObject();
            tmpSymbolObject.SymbolName = "";
            tmpSymbolObject.SymbolFigure = (Bitmap) hashMap.get("D2");
            tempSymbolsList.add(tmpSymbolObject);
            ((ImageSpinnerDialog) this._Dialog.findViewById(R.id.sp_lineType)).SetImageItemList(tempSymbolsList);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DrawSymbolType() {
        String tempSizeStr;
        Bitmap tmpFillBmp;
        try {
            if (this._SymbolType == 1) {
                if (this._SelectSymbolType >= 0) {
                    float tempSize = Float.parseFloat(((EditText) this._Dialog.findViewById(R.id.sp_pointSize)).getText().toString());
                    if (tempSize > 0.0f && tempSize < 512.0f) {
                        Bitmap tmpBmp = Bitmap.createBitmap((int) tempSize, (int) tempSize, Bitmap.Config.ARGB_8888);
                        Canvas tmpCanvas = new Canvas(tmpBmp);
                        Paint tmpPaint = new Paint();
                        int tempColor = 0;
                        try {
                            Object tmpObj = ((InputSpinner) this._Dialog.findViewById(R.id.sp_pointcolor)).getEditTextView().getTag();
                            if (tmpObj != null) {
                                tempColor = Integer.parseInt(String.valueOf(tmpObj));
                            }
                        } catch (Exception e) {
                        }
                        tmpPaint.setColor(tempColor);
                        tmpPaint.setStyle(Paint.Style.FILL);
                        tmpPaint.setAntiAlias(true);
                        if (this._SelectSymbolType == 0) {
                            tmpCanvas.drawCircle(tempSize / 2.0f, tempSize / 2.0f, tempSize / 2.0f, tmpPaint);
                        } else if (this._SelectSymbolType == 1) {
                            tmpCanvas.drawRect(0.0f, 0.0f, tempSize, tempSize, tmpPaint);
                        } else if (this._SelectSymbolType == 2) {
                            Path tmpPath = new Path();
                            tmpPath.moveTo(0.0f, tempSize);
                            tmpPath.lineTo(tempSize, tempSize);
                            tmpPath.lineTo(tempSize / 2.0f, 0.0f);
                            tmpPath.lineTo(0.0f, tempSize);
                            tmpCanvas.drawPath(tmpPath, tmpPaint);
                        }
                        this._SymbolParas = CommonProcess.BitmapToBase64(tmpBmp);
                        this.symbolImageView.setImageBitmap(tmpBmp);
                        this.symbolImageView.setTag(tmpBmp);
                    }
                }
            } else if (this._SymbolType == 2) {
                if (this.polyFillTypeRadioGroup.getCheckedRadioButtonId() == R.id.rg_polygonFillType_R01) {
                    Object tmpObj2 = ((InputSpinner) this._Dialog.findViewById(R.id.sp_polyColor)).getEditTextView().getTag();
                    Object tmpObj22 = ((InputSpinner) this._Dialog.findViewById(R.id.sp_polyBorderColor)).getEditTextView().getTag();
                    if (tmpObj2 != null && tmpObj22 != null) {
                        float tempSize2 = Float.parseFloat(((EditText) this._Dialog.findViewById(R.id.sp_polyLineWidthSize)).getText().toString());
                        Bitmap tmpBmp2 = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
                        Canvas tmpCanvas2 = new Canvas(tmpBmp2);
                        Paint tmpPaint2 = new Paint();
                        int tempColor2 = 0;
                        try {
                            tempColor2 = Integer.parseInt(String.valueOf(tmpObj2));
                        } catch (Exception e2) {
                        }
                        tmpPaint2.setColor(tempColor2);
                        tmpPaint2.setStyle(Paint.Style.FILL);
                        tmpPaint2.setAntiAlias(true);
                        tmpCanvas2.drawRect(0.0f, 0.0f, 64.0f, 64.0f, tmpPaint2);
                        int tempColor22 = 0;
                        if (tempSize2 > 0.0f) {
                            tempColor22 = 0;
                            try {
                                tempColor22 = Integer.parseInt(String.valueOf(tmpObj22));
                            } catch (Exception e3) {
                            }
                            Paint tmpPaint22 = new Paint();
                            tmpPaint22.setColor(tempColor22);
                            tmpPaint22.setStrokeWidth(tempSize2);
                            tmpPaint22.setStyle(Paint.Style.STROKE);
                            tmpPaint22.setAntiAlias(true);
                            tmpCanvas2.drawRect(0.0f, 0.0f, 64.0f, 64.0f, tmpPaint22);
                        }
                        this._SymbolParas = String.valueOf(ColorPickerDialog.ColorToHex(tempColor2)) + "," + ColorPickerDialog.ColorToHex(tempColor22) + "," + String.valueOf(tempSize2);
                        this.symbolImageView.setImageBitmap(tmpBmp2);
                        this.symbolImageView.setTag(tmpBmp2);
                        return;
                    }
                    return;
                }
                Object tmpObj3 = this._Dialog.findViewById(R.id.imgView_polygonFill).getTag();
                Object tmpObj23 = ((InputSpinner) this._Dialog.findViewById(R.id.sp_polyBorderColor)).getEditTextView().getTag();
                if (tmpObj3 == null) {
                    ImageView tmpImgView = (ImageView) this._Dialog.findViewById(R.id.imgView_polygonFill);
                    tmpImgView.setDrawingCacheEnabled(true);
                    tmpFillBmp = Bitmap.createBitmap(tmpImgView.getDrawingCache());
                    tmpImgView.setDrawingCacheEnabled(false);
                } else {
                    tmpFillBmp = CommonProcess.Base64ToBitmap(String.valueOf(tmpObj3));
                }
                if (tmpObj3 != null && tmpObj23 != null) {
                    float tmpScaleX = 1.0f;
                    float tmpScaleY = 1.0f;
                    int tmpDisX = 0;
                    int tmpDisY = 0;
                    try {
                        tmpScaleX = Float.parseFloat(Common.GetEditTextValueOnID(this._Dialog, R.id.et_polyFillScaleX));
                    } catch (Exception e4) {
                    }
                    if (tmpScaleX <= 0.0f) {
                        tmpScaleX = 1.0f;
                    }
                    try {
                        tmpScaleY = Float.parseFloat(Common.GetEditTextValueOnID(this._Dialog, R.id.et_polyFillScaleY));
                    } catch (Exception e5) {
                    }
                    if (tmpScaleY <= 0.0f) {
                        tmpScaleY = 1.0f;
                    }
                    try {
                        tmpDisX = Integer.parseInt(Common.GetEditTextValueOnID(this._Dialog, R.id.et_polyFillDisX));
                    } catch (Exception e6) {
                    }
                    try {
                        tmpDisY = Integer.parseInt(Common.GetEditTextValueOnID(this._Dialog, R.id.et_polyFillDisY));
                    } catch (Exception e7) {
                    }
                    float tempSize3 = Float.parseFloat(((EditText) this._Dialog.findViewById(R.id.sp_polyLineWidthSize)).getText().toString());
                    Bitmap tmpBmp3 = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
                    Canvas tmpCanvas3 = new Canvas(tmpBmp3);
                    Paint tmpPaint3 = new Paint();
                    tmpPaint3.setStyle(Paint.Style.FILL);
                    Bitmap tmpFillBmp2 = Common.ScaleBitmap(tmpFillBmp, tmpScaleX, tmpScaleY, tmpDisX, tmpDisY);
                    tmpPaint3.setShader(new BitmapShader(tmpFillBmp2, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
                    tmpPaint3.setAntiAlias(true);
                    tmpCanvas3.drawRect(0.0f, 0.0f, 64.0f, 64.0f, tmpPaint3);
                    int tempColor23 = 0;
                    if (tempSize3 > 0.0f) {
                        tempColor23 = 0;
                        try {
                            tempColor23 = Integer.parseInt(String.valueOf(tmpObj23));
                        } catch (Exception e8) {
                        }
                        Paint tmpPaint23 = new Paint();
                        tmpPaint23.setColor(tempColor23);
                        tmpPaint23.setStrokeWidth(tempSize3);
                        tmpPaint23.setStyle(Paint.Style.STROKE);
                        tmpPaint23.setAntiAlias(true);
                        tmpCanvas3.drawRect(0.0f, 0.0f, 64.0f, 64.0f, tmpPaint23);
                    }
                    this._SymbolParas = String.valueOf(CommonProcess.BitmapToBase64(tmpFillBmp2)) + "," + ColorPickerDialog.ColorToHex(tempColor23) + "," + String.valueOf(tempSize3);
                    this.symbolImageView.setImageBitmap(tmpBmp3);
                    this.symbolImageView.setTag(tmpBmp3);
                }
            } else if (this._SymbolType == 3 && this._lineSelectSymbol != null && (tempSizeStr = ((EditText) this._Dialog.findViewById(R.id.sp_lineWidth)).getText().toString()) != null && !tempSizeStr.equals("")) {
                StringBuilder tempParas = new StringBuilder();
                float tempSize4 = Float.parseFloat(tempSizeStr);
                Object tmpObj24 = ((InputSpinner) this._Dialog.findViewById(R.id.sp_linecolor)).getEditTextView().getTag();
                if (tmpObj24 != null) {
                    int tmpColor = 0;
                    try {
                        tmpColor = Integer.parseInt(String.valueOf(tmpObj24));
                    } catch (Exception e9) {
                    }
                    if (tmpColor != 0) {
                        tempParas.append(ColorPickerDialog.ColorToHex(tmpColor));
                    } else {
                        tempParas.append("#FF0000");
                    }
                } else {
                    tempParas.append("#FF0000");
                }
                tempParas.append(",");
                tempParas.append(tempSize4);
                if (this._SelectSymbolType == 1) {
                    tempParas.append(",10*5");
                } else if (this._SelectSymbolType == 2) {
                    tempParas.append(",10*2");
                }
                int tempIndex = Integer.parseInt(this._lineSelectSymbol.get("D4").toString());
                Bitmap tmpBmp4 = drawLineSymbol(tempParas.toString());
                if (tmpBmp4 != null) {
                    this._lineSelectSymbol.put("D2", tmpBmp4);
                    List<SymbolObject> tempSymbolsList = new ArrayList<>();
                    SymbolObject tmpSymbolObject = new SymbolObject();
                    tmpSymbolObject.SymbolName = "";
                    tmpSymbolObject.SymbolFigure = tmpBmp4;
                    tempSymbolsList.add(tmpSymbolObject);
                    ((ImageSpinnerDialog) this._Dialog.findViewById(R.id.sp_lineType)).SetImageItemList(tempSymbolsList);
                }
                this._lineSelectSymbol.put("D3", tempParas.toString());
                this.m_MyTableDataList.remove(this._lineSelectSymbol);
                this.m_MyTableDataList.add(tempIndex, this._lineSelectSymbol);
                Bitmap tmpBmp22 = drawLinesSymbol();
                this.symbolImageView.setImageBitmap(tmpBmp22);
                this.symbolImageView.setTag(tmpBmp22);
                this.m_LineHeaderListViewFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2"}, this.pCallback);
            }
        } catch (Exception e10) {
        }
    }

    private Bitmap drawLineSymbol(String paras) {
        String[] tempStrs;
        String[] tempStrs02;
        Bitmap bitmap = null;
        if (!paras.equals("") && (tempStrs = paras.split(",")) != null && tempStrs.length > 1) {
            int tempColor = Color.parseColor(tempStrs[0].toString());
            float tempSize = Float.parseFloat(tempStrs[1]);
            bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
            Canvas tmpCanvas = new Canvas(bitmap);
            Paint tmpPaint = new Paint();
            tmpPaint.setColor(tempColor);
            tmpPaint.setStrokeWidth(tempSize);
            tmpPaint.setStyle(Paint.Style.STROKE);
            tmpPaint.setAntiAlias(true);
            if (tempStrs.length > 2 && (tempStrs02 = tempStrs[2].split("\\*")) != null && tempStrs02.length > 1) {
                tmpPaint.setPathEffect(new DashPathEffect(new float[]{Float.parseFloat(tempStrs02[0]), Float.parseFloat(tempStrs02[1])}, 1.0f));
            }
            tmpCanvas.drawLine(0.0f, 20.0f, 64.0f, 20.0f, tmpPaint);
        }
        return bitmap;
    }

    private Bitmap drawLinesSymbol() {
        Bitmap bitmap = null;
        if (this.m_MyTableDataList.size() > 0) {
            bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
            Canvas tmpCanvas = new Canvas(bitmap);
            Paint tmpPaint = new Paint();
            tmpPaint.setStyle(Paint.Style.STROKE);
            tmpPaint.setAntiAlias(true);
            for (HashMap<String, Object> tempHash : this.m_MyTableDataList) {
                Bitmap tmpBmp = (Bitmap) tempHash.get("D2");
                if (tmpBmp != null) {
                    tmpCanvas.drawBitmap(tmpBmp, 0.0f, 0.0f, tmpPaint);
                }
            }
        }
        return bitmap;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Display.SymbolEdit_Dialog.13
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                SymbolEdit_Dialog.this.refreshSymbol();
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("新建线符号")) {
            HashMap tmpHashMap = new HashMap();
            Bitmap tmpBmp = drawLineSymbol("#FF0000,2");
            tmpHashMap.put("D1", false);
            tmpHashMap.put("D2", tmpBmp);
            tmpHashMap.put("D3", "#FF0000,2");
            tmpHashMap.put("D4", Integer.valueOf(this.m_MyTableDataList.size()));
            this.m_MyTableDataList.add(tmpHashMap);
            this.m_LineHeaderListViewFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2"}, this.pCallback);
            Bitmap tmpBmp2 = drawLinesSymbol();
            this.symbolImageView.setImageBitmap(tmpBmp2);
            this.symbolImageView.setTag(tmpBmp2);
        } else if (command.equals("上移线符号")) {
            HashMap tempMap00 = null;
            Iterator<HashMap<String, Object>> tempIter = this.m_MyTableDataList.iterator();
            while (true) {
                if (!tempIter.hasNext()) {
                    break;
                }
                HashMap tempHash = tempIter.next();
                if (Boolean.parseBoolean(tempHash.get("D1").toString())) {
                    tempMap00 = tempHash;
                    break;
                }
            }
            if (tempMap00 != null) {
                int tempIndex = Integer.parseInt(tempMap00.get("D4").toString());
                if (tempIndex > 0) {
                    HashMap tempMap01 = this.m_MyTableDataList.get(tempIndex - 1);
                    tempMap01.put("D4", Integer.valueOf(tempIndex));
                    tempMap00.put("D4", Integer.valueOf(tempIndex - 1));
                    this.m_MyTableDataList.remove(tempIndex);
                    this.m_MyTableDataList.remove(tempIndex - 1);
                    this.m_MyTableDataList.add(tempIndex - 1, tempMap00);
                    this.m_MyTableDataList.add(tempIndex, tempMap01);
                    this.m_LineHeaderListViewFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2"}, this.pCallback);
                    int tempSelIndex = Integer.parseInt(this._lineSelectSymbol.get("D4").toString());
                    if (tempSelIndex == tempIndex - 1) {
                        this._lineSelectSymbol = tempMap00;
                    } else if (tempSelIndex == tempIndex) {
                        this._lineSelectSymbol = tempMap01;
                    }
                    refreshLineLayoutView(this._lineSelectSymbol);
                    Bitmap tmpBmp22 = drawLinesSymbol();
                    this.symbolImageView.setImageBitmap(tmpBmp22);
                    this.symbolImageView.setTag(tmpBmp22);
                    return;
                }
                Common.ShowToast("已经在最上层.");
                return;
            }
            Common.ShowToast("没有选择任何对象.");
        } else if (command.equals("下移线符号")) {
            HashMap tempMap002 = null;
            Iterator<HashMap<String, Object>> tempIter2 = this.m_MyTableDataList.iterator();
            while (true) {
                if (!tempIter2.hasNext()) {
                    break;
                }
                HashMap tempHash2 = tempIter2.next();
                if (Boolean.parseBoolean(tempHash2.get("D1").toString())) {
                    tempMap002 = tempHash2;
                    break;
                }
            }
            if (tempMap002 != null) {
                int tempIndex2 = Integer.parseInt(tempMap002.get("D4").toString());
                if (tempIndex2 < this.m_MyTableDataList.size() - 1) {
                    HashMap tempMap012 = this.m_MyTableDataList.get(tempIndex2 + 1);
                    tempMap012.put("D4", Integer.valueOf(tempIndex2));
                    tempMap002.put("D4", Integer.valueOf(tempIndex2 + 1));
                    this.m_MyTableDataList.remove(tempIndex2 + 1);
                    this.m_MyTableDataList.remove(tempIndex2);
                    this.m_MyTableDataList.add(tempIndex2, tempMap012);
                    this.m_MyTableDataList.add(tempIndex2 + 1, tempMap002);
                    this.m_LineHeaderListViewFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2"}, this.pCallback);
                    int tempSelIndex2 = Integer.parseInt(this._lineSelectSymbol.get("D4").toString());
                    if (tempSelIndex2 == tempIndex2) {
                        this._lineSelectSymbol = tempMap012;
                    } else if (tempSelIndex2 == tempIndex2 + 1) {
                        this._lineSelectSymbol = tempMap002;
                    }
                    refreshLineLayoutView(this._lineSelectSymbol);
                    Bitmap tmpBmp23 = drawLinesSymbol();
                    this.symbolImageView.setImageBitmap(tmpBmp23);
                    this.symbolImageView.setTag(tmpBmp23);
                    return;
                }
                Common.ShowToast("已经在最底层.");
                return;
            }
            Common.ShowToast("没有选择任何对象.");
        } else if (command.equals("删除线符号")) {
            HashMap tempMap003 = null;
            Iterator<HashMap<String, Object>> tempIter3 = this.m_MyTableDataList.iterator();
            while (true) {
                if (!tempIter3.hasNext()) {
                    break;
                }
                HashMap tempHash3 = tempIter3.next();
                if (Boolean.parseBoolean(tempHash3.get("D1").toString())) {
                    tempMap003 = tempHash3;
                    break;
                }
            }
            if (tempMap003 == null) {
                Common.ShowToast("没有选择任何对象.");
            } else if (this.m_MyTableDataList.size() > 1) {
                this.m_MyTableDataList.remove(tempMap003);
                int tempTid = 0;
                for (HashMap<String, Object> tempHash4 : this.m_MyTableDataList) {
                    tempHash4.put("D4", Integer.valueOf(tempTid));
                    tempTid++;
                }
                this._lineSelectSymbol = this.m_MyTableDataList.get(0);
                refreshLineLayoutView(this._lineSelectSymbol);
                Bitmap tmpBmp24 = drawLinesSymbol();
                this.symbolImageView.setImageBitmap(tmpBmp24);
                this.symbolImageView.setTag(tmpBmp24);
            } else {
                Common.ShowToast("不能删除最后一个线型符号.");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                SymbolEdit_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
