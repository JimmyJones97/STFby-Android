package com.xzy.forestSystem.smssdk.net;

import android.os.Build;
import android.text.TextUtils;

import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.tools.network.KVPair;
import  com.xzy.forestSystem.mob.tools.utils.Data;
import  com.xzy.forestSystem.mob.tools.utils.DeviceHelper;
import  com.xzy.forestSystem.mob.tools.utils.Hashon;
import  com.xzy.forestSystem.gogisapi.Common.Constant;
import com.xzy.forestSystem.smssdk.SMSSDK;
import com.xzy.forestSystem.smssdk.utils.Constants;
import com.xzy.forestSystem.smssdk.utils.LogCatHelper;
import com.xzy.forestSystem.smssdk.utils.SMSLog;
import com.xzy.forestSystem.smssdk.utils.Utils;

import org.gdal.osr.osrConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* renamed from: cn.smssdk.net.e */
public class ParamsBuilder {

    /* renamed from: a */
    private static final String f166a = ParamsBuilder.class.getSimpleName();

    /* renamed from: b */
    private static boolean f167b;

    /* renamed from: c */
    private static DeviceHelper f168c;

    /* renamed from: d */
    private static HashMap<String, Object> f169d;

    /* renamed from: e */
    private static Config f170e;

    /* renamed from: h */
    private static boolean f171h;

    /* renamed from: i */
    private static boolean f172i;

    /* renamed from: j */
    private static ParamsBuilder f173j;

    /* renamed from: f */
    private Hashon f174f = new Hashon();

    /* renamed from: g */
    private HashMap<String, String> f175g;

    /* renamed from: a */
    public static ParamsBuilder m403a() {
        if (f173j == null) {
            synchronized (ParamsBuilder.class) {
                f173j = new ParamsBuilder();
            }
        }
        return f173j;
    }

    private ParamsBuilder() {
    }

    /* renamed from: b */
    public static void m397b() {
        String str;
        String str2 = null;
        f168c = DeviceHelper.getInstance(MobSDK.getContext());
        f170e = Config.m448a();
        f169d = new HashMap<>();
        f169d.put("plat", Integer.valueOf(f168c.getPlatformCode()));
        f169d.put("sdkver", Integer.valueOf(Utils.m373a()));
        try {
            f171h = f168c.checkPermission("android.permission.READ_PHONE_STATE");
            f172i = f168c.checkPermission("android.permission.READ_SMS");
            if (f171h) {
                str = f168c.getSimSerialNumber();
            } else {
                str = null;
            }
            try {
                if (f171h && f172i) {
                    str2 = f168c.getLine1Number();
                }
            } catch (Throwable th) {
            }
        } catch (Throwable th2) {
            str = null;
        }
        String carrier = f168c.getCarrier();
        if (carrier != null && !carrier.equals("-1")) {
            f169d.put("operator", carrier);
        }
        if (str != null && !str.equals("-1")) {
            f169d.put("simserial", str);
        }
        if (str2 != null && !str2.equals("-1")) {
            f169d.put("myPhone", str2);
        }
        f167b = true;
    }

