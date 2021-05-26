package com.xzy.forestSystem.GuiZhou.DiaoCha;

import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.GPS.AddGPSPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;

public class YangMu2_Dialog {
    static String m_LastSelectShuZhong = "";
    static String m_LastXiongJingString = "0";
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private Object m_ReturnTag;
    private InputSpinner m_SpinnerList;
    EditText m_XiongJingET;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public YangMu2_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_SpinnerList = null;
        this.m_ReturnTag = null;
        this.m_XiongJingET = null;
        this.pCallback = new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.YangMu2_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                Coordinate tempCoord;
                if (command.equals("确定")) {
                    String tmpShuZhong = YangMu2_Dialog.this.m_SpinnerList.getText();
                    if (tmpShuZhong == null || tmpShuZhong.trim().length() == 0) {
                        Common.ShowDialog("树种不能为空.");
                        return;
                    }
                    String tmpXiongJingString = Common.GetEditTextValueOnID(YangMu2_Dialog.this._Dialog, R.id.et_Xiongjing);
                    if (tmpXiongJingString == null || tmpXiongJingString.trim().length() == 0) {
                        Common.ShowDialog("胸径不能为空.");
                        return;
                    }
                    double tmpXiongJing = 0.0d;
                    try {
                        tmpXiongJing = Double.parseDouble(tmpXiongJingString);
                    } catch (Exception e) {
                    }
                    if (tmpXiongJing <= 0.0d) {
                        Common.ShowDialog("胸径不能小于或等于0.");
                        return;
                    }
                    String tmpXiongJingString2 = Common.GetEditTextValueOnID(YangMu2_Dialog.this._Dialog, R.id.et_ShuGao);
                    if (tmpXiongJingString2 == null || tmpXiongJingString2.trim().length() == 0) {
                        tmpXiongJingString2 = "0";
                    }
                    double tmpShuGao = 0.0d;
                    try {
                        tmpShuGao = Double.parseDouble(tmpXiongJingString2);
                    } catch (Exception e2) {
                    }
                    String tmpRemarkString = Common.GetEditTextValueOnID(YangMu2_Dialog.this._Dialog, R.id.et_Remark);
                    double tmpX = 0.0d;
                    double tmpY = 0.0d;
                    String tmpStr01 = Common.GetEditTextValueOnID(YangMu2_Dialog.this._Dialog, R.id.et_X).trim();
                    if (tmpStr01.length() > 0) {
                        try {
                            tmpX = Double.parseDouble(tmpStr01);
                        } catch (Exception e3) {
                        }
                    }
                    String tmpStr012 = Common.GetEditTextValueOnID(YangMu2_Dialog.this._Dialog, R.id.et_Y).trim();
                    if (tmpStr012.length() > 0) {
                        try {
                            tmpY = Double.parseDouble(tmpStr012);
                        } catch (Exception e4) {
                        }
                    }
                    YangMu2_Dialog.m_LastXiongJingString = YangMu2_Dialog.this.m_XiongJingET.getText().toString();
                    if (YangMu2_Dialog.this.m_Callback != null) {
                        YangMu2_Dialog.this.m_Callback.OnClick("样木信息返回", new Object[]{YangMu2_Dialog.this.m_ReturnTag, new Object[]{tmpShuZhong, Double.valueOf(tmpXiongJing), Double.valueOf(tmpShuGao), tmpRemarkString, Double.valueOf(tmpX), Double.valueOf(tmpY)}});
                    }
                    YangMu2_Dialog.this._Dialog.dismiss();
                } else if (command.equals("SpinnerSelectCallback")) {
                    YangMu2_Dialog.m_LastSelectShuZhong = String.valueOf(object);
                    YangMu2_Dialog.this.m_XiongJingET.setFocusable(true);
                    YangMu2_Dialog.this.m_XiongJingET.setFocusableInTouchMode(true);
                    YangMu2_Dialog.this.m_XiongJingET.requestFocus();
                    YangMu2_Dialog.this.m_XiongJingET.requestFocusFromTouch();
                } else if (command.equals("选择树种返回")) {
                    YangMu2_Dialog.m_LastSelectShuZhong = String.valueOf(object);
                    YangMu2_Dialog.this.m_SpinnerList.setText(YangMu2_Dialog.m_LastSelectShuZhong);
                    YangMu2_Dialog.this.m_XiongJingET.setFocusable(true);
                    YangMu2_Dialog.this.m_XiongJingET.setFocusableInTouchMode(true);
                    YangMu2_Dialog.this.m_XiongJingET.requestFocus();
                    YangMu2_Dialog.this.m_XiongJingET.requestFocusFromTouch();
                } else if (command.equals("添加GPS采样点返回") && (tempCoord = (Coordinate) object) != null) {
                    Common.SetEditTextValueOnID(YangMu2_Dialog.this._Dialog, R.id.et_X, String.valueOf(tempCoord.getX()));
                    Common.SetEditTextValueOnID(YangMu2_Dialog.this._Dialog, R.id.et_Y, String.valueOf(tempCoord.getY()));
                    Common.ShowToast("设置GPS坐标数据成功!");
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.yangmu2_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("样木信息");
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_XiongJingET = (EditText) this._Dialog.findViewById(R.id.et_Xiongjing);
        this.m_SpinnerList = (InputSpinner) this._Dialog.findViewById(R.id.sp_YangdiShuZhong);
        this._Dialog.findViewById(R.id.btn_FromGPS).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_FromMeasure).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_FromMapCenter).setOnClickListener(new ViewClick());
        this.m_SpinnerList.getEditTextView().setEnabled(true);
        this.m_SpinnerList.setEnabled(true);
        this.m_SpinnerList.SetClickCallback(new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.YangMu2_Dialog.2
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object pObject) {
                if (command.equals("SpinnerSelectCallback")) {
                    ShuZhongSelect_Dialog2 tmpDialog = new ShuZhongSelect_Dialog2();
                    tmpDialog.SetSelectName(YangMu2_Dialog.this.m_SpinnerList.getText());
                    tmpDialog.SetCallback(YangMu2_Dialog.this.pCallback);
                    tmpDialog.ShowDialog();
                }
            }
        });
        if (m_LastSelectShuZhong.length() > 0) {
            this.m_SpinnerList.setText(m_LastSelectShuZhong);
            this.m_XiongJingET.setFocusable(true);
            this.m_XiongJingET.setFocusableInTouchMode(true);
            this.m_XiongJingET.requestFocus();
            this.m_XiongJingET.requestFocusFromTouch();
        }
        this.m_XiongJingET.setText("");
    }

    public void setReturnTag(Object returnTag) {
        this.m_ReturnTag = returnTag;
    }

    public void SetTitle(String title) {
        this._Dialog.SetCaption(title);
    }

    public void SetDefaultData(String shuZhong, String xiongjing, String shuGao, String remark, String x, String y) {
        this.m_SpinnerList.setText(shuZhong);
        Common.SetEditTextValueOnID(this._Dialog, R.id.et_Xiongjing, xiongjing);
        Common.SetEditTextValueOnID(this._Dialog, R.id.et_ShuGao, shuGao);
        Common.SetEditTextValueOnID(this._Dialog, R.id.et_Remark, remark);
        Common.SetEditTextValueOnID(this._Dialog, R.id.et_X, x);
        Common.SetEditTextValueOnID(this._Dialog, R.id.et_Y, y);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("GPS采样")) {
                AddGPSPoint_Dialog tempDialog = new AddGPSPoint_Dialog();
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
            } else if (command.equals("地图中心")) {
                Coordinate tempCoord = PubVar._Map.getCenter();
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_X, String.valueOf(tempCoord.getX()));
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_Y, String.valueOf(tempCoord.getY()));
            } else if (command.equals("测量点")) {
                Coordinate tempCoord2 = PubVar._PubCommand.m_Measure.GetLastCoordinate();
                if (tempCoord2 == null) {
                    Common.ShowDialog("请先利用【测量】工具测量目标坐标点.");
                    return;
                }
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_X, String.valueOf(tempCoord2.getX()));
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_Y, String.valueOf(tempCoord2.getY()));
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.YangMu2_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
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
                YangMu2_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
