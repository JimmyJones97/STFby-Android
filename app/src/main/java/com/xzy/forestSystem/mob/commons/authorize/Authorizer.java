package  com.xzy.forestSystem.mob.commons.authorize;

import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.commons.LockAction;
import  com.xzy.forestSystem.mob.commons.Locks;
import  com.xzy.forestSystem.mob.commons.MobProduct;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.utils.DeviceHelper;
import  com.xzy.forestSystem.mob.tools.utils.FileLocker;
import java.io.File;
import java.util.HashMap;

/* renamed from:  com.xzy.forestSystem.mob.commons.authorize.a */
public final class Authorizer {
    /* renamed from: a */
    public final String m217a(final MobProduct mobProduct) {
        final String[] strArr = new String[1];
        Locks.m141a(Locks.m140a("comm/locks/.globalLock"), new LockAction() { // from class:  com.xzy.forestSystem.mob.commons.authorize.a.1
            @Override //  com.xzy.forestSystem.mob.commons.LockAction
            public boolean run(FileLocker fileLocker) {
                strArr[0] = Authorizer.this.m208b(mobProduct);
                return false;
            }
        });
        return strArr[0];
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: a */
    private final File m218a() {
        File a = Locks.m140a("comm/dbs/.duid");
        if (!a.getParentFile().exists()) {
            a.getParentFile().mkdirs();
        }
        return a;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0028  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x002f  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00a5 A[SYNTHETIC, Splitter:B:46:0x00a5] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00b3 A[SYNTHETIC, Splitter:B:54:0x00b3] */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x00e6  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x00e8  */
    /* renamed from: b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.lang.String m208b( com.xzy.forestSystem.mob.commons.MobProduct r7) {
        /*
        // Method dump skipped, instructions count: 235
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.commons.authorize.Authorizer.m208b( com.xzy.forestSystem.mob.commons.MobProduct):java.lang.String");
    }

    /* renamed from: a */
    private final boolean m211a(HashMap<String, String> hashMap) {
        if (hashMap == null) {
            return true;
        }
        DeviceHelper instance = DeviceHelper.getInstance(MobSDK.getContext());
        String str = hashMap.get("adsid");
        String str2 = null;
        try {
            str2 = instance.getAdvertisingID();
        } catch (Throwable th) {
            MobLog.getInstance().m57w(th);
        }
        if (str2 != null) {
            if (str == null && str2 != null) {
                return true;
            }
            if (str != null && !str.equals(str2)) {
                return true;
            }
        }
        String str3 = hashMap.get("imei");
        String imei = instance.getIMEI();
        if (str3 == null || !str3.equals(imei)) {
            return true;
        }
        String str4 = hashMap.get("serialno");
        String serialno = instance.getSerialno();
        if (str4 == null || !str4.equals(serialno)) {
            return true;
        }
        String str5 = hashMap.get("mac");
        String macAddress = instance.getMacAddress();
        if (str5 == null || !str5.equals(macAddress)) {
            return true;
        }
        String str6 = hashMap.get("model");
        String model = instance.getModel();
        if (str6 == null || !str6.equals(model)) {
            return true;
        }
        String str7 = hashMap.get("factory");
        String manufacturer = instance.getManufacturer();
        if (str7 == null || !str7.equals(manufacturer)) {
            return true;
        }
        String str8 = hashMap.get("androidid");
        String androidID = instance.getAndroidID();
        if (str8 == null || !str8.equals(androidID)) {
            return true;
        }
        String str9 = hashMap.get("sysver");
        String oSVersionName = instance.getOSVersionName();
        if (str9 == null || !str9.equals(oSVersionName)) {
            return true;
        }
        return false;
    }

    /* renamed from: b */
    private final String m209b() {
        return "sdk.commonap.sdk";
    }

    /* JADX WARNING: Removed duplicated region for block: B:31:0x0117 A[SYNTHETIC, Splitter:B:31:0x0117] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0124 A[SYNTHETIC, Splitter:B:37:0x0124] */
    /* renamed from: c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.util.HashMap<java.lang.String, java.lang.Object> m206c( com.xzy.forestSystem.mob.commons.MobProduct r11) {
        /*
        // Method dump skipped, instructions count: 317
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.commons.authorize.Authorizer.m206c( com.xzy.forestSystem.mob.commons.MobProduct):java.util.HashMap");
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0115 A[SYNTHETIC, Splitter:B:32:0x0115] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0122 A[SYNTHETIC, Splitter:B:38:0x0122] */
    /* JADX WARNING: Removed duplicated region for block: B:50:? A[RETURN, SYNTHETIC] */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.lang.String m215a( com.xzy.forestSystem.mob.commons.MobProduct r11, java.util.HashMap<java.lang.String, java.lang.Object> r12, boolean r13) {
        /*
        // Method dump skipped, instructions count: 313
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.commons.authorize.Authorizer.m215a( com.xzy.forestSystem.mob.commons.MobProduct, java.util.HashMap, boolean):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0114 A[SYNTHETIC, Splitter:B:21:0x0114] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0120 A[SYNTHETIC, Splitter:B:26:0x0120] */
    /* JADX WARNING: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final void m216a( com.xzy.forestSystem.mob.commons.MobProduct r9, java.util.HashMap<java.lang.String, java.lang.Object> r10) {
        /*
        // Method dump skipped, instructions count: 313
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.commons.authorize.Authorizer.m216a( com.xzy.forestSystem.mob.commons.MobProduct, java.util.HashMap):void");
    }

    /* renamed from: a */
    public final String m210a(final boolean z) {
        final String[] strArr = new String[1];
        Locks.m141a(Locks.m140a("comm/locks/.globalLock"), new LockAction() { // from class:  com.xzy.forestSystem.mob.commons.authorize.a.2
            /* JADX WARNING: Removed duplicated region for block: B:14:0x002b  */
            /* JADX WARNING: Removed duplicated region for block: B:22:0x004c A[SYNTHETIC, Splitter:B:22:0x004c] */
            /* JADX WARNING: Removed duplicated region for block: B:30:0x0058 A[SYNTHETIC, Splitter:B:30:0x0058] */
            @Override //  com.xzy.forestSystem.mob.commons.LockAction
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public boolean run( com.xzy.forestSystem.mob.tools.utils.FileLocker r6) {
                /*
                    r5 = this;
                    r2 = 0
                    r4 = 0
                     com.xzy.forestSystem.mob.commons.authorize.a r0 =  com.xzy.forestSystem.mob.commons.authorize.Authorizer.this     // Catch:{ Throwable -> 0x005c }
                    java.io.File r0 =  com.xzy.forestSystem.mob.commons.authorize.Authorizer.m214a(r0)     // Catch:{ Throwable -> 0x005c }
                    boolean r1 = r0.exists()     // Catch:{ Throwable -> 0x005c }
                    if (r1 == 0) goto L_0x006d
                    boolean r1 = r0.isFile()     // Catch:{ Throwable -> 0x005c }
                    if (r1 == 0) goto L_0x006d
                    java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0041, all -> 0x0054 }
                    r3.<init>(r0)     // Catch:{ Throwable -> 0x0041, all -> 0x0054 }
                    java.io.ObjectInputStream r1 = new java.io.ObjectInputStream     // Catch:{ Throwable -> 0x0041, all -> 0x0054 }
                    r1.<init>(r3)     // Catch:{ Throwable -> 0x0041, all -> 0x0054 }
                    java.lang.Object r0 = r1.readObject()     // Catch:{ Throwable -> 0x006b }
                    java.util.HashMap r0 = (java.util.HashMap) r0     // Catch:{ Throwable -> 0x006b }
                    if (r1 == 0) goto L_0x0029
                    r1.close()     // Catch:{ Throwable -> 0x0065 }
                L_0x0029:
                    if (r0 != 0) goto L_0x0033
                     com.xzy.forestSystem.mob.commons.authorize.a r0 =  com.xzy.forestSystem.mob.commons.authorize.Authorizer.this
                    boolean r1 = r4
                    java.util.HashMap r0 =  com.xzy.forestSystem.mob.commons.authorize.Authorizer.m212a(r0, r1)
                L_0x0033:
                    java.lang.String[] r1 = r0
                    r2 = 0
                    java.lang.String r3 = "duid"
                    java.lang.Object r0 = r0.get(r3)
                    java.lang.String r0 = (java.lang.String) r0
                    r1[r2] = r0
                L_0x0040:
                    return r4
                L_0x0041:
                    r0 = move-exception
                    r1 = r2
                L_0x0043:
                     com.xzy.forestSystem.mob.tools.log.NLog r3 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()     // Catch:{ all -> 0x0069 }
                    r3.m57w(r0)     // Catch:{ all -> 0x0069 }
                    if (r1 == 0) goto L_0x006d
                    r1.close()     // Catch:{ Throwable -> 0x0051 }
                    r0 = r2
                    goto L_0x0029
                L_0x0051:
                    r0 = move-exception
                    r0 = r2
                    goto L_0x0029
                L_0x0054:
                    r0 = move-exception
                    r1 = r2
                L_0x0056:
                    if (r1 == 0) goto L_0x005b
                    r1.close()     // Catch:{ Throwable -> 0x0067 }
                L_0x005b:
                    throw r0
                L_0x005c:
                    r0 = move-exception
                     com.xzy.forestSystem.mob.tools.log.NLog r1 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
                    r1.m57w(r0)
                    goto L_0x0040
                L_0x0065:
                    r1 = move-exception
                    goto L_0x0029
                L_0x0067:
                    r1 = move-exception
                    goto L_0x005b
                L_0x0069:
                    r0 = move-exception
                    goto L_0x0056
                L_0x006b:
                    r0 = move-exception
                    goto L_0x0043
                L_0x006d:
                    r0 = r2
                    goto L_0x0029
                */
                throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.commons.authorize.Authorizer.C01922.run( com.xzy.forestSystem.mob.tools.utils.FileLocker):boolean");
            }
        });
        return strArr[0];
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0106 A[SYNTHETIC, Splitter:B:30:0x0106] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0112 A[SYNTHETIC, Splitter:B:35:0x0112] */
    /* renamed from: b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.HashMap<java.lang.String, java.lang.Object> m207b(boolean r9) {
        /*
        // Method dump skipped, instructions count: 293
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.commons.authorize.Authorizer.m207b(boolean):java.util.HashMap");
    }
}
