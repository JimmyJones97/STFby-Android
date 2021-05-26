package  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation;

public interface Poly {
    void add(double d, double d2);

    void add(Point2D point2D);

    void add(Poly poly);

    void clear();

    Poly difference(Poly poly);

    double getArea();

    Rectangle2D getBounds();

    Poly getInnerPoly(int i);

    int getNumInnerPoly();

    int getNumPoints();

    double getX(int i);

    double getY(int i);

    Poly intersection(Poly poly);

    boolean isContributing(int i);

    boolean isEmpty();

    boolean isHole();

    void setContributing(int i, boolean z);

    void setIsHole(boolean z);

    Poly union(Poly poly);

    Poly xor(Poly poly);
}
