package  com.xzy.forestSystem.gogisapi.CoordinateSystem;

import  com.xzy.forestSystem.gogisapi.Common.CommonMath;

public class PMTranslate {
    private double _BiasX = 0.0d;
    private double _BiasY = 0.0d;
    private double _BiasZ = 0.0d;
    private ECoorTransMethod _CoorTransMethod = ECoorTransMethod.enNull;
    private AbstractC0383CoordinateSystem _CoordinateSystem = null;
    private double _P31 = 0.0d;
    private double _P32 = 0.0d;
    private double _P33 = 0.0d;
    private double _P41 = 0.0d;
    private double _P42 = 0.0d;
    private double _P43 = 0.0d;
    private double _P44 = 0.0d;
    private double _P71 = 0.0d;
    private double _P72 = 0.0d;
    private double _P73 = 0.0d;
    private double _P74 = 0.0d;
    private double _P75 = 0.0d;
    private double _P76 = 0.0d;
    private double _P77 = 0.0d;
    private String _PMCoorTransMethodName = "无";
    private double[] m_ServerInvMatrix = null;

    public PMTranslate() {
    }

    public PMTranslate(String methodName) {
        SetPMCoorTransMethodName(methodName);
    }

    public void SetBindCoordinateSystem(AbstractC0383CoordinateSystem coordinateSystem) {
        this._CoordinateSystem = coordinateSystem;
    }

    public double GetTransToP31() {
        return this._P31;
    }

    public double GetTransToP32() {
        return this._P32;
    }

    public double GetTransToP33() {
        return this._P33;
    }

    public double GetTransToP34() {
        return new Coordinate_WGS1984().f462_A - this._CoordinateSystem.f462_A;
    }

    public double GetTransToP35() {
        return (1.0d / new Coordinate_WGS1984().f464_f) - (1.0d / this._CoordinateSystem.f464_f);
    }

    public double GetTransToP41() {
        return this._P41;
    }

    public double GetTransToP42() {
        return this._P42;
    }

    public double GetTransToP43() {
        return this._P43;
    }

    public double GetTransToP44() {
        return this._P44;
    }

    public double GetTransToP71() {
        return this._P71;
    }

    public double GetTransToP72() {
        return this._P72;
    }

    public double GetTransToP73() {
        return this._P73;
    }

    public double GetTransToP74() {
        return this._P74;
    }

    public double GetTransToP75() {
        return this._P75;
    }

    public double GetTransToP76() {
        return this._P76;
    }

    public double GetTransToP77() {
        return this._P77;
    }

    public void SetTransToP31(String paramString) {
        this._P31 = ToDouble(paramString);
    }

    public void SetTransToP32(String paramString) {
        this._P32 = ToDouble(paramString);
    }

    public void SetTransToP33(String paramString) {
        this._P33 = ToDouble(paramString);
    }

    public void SetTransToP41(String paramString) {
        this._P41 = ToDouble(paramString);
    }

    public void SetTransToP42(String paramString) {
        this._P42 = ToDouble(paramString);
    }

    public void SetTransToP43(String paramString) {
        this._P43 = ToDouble(paramString);
    }

    public void SetTransToP44(String paramString) {
        this._P44 = ToDouble(paramString);
    }

    public void SetTransToP71(String paramString) {
        this._P71 = ToDouble(paramString);
    }

    public void SetTransToP72(String paramString) {
        this._P72 = ToDouble(paramString);
    }

    public void SetTransToP73(String paramString) {
        this._P73 = ToDouble(paramString);
    }

    public void SetTransToP74(String paramString) {
        this._P74 = ToDouble(paramString);
    }

    public void SetTransToP75(String paramString) {
        this._P75 = ToDouble(paramString);
    }

    public void SetTransToP76(String paramString) {
        this._P76 = ToDouble(paramString);
    }

    public void SetTransToP77(String paramString) {
        this._P77 = ToDouble(paramString);
    }

    public void SetBiasX(String x) {
        this._BiasX = ToDouble(x);
    }

    public void SetBiasY(String y) {
        this._BiasY = ToDouble(y);
    }

    public void SetBiasZ(String z) {
        this._BiasZ = ToDouble(z);
    }

    public double getBiasX() {
        return this._BiasX;
    }

    public double getBiasY() {
        return this._BiasY;
    }

    public double getBiasZ() {
        return this._BiasZ;
    }

    private double ToDouble(String paramString) {
        if (paramString.equals("")) {
            return 0.0d;
        }
        return Double.parseDouble(paramString);
    }

    public String GetPMCoorTransMethodName() {
        return this._PMCoorTransMethodName;
    }

    public void SetPMCoorTransMethod(ECoorTransMethod paramlkCoorTransMethod) {
        this._CoorTransMethod = paramlkCoorTransMethod;
    }

    public void SetPMCoorTransMethodName(String value) {
        this._PMCoorTransMethodName = value;
        if (value.equals("") || value.equals("无")) {
            this._PMCoorTransMethodName = "无";
            SetPMCoorTransMethod(ECoorTransMethod.enNull);
        } else if (value.equals("三参转换")) {
            SetPMCoorTransMethod(ECoorTransMethod.enThreePara);
        } else if (value.equals("平移转换")) {
            SetPMCoorTransMethod(ECoorTransMethod.enXYZMove);
        } else if (value.equals("四参转换")) {
            SetPMCoorTransMethod(ECoorTransMethod.enFourPara);
        } else if (value.equals("七参转换")) {
            SetPMCoorTransMethod(ECoorTransMethod.enServenPara);
            this.m_ServerInvMatrix = null;
        } else {
            this._PMCoorTransMethodName = "无";
        }
    }

    public ECoorTransMethod GetPMCoorTransMethod() {
        return this._CoorTransMethod;
    }

    public double[] getServenInvMatrix() {
        if (this.m_ServerInvMatrix == null) {
            double[] tmpMatrix = {1.0d, Math.toRadians(this._P76 / 3600.0d), -Math.toRadians(this._P75 / 3600.0d), -Math.toRadians(this._P76 / 3600.0d), 1.0d, Math.toRadians(this._P74 / 3600.0d), Math.toRadians(this._P75 / 3600.0d), -Math.toRadians(this._P74 / 3600.0d), 1.0d};
            double tmpK = 1.0d + (this._P77 / 1000000.0d);
            for (int i = 0; i < 9; i++) {
                tmpMatrix[i] = tmpMatrix[i] * tmpK;
            }
            this.m_ServerInvMatrix = CommonMath.Cal3MatrixInv(tmpMatrix);
        }
        return this.m_ServerInvMatrix;
    }
}
