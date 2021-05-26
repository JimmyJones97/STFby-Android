package  com.xzy.forestSystem.mob.commons;

import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;

import com.xzy.forestSystem.hellocharts.animation.ChartViewportAnimator;
import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.commons.authorize.Authorizer;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.network.KVPair;
import  com.xzy.forestSystem.mob.tools.network.NetworkHelper;
import  com.xzy.forestSystem.mob.tools.utils.Data;
import  com.xzy.forestSystem.mob.tools.utils.DeviceHelper;
import  com.xzy.forestSystem.mob.tools.utils.Hashon;
import  com.xzy.forestSystem.mob.tools.utils.ResHelper;
import java.util.ArrayList;
import java.util.HashMap;

/* renamed from:  com.xzy.forestSystem.mob.commons.a */
public class CommonConfig {

    /* renamed from: a */
    private static HashMap<String, Object> f313a;

    /* renamed from: b */
    private static long f314b;

    /* renamed from: c */
    private static long f315c;

    /* renamed from: d */
    private static boolean f316d;

    /* renamed from: a */
    public static long m246a() {
        long j;
        m225s();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        try {
            j = Long.valueOf(String.valueOf(f313a.get("deviceTime"))).longValue();
        } catch (Throwable th) {
            j = 0;
        }
        return ((Long) ResHelper.forceCast(f313a.get("serverTime"), 0L)).longValue() + (elapsedRealtime - j);
    }

    /* renamed from: b */
    public static boolean m243b() {
        m225s();
        return 1 == ((Integer) ResHelper.forceCast(f313a.get("rt"), 0)).intValue();
    }

    /* renamed from: c */
    public static int m241c() {
        m225s();
        return ((Integer) ResHelper.forceCast(f313a.get("rtsr"), Integer.valueOf((int) ChartViewportAnimator.FAST_ANIMATION_DURATION))).intValue();
    }

    /* renamed from: d */
    public static boolean m240d() {
        m225s();
        return 1 == ((Integer) ResHelper.forceCast(f313a.get("in"), 0)).intValue();
    }

    /* renamed from: e */
    public static boolean m239e() {
        m225s();
        return 1 == ((Integer) ResHelper.forceCast(f313a.get("all"), 0)).intValue();
    }

    /* renamed from: f */
    public static boolean m238f() {
        m225s();
        return 1 == ((Integer) ResHelper.forceCast(f313a.get("un"), 0)).intValue();
    }

    /* renamed from: g */
    public static long m237g() {
        m225s();
        return ((Long) ResHelper.forceCast(f313a.get("aspa"), 2592000L)).longValue();
    }

    /* renamed from: h */
    public static boolean m236h() {
        m225s();
        return 1 == ((Integer) ResHelper.forceCast(f313a.get("di"), 0)).intValue();
    }

    /* renamed from: i */
    public static boolean m235i() {
        m225s();
        return 1 == ((Integer) ResHelper.forceCast(f313a.get("ext"), 0)).intValue();
    }

    /* renamed from: j */
    public static boolean m234j() {
        m225s();
        return 1 == ((Integer) ResHelper.forceCast(f313a.get("bs"), 0)).intValue();
    }

    /* renamed from: k */
    public static int m233k() {
        m225s();
        return ((Integer) ResHelper.forceCast(f313a.get("bsgap"), 86400)).intValue();
    }

    /* renamed from: l */
    public static boolean m232l() {
        m225s();
        return 1 == ((Integer) ResHelper.forceCast(f313a.get("l"), 0)).intValue();
    }

    /* renamed from: m */
    public static int m231m() {
        m225s();
        return ((Integer) ResHelper.forceCast(f313a.get("lgap"), 86400)).intValue();
    }

    /* renamed from: n */
    public static boolean m230n() {
        m225s();
        return 1 == ((Integer) ResHelper.forceCast(f313a.get("wi"), 0)).intValue();
    }

