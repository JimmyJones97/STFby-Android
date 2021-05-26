package  com.xzy.forestSystem.gogisapi.Common;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Geodatabase.ExportCoordinateToTXT;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CoordList_Dialog {
    private static final DecimalFormat m_DoubleFormat = new DecimalFormat("0.###");
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private List<Coordinate> m_Coordinates;
    private MyTableFactory m_MyTableFactory;
    private HashMap<String, Object> m_SelectObject;
    private List<HashMap<String, Object>> m_TableDataList;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public CoordList_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_Coordinates = null;
        this.m_SelectObject = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Common.CoordList_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                HashMap<String, Object> tmpHash;
                try {
                    if (command.equals("单击选择行") && (tmpHash = (HashMap) object) != null) {
                        final double[] tmpCoord = {Double.parseDouble(String.valueOf(tmpHash.get("D2"))), Double.parseDouble(String.valueOf(tmpHash.get("D3"))), Double.parseDouble(String.valueOf(tmpHash.get("D4")))};
                        new AlertDialog.Builder(PubVar.MainContext, 3).setTitle("选择操作").setSingleChoiceItems(new String[]{"复制数据", "跳转至地图位置"}, -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.CoordList_Dialog.1.1
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface arg0, int arg1) {
                                if (arg1 == 0) {
                                    Common.CopyText(String.valueOf(String.valueOf(tmpCoord[0])) + "," + String.valueOf(tmpCoord[1]) + "," + String.valueOf(tmpCoord[2]));
                                    Common.ShowToast("复制数据成功.");
                                } else if (arg1 == 1) {
                                    PubVar._Map.ZoomToCenter(new Coordinate(tmpCoord[0], tmpCoord[1]));
                                    if (CoordList_Dialog.this.m_Callback != null) {
                                        CoordList_Dialog.this.m_Callback.OnClick("坐标列表返回_缩放至地图", null);
                                    }
                                    Common.ShowToast("地图已缩放至该节点.");
                                }
                                arg0.dismiss();
                            }
                        }).show();
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_TableDataList = new ArrayList();
        this.m_MyTableFactory = null;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.coordinatelist_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("坐标列表");
        this._Dialog.setCanceledOnTouchOutside(true);
        this.m_MyTableFactory = new MyTableFactory();
        this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.list_coordsList), "自定义", "序号,X坐标,Y坐标,Z坐标", "text,text,text,text", new int[]{-16, -30, -30, -24}, this.pCallback);
        ((Button) this._Dialog.findViewById(R.id.btn_ExportCoords)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.CoordList_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (CoordList_Dialog.this.m_Coordinates == null || CoordList_Dialog.this.m_Coordinates.size() <= 0) {
                    Common.ShowDialog("没有坐标点数据.");
                    return;
                }
                ExportCoordinateToTXT tmpExport = new ExportCoordinateToTXT();
                String tmpTimeStr = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String tempTotalFolder = String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/数据导出";
                if (!Common.ExistFolder(tempTotalFolder)) {
                    new File(tempTotalFolder).mkdir();
                }
                String tempTotalSaveFolder = String.valueOf(tempTotalFolder) + FileSelector_Dialog.sRoot + tmpTimeStr;
                if (!Common.ExistFolder(tempTotalSaveFolder)) {
                    new File(tempTotalSaveFolder).mkdir();
                }
                String tmpSaveFilePath = String.valueOf(tempTotalSaveFolder) + "/坐标点" + tmpTimeStr + ".txt";
                if (tmpExport.Export(tmpSaveFilePath, CoordList_Dialog.this.m_Coordinates)) {
                    Common.ShowDialog("导出坐标点数据成功!\r\n文件存放路径:\r\n" + tmpSaveFilePath);
                } else {
                    Common.ShowDialog("导出坐标点数据错误.");
                }
            }
        });
    }

    public void SetCoordinates(List<Coordinate> coordinates) {
        this.m_Coordinates = coordinates;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        try {
            if (this.m_Coordinates != null && this.m_Coordinates.size() > 0) {
                int tid = 1;
                for (Coordinate tmpCoord : this.m_Coordinates) {
                    HashMap<String, Object> tempHash = new HashMap<>();
                    tid++;
                    tempHash.put("D1", Integer.valueOf(tid));
                    tempHash.put("D2", m_DoubleFormat.format(tmpCoord.getX()));
                    tempHash.put("D3", m_DoubleFormat.format(tmpCoord.getY()));
                    tempHash.put("D4", m_DoubleFormat.format(tmpCoord.getZ()));
                    this.m_TableDataList.add(tempHash);
                }
                this.m_MyTableFactory.BindDataToListView(this.m_TableDataList, new String[]{"D1", "D2", "D3", "D4"}, this.pCallback);
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Common.CoordList_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                CoordList_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
