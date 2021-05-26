package com.xzy.forestSystem.GuiZhou.DiaoCha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.core.internal.view.SupportMenu;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.GraphicSymbolGeometry;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Display.PointSymbol;
import  com.xzy.forestSystem.gogisapi.Display.SymbolManage;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Point;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.MyControls.Input_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Others.MediaManag_Dialog;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class XiaoBanYangDiTable_Dialog {
    private XDialogTemplate _Dialog = null;
    private ICallback m_Callback = null;
    private Button m_MediaButton = null;
    private String m_MediaInfoString = "";
    private List<HashMap<String, Object>> m_MyTableDataList = new ArrayList();
    private MyTableFactory m_MyTableFactory = new MyTableFactory();
    private SQLiteDBHelper m_SQLiteDBHelper = null;
    private String m_YangdiName = "";
    private int m_refreshType = 0;
    private ICallback pCallback = new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiTable_Dialog.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String command, Object object) {
            if (command.equals("列表选项")) {
                final HashMap<String, Object> tmphasMap = (HashMap) object;
                if (tmphasMap != null) {
                    new AlertDialog.Builder(XiaoBanYangDiTable_Dialog.this._Dialog.getContext(), 3).setTitle("选择功能:").setSingleChoiceItems(new String[]{"修改样木信息", "设置平均木树高", "地图显示"}, -1, new DialogInterface.OnClickListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiTable_Dialog.1.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface arg0, int arg1) {
                            if (arg1 == 0) {
                                YangMu2_Dialog tmpDialog = new YangMu2_Dialog();
                                tmpDialog.SetTitle("修改样木");
                                tmpDialog.setReturnTag(String.valueOf(tmphasMap.get("ID")));
                                tmpDialog.SetDefaultData(String.valueOf(tmphasMap.get("D3")), String.valueOf(tmphasMap.get("D4")), String.valueOf(tmphasMap.get("D5")), String.valueOf(tmphasMap.get("D6")), String.valueOf(tmphasMap.get("D7")), String.valueOf(tmphasMap.get("D8")));
                                tmpDialog.SetCallback(new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiTable_Dialog.1.1.1
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String command2, Object pObject) {
                                        Object[] tmpObjs01;
                                        if (command2.equals("样木信息返回") && (tmpObjs01 = (Object[]) pObject) != null && tmpObjs01.length > 1) {
                                            String tmpID = String.valueOf(tmpObjs01[0]);
                                            Object[] tmpObjs02 = (Object[]) tmpObjs01[1];
                                            if (tmpObjs02 != null && tmpObjs02.length > 5 && CommonSetting.UpdateYangMuData(tmpID, XiaoBanYangDiTable_Dialog.this.m_YangdiName, String.valueOf(tmpObjs02[0]), String.valueOf(tmpObjs02[1]), String.valueOf(tmpObjs02[2]), String.valueOf(tmpObjs02[3]), String.valueOf(tmpObjs02[4]), String.valueOf(tmpObjs02[5]))) {
                                                XiaoBanYangDiTable_Dialog.this.refreshTable();
                                            }
                                        }
                                    }
                                });
                                tmpDialog.ShowDialog();
                            } else if (arg1 == 1) {
                                String tmpShuGaoString = String.valueOf(tmphasMap.get("D5"));
                                if (tmpShuGaoString.trim().length() > 0) {
                                    double tmpD = 0.0d;
                                    try {
                                        tmpD = Double.parseDouble(tmpShuGaoString);
                                    } catch (Exception e) {
                                    }
                                    if (tmpD == 0.0d) {
                                        tmpShuGaoString = "";
                                    }
                                }
                                Input_Dialog tempDialog = new Input_Dialog();
                                tempDialog.setValues("平均木树高", "树高(m):", tmpShuGaoString, "提示:请输入该树种该径阶组的平均树高.");
                                tempDialog.SetCallback(XiaoBanYangDiTable_Dialog.this.pCallback);
                                tempDialog.SetTagValue(tmphasMap);
                                tempDialog.setInputType(8192);
                                tempDialog.ShowDialog();
                            } else if (arg1 == 2) {
                                String tmpXString = String.valueOf(tmphasMap.get("D7"));
                                String tmpYString = String.valueOf(tmphasMap.get("D8"));
                                double tmpX = 0.0d;
                                double tmpY = 0.0d;
                                try {
                                    tmpX = Double.parseDouble(tmpXString);
                                    tmpY = Double.parseDouble(tmpYString);
                                } catch (Exception e2) {
                                }
                                if (tmpX == 0.0d || tmpY == 0.0d) {
                                    Common.ShowDialog("样木所在位置不能为(0,0).");
                                    return;
                                }
                                PointSymbol tmpPointSymbol = SymbolManage.GetSystemPointSymbol("tree", PubVar._PubCommand.m_ConfigDB);
                                GraphicSymbolGeometry tmpGraphicSymbolGeo = new GraphicSymbolGeometry();
                                Point tmpGeo = new Point(tmpX, tmpY);
                                tmpGeo.setLabelContent(String.valueOf(tmphasMap.get("D3")));
                                tmpGraphicSymbolGeo._Geoemtry = tmpGeo;
                                tmpGraphicSymbolGeo._Symbol = tmpPointSymbol;
                                tmpGraphicSymbolGeo._GeometryType = "样木对象";
                                HashMap<String, Object> tmpHashMap = new HashMap<>();
                                tmpHashMap.put("YangDiName", XiaoBanYangDiTable_Dialog.this.m_YangdiName);
                                tmpHashMap.put("ID", String.valueOf(tmphasMap.get("ID")));
                                tmpGraphicSymbolGeo._AttributeHashMap = tmpHashMap;
                                PubVar._MapView._GraphicLayer.AddGeometry(tmpGraphicSymbolGeo);
                                PubVar._Map.ZoomToCenter(tmpX, tmpY);
                                Common.ShowToast("已添加样木到地图中显示.");
                            }
                            arg0.dismiss();
                        }
                    }).show();
                }
            } else if (command.equals("多媒体返回")) {
                if (object != null) {
                    String tmpResult = "";
                    String[] tmpStrs = String.valueOf(object).split(",");
                    int tmpI = 0;
                    for (String tmpStr01 : tmpStrs) {
                        if (tmpStr01.trim().length() > 0) {
                            if (tmpResult.length() > 0) {
                                tmpResult = String.valueOf(tmpResult) + ",";
                            }
                            tmpResult = String.valueOf(tmpResult) + tmpStr01.trim();
                            tmpI++;
                        }
                    }
                    XiaoBanYangDiTable_Dialog.this.m_MediaInfoString = tmpResult;
                    XiaoBanYangDiTable_Dialog.this.m_MediaButton.setText("[" + String.valueOf(tmpI) + "]照片");
                    XiaoBanYangDiTable_Dialog.this.saveMediaInfo();
                }
            } else if (command.equals("功能")) {
                new AlertDialog.Builder(XiaoBanYangDiTable_Dialog.this._Dialog.getContext(), 3).setTitle("选择功能:").setSingleChoiceItems(new String[]{"径阶分组", "计算树高", "测树因子", "计算所在小班因子", "散生木调查"}, -1, new DialogInterface.OnClickListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiTable_Dialog.1.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (arg1 == 0) {
                            XiaoBanYangDiTable_Dialog.this.DoCommand("径阶分组");
                        } else if (arg1 == 1) {
                            XiaoBanYangDiTable_Dialog.this.DoCommand("计算树高");
                        } else if (arg1 == 2) {
                            XiaoBanYangDiTable_Dialog.this.DoCommand("计算测树因子");
                        } else if (arg1 == 3) {
                            XiaoBanYangDiTable_Dialog.this.DoCommand("计算所在小班因子");
                        } else if (arg1 == 4) {
                            XiaoBanYangDiTable_Dialog.this.DoCommand("散生木调查");
                        }
                        arg0.dismiss();
                    }
                }).show();
            } else if (command.contains("输入参数")) {
                Object[] tmpObjs = (Object[]) object;
                if (tmpObjs != null && tmpObjs.length >= 1) {
                    final String tempValue = tmpObjs[1].toString();
                    if (tmpObjs.length > 2 && tmpObjs[2] != null && (tmpObjs[2] instanceof HashMap)) {
                        final HashMap<String, Object> tmpHash = (HashMap) tmpObjs[2];
                        CommonSetting.UpdateYangMuData_ShuGao(String.valueOf(tmpHash.get("ID")), tempValue);
                        Common.ShowYesNoDialog(XiaoBanYangDiTable_Dialog.this._Dialog.getContext(), "是否为该树种同径阶组的其他样木设置树高?", new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiTable_Dialog.1.3
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String command2, Object pObject) {
                                if (command2.equals("YES")) {
                                    if (XiaoBanYangDiTable_Dialog.this.SetAvgHeightByShuZhongAndXiong(String.valueOf(tmpHash.get("D3")), Double.parseDouble(String.valueOf(tmpHash.get("D4"))), tempValue)) {
                                        XiaoBanYangDiTable_Dialog.this.refreshTable();
                                    }
                                }
                            }
                        });
                        XiaoBanYangDiTable_Dialog.this.refreshTable();
                    }
                }
            } else if (command.equals("返回")) {
                if (!CeShuYinZiTable_Dialog.IsCalculated(XiaoBanYangDiTable_Dialog.this.m_YangdiName)) {
                    Common.ShowYesNoDialogWithAlert(XiaoBanYangDiTable_Dialog.this._Dialog.getContext(), "还未计算该样地的测树因子.\r\n是否计算测树因子?", true, new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiTable_Dialog.1.4
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String command2, Object pObject) {
                            if (command2.equals("YES")) {
                                XiaoBanYangDiTable_Dialog.this.DoCommand("计算测树因子");
                            } else {
                                XiaoBanYangDiTable_Dialog.this._Dialog.dismiss();
                            }
                        }
                    });
                } else {
                    XiaoBanYangDiTable_Dialog.this._Dialog.dismiss();
                }
            }
        }
    };

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public XiaoBanYangDiTable_Dialog(String yangdiName) {
        this.m_YangdiName = yangdiName;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.xiaobanyangditable_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("每木检尺及树高测量记录表");
        this._Dialog.SetHeadButtons("1,2130837858,功能,功能", this.pCallback);
        this._Dialog.setCanceledOnTouchOutside(false);
        this._Dialog.setCancelable(false);
        this._Dialog.SetAllowedCloseDialog(false);
        this._Dialog.SetCallback(this.pCallback);
        this._Dialog.findViewById(R.id.btn_AddYangMu).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_DeleteYangDi).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectAll).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectDe).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_ShowInMap).setOnClickListener(new ViewClick());
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_yangdi_layerlist), "自定义", "选择,样木编号,树种,胸径,平均木树高,备注", "checkbox,text,text,text,text,text", new int[]{50, 80, 100, 100, 150, 100}, this.pCallback);
        this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
        this.m_SQLiteDBHelper = CommonSetting.GetSQLiteDBHelper();
        this.m_MediaButton = (Button) this._Dialog.findViewById(R.id.btn_mediaMang);
        this.m_MediaButton.setOnClickListener(new View.OnClickListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiTable_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                MediaManag_Dialog tempDialog = new MediaManag_Dialog(XiaoBanYangDiTable_Dialog.this._Dialog.getContext());
                tempDialog.ReSetSize(1.0f, 0.96f);
                tempDialog.setFileNamePreString(XiaoBanYangDiTable_Dialog.this.m_YangdiName);
                tempDialog.SetCaption(XiaoBanYangDiTable_Dialog.this.m_YangdiName);
                tempDialog.SetPhotoInfo(XiaoBanYangDiTable_Dialog.this.m_MediaInfoString);
                tempDialog.SetCallback(XiaoBanYangDiTable_Dialog.this.pCallback);
                tempDialog.show();
            }
        });
        refreshTable();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshTable() {
        try {
            if (this.m_refreshType == 0) {
                this.m_MyTableDataList.clear();
                int tmpTid = 0;
                SQLiteReader tmpLiteReader = this.m_SQLiteDBHelper.Query("Select ShuZhong,XiongJing,PJMShuGao,Remark,ID,X,Y From T_YangDiData Where YangDiName='" + this.m_YangdiName + "'");
                while (tmpLiteReader.Read()) {
                    HashMap<String, Object> tmpHash = new HashMap<>();
                    tmpHash.put("D1", false);
                    tmpTid++;
                    tmpHash.put("D2", String.valueOf(tmpTid));
                    tmpHash.put("D3", tmpLiteReader.GetString(0));
                    tmpHash.put("D4", Double.valueOf(tmpLiteReader.GetDouble(1)));
                    tmpHash.put("D5", Double.valueOf(tmpLiteReader.GetDouble(2)));
                    tmpHash.put("D6", tmpLiteReader.GetString(3));
                    tmpHash.put("ID", Integer.valueOf(tmpLiteReader.GetInt32(4)));
                    tmpHash.put("D7", Double.valueOf(tmpLiteReader.GetDouble(5)));
                    tmpHash.put("D8", Double.valueOf(tmpLiteReader.GetDouble(6)));
                    this.m_MyTableDataList.add(tmpHash);
                }
                this.m_MyTableFactory.notifyDataSetChanged();
            } else if (this.m_refreshType == 1) {
                this.m_MyTableDataList.clear();
                int tmpTid2 = 0;
                SQLiteReader tmpLiteReader2 = this.m_SQLiteDBHelper.Query("Select ShuZhong,XiongJing,PJMShuGao,Remark,ID,X,Y From T_YangDiData Where YangDiName='" + this.m_YangdiName + "' order By ShuZhong,XiongJing");
                String tmplastShuZ = "";
                int tmpLastColor = -1;
                int tmpLastJJZ = -1;
                while (tmpLiteReader2.Read()) {
                    HashMap<String, Object> tmpHash2 = new HashMap<>();
                    tmpHash2.put("D1", false);
                    tmpTid2++;
                    String tmpShuZ = tmpLiteReader2.GetString(0);
                    double tmpXiongjing = tmpLiteReader2.GetDouble(1);
                    tmpHash2.put("D2", String.valueOf(tmpTid2));
                    tmpHash2.put("D3", tmpShuZ);
                    tmpHash2.put("D4", Double.valueOf(tmpXiongjing));
                    tmpHash2.put("D5", Double.valueOf(tmpLiteReader2.GetDouble(2)));
                    tmpHash2.put("D6", tmpLiteReader2.GetString(3));
                    tmpHash2.put("ID", Integer.valueOf(tmpLiteReader2.GetInt32(4)));
                    tmpHash2.put("D7", Double.valueOf(tmpLiteReader2.GetDouble(5)));
                    tmpHash2.put("D8", Double.valueOf(tmpLiteReader2.GetDouble(6)));
                    int tmpJJZ = GetJingJieZu(tmpXiongjing);
                    if (!tmplastShuZ.equals(tmpShuZ)) {
                        if (tmpLastColor == -1) {
                            tmpLastColor = -3355444;
                        } else {
                            tmpLastColor = -1;
                        }
                        tmpLastJJZ = tmpJJZ;
                        tmplastShuZ = tmpShuZ;
                    } else {
                        if (tmpLastJJZ != tmpJJZ) {
                            if (tmpLastColor == -1) {
                                tmpLastColor = -3355444;
                            } else {
                                tmpLastColor = -1;
                            }
                        }
                        tmpLastJJZ = tmpJJZ;
                    }
                    tmpHash2.put("Bg_Color", Integer.valueOf(tmpLastColor));
                    this.m_MyTableDataList.add(tmpHash2);
                }
                if (this.m_MyTableDataList.size() > 1) {
                    int tmpI01 = -1;
                    int tmpLastColor2 = -1000000;
                    List<Double> tmpListD01 = new ArrayList<>();
                    List<Integer> tmpListI01 = new ArrayList<>();
                    double tmpAvgXJ = 0.0d;
                    for (HashMap<String, Object> tmphash : this.m_MyTableDataList) {
                        tmpI01++;
                        int tmpColor = Integer.parseInt(String.valueOf(tmphash.get("Bg_Color")));
                        double tmpXJ = Double.parseDouble(String.valueOf(tmphash.get("D4")));
                        if (tmpColor != tmpLastColor2) {
                            if (tmpListD01.size() > 0 && tmpAvgXJ > 0.0d) {
                                double tmpAvgXJ2 = Math.sqrt(tmpAvgXJ / ((double) tmpListD01.size()));
                                int tmpTid02 = -1;
                                int tmpTid03 = 0;
                                double tmpMinValue = Double.MAX_VALUE;
                                for (Double tmpDouble02 : tmpListD01) {
                                    tmpTid02++;
                                    double tmpD02 = Math.abs(tmpDouble02.doubleValue() - tmpAvgXJ2);
                                    if (tmpMinValue > tmpD02) {
                                        tmpMinValue = tmpD02;
                                        tmpTid03 = tmpTid02;
                                    }
                                }
                                this.m_MyTableDataList.get(tmpListI01.get(tmpTid03).intValue()).put("Text_Color", Integer.valueOf((int) SupportMenu.CATEGORY_MASK));
                            }
                            tmpListD01.clear();
                            tmpListI01.clear();
                            tmpAvgXJ = 0.0d;
                        }
                        tmpLastColor2 = tmpColor;
                        tmpListD01.add(Double.valueOf(tmpXJ));
                        tmpListI01.add(Integer.valueOf(tmpI01));
                        tmpAvgXJ += tmpXJ * tmpXJ;
                    }
                    if (tmpListD01.size() > 0 && tmpAvgXJ > 0.0d) {
                        double tmpAvgXJ3 = Math.sqrt(tmpAvgXJ / ((double) tmpListD01.size()));
                        int tmpTid022 = -1;
                        int tmpTid032 = 0;
                        double tmpMinValue2 = Double.MAX_VALUE;
                        for (Double tmpDouble022 : tmpListD01) {
                            tmpTid022++;
                            double tmpD022 = Math.abs(tmpDouble022.doubleValue() - tmpAvgXJ3);
                            if (tmpMinValue2 > tmpD022) {
                                tmpMinValue2 = tmpD022;
                                tmpTid032 = tmpTid022;
                            }
                        }
                        this.m_MyTableDataList.get(tmpListI01.get(tmpTid032).intValue()).put("Text_Color", Integer.valueOf((int) SupportMenu.CATEGORY_MASK));
                    }
                }
                this.m_MyTableFactory.notifyDataSetChanged();
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("添加样木")) {
                YangMu2_Dialog tmpDialog = new YangMu2_Dialog();
                tmpDialog.SetTitle("添加样木");
                tmpDialog.SetCallback(new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiTable_Dialog.3
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command2, Object pObject) {
                        Object[] tmpObjs02;
                        if (command2.equals("样木信息返回")) {
                            Object[] tmpObjs01 = (Object[]) pObject;
                            if (tmpObjs01 != null && tmpObjs01.length > 1 && (tmpObjs02 = (Object[]) tmpObjs01[1]) != null && tmpObjs02.length > 3 && CommonSetting.SaveYangMuData(XiaoBanYangDiTable_Dialog.this.m_YangdiName, String.valueOf(tmpObjs02[0]), String.valueOf(tmpObjs02[1]), String.valueOf(tmpObjs02[2]), String.valueOf(tmpObjs02[3]), String.valueOf(tmpObjs02[4]), String.valueOf(tmpObjs02[5]))) {
                                XiaoBanYangDiTable_Dialog.this.refreshTable();
                            }
                            XiaoBanYangDiTable_Dialog.this.DoCommand("添加样木");
                        }
                    }
                });
                tmpDialog.ShowDialog();
            } else if (command.equals("全选")) {
                for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
                    tmpHash.put("D1", true);
                }
                this.m_MyTableFactory.notifyDataSetChanged();
            } else if (command.equals("反选")) {
                for (HashMap<String, Object> tmpHash2 : this.m_MyTableDataList) {
                    tmpHash2.put("D1", Boolean.valueOf(!Boolean.parseBoolean(String.valueOf(tmpHash2.get("D1")))));
                }
                this.m_MyTableFactory.notifyDataSetChanged();
            } else if (command.equals("删除样地")) {
                final List<String> tmpList = new ArrayList<>();
                new ArrayList();
                int tmpTid = -1;
                for (HashMap<String, Object> tmpHash3 : this.m_MyTableDataList) {
                    tmpTid++;
                    if (Boolean.parseBoolean(String.valueOf(tmpHash3.get("D1")))) {
                        tmpList.add(String.valueOf(tmpHash3.get("ID")));
                    }
                }
                if (tmpList.size() > 0) {
                    Common.ShowYesNoDialogWithAlert(this._Dialog.getContext(), "是否删除选择的" + String.valueOf(tmpList.size()) + "个样木数据?\r\n提示:删除后将无法恢复,请谨慎操作.", true, new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiTable_Dialog.4
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (paramString.equals("YES")) {
                                CommonSetting.DeleteYangMusData(tmpList);
                                XiaoBanYangDiTable_Dialog.this.refreshTable();
                            }
                        }
                    });
                } else {
                    Common.ShowDialog("没有选择需要删除的样木数据.");
                }
            } else if (command.equals("计算树高")) {
                Common.ShowYesNoDialogWithAlert(this._Dialog.getContext(), "是否根据径阶组计算未输入的树高?\r\n\r\n提示:计算完成后将保存树高值.", true, new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiTable_Dialog.5
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        int tmpI;
                        int tmpI2;
                        if (paramString.equals("YES")) {
                            List<String> tmpShuZhongArray = new ArrayList<>();
                            for (HashMap<String, Object> tmpHash4 : XiaoBanYangDiTable_Dialog.this.m_MyTableDataList) {
                                String tmpShuZ = String.valueOf(tmpHash4.get("D3"));
                                if (!tmpShuZhongArray.contains(tmpShuZ)) {
                                    tmpShuZhongArray.add(tmpShuZ);
                                }
                            }
                            for (String tmpShuZhong : tmpShuZhongArray) {
                                double[] tmpValues = {0.0d, 0.0d, 0.0d, 0.0d};
                                int[] tmpValues2 = new int[4];
                                for (HashMap<String, Object> tmpHash5 : XiaoBanYangDiTable_Dialog.this.m_MyTableDataList) {
                                    if (String.valueOf(tmpHash5.get("D3")).equals(tmpShuZhong)) {
                                        double tmpD = Double.parseDouble(String.valueOf(tmpHash5.get("D4")));
                                        double tmpH = Double.parseDouble(String.valueOf(tmpHash5.get("D5")));
                                        if (tmpH != 0.0d) {
                                            if (tmpD < 5.0d) {
                                                tmpI2 = -1;
                                            } else if (tmpD <= 13.0d) {
                                                tmpI2 = 0;
                                            } else if (tmpD <= 25.0d) {
                                                tmpI2 = 1;
                                            } else if (tmpD <= 37.0d) {
                                                tmpI2 = 2;
                                            } else {
                                                tmpI2 = 3;
                                            }
                                            if (tmpI2 > -1) {
                                                tmpValues[tmpI2] = tmpValues[tmpI2] + tmpH;
                                                tmpValues2[tmpI2] = tmpValues2[tmpI2] + 1;
                                            }
                                        }
                                    }
                                }
                                for (int i = 0; i < 4; i++) {
                                    if (tmpValues2[i] != 0) {
                                        tmpValues[i] = tmpValues[i] / ((double) tmpValues2[i]);
                                    }
                                }
                                for (HashMap<String, Object> tmpHash6 : XiaoBanYangDiTable_Dialog.this.m_MyTableDataList) {
                                    if (String.valueOf(tmpHash6.get("D3")).equals(tmpShuZhong)) {
                                        double tmpD2 = Double.parseDouble(String.valueOf(tmpHash6.get("D4")));
                                        if (Double.parseDouble(String.valueOf(tmpHash6.get("D5"))) == 0.0d) {
                                            if (tmpD2 < 5.0d) {
                                                tmpI = -1;
                                            } else if (tmpD2 <= 13.0d) {
                                                tmpI = 0;
                                            } else if (tmpD2 <= 25.0d) {
                                                tmpI = 1;
                                            } else if (tmpD2 <= 37.0d) {
                                                tmpI = 2;
                                            } else {
                                                tmpI = 3;
                                            }
                                            if (tmpI > -1) {
                                                double tmpH2 = tmpValues[tmpI];
                                                tmpHash6.put("D5", Double.valueOf(tmpH2));
                                                CommonSetting.UpdateYangMuData_ShuGao(String.valueOf(tmpHash6.get("ID")), String.valueOf(tmpH2));
                                            }
                                        }
                                    }
                                }
                            }
                            XiaoBanYangDiTable_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                            Common.ShowToast("计算树高完成!");
                        }
                    }
                });
            } else if (command.equals("计算测树因子")) {
                int tmpCount = 0;
                SQLiteReader tmpLiteReader = this.m_SQLiteDBHelper.Query("Select COUNT(ID) From T_YangDiData Where YangDiName='" + this.m_YangdiName + "' AND PJMShuGao = 0");
                if (tmpLiteReader.Read()) {
                    tmpCount = tmpLiteReader.GetInt32(0);
                }
                if (tmpCount > 0) {
                    Common.ShowDialog("记录表中有" + String.valueOf(tmpCount) + "个样木的树高值为0,请先计算树高或手动输入树高值后再进行本操作!");
                } else {
                    new CeShuYinZiTable_Dialog(this.m_YangdiName).ShowDialog();
                }
            } else if (command.equals("径阶分组")) {
                this.m_refreshType = 1;
                refreshTable();
            } else if (command.equals("计算所在小班因子")) {
                CommonSetting.CalXiaoBanYinZiAndUpdateData(this._Dialog.getContext(), this.m_YangdiName, true);
            } else if (command.equals("散生木调查")) {
                String tmpXiaoBanFullNameString = this.m_YangdiName;
                int tmpI001 = tmpXiaoBanFullNameString.lastIndexOf("_");
                if (tmpI001 > 0) {
                    tmpXiaoBanFullNameString = tmpXiaoBanFullNameString.substring(0, tmpI001);
                }
                if (tmpXiaoBanFullNameString.trim().length() > 0) {
                    SanShengMu_Dialog tmpDialog2 = new SanShengMu_Dialog(tmpXiaoBanFullNameString);
                    tmpDialog2.SetCallback(this.m_Callback);
                    tmpDialog2.ShowDialog();
                }
            } else if (command.equals("地图显示")) {
                List<Coordinate> tmpCoordinates = new ArrayList<>();
                for (HashMap<String, Object> tmpHash4 : this.m_MyTableDataList) {
                    if (Boolean.parseBoolean(String.valueOf(tmpHash4.get("D1")))) {
                        String tmpXString = String.valueOf(tmpHash4.get("D7"));
                        String tmpYString = String.valueOf(tmpHash4.get("D8"));
                        double tmpX = 0.0d;
                        double tmpY = 0.0d;
                        try {
                            tmpX = Double.parseDouble(tmpXString);
                            tmpY = Double.parseDouble(tmpYString);
                        } catch (Exception e) {
                        }
                        if (!(tmpX == 0.0d || tmpY == 0.0d)) {
                            PointSymbol tmpPointSymbol = SymbolManage.GetSystemPointSymbol("tree", PubVar._PubCommand.m_ConfigDB);
                            GraphicSymbolGeometry tmpGraphicSymbolGeo = new GraphicSymbolGeometry();
                            Point tmpGeo = new Point(tmpX, tmpY);
                            tmpGeo.setLabelContent(String.valueOf(tmpHash4.get("D3")));
                            tmpGraphicSymbolGeo._Geoemtry = tmpGeo;
                            tmpGraphicSymbolGeo._Symbol = tmpPointSymbol;
                            tmpGraphicSymbolGeo._GeometryType = "样木对象";
                            HashMap<String, Object> tmpHashMap = new HashMap<>();
                            tmpHashMap.put("YangDiName", this.m_YangdiName);
                            tmpHashMap.put("ID", String.valueOf(tmpHash4.get("ID")));
                            tmpGraphicSymbolGeo._AttributeHashMap = tmpHashMap;
                            PubVar._MapView._GraphicLayer.AddGeometry(tmpGraphicSymbolGeo);
                            tmpCoordinates.add(new Coordinate(tmpX, tmpY));
                        }
                    }
                }
                if (tmpCoordinates.size() > 0) {
                    if (tmpCoordinates.size() == 1) {
                        PubVar._Map.ZoomToCenter(tmpCoordinates.get(0));
                    } else {
                        Polyline tmpPolyline = new Polyline();
                        tmpPolyline.SetAllCoordinateList(tmpCoordinates);
                        PubVar._Map.ZoomToExtend(tmpPolyline.CalEnvelope());
                    }
                    Common.ShowToast("已添加" + String.valueOf(tmpCoordinates.size()) + "个样木到地图中显示.");
                    return;
                }
                Common.ShowDialog("请勾选有效的样木数据然后再进行此操作.\r\n\r\n提示:样木坐标值位于(0,0)处将不显示在地图中.");
            }
        } catch (Exception e2) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void saveMediaInfo() {
        if (this.m_SQLiteDBHelper != null) {
            this.m_SQLiteDBHelper.ExecuteSQL("Update T_YangDiInfo Set PhotoInfo='" + this.m_MediaInfoString + "' Where YangDiName='" + this.m_YangdiName + "'");
        }
    }

    public static int GetJingJieZu(double xiongJing) {
        if (xiongJing < 5.0d) {
            return -1;
        }
        if (xiongJing <= 13.0d) {
            return 0;
        }
        if (xiongJing <= 25.0d) {
            return 1;
        }
        if (xiongJing <= 37.0d) {
            return 2;
        }
        return 3;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean SetAvgHeightByShuZhongAndXiong(String shuZhong, double xiongjing, String height) {
        int tmpClass02;
        boolean result = false;
        int tmpClass01 = GetJingJieZu(xiongjing);
        if (tmpClass01 > -1) {
            for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
                if (shuZhong.equals(String.valueOf(tmpHash.get("D3"))) && (tmpClass02 = GetJingJieZu(Double.parseDouble(String.valueOf(tmpHash.get("D4"))))) > -1 && tmpClass01 == tmpClass02) {
                    result |= CommonSetting.UpdateYangMuData_ShuGao(String.valueOf(tmpHash.get("ID")), height);
                }
            }
        }
        return result;
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiTable_Dialog.6
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                SQLiteReader tmpSQLiteReader = XiaoBanYangDiTable_Dialog.this.m_SQLiteDBHelper.Query("Select PhotoInfo From T_YangDiInfo Where YangDiName='" + XiaoBanYangDiTable_Dialog.this.m_YangdiName + "'");
                if (tmpSQLiteReader.Read()) {
                    XiaoBanYangDiTable_Dialog.this.m_MediaInfoString = tmpSQLiteReader.GetString(0);
                    String[] tmpStrs02 = XiaoBanYangDiTable_Dialog.this.m_MediaInfoString.split(",");
                    if (tmpStrs02 != null && tmpStrs02.length > 0 && tmpStrs02[0].trim().length() > 0) {
                        XiaoBanYangDiTable_Dialog.this.m_MediaButton.setText("[" + String.valueOf(tmpStrs02.length) + "]照片");
                    }
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
                XiaoBanYangDiTable_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
