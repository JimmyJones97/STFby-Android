package com.xzy.forestSystem.smssdk.p004a;

import android.os.Handler;
import android.os.Message;

import com.xzy.forestSystem.smssdk.contact.ContactHelper;
import com.xzy.forestSystem.smssdk.net.Protocols;
import com.xzy.forestSystem.smssdk.utils.Constants;
import com.xzy.forestSystem.smssdk.utils.LogCatHelper;
import com.xzy.forestSystem.smssdk.utils.SMSLog;
import com.xzy.forestSystem.smssdk.utils.SPHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* renamed from: cn.smssdk.a.a */
public final class SocialHelper {

    /* renamed from: a */
    private static final String f18a = SocialHelper.class.getSimpleName();

    /* renamed from: b */
    private static SocialHelper f19b;

    /* renamed from: c */
    private Protocols f20c = Protocols.m395a();

    /* renamed from: d */
    private SPHelper f21d = SPHelper.getInstance();

    /* renamed from: e */
    private Synchronizer f22e = new Synchronizer(this);

    /* renamed from: f */
    private ContactHelper f23f = ContactHelper.m486a();

    /* renamed from: a */
    public static SocialHelper m616a() {
        if (f19b == null) {
            f19b = new SocialHelper();
        }
        return f19b;
    }

    private SocialHelper() {
    }

    /* renamed from: a */
    public void m612a(String str, String str2, String str3, String str4, String str5) throws Throwable {
        this.f20c.m392a(str, str2, str3, str4, str5);
    }

    /* renamed from: a */
    public void m614a(final Handler.Callback callback) {
        if (m607c()) {
            m606c(new Handler.Callback() { // from class: cn.smssdk.a.a.1
                @Override // android.os.Handler.Callback
                public boolean handleMessage(Message message) {
                    Message message2 = new Message();
                    try {
                        ArrayList<HashMap<String, Object>> a = SocialHelper.this.m611a(SocialHelper.this.f20c.m390a((String[]) message.obj));
                        try {
                            SocialHelper.this.f21d.setBufferedNewFriends(a);
                            SocialHelper.this.f21d.setRequestNewFriendsTime();
                        } catch (Throwable th) {
                            SMSLog.getInstance().m57w(th);
                        }
                        message2.what = 1;
                        message2.obj = Integer.valueOf(a.size());
                    } catch (Throwable th2) {
                        message2.what = 0;
                        message2.obj = th2;
                    }
                    callback.handleMessage(message2);
                    return false;
                }
            });
            return;
        }
        Message message = new Message();
        message.what = 1;
        try {
            message.obj = Integer.valueOf(this.f21d.getBufferedNewFriends().size());
        } catch (Throwable th) {
            SMSLog.getInstance().m57w(th);
            message.obj = 0;
        }
        callback.handleMessage(message);
    }

