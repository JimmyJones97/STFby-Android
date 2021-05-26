package com.xzy.forestSystem.smssdk.utils;

import  com.xzy.forestSystem.mob.commons.logcollector.LogsCollector;
import  com.xzy.forestSystem.mob.tools.log.NLog;

/* renamed from: cn.smssdk.utils.SMSLog */
public class SMSLog extends NLog {
    public static final String FORMAT = "[SMSSDK][%s][%s] %s";

    private SMSLog() {
        setCollector("SMSSDK", new LogsCollector() { // from class: cn.smssdk.utils.SMSLog.1
            /* access modifiers changed from: protected */
            @Override //  com.xzy.forestSystem.mob.commons.logcollector.LogsCollector
            public int getSDKVersion() {
                return 30000;
            }

            /* access modifiers changed from: protected */
            @Override //  com.xzy.forestSystem.mob.commons.logcollector.LogsCollector
            public String getSDKTag() {
                return "SMSSDK";
            }
        });
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.mob.tools.log.NLog
    public String getSDKTag() {
        return "SMSSDK";
    }

    public static NLog prepare() {
        return new SMSLog();
    }

    public static NLog getInstance() {
        return getInstanceForSDK("SMSSDK", true);
    }
}
