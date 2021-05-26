package com.xzy.forestSystem.mob;

import  com.xzy.forestSystem.mob.commons.logcollector.LogsCollector;
import  com.xzy.forestSystem.mob.tools.log.NLog;

/* compiled from: MobSDKLog */
/* renamed from:  com.xzy.forestSystem.mob.a */
public class C0189a extends NLog {
    private C0189a() {
        setCollector("MOBSDK", new LogsCollector() { // from class:  com.xzy.forestSystem.mob.MobSDKLog$1
            /* access modifiers changed from: protected */
            @Override //  com.xzy.forestSystem.mob.commons.logcollector.LogsCollector
            public int getSDKVersion() {
                return 1;
            }

            /* access modifiers changed from: protected */
            @Override //  com.xzy.forestSystem.mob.commons.logcollector.LogsCollector
            public String getSDKTag() {
                return "MOBSDK";
            }
        });
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.mob.tools.log.NLog
    public String getSDKTag() {
        return "MOBSDK";
    }

    /* renamed from: a */
    public static NLog m247a() {
        return new C0189a();
    }
}
