package com.xzy.forestSystem.GuiZhou.DiaoCha;

import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import com.xzy.forestSystem.gogisapi.MyControls.AutoInputSpinner;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.List;

public class CalXuJi_Dialog {
    private XDialogTemplate _Dialog = null;
    private ICallback m_Callback = null;
    private FeatureLayer m_FeatureLayer = null;
    private GeoLayer m_GeoLayer = null;
    private ICallback pCallback = new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.CalXuJi_Dialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String command, Object object) {
        }
    };

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public CalXuJi_Dialog(GeoLayer geoLayer) {
        this.m_GeoLayer = geoLayer;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.calxuji_dialog2);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("计算蓄积");
        this.m_FeatureLayer = PubVar._PubCommand.m_ProjectDB.getFeatureLayerByID(this.m_GeoLayer.getLayerID());
        this._Dialog.findViewById(R.id.btn_Cal).setOnClickListener(new View.OnClickListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.CalXuJi_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                String tmpFName01 = Common.GetViewValue(CalXuJi_Dialog.this._Dialog, R.id.et_BasicInput01).trim();
                if (tmpFName01.length() == 0) {
                    Common.ShowDialog("请选择树种字段!");
                    return;
                }
                String tmpFName02 = Common.GetViewValue(CalXuJi_Dialog.this._Dialog, R.id.et_BasicInput02).trim();
                if (tmpFName02.length() == 0) {
                    Common.ShowDialog("请选择胸径字段!");
                    return;
                }
                String tmpFName03 = Common.GetViewValue(CalXuJi_Dialog.this._Dialog, R.id.et_BasicInput03).trim();
                if (tmpFName03.length() == 0) {
                    Common.ShowDialog("请选择树高字段!");
                    return;
                }
                String tmpFName04 = Common.GetViewValue(CalXuJi_Dialog.this._Dialog, R.id.et_BasicInput04).trim();
                if (tmpFName04.length() == 0) {
                    Common.ShowDialog("请选择株树字段!");
                    return;
                }
                String tmpFName05 = Common.GetViewValue(CalXuJi_Dialog.this._Dialog, R.id.et_BasicInput05).trim();
                if (tmpFName05.length() == 0) {
                    Common.ShowDialog("请选择蓄积字段!");
                    return;
                }
                String tmpFID01 = CalXuJi_Dialog.this.m_FeatureLayer.GetDataFieldByFieldName(tmpFName01);
                String tmpFID02 = CalXuJi_Dialog.this.m_FeatureLayer.GetDataFieldByFieldName(tmpFName02);
                String tmpFID03 = CalXuJi_Dialog.this.m_FeatureLayer.GetDataFieldByFieldName(tmpFName03);
                String tmpFID04 = CalXuJi_Dialog.this.m_FeatureLayer.GetDataFieldByFieldName(tmpFName04);
                String tmpFID05 = CalXuJi_Dialog.this.m_FeatureLayer.GetDataFieldByFieldName(tmpFName05);
                DataSet tmpDataSet = CalXuJi_Dialog.this.m_GeoLayer.getDataset();
                if (tmpDataSet != null) {
                    List<String[]> tmpListUpdateList = new ArrayList<>();
                    SQLiteReader tmpSQLiteReader = tmpDataSet.getDataSource().Query("Select " + tmpFID01 + "," + tmpFID02 + "," + tmpFID03 + "," + tmpFID04 + ",SYS_ID From " + tmpDataSet.getDataTableName());
                    if (tmpSQLiteReader != null) {
                        while (tmpSQLiteReader.Read()) {
                            String tmpV01 = tmpSQLiteReader.GetString(0).trim();
                            String tmpV02 = tmpSQLiteReader.GetString(1).trim();
                            String tmpV03 = tmpSQLiteReader.GetString(2).trim();
                            String tmpV04 = tmpSQLiteReader.GetString(3).trim();
                            String tmpSYSID = tmpSQLiteReader.GetString(4);
                            if (tmpV01.length() > 0) {
                                double tmpD01 = 0.0d;
                                double tmpD02 = 0.0d;
                                double tmpD03 = 0.0d;
                                try {
                                    tmpD01 = Double.parseDouble(tmpV02);
                                    tmpD02 = Double.parseDouble(tmpV03);
                                    tmpD03 = Double.parseDouble(tmpV04);
                                } catch (Exception e) {
                                }
                                if (!(tmpD01 == 0.0d || tmpD02 == 0.0d || tmpD03 == 0.0d)) {
                                    tmpListUpdateList.add(new String[]{tmpSYSID, CommonSetting.XUJIFormat.format(CommonSetting.CalXuJiValue(tmpV01, tmpD01, tmpD02) * tmpD03)});
                                }
                            }
                        }
                    }
                    if (tmpListUpdateList.size() > 0) {
                        String tmpSql01 = "Update " + tmpDataSet.getDataTableName() + " Set " + tmpFID05 + "='";
                        SQLiteDBHelper tmpSQLiteDBHelper = tmpDataSet.getDataSource().GetSQLiteDatabase();
                        tmpSQLiteDBHelper.BeginTransaction();
                        try {
                            for (String[] tmpStrings01 : tmpListUpdateList) {
                                tmpSQLiteDBHelper.ExecuteSQLSimple(String.valueOf(tmpSql01) + tmpStrings01[1] + "' Where SYS_ID=" + tmpStrings01[0]);
                            }
                        } catch (Exception e2) {
                        } finally {
                            tmpSQLiteDBHelper.SetTransactionSuccessful();
                            tmpSQLiteDBHelper.EndTransaction();
                        }
                        Common.ShowDialog("计算完成!\r\n共" + String.valueOf(tmpListUpdateList.size()) + "计算条数据.");
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshSelectList() {
        if (this.m_FeatureLayer != null) {
            List<String> tmpList01 = new ArrayList<>();
            for (String tmpString : this.m_FeatureLayer.GetFieldNamesList()) {
                tmpList01.add(tmpString);
            }
            AutoInputSpinner tmpAutoInputSpinner = (AutoInputSpinner) this._Dialog.findViewById(R.id.et_BasicInput01);
            tmpAutoInputSpinner.setTipList(tmpList01);
            tmpAutoInputSpinner.SetSelectItemList(tmpList01, "请选择树种字段");
            AutoInputSpinner tmpAutoInputSpinner2 = (AutoInputSpinner) this._Dialog.findViewById(R.id.et_BasicInput02);
            tmpAutoInputSpinner2.setTipList(tmpList01);
            tmpAutoInputSpinner2.SetSelectItemList(tmpList01, "请选择胸径字段");
            AutoInputSpinner tmpAutoInputSpinner3 = (AutoInputSpinner) this._Dialog.findViewById(R.id.et_BasicInput03);
            tmpAutoInputSpinner3.setTipList(tmpList01);
            tmpAutoInputSpinner3.SetSelectItemList(tmpList01, "请选择树高字段");
            AutoInputSpinner tmpAutoInputSpinner4 = (AutoInputSpinner) this._Dialog.findViewById(R.id.et_BasicInput04);
            tmpAutoInputSpinner4.setTipList(tmpList01);
            tmpAutoInputSpinner4.SetSelectItemList(tmpList01, "请选择株树字段");
            AutoInputSpinner tmpAutoInputSpinner5 = (AutoInputSpinner) this._Dialog.findViewById(R.id.et_BasicInput05);
            tmpAutoInputSpinner5.setTipList(tmpList01);
            tmpAutoInputSpinner5.SetSelectItemList(tmpList01, "请选择蓄积字段");
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.CalXuJi_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                CalXuJi_Dialog.this.refreshSelectList();
            }
        });
        this._Dialog.show();
    }
}
