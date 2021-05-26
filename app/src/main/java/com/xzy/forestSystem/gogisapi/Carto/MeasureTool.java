package  com.xzy.forestSystem.gogisapi.Carto;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Typeface;
import androidx.core.internal.view.SupportMenu;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Display.LineSymbol;
import  com.xzy.forestSystem.gogisapi.Display.PolygonSymbol;
import  com.xzy.forestSystem.gogisapi.Display.SymbolManage;
import  com.xzy.forestSystem.gogisapi.Edit.EDrawType;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.MyControls.TipPanel;
import java.util.ArrayList;
import java.util.List;

public class MeasureTool implements ICommand, IOnPaint {
    private static final float CricleRadius = (5.0f * PubVar.ScaledDensity);
    private static final float TextSize = 16.0f;
    private static final float lineWidth = (3.0f * PubVar.ScaledDensity);
    private double _Area;
    private TextPaint _BKFont;
    private int _ClickCount;
    private long _FirstClickTime;
    private TextPaint _Font;
    private List<Double> _LengthList;
    private Paint _LinePaint;
    private int _Mode;
    private Polygon _Polygon;
    private long _SecondClickTime;
    private TipPanel _TipPanel;
    private List<Coordinate> m_MeasurePointList;
    private int m_MeasurePtnCount;
    private PolygonSymbol m_PolygonSymbol;
    private List<Coordinate> m_ResMeasurePointList;

    public MeasureTool() {
        this._Area = 0.0d;
        this._BKFont = null;
        this._ClickCount = 0;
        this._FirstClickTime = 0;
        this._Font = null;
        this._LengthList = new ArrayList();
        this._Mode = 1;
        this._Polygon = null;
        this._SecondClickTime = 0;
        this.m_MeasurePointList = null;
        this.m_ResMeasurePointList = null;
        this._LinePaint = null;
        this._TipPanel = null;
        this.m_PolygonSymbol = null;
        this.m_MeasurePtnCount = 0;
        this._Polygon = new Polygon();
        this.m_MeasurePointList = new ArrayList();
        this.m_ResMeasurePointList = new ArrayList();
        this._Polygon.SetAllCoordinateList(this.m_MeasurePointList);
        this._LinePaint = new Paint();
        this._LinePaint.setStrokeWidth(lineWidth);
        this._LinePaint.setColor(-5435154);
        this._LinePaint.setStyle(Paint.Style.STROKE);
        this.m_PolygonSymbol = SymbolManage.GetPolySymbol("#dbe4f5,#FF0000,3");
        this.m_PolygonSymbol.getPStyle().setAlpha(100);
    }

    public void SetBindTipPanel(TipPanel tipPanel) {
        this._TipPanel = tipPanel;
        if (this._TipPanel != null) {
            this._TipPanel.setClickHide();
        }
    }

