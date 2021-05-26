package  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha;

import android.content.DialogInterface;
import androidx.core.view.ViewCompat;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalTubanYangdiCaiJi_Dialog {
    private XDialogTemplate _Dialog = null;
    private ICallback m_Callback = null;
    DecimalFormat m_DecimalFormat = new DecimalFormat("0.0000");
    private List<HashMap<String, Object>> m_MyTableDataList = new ArrayList();
    private MyTableFactory m_MyTableFactory = new MyTableFactory();
    public String m_ReturnTag = "";
    private double m_TuBanArea = 1.0d;
    private String m_TuBanName = "";
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.CalTubanYangdiCaiJi_Dialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String command, Object object) {
            try {
                if (command.equals("列表选项")) {
                    if (object != null && (object instanceof HashMap)) {
                        HashMap<String, Object> tmpHashMap = (HashMap) object;
                        if (tmpHashMap.containsKey("yangdiname")) {
                            final String tmpYangdiNameString = String.valueOf(tmpHashMap.get("yangdiname"));
                            final String tmpYangdiMsg = String.valueOf(tmpHashMap.get("msg"));
                            Common.ShowYesNoDialog(CalTubanYangdiCaiJi_Dialog.this._Dialog.getContext(), "是否编辑该样地调查数据?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.CalTubanYangdiCaiJi_Dialog.1.1
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String command2, Object pObject) {
                                    if (command2.equals("YES")) {
                                        CalTubanYangdiCaiJi_Dialog.this.openYangDi2Dialog(tmpYangdiNameString, tmpYangdiMsg);
                                    }
                                }
                            });
                        }
                    }
                } else if (command.equals("计算")) {
                    if (CalTubanYangdiCaiJi_Dialog.this.m_MyTableDataList.size() == 0) {
                        Common.ShowDialog("该图斑中没有对应的样地数据,无法计算.");
                    } else if (CommonSetting.getGenJingCaiJiTable(true).size() == 0) {
                        Common.ShowDialog("系统中没有“根径材积表”数据,请在“设置”中点击“根径材积设置”按钮.");
                    } else {
                        List<String> tmpYDNameList = new ArrayList<>();
                        for (HashMap<String, Object> tmpHash : CalTubanYangdiCaiJi_Dialog.this.m_MyTableDataList) {
                            if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                                tmpYDNameList.add(String.valueOf(tmpHash.get("yangdiname")));
                            }
                        }
                        if (tmpYDNameList.size() > 0) {
                            String tmpSqlString = "Select GenJing, SUM(SHAN), SUM(MA), SUM(KUO) From T_YangDiData Where YangDiName IN ('" + Common.CombineStrings("','", tmpYDNameList) + "') Group By GenJing";
                            SQLiteDBHelper tmpSqLiteDBHelper = CommonSetting.GetSenLinDuChaSQLiteDBHelper();
                            if (tmpSqLiteDBHelper != null) {
                                HashMap<String, Double[]> tmphasHashMap = new HashMap<>();
                                SQLiteReader localSQLiteDataReader = tmpSqLiteDBHelper.Query(tmpSqlString);
                                if (localSQLiteDataReader != null) {
                                    while (localSQLiteDataReader.Read()) {
                                        tmphasHashMap.put(String.valueOf(localSQLiteDataReader.GetInt32(0)), new Double[]{Double.valueOf((double) localSQLiteDataReader.GetInt32(1)), Double.valueOf((double) localSQLiteDataReader.GetInt32(2)), Double.valueOf((double) localSQLiteDataReader.GetInt32(3))});
                                    }
                                    localSQLiteDataReader.Close();
                                    StringBuilder tmpStringBuilder = new StringBuilder();
                                    double tmpYangdiTotalArea = CommonSetting.m_YangDiArea * ((double) tmpYDNameList.size());
                                    double[] tmpDs = CommonSetting.CalCaiJi(tmphasHashMap);
                                    tmpStringBuilder.append("图斑名称：" + CalTubanYangdiCaiJi_Dialog.this.m_TuBanName + "\r\n\r\n");
                                    tmpStringBuilder.append("样地材积：\r\n");
                                    tmpStringBuilder.append("  【杉    木】：" + CalTubanYangdiCaiJi_Dialog.this.m_DecimalFormat.format(tmpDs[0]) + " m3\r\n");
                                    tmpStringBuilder.append("  【马尾松】：" + CalTubanYangdiCaiJi_Dialog.this.m_DecimalFormat.format(tmpDs[1]) + " m3\r\n");
                                    tmpStringBuilder.append("  【阔叶树】：" + CalTubanYangdiCaiJi_Dialog.this.m_DecimalFormat.format(tmpDs[2]) + " m3\r\n");
                                    tmpStringBuilder.append("合计：" + CalTubanYangdiCaiJi_Dialog.this.m_DecimalFormat.format(tmpDs[0] + tmpDs[1] + tmpDs[2]) + "\r\n");
                                    tmpStringBuilder.append("\r\n公顷材积：\r\n");
                                    tmpStringBuilder.append("  【杉    木】：" + CalTubanYangdiCaiJi_Dialog.this.m_DecimalFormat.format(tmpDs[0] / tmpYangdiTotalArea) + " m3\r\n");
                                    tmpStringBuilder.append("  【马尾松】：" + CalTubanYangdiCaiJi_Dialog.this.m_DecimalFormat.format(tmpDs[1] / tmpYangdiTotalArea) + " m3\r\n");
                                    tmpStringBuilder.append("  【阔叶树】：" + CalTubanYangdiCaiJi_Dialog.this.m_DecimalFormat.format(tmpDs[2] / tmpYangdiTotalArea) + " m3\r\n");
                                    tmpStringBuilder.append("合计：" + CalTubanYangdiCaiJi_Dialog.this.m_DecimalFormat.format(((tmpDs[0] + tmpDs[1]) + tmpDs[2]) / tmpYangdiTotalArea) + " m3\r\n");
                                    tmpStringBuilder.append("\r\n图斑材积：\r\n");
                                    tmpStringBuilder.append("图斑面积：" + CalTubanYangdiCaiJi_Dialog.this.m_DecimalFormat.format(CalTubanYangdiCaiJi_Dialog.this.m_TuBanArea) + "公顷\r\n");
                                    tmpStringBuilder.append("  【杉    木】：" + CalTubanYangdiCaiJi_Dialog.this.m_DecimalFormat.format((tmpDs[0] * CalTubanYangdiCaiJi_Dialog.this.m_TuBanArea) / tmpYangdiTotalArea) + " m3\r\n");
                                    tmpStringBuilder.append("  【马尾松】：" + CalTubanYangdiCaiJi_Dialog.this.m_DecimalFormat.format((tmpDs[1] * CalTubanYangdiCaiJi_Dialog.this.m_TuBanArea) / tmpYangdiTotalArea) + " m3\r\n");
                                    tmpStringBuilder.append("  【阔叶树】：" + CalTubanYangdiCaiJi_Dialog.this.m_DecimalFormat.format((tmpDs[2] * CalTubanYangdiCaiJi_Dialog.this.m_TuBanArea) / tmpYangdiTotalArea) + " m3\r\n");
                                    final double tmpResultValue = (((tmpDs[0] + tmpDs[1]) + tmpDs[2]) * CalTubanYangdiCaiJi_Dialog.this.m_TuBanArea) / tmpYangdiTotalArea;
                                    tmpStringBuilder.append("合计：" + CalTubanYangdiCaiJi_Dialog.this.m_DecimalFormat.format(tmpResultValue) + " m3\r\n");
                                    tmpStringBuilder.append("\r\n是否使用该计算结果?\r\n");
                                    Common.ShowYesNoDialog(CalTubanYangdiCaiJi_Dialog.this._Dialog.getContext(), tmpStringBuilder.toString(), ViewCompat.MEASURED_STATE_MASK, "计算结果", "确定", "返回", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.CalTubanYangdiCaiJi_Dialog.1.2
                                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                        public void OnClick(String command2, Object pObject) {
                                            if (command2.equals("YES")) {
                                                if (CalTubanYangdiCaiJi_Dialog.this.m_Callback != null) {
                                                    CalTubanYangdiCaiJi_Dialog.this.m_Callback.OnClick("计算图斑蓄积返回", new Object[]{CalTubanYangdiCaiJi_Dialog.this.m_ReturnTag, Double.valueOf(tmpResultValue)});
                                                }
                                                CalTubanYangdiCaiJi_Dialog.this._Dialog.dismiss();
                                            }
                                        }
                                    });
                                    return;
                                }
                                return;
                            }
                            Common.ShowDialog("样地数据库不存在,无法计算.请现在设置中建立图斑检查数据库.");
                            return;
                        }
                        Common.ShowDialog("请勾选需要参与计算的样地.");
                    }
                } else if (command.equals("样地调查数据编辑返回")) {
                    CalTubanYangdiCaiJi_Dialog.this.refreshTable();
                }
            } catch (Exception e) {
            }
        }
    };

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public CalTubanYangdiCaiJi_Dialog(String tubanName, double tubanArea) {
        this.m_TuBanName = tubanName;
        this.m_TuBanArea = tubanArea;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.caltubanyangdicaiji_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("计算图斑蓄积");
        this._Dialog.SetHeadButtons("1,2130837858,计算,计算", this.pCallback);
        this._Dialog.findViewById(R.id.btn_DeleteYangDi).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_AddYangDi).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectAll).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectDe).setOnClickListener(new ViewClick());
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_yangdi_layerlist), "自定义", "选择,样地名称,样地序号", "checkbox,text,text", new int[]{-15, -60, -25}, this.pCallback);
        this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshTable() {
        try {
            if (this.m_TuBanName.length() > 0) {
                this.m_MyTableDataList.clear();
                SQLiteDBHelper tmpSQLiteDBHelper = CommonSetting.GetSenLinDuChaSQLiteDBHelper();
                if (tmpSQLiteDBHelper != null) {
                    SQLiteReader tmpLiteReader = tmpSQLiteDBHelper.Query("Select YangDiName,YangDiIndex,X,Y From T_YangDiInfo Where YangDiName like '" + this.m_TuBanName + "%'");
                    while (tmpLiteReader.Read()) {
                        String tmpYDName = tmpLiteReader.GetString(0);
                        String tmpYDIndex = tmpLiteReader.GetString(1);
                        HashMap<String, Object> tmpHash = new HashMap<>();
                        tmpHash.put("D1", true);
                        String tmpYDName2 = tmpYDName.replace("_", " ");
                        if (tmpYDName2.endsWith(tmpYDIndex)) {
                            tmpYDName2 = tmpYDName2.substring(0, tmpYDName2.length() - tmpYDIndex.length());
                        }
                        tmpHash.put("D2", tmpYDName2);
                        tmpHash.put("D3", tmpYDIndex);
                        tmpHash.put("yangdiname", tmpYDName);
                        tmpHash.put("msg", "样地位置:" + tmpYDName2 + "\r\n样地编号:" + tmpYDIndex);
                        this.m_MyTableDataList.add(tmpHash);
                    }
                    this.m_MyTableFactory.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("添加样地")) {
                YangDi_Dialog tmpDialog = new YangDi_Dialog();
                tmpDialog.SetCallback(this.pCallback);
                tmpDialog.SetTubanName(this.m_TuBanName);
                tmpDialog.ShowDialog();
            } else if (command.equals("全选")) {
                for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
                    tmpHash.put("D1", true);
                }
                this.m_MyTableFactory.notifyDataSetChanged();
            } else if (command.equals("反选")) {
                for (HashMap<String, Object> tmpHash2 : this.m_MyTableDataList) {
                    tmpHash2.put("D1", Boolean.valueOf(!Boolean.parseBoolean(String.valueOf(tmpHash2.get("D1")))));
                }
                this.m_MyTableFactory.notifyDataSetChanged();
            } else if (command.equals("删除样地")) {
                final List<String> tmpList = new ArrayList<>();
                List<Integer> tmpList2 = new ArrayList<>();
                int tmpTid = -1;
                for (HashMap<String, Object> tmpHash3 : this.m_MyTableDataList) {
                    tmpTid++;
                    if (Boolean.parseBoolean(String.valueOf(tmpHash3.get("D1")))) {
                        tmpList.add(String.valueOf(tmpHash3.get("yangdiname")));
                        tmpList2.add(Integer.valueOf(tmpTid));
                    }
                }
                if (tmpList.size() > 0) {
                    Common.ShowYesNoDialogWithAlert(this._Dialog.getContext(), "是否删除选择的" + String.valueOf(tmpList.size()) + "个样地调查数据?\r\n提示:删除后将无法恢复,请谨慎操作.", true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.CalTubanYangdiCaiJi_Dialog.2
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (paramString.equals("YES")) {
                                CommonSetting.DeleteYangdiData(tmpList);
                                CalTubanYangdiCaiJi_Dialog.this.refreshTable();
                            }
                        }
                    });
                } else {
                    Common.ShowDialog("没有选择需要删除的样地调查数据.");
                }
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void openYangDi2Dialog(String yangdiName, String yangdiMsg) {
        YangDi2_Dialog tmpDialog = new YangDi2_Dialog();
        tmpDialog.setYangDiName(yangdiName);
        tmpDialog.setYangDiInfo(yangdiMsg);
        tmpDialog.SetCallback(this.pCallback);
        tmpDialog.ShowDialog();
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.CalTubanYangdiCaiJi_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                CalTubanYangdiCaiJi_Dialog.this.refreshTable();
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
                CalTubanYangdiCaiJi_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
