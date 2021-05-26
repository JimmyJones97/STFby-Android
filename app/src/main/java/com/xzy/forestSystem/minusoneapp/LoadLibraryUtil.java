package com.xzy.forestSystem.minusoneapp;

import android.os.Build;
import android.util.Log;

import com.xzy.forestSystem.otherlibs.Waterfall.TransportMediator;

import org.gdal.ogr.ogrConstants;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LoadLibraryUtil {
    private static final short[] f625short = {2036, 1940, 1968, 1975, 1964, 1962, 1942, 1975, 1980, 808, 775, 779, 770, 778, 846, 2757, 2699, 2698, 2705, 2757, 2691, 2698, 2704, 2699, 2689, 2757, 2700, 2699, 2757, 2255, 2279, 2294, 2282, 2285, 2278, 2210, 396, 475, 453, 472, 452, 396, 476, 461, 478, 461, 449, 457, 472, 457, 478, 479, 396, 1036, 1090, 1091, 1112, 1036, 1098, 1091, 1113, 1090, 1096, 1036, 1093, 1090, 1036, 2900, 2907, 2902, 2884, 2884, 2939, 2904, 2902, 2899, 2898, 2885, 2839, 2904, 2885, 2839, 2897, 2904, 2907, 2899, 2898, 2885, 2839, 2910, 2884, 2839, 2910, 2907, 2907, 2898, 2896, 2902, 2907, 2839};

    private static final String TAG = (LoadLibraryUtil.class.getSimpleName() + C1235.m641(f625short, 1740743 ^ C1232.m631((Object) "ۙۙۧ"), 1738130 ^ C1232.m631((Object) "ۖۢۧ"), C1232.m631((Object) "۠ۜۡ") ^ 1748412));
    private static File lastSoDir = null;

    /* renamed from: short  reason: not valid java name */

    private static final class V14 {
        private V14() {
        }

        /* access modifiers changed from: private */
        public static void install(ClassLoader classLoader, File file) throws Throwable {
            LoadLibraryUtil.expandFieldArray(LoadLibraryUtil.findField(classLoader, "pathList").get(classLoader), "nativeLibraryDirectories", new File[]{file});
        }
    }

    private static final class V23 {

        /* renamed from: short  reason: not valid java name */
        private static final short[] f626short = {1385, 1400, 1389, 1393, 1365, 1392, 1386, 1389, 1933, 1922, 1943, 1930, 1941, 1926, 1967, 1930, 1921, 1937, 1922, 1937, 1946, 1959, 1930, 1937, 1926, 1920, 1943, 1932, 1937, 1930, 1926, 1936, 1232, 1221, 1172, 1240, 1245, 1238, 1264, 1245, 1222, 1277, 1216, 1178, 1222, 1233, 1241, 1243, 1218, 1233, 1180, 1181, 1172, 2014, 2004, 2014, 2009, 1992, 1984, 2019, 1996, 2009, 1988, 2011, 1992, 2017, 1988, 1999, 2015, 1996, 2015, 2004, 2025, 1988, 2015, 1992, 1998, 2009, 1986, 2015, 1988, 1992, 2014, 843, 862, 783, 860, 854, 860, 859, 842, 834, 867, 838, 845, 875, 838, 861, 860, 771, 860, 838, 853, 842, 786, 2146, 2158, 2148, 2154, 2143, 2158, 2171, 2151, 2122, 2147, 2154, 2146, 2154, 2145, 2171, 2172, 1113, 1110, 1091, 1118, 1089, 1106, 1147, 1118, 1109, 1093, 1110, 1093, 1102, 1127, 1110, 1091, 1119, 1138, 1115, 1106, 1114, 1106, 1113, 1091, 1092};

        private V23() {
        }

        /* access modifiers changed from: private */
//        public static void install(ClassLoader classLoader, File file) throws Throwable {
//            ArrayList arrayList;
//            Object obj = LoadLibraryUtil.findField(classLoader, C1236.m642(f626short, 1758448 ^ C1232.m631((Object) "۫ۦ۫"), 1742143 ^ C1232.m631((Object) "ۚۧۤ"), C1232.m631((Object) "ۦ۠ۡ") ^ 1754238)).get(classLoader);
//            List list = (List) LoadLibraryUtil.findField(obj, C1235.m641(f626short, 1752295 ^ C1232.m631((Object) "ۥۚۤ"), 1738843 ^ C1232.m631((Object) "ۗۙۥ"), C1232.m631((Object) "ۜۖۥ") ^ 1744200)).get(obj);
//            int i = 1616;
//            while (true) {
//                i ^= 1633;
//                switch (i) {
//                    case 14:
//                        break;
//                    case 49:
//                        if (list != null) {
//                            break;
//                        } else {
//                            i = 1709;
//                            continue;
//                        }
//                    case 204:
//                        arrayList = new ArrayList(2);
//                        break;
//                    case 239:
////                        arrayList = (ArrayList) list;
//                        break;
//                }
//                i = 1678;
//            }
////            Iterator it = arrayList.iterator();
//            while (true) {
////                boolean hasNext = it.hasNext();
//                int i2 = 1740;
//                while (true) {
//                    i2 ^= 1757;
//                    switch (i2) {
//                        case 17:
////                            i2 = hasNext ? 1833 : 1802;
//                        case 54:
//                        case 471:
//                            break;
//                        case 500:
////                            boolean equals = file.equals((File) it.next());
//                            int i3 = 1864;
//                            while (true) {
//                                i3 ^= 1881;
//                                switch (i3) {
//                                    case 17:
////                                        i3 = !equals ? 48736 : 48705;
//                                    case 47384:
//                                        break;
//                                    case 47417:
////                                        boolean equals2 = file.equals(LoadLibraryUtil.access$300());
//                                        int i4 = 48767;
//                                        while (true) {
//                                            i4 ^= 48784;
//                                            switch (i4) {
//                                                case 14:
//                                                case 45:
//                                                    int i5 = 49666;
//                                                    while (true) {
//                                                        i5 ^= 49683;
//                                                        switch (i5) {
//                                                            case 17:
//                                                                i5 = 49697;
//                                                            case 50:
//                                                        }
//                                                    }
//                                                    break;
//                                                case 76:
//                                                    int i6 = 48891;
//                                                    while (true) {
//                                                        i6 ^= 48908;
//                                                        switch (i6) {
//                                                            case 22:
//                                                                break;
//                                                            case 503:
//                                                                i6 = 48922;
//                                                                break;
//                                                        }
//                                                    }
//                                                    break;
//                                                case 239:
////                                                    i4 = equals2 ? 48860 : 48829;
//                                            }
//                                        }
//                                        break;
//                                    case 47483:
//                                }
//                            }
//                            break;
//                    }
//                }
//            }
////            it.remove();
////            Log.d(LoadLibraryUtil.access$400(), C1236.m642(f626short, 1738011 ^ C1232.m631((Object) "ۖ۟ۤ"), 1758522 ^ C1232.m631((Object) "۫ۨ۬"), C1232.m631((Object) "۠ۢ۬") ^ 1748894) + file.getAbsolutePath());
//            arrayList.add(0, file);
//            List list2 = (List) LoadLibraryUtil.findField(obj, C1236.m642(f626short, 1758952 ^ C1232.m631((Object) "۬ۗۨ"), 1741209 ^ C1232.m631((Object) "ۙۨۖ"), C1232.m631((Object) "ۥۜ۟") ^ 1751685)).get(obj);
//            int i7 = 49790;
//            while (true) {
//                i7 ^= 49807;
//                switch (i7) {
//                    case 18:
//                        break;
//                    case 51:
//                        break;
//                    case 84:
//                        list2 = new ArrayList(2);
//                        break;
//                    case 241:
//                        if (list2 != null) {
//                            break;
//                        } else {
//                            i7 = 49883;
//                            continue;
//                        }
//                }
//                i7 = 49852;
//            }
////            Log.d(LoadLibraryUtil.access$400(), C1235.m641(f626short, 1738469 ^ C1232.m631((Object) "ۖ۫۫"), 1759411 ^ C1232.m631((Object) "۬ۦ۟"), C1232.m631((Object) "ۨۖ۫") ^ 1754258) + list2.size());
//            Method findMethod = LoadLibraryUtil.findMethod(obj, C1235.m641(f626short, 1743543 ^ C1232.m631((Object) "ۜۘۚ"), 1738401 ^ C1232.m631((Object) "ۖ۫ۦ"), C1232.m631((Object) "ۘۙۚ") ^ 1737718), List.class, File.class, List.class);
//            ArrayList arrayList2 = new ArrayList();
//            arrayList.addAll(list2);
//            Object[] objArr = {arrayList, null, arrayList2};
//            Field findField = LoadLibraryUtil.findField(obj, C1234.m638(f626short, 1741071 ^ C1232.m631((Object) "ۙۧۤ"), 1749483 ^ C1232.m631((Object) "ۢۜ۬"), C1232.m631((Object) "ۙۤۗ") ^ 1742139));
//            findField.setAccessible(true);
//            findField.set(obj, (Object[]) findMethod.invoke(obj, objArr));
//        }
    }

    private static final class V25 {

        /* renamed from: short  reason: not valid java name */
        private static final short[] f627short = {1273, 1256, 1277, 1249, 1221, 1248, 1274, 1277, 1104, 1119, 1098, 1111, 1096, 1115, 1138, 1111, 1116, 1100, 1119, 1100, 1095, 1146, 1111, 1100, 1115, 1117, 1098, 1105, 1100, 1111, 1115, 1101, 2777, 2764, 2717, 2769, 2772, 2783, 2809, 2772, 2767, 2804, 2761, 2707, 2767, 2776, 2768, 2770, 2763, 2776, 2709, 2708, 2355, 2361, 2355, 2356, 2341, 2349, 2318, 2337, 2356, 2345, 2358, 2341, 2316, 2345, 2338, 2354, 2337, 2354, 2361, 2308, 2345, 2354, 2341, 2339, 2356, 2351, 2354, 2345, 2341, 2355, 2906, 2895, 2846, 2893, 2887, 2893, 2890, 2907, 2899, 2930, 2903, 2908, 2938, 2903, 2892, 2893, 2834, 2893, 2903, 2884, 2907, 2819, 2700, 2688, 2698, 2692, 2737, 2688, 2709, 2697, 2724, 2701, 2692, 2700, 2692, 2703, 2709, 2706, 1272, 1271, 1250, 1279, 1248, 1267, 1242, 1279, 1268, 1252, 1271, 1252, 1263, 1222, 1271, 1250, 1278, 1235, 1274, 1267, 1275, 1267, 1272, 1250, 1253};

        private V25() {
        }

        /* access modifiers changed from: private */
//        public static void install(ClassLoader classLoader, File file) throws Throwable {
//            ArrayList arrayList;
//            Object obj = LoadLibraryUtil.findField(classLoader, C1234.m638(f627short, 1739056 ^ C1232.m631((Object) "ۗۡۚ"), 1749782 ^ C1232.m631((Object) "ۢۦۢ"), C1232.m631((Object) "۬ۤۘ") ^ 1760489)).get(classLoader);
//            List list = (List) LoadLibraryUtil.findField(obj, C1235.m641(f627short, 1748451 ^ C1232.m631((Object) "ۡۚۤ"), 1743560 ^ C1232.m631((Object) "ۜۗ۫"), C1232.m631((Object) "ۖۛ۬") ^ 1736953)).get(obj);
//            int i = 49914;
//            while (true) {
//                i ^= 49931;
//                switch (i) {
//                    case 497:
//                        if (list != null) {
//                            break;
//                        } else {
//                            i = 50658;
//                            continue;
//                        }
//                    case 1711:
//                        break;
//                    case 1736:
////                        arrayList = list;
//                        break;
//                    case 1769:
//                        arrayList = new ArrayList(2);
//                        break;
//                }
//                i = 50627;
//            }
////            Iterator it = arrayList.iterator();
//            while (true) {
////                boolean hasNext = it.hasNext();
//                int i2 = 50689;
//                while (true) {
//                    i2 ^= 50706;
//                    switch (i2) {
//                        case 19:
////                            i2 = hasNext ? 50782 : 50751;
//                        case 45:
//                            break;
//                        case 50:
//                        case 76:
////                            boolean equals = file.equals((File) it.next());
//                            int i3 = 50813;
//                            while (true) {
//                                i3 ^= 50830;
//                                switch (i3) {
//                                    case 18:
//                                    case 53:
//                                        break;
//                                    case 243:
////                                        i3 = !equals ? 51557 : 50875;
//                                    case 4075:
////                                        boolean equals2 = file.equals(LoadLibraryUtil.access$300());
//                                        int i4 = 51588;
//                                        while (true) {
//                                            i4 ^= 51605;
//                                            switch (i4) {
//                                                case 17:
////                                                    i4 = equals2 ? 51681 : 51650;
//                                                case 54:
//                                                case 87:
//                                                    int i5 = 51836;
//                                                    while (true) {
//                                                        i5 ^= 51853;
//                                                        switch (i5) {
//                                                            case 241:
//                                                                i5 = 52518;
//                                                            case 1963:
//                                                        }
//                                                    }
//                                                    break;
//                                                case 116:
//                                                    int i6 = 51712;
//                                                    while (true) {
//                                                        i6 ^= 51729;
//                                                        switch (i6) {
//                                                            case 17:
//                                                                i6 = 51743;
//                                                                break;
//                                                        }
//                                                    }
//                                                    break;
//                                            }
//                                        }
//                                        break;
//                                }
//                            }
//                            break;
//                    }
//                }
//            }
////            it.remove();
////            Log.d(LoadLibraryUtil.access$400(), C1233.m636(f627short, 1758321 ^ C1232.m631((Object) "۫ۡۧ"), 1751217 ^ C1232.m631((Object) "ۤۖۗ"), C1232.m631((Object) "ۜۖۦ") ^ 1740817) + file.getAbsolutePath());
//            arrayList.add(0, file);
//            List list2 = (List) LoadLibraryUtil.findField(obj, C1236.m642(f627short, 1739829 ^ C1232.m631((Object) "ۘۙۢ"), 1737819 ^ C1232.m631((Object) "ۖۗۦ"), C1232.m631((Object) "۬۬ۖ") ^ 1757206)).get(obj);
//            int i7 = 52611;
//            while (true) {
//                i7 ^= 52628;
//                switch (i7) {
//                    case 23:
//                        if (list2 != null) {
//                            break;
//                        } else {
//                            i7 = 52704;
//                            continue;
//                        }
//                    case 54:
//                        break;
//                    case 85:
//                        break;
//                    case 116:
//                        list2 = new ArrayList(2);
//                        break;
//                }
//                i7 = 52673;
//            }
////            Log.d(LoadLibraryUtil.access$400(), C1233.m636(f627short, 1749491 ^ C1232.m631((Object) "ۢۚۙ"), 1755226 ^ C1232.m631((Object) "ۨۛ۟"), C1232.m631((Object) "ۖۥ۟") ^ 1740494) + list2.size());
//            Method findMethod = LoadLibraryUtil.findMethod(obj, C1233.m636(f627short, C1232.m631((Object) "ۛۤۡ") ^ 1743088, 1755170 ^ C1232.m631((Object) "ۨۚۤ"), C1232.m631((Object) "ۙ۬ۤ") ^ 1743088), List.class);
//            arrayList.addAll(list2);
//            Object[] objArr = {arrayList};
//            Field findField = LoadLibraryUtil.findField(obj, C1234.m638(f627short, 1754449 ^ C1232.m631((Object) "ۧ۠ۢ"), 1739769 ^ C1232.m631((Object) "ۘۘ۠"), C1232.m631((Object) "۟۠ۜ") ^ 1745805));
//            findField.setAccessible(true);
//            findField.set(obj, (Object[]) findMethod.invoke(obj, objArr));
//        }
    }

//    static {
//        while (true) {
//            int i = 52735;
//            while (true) {
//                i ^= 52752;
//                switch (i) {
//                    case 14:
//                        char c = 364;
//                        while (true) {
//                            switch (c) {
//                                case 37940:
////                                    return;
//                                case 364:
//                                    int i2 = 53634;
//                                    while (true) {
//                                        i2 ^= 53651;
//                                        switch (i2) {
//                                            case 17:
//                                                i2 = 53665;
//                                            case 50:
//                                                int i3 = 53758;
//                                                while (true) {
//                                                    i3 ^= 53775;
//                                                    switch (i3) {
//                                                        case ogrConstants.wkbCompoundCurveZ /* 1009 */:
//                                                            i3 = 54440;
//                                                        case 1703:
//                                                            c = 37940;
//                                                    }
//                                                }
//                                                break;
//                                        }
//                                    }
//                                    break;
//                                default:
//                                    int i4 = 53510;
//                                    while (true) {
//                                        i4 ^= 53527;
//                                        switch (i4) {
//                                            case 17:
//                                                i4 = 53541;
//                                            case 50:
//                                        }
//                                    }
//                                    break;
//                            }
//                        }
//                    case 1007:
//                        i = 52766;
//                }
//            }
//        }
//    }

    public LoadLibraryUtil() {
        while (true) {
            int i = 54533;
            while (true) {
                i ^= 54550;
                switch (i) {
                    case 19:
                        i = 54564;
                    case 50:
                        char c = 20437;
                        while (true) {
                            switch (c) {
                                case 25133:
                                    return;
                                case 20437:
                                    int i2 = 55432;
                                    while (true) {
                                        i2 ^= 55449;
                                        switch (i2) {
                                            case 17:
                                                i2 = 55463;
                                            case 62:
                                                int i3 = 55556;
                                                while (true) {
                                                    i3 ^= 55573;
                                                    switch (i3) {
                                                        case 17:
                                                            i3 = 55587;
                                                        case 54:
                                                            c = 25133;
                                                    }
                                                }
//                                                break;
                                        }
                                    }
//                                    break;
                                default:
                                    int i4 = 54657;
                                    while (true) {
                                        i4 ^= 54674;
                                        switch (i4) {
                                            case 19:
                                                i4 = 54688;
                                            case 50:
                                        }
                                    }
//                                    break;
                            }
                        }
                }
            }
        }
    }

//    static /* synthetic */ File access$300() {
//        while (true) {
//            int i = 55680;
//            while (true) {
//                i ^= 55697;
//                switch (i) {
//                    case 17:
//                        i = 56362;
//                    case 1467:
//                        char c = 61739;
//                        while (true) {
//                            switch (c) {
//                                case 61739:
//                                    int i2 = 56579;
//                                    while (true) {
//                                        i2 ^= 56596;
//                                        switch (i2) {
//                                            case 23:
//                                                i2 = 56610;
//                                            case 54:
//                                                int i3 = 1508526;
//                                                while (true) {
//                                                    i3 ^= 1508543;
//                                                    switch (i3) {
//                                                        case 17:
//                                                            i3 = 1508557;
//                                                        case 114:
//                                                            c = 3423;
//                                                    }
//                                                }
//                                                break;
//                                        }
//                                    }
//                                    break;
//                                case 3423:
//                                    File file = lastSoDir;
//                                    int i4 = 1508650;
//                                    while (true) {
//                                        i4 ^= 1508667;
//                                        switch (i4) {
//                                            case 17:
//                                                i4 = 1508681;
//                                                break;
//                                            case 114:
//                                                while (true) {
//                                                    int i5 = 1507503;
//                                                    while (true) {
//                                                        i5 ^= 1507520;
//                                                        switch (i5) {
//                                                            case 14:
//                                                                char c2 = 48271;
//                                                                while (true) {
//                                                                    switch (c2) {
//                                                                        case 48271:
//                                                                            int i6 = 1507751;
//                                                                            while (true) {
//                                                                                i6 ^= 1507768;
//                                                                                switch (i6) {
//                                                                                    case 31:
//                                                                                        i6 = 1508433;
//                                                                                    case 1513:
//                                                                                        int i7 = 1509425;
//                                                                                        while (true) {
//                                                                                            i7 ^= 1509442;
//                                                                                            switch (i7) {
//                                                                                                case 18:
//                                                                                                    c2 = 48985;
//                                                                                                case 115:
//                                                                                                    i7 = 1509456;
//                                                                                            }
//                                                                                        }
//                                                                                        break;
//                                                                                }
//                                                                            }
//                                                                            break;
//                                                                        case 48985:
//                                                                            return file;
//                                                                        default:
//                                                                            int i8 = 1507627;
//                                                                            while (true) {
//                                                                                i8 ^= 1507644;
//                                                                                switch (i8) {
//                                                                                    case 23:
//                                                                                        i8 = 1507658;
//                                                                                    case 118:
//                                                                                }
//                                                                            }
//                                                                            break;
//                                                                    }
//                                                                }
//                                                            case 111:
//                                                                i5 = 1507534;
//                                                        }
//                                                    }
//                                                }
//                                                break;
//                                        }
//                                    }
//                                    break;
//                                default:
//                                    int i9 = 56455;
//                                    while (true) {
//                                        i9 ^= 56472;
//                                        switch (i9) {
//                                            case 31:
//                                                i9 = 56486;
//                                            case 62:
//                                        }
//                                    }
//                                    break;
//                            }
//                        }
//                }
//            }
//        }
//    }

//    static /* synthetic */ String access$400() {
//        while (true) {
//            int i = 1509549;
//            while (true) {
//                i ^= 1509566;
//                switch (i) {
//                    case 19:
//                        i = 1509580;
//                    case 114:
//                        char c = 34864;
//                        while (true) {
//                            switch (c) {
//                                case 33024:
//                                    String str = TAG;
//                                    int i2 = 1511595;
//                                    while (true) {
//                                        i2 ^= 1511612;
//                                        switch (i2) {
//                                            case 23:
//                                                i2 = 1512277;
//                                                break;
//                                            case 1001:
//                                                while (true) {
//                                                    int i3 = 1510572;
//                                                    while (true) {
//                                                        i3 ^= 1510589;
//                                                        switch (i3) {
//                                                            case 17:
//                                                                i3 = 1510603;
//                                                            case 118:
//                                                                char c2 = 21814;
//                                                                while (true) {
//                                                                    switch (c2) {
//                                                                        case 21814:
//                                                                            int i4 = 1511471;
//                                                                            while (true) {
//                                                                                i4 ^= 1511488;
//                                                                                switch (i4) {
//                                                                                    case 14:
//                                                                                        int i5 = 1512494;
//                                                                                        while (true) {
//                                                                                            i5 ^= 1512511;
//                                                                                            switch (i5) {
//                                                                                                case 17:
//                                                                                                    i5 = 1512525;
//                                                                                                case 114:
//                                                                                                    c2 = 23503;
//                                                                                            }
//                                                                                        }
//                                                                                        break;
//                                                                                    case 111:
//                                                                                        i4 = 1511502;
//                                                                                }
//                                                                            }
//                                                                            break;
//                                                                        case 23503:
//                                                                            return str;
//                                                                        default:
//                                                                            int i6 = 1511347;
//                                                                            while (true) {
//                                                                                i6 ^= 1511364;
//                                                                                switch (i6) {
//                                                                                    case 22:
//                                                                                    case 119:
//                                                                                        i6 = 1511378;
//                                                                                }
//                                                                            }
//                                                                            break;
//                                                                    }
//                                                                }
//                                                        }
//                                                    }
//                                                }
//                                                break;
//                                        }
//                                    }
//                                    break;
//                                case 34864:
//                                    int i7 = 1510448;
//                                    while (true) {
//                                        i7 ^= 1510465;
//                                        switch (i7) {
//                                            case 14:
//                                                int i8 = 1512370;
//                                                while (true) {
//                                                    i8 ^= 1512387;
//                                                    switch (i8) {
//                                                        case 18:
//                                                            c = 33024;
//                                                        case 113:
//                                                            i8 = 1512401;
//                                                    }
//                                                }
//                                                break;
//                                            case 113:
//                                                i7 = 1510479;
//                                        }
//                                    }
//                                    break;
//                                default:
//                                    int i9 = 1509673;
//                                    while (true) {
//                                        i9 ^= 1509690;
//                                        switch (i9) {
//                                            case 19:
//                                                i9 = 1510355;
//                                            case 745:
//                                        }
//                                    }
//                                    break;
//                            }
//                        }
//                }
//            }
//        }
//    }

    public static void expandFieldArray(Object obj, String str, Object[] objArr) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field findField = findField(obj, str);
        Object[] objArr2 = (Object[]) findField.get(obj);
        Object[] objArr3 = (Object[]) Array.newInstance(objArr2.getClass().getComponentType(), objArr2.length + objArr.length);
        System.arraycopy(objArr, 0, objArr3, 0, objArr.length);
        System.arraycopy(objArr2, 0, objArr3, objArr.length, objArr2.length);
        findField.set(obj, objArr3);
//        while (true) {
//            int i = 1513269;
//            while (true) {
//                i ^= 1513286;
//                switch (i) {
//                    case 18:
//                        char c = 'p';
//                        while (true) {
//                            switch (c) {
//                                case 'p':
//                                    int i2 = 1513517;
//                                    while (true) {
//                                        i2 ^= 1513534;
//                                        switch (i2) {
//                                            case 19:
//                                                i2 = 1514199;
//                                            case 745:
//                                                int i3 = 1514292;
//                                                while (true) {
//                                                    i3 ^= 1514309;
//                                                    switch (i3) {
//                                                        case 22:
//                                                            c = 5613;
//                                                        case 113:
//                                                            i3 = 1514323;
//                                                    }
//                                                }
//                                                break;
//                                        }
//                                    }
//                                    break;
//                                case 5613:
//                                    return;
//                                default:
//                                    int i4 = 1513393;
//                                    while (true) {
//                                        i4 ^= 1513410;
//                                        switch (i4) {
//                                            case 18:
//                                            case 115:
//                                                i4 = 1513424;
//                                        }
//                                    }
//                                    break;
//                            }
//                        }
//                    case 115:
//                        i = 1513300;
//                }
//            }
//        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: CFG modification limit reached, blocks count: 262
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:72)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:46)
        */
    public static Field findField(Object r7, String r8) throws NoSuchFieldException {
        /*
        // Method dump skipped, instructions count: 946
        */
        throw new UnsupportedOperationException("Method not decompiled: com.minusoneapp.LoadLibraryUtil.findField(java.lang.Object, java.lang.String):java.lang.reflect.Field");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: CFG modification limit reached, blocks count: 256
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:72)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:46)
        */
    public static Method findMethod(Object r7, String r8, Class<?>... r9) throws NoSuchMethodException {
        /*
        // Method dump skipped, instructions count: 992
        */
        throw new UnsupportedOperationException("Method not decompiled: com.minusoneapp.LoadLibraryUtil.findMethod(java.lang.Object, java.lang.String, java.lang.Class[]):java.lang.reflect.Method");
    }

