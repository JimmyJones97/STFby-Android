package com.xzy.forestSystem.GuiZhou.DiaoCha;

import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.Date;

public class SanShengMu_Dialog {
    private XDialogTemplate _Dialog = null;
    private ICallback m_Callback = null;
    private String m_DefaultXiaoBanFullName = "";
    private SQLiteDBHelper m_SQLiteDBHelper = null;
    private ICallback pCallback = new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.SanShengMu_Dialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String command, Object object) {
            try {
                if (command.equals("保存")) {
                    try {
                        String[] tmpDataStrings = new String[12];
                        String tmpString = Common.GetViewValue(SanShengMu_Dialog.this._Dialog, R.id.et_BasicInput01).trim();
                        if (tmpString.length() == 0) {
                            Common.ShowDialog("请选择县局.");
                            return;
                        }
                        tmpDataStrings[1] = tmpString;
                        String tmpTotalNameString = String.valueOf("") + "_" + tmpString;
                        String tmpString2 = Common.GetViewValue(SanShengMu_Dialog.this._Dialog, R.id.et_BasicInput02).trim();
                        if (tmpString2.length() == 0) {
                            Common.ShowDialog("请选择乡镇.");
                            return;
                        }
                        tmpDataStrings[2] = tmpString2;
                        String tmpTotalNameString2 = String.valueOf(tmpTotalNameString) + "_" + tmpString2;
                        String tmpString3 = Common.GetViewValue(SanShengMu_Dialog.this._Dialog, R.id.et_BasicInput03).trim();
                        if (tmpString3.length() == 0) {
                            Common.ShowDialog("请选择村.");
                            return;
                        }
                        tmpDataStrings[3] = tmpString3;
                        String tmpTotalNameString3 = String.valueOf(tmpTotalNameString2) + "_" + tmpString3;
                        String tmpString4 = Common.GetViewValue(SanShengMu_Dialog.this._Dialog, R.id.et_BasicInput04).trim();
                        if (tmpString4.length() == 0) {
                            Common.ShowDialog("请选择小班.");
                            return;
                        }
                        tmpDataStrings[4] = tmpString4;
                        tmpDataStrings[0] = String.valueOf(tmpTotalNameString3) + "_" + tmpString4;
                        int tmpTid = 0;
                        for (int tmpViewID : new int[]{R.id.et_Input01, R.id.et_Input02, R.id.et_Input03, R.id.et_Input04, R.id.et_Input05}) {
                            View tmpView = SanShengMu_Dialog.this._Dialog.findViewById(tmpViewID);
                            tmpTid++;
                            if (tmpView != null) {
                                tmpDataStrings[tmpTid + 4] = Common.GetViewValue(tmpView).trim();
                            }
                        }
                        tmpDataStrings[10] = CommonSetting.DiaoChaDateFormat.format(new Date());
                        String tmpShuZhong = Common.GetEditTextValueOnID(SanShengMu_Dialog.this._Dialog, R.id.et_Input01).trim();
                        String tmpShuZhongZuString = "";
                        if (tmpShuZhong.length() > 0) {
                            tmpShuZhongZuString = CommonSetting.ConvertShuZhongToZu(tmpShuZhong);
                        }
                        tmpDataStrings[11] = tmpShuZhongZuString;
//                        if (SanShengMu_Dialog.this.m_SQLiteDBHelper.ExecuteSQL(String.format("Replace Into T_SanShengMuInfo (YangDiName,Xian,Xiang,Cun,XiaoBan,ShuZhong,XiongJing,ShuGao,ZhuShu,XuJi,DiaoChaTime,ShuZhongZu) Values ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s','%8$s','%9$s','%10$s','%11$s','%12$s')", tmpDataStrings))) {
//                            C0321Common.ShowToast("保存散生木调查数据成功!");
//                            SanShengMu_Dialog.this._Dialog.dismiss();
//                            return;
//                        }
                        Common.ShowDialog("保存样地信息失败.");
                    } catch (Exception e) {
                        Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
                    }
                } else if (command.equals("选择树种返回")) {
                    Common.SetEditTextValueOnID(SanShengMu_Dialog.this._Dialog, R.id.et_Input01, String.valueOf(object));
                }
            } catch (Exception e2) {
                Common.ShowDialog("错误:\r\n" + e2.getLocalizedMessage());
            }
        }
    };

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public SanShengMu_Dialog(String xiaobanFullName) {
        String[] tmpStrs;
        this.m_DefaultXiaoBanFullName = xiaobanFullName;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.sanshengmu_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("散生木调查");
        this._Dialog.SetHeadButtons("1,2130837858,保存,保存", this.pCallback);
        this.m_SQLiteDBHelper = CommonSetting.GetSQLiteDBHelper();
        boolean tmpBool = false;
        SQLiteReader tmpLiteReader = this.m_SQLiteDBHelper.Query("Select YangDiIndex,Xian,Xiang,Cun,XiaoBan,ShuZhong,XiongJing,ShuGao,ZhuShu,XuJi From T_SanShengMuInfo Where YangDiName='" + this.m_DefaultXiaoBanFullName + "'");
        if (tmpLiteReader != null && tmpLiteReader.Read()) {
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_BasicInput01, tmpLiteReader.GetString(1));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_BasicInput02, tmpLiteReader.GetString(2));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_BasicInput03, tmpLiteReader.GetString(3));
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_BasicInput04, tmpLiteReader.GetString(4));
            int tmpTid = 4;
            for (int tmpViewID : new int[]{R.id.et_Input01, R.id.et_Input02, R.id.et_Input03, R.id.et_Input04, R.id.et_Input05}) {
                tmpTid++;
                View tmpView = this._Dialog.findViewById(tmpViewID);
                if (tmpView != null) {
                    Common.SetValueToView(tmpLiteReader.GetString(tmpTid), tmpView);
                }
            }
            tmpBool = true;
            this._Dialog.findViewById(R.id.et_BasicInput01).setEnabled(false);
            this._Dialog.findViewById(R.id.et_BasicInput02).setEnabled(false);
            this._Dialog.findViewById(R.id.et_BasicInput03).setEnabled(false);
            this._Dialog.findViewById(R.id.et_BasicInput04).setEnabled(false);
        }
        if (!tmpBool && (tmpStrs = this.m_DefaultXiaoBanFullName.split("_")) != null && tmpStrs.length > 1) {
            int tmpLen = tmpStrs.length;
            if (tmpLen > 1) {
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_BasicInput01, tmpStrs[1]);
            }
            if (tmpLen > 2) {
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_BasicInput02, tmpStrs[2]);
            }
            if (tmpLen > 3) {
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_BasicInput03, tmpStrs[3]);
            }
            if (tmpLen > 4) {
                Common.SetEditTextValueOnID(this._Dialog, R.id.et_BasicInput04, tmpStrs[4]);
            }
        }
        this._Dialog.findViewById(R.id.btn_CalXUJI).setOnClickListener(new View.OnClickListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.SanShengMu_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                String tmpShuZhong = Common.GetEditTextValueOnID(SanShengMu_Dialog.this._Dialog, R.id.et_Input01).trim();
                if (tmpShuZhong.length() == 0) {
                    Common.ShowDialog("请选择或输入树种.");
                    return;
                }
                String tmpXJString = Common.GetEditTextValueOnID(SanShengMu_Dialog.this._Dialog, R.id.et_Input02).trim();
                if (tmpXJString.length() == 0) {
                    Common.ShowDialog("请输入平均胸径.");
                    return;
                }
                String tmpSGString = Common.GetEditTextValueOnID(SanShengMu_Dialog.this._Dialog, R.id.et_Input03).trim();
                if (tmpXJString.length() == 0) {
                    Common.ShowDialog("请输入平均高.");
                    return;
                }
                String tmpZSString = Common.GetEditTextValueOnID(SanShengMu_Dialog.this._Dialog, R.id.et_Input04).trim();
                if (tmpXJString.length() == 0) {
                    Common.ShowDialog("请输入株树.");
                    return;
                }
                double tmpD01 = 0.0d;
                double tmpD02 = 0.0d;
                double tmpD03 = 0.0d;
                try {
                    tmpD01 = Double.parseDouble(tmpXJString);
                    tmpD02 = Double.parseDouble(tmpSGString);
                    tmpD03 = Double.parseDouble(tmpZSString);
                } catch (Exception e) {
                }
                if (tmpD01 <= 0.0d || tmpD02 <= 0.0d || tmpD03 <= 0.0d) {
                    Common.ShowDialog("平均胸径、平均高与株树不能小于等于0.");
                    return;
                }
                Common.SetEditTextValueOnID(SanShengMu_Dialog.this._Dialog, R.id.et_Input05, CommonSetting.XUJIFormat.format(CommonSetting.CalXuJiValue(tmpShuZhong, tmpD01, tmpD02) * tmpD03));
                Common.ShowToast("计算散生木蓄积完成!");
            }
        });
        this._Dialog.findViewById(R.id.btn_SelectShuZhong).setOnClickListener(new View.OnClickListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.SanShengMu_Dialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                String tmpShuZhong = Common.GetViewValue(SanShengMu_Dialog.this._Dialog, R.id.et_Input01);
                ShuZhongSelect_Dialog tmpDialog = new ShuZhongSelect_Dialog();
                tmpDialog.SetSelectName(tmpShuZhong);
                tmpDialog.SetCallback(SanShengMu_Dialog.this.pCallback);
                tmpDialog.ShowDialog();
            }
        });
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.SanShengMu_Dialog.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
            }
        });
        this._Dialog.show();
    }
}
