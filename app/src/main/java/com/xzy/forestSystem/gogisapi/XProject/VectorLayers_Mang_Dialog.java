package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import androidx.core.internal.view.SupportMenu;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Encryption.DataAuthority;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSource;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.Input_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.System.AuthorizeTools;
import com.stczh.gzforestSystem.R;
import com.xzy.forestSystem.hellocharts.animation.ChartViewportAnimator;
import com.xzy.forestSystem.hellocharts.animation.PieChartRotationAnimator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VectorLayers_Mang_Dialog {
    private XDialogTemplate _Dialog;
    private boolean _NeedReturn;
    private int m_AllowInputPwdCount;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_TableFactory;
    private String m_TempSelectedFile;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public VectorLayers_Mang_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_TableFactory = null;
        this._NeedReturn = false;
        this.m_TempSelectedFile = "";
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_Mang_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                HashMap temphash;
                HashMap temphash2;
                if (paramString.equals("??????")) {
                    if (VectorLayers_Mang_Dialog.this._NeedReturn) {
                        PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL("Delete From T_BKVectorLayer");
                        if (VectorLayers_Mang_Dialog.this.m_MyTableDataList.size() > 0) {
                            DataAuthority tmpDataAuthority = new DataAuthority(1);
                            tmpDataAuthority.SetIsEncrypt(true);
                            String tmpSYSKey = tmpDataAuthority.getDataKey();
                            StringBuilder tempOutMsgSB = new StringBuilder();
                            StringBuilder tempReturnSB = new StringBuilder();
                            int tempTid = 0;
                            for (HashMap<String, Object> tempHash : VectorLayers_Mang_Dialog.this.m_MyTableDataList) {
                                if (tempHash != null) {
                                    String tempPath = tempHash.get("D3").toString();
                                    if (Common.CheckExistFile(tempPath)) {
                                        String tempLayerIDs = tempHash.get("D11").toString();
                                        String tempBiasX = tempHash.get("D5").toString();
                                        String tempBiasY = tempHash.get("D6").toString();
                                        String tempEditTYPE = tempHash.get("D12").toString();
                                        String tempIsEncrypt = "false";
                                        if (tempHash.containsKey("IsEncrypt")) {
                                            tempIsEncrypt = tempHash.get("IsEncrypt").toString().toLowerCase();
                                        }
                                        String tempAccessPwd = tempHash.get("Security").toString();
                                        if (!tempAccessPwd.equals("")) {
                                            tempAccessPwd = AuthorizeTools.EncryptExpandLayerKey(tempAccessPwd);
                                        }
                                        if (tempEditTYPE.equals("EDIT2") || tempEditTYPE.equals("EDIT3")) {
                                            tempReturnSB.append(String.valueOf(tempPath) + ";" + tempLayerIDs + ";" + tempHash.get("D15").toString() + ";" + tempHash.get("D16").toString());
                                        }
                                        String tempLayerIDs2 = tempLayerIDs.replaceAll(",", "','");
                                        String tmpKey = "";
                                        if (tempIsEncrypt.equals("true")) {
                                            tmpKey = tmpSYSKey;
                                        } else {
                                            tempIsEncrypt = "false";
                                        }
                                        SQLiteDBHelper tmpSqlDatabase = new SQLiteDBHelper(tempPath, tmpKey);
                                        SQLiteReader tempReader = tmpSqlDatabase.Query("Select LayerID,Name,Type,Visible,Transparent,MinX,MinY,MaxX,MaxY From T_Layer Where LayerID in ('" + tempLayerIDs2 + "') Order By ID DESC");
                                        if (tempReader != null) {
                                            while (tempReader.Read()) {
                                                StringBuilder tempSqlSB = new StringBuilder();
                                                tempSqlSB.append("Insert Into T_BKVectorLayer (LayerID,Name,SortID,Type,Path,Visible,Transparent,MinX,MinY,MaxX,MaxY,F1,F2,F3,Security) Values (");
                                                tempSqlSB.append("'" + tempReader.GetString(0) + "','");
                                                tempSqlSB.append(String.valueOf(tempReader.GetString(1)) + "',");
                                                tempSqlSB.append(String.valueOf(tempTid) + ",'");
                                                tempSqlSB.append(String.valueOf(tempReader.GetString(2)) + "','");
                                                tempSqlSB.append(String.valueOf(tempPath) + "','");
                                                tempSqlSB.append(String.valueOf(tempReader.GetString(3)) + "','");
                                                tempSqlSB.append(String.valueOf(tempReader.GetString(4)) + "','");
                                                tempSqlSB.append(String.valueOf(tempReader.GetDouble(5)) + "','");
                                                tempSqlSB.append(String.valueOf(tempReader.GetDouble(6)) + "','");
                                                tempSqlSB.append(String.valueOf(tempReader.GetDouble(7)) + "','");
                                                tempSqlSB.append(String.valueOf(tempReader.GetDouble(8)) + "','");
                                                tempSqlSB.append(String.valueOf(tempBiasX) + "','");
                                                tempSqlSB.append(String.valueOf(tempBiasY) + "','");
                                                tempSqlSB.append(String.valueOf(tempIsEncrypt) + "','");
                                                tempSqlSB.append(String.valueOf(tempAccessPwd) + "')");
                                                if (PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL(tempSqlSB.toString())) {
                                                    tempTid++;
                                                }
                                            }
                                            tempReader.Close();
                                        }
                                        tmpSqlDatabase.Close();
                                    } else {
                                        tempOutMsgSB.append("???????????????" + tempHash.get("D1").toString() + "??????????????????.\r\n");
                                    }
                                }
                            }
                            if (tempOutMsgSB.length() > 0) {
                                Common.ShowDialog(tempOutMsgSB.toString());
                                return;
                            }
                            if (VectorLayers_Mang_Dialog.this.m_Callback != null) {
                                VectorLayers_Mang_Dialog.this.m_Callback.OnClick("??????????????????", tempReturnSB.toString());
                            }
                            VectorLayers_Mang_Dialog.this._Dialog.dismiss();
                            return;
                        }
                        if (VectorLayers_Mang_Dialog.this.m_Callback != null) {
                            VectorLayers_Mang_Dialog.this.m_Callback.OnClick("??????????????????", null);
                        }
                        VectorLayers_Mang_Dialog.this._Dialog.dismiss();
                        return;
                    }
                    VectorLayers_Mang_Dialog.this._Dialog.dismiss();
                } else if (paramString.contains("????????????")) {
                    String[] tempPath2 = pObject.toString().split(";");
                    if (tempPath2 != null && tempPath2.length > 1) {
                        String tempFPath = tempPath2[1];
                        if (VectorLayers_Mang_Dialog.this.findLayersIndex(tempFPath) >= 0) {
                            Common.ShowDialog("????????????:\r\n" + tempFPath + "\r\n????????????!");
                            return;
                        }
                        DataAuthority tmpAuth = new DataAuthority(1);
                        BasicValue tmpOutMsg = new BasicValue();
                        if (tmpAuth.Initial(tempFPath, "", tmpOutMsg)) {
                            VectorLayers_New_Dialog tempDialog = new VectorLayers_New_Dialog();
                            if (tmpAuth.IsEncrypt()) {
                                tempDialog.SetEncryptKey(tmpAuth.getDataKey());
                            } else {
                                tempDialog.SetEncryptKey("");
                            }
                            tempDialog.SetBKVectorLayersEditType(0, tempFPath);
                            tempDialog.SetCallback(VectorLayers_Mang_Dialog.this.pCallback);
                            tempDialog.ShowDialog();
                        } else if (tmpAuth.IsEncrypt()) {
                            if (tmpAuth.NeedPwdAccess()) {
                                VectorLayers_Mang_Dialog.this.m_TempSelectedFile = tempFPath;
                                Input_Dialog tmpDialog = new Input_Dialog();
                                tmpDialog.SetReturnTag("??????????????????????????????");
                                tmpDialog.SetCallback(VectorLayers_Mang_Dialog.this.pCallback);
                                tmpDialog.setValues("????????????", "??????: ", "", "??????:??????????????????????????????????????????.");
                                tmpDialog.setInputType(129);
                                tmpDialog.ShowDialog();
                                return;
                            }
                            Common.ShowDialog(tmpOutMsg.getString());
                        }
                    }
                } else if (paramString.equals("????????????")) {
                    Object[] tmpObjs = (Object[]) pObject;
                    if (tmpObjs != null && tmpObjs.length > 1) {
                        String tmpReturnTag = String.valueOf(tmpObjs[0]);
                        if (tmpReturnTag.equals("??????????????????????????????")) {
                            if (!VectorLayers_Mang_Dialog.this.m_TempSelectedFile.equals("") && new File(VectorLayers_Mang_Dialog.this.m_TempSelectedFile).exists()) {
                                String tmpKey2 = String.valueOf(tmpObjs[1]);
                                DataAuthority tmpAuth2 = new DataAuthority(1);
                                BasicValue tmpOutMsg2 = new BasicValue();
                                if (tmpAuth2.Initial(VectorLayers_Mang_Dialog.this.m_TempSelectedFile, tmpKey2, tmpOutMsg2)) {
                                    VectorLayers_New_Dialog tempDialog2 = new VectorLayers_New_Dialog();
                                    tempDialog2.SetEncryptKey(tmpAuth2.getDataKey());
                                    tempDialog2.SetAccessPassword(tmpKey2);
                                    tempDialog2.SetBKVectorLayersEditType(0, VectorLayers_Mang_Dialog.this.m_TempSelectedFile);
                                    tempDialog2.SetCallback(VectorLayers_Mang_Dialog.this.pCallback);
                                    tempDialog2.ShowDialog();
                                    return;
                                }
                                Common.ShowDialog(tmpOutMsg2.getString());
                            }
                        } else if (tmpReturnTag.equals("????????????????????????????????????") && tmpObjs.length > 2 && (temphash2 = (HashMap) tmpObjs[2]) != null) {
                            String tmpLayersPath = temphash2.get("D3").toString();
                            if (Common.CheckExistFile(tmpLayersPath)) {
                                String tmpAccessPwd = String.valueOf(tmpObjs[1]);
                                DataAuthority tmpDataAuthority2 = new DataAuthority(1);
                                BasicValue tmpOutMsg3 = new BasicValue();
                                if (tmpDataAuthority2.Initial(tmpLayersPath, tmpAccessPwd, tmpOutMsg3)) {
                                    VectorLayers_Mang_Dialog.this.m_AllowInputPwdCount = 0;
                                    temphash2.put("Security", tmpAccessPwd);
                                    VectorLayers_New_Dialog tempDialog3 = new VectorLayers_New_Dialog();
                                    if (tmpDataAuthority2.IsEncrypt()) {
                                        tempDialog3.SetEncryptKey(tmpDataAuthority2.getDataKey());
                                    }
                                    tempDialog3.SetAccessPassword(tmpAccessPwd);
                                    tempDialog3.SetBKVectorLayersEditType(1, temphash2);
                                    tempDialog3.SetCallback(VectorLayers_Mang_Dialog.this.pCallback);
                                    tempDialog3.ShowDialog();
                                    return;
                                }
                                VectorLayers_Mang_Dialog.this.m_AllowInputPwdCount++;
                                Common.ShowDialog(tmpOutMsg3.getString());
                                if (VectorLayers_Mang_Dialog.this.m_AllowInputPwdCount >= 3) {
                                    Common.ShowDialog("?????????????????????????????????,???????????????.");
                                    VectorLayers_Mang_Dialog.this._Dialog.dismiss();
                                    return;
                                }
                                return;
                            }
                            Common.ShowDialog("??????:\r\n" + tmpLayersPath + "\r\n?????????.");
                        }
                    }
                } else if (paramString.equals("???????????????")) {
                    final HashMap temphash3 = (HashMap) pObject;
                    if (temphash3 != null) {
                        String tmpLayersPath2 = temphash3.get("D3").toString();
                        if (Common.CheckExistFile(tmpLayersPath2)) {
                            String tmpAccessPwd2 = String.valueOf(temphash3.get("Security"));
                            DataAuthority tmpDataAuthority3 = new DataAuthority(1);
                            BasicValue tmpOutMsg4 = new BasicValue();
                            if (tmpDataAuthority3.Initial(tmpLayersPath2, tmpAccessPwd2, tmpOutMsg4)) {
                                VectorLayers_New_Dialog tempDialog4 = new VectorLayers_New_Dialog();
                                if (tmpDataAuthority3.IsEncrypt()) {
                                    tempDialog4.SetEncryptKey(tmpDataAuthority3.getDataKey());
                                }
                                tempDialog4.SetAccessPassword(tmpAccessPwd2);
                                tempDialog4.SetBKVectorLayersEditType(1, temphash3);
                                tempDialog4.SetCallback(VectorLayers_Mang_Dialog.this.pCallback);
                                tempDialog4.ShowDialog();
                                return;
                            }
                            Common.ShowYesNoDialog(VectorLayers_Mang_Dialog.this._Dialog.getContext(), String.valueOf(tmpOutMsg4.getString()) + "\r\n?????????????????????????", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_Mang_Dialog.1.1
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String paramString2, Object pObject2) {
                                    if (paramString2.equals("YES")) {
                                        Input_Dialog tmpDialog2 = new Input_Dialog();
                                        tmpDialog2.SetReturnTag("????????????????????????????????????");
                                        tmpDialog2.SetCallback(VectorLayers_Mang_Dialog.this.pCallback);
                                        tmpDialog2.setValues("????????????", "??????: ", "", "??????:??????????????????????????????????????????.");
                                        tmpDialog2.SetTagValue(temphash3);
                                        tmpDialog2.setInputType(129);
                                        tmpDialog2.ShowDialog();
                                    }
                                }
                            });
                            return;
                        }
                        Common.ShowDialog("??????:\r\n" + tmpLayersPath2 + "\r\n?????????.");
                    }
                } else if (!paramString.equals("????????????")) {
                    if (paramString.equals("??????????????????")) {
                        HashMap<String, Object> temphash4 = (HashMap) pObject;
                        if (temphash4 != null) {
                            VectorLayers_Mang_Dialog.this._NeedReturn = true;
                            VectorLayers_Mang_Dialog.this.m_MyTableDataList.add(temphash4);
                            VectorLayers_Mang_Dialog.this.justRefreshList();
                        }
                    } else if (paramString.equals("??????????????????") && (temphash = (HashMap) pObject) != null) {
                        VectorLayers_Mang_Dialog.this._NeedReturn = true;
                        if (VectorLayers_Mang_Dialog.this.m_MyTableDataList.indexOf(temphash) >= 0) {
                            DataSource tmpDataSource = PubVar.m_Workspace.GetDataSourceByName(String.valueOf(temphash.get("D3")));
                            if (tmpDataSource == null) {
                                temphash.put("Bg_Color", Integer.valueOf((int) SupportMenu.CATEGORY_MASK));
                            } else if (!tmpDataSource.InitialDataAuthority(String.valueOf(temphash.get("Security")))) {
                                temphash.put("Bg_Color", Integer.valueOf((int) SupportMenu.CATEGORY_MASK));
                            } else if (!tmpDataSource.IsEnable()) {
                                temphash.put("Bg_Color", Integer.valueOf((int) SupportMenu.CATEGORY_MASK));
                            } else if (temphash.containsKey("Bg_Color")) {
                                temphash.remove("Bg_Color");
                            }
                            VectorLayers_Mang_Dialog.this.justRefreshList();
                        }
                    }
                }
            }
        };
        this.m_AllowInputPwdCount = 0;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.vectorlayers_mang_dialog);
        this._Dialog.Resize(1.0f, PubVar.DialogHeightRatio);
        this._Dialog.SetCaption("??????????????????");
        this._Dialog.SetHeadButtons("1,2130837858,??????,??????", this.pCallback);
        ((Button) this._Dialog.findViewById(R.id.btn_AddVectorLayers)).setOnClickListener(new ViewClick());
        ((Button) this._Dialog.findViewById(R.id.btn_DeleteVectorLayers)).setOnClickListener(new ViewClick());
        this.m_TableFactory = new MyTableFactory();
        this.m_TableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.list_vectorLayers), "?????????", "??????,??????????????????,??????,??????,X??????,Y??????", "checkbox,text,text,text,text,text", new int[]{50, 150, PieChartRotationAnimator.FAST_ANIMATION_DURATION, ChartViewportAnimator.FAST_ANIMATION_DURATION, 150, 150}, this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshList() {
        this.m_MyTableDataList = new ArrayList();
        SQLiteReader tempReader = PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().Query("Select Path,F1,F2,GROUP_CONCAT(Name) as MyF01,GROUP_CONCAT(LayerID) as MyF02,F1,F2,F3,Security From T_BKVectorLayer Group By Path");
        if (tempReader != null) {
            while (tempReader.Read()) {
                String tempPath = tempReader.GetString(0);
                String tempName = tempPath.substring(tempPath.lastIndexOf(FileSelector_Dialog.sRoot) + 1);
                HashMap<String, Object> tempHash = new HashMap<>();
                tempHash.put("D1", false);
                tempHash.put("D2", tempName);
                tempHash.put("D3", tempPath);
                tempHash.put("D4", tempReader.GetString(3));
                tempHash.put("D5", tempReader.GetString(1));
                tempHash.put("D6", tempReader.GetString(2));
                tempHash.put("D11", tempReader.GetString(4));
                tempHash.put("D12", "NONE");
                String tempStr = tempReader.GetString(5);
                if (tempStr == null || tempStr.toUpperCase().equals("NULL")) {
                    tempStr = "0";
                }
                tempHash.put("D15", tempStr);
                String tempStr2 = tempReader.GetString(6);
                if (tempStr2 == null || tempStr2.toUpperCase().equals("NULL")) {
                    tempStr2 = "0";
                }
                tempHash.put("D16", tempStr2);
                String tempStr3 = tempReader.GetString(7);
                if (tempStr3 == null || tempStr3.toUpperCase().equals("NULL")) {
                    tempStr3 = "false";
                }
                tempHash.put("IsEncrypt", tempStr3);
                String tempStr4 = tempReader.GetString(8);
                if (tempStr4 == null || tempStr4.toUpperCase().equals("NULL")) {
                    tempStr4 = "";
                }
                tempHash.put("Security", tempStr4);
                DataSource tmpDataSource = PubVar.m_Workspace.GetDataSourceByName(tempPath);
                if (tmpDataSource == null || !tmpDataSource.IsEnable()) {
                    tempHash.put("Bg_Color", Integer.valueOf((int) SupportMenu.CATEGORY_MASK));
                }
                this.m_MyTableDataList.add(tempHash);
            }
        }
        this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2", "D3", "D4", "D5", "D6"}, this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void justRefreshList() {
        this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2", "D3", "D4", "D5", "D6"}, this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int findLayersIndex(String path) {
        int result = 0;
        if (this.m_MyTableDataList.size() > 0) {
            for (HashMap<String, Object> tempHash : this.m_MyTableDataList) {
                if (tempHash.get("D3").toString().equals(path)) {
                    return result;
                }
                result++;
            }
        }
        return -1;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("??????????????????")) {
            this.m_TempSelectedFile = "";
            FileSelector_Dialog tempDialog = new FileSelector_Dialog(".vxd;.vmx;.db;", false);
            Common.ShowToast("???????????????????????????(VXD,VMX,DB?????????).");
            tempDialog.SetTitle("????????????");
            tempDialog.SetCallback(this.pCallback);
            tempDialog.ShowDialog();
        } else if (command.equals("??????????????????")) {
            boolean tempBool = false;
            int count = this.m_MyTableDataList.size();
            int i = 0;
            while (i < count) {
                if (Boolean.parseBoolean(this.m_MyTableDataList.get(i).get("D1").toString())) {
                    this.m_MyTableDataList.remove(i);
                    i--;
                    count--;
                    tempBool = true;
                }
                i++;
            }
            if (tempBool) {
                this._NeedReturn = true;
                justRefreshList();
                return;
            }
            Common.ShowDialog("?????????????????????????????????????????????.");
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.VectorLayers_Mang_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                VectorLayers_Mang_Dialog.this.refreshList();
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
                VectorLayers_Mang_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
