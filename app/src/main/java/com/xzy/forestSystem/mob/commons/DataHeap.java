package  com.xzy.forestSystem.mob.commons;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import com.xzy.forestSystem.baidu.speech.easr.stat.SynthesizeResultDb;
import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.commons.authorize.DeviceAuthorizer;
import  com.xzy.forestSystem.mob.tools.MobHandlerThread;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.network.KVPair;
import  com.xzy.forestSystem.mob.tools.network.NetworkHelper;
import  com.xzy.forestSystem.mob.tools.utils.Data;
import  com.xzy.forestSystem.mob.tools.utils.DeviceHelper;
import  com.xzy.forestSystem.mob.tools.utils.FileLocker;
import  com.xzy.forestSystem.mob.tools.utils.Hashon;
import  com.xzy.forestSystem.mob.tools.utils.MobRSA;
import  com.xzy.forestSystem.mob.tools.utils.ResHelper;
import  com.xzy.forestSystem.mob.tools.utils.SQLiteHelper;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.zip.GZIPOutputStream;

/* renamed from:  com.xzy.forestSystem.mob.commons.b */
public class DataHeap implements Handler.Callback {

    /* renamed from: a */
    private static DataHeap f323a;

    /* renamed from: b */
    private Handler f324b;

    /* renamed from: c */
    private SQLiteHelper.SingleTableDB f325c;

    /* renamed from: d */
    private Hashon f326d = new Hashon();

    /* renamed from: e */
    private Random f327e = new Random();

    /* renamed from: a */
    public static synchronized DataHeap m205a() {
        DataHeap bVar;
        synchronized (DataHeap.class) {
            if (f323a == null) {
                f323a = new DataHeap();
            }
            bVar = f323a;
        }
        return bVar;
    }

    private DataHeap() {
        MobHandlerThread mobHandlerThread = new MobHandlerThread();
        mobHandlerThread.start();
        this.f324b = new Handler(mobHandlerThread.getLooper(), this);
        this.f325c = SQLiteHelper.getDatabase(Locks.m140a("comm/dbs/.dh").getAbsolutePath(), "DataHeap_1");
        this.f325c.addField(SynthesizeResultDb.KEY_TIME, "text", true);
        this.f325c.addField("data", "text", true);
        this.f324b.sendEmptyMessage(1);
    }

