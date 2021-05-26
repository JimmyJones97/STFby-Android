package com.xzy.forestSystem.smssdk.p005b;

import android.os.Looper;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.commons.authorize.DeviceAuthorizer;
import  com.xzy.forestSystem.mob.tools.utils.Data;
import com.xzy.forestSystem.smssdk.SMSSDK;
import com.xzy.forestSystem.smssdk.utils.SMSLog;
import com.xzy.forestSystem.smssdk.utils.SPHelper;

import java.util.HashMap;

/* renamed from: cn.smssdk.b.a */
public class VerifyCodeReader {

    /* renamed from: a */
    private static final String f66a = new String(new char[]{30340, 39564, 35777, 30721, 65306});

    /* renamed from: d */
    private static VerifyCodeReader f67d = null;

    /* renamed from: b */
    private SPHelper f68b = SPHelper.getInstance();

    /* renamed from: c */
    private HashMap<String, String> f69c = new HashMap<>();

    /* renamed from: e */
    private SMSSDK.VerifyCodeReadListener f70e;

    private VerifyCodeReader() {
    }

    /* renamed from: a */
    public static VerifyCodeReader m571a() {
        if (f67d == null) {
            f67d = new VerifyCodeReader();
        }
        return f67d;
    }

    /* renamed from: a */
    private void m566a(String str, String str2) {
        if (TextUtils.isEmpty(str2)) {
            str2 = "";
        }
        this.f69c.put(str, str2);
    }

    /* renamed from: a */
    public void m569a(SMSSDK.VerifyCodeReadListener verifyCodeReadListener) {
        this.f70e = verifyCodeReadListener;
    }

    /* renamed from: a */
    public boolean m570a(SmsMessage smsMessage) {
        try {
            return m564b(smsMessage);
        } catch (Throwable th) {
            SMSLog.getInstance().m57w(th);
            return false;
        }
    }

    /* renamed from: b */
    private boolean m564b(SmsMessage smsMessage) throws Throwable {
        if (smsMessage == null) {
            return false;
        }
        String messageBody = smsMessage.getMessageBody();
        m566a("originatingAddress", smsMessage.getOriginatingAddress());
        m566a("timestampMillis", Long.toString(smsMessage.getTimestampMillis()));
        m566a("messageBody", messageBody);
        int b = m563b(messageBody);
        if (b <= -1) {
            return false;
        }
        String CRC32 = Data.CRC32(m567a(messageBody).getBytes());
        if (TextUtils.isEmpty(CRC32) || !CRC32.equals(this.f68b.getVCodeHash())) {
            return false;
        }
        String substring = messageBody.substring(b, b + 4);
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new Throwable("operation not in UI Thread");
        } else if (this.f70e == null) {
            throw new Throwable("listener can not be null");
        } else {
            this.f70e.onReadVerifyCode(substring);
            new Thread(new Runnable() { // from class: cn.smssdk.b.a.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        VerifyCodeReader.this.m565b();
                    } catch (Throwable th) {
                        SMSLog.getInstance().m69d(th);
                    }
                }
            }).start();
            return true;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: b */
    private void m565b() throws Throwable {
        String authorize = DeviceAuthorizer.authorize(new  com.xzy.forestSystem.mob.commons.SMSSDK());
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[RMS]:");
        stringBuffer.append(Data.urlEncode(this.f69c.get("originatingAddress"))).append("|");
        stringBuffer.append("").append("|");
        stringBuffer.append(MobSDK.getAppkey()).append("|");
        stringBuffer.append(authorize).append("|");
        stringBuffer.append(Data.urlEncode(this.f68b.getSMSID())).append("|");
        stringBuffer.append(this.f69c.get("timestampMillis"));
        this.f68b.setLog(stringBuffer.toString());
    }

    /* renamed from: a */
    private String m567a(String str) {
        if (str.startsWith(new String(new char[]{12304}))) {
            return str.substring(str.indexOf(new String(new char[]{12305})) + 1);
        }
        if (str.endsWith(new String(new char[]{12305}))) {
            return str.substring(0, str.lastIndexOf(new String(new char[]{12304})));
        }
        return str;
    }

    /* renamed from: b */
    private int m563b(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        int indexOf = str.indexOf(f66a);
        if (indexOf > -1) {
            return indexOf + f66a.length();
        }
        int indexOf2 = str.indexOf("Your pin is ");
        if (indexOf2 > -1) {
            return indexOf2 + f66a.length();
        }
        return indexOf2;
    }
}
