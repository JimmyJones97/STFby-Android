package  com.xzy.forestSystem.gogisapi.Geometry;

import com.xzy.forestSystem.PubVar;

public class Envelope {
    private Coordinate _LeftTop = null;
    private Coordinate _RightBottom = null;
    private boolean _Type = true;

    public Envelope(double xmin, double ymax, double xmax, double ymin) {
        this._LeftTop = new Coordinate(xmin, ymax);
        this._RightBottom = new Coordinate(xmax, ymin);
    }

    public Envelope(Coordinate leftTop, Coordinate rightBottom) {
        this._LeftTop = leftTop;
        this._RightBottom = rightBottom;
    }

    public Envelope Clone() {
        return new Envelope(getLeftTop().Clone(), getRightBottom().Clone());
    }

    public boolean Contains(Envelope paramEnvelope) {
        if (paramEnvelope.getMinX() < getMinX() || paramEnvelope.getMinX() > getMaxX() || paramEnvelope.getMaxX() < getMinX() || paramEnvelope.getMaxX() > getMaxX() || paramEnvelope.getMinY() < getMinY() || paramEnvelope.getMinY() > getMaxY() || paramEnvelope.getMaxY() < getMinY() || paramEnvelope.getMaxY() > getMaxY()) {
            return false;
        }
        return true;
    }

    public boolean ContainsPoint(double paramDouble1, double paramDouble2) {
        return paramDouble1 >= getMinX() && paramDouble1 <= getMaxX() && paramDouble2 >= getMinY() && paramDouble2 <= getMaxY();
    }

    public boolean ContainsPoint(Coordinate tmpCoord) {
        return ContainsPoint(tmpCoord.getX(), tmpCoord.getY());
    }

    public Polyline ConvertToPolyline() {
        Polyline localPolyline = new Polyline();
        localPolyline.getVertexList().add(new Coordinate(getMinX(), getMinY()));
        localPolyline.getVertexList().add(getLeftTop());
        localPolyline.getVertexList().add(new Coordinate(getMaxX(), getMaxY()));
        localPolyline.getVertexList().add(getRightBottom());
        localPolyline.getVertexList().add(new Coordinate(getMinX(), getMinY()));
        return localPolyline;
    }

    public boolean Equal(Envelope paramEnvelope) {
        if (paramEnvelope == null || this == null || !getLeftTop().Equal(paramEnvelope.getLeftTop()) || !getRightBottom().Equal(paramEnvelope.getRightBottom())) {
            return false;
        }
        return true;
    }

    public Envelope ExtendEnvelope() {
        if (getWidth() > getHeight()) {
            double d2 = (getWidth() - getHeight()) / 2.0d;
            return new Envelope(getLeftTop().getX(), getLeftTop().getY() + d2, getRightBottom().getX(), getRightBottom().getY() - d2);
        }
        double d1 = (getHeight() - getWidth()) / 2.0d;
        return new Envelope(getLeftTop().getX() - d1, getLeftTop().getY(), getRightBottom().getX() + d1, getRightBottom().getY());
    }

    public double Factor(Envelope paramEnvelope) {
        double d2;
        if (paramEnvelope.getWidth() <= 0.0d || paramEnvelope.getHeight() <= 0.0d) {
        }
        do {
            d2 = getWidth() / paramEnvelope.getWidth();
        } while (d2 < getHeight() / paramEnvelope.getHeight());
        return d2;
    }

    public Envelope GetIntersectEnvelope(Envelope paramEnvelope) {
        double d1 = Math.max(getMinX(), paramEnvelope.getMinX());
        double d2 = Math.max(getMinY(), paramEnvelope.getMinY());
        double d3 = Math.min(getMaxX(), paramEnvelope.getMaxX());
        double d4 = Math.min(getMaxY(), paramEnvelope.getMaxY());
        if (d1 > d3 || d2 > d4) {
            return null;
        }
        return new Envelope(d1, d4, d3, d2);
    }

    public boolean Intersect(Envelope paramEnvelope) {
        if (paramEnvelope == null) {
            return true;
        }
        double d1 = Math.max(getMinX(), paramEnvelope.getMinX());
        double d2 = Math.max(getMinY(), paramEnvelope.getMinY());
        double d3 = Math.min(getMaxX(), paramEnvelope.getMaxX());
        double d4 = Math.min(getMaxY(), paramEnvelope.getMaxY());
        if (d1 > d3 || d2 > d4) {
            return Contains(paramEnvelope);
        }
        return true;
    }

    public boolean IsZero() {
        double d = PubVar._Map.ToMapDistance(20.0d);
        return getWidth() <= d || getHeight() <= d;
    }

    public boolean IsEmpty() {
        return getWidth() <= 0.0d || getHeight() <= 0.0d;
    }

    public Envelope MergeEnvelope(Envelope paramEnvelope) {
        if (Contains(paramEnvelope)) {
            return new Envelope(getLeftTop().Clone(), getRightBottom().Clone());
        }
        if (paramEnvelope.Contains(this)) {
            return new Envelope(paramEnvelope.getLeftTop().Clone(), paramEnvelope.getRightBottom().Clone());
        }
        double d1 = Math.min(getMinX(), paramEnvelope.getMinX());
        double d2 = Math.min(getMinY(), paramEnvelope.getMinY());
        return new Envelope(d1, Math.max(getMaxY(), paramEnvelope.getMaxY()), Math.max(getMaxX(), paramEnvelope.getMaxX()), d2);
    }

