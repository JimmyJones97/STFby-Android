package  com.xzy.forestSystem.gogisapi.CoordinateSystem;

public class CoordCorrect {

    /* renamed from: a */
    static final double f459a = 6378245.0d;

    /* renamed from: ee */
    static final double f460ee = 0.006693421622965943d;

    /* renamed from: pi */
    static final double f461pi = 3.141592653589793d;

    public static void transform(double wgLat, double wgLon, double[] latlng) {
        if (outOfChina(wgLat, wgLon)) {
            latlng[0] = wgLat;
            latlng[1] = wgLon;
            return;
        }
        double dLat = transformLat(wgLon - 105.0d, wgLat - 35.0d);
        double dLon = transformLon(wgLon - 105.0d, wgLat - 35.0d);
        double radLat = (wgLat / 180.0d) * f461pi;
        double magic = Math.sin(radLat);
        double magic2 = 1.0d - ((f460ee * magic) * magic);
        double sqrtMagic = Math.sqrt(magic2);
        double dLat2 = (180.0d * dLat) / ((6335552.717000426d / (magic2 * sqrtMagic)) * f461pi);
        double dLon2 = (180.0d * dLon) / (((f459a / sqrtMagic) * Math.cos(radLat)) * f461pi);
        latlng[0] = wgLat + dLat2;
        latlng[1] = wgLon + dLon2;
    }

    private static boolean outOfChina(double lat, double lon) {
        if (lon < 72.004d || lon > 137.8347d || lat < 0.8293d || lat > 55.8271d) {
            return true;
        }
        return false;
    }

    private static double transformLat(double x, double y) {
        return -100.0d + (2.0d * x) + (3.0d * y) + (0.2d * y * y) + (0.1d * x * y) + (0.2d * Math.sqrt(Math.abs(x))) + ((((20.0d * Math.sin((6.0d * x) * f461pi)) + (20.0d * Math.sin((2.0d * x) * f461pi))) * 2.0d) / 3.0d) + ((((20.0d * Math.sin(f461pi * y)) + (40.0d * Math.sin((y / 3.0d) * f461pi))) * 2.0d) / 3.0d) + ((((160.0d * Math.sin((y / 12.0d) * f461pi)) + (320.0d * Math.sin((f461pi * y) / 30.0d))) * 2.0d) / 3.0d);
    }

    private static double transformLon(double x, double y) {
        return 300.0d + x + (2.0d * y) + (0.1d * x * x) + (0.1d * x * y) + (0.1d * Math.sqrt(Math.abs(x))) + ((((20.0d * Math.sin((6.0d * x) * f461pi)) + (20.0d * Math.sin((2.0d * x) * f461pi))) * 2.0d) / 3.0d) + ((((20.0d * Math.sin(f461pi * x)) + (40.0d * Math.sin((x / 3.0d) * f461pi))) * 2.0d) / 3.0d) + ((((150.0d * Math.sin((x / 12.0d) * f461pi)) + (300.0d * Math.sin((x / 30.0d) * f461pi))) * 2.0d) / 3.0d);
    }
}
