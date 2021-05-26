package com.xzy.forestSystem.smssdk.net;

import android.text.TextUtils;

import  com.xzy.forestSystem.mob.tools.utils.Hashon;
import com.xzy.forestSystem.baidu.speech.easr.stat.SynthesizeResultDb;
import com.xzy.forestSystem.smssdk.utils.Constants;
import com.xzy.forestSystem.smssdk.utils.LogCatHelper;
import com.xzy.forestSystem.smssdk.utils.SMSLog;
import com.xzy.forestSystem.smssdk.utils.SPHelper;

import org.gdal.osr.osrConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/* renamed from: cn.smssdk.net.f */
public class Protocols {

    /* renamed from: a */
    private static final String f176a = Protocols.class.getSimpleName();

    /* renamed from: b */
    private static Protocols f177b;

    /* renamed from: c */
    private Hashon f178c = new Hashon();

    /* renamed from: d */
    private SPHelper f179d = SPHelper.getInstance();

    /* renamed from: e */
    private Config f180e = Config.m448a();

    /* renamed from: a */
    public static Protocols m395a() {
        if (f177b == null) {
            f177b = new Protocols();
        }
        return f177b;
    }

    private Protocols() {
        ParamsBuilder.m397b();
    }

    /* renamed from: b */
    public boolean m389b() {
        return this.f180e.m434b();
    }

    /* renamed from: a */
    public void m394a(String str, String str2, String str3) throws Throwable {
        if (TextUtils.isEmpty(str2)) {
            throw new Throwable("{\"detail\":\"country code cant be empty\"}");
        } else if (TextUtils.isEmpty(str)) {
            throw new Throwable("{\"detail\":\"phone number cant be empty\"}");
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            HashMap hashMap2 = new HashMap();
            if (!TextUtils.isEmpty(str3)) {
                hashMap2.put("extKey", str3);
            }
            hashMap.put("phone", str);
            hashMap.put(osrConstants.SRS_PP_ZONE, str2);
            hashMap.put("attr", hashMap2);
            hashMap.put("tempCode", "Nul2");
            this.f180e.m445a(10, hashMap);
        }
    }

