package  com.xzy.forestSystem.mob.commons.logcollector;

import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import com.xzy.forestSystem.baidu.speech.VoiceRecognitionService;
import com.xzy.forestSystem.baidu.speech.easr.stat.SynthesizeResultDb;
import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.SSDKHandlerThread;
import  com.xzy.forestSystem.mob.tools.log.NLog;
import  com.xzy.forestSystem.mob.tools.network.KVPair;
import  com.xzy.forestSystem.mob.tools.network.NetworkHelper;
import  com.xzy.forestSystem.mob.tools.utils.Data;
import  com.xzy.forestSystem.mob.tools.utils.DeviceHelper;
import  com.xzy.forestSystem.mob.tools.utils.FileLocker;
import  com.xzy.forestSystem.mob.tools.utils.Hashon;
import  com.xzy.forestSystem.mob.tools.utils.ResHelper;
import  com.xzy.forestSystem.gogisapi.Common.Constant;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.GZIPOutputStream;
import org.gdal.ogr.ogrConstants;

/* renamed from:  com.xzy.forestSystem.mob.commons.logcollector.c */
public class LogsManager extends SSDKHandlerThread {

    /* renamed from: a */
    private static LogsManager f368a;

    /* renamed from: b */
    private static String f369b = "http://api.exc.mob.com:80";

    /* renamed from: c */
    private HashMap<String, Integer> f370c = new HashMap<>();

    /* renamed from: d */
    private NetworkHelper f371d = new NetworkHelper();

    /* renamed from: e */
    private LogsSharePrefrence f372e = LogsSharePrefrence.m87a();

    /* renamed from: f */
    private File f373f = new File(MobSDK.getContext().getFilesDir(), ".lock");

    /* renamed from: g */
    private FileLocker f374g = new FileLocker();

    private LogsManager() {
        if (!this.f373f.exists()) {
            try {
                this.f373f.createNewFile();
            } catch (Exception e) {
                MobLog.getInstance().m57w(e);
            }
        }
        NLog.setContext(MobSDK.getContext());
        startThread();
    }

    /* renamed from: a */
    public static synchronized LogsManager m101a() {
        LogsManager cVar;
        synchronized (LogsManager.class) {
            if (f368a == null) {
                f368a = new LogsManager();
            }
            cVar = f368a;
        }
        return cVar;
    }

    /* renamed from: a */
    public void m99a(int i, String str) {
        Message message = new Message();
        message.what = 100;
        message.arg1 = i;
        message.obj = str;
        this.handler.sendMessage(message);
    }

    /* renamed from: a */
    public void m100a(int i, int i2, String str, String str2) {
        Message message = new Message();
        message.what = ogrConstants.wkbLinearRing;
        message.arg1 = i;
        message.arg2 = i2;
        message.obj = new Object[]{str, str2};
        this.handler.sendMessage(message);
    }

    /* renamed from: a */
    private void m97a(Message message) {
        this.handler.sendMessageDelayed(message, 1000);
    }

