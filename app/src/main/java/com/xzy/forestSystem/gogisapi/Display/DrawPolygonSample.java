package  com.xzy.forestSystem.gogisapi.Display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DrawPolygonSample {
    static final int DefaultFillColor = -5526613;
    static final int DefaultLineColor = -16776961;
    static final int DefaultLineWidth = ((int) PubVar.ScaledDensity);
    static final int ResultFillColor = -16711936;
    static final int ResultLineColor = -65536;
    static final int ResultLineWidth = (((int) PubVar.ScaledDensity) * 2);

    public static Point MapToScreen(double x, double y, double scale, double leftX, double TopY) {
        return new Point((int) ((x - leftX) * scale), (int) ((TopY - y) * scale));
    }

    public static Point[] MapPointsToScreePoints(List<Coordinate> paramList, double scale, double leftX, double TopY, int biasX, int biasY) {
        ArrayList result = new ArrayList();
        int tempI = 0;
        for (Coordinate tmpCoord : paramList) {
            boolean tempTag = false;
            Point localPoint = MapToScreen(tmpCoord.getX(), tmpCoord.getY(), scale, leftX, TopY);
            int tempX = localPoint.x;
            int tempY = localPoint.y;
            if (!(tempI > 0 && ((Point) result.get(tempI - 1)).x + biasX == tempX && ((Point) result.get(tempI - 1)).y + biasY == tempY)) {
                tempTag = true;
            }
            if (tempTag) {
                result.add(new Point(tempX + biasX, tempY + biasY));
                tempI++;
            }
        }
        return (Point[]) result.toArray(new Point[0]);
    }

    public static Bitmap DrawUnionPolygons(List<Polygon> polygons, int width, int height) {
        int tempCount02;
        Path tmpPath2;
        Path tmpPath22;
        Path tmpPath23;
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        if (polygons.size() != 0) {
            Envelope tempExtend = polygons.get(0).getEnvelope().Clone();
            Iterator<Polygon> tempIter = polygons.iterator();
            if (tempIter.hasNext()) {
                tempIter.next();
                while (tempIter.hasNext()) {
                    tempExtend = tempExtend.MergeEnvelope(tempIter.next().getEnvelope());
                }
            }
            double tmpRatio = ((double) width) / tempExtend.getWidth();
            double tmpRatio2 = ((double) height) / tempExtend.getHeight();
            if (tmpRatio > tmpRatio2) {
                tmpRatio = tmpRatio2;
            }
            double tmpLeftX = tempExtend.getMinX();
            double tmpTopY = tempExtend.getMaxY();
            Canvas canvas = new Canvas(result);
            Paint tmpPaint01 = new Paint();
            tmpPaint01.setColor(DefaultFillColor);
            tmpPaint01.setStyle(Paint.Style.FILL);
            tmpPaint01.setAntiAlias(true);
            Paint tmpPaint02 = new Paint();
            tmpPaint02.setColor(DefaultLineColor);
            tmpPaint02.setStrokeWidth((float) DefaultLineWidth);
            tmpPaint02.setStyle(Paint.Style.STROKE);
            tmpPaint02.setAntiAlias(true);
            Path tmpBordPath = new Path();
            for (Polygon tmpPoly : polygons) {
                Path tmpPath = new Path();
                if (tmpPoly.IsSimple()) {
                    Point[] tmpPtns = MapPointsToScreePoints(tmpPoly.GetAllCoordinateList(), tmpRatio, tmpLeftX, tmpTopY, DefaultLineWidth, DefaultLineWidth);
                    if (!(tmpPtns == null || (tmpPath23 = PolygonSymbol.CreatePath(tmpPtns, 0, 0)) == null)) {
                        tmpPath.addPath(tmpPath23);
                    }
                } else {
                    int tempCount00 = tmpPoly.GetNumberOfParts();
                    int tempPtnTID = tmpPoly.GetPartIndex().get(1).intValue();
                    List<Coordinate> tempCoords00 = tmpPoly.GetAllCoordinateList();
                    List<Coordinate> tempCoords01 = tmpPoly.GetAllCoordinateList().subList(0, tempPtnTID);
                    Point[] tmpPtns2 = MapPointsToScreePoints(tempCoords01, tmpRatio, tmpLeftX, tmpTopY, DefaultLineWidth, DefaultLineWidth);
                    if (!(tmpPtns2 == null || (tmpPath22 = PolygonSymbol.CreatePath(tmpPtns2, 0, 0)) == null)) {
                        tmpPath.addPath(tmpPath22);
                    }
                    for (int i0 = 1; i0 < tempCount00; i0++) {
                        new ArrayList();
                        if (i0 != tempCount00 - 1) {
                            tempCount02 = tmpPoly.GetPartIndex().get(i0 + 1).intValue();
                        } else {
                            tempCount02 = tempCoords00.size();
                        }
                        if (tempPtnTID < tempCount02) {
                            tmpPoly.GetAllCoordinateList().subList(tempPtnTID, tempCount02);
                            Point[] tmpPtns22 = MapPointsToScreePoints(tempCoords01, tmpRatio, tmpLeftX, tmpTopY, DefaultLineWidth, DefaultLineWidth);
                            if (!(tmpPtns22 == null || (tmpPath2 = PolygonSymbol.CreatePath(tmpPtns22, 0, 0)) == null)) {
                                tmpPath.addPath(tmpPath2);
                            }
                        }
                        tempPtnTID = tempCount02;
                    }
                    tmpPath.setFillType(Path.FillType.EVEN_ODD);
                }
                canvas.drawPath(tmpPath, tmpPaint01);
                tmpBordPath.addPath(tmpPath);
            }
            if (!tmpBordPath.isEmpty()) {
                canvas.drawPath(tmpBordPath, tmpPaint02);
            }
        }
        return result;
    }

    public static Bitmap DrawPolygons(List<Polygon> polygons, List<Integer> mainPolyIndex, int width, int height) {
        int tempCount02;
        Path tmpPath2;
        Path tmpPath22;
        Path tmpPath23;
        int tempCount022;
        Point[] tmpPtns2;
        Path tmpPath24;
        Path tmpPath25;
        Path tmpPath26;
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        if (polygons.size() != 0) {
            Envelope tempExtend = polygons.get(0).getEnvelope().Clone();
            Iterator<Polygon> tempIter = polygons.iterator();
            if (tempIter.hasNext()) {
                tempIter.next();
                while (tempIter.hasNext()) {
                    tempExtend = tempExtend.MergeEnvelope(tempIter.next().getEnvelope());
                }
            }
            double tmpRatio = ((double) width) / tempExtend.getWidth();
            double tmpRatio2 = ((double) height) / tempExtend.getHeight();
            if (tmpRatio > tmpRatio2) {
                tmpRatio = tmpRatio2;
            }
            double tmpLeftX = tempExtend.getMinX();
            double tmpTopY = tempExtend.getMaxY();
            Canvas canvas = new Canvas(result);
            Paint tmpPaint01 = new Paint();
            tmpPaint01.setColor(DefaultFillColor);
            tmpPaint01.setStyle(Paint.Style.FILL);
            tmpPaint01.setAntiAlias(true);
            Paint tmpPaint02 = new Paint();
            tmpPaint02.setColor(DefaultLineColor);
            tmpPaint02.setStrokeWidth((float) DefaultLineWidth);
            tmpPaint02.setStyle(Paint.Style.STROKE);
            tmpPaint02.setAntiAlias(true);
            Path tmpBordPath = new Path();
            int tid = -1;
            for (Polygon tmpPoly : polygons) {
                tid++;
                if (!mainPolyIndex.contains(Integer.valueOf(tid))) {
                    Path tmpPath = new Path();
                    if (tmpPoly.IsSimple()) {
                        Point[] tmpPtns = MapPointsToScreePoints(tmpPoly.GetAllCoordinateList(), tmpRatio, tmpLeftX, tmpTopY, DefaultLineWidth, DefaultLineWidth);
                        if (!(tmpPtns == null || (tmpPath26 = PolygonSymbol.CreatePath(tmpPtns, 0, 0)) == null)) {
                            tmpPath.addPath(tmpPath26);
                        }
                    } else {
                        int tempCount00 = tmpPoly.GetNumberOfParts();
                        int tempPtnTID = tmpPoly.GetPartIndex().get(1).intValue();
                        List<Coordinate> tempCoords00 = tmpPoly.GetAllCoordinateList();
                        Point[] tmpPtns3 = MapPointsToScreePoints(tmpPoly.GetAllCoordinateList().subList(0, tempPtnTID), tmpRatio, tmpLeftX, tmpTopY, DefaultLineWidth, DefaultLineWidth);
                        if (!(tmpPtns3 == null || (tmpPath25 = PolygonSymbol.CreatePath(tmpPtns3, 0, 0)) == null)) {
                            tmpPath.addPath(tmpPath25);
                        }
                        for (int i0 = 1; i0 < tempCount00; i0++) {
                            new ArrayList();
                            if (i0 != tempCount00 - 1) {
                                tempCount022 = tmpPoly.GetPartIndex().get(i0 + 1).intValue();
                            } else {
                                tempCount022 = tempCoords00.size();
                            }
                            if (!(tempPtnTID >= tempCount022 || (tmpPtns2 = MapPointsToScreePoints(tmpPoly.GetAllCoordinateList().subList(tempPtnTID, tempCount022), tmpRatio, tmpLeftX, tmpTopY, DefaultLineWidth, DefaultLineWidth)) == null || (tmpPath24 = PolygonSymbol.CreatePath(tmpPtns2, 0, 0)) == null)) {
                                tmpPath.addPath(tmpPath24);
                            }
                            tempPtnTID = tempCount022;
                        }
                        tmpPath.setFillType(Path.FillType.EVEN_ODD);
                    }
                    canvas.drawPath(tmpPath, tmpPaint01);
                    tmpBordPath.addPath(tmpPath);
                }
            }
            if (!tmpBordPath.isEmpty()) {
                canvas.drawPath(tmpBordPath, tmpPaint02);
            }
            if (mainPolyIndex.size() > 0) {
                tmpPaint01.setColor(ResultFillColor);
                tmpPaint02.setColor(-65536);
                tmpPaint02.setStrokeWidth((float) ResultLineWidth);
                Path tmpBordPath2 = new Path();
                for (Integer num : mainPolyIndex) {
                    Polygon tmpPoly2 = polygons.get(num.intValue());
                    Path tmpPath3 = new Path();
                    if (tmpPoly2.IsSimple()) {
                        Point[] tmpPtns4 = MapPointsToScreePoints(tmpPoly2.GetAllCoordinateList(), tmpRatio, tmpLeftX, tmpTopY, DefaultLineWidth, DefaultLineWidth);
                        if (!(tmpPtns4 == null || (tmpPath23 = PolygonSymbol.CreatePath(tmpPtns4, 0, 0)) == null)) {
                            tmpPath3.addPath(tmpPath23);
                        }
                    } else {
                        int tempCount002 = tmpPoly2.GetNumberOfParts();
                        int tempPtnTID2 = tmpPoly2.GetPartIndex().get(1).intValue();
                        List<Coordinate> tempCoords002 = tmpPoly2.GetAllCoordinateList();
                        List<Coordinate> tempCoords01 = tmpPoly2.GetAllCoordinateList().subList(0, tempPtnTID2);
                        Point[] tmpPtns5 = MapPointsToScreePoints(tempCoords01, tmpRatio, tmpLeftX, tmpTopY, DefaultLineWidth, DefaultLineWidth);
                        if (!(tmpPtns5 == null || (tmpPath22 = PolygonSymbol.CreatePath(tmpPtns5, 0, 0)) == null)) {
                            tmpPath3.addPath(tmpPath22);
                        }
                        for (int i02 = 1; i02 < tempCount002; i02++) {
                            new ArrayList();
                            if (i02 != tempCount002 - 1) {
                                tempCount02 = tmpPoly2.GetPartIndex().get(i02 + 1).intValue();
                            } else {
                                tempCount02 = tempCoords002.size();
                            }
                            if (tempPtnTID2 < tempCount02) {
                                tmpPoly2.GetAllCoordinateList().subList(tempPtnTID2, tempCount02);
                                Point[] tmpPtns22 = MapPointsToScreePoints(tempCoords01, tmpRatio, tmpLeftX, tmpTopY, DefaultLineWidth, DefaultLineWidth);
                                if (!(tmpPtns22 == null || (tmpPath2 = PolygonSymbol.CreatePath(tmpPtns22, 0, 0)) == null)) {
                                    tmpPath3.addPath(tmpPath2);
                                }
                            }
                            tempPtnTID2 = tempCount02;
                        }
                        tmpPath3.setFillType(Path.FillType.EVEN_ODD);
                    }
                    canvas.drawPath(tmpPath3, tmpPaint01);
                    tmpBordPath2.addPath(tmpPath3);
                }
                if (!tmpBordPath2.isEmpty()) {
                    canvas.drawPath(tmpBordPath2, tmpPaint02);
                }
            }
        }
        return result;
    }

    public static Bitmap DrawPolygons(List<Polygon> polygons, int width, int height) {
        int tempCount02;
        Path tmpPath2;
        Path tmpPath22;
        Path tmpPath23;
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        if (polygons.size() != 0) {
            Envelope tempExtend = polygons.get(0).getEnvelope().Clone();
            Iterator<Polygon> tempIter = polygons.iterator();
            if (tempIter.hasNext()) {
                tempIter.next();
                while (tempIter.hasNext()) {
                    tempExtend = tempExtend.MergeEnvelope(tempIter.next().getEnvelope());
                }
            }
            double tmpRatio = ((double) width) / tempExtend.getWidth();
            double tmpRatio2 = ((double) height) / tempExtend.getHeight();
            if (tmpRatio > tmpRatio2) {
                tmpRatio = tmpRatio2;
            }
            double tmpLeftX = tempExtend.getMinX();
            double tmpTopY = tempExtend.getMaxY();
            Canvas canvas = new Canvas(result);
            Paint tmpPaint01 = new Paint();
            tmpPaint01.setColor(-65536);
            tmpPaint01.setStyle(Paint.Style.FILL);
            tmpPaint01.setAntiAlias(true);
            Paint tmpPaint02 = new Paint();
            tmpPaint02.setColor(DefaultLineColor);
            tmpPaint02.setStrokeWidth((float) DefaultLineWidth);
            tmpPaint02.setStyle(Paint.Style.STROKE);
            tmpPaint02.setAntiAlias(true);
            for (Polygon tmpPoly : polygons) {
                Path tmpPath = new Path();
                if (tmpPoly.IsSimple()) {
                    Point[] tmpPtns = MapPointsToScreePoints(tmpPoly.GetAllCoordinateList(), tmpRatio, tmpLeftX, tmpTopY, DefaultLineWidth, DefaultLineWidth);
                    if (!(tmpPtns == null || (tmpPath23 = PolygonSymbol.CreatePath(tmpPtns, 0, 0)) == null)) {
                        tmpPath.addPath(tmpPath23);
                    }
                } else {
                    int tempCount00 = tmpPoly.GetNumberOfParts();
                    int tempPtnTID = tmpPoly.GetPartIndex().get(1).intValue();
                    List<Coordinate> tempCoords00 = tmpPoly.GetAllCoordinateList();
                    List<Coordinate> tempCoords01 = tmpPoly.GetAllCoordinateList().subList(0, tempPtnTID);
                    Point[] tmpPtns2 = MapPointsToScreePoints(tempCoords01, tmpRatio, tmpLeftX, tmpTopY, DefaultLineWidth, DefaultLineWidth);
                    if (!(tmpPtns2 == null || (tmpPath22 = PolygonSymbol.CreatePath(tmpPtns2, 0, 0)) == null)) {
                        tmpPath.addPath(tmpPath22);
                    }
                    for (int i0 = 1; i0 < tempCount00; i0++) {
                        new ArrayList();
                        if (i0 != tempCount00 - 1) {
                            tempCount02 = tmpPoly.GetPartIndex().get(i0 + 1).intValue();
                        } else {
                            tempCount02 = tempCoords00.size();
                        }
                        if (tempPtnTID < tempCount02) {
                            tmpPoly.GetAllCoordinateList().subList(tempPtnTID, tempCount02);
                            Point[] tmpPtns22 = MapPointsToScreePoints(tempCoords01, tmpRatio, tmpLeftX, tmpTopY, DefaultLineWidth, DefaultLineWidth);
                            if (!(tmpPtns22 == null || (tmpPath2 = PolygonSymbol.CreatePath(tmpPtns22, 0, 0)) == null)) {
                                tmpPath.addPath(tmpPath2);
                            }
                        }
                        tempPtnTID = tempCount02;
                    }
                    tmpPath.setFillType(Path.FillType.EVEN_ODD);
                }
                canvas.drawPath(tmpPath, tmpPaint01);
                canvas.drawPath(tmpPath, tmpPaint02);
            }
        }
        return result;
    }
}
