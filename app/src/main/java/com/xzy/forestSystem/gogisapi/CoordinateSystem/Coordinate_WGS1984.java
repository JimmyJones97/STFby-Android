package  com.xzy.forestSystem.gogisapi.CoordinateSystem;

import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;

public class Coordinate_WGS1984 extends AbstractC0383CoordinateSystem {
    static final double LatMax = 90.0d;
    static Coordinate_WGS1984 m_Coordinate_WGS1984 = null;

    public Coordinate_WGS1984() {
        this._Name = "WGS-84坐标";
        this._DefaultSpheroidName = "WGS84";
        this._CoordinateSystemType = ECoordinateSystemType.enWGS1984;
        this._IsProjectionCoord = false;
        this.f462_A = 6378137.0d;
        this.f463_B = 6356752.3142d;
        this.f464_f = 298.257223563d;
        this._PMTranslate.SetPMCoorTransMethodName("无");
    }

    public static Coordinate_WGS1984 Instance() {
        if (m_Coordinate_WGS1984 == null) {
            m_Coordinate_WGS1984 = new Coordinate_WGS1984();
        }
        return m_Coordinate_WGS1984;
    }

    @Override //  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem
    public Coordinate ConvertXYToBL(double x, double y) {
        return XYToBL(x, y);
    }

    @Override //  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem
    public Coordinate ConvertBLToXY(double longitude, double latitude) {
        return BLToXY(longitude, latitude);
    }

    public static Coordinate XYToBL(double x, double y) {
        return new Coordinate(x, y, (x * 180.0d) / 2.0037508342789244E7d, (Math.atan(Math.exp(0.0174532925199433d * ((180.0d * y) / 2.0037508342789244E7d))) / 0.008726646259971648d) - LatMax);
    }

    public static Coordinate BLToXY(double longitude, double latitude) {
        double[] tmpValues = BLToXYValues(longitude, latitude);
        return new Coordinate(tmpValues[0], tmpValues[1], longitude, latitude);
    }

    public static double[] BLToXYValues(double longitude, double latitude) {
        return new double[]{(longitude * 2.0037508342789244E7d) / 180.0d, ((Math.log(Math.tan((3.141592653589793d * (LatMax + latitude)) / 360.0d)) / 0.0174532925199433d) * 2.0037508342789244E7d) / 180.0d};
    }

    @Override //  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem
    public String ToCoordSystemFileInfo() {
        return "GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]]";
    }
}
