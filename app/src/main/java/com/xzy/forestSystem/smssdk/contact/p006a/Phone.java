package com.xzy.forestSystem.smssdk.contact.p006a;

import android.text.TextUtils;

/* renamed from: cn.smssdk.contact.a.l */
public class Phone extends ContactObject {
    /* renamed from: b */
    public String m502b() {
        String b = m530b("data1");
        if (TextUtils.isEmpty(b)) {
            return null;
        }
        return b.replace(" ", "").replace("-", "");
    }

    /* renamed from: c */
    public int m501c() {
        return m503a(m532a("data2", -1));
    }

    /* renamed from: d */
    public String m500d() {
        if (m532a("data2", -1) == 0) {
            return m530b("data3");
        }
        return null;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public int m503a(int i) {
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
            case 9:
                return 9;
            case 10:
                return 10;
            case 11:
                return 11;
            case 12:
                return 12;
            case 13:
                return 13;
            case 14:
                return 14;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;
            case 19:
                return 19;
            case 20:
                return 20;
            default:
                return -1;
        }
    }
}
