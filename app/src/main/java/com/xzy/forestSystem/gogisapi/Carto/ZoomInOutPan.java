package  com.xzy.forestSystem.gogisapi.Carto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;

public class ZoomInOutPan implements ICommand, IOnPaint {
    boolean FirstMouseMove;
    public boolean IsDoubleClick;
    public boolean IsMove;
    public boolean IsSingleClick;
    Bitmap MaskImage;
    long _FirstClickTime;
    Pan _Pan;
    boolean _PanScreen;
    protected MapView _mapView;
    Paint brush;
    private ICallback m_Callback;
    double m_FromDist;
    PointF m_ScaleStartPoint;
    double m_ToDist;
    RectF m_ToRect;

    public ZoomInOutPan() {
        this.FirstMouseMove = true;
        this.MaskImage = null;
        this._mapView = null;
        this._Pan = null;
        this._PanScreen = false;
        this.m_FromDist = 0.0d;
        this.m_ScaleStartPoint = null;
        this.m_ToDist = 0.0d;
        this.m_ToRect = null;
        this.brush = new Paint(0);
        this._FirstClickTime = 0;
        this.IsSingleClick = false;
        this.IsDoubleClick = false;
        this.IsMove = false;
        this.m_Callback = null;
        this.brush = new Paint(0);
        this.brush.setColor(PubVar.MapBgColor);
        this.brush.setStyle(Paint.Style.FILL);
    }

    public ZoomInOutPan(MapView parammapView) {
        this.FirstMouseMove = true;
        this.MaskImage = null;
        this._mapView = null;
        this._Pan = null;
        this._PanScreen = false;
        this.m_FromDist = 0.0d;
        this.m_ScaleStartPoint = null;
        this.m_ToDist = 0.0d;
        this.m_ToRect = null;
        this.brush = new Paint(0);
        this._FirstClickTime = 0;
        this.IsSingleClick = false;
        this.IsDoubleClick = false;
        this.IsMove = false;
        this.m_Callback = null;
        this._mapView = parammapView;
        this._Pan = new Pan(parammapView);
        this.brush = new Paint(0);
        this.brush.setColor(PubVar.MapBgColor);
        this.brush.setStyle(Paint.Style.FILL);
    }

    public void SetCallback(ICallback pCallback) {
        this.m_Callback = pCallback;
        this._Pan.SetCallback(pCallback);
    }

    public void SetMapView(MapView parammapView) {
        this._mapView = parammapView;
        this._Pan = new Pan(parammapView);
    }

    public MapView getMapView() {
        return this._mapView;
    }

