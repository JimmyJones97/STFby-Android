package com.xzy.forestSystem.mob;

import android.app.Application;

public class MobApplication extends Application {
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public String m251a() {
        return null;
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public String m250b() {
        return null;
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this, m251a(), m250b());
    }
}
