package  com.xzy.forestSystem.gogisapi.Tools;

import android.content.DialogInterface;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.widget.EditText;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalXuJi_Dialog {
    private XDialogTemplate _Dialog;
    private EditText etDMJ;
    private EditText etMXJ;
    private EditText etSMD;
    private ICallback m_Callback;
    private DecimalFormat m_DF_1;
    private DecimalFormat m_DF_2;
    private DecimalFormat m_DF_5;
    private SpinnerList m_ShengSpinner;
    private List<XuJiTableClass> m_XujiTables;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public CalXuJi_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_DF_2 = new DecimalFormat("#.##");
        this.m_DF_1 = new DecimalFormat("#.#");
        this.m_DF_5 = new DecimalFormat("#.#####");
        this.m_ShengSpinner = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalXuJi_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("确定")) {
                        String tmpStr = CalXuJi_Dialog.this.etMXJ.getText().toString();
                        if (tmpStr.equals("")) {
                            Common.ShowDialog("没有任何计算结果.");
                        } else if (tmpStr.equals("计算错误.")) {
                            Common.ShowDialog("计算结果错,不能返回.");
                        } else {
                            if (CalXuJi_Dialog.this.m_Callback != null) {
                                CalXuJi_Dialog.this.m_Callback.OnClick("蓄积量计算返回", tmpStr);
                            }
                            CalXuJi_Dialog.this._Dialog.dismiss();
                        }
                    } else if (command.equals("蓄积计算表格管理返回")) {
                        CalXuJi_Dialog.this.refreshList();
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_XujiTables = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.calxuji_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("蓄积量计算");
        this._Dialog.findViewById(R.id.btn_clear).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_calSMD).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_calMMXJ).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_calDMJ).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_xujiManage).setOnClickListener(new ViewClick());
        this.m_ShengSpinner = (SpinnerList) this._Dialog.findViewById(R.id.sp_shengList);
        List<String> tmpList = new ArrayList<>();
        tmpList.add("松木");
        tmpList.add("人工杉木");
        tmpList.add("天然杉木");
        tmpList.add("阔叶树");
        Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_treeType, tmpList.get(0), "请选择", tmpList, (String) null, this.pCallback);
        this.etSMD = (EditText) this._Dialog.findViewById(R.id.et_SMD);
        this.etDMJ = (EditText) this._Dialog.findViewById(R.id.etDMJ);
        this.etMXJ = (EditText) this._Dialog.findViewById(R.id.etMXJ);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        String tmpShengname = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_shengList);
        if (tmpShengname.length() > 0) {
            HashMap<String, String> tempHash01 = new HashMap<>();
            tempHash01.put("F2", tmpShengname);
            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_CalXuJi_Last_ShengName", tempHash01);
        }
        if (command.equals("清空")) {
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_PJSG, "");
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_PJXJ, "");
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_SMD, "");
        } else if (command.equals("疏密度计算")) {
            String tmpString = Common.GetEditTextValueOnID(this._Dialog, R.id.et_PJSG).trim();
            if (tmpString.length() == 0) {
                Common.ShowToast("请输入正确的平均树高.");
                return;
            }
            Integer tmpH = Integer.valueOf(Integer.parseInt(tmpString));
            String tmpString2 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_PJXJ).trim();
            if (tmpString2.length() == 0) {
                Common.ShowToast("请输入正确的平均胸径.");
                return;
            }
            Integer tmpD = Integer.valueOf(Integer.parseInt(tmpString2));
            String tmpString3 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_SMD).trim();
            if (tmpString3.length() == 0) {
                Common.ShowToast("请输入正确的疏密度.");
                return;
            }
            double tmpRatio = Double.parseDouble(tmpString3);
            String tmpTreeType = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_treeType).trim();
            if (tmpTreeType.length() == 0) {
                Common.ShowToast("请选择树种.");
                return;
            }
            String tmpZoneName = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_shengList).trim();
            if (tmpZoneName.length() == 0) {
                Common.ShowToast("请选择省份.");
                return;
            }
            XuJiTableClass tmpXuJiTableClass = XuJiTableClass.GetXuJiTable(tmpZoneName, tmpTreeType);
            if (tmpXuJiTableClass == null) {
                Common.ShowDialog("系统中没有【" + tmpZoneName + "】的蓄积量计算表【" + tmpTreeType + "】,请先在蓄积量计算表管理中导入计算表文件.");
            } else if (tmpH.intValue() < tmpXuJiTableClass.MinTreeHeight.intValue() || tmpH.intValue() > tmpXuJiTableClass.MaxTreeHeight.intValue()) {
                Common.ShowToast("树高超出范围,树高值应该在 [" + String.valueOf(tmpXuJiTableClass.MinTreeHeight) + " - " + String.valueOf(tmpXuJiTableClass.MaxTreeHeight) + "] 范围内.");
            } else {
                List<List<Integer>> tmpTreeCountList = tmpXuJiTableClass.getTreeCountList();
                if (tmpTreeCountList.size() > 0) {
                    int tmpHIndex = tmpXuJiTableClass.TreeHeights.indexOf(tmpH);
                    if (tmpHIndex > -1) {
                        Integer[] tmpXJExtend = tmpXuJiTableClass.TreeCountExtendList.get(tmpHIndex);
                        if (tmpD.intValue() < tmpXJExtend[0].intValue() || tmpD.intValue() > tmpXJExtend[1].intValue()) {
                            Common.ShowToast("胸径超出范围,胸径值应该在 [" + String.valueOf(tmpXJExtend[0]) + " - " + String.valueOf(tmpXJExtend[1]) + "] 范围内.");
                            return;
                        }
                        int tmpRadisIndex = tmpXuJiTableClass.TreeRadius.indexOf(tmpD);
                        if (tmpRadisIndex > -1) {
                            int tmpCount = tmpTreeCountList.get(tmpHIndex).get(tmpRadisIndex).intValue();
                            Common.SetEditTextValueOnID(this._Dialog, R.id.etMZS, String.valueOf((int) (((double) tmpCount) * tmpRatio)));
                            Common.SetEditTextValueOnID(this._Dialog, R.id.etMXJ, this.m_DF_2.format(tmpXuJiTableClass.TreeVolumns.get(tmpHIndex).doubleValue() * tmpRatio));
                            Common.SetEditTextValueOnID(this._Dialog, R.id.etDMJ, this.m_DF_5.format(((((double) tmpCount) * (((3.141592653589793d * ((double) tmpD.intValue())) * ((double) tmpD.intValue())) / 40000.0d)) / 666.6667d) * tmpRatio));
                            this.etSMD.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                            this.etDMJ.setTextColor(SupportMenu.CATEGORY_MASK);
                            this.etMXJ.setTextColor(SupportMenu.CATEGORY_MASK);
                            return;
                        }
                        Common.ShowDialog("系统中【" + tmpZoneName + "】的蓄积量计算表【" + tmpTreeType + "】中没有该胸径值【" + String.valueOf(tmpD) + "】对应的数据.");
                        return;
                    }
                    Common.ShowDialog("系统中【" + tmpZoneName + "】的蓄积量计算表【" + tmpTreeType + "】中没有该树高值【" + String.valueOf(tmpD) + "】对应的数据.");
                    return;
                }
                Common.ShowDialog("系统中【" + tmpZoneName + "】的蓄积量计算表【" + tmpTreeType + "】没有数据,请先在蓄积量计算表管理中导入计算表文件.");
            }
        } else if (command.equals("断面积计算")) {
            String tmpString4 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_PJSG).trim();
            if (tmpString4.length() == 0) {
                Common.ShowToast("请输入正确的平均树高.");
                return;
            }
            Integer tmpH2 = Integer.valueOf(Integer.parseInt(tmpString4));
            String tmpString5 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_PJXJ).trim();
            if (tmpString5.length() == 0) {
                Common.ShowToast("请输入正确的平均胸径.");
                return;
            }
            Integer tmpD2 = Integer.valueOf(Integer.parseInt(tmpString5));
            String tmpString6 = this.etDMJ.getEditableText().toString();
            if (tmpString6.length() == 0) {
                Common.ShowToast("请输入正确的断面面积(单位亩).");
                return;
            }
            double tmpTotalArea = Double.parseDouble(tmpString6);
            String tmpTreeType2 = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_treeType).trim();
            if (tmpTreeType2.length() == 0) {
                Common.ShowToast("请选择树种.");
                return;
            }
            String tmpZoneName2 = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_shengList).trim();
            if (tmpZoneName2.length() == 0) {
                Common.ShowToast("请选择省份.");
                return;
            }
            XuJiTableClass tmpXuJiTableClass2 = XuJiTableClass.GetXuJiTable(tmpZoneName2, tmpTreeType2);
            if (tmpXuJiTableClass2 == null) {
                Common.ShowDialog("系统中没有【" + tmpZoneName2 + "】的蓄积量计算表【" + tmpTreeType2 + "】,请先在蓄积量计算表管理中导入计算表文件.");
            } else if (tmpH2.intValue() < tmpXuJiTableClass2.MinTreeHeight.intValue() || tmpH2.intValue() > tmpXuJiTableClass2.MaxTreeHeight.intValue()) {
                Common.ShowToast("树高超出范围,树高值应该在 [" + String.valueOf(tmpXuJiTableClass2.MinTreeHeight) + " - " + String.valueOf(tmpXuJiTableClass2.MaxTreeHeight) + "] 范围内.");
            } else {
                List<List<Integer>> tmpTreeCountList2 = tmpXuJiTableClass2.getTreeCountList();
                if (tmpTreeCountList2.size() > 0) {
                    int tmpHIndex2 = tmpXuJiTableClass2.TreeHeights.indexOf(tmpH2);
                    if (tmpHIndex2 > -1) {
                        Integer[] tmpXJExtend2 = tmpXuJiTableClass2.TreeCountExtendList.get(tmpHIndex2);
                        if (tmpD2.intValue() < tmpXJExtend2[0].intValue() || tmpD2.intValue() > tmpXJExtend2[1].intValue()) {
                            Common.ShowToast("胸径超出范围,胸径值应该在 [" + String.valueOf(tmpXJExtend2[0]) + " - " + String.valueOf(tmpXJExtend2[1]) + "] 范围内.");
                            return;
                        }
                        int tmpRadisIndex2 = tmpXuJiTableClass2.TreeRadius.indexOf(tmpD2);
                        if (tmpRadisIndex2 > -1) {
                            int tmpTreeCount_Normal = tmpTreeCountList2.get(tmpHIndex2).get(tmpRadisIndex2).intValue();
                            int tmpTreeCount = (int) ((666.66667d * tmpTotalArea) / (((3.141592653589793d * ((double) tmpD2.intValue())) * ((double) tmpD2.intValue())) / 40000.0d));
                            Common.SetEditTextValueOnID(this._Dialog, R.id.etMZS, String.valueOf(tmpTreeCount));
                            double tmpRatio2 = ((double) tmpTreeCount) / ((double) tmpTreeCount_Normal);
                            Common.SetEditTextValueOnID(this._Dialog, R.id.et_SMD, this.m_DF_1.format(tmpRatio2));
                            Common.SetEditTextValueOnID(this._Dialog, R.id.etMXJ, this.m_DF_2.format(tmpXuJiTableClass2.TreeVolumns.get(tmpHIndex2).doubleValue() * tmpRatio2));
                            this.etSMD.setTextColor(SupportMenu.CATEGORY_MASK);
                            this.etDMJ.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                            this.etMXJ.setTextColor(SupportMenu.CATEGORY_MASK);
                            return;
                        }
                        Common.ShowDialog("系统中【" + tmpZoneName2 + "】的蓄积量计算表【" + tmpTreeType2 + "】中没有该胸径值【" + String.valueOf(tmpD2) + "】对应的数据.");
                        return;
                    }
                    Common.ShowDialog("系统中【" + tmpZoneName2 + "】的蓄积量计算表【" + tmpTreeType2 + "】中没有该树高值【" + String.valueOf(tmpD2) + "】对应的数据.");
                    return;
                }
                Common.ShowDialog("系统中【" + tmpZoneName2 + "】的蓄积量计算表【" + tmpTreeType2 + "】没有数据,请先在蓄积量计算表管理中导入计算表文件.");
            }
        } else if (command.equals("每亩蓄积计算")) {
            String tmpString7 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_PJSG).trim();
            if (tmpString7.length() == 0) {
                Common.ShowToast("请输入正确的平均树高.");
                return;
            }
            Integer tmpH3 = Integer.valueOf(Integer.parseInt(tmpString7));
            String tmpString8 = Common.GetEditTextValueOnID(this._Dialog, R.id.et_PJXJ).trim();
            if (tmpString8.length() == 0) {
                Common.ShowToast("请输入正确的平均胸径.");
                return;
            }
            Integer tmpD3 = Integer.valueOf(Integer.parseInt(tmpString8));
            String tmpString9 = Common.GetEditTextValueOnID(this._Dialog, R.id.etMXJ).trim();
            if (tmpString9.length() == 0) {
                Common.ShowToast("请输入正确的每亩蓄积.");
                return;
            }
            double tmpMXJ = Double.parseDouble(tmpString9);
            String tmpTreeType3 = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_treeType).trim();
            if (tmpTreeType3.length() == 0) {
                Common.ShowToast("请选择树种.");
                return;
            }
            String tmpZoneName3 = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_shengList).trim();
            if (tmpZoneName3.length() == 0) {
                Common.ShowToast("请选择省份.");
                return;
            }
            XuJiTableClass tmpXuJiTableClass3 = XuJiTableClass.GetXuJiTable(tmpZoneName3, tmpTreeType3);
            if (tmpXuJiTableClass3 == null) {
                Common.ShowDialog("系统中没有【" + tmpZoneName3 + "】的蓄积量计算表【" + tmpTreeType3 + "】,请先在蓄积量计算表管理中导入计算表文件.");
            } else if (tmpH3.intValue() < tmpXuJiTableClass3.MinTreeHeight.intValue() || tmpH3.intValue() > tmpXuJiTableClass3.MaxTreeHeight.intValue()) {
                Common.ShowToast("树高超出范围,树高值应该在 [" + String.valueOf(tmpXuJiTableClass3.MinTreeHeight) + " - " + String.valueOf(tmpXuJiTableClass3.MaxTreeHeight) + "] 范围内.");
            } else {
                List<List<Integer>> tmpTreeCountList3 = tmpXuJiTableClass3.getTreeCountList();
                if (tmpTreeCountList3.size() > 0) {
                    int tmpHIndex3 = tmpXuJiTableClass3.TreeHeights.indexOf(tmpH3);
                    if (tmpHIndex3 > -1) {
                        Integer[] tmpXJExtend3 = tmpXuJiTableClass3.TreeCountExtendList.get(tmpHIndex3);
                        if (tmpD3.intValue() < tmpXJExtend3[0].intValue() || tmpD3.intValue() > tmpXJExtend3[1].intValue()) {
                            Common.ShowToast("胸径超出范围,胸径值应该在 [" + String.valueOf(tmpXJExtend3[0]) + " - " + String.valueOf(tmpXJExtend3[1]) + "] 范围内.");
                            return;
                        }
                        int tmpRadisIndex3 = tmpXuJiTableClass3.TreeRadius.indexOf(tmpD3);
                        if (tmpRadisIndex3 > -1) {
                            int tmpTreeCount_Normal2 = tmpTreeCountList3.get(tmpHIndex3).get(tmpRadisIndex3).intValue();
                            double tmpRatio3 = tmpMXJ / tmpXuJiTableClass3.TreeVolumns.get(tmpHIndex3).doubleValue();
                            Common.SetEditTextValueOnID(this._Dialog, R.id.et_SMD, this.m_DF_1.format(tmpRatio3));
                            Common.SetEditTextValueOnID(this._Dialog, R.id.etMZS, String.valueOf((int) (((double) tmpTreeCount_Normal2) * tmpRatio3)));
                            Common.SetEditTextValueOnID(this._Dialog, R.id.etDMJ, this.m_DF_5.format(((((double) tmpTreeCount_Normal2) * (((3.141592653589793d * ((double) tmpD3.intValue())) * ((double) tmpD3.intValue())) / 40000.0d)) / 666.6667d) * tmpRatio3));
                            this.etSMD.setTextColor(SupportMenu.CATEGORY_MASK);
                            this.etDMJ.setTextColor(SupportMenu.CATEGORY_MASK);
                            this.etMXJ.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                            return;
                        }
                        Common.ShowDialog("系统中【" + tmpZoneName3 + "】的蓄积量计算表【" + tmpTreeType3 + "】中没有该胸径值【" + String.valueOf(tmpD3) + "】对应的数据.");
                        return;
                    }
                    Common.ShowDialog("系统中【" + tmpZoneName3 + "】的蓄积量计算表【" + tmpTreeType3 + "】中没有该树高值【" + String.valueOf(tmpD3) + "】对应的数据.");
                    return;
                }
                Common.ShowDialog("系统中【" + tmpZoneName3 + "】的蓄积量计算表【" + tmpTreeType3 + "】没有数据,请先在蓄积量计算表管理中导入计算表文件.");
            }
        } else if (command.equals("蓄积计算设置")) {
            XuJiTable_Manage_Dialog tmpDialog = new XuJiTable_Manage_Dialog();
            tmpDialog.SetCallback(this.pCallback);
            tmpDialog.ShowDialog();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshList() {
        String tempStr;
        try {
            this.m_XujiTables = new ArrayList();
            this.m_XujiTables = XuJiTableClass.GetXuJiTablesInfo();
            if (this.m_XujiTables.size() > 0) {
                List<String> tmpList = new ArrayList<>();
                for (XuJiTableClass tmpXuJiTableClass : this.m_XujiTables) {
                    String tmpShengName = tmpXuJiTableClass.ZoneName;
                    if (!tmpList.contains(tmpShengName)) {
                        tmpList.add(tmpShengName);
                    }
                }
                if (tmpList.size() > 0) {
                    String tmpDefaultName = tmpList.get(0);
                    HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_CalXuJi_Last_ShengName");
                    if (!(tempHashMap == null || (tempStr = tempHashMap.get("F2")) == null || tempStr.equals(""))) {
                        tmpDefaultName = tempStr;
                    }
                    Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_shengList, tmpDefaultName, "请选择", tmpList, (String) null, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalXuJi_Dialog.2
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command, Object pObject) {
                            CalXuJi_Dialog.this.refreshTableNameList();
                        }
                    });
                }
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshTableNameList() {
        try {
            String tmpzonenameString = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_shengList);
            if (tmpzonenameString.length() > 0) {
                List<String> tmpList = new ArrayList<>();
                for (XuJiTableClass tmpXuJiTableClass : this.m_XujiTables) {
                    if (tmpzonenameString.equals(tmpXuJiTableClass.ZoneName)) {
                        tmpList.add(tmpXuJiTableClass.Name);
                    }
                }
                Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_treeType, tmpList.get(0), "请选择", tmpList, (String) null, (ICallback) null);
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.CalXuJi_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                CalXuJi_Dialog.this.refreshList();
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
                CalXuJi_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
