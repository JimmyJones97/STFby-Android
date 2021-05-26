package  com.xzy.forestSystem.gogisapi.Tools;

import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalXuJiSingle_Dialog {
    private static DecimalFormat m_DF_2 = new DecimalFormat("#.00");
    private static DecimalFormat m_DF_5 = new DecimalFormat("#.00000");
    private static double[] m_TreeTypeParam = null;
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public CalXuJiSingle_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalXuJiSingle_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                double[] tmpDs;
                if (command.equals("确定")) {
                    String tmpStr = Common.GetEditTextValueOnID(CalXuJiSingle_Dialog.this._Dialog, R.id.et_XuJiSingle);
                    if (tmpStr.equals("")) {
                        Common.ShowDialog("没有任何计算结果.");
                    } else if (tmpStr.equals("计算错误.")) {
                        Common.ShowDialog("计算结果错,不能返回.");
                    } else {
                        if (CalXuJiSingle_Dialog.this.m_Callback != null) {
                            CalXuJiSingle_Dialog.this.m_Callback.OnClick("蓄积量计算返回", tmpStr);
                        }
                        CalXuJiSingle_Dialog.this._Dialog.dismiss();
                    }
                } else if (command.equals("蓄积量实验形数设置返回") && (tmpDs = (double[]) object) != null && tmpDs.length > 3) {
                    CalXuJiSingle_Dialog.m_TreeTypeParam = tmpDs;
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.calxujisingle_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("单株/多株蓄积量计算");
        this._Dialog.findViewById(R.id.btn_clear).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_calXJL).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_setting).setOnClickListener(new ViewClick());
        List<String> tmpList = new ArrayList<>();
        tmpList.add("松木");
        tmpList.add("人工杉木");
        tmpList.add("天然杉木");
        tmpList.add("阔叶树");
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
                if (tmpH < 4.0d || tmpH > 25.0d) {
                    Common.ShowToast("树高超出范围,树高值应该在 [4 - 25] 范围内.");
                    return;
                }
                String tmpString2 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_PJXJ).trim();
                if (tmpString2.length() == 0) {
                    Common.ShowToast("请输入正确的平均胸径.");
                    return;
                }
                double tmpD = Double.parseDouble(tmpString2);
                if (tmpD < 5.0d || tmpH > 30.0d) {
                    Common.ShowToast("胸径超出范围,树高值应该在 [5 - 30] 范围内.");
                    return;
                }
                String tmpTreeType = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_treeType);
                double tmpFa = 0.39d;
                if (tmpTreeType.equals("松木")) {
                    tmpFa = m_TreeTypeParam[0];
                } else if (tmpTreeType.equals("人工杉木")) {
                    tmpFa = m_TreeTypeParam[1];
                } else if (tmpTreeType.equals("天然杉木")) {
                    tmpFa = m_TreeTypeParam[2];
                } else if (tmpTreeType.equals("阔叶树")) {
                    tmpFa = m_TreeTypeParam[3];
                }
                double tmpV01 = 7.854E-5d * tmpD * tmpD * (3.0d + tmpH) * tmpFa;
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
            } else if (command.equals("参数设置")) {
                XuJiParamSetting_Dialog tmpDialog = new XuJiParamSetting_Dialog();
                tmpDialog.setParam(m_TreeTypeParam);
                tmpDialog.SetCallback(this.pCallback);
                tmpDialog.ShowDialog();
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void readTreeTypeParam() {
        String tempStr;
        String tempStr2;
        String tempStr3;
        String tempStr4;
        try {
            m_TreeTypeParam = new double[]{0.39d, 0.42d, 0.39d, 0.38d};
            HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_XuJi_Param_Xinshu01");
            if (!(tempHashMap == null || (tempStr4 = tempHashMap.get("F2")) == null || tempStr4.equals(""))) {
                double tmpD = 0.0d;
                try {
                    tmpD = Double.parseDouble(tempStr4);
                } catch (Exception e) {
                }
                if (tmpD != 0.0d) {
                    m_TreeTypeParam[0] = tmpD;
                }
            }
            HashMap<String, String> tempHashMap2 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_XuJi_Param_Xinshu02");
            if (!(tempHashMap2 == null || (tempStr3 = tempHashMap2.get("F2")) == null || tempStr3.equals(""))) {
                double tmpD2 = 0.0d;
                try {
                    tmpD2 = Double.parseDouble(tempStr3);
                } catch (Exception e2) {
                }
                if (tmpD2 != 0.0d) {
                    m_TreeTypeParam[1] = tmpD2;
                }
            }
            HashMap<String, String> tempHashMap3 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_XuJi_Param_Xinshu03");
            if (!(tempHashMap3 == null || (tempStr2 = tempHashMap3.get("F2")) == null || tempStr2.equals(""))) {
                double tmpD3 = 0.0d;
                try {
                    tmpD3 = Double.parseDouble(tempStr2);
                } catch (Exception e3) {
                }
                if (tmpD3 != 0.0d) {
                    m_TreeTypeParam[2] = tmpD3;
                }
            }
            HashMap<String, String> tempHashMap4 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_XuJi_Param_Xinshu04");
            if (tempHashMap4 != null && (tempStr = tempHashMap4.get("F2")) != null && !tempStr.equals("")) {
                double tmpD4 = 0.0d;
                try {
                    tmpD4 = Double.parseDouble(tempStr);
                } catch (Exception e4) {
                }
                if (tmpD4 != 0.0d) {
                    m_TreeTypeParam[3] = tmpD4;
                }
            }
        } catch (Exception e5) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalXuJiSingle_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                if (CalXuJiSingle_Dialog.m_TreeTypeParam == null) {
                    CalXuJiSingle_Dialog.this.readTreeTypeParam();
                }
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
                CalXuJiSingle_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
