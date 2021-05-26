package  com.xzy.forestSystem.gogisapi.XProject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import androidx.core.internal.view.SupportMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.ERasterLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.RasterLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XRasterFileLayer;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.ECoordinateSystemType;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.PMTranslate;
import  com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import  com.xzy.forestSystem.gogisapi.Encryption.DataAuthority;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.Input_Dialog;
import  com.xzy.forestSystem.gogisapi.MyControls.LayerListImgBtnListAdapter;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class RasterLayers_Manag_Dialog {
    private View.OnClickListener ViewClick;
    private XDialogTemplate _Dialog;
    private LayerListImgBtnListAdapter layerListAdapter;
    private List<HashMap<String, Object>> layersDataList;
    private ICallback m_Callback;
    private boolean m_IsSelectAllOrDe;
    private List<RasterLayer> m_LayerList;
    private RasterLayer m_SelectLayer;
    private LinearLayout m_SelectLayout;
    private ListView myListView;
    private ICallback pCallback;
    private int selectLayerIndex;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    @SuppressLint("WrongConstant")
    public RasterLayers_Manag_Dialog() {
        this._Dialog = null;
        this.m_LayerList = null;
        this.m_SelectLayer = null;
        this.myListView = null;
        this.layersDataList = new ArrayList();
        this.layerListAdapter = null;
        this.selectLayerIndex = -1;
        this.m_Callback = null;
        this.m_IsSelectAllOrDe = false;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Manag_Dialog.1
            @SuppressLint("WrongConstant")
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, final Object pObject) {
                Object[] tmpObjs;
                PMTranslate tmpPMTranslate;
                String[] tempStrs;
                int tempLayerIndex;
                try {
                    if (paramString.equals("添加网络地图")) {
                        RasterLayers_Manag_Dialog.this.m_LayerList.add(0, (RasterLayer) pObject);
                        RasterLayers_Manag_Dialog.this.LoadLayersInfo();
                    } else if (paramString.contains("图层列表")) {
                        boolean tempBoolTag = false;
                        if (RasterLayers_Manag_Dialog.this.m_IsSelectAllOrDe) {
                            RasterLayers_Manag_Dialog.this.m_IsSelectAllOrDe = false;
                            String[] tempStrs2 = paramString.split(",");
                            if (tempStrs2 == null || tempStrs2.length <= 2) {
                                RasterLayers_Manag_Dialog.this.layerListAdapter.ClearSelectIndex();
                                RasterLayers_Manag_Dialog.this.layerListAdapter.notifyDataSetChanged();
                            } else {
                                String tmpCommand = tempStrs2[1];
                                if (tmpCommand.equals("是否选择") && tempStrs2.length > 4) {
                                    boolean tempBool = Boolean.parseBoolean(tempStrs2[4]);
                                    for (Integer num : RasterLayers_Manag_Dialog.this.layerListAdapter.GetSelectList()) {
                                        int tempI = num.intValue();
                                        if (tempI < RasterLayers_Manag_Dialog.this.layersDataList.size()) {
                                            ((HashMap) RasterLayers_Manag_Dialog.this.layersDataList.get(tempI)).put("D1", Boolean.valueOf(tempBool));
                                        }
                                    }
                                    RasterLayers_Manag_Dialog.this.layerListAdapter.ClearSelectIndex();
                                    RasterLayers_Manag_Dialog.this.layerListAdapter.notifyDataSetChanged();
                                } else if (tmpCommand.equals("删除")) {
                                    StringBuilder tempSB = new StringBuilder();
                                    for (Integer num2 : RasterLayers_Manag_Dialog.this.layerListAdapter.GetSelectList()) {
                                        int tempI2 = num2.intValue();
                                        if (tempI2 < RasterLayers_Manag_Dialog.this.layersDataList.size()) {
                                            tempSB.append("【");
                                            tempSB.append(((HashMap) RasterLayers_Manag_Dialog.this.layersDataList.get(tempI2)).get("D2").toString());
                                            tempSB.append("】\r\n");
                                        }
                                    }
                                    if (tempSB.length() > 0) {
                                        Common.ShowYesNoDialog(RasterLayers_Manag_Dialog.this._Dialog.getContext(), "是否删除以下图层:\r\n" + tempSB.toString(), new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Manag_Dialog.1.1
                                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                            public void OnClick(String paramString2, Object pObject2) {
                                                int tmpIndex;
                                                if (paramString2.equals("YES")) {
                                                    for (Integer num3 : RasterLayers_Manag_Dialog.this.layerListAdapter.GetSelectList()) {
                                                        int tempI3 = num3.intValue();
                                                        if (tempI3 < RasterLayers_Manag_Dialog.this.layersDataList.size() && (tmpIndex = RasterLayers_Manag_Dialog.this.findLayerIndexInDataListByID(((HashMap) RasterLayers_Manag_Dialog.this.layersDataList.get(tempI3)).get("LayerID").toString())) >= 0) {
                                                            ((RasterLayer) RasterLayers_Manag_Dialog.this.m_LayerList.get(tmpIndex)).SetEditMode(EEditMode.DELETE);
                                                            RasterLayers_Manag_Dialog.this.m_SelectLayer = null;
                                                        }
                                                    }
                                                    RasterLayers_Manag_Dialog.this.layerListAdapter.ClearSelectIndex();
                                                    RasterLayers_Manag_Dialog.this.LoadLayersInfo();
                                                }
                                            }
                                        });
                                    } else {
                                        tempBoolTag = true;
                                    }
                                } else {
                                    RasterLayers_Manag_Dialog.this.layerListAdapter.ClearSelectIndex();
                                    RasterLayers_Manag_Dialog.this.layerListAdapter.notifyDataSetChanged();
                                }
                            }
                        } else {
                            tempBoolTag = true;
                        }
                        if (tempBoolTag && (tempStrs = paramString.split(",")) != null && tempStrs.length > 2 && (tempLayerIndex = Integer.parseInt(tempStrs[2])) >= 0 && tempLayerIndex < RasterLayers_Manag_Dialog.this.m_LayerList.size()) {
                            RasterLayers_Manag_Dialog.this.m_SelectLayer = RasterLayers_Manag_Dialog.this.findRasterLayerByLayerID(String.valueOf(((HashMap) RasterLayers_Manag_Dialog.this.layersDataList.get(tempLayerIndex)).get("LayerID")));
                            RasterLayers_Manag_Dialog.this.DoCommand(tempStrs[1]);
                        }
                    } else if (paramString.equals("确定")) {
                        Common.ShowProgressDialog("正在保存图层设置...", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Manag_Dialog.1.2
                            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                            public void OnClick(String paramString2, Object pObject2) {
                                try {
                                    if (RasterLayers_Manag_Dialog.this.SaveLayerInfo()) {
                                        if (RasterLayers_Manag_Dialog.this.m_Callback != null) {
                                            RasterLayers_Manag_Dialog.this.m_Callback.OnClick("栅格图层管理", null);
                                        }
                                        PubVar._Map.UpdateRasterLayersVisibleList();
                                        if (PubVar.m_AuthorizeTools.getRegisterMode() == 0) {
                                            Common.ShowDialog("尊敬的用户：\r\n        【公共版】只能显示最后1个栅格图层.为保证您能使用本软件的全部功能，请获取正式授权码！\r\n详见【关于系统】！");
                                        }
                                        RasterLayers_Manag_Dialog.this._Dialog.dismiss();
                                    }
                                    if (pObject2 != null && (pObject2 instanceof Handler)) {
                                        Handler tmpHandler = (Handler) pObject2;
                                        Message tmpMsg = tmpHandler.obtainMessage();
                                        tmpMsg.what = 0;
                                        tmpHandler.sendMessage(tmpMsg);
                                    }
                                } catch (Exception e) {
                                    if (pObject2 != null && (pObject2 instanceof Handler)) {
                                        Handler tmpHandler2 = (Handler) pObject2;
                                        Message tmpMsg2 = tmpHandler2.obtainMessage();
                                        tmpMsg2.what = 0;
                                        tmpHandler2.sendMessage(tmpMsg2);
                                    }
                                } catch (Throwable th) {
                                    if (pObject2 != null && (pObject2 instanceof Handler)) {
                                        Handler tmpHandler3 = (Handler) pObject2;
                                        Message tmpMsg3 = tmpHandler3.obtainMessage();
                                        tmpMsg3.what = 0;
                                        tmpHandler3.sendMessage(tmpMsg3);
                                    }
                                    throw th;
                                }
                            }
                        });
                    } else if (paramString.contains("选择文件") && pObject != null) {
                        String[] tempStrs3 = pObject.toString().split(";");
                        if (tempStrs3 != null && tempStrs3.length > 1) {
                            StringBuilder tempSB2 = new StringBuilder();
                            int count = tempStrs3.length / 2;
                            int tmpNeedCode = 0;
                            int tmpNeedInputCode = 0;
                            for (int i = 0; i < count; i++) {
                                String tempPath = tempStrs3[(i * 2) + 1];
                                File tempDBFile = new File(tempPath);
                                if (tempDBFile.exists()) {
                                    DataAuthority tmpAuth = new DataAuthority(1);
                                    BasicValue tmpOutMsg = new BasicValue();
                                    if (tmpAuth.Initial(tempPath, "", tmpOutMsg)) {
                                        @SuppressLint("WrongConstant") Cursor pCursor = SQLiteDatabase.openDatabase(tempDBFile.getAbsolutePath(), null, 1).rawQuery("Select CoorType From MapInfo", null);
                                        if (pCursor.moveToNext()) {
                                            AbstractC0383CoordinateSystem tmpCoorSystem = AbstractC0383CoordinateSystem.CreateCoordinateSystem(pCursor.getString(0));
                                            if (!tmpCoorSystem.GetName().equals(PubVar.m_Workspace.GetCoorSystem().GetName())) {
                                                tempSB2.append("【栅格文件】:\r\n  ");
                                                tempSB2.append(tempPath);
                                                tempSB2.append("\r\n坐标系为:[" + tmpCoorSystem.GetName() + "],与项目坐标系不一致.\r\n");
                                            }
                                        }
                                    } else {
                                        tmpNeedCode++;
                                        tempSB2.append("【栅格文件】:\r\n  ");
                                        tempSB2.append(tempPath);
                                        tempSB2.append("\r\n");
                                        tempSB2.append(tmpOutMsg.getString());
                                        tempSB2.append("\r\n");
                                        if (tmpAuth.NeedPwdAccess()) {
                                            tmpNeedInputCode++;
                                        }
                                    }
                                }
                            }
                            if (tempSB2.length() == 0) {
                                RasterLayers_Manag_Dialog.this.pCallback.OnClick("添加本地栅格文件", pObject);
                            } else if (tmpNeedCode == 0) {
                                Common.ShowYesNoDialog(RasterLayers_Manag_Dialog.this._Dialog.getContext(), "坐标系不一致,是否继续添加?\r\n当前项目的坐标系为:" + PubVar.m_Workspace.GetCoorSystem().GetName() + "\r\n" + tempSB2.toString(), new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Manag_Dialog.1.3
                                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                    public void OnClick(String paramString2, Object pObject02) {
                                        if (paramString2.equals("YES")) {
                                            RasterLayers_Manag_Dialog.this.pCallback.OnClick("添加本地栅格文件", pObject);
                                        }
                                    }
                                });
                            } else if (tmpNeedInputCode == 1 && count == 1) {
                                if (RasterLayers_Manag_Dialog.this.findLayerIndexInDataListByID2(tempStrs3[1]) == -1) {
                                    Input_Dialog tmpDialog = new Input_Dialog();
                                    tmpDialog.SetReturnTag("输入栅格数据文件密码返回");
                                    tmpDialog.SetCallback(RasterLayers_Manag_Dialog.this.pCallback);
                                    tmpDialog.setValues("输入密码", "密码: ", "", "提示:请输入栅格数据文件的加密密码.");
                                    tmpDialog.setInputType(129);
                                    tmpDialog.SetTagValue(tempStrs3[1]);
                                    tmpDialog.ShowDialog();
                                    return;
                                }
                                tempSB2.append("栅格图层:" + tempStrs3[1] + " 已经添加.\r\n");
                            } else if (tmpNeedInputCode > 1) {
                                tempSB2.append("存在多个需要输入密码授权的数据文件,请单个文件添加.");
                                Common.ShowDialog(tempSB2.toString());
                            } else {
                                tempSB2.append("请单个文件添加.");
                                Common.ShowDialog(tempSB2.toString());
                            }
                        }
                    } else if (paramString.equals("输入参数")) {
                        Object[] tmpObjs2 = (Object[]) pObject;
                        if (tmpObjs2 != null && tmpObjs2.length > 1 && String.valueOf(tmpObjs2[0]).equals("输入栅格数据文件密码返回") && tmpObjs2.length > 2) {
                            String tmpSelectedFile = String.valueOf(tmpObjs2[2]);
                            if (new File(tmpSelectedFile).exists()) {
                                String tmpKey = String.valueOf(tmpObjs2[1]);
                                DataAuthority tmpAuth2 = new DataAuthority(1);
                                BasicValue tmpOutMsg2 = new BasicValue();
                                if (tmpAuth2.Initial(tmpSelectedFile, tmpKey, tmpOutMsg2)) {
                                    RasterLayer tempLayer = new RasterLayer();
                                    tempLayer.SetEditMode(EEditMode.NEW);
                                    tempLayer.SetLayerName(tmpSelectedFile.substring(tmpSelectedFile.lastIndexOf(FileSelector_Dialog.sRoot) + 1));
                                    tempLayer.setPassowrd(tmpKey);
                                    tempLayer.SetFilePath(tmpSelectedFile);
                                    tempLayer.SetLayerTypeName("文件");
                                    RasterLayers_Manag_Dialog.this.m_LayerList.add(tempLayer);
                                    RasterLayers_Manag_Dialog.this.LoadLayersInfo();
                                    return;
                                }
                                Common.ShowDialog(tmpOutMsg2.getString());
                            }
                        }
                    } else if (paramString.contains("添加本地栅格文件") && pObject != null) {
                        String[] tempStrs4 = pObject.toString().split(";");
                        if (tempStrs4 != null && tempStrs4.length > 1) {
                            StringBuilder tempSB3 = new StringBuilder();
                            int count2 = tempStrs4.length / 2;
                            boolean tempBool2 = false;
                            for (int i2 = 0; i2 < count2; i2++) {
                                String tempPath2 = tempStrs4[(i2 * 2) + 1];
                                if (RasterLayers_Manag_Dialog.this.findLayerIndexInDataListByID2(tempPath2) == -1) {
                                    RasterLayer tempLayer2 = new RasterLayer();
                                    tempLayer2.SetEditMode(EEditMode.NEW);
                                    tempLayer2.SetLayerName(tempStrs4[i2 * 2]);
                                    tempLayer2.SetFilePath(tempPath2);
                                    tempLayer2.SetLayerTypeName("文件");
                                    RasterLayers_Manag_Dialog.this.m_LayerList.add(0, tempLayer2);
                                    tempBool2 = true;
                                } else {
                                    tempSB3.append("栅格图层:" + tempPath2 + " 已经添加.\r\n");
                                }
                            }
                            if (tempBool2) {
                                RasterLayers_Manag_Dialog.this.LoadLayersInfo();
                            }
                            if (tempSB3.length() > 0) {
                                Common.ShowDialog(tempSB3.toString());
                            }
                        }
                    } else if (paramString.equals("平面参数转换返回")) {
                        if (!(pObject == null || (tmpObjs = (Object[]) pObject) == null || tmpObjs.length <= 1 || (tmpPMTranslate = (PMTranslate) tmpObjs[0]) == null || RasterLayers_Manag_Dialog.this.m_SelectLayer == null)) {
                            RasterLayers_Manag_Dialog.this.m_SelectLayer.setOffsetX(tmpPMTranslate.GetTransToP31());
                            RasterLayers_Manag_Dialog.this.m_SelectLayer.setOffsetY(tmpPMTranslate.GetTransToP32());
                            boolean tmpBoolTrans = ((Boolean) tmpObjs[1]).booleanValue();
                            if (RasterLayers_Manag_Dialog.this.m_SelectLayer.getIsConsiderTranslate() != tmpBoolTrans && tmpBoolTrans) {
                                Common.ShowDialog("由于图层【" + RasterLayers_Manag_Dialog.this.m_SelectLayer.GetLayerName() + "】的坐标系为" + RasterLayers_Manag_Dialog.this.m_SelectLayer.GetCoorSystem() + ",当前项目的坐标系为" + PubVar.m_Workspace.GetCoorSystem().GetName() + ",设置坐标系转换时非对应投影带区域将可能存在较大变形.");
                            }
                            RasterLayers_Manag_Dialog.this.m_SelectLayer.setIsConsiderTranslate(tmpBoolTrans);
                        }
                    } else if (paramString.equals("更多工具")) {
                        if (RasterLayers_Manag_Dialog.this.m_SelectLayout.getVisibility() == 0) {
                            RasterLayers_Manag_Dialog.this.m_SelectLayout.setVisibility(8);
                        } else {
                            RasterLayers_Manag_Dialog.this.m_SelectLayout.setVisibility(0);
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        this.m_SelectLayout = null;
        this.ViewClick = new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Manag_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (view.getTag() != null) {
                    RasterLayers_Manag_Dialog.this.DoCommand(view.getTag().toString());
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.rasterlayers_manag_dialog);
        this._Dialog.Resize(1.0f, PubVar.DialogHeightRatio);
        this._Dialog.SetCaption("栅格图层管理");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.myListView = (ListView) this._Dialog.findViewById(R.id.listViewRasterLayers);
        if (this.myListView != null) {
            int tmpMinWidth = (int) (400.0f * PubVar.ScaledDensity);
            if (tmpMinWidth < PubVar.ScreenWidth) {
                this.myListView.getLayoutParams().width = (int) (((float) PubVar.ScreenWidth) * 0.96f);
            } else {
                this.myListView.getLayoutParams().width = tmpMinWidth;
            }
        }
        this.m_SelectLayout = (LinearLayout) this._Dialog.findViewById(R.id.ll_selectOrDe);
        this.m_SelectLayout.setVisibility(8);
        this._Dialog.findViewById(R.id.buttonAddLocal).setOnClickListener(this.ViewClick);
        this._Dialog.findViewById(R.id.buttonAddLocal).setTag("添加本地栅格图层");
        this._Dialog.findViewById(R.id.btn_expandMore).setOnClickListener(this.ViewClick);
        this._Dialog.findViewById(R.id.buttonAddNet).setOnClickListener(this.ViewClick);
        this._Dialog.findViewById(R.id.buttonAddNet).setTag("添加网络地图");
        this._Dialog.findViewById(R.id.buttonSelectAll).setOnClickListener(this.ViewClick);
        this._Dialog.findViewById(R.id.buttonSelectDe).setOnClickListener(this.ViewClick);
        this.myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Manag_Dialog.3
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
                RasterLayers_Manag_Dialog.this.layerListAdapter.AddOrRemoveSelectIndex(arg2);
                RasterLayers_Manag_Dialog.this.layerListAdapter.notifyDataSetChanged();
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void LoadLayersInfo() {
        if (this.m_LayerList != null) {
            List<HashMap<String, Object>> tempLayersDatalist = new ArrayList<>();
            for (RasterLayer tempLayer : this.m_LayerList) {
                if (tempLayer.GetEditMode() != EEditMode.DELETE) {
                    boolean tempTag01 = true;
                    int tempIndex = findLayerIndexInDataListByID(tempLayer.GetLayerID());
                    if (tempIndex >= 0) {
                        tempLayersDatalist.add(this.layersDataList.get(tempIndex));
                        tempTag01 = false;
                    }
                    if (tempTag01) {
                        HashMap<String, Object> tempHash = new HashMap<>();
                        tempHash.put("LayerID", tempLayer.GetLayerID());
                        tempHash.put("TempUID", UUID.randomUUID().toString());
                        if (tempLayer.GetLayerType() == ERasterLayerType.FromFile) {
                            tempHash.put("LayerID2", tempLayer.GetFilePath());
                            XLayer tmpLayer = PubVar._Map.GetRasterLayerByID(tempLayer.GetLayerID());
                            if (tmpLayer != null && (tmpLayer instanceof XRasterFileLayer) && !((XRasterFileLayer) tmpLayer).IsEnable()) {
                                tempHash.put("Bg_Color", Integer.valueOf((int) SupportMenu.CATEGORY_MASK));
                            }
                        } else {
                            tempHash.put("LayerID2", tempLayer.GetLayerName());
                        }
                        tempHash.put("D1", Boolean.valueOf(tempLayer.GetVisible()));
                        tempHash.put("D2", tempLayer.GetLayerName());
                        tempHash.put("D3", Integer.valueOf(tempLayer.GetTransparet()));
                        tempLayersDatalist.add(tempHash);
                    }
                }
            }
            this.layersDataList = tempLayersDatalist;
            updateListViewData();
        }
    }

    private int findLayerIndexInDataList(String TempUID) {
        if (this.layersDataList == null || this.layersDataList.size() <= 0) {
            return -1;
        }
        int tempCount = this.layersDataList.size();
        for (int i = 0; i < tempCount; i++) {
            if (this.layersDataList.get(i).get("TempUID").equals(TempUID)) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int findLayerIndexInDataListByID(String layerID) {
        if (this.layersDataList == null || this.layersDataList.size() <= 0) {
            return -1;
        }
        int tempCount = this.layersDataList.size();
        for (int i = 0; i < tempCount; i++) {
            if (this.layersDataList.get(i).get("LayerID").equals(layerID)) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int findLayerIndexInDataListByID2(String filepath) {
        if (this.layersDataList == null || this.layersDataList.size() <= 0) {
            return -1;
        }
        int tempCount = this.layersDataList.size();
        for (int i = 0; i < tempCount; i++) {
            if (this.layersDataList.get(i).get("LayerID2").equals(filepath)) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private RasterLayer findRasterLayerByLayerID(String layerID) {
        if (this.m_LayerList != null && this.m_LayerList.size() > 0) {
            for (RasterLayer tmpRasterLayer : this.m_LayerList) {
                if (layerID.equals(tmpRasterLayer.GetLayerID())) {
                    return tmpRasterLayer;
                }
            }
        }
        return null;
    }

    private void updateListViewData() {
        if (this.myListView != null) {
            if (this.layersDataList == null || this.layersDataList.size() <= 0) {
                this.layerListAdapter = new LayerListImgBtnListAdapter(this.myListView.getContext(), 1, this.layersDataList, R.layout.layerlistimgbtn_layout);
                this.myListView.setAdapter((ListAdapter) this.layerListAdapter);
                return;
            }
            this.layerListAdapter = new LayerListImgBtnListAdapter(this.myListView.getContext(), 1, this.layersDataList, R.layout.layerlistimgbtn_layout);
            this.layerListAdapter.SetCallback(this.pCallback);
            this.myListView.setAdapter((ListAdapter) this.layerListAdapter);
            if (this.selectLayerIndex >= 0) {
                this.layerListAdapter.SetSelectItemIndex(this.selectLayerIndex);
            }
            this.layerListAdapter.SetNeedCheckBoxCheckReturn(true);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    @SuppressLint("WrongConstant")
    private void DoCommand(String command) {
        try {
            this.m_IsSelectAllOrDe = false;
            if (command.equals("添加网络地图")) {
                RasterLayers_Select_Dialog tempDialog = new RasterLayers_Select_Dialog();
                tempDialog.SetLayerList(this.m_LayerList);
                tempDialog.SetCallback(this.pCallback);
                tempDialog.ShowDialog();
            } else if (command.equals("添加本地栅格图层")) {
                FileSelector_Dialog tempDialog2 = new FileSelector_Dialog(".rxd;.imx;.db;", true);
                Common.ShowToast("请选择RXD等数据文件.");
                tempDialog2.SetCallback(this.pCallback);
                tempDialog2.ShowDialog();
            } else if (command.equals("全选")) {
                this.m_IsSelectAllOrDe = true;
                int tempCount = this.layersDataList.size();
                for (int i = 0; i < tempCount; i++) {
                    this.layerListAdapter.AddSelectIndex(i);
                }
                this.layerListAdapter.notifyDataSetChanged();
                return;
            } else if (command.equals("反选")) {
                this.m_IsSelectAllOrDe = true;
                int count = this.layersDataList.size();
                for (int i2 = 0; i2 < count; i2++) {
                    this.layerListAdapter.AddOrRemoveSelectIndex(i2);
                }
                this.layerListAdapter.notifyDataSetChanged();
                return;
            } else if (command.equals("更多工具")) {
                if (this.m_SelectLayout.getVisibility() == 0) {
                    this.m_SelectLayout.setVisibility(8);
                    return;
                } else {
                    this.m_SelectLayout.setVisibility(0);
                    return;
                }
            }
            if (this.m_SelectLayer != null) {
                if (command.equals("向上") || command.equals("向下")) {
                    HashMap<String, Object> tempLayerHash = null;
                    int tempLayerIndex = -1;
                    int tempCount2 = this.layersDataList.size();
                    int i3 = 0;
                    while (true) {
                        if (i3 >= tempCount2) {
                            break;
                        }
                        HashMap<String, Object> tmpHashMap = this.layersDataList.get(i3);
                        if (tmpHashMap.get("D2").equals(this.m_SelectLayer.GetLayerName())) {
                            tempLayerIndex = i3;
                            tempLayerHash = tmpHashMap;
                            break;
                        }
                        i3++;
                    }
                    if (tempLayerIndex == -1) {
                        return;
                    }
                    if (tempLayerIndex == 0 && command.toString().equals("向上")) {
                        Common.ShowToast("已经在最上层！");
                    } else if (tempLayerIndex != tempCount2 - 1 || !command.toString().equals("向下")) {
                        if (command.toString().equals("向上")) {
                            tempLayerIndex--;
                        } else if (command.toString().equals("向下")) {
                            tempLayerIndex++;
                        }
                        this.layersDataList.remove(tempLayerHash);
                        this.layersDataList.add(tempLayerIndex, tempLayerHash);
                        this.m_LayerList.remove(this.m_SelectLayer);
                        this.m_LayerList.add(tempLayerIndex, this.m_SelectLayer);
                        this.selectLayerIndex = tempLayerIndex;
                        updateListViewData();
                    } else {
                        Common.ShowToast("已经在最下层！");
                    }
                } else if (command.equals("删除")) {
                    Common.ShowYesNoDialog(this._Dialog.getContext(), "是否删除以下图层?\r\n【" + this.m_SelectLayer.GetLayerName() + "】", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Manag_Dialog.4
                        @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                        public void OnClick(String paramString, Object pObject) {
                            if (paramString.equals("YES")) {
                                ((RasterLayer) RasterLayers_Manag_Dialog.this.m_LayerList.get(RasterLayers_Manag_Dialog.this.m_LayerList.indexOf(RasterLayers_Manag_Dialog.this.m_SelectLayer))).SetEditMode(EEditMode.DELETE);
                                RasterLayers_Manag_Dialog.this.m_SelectLayer = null;
                                RasterLayers_Manag_Dialog.this.LoadLayersInfo();
                            }
                        }
                    });
                } else if (command.equals("图层") && this.m_SelectLayer != null) {
                    PMTranslate_Setting_Dialog tempDialog3 = new PMTranslate_Setting_Dialog();
                    tempDialog3.SetCallback(this.pCallback);
                    tempDialog3.SetTitle("图层参数校正");
                    PMTranslate tmpPMTranslate = new PMTranslate("三参转换");
                    tmpPMTranslate.SetTransToP31(String.valueOf(this.m_SelectLayer._OffsetX));
                    tmpPMTranslate.SetTransToP32(String.valueOf(this.m_SelectLayer._OffsetY));
                    tempDialog3.SetPMTranslate(tmpPMTranslate);
                    if (this.m_SelectLayer.GetLayerType() != ERasterLayerType.FromFile) {
                        AbstractC0383CoordinateSystem tmpWorkCoordinateSystem = PubVar.m_Workspace.GetCoorSystem();
                        if (!(tmpWorkCoordinateSystem.GetCoordinateSystemType() == ECoordinateSystemType.enWGS1984 || tmpWorkCoordinateSystem.GetCoordinateSystemType() == ECoordinateSystemType.enWebMercator)) {
                            tempDialog3.setConsideTranslateVisible(true, this.m_SelectLayer.getIsConsiderTranslate());
                        }
                    }
                    tempDialog3.ShowDialog();
                }
            }
        } catch (Exception e) {
        }
    }

    public static void RemoveRasterLayer() {
        PubVar._Map.getRasterLayers();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean SaveLayerInfo() {
        HashMap<String, Object> tempHashMap;
        XLayer tempLayer2;
        Envelope tmpNewZoomExtend = null;
        boolean result = false;
        int tempI = 0;
        try {
            int count = this.m_LayerList.size();
            this.layersDataList.size();
            int i = 0;
            while (i < count) {
                RasterLayer tempLayer = this.m_LayerList.get(i);
                if (tempLayer.GetEditMode() == EEditMode.DELETE) {
                    if (PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL("delete from T_RasterLayer where Name='" + tempLayer.GetLayerName() + "'")) {
                        tempLayer.SetEditMode(EEditMode.NONE);
                        PubVar._Map.RemoveRasterLayer(tempLayer.GetLayerID());
                        this.m_LayerList.remove(i);
                    }
                    i--;
                    count--;
                } else {
                    if (tempLayer.GetEditMode() == EEditMode.NEW) {
                        PubVar._PubCommand.m_ProjectDB.GetRasterLayerManage().RenderLayerForAdd(tempLayer);
                        if (tempLayer.GetLayerType() == ERasterLayerType.FromFile && (tempLayer2 = PubVar._Map.GetRasterLayerByID(tempLayer.GetLayerID())) != null) {
                            tmpNewZoomExtend = tempLayer2.getExtend();
                        }
                    }
                    tempLayer.SetEditMode(EEditMode.NONE);
                    tempLayer.SetLayerIndex(tempI);
                    PubVar._Map.MoveRasterLayerTo(tempLayer.GetLayerID(), tempI);
                    tempI++;
                    int tempLyIndex = findLayerIndex(tempLayer.GetLayerID());
                    if (tempLyIndex >= 0 && (tempHashMap = this.layersDataList.get(tempLyIndex)) != null) {
                        boolean tempBool = Boolean.parseBoolean(tempHashMap.get("D1").toString());
                        tempLayer.SetVisible(tempBool);
                        int tempTransp = Integer.parseInt(tempHashMap.get("D3").toString());
                        tempLayer.SetTransparent(tempTransp);
                        XLayer tempLayer22 = PubVar._Map.GetRasterLayerByID(tempLayer.GetLayerID());
                        if (tempLayer22 != null) {
                            tempLayer22.setVisible(tempBool);
                            tempLayer22.setTransparent(tempTransp);
                            if (tempLayer22 instanceof XBaseTilesLayer) {
                                ((XBaseTilesLayer) tempLayer22).setOffsetX(tempLayer._OffsetX);
                                ((XBaseTilesLayer) tempLayer22).setOffsetY(tempLayer._OffsetY);
                                ((XBaseTilesLayer) tempLayer22).setIsConsiderTranslate(tempLayer.getIsConsiderTranslate());
                            } else if (tempLayer22 instanceof XRasterFileLayer) {
                                ((XRasterFileLayer) tempLayer22).setOffsetX(tempLayer._OffsetX);
                                ((XRasterFileLayer) tempLayer22).setOffsetY(tempLayer._OffsetY);
                            }
                        }
                    }
                    tempLayer.SaveLayerInfo();
                }
                i++;
            }
            PubVar._PubCommand.m_ProjectDB.GetRasterLayerManage().SaveLayerFormLayerList(this.m_LayerList);
            result = true;
            if (tmpNewZoomExtend != null) {
                Common.ShowYesNoDialog(this._Dialog.getContext(), "添加了新的图层,是否缩放到该图层全局范围内?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Manag_Dialog.5
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command, Object pObject) {
                        if (null == null || !command.equals("YES")) {
                            PubVar._Map.Refresh();
                        } else {
                            PubVar._Map.ZoomToExtend(null);
                        }
                    }
                });
            } else {
                PubVar._Map.Refresh();
            }
        } catch (Exception ex) {
            Common.Log("SaveLayerInfo", "错误:" + ex.toString() + "-->" + ex.getMessage());
            if (0 != 0) {
                Common.ShowYesNoDialog(this._Dialog.getContext(), "添加了新的图层,是否缩放到该图层全局范围内?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Manag_Dialog.5
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command, Object pObject) {
                        if (null == null || !command.equals("YES")) {
                            PubVar._Map.Refresh();
                        } else {
                            PubVar._Map.ZoomToExtend(null);
                        }
                    }
                });
            } else {
                PubVar._Map.Refresh();
            }
        } catch (Throwable th) {
            if (0 != 0) {
                Common.ShowYesNoDialog(this._Dialog.getContext(), "添加了新的图层,是否缩放到该图层全局范围内?", new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Manag_Dialog.5
                    @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                    public void OnClick(String command, Object pObject) {
                        if (null == null || !command.equals("YES")) {
                            PubVar._Map.Refresh();
                        } else {
                            PubVar._Map.ZoomToExtend(null);
                        }
                    }
                });
            } else {
                PubVar._Map.Refresh();
            }
            throw th;
        }
        return result;
    }

    private int findLayerIndex(String layerID) {
        int i = -1;
        for (HashMap<String, Object> tempHash : this.layersDataList) {
            i++;
            if (tempHash != null && tempHash.get("LayerID").toString().equals(layerID)) {
                return i;
            }
        }
        return -1;
    }

    public void ShowDialog() {
        this.m_LayerList = PubVar._PubCommand.m_ProjectDB.GetRasterLayerManage().CopyLayerList();
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.RasterLayers_Manag_Dialog.6
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                RasterLayers_Manag_Dialog.this.LoadLayersInfo();
            }
        });
        this._Dialog.show();
    }
}
