package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import androidx.core.view.ViewCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.BaseTileInfo;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.ELayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.RasterLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.WebMapTilesDownloader;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WGS1984;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WebMercator;
import  com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import com.xzy.forestSystem.gogisapi.MyControls.SpinnerList;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.Workspace.RasterLayerWorkspace;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RasterLayers_Select_Dialog {
    private int FormType;
    private LinearLayout LinearLayoutInputExtent;
    private SpinnerList Spinner01;
    private SpinnerList Spinner02;
    private XDialogTemplate _Dialog;
    private int cacheCurrentCount;
    private WebMapTilesDownloader cacheDownloader;
    private int cacheERRORCount;
    private List<BaseTileInfo> cacheTilesArray;
    private int cacheTotalCount;
    private CustomeProgressDialog downloadProgressDialog;
    private ICallback m_Callback;
    private List<RasterLayer> m_LayerList;
    private Handler m_NetHandler;
    private RadioGroup mapTypeRadioGroup;
    Handler myDownloadHandler;
    private ICallback pCallback;
    private EditText xmaxETxt;
    private EditText xminETxt;
    private EditText ymaxETxt;
    private EditText yminETxt;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public RasterLayers_Select_Dialog() {
        Coordinate tempCoord01;
        Coordinate tempCoord02;
        Coordinate tempCoord03;
        Coordinate tempCoord04;
        this._Dialog = null;
        this.m_LayerList = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Select_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (paramString.equals("确定")) {
                    RadioButton tmpRadioButton = (RadioButton) RasterLayers_Select_Dialog.this._Dialog.findViewById(RasterLayers_Select_Dialog.this.mapTypeRadioGroup.getCheckedRadioButtonId());
                    String tmpType = tmpRadioButton.getText().toString();
                    boolean tempBool = false;
                    if (RasterLayers_Select_Dialog.this.m_LayerList != null && RasterLayers_Select_Dialog.this.m_LayerList.size() > 0) {
                        Iterator<RasterLayer> tempIter = RasterLayers_Select_Dialog.this.m_LayerList.iterator();
                        while (true) {
                            if (!tempIter.hasNext()) {
                                break;
                            }
                            RasterLayer tempLayer = tempIter.next();
                            if (tempLayer != null && tempLayer.GetLayerName().equals(tmpType)) {
                                tempBool = true;
                                break;
                            }
                        }
                    }
                    if (tempBool) {
                        Common.ShowDialog("地图:\r\n【" + tmpType + "】\r\n已经存在,不能重复添加.");
                        return;
                    }
                    Object tmpObject = tmpRadioButton.getTag();
                    if (tmpObject != null) {
                        HashMap<String, String> tmpHashMap = (HashMap) tmpObject;
                        String tmpLyrNameString = String.valueOf(tmpHashMap.get("MapName"));
                        RasterLayer tempLayer2 = new RasterLayer();
                        tempLayer2.SetLayerName(tmpLyrNameString);
                        tempLayer2.SetLayerTypeName("网络地图");
                        double tmpBiasX = Double.parseDouble(String.valueOf(tmpHashMap.get("BiasX")));
                        double tmpBiasY = Double.parseDouble(String.valueOf(tmpHashMap.get("BiasY")));
                        tempLayer2._OffsetX = tmpBiasX;
                        tempLayer2._OffsetY = tmpBiasY;
                        String tmpCoodSysNameString = String.valueOf(tmpHashMap.get("CoordSysName"));
                        if (tmpCoodSysNameString.length() == 0) {
                            tmpCoodSysNameString = "WebMercator";
                        }
                        tempLayer2.SetCoorSystem(tmpCoodSysNameString);
                        tempLayer2.SetFilePath(String.valueOf(tmpHashMap.get("MapServer")));
                        tempLayer2.setLayerID(tmpLyrNameString);
                        tempLayer2.MapConfigString = String.valueOf(tmpHashMap.get("MapConfig"));
                        tempLayer2.StartLevel = Integer.parseInt(String.valueOf(tmpHashMap.get("StartLevel")));
                        tempLayer2.SetEditMode(EEditMode.NEW);
                        if (RasterLayers_Select_Dialog.this.m_Callback != null) {
                            RasterLayers_Select_Dialog.this.m_Callback.OnClick("添加网络地图", tempLayer2);
                        }
                        RasterLayers_Select_Dialog.this._Dialog.dismiss();
                        return;
                    }
                    RasterLayer tempLayer3 = new RasterLayer();
                    tempLayer3.SetLayerTypeName(tmpType);
                    tempLayer3.SetLayerName(tmpType);
                    tempLayer3.SetEditMode(EEditMode.NEW);
                    AbstractC0383CoordinateSystem tmpCooSystem = PubVar.m_Workspace.GetCoorSystem();
                    if (tmpCooSystem.getIsProjectionCoord() && !(tmpCooSystem instanceof Coordinate_WebMercator)) {
                        tempLayer3.setIsConsiderTranslate(true);
                    }
                    if (RasterLayers_Select_Dialog.this.m_Callback != null) {
                        RasterLayers_Select_Dialog.this.m_Callback.OnClick("添加网络地图", tempLayer3);
                    }
                    RasterLayers_Select_Dialog.this._Dialog.dismiss();
                }
            }
        };
        this.LinearLayoutInputExtent = null;
        this.cacheTilesArray = null;
        this.cacheTotalCount = 0;
        this.cacheCurrentCount = 0;
        this.cacheERRORCount = 0;
        this.downloadProgressDialog = null;
        this.cacheDownloader = null;
        this.myDownloadHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Select_Dialog.2
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                try {
                    if (msg.what != 100) {
                        RasterLayers_Select_Dialog.this.cacheCurrentCount++;
                        if (msg.what == 2) {
                            RasterLayers_Select_Dialog.this.cacheERRORCount++;
                        }
                        if (RasterLayers_Select_Dialog.this.downloadProgressDialog != null) {
                            Message tempMsg = RasterLayers_Select_Dialog.this.downloadProgressDialog.myHandler.obtainMessage();
                            tempMsg.what = 5;
                            tempMsg.obj = new Object[]{"下载进度 [" + RasterLayers_Select_Dialog.this.cacheCurrentCount + FileSelector_Dialog.sRoot + RasterLayers_Select_Dialog.this.cacheTotalCount + "]", Integer.valueOf((RasterLayers_Select_Dialog.this.cacheCurrentCount * 100) / RasterLayers_Select_Dialog.this.cacheTotalCount)};
                            RasterLayers_Select_Dialog.this.downloadProgressDialog.myHandler.sendMessage(tempMsg);
                            if (RasterLayers_Select_Dialog.this.cacheCurrentCount == RasterLayers_Select_Dialog.this.cacheTotalCount) {
                                RasterLayers_Select_Dialog.this.downloadProgressDialog.dismiss();
                            }
                        }
                    } else if (RasterLayers_Select_Dialog.this.downloadProgressDialog != null && RasterLayers_Select_Dialog.this.downloadProgressDialog.isCancel) {
                        if (RasterLayers_Select_Dialog.this.cacheDownloader != null) {
                            RasterLayers_Select_Dialog.this.cacheDownloader.StopDownload();
                        }
                        RasterLayers_Select_Dialog.this.downloadProgressDialog.dismiss();
                    }
                } catch (Exception ex) {
                    Common.Log("错误", ex.getMessage());
                }
            }
        };
        this.FormType = 0;
        this.m_NetHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Select_Dialog.3
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                JSONArray jsonObject2;
                if (msg.what == 0) {
                    if (msg.obj != null) {
                        Common.ShowToast(String.valueOf(msg.obj));
                    }
                } else if (msg.what == 1) {
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(msg.obj));
                        if (!(jsonObject == null || (jsonObject2 = jsonObject.getJSONArray("maplist")) == null)) {
                            int count = jsonObject2.length();
                            for (int i = 0; i < count; i++) {
                                JSONObject tmpJsonObject = (JSONObject) jsonObject2.get(i);
                                String tmpNameString = tmpJsonObject.getString("MapName");
                                String tmpURLString = tmpJsonObject.getString("MapServer");
                                String tmpCoord = tmpJsonObject.getString("CoordSysName");
                                String tmpCoordConfig = tmpJsonObject.getString("CoordSysConfig");
                                String tmpBaisX = tmpJsonObject.getString("BiasX");
                                String tmpBaisY = tmpJsonObject.getString("BiasY");
                                String tmpLevelStart = tmpJsonObject.getString("StartLevel");
                                String tmpmapConfig = tmpJsonObject.getString("MapConfig");
                                HashMap<String, String> tmphasHashMap = new HashMap<>();
                                tmphasHashMap.put("MapName", tmpNameString);
                                tmphasHashMap.put("MapServer", tmpURLString);
                                tmphasHashMap.put("CoordSysName", tmpCoord);
                                tmphasHashMap.put("CoordSysConfig", tmpCoordConfig);
                                tmphasHashMap.put("BiasX", tmpBaisX);
                                tmphasHashMap.put("BiasY", tmpBaisY);
                                tmphasHashMap.put("StartLevel", tmpLevelStart);
                                tmphasHashMap.put("MapConfig", tmpmapConfig);
                                RadioButton tmpRadioButton = new RadioButton(RasterLayers_Select_Dialog.this._Dialog.getContext());
                                tmpRadioButton.setText(tmpNameString);
                                tmpRadioButton.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                                tmpRadioButton.setTag(tmphasHashMap);
                                RasterLayers_Select_Dialog.this.mapTypeRadioGroup.addView(tmpRadioButton);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.rasterlayers_select_dialog);
        this._Dialog.Resize(0.96f, PubVar.DialogHeightRatio);
        this._Dialog.SetCaption("在线地图");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.mapTypeRadioGroup = (RadioGroup) this._Dialog.findViewById(R.id.rg_mapType);
        this.LinearLayoutInputExtent = (LinearLayout) this._Dialog.findViewById(R.id.LinearLayoutInputExtent);
        this.LinearLayoutInputExtent.setVisibility(8);
        this.Spinner01 = (SpinnerList) this._Dialog.findViewById(R.id.Spinner01);
        this.Spinner02 = (SpinnerList) this._Dialog.findViewById(R.id.Spinner02);
        List<String> tmpClassList = new ArrayList<>();
        for (int i = 22; i > 0; i--) {
            tmpClassList.add(String.valueOf(i));
        }
        Common.SetSpinnerListData(this._Dialog.getContext(), this.Spinner02, "10", "请选择缓存级数:", tmpClassList, "", (ICallback) null);
        this.xmaxETxt = (EditText) this._Dialog.findViewById(R.id.editText3);
        this.xminETxt = (EditText) this._Dialog.findViewById(R.id.editText1);
        this.ymaxETxt = (EditText) this._Dialog.findViewById(R.id.editText4);
        this.yminETxt = (EditText) this._Dialog.findViewById(R.id.editText2);
        if (PubVar._Map != null) {
            tempCoord01 = PubVar._Map.getExtend().getLeftTop();
            tempCoord02 = PubVar._Map.getExtend().getRightBottom();
            try {
                Iterator<XLayer> tmpIter = PubVar._Map.getRasterLayers().iterator();
                while (true) {
                    if (!tmpIter.hasNext()) {
                        break;
                    }
                    XLayer tmpLayer = tmpIter.next();
                    if (tmpLayer.getLayerType() == ELayerType.ONLINEMAP) {
                        Common.SetSpinnerListValue(String.valueOf(((XBaseTilesLayer) tmpLayer).GetCurrentLevel(PubVar._Map.ToMapDistance((double) (PubVar.ScaledDensity / 2.0f)))), this._Dialog.findViewById(R.id.Spinner02));
                        break;
                    }
                }
            } catch (Exception ex) {
                Common.Log("错误", ex.getMessage());
            }
        } else {
            tempCoord01 = new Coordinate(7850000.0d, 5650000.0d);
            tempCoord02 = new Coordinate(1.48E7d, 1570000.0d);
        }
        Coordinate_WGS1984 tmpCoordinate_WGS1984 = new Coordinate_WGS1984();
        if (PubVar.m_Workspace != null) {
            tempCoord03 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tempCoord01.getX(), tempCoord01.getY(), 0.0d, tmpCoordinate_WGS1984);
            tempCoord04 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tempCoord02.getX(), tempCoord02.getY(), 0.0d, tmpCoordinate_WGS1984);
            tempCoord03.setGeoX(tempCoord03.getGeoX());
            tempCoord03.setGeoY(tempCoord03.getGeoY());
            tempCoord04.setGeoX(tempCoord04.getGeoX());
            tempCoord04.setGeoY(tempCoord04.getGeoY());
        } else {
            tempCoord03 = Coordinate_WGS1984.XYToBL(tempCoord01.getX(), tempCoord01.getY());
            tempCoord04 = Coordinate_WGS1984.XYToBL(tempCoord02.getX(), tempCoord02.getY());
            tempCoord03.setGeoX(tempCoord03.getGeoX());
            tempCoord03.setGeoY(tempCoord03.getGeoY());
            tempCoord04.setGeoX(tempCoord04.getGeoX());
            tempCoord04.setGeoY(tempCoord04.getGeoY());
        }
        this.xmaxETxt.setText(String.valueOf(tempCoord04.getGeoX()));
        this.xminETxt.setText(String.valueOf(tempCoord03.getGeoX()));
        this.ymaxETxt.setText(String.valueOf(tempCoord03.getGeoY()));
        this.yminETxt.setText(String.valueOf(tempCoord04.getGeoY()));
        this._Dialog.findViewById(R.id.button_Start_Cache).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Select_Dialog.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String tempMsg;
                try {
                    int tempLevel = Integer.parseInt(Common.GetSpinnerValueOnID(RasterLayers_Select_Dialog.this._Dialog, R.id.Spinner02));
                    double xmax = Double.parseDouble(RasterLayers_Select_Dialog.this.xmaxETxt.getText().toString());
                    double xmin = Double.parseDouble(RasterLayers_Select_Dialog.this.xminETxt.getText().toString());
                    double ymax = Double.parseDouble(RasterLayers_Select_Dialog.this.ymaxETxt.getText().toString());
                    double ymin = Double.parseDouble(RasterLayers_Select_Dialog.this.yminETxt.getText().toString());
                    if (xmax > 180.0d) {
                        xmax = 180.0d;
                    }
                    if (xmax < -180.0d) {
                        xmax = -180.0d;
                    }
                    if (xmin > 180.0d) {
                        xmin = 180.0d;
                    }
                    if (xmin < -180.0d) {
                        xmin = -180.0d;
                    }
                    if (ymax > 90.0d) {
                        ymax = 90.0d;
                    }
                    if (ymax < -90.0d) {
                        ymax = -90.0d;
                    }
                    if (ymin > 90.0d) {
                        ymin = 90.0d;
                    }
                    if (ymin < -90.0d) {
                        ymin = -90.0d;
                    }
                    BasicValue localParam1 = new BasicValue();
                    BasicValue localParam2 = new BasicValue();
                    XBaseTilesLayer.GetTileXY(xmin, ymax, tempLevel, localParam1, localParam2);
                    BasicValue localParam3 = new BasicValue();
                    BasicValue localParam4 = new BasicValue();
                    XBaseTilesLayer.GetTileXY(xmax, ymin, tempLevel, localParam3, localParam4);
                    int minX = localParam1.getInt();
                    int maxX = localParam3.getInt();
                    int maxY = localParam2.getInt();
                    int minY = localParam4.getInt();
                    if (minX < 0) {
                        minX = 0;
                    }
                    if (maxX < 0) {
                        maxX = 0;
                    }
                    if (minY < 0) {
                        minY = 0;
                    }
                    if (maxY < 0) {
                        maxY = 0;
                    }
                    double tempTotalFileSize = (double) (10 * ((long) ((Math.abs(maxX - minX) + 1) * (Math.abs(maxY - minY) + 1))));
                    if (tempTotalFileSize <= 0.0d || tempTotalFileSize >= 1.048576E7d) {
                        tempMsg = "缓存数据所需存储量远远超过设备存储容量.\r\n是否继续缓存?";
                    } else {
                        tempMsg = "共需要缓存数据:\r\n" + Common.SimplifyFileSize(tempTotalFileSize) + "\r\n是否继续缓存?";
                    }
                    Common.ShowYesNoDialog(view.getContext(), tempMsg, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Select_Dialog.4.1
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            try {
                                if (paramString.equals("YES")) {
                                    RasterLayer tempLayer = new RasterLayer();
                                    tempLayer.SetLayerTypeName(((RadioButton) RasterLayers_Select_Dialog.this._Dialog.findViewById(RasterLayers_Select_Dialog.this.mapTypeRadioGroup.getCheckedRadioButtonId())).getText().toString());
                                    XLayer tempXLayer = RasterLayerWorkspace.CreateRenderLayer(tempLayer);
                                    if (tempXLayer != null) {
                                        XBaseTilesLayer tempTilesLayer = (XBaseTilesLayer) tempXLayer;
                                        int tempLevel2 = Integer.parseInt(Common.GetSpinnerValueOnID(RasterLayers_Select_Dialog.this._Dialog, R.id.Spinner02));
                                        double xmax2 = Double.parseDouble(RasterLayers_Select_Dialog.this.xmaxETxt.getText().toString());
                                        double xmin2 = Double.parseDouble(RasterLayers_Select_Dialog.this.xminETxt.getText().toString());
                                        double ymax2 = Double.parseDouble(RasterLayers_Select_Dialog.this.ymaxETxt.getText().toString());
                                        double ymin2 = Double.parseDouble(RasterLayers_Select_Dialog.this.yminETxt.getText().toString());
                                        if (xmax2 > 180.0d) {
                                            xmax2 = 180.0d;
                                        }
                                        if (xmax2 < -180.0d) {
                                            xmax2 = -180.0d;
                                        }
                                        if (xmin2 > 180.0d) {
                                            xmin2 = 180.0d;
                                        }
                                        if (xmin2 < -180.0d) {
                                            xmin2 = -180.0d;
                                        }
                                        if (ymax2 > 90.0d) {
                                            ymax2 = 90.0d;
                                        }
                                        if (ymax2 < -90.0d) {
                                            ymax2 = -90.0d;
                                        }
                                        if (ymin2 > 90.0d) {
                                            ymin2 = 90.0d;
                                        }
                                        if (ymin2 < -90.0d) {
                                            ymin2 = -90.0d;
                                        }
                                        BasicValue localParam12 = new BasicValue();
                                        BasicValue localParam22 = new BasicValue();
                                        BasicValue localParam32 = new BasicValue();
                                        BasicValue localParam42 = new BasicValue();
                                        XBaseTilesLayer.GetTileXY(xmin2, ymax2, tempLevel2, localParam12, localParam22);
                                        XBaseTilesLayer.GetTileXY(xmax2, ymin2, tempLevel2, localParam32, localParam42);
                                        int minX2 = localParam12.getInt();
                                        int maxX2 = localParam32.getInt();
                                        int maxY2 = localParam22.getInt();
                                        int minY2 = localParam42.getInt();
                                        if (minX2 < 0) {
                                            minX2 = 0;
                                        }
                                        if (maxX2 < 0) {
                                            maxX2 = 0;
                                        }
                                        if (minY2 < 0) {
                                            minY2 = 0;
                                        }
                                        if (maxY2 < 0) {
                                            maxY2 = 0;
                                        }
                                        if (minX2 > maxX2) {
                                            minX2 = maxX2;
                                            maxX2 = minX2;
                                        }
                                        if (minY2 > maxY2) {
                                            minY2 = maxY2;
                                            maxY2 = minY2;
                                        }
                                        RasterLayers_Select_Dialog.this.cacheTilesArray = new ArrayList();
                                        for (int tempI = minX2; tempI <= maxX2; tempI++) {
                                            for (int tempJ = minY2; tempJ <= maxY2; tempJ++) {
                                                BaseTileInfo tempTile = new BaseTileInfo(tempI, tempJ, tempLevel2);
                                                tempTile.Url = tempTilesLayer.CreateTileURL(tempTilesLayer.GetTileType(), tempTile.Level, tempTile.Col, tempTile.Row);
                                                RasterLayers_Select_Dialog.this.cacheTilesArray.add(tempTile);
                                            }
                                        }
                                        RasterLayers_Select_Dialog.this.cacheTotalCount = RasterLayers_Select_Dialog.this.cacheTilesArray.size();
                                        RasterLayers_Select_Dialog.this.cacheCurrentCount = 0;
                                        RasterLayers_Select_Dialog.this.cacheERRORCount = 0;
                                        if (RasterLayers_Select_Dialog.this.cacheTotalCount > 0) {
                                            RasterLayers_Select_Dialog.this.downloadProgressDialog.SetHeadTitle("缓存网络地图数据");
                                            RasterLayers_Select_Dialog.this.downloadProgressDialog.SetProgressTitle("开始下载网络地图数据...");
                                            RasterLayers_Select_Dialog.this.downloadProgressDialog.SetProgressInfo("");
                                            RasterLayers_Select_Dialog.this.downloadProgressDialog.SetReturnHandler(RasterLayers_Select_Dialog.this.myDownloadHandler);
                                            RasterLayers_Select_Dialog.this.downloadProgressDialog.show();
                                            RasterLayers_Select_Dialog.this.cacheDownloader = new WebMapTilesDownloader(1);
                                            RasterLayers_Select_Dialog.this.cacheDownloader.setTilesLayer(tempTilesLayer);
                                            RasterLayers_Select_Dialog.this.cacheDownloader.setDownloadFileList(RasterLayers_Select_Dialog.this.cacheTilesArray);
                                            RasterLayers_Select_Dialog.this.cacheDownloader.setReturnHandler(RasterLayers_Select_Dialog.this.myDownloadHandler);
                                            new Thread() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Select_Dialog.4.1.1
                                                @Override // java.lang.Thread, java.lang.Runnable
                                                public void run() {
                                                    RasterLayers_Select_Dialog.this.cacheDownloader.StartUpLoad();
                                                }
                                            }.start();
                                            return;
                                        }
                                        Common.ShowDialog("下载数据量不能小于0.");
                                    }
                                }
                            } catch (Exception ex2) {
                                Common.Log("错误", ex2.getMessage());
                            }
                        }
                    });
                } catch (Exception ex2) {
                    Common.Log("错误", ex2.getMessage());
                }
            }
        });
        this._Dialog.findViewById(R.id.button_Delete_Cache).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Select_Dialog.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                Common.ShowYesNoDialog(RasterLayers_Select_Dialog.this._Dialog.getContext(), "是否确定清空当前地图缓存?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Select_Dialog.5.1
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command, Object pObject) {
                        if (command.equals("YES")) {
                            int tempLevel = Integer.parseInt(Common.GetSpinnerValueOnID(RasterLayers_Select_Dialog.this._Dialog, R.id.Spinner02));
                            double xmax = Double.parseDouble(RasterLayers_Select_Dialog.this.xmaxETxt.getText().toString());
                            double xmin = Double.parseDouble(RasterLayers_Select_Dialog.this.xminETxt.getText().toString());
                            double ymax = Double.parseDouble(RasterLayers_Select_Dialog.this.ymaxETxt.getText().toString());
                            double ymin = Double.parseDouble(RasterLayers_Select_Dialog.this.yminETxt.getText().toString());
                            RadioButton tmpRadioButton = (RadioButton) RasterLayers_Select_Dialog.this._Dialog.findViewById(RasterLayers_Select_Dialog.this.mapTypeRadioGroup.getCheckedRadioButtonId());
                            String tmpLayerType = tmpRadioButton.getText().toString();
                            if (PubVar.m_Workspace != null) {
                                for (XLayer tmpLayer2 : PubVar._Map.getRasterLayers()) {
                                    if (tmpLayer2.getLayerType() == ELayerType.ONLINEMAP && tmpLayer2.getName().equals(tmpLayerType)) {
                                        ((XBaseTilesLayer) tmpLayer2).ClearCacheData(tempLevel, xmin, ymin, xmax, ymax);
                                    }
                                }
                                PubVar._Map.RefreshFastRasterLayers();
                                return;
                            }
                            RasterLayer tempLayer = new RasterLayer();
                            tempLayer.SetLayerTypeName(tmpRadioButton.getText().toString());
                            XLayer tempXLayer = RasterLayerWorkspace.CreateRenderLayer(tempLayer);
                            if (tempXLayer != null) {
                                ((XBaseTilesLayer) tempXLayer).ClearCacheData(tempLevel, xmin, ymin, xmax, ymax);
                                PubVar._Map.RefreshFastRasterLayers();
                            }
                        }
                    }
                });
            }
        });
    }

    public void SetLayerList(List<RasterLayer> value) {
        this.m_LayerList = value;
    }

    public void SetFormType(int type) {
        this.FormType = type;
        if (this.FormType == 1) {
            this._Dialog.SetCaption("缓存网络地图");
            this._Dialog.GetButton("1").setVisibility(8);
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Select_Dialog.6
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                ArrayList tempArray = new ArrayList();
                if (RasterLayers_Select_Dialog.this.FormType == 1) {
                    tempArray.add("自定义范围");
                } else {
                    tempArray.add("当前视图范围");
                    tempArray.add("自定义范围");
                }
                RasterLayers_Select_Dialog.this.downloadProgressDialog = CustomeProgressDialog.createDialog(RasterLayers_Select_Dialog.this._Dialog.getContext());
                Common.SetSpinnerListData(RasterLayers_Select_Dialog.this._Dialog, "请选择缓存范围:", tempArray, (int) R.id.Spinner01, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Select_Dialog.6.1
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        if (pObject == null) {
                            return;
                        }
                        if (String.valueOf(pObject).equals("当前视图范围")) {
                            Coordinate tempCoord01 = PubVar._Map.getExtend().getLeftTop();
                            Coordinate tempCoord02 = PubVar._Map.getExtend().getRightBottom();
                            Coordinate tempCoord03 = PubVar.m_Workspace.GetCoorSystem().ConvertXYToBL(tempCoord01.getX(), tempCoord01.getY());
                            Coordinate tempCoord04 = PubVar.m_Workspace.GetCoorSystem().ConvertXYToBL(tempCoord02.getX(), tempCoord02.getY());
                            Common.CoordinateBoundCheck(tempCoord03);
                            Common.CoordinateBoundCheck(tempCoord04);
                            tempCoord03.setGeoX(tempCoord03.getX());
                            tempCoord03.setGeoY(tempCoord03.getY());
                            tempCoord04.setGeoX(tempCoord04.getX());
                            tempCoord04.setGeoY(tempCoord04.getY());
                            RasterLayers_Select_Dialog.this.xmaxETxt.setText(String.valueOf(tempCoord04.getGeoX()));
                            RasterLayers_Select_Dialog.this.xminETxt.setText(String.valueOf(tempCoord03.getGeoX()));
                            RasterLayers_Select_Dialog.this.ymaxETxt.setText(String.valueOf(tempCoord03.getGeoY()));
                            RasterLayers_Select_Dialog.this.yminETxt.setText(String.valueOf(tempCoord04.getGeoY()));
                            RasterLayers_Select_Dialog.this.LinearLayoutInputExtent.setVisibility(8);
                            return;
                        }
                        RasterLayers_Select_Dialog.this.LinearLayoutInputExtent.setVisibility(0);
                    }
                });
                RasterLayers_Select_Dialog.this.refreshMapList();
            }
        });
        this._Dialog.show();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshMapList() {
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Select_Dialog.7
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    HttpGet httpRequest = new HttpGet(String.valueOf(PubVar.ServerURL) + "MapProcessHandler.ashx?method=maplist&appname=" + PubVar.AppName);
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
                    HttpResponse httpResponse = new DefaultHttpClient(httpParameters).execute(httpRequest);
                    if (httpResponse.getStatusLine().getStatusCode() == 200 && RasterLayers_Select_Dialog.this.m_NetHandler != null) {
                        JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                        boolean result = jsonObject.getBoolean("success");
                        String tmpMsg = jsonObject.getString("msg");
                        if (result) {
                            Message msg = RasterLayers_Select_Dialog.this.m_NetHandler.obtainMessage();
                            msg.what = 1;
                            msg.obj = tmpMsg;
                            RasterLayers_Select_Dialog.this.m_NetHandler.sendMessage(msg);
                            return;
                        }
                        Message msg2 = RasterLayers_Select_Dialog.this.m_NetHandler.obtainMessage();
                        msg2.what = 0;
                        msg2.obj = tmpMsg;
                        RasterLayers_Select_Dialog.this.m_NetHandler.sendMessage(msg2);
                    }
                } catch (Exception e) {
                }
            }
        }.start();
    }
}
