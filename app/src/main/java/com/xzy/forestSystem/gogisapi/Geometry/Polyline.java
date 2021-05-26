package  com.xzy.forestSystem.gogisapi.Geometry;

import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Edit.EGeometryStatus;
import  com.xzy.forestSystem.gogisapi.SpatialAnalyst.SpatialRelation;
import java.util.ArrayList;
import java.util.List;

public class Polyline extends AbstractGeometry {
    private double _Length = -1.0d;
    private SpatialRelation _SpatialRelation = null;

    public Polyline() {
        setStatus(EGeometryStatus.NONE);
        IsSimple(true);
    }

    private double CalculateLength() {
        return CalLengthToVertex(getVertexList().size() - 1);
    }

    public double CalLengthToVertex(int paramInt) {
        double d = 0.0d;
        for (int i = 0; i < paramInt; i++) {
            d += Common.GetTwoPointDistance(getVertexList().get(i), getVertexList().get(i + 1));
        }
        return d;
    }

    public double CalLengthToVertex(Coordinate tmpCoord) {
        int i = 0;
        for (Coordinate localCoordinate : getVertexList()) {
            if (Math.abs(localCoordinate.getX() - tmpCoord.getX()) < 1.0d && Math.abs(localCoordinate.getY() - tmpCoord.getY()) < 1.0d) {
                if (i != getVertexList().size()) {
                    return CalLengthToVertex(i);
                }
                i = 0;
            }
            i++;
        }
        return 0.0d;
    }

