package  com.xzy.forestSystem.mob.commons.filesys;

import android.text.TextUtils;
import com.xzy.forestSystem.baidu.speech.easr.stat.SynthesizeResultDb;
import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.commons.MobProduct;
import  com.xzy.forestSystem.mob.commons.authorize.DeviceAuthorizer;
import  com.xzy.forestSystem.mob.tools.network.KVPair;
import  com.xzy.forestSystem.mob.tools.network.NetworkHelper;
import  com.xzy.forestSystem.mob.tools.proguard.PublicMemberKeeper;
import  com.xzy.forestSystem.mob.tools.utils.Data;
import  com.xzy.forestSystem.mob.tools.utils.Hashon;
import  com.xzy.forestSystem.gogisapi.Common.Constant;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FileUploader implements PublicMemberKeeper {

    /* renamed from: a */
    private static String f363a = "http://up.sdk.mob.com";

    public static void setUploadServer(String str) {
        if (str.endsWith(FileSelector_Dialog.sRoot)) {
            str = str.substring(0, str.length() - 1);
        }
        f363a = str;
    }

    public static UploadedImage uploadImage(MobProduct mobProduct, String str) throws Throwable {
        return uploadImage(mobProduct, str, false, new int[0]);
    }

    /* renamed from: a */
    private static String m117a() {
        return f363a + "/image";
    }

    public static UploadedImage uploadImage(MobProduct mobProduct, String str, boolean z, int... iArr) throws Throwable {
        String str2 = null;
        if (iArr != null && iArr.length > 0) {
            String str3 = "";
            for (int i = 0; i < iArr.length; i++) {
                str3 = str3 + "," + iArr[i];
            }
            if (str3.length() > 0) {
                str2 = str3.substring(1);
            }
        }
        return new UploadedImage(m112a(mobProduct, str, z, str2, m117a()));
    }

    public static UploadedAvatar uploadAvatar(MobProduct mobProduct, String str) throws Throwable {
        return uploadAvatar(mobProduct, str, false, new int[0]);
    }

    /* renamed from: b */
    private static String m111b() {
        return f363a + "/avatar";
    }

    public static UploadedAvatar uploadAvatar(MobProduct mobProduct, String str, boolean z, int... iArr) throws Throwable {
        String str2 = null;
        if (iArr != null && iArr.length > 0) {
            String str3 = "";
            for (int i = 0; i < iArr.length; i++) {
                str3 = str3 + "," + iArr[i];
            }
            if (str3.length() > 0) {
                str2 = str3.substring(1);
            }
        }
        return new UploadedAvatar(m112a(mobProduct, str, z, str2, m111b()));
    }

    /* renamed from: c */
    private static String m110c() {
        return f363a + "/video";
    }

    public static UploadedVideo uploadVideo(MobProduct mobProduct, String str, boolean z) throws Throwable {
        HashMap hashMap = new HashMap();
        hashMap.put("sm", Integer.valueOf(z ? 2 : 1));
        return new UploadedVideo(m113a(mobProduct, str, hashMap, m110c(), 209715200));
    }

    /* renamed from: d */
    private static String m109d() {
        return f363a + "/file";
    }

    public static UploadedFile uploadFile(MobProduct mobProduct, String str, boolean z) throws Throwable {
        HashMap hashMap = new HashMap();
        hashMap.put("sm", Integer.valueOf(z ? 2 : 1));
        return new UploadedFile(m113a(mobProduct, str, hashMap, m109d(), 52428800));
    }

    /* renamed from: a */
    private static Throwable m116a(long j, long j2) {
        return new Throwable("{\"status\": ,\"error\":\"max size: " + j2 + ", file size: " + j + "\"}");
    }

    /* renamed from: a */
    private static HashMap<String, Object> m112a(MobProduct mobProduct, String str, boolean z, String str2, String str3) throws Throwable {
        HashMap hashMap = new HashMap();
        hashMap.put("sm", Integer.valueOf(z ? 2 : 1));
        if (!TextUtils.isEmpty(str2)) {
            hashMap.put("zoomScaleWidths", str2);
        }
        return m113a(mobProduct, str, hashMap, str3, 10485760);
    }

    /* renamed from: a */
    private static HashMap<String, Object> m113a(MobProduct mobProduct, String str, HashMap<String, Object> hashMap, String str2, long j) throws Throwable {
        File file = new File(str);
        if (file.length() > j) {
            throw m116a(file.length(), j);
        }
        NetworkHelper networkHelper = new NetworkHelper();
        ArrayList<KVPair<String>> a = m114a(mobProduct, m115a(mobProduct));
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        for (String str3 : hashMap.keySet()) {
            arrayList.add(new KVPair<>(str3, String.valueOf(hashMap.get(str3))));
        }
        KVPair<String> kVPair = new KVPair<>("file", str);
        NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
        networkTimeOut.readTimout = 30000;
        networkTimeOut.connectionTimeout = 5000;
        String httpPost = networkHelper.httpPost(str2, arrayList, kVPair, a, networkTimeOut);
        HashMap fromJson = new Hashon().fromJson(httpPost);
        if (fromJson != null && "200".equals(String.valueOf(fromJson.get("status")))) {
            return (HashMap) fromJson.get("res");
        }
        throw new Throwable(httpPost);
    }

    /* renamed from: e */
    private static String m108e() {
        return f363a + "/getToken";
    }

    /* renamed from: a */
    private static String m115a(MobProduct mobProduct) throws Throwable {
        NetworkHelper networkHelper = new NetworkHelper();
        ArrayList<KVPair<String>> a = m114a(mobProduct, "1234567890abcdeffedcba0987654321");
        NetworkHelper.NetworkTimeOut networkTimeOut = new NetworkHelper.NetworkTimeOut();
        networkTimeOut.readTimout = 30000;
        networkTimeOut.connectionTimeout = 5000;
        String httpPost = networkHelper.httpPost(m108e(), (ArrayList<KVPair<String>>) null, (KVPair<String>) null, a, networkTimeOut);
        HashMap fromJson = new Hashon().fromJson(httpPost);
        if (fromJson == null || !"200".equals(String.valueOf(fromJson.get("status")))) {
            throw new Throwable(httpPost);
        }
        try {
            return (String) ((HashMap) fromJson.get("res")).get("token");
        } catch (Throwable th) {
            throw new Throwable(httpPost, th);
        }
    }

    /* renamed from: a */
    private static ArrayList<KVPair<String>> m114a(MobProduct mobProduct, String str) {
        ArrayList<KVPair<String>> arrayList = new ArrayList<>();
        arrayList.add(new KVPair<>("sign", Data.MD5(str + MobSDK.getAppSecret())));
        arrayList.add(new KVPair<>(Constant.EXTRA_KEY, MobSDK.getAppkey()));
        arrayList.add(new KVPair<>("token", str));
        arrayList.add(new KVPair<>("product", mobProduct.getProductTag()));
        arrayList.add(new KVPair<>("cliid", DeviceAuthorizer.authorize(mobProduct)));
        return arrayList;
    }

    public static class UploadedFile implements PublicMemberKeeper {
        public final String url;

        public UploadedFile(HashMap<String, Object> hashMap) {
            if (hashMap.containsKey("src")) {
                this.url = (String) hashMap.get("src");
            } else {
                this.url = null;
            }
        }
    }

    public static class UploadedImage extends UploadedFile {
        public final HashMap<String, String> zoomList;

        public UploadedImage(HashMap<String, Object> hashMap) {
            super(hashMap);
            HashMap<String, String> hashMap2;
            try {
                hashMap2 = (HashMap) hashMap.get("list");
            } catch (Throwable th) {
                hashMap2 = null;
            }
            this.zoomList = hashMap2;
        }
    }

    public static class UploadedVideo extends UploadedFile {
        public final int durationUSec;

        public UploadedVideo(HashMap<String, Object> hashMap) {
            super(hashMap);
            int i = -1;
            try {
                i = Integer.parseInt(String.valueOf(hashMap.get(SynthesizeResultDb.KEY_TIME)));
            } catch (Throwable th) {
            }
            this.durationUSec = i;
        }
    }

    public static class UploadedAvatar extends UploadedImage {
        public UploadedAvatar(HashMap<String, Object> hashMap) {
            super(hashMap);
        }
    }
}
