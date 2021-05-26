package  com.xzy.forestSystem.gogisapi.Tools;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;
import  com.xzy.forestSystem.qihoo.jiagutracker.C0246Config;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.GraphicSymbolGeometry;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Config.UserParam;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WGS1984;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.ECoordinateSystemType;
import  com.xzy.forestSystem.gogisapi.Display.ISymbol;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Point;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
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

public class Goto_Dialog {
    private XDialogTemplate _Dialog;
    private AutoCompleteTextView autotextView;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private Handler m_NetHandler;
    private RadioGroup m_RadioGroup;
    private MyTableFactory m_TableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public Goto_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Goto_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                Coordinate tempCoord;
                double tmpD01;
                double tmpD012;
                try {
                    if (command.equals("确定")) {
                        if (Goto_Dialog.this.m_RadioGroup.getCheckedRadioButtonId() == R.id.rg_GotoSelectType01) {
                            String tempStr01 = Common.GetTextValueOnID(Goto_Dialog.this._Dialog, (int) R.id.editTextPara01);
                            String tempStr02 = Common.GetTextValueOnID(Goto_Dialog.this._Dialog, (int) R.id.editTextPara02);
                            if (tempStr01 == null || tempStr01.trim().equals("") || tempStr02 == null || tempStr02.trim().equals("")) {
                                Common.ShowDialog("坐标输入值不能为空.");
                                return;
                            }
                            double tempX = 0.0d;
                            double tempY = 0.0d;
                            String tempType = Common.GetSpinnerValueOnID(Goto_Dialog.this._Dialog, R.id.sp_coordInputFormat);
                            if (tempType.contains("WGS")) {
                                if (tempType.contains("DDD.DDDDDD")) {
                                    tempX = Double.parseDouble(tempStr01);
                                    tempY = Double.parseDouble(tempStr02);
                                } else if (tempType.contains("DDD.MMSSSS")) {
                                    tempX = 0.0d;
                                    if (tempStr01.length() > 0) {
                                        int tmpI = tempStr01.indexOf(FileSelector_Dialog.sFolder);
                                        if (tmpI > 0) {
                                            tempX = 0.0d + ((double) Integer.parseInt(tempStr01.substring(0, tmpI)));
                                            if (tmpI + 3 <= tempStr01.length()) {
                                                tempX += (double) (((float) Integer.parseInt(tempStr01.substring(tmpI + 1, tmpI + 3))) / 60.0f);
                                                if (tmpI + 3 < tempStr01.length()) {
                                                    String tmpStr03 = tempStr01.substring(tmpI + 3);
                                                    int tmpI02 = Integer.parseInt(tmpStr03);
                                                    if (tmpI02 > 99) {
                                                        tmpD012 = ((double) tmpI02) / Math.pow(10.0d, (double) (tmpStr03.length() - 2));
                                                    } else {
                                                        tmpD012 = (double) tmpI02;
                                                    }
                                                    tempX += tmpD012 / 3600.0d;
                                                }
                                            }
                                        } else {
                                            tempX = 0.0d + ((double) Integer.parseInt(tempStr01));
                                        }
                                    }
                                    tempY = 0.0d;
                                    if (tempStr02.length() > 0) {
                                        int tmpI2 = tempStr02.indexOf(FileSelector_Dialog.sFolder);
                                        if (tmpI2 > 0) {
                                            tempY = 0.0d + ((double) Integer.parseInt(tempStr02.substring(0, tmpI2)));
                                            if (tmpI2 + 3 <= tempStr02.length()) {
                                                tempY += (double) (((float) Integer.parseInt(tempStr02.substring(tmpI2 + 1, tmpI2 + 3))) / 60.0f);
                                                if (tmpI2 + 3 < tempStr02.length()) {
                                                    String tmpStr032 = tempStr02.substring(tmpI2 + 3);
                                                    int tmpI022 = Integer.parseInt(tmpStr032);
                                                    if (tmpI022 > 99) {
                                                        tmpD01 = ((double) tmpI022) / Math.pow(10.0d, (double) (tmpStr032.length() - 2));
                                                    } else {
                                                        tmpD01 = (double) tmpI022;
                                                    }
                                                    tempY += tmpD01 / 3600.0d;
                                                }
                                            }
                                        } else {
                                            tempY = 0.0d + ((double) Integer.parseInt(tempStr02));
                                        }
                                    }
                                }
                                AbstractC0383CoordinateSystem pCoordSystem = PubVar.m_Workspace.GetCoorSystem();
                                if (pCoordSystem.GetCoordinateSystemType() == ECoordinateSystemType.enWGS1984) {
                                    tempCoord = pCoordSystem.ConvertBLToXY(new Coordinate(tempX, tempY));
                                    tempCoord.setGeoX(tempX);
                                    tempCoord.setGeoY(tempY);
                                } else {
                                    tempCoord = pCoordSystem.ConvertBLtoXYWithTranslate(new Coordinate(tempX, tempY), PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                                }
                            } else {
                                tempCoord = new Coordinate(Double.parseDouble(tempStr01), Double.parseDouble(tempStr02));
                            }
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_User_Goto_InputCoordType", tempType);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_User_Goto_X", tempStr01);
                            PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_User_Goto_Y", tempStr02);
                            if (tempCoord != null) {
                                if (Goto_Dialog.this.m_Callback != null) {
                                    Goto_Dialog.this.m_Callback.OnClick("GoTo返回", new Object[]{"X:" + tempStr01 + "\r\nY:" + tempStr02, tempCoord});
                                }
                                Goto_Dialog.this._Dialog.dismiss();
                                return;
                            }
                            Common.ShowDialog("输入坐标值无效.");
                            return;
                        }
                        int tmpI3 = Goto_Dialog.this.m_TableFactory.getSelectItemIndex();
                        if (tmpI3 <= -1 || tmpI3 >= Goto_Dialog.this.m_MyTableDataList.size()) {
                            Common.ShowDialog("请在查询结果列表中选择城市.");
                            return;
                        }
                        HashMap<String, Object> tmpHash = (HashMap) Goto_Dialog.this.m_MyTableDataList.get(tmpI3);
                        Coordinate tempCoord2 = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(new Coordinate(Double.parseDouble(String.valueOf(tmpHash.get("D2"))), Double.parseDouble(String.valueOf(tmpHash.get("D3")))), PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                        if (Goto_Dialog.this.m_Callback != null) {
                            Goto_Dialog.this.m_Callback.OnClick("GoTo返回", new Object[]{String.valueOf(tmpHash.get("D1")), tempCoord2});
                        }
                        Goto_Dialog.this._Dialog.dismiss();
                    } else if (!command.equals("OnItemSelected")) {
                    } else {
                        if (object.toString().contains("平面坐标")) {
                            Common.SetTextViewValueOnID(Goto_Dialog.this._Dialog, (int) R.id.tv_xlabel, "X坐标：");
                            Common.SetTextViewValueOnID(Goto_Dialog.this._Dialog, (int) R.id.tv_ylabel, "Y坐标：");
                            return;
                        }
                        Common.SetTextViewValueOnID(Goto_Dialog.this._Dialog, (int) R.id.tv_xlabel, "经度：");
                        Common.SetTextViewValueOnID(Goto_Dialog.this._Dialog, (int) R.id.tv_ylabel, "纬度：");
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_RadioGroup = null;
        this.autotextView = null;
        this.m_TableFactory = null;
        this.m_MyTableDataList = null;
        this.m_NetHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Goto_Dialog.2
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
                                    Goto_Dialog.this._Dialog.dismiss();
                                }
                            }
                        }
                    } else if (msg.what == 102) {
                        Goto_Dialog.this.m_MyTableDataList.clear();
                        if (!(msg.obj == null || (tmpObjs = (Object[]) msg.obj) == null || tmpObjs.length <= 1)) {
                            String.valueOf(tmpObjs[0]);
                            String tmpMsgString = String.valueOf(tmpObjs[1]);
                            if (tmpMsgString.length() > 0 && (jsonObject = new JSONObject(tmpMsgString)) != null && (tmpJsonArray = jsonObject.getJSONArray("results")) != null && tmpJsonArray.length() > 0) {
                                int count = tmpJsonArray.length();
                                for (int i = 0; i < count; i++) {
                                    JSONObject tmpJSONObject01 = tmpJsonArray.getJSONObject(i);
                                    HashMap<String, Object> tmpHashMap = new HashMap<>();
                                    tmpHashMap.put("D1", tmpJSONObject01.getString("name"));
                                    tmpHashMap.put("D2", tmpJSONObject01.getString("lng"));
                                    tmpHashMap.put("D3", tmpJSONObject01.getString("lat"));
                                    Goto_Dialog.this.m_MyTableDataList.add(tmpHashMap);
                                }
                            }
                        }
                        Goto_Dialog.this.m_TableFactory.notifyDataSetChanged();
                    }
                } catch (Exception ex) {
                    Common.Log("错误", ex.getMessage());
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.goto_dialog);
        this._Dialog.Resize(0.96f, 0.96f);
        this._Dialog.SetCaption("Goto");
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_RadioGroup = (RadioGroup) this._Dialog.findViewById(R.id.rg_GotoSelectType);
        this.m_RadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Goto_Dialog.3
            @SuppressLint("WrongConstant")
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                if (arg1 == R.id.rg_GotoSelectType01) {
                    Goto_Dialog.this._Dialog.findViewById(R.id.ll_gotoInputCoord).setVisibility(0);
                    Goto_Dialog.this._Dialog.findViewById(R.id.ll_gotoInputAddress).setVisibility(8);
                    return;
                }
                Goto_Dialog.this._Dialog.findViewById(R.id.ll_gotoInputCoord).setVisibility(8);
                Goto_Dialog.this._Dialog.findViewById(R.id.ll_gotoInputAddress).setVisibility(0);
            }
        });
        this._Dialog.findViewById(R.id.ll_gotoInputCoord).setVisibility(0);
        this._Dialog.findViewById(R.id.ll_gotoInputAddress).setVisibility(8);
        this.autotextView = (AutoCompleteTextView) this._Dialog.findViewById(R.id.autotext_Address);
        this.autotextView.setThreshold(1);
        this.autotextView.setTag("");
        this.autotextView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Goto_Dialog.4
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg0 != null && arg2 < arg0.getCount()) {
                    String tmpStr = String.valueOf(arg0.getItemAtPosition(arg2));
                    if (tmpStr.length() > 0 && !tmpStr.toLowerCase().equals(C0246Config.EMPTY_STRING)) {
                        Goto_Dialog.this.autotextView.setText(tmpStr);
                        Goto_Dialog.this.queryAddress();
                    }
                }
            }
        });
        this._Dialog.findViewById(R.id.btn_queryAddress).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Goto_Dialog.5
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                Goto_Dialog.this.queryAddressFromServer();
            }
        });
        this.m_TableFactory = new MyTableFactory();
        this.m_TableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.mytable_AddressList), "自定义", "城市名称", "text", new int[]{-100}, this.pCallback);
        this.m_TableFactory.SetHeaderVisible(false);
        this.m_MyTableDataList = new ArrayList();
        this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1"}, this.pCallback);
        this.m_RadioGroup.check(R.id.rg_GotoSelectType02);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void queryAddress() {
        String tmpString01;
        String[] arr;
        try {
            String tmpName = this.autotextView.getText().toString().trim();
            if (tmpName.length() > 0) {
                String tmpString012 = String.valueOf(this.autotextView.getTag());
                if (!tmpString012.contains(String.valueOf(tmpName) + ";") && (arr = (String.valueOf(tmpString012) + tmpName + ";").split(";")) != null && arr.length > 0) {
//                    this.autotextView.setTag(tmpString01);
                    this.autotextView.setAdapter(new ArrayAdapter<>(this._Dialog.getContext(), (int) R.layout.autocompletetextview_layout, arr));
                    HashMap<String, String> tempHash01 = new HashMap<>();
//                    tempHash01.put("F2", tmpString01);
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_User_Goto_AddressList", tempHash01);
                }
                List<HashMap<String, Object>> tmpList = QueryAddress(tmpName);
                if (tmpList.size() > 0) {
                    this.m_MyTableDataList.clear();
                    for (HashMap<String, Object> tmpHashMap : tmpList) {
                        this.m_MyTableDataList.add(tmpHashMap);
                    }
                    this.m_TableFactory.notifyDataSetChanged();
                    return;
                }
                this.m_MyTableDataList.clear();
                this.m_TableFactory.notifyDataSetChanged();
                return;
            }
            Common.ShowDialog("请输入城市、市区县名称.");
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void queryAddressFromServer() {
        String tmpString01;
        String[] arr;
        try {
            final String tmpAddressName = this.autotextView.getText().toString().trim();
            if (tmpAddressName.length() > 0) {
                String tmpString012 = String.valueOf(this.autotextView.getTag());
                if (!tmpString012.contains(String.valueOf(tmpAddressName) + ";") && (arr = (String.valueOf(tmpString012) + tmpAddressName + ";").split(";")) != null && arr.length > 0) {
//                    this.autotextView.setTag(tmpString01);
                    this.autotextView.setAdapter(new ArrayAdapter<>(this._Dialog.getContext(), (int) R.layout.autocompletetextview_layout, arr));
                    HashMap<String, String> tempHash01 = new HashMap<>();
//                    tempHash01.put("F2", tmpString01);
                    PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_User_Goto_AddressList", tempHash01);
                }
                if (!tmpAddressName.equals("")) {
                    Common.ShowProgressDialogWithoutClose("正在进行查询,请稍候.", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Goto_Dialog.6
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            Goto_Dialog.QueryAddressInfoFromServer(tmpAddressName, Goto_Dialog.this.m_NetHandler, (Handler) pObject);
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
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Goto_Dialog.7
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
        new Thread() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Goto_Dialog.8
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

    public static List<HashMap<String, Object>> QueryAddress(String cityname) {
        List<HashMap<String, Object>> result = new ArrayList<>();
        try {
            SQLiteReader tmpSQLiteReader = PubVar._PubCommand.m_ConfigDB.GetSQLiteDatabase().Query("Select CityName,lng,lat From T_CityAddress Where CityName like '%" + cityname + "%'");
            if (tmpSQLiteReader != null) {
                while (tmpSQLiteReader.Read()) {
                    HashMap<String, Object> tmpHashMap = new HashMap<>();
                    tmpHashMap.put("D1", tmpSQLiteReader.GetString(0));
                    tmpHashMap.put("D2", Double.valueOf(tmpSQLiteReader.GetDouble(1)));
                    tmpHashMap.put("D3", Double.valueOf(tmpSQLiteReader.GetDouble(2)));
                    result.add(tmpHashMap);
                }
                tmpSQLiteReader.Close();
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
        String tempStr2;
        String tempStr3;
        String tempStr4;
        try {
            List<String> tempArray = new ArrayList<>();
            tempArray.add("【WGS84】DDD.DDDDDD");
            tempArray.add("【WGS84】DDD.MMSSSS");
            tempArray.add("平面坐标");
            Common.SetSpinnerListData(this._Dialog, "选择坐标输入格式", tempArray, (int) R.id.sp_coordInputFormat, this.pCallback);
            String tmpinputType = tempArray.get(0);
            UserParam tempParam = PubVar._PubCommand.m_UserConfigDB.GetUserParam();
            HashMap<String, String> tempHashMap = tempParam.GetUserPara("Tag_User_Goto_InputCoordType");
            if (!(tempHashMap == null || (tempStr4 = tempHashMap.get("F2")) == null || tempStr4.equals(""))) {
                tmpinputType = tempStr4;
            }
            Common.SetValueToView(tmpinputType, this._Dialog.findViewById(R.id.sp_coordInputFormat));
            HashMap<String, String> tempHashMap2 = tempParam.GetUserPara("Tag_User_Goto_X");
            if (!(tempHashMap2 == null || (tempStr3 = tempHashMap2.get("F2")) == null || tempStr3.equals(""))) {
                Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara01, tempStr3);
            }
            HashMap<String, String> tempHashMap3 = tempParam.GetUserPara("Tag_User_Goto_Y");
            if (!(tempHashMap3 == null || (tempStr2 = tempHashMap3.get("F2")) == null || tempStr2.equals(""))) {
                Common.SetEditTextValueOnID(this._Dialog, R.id.editTextPara02, tempStr2);
            }
            HashMap<String, String> tempHashMap4 = tempParam.GetUserPara("Tag_User_Goto_AddressList");
            if (tempHashMap4 != null && (tempStr = tempHashMap4.get("F2")) != null && !tempStr.equals("") && (arr = tempStr.split(";")) != null && arr.length > 0) {
                this.autotextView.setTag(tempStr);
                this.autotextView.setAdapter(new ArrayAdapter<>(this._Dialog.getContext(), (int) R.layout.autocompletetextview_layout, arr));
            }
        } catch (Exception ex) {
            Common.Log("错误", ex.getMessage());
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.Goto_Dialog.9
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Goto_Dialog.this.refreshFormatTypes();
            }
        });
        this._Dialog.show();
    }
}
