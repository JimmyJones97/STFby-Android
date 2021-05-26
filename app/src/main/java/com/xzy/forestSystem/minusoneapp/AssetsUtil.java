package com.xzy.forestSystem.minusoneapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class AssetsUtil {
    public static boolean copyAssetFile(AssetManager assetManager, String str, String str2) {
        if (assetManager == null || TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        Log.i("From", str);
        Log.i("From2", str2);
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(str2);
            file.delete();
            file.getParentFile().mkdirs();
            BufferedInputStream bufferedInputStream2 = new BufferedInputStream(assetManager.open(str));
            FileOutputStream fileOutputStream2 = new FileOutputStream(str2);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = bufferedInputStream2.read(bArr);
                if (read == -1) {
                    break;
                }
                fileOutputStream2.write(bArr, 0, read);
            }
            fileOutputStream2.flush();
            try {
                bufferedInputStream2.close();
            } catch (Exception e) {
            }
            try {
                fileOutputStream2.close();
            } catch (Exception e2) {
            }
            return true;
        } catch (Exception e3) {
            e3.printStackTrace();
            if (0 != 0) {
                try {
                    bufferedInputStream.close();
                } catch (Exception e4) {
                }
            }
            if (0 != 0) {
                try {
                    fileOutputStream.close();
                } catch (Exception e5) {
                }
            }
            return false;
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    bufferedInputStream.close();
                } catch (Exception e6) {
                }
            }
            if (0 != 0) {
                try {
                    fileOutputStream.close();
                } catch (Exception e7) {
                }
            }
            throw th;
        }
    }

    public static void copyAssetsDirectory(Context context, String str, List<String> list) throws Exception {
        AssetManager assets = context.getAssets();
        String[] list2 = context.getAssets().list(str);
        if (!(list2 == null || list2.length == 0)) {
            for (String str2 : list2) {
                if (str2.contains(FileSelector_Dialog.sFolder)) {
                    File file = new File(context.getDir("libs", 0), getParentDir(str));
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    String str3 = file.getAbsolutePath() + FileSelector_Dialog.sRoot + str2;
                    if (copyAssetFile(assets, str + FileSelector_Dialog.sRoot + str2, str3)) {
                        list.add(str3);
                    }
                } else {
                    copyAssetsDirectory(context, str + FileSelector_Dialog.sRoot + str2, list);
                }
            }
        }
    }

    public static String getParentDir(File file) {
        return getParentDir(file.getParent());
    }

    public static String getParentDir(String str) {
        String[] split = str.replaceAll("\\\\", FileSelector_Dialog.sRoot).split(FileSelector_Dialog.sRoot);
        return split[split.length - 1];
    }
}
