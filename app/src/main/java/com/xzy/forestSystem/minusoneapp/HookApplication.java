package com.xzy.forestSystem.minusoneapp;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HookApplication extends Application {
    private static boolean isJavaAPI;
    private static boolean isProxyHook;
    public static String originalSignature = "HYkeO2Pcy0SSE3ZGc9ttwFw3t23hJxwSxjRhmQ0YCt5XwIkb0VY85sFZtiTcFEjXDVDuYABMf8SB81Fpvu53CBJmMTv0T4P8dCA/G/v6AUn7L6iQ1TkDj1H34p/dkCmYzptmnNxSS15xUmqm56TNyfROYR3XW5ZeWMbC0YYb28K8uJN6/ludR8TAydwFMHPUYi7JUeKBaxeiHec57UjDWVtrk/AfHhSfbFybF9lINkBzeiED/DpTyKTsjJBuD8qkSUaHudglH5TG7lR8Q75omjBN9DFOJK/nk41I44Q84pYdFHVH97GdGHXkn7aUumk2n/0c9dpB9/BbbrGmdAhrnsUpH5e4f2SV314cEfavZlDPigKNL7sG6DhtQREQF7emTsYozh6C2mCFAG2M+eGF0wbebxZ8gfupdTxGbIAaKOd/SA8SsPpCNDdyD1JMl+JibMDgqNZ+NqHxPzJwynA7XX6b7tDMC1o7SpFHr3ySyGQtO7hNvBJEyqeOGGendHamDYqgXFUsSLWSCtlGkjyMPC901XKN+DgulPoPBwJGl78Jdctk8qdlWlFxVD5qUz+bwFxQd44QcIWflmszfe0JOyrrv6smDueb1afeptuywEhAzNchHVYqFgCe+cmPcrdTu1OV7zGivvjKZSgfJuv6eVH74XA9f/moJZhkaCbqCRCFauddPv+CQ00pNptjuwL4mSPGrQzsyKN4qX/i8fn5w1EqIIWRiAzjKrJVBVStcaU9ra0DEz/TlrlYlL5xWbmG9fLprj7U11835jmwhQKKDaq9+Y6+BMW+EI9eR5u80NeObeRowqCLtEJ4HGE1qD/zstGAvgyuW5ysDKU5Rd8gcChwYcdx+bLVt5V++CoI0PNK/yEYD6VfTOSmWokeu1yce2rCwE40eojCOIOizvnQ/b0tMA6wabUd2eA1o3YerRAhcRGRsGziYzvOQTzTp8tIs4vM3SK4m9jvgFdgOTS5wPGkhiXKph/cWbvX2lBsCCQtGXT2VMAp4y1e6bhuUhtode2wWs0c2bWVHbEzT6VMJUKO1dr6PurTrH03bwRyfmSesTH/mvB+kSAp4Gyahcu451vLe4y7j1heJwG05MLsRM9/r5U0Vde1OV1HCTQXZKYMTSRZ+sJO1IYHhiI305SyVPyZHwyBGldFjzQ9a1vqjpyM00RVny+Hc3bHVRo4zfrNxKuvaL4fGbI1DDC7GyYO+P34h2ZShNWtJyzZX07gnHafdeVIYIPOpPAYKpxNht1tUOeHdI1rU+WZupuoM41x59DDEDcHzTPI5Vqfgn3mw3sgAaFi9AEtQfnMvnGQrwcf/CMcooMZuRj9YTBZ1R2NToykwFide3ExtZE+MYop8HUabD43rfPu5AQIFGEfJGgIBOpaWaujaKUdjehgVT4FiyE7wJSx+03sHt8psMFH+L2KRzB7SqCfFUtkSd/IZc88gK1hUzERwBYSAWj26/U3bZXLd/ZFGGUhb2+Z+knxPKPcbMln0u4WcEe1589aZUWu4bDgAxtO93JdDyoZzb3bAYIsEH7d64ZFd1elIJN9bw==";
    private Application originalApplication;
    private String original_application_name = "J5Lc584BLgs2tK9uNud2IzbA+w9aX44ZUP7TJ8peYSA=";

    static {
        try {
            Context createAppContext = createAppContext();
            loadSoAndDexFromAssetPathCopy(createAppContext);
            HookSignature(createAppContext);
            Boolean bool = true;
            if (bool.booleanValue()) {
                redirectApk(createAppContext);
            }
        } catch (Throwable th) {
            th.printStackTrace();
            Log.i("HookFailed", "Error", th);
        }
    }

    public static native String ByMinusOne(String str);

    private static native void HookSignature(Context context);

    public static Context createAppContext() {
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread", new Class[0]);
            declaredMethod.setAccessible(true);
            Object invoke = declaredMethod.invoke(null, new Object[0]);
            Field declaredField = cls.getDeclaredField("mBoundApplication");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(invoke);
            Field declaredField2 = obj.getClass().getDeclaredField("info");
            declaredField2.setAccessible(true);
            Object obj2 = declaredField2.get(obj);
            Method declaredMethod2 = Class.forName("android.app.ContextImpl").getDeclaredMethod("createAppContext", cls, obj2.getClass());
            declaredMethod2.setAccessible(true);
            Object invoke2 = declaredMethod2.invoke(null, invoke, obj2);
            if (invoke2 instanceof Context) {
                return (Context) invoke2;
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static native boolean loadDexFile(Context context, List list);

    private static void loadSoAndDexFromAssetPathCopy(Context context) throws Exception {
        ArrayList arrayList = new ArrayList();
        AssetsUtil.copyAssetsDirectory(context, "Hook_so", arrayList);
        ArrayList arrayList2 = new ArrayList();
        AssetsUtil.copyAssetsDirectory(context, "App_dex", arrayList2);
        Log.d("ContentValues", "supported api:" + Build.CPU_ABI + " " + Build.CPU_ABI2);
        if (Build.VERSION.SDK_INT >= 21) {
            String[] strArr = Build.SUPPORTED_ABIS;
            int length = strArr.length;
            int i = 0;
            while (i < length && !loadSoFile(context, strArr[i], arrayList)) {
                i++;
            }
        } else if ((!TextUtils.isEmpty(Build.CPU_ABI) || !loadSoFile(context, Build.CPU_ABI, arrayList)) && (!TextUtils.isEmpty(Build.CPU_ABI2) || !loadSoFile(context, Build.CPU_ABI2, arrayList))) {
            loadSoFile(context, "armeabi", arrayList);
        }
        ArrayList arrayList3 = new ArrayList();
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            arrayList3.add(new File((String) it.next()));
        }
        System.loadLibrary("mocls");
        loadDexFile(context, arrayList3);
    }

    private static boolean loadSoFile(Context context, String str, List<String> list) {
        for (String str2 : list) {
            if (str2.contains(str)) {
                return SoAndDexLoader.loadSoFile(context, AssetsUtil.getParentDir(new File(str2)));
            }
        }
        return false;
    }

    private static native void redirectApk(Context context);

//    /* access modifiers changed from: protected */
//    @Override // android.content.ContextWrapper
//    public native void attachBaseContext(Context context);


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            replaceApplication(this);
            loadSoAndDexFromAssetPathCopy(this);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public void replaceApplication(Application application) throws Throwable {
        Object staticField = FieldUtils.getStaticField(Class.forName("android.app.ActivityThread"), "sCurrentActivityThread");
        Object field = FieldUtils.getField(FieldUtils.getField(staticField, "mBoundApplication"), "info");
        FieldUtils.setField(staticField, "mInitialApplication", application);
        FieldUtils.setField(field, "mApplication", application);
    }

    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x0052: APUT
      (r9v0 java.lang.Class[])
      (1 ??[boolean, int, float, short, byte, char])
      (wrap: java.lang.Class<?> : 0x004d: INVOKE  (r10v2 java.lang.Class<?>) = ("android.app.Instrumentation") type: STATIC call: java.lang.Class.forName(java.lang.String):java.lang.Class)
     */
    public Application sOriginalApplication(String str) throws Throwable {
        Object field;
        Object staticField = FieldUtils.getStaticField(Class.forName("android.app.ActivityThread"), "sCurrentActivityThread");
        Object field2 = FieldUtils.getField(staticField, "mBoundApplication");
        Object field3 = FieldUtils.getField(field2, "info");
        FieldUtils.setField(field3, "mApplication", null);
        Object field4 = FieldUtils.getField(staticField, "mInitialApplication");
        ArrayList arrayList = (ArrayList) FieldUtils.getField(staticField, "mAllApplications");
        arrayList.remove(field4);
        ((ApplicationInfo) FieldUtils.getField(field3, "mApplicationInfo")).className = str;
        ((ApplicationInfo) FieldUtils.getField(field2, "appInfo")).className = str;
        Class[] clsArr = new Class[2];
        clsArr[0] = Boolean.TYPE;
        try {
            clsArr[1] = Class.forName("android.app.Instrumentation");
            Application application = (Application) MethodUtils.callMethod(field3, "makeApplication", clsArr, new Boolean(false), null);
            FieldUtils.setField(staticField, "mInitialApplication", application);
            FieldUtils.setField(field3, "mApplication", application);
            arrayList.add(application);
            for (Object obj : ((ArrayMap) FieldUtils.getField(staticField, "mProviderMap")).values()) {
                if (!(obj == null || (field = FieldUtils.getField(obj, "mLocalProvider")) == null)) {
                    FieldUtils.setField(field, "mContext", application);
                }
            }
            return application;
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }
}
