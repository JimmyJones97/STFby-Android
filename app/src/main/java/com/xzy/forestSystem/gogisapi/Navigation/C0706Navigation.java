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
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import com.stczh.gzforestSystem.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* renamed from:  com.xzy.forestSystem.gogisapi.Navigation.Navigation */
public class C0706Navigation implements IOnPaint {
    private static final float CricleRadius = (4.0f * PubVar.ScaledDensity);
    static final float TipTextDisHeight = (5.0f * PubVar.ScaledDensity);
    static final float TipTextDisHeight2 = (-15.0f * PubVar.ScaledDensity);
    static final float TipTextSize = (14.0f * PubVar.ScaledDensity);
    private static final float lineWidth = (2.0f * PubVar.ScaledDensity);
    private Bitmap _MaskBitmap;
    private Paint _NavLinePaint;
    private Paint _PointPaint;
    private Paint _PointPaintCircle;
    private Paint _TextBgPaint;
    private Paint _TextPaint;
    private Paint _TipTextBgPaint;
    private Paint _TipTextPaint;
    double m_AlarmDistance;
    boolean m_AlarmDistanceBool;
    boolean m_DrawLineBool;
    boolean m_DrawTargetBool;
    private boolean m_IsStart;
    private Coordinate m_LastGPSCoord;
    private List<NavigatePoint> m_NavPoints;
    private Bitmap m_TargetICON;
    private Bitmap m_TargetLocICON;
    private Handler myGPSRecordHandler;
    private HashMap<Integer, Integer> mySoundPMap;
    private SoundPool mySoundPool;
    private Runnable myUpdateByGPSTask;
    private Vibrator myVibrator;

    public C0706Navigation() {
        this.m_IsStart = false;
        this.m_NavPoints = new ArrayList();
        this.m_DrawLineBool = true;
        this.m_DrawTargetBool = true;
        this.m_AlarmDistanceBool = true;
        this.m_AlarmDistance = 1000.0d;
        this._TextPaint = null;
        this._TextBgPaint = null;
        this._TipTextPaint = null;
        this._TipTextBgPaint = null;
        this.m_TargetICON = null;
        this.m_TargetLocICON = null;
        this.myGPSRecordHandler = new Handler();
        this.m_LastGPSCoord = null;
        this._PointPaint = null;
        this._PointPaintCircle = null;
        this.mySoundPool = null;
        this.mySoundPMap = null;
        this.myUpdateByGPSTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.Navigation.Navigation.1
            @Override // java.lang.Runnable
            public void run() {
                int tmpNeedAlram2;
                if (C0706Navigation.this.m_IsStart) {
                    int tmpNeedAlram = 0;
                    try {
                        for (NavigatePoint tmpNavPtn : C0706Navigation.this.m_NavPoints) {
                            if (C0706Navigation.this.m_AlarmDistanceBool && tmpNavPtn._Distance != 0.0d && C0706Navigation.this.m_AlarmDistance >= tmpNavPtn._Distance) {
                                if (tmpNavPtn._Distance < C0706Navigation.this.m_AlarmDistance / 4.0d) {
                                    tmpNeedAlram2 = 3;
                                } else if (tmpNavPtn._Distance < C0706Navigation.this.m_AlarmDistance / 2.0d) {
                                    tmpNeedAlram2 = 2;
                                } else {
                                    tmpNeedAlram2 = 1;
                                }
                                if (tmpNeedAlram2 > tmpNeedAlram) {
                                    tmpNeedAlram = tmpNeedAlram2;
                                }
                            }
                        }
                        if (tmpNeedAlram > 0) {
                            C0706Navigation.this.playSound(tmpNeedAlram);
                        }
                    } catch (Exception e) {
                    }
                    C0706Navigation.this.myGPSRecordHandler.postDelayed(C0706Navigation.this.myUpdateByGPSTask, 5000);
                }
            }
        };
        this._MaskBitmap = null;
        this._NavLinePaint = null;
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
        this._PointPaint = new Paint();
        this._PointPaint.setColor(-2130706688);
        this._PointPaint.setAntiAlias(true);
        this._PointPaintCircle = new Paint();
        this._PointPaintCircle.setStyle(Paint.Style.STROKE);
        this._PointPaintCircle.setStrokeWidth(lineWidth);
        this._PointPaintCircle.setColor(-16711936);
        this._PointPaintCircle.setAntiAlias(true);
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

    public void AddNavigationPoint(NavigatePoint point) {
        this.m_NavPoints.add(point);
    }

    public List<NavigatePoint> getNavPoints() {
        return this.m_NavPoints;
    }

    public int getNavPointCount() {
        return this.m_NavPoints.size();
    }

    public void Clear() {
        this.m_NavPoints.clear();
        PubVar._MapView.invalidate();
    }

