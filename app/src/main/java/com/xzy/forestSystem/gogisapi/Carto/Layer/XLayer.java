package  com.xzy.forestSystem.gogisapi.Carto.Layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import  com.xzy.forestSystem.gogisapi.Carto.Map;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;

public class XLayer {
    Envelope _Extend;
    int _LayerIndex;
    String _LayerName;
    ELayerType _LayerType;
    double _MaxX;
    double _MaxY;
    double _MinX;
    double _MinY;
    String _Name;
    Object _TagValue;
    int _Transparent;
    double _VisiableScaleMax;
    double _VisiableScaleMin;
    boolean _Visible;

    public XLayer() {
        this._Name = "";
        this._LayerName = "";
        this._Visible = true;
        this._Transparent = 0;
        this._LayerType = ELayerType.NONE;
        this._VisiableScaleMin = 0.0d;
        this._VisiableScaleMax = 2.147483647E9d;
        this._LayerIndex = 0;
        this._MinX = 0.0d;
        this._MaxX = 2.147483647E9d;
        this._MinY = 0.0d;
        this._MaxY = 2.147483647E9d;
        this._Extend = null;
        this._TagValue = null;
        this._Extend = new Envelope(this._MinX, this._MaxY, this._MaxX, this._MinY);
    }

    public void Dispose2() {
    }

    public void SetLayerName(String layerName) {
        this._LayerName = layerName;
    }

    public String getLayerName() {
        return this._LayerName;
    }

    public ELayerType getLayerType() {
        return this._LayerType;
    }

    public void setLayerType(ELayerType LayerType) {
        this._LayerType = LayerType;
    }

    public boolean Refresh(Map map) {
        return true;
    }

    public void Dispose() {
    }

    public String getName() {
        return this._Name;
    }

    public void setName(String paramString) {
        this._Name = paramString;
    }

    public boolean getVisible() {
        return this._Visible;
    }

    public void setVisible(boolean paramBoolean) {
        this._Visible = paramBoolean;
    }

    public int getTransparent() {
        return this._Transparent;
    }

    public void setTransparent(int value) {
        this._Transparent = value;
    }

    public double getMinScale() {
        return this._VisiableScaleMin;
    }

    public double getMaxScale() {
        return this._VisiableScaleMax;
    }

    public void setMinScale(double value) {
        this._VisiableScaleMin = value;
    }

    public void setMaxScale(double value) {
        this._VisiableScaleMax = value;
    }

    public void setLayerIndex(int layerIndex) {
        this._LayerIndex = layerIndex;
    }

    public int getLayerIndex() {
        return this._LayerIndex;
    }

    public void setMinX(double value) {
        this._MinX = value;
    }

    public void setMaxX(double value) {
        this._MaxX = value;
    }

    public void setMinY(double value) {
        this._MinY = value;
    }

    public void setMaxY(double value) {
        this._MaxY = value;
    }

    public double getMinX() {
        return this._MinX;
    }

    public double getMaxX() {
        return this._MaxX;
    }

    public double getMinY() {
        return this._MinY;
    }

    public double getMaxY() {
        return this._MaxY;
    }

    public void updateExtend() {
        this._Extend = new Envelope(this._MinX, this._MaxY, this._MaxX, this._MinY);
    }

    public void SetExtend(Envelope extend) {
        this._Extend = extend;
        this._MinX = extend.getMinX();
        this._MinY = extend.getMinY();
        this._MaxX = extend.getMaxX();
        this._MaxY = extend.getMaxY();
    }

    public Envelope getExtend() {
        return this._Extend;
    }

    public boolean IsInMapExtend(Map map) {
        Envelope tempExtend = map.getExtend();
        double tempScale = map.getScale();
        if (tempScale < this._VisiableScaleMin || tempScale > this._VisiableScaleMax || this._Extend.getLeftTop().getX() > tempExtend.getRightBottom().getX() || this._Extend.getLeftTop().getY() < tempExtend.getRightBottom().getY() || this._Extend.getRightBottom().getX() < tempExtend.getLeftTop().getX() || this._Extend.getRightBottom().getY() > tempExtend.getLeftTop().getY()) {
            return false;
        }
        return true;
    }

    public void SetTag(Object value) {
        this._TagValue = value;
    }

    public Object getTag() {
        return this._TagValue;
    }

    public void SetDrawCanvas(Bitmap bitmap) {
    }

    public void SetOtherDrawCanvas(Canvas canvas) {
    }
}