    /* renamed from: a */
    public void m391a(String str, String str2, ArrayList<HashMap<String, Object>> arrayList) throws Throwable {
        if (arrayList != null && arrayList.size() != 0) {
            if (Constants.f191c.booleanValue()) {
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f176a, "uploadContacts", "Upload contacts. country: " + str + ", phone: " + str2 + ", contacts: " + LogCatHelper.m378a(arrayList));
            }
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put(osrConstants.SRS_PP_ZONE, str);
            hashMap.put("phone", str2);
            hashMap.put("contacts", arrayList);
            this.f180e.m445a(5, hashMap);
        }
    }

    /* renamed from: a */
    public boolean m393a(String str, String str2, String str3, String str4) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            throw new Throwable("{\"detail\":\"country code cant be empty\"}");
        } else if (TextUtils.isEmpty(str2)) {
            throw new Throwable("{\"detail\":\"phone number cant be empty\"}");
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("phone", str2);
            hashMap.put(osrConstants.SRS_PP_ZONE, str);
            if (!TextUtils.isEmpty(str3)) {
                HashMap hashMap2 = new HashMap();
                hashMap.put("attr", hashMap2);
                hashMap2.put("extKey", str3);
            }
            if (!TextUtils.isEmpty(str4)) {
                hashMap.put("tempCode", str4);
            }
            HashMap<String, Object> a = this.f180e.m445a(9, hashMap);
            Integer num = (Integer) a.get("smart");
            this.f179d.setSMSID((String) a.get("smsId"));
            this.f179d.setVCodeHash((String) a.get("vCode"));
            if (num == null || num.intValue() != 1) {
                return false;
            }
            this.f179d.clearBuffer();
            try {
                this.f179d.setVerifyCountry(str);
                this.f179d.setVerifyPhone(str2);
            } catch (Throwable th) {
                SMSLog.getInstance().m57w(th);
            }
            return true;
        }
    }

    /* renamed from: b */
    public HashMap<String, Object> m388b(String str, String str2, String str3) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            throw new Throwable("{\"status\":\"466\"}");
        } else if (TextUtils.isEmpty(str2)) {
            throw new Throwable("{\"detail\":\"country code cant be empty\"}");
        } else if (TextUtils.isEmpty(str3)) {
            throw new Throwable("{\"detail\":\"phone number cant be empty\"}");
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("phone", str3);
            hashMap.put(SynthesizeResultDb.KEY_ERROR_CODE, str);
            hashMap.put(osrConstants.SRS_PP_ZONE, str2);
            this.f180e.m445a(11, hashMap);
            HashMap<String, Object> hashMap2 = new HashMap<>();
            hashMap2.put("country", str2);
            hashMap2.put("phone", str3);
            this.f179d.clearBuffer();
            try {
                this.f179d.setVerifyCountry(str2);
                this.f179d.setVerifyPhone(str3);
            } catch (Throwable th) {
                SMSLog.getInstance().m57w(th);
            }
            return hashMap2;
        }
    }

    /* renamed from: c */
    public ArrayList<HashMap<String, Object>> m387c() throws Throwable {
        HashMap<String, Object> a;
        long lastZoneAt = this.f179d.getLastZoneAt();
        String bufferedCountrylist = this.f179d.getBufferedCountrylist();
        if (lastZoneAt != this.f180e.m426d() || TextUtils.isEmpty(bufferedCountrylist) || m389b()) {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f176a, "getSupportedCountries", "Observe country list from server.");
            a = this.f180e.m445a(2, (HashMap<String, Object>) null);
            this.f179d.setBufferedCountrylist(this.f178c.fromHashMap(a));
            this.f179d.setLastZoneAt(this.f180e.m426d());
            this.f180e.m429c();
        } else {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f176a, "getSupportedCountries", "Use country list buffered in SP.");
            a = this.f178c.fromJson(bufferedCountrylist);
        }
        return (ArrayList) ((HashMap) a.get(SynthesizeResultDb.KEY_RESULT)).get("list");
    }

    /* renamed from: a */
    public void m392a(String str, String str2, String str3, String str4, String str5) throws Throwable {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("avatar", str3);
        hashMap.put("uid", str);
        hashMap.put("nickname", str2);
        hashMap.put("phone", str5);
        hashMap.put(osrConstants.SRS_PP_ZONE, str4);
        this.f180e.m445a(4, hashMap);
    }

    /* renamed from: a */
    public ArrayList<HashMap<String, Object>> m390a(String[] strArr) throws Throwable {
        ArrayList<HashMap<String, Object>> arrayList;
        ArrayList<HashMap<String, Object>> arrayList2;
        byte[] bArr = null;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("contactphones", strArr);
        HashMap hashMap2 = (HashMap) this.f180e.m445a(6, hashMap).get(SynthesizeResultDb.KEY_RESULT);
        if (hashMap2 != null) {
            ArrayList<HashMap<String, Object>> arrayList3 = (ArrayList) hashMap2.get("list");
            String str = (String) hashMap2.get("secretKey");
            if (!TextUtils.isEmpty(str)) {
                bArr = Crypto.m405b(Crypto.m404c(str));
                arrayList = arrayList3;
            } else {
                arrayList = arrayList3;
            }
        } else {
            arrayList = null;
        }
        if (arrayList == null) {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f176a, "getFriendsInApp", "Friend list is null");
            arrayList2 = new ArrayList<>();
        } else {
            arrayList2 = arrayList;
        }
        Iterator<HashMap<String, Object>> it = arrayList2.iterator();
        while (it.hasNext()) {
            HashMap<String, Object> next = it.next();
            Object obj = next.get("phone");
            if (obj != null) {
                next.put("phone", this.f180e.m431b(String.valueOf(obj).toLowerCase(Locale.ENGLISH), bArr));
            }
        }
        if (Constants.f191c.booleanValue()) {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f176a, "getFriendsInApp", "Friend list observed. list: " + LogCatHelper.m378a(arrayList2));
        }
        return arrayList2;
    }
}
