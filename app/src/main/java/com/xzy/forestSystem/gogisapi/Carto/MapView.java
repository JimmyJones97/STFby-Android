package  com.xzy.forestSystem.gogisapi.Carto;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Edit.MoveFeature;
import  com.xzy.forestSystem.gogisapi.Edit.PointEditClass;
import  com.xzy.forestSystem.gogisapi.Edit.PolygonEditClass;
import  com.xzy.forestSystem.gogisapi.Edit.PolylineEditClass;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.Geometry.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class MapView extends ImageView {
    boolean FirstLoad = true;
    public Envelope TrackingRectangle;
    private MapTools _Activetool;
    public IOnPaint _CurrentEditPaint;
    public IOnPaint _GPSDisplayPaint;
    public GraphicsLayer _GraphicLayer;
    private ICommand _ICommand;
    private IOnPaint _IOnPaint;
    private Map _Map;
    private MoveFeature _MoveObject = null;
    public Pan _Pan;
    public SelectTool _Select;
    public IOnPaint _TrackLinePaint;
    private Vertex _Vertex = null;
    private ZoomIn _ZoomIn;
    private ZoomInOutPan _ZoomInOutPan;
    private ZoomOut _ZoomOut;
    private boolean m_AllowRfresh = true;
    public ICommand m_BeforeCommand;
    public IOnPaint m_BeforeIOnPaint;
    public Common m_BeforeTool;
    private ICallback m_Callback = null;
    private HashMap<String, Object> m_DrawInMapObjectHashMap = new HashMap<>();
    public MapCompassPaint m_MapCompassPaint = null;
    private List<IOnPaint> m_PaintList = null;
    public ShutterTool m_ShutterTool;
    private ZoomByExtend m_ZoomByExtend = null;

    public MapView(Context paramContext) {
        super(paramContext);
        InitmapView();
    }

    public void InitmapView() {
        this._ZoomIn = new ZoomIn(this);
        this._ZoomOut = new ZoomOut(this);
        this._ZoomInOutPan = new ZoomInOutPan(this);
        this._Pan = new Pan(this);
        this._Select = new SelectTool(this, false);
        this._MoveObject = new MoveFeature(this);
        this._Vertex = new Vertex(this);
        this.m_ShutterTool = new ShutterTool(this);
        this._GraphicLayer = new GraphicsLayer(this);
        this.m_MapCompassPaint = new MapCompassPaint(this);
        setActiveTool(MapTools.None);
    }

    private void mapView_MouseDown(MotionEvent paramMotionEvent) {
        if (this._Map != null && this._Activetool != MapTools.None && this._ICommand != null) {
            this._ICommand.MouseDown(paramMotionEvent);
            if (PubVar._PubCommand.m_Magnifer != null && PubVar._PubCommand.m_Magnifer._IsVisible) {
                PubVar._PubCommand.m_Magnifer.updateMapView();
                PubVar._PubCommand.m_Magnifer.updateCoord(paramMotionEvent.getX(), paramMotionEvent.getY());
            }
        }
    }

    private void mapView_MouseMove(MotionEvent paramMotionEvent) {
        if (this._Map != null && this._Activetool != MapTools.None && this._ICommand != null) {
            this._ICommand.MouseMove(paramMotionEvent);
            if (PubVar._PubCommand.m_Magnifer != null && PubVar._PubCommand.m_Magnifer._IsVisible) {
                PubVar._PubCommand.m_Magnifer.updateCoord(paramMotionEvent.getX(), paramMotionEvent.getY());
            }
        }
    }

    private void mapView_MouseUp(MotionEvent paramMotionEvent) {
        if (this._Map != null && this._Activetool != MapTools.None && this._ICommand != null) {
            this._ICommand.MouseUp(paramMotionEvent);
        }
    }

    private void SetZoomInOut(float paramFloat) {
        this._Map.setExtend(this._Map.getExtend().Scale((double) paramFloat));
        this._Map.Refresh();
    }

    public MoveFeature getMoveFeature() {
        return this._MoveObject;
    }

    public void SetCommand(ICommand paramICommand) {
        this._ICommand = paramICommand;
    }

    public void SetOnPaint(IOnPaint paramIOnPaint) {
        this._IOnPaint = paramIOnPaint;
    }

    public void SetZoomIn() {
        SetZoomInOut(0.5f);
    }

    public void SetZoomOut() {
        SetZoomInOut(2.0f);
    }

    public MapTools getActiveTool() {
        return this._Activetool;
    }

    public Map getMap() {
        return this._Map;
    }

    /* access modifiers changed from: protected */
    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas pCanvas) {
        if (this.m_AllowRfresh) {
            try {
                if (this.FirstLoad) {
                    this.FirstLoad = false;
                }
                super.onDraw(pCanvas);
            } catch (Exception e) {
            }
            try {
                if (this._CurrentEditPaint != null) {
                    this._CurrentEditPaint.OnPaint(pCanvas);
                }
            } catch (Exception e2) {
            }
            try {
                if (this._TrackLinePaint != null) {
                    this._TrackLinePaint.OnPaint(pCanvas);
                }
            } catch (Exception e3) {
            }
            try {
                if (PubVar._PubCommand.m_Navigation != null) {
                    PubVar._PubCommand.m_Navigation.OnPaint(pCanvas);
                }
            } catch (Exception e4) {
            }
            try {
                if (this._GraphicLayer != null) {
                    this._GraphicLayer.OnPaint(pCanvas);
                }
            } catch (Exception e5) {
            }
            try {
                if (this.m_MapCompassPaint != null) {
                    this.m_MapCompassPaint.OnPaint(pCanvas);
                }
            } catch (Exception e6) {
            }
            try {
                if (PubVar._PubCommand.m_Measure != null) {
                    PubVar._PubCommand.m_Measure.OnPaint(pCanvas);
                }
            } catch (Exception e7) {
            }
            try {
                if (this._GPSDisplayPaint != null) {
                    this._GPSDisplayPaint.OnPaint(pCanvas);
                }
            } catch (Exception e8) {
            }
            try {
                if (this.m_PaintList != null) {
                    for (IOnPaint iOnPaint : this.m_PaintList) {
                        iOnPaint.OnPaint(pCanvas);
                    }
                }
            } catch (Exception e9) {
            }
            try {
                if (!(this._IOnPaint == null || this._IOnPaint == PubVar._PubCommand.m_Measure || this._IOnPaint == this._CurrentEditPaint)) {
                    this._IOnPaint.OnPaint(pCanvas);
                }
            } catch (Exception e10) {
            }
            if (this._Map != null) {
                this._Map.DrawUnRegisteInfo(pCanvas);
            }
            if (this._CurrentEditPaint != null) {
                boolean tmpBool01 = false;
                if (this._CurrentEditPaint instanceof PointEditClass) {
                    tmpBool01 = ((PointEditClass) this._CurrentEditPaint).IsDrawInMapCenter;
                } else if (this._CurrentEditPaint instanceof PolylineEditClass) {
                    tmpBool01 = ((PolylineEditClass) this._CurrentEditPaint).IsDrawInMapCenter;
                } else if (this._CurrentEditPaint instanceof PolygonEditClass) {
                    tmpBool01 = ((PolygonEditClass) this._CurrentEditPaint).IsDrawInMapCenter;
                }
                if (tmpBool01) {
                    PointEditClass.DrawMapCenterPlus(pCanvas);
                }
            }
            if (this._Map != null) {
                try {
                    if (this.m_DrawInMapObjectHashMap.size() > 0) {
                        Iterator<Object> tmpIterator = this.m_DrawInMapObjectHashMap.values().iterator();
                        while (tmpIterator.hasNext()) {
                            HashMap<String, Object> tmpHashMap = (HashMap) tmpIterator.next();
                            this._Map.DrawText(pCanvas, String.valueOf(tmpHashMap.get("msg")), Float.parseFloat(String.valueOf(tmpHashMap.get("x"))), Float.parseFloat(String.valueOf(tmpHashMap.get("y"))), String.valueOf(tmpHashMap.get("color")));
                        }
                    }
                } catch (Exception e11) {
                }
            }
        }
    }

    public boolean AddMapTextObject(String key, String msg, float x, float y, String color) {
        try {
            if (this.m_DrawInMapObjectHashMap.containsKey(key)) {
                HashMap<String, Object> tmpHashMap = (HashMap) this.m_DrawInMapObjectHashMap.get(key);
                if (tmpHashMap != null) {
                    tmpHashMap.put("msg", msg);
                    tmpHashMap.put("x", Float.valueOf(x));
                    tmpHashMap.put("y", Float.valueOf(y));
                    tmpHashMap.put("color", color);
                }
            } else {
                HashMap<String, Object> tmpHashMap2 = new HashMap<>();
                tmpHashMap2.put("msg", msg);
                tmpHashMap2.put("x", Float.valueOf(x));
                tmpHashMap2.put("y", Float.valueOf(y));
                tmpHashMap2.put("color", color);
                this.m_DrawInMapObjectHashMap.put(key, tmpHashMap2);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean RemoveMapTextObject(String key) {
        if (!this.m_DrawInMapObjectHashMap.containsKey(key)) {
            return false;
        }
        this.m_DrawInMapObjectHashMap.remove(key);
        return true;
    }

    public void AddPaint(IOnPaint pIOnPaint) {
        if (this.m_PaintList == null) {
            this.m_PaintList = new ArrayList();
        }
        this.m_PaintList.add(pIOnPaint);
    }

    public void RemovePaint(IOnPaint pIOnPaint) {
        if (this.m_PaintList != null) {
            this.m_PaintList.remove(pIOnPaint);
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        int tempAction = paramMotionEvent.getAction();
        paramMotionEvent.getPointerCount();
        if (tempAction == 0) {
            mapView_MouseDown(paramMotionEvent);
        } else if (tempAction == 1) {
            mapView_MouseUp(paramMotionEvent);
        } else if (tempAction == 2) {
            mapView_MouseMove(paramMotionEvent);
        } else if (tempAction == 262) {
            if (this._Activetool == MapTools.ZoomInOutPan || this._Activetool == MapTools.AddPoint || this._Activetool == MapTools.AddPolyline || this._Activetool == MapTools.AddPolygon) {
                mapView_MouseUp(paramMotionEvent);
            }
        } else if (tempAction == 261) {
            if (this._Activetool == MapTools.ZoomInOutPan || this._Activetool == MapTools.AddPoint || this._Activetool == MapTools.AddPolyline || this._Activetool == MapTools.AddPolygon) {
                mapView_MouseDown(paramMotionEvent);
            }
        } else if (tempAction == 5) {
            if (this._Activetool == MapTools.ZoomInOutPan || this._Activetool == MapTools.AddPoint || this._Activetool == MapTools.AddPolyline || this._Activetool == MapTools.AddPolygon) {
                mapView_MouseDown(paramMotionEvent);
            }
        } else if (tempAction == 6 && (this._Activetool == MapTools.ZoomInOutPan || this._Activetool == MapTools.AddPoint || this._Activetool == MapTools.AddPolyline || this._Activetool == MapTools.AddPolygon)) {
            mapView_MouseUp(paramMotionEvent);
        }
        return true;
    }

    public void setActiveTool(MapTools paramTools) {
        if (!(paramTools == MapTools.FullScreen || paramTools == MapTools.FullScreenSize || paramTools == MapTools.GlobalMap)) {
            this._Activetool = paramTools;
        }
        switch (paramTools.ordinal()) {
            case 0:
                this._ICommand = this._Pan;
                this._IOnPaint = this._Pan;
                return;
            case 1:
                this._ICommand = this._ZoomInOutPan;
                this._IOnPaint = this._ZoomInOutPan;
                return;
            case 2:
                this._ICommand = this._ZoomIn;
                this._IOnPaint = this._ZoomIn;
                return;
            case 3:
                this._ICommand = this._ZoomOut;
                this._IOnPaint = this._ZoomIn;
                return;
            case 4:
                this._ICommand = this._Select;
                return;
            case 5:
                setActiveTool(MapTools.FullScreenSize);
                this._Map.Refresh();
                this._Activetool = paramTools;
                return;
            case 6:
                if (getMap() != null) {
                    int i = getWidth();
                    int j = getHeight();
                    if (i == 0 && j == 0) {
                        int[] tmpSize = RefreshSize();
                        if (tmpSize != null) {
                            i = tmpSize[0];
                            j = tmpSize[1];
                        }
                        if (i == 0 && j == 0) {
                            i = PubVar.ScreenWidth;
                            j = PubVar.ScreenHeight;
                        }
                    }
                    if (i > 0 && j > 0 && !(i == this._Map.getSize().getWidth() && j == this._Map.getSize().getHeight())) {
                        this._Map.setSize(getMapSize());
                    }
                    this._Map.setExtend(this._Map.getFullExtendForView());
                    return;
                }
                return;
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 14:
            case 17:
            case 19:
            case 20:
            case 21:
            case 23:
            case 24:
            case 25:
            default:
                return;
            case 13:
                this._ICommand = this._Vertex;
                this._IOnPaint = this._Vertex;
                this._Vertex.SetVertexEditType(Vertex.EVertexEditType.MOVE);
                return;
            case 15:
                this._ICommand = this._Vertex;
                this._IOnPaint = this._Vertex;
                this._Vertex.SetVertexEditType(Vertex.EVertexEditType.ADD);
                return;
            case 16:
                this._ICommand = this._Vertex;
                this._IOnPaint = this._Vertex;
                this._Vertex.SetVertexEditType(Vertex.EVertexEditType.DELETE);
                return;
            case 18:
                this._ICommand = this._MoveObject;
                this._IOnPaint = this._MoveObject;
                return;
            case 22:
                this._ICommand = this._ZoomInOutPan;
                this._IOnPaint = this._ZoomInOutPan;
                return;
            case 26:
                if (this._Map != null) {
                    this._Map.ZoomToExtend(this._Map.getFullExtendForView().Scale(2.0d));
                    return;
                }
                return;
            case 27:
                this._ICommand = this.m_ShutterTool;
                this._IOnPaint = this.m_ShutterTool;
                return;
            case 28:
                this._ICommand = getZoomByExtendTool();
                return;
        }
    }

    public int[] RefreshSize() {
        int[] result = new int[2];
        try {
            measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
            int tmpWidth = getMeasuredWidth();
            int tmpHeight = getMeasuredHeight();
            result[0] = tmpWidth;
            result[1] = tmpHeight;
        } catch (Exception e) {
        }
        return result;
    }

    public void setActiveTools(MapTools paramTools, IOnPaint paramIOnPaint, ICommand paramICommand) {
        setActiveTool(paramTools);
        this._ICommand = paramICommand;
        this._IOnPaint = paramIOnPaint;
    }

    public void setMap(Map paramMap) {
        this._Map = paramMap;
    }

    public boolean getAllowRefreshMap() {
        return this.m_AllowRfresh;
    }

    public void setAllowRefreshMap(boolean allowRefresh) {
        this.m_AllowRfresh = allowRefresh;
    }

    public void SetCallback(ICallback pCallback) {
        this.m_Callback = pCallback;
        this._ZoomInOutPan.SetCallback(this.m_Callback);
    }

    private ZoomByExtend getZoomByExtendTool() {
        if (this.m_ZoomByExtend == null) {
            this.m_ZoomByExtend = new ZoomByExtend(this);
        }
        return this.m_ZoomByExtend;
    }

    public Size getMapSize() {
        return new Size(PubVar._MapView.getWidth(), PubVar._MapView.getHeight());
    }
}
