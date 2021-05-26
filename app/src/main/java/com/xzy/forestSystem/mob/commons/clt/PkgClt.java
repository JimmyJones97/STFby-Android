package  com.xzy.forestSystem.mob.commons.clt;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.commons.CommonConfig;
import  com.xzy.forestSystem.mob.commons.DataHeap;
import  com.xzy.forestSystem.mob.commons.LockAction;
import  com.xzy.forestSystem.mob.commons.Locks;
import  com.xzy.forestSystem.mob.tools.MobHandlerThread;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.proguard.PublicMemberKeeper;
import  com.xzy.forestSystem.mob.tools.utils.DeviceHelper;
import  com.xzy.forestSystem.mob.tools.utils.FileLocker;
import  com.xzy.forestSystem.mob.tools.utils.Hashon;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class PkgClt implements PublicMemberKeeper {

    /* renamed from: a */
    private static final String[] f340a = {"android.intent.action.PACKAGE_ADDED", "android.intent.action.PACKAGE_CHANGED", "android.intent.action.PACKAGE_REMOVED", "android.intent.action.PACKAGE_REPLACED"};

    /* renamed from: b */
    private static PkgClt f341b;

    /* renamed from: c */
    private BroadcastReceiver f342c;

    /* renamed from: d */
    private Hashon f343d = new Hashon();

    /* renamed from: e */
    private Handler f344e;

    public static synchronized void startCollector() {
        synchronized (PkgClt.class) {
            if (f341b == null) {
                f341b = new PkgClt();
                f341b.m175a();
            }
        }
    }

    private PkgClt() {
    }

    /* renamed from: a */
    private void m175a() {
        MobHandlerThread r0 = new MobHandlerThread() { // from class:  com.xzy.forestSystem.mob.commons.clt.PkgClt.1
            @Override //  com.xzy.forestSystem.mob.tools.MobHandlerThread, java.lang.Thread, java.lang.Runnable
            public void run() {
                Locks.m141a(Locks.m140a("comm/locks/.pkg_lock"), new LockAction() { // from class:  com.xzy.forestSystem.mob.commons.clt.PkgClt.1.1
                    @Override //  com.xzy.forestSystem.mob.commons.LockAction
                    public boolean run(FileLocker fileLocker) {
                        if (CommonConfig.m239e()) {
                            PkgClt.this.m167b();
                        }
//                        C01981.this.m159a();
                        return false;
                    }
                });
            }

            /* access modifiers changed from: private */
            /* access modifiers changed from: public */
            /* renamed from: a */
            private void m159a() {
                super.run();
            }
        };
        r0.start();
        this.f344e = new Handler(r0.getLooper(), new Handler.Callback() { // from class:  com.xzy.forestSystem.mob.commons.clt.PkgClt.2
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 1:
                        PkgClt.this.m160f();
                        return false;
                    case 2:
                        PkgClt.this.m161e();
                        return false;
                    default:
                        return false;
                }
            }
        });
        this.f344e.sendEmptyMessage(2);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: b */
    private void m167b() {
        ArrayList<HashMap<String, String>> arrayList;
        ArrayList<HashMap<String, String>> arrayList2;
        ArrayList<HashMap<String, String>> c = m165c();
        if (c == null || c.isEmpty()) {
            try {
                arrayList = DeviceHelper.getInstance(MobSDK.getContext()).getInstalledApp(false);
            } catch (Throwable th) {
                MobLog.getInstance().m57w(th);
                arrayList = new ArrayList<>();
            }
            m173a(CommonConfig.m229o(), "APPS_ALL", arrayList);
            m169a(arrayList);
            m174a(CommonConfig.m246a() + (CommonConfig.m237g() * 1000));
            return;
        }
        long a = CommonConfig.m246a();
        if (a >= m163d()) {
            try {
                arrayList2 = DeviceHelper.getInstance(MobSDK.getContext()).getInstalledApp(false);
            } catch (Throwable th2) {
                MobLog.getInstance().m57w(th2);
                arrayList2 = new ArrayList<>();
            }
            m173a(CommonConfig.m229o(), "APPS_ALL", arrayList2);
            m169a(arrayList2);
            m174a((CommonConfig.m237g() * 1000) + a);
            return;
        }
        m160f();
    }

    /* renamed from: c */
    private ArrayList<HashMap<String, String>> m165c() {
        File a = Locks.m140a("comm/dbs/.al");
        if (a.exists()) {
            try {
                ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(a)), "utf-8"));
                for (String readLine = bufferedReader.readLine(); readLine != null; readLine = bufferedReader.readLine()) {
                    HashMap<String, String> fromJson = this.f343d.fromJson(readLine);
                    if (fromJson != null) {
                        arrayList.add(fromJson);
                    }
                }
                bufferedReader.close();
                return arrayList;
            } catch (Throwable th) {
                MobLog.getInstance().m69d(th);
            }
        }
        return new ArrayList<>();
    }

    /* renamed from: a */
    private void m173a(long j, String str, ArrayList<HashMap<String, String>> arrayList) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", str);
        hashMap.put("list", arrayList);
        hashMap.put("datetime", Long.valueOf(CommonConfig.m246a()));
        DataHeap.m205a().m204a(j, hashMap);
    }

    /* renamed from: a */
    private void m169a(ArrayList<HashMap<String, String>> arrayList) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(Locks.m140a("comm/dbs/.al"))), "utf-8");
            Iterator<HashMap<String, String>> it = arrayList.iterator();
            while (it.hasNext()) {
                outputStreamWriter.append((CharSequence) this.f343d.fromHashMap(it.next())).append('\n');
            }
            outputStreamWriter.flush();
            outputStreamWriter.close();
        } catch (Throwable th) {
            MobLog.getInstance().m69d(th);
        }
    }

    /* renamed from: a */
    private void m174a(long j) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(Locks.m140a("comm/dbs/.nulal")));
            dataOutputStream.writeLong(j);
            dataOutputStream.flush();
            dataOutputStream.close();
        } catch (Throwable th) {
            MobLog.getInstance().m69d(th);
        }
    }

    /* renamed from: d */
    private long m163d() {
        File a = Locks.m140a("comm/dbs/.nulal");
        if (a.exists()) {
            try {
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(a));
                long readLong = dataInputStream.readLong();
                dataInputStream.close();
                return readLong;
            } catch (Throwable th) {
                MobLog.getInstance().m69d(th);
            }
        }
        return 0;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: e */
    private void m161e() {
        if (CommonConfig.m240d() && CommonConfig.m238f()) {
            if (this.f342c == null) {
                this.f342c = new BroadcastReceiver() { // from class:  com.xzy.forestSystem.mob.commons.clt.PkgClt.3
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(Context context, Intent intent) {
                        String str = null;
                        if (intent != null) {
                            str = intent.getAction();
                        }
                        if (PkgClt.this.m170a(str) && PkgClt.this.f344e != null) {
                            PkgClt.this.f344e.removeMessages(1);
                            PkgClt.this.f344e.sendEmptyMessageDelayed(1, 5000);
                        }
                    }
                };
            }
            IntentFilter intentFilter = new IntentFilter();
            for (int i = 0; i < f340a.length; i++) {
                intentFilter.addAction(f340a[i]);
            }
            intentFilter.addDataScheme("package");
            try {
                MobSDK.getContext().registerReceiver(this.f342c, intentFilter);
            } catch (Throwable th) {
            }
        } else if (this.f342c != null) {
            this.f344e.removeMessages(1);
            try {
                MobSDK.getContext().unregisterReceiver(this.f342c);
            } catch (Throwable th2) {
            }
            this.f342c = null;
        }
        this.f344e.sendEmptyMessageDelayed(2, 3600000);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: a */
    private boolean m170a(String str) {
        for (String str2 : f340a) {
            if (str2.equals(str)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: f */
    private void m160f() {
        ArrayList<HashMap<String, String>> arrayList;
        ArrayList<HashMap<String, String>> c = m165c();
        try {
            arrayList = DeviceHelper.getInstance(MobSDK.getContext()).getInstalledApp(false);
        } catch (Throwable th) {
            MobLog.getInstance().m57w(th);
            arrayList = new ArrayList<>();
        }
        if (c == null || c.isEmpty()) {
            m173a(CommonConfig.m229o(), "APPS_ALL", arrayList);
            m169a(arrayList);
            m174a(CommonConfig.m246a() + (CommonConfig.m237g() * 1000));
            return;
        }
        ArrayList<HashMap<String, String>> a = m168a(arrayList, c);
        if (!a.isEmpty()) {
            m173a(CommonConfig.m246a(), "APPS_INCR", a);
        }
        ArrayList<HashMap<String, String>> a2 = m168a(c, arrayList);
        if (!a2.isEmpty()) {
            m173a(CommonConfig.m246a(), "UNINSTALL", a2);
        }
        m169a(arrayList);
        m174a(CommonConfig.m246a() + (CommonConfig.m237g() * 1000));
    }

    /* renamed from: a */
    private ArrayList<HashMap<String, String>> m168a(ArrayList<HashMap<String, String>> arrayList, ArrayList<HashMap<String, String>> arrayList2) {
        boolean z;
        ArrayList<HashMap<String, String>> arrayList3 = new ArrayList<>();
        Iterator<HashMap<String, String>> it = arrayList.iterator();
        while (it.hasNext()) {
            HashMap<String, String> next = it.next();
            String str = next.get("pkg");
            if (!TextUtils.isEmpty(str)) {
                Iterator<HashMap<String, String>> it2 = arrayList2.iterator();
                while (true) {
                    if (it2.hasNext()) {
                        if (str.equals(it2.next().get("pkg"))) {
                            z = true;
                            break;
                        }
                    } else {
                        z = false;
                        break;
                    }
                }
                if (!z) {
                    arrayList3.add(next);
                }
            }
        }
        return arrayList3;
    }
}
