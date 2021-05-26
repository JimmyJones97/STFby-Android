package com.xzy.forestSystem.smssdk.utils;

import android.text.TextUtils;

/* renamed from: cn.smssdk.utils.c */
public class Utils {
    /* renamed from: a */
    public static int m373a() {
        String[] split;
        StringBuffer stringBuffer = new StringBuffer();
        if (!TextUtils.isEmpty("3.0.0") && (split = "3.0.0".split("\\.")) != null && split.length > 0) {
            stringBuffer.append(split[0]);
            for (int i = 1; i < split.length; i++) {
                if (!TextUtils.isEmpty(split[i]) && split[i].length() < 2) {
                    stringBuffer.append("0");
                    stringBuffer.append(split[i]);
                } else if (!TextUtils.isEmpty(split[i]) && split[i].length() >= 2) {
                    stringBuffer.append(split[i]);
                }
            }
        }
        return Integer.valueOf(stringBuffer.toString().trim()).intValue();
    }

    /* renamed from: a */
    public static Throwable m372a(int i) {
        return new Throwable("{\"status\":" + i + "}");
    }
}
