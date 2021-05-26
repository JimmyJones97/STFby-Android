package  com.xzy.forestSystem.gogisapi.Navigation;

import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;

public class NavigatePoint {
    Coordinate _Coordinate = new Coordinate();
    float _Direction = 0.0f;
    double _Distance = 0.0d;
    boolean _HasDrawLocation = false;
    boolean _IsLocation = false;
    boolean _IsVisible = true;
    String _Name = "";
    double _XDistance = 0.0d;
    double _YDistance = 0.0d;

    public void SetName(String name) {
        this._Name = name;
    }

    public boolean IsVisible() {
        return this._IsVisible;
    }

    public void SetVisible(boolean isVisible) {
        this._IsVisible = isVisible;
    }

    public NavigatePoint Clone() {
        NavigatePoint tempPoint = new NavigatePoint();
        tempPoint._Name = this._Name;
        tempPoint._Coordinate = this._Coordinate.Clone();
        tempPoint._Direction = this._Direction;
        tempPoint._XDistance = this._XDistance;
        tempPoint._YDistance = this._YDistance;
        tempPoint._Distance = this._Distance;
        tempPoint._IsLocation = this._IsLocation;
        tempPoint._HasDrawLocation = this._HasDrawLocation;
        return tempPoint;
    }

    public void SetCoordinate(Coordinate coordinate) {
        this._Coordinate = coordinate;
    }

    public void UpdateLocation(Coordinate gpsCoordOfXY, double alarmDistance) {
        if (gpsCoordOfXY != null) {
            this._XDistance = this._Coordinate.getX() - gpsCoordOfXY.getX();
            this._YDistance = this._Coordinate.getY() - gpsCoordOfXY.getY();
            this._Distance = Math.sqrt((this._XDistance * this._XDistance) + (this._YDistance * this._YDistance));
            if (this._YDistance != 0.0d) {
                this._Direction = (float) Math.toDegrees(Math.atan(this._XDistance / this._YDistance));
                if (this._Direction > 0.0f) {
                    if (this._XDistance < 0.0d) {
                        this._Direction = (float) (((double) this._Direction) + 180.0d);
                    }
                } else if (this._YDistance > 0.0d) {
                    this._Direction += 360.0f;
                } else {
                    this._Direction += 180.0f;
                }
                if (this._Direction < 0.0f) {
                    this._Direction += 360.0f;
                }
                if (this._Direction > 360.0f) {
                    this._Direction -= 360.0f;
                }
            } else if (this._XDistance > 0.0d) {
                this._Direction = 90.0f;
            } else {
                this._Direction = 270.0f;
            }
            if (this._Distance <= alarmDistance) {
                this._IsLocation = true;
            } else {
                this._IsLocation = false;
            }
        }
    }
}
