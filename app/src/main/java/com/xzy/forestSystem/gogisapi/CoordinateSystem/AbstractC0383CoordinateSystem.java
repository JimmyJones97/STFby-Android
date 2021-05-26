package  com.xzy.forestSystem.gogisapi.CoordinateSystem;

import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;

/* renamed from:  com.xzy.forestSystem.gogisapi.CoordinateSystem.CoordinateSystem */
public abstract class AbstractC0383CoordinateSystem {

    /* renamed from: _A */
    double f462_A;

    /* renamed from: _B */
    double f463_B;
    ECoordinateSystemType _CoordinateSystemType;
    String _DefaultSpheroidName;
    boolean _IsProjectionCoord;
    String _Name;
    PMTranslate _PMTranslate;
    String _SpheroidName;

    /* renamed from: _f */
    double f464_f;

    public abstract Coordinate ConvertBLToXY(double d, double d2);

    public abstract Coordinate ConvertXYToBL(double d, double d2);

    public abstract String ToCoordSystemFileInfo();

    public AbstractC0383CoordinateSystem() {
        this._Name = "";
        this._IsProjectionCoord = false;
        this.f462_A = 0.0d;
        this.f463_B = 0.0d;
        this.f464_f = 1.0d;
        this._PMTranslate = null;
        this._CoordinateSystemType = ECoordinateSystemType.enUnknow;
        this._DefaultSpheroidName = "";
        this._SpheroidName = "";
        this._PMTranslate = new PMTranslate("无");
        this._PMTranslate.SetBindCoordinateSystem(this);
    }

    public Coordinate ConvertBLToXY(Coordinate coordinate) {
        return ConvertBLToXY(coordinate.getX(), coordinate.getY());
    }

    public Coordinate ConvertXYToBL(Coordinate coordinate) {
        return ConvertXYToBL(coordinate.getX(), coordinate.getY());
    }

    public Coordinate ConvertXYToUnKown(double x, double y) {
        if (this._IsProjectionCoord) {
            return new Coordinate(x, y);
        }
        return ConvertXYToBL(x, y);
    }

    public Coordinate ConvertBLToUnKown(double x, double y) {
        if (this._IsProjectionCoord) {
            return ConvertBLToXY(x, y);
        }
        return new Coordinate(x, y);
    }

    public Coordinate ConvertUnKownToXY(double x, double y) {
        if (this._IsProjectionCoord) {
            return new Coordinate(x, y);
        }
        return ConvertBLToXY(x, y);
    }

    public static AbstractC0383CoordinateSystem CreateCoordinateSystem(String name) {
        String name2 = name.toLowerCase();
        if (name2.contains("84")) {
            if (name2.contains("mercator")) {
                return new Coordinate_WebMercator();
            }
            return new Coordinate_WGS1984();
        } else if (name2.contains("北京")) {
            return new Coordinate_BeiJing1954();
        } else {
            if (name2.contains("西安")) {
                return new Coordinate_XiAn1980();
            }
            if (name2.contains("2000")) {
                return new Coordinate_CGCS2000();
            }
            if (name2.contains("mercator")) {
                return new Coordinate_WebMercator();
            }
            if (name2.contains("utm")) {
                return new Coordinate_UTM();
            }
            return new Coordinate_WGS1984();
        }
    }

    public String GetName() {
        return this._Name;
    }

    public void SetName(String paramString) {
        this._Name = paramString;
    }

    public boolean getIsProjectionCoord() {
        return this._IsProjectionCoord;
    }

    public double GetA() {
        return this.f462_A;
    }

    public double GetB() {
        return this.f463_B;
    }

    public double Getf() {
        return this.f464_f;
    }

    public void SetA(double paramDouble) {
        this.f462_A = paramDouble;
    }

    public void SetB(double paramDouble) {
        this.f463_B = paramDouble;
    }

    public void Setf(double f) {
        this.f464_f = f;
    }

    public double GetE() {
        return GetA() / (GetA() - GetB());
    }

    public double GetE2() {
        return GetB() / (GetA() - GetB());
    }

    public ECoordinateSystemType GetCoordinateSystemType() {
        return this._CoordinateSystemType;
    }

    public PMTranslate GetPMTranslate() {
        return this._PMTranslate;
    }

    public static float GetCenterJX(int daiHao, float fenDai) {
        float i = ((float) daiHao) * fenDai;
        if (fenDai == 6.0f) {
            return i - 3.0f;
        }
        if (((double) fenDai) == 1.5d) {
            return (float) (((double) i) - 1.5d);
        }
        return i;
    }

    public static int GetDH(double longitude, float longWidth) {
        int i = (int) Math.floor((10.0d * longitude) / ((double) (10.0f * longWidth)));
        if (longWidth == 6.0f) {
            return i + 1;
        }
        return i;
    }

    public Coordinate ConvertBLtoXYWithTranslate(Coordinate coord, AbstractC0383CoordinateSystem fromCoordinateSystem) {
        return ConvertBLtoXYWithTranslate(coord.getX(), coord.getY(), coord.getZ(), fromCoordinateSystem);
    }

