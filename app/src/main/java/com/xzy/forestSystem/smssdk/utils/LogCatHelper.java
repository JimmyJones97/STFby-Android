package com.xzy.forestSystem.smssdk.utils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* renamed from: cn.smssdk.utils.b */
public class LogCatHelper {

    /* renamed from: a */
    private static final char[] f194a = "0123456789ABCDEF".toCharArray();

    /* renamed from: a */
    public static String m376a(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return "";
        }
        char[] cArr = new char[(bArr.length * 2)];
        for (int i = 0; i < bArr.length; i++) {
            int i2 = bArr[i] & 255;
            cArr[i * 2] = f194a[i2 >>> 4];
            cArr[(i * 2) + 1] = f194a[i2 & 15];
        }
        return new String(cArr);
    }

    /* renamed from: a */
    public static String m377a(Map map) {
        if (map == null) {
            return "";
        }
        String str = "[";
//        for (Map.Entry entry : map.entrySet()) {
//            str = str + " " + entry.getKey() + ":" + (entry.getValue() == null ? "" : entry.getValue());
//        }
        return str + " ]";
    }

    /* renamed from: a */
    public static String m378a(List list) {
        Object obj;
        if (list == null) {
            return "";
        }
        Iterator it = list.iterator();
        String str = "";
        while (it.hasNext()) {
            Object next = it.next();
            StringBuilder append = new StringBuilder().append(str);
            if (next == null) {
                obj = "";
            } else {
                obj = next;
            }
            str = append.append(obj).toString();
            if (it.hasNext()) {
                str = str + " ,";
            }
        }
        return str;
    }

    /* renamed from: a */
    public static String m375a(String[] strArr) {
        String stringBuffer;
        if (strArr == null || strArr.length <= 0) {
            return "";
        }
        StringBuffer stringBuffer2 = new StringBuffer();
        stringBuffer2.append("[");
        for (String str : strArr) {
            stringBuffer2.append(str);
            stringBuffer2.append(", ");
        }
        if (stringBuffer2.length() >= 2) {
            stringBuffer = stringBuffer2.substring(0, stringBuffer2.length() - 2);
        } else {
            stringBuffer = stringBuffer2.toString();
        }
        return stringBuffer + "]";
    }

    /* renamed from: a */
    public static String m379a(int i) {
        switch (i) {
            case 1:
                return "EVENT_GET_SUPPORTED_COUNTRIES";
            case 2:
                return "EVENT_GET_VERIFICATION_CODE";
            case 3:
                return "EVENT_SUBMIT_VERIFICATION_CODE";
            case 4:
                return "EVENT_GET_CONTACTS";
            case 5:
                return "EVENT_SUBMIT_USER_INFO";
            case 6:
                return "EVENT_GET_FRIENDS_IN_APP";
            case 7:
                return "EVENT_GET_NEW_FRIENDS_COUNT";
            case 8:
                return "EVENT_GET_VOICE_VERIFICATION_CODE";
            default:
                return String.valueOf(i);
        }
    }

    /* renamed from: b */
    public static String m374b(int i) {
        switch (i) {
            case -1:
                return "RESULT_COMPLETE";
            case 0:
                return "RESULT_ERROR";
            default:
                return String.valueOf(i);
        }
    }
}
