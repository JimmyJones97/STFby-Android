package com.xzy.forestSystem.smssdk.contact.p006a;

/* renamed from: cn.smssdk.contact.a.c */
public class Email extends ContactObject {
    /* renamed from: b */
    public String m527b() {
        return m530b("data1");
    }

    /* renamed from: c */
    public int m526c() {
        return m528a(m532a("data2", -1));
    }

    /* renamed from: d */
    public String m525d() {
        if (m532a("data2", -1) == 0) {
            return m530b("data3");
        }
        return null;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public int m528a(int i) {
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 5;
            case 4:
                return 4;
            default:
                return -1;
        }
    }
}
