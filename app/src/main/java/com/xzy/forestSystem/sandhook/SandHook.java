package com.xzy.forestSystem.sandhook;

import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import com.xzy.forestSystem.sandhook.annotation.HookMode;
import com.xzy.forestSystem.sandhook.blacklist.HookBlackList;
import com.xzy.forestSystem.sandhook.utils.ClassStatusUtils;
import com.xzy.forestSystem.sandhook.utils.FileUtils;
import com.xzy.forestSystem.sandhook.utils.ReflectionUtils;
import com.xzy.forestSystem.sandhook.utils.Unsafe;
import com.xzy.forestSystem.sandhook.wrapper.HookErrorException;
import com.xzy.forestSystem.sandhook.wrapper.HookWrapper;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SandHook {
    public static Class artMethodClass;
    static Map<Method, HookWrapper.HookEntity> globalBackupMap = new ConcurrentHashMap();
    static Map<Member, HookWrapper.HookEntity> globalHookEntityMap = new ConcurrentHashMap();
    private static HookModeCallBack hookModeCallBack;
    private static HookResultCallBack hookResultCallBack;
    public static Field nativePeerField;
    public static int testAccessFlag;
    public static Object testOffsetArtMethod1;
    public static Object testOffsetArtMethod2;
    public static Method testOffsetMethod1;
    public static Method testOffsetMethod2;

    @FunctionalInterface
    public interface HookModeCallBack {
        int hookMode(Member member);
    }

    @FunctionalInterface
    public interface HookResultCallBack {
        void hookResult(boolean z, HookWrapper.HookEntity hookEntity);
    }

    static {
        SandHookConfig.libLoader.loadLib();
        init();
    }

    public static native void MakeInitializedClassVisibilyInitialized(long j);

    public static void addHookClass(ClassLoader classLoader, Class... clsArr) throws HookErrorException {
        HookWrapper.addHookClass(classLoader, clsArr);
    }

    public static void addHookClass(Class... clsArr) throws HookErrorException {
        HookWrapper.addHookClass(clsArr);
    }

    public static final Object callOriginByBackup(Method method, Object obj, Object... objArr) throws Throwable {
        HookWrapper.HookEntity hookEntity = globalBackupMap.get(method);
        if (hookEntity == null) {
            return null;
        }
        return callOriginMethod(hookEntity.backupIsStub, hookEntity.target, method, obj, objArr);
    }

    public static final Object callOriginMethod(Member member, Object obj, Object... objArr) throws Throwable {
        HookWrapper.HookEntity hookEntity = globalHookEntityMap.get(member);
        if (hookEntity == null || hookEntity.backup == null) {
            return null;
        }
        return callOriginMethod(hookEntity.backupIsStub, member, hookEntity.backup, obj, objArr);
    }

    public static final Object callOriginMethod(Member member, Method method, Object obj, Object[] objArr) throws Throwable {
        return callOriginMethod(true, member, method, obj, objArr);
    }

    public static final Object callOriginMethod(boolean z, Member member, Method method, Object obj, Object[] objArr) throws Throwable {
        if (!z && SandHookConfig.SDK_INT >= 24) {
            member.getDeclaringClass();
            ensureDeclareClass(member, method);
        }
        if (Modifier.isStatic(member.getModifiers())) {
            try {
                return method.invoke(null, objArr);
            } catch (InvocationTargetException e) {
                if (e.getCause() != null) {
                    throw e.getCause();
                }
                throw e;
            }
        } else {
            try {
                return method.invoke(obj, objArr);
            } catch (InvocationTargetException e2) {
                if (e2.getCause() != null) {
                    throw e2.getCause();
                }
                throw e2;
            }
        }
    }

    public static native boolean canGetObject();

    public static boolean canGetObjectAddress() {
        return Unsafe.support();
    }

    public static native boolean compileMethod(Member member);

    public static native boolean deCompileMethod(Member member, boolean z);

    public static native boolean disableDex2oatInline(boolean z);

    public static native boolean disableVMInline();

    public static final void ensureBackupMethod(Method method) {
        HookWrapper.HookEntity hookEntity;
        if (SandHookConfig.SDK_INT >= 24 && (hookEntity = globalBackupMap.get(method)) != null) {
            ensureDeclareClass(hookEntity.target, method);
        }
    }

    public static native void ensureDeclareClass(Member member, Method method);

    public static native void ensureMethodCached(Method method, Method method2);

    public static long getArtMethod(Member member) {
        return SandHookMethodResolver.getArtMethod(member);
    }

    private static Object[] getFakeArgs(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length == 0) {
            return new Object[]{new Object()};
        }
        return null;
    }

    public static Field getField(Class cls, String str) throws NoSuchFieldException {
        while (cls != null && cls != Object.class) {
            try {
                Field declaredField = cls.getDeclaredField(str);
                declaredField.setAccessible(true);
                return declaredField;
            } catch (Exception e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchFieldException(str);
    }

    public static Object getJavaMethod(String str, String str2) {
        if (str == null) {
            return null;
        }
        try {
            try {
                return Class.forName(str).getDeclaredMethod(str2, new Class[0]);
            } catch (NoSuchMethodException e) {
                return null;
            }
        } catch (ClassNotFoundException e2) {
            return null;
        }
    }

    public static Object getObject(long j) {
        if (j == 0) {
            return null;
        }
        return getObjectNative(getThreadId(), j);
    }

    public static long getObjectAddress(Object obj) {
        return Unsafe.getObjectAddress(obj);
    }

    public static native Object getObjectNative(long j, long j2);

    public static long getThreadId() {
        Field field = nativePeerField;
        if (field == null) {
            return 0;
        }
        try {
            return field.getType() == Integer.TYPE ? (long) nativePeerField.getInt(Thread.currentThread()) : nativePeerField.getLong(Thread.currentThread());
        } catch (IllegalAccessException e) {
            return 0;
        }
    }

    public static boolean hasJavaArtMethod() {
        if (SandHookConfig.SDK_INT >= 26) {
            return false;
        }
        if (artMethodClass != null) {
            return true;
        }
        try {
            if (SandHookConfig.initClassLoader == null) {
                artMethodClass = Class.forName("java.lang.reflect.ArtMethod");
            } else {
                artMethodClass = Class.forName("java.lang.reflect.ArtMethod", true, SandHookConfig.initClassLoader);
            }
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static synchronized void hook(HookWrapper.HookEntity hookEntity) throws HookErrorException {
        int i;
        synchronized (SandHook.class) {
            if (hookEntity != null) {
                Member member = hookEntity.target;
                Method method = hookEntity.hook;
                Method method2 = hookEntity.backup;
                if (member == null || method == null) {
                    throw new HookErrorException("null input");
                } else if (globalHookEntityMap.containsKey(hookEntity.target)) {
                    throw new HookErrorException("method <" + hookEntity.target.toString() + "> has been hooked!");
                } else if (HookBlackList.canNotHook(member)) {
                    throw new HookErrorException("method <" + hookEntity.target.toString() + "> can not hook, because of in blacklist!");
                } else if (!SandHookConfig.delayHook || !PendingHookHandler.canWork() || !ClassStatusUtils.isStaticAndNoInited(hookEntity.target)) {
                    if (hookEntity.initClass) {
                        resolveStaticMethod(member);
                        MakeInitializedClassVisibilyInitialized(getThreadId());
                    }
                    resolveStaticMethod(method2);
                    if (method2 != null && hookEntity.resolveDexCache) {
                        SandHookMethodResolver.resolveMethod(method, method2);
                    }
                    if (member instanceof Method) {
                        ((Method) member).setAccessible(true);
                    }
                    int i2 = 0;
                    HookModeCallBack hookModeCallBack2 = hookModeCallBack;
                    if (hookModeCallBack2 != null) {
                        i2 = hookModeCallBack2.hookMode(member);
                    }
                    globalHookEntityMap.put(hookEntity.target, hookEntity);
                    boolean z = false;
                    if (i2 != 0) {
                        i = hookMethod(member, method, method2, i2);
                    } else {
                        HookMode hookMode = (HookMode) method.getAnnotation(HookMode.class);
                        i = hookMethod(member, method, method2, hookMode == null ? 0 : hookMode.value());
                    }
                    if (i > 0 && method2 != null) {
                        method2.setAccessible(true);
                    }
                    hookEntity.hookMode = i;
                    HookResultCallBack hookResultCallBack2 = hookResultCallBack;
                    if (hookResultCallBack2 != null) {
                        if (i > 0) {
                            z = true;
                        }
                        hookResultCallBack2.hookResult(z, hookEntity);
                    }
                    if (i >= 0) {
                        if (hookEntity.backup != null) {
                            globalBackupMap.put(hookEntity.backup, hookEntity);
                        }
                        HookLog.m10d("method <" + hookEntity.target.toString() + "> hook <" + (i == 1 ? "inline" : "replacement") + "> success!");
                        return;
                    }
                    globalHookEntityMap.remove(hookEntity.target);
                    throw new HookErrorException("hook method <" + hookEntity.target.toString() + "> error in native!");
                } else {
                    PendingHookHandler.addPendingHook(hookEntity);
                }
            } else {
                throw new HookErrorException("null hook entity");
            }
        }
    }

    private static native int hookMethod(Member member, Method method, Method method2, int i);

    private static boolean init() {
        initTestOffset();
        initThreadPeer();
        SandHookMethodResolver.init();
        return initNative(SandHookConfig.SDK_INT, SandHookConfig.DEBUG);
    }

    public static native boolean initForPendingHook();

    private static native boolean initNative(int i, boolean z);

    private static void initTestAccessFlag() {
        if (hasJavaArtMethod()) {
            try {
                loadArtMethod();
                testAccessFlag = ((Integer) getField(artMethodClass, "accessFlags").get(testOffsetArtMethod1)).intValue();
            } catch (Exception e) {
            }
        } else {
            try {
                testAccessFlag = ((Integer) getField(Method.class, "accessFlags").get(testOffsetMethod1)).intValue();
            } catch (Exception e2) {
            }
        }
    }

    private static void initTestOffset() {
        ArtMethodSizeTest.method1();
        ArtMethodSizeTest.method2();
        try {
            testOffsetMethod1 = ArtMethodSizeTest.class.getDeclaredMethod("method1", new Class[0]);
            testOffsetMethod2 = ArtMethodSizeTest.class.getDeclaredMethod("method2", new Class[0]);
            initTestAccessFlag();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("SandHook init error", e);
        }
    }

    private static void initThreadPeer() {
        try {
            nativePeerField = getField(Thread.class, "nativePeer");
        } catch (NoSuchFieldException e) {
        }
    }

    public static native boolean is64Bit();

    private static void loadArtMethod() {
        try {
            Field field = getField(Method.class, "artMethod");
            testOffsetArtMethod1 = field.get(testOffsetMethod1);
            testOffsetArtMethod2 = field.get(testOffsetMethod2);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
    }

    public static boolean passApiCheck() {
        return ReflectionUtils.passApiCheck();
    }

    public static boolean resolveStaticMethod(Member member) {
        if (member == null) {
            return true;
        }
        try {
            if ((member instanceof Method) && Modifier.isStatic(member.getModifiers())) {
                ((Method) member).setAccessible(true);
                ((Method) member).invoke(new Object(), getFakeArgs((Method) member));
            }
        } catch (ExceptionInInitializerError e) {
            return false;
        } catch (Throwable th) {
        }
        return true;
    }

    public static native void setHookMode(int i);

    public static void setHookModeCallBack(HookModeCallBack hookModeCallBack2) {
        hookModeCallBack = hookModeCallBack2;
    }

    public static void setHookResultCallBack(HookResultCallBack hookResultCallBack2) {
        hookResultCallBack = hookResultCallBack2;
    }

    public static native void setInlineSafeCheck(boolean z);

    public static native boolean setNativeEntry(Member member, Member member2, long j);

    public static native void skipAllSafeCheck(boolean z);

    public static boolean tryDisableProfile(String str) {
        if (SandHookConfig.SDK_INT < 24) {
            return false;
        }
        try {
            File file = new File("/data/misc/profiles/cur/" + SandHookConfig.curUser + FileSelector_Dialog.sRoot + str + "/primary.prof");
            if (!file.getParentFile().exists()) {
                return false;
            }
            try {
                file.delete();
                file.createNewFile();
            } catch (Throwable th) {
            }
            FileUtils.chmod(file.getAbsolutePath(), 256);
            return true;
        } catch (Throwable th2) {
            return false;
        }
    }
}
