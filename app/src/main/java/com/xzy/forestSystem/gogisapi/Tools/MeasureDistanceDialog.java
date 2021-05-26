package  com.xzy.forestSystem.gogisapi.Tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import com.stczh.gzforestSystem.R;
import com.xzy.forestSystem.hellocharts.animation.ChartViewportAnimator;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;

public class MeasureDistanceDialog implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private int LastFileLen;
    private long LastSendTime;
    private int VideoFormatIndex;
    private int VideoHeight;
    private int VideoPreRate;
    private int VideoQuality;
    private int VideoWidth;
    private XDialogTemplate _Dialog;
    private Canvas canvas;
    private float currentXAngle;
    private float currentYAngle;
    private float currentZAngle;
    private EditText et_DeviceHeight;
    private EditText et_measureDistance;
    private boolean isCalWidth;
    private boolean isCheckLight;
    private Camera mCamera;
    private DecimalFormat mDecimalFormat;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceView mSurfaceview;
    private ICallback m_Callback;
    private Context m_Context;
    private Button myBtn01;
    private Button myBtn02;
    private Rect myImageRec;
    final SensorEventListener mySensorListener;
    private ICallback pCallback;

    /* renamed from: sm */
    private SensorManager f557sm;
    private float startXAngle;
    private float startZAngle;
    private int tempPreRate;
    private TextView tipTextView;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    @SuppressLint("WrongConstant")
    public MeasureDistanceDialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.mDecimalFormat = new DecimalFormat("0.00");
        this.mSurfaceview = null;
        this.mSurfaceHolder = null;
        this.mCamera = null;
        this.tipTextView = null;
        this.et_DeviceHeight = null;
        this.et_measureDistance = null;
        this.isCalWidth = false;
        this.startXAngle = 0.0f;
        this.startZAngle = 0.0f;
        this.currentXAngle = 0.0f;
        this.currentYAngle = 0.0f;
        this.currentZAngle = 0.0f;
        this.VideoWidth = 320;
        this.VideoHeight = 240;
        this.VideoFormatIndex = 0;
        this.myImageRec = new Rect(0, 0, 400, ChartViewportAnimator.FAST_ANIMATION_DURATION);
        this.VideoPreRate = 5;
        this.tempPreRate = 0;
        this.VideoQuality = 85;
        this.LastFileLen = 0;
        this.LastSendTime = 0;
        this.f557sm = null;
        this.m_Context = null;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.Tools.MeasureDistanceDialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("确定")) {
                    String tmpStr = MeasureDistanceDialog.this.et_measureDistance.getEditableText().toString();
                    if (tmpStr.equals("")) {
                        Common.ShowDialog("没有任何计算结果.");
                        return;
                    }
                    if (MeasureDistanceDialog.this.m_Callback != null) {
                        MeasureDistanceDialog.this.m_Callback.OnClick("测量距离返回", tmpStr);
                    }
                    MeasureDistanceDialog.this._Dialog.dismiss();
                } else if (command.equals("对话框关闭事件")) {
                    if (MeasureDistanceDialog.this.mCamera != null) {
                        MeasureDistanceDialog.this.mCamera.setPreviewCallback(null);
                        MeasureDistanceDialog.this.mCamera.stopPreview();
                        MeasureDistanceDialog.this.mCamera.release();
                        MeasureDistanceDialog.this.mCamera = null;
                    }
                    String tmpStr2 = MeasureDistanceDialog.this.et_DeviceHeight.getText().toString();
                    if (!tmpStr2.equals("")) {
                        PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_Input_Measure_DeviceHeight", tmpStr2);
                    }
                }
            }
        };
        this.mySensorListener = new SensorEventListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.MeasureDistanceDialog.2
            private float lastDegree = 0.0f;

            @Override // android.hardware.SensorEventListener
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }

            @Override // android.hardware.SensorEventListener
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() != 1 && event.sensor.getType() != 2) {
                    if (event.sensor.getType() == 3) {
                        MeasureDistanceDialog.this.currentZAngle = event.values[0];
                        MeasureDistanceDialog.this.currentXAngle = event.values[1];
                        MeasureDistanceDialog.this.currentYAngle = event.values[2];
                        try {
                            if (MeasureDistanceDialog.this.isCalWidth) {
                                float tempAngle = Math.abs(MeasureDistanceDialog.this.currentXAngle);
                                float tempHeight = Float.parseFloat(MeasureDistanceDialog.this.et_DeviceHeight.getText().toString());
                                if (tempAngle < 0.0f) {
                                    tempAngle = -tempAngle;
                                }
                                MeasureDistanceDialog.this.et_measureDistance.setText(MeasureDistanceDialog.this.mDecimalFormat.format((double) Math.abs((float) (Math.tan((double) ((float) Math.toRadians((double) tempAngle))) * ((double) tempHeight)))));
                            }
                        } catch (Exception e) {
                        }
                    } else {
                        event.sensor.getType();
                    }
                }
            }
        };
        this.canvas = null;
        this.isCheckLight = false;
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.measuredistance_dialog);
        this._Dialog.Resize(1.0f, 1.0f);
        this._Dialog.SetCaption("测量距离");
        this._Dialog.getWindow().setFlags(128, 128);
        Window win = this._Dialog.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = -1;
        lp.height = -2;
        win.setAttributes(lp);
        this._Dialog.SetDismissCallback(this.pCallback);
        this.f557sm = (SensorManager) this._Dialog.getContext().getSystemService("sensor");
        this.myBtn01 = (Button) this._Dialog.findViewById(R.id.btn_restoreCamera);
        this.myBtn02 = (Button) this._Dialog.findViewById(R.id.btn_measureHeight);
        this.tipTextView = (TextView) this._Dialog.findViewById(R.id.tv_tipInfo);
        this.tipTextView.setTag(0);
        this.tipTextView.setText("操作提示(点击进入下一条提示):\r\n1.首先在\"设备离地高度\"后输入当前设备摄像头与地面的垂直高度.");
        this.tipTextView.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.MeasureDistanceDialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                String tmpTag = String.valueOf(arg0.getTag());
                if (tmpTag.equals("0")) {
                    MeasureDistanceDialog.this.tipTextView.setTag(1);
                    MeasureDistanceDialog.this.tipTextView.setText("操作提示(2/3):\r\n2.然后将屏幕中央十字丝对准目标(树)底部.\r\n如果没有出现摄像头界面可点击\"重启摄像头\"按钮重新启动摄像头.");
                } else if (tmpTag.equals("1")) {
                    MeasureDistanceDialog.this.tipTextView.setTag(2);
                    MeasureDistanceDialog.this.tipTextView.setText("操作提示(2/3):\r\n3.然后点击\"开始计算距离\"按钮开始计算距离.");
                } else if (tmpTag.equals("2")) {
                    MeasureDistanceDialog.this.tipTextView.setTag(0);
                    MeasureDistanceDialog.this.tipTextView.setText("操作提示(点击进入下一条提示):\r\n1.首先在\"设备离地高度\"后输入当前设备摄像头与地面的垂直高度.");
                }
            }
        });
        this.et_DeviceHeight = (EditText) this._Dialog.findViewById(R.id.et_deviceHeight);
        this.et_measureDistance = (EditText) this._Dialog.findViewById(R.id.et_measureDistance);
        this.myBtn01.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.MeasureDistanceDialog.4
            @Override // android.view.View.OnClickListener
            @SuppressLint({"NewApi"})
            public void onClick(View arg0) {
                MeasureDistanceDialog.this.initialCamera();
            }
        });
        this.myBtn02.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.MeasureDistanceDialog.5
            @Override // android.view.View.OnClickListener
            public void onClick(View arg0) {
                MeasureDistanceDialog.this.startXAngle = MeasureDistanceDialog.this.currentXAngle;
                if (String.valueOf(arg0.getTag()).equals("开始计算距离")) {
                    MeasureDistanceDialog.this.isCalWidth = true;
                    MeasureDistanceDialog.this.myBtn02.setText("停止计算距离");
                    MeasureDistanceDialog.this.myBtn02.setTag("停止计算距离");
                    return;
                }
                MeasureDistanceDialog.this.isCalWidth = false;
                MeasureDistanceDialog.this.myBtn02.setText("开始计算距离");
                MeasureDistanceDialog.this.myBtn02.setTag("开始计算距离");
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void initialCamera() {
        this.f557sm.registerListener(this.mySensorListener, this.f557sm.getDefaultSensor(3), 3);
        Common.ShowToast("正在初始化摄像头...");
        initSurfaceView();
        InitCamera();
        try {
            this.mCamera.stopPreview();
            this.mCamera.setPreviewCallback(this);
            this.mCamera.setDisplayOrientation(90);
            Camera.Parameters parameters = this.mCamera.getParameters();
            parameters.setPreviewFpsRange(20, 30);
            parameters.setFocusMode("continuous-video");
            Camera.Size size = parameters.getPreviewSize();
            this.VideoWidth = size.width;
            this.VideoHeight = size.height;
            this.VideoFormatIndex = parameters.getPreviewFormat();
            this.mCamera.setPreviewDisplay(this.mSurfaceHolder);
            this.mCamera.startPreview();
            this.mCamera.autoFocus(null);
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.MeasureDistanceDialog.6
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                if (MeasureDistanceDialog.this.f557sm.getDefaultSensor(3) == null) {
                    Common.ShowDialog("很抱歉,当前使用的设备中【方向传感器/磁场传感器】不可用!无法使用本功能!");
                    MeasureDistanceDialog.this._Dialog.dismiss();
                    return;
                }
                MeasureDistanceDialog.this.initialCamera();
                if (MeasureDistanceDialog.this.mSurfaceview != null) {
                    MeasureDistanceDialog.this.mSurfaceview.setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Tools.MeasureDistanceDialog.6.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) {
                            if (MeasureDistanceDialog.this.mCamera != null) {
                                MeasureDistanceDialog.this.mCamera.autoFocus(null);
                            }
                        }
                    });
                }
                HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_Input_Measure_DeviceHeight");
                if (tempHashMap != null) {
                    String tmpStr01 = tempHashMap.get("F2");
                    if (MeasureDistanceDialog.this.et_DeviceHeight != null) {
                        MeasureDistanceDialog.this.et_DeviceHeight.setText(tmpStr01);
                    }
                }
                if (MeasureDistanceDialog.this.m_Callback != null) {
                    MeasureDistanceDialog.this._Dialog.SetHeadButtons("1,2130837858,确定,确定", MeasureDistanceDialog.this.pCallback);
                }
            }
        });
        this._Dialog.show();
    }

    private void initSurfaceView() {
        this.mSurfaceview = (SurfaceView) this._Dialog.findViewById(R.id.camera_preview);
        this.mSurfaceHolder = this.mSurfaceview.getHolder();
        this.mSurfaceHolder.addCallback(this);
        this.mSurfaceHolder.setType(3);
    }

    private void InitCamera() {
        try {
            if (this.mCamera != null) {
                this.mCamera.setPreviewCallback(null);
                this.mCamera.stopPreview();
                this.mCamera.release();
                this.mCamera = null;
            }
            this.mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                view.getTag().toString();
            }
        }
    }

    @Override // android.hardware.Camera.PreviewCallback
    public void onPreviewFrame(byte[] data, Camera camera) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        if (this.mCamera != null) {
            try {
                this.mCamera.stopPreview();
                this.mCamera.setPreviewCallback(this);
                this.mCamera.setDisplayOrientation(90);
                Camera.Parameters parameters = this.mCamera.getParameters();
                parameters.setPreviewFpsRange(20, 30);
                parameters.setFocusMode("continuous-video");
                Camera.Size size = parameters.getPreviewSize();
                this.VideoWidth = size.width;
                this.VideoHeight = size.height;
                this.VideoFormatIndex = parameters.getPreviewFormat();
                this.myImageRec = new Rect(0, 0, this.VideoWidth, this.VideoHeight);
                this.mCamera.setPreviewDisplay(this.mSurfaceHolder);
                this.mCamera.startPreview();
                this.mCamera.autoFocus(null);
            } catch (Exception e) {
            }
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder arg0) {
        try {
            if (this.mCamera != null) {
                this.mCamera.setPreviewDisplay(this.mSurfaceHolder);
                this.mCamera.startPreview();
            }
        } catch (IOException e) {
            if (this.mCamera != null) {
                this.mCamera.release();
                this.mCamera = null;
            }
            e.printStackTrace();
        }
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder arg0) {
        if (this.mCamera != null) {
            this.mCamera.setPreviewCallback(null);
            this.mCamera.stopPreview();
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    private void updateCameraParameters() {
        if (this.mCamera != null) {
            Camera.Parameters p = this.mCamera.getParameters();
            p.setGpsTimestamp(new Date().getTime());
            Camera.Size pictureSize = findBestPictureSize(p);
            p.setPictureSize(pictureSize.width, pictureSize.height);
            p.getPictureSize();
            FrameLayout frameLayout = (FrameLayout) this._Dialog.findViewById(R.id.FrameLayout01);
            Camera.Size previewSize = findBestPreviewSize(p);
            previewSize.width = 500;
            previewSize.height = 500;
            p.setPreviewSize(previewSize.width, previewSize.height);
            Common.GetViewSize(this.mSurfaceview);
            this.mCamera.setParameters(p);
            int supportPreviewWidth = previewSize.width;
            int supportPreviewHeight = previewSize.height;
            int width = Math.min(PubVar.ScreenWidth, PubVar.ScreenHeight);
            this.mSurfaceview.setLayoutParams(new FrameLayout.LayoutParams((width * supportPreviewWidth) / supportPreviewHeight, width));
        }
    }

    private Camera.Size findBestPictureSize(Camera.Parameters parameters) {
        int diff = Integer.MIN_VALUE;
        String pictureSizeValueString = parameters.get("picture-size-values");
        if (pictureSizeValueString == null) {
            pictureSizeValueString = parameters.get("picture-size-value");
        }
        if (pictureSizeValueString == null) {
            Camera camera = this.mCamera;
            camera.getClass();
//            return new Camera.Size(camera, PubVar.ScreenWidth, PubVar.ScreenHeight);
        }
        int bestX = 0;
        int bestY = 0;
        String[] tmpStrs = pictureSizeValueString.split(",");
        if (tmpStrs != null && tmpStrs.length > 0) {
            int length = tmpStrs.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String pictureSizeString = tmpStrs[i].trim();
                int dimPosition = pictureSizeString.indexOf(120);
                if (dimPosition != -1) {
                    try {
                        int newX = Integer.parseInt(pictureSizeString.substring(0, dimPosition));
                        int newY = Integer.parseInt(pictureSizeString.substring(dimPosition + 1));
                        Point screenResolution = new Point(PubVar.ScreenWidth, PubVar.ScreenHeight);
                        int newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);
                        if (newDiff == diff) {
                            bestX = newX;
                            bestY = newY;
                            break;
                        } else if (newDiff > diff && newX * 3 == newY * 4) {
                            bestX = newX;
                            bestY = newY;
                            diff = newDiff;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
                i++;
            }
        }
        if (bestX <= 0 || bestY <= 0) {
            return null;
        }
        Camera camera2 = this.mCamera;
        camera2.getClass();
        return /*new Camera.Size(camera2, bestX, bestY)*/null;
    }

    private Camera.Size findBestPreviewSize(Camera.Parameters parameters) {
        int diff = Integer.MAX_VALUE;
        String previewSizeValueString = parameters.get("preview-size-values");
        if (previewSizeValueString == null) {
            previewSizeValueString = parameters.get("preview-size-value");
        }
        if (previewSizeValueString == null) {
            Camera camera = this.mCamera;
            camera.getClass();
            return /*new Camera.Size(camera, PubVar.ScreenWidth, PubVar.ScreenHeight)*/null;
        }
        int bestX = 0;
        int bestY = 0;
        String[] tmpStrs = previewSizeValueString.split(",");
        if (tmpStrs != null && tmpStrs.length > 0) {
            int length = tmpStrs.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String prewsizeString = tmpStrs[i].trim();
                int dimPosition = prewsizeString.indexOf(120);
                if (dimPosition != -1) {
                    try {
                        int newX = Integer.parseInt(prewsizeString.substring(0, dimPosition));
                        int newY = Integer.parseInt(prewsizeString.substring(dimPosition + 1));
                        Point screenResolution = new Point(PubVar.ScreenWidth, PubVar.ScreenHeight);
                        int newDiff = Math.abs(newX - screenResolution.x) + Math.abs(newY - screenResolution.y);
                        if (newDiff == diff) {
                            bestX = newX;
                            bestY = newY;
                            break;
                        } else if (newDiff < diff && newX * 3 == newY * 4) {
                            bestX = newX;
                            bestY = newY;
                            diff = newDiff;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
                i++;
            }
        }
        if (bestX <= 0 || bestY <= 0) {
            return null;
        }
        Camera camera2 = this.mCamera;
        camera2.getClass();
        return /*new Camera.Size(camera2, bestX, bestY)*/null;
    }
}
