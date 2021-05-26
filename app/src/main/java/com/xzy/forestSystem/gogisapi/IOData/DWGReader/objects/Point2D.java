package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

public class Point2D {

    /* renamed from: x */
    private double f489x;

    /* renamed from: y */
    private double f490y;

    public Point2D() {
    }

    public Point2D(double x, double y) {
        this.f489x = x;
        this.f490y = y;
    }

    public double getX() {
        return this.f489x;
    }

    public double getY() {
        return this.f490y;
    }

    public void setLocation(double x, double y) {
        this.f489x = x;
        this.f490y = y;
    }

    public double distance(Point2D ptn02) {
        return 0.0d;
    }

    public boolean IsEqual(Point2D ptn) {
        return ptn.f489x == this.f489x && ptn.f490y == this.f490y;
    }

    public double[] getCoord() {
        return new double[]{this.f489x, this.f490y};
    }
}