    public void UpdateGPSLocation() {
        try {
            if (this.m_IsStart && this.m_NavPoints.size() > 0) {
                if (PubVar._PubCommand.m_GpsLocation.IsLocation()) {
                    this.m_LastGPSCoord = PubVar._PubCommand.m_GpsLocation.getGPSCoordinate();
                    if (this.m_LastGPSCoord != null) {
                        for (NavigatePoint tmpNavPtn : this.m_NavPoints) {
                            tmpNavPtn.UpdateLocation(this.m_LastGPSCoord, PubVar._PubCommand.m_Navigation.m_AlarmDistance);
                        }
                        return;
                    }
                    return;
                }
                for (NavigatePoint tmpNavPtn2 : this.m_NavPoints) {
                    tmpNavPtn2._IsLocation = false;
                }
                this.m_LastGPSCoord = null;
            }
        } catch (Exception e) {
        }
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
            for (NavigatePoint tmpNavPtn : this.m_NavPoints) {
                Point tmpCenterPtn = PubVar._Map.MapToScreen(tmpNavPtn._Coordinate);
                if (this.m_DrawLineBool && tmpMayShowLine) {
                    Coordinate tmpCoord01 = new Coordinate();
                    Coordinate tmpCoord02 = new Coordinate();
                    if (PubVar._Map.Clipline(this.m_LastGPSCoord, tmpNavPtn._Coordinate, tmpCoord01, tmpCoord02)) {
                        Point tmpPtn01 = PubVar._Map.MapToScreen(tmpCoord01);
                        Point tmpPtn02 = PubVar._Map.MapToScreen(tmpCoord02);
                        if (!(tmpPtn01.x == tmpPtn02.x && tmpPtn01.y == tmpPtn02.y)) {
                            drawNavLine(paramCanvas, tmpNavPtn, tmpPtn01, tmpPtn02);
                        }
                    }
                }
                if (this.m_DrawTargetBool && tmpCenterPtn.x >= 0 && tmpCenterPtn.x <= PubVar.ScreenWidth && tmpCenterPtn.y >= 0 && tmpCenterPtn.y <= PubVar.ScreenHeight) {
                    if (tmpNavPtn._IsLocation && this.m_AlarmDistanceBool) {
                        if (!tmpNavPtn._HasDrawLocation) {
                            Paint tmpPaint = new Paint();
                            tmpPaint.setColor(-1761673216);
                            tmpPaint.setAntiAlias(true);
                            paramCanvas.drawCircle((float) tmpCenterPtn.x, (float) tmpCenterPtn.y, 3.0f * CricleRadius, tmpPaint);
                            tmpNavPtn._HasDrawLocation = true;
                        } else {
                            tmpNavPtn._HasDrawLocation = false;
                        }
                    }
                    paramCanvas.drawCircle((float) tmpCenterPtn.x, (float) tmpCenterPtn.y, CricleRadius, this._PointPaint);
                    paramCanvas.drawCircle((float) tmpCenterPtn.x, (float) tmpCenterPtn.y, CricleRadius, this._PointPaintCircle);
                    if (tmpNavPtn._IsLocation) {
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
                    if (!tmpNavPtn._Name.equals("")) {
                        drawText(paramCanvas, tmpNavPtn._Name, tmpCenterPtn);
                    }
                }
            }
        } catch (Exception e) {
        }
        return this._MaskBitmap;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas paramCanvas) {
        if (this.m_IsStart && this.m_NavPoints.size() > 0) {
            paramCanvas.drawBitmap(getMaskBitmap(), 0.0f, 0.0f, (Paint) null);
        }
    }

    private Paint getNavLinePaint() {
        if (this._NavLinePaint == null) {
            this._NavLinePaint = new Paint();
            this._NavLinePaint.setStyle(Paint.Style.STROKE);
            this._NavLinePaint.setColor(SupportMenu.CATEGORY_MASK);
            this._NavLinePaint.setAntiAlias(true);
            this._NavLinePaint.setStrokeWidth(3.0f * PubVar.ScaledDensity);
            this._NavLinePaint.setPathEffect(new DashPathEffect(new float[]{PubVar.ScaledDensity * 5.0f, PubVar.ScaledDensity * 5.0f}, 1.0f));
        }
        return this._NavLinePaint;
    }

    private void drawNavLine(Canvas paramCanvas, NavigatePoint navPtn, Point startPtn, Point endPtn) {
        paramCanvas.drawLine((float) startPtn.x, (float) startPtn.y, (float) endPtn.x, (float) endPtn.y, getNavLinePaint());
        String tempMsg = String.valueOf(Common.SimplifyLength(navPtn._Distance)) + " 【" + ((int) navPtn._Direction) + "°】";
        double tmpAngle = (double) (navPtn._Direction - 90.0f);
        if (tmpAngle > 90.0d) {
            tmpAngle -= 180.0d;
        }
        double tmpAngle2 = Math.toRadians(tmpAngle);
        if (!navPtn._Name.equals("")) {
            drawRotateText(paramCanvas, navPtn._Name, (float) (((double) ((startPtn.x + endPtn.x) / 2)) + (((double) TipTextDisHeight) * Math.sin(tmpAngle2))), (float) (((double) ((startPtn.y + endPtn.y) / 2)) + (((double) (-TipTextDisHeight)) * Math.cos(tmpAngle2))), (float) tmpAngle);
        }
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

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void playSound(int index) {
        try {
            if (this.mySoundPool == null) {
                this.mySoundPool = new SoundPool(3, 1, 0);
                this.mySoundPMap = new HashMap<>();
                this.mySoundPMap.put(1, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.bbmsg, 1)));
                this.mySoundPMap.put(2, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.bbmsg2, 1)));
                this.mySoundPMap.put(3, Integer.valueOf(this.mySoundPool.load(PubVar.MainContext, R.raw.bbmsg3, 1)));
            }
            this.mySoundPool.play(this.mySoundPMap.get(Integer.valueOf(index)).intValue(), 1.0f, 1.0f, 0, 0, 1.0f);
            if (this.myVibrator == null) {
                this.myVibrator = (Vibrator) PubVar.MainContext.getSystemService("vibrator");
            }
            this.myVibrator.vibrate((long) (index * easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX));
        } catch (Exception e) {
        }
    }
}
