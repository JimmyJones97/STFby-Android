package  com.xzy.forestSystem.gogisapi.CoordinateSystem;

public class Coordinate_CGCS2000 extends Projection_GuassKruger {
    public Coordinate_CGCS2000() {
        this._Name = "2000国家大地坐标系";
        this._DefaultSpheroidName = "国家2000";
        this._CoordinateSystemType = ECoordinateSystemType.enCGCS2000;
        this.f462_A = 6378137.0d;
        this.f463_B = 6356752.31414d;
        this.f464_f = 298.257222101d;
        this._CenterMeridian = 120.0f;
        this._DaiHao = 3.0f;
        this._FenQu = 40;
    }

    @Override //  com.xzy.forestSystem.gogisapi.CoordinateSystem.ProjectionCoordinateSystem,  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem
    public String ToCoordSystemFileInfo() {
        String tmpPrjName;
        Object[] arrayOfObject = new Object[7];
        arrayOfObject[6] = "CGCS2000";
        boolean tmpEastHashCode = false;
        if (String.valueOf(GetEasting()).startsWith(String.valueOf(getFenQu()))) {
            tmpEastHashCode = true;
        }
        if (this._DaiHao == 3.0f) {
            if (tmpEastHashCode) {
                tmpPrjName = String.valueOf("CGCS2000_") + String.valueOf((int) getDaiHao()) + "_Degree_GK_Zone_" + String.valueOf(getFenQu());
            } else {
                tmpPrjName = String.valueOf("CGCS2000_") + String.valueOf((int) getDaiHao()) + "_Degree_GK_CM_" + String.valueOf((int) GetCenterMeridian()) + "E";
            }
        } else if (tmpEastHashCode) {
            tmpPrjName = String.valueOf("CGCS2000_") + "GK_Zone_" + String.valueOf(getFenQu());
        } else {
            tmpPrjName = String.valueOf("CGCS2000_") + "GK_CM_" + String.valueOf((int) GetCenterMeridian()) + "E";
        }
        arrayOfObject[5] = tmpPrjName;
        arrayOfObject[0] = "CGCS2000";
        arrayOfObject[1] = Double.valueOf(GetA());
        arrayOfObject[2] = Double.valueOf(GetE());
        arrayOfObject[3] = Double.valueOf(GetEasting());
        arrayOfObject[4] = Float.valueOf(GetCenterMeridian());
        return String.format("PROJCS[\"%6$s\",GEOGCS[\"GCS_%1$s\",DATUM[\"D_China_2000\",SPHEROID[\"%7$s\",%2$s,%3$s]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Gauss_Kruger\"],PARAMETER[\"False_Easting\",%4$s],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",%5$s],PARAMETER[\"Scale_Factor\",1.0],PARAMETER[\"Latitude_Of_Origin\",0.0],UNIT[\"Meter\",1.0]]", arrayOfObject);
    }
}
