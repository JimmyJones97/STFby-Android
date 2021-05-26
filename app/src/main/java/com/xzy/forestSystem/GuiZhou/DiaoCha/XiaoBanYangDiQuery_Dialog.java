package com.xzy.forestSystem.GuiZhou.DiaoCha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.GraphicSymbolGeometry;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Display.IRender;
import  com.xzy.forestSystem.gogisapi.Display.ISymbol;
import  com.xzy.forestSystem.gogisapi.Display.SimpleDisplay;
import  com.xzy.forestSystem.gogisapi.Display.SymbolManage;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geodatabase.ExportToShp;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.Geometry.Point;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.CellFormat;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class XiaoBanYangDiQuery_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private SQLiteDBHelper m_SQLiteDBHelper;
    private SpinnerList m_SpinnerList02;
    private SpinnerList m_SpinnerList03;
    private SpinnerList m_SpinnerList04;
    private SpinnerList m_SpinnerList05;
    private SpinnerList m_SpinnerList06;
    Button m_expandButton;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean checkNeedTip() {
        List<String> tmpList = new ArrayList<>();
        for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
            if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                tmpList.add(String.valueOf(tmpHash.get("yangdiname")));
            }
        }
        if (tmpList.size() != 0) {
            return true;
        }
        Common.ShowDialog("请选择需要导出的样地数据.");
        return false;
    }

    public XiaoBanYangDiQuery_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_SQLiteDBHelper = null;
        this.m_SpinnerList02 = null;
        this.m_SpinnerList03 = null;
        this.m_SpinnerList04 = null;
        this.m_SpinnerList05 = null;
        this.m_SpinnerList06 = null;
        this.m_expandButton = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this.pCallback = new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiQuery_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("选择县返回")) {
                        String tmpString = String.valueOf(object);
                        if (tmpString.length() > 0) {
                            List<String> tmpList01 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader = XiaoBanYangDiQuery_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct Xiang From T_YangDiInfo Where Xian='" + tmpString + "'");
                            while (tmpSQLiteReader.Read()) {
                                tmpList01.add(tmpSQLiteReader.GetString(0));
                            }
                            if (tmpList01.size() > 0) {
                                XiaoBanYangDiQuery_Dialog.this.m_SpinnerList03.setEnabled(true);
                                Common.SetSpinnerListData(XiaoBanYangDiQuery_Dialog.this._Dialog, (int) R.id.sp_Yangdi03, "", "请选择乡镇", tmpList01, "选择乡镇返回", XiaoBanYangDiQuery_Dialog.this.pCallback);
                            } else {
                                XiaoBanYangDiQuery_Dialog.this.m_SpinnerList03.setEnabled(false);
                            }
                        }
                    } else if (command.equals("选择乡镇返回")) {
                        String tmpString2 = String.valueOf(object);
                        if (tmpString2.length() > 0) {
                            String tmpString3 = Common.GetViewValue(XiaoBanYangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi02);
                            List<String> tmpList012 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader2 = XiaoBanYangDiQuery_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct Cun From T_YangDiInfo Where Xian='" + tmpString3 + "' And Xiang='" + tmpString2 + "'");
                            while (tmpSQLiteReader2.Read()) {
                                tmpList012.add(tmpSQLiteReader2.GetString(0));
                            }
                            if (tmpList012.size() > 0) {
                                XiaoBanYangDiQuery_Dialog.this.m_SpinnerList04.setEnabled(true);
                                Common.SetSpinnerListData(XiaoBanYangDiQuery_Dialog.this._Dialog, (int) R.id.sp_Yangdi04, "", "请选择村", tmpList012, "选择村返回", XiaoBanYangDiQuery_Dialog.this.pCallback);
                            } else {
                                XiaoBanYangDiQuery_Dialog.this.m_SpinnerList04.setEnabled(true);
                            }
                        }
                    } else if (command.equals("选择村返回")) {
                        String tmpString4 = String.valueOf(object);
                        if (tmpString4.length() > 0) {
                            String tmpString32 = Common.GetViewValue(XiaoBanYangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi02);
                            String tmpString42 = Common.GetViewValue(XiaoBanYangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi03);
                            List<String> tmpList013 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader3 = XiaoBanYangDiQuery_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct XiaoBan From T_YangDiInfo Where Xian='" + tmpString32 + "' And Xiang='" + tmpString42 + "' And Cun='" + tmpString4 + "'");
                            while (tmpSQLiteReader3.Read()) {
                                tmpList013.add(tmpSQLiteReader3.GetString(0));
                            }
                            if (tmpList013.size() > 0) {
                                XiaoBanYangDiQuery_Dialog.this.m_SpinnerList05.setEnabled(true);
                                Common.SetSpinnerListData(XiaoBanYangDiQuery_Dialog.this._Dialog, (int) R.id.sp_Yangdi05, "", "请选择小班", tmpList013, "选择小班返回", XiaoBanYangDiQuery_Dialog.this.pCallback);
                            } else {
                                XiaoBanYangDiQuery_Dialog.this.m_SpinnerList05.setEnabled(true);
                            }
                        }
                    } else if (command.equals("选择小班返回")) {
                        String tmpString5 = String.valueOf(object);
                        if (tmpString5.length() > 0) {
                            String tmpString33 = Common.GetViewValue(XiaoBanYangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi02);
                            String tmpString43 = Common.GetViewValue(XiaoBanYangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi03);
                            String tmpString52 = Common.GetViewValue(XiaoBanYangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi04);
                            List<String> tmpList014 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader4 = XiaoBanYangDiQuery_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct YangDiIndex From T_YangDiInfo Where Xian='" + tmpString33 + "' And Xiang='" + tmpString43 + "' And Cun='" + tmpString52 + "' And XiaoBan='" + tmpString5 + "'");
                            while (tmpSQLiteReader4.Read()) {
                                tmpList014.add(tmpSQLiteReader4.GetString(0));
                            }
                            if (tmpList014.size() > 0) {
                                XiaoBanYangDiQuery_Dialog.this.m_SpinnerList06.setEnabled(true);
                                Common.SetSpinnerListData(XiaoBanYangDiQuery_Dialog.this._Dialog, (int) R.id.sp_Yangdi06, "", "请选择样地", tmpList014, (String) null, (ICallback) null);
                            } else {
                                XiaoBanYangDiQuery_Dialog.this.m_SpinnerList06.setEnabled(true);
                            }
                        }
                    } else if (command.equals("列表选项") && object != null && (object instanceof HashMap)) {
                        HashMap<String, Object> tmpHashMap = (HashMap) object;
                        if (tmpHashMap.containsKey("yangdiname")) {
                            final String tmpYangdiNameString = String.valueOf(tmpHashMap.get("yangdiname"));
                            Common.ShowYesNoDialog(XiaoBanYangDiQuery_Dialog.this._Dialog.getContext(), "是否编辑该样地数据?", new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiQuery_Dialog.1.1
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String command2, Object pObject) {
                                    if (command2.equals("YES")) {
                                        XiaoBanYangDiQuery_Dialog.this.openXiaoBanYangDiInfoDialog(tmpYangdiNameString);
                                    }
                                }
                            });
                        }
                    }
                    if (!command.equals("导出")) {
                        return;
                    }
                    if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                        Common.ShowDialog("尊敬的用户：\r\n        【公共版】不能导出数据.为保证您能使用本软件的全部功能，请获取正式授权码！\r\n详见【关于系统】！");
                    } else {
                        new AlertDialog.Builder(PubVar.MainContext, 3).setTitle("选中导出格式").setItems(new String[]{"导出Excel文件", "导出Excel文件(带图片文件)", "Shapefile格式(Shp)", "导出Excel文件(所有小班)"}, new DialogInterface.OnClickListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiQuery_Dialog.1.2
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (arg1 < 0) {
                                    arg0.dismiss();
                                } else if ((arg1 == 0 || arg1 == 1 || arg1 == 2) && !XiaoBanYangDiQuery_Dialog.this.checkNeedTip()) {
                                    arg0.dismiss();
                                } else {
                                    String tempTotalFolder = String.valueOf(Common.GetAPPPath()) + "/小班调查";
                                    if (!Common.ExistFolder(tempTotalFolder)) {
                                        new File(tempTotalFolder).mkdir();
                                    }
                                    String m_ExportFolder = String.valueOf(tempTotalFolder) + FileSelector_Dialog.sRoot + PubVar._PubCommand.m_ProjectDB.GetProjectManage().getProjectName();
                                    if (!Common.ExistFolder(m_ExportFolder)) {
                                        new File(m_ExportFolder).mkdir();
                                    }
                                    String m_ExportFolder2 = String.valueOf(m_ExportFolder) + FileSelector_Dialog.sRoot + Common.fileDateFormat.format(new Date());
                                    if (!Common.ExistFolder(m_ExportFolder2)) {
                                        new File(m_ExportFolder2).mkdir();
                                    }
                                    if (arg1 == 0) {
                                        XiaoBanYangDiQuery_Dialog.this.exportSelectYangdi(m_ExportFolder2, false);
                                    } else if (arg1 == 1) {
                                        XiaoBanYangDiQuery_Dialog.this.exportSelectYangdi(m_ExportFolder2, true);
                                    } else if (arg1 == 2) {
                                        XiaoBanYangDiQuery_Dialog.this.exportSelectYangdiToShp(String.valueOf(m_ExportFolder2) + "/样地数据");
                                    } else if (arg1 == 3) {
                                        XiaoBanYangDiQuery_Dialog.this.ExportAllXiaoBanTable(m_ExportFolder2, false);
                                    }
                                    arg0.dismiss();
                                }
                            }
                        }).show();
                    }
                } catch (Exception e) {
                    Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.xiaobanyangdiquery_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("样地数据查询");
        this._Dialog.SetHeadButtons("1,2130837858,导出,导出", this.pCallback);
        this.m_SQLiteDBHelper = CommonSetting.GetSQLiteDBHelper();
        this.m_SpinnerList02 = (SpinnerList) this._Dialog.findViewById(R.id.sp_Yangdi02);
        this.m_SpinnerList02.setEnabled(true);
        this.m_SpinnerList03 = (SpinnerList) this._Dialog.findViewById(R.id.sp_Yangdi03);
        this.m_SpinnerList03.setEnabled(false);
        this.m_SpinnerList04 = (SpinnerList) this._Dialog.findViewById(R.id.sp_Yangdi04);
        this.m_SpinnerList04.setEnabled(false);
        this.m_SpinnerList05 = (SpinnerList) this._Dialog.findViewById(R.id.sp_Yangdi05);
        this.m_SpinnerList05.setEnabled(false);
        this.m_SpinnerList06 = (SpinnerList) this._Dialog.findViewById(R.id.sp_Yangdi06);
        this.m_SpinnerList06.setEnabled(false);
        this.m_expandButton = (Button) this._Dialog.findViewById(R.id.btn_YangDiExpand);
        this.m_expandButton.setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_YangDiQuery).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_DeleteYangDi).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectAll).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectDe).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_YangDiInMap).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_SettingFieldRel).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_Tools).setOnClickListener(new ViewClick());
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_yangdi_layerlist), "自定义", "选择,样地名称,样地序号", "checkbox,text,text", new int[]{-15, -60, -25}, this.pCallback);
        this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshList() {
        boolean tmpBool01 = false;
        try {
            List<String> tmpList01 = new ArrayList<>();
            SQLiteReader tmpSQLiteReader = this.m_SQLiteDBHelper.Query("Select Distinct Xian From T_YangDiInfo");
            while (tmpSQLiteReader.Read()) {
                tmpList01.add(tmpSQLiteReader.GetString(0));
            }
            if (tmpList01.size() > 0) {
                tmpBool01 = true;
                Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_Yangdi02, "", "请选择县区", tmpList01, "选择县返回", this.pCallback);
            }
            if (!tmpBool01) {
                Common.ShowDialog("当前还没有样地数据.");
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        try {
            if (command.equals("展开查询条件")) {
                this._Dialog.findViewById(R.id.ll_queryCondition).setVisibility(0);
                this.m_expandButton.setTag("隐藏查询条件");
                Drawable tmpDrawable = this._Dialog.getContext().getResources().getDrawable(R.drawable.expand0248);
                tmpDrawable.setBounds(0, 0, tmpDrawable.getMinimumWidth(), tmpDrawable.getMinimumHeight());
                this.m_expandButton.setCompoundDrawables(tmpDrawable, null, null, null);
            } else if (command.equals("隐藏查询条件")) {
                this._Dialog.findViewById(R.id.ll_queryCondition).setVisibility(8);
                this.m_expandButton.setTag("展开查询条件");
                Drawable tmpDrawable2 = this._Dialog.getContext().getResources().getDrawable(R.drawable.expand0148);
                tmpDrawable2.setBounds(0, 0, tmpDrawable2.getMinimumWidth(), tmpDrawable2.getMinimumHeight());
                this.m_expandButton.setCompoundDrawables(tmpDrawable2, null, null, null);
            } else if (command.equals("查询")) {
                this.m_MyTableDataList.clear();
                String tmpTotalNameString = "";
                String tmpString = Common.GetViewValue(this._Dialog, R.id.sp_Yangdi02).trim();
                if (tmpString.length() > 0) {
                    tmpTotalNameString = String.valueOf(tmpTotalNameString) + "_" + tmpString;
                    String tmpString2 = Common.GetViewValue(this._Dialog, R.id.sp_Yangdi03).trim();
                    if (tmpString2.length() > 0) {
                        tmpTotalNameString = String.valueOf(tmpTotalNameString) + "_" + tmpString2;
                        String tmpString3 = Common.GetViewValue(this._Dialog, R.id.sp_Yangdi04).trim();
                        if (tmpString3.length() > 0) {
                            tmpTotalNameString = String.valueOf(tmpTotalNameString) + "_" + tmpString3;
                            String tmpString4 = Common.GetViewValue(this._Dialog, R.id.sp_Yangdi05).trim();
                            if (tmpString4.length() > 0) {
                                tmpTotalNameString = String.valueOf(tmpTotalNameString) + "_" + tmpString4;
                                String tmpString5 = Common.GetViewValue(this._Dialog, R.id.sp_Yangdi06).trim();
                                if (tmpString5.length() > 0) {
                                    tmpTotalNameString = String.valueOf(tmpTotalNameString) + "_" + tmpString5;
                                }
                            }
                        }
                    }
                }
                SQLiteReader tmpLiteReader = this.m_SQLiteDBHelper.Query("Select YangDiName,YangDiIndex,X,Y From T_YangDiInfo Where YangDiName like '" + tmpTotalNameString + "%'");
                while (tmpLiteReader.Read()) {
                    String tmpYDName = tmpLiteReader.GetString(0);
                    String tmpYDIndex = tmpLiteReader.GetString(1);
                    HashMap<String, Object> tmpHash = new HashMap<>();
                    tmpHash.put("D1", false);
                    String tmpYDName2 = tmpYDName.replace("_", " ");
                    if (tmpYDName2.endsWith(tmpYDIndex)) {
                        tmpYDName2 = tmpYDName2.substring(0, tmpYDName2.length() - tmpYDIndex.length());
                    }
                    tmpHash.put("D2", tmpYDName2);
                    tmpHash.put("D3", tmpYDIndex);
                    tmpHash.put("yangdiname", tmpYDName);
                    tmpHash.put("X", Double.valueOf(tmpLiteReader.GetDouble(2)));
                    tmpHash.put("Y", Double.valueOf(tmpLiteReader.GetDouble(3)));
                    tmpHash.put("msg", "样地位置:" + tmpYDName2 + "\r\n样地编号:" + tmpYDIndex);
                    this.m_MyTableDataList.add(tmpHash);
                }
                this.m_MyTableFactory.notifyDataSetChanged();
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
            } else if (command.equals("删除样地")) {
                final List<String> tmpList = new ArrayList<>();
                final List<Integer> tmpList2 = new ArrayList<>();
                int tmpTid = -1;
                for (HashMap<String, Object> tmpHash4 : this.m_MyTableDataList) {
                    tmpTid++;
                    if (Boolean.parseBoolean(String.valueOf(tmpHash4.get("D1")))) {
                        tmpList.add(String.valueOf(tmpHash4.get("yangdiname")));
                        tmpList2.add(Integer.valueOf(tmpTid));
                    }
                }
                if (tmpList.size() > 0) {
                    Common.ShowYesNoDialogWithAlert(this._Dialog.getContext(), "是否删除选择的" + String.valueOf(tmpList.size()) + "个样地调查数据?\r\n提示:删除后将无法恢复,请谨慎操作.", true, new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiQuery_Dialog.2
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (paramString.equals("YES")) {
                                for (int i = tmpList.size() - 1; i > -1; i--) {
                                    XiaoBanYangDiQuery_Dialog.this.m_MyTableDataList.remove(((Integer) tmpList2.get(i)).intValue());
                                }
                                CommonSetting.DeleteYangDisData(tmpList);
                                XiaoBanYangDiQuery_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                            }
                        }
                    });
                } else {
                    Common.ShowDialog("没有选择需要删除的样地调查数据.");
                }
            } else if (command.equals("样地地图展示")) {
                boolean tmpBool = true;
                List<Coordinate> tmpList3 = new ArrayList<>();
                ISymbol tmpISymbol = PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemPointSymbol("样式03");
                for (HashMap<String, Object> tmpHash5 : this.m_MyTableDataList) {
                    if (Boolean.parseBoolean(String.valueOf(tmpHash5.get("D1")))) {
                        double tmpD01 = Double.parseDouble(String.valueOf(tmpHash5.get("X")));
                        double tmpD02 = Double.parseDouble(String.valueOf(tmpHash5.get("Y")));
                        if (!(tmpD01 == 0.0d || tmpD02 == 0.0d)) {
                            tmpBool = false;
                            Coordinate tmpCoordinate = new Coordinate(tmpD01, tmpD02);
                            GraphicSymbolGeometry tmpGraphicSymbolGeo = new GraphicSymbolGeometry();
                            Point tmpGeo = new Point(tmpCoordinate.getX(), tmpCoordinate.getY());
                            tmpGeo.setLabelContent(String.valueOf(tmpHash5.get("D3")));
                            tmpGraphicSymbolGeo._Geoemtry = tmpGeo;
                            tmpGraphicSymbolGeo._Symbol = tmpISymbol;
                            tmpGraphicSymbolGeo._GeometryType = "样地对象";
                            HashMap<String, Object> tmpHashMap = new HashMap<>();
                            tmpHashMap.put("yangdiname", String.valueOf(tmpHash5.get("yangdiname")));
                            tmpGraphicSymbolGeo._AttributeHashMap = tmpHashMap;
                            PubVar._MapView._GraphicLayer.AddGeometry(tmpGraphicSymbolGeo);
                            tmpList3.add(tmpCoordinate);
                        }
                    }
                }
                if (tmpList3.size() > 0) {
                    if (tmpList3.size() == 1) {
                        PubVar._Map.ZoomToCenter(tmpList3.get(0));
                    } else {
                        Polyline tmpPolyline = new Polyline();
                        tmpPolyline.SetAllCoordinateList(tmpList3);
                        PubVar._Map.ZoomToExtend(tmpPolyline.CalEnvelope().Scale(1.2d));
                    }
                    Common.ShowToast("已加载样地数据至地图中.");
                }
                if (tmpBool) {
                    Common.ShowDialog("没有选择需要展示在地图中的样地.");
                }
            } else if (command.equals("设置字段关联")) {
                new XiaoBanFieldsSetting_Dialog().ShowDialog();
            } else if (command.equals("功能")) {
                new AlertDialog.Builder(this._Dialog.getContext(), 3).setTitle("选择功能:").setSingleChoiceItems(new String[]{"生成样地图层", "生成样木图层"}, -1, new DialogInterface.OnClickListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiQuery_Dialog.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (arg1 == 0) {
                            XiaoBanYangDiQuery_Dialog.this.DoCommand("生成样地图层");
                        } else if (arg1 == 1) {
                            XiaoBanYangDiQuery_Dialog.this.DoCommand("生成样木图层");
                        }
                        arg0.dismiss();
                    }
                }).show();
            } else if (command.equals("生成样地图层")) {
                boolean tmpBool01 = false;
                SQLiteReader tmpSqLiteReader = this.m_SQLiteDBHelper.Query("Select COUNT(*) FROM  T_YangDiInfo Where X<>0 AND Y<>0");
                if (tmpSqLiteReader != null && tmpSqLiteReader.Read() && tmpSqLiteReader.GetInt32(0) > 0) {
                    tmpBool01 = true;
                    String[] tmpFNamesStrings = "样地名称,样地编号,县,乡,村,小班,X,Y,面积,优势树种,树种组成,起源,乔木郁闭度,灌木覆盖度,平均年龄,龄组,调查人,调查时间,备注".split(",");
                    int tmpLyrIndex = 1;
                    List<FeatureLayer> tmpFeatureLayers = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerList();
                    if (tmpFeatureLayers != null && tmpFeatureLayers.size() > 0) {
                        List<String> tmpLyrNamesList = new ArrayList<>();
                        for (FeatureLayer tmpFeatureLayer2 : tmpFeatureLayers) {
                            tmpLyrNamesList.add(tmpFeatureLayer2.GetLayerName());
                        }
                        while (tmpLyrNamesList.contains(String.valueOf("样地图层_") + String.valueOf(tmpLyrIndex))) {
                            tmpLyrIndex++;
                        }
                    }
                    String tmpLyrName = String.valueOf("样地图层_") + String.valueOf(tmpLyrIndex);
                    FeatureLayer tmpFeatureLayer = new FeatureLayer();
                    tmpFeatureLayer.SetLayerName(tmpLyrName);
                    tmpFeatureLayer.SetLayerTypeName("点");
                    tmpFeatureLayer.SetLayerIndex(tmpFeatureLayers.size());
                    int tmpTid2 = 0;
                    int length = tmpFNamesStrings.length;
                    for (int i = 0; i < length; i++) {
                        String tmpFName = tmpFNamesStrings[i];
                        if (tmpFName.trim().length() > 0) {
                            tmpTid2++;
                            LayerField tmpField = new LayerField();
                            tmpField.SetFieldName(tmpFName);
                            tmpField.SetDataFieldName("F" + String.valueOf(tmpTid2));
                            tmpField.setFieldIndex(tmpTid2);
                            tmpField.SetFieldTypeName("字符串");
                            tmpFeatureLayer.AddField(tmpField);
                        }
                    }
                    tmpFeatureLayer.SetDataSourceName(PubVar.m_Workspace.GetDataSourceByEditing().getName());
                    PubVar._PubCommand.m_ProjectDB.GetLayerManage().AddNewLayer(tmpFeatureLayer);
                    PubVar.m_Workspace.GetDataSourceByEditing().CreateDataset(tmpFeatureLayer.GetLayerID());
                    PubVar._Map.getGeoLayers().MoveTo(tmpFeatureLayer.GetLayerID(), tmpFeatureLayer.GetLayerIndex());
                    PubVar._PubCommand.m_ProjectDB.GetLayerRenderManage().RenderLayerForNew(tmpFeatureLayer);
                    tmpFeatureLayer.SaveLayerInfo();
                    GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByID(tmpFeatureLayer.GetLayerID());
                    if (tmpGeoLayer != null) {
                        IRender tempRender = IRender.CreateRender(tmpGeoLayer, 1);
                        ((SimpleDisplay) tempRender).SetSymbol(SymbolManage.GetSystemPointSymbol("tree17", PubVar._PubCommand.m_ConfigDB));
                        tempRender.UpdateToLayer(tmpFeatureLayer);
                        tempRender.SetSymbolTransparent(tempRender.getTransparent());
                        tmpGeoLayer.setRender(tempRender);
                        tmpGeoLayer.getRender().SaveRender();
                        DataSet tmpDataset = tmpGeoLayer.getDataset();
                        SQLiteReader tmpSqLiteReader2 = this.m_SQLiteDBHelper.Query("Select YangDiName,YangDiIndex,Xian,Xiang,Cun,XiaoBan,X,Y,YDArea,YouShiShuZhong,ZhuZhongZuCheng,QiYuan,QiaoMuYBD,GuanMuFGD,PJNL,LingZu,DiaoChaRen,DiaoChaTime,Remark,PhotoInfo FROM  T_YangDiInfo Where X<>0 AND Y<>0");
                        if (tmpSqLiteReader2 != null) {
                            while (tmpSqLiteReader2.Read()) {
                                double tmpX = tmpSqLiteReader2.GetDouble(6);
                                double tmpY = tmpSqLiteReader2.GetDouble(7);
                                double tmpArea = tmpSqLiteReader2.GetDouble(8);
                                String tmpphotoInfo = tmpSqLiteReader2.GetString(19);
                                int tmpSYSID = tmpDataset.AddNewGeometry(new Point(tmpX, tmpY), "自动生成", false);
                                if (tmpSYSID > -1) {
                                    HashMap<String, Object> tmpFVHashMap = new HashMap<>();
                                    for (int i2 = 0; i2 < 18; i2++) {
                                        if (!(i2 == 6 || i2 == 7 || i2 == 8)) {
                                            tmpFVHashMap.put("F" + String.valueOf(i2 + 1), tmpSqLiteReader2.GetString(i2));
                                        }
                                    }
                                    tmpFVHashMap.put("F7", Double.valueOf(tmpX));
                                    tmpFVHashMap.put("F8", Double.valueOf(tmpY));
                                    tmpFVHashMap.put("F9", Double.valueOf(tmpArea));
                                    tmpDataset.UpdateFieldsValue(String.valueOf(tmpSYSID), tmpFVHashMap, tmpphotoInfo);
                                }
                            }
                            tmpDataset.BuildAllGeosMapIndex();
                            PubVar._Map.InitialSelections();
                            final Envelope tmpEnvelope = tmpDataset.GetExtendFromDB();
                            Common.ShowYesNoDialog(this._Dialog.getContext(), "生成样地图层成功,图层名称为【" + tmpLyrName + "】.\r\n\r\n是否缩放至该图层范围内?", new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiQuery_Dialog.4
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String command2, Object pObject) {
                                    if (command2.equals("YES") && tmpEnvelope != null) {
                                        if (tmpEnvelope.getWidth() > 0.0d) {
                                            PubVar._Map.ZoomToExtend(tmpEnvelope);
                                        } else {
                                            PubVar._Map.ZoomToCenter(tmpEnvelope.getLeftTop());
                                        }
                                        XiaoBanYangDiQuery_Dialog.this._Dialog.dismiss();
                                    }
                                }
                            });
                        }
                    } else {
                        Common.ShowDialog("生成样地图层失败.");
                    }
                }
                if (!tmpBool01) {
                    Common.ShowDialog("调查数据中没有有效的样地数据.\r\n\r\n提示:样地坐标值位于(0,0)处将不处理.");
                }
            } else if (command.equals("生成样木图层")) {
                boolean tmpBool012 = false;
                SQLiteReader tmpSqLiteReader3 = this.m_SQLiteDBHelper.Query("Select COUNT(*) FROM T_YangDiData Where X<>0 AND Y<>0");
                if (tmpSqLiteReader3 != null && tmpSqLiteReader3.Read() && tmpSqLiteReader3.GetInt32(0) > 0) {
                    tmpBool012 = true;
                    String[] tmpFNamesStrings2 = "样地名称,树种,树种组,胸径,树高,X,Y,备注".split(",");
                    int tmpLyrIndex2 = 1;
                    List<FeatureLayer> tmpFeatureLayers2 = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayerList();
                    if (tmpFeatureLayers2 != null && tmpFeatureLayers2.size() > 0) {
                        List<String> tmpLyrNamesList2 = new ArrayList<>();
                        for (FeatureLayer tmpFeatureLayer22 : tmpFeatureLayers2) {
                            tmpLyrNamesList2.add(tmpFeatureLayer22.GetLayerName());
                        }
                        while (tmpLyrNamesList2.contains(String.valueOf("样木图层_") + String.valueOf(tmpLyrIndex2))) {
                            tmpLyrIndex2++;
                        }
                    }
                    String tmpLyrName2 = String.valueOf("样木图层_") + String.valueOf(tmpLyrIndex2);
                    FeatureLayer tmpFeatureLayer3 = new FeatureLayer();
                    tmpFeatureLayer3.SetLayerName(tmpLyrName2);
                    tmpFeatureLayer3.SetLayerTypeName("点");
                    tmpFeatureLayer3.SetLayerIndex(tmpFeatureLayers2.size());
                    int tmpTid3 = 0;
                    int length2 = tmpFNamesStrings2.length;
                    for (int i3 = 0; i3 < length2; i3++) {
                        String tmpFName2 = tmpFNamesStrings2[i3];
                        if (tmpFName2.trim().length() > 0) {
                            tmpTid3++;
                            LayerField tmpField2 = new LayerField();
                            tmpField2.SetFieldName(tmpFName2);
                            tmpField2.SetDataFieldName("F" + String.valueOf(tmpTid3));
                            tmpField2.setFieldIndex(tmpTid3);
                            tmpField2.SetFieldTypeName("字符串");
                            tmpFeatureLayer3.AddField(tmpField2);
                        }
                    }
                    tmpFeatureLayer3.SetDataSourceName(PubVar.m_Workspace.GetDataSourceByEditing().getName());
                    PubVar._PubCommand.m_ProjectDB.GetLayerManage().AddNewLayer(tmpFeatureLayer3);
                    PubVar.m_Workspace.GetDataSourceByEditing().CreateDataset(tmpFeatureLayer3.GetLayerID());
                    PubVar._Map.getGeoLayers().MoveTo(tmpFeatureLayer3.GetLayerID(), tmpFeatureLayer3.GetLayerIndex());
                    PubVar._PubCommand.m_ProjectDB.GetLayerRenderManage().RenderLayerForNew(tmpFeatureLayer3);
                    tmpFeatureLayer3.SaveLayerInfo();
                    GeoLayer tmpGeoLayer2 = PubVar._Map.GetGeoLayerByID(tmpFeatureLayer3.GetLayerID());
                    if (tmpGeoLayer2 != null) {
                        IRender tempRender2 = IRender.CreateRender(tmpGeoLayer2, 1);
                        ((SimpleDisplay) tempRender2).SetSymbol(SymbolManage.GetSystemPointSymbol("tree", PubVar._PubCommand.m_ConfigDB));
                        tempRender2.UpdateToLayer(tmpFeatureLayer3);
                        tempRender2.SetSymbolTransparent(tempRender2.getTransparent());
                        tmpGeoLayer2.setRender(tempRender2);
                        tmpGeoLayer2.getRender().SaveRender();
                        DataSet tmpDataset2 = tmpGeoLayer2.getDataset();
                        SQLiteReader tmpSqLiteReader4 = this.m_SQLiteDBHelper.Query("Select YangDiName,ShuZhong,ShuZhongZu,XiongJing,PJMShuGao,X,Y,Remark FROM  T_YangDiData Where X<>0 AND Y<>0");
                        if (tmpSqLiteReader4 != null) {
                            while (tmpSqLiteReader4.Read()) {
                                double tmpX2 = tmpSqLiteReader4.GetDouble(5);
                                double tmpY2 = tmpSqLiteReader4.GetDouble(6);
                                int tmpSYSID2 = tmpDataset2.AddNewGeometry(new Point(tmpX2, tmpY2), "自动生成", false);
                                if (tmpSYSID2 > -1) {
                                    HashMap<String, Object> tmpFVHashMap2 = new HashMap<>();
                                    tmpFVHashMap2.put("F1", tmpSqLiteReader4.GetString(0));
                                    tmpFVHashMap2.put("F2", tmpSqLiteReader4.GetString(1));
                                    tmpFVHashMap2.put("F3", tmpSqLiteReader4.GetString(2));
                                    tmpFVHashMap2.put("F4", Double.valueOf(tmpSqLiteReader4.GetDouble(3)));
                                    tmpFVHashMap2.put("F5", Double.valueOf(tmpSqLiteReader4.GetDouble(4)));
                                    tmpFVHashMap2.put("F6", Double.valueOf(tmpX2));
                                    tmpFVHashMap2.put("F7", Double.valueOf(tmpY2));
                                    tmpFVHashMap2.put("F8", tmpSqLiteReader4.GetString(7));
                                    tmpDataset2.UpdateFieldsValue(String.valueOf(tmpSYSID2), tmpFVHashMap2);
                                }
                            }
                            tmpDataset2.BuildAllGeosMapIndex();
                            PubVar._Map.InitialSelections();
                            final Envelope tmpEnvelope2 = tmpDataset2.GetExtendFromDB();
                            Common.ShowYesNoDialog(this._Dialog.getContext(), "生成样木图层成功,图层名称为【" + tmpLyrName2 + "】.\r\n\r\n是否缩放至该图层范围内?", new ICallback() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiQuery_Dialog.5
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String command2, Object pObject) {
                                    if (command2.equals("YES") && tmpEnvelope2 != null) {
                                        if (tmpEnvelope2.getWidth() > 0.0d) {
                                            PubVar._Map.ZoomToExtend(tmpEnvelope2);
                                        } else {
                                            PubVar._Map.ZoomToCenter(tmpEnvelope2.getLeftTop());
                                        }
                                        XiaoBanYangDiQuery_Dialog.this._Dialog.dismiss();
                                    }
                                }
                            });
                        }
                    } else {
                        Common.ShowDialog("生成样木图层失败.");
                    }
                }
                if (!tmpBool012) {
                    Common.ShowDialog("调查数据中没有有效的样木数据.\r\n\r\n提示:样木坐标值位于(0,0)处将不处理.");
                }
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void openXiaoBanYangDiInfoDialog(String yangdiName) {
        new XiaoBanYangDiInfo_Dialog(yangdiName).ShowDialog();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void exportSelectYangdi(String m_ExportFolder, boolean exportPhoto) {
        List<String> tmpList = new ArrayList<>();
        for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
            if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                tmpList.add(String.valueOf(tmpHash.get("yangdiname")));
            }
        }
        ExportYangDiTable(m_ExportFolder, tmpList, exportPhoto);
        Common.ShowDialog("导出数据完成,文件导出至:\r\n\r\n" + m_ExportFolder);
    }

    private static String ConvertToPhotoNames(String photoInfo, String XiaoBanCode) {
        String[] tmpStrs;
        String resultString = "";
        if (photoInfo != null && photoInfo.length() > 0 && (tmpStrs = photoInfo.trim().split(",")) != null && tmpStrs.length > 0) {
            int tmpTid = 0;
            for (String tmpStr : tmpStrs) {
                if (tmpStr.trim().length() > 0) {
                    tmpTid++;
                    if (resultString.length() > 0) {
                        resultString = String.valueOf(resultString) + "、";
                    }
                    resultString = String.valueOf(resultString) + XiaoBanCode + "-" + String.valueOf(tmpTid);
                }
            }
        }
        return resultString;
    }

    private static HashMap<String, String> ConvertPhotoNamesToHash(String photoInfo, String XiaoBanCode) {
        String[] tmpStrs;
        HashMap<String, String> result = new HashMap<>();
        if (photoInfo != null && photoInfo.length() > 0 && (tmpStrs = photoInfo.trim().split(",")) != null && tmpStrs.length > 0) {
            int tmpTid = 0;
            for (String tmpStr : tmpStrs) {
                if (tmpStr.trim().length() > 0) {
                    tmpTid++;
                    result.put(String.valueOf(XiaoBanCode) + "-" + String.valueOf(tmpTid), tmpStr.trim());
                }
            }
        }
        return result;
    }

    private boolean ExportYangDiTable(String m_ExportFolder, List<String> ydNameList, boolean exportPhoto) {
        String[] tmpStrs01;
        if (ydNameList.size() <= 0) {
            return false;
        }
        String tmpExcelPathString = String.valueOf(m_ExportFolder) + "/小班因子调查表.xls";
        String tmpOrgPhotoFolder = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/Photo/";
        try {
            WritableCellFormat headerFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 8));
            headerFormat.setAlignment(Alignment.CENTRE);
            headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            WritableCellFormat headerFormat6 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 6));
            headerFormat6.setAlignment(Alignment.CENTRE);
            headerFormat6.setVerticalAlignment(VerticalAlignment.CENTRE);
            headerFormat6.setBorder(Border.ALL, BorderLineStyle.THIN);
            WritableWorkbook wb = Workbook.createWorkbook(new File(tmpExcelPathString), Workbook.getWorkbook(new File(String.valueOf(Common.GetAPPPath()) + "/SysFile/xbyzdctable.xls")));
            HashMap<String, HashMap<String, String>> tmpHashMap = new HashMap<>();
            WritableSheet wSheet02 = wb.getSheet("2因子调查表");
            SQLiteReader tmpSQLiteReader = this.m_SQLiteDBHelper.Query("Select YangDiName,YangDiIndex,Xian,Xiang,Cun,XiaoBan,LayerID,SYSID,X,Y,YDArea,YouShiShuZhong,ZhuZhongZuCheng,QiYuan,QiaoMuYBD,GuanMuFGD,PJNL,LingZu,PhotoInfo,DiaoChaRen,DiaoChaTime,Remark From T_YangDiInfo Where YangDiName IN ('" + Common.CombineStrings("','", ydNameList) + "')");
            int tmpRow = -1;
            while (tmpSQLiteReader.Read()) {
                tmpRow++;
                String tmpYDNameString = tmpSQLiteReader.GetString(0);
                HashMap<String, String> tmpHashMap2 = new HashMap<>();
                int i = 0 + 1;
                tmpHashMap2.put("YangDiName", tmpSQLiteReader.GetString(0));
                int i2 = i + 1;
                tmpHashMap2.put("YangDiIndex", tmpSQLiteReader.GetString(i));
                int i3 = i2 + 1;
                tmpHashMap2.put("Xian", tmpSQLiteReader.GetString(i2));
                int i4 = i3 + 1;
                tmpHashMap2.put("Xiang", tmpSQLiteReader.GetString(i3));
                int i5 = i4 + 1;
                tmpHashMap2.put("Cun", tmpSQLiteReader.GetString(i4));
                int i6 = i5 + 1;
                tmpHashMap2.put("XiaoBan", tmpSQLiteReader.GetString(i5));
                int i7 = i6 + 1;
                tmpHashMap2.put("LayerID", tmpSQLiteReader.GetString(i6));
                int i8 = i7 + 1;
                tmpHashMap2.put("SYSID", tmpSQLiteReader.GetString(i7));
                int i9 = i8 + 1;
                tmpHashMap2.put("X", String.format("%.2f", Double.valueOf(tmpSQLiteReader.GetDouble(i8))));
                int i10 = i9 + 1;
                tmpHashMap2.put("Y", String.format("%.2f", Double.valueOf(tmpSQLiteReader.GetDouble(i9))));
                int i11 = i10 + 1;
                tmpHashMap2.put("YDArea", String.format("%.4f", Double.valueOf(tmpSQLiteReader.GetDouble(i10))));
                int i12 = i11 + 1;
                tmpHashMap2.put("YouShiShuZhong", tmpSQLiteReader.GetString(i11));
                int i13 = i12 + 1;
                tmpHashMap2.put("ZhuZhongZuCheng", tmpSQLiteReader.GetString(i12));
                int i14 = i13 + 1;
                tmpHashMap2.put("QiYuan", tmpSQLiteReader.GetString(i13));
                int i15 = i14 + 1;
                tmpHashMap2.put("QiaoMuYBD", tmpSQLiteReader.GetString(i14));
                int i16 = i15 + 1;
                tmpHashMap2.put("GuanMuFGD", tmpSQLiteReader.GetString(i15));
                int i17 = i16 + 1;
                tmpHashMap2.put("PJNL", tmpSQLiteReader.GetString(i16));
                int i18 = i17 + 1;
                tmpHashMap2.put("LingZu", tmpSQLiteReader.GetString(i17));
                int i19 = i18 + 1;
                tmpHashMap2.put("PhotoInfo", tmpSQLiteReader.GetString(i18));
                int i20 = i19 + 1;
                tmpHashMap2.put("DiaoChaRen", tmpSQLiteReader.GetString(i19));
                int i21 = i20 + 1;
                tmpHashMap2.put("DiaoChaTime", tmpSQLiteReader.GetString(i20));
                int i22 = i21 + 1;
                tmpHashMap2.put("Remark", tmpSQLiteReader.GetString(i21));
                tmpHashMap.put(tmpYDNameString, tmpHashMap2);
                wSheet02.addCell(new Label(0, 3 + tmpRow, String.valueOf(tmpRow + 1), headerFormat));
                wSheet02.addCell(new Label(1, 3 + tmpRow, tmpHashMap2.get("Xian"), headerFormat));
                wSheet02.addCell(new Label(2, 3 + tmpRow, tmpHashMap2.get("Xiang"), headerFormat));
                wSheet02.addCell(new Label(3, 3 + tmpRow, tmpHashMap2.get("Cun"), headerFormat));
                wSheet02.addCell(new Label(4, 3 + tmpRow, tmpHashMap2.get("XiaoBan"), headerFormat));
                wSheet02.addCell(new Label(5, 3 + tmpRow, tmpHashMap2.get("X"), headerFormat));
                wSheet02.addCell(new Label(6, 3 + tmpRow, tmpHashMap2.get("Y"), headerFormat));
                wSheet02.addCell(new Label(7, 3 + tmpRow, tmpHashMap2.get("YouShiShuZhong"), headerFormat));
                wSheet02.addCell(new Label(8, 3 + tmpRow, tmpHashMap2.get("ZhuZhongZuCheng"), headerFormat));
                wSheet02.addCell(new Label(9, 3 + tmpRow, tmpHashMap2.get("QiYuan"), headerFormat));
                wSheet02.addCell(new Label(10, 3 + tmpRow, tmpHashMap2.get("QiaoMuYBD"), headerFormat));
                wSheet02.addCell(new Label(11, 3 + tmpRow, tmpHashMap2.get("GuanMuFGD"), headerFormat));
                wSheet02.addCell(new Label(12, 3 + tmpRow, tmpHashMap2.get("PJNL"), headerFormat));
                wSheet02.addCell(new Label(13, 3 + tmpRow, tmpHashMap2.get("LingZu"), headerFormat));
                wSheet02.addCell(new Label(14, 3 + tmpRow, ConvertToPhotoNames(tmpHashMap2.get("PhotoInfo"), tmpHashMap2.get("YangDiIndex")), headerFormat));
                wSheet02.addCell(new Label(15, 3 + tmpRow, tmpHashMap2.get("DiaoChaRen"), headerFormat));
                wSheet02.addCell(new Label(16, 3 + tmpRow, tmpHashMap2.get("Remark"), headerFormat));
            }
            if (tmpHashMap.size() > 0) {
                WritableSheet wSheet01 = wb.getSheet("1小班因子调查表");
                int tmpCurrentRow = 4 - 1;
                double tmpTotalArea = 0.0d;
                double tmpTotalLinMuXuJi = 0.0d;
                double tmpTotalZhuShu = 0.0d;
                double tmpTotalXuJi = 0.0d;
                for (Map.Entry<String, HashMap<String, String>> tmpEntry : tmpHashMap.entrySet()) {
                    tmpEntry.getKey();
                    HashMap<String, String> tmphasMap = tmpEntry.getValue();
                    String tmpSYSID = tmphasMap.get("SYSID");
                    GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByID(tmphasMap.get("LayerID"));
                    if (tmpGeoLayer != null) {
                        tmpCurrentRow++;
                        HashMap<String, Object> tmphashMap2 = tmpGeoLayer.getDataset().getGeometryFieldValuesBySYSID(tmpSYSID, null);
                        int tmpColIndex = -1;
                        for (String tmpFIDName : CommonSetting.m_XiaoBanLayerMustFieldsNameList) {
                            tmpColIndex++;
                            if (tmpColIndex == 50) {
                                tmpColIndex += 3;
                            }
                            String tmpFIDName2 = CommonSetting.m_XiaoBanLayerMustFieldsName.get(tmpFIDName);
                            String tmpFValue = "";
                            if (tmphashMap2.containsKey(tmpFIDName2)) {
                                tmpFValue = String.valueOf(tmphashMap2.get(tmpFIDName2));
                            }
                            wSheet01.addCell(new Label(0 + tmpColIndex, tmpCurrentRow, tmpFValue, headerFormat6));
                            if (tmpColIndex == 9) {
                                double tmpD01 = 0.0d;
                                try {
                                    tmpD01 = Double.parseDouble(tmpFValue);
                                } catch (Exception e) {
                                }
                                tmpTotalArea += tmpD01;
                            } else if (tmpColIndex == 37) {
                                double tmpD012 = 0.0d;
                                try {
                                    tmpD012 = Double.parseDouble(tmpFValue);
                                } catch (Exception e2) {
                                }
                                tmpTotalLinMuXuJi += tmpD012;
                            } else if (tmpColIndex == 38) {
                                double tmpD013 = 0.0d;
                                try {
                                    tmpD013 = Double.parseDouble(tmpFValue);
                                } catch (Exception e3) {
                                }
                                tmpTotalZhuShu += tmpD013;
                            } else if (tmpColIndex == 44) {
                                double tmpD014 = 0.0d;
                                try {
                                    tmpD014 = Double.parseDouble(tmpFValue);
                                } catch (Exception e4) {
                                }
                                tmpTotalXuJi += tmpD014;
                            }
                        }
                        tmphasMap.get("XiaoBan");
                        String tmpMediaInfo = tmpGeoLayer.getDataset().getGeoemtryMediaInfoBySYSID(tmpSYSID);
                        if (tmpMediaInfo == null || tmpMediaInfo.trim().length() == 0) {
                            tmpMediaInfo = tmphasMap.get("PhotoInfo");
                        }
                        if (tmpMediaInfo != null && tmpMediaInfo.length() > 0) {
                            wSheet01.addCell(new Label(50, tmpCurrentRow, ConvertToPhotoNames(tmpMediaInfo, tmphasMap.get("YangDiIndex")), headerFormat6));
                        }
                        String tmpDCR = tmphasMap.get("DiaoChaRen");
                        if (tmpDCR != null && tmpDCR.length() > 0) {
                            wSheet01.addCell(new Label(51, tmpCurrentRow, tmpDCR, headerFormat6));
                        }
                        String tmpDiaoChaTime = tmphasMap.get("DiaoChaTime");
                        if (tmpDiaoChaTime != null && tmpDiaoChaTime.length() > 0) {
                            wSheet01.addCell(new Label(52, tmpCurrentRow, tmpDiaoChaTime, headerFormat6));
                        }
                        if (exportPhoto && tmpMediaInfo != null && tmpMediaInfo.length() > 0) {
                            String tmpMediaFolderString = String.valueOf(m_ExportFolder) + FileSelector_Dialog.sRoot + tmphasMap.get("Xiang") + "_" + tmphasMap.get("Cun") + "_" + tmphasMap.get("XiaoBan");
                            if (!Common.ExistFolder(tmpMediaFolderString)) {
                                new File(tmpMediaFolderString).mkdir();
                            }
                            HashMap<String, String> tmpMediaHashMap = ConvertPhotoNamesToHash(tmpMediaInfo, tmphasMap.get("YangDiIndex"));
                            if (tmpMediaHashMap.size() > 0) {
                                for (Map.Entry<String, String> tmpEntry2 : tmpMediaHashMap.entrySet()) {
                                    String tmpFilePath01 = String.valueOf(tmpOrgPhotoFolder) + tmpEntry2.getValue();
                                    if (new File(tmpFilePath01).exists()) {
                                        Common.CopyFile(tmpFilePath01, String.valueOf(tmpMediaFolderString) + FileSelector_Dialog.sRoot + tmpEntry2.getKey() + ".jpg");
                                    }
                                }
                            }
                        }
                    }
                }
                CellFormat tmpCellFormat = wSheet01.getWritableCell(9, 3).getCellFormat();
                Label lbl = new Label(9, 3, String.valueOf(tmpTotalArea));
                lbl.setCellFormat(tmpCellFormat);
                wSheet01.addCell(lbl);
                CellFormat tmpCellFormat2 = wSheet01.getWritableCell(37, 3).getCellFormat();
                Label lbl2 = new Label(37, 3, String.valueOf(tmpTotalLinMuXuJi));
                lbl2.setCellFormat(tmpCellFormat2);
                wSheet01.addCell(lbl2);
                CellFormat tmpCellFormat3 = wSheet01.getWritableCell(38, 3).getCellFormat();
                Label lbl3 = new Label(38, 3, String.valueOf((int) tmpTotalZhuShu));
                lbl3.setCellFormat(tmpCellFormat3);
                wSheet01.addCell(lbl3);
                CellFormat tmpCellFormat4 = wSheet01.getWritableCell(44, 3).getCellFormat();
                Label lbl4 = new Label(44, 3, String.valueOf(tmpTotalXuJi));
                lbl4.setCellFormat(tmpCellFormat4);
                wSheet01.addCell(lbl4);
            }
            if (tmpHashMap.size() > 0) {
                for (Map.Entry<String, HashMap<String, String>> tmpEntry3 : tmpHashMap.entrySet()) {
                    String tmpYDName = tmpEntry3.getKey();
                    String tmpTableName = "3" + tmpYDName;
                    HashMap<String, String> tmphasMap2 = tmpEntry3.getValue();
                    wb.copySheet("3记录表", tmpTableName, wb.getNumberOfSheets() - 1);
                    WritableSheet tmpSheet = wb.getSheet(tmpTableName);
                    CellFormat tmpCellFormat5 = tmpSheet.getWritableCell(2, 1).getCellFormat();
                    Label lbl5 = new Label(2, 1, tmphasMap2.get("XiaoBan"));
                    lbl5.setCellFormat(tmpCellFormat5);
                    tmpSheet.addCell(lbl5);
                    CellFormat tmpCellFormat6 = tmpSheet.getWritableCell(5, 1).getCellFormat();
                    Label lbl6 = new Label(5, 1, tmphasMap2.get("YangDiIndex"));
                    lbl6.setCellFormat(tmpCellFormat6);
                    tmpSheet.addCell(lbl6);
                    CellFormat tmpCellFormat7 = tmpSheet.getWritableCell(9, 1).getCellFormat();
                    Label lbl7 = new Label(9, 1, tmphasMap2.get("YDArea"));
                    lbl7.setCellFormat(tmpCellFormat7);
                    tmpSheet.addCell(lbl7);
                    List<String[]> tmpList01 = CommonSetting.GetXiaoBanYangDiMeiMuTable(tmpYDName);
                    if (tmpList01.size() > 0) {
                        int tmpTotalCount = tmpList01.size();
                        int tmpRowsCount = (tmpTotalCount + 1) / 2;
                        for (int i23 = 0; i23 < tmpRowsCount; i23++) {
                            tmpSheet.insertRow(4 + i23);
                            String[] tmpStrs012 = tmpList01.get(i23);
                            tmpSheet.addCell(new Label(0, 4 + i23, String.valueOf(i23 + 1), headerFormat));
                            tmpSheet.addCell(new Label(1, 4 + i23, tmpStrs012[0], headerFormat));
                            tmpSheet.addCell(new Label(2, 4 + i23, tmpStrs012[1], headerFormat));
                            tmpSheet.addCell(new Label(3, 4 + i23, tmpStrs012[2], headerFormat));
                            tmpSheet.addCell(new Label(4, 4 + i23, tmpStrs012[3], headerFormat));
                            if (i23 + tmpRowsCount >= tmpTotalCount) {
                                tmpStrs01 = new String[4];
                                for (int j = 0; j < 4; j++) {
                                    tmpStrs01[j] = "";
                                }
                                tmpSheet.addCell(new Label(5, 4 + i23, "", headerFormat));
                            } else {
                                tmpStrs01 = tmpList01.get(i23 + tmpRowsCount);
                                tmpSheet.addCell(new Label(5, 4 + i23, String.valueOf(i23 + tmpRowsCount + 1), headerFormat));
                            }
                            tmpSheet.addCell(new Label(6, 4 + i23, tmpStrs01[0], headerFormat));
                            tmpSheet.addCell(new Label(7, 4 + i23, tmpStrs01[1], headerFormat));
                            tmpSheet.addCell(new Label(8, 4 + i23, tmpStrs01[2], headerFormat));
                            tmpSheet.addCell(new Label(9, 4 + i23, tmpStrs01[3], headerFormat));
                        }
                        HashMap<String, String[]> tmpCeShuHashMap = CommonSetting.GetXiaoBanYangDiCeShuYinZiTable(tmpYDName);
                        if (tmpCeShuHashMap != null && tmpCeShuHashMap.size() > 0) {
                            int tmpStartRow = 4 + tmpRowsCount + 3;
                            Label lbl8 = new Label(1, tmpStartRow - 2, tmpSheet.getWritableCell(1, tmpStartRow - 2).getContents());
                            lbl8.setCellFormat(headerFormat);
                            tmpSheet.addCell(lbl8);
                            String[] tmpStrs013 = {"平均胸径", "平均树高", "样地蓄积", "公顷蓄积", "样地株树", "公顷株树"};
                            int tmpRowIndex = -1;
                            int length = tmpStrs013.length;
                            for (int i24 = 0; i24 < length; i24++) {
                                String tmpXM = tmpStrs013[i24];
                                tmpRowIndex++;
                                if (tmpCeShuHashMap.containsKey(tmpXM)) {
                                    String[] tmpStrings02 = tmpCeShuHashMap.get(tmpXM);
                                    int tmpCount2 = tmpStrings02.length;
                                    for (int j2 = 0; j2 < tmpCount2; j2++) {
                                        tmpSheet.addCell(new Label(1 + j2, tmpStartRow + tmpRowIndex, tmpStrings02[j2], headerFormat));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            int tmpSheetIndex = wb.getNumberOfSheets() - 1;
            if (wb.getSheet(tmpSheetIndex).getName().equals("3记录表")) {
                wb.removeSheet(tmpSheetIndex);
            }
            wb.write();
            wb.close();
            return false;
        } catch (Exception e5) {
            Common.ShowToast(e5.getMessage());
            return false;
        }
    }

    private void exportYangdi(String tubanName, String yangdiName, int yangdiIndex, String saveFolder, boolean exportPhoto) {
        String[] tmpStrs02;
        WritableWorkbook wb;
        String tubanName2 = tubanName.replace(" ", "_");
        String tmpExcelPathString = String.valueOf(saveFolder) + FileSelector_Dialog.sRoot + tubanName2 + ".xls";
        SQLiteDBHelper tmpSqLiteDBHelper = CommonSetting.GetSQLiteDBHelper();
        try {
            if (new File(tmpExcelPathString).exists()) {
                wb = Workbook.createWorkbook(new File(tmpExcelPathString), Workbook.getWorkbook(new File(tmpExcelPathString)));
            } else {
                wb = Workbook.createWorkbook(new File(tmpExcelPathString));
            }
            WritableSheet wSheet = wb.createSheet("样地" + String.valueOf(yangdiIndex), yangdiIndex - 1);
            wSheet.addCell(new Label(0, 0, "根径"));
            wSheet.addCell(new Label(1, 0, "杉木"));
            wSheet.addCell(new Label(2, 0, "马尾松"));
            wSheet.addCell(new Label(3, 0, "阔叶树"));
            int tmpTid = 0;
            if (tmpSqLiteDBHelper != null) {
                SQLiteReader tmpSQLiteReader = this.m_SQLiteDBHelper.Query("Select GenJing,Shan,Ma,Kuo From T_YangDiData Where YangDiName='" + yangdiName + "' order by GenJing");
                while (tmpSQLiteReader.Read()) {
                    tmpTid++;
                    wSheet.addCell(new Label(0, tmpTid, String.valueOf(tmpSQLiteReader.GetInt32(0))));
                    wSheet.addCell(new Label(1, tmpTid, String.valueOf(tmpSQLiteReader.GetInt32(1))));
                    wSheet.addCell(new Label(2, tmpTid, String.valueOf(tmpSQLiteReader.GetInt32(2))));
                    wSheet.addCell(new Label(3, tmpTid, String.valueOf(tmpSQLiteReader.GetInt32(3))));
                }
            }
            wb.write();
            wb.close();
        } catch (Exception e) {
            Common.Log("导出excel时错误", e.getLocalizedMessage());
        }
        if (exportPhoto) {
            String tmpSqlString = "Select PhotoInfo From T_YangDiInfo Where YangDiName='" + yangdiName + "'";
            if (tmpSqLiteDBHelper != null) {
                String tmpOrgPhotoFolder = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/Photo/";
                SQLiteReader tmpSQLiteReader2 = this.m_SQLiteDBHelper.Query(tmpSqlString);
                while (tmpSQLiteReader2.Read()) {
                    String tmpPhotoInfoString = tmpSQLiteReader2.GetString(0);
                    String tmpFileName = String.valueOf(tubanName2) + "_样地" + String.valueOf(yangdiIndex);
                    if (tmpPhotoInfoString != null && tmpPhotoInfoString.length() > 0 && (tmpStrs02 = tmpPhotoInfoString.split(",")) != null && tmpStrs02.length > 0) {
                        int tmpTid2 = 1;
                        int length = tmpStrs02.length;
                        for (int i = 0; i < length; i++) {
                            String tmpStr03 = tmpStrs02[i];
                            if (tmpStr03.length() > 0) {
                                String tmpFilePath01 = String.valueOf(tmpOrgPhotoFolder) + tmpStr03;
                                if (new File(tmpFilePath01).exists()) {
                                    Common.CopyFile(tmpFilePath01, String.valueOf(saveFolder) + FileSelector_Dialog.sRoot + tmpFileName + "_" + String.valueOf(tmpTid2) + ".jpg");
                                    tmpTid2++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void exportSelectYangdiToShp(String saveFilePath) {
        List<String> tmpList = new ArrayList<>();
        for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
            if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                tmpList.add(String.valueOf(tmpHash.get("yangdiname")));
            }
        }
        if (CommonSetting.GetSQLiteDBHelper() != null) {
            List<ArrayList> tmpValuesList = new ArrayList<>();
            List<AbstractGeometry> tmpGeoList = new ArrayList<>();
            double tmpMinY = Double.MAX_VALUE;
            double tmpMinX = Double.MAX_VALUE;
            double tmpMaxY = Double.MIN_VALUE;
            double tmpMaxX = Double.MIN_VALUE;
            SQLiteReader tmpSQLiteReader = this.m_SQLiteDBHelper.Query("Select Sheng,Xian,Xiang,Cun,partIndex,YangDiIndex,X,Y From T_YangDiInfo Where YangDiName in ('" + Common.CombineStrings("','", tmpList) + "')");
            while (tmpSQLiteReader.Read()) {
                ArrayList tmpArrayList01 = new ArrayList();
                tmpArrayList01.add(String.valueOf(tmpSQLiteReader.GetString(0)));
                tmpArrayList01.add(String.valueOf(tmpSQLiteReader.GetString(1)));
                tmpArrayList01.add(String.valueOf(tmpSQLiteReader.GetString(2)));
                tmpArrayList01.add(String.valueOf(tmpSQLiteReader.GetString(3)));
                tmpArrayList01.add(String.valueOf(tmpSQLiteReader.GetString(4)));
                tmpArrayList01.add(String.valueOf(tmpSQLiteReader.GetString(5)));
                double tmpX = tmpSQLiteReader.GetDouble(6);
                double tmpY = tmpSQLiteReader.GetDouble(7);
                Point tmpPtn = new Point(tmpX, tmpY);
                if (tmpMinX > tmpX) {
                    tmpMinX = tmpX;
                }
                if (tmpMinY > tmpY) {
                    tmpMinY = tmpY;
                }
                if (tmpMaxX < tmpX) {
                    tmpMaxX = tmpX;
                }
                if (tmpMaxY < tmpY) {
                    tmpMaxY = tmpY;
                }
                tmpGeoList.add(tmpPtn);
                tmpValuesList.add(tmpArrayList01);
            }
            Envelope tmpExtend = new Envelope(tmpMinX, tmpMaxY, tmpMaxX, tmpMinY);
            ExportToShp tempExport = new ExportToShp();
            tempExport.setExportCoordType(PubVar.m_Workspace.GetCoorSystem().GetName());
            tempExport.ToPrj(String.valueOf(saveFilePath) + ".prj");
            if (tempExport.ExportToShp(String.valueOf(saveFilePath) + ".shp", tmpGeoList, tmpExtend, false)) {
                List<LayerField> tmpfields = new ArrayList<>();
                LayerField tmpField00 = new LayerField();
                tmpField00.SetFieldName("省");
                tmpField00.SetFieldTypeName("字符串");
                tmpfields.add(tmpField00);
                LayerField tmpField002 = new LayerField();
                tmpField002.SetFieldName("县");
                tmpField002.SetFieldTypeName("字符串");
                tmpfields.add(tmpField002);
                LayerField tmpField01 = new LayerField();
                tmpField01.SetFieldName("乡镇");
                tmpField01.SetFieldTypeName("字符串");
                tmpfields.add(tmpField01);
                LayerField tmpField02 = new LayerField();
                tmpField02.SetFieldName("村");
                tmpField02.SetFieldTypeName("字符串");
                tmpfields.add(tmpField02);
                LayerField tmpField03 = new LayerField();
                tmpField03.SetFieldName("小班号");
                tmpField03.SetFieldTypeName("字符串");
                tmpfields.add(tmpField03);
                LayerField tmpField04 = new LayerField();
                tmpField04.SetFieldName("样地号");
                tmpField04.SetFieldTypeName("字符串");
                tmpfields.add(tmpField04);
                if (tempExport.ExportToDbf(String.valueOf(saveFilePath) + ".dbf", tmpfields, tmpValuesList)) {
                    tempExport.ExportToShx(String.valueOf(saveFilePath) + ".shx", tmpGeoList, tmpExtend, false);
                }
            }
        }
        Common.ShowDialog("导出数据完成,文件导出至:\r\n\r\n" + saveFilePath);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean ExportAllXiaoBanTable(String m_ExportFolder, boolean exportPhoto) {
        String[] tmpStrs01;
        List<Integer> tmpList01;
        String tmpExcelPathString = String.valueOf(m_ExportFolder) + "/小班因子调查表.xls";
        String tmpOrgPhotoFolder = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/Photo/";
        try {
            WritableCellFormat headerFormat = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 8));
            headerFormat.setAlignment(Alignment.CENTRE);
            headerFormat.setVerticalAlignment(VerticalAlignment.CENTRE);
            headerFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
            WritableCellFormat headerFormat6 = new WritableCellFormat(new WritableFont(WritableFont.ARIAL, 6));
            headerFormat6.setAlignment(Alignment.CENTRE);
            headerFormat6.setVerticalAlignment(VerticalAlignment.CENTRE);
            headerFormat6.setBorder(Border.ALL, BorderLineStyle.THIN);
            WritableWorkbook wb = Workbook.createWorkbook(new File(tmpExcelPathString), Workbook.getWorkbook(new File(String.valueOf(Common.GetAPPPath()) + "/SysFile/xbyzdctable.xls")));
            HashMap<String, HashMap<String, String>> tmpHashMap = new HashMap<>();
            HashMap<String, List<String>> tmpLyrHashMap = new HashMap<>();
            WritableSheet wSheet02 = wb.getSheet("2因子调查表");
            SQLiteReader tmpSQLiteReader = this.m_SQLiteDBHelper.Query("Select YangDiName,YangDiIndex,Xian,Xiang,Cun,XiaoBan,LayerID,SYSID,X,Y,YDArea,YouShiShuZhong,ZhuZhongZuCheng,QiYuan,QiaoMuYBD,GuanMuFGD,PJNL,LingZu,PhotoInfo,DiaoChaRen,DiaoChaTime,Remark From T_YangDiInfo Where YangDiName <> ''");
            int tmpRow = -1;
            while (tmpSQLiteReader.Read()) {
                tmpRow++;
                String tmpYDNameString = tmpSQLiteReader.GetString(0);
                HashMap<String, String> tmpHashMap2 = new HashMap<>();
                int i = 0 + 1;
                tmpHashMap2.put("YangDiName", tmpSQLiteReader.GetString(0));
                int i2 = i + 1;
                tmpHashMap2.put("YangDiIndex", tmpSQLiteReader.GetString(i));
                int i3 = i2 + 1;
                tmpHashMap2.put("Xian", tmpSQLiteReader.GetString(i2));
                int i4 = i3 + 1;
                tmpHashMap2.put("Xiang", tmpSQLiteReader.GetString(i3));
                int i5 = i4 + 1;
                tmpHashMap2.put("Cun", tmpSQLiteReader.GetString(i4));
                int i6 = i5 + 1;
                tmpHashMap2.put("XiaoBan", tmpSQLiteReader.GetString(i5));
                int i7 = i6 + 1;
                String tmpLyrID = tmpSQLiteReader.GetString(i6);
                tmpHashMap2.put("LayerID", tmpLyrID);
                int i8 = i7 + 1;
                String tmpSYSID = tmpSQLiteReader.GetString(i7);
                tmpHashMap2.put("SYSID", tmpSYSID);
                int i9 = i8 + 1;
                tmpHashMap2.put("X", String.format("%.2f", Double.valueOf(tmpSQLiteReader.GetDouble(i8))));
                int i10 = i9 + 1;
                tmpHashMap2.put("Y", String.format("%.2f", Double.valueOf(tmpSQLiteReader.GetDouble(i9))));
                int i11 = i10 + 1;
                tmpHashMap2.put("YDArea", String.format("%.4f", Double.valueOf(tmpSQLiteReader.GetDouble(i10))));
                int i12 = i11 + 1;
                tmpHashMap2.put("YouShiShuZhong", tmpSQLiteReader.GetString(i11));
                int i13 = i12 + 1;
                tmpHashMap2.put("ZhuZhongZuCheng", tmpSQLiteReader.GetString(i12));
                int i14 = i13 + 1;
                tmpHashMap2.put("QiYuan", tmpSQLiteReader.GetString(i13));
                int i15 = i14 + 1;
                tmpHashMap2.put("QiaoMuYBD", tmpSQLiteReader.GetString(i14));
                int i16 = i15 + 1;
                tmpHashMap2.put("GuanMuFGD", tmpSQLiteReader.GetString(i15));
                int i17 = i16 + 1;
                tmpHashMap2.put("PJNL", tmpSQLiteReader.GetString(i16));
                int i18 = i17 + 1;
                tmpHashMap2.put("LingZu", tmpSQLiteReader.GetString(i17));
                int i19 = i18 + 1;
                tmpHashMap2.put("PhotoInfo", tmpSQLiteReader.GetString(i18));
                int i20 = i19 + 1;
                tmpHashMap2.put("DiaoChaRen", tmpSQLiteReader.GetString(i19));
                int i21 = i20 + 1;
                tmpHashMap2.put("DiaoChaTime", tmpSQLiteReader.GetString(i20));
                int i22 = i21 + 1;
                tmpHashMap2.put("Remark", tmpSQLiteReader.GetString(i21));
                tmpHashMap.put(tmpYDNameString, tmpHashMap2);
                if (tmpLyrHashMap.containsKey(tmpLyrID)) {
                    tmpLyrHashMap.get(tmpLyrID).add(tmpSYSID);
                } else {
                    List<String> tmpList012 = new ArrayList<>();
                    tmpList012.add(tmpSYSID);
                    tmpLyrHashMap.put(tmpLyrID, tmpList012);
                }
                wSheet02.addCell(new Label(0, 3 + tmpRow, String.valueOf(tmpRow + 1), headerFormat));
                wSheet02.addCell(new Label(1, 3 + tmpRow, tmpHashMap2.get("Xian"), headerFormat));
                wSheet02.addCell(new Label(2, 3 + tmpRow, tmpHashMap2.get("Xiang"), headerFormat));
                wSheet02.addCell(new Label(3, 3 + tmpRow, tmpHashMap2.get("Cun"), headerFormat));
                wSheet02.addCell(new Label(4, 3 + tmpRow, tmpHashMap2.get("XiaoBan"), headerFormat));
                wSheet02.addCell(new Label(5, 3 + tmpRow, tmpHashMap2.get("X"), headerFormat));
                wSheet02.addCell(new Label(6, 3 + tmpRow, tmpHashMap2.get("Y"), headerFormat));
                wSheet02.addCell(new Label(7, 3 + tmpRow, tmpHashMap2.get("YouShiShuZhong"), headerFormat));
                wSheet02.addCell(new Label(8, 3 + tmpRow, tmpHashMap2.get("ZhuZhongZuCheng"), headerFormat));
                wSheet02.addCell(new Label(9, 3 + tmpRow, tmpHashMap2.get("QiYuan"), headerFormat));
                wSheet02.addCell(new Label(10, 3 + tmpRow, tmpHashMap2.get("QiaoMuYBD"), headerFormat));
                wSheet02.addCell(new Label(11, 3 + tmpRow, tmpHashMap2.get("GuanMuFGD"), headerFormat));
                wSheet02.addCell(new Label(12, 3 + tmpRow, tmpHashMap2.get("PJNL"), headerFormat));
                wSheet02.addCell(new Label(13, 3 + tmpRow, tmpHashMap2.get("LingZu"), headerFormat));
                wSheet02.addCell(new Label(14, 3 + tmpRow, ConvertToPhotoNames(tmpHashMap2.get("PhotoInfo"), tmpHashMap2.get("YangDiIndex")), headerFormat));
                wSheet02.addCell(new Label(15, 3 + tmpRow, tmpHashMap2.get("DiaoChaRen"), headerFormat));
                wSheet02.addCell(new Label(16, 3 + tmpRow, tmpHashMap2.get("Remark"), headerFormat));
            }
            if (tmpHashMap.size() > 0) {
                WritableSheet wSheet01 = wb.getSheet("1小班因子调查表");
                int tmpCurrentRow = 4 - 1;
                double tmpTotalArea = 0.0d;
                double tmpTotalLinMuXuJi = 0.0d;
                double tmpTotalZhuShu = 0.0d;
                double tmpTotalXuJi = 0.0d;
                for (Map.Entry<String, HashMap<String, String>> tmpEntry : tmpHashMap.entrySet()) {
                    HashMap<String, String> tmphasMap = tmpEntry.getValue();
                    String tmpSYSID2 = tmphasMap.get("SYSID");
                    GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByID(tmphasMap.get("LayerID"));
                    if (tmpGeoLayer != null) {
                        tmpCurrentRow++;
                        HashMap<String, Object> tmphashMap2 = tmpGeoLayer.getDataset().getGeometryFieldValuesBySYSID(tmpSYSID2, null);
                        int tmpColIndex = -1;
                        for (String tmpFIDName : CommonSetting.m_XiaoBanLayerMustFieldsNameList) {
                            tmpColIndex++;
                            if (tmpColIndex == 50) {
                                tmpColIndex += 3;
                            }
                            String tmpFIDName2 = CommonSetting.m_XiaoBanLayerMustFieldsName.get(tmpFIDName);
                            String tmpFValue = "";
                            if (tmphashMap2.containsKey(tmpFIDName2)) {
                                tmpFValue = String.valueOf(tmphashMap2.get(tmpFIDName2));
                            }
                            wSheet01.addCell(new Label(0 + tmpColIndex, tmpCurrentRow, tmpFValue, headerFormat6));
                            if (tmpColIndex == 9) {
                                double tmpD01 = 0.0d;
                                try {
                                    tmpD01 = Double.parseDouble(tmpFValue);
                                } catch (Exception e) {
                                }
                                tmpTotalArea += tmpD01;
                            } else if (tmpColIndex == 37) {
                                double tmpD012 = 0.0d;
                                try {
                                    tmpD012 = Double.parseDouble(tmpFValue);
                                } catch (Exception e2) {
                                }
                                tmpTotalLinMuXuJi += tmpD012;
                            } else if (tmpColIndex == 38) {
                                double tmpD013 = 0.0d;
                                try {
                                    tmpD013 = Double.parseDouble(tmpFValue);
                                } catch (Exception e3) {
                                }
                                tmpTotalZhuShu += tmpD013;
                            } else if (tmpColIndex == 44) {
                                double tmpD014 = 0.0d;
                                try {
                                    tmpD014 = Double.parseDouble(tmpFValue);
                                } catch (Exception e4) {
                                }
                                tmpTotalXuJi += tmpD014;
                            }
                        }
                        tmphasMap.get("XiaoBan");
                        String tmpMediaInfo = tmpGeoLayer.getDataset().getGeoemtryMediaInfoBySYSID(tmpSYSID2);
                        if (tmpMediaInfo == null || tmpMediaInfo.trim().length() == 0) {
                            tmpMediaInfo = tmphasMap.get("PhotoInfo");
                        }
                        if (tmpMediaInfo != null && tmpMediaInfo.length() > 0) {
                            wSheet01.addCell(new Label(50, tmpCurrentRow, ConvertToPhotoNames(tmpMediaInfo, tmphasMap.get("YangDiIndex")), headerFormat6));
                        }
                        String tmpDCR = tmphasMap.get("DiaoChaRen");
                        if (tmpDCR != null && tmpDCR.length() > 0) {
                            wSheet01.addCell(new Label(51, tmpCurrentRow, tmpDCR, headerFormat6));
                        }
                        String tmpDiaoChaTime = tmphasMap.get("DiaoChaTime");
                        if (tmpDiaoChaTime != null && tmpDiaoChaTime.length() > 0) {
                            wSheet01.addCell(new Label(52, tmpCurrentRow, tmpDiaoChaTime, headerFormat6));
                        }
                        if (exportPhoto && tmpMediaInfo != null && tmpMediaInfo.length() > 0) {
                            String tmpMediaFolderString = String.valueOf(m_ExportFolder) + FileSelector_Dialog.sRoot + tmphasMap.get("Xiang") + "_" + tmphasMap.get("Cun") + "_" + tmphasMap.get("XiaoBan");
                            if (!Common.ExistFolder(tmpMediaFolderString)) {
                                new File(tmpMediaFolderString).mkdir();
                            }
                            HashMap<String, String> tmpMediaHashMap = ConvertPhotoNamesToHash(tmpMediaInfo, tmphasMap.get("YangDiIndex"));
                            if (tmpMediaHashMap.size() > 0) {
                                for (Map.Entry<String, String> tmpEntry2 : tmpMediaHashMap.entrySet()) {
                                    String tmpFilePath01 = String.valueOf(tmpOrgPhotoFolder) + tmpEntry2.getValue();
                                    if (new File(tmpFilePath01).exists()) {
                                        Common.CopyFile(tmpFilePath01, String.valueOf(tmpMediaFolderString) + FileSelector_Dialog.sRoot + tmpEntry2.getKey() + ".jpg");
                                    }
                                }
                            }
                        }
                    }
                }
                if (tmpLyrHashMap.size() > 0) {
                    for (Map.Entry<String, List<String>> tmpEntry3 : tmpLyrHashMap.entrySet()) {
                        List<String> tmpHasSYSIDList = tmpEntry3.getValue();
                        GeoLayer tmpGeoLayer2 = PubVar._Map.GetGeoLayerByID(tmpEntry3.getKey());
                        if (!(tmpGeoLayer2 == null || (tmpList01 = tmpGeoLayer2.getDataset().GetAllGeometrysSYSIDList()) == null || tmpList01.size() <= 0)) {
                            for (Integer num : tmpList01) {
                                String tmpSYSID3 = String.valueOf(num);
                                if (!tmpHasSYSIDList.contains(tmpSYSID3)) {
                                    HashMap<String, Object> tmphashMap22 = tmpGeoLayer2.getDataset().getGeometryFieldValuesBySYSID(tmpSYSID3, null);
                                    if (tmphashMap22.size() > 0) {
                                        tmpCurrentRow++;
                                        int tmpColIndex2 = -1;
                                        for (String tmpFIDName3 : CommonSetting.m_XiaoBanLayerMustFieldsNameList) {
                                            tmpColIndex2++;
                                            if (tmpColIndex2 == 50) {
                                                tmpColIndex2 += 3;
                                            }
                                            String tmpFIDName22 = CommonSetting.m_XiaoBanLayerMustFieldsName.get(tmpFIDName3);
                                            String tmpFValue2 = "";
                                            if (tmphashMap22.containsKey(tmpFIDName22)) {
                                                tmpFValue2 = String.valueOf(tmphashMap22.get(tmpFIDName22));
                                            }
                                            wSheet01.addCell(new Label(0 + tmpColIndex2, tmpCurrentRow, tmpFValue2, headerFormat6));
                                            if (tmpColIndex2 == 9) {
                                                double tmpD015 = 0.0d;
                                                try {
                                                    tmpD015 = Double.parseDouble(tmpFValue2);
                                                } catch (Exception e5) {
                                                }
                                                tmpTotalArea += tmpD015;
                                            } else if (tmpColIndex2 == 37) {
                                                double tmpD016 = 0.0d;
                                                try {
                                                    tmpD016 = Double.parseDouble(tmpFValue2);
                                                } catch (Exception e6) {
                                                }
                                                tmpTotalLinMuXuJi += tmpD016;
                                            } else if (tmpColIndex2 == 38) {
                                                double tmpD017 = 0.0d;
                                                try {
                                                    tmpD017 = Double.parseDouble(tmpFValue2);
                                                } catch (Exception e7) {
                                                }
                                                tmpTotalZhuShu += tmpD017;
                                            } else if (tmpColIndex2 == 44) {
                                                double tmpD018 = 0.0d;
                                                try {
                                                    tmpD018 = Double.parseDouble(tmpFValue2);
                                                } catch (Exception e8) {
                                                }
                                                tmpTotalXuJi += tmpD018;
                                            }
                                        }
                                        String tmpMediaInfo2 = tmpGeoLayer2.getDataset().getGeoemtryMediaInfoBySYSID(tmpSYSID3);
                                        if (tmpMediaInfo2 != null && tmpMediaInfo2.length() > 0) {
                                            wSheet01.addCell(new Label(50, tmpCurrentRow, ConvertToPhotoNames(tmpMediaInfo2, tmpSYSID3), headerFormat6));
                                        }
                                        if (exportPhoto && tmpMediaInfo2 != null && tmpMediaInfo2.length() > 0) {
                                            String tmpMediaFolderString2 = String.valueOf(m_ExportFolder) + "/小班_" + tmpSYSID3;
                                            if (!Common.ExistFolder(tmpMediaFolderString2)) {
                                                new File(tmpMediaFolderString2).mkdir();
                                            }
                                            HashMap<String, String> tmpMediaHashMap2 = ConvertPhotoNamesToHash(tmpMediaInfo2, tmpSYSID3);
                                            if (tmpMediaHashMap2.size() > 0) {
                                                for (Map.Entry<String, String> tmpEntry22 : tmpMediaHashMap2.entrySet()) {
                                                    String tmpFilePath012 = String.valueOf(tmpOrgPhotoFolder) + tmpEntry22.getValue();
                                                    if (new File(tmpFilePath012).exists()) {
                                                        Common.CopyFile(tmpFilePath012, String.valueOf(tmpMediaFolderString2) + FileSelector_Dialog.sRoot + tmpEntry22.getKey() + ".jpg");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                CellFormat tmpCellFormat = wSheet01.getWritableCell(9, 3).getCellFormat();
                Label lbl = new Label(9, 3, String.valueOf(tmpTotalArea));
                lbl.setCellFormat(tmpCellFormat);
                wSheet01.addCell(lbl);
                CellFormat tmpCellFormat2 = wSheet01.getWritableCell(37, 3).getCellFormat();
                Label lbl2 = new Label(37, 3, String.valueOf(tmpTotalLinMuXuJi));
                lbl2.setCellFormat(tmpCellFormat2);
                wSheet01.addCell(lbl2);
                CellFormat tmpCellFormat3 = wSheet01.getWritableCell(38, 3).getCellFormat();
                Label lbl3 = new Label(38, 3, String.valueOf((int) tmpTotalZhuShu));
                lbl3.setCellFormat(tmpCellFormat3);
                wSheet01.addCell(lbl3);
                CellFormat tmpCellFormat4 = wSheet01.getWritableCell(44, 3).getCellFormat();
                Label lbl4 = new Label(44, 3, String.valueOf(tmpTotalXuJi));
                lbl4.setCellFormat(tmpCellFormat4);
                wSheet01.addCell(lbl4);
            }
            if (tmpHashMap.size() > 0) {
                for (Map.Entry<String, HashMap<String, String>> tmpEntry4 : tmpHashMap.entrySet()) {
                    String tmpYDName = tmpEntry4.getKey();
                    String tmpTableName = "3" + tmpYDName;
                    HashMap<String, String> tmphasMap2 = tmpEntry4.getValue();
                    wb.copySheet("3记录表", tmpTableName, wb.getNumberOfSheets() - 1);
                    WritableSheet tmpSheet = wb.getSheet(tmpTableName);
                    CellFormat tmpCellFormat5 = tmpSheet.getWritableCell(2, 1).getCellFormat();
                    Label lbl5 = new Label(2, 1, tmphasMap2.get("XiaoBan"));
                    lbl5.setCellFormat(tmpCellFormat5);
                    tmpSheet.addCell(lbl5);
                    CellFormat tmpCellFormat6 = tmpSheet.getWritableCell(5, 1).getCellFormat();
                    Label lbl6 = new Label(5, 1, tmphasMap2.get("YangDiIndex"));
                    lbl6.setCellFormat(tmpCellFormat6);
                    tmpSheet.addCell(lbl6);
                    CellFormat tmpCellFormat7 = tmpSheet.getWritableCell(9, 1).getCellFormat();
                    Label lbl7 = new Label(9, 1, tmphasMap2.get("YDArea"));
                    lbl7.setCellFormat(tmpCellFormat7);
                    tmpSheet.addCell(lbl7);
                    List<String[]> tmpList013 = CommonSetting.GetXiaoBanYangDiMeiMuTable(tmpYDName);
                    if (tmpList013.size() > 0) {
                        int tmpTotalCount = tmpList013.size();
                        int tmpRowsCount = (tmpTotalCount + 1) / 2;
                        for (int i23 = 0; i23 < tmpRowsCount; i23++) {
                            tmpSheet.insertRow(4 + i23);
                            String[] tmpStrs012 = tmpList013.get(i23);
                            tmpSheet.addCell(new Label(0, 4 + i23, String.valueOf(i23 + 1), headerFormat));
                            tmpSheet.addCell(new Label(1, 4 + i23, tmpStrs012[0], headerFormat));
                            tmpSheet.addCell(new Label(2, 4 + i23, tmpStrs012[1], headerFormat));
                            tmpSheet.addCell(new Label(3, 4 + i23, tmpStrs012[2], headerFormat));
                            tmpSheet.addCell(new Label(4, 4 + i23, tmpStrs012[3], headerFormat));
                            if (i23 + tmpRowsCount >= tmpTotalCount) {
                                tmpStrs01 = new String[4];
                                for (int j = 0; j < 4; j++) {
                                    tmpStrs01[j] = "";
                                }
                                tmpSheet.addCell(new Label(5, 4 + i23, "", headerFormat));
                            } else {
                                tmpStrs01 = tmpList013.get(i23 + tmpRowsCount);
                                tmpSheet.addCell(new Label(5, 4 + i23, String.valueOf(i23 + tmpRowsCount + 1), headerFormat));
                            }
                            tmpSheet.addCell(new Label(6, 4 + i23, tmpStrs01[0], headerFormat));
                            tmpSheet.addCell(new Label(7, 4 + i23, tmpStrs01[1], headerFormat));
                            tmpSheet.addCell(new Label(8, 4 + i23, tmpStrs01[2], headerFormat));
                            tmpSheet.addCell(new Label(9, 4 + i23, tmpStrs01[3], headerFormat));
                        }
                        HashMap<String, String[]> tmpCeShuHashMap = CommonSetting.GetXiaoBanYangDiCeShuYinZiTable(tmpYDName);
                        if (tmpCeShuHashMap != null && tmpCeShuHashMap.size() > 0) {
                            int tmpStartRow = 4 + tmpRowsCount + 3;
                            Label lbl8 = new Label(1, tmpStartRow - 2, tmpSheet.getWritableCell(1, tmpStartRow - 2).getContents());
                            lbl8.setCellFormat(headerFormat);
                            tmpSheet.addCell(lbl8);
                            String[] tmpStrs013 = {"平均胸径", "平均树高", "样地蓄积", "公顷蓄积", "样地株树", "公顷株树"};
                            int tmpRowIndex = -1;
                            int length = tmpStrs013.length;
                            for (int i24 = 0; i24 < length; i24++) {
                                String tmpXM = tmpStrs013[i24];
                                tmpRowIndex++;
                                if (tmpCeShuHashMap.containsKey(tmpXM)) {
                                    String[] tmpStrings02 = tmpCeShuHashMap.get(tmpXM);
                                    int tmpCount2 = tmpStrings02.length;
                                    for (int j2 = 0; j2 < tmpCount2; j2++) {
                                        tmpSheet.addCell(new Label(1 + j2, tmpStartRow + tmpRowIndex, tmpStrings02[j2], headerFormat));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            int tmpSheetIndex = wb.getNumberOfSheets() - 1;
            if (wb.getSheet(tmpSheetIndex).getName().equals("3记录表")) {
                wb.removeSheet(tmpSheetIndex);
            }
            wb.write();
            wb.close();
            Common.ShowDialog("导出数据完成,文件导出至:\r\n\r\n" + m_ExportFolder);
            return true;
        } catch (Exception e9) {
            Common.ShowToast(e9.getMessage());
            return false;
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.xzy.forestSystem.GuiZhou.DiaoCha.XiaoBanYangDiQuery_Dialog.6
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                XiaoBanYangDiQuery_Dialog.this.refreshList();
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
                XiaoBanYangDiQuery_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
