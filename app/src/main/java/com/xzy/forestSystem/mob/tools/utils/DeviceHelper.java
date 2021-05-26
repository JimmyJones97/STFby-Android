package com.xzy.forestSystem.mob.tools.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.UiModeManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.os.EnvironmentCompat;

import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.utils.ReflectHelper;
import  com.xzy.forestSystem.stub.StubApp;
import com.xzy.forestSystem.swift.sandhook.annotation.MethodReflectParams;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.json.JSONArray;
import org.json.JSONException;

public class DeviceHelper {
    private static DeviceHelper deviceHelper;
    private Context context;

    public static synchronized DeviceHelper getInstance(Context c) {
        DeviceHelper deviceHelper2;
        synchronized (DeviceHelper.class) {
            if (deviceHelper == null && c != null) {
                deviceHelper = new DeviceHelper(c);
            }
            deviceHelper2 = deviceHelper;
        }
        return deviceHelper2;
    }

    private DeviceHelper(Context context2) {
        this.context = StubApp.getOrigApplicationContext(context2.getApplicationContext());
    }

    public boolean isRooted() {
        return false;
    }

    public String getSSID() {
        Object wifi;
        try {
            if (!checkPermission("android.permission.ACCESS_WIFI_STATE") || (wifi = getSystemService("wifi")) == null) {
                return null;
            }
            Object info = ReflectHelper.invokeInstanceMethod(wifi, "getConnectionInfo", new Object[0]);
            if (info == null) {
                return null;
            }
            String ssid = (String) ReflectHelper.invokeInstanceMethod(info, "getSSID", new Object[0]);
            if (ssid != null) {
                return ssid.replace("\"", "");
            }
            return null;
        } catch (Throwable t) {
            MobLog.getInstance().m69d(t);
            return null;
        }
    }

    public String getBssid() {
        Object wifi;
        try {
            if (!checkPermission("android.permission.ACCESS_WIFI_STATE") || (wifi = getSystemService("wifi")) == null) {
                return null;
            }
            Object info = ReflectHelper.invokeInstanceMethod(wifi, "getConnectionInfo", new Object[0]);
            if (info == null) {
                return null;
            }
            String bssid = (String) ReflectHelper.invokeInstanceMethod(info, "getBSSID", new Object[0]);
            if (bssid == null) {
                bssid = null;
            }
            return bssid;
        } catch (Throwable t) {
            MobLog.getInstance().m69d(t);
            return null;
        }
    }

