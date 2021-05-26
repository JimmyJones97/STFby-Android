package  com.xzy.forestSystem.mob.commons;

import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.tools.utils.SharePrefrenceHelper;

/* renamed from:  com.xzy.forestSystem.mob.commons.e */
public class ProcessLevelSPDB {

    /* renamed from: a */
    private static SharePrefrenceHelper f357a;

    /* renamed from: h */
    private static synchronized void m124h() {
        synchronized (ProcessLevelSPDB.class) {
            if (f357a == null) {
                f357a = new SharePrefrenceHelper(MobSDK.getContext());
                f357a.open("mob_commons", 1);
            }
        }
    }

    /* renamed from: a */
    public static synchronized String m139a() {
        String string;
        synchronized (ProcessLevelSPDB.class) {
            m124h();
            string = f357a.getString("key_ext_info");
        }
        return string;
    }

    /* renamed from: a */
    public static synchronized void m137a(String str) {
        synchronized (ProcessLevelSPDB.class) {
            m124h();
            f357a.putString("key_ext_info", str);
        }
    }

    /* renamed from: b */
    public static synchronized long m136b() {
        long j;
        synchronized (ProcessLevelSPDB.class) {
            m124h();
            j = f357a.getLong("wifi_last_time");
        }
        return j;
    }

    /* renamed from: a */
    public static synchronized void m138a(long j) {
        synchronized (ProcessLevelSPDB.class) {
            m124h();
            f357a.putLong("wifi_last_time", Long.valueOf(j));
        }
    }

    /* renamed from: c */
    public static synchronized String m133c() {
        String string;
        synchronized (ProcessLevelSPDB.class) {
            m124h();
            string = f357a.getString("wifi_last_info");
        }
        return string;
    }

    /* renamed from: b */
    public static synchronized void m134b(String str) {
        synchronized (ProcessLevelSPDB.class) {
            m124h();
            f357a.putString("wifi_last_info", str);
        }
    }

    /* renamed from: c */
    public static synchronized void m132c(String str) {
        synchronized (ProcessLevelSPDB.class) {
            m124h();
            f357a.putString("key_cellinfo", str);
        }
    }

    /* renamed from: d */
    public static synchronized String m131d() {
        String string;
        synchronized (ProcessLevelSPDB.class) {
            m124h();
            string = f357a.getString("key_cellinfo");
        }
        return string;
    }

    /* renamed from: b */
    public static synchronized void m135b(long j) {
        synchronized (ProcessLevelSPDB.class) {
            m124h();
            f357a.putLong("key_cellinfo_next_total", Long.valueOf(j));
        }
    }

    /* renamed from: d */
    public static synchronized void m130d(String str) {
        synchronized (ProcessLevelSPDB.class) {
            m124h();
            f357a.putString("key_switches", str);
        }
    }

    /* renamed from: e */
    public static synchronized String m129e() {
        String string;
        synchronized (ProcessLevelSPDB.class) {
            m124h();
            string = f357a.getString("key_switches");
        }
        return string;
    }

    /* renamed from: e */
    public static synchronized void m128e(String str) {
        synchronized (ProcessLevelSPDB.class) {
            m124h();
            if (str == null) {
                f357a.remove("key_data_url");
            } else {
                f357a.putString("key_data_url", str);
            }
        }
    }

    /* renamed from: f */
    public static synchronized String m127f() {
        String string;
        synchronized (ProcessLevelSPDB.class) {
            m124h();
            string = f357a.getString("key_data_url");
        }
        return string;
    }

    /* renamed from: f */
    public static synchronized void m126f(String str) {
        synchronized (ProcessLevelSPDB.class) {
            m124h();
            if (str == null) {
                f357a.remove("key_conf_url");
            } else {
                f357a.putString("key_conf_url", str);
            }
        }
    }

    /* renamed from: g */
    public static synchronized String m125g() {
        String string;
        synchronized (ProcessLevelSPDB.class) {
            m124h();
            string = f357a.getString("key_conf_url");
        }
        return string;
    }
}
