package com.xzy.forestSystem.smssdk.contact.p006a;

/* renamed from: cn.smssdk.contact.a.g */
public class Im extends ContactObject {
    /* renamed from: b */
    public String m518b() {
        return m530b("data1");
    }

    /* renamed from: c */
    public int m517c() {
        return m519a(m532a("data5", -1));
    }

    /* renamed from: d */
    public String m516d() {
        if (m532a("data5", -1) == -1) {
            return m530b("data3");
        }
        return null;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public int m519a(int i) {
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            default:
                return -1;
        }
    }
}
