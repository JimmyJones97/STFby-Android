package  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation;

import java.util.ArrayList;
import java.util.List;

public class PolySimple implements Poly {
    private boolean m_Contributes = true;
    protected List<Point2D> m_List = new ArrayList();

    public boolean equals(Object obj) {
        if (!(obj instanceof PolySimple)) {
            return false;
        }
        PolySimple that = (PolySimple) obj;
        int this_num = this.m_List.size();
        int that_num = that.m_List.size();
        if (this_num != that_num) {
            return false;
        }
        if (this_num > 0) {
            double this_x = getX(0);
            double this_y = getY(0);
            int that_first_index = -1;
            int that_index = 0;
            while (that_first_index == -1 && that_index < that_num) {
                double that_x = that.getX(that_index);
                double that_y = that.getY(that_index);
                if (this_x == that_x && this_y == that_y) {
                    that_first_index = that_index;
                }
                that_index++;
            }
            if (that_first_index == -1) {
                return false;
            }
            int that_index2 = that_first_index;
            for (int this_index = 0; this_index < this_num; this_index++) {
                double this_x2 = getX(this_index);
                double this_y2 = getY(this_index);
                double that_x2 = that.getX(that_index2);
                double that_y2 = that.getY(that_index2);
                if (!(this_x2 == that_x2 && this_y2 == that_y2)) {
                    return false;
                }
                that_index2++;
                if (that_index2 >= that_num) {
                    that_index2 = 0;
                }
            }
        }
        return true;
    }

    public int hashCode() {
        return this.m_List.hashCode() + 629;
    }

    public String toString() {
        return "PolySimple: num_points=" + getNumPoints();
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public void clear() {
        this.m_List.clear();
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public void add(double x, double y) {
        add(new Point2D(x, y));
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public void add(Point2D p) {
        this.m_List.add(p);
    }

    public void removeAt(int index) {
        this.m_List.remove(index);
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public void add(Poly p) {
        throw new IllegalStateException("Cannot add poly to a simple poly.");
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public boolean isEmpty() {
        return this.m_List.isEmpty();
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public Rectangle2D getBounds() {
        double xmin = Double.MAX_VALUE;
        double ymin = Double.MAX_VALUE;
        double xmax = -1.7976931348623157E308d;
        double ymax = -1.7976931348623157E308d;
        for (int i = 0; i < this.m_List.size(); i++) {
            double x = getX(i);
            double y = getY(i);
            if (x < xmin) {
                xmin = x;
            }
            if (x > xmax) {
                xmax = x;
            }
            if (y < ymin) {
                ymin = y;
            }
            if (y > ymax) {
                ymax = y;
            }
        }
        return new Rectangle2D(xmin, ymin, xmax - xmin, ymax - ymin);
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public Poly getInnerPoly(int polyIndex) {
        if (polyIndex == 0) {
            return this;
        }
        throw new IllegalStateException("PolySimple only has one poly");
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public int getNumInnerPoly() {
        return 1;
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public int getNumPoints() {
        return this.m_List.size();
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public double getX(int index) {
        return this.m_List.get(index).getX();
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public double getY(int index) {
        return this.m_List.get(index).getY();
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public boolean isHole() {
        return false;
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public void setIsHole(boolean isHole) {
        throw new IllegalStateException("PolySimple cannot be a hole");
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public boolean isContributing(int polyIndex) {
        if (polyIndex == 0) {
            return this.m_Contributes;
        }
        throw new IllegalStateException("PolySimple only has one poly");
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public void setContributing(int polyIndex, boolean contributes) {
        if (polyIndex != 0) {
            throw new IllegalStateException("PolySimple only has one poly");
        }
        this.m_Contributes = contributes;
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public Poly intersection(Poly p) {
        return Clip.intersection(this, p, getClass());
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public Poly union(Poly p) {
        return Clip.union(this, p, getClass());
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public Poly xor(Poly p) {
        return Clip.xor(p, this, getClass());
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public Poly difference(Poly p) {
        return Clip.difference(this, p, getClass());
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public double getArea() {
        if (getNumPoints() < 3) {
            return 0.0d;
        }
        double ax = getX(0);
        double ay = getY(0);
        double area = 0.0d;
        for (int i = 1; i < getNumPoints() - 1; i++) {
            double bx = getX(i);
            double by = getY(i);
            area += ((getX(i + 1) - bx) * (ay - by)) - ((ax - bx) * (getY(i + 1) - by));
        }
        return 0.5d * Math.abs(area);
    }

    public List<Point2D> getList() {
        return this.m_List;
    }
}
