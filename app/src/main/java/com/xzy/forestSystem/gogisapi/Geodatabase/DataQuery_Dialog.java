package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;
import com.xzy.forestSystem.GuiZhou.DiaoCha.CalXuJi_Dialog;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import com.xzy.forestSystem.gogisapi.MyControls.InputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Others.DataDictionary.DataDictionaryManage;
import  com.xzy.forestSystem.gogisapi.XProject.Layer_Select_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class DataQuery_Dialog {
    private List<String> SeletedObjectsList;
    private XDialogTemplate _Dialog;
    private InputSpinner layersSpinner;
    private ICallback m_Callback;
    private List<FeatureLayer> m_LayerList;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private String m_QueryCondition;
    private FeatureLayer m_SelectLayer;
    private List<Integer> m_SelectMultiList;
    private List<XLayer> m_XLayerList;
    private ICallback pCallback;
    private String queryPageRow;
    private TextView textviewInfo;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public DataQuery_Dialog() {
        String tempStr;
        this._Dialog = null;
        this.m_Callback = null;
        this.m_LayerList = null;
        this.m_SelectLayer = null;
        this.queryPageRow = "100";
        this.textviewInfo = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                try {
                    if (paramString.equals("查询全部")) {
                        DataQuery_Dialog.this.queryDataList(null);
                    } else if (paramString.equals("根据条件查询")) {
                        Object[] tmpObjs = (Object[]) pObject;
                        if (tmpObjs != null && tmpObjs.length > 1) {
                            if (DataQuery_Dialog.this.m_SelectLayer != null) {
                                Object tmpTagObj = DataQuery_Dialog.this.m_SelectLayer.getTag();
                                HashMap<String, Object> tmpTagHash = null;
                                if (tmpTagObj instanceof HashMap) {
                                    tmpTagHash = (HashMap) tmpTagObj;
                                }
                                if (tmpTagHash == null) {
                                    tmpTagHash = new HashMap<>();
                                }
                                tmpTagHash.put("QuerySQLList", tmpObjs[1]);
                                DataQuery_Dialog.this.m_SelectLayer.setTag(tmpTagHash);
                            }
                            DataQuery_Dialog.this.queryDataList(tmpObjs[0].toString());
                        }
                    } else if (paramString.equals("设置返回")) {
                        DataQuery_Dialog.this.queryPageRow = pObject.toString();
                        if (DataQuery_Dialog.this.m_MyTableDataList != null) {
                            DataQuery_Dialog.this.m_MyTableDataList.clear();
                        }
                        DataQuery_Dialog.this.refreshLayerFields();
                    } else if (paramString.equals("单击选择行")) {
                        if (!(DataQuery_Dialog.this.m_SelectLayer == null || !DataQuery_Dialog.this.m_SelectLayer.GetEditable() || pObject == null)) {
                            HashMap tmpHashMap = (HashMap) pObject;
                            GeoLayer localGeoLayer = PubVar._Map.getGeoLayers().GetLayerByName(DataQuery_Dialog.this.m_SelectLayer.GetLayerID());
                            if (localGeoLayer != null) {
                                int tempInt = Integer.parseInt(tmpHashMap.get("D2").toString());
                                if (localGeoLayer.getDataset().getDataSource().getEditing()) {
                                    FeatureAttribute_Dialog temp_Data_Template = new FeatureAttribute_Dialog();
                                    temp_Data_Template.SetLayerID(DataQuery_Dialog.this.m_SelectLayer.GetLayerID());
                                    temp_Data_Template.SetSYS_ID(tempInt);
                                    temp_Data_Template.SetCallback(DataQuery_Dialog.this.pCallback);
                                    temp_Data_Template.ShowDialog();
                                }
                            }
                        }
                    } else if (paramString.equals("编辑属性返回")) {
                        int tempSYSID = ((Integer) pObject).intValue();
                        if (DataQuery_Dialog.this.m_MyTableDataList != null) {
                            String tempSYSIDStr = String.valueOf(tempSYSID);
                            HashMap tmpHashMap2 = null;
                            Iterator<HashMap<String, Object>> tempIter01 = DataQuery_Dialog.this.m_MyTableDataList.iterator();
                            while (true) {
                                if (!tempIter01.hasNext()) {
                                    break;
                                }
                                HashMap tempHash = tempIter01.next();
                                if (tempSYSIDStr.equals(tempHash.get("D2"))) {
                                    tmpHashMap2 = tempHash;
                                    break;
                                }
                            }
                            if (tmpHashMap2 != null) {
                                List<LayerField> tmpFields = DataQuery_Dialog.this.m_SelectLayer.GetFieldList();
                                StringBuilder tempFIDs = new StringBuilder();
                                int tempAllowFIDCount = 0;
                                for (LayerField tempField : tmpFields) {
                                    Object tempTag = tempField.GetTag();
                                    if (tempTag == null || tempTag.toString().equals("true")) {
                                        if (tempFIDs.length() > 0) {
                                            tempFIDs.append(",");
                                        }
                                        tempFIDs.append(tempField.GetDataFieldName());
                                        tempAllowFIDCount++;
                                    }
                                }
                                List<String> tempFieldIDArray = new ArrayList<>();
                                tempFieldIDArray.add("D1");
                                DataSet localDataset = PubVar._Map.getGeoLayers().GetLayerByName(DataQuery_Dialog.this.m_SelectLayer.GetLayerID()).getDataset();
                                if (!(localDataset == null || localDataset.getDataSource() == null || localDataset.getDataSource()._EDatabase == null)) {
                                    SQLiteReader tempSQLiteDataReader = localDataset.getDataSource()._EDatabase.Query("Select SYS_ID," + tempFIDs.toString() + " From " + localDataset.getDataTableName() + " Where SYS_ID=" + tempSYSID);
                                    if (tempSQLiteDataReader.Read()) {
                                        for (int i = 1; i <= tempAllowFIDCount; i++) {
                                            tmpHashMap2.put("F" + i, tempSQLiteDataReader.GetString(i));
                                            tempFieldIDArray.add("F" + i);
                                        }
                                    }
                                }
                                DataQuery_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                            }
                        }
                    } else if (paramString.equals("导出")) {
                        if (DataQuery_Dialog.this.m_SelectLayer != null) {
                            DataQuery_Dialog.this.SeletedObjectsList = new ArrayList();
                            if (DataQuery_Dialog.this.m_MyTableDataList != null) {
                                for (HashMap<String, Object> tmpHashMap3 : DataQuery_Dialog.this.m_MyTableDataList) {
                                    if (((Boolean) tmpHashMap3.get("D1")).booleanValue()) {
                                        DataQuery_Dialog.this.SeletedObjectsList.add(tmpHashMap3.get("D2").toString());
                                    }
                                }
                            }
                            if (DataQuery_Dialog.this.SeletedObjectsList.size() > 0) {
                                String tempOutIDS = Common.CombineStrings(",", DataQuery_Dialog.this.SeletedObjectsList);
                                DataExportOfSel_Dialog tempDialog = new DataExportOfSel_Dialog();
                                tempDialog.SetLayerID(DataQuery_Dialog.this.m_SelectLayer.GetLayerID(), DataQuery_Dialog.this.m_SelectLayer.GetDataSourceName());
                                tempDialog.SetExportDataSYSID(tempOutIDS);
                                tempDialog.ShowDialog();
                                return;
                            }
                            Common.ShowDialog("请选择需要导出的数据.");
                        }
                    } else if (paramString.equals("选择图层")) {
                        if (DataQuery_Dialog.this.m_MyTableDataList != null) {
                            DataQuery_Dialog.this.m_MyTableDataList.clear();
                        }
                        String tmpLayerID = pObject.toString();
                        if (DataQuery_Dialog.this.m_SelectLayer == null || !DataQuery_Dialog.this.m_SelectLayer.GetLayerID().equals(tmpLayerID)) {
                            for (FeatureLayer tmpLayer : DataQuery_Dialog.this.m_LayerList) {
                                if (tmpLayer.GetLayerID().equals(tmpLayerID)) {
                                    DataQuery_Dialog.this.m_SelectLayer = tmpLayer;
                                    DataQuery_Dialog.this.layersSpinner.getEditTextView().setText(DataQuery_Dialog.this.m_SelectLayer.GetLayerName());
                                    DataQuery_Dialog.this.refreshLayerFields();
                                    return;
                                }
                            }
                        }
                    } else if (paramString.equals("直接转换数据字典文字为代码")) {
                        DataQuery_Dialog.this.m_SelectMultiList = new ArrayList();
                        if (DataQuery_Dialog.this.m_SelectLayer != null) {
                            final List tmpFieldArray = DataQuery_Dialog.this.getDataDictFieldsName();
                            if (tmpFieldArray.size() > 0) {
                                new AlertDialog.Builder(DataQuery_Dialog.this._Dialog.getContext(), 3).setTitle("请选择转换的字段:").setMultiChoiceItems((String[]) tmpFieldArray.toArray(new String[tmpFieldArray.size()]), (boolean[]) null, new DialogInterface.OnMultiChoiceClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Dialog.1.1
                                    @Override // android.content.DialogInterface.OnMultiChoiceClickListener
                                    public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
                                        if (!arg2) {
                                            int tmpI = DataQuery_Dialog.this.m_SelectMultiList.indexOf(Integer.valueOf(arg1));
                                            if (tmpI > -1) {
                                                DataQuery_Dialog.this.m_SelectMultiList.remove(tmpI);
                                            }
                                        } else if (!DataQuery_Dialog.this.m_SelectMultiList.contains(Integer.valueOf(arg1))) {
                                            DataQuery_Dialog.this.m_SelectMultiList.add(Integer.valueOf(arg1));
                                        }
                                    }
                                }).setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Dialog.1.2
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        DataDictionaryManage tmpManage;
                                        DataSet localDataset2;
                                        try {
                                            if (DataQuery_Dialog.this.m_SelectMultiList.size() > 0) {
                                                for (Integer num : DataQuery_Dialog.this.m_SelectMultiList) {
                                                    LayerField tmpField = DataQuery_Dialog.this.m_SelectLayer.getLayerFieldByFieldName((String) tmpFieldArray.get(num.intValue()));
                                                    if (!(tmpField == null || (tmpManage = PubVar._PubCommand.m_ConfigDB.GetDataDictionaryManage()) == null)) {
                                                        List<String> tmpDctValueList = tmpManage.GetZDValueList(tmpField.GetFieldEnumCode());
                                                        DataSource tempDatasource = PubVar.m_Workspace.GetDataSourceByName(DataQuery_Dialog.this.m_SelectLayer.GetDataSourceName());
                                                        if (!(tempDatasource == null || (localDataset2 = tempDatasource.GetDatasetByName(DataQuery_Dialog.this.m_SelectLayer.GetLayerID())) == null || localDataset2.getDataSource() == null || localDataset2.getDataSource()._EDatabase == null)) {
                                                            HashMap<String, String> tmpValueCodeList = new HashMap<>();
                                                            HashMap<String, String> tmpCodeValueList = new HashMap<>();
                                                            for (String tmpStr : tmpDctValueList) {
                                                                int tmpI01 = tmpStr.indexOf("(");
                                                                String tmpCode01 = tmpStr.substring(0, tmpI01);
                                                                String tmpValue01 = tmpStr.substring(tmpI01 + 1, tmpStr.length() - 1);
                                                                tmpValueCodeList.put(tmpValue01, tmpCode01);
                                                                tmpCodeValueList.put(tmpCode01, tmpValue01);
                                                            }
                                                            SQLiteReader tempSQLiteDataReader2 = localDataset2.getDataSource()._EDatabase.Query("Select SYS_ID," + tmpField.GetDataFieldName() + " From " + localDataset2.getDataTableName() + " Where SYS_STATUS=0 ");
                                                            while (tempSQLiteDataReader2.Read()) {
                                                                try {
                                                                    String tmpSYSID = String.valueOf(tempSQLiteDataReader2.GetInt32(0));
                                                                    String tmpDictValue = tempSQLiteDataReader2.GetString(1);
                                                                    if (tmpValueCodeList.containsKey(tmpDictValue)) {
                                                                        localDataset2.getDataSource()._EDatabase.ExecuteSQL("Update " + localDataset2.getDataTableName() + " Set " + tmpField.GetDataFieldName() + "='" + tmpValueCodeList.get(tmpDictValue) + "' Where SYS_ID=" + tmpSYSID);
                                                                    }
                                                                } catch (Exception e) {
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                Common.ShowDialog("转换数据字典文字为代码\r\n完成.");
                                                return;
                                            }
                                            Common.ShowDialog("没有选择任何字段.");
                                        } catch (Exception ex) {
                                            Common.Log("错误", ex.getMessage());
                                        }
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Dialog.1.3
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        arg0.dismiss();
                                    }
                                }).show();
                                return;
                            }
                            Common.ShowDialog("该图层中没有定义为数据字典的字段.");
                            return;
                        }
                        Common.ShowDialog("查询图层不能为空.");
                    } else if (paramString.equals("直接转换数据字典代码为文字")) {
                        DataQuery_Dialog.this.m_SelectMultiList = new ArrayList();
                        if (DataQuery_Dialog.this.m_SelectLayer != null) {
                            final List tmpFieldArray2 = DataQuery_Dialog.this.getDataDictFieldsName();
                            if (tmpFieldArray2.size() > 0) {
                                new AlertDialog.Builder(DataQuery_Dialog.this._Dialog.getContext(), 3).setTitle("请选择转换的字段:").setMultiChoiceItems((String[]) tmpFieldArray2.toArray(new String[tmpFieldArray2.size()]), (boolean[]) null, new DialogInterface.OnMultiChoiceClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Dialog.1.4
                                    @Override // android.content.DialogInterface.OnMultiChoiceClickListener
                                    public void onClick(DialogInterface arg0, int arg1, boolean arg2) {
                                        if (!arg2) {
                                            int tmpI = DataQuery_Dialog.this.m_SelectMultiList.indexOf(Integer.valueOf(arg1));
                                            if (tmpI > -1) {
                                                DataQuery_Dialog.this.m_SelectMultiList.remove(tmpI);
                                            }
                                        } else if (!DataQuery_Dialog.this.m_SelectMultiList.contains(Integer.valueOf(arg1))) {
                                            DataQuery_Dialog.this.m_SelectMultiList.add(Integer.valueOf(arg1));
                                        }
                                    }
                                }).setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Dialog.1.5
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        DataDictionaryManage tmpManage;
                                        DataSource tempDatasource;
                                        DataSet localDataset2;
                                        try {
                                            if (DataQuery_Dialog.this.m_SelectMultiList.size() > 0) {
                                                for (Integer num : DataQuery_Dialog.this.m_SelectMultiList) {
                                                    LayerField tmpField = DataQuery_Dialog.this.m_SelectLayer.getLayerFieldByFieldName((String) tmpFieldArray2.get(num.intValue()));
                                                    if (!(tmpField == null || (tmpManage = PubVar._PubCommand.m_ConfigDB.GetDataDictionaryManage()) == null)) {
                                                        List<String> tmpDctValueList = tmpManage.GetZDValueList(tmpField.GetFieldEnumCode());
                                                        tmpDctValueList.size();
                                                        if (!(tmpDctValueList.size() <= 0 || (tempDatasource = PubVar.m_Workspace.GetDataSourceByName(DataQuery_Dialog.this.m_SelectLayer.GetDataSourceName())) == null || (localDataset2 = tempDatasource.GetDatasetByName(DataQuery_Dialog.this.m_SelectLayer.GetLayerID())) == null || localDataset2.getDataSource() == null || localDataset2.getDataSource()._EDatabase == null)) {
                                                            HashMap<String, String> tmpValueCodeList = new HashMap<>();
                                                            HashMap<String, String> tmpCodeValueList = new HashMap<>();
                                                            for (String tmpStr : tmpDctValueList) {
                                                                int tmpI01 = tmpStr.indexOf("(");
                                                                String tmpCode01 = tmpStr.substring(0, tmpI01);
                                                                String tmpValue01 = tmpStr.substring(tmpI01 + 1, tmpStr.length() - 1);
                                                                tmpValueCodeList.put(tmpValue01, tmpCode01);
                                                                tmpCodeValueList.put(tmpCode01, tmpValue01);
                                                            }
                                                            SQLiteReader tempSQLiteDataReader2 = localDataset2.getDataSource()._EDatabase.Query("Select SYS_ID," + tmpField.GetDataFieldName() + " From " + localDataset2.getDataTableName() + " Where SYS_STATUS=0 ");
                                                            while (tempSQLiteDataReader2.Read()) {
                                                                try {
                                                                    String tmpSYSID = String.valueOf(tempSQLiteDataReader2.GetInt32(0));
                                                                    String tmpDictCode = tempSQLiteDataReader2.GetString(1);
                                                                    if (tmpCodeValueList.containsKey(tmpDictCode)) {
                                                                        localDataset2.getDataSource()._EDatabase.ExecuteSQL("Update " + localDataset2.getDataTableName() + " Set " + tmpField.GetDataFieldName() + "='" + tmpCodeValueList.get(tmpDictCode) + "' Where SYS_ID=" + tmpSYSID);
                                                                    }
                                                                } catch (Exception e) {
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                Common.ShowDialog("转换数据字典代码为文字\r\n完成.");
                                                return;
                                            }
                                            Common.ShowDialog("没有选择任何字段.");
                                        } catch (Exception ex) {
                                            Common.Log("错误", ex.getMessage());
                                        }
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Dialog.1.6
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        arg0.dismiss();
                                    }
                                }).show();
                                return;
                            }
                            Common.ShowDialog("该图层中没有定义为数据字典的字段.");
                            return;
                        }
                        Common.ShowDialog("查询图层不能为空.");
                    } else if (paramString.equals("属性值自动计算")) {
                        FieldValuesCal_Dialog tmpDialog = new FieldValuesCal_Dialog();
                        tmpDialog.SetCallback(DataQuery_Dialog.this.pCallback);
                        tmpDialog.setFeatureLayer(DataQuery_Dialog.this.m_SelectLayer);
                        tmpDialog.ShowDialog();
                    } else if (paramString.equals("统计分析")) {
                        Statistic_Dialog tmpDialog2 = new Statistic_Dialog();
                        tmpDialog2.SetCallback(DataQuery_Dialog.this.pCallback);
                        tmpDialog2.SetLayer(DataQuery_Dialog.this.m_SelectLayer);
                        tmpDialog2.SetCondition(DataQuery_Dialog.this.m_QueryCondition);
                        tmpDialog2.ShowDialog();
                    } else if (paramString.equals("计算蓄积")) {
                        if (DataQuery_Dialog.this.m_SelectLayer != null) {
                            GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByID(DataQuery_Dialog.this.m_SelectLayer.GetLayerID());
                            if (tmpGeoLayer != null) {
                                CalXuJi_Dialog tmpDialog3 = new CalXuJi_Dialog(tmpGeoLayer);
                                tmpDialog3.SetCallback(DataQuery_Dialog.this.pCallback);
                                tmpDialog3.ShowDialog();
                                return;
                            }
                            Common.ShowDialog("查询图层不能为空.");
                            return;
                        }
                        Common.ShowDialog("查询图层不能为空.");
                    }
                } catch (Exception e) {
                }
            }
        };
        this.layersSpinner = null;
        this.m_QueryCondition = null;
        this.SeletedObjectsList = new ArrayList();
        this.m_XLayerList = new ArrayList();
        this.m_SelectMultiList = new ArrayList();
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.dataquery_dialog);
        this._Dialog.Resize(1.0f, PubVar.DialogHeightRatio);
        this._Dialog.SetCaption("数据查询");
        this._Dialog.SetHeadButtons("1,2130837673,导出,导出", this.pCallback);
        this._Dialog.HideKeybroad();
        this.layersSpinner = (InputSpinner) this._Dialog.findViewById(R.id.sp_myQueryLayersList);
        this._Dialog.findViewById(R.id.buttonSetting).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonDelete).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonLoc).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonFilter).setOnClickListener(new ViewClick());
        this.textviewInfo = (TextView) this._Dialog.findViewById(R.id.textviewInfo);
        this._Dialog.findViewById(R.id.buttonSelectAll).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectDe).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_queryData).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.button_Tools).setOnClickListener(new ViewClick());
        HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_Query_Page_Row");
        if (tempHashMap != null && (tempStr = tempHashMap.get("F2")) != null) {
            this.queryPageRow = tempStr;
        }
    }

    public void SetQueryLayer(FeatureLayer layer) {
        this.m_SelectLayer = layer;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayerList() {
        try {
            ArrayList tempArray = new ArrayList();
            if (this.m_LayerList != null && this.m_LayerList.size() > 0) {
                if (this.m_SelectLayer == null) {
                    this.m_SelectLayer = this.m_LayerList.get(0);
                }
                for (FeatureLayer tempLayer : this.m_LayerList) {
                    if (tempLayer != null) {
                        tempArray.add(tempLayer.GetLayerName());
                    }
                }
            }
            this.layersSpinner.setEditTextEnable(false);
            if (this.m_SelectLayer != null) {
                this.layersSpinner.getEditTextView().setText(this.m_SelectLayer.GetLayerName());
            }
            this.layersSpinner.SetCallback(new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Dialog.2
                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                public void OnClick(String paramString, Object pObject) {
                    Layer_Select_Dialog tempDialog = new Layer_Select_Dialog();
                    tempDialog.SetLayersList(DataQuery_Dialog.this.m_XLayerList);
                    tempDialog.SetLayerSelectType(2);
                    tempDialog.SetCallback(DataQuery_Dialog.this.pCallback);
                    tempDialog.ShowDialog();
                }
            });
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void queryDataList(String condition) {
        DataSet localDataset;
        this.m_QueryCondition = condition;
        try {
            this.m_MyTableDataList = new ArrayList();
            if (this.m_SelectLayer != null) {
                List<LayerField> tmpFields = this.m_SelectLayer.GetFieldList();
                StringBuilder tempFIDSB = new StringBuilder();
                StringBuilder tempColumSB = new StringBuilder();
                StringBuilder tempColumTypeSB = new StringBuilder();
                List<Integer> tempColWidthList = new ArrayList<>();
                tempColumSB.append("选择");
                tempColumTypeSB.append("checkbox");
                tempColWidthList.add(50);
                int tmpShowFIDCount = 0;
                for (LayerField tempField : tmpFields) {
                    Object tempTag = tempField.GetTag();
                    if (tempTag == null || tempTag.toString().equals("true")) {
                        if (tempFIDSB.length() > 0) {
                            tempFIDSB.append(",");
                        }
                        tempFIDSB.append(tempField.GetDataFieldName());
                        if (tempColumSB.length() > 0) {
                            tempColumSB.append(",");
                        }
                        tempColumSB.append(tempField.GetFieldName());
                        if (tempColumTypeSB.length() > 0) {
                            tempColumTypeSB.append(",");
                        }
                        tempColumTypeSB.append("text");
                        tempColWidthList.add(100);
                        tmpShowFIDCount++;
                    }
                }
                DataSource tempDatasource = PubVar.m_Workspace.GetDataSourceByName(this.m_SelectLayer.GetDataSourceName());
                if (!(tempDatasource == null || (localDataset = tempDatasource.GetDatasetByName(this.m_SelectLayer.GetLayerID())) == null || localDataset.getDataSource() == null || localDataset.getDataSource()._EDatabase == null)) {
                    String tempSql = "Select SYS_ID," + tempFIDSB.toString() + " From " + localDataset.getDataTableName() + " Where SYS_STATUS=0 ";
                    if (condition != null && !condition.equals("")) {
                        tempSql = String.valueOf(tempSql) + " And (" + condition + ") ";
                    }
                    if (!this.queryPageRow.equals("全部")) {
                        tempSql = String.valueOf(tempSql) + " Limit " + this.queryPageRow;
                    }
                    int tmpTid = -1;
                    SQLiteReader tempSQLiteDataReader = localDataset.getDataSource()._EDatabase.Query(tempSql);
                    while (tempSQLiteDataReader.Read()) {
                        tmpTid++;
                        HashMap tmpHashMap = new HashMap();
                        tmpHashMap.put("D1", false);
                        tmpHashMap.put("D2", tempSQLiteDataReader.GetString(0));
                        for (int i = 1; i <= tmpShowFIDCount; i++) {
                            tmpHashMap.put("F" + i, tempSQLiteDataReader.GetString(i));
                        }
                        tmpHashMap.put("Index", Integer.valueOf(tmpTid));
                        this.m_MyTableDataList.add(tmpHashMap);
                    }
                }
                List<String> tempFieldIDArray = new ArrayList<>();
                tempFieldIDArray.add("D1");
                for (int i2 = 1; i2 <= tmpShowFIDCount; i2++) {
                    tempFieldIDArray.add("F" + i2);
                }
                this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.prj_list), "自定义", tempColumSB.toString(), tempColumTypeSB.toString(), Common.ListIntToArray(tempColWidthList), this.pCallback);
                this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, (String[]) tempFieldIDArray.toArray(new String[tempFieldIDArray.size()]), this.pCallback);
                this.textviewInfo.setText("数量:" + this.m_MyTableDataList.size());
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayerFields() {
        if (this.m_SelectLayer != null) {
            List<LayerField> tmpFields = this.m_SelectLayer.GetFieldList();
            StringBuilder tempSB = new StringBuilder();
            StringBuilder tempSB2 = new StringBuilder();
            List<Integer> tmpList = new ArrayList<>();
            tempSB.append("选择");
            tempSB2.append("checkbox");
            tmpList.add(50);
            for (LayerField tempField : tmpFields) {
                Object tempTag = tempField.GetTag();
                if (tempTag == null || tempTag.toString().equals("true")) {
                    if (tempSB.length() > 0) {
                        tempSB.append(",");
                    }
                    tempSB.append(tempField.GetFieldName());
                    if (tempSB2.length() > 0) {
                        tempSB2.append(",");
                    }
                    tempSB2.append("text");
                    tmpList.add(100);
                }
            }
            this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.prj_list), "自定义", tempSB.toString(), tempSB2.toString(), Common.ListIntToArray(tmpList), this.pCallback);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        GeoLayer localGeoLayer;
        DataSet pDataset;
        Envelope tempExtend;
        try {
            if (command.equals("查询全部")) {
                queryDataList(null);
            } else if (command.equals("设置")) {
                if (this.m_SelectLayer != null) {
                    DataQuerySetting_Dialog tempDialog = new DataQuerySetting_Dialog();
                    tempDialog.SetLayerFields(this.m_SelectLayer.GetFieldList());
                    tempDialog.SetCallback(this.pCallback);
                    tempDialog.ShowDialog();
                } else {
                    Common.ShowDialog("没有选择任何图层对象.");
                }
            } else if (command.equals("过滤")) {
                if (this.m_SelectLayer != null) {
                    DataQuery_Filter_Dialog tempDialog2 = new DataQuery_Filter_Dialog();
                    tempDialog2.SetSelectLayer(this.m_SelectLayer);
                    tempDialog2.SetCallback(this.pCallback);
                    tempDialog2.ShowDialog();
                } else {
                    Common.ShowDialog("没有选择任何图层对象.");
                }
            } else if (command.equals("全选")) {
                if (this.m_MyTableDataList != null) {
                    for (HashMap<String, Object> tmpHashMap : this.m_MyTableDataList) {
                        tmpHashMap.put("D1", true);
                    }
                    this.m_MyTableFactory.notifyDataSetInvalidated();
                }
            } else if (command.equals("反选") && this.m_MyTableDataList != null) {
                for (HashMap<String, Object> tmpHashMap2 : this.m_MyTableDataList) {
                    tmpHashMap2.put("D1", Boolean.valueOf(!((Boolean) tmpHashMap2.get("D1")).booleanValue()));
                }
                this.m_MyTableFactory.notifyDataSetInvalidated();
            }
            if (command.equals("缩放至图层")) {
                if (this.m_SelectLayer != null) {
                    DataSource pDataSource = PubVar.m_Workspace.GetDataSourceByName(this.m_SelectLayer.GetDataSourceName());
                    if (!(pDataSource == null || (pDataset = pDataSource.GetDatasetByName(this.m_SelectLayer.GetLayerID())) == null || (tempExtend = pDataset.GetExtendFromDB()) == null)) {
                        PubVar._Map.ZoomToExtend(tempExtend.Scale(2.0d));
                        Common.ShowToast("已经缩放至图层【" + this.m_SelectLayer.GetLayerName() + "】全图范围.");
                        return;
                    }
                    return;
                }
                Common.ShowDialog("没有选择任何图层对象.");
            } else if (command.equals("删除") || command.equals("定位")) {
                this.SeletedObjectsList = new ArrayList();
                if (this.m_MyTableDataList != null) {
                    for (HashMap<String, Object> tmpHashMap3 : this.m_MyTableDataList) {
                        if (((Boolean) tmpHashMap3.get("D1")).booleanValue()) {
                            this.SeletedObjectsList.add(tmpHashMap3.get("D2").toString());
                        }
                    }
                }
                if (this.SeletedObjectsList.size() <= 0) {
                    Common.ShowDialog("请至少选择一行数据.");
                } else if (command.equals("删除")) {
                    DataSource tempDatasource = PubVar.m_Workspace.GetDataSourceByName(this.m_SelectLayer.GetDataSourceName());
                    if (tempDatasource == null || !tempDatasource.getEditing()) {
                        Common.ShowDialog("该图层为非编辑图层,不能编辑数据.");
                    } else {
                        Common.ShowYesNoDialog(this._Dialog.getContext(), "是否删除选择的数据?\r\n删除后数据将无法恢复.", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Dialog.3
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString, Object pObject) {
                                if (DataQuery_Dialog.this.m_SelectLayer != null && PubVar._PubCommand.GetDeleteObject().Delete(DataQuery_Dialog.this.m_SelectLayer.GetLayerID(), DataQuery_Dialog.this.SeletedObjectsList, true)) {
                                    if (DataQuery_Dialog.this.m_MyTableDataList != null) {
                                        int i = 0;
                                        int count = DataQuery_Dialog.this.m_MyTableDataList.size();
                                        while (i < count) {
                                            if (((Boolean) ((HashMap) DataQuery_Dialog.this.m_MyTableDataList.get(i)).get("D1")).booleanValue()) {
                                                DataQuery_Dialog.this.m_MyTableDataList.remove(i);
                                                i--;
                                                count--;
                                            }
                                            i++;
                                        }
                                        DataQuery_Dialog.this.m_MyTableFactory.notifyDataSetInvalidated();
                                    }
                                    PubVar._Map.RefreshFast();
                                }
                            }
                        });
                    }
                } else if (command.equals("定位") && this.m_SelectLayer != null && (localGeoLayer = PubVar._Map.GetGeoLayerByDataSource(this.m_SelectLayer.GetDataSourceName(), this.m_SelectLayer.GetLayerID())) != null) {
                    if (localGeoLayer.getVisible()) {
                        Selection tmpSelection = PubVar._Map.getSelectionByLayerID(localGeoLayer.getLayerID(), false);
                        if (tmpSelection != null && tmpSelection.getCount() > 0) {
                            tmpSelection.RemoveAll();
                        }
                        Iterator<String> tempIter = this.SeletedObjectsList.iterator();
                        AbstractGeometry tempGeo = localGeoLayer.getDataset().GetGeometryBySYSIDMust(tempIter.next());
                        if (tempGeo != null) {
                            if (tmpSelection != null) {
                                tmpSelection.Add(tempGeo);
                            }
                            Envelope tempExtend2 = tempGeo.getEnvelope();
                            if (tempIter.hasNext()) {
                                while (tempIter.hasNext()) {
                                    AbstractGeometry tempGeo2 = localGeoLayer.getDataset().GetGeometryBySYSID(tempIter.next());
                                    if (tempGeo2 != null) {
                                        if (tmpSelection != null) {
                                            tmpSelection.Add(tempGeo2);
                                        }
                                        tempExtend2 = tempExtend2.MergeEnvelope(tempGeo2.getEnvelope());
                                    }
                                }
                            }
                            if (tempExtend2.getHeight() > 0.0d) {
                                PubVar._Map.ZoomToExtend(tempExtend2.Scale(2.0d));
                            } else {
                                PubVar._Map.ZoomToCenter(tempExtend2.getCenter());
                            }
                            Common.ShowToast("在地图上已经定位.");
                            return;
                        }
                        return;
                    }
                    Common.ShowDialog("图层【" + this.m_SelectLayer.GetLayerName() + "】不可见,不能显示定位.");
                }
            } else if (command.equals("功能")) {
                new AlertDialog.Builder(this._Dialog.getContext(), 3).setTitle("请选择工具:").setSingleChoiceItems(new String[]{"转换数据字典代码为文字", "转换数据字典文字为代码", "属性值计算", "统计分析", "计算蓄积"}, -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Dialog.4
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface arg0, int arg1) {
                        DataQuery_Dialog.this.m_SelectMultiList = new ArrayList();
                        if (arg1 == 1) {
                            DataQuery_Dialog.this.pCallback.OnClick("直接转换数据字典文字为代码", null);
                        } else if (arg1 == 0) {
                            DataQuery_Dialog.this.pCallback.OnClick("直接转换数据字典代码为文字", null);
                        } else if (arg1 == 2) {
                            DataQuery_Dialog.this.pCallback.OnClick("属性值自动计算", null);
                        } else if (arg1 == 3) {
                            DataQuery_Dialog.this.pCallback.OnClick("统计分析", null);
                        } else if (arg1 == 4) {
                            DataQuery_Dialog.this.pCallback.OnClick("计算蓄积", null);
                        }
                        arg0.dismiss();
                    }
                }).show();
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this.m_LayerList = PubVar._PubCommand.m_ProjectDB.GetLayerManage().CopyLayerList();
        List<FeatureLayer> tmpLayers = PubVar._PubCommand.m_ProjectDB.GetBKVectorLayerManage().GetLayerListCopy();
        if (tmpLayers != null && tmpLayers.size() > 0) {
            this.m_LayerList.addAll(tmpLayers);
        }
        this.m_XLayerList.addAll(PubVar._Map.getGeoLayers().getList());
        this.m_XLayerList.addAll(PubVar._Map.getVectorGeoLayers().getList());
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.DataQuery_Dialog.5
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                DataQuery_Dialog.this.refreshLayerList();
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private List getDataDictFieldsName() {
        List tmpFieldArray = new ArrayList();
        if (this.m_SelectLayer != null) {
            for (LayerField tmpField : this.m_SelectLayer.GetFieldList()) {
                if (!tmpField.GetFieldEnumCode().equals("")) {
                    tmpFieldArray.add(tmpField.GetFieldName());
                }
            }
        }
        return tmpFieldArray;
    }

    private int getDictValueIndex(List<String> tmpDctValueList, String value) {
        int tid = 0;
        String tmpValue = "(" + value + ")";
        for (String tmpStr : tmpDctValueList) {
            tid++;
            if (tmpStr.contains(tmpValue)) {
                return tid;
            }
        }
        return -1;
    }

    private String getDictCodeByValue(List<String> tmpDctValueList, String value) {
        String tmpValue = "(" + value + ")";
        for (String tmpStr : tmpDctValueList) {
            if (tmpStr.contains(tmpValue)) {
                return tmpStr.substring(0, tmpStr.indexOf("("));
            }
        }
        return value;
    }

    private String getDictValueByCode(List<String> tmpDctValueList, String code) {
        String tmpValue = String.valueOf(code) + "(";
        for (String tmpStr : tmpDctValueList) {
            if (tmpStr.contains(tmpValue)) {
                return tmpStr.substring(tmpStr.indexOf("(") + 1, tmpStr.lastIndexOf(")"));
            }
        }
        return code;
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                DataQuery_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
