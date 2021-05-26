package com.xzy.forestSystem.smssdk.contact;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.widget.Toast;

import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.tools.FakeActivity;
import  com.xzy.forestSystem.mob.tools.utils.DeviceHelper;
import  com.xzy.forestSystem.mob.tools.utils.UIHandler;
import  com.xzy.forestSystem.gogisapi.Common.Constant;
import com.xzy.forestSystem.baidu.speech.easr.stat.SynthesizeResultDb;
import com.xzy.forestSystem.smssdk.InitConfig;
import com.xzy.forestSystem.smssdk.contact.p006a.Contact;
import com.xzy.forestSystem.smssdk.contact.p006a.Email;
import com.xzy.forestSystem.smssdk.contact.p006a.Event;
import com.xzy.forestSystem.smssdk.contact.p006a.Group;
import com.xzy.forestSystem.smssdk.contact.p006a.Im;
import com.xzy.forestSystem.smssdk.contact.p006a.Name;
import com.xzy.forestSystem.smssdk.contact.p006a.Nickname;
import com.xzy.forestSystem.smssdk.contact.p006a.Note;
import com.xzy.forestSystem.smssdk.contact.p006a.Organization;
import com.xzy.forestSystem.smssdk.contact.p006a.Phone;
import com.xzy.forestSystem.smssdk.contact.p006a.Photo;
import com.xzy.forestSystem.smssdk.contact.p006a.Postal;
import com.xzy.forestSystem.smssdk.contact.p006a.Relation;
import com.xzy.forestSystem.smssdk.contact.p006a.Website;
import com.xzy.forestSystem.smssdk.utils.SMSLog;
import com.xzy.forestSystem.smssdk.utils.SPHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/* renamed from: cn.smssdk.contact.b */
public class ContactHelper {

    /* renamed from: a */
    private static final String f99a = ContactHelper.class.getSimpleName();

    /* renamed from: b */
    private static ContactHelper f100b;

    /* renamed from: c */
    private static ContentObserver f101c;

    /* renamed from: d */
    private ContentResolver f102d = MobSDK.getContext().getContentResolver();

    /* renamed from: e */
    private Querier f103e = new Querier(this.f102d);

    /* renamed from: f */
    private OnContactChangeListener f104f;

    /* renamed from: g */
    private C0112d f105g;

    /* renamed from: h */
    private String f106h;

    /* renamed from: a */
    public static ContactHelper m486a() {
        if (f100b == null) {
            f100b = new ContactHelper();
        }
        return f100b;
    }

    private ContactHelper() {
        if (!InitConfig.m620a().m618b()) {
            m474e();
        }
        this.f105g = new C0112d(this);
        this.f106h = new File(MobSDK.getContext().getFilesDir(), ".cb").getAbsolutePath();
    }

