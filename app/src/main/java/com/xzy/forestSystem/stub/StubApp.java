package  com.xzy.forestSystem.stub;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import  com.xzy.forestSystem.qihoo.util.C0249;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import dalvik.system.DexFile;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class StubApp extends Application {
    private static boolean loadFromLib = false;
    private static boolean needX86Bridge = false;
    public static String strEntryApplication = "entryRunApplication";

    /* renamed from: ᵢˋ */
    private static Application f438 = null;

    /* renamed from: ᵢˎ */
    private static Application f439 = null;

    /* renamed from: ᵢˏ */
    private static String f440 = "libjiagu";

    /* renamed from: ᵢˑ */
    private static Context f441;

    /* renamed from: ᵢי */
    private static String f442 = null;

    /* renamed from: ᵢـ */
    private static String f443 = null;

    /* renamed from: ᵢٴ */
    private static String f444 = null;

    /* renamed from: ᵢᐧ */
    private static String f445 = null;

    /* renamed from: ᵢᴵ */
    private static String f446 = null;

    /* renamed from: ᵢᵎ */
    private static Map<Integer, String> f447 = new ConcurrentHashMap();

    public static native void interface11(int i);

    public static native Enumeration<String> interface12(DexFile dexFile);

    public static native long interface13(int i, long j, long j2, long j3, int i2, int i3, long j4);

    public static native String interface14(int i);

    public static native AssetFileDescriptor interface17(AssetManager assetManager, String str);

    public static native InputStream interface18(Class cls, String str);

    public static native InputStream interface19(ClassLoader classLoader, String str);

    public static native void interface20();

    public static native void interface21(Application application);

    public static native void interface5(Application application);

    public static native String interface6(String str);

    public static native boolean interface7(Application application, Context context);

    public static native boolean interface8(Application application, Context context);

    public static native Location mark(LocationManager locationManager, String str);

    public static native void mark();

    public static native void mark(Location location);

    public static native synchronized void n0100();

    public static native synchronized void n01010(boolean z);

    public static native synchronized long n0102();

    public static native synchronized void n01020(long j);

    public static native synchronized long n01023132(long j, Object obj, int i, Object obj2);

    public static native synchronized Object n0103();

    public static native synchronized void n01030(Object obj);

    public static native synchronized boolean n01031(Object obj);

    public static native synchronized long n01032(Object obj);

    public static native synchronized Object n01033(Object obj);

    public static native synchronized void n010330(Object obj, Object obj2);

    public static native synchronized void n0103311230(Object obj, Object obj2, boolean z, boolean z2, long j, Object obj3);

    public static native synchronized void n010331130(Object obj, Object obj2, boolean z, boolean z2, Object obj3);

    public static native synchronized Object n010333(Object obj, Object obj2);

    public static native synchronized void n0103330(Object obj, Object obj2, Object obj3);

    public static native synchronized Object n0103333(Object obj, Object obj2, Object obj3);

    public static native void n0110();

    public static native boolean n0111();

    public static native void n01110(boolean z);

    public static native int n01111(int i);

    public static native void n011110(int i, int i2);

    public static native int n011111(int i, boolean z);

    public static native int n0111111(int i, int i2, int i3);

    public static native int n01111111(int i, int i2, boolean z, int i3);

    public static native int n011111111(int i, int i2, int i3, int i4, int i5);

    public static native Object n01111111111111111113(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16);

    public static native Object n011111111113(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8);

    public static native Object n011111113(int i, int i2, int i3, int i4, boolean z);

    public static native void n01111111330(int i, int i2, boolean z, int i3, boolean z2, Object obj, Object obj2);

    public static native Object n01111113(int i, int i2, int i3, int i4);

    public static native void n011111130(int i, boolean z, int i2, boolean z2, Object obj);

    public static native void n0111111330(int i, int i2, int i3, int i4, Object obj, Object obj2);

    public static native int n01111121(int i, int i2, int i3, double d);

    public static native boolean n011111221(int i, int i2, int i3, long j, long j2);

    public static native Object n0111113(int i, int i2, int i3);

    public static native void n01111130(float f, float f2, int i, Object obj);

    public static native void n011111311310(int i, int i2, int i3, Object obj, int i4, int i5, Object obj2, int i6);

    public static native Object n01111133(int i, int i2, int i3, Object obj);

    public static native void n011111330(int i, int i2, int i3, Object obj, Object obj2);

    public static native void n0111113310(int i, int i2, int i3, Object obj, Object obj2, int i4);

    public static native Object n011113(int i, int i2);

    public static native void n0111130(int i, int i2, Object obj);

    public static native Object n0111133(int i, int i2, Object obj);

    public static native void n01111330(int i, int i2, Object obj, Object obj2);

    public static native double n01112(int i);

    public static native Object n011123(int i, long j);

    public static native boolean n0111231(int i, long j, Object obj);

    public static native Object n01113(int i);

    public static native void n011130(int i, Object obj);

    public static native int n011131(int i, Object obj);

    public static native void n0111310(int i, Object obj, int i2);

    public static native int n01113111(int i, Object obj, int i2, boolean z);

    public static native int n01113131(int i, Object obj, int i2, Object obj2);

    public static native int n0111313111(int i, Object obj, int i2, Object obj2, int i3, boolean z);

    public static native long n011132(int i, Object obj);

    public static native Object n011133(int i, Object obj);

    public static native void n0111330(boolean z, Object obj, Object obj2);

    public static native float n0111331(int i, Object obj, Object obj2);

    public static native void n01113310(int i, Object obj, Object obj2, int i2);

    public static native void n01113330(int i, Object obj, Object obj2, Object obj3);

    public static native long n01113332(int i, Object obj, Object obj2, Object obj3);

    public static native long n0112();

    public static native void n01120(long j);

    public static native int n01121(long j);

    public static native int n011211(long j, int i);

    public static native Object n011213(long j, int i);

    public static native long n01122(long j);

    public static native boolean n011221(double d, double d2);

    public static native void n011221330(double d, double d2, int i, Object obj, Object obj2);

    public static native double n011222(double d, double d2);

    public static native Object n011222113(double d, double d2, double d3, boolean z, boolean z2);

    public static native Object n0112221223(double d, double d2, double d3, int i, double d4, double d5);

    public static native double n0112222(double d, double d2, double d3);

    public static native double n01122222(double d, double d2, double d3, double d4);

    public static native boolean n0112222221(double d, double d2, double d3, double d4, double d5, double d6);

    public static native double n0112222222(double d, double d2, double d3, double d4, double d5, double d6);

    public static native boolean n011222222221(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8);

    public static native double n011222222222(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8);

    public static native long n0112222222222(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9);

    public static native Object n0112222222223(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9);

    public static native boolean n0112222222231(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8, Object obj);

    public static native double n01122222232(double d, double d2, double d3, double d4, double d5, double d6, Object obj);

    public static native Object n011222223(double d, double d2, double d3, double d4, double d5);

    public static native long n01122222332(double d, double d2, double d3, double d4, double d5, Object obj, Object obj2);

    public static native Object n01122223(double d, double d2, double d3, double d4);

    public static native void n011222230(double d, double d2, double d3, double d4, Object obj);

    public static native boolean n0112222331(double d, double d2, double d3, double d4, Object obj, Object obj2);

    public static native boolean n01122223331(double d, double d2, double d3, double d4, Object obj, Object obj2, Object obj3);

    public static native Object n01122233(double d, double d2, double d3, Object obj);

    public static native Object n011223(long j, long j2);

    public static native void n0112230(long j, long j2, Object obj);

    public static native boolean n0112231(double d, double d2, Object obj);

    public static native Object n01123(long j);

    public static native void n011230(long j, Object obj);

    public static native int n011231(long j, Object obj);

    public static native void n0112310(long j, Object obj, int i);

    public static native int n0112311(long j, Object obj, boolean z);

    public static native void n01123110(long j, Object obj, int i, int i2);

    public static native int n01123111(long j, Object obj, int i, int i2);

    public static native void n011231110(long j, Object obj, int i, int i2, int i3);

    public static native void n0112311110(long j, Object obj, int i, int i2, int i3, int i4);

    public static native int n0112311111(long j, Object obj, int i, int i2, int i3, int i4);

    public static native void n01123111111110(long j, Object obj, int i, int i2, int i3, int i4, int i5, int i6, float f, int i7);

    public static native int n01123111111131(long j, Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, Object obj2);

    public static native int n011231111111311(long j, Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, Object obj2, int i8);

    public static native int n0112311111113111(long j, Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, Object obj2, int i8, int i9);

    public static native int n011231111111331(long j, Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, Object obj2, Object obj3);

    public static native int n0112311111113311(long j, Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, Object obj2, Object obj3, int i8);

    public static native int n01123111111133111(long j, Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, Object obj2, Object obj3, int i8, int i9);

    public static native int n011231111111331111(long j, Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, Object obj2, Object obj3, int i8, int i9, int i10);

    public static native double n01123112(long j, Object obj, int i, int i2);

    public static native void n011231120(long j, Object obj, int i, int i2, double d);

    public static native long n011231122(long j, Object obj, int i, int i2, double d);

    public static native Object n01123113(long j, Object obj, int i, int i2);

    public static native void n011231130(long j, Object obj, int i, int i2, Object obj2);

    public static native int n011231131(long j, Object obj, int i, int i2, Object obj2);

    public static native int n0112311311(long j, Object obj, int i, int i2, Object obj2, int i3);

    public static native int n0112311321(long j, Object obj, int i, int i2, Object obj2, double d);

    public static native int n011231133331(long j, Object obj, int i, int i2, Object obj2, Object obj3, Object obj4, Object obj5);

    public static native long n0112312(long j, Object obj, int i);

    public static native void n01123120(long j, Object obj, int i, long j2);

    public static native void n011231220(long j, Object obj, int i, double d, double d2);

    public static native void n0112312220(long j, Object obj, int i, double d, double d2, double d3);

    public static native void n01123122220(long j, Object obj, int i, double d, double d2, double d3, double d4);

    public static native void n011231230(long j, Object obj, int i, long j2, Object obj2);

    public static native int n011231231(long j, Object obj, int i, long j2, Object obj2);

    public static native int n0112312311(long j, Object obj, int i, long j2, Object obj2, int i2);

    public static native Object n0112313(double d, Object obj, int i);

    public static native void n01123130(long j, Object obj, int i, Object obj2);

    public static native int n01123131(long j, Object obj, boolean z, Object obj2);

    public static native long n01123132(long j, Object obj, int i, Object obj2);

    public static native int n011231331(long j, Object obj, boolean z, Object obj2, Object obj3);

    public static native int n0112313331(long j, Object obj, boolean z, Object obj2, Object obj3, Object obj4);

    public static native int n01123133331(long j, Object obj, boolean z, Object obj2, Object obj3, Object obj4, Object obj5);

    public static native int n011231333331(long j, Object obj, boolean z, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6);

    public static native void n01123133333330(long j, Object obj, int i, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8);

    public static native long n011232(long j, Object obj);

    public static native void n0112320(long j, Object obj, double d);

    public static native int n0112321(long j, Object obj, double d);

    public static native int n01123211(long j, Object obj, long j2, boolean z);

    public static native long n01123212(long j, Object obj, double d, int i);

    public static native long n0112322(long j, Object obj, long j2);

    public static native void n01123220(long j, Object obj, double d, double d2);

    public static native int n01123221(long j, Object obj, double d, double d2);

    public static native long n011232212(long j, Object obj, long j2, long j3, int i);

    public static native long n0112322132(long j, Object obj, long j2, long j3, int i, Object obj2);

    public static native void n011232220(long j, Object obj, double d, double d2, double d3);

    public static native int n011232221(long j, Object obj, long j2, long j3, long j4);

    public static native void n0112322220(long j, Object obj, double d, double d2, double d3, double d4);

    public static native int n0112322221(long j, Object obj, double d, double d2, double d3, double d4);

    public static native int n01123222221(long j, Object obj, double d, double d2, double d3, double d4, double d5);

    public static native int n011232222211(long j, Object obj, long j2, long j3, long j4, double d, double d2, int i);

    public static native int n011232222221(long j, Object obj, double d, double d2, double d3, double d4, double d5, double d6);

    public static native int n0112322222221(long j, Object obj, double d, double d2, double d3, double d4, double d5, double d6, double d7);

    public static native int n01123222222221(long j, Object obj, double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8);

    public static native int n011232231(long j, Object obj, double d, double d2, Object obj2);

    public static native int n01123223111(long j, Object obj, double d, double d2, Object obj2, boolean z, boolean z2);

    public static native int n011232231131(long j, Object obj, double d, double d2, Object obj2, boolean z, boolean z2, Object obj3);

    public static native int n011232231223111(long j, Object obj, double d, double d2, Object obj2, int i, double d3, long j2, Object obj3, int i2, int i3);

    public static native int n0112322312231131(long j, Object obj, double d, double d2, Object obj2, int i, double d3, long j2, Object obj3, int i2, int i3, Object obj4);

    public static native void n01123230(long j, Object obj, long j2, Object obj2);

    public static native int n01123231(long j, Object obj, long j2, Object obj2);

    public static native int n011232311(long j, Object obj, long j2, Object obj2, int i);

    public static native long n011232312(long j, Object obj, long j2, Object obj2, boolean z);

    public static native long n0112323122(long j, Object obj, long j2, Object obj2, boolean z, double d);

    public static native long n01123231222(long j, Object obj, long j2, Object obj2, boolean z, double d, double d2);

    public static native long n011232312232(long j, Object obj, long j2, Object obj2, boolean z, double d, double d2, Object obj3);

    public static native int n0112323131(long j, Object obj, long j2, Object obj2, int i, Object obj3);

    public static native long n01123232(long j, Object obj, long j2, Object obj2);

    public static native int n0112323211(long j, Object obj, long j2, Object obj2, double d, int i);

    public static native int n01123232131(long j, Object obj, long j2, Object obj2, double d, int i, Object obj3);

    public static native int n011232321331(long j, Object obj, long j2, Object obj2, double d, int i, Object obj3, Object obj4);

    public static native int n0112323231(long j, Object obj, long j2, Object obj2, long j3, Object obj3);

    public static native int n01123232311(long j, Object obj, long j2, Object obj2, long j3, Object obj3, int i);

    public static native int n011232323111(long j, Object obj, long j2, Object obj2, long j3, Object obj3, int i, int i2);

    public static native int n0112323231131(long j, Object obj, long j2, Object obj2, long j3, Object obj3, int i, int i2, Object obj4);

    public static native int n01123232311331(long j, Object obj, long j2, Object obj2, long j3, Object obj3, int i, int i2, Object obj4, Object obj5);

    public static native int n0112323231231(long j, Object obj, long j2, Object obj2, long j3, Object obj3, int i, long j4, Object obj4);

    public static native int n01123232312331(long j, Object obj, long j2, Object obj2, long j3, Object obj3, int i, long j4, Object obj4, Object obj5);

    public static native int n011232323131(long j, Object obj, long j2, Object obj2, long j3, Object obj3, int i, Object obj4);

    public static native int n0112323231331(long j, Object obj, long j2, Object obj2, long j3, Object obj3, int i, Object obj4, Object obj5);

    public static native int n011232323231(long j, Object obj, long j2, Object obj2, long j3, Object obj3, long j4, Object obj4);

    public static native int n01123232323231(long j, Object obj, long j2, Object obj2, long j3, Object obj3, long j4, Object obj4, long j5, Object obj5);

    public static native int n011232323232331(long j, Object obj, long j2, Object obj2, long j3, Object obj3, long j4, Object obj4, long j5, Object obj5, Object obj6);

    public static native int n0112323232331(long j, Object obj, long j2, Object obj2, long j3, Object obj3, long j4, Object obj4, Object obj5);

    public static native int n01123232323331(long j, Object obj, long j2, Object obj2, long j3, Object obj3, long j4, Object obj4, Object obj5, Object obj6);

    public static native int n01123232331(long j, Object obj, long j2, Object obj2, long j3, Object obj3, Object obj4);

    public static native int n011232323331(long j, Object obj, long j2, Object obj2, long j3, Object obj3, Object obj4, Object obj5);

    public static native Object n01123233(long j, Object obj, long j2, Object obj2);

    public static native int n011232331(long j, Object obj, long j2, Object obj2, Object obj3);

    public static native long n011232332(long j, Object obj, long j2, Object obj2, Object obj3);

    public static native int n0112323331(long j, Object obj, long j2, Object obj2, Object obj3, Object obj4);

    public static native int n01123233311(long j, Object obj, long j2, Object obj2, Object obj3, Object obj4, int i);

    public static native int n011232333121(long j, Object obj, long j2, Object obj2, Object obj3, Object obj4, int i, double d);

    public static native int n0112323331221(long j, Object obj, long j2, Object obj2, Object obj3, Object obj4, int i, double d, double d2);

    public static native int n01123233312231(long j, Object obj, long j2, Object obj2, Object obj3, Object obj4, int i, double d, double d2, Object obj5);

    public static native int n011232333122331(long j, Object obj, long j2, Object obj2, Object obj3, Object obj4, int i, double d, double d2, Object obj5, Object obj6);

    public static native long n0112323332(long j, Object obj, long j2, Object obj2, Object obj3, Object obj4);

    public static native int n01123233331(long j, Object obj, double d, Object obj2, Object obj3, Object obj4, Object obj5);

    public static native Object n011233(double d, Object obj);

    public static native void n0112330(double d, Object obj, Object obj2);

    public static native int n0112331(long j, Object obj, Object obj2);

    public static native void n01123310(long j, Object obj, Object obj2, int i);

    public static native int n01123311(long j, Object obj, Object obj2, int i);

    public static native int n011233111(long j, Object obj, Object obj2, int i, int i2);

    public static native void n01123311111110(long j, Object obj, Object obj2, int i, int i2, int i3, int i4, int i5, float f, int i6);

    public static native long n01123311112(long j, Object obj, Object obj2, int i, int i2, int i3, int i4);

    public static native long n011233111132(long j, Object obj, Object obj2, int i, int i2, int i3, int i4, Object obj3);

    public static native long n0112331112(long j, Object obj, Object obj2, int i, int i2, int i3);

    public static native long n011233112(long j, Object obj, Object obj2, int i, int i2);

    public static native long n01123312(long j, Object obj, Object obj2, int i);

    public static native int n0112331221(long j, Object obj, Object obj2, int i, double d, double d2);

    public static native int n01123312221(long j, Object obj, Object obj2, int i, double d, double d2, double d3);

    public static native Object n01123313(long j, Object obj, Object obj2, int i);

    public static native long n0112332(long j, Object obj, Object obj2);

    public static native void n01123320(long j, Object obj, Object obj2, double d);

    public static native int n01123321(long j, Object obj, Object obj2, double d);

    public static native double n01123322(long j, Object obj, Object obj2, double d);

    public static native void n011233220(long j, Object obj, Object obj2, double d, double d2);

    public static native void n0112332220(long j, Object obj, Object obj2, double d, double d2, double d3);

    public static native int n01123322211(long j, Object obj, Object obj2, double d, double d2, double d3, int i);

    public static native int n011233222221(long j, Object obj, Object obj2, double d, double d2, double d3, double d4, double d5);

    public static native int n011233231(long j, Object obj, Object obj2, long j2, Object obj3);

    public static native long n0112332312(long j, Object obj, Object obj2, long j2, Object obj3, int i);

    public static native long n01123323132(long j, Object obj, Object obj2, long j2, Object obj3, int i, Object obj4);

    public static native long n011233231332(long j, Object obj, Object obj2, long j2, Object obj3, int i, Object obj4, Object obj5);

    public static native long n011233232(long j, Object obj, Object obj2, long j2, Object obj3);

    public static native int n01123323231(long j, Object obj, Object obj2, long j2, Object obj3, long j3, Object obj4);

    public static native int n0112332331(long j, Object obj, Object obj2, long j2, Object obj3, Object obj4);

    public static native long n0112332332(long j, Object obj, Object obj2, long j2, Object obj3, Object obj4);

    public static native int n01123323331(long j, Object obj, Object obj2, long j2, Object obj3, Object obj4, Object obj5);

    public static native int n011233233331(long j, Object obj, Object obj2, long j2, Object obj3, Object obj4, Object obj5, Object obj6);

    public static native Object n0112333(long j, Object obj, Object obj2);

    public static native void n01123330(long j, Object obj, Object obj2, Object obj3);

    public static native int n01123331(long j, Object obj, Object obj2, Object obj3);

    public static native int n011233311(long j, Object obj, Object obj2, Object obj3, int i);

    public static native long n011233312(long j, Object obj, Object obj2, Object obj3, int i);

    public static native long n0112333122(long j, Object obj, Object obj2, Object obj3, int i, double d);

    public static native long n01123332(long j, Object obj, Object obj2, Object obj3);

    public static native int n011233321(long j, Object obj, Object obj2, Object obj3, double d);

    public static native Object n01123333(long j, Object obj, Object obj2, Object obj3);

    public static native int n011233331(long j, Object obj, Object obj2, Object obj3, Object obj4);

    public static native int n0112333311(long j, Object obj, Object obj2, Object obj3, Object obj4, boolean z);

    public static native int n01123333131(long j, Object obj, Object obj2, Object obj3, Object obj4, boolean z, Object obj5);

    public static native int n01123333221(long j, Object obj, Object obj2, Object obj3, Object obj4, double d, double d2);

    public static native int n011233332231(long j, Object obj, Object obj2, Object obj3, Object obj4, double d, double d2, Object obj5);

    public static native int n0112333322321(long j, Object obj, Object obj2, Object obj3, Object obj4, double d, double d2, Object obj5, double d3);

    public static native int n01123333223231(long j, Object obj, Object obj2, Object obj3, Object obj4, double d, double d2, Object obj5, double d3, Object obj6);

    public static native int n011233332232321(long j, Object obj, Object obj2, Object obj3, Object obj4, double d, double d2, Object obj5, double d3, Object obj6, double d4);

    public static native int n0112333331(long j, Object obj, Object obj2, Object obj3, Object obj4, Object obj5);

    public static native void n01123333333330(long j, Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9);

    public static native Object n0113();

    public static native void n01130(Object obj);

    public static native boolean n01131(Object obj);

    public static native void n011310(Object obj, boolean z);

    public static native boolean n011311(Object obj, boolean z);

    public static native void n0113110(Object obj, int i, int i2);

    public static native int n0113111(Object obj, int i, int i2);

    public static native void n01131110(Object obj, int i, int i2, int i3);

    public static native int n01131111(Object obj, int i, int i2, int i3);

    public static native void n011311110(Object obj, int i, int i2, int i3, int i4);

    public static native int n011311111(Object obj, float f, int i, int i2, char c);

    public static native void n0113111110(Object obj, int i, int i2, int i3, int i4, int i5);

    public static native int n0113111111(Object obj, int i, char c, int i2, boolean z, int i3);

    public static native void n0113111111110(Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    public static native void n011311111111110(Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

    public static native Object n011311111113(Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7);

    public static native Object n01131111113(Object obj, int i, int i2, float f, float f2, float f3, float f4);

    public static native Object n011311113(Object obj, int i, int i2, int i3, int i4);

    public static native void n0113111130(Object obj, int i, int i2, int i3, int i4, Object obj2);

    public static native Object n0113111133(Object obj, int i, int i2, int i3, boolean z, Object obj2);

    public static native void n01131111330(Object obj, int i, int i2, int i3, int i4, Object obj2, Object obj3);

    public static native Object n011311123(Object obj, int i, int i2, int i3, long j);

    public static native Object n01131113(Object obj, int i, int i2, int i3);

    public static native void n011311130(Object obj, int i, int i2, int i3, Object obj2);

    public static native Object n011311133(Object obj, int i, int i2, boolean z, Object obj2);

    public static native Object n01131123(Object obj, int i, int i2, double d);

    public static native Object n0113113(Object obj, int i, int i2);

    public static native void n01131130(Object obj, int i, int i2, Object obj2);

    public static native boolean n01131131(Object obj, int i, int i2, Object obj2);

    public static native Object n011311313(Object obj, int i, int i2, Object obj2, int i3);

    public static native Object n01131133(Object obj, int i, int i2, Object obj2);

    public static native boolean n011311331(Object obj, int i, int i2, Object obj2, Object obj3);

    public static native boolean n0113113331(Object obj, int i, int i2, Object obj2, Object obj3, Object obj4);

    public static native boolean n01131133331(Object obj, int i, int i2, Object obj2, Object obj3, Object obj4, Object obj5);

    public static native boolean n011311333331(Object obj, int i, int i2, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6);

    public static native boolean n0113113333331(Object obj, int i, int i2, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7);

    public static native long n011312(Object obj, int i);

    public static native int n0113121(Object obj, int i, long j);

    public static native Object n0113123(Object obj, int i, double d);

    public static native Object n011313(Object obj, int i);

    public static native void n0113130(Object obj, int i, Object obj2);

    public static native boolean n0113131(Object obj, int i, Object obj2);

    public static native int n01131311(Object obj, int i, Object obj2, int i2);

    public static native void n011313110(Object obj, int i, Object obj2, int i2, int i3);

    public static native int n011313111(Object obj, int i, Object obj2, int i2, boolean z);

    public static native Object n01131313(Object obj, int i, Object obj2, int i2);

    public static native Object n011313133(Object obj, int i, Object obj2, int i2, Object obj3);

    public static native Object n0113133(Object obj, float f, Object obj2);

    public static native void n01131331130(Object obj, int i, Object obj2, Object obj3, float f, float f2, Object obj4);

    public static native Object n01131333(Object obj, int i, Object obj2, Object obj3);

    public static native void n01131333330(Object obj, int i, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6);

    public static native long n01132(Object obj);

    public static native void n011320(Object obj, long j);

    public static native long n011322(Object obj, long j);

    public static native double n0113222(Object obj, double d, double d2);

    public static native Object n0113222113(Object obj, double d, double d2, double d3, int i, int i2);

    public static native Object n01132223(Object obj, double d, double d2, double d3);

    public static native int n0113223123111(Object obj, double d, double d2, Object obj2, int i, double d3, Object obj3, int i2, int i3);

    public static native int n01132231231131(Object obj, double d, double d2, Object obj2, int i, double d3, Object obj3, int i2, int i3, Object obj4);

    public static native void n011322330(Object obj, double d, double d2, Object obj2, Object obj3);

    public static native Object n011323(Object obj, double d);

    public static native boolean n0113231(Object obj, long j, Object obj2);

    public static native long n0113232(Object obj, long j, Object obj2);

    public static native long n011323232(Object obj, long j, Object obj2, long j2, Object obj3);

    public static native long n0113232332(Object obj, long j, Object obj2, long j2, Object obj3, Object obj4);

    public static native Object n0113233(Object obj, long j, Object obj2);

    public static native long n01132332(Object obj, long j, Object obj2, Object obj3);

    public static native Object n01132333(Object obj, long j, Object obj2, Object obj3);

    public static native long n011323332(Object obj, long j, Object obj2, Object obj3, Object obj4);

    public static native long n01132333232(Object obj, long j, Object obj2, Object obj3, Object obj4, long j2, Object obj5);

    public static native long n011323332332(Object obj, long j, Object obj2, Object obj3, Object obj4, long j2, Object obj5, Object obj6);

    public static native Object n011323333(Object obj, long j, Object obj2, Object obj3, Object obj4);

    public static native Object n01133(Object obj);

    public static native void n011330(Object obj, Object obj2);

    public static native boolean n011331(Object obj, Object obj2);

    public static native void n0113310(Object obj, Object obj2, int i);

    public static native int n0113311(Object obj, Object obj2, int i);

    public static native void n01133110(Object obj, Object obj2, int i, int i2);

    public static native int n01133111(Object obj, Object obj2, int i, int i2);

    public static native int n011331111(Object obj, Object obj2, int i, int i2, char c);

    public static native Object n011331113(Object obj, Object obj2, int i, int i2, int i3);

    public static native boolean n0113311131(Object obj, Object obj2, int i, int i2, int i3, Object obj3);

    public static native Object n01133113(Object obj, Object obj2, int i, int i2);

    public static native void n011331130(Object obj, Object obj2, int i, int i2, Object obj3);

    public static native Object n011331133(Object obj, Object obj2, int i, int i2, Object obj3);

    public static native double n0113312(Object obj, Object obj2, int i);

    public static native Object n011331223(Object obj, Object obj2, boolean z, double d, double d2);

    public static native Object n0113312233(Object obj, Object obj2, boolean z, double d, double d2, Object obj3);

    public static native Object n01133123(Object obj, Object obj2, boolean z, double d);

    public static native Object n0113313(Object obj, Object obj2, int i);

    public static native void n01133130(Object obj, Object obj2, int i, Object obj3);

    public static native boolean n01133131(Object obj, Object obj2, int i, Object obj3);

    public static native Object n0113313113(Object obj, Object obj2, int i, Object obj3, int i2, int i3);

    public static native Object n01133133(Object obj, Object obj2, boolean z, Object obj3);

    public static native void n011331330(Object obj, Object obj2, boolean z, Object obj3, Object obj4);

    public static native Object n011331333(Object obj, Object obj2, boolean z, Object obj3, Object obj4);

    public static native void n0113313330(Object obj, Object obj2, int i, Object obj3, Object obj4, Object obj5);

    public static native void n01133133310(Object obj, Object obj2, boolean z, Object obj3, Object obj4, Object obj5, boolean z2);

    public static native long n011331333112(Object obj, Object obj2, int i, Object obj3, Object obj4, Object obj5, int i2, int i3);

    public static native Object n0113313333(Object obj, Object obj2, int i, Object obj3, Object obj4, Object obj5);

    public static native void n01133133330(Object obj, Object obj2, int i, Object obj3, Object obj4, Object obj5, Object obj6);

    public static native long n011332(Object obj, Object obj2);

    public static native void n0113320(Object obj, Object obj2, long j);

    public static native boolean n0113321(Object obj, Object obj2, double d);

    public static native int n01133211(Object obj, Object obj2, double d, int i);

    public static native int n011332131(Object obj, Object obj2, double d, int i, Object obj3);

    public static native int n0113321331(Object obj, Object obj2, double d, int i, Object obj3, Object obj4);

    public static native int n01133222211131(Object obj, Object obj2, double d, double d2, double d3, double d4, int i, int i2, int i3, Object obj3);

    public static native int n011332222111331(Object obj, Object obj2, double d, double d2, double d3, double d4, int i, int i2, int i3, Object obj3, Object obj4);

    public static native Object n011332223(Object obj, Object obj2, double d, double d2, double d3);

    public static native Object n0113323(Object obj, Object obj2, double d);

    public static native boolean n01133231(Object obj, Object obj2, double d, Object obj3);

    public static native long n01133232(Object obj, Object obj2, long j, Object obj3);

    public static native long n011332332(Object obj, Object obj2, long j, Object obj3, Object obj4);

    public static native Object n011333(Object obj, Object obj2);

    public static native void n0113330(Object obj, Object obj2, Object obj3);

    public static native boolean n0113331(Object obj, Object obj2, Object obj3);

    public static native void n01133310(Object obj, Object obj2, Object obj3, int i);

    public static native int n01133311(Object obj, Object obj2, Object obj3, float f);

    public static native int n011333111(Object obj, Object obj2, Object obj3, int i, int i2);

    public static native Object n01133311113(Object obj, Object obj2, Object obj3, int i, int i2, int i3, int i4);

    public static native Object n0113331113(Object obj, Object obj2, Object obj3, boolean z, boolean z2, int i);

    public static native Object n011333113(Object obj, Object obj2, Object obj3, int i, int i2);

    public static native int n0113331131(Object obj, Object obj2, Object obj3, int i, int i2, Object obj4);

    public static native int n01133311331(Object obj, Object obj2, Object obj3, int i, int i2, Object obj4, Object obj5);

    public static native Object n011333123(Object obj, Object obj2, Object obj3, int i, double d);

    public static native Object n01133313(Object obj, Object obj2, Object obj3, int i);

    public static native void n011333130(Object obj, Object obj2, Object obj3, int i, Object obj4);

    public static native int n011333131(Object obj, Object obj2, Object obj3, int i, Object obj4);

    public static native Object n011333133(Object obj, Object obj2, Object obj3, int i, Object obj4);

    public static native void n0113331330(Object obj, Object obj2, Object obj3, int i, Object obj4, Object obj5);

    public static native int n0113331331(Object obj, Object obj2, Object obj3, int i, Object obj4, Object obj5);

    public static native double n0113332(Object obj, Object obj2, Object obj3);

    public static native boolean n01133321(Object obj, Object obj2, Object obj3, double d);

    public static native Object n011333223(Object obj, Object obj2, Object obj3, double d, double d2);

    public static native Object n01133323(Object obj, Object obj2, Object obj3, double d);

    public static native Object n0113333(Object obj, Object obj2, Object obj3);

    public static native void n01133330(Object obj, Object obj2, Object obj3, Object obj4);

    public static native boolean n01133331(Object obj, Object obj2, Object obj3, Object obj4);

    public static native int n011333311(Object obj, Object obj2, Object obj3, Object obj4, boolean z);

    public static native int n0113333121(Object obj, Object obj2, Object obj3, Object obj4, int i, double d);

    public static native int n01133331221(Object obj, Object obj2, Object obj3, Object obj4, int i, double d, double d2);

    public static native int n011333312231(Object obj, Object obj2, Object obj3, Object obj4, int i, double d, double d2, Object obj5);

    public static native int n0113333122331(Object obj, Object obj2, Object obj3, Object obj4, int i, double d, double d2, Object obj5, Object obj6);

    public static native void n0113333130(Object obj, Object obj2, Object obj3, Object obj4, int i, Object obj5);

    public static native int n01133331331(Object obj, Object obj2, Object obj3, Object obj4, int i, Object obj5, Object obj6);

    public static native Object n011333323(Object obj, Object obj2, Object obj3, Object obj4, long j);

    public static native Object n01133333(Object obj, Object obj2, Object obj3, Object obj4);

    public static native void n011333330(Object obj, Object obj2, Object obj3, Object obj4, Object obj5);

    public static native int n011333331(Object obj, Object obj2, Object obj3, Object obj4, Object obj5);

    public static native void n011333331130(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, float f, float f2, Object obj6);

    public static native Object n0113333313(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, int i);

    public static native Object n011333333(Object obj, Object obj2, Object obj3, Object obj4, Object obj5);

    public static native int n0113333331(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6);

    public static native Object n01133333313331113(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, int i, Object obj7, Object obj8, Object obj9, int i2, int i3, boolean z);

    public static native Object n01133333313333(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, int i, Object obj7, Object obj8, Object obj9);

    public static native Object n0113333333(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6);

    public static native void n01133333330(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7);

    public static native boolean n01133333331(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7);

    public static native boolean n011333333331(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8);

    public native synchronized void n1100();

    public native synchronized int n1101();

    public native synchronized void n11010(boolean z);

    public native synchronized boolean n11011(boolean z);

    public native synchronized Object n11013(boolean z);

    public native synchronized int n1101331(boolean z, Object obj, Object obj2);

    public native synchronized void n11020(long j);

    public native synchronized void n110230(long j, Object obj);

    public native synchronized Object n1103();

    public native synchronized void n11030(Object obj);

    public native synchronized void n110310(Object obj, boolean z);

    public native synchronized void n1103220(Object obj, long j, long j2);

    public native synchronized Object n11033(Object obj);

    public native synchronized boolean n110331(Object obj, Object obj2);

    public native synchronized int n1103331(Object obj, Object obj2, Object obj3);

    public native void n1110();

    public native boolean n1111();

    public native void n11110(int i);

    public native int n11111(boolean z);

    public native void n111110(int i, int i2);

    public native boolean n111111(int i, int i2);

    public native void n1111110(int i, float f, boolean z);

    public native int n1111111(int i, int i2, int i3);

    public native void n11111110(int i, int i2, int i3, boolean z);

    public native int n11111111(int i, float f, int i2, int i3);

    public native void n111111110(boolean z, int i, int i2, int i3, int i4);

    public native int n111111111(int i, int i2, int i3, int i4, int i5);

    public native void n1111111110(int i, int i2, int i3, int i4, int i5, int i6);

    public native void n111111111110(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    public native void n1111111111110(float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9);

    public native void n11111111111110(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

    public native Object n1111111111113(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    public native Object n11111111113(int i, int i2, int i3, int i4, int i5, int i6, int i7);

    public native int n111111111131(int i, int i2, int i3, int i4, int i5, int i6, int i7, Object obj);

    public native int n1111111111311(int i, int i2, int i3, int i4, int i5, int i6, int i7, Object obj, int i8);

    public native int n11111111113111(int i, int i2, int i3, int i4, int i5, int i6, int i7, Object obj, int i8, int i9);

    public native int n1111111111331(int i, int i2, int i3, int i4, int i5, int i6, int i7, Object obj, Object obj2);

    public native int n11111111113311(int i, int i2, int i3, int i4, int i5, int i6, int i7, Object obj, Object obj2, int i8);

    public native int n111111111133111(int i, int i2, int i3, int i4, int i5, int i6, int i7, Object obj, Object obj2, int i8, int i9);

    public native int n1111111111331111(int i, int i2, int i3, int i4, int i5, int i6, int i7, Object obj, Object obj2, int i8, int i9, int i10);

    public native int n11111111131(int i, int i2, int i3, int i4, int i5, int i6, Object obj);

    public native Object n111111113(int i, int i2, int i3, int i4, boolean z);

    public native void n1111111130(int i, int i2, int i3, int i4, int i5, Object obj);

    public native int n1111111131(int i, int i2, int i3, int i4, int i5, Object obj);

    public native Object n11111113(int i, int i2, int i3, int i4);

    public native void n111111130(int i, int i2, int i3, int i4, Object obj);

    public native boolean n111111131(int i, int i2, int i3, int i4, Object obj);

    public native void n11111113110(boolean z, int i, int i2, int i3, Object obj, char c, int i4);

    public native void n1111111330(int i, int i2, int i3, int i4, Object obj, Object obj2);

    public native void n11111113311110(int i, int i2, int i3, int i4, Object obj, Object obj2, int i5, int i6, int i7, int i8);

    public native void n11111120(int i, int i2, int i3, long j);

    public native Object n1111113(int i, boolean z, int i2);

    public native void n11111130(int i, int i2, boolean z, Object obj);

    public native boolean n11111131(int i, int i2, int i3, Object obj);

    public native void n111111311310(int i, int i2, int i3, Object obj, int i4, int i5, Object obj2, int i6);

    public native Object n11111133(int i, int i2, boolean z, Object obj);

    public native void n111111330(int i, int i2, int i3, Object obj, Object obj2);

    public native void n1111113310(int i, int i2, int i3, Object obj, Object obj2, int i4);

    public native long n111112(int i, int i2);

    public native void n1111120(boolean z, boolean z2, long j);

    public native void n1111121330(int i, int i2, double d, int i3, Object obj, Object obj2);

    public native void n11111230(int i, int i2, double d, Object obj);

    public native Object n111113(int i, int i2);

    public native void n1111130(int i, int i2, Object obj);

    public native boolean n1111131(int i, int i2, Object obj);

    public native int n11111311(int i, int i2, Object obj, int i3);

    public native Object n11111313(int i, int i2, Object obj, int i3);

    public native int n11111321(int i, int i2, Object obj, double d);

    public native void n111113220(int i, int i2, Object obj, double d, double d2);

    public native Object n1111133(int i, int i2, Object obj);

    public native void n11111330(boolean z, int i, Object obj, Object obj2);

    public native void n111113310(int i, int i2, Object obj, Object obj2, boolean z);

    public native void n1111133110(int i, int i2, Object obj, Object obj2, int i3, int i4);

    public native void n111113330(int i, int i2, Object obj, Object obj2, Object obj3);

    public native int n1111133331(int i, int i2, Object obj, Object obj2, Object obj3, Object obj4);

    public native long n11112(int i);

    public native void n111120(int i, double d);

    public native int n111121(int i, long j);

    public native void n1111210(boolean z, long j, int i);

    public native void n1111220(int i, long j, long j2);

    public native void n11112220(int i, double d, double d2, double d3);

    public native void n111122220(int i, double d, double d2, double d3, double d4);

    public native boolean n111122221(int i, double d, double d2, double d3, double d4);

    public native void n11112222220(int i, double d, double d2, double d3, double d4, double d5, double d6);

    public native Object n11113(int i);

    public native void n111130(int i, Object obj);

    public native boolean n111131(int i, Object obj);

    public native void n1111310(int i, Object obj, int i2);

    public native int n1111311(int i, Object obj, int i2);

    public native void n11113110(int i, Object obj, int i2, int i3);

    public native void n111131110(int i, Object obj, int i2, int i3, int i4);

    public native Object n1111313(int i, Object obj, int i2);

    public native Object n111133(int i, Object obj);

    public native void n1111330(int i, Object obj, Object obj2);

    public native boolean n1111331(int i, Object obj, Object obj2);

    public native void n11113310(int i, Object obj, Object obj2, int i2);

    public native boolean n11113311(int i, Object obj, Object obj2, int i2);

    public native Object n1111331113(int i, Object obj, Object obj2, int i2, int i3, boolean z);

    public native Object n111133133(int i, Object obj, Object obj2, int i2, Object obj3);

    public native Object n1111333(int i, Object obj, Object obj2);

    public native void n11113330(int i, Object obj, Object obj2, Object obj3);

    public native int n11113331(boolean z, Object obj, Object obj2, Object obj3);

    public native Object n11113333(int i, Object obj, Object obj2, Object obj3);

    public native void n111133330(int i, Object obj, Object obj2, Object obj3, Object obj4);

    public native boolean n111133331(int i, Object obj, Object obj2, Object obj3, Object obj4);

    public native void n1111333310(int i, Object obj, Object obj2, Object obj3, Object obj4, int i2);

    public native Object n111133333(int i, Object obj, Object obj2, Object obj3, Object obj4);

    public native void n1111333330(int i, Object obj, Object obj2, Object obj3, Object obj4, Object obj5);

    public native int n1111333331(boolean z, Object obj, Object obj2, Object obj3, Object obj4, Object obj5);

    public native void n111133333330(int i, Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7);

    public native long n1112();

    public native void n11120(long j);

    public native int n11121(long j);

    public native void n111210(long j, boolean z);

    public native int n111211(long j, boolean z);

    public native void n111211130(long j, int i, int i2, int i3, Object obj);

    public native void n11121120(double d, short s, boolean z, double d2);

    public native Object n111213(double d, int i);

    public native long n11122(long j);

    public native void n111220(long j, long j2);

    public native int n111221(double d, double d2);

    public native void n1112210(double d, double d2, float f);

    public native Object n11122113(double d, double d2, int i, int i2);

    public native void n111221330(double d, double d2, int i, Object obj, Object obj2);

    public native double n111222(double d, double d2);

    public native void n1112220(double d, double d2, double d3);

    public native int n1112221(long j, long j2, long j3);

    public native Object n111222113(double d, double d2, double d3, int i, int i2);

    public native void n11122220(double d, double d2, double d3, double d4);

    public native int n11122221(double d, double d2, double d3, double d4);

    public native void n111222210(double d, double d2, double d3, double d4, int i);

    public native void n111222220(double d, double d2, double d3, double d4, double d5);

    public native int n111222221(double d, double d2, double d3, double d4, double d5);

    public native int n1112222211(long j, long j2, long j3, double d, double d2, int i);

    public native int n1112222221(double d, double d2, double d3, double d4, double d5, double d6);

    public native void n11122222210(long j, double d, double d2, double d3, double d4, double d5, float f);

    public native void n111222222110(long j, double d, double d2, double d3, double d4, double d5, float f, float f2);

    public native void n11122222211110(long j, double d, double d2, double d3, double d4, double d5, float f, int i, int i2, float f2);

    public native double n1112222222(double d, double d2, double d3, double d4, double d5, double d6);

    public native int n11122222221(double d, double d2, double d3, double d4, double d5, double d6, double d7);

    public native boolean n111222222221(double d, double d2, double d3, double d4, double d5, double d6, double d7, double d8);

    public native void n1112222230(double d, double d2, double d3, double d4, double d5, Object obj);

    public native void n11122222330(double d, double d2, double d3, double d4, double d5, Object obj, Object obj2);

    public native Object n11122223(double d, double d2, double d3, double d4);

    public native void n111222230(double d, double d2, double d3, double d4, Object obj);

    public native void n1112222330(double d, double d2, double d3, double d4, Object obj, Object obj2);

    public native Object n1112223(double d, double d2, double d3);

    public native void n11122230(double d, double d2, double d3, Object obj);

    public native Object n11122233(double d, double d2, double d3, Object obj);

    public native Object n111223(double d, double d2);

    public native void n1112230(double d, double d2, Object obj);

    public native int n1112231(double d, double d2, Object obj);

    public native int n111223111(double d, double d2, Object obj, boolean z, boolean z2);

    public native int n1112231131(double d, double d2, Object obj, boolean z, boolean z2, Object obj2);

    public native void n111223220(double d, double d2, Object obj, double d3, double d4);

    public native Object n1112233(long j, long j2, Object obj);

    public native Object n11122333(long j, long j2, Object obj, Object obj2);

    public native Object n11123(long j);

    public native void n111230(long j, Object obj);

    public native int n111231(double d, Object obj);

    public native void n11123120(double d, Object obj, boolean z, double d2);

    public native Object n111233(long j, Object obj);

    public native void n1112330(long j, Object obj, Object obj2);

    public native boolean n1112331(double d, Object obj, Object obj2);

    public native int n111233331(double d, Object obj, Object obj2, Object obj3, Object obj4);

    public native Object n1113();

    public native void n11130(Object obj);

    public native boolean n11131(Object obj);

    public native void n111310(Object obj, int i);

    public native boolean n111311(Object obj, int i);

    public native void n1113110(Object obj, int i, int i2);

    public native boolean n1113111(Object obj, int i, int i2);

    public native void n11131110(Object obj, int i, int i2, int i3);

    public native int n11131111(Object obj, int i, int i2, int i3);

    public native void n111311110(Object obj, int i, int i2, int i3, boolean z);

    public native boolean n111311111(Object obj, boolean z, int i, int i2, int i3);

    public native void n1113111110(Object obj, int i, int i2, int i3, int i4, int i5);

    public native boolean n1113111111(Object obj, boolean z, int i, int i2, int i3, int i4);

    public native void n11131111110(Object obj, int i, int i2, int i3, int i4, int i5, int i6);

    public native void n111311111110(Object obj, int i, int i2, int i3, int i4, int i5, int i6, boolean z);

    public native void n1113111111110(Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    public native void n111311111111110(Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10);

    public native void n1113111111111110(Object obj, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, boolean z);

    public native Object n1113111111111111111113(Object obj, int i, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16);

    public native Object n1113111113(Object obj, int i, int i2, int i3, int i4, boolean z);

    public native void n11131111130(Object obj, int i, int i2, int i3, int i4, float f, Object obj2);

    public native Object n111311113(Object obj, int i, int i2, int i3, int i4);

    public native Object n1113111133(Object obj, int i, int i2, int i3, int i4, Object obj2);

    public native void n11131111333310(Object obj, float f, float f2, float f3, float f4, Object obj2, Object obj3, Object obj4, Object obj5, boolean z);

    public native Object n11131113(Object obj, int i, boolean z, int i2);

    public native boolean n111311131(Object obj, int i, int i2, int i3, Object obj2);

    public native Object n111311133(Object obj, int i, int i2, int i3, Object obj2);

    public native boolean n11131122221(Object obj, int i, int i2, double d, double d2, double d3, double d4);

    public native Object n1113113(Object obj, boolean z, boolean z2);

    public native void n11131130(Object obj, int i, int i2, Object obj2);

    public native int n11131131(Object obj, int i, int i2, Object obj2);

    public native int n111311311(Object obj, int i, int i2, Object obj2, int i3);

    public native Object n11131133(Object obj, boolean z, int i, Object obj2);

    public native void n111311330(Object obj, int i, int i2, Object obj2, Object obj3);

    public native Object n1113113313(Object obj, int i, int i2, Object obj2, Object obj3, int i3);

    public native void n1113113330(Object obj, float f, float f2, Object obj2, Object obj3, Object obj4);

    public native long n111312(Object obj, int i);

    public native void n1113120(Object obj, int i, long j);

    public native boolean n1113121(Object obj, int i, long j);

    public native int n11131221(Object obj, int i, double d, double d2);

    public native int n111312221(Object obj, int i, double d, double d2, double d3);

    public native void n1113122211230(Object obj, boolean z, double d, double d2, double d3, boolean z2, boolean z3, double d4, Object obj2);

    public native Object n1113123(Object obj, int i, long j);

    public native Object n111313(Object obj, int i);

    public native void n1113130(Object obj, int i, Object obj2);

    public native boolean n1113131(Object obj, int i, Object obj2);

    public native void n11131310(Object obj, int i, Object obj2, int i2);

    public native int n1113131111(Object obj, int i, Object obj2, int i2, int i3, boolean z);

    public native void n111313130(Object obj, int i, Object obj2, boolean z, Object obj3);

    public native void n1113131330(Object obj, int i, Object obj2, boolean z, Object obj3, Object obj4);

    public native void n11131313330(Object obj, int i, Object obj2, boolean z, Object obj3, Object obj4, Object obj5);

    public native Object n1113133(Object obj, int i, Object obj2);

    public native void n11131330(Object obj, int i, Object obj2, Object obj3);

    public native boolean n11131331(Object obj, int i, Object obj2, Object obj3);

    public native void n111313330(Object obj, int i, Object obj2, Object obj3, Object obj4);

    public native void n1113133310(Object obj, int i, Object obj2, Object obj3, Object obj4, int i2);

    public native long n11132(Object obj);

    public native void n111320(Object obj, long j);

    public native boolean n111321(Object obj, double d);

    public native boolean n111321331(Object obj, double d, boolean z, Object obj2, Object obj3);

    public native double n111322(Object obj, double d);

    public native void n1113220(Object obj, double d, double d2);

    public native void n11132220(Object obj, double d, double d2, double d3);

    public native int n111322211(Object obj, double d, double d2, double d3, int i);

    public native void n111322220(Object obj, double d, double d2, double d3, double d4);

    public native int n1113222221(Object obj, double d, double d2, double d3, double d4, double d5);

    public native void n11132222220(Object obj, double d, double d2, double d3, double d4, double d5, double d6);

    public native boolean n1113222231(Object obj, double d, double d2, double d3, double d4, Object obj2);

    public native void n111322230(Object obj, double d, double d2, double d3, Object obj2);

    public native void n11132230(Object obj, double d, double d2, Object obj2);

    public native Object n111323(Object obj, double d);

    public native void n1113230(Object obj, long j, Object obj2);

    public native boolean n1113231(Object obj, double d, Object obj2);

    public native boolean n11132331(Object obj, double d, Object obj2, Object obj3);

    public native boolean n111323311(Object obj, double d, Object obj2, Object obj3, boolean z);

    public native Object n11133(Object obj);

    public native void n111330(Object obj, Object obj2);

    public native boolean n111331(Object obj, Object obj2);

    public native void n1113310(Object obj, Object obj2, boolean z);

    public native boolean n1113311(Object obj, Object obj2, int i);

    public native void n11133110(Object obj, Object obj2, boolean z, int i);

    public native boolean n11133111(Object obj, Object obj2, int i, int i2);

    public native void n111331110(Object obj, Object obj2, int i, int i2, int i3);

    public native void n1113311110(Object obj, Object obj2, int i, int i2, int i3, int i4);

    public native void n11133111110(Object obj, Object obj2, int i, int i2, int i3, int i4, boolean z);

    public native void n1113311111110(Object obj, Object obj2, int i, int i2, byte b, int i3, int i4, int i5, int i6);

    public native void n111331111111110(Object obj, Object obj2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z);

    public native Object n111331113(Object obj, Object obj2, boolean z, boolean z2, int i);

    public native Object n11133113(Object obj, Object obj2, int i, boolean z);

    public native void n111331130(Object obj, Object obj2, float f, float f2, Object obj3);

    public native boolean n111331131(Object obj, Object obj2, float f, float f2, Object obj3);

    public native void n1113311310(Object obj, Object obj2, float f, float f2, Object obj3, float f3);

    public native int n1113311311(Object obj, Object obj2, int i, int i2, Object obj3, int i3);

    public native Object n111331133(Object obj, Object obj2, int i, int i2, Object obj3);

    public native void n1113311330(Object obj, Object obj2, float f, float f2, Object obj3, Object obj4);

    public native void n11133120(Object obj, Object obj2, int i, long j);

    public native boolean n11133121(Object obj, Object obj2, int i, long j);

    public native Object n1113313(Object obj, Object obj2, int i);

    public native void n11133130(Object obj, Object obj2, int i, Object obj3);

    public native boolean n11133131(Object obj, Object obj2, int i, Object obj3);

    public native void n111331310(Object obj, Object obj2, int i, Object obj3, boolean z);

    public native int n111331311(Object obj, Object obj2, int i, Object obj3, int i2);

    public native Object n11133133(Object obj, Object obj2, int i, Object obj3);

    public native void n111331330(Object obj, Object obj2, int i, Object obj3, Object obj4);

    public native boolean n111331331(Object obj, Object obj2, int i, Object obj3, Object obj4);

    public native Object n111331333(Object obj, Object obj2, int i, Object obj3, Object obj4);

    public native void n11133133310(Object obj, Object obj2, int i, Object obj3, Object obj4, Object obj5, int i2);

    public native void n11133133330(Object obj, Object obj2, boolean z, Object obj3, Object obj4, Object obj5, Object obj6);

    public native boolean n11133133331(Object obj, Object obj2, int i, Object obj3, Object obj4, Object obj5, Object obj6);

    public native long n111332(Object obj, Object obj2);

    public native void n1113320(Object obj, Object obj2, long j);

    public native boolean n1113321(Object obj, Object obj2, long j);

    public native void n1113321130(Object obj, Object obj2, double d, int i, int i2, Object obj3);

    public native void n11133220(Object obj, Object obj2, double d, double d2);

    public native void n11133221130(Object obj, Object obj2, double d, double d2, int i, int i2, Object obj3);

    public native void n111332230(Object obj, Object obj2, double d, double d2, Object obj3);

    public native Object n1113323(Object obj, Object obj2, double d);

    public native void n11133233330(Object obj, Object obj2, double d, Object obj3, Object obj4, Object obj5, Object obj6);

    public native Object n111333(Object obj, Object obj2);

    public native void n1113330(Object obj, Object obj2, Object obj3);

    public native int n1113331(Object obj, Object obj2, Object obj3);

    public native void n11133310(Object obj, Object obj2, Object obj3, int i);

    public native boolean n11133311(Object obj, Object obj2, Object obj3, boolean z);

    public native void n111333110(Object obj, Object obj2, Object obj3, int i, int i2);

    public native void n1113331110(Object obj, Object obj2, Object obj3, float f, float f2, float f3);

    public native void n11133311110(Object obj, Object obj2, Object obj3, float f, float f2, int i, int i2);

    public native void n111333111110(Object obj, Object obj2, Object obj3, boolean z, boolean z2, boolean z3, boolean z4, boolean z5);

    public native void n1113331130(Object obj, Object obj2, Object obj3, int i, int i2, Object obj4);

    public native void n11133311330(Object obj, Object obj2, Object obj3, int i, int i2, Object obj4, Object obj5);

    public native Object n11133313(Object obj, Object obj2, Object obj3, int i);

    public native void n111333130(Object obj, Object obj2, Object obj3, boolean z, Object obj4);

    public native int n111333131(Object obj, Object obj2, Object obj3, boolean z, Object obj4);

    public native Object n111333133(Object obj, Object obj2, Object obj3, boolean z, Object obj4);

    public native void n1113331330(Object obj, Object obj2, Object obj3, int i, Object obj4, Object obj5);

    public native Object n1113331333(Object obj, Object obj2, Object obj3, boolean z, Object obj4, Object obj5);

    public native double n1113332(Object obj, Object obj2, Object obj3);

    public native int n111333221(Object obj, Object obj2, Object obj3, double d, double d2);

    public native int n1113332231(Object obj, Object obj2, Object obj3, double d, double d2, Object obj4);

    public native int n11133322321(Object obj, Object obj2, Object obj3, double d, double d2, Object obj4, double d3);

    public native int n111333223231(Object obj, Object obj2, Object obj3, double d, double d2, Object obj4, double d3, Object obj5);

    public native int n1113332232321(Object obj, Object obj2, Object obj3, double d, double d2, Object obj4, double d3, Object obj5, double d4);

    public native void n111333233330(Object obj, Object obj2, Object obj3, long j, Object obj4, Object obj5, Object obj6, Object obj7);

    public native Object n1113333(Object obj, Object obj2, Object obj3);

    public native void n11133330(Object obj, Object obj2, Object obj3, Object obj4);

    public native int n11133331(Object obj, Object obj2, Object obj3, Object obj4);

    public native void n111333310(Object obj, Object obj2, Object obj3, Object obj4, int i);

    public native void n11133331110(Object obj, Object obj2, Object obj3, Object obj4, int i, int i2, boolean z);

    public native void n11133331130(Object obj, Object obj2, Object obj3, Object obj4, int i, int i2, Object obj5);

    public native Object n111333313(Object obj, Object obj2, Object obj3, Object obj4, int i);

    public native void n1113333130(Object obj, Object obj2, Object obj3, Object obj4, boolean z, Object obj5);

    public native Object n1113333133(Object obj, Object obj2, Object obj3, Object obj4, int i, Object obj5);

    public native void n11133331330(Object obj, Object obj2, Object obj3, Object obj4, int i, Object obj5, Object obj6);

    public native int n11133331331(Object obj, Object obj2, Object obj3, Object obj4, int i, Object obj5, Object obj6);

    public native void n111333320(Object obj, Object obj2, Object obj3, Object obj4, long j);

    public native void n11133332130(Object obj, Object obj2, Object obj3, Object obj4, double d, int i, Object obj5);

    public native void n1113333230(Object obj, Object obj2, Object obj3, Object obj4, long j, Object obj5);

    public native Object n11133333(Object obj, Object obj2, Object obj3, Object obj4);

    public native void n111333330(Object obj, Object obj2, Object obj3, Object obj4, Object obj5);

    public native int n111333331(Object obj, Object obj2, Object obj3, Object obj4, Object obj5);

    public native void n1113333310(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, int i);

    public native boolean n1113333311(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, boolean z);

    public native void n11133333130(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, boolean z, Object obj6);

    public native void n1113333313130(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, boolean z, Object obj6, int i, Object obj7);

    public native Object n111333333(Object obj, Object obj2, Object obj3, Object obj4, Object obj5);

    public native void n1113333330(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6);

    public native void n111333333110(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, int i, int i2);

    public native void n1113333331333111113133130(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, int i, Object obj7, Object obj8, Object obj9, int i2, int i3, boolean z, boolean z2, int i4, Object obj10, boolean z3, Object obj11, Object obj12, boolean z4, Object obj13);

    public native Object n1113333333(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6);

    public native void n11133333330(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7);

    public native void n111333333330(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8);

    public native void n11133333333330(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9, Object obj10);

    public native void n111333333333333330(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object obj6, Object obj7, Object obj8, Object obj9, Object obj10, Object obj11, Object obj12, Object obj13, Object obj14);

    public static String getSoPath1() {
        return f443;
    }

    public static String getSoPath2() {
        return f444;
    }

    public static String getDir() {
        return f445;
    }

    public static Context getAppContext() {
        return f441;
    }

    public static Context getOrigApplicationContext(Context context) {
        if (context == f438) {
            return f439;
        }
        return context;
    }

    /* renamed from: ᵢˋ */
    private static Application m12(Context context) {
        ClassLoader classLoader;
        Class<?> loadClass;
        try {
            if (!(f439 != null || (classLoader = context.getClassLoader()) == null || (loadClass = classLoader.loadClass(strEntryApplication)) == null)) {
                f439 = (Application) loadClass.newInstance();
            }
        } catch (Exception e) {
        }
        return f439;
    }

    @Override // android.app.Application
    public final void onCreate() {
        super.onCreate();
        try {
            interface7(f439, f438.getBaseContext());
        } catch (Exception e) {
        }
        if (f439 != null) {
            f439.onCreate();
        }
        interface21(f439);
    }

    /* access modifiers changed from: protected */
    @Override // android.content.ContextWrapper
    public final void attachBaseContext(Context context) {
        boolean r0;
        super.attachBaseContext(context);
        C0249.m13();
        f441 = context;
        if (f438 == null) {
            f438 = this;
        }
        if (f439 == null) {
            Boolean valueOf = Boolean.valueOf(C0249.m18());
            Boolean bool = false;
            Boolean bool2 = false;
            if (Build.CPU_ABI.contains("64") || Build.CPU_ABI2.contains("64")) {
                bool = true;
            }
            if (Build.CPU_ABI.contains("mips") || Build.CPU_ABI2.contains("mips")) {
                bool2 = true;
            }
            if (valueOf.booleanValue() && needX86Bridge) {
                System.loadLibrary("X86Bridge");
            }
            if (!loadFromLib) {
                String absolutePath = context.getFilesDir().getParentFile().getAbsolutePath();
                try {
                    absolutePath = context.getFilesDir().getParentFile().getCanonicalPath();
                } catch (Exception e) {
                }
                String str = absolutePath + "/.jiagu";
                f446 = m11(str, bool.booleanValue(), bool2.booleanValue());
                f442 = m11(str, false, false);
                f443 = str + File.separator + f442;
                f444 = str + File.separator + f446;
                f445 = str;
                if (bool2.booleanValue()) {
                    C0249.m17(context, f440 + "_mips.so", str, f442);
                } else if (!valueOf.booleanValue() || needX86Bridge) {
                    C0249.m17(context, f440 + ".so", str, f442);
                } else {
                    C0249.m17(context, f440 + "_x86.so", str, f442);
                }
                if (!bool.booleanValue() || bool2.booleanValue()) {
                    System.load(str + FileSelector_Dialog.sRoot + f442);
                } else {
                    if (!valueOf.booleanValue() || needX86Bridge) {
                        r0 = C0249.m17(context, f440 + "_a64.so", str, f446);
                    } else {
                        r0 = C0249.m17(context, f440 + "_x64.so", str, f446);
                    }
                    if (r0) {
                        System.load(str + FileSelector_Dialog.sRoot + f446);
                    } else {
                        System.load(str + FileSelector_Dialog.sRoot + f442);
                    }
                }
            } else if (!valueOf.booleanValue() || needX86Bridge) {
                System.loadLibrary("jiagu");
            } else {
                System.loadLibrary("jiagu_x86");
            }
        }
        interface5(f438);
        if (f439 == null) {
            f439 = m12(context);
            if (f439 != null) {
                try {
                    Method declaredMethod = Application.class.getDeclaredMethod("attach", Context.class);
                    if (declaredMethod != null) {
                        declaredMethod.setAccessible(true);
                        declaredMethod.invoke(f439, context);
                    }
                    interface8(f439, context);
                } catch (Exception e2) {
                    throw new RuntimeException("Failed to call attachBaseContext.", e2);
                }
            } else {
                System.exit(1);
            }
        }
    }

    /* renamed from: ᵢˋ */
    private static String m11(String str, boolean z, boolean z2) {
        String str2 = f440;
        if (Build.VERSION.SDK_INT < 23) {
            str2 = str2 + str.hashCode();
        }
        if (!z || z2) {
            return str2 + ".so";
        }
        return str2 + "_64.so";
    }

    public static String getString2(int i) {
        String str = f447.get(Integer.valueOf(i));
        if (str == null) {
            str = interface14(i);
            f447.put(Integer.valueOf(i), str);
        }
        if (str != null) {
            return str.intern();
        }
        return str;
    }

    public static String getString2(String str) {
        try {
            return getString2(Integer.parseInt(str));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }
}
