package com.xzy.forestSystem.smssdk.contact.p006a;

import android.util.Base64;

/* renamed from: cn.smssdk.contact.a.m */
public class Photo extends ContactObject {
    /* renamed from: b */
    public String m499b() {
        byte[] a = m533a("data15");
        if (a != null) {
            return Base64.encodeToString(a, 2);
        }
        return null;
    }
}
