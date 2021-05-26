package com.xzy.forestSystem.mob.tools;

import  com.xzy.forestSystem.mob.tools.log.NLog;

public class MobLog extends NLog {
    private MobLog() {
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.mob.tools.log.NLog
    public String getSDKTag() {
        return "MOBTOOLS";
    }

    public static NLog getInstance() {
        return getInstanceForSDK("MOBTOOLS", true);
    }
}
