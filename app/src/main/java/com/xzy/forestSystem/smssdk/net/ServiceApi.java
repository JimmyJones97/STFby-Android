package com.xzy.forestSystem.smssdk.net;

import android.text.TextUtils;

import com.xzy.forestSystem.smssdk.utils.SMSLog;
import com.xzy.forestSystem.smssdk.utils.SPHelper;

import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: cn.smssdk.net.g */
public class ServiceApi extends BaseApi {

    /* renamed from: j */
    private static final String f181j = ServiceApi.class.getSimpleName();

    /* renamed from: k */
    private int f182k;

    /* renamed from: l */
    private long f183l;

    /* renamed from: m */
    private SPHelper f184m = SPHelper.getInstance();

    /* renamed from: a */
    public void m385a(HashMap<String, Object> hashMap) throws Throwable {
        this.f128b = (String) hashMap.get("name");
        if (TextUtils.isEmpty(this.f128b)) {
            throw new Throwable("GET API NAME ERROR");
        }
        if (this.f128b.equals("getZoneList")) {
            this.f127a = 2;
        } else if (this.f128b.equals("getToken")) {
            this.f127a = 3;
        } else if (this.f128b.equals("submitUser")) {
            this.f127a = 4;
        } else if (this.f128b.equals("uploadContacts")) {
            this.f127a = 5;
        } else if (this.f128b.equals("getFriend")) {
            this.f127a = 6;
        } else if (this.f128b.equals("logCollect")) {
            this.f127a = 7;
        } else if (this.f128b.equals("logInstall")) {
            this.f127a = 8;
        } else if (this.f128b.equals("sendTextSMS")) {
            this.f127a = 9;
        } else if (this.f128b.equals("sendVoiceSMS")) {
            this.f127a = 10;
        } else if (this.f128b.equals("verifyCode")) {
            this.f127a = 11;
        } else if (this.f128b.equals("uploadCollectData")) {
            this.f127a = 12;
        } else {
            this.f127a = 0;
        }
        this.f129c = m383b(hashMap);
        this.f135i = (ArrayList) hashMap.get("params");
        if (this.f135i != null || this.f135i.size() > 0) {
            Integer num = (Integer) hashMap.get("zip");
            if (num == null || num.intValue() != 1) {
                this.f131e = false;
            } else {
                this.f131e = true;
            }
            Integer num2 = (Integer) hashMap.get("request");
            if (num2 == null || num2.intValue() != 1) {
                this.f132f = false;
            } else {
                this.f132f = true;
            }
            this.f182k = ((Integer) hashMap.get("frequency")).intValue();
            if (this.f182k != 0) {
                this.f133g = true;
            }
            m380e();
            return;
        }
        throw new Throwable("GET API PARAMS ERROR");
    }

    /* renamed from: c */
    public void m382c() {
        if (this.f133g) {
            this.f183l = System.currentTimeMillis();
            m381d();
        }
    }

    @Override // p003cn.smssdk.net.BaseApi
    /* renamed from: b */
    public boolean mo384b() throws Throwable {
        if (!this.f132f) {
            SMSLog.getInstance().m58w(SMSLog.FORMAT, f181j, "checkLimit", "[" + this.f128b + "]No access permission for this api, terminate this request.");
            throw new Throwable("{\"status\":606}");
        }
        if (this.f133g) {
            long currentTimeMillis = System.currentTimeMillis() - this.f183l;
            if (currentTimeMillis < ((long) this.f182k)) {
                SMSLog.getInstance().m58w(SMSLog.FORMAT, f181j, "checkLimit", "[" + this.f128b + "]Request too frequently, terminate this request. Interval: " + currentTimeMillis + ", frequency: " + this.f182k);
                throw new Throwable("{\"status\":600}");
            }
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f181j, "checkLimit", "[" + this.f128b + "]interval > frequency.");
        } else {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f181j, "checkLimit", "[" + this.f128b + "]Not limited for this api.");
        }
        SMSLog.getInstance().m70d(SMSLog.FORMAT, f181j, "checkLimit", "[" + this.f128b + "]Check OK, allow sending request.");
        return false;
    }

    /* renamed from: d */
    private void m381d() {
        this.f184m.setLastRequestTimeMillis(this.f128b, this.f183l);
    }

    /* renamed from: e */
    private void m380e() {
        if (this.f133g) {
            this.f183l = this.f184m.getLastRequestTimeMillis(this.f128b);
        }
    }

    /* renamed from: b */
    private String m383b(HashMap<String, Object> hashMap) {
        StringBuffer stringBuffer = new StringBuffer();
        String str = (String) hashMap.get("host");
        int intValue = ((Integer) hashMap.get("port")).intValue();
        String str2 = (String) hashMap.get("action");
        if (!TextUtils.isEmpty(str) && !str.contains("http://")) {
            stringBuffer.append("http://");
        }
        stringBuffer.append(str);
        stringBuffer.append(":");
        stringBuffer.append(intValue);
        stringBuffer.append(str2);
        return stringBuffer.toString();
    }

    /* access modifiers changed from: protected */
    @Override // p003cn.smssdk.net.BaseApi
    /* renamed from: a */
    public HashMap<String, Object> mo386a(String str, String str2, HashMap<String, Object> hashMap) throws Throwable {
        if (this.f135i != null && this.f135i.size() > 0) {
            return ParamsBuilder.m403a().m402a(this.f127a, this.f135i, str, str2, hashMap);
        }
        SMSLog.getInstance().m67e(SMSLog.FORMAT, f181j, "buildParams", "[" + this.f128b + "]Can not build request params since listParam is null.");
        throw new Throwable("Can not build request params since listParam is null.");
    }
}
