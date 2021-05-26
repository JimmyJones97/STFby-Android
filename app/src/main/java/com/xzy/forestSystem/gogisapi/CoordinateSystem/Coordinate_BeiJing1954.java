package  com.xzy.forestSystem.gogisapi.CoordinateSystem;

import java.text.DecimalFormat;

public class Coordinate_BeiJing1954 extends Projection_GuassKruger {
    public Coordinate_BeiJing1954() {
        this._Name = "北京54坐标";
        this._DefaultSpheroidName = "北京54";
        this._CoordinateSystemType = ECoordinateSystemType.enBeiJing54;
        this.f462_A = 6378245.0d;
        this.f463_B = 6356863.0188d;
        this.f464_f = 298.3d;
    }

    @Override //  com.xzy.forestSystem.gogisapi.CoordinateSystem.ProjectionCoordinateSystem,  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem
    public String ToCoordSystemFileInfo() {
        String tmpPrjName;
        Object[] arrayOfObject = new Object[7];
        arrayOfObject[6] = "Krasovsky_1940";
        DecimalFormat tmpDF = new DecimalFormat("0.0");
        boolean tmpEastHashCode = false;
        if (String.valueOf(GetEasting()).startsWith(String.valueOf(getFenQu()))) {
            tmpEastHashCode = true;
        }
        if (this._DaiHao == 3.0f) {
            if (tmpEastHashCode) {
                tmpPrjName = String.valueOf("Beijing_1954_") + String.valueOf((int) getDaiHao()) + "_Degree_GK_Zone_" + String.valueOf(getFenQu());
            } else {
                tmpPrjName = String.valueOf("Beijing_1954_") + String.valueOf((int) getDaiHao()) + "_Degree_GK_CM_" + String.valueOf((int) GetCenterMeridian()) + "E";
            }
        } else if (tmpEastHashCode) {
            tmpPrjName = String.valueOf("Beijing_1954_") + "GK_Zone_" + String.valueOf(getFenQu());
        } else {
            tmpPrjName = String.valueOf("Beijing_1954_") + "GK_Zone_" + String.valueOf(getFenQu()) + "N";
        }
        arrayOfObject[5] = tmpPrjName;
        arrayOfObject[0] = "Beijing_1954";
        arrayOfObject[1] = Double.valueOf(GetA());
        arrayOfObject[2] = tmpDF.format(GetE());
        arrayOfObject[3] = Double.valueOf(GetEasting());
        arrayOfObject[4] = Float.valueOf(GetCenterMeridian());
        return String.format("PROJCS[\"%6$s\",GEOGCS[\"GCS_%1$s\",DATUM[\"D_%1$s\",SPHEROID[\"%7$s\",%2$s,%3$s]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],PROJECTION[\"Gauss_Kruger\"],PARAMETER[\"False_Easting\",%4$s],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",%5$s],PARAMETER[\"Scale_Factor\",1.0],PARAMETER[\"Latitude_Of_Origin\",0.0],UNIT[\"Meter\",1.0]]", arrayOfObject);
    }
}
