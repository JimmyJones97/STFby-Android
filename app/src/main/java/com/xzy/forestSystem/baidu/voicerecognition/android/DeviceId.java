package com.xzy.forestSystem.baidu.voicerecognition.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public final class DeviceId {
    private static final String AES_KEY = "30212102dicudiab";
    private static final boolean DEBUG = false;
    private static final String EXT_DIR = "backups/.SystemConfig";
    private static final String EXT_FILE = ".cuid";
    private static final String KEY_DEVICE_ID = "com.baidu.deviceid";
    private static final String OLD_EXT_DIR = "baidu";
    private static final String TAG = "DeviceId";

    public static String getDeviceID(Context paramContext) {
        checkPermission(paramContext, "android.permission.WRITE_SETTINGS");
        checkPermission(paramContext, "android.permission.READ_PHONE_STATE");
        checkPermission(paramContext, "android.permission.WRITE_EXTERNAL_STORAGE");
        IMEIInfo localIMEIInfo = IMEIInfo.getIMEIInfo(paramContext);
        String str1 = localIMEIInfo.IMEI;
        int i = !localIMEIInfo.CAN_READ_AND_WRITE_SYSTEM_SETTINGS ? 1 : 0;
        String str2 = getAndroidId(paramContext);
        if (i != 0) {
            return MD5Util.toMd5(("com.baidu" + str2).getBytes(), true);
        }
        String str4 = null;
        String str3 = Settings.System.getString(paramContext.getContentResolver(), KEY_DEVICE_ID);
        if (TextUtils.isEmpty(str3)) {
            str4 = MD5Util.toMd5(("com.baidu" + str1 + str2).getBytes(), true);
            str3 = Settings.System.getString(paramContext.getContentResolver(), str4);
            if (!TextUtils.isEmpty(str3)) {
                Settings.System.putString(paramContext.getContentResolver(), KEY_DEVICE_ID, str3);
                setExternalDeviceId(str1, str3);
            }
        } else if (!isExternalFileExists()) {
            setExternalDeviceId(str1, str3);
        }
        if (TextUtils.isEmpty(str3)) {
            str3 = getExternalDeviceId(str1);
            if (!TextUtils.isEmpty(str3)) {
                Settings.System.putString(paramContext.getContentResolver(), str4, str3);
                Settings.System.putString(paramContext.getContentResolver(), KEY_DEVICE_ID, str3);
            }
        }
        if (TextUtils.isEmpty(str3)) {
            str3 = MD5Util.toMd5((str1 + str2 + UUID.randomUUID().toString()).getBytes(), true);
            Settings.System.putString(paramContext.getContentResolver(), str4, str3);
            Settings.System.putString(paramContext.getContentResolver(), KEY_DEVICE_ID, str3);
            setExternalDeviceId(str1, str3);
        }
        return str3;
    }

    public static String getIMEI(Context paramContext) {
        return IMEIInfo.getIMEIInfo(paramContext).IMEI;
    }

    public static String getAndroidId(Context paramContext) {
        String str = Settings.Secure.getString(paramContext.getContentResolver(), "android_id");
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        return str;
    }

    @SuppressLint("WrongConstant")
    private static void checkPermission(Context paramContext, String paramString) {
        if ((paramContext.checkCallingOrSelfPermission(paramString) == 0 ? 1 : 0) == 0) {
            throw new SecurityException("Permission Denial: requires permission " + paramString);
        }
    }

    private static boolean isExternalFileExists() {
        return new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig/.cuid").exists();
    }

    private static String getExternalDeviceId(String paramString) {
        if (TextUtils.isEmpty(paramString)) {
            return "";
        }
        int i = 0;
        String str1 = "";
        File localFile = new File(Environment.getExternalStorageDirectory(), "baidu/.cuid");
        if (!localFile.exists()) {
            localFile = new File(Environment.getExternalStorageDirectory(), "backups/.SystemConfig/.cuid");
            i = 1;
        }
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader(localFile));
            StringBuilder localStringBuilder = new StringBuilder();
            while (true) {
                String str2 = localBufferedReader.readLine();
                if (str2 == null) {
                    break;
                }
                localStringBuilder.append(str2);
                localStringBuilder.append("\r\n");
            }
            localBufferedReader.close();
            String[] arrayOfString = new String(AESUtil.decrypt(AES_KEY, AES_KEY, Base64.decode(localStringBuilder.toString().getBytes()))).split("=");
            if (arrayOfString != null && arrayOfString.length == 2 && paramString.equals(arrayOfString[0])) {
                str1 = arrayOfString[1];
            }
            if (i != 0) {
                return str1;
            }
            setExternalDeviceId(paramString, str1);
            return str1;
        } catch (FileNotFoundException e) {
            return str1;
        } catch (Exception e2) {
            return str1;
        }
    }

    private static void setExternalDeviceId(String paramString1, String paramString2) {
        File localObject2;
        if (!TextUtils.isEmpty(paramString1)) {
            File localFile1 = new File(Environment.getExternalStorageDirectory(), EXT_DIR);
            File localFile2 = new File(localFile1, EXT_FILE);
            try {
                if (localFile1.exists() && !localFile1.isDirectory()) {
                    Random localObject1 = new Random();
                    File localFile3 = localFile1.getParentFile();
                    String str = localFile1.getName();
                    do {
                        localObject2 = new File(localFile3, str + localObject1.nextInt() + ".tmp");
                    } while (localObject2.exists());
                    localFile1.renameTo(localObject2);
                    localObject2.delete();
                }
                localFile1.mkdirs();
                FileWriter localObject12 = new FileWriter(localFile2, (boolean) DEBUG);
                localObject12.write(Base64.encode(AESUtil.encrypt(AES_KEY, AES_KEY, (paramString1 + "=" + paramString2).getBytes()), "utf-8"));
                localObject12.flush();
                localObject12.close();
            } catch (Exception e) {
            }
        }
    }

    /* access modifiers changed from: package-private */
    public static final class IMEIInfo {
        public static final String DEFAULT_TM_DEVICEID = "";
        private static final String KEY_IMEI = "bd_setting_i";
        public final boolean CAN_READ_AND_WRITE_SYSTEM_SETTINGS;
        public final String IMEI;

        private IMEIInfo(String paramString, boolean paramBoolean) {
            this.IMEI = paramString;
            this.CAN_READ_AND_WRITE_SYSTEM_SETTINGS = paramBoolean;
        }

        private static String getIMEI(Context paramContext, String paramString) {
            String str = null;
            try {
                @SuppressLint("WrongConstant") TelephonyManager localTelephonyManager = (TelephonyManager) paramContext.getSystemService("phone");
                if (localTelephonyManager != null) {
                    str = localTelephonyManager.getDeviceId();
                }
            } catch (Exception localException) {
                Log.e(DeviceId.TAG, "Read IMEI failed", localException);
            }
            String str2 = imeiCheck(str);
            return TextUtils.isEmpty(str2) ? paramString : str2;
        }

        static IMEIInfo getIMEIInfo(Context paramContext) {
            boolean z;
            int i = 0;
            String str = "";
            try {
                str = Settings.System.getString(paramContext.getContentResolver(), KEY_IMEI);
                if (TextUtils.isEmpty(str)) {
                    str = getIMEI(paramContext, "");
                }
                Settings.System.putString(paramContext.getContentResolver(), KEY_IMEI, str);
            } catch (Exception localException) {
                Log.e(DeviceId.TAG, "Settings.System.getString or putString failed", localException);
                i = 1;
                if (TextUtils.isEmpty(str)) {
                    str = getIMEI(paramContext, "");
                }
            }
            if (i == 0) {
                z = true;
            } else {
                z = DeviceId.DEBUG;
            }
            return new IMEIInfo(str, z);
        }

        private static String imeiCheck(String paramString) {
            if (paramString == null || !paramString.contains(":")) {
                return paramString;
            }
            return "";
        }
    }
}