    /* renamed from: b */
    public void m93b(int i, int i2, String str, String str2) {
        m100a(i, i2, str, str2);
        try {
            this.handler.wait();
        } catch (Throwable th) {
        }
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.mob.tools.SSDKHandlerThread
    public void onMessage(Message message) {
        switch (message.what) {
            case 100:
                m91b(message);
                return;
            case ogrConstants.wkbLinearRing /* 101 */:
                m88c(message);
                return;
            default:
                return;
        }
    }

    /* renamed from: b */
    private void m91b(Message message) {
        try {
            int i = message.arg1;
            String str = (String) message.obj;
            m92b(i, str);
            m98a(i, str, null);
        } catch (Throwable th) {
            MobLog.getInstance().m57w(th);
        }
    }

    /* renamed from: c */
    private void m88c(Message message) {
        int i;
        String MD5;
        ArrayList arrayList;
        try {
            int i2 = message.arg1;
            Object[] objArr = (Object[]) message.obj;
            String str = (String) objArr[0];
            String str2 = (String) objArr[1];
            if (message.arg2 == 0) {
                i = 2;
            } else if (message.arg2 == 2) {
                i = 1;
            } else {
                i = 1;
            }
            String g = this.f372e.m75g();
            if (!TextUtils.isEmpty(g) && (arrayList = (ArrayList) new Hashon().fromJson(g).get("fakelist")) != null && arrayList.size() > 0) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    String str3 = (String) it.next();
                    if (!TextUtils.isEmpty(str3) && str2.contains(str3)) {
                        return;
                    }
                }
            }
            int d = this.f372e.m78d();
            int e = this.f372e.m77e();
            int f = this.f372e.m76f();
            if (3 != i || -1 != f) {
                if (1 != i || -1 != d) {
                    if (2 != i || -1 != e) {
                        MD5 = Data.MD5(str2);
                        this.f374g.setLockFile(this.f373f.getAbsolutePath());
                        if (this.f374g.lock(false)) {
                            MessageUtils.m74a(System.currentTimeMillis() - this.f372e.m82b(), str2, i, MD5);
                        }
                        this.f374g.release();
                        this.f370c.remove(MD5);
                        if (3 == i && 1 == f) {
                            m98a(i2, str, new String[]{String.valueOf(3)});
                        } else if (1 == i && 1 == d) {
                            m98a(i2, str, new String[]{String.valueOf(1)});
                        } else if (2 == i && 1 == e) {
                            m98a(i2, str, new String[]{String.valueOf(2)});
                        }
                    }
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().m57w(th);
        }
    }

    /* renamed from: b */
    private String m94b() {
        return f369b + "/errconf";
    }

    /* renamed from: b */
    private void m92b(int i, String str) throws Throwable {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        DeviceHelper instance = DeviceHelper.getInstance(MobSDK.getContext());
        arrayList.add(new KVPair<>(Constant.EXTRA_KEY, MobSDK.getAppkey()));
        arrayList.add(new KVPair<>("sdk", str));
        arrayList.add(new KVPair<>("apppkg", String.valueOf(instance.getPackageName())));
        arrayList.add(new KVPair<>("appver", String.valueOf(instance.getAppVersion())));
        arrayList.add(new KVPair<>("sdkver", String.valueOf(i)));
        arrayList.add(new KVPair<>("plat", String.valueOf(instance.getPlatformCode())));
        try {
            NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
            networkTimeOut.readTimout = 10000;
            networkTimeOut.connectionTimeout = 10000;
            String httpPost = this.f371d.httpPost(m94b(), arrayList, (KVPair<String>) null, (ArrayList<KVPair<String>>) null, networkTimeOut);
            MobLog.getInstance().m64i("get server config == %s", httpPost);
            HashMap fromJson = new Hashon().fromJson(httpPost);
            if ("-200".equals(String.valueOf(fromJson.get("status")))) {
                MobLog.getInstance().m64i("error log server config response fail !!", new Object[0]);
                return;
            }
            Object obj = fromJson.get(SynthesizeResultDb.KEY_RESULT);
            if (obj != null && (obj instanceof HashMap)) {
                HashMap hashMap = (HashMap) obj;
                if (hashMap.containsKey("timestamp")) {
                    try {
                        this.f372e.m85a(System.currentTimeMillis() - ResHelper.parseLong(String.valueOf(hashMap.get("timestamp"))));
                    } catch (Throwable th) {
                        MobLog.getInstance().m63i(th);
                    }
                }
                if ("1".equals(String.valueOf(hashMap.get(VoiceRecognitionService.NLU_ENABLE)))) {
                    this.f372e.m83a(true);
                } else {
                    this.f372e.m83a(false);
                }
                Object obj2 = hashMap.get("upconf");
                if (obj2 != null && (obj2 instanceof HashMap)) {
                    HashMap hashMap2 = (HashMap) obj2;
                    String valueOf = String.valueOf(hashMap2.get("crash"));
                    String valueOf2 = String.valueOf(hashMap2.get("sdkerr"));
                    String valueOf3 = String.valueOf(hashMap2.get("apperr"));
                    this.f372e.m86a(Integer.parseInt(valueOf));
                    this.f372e.m81b(Integer.parseInt(valueOf2));
                    this.f372e.m79c(Integer.parseInt(valueOf3));
                }
                if (hashMap.containsKey("requesthost") && hashMap.containsKey("requestport")) {
                    String valueOf4 = String.valueOf(hashMap.get("requesthost"));
                    String valueOf5 = String.valueOf(hashMap.get("requestport"));
                    if (!TextUtils.isEmpty(valueOf4) && !TextUtils.isEmpty(valueOf5)) {
                        f369b = "http://" + valueOf4 + ":" + valueOf5;
                    }
                }
                Object obj3 = hashMap.get("filter");
                if (obj3 != null && (obj3 instanceof ArrayList)) {
                    ArrayList arrayList2 = (ArrayList) obj3;
                    if (arrayList2.size() > 0) {
                        HashMap hashMap3 = new HashMap();
                        hashMap3.put("fakelist", arrayList2);
                        this.f372e.m84a(new Hashon().fromHashMap(hashMap3));
                    }
                }
            }
        } catch (Throwable th2) {
            MobLog.getInstance().m69d(th2);
        }
    }

    /* renamed from: c */
    private String m90c() {
        return f369b + "/errlog";
    }

    /* renamed from: a */
    private void m98a(int i, String str, String[] strArr) {
        try {
            if (this.f372e.m80c()) {
                if ("none".equals(DeviceHelper.getInstance(MobSDK.getContext()).getDetailNetworkTypeForStatic())) {
                    throw new IllegalStateException("network is disconnected!");
                }
                ArrayList<MessageModel> a = MessageUtils.m71a(strArr);
                for (int i2 = 0; i2 < a.size(); i2++) {
                    MessageModel eVar = a.get(i2);
                    HashMap<String, Object> c = m89c(i, str);
                    c.put("errmsg", eVar.f377a);
                    if (m95a(m96a(new Hashon().fromHashMap(c)), true)) {
                        MessageUtils.m72a(eVar.f378b);
                    }
                }
            }
        } catch (Throwable th) {
            MobLog.getInstance().m63i(th);
        }
    }

    /* renamed from: c */
    private HashMap<String, Object> m89c(int i, String str) throws Throwable {
        HashMap<String, Object> hashMap = new HashMap<>();
        DeviceHelper instance = DeviceHelper.getInstance(MobSDK.getContext());
        hashMap.put(Constant.EXTRA_KEY, MobSDK.getAppkey());
        hashMap.put("plat", Integer.valueOf(instance.getPlatformCode()));
        hashMap.put("sdk", str);
        hashMap.put("sdkver", Integer.valueOf(i));
        hashMap.put("appname", instance.getAppName());
        hashMap.put("apppkg", instance.getPackageName());
        hashMap.put("appver", String.valueOf(instance.getAppVersion()));
        hashMap.put("deviceid", instance.getDeviceKey());
        hashMap.put("model", instance.getModel());
        hashMap.put("mac", instance.getMacAddress());
        hashMap.put("udid", instance.getDeviceId());
        hashMap.put("sysver", String.valueOf(instance.getOSVersionInt()));
        hashMap.put("networktype", instance.getDetailNetworkTypeForStatic());
        return hashMap;
    }

    /* renamed from: a */
    private String m96a(String str) throws Throwable {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
        byte[] bArr = new byte[1024];
        while (true) {
            int read = byteArrayInputStream.read(bArr, 0, 1024);
            if (read != -1) {
                gZIPOutputStream.write(bArr, 0, read);
            } else {
                gZIPOutputStream.flush();
                gZIPOutputStream.close();
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();
                byteArrayInputStream.close();
                return Base64.encodeToString(byteArray, 2);
            }
        }
    }

    /* renamed from: a */
    private boolean m95a(String str, boolean z) throws Throwable {
        try {
            if ("none".equals(DeviceHelper.getInstance(MobSDK.getContext()).getDetailNetworkTypeForStatic())) {
                throw new IllegalStateException("network is disconnected!");
            }
            ArrayList<KVPair<String>> arrayList = new ArrayList<>();
            arrayList.add(new KVPair<>("m", str));
            NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
            networkTimeOut.readTimout = 10000;
            networkTimeOut.connectionTimeout = 10000;
            this.f371d.httpPost(m90c(), arrayList, (KVPair<String>) null, (ArrayList<KVPair<String>>) null, networkTimeOut);
            return true;
        } catch (Throwable th) {
            MobLog.getInstance().m63i(th);
            return false;
        }
    }
}
