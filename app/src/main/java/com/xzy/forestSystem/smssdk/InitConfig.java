package com.xzy.forestSystem.smssdk;

/* renamed from: cn.smssdk.a */
public class InitConfig {

    /* renamed from: c */
    private static InitConfig f15c;

    /* renamed from: a */
    private boolean f16a = false;

    /* renamed from: b */
    private boolean f17b = false;

    private InitConfig() {
    }

    /* renamed from: a */
    public static synchronized InitConfig m620a() {
        InitConfig aVar;
        synchronized (InitConfig.class) {
            if (f15c == null) {
                f15c = new InitConfig();
            }
            aVar = f15c;
        }
        return aVar;
    }

    /* renamed from: a */
    public void m619a(boolean z) {
        this.f16a = z;
    }

    /* renamed from: b */
    public boolean m618b() {
        return this.f17b;
    }

    /* renamed from: b */
    public void m617b(boolean z) {
        this.f17b = z;
    }
}
