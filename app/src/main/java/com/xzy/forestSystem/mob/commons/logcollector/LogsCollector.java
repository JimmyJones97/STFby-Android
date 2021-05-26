package  com.xzy.forestSystem.mob.commons.logcollector;

import android.content.Intent;
import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.log.LogCollector;
import  com.xzy.forestSystem.mob.tools.proguard.ProtectedMemberKeeper;
import  com.xzy.forestSystem.mob.tools.proguard.PublicMemberKeeper;

public abstract class LogsCollector implements LogCollector, ProtectedMemberKeeper, PublicMemberKeeper {

    /* renamed from: a */
    private LogsManager f364a = LogsManager.m101a();

    /* renamed from: b */
    private boolean f365b;

    /* access modifiers changed from: protected */
    public abstract String getSDKTag();

    /* access modifiers changed from: protected */
    public abstract int getSDKVersion();

    public LogsCollector() {
        this.f364a.m99a(getSDKVersion(), getSDKTag());
        try {
            if (MobSDK.getContext().getPackageManager().getPackageInfo("cn.sharesdk.log", 64) != null) {
                this.f365b = true;
            }
        } catch (Throwable th) {
        }
        this.f365b = false;
    }

    @Override //  com.xzy.forestSystem.mob.tools.log.LogCollector
    public final void log(String str, int i, int i2, String str2, String str3) {
        m107a(i, str3);
        if (str != null && str.equals(getSDKTag())) {
            if ("SHARESDK".equals(str) && (!str3.contains(" com.xzy.forestSystem.mob.") || !str3.contains("cn.sharesdk."))) {
                return;
            }
            if (i2 == 1) {
                this.f364a.m93b(getSDKVersion(), i2, str, str3);
            } else if (i2 == 2) {
                this.f364a.m100a(getSDKVersion(), i2, str, str3);
            } else if (i == 5) {
                this.f364a.m100a(getSDKVersion(), i2, str, str3);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public final int m107a(int i, String str) {
        if (MobSDK.getContext() == null || !this.f365b) {
            return 0;
        }
        try {
            Intent intent = new Intent();
            intent.setAction("cn.sharesdk.log");
            intent.putExtra("package", MobSDK.getContext().getPackageName());
            intent.putExtra("priority", i);
            intent.putExtra("msg", str);
            MobSDK.getContext().sendBroadcast(intent);
            return 0;
        } catch (Throwable th) {
            MobLog.getInstance().m57w(th);
            return 0;
        }
    }
}
