package  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation;

import java.util.ArrayList;
import java.util.List;

public class PolyDefault implements Poly {
    private boolean m_IsHole;
    List<Poly> m_List;

    public PolyDefault() {
        this(false);
    }

    public PolyDefault(boolean isHole) {
        this.m_IsHole = false;
        this.m_List = new ArrayList();
        this.m_IsHole = isHole;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof PolyDefault)) {
            return false;
        }
        PolyDefault that = (PolyDefault) obj;
        if (this.m_IsHole != that.m_IsHole || !this.m_List.equals(that.m_List)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.m_List.hashCode() + 629;
    }

    public String toString() {
        return super.toString();
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
        if (this.m_List.size() == 0) {
            this.m_List.add(new PolySimple());
        }
        this.m_List.get(0).add(p);
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public void add(Poly p) {
        if (this.m_List.size() <= 0 || !this.m_IsHole) {
            this.m_List.add(p);
            return;
        }
        throw new IllegalStateException("Cannot add polys to something designated as a hole.");
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public boolean isEmpty() {
        return this.m_List.isEmpty();
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public Rectangle2D getBounds() {
        if (this.m_List.size() == 0) {
            return new Rectangle2D();
        }
        if (this.m_List.size() == 1) {
            return getInnerPoly(0).getBounds();
        }
        throw new UnsupportedOperationException("getBounds not supported on complex poly.");
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public Poly getInnerPoly(int polyIndex) {
        return this.m_List.get(polyIndex);
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public int getNumInnerPoly() {
        return this.m_List.size();
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public int getNumPoints() {
        return this.m_List.get(0).getNumPoints();
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public double getX(int index) {
        return this.m_List.get(0).getX(index);
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public double getY(int index) {
        return this.m_List.get(0).getY(index);
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public boolean isHole() {
        if (this.m_List.size() <= 1) {
            return this.m_IsHole;
        }
        throw new IllegalStateException("Cannot call on a poly made up of more than one poly.");
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public void setIsHole(boolean isHole) {
        if (this.m_List.size() > 1) {
            throw new IllegalStateException("Cannot call on a poly made up of more than one poly.");
        }
        this.m_IsHole = isHole;
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public boolean isContributing(int polyIndex) {
        return this.m_List.get(polyIndex).isContributing(0);
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public void setContributing(int polyIndex, boolean contributes) {
        if (this.m_List.size() != 1) {
            throw new IllegalStateException("Only applies to polys of size 1");
        }
        this.m_List.get(polyIndex).setContributing(0, contributes);
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public Poly intersection(Poly p) {
        return Clip.intersection(p, this, getClass());
    }

    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly
    public Poly union(Poly p) {
        return Clip.union(p, this, getClass());
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
        double area = 0.0d;
        for (int i = 0; i < getNumInnerPoly(); i++) {
            Poly p = getInnerPoly(i);
            area += p.getArea() * (p.isHole() ? -1.0d : 1.0d);
        }
        return area;
    }

    /* access modifiers changed from: package-private */
    public void print() {
    }

    public List getList() {
        return this.m_List;
    }
}
