package com.xzy.forestSystem.sandhook.utils;

import com.xzy.forestSystem.sandhook.SandHookConfig;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ClassStatusUtils {
    static Field fieldStatusOfClass;

    static {
        try {
            Field declaredField = Class.class.getDeclaredField("status");
            fieldStatusOfClass = declaredField;
            declaredField.setAccessible(true);
        } catch (NoSuchFieldException e) {
        }
    }

    public static int getClassStatus(Class cls, boolean z) {
        if (cls == null) {
            return 0;
        }
        int i = 0;
        try {
            i = fieldStatusOfClass.getInt(cls);
        } catch (Throwable th) {
        }
        return z ? (int) (toUnsignedLong(i) >> 28) : i;
    }

    public static boolean isInitialized(Class cls) {
        if (fieldStatusOfClass == null) {
            return true;
        }
        return SandHookConfig.SDK_INT >= 30 ? getClassStatus(cls, true) >= 14 : SandHookConfig.SDK_INT >= 28 ? getClassStatus(cls, true) == 14 : SandHookConfig.SDK_INT == 27 ? getClassStatus(cls, false) == 11 : getClassStatus(cls, false) == 10;
    }

    public static boolean isStaticAndNoInited(Member member) {
        if (!(member instanceof Method)) {
            return false;
        }
        return Modifier.isStatic(member.getModifiers()) && !isInitialized(member.getDeclaringClass());
    }

    public static long toUnsignedLong(int i) {
        return ((long) i) & 4294967295L;
    }
}
