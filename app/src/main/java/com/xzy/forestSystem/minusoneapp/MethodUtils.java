package com.xzy.forestSystem.minusoneapp;

import java.lang.reflect.Method;

public class MethodUtils {
    static {
        try {
            Object staticField = FieldUtils.getStaticField(Class.forName("sun.misc.Unsafe"), "theUnsafe");
            try {
                Class<?> cls = Class.forName("com.xzy.forestSystem.minusoneapp.MethodUtils");
                try {
                    long longValue = ((Long) callMethod(staticField, "objectFieldOffset", new Class[]{Class.forName("java.lang.reflect.Field")}, cls.getClass().getDeclaredField("module"))).longValue();
                    try {
                        Object callMethod = callMethod(Class.forName("java.lang.Object"), "getModule", new Object[0]);
                        try {
                            callMethod(staticField, "putObject", new Class[]{Class.forName("java.lang.Object"), Long.TYPE, Class.forName("java.lang.Object")}, cls, new Long(longValue), callMethod);
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

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:57)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:15)
        */
    /* JADX WARNING: Removed duplicated region for block: B:128:0x0154 A[EDGE_INSN: B:128:0x0154->B:97:0x0154 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00a7  */
    public static <T> T callMethod(Object r9, Class<?> r10, String r11, Class<?>[] r12, Object... r13) {
        /*
        // Method dump skipped, instructions count: 391
        */
        throw new UnsupportedOperationException("Method not decompiled: com.minusoneapp.MethodUtils.callMethod(java.lang.Object, java.lang.Class, java.lang.String, java.lang.Class[], java.lang.Object[]):java.lang.Object");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:57)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:15)
        */
    /* JADX WARNING: Removed duplicated region for block: B:125:0x0150 A[EDGE_INSN: B:125:0x0150->B:94:0x0150 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00a3  */
    public static <T> T callMethod(Object r9, Class<?> r10, String r11, Object... r12) {
        /*
        // Method dump skipped, instructions count: 387
        */
        throw new UnsupportedOperationException("Method not decompiled: com.minusoneapp.MethodUtils.callMethod(java.lang.Object, java.lang.Class, java.lang.String, java.lang.Object[]):java.lang.Object");
    }

    public static <T> T callMethod(Object obj, String str, Class<?>[] clsArr, Object... objArr) {
        return (T) callMethod(obj, null, str, clsArr, objArr);
    }

    public static <T> T callMethod(Object obj, String str, Object... objArr) {
        return (T) callMethod(obj, (Class<?>) null, str, objArr);
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:57)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:15)
        */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x0151 A[EDGE_INSN: B:133:0x0151->B:98:0x0151 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00a4  */
    public static <T> T callStaticMethod(Class<?> r9, Class<?> r10, String r11, Class<?>[] r12, Object... r13) {
        /*
        // Method dump skipped, instructions count: 418
        */
        throw new UnsupportedOperationException("Method not decompiled: com.minusoneapp.MethodUtils.callStaticMethod(java.lang.Class, java.lang.Class, java.lang.String, java.lang.Class[], java.lang.Object[]):java.lang.Object");
    }

    /*  JADX ERROR: JadxOverflowException in pass: RegionMakerVisitor
        jadx.core.utils.exceptions.JadxOverflowException: Regions count limit reached
        	at jadx.core.utils.ErrorsCounter.addError(ErrorsCounter.java:57)
        	at jadx.core.utils.ErrorsCounter.error(ErrorsCounter.java:31)
        	at jadx.core.dex.attributes.nodes.NotificationAttrNode.addError(NotificationAttrNode.java:15)
        */
    /* JADX WARNING: Removed duplicated region for block: B:130:0x014d A[EDGE_INSN: B:130:0x014d->B:95:0x014d ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00a0  */
    public static <T> T callStaticMethod(Class<?> r9, Class<?> r10, String r11, Object... r12) {
        /*
        // Method dump skipped, instructions count: 414
        */
        throw new UnsupportedOperationException("Method not decompiled: com.minusoneapp.MethodUtils.callStaticMethod(java.lang.Class, java.lang.Class, java.lang.String, java.lang.Object[]):java.lang.Object");
    }

    public static <T> T callStaticMethod(Class<?> cls, String str, Class<?>[] clsArr, Object... objArr) {
        return (T) callStaticMethod(cls, null, str, clsArr, objArr);
    }

    public static <T> T callStaticMethod(Class<?> cls, String str, Object... objArr) {
        return (T) callStaticMethod(cls, (Class<?>) null, str, objArr);
    }

    private static boolean equals(Class<?>[] clsArr, Class<?>[] clsArr2) {
        boolean z = clsArr.length == clsArr2.length;
        for (int i = 0; i < Math.min(clsArr.length, clsArr2.length); i++) {
            z &= clsArr[i].equals(clsArr2[i]);
        }
        return z;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0036, code lost:
        if (r4.getClass() != java.lang.Class.forName("java.lang.Byte")) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0054, code lost:
        if (r4.getClass() != java.lang.Class.forName("java.lang.Double")) goto L_0x0062;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:36:0x0072, code lost:
        if (r4.getClass() != java.lang.Class.forName("java.lang.Character")) goto L_0x0080;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0090, code lost:
        if (r4.getClass() != java.lang.Class.forName("java.lang.Short")) goto L_0x009e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00ae, code lost:
        if (r4.getClass() != java.lang.Class.forName("java.lang.Integer")) goto L_0x00bc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00cc, code lost:
        if (r4.getClass() != java.lang.Class.forName("java.lang.Float")) goto L_0x00da;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x00ea, code lost:
        if (r4.getClass() != java.lang.Class.forName("java.lang.Long")) goto L_0x00f8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x0108, code lost:
        if (r4.getClass() != java.lang.Class.forName("java.lang.Boolean")) goto L_0x0116;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean equals(Class<?>[] r7, Object[] r8) {
        /*
        // Method dump skipped, instructions count: 284
        */
        throw new UnsupportedOperationException("Method not decompiled: com.minusoneapp.MethodUtils.equals(java.lang.Class[], java.lang.Object[]):boolean");
    }

    private static Method[] getDeclaredMethods(Class<?> cls) throws Throwable {
        try {
            return (Method[]) Class.forName("java.lang.Class").getMethod("getDeclaredMethods", new Class[0]).invoke(cls, new Object[0]);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    private static Method[] getMethods(Class<?> cls) throws Throwable {
        try {
            return (Method[]) Class.forName("java.lang.Class").getMethod("getMethods", new Class[0]).invoke(cls, new Object[0]);
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }
}
