package  com.xzy.forestSystem.gogisapi.Tools;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import  com.xzy.forestSystem.qihoo.jiagutracker.C0246Config;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.GraphicSymbolGeometry;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WGS1984;
import  com.xzy.forestSystem.gogisapi.Display.ISymbol;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Point;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
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
import org.gdal.ogr.ogrConstants;
import org.json.JSONArray;
import org.json.JSONObject;

public class Search_Dialog {
    private XDialogTemplate _Dialog;
    private AutoCompleteTextView autotextView;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private Handler m_NetHandler;
    private MyTableFactory m_TableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean GotoGeoemtry() {
        int tmpI = this.m_TableFactory.getSelectItemIndex();
        if (tmpI <= -1 || tmpI >= this.m_MyTableDataList.size()) {
            return false;
        }
        HashMap<String, Object> tmpHash = this.m_MyTableDataList.get(tmpI);
        AbstractGeometry tmpGeometry = (AbstractGeometry) tmpHash.get("Geometry");
        if (!(tmpGeometry == null || this.m_Callback == null)) {
            this.m_Callback.OnClick("搜索返回", new Object[]{String.valueOf(tmpHash.get("D1")), tmpGeometry});
        }
        return true;
    }

    public Search_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Search_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                try {
                    if (command.equals("确定")) {
                        if (Search_Dialog.this.GotoGeoemtry()) {
                            Search_Dialog.this._Dialog.dismiss();
                        } else {
                            Common.ShowDialog("请在查询结果列表中选择要查看的结果.");
                        }
                    } else if (command.equals("单击选择行") && object != null) {
                        Search_Dialog.this.pCallback.OnClick("确定", null);
                    }
                } catch (Exception e) {
                }
            }
        };
        this.autotextView = null;
        this.m_TableFactory = null;
        this.m_MyTableDataList = null;
        this.m_NetHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Search_Dialog.2
            @Override // android.os.Handler
            public void handleMessage(Message msg) {
                Object[] tmpObjs;
                JSONObject jsonObject;
                JSONArray tmpJsonArray;
                Object[] tmpObjs2;
                try {
                    if (msg.what == 0) {
                        if (msg.obj != null) {
                            Common.ShowToast(String.valueOf(msg.obj));
                        }
                    } else if (msg.what == 101) {
                        if (!(msg.obj == null || (tmpObjs2 = (Object[]) msg.obj) == null || tmpObjs2.length <= 1)) {
                            String tmpAddres = String.valueOf(tmpObjs2[0]);
                            String[] tmpStrs01 = String.valueOf(tmpObjs2[1]).split(",");
                            if (tmpStrs01 != null && tmpStrs01.length > 1) {
                                double tmpX = Double.parseDouble(tmpStrs01[0].trim());
                                double tmpY = Double.parseDouble(tmpStrs01[1].trim());
                                if (tmpX > 0.0d && tmpY > 0.0d) {
                                    Coordinate tmpCoordinate = Coordinate_WGS1984.BLToXY(tmpX, tmpY);
                                    PubVar._Map.ZoomToCenter(tmpCoordinate);
                                    ISymbol m_GPSPoint = PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemPointSymbol("样式04");
                                    GraphicSymbolGeometry tmpGraphicSymbolGeo = new GraphicSymbolGeometry();
                                    Point tmpGeo = new Point(tmpCoordinate.getX(), tmpCoordinate.getY());
                                    tmpGeo.setLabelContent(tmpAddres);
                                    tmpGraphicSymbolGeo._Geoemtry = tmpGeo;
                                    tmpGraphicSymbolGeo._Symbol = m_GPSPoint;
                                    PubVar._MapView._GraphicLayer.AddGeometry(tmpGraphicSymbolGeo);
                                    PubVar._MapView.invalidate();
                                    Search_Dialog.this._Dialog.dismiss();
                                }
                            }
                        }
                    } else if (msg.what == 102) {
                        Search_Dialog.this.m_MyTableDataList.clear();
                        if (!(msg.obj == null || (tmpObjs = (Object[]) msg.obj) == null || tmpObjs.length <= 1)) {
                            String.valueOf(tmpObjs[0]);
                            String tmpMsgString = String.valueOf(tmpObjs[1]);
                            if (tmpMsgString.length() > 0 && (jsonObject = new JSONObject(tmpMsgString)) != null && (tmpJsonArray = jsonObject.getJSONArray("results")) != null && tmpJsonArray.length() > 0) {
                                int count = tmpJsonArray.length();
                                for (int i = 0; i < count; i++) {
                                    JSONObject tmpJSONObject01 = tmpJsonArray.getJSONObject(i);
                                    HashMap<String, Object> tmpHashMap = new HashMap<>();
                                    tmpHashMap.put("D1", tmpJSONObject01.getString("name"));
                                    tmpHashMap.put("Geometry", new Point(PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(new Coordinate(Double.parseDouble(tmpJSONObject01.getString("lng")), Double.parseDouble(tmpJSONObject01.getString("lat"))), PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem())));
                                    Search_Dialog.this.m_MyTableDataList.add(tmpHashMap);
                                }
                            }
                        }
                        Search_Dialog.this.m_TableFactory.notifyDataSetChanged();
                    }
                } catch (Exception ex) {
                    Common.Log("错误", ex.getMessage());
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.search_dialog);
        this._Dialog.Resize(0.96f, 0.96f);
        this._Dialog.SetCaption("搜索");
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.autotextView = (AutoCompleteTextView) this._Dialog.findViewById(R.id.autotext_Address);
        this.autotextView.setThreshold(1);
        this.autotextView.setTag("");
        this.autotextView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Search_Dialog.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg0 != null && arg2 < arg0.getCount()) {
                    String tmpStr = String.valueOf(arg0.getItemAtPosition(arg2));
                    if (tmpStr.length() > 0 && !tmpStr.toLowerCase().equals(C0246Config.EMPTY_STRING)) {
                        Search_Dialog.this.autotextView.setText(tmpStr);
                        Search_Dialog.this.queryAddress();
                    }
                }
            }
        });
        this._Dialog.findViewById(R.id.btn_queryAddress).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Search_Dialog.4
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                Search_Dialog.this.queryAddress();
            }
        });
        this.m_TableFactory = new MyTableFactory();
        this.m_TableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_AddressList), "自定义", "结果", "text", new int[]{-100}, this.pCallback);
        this.m_TableFactory.SetHeaderVisible(false);
        this.m_MyTableDataList = new ArrayList();
        this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1"}, this.pCallback);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void queryAddress() {
        String tmpString01 = null;
        String[] arr;
        this.m_MyTableDataList.clear();
        try {
            String tmpName = this.autotextView.getText().toString().trim();
            if (tmpName.length() > 0) {
                String tmpString012 = String.valueOf(this.autotextView.getTag());
                if (!tmpString012.contains(String.valueOf(tmpName) + ";") && (arr = (String.valueOf(tmpString012) + tmpName + ";").split(";")) != null && arr.length > 0) {
                    this.autotextView.setTag(tmpString01);
                    this.autotextView.setAdapter(new ArrayAdapter<>(this._Dialog.getContext(), (int) R.layout.autocompletetextview_layout, arr));
                    HashMap<String, String> tempHash01 = new HashMap<>();
                    tempHash01.put("F2", tmpString01);
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_User_Query_AddressList", tempHash01);
                }
                if (Common.GetCheckBoxValueOnID(this._Dialog, R.id.ck_QueryFromNet)) {
                    queryAddressFromServer();
                } else {
                    List<HashMap<String, Object>> tmpList = queryInfoFromLocal(tmpName);
                    if (tmpList.size() > 0) {
                        this.m_MyTableDataList.addAll(tmpList);
                    }
                }
            } else {
                Common.ShowDialog("请输入查询的关键字.");
            }
        } catch (Exception e) {
        }
        this.m_TableFactory.notifyDataSetChanged();
    }

    private void queryAddressFromServer() {
        String tmpString01 = null;
        String[] arr;
        try {
            final String tmpAddressName = this.autotextView.getText().toString().trim();
            if (tmpAddressName.length() > 0) {
                String tmpString012 = String.valueOf(this.autotextView.getTag());
                if (!tmpString012.contains(String.valueOf(tmpAddressName) + ";") && (arr = (String.valueOf(tmpString012) + tmpAddressName + ";").split(";")) != null && arr.length > 0) {
                    this.autotextView.setTag(tmpString01);
                    this.autotextView.setAdapter(new ArrayAdapter<>(this._Dialog.getContext(), (int) R.layout.autocompletetextview_layout, arr));
                    HashMap<String, String> tempHash01 = new HashMap<>();
                    tempHash01.put("F2", tmpString01);
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_User_Query_AddressList", tempHash01);
                }
                if (!tmpAddressName.equals("")) {
                    Common.ShowProgressDialogWithoutClose("正在进行查询,请稍候.", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Search_Dialog.5
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            Search_Dialog.QueryAddressInfoFromServer(tmpAddressName, Search_Dialog.this.m_NetHandler, (Handler) pObject);
                        }
                    });
                    return;
                }
                return;
            }
            Common.ShowDialog("请输入地址名称.");
        } catch (Exception e) {
        }
    }

    public static void QueryAddressLatLngFromServer(final String address, final Handler netHandler, final Handler progrssbarHandler) {
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Search_Dialog.6
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    HttpGet httpRequest = new HttpGet(String.valueOf(PubVar.ServerURL) + "MapProcessHandler.ashx?method=address2coord&deviceid=" + PubVar.m_AuthorizeTools.getUserAndroidID() + "&address=" + address);
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
                    HttpResponse httpResponse = new DefaultHttpClient(httpParameters).execute(httpRequest);
                    if (httpResponse.getStatusLine().getStatusCode() == 200 && netHandler != null) {
                        JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                        boolean result = jsonObject.getBoolean("success");
                        String tmpMsg = jsonObject.getString("msg");
                        if (result) {
                            Message msg = netHandler.obtainMessage();
                            msg.what = ogrConstants.wkbLinearRing;
                            msg.obj = new Object[]{address, tmpMsg};
                            netHandler.sendMessage(msg);
                        } else {
                            Message msg2 = netHandler.obtainMessage();
                            msg2.what = 0;
                            msg2.obj = tmpMsg;
                            netHandler.sendMessage(msg2);
                        }
                    }
                } catch (Exception e) {
                }
                try {
                    if (progrssbarHandler != null) {
                        Message msg3 = progrssbarHandler.obtainMessage();
                        msg3.what = 0;
                        progrssbarHandler.sendMessage(msg3);
                    }
                } catch (Exception e2) {
                }
            }
        }.start();
    }

    public static void QueryAddressInfoFromServer(final String address, final Handler netHandler, final Handler progrssbarHandler) {
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Search_Dialog.7
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    HttpGet httpRequest = new HttpGet(String.valueOf(PubVar.ServerURL) + "MapProcessHandler.ashx?method=queryaddress&deviceid=" + PubVar.m_AuthorizeTools.getUserAndroidID() + "&address=" + address);
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
                    HttpResponse httpResponse = new DefaultHttpClient(httpParameters).execute(httpRequest);
                    if (httpResponse.getStatusLine().getStatusCode() == 200 && netHandler != null) {
                        JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                        boolean result = jsonObject.getBoolean("success");
                        String tmpMsg = jsonObject.getString("msg");
                        if (result) {
                            Message msg = netHandler.obtainMessage();
                            msg.what = 102;
                            msg.obj = new Object[]{address, tmpMsg};
                            netHandler.sendMessage(msg);
                        } else {
                            Message msg2 = netHandler.obtainMessage();
                            msg2.what = 0;
                            msg2.obj = tmpMsg;
                            netHandler.sendMessage(msg2);
                        }
                    }
                } catch (Exception e) {
                }
                try {
                    if (progrssbarHandler != null) {
                        Message msg3 = progrssbarHandler.obtainMessage();
                        msg3.what = 0;
                        progrssbarHandler.sendMessage(msg3);
                    }
                } catch (Exception e2) {
                }
            }
        }.start();
    }

    private List<HashMap<String, Object>> queryInfoFromLocal(String keyword) {
        List<HashMap<String, Object>> result = new ArrayList<>();
        int tmpCount = 0;
        try {
            Iterator<GeoLayer> localIterator1 = PubVar._Map.getGeoLayers().getList().iterator();
            while (true) {
                if (!localIterator1.hasNext() || tmpCount > 9) {
                    break;
                }
                DataSet tmpDataSet = localIterator1.next().getDataset();
                SQLiteReader tmpSQLiteReader = tmpDataSet.getDataSource().GetSQLiteDatabase().Query("Select SYS_ID,F1||','||F2||','||F3||','||F4||','||F5||','||F6||','||F7||','||F8||','||F9||','||F10 AS Info From " + tmpDataSet.getDataTableName() + " Where F1 like '%" + keyword + "%' OR F2 like '%" + keyword + "%' OR F3 like '%" + keyword + "%' OR F4 like '%" + keyword + "%' OR F5 like '%" + keyword + "%' OR F6 like '%" + keyword + "%' OR F7 like '%" + keyword + "%' OR F8 like '%" + keyword + "%' OR F9 like '%" + keyword + "%' OR F10 like '%" + keyword + "%' Limit 10 ");
                if (tmpSQLiteReader != null) {
                    while (tmpSQLiteReader.Read() && tmpCount <= 9) {
                        AbstractGeometry tmpGeometry = tmpDataSet.GetGeometryBySYSIDMust(String.valueOf(tmpSQLiteReader.GetInt32(0)));
                        if (tmpGeometry != null) {
                            HashMap<String, Object> tmpHashMap = new HashMap<>();
                            tmpHashMap.put("D1", tmpSQLiteReader.GetString(1));
                            tmpHashMap.put("Geometry", tmpGeometry);
                            result.add(tmpHashMap);
                            tmpCount++;
                        }
                    }
                    tmpSQLiteReader.Close();
                }
                if (tmpCount > 9) {
                    break;
                }
            }
            if (tmpCount < 10) {
                Iterator<GeoLayer> tempIterBKVLayers = PubVar._Map.getVectorGeoLayers().getList().iterator();
                while (true) {
                    if (tempIterBKVLayers.hasNext()) {
                        DataSet tmpDataSet2 = tempIterBKVLayers.next().getDataset();
                        SQLiteReader tmpSQLiteReader2 = tmpDataSet2.getDataSource().GetSQLiteDatabase().Query("Select SYS_ID,F1||','||F2||','||F3||','||F4||','||F5||','||F6||','||F7||','||F8||','||F9||','||F10 AS Info From " + tmpDataSet2.getDataTableName() + " Where F1 like '%" + keyword + "%' OR F2 like '%" + keyword + "%' OR F3 like '%" + keyword + "%' OR F4 like '%" + keyword + "%' OR F5 like '%" + keyword + "%' OR F6 like '%" + keyword + "%' OR F7 like '%" + keyword + "%' OR F8 like '%" + keyword + "%' OR F9 like '%" + keyword + "%' OR F10 like '%" + keyword + "%' Limit 10 ");
                        if (tmpSQLiteReader2 != null) {
                            while (tmpSQLiteReader2.Read() && tmpCount <= 9) {
                                AbstractGeometry tmpGeometry2 = tmpDataSet2.GetGeometryBySYSIDMust(String.valueOf(tmpSQLiteReader2.GetInt32(0)));
                                if (tmpGeometry2 != null) {
                                    HashMap<String, Object> tmpHashMap2 = new HashMap<>();
                                    tmpHashMap2.put("D1", tmpSQLiteReader2.GetString(1));
                                    tmpHashMap2.put("Geometry", tmpGeometry2);
                                    result.add(tmpHashMap2);
                                    tmpCount++;
                                }
                            }
                            tmpSQLiteReader2.Close();
                        }
                        if (tmpCount > 9) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            Common.Log("错误", ex.getMessage());
        }
        return result;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshFormatTypes() {
        String tempStr;
        String[] arr;
        try {
            HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_User_Query_AddressList");
            if (tempHashMap != null && (tempStr = tempHashMap.get("F2")) != null && !tempStr.equals("") && (arr = tempStr.split(";")) != null && arr.length > 0) {
                this.autotextView.setTag(tempStr);
                this.autotextView.setAdapter(new ArrayAdapter<>(this._Dialog.getContext(), (int) R.layout.autocompletetextview_layout, arr));
            }
        } catch (Exception ex) {
            Common.Log("错误", ex.getMessage());
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Search_Dialog.8
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Search_Dialog.this.refreshFormatTypes();
            }
        });
        this._Dialog.show();
    }
}
