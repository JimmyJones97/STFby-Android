package  com.xzy.forestSystem.gogisapi.Tools;

import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.ZipUtil;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BakPorject_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private String m_CurrentPrjNameString;
    private boolean m_IsBak;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public BakPorject_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this.m_CurrentPrjNameString = "";
        this.m_IsBak = false;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.BakPorject_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("列表选项") && object != null && (object instanceof HashMap)) {
                    final String tmpFilePathString = String.valueOf(((HashMap) object).get("Path"));
                    String tmpMsg = "是否恢复至该备份文件?\r\n";
                    if (!BakPorject_Dialog.this.m_IsBak) {
                        tmpMsg = String.valueOf(tmpMsg) + "请先备份当前项目,否则将无法恢复至当前状态,请谨慎操作!";
                    }
                    Common.ShowYesNoDialogWithAlert(BakPorject_Dialog.this._Dialog.getContext(), tmpMsg, true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.BakPorject_Dialog.1.1
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (paramString.equals("YES")) {
                                try {
                                    ZipUtil.unZipFolder(tmpFilePathString, String.valueOf(PubVar.m_SystemPath) + "/Data");
                                    Common.ShowDialog("备份还原成功!\r\n建议退出软件后重新打开该项目!");
                                } catch (Exception e) {
                                    Common.Log("备份还原错误", e.getLocalizedMessage());
                                }
                            }
                        }
                    });
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.bakproject_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("项目备份管理");
        this._Dialog.findViewById(R.id.btn_BakPrj_add).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_BakPrj_delete).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectAll).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectDe).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_BakPrj_allPrjList).setOnClickListener(new ViewClick());
        this._Dialog.SetCallback(this.pCallback);
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_bakPrj_datalist), "自定义", "选择,备份时间,文件大小", "checkbox,text,text", new int[]{-15, -55, -30}, this.pCallback);
        this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
    }

    public void setCurrentPrjNameString(String prjName) {
        this.m_CurrentPrjNameString = prjName;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshTable() {
        try {
            this.m_MyTableDataList.clear();
            File[] arrayOfFile = new File(String.valueOf(PubVar.m_SystemPath) + "/备份").listFiles();
            if (arrayOfFile != null && arrayOfFile.length > 0) {
                for (File tmpfile : arrayOfFile) {
                    int tmpI = tmpfile.getName().indexOf(this.m_CurrentPrjNameString);
                    if (tmpI >= 0) {
                        HashMap tmpHash = new HashMap();
                        tmpHash.put("D1", false);
                        String tmpString = tmpfile.getName().substring(this.m_CurrentPrjNameString.length() + tmpI);
                        tmpHash.put("D2", Common.dateFormat.format(new Date(Long.parseLong(tmpString.substring(0, tmpString.length() - 4)))));
                        tmpHash.put("D3", Common.SimplifyFileSize((double) (tmpfile.length() / 1024)));
                        tmpHash.put("Path", tmpfile.getAbsolutePath());
                        this.m_MyTableDataList.add(tmpHash);
                    }
                }
            }
            this.m_MyTableFactory.notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("备份")) {
                if (!this.m_CurrentPrjNameString.equals("")) {
                    try {
                        ZipUtil.zipFolder(Common.GetProjectPath(this.m_CurrentPrjNameString), String.valueOf(PubVar.m_SystemPath) + "/备份/" + this.m_CurrentPrjNameString + String.valueOf(new Date().getTime()) + ".zip");
                        Common.ShowDialog("备份成功!");
                        this.m_IsBak = true;
                        refreshTable();
                    } catch (Exception e) {
                        Common.Log("备份错误", e.getLocalizedMessage());
                    }
                }
            } else if (command.equals("删除")) {
                final List<Object> tmpList = new ArrayList<>();
                for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
                    if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                        tmpList.add(tmpHash.get("Path"));
                    }
                }
                if (tmpList.size() > 0) {
                    Common.ShowYesNoDialogWithAlert(this._Dialog.getContext(), "是否删除选择的" + String.valueOf(tmpList.size()) + "个备份文件?\r\n删除后将不可恢复,请谨慎操作!", true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.BakPorject_Dialog.2
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            for (int i = tmpList.size() - 1; i > -1; i--) {
                                try {
                                    Object tmpObject = tmpList.get(i);
                                    if (tmpObject != null) {
                                        File tmpFile = new File(String.valueOf(tmpObject));
                                        if (tmpFile.exists()) {
                                            tmpFile.delete();
                                        }
                                    }
                                } catch (Exception e2) {
                                }
                            }
                            BakPorject_Dialog.this.refreshTable();
                        }
                    });
                } else {
                    Common.ShowDialog("没有勾选任何备份文件.");
                }
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
            } else if (command.equals("所有项目")) {
                String tmpString = this.m_CurrentPrjNameString;
                this.m_CurrentPrjNameString = "";
                refreshTable();
                this.m_CurrentPrjNameString = tmpString;
            }
        } catch (Exception e2) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.BakPorject_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                BakPorject_Dialog.this.refreshTable();
                if (BakPorject_Dialog.this.m_CurrentPrjNameString.equals("")) {
                    BakPorject_Dialog.this._Dialog.findViewById(R.id.btn_BakPrj_add).setVisibility(8);
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
                BakPorject_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
