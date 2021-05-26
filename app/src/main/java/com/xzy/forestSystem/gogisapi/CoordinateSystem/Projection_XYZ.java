package  com.xzy.forestSystem.gogisapi.CoordinateSystem;

public class Projection_XYZ {
    public static double[] ConvertBLHToXYZ(double longitude, double latitude, double elevation, AbstractC0383CoordinateSystem coordinateSystem) {
        double d1 = coordinateSystem.GetA();
        double d2 = coordinateSystem.GetB();
        double tempLong = longitude * 0.0174532925199433d;
        double tempLat = latitude * 0.0174532925199433d;
        double d3 = ((d1 * d1) - (d2 * d2)) / (d1 * d1);
        double d4 = d1 / Math.sqrt(1.0d - ((Math.sin(tempLat) * d3) * Math.sin(tempLat)));
        return new double[]{(d4 + elevation) * Math.cos(tempLat) * Math.cos(tempLong), (d4 + elevation) * Math.cos(tempLat) * Math.sin(tempLong), (((1.0d - d3) * d4) + elevation) * Math.sin(tempLat)};
    }

    public static double[] ConvertXYZToBLH(double x, double y, double z, AbstractC0383CoordinateSystem coordinateSystem) {
        double tmpL = 0.0d;
        if (x != 0.0d) {
            tmpL = Math.atan(y / x);
        }
        double tmpXYSqrt = Math.sqrt((x * x) + (y * y));
        Math.atan(z / tmpXYSqrt);
        double d1 = coordinateSystem.GetA();
        double d2 = coordinateSystem.GetB();
        double tmpe2 = ((d1 * d1) - (d2 * d2)) / (d1 * d1);
        double tempAngle = Math.atan((z * d1) / (tmpXYSqrt * d2));
        double tmpSinAngle = Math.sin(tempAngle);
        double tmpCosAngle = Math.cos(tempAngle);
        double tmpB = Math.atan(((((((((d1 * d1) - (d2 * d2)) / (d2 * d2)) * d2) * tmpSinAngle) * tmpSinAngle) * tmpSinAngle) + z) / (tmpXYSqrt - ((((tmpe2 * d1) * tmpCosAngle) * tmpCosAngle) * tmpCosAngle)));
        double tmpSinB = Math.sin(tmpB);
        double tmpH = (tmpXYSqrt / Math.cos(tmpB)) - (d1 / Math.sqrt(1.0d - ((tmpe2 * tmpSinB) * tmpSinB)));
        double tmpL2 = tmpL * 57.29577951308232d;
        if (tmpL2 < 0.0d) {
            tmpL2 += 180.0d;
        }
        return new double[]{tmpL2, tmpB * 57.29577951308232d, tmpH};
    }
}
