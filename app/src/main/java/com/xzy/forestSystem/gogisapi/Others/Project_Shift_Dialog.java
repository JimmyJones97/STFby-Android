package  com.xzy.forestSystem.gogisapi.Others;

import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.util.Date;
import java.util.Iterator;

public class Project_Shift_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public Project_Shift_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Others.Project_Shift_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                String[] tempPath2;
                if (command.equals("确定")) {
                    String tmpPrjName = Common.GetEditTextValueOnID(Project_Shift_Dialog.this._Dialog, R.id.et_prjShift_Name);
                    if (tmpPrjName.equals("")) {
                        Common.ShowDialog("项目名称不能为空.");
                        return;
                    }
                    boolean tmpBool = false;
                    Iterator<String> tempIter = Common.GetProjectList().iterator();
                    while (true) {
                        if (!tempIter.hasNext()) {
                            break;
                        }
                        String[] tmpStrs = tempIter.next().split(",");
                        if (tmpStrs != null && tmpStrs.length > 0 && tmpPrjName.equals(tmpStrs[0])) {
                            tmpBool = true;
                            break;
                        }
                    }
                    if (tmpBool) {
                        Common.ShowDialog("项目【" + tmpPrjName + "】已经存在.\r\n请采用其他项目名称.");
                        return;
                    }
                    boolean tmpBool2 = false;
                    try {
                        String tmpPrjPath = Common.GetEditTextValueOnID(Project_Shift_Dialog.this._Dialog, R.id.editText1);
                        String tmpDBPath = Common.GetEditTextValueOnID(Project_Shift_Dialog.this._Dialog, R.id.editText2);
                        File tmpFile = new File(tmpPrjPath);
                        File tmpFile2 = new File(tmpDBPath);
                        if (!tmpFile.exists() || !tmpFile2.exists()) {
                            Common.ShowDialog("项目文件或项目数据文件不存在.");
                            return;
                        }
                        String tmpNewFolder = String.valueOf(PubVar.m_SystemPath) + "/Data/" + tmpPrjName;
                        if (!Common.CheckExistFile(tmpNewFolder) && new File(tmpNewFolder).mkdirs()) {
                            Common.CopyFile(tmpDBPath, String.valueOf(tmpNewFolder) + "/TAData.dbx");
                            Common.CopyFile(tmpPrjPath, String.valueOf(tmpNewFolder) + "/Project.dbx");
                            String tempTimeStr = String.valueOf(new Date().getTime());
                            PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().ExecuteSQL("Replace Into T_Project (ProjectName,CreateTime,ModifyTime) Values ('" + tmpPrjName + "','" + tempTimeStr + "','" + tempTimeStr + "')");
                            tmpBool2 = true;
                            SQLiteDBHelper tmpSQLite = new SQLiteDBHelper(String.valueOf(tmpNewFolder) + "/Project.dbx");
                            if (tmpSQLite != null) {
                                tmpSQLite.ExecuteSQL("Update T_Project Set ProjectName='" + tmpPrjName + "' Where 1=1");
                            }
                        }
                        if (tmpBool2) {
                            Common.ShowDialog("项目转移成功.");
                            Project_Shift_Dialog.this._Dialog.dismiss();
                        }
                    } catch (Exception e) {
                    }
                } else if (!command.contains("选择文件")) {
                } else {
                    if (command.contains("选择文件_项目文件")) {
                        String[] tempPath22 = object.toString().split(";");
                        if (tempPath22 != null && tempPath22.length > 1) {
                            Common.SetEditTextValueOnID(Project_Shift_Dialog.this._Dialog, R.id.editText1, tempPath22[1]);
                            String tmpPath3 = Common.GetEditTextValueOnID(Project_Shift_Dialog.this._Dialog, R.id.editText2);
                            if (tmpPath3 == null || tmpPath3.equals("")) {
                                String tmpPath32 = tempPath22[1];
                                Common.SetEditTextValueOnID(Project_Shift_Dialog.this._Dialog, R.id.editText2, String.valueOf(tmpPath32.substring(0, tmpPath32.lastIndexOf(FileSelector_Dialog.sRoot))) + "/TADATA" + tmpPath32.substring(tmpPath32.lastIndexOf(FileSelector_Dialog.sFolder)));
                            }
                            Project_Shift_Dialog.this.refreshProjectInfo();
                        }
                    } else if (command.contains("选择文件_项目数据") && (tempPath2 = object.toString().split(";")) != null && tempPath2.length > 1) {
                        Common.SetEditTextValueOnID(Project_Shift_Dialog.this._Dialog, R.id.editText2, tempPath2[1]);
                        Project_Shift_Dialog.this.refreshProjectInfo();
                    }
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.project_shift_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("项目数据转移");
        this._Dialog.SetHeadButtons("1,2130837858,转移,确定", this.pCallback);
        this._Dialog.findViewById(R.id.imgtxt_projectPath).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.imgtxt_projectDataPath).setOnClickListener(new ViewClick());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshProjectInfo() {
        SQLiteReader tmpReader2;
        SQLiteReader tmpReader22;
        SQLiteReader tmpReader23;
        SQLiteReader tmpReader3;
        StringBuilder tmpSB = new StringBuilder();
        try {
            String tmpPrjPath = Common.GetEditTextValueOnID(this._Dialog, R.id.editText1);
            File tmpFile = new File(tmpPrjPath);
            String tmpDBPath = Common.GetEditTextValueOnID(this._Dialog, R.id.editText2);
            if (tmpFile.exists()) {
                SQLiteDBHelper tmpSQLite = new SQLiteDBHelper(tmpPrjPath);
                SQLiteDBHelper tmpSQLite2 = null;
                if (!tmpDBPath.equals("") && new File(tmpDBPath).exists()) {
                    tmpSQLite2 = new SQLiteDBHelper(tmpDBPath);
                }
                SQLiteReader tmpReader = tmpSQLite.Query("Select ProjectName,CreateTime,CoorSystem,TransMethod From T_Project order by ID ASC limit 1");
                if (tmpReader != null && tmpReader.Read()) {
                    tmpSB.append("项目名称:" + tmpReader.GetString(0));
                    tmpSB.append("\r\n创建时间:" + tmpReader.GetString(1));
                    tmpSB.append("\r\n坐标系统:" + tmpReader.GetString(2));
                    tmpSB.append("\r\n转换方法:" + tmpReader.GetString(3));
                    tmpReader.Close();
                }
                int tmpCount = 0;
                SQLiteReader tmpReader4 = tmpSQLite.Query("Select COUNT(*) From T_Layer");
                if (tmpReader4 != null && tmpReader4.Read()) {
                    tmpCount = tmpReader4.GetInt32(0);
                    tmpSB.append("\r\n\r\n【采集图层】     图层数目: " + String.valueOf(tmpCount));
                    tmpReader4.Close();
                }
                if (tmpCount > 0 && (tmpReader23 = tmpSQLite.Query("Select Name,Type,LayerID From T_Layer")) != null) {
                    while (tmpReader23.Read()) {
                        tmpSB.append("\r\n     [" + tmpReader23.GetString(0) + "]:" + tmpReader23.GetString(1));
                        String tmpLayerID = tmpReader23.GetString(2);
                        if (!(tmpSQLite2 == null || (tmpReader3 = tmpSQLite2.Query("Select COUNT(*) From " + tmpLayerID + "_D")) == null || !tmpReader3.Read())) {
                            tmpSB.append("  " + String.valueOf(tmpReader3.GetInt32(0)) + "个.");
                            tmpReader3.Close();
                        }
                    }
                    tmpReader23.Close();
                }
                int tmpCount2 = 0;
                SQLiteReader tmpReader5 = tmpSQLite.Query("Select COUNT(*) From T_BKVectorLayer");
                if (tmpReader5 != null && tmpReader5.Read()) {
                    tmpCount2 = tmpReader5.GetInt32(0);
                    tmpSB.append("\r\n\r\n【矢量背景图层】     图层数目: " + String.valueOf(tmpCount2));
                    tmpReader5.Close();
                }
                if (tmpCount2 > 0 && (tmpReader22 = tmpSQLite.Query("Select Name,Type From T_BKVectorLayer")) != null) {
                    while (tmpReader22.Read()) {
                        tmpSB.append("\r\n     [" + tmpReader22.GetString(0) + "]:" + tmpReader22.GetString(1));
                    }
                    tmpReader22.Close();
                }
                int tmpCount3 = 0;
                SQLiteReader tmpReader6 = tmpSQLite.Query("Select COUNT(*) From T_RasterLayer");
                if (tmpReader6 != null && tmpReader6.Read()) {
                    tmpCount3 = tmpReader6.GetInt32(0);
                    tmpSB.append("\r\n【栅格图层】     图层数目: " + String.valueOf(tmpCount3));
                    tmpReader6.Close();
                }
                if (tmpCount3 > 0 && (tmpReader2 = tmpSQLite.Query("Select Name From T_RasterLayer")) != null) {
                    while (tmpReader2.Read()) {
                        tmpSB.append("\r\n     [" + tmpReader2.GetString(0) + "]");
                    }
                    tmpReader2.Close();
                }
                if (tmpSQLite2 != null) {
                    tmpSQLite2.Close();
                }
                tmpSQLite.Close();
            }
        } catch (Exception e) {
        }
        Common.SetTextViewValueOnID(this._Dialog, (int) R.id.textView1, tmpSB.toString());
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("项目文件路径")) {
            FileSelector_Dialog tempDialog = new FileSelector_Dialog("", false);
            Common.ShowToast("请选择项目文件(Project.db等).");
            tempDialog.SetCallback(this.pCallback);
            tempDialog.SetTag("项目文件");
            tempDialog.ShowDialog();
        } else if (command.equals("项目数据路径")) {
            FileSelector_Dialog tempDialog2 = new FileSelector_Dialog("", false);
            Common.ShowToast("请选择项目数据文件(TAData.db等).");
            tempDialog2.SetCallback(this.pCallback);
            tempDialog2.SetTag("项目数据");
            tempDialog2.ShowDialog();
        }
    }

    public void ShowDialog() {
        this._Dialog.show();
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                Project_Shift_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
