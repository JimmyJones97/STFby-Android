package  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation;

public class Rectangle2D {
    double height;
    double width;

    /* renamed from: x */
    double f533x;

    /* renamed from: y */
    double f534y;

    public Rectangle2D(double x, double y, double width2, double height2) {
        this.f533x = x;
        this.f534y = y;
        this.width = width2;
        this.height = height2;
    }

    public Rectangle2D() {
        this(0.0d, 0.0d, 0.0d, 0.0d);
    }

    public double getX() {
        return this.f533x;
    }

    public double getY() {
        return this.f534y;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public double getMinX() {
        return this.f533x;
    }

    public double getMaxX() {
        return this.f533x + this.width;
    }

    public double getMinY() {
        return this.f534y;
    }

    public double getMaxY() {
        return this.f534y + this.height;
    }
}
