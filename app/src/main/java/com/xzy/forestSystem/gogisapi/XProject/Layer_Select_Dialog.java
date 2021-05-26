package  com.xzy.forestSystem.gogisapi.XProject;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.ELayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XRasterFileLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.MyTableFactory;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Layer_Select_Dialog {
    private List<XLayer> _AllLayersList;
    private XDialogTemplate _Dialog;
    private int _LayerSelectType;
    private Object _TAGObject;
    private boolean m_AllowMultiSelect;
    private ICallback m_Callback;
    private List<HashMap<String, Object>> m_MyTableDataList;
    private HashMap<String, Object> m_SelectItem;
    private String m_SelectLayersID;
    private MyTableFactory m_TableFactory;
    private ICallback pCallback;

    public void SetCallback(ICallback tmpICallback) {
        this.m_Callback = tmpICallback;
    }

    public Layer_Select_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_TableFactory = null;
        this.m_MyTableDataList = null;
        this.m_SelectItem = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Select_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                HashMap tmpHash;
                try {
                    if (paramString.equals("列表选项")) {
                        Layer_Select_Dialog.this.m_SelectItem = (HashMap) pObject;
                    } else if (paramString.equals("单击选择行")) {
                        if (!(Layer_Select_Dialog.this.m_AllowMultiSelect || (tmpHash = (HashMap) pObject) == null)) {
                            Layer_Select_Dialog.this.m_SelectItem = tmpHash;
                            Layer_Select_Dialog.this.pCallback.OnClick("确定", null);
                        }
                    } else if (paramString.equals("确定")) {
                        if (Layer_Select_Dialog.this._LayerSelectType == 5) {
                            StringBuilder tempSB = new StringBuilder();
                            for (HashMap<String, Object> temphash : Layer_Select_Dialog.this.m_MyTableDataList) {
                                if (Boolean.parseBoolean(temphash.get("D5").toString())) {
                                    if (tempSB.length() > 0) {
                                        tempSB.append(";");
                                    }
                                    tempSB.append(temphash.get("D3").toString());
                                }
                            }
                            if (Layer_Select_Dialog.this.m_Callback != null) {
                                Layer_Select_Dialog.this.m_Callback.OnClick("选择图层", tempSB.toString());
                            }
                            Layer_Select_Dialog.this._Dialog.dismiss();
                        } else if (Layer_Select_Dialog.this.m_SelectItem != null) {
                            String tempLayerID = Layer_Select_Dialog.this.m_SelectItem.get("D3").toString();
                            if (Layer_Select_Dialog.this.m_Callback != null) {
                                Layer_Select_Dialog.this.m_Callback.OnClick("选择图层", tempLayerID);
                            }
                            Layer_Select_Dialog.this._Dialog.dismiss();
                        } else {
                            Common.ShowDialog("没有选择任何图层对象.");
                        }
                    }
                } catch (Exception e) {
                }
            }
        };
        this._AllLayersList = new ArrayList();
        this._LayerSelectType = 0;
        this._TAGObject = null;
        this.m_SelectLayersID = "";
        this.m_AllowMultiSelect = false;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.layer_select_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("选择图层");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        ((Button) this._Dialog.findViewById(R.id.buttonSelectAll)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Select_Dialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (Layer_Select_Dialog.this.m_MyTableDataList != null && Layer_Select_Dialog.this.m_MyTableDataList.size() > 0) {
                    for (HashMap<String, Object> tempHash : Layer_Select_Dialog.this.m_MyTableDataList) {
                        tempHash.put("D5", true);
                    }
                    Layer_Select_Dialog.this.m_TableFactory.notifyDataSetInvalidated();
                }
            }
        });
        ((Button) this._Dialog.findViewById(R.id.buttonSelectDe)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Select_Dialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                if (Layer_Select_Dialog.this.m_MyTableDataList != null && Layer_Select_Dialog.this.m_MyTableDataList.size() > 0) {
                    for (HashMap<String, Object> tempHash : Layer_Select_Dialog.this.m_MyTableDataList) {
                        tempHash.put("D5", Boolean.valueOf(!Boolean.parseBoolean(tempHash.get("D5").toString())));
                    }
                    Layer_Select_Dialog.this.m_TableFactory.notifyDataSetInvalidated();
                }
            }
        });
    }

    public void SetLayersList(List layers) {
        this._AllLayersList = layers;
    }

    public void SetTagObject(Object TAGObject) {
        this._TAGObject = TAGObject;
    }

    public void SetLayerSelectType(int type) {
        this._LayerSelectType = type;
        if (type == 1) {
            this._Dialog.SetCaption("选择输出图层");
        } else if (type == 2) {
            this._Dialog.SetCaption("选择查询图层");
        } else if (type == 5) {
            this._Dialog.SetCaption("选择筛选图层");
        } else {
            this._Dialog.SetCaption("选择图层");
        }
    }

    public void SetTitle(String title) {
        this._Dialog.SetCaption(title);
    }

    public void SetSelectedLayers(String selectLayersID) {
        this.m_SelectLayersID = selectLayersID;
    }

    public void SetAllowMultiSelect(boolean value) {
        this.m_AllowMultiSelect = value;
        if (value) {
            this._Dialog.findViewById(R.id.linearLayoutbottom).setVisibility(0);
        } else {
            this._Dialog.findViewById(R.id.linearLayoutbottom).setVisibility(8);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout() {
        String tempColumns;
        String tempColumnTypes;
        int[] tempColumnWidth;
        try {
            if (this._AllLayersList.size() != 0) {
                List<Integer> tempListObjs = null;
                if (this._LayerSelectType == 1) {
                    tempColumns = "图层名称,选择数量,类型";
                    tempColumnTypes = "text,text,image";
                    tempColumnWidth = new int[]{-50, -30, -20};
                    if (this._TAGObject != null) {
                        tempListObjs = (List) this._TAGObject;
                    }
                } else if (this._LayerSelectType == 5) {
                    tempColumns = "选择,图层名称,数量,类型";
                    tempColumnTypes = "checkbox,text,text,image";
                    tempColumnWidth = new int[]{-15, -40, -25, -20};
                    if (this._TAGObject != null) {
                        tempListObjs = (List) this._TAGObject;
                    }
                } else if (this._LayerSelectType == 2) {
                    tempColumns = "图层名称,数量,类型";
                    tempColumnTypes = "text,text,image";
                    tempColumnWidth = new int[]{-50, -30, -20};
                    if (this._TAGObject != null) {
                        tempListObjs = (List) this._TAGObject;
                    }
                } else {
                    tempColumns = "图层名称,类型";
                    tempColumnTypes = "text,image";
                    tempColumnWidth = new int[]{-80, -20};
                }
                this.m_TableFactory = new MyTableFactory();
                this.m_TableFactory.SetHeaderListView(this._Dialog.findViewById(R.id.list_layers), "自定义", tempColumns, tempColumnTypes, tempColumnWidth, this.pCallback);
                this.m_MyTableDataList = new ArrayList();
                Bitmap tmpBmpPoint01 = null;
                Bitmap tmpBmpPoint02 = null;
                Bitmap tmpBmpLine01 = null;
                Bitmap tmpBmpLine02 = null;
                Bitmap tmpBmpPoly01 = null;
                Bitmap tmpBmpPoly02 = null;
                Bitmap tmpBmpError = null;
                Bitmap tmpBmpRaster = null;
                Bitmap tmpBmpRaster2 = null;
                int tid = 0;
                for (XLayer tmpXLayer : this._AllLayersList) {
                    HashMap tmpHashMap = null;
                    if (tmpXLayer.getLayerType() == ELayerType.FEATURE || tmpXLayer.getLayerType() == ELayerType.VECTOR) {
                        GeoLayer tmpLayer = (GeoLayer) tmpXLayer;
                        tmpHashMap = new HashMap();
                        tmpHashMap.put("D1", tmpLayer.getLayerName());
                        tmpHashMap.put("D3", tmpLayer.getLayerID());
                        if (this.m_SelectLayersID.contains(tmpLayer.getLayerID())) {
                            tmpHashMap.put("D5", true);
                        } else {
                            tmpHashMap.put("D5", false);
                        }
                        tmpHashMap.put("D6", Integer.valueOf(tmpLayer.getDataset().getTotalCount()));
                        if (tmpLayer.getEditable()) {
                            if (tmpLayer.getType() == EGeoLayerType.POINT) {
                                if (tmpBmpPoint01 == null) {
                                    tmpBmpPoint01 = BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.point11032);
                                }
                                tmpHashMap.put("D2", tmpBmpPoint01);
                            } else if (tmpLayer.getType() == EGeoLayerType.POLYLINE) {
                                if (tmpBmpLine01 == null) {
                                    tmpBmpLine01 = BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.polyline11032);
                                }
                                tmpHashMap.put("D2", tmpBmpLine01);
                            } else if (tmpLayer.getType() == EGeoLayerType.POLYGON) {
                                if (tmpBmpPoly01 == null) {
                                    tmpBmpPoly01 = BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.polygon11032);
                                }
                                tmpHashMap.put("D2", tmpBmpPoly01);
                            } else {
                                if (tmpBmpError == null) {
                                    tmpBmpError = BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.point_symbol_rect48);
                                }
                                tmpHashMap.put("D2", tmpBmpError);
                            }
                        } else if (tmpLayer.getType() == EGeoLayerType.POINT) {
                            if (tmpBmpPoint02 == null) {
                                tmpBmpPoint02 = BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.point11132);
                            }
                            tmpHashMap.put("D2", tmpBmpPoint02);
                        } else if (tmpLayer.getType() == EGeoLayerType.POLYLINE) {
                            if (tmpBmpLine02 == null) {
                                tmpBmpLine02 = BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.polyline11132);
                            }
                            tmpHashMap.put("D2", tmpBmpLine02);
                        } else if (tmpLayer.getType() == EGeoLayerType.POLYGON) {
                            if (tmpBmpPoly02 == null) {
                                tmpBmpPoly02 = BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.polygon11132);
                            }
                            tmpHashMap.put("D2", tmpBmpPoly02);
                        } else {
                            if (tmpBmpError == null) {
                                tmpBmpError = BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.point_symbol_rect48);
                            }
                            tmpHashMap.put("D2", tmpBmpError);
                        }
                    } else if (tmpXLayer.getLayerType() == ELayerType.RASTERFILE) {
                        XRasterFileLayer tmpXRasterLayer = (XRasterFileLayer) tmpXLayer;
                        if (tmpXRasterLayer != null && tmpXRasterLayer.IsEnable()) {
                            tmpHashMap = new HashMap();
                            tmpHashMap.put("D1", tmpXLayer.getLayerName());
                            if (tmpBmpRaster == null) {
                                tmpBmpRaster = BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.raster1032);
                            }
                            tmpHashMap.put("D2", tmpBmpRaster);
                            tmpHashMap.put("D3", ((XRasterFileLayer) tmpXLayer).getFilePath());
                            if (this.m_SelectLayersID.contains(tmpXRasterLayer.getFilePath())) {
                                tmpHashMap.put("D5", true);
                            } else {
                                tmpHashMap.put("D5", false);
                            }
                            tmpHashMap.put("D6", "--");
                        }
                    } else if (tmpXLayer.getLayerType() == ELayerType.ONLINEMAP) {
                        tmpHashMap = new HashMap();
                        tmpHashMap.put("D1", tmpXLayer.getName());
                        if (tmpBmpRaster2 == null) {
                            tmpBmpRaster2 = BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.raster2032);
                        }
                        tmpHashMap.put("D2", tmpBmpRaster2);
                        tmpHashMap.put("D3", tmpXLayer.getName());
                        if (this.m_SelectLayersID.contains(tmpXLayer.getName())) {
                            tmpHashMap.put("D5", true);
                        } else {
                            tmpHashMap.put("D5", false);
                        }
                        tmpHashMap.put("D6", "--");
                    }
                    if (tmpHashMap != null) {
                        if (this._LayerSelectType == 1 && tempListObjs != null) {
                            tmpHashMap.put("D4", String.valueOf(tempListObjs.get(tid)));
                        }
                        this.m_MyTableDataList.add(tmpHashMap);
                    }
                    tid++;
                }
                if (this._LayerSelectType == 1) {
                    this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D4", "D2"}, this.pCallback);
                } else if (this._LayerSelectType == 2) {
                    this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D6", "D2"}, this.pCallback);
                } else if (this._LayerSelectType == 5) {
                    this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D5", "D1", "D6", "D2"}, this.pCallback);
                } else {
                    this.m_TableFactory.BindDataToListView(this.m_MyTableDataList, new String[]{"D1", "D2"}, this.pCallback);
                }
            }
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.XProject.Layer_Select_Dialog.4
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                Layer_Select_Dialog.this.refreshLayout();
            }
        });
        this._Dialog.show();
    }
}
