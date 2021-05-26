package  com.xzy.forestSystem.gogisapi.QRCode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.stczh.gzforestSystem.R;
import java.util.Vector;

public final class QRCaptureActivityHandler extends Handler {
    private static final String TAG = QRCaptureActivityHandler.class.getSimpleName();
    private final QRCaptureActivity activity;
    private final DecodeThread decodeThread;
    private State state = State.SUCCESS;

    /* access modifiers changed from: private */
    public enum State {
        PREVIEW,
        SUCCESS,
        DONE
    }

    public QRCaptureActivityHandler(QRCaptureActivity activity2, Vector<BarcodeFormat> decodeFormats, String characterSet) {
        this.activity = activity2;
        this.decodeThread = new DecodeThread(activity2, decodeFormats, characterSet, new ViewfinderResultPointCallback(activity2.getViewfinderView()));
        this.decodeThread.start();
        CameraManager.get().startPreview();
        restartPreviewAndDecode();
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        Bitmap barcode;
//        switch (message.what) {
//            case R.id.qr_auto_focus /* 2131230720 */:
//                if (this.state == State.PREVIEW) {
//                    CameraManager.get().requestAutoFocus(this, R.id.qr_auto_focus);
//                    return;
//                }
//                return;
//            case R.id.qr_decode /* 2131230721 */:
//            case R.id.qr_encode_failed /* 2131230724 */:
//            case R.id.qr_encode_succeeded /* 2131230725 */:
//            case R.id.qr_quit /* 2131230727 */:
//            default:
//                return;
//            case R.id.qr_decode_failed /* 2131230722 */:
//                this.state = State.PREVIEW;
//                CameraManager.get().requestPreviewFrame(this.decodeThread.getHandler(), R.id.qr_decode);
//                return;
//            case R.id.qr_decode_succeeded /* 2131230723 */:
//                Log.d(TAG, "Got decode succeeded message");
//                this.state = State.SUCCESS;
//                Bundle bundle = message.getData();
//                if (bundle == null) {
//                    barcode = null;
//                } else {
//                    barcode = (Bitmap) bundle.getParcelable(DecodeThread.BARCODE_BITMAP);
//                }
//                this.activity.handleDecode((Result) message.obj, barcode);
//                return;
//            case R.id.qr_launch_product_query /* 2131230726 */:
//                Log.d(TAG, "Got product query message");
//                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse((String) message.obj));
//                intent.addFlags(AccessibilityEventCompat.TYPE_GESTURE_DETECTION_END);
//                this.activity.startActivity(intent);
//                return;
//            case R.id.qr_restart_preview /* 2131230728 */:
//                Log.d(TAG, "Got restart preview message");
//                restartPreviewAndDecode();
//                return;
//            case R.id.qr_return_scan_result /* 2131230729 */:
//                Log.d(TAG, "Got return scan result message");
//                this.activity.setResult(-1, (Intent) message.obj);
//                this.activity.finish();
//                return;
//        }
    }

    public void quitSynchronously() {
        this.state = State.DONE;
        CameraManager.get().stopPreview();
//        Message.obtain(this.decodeThread.getHandler(), (int) R.id.qr_quit).sendToTarget();
        try {
            this.decodeThread.join();
        } catch (InterruptedException e) {
        }
//        removeMessages(R.id.qr_decode_succeeded);
//        removeMessages(R.id.qr_decode_failed);
    }

    private void restartPreviewAndDecode() {
        if (this.state == State.SUCCESS) {
            this.state = State.PREVIEW;
//            CameraManager.get().requestPreviewFrame(this.decodeThread.getHandler(), R.id.qr_decode);
//            CameraManager.get().requestAutoFocus(this, R.id.qr_auto_focus);
            this.activity.drawViewfinder();
        }
    }
}
