package com.xzy.forestSystem.sandhook;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

public class SandHookMethodResolver {
    public static Field artMethodField;
    public static boolean canResolvedInJava = false;
    public static Field dexCacheField;
    public static int dexMethodIndex = 0;
    public static Field dexMethodIndexField;
    public static long entryPointFromCompiledCode = 0;
    public static long entryPointFromInterpreter = 0;
    public static Field fieldEntryPointFromCompiledCode;
    public static Field fieldEntryPointFromInterpreter;
    public static boolean isArtMethod = false;
    public static long resolvedMethodsAddress = 0;
    public static Field resolvedMethodsField;
    public static Object testArtMethod;
    public static Method testMethod;

    private static void checkSupport() {
        try {
            Field field = SandHook.getField(Method.class, "artMethod");
            artMethodField = field;
            testArtMethod = field.get(testMethod);
            if (SandHook.hasJavaArtMethod() && testArtMethod.getClass() == SandHook.artMethodClass) {
                checkSupportForArtMethod();
                isArtMethod = true;
            } else if (testArtMethod instanceof Long) {
                checkSupportForArtMethodId();
                isArtMethod = false;
            } else {
                canResolvedInJava = false;
            }
        } catch (Exception e) {
        }
    }

    private static void checkSupportForArtMethod() throws Exception {
        try {
            dexMethodIndexField = SandHook.getField(SandHook.artMethodClass, "dexMethodIndex");
        } catch (NoSuchFieldException e) {
            dexMethodIndexField = SandHook.getField(SandHook.artMethodClass, "methodDexIndex");
        }
        Field field = SandHook.getField(Class.class, "dexCache");
        dexCacheField = field;
        Object obj = field.get(testMethod.getDeclaringClass());
        Field field2 = SandHook.getField(obj.getClass(), "resolvedMethods");
        resolvedMethodsField = field2;
        if (field2.get(obj) instanceof Object[]) {
            canResolvedInJava = true;
        }
        try {
            dexMethodIndex = ((Integer) dexMethodIndexField.get(testArtMethod)).intValue();
        } catch (Throwable th) {
        }
        try {
            fieldEntryPointFromCompiledCode = SandHook.getField(SandHook.artMethodClass, "entryPointFromQuickCompiledCode");
        } catch (Throwable th2) {
            return;
        }
        if (fieldEntryPointFromCompiledCode.getType() == Integer.TYPE) {
            entryPointFromCompiledCode = (long) fieldEntryPointFromCompiledCode.getInt(testArtMethod);
        } else if (fieldEntryPointFromCompiledCode.getType() == Long.TYPE) {
            entryPointFromCompiledCode = fieldEntryPointFromCompiledCode.getLong(testArtMethod);
        }
        Field field3 = SandHook.getField(SandHook.artMethodClass, "entryPointFromInterpreter");
        fieldEntryPointFromInterpreter = field3;
        if (field3.getType() == Integer.TYPE) {
            entryPointFromInterpreter = (long) fieldEntryPointFromInterpreter.getInt(testArtMethod);
        } else if (fieldEntryPointFromCompiledCode.getType() == Long.TYPE) {
            entryPointFromInterpreter = fieldEntryPointFromInterpreter.getLong(testArtMethod);
        }
    }

    private static void checkSupportForArtMethodId() throws Exception {
        Field field = SandHook.getField(Method.class, "dexMethodIndex");
        dexMethodIndexField = field;
        dexMethodIndex = ((Integer) field.get(testMethod)).intValue();
        Field field2 = SandHook.getField(Class.class, "dexCache");
        dexCacheField = field2;
        Object obj = field2.get(testMethod.getDeclaringClass());
        Field field3 = SandHook.getField(obj.getClass(), "resolvedMethods");
        resolvedMethodsField = field3;
        Object obj2 = field3.get(obj);
        if (obj2 instanceof Long) {
            canResolvedInJava = false;
            resolvedMethodsAddress = ((Long) obj2).longValue();
        } else if (obj2 instanceof long[]) {
            canResolvedInJava = true;
        } else if (obj2 instanceof int[]) {
            canResolvedInJava = true;
        }
    }

    public static long getArtMethod(Member member) {
        Field field = artMethodField;
        if (field == null) {
            return 0;
        }
        try {
            return ((Long) field.get(member)).longValue();
        } catch (IllegalAccessException e) {
            return 0;
        }
    }

    public static void init() {
        testMethod = SandHook.testOffsetMethod1;
        checkSupport();
    }

    private static void resolveInJava(Method method, Method method2) throws Exception {
        Object obj = dexCacheField.get(method.getDeclaringClass());
        if (isArtMethod) {
            Object obj2 = artMethodField.get(method2);
            ((Object[]) resolvedMethodsField.get(obj))[((Integer) dexMethodIndexField.get(obj2)).intValue()] = obj2;
            return;
        }
        int intValue = ((Integer) dexMethodIndexField.get(method2)).intValue();
        Object obj3 = resolvedMethodsField.get(obj);
        if (obj3 instanceof long[]) {
            ((long[]) obj3)[intValue] = ((Long) artMethodField.get(method2)).longValue();
        } else if (obj3 instanceof int[]) {
            ((int[]) obj3)[intValue] = Long.valueOf(((Long) artMethodField.get(method2)).longValue()).intValue();
        } else {
            throw new UnsupportedOperationException("un support");
        }
    }

    private static void resolveInNative(Method method, Method method2) {
        SandHook.ensureMethodCached(method, method2);
    }

    public static void resolveMethod(Method method, Method method2) {
        if (!canResolvedInJava || artMethodField == null) {
            resolveInNative(method, method2);
            return;
        }
        try {
            resolveInJava(method, method2);
        } catch (Exception e) {
            resolveInNative(method, method2);
        }
    }
}