    public void MergeEnvelope2(Envelope paramEnvelope) {
        if (Contains(paramEnvelope)) {
            return;
        }
        if (paramEnvelope.Contains(this)) {
            this._LeftTop = paramEnvelope.getLeftTop().Clone();
            this._RightBottom = paramEnvelope.getRightBottom().Clone();
            return;
        }
        double d1 = Math.min(getMinX(), paramEnvelope.getMinX());
        double d2 = Math.min(getMinY(), paramEnvelope.getMinY());
        double d3 = Math.max(getMaxX(), paramEnvelope.getMaxX());
        this._LeftTop = new Coordinate(d1, Math.max(getMaxY(), paramEnvelope.getMaxY()));
        this._RightBottom = new Coordinate(d3, d2);
    }

    public Envelope Scale(double paramDouble) {
        Coordinate localCoordinate = getCenter();
        return new Envelope(localCoordinate.getX() - ((getWidth() / 2.0d) * paramDouble), localCoordinate.getY() + ((getHeight() / 2.0d) * paramDouble), localCoordinate.getX() + ((getWidth() / 2.0d) * paramDouble), localCoordinate.getY() - ((getHeight() / 2.0d) * paramDouble));
    }

    public Coordinate getCenter() {
        return new Coordinate((this._RightBottom.getX() + this._LeftTop.getX()) / 2.0d, (this._LeftTop.getY() + this._RightBottom.getY()) / 2.0d);
    }

    public double getHeight() {
        return getMaxY() - getMinY();
    }

    public Coordinate getLeftTop() {
        return this._LeftTop;
    }

    public double getMaxX() {
        return this._RightBottom.getX();
    }

    public double getMaxY() {
        return this._LeftTop.getY();
    }

    public double getMinX() {
        return this._LeftTop.getX();
    }

    public double getMinY() {
        return this._RightBottom.getY();
    }

    public Coordinate getRightBottom() {
        return this._RightBottom;
    }

    public boolean getType() {
        return this._Type;
    }

    public double getWidth() {
        return getMaxX() - getMinX();
    }

    public void setCenter(Coordinate tmpCoord) {
        Coordinate localCoordinate = getCenter();
        this._LeftTop.setX(this._LeftTop.getX() + (tmpCoord.getX() - localCoordinate.getX()));
        this._LeftTop.setY(this._LeftTop.getY() + (tmpCoord.getY() - localCoordinate.getY()));
        this._RightBottom.setX(this._RightBottom.getX() + (tmpCoord.getX() - localCoordinate.getX()));
        this._RightBottom.setY(this._RightBottom.getY() + (tmpCoord.getY() - localCoordinate.getY()));
    }

    public void setLeftTop(Coordinate tmpCoord) {
        this._LeftTop = tmpCoord;
    }

    public void setRightBottom(Coordinate tmpCoord) {
        this._RightBottom = tmpCoord;
    }

    public void setType(boolean paramBoolean) {
        this._Type = paramBoolean;
    }

    public void SetOffset(double offsetX, double offsetY) {
        this._LeftTop.SetOffset(offsetX, offsetY);
        this._RightBottom.SetOffset(offsetX, offsetY);
    }

    public void SetExtendSize(double xSize, double ySize) {
        this._LeftTop.f484_x -= xSize;
        this._LeftTop.f485_y += ySize;
        this._RightBottom.f484_x += xSize;
        this._RightBottom.f485_y -= ySize;
    }

    public void SetExtendSize(double Size) {
        SetExtendSize(Size, Size);
    }

    public double ToPointDistance(Coordinate coord) {
        if (ContainsPoint(coord)) {
            return 0.0d;
        }
        if (coord.f484_x >= this._LeftTop.f484_x && coord.f484_x <= this._RightBottom.f484_x) {
            double result = Math.abs(coord.f485_y - this._LeftTop.f485_y);
            double tmpD2 = Math.abs(coord.f485_y - this._RightBottom.f485_y);
            return result > tmpD2 ? tmpD2 : result;
        } else if (coord.f485_y > this._LeftTop.f485_y || coord.f485_y < this._RightBottom.f485_y) {
            double tmpD = coord.ToDistance(this._LeftTop);
            double tmpD22 = coord.ToDistance(this._RightBottom);
            if (tmpD > tmpD22) {
                tmpD = tmpD22;
            }
            double tmpD23 = coord.ToDistance(this._LeftTop.f484_x, this._RightBottom.f485_y);
            if (tmpD > tmpD23) {
                tmpD = tmpD23;
            }
            double tmpD24 = coord.ToDistance(this._RightBottom.f484_x, this._LeftTop.f485_y);
            if (tmpD > tmpD24) {
                tmpD = tmpD24;
            }
            return tmpD;
        } else {
            double result2 = Math.abs(coord.f484_x - this._LeftTop.f484_x);
            double tmpD25 = Math.abs(coord.f484_x - this._RightBottom.f484_x);
            return result2 > tmpD25 ? tmpD25 : result2;
        }
    }

    public boolean EqualWithBias(Envelope paramEnvelope, double biasRatio) {
        if (paramEnvelope == null || this == null) {
            return false;
        }
        double tmpBias = biasRatio * paramEnvelope.getWidth();
        if (!getLeftTop().EqualWithBias(paramEnvelope.getLeftTop(), tmpBias) || !getRightBottom().EqualWithBias(paramEnvelope.getRightBottom(), tmpBias)) {
            return false;
        }
        return true;
    }
}
