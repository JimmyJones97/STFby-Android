package  com.xzy.forestSystem.mob.commons.clt;

import android.os.Build;
import com.xzy.forestSystem.baidu.speech.easr.easrNativeJni;
import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.commons.CommonConfig;
import  com.xzy.forestSystem.mob.commons.DataHeap;
import  com.xzy.forestSystem.mob.commons.DeviceLevelTags;
import  com.xzy.forestSystem.mob.commons.LockAction;
import  com.xzy.forestSystem.mob.commons.Locks;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.proguard.PublicMemberKeeper;
import  com.xzy.forestSystem.mob.tools.utils.DeviceHelper;
import  com.xzy.forestSystem.mob.tools.utils.FileLocker;
import  com.xzy.forestSystem.mob.tools.utils.MobLooper;
import  com.xzy.forestSystem.mob.tools.utils.ResHelper;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class RtClt implements PublicMemberKeeper {

    /* renamed from: a */
    private static final String f349a = (Build.VERSION.SDK_INT >= 16 ? "^u\\d+_a\\d+" : "^app_\\d+");

    /* renamed from: b */
    private static RtClt f350b;

    public static synchronized void startCollector() {
        synchronized (RtClt.class) {
            if (f350b == null) {
                f350b = new RtClt();
                f350b.m157a();
            }
        }
    }

    private RtClt() {
    }

    /* renamed from: a */
    private void m157a() {
        Thread r0 = new Thread() { // from class:  com.xzy.forestSystem.mob.commons.clt.RtClt.1
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                Locks.m141a(Locks.m140a("comm/locks/.rc_lock"), new LockAction() { // from class:  com.xzy.forestSystem.mob.commons.clt.RtClt.1.1
                    @Override //  com.xzy.forestSystem.mob.commons.LockAction
                    public boolean run(FileLocker fileLocker) {
                        if (DeviceLevelTags.m191a("comm/tags/.rcTag")) {
                            return false;
                        }
                        RtClt.this.m147b();
                        return true;
                    }
                });
            }
        };
        r0.setPriority(1);
        r0.start();
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: b */
    private void m147b() {
        Process process;
        Throwable th;
        OutputStream outputStream = null;
        try {
            final File a = Locks.m140a("comm/dbs/.plst");
            final String absolutePath = a.getAbsolutePath();
            long c = m144c();
            DeviceLevelTags.m190b("comm/tags/.rcTag");
            try {
                process = Runtime.getRuntime().exec("sh");
                try {
                    outputStream = process.getOutputStream();
                } catch (Throwable th2) {
                    th = th2;
                    MobLog.getInstance().m57w(th);
                    DeviceLevelTags.m189c("comm/tags/.rcTag");
                    final HashMap hashMap = new HashMap();
                    hashMap.put("nextUploadTime", Long.valueOf(c));
                    hashMap.put("p", process);
                    hashMap.put("os", outputStream);
                    hashMap.put("firstLog", true);
                    new MobLooper(MobSDK.getContext()).start(new Runnable() { // from class:  com.xzy.forestSystem.mob.commons.clt.RtClt.2
                        @Override // java.lang.Runnable
                        public void run() {
                            String str;
                            long j;
                            try {
                                if (CommonConfig.m243b()) {
                                    if (!a.exists()) {
                                        a.createNewFile();
                                    }
                                    long a2 = CommonConfig.m246a();
                                    OutputStream outputStream2 = (OutputStream) hashMap.get("os");
                                    outputStream2.write(("top -d 0 -n 1 | grep -E -v 'root|shell|system' >> " + absolutePath + " && echo \"======================\" >> " + absolutePath + "\n").getBytes("ascii"));
                                    if (String.valueOf(hashMap.get("firstLog")).equals("true")) {
                                        str = "echo \"" + a2 + "_0\" >> " + absolutePath + "\n";
                                        hashMap.put("firstLog", false);
                                    } else {
                                        str = "echo \"" + a2 + "_" + CommonConfig.m241c() + "\" >> " + absolutePath + "\n";
                                    }
                                    outputStream2.write(str.getBytes("ascii"));
                                    outputStream2.flush();
                                    try {
                                        j = Long.parseLong(String.valueOf(hashMap.get("nextUploadTime")));
                                    } catch (Throwable th3) {
                                        j = 0;
                                    }
                                    if (a2 >= j) {
                                        outputStream2.write("exit\n".getBytes("ascii"));
                                        outputStream2.flush();
                                        outputStream2.close();
                                        Process process2 = (Process) hashMap.get("p");
                                        process2.waitFor();
                                        process2.destroy();
                                        if (RtClt.this.m154a(absolutePath)) {
                                            long d = RtClt.this.m143d();
                                            if (d > 0) {
                                                hashMap.put("nextUploadTime", Long.valueOf(d));
                                            }
                                            hashMap.put("firstLog", true);
                                        }
                                        DeviceLevelTags.m190b("comm/tags/.rcTag");
                                        try {
                                            Process exec = Runtime.getRuntime().exec("sh");
                                            hashMap.put("p", exec);
                                            hashMap.put("os", exec.getOutputStream());
                                        } catch (Throwable th4) {
                                            MobLog.getInstance().m57w(th4);
                                        }
                                        DeviceLevelTags.m189c("comm/tags/.rcTag");
                                    }
                                }
                            } catch (Throwable th5) {
                                MobLog.getInstance().m69d(th5);
                            }
                        }
                    }, (long) (CommonConfig.m241c() * easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX));
                }
            } catch (Throwable th3) {
                th = th3;
                process = null;
                MobLog.getInstance().m57w(th);
                DeviceLevelTags.m189c("comm/tags/.rcTag");
                final HashMap hashMap = new HashMap();
                hashMap.put("nextUploadTime", Long.valueOf(c));
                hashMap.put("p", process);
                hashMap.put("os", outputStream);
                hashMap.put("firstLog", true);
                new MobLooper(MobSDK.getContext()).start(new Runnable() { // from class:  com.xzy.forestSystem.mob.commons.clt.RtClt.2
                    @Override // java.lang.Runnable
                    public void run() {
                        String str;
                        long j;
                        try {
                            if (CommonConfig.m243b()) {
                                if (!a.exists()) {
                                    a.createNewFile();
                                }
                                long a2 = CommonConfig.m246a();
                                OutputStream outputStream2 = (OutputStream) hashMap.get("os");
                                outputStream2.write(("top -d 0 -n 1 | grep -E -v 'root|shell|system' >> " + absolutePath + " && echo \"======================\" >> " + absolutePath + "\n").getBytes("ascii"));
                                if (String.valueOf(hashMap.get("firstLog")).equals("true")) {
                                    str = "echo \"" + a2 + "_0\" >> " + absolutePath + "\n";
                                    hashMap.put("firstLog", false);
                                } else {
                                    str = "echo \"" + a2 + "_" + CommonConfig.m241c() + "\" >> " + absolutePath + "\n";
                                }
                                outputStream2.write(str.getBytes("ascii"));
                                outputStream2.flush();
                                try {
                                    j = Long.parseLong(String.valueOf(hashMap.get("nextUploadTime")));
                                } catch (Throwable th3) {
                                    j = 0;
                                }
                                if (a2 >= j) {
                                    outputStream2.write("exit\n".getBytes("ascii"));
                                    outputStream2.flush();
                                    outputStream2.close();
                                    Process process2 = (Process) hashMap.get("p");
                                    process2.waitFor();
                                    process2.destroy();
                                    if (RtClt.this.m154a(absolutePath)) {
                                        long d = RtClt.this.m143d();
                                        if (d > 0) {
                                            hashMap.put("nextUploadTime", Long.valueOf(d));
                                        }
                                        hashMap.put("firstLog", true);
                                    }
                                    DeviceLevelTags.m190b("comm/tags/.rcTag");
                                    try {
                                        Process exec = Runtime.getRuntime().exec("sh");
                                        hashMap.put("p", exec);
                                        hashMap.put("os", exec.getOutputStream());
                                    } catch (Throwable th4) {
                                        MobLog.getInstance().m57w(th4);
                                    }
                                    DeviceLevelTags.m189c("comm/tags/.rcTag");
                                }
                            }
                        } catch (Throwable th5) {
                            MobLog.getInstance().m69d(th5);
                        }
                    }
                }, (long) (CommonConfig.m241c() * easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX));
            }
            DeviceLevelTags.m189c("comm/tags/.rcTag");
            final HashMap hashMap = new HashMap();
            hashMap.put("nextUploadTime", Long.valueOf(c));
            hashMap.put("p", process);
            hashMap.put("os", outputStream);
            hashMap.put("firstLog", true);
            new MobLooper(MobSDK.getContext()).start(new Runnable() { // from class:  com.xzy.forestSystem.mob.commons.clt.RtClt.2
                @Override // java.lang.Runnable
                public void run() {
                    String str;
                    long j;
                    try {
                        if (CommonConfig.m243b()) {
                            if (!a.exists()) {
                                a.createNewFile();
                            }
                            long a2 = CommonConfig.m246a();
                            OutputStream outputStream2 = (OutputStream) hashMap.get("os");
                            outputStream2.write(("top -d 0 -n 1 | grep -E -v 'root|shell|system' >> " + absolutePath + " && echo \"======================\" >> " + absolutePath + "\n").getBytes("ascii"));
                            if (String.valueOf(hashMap.get("firstLog")).equals("true")) {
                                str = "echo \"" + a2 + "_0\" >> " + absolutePath + "\n";
                                hashMap.put("firstLog", false);
                            } else {
                                str = "echo \"" + a2 + "_" + CommonConfig.m241c() + "\" >> " + absolutePath + "\n";
                            }
                            outputStream2.write(str.getBytes("ascii"));
                            outputStream2.flush();
                            try {
                                j = Long.parseLong(String.valueOf(hashMap.get("nextUploadTime")));
                            } catch (Throwable th3) {
                                j = 0;
                            }
                            if (a2 >= j) {
                                outputStream2.write("exit\n".getBytes("ascii"));
                                outputStream2.flush();
                                outputStream2.close();
                                Process process2 = (Process) hashMap.get("p");
                                process2.waitFor();
                                process2.destroy();
                                if (RtClt.this.m154a(absolutePath)) {
                                    long d = RtClt.this.m143d();
                                    if (d > 0) {
                                        hashMap.put("nextUploadTime", Long.valueOf(d));
                                    }
                                    hashMap.put("firstLog", true);
                                }
                                DeviceLevelTags.m190b("comm/tags/.rcTag");
                                try {
                                    Process exec = Runtime.getRuntime().exec("sh");
                                    hashMap.put("p", exec);
                                    hashMap.put("os", exec.getOutputStream());
                                } catch (Throwable th4) {
                                    MobLog.getInstance().m57w(th4);
                                }
                                DeviceLevelTags.m189c("comm/tags/.rcTag");
                            }
                        }
                    } catch (Throwable th5) {
                        MobLog.getInstance().m69d(th5);
                    }
                }
            }, (long) (CommonConfig.m241c() * easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX));
        } catch (Throwable th4) {
        }
    }

    /* renamed from: c */
    private long m144c() {
        long a = CommonConfig.m246a();
        try {
            File a2 = Locks.m140a("comm/dbs/.nulplt");
            if (a2.exists()) {
                DataInputStream dataInputStream = new DataInputStream(new FileInputStream(a2));
                long readLong = dataInputStream.readLong();
                dataInputStream.close();
                return readLong;
            }
            a2.createNewFile();
            m143d();
            return CommonConfig.m228p() + a;
        } catch (Throwable th) {
            MobLog.getInstance().m69d(th);
            return CommonConfig.m228p() + a;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: d */
    private long m143d() {
        long a = CommonConfig.m246a() + CommonConfig.m228p();
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(Locks.m140a("comm/dbs/.nulplt")));
            dataOutputStream.writeLong(a);
            dataOutputStream.flush();
            dataOutputStream.close();
            return a;
        } catch (Throwable th) {
            MobLog.getInstance().m69d(th);
            return 0;
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    /* renamed from: a */
    private boolean m154a(String str) {
        ArrayList<ArrayList<HashMap<String, String>>> arrayList = new ArrayList<>();
        ArrayList<long[]> arrayList2 = new ArrayList<>();
        m153a(str, arrayList, arrayList2);
        m150a(m148a(m149a(m151a(arrayList), arrayList), arrayList2), arrayList2);
        return m145b(str);
    }

    /* renamed from: a */
    private void m153a(String str, ArrayList<ArrayList<HashMap<String, String>>> arrayList, ArrayList<long[]> arrayList2) {
        try {
            HashMap<String, String[]> e = m142e();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(str), "utf-8"));
            String readLine = bufferedReader.readLine();
            for (int i = 0; i < 7; i++) {
                readLine = bufferedReader.readLine();
            }
            ArrayList<HashMap<String, String>> arrayList3 = new ArrayList<>();
            while (readLine != null) {
                if ("======================".equals(readLine)) {
                    try {
                        String[] split = bufferedReader.readLine().split("_");
                        long[] jArr = {Long.valueOf(split[0]).longValue(), Long.valueOf(split[1]).longValue()};
                        arrayList.add(arrayList3);
                        arrayList2.add(jArr);
                    } catch (Throwable th) {
                    }
                    arrayList3 = new ArrayList<>();
                    for (int i2 = 0; i2 < 7; i2++) {
                        bufferedReader.readLine();
                    }
                } else if (readLine.length() > 0) {
                    m152a(readLine, e, arrayList3);
                }
                readLine = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (Throwable th2) {
            MobLog.getInstance().m69d(th2);
        }
    }

    /* renamed from: a */
    private void m152a(String str, HashMap<String, String[]> hashMap, ArrayList<HashMap<String, String>> arrayList) {
        String[] strArr;
        String[] split = str.replaceAll(" +", " ").split(" ");
        if (split != null && split.length >= 10) {
            String str2 = split[split.length - 1];
            if (split[split.length - 2].matches(f349a) && !"top".equals(str2) && (strArr = hashMap.get(str2)) != null) {
                HashMap<String, String> hashMap2 = new HashMap<>();
                hashMap2.put("pkg", str2);
                hashMap2.put("name", strArr[0]);
                hashMap2.put("version", strArr[1]);
                hashMap2.put("pcy", split[split.length - 3]);
                arrayList.add(hashMap2);
            }
        }
    }

    /* renamed from: e */
    private HashMap<String, String[]> m142e() {
        ArrayList<HashMap<String, String>> arrayList;
        try {
            arrayList = DeviceHelper.getInstance(MobSDK.getContext()).getInstalledApp(false);
        } catch (Throwable th) {
            MobLog.getInstance().m57w(th);
            arrayList = new ArrayList<>();
        }
        HashMap<String, String[]> hashMap = new HashMap<>();
        Iterator<HashMap<String, String>> it = arrayList.iterator();
        while (it.hasNext()) {
            HashMap<String, String> next = it.next();
            hashMap.put(next.get("pkg"), new String[]{next.get("name"), next.get("version")});
        }
        return hashMap;
    }

    /* renamed from: a */
    private HashMap<String, Integer> m151a(ArrayList<ArrayList<HashMap<String, String>>> arrayList) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        Iterator<ArrayList<HashMap<String, String>>> it = arrayList.iterator();
        int i = 0;
        while (it.hasNext()) {
            Iterator<HashMap<String, String>> it2 = it.next().iterator();
            int i2 = i;
            while (it2.hasNext()) {
                HashMap<String, String> next = it2.next();
                String str = next.get("pkg") + ":" + next.get("version");
                if (!hashMap.containsKey(str)) {
                    hashMap.put(str, Integer.valueOf(i2));
                    i2++;
                }
            }
            i = i2;
        }
        return hashMap;
    }

    /* renamed from: a */
    private HashMap<String, String>[][] m149a(HashMap<String, Integer> hashMap, ArrayList<ArrayList<HashMap<String, String>>> arrayList) {
        HashMap<String, String>[][] hashMapArr = (HashMap[][]) Array.newInstance(HashMap.class, hashMap.size(), arrayList.size());
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            ArrayList<HashMap<String, String>> arrayList2 = arrayList.get(i);
            int size2 = arrayList2.size();
            for (int i2 = 0; i2 < size2; i2++) {
                HashMap<String, String> hashMap2 = arrayList2.get(i2);
                hashMapArr[hashMap.get(hashMap2.get("pkg") + ":" + hashMap2.get("version")).intValue()][i] = hashMap2;
            }
        }
        return hashMapArr;
    }

    /* renamed from: a */
    private ArrayList<HashMap<String, Object>> m148a(HashMap<String, String>[][] hashMapArr, ArrayList<long[]> arrayList) {
        ArrayList<HashMap<String, Object>> arrayList2 = new ArrayList<>(hashMapArr.length);
        for (HashMap<String, String>[] hashMapArr2 : hashMapArr) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("runtimes", 0L);
            hashMap.put("fg", 0);
            hashMap.put("bg", 0);
            hashMap.put("empty", 0);
            arrayList2.add(hashMap);
            int length = hashMapArr2.length - 1;
            while (length >= 0) {
                if (hashMapArr2[length] != null) {
                    hashMap.put("runtimes", Long.valueOf((length == 0 ? 0 : arrayList.get(length)[1]) + ((Long) ResHelper.forceCast(hashMap.get("runtimes"), 0L)).longValue()));
                    if ("fg".equals(hashMapArr2[length].get("pcy"))) {
                        hashMap.put("fg", Integer.valueOf(((Integer) ResHelper.forceCast(hashMap.get("fg"), 0)).intValue() + 1));
                    } else if ("bg".equals(hashMapArr2[length].get("pcy"))) {
                        hashMap.put("bg", Integer.valueOf(((Integer) ResHelper.forceCast(hashMap.get("bg"), 0)).intValue() + 1));
                    } else {
                        hashMap.put("empty", Integer.valueOf(((Integer) ResHelper.forceCast(hashMap.get("empty"), 0)).intValue() + 1));
                    }
                    hashMap.put("pkg", hashMapArr2[length].get("pkg"));
                    hashMap.put("name", hashMapArr2[length].get("name"));
                    hashMap.put("version", hashMapArr2[length].get("version"));
                }
                length--;
            }
        }
        return arrayList2;
    }

    /* renamed from: a */
    private void m150a(ArrayList<HashMap<String, Object>> arrayList, ArrayList<long[]> arrayList2) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("type", "APP_RUNTIMES");
        hashMap.put("list", arrayList);
        hashMap.put("datatime", Long.valueOf(CommonConfig.m246a()));
        hashMap.put("recordat", Long.valueOf(arrayList2.get(0)[0]));
        long j = 0;
        for (int i = 1; i < arrayList2.size(); i++) {
            j += arrayList2.get(i)[1];
        }
        hashMap.put("sdk_runtime_len", Long.valueOf(j));
        hashMap.put("top_count", Integer.valueOf(arrayList2.size()));
        DataHeap.m205a().m204a(CommonConfig.m246a(), hashMap);
    }

    /* renamed from: b */
    private boolean m145b(String str) {
        try {
            File file = new File(str);
            file.delete();
            file.createNewFile();
            return true;
        } catch (Throwable th) {
            MobLog.getInstance().m69d(th);
            return false;
        }
    }
}
