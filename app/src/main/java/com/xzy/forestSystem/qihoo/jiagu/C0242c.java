package  com.xzy.forestSystem.qihoo.jiagu;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import  com.xzy.forestSystem.qihoo.bugreport.Protocol;
import  com.xzy.forestSystem.qihoo.bugreport.javacrash.ReportField;
import  com.xzy.forestSystem.qihoo.jiagutracker.C0246Config;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.EnumMap;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* renamed from:  com.xzy.forestSystem.qihoo.jiagu.c */
public class C0242c {
    /* renamed from: a */
    public static JSONObject m35a(EnumMap<ReportField, String> enumMap, String str) {
        if (str == null) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (!jSONObject.has("mpv")) {
                try {
                    jSONObject.accumulate("mpv", 1);
                } catch (JSONException e) {
                }
            }
            for (ReportField reportField : enumMap.keySet()) {
                try {
                    if (reportField.name().equals(ReportField.t.name()) || reportField.name().equals(ReportField.cpv.name()) || reportField.name().equals(ReportField.jc.name())) {
                        String str2 = enumMap.get(reportField);
                        jSONObject.accumulate(reportField.name(), Pattern.compile("[0-9]*").matcher(str2).matches() ? Integer.valueOf(Integer.parseInt(str2)) : "");
                    } else {
                        jSONObject.accumulate(reportField.name(), enumMap.get(reportField));
                    }
                } catch (JSONException e2) {
                }
            }
            JSONArray a = m37a();
            if (a != null) {
                try {
                    jSONObject.accumulate(ReportField.rt.name(), a);
                } catch (JSONException e3) {
                }
            }
            return jSONObject;
        } catch (JSONException e4) {
            return null;
        }
    }

    /* renamed from: a */
    static JSONArray m37a() {
        JSONArray jSONArray = new JSONArray();
        try {
            for (String str : (String[]) Class.forName(" com.xzy.forestSystem.qihoo.jiagutracker.TrackDataManager").getDeclaredMethod("getTrackData", new Class[0]).invoke(null, new Object[0])) {
                String[] split = str.split(C0246Config.SEPARATOR_CHAR);
                if (Protocol.TrackerDataField.values().length != split.length) {
                    return null;
                }
                JSONObject jSONObject = new JSONObject();
                jSONObject.accumulate(Protocol.TrackerDataField.cn.name(), split[0]);
                jSONObject.accumulate(Protocol.TrackerDataField.mn.name(), split[1]);
                jSONObject.accumulate(Protocol.TrackerDataField.vi.name(), split[2]);
                jSONObject.accumulate(Protocol.TrackerDataField.vt.name(), split[3]);
                jSONObject.accumulate(Protocol.TrackerDataField.st.name(), split[4]);
                jSONArray.put(jSONObject);
            }
        } catch (Throwable th) {
        }
        return jSONArray;
    }

    /* renamed from: a */
    public static String m34a(byte[] bArr) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(bArr);
            byte[] digest = instance.digest();
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                if ((b & 255) < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(b & 255));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /* renamed from: a */
    public static boolean m36a(Context context, String str) {
        if (context == null || str.isEmpty()) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            return m33b(context, str);
        }
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return false;
        }
        try {
            if (packageManager.checkPermission(str, context.getPackageName()) == 0) {
                return true;
            }
            return false;
        } catch (RuntimeException e) {
            return false;
        }
    }

    /* renamed from: b */
    public static boolean m33b(Context context, String str) {
        boolean z = true;
        if (context == null || str.isEmpty()) {
            return false;
        }
        try {
            int i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.targetSdkVersion;
            if (i >= 23) {
                if (ContextCompat.checkSelfPermission(context, str) != 0) {
                    z = false;
                }
                new StringBuilder("targetSdkVersion01:").append(i);
                return z;
            }
            if (PermissionChecker.checkSelfPermission(context, str) != 0) {
                z = false;
            }
            new StringBuilder("targetSdkVersion02:").append(i);
            return z;
        } catch (Throwable th) {
            return false;
        }
    }

    /* renamed from: c */
    public static boolean m32c(Context context, String str) {
        try {
            ZipFile zipFile = new ZipFile(context.getApplicationInfo().sourceDir);
            if (zipFile.getEntry("META-INF/" + str) != null) {
                zipFile.close();
                return true;
            }
            try {
                zipFile.close();
                return false;
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e2) {
            return false;
        }
    }
}
