package  com.xzy.forestSystem.gogisapi.CoordinateSystem;

import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;

public class Coordinate_UTM extends ProjectionCoordinateSystem {
    static double UTMScaleFactor = 0.9996d;

    /* renamed from: pi */
    static double f465pi = 3.141592653589793d;

    public Coordinate_UTM() {
        this._Name = "UTM坐标";
        this._DefaultSpheroidName = "WGS84";
        this._CoordinateSystemType = ECoordinateSystemType.enWGS1984UTM;
        this.f462_A = 6378137.0d;
        this.f463_B = 6356752.3142d;
        this.f464_f = 298.257223563d;
    }

    @Override //  com.xzy.forestSystem.gogisapi.CoordinateSystem.ProjectionCoordinateSystem,  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem
    public Coordinate ConvertXYToBL(double x, double y) {
        double[] tmpLngLat = XY2LngLat(x, y, (double) this._FenQu, this.f462_A, this.f463_B);
        return new Coordinate(x, y, tmpLngLat[0], tmpLngLat[1]);
    }

    @Override //  com.xzy.forestSystem.gogisapi.CoordinateSystem.ProjectionCoordinateSystem,  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem
    public Coordinate ConvertBLToXY(double longitude, double latitude) {
        double[] tmpXY = LatLonToUTM(latitude, longitude, (double) this._CenterMeridian, this.f462_A, this.f463_B);
        return new Coordinate(tmpXY[0], tmpXY[1], longitude, latitude);
    }

    public static int GetDH(double longitude) {
        return (int) (Math.floor((180.0d + longitude) / 6.0d) + 1.0d);
    }

    public static double[] LatLonToUTM(double lat, double lon, double centerLine, double sm_a, double sm_b) {
        double[] xy = latLonToXY((lat / 180.0d) * f465pi, (lon / 180.0d) * f465pi, (centerLine / 180.0d) * f465pi, sm_a, sm_b);
        xy[0] = (xy[0] * UTMScaleFactor) + 500000.0d;
        xy[1] = xy[1] * UTMScaleFactor;
        if (xy[1] < 0.0d) {
            xy[1] = xy[1] + 1.0E7d;
        }
        return new double[]{xy[0], xy[1], centerLine};
    }

    public static double[] LatLonToUTMAuto(double lat, double lon, double sm_a, double sm_b) {
        double zone = Math.floor((180.0d + lon) / 6.0d) + 1.0d;
        double[] xy = latLonToXY((lat / 180.0d) * f465pi, (lon / 180.0d) * f465pi, GetUTMCentralMeridian(zone), sm_a, sm_b);
        xy[0] = (xy[0] * UTMScaleFactor) + 500000.0d;
        xy[1] = xy[1] * UTMScaleFactor;
        if (xy[1] < 0.0d) {
            xy[1] = xy[1] + 1.0E7d;
        }
        return new double[]{xy[0], xy[1], zone};
    }

    public static double GetUTMCenterline(double zone) {
        return -183.0d + (6.0d * zone);
    }

    public static double GetUTMCentralMeridian(double zone) {
        return ((-183.0d + (6.0d * zone)) / 180.0d) * f465pi;
    }

    static double[] latLonToXY(double phi, double lambda, double lambda0, double sm_a, double sm_b) {
        double[] dArr = new double[2];
        double nu2 = ((Math.pow(sm_a, 2.0d) - Math.pow(sm_b, 2.0d)) / Math.pow(sm_b, 2.0d)) * Math.pow(Math.cos(phi), 2.0d);
        double N = Math.pow(sm_a, 2.0d) / (Math.sqrt(1.0d + nu2) * sm_b);
        double t = Math.tan(phi);
        double t2 = t * t;
        double pow = ((t2 * t2) * t2) - Math.pow(t, 6.0d);
        double l = lambda - lambda0;
        return new double[]{(Math.cos(phi) * N * l) + ((N / 6.0d) * Math.pow(Math.cos(phi), 3.0d) * ((1.0d - t2) + nu2) * Math.pow(l, 3.0d)) + ((N / 120.0d) * Math.pow(Math.cos(phi), 5.0d) * ((((5.0d - (18.0d * t2)) + (t2 * t2)) + (14.0d * nu2)) - ((58.0d * t2) * nu2)) * Math.pow(l, 5.0d)) + ((N / 5040.0d) * Math.pow(Math.cos(phi), 7.0d) * (((61.0d - (479.0d * t2)) + (179.0d * (t2 * t2))) - ((t2 * t2) * t2)) * Math.pow(l, 7.0d)), ArcLengthOfMeridian(phi, sm_a, sm_b) + ((t / 2.0d) * N * Math.pow(Math.cos(phi), 2.0d) * Math.pow(l, 2.0d)) + ((t / 24.0d) * N * Math.pow(Math.cos(phi), 4.0d) * ((5.0d - t2) + (9.0d * nu2) + (4.0d * nu2 * nu2)) * Math.pow(l, 4.0d)) + ((t / 720.0d) * N * Math.pow(Math.cos(phi), 6.0d) * ((((61.0d - (58.0d * t2)) + (t2 * t2)) + (270.0d * nu2)) - ((330.0d * t2) * nu2)) * Math.pow(l, 6.0d)) + ((t / 40320.0d) * N * Math.pow(Math.cos(phi), 8.0d) * (((1385.0d - (3111.0d * t2)) + (543.0d * (t2 * t2))) - ((t2 * t2) * t2)) * Math.pow(l, 8.0d))};
    }

