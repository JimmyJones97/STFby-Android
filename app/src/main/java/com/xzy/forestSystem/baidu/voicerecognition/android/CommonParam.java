package com.xzy.forestSystem.baidu.voicerecognition.android;

import android.content.Context;
import android.text.TextUtils;

public class CommonParam {
    private static final boolean DEBUG = false;
    private static final String TAG = CommonParam.class.getSimpleName();

    public static String getCUID(Context paramContext) {
        String str1 = getDeviceId(paramContext);
        String str2 = DeviceId.getIMEI(paramContext);
        if (TextUtils.isEmpty(str2)) {
            str2 = "0";
        }
        return str1 + "|" + new StringBuffer(str2).reverse().toString();
    }

    private static String getDeviceId(Context paramContext) {
        return DeviceId.getDeviceID(paramContext);
    }
}
