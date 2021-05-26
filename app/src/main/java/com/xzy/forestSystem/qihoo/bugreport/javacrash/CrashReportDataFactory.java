package  com.xzy.forestSystem.qihoo.bugreport.javacrash;

import android.content.Context;
import android.os.Process;
import  com.xzy.forestSystem.qihoo.jiagu.C0240a;
import  com.xzy.forestSystem.qihoo.jiagu.C0241b;
import  com.xzy.forestSystem.qihoo.jiagu.C0242c;
import  com.xzy.forestSystem.qihoo.jiagutracker.C0246Config;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CrashReportDataFactory {

    /* renamed from: a */
    final Calendar f393a;

    public native String interface9();

    public CrashReportDataFactory(Context context, Calendar calendar) {
        this.f393a = calendar;
    }

    /* renamed from: a */
    static String m53a(String str) {
        if (str == null) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        String[] split = str.split("\n");
        for (int i = 0; i < split.length; i++) {
            if (!split[i].contains(C0241b.f428b)) {
                stringBuffer.append(split[i]).append("\n");
            }
        }
        return stringBuffer.toString();
    }

    /* renamed from: a */
    static String m51a(String str, ArrayList<String> arrayList) {
        int i = 0;
        if (str == null || arrayList == null) {
            return null;
        }
        String[] split = str.split("\n");
        for (int i2 = 0; i2 < split.length - 1; i2++) {
            if (split[i2].replaceAll("\t", "").trim().startsWith("Caused by:")) {
                i = i2;
            } else {
                i = i;
            }
        }
        for (int i3 = i + 1; i3 < split.length; i3++) {
            String trim = split[i3].replaceAll("\t", "").trim();
            if (trim.startsWith("at")) {
                trim = trim.substring(2).trim();
            }
            if (arrayList.contains(trim)) {
                return trim;
            }
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r1.close();
     */
    /* renamed from: a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.util.HashMap<java.lang.String, java.lang.String> m54a() {
        /*
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>()
            int r1 = android.os.Process.myPid()
            java.lang.String r2 = "ed"
            java.lang.String r3 = java.lang.Integer.toString(r1)
            r0.put(r2, r3)
            java.io.File r2 = new java.io.File
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r4 = "/proc/"
            r3.<init>(r4)
            java.lang.StringBuilder r1 = r3.append(r1)
            java.lang.String r3 = "/status"
            java.lang.StringBuilder r1 = r1.append(r3)
            java.lang.String r1 = r1.toString()
            r2.<init>(r1)
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x0076 }
            r1.<init>(r2)     // Catch:{ FileNotFoundException -> 0x0076 }
            java.io.InputStreamReader r2 = new java.io.InputStreamReader
            r2.<init>(r1)
            java.io.BufferedReader r1 = new java.io.BufferedReader
            r1.<init>(r2)
        L_0x003b:
            java.lang.String r2 = r1.readLine()     // Catch:{ IOException -> 0x0066, all -> 0x006d }
            if (r2 == 0) goto L_0x0062
            java.lang.String r3 = "PPid:"
            boolean r3 = r2.startsWith(r3)     // Catch:{ IOException -> 0x0066, all -> 0x006d }
            if (r3 == 0) goto L_0x003b
            java.lang.String r3 = ":"
            int r3 = r2.indexOf(r3)     // Catch:{ IOException -> 0x0066, all -> 0x006d }
            int r3 = r3 + 1
            java.lang.String r2 = r2.substring(r3)     // Catch:{ IOException -> 0x0066, all -> 0x006d }
            java.lang.String r3 = "^\\s*"
            java.lang.String r4 = ""
            java.lang.String r2 = r2.replaceFirst(r3, r4)     // Catch:{ IOException -> 0x0066, all -> 0x006d }
            java.lang.String r3 = "epd"
            r0.put(r3, r2)     // Catch:{ IOException -> 0x0066, all -> 0x006d }
        L_0x0062:
            r1.close()     // Catch:{ IOException -> 0x0072 }
        L_0x0065:
            return r0
        L_0x0066:
            r2 = move-exception
            r1.close()     // Catch:{ IOException -> 0x006b }
            goto L_0x0065
        L_0x006b:
            r1 = move-exception
            goto L_0x0065
        L_0x006d:
            r0 = move-exception
            r1.close()     // Catch:{ IOException -> 0x0074 }
        L_0x0071:
            throw r0
        L_0x0072:
            r1 = move-exception
            goto L_0x0065
        L_0x0074:
            r1 = move-exception
            goto L_0x0071
        L_0x0076:
            r1 = move-exception
            goto L_0x0065
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.qihoo.bugreport.javacrash.CrashReportDataFactory.m54a():java.util.HashMap");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: b */
    public String m47b() {
        try {
            return interface9();
        } catch (Throwable th) {
            return null;
        }
    }

    /* renamed from: c */
    static String m46c() {
        int indexOf;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("/proc/" + Process.myPid() + "/cmdline"))));
            try {
                String readLine = bufferedReader.readLine();
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                }
                if (readLine != null && (indexOf = readLine.indexOf("\u0000")) >= 0) {
                    return readLine.substring(0, indexOf);
                }
                return null;
            } catch (IOException e2) {
                try {
                    bufferedReader.close();
                    return null;
                } catch (IOException e3) {
                    return null;
                }
            } catch (Throwable th) {
                try {
                    bufferedReader.close();
                } catch (IOException e4) {
                }
                throw th;
            }
        } catch (FileNotFoundException e5) {
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a */
    public String m52a(String str, String str2, ArrayList<String> arrayList) {
        StringBuffer stringBuffer = new StringBuffer();
        if (str != null) {
            stringBuffer.append(str);
        }
        if (str2 != null) {
            stringBuffer.append(str2);
        }
        List asList = Arrays.asList(C0240a.f426a);
        for (int i = 0; i < arrayList.size(); i++) {
            String str3 = arrayList.get(i);
            if (!m48a(asList, str3)) {
                stringBuffer.append(str3);
            }
        }
        return C0242c.m34a(stringBuffer.toString().getBytes());
    }

    /* renamed from: a */
    private static boolean m48a(List<String> list, String str) {
        for (int i = 0; i < list.size(); i++) {
            if (str.startsWith(list.get(i))) {
                return true;
            }
        }
        return false;
    }

    /* renamed from: a */
    static ArrayList<String> m50a(Throwable th) {
        ArrayList<String> arrayList = new ArrayList<>();
        while (th != null) {
            for (StackTraceElement stackTraceElement : th.getStackTrace()) {
                arrayList.add(stackTraceElement.toString());
            }
            th = th.getCause();
        }
        return arrayList;
    }

    /* renamed from: a */
    static String m49a(Calendar calendar) {
        return new SimpleDateFormat(C0246Config.DATE_TIME_FORMAT_STRING, Locale.ENGLISH).format(Long.valueOf(calendar.getTimeInMillis()));
    }
}