    /* renamed from: c */
    private boolean m607c() {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(System.currentTimeMillis());
        int i = instance.get(1);
        int i2 = instance.get(2);
        int i3 = instance.get(5);
        instance.setTimeInMillis(this.f21d.getLastRequestNewFriendsTime());
        int i4 = instance.get(1);
        int i5 = instance.get(2);
        int i6 = instance.get(5);
        if (i == i4 && i2 == i5 && i3 == i6) {
            return false;
        }
        return true;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r1v3, resolved type: java.util.ArrayList<java.util.HashMap<java.lang.String, java.lang.Object>> */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: protected */
    /* renamed from: a */
    public ArrayList<HashMap<String, Object>> m611a(ArrayList<HashMap<String, Object>> arrayList) throws Throwable {
        ArrayList<HashMap<String, Object>> arrayList2;
        ArrayList<HashMap<String, Object>> arrayList3;
        boolean z;
        try {
            arrayList2 = this.f21d.getBufferedFriends();
        } catch (Throwable th) {
            SMSLog.getInstance().m57w(th);
            arrayList2 = new ArrayList<>();
        }
        HashMap hashMap = new HashMap();
        Iterator<HashMap<String, Object>> it = arrayList.iterator();
        while (it.hasNext()) {
            HashMap<String, Object> next = it.next();
            Object obj = next.get("phone");
            if (obj != null) {
                Iterator<HashMap<String, Object>> it2 = arrayList2.iterator();
                while (true) {
                    if (it2.hasNext()) {
                        if (obj.equals(it2.next().get("phone"))) {
                            z = false;
                            break;
                        }
                    } else {
                        z = true;
                        break;
                    }
                }
                if (z) {
                    hashMap.put(obj, next);
                }
            }
        }
        try {
            arrayList3 = this.f21d.getBufferedNewFriends();
        } catch (Throwable th2) {
            SMSLog.getInstance().m57w(th2);
            arrayList3 = new ArrayList<>();
        }
        Iterator<HashMap<String, Object>> it3 = arrayList3.iterator();
        while (it3.hasNext()) {
            HashMap<String, Object> next2 = it3.next();
            Object obj2 = next2.get("phone");
            if (obj2 != null) {
                hashMap.put(obj2, next2);
            }
        }
        ArrayList<HashMap<String, Object>> arrayList4 = new ArrayList<>();
        for (Object object : hashMap.entrySet()) {
            Map.Entry entry = (Map.Entry) object;
            arrayList4.add((HashMap<String, Object>) entry.getValue());
        }
        return arrayList4;
    }

    /* renamed from: b */
    public void m609b(final Handler.Callback callback) {
        m606c(new Handler.Callback() { // from class: cn.smssdk.a.a.2
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                Message message2 = new Message();
                try {
                    ArrayList<HashMap<String, Object>> a = SocialHelper.this.f20c.m390a((String[]) message.obj);
                    ArrayList<HashMap<String, Object>> a2 = SocialHelper.this.m611a(a);
                    try {
                        SocialHelper.this.f21d.setBufferedFriends(a);
                        SocialHelper.this.f21d.setBufferedNewFriends(new ArrayList<>());
                    } catch (Throwable th) {
                        SMSLog.getInstance().m57w(th);
                    }
                    Iterator<HashMap<String, Object>> it = a2.iterator();
                    while (it.hasNext()) {
                        Object obj = it.next().get("phone");
                        if (obj != null) {
                            Iterator<HashMap<String, Object>> it2 = a.iterator();
                            while (true) {
                                if (!it2.hasNext()) {
                                    break;
                                }
                                HashMap<String, Object> next = it2.next();
                                if (obj.equals(next.get("phone"))) {
                                    next.put("isnew", true);
                                    break;
                                }
                            }
                        }
                    }
                    message2.what = 1;
                    message2.obj = a;
                } catch (Throwable th2) {
                    message2.what = 0;
                    message2.obj = th2;
                }
                callback.handleMessage(message2);
                return false;
            }
        });
    }

    /* renamed from: a */
    public void m615a(int i, Handler.Callback callback) {
        this.f22e.m603a(i, callback);
    }

    /* renamed from: c */
    private void m606c(final Handler.Callback callback) {
        String[] strArr;
        final Message message = new Message();
        try {
            strArr = this.f21d.getBufferedContactPhones();
        } catch (Throwable th) {
            SMSLog.getInstance().m57w(th);
            strArr = null;
        }
        if (strArr == null || strArr.length <= 0) {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f18a, "getPhones", "Buffered contact phones DO NOT exist in SP. Get from mobile.");
            this.f23f.m482a(new Runnable() { // from class: cn.smssdk.a.a.3
                @Override // java.lang.Runnable
                public void run() {
                    new Thread() { // from class: cn.smssdk.a.a.3.1
                        @Override // java.lang.Thread, java.lang.Runnable
                        public void run() {
                            String[] c = SocialHelper.this.f23f.m476c();
                            try {
                                SocialHelper.this.f21d.setBufferedContactPhones(c);
                            } catch (Throwable th2) {
                                SMSLog.getInstance().m57w(th2);
                            }
                            if (Constants.f191c.booleanValue()) {
                                SMSLog.getInstance().m70d(SMSLog.FORMAT, SocialHelper.f18a, "getPhones", "Phones observed from mobile: " + LogCatHelper.m375a(c));
                            }
                            message.obj = c;
                            callback.handleMessage(message);
                        }
                    }.start();
                }
            }, new Runnable() { // from class: cn.smssdk.a.a.4
                @Override // java.lang.Runnable
                public void run() {
                    new Thread() { // from class: cn.smssdk.a.a.4.1
                        @Override // java.lang.Thread, java.lang.Runnable
                        public void run() {
                            callback.handleMessage(message);
                        }
                    }.start();
                }
            });
            return;
        }
        SMSLog.getInstance().m70d(SMSLog.FORMAT, f18a, "getPhones", "Buffered contact phones exist in SP. Use them.");
        message.obj = strArr;
        callback.handleMessage(message);
    }
}
