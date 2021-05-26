package  com.xzy.forestSystem.gogisapi.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetWorkUtils {
    public static boolean isNetworkConnected(Context context) {
        NetworkInfo mNetworkInfo;
        if (context == null || (mNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) == null) {
            return false;
        }
        return mNetworkInfo.isAvailable();
    }

    public static boolean isWifiConnected(Context context) {
        NetworkInfo mWiFiNetworkInfo;
        if (context == null || (mWiFiNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1)) == null) {
            return false;
        }
        return mWiFiNetworkInfo.isAvailable();
    }

    public static boolean isMobileConnected(Context context) {
        NetworkInfo mMobileNetworkInfo;
        if (context == null || (mMobileNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(0)) == null) {
            return false;
        }
        return mMobileNetworkInfo.isAvailable();
    }

    public static int getConnectedType(Context context) {
        NetworkInfo mNetworkInfo;
        if (context == null || (mNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo()) == null || !mNetworkInfo.isAvailable()) {
            return -1;
        }
        return mNetworkInfo.getType();
    }

    public static int getAPNType(Context context) {
        int netType = 0;
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo == null) {
            return 0;
        }
        int nType = networkInfo.getType();
        if (nType == 1) {
            netType = 1;
        } else if (nType == 0) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager mTelephony = (TelephonyManager) context.getSystemService("phone");
            if (nSubType != 3 || mTelephony.isNetworkRoaming()) {
                netType = 3;
            } else {
                netType = 2;
            }
        }
        return netType;
    }
}
