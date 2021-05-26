package  com.xzy.forestSystem.qihoo.jiagutracker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.lang.reflect.Field;

public class ViewInfo {
    byte[] background;
    int buttom;
    String classname;
    boolean isFocus;
    int left;
    int right;
    String text;
    int top;

    public ViewInfo(View view) {
        this.classname = view.getClass().getCanonicalName();
        this.text = getText(view);
        this.background = getBackground(view);
        this.left = view.getLeft();
        this.top = view.getTop();
        this.right = view.getRight();
        this.buttom = view.getBottom();
        this.isFocus = view.isFocused();
    }

    public String getText(View view) {
        if (view instanceof TextView) {
            return ((TextView) view).getText().toString();
        }
        if (view instanceof EditText) {
            return ((EditText) view).getHint().toString();
        }
        return null;
    }

    public byte[] getBackground(View view) {
        return getBytes(view);
    }

    public byte[] getBytes(View view) {
        if (view == null) {
            return null;
        }
        try {
            Bitmap bitmapFromView = getBitmapFromView(view);
            if (bitmapFromView != null) {
                return getBytes(bitmapFromView);
            }
            return null;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0033  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] getBytes(android.graphics.Bitmap r6) throws java.lang.Throwable {
        /*
            r5 = this;
            r0 = 0
            r1 = 32
            r2 = 32
            r3 = 1
            android.graphics.Bitmap r1 = android.graphics.Bitmap.createScaledBitmap(r6, r1, r2, r3)     // Catch:{ Throwable -> 0x0024, all -> 0x002f }
            android.graphics.Bitmap r1 = r5.toGrayscale(r1)     // Catch:{ Throwable -> 0x0024, all -> 0x002f }
            java.io.ByteArrayOutputStream r2 = new java.io.ByteArrayOutputStream     // Catch:{ Throwable -> 0x0024, all -> 0x002f }
            r2.<init>()     // Catch:{ Throwable -> 0x0024, all -> 0x002f }
            android.graphics.Bitmap$CompressFormat r3 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch:{ Throwable -> 0x003a }
            r4 = 100
            r1.compress(r3, r4, r2)     // Catch:{ Throwable -> 0x003a }
            byte[] r0 = r2.toByteArray()     // Catch:{ Throwable -> 0x003a }
            if (r2 == 0) goto L_0x0023
            r2.close()
        L_0x0023:
            return r0
        L_0x0024:
            r1 = move-exception
            r2 = r0
        L_0x0026:
            r1.printStackTrace()     // Catch:{ all -> 0x0037 }
            if (r2 == 0) goto L_0x0023
            r2.close()
            goto L_0x0023
        L_0x002f:
            r1 = move-exception
            r2 = r0
        L_0x0031:
            if (r2 == 0) goto L_0x0036
            r2.close()
        L_0x0036:
            throw r1
        L_0x0037:
            r0 = move-exception
            r1 = r0
            goto L_0x0031
        L_0x003a:
            r1 = move-exception
            goto L_0x0026
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.qihoo.jiagutracker.ViewInfo.getBytes(android.graphics.Bitmap):byte[]");
    }

    private Bitmap toGrayscale(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public Bitmap getBitmapFromView(View view) throws Throwable {
        Drawable background2;
        Bitmap bitmap;
        NinePatch ninePatch;
        Bitmap bitmap2 = null;
        if (view instanceof ImageView) {
            background2 = ((ImageView) view).getDrawable();
        } else {
            background2 = view.getBackground();
        }
        if (background2 == null) {
            return null;
        }
        if (background2 instanceof NinePatchDrawable) {
            NinePatchDrawable ninePatchDrawable = (NinePatchDrawable) background2;
            try {
                Field declaredField = NinePatchDrawable.class.getDeclaredField("mNinePatch");
                declaredField.setAccessible(true);
                ninePatch = (NinePatch) declaredField.get(ninePatchDrawable);
            } catch (Throwable th) {
                th.printStackTrace();
                ninePatch = null;
            }
            if (Build.VERSION.SDK_INT >= 19 && ninePatch != null && ninePatch.getBitmap() == null) {
                bitmap2 = ninePatch.getBitmap();
            }
            bitmap = bitmap2;
        } else {
            bitmap = background2 instanceof BitmapDrawable ? ((BitmapDrawable) background2).getBitmap() : null;
        }
        if (bitmap == null) {
            bitmap = drawableToBitmap(background2);
        }
        return bitmap;
    }

    public Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), drawable.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        if (drawable.getBounds() == null) {
            return null;
        }
        drawable.draw(canvas);
        return createBitmap;
    }

    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x000d: APUT  (r2v0 java.lang.Object[]), (0 ??[int, short, byte, char]), (r0v3 java.lang.String) */
    public String toString() {
        String str;
        Object[] objArr = new Object[8];
        objArr[0] = this.classname == null ? "" : this.classname;
        objArr[1] = this.text == null ? "" : this.text;
        objArr[2] = Integer.valueOf(this.left);
        objArr[3] = Integer.valueOf(this.top);
        objArr[4] = Integer.valueOf(this.right);
        objArr[5] = Integer.valueOf(this.buttom);
        objArr[6] = Boolean.valueOf(this.isFocus);
        if (this.background == null) {
            str = "";
        } else {
            str = new String(this.background);
        }
        objArr[7] = str;
        return String.format("classname:%s text:%s left:%d top:%d right:%d buttom:%d isFocus:%b background:%s \n", objArr);
    }
}
