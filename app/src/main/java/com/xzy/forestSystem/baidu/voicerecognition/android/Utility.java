package com.xzy.forestSystem.baidu.voicerecognition.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import  com.xzy.forestSystem.stub.StubApp;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public final class Utility {
    private static final int BYTES_PER_SAMPLE_16BIT = 2;
    private static final int BYTES_PER_SAMPLE_8BIT = 1;
    private static final int BYTES_PER_SHORT = 2;
    private static final String TAG = "Utility";
    private static final int THOUSAND_DIV = 1000;
    private static ConnectivityManager mConnManager = null;
    private static int maxCpuFreq = 0;

    private Utility() {
    }

    static void init(Context application) {
        if (application != null) {
            mConnManager = (ConnectivityManager) application.getSystemService("connectivity");
        }
    }

    public static int getVoiceDataSizeInShort(int timeLength, int sampleRate, int audioFormat) {
        int sampleBytes;
        if (audioFormat == 2 || audioFormat == 3) {
            if (audioFormat == 3) {
                sampleBytes = 1;
            } else {
                sampleBytes = 2;
            }
            return (((timeLength * sampleRate) * sampleBytes) / 1000) / 2;
        }
        throw new IllegalArgumentException("audio format invalid");
    }

    static String generatePlatformString() {
        StringBuilder sb = new StringBuilder("Android");
        sb.append('&');
        try {
            sb.append(URLEncoder.encode(Build.MODEL, "utf-8"));
            sb.append('&');
            sb.append(URLEncoder.encode(Build.VERSION.RELEASE, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        sb.append('&');
        sb.append(Build.VERSION.SDK_INT);
        return sb.toString();
    }

    static int getStatusType(int status) {
        return -65536 & status;
    }

    private static boolean isRunningEmulator() {
        return Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk");
    }

    public static int getMaxCpuFreq() {
        String result = "";
        try {
            if (maxCpuFreq != 0) {
                return maxCpuFreq;
            }
            if (isRunningEmulator()) {
                Process process = new ProcessBuilder("/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq").start();
                InputStream in = process.getInputStream();
                byte[] re = new byte[24];
                while (in.read(re) != -1) {
                    result = result + new String(re);
                }
                in.close();
                process.destroy();
            } else {
                FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq");
                BufferedReader br = new BufferedReader(fr);
                result = br.readLine();
                br.close();
                fr.close();
            }
            int infoFreq = getCpuInfo();
            if (TextUtils.isEmpty(result)) {
                return infoFreq;
            }
            String result2 = result.trim();
            Log.d(TAG, "cpu result:" + result2);
            int maxFreq = Integer.parseInt(result2);
            if (maxFreq < infoFreq) {
                maxFreq = infoFreq;
            }
            maxCpuFreq = maxFreq;
            return maxCpuFreq;
        } catch (Exception e) {
            return 0;
        }
    }

    private static int getCpuInfo() {
        String cpuInfo = "";
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"), 1024);
            while (true) {
                String str2 = localBufferedReader.readLine();
                if (str2 != null) {
                    if (str2.indexOf("BogoMIPS") != -1) {
                        cpuInfo = str2.split("\\s+")[2];
                        break;
                    }
                } else {
                    break;
                }
            }
            localBufferedReader.close();
            String cpuInfo2 = cpuInfo.trim();
            Log.d(TAG, "cpuInfo:" + cpuInfo2);
            return (int) (Float.parseFloat(cpuInfo2) * 1000.0f);
        } catch (Exception e) {
            return 0;
        }
    }

    @SuppressLint({"DefaultLocale"})
    static boolean isUsingWifi() {
        NetworkInfo activeNetInfo;
        if (mConnManager == null || (activeNetInfo = mConnManager.getActiveNetworkInfo()) == null) {
            return false;
        }
        return "wifi".equals(activeNetInfo.getTypeName().toLowerCase());
    }

    public static boolean is2G(Context context) {
        NetworkInfo activeNetInfo = ((ConnectivityManager) StubApp.getOrigApplicationContext(context.getApplicationContext()).getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetInfo == null || !activeNetInfo.isConnectedOrConnecting() || activeNetInfo.getTypeName().toLowerCase().equals("wifi")) {
            return false;
        }
        switch (activeNetInfo.getSubtype()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
            case 16:
                return true;
            default:
                return false;
        }
    }

    static boolean isUsingWifi(Context context) {
        NetworkInfo activeNetInfo = ((ConnectivityManager) StubApp.getOrigApplicationContext(context.getApplicationContext()).getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetInfo == null || !"wifi".equals(activeNetInfo.getTypeName().toLowerCase(Locale.US))) {
            return false;
        }
        return true;
    }

    @SuppressLint({"DefaultLocale"})
    public static String getWifiOr2gOr3G(Context context) {
        NetworkInfo activeNetInfo;
        if (context == null || (activeNetInfo = ((ConnectivityManager) StubApp.getOrigApplicationContext(context.getApplicationContext()).getSystemService("connectivity")).getActiveNetworkInfo()) == null || !activeNetInfo.isConnectedOrConnecting()) {
            return "";
        }
        if (activeNetInfo.getTypeName().toLowerCase().equals("wifi")) {
            return "1";
        }
        switch (activeNetInfo.getSubtype()) {
            case 1:
            case 2:
            case 4:
            case 11:
            default:
                return "2";
            case 3:
                return "3";
            case 5:
                return "3";
            case 6:
                return "3";
            case 7:
                return "3";
            case 8:
                return "3";
            case 9:
                return "3";
            case 10:
                return "3";
            case 12:
                return "3";
            case 13:
                return "4";
            case 14:
                return "3";
            case 15:
                return "3";
        }
    }

    public static String urlEncode(String value, String charset) {
        try {
            if (!TextUtils.isEmpty(value)) {
                return URLEncoder.encode(value, charset);
            }
            return value;
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    static boolean checkPermission(Context context, String permission) {
        return context.checkCallingOrSelfPermission(permission) == 0;
    }

    public static NetworkInfo getNetworkInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
    }

    public static boolean isNetworkConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        return info != null && info.isConnected();
    }

    public static boolean isWifiConnected(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info == null || !info.isConnected() || info.getType() != 1) {
            return false;
        }
        return true;
    }
}