    public String getMacAddress() {
        String hd;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                hd = getHardwareAddressFromShell("wlan0");
            } catch (Throwable t) {
                MobLog.getInstance().m69d(t);
                hd = null;
            }
            if (hd == null) {
                try {
                    hd = getCurrentNetworkHardwareAddress();
                } catch (Throwable t2) {
                    MobLog.getInstance().m69d(t2);
                    hd = null;
                }
            }
            if (hd == null) {
                try {
                    String[] hds = listNetworkHardwareAddress();
                    if (hds.length > 0) {
                        hd = hds[0];
                    }
                } catch (Throwable t3) {
                    MobLog.getInstance().m69d(t3);
                    hd = null;
                }
            }
            if (hd != null) {
                return hd;
            }
        }
        try {
            Object wifi = getSystemService("wifi");
            if (wifi == null) {
                return null;
            }
            Object info = ReflectHelper.invokeInstanceMethod(wifi, "getConnectionInfo", new Object[0]);
            if (info == null) {
                return null;
            }
            String mac = (String) ReflectHelper.invokeInstanceMethod(info, "getMacAddress", new Object[0]);
            if (mac != null) {
                return mac.trim();
            }
            return null;
        } catch (Throwable t4) {
            MobLog.getInstance().m57w(t4);
            return null;
        }
    }

    private String getCurrentNetworkHardwareAddress() throws Throwable {
        byte[] mac;
        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        if (nis == null) {
            return null;
        }
        for (NetworkInterface intf : Collections.list(nis)) {
            Enumeration<InetAddress> ias = intf.getInetAddresses();
            if (ias != null) {
                for (InetAddress add : Collections.list(ias)) {
                    if (!(add.isLoopbackAddress() || !(add instanceof Inet4Address) || (mac = intf.getHardwareAddress()) == null)) {
                        StringBuilder buf = new StringBuilder();
                        int length = mac.length;
                        for (int i = 0; i < length; i++) {
                            buf.append(String.format("%02x:", Byte.valueOf(mac[i])));
                        }
                        if (buf.length() > 0) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        return buf.toString();
                    }
                }
                continue;
            }
        }
        return null;
    }

    private String[] listNetworkHardwareAddress() throws Throwable {
        Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
        if (nis == null) {
            return null;
        }
        List<NetworkInterface> interfaces = Collections.list(nis);
        HashMap<String, String> macs = new HashMap<>();
        for (NetworkInterface intf : interfaces) {
            byte[] mac = intf.getHardwareAddress();
            if (mac != null) {
                StringBuilder buf = new StringBuilder();
                int length = mac.length;
                for (int i = 0; i < length; i++) {
                    buf.append(String.format("%02x:", Byte.valueOf(mac[i])));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                macs.put(intf.getName(), buf.toString());
            }
        }
        ArrayList<String> names = new ArrayList<>(macs.keySet());
        ArrayList<String> wlans = new ArrayList<>();
        ArrayList<String> eths = new ArrayList<>();
        ArrayList<String> rmnets = new ArrayList<>();
        ArrayList<String> dummys = new ArrayList<>();
        ArrayList<String> usbs = new ArrayList<>();
        ArrayList<String> rmnetUsbs = new ArrayList<>();
        ArrayList<String> others = new ArrayList<>();
        while (names.size() > 0) {
            String name = names.remove(0);
            if (name.startsWith("wlan")) {
                wlans.add(name);
            } else if (name.startsWith("eth")) {
                eths.add(name);
            } else if (name.startsWith("rev_rmnet")) {
                rmnets.add(name);
            } else if (name.startsWith("dummy")) {
                dummys.add(name);
            } else if (name.startsWith("usbnet")) {
                usbs.add(name);
            } else if (name.startsWith("rmnet_usb")) {
                rmnetUsbs.add(name);
            } else {
                others.add(name);
            }
        }
        Collections.sort(wlans);
        Collections.sort(eths);
        Collections.sort(rmnets);
        Collections.sort(dummys);
        Collections.sort(usbs);
        Collections.sort(rmnetUsbs);
        Collections.sort(others);
        names.addAll(wlans);
        names.addAll(eths);
        names.addAll(rmnets);
        names.addAll(dummys);
        names.addAll(usbs);
        names.addAll(rmnetUsbs);
        names.addAll(others);
        String[] macArr = new String[names.size()];
        for (int i2 = 0; i2 < macArr.length; i2++) {
            macArr[i2] = macs.get(names.get(i2));
        }
        return macArr;
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x007a A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0089 A[SYNTHETIC, Splitter:B:18:0x0089] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0092 A[SYNTHETIC, Splitter:B:23:0x0092] */
    /* JADX WARNING: Removed duplicated region for block: B:32:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getHardwareAddressFromShell(java.lang.String r10) {
        /*
            r9 = this;
            r3 = 0
            r0 = 0
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x007f }
            r5.<init>()     // Catch:{ Throwable -> 0x007f }
            java.lang.String r7 = "ca"
            r5.append(r7)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r7 = "t "
            r5.append(r7)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r7 = "/s"
            r5.append(r7)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r7 = "ys"
            r5.append(r7)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r7 = "/c"
            r5.append(r7)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r7 = "la"
            r5.append(r7)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r7 = "ss"
            r5.append(r7)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r7 = "/n"
            r5.append(r7)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r7 = "et"
            r5.append(r7)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r7 = "/"
            r5.append(r7)     // Catch:{ Throwable -> 0x007f }
            r5.append(r10)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r7 = "/a"
            r5.append(r7)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r7 = "dd"
            r5.append(r7)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r7 = "re"
            r5.append(r7)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r7 = "ss"
            r5.append(r7)     // Catch:{ Throwable -> 0x007f }
            java.lang.Runtime r7 = java.lang.Runtime.getRuntime()     // Catch:{ Throwable -> 0x007f }
            java.lang.String r8 = r5.toString()     // Catch:{ Throwable -> 0x007f }
            java.lang.Process r4 = r7.exec(r8)     // Catch:{ Throwable -> 0x007f }
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ Throwable -> 0x007f }
            java.io.InputStream r7 = r4.getInputStream()     // Catch:{ Throwable -> 0x007f }
            r2.<init>(r7)     // Catch:{ Throwable -> 0x007f }
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Throwable -> 0x007f }
            r1.<init>(r2)     // Catch:{ Throwable -> 0x007f }
            java.lang.String r3 = r1.readLine()     // Catch:{ Throwable -> 0x009b, all -> 0x0098 }
            if (r1 == 0) goto L_0x009e
            r1.close()     // Catch:{ Throwable -> 0x007c }
            r0 = r1
        L_0x0074:
            boolean r7 = android.text.TextUtils.isEmpty(r3)
            if (r7 == 0) goto L_0x007b
            r3 = 0
        L_0x007b:
            return r3
        L_0x007c:
            r7 = move-exception
            r0 = r1
            goto L_0x0074
        L_0x007f:
            r6 = move-exception
        L_0x0080:
             com.xzy.forestSystem.mob.tools.log.NLog r7 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()     // Catch:{ all -> 0x008f }
            r7.m69d(r6)     // Catch:{ all -> 0x008f }
            if (r0 == 0) goto L_0x0074
            r0.close()     // Catch:{ Throwable -> 0x008d }
            goto L_0x0074
        L_0x008d:
            r7 = move-exception
            goto L_0x0074
        L_0x008f:
            r7 = move-exception
        L_0x0090:
            if (r0 == 0) goto L_0x0095
            r0.close()     // Catch:{ Throwable -> 0x0096 }
        L_0x0095:
            throw r7
        L_0x0096:
            r8 = move-exception
            goto L_0x0095
        L_0x0098:
            r7 = move-exception
            r0 = r1
            goto L_0x0090
        L_0x009b:
            r6 = move-exception
            r0 = r1
            goto L_0x0080
        L_0x009e:
            r0 = r1
            goto L_0x0074
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.utils.DeviceHelper.getHardwareAddressFromShell(java.lang.String):java.lang.String");
    }

    public String getModel() {
        String model = Build.MODEL;
        if (!TextUtils.isEmpty(model)) {
            return model.trim();
        }
        return model;
    }

    public String getManufacturer() {
        return Build.MANUFACTURER;
    }

    public String getDeviceId() {
        String deviceId = getIMEI();
        if (!TextUtils.isEmpty(deviceId) || Build.VERSION.SDK_INT < 9) {
            return deviceId;
        }
        return getSerialno();
    }

    public String getIMEI() {
        Object phone = getSystemService("phone");
        if (phone == null) {
            return null;
        }
        String deviceId = null;
        try {
            if (checkPermission("android.permission.READ_PHONE_STATE")) {
                deviceId = (String) ReflectHelper.invokeInstanceMethod(phone, "getDeviceId", new Object[0]);
            }
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
        }
        if (TextUtils.isEmpty(deviceId)) {
            return null;
        }
        return deviceId.trim();
    }

    public String getSerialno() {
        String serialno = null;
        if (Build.VERSION.SDK_INT >= 9) {
            try {
                ReflectHelper.importClass("android.os.SystemProperties");
                StringBuilder sb1 = new StringBuilder();
                sb1.append("ge");
                sb1.append("t");
                serialno = (String) ReflectHelper.invokeStaticMethod("SystemProperties", sb1.toString(), "ro.serialno", EnvironmentCompat.MEDIA_UNKNOWN);
            } catch (Throwable t) {
                MobLog.getInstance().m69d(t);
                serialno = null;
            }
        }
        if (!TextUtils.isEmpty(serialno)) {
            return serialno.trim();
        }
        return serialno;
    }

    public String getDeviceData() {
        return Base64AES(getModel() + "|" + getOSVersionInt() + "|" + getManufacturer() + "|" + getCarrier() + "|" + getScreenSize(), getDeviceKey().substring(0, 16));
    }

    public String getDeviceDataNotAES() {
        return getModel() + "|" + getOSVersionInt() + "|" + getManufacturer() + "|" + getCarrier() + "|" + getScreenSize();
    }

    public String Base64AES(String msg, String key) {
        try {
            String result = Base64.encodeToString(Data.AES128Encode(key, msg), 0);
            if (result.contains("\n")) {
                return result.replace("\n", "");
            }
            return result;
        } catch (Throwable e) {
            MobLog.getInstance().m57w(e);
            return null;
        }
    }

    public int getOSVersionInt() {
        return Build.VERSION.SDK_INT;
    }

    public String getOSVersionName() {
        return Build.VERSION.RELEASE;
    }

    public String getOSLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public String getAppLanguage() {
        return this.context.getResources().getConfiguration().locale.getLanguage();
    }

    public String getOSCountry() {
        return Locale.getDefault().getCountry();
    }

    public String getScreenSize() {
        int[] size = ResHelper.getScreenSize(this.context);
        if (this.context.getResources().getConfiguration().orientation == 1) {
            return size[0] + "x" + size[1];
        }
        return size[1] + "x" + size[0];
    }

    public String getCarrier() {
        try {
            Object tm = getSystemService("phone");
            if (tm == null) {
                return "-1";
            }
            String operator = (String) ReflectHelper.invokeInstanceMethod(tm, "getSimOperator", new Object[0]);
            if (TextUtils.isEmpty(operator)) {
                return "-1";
            }
            return operator;
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
            return "-1";
        }
    }

    public String getCarrierName() {
        Object tm = getSystemService("phone");
        if (tm == null) {
            return null;
        }
        try {
            if (checkPermission("android.permission.READ_PHONE_STATE")) {
                String operator = (String) ReflectHelper.invokeInstanceMethod(tm, "getSimOperatorName", new Object[0]);
                if (TextUtils.isEmpty(operator)) {
                    return null;
                }
                return operator;
            }
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
        }
        return null;
    }

    public String getMCC() {
        String imsi = getIMSI();
        if (imsi == null || imsi.length() < 3) {
            return null;
        }
        return imsi.substring(0, 3);
    }

    public String getMNC() {
        String imsi = getIMSI();
        if (imsi == null || imsi.length() < 5) {
            return null;
        }
        return imsi.substring(3, 5);
    }

    public String getSimSerialNumber() {
        try {
            Object tm = getSystemService("phone");
            if (tm == null) {
                return "-1";
            }
            return (String) ReflectHelper.invokeInstanceMethod(tm, "getSimSerialNumber", new Object[0]);
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
            return "-1";
        }
    }

    public String getLine1Number() {
        try {
            Object tm = getSystemService("phone");
            if (tm == null) {
                return "-1";
            }
            return (String) ReflectHelper.invokeInstanceMethod(tm, "getLine1Number", new Object[0]);
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
            return "-1";
        }
    }

    public String getBluetoothName() {
        try {
            ReflectHelper.importClass("android.bluetooth.BluetoothAdapter");
            if (checkPermission("android.permission.BLUETOOTH")) {
                Object myDevice = ReflectHelper.invokeStaticMethod("BluetoothAdapter", "getDefaultAdapter", new Object[0]);
                if (myDevice != null) {
                    return (String) ReflectHelper.invokeInstanceMethod(myDevice, "getName", new Object[0]);
                }
            }
        } catch (Throwable e) {
            MobLog.getInstance().m69d(e);
        }
        return null;
    }

    @SuppressLint("WrongConstant")
    public String getSignMD5() {
        try {
            return Data.MD5(this.context.getPackageManager().getPackageInfo(getPackageName(), 64).signatures[0].toByteArray());
        } catch (Exception e) {
            MobLog.getInstance().m57w(e);
            return null;
        }
    }

    private Object getSystemService(String name) {
        try {
            return this.context.getSystemService(name);
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
            return null;
        }
    }

    public String getNetworkType() {
        NetworkInfo network;
        ConnectivityManager conn = (ConnectivityManager) getSystemService("connectivity");
        if (conn == null) {
            return "none";
        }
        try {
            if (!checkPermission("android.permission.ACCESS_NETWORK_STATE") || (network = conn.getActiveNetworkInfo()) == null || !network.isAvailable()) {
                return "none";
            }
            int type = network.getType();
            switch (type) {
                case 0:
                    if (is4GMobileNetwork()) {
                        return "4G";
                    }
                    return isFastMobileNetwork() ? "3G" : "2G";
                case 1:
                    return "wifi";
                case 2:
                case 3:
                case 4:
                case 5:
                default:
                    return String.valueOf(type);
                case 6:
                    return "wimax";
                case 7:
                    return "bluetooth";
                case 8:
                    return "dummy";
                case 9:
                    return "ethernet";
            }
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
            return "none";
        }
    }

    public String getNetworkTypeForStatic() {
        String networkType = getNetworkType().toLowerCase();
        if (TextUtils.isEmpty(networkType) || "none".equals(networkType)) {
            return "none";
        }
        if (networkType.startsWith("4g") || networkType.startsWith("3g") || networkType.startsWith("2g")) {
            return "cell";
        }
        if (networkType.startsWith("wifi")) {
            return "wifi";
        }
        return "other";
    }

    public String getDetailNetworkTypeForStatic() {
        String networkType = getNetworkType().toLowerCase();
        if (TextUtils.isEmpty(networkType) || "none".equals(networkType)) {
            return "none";
        }
        if (networkType.startsWith("wifi")) {
            return "wifi";
        }
        if (networkType.startsWith("4g")) {
            return "4g";
        }
        if (networkType.startsWith("3g")) {
            return "3g";
        }
        if (networkType.startsWith("2g")) {
            return "2g";
        }
        if (networkType.startsWith("bluetooth")) {
            return "bluetooth";
        }
        return networkType;
    }

    public int getPlatformCode() {
        return 1;
    }

    private boolean is4GMobileNetwork() {
        Object phone = getSystemService("phone");
        if (phone == null) {
            return false;
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("ge");
            sb.append("tN");
            sb.append("et");
            sb.append("wo");
            sb.append("rk");
            sb.append("Ty");
            sb.append("pe");
            return ((Integer) ReflectHelper.invokeInstanceMethod(phone, sb.toString(), new Object[0])).intValue() == 13;
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
            return false;
        }
    }

    private boolean isFastMobileNetwork() {
        Object phone = getSystemService("phone");
        if (phone == null) {
            return false;
        }
        try {
            switch (((Integer) ReflectHelper.invokeInstanceMethod(phone, "getNetworkType", new Object[0])).intValue()) {
                case 0:
                    return false;
                case 1:
                    return false;
                case 2:
                    return false;
                case 3:
                    return true;
                case 4:
                    return false;
                case 5:
                    return true;
                case 6:
                    return true;
                case 7:
                    return false;
                case 8:
                    return true;
                case 9:
                    return true;
                case 10:
                    return true;
                case 11:
                    return false;
                case 12:
                    return true;
                case 13:
                    return true;
                case 14:
                    return true;
                case 15:
                    return true;
            }
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
        }
        return false;
    }

    public JSONArray getRunningApp() {
        JSONArray appNmes = new JSONArray();
        Object am = getSystemService("activity");
        if (am != null) {
            try {
                List<?> apps = (List) ReflectHelper.invokeInstanceMethod(am, "getRunningAppProcesses", new Object[0]);
                if (apps != null) {
                    for (Object app : apps) {
                        appNmes.put(ReflectHelper.getInstanceField(app, "processName"));
                    }
                }
            } catch (Throwable t) {
                MobLog.getInstance().m57w(t);
            }
        }
        return appNmes;
    }

    public String getRunningAppStr() throws JSONException {
        JSONArray apps = getRunningApp();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < apps.length(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(String.valueOf(apps.get(i)));
        }
        return sb.toString();
    }

    public String getDeviceKey() {
        String deviceKey;
        String deviceKey2 = null;
        try {
            deviceKey2 = getDeviceKeyWithDuid("comm/dbs/.duid");
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
        }
        if (TextUtils.isEmpty(deviceKey2) || deviceKey2.length() < 40) {
            deviceKey2 = genDeviceKey();
        }
        if (!TextUtils.isEmpty(deviceKey2) && deviceKey2.length() >= 40) {
            return deviceKey2.trim();
        }
        try {
            deviceKey = getLocalDeviceKey();
        } catch (Throwable t2) {
            MobLog.getInstance().m57w(t2);
            deviceKey = null;
        }
        if (!TextUtils.isEmpty(deviceKey) && deviceKey.length() >= 40) {
            return deviceKey.trim();
        }
        if (TextUtils.isEmpty(deviceKey) || deviceKey.length() < 40) {
            deviceKey = getCharAndNumr(40);
        }
        if (deviceKey != null) {
            try {
                deviceKey = deviceKey.trim();
                saveLocalDeviceKey(deviceKey);
            } catch (Throwable t3) {
                MobLog.getInstance().m57w(t3);
            }
        }
        return deviceKey;
    }

    private String genDeviceKey() {
        try {
            String mac = getMacAddress();
            String udid = getDeviceId();
            return Data.byteToHex(Data.SHA1(mac + ":" + udid + ":" + getModel()));
        } catch (Throwable t) {
            MobLog.getInstance().m69d(t);
            return null;
        }
    }

    public String getCharAndNumr(int length) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(System.currentTimeMillis() ^ SystemClock.elapsedRealtime());
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            if (MethodReflectParams.CHAR.equalsIgnoreCase(random.nextInt(2) % 2 == 0 ? MethodReflectParams.CHAR : "num")) {
                stringBuffer.insert(i + 1, (char) (random.nextInt(26) + 97));
            } else {
                stringBuffer.insert(stringBuffer.length(), random.nextInt(10));
            }
        }
        return stringBuffer.toString().substring(0, 40);
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0036 A[ADDED_TO_REGION, ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0042 A[SYNTHETIC, Splitter:B:20:0x0042] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x004b A[SYNTHETIC, Splitter:B:25:0x004b] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0058  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getDeviceKeyWithDuid(java.lang.String r17) throws java.lang.Throwable {
        /*
            r16 = this;
            r9 = 0
            java.io.File r6 = new java.io.File     // Catch:{ Throwable -> 0x004f }
            r0 = r16
            android.content.Context r14 = r0.context     // Catch:{ Throwable -> 0x004f }
            java.lang.String r14 =  com.xzy.forestSystem.mob.tools.utils.ResHelper.getCacheRoot(r14)     // Catch:{ Throwable -> 0x004f }
            r0 = r17
            r6.<init>(r14, r0)     // Catch:{ Throwable -> 0x004f }
            boolean r14 = r6.exists()     // Catch:{ Throwable -> 0x004f }
            if (r14 == 0) goto L_0x0034
            boolean r14 = r6.isFile()     // Catch:{ Throwable -> 0x004f }
            if (r14 == 0) goto L_0x0034
            r11 = 0
            java.io.FileInputStream r7 = new java.io.FileInputStream     // Catch:{ Throwable -> 0x0038 }
            r7.<init>(r6)     // Catch:{ Throwable -> 0x0038 }
            java.io.ObjectInputStream r12 = new java.io.ObjectInputStream     // Catch:{ Throwable -> 0x0038 }
            r12.<init>(r7)     // Catch:{ Throwable -> 0x0038 }
            java.lang.Object r14 = r12.readObject()     // Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r0 = r14
            java.util.HashMap r0 = (java.util.HashMap) r0     // Catch:{ Throwable -> 0x00d1, all -> 0x00cd }
            r9 = r0
            if (r12 == 0) goto L_0x0034
            r12.close()     // Catch:{ Throwable -> 0x00c8 }
        L_0x0034:
            if (r9 != 0) goto L_0x0058
            r5 = 0
        L_0x0037:
            return r5
        L_0x0038:
            r13 = move-exception
        L_0x0039:
             com.xzy.forestSystem.mob.tools.log.NLog r14 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()     // Catch:{ all -> 0x0048 }
            r14.m57w(r13)     // Catch:{ all -> 0x0048 }
            if (r11 == 0) goto L_0x0034
            r11.close()     // Catch:{ Throwable -> 0x0046 }
            goto L_0x0034
        L_0x0046:
            r14 = move-exception
            goto L_0x0034
        L_0x0048:
            r14 = move-exception
        L_0x0049:
            if (r11 == 0) goto L_0x004e
            r11.close()     // Catch:{ Throwable -> 0x00cb }
        L_0x004e:
            throw r14
        L_0x004f:
            r13 = move-exception
             com.xzy.forestSystem.mob.tools.log.NLog r14 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            r14.m57w(r13)
            goto L_0x0034
        L_0x0058:
            java.lang.String r14 = "deviceInfo"
            java.lang.Object r4 = r9.get(r14)
            java.util.HashMap r4 = (java.util.HashMap) r4
            if (r4 != 0) goto L_0x0064
            r5 = 0
            goto L_0x0037
        L_0x0064:
            java.lang.String r5 = ""
            java.lang.String r14 = "mac"
            java.lang.Object r8 = r4.get(r14)     // Catch:{ Throwable -> 0x00bd }
            java.lang.String r8 = (java.lang.String) r8     // Catch:{ Throwable -> 0x00bd }
            java.lang.String r14 = "imei"
            java.lang.Object r3 = r4.get(r14)     // Catch:{ Throwable -> 0x00bd }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Throwable -> 0x00bd }
            boolean r14 = android.text.TextUtils.isEmpty(r3)     // Catch:{ Throwable -> 0x00bd }
            if (r14 == 0) goto L_0x008a
            int r14 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x00bd }
            r15 = 9
            if (r14 < r15) goto L_0x008a
            java.lang.String r14 = "serialno"
            java.lang.Object r3 = r4.get(r14)     // Catch:{ Throwable -> 0x00bd }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Throwable -> 0x00bd }
        L_0x008a:
            java.lang.String r14 = "model"
            java.lang.Object r10 = r4.get(r14)     // Catch:{ Throwable -> 0x00bd }
            java.lang.String r10 = (java.lang.String) r10     // Catch:{ Throwable -> 0x00bd }
            java.lang.StringBuilder r14 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00bd }
            r14.<init>()     // Catch:{ Throwable -> 0x00bd }
            java.lang.StringBuilder r14 = r14.append(r8)     // Catch:{ Throwable -> 0x00bd }
            java.lang.String r15 = ":"
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Throwable -> 0x00bd }
            java.lang.StringBuilder r14 = r14.append(r3)     // Catch:{ Throwable -> 0x00bd }
            java.lang.String r15 = ":"
            java.lang.StringBuilder r14 = r14.append(r15)     // Catch:{ Throwable -> 0x00bd }
            java.lang.StringBuilder r14 = r14.append(r10)     // Catch:{ Throwable -> 0x00bd }
            java.lang.String r2 = r14.toString()     // Catch:{ Throwable -> 0x00bd }
            byte[] r1 =  com.xzy.forestSystem.mob.tools.utils.Data.SHA1(r2)     // Catch:{ Throwable -> 0x00bd }
            java.lang.String r5 =  com.xzy.forestSystem.mob.tools.utils.Data.byteToHex(r1)     // Catch:{ Throwable -> 0x00bd }
            goto L_0x0037
        L_0x00bd:
            r13 = move-exception
             com.xzy.forestSystem.mob.tools.log.NLog r14 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            r14.m69d(r13)
            r5 = 0
            goto L_0x0037
        L_0x00c8:
            r14 = move-exception
            goto L_0x0034
        L_0x00cb:
            r15 = move-exception
            goto L_0x004e
        L_0x00cd:
            r14 = move-exception
            r11 = r12
            goto L_0x0049
        L_0x00d1:
            r13 = move-exception
            r11 = r12
            goto L_0x0039
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.utils.DeviceHelper.getDeviceKeyWithDuid(java.lang.String):java.lang.String");
    }

    private String getLocalDeviceKey() throws Throwable {
        String strKey = null;
        if (getSdcardState()) {
            File cacheRoot = new File(getSdcardPath(), "ShareSDK");
            if (cacheRoot.exists()) {
                File keyFile = new File(cacheRoot, ".dk");
                if (keyFile.exists() && keyFile.renameTo(new File(ResHelper.getCacheRoot(this.context), ".dk"))) {
                    keyFile.delete();
                }
            }
            File keyFile2 = new File(ResHelper.getCacheRoot(this.context), ".dk");
            if (keyFile2.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(keyFile2));
                Object key = ois.readObject();
                strKey = null;
                if (key != null && (key instanceof char[])) {
                    strKey = String.valueOf((char[]) ((char[]) key));
                }
                ois.close();
            }
        }
        return strKey;
    }

    private void saveLocalDeviceKey(String key) throws Throwable {
        if (getSdcardState()) {
            File keyFile = new File(ResHelper.getCacheRoot(this.context), ".dk");
            if (keyFile.exists()) {
                keyFile.delete();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(keyFile));
            oos.writeObject(key.toCharArray());
            oos.flush();
            oos.close();
        }
    }

    public String getPackageName() {
        return this.context.getPackageName();
    }

    public String getAppName() {
        String appName;
        String appName2 = this.context.getApplicationInfo().name;
        if (appName2 != null) {
            if (Build.VERSION.SDK_INT < 25 || appName2.endsWith(".*")) {
                return appName2;
            }
            try {
                ReflectHelper.importClass(appName2);
            } catch (Throwable th) {
            }
        }
        int appLbl = this.context.getApplicationInfo().labelRes;
        if (appLbl > 0) {
            appName = this.context.getString(appLbl);
        } else {
            appName = String.valueOf(this.context.getApplicationInfo().nonLocalizedLabel);
        }
        return appName;
    }

    public int getAppVersion() {
        try {
            return this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0).versionCode;
        } catch (Throwable t) {
            MobLog.getInstance().m69d(t);
            return 0;
        }
    }

    public String getAppVersionName() {
        try {
            return this.context.getPackageManager().getPackageInfo(this.context.getPackageName(), 0).versionName;
        } catch (Throwable t) {
            MobLog.getInstance().m69d(t);
            return "1.0";
        }
    }

    public ArrayList<HashMap<String, String>> getInstalledApp(boolean includeSystemApp) {
        CharSequence label;
        try {
            ArrayList<String> packages = new ArrayList<>();
            try {
                java.lang.Process p = Runtime.getRuntime().exec("pm list packages");
                BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "utf-8"));
                for (String line = br.readLine(); line != null; line = br.readLine()) {
                    String line2 = line.toLowerCase().trim();
                    if (line2.startsWith("package:")) {
                        packages.add(line2.substring("package:".length()).trim());
                    }
                }
                br.close();
                p.destroy();
            } catch (Throwable t) {
                MobLog.getInstance().m57w(t);
            }
            ArrayList<HashMap<String, String>> apps = new ArrayList<>();
            PackageManager pm = this.context.getPackageManager();
            Iterator<String> it = packages.iterator();
            while (it.hasNext()) {
                PackageInfo pi = null;
                try {
                    pi = pm.getPackageInfo(it.next(), 0);
                } catch (Throwable t2) {
                    MobLog.getInstance().m69d(t2);
                }
                if (pi != null && (includeSystemApp || !isSystemApp(pi))) {
                    HashMap<String, String> app = new HashMap<>();
                    app.put("pkg", pi.packageName);
                    String appName = pi.applicationInfo.name;
                    if (appName == null) {
                        int appLbl = pi.applicationInfo.labelRes;
                        if (appLbl > 0 && (label = pm.getText(pi.packageName, appLbl, pi.applicationInfo)) != null) {
                            appName = label.toString().trim();
                        }
                        if (appName == null) {
                            appName = String.valueOf(pi.applicationInfo.nonLocalizedLabel);
                        }
                    }
                    app.put("name", appName);
                    app.put("version", pi.versionName);
                    apps.add(app);
                }
            }
            return apps;
        } catch (Throwable t3) {
            MobLog.getInstance().m57w(t3);
            return new ArrayList<>();
        }
    }

    private boolean isSystemApp(PackageInfo pi) {
        boolean isSysApp;
        boolean isSysUpd;
        if ((pi.applicationInfo.flags & 1) == 1) {
            isSysApp = true;
        } else {
            isSysApp = false;
        }
        if ((pi.applicationInfo.flags & 128) == 1) {
            isSysUpd = true;
        } else {
            isSysUpd = false;
        }
        return isSysApp || isSysUpd;
    }

    public String getNetworkOperator() {
        Object tm = getSystemService("phone");
        if (tm == null) {
            return null;
        }
        try {
            return (String) ReflectHelper.invokeInstanceMethod(tm, "getNetworkOperator", new Object[0]);
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
            return null;
        }
    }

    public boolean checkPermission(String permission) throws Throwable {
        int res;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                ReflectHelper.importClass("android.content.Context");
                StringBuilder sb1 = new StringBuilder();
                sb1.append("ch");
                sb1.append("ec");
                sb1.append("kS");
                sb1.append("el");
                sb1.append("fP");
                sb1.append("er");
                sb1.append("mi");
                sb1.append("ss");
                sb1.append("io");
                sb1.append("n");
                Integer ret = (Integer) ReflectHelper.invokeInstanceMethod(this.context, sb1.toString(), permission);
                res = ret == null ? -1 : ret.intValue();
            } catch (Throwable t) {
                MobLog.getInstance().m69d(t);
                res = -1;
            }
        } else {
            this.context.checkPermission(permission, Process.myPid(), Process.myUid());
            res = this.context.getPackageManager().checkPermission(permission, getPackageName());
        }
        return res == 0;
    }

    public String getTopTaskPackageName() {
        boolean hasPer;
        try {
            hasPer = checkPermission("android.permission.GET_TASKS");
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
            hasPer = false;
        }
        if (hasPer) {
            try {
                Object am = getSystemService("activity");
                if (am == null) {
                    return null;
                }
                if (Build.VERSION.SDK_INT <= 20) {
                    Object topActivity = ReflectHelper.getInstanceField(((List) ReflectHelper.invokeInstanceMethod(am, "getRunningTasks", 1)).get(0), "topActivity");
                    return (String) ReflectHelper.invokeInstanceMethod(topActivity, "getPackageName", new Object[0]);
                }
                return ((String) ReflectHelper.getInstanceField(((List) ReflectHelper.invokeInstanceMethod(am, "getRunningAppProcesses", new Object[0])).get(0), "processName")).split(":")[0];
            } catch (Throwable t2) {
                MobLog.getInstance().m69d(t2);
            }
        }
        return null;
    }

    public boolean amIOnForeground() {
        try {
            Object thread = currentActivityThread();
            for (Object activity : ((Map) ReflectHelper.getInstanceField(thread, "mActivities")).values()) {
                if (!((Boolean) ReflectHelper.getInstanceField(activity, "stopped")).booleanValue()) {
                    return true;
                }
            }
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
        }
        return false;
    }

    public boolean getSdcardState() {
        try {
            if (!checkPermission("android.permission.WRITE_EXTERNAL_STORAGE") || !"mounted".equals(Environment.getExternalStorageState())) {
                return false;
            }
            return true;
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
            return false;
        }
    }

    public String getSdcardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public String getAndroidID() {
        String androidId = Settings.Secure.getString(this.context.getContentResolver(), "android_id");
        MobLog.getInstance().m64i("getAndroidID === " + androidId, new Object[0]);
        return androidId;
    }

    @SuppressLint("WrongConstant")
    public String getAdvertisingID() throws Throwable {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new Throwable("Do not call this function from the main thread !");
        }
        Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
        intent.setPackage("com.google.android.gms");
        GSConnection gsc = new GSConnection();
        String adsid = null;
        try {
            this.context.bindService(intent, gsc, 1);
            IBinder binder = gsc.takeBinder();
            Parcel input = Parcel.obtain();
            Parcel output = Parcel.obtain();
            input.writeInterfaceToken("com.google.android.gms.ads.identifier.internal.IAdvertisingIdService");
            binder.transact(1, input, output, 0);
            output.readException();
            adsid = output.readString();
            output.recycle();
            input.recycle();
            return adsid;
        } catch (Throwable t) {
            MobLog.getInstance().m69d(t);
            return adsid;
        } finally {
            this.context.unbindService(gsc);
        }
    }

    public void hideSoftInput(View view) {
        Object service = getSystemService("input_method");
        if (service != null) {
            ((InputMethodManager) service).hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showSoftInput(View view) {
        Object service = getSystemService("input_method");
        if (service != null) {
            ((InputMethodManager) service).toggleSoftInputFromWindow(view.getWindowToken(), 2, 0);
        }
    }

    public boolean isMainProcess(int pid) {
        int myPid;
        try {
            Object am = getSystemService("activity");
            List<?> rps = (List) ReflectHelper.invokeInstanceMethod(am, "getRunningAppProcesses", new Object[0]);
            if (rps != null) {
                String application = null;
                if (pid <= 0) {
                    myPid = Process.myPid();
                } else {
                    myPid = pid;
                }
                Iterator<?> it = rps.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Object appProcess = it.next();
                    if (((Integer) ReflectHelper.getInstanceField(appProcess, "pid")).intValue() == myPid) {
                        application = (String) ReflectHelper.getInstanceField(appProcess, "processName");
                        break;
                    }
                }
                return getPackageName().equals(application);
            } else if (pid <= 0) {
                return true;
            } else {
                return false;
            }
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
            return false;
        }
    }

    public String getIMSI() {
        Object phone = getSystemService("phone");
        if (phone == null) {
            return null;
        }
        String imsi = null;
        try {
            if (checkPermission("android.permission.READ_PHONE_STATE")) {
                imsi = (String) ReflectHelper.invokeInstanceMethod(phone, "getSubscriberId", new Object[0]);
            }
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
        }
        if (TextUtils.isEmpty(imsi)) {
            return null;
        }
        return imsi;
    }

    public String getIPAddress() {
        try {
            if (checkPermission("android.permission.INTERNET")) {
                Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                while (en.hasMoreElements()) {
                    Enumeration<InetAddress> enumIpAddr = en.nextElement().getInetAddresses();
                    while (true) {
                        if (enumIpAddr.hasMoreElements()) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                }
            }
        } catch (Throwable e) {
            MobLog.getInstance().m57w(e);
        }
        return "0.0.0.0";
    }

    public Location getLocation(int GPSTimeout, int networkTimeout, boolean useLastKnown) {
        try {
            if (checkPermission("android.permission.ACCESS_FINE_LOCATION")) {
                return new LocationHelper().getLocation(this.context, GPSTimeout, networkTimeout, useLastKnown);
            }
        } catch (Throwable t) {
            MobLog.getInstance().m69d(t);
        }
        return null;
    }

    public HashMap<String, String> ping(String address, int count, int packetsize) {
        ArrayList<Float> sucRes = new ArrayList<>();
        try {
            int bytes = packetsize + 8;
            java.lang.Process p = Runtime.getRuntime().exec("ping -c " + count + " -s " + packetsize + " " + address);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = br.readLine();
            while (line != null) {
                if (line.startsWith(bytes + " bytes from")) {
                    if (line.endsWith("ms")) {
                        line = line.substring(0, line.length() - 2).trim();
                    } else if (line.endsWith("s")) {
                        line = line.substring(0, line.length() - 1).trim() + "000";
                    }
                    int i = line.indexOf("time=");
                    if (i > 0) {
                        try {
                            sucRes.add(Float.valueOf(Float.parseFloat(line.substring(i + 5).trim())));
                        } catch (Throwable t) {
                            MobLog.getInstance().m57w(t);
                        }
                    }
                }
                line = br.readLine();
            }
            p.waitFor();
        } catch (Throwable t2) {
            MobLog.getInstance().m69d(t2);
        }
        int sucCount = sucRes.size();
        int fldCount = count - sucRes.size();
        float min = 0.0f;
        float max = 0.0f;
        float average = 0.0f;
        if (sucCount > 0) {
            min = Float.MAX_VALUE;
            for (int i2 = 0; i2 < sucCount; i2++) {
                float item = sucRes.get(i2).floatValue();
                if (item < min) {
                    min = item;
                }
                if (item > max) {
                    max = item;
                }
                average += item;
            }
            average /= (float) sucCount;
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("address", address);
        map.put("transmitted", String.valueOf(count));
        map.put("received", String.valueOf(sucCount));
        map.put("loss", String.valueOf(fldCount));
        map.put("min", String.valueOf(min));
        map.put("max", String.valueOf(max));
        map.put("avg", String.valueOf(average));
        return map;
    }

    public int getCellId() {
        Object tm;
        try {
            if (checkPermission("android.permission.ACCESS_COARSE_LOCATION") && (tm = getSystemService("phone")) != null) {
                Object loc = ReflectHelper.invokeInstanceMethod(tm, "getCellLocation", new Object[0]);
                if (loc != null) {
                    return ((Integer) ResHelper.forceCast(ReflectHelper.invokeInstanceMethod(loc, "getCid", new Object[0]), -1)).intValue();
                }
            }
        } catch (Throwable t) {
            MobLog.getInstance().m69d(t);
        }
        return -1;
    }

    public int getCellLac() {
        Object tm;
        try {
            if (checkPermission("android.permission.ACCESS_COARSE_LOCATION") && (tm = getSystemService("phone")) != null) {
                Object loc = ReflectHelper.invokeInstanceMethod(tm, "getCellLocation", new Object[0]);
                if (loc != null) {
                    return ((Integer) ResHelper.forceCast(ReflectHelper.invokeInstanceMethod(loc, "getLac", new Object[0]), -1)).intValue();
                }
            }
        } catch (Throwable t) {
            MobLog.getInstance().m69d(t);
        }
        return -1;
    }

    public String getDeviceType() {
        UiModeManager um = (UiModeManager) getSystemService("uimode");
        if (um != null) {
            switch (um.getCurrentModeType()) {
                case 1:
                    return "NO_UI";
                case 2:
                    return "DESK";
                case 3:
                    return "CAR";
                case 4:
                    return "TELEVISION";
                case 5:
                    return "APPLIANCE";
                case 6:
                    return "WATCH";
            }
        }
        return "UNDEFINED";
    }

    public Activity getTopActivity() {
        try {
            Object thread = currentActivityThread();
            Map activities = (Map) ReflectHelper.getInstanceField(thread, "mActivities");
            for (Object activity : activities.values()) {
                if (!((Boolean) ReflectHelper.getInstanceField(activity, "paused")).booleanValue()) {
                    return (Activity) ReflectHelper.getInstanceField(activity, "activity");
                }
            }
            for (Object activity2 : activities.values()) {
                if (!((Boolean) ReflectHelper.getInstanceField(activity2, "stopped")).booleanValue()) {
                    return (Activity) ReflectHelper.getInstanceField(activity2, "activity");
                }
            }
        } catch (Throwable th) {
        }
        return null;
    }

    public Object currentActivityThread() {
        final ReflectHelper.ReflectRunnable mainThreadAct = new ReflectHelper.ReflectRunnable() { // from class:  com.xzy.forestSystem.mob.tools.utils.DeviceHelper.1
            @Override //  com.xzy.forestSystem.mob.tools.utils.ReflectHelper.ReflectRunnable
            public Object run(Object arg) {
                try {
                    String clzName = ReflectHelper.importClass("android.app.ActivityThread");
                    StringBuilder sb1 = new StringBuilder();
                    sb1.append("cu");
                    sb1.append("rr");
                    sb1.append("en");
                    sb1.append("tA");
                    sb1.append("ct");
                    sb1.append("iv");
                    sb1.append("it");
                    sb1.append("yT");
                    sb1.append("hr");
                    sb1.append("ea");
                    sb1.append("d");
                    ReflectHelper.invokeStaticMethod(clzName, sb1.toString(), new Object[0]);
                    return null;
                } catch (Throwable t) {
                    MobLog.getInstance().m57w(t);
                    return null;
                }
            }
        };
        if (Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId()) {
            return mainThreadAct.run(null);
        }
        final Object lock = new Object();
        final Object[] output = new Object[1];
        synchronized (lock) {
            UIHandler.sendEmptyMessage(0, new Handler.Callback() { // from class:  com.xzy.forestSystem.mob.tools.utils.DeviceHelper.2
                @Override // android.os.Handler.Callback
                public boolean handleMessage(Message msg) {
                    synchronized (lock) {
                        try {
                            output[0] = mainThreadAct.run(null);
                            lock.notify();
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                    return false;
                }
            });
            try {
                lock.wait();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return output[0];
    }

    private class GSConnection implements ServiceConnection {
        boolean got;
        private final BlockingQueue<IBinder> iBinders;

        private GSConnection() {
            this.got = false;
            this.iBinders = new LinkedBlockingQueue();
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                this.iBinders.put(service);
            } catch (Throwable t) {
                MobLog.getInstance().m57w(t);
            }
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName name) {
        }

        public IBinder takeBinder() throws InterruptedException {
            if (this.got) {
                throw new IllegalStateException();
            }
            this.got = true;
            return this.iBinders.poll(1500, TimeUnit.MILLISECONDS);
        }
    }
}
