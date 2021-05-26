package  com.xzy.forestSystem.mob.commons.clt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Parcelable;
import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.commons.CommonConfig;
import  com.xzy.forestSystem.mob.commons.DataHeap;
import  com.xzy.forestSystem.mob.commons.LockAction;
import  com.xzy.forestSystem.mob.commons.Locks;
import  com.xzy.forestSystem.mob.commons.ProcessLevelSPDB;
import  com.xzy.forestSystem.mob.tools.MobHandlerThread;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.proguard.PublicMemberKeeper;
import  com.xzy.forestSystem.mob.tools.utils.Data;
import  com.xzy.forestSystem.mob.tools.utils.DeviceHelper;
import  com.xzy.forestSystem.mob.tools.utils.FileLocker;
import  com.xzy.forestSystem.mob.tools.utils.Hashon;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class DvcClt implements Handler.Callback, PublicMemberKeeper {

    /* renamed from: a */
    private static DvcClt f332a;

    /* renamed from: b */
    private Hashon f333b = new Hashon();

    /* renamed from: c */
    private Handler f334c;

    /* renamed from: d */
    private Random f335d = new Random();

    /* renamed from: e */
    private BroadcastReceiver f336e;

    public static synchronized void startCollector() {
        synchronized (DvcClt.class) {
            if (f332a == null) {
                f332a = new DvcClt();
                f332a.m188a();
            }
        }
    }

    private DvcClt() {
    }

    /* renamed from: a */
    private void m188a() {
        MobHandlerThread r0 = new MobHandlerThread() { // from class:  com.xzy.forestSystem.mob.commons.clt.DvcClt.1
            @Override //  com.xzy.forestSystem.mob.tools.MobHandlerThread, java.lang.Thread, java.lang.Runnable
            public void run() {
                Locks.m141a(Locks.m140a("comm/locks/.dic_lock"), new LockAction() { // from class:  com.xzy.forestSystem.mob.commons.clt.DvcClt.1.1
                    @Override //  com.xzy.forestSystem.mob.commons.LockAction
                    public boolean run(FileLocker fileLocker) {
//                        this.m177a();
                        return false;
                    }
                });
            }

            /* access modifiers changed from: private */
            /* access modifiers changed from: public */
            /* renamed from: a */
            private void m177a() {
                super.run();
            }
        };
        r0.start();
        this.f334c = new Handler(r0.getLooper(), this);
        this.f334c.sendEmptyMessage(1);
        this.f334c.sendEmptyMessage(2);
        this.f334c.sendEmptyMessage(3);
        this.f334c.sendEmptyMessage(5);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0076, code lost:
        if (m178h() != false) goto L_0x0078;
     */
    @Override // android.os.Handler.Callback
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean handleMessage(android.os.Message r10) {
        /*
            r9 = this;
            r8 = 120(0x78, float:1.68E-43)
            r7 = 4
            r1 = 2
            r6 = 0
            int r0 = r10.what
            switch(r0) {
                case 1: goto L_0x000b;
                case 2: goto L_0x0015;
                case 3: goto L_0x0034;
                case 4: goto L_0x0057;
                case 5: goto L_0x0096;
                default: goto L_0x000a;
            }
        L_0x000a:
            return r6
        L_0x000b:
            boolean r0 =  com.xzy.forestSystem.mob.commons.CommonConfig.m235i()
            if (r0 == 0) goto L_0x000a
            r9.m185b()
            goto L_0x000a
        L_0x0015:
            boolean r0 =  com.xzy.forestSystem.mob.commons.CommonConfig.m230n()
            if (r0 == 0) goto L_0x0030
            boolean r0 = r9.m183c()
            if (r0 == 0) goto L_0x0024
            r9.m182d()
        L_0x0024:
            r9.m181e()
        L_0x0027:
            android.os.Handler r0 = r9.f334c
            r2 = 3600000(0x36ee80, double:1.7786363E-317)
            r0.sendEmptyMessageDelayed(r1, r2)
            goto L_0x000a
        L_0x0030:
            r9.m180f()
            goto L_0x0027
        L_0x0034:
            boolean r0 =  com.xzy.forestSystem.mob.commons.CommonConfig.m234j()
            if (r0 == 0) goto L_0x000a
            r9.m179g()     // Catch:{ Throwable -> 0x004e }
        L_0x003d:
            java.util.Random r0 = r9.f335d
            int r0 = r0.nextInt(r8)
            int r0 = r0 + 180
            android.os.Handler r1 = r9.f334c
            int r0 = r0 * 1000
            long r2 = (long) r0
            r1.sendEmptyMessageDelayed(r7, r2)
            goto L_0x000a
        L_0x004e:
            r0 = move-exception
             com.xzy.forestSystem.mob.tools.log.NLog r1 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            r1.m57w(r0)
            goto L_0x003d
        L_0x0057:
            boolean r0 =  com.xzy.forestSystem.mob.commons.CommonConfig.m234j()
            if (r0 == 0) goto L_0x000a
            long r0 =  com.xzy.forestSystem.mob.commons.CommonConfig.m246a()
            int r2 =  com.xzy.forestSystem.mob.commons.CommonConfig.m233k()
            long r2 = (long) r2
            r4 = 1000(0x3e8, double:4.94E-321)
            long r2 = r2 * r4
            long r0 = r0 + r2
            long r2 =  com.xzy.forestSystem.mob.commons.CommonConfig.m246a()
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 < 0) goto L_0x0078
            boolean r0 = r9.m178h()     // Catch:{ Throwable -> 0x008d }
            if (r0 == 0) goto L_0x007b
        L_0x0078:
            r9.m179g()     // Catch:{ Throwable -> 0x008d }
        L_0x007b:
            java.util.Random r0 = r9.f335d
            int r0 = r0.nextInt(r8)
            int r0 = r0 + 180
            android.os.Handler r1 = r9.f334c
            int r0 = r0 * 1000
            long r2 = (long) r0
            r1.sendEmptyMessageDelayed(r7, r2)
            goto L_0x000a
        L_0x008d:
            r0 = move-exception
             com.xzy.forestSystem.mob.tools.log.NLog r1 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            r1.m57w(r0)
            goto L_0x007b
        L_0x0096:
            boolean r0 =  com.xzy.forestSystem.mob.commons.CommonConfig.m232l()
            if (r0 == 0) goto L_0x00bc
            android.content.Context r0 =  com.xzy.forestSystem.mob.MobSDK.getContext()     // Catch:{ Throwable -> 0x00cb }
             com.xzy.forestSystem.mob.tools.utils.DeviceHelper r0 =  com.xzy.forestSystem.mob.tools.utils.DeviceHelper.getInstance(r0)     // Catch:{ Throwable -> 0x00cb }
            r1 = 30
            r2 = 0
            r3 = 1
            android.location.Location r1 = r0.getLocation(r1, r2, r3)     // Catch:{ Throwable -> 0x00cb }
            r2 = 1
            r9.m187a(r1, r2)     // Catch:{ Throwable -> 0x00cb }
            r1 = 15
            r2 = 0
            r3 = 1
            android.location.Location r0 = r0.getLocation(r1, r2, r3)     // Catch:{ Throwable -> 0x00cb }
            r1 = 2
            r9.m187a(r0, r1)     // Catch:{ Throwable -> 0x00cb }
        L_0x00bc:
            android.os.Handler r0 = r9.f334c
            r1 = 5
            int r2 =  com.xzy.forestSystem.mob.commons.CommonConfig.m231m()
            int r2 = r2 * 1000
            long r2 = (long) r2
            r0.sendEmptyMessageDelayed(r1, r2)
            goto L_0x000a
        L_0x00cb:
            r0 = move-exception
             com.xzy.forestSystem.mob.tools.log.NLog r1 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            r1.m57w(r0)
            goto L_0x00bc
            switch-data {1->0x000b, 2->0x0015, 3->0x0034, 4->0x0057, 5->0x0096, }
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.commons.clt.DvcClt.handleMessage(android.os.Message):boolean");
    }

    /* renamed from: b */
    private void m185b() {
        try {
            HashMap hashMap = new HashMap();
            DeviceHelper instance = DeviceHelper.getInstance(MobSDK.getContext());
            hashMap.put("phonename", instance.getBluetoothName());
            hashMap.put("signmd5", instance.getSignMD5());
            String MD5 = Data.MD5(this.f333b.fromHashMap(hashMap));
            String a = ProcessLevelSPDB.m139a();
            if (a == null || !a.equals(MD5)) {
                HashMap<String, Object> hashMap2 = new HashMap<>();
                hashMap2.put("type", "DEVEXT");
                hashMap2.put("data", hashMap);
                hashMap2.put("datetime", Long.valueOf(CommonConfig.m246a()));
                DataHeap.m205a().m204a(CommonConfig.m246a(), hashMap2);
                ProcessLevelSPDB.m137a(MD5);
            }
        } catch (Throwable th) {
            MobLog.getInstance().m57w(th);
        }
    }

    /* renamed from: c */
    private boolean m183c() {
        long b = ProcessLevelSPDB.m136b();
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(b);
        int i = instance.get(1);
        int i2 = instance.get(2);
        int i3 = instance.get(5);
        long a = CommonConfig.m246a();
        Calendar instance2 = Calendar.getInstance();
        instance2.setTimeInMillis(a);
        int i4 = instance2.get(1);
        int i5 = instance2.get(2);
        int i6 = instance2.get(5);
        if (i == i4 && i2 == i5 && i3 == i6) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: d */
    private void m182d() {
        synchronized (f332a) {
            try {
                HashMap hashMap = new HashMap();
                DeviceHelper instance = DeviceHelper.getInstance(MobSDK.getContext());
                hashMap.put("ssid", instance.getSSID());
                hashMap.put("bssid", instance.getBssid());
                HashMap<String, Object> hashMap2 = new HashMap<>();
                hashMap2.put("type", "WIFI_INFO");
                hashMap2.put("data", hashMap);
                long a = CommonConfig.m246a();
                hashMap2.put("datetime", Long.valueOf(a));
                DataHeap.m205a().m204a(CommonConfig.m246a(), hashMap2);
                ProcessLevelSPDB.m138a(a);
                ProcessLevelSPDB.m134b(Data.MD5(this.f333b.fromHashMap(hashMap)));
            } catch (Throwable th) {
                MobLog.getInstance().m57w(th);
            }
        }
    }

    /* renamed from: e */
    private void m181e() {
        if (this.f336e == null) {
            this.f336e = new BroadcastReceiver() { // from class:  com.xzy.forestSystem.mob.commons.clt.DvcClt.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    Parcelable parcelableExtra;
                    try {
                        if ("android.net.wifi.STATE_CHANGE".equals(intent.getAction()) && (parcelableExtra = intent.getParcelableExtra("networkInfo")) != null && ((NetworkInfo) parcelableExtra).isConnected()) {
                            HashMap hashMap = new HashMap();
                            DeviceHelper instance = DeviceHelper.getInstance(context);
                            hashMap.put("ssid", instance.getSSID());
                            hashMap.put("bssid", instance.getBssid());
                            String MD5 = Data.MD5(DvcClt.this.f333b.fromHashMap(hashMap));
                            String c = ProcessLevelSPDB.m133c();
                            if ((c == null || !c.equals(MD5)) && CommonConfig.m230n()) {
                                DvcClt.this.m182d();
                            }
                        }
                    } catch (Throwable th) {
                        MobLog.getInstance().m57w(th);
                    }
                }
            };
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.wifi.STATE_CHANGE");
        try {
            MobSDK.getContext().registerReceiver(this.f336e, intentFilter);
        } catch (Throwable th) {
        }
    }

    /* renamed from: f */
    private void m180f() {
        if (this.f336e != null) {
            try {
                MobSDK.getContext().unregisterReceiver(this.f336e);
            } catch (Throwable th) {
            }
            this.f336e = null;
        }
    }

    /* renamed from: g */
    private void m179g() throws Throwable {
        int i;
        DeviceHelper instance = DeviceHelper.getInstance(MobSDK.getContext());
        try {
            i = Integer.parseInt(instance.getCarrier());
        } catch (Throwable th) {
            i = -1;
        }
        int cellLac = instance.getCellLac();
        int cellId = instance.getCellId();
        if (!(i == -1 || cellLac == -1 || cellId == -1)) {
            HashMap hashMap = new HashMap();
            hashMap.put("carrier", Integer.valueOf(i));
            hashMap.put("simopname", instance.getCarrierName());
            hashMap.put("lac", Integer.valueOf(cellLac));
            hashMap.put("cell", Integer.valueOf(cellId));
            HashMap<String, Object> hashMap2 = new HashMap<>();
            hashMap2.put("type", "BSINFO");
            hashMap2.put("data", hashMap);
            hashMap2.put("datetime", Long.valueOf(CommonConfig.m246a()));
            DataHeap.m205a().m204a(CommonConfig.m246a(), hashMap2);
            ProcessLevelSPDB.m132c(Data.MD5(this.f333b.fromHashMap(hashMap)));
        }
        ProcessLevelSPDB.m135b(CommonConfig.m246a() + (((long) CommonConfig.m233k()) * 1000));
    }

    /* renamed from: h */
    private boolean m178h() throws Throwable {
        int i;
        DeviceHelper instance = DeviceHelper.getInstance(MobSDK.getContext());
        try {
            i = Integer.parseInt(instance.getCarrier());
        } catch (Throwable th) {
            i = -1;
        }
        int cellLac = instance.getCellLac();
        int cellId = instance.getCellId();
        if (i == -1 || cellLac == -1 || cellId == -1) {
            return false;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("carrier", Integer.valueOf(i));
        hashMap.put("simopname", instance.getCarrierName());
        hashMap.put("lac", Integer.valueOf(cellLac));
        hashMap.put("cell", Integer.valueOf(cellId));
        String MD5 = Data.MD5(this.f333b.fromHashMap(hashMap));
        String d = ProcessLevelSPDB.m131d();
        if (d == null || !d.equals(MD5)) {
            return true;
        }
        return false;
    }

    /* renamed from: a */
    private void m187a(Location location, int i) {
        if (location != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("accuracy", Float.valueOf(location.getAccuracy()));
            hashMap.put("latitude", Double.valueOf(location.getLatitude()));
            hashMap.put("longitude", Double.valueOf(location.getLongitude()));
            hashMap.put("location_type", Integer.valueOf(i));
            HashMap<String, Object> hashMap2 = new HashMap<>();
            hashMap2.put("type", "LOCATION");
            hashMap2.put("data", hashMap);
            hashMap2.put("datetime", Long.valueOf(CommonConfig.m246a()));
            DataHeap.m205a().m204a(CommonConfig.m246a(), hashMap2);
        }
    }
}
