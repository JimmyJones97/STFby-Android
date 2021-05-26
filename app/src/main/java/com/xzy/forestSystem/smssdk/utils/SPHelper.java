package com.xzy.forestSystem.smssdk.utils;

import android.text.TextUtils;

import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.tools.utils.SharePrefrenceHelper;
import com.xzy.forestSystem.smssdk.net.Crypto;

import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: cn.smssdk.utils.SPHelper */
public class SPHelper {

    /* renamed from: a */
    private static SPHelper f186a;

    /* renamed from: b */
    private SharePrefrenceHelper f187b = new SharePrefrenceHelper(MobSDK.getContext());

    /* renamed from: c */
    private SharePrefrenceHelper f188c;

    public static SPHelper getInstance() {
        if (f186a == null) {
            f186a = new SPHelper();
        }
        return f186a;
    }

    private SPHelper() {
        this.f187b.open("SMSSDK", 2);
        this.f188c = new SharePrefrenceHelper(MobSDK.getContext());
        this.f188c.open("SMSSDK_VCODE", 1);
    }

    public String getConfig() throws Throwable {
        String b;
        String string = this.f187b.getString("config");
        if (!TextUtils.isEmpty(string) && (b = Crypto.m406b(string)) != null) {
            return b;
        }
        return null;
    }

    public void setConfig(String str) throws Throwable {
        if (!TextUtils.isEmpty(str)) {
            this.f187b.putString("config", Crypto.m415a(str));
        }
    }

    public long getLastRequestTimeMillis(String str) {
        return this.f187b.getLong(str);
    }

    public void setLastRequestTimeMillis(String str, long j) {
        this.f187b.putLong(str, Long.valueOf(j));
    }

    public boolean isAllowReadContact() {
        return this.f187b.getBoolean("read_contact");
    }

    public void setAllowReadContact() {
        this.f187b.putBoolean("read_contact", true);
    }

    public boolean isWarnWhenReadContact() {
        return this.f187b.getBoolean("read_contact_warn");
    }

    public void setWarnWhenReadContact(boolean z) {
        this.f187b.putBoolean("read_contact_warn", Boolean.valueOf(z));
    }

    public String getVerifyCountry() throws Throwable {
        String string = this.f187b.getString("verify_country");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return (String) Crypto.m413a(MobSDK.getAppkey(), string);
    }

    public void setVerifyCountry(String str) throws Throwable {
        if (!TextUtils.isEmpty(str)) {
            this.f187b.putString("verify_country", Crypto.m414a(MobSDK.getAppkey(), (Object) str));
        }
    }

    public String getVerifyPhone() throws Throwable {
        String string = this.f187b.getString("verify_phone");
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return (String) Crypto.m413a(MobSDK.getAppkey(), string);
    }

    public void setVerifyPhone(String str) throws Throwable {
        if (!TextUtils.isEmpty(str)) {
            this.f187b.putString("verify_phone", Crypto.m414a(MobSDK.getAppkey(), (Object) str));
        }
    }

    public void clearBuffer() {
        this.f187b.remove("bufferedNewFriends");
        this.f187b.remove("bufferedFriends");
        this.f187b.remove("lastRequestNewFriendsTime");
        this.f187b.remove("bufferedContactPhones");
    }

    public String getBufferedCountrylist() {
        return this.f187b.getString("bufferedCountryList");
    }

    public void setBufferedCountrylist(String str) {
        this.f187b.putString("bufferedCountryList", str);
    }

    public long getLastZoneAt() {
        return this.f187b.getLong("lastZoneAt");
    }

    public void setLastZoneAt(long j) {
        this.f187b.putLong("lastZoneAt", Long.valueOf(j));
    }

    public String getBufferedContactsSignature() {
        return this.f187b.getString("bufferedContactsSignature");
    }

    public void setBufferedContactsSignature(String str) {
        this.f187b.putString("bufferedContactsSignature", str);
    }

    public ArrayList<HashMap<String, Object>> getBufferedContacts() {
        Object obj = this.f187b.get("bufferedContacts");
        if (obj != null) {
            return (ArrayList) obj;
        }
        return new ArrayList<>();
    }

    public void setBufferedContacts(ArrayList<HashMap<String, Object>> arrayList) {
        this.f187b.put("bufferedContacts", arrayList);
    }

    public ArrayList<HashMap<String, Object>> getBufferedFriends() {
        ArrayList<HashMap<String, Object>> arrayList;
        synchronized ("bufferedFriends") {
            Object obj = this.f187b.get("bufferedFriends");
            if (obj != null) {
                arrayList = (ArrayList) obj;
            } else {
                arrayList = new ArrayList<>();
            }
        }
        return arrayList;
    }

    public void setBufferedFriends(ArrayList<HashMap<String, Object>> arrayList) {
        synchronized ("bufferedFriends") {
            this.f187b.put("bufferedFriends", arrayList);
        }
    }

    public ArrayList<HashMap<String, Object>> getBufferedNewFriends() {
        Object obj = this.f187b.get("bufferedNewFriends");
        if (obj != null) {
            return (ArrayList) obj;
        }
        return new ArrayList<>();
    }

    public void setBufferedNewFriends(ArrayList<HashMap<String, Object>> arrayList) {
        this.f187b.put("bufferedNewFriends", arrayList);
    }

    public long getLastRequestNewFriendsTime() {
        return this.f187b.getLong("lastRequestNewFriendsTime");
    }

    public void setRequestNewFriendsTime() {
        this.f187b.putLong("lastRequestNewFriendsTime", Long.valueOf(System.currentTimeMillis()));
    }

    public void setBufferedContactPhones(String[] strArr) {
        this.f187b.put("bufferedContactPhones", strArr);
    }

    public String[] getBufferedContactPhones() {
        Object obj = this.f187b.get("bufferedContactPhones");
        if (obj != null) {
            return (String[]) obj;
        }
        return new String[0];
    }

    public String getVCodeHash() {
        return this.f188c.getString("KEY_VCODE_HASH");
    }

    public void setVCodeHash(String str) {
        this.f188c.putString("KEY_VCODE_HASH", str);
    }

    public String getSMSID() {
        return this.f188c.getString("KEY_SMSID");
    }

    public void setSMSID(String str) {
        this.f188c.putString("KEY_SMSID", str);
    }

    public void setLog(String str) {
        synchronized ("KEY_LOG") {
            String log = getLog();
            if (!TextUtils.isEmpty(log)) {
                str = log + "\r\n" + str;
            }
            this.f188c.putString("KEY_LOG", str);
        }
    }

    public String getLog() {
        return this.f188c.getString("KEY_LOG");
    }

    public void clearLog() {
        synchronized ("KEY_LOG") {
            this.f188c.remove("KEY_LOG");
        }
    }

    public String getToken() {
        return this.f187b.getString("token");
    }

    public void setToken(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.f187b.putString("token", str);
        }
    }
}
