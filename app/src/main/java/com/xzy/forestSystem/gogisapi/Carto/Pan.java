package  com.xzy.forestSystem.gogisapi.Carto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;

public class Pan implements ICommand, IOnPaint {
    float DeltX;
    float DeltY;
    Integer GGdc;

    /* renamed from: H1 */
    float f449H1;

    /* renamed from: H2 */
    float f450H2;
    public boolean IsMove = false;
    Bitmap MaskImage = null;
    Bitmap MaskImage2 = null;

    /* renamed from: W1 */
    float f451W1;

    /* renamed from: W2 */
    float f452W2;

    /* renamed from: X1 */
    float f453X1;

    /* renamed from: X2 */
    float f454X2;

    /* renamed from: Y1 */
    float f455Y1;

    /* renamed from: Y2 */
    float f456Y2;
    long _FirstClickTime = 0;
    boolean _MouseDown = false;
    private boolean _SelectMode = true;
    private MapView _mapView;
    Paint brush = new Paint(0);

    /* renamed from: g2 */
    Canvas f457g2;
    private ICallback m_Callback = null;
    private PointF m_MoveStart;

    public Pan() {
        this.brush.setColor(PubVar.MapBgColor);
        this.brush.setStyle(Paint.Style.FILL);
    }

    public Pan(MapView parammapView) {
        this._mapView = parammapView;
        this.brush.setColor(PubVar.MapBgColor);
        this.brush.setStyle(Paint.Style.FILL);
    }

    public void SetMapView(MapView parammapView) {
        this._mapView = parammapView;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseDown(MotionEvent paramMotionEvent) {
        this.IsMove = false;
        this._FirstClickTime = System.currentTimeMillis();
        try {
            this._mapView.getMap().setAllowRefreshMap(false);
            this._mapView.TrackingRectangle = null;
            this.m_MoveStart = new PointF(paramMotionEvent.getX(), paramMotionEvent.getY());
            this.MaskImage = this._mapView.getMap().getMaskBitmap();
            this.MaskImage2 = this._mapView.getMap().getRasterBitmap();
            this._MouseDown = true;
        } catch (Exception ex) {
            Common.Log("MouseDown", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseMove(MotionEvent paramMotionEvent) {
        try {
            if (this.MaskImage != null) {
                this.DeltX = paramMotionEvent.getX() - this.m_MoveStart.x;
                this.DeltY = paramMotionEvent.getY() - this.m_MoveStart.y;
                float f1 = (float) this._mapView.getWidth();
                float f2 = (float) this._mapView.getHeight();
                if (!(this.DeltX == 0.0f || this.DeltY == 0.0f)) {
                    if (this.DeltX < 0.0f) {
                        if (this.DeltY < 0.0f) {
                            this.f453X1 = 0.0f;
                            this.f455Y1 = this.DeltY + f2;
                            this.f451W1 = f1;
                            this.f449H1 = -this.DeltY;
                            this.f454X2 = this.DeltX + f1;
                            this.f456Y2 = 0.0f;
                            this.f452W2 = -this.DeltX;
                        } else {
                            this.f453X1 = 0.0f;
                            this.f455Y1 = 0.0f;
                            this.f451W1 = f1;
                            this.f449H1 = this.DeltY;
                            this.f454X2 = this.DeltX + f1;
                            this.f456Y2 = 0.0f;
                            this.f452W2 = -this.DeltX;
                            this.f450H2 = f2;
                        }
                    } else if (this.DeltY < 0.0f) {
                        this.f453X1 = 0.0f;
                        this.f455Y1 = 0.0f;
                        this.f451W1 = this.DeltX;
                        this.f449H1 = f2;
                        this.f454X2 = 0.0f;
                        this.f456Y2 = this.DeltY + f2;
                        this.f452W2 = f1;
                        this.f450H2 = -this.DeltY;
                    } else {
                        this.f453X1 = 0.0f;
                        this.f455Y1 = 0.0f;
                        this.f451W1 = this.DeltX;
                        this.f449H1 = f2;
                        this.f454X2 = 0.0f;
                        this.f456Y2 = 0.0f;
                        this.f452W2 = f1;
                    }
                }
                this._mapView.invalidate();
            }
        } catch (Exception ex) {
            Common.Log("MouseMove", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseUp(MotionEvent paramMotionEvent) {
        this._mapView.getMap().setAllowRefreshMap(true);
        this._mapView.setAllowRefreshMap(true);
        try {
            if (this.f457g2 != null) {
                this.f457g2 = null;
            }
            if (this.MaskImage != null) {
                this.MaskImage = null;
            }
            if (this._MouseDown) {
                if (Math.abs(paramMotionEvent.getX() - this.m_MoveStart.x) > 10.0f || Math.abs(paramMotionEvent.getY() - this.m_MoveStart.y) > 10.0f) {
                    this.IsMove = true;
                    Coordinate localCoordinate1 = this._mapView.getMap().ScreenToMap(this.m_MoveStart);
                    Coordinate localCoordinate2 = this._mapView.getMap().ScreenToMap(paramMotionEvent.getX(), paramMotionEvent.getY());
                    SetNewCenter(new Coordinate(this._mapView.getMap().getCenter().getX() - (localCoordinate2.getX() - localCoordinate1.getX()), this._mapView.getMap().getCenter().getY() - (localCoordinate2.getY() - localCoordinate1.getY())));
                } else {
                    long tmpUpTime = System.currentTimeMillis();
                    if (tmpUpTime - this._FirstClickTime > 1000) {
                        if (this.m_Callback != null) {
                            this.m_Callback.OnClick("MouseLongClickReturn", paramMotionEvent);
                        }
                        this._FirstClickTime = tmpUpTime;
                    }
                }
                this._MouseDown = false;
            }
        } catch (Exception ex) {
            Common.Log("MouseUp", "错误:" + ex.toString() + "-->" + ex.getMessage());
        } finally {
            this._mapView.invalidate();
            System.gc();
        }
        this._FirstClickTime = System.currentTimeMillis();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas paramCanvas) {
        try {
            if (this.MaskImage != null) {
                this.brush.setColor(PubVar.MapBgColor);
                paramCanvas.drawRect(0.0f, 0.0f, (float) this.MaskImage.getWidth(), (float) this.MaskImage.getHeight(), this.brush);
                paramCanvas.drawBitmap(this.MaskImage, this.DeltX + this._mapView.getMap().MaskBiasX, this.DeltY + this._mapView.getMap().MaskBiasY, new Paint());
                if (PubVar._MapView.m_ShutterTool.getIsShutterMode()) {
                    PubVar._MapView.m_ShutterTool.OnPaint(paramCanvas);
                }
                if (PubVar.m_MapCompassVisible) {
                    PubVar._MapView.m_MapCompassPaint.OnPaint(paramCanvas);
                }
            }
        } catch (Exception ex) {
            Common.Log("OnPaint", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    public void SetNewCenter(Coordinate tmpCoord) {
        if (tmpCoord != null) {
            this._mapView.getMap().getCenter().setX(tmpCoord.getX());
            this._mapView.getMap().getCenter().setY(tmpCoord.getY());
            this._mapView.getMap().CalExtend();
            this._mapView.getMap().Refresh();
        }
    }

    public void SetCallback(ICallback pCallback) {
        this.m_Callback = pCallback;
    }
}
