package  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Setting_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private boolean m_NeedSave;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public Setting_Dialog() {
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this._Dialog = null;
        this.m_Callback = null;
        this.m_NeedSave = false;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.Setting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("返回")) {
                    if (Setting_Dialog.this.m_NeedSave) {
                        Common.ShowYesNoDialogWithAlert(Setting_Dialog.this._Dialog.getContext(), "督察图层数据已经修改过,需要重新建立督察图斑数据库.\r\n是否确定退出?", true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.Setting_Dialog.1.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String command2, Object pObject) {
                                if (command2.equals("YES")) {
                                    Setting_Dialog.this._Dialog.dismiss();
                                }
                            }
                        });
                    } else {
                        Setting_Dialog.this._Dialog.dismiss();
                    }
                } else if (command.equals("确定")) {
                    Setting_Dialog.this.buildCheckDatabase();
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.senlinducha_setting_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("森林督查系统设置");
        this._Dialog.SetHeadButtons("1,2130837858,保存,确定", this.pCallback);
        this._Dialog.SetAllowedCloseDialog(false);
        this._Dialog.SetCallback(this.pCallback);
        this._Dialog.SetCancelCommand("");
        this._Dialog.findViewById(R.id.btn_AddLayer).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_DeleteLayers).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_FieldsSetting).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_GenJingTableSetting).setOnClickListener(new ViewClick());
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_senlinducha_layerlist), "自定义", "选择,督查图层名称", "checkbox,text", new int[]{-15, -85}, this.pCallback);
        this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void buildCheckDatabase() {
        try {
            String tmpPath = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/SenLinDuCha.dbx";
            SQLiteDBHelper tmpSQLiteDBHelper = null;
            if (!new File(tmpPath).exists()) {
                Common.CopyFile(String.valueOf(Common.GetAPPPath()) + "/SysFile/Template.dbx", tmpPath);
            }
            if (new File(tmpPath).exists()) {
                tmpSQLiteDBHelper = new SQLiteDBHelper(tmpPath);
            }
            if (tmpSQLiteDBHelper != null) {
                tmpSQLiteDBHelper.ExecuteSQL("Create Table If Not Exists T_SysParams (ID integer primary key AutoIncrement,ParaName varchar(1024) NOT NULL Unique,ParaValue TEXT Default '')");
                tmpSQLiteDBHelper.ExecuteSQL("Create Table If Not Exists T_CheckPartsInfo (ID integer primary key AutoIncrement,PartName varchar(1024) Default '',Sheng varchar(50) Default '',Xian varchar(50) Default '',Xiang varchar(50) Default '',Cun varchar(50) Default '',PartIndex varchar(50) Default '',LayerID varchar(50),SYSID varchar(10),Remark TEXT )");
                tmpSQLiteDBHelper.ExecuteSQL("Create Table If Not Exists T_YangDiInfo (ID integer primary key AutoIncrement,YangDiName varchar(1024) NOT NULL Unique,YangDiIndex varchar(10) Default '', Sheng varchar(50) Default '',Xian varchar(50) Default '',Xiang varchar(50) Default '',Cun varchar(50) Default '',PartIndex varchar(50) Default '',LayerID varchar(50),SYSID varchar(10), X Double Default 0, Y Double Default 0,PhotoInfo varchar(4096) Default '',YangDiDataID varchar(50) Default '', Remark TEXT )");
                tmpSQLiteDBHelper.ExecuteSQL("Create Table If Not Exists T_YangDiData (ID integer primary key AutoIncrement,YangDiName varchar(1024) Default '',GenJing Integer Default 0,Shan Integer Default 0,Ma Integer Default 0 ,Kuo Integer Default 0, Remark TEXT )");
            }
            SQLiteReader tmpSQLiteReader = tmpSQLiteDBHelper.Query("Select Count(ID) From T_CheckPartsInfo");
            int tmpCount = 0;
            if (tmpSQLiteReader.Read()) {
                tmpCount = tmpSQLiteReader.GetInt32(0);
            }
            if (tmpCount > 0) {
                SQLiteDBHelper finalTmpSQLiteDBHelper = tmpSQLiteDBHelper;
                Common.ShowYesNoDialogWithAlert(this._Dialog.getContext(), "督查图斑数据库中已经存在数据.\r\n是否清空之前的数据?", true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.Setting_Dialog.2
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command, Object pObject) {
                        if (command.equals("YES")) {
                            finalTmpSQLiteDBHelper.ExecuteSQL("Delete From T_CheckPartsInfo");
                            Common.ShowToast("清空督查图斑数据完成!");
                            Setting_Dialog.this.buildCheckDatabase(finalTmpSQLiteDBHelper);
                        }
                    }
                });
            } else {
                buildCheckDatabase(tmpSQLiteDBHelper);
            }
        } catch (Exception e) {
            Common.Log("错误", e.getLocalizedMessage());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void buildCheckDatabase(SQLiteDBHelper mSQLiteDBHelper) {
        try {
            List<String> tmpLyrList = CommonSetting.getCheckLayersList();
            if (tmpLyrList != null && tmpLyrList.size() > 0) {
                mSQLiteDBHelper.ExecuteSQL("ATTACH '" + (String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/TAData.dbx") + "' As TAData01");
                HashMap<String, String> tmpDuChaFixFieldsHash = CommonSetting.m_DuChaLayerMustFieldsName;
                for (String tmpLyrID : tmpLyrList) {
                    try {
                        FeatureLayer tmpFeatureLayer = PubVar._PubCommand.m_ProjectDB.getFeatureLayerByID(tmpLyrID);
                        if (tmpFeatureLayer != null) {
                            String tmpFID = tmpFeatureLayer.GetDataFieldByFieldName(tmpDuChaFixFieldsHash.get("省"));
                            if (tmpFID.length() > 0) {
                                String tmpFIDsString = String.valueOf("") + tmpFID;
                                String tmpFID2 = tmpFeatureLayer.GetDataFieldByFieldName(tmpDuChaFixFieldsHash.get("县"));
                                if (tmpFID2.length() > 0) {
                                    String tmpFIDsString2 = String.valueOf(tmpFIDsString) + "," + tmpFID2;
                                    String tmpFID3 = tmpFeatureLayer.GetDataFieldByFieldName(tmpDuChaFixFieldsHash.get("乡镇"));
                                    if (tmpFID3.length() > 0) {
                                        String tmpFIDsString3 = String.valueOf(tmpFIDsString2) + "," + tmpFID3;
                                        String tmpFID4 = tmpFeatureLayer.GetDataFieldByFieldName(tmpDuChaFixFieldsHash.get("村"));
                                        if (tmpFID4.length() > 0) {
                                            String tmpFIDsString4 = String.valueOf(tmpFIDsString3) + "," + tmpFID4;
                                            String tmpFID5 = tmpFeatureLayer.GetDataFieldByFieldName(tmpDuChaFixFieldsHash.get("图斑号"));
                                            if (tmpFID5.length() > 0) {
                                                String tmpFIDsString5 = String.valueOf(tmpFIDsString4) + "," + tmpFID5;
                                                SQLiteReader tmpSQLiteReader = mSQLiteDBHelper.Query("Select MAX(ID) From T_CheckPartsInfo");
                                                int tmpCount = 0;
                                                if (tmpSQLiteReader.Read()) {
                                                    tmpCount = tmpSQLiteReader.GetInt32(0);
                                                }
                                                mSQLiteDBHelper.ExecuteSQL("Insert Into T_CheckPartsInfo (Sheng,Xian,Xiang,Cun,PartIndex,SYSID) Select " + tmpFIDsString5 + ",SYS_ID From TAData01." + tmpFeatureLayer.GetLayerID() + "_D");
                                                mSQLiteDBHelper.ExecuteSQL("Update T_CheckPartsInfo Set LayerID='" + tmpLyrID + "' Where ID > " + String.valueOf(tmpCount));
                                                mSQLiteDBHelper.ExecuteSQL("Update T_CheckPartsInfo Set PartName= Sheng||'_'||Xian||'_'||Xiang||'_'||Cun||'_'||PartIndex Where ID > " + String.valueOf(tmpCount));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        Common.Log("错误", e.getLocalizedMessage());
                    }
                }
                mSQLiteDBHelper.ExecuteSQL("DETACH DATABASE TAData01");
            }
            this.m_NeedSave = false;
            Common.ShowDialog("建立督查图斑数据库完成!");
            this._Dialog.dismiss();
        } catch (Exception e2) {
            Common.Log("错误", e2.getLocalizedMessage());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void setLayerList() {
        List<String> tmpCheckLyrList = CommonSetting.getCheckLayersList();
        tmpCheckLyrList.clear();
        if (this.m_MyTableDataList.size() > 0) {
            for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
                tmpCheckLyrList.add(String.valueOf(tmpHash.get("D3")));
            }
        }
        CommonSetting.SaveCheckLayersList();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void initialLayers() {
        try {
            List<String> tmpLyrList = CommonSetting.getCheckLayersList();
            if (tmpLyrList != null && tmpLyrList.size() > 0) {
                for (String tmpLyrID : tmpLyrList) {
                    GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByID(tmpLyrID);
                    if (tmpGeoLayer != null) {
                        HashMap<String, Object> tmpHash = new HashMap<>();
                        tmpHash.put("D1", false);
                        tmpHash.put("D2", tmpGeoLayer.getLayerName());
                        tmpHash.put("D3", tmpGeoLayer.getLayerID());
                        tmpHash.put("layer", tmpGeoLayer);
                        this.m_MyTableDataList.add(tmpHash);
                    }
                }
                this.m_MyTableFactory.notifyDataSetChanged();
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean checkDuChaLayer(GeoLayer layer, BasicValue outNeedFieldName, BasicValue outMsg) {
        FeatureLayer tmpFeatureLayer = PubVar._PubCommand.m_ProjectDB.getFeatureLayerByID(layer.getLayerID());
        StringBuilder tmpStringBuilder = new StringBuilder();
        StringBuilder tmpStringBuilder2 = new StringBuilder();
        for (Map.Entry<String, String> tmpEntry : CommonSetting.m_DuChaLayerMustFieldsName.entrySet()) {
            if (tmpFeatureLayer.GetDataFieldByFieldName(tmpEntry.getValue()).length() == 0) {
                tmpStringBuilder.append("\r\n【" + tmpEntry.getKey() + "】：" + tmpEntry.getValue());
                tmpStringBuilder2.append(String.valueOf(tmpEntry.getValue()) + ";");
            }
        }
        if (tmpStringBuilder.length() == 0) {
            return true;
        }
        if (outMsg != null) {
            outMsg.setValue(tmpStringBuilder.toString());
        }
        if (outNeedFieldName != null) {
            outNeedFieldName.setValue(tmpStringBuilder2.toString());
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("添加督查图层")) {
            List<String> tmpCheckLyrList = CommonSetting.getCheckLayersList();
            final List<HashMap<String, Object>> tmplayersDataList = new ArrayList<>();
            List<String> tempArray = new ArrayList<>();
            for (GeoLayer tmpGeoLayer : PubVar._Map.getGeoLayers().getList()) {
                if (tmpCheckLyrList == null || !tmpCheckLyrList.contains(tmpGeoLayer.getLayerID())) {
                    if (tmpGeoLayer.getType() == EGeoLayerType.POLYGON) {
                        tempArray.add(tmpGeoLayer.getLayerName());
                        HashMap<String, Object> tempHash = new HashMap<>();
                        tempHash.put("D1", tmpGeoLayer.getLayerName());
                        tempHash.put("D2", tmpGeoLayer.getLayerID());
                        tempHash.put("layer", tmpGeoLayer);
                        tmplayersDataList.add(tempHash);
                    }
                }
            }
            if (tmplayersDataList.size() > 0) {
                new AlertDialog.Builder(this._Dialog.getContext(), 3).setTitle("添加督查图层:").setSingleChoiceItems((String[]) tempArray.toArray(new String[tempArray.size()]), -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.Setting_Dialog.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface arg0, int arg1) {
                        HashMap<String, Object> tempHash2 = (HashMap) tmplayersDataList.get(arg1);
                        if (tempHash2 != null) {
                            final GeoLayer tmpLayer = (GeoLayer) tempHash2.get("layer");
                            BasicValue tmpMsg = new BasicValue();
                            final BasicValue tmpNeedFIDs = new BasicValue();
                            if (Setting_Dialog.this.checkDuChaLayer(tmpLayer, tmpNeedFIDs, tmpMsg)) {
                                HashMap<String, Object> tmpHash = new HashMap<>();
                                tmpHash.put("D1", false);
                                tmpHash.put("D2", String.valueOf(tempHash2.get("D1")));
                                tmpHash.put("D3", String.valueOf(tempHash2.get("D2")));
                                tmpHash.put("layer", tmpLayer);
                                Setting_Dialog.this.m_MyTableDataList.add(tmpHash);
                                Setting_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                                Setting_Dialog.this.setLayerList();
                                Setting_Dialog.this.m_NeedSave = true;
                            } else {
                                Common.ShowYesNoDialogWithAlert(Setting_Dialog.this._Dialog.getContext(), "选择的图层【" + tmpLayer.getLayerName() + "】中不包含如下必须具备的字段,是否自动创建缺失字段?\r\n" + tmpMsg.getString(), true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.Setting_Dialog.3.1
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String command2, Object pObject) {
                                        String[] tmpStrs;
                                        FeatureLayer tmpFeatureLayer;
                                        int tmpMaxFieldOrder;
                                        if (command2.equals("YES") && (tmpStrs = tmpNeedFIDs.getString().split(";")) != null && tmpStrs.length > 0 && (tmpFeatureLayer = PubVar._PubCommand.m_ProjectDB.getFeatureLayerByID(tmpLayer.getLayerID())) != null) {
                                            int tmpMaxFieldOrder2 = tmpFeatureLayer.getFiledMaxIndex() + 1;
                                            int length = tmpStrs.length;
                                            int i = 0;
                                            int tmpMaxFieldOrder3 = tmpMaxFieldOrder2;
                                            while (i < length) {
                                                String tmpString2 = tmpStrs[i];
                                                if (tmpString2.trim().length() > 0) {
                                                    LayerField tmpLayerField = new LayerField();
                                                    tmpLayerField.SetFieldName(tmpString2.trim());
                                                    tmpLayerField.SetFieldTypeName("字符串");
                                                    tmpLayerField.SetDataFieldName(tmpFeatureLayer.GetNewFID());
                                                    tmpLayerField.SetFieldVisible(true);
                                                    tmpMaxFieldOrder = tmpMaxFieldOrder3 + 1;
                                                    tmpLayerField.setFieldIndex(tmpMaxFieldOrder3);
                                                    tmpFeatureLayer.AddField(tmpLayerField);
                                                } else {
                                                    tmpMaxFieldOrder = tmpMaxFieldOrder3;
                                                }
                                                i++;
                                                tmpMaxFieldOrder3 = tmpMaxFieldOrder;
                                            }
                                            tmpFeatureLayer.SaveLayerInfo();
                                            Common.ShowDialog("为图层【" + tmpLayer.getLayerName() + "】新建字段完成,请重新添加为督查图层!");
                                        }
                                    }
                                });
                            }
                        } else {
                            Common.ShowDialog("选择的图层无效.");
                        }
                        arg0.dismiss();
                    }
                }).show();
                return;
            }
            Common.ShowDialog("项目中没有可选择的督查图层.");
        } else if (command.equals("删除督查图层")) {
            final List<Integer> tmpList = new ArrayList<>();
            int tmpTid = -1;
            for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
                tmpTid++;
                if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                    tmpList.add(Integer.valueOf(tmpTid));
                }
            }
            if (tmpList.size() > 0) {
                Common.ShowYesNoDialog(this._Dialog.getContext(), "是否删除选择的" + String.valueOf(tmpList.size()) + "个督查图层?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.Setting_Dialog.4
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        if (paramString.equals("YES")) {
                            boolean tempBool = false;
                            for (int j = tmpList.size() - 1; j > -1; j--) {
                                Setting_Dialog.this.m_MyTableDataList.remove(((Integer) tmpList.get(j)).intValue());
                                tempBool = true;
                            }
                            if (tempBool) {
                                Setting_Dialog.this.m_NeedSave = true;
                                Setting_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                                Setting_Dialog.this.setLayerList();
                            }
                        }
                    }
                });
            } else {
                Common.ShowDialog("请勾选要删除的督查图层.");
            }
        } else if (command.equals("根茎材积设置")) {
            new GenJingTable_Dialog().ShowDialog();
        } else if (command.equals("字段关联设置")) {
            new FieldSetting_Dialog().ShowDialog();
        } else if (command.equals("建立督查图斑数据库")) {
            buildCheckDatabase();
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.Setting_Dialog.5
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Setting_Dialog.this.initialLayers();
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
                Setting_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
