package com.xzy.forestSystem.smssdk.net;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.commons.SMSSDK;
import  com.xzy.forestSystem.mob.commons.authorize.DeviceAuthorizer;
import  com.xzy.forestSystem.mob.commons.eventrecoder.EventRecorder;
import  com.xzy.forestSystem.mob.tools.utils.Data;
import  com.xzy.forestSystem.mob.tools.utils.DeviceHelper;
import  com.xzy.forestSystem.mob.tools.utils.Hashon;
import  com.xzy.forestSystem.mob.tools.utils.ReflectHelper;
import  com.xzy.forestSystem.mob.tools.utils.ResHelper;
import com.xzy.forestSystem.baidu.speech.easr.stat.SynthesizeResultDb;
import com.xzy.forestSystem.smssdk.InitConfig;
import com.xzy.forestSystem.smssdk.utils.Constants;
import com.xzy.forestSystem.smssdk.utils.LogCatHelper;
import com.xzy.forestSystem.smssdk.utils.SMSLog;
import com.xzy.forestSystem.smssdk.utils.SPHelper;
import com.xzy.forestSystem.smssdk.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* renamed from: cn.smssdk.net.b */
public class Config {

    /* renamed from: a */
    public static HashMap<String, Object> f136a;

    /* renamed from: b */
    public static HashMap<String, Object> f137b;

    /* renamed from: c */
    private static final String f138c = Config.class.getSimpleName();

    /* renamed from: w */
    private static Config f139w;

    /* renamed from: d */
    private SPHelper f140d = SPHelper.getInstance();

    /* renamed from: e */
    private Hashon f141e = new Hashon();

    /* renamed from: f */
    private Map<Integer, ServiceApi> f142f;

    /* renamed from: g */
    private final InitApi f143g = new InitApi();

    /* renamed from: h */
    private ReentrantLock f144h = new ReentrantLock();

    /* renamed from: i */
    private ReentrantReadWriteLock f145i = new ReentrantReadWriteLock();

    /* renamed from: j */
    private C0115a f146j;

    /* renamed from: k */
    private boolean f147k = false;

    /* renamed from: l */
    private boolean f148l;

    /* renamed from: m */
    private long f149m;

    /* renamed from: n */
    private int f150n = 1;

    /* renamed from: o */
    private long f151o;

    /* renamed from: p */
    private String f152p;

    /* renamed from: q */
    private String f153q;

    /* renamed from: r */
    private int f154r;

    /* renamed from: s */
    private String f155s;

    /* renamed from: t */
    private String f156t;

    /* renamed from: u */
    private String f157u;

    /* renamed from: v */
    private DeviceHelper f158v = DeviceHelper.getInstance(MobSDK.getContext());

