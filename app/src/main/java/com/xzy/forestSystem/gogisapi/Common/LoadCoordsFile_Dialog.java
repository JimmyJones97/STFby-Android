package  com.xzy.forestSystem.gogisapi.Common;

import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadCoordsFile_Dialog {
    private static Exception ex;
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private HashMap<String, String> m_ColumnHashMap;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private MyTableFactory m_MyTableFactory;
    private String m_TxtFilePath;
    private ICallback pCallback;
    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public LoadCoordsFile_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_TxtFilePath = "";
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Common.LoadCoordsFile_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                Coordinate tmpCoordinate;
                try {
                    if (command.equals("选择文件")) {
                        String[] tempPath2 = object.toString().split(";");
                        if (tempPath2 != null && tempPath2.length > 1) {
                            Common.SetEditTextValueOnID(LoadCoordsFile_Dialog.this._Dialog, R.id.et_Import_filepath, tempPath2[1]);
                        }
                    } else if (command.equals("确定")) {
                        if (LoadCoordsFile_Dialog.this.m_MyTableDataList.size() > 0) {
                            List<Coordinate> tmpCoordinates = new ArrayList<>();
                            String tmpXColumn = Common.GetSpinnerValueOnID(LoadCoordsFile_Dialog.this._Dialog, R.id.sp_column_selectX);
                            String tmpYColumn = Common.GetSpinnerValueOnID(LoadCoordsFile_Dialog.this._Dialog, R.id.sp_column_selectY);
                            String tmpZColumn = Common.GetSpinnerValueOnID(LoadCoordsFile_Dialog.this._Dialog, R.id.sp_column_selectZ);
                            boolean tmpWithZValue = true;
                            if (tmpZColumn.equals("无")) {
                                tmpWithZValue = false;
                            }
                            String tmpFID01 = (String) LoadCoordsFile_Dialog.this.m_ColumnHashMap.get(tmpXColumn);
                            String tmpFID02 = (String) LoadCoordsFile_Dialog.this.m_ColumnHashMap.get(tmpYColumn);
                            String tmpFID03 = "";
                            if (tmpWithZValue) {
                                tmpFID03 = (String) LoadCoordsFile_Dialog.this.m_ColumnHashMap.get(tmpZColumn);
                            }
                            if (!(tmpFID01 == null || tmpFID02 == null || tmpFID03 == null)) {
                                boolean tmpIsLatLng = Common.GetCheckBoxValueOnID(LoadCoordsFile_Dialog.this._Dialog, R.id.ck_IsCoordLngLat);
                                AbstractC0383CoordinateSystem pCoordSystem = PubVar.m_Workspace.GetCoorSystem();
                                for (HashMap<String, Object> tmpHashMap : LoadCoordsFile_Dialog.this.m_MyTableDataList) {
                                    if (Boolean.parseBoolean(String.valueOf(tmpHashMap.get("D1")))) {
                                        String tmpXString = String.valueOf(tmpHashMap.get(tmpFID01));
                                        String tmpYString = String.valueOf(tmpHashMap.get(tmpFID02));
                                        String tmpZString = "0";
                                        if (tmpWithZValue) {
                                            tmpZString = String.valueOf(tmpHashMap.get(tmpFID03));
                                        }
                                        if (tmpIsLatLng) {
                                            tmpXString = tmpXString.replace("°", FileSelector_Dialog.sFolder);
                                            tmpYString = tmpYString.replace("°", FileSelector_Dialog.sFolder);
                                            tmpZString = tmpZString.replace("°", FileSelector_Dialog.sFolder);
                                        }
                                        try {
                                            double tmpX = Double.parseDouble(tmpXString);
                                            double tmpY = Double.parseDouble(tmpYString);
                                            double tmpZ = Double.parseDouble(tmpZString);
                                            if (tmpIsLatLng) {
                                                tmpCoordinate = pCoordSystem.ConvertBLtoXYWithTranslate(new Coordinate(tmpX, tmpY), PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                                                try {
                                                    tmpCoordinate.setGeoX(tmpX);
                                                    tmpCoordinate.setGeoY(tmpY);
                                                    tmpCoordinate.setZ(tmpZ);
                                                } catch (Exception e) {
                                                }
                                            } else {
                                                tmpCoordinate = new Coordinate(tmpX, tmpY, tmpZ);
                                            }
                                            if (tmpCoordinate != null) {
                                                tmpCoordinates.add(tmpCoordinate);
                                            }
                                        } catch (Exception e2) {
                                        }
                                    }
                                }
                            }
                            if (tmpCoordinates.size() == 0) {
                                Common.ShowDialog("请先勾选需要导入的节点数据.");
                                return;
                            }
                            if (LoadCoordsFile_Dialog.this.m_Callback != null) {
                                LoadCoordsFile_Dialog.this.m_Callback.OnClick("导入节点数据返回", tmpCoordinates);
                            }
                            LoadCoordsFile_Dialog.this._Dialog.dismiss();
                            return;
                        }
                        Common.ShowDialog("没有加载任何节点数据,不能导入.");
                    }
                } catch (Exception e3) {
                }
            }
        };
        this.m_ColumnHashMap = null;
        this.m_MyTableDataList = new ArrayList();
        this.m_MyTableFactory = new MyTableFactory();
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.loadcoordsfile_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetCaption("导入节点数据");
        this._Dialog.SetHeadButtons("1,2130837858,导入,确定", this.pCallback);
        this._Dialog.findViewById(R.id.btn_Import_file).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.LoadCoordsFile_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                FileSelector_Dialog tempDialog = new FileSelector_Dialog(".csv;.txt;.data;", false);
                Common.ShowToast("请选择数据文件(.CSV,.TXT等).");
                tempDialog.SetTitle("选择文件");
                tempDialog.SetCallback(LoadCoordsFile_Dialog.this.pCallback);
                tempDialog.ShowDialog();
            }
        });
        this._Dialog.findViewById(R.id.btn_PreviewFile).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.LoadCoordsFile_Dialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                String tmpFileString = Common.GetEditTextValueOnID(LoadCoordsFile_Dialog.this._Dialog, R.id.et_Import_filepath);
                if (new File(tmpFileString).exists()) {
                    TxtPreview_Dialog tmpDialog = new TxtPreview_Dialog();
                    tmpDialog.setTextFilePath(tmpFileString);
                    tmpDialog.ShowDialog();
                    return;
                }
                Common.ShowDialog("数据文件不存在,请先选择有效的数据文件后再进行此操作.");
            }
        });
        this._Dialog.findViewById(R.id.btn_Import_LoadFile).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.LoadCoordsFile_Dialog.4
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                LoadCoordsFile_Dialog.this.loadFileData();
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void loadFileData() {
        List<String> tmpStrsList02;
        try {
            this.m_MyTableDataList.clear();
            String tmpFilePath = Common.GetEditTextValueOnID(this._Dialog, R.id.et_Import_filepath);
            if (!new File(tmpFilePath).exists()) {
                Common.ShowToast("文件:" + tmpFilePath + " 不存在.");
                return;
            }
            String tmpSplitChar = Common.GetEditTextValueOnID(this._Dialog, R.id.et_Import_splitChar);
            if (tmpSplitChar.equals("")) {
                Common.ShowToast("分隔符号不能为空.");
                return;
            }
            this.m_ColumnHashMap = new HashMap<>();
            String tmpFirstLineString = ReadTxtFileFirstContent(tmpFilePath);
            List<String> tmpColumnNamesList = null;
            if (Common.GetCheckBoxValueOnID(this._Dialog, R.id.ck_SkipFirst)) {
                tmpColumnNamesList = ProcessSCVLine(tmpFirstLineString, tmpSplitChar);
            } else {
                List<String> tmpStrsList = ProcessSCVLine(tmpFirstLineString, tmpSplitChar);
                if (tmpStrsList.size() > 0) {
                    int tmpTid01 = 0;
                    tmpColumnNamesList = new ArrayList<>();
                    int count = tmpStrsList.size();
                    for (int i = 0; i < count; i++) {
                        tmpTid01++;
                        tmpColumnNamesList.add("列" + String.valueOf(tmpTid01));
                    }
                }
            }
            if (tmpColumnNamesList == null || tmpColumnNamesList.size() <= 0) {
                Common.ShowToast("没有读取到有效的文件内容.");
                return;
            }
            StringBuilder tmpColumnsNameSB = new StringBuilder();
            StringBuilder tmpColumnsTypeSB = new StringBuilder();
            int tmpCellWidth = (int) (30.0f * PubVar.ScaledDensity);
            tmpColumnsNameSB.append("选择");
            tmpColumnsTypeSB.append("checkbox");
            int[] tmpColumnWidths = new int[(tmpColumnNamesList.size() + 1)];
            tmpColumnWidths[0] = tmpCellWidth;
            int tmpCellWidth2 = tmpCellWidth * 2;
            int tmpTid012 = -1;
            for (String tmpString01 : tmpColumnNamesList) {
                tmpTid012++;
                tmpColumnsNameSB.append("," + tmpString01);
                tmpColumnsTypeSB.append(",text");
                tmpColumnWidths[tmpTid012 + 1] = tmpCellWidth2;
                this.m_ColumnHashMap.put(tmpString01, "D" + String.valueOf(tmpTid012 + 2));
            }
            int tmpColumnCount = tmpTid012 + 1;
            this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_CoordsImport_datalist), "自定义", tmpColumnsNameSB.toString(), tmpColumnsTypeSB.toString(), tmpColumnWidths, null);
            this.m_MyTableFactory.BindDataToListView(this.m_MyTableDataList, null);
            Common.SetSpinnerListData(this._Dialog, "请选择X坐标数据所在列", tmpColumnNamesList, (int) R.id.sp_column_selectX);
            Common.SetValueToView(tmpColumnNamesList.get(0), this._Dialog.findViewById(R.id.sp_column_selectX));
            String tmpDefaultString = tmpColumnNamesList.get(0);
            if (tmpColumnNamesList.size() > 1) {
                tmpDefaultString = tmpColumnNamesList.get(1);
            }
            Common.SetSpinnerListData(this._Dialog, tmpDefaultString, "请选择Y坐标数据所在列", tmpColumnNamesList, R.id.sp_column_selectY, null);
            tmpColumnNamesList.add(0, "无");
            Common.SetSpinnerListData(this._Dialog, tmpColumnNamesList.get(0), "请选择Z坐标数据所在列", tmpColumnNamesList, R.id.sp_column_selectZ, null);
            try {
                FileInputStream inputStream = new FileInputStream(tmpFilePath);
                InputStreamReader inputStreamReader = null;
                try {
                    inputStreamReader = new InputStreamReader(inputStream, "gb2312");
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                BufferedReader reader = new BufferedReader(inputStreamReader);
                new ArrayList();
                int tmpColumnCount2 = tmpColumnCount - 1;
                try {
                    if (Common.GetCheckBoxValueOnID(this._Dialog, R.id.ck_SkipFirst)) {
                        reader.readLine();
                    }
                    while (true) {
                        String line = reader.readLine();
                        if (line == null) {
                            break;
                        } else if (!line.equals("") && (tmpStrsList02 = ProcessSCVLine(line, tmpSplitChar)) != null && tmpStrsList02.size() > tmpColumnCount2) {
                            HashMap tmpHash = new HashMap();
                            tmpHash.put("D1", true);
                            int tmpTid02 = 1;
                            for (String tmpString012 : tmpStrsList02) {
                                tmpTid02++;
                                tmpHash.put("D" + String.valueOf(tmpTid02), tmpString012);
                            }
                            this.m_MyTableDataList.add(tmpHash);
                        }
                    }
                } catch (IOException e) {
                }
                inputStream.close();
            } catch (Exception e2) {
            }
        } catch (Exception e3) {
        }
    }

    public static String ReadTxtFileFirstContent(String filepath) {
        try {
            FileInputStream inputStream = new FileInputStream(filepath);
            InputStreamReader inputStreamReader = null;
            try {
                inputStreamReader = new InputStreamReader(inputStream, "gbk");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuffer sb = new StringBuffer("");
            while (true) {
                try {
                    String line = reader.readLine();
                    if (line == null || sb.length() > 0) {
                        break;
                    }
                    sb.append(line);
                } catch (IOException e) {
                }
            }
            String result = sb.toString();
            inputStream.close();
            return result;
        } catch (Exception e2) {
            return null;
        }
    }

    public static List<String> ProcessSCVLine(String line, String splitChar) {
        List<String> result = null;
        try {
            String[] tmpStrs = line.split(splitChar);
            if (tmpStrs == null || tmpStrs.length <= 0) {
                return null;
            }
            List<String> result2 = new ArrayList<>();
            boolean isLine = false;
            try {
                StringBuilder tmpSB = new StringBuilder();
                for (String tmpStr : tmpStrs) {
                    if (tmpStr.startsWith("\"")) {
                        if (isLine) {
                            if (tmpSB.length() > 0) {
                                tmpSB.append(tmpStr.substring(0, tmpStr.length() - 1));
                                result2.add(tmpSB.toString());
                            }
                            isLine = false;
                            tmpSB = new StringBuilder();
                        } else {
                            isLine = true;
                            tmpSB = new StringBuilder();
                            tmpSB.append(tmpStr.substring(1));
                            tmpSB.append(splitChar);
                        }
                    } else if (tmpStr.endsWith("\"")) {
                        isLine = false;
                        tmpSB.append(tmpStr.substring(0, tmpStr.length() - 1));
                        result2.add(tmpSB.toString());
                        tmpSB = new StringBuilder();
                    } else if (isLine) {
                        tmpSB.append(tmpStr);
                        tmpSB.append(splitChar);
                    } else {
                        result2.add(tmpStr);
                    }
                }
                return result2;
            } catch (Exception e) {
                ex = e;
                result = result2;
                Common.Log("错误", ex.getMessage());
                return result;
            }
        } catch (Exception e2) {
            ex = e2;
            Common.Log("错误", ex.getMessage());
            return result;
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.LoadCoordsFile_Dialog.5
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
            }
        });
        this._Dialog.show();
    }
}
