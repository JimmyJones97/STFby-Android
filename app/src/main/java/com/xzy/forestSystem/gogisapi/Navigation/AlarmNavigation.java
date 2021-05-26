package  com.xzy.forestSystem.gogisapi.Navigation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Vibrator;
import androidx.core.internal.view.SupportMenu;
import com.xzy.forestSystem.baidu.speech.easr.easrNativeJni;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.IOnPaint;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Config.UserParam;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.ExtraGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import com.stczh.gzforestSystem.R;
import java.util.HashMap;
import java.util.Iterator;

public class AlarmNavigation implements IOnPaint {
    private static final float CricleRadius = (4.0f * PubVar.ScaledDensity);
    static final float TipTextDisHeight = (5.0f * PubVar.ScaledDensity);
    static final float TipTextDisHeight2 = (-15.0f * PubVar.ScaledDensity);
    static final float TipTextSize = (14.0f * PubVar.ScaledDensity);
    private static final float lineWidth = (2.0f * PubVar.ScaledDensity);
    private boolean Is_Alarm = false;
    private Bitmap _MaskBitmap = null;
    private Paint _NavLinePaint = null;
    private Paint _PointPaint = null;
    private Paint _PointPaintCircle = null;
    private Paint _TextBgPaint = null;
    private Paint _TextPaint = null;
    private Paint _TipTextBgPaint = null;
    private Paint _TipTextPaint = null;
    double m_AlarmDistance = 1000.0d;
    private DataSet m_DataSet = null;
    boolean m_DrawLineBool = true;
    boolean m_DrawTargetBool = true;
    private long m_GPSRefreshInterval = 10000;
    private boolean m_IsStart = false;
    private Coordinate m_LastGPSCoord = null;
    private NavigatePoint m_NavigatePoint = null;
    private Bitmap m_TargetICON = null;
    private Bitmap m_TargetLocICON = null;
    private Handler myGPSRecordHandler = new Handler();
    private HashMap<Integer, Integer> mySoundPMap = null;
    private SoundPool mySoundPool = null;
    private Runnable myUpdateByGPSTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.AlarmNavigation.1
        @Override // java.lang.Runnable
        public void run() {
            if (AlarmNavigation.this.m_IsStart) {
                AlarmNavigation.this.UpdateGPSLocation();
                AlarmNavigation.this.myGPSRecordHandler.postDelayed(AlarmNavigation.this.myUpdateByGPSTask, AlarmNavigation.this.m_GPSRefreshInterval);
            }
        }
    };
    private Vibrator myVibrator;

    public AlarmNavigation() {
        String tempStr;
        String tempStr2;
        String tempStr3;
        try {
            this._TextPaint = new Paint();
            this._TextPaint.setAntiAlias(true);
            this._TextPaint.setTextSize(TipTextSize);
            this._TextPaint.setColor(SupportMenu.CATEGORY_MASK);
            Typeface localTypeface = Typeface.create("宋体", 0);
            this._TextBgPaint = new Paint();
            this._TextBgPaint.setColor(-1);
            this._TextBgPaint.setAntiAlias(true);
            this._TextBgPaint.setAntiAlias(true);
            this._TextBgPaint.setTextSize(TipTextSize);
            this._TipTextPaint = new Paint();
            this._TipTextPaint.setAntiAlias(true);
            this._TipTextPaint.setTextSize(TipTextSize);
            this._TipTextPaint.setColor(SupportMenu.CATEGORY_MASK);
            this._TipTextPaint.setTypeface(localTypeface);
            this._TipTextPaint.setTextAlign(Paint.Align.CENTER);
            this._TipTextBgPaint = new Paint();
            this._TipTextBgPaint.setColor(-1);
            this._TipTextBgPaint.setAntiAlias(true);
            this._TipTextBgPaint.setTypeface(localTypeface);
            this._TipTextBgPaint.setAntiAlias(true);
            this._TipTextBgPaint.setTextSize(TipTextSize);
            this._TipTextBgPaint.setTextAlign(Paint.Align.CENTER);
            this._PointPaint = new Paint();
            this._PointPaint.setColor(-2130706688);
            this._PointPaint.setAntiAlias(true);
            this._PointPaintCircle = new Paint();
            this._PointPaintCircle.setStyle(Paint.Style.STROKE);
            this._PointPaintCircle.setStrokeWidth(lineWidth);
            this._PointPaintCircle.setColor(-16711936);
            this._PointPaintCircle.setAntiAlias(true);
            this.m_NavigatePoint = new NavigatePoint();
            if (this.mySoundPool == null) {
                this.mySoundPool = new SoundPool(7, 1, 0);
                this.mySoundPMap = new HashMap<>();
                this.mySoundPMap.put(1, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.envi_pro_100, 1)));
                this.mySoundPMap.put(2, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.envi_pro_200, 1)));
                this.mySoundPMap.put(3, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.envi_pro_500, 1)));
                this.mySoundPMap.put(4, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.envi_pro_1000, 1)));
                this.mySoundPMap.put(5, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.envi_pro_near, 1)));
                this.mySoundPMap.put(6, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.envi_pro_inside, 1)));
                this.mySoundPMap.put(7, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.envi_pro_non, 1)));
            }
            UserParam tempParam = PubVar._PubCommand.m_UserConfigDB.GetUserParam();
            HashMap<String, String> tempHashMap = tempParam.GetUserPara("Tag_System_AlarmNavig_AlarmDistance");
            if (!(tempHashMap == null || (tempStr3 = tempHashMap.get("F2")) == null)) {
                this.m_AlarmDistance = Double.parseDouble(tempStr3);
            }
            if (this.m_AlarmDistance <= 0.0d) {
                this.m_AlarmDistance = 1000.0d;
            }
            HashMap<String, String> tempHashMap2 = tempParam.GetUserPara("Tag_System_AlarmNavig_Layer");
            if (!(tempHashMap2 == null || (tempStr2 = tempHashMap2.get("F2")) == null)) {
                SetAlarmLayer(tempStr2);
            }
            HashMap<String, String> tempHashMap3 = tempParam.GetUserPara("Tag_System_AlarmNavig_AlarmGPSTime");
            if (tempHashMap3 != null && (tempStr = tempHashMap3.get("F2")) != null) {
                double tmpI = Double.parseDouble(tempStr);
                if (tmpI > 2.0d) {
                    SetGPSRefreshInterval((int) tmpI);
                }
            }
        } catch (Exception e) {
        }
    }

    public void SetQueryCoordinate(Coordinate coord) {
        this.m_IsStart = true;
        this.m_LastGPSCoord = coord;
        UpdateCoordinate(coord, true);
    }

    public void SetStartNavigation(boolean isStart) {
        if (isStart && !this.m_IsStart) {
            this.myGPSRecordHandler.post(this.myUpdateByGPSTask);
        }
        if (!isStart) {
            this.m_LastGPSCoord = null;
        }
        this.m_IsStart = isStart;
    }

    public void SetAlarmLayer(String alarmLayerID) {
        this.m_DataSet = PubVar.m_Workspace.GetDatasetByName(alarmLayerID);
        this.m_DataSet.QueryAllGeometryFromDB();
    }

    public void UpdateCoordinate(Coordinate coordinate, boolean notInsideAlarm) {
        boolean IsInside = false;
        try {
            if (this.m_IsStart && this.m_DataSet.getTotalCount() > 0 && coordinate != null) {
                int tmpNeedAlram = 0;
                double tmpMinD = Double.MAX_VALUE;
                Iterator<ExtraGeometry> tmpIter = this.m_DataSet.getGeometryList().iterator();
                while (true) {
                    if (!tmpIter.hasNext()) {
                        break;
                    }
                    AbstractGeometry tmpGeo = tmpIter.next().getGeometry();
                    if (tmpGeo instanceof Polygon) {
                        Polygon tmpPolygon = (Polygon) tmpGeo;
                        if (tmpPolygon.getEnvelope().ToPointDistance(coordinate) > this.m_AlarmDistance) {
                            continue;
                        } else if (tmpPolygon.IsContainPoint(coordinate, true)) {
                            this.m_NavigatePoint.SetCoordinate(coordinate);
                            tmpNeedAlram = 6;
                            break;
                        } else {
                            Coordinate tmpMinCoord = new Coordinate();
                            double tmpD = tmpPolygon.CalMinDistance(coordinate, tmpMinCoord);
                            if (tmpD <= this.m_AlarmDistance && tmpMinD > tmpD) {
                                tmpMinD = tmpD;
                                this.m_NavigatePoint.SetCoordinate(tmpMinCoord);
                                if (tmpMinD < 150.0d) {
                                    tmpNeedAlram = 1;
                                } else if (tmpMinD < 250.0d) {
                                    tmpNeedAlram = 2;
                                } else if (tmpMinD < 550.0d) {
                                    tmpNeedAlram = 3;
                                } else if (tmpMinD < 1000.0d) {
                                    tmpNeedAlram = 4;
                                } else {
                                    tmpNeedAlram = 5;
                                }
                            }
                        }
                    }
                }
                if (tmpNeedAlram > 0) {
                    playSound(tmpNeedAlram);
                    this.Is_Alarm = true;
                    IsInside = true;
                    PubVar._MapView.invalidate();
                } else {
                    this.Is_Alarm = false;
                }
                this.m_NavigatePoint.UpdateLocation(coordinate, this.m_AlarmDistance);
            }
        } catch (Exception e) {
        }
        if (notInsideAlarm && !IsInside) {
            playSound(7);
            PubVar._MapView.invalidate();
        }
    }

    public void UpdateGPSLocation() {
        try {
            if (PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                this.m_LastGPSCoord = PubVar._PubCommand.m_GpsLocation.getGPSCoordinate();
                if (this.m_LastGPSCoord != null) {
                    UpdateCoordinate(this.m_LastGPSCoord, false);
                    return;
                }
                return;
            }
            this.m_LastGPSCoord = null;
        } catch (Exception e) {
        }
    }

    public void SetGPSRefreshInterval(int time) {
        this.m_GPSRefreshInterval = (long) (time * easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX);
    }

    private Bitmap getMaskBitmap() {
        try {
            if (this._MaskBitmap == null) {
                this._MaskBitmap = Bitmap.createBitmap(PubVar._Map.getSize().getWidth(), PubVar._Map.getSize().getHeight(), Bitmap.Config.ARGB_4444);
            }
            this._MaskBitmap.eraseColor(0);
            Canvas paramCanvas = new Canvas(this._MaskBitmap);
            boolean tmpMayShowLine = false;
            if (this.m_LastGPSCoord != null) {
                tmpMayShowLine = true;
                PubVar._Map.MapToScreen(this.m_LastGPSCoord);
            }
            if (this.m_NavigatePoint != null) {
                Point tmpCenterPtn = PubVar._Map.MapToScreen(this.m_NavigatePoint._Coordinate);
                if (this.m_DrawLineBool && tmpMayShowLine) {
                    Coordinate tmpCoord01 = new Coordinate();
                    Coordinate tmpCoord02 = new Coordinate();
                    if (PubVar._Map.Clipline(this.m_LastGPSCoord, this.m_NavigatePoint._Coordinate, tmpCoord01, tmpCoord02)) {
                        Point tmpPtn01 = PubVar._Map.MapToScreen(tmpCoord01);
                        Point tmpPtn02 = PubVar._Map.MapToScreen(tmpCoord02);
                        if (!(tmpPtn01.x == tmpPtn02.x && tmpPtn01.y == tmpPtn02.y)) {
                            drawNavLine(paramCanvas, this.m_NavigatePoint, tmpPtn01, tmpPtn02);
                        }
                    }
                }
                if (this.m_DrawTargetBool && tmpCenterPtn.x >= 0 && tmpCenterPtn.x <= PubVar.ScreenWidth && tmpCenterPtn.y >= 0 && tmpCenterPtn.y <= PubVar.ScreenHeight) {
                    if (this.m_NavigatePoint._IsLocation) {
                        if (!this.m_NavigatePoint._HasDrawLocation) {
                            Paint tmpPaint = new Paint();
                            tmpPaint.setColor(-1761673216);
                            tmpPaint.setAntiAlias(true);
                            paramCanvas.drawCircle((float) tmpCenterPtn.x, (float) tmpCenterPtn.y, 3.0f * CricleRadius, tmpPaint);
                            this.m_NavigatePoint._HasDrawLocation = true;
                        } else {
                            this.m_NavigatePoint._HasDrawLocation = false;
                        }
                    }
                    paramCanvas.drawCircle((float) tmpCenterPtn.x, (float) tmpCenterPtn.y, CricleRadius, this._PointPaint);
                    paramCanvas.drawCircle((float) tmpCenterPtn.x, (float) tmpCenterPtn.y, CricleRadius, this._PointPaintCircle);
                    if (this.m_NavigatePoint._IsLocation) {
                        if (this.m_TargetLocICON == null) {
                            this.m_TargetLocICON = BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.navitarget0248);
                        }
                        if (this.m_TargetLocICON != null) {
                            int tmpWidth = this.m_TargetLocICON.getWidth();
                            paramCanvas.drawBitmap(this.m_TargetLocICON, (float) (tmpCenterPtn.x - (tmpWidth / 2)), (float) (tmpCenterPtn.y - tmpWidth), (Paint) null);
                        }
                    } else {
                        if (this.m_TargetICON == null) {
                            this.m_TargetICON = BitmapFactory.decodeResource(PubVar.MainContext.getResources(), R.drawable.navitarget0148);
                        }
                        if (this.m_TargetICON != null) {
                            int tmpWidth2 = this.m_TargetICON.getWidth();
                            paramCanvas.drawBitmap(this.m_TargetICON, (float) (tmpCenterPtn.x - (tmpWidth2 / 2)), (float) (tmpCenterPtn.y - tmpWidth2), (Paint) null);
                        }
                    }
                    if (!this.m_NavigatePoint._Name.equals("")) {
                        drawText(paramCanvas, this.m_NavigatePoint._Name, tmpCenterPtn);
                    }
                }
            }
        } catch (Exception e) {
        }
        return this._MaskBitmap;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas paramCanvas) {
        if (this.m_IsStart && this.m_DataSet != null && this.Is_Alarm) {
            paramCanvas.drawBitmap(getMaskBitmap(), 0.0f, 0.0f, (Paint) null);
        }
    }

    private Paint getNavLinePaint() {
        if (this._NavLinePaint == null) {
            this._NavLinePaint = new Paint();
            this._NavLinePaint.setStyle(Paint.Style.STROKE);
            this._NavLinePaint.setColor(-16776961);
            this._NavLinePaint.setAntiAlias(true);
            this._NavLinePaint.setStrokeWidth(3.0f * PubVar.ScaledDensity);
            this._NavLinePaint.setPathEffect(new DashPathEffect(new float[]{PubVar.ScaledDensity * 5.0f, PubVar.ScaledDensity * 5.0f}, 1.0f));
        }
        return this._NavLinePaint;
    }

    private void drawNavLine(Canvas paramCanvas, NavigatePoint navPtn, Point startPtn, Point endPtn) {
        paramCanvas.drawLine((float) startPtn.x, (float) startPtn.y, (float) endPtn.x, (float) endPtn.y, getNavLinePaint());
        String tempMsg = "【" + ((int) navPtn._Direction) + "°】";
        String tempMsg2 = Common.SimplifyLength(navPtn._Distance);
        double tmpAngle = (double) (navPtn._Direction - 90.0f);
        if (tmpAngle > 90.0d) {
            tmpAngle -= 180.0d;
        }
        double tmpAngle2 = Math.toRadians(tmpAngle);
        drawRotateText(paramCanvas, tempMsg2, (float) (((double) ((startPtn.x + endPtn.x) / 2)) + (((double) TipTextDisHeight) * Math.sin(tmpAngle2))), (float) (((double) ((startPtn.y + endPtn.y) / 2)) + (((double) (-TipTextDisHeight)) * Math.cos(tmpAngle2))), (float) tmpAngle);
        drawRotateText(paramCanvas, tempMsg, (float) (((double) ((startPtn.x + endPtn.x) / 2)) + (((double) TipTextDisHeight2) * Math.sin(tmpAngle2))), (float) (((double) ((startPtn.y + endPtn.y) / 2)) + (((double) (-TipTextDisHeight2)) * Math.cos(tmpAngle2))), (float) tmpAngle);
    }

    /* access modifiers changed from: package-private */
    public void drawRotateText(Canvas canvas, String text, float x, float y, Paint paint, float angle) {
        if (angle != 0.0f) {
            canvas.rotate(angle, x, y);
        }
        canvas.drawText(text, x, y, paint);
        if (angle != 0.0f) {
            canvas.rotate(-angle, x, y);
        }
    }

    /* access modifiers changed from: package-private */
    public void drawRotateText(Canvas paramCanvas, String content, float x, float y, float angle) {
        if (angle != 0.0f) {
            paramCanvas.rotate(angle, x, y);
        }
        paramCanvas.drawText(content, x - 1.0f, y, this._TipTextBgPaint);
        paramCanvas.drawText(content, 1.0f + x + 1.0f, y, this._TipTextBgPaint);
        paramCanvas.drawText(content, x, y - 1.0f, this._TipTextBgPaint);
        paramCanvas.drawText(content, x, 1.0f + y + 1.0f, this._TipTextBgPaint);
        paramCanvas.drawText(content, 1.0f + x, 1.0f + y, this._TipTextBgPaint);
        paramCanvas.drawText(content, x - 1.0f, y - 1.0f, this._TipTextBgPaint);
        paramCanvas.drawText(content, 1.0f + x, y - 1.0f, this._TipTextBgPaint);
        paramCanvas.drawText(content, x - 1.0f, 1.0f + y, this._TipTextBgPaint);
        paramCanvas.drawText(content, x, y, this._TipTextPaint);
        if (angle != 0.0f) {
            paramCanvas.rotate(-angle, x, y);
        }
    }

    private void drawText(Canvas paramCanvas, String content, Point point) {
        if (content != null) {
            try {
                if (!content.equals("")) {
                    float tempX = ((float) point.x) + TipTextSize;
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

    private void playSound(int index) {
        try {
            if (this.mySoundPool == null) {
                this.mySoundPool = new SoundPool(7, 1, 0);
                this.mySoundPMap = new HashMap<>();
                this.mySoundPMap.put(1, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.envi_pro_100, 1)));
                this.mySoundPMap.put(2, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.envi_pro_200, 1)));
                this.mySoundPMap.put(3, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.envi_pro_500, 1)));
                this.mySoundPMap.put(4, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.envi_pro_1000, 1)));
                this.mySoundPMap.put(5, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.envi_pro_near, 1)));
                this.mySoundPMap.put(6, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.envi_pro_inside, 1)));
                this.mySoundPMap.put(7, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.envi_pro_non, 1)));
            }
            this.mySoundPool.play(this.mySoundPMap.get(Integer.valueOf(index)).intValue(), 1.0f, 1.0f, 0, 0, 1.0f);
            if (this.myVibrator == null) {
                this.myVibrator = (Vibrator) PubVar.MainContext.getSystemService("vibrator");
            }
            this.myVibrator.vibrate(1000);
        } catch (Exception e) {
        }
    }

    public void SetAlarmDistance(double alarmDistance) {
        this.m_AlarmDistance = alarmDistance;
    }
}
