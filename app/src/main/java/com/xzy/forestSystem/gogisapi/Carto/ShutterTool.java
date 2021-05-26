package  com.xzy.forestSystem.gogisapi.Carto;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer;
import java.util.HashMap;
import java.util.Iterator;

public class ShutterTool implements ICommand, IOnPaint {
    static final int DrawLineColor = -65536;
    static final int DrawLineColor2 = -256;
    static final int DrawLineWidth = ((int) (6.0f * PubVar.ScaledDensity));
    private boolean IsShutterMode = false;
    private Paint _linePaint = null;
    private Paint _linePaint2;
    private MapView _mapView;
    private Rect m_ClipRect = new Rect(0, 0, 0, 0);
    private float m_CurrentX = 0.0f;
    private float m_CurrentY = 0.0f;
    private float m_LastX = 0.0f;
    private float m_LastY = 0.0f;
    public String m_SelectLayers = "";
    private boolean m_isHorizontal = false;

    public ShutterTool(MapView parammapView) {
        this._mapView = parammapView;
        this._linePaint = new Paint();
        this._linePaint.setStyle(Paint.Style.STROKE);
        this._linePaint.setColor(-65536);
        this._linePaint.setStrokeWidth((float) DrawLineWidth);
        this._linePaint.setAntiAlias(true);
        this._linePaint2 = new Paint();
        this._linePaint2.setStyle(Paint.Style.STROKE);
        this._linePaint2.setColor(DrawLineColor2);
        this._linePaint2.setStrokeWidth((float) (DrawLineWidth / 3));
        this._linePaint2.setAntiAlias(true);
    }

    public void SetIsShutterMode(boolean isShutterMode) {
        String tempStr;
        if (isShutterMode) {
            try {
                if (this.IsShutterMode != isShutterMode) {
                    this.IsShutterMode = true;
                    HashMap<String, String> tempHashMap = PubVar._PubCommand.m_ProjectDB.GetProjectConfigDB().GetUserPara("卷帘图层");
                    if (!(tempHashMap == null || (tempStr = tempHashMap.get("F1")) == null || tempStr.equals(""))) {
                        PubVar._MapView.m_ShutterTool.m_SelectLayers = tempStr;
                    }
                    if (this.m_SelectLayers.equals("") && PubVar._Map.getRasterLayers().size() > 1) {
                        Iterator<XLayer> tempIter = PubVar._Map.getRasterLayers().iterator();
                        tempIter.next();
                        while (true) {
                            if (tempIter.hasNext()) {
                                XLayer tmpLayer = tempIter.next();
                                if (tmpLayer.getVisible()) {
                                    this.m_SelectLayers = tmpLayer.getName();
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                    if (!this.m_SelectLayers.equals("")) {
                        PubVar._Map.RefreshRasterLayers();
                        PubVar._Map.RefreshFast();
                    }
                }
            } catch (Exception e) {
            }
        }
        this.IsShutterMode = isShutterMode;
    }

    public boolean getIsShutterMode() {
        return this.IsShutterMode;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.IOnPaint
    public void OnPaint(Canvas canvas) {
        if (this.m_ClipRect.width() > 0 && this.m_ClipRect.height() > 0) {
            if (this.m_isHorizontal) {
                canvas.drawLine(0.0f, this.m_CurrentY, (float) this._mapView.getWidth(), this.m_CurrentY, this._linePaint);
                canvas.drawLine(0.0f, this.m_CurrentY, (float) this._mapView.getWidth(), this.m_CurrentY, this._linePaint2);
                return;
            }
            canvas.drawLine(this.m_CurrentX, 0.0f, this.m_CurrentX, (float) this._mapView.getHeight(), this._linePaint);
            canvas.drawLine(this.m_CurrentX, 0.0f, this.m_CurrentX, (float) this._mapView.getHeight(), this._linePaint2);
        }
    }

    public void RefreshMap(Canvas canvas) {
        try {
            if (this.m_ClipRect.width() > 0 && this.m_ClipRect.height() > 0) {
                Bitmap tmpBmp = PubVar._Map.getShutterRasterBitmap();
                if (tmpBmp != null) {
                    canvas.clipRect(this.m_ClipRect);
                    canvas.drawBitmap(tmpBmp, this._mapView.getMap().MaskBiasX, this._mapView.getMap().MaskBiasY, (Paint) null);
                }
                Bitmap tmpBmp2 = PubVar._Map.getShutterBitmap();
                if (tmpBmp2 != null) {
                    canvas.clipRect(this.m_ClipRect);
                    canvas.drawBitmap(tmpBmp2, 0.0f, 0.0f, (Paint) null);
                }
                if (this.m_isHorizontal) {
                    canvas.drawLine(0.0f, this.m_CurrentY, (float) this._mapView.getWidth(), this.m_CurrentY, this._linePaint);
                    canvas.drawLine(0.0f, this.m_CurrentY, (float) this._mapView.getWidth(), this.m_CurrentY, this._linePaint2);
                    return;
                }
                canvas.drawLine(this.m_CurrentX, 0.0f, this.m_CurrentX, (float) this._mapView.getHeight(), this._linePaint);
                canvas.drawLine(this.m_CurrentX, 0.0f, this.m_CurrentX, (float) this._mapView.getHeight(), this._linePaint2);
            }
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseDown(MotionEvent paramMotionEvent) {
        this.m_LastX = paramMotionEvent.getX();
        this.m_LastY = paramMotionEvent.getY();
        this.m_CurrentX = paramMotionEvent.getX();
        this.m_CurrentY = paramMotionEvent.getY();
        this._mapView.invalidate();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseMove(MotionEvent paramMotionEvent) {
        try {
            this.m_CurrentX = paramMotionEvent.getX();
            this.m_CurrentY = paramMotionEvent.getY();
            if (Math.abs(this.m_CurrentY - this.m_LastY) > Math.abs(this.m_CurrentX - this.m_LastX)) {
                this.m_isHorizontal = true;
            } else {
                this.m_isHorizontal = false;
            }
            if (this.m_isHorizontal) {
                if (this.m_LastY < this.m_CurrentY) {
                    this.m_ClipRect = new Rect(0, 0, this._mapView.getWidth(), (int) this.m_CurrentY);
                } else {
                    this.m_ClipRect = new Rect(0, (int) this.m_CurrentY, this._mapView.getWidth(), this._mapView.getHeight());
                }
            } else if (this.m_LastX < this.m_CurrentX) {
                this.m_ClipRect = new Rect(0, 0, (int) this.m_CurrentX, this._mapView.getHeight());
            } else {
                this.m_ClipRect = new Rect((int) this.m_CurrentX, 0, this._mapView.getWidth(), this._mapView.getHeight());
            }
            PubVar._Map.RefreshFast2();
        } catch (Exception e) {
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.ICommand
    public void MouseUp(MotionEvent paramMotionEvent) {
    }
}
