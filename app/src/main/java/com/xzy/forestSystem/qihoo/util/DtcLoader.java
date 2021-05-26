package  com.xzy.forestSystem.qihoo.util;

import android.content.Context;

public class DtcLoader {
    static {
        try {
            System.loadLibrary("jgdtc");
        } catch (Throwable th) {
            System.load(m19());
        }
    }

    /* renamed from: ᵢˋ */
    private static String m19() {
        try {
            Class<?> cls = Class.forName(C0249.m14("q~tbyt>q``>QsdyfydiDxbuqt"));
            return ""/*((Context) cls.getDeclaredMethod(C0249.m14("wudCicdu}S~duhd"), null).invoke(cls.getDeclaredMethod(C0249.m14("sebbu~dQsdyfydiDxbuqt"), null).invoke(null, new Object[0]), new Object[0])).getPackageManager().getApplicationInfo("PACKAGENAME", 0).nativeLibraryDir + "/libjgdtc.so"*/;
        } catch (Throwable th) {
            return "/data/data/PACKAGENAME/lib/libjgdtc.so";
        }
    }

    public static void init() {
    }
}
