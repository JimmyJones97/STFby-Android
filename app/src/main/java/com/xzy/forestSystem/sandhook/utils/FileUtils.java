package com.xzy.forestSystem.sandhook.utils;

import android.os.Build;
import android.system.Os;
import android.text.TextUtils;

import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import com.xzy.forestSystem.sandhook.HookLog;
import com.xzy.forestSystem.sandhook.SandHookConfig;

import java.io.File;
import java.io.IOException;

public class FileUtils {
    public static final boolean IS_USING_PROTECTED_STORAGE = (Build.VERSION.SDK_INT >= 24);

    public interface FileMode {
        public static final int MODE_755 = 493;
        public static final int MODE_IRGRP = 32;
        public static final int MODE_IROTH = 4;
        public static final int MODE_IRUSR = 256;
        public static final int MODE_ISGID = 1024;
        public static final int MODE_ISUID = 2048;
        public static final int MODE_ISVTX = 512;
        public static final int MODE_IWGRP = 16;
        public static final int MODE_IWOTH = 2;
        public static final int MODE_IWUSR = 128;
        public static final int MODE_IXGRP = 8;
        public static final int MODE_IXOTH = 1;
        public static final int MODE_IXUSR = 64;
    }

    public static void chmod(String str, int i) throws Exception {
        if (SandHookConfig.SDK_INT >= 21) {
            try {
                Os.chmod(str, i);
                return;
            } catch (Exception e) {
            }
        }
        String str2 = "chmod ";
        if (new File(str).isDirectory()) {
            str2 = str2 + " -R ";
        }
        Runtime.getRuntime().exec(str2 + String.format("%o", Integer.valueOf(i)) + " " + str).waitFor();
    }

    public static void delete(File file) throws IOException {
        File[] listFiles = file.listFiles();
        for (File file2 : listFiles) {
            if (file2.isDirectory()) {
                delete(file2);
            } else if (!file2.delete()) {
                throw new IOException();
            }
        }
        if (!file.delete()) {
            throw new IOException();
        }
    }

    public static String getDataPathPrefix() {
        return IS_USING_PROTECTED_STORAGE ? "/data/user_de/0/" : "/data/data/";
    }

    public static String getPackageName(String str) {
        if (TextUtils.isEmpty(str)) {
            HookLog.m9e("getPackageName using empty dataDir");
            return "";
        }
        int lastIndexOf = str.lastIndexOf(FileSelector_Dialog.sRoot);
        return lastIndexOf < 0 ? str : str.substring(lastIndexOf + 1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0019, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001a, code lost:
        r1.addSuppressed(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001d, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0014, code lost:
        r2 = move-exception;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String readLine(File r4) {
        /*
            java.io.BufferedReader r0 = new java.io.BufferedReader     // Catch:{ all -> 0x001e }
            java.io.FileReader r1 = new java.io.FileReader     // Catch:{ all -> 0x001e }
            r1.<init>(r4)     // Catch:{ all -> 0x001e }
            r0.<init>(r1)     // Catch:{ all -> 0x001e }
            java.lang.String r1 = r0.readLine()     // Catch:{ all -> 0x0012 }
            r0.close()
            return r1
        L_0x0012:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x0014 }
        L_0x0014:
            r2 = move-exception
            r0.close()     // Catch:{ all -> 0x0019 }
            goto L_0x001d
        L_0x0019:
            r3 = move-exception
            r1.addSuppressed(r3)
        L_0x001d:
            throw r2
        L_0x001e:
            r0 = move-exception
            java.lang.String r1 = ""
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xzy.forestSystem.swift.sandhook.utils.FileUtils.readLine(java.io.File):java.lang.String");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001b, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0020, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0021, code lost:
        r1.addSuppressed(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0024, code lost:
        throw r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void writeLine(File r4, String r5) {
        /*
            r4.createNewFile()     // Catch:{ IOException -> 0x0004 }
            goto L_0x0005
        L_0x0004:
            r0 = move-exception
        L_0x0005:
            java.io.BufferedWriter r0 = new java.io.BufferedWriter     // Catch:{ all -> 0x0025 }
            java.io.FileWriter r1 = new java.io.FileWriter     // Catch:{ all -> 0x0025 }
            r1.<init>(r4)     // Catch:{ all -> 0x0025 }
            r0.<init>(r1)     // Catch:{ all -> 0x0025 }
            r0.write(r5)     // Catch:{ all -> 0x0019 }
            r0.flush()     // Catch:{ all -> 0x0019 }
            r0.close()
            goto L_0x004a
        L_0x0019:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x001b }
        L_0x001b:
            r2 = move-exception
            r0.close()     // Catch:{ all -> 0x0020 }
            goto L_0x0024
        L_0x0020:
            r3 = move-exception
            r1.addSuppressed(r3)
        L_0x0024:
            throw r2
        L_0x0025:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "error writing line to file "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.StringBuilder r1 = r1.append(r4)
            java.lang.String r2 = ": "
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r2 = r0.getMessage()
            java.lang.StringBuilder r1 = r1.append(r2)
            java.lang.String r1 = r1.toString()
            com.xzy.forestSystem.swift.sandhook.HookLog.m9e(r1)
        L_0x004a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xzy.forestSystem.swift.sandhook.utils.FileUtils.writeLine(java.io.File, java.lang.String):void");
    }
}
