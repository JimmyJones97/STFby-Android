package  com.xzy.forestSystem.qihoo.jiagu;

/* renamed from:  com.xzy.forestSystem.qihoo.jiagu.d */
public class C0243d {
    /* renamed from: a */
    public static void m31a(String str) {
        try {
            if (Runtime.getRuntime().exec(str).waitFor() != 0) {
                new StringBuilder("Failed to execute cmd:").append(str);
            }
        } catch (Exception e) {
        }
    }
}
