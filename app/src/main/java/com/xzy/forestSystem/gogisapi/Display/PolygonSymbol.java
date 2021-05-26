package  com.xzy.forestSystem.gogisapi.Display;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import androidx.core.internal.view.SupportMenu;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Map;
import  com.xzy.forestSystem.gogisapi.Common.CommonProcess;
import  com.xzy.forestSystem.gogisapi.Edit.EDrawType;
import  com.xzy.forestSystem.gogisapi.Edit.EGeometryStatus;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.EGeoDisplayType;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PolygonSymbol extends ISymbol {
    static final int DefaultFillColor = -16744448;
    static final int DefaultLineColor = -65536;
    static final int DefaultLineWidth = 5;
    static final int SeletedLineColor = -16711681;
    static final int SeletedLineNodeColor = -65536;
    static final int SeletedLineNodeSize = ((int) (6.0f * PubVar.ScaledDensity));
    private Paint _ClearBrush = null;
    private Paint _LBrush = null;
    private Paint _PBrush = null;
    private Paint _SelectedLinePaint = null;

    public PolygonSymbol() {
        setName("默认");
        Paint localPaint1 = new Paint((int) DefaultFillColor);
        Paint localPaint2 = new Paint((int) SupportMenu.CATEGORY_MASK);
        localPaint2.setStrokeWidth(5.0f);
        setPStyle(localPaint1);
        setLStyle(localPaint2);
        this._ClearBrush = new Paint(0);
        this._SelectedLinePaint = new Paint();
        this._SelectedLinePaint.setColor(SeletedLineColor);
        this._SelectedLinePaint.setStyle(Paint.Style.STROKE);
        this._SelectedLinePaint.setAntiAlias(true);
        this._SelectedLinePaint.setStrokeWidth(3.0f * PubVar.ScaledDensity);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void Dispose2() {
        try {
            if (this._SymbolFigure != null && !this._SymbolFigure.isRecycled()) {
                this._SymbolFigure.recycle();
            }
            this._SymbolFigure = null;
        } catch (Exception e) {
        }
    }

    public static Path CreatePath(Point[] paramArrayOfPoint, int paramInt1, int paramInt2) {
        Path localPath = new Path();
        int count = paramArrayOfPoint.length;
        localPath.moveTo((float) (paramArrayOfPoint[0].x + paramInt1), (float) (paramArrayOfPoint[0].y + paramInt2));
        for (int i = 1; i < count; i++) {
            localPath.lineTo((float) (paramArrayOfPoint[i].x + paramInt1), (float) (paramArrayOfPoint[i].y + paramInt2));
        }
        localPath.lineTo((float) (paramArrayOfPoint[0].x + paramInt1), (float) (paramArrayOfPoint[0].y + paramInt2));
        return localPath;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public Bitmap getSymbolFigure() {
        if (this._SymbolFigure == null) {
            DrawSymbolFigure();
        }
        return this._SymbolFigure;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public Bitmap DrawSymbolFigure() {
        String[] arrayOfString;
        this._SymbolFigure = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888);
        try {
            Canvas localCanvas2 = new Canvas(this._SymbolFigure);
            if (!this._ConfigParas.equals("") && (arrayOfString = this._ConfigParas.split(",")) != null && arrayOfString.length > 2) {
                String str7 = arrayOfString[0];
                String str8 = arrayOfString[1];
                String str9 = arrayOfString[2];
                Paint localPaint2 = new Paint();
                localPaint2.setStyle(Paint.Style.FILL);
                localPaint2.setAntiAlias(true);
                if (str7.startsWith("#")) {
                    localPaint2.setColor(Color.parseColor(str7));
                } else {
                    localPaint2.setShader(new BitmapShader(CommonProcess.Base64ToBitmap(str7), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
                }
                localCanvas2.drawRect(0.0f, 0.0f, 64.0f, 64.0f, localPaint2);
                Paint localPaint3 = new Paint();
                localPaint3.setColor(Color.parseColor(str8));
                localPaint3.setStyle(Paint.Style.STROKE);
                float tmpWidth = getLStyle().getStrokeWidth();
                localPaint3.setStrokeWidth(Float.valueOf(str9).floatValue());
                localCanvas2.drawRect(0.0f, 0.0f, 64.0f - tmpWidth, 64.0f - tmpWidth, localPaint3);
            }
        } catch (Exception e) {
        }
        return this._SymbolFigure;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void UpdateTransparent() {
        super.UpdateTransparent();
        this._PBrush.setAlpha(255 - this._Transparent);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void Draw(Map paramMap, Canvas paramCanvas, AbstractGeometry paramGeometry, int offsetX, int offsetY, EDrawType paramlkDrawType) {
        Point[] tempPoints01;
        int tempCount02;
        Point[] tempPoints02;
        Point[] arrayOfPoint;
        if (paramGeometry.getStatus() != EGeometryStatus.DELETE && paramMap.getExtend().Intersect(paramGeometry.getEnvelope())) {
            Polygon localPolygon = (Polygon) paramGeometry;
            Path localPathPoly = null;
            Path localPathLine = null;
            if (paramGeometry.IsSimple()) {
                if (paramMap.getExtend().Contains(paramGeometry.getEnvelope())) {
                    arrayOfPoint = paramMap.MapPointsToScreePoints(localPolygon.GetAllCoordinateList(), true, offsetX, offsetY);
                } else {
                    arrayOfPoint = paramMap.ClipPolygon(localPolygon.GetAllCoordinateList(), offsetX, offsetY);
                }
                if (arrayOfPoint != null && arrayOfPoint.length > 2) {
                    localPathPoly = CreatePath(arrayOfPoint, 0, 0);
                    localPathLine = localPathPoly;
                }
                if (arrayOfPoint != null && arrayOfPoint.length > 2 && localPathPoly != null) {
                    if (paramlkDrawType == EDrawType.NONE) {
                        paramCanvas.drawPath(localPathPoly, getPStyle());
                        if (localPathLine != null) {
                            paramCanvas.drawPath(localPathLine, getLStyle());
                        }
                    } else if (paramlkDrawType == EDrawType.EDIT_SEL || paramlkDrawType == EDrawType.NON_EDIT_SEL) {
                        paramCanvas.drawPath(localPathPoly, getPStyle());
                        if (localPathLine != null) {
                            paramCanvas.drawPath(localPathLine, this._SelectedLinePaint);
                        }
                        int tempDPI = paramMap.SetDPI(SeletedLineNodeSize) / 2;
                        Paint tempPaint03 = new Paint();
                        tempPaint03.setColor(SupportMenu.CATEGORY_MASK);
                        for (Point tmpPoint : arrayOfPoint) {
                            paramCanvas.drawRect((float) (tmpPoint.x - tempDPI), (float) (tmpPoint.y - tempDPI), (float) (tmpPoint.x + tempDPI), (float) (tmpPoint.y + tempDPI), tempPaint03);
                        }
                    }
                }
            } else {
                Path localPathPoly2 = new Path();
                Path localPathLine2 = new Path();
                boolean tempNotContainTag = !paramMap.getExtend().Contains(paramGeometry.getEnvelope());
                boolean tmpConsideDrawPtn = false;
                if (paramlkDrawType == EDrawType.EDIT_SEL || paramlkDrawType == EDrawType.NON_EDIT_SEL) {
                    tmpConsideDrawPtn = true;
                }
                List<Point> tmpAllDrawPoints = new ArrayList<>();
                int tempCount00 = paramGeometry.GetNumberOfParts();
                List<Coordinate> tempCoords00 = paramGeometry.GetAllCoordinateList();
                int tempPtnTID = paramGeometry.GetPartIndex().get(1).intValue();
                List<Coordinate> tempCoords01 = localPolygon.GetAllCoordinateList().subList(0, tempPtnTID);
                if (tempNotContainTag) {
                    tempPoints01 = paramMap.ClipPolygon(tempCoords01, offsetX, offsetY);
                } else {
                    tempPoints01 = paramMap.MapPointsToScreePoints(tempCoords01, true, offsetX, offsetY);
                }
                if (tempPoints01 != null && tempPoints01.length > 2) {
                    Path localPathPoly01 = CreatePath(tempPoints01, 0, 0);
                    if (tmpConsideDrawPtn) {
                        tmpAllDrawPoints.addAll(Arrays.asList(tempPoints01));
                    }
                    localPathPoly2.addPath(localPathPoly01);
                    localPathLine2.addPath(localPathPoly01);
                }
                for (int i0 = 1; i0 < tempCount00; i0++) {
                    new ArrayList();
                    if (i0 != tempCount00 - 1) {
                        tempCount02 = paramGeometry.GetPartIndex().get(i0 + 1).intValue();
                    } else {
                        tempCount02 = tempCoords00.size();
                    }
                    if (tempPtnTID < tempCount02) {
                        List<Coordinate> tempCoords02 = paramGeometry.GetAllCoordinateList().subList(tempPtnTID, tempCount02);
                        if (tempNotContainTag) {
                            tempPoints02 = paramMap.ClipPolygon(tempCoords02, offsetX, offsetY);
                        } else {
                            tempPoints02 = paramMap.MapPointsToScreePoints(tempCoords02, true, offsetX, offsetY);
                        }
                        if (tempPoints02 != null && tempPoints02.length > 1) {
                            Path tempPath01 = CreatePath(tempPoints02, 0, 0);
                            localPathPoly2.addPath(tempPath01);
                            localPathLine2.addPath(tempPath01);
                            if (tmpConsideDrawPtn) {
                                tmpAllDrawPoints.addAll(Arrays.asList(tempPoints02));
                            }
                        }
                    }
                    tempPtnTID = tempCount02;
                }
                if (localPathPoly2 == null) {
                    return;
                }
                if (paramlkDrawType == EDrawType.NONE) {
                    Paint tempPaint01 = getPStyle();
                    localPathPoly2.setFillType(Path.FillType.EVEN_ODD);
                    paramCanvas.drawPath(localPathPoly2, tempPaint01);
                    if (localPathLine2 != null) {
                        paramCanvas.drawPath(localPathLine2, getLStyle());
                    }
                } else if (paramlkDrawType == EDrawType.EDIT_SEL || paramlkDrawType == EDrawType.NON_EDIT_SEL) {
                    Paint tempPaint012 = getPStyle();
                    localPathPoly2.setFillType(Path.FillType.EVEN_ODD);
                    paramCanvas.drawPath(localPathPoly2, tempPaint012);
                    if (localPathLine2 != null) {
                        paramCanvas.drawPath(localPathLine2, this._SelectedLinePaint);
                    }
                    if (tmpAllDrawPoints != null && tmpAllDrawPoints.size() > 0) {
                        Paint tempPointPaint = new Paint();
                        tempPointPaint.setColor(SupportMenu.CATEGORY_MASK);
                        int tempDPI2 = paramMap.SetDPI(SeletedLineNodeSize) / 2;
                        for (Point tmpPoint2 : tmpAllDrawPoints) {
                            paramCanvas.drawRect((float) (tmpPoint2.x - tempDPI2), (float) (tmpPoint2.y - tempDPI2), (float) (tmpPoint2.x + tempDPI2), (float) (tmpPoint2.y + tempDPI2), tempPointPaint);
                        }
                    }
                }
            }
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void Draw(Map paramMap, AbstractGeometry paramGeometry) {
        Draw(paramMap, paramMap.getDisplayGraphic(), paramGeometry, 0, 0, EDrawType.NONE);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void DrawLabel(Map paramMap, IRender pRender, Canvas paramCanvas, AbstractGeometry paramGeometry, int offsetX, int offsetY, EGeoDisplayType paramlkSelectionType) {
        try {
            String text = paramGeometry.getLabelContent();
            if (text != null && !text.equals("")) {
                if (PubVar.Map_Text_AutoAdjustPos) {
                    Point[] pfList = paramMap.ClipPolygon(((Polygon) paramGeometry).GetAllCoordinateList(), offsetX, offsetY);
                    if (pfList != null && pfList.length > 0) {
                        Point tmpPtn = CalPolygonCenterPoint(pfList);
                        pRender.getTextSymbol().Draw(paramCanvas, (float) tmpPtn.x, (float) tmpPtn.y, text, ETextPosition.CENTER_ALIGIN, paramlkSelectionType);
                        return;
                    }
                    return;
                }
                Point localPoint = paramMap.MapToScreen(((Polygon) paramGeometry).getInnerPoint());
                pRender.getTextSymbol().Draw(paramCanvas, (float) localPoint.x, (float) localPoint.y, text, ETextPosition.CENTER_ALIGIN, paramlkSelectionType);
            }
        } catch (Exception e) {
        }
    }

    private Point CalPolygonCenterPoint(Point[] list) {
        if (1 == list.length) {
            return list[0];
        }
        if (2 == list.length) {
            return new Point((list[0].x + list[1].x) / 2, (list[0].y + list[1].y) / 2);
        }
        if (list.length < 3) {
            return null;
        }
        Point p0 = list[0];
        double sum_x = 0.0d;
        double sum_y = 0.0d;
        double sum_area = 0.0d;
        int count = list.length - 1;
        for (int i = 1; i < count; i++) {
            Point p1 = list[i];
            Point p2 = list[i + 1];
            double area = Area(p0, p1, p2);
            sum_area += area;
            sum_x += ((double) (p0.x + p1.x + p2.x)) * area;
            sum_y += ((double) (p0.y + p1.y + p2.y)) * area;
        }
        return new Point((int) ((sum_x / sum_area) / 3.0d), (int) ((sum_y / sum_area) / 3.0d));
    }

    private Point CalPolygonCenterPoint(List<Point> list) {
        if (1 == list.size()) {
            return list.get(0);
        }
        if (2 == list.size()) {
            return new Point((list.get(1).x + list.get(0).x) / 2, (list.get(1).y + list.get(0).y) / 2);
        } else if (list.size() < 3) {
            return null;
        } else {
            Point p0 = list.get(0);
            double sum_x = 0.0d;
            double sum_y = 0.0d;
            double sum_area = 0.0d;
            int count = list.size() - 1;
            for (int i = 1; i < count; i++) {
                Point p1 = list.get(i);
                Point p2 = list.get(i + 1);
                double area = Area(p0, p1, p2);
                sum_area += area;
                sum_x += ((double) (p0.x + p1.x + p2.x)) * area;
                sum_y += ((double) (p0.y + p1.y + p2.y)) * area;
            }
            return new Point((int) ((sum_x / sum_area) / 3.0d), (int) ((sum_y / sum_area) / 3.0d));
        }
    }

    public static double Area(Point p0, Point p1, Point p2) {
        return ((double) ((((((p0.x * p1.y) + (p1.x * p2.y)) + (p2.x * p0.y)) - (p1.x * p0.y)) - (p2.x * p1.y)) - (p0.x * p2.y))) / 2.0d;
    }

    private Point CalTriCenterPoint(Point pt1, Point pt2, Point pt3) {
        return new Point((int) (((float) ((pt1.x + pt2.x) + pt3.x)) / 3.0f), (int) (((float) ((pt1.y + pt2.y) + pt3.y)) / 3.0f));
    }

    private double GetTowPointDistance(Point P1, Point P2) {
        return Math.sqrt((double) (((P1.x - P2.x) * (P1.x - P2.x)) + ((P1.y - P2.y) * (P1.y - P2.y))));
    }

    private double GetLength(Point[] pfList) {
        double num = 0.0d;
        for (int i = 0; i < pfList.length - 1; i++) {
            num += GetTowPointDistance(pfList[i], pfList[i + 1]);
        }
        return num;
    }

    private Point GetToStartCoordinate(Point StartPoint, Point EndPoint, double ToStartDistance) {
        double x = (double) StartPoint.x;
        double y = (double) StartPoint.y;
        double num3 = (double) EndPoint.x;
        double num4 = (double) EndPoint.y;
        double num6 = GetTowPointDistance(StartPoint, EndPoint) - ToStartDistance;
        if (ToStartDistance == 0.0d) {
            return StartPoint;
        }
        if (num6 == 0.0d) {
            return EndPoint;
        }
        double num7 = ToStartDistance / num6;
        return new Point((int) (((num7 * num3) + x) / (1.0d + num7)), (int) (((num7 * num4) + y) / (1.0d + num7)));
    }

    public Paint getLStyle() {
        return this._LBrush;
    }

    public Paint getPStyle() {
        return this._PBrush;
    }

    public Paint getClearStyle() {
        return this._ClearBrush;
    }

    public void setLStyle(Paint paramPaint) {
        this._LBrush = paramPaint;
        this._LBrush.setStyle(Paint.Style.STROKE);
        this._LBrush.setAntiAlias(true);
    }

    public void setPStyle(Paint paramPaint) {
        this._PBrush = paramPaint;
        this._PBrush.setStyle(Paint.Style.FILL);
        this._PBrush.setAntiAlias(true);
        UpdateTransparent();
    }

    public void setPSColor(int color) {
        getPStyle().setColor(color);
    }

    public void setLSColor(int color) {
        getLStyle().setColor(color);
    }

    public void setLSWidth(float width) {
        getLStyle().setStrokeWidth(width);
    }
}
