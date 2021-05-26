package  com.xzy.forestSystem.gogisapi.SpatialAnalyst;

public class DoublePoint {

    /* renamed from: X */
    public double f510X;

    /* renamed from: Y */
    public double f511Y;

    public DoublePoint() {
        this.f511Y = 0.0d;
        this.f510X = 0.0d;
    }

    public DoublePoint(double x, double y) {
        this.f510X = x;
        this.f511Y = y;
    }

    public DoublePoint(DoublePoint dp) {
        this.f510X = dp.f510X;
        this.f511Y = dp.f511Y;
    }

    public DoublePoint(IntPoint ip) {
        this.f510X = (double) ip.f512X;
        this.f511Y = (double) ip.f513Y;
    }
}
