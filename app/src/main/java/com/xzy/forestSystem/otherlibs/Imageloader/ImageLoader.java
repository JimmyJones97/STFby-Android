package com.xzy.forestSystem.otherlibs.Imageloader;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class ImageLoader {
    private static final boolean DEBUG = true;
    private static final String TAG = "ImageLoader";
    ExecutorService executorService;
    private FileCache fileCache;
    Handler handler = new Handler();
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap());
    private boolean isUseMediaStoreThumbnails = true;
    private Object lock = new Object();
    private Context mContext;
    private volatile Semaphore mPoolSemaphore;
    private Thread mPoolThread;
    private Handler mPoolThreadHander;
    private boolean mScrollingLock = false;
    private volatile Semaphore mSemaphore = new Semaphore(1);
    private LinkedList<Runnable> mTasks;
    private Type mType = Type.LIFO;
    MemoryCache memoryCache = new MemoryCache();
    private boolean needCropSquareBitmap = false;
    private int requiredSize = 50;

    public enum Type {
        FIFO,
        LIFO
    }

    public ImageLoader(Context context) {
        this.fileCache = new FileCache(context);
        this.mContext = context;
        this.executorService = Executors.newFixedThreadPool(2);
        this.mPoolSemaphore = new Semaphore(24);
        this.mTasks = new LinkedList<>();
        this.mPoolThread = new Thread() { // from class: com.stczh.otherlibs.Imageloader.ImageLoader.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                Looper.prepare();
                ImageLoader.this.mPoolThreadHander = new Handler() { // from class: com.stczh.otherlibs.Imageloader.ImageLoader.1.1
                    @Override // android.os.Handler
                    public void handleMessage(Message msg) {
                        ImageLoader.this.executorService.execute(ImageLoader.this.getTask());
                    }
                };
                Looper.loop();
            }
        };
        this.mPoolThread.start();
    }

    public void DisplayImage(String url, ImageView imageView) {
        this.imageViews.put(imageView, url);
        Bitmap bitmap = this.memoryCache.get(url);
        if (this.needCropSquareBitmap && !(imageView.getLayoutParams().width == this.requiredSize && imageView.getLayoutParams().height == this.requiredSize)) {
            Log.i(TAG, "change imageview LayoutParams ");
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.height = this.requiredSize;
            params.width = this.requiredSize;
            imageView.setLayoutParams(params);
        }
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        queuePhoto(url, imageView);
        imageView.setImageDrawable(new ColorDrawable(-197380));
    }

    public void lock() {
        Log.i(TAG, "lock");
        this.mScrollingLock = true;
    }

    public void unlock() {
        this.mScrollingLock = false;
        synchronized (this.lock) {
            Log.i(TAG, "unlock");
            this.lock.notifyAll();
        }
    }

    public void setRequiredSize(int size) {
        this.requiredSize = size;
    }

    public void setIsUseMediaStoreThumbnails(boolean f) {
        this.isUseMediaStoreThumbnails = f;
    }

    public void setNeedCropSquareBitmap(boolean n) {
        this.needCropSquareBitmap = n;
    }

    private void queuePhoto(String url, ImageView imageView) {
        addTask(new PhotosLoader(new PhotoToLoad(url, imageView)));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private Bitmap getBitmap(String url) {
        long startTime = SystemClock.uptimeMillis();
        Uri uri = Uri.parse(url);
        Bitmap bitmap = null;
        String scheme = uri.getScheme();
        if (scheme == null || "file".equals(scheme)) {
            bitmap = decodeFile(new File(url));
        } else if ("content".equals(scheme)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            if (this.isUseMediaStoreThumbnails) {
                bitmap = MediaStore.Images.Thumbnails.getThumbnail(this.mContext.getContentResolver(), ContentUris.parseId(uri), 3, options);
            } else {
                bitmap = getCustomThumbnail(uri, this.requiredSize);
            }
        } else if ("http".equals(scheme) || "https".equals(scheme)) {
            File f = this.fileCache.getFile(url);
            Bitmap bitmap2 = decodeFile(f);
            if (bitmap2 != null) {
                return bitmap2;
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setInstanceFollowRedirects(true);
                InputStream is = conn.getInputStream();
                OutputStream os = new FileOutputStream(f);
                Utils.CopyStream(is, os);
                os.close();
                return decodeFile(f);
            } catch (Throwable ex) {
                ex.printStackTrace();
                if (ex instanceof OutOfMemoryError) {
                    this.memoryCache.clear();
                }
                return null;
            }
        }
        if (this.needCropSquareBitmap) {
            bitmap = Utils.createCropScaledBitmap(bitmap, this.requiredSize, this.requiredSize);
        }
        Log.i(TAG, "Time taken: " + (SystemClock.uptimeMillis() - startTime) + " ms. Memory used for scaling: " + ((bitmap.getRowBytes() * bitmap.getHeight()) / 1024) + " kb.");
        return bitmap;
    }

    private Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1 = new FileInputStream(f);
            BitmapFactory.decodeStream(stream1, null, o);
            stream1.close();
            int scale = Utils.calculateInSampleSize(o, this.requiredSize, this.requiredSize);
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Log.i(TAG, "scale = " + scale);
            FileInputStream stream2 = new FileInputStream(f);
            Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    private Bitmap getCustomThumbnail(Uri uri, int size) {
        try {
            InputStream input = this.mContext.getContentResolver().openInputStream(uri);
            BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
            onlyBoundsOptions.inJustDecodeBounds = true;
            onlyBoundsOptions.inDither = true;
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
            input.close();
            if (onlyBoundsOptions.outWidth == -1 || onlyBoundsOptions.outHeight == -1) {
                return null;
            }
            int originalSize = onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
            double ratio = originalSize > size ? (double) (originalSize / size) : 1.0d;
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
            bitmapOptions.inDither = true;
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            InputStream input2 = this.mContext.getContentResolver().openInputStream(uri);
            Bitmap decodeStream = BitmapFactory.decodeStream(input2, null, bitmapOptions);
            input2.close();
            return decodeStream;
        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) {
            return 1;
        }
        return k;
    }

    /* access modifiers changed from: private */
    public class PhotoToLoad {
        public ImageView imageView;
        public String url;

        public PhotoToLoad(String u, ImageView i) {
            this.url = u;
            this.imageView = i;
        }
    }

    /* access modifiers changed from: package-private */
    public class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad2) {
            this.photoToLoad = photoToLoad2;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                if (!ImageLoader.this.imageViewReused(this.photoToLoad)) {
                    Bitmap bmp = ImageLoader.this.getBitmap(this.photoToLoad.url);
                    ImageLoader.this.memoryCache.put(this.photoToLoad.url, bmp);
                    if (!ImageLoader.this.imageViewReused(this.photoToLoad)) {
                        if (ImageLoader.this.mScrollingLock) {
                            synchronized (ImageLoader.this.lock) {
                                try {
                                    ImageLoader.this.lock.wait();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        ImageLoader.this.handler.post(new BitmapDisplayer(bmp, this.photoToLoad));
                    }
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = this.imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url)) {
            return true;
        }
        return false;
    }

    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            this.bitmap = b;
            this.photoToLoad = p;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (!ImageLoader.this.imageViewReused(this.photoToLoad)) {
                if (this.bitmap != null) {
                    this.photoToLoad.imageView.setImageBitmap(this.bitmap);
                } else {
                    this.photoToLoad.imageView.setImageDrawable(new ColorDrawable(-197380));
                }
            }
        }
    }

    public void clearCache() {
        this.memoryCache.clear();
        this.fileCache.clear();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private synchronized Runnable getTask() {
        Runnable runnable;
        if (this.mType == Type.FIFO) {
            runnable = this.mTasks.removeFirst();
        } else if (this.mType == Type.LIFO) {
            runnable = this.mTasks.removeLast();
        } else {
            runnable = null;
        }
        return runnable;
    }

    private synchronized void addTask(Runnable runnable) {
        try {
            if (this.mPoolThreadHander == null) {
                this.mSemaphore.acquire();
            }
        } catch (InterruptedException e) {
        }
        this.mTasks.add(runnable);
        this.mPoolThreadHander.sendEmptyMessage(272);
    }
}
