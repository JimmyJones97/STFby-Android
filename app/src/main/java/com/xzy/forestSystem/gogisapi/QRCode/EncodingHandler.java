package  com.xzy.forestSystem.gogisapi.QRCode;

import android.graphics.Bitmap;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.xzy.forestSystem.gogisapi.Common.Common;
import java.util.Hashtable;

public final class EncodingHandler {
    private static final int BLACK = -16777216;

    public static Bitmap createQRCode(String content, int QRWidth, int QRHeight) {
        try {
            new Hashtable<>().put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRWidth, QRHeight);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            int[] pixels = new int[(width * height)];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[(y * width) + x] = -16777216;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return Common.ReplaceBitmapColor(bitmap, 0, -1);
        } catch (Exception e) {
            return null;
        }
    }
}