    public Coordinate ConvertBLtoXYWithTranslate(double longitude, double latitude, double elevation, AbstractC0383CoordinateSystem fromCoordinateSystem) {
        if (fromCoordinateSystem == null) {
            fromCoordinateSystem = new Coordinate_WGS1984();
        }
        if (this._PMTranslate.GetPMCoorTransMethod() == ECoorTransMethod.enXYZMove) {
            Coordinate tempCoord = ConvertBLToXY(longitude, latitude);
            tempCoord.setZ(elevation);
            tempCoord.setGeoX(longitude);
            tempCoord.setGeoY(latitude);
            tempCoord.setX(tempCoord.getX() + this._PMTranslate.getBiasX());
            tempCoord.setY(tempCoord.getY() + this._PMTranslate.getBiasY());
            tempCoord.setZ(tempCoord.getZ() + this._PMTranslate.getBiasZ());
            return tempCoord;
        } else if (this._Name.contains("WGS-84坐标") || this._Name.contains("WebMercator")) {
            Coordinate tempCoord2 = Coordinate_WGS1984.BLToXY(longitude, latitude);
            tempCoord2.setZ(elevation);
            tempCoord2.setGeoX(longitude);
            tempCoord2.setGeoY(latitude);
            if (this._PMTranslate.GetPMCoorTransMethod() != ECoorTransMethod.enThreePara) {
                return tempCoord2;
            }
            tempCoord2.setX(tempCoord2.getX() + this._PMTranslate.GetTransToP31());
            tempCoord2.setY(tempCoord2.getY() + this._PMTranslate.GetTransToP32());
            tempCoord2.setZ(tempCoord2.getZ() + this._PMTranslate.GetTransToP33());
            return tempCoord2;
        } else if (this._PMTranslate.GetPMCoorTransMethod() == ECoorTransMethod.enThreePara) {
            double[] tmpXYZ = Projection_XYZ.ConvertBLHToXYZ(longitude, latitude, elevation, fromCoordinateSystem);
            double[] tmpBLH = Projection_XYZ.ConvertXYZToBLH(tmpXYZ[0] + this._PMTranslate.GetTransToP31(), tmpXYZ[1] + this._PMTranslate.GetTransToP32(), tmpXYZ[2] + this._PMTranslate.GetTransToP33(), this);
            Coordinate tempCoord03 = ConvertBLToXY(tmpBLH[0], tmpBLH[1]);
            tempCoord03.setZ(tmpBLH[2]);
            tempCoord03.setGeoX(longitude);
            tempCoord03.setGeoY(latitude);
            return tempCoord03;
        } else if (this._PMTranslate.GetPMCoorTransMethod() == ECoorTransMethod.enServenPara) {
            double[] tmpXYZ2 = Projection_XYZ.ConvertBLHToXYZ(longitude, latitude, elevation, fromCoordinateSystem);
            double tmpM = 1.0d + (this._PMTranslate.GetTransToP77() / 1000000.0d);
            double r01 = this._PMTranslate.GetTransToP74();
            double r02 = this._PMTranslate.GetTransToP75();
            double r03 = this._PMTranslate.GetTransToP76();
            double r012 = Math.toRadians(r01 / 3600.0d);
            double r022 = Math.toRadians(r02 / 3600.0d);
            double r032 = Math.toRadians(r03 / 3600.0d);
            double[] tmpBLH2 = Projection_XYZ.ConvertXYZToBLH((tmpXYZ2[0] * tmpM) + ((tmpXYZ2[1] * r032) - (tmpXYZ2[2] * r022)) + this._PMTranslate.GetTransToP71(), (tmpXYZ2[1] * tmpM) + ((tmpXYZ2[2] * r012) - (tmpXYZ2[0] * r032)) + this._PMTranslate.GetTransToP72(), (tmpXYZ2[2] * tmpM) + ((-r012) * tmpXYZ2[1]) + (tmpXYZ2[0] * r022) + this._PMTranslate.GetTransToP73(), this);
            Coordinate tempCoord032 = ConvertBLToXY(tmpBLH2[0], tmpBLH2[1]);
            tempCoord032.setZ(elevation);
            tempCoord032.setGeoX(longitude);
            tempCoord032.setGeoY(latitude);
            return tempCoord032;
        } else if (this._PMTranslate.GetPMCoorTransMethod() == ECoorTransMethod.enFourPara) {
            double d8 = this._PMTranslate.GetTransToP41();
            double d9 = this._PMTranslate.GetTransToP42();
            double d10 = Math.toRadians(this._PMTranslate.GetTransToP43());
            double d11 = 1.0d + (this._PMTranslate.GetTransToP44() / 1000000.0d);
            Coordinate tempCoord01 = ConvertBLToXY(longitude, latitude);
            tempCoord01.setX((((tempCoord01.getX() * d11) * Math.cos(d10)) + d8) - ((tempCoord01.getY() * d11) * Math.sin(d10)));
            tempCoord01.setY((tempCoord01.getX() * d11 * Math.sin(d10)) + d9 + (tempCoord01.getY() * d11 * Math.cos(d10)));
            tempCoord01.setZ(elevation);
            tempCoord01.setGeoX(longitude);
            tempCoord01.setGeoY(latitude);
            return tempCoord01;
        } else {
            Coordinate tmpResult = ConvertBLToXY(longitude, latitude);
            tmpResult.setZ(elevation);
            tmpResult.setGeoX(longitude);
            tmpResult.setGeoY(latitude);
            return tmpResult;
        }
    }

