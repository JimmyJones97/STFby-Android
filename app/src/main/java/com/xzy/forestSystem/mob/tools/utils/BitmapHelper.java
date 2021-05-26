package com.xzy.forestSystem.mob.tools.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.opengl.GLES10;
import androidx.core.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.network.HttpConnection;
import  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback;
import  com.xzy.forestSystem.mob.tools.network.NetworkHelper;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BitmapHelper {
    private static final int DEFAULT_MAX_BITMAP_DIMENSION = 2048;
    private static int maxBitmapHeight;
    private static int maxBitmapWidth;

    static {
        int[] maxTextureSize = new int[1];
        GLES10.glGetIntegerv(3379, maxTextureSize, 0);
        int maxBitmapDimension = Math.max(maxTextureSize[0], 2048);
        maxBitmapWidth = maxBitmapDimension;
        maxBitmapHeight = maxBitmapDimension;
    }

    public static Bitmap getBitmapByCompressSize(String path, int desiredWidth, int desiredHeight) throws Throwable {
        BitmapFactory.Options measureOptions = new BitmapFactory.Options();
        measureOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, measureOptions);
        int realWidth = measureOptions.outWidth;
        int realHeight = measureOptions.outHeight;
        int inSampleSize = 1;
        if (desiredWidth > 1 && desiredHeight > 1) {
            float minSideScale = (1.0f * ((float) Math.min(realWidth, realHeight))) / ((float) Math.min(desiredWidth, desiredHeight));
            float maxSideScale = (1.0f * ((float) Math.max(realWidth, realHeight))) / ((float) Math.max(desiredWidth, desiredHeight));
            float ratio = (float) (realWidth / realHeight);
            if (ratio > 2.0f || ((double) ratio) < 0.5d) {
                float n = 1.0f;
                while (2.0f * n <= minSideScale) {
                    n *= 2.0f;
                }
                inSampleSize = (int) n;
            } else {
                float n2 = 1.0f;
                while (2.0f * n2 <= Math.min(minSideScale, maxSideScale)) {
                    n2 *= 2.0f;
                }
                inSampleSize = (int) n2;
            }
        }
        if (inSampleSize < 1) {
            inSampleSize = 1;
        }
        while (true) {
            if (realWidth / inSampleSize > maxBitmapWidth || realHeight / inSampleSize > maxBitmapHeight) {
                inSampleSize++;
            } else {
                BitmapFactory.Options decodingOptions = new BitmapFactory.Options();
                decodingOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                decodingOptions.inSampleSize = inSampleSize;
                return BitmapFactory.decodeFile(path, decodingOptions);
            }
        }
    }

    public static Bitmap getBitmapByCompressQuality(String path, int desiredWidth, int desiredHeight, int quality, long maxBytes) throws Throwable {
        Bitmap newBitmap;
        Bitmap compressedSizeBitmap = getBitmapByCompressSize(path, desiredWidth, desiredHeight);
        if (quality < 10 || quality > 100) {
            quality = 100;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap.CompressFormat format = getBmpFormat(path);
        compressedSizeBitmap.compress(format, quality, baos);
        byte[] compressedBytes = baos.toByteArray();
        if (maxBytes < 10240) {
            Bitmap qualityBitmap = BitmapFactory.decodeByteArray(compressedBytes, 0, compressedBytes.length);
            try {
                baos.close();
                return qualityBitmap;
            } catch (Throwable th) {
                return qualityBitmap;
            }
        } else {
            while (((long) compressedBytes.length) > maxBytes && quality >= 11) {
                baos.reset();
                quality -= 6;
                compressedSizeBitmap.compress(format, quality, baos);
                compressedBytes = baos.toByteArray();
            }
            if (quality == 100) {
                newBitmap = compressedSizeBitmap;
            } else {
                newBitmap = BitmapFactory.decodeByteArray(compressedBytes, 0, compressedBytes.length);
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (Throwable th2) {
                }
            }
            return newBitmap;
        }
    }

    public static String saveBitmapByCompress(String path, int desiredWidth, int desiredHeight, int quality) throws Throwable {
        Bitmap compressedSizeBitmap = getBitmapByCompressSize(path, desiredWidth, desiredHeight);
        if (quality > 100) {
            quality = 100;
        } else if (quality < 10) {
            quality = 10;
        }
        Bitmap.CompressFormat format = getBmpFormat(path);
        String extension = ".jpg";
        if (format == Bitmap.CompressFormat.PNG) {
            extension = ".png";
        }
        File newFile = new File(new File(path).getParent(), String.valueOf(System.currentTimeMillis()) + extension);
        FileOutputStream fos = new FileOutputStream(newFile);
        compressedSizeBitmap.compress(format, quality, fos);
        fos.flush();
        fos.close();
        return newFile.getAbsolutePath();
    }

    public static Bitmap getBitmap(String path, int inSampleSize) throws Throwable {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return getBitmap(new File(path), inSampleSize);
    }

    public static Bitmap getBitmap(File file, int inSampleSize) throws Throwable {
        if (file == null || !file.exists()) {
            return null;
        }
        FileInputStream fis = new FileInputStream(file);
        Bitmap bitmap = getBitmap(fis, inSampleSize);
        fis.close();
        return bitmap;
    }

    public static Bitmap getBitmap(InputStream is, int inSampleSize) {
        if (is == null) {
            return null;
        }
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        opt.inSampleSize = inSampleSize;
        return BitmapFactory.decodeStream(is, null, opt);
    }

    public static Bitmap getBitmap(String path) throws Throwable {
        return getBitmap(path, 1);
    }

    public static Bitmap getBitmap(Context context, String url) throws Throwable {
        return getBitmap(downloadBitmap(context, url));
    }

    public static String downloadBitmap(Context context, final String imageUrl) throws Throwable {
        final String path = ResHelper.getCachePath(context, "images");
        File cache = new File(path, Data.MD5(imageUrl));
        if (cache.exists()) {
            return cache.getAbsolutePath();
        }
        final HashMap<String, String> buffer = new HashMap<>();
        new NetworkHelper().rawGet(imageUrl, new HttpResponseCallback() { // from class:  com.xzy.forestSystem.mob.tools.utils.BitmapHelper.1
            @Override //  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback
            public void onResponse(HttpConnection conn) throws Throwable {
                int status = conn.getResponseCode();
                if (status == 200) {
                    String name = BitmapHelper.getFileName(conn, imageUrl);
                    File cache2 = new File(path, name);
                    if (cache2.exists()) {
                        buffer.put("bitmap", cache2.getAbsolutePath());
                        return;
                    }
                    if (!cache2.getParentFile().exists()) {
                        cache2.getParentFile().mkdirs();
                    }
                    if (cache2.exists()) {
                        cache2.delete();
                    }
                    try {
                        Bitmap bitmap = BitmapHelper.getBitmap(new FilterInputStream(conn.getInputStream()) { // from class:  com.xzy.forestSystem.mob.tools.utils.BitmapHelper.1.1
                            @Override // java.io.FilterInputStream, java.io.InputStream
                            public long skip(long n) throws IOException {
                                long m = 0;
                                while (m < n) {
                                    long mm = this.in.skip(n - m);
                                    if (mm == 0) {
                                        break;
                                    }
                                    m += mm;
                                }
                                return m;
                            }
                        }, 1);
                        if (!(bitmap == null || bitmap.isRecycled())) {
                            FileOutputStream fos = new FileOutputStream(cache2);
                            if (name.toLowerCase().endsWith(".gif") || name.toLowerCase().endsWith(".png")) {
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                            } else {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                            }
                            fos.flush();
                            fos.close();
                            buffer.put("bitmap", cache2.getAbsolutePath());
                        }
                    } catch (Throwable t) {
                        if (cache2.exists()) {
                            cache2.delete();
                        }
                        throw t;
                    }
                } else {
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), Charset.forName("utf-8")));
                    for (String txt = br.readLine(); txt != null; txt = br.readLine()) {
                        if (sb.length() > 0) {
                            sb.append('\n');
                        }
                        sb.append(txt);
                    }
                    br.close();
                    HashMap<String, Object> errMap = new HashMap<>();
                    errMap.put("error", sb.toString());
                    errMap.put("status", Integer.valueOf(status));
                    throw new Throwable(new Hashon().fromHashMap(errMap));
                }
            }
        }, (NetworkHelper.NetworkTimeOut) null);
        return buffer.get("bitmap");
    }

    /* access modifiers changed from: private */
    public static String getFileName(HttpConnection conn, String url) throws Throwable {
        List<String> headers;
        int dot;
        List<String> headers2;
        String name = null;
        Map<String, List<String>> map = conn.getHeaderFields();
        if (!(map == null || (headers2 = map.get("Content-Disposition")) == null || headers2.size() <= 0)) {
            String[] parts = headers2.get(0).split(";");
            for (String part : parts) {
                if (part.trim().startsWith("filename")) {
                    name = part.split("=")[1];
                    if (name.startsWith("\"") && name.endsWith("\"")) {
                        name = name.substring(1, name.length() - 1);
                    }
                }
            }
        }
        if (name != null) {
            return name;
        }
        String name2 = Data.MD5(url);
        if (map == null || (headers = map.get("Content-Type")) == null || headers.size() <= 0) {
            return name2;
        }
        String value = headers.get(0);
        String value2 = value == null ? "" : value.trim();
        if (value2.startsWith("image/")) {
            String type = value2.substring("image/".length());
            StringBuilder append = new StringBuilder().append(name2).append(FileSelector_Dialog.sFolder);
            if ("jpeg".equals(type)) {
                type = "jpg";
            }
            return append.append(type).toString();
        }
        int index = url.lastIndexOf(47);
        String lastPart = null;
        if (index > 0) {
            lastPart = url.substring(index + 1);
        }
        if (lastPart == null || lastPart.length() <= 0 || (dot = lastPart.lastIndexOf(46)) <= 0 || lastPart.length() - dot >= 10) {
            return name2;
        }
        return name2 + lastPart.substring(dot);
    }

    public static String saveViewToImage(View view) throws Throwable {
        if (view == null) {
            return null;
        }
        int width = view.getWidth();
        int height = view.getHeight();
        if (width <= 0 || height <= 0) {
            return null;
        }
        return saveViewToImage(view, width, height);
    }

    public static String saveViewToImage(View view, int width, int height) throws Throwable {
        Bitmap bm = captureView(view, width, height);
        if (bm == null || bm.isRecycled()) {
            return null;
        }
        File ss = new File(ResHelper.getCachePath(view.getContext(), "screenshot"), String.valueOf(System.currentTimeMillis()) + ".jpg");
        FileOutputStream fos = new FileOutputStream(ss);
        bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
        return ss.getAbsolutePath();
    }

    public static Bitmap captureView(View view, int width, int height) throws Throwable {
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bm));
        return bm;
    }

    public static Bitmap blur(Bitmap bm, int radius, int scale) {
        Bitmap overlay = Bitmap.createBitmap((int) ((((float) bm.getWidth()) / ((float) scale)) + 0.5f), (int) ((((float) bm.getHeight()) / ((float) scale)) + 0.5f), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.scale(1.0f / ((float) scale), 1.0f / ((float) scale));
        Paint paint = new Paint();
        paint.setFlags(2);
        canvas.drawBitmap(bm, 0.0f, 0.0f, paint);
        blur(overlay, (int) ((((float) radius) / ((float) scale)) + 0.5f), true);
        return overlay;
    }

    private static Bitmap blur(Bitmap sentBitmap, int radius, boolean canReuseInBitmap) {
        Bitmap bitmap;
        if (canReuseInBitmap) {
            bitmap = sentBitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
        }
        if (radius < 1) {
            return null;
        }
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        int[] pix = new int[(w * h)];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;
        int[] r = new int[wh];
        int[] g = new int[wh];
        int[] b = new int[wh];
        int[] vmin = new int[Math.max(w, h)];
        int divsum = (div + 1) >> 1;
        int divsum2 = divsum * divsum;
        int[] dv = new int[(divsum2 * 256)];
        for (int i = 0; i < divsum2 * 256; i++) {
            dv[i] = i / divsum2;
        }
        int yi = 0;
        int yw = 0;
        int[][] stack = (int[][]) Array.newInstance(Integer.TYPE, div, 3);
        int r1 = radius + 1;
        for (int y = 0; y < h; y++) {
            int bsum = 0;
            int gsum = 0;
            int rsum = 0;
            int boutsum = 0;
            int goutsum = 0;
            int routsum = 0;
            int binsum = 0;
            int ginsum = 0;
            int rinsum = 0;
            for (int i2 = -radius; i2 <= radius; i2++) {
                int p = pix[Math.min(wm, Math.max(i2, 0)) + yi];
                int[] sir = stack[i2 + radius];
                sir[0] = (16711680 & p) >> 16;
                sir[1] = (65280 & p) >> 8;
                sir[2] = p & 255;
                int rbs = r1 - Math.abs(i2);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i2 > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            int stackpointer = radius;
            for (int x = 0; x < w; x++) {
                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];
                int rsum2 = rsum - routsum;
                int gsum2 = gsum - goutsum;
                int bsum2 = bsum - boutsum;
                int[] sir2 = stack[((stackpointer - radius) + div) % div];
                int routsum2 = routsum - sir2[0];
                int goutsum2 = goutsum - sir2[1];
                int boutsum2 = boutsum - sir2[2];
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                int p2 = pix[vmin[x] + yw];
                sir2[0] = (16711680 & p2) >> 16;
                sir2[1] = (65280 & p2) >> 8;
                sir2[2] = p2 & 255;
                int rinsum2 = rinsum + sir2[0];
                int ginsum2 = ginsum + sir2[1];
                int binsum2 = binsum + sir2[2];
                rsum = rsum2 + rinsum2;
                gsum = gsum2 + ginsum2;
                bsum = bsum2 + binsum2;
                stackpointer = (stackpointer + 1) % div;
                int[] sir3 = stack[stackpointer % div];
                routsum = routsum2 + sir3[0];
                goutsum = goutsum2 + sir3[1];
                boutsum = boutsum2 + sir3[2];
                rinsum = rinsum2 - sir3[0];
                ginsum = ginsum2 - sir3[1];
                binsum = binsum2 - sir3[2];
                yi++;
            }
            yw += w;
        }
        for (int x2 = 0; x2 < w; x2++) {
            int bsum3 = 0;
            int gsum3 = 0;
            int rsum3 = 0;
            int boutsum3 = 0;
            int goutsum3 = 0;
            int routsum3 = 0;
            int binsum3 = 0;
            int ginsum3 = 0;
            int rinsum3 = 0;
            int yp = (-radius) * w;
            for (int i3 = -radius; i3 <= radius; i3++) {
                int yi2 = Math.max(0, yp) + x2;
                int[] sir4 = stack[i3 + radius];
                sir4[0] = r[yi2];
                sir4[1] = g[yi2];
                sir4[2] = b[yi2];
                int rbs2 = r1 - Math.abs(i3);
                rsum3 += r[yi2] * rbs2;
                gsum3 += g[yi2] * rbs2;
                bsum3 += b[yi2] * rbs2;
                if (i3 > 0) {
                    rinsum3 += sir4[0];
                    ginsum3 += sir4[1];
                    binsum3 += sir4[2];
                } else {
                    routsum3 += sir4[0];
                    goutsum3 += sir4[1];
                    boutsum3 += sir4[2];
                }
                if (i3 < hm) {
                    yp += w;
                }
            }
            int yi3 = x2;
            int stackpointer2 = radius;
            for (int y2 = 0; y2 < h; y2++) {
                pix[yi3] = (-16777216 & pix[yi3]) | (dv[rsum3] << 16) | (dv[gsum3] << 8) | dv[bsum3];
                int rsum4 = rsum3 - routsum3;
                int gsum4 = gsum3 - goutsum3;
                int bsum4 = bsum3 - boutsum3;
                int[] sir5 = stack[((stackpointer2 - radius) + div) % div];
                int routsum4 = routsum3 - sir5[0];
                int goutsum4 = goutsum3 - sir5[1];
                int boutsum4 = boutsum3 - sir5[2];
                if (x2 == 0) {
                    vmin[y2] = Math.min(y2 + r1, hm) * w;
                }
                int p3 = x2 + vmin[y2];
                sir5[0] = r[p3];
                sir5[1] = g[p3];
                sir5[2] = b[p3];
                int rinsum4 = rinsum3 + sir5[0];
                int ginsum4 = ginsum3 + sir5[1];
                int binsum4 = binsum3 + sir5[2];
                rsum3 = rsum4 + rinsum4;
                gsum3 = gsum4 + ginsum4;
                bsum3 = bsum4 + binsum4;
                stackpointer2 = (stackpointer2 + 1) % div;
                int[] sir6 = stack[stackpointer2];
                routsum3 = routsum4 + sir6[0];
                goutsum3 = goutsum4 + sir6[1];
                boutsum3 = boutsum4 + sir6[2];
                rinsum3 = rinsum4 - sir6[0];
                ginsum3 = ginsum4 - sir6[1];
                binsum3 = binsum4 - sir6[2];
                yi3 += w;
            }
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return bitmap;
    }

    public static Bitmap roundBitmap(Bitmap orginal, int width, int height, float leftTop, float rightTop, float rightBottom, float leftBottom) throws Throwable {
        Bitmap output;
        int oriWidth = orginal.getWidth();
        int oriHeight = orginal.getHeight();
        Rect src = new Rect(0, 0, oriWidth, oriHeight);
        if (oriWidth == width && oriHeight == height) {
            output = Bitmap.createBitmap(orginal.getWidth(), orginal.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect dst = new Rect(0, 0, width, height);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        float[] outerRadii = {leftTop, leftTop, rightTop, rightTop, rightBottom, rightBottom, leftBottom, leftBottom};
        ShapeDrawable draweable = new ShapeDrawable(new RoundRectShape(outerRadii, new RectF(0.0f, 0.0f, 0.0f, 0.0f), outerRadii));
        draweable.setBounds(dst);
        draweable.draw(canvas);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(orginal, src, dst, paint);
        return output;
    }

    public static int[] fixRect(int[] src, int[] target) {
        int[] dst = new int[2];
        if (((float) src[0]) / ((float) src[1]) > ((float) target[0]) / ((float) target[1])) {
            dst[0] = target[0];
            dst[1] = (int) (((((float) src[1]) * ((float) target[0])) / ((float) src[0])) + 0.5f);
        } else {
            dst[1] = target[1];
            dst[0] = (int) (((((float) src[0]) * ((float) target[1])) / ((float) src[1])) + 0.5f);
        }
        return dst;
    }

    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x0031: APUT  
      (r0v0 'dst' int[] A[D('dst' int[])])
      (0 ??[int, short, byte, char])
      (wrap: int : 0x002f: AGET  (r3v6 int) = (r9v0 'target' int[] A[D('target' int[])]), (0 ??[int, short, byte, char]))
     */
    public static int[] fixRect_2(int[] src, int[] target) {
        int[] dst = new int[2];
        if (((float) src[0]) / ((float) src[1]) > ((float) target[0]) / ((float) target[1])) {
            dst[1] = target[1];
            dst[0] = (int) (((((float) src[0]) * ((float) target[1])) / ((float) src[1])) + 0.5f);
        } else {
            dst[0] = target[0];
            dst[1] = (int) (((((float) src[1]) * ((float) target[0])) / ((float) src[0])) + 0.5f);
        }
        return dst;
    }

    public static String saveBitmap(Context context, Bitmap bm, Bitmap.CompressFormat format, int quality) throws Throwable {
        String path = ResHelper.getCachePath(context, "images");
        String ext = ".jpg";
        if (format == Bitmap.CompressFormat.PNG) {
            ext = ".png";
        }
        File ss = new File(path, String.valueOf(System.currentTimeMillis()) + ext);
        FileOutputStream fos = new FileOutputStream(ss);
        bm.compress(format, quality, fos);
        fos.flush();
        fos.close();
        return ss.getAbsolutePath();
    }

    public static String saveBitmap(Context context, Bitmap bm) throws Throwable {
        return saveBitmap(context, bm, Bitmap.CompressFormat.JPEG, 80);
    }

    public static Bitmap.CompressFormat getBmpFormat(byte[] data) {
        String mime = getMime(data);
        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        if (mime == null) {
            return format;
        }
        if (mime.endsWith("png") || mime.endsWith("gif")) {
            return Bitmap.CompressFormat.PNG;
        }
        return format;
    }

    public static Bitmap.CompressFormat getBmpFormat(String filePath) {
        String pathLower = filePath.toLowerCase();
        if (pathLower.endsWith("png") || pathLower.endsWith("gif")) {
            return Bitmap.CompressFormat.PNG;
        }
        if (pathLower.endsWith("jpg") || pathLower.endsWith("jpeg") || pathLower.endsWith("bmp") || pathLower.endsWith("tif")) {
            return Bitmap.CompressFormat.JPEG;
        }
        String mime = getMime(filePath);
        if (mime.endsWith("png") || mime.endsWith("gif")) {
            return Bitmap.CompressFormat.PNG;
        }
        return Bitmap.CompressFormat.JPEG;
    }

    public static String getMime(String file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] bytes = new byte[8];
            fis.read(bytes);
            fis.close();
            return getMime(bytes);
        } catch (Exception e) {
            MobLog.getInstance().m57w(e);
            return null;
        }
    }

    private static String getMime(byte[] bytes) {
        byte[] jpeg2 = {-1, -40, -1, -31};
        if (bytesStartWith(bytes, new byte[]{-1, -40, -1, -32}) || bytesStartWith(bytes, jpeg2)) {
            return "jpg";
        }
        if (bytesStartWith(bytes, new byte[]{-119, 80, 78, 71})) {
            return "png";
        }
        if (bytesStartWith(bytes, "GIF".getBytes())) {
            return "gif";
        }
        if (bytesStartWith(bytes, "BM".getBytes())) {
            return "bmp";
        }
        byte[] tiff2 = {77, 77, 42};
        if (bytesStartWith(bytes, new byte[]{73, 73, 42}) || bytesStartWith(bytes, tiff2)) {
            return "tif";
        }
        return null;
    }

    private static boolean bytesStartWith(byte[] target, byte[] prefix) {
        if (target == prefix) {
            return true;
        }
        if (target == null || prefix == null) {
            return false;
        }
        if (target.length < prefix.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (target[i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }

    public static Bitmap cropBitmap(Bitmap orginal, int left, int top, int right, int bottom) throws Throwable {
        int width = (orginal.getWidth() - left) - right;
        int height = (orginal.getHeight() - top) - bottom;
        if (width == orginal.getWidth() && height == orginal.getHeight()) {
            return orginal;
        }
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        new Canvas(output).drawBitmap(orginal, (float) (-left), (float) (-top), new Paint());
        return output;
    }

    public static boolean isBlackBitmap(Bitmap bm) throws Throwable {
        if (bm == null || bm.isRecycled()) {
            return true;
        }
        int[] pixels = new int[(bm.getWidth() * bm.getHeight())];
        bm.getPixels(pixels, 0, bm.getWidth(), 0, 0, bm.getWidth(), bm.getHeight());
        boolean found = false;
        int i = 0;
        while (true) {
            if (i >= pixels.length) {
                break;
            } else if ((pixels[i] & ViewCompat.MEASURED_SIZE_MASK) != 0) {
                found = true;
                break;
            } else {
                i++;
            }
        }
        return !found;
    }

    public static int mixAlpha(int frontARGB, int backRGB) {
        int fa = frontARGB >>> 24;
        return -16777216 | ((((fa * ((16711680 & frontARGB) >>> 16)) + ((255 - fa) * ((16711680 & backRGB) >>> 16))) / 255) << 16) | ((((fa * ((65280 & frontARGB) >>> 8)) + ((255 - fa) * ((65280 & backRGB) >>> 8))) / 255) << 8) | (((fa * (frontARGB & 255)) + ((255 - fa) * (backRGB & 255))) / 255);
    }

    public static Bitmap scaleBitmapByHeight(Context context, int resId, int height) throws Throwable {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resId);
        boolean rec = height != bm.getHeight();
        Bitmap dst = scaleBitmapByHeight(bm, height);
        if (rec) {
            bm.recycle();
        }
        return dst;
    }

    public static Bitmap scaleBitmapByHeight(Bitmap src, int height) throws Throwable {
        return Bitmap.createScaledBitmap(src, (src.getWidth() * height) / src.getHeight(), height, true);
    }
}