    private void AddPoint(MotionEvent paramMotionEvent) {
        Coordinate localCoordinate1 = PubVar._Map.ScreenToMap(new PointF(paramMotionEvent.getX(), paramMotionEvent.getY()));
        Coordinate localCoordinate2 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(localCoordinate1, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
        localCoordinate1.setGeoX(localCoordinate2.getX());
        localCoordinate1.setGeoY(localCoordinate2.getY());
        AddPoint(localCoordinate1);
    }

    public void AddPoint(Coordinate coordinate) {
        if (this._Mode == 0) {
            Clear();
        }
        if (this.m_MeasurePointList.size() <= 0 || !this.m_MeasurePointList.get(this.m_MeasurePointList.size() - 1).Equal(coordinate)) {
            this.m_MeasurePointList.add(coordinate);
            while (this.m_ResMeasurePointList.size() > this.m_MeasurePtnCount) {
                this.m_ResMeasurePointList.remove(this.m_ResMeasurePointList.size() - 1);
            }
            this.m_ResMeasurePointList.add(coordinate);
            this.m_MeasurePtnCount++;
            if (this.m_MeasurePointList.size() == 1) {
                this._LengthList.add(Double.valueOf(0.0d));
                return;
            }
            this._LengthList.add(Double.valueOf(this._Polygon.getLength(true)));
            if (this.m_MeasurePointList.size() >= 3) {
                this._Area = this._Polygon.getArea(true);
            }
            PubVar._MapView.invalidate();
        }
    }

    public boolean RedoLastPoint(String[] outMsg) {
        outMsg[0] = "";
        if (this.m_MeasurePointList.size() < this.m_ResMeasurePointList.size()) {
            this.m_MeasurePointList.add(this.m_ResMeasurePointList.get(this.m_MeasurePointList.size()));
            this._LengthList.add(Double.valueOf(this._Polygon.getLength(true)));
            if (this.m_MeasurePointList.size() >= 3) {
                this._Area = this._Polygon.getArea(true);
            }
            this.m_MeasurePtnCount++;
            return true;
        }
        outMsg[0] = "已经是最后一个点.";
        return false;
    }

    public boolean UndoLastPoint(String[] outMsg) {
        outMsg[0] = "";
        if (this.m_MeasurePointList.size() > 1) {
            this.m_MeasurePointList.remove(this.m_MeasurePointList.size() - 1);
            this._LengthList.remove(this._LengthList.size() - 1);
            if (this.m_MeasurePointList.size() >= 3) {
                this._Area = this._Polygon.getArea(true);
            }
            this.m_MeasurePtnCount--;
            return true;
        } else if (this.m_MeasurePointList.size() == 1) {
            outMsg[0] = "最后一个点不能取消.";
            return false;
        } else {
            outMsg[0] = "没有可以取消的点.";
            return false;
        }
    }

    private void DrawText(Canvas paramCanvas, String paramString, float x, float y) {
        if (this._TipPanel != null) {
            this._TipPanel.showTip(PubVar._PubCommand.m_MainLayout, (int) x, (int) y, paramString);
            return;
        }
        StaticLayout layout = new StaticLayout(paramString, getTextFont(), 800, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        paramCanvas.save();
        paramCanvas.translate(x, y);
        StaticLayout layout2 = new StaticLayout(paramString, getBKFont(), 800, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        paramCanvas.translate(-2.0f, -2.0f);
        layout2.draw(paramCanvas);
        paramCanvas.translate(2.0f, 0.0f);
        layout2.draw(paramCanvas);
        paramCanvas.translate(2.0f, 0.0f);
        layout2.draw(paramCanvas);
        paramCanvas.translate(0.0f, 2.0f);
        layout2.draw(paramCanvas);
        paramCanvas.translate(0.0f, 2.0f);
        layout2.draw(paramCanvas);
        paramCanvas.translate(-2.0f, 0.0f);
        layout2.draw(paramCanvas);
        paramCanvas.translate(-2.0f, 0.0f);
        layout2.draw(paramCanvas);
        paramCanvas.translate(0.0f, -2.0f);
        layout2.draw(paramCanvas);
        paramCanvas.translate(2.0f, 0.0f);
        layout.draw(paramCanvas);
        paramCanvas.restore();
    }

    private TextPaint getBKFont() {
        if (this._BKFont == null) {
            this._BKFont = new TextPaint();
            this._BKFont.setColor(-1);
            this._BKFont.setAntiAlias(true);
            this._BKFont.setTypeface(Typeface.create("宋体", 0));
            this._BKFont.setAntiAlias(true);
            this._BKFont.setTextSize(getTextFont().getTextSize());
        }
        return this._BKFont;
    }

    private TextPaint getTextFont() {
        if (this._Font == null) {
            this._Font = new TextPaint();
            this._Font.setAntiAlias(true);
            this._Font.setTextSize(TextSize * PubVar.ScaledDensity);
            this._Font.setTypeface(Typeface.create("宋体", 0));
        }
        this._Font.setAntiAlias(true);
        return this._Font;
    }

    public void Clear() {
        this.m_MeasurePtnCount = 0;
        this.m_MeasurePointList.clear();
        this.m_ResMeasurePointList.clear();
        this._LengthList.clear();
        PubVar._MapView.invalidate();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseDown(MotionEvent paramMotionEvent) {
        if (this._TipPanel != null) {
            this._TipPanel.AllowShow = true;
        }
        if (this._Mode == 0) {
            this._ClickCount = 1;
            this._FirstClickTime = System.currentTimeMillis();
            this.m_MeasurePointList.clear();
            this._LengthList.clear();
            AddPoint(paramMotionEvent);
            PubVar._MapView.invalidate();
            return;
        }
        this._ClickCount++;
        if (this._ClickCount == 1) {
            this._FirstClickTime = System.currentTimeMillis();
            AddPoint(paramMotionEvent);
            PubVar._MapView.invalidate();
            return;
        }
        this._SecondClickTime = System.currentTimeMillis();
        if (this._SecondClickTime - this._FirstClickTime < 200) {
            this._ClickCount = 0;
            this.m_MeasurePointList.clear();
            this._LengthList.clear();
        } else {
            AddPoint(paramMotionEvent);
            this._FirstClickTime = this._SecondClickTime;
        }
        PubVar._MapView.invalidate();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseMove(MotionEvent paramMotionEvent) {
        if (this._Mode == 2) {
            this._ClickCount++;
            if (this._ClickCount == 1) {
                this._FirstClickTime = System.currentTimeMillis();
                AddPoint(paramMotionEvent);
                return;
            }
            PubVar._MapView.invalidate();
            AddPoint(paramMotionEvent);
            this._FirstClickTime = this._SecondClickTime;
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseUp(MotionEvent paramMotionEvent) {
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas paramCanvas) {
        String tempShowTxt;
        try {
            if (this.m_MeasurePointList.size() != 0) {
                if (this._Mode == 3 && this.m_MeasurePointList.size() > 2) {
                    this.m_PolygonSymbol.Draw(PubVar._MapView.getMap(), paramCanvas, this._Polygon, 0, 0, EDrawType.NONE);
                }
                Point[] arrayOfPoint = PubVar._MapView.getMap().MapPointsToScreePoints(this.m_MeasurePointList);
                if (arrayOfPoint == null) {
                    return;
                }
                if (arrayOfPoint.length > 1) {
                    List<Integer> tmpPtnIndexList = new ArrayList<>();
                    Point[] tmpLinePtns = PubVar._MapView.getMap().ClipPolyline(this.m_MeasurePointList, tmpPtnIndexList, 0, 0);
                    if (tmpLinePtns != null && tmpLinePtns.length > 1) {
                        Path localPath = new Path();
                        int tmpStart = 0;
                        int tmpCount = tmpPtnIndexList.size();
                        for (int i = 0; i < tmpCount; i++) {
                            int tmpEnd = tmpPtnIndexList.get(i).intValue();
                            Path tmpPath = LineSymbol.CreatePathByIndex(tmpLinePtns, tmpStart, tmpEnd);
                            if (tmpPath != null) {
                                localPath.addPath(tmpPath);
                            }
                            tmpStart = tmpEnd;
                        }
                        paramCanvas.drawPath(localPath, this._LinePaint);
                    }
                    int count = arrayOfPoint.length;
                    Point localPoint = arrayOfPoint[0];
                    Paint tempPointPat = new Paint();
                    Paint tempPointPatCricle = new Paint();
                    tempPointPat.setColor(-16711936);
                    paramCanvas.drawCircle((float) localPoint.x, (float) localPoint.y, CricleRadius, tempPointPat);
                    tempPointPatCricle.setStyle(Paint.Style.STROKE);
                    tempPointPatCricle.setStrokeWidth(lineWidth);
                    tempPointPatCricle.setColor(-16776961);
                    paramCanvas.drawCircle((float) localPoint.x, (float) localPoint.y, CricleRadius, tempPointPatCricle);
                    for (int i2 = 1; i2 < count - 1; i2++) {
                        Point localPoint2 = arrayOfPoint[i2];
                        tempPointPat.setColor(-256);
                        paramCanvas.drawCircle((float) localPoint2.x, (float) localPoint2.y, CricleRadius, tempPointPat);
                        paramCanvas.drawCircle((float) localPoint2.x, (float) localPoint2.y, CricleRadius, tempPointPatCricle);
                    }
                    Point localPoint3 = arrayOfPoint[count - 1];
                    tempPointPat.setColor(SupportMenu.CATEGORY_MASK);
                    paramCanvas.drawCircle((float) localPoint3.x, (float) localPoint3.y, CricleRadius, tempPointPat);
                    paramCanvas.drawCircle((float) localPoint3.x, (float) localPoint3.y, CricleRadius, tempPointPatCricle);
                    String tempShowTxt2 = "长度:" + Common.SimplifyLength(this._LengthList.get(this._LengthList.size() - 1).doubleValue());
                    if (count > 2) {
                        tempShowTxt2 = String.valueOf(tempShowTxt2) + "\r\n面积:" + Common.SimplifyArea(this._Area, true);
                    }
                    double tmpDelteX = this.m_MeasurePointList.get(count - 1).getX() - this.m_MeasurePointList.get(count - 2).getX();
                    double tmpDelteY = this.m_MeasurePointList.get(count - 1).getY() - this.m_MeasurePointList.get(count - 2).getY();
                    String tempShowTxt3 = String.valueOf(String.valueOf(String.valueOf(String.valueOf(tempShowTxt2) + "\r\nΔX:" + Common.SimplifyLength(tmpDelteX)) + "\r\nΔY:" + Common.SimplifyLength(tmpDelteY)) + "\r\n本段长度:" + Common.SimplifyLength(Math.sqrt((tmpDelteX * tmpDelteX) + (tmpDelteY * tmpDelteY)))) + "\r\n方位角:" + Common.doubleFormat1.format(Common.CalculateAngle(tmpDelteX, tmpDelteY)) + "°";
                    if (this._TipPanel == null || this._TipPanel.AllowShow) {
                        DrawText(paramCanvas, tempShowTxt3, CricleRadius + CricleRadius + ((float) localPoint3.x), (float) localPoint3.y);
                        return;
                    }
                    return;
                }
                Coordinate tempCoord = this.m_MeasurePointList.get(0);
                if (this._Mode == 0) {
                    if (PubVar.m_Workspace.GetCoorSystem().getIsProjectionCoord()) {
                        Coordinate tempCoord2 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tempCoord, PubVar.m_Workspace.GetCoorSystem());
                        tempShowTxt = "经度:" + Common.ConvertDegree(tempCoord2.getGeoX()) + "\r\n纬度:" + Common.ConvertDegree(tempCoord2.getGeoY()) + "\r\nX:" + String.format("%.3f", Double.valueOf(tempCoord.getX())) + "\r\nY:" + String.format("%.3f", Double.valueOf(tempCoord.getY()));
                    } else {
                        Coordinate tempCoord22 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tempCoord, PubVar.m_Workspace.GetCoorSystem());
                        tempShowTxt = "经度:" + Common.ConvertDegree(tempCoord22.getGeoX()) + "\r\n纬度:" + Common.ConvertDegree(tempCoord22.getGeoY()) + "\r\nX:" + String.format("%.3f", Double.valueOf(tempCoord.getX())) + "\r\nY:" + String.format("%.3f", Double.valueOf(tempCoord.getY()));
                    }
                } else if (PubVar.m_Workspace.GetCoorSystem().getIsProjectionCoord()) {
                    tempShowTxt = "X:" + String.format("%.3f", Double.valueOf(tempCoord.getX())) + "\r\nY:" + String.format("%.3f", Double.valueOf(tempCoord.getY()));
                } else {
                    Coordinate tempCoord23 = PubVar.m_Workspace.GetCoorSystem().ConvertXYToBL(tempCoord);
                    tempShowTxt = "经度:" + Common.ConvertDegree(tempCoord23.getGeoX()) + "\r\n纬度:" + Common.ConvertDegree(tempCoord23.getGeoY());
                }
                Point localPoint4 = arrayOfPoint[0];
                Paint localPaint1 = new Paint();
                localPaint1.setColor(SupportMenu.CATEGORY_MASK);
                paramCanvas.drawCircle((float) localPoint4.x, (float) localPoint4.y, CricleRadius, localPaint1);
                localPaint1.setStyle(Paint.Style.STROKE);
                localPaint1.setStrokeWidth(lineWidth);
                localPaint1.setColor(-16776961);
                paramCanvas.drawCircle((float) localPoint4.x, (float) localPoint4.y, CricleRadius, localPaint1);
                if (this._TipPanel == null || this._TipPanel.AllowShow) {
                    DrawText(paramCanvas, tempShowTxt, CricleRadius + CricleRadius + ((float) localPoint4.x), (float) localPoint4.y);
                }
            } else if (this._TipPanel != null) {
                this._TipPanel.hideTip();
            }
        } catch (Exception e) {
        }
    }

    public void SetMode(int paramInt) {
        this._Mode = paramInt;
    }

    public Coordinate GetLastCoordinate() {
        if (this.m_MeasurePointList.size() > 0) {
            return this.m_MeasurePointList.get(this.m_MeasurePointList.size() - 1);
        }
        return null;
    }

    public List<Coordinate> GetLastCoordinates(int count) {
        if (count <= -1) {
            return this.m_MeasurePointList;
        }
        List<Coordinate> result = new ArrayList<>();
        int tempSize = this.m_MeasurePointList.size();
        if (count > tempSize) {
            return result;
        }
        int tempSize2 = tempSize - 1;
        for (int i = 0; i < count; i++) {
            result.add(this.m_MeasurePointList.get(tempSize2 - i));
        }
        return result;
    }

    public List<Coordinate> getMeasurePointList() {
        return this.m_MeasurePointList;
    }
}
