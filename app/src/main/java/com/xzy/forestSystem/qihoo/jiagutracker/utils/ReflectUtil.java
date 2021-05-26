package  com.xzy.forestSystem.qihoo.jiagutracker.utils;

import  com.xzy.forestSystem.qihoo.jiagutracker.QVMProtect;
import  com.xzy.forestSystem.stub.StubApp;

@QVMProtect
public class ReflectUtil {
    static {
        StubApp.interface11(3792);
    }

    public static native Object getField(Class<?> cls, String str, Object obj);

    private static native Object invokeMethod(Class<?> cls, String str, Object obj, Class<?>[] clsArr, Object[] objArr);

    public static native Object invokeStaticMethod(String str, String str2) throws ClassNotFoundException;
}
