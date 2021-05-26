package  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.GraphicSymbolGeometry;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Display.ISymbol;
import  com.xzy.forestSystem.gogisapi.Geodatabase.ExportToShp;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.Geometry.Point;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.LoadingDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Others.MediaActivity;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class YangDiQuery_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private String m_RevYangdiDataString;
    private String m_RevYangdiInfoString;
    private List<String> m_RevYangdiNameList;
    private List<String> m_RevYangdiNameList2;
    private SQLiteDBHelper m_SQLiteDBHelper;
    private SpinnerList m_SpinnerList01;
    private SpinnerList m_SpinnerList02;
    private SpinnerList m_SpinnerList03;
    private SpinnerList m_SpinnerList04;
    private SpinnerList m_SpinnerList05;
    private SpinnerList m_SpinnerList06;
    Button m_expandButton;
    Handler myHandler;
    private ICallback pCallback;
    LoadingDialog progressDialog;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public YangDiQuery_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_SQLiteDBHelper = null;
        this.m_SpinnerList01 = null;
        this.m_SpinnerList02 = null;
        this.m_SpinnerList03 = null;
        this.m_SpinnerList04 = null;
        this.m_SpinnerList05 = null;
        this.m_SpinnerList06 = null;
        this.m_expandButton = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiQuery_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("选择省返回")) {
                        String tmpString = String.valueOf(object);
                        if (tmpString.length() > 0) {
                            List<String> tmpList01 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader = YangDiQuery_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct Xian From T_YangDiInfo Where Sheng ='" + tmpString + "'");
                            while (tmpSQLiteReader.Read()) {
                                tmpList01.add(tmpSQLiteReader.GetString(0));
                            }
                            if (tmpList01.size() > 0) {
                                YangDiQuery_Dialog.this.m_SpinnerList02.setEnabled(true);
                                Common.SetSpinnerListData(YangDiQuery_Dialog.this._Dialog, (int) R.id.sp_Yangdi02, "", "请选择县局", tmpList01, "选择县返回", YangDiQuery_Dialog.this.pCallback);
                            } else {
                                YangDiQuery_Dialog.this.m_SpinnerList02.setEnabled(false);
                            }
                        }
                    } else if (command.equals("选择县返回")) {
                        String tmpString2 = String.valueOf(object);
                        if (tmpString2.length() > 0) {
                            String tmpString22 = Common.GetViewValue(YangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi01);
                            List<String> tmpList012 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader2 = YangDiQuery_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct Xiang From T_YangDiInfo Where Sheng ='" + tmpString22 + "' And Xian='" + tmpString2 + "'");
                            while (tmpSQLiteReader2.Read()) {
                                tmpList012.add(tmpSQLiteReader2.GetString(0));
                            }
                            if (tmpList012.size() > 0) {
                                YangDiQuery_Dialog.this.m_SpinnerList03.setEnabled(true);
                                Common.SetSpinnerListData(YangDiQuery_Dialog.this._Dialog, (int) R.id.sp_Yangdi03, "", "请选择乡镇", tmpList012, "选择乡镇返回", YangDiQuery_Dialog.this.pCallback);
                            } else {
                                YangDiQuery_Dialog.this.m_SpinnerList03.setEnabled(false);
                            }
                        }
                    } else if (command.equals("选择乡镇返回")) {
                        String tmpString3 = String.valueOf(object);
                        if (tmpString3.length() > 0) {
                            String tmpString23 = Common.GetViewValue(YangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi01);
                            String tmpString32 = Common.GetViewValue(YangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi02);
                            List<String> tmpList013 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader3 = YangDiQuery_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct Cun From T_YangDiInfo Where Sheng ='" + tmpString23 + "' And Xian='" + tmpString32 + "' And Xiang='" + tmpString3 + "'");
                            while (tmpSQLiteReader3.Read()) {
                                tmpList013.add(tmpSQLiteReader3.GetString(0));
                            }
                            if (tmpList013.size() > 0) {
                                YangDiQuery_Dialog.this.m_SpinnerList04.setEnabled(true);
                                Common.SetSpinnerListData(YangDiQuery_Dialog.this._Dialog, (int) R.id.sp_Yangdi04, "", "请选择村", tmpList013, "选择村返回", YangDiQuery_Dialog.this.pCallback);
                            } else {
                                YangDiQuery_Dialog.this.m_SpinnerList04.setEnabled(true);
                            }
                        }
                    } else if (command.equals("选择村返回")) {
                        String tmpString4 = String.valueOf(object);
                        if (tmpString4.length() > 0) {
                            String tmpString24 = Common.GetViewValue(YangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi01);
                            String tmpString33 = Common.GetViewValue(YangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi02);
                            String tmpString42 = Common.GetViewValue(YangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi03);
                            List<String> tmpList014 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader4 = YangDiQuery_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct PartIndex From T_YangDiInfo Where Sheng ='" + tmpString24 + "' And Xian='" + tmpString33 + "' And Xiang='" + tmpString42 + "' And Cun='" + tmpString4 + "'");
                            while (tmpSQLiteReader4.Read()) {
                                tmpList014.add(tmpSQLiteReader4.GetString(0));
                            }
                            if (tmpList014.size() > 0) {
                                YangDiQuery_Dialog.this.m_SpinnerList05.setEnabled(true);
                                Common.SetSpinnerListData(YangDiQuery_Dialog.this._Dialog, (int) R.id.sp_Yangdi05, "", "请选择图斑", tmpList014, "选择图斑返回", YangDiQuery_Dialog.this.pCallback);
                            } else {
                                YangDiQuery_Dialog.this.m_SpinnerList05.setEnabled(true);
                            }
                        }
                    } else if (command.equals("选择图斑返回")) {
                        String tmpString5 = String.valueOf(object);
                        if (tmpString5.length() > 0) {
                            String tmpString25 = Common.GetViewValue(YangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi01);
                            String tmpString34 = Common.GetViewValue(YangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi02);
                            String tmpString43 = Common.GetViewValue(YangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi03);
                            String tmpString52 = Common.GetViewValue(YangDiQuery_Dialog.this._Dialog, R.id.sp_Yangdi04);
                            List<String> tmpList015 = new ArrayList<>();
                            SQLiteReader tmpSQLiteReader5 = YangDiQuery_Dialog.this.m_SQLiteDBHelper.Query("Select Distinct YangDiIndex From T_YangDiInfo Where Sheng ='" + tmpString25 + "' And Xian='" + tmpString34 + "' And Xiang='" + tmpString43 + "' And Cun='" + tmpString52 + "' And PartIndex='" + tmpString5 + "'");
                            while (tmpSQLiteReader5.Read()) {
                                tmpList015.add(tmpSQLiteReader5.GetString(0));
                            }
                            if (tmpList015.size() > 0) {
                                YangDiQuery_Dialog.this.m_SpinnerList06.setEnabled(true);
                                Common.SetSpinnerListData(YangDiQuery_Dialog.this._Dialog, (int) R.id.sp_Yangdi06, "", "请选择样地", tmpList015, (String) null, (ICallback) null);
                            } else {
                                YangDiQuery_Dialog.this.m_SpinnerList06.setEnabled(true);
                            }
                        }
                    } else if (command.equals("列表选项")) {
                        if (object != null && (object instanceof HashMap)) {
                            HashMap<String, Object> tmpHashMap = (HashMap) object;
                            if (tmpHashMap.containsKey("yangdiname")) {
                                final String tmpYangdiNameString = String.valueOf(tmpHashMap.get("yangdiname"));
                                final String tmpYangdiMsg = String.valueOf(tmpHashMap.get("msg"));
                                Common.ShowYesNoDialog(YangDiQuery_Dialog.this._Dialog.getContext(), "是否编辑该样地调查数据?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiQuery_Dialog.1.1
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String command2, Object pObject) {
                                        if (command2.equals("YES")) {
                                            YangDiQuery_Dialog.this.openYangDi2Dialog(tmpYangdiNameString, tmpYangdiMsg);
                                        }
                                    }
                                });
                            }
                        }
                    } else if (command.equals("QRResult")) {
                        String tempkey = object.toString();
                        int tmpI = tempkey.indexOf("ForestSystemShareData:");
                        if (tmpI >= 0) {
                            YangDiQuery_Dialog.this.getShareDataFromServer(tempkey.substring("ForestSystemShareData:".length() + tmpI));
                        } else {
                            Common.ShowDialog("您扫描的不是有效的样地数据共享识别二维码!");
                        }
                    }
                    if (!command.equals("导出")) {
                        return;
                    }
                    if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                        Common.ShowDialog("尊敬的用户：\r\n        【公共版】不能导出数据.为保证您能使用本软件的全部功能，请获取正式授权码！\r\n详见【关于系统】！");
                        return;
                    }
                    List<String> tmpList = new ArrayList<>();
                    for (HashMap<String, Object> tmpHash : YangDiQuery_Dialog.this.m_MyTableDataList) {
                        if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                            tmpList.add(String.valueOf(tmpHash.get("yangdiname")));
                        }
                    }
                    if (tmpList.size() == 0) {
                        Common.ShowDialog("请选择需要导出的样地数据.");
                        return;
                    }
                    String tempTotalFolder = String.valueOf(Common.GetAPPPath()) + "/森林督察/样地导出";
                    if (!Common.ExistFolder(tempTotalFolder)) {
                        new File(tempTotalFolder).mkdir();
                    }
                    String m_ExportFolder = String.valueOf(tempTotalFolder) + FileSelector_Dialog.sRoot + PubVar._PubCommand.m_ProjectDB.GetProjectManage().getProjectName();
                    if (!Common.ExistFolder(m_ExportFolder)) {
                        new File(m_ExportFolder).mkdir();
                    }
                    final String m_ExportFolder2 = String.valueOf(m_ExportFolder) + FileSelector_Dialog.sRoot + Common.fileDateFormat.format(new Date());
                    if (!Common.ExistFolder(m_ExportFolder2)) {
                        new File(m_ExportFolder2).mkdir();
                    }
                    new AlertDialog.Builder(PubVar.MainContext, 3).setTitle("选中导出格式").setItems(new String[]{"导出Excel文件", "导出Excel文件(带图片文件)", "Shapefile格式(Shp)"}, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiQuery_Dialog.1.2
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface arg0, int arg1) {
                            if (arg1 == 0) {
                                YangDiQuery_Dialog.this.exportSelectYangdi(m_ExportFolder2, false);
                            } else if (arg1 == 1) {
                                YangDiQuery_Dialog.this.exportSelectYangdi(m_ExportFolder2, true);
                            } else if (arg1 == 2) {
                                YangDiQuery_Dialog.this.exportSelectYangdiToShp(String.valueOf(m_ExportFolder2) + "/样地数据");
                            }
                            arg0.dismiss();
                        }
                    }).show();
                } catch (Exception e) {
                    Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
                }
            }
        };
        this.m_RevYangdiInfoString = "";
        this.m_RevYangdiDataString = "";
        this.m_RevYangdiNameList = new ArrayList();
        this.m_RevYangdiNameList2 = new ArrayList();
        this.myHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiQuery_Dialog.2
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    Common.ShowDialog(String.valueOf(msg.obj));
                } else if (msg.what != 1) {
                    int i = msg.what;
                } else if (msg.obj != null) {
                    String[] tmpStrs = (String[]) msg.obj;
                    if (tmpStrs == null || tmpStrs.length <= 1) {
                        Common.ShowDialog("未获取有效的共享数据.");
                        return;
                    }
                    YangDiQuery_Dialog.this.m_RevYangdiInfoString = tmpStrs[0];
                    YangDiQuery_Dialog.this.m_RevYangdiDataString = tmpStrs[1];
                    YangDiQuery_Dialog.this.m_RevYangdiNameList = new ArrayList();
                    YangDiQuery_Dialog.this.m_RevYangdiNameList2 = new ArrayList();
                    String[] tmpStrs01 = YangDiQuery_Dialog.this.m_RevYangdiInfoString.split("@");
                    if (tmpStrs01 != null && tmpStrs01.length > 0) {
                        for (String tmpStr01 : tmpStrs01) {
                            String[] tmpStrs02 = tmpStr01.split(",");
                            if (tmpStrs02 != null && tmpStrs02.length > 1) {
                                String tmpYDNameString = tmpStrs02[0];
                                String tmpYDName2 = tmpYDNameString.replace("_", " ");
                                String tmpYDIndexString = tmpStrs02[1];
                                if (tmpYDName2.endsWith(tmpYDIndexString)) {
                                    tmpYDName2 = tmpYDName2.substring(0, tmpYDName2.length() - tmpYDIndexString.length());
                                }
                                YangDiQuery_Dialog.this.m_RevYangdiNameList.add(tmpYDNameString);
                                YangDiQuery_Dialog.this.m_RevYangdiNameList2.add(String.valueOf(tmpYDName2) + " 样地号:" + tmpYDIndexString);
                            }
                        }
                    }
                    if (YangDiQuery_Dialog.this.m_RevYangdiNameList2.size() > 0) {
                        String[] tmpArray = (String[]) YangDiQuery_Dialog.this.m_RevYangdiNameList2.toArray(new String[YangDiQuery_Dialog.this.m_RevYangdiNameList2.size()]);
                        final boolean[] tmpbooleans = new boolean[YangDiQuery_Dialog.this.m_RevYangdiNameList2.size()];
                        int count = YangDiQuery_Dialog.this.m_RevYangdiNameList2.size();
                        for (int i2 = 0; i2 < count; i2++) {
                            tmpbooleans[i2] = true;
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(YangDiQuery_Dialog.this._Dialog.getContext(), 3);
                        builder.setTitle("请选择需要加载的样地数据：").setMultiChoiceItems(tmpArray, tmpbooleans, new DialogInterface.OnMultiChoiceClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiQuery_Dialog.2.1
                            @Override // android.content.DialogInterface.OnMultiChoiceClickListener
                            public void onClick(DialogInterface dialogInterface, int i3, boolean b) {
                                tmpbooleans[i3] = b;
                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiQuery_Dialog.2.2
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                                YangDiQuery_Dialog.this.loadYangdiData(tmpbooleans);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiQuery_Dialog.2.3
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.create().show();
                        return;
                    }
                    Common.ShowDialog("未获取有效的共享数据.");
                }
            }
        };
        this.progressDialog = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.yangdiquery_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("样地调查数据查询");
        this._Dialog.SetHeadButtons("1,2130837858,导出,导出", this.pCallback);
        this.m_SpinnerList01 = (SpinnerList) this._Dialog.findViewById(R.id.sp_Yangdi01);
        this.m_SpinnerList02 = (SpinnerList) this._Dialog.findViewById(R.id.sp_Yangdi02);
        this.m_SpinnerList02.setEnabled(false);
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
        this._Dialog.findViewById(R.id.btn_LoadYangdiQR).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_YangdiShare).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectAll).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonSelectDe).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_YangDiInMap).setOnClickListener(new ViewClick());
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_yangdi_layerlist), "自定义", "选择,样地名称,样地序号", "checkbox,text,text", new int[]{-15, -60, -25}, this.pCallback);
        this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void loadYangdiData(boolean[] selecteds) {
        try {
            final List<String> tmpSelNamesList = new ArrayList<>();
            final List<String> tmpHasNamesList = new ArrayList<>();
            StringBuilder tmpStringBuilder = new StringBuilder();
            int count = selecteds.length;
            for (int i = 0; i < count; i++) {
                if (selecteds[i]) {
                    String tmpName = this.m_RevYangdiNameList.get(i);
                    if (isContainYangDi(tmpName)) {
                        tmpHasNamesList.add(tmpName);
                        tmpStringBuilder.append(this.m_RevYangdiNameList2.get(i));
                        tmpStringBuilder.append("\r\n");
                    } else {
                        tmpSelNamesList.add(tmpName);
                    }
                }
            }
            if (tmpHasNamesList.size() > 0) {
                Common.ShowYesNoDialogWithAlert(this._Dialog.getContext(), "以下样地调查数据已经存在,是否覆盖?\r\n\r\n" + tmpStringBuilder.toString(), true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiQuery_Dialog.3
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command, Object pObject) {
                        if (command.equals("YES")) {
                            YangDiQuery_Dialog.this.deleteYangdiData(tmpHasNamesList);
                            List<String> tmpTotaList = new ArrayList<>();
                            tmpTotaList.addAll(tmpHasNamesList);
                            tmpTotaList.addAll(tmpSelNamesList);
                            YangDiQuery_Dialog.this.loadYangdiData2(tmpTotaList);
                        } else if (command.equals("NO")) {
                            YangDiQuery_Dialog.this.loadYangdiData2(tmpSelNamesList);
                        }
                    }
                });
            } else if (tmpSelNamesList.size() > 0) {
                loadYangdiData2(tmpSelNamesList);
            } else {
                Common.ShowDialog("没有勾选需要加载的数据.");
            }
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void loadYangdiData2(List<String> yangdiNameList) {
        String[] tmpStrs01;
        String[] tmpStrs020201;
        try {
            if (yangdiNameList.size() > 0 && (tmpStrs01 = this.m_RevYangdiInfoString.split("@")) != null && tmpStrs01.length > 0) {
                for (String tmpStr01 : tmpStrs01) {
                    String[] tmpDataStrings = tmpStr01.split(",");
//                    if (tmpDataStrings != null && tmpDataStrings.length > 1 && yangdiNameList.contains(tmpDataStrings[0])) {
//                        this.m_SQLiteDBHelper.ExecuteSQL(String.format("Replace Into T_YangDiInfo (YangDiName,YangDiIndex,Sheng,Xian,Xiang,Cun,PartIndex,X,Y) Values ('%1$s','%2$s','%3$s','%4$s','%5$s','%6$s','%7$s',%10$s,%11$s)", tmpDataStrings));
//                    }
                }
                String[] tmpStrs02 = this.m_RevYangdiDataString.split("@");
                if (tmpStrs02 != null && tmpStrs02.length > 0) {
                    for (String tmpStr012 : tmpStrs02) {
                        String[] tmpStrs0202 = tmpStr012.split("]");
                        if (tmpStrs0202 != null && tmpStrs0202.length > 1) {
                            String tmpYDNameString = tmpStrs0202[0];
                            if (yangdiNameList.contains(tmpYDNameString) && (tmpStrs020201 = tmpStrs0202[1].split(";")) != null && tmpStrs020201.length > 0) {
                                for (String tmpString03 : tmpStrs020201) {
//                                    if (tmpString03.length() > 0) {
//                                        this.m_SQLiteDBHelper.ExecuteSQL(String.format("Insert Into T_YangDiData (YangDiName,GenJing,Shan,Ma,Kuo) Values ('" + tmpYDNameString + "',%1$s,%2$s,%3$s,%4$s)", tmpString03.split(",")));
//                                    }
                                }
                            }
                        }
                    }
                }
                Common.ShowToast("加载样地调查数据完成!");
                refreshList();
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void getShareDataFromServer(final String shareKey) {
        if (this.progressDialog == null) {
            this.progressDialog = new LoadingDialog(this._Dialog.getContext());
            this.progressDialog.setTitle("正在建立共享数据识别码,请稍候...");
            this.progressDialog.setCancelable(false);
            this.progressDialog.setBindCallback(this.pCallback, "加载文件");
            this.progressDialog.showDialog();
        }
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiQuery_Dialog.4
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(String.valueOf(PubVar.ServerURL) + "YangDiHandler.ashx?method=getshare&deviceid=" + PubVar.m_AuthorizeTools.getUserAndroidID() + "&key=" + shareKey));
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                        if (jsonObject.getBoolean("success")) {
                            JSONObject jsonObject2 = new JSONObject(jsonObject.getString("msg"));
                            Message msg = YangDiQuery_Dialog.this.myHandler.obtainMessage();
                            msg.what = 1;
                            msg.obj = new String[]{String.valueOf(jsonObject2.get("yangdiInfo")), String.valueOf(jsonObject2.get("yangdidata"))};
                            YangDiQuery_Dialog.this.myHandler.sendMessage(msg);
                        } else {
                            String tmpMsg = jsonObject.getString("msg");
                            Message msg2 = YangDiQuery_Dialog.this.myHandler.obtainMessage();
                            msg2.what = 0;
                            msg2.obj = tmpMsg;
                            YangDiQuery_Dialog.this.myHandler.sendMessage(msg2);
                        }
                    }
                } catch (Exception e) {
                }
                if (YangDiQuery_Dialog.this.progressDialog != null) {
                    YangDiQuery_Dialog.this.progressDialog.dismiss();
                    YangDiQuery_Dialog.this.progressDialog = null;
                }
            }
        }.start();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshList() {
        boolean tmpBool01 = false;
        try {
            String tmpPath = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/SenLinDuCha.dbx";
            if (new File(tmpPath).exists()) {
                this.m_SQLiteDBHelper = new SQLiteDBHelper(tmpPath);
                List<String> tmpList01 = new ArrayList<>();
                SQLiteReader tmpSQLiteReader = this.m_SQLiteDBHelper.Query("Select Distinct Sheng From T_YangDiInfo");
                while (tmpSQLiteReader.Read()) {
                    tmpList01.add(tmpSQLiteReader.GetString(0));
                }
                if (tmpList01.size() > 0) {
                    tmpBool01 = true;
                    Common.SetSpinnerListData(this._Dialog, (int) R.id.sp_Yangdi01, "", "请选择省区", tmpList01, "选择省返回", this.pCallback);
                }
            }
            if (!tmpBool01) {
                Common.ShowDialog("当前还没有样地数据.");
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
    }

    private boolean isContainYangDi(String yangdiName) {
        try {
            if (this.m_SQLiteDBHelper == null) {
                return false;
            }
            SQLiteReader tmpSQLiteReader = this.m_SQLiteDBHelper.Query("Select COUNT(*) From T_YangDiData Where YangDiName='" + yangdiName + "'");
            if (!tmpSQLiteReader.Read() || tmpSQLiteReader.GetInt32(0) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
            return false;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean deleteYangdiData(List<String> yangdiNameList) {
        try {
            if (this.m_SQLiteDBHelper != null) {
                String tmpSql2 = Common.CombineStrings("','", yangdiNameList);
                this.m_SQLiteDBHelper.ExecuteSQL("Delete from T_YangDiInfo Where YangDiName IN ('" + tmpSql2 + "')");
                this.m_SQLiteDBHelper.ExecuteSQL("Delete from T_YangDiData Where YangDiName IN ('" + tmpSql2 + "')");
                return true;
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
        return false;
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
                String tmpString = Common.GetViewValue(this._Dialog, R.id.sp_Yangdi01).trim();
                if (tmpString.length() > 0) {
                    tmpTotalNameString = String.valueOf(tmpTotalNameString) + tmpString;
                    String tmpString2 = Common.GetViewValue(this._Dialog, R.id.sp_Yangdi02).trim();
                    if (tmpString2.length() > 0) {
                        tmpTotalNameString = String.valueOf(tmpTotalNameString) + "_" + tmpString2;
                        String tmpString3 = Common.GetViewValue(this._Dialog, R.id.sp_Yangdi03).trim();
                        if (tmpString3.length() > 0) {
                            tmpTotalNameString = String.valueOf(tmpTotalNameString) + "_" + tmpString3;
                            String tmpString4 = Common.GetViewValue(this._Dialog, R.id.sp_Yangdi04).trim();
                            if (tmpString4.length() > 0) {
                                tmpTotalNameString = String.valueOf(tmpTotalNameString) + "_" + tmpString4;
                                String tmpString5 = Common.GetViewValue(this._Dialog, R.id.sp_Yangdi05).trim();
                                if (tmpString5.length() > 0) {
                                    tmpTotalNameString = String.valueOf(tmpTotalNameString) + "_" + tmpString5;
                                    String tmpString6 = Common.GetViewValue(this._Dialog, R.id.sp_Yangdi06).trim();
                                    if (tmpString6.length() > 0) {
                                        tmpTotalNameString = String.valueOf(tmpTotalNameString) + "_" + tmpString6;
                                    }
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
                    Common.ShowYesNoDialogWithAlert(this._Dialog.getContext(), "是否删除选择的" + String.valueOf(tmpList.size()) + "个样地调查数据?\r\n提示:删除后将无法恢复,请谨慎操作.", true, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiQuery_Dialog.5
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (paramString.equals("YES")) {
                                for (int i = tmpList.size() - 1; i > -1; i--) {
                                    YangDiQuery_Dialog.this.m_MyTableDataList.remove(((Integer) tmpList2.get(i)).intValue());
                                }
                                YangDiQuery_Dialog.this.deleteYangdiData(tmpList);
                                YangDiQuery_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                            }
                        }
                    });
                } else {
                    Common.ShowDialog("没有选择需要删除的样地调查数据.");
                }
            } else if (command.equals("导入样地数据")) {
                Intent intent = new Intent();
                intent.setClass(this._Dialog.getContext(), MediaActivity.class);
                intent.putExtra("Type", 2);
                intent.putExtra("title", "扫描数据共享二维码");
                MediaActivity.BindCallbak = this.pCallback;
                this._Dialog.getContext().startActivity(intent);
            } else if (command.equals("共享样地数据")) {
                List<String> tmpList3 = new ArrayList<>();
                for (HashMap<String, Object> tmpHash5 : this.m_MyTableDataList) {
                    if (Boolean.parseBoolean(String.valueOf(tmpHash5.get("D1")))) {
                        tmpList3.add(String.valueOf(tmpHash5.get("yangdiname")));
                    }
                }
                if (tmpList3.size() > 0) {
                    YangDiShare_Dialog tmpDialog = new YangDiShare_Dialog();
                    tmpDialog.SetYangDiNameList(tmpList3);
                    tmpDialog.ShowDialog();
                    return;
                }
                Common.ShowDialog("没有选择需要共享的样地调查数据.");
            } else if (command.equals("样地地图展示")) {
                boolean tmpBool = true;
                List<Coordinate> tmpList4 = new ArrayList<>();
                ISymbol tmpISymbol = PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemPointSymbol("样式03");
                for (HashMap<String, Object> tmpHash6 : this.m_MyTableDataList) {
                    if (Boolean.parseBoolean(String.valueOf(tmpHash6.get("D1")))) {
                        double tmpD01 = Double.parseDouble(String.valueOf(tmpHash6.get("X")));
                        double tmpD02 = Double.parseDouble(String.valueOf(tmpHash6.get("Y")));
                        if (!(tmpD01 == 0.0d || tmpD02 == 0.0d)) {
                            tmpBool = false;
                            Coordinate tmpCoordinate = new Coordinate(tmpD01, tmpD02);
                            GraphicSymbolGeometry tmpGraphicSymbolGeo = new GraphicSymbolGeometry();
                            Point tmpGeo = new Point(tmpCoordinate.getX(), tmpCoordinate.getY());
                            tmpGeo.setLabelContent(String.valueOf(tmpHash6.get("D3")));
                            tmpGraphicSymbolGeo._Geoemtry = tmpGeo;
                            tmpGraphicSymbolGeo._Symbol = tmpISymbol;
                            tmpGraphicSymbolGeo._GeometryType = "样地对象";
                            HashMap<String, Object> tmpHashMap = new HashMap<>();
                            tmpHashMap.put("yangdiname", String.valueOf(tmpHash6.get("yangdiname")));
                            tmpGraphicSymbolGeo._AttributeHashMap = tmpHashMap;
                            PubVar._MapView._GraphicLayer.AddGeometry(tmpGraphicSymbolGeo);
                            tmpList4.add(tmpCoordinate);
                        }
                    }
                }
                if (tmpList4.size() > 0) {
                    if (tmpList4.size() == 1) {
                        PubVar._Map.ZoomToCenter(tmpList4.get(0));
                    } else {
                        Polyline tmpPolyline = new Polyline();
                        tmpPolyline.SetAllCoordinateList(tmpList4);
                        PubVar._Map.ZoomToExtend(tmpPolyline.CalEnvelope().Scale(1.2d));
                    }
                    Common.ShowToast("已加载样地数据至地图中.");
                }
                if (tmpBool) {
                    Common.ShowDialog("没有选择需要展示在地图中的样地.");
                }
            }
        } catch (Exception e) {
            Common.ShowDialog("错误:\r\n" + e.getLocalizedMessage());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void openYangDi2Dialog(String yangdiName, String yangdiMsg) {
        YangDi2_Dialog tmpDialog = new YangDi2_Dialog();
        tmpDialog.setYangDiName(yangdiName);
        tmpDialog.setYangDiInfo(yangdiMsg);
        tmpDialog.ShowDialog();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void exportSelectYangdi(String m_ExportFolder, boolean exportPhoto) {
        List<String> tmpList = new ArrayList<>();
        for (HashMap<String, Object> tmpHash : this.m_MyTableDataList) {
            if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                String tmpYDName = String.valueOf(tmpHash.get("yangdiname"));
                String tmpTuban = String.valueOf(tmpHash.get("D2")).trim();
                String tmpYangdIndexString = String.valueOf(tmpHash.get("D3"));
                tmpList.add(tmpYDName);
                exportYangdi(tmpTuban, tmpYDName, Integer.parseInt(tmpYangdIndexString), m_ExportFolder, exportPhoto);
            }
        }
        Common.ShowDialog("导出数据完成,文件导出至:\r\n\r\n" + m_ExportFolder);
    }

    private void exportYangdi(String tubanName, String yangdiName, int yangdiIndex, String saveFolder, boolean exportPhoto) {
        String[] tmpStrs02;
        WritableWorkbook wb;
        String tubanName2 = tubanName.replace(" ", "_");
        String tmpExcelPathString = String.valueOf(saveFolder) + FileSelector_Dialog.sRoot + tubanName2 + ".xls";
        SQLiteDBHelper tmpSqLiteDBHelper = CommonSetting.GetSenLinDuChaSQLiteDBHelper();
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
        if (CommonSetting.GetSenLinDuChaSQLiteDBHelper() != null) {
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
                tmpField03.SetFieldName("图斑号");
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

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.YangDiQuery_Dialog.6
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                YangDiQuery_Dialog.this.refreshList();
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
                YangDiQuery_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