//    private static int getPreviousSdkInt() {
//        while (true) {
//            int i = 1575796;
//            while (true) {
//                i ^= 1575813;
//                switch (i) {
//                    case 22:
//                        char c = 57717;
//                        while (true) {
//                            switch (c) {
//                                case 20785:
//                                    int i2 = Build.VERSION.PREVIEW_SDK_INT;
//                                    int i3 = 1598798;
//                                    while (true) {
//                                        i3 ^= 1598815;
//                                        switch (i3) {
//                                            case 17:
//                                                i3 = 1598829;
//                                                break;
//                                            case 50:
//                                                while (true) {
//                                                    int i4 = 1597000;
//                                                    while (true) {
//                                                        i4 ^= 1597017;
//                                                        switch (i4) {
//                                                            case 17:
//                                                                i4 = 1597031;
//                                                            case 62:
//                                                                char c2 = 39553;
//                                                                while (true) {
//                                                                    switch (c2) {
//                                                                        case 9063:
//                                                                            return i2;
//                                                                        case 39553:
//                                                                            int i5 = 1597899;
//                                                                            while (true) {
//                                                                                i5 ^= 1597916;
//                                                                                switch (i5) {
//                                                                                    case 23:
//                                                                                        i5 = 1597930;
//                                                                                    case 54:
//                                                                                        int i6 = 1598922;
//                                                                                        while (true) {
//                                                                                            i6 ^= 1598939;
//                                                                                            switch (i6) {
//                                                                                                case 17:
//                                                                                                    i6 = 1598953;
//                                                                                                case 50:
//                                                                                                    c2 = 9063;
//                                                                                            }
//                                                                                        }
//                                                                                        break;
//                                                                                }
//                                                                            }
//                                                                            break;
//                                                                        default:
//                                                                            int i7 = 1597124;
//                                                                            while (true) {
//                                                                                i7 ^= 1597141;
//                                                                                switch (i7) {
//                                                                                    case 17:
//                                                                                        i7 = 1597806;
//                                                                                    case 16315:
//                                                                                }
//                                                                            }
//                                                                            break;
//                                                                    }
//                                                                }
//                                                        }
//                                                    }
//                                                }
//                                                break;
//                                        }
//                                    }
//                                    break;
//                                case 57717:
//                                    int i8 = 1596876;
//                                    while (true) {
//                                        i8 ^= 1596893;
//                                        switch (i8) {
//                                            case 17:
//                                                i8 = 1596907;
//                                            case 54:
//                                                int i9 = 1598023;
//                                                while (true) {
//                                                    i9 ^= 1598040;
//                                                    switch (i9) {
//                                                        case 31:
//                                                            i9 = 1598054;
//                                                        case 62:
//                                                            c = 20785;
//                                                    }
//                                                }
//                                                break;
//                                        }
//                                    }
//                                    break;
//                                default:
//                                    int i10 = 1575920;
//                                    while (true) {
//                                        i10 ^= 1575937;
//                                        switch (i10) {
//                                            case 14:
//                                            case 2033:
//                                                i10 = 1575951;
//                                        }
//                                    }
//                                    break;
//                            }
//                        }
//                    case 241:
//                        i = 1575827;
//                }
//            }
//        }
//    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: CFG modification limit reached, blocks count: 796
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:72)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:46)
        */
    public static boolean installNativeLibraryPath(ClassLoader r8, File r9) throws Throwable {
        /*
        // Method dump skipped, instructions count: 3982
        */
        throw new UnsupportedOperationException("Method not decompiled: com.minusoneapp.LoadLibraryUtil.installNativeLibraryPath(java.lang.ClassLoader, java.io.File):boolean");
    }

    /* renamed from: ۖۥۙۚ͗  reason: not valid java name and contains not printable characters */
