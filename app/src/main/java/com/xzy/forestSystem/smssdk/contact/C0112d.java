package com.xzy.forestSystem.smssdk.contact;

import android.os.Handler;
import android.os.Message;

import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.tools.MobHandlerThread;
import  com.xzy.forestSystem.mob.tools.utils.Data;
import  com.xzy.forestSystem.mob.tools.utils.ResHelper;
import com.xzy.forestSystem.smssdk.net.Protocols;
import com.xzy.forestSystem.smssdk.utils.Constants;
import com.xzy.forestSystem.smssdk.utils.SMSLog;
import com.xzy.forestSystem.smssdk.utils.SPHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/* compiled from: Synchronizer */
/* renamed from: cn.smssdk.contact.d */
public class C0112d implements Handler.Callback {

    /* renamed from: a */
    private Handler f117a;

    /* renamed from: b */
    private ContactHelper f118b;

    /* renamed from: c */
    private SPHelper f119c = SPHelper.getInstance();

    /* renamed from: d */
    private Protocols f120d = Protocols.m395a();

    /* renamed from: e */
    private String f121e = (ResHelper.getCacheRoot(MobSDK.getContext()) + ".slock");

    public C0112d(ContactHelper bVar) {
        MobHandlerThread mobHandlerThread = new MobHandlerThread();
        mobHandlerThread.start();
        this.f117a = new Handler(mobHandlerThread.getLooper(), this);
        this.f118b = bVar;
    }

    /* renamed from: a */
    public void m470a() {
        m465a("synchronize", "Start to sync contact.");
        if (!m464b()) {
            this.f117a.removeMessages(1);
            this.f117a.sendEmptyMessageDelayed(1, 180000);
        }
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        new Thread() { // from class: cn.smssdk.contact.d.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                String str;
                String str2 = null;
                try {
                    C0112d.this.m465a("handleMessage", "starting processing MSG_SYNCHRONIZE");
                    try {
                        String verifyCountry = C0112d.this.f119c.getVerifyCountry();
                        str = C0112d.this.f119c.getVerifyPhone();
                        str2 = verifyCountry;
                    } catch (Throwable th) {
                        SMSLog.getInstance().m57w(th);
                        str = null;
                    }
                    ArrayList<HashMap<String, Object>> a = C0112d.this.f118b.m481a(false);
                    String a2 = C0112d.this.m466a(a);
                    String bufferedContactsSignature = C0112d.this.f119c.getBufferedContactsSignature();
                    C0112d.this.m465a("handleMessage", "contacts got, start uploading");
                    if (a == null || a.isEmpty() || a2 == null || a2.equals(bufferedContactsSignature)) {
                        C0112d.this.m465a("handleMessage", "Current contact sign has NO change, no need to upload contacts.");
                    } else {
                        C0112d.this.m465a("handleMessage", "Current contact sign changed, upload contacts.");
                        C0112d.this.f120d.m391a(str2, str, a);
                    }
                    C0112d.this.f119c.setBufferedContactsSignature(a2);
                    C0112d.this.m465a("handleMessage", "upload done!");
                } catch (Throwable th2) {
                    SMSLog.getInstance().m57w(th2);
                } finally {
                    C0112d.this.m462c();
                }
            }
        }.start();
        return false;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: a */
    private String m466a(Object obj) throws Throwable {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.flush();
        objectOutputStream.close();
        return Data.CRC32(byteArrayOutputStream.toByteArray());
    }

    /* renamed from: b */
    private boolean m464b() {
        try {
            m465a("isSynchronizing", "Lock file path: " + this.f121e);
            File file = new File(this.f121e);
            if (file.exists()) {
                if (System.currentTimeMillis() - file.lastModified() >= 86400000) {
                    file.delete();
                    file.createNewFile();
                    m465a("isSynchronizing", "sync file is expired and deleted!");
                    return false;
                }
                m465a("isSynchronizing", "sync file exists, maybe other app is syncing, ignore our syncing!");
                return true;
            }
            file.createNewFile();
            m465a("isSynchronizing", "sync file doesn't exist, and is created!");
            return false;
        } catch (Exception e) {
            SMSLog.getInstance().m57w(e);
            return false;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: c */
    private void m462c() {
        try {
            File file = new File(this.f121e);
            if (file.exists()) {
                Thread.sleep(5000);
                file.delete();
                m465a("onSyncFinished", "sync finished, and sync file has been deleted!");
            }
        } catch (Exception e) {
            SMSLog.getInstance().m57w(e);
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: a */
    private void m465a(String str, String str2) {
        if (Constants.f193e.booleanValue()) {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, "Synchronizer", str, str2);
        }
    }
}