    /* renamed from: a */
    public static Config m448a() {
        if (f139w == null) {
            synchronized (Config.class) {
                f139w = new Config();
                new Thread(new Runnable() { // from class: cn.smssdk.net.b.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            Config.f139w.m419h();
                        } catch (Throwable th) {
                            th.printStackTrace();
                        }
                    }
                }).start();
            }
        }
        return f139w;
    }

    private Config() {
    }

    /* renamed from: b */
    public boolean m434b() {
        return this.f148l;
    }

    /* renamed from: c */
    public void m429c() {
        this.f148l = false;
    }

    /* renamed from: d */
    public long m426d() {
        return this.f151o;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: a */
    private boolean m436a(HashMap<String, Object> hashMap) throws Throwable {
        Integer num;
        if (Constants.f191c.booleanValue()) {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f138c, "parseConfig", "Parse config. config: " + this.f141e.fromHashMap(hashMap));
        }
        Long l = (Long) hashMap.get("updateAt");
        if (l.longValue() <= this.f149m) {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f138c, "parseConfig", "'updateAt' not changed, DO NOT update local config.");
            return false;
        }
        if (this.f149m == 0) {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f138c, "parseConfig", "Initialize local config.");
        } else {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f138c, "parseConfig", "'updateAt' changed, update local config.");
        }
        this.f149m = l.longValue();
        long longValue = ((Long) hashMap.get("zoneAt")).longValue();
        if (longValue > this.f151o) {
            if (this.f151o != 0) {
                this.f148l = true;
            }
            this.f151o = longValue;
        }
        this.f150n = ((Integer) hashMap.get("request")).intValue();
        if (!(InitConfig.m620a().m618b() || (num = (Integer) hashMap.get("uploadContacts")) == null || num.intValue() == 1)) {
            InitConfig.m620a().m617b(true);
        }
        this.f152p = (String) hashMap.get("publicKey");
        this.f153q = (String) hashMap.get("modulus");
        Integer num2 = (Integer) hashMap.get("size");
        this.f154r = num2 != null ? num2.intValue() : 0;
        if (!TextUtils.isEmpty(this.f152p) && !TextUtils.isEmpty(this.f153q) && this.f154r > 0) {
            Crypto.m412a(this.f152p, this.f153q, this.f154r);
        }
        ArrayList arrayList = (ArrayList) ((HashMap) hashMap.get(SynthesizeResultDb.KEY_RESULT)).get("urls");
        if (this.f142f == null) {
            this.f142f = new HashMap();
        } else if (this.f142f != null && this.f142f.size() > 0) {
            this.f142f.clear();
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            HashMap<String, Object> hashMap2 = (HashMap) it.next();
            if (Constants.f190b.booleanValue()) {
                SMSLog.getInstance().m70d("api: " + LogCatHelper.m377a(hashMap2) + " urls.size: " + this.f142f.size(), new Object[0]);
            }
            ServiceApi gVar = new ServiceApi();
            gVar.m385a(hashMap2);
            gVar.m455a(this.f145i);
            this.f142f.put(Integer.valueOf(gVar.m459a()), gVar);
        }
        return true;
    }

    /* renamed from: a */
    private ServiceApi m447a(int i) throws Throwable {
        String str;
        if (MobSDK.getAppkey() != null && MobSDK.getAppkey().equalsIgnoreCase("moba6b6c6d6")) {
            if ("zh".equals(DeviceHelper.getInstance(MobSDK.getContext()).getOSLanguage())) {
                str = String.valueOf(new char[]{25152, 22635, 20889, 'A', 'P', 'P', 'K', 'E', 'Y', 20165, 20379, 27979, 35797, 20351, 29992, 65292, 19988, 19981, 23450, 26399, 22833, 25928, 65292, 35831, 21040, 'm', 'o', 'b', '.', 'c', 'o', 'm', 21518, 21488, 30003, 35831, 27491, 24335, 'A', 'P', 'P', 'K', 'E', 'Y'});
            } else {
                str = "This appkey only for demo!Please request a new one for your own App";
            }
            SMSLog.getInstance().m67e(SMSLog.FORMAT, f138c, "getApi", "SMSSDK WARNING: " + str);
        }
        m419h();
        if (this.f150n != 0) {
            return this.f142f.get(Integer.valueOf(i));
        }
        throw new Throwable("{\"status\":604,\"detail\":\"" + MobSDK.getContext().getResources().getString(ResHelper.getStringRes(MobSDK.getContext(), "smssdk_error_desc_604")) + "\"}");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: h */
    private void m419h() throws Throwable {
        try {
            this.f145i.writeLock().lock();
            if (!this.f147k) {
                EventRecorder.prepare();
                String str = null;
                if (Constants.f192d.booleanValue()) {
                    str = EventRecorder.checkRecord("SMSSDK");
                }
                if (str != null) {
                    EventRecorder.clear();
                }
                String config = this.f140d.getConfig();
                this.f146j = new C0115a();
                String a = m439a(config);
                EventRecorder.addBegin("SMSSDK", "parseConfig");
                if (!TextUtils.isEmpty(str) || TextUtils.isEmpty(a)) {
                    HashMap<String, Object> fromJson = this.f141e.fromJson("{\"updateAt\":1496649531507,\"zoneAt\":1496649531507,\"uploadContacts\":1,\"result\":{\"urls\":[{\"name\":\"getToken\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v2/token/get\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"sign\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"getZoneList\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v2/utils/zonelist\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"uploadContacts\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v2/relat/apply\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"myPhone\",\"simserial\",\"operator\",\"secretKey\",\"contacts\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"getFriend\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v2/relat/fm\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"contactphones\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"submitUser\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v2/app/submituserinfo\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"phone\",\"uid\",\"nickname\",\"avatar\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"sendTextSMS\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v2/verify/code\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"phone\",\"simserial\",\"myPhone\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"sendVoiceSMS\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v2/voice/verify/code\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"phone\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"verifyCode\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v2/client/verification\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"phone\",\"code\"],\"zip\":0,\"request\":1,\"frequency\":0}]},\"request\":1}");
                    if (fromJson != null) {
                        f137b = new HashMap<>(fromJson);
                    }
                    m436a(fromJson);
                } else {
                    try {
                        m436a(this.f141e.fromJson(a));
                    } catch (Throwable th) {
                        this.f140d.setConfig("");
                        m436a(this.f141e.fromJson("{\"updateAt\":1496649531507,\"zoneAt\":1496649531507,\"uploadContacts\":1,\"result\":{\"urls\":[{\"name\":\"getToken\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v2/token/get\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"sign\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"getZoneList\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v2/utils/zonelist\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"uploadContacts\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v2/relat/apply\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"myPhone\",\"simserial\",\"operator\",\"secretKey\",\"contacts\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"getFriend\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v2/relat/fm\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"contactphones\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"submitUser\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v2/app/submituserinfo\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"phone\",\"uid\",\"nickname\",\"avatar\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"sendTextSMS\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v2/verify/code\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"phone\",\"simserial\",\"myPhone\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"sendVoiceSMS\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v2/voice/verify/code\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"phone\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"verifyCode\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v2/client/verification\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"phone\",\"code\"],\"zip\":0,\"request\":1,\"frequency\":0}]},\"request\":1}"));
                    }
                }
                this.f147k = true;
                this.f146j.start();
                EventRecorder.addEnd("SMSSDK", "parseConfig");
                this.f145i.writeLock().unlock();
            }
        } finally {
            this.f145i.writeLock().unlock();
        }
    }

    /* renamed from: a */
    private String m439a(String str) throws Throwable {
        if (TextUtils.isEmpty(str)) {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f138c, "checkConfigVersion", "Local config does not exist in SP, use default config.");
            return str;
        } else if (((Integer) this.f141e.fromJson(str).get("expire_at")) != null) {
            this.f140d.setConfig("");
            this.f140d.setBufferedCountrylist("");
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f138c, "checkConfigVersion", "Local config is for SMSSDK V2.1.4 or older, clear SP and use default config instead.");
            return "";
        } else {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f138c, "checkConfigVersion", "Local config is for SMSSDK V3.0.0 or later, use local config.");
            return str;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: a */
    private String m435a(boolean z) {
        if (z || TextUtils.isEmpty(this.f155s)) {
            try {
                this.f144h.lock();
                if (!TextUtils.isEmpty(this.f155s)) {
                    return this.f155s;
                }
                this.f155s = DeviceAuthorizer.authorize(new SMSSDK());
                this.f144h.unlock();
            } finally {
                this.f144h.unlock();
            }
        }
        return this.f155s;
    }

    /* renamed from: b */
    private synchronized String m430b(boolean z) throws Throwable {
        String str;
        this.f156t = this.f140d.getToken();
        SMSLog.getInstance().m70d(SMSLog.FORMAT, f138c, "getToken", "force: " + z + ", tokenInSp: " + this.f156t);
        if (z || TextUtils.isEmpty(this.f156t)) {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f138c, "getToken", "Observe token from server.");
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("sign", m418i());
            this.f156t = (String) ((HashMap) m445a(3, hashMap).get(SynthesizeResultDb.KEY_RESULT)).get("token");
            if (TextUtils.isEmpty(this.f156t)) {
                throw new Throwable("get token error!");
            }
            this.f140d.setToken(this.f156t);
            str = this.f156t;
        } else {
            SMSLog.getInstance().m70d(SMSLog.FORMAT, f138c, "getToken", "Use token stored in SP. token=" + this.f156t);
            str = this.f156t;
        }
        return str;
    }

    /* renamed from: a */
    public HashMap<String, Object> m445a(int i, HashMap<String, Object> hashMap) throws Throwable {
        ServiceApi a = m447a(i);
        HashMap<String, Object> a2 = m444a((BaseApi) a, hashMap, false, false, 1);
        if (a.m459a() != 9 || a2 == null) {
            if (a2 != null) {
                a.m382c();
            }
        } else if (((Integer) a2.get("smart")) == null) {
            a.m382c();
        }
        return a2;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: a */
    private HashMap<String, Object> m444a(BaseApi aVar, HashMap<String, Object> hashMap, boolean z, boolean z2, int i) throws Throwable {
        String str;
        HashMap<String, Object> hashMap2 = null;
        if (i > 5) {
            String str2 = "Server is busy!";
            int stringRes = ResHelper.getStringRes(MobSDK.getContext(), "smssdk_error_desc_server_busy");
            if (stringRes > 0) {
                str2 = MobSDK.getContext().getString(stringRes);
            }
            HashMap hashMap3 = new HashMap();
            hashMap3.put("description", str2);
            throw new Throwable(this.f141e.fromHashMap(hashMap3));
        }
        String a = m435a(z);
        if (!(aVar instanceof ServiceApi) || aVar.m459a() == 3) {
            str = null;
        } else {
            str = m430b(z2);
        }
        try {
            String b = aVar.m451b(a, str, hashMap);
            try {
                hashMap2 = this.f141e.fromJson(b);
            } catch (Throwable th) {
                SMSLog.getInstance().m66e(th);
            }
            if (hashMap2 == null || hashMap2.size() <= 0) {
                SMSLog.getInstance().m67e(SMSLog.FORMAT, f138c, "post", "Response is empty");
                throw new Throwable("[hashon]Response is empty");
            }
            Object obj = hashMap2.get("status");
            if (obj == null || !(obj instanceof Integer)) {
                SMSLog.getInstance().m67e(SMSLog.FORMAT, f138c, "post", "'status' in response is null or not a Integer");
                throw new Throwable(b);
            }
            int intValue = ((Integer) obj).intValue();
            if (intValue != 200) {
                return m446a(intValue, aVar, hashMap, i, new Throwable(b));
            }
            return hashMap2;
        } catch (Throwable th2) {
            return m437a(th2, aVar, hashMap, i);
        }
    }

    /* renamed from: a */
    private HashMap<String, Object> m437a(Throwable th, BaseApi aVar, HashMap<String, Object> hashMap, int i) throws Throwable {
        String message = th.getMessage();
        SMSLog.getInstance().m67e(SMSLog.FORMAT, f138c, "handleThrowable", "[" + aVar.f128b + "]Request error. url: " + aVar.f129c + ". msg: " + message);
        HashMap fromJson = this.f141e.fromJson(message);
        Integer num = (Integer) fromJson.get("status");
        if (num == null || num.intValue() == 0) {
            throw th;
        }
        HashMap<String, Object> a = m446a(num.intValue(), aVar, hashMap, i, th);
        if (a != null) {
            return a;
        }
        fromJson.put("description", m433b(num.intValue()));
        fromJson.put("detail", m428c(num.intValue()));
        throw new Throwable(this.f141e.fromHashMap(fromJson));
    }

    /* renamed from: a */
    private HashMap<String, Object> m446a(int i, BaseApi aVar, HashMap<String, Object> hashMap, int i2, Throwable th) throws Throwable {
        ServiceApi gVar;
        int a;
        SMSLog.getInstance().m70d(SMSLog.FORMAT, f138c, "handleErrorStatus", "[" + aVar.f128b + "]Handle error status. status: " + i + ", count: " + i2);
        int i3 = i2 + 1;
        if (i == 453) {
            if (!(aVar instanceof ServiceApi) || (a = aVar.m459a()) <= 0) {
                gVar = (ServiceApi) aVar;
            } else {
                gVar = m447a(a);
            }
            return m444a(gVar, hashMap, false, false, i3);
        } else if (i == 419 || i == 420) {
            this.f140d.setToken("");
            return m444a(aVar, hashMap, true, true, i3);
        } else if (i == 401 || i == 402) {
            this.f140d.setToken("");
            return m444a(aVar, hashMap, false, true, i3);
        } else if (i == 482) {
            return m444a(aVar, hashMap, false, false, i3);
        } else {
            throw th;
        }
    }

    /* renamed from: b */
    private String m433b(int i) {
        try {
            int stringRes = ResHelper.getStringRes(MobSDK.getContext(), "smssdk_error_desc_" + i);
            if (stringRes > 0) {
                return MobSDK.getContext().getString(stringRes);
            }
        } catch (Throwable th) {
            SMSLog.getInstance().m57w(th);
        }
        return null;
    }

    /* renamed from: c */
    private String m428c(int i) {
        try {
            int stringRes = ResHelper.getStringRes(MobSDK.getContext(), "smssdk_error_detail_" + i);
            if (stringRes > 0) {
                return MobSDK.getContext().getString(stringRes);
            }
        } catch (Throwable th) {
            SMSLog.getInstance().m57w(th);
        }
        return null;
    }

    /* renamed from: i */
    @SuppressLint("WrongConstant")
    private String m418i() {
        if (!TextUtils.isEmpty(this.f157u)) {
            return this.f157u;
        }
        try {
            this.f157u = Data.MD5(MobSDK.getContext().getPackageManager().getPackageInfo(MobSDK.getContext().getPackageName(), 64).signatures[0].toByteArray());
            return this.f157u;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /* renamed from: a */
    public String m438a(String str, byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        return Data.byteToHex(Crypto.m411a(str, bArr));
    }

    /* renamed from: b */
    public String m431b(String str, byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        return new String(Crypto.m407a(Crypto.m404c(str), bArr)).trim();
    }

    /* renamed from: e */
    public static String m424e() {
        boolean z = true;
        try {
            ReflectHelper.invokeStaticMethod("DeviceHelper", "getInstance", MobSDK.getContext());
        } catch (Throwable th) {
            z = false;
        }
        return "reflectInvoke: " + z;
    }

    /* compiled from: Config */
    /* access modifiers changed from: package-private */
    /* renamed from: cn.smssdk.net.b$a */
    public class C0115a extends Thread {

        /* renamed from: b */
        private boolean f160b = true;

        C0115a() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            try {
                m417a();
            } catch (Throwable th) {
                if (Config.this.f145i.writeLock().tryLock()) {
                    Config.this.f145i.writeLock().unlock();
                }
            }
        }

        /* JADX INFO: finally extract failed */
        /* renamed from: a */
        private void m417a() throws Throwable {
            boolean z = false;
            EventRecorder.addBegin("SMSSDK", "getConfig");
            String a = Config.this.m435a(false);
            if (TextUtils.isEmpty(a)) {
                throw new Throwable("duid is empty!");
            }
            HashMap hashMap = new HashMap();
            hashMap.put("appkey", MobSDK.getAppkey());
            hashMap.put("duid", a);
            hashMap.put("sdkver", Integer.valueOf(Utils.m373a()));
            hashMap.put("plat", Integer.valueOf(Config.this.f158v.getPlatformCode()));
            HashMap a2 = Config.this.m444a((BaseApi) Config.this.f143g, (HashMap<String, Object>) hashMap, false, false, 1);
            if (Constants.f191c.booleanValue()) {
                SMSLog.getInstance().m70d(SMSLog.FORMAT, Config.f138c, "getConfigFromService", "Config from server got. resp: " + Config.this.f141e.fromHashMap(a2));
            }
            if (a2 == null) {
                throw new Throwable("response is empty");
            }
            Config.f136a = new HashMap<>(a2);
            try {
                Config.this.f145i.writeLock().lock();
                try {
                    z = Config.this.m436a(a2);
                    this.f160b = false;
                } catch (Throwable th) {
                    SMSLog.getInstance().m58w(SMSLog.FORMAT, Config.f138c, "getConfigFromService", "parseConfig encounters error, use default config re-configure");
                    this.f160b = true;
                    Config.this.f140d.setConfig("");
                    Config.this.m436a(Config.this.f141e.fromJson("{\"updateAt\":1496649531507,\"zoneAt\":1496649531507,\"uploadContacts\":1,\"result\":{\"urls\":[{\"name\":\"getToken\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v2/token/get\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"sign\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"getZoneList\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v2/utils/zonelist\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"uploadContacts\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v2/relat/apply\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"myPhone\",\"simserial\",\"operator\",\"secretKey\",\"contacts\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"getFriend\",\"host\":\"addrlist.sms.mob.com\",\"port\":80,\"action\":\"/v2/relat/fm\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"contactphones\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"submitUser\",\"host\":\"sdkapi.sms.mob.com\",\"port\":80,\"action\":\"/v2/app/submituserinfo\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"phone\",\"uid\",\"nickname\",\"avatar\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"sendTextSMS\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v2/verify/code\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"phone\",\"simserial\",\"myPhone\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"sendVoiceSMS\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v2/voice/verify/code\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"phone\"],\"zip\":0,\"request\":1,\"frequency\":0},{\"name\":\"verifyCode\",\"host\":\"code.sms.mob.com\",\"port\":80,\"action\":\"/v2/client/verification\",\"params\":[\"appkey\",\"duid\",\"sdkver\",\"plat\",\"zone\",\"phone\",\"code\"],\"zip\":0,\"request\":1,\"frequency\":0}]},\"request\":1}"));
                }
                if (!this.f160b && z) {
                    SMSLog.getInstance().m70d(SMSLog.FORMAT, Config.f138c, "getConfigFromService", "config observed from server has been updated, store into SP");
                    Config.this.f140d.setConfig(Config.this.f141e.fromHashMap(a2));
                }
                Config.this.f145i.writeLock().unlock();
                EventRecorder.addEnd("SMSSDK", "getConfig");
            } catch (Throwable th2) {
                Config.this.f145i.writeLock().unlock();
                throw th2;
            }
        }
    }
}
