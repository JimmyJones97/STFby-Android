package  com.xzy.forestSystem.gogisapi.Encryption;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataAuthorityManage_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private Button m_CodeTypeBtn;
    private RadioButton m_DeviceAuthRadioButton;
    private EditText m_FileEditTxt;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private Button m_RemoveCodeBtn;
    private Button m_SetCodeBtn;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public DataAuthorityManage_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_FileEditTxt = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityManage_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("选择文件")) {
                    String[] tempPath2 = object.toString().split(";");
                    if (tempPath2 != null && tempPath2.length > 1) {
                        String tempFPath = tempPath2[1];
                        if (PubVar.m_Workspace == null || PubVar.m_Workspace.GetDataSourceByName(tempFPath) == null) {
                            DataAuthorityManage_Dialog.this.m_FileEditTxt.setText(tempFPath);
                            DataAuthorityManage_Dialog.this.refreshDataInfo(tempFPath);
                            return;
                        }
                        Common.ShowDialog("该数据文件正在被当前项目使用!请在登入系统时或其他未使用该数据的项目中进行设置.");
                    }
                } else if (command.equals("输入参数")) {
                    Object[] tmpObjs = (Object[]) object;
                    if (tmpObjs != null && tmpObjs.length > 1 && String.valueOf(tmpObjs[0]).equals("加密类型设置输入密码")) {
                        String tmpKey = String.valueOf(tmpObjs[1]);
                        String tmpOrigPwd = DataAuthorityManage_Dialog.this.getOrigPWD();
                        if (tmpKey == null || !tmpKey.equals(tmpOrigPwd)) {
                            Common.ShowDialog("输入的密码不正确.");
                            return;
                        }
                        String tmpFilepath = DataAuthorityManage_Dialog.this.m_FileEditTxt.getText().toString();
                        DataAuthority tmpDataAuthority = new DataAuthority(1);
                        tmpDataAuthority.Initial(tmpFilepath, "", new BasicValue());
                        if (tmpDataAuthority.IsEncrypt()) {
                            DataAuthorityManage_Dialog.this._Dialog.findViewById(R.id.ll_dataauth_tools).setVisibility(0);
                            DataAuthorityManage_Dialog.this._Dialog.findViewById(R.id.ll_dataauth_codeType).setVisibility(0);
                            DataAuthorityManage_Dialog.this.m_CodeTypeBtn.setText("保存设置");
                            DataAuthorityManage_Dialog.this.m_CodeTypeBtn.setTag("保存加密类型设置");
                            if (tmpDataAuthority.NeedPwdAccess()) {
                                DataAuthorityManage_Dialog.this._Dialog.findViewById(R.id.ll_dataauth_device).setVisibility(8);
                                ((RadioButton) DataAuthorityManage_Dialog.this._Dialog.findViewById(R.id.radio0)).setChecked(true);
                                return;
                            }
                            DataAuthorityManage_Dialog.this._Dialog.findViewById(R.id.ll_dataauth_device).setVisibility(0);
                            ((RadioButton) DataAuthorityManage_Dialog.this._Dialog.findViewById(R.id.radio1)).setChecked(true);
                            DataAuthorityManage_Dialog.this.refeshDeviceList();
                        }
                    }
                } else if (command.equals("设置数据加密密码返回")) {
                    final String tmpKey2 = String.valueOf(object);
                    if (tmpKey2 == null || tmpKey2.equals("")) {
                        Common.ShowDialog("设置的密码不能为空.");
                    } else {
                        Common.ShowProgressDialog("正在进行数据加密,请稍候...", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityManage_Dialog.1.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString, Object pObject) {
                                String tmpFilepath2 = DataAuthorityManage_Dialog.this.m_FileEditTxt.getText().toString();
                                File tmpFile01 = new File(tmpFilepath2);
                                if (tmpFile01.exists()) {
                                    String tmpFilePath012 = String.valueOf(tmpFilepath2) + "bak";
                                    File tmpFile02 = new File(tmpFilePath012);
                                    if (tmpFile02.exists()) {
                                        tmpFile02.delete();
                                    }
                                    Common.CopyFile(tmpFilepath2, tmpFilePath012);
                                    try {
                                        SQLiteDatabase tmpDB = new DatabaseHelper(DataAuthorityManage_Dialog.this._Dialog.getContext(), tmpFilepath2, null, 1).getWritableDatabase();
                                        if (tmpDB != null) {
                                            tmpDB.execSQL("Create Table If Not Exists Authority (ID integer primary key AutoIncrement,ParaName varchar(255) NOT NULL Unique Default '', ParaValue TEXT Default '')");
                                            tmpDB.execSQL("Replace Into Authority (ParaName, ParaValue) Values ('NeedPwdAccess','true');");
                                            tmpDB.execSQL("Replace Into Authority (ParaName, ParaValue) Values ('AccessPassword','" + tmpKey2 + "');");
                                            tmpDB.close();
                                        }
                                        DataAuthority tmpDataAuthority2 = new DataAuthority(1);
                                        tmpDataAuthority2.SetIsEncrypt(true);
                                        if (C0433Common.CreatePassword(DataAuthorityManage_Dialog.this._Dialog.getContext(), tmpFilepath2, tmpDataAuthority2.getDataKey())) {
                                            if (tmpFile02.exists()) {
                                                tmpFile02.delete();
                                            }
                                            DataAuthorityManage_Dialog.this.refreshDataInfo(tmpFilepath2);
                                            Common.ShowDialog("密码设置成功.");
                                            return;
                                        }
                                        Common.ShowDialog("加密设置失败.");
                                        if (tmpFile02.exists()) {
                                            if (tmpFile01.exists()) {
                                                tmpFile01.delete();
                                            }
                                            tmpFile02.renameTo(new File(tmpFilepath2));
                                        }
                                    } catch (Exception e) {
                                    }
                                } else {
                                    Common.ShowDialog("文件:" + tmpFilepath2 + " 不存在.");
                                }
                            }
                        });
                    }
                } else if (command.equals("修改数据加密密码返回")) {
                    String tmpNewKey = String.valueOf(object);
                    String tmpFilepath2 = DataAuthorityManage_Dialog.this.m_FileEditTxt.getText().toString();
                    if (new File(tmpFilepath2).exists()) {
                        boolean tmpBool = false;
                        DataAuthority tmpDataAuthority2 = new DataAuthority(1);
                        tmpDataAuthority2.SetIsEncrypt(true);
                        tmpDataAuthority2.getDataKey();
                        try {
                            SQLiteDatabase tmpDB = new DatabaseHelper(DataAuthorityManage_Dialog.this._Dialog.getContext(), tmpFilepath2, null, 1).getWritableDatabase();
                            if (tmpDB != null) {
                                tmpDB.execSQL("Replace Into Authority (ParaName, ParaValue) Values ('AccessPassword','" + tmpNewKey + "');");
                                tmpBool = true;
                                tmpDB.close();
                            }
                        } catch (Exception e) {
                            tmpBool = false;
                        }
                        if (tmpBool) {
                            Common.ShowDialog("修改密码成功.");
                        } else {
                            Common.ShowDialog("修改密码失败.");
                        }
                    } else {
                        Common.ShowDialog("文件:" + tmpFilepath2 + " 不存在.");
                    }
                } else if (command.equals("解除数据加密返回")) {
                    Common.ShowProgressDialog("正在解除数据加密,请稍候...", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityManage_Dialog.1.2
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            String tmpFilepath3 = DataAuthorityManage_Dialog.this.m_FileEditTxt.getText().toString();
                            File tmpFile01 = new File(tmpFilepath3);
                            if (tmpFile01.exists()) {
                                String tmpFilePath012 = String.valueOf(tmpFilepath3) + "bak";
                                File tmpFile02 = new File(tmpFilePath012);
                                if (tmpFile02.exists()) {
                                    tmpFile02.delete();
                                }
                                Common.CopyFile(tmpFilepath3, tmpFilePath012);
                                try {
                                    DataAuthority tmpDataAuthority3 = new DataAuthority(1);
                                    tmpDataAuthority3.SetIsEncrypt(true);
                                    if (C0433Common.RemovePassword(DataAuthorityManage_Dialog.this._Dialog.getContext(), tmpFilepath3, tmpDataAuthority3.getDataKey())) {
                                        if (tmpFile02.exists()) {
                                            tmpFile02.delete();
                                        }
                                        DataAuthorityManage_Dialog.this.refreshDataInfo(tmpFilepath3);
                                        Common.ShowDialog("解除数据加密成功.");
                                        return;
                                    }
                                    Common.ShowDialog("解除数据加密失败.");
                                    if (tmpFile02.exists()) {
                                        if (tmpFile01.exists()) {
                                            tmpFile01.delete();
                                        }
                                        tmpFile02.renameTo(new File(tmpFilepath3));
                                    }
                                } catch (Exception e2) {
                                }
                            } else {
                                Common.ShowDialog("文件:" + tmpFilepath3 + " 不存在.");
                            }
                        }
                    });
                } else if (command.equals("添加设备返回")) {
                    HashMap tmpHash = (HashMap) object;
                    if (tmpHash != null) {
                        if (DataAuthorityManage_Dialog.this.findDeviceIndex(String.valueOf(tmpHash.get("D2"))) >= 0) {
                            Common.ShowDialog("该设备已经存在列表中.");
                            return;
                        }
                        DataAuthorityManage_Dialog.this.m_MyTableDataList.add(tmpHash);
                        DataAuthorityManage_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                    }
                } else if (command.equals("修改设备返回") && ((HashMap) object) != null) {
                    DataAuthorityManage_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.dataauthoritymanage_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("数据加密管理");
        this.m_FileEditTxt = (EditText) this._Dialog.findViewById(R.id.et_dataauth_filepath);
        this._Dialog.findViewById(R.id.ll_dataauth_tools).setVisibility(8);
        this._Dialog.findViewById(R.id.ll_dataauth_codeType).setVisibility(8);
        this._Dialog.findViewById(R.id.ll_dataauth_device).setVisibility(8);
        this._Dialog.findViewById(R.id.btn_dataauth_selectFile).setOnClickListener(new ViewClick());
        this.m_SetCodeBtn = (Button) this._Dialog.findViewById(R.id.btn_dataauth_SetCode);
        this.m_RemoveCodeBtn = (Button) this._Dialog.findViewById(R.id.btn_dataauth_RemoveCode);
        this.m_SetCodeBtn.setOnClickListener(new ViewClick());
        this.m_RemoveCodeBtn.setOnClickListener(new ViewClick());
        this.m_CodeTypeBtn = (Button) this._Dialog.findViewById(R.id.btn_dataauth_CodeTypeSet);
        this.m_CodeTypeBtn.setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.ll_dataauth_tools).setVisibility(8);
        this._Dialog.findViewById(R.id.ll_dataauth_codeType).setVisibility(8);
        this._Dialog.findViewById(R.id.ll_dataauth_device).setVisibility(8);
        this._Dialog.findViewById(R.id.btn_dataauth_addDevice).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_dataauth_modifyDevice).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_dataauth_deleteDevice).setOnClickListener(new ViewClick());
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_dataauth_device), "自定义", "选择,设备ID,可读,可写", "checkbox,text,text,text", new int[]{-15, -45, -20, -20}, null);
        this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
        this.m_DeviceAuthRadioButton = (RadioButton) this._Dialog.findViewById(R.id.radio1);
        this.m_DeviceAuthRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityManage_Dialog.2
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    DataAuthorityManage_Dialog.this._Dialog.findViewById(R.id.ll_dataauth_device).setVisibility(0);
                    DataAuthorityManage_Dialog.this.refeshDeviceList();
                    return;
                }
                DataAuthorityManage_Dialog.this._Dialog.findViewById(R.id.ll_dataauth_device).setVisibility(8);
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private String getOrigPWD() {
        String tmpOrigPwd = null;
        String tmpFilepath = this.m_FileEditTxt.getText().toString();
        if (new File(tmpFilepath).exists()) {
            DataAuthority tmpDataAuthority = new DataAuthority(1);
            tmpDataAuthority.SetIsEncrypt(true);
            tmpDataAuthority.getDataKey();
            try {
                SQLiteDatabase tmpDB = new DatabaseHelper(this._Dialog.getContext(), tmpFilepath, null, 1).getWritableDatabase();
                if (tmpDB != null) {
                    Cursor tmpCursor = tmpDB.rawQuery("select ParaValue from Authority Where ParaName='AccessPassword'", null);
                    if (tmpCursor.moveToNext()) {
                        tmpOrigPwd = String.valueOf(tmpCursor.getString(0));
                        tmpCursor.close();
                    }
                    tmpDB.close();
                }
            } catch (Exception e) {
            }
        } else {
            Common.ShowDialog("文件:" + tmpFilepath + " 不存在.");
        }
        return tmpOrigPwd;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshDataInfo(String filepath) {
        if (new File(filepath).exists()) {
            DataAuthority tmpDataAuthority = new DataAuthority(1);
            tmpDataAuthority.Initial(filepath, "", new BasicValue());
            if (tmpDataAuthority.IsEncrypt()) {
                this._Dialog.findViewById(R.id.ll_dataauth_tools).setVisibility(0);
                this.m_SetCodeBtn.setVisibility(0);
                this.m_RemoveCodeBtn.setVisibility(0);
                this.m_SetCodeBtn.setText("修改密码");
                this.m_SetCodeBtn.setTag("修改加密密码");
                this.m_CodeTypeBtn.setText("加密类型");
                this.m_CodeTypeBtn.setTag("加密类型设置");
                this.m_CodeTypeBtn.setVisibility(0);
                this._Dialog.findViewById(R.id.ll_dataauth_codeType).setVisibility(8);
                this._Dialog.findViewById(R.id.ll_dataauth_device).setVisibility(8);
                return;
            }
            this._Dialog.findViewById(R.id.ll_dataauth_tools).setVisibility(0);
            this._Dialog.findViewById(R.id.ll_dataauth_codeType).setVisibility(8);
            this._Dialog.findViewById(R.id.ll_dataauth_device).setVisibility(8);
            this.m_SetCodeBtn.setVisibility(0);
            this.m_RemoveCodeBtn.setVisibility(8);
            this.m_CodeTypeBtn.setVisibility(8);
            this.m_SetCodeBtn.setText("设置密码");
            this.m_SetCodeBtn.setTag("设置密码");
            return;
        }
        this._Dialog.findViewById(R.id.ll_dataauth_tools).setVisibility(8);
        this._Dialog.findViewById(R.id.ll_dataauth_codeType).setVisibility(8);
        this._Dialog.findViewById(R.id.ll_dataauth_device).setVisibility(8);
        Common.ShowToast("文件:" + filepath + " 不存在.");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refeshDeviceList() {
        String tmpStr;
        String[] tmpStrs;
        String tmpStr2;
        String[] tmpStrs2;
        this.m_MyTableDataList.clear();
        String tmpFilepath = this.m_FileEditTxt.getText().toString();
        if (new File(tmpFilepath).exists()) {
            DataAuthority tmpDataAuthority = new DataAuthority(1);
            tmpDataAuthority.SetIsEncrypt(true);
            tmpDataAuthority.getDataKey();
            try {
                SQLiteDatabase tmpDB = new DatabaseHelper(this._Dialog.getContext(), tmpFilepath, null, 1).getWritableDatabase();
                if (tmpDB != null) {
                    List<String> tmpList = new ArrayList<>();
                    Cursor tmpCursor = tmpDB.rawQuery("select ParaValue from Authority Where ParaName='AllowRead'", null);
                    if (tmpCursor != null) {
                        if (tmpCursor.moveToNext() && (tmpStr2 = tmpCursor.getString(0)) != null && !tmpStr2.equals("") && (tmpStrs2 = tmpStr2.split(";")) != null && tmpStrs2.length > 0) {
                            int length = tmpStrs2.length;
                            for (int i = 0; i < length; i++) {
                                String tmpDevice = tmpStrs2[i];
                                if (!tmpList.contains(tmpDevice)) {
                                    tmpList.add(tmpDevice);
                                }
                            }
                        }
                        tmpCursor.close();
                    }
                    List<String> tmpList2 = new ArrayList<>();
                    Cursor tmpCursor2 = tmpDB.rawQuery("select ParaValue from Authority Where ParaName='AllowWrite'", null);
                    if (tmpCursor2 != null) {
                        if (tmpCursor2.moveToNext() && (tmpStr = tmpCursor2.getString(0)) != null && !tmpStr.equals("") && (tmpStrs = tmpStr.split(";")) != null && tmpStrs.length > 0) {
                            int length2 = tmpStrs.length;
                            for (int i2 = 0; i2 < length2; i2++) {
                                String tmpDevice2 = tmpStrs[i2];
                                if (!tmpList2.contains(tmpDevice2)) {
                                    tmpList2.add(tmpDevice2);
                                }
                            }
                        }
                        tmpCursor2.close();
                    }
                    if (tmpList.size() > 0) {
                        for (String tmpDevice3 : tmpList) {
                            HashMap tmpHash = new HashMap();
                            tmpHash.put("D1", false);
                            tmpHash.put("D2", tmpDevice3);
                            tmpHash.put("D3", "是");
                            if (tmpList2.contains(tmpDevice3)) {
                                tmpHash.put("D4", "是");
                            } else {
                                tmpHash.put("D4", "否");
                            }
                            this.m_MyTableDataList.add(tmpHash);
                        }
                    }
                    tmpDB.close();
                }
            } catch (Exception e) {
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int findDeviceIndex(String deiveID) {
        int result = -1;
        for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
            result++;
            if (String.valueOf(tmpHash.get("D2")).equals(deiveID)) {
                return result;
            }
        }
        return -1;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityManage_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x0386, code lost:
        r26 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x0387, code lost:
        r30.m_CodeTypeBtn.setText("加密类型");
        r30.m_CodeTypeBtn.setTag("加密类型设置");
        r30._Dialog.findViewById(com.stczh.gzforestSystem.R.id.ll_dataauth_codeType).setVisibility(8);
        r30._Dialog.findViewById(com.stczh.gzforestSystem.R.id.ll_dataauth_device).setVisibility(8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x03c1, code lost:
        throw r26;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x03df, code lost:
        r30.m_CodeTypeBtn.setText("加密类型");
        r30.m_CodeTypeBtn.setTag("加密类型设置");
        r30._Dialog.findViewById(com.stczh.gzforestSystem.R.id.ll_dataauth_codeType).setVisibility(8);
        r30._Dialog.findViewById(com.stczh.gzforestSystem.R.id.ll_dataauth_device).setVisibility(8);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:?, code lost:
        return;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x0386 A[ExcHandler: all (r26v25 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:41:0x022c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void DoCommand(java.lang.String r31) {
        /*
        // Method dump skipped, instructions count: 1051
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.gogisapi.Encryption.DataAuthorityManage_Dialog.DoCommand(java.lang.String):void");
    }

    private List<Integer> getSelectedDevices() {
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
                DataAuthorityManage_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
