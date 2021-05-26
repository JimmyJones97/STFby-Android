package  com.xzy.forestSystem.mob.commons;

import  com.xzy.forestSystem.mob.tools.MobLog;
import java.io.File;

/* renamed from:  com.xzy.forestSystem.mob.commons.c */
public class DeviceLevelTags {
    /* renamed from: a */
    public static synchronized boolean m191a(String str) {
        boolean z;
        synchronized (DeviceLevelTags.class) {
            try {
                z = Locks.m140a(str).exists();
            } catch (Throwable th) {
                MobLog.getInstance().m57w(th);
                z = true;
            }
        }
        return z;
    }

    /* renamed from: b */
    public static synchronized void m190b(String str) {
        synchronized (DeviceLevelTags.class) {
            try {
                File a = Locks.m140a(str);
                if (!a.exists()) {
                    a.createNewFile();
                }
            } catch (Throwable th) {
                MobLog.getInstance().m57w(th);
            }
        }
        return;
    }

    /* renamed from: c */
    public static synchronized void m189c(String str) {
        synchronized (DeviceLevelTags.class) {
            try {
                File a = Locks.m140a(str);
                if (a.exists()) {
                    a.delete();
                }
            } catch (Throwable th) {
                MobLog.getInstance().m57w(th);
            }
        }
        return;
    }
}
