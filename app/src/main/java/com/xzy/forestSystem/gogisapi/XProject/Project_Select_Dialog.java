package  com.xzy.forestSystem.gogisapi.XProject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.HashValueObject;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Encryption.ENCRYPT_SET_TYPE;
import  com.xzy.forestSystem.gogisapi.Encryption.Encrypt_Setting_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.System.AuthorizeTools;
import  com.xzy.forestSystem.gogisapi.Tools.ToolsManager_Dialog;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Project_Select_Dialog {
    private XDialogTemplate _Dialog;
    private Button _LockButton;
    private Button _LockModifyButton;
    private MyTableFactory _MyTableFactory;
    private LinearLayout _ll_project_lock;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private Drawable m_prjLockDw;
    private Drawable m_prjUnockDw;
    private ICallback pCallback;

    @SuppressLint("WrongConstant")
    public Project_Select_Dialog() {
        this._Dialog = null;
        this.m_MyTableDataList = null;
        this._MyTableFactory = null;
        this.m_prjLockDw = null;
        this.m_prjUnockDw = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, final Object pObject) {
                int tmpI;
                int tmpI2;
                try {
                    if (paramString.equals("新建项目")) {
                        if (PubVar.m_IsEnable) {
                            Project_Create_Dialog tmpDialog = new Project_Create_Dialog();
                            tmpDialog.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.1.1
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String paramString2, Object pObject2) {
                                    Project_Select_Dialog.this.LoadProjectListInfo();
                                }
                            });
                            tmpDialog.ShowDialog();
                        }
                    } else if (paramString.equals("打开项目")) {
                        if (PubVar.m_IsEnable) {
                            Common.ShowProgressDialog("正在打开项目【" + pObject.toString() + "】,请稍候...", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.1.2
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String paramString2, Object pObject2) {
                                    try {
                                        Project_Select_Dialog.this.OpenProject(pObject.toString());
                                        if (pObject != null && (pObject instanceof Handler)) {
                                            Handler tmpHandler = (Handler) pObject;
                                            Message tmpMsg = tmpHandler.obtainMessage();
                                            tmpMsg.what = 0;
                                            tmpHandler.sendMessage(tmpMsg);
                                        }
                                    } catch (Exception e) {
                                        if (pObject != null && (pObject instanceof Handler)) {
                                            Handler tmpHandler2 = (Handler) pObject;
                                            Message tmpMsg2 = tmpHandler2.obtainMessage();
                                            tmpMsg2.what = 0;
                                            tmpHandler2.sendMessage(tmpMsg2);
                                        }
                                    } catch (Throwable th) {
                                        if (pObject != null && (pObject instanceof Handler)) {
                                            Handler tmpHandler3 = (Handler) pObject;
                                            Message tmpMsg3 = tmpHandler3.obtainMessage();
                                            tmpMsg3.what = 0;
                                            tmpHandler3.sendMessage(tmpMsg3);
                                        }
                                        throw th;
                                    }
                                }
                            });
                            Project_Select_Dialog.this._Dialog.dismiss();
                        }
                    } else if (paramString.equals("删除项目")) {
                        if (PubVar.m_IsEnable) {
                            final ArrayList localArrayList = new ArrayList();
                            for (HashMap tmpHashMap : Project_Select_Dialog.this.m_MyTableDataList) {
                                if (Boolean.parseBoolean(tmpHashMap.get("D1").toString())) {
                                    localArrayList.add(tmpHashMap.get("D2").toString());
                                }
                            }
                            if (localArrayList.size() > 0) {
                                Common.ShowYesNoDialog(Project_Select_Dialog.this._Dialog.getContext(), "是否确定要删除以下项目？\r\n项目名称：" + Common.CombineStrings("\r\n项目名称：", localArrayList), new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.1.3
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String paramString2, Object pObject2) {
                                        if (paramString2.equals("YES")) {
                                            Iterator localIterator = localArrayList.iterator();
                                            String str1 = String.valueOf(PubVar.m_SystemPath) + "/Data";
                                            while (localIterator.hasNext()) {
                                                Common.DeleteAll(new File(String.valueOf(str1) + FileSelector_Dialog.sRoot + ((String) localIterator.next())));
                                            }
                                            HashMap tmpHashMap2 = new HashMap();
                                            tmpHashMap2.put("F2", "");
                                            tmpHashMap2.put("F3", "");
                                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_BeforeOpenProject", (HashMap<String, String>) tmpHashMap2);
                                            PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().ExecuteSQL("Delete From T_Project Where ProjectName in ('" + Common.CombineStrings("','", localArrayList) + "')");
                                            Project_Select_Dialog.this.LoadProjectListInfo();
                                        }
                                    }
                                });
                            } else {
                                Common.ShowDialog(Project_Select_Dialog.this._Dialog.getContext(), "请勾选需要删除的项目！");
                            }
                        }
                    } else if (paramString.contains("项目加密返回")) {
                        String[] tmpStrs = paramString.split("_");
                        if (tmpStrs != null && tmpStrs.length > 1 && (tmpI2 = Integer.parseInt(tmpStrs[1])) >= 0 && tmpI2 < Project_Select_Dialog.this.m_MyTableDataList.size()) {
                            ((HashMap) Project_Select_Dialog.this.m_MyTableDataList.get(tmpI2)).put("D5", true);
                            Project_Select_Dialog.this._MyTableFactory.notifyDataSetChanged();
                        }
                    } else if (paramString.contains("项目解除密码返回")) {
                        String[] tmpStrs2 = paramString.split("_");
                        if (tmpStrs2 != null && tmpStrs2.length > 1 && (tmpI = Integer.parseInt(tmpStrs2[1])) >= 0 && tmpI < Project_Select_Dialog.this.m_MyTableDataList.size()) {
                            ((HashMap) Project_Select_Dialog.this.m_MyTableDataList.get(tmpI)).put("D5", false);
                            Project_Select_Dialog.this._MyTableFactory.notifyDataSetChanged();
                        }
                    } else if (paramString.equals("返回")) {
                        if (PubVar.m_Workspace == null) {
                            Common.ShowYesNoDialog(PubVar.MainContext, "没有打开任何项目,请先选择需要打开的项目.\r\n如果退出系统,请点击\"确定\".", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.1.4
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String paramString2, Object pObject2) {
                                    if (paramString2.equals("YES")) {
                                        PubVar.m_Callback.OnClick("强行退出系统", null);
                                    }
                                }
                            });
                        } else {
                            Project_Select_Dialog.this._Dialog.dismiss();
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        this._ll_project_lock = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.project_select);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.findViewById(R.id.ll_currentproject).setVisibility(8);
        this._Dialog.setCancelable(false);
        this._Dialog.SetAllowedCloseDialog(false);
        this._Dialog.SetCallback(this.pCallback);
        HashValueObject localHashValueObject = PubVar._ComHashMap.GetValue("Project");
        if (localHashValueObject != null) {
            this._Dialog.findViewById(R.id.ll_currentproject).setVisibility(0);
            Common.SetTextViewValueOnID(this._Dialog, (int) R.id.bt_currentproject, " " + localHashValueObject.Value);
            this._Dialog.findViewById(R.id.bt_currentproject).setTag(localHashValueObject.Value);
            this._Dialog.findViewById(R.id.bt_currentproject).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.2
                @Override // android.view.View.OnClickListener
                public void onClick(View paramView) {
                    HashMap tmpHashMap = new HashMap();
                    tmpHashMap.put("Name", paramView.getTag().toString());
                    tmpHashMap.put("CreateTime", "");
                    Project_DetailInfo_Dialog tempPrjOpen = new Project_DetailInfo_Dialog();
                    tempPrjOpen.SetProject(tmpHashMap);
                    tempPrjOpen.SetCallback(Project_Select_Dialog.this.pCallback);
                    tempPrjOpen.ShowDialog();
                }
            });
        }
        this._Dialog.SetCaption("项目管理");
        this._Dialog.SetHeadButtons("1,2130837508,新建,新建项目", this.pCallback);
        this._LockButton = (Button) this._Dialog.findViewById(R.id.bt_project_Lock);
        this._LockModifyButton = (Button) this._Dialog.findViewById(R.id.bt_project_lockEdit);
        this._LockButton.setOnClickListener(new ViewClick());
        this._LockModifyButton.setOnClickListener(new ViewClick());
        this._ll_project_lock = (LinearLayout) this._Dialog.findViewById(R.id.ll_project_lock);
        Resources res = PubVar.MainContext.getResources();
        this.m_prjLockDw = res.getDrawable(R.drawable.lock48);
        this.m_prjLockDw.setBounds(0, 0, this.m_prjLockDw.getMinimumWidth(), this.m_prjLockDw.getMinimumHeight());
        this.m_prjUnockDw = res.getDrawable(R.drawable.unlock48);
        this.m_prjUnockDw.setBounds(0, 0, this.m_prjUnockDw.getMinimumWidth(), this.m_prjUnockDw.getMinimumHeight());
        this._Dialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.3
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode != 82 || event.getAction() != 0) {
                    return Project_Select_Dialog.this._Dialog.onKeyDown(keyCode, event);
                }
                ToolsManager_Dialog tempDialog = new ToolsManager_Dialog();
                tempDialog.SetToolboxType(1);
                tempDialog.ShowDialog();
                return true;
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void LoadProjectListInfo() {
        String tmpPrjCreateTime = null;
        try {
            this._MyTableFactory = new MyTableFactory();
            this._MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.prj_list), "自定义", "选择,项目名称,创建时间,操作", "checkbox,text,text,image", new int[]{-15, -35, -35, -15}, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.4
                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                public void OnClick(String paramString, Object pObject) {
                    if (paramString.equals("列表选项")) {
                        Project_Select_Dialog.this.ShowDataPreview((HashMap) pObject);
                    }
                }
            });
            List<String> localList = Common.GetProjectList();
            String str1 = "";
            HashValueObject localHashValueObject = PubVar._ComHashMap.GetValue("Project");
            if (localHashValueObject != null) {
                str1 = localHashValueObject.Value;
            }
            this.m_MyTableDataList = new ArrayList();
            for (String str2 : localList) {
                boolean tmpIsLock = false;
                if (!str1.equals(str2.split(",")[0])) {
                    String[] tmpStr2s = str2.split(",");
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    try {
                        tmpPrjCreateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(tmpStr2s[1])));
                    } catch (Exception e) {
                    }
                    if (tmpStr2s.length > 2 && tmpStr2s[2] != null && !tmpStr2s[2].equals("")) {
                        tmpIsLock = true;
                    }
                    HashMap tmpHashMap = new HashMap();
                    tmpHashMap.put("D1", false);
                    tmpHashMap.put("D2", str2.split(",")[0]);
                    tmpHashMap.put("D3", tmpPrjCreateTime);
                    tmpHashMap.put("D4", BitmapFactory.decodeResource(PubVar._PubCommand.m_Context.getResources(), R.drawable.deleteobject));
                    tmpHashMap.put("D5", Boolean.valueOf(tmpIsLock));
                    this.m_MyTableDataList.add(tmpHashMap);
                }
            }
            this._MyTableFactory.BindDataToListView(this.m_MyTableDataList, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.5
                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                public void OnClick(String paramString, Object pObject) {
                    String tmpStr;
                    String[] tmpStrs;
                    if (paramString.equals("单击项")) {
                        Project_Select_Dialog.this.OpenDetailProjectInfo((HashMap) pObject);
                        Project_Select_Dialog.this.ShowDataPreview((HashMap) pObject);
                    } else if (paramString.equals("单击选择行")) {
                        final String tempPrjName = (String) ((HashMap) pObject).get("D2");
                        Common.ShowYesNoDialog(Project_Select_Dialog.this._Dialog.getContext(), "是否打开以下项目?\r\n【" + tempPrjName + "】", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.5.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject2) {
                                if (paramString2.equals("YES")) {
                                    final String str = tempPrjName;
                                    Common.ShowProgressDialog("正在打开项目【" + tempPrjName + "】,请稍候...", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.5.1.1
                                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                        public void OnClick(String paramString3, Object pObject3) {
                                            try {
                                                Project_Select_Dialog.this.OpenProject(str);
                                                Project_Select_Dialog.this._Dialog.dismiss();
                                                if (pObject3 != null && (pObject3 instanceof Handler)) {
                                                    Handler tmpHandler = (Handler) pObject3;
                                                    Message tmpMsg = tmpHandler.obtainMessage();
                                                    tmpMsg.what = 0;
                                                    tmpHandler.sendMessage(tmpMsg);
                                                }
                                            } catch (Exception e2) {
                                                if (pObject3 != null && (pObject3 instanceof Handler)) {
                                                    Handler tmpHandler2 = (Handler) pObject3;
                                                    Message tmpMsg2 = tmpHandler2.obtainMessage();
                                                    tmpMsg2.what = 0;
                                                    tmpHandler2.sendMessage(tmpMsg2);
                                                }
                                            } catch (Throwable th) {
                                                if (pObject3 != null && (pObject3 instanceof Handler)) {
                                                    Handler tmpHandler3 = (Handler) pObject3;
                                                    Message tmpMsg3 = tmpHandler3.obtainMessage();
                                                    tmpMsg3.what = 0;
                                                    tmpHandler3.sendMessage(tmpMsg3);
                                                }
                                                throw th;
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    } else if (paramString.equals("单击单元格") && (tmpStr = String.valueOf(pObject)) != null && !tmpStr.equals("") && (tmpStrs = tmpStr.split(",")) != null && tmpStrs.length > 1 && Integer.parseInt(tmpStrs[1]) == 3) {
                        Project_Select_Dialog.this.pCallback.OnClick("删除项目", null);
                    }
                }
            });
            this._MyTableFactory.SetClickItemReturnColumns("3,");
        } catch (Exception ex) {
            Log.v("LoadProjectListInfo", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void OpenDetailProjectInfo(HashMap<String, Object> paramHashMap) {
        HashMap tmpHashMap = new HashMap();
        tmpHashMap.put("Name", paramHashMap.get("D2").toString());
        tmpHashMap.put("CreateTime", paramHashMap.get("D3").toString());
        Project_DetailInfo_Dialog tempproject_open = new Project_DetailInfo_Dialog();
        tempproject_open.SetProject(tmpHashMap);
        tempproject_open.SetCallback(this.pCallback);
        tempproject_open.ShowDialog();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean OpenProject(String projectName) {
        HashValueObject localHashValueObject = new HashValueObject();
        localHashValueObject.Value = projectName;
        PubVar._ComHashMap.AddValue("Project", localHashValueObject);
        SQLiteReader tempReader = PubVar._PubCommand.m_UserConfigDB.GetSQLiteDatabase().Query("Select Security From T_Project Where ProjectName='" + projectName + "'");
        if (tempReader != null && tempReader.Read()) {
            String tmpSecurity = tempReader.GetString(0);
            tempReader.Close();
            if (tmpSecurity != null && !tmpSecurity.equals("")) {
                BasicValue tmpMsg = new BasicValue();
                BasicValue tmpKey = new BasicValue();
                if (AuthorizeTools.DecryptPrjPwd(tmpSecurity, tmpMsg, tmpKey)) {
                    PubVar._PubCommand.m_ProjectDB.SetPassword(tmpKey.getString());
                } else {
                    Common.ShowDialog(tmpMsg.getString());
                    return false;
                }
            }
        }
        if (PubVar._PubCommand.m_ProjectDB.OpenProject(projectName)) {
            PubVar._PubCommand.ProcessCommand("项目_打开");
        }
        return true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    @SuppressLint("WrongConstant")
    private void ShowDataPreview(final HashMap<String, Object> paramHashMap) {
        if (paramHashMap != null) {
            String tempPrjName = (String) paramHashMap.get("D2");
            Button localImageButton1 = (Button) this._Dialog.findViewById(R.id.bt_openproject);
            localImageButton1.setVisibility(0);
            Button localImageButton2 = (Button) this._Dialog.findViewById(R.id.bt_projectinfo);
            localImageButton2.setVisibility(0);
            Button localImageButton3 = (Button) this._Dialog.findViewById(R.id.bt_fullscreen);
            localImageButton3.setVisibility(4);
            ((TextView) this._Dialog.findViewById(R.id.tv_datapreview)).setText("【" + tempPrjName + "】数据预览图:");
            ImageView localImageView = (ImageView) this._Dialog.findViewById(R.id.iv_datapreview);
            localImageView.setEnabled(false);
            localImageView.setImageBitmap(null);
            final String tempPrjImg = String.valueOf(PubVar.m_SystemPath) + "/Data/" + tempPrjName + "/DataPreview.jpg";
            if (Common.CheckExistFile(tempPrjImg)) {
                localImageView.setImageBitmap(BitmapFactory.decodeFile(tempPrjImg));
                localImageView.setEnabled(true);
                localImageButton3.setVisibility(0);
            }
            localImageView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.6
                @Override // android.view.View.OnClickListener
                public void onClick(View paramView) {
                    Project_Select_Preview_Dialog localv1_project_select_datapreview = new Project_Select_Preview_Dialog();
                    localv1_project_select_datapreview.SetDataPreviewInfo(paramHashMap, tempPrjImg);
                    localv1_project_select_datapreview.SetCallback(Project_Select_Dialog.this.pCallback);
                    localv1_project_select_datapreview.ShowDialog();
                }
            });
            localImageButton3.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.7
                @Override // android.view.View.OnClickListener
                public void onClick(View paramView) {
                    Project_Select_Preview_Dialog localv1_project_select_datapreview = new Project_Select_Preview_Dialog();
                    localv1_project_select_datapreview.SetDataPreviewInfo(paramHashMap, tempPrjImg);
                    localv1_project_select_datapreview.SetCallback(Project_Select_Dialog.this.pCallback);
                    localv1_project_select_datapreview.ShowDialog();
                }
            });
            localImageButton2.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.8
                @Override // android.view.View.OnClickListener
                public void onClick(View paramView) {
                    Project_Select_Dialog.this.OpenDetailProjectInfo(paramHashMap);
                }
            });
            Button tmpOpenSaveBtn = (Button) this._Dialog.findViewById(R.id.bt_openprojectSafe);
            tmpOpenSaveBtn.setVisibility(0);
            localImageButton1.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.9
                @Override // android.view.View.OnClickListener
                public void onClick(View paramView) {
                    Project_Select_Dialog.this.pCallback.OnClick("打开项目", paramHashMap.get("D2"));
                }
            });
            tmpOpenSaveBtn.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.10
                @Override // android.view.View.OnClickListener
                public void onClick(View paramView) {
                    PubVar._PubCommand.SetOpenProjectMode(1);
                    Project_Select_Dialog.this.pCallback.OnClick("打开项目", paramHashMap.get("D2"));
                }
            });
            if (this._ll_project_lock.getVisibility() != 0) {
                this._ll_project_lock.setVisibility(0);
            }
            boolean tmpIsLock = false;
            if (paramHashMap.containsKey("D5")) {
                tmpIsLock = Boolean.parseBoolean(String.valueOf(paramHashMap.get("D5")));
            }
            if (tmpIsLock) {
                this._LockButton.setText("解密");
                this._LockButton.setTag("项目解密");
                this._LockButton.setCompoundDrawables(this.m_prjUnockDw, null, null, null);
                this._LockModifyButton.setVisibility(0);
                return;
            }
            this._LockButton.setText("加密");
            this._LockButton.setTag("项目加密");
            this._LockButton.setCompoundDrawables(this.m_prjLockDw, null, null, null);
            this._LockModifyButton.setVisibility(8);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        LoadProjectListInfo();
        HashMap tmpHashMap2 = null;
        if (PubVar._ComHashMap.GetValue("Tag_BeforeOpenProject") == null) {
            tmpHashMap2 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_BeforeOpenProject");
        }
        if (tmpHashMap2 != null && tmpHashMap2.get("F2") != null && !tmpHashMap2.get("F2").toString().equals("")) {
            String tmpPrjName = tmpHashMap2.get("F2").toString();
            Iterator<HashMap<String, Object>> localIterator = this.m_MyTableDataList.iterator();
            boolean tempBool = false;
            while (true) {
                if (!localIterator.hasNext()) {
                    break;
                }
                HashMap<String, Object> temphash = localIterator.next();
                if (temphash != null && temphash.get("D2").toString().equals(tmpPrjName)) {
                    tempBool = true;
                    break;
                }
            }
            if (tempBool) {
                HashMap finalTmpHashMap = tmpHashMap2;
                Common.ShowYesNoDialog(this._Dialog.getContext(), String.format("是否打开最近打开的项目？\r\n\r\n项目名称：%1$s\r\n最后打开的时间：%2$s", tmpHashMap2.get("F2"), tmpHashMap2.get("F3")), new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.11
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        if (paramString.equals("YES")) {
                            final HashMap hashMap = finalTmpHashMap;
                            Common.ShowProgressDialog("正在打开项目【" + finalTmpHashMap.get("F2").toString() + "】,请稍候...", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.11.1
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String paramString2, Object pObject2) {
                                    try {
                                        Project_Select_Dialog.this.OpenProject((String) hashMap.get("F2"));
                                        Project_Select_Dialog.this._Dialog.dismiss();
                                        if (pObject2 != null && (pObject2 instanceof Handler)) {
                                            Handler tmpHandler = (Handler) pObject2;
                                            Message tmpMsg = tmpHandler.obtainMessage();
                                            tmpMsg.what = 0;
                                            tmpHandler.sendMessage(tmpMsg);
                                        }
                                    } catch (Exception e) {
                                        if (pObject2 != null && (pObject2 instanceof Handler)) {
                                            Handler tmpHandler2 = (Handler) pObject2;
                                            Message tmpMsg2 = tmpHandler2.obtainMessage();
                                            tmpMsg2.what = 0;
                                            tmpHandler2.sendMessage(tmpMsg2);
                                        }
                                    } catch (Throwable th) {
                                        if (pObject2 != null && (pObject2 instanceof Handler)) {
                                            Handler tmpHandler3 = (Handler) pObject2;
                                            Message tmpMsg3 = tmpHandler3.obtainMessage();
                                            tmpMsg3.what = 0;
                                            tmpHandler3.sendMessage(tmpMsg3);
                                        }
                                        throw th;
                                    }
                                }
                            });
                        }
                    }
                });
            }
        } else if (this.m_MyTableDataList.size() == 0 && !PubVar._PubCommand.m_ProjectDB.AlwaysOpenProject()) {
            PubVar.m_AuthorizeTools.getRegisterMode();
            Project_Create_Dialog.CreateProject("默认在线地图", "WGS-84坐标", "系统默认图层模板", new String[1]);
            LoadProjectListInfo();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("项目加密")) {
                int tmpIndex = this._MyTableFactory.getSelectItemIndex();
                if (tmpIndex >= 0) {
                    String tmpPrjName = String.valueOf(this.m_MyTableDataList.get(tmpIndex).get("D2"));
                    if (!tmpPrjName.equals("")) {
                        Encrypt_Setting_Dialog tmpDialog = new Encrypt_Setting_Dialog(tmpPrjName, String.valueOf(PubVar.m_SystemPath) + "/Data/" + tmpPrjName, ENCRYPT_SET_TYPE.Create);
                        tmpDialog.SetReturnTag("项目加密返回_" + tmpIndex);
                        tmpDialog.SetCallback(this.pCallback);
                        tmpDialog.ShowDialog();
                        return;
                    }
                    return;
                }
                Common.ShowDialog("请选择需要加密的项目.");
            } else if (command.equals("项目修改密码")) {
                int tmpIndex2 = this._MyTableFactory.getSelectItemIndex();
                if (tmpIndex2 >= 0) {
                    String tmpPrjName2 = String.valueOf(this.m_MyTableDataList.get(tmpIndex2).get("D2"));
                    if (!tmpPrjName2.equals("")) {
                        new Encrypt_Setting_Dialog(tmpPrjName2, String.valueOf(PubVar.m_SystemPath) + "/Data/" + tmpPrjName2, ENCRYPT_SET_TYPE.Modify).ShowDialog();
                        return;
                    }
                    return;
                }
                Common.ShowDialog("请选择需要加密的项目.");
            } else if (command.equals("项目解密")) {
                int tmpIndex3 = this._MyTableFactory.getSelectItemIndex();
                if (tmpIndex3 >= 0) {
                    String tmpPrjName3 = String.valueOf(this.m_MyTableDataList.get(tmpIndex3).get("D2"));
                    if (!tmpPrjName3.equals("")) {
                        Encrypt_Setting_Dialog tmpDialog2 = new Encrypt_Setting_Dialog(tmpPrjName3, String.valueOf(PubVar.m_SystemPath) + "/Data/" + tmpPrjName3, ENCRYPT_SET_TYPE.Remove);
                        tmpDialog2.SetReturnTag("项目解除密码返回_" + tmpIndex3);
                        tmpDialog2.SetCallback(this.pCallback);
                        tmpDialog2.ShowDialog();
                        return;
                    }
                    return;
                }
                Common.ShowDialog("请选择需要加密的项目.");
            } else if (command.equals("打开系统工具箱")) {
                ToolsManager_Dialog tmpDialog3 = new ToolsManager_Dialog();
                tmpDialog3.SetToolboxType(1);
                tmpDialog3.ShowDialog();
            }
        } catch (Exception ex) {
            Common.Log("错误", ex.getMessage());
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Project_Select_Dialog.12
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Project_Select_Dialog.this.refreshLayout();
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
                Project_Select_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
