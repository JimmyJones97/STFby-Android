package com.xzy.forestSystem.GuiZhou.DiaoCha;

import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.Input_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CeShuYinZiTable_Dialog {
    private XDialogTemplate _Dialog = null;
    private List<HashMap<String, Object>> m_MyTableDataList = new ArrayList();
    private MyTableFactory m_MyTableFactory = new MyTableFactory();
    private SQLiteDBHelper m_SQLiteDBHelper = null;
    private String m_YangdiName = "";
    private ICallback pCallback = new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.CeShuYinZiTable_Dialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String command, Object object) {
            Object[] tmpObjs;
            if (command.equals("计算")) {
                double tmpArea = 0.0d;
                SQLiteReader tmpSqLiteReader = CeShuYinZiTable_Dialog.this.m_SQLiteDBHelper.Query("Select YDArea From T_YangDiInfo Where YangDiName='" + CeShuYinZiTable_Dialog.this.m_YangdiName + "'");
                if (tmpSqLiteReader != null && tmpSqLiteReader.Read()) {
                    tmpArea = tmpSqLiteReader.GetDouble(0);
                }
                if (tmpArea == 0.0d) {
                    Common.ShowYesNoDialogWithAlert(CeShuYinZiTable_Dialog.this._Dialog.getContext(), "样地面积值为0,是否需要重新设置样地面积?", true, new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.CeShuYinZiTable_Dialog.1.1
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command2, Object pObject) {
                            if (command2.equals("YES")) {
                                Input_Dialog tmpDialog = new Input_Dialog();
                                tmpDialog.SetReturnTag("输入样地面积返回");
                                tmpDialog.SetCallback(CeShuYinZiTable_Dialog.this.pCallback);
                                tmpDialog.setValues("输入样地面积", "面积(公顷): ", "", "提示:请输入样地的面积值,单位为公顷.");
                                tmpDialog.setInputType(8192);
                                tmpDialog.ShowDialog();
                            }
                        }
                    });
                } else {
                    CeShuYinZiTable_Dialog.this.calulateTable(tmpArea);
                }
            } else if (command.equals("输入参数") && (tmpObjs = (Object[]) object) != null && tmpObjs.length > 1 && String.valueOf(tmpObjs[0]).equals("输入样地面积返回")) {
                double tmpArea2 = 0.0d;
                try {
                    tmpArea2 = Double.parseDouble(String.valueOf(tmpObjs[1]));
                } catch (Exception e) {
                }
                if (tmpArea2 > 0.0d) {
                    CeShuYinZiTable_Dialog.this.m_SQLiteDBHelper.ExecuteSQL("Update T_YangDiInfo Set YDArea=" + String.valueOf(tmpArea2) + " Where YangDiName='" + CeShuYinZiTable_Dialog.this.m_YangdiName + "'");
                    CeShuYinZiTable_Dialog.this.calulateTable(tmpArea2);
                    return;
                }
                Common.ShowDialog("输入的面积值无效.");
            }
        }
    };

    public CeShuYinZiTable_Dialog(String yangdiName) {
        this.m_YangdiName = yangdiName;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.ceshuyinzitable_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("测树因子计算表");
        this._Dialog.SetHeadButtons("1,2130837858,计算,计算", this.pCallback);
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_ceshu_layerlist), "自定义", "树种,平均胸径,平均树高,样地蓄积,公顷蓄积,样地株数,公顷株数", "text,text,text,text,text,text,text", new int[]{80, 80, 80, 80, 80, 80, 80}, this.pCallback);
        this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
        this.m_SQLiteDBHelper = CommonSetting.GetSQLiteDBHelper();
        refreshTable();
        this._Dialog.findViewById(R.id.btn_SettingYDArea).setOnClickListener(new View.OnClickListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.CeShuYinZiTable_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                double tmpArea = 0.0d;
                SQLiteReader tmpSqLiteReader = CeShuYinZiTable_Dialog.this.m_SQLiteDBHelper.Query("Select YDArea From T_YangDiInfo Where YangDiName='" + CeShuYinZiTable_Dialog.this.m_YangdiName + "'");
                if (tmpSqLiteReader != null && tmpSqLiteReader.Read()) {
                    tmpArea = tmpSqLiteReader.GetDouble(0);
                }
                Input_Dialog tmpDialog = new Input_Dialog();
                tmpDialog.SetReturnTag("输入样地面积返回");
                tmpDialog.SetCallback(CeShuYinZiTable_Dialog.this.pCallback);
                tmpDialog.setValues("输入样地面积", "面积(公顷): ", String.valueOf(tmpArea), "提示:请输入样地的面积值,单位为公顷.");
                tmpDialog.setInputType(8192);
                tmpDialog.ShowDialog();
            }
        });
    }

    private void refreshTable() {
        try {
            this.m_MyTableDataList.clear();
            SQLiteReader tmpLiteReader = this.m_SQLiteDBHelper.Query("Select ShuZhong,PJXJ,PJSG,YDXJ,GQXJ,YDZS,GQZS From T_CeShuYinZiTable_ShuZhong Where YangDiName='" + this.m_YangdiName + "'");
            while (tmpLiteReader.Read()) {
                HashMap<String, Object> tmpHash = new HashMap<>();
                tmpHash.put("D1", tmpLiteReader.GetString(0));
                tmpHash.put("D2", tmpLiteReader.GetString(1));
                tmpHash.put("D3", tmpLiteReader.GetString(2));
                tmpHash.put("D4", tmpLiteReader.GetString(3));
                tmpHash.put("D5", tmpLiteReader.GetString(4));
                tmpHash.put("D6", tmpLiteReader.GetString(5));
                tmpHash.put("D7", tmpLiteReader.GetString(6));
                this.m_MyTableDataList.add(tmpHash);
            }
            this.m_MyTableFactory.notifyDataSetChanged();
        } catch (Exception e) {
        }
    }

    public static boolean IsCalculated(String YangDiName) {
        try {
            SQLiteDBHelper tmpSqLiteDBHelper = CommonSetting.GetSQLiteDBHelper();
            if (tmpSqLiteDBHelper == null) {
                return false;
            }
            SQLiteReader tmpSQLiteReader = tmpSqLiteDBHelper.Query("Select COUNT(*) From T_CeShuYinZiTable_ShuZhong Where YangDiName='" + YangDiName + "'");
            if (!tmpSQLiteReader.Read() || tmpSQLiteReader.GetInt32(0) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void calulateTable(double area) {
        SaveCeShuYinZiTable_ShuZhong(CalCeShuYinZiTable_ShuZhong(this.m_YangdiName, area), this.m_YangdiName);
        SaveCeShuYinZiTable_ShuZhongZu(CalCeShuYinZiTable_ShuZhongZu(this.m_YangdiName, area), this.m_YangdiName);
        refreshTable();
        Common.ShowToast("测树因子计算完成!");
    }

    public static boolean SaveCeShuYinZiTable_ShuZhong(List<HashMap<String, String[]>> list, String yangdiName) {
        try {
            SQLiteDBHelper tmpSqLiteDBHelper = CommonSetting.GetSQLiteDBHelper();
            if (!(tmpSqLiteDBHelper == null || list == null || list.size() <= 0)) {
                tmpSqLiteDBHelper.ExecuteSQL("Delete from T_CeShuYinZiTable_ShuZhong Where YangDiName='" + yangdiName + "'");
                String tmpSql = "Insert Into T_CeShuYinZiTable_ShuZhong (YangDiName,ShuZhong,ShuZhongZu,PJXJ,PJSG,YDXJ,GQXJ,YDZS,GQZS) Values ('" + yangdiName + "',";
                for (HashMap<String, String[]> tmphashMap : list) {
                    String[] tmpStrs02 = tmphashMap.get("Data");
                    String tmpShuZhong = tmphashMap.get("ShuZhong")[0];
                    tmpSqLiteDBHelper.ExecuteSQL(String.valueOf(tmpSql) + ("'" + tmpShuZhong + "','" + CommonSetting.ConvertShuZhongToZu(tmpShuZhong) + "','" + tmpStrs02[0] + "','" + tmpStrs02[1] + "','" + tmpStrs02[2] + "','" + tmpStrs02[3] + "','" + tmpStrs02[4] + "','" + tmpStrs02[5] + "'") + ")");
                }
                return true;
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
        return false;
    }

    public static boolean SaveCeShuYinZiTable_ShuZhongZu(HashMap<String, String[]> hashMap, String yangdiName) {
        try {
            SQLiteDBHelper tmpSqLiteDBHelper = CommonSetting.GetSQLiteDBHelper();
            if (!(tmpSqLiteDBHelper == null || hashMap == null || hashMap.size() <= 0)) {
                tmpSqLiteDBHelper.ExecuteSQL("Delete from T_CeShuYinZiData_ShuZhongZu Where YangDiName='" + yangdiName + "'");
                String tmpSql = "Insert Into T_CeShuYinZiData_ShuZhongZu (YangDiName,XiangMu,HeJi,SM,MWS,BM,YNS,HSS,RK,YK,MZ) Values ('" + yangdiName + "',";
                String[] tmpStrs00 = hashMap.get("合计");
                String[] tmpStrs01 = hashMap.get("杉木");
                String[] tmpStrs02 = hashMap.get("马尾松");
                String[] tmpStrs03 = hashMap.get("柏木");
                String[] tmpStrs04 = hashMap.get("云南松");
                String[] tmpStrs05 = hashMap.get("华山松");
                String[] tmpStrs06 = hashMap.get("软阔");
                String[] tmpStrs07 = hashMap.get("硬阔");
                String[] tmpStrs08 = hashMap.get("毛竹");
                String[] tmpPrjNameStrings = {"平均胸径", "平均树高", "样地蓄积", "公顷蓄积", "样地株树", "公顷株树"};
                for (int i = 0; i < 6; i++) {
                    tmpSqLiteDBHelper.ExecuteSQL(String.valueOf(tmpSql) + ("'" + tmpPrjNameStrings[i] + "','" + tmpStrs00[i] + "','" + tmpStrs01[i] + "','" + tmpStrs02[i] + "','" + tmpStrs03[i] + "','" + tmpStrs04[i] + "','" + tmpStrs05[i] + "','" + tmpStrs06[i] + "','" + tmpStrs07[i] + "','" + tmpStrs08[i] + "'") + ")");
                }
                return true;
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
        return false;
    }

    public static double CalAvgXiongJing(List<Double> xiongJingArray) {
        double result = 0.0d;
        if (xiongJingArray == null || xiongJingArray.size() <= 0) {
            return 0.0d;
        }
        for (Double tmpDouble : xiongJingArray) {
            result += tmpDouble.doubleValue() * tmpDouble.doubleValue();
        }
        return Math.sqrt(result / ((double) xiongJingArray.size()));
    }

    public static double CalAvgShuGao(List<Double> shuGaoArray) {
        double result = 0.0d;
        if (shuGaoArray == null || shuGaoArray.size() <= 0) {
            return 0.0d;
        }
        for (Double tmpDouble : shuGaoArray) {
            result += tmpDouble.doubleValue();
        }
        return result / ((double) shuGaoArray.size());
    }

    public static double CalXiaobanXuJi(List<Double> xiongJingArray, List<Double> shuGaoArray, int shuZhongIndex) {
        double result = 0.0d;
        if (shuGaoArray != null && shuGaoArray.size() > 0) {
            int tmpCount = shuGaoArray.size();
            for (int i = 0; i < tmpCount; i++) {
                double tmpXuJi = 0.0d;
                double tmpXiongJing = xiongJingArray.get(i).doubleValue();
                double tmpShuGao = shuGaoArray.get(i).doubleValue();
                if (shuZhongIndex == 0) {
                    tmpXuJi = CommonSetting.CalCaiJi_ShanMu(tmpXiongJing, tmpShuGao);
                } else if (shuZhongIndex == 1) {
                    tmpXuJi = CommonSetting.CalCaiJi_MaWeiSong(tmpXiongJing, tmpShuGao);
                } else if (shuZhongIndex == 2) {
                    tmpXuJi = CommonSetting.CalCaiJi_BoMu(tmpXiongJing, tmpShuGao);
                } else if (shuZhongIndex == 3) {
                    tmpXuJi = CommonSetting.CalCaiJi_YunNanSong(tmpXiongJing, tmpShuGao);
                } else if (shuZhongIndex == 4) {
                    tmpXuJi = CommonSetting.CalCaiJi_HuaShanSong(tmpXiongJing, tmpShuGao);
                } else if (shuZhongIndex == 5) {
                    tmpXuJi = CommonSetting.CalCaiJi_RuanKuo(tmpXiongJing, tmpShuGao);
                } else if (shuZhongIndex == 6) {
                    tmpXuJi = CommonSetting.CalCaiJi_YingKuo(tmpXiongJing, tmpShuGao);
                }
                result += tmpXuJi;
            }
        }
        return result;
    }

    public static double CalXiaobanXuJi(List<Double> xiongJingArray, List<Double> shuGaoArray, String shuZhongName) {
        int tmpIndex = -1;
        if (shuZhongName.equals("杉木")) {
            tmpIndex = 0;
        } else if (shuZhongName.equals("马尾松")) {
            tmpIndex = 1;
        } else if (shuZhongName.equals("柏木")) {
            tmpIndex = 2;
        } else if (shuZhongName.equals("云南松")) {
            tmpIndex = 3;
        } else if (shuZhongName.equals("华山松")) {
            tmpIndex = 4;
        } else if (shuZhongName.equals("软阔")) {
            tmpIndex = 5;
        } else if (shuZhongName.equals("硬阔")) {
            tmpIndex = 6;
        }
        return CalXiaobanXuJi(xiongJingArray, shuGaoArray, tmpIndex);
    }

    public static List<HashMap<String, String[]>> CalCeShuYinZiTable_ShuZhong(String yangdiName, double xiaobanArea) {
        HashMap<String, List<Double>> tmpHashMap2;
        List<Double> tmpList01;
        List<Double> tmpList02;
        if (xiaobanArea <= 0.0d) {
            xiaobanArea = 1.0d;
        }
        HashMap<String, HashMap<String, List<Double>>> tmpYDHashMap = new HashMap<>();
        SQLiteReader tmpLiteReader = CommonSetting.GetSQLiteDBHelper().Query("Select ShuZhong,XiongJing,PJMShuGao,ShuZhongZu From T_YangDiData Where YangDiName='" + yangdiName + "'");
        while (tmpLiteReader.Read()) {
            new HashMap();
            String tmpShuZhong = tmpLiteReader.GetString(0);
            double tmpXiongJ = tmpLiteReader.GetDouble(1);
            double tmpShuGao = tmpLiteReader.GetDouble(2);
            tmpLiteReader.GetString(3);
            new HashMap();
            if (tmpYDHashMap.containsKey(tmpShuZhong)) {
                tmpHashMap2 = tmpYDHashMap.get(tmpShuZhong);
            } else {
                tmpHashMap2 = new HashMap<>();
                tmpYDHashMap.put(tmpShuZhong, tmpHashMap2);
            }
            if (tmpHashMap2.containsKey("XiongJing")) {
                tmpList01 = tmpHashMap2.get("XiongJing");
            } else {
                tmpList01 = new ArrayList<>();
                tmpHashMap2.put("XiongJing", tmpList01);
            }
            if (tmpHashMap2.containsKey("ShuGao")) {
                tmpList02 = tmpHashMap2.get("ShuGao");
            } else {
                tmpList02 = new ArrayList<>();
                tmpHashMap2.put("ShuGao", tmpList02);
            }
            if (!(tmpList01 == null || tmpList02 == null)) {
                tmpList01.add(Double.valueOf(tmpXiongJ));
                tmpList02.add(Double.valueOf(tmpShuGao));
            }
        }
        List<HashMap<String, String[]>> result = new ArrayList<>();
        double[] tmpTotalDs = new double[6];
        for (Map.Entry<String, HashMap<String, List<Double>>> tmpEntry : tmpYDHashMap.entrySet()) {
            String tmpShuZhong2 = tmpEntry.getKey();
            String tmpShuZhongZu = CommonSetting.ConvertShuZhongToZu(tmpShuZhong2);
            String[] tmpDoubles = new String[6];
            for (int i = 0; i < 6; i++) {
                tmpDoubles[i] = "";
            }
            HashMap<String, List<Double>> tmpHashMap22 = tmpEntry.getValue();
            if (tmpHashMap22.containsKey("XiongJing") && tmpHashMap22.containsKey("ShuGao")) {
                List<Double> tmpList012 = tmpHashMap22.get("XiongJing");
                List<Double> tmpList022 = tmpHashMap22.get("ShuGao");
                if (tmpList012.size() > 0 && tmpList022.size() > 0) {
                    double tmpAvgXJ = CalAvgXiongJing(tmpList012);
                    double tmpAvgShuGao = CalAvgShuGao(tmpList022);
                    double tmpXuJi = Double.parseDouble(CommonSetting.XUJIFormat.format(CalXiaobanXuJi(tmpList012, tmpList022, tmpShuZhongZu)));
                    double tmpXuJi2 = tmpXuJi / xiaobanArea;
                    double tmpZhuShu = (double) tmpList012.size();
                    double tmpZhuShu2 = tmpZhuShu / xiaobanArea;
                    tmpDoubles[0] = CommonSetting.XIONGJINGFormat.format(tmpAvgXJ);
                    tmpDoubles[1] = CommonSetting.XIONGJINGFormat.format(tmpAvgShuGao);
                    tmpDoubles[2] = CommonSetting.XUJIFormat.format(tmpXuJi);
                    tmpDoubles[3] = CommonSetting.XUJIFormat.format(tmpXuJi2);
                    tmpDoubles[4] = CommonSetting.ZHUSHUFormat.format(tmpZhuShu);
                    tmpDoubles[5] = CommonSetting.ZHUSHUFormat.format(tmpZhuShu2);
                    tmpTotalDs[0] = tmpTotalDs[0] + tmpAvgXJ;
                    tmpTotalDs[1] = tmpTotalDs[1] + tmpAvgShuGao;
                    tmpTotalDs[2] = tmpTotalDs[2] + tmpXuJi;
                    tmpTotalDs[3] = tmpTotalDs[3] + tmpXuJi2;
                    tmpTotalDs[4] = tmpTotalDs[4] + tmpZhuShu;
                    tmpTotalDs[5] = tmpTotalDs[5] + tmpZhuShu2;
                }
            }
            HashMap<String, String[]> tmpHashMap = new HashMap<>();
            tmpHashMap.put("ShuZhong", new String[]{tmpShuZhong2});
            tmpHashMap.put("Data", tmpDoubles);
            result.add(tmpHashMap);
        }
        String[] tmpDoubles2 = {"", "", CommonSetting.XUJIFormat.format(tmpTotalDs[2]), CommonSetting.XUJIFormat.format(tmpTotalDs[3]), CommonSetting.ZHUSHUFormat.format(tmpTotalDs[4]), CommonSetting.ZHUSHUFormat.format(tmpTotalDs[5])};
        HashMap<String, String[]> tmpHashMap3 = new HashMap<>();
        tmpHashMap3.put("ShuZhong", new String[]{"合计"});
        tmpHashMap3.put("Data", tmpDoubles2);
        result.add(tmpHashMap3);
        return result;
    }

    public static HashMap<String, String[]> CalCeShuYinZiTable_ShuZhongZu(String yangdiName, double xiaobanArea) {
        List<Double> tmpList01;
        List<Double> tmpList02;
        if (xiaobanArea <= 0.0d) {
            xiaobanArea = 1.0d;
        }
        HashMap<String, HashMap<String, List<Double>>> tmpYDHashMap = new HashMap<>();
        tmpYDHashMap.put("杉木", new HashMap<>());
        tmpYDHashMap.put("马尾松", new HashMap<>());
        tmpYDHashMap.put("柏木", new HashMap<>());
        tmpYDHashMap.put("云南松", new HashMap<>());
        tmpYDHashMap.put("华山松", new HashMap<>());
        tmpYDHashMap.put("软阔", new HashMap<>());
        tmpYDHashMap.put("硬阔", new HashMap<>());
        tmpYDHashMap.put("毛竹", new HashMap<>());
        SQLiteReader tmpLiteReader = CommonSetting.GetSQLiteDBHelper().Query("Select ShuZhongZu,XiongJing,PJMShuGao From T_YangDiData Where YangDiName='" + yangdiName + "'");
        while (tmpLiteReader.Read()) {
            new HashMap();
            String tmpShuZhongZu = tmpLiteReader.GetString(0);
            if (!(tmpShuZhongZu == null || tmpShuZhongZu.length() == 0 || !tmpYDHashMap.containsKey(tmpShuZhongZu))) {
                double tmpXiongJ = tmpLiteReader.GetDouble(1);
                double tmpShuGao = tmpLiteReader.GetDouble(2);
                HashMap<String, List<Double>> tmpHashMap2 = tmpYDHashMap.get(tmpShuZhongZu);
                if (tmpHashMap2.containsKey("XiongJing")) {
                    tmpList01 = tmpHashMap2.get("XiongJing");
                } else {
                    tmpList01 = new ArrayList<>();
                    tmpHashMap2.put("XiongJing", tmpList01);
                }
                if (tmpHashMap2.containsKey("ShuGao")) {
                    tmpList02 = tmpHashMap2.get("ShuGao");
                } else {
                    tmpList02 = new ArrayList<>();
                    tmpHashMap2.put("ShuGao", tmpList02);
                }
                if (!(tmpList01 == null || tmpList02 == null)) {
                    tmpList01.add(Double.valueOf(tmpXiongJ));
                    tmpList02.add(Double.valueOf(tmpShuGao));
                }
            }
        }
        HashMap<String, String[]> result = new HashMap<>();
        double[] tmpTotalDs = new double[6];
        for (Map.Entry<String, HashMap<String, List<Double>>> tmpEntry : tmpYDHashMap.entrySet()) {
            String tmpShuZhong = tmpEntry.getKey();
            String[] tmpDoubles = new String[6];
            for (int i = 0; i < 6; i++) {
                tmpDoubles[i] = "";
            }
            HashMap<String, List<Double>> tmpHashMap22 = tmpEntry.getValue();
            if (tmpHashMap22.containsKey("XiongJing") && tmpHashMap22.containsKey("ShuGao")) {
                List<Double> tmpList012 = tmpHashMap22.get("XiongJing");
                List<Double> tmpList022 = tmpHashMap22.get("ShuGao");
                if (tmpList012.size() > 0 && tmpList022.size() > 0) {
                    double tmpAvgXJ = CalAvgXiongJing(tmpList012);
                    double tmpAvgShuGao = CalAvgShuGao(tmpList022);
                    double tmpXuJi = Double.parseDouble(CommonSetting.XUJIFormat.format(CalXiaobanXuJi(tmpList012, tmpList022, tmpShuZhong)));
                    double tmpXuJi2 = tmpXuJi / xiaobanArea;
                    double tmpZhuShu = (double) tmpList012.size();
                    double tmpZhuShu2 = tmpZhuShu / xiaobanArea;
                    tmpDoubles[0] = CommonSetting.XIONGJINGFormat.format(tmpAvgXJ);
                    tmpDoubles[1] = CommonSetting.XIONGJINGFormat.format(tmpAvgShuGao);
                    tmpDoubles[2] = CommonSetting.XUJIFormat.format(tmpXuJi);
                    tmpDoubles[3] = CommonSetting.XUJIFormat.format(tmpXuJi2);
                    tmpDoubles[4] = CommonSetting.ZHUSHUFormat.format(tmpZhuShu);
                    tmpDoubles[5] = CommonSetting.ZHUSHUFormat.format(tmpZhuShu2);
                    tmpTotalDs[0] = tmpTotalDs[0] + tmpAvgXJ;
                    tmpTotalDs[1] = tmpTotalDs[1] + tmpAvgShuGao;
                    tmpTotalDs[2] = tmpTotalDs[2] + tmpXuJi;
                    tmpTotalDs[3] = tmpTotalDs[3] + tmpXuJi2;
                    tmpTotalDs[4] = tmpTotalDs[4] + tmpZhuShu;
                    tmpTotalDs[5] = tmpTotalDs[5] + tmpZhuShu2;
                }
            }
            result.put(tmpShuZhong, tmpDoubles);
        }
        result.put("合计", new String[]{"", "", CommonSetting.XUJIFormat.format(tmpTotalDs[2]), CommonSetting.XUJIFormat.format(tmpTotalDs[3]), CommonSetting.ZHUSHUFormat.format(tmpTotalDs[4]), CommonSetting.ZHUSHUFormat.format(tmpTotalDs[5])});
        return result;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.CeShuYinZiTable_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
            }
        });
        this._Dialog.show();
    }
}
