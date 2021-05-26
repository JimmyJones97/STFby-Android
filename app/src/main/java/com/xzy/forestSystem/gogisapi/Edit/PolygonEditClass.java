package  com.xzy.forestSystem.gogisapi.Edit;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import android.view.MotionEvent;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.MapView;
import  com.xzy.forestSystem.gogisapi.Carto.ZoomInOutPan;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Display.LineSymbol;
import  com.xzy.forestSystem.gogisapi.Display.PolygonSymbol;
import  com.xzy.forestSystem.gogisapi.Display.SymbolManage;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PolygonEditClass extends ZoomInOutPan {
    static final int EditingLineColor = -65536;
    static final int EditingLineNodeCircleSize = ((int) (10.0f * PubVar.ScaledDensity));
    static final int EditingLineNodeColor = -256;
    static final int EditingLineNodeColor2 = -65536;
    static final int EditingLineNodeColor3 = -16776961;
    static final int EditingLineNodeSize = ((int) (3.0f * PubVar.ScaledDensity));
    static final int EditingLineWidth = ((int) (2.0f * PubVar.ScaledDensity));
    static final float TipTextSize = 14.0f;
    public boolean IsDrawInMapCenter;
    private boolean _AllowSnap;
    private String _CurrentDrawType;
    private boolean _IsEnabelDraw;
    private boolean _IsHandFreeMode;
    private Paint _LineEndVPaint;
    private Paint _LineVertexPaint01;
    private Paint _LineVertexPaint02;
    private Polygon _Polygon;
    private double _SnapDistance;
    private Paint _TextBgPaint;
    private Paint _TextPaint;
    private Paint _linePaint;
    private List<Coordinate> m_MeasurePointList;
    private int m_MeasurePtnCount;
    private PolygonSymbol m_PolygonSymbol;
    private List<Coordinate> m_ResMeasurePointList;

    public PolygonEditClass(MapView mapView) {
        super(mapView);
        this._Polygon = null;
        this.m_MeasurePointList = null;
        this.m_ResMeasurePointList = null;
        this._IsEnabelDraw = false;
        this._linePaint = null;
        this._LineVertexPaint01 = null;
        this._LineVertexPaint02 = null;
        this._LineEndVPaint = null;
        this._IsHandFreeMode = false;
        this._TextPaint = null;
        this._TextBgPaint = null;
        this._CurrentDrawType = "手绘";
        this._AllowSnap = true;
        this._SnapDistance = 20.0d * ((double) PubVar.ScaledDensity);
        this.m_PolygonSymbol = null;
        this.IsDrawInMapCenter = false;
        this.m_MeasurePtnCount = 0;
        this.m_MeasurePointList = new ArrayList();
        this.m_ResMeasurePointList = new ArrayList();
        this._Polygon = new Polygon();
        this._Polygon.SetAllCoordinateList(this.m_MeasurePointList);
        this._linePaint = new Paint();
        this._linePaint.setStyle(Paint.Style.STROKE);
        this._linePaint.setColor(SupportMenu.CATEGORY_MASK);
        this._linePaint.setStrokeWidth((float) PolylineEditClass.EditingLineWidth);
        this._linePaint.setAntiAlias(true);
        this._LineVertexPaint01 = new Paint();
        this._LineVertexPaint01.setColor(EditingLineNodeColor);
        this._LineVertexPaint01.setAntiAlias(true);
        this._LineVertexPaint02 = new Paint();
        this._LineVertexPaint02.setAntiAlias(true);
        this._LineVertexPaint02.setColor(SupportMenu.CATEGORY_MASK);
        this._LineEndVPaint = new Paint();
        this._LineEndVPaint.setAntiAlias(true);
        this._LineEndVPaint.setStyle(Paint.Style.STROKE);
        this._LineEndVPaint.setColor(EditingLineNodeColor3);
        this._LineEndVPaint.setStrokeWidth((float) PolylineEditClass.EditingLineWidth);
        this._TextPaint = new Paint();
        this._TextPaint.setAntiAlias(true);
        this._TextPaint.setTextSize(PubVar.ScaledDensity * TipTextSize);
        this._TextPaint.setColor(-1);
        Typeface localTypeface = Typeface.create("宋体", 0);
        this._TextPaint.setTypeface(localTypeface);
        this._TextPaint.setTextAlign(Paint.Align.CENTER);
        this._TextBgPaint = new Paint();
        this._TextBgPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this._TextBgPaint.setAlpha(100);
        this._TextBgPaint.setAntiAlias(true);
        this._TextBgPaint.setTypeface(localTypeface);
        this._TextBgPaint.setAntiAlias(true);
        this._TextBgPaint.setTextSize(PubVar.ScaledDensity * TipTextSize);
        this._TextBgPaint.setTextAlign(Paint.Align.CENTER);
        RefreshSnap();
        this.m_PolygonSymbol = SymbolManage.GetPolySymbol("#dbe4f5,#FF0000,3");
        this.m_PolygonSymbol.getPStyle().setAlpha(100);
    }

    public void RefreshSnap() {
        this._AllowSnap = PubVar.AllowEditSnap;
        this._SnapDistance = PubVar.SnapDistance * ((double) PubVar.ScaledDensity);
        this._SnapDistance *= this._SnapDistance;
    }

    public void SetIsEnabelDraw(boolean isEnabelDraw) {
        this._IsEnabelDraw = isEnabelDraw;
    }

    public boolean getIsEnabelDraw() {
        return this._IsEnabelDraw;
    }

    private void AddPoint(MotionEvent paramMotionEvent) {
        this._CurrentDrawType = "手绘";
        PointF localPointF = new PointF(paramMotionEvent.getX(), paramMotionEvent.getY());
        Coordinate tmpCoord = null;
        if (this._AllowSnap && !this._IsHandFreeMode) {
            tmpCoord = CheckSnap(this._mapView, localPointF, this._SnapDistance);
        }
        if (tmpCoord == null) {
            tmpCoord = this._mapView.getMap().ScreenToMap(localPointF);
            Coordinate localCoordinate2 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tmpCoord, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
            tmpCoord.setGeoX(localCoordinate2.getX());
            tmpCoord.setGeoY(localCoordinate2.getY());
        }
        if (tmpCoord != null) {
            AddPoint(tmpCoord);
        }
    }

    public static Coordinate CheckSnap(MapView mapView, PointF point, double snapDistance) {
        List<Coordinate> tmpCoords = new ArrayList<>();
        List<PointF> tmpList = mapView._Select.RefreshScreenShowSelectPoints(tmpCoords);
        if (tmpList.size() <= 0) {
            return null;
        }
        int tmpI = -1;
        int tid = -1;
        double tmpMinD = snapDistance;
        for (PointF tmpPtn : tmpList) {
            tid++;
            double tmpD = (double) (((tmpPtn.x - point.x) * (tmpPtn.x - point.x)) + ((tmpPtn.y - point.y) * (tmpPtn.y - point.y)));
            if (tmpD < tmpMinD) {
                tmpI = tid;
                tmpMinD = tmpD;
            }
        }
        if (tmpI != -1) {
            return tmpCoords.get(tmpI).Clone();
        }
        return null;
    }

    public void AddPoint(Coordinate coordinate) {
        if (this.m_MeasurePointList.size() <= 0 || !this.m_MeasurePointList.get(this.m_MeasurePointList.size() - 1).Equal(coordinate)) {
            this.m_MeasurePointList.add(coordinate);
            while (this.m_ResMeasurePointList.size() > this.m_MeasurePtnCount) {
                this.m_ResMeasurePointList.remove(this.m_ResMeasurePointList.size() - 1);
            }
            this.m_ResMeasurePointList.add(coordinate);
            this.m_MeasurePtnCount++;
            this._Polygon.CalEnvelope();
            this._Polygon.ComputeArea();
            this._mapView.invalidate();
        }
    }

    public boolean RedoLastPoint(String[] outMsg) {
        outMsg[0] = "";
        if (this.m_MeasurePointList.size() < this.m_ResMeasurePointList.size()) {
            this.m_MeasurePointList.add(this.m_ResMeasurePointList.get(this.m_MeasurePointList.size()));
            this._Polygon.CalEnvelope();
            this._Polygon.ComputeArea();
            this._mapView.invalidate();
            this.m_MeasurePtnCount++;
            return true;
        }
        outMsg[0] = "已经是最后一个点.";
        return false;
    }

    public boolean UndoLastPoint(String[] outMsg) {
        outMsg[0] = "";
        if (this.m_MeasurePointList.size() > 0) {
            this.m_MeasurePointList.remove(this.m_MeasurePointList.size() - 1);
            this._Polygon.CalEnvelope();
            this._Polygon.ComputeArea();
            this._mapView.invalidate();
            this.m_MeasurePtnCount--;
            return true;
        }
        outMsg[0] = "没有可以取消的点.";
        return false;
    }

    public void Clear() {
        this.m_MeasurePtnCount = 0;
        this.m_MeasurePointList.clear();
        this.m_ResMeasurePointList.clear();
        this._mapView.invalidate();
    }

    public boolean ReversCoords() {
        if (this.m_MeasurePointList.size() <= 0) {
            return false;
        }
        Collections.reverse(this.m_MeasurePointList);
        Collections.copy(this.m_ResMeasurePointList, this.m_MeasurePointList);
        return true;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ZoomInOutPan,  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas paramCanvas) {
        if (!this._IsEnabelDraw || !this._IsHandFreeMode) {
            drawCurrentPolygon(paramCanvas);
            super.OnPaint(paramCanvas);
            return;
        }
        super.OnPaint(paramCanvas);
        drawCurrentPolygon(paramCanvas);
    }

    private void drawCurrentPolygon(Canvas paramCanvas) {
        try {
            if (this.m_MeasurePointList.size() == 1) {
                Point tmpPoint = this._mapView.getMap().MapToScreen(this.m_MeasurePointList.get(0));
                if (tmpPoint.x >= 0 && tmpPoint.x <= PubVar.ScreenWidth && tmpPoint.y >= 0 && tmpPoint.y <= PubVar.ScreenHeight) {
                    paramCanvas.drawCircle((float) tmpPoint.x, (float) tmpPoint.y, (float) (PolylineEditClass.EditingLineNodeSize + 5), this._LineVertexPaint01);
                    paramCanvas.drawCircle((float) tmpPoint.x, (float) tmpPoint.y, (float) PolylineEditClass.EditingLineNodeSize, this._LineVertexPaint02);
                    paramCanvas.drawCircle((float) tmpPoint.x, (float) tmpPoint.y, (float) PolylineEditClass.EditingLineNodeCircleSize, this._LineEndVPaint);
                }
            } else if (this.m_MeasurePointList.size() > 1) {
                if (this.m_MeasurePointList.size() > 2 && PubVar.Draw_Polygon_ShowCurrent) {
                    this.m_PolygonSymbol.Draw(this._mapView.getMap(), paramCanvas, this._Polygon, 0, 0, EDrawType.NONE);
                }
                List<Integer> tmpPtnIndexList = new ArrayList<>();
                Point[] tmpLinePtns = this._mapView.getMap().ClipPolyline(this.m_MeasurePointList, tmpPtnIndexList, 0, 0);
                if (tmpLinePtns != null && tmpLinePtns.length > 0) {
                    if (tmpLinePtns.length > 1) {
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
                        paramCanvas.drawPath(localPath, this._linePaint);
                    }
                    Point tmpEndPtn = this._mapView.getMap().MapToScreen(this.m_MeasurePointList.get(this.m_MeasurePointList.size() - 1));
                    if (tmpEndPtn.x >= 0 && tmpEndPtn.x <= PubVar.ScreenWidth && tmpEndPtn.y >= 0 && tmpEndPtn.y <= PubVar.ScreenHeight) {
                        paramCanvas.drawCircle((float) tmpEndPtn.x, (float) tmpEndPtn.y, (float) PolylineEditClass.EditingLineNodeCircleSize, this._LineEndVPaint);
                    }
                    for (Point tmpPoint2 : tmpLinePtns) {
                        if (tmpPoint2.x >= 0 && tmpPoint2.x <= PubVar.ScreenWidth && tmpPoint2.y >= 0 && tmpPoint2.y <= PubVar.ScreenHeight) {
                            paramCanvas.drawCircle((float) tmpPoint2.x, (float) tmpPoint2.y, ((float) PolylineEditClass.EditingLineNodeSize) + (2.0f * PubVar.ScaledDensity), this._LineVertexPaint01);
                            paramCanvas.drawCircle((float) tmpPoint2.x, (float) tmpPoint2.y, (float) PolylineEditClass.EditingLineNodeSize, this._LineVertexPaint02);
                        }
                    }
                }
                double tempArea = this._Polygon.getArea(false);
                if (tempArea > 0.0d) {
                    String tempMgs = "【" + this._CurrentDrawType + "】面:" + Common.SimplifyArea(tempArea, true);
                    if (PubVar.MessageDialogAllowShow) {
                        PubVar.OutputMessage(tempMgs);
                    } else {
                        drawText(paramCanvas, tempMgs);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void SetCurrentDrawType(String currentDrawType) {
        this._CurrentDrawType = currentDrawType;
    }

    public void SetHandFreeMode(boolean isHandFreeMode) {
        this._IsHandFreeMode = isHandFreeMode;
    }

    private void drawText(Canvas paramCanvas, String content) {
        if (content != null && !content.equals("")) {
            float tempX = (float) paramCanvas.getWidth();
            float tempY = (float) (paramCanvas.getHeight() - 10);
            Paint.FontMetrics fontMetrics = this._TextPaint.getFontMetrics();
            float tempX2 = tempX / 2.0f;
            float tempY2 = ((tempY - (fontMetrics.bottom - fontMetrics.top)) - fontMetrics.bottom) - (48.0f * PubVar.ScaledDensity);
            float tempBias = 3.0f * PubVar.ScaledDensity;
            float tmpTxtsWidth = this._TextBgPaint.measureText(content);
            paramCanvas.drawRoundRect(new RectF((tempX2 - (tmpTxtsWidth / 2.0f)) - tempBias, (fontMetrics.top + tempY2) - tempBias, (tmpTxtsWidth / 2.0f) + tempX2 + tempBias, fontMetrics.bottom + tempY2 + tempBias), 10.0f, 10.0f, this._TextBgPaint);
            paramCanvas.drawText(content, tempX2, tempY2, this._TextPaint);
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ZoomInOutPan,  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseDown(MotionEvent paramMotionEvent) {
        if (!this._IsHandFreeMode) {
            super.MouseDown(paramMotionEvent);
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ZoomInOutPan,  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseMove(MotionEvent paramMotionEvent) {
        if (!this._IsEnabelDraw || !this._IsHandFreeMode) {
            super.MouseMove(paramMotionEvent);
            return;
        }
        boolean tmpBool = false;
        if (this.m_MeasurePtnCount == 0) {
            tmpBool = true;
        } else {
            if (Common.GetTwoPointDistance(this._mapView.getMap().ScreenToMap(new PointF(paramMotionEvent.getX(), paramMotionEvent.getY())), this.m_MeasurePointList.get(this.m_MeasurePtnCount - 1)) > 1.0d) {
                tmpBool = true;
            }
        }
        if (tmpBool) {
            AddPoint(paramMotionEvent);
            if (this._mapView.m_ShutterTool.getIsShutterMode()) {
                this._mapView.getMap().RefreshFast2();
            } else {
                this._mapView.invalidate();
            }
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ZoomInOutPan,  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseUp(MotionEvent paramMotionEvent) {
        if (!this._IsEnabelDraw || !this._IsHandFreeMode) {
            super.MouseUp(paramMotionEvent);
            if (!this.IsMove && !this.IsDoubleClick && this._IsEnabelDraw) {
                AddPoint(paramMotionEvent);
                return;
            }
            return;
        }
        super.getMapView().getMap().setAllowRefreshMap(true);
        super.getMapView().setAllowRefreshMap(true);
    }

    public Coordinate GetLastCoordinate() {
        if (this.m_MeasurePointList.size() > 0) {
            return this.m_MeasurePointList.get(this.m_MeasurePointList.size() - 1);
        }
        return null;
    }

    public List<Coordinate> GetAllCoordinates() {
        return this.m_MeasurePointList;
    }

    public int getPointsCount() {
        return this.m_MeasurePtnCount;
    }
}
