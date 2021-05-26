package  com.xzy.forestSystem.mob.commons.logcollector;

import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.tools.utils.SharePrefrenceHelper;

/* renamed from:  com.xzy.forestSystem.mob.commons.logcollector.d */
public class LogsSharePrefrence {

    /* renamed from: a */
    private static LogsSharePrefrence f375a;

    /* renamed from: b */
    private SharePrefrenceHelper f376b = new SharePrefrenceHelper(MobSDK.getContext());

    private LogsSharePrefrence() {
        this.f376b.open("mob_sdk_exception", 1);
    }

    /* renamed from: a */
    public static LogsSharePrefrence m87a() {
        if (f375a == null) {
            f375a = new LogsSharePrefrence();
        }
        return f375a;
    }

    /* renamed from: a */
    public void m85a(long j) {
        this.f376b.putLong("service_time", Long.valueOf(j));
    }

    /* renamed from: b */
    public long m82b() {
        return this.f376b.getLong("service_time");
    }

    /* renamed from: a */
    public void m83a(boolean z) {
        this.f376b.putInt("is_upload_err_log", Integer.valueOf(z ? 0 : 1));
    }

    /* renamed from: c */
    public boolean m80c() {
        return this.f376b.getInt("is_upload_err_log") == 0;
    }

    /* renamed from: a */
    public void m86a(int i) {
        this.f376b.putInt("is_upload_crash", Integer.valueOf(i));
    }

    /* renamed from: d */
    public int m78d() {
        return this.f376b.getInt("is_upload_crash");
    }

    /* renamed from: b */
    public void m81b(int i) {
        this.f376b.putInt("is_upload_sdkerr", Integer.valueOf(i));
    }

    /* renamed from: e */
    public int m77e() {
        return this.f376b.getInt("is_upload_sdkerr");
    }

    /* renamed from: c */
    public void m79c(int i) {
        this.f376b.putInt("is_upload_apperr", Integer.valueOf(i));
    }

    /* renamed from: f */
    public int m76f() {
        return this.f376b.getInt("is_upload_apperr");
    }

    /* renamed from: g */
    public String m75g() {
        return this.f376b.getString("err_log_filter");
    }

    /* renamed from: a */
    public void m84a(String str) {
        this.f376b.putString("err_log_filter", str);
    }
}
