package  com.xzy.forestSystem.gogisapi.Tools;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class XuJiTable_Import_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private EditText m_FileEditTxt;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public XuJiTable_Import_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_FileEditTxt = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.XuJiTable_Import_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                String[] tempPath2;
                try {
                    if (command.equals("确定")) {
                        String tmpTableName = Common.GetSpinnerValueOnID(XuJiTable_Import_Dialog.this._Dialog, R.id.sp_xujitableImport_TableType);
                        if (tmpTableName.length() == 0) {
                            Common.ShowToast("表类型不能为空.");
                            return;
                        }
                        String tmpZoneName = Common.GetEditTextValueOnID(XuJiTable_Import_Dialog.this._Dialog, R.id.et_xujitableImport_zonename);
                        if (tmpZoneName.length() == 0) {
                            Common.ShowToast("省不能为空.需输入计算表所适应的省份,如浙江省");
                        } else if (XuJiTable_Import_Dialog.this.m_MyTableDataList.size() == 0) {
                            Common.ShowDialog("没有导入任何有效的表格数据.");
                        } else {
                            final XuJiTableClass tmpXuJiTable = XuJiTableClass.GetXuJiTable(tmpZoneName, tmpTableName);
                            if (tmpXuJiTable != null) {
                                Common.ShowYesNoDialog(XuJiTable_Import_Dialog.this._Dialog.getContext(), "【" + tmpZoneName + "】的蓄积量计算表【" + tmpTableName + "】已经存在,是否替换之前的计算表?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.XuJiTable_Import_Dialog.1.1
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String command2, Object pObject) {
                                        if (command2.equals("YES")) {
                                            XuJiTableClass.DeleteXuJiTable(tmpXuJiTable);
                                            XuJiTable_Import_Dialog.this.addXujiTable();
                                            if (XuJiTable_Import_Dialog.this.m_Callback != null) {
                                                XuJiTable_Import_Dialog.this.m_Callback.OnClick("导入蓄积量计算表返回", null);
                                            }
                                            XuJiTable_Import_Dialog.this._Dialog.dismiss();
                                        }
                                    }
                                });
                                return;
                            }
                            XuJiTable_Import_Dialog.this.addXujiTable();
                            if (XuJiTable_Import_Dialog.this.m_Callback != null) {
                                XuJiTable_Import_Dialog.this.m_Callback.OnClick("导入蓄积量计算表返回", null);
                            }
                            XuJiTable_Import_Dialog.this._Dialog.dismiss();
                        }
                    } else if (command.equals("选择文件") && (tempPath2 = object.toString().split(";")) != null && tempPath2.length > 1) {
                        XuJiTable_Import_Dialog.this.m_FileEditTxt.setText(tempPath2[1]);
                        XuJiTable_Import_Dialog.this.DoCommand("加载文件");
                    }
                } catch (Exception e) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.xujitable_import_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("蓄积表导入");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_FileEditTxt = (EditText) this._Dialog.findViewById(R.id.et_xujitableImport_filepath);
        this._Dialog.findViewById(R.id.btn_xujitableImport_file).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_xujitableImport_LoadFile).setOnClickListener(new ViewClick());
        List<String> tmpList = new ArrayList<>();
        tmpList.add("松木");
        tmpList.add("人工杉木");
        tmpList.add("天然杉木");
        tmpList.add("阔叶树");
        Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_xujitableImport_TableType, tmpList.get(0), "请选择", tmpList, (String) null, this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void addXujiTable() {
        try {
            String tmpTableName = Common.GetSpinnerValueOnID(this._Dialog, R.id.sp_xujitableImport_TableType);
            String tmpZoneName = Common.GetEditTextValueOnID(this._Dialog, R.id.et_xujitableImport_zonename);
            if (this.m_MyTableDataList.size() > 0) {
                XuJiTableClass tmpXuJiTableClass = new XuJiTableClass();
                tmpXuJiTableClass.Name = tmpTableName;
                tmpXuJiTableClass.ZoneName = tmpZoneName;
                tmpXuJiTableClass.Remark = "";
                HashMap<String, Object> tmpHashMap = this.m_MyTableDataList.get(0);
                List<Integer> tmpList01 = new ArrayList<>();
                int count = tmpHashMap.size();
                for (int i = 2; i < count; i++) {
                    String tmpFIDString = "D" + String.valueOf(i + 1);
                    if (tmpHashMap.containsKey(tmpFIDString)) {
                        String tmpString = String.valueOf(tmpHashMap.get(tmpFIDString));
                        int tmpInteger = 0;
                        if (tmpString.length() > 0) {
                            tmpInteger = Integer.valueOf(Integer.parseInt(tmpString));
                        }
                        tmpList01.add(tmpInteger);
                    }
                }
                tmpXuJiTableClass.TreeRadius = tmpList01;
                List<Integer> tmpList02 = new ArrayList<>();
                List<Double> tmpList03 = new ArrayList<>();
                List<List<Integer>> tmpList04 = new ArrayList<>();
                Iterator<HashMap<String, Object>> tmpIterator = this.m_MyTableDataList.iterator();
                tmpIterator.next();
                while (tmpIterator.hasNext()) {
                    HashMap<String, Object> tmpHashMap2 = tmpIterator.next();
                    String tmpString01 = String.valueOf(tmpHashMap2.get("D1"));
                    int tmpInteger2 = 0;
                    if (tmpString01.length() > 0) {
                        tmpInteger2 = Integer.valueOf(Integer.parseInt(tmpString01));
                    }
                    tmpList02.add(tmpInteger2);
                    String tmpString02 = String.valueOf(tmpHashMap2.get("D2"));
                    Double tmpDouble = Double.valueOf(0.0d);
                    if (tmpString02.length() > 0) {
                        tmpDouble = Double.valueOf(Double.parseDouble(tmpString02));
                    }
                    tmpList03.add(tmpDouble);
                    List<Integer> tmpList0401 = new ArrayList<>();
                    int count2 = tmpHashMap2.size();
                    for (int i2 = 2; i2 < count2; i2++) {
                        String tmpFIDString2 = "D" + String.valueOf(i2 + 1);
                        if (tmpHashMap2.containsKey(tmpFIDString2)) {
                            String tmpString2 = String.valueOf(tmpHashMap2.get(tmpFIDString2));
                            int tmpInteger3 = 0;
                            if (tmpString2.length() > 0) {
                                tmpInteger3 = Integer.valueOf(Integer.parseInt(tmpString2));
                            }
                            tmpList0401.add(tmpInteger3);
                        }
                    }
                    tmpList04.add(tmpList0401);
                }
                tmpXuJiTableClass.TreeHeights = tmpList02;
                tmpXuJiTableClass.TreeVolumns = tmpList03;
                tmpXuJiTableClass.setTreeCountList(tmpList04);
                XuJiTableClass.SaveXuJiTable(tmpXuJiTableClass);
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.XuJiTable_Import_Dialog.2
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
        this.m_MyTableDataList.clear();
        try {
            int Rows = sheet.getRows();
            int Cols = sheet.getColumns();
            String tempColumns = "";
            String tempColumnTypes = "";
            int[] tempColumnWidth = new int[Cols];
            for (int j = 0; j < Cols; j++) {
                if (tempColumns.length() > 0) {
                    tempColumns = String.valueOf(tempColumns) + ",";
                }
                tempColumns = String.valueOf(tempColumns) + "F" + String.valueOf(j + 1);
                if (tempColumnTypes.length() > 0) {
                    tempColumnTypes = String.valueOf(tempColumnTypes) + ",";
                }
                tempColumnTypes = String.valueOf(tempColumnTypes) + "text";
                tempColumnWidth[j] = 90;
            }
            this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_xujitableImport_datalist), "自定义", tempColumns, tempColumnTypes, tempColumnWidth, null);
            for (int i = 0; i < Rows; i++) {
                HashMap tmpHash = new HashMap();
                for (int j2 = 0; j2 < Cols; j2++) {
                    Cell tmpCell = sheet.getCell(j2, i);
                    if (tmpCell != null) {
                        tmpStr = tmpCell.getContents();
                    } else {
                        tmpStr = "";
                    }
                    tmpHash.put("D" + String.valueOf(j2 + 1), tmpStr);
                }
                this.m_MyTableDataList.add(tmpHash);
            }
            this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("选择文件")) {
                FileSelector_Dialog tempDialog = new FileSelector_Dialog(".xls;", false);
                Common.ShowToast("请选择蓄积量表数据文件(.XLS).");
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
                            new AlertDialog.Builder(this._Dialog.getContext(), 3).setTitle("选择表格").setSingleChoiceItems(tmpArray, -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.XuJiTable_Import_Dialog.3
                                @Override // android.content.DialogInterface.OnClickListener
                                public void onClick(DialogInterface arg0, int arg1) {
                                    if (arg1 > -1) {
                                        try {
                                            XuJiTable_Import_Dialog.this.loadExcelSheet(excelBook.getSheet(arg1));
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
                } else {
                    Common.ShowToast("请选择Excel格式文件.");
                }
            }
        } catch (Exception e2) {
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
                XuJiTable_Import_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
