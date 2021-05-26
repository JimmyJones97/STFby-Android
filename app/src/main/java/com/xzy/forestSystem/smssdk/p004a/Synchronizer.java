package com.xzy.forestSystem.smssdk.p004a;

import android.os.Handler;
import android.os.Message;

import  com.xzy.forestSystem.mob.tools.MobHandlerThread;
import com.xzy.forestSystem.smssdk.contact.ContactHelper;
import com.xzy.forestSystem.smssdk.net.Protocols;
import com.xzy.forestSystem.smssdk.utils.SMSLog;
import com.xzy.forestSystem.smssdk.utils.SPHelper;

import java.util.ArrayList;
import java.util.HashMap;

/* renamed from: cn.smssdk.a.b */
public class Synchronizer implements Handler.Callback {

    /* renamed from: a */
    private Handler f36a;

    /* renamed from: b */
    private ContactHelper f37b = ContactHelper.m486a();

    /* renamed from: c */
    private SPHelper f38c = SPHelper.getInstance();

    /* renamed from: d */
    private Protocols f39d = Protocols.m395a();

    /* renamed from: e */
    private SocialHelper f40e;

    /* renamed from: f */
    private int f41f;

    /* renamed from: g */
    private Handler.Callback f42g;

    public Synchronizer(SocialHelper aVar) {
        MobHandlerThread mobHandlerThread = new MobHandlerThread();
        mobHandlerThread.start();
        this.f36a = new Handler(mobHandlerThread.getLooper(), this);
        this.f40e = aVar;
    }

    /* renamed from: a */
    public void m603a(int i, Handler.Callback callback) {
        this.f36a.removeMessages(1);
        this.f41f = i;
        this.f42g = callback;
        this.f36a.sendEmptyMessageDelayed(1, 5000);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        new Thread() { // from class: cn.smssdk.a.b.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                try {
                    int a = Synchronizer.this.m604a();
                    if (Synchronizer.this.f42g != null) {
                        Message message2 = new Message();
                        message2.what = Synchronizer.this.f41f;
                        message2.arg1 = a;
                        Synchronizer.this.f42g.handleMessage(message2);
                    }
                } catch (Throwable th) {
                    SMSLog.getInstance().m57w(th);
                }
            }
        }.start();
        return false;
    }

    /* renamed from: a */
    public int m604a() throws Throwable {
        String[] strArr;
        String[] c = this.f37b.m476c();
        try {
            strArr = this.f38c.getBufferedContactPhones();
            this.f38c.setBufferedContactPhones(c);
        } catch (Throwable th) {
            SMSLog.getInstance().m57w(th);
            strArr = new String[0];
        }
        ArrayList arrayList = new ArrayList();
        for (String str : c) {
            if (str != null) {
                boolean z = true;
                int length = strArr.length;
                int i = 0;
                while (true) {
                    if (i >= length) {
                        break;
                    } else if (str.equals(strArr[i])) {
                        z = false;
                        break;
                    } else {
                        i++;
                    }
                }
                if (z) {
                    arrayList.add(str);
                }
            }
        }
        if (arrayList.size() <= 0) {
            return 0;
        }
        String[] strArr2 = new String[arrayList.size()];
        for (int i2 = 0; i2 < strArr2.length; i2++) {
            strArr2[i2] = (String) arrayList.get(i2);
        }
        ArrayList<HashMap<String, Object>> a = this.f40e.m611a(this.f39d.m390a(strArr2));
        try {
            this.f38c.setBufferedNewFriends(a);
            this.f38c.setRequestNewFriendsTime();
        } catch (Throwable th2) {
            SMSLog.getInstance().m57w(th2);
        }
        return a.size();
    }
}
