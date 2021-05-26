package com.xzy.forestSystem.baidu.voicerecognition.android;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import java.util.UUID;

public final class Device {
    private static final boolean DEBUG = true;
    private static final String TAG = "Device";

    public static String getDeviceID(Context context) {
        String savedDeviceID = PreferenceSetting.getString(context, "device_id", "");
        if (TextUtils.isEmpty(savedDeviceID)) {
            String savedDeviceID2 = CommonParam.getCUID(context);
            PreferenceSetting.setString(context, "device_id", savedDeviceID2);
            return savedDeviceID2;
        }
        Log.d(TAG, "read deviceID:" + savedDeviceID);
        return savedDeviceID;
    }

    private Device() {
    }

    private static String generateDeviceID(Context context) {
        String imeiNo = getIMEI(context);
        Log.d(TAG, "imei:" + imeiNo);
        String andridID = getAndroidId(context);
        Log.d(TAG, "AndroidID:" + andridID);
        String uuid = UUID.randomUUID().toString();
        Log.d(TAG, "UUID:" + uuid);
        String deviceID = Util.toMd5((imeiNo + andridID + uuid).getBytes(), true);
        Log.d(TAG, "deviceID" + deviceID);
        return deviceID;
    }

    private static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
        if (tm == null) {
            return "";
        }
        String tmDeviceId = tm.getDeviceId();
        if (TextUtils.isEmpty(tmDeviceId)) {
            return "";
        }
        return tmDeviceId;
    }

    private static String getAndroidId(Context context) {
        String androidId = Settings.Secure.getString(context.getContentResolver(), "android_id");
        if (TextUtils.isEmpty(androidId)) {
            return "";
        }
        return androidId;
    }
}
