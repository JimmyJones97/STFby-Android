package  com.xzy.forestSystem.gogisapi.Geometry;

public class ExtraGeometry {
    private int _DIndex = -1;
    private AbstractGeometry _Geometry = null;
    private double _MaxX;
    private double _MaxY;
    private double _MinX;
    private double _MinY;

    public void Dispose2() {
        if (this._Geometry != null) {
            this._Geometry.Dispose2();
        }
        this._Geometry = null;
    }

    public int getDIndex() {
        return this._DIndex;
    }

    public Envelope getEnvelope() {
        if (this._Geometry != null) {
            return this._Geometry.getEnvelope();
        }
        return new Envelope(this._MinX, this._MaxY, this._MaxX, this._MinY);
    }

    public AbstractGeometry getGeometry() {
        return this._Geometry;
    }

    public double getMaxX() {
        return this._MaxX;
    }

    public double getMaxY() {
        return this._MaxY;
    }

    public double getMinX() {
        return this._MinX;
    }

    public double getMinY() {
        return this._MinY;
    }

    public void setDIndex(int paramInt) {
        this._DIndex = paramInt;
        if (this._Geometry != null) {
            this._Geometry.setIndex(paramInt);
        }
    }

    public void setGeometry(AbstractGeometry paramGeometry) {
        this._Geometry = paramGeometry;
    }

    public void setMaxX(double paramDouble) {
        this._MaxX = paramDouble;
    }

    public void setMaxY(double paramDouble) {
        this._MaxY = paramDouble;
    }

    public void setMinX(double paramDouble) {
        this._MinX = paramDouble;
    }

    public void setMinY(double paramDouble) {
        this._MinY = paramDouble;
    }
}