    /* renamed from: o */
    public static long m229o() {
        m225s();
        return (((long) ((Integer) ResHelper.forceCast(f313a.get("adle"), 172800)).intValue()) * 1000) + m246a();
    }

    /* renamed from: p */
    public static long m228p() {
        m225s();
        return ((long) ((Integer) ResHelper.forceCast(f313a.get("rtgap"), 86400)).intValue()) * 1000;
    }

    /* renamed from: s */
    private static synchronized void m225s() {
        synchronized (CommonConfig.class) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (f313a == null) {
                if (m224t()) {
                    f314b = elapsedRealtime;
                }
            } else if (elapsedRealtime - f314b >= 60000 && m223u()) {
                f314b = elapsedRealtime;
            }
        }
    }

    /* renamed from: t */
    private static boolean m224t() {
        String v = m222v();
        if (TextUtils.isEmpty(v)) {
            m221w();
            return false;
        }
        m242b(v);
        ProcessLevelSPDB.m130d(new Hashon().fromHashMap(f313a));
        return true;
    }

    /* renamed from: u */
    private static boolean m223u() {
        String e = ProcessLevelSPDB.m129e();
        if (TextUtils.isEmpty(e)) {
            return m224t();
        }
        m242b(e);
        if (((Long) ResHelper.forceCast(f313a.get("timestamp"), 0L)).longValue() - f315c >= 86400000) {
            m220x();
        }
        return true;
    }

    /* access modifiers changed from: private */
    /* renamed from: v */
    public static String m222v() {
        HashMap fromJson;
        try {
            NetworkHelper networkHelper = new NetworkHelper();
            ArrayList<MobProduct> products = MobProductCollector.getProducts();
            if (products.isEmpty()) {
                return null;
            }
            DeviceHelper instance = DeviceHelper.getInstance(MobSDK.getContext());
            ArrayList<KVPair<String>> arrayList = new ArrayList<>();
            arrayList.add(new KVPair<>("appkey", MobSDK.getAppkey()));
            arrayList.add(new KVPair<>("plat", String.valueOf(instance.getPlatformCode())));
            arrayList.add(new KVPair<>("apppkg", instance.getPackageName()));
            arrayList.add(new KVPair<>("appver", instance.getAppVersionName()));
            arrayList.add(new KVPair<>("networktype", instance.getDetailNetworkTypeForStatic()));
            String a = new Authorizer().m210a(true);
            if (!TextUtils.isEmpty(a)) {
                arrayList.add(new KVPair<>("duid", a));
            }
            NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
            networkTimeOut.readTimout = 30000;
            networkTimeOut.connectionTimeout = 30000;
            ArrayList<KVPair<String>> arrayList2 = new ArrayList<>();
            arrayList2.add(new KVPair<>("User-Identity", MobProductCollector.getUserIdentity(products)));
            String httpGet = networkHelper.httpGet(m219y(), arrayList, arrayList2, networkTimeOut);
            Hashon hashon = new Hashon();
            HashMap fromJson2 = hashon.fromJson(httpGet);
            if (fromJson2 == null) {
                return null;
            }
            if (!"200".equals(String.valueOf(fromJson2.get("status")))) {
                ProcessLevelSPDB.m128e(null);
                ProcessLevelSPDB.m126f(null);
                throw new Throwable("response is illegal: " + httpGet);
            }
            String str = (String) ResHelper.forceCast(fromJson2.get("sr"));
            if (!(str == null || (fromJson = hashon.fromJson(Data.AES128Decode("FYsAXMqlWJLCDpnc", Base64.decode(str, 2)))) == null)) {
                HashMap hashMap = (HashMap) ResHelper.forceCast(fromJson.get("cdata"));
                if (hashMap != null) {
                    String str2 = (String) ResHelper.forceCast(hashMap.get("host"));
                    int intValue = ((Integer) ResHelper.forceCast(hashMap.get("httpport"), 0)).intValue();
                    String str3 = (String) ResHelper.forceCast(hashMap.get("path"));
                    if (str2 == null || intValue == 0 || str3 == null) {
                        ProcessLevelSPDB.m128e(null);
                    } else {
                        ProcessLevelSPDB.m128e("http://" + str2 + ":" + intValue + str3);
                    }
                } else {
                    ProcessLevelSPDB.m128e(null);
                }
                HashMap hashMap2 = (HashMap) ResHelper.forceCast(fromJson.get("cconf"));
                if (hashMap2 != null) {
                    String str4 = (String) ResHelper.forceCast(hashMap2.get("host"));
                    int intValue2 = ((Integer) ResHelper.forceCast(hashMap2.get("httpport"), 0)).intValue();
                    String str5 = (String) ResHelper.forceCast(hashMap2.get("path"));
                    if (str4 == null || intValue2 == 0 || str5 == null) {
                        ProcessLevelSPDB.m126f(null);
                    } else {
                        ProcessLevelSPDB.m126f("http://" + str4 + ":" + intValue2 + str5);
                    }
                } else {
                    ProcessLevelSPDB.m126f(null);
                }
            }
            String str6 = (String) ResHelper.forceCast(fromJson2.get("sc"));
            if (str6 == null) {
                throw new Throwable("response is illegal: " + httpGet);
            }
            HashMap fromJson3 = hashon.fromJson(Data.AES128Decode("FYsAXMqlWJLCDpnc", Base64.decode(str6, 2)));
            if (fromJson3 == null) {
                throw new Throwable("response is illegal: " + httpGet);
            }
            long longValue = ((Long) ResHelper.forceCast(fromJson2.get("timestamp"), 0L)).longValue();
            fromJson3.put("deviceTime", Long.valueOf(SystemClock.elapsedRealtime()));
            fromJson3.put("serverTime", Long.valueOf(longValue));
            return hashon.fromHashMap(fromJson3);
        } catch (Throwable th) {
            ProcessLevelSPDB.m128e(null);
            ProcessLevelSPDB.m126f(null);
            MobLog.getInstance().m57w(th);
            return null;
        }
    }

    /* renamed from: w */
    private static void m221w() {
        f313a = new HashMap<>();
        f313a.put("in", 0);
        f313a.put("all", 0);
        f313a.put("aspa", 2592000L);
        f313a.put("un", 0);
        f313a.put("rt", 0);
        f313a.put("rtsr", 300000);
        f313a.put("mi", 0);
        f313a.put("ext", 0);
        f313a.put("bs", 0);
        f313a.put("bsgap", 86400);
        f313a.put("di", 0);
        f313a.put("l", 0);
        f313a.put("lgap", 86400);
        f313a.put("wi", 0);
        f313a.put("adle", 172800);
        f313a.put("rtgap", 86400);
    }

    /* access modifiers changed from: private */
    /* renamed from: b */
    public static void m242b(String str) {
        try {
            f313a = new Hashon().fromJson(str);
        } catch (Throwable th) {
            MobLog.getInstance().m57w(th);
        }
    }

    /* renamed from: x */
    private static void m220x() {
        if (!f316d) {
            f316d = true;
            new Thread() { // from class:  com.xzy.forestSystem.mob.commons.a.1
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    String v = CommonConfig.m222v();
                    if (!TextUtils.isEmpty(v)) {
                        CommonConfig.m242b(v);
                        ProcessLevelSPDB.m130d(new Hashon().fromHashMap(CommonConfig.f313a));
                    }
                    boolean unused = CommonConfig.f316d = false;
                }
            }.start();
        }
    }

    /* renamed from: y */
    private static String m219y() {
        String str = null;
        try {
            str = ProcessLevelSPDB.m125g();
        } catch (Throwable th) {
            MobLog.getInstance().m57w(th);
        }
        return TextUtils.isEmpty(str) ? "http://m.data.mob.com/v3/cconf" : str;
    }
}
