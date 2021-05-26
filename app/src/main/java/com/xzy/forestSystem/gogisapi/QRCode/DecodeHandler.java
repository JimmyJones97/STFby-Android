package  com.xzy.forestSystem.gogisapi.QRCode;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.stczh.gzforestSystem.R;
import java.util.Hashtable;

final class DecodeHandler extends Handler {
    private static final String TAG = DecodeHandler.class.getSimpleName();
    private final QRCaptureActivity activity;
    private final MultiFormatReader multiFormatReader = new MultiFormatReader();

    DecodeHandler(QRCaptureActivity activity2, Hashtable<DecodeHintType, Object> hints) {
        this.multiFormatReader.setHints(hints);
        this.activity = activity2;
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        switch (message.what) {
//            case R.id.qr_decode /* 2131230721 */:
//                decode((byte[]) message.obj, message.arg1, message.arg2);
//                return;
//            case R.id.qr_quit /* 2131230727 */:
//                Looper.myLooper().quit();
//                return;
//            default:
//                return;
        }
    }

    private void decode(byte[] data, int width, int height) {
        System.currentTimeMillis();
        Result rawResult = null;
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                rotatedData[(((x * height) + height) - y) - 1] = data[(y * width) + x];
            }
        }
        PlanarYUVLuminanceSource source = CameraManager.get().buildLuminanceSource(rotatedData, height, width);
        try {
            rawResult = this.multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(source)));
        } catch (ReaderException e) {
        } finally {
            this.multiFormatReader.reset();
        }
        if (rawResult != null) {
            System.currentTimeMillis();
//            Message message = Message.obtain(this.activity.getHandler(), R.id.qr_decode_succeeded, rawResult);
//            Bundle bundle = new Bundle();
//            bundle.putParcelable(DecodeThread.BARCODE_BITMAP, source.renderCroppedGreyscaleBitmap());
//            message.setData(bundle);
//            message.sendToTarget();
            return;
        }
//        Message.obtain(this.activity.getHandler(), (int) R.id.qr_decode_failed).sendToTarget();
    }
}
