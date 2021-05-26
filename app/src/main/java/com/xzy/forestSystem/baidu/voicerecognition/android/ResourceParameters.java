package com.xzy.forestSystem.baidu.voicerecognition.android;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import  com.xzy.forestSystem.stub.StubApp;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ResourceParameters {
    private static final String KEY_COOKIE_URL = "http://m.baidu.com";
    private static final String KEY_RESERVE_MAP_VERSION = "mapver";
    private static final String KEY_RESERVE_NET_TYPE = "net_type";
    private static final String LOG_TAG = "ResourceParameters";
    private static final ResourceParameters sInstance = new ResourceParameters();
    private String mBrowserUA;
    private String mCFrom;
    private String mCUID;
    private WeakReference<Context> mContext;
    private CookieManager mCookieManager;
    private String mDeviceInfo = generateDeviceInfo();
    private String mFrom;
    private String mOriginalBUA;
    private String mOriginalPu;
    private String mProductLine = "baiduvoice";
    private String mPu;
    private Map<String, String> mReserves = new HashMap();
    private String mUA;

    private ResourceParameters() {
    }

    private void appendPu(StringBuffer sb, String key, String value) {
        if (sb != null && !TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(key);
            sb.append("@");
            sb.append(Utility.urlEncode(value, "utf-8"));
        }
    }

    public String buildPU(String csrc) {
        String puAfterUrl = this.mPu;
        if (!TextUtils.isEmpty(csrc)) {
            StringBuffer pu = new StringBuffer();
            if (!TextUtils.isEmpty(puAfterUrl)) {
                pu.append(puAfterUrl);
            }
            appendPu(pu, "csrc", csrc);
            puAfterUrl = pu.toString();
        }
        return Utility.urlEncode(puAfterUrl, "utf-8");
    }

    public String buildReserved(Map<String, String> extras) {
        Context context;
        Map<String, String> reserves = new HashMap<>(this.mReserves);
        if (this.mContext == null) {
            context = null;
        } else {
            context = this.mContext.get();
        }
        if (context != null) {
            reserves.put(KEY_RESERVE_NET_TYPE, Utility.getWifiOr2gOr3G(context));
        }
        if (extras != null) {
            reserves.putAll(extras);
        }
        Set<Map.Entry<String, String>> entries = reserves.entrySet();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : entries) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        return sb.toString();
    }

    private String generateDeviceInfo() {
        String model = Build.MODEL;
        String info = model.replace("_", "-") + "_" + Build.VERSION.RELEASE + "_" + Build.VERSION.SDK_INT + "_" + Build.MANUFACTURER.replace("_", "-");
        if (Log.isLoggable(LOG_TAG, 3)) {
            Log.d(LOG_TAG, "device info : " + info);
        }
        return info;
    }

    private String generatePu(Context context) {
        StringBuffer pu = new StringBuffer();
        appendPu(pu, "sz", "1320_480");
        appendPu(pu, "cuid", this.mCUID);
        appendPu(pu, "cua", this.mUA);
        appendPu(pu, "cut", this.mDeviceInfo);
        appendPu(pu, "osname", this.mProductLine);
        appendPu(pu, "ctv", "1");
        if (Log.isLoggable(LOG_TAG, 3)) {
            Log.d(LOG_TAG, "pu=" + ((Object) pu));
        }
        return pu.toString();
    }

    private String generateUA(Context context) {
        String versionName;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int density = dm.densityDpi;
        try {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            versionName = "1.0.0.0";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(width);
        sb.append("_");
        sb.append(height);
        sb.append("_");
        sb.append("android");
        sb.append("_");
        sb.append(versionName);
        sb.append("_");
        sb.append(density);
        String ua = sb.toString();
        if (Log.isLoggable(LOG_TAG, 3)) {
            Log.d(LOG_TAG, "ua = " + ua);
        }
        return ua;
    }

    public String getBrowserUA() {
        return this.mBrowserUA;
    }

    public String getFrom() {
        if (!TextUtils.isEmpty(this.mFrom)) {
            return this.mFrom;
        }
        if (!TextUtils.isEmpty(this.mCFrom)) {
            return this.mCFrom;
        }
        return "959b";
    }

    public static ResourceParameters getInstance() {
        return sInstance;
    }

    public void init(Context context) {
        Context application = StubApp.getOrigApplicationContext(context.getApplicationContext());
        this.mCUID = Device.getDeviceID(application);
        this.mUA = generateUA(application);
        this.mOriginalPu = generatePu(application);
        refreshUserAgent();
        StringBuffer pu = new StringBuffer(this.mOriginalPu);
        appendPu(pu, "cfrom", this.mCFrom);
        this.mPu = pu.toString();
        CookieSyncManager.createInstance(application);
        this.mCookieManager = CookieManager.getInstance();
        this.mContext = new WeakReference<>(application);
    }

    private void refreshUserAgent() {
        if (!TextUtils.isEmpty(this.mOriginalBUA)) {
            this.mBrowserUA = this.mOriginalBUA + " ";
            this.mBrowserUA += Utility.urlEncode(this.mProductLine, "utf-8");
            this.mBrowserUA += FileSelector_Dialog.sRoot;
            this.mBrowserUA += reverseString(Utility.urlEncode(this.mUA, "utf-8"));
            this.mBrowserUA += FileSelector_Dialog.sRoot;
            this.mBrowserUA += reverseString(Utility.urlEncode(this.mDeviceInfo, "utf-8"));
            this.mBrowserUA += FileSelector_Dialog.sRoot;
            this.mBrowserUA += Utility.urlEncode(getFrom(), "utf-8");
            this.mBrowserUA += FileSelector_Dialog.sRoot;
            this.mBrowserUA += Utility.urlEncode(this.mCUID, "utf-8");
            this.mBrowserUA += FileSelector_Dialog.sRoot;
            this.mBrowserUA += "1";
        }
    }

    private String reverseString(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder(str);
        sb.reverse();
        return sb.toString();
    }

    public void setBrowserUA(String browserUA) {
        this.mOriginalBUA = browserUA;
        refreshUserAgent();
    }

    public void setCFrom(String cfrom) {
        this.mCFrom = cfrom;
        if (this.mOriginalPu != null) {
            StringBuffer pu = new StringBuffer(this.mOriginalPu);
            appendPu(pu, "cfrom", cfrom);
            this.mPu = pu.toString();
        }
    }

    public String getCookie() {
        if (this.mCookieManager != null) {
            return this.mCookieManager.getCookie(KEY_COOKIE_URL);
        }
        return null;
    }
}
