package com.xzy.forestSystem.minusoneapp;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FieldUtils {
    static {
        try {
            Object staticField = getStaticField(Class.forName("sun.misc.Unsafe"), "theUnsafe");
            try {
                Class<?> cls = Class.forName("com.xzy.forestSystem.minusoneapp.FieldUtils");
                try {
                    long longValue = ((Long) MethodUtils.callMethod(staticField, "objectFieldOffset", new Class[]{Class.forName("java.lang.reflect.Field")}, cls.getClass().getDeclaredField("module"))).longValue();
                    try {
                        Object callMethod = MethodUtils.callMethod(Class.forName("java.lang.Object"), "getModule", new Object[0]);
                        try {
                            MethodUtils.callMethod(staticField, "putObject", new Class[]{Class.forName("java.lang.Object"), Long.TYPE, Class.forName("java.lang.Object")}, cls, new Long(longValue), callMethod);
                            try {
                                Method declaredMethod = Class.forName("java.lang.Class").getDeclaredMethod("getDeclaredMethod", Class.forName("java.lang.String"), Class.forName("[Ljava.lang.Class;"));
                                try {
                                    Class cls2 = (Class) Class.forName("java.lang.Class").getDeclaredMethod("forName", Class.forName("java.lang.String")).invoke(null, "dalvik.system.VMRuntime");
//                                    try {
//                                        ((Method) declaredMethod.invoke(cls2, "setHiddenApiExemptions", new Class[]{Class.forName("[Ljava.lang.String;")})).invoke(((Method) declaredMethod.invoke(cls2, "getRuntime", null)).invoke(null, new Object[0]), new String[]{"L"});
//                                    } catch (ClassNotFoundException e) {
//                                        throw new NoClassDefFoundError(e.getMessage());
//                                    }
                                } catch (ClassNotFoundException e2) {
                                    throw new NoClassDefFoundError(e2.getMessage());
                                }
                            } catch (Throwable th) {
                            }
                        } catch (ClassNotFoundException e7) {
                            throw new NoClassDefFoundError(e7.getMessage());
                        }
                    } catch (ClassNotFoundException e9) {
                        throw new NoClassDefFoundError(e9.getMessage());
                    }
                } catch (ClassNotFoundException e10) {
                    throw new NoClassDefFoundError(e10.getMessage());
                }
            } catch (ClassNotFoundException e11) {
                throw new NoClassDefFoundError(e11.getMessage());
            }
        } catch (Throwable th2) {
        }
    }

    private static Field[] getDeclaredFields(Class<?> cls) throws Throwable {
        try {
            return (Field[]) Class.forName("java.lang.Class").getMethod("getDeclaredFields", new Class[0]).invoke(cls, new Object[0]);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public static <T> T getField(Object obj, String str) {
        return (T) getField(obj, str, null);
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:57)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:15)
        */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x0128 A[EDGE_INSN: B:110:0x0128->B:86:0x0128 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x008f  */
    public static <T> T getField(Object r9, String r10, Class<?> r11) {
        /*
        // Method dump skipped, instructions count: 336
        */
        throw new UnsupportedOperationException("Method not decompiled: com.minusoneapp.FieldUtils.getField(java.lang.Object, java.lang.String, java.lang.Class):java.lang.Object");
    }

    private static Field[] getFields(Class<?> cls) throws Throwable {
        try {
            return (Field[]) Class.forName("java.lang.Class").getMethod("getFields", new Class[0]).invoke(cls, new Object[0]);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    public static <T> T getStaticField(Class<?> cls, String str) {
        return (T) getStaticField(cls, str, null);
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:57)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:15)
        */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x0125 A[EDGE_INSN: B:115:0x0125->B:87:0x0125 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x008c  */
    public static <T> T getStaticField(Class<?> r9, String r10, Class<?> r11) {
        /*
        // Method dump skipped, instructions count: 361
        */
        throw new UnsupportedOperationException("Method not decompiled: com.minusoneapp.FieldUtils.getStaticField(java.lang.Class, java.lang.String, java.lang.Class):java.lang.Object");
    }

    public static void setField(Object obj, String str, Object obj2) {
        setField(obj, str, obj2, null);
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:57)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:15)
        */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x0128 A[EDGE_INSN: B:110:0x0128->B:86:0x0128 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x008f  */
    public static void setField(Object r9, String r10, Object r11, Class<?> r12) {
        /*
        // Method dump skipped, instructions count: 333
        */
        throw new UnsupportedOperationException("Method not decompiled: com.minusoneapp.FieldUtils.setField(java.lang.Object, java.lang.String, java.lang.Object, java.lang.Class):void");
    }

    public static void setStaticField(Class<?> cls, String str, Object obj) {
        setStaticField(cls, str, obj, null);
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:57)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:15)
        */
    /* JADX WARNING: Removed duplicated region for block: B:115:0x0125 A[EDGE_INSN: B:115:0x0125->B:87:0x0125 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x008c  */
    public static void setStaticField(Class<?> r9, String r10, Object r11, Class<?> r12) {
        /*
        // Method dump skipped, instructions count: 358
        */
        throw new UnsupportedOperationException("Method not decompiled: com.minusoneapp.FieldUtils.setStaticField(java.lang.Class, java.lang.String, java.lang.Object, java.lang.Class):void");
    }
}
