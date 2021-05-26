package com.xzy.forestSystem.baidu.voicerecognition.android;

import android.content.Context;

/* access modifiers changed from: package-private */
public final class PreferenceSetting {
    private static final String FILE_NAME = "bdvrsetting";
    private static final String FILE_NAME_MD5 = Util.toMd5(FILE_NAME.getBytes(), false);
    private static final String VTLN_KEY = "vtln";
    private static final int VTLN_LIMIT = 255;
    private static final String VTLN_SECRET_KEY = "BDVRVtln*!Secret";

    public static String getString(Context context, String key, String defaultValue) {
        return context.getSharedPreferences(FILE_NAME_MD5, 0).getString(key, defaultValue);
    }

    public static void setString(Context context, String key, String value) {
        context.getSharedPreferences(FILE_NAME_MD5, 0).edit().putString(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return context.getSharedPreferences(FILE_NAME_MD5, 0).getBoolean(key, defaultValue);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        context.getSharedPreferences(FILE_NAME_MD5, 0).edit().putBoolean(key, value).commit();
    }

    public static void removeString(Context context, String key) {
        context.getSharedPreferences(FILE_NAME_MD5, 0).edit().remove(key).commit();
    }

    public static boolean setVtlnWithCheckSum(Context context, int value) {
        if (value < 0 || value > 255) {
            return false;
        }
        setString(context, VTLN_KEY, value + "||" + Util.toMd5((value + VTLN_SECRET_KEY).getBytes(), false));
        return true;
    }

    public static int getVtlnWithCheckSum(Context context) {
        String vtlnAndCheckSumString = getString(context, VTLN_KEY, "");
        if (vtlnAndCheckSumString.indexOf("||") == -1) {
            return -1;
        }
        String[] vtlnsStrings = vtlnAndCheckSumString.split("\\|\\|");
        if (vtlnsStrings.length < 2) {
            return -1;
        }
        String preCheckSumString = vtlnsStrings[1];
        String vtlnValue = vtlnsStrings[0];
        if (Util.toMd5((vtlnValue + VTLN_SECRET_KEY).getBytes(), false).equals(preCheckSumString)) {
            return Integer.parseInt(vtlnValue);
        }
        return -1;
    }

    private PreferenceSetting() {
    }
}