    public boolean CheckCorner(List<Coordinate> paramList) {
        int i = 1;
        int j = GetAllCoordinateList().size() - 2;
        for (int k = 0; k < j; k++) {
            if (Common.CalAngleByPoints(GetAllCoordinateList().get(k), GetAllCoordinateList().get(k + 1), GetAllCoordinateList().get(k + 2)) < 90.0d) {
                paramList.add(GetAllCoordinateList().get(k + 1).Clone());
                i = 0;
            }
        }
        if (i == 1) {
            return true;
        }
        return false;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public AbstractGeometry Clone() {
        Polyline result = new Polyline();
        ArrayList<Coordinate> localArrayList = new ArrayList<>();
        for (Coordinate coordinate : GetAllCoordinateList()) {
            localArrayList.add(coordinate.Clone());
        }
        result.SetAllCoordinateList(localArrayList);
        List<Integer> tmpList2 = new ArrayList<>();
        if (this._PartIndex != null && this._PartIndex.size() > 1) {
            for (Integer num : this._PartIndex) {
                tmpList2.add(num);
            }
            result._PartIndex = this._PartIndex;
        }
        return result;
    }

    public List<Coordinate> GetPartAt(int paramInt) {
        if (IsSimple()) {
            return getItems();
        }
        ArrayList localObject = new ArrayList();
        int i = ((Integer) this._PartIndex.get(paramInt)).intValue();
        int j = getItems().size() - 1;
        if (paramInt != this._PartIndex.size() - 1) {
            j = ((Integer) this._PartIndex.get(paramInt + 1)).intValue() - 1;
        }
        int k = i;
        while (true) {
            if (k <= j) {
            }
            localObject.add(getItems().get(k));
            k++;
        }
    }

    public boolean GetToStartCoordinate(double paramDouble, BasicValue paramParam, Coordinate tmpCoord) {
        double d = 0.0d;
        for (int i = 0; i <= getVertexList().size() - 2; i++) {
            Coordinate localCoordinate1 = getVertexList().get(i);
            Coordinate localCoordinate2 = getVertexList().get(i + 1);
            d += Common.GetTwoPointDistance(localCoordinate1, localCoordinate2);
            if (d >= paramDouble) {
                new Line(localCoordinate1, localCoordinate2).GetToStartCoordinate(d - paramDouble);
                paramParam.setValue(i);
                return true;
            }
        }
        paramParam.setValue(-1);
        return false;
    }

    public boolean GetToStartDistance(Coordinate tmpCoord, double paramDouble, BasicValue paramParam) {
        double d1 = 0.0d;
        double d2 = 0.0d;
        BasicValue localParam = new BasicValue();
        for (int i = 0; i <= getVertexList().size() - 2; i++) {
            if (i >= 1) {
                d2 += d1;
            }
            Line localLine = new Line(getVertexList().get(i), getVertexList().get(i + 1));
            d1 = localLine.Length();
            if (localLine.PointToLineDistance(tmpCoord, paramDouble, localParam)) {
                paramParam.setValue(localParam.getDouble() + d2);
                return true;
            }
        }
        paramParam.setValue(0.0d);
        return false;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public EGeoLayerType GetType() {
        return EGeoLayerType.POLYLINE;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public boolean HitTest(Coordinate tmpCoord, double paramDouble) {
        return HitTestSegment(tmpCoord, paramDouble, new BasicValue());
    }

    public boolean HitTestSegment(Coordinate tmpCoord, double limitDistance, BasicValue paramParam) {
        if (!getEnvelope().ContainsPoint(tmpCoord)) {
            paramParam.setValue(-1);
            return false;
        }
        int count = getVertexList().size() - 1;
        for (int i = 0; i < count; i++) {
            if (new Line(getVertexList().get(i), getVertexList().get(i + 1)).PointToLineDistance(tmpCoord, limitDistance)) {
                paramParam.setValue(i);
                return true;
            }
        }
        paramParam.setValue(-1);
        return false;
    }

    public boolean HitVertexTest(Coordinate tmpCoord, double limitDistance, BasicValue paramParam) {
        int tmpIndex = -1;
        int count = getVertexList().size();
        double tmpMin = Double.MAX_VALUE;
        for (int i = 0; i < count; i++) {
            double d2 = Common.GetTwoPointDistance(tmpCoord, getVertexList().get(i));
            if (d2 < tmpMin) {
                tmpMin = d2;
                tmpIndex = i;
            }
        }
        if (tmpMin <= limitDistance) {
            paramParam.setValue(tmpIndex);
            return true;
        }
        paramParam.setValue(-1);
        return false;
    }

    public static boolean HitVertexInList(Coordinate hitCoord, List<Coordinate> listCoordinates, double limitDistance, BasicValue paramParam) {
        int tmpIndex = -1;
        int count = listCoordinates.size();
        double tmpMin = Double.MAX_VALUE;
        for (int i = 0; i < count; i++) {
            double d2 = Common.GetTwoPointDistance(hitCoord, listCoordinates.get(i));
            if (d2 < tmpMin) {
                tmpMin = d2;
                tmpIndex = i;
            }
        }
        if (tmpMin <= limitDistance) {
            paramParam.setValue(tmpIndex);
            return true;
        }
        paramParam.setValue(-1);
        return false;
    }

    public int JudgeInVertexList(Coordinate tmpCoord) {
        if (!getEnvelope().ContainsPoint(tmpCoord)) {
            return -1;
        }
        int tid = 0;
        for (Coordinate coordinate : GetAllCoordinateList()) {
            if (tmpCoord.Equal(coordinate)) {
                return tid;
            }
            tid++;
        }
        return -1;
    }

    public void InsertPartAt(int paramInt, List<Coordinate> list) {
    }

    public boolean Intersect(Polyline paramPolyline, List<Coordinate> paramList) {
        if (!getEnvelope().Intersect(paramPolyline.getEnvelope()) && !getEnvelope().Contains(paramPolyline.getEnvelope())) {
            return false;
        }
        int i = GetAllCoordinateList().size();
        int j = paramPolyline.GetAllCoordinateList().size();
        int k = 0;
        if (0 > i - 2) {
            return paramList.size() > 0;
        }
        Line localLine1 = new Line(GetAllCoordinateList().get(0), GetAllCoordinateList().get(1));
        int l = 0;
        while (true) {
            if (l > j - 2) {
                k++;
            }
            Line localLine2 = new Line(paramPolyline.GetAllCoordinateList().get(l), paramPolyline.GetAllCoordinateList().get(l + 1));
            if (localLine1.Intersect(localLine2)) {
                Coordinate localCoordinate1 = new Coordinate();
                Coordinate localCoordinate2 = new Coordinate();
                int i1 = localLine1.Intersect(localLine2, localCoordinate1, localCoordinate2);
                if (i1 == 1) {
                    paramList.add(localCoordinate1.Clone());
                }
                if (i1 == 2) {
                    paramList.add(localCoordinate1.Clone());
                    paramList.add(localCoordinate2.Clone());
                }
            }
            l++;
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public boolean Offset(double paramDouble1, double paramDouble2) {
        return true;
    }

    public boolean Split(Coordinate tmpCoord, int paramInt, Polyline paramPolyline1, Polyline paramPolyline2) {
        Polyline localPolyline1 = new Polyline();
        Polyline localPolyline2 = new Polyline();
        for (int i = 0; i <= getVertexList().size(); i++) {
            if (i <= paramInt) {
                localPolyline1.getVertexList().add(getVertexList().get(i).Clone());
            }
            localPolyline2.getVertexList().add(getVertexList().get(i).Clone());
        }
        localPolyline1.getVertexList().add(tmpCoord.Clone());
        localPolyline2.getVertexList().add(0, tmpCoord.Clone());
        return true;
    }

    public Coordinate getEndPoint() {
        return GetAllCoordinateList().get(GetAllCoordinateList().size() - 1);
    }

    public double getLength(boolean paramBoolean) {
        if (paramBoolean) {
            this._Length = CalculateLength();
        }
        if (this._Length == -1.0d) {
            this._Length = CalculateLength();
        }
        return this._Length;
    }

    public int getPartCount() {
        return this._PartIndex.size();
    }

    public List<Integer> getPartIndex() {
        return this._PartIndex;
    }

    public SpatialRelation getSpatialRelation() {
        if (this._SpatialRelation == null) {
            this._SpatialRelation = new SpatialRelation(this);
        }
        return this._SpatialRelation;
    }

    public Coordinate getStartPoint() {
        return GetAllCoordinateList().get(0);
    }

    public List<Coordinate> getVertexList() {
        if (getItems() == null) {
            setItems(new ArrayList());
        }
        return getItems();
    }

    public void setPartIndex(List<Integer> paramList) {
        this._PartIndex = paramList;
    }

    public void setVertexList(List<Coordinate> paramList) {
        setItems(paramList);
    }

    public boolean IsContainPoint(Coordinate coordinate, boolean needFirstExtendJudge) {
        boolean result = false;
        if (needFirstExtendJudge && !getEnvelope().ContainsPoint(coordinate)) {
            return false;
        }
        int tempCount = getVertexList().size();
        double tempX = coordinate.getX();
        double tempY = coordinate.getY();
        Coordinate tempCoord02 = (Coordinate) this._CoorList.get(tempCount - 1);
        for (Coordinate tempCoord01 : this._CoorList) {
            if (((tempCoord01.f484_x < tempX && tempCoord02.f484_x >= tempX) || (tempCoord02.f484_x < tempX && tempCoord01.f484_x >= tempX)) && tempCoord01.f485_y + (((tempX - tempCoord01.f484_x) / (tempCoord02.f484_x - tempCoord01.f484_x)) * (tempCoord02.f485_y - tempCoord01.f485_y)) < tempY) {
                result = !result;
            }
            tempCoord02 = tempCoord01;
        }
        return result;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public EGeometryType GetGeometryType() {
        return EGeometryType.POLYLINE;
    }
}
