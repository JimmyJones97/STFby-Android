package com.xzy.forestSystem.smssdk.contact.p006a;

import  com.xzy.forestSystem.mob.tools.utils.Hashon;
import com.xzy.forestSystem.smssdk.utils.SMSLog;

import java.io.Serializable;
import java.util.HashMap;

/* renamed from: cn.smssdk.contact.a.b */
public abstract class ContactObject implements Serializable {

    /* renamed from: a */
    private static final HashMap<String, Class<? extends ContactObject>> f97a = new HashMap<>();

    /* renamed from: b */
    private HashMap<String, Object> f98b;

    static {
        f97a.put("vnd.android.cursor.item/name", Name.class);
        f97a.put("vnd.android.cursor.item/nickname", Nickname.class);
        f97a.put("vnd.android.cursor.item/group_membership", Group.class);
        f97a.put("vnd.android.cursor.item/organization", Organization.class);
        f97a.put("vnd.android.cursor.item/contact_event", Event.class);
        f97a.put("vnd.android.cursor.item/photo", Photo.class);
        f97a.put("vnd.android.cursor.item/note", Note.class);
        f97a.put("vnd.android.cursor.item/website", Website.class);
        f97a.put("vnd.android.cursor.item/im", Im.class);
        f97a.put("vnd.android.cursor.item/email_v2", Email.class);
        f97a.put("vnd.android.cursor.item/phone_v2", Phone.class);
        f97a.put("vnd.android.cursor.item/postal-address_v2", Postal.class);
        f97a.put("vnd.android.cursor.item/relation", Relation.class);
        f97a.put("vnd.android.cursor.item/sip_address", SipAddress.class);
        f97a.put("vnd.android.cursor.item/identity", Identity.class);
    }

    /* renamed from: a */
    public static ContactObject m531a(HashMap<String, Object> hashMap) {
        Class<? extends ContactObject> cls = f97a.get((String) hashMap.get("mimetype"));
        if (cls != null) {
            try {
                ContactObject bVar = (ContactObject) cls.newInstance();
                bVar.m529b(hashMap);
                return bVar;
            } catch (Throwable th) {
                SMSLog.getInstance().m57w(th);
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public void m529b(HashMap<String, Object> hashMap) {
        this.f98b = hashMap;
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public byte[] m533a(String str) {
        return (byte[]) this.f98b.get(str);
    }

    /* access modifiers changed from: protected */
    /* renamed from: b */
    public String m530b(String str) {
        return (String) this.f98b.get(str);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public int m532a(String str, int i) {
        Object obj = this.f98b.get(str);
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        try {
            return Integer.parseInt((String) obj);
        } catch (Throwable th) {
            SMSLog.getInstance().m57w(th);
            return i;
        }
    }

    @Override // java.lang.Object
    public String toString() {
        return this.f98b == null ? "" : new Hashon().fromHashMap(this.f98b);
    }

    /* access modifiers changed from: protected */
    /* renamed from: a */
    public HashMap<String, Object> m534a() {
        return this.f98b;
    }
}
