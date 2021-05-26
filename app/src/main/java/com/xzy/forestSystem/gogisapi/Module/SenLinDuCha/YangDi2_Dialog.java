package  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.InputXY_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Others.MediaManag_Dialog;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YangDi2_Dialog {
    private XDialogTemplate _Dialog;
    private LinearLayout.LayoutParams layoutParamsRoot;
    private ICallback m_Callback;
    private String m_CoordinateX;
    private String m_CoordinateY;
    DecimalFormat m_DecimalFormat;
    private List<Integer> m_GenJingList;
    private LinearLayout m_MainLinearLayout;
    private Button m_MediaButton;
    private String m_MediaInfoString;
    private boolean m_NeedSave;
    private SQLiteDBHelper m_SQLiteDBHelper;
    private String m_YangDiInfo;
    private String m_YangDiName;
    private HashMap<String, EditText[]> m_m_InputEditList;
    private ICallback pCallback;
    private LinearLayout.LayoutParams tmpImgViewLayoutParams;
    private LinearLayout.LayoutParams tmpTxtLP01;
    private LinearLayout.LayoutParams tmpTxtLP0101;
    private LinearLayout.LayoutParams tmpTxtLP02;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public YangDi2_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_SQLiteDBHelper = null;
        this.m_MainLinearLayout = null;
        this.m_YangDiName = "";
        this.m_YangDiInfo = "";
        this.m_MediaInfoString = "";
        this.m_CoordinateX = "0.0";
        this.m_CoordinateY = "0.0";
        this.m_GenJingList = new ArrayList();
        this.m_m_InputEditList = new HashMap<>();
        this.m_MediaButton = null;
        this.m_NeedSave = false;
        this.m_DecimalFormat = new DecimalFormat("0.0000");
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDi2_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("确定")) {
                    if (YangDi2_Dialog.this.saveData()) {
                        if (YangDi2_Dialog.this.m_Callback != null) {
                            YangDi2_Dialog.this.m_Callback.OnClick("样地调查数据编辑返回", YangDi2_Dialog.this.m_YangDiName);
                        }
                        Common.ShowToast("保存样地调查数据成功!");
                        YangDi2_Dialog.this._Dialog.dismiss();
                    }
                } else if (command.equals("返回")) {
                    if (YangDi2_Dialog.this.m_NeedSave) {
                        Common.ShowYesNoDialogWithAlert(YangDi2_Dialog.this._Dialog.getContext(), "样地调查数据已经修改过.\r\n是否确定退出?", true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDi2_Dialog.1.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String command2, Object pObject) {
                                if (command2.equals("YES")) {
                                    YangDi2_Dialog.this._Dialog.dismiss();
                                }
                            }
                        });
                    } else {
                        YangDi2_Dialog.this._Dialog.dismiss();
                    }
                } else if (command.equals("多媒体返回")) {
                    if (object != null) {
                        String tmpResult = "";
                        String[] tmpStrs = String.valueOf(object).split(",");
                        int tmpI = 0;
                        for (String tmpStr01 : tmpStrs) {
                            if (tmpStr01.trim().length() > 0) {
                                if (tmpResult.length() > 0) {
                                    tmpResult = String.valueOf(tmpResult) + ",";
                                }
                                tmpResult = String.valueOf(tmpResult) + tmpStr01.trim();
                                tmpI++;
                            }
                        }
                        YangDi2_Dialog.this.m_MediaInfoString = tmpResult;
                        YangDi2_Dialog.this.m_MediaButton.setText("[" + String.valueOf(tmpI) + "]照片");
                        YangDi2_Dialog.this.saveMediaInfo();
                    }
                } else if (command.equals("坐标输入返回") && object != null) {
                    double[] tmpDs = (double[]) object;
                    YangDi2_Dialog.this.m_CoordinateX = String.valueOf(tmpDs[0]);
                    YangDi2_Dialog.this.m_CoordinateY = String.valueOf(tmpDs[1]);
                    Common.SetTextViewValueOnID(YangDi2_Dialog.this._Dialog, (int) R.id.tv_YangdiInfo, String.valueOf(YangDi2_Dialog.this.m_YangDiInfo) + "\r\n坐标X:" + YangDi2_Dialog.this.m_CoordinateX + "\r\n坐标Y:" + YangDi2_Dialog.this.m_CoordinateY);
                    YangDi2_Dialog.this.saveCoordinateInfo();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.yangdi2_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("样地调查数据");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.SetAllowedCloseDialog(false);
        this._Dialog.SetCallback(this.pCallback);
        this._Dialog.SetCancelCommand("");
        this.m_MainLinearLayout = (LinearLayout) this._Dialog.findViewById(R.id.ll_Yangdi_DataList);
        this.m_GenJingList.clear();
        for (int i = 6; i < 31; i += 2) {
            this.m_GenJingList.add(Integer.valueOf(i));
        }
        this._Dialog.findViewById(R.id.btn_AddGenJing).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDi2_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                List<String> tmpArrays = new ArrayList<>();
                for (int i2 = 6; i2 < 107; i2 += 2) {
                    if (!YangDi2_Dialog.this.m_GenJingList.contains(Integer.valueOf(i2))) {
                        tmpArrays.add(String.valueOf(i2));
                    }
                }
                final String[] tmpArray = (String[]) tmpArrays.toArray(new String[tmpArrays.size()]);
                new AlertDialog.Builder(YangDi2_Dialog.this._Dialog.getContext(), 3).setTitle("选择根径:").setSingleChoiceItems(tmpArray, -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDi2_Dialog.2.1
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface arg0, int arg1) {
                        String tmpGenJingString = tmpArray[arg1];
                        int tmpI = Integer.parseInt(tmpGenJingString);
                        if (!YangDi2_Dialog.this.m_GenJingList.contains(Integer.valueOf(tmpI))) {
                            YangDi2_Dialog.this.m_GenJingList.add(Integer.valueOf(tmpI));
                        }
                        YangDi2_Dialog.this.addGenJingLineLayout(tmpGenJingString);
                        Common.ShowToast("添加根径【" + tmpGenJingString + "】完成.");
                        arg0.dismiss();
                    }
                }).show();
            }
        });
        this.m_MediaButton = (Button) this._Dialog.findViewById(R.id.btn_mediaMang);
        this.m_MediaButton.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDi2_Dialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                MediaManag_Dialog tempDialog = new MediaManag_Dialog(YangDi2_Dialog.this._Dialog.getContext());
                tempDialog.ReSetSize(1.0f, 0.96f);
                tempDialog.setFileNamePreString(YangDi2_Dialog.this.m_YangDiName);
                tempDialog.SetCaption(YangDi2_Dialog.this.m_YangDiName);
                tempDialog.SetPhotoInfo(YangDi2_Dialog.this.m_MediaInfoString);
                tempDialog.SetCallback(YangDi2_Dialog.this.pCallback);
                tempDialog.show();
            }
        });
        this._Dialog.findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDi2_Dialog.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                InputXY_Dialog tmpDialog = new InputXY_Dialog();
                tmpDialog.SetXY(new String[]{YangDi2_Dialog.this.m_CoordinateX, YangDi2_Dialog.this.m_CoordinateY});
                tmpDialog.SetCallback(YangDi2_Dialog.this.pCallback);
                tmpDialog.ShowDialog();
            }
        });
        this._Dialog.findViewById(R.id.btn_Cal).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDi2_Dialog.5
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (CommonSetting.getGenJingCaiJiTable(true).size() == 0) {
                    Common.ShowDialog("系统中没有“根径材积表”数据,请在“设置”中点击“根径材积设置”按钮.");
                    return;
                }
                HashMap<String, Double[]> tmpYdDataHashMap = new HashMap<>();
                for (Map.Entry<String, EditText[]> tmpEntry : YangDi2_Dialog.this.m_m_InputEditList.entrySet()) {
                    EditText[] tmpEditTexts = tmpEntry.getValue();
                    tmpYdDataHashMap.put(tmpEntry.getKey(), new Double[]{Double.valueOf(Double.parseDouble(tmpEditTexts[0].getText().toString())), Double.valueOf(Double.parseDouble(tmpEditTexts[1].getText().toString())), Double.valueOf(Double.parseDouble(tmpEditTexts[2].getText().toString()))});
                }
                if (tmpYdDataHashMap.size() > 0) {
                    StringBuilder tmpStringBuilder = new StringBuilder();
                    double[] tmpDs = CommonSetting.CalCaiJi(tmpYdDataHashMap);
                    tmpStringBuilder.append("样地名称：" + YangDi2_Dialog.this.m_YangDiName + "\r\n\r\n");
                    tmpStringBuilder.append("样地材积：\r\n");
                    tmpStringBuilder.append("  【杉    木】：" + YangDi2_Dialog.this.m_DecimalFormat.format(tmpDs[0]) + "m3\r\n");
                    tmpStringBuilder.append("  【马尾松】：" + YangDi2_Dialog.this.m_DecimalFormat.format(tmpDs[1]) + "m3\r\n");
                    tmpStringBuilder.append("  【阔叶树】：" + YangDi2_Dialog.this.m_DecimalFormat.format(tmpDs[2]) + "m3\r\n");
                    tmpStringBuilder.append("合计：" + YangDi2_Dialog.this.m_DecimalFormat.format(tmpDs[0] + tmpDs[1] + tmpDs[2]) + "\r\n");
                    tmpStringBuilder.append("\r\n公顷材积：\r\n");
                    tmpStringBuilder.append("  【杉    木】：" + YangDi2_Dialog.this.m_DecimalFormat.format(tmpDs[0] / CommonSetting.m_YangDiArea) + "m3\r\n");
                    tmpStringBuilder.append("  【马尾松】：" + YangDi2_Dialog.this.m_DecimalFormat.format(tmpDs[1] / CommonSetting.m_YangDiArea) + "m3\r\n");
                    tmpStringBuilder.append("  【阔叶树】：" + YangDi2_Dialog.this.m_DecimalFormat.format(tmpDs[2] / CommonSetting.m_YangDiArea) + "m3\r\n");
                    tmpStringBuilder.append("合计：" + YangDi2_Dialog.this.m_DecimalFormat.format(((tmpDs[0] + tmpDs[1]) + tmpDs[2]) / CommonSetting.m_YangDiArea) + "m3\r\n");
                    Common.ShowToast("计算完成!长按文字复制!");
                    Common.ShowDialog(YangDi2_Dialog.this._Dialog.getContext(), tmpStringBuilder.toString(), "计算结果", ViewCompat.MEASURED_STATE_MASK, null, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDi2_Dialog.5.1
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command, Object pObject) {
                            if (command.equals("点击对话框窗体文字返回")) {
                                Common.CopyText(String.valueOf(pObject));
                                Common.ShowToast("复制文字成功!");
                            }
                        }
                    });
                    return;
                }
                Common.ShowDialog("没有样地调查数据.");
            }
        });
    }

    public void setYangDiName(String yangdiName) {
        this.m_YangDiName = yangdiName;
    }

    public void setYangDiInfo(String info) {
        this.m_YangDiInfo = info;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_YangdiInfo, this.m_YangDiInfo);
        this.layoutParamsRoot = new LinearLayout.LayoutParams(-1, -2);
        this.layoutParamsRoot.setMargins(0, 0, 0, 0);
        this.tmpImgViewLayoutParams = new LinearLayout.LayoutParams(-2, -2);
        int tmpMargInt = (int) (PubVar.ScaledDensity * 5.0f);
        this.tmpImgViewLayoutParams.setMargins(tmpMargInt, tmpMargInt, tmpMargInt, tmpMargInt);
        this.tmpTxtLP01 = new LinearLayout.LayoutParams(-2, -2);
        this.tmpTxtLP01.width = (int) (PubVar.ScaledDensity * 60.0f);
        this.tmpTxtLP01.setMargins(0, 0, 0, 0);
        this.tmpTxtLP0101 = new LinearLayout.LayoutParams(-2, -1);
        this.tmpTxtLP0101.width = (int) (PubVar.ScaledDensity * 60.0f);
        this.tmpTxtLP0101.setMargins(0, 0, 0, 0);
        this.tmpTxtLP02 = new LinearLayout.LayoutParams(-2, -2);
        this.tmpTxtLP02.weight = 1.0f;
        this.tmpTxtLP02.setMargins(0, 0, 0, 0);
        this.m_m_InputEditList = new HashMap<>();
        if (this.m_GenJingList.size() > 0) {
            for (Integer num : this.m_GenJingList) {
                addGenJingLineLayout(String.valueOf(num));
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    @SuppressLint("WrongConstant")
    private void addGenJingLineLayout(String genJing) {
        try {
            LinearLayout layoutRoot = new LinearLayout(this._Dialog.getContext());
            layoutRoot.setLayoutParams(this.layoutParamsRoot);
            layoutRoot.setOrientation(0);
            TextView tmpTextView = new TextView(this._Dialog.getContext());
            tmpTextView.setText(genJing);
            tmpTextView.setTextSize(18.0f);
            tmpTextView.setGravity(17);
            tmpTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            tmpTextView.setLayoutParams(this.tmpTxtLP0101);
            tmpTextView.setBackgroundResource(R.drawable.bg_withborder01);
            layoutRoot.addView(tmpTextView);
            LinearLayout tmpLinearLayout01 = new LinearLayout(this._Dialog.getContext());
            tmpLinearLayout01.setLayoutParams(this.tmpTxtLP02);
            tmpLinearLayout01.setOrientation(1);
            tmpLinearLayout01.setBackgroundResource(R.drawable.bg_withborder01);
            EditText tmpEditText = new EditText(this._Dialog.getContext());
            tmpEditText.setText("0");
            tmpEditText.setInputType(2);
            tmpEditText.setLayoutParams(this.layoutParamsRoot);
            tmpEditText.setGravity(17);
            tmpEditText.setTag(genJing);
            tmpLinearLayout01.addView(tmpEditText);
            LinearLayout tmpLinearLayout001 = new LinearLayout(this._Dialog.getContext());
            tmpLinearLayout001.setLayoutParams(this.layoutParamsRoot);
            tmpLinearLayout001.setOrientation(0);
            Button tmpButton01 = new Button(this._Dialog.getContext());
            tmpButton01.setText("-");
            tmpButton01.setLayoutParams(this.tmpTxtLP02);
            tmpButton01.setTag("减少;" + genJing + ";0");
            tmpButton01.setOnClickListener(new ViewClick());
            tmpLinearLayout001.addView(tmpButton01);
            Button tmpButton02 = new Button(this._Dialog.getContext());
            tmpButton02.setText("+");
            tmpButton02.setLayoutParams(this.tmpTxtLP02);
            tmpButton02.setTag("增加;" + genJing + ";0");
            tmpButton02.setOnClickListener(new ViewClick());
            tmpLinearLayout001.addView(tmpButton02);
            tmpLinearLayout01.addView(tmpLinearLayout001);
            layoutRoot.addView(tmpLinearLayout01);
            LinearLayout tmpLinearLayout012 = new LinearLayout(this._Dialog.getContext());
            tmpLinearLayout012.setLayoutParams(this.tmpTxtLP02);
            tmpLinearLayout012.setOrientation(1);
            tmpLinearLayout012.setBackgroundResource(R.drawable.bg_withborder01);
            EditText tmpEditText2 = new EditText(this._Dialog.getContext());
            tmpEditText2.setText("0");
            tmpEditText2.setInputType(2);
            tmpEditText2.setLayoutParams(this.layoutParamsRoot);
            tmpEditText2.setGravity(17);
            tmpLinearLayout012.addView(tmpEditText2);
            LinearLayout tmpLinearLayout0012 = new LinearLayout(this._Dialog.getContext());
            tmpLinearLayout0012.setLayoutParams(this.layoutParamsRoot);
            tmpLinearLayout0012.setOrientation(0);
            Button tmpButton012 = new Button(this._Dialog.getContext());
            tmpButton012.setText("-");
            tmpButton012.setLayoutParams(this.tmpTxtLP02);
            tmpButton012.setTag("减少;" + genJing + ";1");
            tmpButton012.setOnClickListener(new ViewClick());
            tmpLinearLayout0012.addView(tmpButton012);
            Button tmpButton022 = new Button(this._Dialog.getContext());
            tmpButton022.setText("+");
            tmpButton022.setLayoutParams(this.tmpTxtLP02);
            tmpButton022.setTag("增加;" + genJing + ";1");
            tmpButton022.setOnClickListener(new ViewClick());
            tmpLinearLayout0012.addView(tmpButton022);
            tmpLinearLayout012.addView(tmpLinearLayout0012);
            layoutRoot.addView(tmpLinearLayout012);
            LinearLayout tmpLinearLayout013 = new LinearLayout(this._Dialog.getContext());
            tmpLinearLayout013.setLayoutParams(this.tmpTxtLP02);
            tmpLinearLayout013.setOrientation(1);
            tmpLinearLayout013.setBackgroundResource(R.drawable.bg_withborder01);
            EditText tmpEditText3 = new EditText(this._Dialog.getContext());
            tmpEditText3.setText("0");
            tmpEditText3.setInputType(2);
            tmpEditText3.setLayoutParams(this.layoutParamsRoot);
            tmpEditText3.setGravity(17);
            tmpLinearLayout013.addView(tmpEditText3);
            LinearLayout tmpLinearLayout0013 = new LinearLayout(this._Dialog.getContext());
            tmpLinearLayout0013.setLayoutParams(this.layoutParamsRoot);
            tmpLinearLayout0013.setOrientation(0);
            Button tmpButton013 = new Button(this._Dialog.getContext());
            tmpButton013.setText("-");
            tmpButton013.setLayoutParams(this.tmpTxtLP02);
            tmpButton013.setTag("减少;" + genJing + ";2");
            tmpButton013.setOnClickListener(new ViewClick());
            tmpLinearLayout0013.addView(tmpButton013);
            Button tmpButton023 = new Button(this._Dialog.getContext());
            tmpButton023.setText("+");
            tmpButton023.setLayoutParams(this.tmpTxtLP02);
            tmpButton023.setTag("增加;" + genJing + ";2");
            tmpButton023.setOnClickListener(new ViewClick());
            tmpLinearLayout0013.addView(tmpButton023);
            tmpLinearLayout013.addView(tmpLinearLayout0013);
            layoutRoot.addView(tmpLinearLayout013);
            this.m_MainLinearLayout.addView(layoutRoot);
            this.m_m_InputEditList.put(genJing, new EditText[]{tmpEditText, tmpEditText2, tmpEditText3});
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void saveMediaInfo() {
        if (this.m_SQLiteDBHelper != null) {
            this.m_SQLiteDBHelper.ExecuteSQL("Update T_YangDiInfo Set PhotoInfo='" + this.m_MediaInfoString + "' Where YangDiName='" + this.m_YangDiName + "'");
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void saveCoordinateInfo() {
        if (this.m_SQLiteDBHelper != null) {
            this.m_SQLiteDBHelper.ExecuteSQL("Update T_YangDiInfo Set X=" + this.m_CoordinateX + ",Y=" + this.m_CoordinateY + " Where YangDiName='" + this.m_YangDiName + "'");
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshDatabase() {
        boolean tmpBool01 = false;
        try {
            String tmpPath = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/SenLinDuCha.dbx";
            if (new File(tmpPath).exists()) {
                this.m_SQLiteDBHelper = new SQLiteDBHelper(tmpPath);
                tmpBool01 = true;
                SQLiteReader tmpSQLiteReader = this.m_SQLiteDBHelper.Query("Select GenJing,Shan,Ma,Kuo From T_YangDiData Where YangDiName='" + this.m_YangDiName + "' Order By ID");
                while (tmpSQLiteReader.Read()) {
                    String tmpGenJingString = tmpSQLiteReader.GetString(0);
                    if (this.m_m_InputEditList.containsKey(tmpGenJingString)) {
                        EditText[] tmpEditTexts = this.m_m_InputEditList.get(tmpGenJingString);
                        setEditTextValue(tmpEditTexts[0], tmpSQLiteReader.GetString(1));
                        setEditTextValue(tmpEditTexts[1], tmpSQLiteReader.GetString(2));
                        setEditTextValue(tmpEditTexts[2], tmpSQLiteReader.GetString(3));
                    } else {
                        int tmpI = Integer.parseInt(tmpGenJingString);
                        if (!this.m_GenJingList.contains(Integer.valueOf(tmpI))) {
                            this.m_GenJingList.add(Integer.valueOf(tmpI));
                        }
                        addGenJingLineLayout(tmpGenJingString);
                        if (this.m_m_InputEditList.containsKey(tmpGenJingString)) {
                            EditText[] tmpEditTexts2 = this.m_m_InputEditList.get(tmpGenJingString);
                            setEditTextValue(tmpEditTexts2[0], tmpSQLiteReader.GetString(1));
                            setEditTextValue(tmpEditTexts2[1], tmpSQLiteReader.GetString(2));
                            setEditTextValue(tmpEditTexts2[2], tmpSQLiteReader.GetString(3));
                        }
                    }
                }
                SQLiteReader tmpSQLiteReader2 = this.m_SQLiteDBHelper.Query("Select PhotoInfo,X,Y From T_YangDiInfo Where YangDiName='" + this.m_YangDiName + "'");
                if (tmpSQLiteReader2.Read()) {
                    this.m_MediaInfoString = tmpSQLiteReader2.GetString(0);
                    this.m_CoordinateX = String.valueOf(tmpSQLiteReader2.GetDouble(1));
                    this.m_CoordinateY = String.valueOf(tmpSQLiteReader2.GetDouble(2));
                    String[] tmpStrs02 = this.m_MediaInfoString.split(",");
                    if (tmpStrs02 != null && tmpStrs02.length > 0 && tmpStrs02[0].trim().length() > 0) {
                        this.m_MediaButton.setText("[" + String.valueOf(tmpStrs02.length) + "]照片");
                    }
                    Common.SetTextViewValueOnID(this._Dialog, (int) R.id.tv_YangdiInfo, String.valueOf(this.m_YangDiInfo) + "\r\n坐标X:" + this.m_CoordinateX + "\r\n坐标Y:" + this.m_CoordinateY);
                }
            }
            if (!tmpBool01) {
                Common.ShowDialog("督查图斑数据库还未建立!请先在“森林督查”工具栏中选择“设置”,然后添加督查图斑,最后建立督查图斑数据库.");
                this._Dialog.dismiss();
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        EditText tmpEditText;
        int tmpIDs2;
        EditText tmpEditText2;
        try {
            if (command.contains("增加;")) {
                this.m_NeedSave = true;
                String[] tmpStrings = command.split(";");
                if (tmpStrings != null && tmpStrings.length > 2) {
                    String tmpGenJingString = tmpStrings[1];
                    if (this.m_m_InputEditList.containsKey(tmpGenJingString) && (tmpEditText2 = this.m_m_InputEditList.get(tmpGenJingString)[Integer.parseInt(tmpStrings[2])]) != null) {
                        setEditTextValue(tmpEditText2, Integer.parseInt(tmpEditText2.getText().toString()) + 1);
                    }
                }
            } else if (command.contains("减少;")) {
                this.m_NeedSave = true;
                String[] tmpStrings2 = command.split(";");
                if (tmpStrings2 != null && tmpStrings2.length > 2) {
                    String tmpGenJingString2 = tmpStrings2[1];
                    if (this.m_m_InputEditList.containsKey(tmpGenJingString2) && (tmpEditText = this.m_m_InputEditList.get(tmpGenJingString2)[Integer.parseInt(tmpStrings2[2])]) != null && Integer.parseInt(tmpEditText.getText().toString()) - 1 >= 0) {
//                        setEditTextValue(tmpEditText, tmpIDs2);
                    }
                }
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean saveData() {
        try {
            this.m_SQLiteDBHelper.ExecuteSQL("Delete From T_YangDiData Where YangDiName='" + this.m_YangDiName + "'");
            for (Map.Entry<String, EditText[]> tmpEntry : this.m_m_InputEditList.entrySet()) {
                String tmpGenJingString = tmpEntry.getKey();
                EditText[] tmpEditTexts = tmpEntry.getValue();
                String tmpString01 = String.valueOf(tmpEditTexts[0].getText().toString()) + "','" + tmpEditTexts[1].getText().toString() + "','" + tmpEditTexts[2].getText().toString();
                if (!tmpString01.equals("0','0','0")) {
                    this.m_SQLiteDBHelper.ExecuteSQL("Insert Into T_YangDiData (YangDiName,GenJing,Shan,Ma,Kuo) Values ('" + this.m_YangDiName + "','" + tmpGenJingString + "','" + tmpString01 + "')");
                }
            }
            return true;
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
            return false;
        }
    }

    private void setEditTextValue(EditText editText, int value) {
        editText.setText(String.valueOf(value));
        if (value == 0) {
            editText.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        } else {
            editText.setTextColor(SupportMenu.CATEGORY_MASK);
        }
    }

    private void setEditTextValue(EditText editText, String value) {
        editText.setText(value);
        if (value.equals("0")) {
            editText.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        } else {
            editText.setTextColor(SupportMenu.CATEGORY_MASK);
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDi2_Dialog.6
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                YangDi2_Dialog.this.refreshLayout();
                YangDi2_Dialog.this.refreshDatabase();
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: package-private */
    public class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                YangDi2_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
