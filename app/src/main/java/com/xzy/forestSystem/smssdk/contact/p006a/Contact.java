package com.xzy.forestSystem.smssdk.contact.p006a;

import android.provider.ContactsContract;

import  com.xzy.forestSystem.mob.tools.utils.Hashon;
import com.xzy.forestSystem.smssdk.contact.Querier;
import com.xzy.forestSystem.smssdk.utils.SMSLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* renamed from: cn.smssdk.contact.a.a */
public class Contact implements Serializable {

    /* renamed from: a */
    private String f81a;

    /* renamed from: b */
    private Name f82b;

    /* renamed from: c */
    private Nickname f83c;

    /* renamed from: d */
    private Group f84d;

    /* renamed from: e */
    private Organization f85e;

    /* renamed from: f */
    private ArrayList<Event> f86f;

    /* renamed from: g */
    private Photo f87g;

    /* renamed from: h */
    private Note f88h;

    /* renamed from: i */
    private ArrayList<Website> f89i;

    /* renamed from: j */
    private ArrayList<Im> f90j;

    /* renamed from: k */
    private ArrayList<Email> f91k;

    /* renamed from: l */
    private ArrayList<Phone> f92l;

    /* renamed from: m */
    private ArrayList<Postal> f93m;

    /* renamed from: n */
    private ArrayList<SipAddress> f94n;

    /* renamed from: o */
    private ArrayList<Relation> f95o;

    /* renamed from: p */
    private Identity f96p;

