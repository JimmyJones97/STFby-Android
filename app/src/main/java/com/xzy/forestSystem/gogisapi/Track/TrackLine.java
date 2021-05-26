package  com.xzy.forestSystem.gogisapi.Track;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.IOnPaint;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TrackLine implements IOnPaint {
    static final int ArrowFillColor = -16711936;
    static final double ArrowLen = ((double) (12.0f * PubVar.ScaledDensity));
    static final int EditingLineColor = -65281;
    static final int EditingLineNodeCircleSize = ((int) (10.0f * PubVar.ScaledDensity));
    static final int EditingLineNodeColor = -16711936;
    static final int EditingLineNodeColor2 = -65536;
    static final int EditingLineNodeColor3 = -15472922;
    static final int EditingLineNodeSize = ((int) (3.0f * PubVar.ScaledDensity));
    static final int EditingLineWidth = ((int) (2.0f * PubVar.ScaledDensity));
    static final float TipTextSize = (14.0f * PubVar.ScaledDensity);
    private Paint _ArrowPaint = null;
    private Paint _ArrowPaint2 = null;
    private Paint _CurrentTextPaint = null;
    private boolean _IsEnabelDraw = false;
    private boolean _IsQueryTrack = false;
    private boolean _IsRecordByGPS = false;
    private Coordinate _LastCoord = null;
    private int _LastCoordIndex = -1;
    private long _LastLocTime = 0;
    private Paint _LineEndVPaint = null;
    private Paint _LineVertexPaint01 = null;
    private Paint _LineVertexPaint02 = null;
    private Bitmap _MaskBitmap = null;
    private Paint _TextBgPaint = null;
    private Paint _TextPaint = null;
    private long _ThreadLoopTime = 1000;
    private Paint _TipTextBgPaint = null;
    private Paint _TipTextPaint = null;
    private List<Coordinate> _TrackCoords = new ArrayList();
    private List<TrackPoint> _TrackPoints = new ArrayList();
    private Paint _linePaint = null;
    private float fontBottom = 10.0f;
    private float fontHeight = 20.0f;
    List<Coordinate> m_SingleTrack = new ArrayList();
    List<TrackPoint> m_SingleTrackPoints = new ArrayList();
    private Handler myGPSRecordHandler = new Handler();
    private Runnable myRecordLineByGPSTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Track.TrackLine.1
        @Override // java.lang.Runnable
        public void run() {
            if (TrackLine.this._IsRecordByGPS) {
                try {
                    if (PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                        Coordinate tempCoord = PubVar._PubCommand.m_GpsLocation.getGPSCoordinate2();
                        if (!tempCoord.Equal(TrackLine.this._LastCoord)) {
                            boolean tempBool = false;
                            boolean tmpIsFirst = false;
                            long tempTime = new Date().getTime();
                            if (TrackLine.this._LastCoord == null) {
                                tempBool = true;
                                tmpIsFirst = true;
                            } else if (PubVar.Track_Record_Type == 0) {
                                if (tempTime - TrackLine.this._LastLocTime >= PubVar.GPSGatherIntervalTime) {
                                    tempBool = true;
                                }
                            } else if (Common.GetTwoPointDistanceOfWGS84(tempCoord, TrackLine.this._LastCoord) >= PubVar.Track_Record_Param) {
                                tempBool = true;
                            }
                            if (tempBool) {
                                TrackLine.this._LastCoord = tempCoord;
                                TrackLine.this._LastLocTime = tempTime;
                                TrackPoint tmpTrackPoint = new TrackPoint();
                                tmpTrackPoint._Time = PubVar._PubCommand.m_GpsLocation.loctime;
                                tmpTrackPoint._Longitude = PubVar._PubCommand.m_GpsLocation.longtitude;
                                tmpTrackPoint._Latitude = PubVar._PubCommand.m_GpsLocation.latitude;
                                tmpTrackPoint._Elevation = PubVar._PubCommand.m_GpsLocation.elevation;
                                tmpTrackPoint._Accuracy = PubVar._PubCommand.m_GpsLocation.accuracy;
                                tmpTrackPoint._Direction = (double) PubVar._PubCommand.m_GpsLocation.bearing;
                                tmpTrackPoint._Speed = PubVar._PubCommand.m_GpsLocation.rate;
                                tmpTrackPoint._TimeStr = Common.dateFormat.format(new Date(tmpTrackPoint._Time));
                                tmpTrackPoint.SaveData(tmpIsFirst);
                                if (TrackLine.this._IsEnabelDraw && !TrackLine.this._IsQueryTrack) {
                                    TrackLine.this._TrackCoords.add(PubVar._PubCommand.m_GpsLocation.getGPSCoordinate());
                                    TrackLine.this._TrackPoints.add(tmpTrackPoint);
                                    if (TrackLine.this._TrackCoords.size() >= PubVar.Track_Point_MaxCount) {
                                        TrackLine.this.RemoveCount(PubVar.Track_Point_MaxCount / 2);
                                    }
                                    TrackLine.this._LastCoordIndex = TrackLine.this._TrackCoords.size() - 1;
                                    PubVar._MapView.invalidate();
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }
                TrackLine.this.myGPSRecordHandler.postDelayed(TrackLine.this.myRecordLineByGPSTask, TrackLine.this._ThreadLoopTime);
            }
        }
    };

    public TrackLine() {
        PubVar._MapView._TrackLinePaint = this;
        this._linePaint = new Paint();
        this._linePaint.setStyle(Paint.Style.STROKE);
        this._linePaint.setColor(EditingLineColor);
        this._linePaint.setStrokeWidth((float) EditingLineWidth);
        this._linePaint.setAntiAlias(true);
        this._ArrowPaint = new Paint();
        this._ArrowPaint.setStyle(Paint.Style.FILL);
        this._ArrowPaint.setColor(-16711936);
        this._ArrowPaint.setAntiAlias(true);
        this._ArrowPaint2 = new Paint();
        this._ArrowPaint2.setStyle(Paint.Style.STROKE);
        this._ArrowPaint2.setColor(-65536);
        this._ArrowPaint2.setStrokeWidth(1.0f);
        this._ArrowPaint2.setAntiAlias(true);
        this._LineVertexPaint01 = new Paint();
        this._LineVertexPaint01.setColor(-16711936);
        this._LineVertexPaint01.setAntiAlias(true);
        this._LineVertexPaint02 = new Paint();
        this._LineVertexPaint02.setAntiAlias(true);
        this._LineVertexPaint02.setColor(-65536);
        this._LineEndVPaint = new Paint();
        this._LineEndVPaint.setAntiAlias(true);
        this._LineEndVPaint.setStyle(Paint.Style.STROKE);
        this._LineEndVPaint.setColor(EditingLineNodeColor3);
        this._LineEndVPaint.setStrokeWidth((float) EditingLineWidth);
        this._TextPaint = new Paint();
        this._TextPaint.setAntiAlias(true);
        this._TextPaint.setTextSize(TipTextSize);
        this._TextPaint.setColor(-16776961);
        Typeface localTypeface = Typeface.create("宋体", 0);
        this._CurrentTextPaint = new Paint();
        this._CurrentTextPaint.setAntiAlias(true);
        this._CurrentTextPaint.setTextSize(TipTextSize);
        this._CurrentTextPaint.setColor(-65536);
        this._TextBgPaint = new Paint();
        this._TextBgPaint.setColor(-1);
        this._TextBgPaint.setAntiAlias(true);
        this._TextBgPaint.setAntiAlias(true);
        this._TextBgPaint.setTextSize(TipTextSize);
        this._TipTextPaint = new Paint();
        this._TipTextPaint.setAntiAlias(true);
        this._TipTextPaint.setTextSize(TipTextSize);
        this._TipTextPaint.setColor(-16776961);
        this._TipTextPaint.setTypeface(localTypeface);
        this._TipTextPaint.setTextAlign(Paint.Align.CENTER);
        this._TipTextBgPaint = new Paint();
        this._TipTextBgPaint.setColor(-1);
        this._TipTextBgPaint.setAntiAlias(true);
        this._TipTextBgPaint.setTypeface(localTypeface);
        this._TipTextBgPaint.setAntiAlias(true);
        this._TipTextBgPaint.setTextSize(TipTextSize);
        this._TipTextBgPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = this._TipTextBgPaint.getFontMetrics();
        this.fontHeight = fontMetrics.bottom - fontMetrics.top;
        this.fontBottom = fontMetrics.bottom;
        SetIsRecordByGPS(PubVar.Track_Auto_Record);
    }

    public void SetIsRecordByGPS(boolean value) {
        if (value && !this._IsRecordByGPS) {
            UpdateParas();
            this.myGPSRecordHandler.post(this.myRecordLineByGPSTask);
        }
        if (!value) {
            this._LastCoord = null;
            this._LastLocTime = 0;
        }
        this._IsRecordByGPS = value;
    }

    public void SetEnabelDraw(boolean value) {
        this._IsEnabelDraw = value;
        PubVar._MapView.invalidate();
    }

    public boolean getIsRecord() {
        return this._IsRecordByGPS;
    }

    public boolean getEnabelDraw() {
        return this._IsEnabelDraw;
    }

    public void UpdateParas() {
        if (PubVar.Track_Record_Type == 0) {
            this._ThreadLoopTime = (long) (PubVar.Track_Record_Param * 1000.0d);
            if (this._ThreadLoopTime < 500) {
                this._ThreadLoopTime = 1000;
                return;
            }
            return;
        }
        this._ThreadLoopTime = 1000;
    }

    public void Clear() {
        this._TrackPoints.clear();
        this._TrackCoords.clear();
        this.m_SingleTrackPoints.clear();
        this.m_SingleTrack.clear();
        PubVar._MapView.invalidate();
    }

    public void SetIsQueryTrack(boolean value) {
        this._IsQueryTrack = value;
    }

    public boolean getIsQueryTrack() {
        return this._IsQueryTrack;
    }

    public void RemoveCount(int count) {
        int tid = -1;
        try {
            int tmpMaxCount = this._TrackCoords.size();
            this._LastCoordIndex = (tmpMaxCount - count) - 1;
            if (tmpMaxCount > count) {
                tmpMaxCount = count;
            }
            Iterator iter = this._TrackCoords.iterator();
            while (iter.hasNext() && (tid = tid + 1) < tmpMaxCount) {
                iter.remove();
            }
            Iterator iter2 = this._TrackPoints.iterator();
            while (iter2.hasNext() && (tid = tid + 1) < tmpMaxCount) {
                iter2.remove();
            }
        } catch (Exception e) {
        }
    }

    public void AddTrackPoints(List<TrackPoint> list) {
        try {
            AbstractC0383CoordinateSystem tempSystem = PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem();
            for (TrackPoint trackPoint : list) {
                TrackPoint tmpPoint = trackPoint.clone();
                this._TrackPoints.add(tmpPoint);
                this._TrackCoords.add(PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(tmpPoint._Longitude, tmpPoint._Latitude, tmpPoint._Elevation, tempSystem));
            }
            this._LastCoordIndex = this._TrackCoords.size();
        } catch (Exception e) {
        } finally {
            PubVar._MapView.invalidate();
        }
    }

    private Bitmap getMaskBitmap() {
        Point[] arrayOfPoint;
        try {
            if (this._MaskBitmap == null) {
                this._MaskBitmap = Bitmap.createBitmap(PubVar._Map.getSize().getWidth(), PubVar._Map.getSize().getHeight(), Bitmap.Config.ARGB_4444);
            }
            this._MaskBitmap.eraseColor(0);
            Canvas paramCanvas = new Canvas(this._MaskBitmap);
            if (this.m_SingleTrack.size() > 0 && (arrayOfPoint = PubVar._Map.MapPointsToScreePoints(this.m_SingleTrack)) != null && arrayOfPoint.length > 0) {
                int tid = -1;
                for (Point tmpPoint : arrayOfPoint) {
                    tid++;
                    if (tmpPoint.x >= 0 && tmpPoint.x <= PubVar.ScreenWidth && tmpPoint.y >= 0 && tmpPoint.y <= PubVar.ScreenHeight) {
                        paramCanvas.drawCircle((float) tmpPoint.x, (float) tmpPoint.y, ((float) EditingLineNodeSize) + (2.0f * PubVar.ScaledDensity), this._LineVertexPaint01);
                        paramCanvas.drawCircle((float) tmpPoint.x, (float) tmpPoint.y, (float) EditingLineNodeSize, this._LineVertexPaint02);
                        if (PubVar.Track_Draw_Date) {
                            drawText(paramCanvas, this.m_SingleTrackPoints.get(tid)._TimeStr, tmpPoint);
                        }
                    }
                }
            }
            if (this._TrackCoords.size() > 0) {
                List<Integer> tmpPtnIndexList = new ArrayList<>();
                Point[] tmpLinePtns = PubVar._MapView.getMap().ClipPolyline(this._TrackCoords, tmpPtnIndexList, 0, 0);
                if (tmpLinePtns != null && tmpLinePtns.length > 1) {
                    int tmpStart = 0;
                    int tmpCount = tmpPtnIndexList.size();
                    for (int i = 0; i < tmpCount; i++) {
                        int tmpEnd = tmpPtnIndexList.get(i).intValue();
                        Path tmpPath = CreatePathByIndex(tmpLinePtns, tmpStart, tmpEnd);
                        if (tmpPath != null && !tmpPath.isEmpty()) {
                            paramCanvas.drawPath(tmpPath, this._linePaint);
                            drawArrow(paramCanvas, tmpLinePtns, tmpStart, tmpEnd);
                        }
                        tmpStart = tmpEnd;
                    }
                }
                Point[] arrayOfPoint2 = PubVar._Map.MapPointsToScreePoints(this._TrackCoords);
                if (arrayOfPoint2 != null && arrayOfPoint2.length > 0) {
                    int tid2 = -1;
                    for (Point tmpPoint2 : arrayOfPoint2) {
                        tid2++;
                        if (tmpPoint2.x >= 0 && tmpPoint2.x <= PubVar.ScreenWidth && tmpPoint2.y >= 0 && tmpPoint2.y <= PubVar.ScreenHeight) {
                            paramCanvas.drawCircle((float) tmpPoint2.x, (float) tmpPoint2.y, ((float) EditingLineNodeSize) + (2.0f * PubVar.ScaledDensity), this._LineVertexPaint01);
                            paramCanvas.drawCircle((float) tmpPoint2.x, (float) tmpPoint2.y, (float) EditingLineNodeSize, this._LineVertexPaint02);
                            if (PubVar.Track_Draw_Date) {
                                drawText(paramCanvas, this._TrackPoints.get(tid2)._TimeStr, tmpPoint2);
                            }
                        }
                    }
                }
                if (this._LastCoordIndex >= 0 && this._LastCoordIndex < this._TrackCoords.size()) {
                    Point tmpCenterPtn = PubVar._Map.MapToScreen(this._TrackCoords.get(this._LastCoordIndex));
                    if (tmpCenterPtn.x >= 0 && tmpCenterPtn.x <= PubVar.ScreenWidth && tmpCenterPtn.y >= 0 && tmpCenterPtn.y <= PubVar.ScreenHeight) {
                        paramCanvas.drawCircle((float) tmpCenterPtn.x, (float) tmpCenterPtn.y, ((float) EditingLineNodeSize) + (2.0f * PubVar.ScaledDensity), this._LineVertexPaint01);
                        paramCanvas.drawCircle((float) tmpCenterPtn.x, (float) tmpCenterPtn.y, (float) EditingLineNodeSize, this._LineVertexPaint02);
                        paramCanvas.drawCircle((float) tmpCenterPtn.x, (float) tmpCenterPtn.y, (float) EditingLineNodeCircleSize, this._LineEndVPaint);
                        TrackPoint tmpPtn = this._TrackPoints.get(this._LastCoordIndex);
                        if (PubVar.Track_Draw_Date) {
                            drawText(paramCanvas, tmpPtn._TimeStr, (float) tmpCenterPtn.x, (float) tmpCenterPtn.y, this._CurrentTextPaint);
                        }
                        drawBottomText(paramCanvas, "[" + String.valueOf(this._LastCoordIndex + 1) + FileSelector_Dialog.sRoot + String.valueOf(this._TrackCoords.size()) + "] " + tmpPtn._TimeStr, 1);
                        drawBottomText(paramCanvas, "经度:" + Common.ConvertDegree(tmpPtn._Longitude), 2);
                        drawBottomText(paramCanvas, "纬度:" + Common.ConvertDegree(tmpPtn._Latitude), 3);
                        drawBottomText(paramCanvas, "高程:" + String.valueOf(tmpPtn._Elevation), 4);
                    }
                }
            }
        } catch (Exception e) {
        }
        return this._MaskBitmap;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas paramCanvas) {
        try {
            if (this._IsEnabelDraw) {
                paramCanvas.drawBitmap(getMaskBitmap(), 0.0f, 0.0f, (Paint) null);
            }
        } catch (Exception e) {
        }
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

    public void gotoLastCoord() {
        try {
            if (this._LastCoordIndex > 0) {
                this._LastCoordIndex--;
                PubVar._PubCommand.ProcessCommand("解锁GPS");
                TrackPoint tmpPtn = this._TrackPoints.get(this._LastCoordIndex);
                Coordinate tempCoord = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(tmpPtn._Longitude, tmpPtn._Latitude, tmpPtn._Elevation, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                PubVar._Map.ZoomToCenter(tempCoord.getX(), tempCoord.getY());
                return;
            }
            Common.ShowToast("已经是第一个足迹点.");
        } catch (Exception e) {
        }
    }

    public void gotoNextCoord() {
        try {
            this._LastCoordIndex++;
            if (this._LastCoordIndex >= this._TrackCoords.size()) {
                this._LastCoordIndex = this._TrackCoords.size() - 1;
                Common.ShowToast("已经是最后一个足迹点.");
            }
            PubVar._PubCommand.ProcessCommand("解锁GPS");
            TrackPoint tmpPtn = this._TrackPoints.get(this._LastCoordIndex);
            Coordinate tempCoord = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(tmpPtn._Longitude, tmpPtn._Latitude, tmpPtn._Elevation, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
            PubVar._Map.ZoomToCenter(tempCoord.getX(), tempCoord.getY());
        } catch (Exception e) {
        }
    }

    public void gotoCoordByIndex(int index) {
        if (index >= 0) {
            try {
                if (index < this._TrackPoints.size()) {
                    PubVar._PubCommand.ProcessCommand("解锁GPS");
                    TrackPoint tmpPtn = this._TrackPoints.get(index);
                    Coordinate tempCoord = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(tmpPtn._Longitude, tmpPtn._Latitude, tmpPtn._Elevation, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                    PubVar._Map.ZoomToCenter(tempCoord.getX(), tempCoord.getY());
                    this._LastCoordIndex = index;
                }
            } catch (Exception e) {
            }
        }
    }

    private void drawText(Canvas paramCanvas, String content, Point point) {
        if (content != null) {
            try {
                if (!content.equals("")) {
                    float tempX = (float) (point.x + EditingLineNodeCircleSize);
                    float tempY = (float) point.y;
                    paramCanvas.drawText(content, tempX - 1.0f, tempY, this._TextBgPaint);
                    paramCanvas.drawText(content, 1.0f + tempX + 1.0f, tempY, this._TextBgPaint);
                    paramCanvas.drawText(content, tempX, tempY - 1.0f, this._TextBgPaint);
                    paramCanvas.drawText(content, tempX, 1.0f + tempY + 1.0f, this._TextBgPaint);
                    paramCanvas.drawText(content, 1.0f + tempX, 1.0f + tempY, this._TextBgPaint);
                    paramCanvas.drawText(content, tempX - 1.0f, tempY - 1.0f, this._TextBgPaint);
                    paramCanvas.drawText(content, 1.0f + tempX, tempY - 1.0f, this._TextBgPaint);
                    paramCanvas.drawText(content, tempX - 1.0f, 1.0f + tempY, this._TextBgPaint);
                    paramCanvas.drawText(content, tempX, tempY, this._TextPaint);
                }
            } catch (Exception e) {
            }
        }
    }

    private void drawText(Canvas paramCanvas, String content, float x, float y, Paint paint) {
        paramCanvas.drawText(content, x + ((float) EditingLineNodeCircleSize), y, paint);
    }

    private void drawBottomText(Canvas paramCanvas, String content, int step) {
        if (content != null) {
            try {
                if (!content.equals("")) {
                    float tempX = ((float) paramCanvas.getWidth()) / 2.0f;
                    PubVar._PubCommand.m_MapCompLayoutToolbar.RefreshGPSInfoHeadHeight();
                    float tempY = ((float) PubVar._PubCommand.GPSInfoPanelHeight) + (((float) step) * (this.fontHeight + 5.0f)) + (10.0f * PubVar.ScaledDensity);
                    paramCanvas.drawText(content, tempX - 1.0f, tempY, this._TipTextBgPaint);
                    paramCanvas.drawText(content, 1.0f + tempX + 1.0f, tempY, this._TipTextBgPaint);
                    paramCanvas.drawText(content, tempX, tempY - 1.0f, this._TipTextBgPaint);
                    paramCanvas.drawText(content, tempX, 1.0f + tempY + 1.0f, this._TipTextBgPaint);
                    paramCanvas.drawText(content, 1.0f + tempX, 1.0f + tempY, this._TipTextBgPaint);
                    paramCanvas.drawText(content, tempX - 1.0f, tempY - 1.0f, this._TipTextBgPaint);
                    paramCanvas.drawText(content, 1.0f + tempX, tempY - 1.0f, this._TipTextBgPaint);
                    paramCanvas.drawText(content, tempX - 1.0f, 1.0f + tempY, this._TipTextBgPaint);
                    paramCanvas.drawText(content, tempX, tempY, this._TipTextPaint);
                }
            } catch (Exception e) {
            }
        }
    }

    private void drawArrow(Canvas canvas, Point[] points, int fromIndex, int toIndex) {
        if (toIndex > fromIndex) {
            int toIndex2 = toIndex - 1;
            for (int i = fromIndex; i < toIndex2; i++) {
                try {
                    Point p1 = points[i];
                    Point p2 = points[i + 1];
                    if (GetTowPointDistance(p1, p2) >= 4.0d * ArrowLen) {
                        double tmpCenterX = (double) ((p1.x + p2.x) / 2);
                        double tmpCenterY = (double) ((p1.y + p2.y) / 2);
                        double v1x = tmpCenterX - ((double) p2.x);
                        double v1y = tmpCenterY - ((double) p2.y);
                        double tmpLen = Math.sqrt((v1x * v1x) + (v1y * v1y));
                        if (tmpLen != ArrowLen) {
                            double tmpLen2 = ArrowLen / tmpLen;
                            double v1x2 = v1x * tmpLen2;
                            double v1y2 = v1y * tmpLen2;
                            double tmpX = ((Math.cos(0.785398163d) * v1x2) + tmpCenterX) - (Math.sin(0.785398163d) * v1y2);
                            double tmpY = (Math.sin(0.785398163d) * v1x2) + tmpCenterY + (Math.cos(0.785398163d) * v1y2);
                            Path tmpPath = new Path();
                            tmpPath.moveTo((float) tmpX, (float) tmpY);
                            tmpPath.lineTo((float) tmpCenterX, (float) tmpCenterY);
                            double rotAngle = -0.785398163d;
                            tmpPath.lineTo((float) (((Math.cos(rotAngle) * v1x2) + tmpCenterX) - (Math.sin(rotAngle) * v1y2)), (float) ((Math.sin(rotAngle) * v1x2) + tmpCenterY + (Math.cos(rotAngle) * v1y2)));
                            tmpPath.lineTo((float) (tmpCenterX - v1x2), (float) (tmpCenterY - v1y2));
                            tmpPath.lineTo((float) tmpX, (float) tmpY);
                            if (!tmpPath.isEmpty()) {
                                canvas.drawPath(tmpPath, this._ArrowPaint);
                                canvas.drawPath(tmpPath, this._ArrowPaint2);
                            }
                        }
                    }
                } catch (Exception e) {
                    return;
                }
            }
        }
    }

    private double GetTowPointDistance(Point P1, Point P2) {
        return Math.sqrt((double) (((P1.x - P2.x) * (P1.x - P2.x)) + ((P1.y - P2.y) * (P1.y - P2.y))));
    }

    public void AddSingleTrackPoint(TrackPoint trackPoint, Coordinate coordinate) {
        this.m_SingleTrackPoints.add(trackPoint);
        this.m_SingleTrack.add(coordinate);
    }
}
