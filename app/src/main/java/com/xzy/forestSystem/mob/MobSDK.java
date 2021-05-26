package com.xzy.forestSystem.mob;

import android.content.Context;
import android.os.Bundle;
import  com.xzy.forestSystem.mob.commons.clt.DvcClt;
import  com.xzy.forestSystem.mob.commons.clt.PkgClt;
import  com.xzy.forestSystem.mob.commons.clt.RtClt;
import  com.xzy.forestSystem.mob.commons.iosbridge.UDPServer;
import  com.xzy.forestSystem.mob.tools.proguard.PublicMemberKeeper;
import  com.xzy.forestSystem.stub.StubApp;

public class MobSDK implements PublicMemberKeeper {

    /* renamed from: a */
    private static Context f307a;

    /* renamed from: b */
    private static String f308b;

    /* renamed from: c */
    private static String f309c;

    public static synchronized void init(Context context) {
        synchronized (MobSDK.class) {
            init(context, null, null);
        }
    }

    public static synchronized void init(Context context, String str) {
        synchronized (MobSDK.class) {
            init(context, str, null);
        }
    }

    public static synchronized void init(Context context, String str, String str2) {
        synchronized (MobSDK.class) {
            if (f307a == null) {
                f307a = StubApp.getOrigApplicationContext(context.getApplicationContext());
                m248a(str, str2);
                C0189a.m247a();
                m249a();
            }
        }
    }

    /* renamed from: a */
    private static void m248a(String str, String str2) {
        if (str == null || str2 == null) {
            Bundle bundle = null;
            try {
                bundle = f307a.getPackageManager().getPackageInfo(f307a.getPackageName(), 128).applicationInfo.metaData;
            } catch (Throwable th) {
            }
            if (str == null && bundle != null) {
                str = bundle.getString("Mob-AppKey");
            }
            if (str2 == null && bundle != null) {
                str2 = bundle.getString("Mob-AppSecret");
            }
            if (str2 == null && bundle != null) {
                str2 = bundle.getString("Mob-AppSeret");
            }
        }
        f308b = str;
        f309c = str2;
    }

    /* renamed from: a */
    private static void m249a() {
        new Thread() { // from class:  com.xzy.forestSystem.mob.MobSDK.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                DvcClt.startCollector();
                PkgClt.startCollector();
                RtClt.startCollector();
                UDPServer.start();
            }
        }.start();
    }

    public static Context getContext() {
        return f307a;
    }

    public static String getAppkey() {
        return f308b;
    }

    public static String getAppSecret() {
        return f309c;
    }
}
