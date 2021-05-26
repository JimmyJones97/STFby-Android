package  com.xzy.forestSystem.gogisapi.Navigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WGS1984;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.Input_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Navigation_Dialog {
    private static DecimalFormat _DecimalFormat = new DecimalFormat("0.0");
    private XDialogTemplate _Dialog;
    private boolean _myTaskPause;
    private Handler _myUpdateHandler;
    private ICallback m_Callback;
    private Bitmap m_DeleteICON;
    private List<HashMap<String, Object>> m_ListViewDataItemList;
    private MyTableFactory m_MyTableFactory;
    private Runnable myUpdateByGPSTask;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public Navigation_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_DeleteICON = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.Navigation_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                List<NavigatePoint> tempList;
                int tmpIndex;
                String[] tempStrs;
                try {
                    if (command.equals("确定")) {
                        C0706Navigation tempNav = PubVar._PubCommand.GetNavigation();
                        tempNav.m_DrawLineBool = Common.GetCheckBoxValueOnID(Navigation_Dialog.this._Dialog, R.id.cb_Nav_ShowNavLine);
                        tempNav.m_DrawTargetBool = Common.GetCheckBoxValueOnID(Navigation_Dialog.this._Dialog, R.id.cb_Nav_ShowNavPtnInfo);
                        tempNav.m_AlarmDistanceBool = Common.GetCheckBoxValueOnID(Navigation_Dialog.this._Dialog, R.id.cb_Nav_AlarmNav);
                        if (tempNav.m_AlarmDistanceBool) {
                            tempNav.m_AlarmDistance = Double.parseDouble(Common.GetEditTextValueOnID(Navigation_Dialog.this._Dialog, R.id.et_Nav_AlarmDis));
                        }
                        tempNav.Clear();
                        for (HashMap<String, Object> tempHash : Navigation_Dialog.this.m_ListViewDataItemList) {
                            NavigatePoint tempNavPtn = (NavigatePoint) tempHash.get("D5");
                            if (tempNavPtn != null) {
                                tempNav.AddNavigationPoint(tempNavPtn);
                            }
                        }
                        tempNav.SetStartNavigation(true);
                        Navigation_Dialog.this._Dialog.dismiss();
                    } else if (command.equals("清空")) {
                        Common.ShowYesNoDialog(Navigation_Dialog.this._Dialog.getContext(), "是否清空所有导航目标?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.Navigation_Dialog.1.1
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString, Object pObject) {
                                if (paramString.equals("YES")) {
                                    C0706Navigation tempNav2 = PubVar._PubCommand.GetNavigation();
                                    tempNav2.Clear();
                                    tempNav2.SetStartNavigation(false);
                                    Navigation_Dialog.this.m_ListViewDataItemList.clear();
                                    Navigation_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                                }
                            }
                        });
                    } else if (command.equals("单击单元格")) {
                        if (object != null && (tempStrs = object.toString().split(",")) != null && tempStrs.length > 0) {
                            final int tmpRowIndex = Integer.parseInt(tempStrs[0]);
                            int tmpColIndex = Integer.parseInt(tempStrs[1]);
                            if (tmpColIndex == 0) {
                                Navigation_Dialog.this._myTaskPause = true;
                                Input_Dialog tempDialog = new Input_Dialog();
                                tempDialog.setValues("重命名", "导航目标名称:", String.valueOf(((HashMap) Navigation_Dialog.this.m_ListViewDataItemList.get(tmpRowIndex)).get("D1")));
                                tempDialog.SetReturnTag(Integer.valueOf(tmpRowIndex));
                                tempDialog.SetCallback(Navigation_Dialog.this.pCallback);
                                tempDialog.ShowDialog();
                            } else if (tmpColIndex == 3) {
                                Common.ShowYesNoDialog(PubVar.MainContext, "是否删除以下导航目标?\r\n" + ((HashMap) Navigation_Dialog.this.m_ListViewDataItemList.get(tmpRowIndex)).get("D1").toString(), new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.Navigation_Dialog.1.2
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String paramString, Object pObject) {
                                        if (paramString.equals("YES") && tmpRowIndex < Navigation_Dialog.this.m_ListViewDataItemList.size()) {
                                            Navigation_Dialog.this.m_ListViewDataItemList.remove(tmpRowIndex);
                                            Navigation_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    } else if (command.equals("输入参数")) {
                        Object[] tmpObjs = (Object[]) object;
                        if (tmpObjs != null && tmpObjs.length > 1 && tmpObjs[0] != null && (tmpIndex = Integer.parseInt(tmpObjs[0].toString())) < Navigation_Dialog.this.m_ListViewDataItemList.size()) {
                            String tempValue = tmpObjs[1].toString();
                            HashMap tempHash2 = (HashMap) Navigation_Dialog.this.m_ListViewDataItemList.get(tmpIndex);
                            tempHash2.put("D1", tempValue);
                            NavigatePoint tmpPtn = (NavigatePoint) tempHash2.get("D5");
                            if (tmpPtn != null) {
                                tmpPtn._Name = tempValue;
                            }
                            Navigation_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                        }
                        Navigation_Dialog.this._myTaskPause = false;
                    } else if (command.contains("选择文件")) {
                        String[] tempPath2 = object.toString().split(";");
                        if (tempPath2 != null && tempPath2.length > 1) {
                            int tempCount = tempPath2.length / 2;
                            boolean tmpNeedRefresh = false;
                            int tmpTid01 = 0;
                            for (int tempI = 0; tempI < tempCount; tempI++) {
                                String str = tempPath2[tempI * 2];
                                String tempfilepath = tempPath2[(tempI * 2) + 1];
                                File tmpGPSFile = new File(String.valueOf(tempfilepath.substring(0, tempfilepath.lastIndexOf(FileSelector_Dialog.sFolder))) + ".txt");
                                if (tmpGPSFile.exists()) {
                                    String tempGPSInfo = Common.ReadTxtFileContent(tmpGPSFile.getAbsolutePath());
                                    if (!tempGPSInfo.equals("")) {
                                        String[] tempStrs2 = tempGPSInfo.split("\r\n");
                                        double tmpX = 0.0d;
                                        double tmpY = 0.0d;
                                        double tmpZ = 0.0d;
                                        if (tempStrs2 != null && tempStrs2.length > 0) {
                                            int length = tempStrs2.length;
                                            int i = 0;
                                            while (true) {
                                                if (i >= length) {
                                                    break;
                                                }
                                                String tempStr = tempStrs2[i];
                                                if (tempStr.contains("经度：")) {
                                                    tmpX = Double.parseDouble(tempStr.substring(tempStr.indexOf("经度：") + 3));
                                                    if (!(tmpY == 0.0d || tmpZ == 0.0d)) {
                                                        break;
                                                    }
                                                } else if (tempStr.contains("纬度：")) {
                                                    tmpY = Double.parseDouble(tempStr.substring(tempStr.indexOf("纬度：") + 3));
                                                    if (!(tmpX == 0.0d || tmpZ == 0.0d)) {
                                                        break;
                                                    }
                                                } else if (tempStr.contains("高程：")) {
                                                    tmpZ = Double.parseDouble(tempStr.substring(tempStr.indexOf("高程：") + 3));
                                                    if (!(tmpX == 0.0d || tmpY == 0.0d)) {
                                                        break;
                                                    }
                                                } else {
                                                    continue;
                                                }
                                                i++;
                                            }
                                            if (!(tmpX == 0.0d || tmpY == 0.0d)) {
                                                tmpTid01++;
                                                NavigatePoint tmpPtn2 = new NavigatePoint();
                                                tmpPtn2.SetName("图片" + tmpTid01);
                                                tmpPtn2.SetCoordinate(PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(tmpX, tmpY, tmpZ, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem()));
                                                HashMap tempHashMap = new HashMap();
                                                tempHashMap.put("D1", tmpPtn2._Name);
                                                tempHashMap.put("D2", Navigation_Dialog._DecimalFormat.format(tmpPtn2._Distance));
                                                tempHashMap.put("D3", Integer.valueOf((int) tmpPtn2._Direction));
                                                tempHashMap.put("D4", Navigation_Dialog.this.m_DeleteICON);
                                                tempHashMap.put("D5", tmpPtn2);
                                                Navigation_Dialog.this.m_ListViewDataItemList.add(tempHashMap);
                                                tmpNeedRefresh = true;
                                            }
                                        }
                                    }
                                }
                            }
                            if (tmpNeedRefresh) {
                                Navigation_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                            }
                        }
                    } else if (command.equals("添加导航目标返回") && (tempList = (List) object) != null && tempList.size() > 0) {
                        boolean tmpNeedRefresh2 = false;
                        for (NavigatePoint tmpPtn3 : tempList) {
                            if (tmpPtn3 != null) {
                                HashMap tempHashMap2 = new HashMap();
                                tempHashMap2.put("D1", tmpPtn3._Name);
                                tempHashMap2.put("D2", Navigation_Dialog._DecimalFormat.format(tmpPtn3._Distance));
                                tempHashMap2.put("D3", Integer.valueOf((int) tmpPtn3._Direction));
                                tempHashMap2.put("D4", Navigation_Dialog.this.m_DeleteICON);
                                tempHashMap2.put("D5", tmpPtn3);
                                Navigation_Dialog.this.m_ListViewDataItemList.add(tempHashMap2);
                                tmpNeedRefresh2 = true;
                            }
                        }
                        if (tmpNeedRefresh2) {
                            Navigation_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_ListViewDataItemList = null;
        this.m_MyTableFactory = new MyTableFactory();
        this._myTaskPause = false;
        this._myUpdateHandler = new Handler();
        this.myUpdateByGPSTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.Navigation_Dialog.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    if (!Navigation_Dialog.this._myTaskPause && Navigation_Dialog.this.m_ListViewDataItemList.size() > 0) {
                        Coordinate tempGPSCoord = null;
                        if (PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                            tempGPSCoord = PubVar._PubCommand.m_GpsLocation.getGPSCoordinate();
                        }
                        for (HashMap<String, Object> tempHashMap : Navigation_Dialog.this.m_ListViewDataItemList) {
                            NavigatePoint tempNavPtn = (NavigatePoint) tempHashMap.get("D5");
                            if (tempNavPtn != null) {
                                if (tempGPSCoord != null) {
                                    tempNavPtn.UpdateLocation(tempGPSCoord, PubVar._PubCommand.m_Navigation.m_AlarmDistance);
                                }
                                tempHashMap.put("D1", tempNavPtn._Name);
                                tempHashMap.put("D2", Navigation_Dialog._DecimalFormat.format(tempNavPtn._Distance));
                                tempHashMap.put("D3", Integer.valueOf((int) tempNavPtn._Direction));
                            }
                        }
                        Navigation_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                }
                Navigation_Dialog.this._myUpdateHandler.postDelayed(Navigation_Dialog.this.myUpdateByGPSTask, 1000);
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.navigation_dialog);
        this._Dialog.Resize(1.0f, PubVar.DialogHeightRatio);
        this._Dialog.SetCaption("导航");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.btn_Nav_AddNavObj).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_Nav_OtherApp).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.btn_Nav_Clear).setOnClickListener(new ViewClick());
        this.m_DeleteICON = BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.deleteobject);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        try {
            this.m_ListViewDataItemList = new ArrayList();
            C0706Navigation tempNav = PubVar._PubCommand.GetNavigation();
            if (tempNav.getNavPointCount() > 0) {
                for (NavigatePoint tempNavPtn : tempNav.getNavPoints()) {
                    HashMap tempHashMap = new HashMap();
                    tempHashMap.put("D1", tempNavPtn._Name);
                    tempHashMap.put("D2", _DecimalFormat.format(tempNavPtn._Distance));
                    tempHashMap.put("D3", Integer.valueOf((int) tempNavPtn._Direction));
                    tempHashMap.put("D4", this.m_DeleteICON);
                    tempHashMap.put("D5", tempNavPtn);
                    this.m_ListViewDataItemList.add(tempHashMap);
                }
            }
            this.m_MyTableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.navPtn_list), "自定义", "导航点,距离(m),方位(°),操作", "text,text,text,image", new int[]{-30, -40, -15, -15}, this.pCallback);
            this.m_MyTableFactory.BindDataToListView(this.m_ListViewDataItemList, new String[]{"D1", "D2", "D3", "D4"}, this.pCallback, (int) (50.0f * PubVar.ScaledDensity));
            this.m_MyTableFactory.SetClickItemReturnColumns("0,3,");
            Common.SetCheckValueOnID(this._Dialog, R.id.cb_Nav_ShowNavLine, tempNav.m_DrawLineBool);
            Common.SetCheckValueOnID(this._Dialog, R.id.cb_Nav_ShowNavPtnInfo, tempNav.m_DrawTargetBool);
            Common.SetEditTextValueOnID(this._Dialog, R.id.et_Nav_AlarmDis, String.valueOf(tempNav.m_AlarmDistance));
            Common.GetEditTextOnID(this._Dialog, R.id.et_Nav_AlarmDis).setEnabled(tempNav.m_AlarmDistanceBool);
            CheckBox tmpAlarmCheckbox = (CheckBox) this._Dialog.findViewById(R.id.cb_Nav_AlarmNav);
            tmpAlarmCheckbox.setChecked(tempNav.m_AlarmDistanceBool);
            tmpAlarmCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.Navigation_Dialog.3
                @Override // android.widget.CompoundButton.OnCheckedChangeListener
                public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                    Common.GetEditTextOnID(Navigation_Dialog.this._Dialog, R.id.et_Nav_AlarmDis).setEnabled(arg1);
                }
            });
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        NavigatePoint tmpNavigatorPoint;
        try {
            if (command.equals("清空")) {
                Common.ShowYesNoDialog(this._Dialog.getContext(), "是否清空所有导航目标?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.Navigation_Dialog.4
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String paramString, Object pObject) {
                        if (paramString.equals("YES")) {
                            C0706Navigation tempNav = PubVar._PubCommand.GetNavigation();
                            tempNav.Clear();
                            tempNav.SetStartNavigation(false);
                            Navigation_Dialog.this.m_ListViewDataItemList.clear();
                            Navigation_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                        }
                    }
                });
            } else if (command.equals("添加导航目标")) {
                new AlertDialog.Builder(PubVar.MainContext, 3).setTitle("选择操作").setSingleChoiceItems(new String[]{"添加选中对象为导航目标", "添加图片对象为导航目标", "添加导航目标坐标"}, -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.Navigation_Dialog.5
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (arg1 == 0) {
                            Navigation_Dialog.this.DoCommand("被选对象");
                        } else if (arg1 == 1) {
                            Navigation_Dialog.this.DoCommand("图片对象");
                        } else if (arg1 == 2) {
                            Navigation_Dialog.this.DoCommand("坐标点");
                        }
                        arg0.dismiss();
                    }
                }).show();
            } else if (command.equals("被选对象")) {
                if (Common.GetSelectObjectsCount(PubVar._Map) > 0) {
                    final List<HashMap<String, Object>> tempList = Common.GetSelectObjects(PubVar._Map, -1, -1, true, new BasicValue());
                    if (tempList.size() > 0) {
                        StringBuilder tempSB = new StringBuilder();
                        int tid = 0;
                        String tempLayerID = "";
                        String tempLayerName = "";
                        for (HashMap<String, Object> tempHash : tempList) {
                            String tempStr = tempHash.get("LayerID").toString();
                            if (tempLayerID.equals("")) {
                                tempLayerID = tempStr;
                            }
                            if (!tempStr.equals(tempLayerID)) {
                                tempSB.append("图层:【");
                                tempSB.append(tempLayerName);
                                tempSB.append("】,数量:");
                                tempSB.append(tid);
                                tempSB.append("\r\n");
                                tid = 0;
                            }
                            tempLayerName = tempHash.get("LayerName").toString();
                            tempLayerID = tempStr;
                            tid++;
                        }
                        if (tid != 0) {
                            tempSB.append("图层:【");
                            tempSB.append(tempLayerName);
                            tempSB.append("】,数量:");
                            tempSB.append(tid);
                        }
                        if (tempSB.length() > 0) {
                            Common.ShowYesNoDialog(this._Dialog.getContext(), "是否将以下图层中的选择对象作为导航目标?\r\n" + tempSB.toString(), new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.Navigation_Dialog.6
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String paramString, Object pObject) {
                                    if (paramString.equals("YES")) {
                                        int tmpTid01 = 0;
                                        int tmpTid02 = 0;
                                        int tmpTid03 = 0;
                                        boolean tmpNeedRefresh = false;
                                        for (HashMap<String, Object> hashMap : tempList) {
                                            AbstractGeometry tempGeo = (AbstractGeometry) hashMap.get("Geometry");
                                            if (tempGeo != null) {
                                                NavigatePoint tmpPtn = null;
                                                if (tempGeo.GetType() == EGeoLayerType.POINT) {
                                                    tmpTid01++;
                                                    tmpPtn = new NavigatePoint();
                                                    tmpPtn.SetName("点" + tmpTid01);
                                                    tmpPtn.SetCoordinate(tempGeo.GetAllCoordinateList().get(0).Clone());
                                                } else if (tempGeo.GetType() == EGeoLayerType.POLYLINE) {
                                                    int tmpSize = tempGeo.GetAllCoordinateList().size() - 2;
                                                    if (tmpSize >= 0) {
                                                        tmpTid02++;
                                                        tmpPtn = new NavigatePoint();
                                                        tmpPtn.SetName("线" + tmpTid02);
                                                        tmpPtn.SetCoordinate(tempGeo.GetAllCoordinateList().get(tmpSize).Clone());
                                                    }
                                                } else if (tempGeo.GetType() == EGeoLayerType.POLYGON) {
                                                    tmpTid03++;
                                                    tmpPtn = new NavigatePoint();
                                                    tmpPtn.SetName("面" + tmpTid03);
                                                    tmpPtn.SetCoordinate(((Polygon) tempGeo).getInnerPoint().Clone());
                                                }
                                                if (tmpPtn != null) {
                                                    HashMap tempHashMap = new HashMap();
                                                    tempHashMap.put("D1", tmpPtn._Name);
                                                    tempHashMap.put("D2", Navigation_Dialog._DecimalFormat.format(tmpPtn._Distance));
                                                    tempHashMap.put("D3", Integer.valueOf((int) tmpPtn._Direction));
                                                    tempHashMap.put("D4", Navigation_Dialog.this.m_DeleteICON);
                                                    tempHashMap.put("D5", tmpPtn);
                                                    Navigation_Dialog.this.m_ListViewDataItemList.add(tempHashMap);
                                                    tmpNeedRefresh = true;
                                                }
                                            }
                                        }
                                        if (tmpNeedRefresh) {
                                            Navigation_Dialog.this.m_MyTableFactory.notifyDataSetChanged();
                                        }
                                    }
                                }
                            });
                            return;
                        }
                        return;
                    }
                    return;
                }
                Common.ShowDialog("没有选择任何对象.");
            } else if (command.equals("图片对象")) {
                FileSelector_Dialog tempDialog = new FileSelector_Dialog(".jpg;.png;.arm;.mp4;", true);
                Common.ShowToast("请选择采用本软件进行采集的多媒体对象(包括照片、视频、音频等文件).");
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
            } else if (command.equals("坐标点")) {
                Navigation_AddPoint_Dialog tempDialog2 = new Navigation_AddPoint_Dialog();
                tempDialog2.SetCallback(this.pCallback);
                tempDialog2.ShowDialog();
            } else if (command.equals("导航软件")) {
                int tmpSelIndex = this.m_MyTableFactory.getSelectItemIndex();
                if (tmpSelIndex > -1) {
                    HashMap<String, Object> tempHashMap = this.m_ListViewDataItemList.get(tmpSelIndex);
                    if (!(tempHashMap == null || (tmpNavigatorPoint = (NavigatePoint) tempHashMap.get("D5")) == null)) {
                        Coordinate tmpCoordinate = tmpNavigatorPoint._Coordinate;
                        double tmpLng = tmpNavigatorPoint._Coordinate.getGeoX();
                        double tmpLat = tmpNavigatorPoint._Coordinate.getGeoY();
                        if (tmpLng == 0.0d || tmpLat == 0.0d) {
                            tmpCoordinate = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tmpNavigatorPoint._Coordinate, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                        }
                        NavigateByMap(tmpCoordinate);
                        return;
                    }
                    return;
                }
                Common.ShowDialog("请先在导航目标列表中选择需要导航的目标对象!");
            }
        } catch (Exception e) {
        }
    }

    public static void NavigateByMap(Coordinate destCoordinate) {
        final Coordinate tmpMouseCoord01 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(destCoordinate.getX(), destCoordinate.getY(), destCoordinate.getZ(), new Coordinate_WGS1984());
        new AlertDialog.Builder(PubVar.MainContext, 3).setTitle("选择导航地图").setItems(new String[]{"百度地图", "高德地图"}, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.Navigation_Dialog.7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface arg0, int arg1) {
                if (arg1 == 0) {
                    try {
//                        C0321Common.NavigatorBaiduMap(PubVar.MainContext, String.valueOf(Coordinate.this.getGeoX()), String.valueOf(Coordinate.this.getGeoY()));
                    } catch (Exception ex) {
                        Common.Log("错误", ex.getMessage());
                    }
                } else if (arg1 == 1) {
//                    C0321Common.NavigatorGaodeMap(PubVar.MainContext, String.valueOf(Coordinate.this.getGeoX()), String.valueOf(Coordinate.this.getGeoY()));
                }
                arg0.dismiss();
            }
        }).show();
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.Navigation_Dialog.8
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Navigation_Dialog.this.refreshLayout();
                Navigation_Dialog.this._myUpdateHandler.postDelayed(Navigation_Dialog.this.myUpdateByGPSTask, 1000);
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
                Navigation_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
