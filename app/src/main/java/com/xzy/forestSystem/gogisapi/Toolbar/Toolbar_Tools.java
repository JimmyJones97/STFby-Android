package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.GraphicSymbolGeometry;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.ELayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer;
import  com.xzy.forestSystem.gogisapi.Carto.MapChild_Dialog;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WGS1984;
import  com.xzy.forestSystem.gogisapi.Display.ISymbol;
import  com.xzy.forestSystem.gogisapi.Display.PointSymbol;
import  com.xzy.forestSystem.gogisapi.Edit.AddPoint_Dialog;
import  com.xzy.forestSystem.gogisapi.Edit.EDrawType;
import  com.xzy.forestSystem.gogisapi.GPS.FenceSetting_Dialog;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.Geometry.Point;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.Navigation.AlarmNavigation;
import  com.xzy.forestSystem.gogisapi.Navigation.AlarmNavigationSetting_Dialog;
import  com.xzy.forestSystem.gogisapi.Tools.Goto_Dialog;
import  com.xzy.forestSystem.gogisapi.Tools.GraphicViewSetting_Dialog;
import  com.xzy.forestSystem.gogisapi.Tools.Search_Dialog;
import  com.xzy.forestSystem.gogisapi.XProject.Layer_Select_Dialog;
import com.stczh.gzforestSystem.R;
import com.xzy.forestSystem.hellocharts.animation.PieChartRotationAnimator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Toolbar_Tools extends BaseToolbar {
    private Drawable _Envi_Pro_Bg01 = null;
    private Drawable _Envi_Pro_Bg02 = null;
    private Drawable _GPSFenceBg01 = null;
    private Drawable _GPSFenceBg02 = null;
    private AlarmNavigation m_AlarmNavigation = null;
    private Button m_GPSFenceBtn = null;
    private Button m__Envi_ProBtn = null;
    private ICallback pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Tools.1
        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
        public void OnClick(String paramString, Object pObject) {
            Object[] tmpObjs;
            Object[] tmpObjs2;
            try {
                if (paramString.equals("????????????")) {
                    PubVar._MapView.m_ShutterTool.m_SelectLayers = pObject.toString();
                    PubVar._Map.RefreshRasterLayers();
                    PubVar._Map.RefreshFast();
                    HashMap tempHash = new HashMap();
                    tempHash.put("F1", PubVar._MapView.m_ShutterTool.m_SelectLayers);
                    PubVar._PubCommand.m_ProjectDB.GetProjectConfigDB().SaveUserPara("????????????", tempHash);
                } else if (paramString.equals("??????????????????")) {
                    Object[] tmpObjs3 = (Object[]) pObject;
                    if (Toolbar_Tools.this.m_AlarmNavigation != null) {
                        Toolbar_Tools.this.m_AlarmNavigation.SetAlarmLayer(String.valueOf(tmpObjs3[0]));
                        Toolbar_Tools.this.m_AlarmNavigation.SetAlarmDistance(Double.parseDouble(String.valueOf(tmpObjs3[1])));
                        Toolbar_Tools.this.m_AlarmNavigation.SetGPSRefreshInterval(Integer.parseInt(String.valueOf(tmpObjs3[2])));
                    }
                } else if (paramString.equals("??????????????????")) {
                    Coordinate tmpCoord = (Coordinate) pObject;
                    if (Toolbar_Tools.this.m_AlarmNavigation == null) {
                        Toolbar_Tools.this.m_AlarmNavigation = new AlarmNavigation();
                        PubVar._MapView.AddPaint(Toolbar_Tools.this.m_AlarmNavigation);
                    }
                    Toolbar_Tools.this.m_AlarmNavigation.SetQueryCoordinate(tmpCoord);
                } else if (paramString.equals("????????????????????????")) {
                    Object[] tmpObjs4 = (Object[]) pObject;
                    if (tmpObjs4 != null && tmpObjs4.length > 2) {
                        Coordinate tmpCoord2 = new Coordinate();
                        tmpCoord2.setX(Double.parseDouble(String.valueOf(tmpObjs4[1])));
                        tmpCoord2.setY(Double.parseDouble(String.valueOf(tmpObjs4[2])));
                        PubVar._Map.ZoomToCenter(tmpCoord2);
                        Common.ShowToast("?????????????????????.");
                    }
                } else if (paramString.equals("GoTo??????")) {
                    if (pObject != null && (tmpObjs2 = (Object[]) pObject) != null && tmpObjs2.length > 1) {
                        String tmpLabel = String.valueOf(tmpObjs2[0]);
                        Coordinate tmpCoordinate = (Coordinate) tmpObjs2[1];
                        PubVar._Map.setActualScale(25000);
                        PubVar._Map.ZoomToCenter(tmpCoordinate);
                        ISymbol m_GPSPoint = PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemPointSymbol("??????04");
                        GraphicSymbolGeometry tmpGraphicSymbolGeo = new GraphicSymbolGeometry();
                        Point tmpGeo = new Point(tmpCoordinate.getX(), tmpCoordinate.getY());
                        tmpGeo.setLabelContent(tmpLabel);
                        tmpGraphicSymbolGeo._Geoemtry = tmpGeo;
                        tmpGraphicSymbolGeo._Symbol = m_GPSPoint;
                        PubVar._MapView._GraphicLayer.AddGeometry(tmpGraphicSymbolGeo);
                        PubVar._MapView.invalidate();
                    }
                } else if (paramString.equals("????????????") && pObject != null && (tmpObjs = (Object[]) pObject) != null && tmpObjs.length > 1) {
                    String tmpLabel2 = String.valueOf(tmpObjs[0]);
                    AbstractGeometry tmpGeo2 = (AbstractGeometry) tmpObjs[1];
                    PubVar._Map.setActualScale(25000);
                    PubVar._Map.ZoomToExtend(tmpGeo2.getEnvelope().Scale(1.5d));
                    ISymbol tmpSymbol = PubVar._PubCommand.m_ConfigDB.GetSymbolManage().GetSystemSymbol("??????", tmpGeo2.GetType());
                    GraphicSymbolGeometry tmpGraphicSymbolGeo2 = new GraphicSymbolGeometry();
                    tmpGraphicSymbolGeo2._DrawMode = EDrawType.NON_EDIT_SEL;
                    tmpGeo2.setLabelContent(tmpLabel2);
                    tmpGraphicSymbolGeo2._Geoemtry = tmpGeo2;
                    tmpGraphicSymbolGeo2._Symbol = tmpSymbol;
                    PubVar._MapView._GraphicLayer.AddGeometry(tmpGraphicSymbolGeo2);
                    PubVar._MapView.invalidate();
                }
            } catch (Exception e) {
            }
        }
    };

    public Toolbar_Tools(Context context, MapView mapView) {
        super(context, mapView);
        this.m_ToolbarName = "???????????????";
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void LoadToolBar(View view, int mainLinearLayoutID) {
        super.LoadToolBar(view, mainLinearLayoutID);
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.buttonTools_Search));
        this.buttonIDs.put("Goto", Integer.valueOf((int) R.id.buttonTools_GotoMap));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.buttonTools_Measure));
        this.buttonIDs.put("?????????", Integer.valueOf((int) R.id.buttonTools_Magnifier));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.buttonTools_SplitScreen));
        this.buttonIDs.put("????????????", Integer.valueOf((int) R.id.buttonTools_SplitSetting));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.buttonTools_NewMap));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.buttonTools_NewMap2));
        this.buttonIDs.put("??????", Integer.valueOf((int) R.id.buttonTools_NewMap4));
        this.buttonIDs.put("??????????????????", Integer.valueOf((int) R.id.buttonTools_ClearWebMap));
        this.buttonIDs.put("????????????", Integer.valueOf((int) R.id.buttonTools_Blur));
        this.m_view.setOnTouchListener(this.touchListener);
        this.m_baseLayout.setOnTouchListener(this.touchListener);
        this.m_view.findViewById(R.id.buttonTools_Search).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonTools_GotoMap).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonTools_Measure).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonTools_Magnifier).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonTools_SplitScreen).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonTools_SplitSetting).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonTools_NewMap).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonTools_NewMap2).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonTools_NewMap4).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonTools_Exit2).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonTools_ClearWebMap).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonTools_Blur).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonTools_PhotoMap).setOnClickListener(this.buttonClickListener);
        this.m_GPSFenceBtn = (Button) this.m_view.findViewById(R.id.buttonTools_Fence);
        this.m_GPSFenceBtn.setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonTools_FenceSetting).setOnClickListener(this.buttonClickListener);
        this.m__Envi_ProBtn = (Button) this.m_view.findViewById(R.id.buttonTools_Envi_Pro);
        this.m__Envi_ProBtn.setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonTools_Envi_Pro_Input).setOnClickListener(this.buttonClickListener);
        this.m_view.findViewById(R.id.buttonTools_Envi_Pro_Setting).setOnClickListener(this.buttonClickListener);
        this._GPSFenceBg01 = PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.fence0248);
        this._GPSFenceBg01.setBounds(0, 0, this._GPSFenceBg01.getMinimumWidth(), this._GPSFenceBg01.getMinimumHeight());
        this._GPSFenceBg02 = PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.fence0648);
        this._GPSFenceBg02.setBounds(0, 0, this._GPSFenceBg02.getMinimumWidth(), this._GPSFenceBg02.getMinimumHeight());
        this._Envi_Pro_Bg01 = PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.envi_pro01_48);
        this._Envi_Pro_Bg01.setBounds(0, 0, this._Envi_Pro_Bg01.getMinimumWidth(), this._Envi_Pro_Bg01.getMinimumHeight());
        this._Envi_Pro_Bg02 = PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.envi_pro02_48);
        this._Envi_Pro_Bg02.setBounds(0, 0, this._Envi_Pro_Bg02.getMinimumWidth(), this._Envi_Pro_Bg02.getMinimumHeight());
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Show() {
        super.Show();
        if (PubVar._PubCommand.m_GpsLocation.IsOpenFenceAlarm) {
            this.m_GPSFenceBtn.setTag("??????????????????");
            this.m_GPSFenceBtn.setCompoundDrawables(null, this._GPSFenceBg02, null, null);
            return;
        }
        this.m_GPSFenceBtn.setTag("??????????????????");
        this.m_GPSFenceBtn.setCompoundDrawables(null, this._GPSFenceBg01, null, null);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void Hide() {
        super.Hide();
        SetButtonSelectedStatus("??????", false);
        PubVar._PubCommand.ProcessCommand("????????????");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void StartAlarmNavigation(String layerID) {
        this.m__Envi_ProBtn.setTag("?????????????????????");
        this.m__Envi_ProBtn.setCompoundDrawables(null, this._Envi_Pro_Bg01, null, null);
        if (this.m_AlarmNavigation == null) {
            this.m_AlarmNavigation = new AlarmNavigation();
            PubVar._MapView.AddPaint(this.m_AlarmNavigation);
        }
        this.m_AlarmNavigation.SetAlarmLayer(layerID);
        this.m_AlarmNavigation.SetStartNavigation(true);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Toolbar.BaseToolbar
    public void DoCommand(View view) {
        HashMap<String, String> tempHashMap;
        String tempStr;
        String tempStr2;
        Object tempObj = view.getTag();
        if (tempObj != null) {
            String command = tempObj.toString();
            if (command.equals("????????????")) {
                Hide();
                SaveConfigDB();
            } else if (command.equals("??????")) {
                Search_Dialog tmpDialog = new Search_Dialog();
                tmpDialog.SetCallback(this.pCallback);
                tmpDialog.ShowDialog();
            } else if (command.equals("GoTo")) {
                Goto_Dialog tmpDialog2 = new Goto_Dialog();
                tmpDialog2.SetCallback(this.pCallback);
                tmpDialog2.ShowDialog();
            } else if (command.equals("????????????")) {
                new GraphicViewSetting_Dialog().ShowDialog();
            } else if (command.equals("???????????????")) {
                if (!PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                    Common.ShowToast("?????????GPS??????.\r\n????????????GPS??????????????????????????????.");
                    return;
                }
                String tmpLayerID = "";
                HashMap<String, String> tempHashMap2 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_AlarmNavig_Layer");
                if (!(tempHashMap2 == null || (tempStr2 = tempHashMap2.get("F2")) == null)) {
                    tmpLayerID = tempStr2;
                }
                if (tmpLayerID.equals("")) {
                    final List<HashMap<String, Object>> tmplayersDataList = new ArrayList<>();
                    List<String> tempArray = new ArrayList<>();
                    for (GeoLayer tmpGeoLayer : PubVar._Map.getGeoLayers().getList()) {
                        if (tmpGeoLayer.getGeoTypeName().equals("???")) {
                            tempArray.add(tmpGeoLayer.getLayerName());
                            HashMap<String, Object> tempHash = new HashMap<>();
                            tempHash.put("D1", tmpGeoLayer.getLayerName());
                            tempHash.put("D2", tmpGeoLayer.getLayerID());
                            tempHash.put("D3", tmpGeoLayer.getGeoTypeName());
                            tempHash.put("layer", tmpGeoLayer);
                            tmplayersDataList.add(tempHash);
                        }
                    }
                    if (tmplayersDataList.size() > 0) {
                        new AlertDialog.Builder(PubVar.MainContext, 3).setTitle("?????????????????????:").setSingleChoiceItems((String[]) tempArray.toArray(new String[tempArray.size()]), -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Tools.2
                            @Override // android.content.DialogInterface.OnClickListener
                            public void onClick(DialogInterface arg0, int arg1) {
                                HashMap<String, Object> tempHash2 = (HashMap) tmplayersDataList.get(arg1);
                                if (tempHash2 != null) {
                                    Toolbar_Tools.this.StartAlarmNavigation(String.valueOf(tempHash2.get("D2")));
                                } else {
                                    Common.ShowDialog("?????????????????????.");
                                }
                                arg0.dismiss();
                            }
                        }).show();
                    } else {
                        Common.ShowDialog("?????????????????????????????????.");
                    }
                } else {
                    StartAlarmNavigation(tmpLayerID);
                }
            } else if (command.equals("?????????????????????")) {
                this.m__Envi_ProBtn.setTag("???????????????");
                this.m__Envi_ProBtn.setCompoundDrawables(null, this._Envi_Pro_Bg02, null, null);
                if (this.m_AlarmNavigation != null) {
                    this.m_AlarmNavigation.SetStartNavigation(false);
                }
                PubVar._MapView.RemovePaint(this.m_AlarmNavigation);
                PubVar._MapView.invalidate();
                this.m_AlarmNavigation = null;
            } else if (command.equals("???????????????????????????")) {
                AddPoint_Dialog tmpDialog3 = new AddPoint_Dialog();
                tmpDialog3.SetCallback(this.pCallback);
                tmpDialog3.ShowDialog();
            } else if (command.equals("?????????????????????")) {
                AlarmNavigationSetting_Dialog tmpDialog4 = new AlarmNavigationSetting_Dialog();
                tmpDialog4.SetCallback(this.pCallback);
                tmpDialog4.ShowDialog();
            } else if (command.equals("??????")) {
                PubVar._PubCommand.ShowToolbar("???????????????");
            } else if (command.equals("?????????")) {
                if (PubVar._PubCommand.m_Magnifer == null || !PubVar._PubCommand.m_Magnifer._IsVisible) {
                    PubVar._PubCommand.ProcessCommand("???????????????");
                    Common.ShowToast("???????????????.");
                    return;
                }
                PubVar._PubCommand.ProcessCommand("???????????????");
                Common.ShowToast("???????????????.");
            } else if (command.equals("??????")) {
                if (view.isSelected()) {
                    SetButtonSelectedStatus("??????", false);
                    PubVar._PubCommand.ProcessCommand("????????????");
                    Common.ShowToast("??????????????????.");
                    return;
                }
                SetButtonSelectedStatus("??????", true);
                PubVar._PubCommand.ProcessCommand("????????????");
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("???????????????;???????????????");
                Common.ShowToast("??????????????????.");
            } else if (command.equals("????????????")) {
                Layer_Select_Dialog tempDialog = new Layer_Select_Dialog();
                List tmpList = new ArrayList();
                tmpList.addAll(PubVar._Map.getGeoLayers().getList());
                tmpList.addAll(PubVar._Map.getVectorGeoLayers().getList());
                tmpList.addAll(PubVar._Map.getRasterLayers());
                tempDialog.SetLayersList(tmpList);
                tempDialog.SetLayerSelectType(5);
                tempDialog.SetTitle("??????????????????");
                tempDialog.SetAllowMultiSelect(true);
                if (PubVar._MapView.m_ShutterTool.m_SelectLayers.equals("") && (tempHashMap = PubVar._PubCommand.m_ProjectDB.GetProjectConfigDB().GetUserPara("????????????")) != null && (tempStr = tempHashMap.get("F1")) != null && !tempStr.equals("")) {
                    PubVar._MapView.m_ShutterTool.m_SelectLayers = tempStr;
                }
                tempDialog.SetSelectedLayers(PubVar._MapView.m_ShutterTool.m_SelectLayers);
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
            } else if (command.equals("??????????????????")) {
                final List<String> tempArray2 = FenceSetting_Dialog.GetFenceNameList();
                if (tempArray2.size() > 0) {
                    new AlertDialog.Builder(PubVar.MainContext, 3).setTitle("???????????????????????????:").setSingleChoiceItems((String[]) tempArray2.toArray(new String[tempArray2.size()]), -1, new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Tools.3
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface arg0, int arg1) {
                            Polyline tmpPoly = FenceSetting_Dialog.GetFenceBorder((String) tempArray2.get(arg1));
                            if (tmpPoly != null) {
                                PubVar._PubCommand.m_GpsLocation.IsOpenFenceAlarm = true;
                                PubVar._PubCommand.m_GpsLocation.SetFenceBorder(tmpPoly);
                                PubVar._PubCommand.m_GpsLocation.UpdateGPSTopICON();
                                Toolbar_Tools.this.m_GPSFenceBtn.setTag("??????????????????");
                                Toolbar_Tools.this.m_GPSFenceBtn.setCompoundDrawables(null, Toolbar_Tools.this._GPSFenceBg02, null, null);
                            } else {
                                Common.ShowDialog("???????????????????????????.\r\n?????????????????????????????????.");
                            }
                            arg0.dismiss();
                        }
                    }).show();
                } else {
                    Common.ShowDialog("???????????????????????????????????????.\r\n?????????????????????????????????????????????.");
                }
            } else if (command.equals("??????????????????")) {
                PubVar._PubCommand.m_GpsLocation.IsOpenFenceAlarm = false;
                PubVar._PubCommand.m_GpsLocation.UpdateGPSTopICON();
                this.m_GPSFenceBtn.setTag("??????????????????");
                this.m_GPSFenceBtn.setCompoundDrawables(null, this._GPSFenceBg01, null, null);
            } else if (command.equals("??????????????????")) {
                new FenceSetting_Dialog().ShowDialog();
            } else if (command.equals("??????")) {
                CreateChildMap(0, 0, -1, -1).getMap().Refresh();
            } else if (command.equals("??????2")) {
                if (PubVar.ScreenHeight > PubVar.ScreenWidth) {
                    int tmpW = PubVar.ScreenWidth;
                    int tmpH = PubVar.ScreenHeight / 2;
                    MapChild_Dialog tmpDialog5 = CreateChildMap(tmpW, tmpH, 0, tmpH);
                    tmpDialog5.getMap().Refresh();
                    MapChild_Dialog tmpDialog22 = CreateChildMap(tmpW, tmpH, 0, 0);
                    tmpDialog22.getMap().CloneShowSelections(tmpDialog5.getMap());
                    tmpDialog22.getMap().RefreshClone();
                    return;
                }
                int tmpW2 = PubVar.ScreenWidth / 2;
                int tmpH2 = PubVar.ScreenHeight;
                MapChild_Dialog tmpDialog6 = CreateChildMap(tmpW2, tmpH2, 0, 0);
                tmpDialog6.getMap().Refresh();
                MapChild_Dialog tmpDialog23 = CreateChildMap(tmpW2, tmpH2, tmpW2, 0);
                tmpDialog23.getMap().CloneShowSelections(tmpDialog6.getMap());
                tmpDialog23.getMap().RefreshClone();
            } else if (command.equals("??????4")) {
                int tmpW3 = PubVar.ScreenWidth / 2;
                int tmpH3 = PubVar.ScreenHeight / 2;
                MapChild_Dialog tmpDialog7 = CreateChildMap(tmpW3, tmpH3, 0, tmpH3);
                tmpDialog7.getMap().Refresh();
                MapChild_Dialog tmpDialog24 = CreateChildMap(tmpW3, tmpH3, tmpW3, tmpH3);
                tmpDialog24.getMap().CloneShowSelections(tmpDialog7.getMap());
                tmpDialog24.getMap().RefreshClone();
                MapChild_Dialog tmpDialog32 = CreateChildMap(tmpW3, tmpH3, 0, 0);
                tmpDialog32.getMap().CloneShowSelections(tmpDialog7.getMap());
                tmpDialog32.getMap().RefreshClone();
                MapChild_Dialog tmpDialog42 = CreateChildMap(tmpW3, tmpH3, tmpW3, 0);
                tmpDialog42.getMap().CloneShowSelections(tmpDialog7.getMap());
                tmpDialog42.getMap().RefreshClone();
            } else if (command.equals("??????")) {
                if (view.isSelected()) {
                    SetButtonSelectedStatus("??????", false);
                    PubVar._PubCommand.ProcessCommand("??????_????????????");
                } else {
                    SetButtonSelectedStatus("??????", true);
                    PubVar._PubCommand.ProcessCommand("??????");
                }
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("?????????????????????;???????????????;???????????????");
            } else if (command.equals("????????????")) {
                if (view.isSelected()) {
                    SetButtonSelectedStatus("??????", false);
                    PubVar._PubCommand.ProcessCommand("??????_????????????");
                } else {
                    SetButtonSelectedStatus("??????", true);
                    PubVar._PubCommand.ProcessCommand("????????????");
                }
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("?????????????????????;???????????????;???????????????");
            } else if (command.equals("??????")) {
                PubVar._PubCommand.ProcessCommand("??????");
            } else if (command.equals("????????????")) {
                Layer_Select_Dialog tempDialog2 = new Layer_Select_Dialog();
                List tmpList2 = new ArrayList();
                tmpList2.addAll(PubVar._Map.getGeoLayers().getList());
                tmpList2.addAll(PubVar._Map.getVectorGeoLayers().getList());
                tempDialog2.SetLayersList(tmpList2);
                tempDialog2.SetLayerSelectType(5);
                tempDialog2.SetAllowMultiSelect(true);
                tempDialog2.SetSelectedLayers(PubVar._MapView.m_ShutterTool.m_SelectLayers);
                tempDialog2.SetCallback(this.pCallback);
                tempDialog2.ShowDialog();
            } else if (command.equals("???????????????")) {
                if (view.isSelected()) {
                    SetButtonSelectedStatus("??????", false);
                    PubVar._PubCommand.ProcessCommand("?????????");
                } else {
                    PubVar._PubCommand.ProcessCommand("????????????");
                    SetButtonSelectedStatus("??????", true);
                }
                PubVar._PubCommand.UpdateAllToolbarStatusNotSelected("???????????????;???????????????");
            } else if (command.equals("????????????")) {
                int tempCount = PubVar._MapView._Select.GetSelectedCount();
                if (tempCount == 0) {
                    Common.ShowDialog("????????????????????????.");
                    return;
                }
                AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.m_view.getContext());
                localBuilder.setIcon(R.drawable.messageinfo);
                localBuilder.setTitle("????????????");
                localBuilder.setMessage("?????????" + tempCount + "?????????.\n????????????????????????,?????????????");
                localBuilder.setPositiveButton("??????", new DialogInterface.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.Toolbar_Tools.4
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        PubVar._PubCommand.ProcessCommand("??????_??????");
                    }
                });
                localBuilder.setNegativeButton("??????", (DialogInterface.OnClickListener) null);
                localBuilder.show();
            } else if (command.equals("????????????")) {
                PubVar._PubCommand.ProcessCommand("??????_???????????????");
                SetButtonSelectedStatus("??????", false);
            } else if (command.equals("????????????????????????")) {
                boolean tmpNeedRefresh = false;
                for (XLayer tmpLayer : PubVar._Map.getRasterLayers()) {
                    if (tmpLayer.getLayerType() == ELayerType.ONLINEMAP && ((XBaseTilesLayer) tmpLayer).ClearCurrentShow()) {
                        tmpNeedRefresh = true;
                    }
                }
                if (tmpNeedRefresh) {
                    PubVar._Map.RefreshFastRasterLayers();
                }
            } else if (command.equals("????????????")) {
                BuildPhotosMap();
            }
        }
    }

    private MapChild_Dialog CreateChildMap(int width, int height, int x, int y) {
        MapChild_Dialog tmpDialog = new MapChild_Dialog(PubVar.MainContext, PubVar._PubCommand.m_MainLayout);
        int i = 1;
        String tmpTitle = "??????" + String.valueOf(1);
        for (MapChild_Dialog mapChild_Dialog : PubVar._PubCommand.getChildMapList()) {
            mapChild_Dialog.getTitle();
            if (!tmpTitle.equals(tmpTitle)) {
                break;
            }
            i++;
            tmpTitle = "??????" + String.valueOf(i);
        }
        tmpDialog.setTitle(tmpTitle);
        if (!(width == 0 || height == 0)) {
            tmpDialog.setSize(width, height);
        }
        if (x == -1 || y == -1) {
            tmpDialog.UpdatePosition();
        } else {
            tmpDialog.UpdatePosition(x, y);
        }
        PubVar._PubCommand.getChildMapList().add(tmpDialog);
        return tmpDialog;
    }

    public void BuildPhotosMap() {
        String tmpString01;
        int tmpI;
        Coordinate tmpCoordinate;
        Bitmap tmpBitmap;
        try {
            File[] arrayOfFile = new File(String.valueOf(PubVar._PubCommand.GetCurrentProjectPath()) + "/Photo").listFiles();
            int count = arrayOfFile.length;
            if (count > 0) {
                int tmpPhotosCount = 0;
                List<Coordinate> tmpListCoordinates = new ArrayList<>();
                for (File localFile : arrayOfFile) {
                    try {
                        if (!localFile.isDirectory() && (tmpI = (tmpString01 = localFile.getAbsolutePath().toLowerCase()).indexOf(".txt")) > 0) {
                            String tmpName2 = tmpString01.substring(0, tmpI);
                            File tmpFile2 = new File(String.valueOf(tmpName2) + ".jpg");
                            if (tmpFile2.exists() && (tmpCoordinate = Common.ReadTxtFileCoord(localFile.getAbsolutePath())) != null) {
                                Coordinate tmpCoordinate2 = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(tmpCoordinate.getX(), tmpCoordinate.getY(), tmpCoordinate.getZ(), Coordinate_WGS1984.Instance());
                                Bitmap tmpICONBitmap = null;
                                File tmpFile3 = new File(String.valueOf(tmpName2) + "_scale.jpg");
                                if (tmpFile3.exists()) {
                                    tmpICONBitmap = Common.GetLocalBitmap(tmpFile3.getAbsolutePath());
                                }
                                if (tmpICONBitmap == null && (tmpBitmap = Common.GetLocalBitmap(tmpFile2.getAbsolutePath())) != null) {
                                    Bitmap tmpBitmap2 = Bitmap.createScaledBitmap(tmpBitmap, PieChartRotationAnimator.FAST_ANIMATION_DURATION, PieChartRotationAnimator.FAST_ANIMATION_DURATION, true);
                                    Canvas tmpCanvas = new Canvas(tmpBitmap2);
                                    Rect rect = tmpCanvas.getClipBounds();
                                    Paint paint = new Paint();
                                    paint.setColor(-1);
                                    paint.setStyle(Paint.Style.STROKE);
                                    paint.setStrokeWidth(5.0f);
                                    tmpCanvas.drawRect(rect, paint);
                                    if (Common.SaveImgFile(tmpFile3.getAbsolutePath(), tmpBitmap2)) {
                                        tmpICONBitmap = Common.GetLocalBitmap(tmpFile3.getAbsolutePath());
                                    }
                                }
                                if (tmpICONBitmap != null) {
                                    PointSymbol tmpPointSymbol = new PointSymbol();
                                    tmpPointSymbol.setIcon(tmpICONBitmap);
                                    GraphicSymbolGeometry tmpGraphicSymbolGeo = new GraphicSymbolGeometry();
                                    Point tmpGeo = new Point(tmpCoordinate2.getX(), tmpCoordinate2.getY());
                                    tmpGeo.setLabelContent("");
                                    tmpGraphicSymbolGeo._Geoemtry = tmpGeo;
                                    tmpGraphicSymbolGeo._Symbol = tmpPointSymbol;
                                    tmpGraphicSymbolGeo._GeometryType = "????????????";
                                    HashMap<String, Object> tmpHashMap = new HashMap<>();
                                    tmpHashMap.put("Path", tmpFile2.getAbsolutePath());
                                    tmpGraphicSymbolGeo._AttributeHashMap = tmpHashMap;
                                    PubVar._MapView._GraphicLayer.AddGeometry(tmpGraphicSymbolGeo);
                                    tmpListCoordinates.add(tmpCoordinate2);
                                    tmpPhotosCount++;
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
                if (tmpPhotosCount > 0) {
                    Common.ShowToast("??????" + String.valueOf(tmpPhotosCount) + "?????????????????????.");
                    if (tmpListCoordinates.size() > 1) {
                        Polyline tmpPolyline = new Polyline();
                        tmpPolyline.SetAllCoordinateList(tmpListCoordinates);
                        Envelope tmpEvEnvelope = tmpPolyline.CalEnvelope();
                        if (tmpEvEnvelope != null) {
                            PubVar._Map.ZoomToExtend(tmpEvEnvelope.Scale(1.2d));
                        }
                    } else if (tmpListCoordinates.size() == 1) {
                        PubVar._Map.ZoomToCenter(tmpListCoordinates.get(0));
                    }
                }
            }
        } catch (Exception e2) {
        }
    }
}
