package  com.xzy.forestSystem.gogisapi.Geometry;

import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Edit.EGeometryStatus;
import java.util.ArrayList;
import java.util.List;

public class Point extends AbstractGeometry {
    public Point() {
        setStatus(EGeometryStatus.NONE);
        IsSimple(true);
    }

    public Point(double paramDouble1, double paramDouble2) {
        setItems(new ArrayList());
        getItems().add(new Coordinate(paramDouble1, paramDouble2));
        InitPoint();
    }

    public Point(List<Coordinate> paramList) {
        setItems(paramList);
        InitPoint();
    }

    public Point(Coordinate tmpCoord) {
        setItems(new ArrayList());
        getItems().add(tmpCoord);
        InitPoint();
    }

    private void InitPoint() {
        setStatus(EGeometryStatus.NONE);
        if (PartCount() == 1) {
            IsSimple(true);
        } else {
            IsSimple(false);
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public AbstractGeometry Clone() {
        ArrayList<Coordinate> localArrayList = new ArrayList<>();
        for (Coordinate coordinate : GetAllCoordinateList()) {
            localArrayList.add(coordinate.Clone());
        }
        return new Point(localArrayList);
    }

    public double DistanceTo(Coordinate tmpCoord) {
        double result = Double.MAX_VALUE;
        if (IsSimple()) {
            return Common.GetTwoPointDistance((Coordinate) this._CoorList.get(0), tmpCoord);
        }
        for (Coordinate coordinate : this._CoorList) {
            double tempD = Common.GetTwoPointDistance(coordinate, tmpCoord);
            if (tempD < result) {
                result = tempD;
            }
        }
        return result;
    }

    public List<Coordinate> GetAllPartCoordinate() {
        return getItems();
    }

    public Coordinate GetPart(int paramInt) {
        return getItems().get(paramInt);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public EGeoLayerType GetType() {
        return EGeoLayerType.POINT;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public boolean HitTest(Coordinate tmpCoord, double paramDouble) {
        return DistanceTo(tmpCoord) <= paramDouble;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public boolean Offset(double paramDouble1, double paramDouble2) {
        return false;
    }

    public int PartCount() {
        return getItems().size();
    }

    public double getX() {
        return getItems().get(0).getX();
    }

    public double getY() {
        return getItems().get(0).getY();
    }

    public void setX(double paramDouble) {
        getItems().get(0).setX(paramDouble);
    }

    public void setY(double paramDouble) {
        getItems().get(0).setY(paramDouble);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public EGeometryType GetGeometryType() {
        return EGeometryType.POINT;
    }
}
