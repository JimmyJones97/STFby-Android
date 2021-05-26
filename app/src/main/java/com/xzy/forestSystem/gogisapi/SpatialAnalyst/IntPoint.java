package  com.xzy.forestSystem.gogisapi.SpatialAnalyst;

/* access modifiers changed from: package-private */
public class IntPoint {

    /* renamed from: X */
    public int f512X;

    /* renamed from: Y */
    public int f513Y;

    public IntPoint() {
        this.f513Y = 0;
        this.f512X = 0;
    }

    public IntPoint(int X, int Y) {
        this.f512X = X;
        this.f513Y = Y;
    }

    public IntPoint(double x, double y) {
        this.f512X = (int) x;
        this.f513Y = (int) y;
    }

    public IntPoint(IntPoint pt) {
        this.f512X = pt.f512X;
        this.f513Y = pt.f513Y;
    }

    public static boolean IsEqual(IntPoint a, IntPoint b) {
        return a.f512X == b.f512X && a.f513Y == b.f513Y;
    }

    public boolean Equals(IntPoint ptn) {
        if (ptn != null && this.f512X == ptn.f512X && this.f513Y == ptn.f513Y) {
            return true;
        }
        return false;
    }
}
