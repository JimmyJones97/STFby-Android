package com.xzy.forestSystem.baidu.speech.easr;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.util.DisplayMetrics;
import com.xzy.forestSystem.baidu.speech.VoiceRecognitionService;
import com.xzy.forestSystem.baidu.voicerecognition.android.CommonParam;
import com.xzy.forestSystem.baidu.voicerecognition.android.CommonParam;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.zip.GZIPOutputStream;

public class Utility {
    private static final String KEY_LSAT_DOWNLOAD_STAT_PERIOD = "last_download_stat_period";
    private static final String KEY_LSAT_UPLOAD_STAT_TIME = "last_upload_stat_time";
    private static final String SDK_VERSION_NAME = "1.0.0-20140804";
    private static final String SHARED_PREFERENCE_NAME = "tts";

    public static void setLastUploadStatTime(Context context, long time) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).edit();
        editor.putLong(KEY_LSAT_UPLOAD_STAT_TIME, time);
        editor.commit();
    }

    public static long getLastUploadStatTime(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).getLong(KEY_LSAT_UPLOAD_STAT_TIME, 0);
    }

    public static void setLastDownloadStatPeriod(Context context, int period) {
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).edit();
        editor.putInt(KEY_LSAT_DOWNLOAD_STAT_PERIOD, period);
        editor.commit();
    }

    public static int getLastDownloadStatPeriod(Context context) {
        return context.getSharedPreferences(SHARED_PREFERENCE_NAME, 0).getInt(KEY_LSAT_DOWNLOAD_STAT_PERIOD, 0);
    }

    public static String encryptBASE64(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            byte[] encode = str.getBytes("utf-8");
            return new String(Base64.encode(encode, 0, encode.length, 0), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptBASE64(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        try {
            return new String(Base64.encode(data, 0, data.length, 0), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] encryptGZIP(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(baos);
            gzip.write(str.getBytes("utf-8"));
            gzip.close();
            byte[] byteArray = baos.toByteArray();
            baos.flush();
            baos.close();
            return byteArray;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public static String getCUID(Context context) {
        try {
            return CommonParam.getCUID(context);
        } catch (SecurityException e) {
            return "default.for.security.exception";
        }
    }

    public static String getSdkVersion() {
        return VoiceRecognitionService.getSdkVersion();
    }

    public static String getAppName(Context context) {
        return getApplicationName(context);
    }

    public static String getPlatform(Context context) {
        return getOS() + "&" + Build.MODEL + "&" + Build.VERSION.RELEASE + "&" + Build.VERSION.SDK_INT + "&" + getNetType(context);
    }

    public static String getOS() {
        return "Android";
    }

    @SuppressLint({"DefaultLocale"})
    public static int getNetType(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo == null || !"wifi".equals(networkInfo.getTypeName().toLowerCase())) {
            return 3;
        }
        return 1;
    }

    public static String getScreen(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        return width + "*" + dm.heightPixels;
    }

    public static String getSdkName() {
        return "离线TTS SDK";
    }

    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    public static String getSignatureMD5(Context context) {
        try {
            return parseSignature(context.getPackageManager().getPackageInfo(context.getPackageName(), 64).signatures[0].toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String parseSignature(byte[] signature) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(((X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(signature))).getEncoded());
            return toHexString(md5.digest());
        } catch (CertificateException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static String toHexString(byte[] bytes) {
        StringBuffer hexString = new StringBuffer();
        for (byte b : bytes) {
            String appendString = Integer.toHexString(b & 255);
            if (appendString.length() == 1) {
                hexString.append("0");
            }
            hexString.append(appendString);
        }
        return hexString.toString();
    }

    private static String getApplicationName(Context context) {
        ApplicationInfo applicationInfo;
        PackageManager packageManager = null;
        try {
            packageManager = context.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        return (String) packageManager.getApplicationLabel(applicationInfo);
    }

    public static boolean checkPermission(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == 0;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x001e A[SYNTHETIC, Splitter:B:16:0x001e] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x002d A[SYNTHETIC, Splitter:B:23:0x002d] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0039 A[SYNTHETIC, Splitter:B:29:0x0039] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getLicenseDataFromFile(java.lang.String r5, byte[] r6) {
        /*
            r1 = 0
            r3 = -1
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x0018, IOException -> 0x0027 }
            r2.<init>(r5)     // Catch:{ FileNotFoundException -> 0x0018, IOException -> 0x0027 }
            int r3 = r2.read(r6)     // Catch:{ FileNotFoundException -> 0x0048, IOException -> 0x0045, all -> 0x0042 }
            if (r2 == 0) goto L_0x0010
            r2.close()     // Catch:{ IOException -> 0x0012 }
        L_0x0010:
            r1 = r2
        L_0x0011:
            return r3
        L_0x0012:
            r0 = move-exception
            r0.printStackTrace()
            r1 = r2
            goto L_0x0011
        L_0x0018:
            r0 = move-exception
        L_0x0019:
            r0.printStackTrace()     // Catch:{ all -> 0x0036 }
            if (r1 == 0) goto L_0x0011
            r1.close()     // Catch:{ IOException -> 0x0022 }
            goto L_0x0011
        L_0x0022:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0011
        L_0x0027:
            r0 = move-exception
        L_0x0028:
            r0.printStackTrace()
            if (r1 == 0) goto L_0x0011
            r1.close()     // Catch:{ IOException -> 0x0031 }
            goto L_0x0011
        L_0x0031:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0011
        L_0x0036:
            r4 = move-exception
        L_0x0037:
            if (r1 == 0) goto L_0x003c
            r1.close()     // Catch:{ IOException -> 0x003d }
        L_0x003c:
            throw r4
        L_0x003d:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x003c
        L_0x0042:
            r4 = move-exception
            r1 = r2
            goto L_0x0037
        L_0x0045:
            r0 = move-exception
            r1 = r2
            goto L_0x0028
        L_0x0048:
            r0 = move-exception
            r1 = r2
            goto L_0x0019
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.speech.easr.Utility.getLicenseDataFromFile(java.lang.String, byte[]):int");
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
    }

    public static boolean isNetworkConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return info != null && info.isConnected();
    }
}
