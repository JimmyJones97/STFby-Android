package com.xzy.forestSystem.smssdk.contact.p006a;

/* renamed from: cn.smssdk.contact.a.n */
public class Postal extends ContactObject {
    /* renamed from: b */
    public String m497b() {
        return m530b("data1");
    }

    /* renamed from: c */
    public int m496c() {
        return m498a(m532a("data2", -1));
    }

    /* renamed from: d */
    public String m495d() {
        if (m532a("data2", -1) == 0) {
            return m530b("data3");
        }
        return null;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public int m498a(int i) {
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            default:
                return -1;
        }
    }
}
