package  com.xzy.forestSystem.gogisapi.CoordinateSystem;

import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;

public class ProjectionCoordinateSystem extends AbstractC0383CoordinateSystem {
    float _CenterMeridian = 120.0f;
    float _DaiHao = 3.0f;
    double _Easting = 500000.0d;
    int _FenQu = 40;
    boolean _WithDaiHao = false;

    public ProjectionCoordinateSystem() {
        this._IsProjectionCoord = true;
    }

    @Override //  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem
    public Coordinate ConvertXYToBL(double x, double y) {
        return null;
    }

    @Override //  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem
    public Coordinate ConvertBLToXY(double longitude, double latitude) {
        return null;
    }

    public float GetCenterMeridian() {
        return this._CenterMeridian;
    }

    public double GetEasting() {
        return this._Easting;
    }

    public void SetCenterMeridian(float centerM) {
        this._CenterMeridian = centerM;
    }

    public void SetEasting(double paramDouble) {
        this._Easting = paramDouble;
    }

    public void SetDaiHao(float daiHao) {
        this._DaiHao = daiHao;
    }

    public void SetFenQu(int fenQu) {
        this._FenQu = fenQu;
    }

    public float getDaiHao() {
        return this._DaiHao;
    }

    public int getFenQu() {
        return this._FenQu;
    }

    @Override //  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem
    public String ToCoordSystemFileInfo() {
        return "";
    }

    public boolean IsWithDaiHao() {
        return this._WithDaiHao;
    }
}
