package  com.xzy.forestSystem.gogisapi.Geometry;

import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Common.CommonMath;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Polygon extends AbstractGeometry {
    private double _Area = 0.0d;
    private Polyline _BorderLine = null;
    private Coordinate _InnerPoint = null;
    private List<Integer> _PartInIndexList = null;

    public Polygon() {
        IsSimple(true);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public void Dispose2() {
        try {
            super.Dispose2();
            if (this._BorderLine != null) {
                this._BorderLine.Dispose2();
            }
            this._BorderLine = null;
            if (this._PartInIndexList != null) {
                this._PartInIndexList.clear();
            }
            this._PartInIndexList = null;
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public void ResetData() {
        super.ResetData();
        this._Area = 0.0d;
        this._InnerPoint = null;
        this._PartInIndexList = null;
        this._BorderLine = null;
    }

    public int getPartInnerIndex(int partIndex) {
        if (this._PartInIndexList == null) {
            CalPartInnerIndexList();
        }
        if (partIndex < this._PartInIndexList.size()) {
            return this._PartInIndexList.get(partIndex).intValue();
        }
        return -1;
    }

    public void CalPartInnerIndexList() {
        int tempCount02;
        if (this._PartIndex.size() > 1) {
            this._PartInIndexList = new ArrayList();
            this._PartInIndexList.add(-1);
            int count = this._PartIndex.size();
            for (int j = 1; j < count; j++) {
                int tmpPartInnerIndex = -1;
                Coordinate tempCoord = (Coordinate) this._CoorList.get(((Integer) this._PartIndex.get(j)).intValue());
                int i = 0;
                while (true) {
                    if (i >= j) {
                        break;
                    }
                    new ArrayList();
                    int tempPtnTID = ((Integer) this._PartIndex.get(i)).intValue();
                    if (i != this._PartIndex.size() - 1) {
                        tempCount02 = ((Integer) this._PartIndex.get(i + 1)).intValue();
                    } else {
                        tempCount02 = this._CoorList.size();
                    }
                    if (tempPtnTID < tempCount02 && tempCount02 - tempPtnTID > 2) {
                        if (isPointInPolygonVertexList(tempCoord.getX(), tempCoord.getY(), this._CoorList.subList(tempPtnTID, tempCount02))) {
                            tmpPartInnerIndex = i;
                            break;
                        }
                    }
                    i++;
                }
                this._PartInIndexList.add(Integer.valueOf(tmpPartInnerIndex));
            }
            return;
        }
        this._PartInIndexList = new ArrayList();
        this._PartInIndexList.add(-1);
    }

    private double CalArea(int paramInt) {
        int tempCount02;
        double result = 0.0d;
        if (paramInt == -1) {
            int count = this._CoorList.size() - 1;
            if (count < 2) {
                return 0.0d;
            }
            List<Coordinate> tempCoords = this._CoorList;
            Coordinate tempCoord01 = tempCoords.get(0);
            for (int i = 1; i < count; i++) {
                Coordinate tempCoord02 = tempCoords.get(i);
                Coordinate tempCoord03 = tempCoords.get(i + 1);
                result += ((tempCoord02.f484_x - tempCoord01.f484_x) * (tempCoord03.f485_y - tempCoord01.f485_y)) - ((tempCoord02.f485_y - tempCoord01.f485_y) * (tempCoord03.f484_x - tempCoord01.f484_x));
            }
            return Math.abs(result) / 2.0d;
        } else if (paramInt < 0) {
            return 0.0d;
        } else {
            new ArrayList();
            int tempPtnTID = ((Integer) this._PartIndex.get(paramInt)).intValue();
            if (paramInt != this._PartIndex.size() - 1) {
                tempCount02 = ((Integer) this._PartIndex.get(paramInt + 1)).intValue();
            } else {
                tempCount02 = this._CoorList.size();
            }
            if (tempPtnTID >= tempCount02 || tempCount02 - tempPtnTID <= 2) {
                return 0.0d;
            }
            List<Coordinate> tempCoords02 = GetAllCoordinateList().subList(tempPtnTID, tempCount02);
            Coordinate tempCoord012 = tempCoords02.get(0);
            int count2 = tempCoords02.size() - 1;
            double tempArea = 0.0d;
            for (int i2 = 1; i2 < count2; i2++) {
                Coordinate tempCoord022 = tempCoords02.get(i2);
                Coordinate tempCoord032 = tempCoords02.get(i2 + 1);
                tempArea += ((tempCoord022.f484_x - tempCoord012.f484_x) * (tempCoord032.f485_y - tempCoord012.f485_y)) - ((tempCoord022.f485_y - tempCoord012.f485_y) * (tempCoord032.f484_x - tempCoord012.f484_x));
            }
            double result2 = Math.abs(tempArea) / 2.0d;
            if (getPartInnerIndex(paramInt) >= 0) {
                return -result2;
            }
            return result2;
        }
    }

    public double ComputeArea() {
        this._Area = 0.0d;
        if (IsSimple()) {
            this._Area = CalArea(-1);
        } else {
            int count = this._PartIndex.size();
            for (int i = 0; i < count; i++) {
                this._Area += CalArea(i);
            }
        }
        return this._Area;
    }

    private Coordinate GetInnerPoint() {
        int tmpSize = GetAllCoordinateList().size();
        if (tmpSize == 1) {
            return (Coordinate) this._CoorList.get(0);
        }
        if (tmpSize == 2) {
            return new Coordinate((((Coordinate) this._CoorList.get(0)).f484_x + ((Coordinate) this._CoorList.get(1)).f484_x) / 2.0d, (((Coordinate) this._CoorList.get(0)).f485_y + ((Coordinate) this._CoorList.get(1)).f485_y) / 2.0d);
        }
        if (tmpSize < 3) {
            return null;
        }
        Coordinate p0 = (Coordinate) this._CoorList.get(0);
        double sum_x = 0.0d;
        double sum_y = 0.0d;
        double sum_area = 0.0d;
        int count = tmpSize - 1;
        for (int i = 1; i < count; i++) {
            Coordinate p1 = (Coordinate) this._CoorList.get(i);
            Coordinate p2 = (Coordinate) this._CoorList.get(i + 1);
            double area = Area(p0, p1, p2);
            sum_area += area;
            sum_x += (p0.f484_x + p1.f484_x + p2.f484_x) * area;
            sum_y += (p0.f485_y + p1.f485_y + p2.f485_y) * area;
        }
        return new Coordinate((sum_x / sum_area) / 3.0d, (sum_y / sum_area) / 3.0d);
    }

    public static double Area(Coordinate p0, Coordinate p1, Coordinate p2) {
        return ((((((p0.f484_x * p1.f485_y) + (p1.f484_x * p2.f485_y)) + (p2.f484_x * p0.f485_y)) - (p1.f484_x * p0.f485_y)) - (p2.f484_x * p1.f485_y)) - (p0.f484_x * p2.f485_y)) / 2.0d;
    }

    private double Multiply(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6) {
        return ((paramDouble3 - paramDouble1) * (paramDouble6 - paramDouble2)) - ((paramDouble5 - paramDouble1) * (paramDouble4 - paramDouble2));
    }

    public Polyline getBorderLine() {
        if (this._BorderLine == null) {
            this._BorderLine = new Polyline();
            if (this._PartIndex == null || this._PartIndex.size() < 2) {
                this._BorderLine.SetAllCoordinateList(GetAllCoordinateList());
            } else if (this._PartIndex.size() > 1) {
                this._BorderLine.SetAllCoordinateList(GetAllCoordinateList().subList(0, ((Integer) this._PartIndex.get(1)).intValue()));
            }
        }
        return this._BorderLine;
    }

    private boolean isIntersect(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6, double paramDouble7, double paramDouble8) {
        double d1 = ((paramDouble3 - paramDouble1) * (paramDouble8 - paramDouble6)) - ((paramDouble4 - paramDouble2) * (paramDouble7 - paramDouble5));
        int i = 0;
        if (d1 < 0.0d) {
            double d2 = (((paramDouble2 - paramDouble6) * (paramDouble7 - paramDouble5)) - ((paramDouble1 - paramDouble5) * (paramDouble8 - paramDouble6))) / d1;
            double d3 = (((paramDouble2 - paramDouble6) * (paramDouble3 - paramDouble1)) - ((paramDouble1 - paramDouble5) * (paramDouble4 - paramDouble2))) / d1;
            i = 0;
            if (!(d2 < 0.0d)) {
                i = 0;
                if (!(d2 < 1.0d)) {
                    i = 0;
                    if (!(d3 < 0.0d)) {
                        i = 0;
                        if (!(d3 < 1.0d)) {
                            i = 1;
                        }
                    }
                }
            }
        }
        if (i == 1) {
            return true;
        }
        return false;
    }

    public boolean isPointInPolygonVertexList(double x, double y, List<Coordinate> coordsList) {
        boolean result = false;
        Coordinate tempCoord02 = coordsList.get(coordsList.size() - 1);
        for (Coordinate tempCoord01 : coordsList) {
            if (((tempCoord01.f484_x < x && tempCoord02.f484_x >= x) || (tempCoord02.f484_x < x && tempCoord01.f484_x >= x)) && tempCoord01.f485_y + (((x - tempCoord01.f484_x) / (tempCoord02.f484_x - tempCoord01.f484_x)) * (tempCoord02.f485_y - tempCoord01.f485_y)) < y) {
                result = !result;
            }
            tempCoord02 = tempCoord01;
        }
        return result;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public AbstractGeometry Clone() {
        Polygon localPolygon = new Polygon();
        ArrayList<Coordinate> localArrayList = new ArrayList<>();
        for (Coordinate coordinate : GetAllCoordinateList()) {
            localArrayList.add(coordinate.Clone());
        }
        localPolygon.SetAllCoordinateList(localArrayList);
        List<Integer> tmpList2 = new ArrayList<>();
        if (this._PartIndex != null && this._PartIndex.size() > 1) {
            for (Integer num : this._PartIndex) {
                tmpList2.add(num);
            }
            localPolygon._PartIndex = this._PartIndex;
        }
        return localPolygon;
    }

    public Polyline ConvertToPolyline() {
        return getBorderLine();
    }

    public List<Coordinate> GetPartOfBorderLineAt(int paramInt) {
        return getBorderLine().GetPartAt(paramInt);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public EGeoLayerType GetType() {
        return EGeoLayerType.POLYGON;
    }

    public boolean IsContainPoint(Coordinate coordinate, boolean needFirstExtendJudge) {
        boolean result = getBorderLine().IsContainPoint(coordinate, needFirstExtendJudge);
        if (IsSimple()) {
            return result;
        }
        int tmpCount = this._PartIndex.size();
        for (int i = 1; i < tmpCount; i++) {
            Polyline tmpPolyline = getSinglePolyline(i);
            if (tmpPolyline != null) {
                if (getBorderLine().IsContainPoint(tmpPolyline.getStartPoint(), true)) {
                    if (tmpPolyline.IsContainPoint(coordinate, false)) {
                        return false;
                    }
                } else if (tmpPolyline.IsContainPoint(coordinate, false)) {
                    return true;
                }
            }
        }
        return result;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public boolean HitTest(Coordinate tmpCoord, double paramDouble) {
        if (!getEnvelope().ContainsPoint(tmpCoord)) {
            return false;
        }
        return IsContainPoint(tmpCoord, false);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public boolean Offset(double paramDouble1, double paramDouble2) {
        return false;
    }

    public void UpdateInnerPoint() {
        this._InnerPoint = GetInnerPoint();
    }

    public double getArea(boolean calculateNow) {
        if (this._Area == 0.0d || calculateNow) {
            this._Area = ComputeArea();
        }
        return this._Area;
    }

    public Coordinate getInnerPoint() {
        if (this._InnerPoint == null) {
            this._InnerPoint = GetInnerPoint();
        }
        return this._InnerPoint;
    }

    public double getLength(boolean calculateNow) {
        return getBorderLine().getLength(calculateNow);
    }

    public int getPartCount() {
        return this._PartIndex.size();
    }

    public List<Coordinate> getVertexList() {
        return getBorderLine().getVertexList();
    }

    public boolean isPointInPolygonEx(double paramDouble1, double paramDouble2) {
        if (IsSimple()) {
            return isPointInPolygonVertexList(paramDouble1, paramDouble2, GetAllCoordinateList());
        }
        if (!isPointInPolygonVertexList(paramDouble1, paramDouble2, this._BorderLine.GetPartAt(0))) {
            return false;
        }
        int i = getPartCount();
        for (int j = 1; j < i; j++) {
            if (isPointInPolygonVertexList(paramDouble1, paramDouble2, GetPartOfBorderLineAt(j))) {
                return false;
            }
        }
        return true;
    }

    public void setVertexList(List<Coordinate> paramList) {
        getBorderLine().setVertexList(paramList);
    }

    public double CalMinDistance(Coordinate coord, Coordinate relateCoord) {
        double result = Double.MAX_VALUE;
        Iterator<Coordinate> tmpIter = this._CoorList.iterator();
        Coordinate tmpCoord01 = tmpIter.next();
        while (tmpIter.hasNext()) {
            Coordinate tmpCoord02 = tmpIter.next();
            Coordinate tmpOutCoord = new Coordinate();
            double tmpD = CommonMath.PointToSegDist(coord.f484_x, coord.f485_y, tmpCoord01.f484_x, tmpCoord01.f485_y, tmpCoord02.f484_x, tmpCoord02.f485_y, tmpOutCoord);
            if (result > tmpD) {
                result = tmpD;
                relateCoord.setX(tmpOutCoord.f484_x);
                relateCoord.setY(tmpOutCoord.f485_y);
            }
            tmpCoord01 = tmpCoord02;
        }
        Coordinate tmpCoord022 = (Coordinate) this._CoorList.get(0);
        Coordinate tmpOutCoord2 = new Coordinate();
        double tmpD2 = CommonMath.PointToSegDist(coord.f484_x, coord.f485_y, tmpCoord01.f484_x, tmpCoord01.f485_y, tmpCoord022.f484_x, tmpCoord022.f485_y, tmpOutCoord2);
        if (result <= tmpD2) {
            return result;
        }
        relateCoord.setX(tmpOutCoord2.f484_x);
        relateCoord.setY(tmpOutCoord2.f485_y);
        return tmpD2;
    }

    public Polyline getSinglePolyline(int partIndex) {
        int tmpI2;
        if (partIndex == 0) {
            return (Polyline) getBorderLine().Clone();
        }
        if (partIndex >= this._PartIndex.size()) {
            return null;
        }
        Polyline tmpPolyline = new Polyline();
        int tmpI = ((Integer) this._PartIndex.get(partIndex)).intValue();
        if (partIndex == this._PartIndex.size() - 1) {
            tmpI2 = this._CoorList.size();
        } else {
            tmpI2 = ((Integer) this._PartIndex.get(partIndex + 1)).intValue();
        }
        tmpPolyline.SetAllCoordinateList(GetAllCoordinateList().subList(tmpI, tmpI2));
        return tmpPolyline;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geometry.AbstractC0543Geometry
    public EGeometryType GetGeometryType() {
        return EGeometryType.POLYGON;
    }

    public boolean IsIntersectWithGeoemtry(AbstractGeometry geometry) {
        Polyline tempPolyline = (Polyline) getBorderLine().Clone();
        tempPolyline.GetAllCoordinateList().add(tempPolyline.getStartPoint());
        if (tempPolyline.getSpatialRelation().JudgeIntersect(geometry)) {
            return true;
        }
        if (!IsSimple()) {
            int tmpCount = this._PartIndex.size();
            for (int i = 1; i < tmpCount; i++) {
                Polyline tmpPolyline = getSinglePolyline(i);
                if (tmpPolyline != null && ((Polyline) tmpPolyline.Clone()).getSpatialRelation().JudgeIntersect(geometry)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Coordinate> GetPartAt(int partIndex) {
        List<Coordinate> result = new ArrayList<>();
        try {
            if (IsSimple()) {
                List<Coordinate> localObject = getItems();
                if (localObject.size() > 0) {
                    for (Coordinate tmpCoordinate : localObject) {
                        result.add(tmpCoordinate.Clone());
                    }
                }
            } else {
                int i = ((Integer) this._PartIndex.get(partIndex)).intValue();
                int j = getItems().size() - 1;
                if (partIndex != this._PartIndex.size() - 1) {
                    j = ((Integer) this._PartIndex.get(partIndex + 1)).intValue() - 1;
                }
                for (int k = i; k <= j; k++) {
                    result.add(getItems().get(k).Clone());
                }
            }
        } catch (Exception e) {
        }
        return result;
    }

    public static List<Polygon> ExplodePolygons(Polygon mainPolygon) {
        List<Polygon> result = new ArrayList<>();
        int tmpCount = mainPolygon.getPartCount();
        for (int tmpI = 0; tmpI < tmpCount; tmpI++) {
            List<Coordinate> tmpList = mainPolygon.GetPartAt(tmpI);
            if (tmpList.size() > 2) {
                Polygon tmpPolygon = new Polygon();
                tmpPolygon.SetAllCoordinateList(tmpList);
                result.add(tmpPolygon);
            }
        }
        return result;
    }

    public boolean AddPoint(Coordinate coordinate, int partIndex, int pointIndex) {
        if (partIndex >= this._PartIndex.size()) {
            return false;
        }
        int count = this._PartIndex.size();
        int totalIndex = 0;
        if (partIndex > 0) {
            totalIndex = ((Integer) this._PartIndex.get(partIndex - 1)).intValue();
        }
        GetAllCoordinateList().add(totalIndex + pointIndex, coordinate);
        CalEnvelope();
        for (int i = partIndex + 1; i < count; i++) {
            this._PartIndex.set(i, Integer.valueOf(((Integer) this._PartIndex.get(i)).intValue() + 1));
        }
        return false;
    }

    public boolean DeletePoint(int partIndex, int pointIndex) {
        int tmpEnd;
        if (partIndex < this._PartIndex.size()) {
            int count = this._PartIndex.size();
            int totalIndex = ((Integer) this._PartIndex.get(partIndex)).intValue();
            int tmpStart = ((Integer) this._PartIndex.get(partIndex)).intValue();
            if (partIndex == this._PartIndex.size() - 1) {
                tmpEnd = GetAllCoordinateList().size();
            } else {
                tmpEnd = ((Integer) this._PartIndex.get(partIndex + 1)).intValue();
            }
            if (tmpEnd - tmpStart < 3) {
                for (int tmpTid = tmpEnd - tmpStart; tmpTid > 0; tmpTid--) {
                    GetAllCoordinateList().remove(tmpStart);
                }
                int tmpTid2 = tmpEnd - tmpStart;
                this._PartIndex.remove(partIndex);
                for (int i = partIndex + 1; i < count; i++) {
                    this._PartIndex.set(i, Integer.valueOf(((Integer) this._PartIndex.get(i)).intValue() - tmpTid2));
                }
                return true;
            }
            GetAllCoordinateList().remove(totalIndex + pointIndex);
            CalEnvelope();
            for (int i2 = partIndex + 1; i2 < count; i2++) {
                this._PartIndex.set(i2, Integer.valueOf(((Integer) this._PartIndex.get(i2)).intValue() - 1));
            }
        }
        return false;
    }
}
