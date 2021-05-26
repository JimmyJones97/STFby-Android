package  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityDevice_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class GenJingTableImport_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private EditText m_FileEditTxt;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public GenJingTableImport_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_FileEditTxt = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.GenJingTableImport_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                String[] tempPath2;
                try {
                    if (command.equals("确定")) {
                        if (GenJingTableImport_Dialog.this.m_MyTableDataList.size() > 0) {
                            for (HashMap<String, Object> tmpHash : GenJingTableImport_Dialog.this.m_MyTableDataList) {
                                PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().ExecuteSQL("Replace Into T_GenJingTable (GenJing,Shan,Ma,Kuo) Values ('" + String.valueOf(tmpHash.get("D2")) + "'," + String.valueOf(tmpHash.get("D3")) + "," + String.valueOf(tmpHash.get("D4")) + "," + String.valueOf(tmpHash.get("D5")) + ")");
                            }
                            Common.ShowDialog("导入根径材积数据成功.");
                            if (GenJingTableImport_Dialog.this.m_Callback != null) {
                                GenJingTableImport_Dialog.this.m_Callback.OnClick("导入数据文件返回", null);
                            }
                            GenJingTableImport_Dialog.this._Dialog.dismiss();
                            return;
                        }
                        Common.ShowDialog("没有导入任何有效的数据.");
                    } else if (command.equals("选择文件") && (tempPath2 = object.toString().split(";")) != null && tempPath2.length > 1) {
                        String tempFPath = tempPath2[1];
                        GenJingTableImport_Dialog.this.m_FileEditTxt.setText(tempFPath);
                        if (tempFPath.substring(tempFPath.lastIndexOf(FileSelector_Dialog.sFolder)).toLowerCase().contains("xls")) {
                            GenJingTableImport_Dialog.this._Dialog.findViewById(R.id.ll_dataDictImport_splitChar).setVisibility(8);
                        } else {
                            GenJingTableImport_Dialog.this._Dialog.findViewById(R.id.ll_dataDictImport_splitChar).setVisibility(0);
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.genjingtableimport_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("根径材积表导入");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_FileEditTxt = (EditText) this._Dialog.findViewById(R.id.et_dataDictImport_filepath);
        this._Dialog.findViewById(R.id.btn_dataDictImport_file).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_dataDictImport_LoadFile).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_dataDictImport_delete).setOnClickListener(new ViewClick());
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_dataDictImport_datalist), "自定义", "选择,根径,杉木,马尾松,阔叶树", "checkbox,text,text,text,text", new int[]{-15, -19, -22, -22, -22}, null);
        this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
    }

    private void loadTxtDataList() {
        List<String> tmpStrsList;
        this.m_MyTableDataList.clear();
        String tmpFilepath = this.m_FileEditTxt.getText().toString();
        if (new File(tmpFilepath).exists()) {
            String tmpSplitChar = Common.GetEditTextValueOnID(this._Dialog, R.id.et_dataDictImport_splitChar);
            try {
                FileInputStream inputStream = new FileInputStream(tmpFilepath);
                InputStreamReader inputStreamReader = null;
                try {
                    inputStreamReader = new InputStreamReader(inputStream, "gb2312");
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                BufferedReader reader = new BufferedReader(inputStreamReader);
                new ArrayList();
                try {
                    if (Common.GetCheckBoxValueOnID(this._Dialog, R.id.ck_SkipFirst)) {
                        reader.readLine();
                    }
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        } else if (!line.trim().equals("") && (tmpStrsList = ProcessSCVLine(line, tmpSplitChar)) != null && tmpStrsList.size() > 3) {
                            HashMap tmpHash = new HashMap();
                            tmpHash.put("D1", false);
                            tmpHash.put("D2", tmpStrsList.get(0).trim());
                            tmpHash.put("D3", tmpStrsList.get(1).trim());
                            tmpHash.put("D4", tmpStrsList.get(2).trim());
                            tmpHash.put("D5", tmpStrsList.get(3).trim());
                            this.m_MyTableDataList.add(tmpHash);
                        }
                    }
                } catch (IOException e) {
                }
                inputStream.close();
            } catch (Exception e2) {
            }
        } else {
            Common.ShowToast("文件:" + tmpFilepath + " 不存在.");
        }
        this.m_MyTableFactory.notifyDataSetChanged();
    }

    public static List<String> ProcessSCVLine(String line, String splitChar) {
        List<String> result = null;
        String[] tmpStrs = line.split(splitChar);
        if (tmpStrs != null && tmpStrs.length > 0) {
            result = new ArrayList<>();
            boolean isLine = false;
            StringBuilder tmpSB = new StringBuilder();
            for (String tmpStr : tmpStrs) {
                if (tmpStr.startsWith("\"")) {
                    if (isLine) {
                        if (tmpSB.length() > 0) {
                            tmpSB.append(tmpStr.substring(0, tmpStr.length() - 1));
                            result.add(tmpSB.toString());
                        }
                        isLine = false;
                        tmpSB = new StringBuilder();
                    } else {
                        isLine = true;
                        tmpSB = new StringBuilder();
                        tmpSB.append(tmpStr.substring(1));
                        tmpSB.append(splitChar);
                    }
                } else if (tmpStr.endsWith("\"")) {
                    isLine = false;
                    tmpSB.append(tmpStr.substring(0, tmpStr.length() - 1));
                    result.add(tmpSB.toString());
                    tmpSB = new StringBuilder();
                } else if (isLine) {
                    tmpSB.append(tmpStr);
                    tmpSB.append(splitChar);
                } else {
                    result.add(tmpStr);
                }
            }
        }
        return result;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.GenJingTableImport_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void loadExcelSheet(Sheet sheet) {
        String tmpStr;
        String tmpStr2;
        String tmpStr3;
        String tmpStr4;
        this.m_MyTableDataList.clear();
        try {
            int Rows = sheet.getRows();
            sheet.getColumns();
            int i = 0;
            if (Common.GetCheckBoxValueOnID(this._Dialog, R.id.ck_SkipFirst)) {
                i = 1;
            }
            while (i < Rows) {
                HashMap tmpHash = new HashMap();
                tmpHash.put("D1", false);
                Cell tmpCell = sheet.getCell(0, i);
                if (tmpCell != null) {
                    tmpStr = tmpCell.getContents();
                } else {
                    tmpStr = "";
                }
                int tmpLen = 0 + tmpStr.length();
                tmpHash.put("D2", tmpStr);
                Cell tmpCell2 = sheet.getCell(1, i);
                if (tmpCell2 != null) {
                    tmpStr2 = tmpCell2.getContents();
                } else {
                    tmpStr2 = "";
                }
                int tmpLen2 = tmpLen + tmpStr2.length();
                tmpHash.put("D3", tmpStr2);
                Cell tmpCell3 = sheet.getCell(2, i);
                if (tmpCell3 != null) {
                    tmpStr3 = tmpCell3.getContents();
                } else {
                    tmpStr3 = "";
                }
                int tmpLen3 = tmpLen2 + tmpStr3.length();
                tmpHash.put("D4", tmpStr3);
                Cell tmpCell4 = sheet.getCell(3, i);
                if (tmpCell4 != null) {
                    tmpStr4 = tmpCell4.getContents();
                } else {
                    tmpStr4 = "";
                }
                int tmpLen4 = tmpLen3 + tmpStr4.length();
                tmpHash.put("D5", tmpStr4);
                if (tmpLen4 > 0) {
                    this.m_MyTableDataList.add(tmpHash);
                }
                i++;
            }
        } catch (Exception e) {
        }
        this.m_MyTableFactory.notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("选择文件")) {
            FileSelector_Dialog tempDialog = new FileSelector_Dialog(".xls;.csv;.txt;.data;", false);
            Common.ShowToast("请选择根径材积表数据文件(.XLS,.CSV,.TXT等).");
            tempDialog.SetTitle("选择文件");
            tempDialog.SetCallback(this.pCallback);
            tempDialog.ShowDialog();
        } else if (command.equals("加载文件")) {
            String tmpFilePath = this.m_FileEditTxt.getText().toString();
            if (!new File(tmpFilePath).exists()) {
                Common.ShowToast("文件:" + tmpFilePath + " 不存在.");
            } else if (tmpFilePath.substring(tmpFilePath.lastIndexOf(FileSelector_Dialog.sFolder)).toLowerCase().contains("xls")) {
                try {
                    final Workbook excelBook = Workbook.getWorkbook(new FileInputStream(tmpFilePath));
                    int num = excelBook.getNumberOfSheets();
                    if (num <= 0) {
                        excelBook.close();
                        Common.ShowToast("该文件中没有任何表.");
                    } else if (num == 1) {
                        loadExcelSheet(excelBook.getSheet(0));
                    } else {
                        String[] tmpArray = new String[num];
                        for (int i = 0; i < num; i++) {
                            tmpArray[i] = excelBook.getSheet(i).getName();
                        }
                        new AlertDialog.Builder(this._Dialog.getContext(), 3).setTitle("选择表格").setSingleChoiceItems(tmpArray, -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.GenJingTableImport_Dialog.3
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (arg1 > -1) {
                                    try {
                                        GenJingTableImport_Dialog.this.loadExcelSheet(excelBook.getSheet(arg1));
                                    } catch (Exception e) {
                                    }
                                }
                                arg0.dismiss();
                            }
                        }).show();
                    }
                } catch (Exception e) {
                    Common.ShowToast("打开Excel文件失败,请另存为Excel 2003格式后再进行此操作.");
                }
            } else if (Common.GetEditTextValueOnID(this._Dialog, R.id.et_dataDictImport_splitChar).equals("")) {
                Common.ShowToast("分隔符号不能为空.");
            } else {
                loadTxtDataList();
            }
        } else if (command.equals("添加设备")) {
            DataAuthorityDevice_Dialog tmpDialog = new DataAuthorityDevice_Dialog();
            tmpDialog.SetCallback(this.pCallback);
            tmpDialog.SetReturnTag("添加设备返回");
            tmpDialog.ShowDialog();
        } else if (command.equals("修改设备")) {
            List<Integer> tmpList = getSelectedItems();
            if (tmpList.size() == 1) {
                int tmpI = tmpList.get(0).intValue();
                DataAuthorityDevice_Dialog tmpDialog2 = new DataAuthorityDevice_Dialog();
                tmpDialog2.SetCallback(this.pCallback);
                tmpDialog2.SetDeviceHash(this.m_MyTableDataList.get(tmpI));
                tmpDialog2.SetReturnTag("修改设备返回");
                tmpDialog2.ShowDialog();
                return;
            }
            Common.ShowDialog("请选择一个设备后再进行此操作.");
        } else if (command.equals("删除")) {
            List<Integer> tmpList2 = getSelectedItems();
            if (tmpList2.size() > 0) {
                StringBuilder tmpSB = new StringBuilder();
                tmpSB.append("是否删除以下选择的数据?\r\n");
                for (Integer num2 : tmpList2) {
                    tmpSB.append(String.valueOf(this.m_MyTableDataList.get(num2.intValue()).get("D2")));
                    tmpSB.append("\r\n");
                }
                Common.ShowYesNoDialog(this._Dialog.getContext(), tmpSB.toString(), new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.GenJingTableImport_Dialog.4
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        if (paramString.equals("YES")) {
                            int i2 = 0;
                            while (i2 < GenJingTableImport_Dialog.this.m_MyTableDataList.size()) {
                                if (Boolean.parseBoolean(((HashMap) GenJingTableImport_Dialog.this.m_MyTableDataList.get(i2)).get("D1").toString())) {
                                    GenJingTableImport_Dialog.this.m_MyTableDataList.remove(i2);
                                    i2--;
                                }
                                i2++;
                            }
                            GenJingTableImport_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                        }
                    }
                });
                return;
            }
            Common.ShowDialog("请至少选择一个数据后再进行此操作.");
        }
    }

    private List<Integer> getSelectedItems() {
        List<Integer> list = new ArrayList<>();
        int tid = -1;
        for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
            tid++;
            if (Boolean.parseBoolean(tmpHash.get("D1").toString())) {
                list.add(Integer.valueOf(tid));
            }
        }
        return list;
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                GenJingTableImport_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
