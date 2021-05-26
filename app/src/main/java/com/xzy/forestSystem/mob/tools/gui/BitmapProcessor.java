package  com.xzy.forestSystem.mob.tools.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import  com.xzy.forestSystem.mob.tools.MobHandlerThread;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.network.NetworkHelper;
import  com.xzy.forestSystem.mob.tools.network.RawNetworkCallback;
import  com.xzy.forestSystem.mob.tools.utils.BitmapHelper;
import  com.xzy.forestSystem.mob.tools.utils.Data;
import  com.xzy.forestSystem.mob.tools.utils.ResHelper;
import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Iterator;

public class BitmapProcessor {
    private static final int CAPACITY = 3;
    private static final int MAX_CACHE_SIZE = 50;
    private static final int MAX_CACHE_TIME = 60000;
    private static final int MAX_REQ_TIME = 20000;
    private static final int MAX_SIZE = 100;
    private static final int OVERFLOW_SIZE = 120;
    private static final int SCAN_INTERVAL = 20000;
    private static File cacheDir;
    private static CachePool<String, SoftReference<Bitmap>> cachePool = new CachePool<>(50);
    private static ManagerThread manager;
    private static ArrayList<ImageReq> netReqTPS = new ArrayList<>();
    private static ArrayList<ImageReq> reqList = new ArrayList<>();
    private static NetworkHelper.NetworkTimeOut timeout;
    private static boolean work;
    private static WorkerThread[] workerList = new WorkerThread[3];

    public interface BitmapCallback {
        void onImageGot(String str, Bitmap bitmap);
    }

    static {
        NetworkHelper.NetworkTimeOut timeout2 = new NetworkHelper.NetworkTimeOut();
        timeout2.connectionTimeout = 5000;
        timeout2.readTimout = 20000 - timeout2.connectionTimeout;
    }

    public static synchronized void prepare(Context context) {
        synchronized (BitmapProcessor.class) {
            cacheDir = new File(ResHelper.getImageCachePath(context));
        }
    }

    public static synchronized void start() {
        synchronized (BitmapProcessor.class) {
            if (!work) {
                work = true;
                manager = new ManagerThread();
            }
        }
    }

    public static synchronized void stop() {
        synchronized (BitmapProcessor.class) {
            if (work) {
                work = false;
                synchronized (reqList) {
                    reqList.clear();
                    cachePool.clear();
                }
                manager.quit();
            }
        }
    }

    public static synchronized void process(String url, BitmapCallback callback) {
        synchronized (BitmapProcessor.class) {
            process(url, null, callback);
        }
    }

    public static synchronized void process(String url, BitmapDesiredOptions bitmapDesiredOptions, BitmapCallback callback) {
        synchronized (BitmapProcessor.class) {
            process(url, null, true, true, callback);
        }
    }

    public static synchronized void process(String url, BitmapDesiredOptions bitmapDesiredOptions, boolean useRamCache, boolean useDiskCache, BitmapCallback callback) {
        synchronized (BitmapProcessor.class) {
            process(url, null, true, true, 0, callback);
        }
    }

