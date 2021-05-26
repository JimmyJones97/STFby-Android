package com.xzy.forestSystem.smssdk;

import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.tools.MobHandlerThread;
import  com.xzy.forestSystem.mob.tools.utils.DeviceHelper;
import  com.xzy.forestSystem.mob.tools.utils.ResHelper;
import com.xzy.forestSystem.smssdk.contact.AlertPage;
import com.xzy.forestSystem.smssdk.contact.ContactHelper;
import com.xzy.forestSystem.smssdk.contact.OnContactChangeListener;
import com.xzy.forestSystem.smssdk.net.Protocols;
import com.xzy.forestSystem.smssdk.p004a.SocialHelper;
import com.xzy.forestSystem.smssdk.p005b.VerifyCodeReader;
import com.xzy.forestSystem.smssdk.utils.Constants;
import com.xzy.forestSystem.smssdk.utils.LogCatHelper;
import com.xzy.forestSystem.smssdk.utils.SMSLog;
import com.xzy.forestSystem.smssdk.utils.SPHelper;

import org.gdal.osr.osrConstants;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/* renamed from: cn.smssdk.b */
public class SMSSDKCore implements OnContactChangeListener {

    /* renamed from: a */
    private static final String f44a = SMSSDKCore.class.getSimpleName();

    /* renamed from: b */
    private static final Long f45b = 3000L;

    /* renamed from: c */
    private HashSet<EventHandler> f46c;

    /* renamed from: d */
    private Protocols f47d;

    /* renamed from: e */
    private ContactHelper f48e;

    /* renamed from: f */
    private SocialHelper f49f;

    /* renamed from: g */
    private VerifyCodeReader f50g;

    /* renamed from: h */
    private String f51h;

    /* renamed from: i */
    private HashMap<Character, ArrayList<String[]>> f52i;

    /* renamed from: j */
    private HashMap<String, String> f53j;

    /* renamed from: k */
    private ArrayList<HashMap<String, Object>> f54k;

    /* renamed from: l */
    private InitConfig f55l = InitConfig.m620a();

    ArrayList<HashMap<String, Object>> th = new ArrayList<>();
    public SMSSDKCore(SMSSDK.InitFlag initFlag) {
        if (initFlag != SMSSDK.InitFlag.DEFAULT) {
            switch (initFlag) {
                case WARNNING_READCONTACT:
                    this.f55l.m619a(true);
                    m580d();
                    break;
                case WARNNING_READCONTACT_DIALOG_MODE:
                    this.f55l.m619a(true);
                    AlertPage.m562a();
                    m580d();
                    break;
                case DISABLE_CONTACT:
                    this.f55l.m617b(true);
                    break;
            }
        }
        this.f46c = new HashSet<>();
        SMSLog.prepare();
        this.f47d = Protocols.m395a();
        this.f49f = SocialHelper.m616a();
        this.f48e = ContactHelper.m486a();
        this.f50g = VerifyCodeReader.m571a();
    }

