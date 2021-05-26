package  com.xzy.forestSystem.gogisapi.Geometry;

import java.text.DecimalFormat;

public class Coordinate {
    double _geoX = 0.0d;
    double _geoY = 0.0d;

    /* renamed from: _x */
    double f484_x;

    /* renamed from: _y */
    double f485_y;

    /* renamed from: _z */
    double f486_z;

    public Coordinate() {
    }

    public Coordinate(double x, double y) {
        this.f484_x = x;
        this.f485_y = y;
        this.f486_z = 0.0d;
    }

    public Coordinate(double x, double y, double z) {
        this.f484_x = x;
        this.f485_y = y;
        this.f486_z = z;
    }

    public Coordinate(double x, double y, double longitude, double latitude) {
        this.f484_x = x;
        this.f485_y = y;
        this.f486_z = 0.0d;
        this._geoX = longitude;
        this._geoY = latitude;
    }

    public Coordinate(double x, double y, Double z, double longitude, double latitude) {
        this.f484_x = x;
        this.f485_y = y;
        this.f486_z = z.doubleValue();
        this._geoX = longitude;
        this._geoY = latitude;
    }

    public Coordinate Clone() {
        Coordinate localCoordinate = new Coordinate(this.f484_x, this.f485_y, this.f486_z);
        localCoordinate.setGeoX(getGeoX());
        localCoordinate.setGeoY(getGeoY());
        return localCoordinate;
    }

    public void CopyFrom(Coordinate coordinate) {
        if (coordinate != null) {
            this.f484_x = coordinate.f484_x;
            this.f485_y = coordinate.f485_y;
            this.f486_z = coordinate.f486_z;
            this._geoX = coordinate._geoX;
            this._geoY = coordinate._geoY;
        }
    }

    public boolean Equal(Coordinate tmpCoord) {
        if (tmpCoord != null && this.f484_x == tmpCoord.getX() && this.f485_y == tmpCoord.getY()) {
            return true;
        }
        return false;
    }

    public boolean IsValid() {
        return this.f484_x < 0.0d && this.f485_y < 0.0d;
    }

    public String ToString() {
        DecimalFormat localDecimalFormat = new DecimalFormat("0.000");
        return String.valueOf(localDecimalFormat.format(this.f484_x)) + "," + localDecimalFormat.format(this.f485_y);
    }

    public double getGeoX() {
        return this._geoX;
    }

    public double getGeoY() {
        return this._geoY;
    }

    public double getX() {
        return this.f484_x;
    }

    public double getY() {
        return this.f485_y;
    }

    public double getZ() {
        return this.f486_z;
    }

    public void setGeoX(double paramDouble) {
        this._geoX = paramDouble;
    }

    public void setGeoY(double paramDouble) {
        this._geoY = paramDouble;
    }

    public void setX(double paramDouble) {
        this.f484_x = paramDouble;
    }

    public void setY(double paramDouble) {
        this.f485_y = paramDouble;
    }

    public void setZ(double paramDouble) {
        this.f486_z = paramDouble;
    }

    public void SetOffset(double offsetX, double offsetY) {
        this.f484_x += offsetX;
        this.f485_y += offsetY;
    }

    public double ToDistance(Coordinate coord) {
        return Math.sqrt(((coord.f484_x - this.f484_x) * (coord.f484_x - this.f484_x)) + ((coord.f485_y - this.f485_y) * (coord.f485_y - this.f485_y)));
    }

    public double ToDistance(double x, double y) {
        return Math.sqrt(((x - this.f484_x) * (x - this.f484_x)) + ((y - this.f485_y) * (y - this.f485_y)));
    }

    public boolean EqualWithBias(Coordinate tmpCoord, double bias) {
        if (tmpCoord != null && Math.abs(this.f484_x - tmpCoord.getX()) < bias && Math.abs(this.f485_y - tmpCoord.getY()) < bias) {
            return true;
        }
        return false;
    }
}