    private double GetScaleDist(MotionEvent paramMotionEvent) {
        float x1 = paramMotionEvent.getX(0);
        float y1 = paramMotionEvent.getY(0);
        float x2 = paramMotionEvent.getX(1);
        float y2 = paramMotionEvent.getY(1);
        return Math.sqrt((double) (((x1 - x2) * (x1 - x2)) + ((y1 - y2) * (y1 - y2))));
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseDown(MotionEvent paramMotionEvent) {
        try {
            this.IsMove = false;
            this.IsDoubleClick = false;
            this.IsSingleClick = false;
            this.FirstMouseMove = true;
            this._PanScreen = false;
            this.m_ToDist = 0.0d;
            if (paramMotionEvent.getPointerCount() > 1) {
                this._PanScreen = false;
                this._mapView.getMap().setAllowRefreshMap(false);
                this.MaskImage = this._mapView.getMap().getMaskBitmap();
                this.m_FromDist = GetScaleDist(paramMotionEvent);
                this.m_ToRect = null;
                this.m_ScaleStartPoint = new PointF((paramMotionEvent.getX(0) + paramMotionEvent.getX(1)) / 2.0f, (paramMotionEvent.getY(0) + paramMotionEvent.getY(1)) / 2.0f);
                this._FirstClickTime = System.currentTimeMillis();
                return;
            }
            long tmpTimeLong = System.currentTimeMillis();
            if (tmpTimeLong - this._FirstClickTime < 200) {
                Coordinate tmpCoord = this._mapView.getMap().ScreenToMap(new PointF(paramMotionEvent.getX(), paramMotionEvent.getY()));
                this._mapView.getMap().ZoomToCenterAndScale(tmpCoord.getX(), tmpCoord.getY(), 0.5f);
                this.IsDoubleClick = true;
            } else {
                this._Pan.MouseDown(paramMotionEvent);
                this._PanScreen = true;
            }
            this._FirstClickTime = tmpTimeLong;
        } catch (Exception e) {
            this._FirstClickTime = System.currentTimeMillis();
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseMove(MotionEvent paramMotionEvent) {
        try {
            if (paramMotionEvent.getPointerCount() <= 1) {
                this._Pan.MouseMove(paramMotionEvent);
                this.FirstMouseMove = false;
            } else if (!this._PanScreen) {
                this.m_ToDist = GetScaleDist(paramMotionEvent);
                if (this.m_FromDist != 0.0d && this.m_ToDist != 0.0d) {
                    double f1 = this.m_ToDist / this.m_FromDist;
                    if (this.MaskImage != null) {
                        float tmpW = ((float) f1) * ((float) this.MaskImage.getWidth());
                        float tmpH = ((float) f1) * ((float) this.MaskImage.getHeight());
                        float tmpX = ((1.0f - ((float) f1)) * this.m_ScaleStartPoint.x) + (this._mapView.getMap().MaskBiasX * ((float) f1));
                        float tmpY = ((1.0f - ((float) f1)) * this.m_ScaleStartPoint.y) + (this._mapView.getMap().MaskBiasY * ((float) f1));
                        this.m_ToRect = new RectF(tmpX, tmpY, tmpX + tmpW, tmpY + tmpH);
                    }
                    if (PubVar._MapView.m_ShutterTool.getIsShutterMode()) {
                        this._mapView.getMap().RefreshFast2();
                    } else {
                        this._mapView.invalidate();
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseUp(MotionEvent paramMotionEvent) {
        this._mapView.getMap().setAllowRefreshMap(true);
        this._mapView.setAllowRefreshMap(true);
        try {
            int tempPCount = paramMotionEvent.getPointerCount();
            if (tempPCount <= 1 && this._PanScreen) {
                this._Pan.MouseUp(paramMotionEvent);
                this.IsMove = this._Pan.IsMove;
                if (!this._Pan.IsMove) {
                    this.IsSingleClick = true;
                }
            }
            if (tempPCount > 1 && !this._PanScreen) {
                this.IsMove = true;
                if (this.MaskImage != null) {
                    this.MaskImage = null;
                }
                if (!(this.m_FromDist == 0.0d || this.m_ToDist == 0.0d)) {
                    double f = this.m_FromDist / this.m_ToDist;
                    if (f <= 1.0d || this._mapView.getMap().getActualScale() <= 527012278) {
                        Coordinate localCoordinate1 = this._mapView.getMap().ScreenToMap(this.m_ScaleStartPoint);
                        this._mapView.getMap().setExtend(this._mapView.getMap().getExtend().Scale(f));
                        Coordinate localCoordinate2 = PubVar._Map.ScreenToMap(this.m_ScaleStartPoint);
                        this._mapView.getMap().getCenter().setX(this._mapView.getMap().getCenter().getX() - (localCoordinate2.getX() - localCoordinate1.getX()));
                        this._mapView.getMap().getCenter().setY(this._mapView.getMap().getCenter().getY() - (localCoordinate2.getY() - localCoordinate1.getY()));
                        this._mapView.getMap().CalExtend();
                        this._mapView.getMap().setAllowRefreshMap(true);
                        this._mapView.getMap().Refresh();
                    } else {
                        Common.ShowToast("不能再缩小地图了.");
                        return;
                    }
                }
            }
        } catch (Exception e) {
        }
        this._mapView.getMap().setAllowRefreshMap(true);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas paramCanvas) {
        try {
            if (this._PanScreen) {
                this._Pan.OnPaint(paramCanvas);
            }
            if (this.MaskImage != null && this.m_ToRect != null) {
                this.brush.setColor(PubVar.MapBgColor);
                paramCanvas.drawRect(0.0f, 0.0f, (float) this.MaskImage.getWidth(), (float) this.MaskImage.getHeight(), this.brush);
                paramCanvas.drawBitmap(this.MaskImage, new Rect(0, 0, this.MaskImage.getWidth(), this.MaskImage.getHeight()), this.m_ToRect, this.brush);
                if (this._mapView.m_ShutterTool.getIsShutterMode()) {
                    this._mapView.m_ShutterTool.OnPaint(paramCanvas);
                }
                if (PubVar.m_MapCompassVisible) {
                    this._mapView.m_MapCompassPaint.OnPaint(paramCanvas);
                }
            }
        } catch (Exception e) {
        }
    }
}