    /* renamed from: a */
    public void m600a() {
        SMSLog.getInstance().m70d(SMSLog.FORMAT, f44a, "start", "First launch, start to upload contact.");
        MobHandlerThread mobHandlerThread = new MobHandlerThread();
        mobHandlerThread.start();
        new Handler(mobHandlerThread.getLooper()).postDelayed(new Runnable() { // from class: cn.smssdk.b.1
            @Override // java.lang.Runnable
            public void run() {
                if (!SMSSDKCore.this.f55l.m618b()) {
                    SMSLog.getInstance().m70d(SMSLog.FORMAT, SMSSDKCore.f44a, "start", "First launch, force rebuild contact.");
                    SMSSDKCore.this.f48e.m478b(new Runnable() { // from class: cn.smssdk.b.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            SMSSDKCore.this.f48e.m485a(SMSSDKCore.this);
                            SMSSDKCore.this.f48e.m480b();
                        }
                    }, null);
                    return;
                }
                SMSLog.getInstance().m58w(SMSLog.FORMAT, SMSSDKCore.f44a, "start", "Read contact permission NOT granted, do NOT sync contact.");
            }
        }, f45b.longValue());
    }

    /* renamed from: a */
    public void m596a(EventHandler eventHandler) {
        synchronized (this.f46c) {
            if (eventHandler != null) {
                if (!this.f46c.contains(eventHandler)) {
                    this.f46c.add(eventHandler);
                    eventHandler.onRegister();
                }
            }
        }
    }

    /* renamed from: b */
    public void m587b(EventHandler eventHandler) {
        synchronized (this.f46c) {
            if (eventHandler != null) {
                if (this.f46c.contains(eventHandler)) {
                    eventHandler.onUnregister();
                    this.f46c.remove(eventHandler);
                }
            }
        }
    }

    /* renamed from: b */
    public void m589b() {
        synchronized (this.f46c) {
            Iterator<EventHandler> it = this.f46c.iterator();
            while (it.hasNext()) {
                it.next().onUnregister();
            }
            this.f46c.clear();
        }
    }

    /* renamed from: a */
    public void m598a(final int i, final Object obj) {
        new Thread() { // from class: cn.smssdk.b.2
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                synchronized (SMSSDKCore.this.f46c) {
                    Iterator it = SMSSDKCore.this.f46c.iterator();
                    while (it.hasNext()) {
                        ((EventHandler) it.next()).beforeEvent(i, obj);
                    }
                }
                SMSSDKCore.this.m588b(i, obj);
            }
        }.start();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: b */
    private void m588b(int i, Object obj) {
        switch (i) {
            case 1:
                m575f();
                return;
            case 2:
                m585b(obj);
                return;
            case 3:
                m581c(obj);
                return;
            case 4:
                m592a(obj);
                return;
            case 5:
                m578d(obj);
                return;
            case 6:
                m573h();
                return;
            case 7:
                m574g();
                return;
            case 8:
                m576e(obj);
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: a */
    private void m599a(int i, int i2, Object obj) {
        int stringRes;
        if (Constants.f191c.booleanValue()) {
            if (i2 == -1) {
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f44a, "throwResult", "afterEvent. event: " + LogCatHelper.m379a(i) + ", result: " + LogCatHelper.m374b(i2));
            } else {
                SMSLog.getInstance().m67e(SMSLog.FORMAT, f44a, "throwResult", "afterEvent. event: " + LogCatHelper.m379a(i) + ", result: " + LogCatHelper.m374b(i2));
            }
        }
        if (obj != null && (obj instanceof Throwable)) {
            Throwable th = (Throwable) obj;
            String message = th.getMessage();
            if (!TextUtils.isEmpty(message)) {
                try {
                    JSONObject jSONObject = new JSONObject(message);
                    SMSLog.getInstance().m67e(SMSLog.FORMAT, f44a, "throwResult", "Throwable message: " + jSONObject);
                    int optInt = jSONObject.optInt("status");
                    if (TextUtils.isEmpty(jSONObject.optString("detail")) && (((optInt >= 400 && optInt <= 500) || optInt >= 600) && (stringRes = ResHelper.getStringRes(MobSDK.getContext(), "smssdk_error_desc_" + optInt)) > 0)) {
                        String string = MobSDK.getContext().getResources().getString(stringRes);
                        JSONObject jSONObject2 = new JSONObject();
                        jSONObject2.put("status", optInt);
                        jSONObject2.put("detail", string);
                        obj = new Throwable(jSONObject2.toString(), th);
                    }
                } catch (Throwable th2) {
                    SMSLog.getInstance().m57w(th2);
                }
            }
            SMSLog.getInstance().m57w(th);
        }
        SMSLog.getInstance().m70d(SMSLog.FORMAT, f44a, "throwResult", "Data put into afterEvent: " + obj);
        synchronized (this.f46c) {
            Iterator<EventHandler> it = this.f46c.iterator();
            while (it.hasNext()) {
                it.next().afterEvent(i, i2, obj);
            }
        }
    }

    /* renamed from: a */
    private void m592a(Object obj) {
        ArrayList<HashMap<String, Object>> arrayList;
        int i = -1;
        if (this.f55l.m618b()) {
            Message message = new Message();
            message.what = 1;
            message.obj = new ArrayList();
            m599a(4, -1, message.obj);
            return;
        }
//        try {
//            arrayList = this.f48e.m481a(((Boolean) obj).booleanValue());
//        } catch (ArrayList<HashMap<String, Object>> | Throwable th) {
//            arrayList = th;
//            i = 0;
//        }
//        m599a(4, i, arrayList);
    }

    /* renamed from: b */
    private void m585b(Object obj) {
        Object obj2;
        int i;
        String str;
        try {
            if (this.f53j == null || this.f53j.size() <= 0) {
                m572i();
            }
            Object[] objArr = (Object[]) obj;
            String str2 = (String) objArr[0];
            String str3 = (String) objArr[1];
            String str4 = (String) objArr[2];
            String str5 = (String) objArr[3];
            if (str2.startsWith("+")) {
                str = str2.substring(1);
            } else {
                str = str2;
            }
            if (!m590a(str3, str)) {
                SMSLog.getInstance().m70d("phone num error", new Object[0]);
            }
            OnSendMessageHandler onSendMessageHandler = (OnSendMessageHandler) objArr[4];
            if (onSendMessageHandler == null || !onSendMessageHandler.onSendMessage(str, str3)) {
                obj2 = Boolean.valueOf(this.f47d.m393a(str, str3, str4, str5));
                i = -1;
                m599a(2, i, obj2);
                return;
            }
            throw new UserInterruptException();
        } catch (Throwable th) {
            obj2 = th;
            i = 0;
        }
    }

    /* renamed from: c */
    private void m581c(Object obj) {
        Object th;
        int i = 0;
        try {
            if (this.f53j == null || this.f53j.size() <= 0) {
                m572i();
            }
            String[] strArr = (String[]) obj;
            String str = strArr[0];
            String str2 = strArr[1];
            String str3 = strArr[2];
            if (str.startsWith("+")) {
                str = str.substring(1);
            }
            if (!m590a(str2, str)) {
                throw new Throwable("phone num error");
            }
            th = this.f47d.m388b(str3, str, str2);
            i = -1;
            m599a(3, i, th);
        } catch (Throwable th2) {
            th = th2;
        }
    }

    /* renamed from: f */
    private void m575f() {
        int i = 0;
        try {
            th = m572i();
            i = -1;
        } catch (Throwable th) {
            th = th;
        }
        m599a(1, i, th);
    }

    /* renamed from: d */
    private void m578d(Object obj) {
        Throwable th;
        int i;
        try {
            String[] strArr = (String[]) obj;
            this.f49f.m612a(strArr[0], strArr[1], strArr[2], strArr[3], strArr[4]);
            i = -1;
            th = null;
        } catch (Throwable th2) {
            th = th2;
            i = 0;
        }
        m599a(5, i, th);
    }

    /* renamed from: g */
    private void m574g() {
        if (this.f55l.m618b()) {
            Message message = new Message();
            message.what = 1;
            message.obj = 0;
            m599a(7, -1, message.obj);
            return;
        }
        this.f49f.m614a(new Handler.Callback() { // from class: cn.smssdk.b.3
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message2) {
                int i;
                if (message2.what == 1) {
                    i = -1;
                } else {
                    i = 0;
                }
                SMSSDKCore.this.m599a(7, i, message2.obj);
                return false;
            }
        });
    }

    /* renamed from: h */
    private void m573h() {
        if (this.f55l.m618b()) {
            Message message = new Message();
            message.what = 1;
            message.obj = new ArrayList();
            m599a(6, -1, message.obj);
            return;
        }
        this.f49f.m609b(new Handler.Callback() { // from class: cn.smssdk.b.4
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message2) {
                int i;
                if (message2.what == 1) {
                    i = -1;
                } else {
                    i = 0;
                }
                SMSSDKCore.this.m599a(6, i, message2.obj);
                return false;
            }
        });
    }

    @Override // p003cn.smssdk.contact.OnContactChangeListener
    public void onContactChange(boolean z) {
        this.f48e.m478b(new Runnable() { // from class: cn.smssdk.b.5
            @Override // java.lang.Runnable
            public void run() {
                SMSSDKCore.this.f49f.m615a(0, new Handler.Callback() { // from class: cn.smssdk.b.5.1
                    @Override // android.os.Handler.Callback
                    public boolean handleMessage(Message message) {
                        if (message.arg1 <= 0) {
                            return false;
                        }
                        SMSSDKCore.this.m599a(7, -1, Integer.valueOf(message.arg1));
                        return false;
                    }
                });
            }
        }, null);
    }

    /* renamed from: c */
    public HashMap<Character, ArrayList<String[]>> m583c() {
        ArrayList arrayList;
        String appLanguage = DeviceHelper.getInstance(MobSDK.getContext()).getAppLanguage();
        SMSLog.getInstance().m70d("appLanguage:" + appLanguage, new Object[0]);
        if (appLanguage != null && !appLanguage.equals(this.f51h)) {
            this.f51h = appLanguage;
            this.f52i = null;
        }
        if (this.f52i != null && this.f52i.size() > 0) {
            return this.f52i;
        }
        LinkedHashMap linkedHashMap = null;
        for (char c = 'A'; c <= 'Z'; c = (char) (c + 1)) {
            int stringArrayRes = ResHelper.getStringArrayRes(MobSDK.getContext(), "smssdk_country_group_" + Character.toLowerCase(c));
            if (stringArrayRes > 0) {
                String[] stringArray = MobSDK.getContext().getResources().getStringArray(stringArrayRes);
                if (stringArray != null) {
                    arrayList = null;
                    for (String str : stringArray) {
                        String[] split = str.split(",");
                        if (arrayList == null) {
                            arrayList = new ArrayList();
                        }
                        arrayList.add(split);
                    }
                } else {
                    arrayList = null;
                }
                if (arrayList != null) {
                    if (linkedHashMap == null) {
                        linkedHashMap = new LinkedHashMap();
                    }
                    linkedHashMap.put(Character.valueOf(c), arrayList);
                }
            }
        }
        this.f52i = linkedHashMap;
        return this.f52i;
    }

    /* renamed from: a */
    public String[] m591a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (Map.Entry<Character, ArrayList<String[]>> entry : m583c().entrySet()) {
            ArrayList<String[]> value = entry.getValue();
            int size = value == null ? 0 : value.size();
            int i = 0;
            while (true) {
                if (i < size) {
                    String[] strArr = value.get(i);
                    if (strArr != null && strArr.length > 2 && str.equals(strArr[2])) {
                        return strArr;
                    }
                    i++;
                }
            }
        }
        return null;
    }

    /* renamed from: b */
    public String[] m584b(String str) {
        SMSLog.getInstance().m70d(SMSLog.FORMAT, f44a, "getCountryByMCC", "mcc: " + str);
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        for (Map.Entry<Character, ArrayList<String[]>> entry : m583c().entrySet()) {
            ArrayList<String[]> value = entry.getValue();
            int size = value == null ? 0 : value.size();
            int i = 0;
            while (true) {
                if (i < size) {
                    String[] strArr = value.get(i);
                    if (strArr.length < 4) {
                        SMSLog.getInstance().m70d("MCC not found in the country: " + strArr[0], new Object[0]);
                    } else {
                        String str2 = strArr[3];
                        if (str2.indexOf("|") >= 0) {
                            String[] split = str2.split("\\|");
                            for (String str3 : split) {
                                if (str3.startsWith(str)) {
                                    return strArr;
                                }
                            }
                        } else if (str2.startsWith(str)) {
                            return strArr;
                        }
                    }
                    i++;
                }
            }
        }
        return null;
    }

    /* renamed from: a */
    public void m597a(SmsMessage smsMessage, SMSSDK.VerifyCodeReadListener verifyCodeReadListener) {
        this.f50g.m569a(verifyCodeReadListener);
        this.f50g.m570a(smsMessage);
    }

    /* renamed from: e */
    private void m576e(Object obj) {
        Throwable th;
        int i;
        Object[] objArr = (Object[]) obj;
        String str = (String) objArr[0];
        String str2 = (String) objArr[1];
        String str3 = (String) objArr[2];
        if (str2.startsWith("+")) {
            str2 = str2.substring(1);
        }
        try {
            if (this.f53j == null || this.f53j.size() <= 0) {
                m572i();
            }
            if (!m590a(str, str2)) {
                SMSLog.getInstance().m70d("phone num error", new Object[0]);
            }
            this.f47d.m394a(str, str2, str3);
            i = -1;
            th = null;
        } catch (Throwable th2) {
            th = th2;
            i = 0;
        }
        m599a(8, i, th);
    }

    /* renamed from: d */
    public void m580d() {
        SPHelper.getInstance().setWarnWhenReadContact(true);
    }

    /* renamed from: a */
    private boolean m590a(String str, String str2) throws Throwable {
        int stringRes;
        int stringRes2;
        if (TextUtils.isEmpty(str)) {
            int stringRes3 = ResHelper.getStringRes(MobSDK.getContext(), "smssdk_error_desc_603");
            if (stringRes3 <= 0) {
                return false;
            }
            throw new Throwable("{\"status\":603,\"detail\":\"" + MobSDK.getContext().getResources().getString(stringRes3) + "\"}");
        } else if (this.f53j == null || this.f53j.size() <= 0) {
            if (str2 != "86") {
                int stringRes4 = ResHelper.getStringRes(MobSDK.getContext(), "smssdk_error_desc_604");
                if (stringRes4 > 0) {
                    throw new Throwable("{\"status\":604,\"detail\":\"" + MobSDK.getContext().getResources().getString(stringRes4) + "\"}");
                }
            } else if (str.length() != 11 && (stringRes = ResHelper.getStringRes(MobSDK.getContext(), "smssdk_error_desc_603")) > 0) {
                throw new Throwable("{\"status\":603,\"detail\":\"" + MobSDK.getContext().getResources().getString(stringRes) + "\"}");
            }
            return false;
        } else {
            String str3 = this.f53j.get(str2);
            if (TextUtils.isEmpty(str3) && (stringRes2 = ResHelper.getStringRes(MobSDK.getContext(), "smssdk_error_desc_604")) > 0) {
                throw new Throwable("{\"status\":604,\"detail\":\"" + MobSDK.getContext().getResources().getString(stringRes2) + "\"}");
            } else if (Pattern.compile(str3).matcher(str).matches()) {
                return true;
            } else {
                int stringRes5 = ResHelper.getStringRes(MobSDK.getContext(), "smssdk_error_desc_603");
                if (stringRes5 <= 0) {
                    return false;
                }
                throw new Throwable("{\"status\":603,\"detail\":\"" + MobSDK.getContext().getResources().getString(stringRes5) + "\"}");
            }
        }
    }

    /* renamed from: i */
    private ArrayList<HashMap<String, Object>> m572i() throws Throwable {
        if (this.f54k == null || this.f47d.m389b()) {
            this.f54k = this.f47d.m387c();
        }
        Iterator<HashMap<String, Object>> it = this.f54k.iterator();
        while (it.hasNext()) {
            HashMap<String, Object> next = it.next();
            String str = (String) next.get(osrConstants.SRS_PP_ZONE);
            String str2 = (String) next.get("rule");
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
                if (this.f53j == null) {
                    this.f53j = new HashMap<>();
                }
                this.f53j.put(str, str2);
            }
        }
        return this.f54k;
    }
}