    public Contact(String str) {
        try {
            HashMap fromJson = new Hashon().fromJson(str);
            if (fromJson != null) {
                HashMap hashMap = (HashMap) fromJson.get("name");
                if (hashMap != null) {
                    this.f82b = (Name) ContactObject.m531a(hashMap);
                }
                HashMap hashMap2 = (HashMap) fromJson.get("nickname");
                if (hashMap2 != null) {
                    this.f83c = (Nickname) ContactObject.m531a(hashMap2);
                }
                HashMap hashMap3 = (HashMap) fromJson.get("group");
                if (hashMap3 != null) {
                    this.f84d = (Group) ContactObject.m531a(hashMap3);
                }
                HashMap hashMap4 = (HashMap) fromJson.get("organization");
                if (hashMap4 != null) {
                    this.f85e = (Organization) ContactObject.m531a(hashMap4);
                }
                ArrayList arrayList = (ArrayList) fromJson.get("event");
                if (arrayList != null) {
                    if (this.f86f == null) {
                        this.f86f = new ArrayList<>();
                    }
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        this.f86f.add((Event) ContactObject.m531a((HashMap) it.next()));
                    }
                }
                HashMap hashMap5 = (HashMap) fromJson.get("photo");
                if (hashMap5 != null) {
                    this.f87g = (Photo) ContactObject.m531a(hashMap5);
                }
                HashMap hashMap6 = (HashMap) fromJson.get("note");
                if (hashMap6 != null) {
                    this.f88h = (Note) ContactObject.m531a(hashMap6);
                }
                ArrayList arrayList2 = (ArrayList) fromJson.get("websites");
                if (arrayList2 != null) {
                    if (this.f89i == null) {
                        this.f89i = new ArrayList<>();
                    }
                    Iterator it2 = arrayList2.iterator();
                    while (it2.hasNext()) {
                        this.f89i.add((Website) ContactObject.m531a((HashMap) it2.next()));
                    }
                }
                ArrayList arrayList3 = (ArrayList) fromJson.get("ims");
                if (arrayList3 != null) {
                    if (this.f90j == null) {
                        this.f90j = new ArrayList<>();
                    }
                    Iterator it3 = arrayList3.iterator();
                    while (it3.hasNext()) {
                        this.f90j.add((Im) ContactObject.m531a((HashMap) it3.next()));
                    }
                }
                ArrayList arrayList4 = (ArrayList) fromJson.get("emails");
                if (arrayList4 != null) {
                    if (this.f91k == null) {
                        this.f91k = new ArrayList<>();
                    }
                    Iterator it4 = arrayList4.iterator();
                    while (it4.hasNext()) {
                        this.f91k.add((Email) ContactObject.m531a((HashMap) it4.next()));
                    }
                }
                ArrayList arrayList5 = (ArrayList) fromJson.get("phones");
                if (arrayList5 != null) {
                    if (this.f92l == null) {
                        this.f92l = new ArrayList<>();
                    }
                    Iterator it5 = arrayList5.iterator();
                    while (it5.hasNext()) {
                        this.f92l.add((Phone) ContactObject.m531a((HashMap) it5.next()));
                    }
                }
                ArrayList arrayList6 = (ArrayList) fromJson.get("postals");
                if (arrayList6 != null) {
                    if (this.f93m == null) {
                        this.f93m = new ArrayList<>();
                    }
                    Iterator it6 = arrayList6.iterator();
                    while (it6.hasNext()) {
                        this.f93m.add((Postal) ContactObject.m531a((HashMap) it6.next()));
                    }
                }
                ArrayList arrayList7 = (ArrayList) fromJson.get("sipAddresses");
                if (arrayList7 != null) {
                    if (this.f94n == null) {
                        this.f94n = new ArrayList<>();
                    }
                    Iterator it7 = arrayList7.iterator();
                    while (it7.hasNext()) {
                        this.f94n.add((SipAddress) ContactObject.m531a((HashMap) it7.next()));
                    }
                }
                ArrayList arrayList8 = (ArrayList) fromJson.get("relations");
                if (arrayList8 != null) {
                    if (this.f95o == null) {
                        this.f95o = new ArrayList<>();
                    }
                    Iterator it8 = arrayList8.iterator();
                    while (it8.hasNext()) {
                        this.f95o.add((Relation) ContactObject.m531a((HashMap) it8.next()));
                    }
                }
                HashMap hashMap7 = (HashMap) fromJson.get("identity");
                if (hashMap7 != null) {
                    this.f96p = (Identity) ContactObject.m531a(hashMap7);
                }
            }
        } catch (Throwable th) {
            SMSLog.getInstance().m57w(th);
        }
    }

    public Contact(Querier cVar, String str) {
        this.f81a = str;
        m548a(cVar);
    }

    /* renamed from: a */
    private void m548a(Querier cVar) {
        ArrayList<HashMap<String, Object>> a;
        if (this.f81a != null && (a = cVar.m471a(ContactsContract.Data.CONTENT_URI, null, "raw_contact_id=" + this.f81a, null, null)) != null) {
            Iterator<HashMap<String, Object>> it = a.iterator();
            while (it.hasNext()) {
                HashMap<String, Object> next = it.next();
                ContactObject a2 = ContactObject.m531a(next);
                if (a2 != null) {
                    if (a2 instanceof Name) {
                        this.f82b = (Name) a2;
                    } else if (a2 instanceof Nickname) {
                        this.f83c = (Nickname) a2;
                    } else if (a2 instanceof Group) {
                        ArrayList<HashMap<String, Object>> a3 = cVar.m471a(ContactsContract.Groups.CONTENT_URI, null, "_id=" + next.get("data1"), null, null);
                        if (a3 != null && a3.size() > 0) {
                            HashMap<String, Object> hashMap = a3.get(0);
                            hashMap.put("mimetype", "vnd.android.cursor.item/group_membership");
                            this.f84d = (Group) ContactObject.m531a(hashMap);
                        }
                    } else if (a2 instanceof Organization) {
                        this.f85e = (Organization) a2;
                    } else if (a2 instanceof Event) {
                        if (this.f86f == null) {
                            this.f86f = new ArrayList<>();
                        }
                        this.f86f.add((Event) a2);
                    } else if (a2 instanceof Photo) {
                        this.f87g = (Photo) a2;
                    } else if (a2 instanceof Note) {
                        this.f88h = (Note) a2;
                    } else if (a2 instanceof Website) {
                        if (this.f89i == null) {
                            this.f89i = new ArrayList<>();
                        }
                        this.f89i.add((Website) a2);
                    } else if (a2 instanceof Im) {
                        if (this.f90j == null) {
                            this.f90j = new ArrayList<>();
                        }
                        this.f90j.add((Im) a2);
                    } else if (a2 instanceof Email) {
                        if (this.f91k == null) {
                            this.f91k = new ArrayList<>();
                        }
                        this.f91k.add((Email) a2);
                    } else if (a2 instanceof Phone) {
                        if (this.f92l == null) {
                            this.f92l = new ArrayList<>();
                        }
                        this.f92l.add((Phone) a2);
                    } else if (a2 instanceof Postal) {
                        if (this.f93m == null) {
                            this.f93m = new ArrayList<>();
                        }
                        this.f93m.add((Postal) a2);
                    } else if (a2 instanceof Relation) {
                        if (this.f93m == null) {
                            this.f95o = new ArrayList<>();
                        }
                        this.f95o.add((Relation) a2);
                    } else if (a2 instanceof SipAddress) {
                        if (this.f94n == null) {
                            this.f94n = new ArrayList<>();
                        }
                        this.f94n.add((SipAddress) a2);
                    } else if (a2 instanceof Identity) {
                        this.f96p = (Identity) a2;
                    }
                }
            }
        }
    }

    /* renamed from: a */
    public Name m549a() {
        return this.f82b;
    }

    /* renamed from: b */
    public Nickname m547b() {
        return this.f83c;
    }

    /* renamed from: c */
    public Group m546c() {
        return this.f84d;
    }

    /* renamed from: d */
    public Organization m545d() {
        return this.f85e;
    }

    /* renamed from: e */
    public Photo m544e() {
        return this.f87g;
    }

    /* renamed from: f */
    public Note m543f() {
        return this.f88h;
    }

    /* renamed from: g */
    public ArrayList<Website> m542g() {
        return this.f89i;
    }

    /* renamed from: h */
    public ArrayList<Im> m541h() {
        return this.f90j;
    }

    /* renamed from: i */
    public ArrayList<Email> m540i() {
        return this.f91k;
    }

    /* renamed from: j */
    public ArrayList<Phone> m539j() {
        return this.f92l;
    }

    /* renamed from: k */
    public ArrayList<Postal> m538k() {
        return this.f93m;
    }

    /* renamed from: l */
    public ArrayList<Event> m537l() {
        return this.f86f;
    }

    /* renamed from: m */
    public ArrayList<Relation> m536m() {
        return this.f95o;
    }

    @Override // java.lang.Object
    public String toString() {
        return new Hashon().fromHashMap(m535n());
    }

    /* renamed from: n */
    public HashMap<String, Object> m535n() {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (this.f82b != null) {
            hashMap.put("name", this.f82b.m534a());
        }
        if (this.f83c != null) {
            hashMap.put("nickname", this.f83c.m534a());
        }
        if (this.f84d != null) {
            hashMap.put("group", this.f84d.m534a());
        }
        if (this.f85e != null) {
            hashMap.put("organization", this.f85e.m534a());
        }
        if (this.f86f != null) {
            ArrayList arrayList = new ArrayList();
            Iterator<Event> it = this.f86f.iterator();
            while (it.hasNext()) {
                arrayList.add(it.next().m534a());
            }
            hashMap.put("events", arrayList);
        }
        if (this.f87g != null) {
            hashMap.put("photo", this.f87g.m534a());
        }
        if (this.f88h != null) {
            hashMap.put("note", this.f88h.m534a());
        }
        if (this.f89i != null) {
            ArrayList arrayList2 = new ArrayList();
            Iterator<Website> it2 = this.f89i.iterator();
            while (it2.hasNext()) {
                arrayList2.add(it2.next().m534a());
            }
            hashMap.put("websites", arrayList2);
        }
        if (this.f90j != null) {
            ArrayList arrayList3 = new ArrayList();
            Iterator<Im> it3 = this.f90j.iterator();
            while (it3.hasNext()) {
                arrayList3.add(it3.next().m534a());
            }
            hashMap.put("ims", arrayList3);
        }
        if (this.f91k != null) {
            ArrayList arrayList4 = new ArrayList();
            Iterator<Email> it4 = this.f91k.iterator();
            while (it4.hasNext()) {
                arrayList4.add(it4.next().m534a());
            }
            hashMap.put("emails", arrayList4);
        }
        if (this.f92l != null) {
            ArrayList arrayList5 = new ArrayList();
            Iterator<Phone> it5 = this.f92l.iterator();
            while (it5.hasNext()) {
                arrayList5.add(it5.next().m534a());
            }
            hashMap.put("phones", arrayList5);
        }
        if (this.f93m != null) {
            ArrayList arrayList6 = new ArrayList();
            Iterator<Postal> it6 = this.f93m.iterator();
            while (it6.hasNext()) {
                arrayList6.add(it6.next().m534a());
            }
            hashMap.put("postals", arrayList6);
        }
        if (this.f94n != null) {
            ArrayList arrayList7 = new ArrayList();
            Iterator<SipAddress> it7 = this.f94n.iterator();
            while (it7.hasNext()) {
                arrayList7.add(it7.next().m534a());
            }
            hashMap.put("sipAddresses", arrayList7);
        }
        if (this.f95o != null) {
            ArrayList arrayList8 = new ArrayList();
            Iterator<Relation> it8 = this.f95o.iterator();
            while (it8.hasNext()) {
                arrayList8.add(it8.next().m534a());
            }
            hashMap.put("relations", arrayList8);
        }
        if (this.f96p != null) {
            hashMap.put("identity", this.f96p.m534a());
        }
        return hashMap;
    }
}
