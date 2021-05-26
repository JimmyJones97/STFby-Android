package  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha;

import android.content.DialogInterface;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
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

public class TubanExport_Dialog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    List<String> m_FieldValuesList01;
    List<String> m_FieldValuesList02;
    List<String> m_FieldValuesList03;
    List<String> m_FieldValuesList04;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public TubanExport_Dialog() {
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.TubanExport_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (!command.equals("导出")) {
                    return;
                }
                if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                    Common.ShowDialog("尊敬的用户：\r\n        【公共版】不能导出数据.为保证您能使用本软件的全部功能，请获取正式授权码！\r\n详见【关于系统】！");
                    return;
                }
                StringBuilder tmpStringBuilder = new StringBuilder();
                List<String> tmpList = new ArrayList<>();
                for (HashMap<String, Object> tmpHash : TubanExport_Dialog.this.m_MyTableDataList) {
                    if (Boolean.parseBoolean(String.valueOf(tmpHash.get("D1")))) {
                        tmpList.add(String.valueOf(tmpHash.get("D3")));
                        tmpStringBuilder.append(TubanExport_Dialog.this.exportLayer(String.valueOf(tmpHash.get("D3"))));
                        tmpStringBuilder.append("\r\n");
                    }
                }
                if (tmpList.size() == 0) {
                    Common.ShowDialog("请选择需要导出的图斑图层.");
                } else if (tmpStringBuilder.length() > 0) {
                    Common.ShowDialog(tmpStringBuilder.toString());
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.tubanexport_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("图斑导出");
        this._Dialog.SetHeadButtons("1,2130837858,导出,导出", this.pCallback);
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_senlinducha_layerlist), "自定义", "选择,督查图层名称", "checkbox,text", new int[]{-15, -85}, this.pCallback);
        this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
        this.m_FieldValuesList01 = new ArrayList();
        this.m_FieldValuesList01.add("林地");
        this.m_FieldValuesList01.add("非林地");
        this.m_FieldValuesList02 = new ArrayList();
        this.m_FieldValuesList02.add("建设项目使用林地(不包括直接为林业生产服务)");
        this.m_FieldValuesList02.add("直接为林业生产服务");
        this.m_FieldValuesList02.add("毁林(湿)开垦");
        this.m_FieldValuesList02.add("土地整理");
        this.m_FieldValuesList02.add("林木采伐");
        this.m_FieldValuesList02.add("造林、抚育及其他森林经营活动");
        this.m_FieldValuesList02.add("森林病虫害、森林火灾");
        this.m_FieldValuesList02.add("地质灾害等自然原因");
        this.m_FieldValuesList02.add("植物季节性变化");
        this.m_FieldValuesList02.add("遥感数据质量");
        this.m_FieldValuesList02.add("前地类是非林地变化");
        this.m_FieldValuesList02.add("其他原因");
        this.m_FieldValuesList03 = new ArrayList();
        this.m_FieldValuesList03.add("县级");
        this.m_FieldValuesList03.add("市级");
        this.m_FieldValuesList03.add("省级");
        this.m_FieldValuesList03.add("直属院");
        this.m_FieldValuesList03.add("专员办");
        this.m_FieldValuesList04 = new ArrayList();
        this.m_FieldValuesList04.add("一致");
        this.m_FieldValuesList04.add("不一致");
        this.m_FieldValuesList04.add("县级检查");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void initialLayers() {
        try {
            List<String> tmpLyrList = CommonSetting.getCheckLayersList();
            if (tmpLyrList == null || tmpLyrList.size() <= 0) {
                Common.ShowDialog("项目中没有可导出的督查图斑图层,请在“设置”中先添加督查图层.");
                return;
            }
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
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private String exportLayer(String layerID) {
        FeatureLayer tmpFeatureLayer;
        String[] tmpStrs02;
        int tmpJ;
        StringBuilder tmpMsgBuilder = new StringBuilder();
        try {
            GeoLayer tmpGeoLayer = PubVar._Map.GetGeoLayerByID(layerID);
            if (!(tmpGeoLayer == null || (tmpFeatureLayer = PubVar._PubCommand.m_ProjectDB.getFeatureLayerByID(tmpGeoLayer.getLayerID())) == null)) {
                String tempTotalFolder = String.valueOf(Common.GetAPPPath()) + "/森林督察/图斑导出";
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
                String tmpExcelPathString = String.valueOf(m_ExportFolder2) + "/全国森林督查数据录入格式表_" + tmpGeoLayer.getLayerName() + ".xls";
                String tempFieldsName = "";
                try {
                    WritableWorkbook wb = Workbook.createWorkbook(new File(tmpExcelPathString), Workbook.getWorkbook(new File(String.valueOf(Common.GetAPPPath()) + "/SysFile/sldcexporttable.xls")));
                    WritableSheet wSheet = wb.getSheet("图斑检测验证");
                    int tmpFCount = 0;
                    int tmpF01I = -1;
                    int tmpF01I02 = -1;
                    int tmpF02I = -1;
                    int tmpF03I = -1;
                    int tmpF04I = -1;
                    List<Integer> tmpInts = new ArrayList<>();
                    StringBuilder tmpStringBuilder = new StringBuilder();
                    HashMap<String, String> tmphasHashMap = CommonSetting.m_DuChaLayerMustFieldsName;
                    int tmpTid = 0;
                    for (String tmpString : CommonSetting.m_DuChaLayerMustFieldsNameList) {
                        String tmpFIDNameString = tmpFeatureLayer.GetDataFieldByFieldName(tmphasHashMap.get(tmpString));
                        if (tmpFIDNameString.length() > 0) {
                            if (tmpStringBuilder.length() > 0) {
                                tmpStringBuilder.append(",");
                            }
                            tmpStringBuilder.append(tmpFIDNameString);
                            if (tmpString.equals("前地类")) {
                                tmpF01I = tmpFCount;
                            } else if (tmpString.equals("现地类")) {
                                tmpF01I02 = tmpFCount;
                            } else if (tmpString.equals("图斑变化原因")) {
                                tmpF02I = tmpFCount;
                            } else if (tmpString.equals("检查级别")) {
                                tmpF03I = tmpFCount;
                            } else if (tmpString.equals("检查结果是否一致")) {
                                tmpF04I = tmpFCount;
                            }
                            tmpFCount++;
                            tmpInts.add(Integer.valueOf(tmpTid));
                        }
                        tmpTid++;
                    }
                    tempFieldsName = String.valueOf(tmphasHashMap.get("省")) + "," + tmphasHashMap.get("县") + "," + tmphasHashMap.get("乡镇") + "," + tmphasHashMap.get("村") + "," + tmphasHashMap.get("图斑号");
                    SQLiteReader tmpSQLiteReader = tmpGeoLayer.getDataset().getDataSource().Query("Select " + tmpStringBuilder.toString() + " From " + tmpGeoLayer.getDataset().getDataTableName());
                    int tmpRow = -1;
                    while (tmpSQLiteReader.Read()) {
                        tmpRow++;
                        wSheet.addCell(new Label(0, 6 + tmpRow, String.valueOf(tmpRow + 1)));
                        for (int i = 0; i < tmpFCount; i++) {
                            String tmpValueString = tmpSQLiteReader.GetString(i);
                            if (i == tmpF01I || i == tmpF01I02) {
                                int tmpJ2 = this.m_FieldValuesList01.indexOf(tmpValueString);
                                if (tmpJ2 >= 0) {
                                    tmpValueString = String.valueOf(tmpJ2 + 1);
                                }
                            } else if (i == tmpF02I) {
                                int tmpJ3 = this.m_FieldValuesList02.indexOf(tmpValueString);
                                if (tmpJ3 >= 0) {
                                    tmpValueString = String.valueOf(tmpJ3 + 1);
                                }
                            } else if (i == tmpF03I) {
                                int tmpJ4 = this.m_FieldValuesList03.indexOf(tmpValueString);
                                if (tmpJ4 >= 0) {
                                    tmpValueString = String.valueOf(tmpJ4 + 1);
                                }
                            } else if (i == tmpF04I && (tmpJ = this.m_FieldValuesList04.indexOf(tmpValueString)) >= 0) {
                                tmpValueString = String.valueOf(tmpJ + 1);
                            }
                            wSheet.addCell(new Label(tmpInts.get(i).intValue() + 1, 6 + tmpRow, tmpValueString));
                        }
                    }
                    wb.write();
                    wb.close();
                    tmpMsgBuilder.append("图层【" + tmpGeoLayer.getLayerName() + "】数据导出成功,保存至:\r\n" + tmpExcelPathString);
                } catch (Exception e) {
                }
                if (Common.GetCheckBoxValueOnID(this._Dialog, R.id.ck_exportWithPhoto)) {
                    String tmpOrgPhotoFolder = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/Photo/";
                    String[] tmpStrs01 = tempFieldsName.split(",");
                    StringBuilder tmpSB = new StringBuilder();
                    int length = tmpStrs01.length;
                    for (int i2 = 0; i2 < length; i2++) {
                        String tmpStr01 = tmpStrs01[i2];
                        if (tmpStr01.length() > 0) {
                            String tmpFIDName = tmpFeatureLayer.GetDataFieldByFieldName(tmpStr01);
                            if (tmpFIDName.length() > 0) {
                                if (tmpSB.length() > 0) {
                                    tmpSB.append("||''||");
                                }
                                tmpSB.append(tmpFIDName);
                            }
                        }
                    }
                    SQLiteReader tmpSQLiteReader2 = tmpGeoLayer.getDataset().getDataSource().Query("Select " + tmpSB.toString() + " AS A, SYS_PHOTO From " + tmpGeoLayer.getDataset().getDataTableName() + " Where length(SYS_PHOTO)>0 ");
                    while (tmpSQLiteReader2.Read()) {
                        String tmpPhotoInfoString = tmpSQLiteReader2.GetString(1);
                        String tmpFileName = tmpSQLiteReader2.GetString(0);
                        if (tmpPhotoInfoString != null && tmpPhotoInfoString.length() > 0 && (tmpStrs02 = tmpPhotoInfoString.split(",")) != null && tmpStrs02.length > 0) {
                            int tmpTid2 = 1;
                            int length2 = tmpStrs02.length;
                            for (int i3 = 0; i3 < length2; i3++) {
                                String tmpStr03 = tmpStrs02[i3];
                                if (tmpStr03.length() > 0) {
                                    String tmpFilePath01 = String.valueOf(tmpOrgPhotoFolder) + tmpStr03;
                                    if (new File(tmpFilePath01).exists()) {
                                        Common.CopyFile(tmpFilePath01, String.valueOf(m_ExportFolder2) + FileSelector_Dialog.sRoot + tmpFileName + "" + String.valueOf(tmpTid2) + ".jpg");
                                        tmpTid2++;
                                    }
                                }
                            }
                        }
                    }
                    tmpMsgBuilder.append("\r\n导出图层【" + tmpGeoLayer.getLayerName() + "】的图片成功,共导出" + String.valueOf(0) + "个文件.\r\n图片导出至文件夹:" + m_ExportFolder2);
                }
            }
        } catch (Exception e2) {
        }
        return tmpMsgBuilder.toString();
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Module.SenLinDuCha.TubanExport_Dialog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                TubanExport_Dialog.this.initialLayers();
            }
        });
        this._Dialog.show();
    }
}
