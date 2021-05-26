package  com.xzy.forestSystem.gogisapi.SpatialAnalyst;

import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.Geometry.Line;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import java.util.Iterator;
import java.util.List;

public class SpatialRelation {
    public static final double EPS = 1.0E-7d;
    private AbstractGeometry _Geometry = null;

    public SpatialRelation(AbstractGeometry paramGeometry) {
        this._Geometry = paramGeometry;
    }

    private Polyline ConvertToPolyline() {
        return ConvertToPolyline(this._Geometry);
    }

    private Polyline ConvertToPolyline(AbstractGeometry paramGeometry) {
        int tempType = paramGeometry.GetType().ordinal();
        if (tempType == 1) {
            return (Polyline) paramGeometry;
        }
        if (tempType == 2) {
            return ((Polygon) paramGeometry).ConvertToPolyline();
        }
        return null;
    }

    public boolean IsContains(Envelope paramEnvelope) {
        return paramEnvelope.Contains(this._Geometry.getEnvelope());
    }

    public boolean JudgeIntersect(AbstractGeometry paramGeometry) {
        Polyline localPolyline1 = ConvertToPolyline();
        Polyline localPolyline2 = ConvertToPolyline(paramGeometry);
        if (!localPolyline2.getEnvelope().Intersect(localPolyline1.getEnvelope())) {
            return false;
        }
        int tempCount = localPolyline1.getVertexList().size();
        int tempCount2 = localPolyline2.getVertexList().size();
        if (tempCount > 1 && tempCount2 > 1) {
            int tempCount3 = tempCount - 1;
            int tempCount22 = tempCount2 - 1;
            for (int i = 0; i < tempCount3; i++) {
                Line tempLine01 = new Line(localPolyline1.getVertexList().get(i), localPolyline1.getVertexList().get(i + 1));
                for (int j = 0; j < tempCount22; j++) {
                    if (tempLine01.Intersect(new Line(localPolyline2.getVertexList().get(j), localPolyline2.getVertexList().get(j + 1)))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean JudgeLineIntersectExtend2(double lineStartX, double lineStartY, double lineEndX, double lineEndY, double xmin, double xmax, double ymin, double ymax) {
        double a = lineStartY - lineEndY;
        double b = lineEndX - lineStartX;
        double c = (lineEndY * lineStartX) - (lineEndX * lineStartY);
        if (((a * xmin) + (b * ymax) + c) * ((a * xmax) + (b * ymin) + c) > 0.0d && ((a * xmax) + (b * ymax) + c) * ((a * xmin) + (b * ymin) + c) > 0.0d) {
            return false;
        }
        if ((lineStartX >= xmin || lineEndX >= xmin) && ((lineStartX <= xmax || lineEndX <= xmax) && ((lineStartY <= ymax || lineEndY <= ymax) && (lineStartY >= ymin || lineEndY >= ymin)))) {
            return true;
        }
        return false;
    }

    public static boolean JudgeLineIntersectExtend(double lineStartX, double lineStartY, double lineEndX, double lineEndY, double xmin, double xmax, double ymin, double ymax) {
        double xmin01;
        double xmax01;
        double ymin01;
        double ymax01;
        if (lineStartX < lineEndX) {
            xmin01 = lineStartX;
            xmax01 = lineEndX;
        } else {
            xmin01 = lineEndX;
            xmax01 = lineStartX;
        }
        if (lineStartY < lineEndY) {
            ymin01 = lineStartY;
            ymax01 = lineEndY;
        } else {
            ymin01 = lineEndY;
            ymax01 = lineStartY;
        }
        if (ymax01 < ymin || xmax01 < xmin || ymin01 > ymax || xmin01 > xmax) {
            return false;
        }
        return true;
    }

    public static boolean IsPointInRect(Coordinate p, Envelope rect) {
        return (p.getX() - rect.getMinX()) * (p.getX() - rect.getMaxX()) <= 0.0d && (p.getY() - rect.getMinY()) * (p.getY() - rect.getMaxY()) <= 0.0d;
    }

    public static boolean IsPointInEnvelop(double x, double y, double xmin, double xmax, double ymin, double ymax) {
        return x >= xmin && x <= xmax && y >= ymin && y <= ymax;
    }

    public static double Determinant2D(double v1, double v2, double v3, double v4) {
        return (v1 * v3) - (v2 * v4);
    }

    public static boolean Judge2LineIntersect(double line01X1, double line01Y1, double line01X2, double line01Y2, double line02X1, double line02Y1, double line02X2, double line02Y2) {
        boolean result;
        if (Math.min(line01X1, line01X2) > Math.max(line02X1, line02X2) || Math.min(line02X1, line02X2) > Math.max(line01X1, line01X2) || Math.min(line01Y1, line01Y2) > Math.max(line02Y1, line02Y2) || Math.min(line02Y1, line02Y2) > Math.max(line01Y1, line01Y2)) {
            result = false;
        } else {
            double tempResult01 = ((line02X1 - line01X1) * (line02Y2 - line01Y1)) - ((line02X2 - line01X1) * (line02Y1 - line01Y1));
            if (Math.abs(tempResult01) <= 1.0E-7d) {
                tempResult01 = 0.0d;
            }
            double tempResult02 = ((line02X1 - line01X2) * (line02Y2 - line01Y2)) - ((line02X2 - line01X2) * (line02Y1 - line01Y2));
            if (Math.abs(tempResult02) <= 1.0E-7d) {
                tempResult02 = 0.0d;
            }
            if (tempResult01 * tempResult02 > 0.0d) {
                return false;
            }
            double tempResult03 = ((line02X1 - line01X1) * (line02Y1 - line01Y2)) - ((line02X1 - line01X2) * (line02Y1 - line01Y1));
            if (Math.abs(tempResult03) <= 1.0E-7d) {
                tempResult03 = 0.0d;
            }
            double tempResult04 = ((line02X2 - line01X1) * (line02Y2 - line01Y2)) - ((line02X2 - line01X2) * (line02Y2 - line01Y1));
            if (Math.abs(tempResult04) <= 1.0E-7d) {
                tempResult04 = 0.0d;
            }
            if (tempResult03 * tempResult04 > 0.0d) {
                return false;
            }
            result = true;
        }
        return result;
    }

    public static boolean Judge2LineIntersect2(double line01X1, double line01Y1, double line01X2, double line01Y2, double line02X1, double line02Y1, double line02X2, double line02Y2) {
        double delta = Determinant2D(line01X2 - line01X1, line02X1 - line02X2, line01Y2 - line01Y1, line02Y1 - line02Y2);
        if (delta <= 1.0E-7d && delta >= -1.0E-7d) {
            return false;
        }
        double namenda = Determinant2D(line02X1 - line01X1, line02X1 - line02X2, line02Y1 - line01Y1, line02Y1 - line02Y2) / delta;
        if (namenda > 1.0d || namenda < 0.0d) {
            return false;
        }
        double miu = Determinant2D(line01X2 - line01X1, line02X1 - line01X1, line01Y2 - line01Y1, line02Y1 - line01Y1) / delta;
        if (miu > 1.0d || miu < 0.0d) {
            return false;
        }
        return true;
    }

    public static double CalVectorCross(double p1X, double p1Y, double p2X, double p2Y, double p3X, double p3Y, double p4X, double p4Y) {
        return ((p2X - p1X) * (p4Y - p3Y)) - ((p2Y - p1Y) * (p4X - p3X));
    }

    public static double CalTriArea(double p1X, double p1Y, double p2X, double p2Y, double p3X, double p3Y) {
        return Math.abs(CalVectorCross(p1X, p1Y, p2X, p2Y, p1X, p1Y, p3X, p3Y));
    }

    public static boolean Cal2LineIntersectPoint(double line01X1, double line01Y1, double line01X2, double line01Y2, double line02X1, double line02Y1, double line02X2, double line02Y2, double[] coords) {
        double s1 = CalTriArea(line01X1, line01Y1, line01X2, line01Y2, line02X1, line02Y1);
        double s2 = CalTriArea(line01X1, line01Y1, line01X2, line01Y2, line02X2, line02Y2);
        double tempS = s1 + s2;
        if (tempS == 0.0d) {
            return false;
        }
        coords[0] = ((line02X2 * s1) + (line02X1 * s2)) / tempS;
        coords[1] = ((line02Y2 * s1) + (line02Y1 * s2)) / tempS;
        return true;
    }

    public static boolean Cal2LineSegIntersectPoint(double line01X1, double line01Y1, double line01X2, double line01Y2, double line02X1, double line02Y1, double line02X2, double line02Y2, double[] coords) {
        if (Judge2LineIntersect(line01X1, line01Y1, line01X2, line01Y2, line02X1, line02Y1, line02X2, line02Y2)) {
            return Cal2LineIntersectPoint(line01X1, line01Y1, line01X2, line01Y2, line02X1, line02Y1, line02X2, line02Y2, coords);
        }
        return false;
    }

    public static boolean CalLineSegIntersectPoint(double lineX1, double lineY1, double lineX2, double lineY2, List<Coordinate> lineCoords, double[] coords) {
        return CalLineSegIntersectPoint(lineX1, lineY1, lineX2, lineY2, lineCoords, coords, new BasicValue());
    }

    public static boolean CalLineSegIntersectPoint(double lineX1, double lineY1, double lineX2, double lineY2, List<Coordinate> lineCoords, double[] coords, BasicValue returnLineInedex) {
        Iterator<Coordinate> tmpIter = lineCoords.iterator();
        Coordinate tempCoord = tmpIter.next();
        double line02X1 = tempCoord.getX();
        double line02Y1 = tempCoord.getY();
        int tid = -1;
        while (tmpIter.hasNext()) {
            tid++;
            Coordinate tempCoord2 = tmpIter.next();
            double line02X2 = tempCoord2.getX();
            double line02Y2 = tempCoord2.getY();
            if (!Judge2LineIntersect(lineX1, lineY1, lineX2, lineY2, line02X1, line02Y1, line02X2, line02Y2) || !Cal2LineIntersectPoint(lineX1, lineY1, lineX2, lineY2, line02X1, line02Y1, line02X2, line02Y2, coords)) {
                line02X1 = line02X2;
                line02Y1 = line02Y2;
            } else {
                returnLineInedex.setValue(tid);
                return true;
            }
        }
        return false;
    }

    public static boolean CalLineSegIntersectPoints(double lineX1, double lineY1, double lineX2, double lineY2, List<Coordinate> lineCoords, List<double[]> coordsList, List<Integer> returnLineInedexList) {
        boolean result = false;
        Iterator<Coordinate> tmpIter = lineCoords.iterator();
        Coordinate tempCoord = tmpIter.next();
        double line02X1 = tempCoord.getX();
        double line02Y1 = tempCoord.getY();
        int tid = -1;
        while (tmpIter.hasNext()) {
            tid++;
            Coordinate tempCoord2 = tmpIter.next();
            double line02X2 = tempCoord2.getX();
            double line02Y2 = tempCoord2.getY();
            if (Judge2LineIntersect(lineX1, lineY1, lineX2, lineY2, line02X1, line02Y1, line02X2, line02Y2)) {
                double[] tmpCoord = new double[2];
                if (Cal2LineIntersectPoint(lineX1, lineY1, lineX2, lineY2, line02X1, line02Y1, line02X2, line02Y2, tmpCoord)) {
                    returnLineInedexList.add(Integer.valueOf(tid));
                    coordsList.add(tmpCoord);
                    result = true;
                }
            }
            line02X1 = line02X2;
            line02Y1 = line02Y2;
        }
        return result;
    }

    public static void ArrangLineDirection(double lineStartX, double lineStartY, double lineEndX, double lineEndY, double[] coords) {
        if (lineEndX > lineStartX) {
            if (coords[2] < coords[0]) {
                double tempD = coords[0];
                coords[0] = coords[2];
                coords[2] = tempD;
                double tempD2 = coords[1];
                coords[1] = coords[3];
                coords[3] = tempD2;
            }
        } else if (lineEndX < lineStartX) {
            if (coords[2] > coords[0]) {
                double tempD3 = coords[0];
                coords[0] = coords[2];
                coords[2] = tempD3;
                double tempD4 = coords[1];
                coords[1] = coords[3];
                coords[3] = tempD4;
            }
        } else if (lineEndY > lineStartY) {
            if (coords[3] < coords[1]) {
                double tempD5 = coords[0];
                coords[0] = coords[2];
                coords[2] = tempD5;
                double tempD6 = coords[1];
                coords[1] = coords[3];
                coords[3] = tempD6;
            }
        } else if (lineEndY < lineStartY && coords[3] > coords[1]) {
            double tempD7 = coords[0];
            coords[0] = coords[2];
            coords[2] = tempD7;
            double tempD8 = coords[1];
            coords[1] = coords[3];
            coords[3] = tempD8;
        }
    }

    public static int CalLineEnvelopIntersectPtn(double lineStartX, double lineStartY, double lineEndX, double lineEndY, double xmin, double xmax, double ymin, double ymax, double[] coords) {
        if (!JudgeLineIntersectExtend2(lineStartX, lineStartY, lineEndX, lineEndY, xmin, xmax, ymin, ymax)) {
            return -1;
        }
        boolean tempTag = false;
        if (IsPointInEnvelop(lineStartX, lineStartY, xmin, xmax, ymin, ymax)) {
            tempTag = true;
            coords[0] = lineStartX;
            coords[1] = lineStartY;
        }
        if (IsPointInEnvelop(lineEndX, lineEndY, xmin, xmax, ymin, ymax)) {
            if (tempTag) {
                coords[2] = lineEndX;
                coords[3] = lineEndY;
                return 0;
            }
            coords[0] = lineEndX;
            coords[1] = lineEndY;
            tempTag = true;
        }
        boolean hasCoord = false;
        double[] tempCoords = new double[2];
        if (Cal2LineSegIntersectPoint(lineStartX, lineStartY, lineEndX, lineEndY, xmin, ymin, xmax, ymin, tempCoords)) {
            if (tempTag) {
                coords[2] = tempCoords[0];
                coords[3] = tempCoords[1];
                ArrangLineDirection(lineStartX, lineStartY, lineEndX, lineEndY, coords);
                return 1;
            }
            coords[0] = tempCoords[0];
            coords[1] = tempCoords[1];
            hasCoord = true;
        }
        if (Cal2LineSegIntersectPoint(lineStartX, lineStartY, lineEndX, lineEndY, xmax, ymin, xmax, ymax, tempCoords)) {
            if (tempTag) {
                coords[2] = tempCoords[0];
                coords[3] = tempCoords[1];
                ArrangLineDirection(lineStartX, lineStartY, lineEndX, lineEndY, coords);
                return 2;
            } else if (!hasCoord) {
                coords[0] = tempCoords[0];
                coords[1] = tempCoords[1];
                hasCoord = true;
            } else {
                coords[2] = tempCoords[0];
                coords[3] = tempCoords[1];
                ArrangLineDirection(lineStartX, lineStartY, lineEndX, lineEndY, coords);
                return 2;
            }
        }
        if (Cal2LineSegIntersectPoint(lineStartX, lineStartY, lineEndX, lineEndY, xmin, ymax, xmax, ymax, tempCoords)) {
            if (tempTag) {
                coords[2] = tempCoords[0];
                coords[3] = tempCoords[1];
                ArrangLineDirection(lineStartX, lineStartY, lineEndX, lineEndY, coords);
                return 3;
            } else if (!hasCoord) {
                coords[0] = tempCoords[0];
                coords[1] = tempCoords[1];
                hasCoord = true;
            } else {
                coords[2] = tempCoords[0];
                coords[3] = tempCoords[1];
                ArrangLineDirection(lineStartX, lineStartY, lineEndX, lineEndY, coords);
                return 3;
            }
        }
        if (!Cal2LineSegIntersectPoint(lineStartX, lineStartY, lineEndX, lineEndY, xmin, ymin, xmin, ymax, tempCoords)) {
            return -1;
        }
        if (tempTag) {
            coords[2] = tempCoords[0];
            coords[3] = tempCoords[1];
            ArrangLineDirection(lineStartX, lineStartY, lineEndX, lineEndY, coords);
            return 4;
        } else if (!hasCoord) {
            coords[0] = tempCoords[0];
            coords[1] = tempCoords[1];
            return -1;
        } else {
            coords[2] = tempCoords[0];
            coords[3] = tempCoords[1];
            ArrangLineDirection(lineStartX, lineStartY, lineEndX, lineEndY, coords);
            return 4;
        }
    }

    public static boolean CalLineEnvelopIntersect(double lineStartX, double lineStartY, double lineEndX, double lineEndY, double xmin, double xmax, double ymin, double ymax, double[] coords) {
        return false;
    }

    public static boolean ContainPoint(double pointX, double pointY, double[] coords) {
        boolean result = false;
        if (coords != null) {
            int tempCount = coords.length / 2;
            double tempMinX = coords[0];
            double tempMaxX = coords[0];
            double tempMinY = coords[1];
            double tempMaxY = coords[1];
            for (int i = 1; i < tempCount; i++) {
                int tempI = i * 2;
                if (tempMinX > coords[tempI]) {
                    tempMinX = coords[tempI];
                } else if (tempMaxX < coords[tempI]) {
                    tempMaxX = coords[tempI];
                }
                int tempI2 = tempI + 1;
                if (tempMinY > coords[tempI2]) {
                    tempMinY = coords[tempI2];
                } else if (tempMaxY < coords[tempI2]) {
                    tempMaxY = coords[tempI2];
                }
            }
            if (pointX < tempMinX || pointX > tempMaxX || pointY < tempMinY || pointY > tempMaxY) {
                return false;
            }
            int j = tempCount - 1;
            for (int i2 = 0; i2 < tempCount; i2++) {
                if (((coords[i2 * 2] < pointX && coords[j * 2] >= pointX) || (coords[j * 2] < pointX && coords[i2 * 2] >= pointX)) && coords[(i2 * 2) + 1] + (((pointX - coords[i2 * 2]) / (coords[j * 2] - coords[i2 * 2])) * (coords[(j * 2) + 1] - coords[(i2 * 2) + 1])) < pointY) {
                    result = !result;
                }
                j = i2;
            }
        }
        return result;
    }

    public static boolean ContainPoint(double pointX, double pointY, List<Coordinate> coordinates) {
        if (coordinates.size() <= 2) {
            return false;
        }
        double[] tmpCoords = new double[(coordinates.size() * 2)];
        int tid = 0;
        for (Coordinate tmpCoord : coordinates) {
            int tid2 = tid + 1;
            tmpCoords[tid] = tmpCoord.getX();
            tid = tid2 + 1;
            tmpCoords[tid2] = tmpCoord.getY();
        }
        return ContainPoint(pointX, pointY, tmpCoords);
    }
}