//    public static String m630(String str) {
//        char[] charArray = str.toCharArray();
//        int i = 0;
//        while (true) {
//            int length = charArray.length;
//            int i2 = 1749799;
//            while (true) {
//                i2 ^= 1749816;
//                switch (i2) {
//                    case 31:
//                        i2 = i >= length ? 1749892 : 1749861;
//                    case 93:
//                        charArray[i] = (char) ((char) (charArray[i] ^ 20978));
//                        int length2 = charArray.length;
//                        int i3 = 1749923;
//                        while (true) {
//                            i3 ^= 1749940;
//                            switch (i3) {
//                                case 23:
//                                    i3 = i + 1 < length2 ? 1750667 : 1750636;
//                                case 1343:
//                                    i++;
//                                    charArray[i] = (char) ((char) (charArray[i] ^ i));
//                                    break;
//                                case 1496:
//                                    break;
//                                case 1529:
//                            }
//                        }
//                        i++;
//                        int i4 = 1750698;
//                        while (true) {
//                            i4 ^= 1750715;
//                            switch (i4) {
//                                case 17:
//                                    i4 = 1750729;
//                                case 114:
//                            }
//                        }
//                        break;
//                    case TransportMediator.KEYCODE_MEDIA_PLAY /* 126 */:
//                    case 188:
//                        return new String(charArray);
//                }
//            }
//        }
//    }
}
