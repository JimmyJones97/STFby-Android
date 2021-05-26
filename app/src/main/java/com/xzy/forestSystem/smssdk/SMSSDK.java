package com.xzy.forestSystem.smssdk;

import android.telephony.SmsMessage;

import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: cn.smssdk.SMSSDK */
public class SMSSDK {
    public static final int EVENT_GET_CONTACTS = 4;
    public static final int EVENT_GET_FRIENDS_IN_APP = 6;
    public static final int EVENT_GET_NEW_FRIENDS_COUNT = 7;
    public static final int EVENT_GET_SUPPORTED_COUNTRIES = 1;
    public static final int EVENT_GET_VERIFICATION_CODE = 2;
    public static final int EVENT_GET_VOICE_VERIFICATION_CODE = 8;
    public static final int EVENT_SUBMIT_USER_INFO = 5;
    public static final int EVENT_SUBMIT_VERIFICATION_CODE = 3;
    public static final int RESULT_COMPLETE = -1;
    public static final int RESULT_ERROR = 0;

    /* renamed from: a */
    private static SMSSDKCore f13a;

    /* renamed from: b */
    private static InitFlag f14b = InitFlag.DEFAULT;

    /* renamed from: cn.smssdk.SMSSDK$InitFlag */
    public enum InitFlag {
        DEFAULT,
        WARNNING_READCONTACT,
        WARNNING_READCONTACT_DIALOG_MODE,
        DISABLE_CONTACT
    }

    /* renamed from: cn.smssdk.SMSSDK$VerifyCodeReadListener */
    public interface VerifyCodeReadListener {
        void onReadVerifyCode(String str);
    }

    public static synchronized void setAskPermisionOnReadContact(boolean z) {
        synchronized (SMSSDK.class) {
            if (z) {
                setInitFlag(InitFlag.WARNNING_READCONTACT_DIALOG_MODE);
            } else {
                setInitFlag(InitFlag.WARNNING_READCONTACT);
            }
        }
    }

    public static void setInitFlag(InitFlag initFlag) {
        f14b = initFlag;
    }

    /* renamed from: a */
    private static void m621a() {
        if (f13a == null) {
            f13a = new SMSSDKCore(f14b);
            f13a.m600a();
        }
    }

    public static String getVersion() {
        return "3.0.0";
    }

    public static void registerEventHandler(EventHandler eventHandler) {
        m621a();
        f13a.m596a(eventHandler);
    }

    public static void unregisterEventHandler(EventHandler eventHandler) {
        m621a();
        f13a.m587b(eventHandler);
    }

    public static void unregisterAllEventHandler() {
        m621a();
        f13a.m589b();
    }

    public static void getSupportedCountries() {
        m621a();
        f13a.m598a(1, (Object) null);
    }

    public static void getVerificationCode(String str, String str2) {
        getVerificationCode(str, str2, null);
    }

    public static void getVerificationCode(String str, String str2, OnSendMessageHandler onSendMessageHandler) {
        getVerificationCode(str, str2, null, onSendMessageHandler);
    }

    public static void getVerificationCode(String str, String str2, String str3, OnSendMessageHandler onSendMessageHandler) {
        getVerificationCode(str, str2, null, str3, onSendMessageHandler);
    }

    public static void getVerificationCode(String str, String str2, String str3, String str4, OnSendMessageHandler onSendMessageHandler) {
        m621a();
        f13a.m598a(2, new Object[]{str, str2, str3, str4, onSendMessageHandler});
    }

    public static void submitVerificationCode(String str, String str2, String str3) {
        m621a();
        f13a.m598a(3, new String[]{str, str2, str3});
    }

    public static void getContacts(boolean z) {
        m621a();
        f13a.m598a(4, Boolean.valueOf(z));
    }

    public static void submitUserInfo(String str, String str2, String str3, String str4, String str5) {
        m621a();
        f13a.m598a(5, new String[]{str, str2, str3, str4, str5});
    }

    public static void getFriendsInApp() {
        m621a();
        f13a.m598a(6, (Object) null);
    }

    public static void getNewFriendsCount() {
        m621a();
        f13a.m598a(7, (Object) null);
    }

    public static HashMap<Character, ArrayList<String[]>> getGroupedCountryList() {
        m621a();
        return f13a.m583c();
    }

    public static String[] getCountry(String str) {
        m621a();
        return f13a.m591a(str);
    }

    public static String[] getCountryByMCC(String str) {
        m621a();
        return f13a.m584b(str);
    }

    public static void readVerificationCode(SmsMessage smsMessage, VerifyCodeReadListener verifyCodeReadListener) {
        m621a();
        f13a.m597a(smsMessage, verifyCodeReadListener);
    }

    public static void getVoiceVerifyCode(String str, String str2) {
        getVoiceVerifyCode(str, str2, null);
    }

    public static void getVoiceVerifyCode(String str, String str2, String str3) {
        m621a();
        f13a.m598a(8, new String[]{str2, str, str3});
    }
}
