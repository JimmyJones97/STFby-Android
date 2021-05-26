package  com.xzy.forestSystem.gogisapi.Others.DataDictionary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class DataDict_Manage_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public DataDict_Manage_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDict_Manage_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("导入数据字典文件返回")) {
                    DataDict_Manage_Dialog.this.refreshTable();
                } else if (command.equals("字典条目添加返回") || command.equals("字典编辑返回")) {
                    DataDict_Manage_Dialog.this.refreshTable();
                } else if (command.equals("导出")) {
                    int tmpCount = 0;
                    for (HashMap<String, Object> tmpHash : DataDict_Manage_Dialog.this.m_MyTableDataList) {
                        if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                            tmpCount++;
                        }
                    }
                    if (tmpCount == 0) {
                        Common.ShowDialog("请在字典数据列表中勾选需要导出的字典内容.");
                        return;
                    }
                    String tmpPathString = String.valueOf(Common.GetAPPPath()) + "/数据字典_" + Common.fileDateFormat.format(new Date()) + ".xls";
                    if (DataDict_Manage_Dialog.this.exportExcel(tmpPathString)) {
                        Common.ShowDialog("数据字典已成功导出.\r\n导出至:\r\n" + tmpPathString);
                    }
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.datadict_manage_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("数据字典管理");
        this._Dialog.SetHeadButtons("1,2130837858,导出,导出", this.pCallback);
        this._Dialog.findViewById(R.id.btn_dataManag_add).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_dataManag_modify).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_dataManag_delete).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_dataManag_Load).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectAll).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectDe).setOnClickListener(new ViewClick());
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_dataManag_datalist), "自定义", "选择,字典,大类,细类,细类代码,详细,编号", "checkbox,text,text,text,text,text,text", new int[]{45, 90, 135, 135, 90, 225, 90}, null);
        this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
        this._Dialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDict_Manage_Dialog.2
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode == 82) {
                    new AlertDialog.Builder(DataDict_Manage_Dialog.this._Dialog.getContext(), 3).setTitle("选择操作:").setSingleChoiceItems(new String[]{"全选", "反选"}, -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDict_Manage_Dialog.2.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface arg02, int arg1) {
                            if (arg1 == 0) {
                                for (HashMap<String, Object> tmpHash : DataDict_Manage_Dialog.this.m_MyTableDataList) {
                                    tmpHash.put("D1", true);
                                }
                                DataDict_Manage_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                            } else if (arg1 == 1) {
                                for (HashMap<String, Object> tmpHash2 : DataDict_Manage_Dialog.this.m_MyTableDataList) {
                                    tmpHash2.put("D1", Boolean.valueOf(!Boolean.parseBoolean(String.valueOf(tmpHash2.get("D1")))));
                                }
                                DataDict_Manage_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                            }
                            arg02.dismiss();
                        }
                    }).show();
                }
                return DataDict_Manage_Dialog.this._Dialog.onKeyDown(keyCode, event);
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("添加")) {
            DataDict_AddModify_Dialog tmpDialog = new DataDict_AddModify_Dialog();
            tmpDialog.SetCallback(this.pCallback);
            tmpDialog.ShowDialog();
        } else if (command.equals("修改")) {
            int tmpI = this.m_MyTableFactory.getSelectItemIndex();
            if (tmpI >= 0) {
                DataDict_AddModify_Dialog tmpDialog2 = new DataDict_AddModify_Dialog();
                tmpDialog2.SetCallback(this.pCallback);
                tmpDialog2.setDataObject(this.m_MyTableDataList.get(tmpI));
                tmpDialog2.ShowDialog();
                return;
            }
            Common.ShowDialog("请先点击选择需要修改的条目(选中后背景颜色为黄色).");
        } else if (command.equals("删除")) {
            final List<String> tmpList = new ArrayList<>();
            for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
                if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                    tmpList.add(String.valueOf(tmpHash.get("D7")));
                }
            }
            if (tmpList.size() > 0) {
                Common.ShowYesNoDialog(this._Dialog.getContext(), "是否删除选择的" + String.valueOf(tmpList.size()) + "个字典条目?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDict_Manage_Dialog.3
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        if (paramString.equals("YES")) {
                            for (int i = tmpList.size() - 1; i > -1; i--) {
                                PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().ExecuteSQL("Delete from T_DataDictionary Where ZDBM='" + ((String) tmpList.get(i)) + "'");
                            }
                            DataDict_Manage_Dialog.this.refreshTable();
                        }
                    }
                });
            } else {
                Common.ShowDialog("没有勾选任何字典条目.");
            }
        } else if (command.equals("导入文件")) {
            DataDict_Import_Dialog tmpDialog3 = new DataDict_Import_Dialog();
            tmpDialog3.SetCallback(this.pCallback);
            tmpDialog3.ShowDialog();
        } else if (command.equals("全选")) {
            for (HashMap<String, Object> tmpHash2 : this.m_MyTableDataList) {
                tmpHash2.put("D1", true);
            }
            this.m_MyTableFactory.notifyDataSetChanged();
        } else if (command.equals("反选")) {
            for (HashMap<String, Object> tmpHash3 : this.m_MyTableDataList) {
                tmpHash3.put("D1", Boolean.valueOf(!Boolean.parseBoolean(String.valueOf(tmpHash3.get("D1")))));
            }
            this.m_MyTableFactory.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshTable() {
        this.m_MyTableDataList.clear();
        SQLiteReader localSQLiteDataReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("Select ZDType,ZDSub,ZDName,ZdNameCode,ZDList,ZDBM from T_DataDictionary");
        if (localSQLiteDataReader != null) {
            while (localSQLiteDataReader.Read()) {
                HashMap tmpHash = new HashMap();
                tmpHash.put("D1", false);
                tmpHash.put("D2", localSQLiteDataReader.GetString(0));
                tmpHash.put("D3", localSQLiteDataReader.GetString(1));
                tmpHash.put("D4", localSQLiteDataReader.GetString(2));
                tmpHash.put("D5", localSQLiteDataReader.GetString(3));
                tmpHash.put("D6", localSQLiteDataReader.GetString(4));
                tmpHash.put("D7", localSQLiteDataReader.GetString(5));
                this.m_MyTableDataList.add(tmpHash);
            }
            localSQLiteDataReader.Close();
        }
        this.m_MyTableFactory.notifyDataSetChanged();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean exportExcel(String filepath) {
        try {
            WritableWorkbook wb = Workbook.createWorkbook(new File(filepath));
            WritableSheet wSheet = wb.createSheet("数据字典", 0);
            wSheet.addCell(new Label(0, 0, "字典类型"));
            wSheet.addCell(new Label(1, 0, "字典大类"));
            wSheet.addCell(new Label(2, 0, "细类"));
            wSheet.addCell(new Label(3, 0, "细类代码"));
            wSheet.addCell(new Label(4, 0, "详细数据"));
            wSheet.addCell(new Label(5, 0, "字典编码"));
            int tmpTid = 0;
            for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
                if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                    tmpTid++;
                    wSheet.addCell(new Label(0, tmpTid, String.valueOf(tmpHash.get("D2"))));
                    wSheet.addCell(new Label(1, tmpTid, String.valueOf(tmpHash.get("D3"))));
                    wSheet.addCell(new Label(2, tmpTid, String.valueOf(tmpHash.get("D4"))));
                    wSheet.addCell(new Label(3, tmpTid, String.valueOf(tmpHash.get("D5"))));
                    wSheet.addCell(new Label(4, tmpTid, String.valueOf(tmpHash.get("D6"))));
                    wSheet.addCell(new Label(5, tmpTid, String.valueOf(tmpHash.get("D7"))));
                }
            }
            wb.write();
            wb.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDict_Manage_Dialog.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                DataDict_Manage_Dialog.this.refreshTable();
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
                DataDict_Manage_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