    /* renamed from: a */
    public HashMap<String, Object> m402a(int i, ArrayList<String> arrayList, String str, String str2, HashMap<String, Object> hashMap) throws Throwable {
        if (!f167b) {
            throw new Throwable("ParamsBuilder need prepare before use");
        }
        if (Constants.f191c.booleanValue()) {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f166a, "buildParams", "Build params. config: " + LogCatHelper.m378a(arrayList));
            String str3 = null;
            if (hashMap != null) {
                str3 = this.f174f.fromHashMap(hashMap);
            }
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f166a, "buildParams", "Build params. extParams: " + str3);
        }
        HashMap<String, Object> hashMap2 = new HashMap<>();
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (next.equals("appkey")) {
                hashMap2.put("appkey", MobSDK.getAppkey());
            } else if (next.equals("token")) {
                hashMap2.put("token", str2);
            } else if (next.equals("duid")) {
                hashMap2.put("duid", str);
            } else if (next.equals("contactphones")) {
                if (hashMap == null || !hashMap.containsKey("contactphones")) {
                    SMSLog.getInstance().m70d(SMSLog.FORMAT, f166a, "buildParams", "(extParams)No contact phones.");
                } else {
                    String[] strArr = (String[]) hashMap.get("contactphones");
                    if (strArr == null || strArr.length <= 0) {
                        SMSLog.getInstance().m70d(SMSLog.FORMAT, f166a, "buildParams", "(contactPhones)No contact phones.");
                    } else {
                        byte[] a = Crypto.m416a();
                        String byteToHex = Data.byteToHex(Crypto.m409a(a));
                        hashMap2.put("contactphones", m398a(strArr, a));
                        hashMap2.put("secretKey", byteToHex);
                    }
                }
            } else if (next.equals("contacts")) {
                ArrayList<HashMap<String, Object>> arrayList2 = (ArrayList) hashMap.get("contacts");
                byte[] a2 = Crypto.m416a();
                m400a(arrayList2, a2);
                String byteToHex2 = Data.byteToHex(Crypto.m409a(a2));
                hashMap2.put("contacts", m401a(arrayList2));
                hashMap2.put("secretKey", byteToHex2);
            } else if (!next.equals(osrConstants.SRS_PP_ZONE) || i != 5) {
                Object obj = f169d.get(next);
                if (obj == null) {
                    obj = hashMap.get(next);
                }
                hashMap2.put(next, obj);
            } else {
                String str4 = (String) hashMap.get(osrConstants.SRS_PP_ZONE);
                String[] countryByMCC = SMSSDK.getCountryByMCC(f168c.getMCC());
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f166a, "buildParams", "zone got from extParams: " + str4);
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f166a, "buildParams", "country params got from device: " + LogCatHelper.m375a(countryByMCC));
                if (countryByMCC != null) {
                    str4 = countryByMCC[1];
                }
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f166a, "buildParams", "final zone: " + str4);
                hashMap2.put(osrConstants.SRS_PP_ZONE, str4);
            }
        }
        return hashMap2;
    }

    /* renamed from: a */
    public ArrayList<KVPair<String>> m399a(HashMap<String, Object> hashMap, String str, int i, boolean z) throws Throwable {
        String appkey = MobSDK.getAppkey();
        String MD5 = Data.MD5(this.f174f.fromHashMap(hashMap) + MobSDK.getAppSecret());
        String c = m396c();
        if (i <= 0) {
            throw Utils.m372a(607);
        } else if (TextUtils.isEmpty(appkey)) {
            throw Utils.m372a(608);
        } else if (TextUtils.isEmpty(MobSDK.getAppSecret())) {
            throw Utils.m372a(611);
        } else if (TextUtils.isEmpty(MD5)) {
            throw Utils.m372a(609);
        } else if (TextUtils.isEmpty(c)) {
            throw Utils.m372a(610);
        } else {
            ArrayList<KVPair<String>> arrayList = new ArrayList<>();
            arrayList.add(new KVPair<>("Content-Length", String.valueOf(i)));
            arrayList.add(new KVPair<>(Constant.EXTRA_KEY, appkey));
            if (TextUtils.isEmpty(str)) {
                str = "";
            }
            arrayList.add(new KVPair<>("token", str));
            arrayList.add(new KVPair<>("sign", MD5));
            arrayList.add(new KVPair<>("User-Agent", c));
            if (z) {
                arrayList.add(new KVPair<>("zip", "1"));
            }
            if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
                arrayList.add(new KVPair<>("Connection", "close"));
            }
            return arrayList;
        }
    }

    /* renamed from: c */
    private String m396c() {
        StringBuilder sb = new StringBuilder();
        sb.append("SMSSDK/").append(Utils.m373a()).append(' ');
        sb.append("(Android; ").append(Data.urlEncode(f168c.getOSVersionName())).append('/').append(f168c.getOSVersionInt()).append(") ");
        sb.append(Data.urlEncode(f168c.getManufacturer())).append('/').append(Data.urlEncode(f168c.getModel())).append(' ');
        sb.append(Data.urlEncode(f168c.getAppName())).append('/').append(f168c.getPackageName()).append('/');
        sb.append(Data.urlEncode(f168c.getAppVersionName()));
        return sb.toString();
    }

    /* renamed from: a */
    private void m400a(ArrayList<HashMap<String, Object>> arrayList, byte[] bArr) throws Throwable {
        Object obj;
        if (arrayList != null && arrayList.size() > 0) {
            Iterator<HashMap<String, Object>> it = arrayList.iterator();
            while (it.hasNext()) {
                HashMap<String, Object> next = it.next();
                if (!(next == null || (obj = next.get("phones")) == null)) {
                    Iterator it2 = ((ArrayList) obj).iterator();
                    while (it2.hasNext()) {
                        HashMap hashMap = (HashMap) it2.next();
                        Object obj2 = hashMap.get("phone");
                        if (obj2 != null) {
                            hashMap.put("phone", f170e.m438a((String) obj2, bArr));
                        }
                    }
                }
            }
        }
    }

    /* renamed from: a */
    private String m401a(ArrayList<HashMap<String, Object>> arrayList) throws Throwable {
        HashMap hashMap = new HashMap();
        hashMap.put("list", arrayList);
        String fromHashMap = this.f174f.fromHashMap(hashMap);
        return fromHashMap.substring(8, fromHashMap.length() - 1);
    }

    /* renamed from: a */
    private String m398a(String[] strArr, byte[] bArr) {
        if (strArr == null || strArr.length == 0) {
            return null;
        }
        if (this.f175g == null) {
            this.f175g = new HashMap<>();
        } else {
            this.f175g.clear();
        }
        for (String str : strArr) {
            this.f175g.put(f170e.m438a(str, bArr), str);
        }
        if (this.f175g == null || this.f175g.size() <= 0) {
            return null;
        }
        return TextUtils.join(",", this.f175g.keySet());
    }
}
