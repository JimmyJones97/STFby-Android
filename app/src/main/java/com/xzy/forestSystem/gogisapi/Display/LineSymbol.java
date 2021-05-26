package  com.xzy.forestSystem.gogisapi.Display;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import androidx.core.internal.view.SupportMenu;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Map;
import  com.xzy.forestSystem.gogisapi.Edit.EDrawType;
import  com.xzy.forestSystem.gogisapi.Edit.EGeometryStatus;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.EGeoDisplayType;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class LineSymbol extends ISymbol {
    static final int DefaultLineColor = -16744448;
    static final int DefaultLineWidth = 5;
    static final int EditingLineColor = -65536;
    static final int EditingLineNodeCircleSize = ((int) (10.0f * PubVar.ScaledDensity));
    static final int EditingLineNodeColor = -256;
    static final int EditingLineNodeColor2 = -65536;
    static final int EditingLineNodeColor3 = -16776961;
    static final int EditingLineNodeSize = ((int) (PubVar.ScaledDensity * 3.0f));
    static final int EditingLineWidth = ((int) (PubVar.ScaledDensity * 3.0f));
    static final int SeletedLineColor = -16711681;
    static final int SeletedLineEndNodeColor = -16711936;
    static final int SeletedLineNodeColor = -349389;
    static final int SeletedLineNodeSize = ((int) (6.0f * PubVar.ScaledDensity));
    static final int SeletedLineStartNodeColor = -65536;
    static final int SeletedLineWidth = ((int) (PubVar.ScaledDensity * 3.0f));
    private Paint _BKLinePaint = null;
    private List<Paint> _PenList = new ArrayList();

    public LineSymbol() {
        setName("NULL");
        Random localRandom = new Random();
        int i = localRandom.nextInt(255);
        int j = localRandom.nextInt(255);
        int k = localRandom.nextInt(255);
        Paint localPaint = new Paint();
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setARGB(0, i, j, k);
        this._PenList.add(localPaint);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void Dispose2() {
        try {
            if (this._SymbolFigure != null && !this._SymbolFigure.isRecycled()) {
                this._SymbolFigure.recycle();
            }
            this._SymbolFigure = null;
            if (this._PenList.size() > 0) {
                this._PenList.clear();
            }
        } catch (Exception e) {
        }
    }

    private Paint getBKLinePaint() {
        if (this._BKLinePaint == null) {
            this._BKLinePaint = new Paint();
            this._BKLinePaint.setStyle(Paint.Style.STROKE);
            this._BKLinePaint.setColor(-1);
        }
        return this._BKLinePaint;
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
        String[] tempStrs02;
        this._SymbolFigure = Bitmap.createBitmap(64, 40, Bitmap.Config.ARGB_8888);
        try {
            Canvas localCanvas2 = new Canvas(this._SymbolFigure);
            if (!this._ConfigParas.equals("")) {
                for (String str5 : this._ConfigParas.split("@")) {
                    String[] tempStrs01 = str5.split(",");
                    if (tempStrs01 != null && tempStrs01.length > 1) {
                        Paint localPaint = new Paint();
                        localPaint.setColor(Color.parseColor(tempStrs01[0]));
                        localPaint.setAntiAlias(true);
                        localPaint.setStyle(Paint.Style.STROKE);
                        localPaint.setStrokeWidth(Float.valueOf(tempStrs01[1]).floatValue());
                        if (tempStrs01.length > 2 && (tempStrs02 = tempStrs01[2].split("\\*")) != null && tempStrs02.length > 1) {
                            localPaint.setPathEffect(new DashPathEffect(new float[]{Float.parseFloat(tempStrs02[0]), Float.parseFloat(tempStrs02[1])}, 1.0f));
                        }
                        localCanvas2.drawLine(0.0f, 20.0f, 64.0f, 20.0f, localPaint);
                    }
                }
            }
        } catch (Exception e) {
        }
        return this._SymbolFigure;
    }

    public static Path CreatePath(Point[] paramArrayOfPoint, int offsetX, int offsetY) {
        Path localPath = new Path();
        int count = paramArrayOfPoint.length;
        localPath.moveTo((float) (paramArrayOfPoint[0].x + offsetX), (float) (paramArrayOfPoint[0].y + offsetY));
        for (int i = 1; i < count; i++) {
            localPath.lineTo((float) (paramArrayOfPoint[i].x + offsetX), (float) (paramArrayOfPoint[i].y + offsetY));
        }
        return localPath;
    }

    public static Path CreatePathByIndex(Point[] paramArrayOfPoint, int fromIndex, int toIndex) {
        if (toIndex <= fromIndex) {
            return null;
        }
        Path localPath = new Path();
        localPath.moveTo((float) paramArrayOfPoint[fromIndex].x, (float) paramArrayOfPoint[fromIndex].y);
        for (int i = fromIndex + 1; i < toIndex; i++) {
            localPath.lineTo((float) paramArrayOfPoint[i].x, (float) paramArrayOfPoint[i].y);
        }
        return localPath;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void Draw(Map paramMap, Canvas paramCanvas, AbstractGeometry paramGeometry, int offsetX, int offsetY, EDrawType paramlkDrawType) {
        int tempCount02;
        Point[] arrayOfPoint;
        Point[] arrayOfPoint2;
        if (paramGeometry.getStatus() != EGeometryStatus.DELETE && paramMap.getExtend().Intersect(paramGeometry.getEnvelope())) {
            Polyline pPolyline = (Polyline) paramGeometry;
            boolean tmpConsideDrawPtn = false;
            if (paramlkDrawType == EDrawType.EDIT_SEL || paramlkDrawType == EDrawType.NON_EDIT_SEL) {
                tmpConsideDrawPtn = true;
            }
            Path localPath = new Path();
            List<Point> tmpAllPoints = new ArrayList<>();
            if (paramGeometry.IsSimple()) {
                if (paramMap.getExtend().Contains(paramGeometry.getEnvelope())) {
                    arrayOfPoint2 = paramMap.MapPointsToScreePoints(pPolyline.GetAllCoordinateList(), true, offsetX, offsetY);
                    localPath = CreatePath(arrayOfPoint2, 0, 0);
                } else {
                    List<Integer> tmpPtnIndexList = new ArrayList<>();
                    arrayOfPoint2 = paramMap.ClipPolyline(pPolyline.GetAllCoordinateList(), tmpPtnIndexList, offsetX, offsetY);
                    if (arrayOfPoint2 != null && arrayOfPoint2.length > 1) {
                        localPath = new Path();
                        int tmpStart = 0;
                        int tmpCount = tmpPtnIndexList.size();
                        for (int i = 0; i < tmpCount; i++) {
                            int tmpEnd = tmpPtnIndexList.get(i).intValue();
                            Path tmpPath = CreatePathByIndex(arrayOfPoint2, tmpStart, tmpEnd);
                            if (tmpPath != null) {
                                localPath.addPath(tmpPath);
                            }
                            tmpStart = tmpEnd;
                        }
                    }
                }
                if (arrayOfPoint2 != null && arrayOfPoint2.length > 1 && tmpConsideDrawPtn) {
                    tmpAllPoints.addAll(Arrays.asList(arrayOfPoint2));
                }
            } else {
                boolean tempNotContainTag = !paramMap.getExtend().Contains(paramGeometry.getEnvelope());
                int tempPtnTID = 0;
                int tempCount00 = paramGeometry.GetNumberOfParts();
                List<Coordinate> tempCoords00 = paramGeometry.GetAllCoordinateList();
                localPath = new Path();
                for (int i0 = 0; i0 < tempCount00; i0++) {
                    List<Coordinate> tempCoords01 = new ArrayList<>();
                    if (i0 != tempCount00 - 1) {
                        tempCount02 = paramGeometry.GetPartIndex().get(i0 + 1).intValue();
                    } else {
                        tempCount02 = tempCoords00.size();
                    }
                    if (tempPtnTID < tempCount02) {
                        tempCoords01 = tempCoords00.subList(tempPtnTID, tempCount02);
                    }
                    if (tempCoords01.size() > 0) {
                        tempPtnTID += tempCoords01.size();
                        if (tempNotContainTag) {
                            List<Integer> tmpPtnIndexList2 = new ArrayList<>();
                            arrayOfPoint = paramMap.ClipPolyline(tempCoords01, tmpPtnIndexList2, offsetX, offsetY);
                            if (arrayOfPoint != null && arrayOfPoint.length > 1) {
                                int tmpStart2 = 0;
                                int tmpCount2 = tmpPtnIndexList2.size();
                                for (int i2 = 0; i2 < tmpCount2; i2++) {
                                    int tmpEnd2 = tmpPtnIndexList2.get(i2).intValue();
                                    Path tmpPath2 = CreatePathByIndex(arrayOfPoint, tmpStart2, tmpEnd2);
                                    if (tmpPath2 != null) {
                                        localPath.addPath(tmpPath2);
                                    }
                                    tmpStart2 = tmpEnd2;
                                }
                            }
                        } else {
                            arrayOfPoint = paramMap.MapPointsToScreePoints(tempCoords01, true, offsetX, offsetY);
                            localPath.addPath(CreatePath(arrayOfPoint, 0, 0));
                        }
                        if (arrayOfPoint != null && arrayOfPoint.length > 1 && tmpConsideDrawPtn) {
                            tmpAllPoints.addAll(Arrays.asList(arrayOfPoint));
                        }
                    }
                }
            }
            if (localPath == null) {
                return;
            }
            if (paramlkDrawType == EDrawType.NON_EDIT_SEL) {
                int ptnLen = tmpAllPoints.size();
                if (ptnLen >= 2) {
                    Paint tempLintPaint = new Paint();
                    tempLintPaint.setStyle(Paint.Style.STROKE);
                    tempLintPaint.setColor(SeletedLineColor);
                    tempLintPaint.setStrokeWidth((float) SeletedLineWidth);
                    tempLintPaint.setAntiAlias(true);
                    paramCanvas.drawPath(localPath, tempLintPaint);
                    int tempDpi = paramMap.SetDPI(SeletedLineNodeSize) / 2;
                    tempLintPaint.setStyle(Paint.Style.FILL);
                    tempLintPaint.setColor(SupportMenu.CATEGORY_MASK);
                    Point tmpPoint01 = tmpAllPoints.get(0);
                    paramCanvas.drawRect((float) (tmpPoint01.x - tempDpi), (float) (tmpPoint01.y - tempDpi), (float) (tmpPoint01.x + tempDpi), (float) (tmpPoint01.y + tempDpi), tempLintPaint);
                    Point tmpPoint012 = tmpAllPoints.get(tmpAllPoints.size() - 1);
                    tempLintPaint.setColor(SeletedLineEndNodeColor);
                    paramCanvas.drawRect((float) (tmpPoint012.x - tempDpi), (float) (tmpPoint012.y - tempDpi), (float) (tmpPoint012.x + tempDpi), (float) (tmpPoint012.y + tempDpi), tempLintPaint);
                    tempLintPaint.setColor(SeletedLineNodeColor);
                    int ptnLen2 = ptnLen - 1;
                    for (int i3 = 1; i3 < ptnLen2; i3++) {
                        Point tmpPoint013 = tmpAllPoints.get(i3);
                        paramCanvas.drawRect((float) (tmpPoint013.x - tempDpi), (float) (tmpPoint013.y - tempDpi), (float) (tmpPoint013.x + tempDpi), (float) (tmpPoint013.y + tempDpi), tempLintPaint);
                    }
                }
            } else if (paramlkDrawType == EDrawType.EDIT_SEL) {
                paramMap.SetDPI(EditingLineNodeSize);
                Paint tempLintPaint2 = new Paint();
                tempLintPaint2.setStyle(Paint.Style.STROKE);
                tempLintPaint2.setColor(SupportMenu.CATEGORY_MASK);
                tempLintPaint2.setStrokeWidth((float) EditingLineWidth);
                tempLintPaint2.setAntiAlias(true);
                paramCanvas.drawPath(localPath, tempLintPaint2);
                Paint tempLintPaint01 = new Paint();
                tempLintPaint01.setColor(EditingLineNodeColor);
                tempLintPaint01.setAntiAlias(true);
                Paint tempLintPaint02 = new Paint();
                tempLintPaint02.setAntiAlias(true);
                tempLintPaint02.setColor(SupportMenu.CATEGORY_MASK);
                for (Point tmpPoint014 : tmpAllPoints) {
                    paramCanvas.drawCircle((float) tmpPoint014.x, (float) tmpPoint014.y, (float) EditingLineNodeSize, tempLintPaint02);
                    paramCanvas.drawCircle((float) tmpPoint014.x, (float) tmpPoint014.y, (float) (EditingLineNodeSize + 5), tempLintPaint01);
                }
                tempLintPaint01.setStyle(Paint.Style.STROKE);
                tempLintPaint01.setColor(EditingLineNodeColor3);
                tempLintPaint01.setStrokeWidth((float) EditingLineWidth);
                Point tmpPoint02 = tmpAllPoints.get(tmpAllPoints.size() - 1);
                paramCanvas.drawCircle((float) tmpPoint02.x, (float) tmpPoint02.y, (float) EditingLineNodeCircleSize, tempLintPaint01);
            } else {
                Iterator tempIterator = getStyle().iterator();
                if (tempIterator.hasNext()) {
                    while (tempIterator.hasNext()) {
                        paramCanvas.drawPath(localPath, (Paint) tempIterator.next());
                    }
                    return;
                }
                Paint tempLintPaint3 = new Paint();
                tempLintPaint3.setStyle(Paint.Style.STROKE);
                tempLintPaint3.setColor(DefaultLineColor);
                tempLintPaint3.setStrokeWidth(-1.6744448E7f);
                tempLintPaint3.setAntiAlias(true);
                paramCanvas.drawPath(localPath, tempLintPaint3);
            }
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void Draw(Map paramMap, AbstractGeometry paramGeometry) {
        Draw(paramMap, paramMap.getDisplayGraphic(), paramGeometry, 0, 0, EDrawType.NONE);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void DrawLabel(Map paramMap, IRender pRender, Canvas paramCanvas, AbstractGeometry pGeometry, int OffsetX, int OffsetY, EGeoDisplayType paramlkSelectionType) {
        String text = pGeometry.getLabelContent();
        if (!(text == null || text.equals(""))) {
            if (PubVar.Map_Text_AutoAdjustPos) {
                Point[] pfList = paramMap.ClipPolyline(((Polyline) pGeometry).getVertexList(), OffsetX, OffsetY);
                if (pfList.length > 1) {
                    double num2 = (GetLength(pfList) / 2.0d) - ((double) (pRender.getTextSymbol().getTextFont().measureText(text) / 2.0f));
                    int num3 = 0;
                    int num4 = 0;
                    while (true) {
                        if (num4 >= pfList.length - 1) {
                            break;
                        }
                        Point point = pfList[num4];
                        Point point2 = pfList[num4 + 1];
                        double towPointDistance = GetTowPointDistance(point, point2);
                        if (num2 - towPointDistance < 0.0d) {
                            pfList[num4] = GetToStartCoordinate(point, point2, Math.abs(num2));
                            num3 = num4;
                            break;
                        }
                        num2 -= towPointDistance;
                        num4++;
                    }
                    double num6 = (double) pRender.getTextSymbol().getTextFont().getTextSize();
                    double num7 = 0.0d;
                    int num8 = 0;
                    List<Point> list = new ArrayList<>();
                    int num42 = num3;
                    while (num42 < pfList.length - 1) {
                        Point point3 = pfList[num42];
                        Point point22 = pfList[num42 + 1];
                        double towPointDistance2 = GetTowPointDistance(point3, point22);
                        if (num7 + towPointDistance2 > num6) {
                            Point point32 = GetToStartCoordinate(point3, point22, num6 - num7);
                            list.add(point32);
                            num8++;
                            if (num8 >= text.length()) {
                                break;
                            }
                            num7 = 0.0d;
                            pfList[num42] = point32;
                            num42--;
                        } else {
                            num7 += towPointDistance2;
                        }
                        num42++;
                    }
                    boolean flag = false;
                    if (list.size() > 1 && list.get(0).x >= list.get(list.size() - 1).x) {
                        flag = true;
                    }
                    int i = 0;
                    for (Point tmpPoint01 : list) {
                        int startIndex = i;
                        if (flag) {
                            startIndex = (list.size() - 1) - i;
                        }
                        pRender.getTextSymbol().Draw(paramCanvas, (float) tmpPoint01.x, (float) tmpPoint01.y, text.substring(startIndex, startIndex + 1), ETextPosition.CENTER_ALIGIN, paramlkSelectionType);
                        i++;
                    }
                    return;
                }
                return;
            }
            int i2 = pGeometry.GetAllCoordinateList().size();
            Coordinate localCoordinate1 = pGeometry.GetAllCoordinateList().get(i2 / 2);
            if (i2 % 2 == 0) {
                Coordinate localCoordinate2 = pGeometry.GetAllCoordinateList().get((i2 / 2) - 1);
                localCoordinate1 = new Coordinate((localCoordinate1.getX() + localCoordinate2.getX()) / 2.0d, (localCoordinate1.getY() + localCoordinate2.getY()) / 2.0d);
            }
            Point localPoint = paramMap.MapToScreen(localCoordinate1);
            pRender.getTextSymbol().Draw(paramCanvas, (float) localPoint.x, (float) localPoint.y, text, ETextPosition.CENTER_ALIGIN, paramlkSelectionType);
        }
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

    private double GetTowPointDistance(Point P1, Point P2) {
        return Math.sqrt((double) (((P1.x - P2.x) * (P1.x - P2.x)) + ((P1.y - P2.y) * (P1.y - P2.y))));
    }

    public List<Paint> getStyle() {
        return this._PenList;
    }

    public void setStyle(List<Paint> paramList) {
        this._PenList = paramList;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void setConfigParas(String configParas) {
        String[] tempStrs02;
        super.setConfigParas(configParas);
        if (!(this._ConfigParas == null || this._ConfigParas.equals(""))) {
            String[] tempConfigParas = this._ConfigParas.split("@");
            this._PenList = new ArrayList();
            for (String paras : tempConfigParas) {
                String[] tempStrs = paras.split(",");
                if (tempStrs != null && tempStrs.length > 1) {
                    int tempColor = Color.parseColor(tempStrs[0].toString());
                    float tempSize = Float.parseFloat(tempStrs[1]);
                    Paint tmpPaint = new Paint();
                    tmpPaint.setStyle(Paint.Style.STROKE);
                    tmpPaint.setColor(tempColor);
                    tmpPaint.setStrokeWidth(tempSize);
                    tmpPaint.setSubpixelText(true);
                    tmpPaint.setAntiAlias(true);
                    if (tempStrs.length > 2 && (tempStrs02 = tempStrs[2].split("\\*")) != null && tempStrs02.length > 1) {
                        tmpPaint.setPathEffect(new DashPathEffect(new float[]{Float.parseFloat(tempStrs02[0]), Float.parseFloat(tempStrs02[1])}, 1.0f));
                    }
                    this._PenList.add(tmpPaint);
                }
            }
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Display.ISymbol
    public void UpdateTransparent() {
        super.UpdateTransparent();
        if (this._PenList.size() > 0) {
            for (Paint tmpPaint : this._PenList) {
                if (tmpPaint != null) {
                    tmpPaint.setAlpha(255 - this._Transparent);
                }
            }
        }
    }
}
