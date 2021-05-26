package com.xzy.forestSystem.smssdk.net;

import android.text.TextUtils;

import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.tools.network.ByteArrayPart;
import  com.xzy.forestSystem.mob.tools.network.KVPair;
import  com.xzy.forestSystem.mob.tools.network.NetworkHelper;
import  com.xzy.forestSystem.mob.tools.utils.Data;
import com.xzy.forestSystem.smssdk.utils.Constants;
import com.xzy.forestSystem.smssdk.utils.LogCatHelper;
import com.xzy.forestSystem.smssdk.utils.SMSLog;

import org.gdal.osr.osrConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* renamed from: cn.smssdk.net.a */
public abstract class BaseApi {

    /* renamed from: j */
    private static final String f124j = BaseApi.class.getSimpleName();

    /* renamed from: k */
    private static NetworkHelper f125k;

    /* renamed from: l */
    private static NetworkHelper.NetworkTimeOut f126l;

    /* renamed from: a */
    protected int f127a;

    /* renamed from: b */
    protected String f128b;

    /* renamed from: c */
    protected String f129c;

    /* renamed from: d */
    protected int f130d;

    /* renamed from: e */
    protected boolean f131e;

    /* renamed from: f */
    protected boolean f132f;

    /* renamed from: g */
    protected boolean f133g = false;

    /* renamed from: h */
    protected ReentrantReadWriteLock f134h;

    /* renamed from: i */
    protected ArrayList<String> f135i;

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public abstract HashMap<String, Object> mo386a(String str, String str2, HashMap<String, Object> hashMap) throws Throwable;

    /* renamed from: b */
    public abstract boolean mo384b() throws Throwable;

    /* renamed from: a */
    public int m459a() {
        return this.f127a;
    }

    /* renamed from: a */
    public void m455a(ReentrantReadWriteLock reentrantReadWriteLock) {
        this.f134h = reentrantReadWriteLock;
    }