    static double ArcLengthOfMeridian(double phi, double sm_a, double sm_b) {
        double n = (sm_a - sm_b) / (sm_a + sm_b);
        return ((sm_a + sm_b) / 2.0d) * (1.0d + (Math.pow(n, 2.0d) / 4.0d) + (Math.pow(n, 4.0d) / 64.0d)) * ((Math.sin(2.0d * phi) * (((-3.0d * n) / 2.0d) + ((9.0d * Math.pow(n, 3.0d)) / 16.0d) + ((-3.0d * Math.pow(n, 5.0d)) / 32.0d))) + phi + (Math.sin(4.0d * phi) * (((15.0d * Math.pow(n, 2.0d)) / 16.0d) + ((-15.0d * Math.pow(n, 4.0d)) / 32.0d))) + (Math.sin(6.0d * phi) * (((-35.0d * Math.pow(n, 3.0d)) / 48.0d) + ((105.0d * Math.pow(n, 5.0d)) / 256.0d))) + (Math.sin(8.0d * phi) * ((315.0d * Math.pow(n, 4.0d)) / 512.0d)));
    }

    public static double[] XY2LngLat(double x, double y, double huso, double sm_a, double sm_b) {
        return XY2LngLat(x, y, huso, 1, sm_a, sm_b);
    }

    public static double[] XY2LngLat(double x, double y, double centerLineCode, int hemisfery, double sm_a, double sm_b) {
        double c = Math.pow(sm_a, 2.0d) / sm_b;
        double ep2 = (Math.pow(sm_a, 2.0d) - Math.pow(sm_b, 2.0d)) / Math.pow(sm_b, 2.0d);
        double x2 = x - 500000.0d;
        if (hemisfery == -1) {
            y -= 1.0E7d;
        }
        double fip = y / 6363651.2449104d;
        double v = (0.9996d * c) / Math.sqrt(1.0d + (Math.pow(Math.cos(fip), 2.0d) * ep2));
        double aa = x2 / v;
        double A1 = Math.sin(2.0d * fip);
        double A2 = A1 * Math.pow(Math.cos(fip), 2.0d);
        double J2 = fip + (A1 / 2.0d);
        double J4 = ((3.0d * J2) + A2) / 4.0d;
        double J6 = ((5.0d * J4) + (Math.pow(Math.cos(fip), 2.0d) * A2)) / 3.0d;
        double alfa = 0.75d * ep2;
        double S = ((Math.pow(aa, 2.0d) * ep2) * Math.pow(Math.cos(fip), 2.0d)) / 2.0d;
        double CC = aa * (1.0d - (S / 3.0d));
        double n = ((1.0d - S) * ((y - ((0.9996d * c) * (((fip - (alfa * J2)) + ((1.6666666666666667d * Math.pow(alfa, 2.0d)) * J4)) - ((1.2962962962962963d * Math.pow(alfa, 3.0d)) * J6)))) / v)) + fip;
        double ilam = Math.atan(((Math.pow(2.718281828459045d, CC) - Math.pow(2.718281828459045d, -CC)) / 2.0d) / Math.cos(n));
        double tau = Math.atan(Math.cos(ilam) * Math.tan(n));
        return new double[]{((180.0d * ilam) / 3.141592653589793d) + ((6.0d * centerLineCode) - 183.0d), (180.0d * (fip + (((1.0d + (Math.pow(Math.cos(fip), 2.0d) * ep2)) - ((((1.5d * ep2) * Math.sin(fip)) * Math.cos(fip)) * (tau - fip))) * (tau - fip)))) / 3.141592653589793d};
    }
}