    /* renamed from: e */
    private void m474e() {
        if (f101c == null) {
            f101c = new ContentObserver(new Handler()) { // from class: cn.smssdk.contact.b.1
                @Override // android.database.ContentObserver
                public void onChange(boolean z) {
                    SMSLog.getInstance().m70d(SMSLog.FORMAT, ContactHelper.f99a, "onChange(selfChange)", "Observed device contact changing, start to sync contact. selfChange: " + z);
                    onChange(z, null);
                }

                @Override // android.database.ContentObserver
                public void onChange(boolean z, Uri uri) {
                    SMSLog.getInstance().m70d(SMSLog.FORMAT, ContactHelper.f99a, "onChange(selfChange, uri)", "Observed device contact changing, start to sync contact. selfChange: " + z + ", uri: " + uri);
                    ContactHelper.this.f105g.m470a();
                    if (ContactHelper.this.f104f != null) {
                        ContactHelper.this.f104f.onContactChange(z);
                    }
                }
            };
        }
        try {
            this.f102d.unregisterContentObserver(f101c);
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "registerContentObserver", "register ContentObserver");
            this.f102d.registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, f101c);
        } catch (Throwable th) {
            SMSLog.getInstance().m57w(th);
        }
    }

    /* renamed from: b */
    public void m480b() {
        SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "synchronize", "Check permission before upload contact.");
        m482a(new Runnable() { // from class: cn.smssdk.contact.b.2
            @Override // java.lang.Runnable
            public void run() {
                ContactHelper.this.f105g.m470a();
            }
        }, (Runnable) null);
    }

    /* renamed from: a */
    public void m482a(Runnable runnable, Runnable runnable2) {
        try {
            if (!DeviceHelper.getInstance(MobSDK.getContext()).checkPermission("android.permission.READ_CONTACTS")) {
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "checkPermission", "No permission to read contacts.");
                m472g();
                if (runnable2 != null) {
                    runnable2.run();
                    return;
                }
                return;
            }
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "checkPermission", "Has permission to read contacts.");
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "checkPermission", "isWarnWhenReadContact: " + SPHelper.getInstance().isWarnWhenReadContact());
            if (SPHelper.getInstance().isWarnWhenReadContact()) {
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "checkPermission", "isAllowReadContact: " + SPHelper.getInstance().isAllowReadContact());
                if (!SPHelper.getInstance().isAllowReadContact()) {
                    SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "checkPermission", "AlertPage.isShow(): " + AlertPage.m559b());
                    if (AlertPage.m559b()) {
                        AlertPage.m560a(runnable, runnable2);
                        return;
                    }
                    AlertPage aVar = new AlertPage();
                    AlertPage.m560a(runnable, runnable2);
                    aVar.showForResult(MobSDK.getContext(), null, new FakeActivity() { // from class: cn.smssdk.contact.b.3
                        @Override //  com.xzy.forestSystem.mob.tools.FakeActivity
                        public void onResult(HashMap<String, Object> hashMap) {
                            if ("true".equals(String.valueOf(hashMap.get("res")))) {
                                SMSLog.getInstance().m70d(SMSLog.FORMAT, ContactHelper.f99a, "checkPermission", "AlertPage: TRUE clicked");
                                SPHelper.getInstance().setAllowReadContact();
                                Iterator it = ((ArrayList) hashMap.get("okActions")).iterator();
                                while (it.hasNext()) {
                                    Runnable runnable3 = (Runnable) it.next();
                                    if (runnable3 != null) {
                                        runnable3.run();
                                    }
                                }
                                return;
                            }
                            SMSLog.getInstance().m70d(SMSLog.FORMAT, ContactHelper.f99a, "checkPermission", "AlertPage: FALSE clicked");
                            Iterator it2 = ((ArrayList) hashMap.get("cancelActions")).iterator();
                            while (it2.hasNext()) {
                                Runnable runnable4 = (Runnable) it2.next();
                                if (runnable4 != null) {
                                    runnable4.run();
                                }
                            }
                        }
                    });
                } else if (runnable != null) {
                    runnable.run();
                }
            } else {
                SPHelper.getInstance().setAllowReadContact();
                if (runnable != null) {
                    runnable.run();
                }
            }
        } catch (Throwable th) {
            m472g();
            SMSLog.getInstance().m57w(th);
            if (runnable2 != null) {
                runnable2.run();
            }
        }
    }

    /* renamed from: b */
    public void m478b(final Runnable runnable, final Runnable runnable2) {
        m482a(new Runnable() { // from class: cn.smssdk.contact.b.4
            @Override // java.lang.Runnable
            public void run() {
                new Thread() { // from class: cn.smssdk.contact.b.4.1
                    @Override // java.lang.Thread, java.lang.Runnable
                    public void run() {
                        try {
                            ContactHelper.this.m477b(true);
                            if (runnable != null) {
                                runnable.run();
                            }
                        } catch (Throwable th) {
                            SMSLog.getInstance().m57w(th);
                        }
                    }
                }.start();
            }
        }, new Runnable() { // from class: cn.smssdk.contact.b.5
            @Override // java.lang.Runnable
            public void run() {
                if (runnable2 != null) {
                    runnable2.run();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: b */
    private synchronized void m477b(boolean z) throws Throwable {
        if (InitConfig.m620a().m618b()) {
            SMSLog.getInstance().m58w(SMSLog.FORMAT, f99a, "onRebuild", "disable reading contact, onRebuild return!");
        } else {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "onRebuild", ">>>>>>>>> ContactHelper.onRebuild. force: " + z);
            if (z || !new File(this.f106h).exists()) {
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "onRebuild", ">>>>>>>>> ContactHelper.onRebuild.start");
                ArrayList arrayList = new ArrayList();
                String str = Build.VERSION.SDK_INT <= 10 ? SynthesizeResultDb.KEY_ROWID : "name_raw_contact_id";
                Uri uri = Build.VERSION.SDK_INT <= 9 ? ContactsContract.RawContacts.CONTENT_URI : ContactsContract.Contacts.CONTENT_URI;
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "onRebuild", ">>>>>>>>> query: " + uri);
                ArrayList<HashMap<String, Object>> a = this.f103e.m471a(uri, new String[]{str}, null, null, "sort_key asc");
                if (a != null) {
                    SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "onRebuild", ">>>>>>>>> found: " + a.size() + " ids");
                    Iterator<HashMap<String, Object>> it = a.iterator();
                    while (it.hasNext()) {
                        String str2 = (String) it.next().get(str);
                        if (str2 != null) {
                            arrayList.add(new Contact(this.f103e, str2));
                        }
                    }
                }
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "onRebuild", ">>>>>>>>> ContactHelper.onRebuild.buffercontacts");
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(this.f106h)));
                objectOutputStream.writeInt(arrayList.size());
                Iterator it2 = arrayList.iterator();
                while (it2.hasNext()) {
                    byte[] bytes = ((Contact) it2.next()).toString().getBytes("utf-8");
                    objectOutputStream.writeInt(bytes.length);
                    objectOutputStream.write(bytes);
                }
                objectOutputStream.flush();
                objectOutputStream.close();
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "onRebuild", ">>>>>>>>> ContactHelper.onRebuild.buffercontacts.finish");
            } else {
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "onRebuild", ">>>>>>>>> ContactHelper.onRebuild.quickfinish");
            }
        }
    }

    /* renamed from: f */
    private ArrayList<Contact> m473f() {
        if (this.f106h == null) {
            return new ArrayList<>();
        }
        SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "getContacts", ">>>>>>>>> Contact cachePath: " + this.f106h);
        File file = new File(this.f106h);
        try {
            if (!file.exists()) {
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "getContacts", ">>>>>>>>> Cache file does NOT exist");
                m477b(false);
            } else if (file.length() <= 28) {
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "getContacts", ">>>>>>>>> Cache file length <= 28. length: " + file.length());
                m477b(true);
            } else {
                SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "getContacts", ">>>>>>>>> Cache file exists & length > 28, use the current cache file");
            }
            ObjectInputStream objectInputStream = new ObjectInputStream(new GZIPInputStream(new FileInputStream(this.f106h)));
            int readInt = objectInputStream.readInt();
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "getContacts", ">>>>>>>>> found: " + readInt + " contacts");
            ArrayList<Contact> arrayList = new ArrayList<>(readInt);
            for (int i = 0; i < readInt; i++) {
                byte[] bArr = new byte[objectInputStream.readInt()];
                objectInputStream.readFully(bArr);
                arrayList.add(new Contact(new String(bArr, "utf-8")));
            }
            objectInputStream.close();
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f99a, "getContacts", ">>>>>>>>> ContactHelper.getContacts.finish");
            return arrayList;
        } catch (Throwable th) {
            if (file.exists()) {
                file.delete();
            }
            SMSLog.getInstance().m57w(th);
            return new ArrayList<>();
        }
    }

    /* renamed from: a */
    public ArrayList<HashMap<String, Object>> m481a(boolean z) throws Throwable {
        ArrayList<Contact> f;
        ArrayList arrayList;
        Photo e;
        ArrayList arrayList2;
        if (!DeviceHelper.getInstance(MobSDK.getContext()).checkPermission("android.permission.READ_CONTACTS") || (f = m473f()) == null) {
            return null;
        }
        ArrayList<HashMap<String, Object>> arrayList3 = new ArrayList<>();
        Iterator<Contact> it = f.iterator();
        while (it.hasNext()) {
            Contact next = it.next();
            HashMap<String, Object> hashMap = new HashMap<>();
            Name a = next.m549a();
            if (a != null) {
                String b = a.m515b();
                if (!TextUtils.isEmpty(b)) {
                    hashMap.put("prefixname", b);
                }
                String c = a.m514c();
                if (!TextUtils.isEmpty(c)) {
                    hashMap.put("suffixname", c);
                }
                String d = a.m513d();
                if (!TextUtils.isEmpty(d)) {
                    hashMap.put("lastname", d);
                }
                String e2 = a.m512e();
                if (!TextUtils.isEmpty(e2)) {
                    hashMap.put("firstname", e2);
                }
                String f2 = a.m511f();
                if (!TextUtils.isEmpty(f2)) {
                    hashMap.put("displayname", f2);
                }
                String i = a.m508i();
                if (!TextUtils.isEmpty(i)) {
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put(Constant.EXTRA_KEY, "phonetic");
                    ArrayList arrayList4 = new ArrayList();
                    arrayList4.add(i);
                    hashMap2.put("vals", arrayList4);
                    if (0 == 0) {
                        arrayList2 = new ArrayList();
                        hashMap.put("others", arrayList2);
                    } else {
                        arrayList2 = null;
                    }
                    arrayList2.add(hashMap2);
                } else {
                    arrayList2 = null;
                }
                String g = a.m510g();
                if (!TextUtils.isEmpty(g)) {
                    HashMap hashMap3 = new HashMap();
                    hashMap3.put(Constant.EXTRA_KEY, "phoneticfirstname");
                    ArrayList arrayList5 = new ArrayList();
                    arrayList5.add(g);
                    hashMap3.put("vals", arrayList5);
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList();
                        hashMap.put("others", arrayList2);
                    }
                    arrayList2.add(hashMap3);
                }
                String h = a.m509h();
                if (!TextUtils.isEmpty(h)) {
                    HashMap hashMap4 = new HashMap();
                    hashMap4.put(Constant.EXTRA_KEY, "phoneticlastname");
                    ArrayList arrayList6 = new ArrayList();
                    arrayList6.add(h);
                    hashMap4.put("vals", arrayList6);
                    if (arrayList2 == null) {
                        arrayList2 = new ArrayList();
                        hashMap.put("others", arrayList2);
                    }
                    arrayList2.add(hashMap4);
                }
                arrayList = arrayList2;
            } else {
                arrayList = null;
            }
            Nickname b2 = next.m547b();
            if (b2 != null) {
                String b3 = b2.m507b();
                if (!TextUtils.isEmpty(b3)) {
                    hashMap.put("nickname", b3);
                }
            }
            Organization d2 = next.m545d();
            if (d2 != null) {
                String b4 = d2.m505b();
                if (!TextUtils.isEmpty(b4)) {
                    hashMap.put("company", b4);
                }
                String c2 = d2.m504c();
                if (!TextUtils.isEmpty(c2)) {
                    hashMap.put("position", c2);
                }
            }
            ArrayList<Phone> j = next.m539j();
            if (j != null) {
                Iterator<Phone> it2 = j.iterator();
                ArrayList arrayList7 = null;
                while (it2.hasNext()) {
                    Phone next2 = it2.next();
                    String b5 = next2.m502b();
                    if (!TextUtils.isEmpty(b5)) {
                        if (arrayList7 == null) {
                            arrayList7 = new ArrayList();
                            hashMap.put("phones", arrayList7);
                        }
                        HashMap hashMap5 = new HashMap();
                        hashMap5.put("phone", b5);
                        hashMap5.put("type", Integer.valueOf(next2.m501c()));
                        hashMap5.put("desc", next2.m500d());
                        arrayList7.add(hashMap5);
                    }
                }
            }
            ArrayList<Email> i2 = next.m540i();
            if (i2 != null) {
                Iterator<Email> it3 = i2.iterator();
                ArrayList arrayList8 = null;
                while (it3.hasNext()) {
                    Email next3 = it3.next();
                    String b6 = next3.m527b();
                    if (!TextUtils.isEmpty(b6)) {
                        if (arrayList8 == null) {
                            arrayList8 = new ArrayList();
                            hashMap.put("mails", arrayList8);
                        }
                        HashMap hashMap6 = new HashMap();
                        hashMap6.put("email", b6);
                        hashMap6.put("type", Integer.valueOf(next3.m526c()));
                        hashMap6.put("desc", next3.m525d());
                        arrayList8.add(hashMap6);
                    }
                }
            }
            ArrayList<Postal> k = next.m538k();
            if (k != null) {
                Iterator<Postal> it4 = k.iterator();
                ArrayList arrayList9 = null;
                while (it4.hasNext()) {
                    Postal next4 = it4.next();
                    String b7 = next4.m497b();
                    if (!TextUtils.isEmpty(b7)) {
                        if (arrayList9 == null) {
                            arrayList9 = new ArrayList();
                            hashMap.put("addresses", arrayList9);
                        }
                        HashMap hashMap7 = new HashMap();
                        hashMap7.put("address", b7);
                        hashMap7.put("type", Integer.valueOf(next4.m496c()));
                        hashMap7.put("desc", next4.m495d());
                        arrayList9.add(hashMap7);
                    }
                }
            }
            ArrayList<Event> l = next.m537l();
            if (l != null) {
                Iterator<Event> it5 = l.iterator();
                ArrayList arrayList10 = null;
                while (it5.hasNext()) {
                    Event next5 = it5.next();
                    String b8 = next5.m523b();
                    if (!TextUtils.isEmpty(b8)) {
                        if (arrayList10 == null) {
                            arrayList10 = new ArrayList();
                            hashMap.put("specialdate", arrayList10);
                        }
                        HashMap hashMap8 = new HashMap();
                        hashMap8.put("date", b8);
                        hashMap8.put("type", Integer.valueOf(next5.m522c()));
                        hashMap8.put("desc", next5.m521d());
                        arrayList10.add(hashMap8);
                    }
                }
            }
            ArrayList<Im> h2 = next.m541h();
            if (h2 != null) {
                Iterator<Im> it6 = h2.iterator();
                ArrayList arrayList11 = null;
                while (it6.hasNext()) {
                    Im next6 = it6.next();
                    String b9 = next6.m518b();
                    if (!TextUtils.isEmpty(b9)) {
                        if (arrayList11 == null) {
                            arrayList11 = new ArrayList();
                            hashMap.put("ims", arrayList11);
                        }
                        HashMap hashMap9 = new HashMap();
                        hashMap9.put("val", b9);
                        hashMap9.put("type", Integer.valueOf(next6.m517c()));
                        hashMap9.put("desc", next6.m516d());
                        arrayList11.add(hashMap9);
                    }
                }
            }
            Group c3 = next.m546c();
            if (c3 != null) {
                String b10 = c3.m520b();
                if (!TextUtils.isEmpty(b10)) {
                    hashMap.put("group", b10);
                }
            }
            Note f3 = next.m543f();
            if (f3 != null) {
                String b11 = f3.m506b();
                if (!TextUtils.isEmpty(b11)) {
                    hashMap.put("remarks", b11);
                }
            }
            ArrayList<Website> g2 = next.m542g();
            if (g2 != null) {
                Iterator<Website> it7 = g2.iterator();
                ArrayList arrayList12 = null;
                while (it7.hasNext()) {
                    Website next7 = it7.next();
                    String b12 = next7.m489b();
                    if (!TextUtils.isEmpty(b12)) {
                        if (arrayList12 == null) {
                            arrayList12 = new ArrayList();
                            hashMap.put("websites", arrayList12);
                        }
                        HashMap hashMap10 = new HashMap();
                        hashMap10.put("val", b12);
                        hashMap10.put("type", Integer.valueOf(next7.m488c()));
                        hashMap10.put("desc", next7.m487d());
                        arrayList12.add(hashMap10);
                    }
                }
            }
            ArrayList<Relation> m = next.m536m();
            if (m != null) {
                Iterator<Relation> it8 = m.iterator();
                ArrayList arrayList13 = null;
                while (it8.hasNext()) {
                    Relation next8 = it8.next();
                    String b13 = next8.m493b();
                    if (!TextUtils.isEmpty(b13)) {
                        if (arrayList13 == null) {
                            arrayList13 = new ArrayList();
                            hashMap.put("relations", arrayList13);
                        }
                        HashMap hashMap11 = new HashMap();
                        hashMap11.put("val", b13);
                        hashMap11.put("type", Integer.valueOf(next8.m492c()));
                        hashMap11.put("desc", next8.m491d());
                        arrayList13.add(hashMap11);
                    }
                }
            }
            if (z && (e = next.m544e()) != null) {
                String b14 = e.m499b();
                if (!TextUtils.isEmpty(b14)) {
                    HashMap hashMap12 = new HashMap();
                    hashMap12.put(Constant.EXTRA_KEY, "avatar");
                    ArrayList arrayList14 = new ArrayList();
                    arrayList14.add(b14);
                    hashMap12.put("vals", arrayList14);
                    if (arrayList == null) {
                        arrayList = new ArrayList();
                        hashMap.put("others", arrayList);
                    }
                    arrayList.add(hashMap12);
                }
            }
            arrayList3.add(hashMap);
        }
        return arrayList3;
    }

    /* renamed from: c */
    public String[] m476c() {
        ArrayList<Contact> f = m473f();
        if (f == null) {
            return null;
        }
        HashSet hashSet = new HashSet();
        Iterator<Contact> it = f.iterator();
        while (it.hasNext()) {
            ArrayList<Phone> j = it.next().m539j();
            if (j != null) {
                Iterator<Phone> it2 = j.iterator();
                while (it2.hasNext()) {
                    String b = it2.next().m502b();
                    if (!TextUtils.isEmpty(b)) {
                        hashSet.add(b);
                    }
                }
            }
        }
        String[] strArr = new String[hashSet.size()];
        int i = 0;
        Iterator it3 = hashSet.iterator();
        while (it3.hasNext()) {
            strArr[i] = (String) it3.next();
            i++;
        }
        return strArr.length > 0 ? strArr : null;
    }

    /* renamed from: a */
    public void m485a(OnContactChangeListener onContactChangeListener) {
        this.f104f = onContactChangeListener;
    }

    /* renamed from: g */
    private void m472g() {
        if (SPHelper.getInstance().isWarnWhenReadContact()) {
            UIHandler.sendEmptyMessage(1, new Handler.Callback() { // from class: cn.smssdk.contact.b.6
                @Override // android.os.Handler.Callback
                public boolean handleMessage(Message message) {
                    String str;
                    if (message.what == 1) {
                        if ("zh".equals(DeviceHelper.getInstance(MobSDK.getContext()).getOSLanguage())) {
                            str = String.valueOf(new char[]{24212, 29992, 26080, 26435, 38480, 35835, 21462, 36890, 35759, 24405});
                        } else {
                            str = "no permission to read contacts";
                        }
                        Toast.makeText(MobSDK.getContext(), str, 0).show();
                    }
                    return false;
                }
            });
        }
    }
}
