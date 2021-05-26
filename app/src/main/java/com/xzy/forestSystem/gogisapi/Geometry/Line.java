package  com.xzy.forestSystem.gogisapi.Geometry;

import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;

public class Line {
    private Coordinate _EndPoint;
    private Envelope _Envelope;
    private Coordinate _StartPoint;

    public Line(Coordinate tmpCoord1, Coordinate tmpCoord2) {
        this._StartPoint = tmpCoord1;
        this._EndPoint = tmpCoord2;
    }

    private Coordinate TXb(double paramDouble, Coordinate tmpCoord) {
        return new Coordinate(tmpCoord.getX() * paramDouble, tmpCoord.getY() * paramDouble);
    }

    private Coordinate aAb(Coordinate tmpCoord1, Coordinate tmpCoord2) {
        return new Coordinate(tmpCoord1.getX() + tmpCoord2.getX(), tmpCoord1.getY() + tmpCoord2.getY());
    }

    private Coordinate aDb(Coordinate tmpCoord1, Coordinate tmpCoord2) {
        return new Coordinate(tmpCoord1.getX() - tmpCoord2.getX(), tmpCoord1.getY() - tmpCoord2.getY());
    }

    private double aXb(Coordinate tmpCoord1, Coordinate tmpCoord2) {
        return (tmpCoord1.getX() * tmpCoord2.getX()) + (tmpCoord1.getY() * tmpCoord2.getY());
    }

    public Coordinate GetToStartCoordinate(double paramDouble) {
        double d1 = this._StartPoint.getX();
        double d2 = this._StartPoint.getY();
        double d3 = this._EndPoint.getX();
        double d4 = this._EndPoint.getY();
        double d5 = paramDouble / (Length() - paramDouble);
        return new Coordinate(((d5 * d3) + d1) / (1.0d + d5), ((d5 * d4) + d2) / (1.0d + d5));
    }

    public int Intersect(Line paramLine, Coordinate tmpCoord1, Coordinate tmpCoord2) {
        if (!getEnvelope().Intersect(paramLine.getEnvelope())) {
            return 0;
        }
        double d1 = this._StartPoint.getX();
        double d2 = this._StartPoint.getY();
        double d3 = this._EndPoint.getX();
        double d4 = this._EndPoint.getY();
        double d5 = paramLine._StartPoint.getX();
        double d6 = paramLine._StartPoint.getY();
        double d7 = paramLine._EndPoint.getX();
        double d8 = paramLine._EndPoint.getY();
        double d9 = ((d4 - d2) * (d7 - d5)) - ((d8 - d6) * (d3 - d1));
        if (d9 != 0.0d && PointOnLine(new Coordinate((((((d3 - d1) * (d7 - d5)) * (d6 - d2)) + (((d4 - d2) * (d7 - d5)) * d1)) - (((d8 - d6) * (d3 - d1)) * d5)) / d9, (((((d4 - d2) * (d8 - d6)) * (d5 - d1)) + (((d3 - d1) * (d8 - d6)) * d2)) - (((d7 - d5) * (d4 - d2)) * d6)) / (-d9)))) {
            return 1;
        }
        return 0;
    }

    public boolean Intersect(Line paramLine) {
        Coordinate localCoordinate1 = paramLine._StartPoint;
        Coordinate localCoordinate2 = paramLine._EndPoint;
        Coordinate localCoordinate3 = this._StartPoint;
        Coordinate localCoordinate4 = this._EndPoint;
        double d1 = localCoordinate1.getX() - localCoordinate3.getX();
        double d2 = localCoordinate1.getY() - localCoordinate3.getY();
        double d3 = localCoordinate4.getX() - localCoordinate3.getX();
        double d4 = localCoordinate4.getY() - localCoordinate3.getY();
        if (((d1 * d4) - (d2 * d3)) * ((d3 * (localCoordinate2.getY() - localCoordinate3.getY())) - (d4 * (localCoordinate2.getX() - localCoordinate3.getX()))) < 0.0d) {
            return false;
        }
        double d7 = localCoordinate3.getX() - localCoordinate1.getX();
        double d8 = localCoordinate3.getY() - localCoordinate1.getY();
        double d9 = localCoordinate2.getX() - localCoordinate1.getX();
        double d10 = localCoordinate2.getY() - localCoordinate1.getY();
        return ((d7 * d10) - (d8 * d9)) * ((d9 * (localCoordinate4.getY() - localCoordinate1.getY())) - (d10 * (localCoordinate4.getX() - localCoordinate1.getX()))) >= 0.0d;
    }

