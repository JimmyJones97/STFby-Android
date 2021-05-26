package  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha;

import android.content.DialogInterface;
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

public class GenJingTable_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public GenJingTable_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.GenJingTable_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("导入数据文件返回")) {
                    GenJingTable_Dialog.this.refreshTable();
                    CommonSetting.getGenJingCaiJiTable(true);
                } else if (command.equals("导出")) {
                    int tmpCount = 0;
                    for (HashMap<String, Object> tmpHash : GenJingTable_Dialog.this.m_MyTableDataList) {
                        if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                            tmpCount++;
                        }
                    }
                    if (tmpCount == 0) {
                        Common.ShowDialog("请在数据列表中勾选需要导出的内容.");
                        return;
                    }
                    String tmpPathString = String.valueOf(Common.GetAPPPath()) + "/根径材积表_" + Common.fileDateFormat.format(new Date()) + ".xls";
                    if (GenJingTable_Dialog.this.exportExcel(tmpPathString)) {
                        Common.ShowDialog("根径材积表已成功导出.\r\n导出至:\r\n" + tmpPathString);
                    }
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.genjingtable_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("根径材积表");
        this._Dialog.SetHeadButtons("1,2130837858,导出,导出", this.pCallback);
        this._Dialog.findViewById(R.id.btn_GenJing_delete).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_GenJing_Load).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectAll).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectDe).setOnClickListener(new ViewClick());
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_GenJing_datalist), "自定义", "选择,根径,杉木,马尾松,阔叶树", "checkbox,text,text,text,text", new int[]{-15, -19, -22, -22, -22}, null);
        this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("删除")) {
            final List<String> tmpList = new ArrayList<>();
            for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
                if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                    tmpList.add(String.valueOf(tmpHash.get("D2")));
                }
            }
            if (tmpList.size() > 0) {
                Common.ShowYesNoDialog(this._Dialog.getContext(), "是否删除选择的" + String.valueOf(tmpList.size()) + "个数据?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.GenJingTable_Dialog.2
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        if (paramString.equals("YES")) {
                            PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().ExecuteSQL("Delete from T_GenJingTable Where GenJing IN ('" + Common.CombineStrings("','", tmpList) + "')");
                            GenJingTable_Dialog.this.refreshTable();
                            CommonSetting.getGenJingCaiJiTable(true);
                        }
                    }
                });
            } else {
                Common.ShowDialog("没有勾选任何数据.");
            }
        } else if (command.equals("导入文件")) {
            GenJingTableImport_Dialog tmpDialog = new GenJingTableImport_Dialog();
            tmpDialog.SetCallback(this.pCallback);
            tmpDialog.ShowDialog();
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
        try {
            this.m_MyTableDataList.clear();
            PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().ExecuteSQL("Create Table If Not Exists T_GenJingTable (ID integer primary key AutoIncrement,GenJing varchar(10) NOT NULL Unique,Shan Double Default 0,Ma Double Default 0,Kuo Double Default 0)");
            SQLiteReader localSQLiteDataReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("Select GenJing,Shan,Ma,Kuo from T_GenJingTable");
            if (localSQLiteDataReader != null) {
                while (localSQLiteDataReader.Read()) {
                    HashMap tmpHash = new HashMap();
                    tmpHash.put("D1", false);
                    tmpHash.put("D2", localSQLiteDataReader.GetString(0));
                    tmpHash.put("D3", Double.valueOf(localSQLiteDataReader.GetDouble(1)));
                    tmpHash.put("D4", Double.valueOf(localSQLiteDataReader.GetDouble(2)));
                    tmpHash.put("D5", Double.valueOf(localSQLiteDataReader.GetDouble(3)));
                    this.m_MyTableDataList.add(tmpHash);
                }
                localSQLiteDataReader.Close();
            }
            this.m_MyTableFactory.notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean exportExcel(String filepath) {
        try {
            WritableWorkbook wb = Workbook.createWorkbook(new File(filepath));
            WritableSheet wSheet = wb.createSheet("根径材积表", 0);
            wSheet.addCell(new Label(0, 0, "根径"));
            wSheet.addCell(new Label(1, 0, "杉木"));
            wSheet.addCell(new Label(2, 0, "马尾松"));
            wSheet.addCell(new Label(3, 0, "阔叶树"));
            int tmpTid = 0;
            for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
                if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                    tmpTid++;
                    wSheet.addCell(new Label(0, tmpTid, String.valueOf(tmpHash.get("D2"))));
                    wSheet.addCell(new Label(1, tmpTid, String.valueOf(tmpHash.get("D3"))));
                    wSheet.addCell(new Label(2, tmpTid, String.valueOf(tmpHash.get("D4"))));
                    wSheet.addCell(new Label(3, tmpTid, String.valueOf(tmpHash.get("D5"))));
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
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.GenJingTable_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                GenJingTable_Dialog.this.refreshTable();
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
                GenJingTable_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