    /* JADX INFO: finally extract failed */
    /* renamed from: b */
    public String m451b(String str, String str2, HashMap<String, Object> hashMap) throws Throwable {
        HashMap<String, Object> hashMap2;
        String str3;
        if (mo384b()) {
            SMSLog.getInstance().m58w(SMSLog.FORMAT, f124j, "request", "[" + this.f128b + "]Request limited.");
            return "";
        }
        try {
            if (this.f134h != null) {
                this.f134h.readLock().lock();
            }
            HashMap<String, Object> a = mo386a(str, str2, hashMap);
            if (a == null) {
                hashMap2 = new HashMap<>();
            } else {
                hashMap2 = a;
            }
            if (!hashMap2.containsKey("duid")) {
                hashMap2.put("duid", str);
                if (this.f128b == null || !this.f128b.equals("getToken")) {
                    hashMap2.put("duid", "Api: " + this.f128b + " duid: " + str + " is added by workaround.");
                } else {
                    hashMap2.put("duidinfo_x17zcD", "Api: " + this.f128b + " duid: " + str + " added by workaround. " + Config.m424e() + " params is : " + LogCatHelper.m378a(this.f135i) + " cfgsrv: " + LogCatHelper.m377a(Config.f136a) + "cfgsp: " + LogCatHelper.m377a(Config.f137b));
                }
            } else if (TextUtils.isEmpty((String) hashMap2.get("duid"))) {
                if (!TextUtils.isEmpty(str)) {
                    hashMap2.put("duid", str);
                    hashMap2.put("duidinfo_x17zcD", "Api: " + this.f128b + " duid build to params is invalid and added by workaround.");
                } else {
                    hashMap2.put("duidinfo_x17zcD", "Api: " + this.f128b + " duid got from CommonsLib is invalid.");
                }
            }
            byte[] a2 = Crypto.m416a();
            byte[] a3 = m456a(hashMap2, this.f131e, a2);
            byte[] a4 = m452a(Crypto.m409a(a2), a3);
            ArrayList<KVPair<String>> a5 = ParamsBuilder.m403a().m399a(hashMap2, str2, a4.length, this.f131e);
            if (Constants.f190b.booleanValue()) {
                if (this.f128b.equals("uploadContacts")) {
                    String str4 = (String) hashMap2.get(osrConstants.SRS_PP_ZONE);
                    String str5 = (String) hashMap2.get("phone");
                    String str6 = "";
                    Iterator it = ((ArrayList) hashMap.get("contacts")).iterator();
                    while (it.hasNext()) {
                        str6 = str6 + LogCatHelper.m377a((HashMap) it.next());
                    }
                    SMSLog.getInstance().m70d(SMSLog.FORMAT, f124j, "request", "[" + this.f128b + "]uploadContacts. country: " + str4 + " phone: " + str5 + " contacts: " + str6);
                } else if (this.f128b.equals("getFriend")) {
                    String[] strArr = (String[]) hashMap.get("contactphones");
                    if (strArr == null || strArr.length <= 0) {
                        str3 = "";
                    } else {
                        str3 = LogCatHelper.m375a(strArr);
                    }
                    SMSLog.getInstance().m70d(SMSLog.FORMAT, f124j, "request", "[" + this.f128b + "]getFriend. phonelist: " + str3);
                }
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f124j, "request", "[" + this.f128b + "]url: " + this.f129c + " params: " + LogCatHelper.m377a(hashMap2) + " zip: " + this.f131e);
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f124j, "request", "[" + this.f128b + "]encoded params: " + LogCatHelper.m376a(a3));
            }
            String a6 = m457a(this.f129c, a4, a5, 0);
            if (this.f134h == null) {
                return a6;
            }
            this.f134h.readLock().unlock();
            return a6;
        } catch (Throwable th) {
            if (this.f134h != null) {
                this.f134h.readLock().unlock();
            }
            throw th;
        }
    }

    /* renamed from: a */
    private String m457a(String str, byte[] bArr, ArrayList<KVPair<String>> arrayList, int i) throws Throwable {
        String str2;
        String str3;
        if (i > 3) {
            throw new Throwable("{'detail':'Content-Length Error,Network is poor'}");
        }
        if (f125k == null || f126l == null) {
            f126l = new NetworkHelper.NetworkTimeOut();
            f126l.connectionTimeout = 180000;
            f126l.readTimout = 180000;
            f125k = new NetworkHelper();
        }
        ByteArrayPart byteArrayPart = new ByteArrayPart();
        byteArrayPart.append(bArr);
        HashMap hashMap = new HashMap();
        if (Constants.f190b.booleanValue()) {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f124j, "httpPost", "[" + this.f128b + "]header: " + LogCatHelper.m378a(arrayList));
        }
        f125k.rawPost(str, arrayList, byteArrayPart, new HttpResponseCallbackImp(hashMap), f126l);
        if (hashMap == null || hashMap.size() <= 0) {
            SMSLog.getInstance().m67e(SMSLog.FORMAT, f124j, "httpPost", "[" + this.f128b + "](map)Response is empty.");
            throw new Throwable("[map]Response is empty");
        }
        byte[] bArr2 = (byte[]) hashMap.get("bResp");
        if (bArr2 == null || bArr2.length <= 0) {
            SMSLog.getInstance().m67e(SMSLog.FORMAT, f124j, "httpPost", "[" + this.f128b + "](resp)Response is empty.");
            throw new Throwable("[resp]Response is empty");
        }
        Object obj = hashMap.get("Content-Length");
        if (obj != null) {
            str2 = (String) obj;
        } else {
            str2 = null;
        }
        if (TextUtils.isEmpty(str2) || Integer.valueOf(str2).intValue() == bArr2.length) {
            boolean z = false;
            Object obj2 = hashMap.get("zip");
            if (obj2 != null) {
                z = ((Boolean) obj2).booleanValue();
            }
            Map<String, byte[]> a = m454a(bArr2);
            String a2 = m453a(a.get("body"), z, a.get("aesKey"));
            if (Constants.f190b.booleanValue()) {
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f124j, "httpPost", "[" + this.f128b + "]Response: " + a2);
            }
            Object obj3 = hashMap.get("sign");
            if (obj3 != null) {
                str3 = (String) obj3;
            } else {
                str3 = null;
            }
            if (Data.MD5(a2 + MobSDK.getAppSecret()).equals(str3)) {
                return a2;
            }
            SMSLog.getInstance().m67e(SMSLog.FORMAT, f124j, "httpPost", "[" + this.f128b + "]Validate sign error");
            throw new Throwable("Validate sign error");
        }
        SMSLog.getInstance().m58w(SMSLog.FORMAT, f124j, "httpPost", "[" + this.f128b + "]Content-Length != bodyLength, retry!");
        return m457a(str, bArr, arrayList, i + 1);
    }

    /* renamed from: a */
    private static byte[] m456a(HashMap<String, Object> hashMap, boolean z, byte[] bArr) throws Throwable {
        return Crypto.m410a(hashMap, z, bArr);
    }

    /* renamed from: a */
    private static String m453a(byte[] bArr, boolean z, byte[] bArr2) throws Throwable {
        return Crypto.m408a(bArr, z, bArr2);
    }

    /* renamed from: a */
    private byte[] m452a(byte[] bArr, byte[] bArr2) {
        byte[] a = m458a(bArr.length);
        return m449b(m449b(m449b(a, bArr), m458a(bArr2.length)), bArr2);
    }

    /* renamed from: a */
    private byte[] m458a(int i) {
        byte[] bArr = new byte[4];
        for (int i2 = 0; i2 < 4; i2++) {
            bArr[i2] = (byte) (i >> (24 - (i2 * 8)));
        }
        return bArr;
    }

    /* renamed from: b */
    private byte[] m449b(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[(bArr.length + bArr2.length)];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    /* renamed from: a */
    private Map<String, byte[]> m454a(byte[] bArr) throws Throwable {
        HashMap hashMap = new HashMap();
        byte[] bArr2 = new byte[4];
        System.arraycopy(bArr, 0, bArr2, 0, 4);
        int b = m450b(bArr2);
        byte[] bArr3 = new byte[b];
        System.arraycopy(bArr, 4, bArr3, 0, b);
        byte[] b2 = Crypto.m405b(bArr3);
        byte[] bArr4 = new byte[4];
        System.arraycopy(bArr, b + 4, bArr4, 0, 4);
        int b3 = m450b(bArr4);
        byte[] bArr5 = new byte[b3];
        System.arraycopy(bArr, b + 4 + 4, bArr5, 0, b3);
        hashMap.put("aesKey", b2);
        hashMap.put("body", bArr5);
        return hashMap;
    }

    /* renamed from: b */
    private int m450b(byte[] bArr) {
        return (bArr[3] & 255) | ((bArr[2] & 255) << 8) | ((bArr[1] & 255) << 16) | ((bArr[0] & 255) << 24);
    }
}
