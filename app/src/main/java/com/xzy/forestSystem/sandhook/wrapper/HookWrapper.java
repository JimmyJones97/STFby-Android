package com.xzy.forestSystem.sandhook.wrapper;

import android.text.TextUtils;

import com.xzy.forestSystem.sandhook.SandHook;
import com.xzy.forestSystem.sandhook.annotation.HookClass;
import com.xzy.forestSystem.sandhook.annotation.HookMethod;
import com.xzy.forestSystem.sandhook.annotation.HookMethodBackup;
import com.xzy.forestSystem.sandhook.annotation.HookReflectClass;
import com.xzy.forestSystem.sandhook.annotation.MethodParams;
import com.xzy.forestSystem.sandhook.annotation.MethodReflectParams;
import com.xzy.forestSystem.sandhook.annotation.Param;
import com.xzy.forestSystem.sandhook.annotation.SkipParamCheck;
import com.xzy.forestSystem.sandhook.annotation.ThisObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class HookWrapper {

    public static class HookEntity {
        public Method backup;
        public boolean backupIsStub = true;
        public Method hook;
        public boolean hookIsStub = false;
        public int hookMode;
        public boolean initClass = true;
        public Class[] pars;
        public boolean resolveDexCache = true;
        public Member target;

        public HookEntity(Member member) {
            this.target = member;
        }

        public HookEntity(Member member, Method method, Method method2) {
            this.target = member;
            this.hook = method;
            this.backup = method2;
        }

        public HookEntity(Member member, Method method, Method method2, boolean z) {
            this.target = member;
            this.hook = method;
            this.backup = method2;
            this.resolveDexCache = z;
        }

        public Object callOrigin(Object obj, Object... objArr) throws Throwable {
            return SandHook.callOriginMethod(this.backupIsStub, this.target, this.backup, obj, objArr);
        }

        public boolean isCtor() {
            return this.target instanceof Constructor;
        }
    }

    public static void addHookClass(ClassLoader classLoader, Class<?> cls) throws HookErrorException {
        Class targetHookClass = getTargetHookClass(classLoader, cls);
        if (targetHookClass != null) {
            Map<Member, HookEntity> hookMethods = getHookMethods(classLoader, targetHookClass, cls);
            try {
                fillBackupMethod(classLoader, cls, hookMethods);
                for (HookEntity hookEntity : hookMethods.values()) {
                    SandHook.hook(hookEntity);
                }
            } catch (Throwable th) {
                throw new HookErrorException("fillBackupMethod error!", th);
            }
        } else {
            throw new HookErrorException("error hook wrapper class :" + cls.getName());
        }
    }

    public static void addHookClass(ClassLoader classLoader, Class<?>... clsArr) throws HookErrorException {
        for (Class<?> cls : clsArr) {
            addHookClass(classLoader, cls);
        }
    }

    public static void addHookClass(Class<?>... clsArr) throws HookErrorException {
        addHookClass((ClassLoader) null, clsArr);
    }

    public static void checkSignature(Member member, Method method, Class[] clsArr) throws HookErrorException {
        Class<?> returnType;
        if (Modifier.isStatic(method.getModifiers())) {
            if (member instanceof Constructor) {
                if (!method.getReturnType().equals(Void.TYPE)) {
                    throw new HookErrorException("error return type! - " + method.getName());
                }
            } else if ((member instanceof Method) && (returnType = ((Method) member).getReturnType()) != method.getReturnType() && !returnType.isAssignableFrom(returnType)) {
                throw new HookErrorException("error return type! - " + method.getName());
            }
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes == null) {
                parameterTypes = new Class[0];
            }
            if (clsArr == null) {
                clsArr = new Class[0];
            }
            if (!(clsArr.length == 0 && parameterTypes.length == 0)) {
                int i = 0;
                if (!Modifier.isStatic(member.getModifiers())) {
                    i = 1;
                    if (parameterTypes.length == 0) {
                        throw new HookErrorException("first par must be this! " + method.getName());
                    } else if (parameterTypes[0] != member.getDeclaringClass() && !parameterTypes[0].isAssignableFrom(member.getDeclaringClass())) {
                        throw new HookErrorException("first par must be this! " + method.getName());
                    } else if (parameterTypes.length != clsArr.length + 1) {
                        throw new HookErrorException("hook method pars must match the origin method! " + method.getName());
                    }
                } else if (parameterTypes.length != clsArr.length) {
                    throw new HookErrorException("hook method pars must match the origin method! " + method.getName());
                }
                for (int i2 = 0; i2 < clsArr.length; i2++) {
                    if (parameterTypes[i2 + i] != clsArr[i2] && !parameterTypes[i2 + i].isAssignableFrom(clsArr[i2])) {
                        throw new HookErrorException("hook method pars must match the origin method! " + method.getName());
                    }
                }
                return;
            }
            return;
        }
        throw new HookErrorException("hook method must static! - " + method.getName());
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    private static Class classNameToClass(String str, ClassLoader classLoader) throws ClassNotFoundException {
        char c;
        switch (str.hashCode()) {
            case -1325958191:
                if (str.equals(MethodReflectParams.DOUBLE)) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 104431:
                if (str.equals(MethodReflectParams.INT)) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 3039496:
                if (str.equals(MethodReflectParams.BYTE)) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 3052374:
                if (str.equals(MethodReflectParams.CHAR)) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 3327612:
                if (str.equals(MethodReflectParams.LONG)) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 64711720:
                if (str.equals(MethodReflectParams.BOOLEAN)) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 97526364:
                if (str.equals(MethodReflectParams.FLOAT)) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 109413500:
                if (str.equals(MethodReflectParams.SHORT)) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return Boolean.TYPE;
            case 1:
                return Byte.TYPE;
            case 2:
                return Character.TYPE;
            case 3:
                return Double.TYPE;
            case 4:
                return Float.TYPE;
            case 5:
                return Integer.TYPE;
            case 6:
                return Long.TYPE;
            case 7:
                return Short.TYPE;
            default:
                return classLoader == null ? Class.forName(str) : Class.forName(str, true, classLoader);
        }
    }

    private static void fillBackupMethod(ClassLoader classLoader, Class<?> cls, Map<Member, HookEntity> map) {
        HookMethodBackup hookMethodBackup;
        Field[] fieldArr = null;
        try {
            fieldArr = cls.getDeclaredFields();
        } catch (Throwable th) {
        }
        if (!(fieldArr == null || fieldArr.length == 0 || map.isEmpty())) {
            for (Field field : fieldArr) {
                if (Modifier.isStatic(field.getModifiers()) && (hookMethodBackup = (HookMethodBackup) field.getAnnotation(HookMethodBackup.class)) != null) {
                    for (HookEntity hookEntity : map.values()) {
                        if (TextUtils.equals(hookEntity.isCtor() ? "<init>" : hookEntity.target.getName(), hookMethodBackup.value()) && samePars(classLoader, field, hookEntity.pars)) {
                            field.setAccessible(true);
                            if (hookEntity.backup == null) {
                                hookEntity.backup = StubMethodsFactory.getStubMethod();
                                hookEntity.hookIsStub = true;
                                hookEntity.resolveDexCache = false;
                            }
                            if (hookEntity.backup != null) {
                                try {
                                    if (field.getType() == Method.class) {
                                        field.set(null, hookEntity.backup);
                                    } else if (field.getType() == HookEntity.class) {
                                        field.set(null, hookEntity);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static Map<Member, HookEntity> getHookMethods(ClassLoader classLoader, Class cls, Class<?> cls2) throws HookErrorException {
        HashMap hashMap = new HashMap();
        Method[] methodArr = null;
        try {
            methodArr = cls2.getDeclaredMethods();
        } catch (Throwable th) {
        }
        if (methodArr == null || methodArr.length == 0) {
            throw new HookErrorException("error hook wrapper class :" + cls.getName());
        }
        for (Method method : methodArr) {
            HookMethod hookMethod = (HookMethod) method.getAnnotation(HookMethod.class);
            HookMethodBackup hookMethodBackup = (HookMethodBackup) method.getAnnotation(HookMethodBackup.class);
            if (hookMethod != null) {
                String value = hookMethod.value();
                Class<?>[] parseMethodPars = parseMethodPars(classLoader, method);
                try {
                    Member constructor = value.equals("<init>") ? cls.getConstructor(parseMethodPars) : cls.getDeclaredMethod(value, parseMethodPars);
                    if (!method.isAnnotationPresent(SkipParamCheck.class)) {
                        checkSignature(constructor, method, parseMethodPars);
                    }
                    HookEntity hookEntity = (HookEntity) hashMap.get(constructor);
                    if (hookEntity == null) {
                        hookEntity = new HookEntity(constructor);
                        hashMap.put(constructor, hookEntity);
                    }
                    hookEntity.pars = parseMethodPars;
                    hookEntity.hook = method;
                } catch (NoSuchMethodException e) {
                    throw new HookErrorException("can not find target method: " + value, e);
                }
            } else if (hookMethodBackup != null) {
                String value2 = hookMethodBackup.value();
                Class<?>[] parseMethodPars2 = parseMethodPars(classLoader, method);
                try {
                    Member constructor2 = value2.equals("<init>") ? cls.getConstructor(parseMethodPars2) : cls.getDeclaredMethod(value2, parseMethodPars2);
                    if (!method.isAnnotationPresent(SkipParamCheck.class)) {
                        checkSignature(constructor2, method, parseMethodPars2);
                    }
                    HookEntity hookEntity2 = (HookEntity) hashMap.get(constructor2);
                    if (hookEntity2 == null) {
                        hookEntity2 = new HookEntity(constructor2);
                        hashMap.put(constructor2, hookEntity2);
                    }
                    hookEntity2.pars = parseMethodPars2;
                    hookEntity2.backup = method;
                } catch (NoSuchMethodException e2) {
                    throw new HookErrorException("can not find target method: " + value2, e2);
                }
            }
        }
        return hashMap;
    }

    private static int getParsCount(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null) {
            return 0;
        }
        return parameterTypes.length;
    }

    private static Class getRealParType(ClassLoader classLoader, Class cls, Annotation[] annotationArr, boolean z) throws Exception {
        if (annotationArr == null || annotationArr.length == 0) {
            return cls;
        }
        for (Annotation annotation : annotationArr) {
            if (annotation instanceof Param) {
                Param param = (Param) annotation;
                if (TextUtils.isEmpty(param.value())) {
                    return cls;
                }
                Class<?> classNameToClass = classNameToClass(param.value(), classLoader);
                if (z || classNameToClass.equals(cls) || cls.isAssignableFrom(classNameToClass)) {
                    return classNameToClass;
                }
                throw new ClassCastException("hook method par cast error!");
            }
        }
        return cls;
    }

    private static Class getTargetHookClass(ClassLoader classLoader, Class<?> cls) {
        HookClass hookClass = (HookClass) cls.getAnnotation(HookClass.class);
        HookReflectClass hookReflectClass = (HookReflectClass) cls.getAnnotation(HookReflectClass.class);
        if (hookClass != null) {
            return hookClass.value();
        }
        if (hookReflectClass == null) {
            return null;
        }
        if (classLoader != null) {
            try {
                return Class.forName(hookReflectClass.value(), true, classLoader);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            return Class.forName(hookReflectClass.value());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static boolean hasThisObject(Method method) {
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return false;
        }
        return isThisObject(parameterAnnotations[0]);
    }

    private static boolean isThisObject(Annotation[] annotationArr) {
        if (annotationArr == null || annotationArr.length == 0) {
            return false;
        }
        for (Annotation annotation : annotationArr) {
            if (annotation instanceof ThisObject) {
                return true;
            }
        }
        return false;
    }

    private static Class[] parseMethodPars(ClassLoader classLoader, Field field) throws HookErrorException {
        MethodParams methodParams = (MethodParams) field.getAnnotation(MethodParams.class);
        MethodReflectParams methodReflectParams = (MethodReflectParams) field.getAnnotation(MethodReflectParams.class);
        if (methodParams != null) {
            return methodParams.value();
        }
        if (methodReflectParams == null || methodReflectParams.value().length == 0) {
            return null;
        }
        Class[] clsArr = new Class[methodReflectParams.value().length];
        for (int i = 0; i < methodReflectParams.value().length; i++) {
            try {
                clsArr[i] = classNameToClass(methodReflectParams.value()[i], classLoader);
            } catch (ClassNotFoundException e) {
                throw new HookErrorException("hook method pars error: " + field.getName(), e);
            }
        }
        return clsArr;
    }

    private static Class[] parseMethodPars(ClassLoader classLoader, Method method) throws HookErrorException {
        MethodParams methodParams = (MethodParams) method.getAnnotation(MethodParams.class);
        MethodReflectParams methodReflectParams = (MethodReflectParams) method.getAnnotation(MethodReflectParams.class);
        if (methodParams != null) {
            return methodParams.value();
        }
        if (methodReflectParams != null) {
            if (methodReflectParams.value().length == 0) {
                return null;
            }
            Class[] clsArr = new Class[methodReflectParams.value().length];
            for (int i = 0; i < methodReflectParams.value().length; i++) {
                try {
                    clsArr[i] = classNameToClass(methodReflectParams.value()[i], classLoader);
                } catch (ClassNotFoundException e) {
                    throw new HookErrorException("hook method pars error: " + method.getName(), e);
                }
            }
            return clsArr;
        } else if (getParsCount(method) <= 0) {
            return null;
        } else {
            if (getParsCount(method) != 1) {
                return parseMethodParsNew(classLoader, method);
            }
            if (hasThisObject(method)) {
                return parseMethodParsNew(classLoader, method);
            }
            return null;
        }
    }

    private static Class[] parseMethodParsNew(ClassLoader classLoader, Method method) throws HookErrorException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null || parameterTypes.length == 0) {
            return null;
        }
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Class[] clsArr = null;
        int i = 0;
        for (int i2 = 0; i2 < parameterAnnotations.length; i2++) {
            Class<?> cls = parameterTypes[i2];
            Annotation[] annotationArr = parameterAnnotations[i2];
            if (i2 == 0) {
                if (isThisObject(annotationArr)) {
                    clsArr = new Class[(parameterAnnotations.length - 1)];
                } else {
                    clsArr = new Class[parameterAnnotations.length];
                }
            }
            try {
                clsArr[i] = getRealParType(classLoader, cls, annotationArr, method.isAnnotationPresent(SkipParamCheck.class));
                i++;
            } catch (Exception e) {
                throw new HookErrorException("hook method <" + method.getName() + "> parser pars error", e);
            }
        }
        return clsArr;
    }

    private static boolean samePars(ClassLoader classLoader, Field field, Class[] clsArr) {
        try {
            Class[] parseMethodPars = parseMethodPars(classLoader, field);
            if (parseMethodPars == null && field.isAnnotationPresent(SkipParamCheck.class)) {
                return true;
            }
            if (clsArr == null) {
                clsArr = new Class[0];
            }
            if (parseMethodPars == null) {
                parseMethodPars = new Class[0];
            }
            if (clsArr.length != parseMethodPars.length) {
                return false;
            }
            for (int i = 0; i < clsArr.length; i++) {
                if (clsArr[i] != parseMethodPars[i]) {
                    return false;
                }
            }
            return true;
        } catch (HookErrorException e) {
            return false;
        }
    }
}
