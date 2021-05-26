package com.xzy.forestSystem.baidu.voicerecognition.android;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public final class Util {
    public static String toMd5(byte[] paramArrayOfByte, boolean paramBoolean) {
        return MD5Util.toMd5(paramArrayOfByte, paramBoolean);
    }

    public static String pfm(Context context) throws SecurityException {
        String pfm;
        boolean isUsingWifi = Utility.isUsingWifi(context);
        String pfm2 = Utility.generatePlatformString();
        if (isUsingWifi) {
            pfm = pfm2 + "&1";
        } else {
            pfm = pfm2 + "&3";
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        if (tm == null) {
            return pfm;
        }
        String tmDeviceId = tm.getDeviceId();
        if (TextUtils.isEmpty(tmDeviceId)) {
            return pfm;
        }
        return (pfm + "&") + tmDeviceId;
    }
}
