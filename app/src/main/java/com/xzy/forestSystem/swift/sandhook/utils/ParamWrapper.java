package com.xzy.forestSystem.swift.sandhook.utils;

import com.xzy.forestSystem.swift.sandhook.SandHook;

public class ParamWrapper {
    private static boolean is64Bit = SandHook.is64Bit();

    public static Object addressToObject(Class cls, long j) {
        return is64Bit ? addressToObject64(cls, j) : addressToObject32(cls, (int) j);
    }

    public static Object addressToObject32(Class cls, int i) {
        if (cls == null) {
            return null;
        }
        if (!cls.isPrimitive()) {
            return SandHook.getObject((long) i);
        }
        if (cls == Integer.TYPE) {
            return Integer.valueOf(i);
        }
        if (cls == Short.TYPE) {
            return Short.valueOf((short) i);
        }
        if (cls == Byte.TYPE) {
            return Byte.valueOf((byte) i);
        }
        if (cls == Character.TYPE) {
            return Character.valueOf((char) i);
        }
        if (cls == Boolean.TYPE) {
            return Boolean.valueOf(i != 0);
        }
        throw new RuntimeException("unknown type: " + cls.toString());
    }

    public static Object addressToObject64(Class cls, long j) {
        if (cls == null) {
            return null;
        }
        if (!cls.isPrimitive()) {
            return SandHook.getObject(j);
        }
        if (cls == Integer.TYPE) {
            return Integer.valueOf((int) j);
        }
        if (cls == Long.TYPE) {
            return Long.valueOf(j);
        }
        if (cls == Short.TYPE) {
            return Short.valueOf((short) ((int) j));
        }
        if (cls == Byte.TYPE) {
            return Byte.valueOf((byte) ((int) j));
        }
        if (cls == Character.TYPE) {
            return Character.valueOf((char) ((int) j));
        }
        if (cls == Boolean.TYPE) {
            return Boolean.valueOf(j != 0);
        }
        throw new RuntimeException("unknown type: " + cls.toString());
    }

    public static long objectToAddress(Class cls, Object obj) {
        return is64Bit ? objectToAddress64(cls, obj) : (long) objectToAddress32(cls, obj);
    }

    public static int objectToAddress32(Class cls, Object obj) {
        if (obj == null) {
            return 0;
        }
        if (!cls.isPrimitive()) {
            return (int) SandHook.getObjectAddress(obj);
        }
        if (cls == Integer.TYPE) {
            return ((Integer) obj).intValue();
        }
        if (cls == Short.TYPE) {
            return ((Short) obj).shortValue();
        }
        if (cls == Byte.TYPE) {
            return ((Byte) obj).byteValue();
        }
        if (cls == Character.TYPE) {
            return ((Character) obj).charValue();
        }
        if (cls == Boolean.TYPE) {
            return Boolean.TRUE.equals(obj) ? 1 : 0;
        }
        throw new RuntimeException("unknown type: " + cls.toString());
    }

    public static long objectToAddress64(Class cls, Object obj) {
        if (obj == null) {
            return 0;
        }
        if (!cls.isPrimitive()) {
            return SandHook.getObjectAddress(obj);
        }
        if (cls == Integer.TYPE) {
            return (long) ((Integer) obj).intValue();
        }
        if (cls == Long.TYPE) {
            return ((Long) obj).longValue();
        }
        if (cls == Short.TYPE) {
            return (long) ((Short) obj).shortValue();
        }
        if (cls == Byte.TYPE) {
            return (long) ((Byte) obj).byteValue();
        }
        if (cls == Character.TYPE) {
            return (long) ((Character) obj).charValue();
        }
        if (cls == Boolean.TYPE) {
            return Boolean.TRUE.equals(obj) ? 1 : 0;
        }
        throw new RuntimeException("unknown type: " + cls.toString());
    }

    public static boolean support(Class cls) {
        return is64Bit ? (cls == Float.TYPE || cls == Double.TYPE) ? false : true : (cls == Float.TYPE || cls == Double.TYPE || cls == Long.TYPE) ? false : true;
    }
}
