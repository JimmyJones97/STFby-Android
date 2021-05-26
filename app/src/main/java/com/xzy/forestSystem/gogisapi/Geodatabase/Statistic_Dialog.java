package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import com.xzy.forestSystem.hellocharts.listener.PieChartOnValueSelectListener;
import com.xzy.forestSystem.hellocharts.model.PieChartData;
import com.xzy.forestSystem.hellocharts.model.SliceValue;
import com.xzy.forestSystem.hellocharts.util.ChartUtils;
import com.xzy.forestSystem.hellocharts.view.PieChartView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistic_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private String m_QueryCondition;
    private FeatureLayer m_SelectLayer;
    List<SliceValue> m_SliceValue;
    private String m_TableHeadString;
    private ICallback pCallback;
    private PieChartData pieChardata;
    private PieChartView pieChart;
    private PieChartOnValueSelectListener selectListener;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public Statistic_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_SelectLayer = null;
        this.m_QueryCondition = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this.m_TableHeadString = "序号,统计类型,统计值";
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.Statistic_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
            }
        };
        this.m_SliceValue = new ArrayList();
        this.selectListener = new PieChartOnValueSelectListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.Statistic_Dialog.2
            @Override // lecho.lib.hellocharts.listener.OnValueDeselectListener
            public void onValueDeselected() {
            }

            @Override // lecho.lib.hellocharts.listener.PieChartOnValueSelectListener
            public void onValueSelected(int arg0, SliceValue value) {
                Common.ShowToast(String.valueOf(String.valueOf(value.getLabel())) + ":" + value.getValue());
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.statistic_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("数据统计");
        this._Dialog.findViewById(R.id.btn_StartStatistic).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_ChangeViewTab).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_ExportStatisticTb).setOnClickListener(new ViewClick());
        this.m_MyTableFactory = new MyTableFactory();
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.statistic_list), "自定义", this.m_TableHeadString, "text,text,text", new int[]{-15, -45, -40}, this.pCallback);
        this.pieChart = (PieChartView) this._Dialog.findViewById(R.id.pie_chart);
        this.pieChart.setOnValueTouchListener(this.selectListener);
    }

    private void initPieChart() {
        this.pieChardata = new PieChartData();
        this.pieChardata.setHasLabels(true);
        this.pieChardata.setHasLabelsOnlyForSelected(false);
        this.pieChardata.setHasLabelsOutside(false);
        this.pieChardata.setValues(this.m_SliceValue);
        this.pieChart.setPieChartData(this.pieChardata);
        this.pieChart.setValueSelectionEnabled(true);
        this.pieChart.setAlpha(0.9f);
        this.pieChart.setCircleFillRatio(1.0f);
    }

    public void SetLayer(FeatureLayer featLayer) {
        this.m_SelectLayer = featLayer;
    }

    public void SetCondition(String condition) {
        this.m_QueryCondition = condition;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void initialLayout() {
        if (this.m_SelectLayer != null) {
            List<String> tmpFieldsList = new ArrayList<>();
            HashMap<String, String> tmpHashMap = this.m_SelectLayer.getFieldsNameArray();
            if (tmpHashMap != null && tmpHashMap.size() > 0) {
                for (Map.Entry<String, String> tmpEntry : tmpHashMap.entrySet()) {
                    tmpFieldsList.add(tmpEntry.getKey());
                }
            }
            String tmpString = tmpFieldsList.get(0);
            Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_stasField, tmpString, "请选择分类字段", tmpFieldsList, (String) null, (ICallback) null);
            if (tmpFieldsList.size() > 1) {
                tmpString = tmpFieldsList.get(1);
            }
            Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_stasField2, tmpString, "请选择统计字段", tmpFieldsList, (String) null, (ICallback) null);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    @SuppressLint("WrongConstant")
    private void DoCommand(String command) {
        DataSet localDataset;
        String tmpSql2;
        try {
            if (command.equals("开始统计")) {
                this.m_MyTableDataList = new ArrayList();
                this.m_SliceValue = new ArrayList();
                String tmpFIDName01 = Common.GetViewValue(this._Dialog, R.id.sp_stasField);
                if (tmpFIDName01 == null || tmpFIDName01.trim().length() == 0) {
                    Common.ShowDialog("统计的分类字段不能为空.");
                    return;
                }
                String tmpFIDName02 = Common.GetViewValue(this._Dialog, R.id.sp_stasField2);
                if (tmpFIDName02 == null || tmpFIDName02.trim().length() == 0) {
                    Common.ShowDialog("统计字段不能为空.");
                    return;
                }
                String tmpFID01 = this.m_SelectLayer.GetDataFieldByFieldName(tmpFIDName01);
                String tmpFID02 = this.m_SelectLayer.GetDataFieldByFieldName(tmpFIDName02);
                this.m_TableHeadString = "序号," + tmpFIDName01;
                if (tmpFID01.equals(tmpFID02)) {
                    this.m_TableHeadString = String.valueOf(this.m_TableHeadString) + ",数量";
                } else {
                    this.m_TableHeadString = String.valueOf(this.m_TableHeadString) + "," + tmpFIDName02;
                }
                this.m_MyTableFactory = new MyTableFactory();
                this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.statistic_list), "自定义", this.m_TableHeadString, "text,text,text", new int[]{-15, -45, -40}, this.pCallback);
                DataSource tempDatasource = PubVar.m_Workspace.GetDataSourceByName(this.m_SelectLayer.GetDataSourceName());
                if (tempDatasource != null && (localDataset = tempDatasource.GetDatasetByName(this.m_SelectLayer.GetLayerID())) != null && localDataset.getDataSource() != null && localDataset.getDataSource()._EDatabase != null) {
                    if (tmpFID01.equals(tmpFID02)) {
                        tmpSql2 = "COUNT(*) As ParaValue";
                    } else {
                        tmpSql2 = "SUM(" + tmpFID02 + ") As ParaValue";
                    }
                    String tmpSql = "Select " + tmpFID01 + " As ParaName," + tmpSql2 + " From " + localDataset.getDataTableName() + " Where SYS_STATUS=0 ";
                    if (this.m_QueryCondition != null && !this.m_QueryCondition.equals("")) {
                        tmpSql = String.valueOf(tmpSql) + " And (" + this.m_QueryCondition + ") ";
                    }
                    SQLiteReader tempSQLiteDataReader = localDataset.getDataSource()._EDatabase.Query(String.valueOf(tmpSql) + " Group By " + tmpFID01);
                    int tmpTid = 1;
                    while (tempSQLiteDataReader.Read()) {
                        HashMap tmpHashMap = new HashMap();
                        tmpTid++;
                        tmpHashMap.put("D1", Integer.valueOf(tmpTid));
                        String tmpName = tempSQLiteDataReader.GetString(0);
                        tmpHashMap.put("D2", tmpName);
                        String tmpString = tempSQLiteDataReader.GetString(1);
                        tmpHashMap.put("D3", tmpString);
                        this.m_MyTableDataList.add(tmpHashMap);
                        double tmpDouble01 = 0.0d;
                        try {
                            tmpDouble01 = Double.parseDouble(tmpString);
                        } catch (Exception e) {
                        }
                        SliceValue tmpSliceValue = new SliceValue(0.0f, ChartUtils.pickColor());
                        tmpSliceValue.setValue((float) tmpDouble01);
                        tmpSliceValue.setLabel(tmpName);
                        this.m_SliceValue.add(tmpSliceValue);
                    }
                    this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2", "D3"}, this.pCallback);
                    initPieChart();
                }
            } else if (command.equals("显示切换")) {
                if (this._Dialog.findViewById(R.id.ll_chartView).getVisibility() == 8) {
                    this._Dialog.findViewById(R.id.ll_chartView).setVisibility(0);
                    this._Dialog.findViewById(R.id.ll_tableView).setVisibility(8);
                    Common.SetTextViewValueOnID(this._Dialog, (int) R.id.btn_ChangeViewTab, "显示表格");
                    return;
                }
                this._Dialog.findViewById(R.id.ll_chartView).setVisibility(8);
                this._Dialog.findViewById(R.id.ll_tableView).setVisibility(0);
                Common.SetTextViewValueOnID(this._Dialog, (int) R.id.btn_ChangeViewTab, "显示统计图");
            } else if (!command.equals("导出统计表")) {
            } else {
                if (this.m_MyTableDataList.size() > 0) {
                    String tmpFilePathString = String.valueOf(Common.GetAPPPath()) + "/统计结果" + Common.fileDateFormat.format(new Date()) + ".csv";
                    exportCSV(tmpFilePathString);
                    Common.ShowDialog("导出统计表成功!\r\n" + tmpFilePathString);
                    return;
                }
                Common.ShowToast("没有可导出的统计数据.");
            }
        } catch (Exception e2) {
        }
    }

    private void exportCSV(String _outputPath) {
        OutputStreamWriter osw = null;
        try {
            OutputStreamWriter osw2 = new OutputStreamWriter(new FileOutputStream(new File(_outputPath)), "gbk");
            try {
                osw2.write(String.valueOf(this.m_TableHeadString) + "\r\n");
                if (this.m_MyTableDataList.size() > 0) {
                    for (HashMap<String, Object> tmpHashMap : this.m_MyTableDataList) {
                        osw2.write(String.valueOf(String.valueOf(tmpHashMap.get("D1"))) + "," + String.valueOf(tmpHashMap.get("D2")) + "," + String.valueOf(tmpHashMap.get("D3")));
                        osw2.write("\r\n");
                    }
                }
                osw2.flush();
                osw2.close();
            } catch (Exception e) {
                osw = osw2;
                try {
                    osw.close();
                } catch (Exception e2) {
                }
            }
        } catch (Exception e3) {
            try {
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Geodatabase.Statistic_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Statistic_Dialog.this.initialLayout();
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
                Statistic_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
