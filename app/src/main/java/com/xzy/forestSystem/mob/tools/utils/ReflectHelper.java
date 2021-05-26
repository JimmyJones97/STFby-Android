package com.xzy.forestSystem.mob.tools.utils;

import  com.xzy.forestSystem.mob.tools.gui.CachePool;
import com.xzy.forestSystem.swift.sandhook.annotation.MethodReflectParams;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class ReflectHelper {
    private static CachePool<String, Constructor<?>> cachedConstr = new CachePool<>(5);
    private static CachePool<String, Method> cachedMethod = new CachePool<>(25);
    private static HashMap<String, Class<?>> classMap = new HashMap<>();
    private static HashMap<Class<?>, String> nameMap = new HashMap<>();
    private static HashSet<String> packageSet = new HashSet<>();

    public interface ReflectRunnable {
        Object run(Object obj);
    }

    static {
        packageSet.add("java.lang");
        packageSet.add("java.io");
        packageSet.add("java.nio");
        packageSet.add("java.net");
        packageSet.add("java.util");
        packageSet.add(" com.xzy.forestSystem.mob.tools");
        packageSet.add(" com.xzy.forestSystem.mob.tools.gui");
        packageSet.add(" com.xzy.forestSystem.mob.tools.log");
        packageSet.add(" com.xzy.forestSystem.mob.tools.network");
        packageSet.add(" com.xzy.forestSystem.mob.tools.utils");
        classMap.put(MethodReflectParams.DOUBLE, Double.TYPE);
        classMap.put(MethodReflectParams.FLOAT, Float.TYPE);
        classMap.put(MethodReflectParams.LONG, Long.TYPE);
        classMap.put(MethodReflectParams.INT, Integer.TYPE);
        classMap.put(MethodReflectParams.SHORT, Short.TYPE);
        classMap.put(MethodReflectParams.BYTE, Byte.TYPE);
        classMap.put(MethodReflectParams.CHAR, Character.TYPE);
        classMap.put(MethodReflectParams.BOOLEAN, Boolean.TYPE);
        classMap.put("Object", Object.class);
        classMap.put("String", String.class);
        classMap.put("Thread", Thread.class);
        classMap.put("Runnable", Runnable.class);
        classMap.put("System", System.class);
        classMap.put(MethodReflectParams.DOUBLE, Double.class);
        classMap.put("Float", Float.class);
        classMap.put("Long", Long.class);
        classMap.put("Integer", Integer.class);
        classMap.put("Short", Short.class);
        classMap.put("Byte", Byte.class);
        classMap.put("Character", Character.class);
        classMap.put("Boolean", Boolean.class);
        for (Map.Entry<String, Class<?>> ent : classMap.entrySet()) {
            nameMap.put(ent.getValue(), ent.getKey());
        }
    }

    public static String importClass(String className) throws Throwable {
        return importClass(null, className);
    }

    public static synchronized String importClass(String name, String className) throws Throwable {
        synchronized (ReflectHelper.class) {
            if (className.endsWith(".*")) {
                packageSet.add(className.substring(0, className.length() - 2));
                name = "*";
            } else {
                Class<?> clz = Class.forName(className);
                if (name == null) {
                    name = clz.getSimpleName();
                }
                classMap.put(name, clz);
                nameMap.put(clz, name);
            }
        }
        return name;
    }

    private static synchronized Class<?> getImportedClass(String className) {
        Class<?> clz;
        synchronized (ReflectHelper.class) {
            clz = classMap.get(className);
            if (clz == null) {
                Iterator<String> it = packageSet.iterator();
                while (it.hasNext()) {
                    try {
                        importClass(it.next() + FileSelector_Dialog.sFolder + className);
                    } catch (Throwable th) {
                    }
                    clz = classMap.get(className);
                    if (clz != null) {
                        break;
                    }
                }
            }
        }
        return clz;
    }

    private static Class<?>[] getTypes(Object[] args) {
        Class<?>[] types = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            types[i] = args[i] == null ? null : args[i].getClass();
        }
        return types;
    }

    private static boolean primitiveEquals(Class<?> primitive, Class<?> target) {
        return (primitive == Byte.TYPE && target == Byte.class) || (primitive == Short.TYPE && (target == Short.class || target == Byte.class || target == Character.class)) || ((primitive == Character.TYPE && (target == Character.class || target == Short.class || target == Byte.class)) || ((primitive == Integer.TYPE && (target == Integer.class || target == Short.class || target == Byte.class || target == Character.class)) || ((primitive == Long.TYPE && (target == Long.class || target == Integer.class || target == Short.class || target == Byte.class || target == Character.class)) || ((primitive == Float.TYPE && (target == Float.class || target == Long.class || target == Integer.class || target == Short.class || target == Byte.class || target == Character.class)) || ((primitive == Double.TYPE && (target == Double.class || target == Float.class || target == Long.class || target == Integer.class || target == Short.class || target == Byte.class || target == Character.class)) || (primitive == Boolean.TYPE && target == Boolean.class))))));
    }

    private static boolean matchParams(Class<?>[] mTypes, Class<?>[] types) {
        if (mTypes.length != types.length) {
            return false;
        }
        for (int i = 0; i < types.length; i++) {
            if (!(types[i] == null || primitiveEquals(mTypes[i], types[i]) || mTypes[i].isAssignableFrom(types[i]))) {
                return false;
            }
        }
        return true;
    }

    private static boolean tryMatchParams(Class<?>[] mTypes, Class<?>[] types) {
        if (mTypes.length - types.length != 1) {
            return false;
        }
        boolean match = true;
        int i = 0;
        while (true) {
            if (i < types.length) {
                if (types[i] != null && !primitiveEquals(mTypes[i], types[i]) && !mTypes[i].isAssignableFrom(types[i])) {
                    match = false;
                    break;
                }
                i++;
            } else {
                break;
            }
        }
        return match && mTypes[mTypes.length + -1].isArray();
    }

    public static Object newInstance(String className, Object... args) throws Throwable {
        try {
            return onNewInstance(className, args);
        } catch (Throwable t) {
            if (t instanceof NoSuchMethodException) {
                throw t;
            }
            throw new Throwable("className: " + className + ", methodName: <init>", t);
        }
    }

    private static Object onNewInstance(String className, Object... args) throws Throwable {
        if (className.startsWith("[")) {
            return newArray(className, args);
        }
        String mthSign = className + "#" + args.length;
        Constructor<?> con = cachedConstr.get(mthSign);
        Class<?>[] types = getTypes(args);
        if (con == null || !matchParams(con.getParameterTypes(), types)) {
            Constructor<?>[] cons = getImportedClass(className).getDeclaredConstructors();
            ArrayList<Constructor<?>> overloads = new ArrayList<>();
            ArrayList<Class<?>[]> paramsTypes = new ArrayList<>();
            int length = cons.length;
            for (int i = 0; i < length; i++) {
                Constructor<?> c = cons[i];
                Class<?>[] paramTypes = c.getParameterTypes();
                if (matchParams(paramTypes, types)) {
                    cachedConstr.put(mthSign, c);
                    c.setAccessible(true);
                    return c.newInstance(args);
                }
                if (paramTypes.length > 0 && paramTypes[paramTypes.length - 1].isArray() && types.length >= paramTypes.length - 1) {
                    overloads.add(c);
                    paramsTypes.add(paramTypes);
                }
            }
            for (int i2 = 0; i2 < paramsTypes.size(); i2++) {
                Class<?>[] paramTypes2 = paramsTypes.get(i2);
                Class<?> componentType = paramTypes2[paramTypes2.length - 1].getComponentType();
                if (tryMatchParams(paramTypes2, types)) {
                    Object[] newArgs = new Object[(args.length + 1)];
                    System.arraycopy(args, 0, newArgs, 0, args.length);
                    newArgs[args.length] = Array.newInstance(componentType, 0);
                    Constructor<?> c2 = overloads.get(i2);
                    c2.setAccessible(true);
                    return c2.newInstance(args);
                }
                boolean isElement = true;
                int t = paramTypes2.length - 1;
                while (true) {
                    if (t >= types.length) {
                        break;
                    } else if (!types[t].equals(componentType)) {
                        isElement = false;
                        break;
                    } else {
                        t++;
                    }
                }
                if (isElement) {
                    int arrLen = (types.length - paramTypes2.length) + 1;
                    Object arr = Array.newInstance(componentType, arrLen);
                    for (int e = 0; e < arrLen; e++) {
                        Array.set(arr, e, args[(paramTypes2.length - 1) + e]);
                    }
                    Object[] newArgs2 = new Object[(args.length + 1)];
                    System.arraycopy(args, 0, newArgs2, 0, args.length);
                    newArgs2[args.length] = arr;
                    Constructor<?> c3 = overloads.get(i2);
                    c3.setAccessible(true);
                    return c3.newInstance(args);
                }
            }
            throw new NoSuchMethodException("className: " + className + ", methodName: <init>");
        }
        con.setAccessible(true);
        return con.newInstance(args);
    }

    private static Object newArray(String className, Object... args) throws Throwable {
        Class<?> eleClz;
        String tmp = className;
        int dimension = 0;
        while (tmp.startsWith("[")) {
            dimension++;
            tmp = tmp.substring(1);
        }
        int[] lens = null;
        if (dimension == args.length) {
            lens = new int[dimension];
            for (int i = 0; i < dimension; i++) {
                try {
                    lens[i] = Integer.parseInt(String.valueOf(args[i]));
                } catch (Throwable th) {
                    lens = null;
                }
            }
        }
        if (lens != null) {
            if ("B".equals(tmp)) {
                eleClz = Byte.TYPE;
            } else if ("S".equals(tmp)) {
                eleClz = Short.TYPE;
            } else if ("I".equals(tmp)) {
                eleClz = Integer.TYPE;
            } else if ("J".equals(tmp)) {
                eleClz = Long.TYPE;
            } else if ("F".equals(tmp)) {
                eleClz = Float.TYPE;
            } else if ("D".equals(tmp)) {
                eleClz = Double.TYPE;
            } else if ("Z".equals(tmp)) {
                eleClz = Boolean.TYPE;
            } else if ("C".equals(tmp)) {
                eleClz = Character.TYPE;
            } else {
                eleClz = getImportedClass(tmp);
            }
            if (eleClz != null) {
                return Array.newInstance(eleClz, lens);
            }
        }
        throw new NoSuchMethodException("className: [" + className + ", methodName: <init>");
    }

    public static <T> T invokeStaticMethod(String className, String methodName, Object... args) throws Throwable {
        try {
            return (T) invokeMethod(className, null, methodName, args);
        } catch (Throwable t) {
            if (t instanceof NoSuchMethodException) {
                throw t;
            }
            throw new Throwable("className: " + className + ", methodName: " + methodName, t);
        }
    }

    private static <T> T invokeMethod(String className, Object receiver, String methodName, Object... args) throws Throwable {
        Class<?> clz;
        if (receiver == null) {
            clz = getImportedClass(className);
        } else {
            clz = receiver.getClass();
        }
        String mthSign = clz.getName() + "#" + methodName + "#" + args.length;
        Method mth = cachedMethod.get(mthSign);
        Class<?>[] types = getTypes(args);
        if (mth != null) {
            boolean isReqStatic = Modifier.isStatic(mth.getModifiers());
            if ((receiver == null ? isReqStatic : !isReqStatic) && matchParams(mth.getParameterTypes(), types)) {
                mth.setAccessible(true);
                if (mth.getReturnType() != Void.TYPE) {
                    return (T) mth.invoke(receiver, args);
                }
                mth.invoke(receiver, args);
                return null;
            }
        }
        ArrayList<Class<?>> clzs = new ArrayList<>();
        while (clz != null) {
            clzs.add(clz);
            clz = clz.getSuperclass();
        }
        ArrayList<Method> overloads = new ArrayList<>();
        ArrayList<Class<?>[]> paramsTypes = new ArrayList<>();
        Iterator<Class<?>> it = clzs.iterator();
        while (it.hasNext()) {
            Method[] mths = it.next().getDeclaredMethods();
            int length = mths.length;
            int i = 0;
            while (true) {
                if (i < length) {
                    Method m = mths[i];
                    boolean isStatic = Modifier.isStatic(m.getModifiers());
                    boolean modifier = receiver == null ? isStatic : !isStatic;
                    if (m.getName().equals(methodName) && modifier) {
                        Class<?>[] paramTypes = m.getParameterTypes();
                        if (matchParams(paramTypes, types)) {
                            cachedMethod.put(mthSign, m);
                            m.setAccessible(true);
                            if (m.getReturnType() != Void.TYPE) {
                                return (T) m.invoke(receiver, args);
                            }
                            m.invoke(receiver, args);
                            return null;
                        } else if (paramTypes.length > 0 && paramTypes[paramTypes.length - 1].isArray() && types.length >= paramTypes.length - 1) {
                            overloads.add(m);
                            paramsTypes.add(paramTypes);
                        }
                    }
                    i++;
                }
            }
        }
        for (int i2 = 0; i2 < paramsTypes.size(); i2++) {
            Class<?>[] paramTypes2 = paramsTypes.get(i2);
            Class<?> componentType = paramTypes2[paramTypes2.length - 1].getComponentType();
            if (tryMatchParams(paramTypes2, types)) {
                Object[] newArgs = new Object[(args.length + 1)];
                System.arraycopy(args, 0, newArgs, 0, args.length);
                newArgs[args.length] = Array.newInstance(componentType, 0);
                Method m2 = overloads.get(i2);
                m2.setAccessible(true);
                if (m2.getReturnType() != Void.TYPE) {
                    return (T) m2.invoke(receiver, newArgs);
                }
                m2.invoke(receiver, newArgs);
                return null;
            }
            boolean isElement = true;
            int t = paramTypes2.length - 1;
            while (true) {
                if (t >= types.length) {
                    break;
                } else if (!types[t].equals(componentType)) {
                    isElement = false;
                    break;
                } else {
                    t++;
                }
            }
            if (isElement) {
                int arrLen = (types.length - paramTypes2.length) + 1;
                Object arr = Array.newInstance(componentType, arrLen);
                for (int e = 0; e < arrLen; e++) {
                    Array.set(arr, e, args[(paramTypes2.length - 1) + e]);
                }
                Object[] newArgs2 = new Object[paramTypes2.length];
                System.arraycopy(args, 0, newArgs2, 0, paramTypes2.length - 1);
                newArgs2[paramTypes2.length - 1] = arr;
                Method m3 = overloads.get(i2);
                m3.setAccessible(true);
                if (m3.getReturnType() != Void.TYPE) {
                    return (T) m3.invoke(receiver, newArgs2);
                }
                m3.invoke(receiver, newArgs2);
                return null;
            }
        }
        throw new NoSuchMethodException("className: " + receiver.getClass() + ", methodName: " + methodName);
    }

    public static <T> T invokeInstanceMethod(Object receiver, String methodName, Object... args) throws Throwable {
        try {
            return (T) invokeMethod(null, receiver, methodName, args);
        } catch (Throwable t) {
            if (t instanceof NoSuchMethodException) {
                throw t;
            }
            throw new Throwable("className: " + receiver.getClass() + ", methodName: " + methodName, t);
        }
    }

    public static <T> T getStaticField(String className, String fieldName) throws Throwable {
        try {
            return (T) onGetStaticField(className, fieldName);
        } catch (Throwable t) {
            if (t instanceof NoSuchFieldException) {
                throw t;
            }
            throw new Throwable("className: " + className + ", fieldName: " + fieldName, t);
        }
    }

    private static <T> T onGetStaticField(String className, String fieldName) throws Throwable {
        ArrayList<Class<?>> clzs = new ArrayList<>();
        for (Class<?> clz = getImportedClass(className); clz != null; clz = clz.getSuperclass()) {
            clzs.add(clz);
        }
        Iterator<Class<?>> it = clzs.iterator();
        while (it.hasNext()) {
            Field fld = null;
            try {
                fld = it.next().getDeclaredField(fieldName);
            } catch (Throwable th) {
            }
            if (fld != null && Modifier.isStatic(fld.getModifiers())) {
                fld.setAccessible(true);
                return (T) fld.get(null);
            }
        }
        throw new NoSuchFieldException("className: " + className + ", fieldName: " + fieldName);
    }

    public static void setStaticField(String className, String fieldName, Object value) throws Throwable {
        try {
            onSetStaticField(className, fieldName, value);
        } catch (Throwable t) {
            if (t instanceof NoSuchFieldException) {
                throw t;
            }
            throw new Throwable("className: " + className + ", fieldName: " + fieldName + ", value: " + String.valueOf(value), t);
        }
    }

    private static void onSetStaticField(String className, String fieldName, Object value) throws Throwable {
        ArrayList<Class<?>> clzs = new ArrayList<>();
        for (Class<?> clz = getImportedClass(className); clz != null; clz = clz.getSuperclass()) {
            clzs.add(clz);
        }
        Iterator<Class<?>> it = clzs.iterator();
        while (it.hasNext()) {
            Field fld = null;
            try {
                fld = it.next().getDeclaredField(fieldName);
            } catch (Throwable th) {
            }
            if (fld != null && Modifier.isStatic(fld.getModifiers())) {
                fld.setAccessible(true);
                fld.set(null, value);
                return;
            }
        }
        throw new NoSuchFieldException("className: " + className + ", fieldName: " + fieldName + ", value: " + String.valueOf(value));
    }

    public static <T> T getInstanceField(Object receiver, String fieldName) throws Throwable {
        try {
            return (T) onGetInstanceField(receiver, fieldName);
        } catch (Throwable t) {
            if (t instanceof NoSuchFieldException) {
                throw t;
            }
            throw new Throwable("className: " + receiver.getClass() + ", fieldName: " + fieldName, t);
        }
    }

    private static <T> T onGetInstanceField(Object receiver, String fieldName) throws Throwable {
        if (receiver.getClass().isArray()) {
            return (T) onGetElement(receiver, fieldName);
        }
        ArrayList<Class<?>> clzs = new ArrayList<>();
        for (Class<?> clz = receiver.getClass(); clz != null; clz = clz.getSuperclass()) {
            clzs.add(clz);
        }
        Iterator<Class<?>> it = clzs.iterator();
        while (it.hasNext()) {
            Field fld = null;
            try {
                fld = it.next().getDeclaredField(fieldName);
            } catch (Throwable th) {
            }
            if (!(fld == null || Modifier.isStatic(fld.getModifiers()))) {
                fld.setAccessible(true);
                return (T) fld.get(receiver);
            }
        }
        throw new NoSuchFieldException("className: " + receiver.getClass() + ", fieldName: " + fieldName);
    }

    private static Object onGetElement(Object receiver, String fieldName) throws Throwable {
        if ("length".equals(fieldName)) {
            return Integer.valueOf(Array.getLength(receiver));
        }
        if (fieldName.startsWith("[") && fieldName.endsWith("]")) {
            int index = -1;
            try {
                index = Integer.parseInt(fieldName.substring(1, fieldName.length() - 1));
            } catch (Throwable th) {
            }
            if (index != -1) {
                return Array.get(receiver, index);
            }
        }
        throw new NoSuchFieldException("className: " + receiver.getClass() + ", fieldName: " + fieldName);
    }

    public static void setInstanceField(Object receiver, String fieldName, Object value) throws Throwable {
        try {
            onSetInstanceField(receiver, fieldName, value);
        } catch (Throwable t) {
            if (t instanceof NoSuchFieldException) {
                throw t;
            }
            throw new Throwable("className: " + receiver.getClass() + ", fieldName: " + fieldName + ", value: " + String.valueOf(value), t);
        }
    }

    private static void onSetInstanceField(Object receiver, String fieldName, Object value) throws Throwable {
        if (receiver.getClass().isArray()) {
            onSetElement(receiver, fieldName, value);
            return;
        }
        ArrayList<Class<?>> clzs = new ArrayList<>();
        for (Class<?> clz = receiver.getClass(); clz != null; clz = clz.getSuperclass()) {
            clzs.add(clz);
        }
        Iterator<Class<?>> it = clzs.iterator();
        while (it.hasNext()) {
            Field fld = null;
            try {
                fld = it.next().getDeclaredField(fieldName);
            } catch (Throwable th) {
            }
            if (!(fld == null || Modifier.isStatic(fld.getModifiers()))) {
                fld.setAccessible(true);
                fld.set(receiver, value);
                return;
            }
        }
        throw new NoSuchFieldException("className: " + receiver.getClass() + ", fieldName: " + fieldName + ", value: " + String.valueOf(value));
    }

    private static void onSetElement(Object receiver, String fieldName, Object value) throws Throwable {
        if (fieldName.startsWith("[") && fieldName.endsWith("]")) {
            int index = -1;
            try {
                index = Integer.parseInt(fieldName.substring(1, fieldName.length() - 1));
            } catch (Throwable th) {
            }
            if (index != -1) {
                String recClzName = receiver.getClass().getName();
                while (recClzName.startsWith("[")) {
                    recClzName = recClzName.substring(1);
                }
                Class<?> vClass = value.getClass();
                if ("B".equals(recClzName)) {
                    if (vClass == Byte.class) {
                        Array.set(receiver, index, value);
                        return;
                    }
                } else if ("S".equals(recClzName)) {
                    Object sValue = null;
                    if (vClass == Short.class) {
                        sValue = value;
                    } else if (vClass == Byte.class) {
                        sValue = Short.valueOf((short) ((Byte) value).byteValue());
                    }
                    if (sValue != null) {
                        Array.set(receiver, index, sValue);
                        return;
                    }
                } else if ("I".equals(recClzName)) {
                    Object iValue = null;
                    if (vClass == Integer.class) {
                        iValue = value;
                    } else if (vClass == Short.class) {
                        iValue = Integer.valueOf(((Short) value).shortValue());
                    } else if (vClass == Byte.class) {
                        iValue = Integer.valueOf(((Byte) value).byteValue());
                    }
                    if (iValue != null) {
                        Array.set(receiver, index, iValue);
                        return;
                    }
                } else if ("J".equals(recClzName)) {
                    Object jValue = null;
                    if (vClass == Long.class) {
                        jValue = value;
                    } else if (vClass == Integer.class) {
                        jValue = Long.valueOf((long) ((Integer) value).intValue());
                    } else if (vClass == Short.class) {
                        jValue = Long.valueOf((long) ((Short) value).shortValue());
                    } else if (vClass == Byte.class) {
                        jValue = Long.valueOf((long) ((Byte) value).byteValue());
                    }
                    if (jValue != null) {
                        Array.set(receiver, index, jValue);
                        return;
                    }
                } else if ("F".equals(recClzName)) {
                    Object fValue = null;
                    if (vClass == Float.class) {
                        fValue = value;
                    } else if (vClass == Long.class) {
                        fValue = Float.valueOf((float) ((Long) value).longValue());
                    } else if (vClass == Integer.class) {
                        fValue = Float.valueOf((float) ((Integer) value).intValue());
                    } else if (vClass == Short.class) {
                        fValue = Float.valueOf((float) ((Short) value).shortValue());
                    } else if (vClass == Byte.class) {
                        fValue = Float.valueOf((float) ((Byte) value).byteValue());
                    }
                    if (fValue != null) {
                        Array.set(receiver, index, fValue);
                        return;
                    }
                } else if ("D".equals(recClzName)) {
                    Object dValue = null;
                    if (vClass == Double.class) {
                        dValue = value;
                    } else if (vClass == Float.class) {
                        dValue = Double.valueOf((double) ((Float) value).floatValue());
                    } else if (vClass == Long.class) {
                        dValue = Double.valueOf((double) ((Long) value).longValue());
                    } else if (vClass == Integer.class) {
                        dValue = Double.valueOf((double) ((Integer) value).intValue());
                    } else if (vClass == Short.class) {
                        dValue = Double.valueOf((double) ((Short) value).shortValue());
                    } else if (vClass == Byte.class) {
                        dValue = Double.valueOf((double) ((Byte) value).byteValue());
                    }
                    if (dValue != null) {
                        Array.set(receiver, index, dValue);
                        return;
                    }
                } else if ("Z".equals(recClzName)) {
                    if (vClass == Boolean.class) {
                        Array.set(receiver, index, value);
                        return;
                    }
                } else if ("C".equals(recClzName)) {
                    if (vClass == Character.class) {
                        Array.set(receiver, index, value);
                        return;
                    }
                } else if (recClzName.equals(vClass.getName())) {
                    Array.set(receiver, index, value);
                    return;
                }
            }
        }
        throw new NoSuchFieldException("className: " + receiver.getClass() + ", fieldName: " + fieldName + ", value: " + String.valueOf(value));
    }

    public static Class<?> getClass(String name) throws Throwable {
        Class<?> clz = getImportedClass(name);
        if (clz == null) {
            try {
                clz = Class.forName(name);
                if (clz != null) {
                    classMap.put(name, clz);
                }
            } catch (Throwable th) {
            }
        }
        return clz;
    }

    public static String getName(Class<?> clz) throws Throwable {
        String name = nameMap.get(clz);
        if (name != null) {
            return name;
        }
        String name2 = clz.getSimpleName();
        if (classMap.containsKey(name2)) {
            return null;
        }
        classMap.put(name2, clz);
        nameMap.put(clz, name2);
        return name2;
    }

    public static Object createProxy(final HashMap<String, ReflectRunnable> proxyHandler, Class<?>... proxyIntefaces) throws Throwable {
        return Proxy.newProxyInstance(proxyHandler.getClass().getClassLoader(), proxyIntefaces, new InvocationHandler() { // from class:  com.xzy.forestSystem.mob.tools.utils.ReflectHelper.1
            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                ReflectRunnable function = (ReflectRunnable) proxyHandler.get(method.getName());
                if (function != null) {
                    return function.run(args);
                }
                throw new NoSuchMethodException();
            }
        });
    }
}