    public double Length() {
        return Common.GetTwoPointDistance(this._StartPoint, this._EndPoint);
    }

    public boolean PointOnLine(Coordinate tmpCoord) {
        BasicValue localParam = new BasicValue();
        PointToLineNearestDistance(tmpCoord, localParam, null);
        return localParam.getBoolean() && Common.GetTwoPointDistance(tmpCoord, null) < 0.5d;
    }

    public boolean PointToLineDistance(Coordinate tmpCoord, double paramDouble) {
        return PointToLineNearestDistance(tmpCoord, new BasicValue(), null) <= paramDouble;
    }

    public boolean PointToLineDistance(Coordinate tmpCoord, double paramDouble, BasicValue paramParam) {
        BasicValue localParam = new BasicValue();
        double d = PointToLineNearestDistance(tmpCoord, localParam, null);
        boolean bool = localParam.getBoolean();
        if (d <= paramDouble) {
            if (bool) {
                d = Common.GetTwoPointDistance(this._StartPoint, null);
            }
            paramParam.setValue(d);
            return true;
        }
        paramParam.setValue(d);
        return false;
    }

    public boolean PointToLineDistance(Coordinate tmpCoord1, double paramDouble, BasicValue paramParam, Coordinate tmpCoord2, boolean paramBoolean) {
        BasicValue localParam = new BasicValue();
        localParam.setValue(paramBoolean);
        double d = PointToLineNearestDistance(tmpCoord1, localParam, tmpCoord2);
        if (d <= paramDouble) {
            if (paramBoolean) {
                d = Common.GetTwoPointDistance(this._StartPoint, tmpCoord2);
            }
            paramParam.setValue(d);
            return true;
        }
        paramParam.setValue(d);
        return false;
    }

    public double PointToLineNearestDistance(Coordinate tmpCoord) {
        BasicValue localParam = new BasicValue();
        localParam.setValue(false);
        return PointToLineNearestDistance(tmpCoord, localParam, null);
    }

    public double PointToLineNearestDistance(Coordinate tmpCoord1, BasicValue paramParam, Coordinate tmpCoord2) {
        paramParam.setValue(false);
        Coordinate localCoordinate1 = this._StartPoint;
        Coordinate localCoordinate2 = this._EndPoint;
        Coordinate localCoordinate3 = aDb(localCoordinate2, localCoordinate1);
        double d1 = aXb(localCoordinate3, aDb(tmpCoord1, localCoordinate1));
        if (d1 <= 0.0d) {
            return Common.GetTwoPointDistance(tmpCoord1, localCoordinate1);
        }
        double d2 = aXb(localCoordinate3, localCoordinate3);
        if (d1 > d2) {
            return Common.GetTwoPointDistance(tmpCoord1, localCoordinate2);
        }
        Coordinate localCoordinate4 = aAb(localCoordinate1, TXb(d1 / d2, localCoordinate3));
        paramParam.setValue(true);
        return Common.GetTwoPointDistance(tmpCoord1, localCoordinate4);
    }

    public Coordinate getEndPoint() {
        return this._EndPoint;
    }

    public Envelope getEnvelope() {
        double d1;
        double d2;
        double d3;
        double d4;
        if (this._Envelope == null) {
            if (this._StartPoint.getX() <= this._EndPoint.getX()) {
                d1 = this._StartPoint.getX();
                d2 = this._EndPoint.getX();
            } else {
                d1 = this._EndPoint.getX();
                d2 = this._StartPoint.getX();
            }
            if (this._StartPoint.getY() <= this._EndPoint.getY()) {
                d3 = this._StartPoint.getY();
                d4 = this._EndPoint.getY();
            } else {
                d3 = this._EndPoint.getY();
                d4 = this._StartPoint.getY();
            }
            this._Envelope = new Envelope(d1, d4, d2, d3);
        }
        return this._Envelope;
    }

    public Coordinate getStartPoint() {
        return this._StartPoint;
    }
}
