package  com.xzy.forestSystem.gogisapi.CoordinateSystem;

import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;

public class Coordinate_WebMercator extends AbstractC0383CoordinateSystem {
    public Coordinate_WebMercator() {
        this._Name = "WebMercator";
        this._DefaultSpheroidName = "WGS84";
        this._CoordinateSystemType = ECoordinateSystemType.enWebMercator;
        this._IsProjectionCoord = true;
        this.f462_A = 6378137.0d;
        this.f463_B = 6356752.3142d;
        this.f464_f = 298.257223563d;
        this._PMTranslate.SetPMCoorTransMethodName("无");
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
        return new Coordinate(x, y, (x * 180.0d) / 2.0037508342789244E7d, (Math.atan(Math.exp(0.0174532925199433d * ((180.0d * y) / 2.0037508342789244E7d))) / 0.008726646259971648d) - 90.0d);
    }

    public static Coordinate BLToXY(double longitude, double latitude) {
        return new Coordinate((longitude * 2.0037508342789244E7d) / 180.0d, ((Math.log(Math.tan((3.141592653589793d * (90.0d + latitude)) / 360.0d)) / 0.0174532925199433d) * 2.0037508342789244E7d) / 180.0d, longitude, latitude);
    }

    @Override //  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem
    public String ToCoordSystemFileInfo() {
        return "PROJCS[\"WGS_1984_Web_Mercator_Auxiliary_Sphere\",GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Mercator_Auxiliary_Sphere\"],PARAMETER[\"False_Easting\",0.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",0.0],PARAMETER[\"Standard_Parallel_1\",0.0],PARAMETER[\"Auxiliary_Sphere_Type\",0.0],UNIT[\"Meter\",1.0]]";
    }
}
