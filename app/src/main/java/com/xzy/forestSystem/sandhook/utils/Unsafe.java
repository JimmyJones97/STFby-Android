package com.xzy.forestSystem.sandhook.utils;

import android.util.Log;

import com.xzy.forestSystem.sandhook.HookLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class Unsafe {
    private static final String TAG = "Unsafe";
    private static Method arrayBaseOffsetMethod;
    private static Method arrayIndexScaleMethod;
    private static Method getIntMethod;
    private static Method getLongMethod;
    private static Class objectArrayClass = Object[].class;
    private static volatile boolean supported;
    private static Object unsafe;
    private static Class unsafeClass;

    static {
        supported = false;
        try {
            Class<?> cls = Class.forName("sun.misc.Unsafe");
            unsafeClass = cls;
            Field declaredField = cls.getDeclaredField("theUnsafe");
            declaredField.setAccessible(true);
            unsafe = declaredField.get(null);
        } catch (Exception e) {
            try {
                Field declaredField2 = unsafeClass.getDeclaredField("THE_ONE");
                declaredField2.setAccessible(true);
                unsafe = declaredField2.get(null);
            } catch (Exception e2) {
                Log.w(TAG, "Unsafe not found o.O");
            }
        }
        if (unsafe != null) {
            try {
                arrayBaseOffsetMethod = unsafeClass.getDeclaredMethod("arrayBaseOffset", Class.class);
                arrayIndexScaleMethod = unsafeClass.getDeclaredMethod("arrayIndexScale", Class.class);
                getIntMethod = unsafeClass.getDeclaredMethod("getInt", Object.class, Long.TYPE);
                getLongMethod = unsafeClass.getDeclaredMethod("getLong", Object.class, Long.TYPE);
                supported = true;
            } catch (Exception e3) {
            }
        }
    }

    private Unsafe() {
    }

    public static int arrayBaseOffset(Class cls) {
        try {
            return ((Integer) arrayBaseOffsetMethod.invoke(unsafe, cls)).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static int arrayIndexScale(Class cls) {
        try {
            return ((Integer) arrayIndexScaleMethod.invoke(unsafe, cls)).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getInt(Object obj, long j) {
        try {
            return ((Integer) getIntMethod.invoke(unsafe, obj, Long.valueOf(j))).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static long getLong(Object obj, long j) {
        try {
            return ((Long) getLongMethod.invoke(unsafe, obj, Long.valueOf(j))).longValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static long getObjectAddress(Object obj) {
        try {
            Object[] objArr = {obj};
            return arrayIndexScale(objectArrayClass) == 8 ? getLong(objArr, (long) arrayBaseOffset(objectArrayClass)) : 4294967295L & ((long) getInt(objArr, (long) arrayBaseOffset(objectArrayClass)));
        } catch (Exception e) {
            HookLog.m8e("get object address error", e);
            return -1;
        }
    }

    public static boolean support() {
        return supported;
    }
}