    /* renamed from: a */
    public synchronized void m204a(long j, HashMap<String, Object> hashMap) {
        Message message = new Message();
        message.what = 2;
        message.obj = new Object[]{Long.valueOf(j), hashMap};
        this.f324b.sendMessage(message);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 1:
                m199b();
                this.f324b.sendEmptyMessageDelayed(1, 10000);
                break;
            case 2:
                Object[] objArr = (Object[]) message.obj;
                long longValue = ((Long) ResHelper.forceCast(objArr[0], -1L)).longValue();
                if (longValue > 0) {
                    m198b(longValue, (HashMap) objArr[1]);
                    break;
                }
                break;
        }
        return false;
    }

    /* renamed from: b */
    private void m198b(final long j, final HashMap<String, Object> hashMap) {
        Locks.m141a(Locks.m140a("comm/locks/.dhlock"), new LockAction() { // from class:  com.xzy.forestSystem.mob.commons.b.1
            @Override //  com.xzy.forestSystem.mob.commons.LockAction
            public boolean run(FileLocker fileLocker) {
                try {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(SynthesizeResultDb.KEY_TIME, String.valueOf(j));
                    contentValues.put("data", Base64.encodeToString(Data.AES128Encode(Data.rawMD5(DeviceHelper.getInstance(MobSDK.getContext()).getManufacturer()), DataHeap.this.f326d.fromHashMap(hashMap).getBytes("utf-8")), 2));
                    SQLiteHelper.insert(DataHeap.this.f325c, contentValues);
                    return false;
                } catch (Throwable th) {
                    MobLog.getInstance().m57w(th);
                    return false;
                }
            }
        });
    }

    /* renamed from: b */
    private void m199b() {
        String networkType = DeviceHelper.getInstance(MobSDK.getContext()).getNetworkType();
        if (networkType != null && !"none".equals(networkType)) {
            Locks.m141a(Locks.m140a("comm/locks/.dhlock"), new LockAction() { // from class:  com.xzy.forestSystem.mob.commons.b.2
                @Override //  com.xzy.forestSystem.mob.commons.LockAction
                public boolean run(FileLocker fileLocker) {
                    ArrayList c = DataHeap.this.m194c();
                    if (c.size() <= 0 || !DataHeap.this.m200a(c)) {
                        return false;
                    }
                    DataHeap.this.m195b(c);
                    return false;
                }
            });
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: c */
    private ArrayList<String[]> m194c() {
        ArrayList<String[]> arrayList = new ArrayList<>();
        try {
            Cursor query = SQLiteHelper.query(this.f325c, new String[]{SynthesizeResultDb.KEY_TIME, "data"}, null, null, null);
            if (query != null) {
                if (query.moveToFirst()) {
                    long a = CommonConfig.m246a();
                    do {
                        String[] strArr = {query.getString(0), query.getString(1)};
                        long j = -1;
                        try {
                            j = Long.parseLong(strArr[0]);
                        } catch (Throwable th) {
                        }
                        if (j <= a) {
                            arrayList.add(strArr);
                        }
                    } while (query.moveToNext());
                }
                query.close();
            }
        } catch (Throwable th2) {
            MobLog.getInstance().m57w(th2);
        }
        return arrayList;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: a */
    private boolean m200a(ArrayList<String[]> arrayList) {
        try {
            NetworkHelper networkHelper = new NetworkHelper();
            ArrayList<MobProduct> products = MobProductCollector.getProducts();
            if (products.isEmpty()) {
                return false;
            }
            HashMap hashMap = new HashMap();
            DeviceHelper instance = DeviceHelper.getInstance(MobSDK.getContext());
            hashMap.put("plat", Integer.valueOf(instance.getPlatformCode()));
            hashMap.put("device", instance.getDeviceKey());
            hashMap.put("mac", instance.getMacAddress());
            hashMap.put("model", instance.getModel());
            hashMap.put("duid", DeviceAuthorizer.authorize(null));
            hashMap.put("imei", instance.getIMEI());
            hashMap.put("serialno", instance.getSerialno());
            hashMap.put("networktype", instance.getDetailNetworkTypeForStatic());
            ArrayList arrayList2 = new ArrayList();
            byte[] rawMD5 = Data.rawMD5(instance.getManufacturer());
            Iterator<String[]> it = arrayList.iterator();
            while (it.hasNext()) {
                try {
                    arrayList2.add(this.f326d.fromJson(new String(Data.AES128Decode(rawMD5, Base64.decode(it.next()[1], 2)), "utf-8").trim()));
                } catch (Throwable th) {
                    MobLog.getInstance().m57w(th);
                }
            }
            hashMap.put("datas", arrayList2);
            ArrayList<KVPair<String>> arrayList3 = new ArrayList<>();
            arrayList3.add(new KVPair<>("appkey", MobSDK.getAppkey()));
            arrayList3.add(new KVPair<>("m", m201a(this.f326d.fromHashMap(hashMap))));
            ArrayList<KVPair<String>> arrayList4 = new ArrayList<>();
            arrayList4.add(new KVPair<>("User-Identity", MobProductCollector.getUserIdentity(products)));
            NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
            networkTimeOut.readTimout = 30000;
            networkTimeOut.connectionTimeout = 30000;
            boolean equals = "200".equals(String.valueOf(this.f326d.fromJson(networkHelper.httpPost(m192d(), arrayList3, (KVPair<String>) null, arrayList4, networkTimeOut)).get("status")));
            if (equals) {
                return equals;
            }
            ProcessLevelSPDB.m128e(null);
            return equals;
        } catch (Throwable th2) {
            ProcessLevelSPDB.m128e(null);
            MobLog.getInstance().m57w(th2);
            return false;
        }
    }

    /* renamed from: a */
    private String m201a(String str) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeLong(this.f327e.nextLong());
        dataOutputStream.writeLong(this.f327e.nextLong());
        dataOutputStream.flush();
        dataOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        GZIPOutputStream gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream2);
        gZIPOutputStream.write(str.getBytes("utf-8"));
        gZIPOutputStream.flush();
        gZIPOutputStream.close();
        byte[] AES128Encode = Data.AES128Encode(byteArray, byteArrayOutputStream2.toByteArray());
        byte[] encode = new MobRSA(1024).encode(byteArray, new BigInteger("ceeef5035212dfe7c6a0acdc0ef35ce5b118aab916477037d7381f85c6b6176fcf57b1d1c3296af0bb1c483fe5e1eb0ce9eb2953b44e494ca60777a1b033cc07", 16), new BigInteger("191737288d17e660c4b61440d5d14228a0bf9854499f9d68d8274db55d6d954489371ecf314f26bec236e58fac7fffa9b27bcf923e1229c4080d49f7758739e5bd6014383ed2a75ce1be9b0ab22f283c5c5e11216c5658ba444212b6270d629f2d615b8dfdec8545fb7d4f935b0cc10b6948ab4fc1cb1dd496a8f94b51e888dd", 16));
        ByteArrayOutputStream byteArrayOutputStream3 = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream2 = new DataOutputStream(byteArrayOutputStream3);
        dataOutputStream2.writeInt(encode.length);
        dataOutputStream2.write(encode);
        dataOutputStream2.writeInt(AES128Encode.length);
        dataOutputStream2.write(AES128Encode);
        dataOutputStream2.flush();
        dataOutputStream2.close();
        return Base64.encodeToString(byteArrayOutputStream3.toByteArray(), 2);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: b */
    private void m195b(ArrayList<String[]> arrayList) {
        try {
            StringBuilder sb = new StringBuilder();
            Iterator<String[]> it = arrayList.iterator();
            while (it.hasNext()) {
                String[] next = it.next();
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append('\'').append(next[0]).append('\'');
            }
            SQLiteHelper.delete(this.f325c, "time in (" + sb.toString() + ")", null);
        } catch (Throwable th) {
            MobLog.getInstance().m57w(th);
        }
    }

    /* renamed from: d */
    private static String m192d() {
        String str = null;
        try {
            str = ProcessLevelSPDB.m127f();
        } catch (Throwable th) {
            MobLog.getInstance().m57w(th);
        }
        return TextUtils.isEmpty(str) ? "http://c.data.mob.com/v2/cdata" : str;
    }
}
