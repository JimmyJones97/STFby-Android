package com.xzy.forestSystem.GuiZhou.DiaoCha;

import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CalXuJiSingle_Dialog2 {
    private static DecimalFormat m_DF_2 = new DecimalFormat("#.##");
    private static DecimalFormat m_DF_5 = new DecimalFormat("#.#####");
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
        if (this.m_Callback != null) {
            this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        }
    }

    public CalXuJiSingle_Dialog2() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.CalXuJiSingle_Dialog2.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("确定")) {
                    String tmpStr = Common.GetEditTextValueOnID(CalXuJiSingle_Dialog2.this._Dialog, R.id.et_XuJiTotal);
                    if (tmpStr.equals("")) {
                        Common.ShowDialog("没有任何计算结果.");
                    } else if (tmpStr.equals("计算错误.")) {
                        Common.ShowDialog("计算结果错,不能返回.");
                    } else {
                        if (CalXuJiSingle_Dialog2.this.m_Callback != null) {
                            CalXuJiSingle_Dialog2.this.m_Callback.OnClick("蓄积量计算返回", tmpStr);
                        }
                        CalXuJiSingle_Dialog2.this._Dialog.dismiss();
                    }
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.calxujisingle_dialog2);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("单株/多株蓄积量计算");
        this._Dialog.findViewById(R.id.btn_clear).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_calXJL).setOnClickListener(new ViewClick());
        List<String> tmpList = new ArrayList<>();
        tmpList.add("杉木");
        tmpList.add("马尾松");
        tmpList.add("华山松");
        tmpList.add("云南松");
        tmpList.add("柏木");
        tmpList.add("软阔");
        tmpList.add("硬阔");
        Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_treeType, tmpList.get(0), "请选择", tmpList, (String) null, this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("清空")) {
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_PJSG, "");
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_PJXJ, "");
            } else if (command.equals("蓄积量计算")) {
                String tmpString = Common.GetEditTextValueOnID(this._Dialog, R.id.et_PJSG).trim();
                if (tmpString.length() == 0) {
                    Common.ShowToast("请输入正确的平均树高.");
                    return;
                }
                double tmpH = Double.parseDouble(tmpString);
                if (tmpH < 4.0d || tmpH > 50.0d) {
                    Common.ShowToast("树高超出范围,树高值应该在 [4 - 50] 范围内.");
                    return;
                }
                String tmpString2 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_PJXJ).trim();
                if (tmpString2.length() == 0) {
                    Common.ShowToast("请输入正确的平均胸径.");
                    return;
                }
                double tmpD = Double.parseDouble(tmpString2);
                if (tmpD < 5.0d || tmpH > 100.0d) {
                    Common.ShowToast("胸径超出范围,胸径值应该在 [5 - 100] 范围内.");
                    return;
                }
                double tmpV01 = CommonSetting.CalXuJiValue(Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_treeType), tmpD, tmpH);
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_XuJiSingle, m_DF_5.format(tmpV01));
                String tmpString3 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_TreeCount).trim();
                if (tmpString3.length() == 0) {
                    Common.ShowToast("请输入正确的株数.");
                    return;
                }
                double tmpCount = Double.parseDouble(tmpString3);
                if (tmpCount < 1.0d) {
                    Common.ShowToast("请输入正确的株数.");
                    return;
                }
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_XuJiTotal, m_DF_2.format(tmpV01 * tmpCount));
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.CalXuJiSingle_Dialog2.2
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
                CalXuJiSingle_Dialog2.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
