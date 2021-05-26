package  com.xzy.forestSystem.gogisapi.Carto;

import android.graphics.PointF;
import android.view.MotionEvent;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Selection;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectTool implements ICommand {
    private static final float DoubleClick_Bias = (PubVar.ScaledDensity * 10.0f);
    static long DoubleClick_TIME = 400;
    public static double SELECT_CLICK_BIAS = ((double) (PubVar.ScaledDensity * 10.0f));
    private float LastX;
    private float LastY;
    private boolean _AllowSelect;
    private ICallback _Callback;
    private ICommand _Command;
    private boolean _IsDoubleClick;
    private boolean _IsSingleClick;
    private long _LastMouseDownTime;
    private boolean _MultiSelect;
    private int _SelectedCount;
    private TrackRectangle _TrackRect;
    private MapView _mapView;
    private String m_SelectLayers;

    public SelectTool(MapView parammapView, boolean isAllowSelect) {
        this._Callback = null;
        this._Command = null;
        this._mapView = null;
        this._MultiSelect = true;
        this._AllowSelect = false;
        this._IsSingleClick = false;
        this._IsDoubleClick = false;
        this._TrackRect = null;
        this._SelectedCount = 0;
        this.m_SelectLayers = "";
        this.LastX = 0.0f;
        this.LastY = 0.0f;
        this._LastMouseDownTime = 0;
        this._MultiSelect = false;
        this._mapView = parammapView;
        this._TrackRect = new TrackRectangle(parammapView);
        this._Command = this._TrackRect;
        this._SelectedCount = 0;
        this._AllowSelect = isAllowSelect;
    }

    public void SetSeletected(boolean isAllowdSelected) {
        this._AllowSelect = isAllowdSelected;
    }

    public void SetIsSingleClick(boolean value) {
        this._IsSingleClick = value;
    }

    public boolean getSeletected() {
        return this._AllowSelect;
    }

    public void SetMultiSeletected(boolean isAllowdMultiSelected) {
        this._MultiSelect = isAllowdMultiSelected;
    }

    public void SetSelectLayers(String layersID) {
        this.m_SelectLayers = layersID;
    }

    public String getSelectLayers() {
        return this.m_SelectLayers;
    }

    public void ClearAllSelection() {
        this._SelectedCount = 0;
        this._mapView.getMap().ClearSelSelection();
    }

    public int GetSelectedCount() {
        return this._SelectedCount;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseDown(MotionEvent paramMotionEvent) {
        if (this._AllowSelect) {
            this._TrackRect.setTrackEnvelope(null);
            this._Command.MouseDown(paramMotionEvent);
            long tmpIntv = paramMotionEvent.getDownTime() - this._LastMouseDownTime;
            this._LastMouseDownTime = paramMotionEvent.getDownTime();
            float tmpX = Math.abs(paramMotionEvent.getX() - this.LastX);
            float tmpY = Math.abs(paramMotionEvent.getY() - this.LastY);
            this._IsDoubleClick = false;
            if (tmpX >= DoubleClick_Bias || tmpY >= DoubleClick_Bias || tmpIntv >= DoubleClick_TIME) {
                this.LastX = paramMotionEvent.getX();
                this.LastY = paramMotionEvent.getY();
                return;
            }
            ClearAllSelection();
            this._mapView.getMap().RefreshFast();
            this.LastX = paramMotionEvent.getX();
            this.LastY = paramMotionEvent.getY();
            this._IsDoubleClick = true;
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseMove(MotionEvent paramMotionEvent) {
        if (!this._IsDoubleClick) {
            if (paramMotionEvent.getPointerCount() > 1) {
                PubVar._PubCommand.ProcessCommand("自由缩放");
                Common.ShowToast("自由缩放地图");
            } else if (!this._AllowSelect) {
            } else {
                if (!this._IsSingleClick) {
                    this._Command.MouseMove(paramMotionEvent);
                } else if (((double) Math.abs(this.LastX - paramMotionEvent.getX())) > SELECT_CLICK_BIAS || ((double) Math.abs(this.LastY - paramMotionEvent.getY())) > SELECT_CLICK_BIAS) {
                    PubVar._PubCommand.ProcessCommand("自由缩放");
                    Common.ShowToast("自由缩放地图");
                }
            }
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseUp(MotionEvent paramMotionEvent) {
        if (this._IsDoubleClick) {
            this._IsDoubleClick = false;
        } else if (this._AllowSelect) {
            this._Command.MouseUp(paramMotionEvent);
            if (!this._MultiSelect) {
                ClearAllSelection();
            }
            this.LastX = paramMotionEvent.getX();
            this.LastY = paramMotionEvent.getY();
            StartSelect(new PointF(paramMotionEvent.getX(), paramMotionEvent.getY()));
            if (this._Callback != null) {
                this._Callback.OnClick("OK", String.valueOf(this._SelectedCount));
            }
        }
    }

    public void SetCallback(ICallback tmpICallback) {
        this._Callback = tmpICallback;
    }

    public void StartSelect(PointF paramPointF) {
        Exception ex;
        Envelope selectEnvelope = null;
        try {
            this._SelectedCount = 0;
            Coordinate localCoordinate = this._mapView.getMap().ScreenToMap(paramPointF);
            double tempDis = this._mapView.getMap().ToMapDistance(SELECT_CLICK_BIAS);
            boolean tmpIsClick = false;
            if (this._IsSingleClick) {
                PointF tmpStartPtn = this._TrackRect.getStartPoint();
                Envelope selectEnvelope2 = new Envelope(this._mapView.getMap().ScreenToMap(tmpStartPtn.x - (((float) SELECT_CLICK_BIAS) / 2.0f), tmpStartPtn.y - (((float) SELECT_CLICK_BIAS) / 2.0f)), this._mapView.getMap().ScreenToMap(tmpStartPtn.x + (((float) SELECT_CLICK_BIAS) / 2.0f), tmpStartPtn.y + (((float) SELECT_CLICK_BIAS) / 2.0f)));
                tmpIsClick = true;
                try {
                    BasicValue tmpBasicValue = new BasicValue();
                    if (this._mapView._GraphicLayer.HitTest(localCoordinate, tempDis, tmpBasicValue)) {
                        if (this._Callback != null) {
                            this._Callback.OnClick("SelectToolReturnGraphicLayer", Integer.valueOf(tmpBasicValue.getInt()));
                        }
                        this._mapView.getMap().RefreshFast();
                        return;
                    }
                    selectEnvelope = selectEnvelope2;
                } catch (Exception e) {
                    ex = e;
                    try {
                        Common.Log("StartSelect02", "错误:" + ex.toString() + "-->" + ex.getMessage());
                    } finally {
                        this._mapView.getMap().RefreshFast();
                    }
                }
            } else {
                selectEnvelope = this._TrackRect.getTrackEnvelope();
            }
            if (selectEnvelope == null) {
                this._mapView.getMap().RefreshFast();
                return;
            }
            if (selectEnvelope.getHeight() < tempDis && selectEnvelope.getWidth() < tempDis) {
                tmpIsClick = true;
            }
            boolean hasSeleted = false;
            boolean isMustContain = true;
            if (localCoordinate.getX() < selectEnvelope.getMaxX()) {
                isMustContain = false;
            }
            boolean tmpNeedConsiderFilter = true;
            if (this.m_SelectLayers.equals("")) {
                tmpNeedConsiderFilter = false;
            }
            for (int i = this._mapView.getMap().getGeoLayers().size() - 1; i >= 0; i--) {
                try {
                    GeoLayer localGeoLayer = this._mapView.getMap().getGeoLayers().GetLayerByIndex(i);
                    Selection tmpSelSelction = this._mapView.getMap().getFeatureSelectionByIndex(i, false);
                    Selection tmpShowSelction = this._mapView.getMap().getFeatureSelectionByIndex(i, true);
                    if (!(tmpSelSelction == null || tmpShowSelction == null || (tmpNeedConsiderFilter && !this.m_SelectLayers.contains(localGeoLayer.getLayerID())))) {
                        if (localGeoLayer.getSelectable() && localGeoLayer.getVisible() && selectEnvelope != null) {
                            if (tmpIsClick) {
                                Coordinate localCoordinate2 = this._mapView.getMap().ScreenToMap(paramPointF);
                                Selection tmpSelSelction2 = tmpSelSelction.Clone();
                                tmpSelSelction.RemoveAll();
                                hasSeleted = localGeoLayer.getDataset().HitTest(localCoordinate2, tempDis, tmpSelSelction, tmpShowSelction);
                                if (hasSeleted) {
                                    SwitchSelection(tmpSelSelction, tmpSelSelction2);
                                }
                            } else {
                                hasSeleted = localGeoLayer.getDataset().QueryWithSelEnvelope(selectEnvelope, tmpSelSelction, tmpShowSelction, isMustContain);
                            }
                        }
                        this._SelectedCount += tmpSelSelction.getCount();
                        if (!this._MultiSelect && hasSeleted && this._SelectedCount > 0) {
                            this._mapView.getMap().RefreshFast();
                            if (this._IsSingleClick && this._Callback != null) {
                                this._Callback.OnClick("SelectToolReturn", "属性");
                            }
                            PubVar.OutputMessage("【选择】:选择对象" + this._SelectedCount + "个.");
                            this._mapView.getMap().RefreshFast();
                            return;
                        }
                    }
                } catch (Exception ex2) {
                    Common.Log("StartSelect01", "错误:" + ex2.toString() + "-->" + ex2.getMessage());
                }
            }
            int i2 = this._mapView.getMap().getVectorGeoLayers().size() - 1;
            while (true) {
                if (i2 < 0) {
                    break;
                }
                try {
                    GeoLayer localGeoLayer2 = this._mapView.getMap().getVectorGeoLayers().GetLayerByIndex(i2);
                    Selection tmpSelSelction3 = this._mapView.getMap().getSelectionByIndex(i2, false);
                    Selection tmpShowSelction2 = this._mapView.getMap().getSelectionByIndex(i2, true);
                    if (!(tmpSelSelction3 == null || tmpShowSelction2 == null || (tmpNeedConsiderFilter && !this.m_SelectLayers.contains(localGeoLayer2.getLayerID())))) {
                        if (localGeoLayer2.getSelectable() && localGeoLayer2.getVisible() && selectEnvelope != null) {
                            if (tmpIsClick) {
                                hasSeleted = localGeoLayer2.getDataset().HitTest(this._mapView.getMap().ScreenToMap(paramPointF), tempDis, tmpSelSelction3, tmpShowSelction2);
                            } else {
                                hasSeleted = localGeoLayer2.getDataset().QueryWithSelEnvelope(selectEnvelope, tmpSelSelction3, tmpShowSelction2, isMustContain);
                            }
                        }
                        this._SelectedCount += tmpSelSelction3.getCount();
                        if (!this._MultiSelect && hasSeleted && this._SelectedCount > 0) {
                            break;
                        }
                    }
                } catch (Exception ex3) {
                    Common.Log("StartSelect01-1", "错误:" + ex3.toString() + "-->" + ex3.getMessage());
                }
                i2--;
            }
            if (this._SelectedCount > 0) {
                if (this._IsSingleClick && this._Callback != null) {
                    this._Callback.OnClick("SelectToolReturn", "属性");
                }
                PubVar.OutputMessage("【选择】:选择对象" + this._SelectedCount + "个.");
            }
            this._mapView.getMap().RefreshFast();
        } catch (Exception e2) {
            ex = e2;
            Common.Log("StartSelect02", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    private void SwitchSelection(Selection newSelection, Selection oldSelection) {
        if (newSelection.getCount() > 0 && oldSelection.getCount() > 0) {
            List<Integer> tmpList = oldSelection.getGeometryIndexList();
            List<Integer> tmpList2 = newSelection.getGeometryIndexList();
            int count = tmpList.size();
            for (int i = 0; i < count; i++) {
                int tmpI = tmpList.get(i).intValue();
                if (tmpList2.contains(Integer.valueOf(tmpI))) {
                    tmpList2.remove(Integer.valueOf(tmpI));
                } else {
                    tmpList2.add(Integer.valueOf(tmpI));
                }
            }
        }
    }

    public List<PointF> RefreshScreenShowSelectPoints(List<Coordinate> returnCoordinates) {
        List<PointF> result = new ArrayList<>();
        try {
            List<HashMap<String, Object>> tmpList = PubVar._Map.GetSelectObjects(-1, -1, true, new BasicValue());
            if (tmpList.size() > 0) {
                for (HashMap<String, Object> tmpHash : tmpList) {
                    try {
                        AbstractGeometry tmpGeo = (AbstractGeometry) tmpHash.get("Geometry");
                        if (tmpGeo != null) {
                            for (Coordinate tmpCoord : tmpGeo.GetAllCoordinateList()) {
                                PointF tmpPtn1 = PubVar._Map.MapToScreenF(tmpCoord.getX(), tmpCoord.getY());
                                if (tmpPtn1.x > -1.0f && tmpPtn1.x <= ((float) PubVar.ScreenWidth) && tmpPtn1.y > -1.0f && tmpPtn1.y <= ((float) PubVar.ScreenHeight)) {
                                    returnCoordinates.add(tmpCoord.Clone());
                                    result.add(tmpPtn1);
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        } catch (Exception e2) {
        }
        return result;
    }
}