    public static synchronized void process(String url, BitmapDesiredOptions bitmapDesiredOptions, boolean useRamCache, boolean useDiskCache, long diskCacheTime, BitmapCallback callback) {
        boolean sameOpt;
        synchronized (BitmapProcessor.class) {
            if (url != null) {
                synchronized (reqList) {
                    int i = 0;
                    int size = reqList.size();
                    while (true) {
                        if (i < size) {
                            ImageReq req = reqList.get(i);
                            boolean sameUrl = req.url.equals(url);
                            if (!(req.bitmapDesiredOptions == null && bitmapDesiredOptions == null) && (req.bitmapDesiredOptions == null || !req.bitmapDesiredOptions.equals(bitmapDesiredOptions))) {
                                sameOpt = false;
                            } else {
                                sameOpt = true;
                            }
                            if (!sameUrl || !sameOpt) {
                                i++;
                            } else {
                                if (callback != null && req.callbacks.indexOf(callback) == -1) {
                                    req.callbacks.add(callback);
                                }
                                start();
                            }
                        } else {
                            ImageReq req2 = new ImageReq();
                            req2.url = url;
                            req2.bitmapDesiredOptions = bitmapDesiredOptions;
                            req2.useRamCache = useRamCache;
                            req2.diskCacheTime = diskCacheTime;
                            req2.useDiskCache = useDiskCache;
                            if (callback != null) {
                                req2.callbacks.add(callback);
                            }
                            synchronized (reqList) {
                                reqList.add(req2);
                                if (reqList.size() > OVERFLOW_SIZE) {
                                    while (reqList.size() > 100) {
                                        reqList.remove(0);
                                    }
                                }
                            }
                            start();
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static String getCacheKey(String url, BitmapDesiredOptions bitmapDesiredOptions) {
        return bitmapDesiredOptions == null ? url : url + bitmapDesiredOptions.toString();
    }

    public static Bitmap getBitmapFromCache(String url) {
        return getBitmapFromCache(url, null);
    }

    public static Bitmap getBitmapFromCache(String url, BitmapDesiredOptions bitmapDesiredOptions) {
        if (cachePool == null || url == null || cachePool.get(getCacheKey(url, bitmapDesiredOptions)) == null) {
            return null;
        }
        return cachePool.get(getCacheKey(url, bitmapDesiredOptions)).get();
    }

    public static void removeBitmapFromRamCache(String url, BitmapDesiredOptions bitmapDesiredOptions) {
        if (cachePool != null) {
            cachePool.put(getCacheKey(url, bitmapDesiredOptions), null);
        }
    }

    public static void deleteCachedFile(String url, BitmapDesiredOptions bitmapDesiredOptions) {
        removeBitmapFromRamCache(url, bitmapDesiredOptions);
        try {
            new File(cacheDir, Data.MD5(url)).delete();
        } catch (Throwable th) {
        }
    }

    /* access modifiers changed from: private */
    public static class ManagerThread implements Handler.Callback {
        private Handler handler;

        public ManagerThread() {
            MobHandlerThread thread = new MobHandlerThread() { // from class:  com.xzy.forestSystem.mob.tools.gui.BitmapProcessor.ManagerThread.1
                @Override //  com.xzy.forestSystem.mob.tools.MobHandlerThread, java.lang.Thread, java.lang.Runnable
                public void run() {
                    ManagerThread.this.beforeRun();
                    super.run();
                }
            };
            thread.start();
            this.handler = new Handler(thread.getLooper(), this);
            this.handler.sendEmptyMessageDelayed(1, 20000);
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void beforeRun() {
            int i = 0;
            while (i < BitmapProcessor.workerList.length) {
                if (BitmapProcessor.workerList[i] == null) {
                    BitmapProcessor.workerList[i] = new WorkerThread();
                    BitmapProcessor.workerList[i].setName("worker " + i);
                    BitmapProcessor.workerList[i].localType = i == 0;
                    BitmapProcessor.workerList[i].start();
                }
                i++;
            }
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message msg) {
            if (BitmapProcessor.cachePool != null) {
                BitmapProcessor.cachePool.trimBeforeTime(System.currentTimeMillis() - 60000);
            }
            MobLog.getInstance().m70d(">>>> BitmapProcessor.cachePool: " + (BitmapProcessor.cachePool == null ? 0 : BitmapProcessor.cachePool.size()), new Object[0]);
            MobLog.getInstance().m70d(">>>> BitmapProcessor.reqList: " + (BitmapProcessor.reqList == null ? 0 : BitmapProcessor.reqList.size()), new Object[0]);
            if (BitmapProcessor.work) {
                this.handler.sendEmptyMessageDelayed(1, 20000);
            }
            return false;
        }

        public void quit() {
            this.handler.removeMessages(1);
            this.handler.getLooper().quit();
            for (int i = 0; i < BitmapProcessor.workerList.length; i++) {
                if (BitmapProcessor.workerList[i] != null) {
                    BitmapProcessor.workerList[i].interrupt();
                    BitmapProcessor.workerList[i] = null;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static class WorkerThread extends Thread {
        private ImageReq curReq;
        private boolean localType;

        private WorkerThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (BitmapProcessor.work) {
                try {
                    if (this.localType) {
                        doLocalTask();
                    } else {
                        doNetworkTask();
                    }
                } catch (Throwable t) {
                    MobLog.getInstance().m57w(t);
                }
            }
        }

        private void doLocalTask() throws Throwable {
            ImageReq req = null;
            synchronized (BitmapProcessor.reqList) {
                if (BitmapProcessor.reqList.size() > 0) {
                    req = (ImageReq) BitmapProcessor.reqList.remove(0);
                }
            }
            if (req != null) {
                Bitmap bm = null;
                if (req.useRamCache) {
                    SoftReference<Bitmap> ref = (SoftReference) BitmapProcessor.cachePool.get(BitmapProcessor.getCacheKey(req.url, req.bitmapDesiredOptions));
                    bm = ref == null ? null : ref.get();
                }
                if (bm != null) {
                    this.curReq = req;
                    this.curReq.worker = this;
                    req.throwComplete(bm);
                } else if (!req.useDiskCache || BitmapProcessor.cacheDir == null || !new File(BitmapProcessor.cacheDir, Data.MD5(req.url)).exists()) {
                    synchronized (BitmapProcessor.reqList) {
                        if (BitmapProcessor.netReqTPS.size() > 100) {
                            synchronized (BitmapProcessor.reqList) {
                                while (BitmapProcessor.reqList.size() > 0) {
                                    BitmapProcessor.reqList.remove(0);
                                }
                            }
                            BitmapProcessor.netReqTPS.remove(0);
                        }
                    }
                    BitmapProcessor.netReqTPS.add(req);
                } else {
                    doTask(req);
                }
            } else {
                try {
                    Thread.sleep(30);
                } catch (Throwable th) {
                }
            }
        }

        private void doNetworkTask() throws Throwable {
            ImageReq req = null;
            synchronized (BitmapProcessor.netReqTPS) {
                if (BitmapProcessor.netReqTPS.size() > 0) {
                    req = (ImageReq) BitmapProcessor.netReqTPS.remove(0);
                }
            }
            if (req == null) {
                synchronized (BitmapProcessor.reqList) {
                    if (BitmapProcessor.reqList.size() > 0) {
                        req = (ImageReq) BitmapProcessor.reqList.remove(0);
                    }
                }
            }
            if (req != null) {
                Bitmap bm = null;
                if (req.useRamCache) {
                    SoftReference<Bitmap> ref = (SoftReference) BitmapProcessor.cachePool.get(BitmapProcessor.getCacheKey(req.url, req.bitmapDesiredOptions));
                    bm = ref == null ? null : ref.get();
                }
                if (bm != null) {
                    this.curReq = req;
                    this.curReq.worker = this;
                    req.throwComplete(bm);
                    return;
                }
                doTask(req);
                return;
            }
            try {
                Thread.sleep(30);
            } catch (Throwable th) {
            }
        }

        private void doTask(final ImageReq req) throws Throwable {
            Bitmap bm;
            try {
                this.curReq = req;
                this.curReq.worker = this;
                final String md5 = Data.MD5(req.url);
                File cacheFile = new File(BitmapProcessor.cacheDir, md5);
                if (req.useDiskCache && req.diskCacheTime > 0 && cacheFile.exists()) {
                    long lastModified = cacheFile.lastModified();
                    if (req.diskCacheTime + lastModified < System.currentTimeMillis()) {
                        cacheFile.delete();
                    }
                }
                if (!req.useDiskCache || BitmapProcessor.cacheDir == null || !cacheFile.exists()) {
                    new NetworkHelper().rawGet(req.url, new RawNetworkCallback() { // from class:  com.xzy.forestSystem.mob.tools.gui.BitmapProcessor.WorkerThread.1
                        @Override //  com.xzy.forestSystem.mob.tools.network.RawNetworkCallback
                        public void onResponse(InputStream is) throws Throwable {
                            Bitmap bitmap;
                            PatchInputStream pis = new PatchInputStream(is);
                            if (BitmapProcessor.cacheDir != null) {
                                File file = new File(BitmapProcessor.cacheDir, md5);
                                WorkerThread.this.saveFile(pis, file);
                                if (req.bitmapDesiredOptions == null || req.bitmapDesiredOptions.equals("")) {
                                    bitmap = BitmapHelper.getBitmap(file, 1);
                                } else {
                                    bitmap = BitmapHelper.getBitmapByCompressQuality(file.getAbsolutePath(), req.bitmapDesiredOptions.desiredWidth, req.bitmapDesiredOptions.desiredHeight, req.bitmapDesiredOptions.quality, req.bitmapDesiredOptions.maxBytes);
                                }
                                if (!req.useDiskCache) {
                                    file.delete();
                                }
                            } else {
                                bitmap = BitmapHelper.getBitmap(pis, 1);
                            }
                            if (bitmap == null || bitmap.isRecycled()) {
                                req.throwError();
                            } else {
                                if (req.useRamCache) {
                                    BitmapProcessor.cachePool.put(BitmapProcessor.getCacheKey(req.url, req.bitmapDesiredOptions), new SoftReference(bitmap));
                                }
                                req.throwComplete(bitmap);
                            }
                            WorkerThread.this.curReq = null;
                        }
                    }, BitmapProcessor.timeout);
                    return;
                }
                if (req.bitmapDesiredOptions == null || req.bitmapDesiredOptions.equals("")) {
                    bm = BitmapHelper.getBitmap(cacheFile.getAbsolutePath());
                } else {
                    bm = BitmapHelper.getBitmapByCompressQuality(new File(BitmapProcessor.cacheDir, md5).getAbsolutePath(), req.bitmapDesiredOptions.desiredWidth, req.bitmapDesiredOptions.desiredHeight, req.bitmapDesiredOptions.quality, req.bitmapDesiredOptions.maxBytes);
                }
                if (bm != null) {
                    if (req.useRamCache) {
                        BitmapProcessor.cachePool.put(BitmapProcessor.getCacheKey(req.url, req.bitmapDesiredOptions), new SoftReference(bm));
                    }
                    req.throwComplete(bm);
                } else {
                    req.throwError();
                }
                this.curReq = null;
            } catch (Throwable t) {
                MobLog.getInstance().m57w(t);
                req.throwError();
                this.curReq = null;
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        /* JADX WARNING: Removed duplicated region for block: B:24:0x004b A[Catch:{ all -> 0x0057 }] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private void saveFile(java.io.InputStream r8, java.io.File r9) {
            /*
                r7 = this;
                r0 = 0
                boolean r5 = r9.exists()     // Catch:{ Throwable -> 0x0044 }
                if (r5 == 0) goto L_0x000a
                r9.delete()     // Catch:{ Throwable -> 0x0044 }
            L_0x000a:
                java.io.File r5 = r9.getParentFile()     // Catch:{ Throwable -> 0x0044 }
                boolean r5 = r5.exists()     // Catch:{ Throwable -> 0x0044 }
                if (r5 != 0) goto L_0x001b
                java.io.File r5 = r9.getParentFile()     // Catch:{ Throwable -> 0x0044 }
                r5.mkdirs()     // Catch:{ Throwable -> 0x0044 }
            L_0x001b:
                r9.createNewFile()     // Catch:{ Throwable -> 0x0044 }
                java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x0044 }
                r1.<init>(r9)     // Catch:{ Throwable -> 0x0044 }
                r5 = 256(0x100, float:3.59E-43)
                byte[] r4 = new byte[r5]     // Catch:{ Throwable -> 0x0064, all -> 0x0061 }
                int r2 = r8.read(r4)     // Catch:{ Throwable -> 0x0064, all -> 0x0061 }
            L_0x002b:
                if (r2 <= 0) goto L_0x0036
                r5 = 0
                r1.write(r4, r5, r2)     // Catch:{ Throwable -> 0x0064, all -> 0x0061 }
                int r2 = r8.read(r4)     // Catch:{ Throwable -> 0x0064, all -> 0x0061 }
                goto L_0x002b
            L_0x0036:
                r1.flush()     // Catch:{ Throwable -> 0x0064, all -> 0x0061 }
                r1.close()     // Catch:{ Throwable -> 0x0041 }
                r8.close()     // Catch:{ Throwable -> 0x0041 }
                r0 = r1
            L_0x0040:
                return
            L_0x0041:
                r5 = move-exception
                r0 = r1
                goto L_0x0040
            L_0x0044:
                r3 = move-exception
            L_0x0045:
                boolean r5 = r9.exists()     // Catch:{ all -> 0x0057 }
                if (r5 == 0) goto L_0x004e
                r9.delete()     // Catch:{ all -> 0x0057 }
            L_0x004e:
                r0.close()     // Catch:{ Throwable -> 0x0055 }
                r8.close()     // Catch:{ Throwable -> 0x0055 }
                goto L_0x0040
            L_0x0055:
                r5 = move-exception
                goto L_0x0040
            L_0x0057:
                r5 = move-exception
            L_0x0058:
                r0.close()     // Catch:{ Throwable -> 0x005f }
                r8.close()     // Catch:{ Throwable -> 0x005f }
            L_0x005e:
                throw r5
            L_0x005f:
                r6 = move-exception
                goto L_0x005e
            L_0x0061:
                r5 = move-exception
                r0 = r1
                goto L_0x0058
            L_0x0064:
                r3 = move-exception
                r0 = r1
                goto L_0x0045
            */
            throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.gui.BitmapProcessor.WorkerThread.saveFile(java.io.InputStream, java.io.File):void");
        }

        @Override // java.lang.Thread
        public void interrupt() {
            try {
                super.interrupt();
            } catch (Throwable th) {
            }
        }
    }

    private static class PatchInputStream extends FilterInputStream {

        /* renamed from: in */
        InputStream f379in;

        protected PatchInputStream(InputStream in) {
            super(in);
            this.f379in = in;
        }

        @Override // java.io.FilterInputStream, java.io.InputStream
        public long skip(long n) throws IOException {
            long m = 0;
            while (m < n) {
                long mm = this.f379in.skip(n - m);
                if (mm == 0) {
                    break;
                }
                m += mm;
            }
            return m;
        }
    }

    public static class ImageReq {
        private BitmapDesiredOptions bitmapDesiredOptions;
        private ArrayList<BitmapCallback> callbacks = new ArrayList<>();
        private long diskCacheTime = 0;
        private long reqTime = System.currentTimeMillis();
        private String url;
        private boolean useDiskCache = true;
        private boolean useRamCache = true;
        private WorkerThread worker;

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void throwComplete(Bitmap bitmap) {
            Iterator<BitmapCallback> it = this.callbacks.iterator();
            while (it.hasNext()) {
                it.next().onImageGot(this.url, bitmap);
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void throwError() {
            Iterator<BitmapCallback> it = this.callbacks.iterator();
            while (it.hasNext()) {
                it.next().onImageGot(this.url, null);
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("url=").append(this.url);
            sb.append("time=").append(this.reqTime);
            sb.append("worker=").append(this.worker.getName()).append(" (").append(this.worker.getId()).append("");
            return sb.toString();
        }
    }

    public static class BitmapDesiredOptions {
        public int desiredHeight = 0;
        public int desiredWidth = 0;
        public long maxBytes = 0;
        public int quality = 0;

        public boolean equals(Object o) {
            return super.equals(o) || (o != null && o.toString().equals(toString()));
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (this.desiredWidth > 0) {
                sb.append(this.desiredWidth);
            }
            if (this.desiredHeight > 0) {
                sb.append(this.desiredHeight);
            }
            if (this.maxBytes > 0) {
                sb.append(this.maxBytes);
            }
            if (this.quality > 0) {
                sb.append(this.quality);
            }
            return sb.toString();
        }
    }
}
