package com.xzy.forestSystem.minusoneapp;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.xzy.forestSystem.swift.sandhook.annotation.HookMethod;
import com.xzy.forestSystem.swift.sandhook.annotation.HookMethodBackup;
import com.xzy.forestSystem.swift.sandhook.annotation.HookReflectClass;
import com.xzy.forestSystem.swift.sandhook.annotation.MethodParams;
import com.xzy.forestSystem.swift.sandhook.annotation.ThisObject;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@HookReflectClass("android.app.ApplicationPackageManager")
public class PackageSignatureHooker implements InvocationHandler {
    public static String Copyright = "2020-2021 854366888@qq.com All Rights Reserved";
    @HookMethodBackup("getPackageInfo")
    @MethodParams({String.class, int.class})
    static Method getPackageInfoBackup;
    public static Context mContext;
    public static Object pmBase;

    public static native void hookPackageManager(PackageManager packageManager, String str);

    public static native void killStubApp(Application application, Class cls);

    @MethodParams({String.class, int.class})
    @HookMethod("getPackageInfo")
    public static native PackageInfo replacePackageInfo(Object obj, String str, int i);

    public static native void replaceSigningInfo(PackageManager packageManager, String str, int i);

    @Override // java.lang.reflect.InvocationHandler
    public native Object invoke(Object obj, Method method, Object[] objArr);
}