    public Coordinate ConvertXYtoBLWithTranslate(Coordinate coord, AbstractC0383CoordinateSystem toCoordinateSystem) {
        return ConvertXYtoBLWithTranslate(coord.getX(), coord.getY(), coord.getZ(), toCoordinateSystem);
    }

    public Coordinate ConvertXYtoBLWithTranslate(double X, double Y, double Z, AbstractC0383CoordinateSystem toCoordinateSystem) {
        if (toCoordinateSystem == null) {
            toCoordinateSystem = new Coordinate_WGS1984();
        }
        if (this._PMTranslate.GetPMCoorTransMethod() == ECoorTransMethod.enXYZMove) {
            Coordinate tempCoord = ConvertXYToBL(X - this._PMTranslate.getBiasX(), Y - this._PMTranslate.getBiasY());
            tempCoord.setZ(Z - this._PMTranslate.getBiasZ());
            return tempCoord;
        } else if (this._Name.contains("WGS-84坐标") || this._Name.contains("WebMercator")) {
            Coordinate tempCoord2 = Coordinate_WGS1984.XYToBL(X, Y);
            tempCoord2.setZ(Z);
            return tempCoord2;
        } else if (this._PMTranslate.GetPMCoorTransMethod() == ECoorTransMethod.enThreePara) {
            Coordinate tempCoord01 = ConvertXYToBL(X, Y);
            double[] tmpXYZ = Projection_XYZ.ConvertBLHToXYZ(tempCoord01.getGeoX(), tempCoord01.getGeoY(), Z, this);
            double[] tmpBLH = Projection_XYZ.ConvertXYZToBLH(tmpXYZ[0] - this._PMTranslate.GetTransToP31(), tmpXYZ[1] - this._PMTranslate.GetTransToP32(), tmpXYZ[2] - this._PMTranslate.GetTransToP33(), toCoordinateSystem);
            return new Coordinate(X, Y, tmpBLH[0], tmpBLH[1]);
        } else if (this._PMTranslate.GetPMCoorTransMethod() == ECoorTransMethod.enServenPara) {
            Coordinate tempCoord012 = ConvertXYToBL(X, Y);
            double[] tmpXYZ2 = Projection_XYZ.ConvertBLHToXYZ(tempCoord012.getGeoX(), tempCoord012.getGeoY(), Z, this);
            double[] tmpMatrixInv = this._PMTranslate.getServenInvMatrix();
            double tmpX = tmpXYZ2[0] - this._PMTranslate.GetTransToP71();
            double tmpY = tmpXYZ2[1] - this._PMTranslate.GetTransToP72();
            double tmpZ = tmpXYZ2[2] - this._PMTranslate.GetTransToP73();
            double[] tmpBLH2 = Projection_XYZ.ConvertXYZToBLH((tmpMatrixInv[0] * tmpX) + (tmpMatrixInv[1] * tmpY) + (tmpMatrixInv[2] * tmpZ), (tmpMatrixInv[3] * tmpX) + (tmpMatrixInv[4] * tmpY) + (tmpMatrixInv[5] * tmpZ), (tmpMatrixInv[6] * tmpX) + (tmpMatrixInv[7] * tmpY) + (tmpMatrixInv[8] * tmpZ), toCoordinateSystem);
            return new Coordinate(X, Y, tmpBLH2[0], tmpBLH2[1]);
        } else if (this._PMTranslate.GetPMCoorTransMethod() == ECoorTransMethod.enFourPara) {
            double d1 = this._PMTranslate.GetTransToP41();
            double d2 = this._PMTranslate.GetTransToP42();
            double d3 = Math.toRadians(this._PMTranslate.GetTransToP43());
            double d4 = 1.0d + (this._PMTranslate.GetTransToP44() / 1000000.0d);
            double d5 = d4 * Math.cos(d3);
            double d6 = (-d4) * Math.sin(d3);
            double d7 = X - d1;
            double d8 = d4 * Math.sin(d3);
            double d9 = d4 * Math.cos(d3);
            double d10 = (d7 - ((Y - d2) * (d6 / d9))) / (d5 - ((d6 / d9) * d8));
            Coordinate tempCoord3 = ConvertXYToBL(d10, (d7 - (d5 * d10)) / d6);
            tempCoord3.setZ(Z);
            return tempCoord3;
        } else {
            Coordinate tempResult = ConvertXYToBL(X, Y);
            tempResult.setZ(Z);
            return tempResult;
        }
    }

    public String getDefaultSpheroidName() {
        return this._DefaultSpheroidName;
    }

    public String getSpheroidName() {
        if (this._SpheroidName.equals("")) {
            return this._DefaultSpheroidName;
        }
        return this._SpheroidName;
    }

    public void setSpheroidName(String SpheroidName) {
        this._SpheroidName = SpheroidName;
    }
}
