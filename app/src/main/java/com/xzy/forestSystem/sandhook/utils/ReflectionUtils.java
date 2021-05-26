package com.xzy.forestSystem.sandhook.utils;

import android.util.Log;

import java.lang.reflect.Method;

public class ReflectionUtils {
    static Method addWhiteListMethod;
    public static Method forNameMethod;
    public static Method getMethodMethod;
    static Object vmRuntime;
    static Class vmRuntimeClass;

    static {
        try {
            getMethodMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
            Method declaredMethod = Class.class.getDeclaredMethod("forName", String.class);
            forNameMethod = declaredMethod;
            Class cls = (Class) declaredMethod.invoke(null, "dalvik.system.VMRuntime");
            vmRuntimeClass = cls;
            addWhiteListMethod = (Method) getMethodMethod.invoke(cls, "setHiddenApiExemptions", new Class[]{String[].class});
            vmRuntime = ((Method) getMethodMethod.invoke(vmRuntimeClass, "getRuntime", null)).invoke(null, new Object[0]);
        } catch (Exception e) {
            Log.e("ReflectionUtils", "error get methods", e);
        }
    }

    public static void addReflectionWhiteList(String... strArr) throws Throwable {
//        addWhiteListMethod.invoke(vmRuntime, strArr);
    }

    public static boolean passApiCheck() {
        try {
            addReflectionWhiteList("Landroid/", "Lcom/android/", "Ljava/lang/", "Ldalvik/system/", "Llibcore/io/");
            return true;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }
}
