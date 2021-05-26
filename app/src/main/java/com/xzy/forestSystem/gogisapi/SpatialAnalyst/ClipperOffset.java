package  com.xzy.forestSystem.gogisapi.SpatialAnalyst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClipperOffset {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$JoinType = null;
    private static final double def_arc_tolerance = 0.25d;
    private static final double two_pi = 6.283185307179586d;
    public double ArcTolerance;
    public double MiterLimit;
    private double m_StepsPerRad;
    private double m_cos;
    private double m_delta;
    private List<IntPoint> m_destPoly;
    private List<List<IntPoint>> m_destPolys;
    private IntPoint m_lowest;
    private double m_miterLim;
    private List<DoublePoint> m_normals = new ArrayList();
    private PolyNode m_polyNodes = new PolyNode();
    private double m_sin;
    private double m_sinA;
    private List<IntPoint> m_srcPoly;

    static /* synthetic */ int[] $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$JoinType() {
        int[] iArr = $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$JoinType;
        if (iArr == null) {
            iArr = new int[JoinType.values().length];
            try {
                iArr[JoinType.jtMiter.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[JoinType.jtRound.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[JoinType.jtSquare.ordinal()] = 1;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$JoinType = iArr;
        }
        return iArr;
    }

    public ClipperOffset(double miterLimit, double arcTolerance) {
        this.MiterLimit = miterLimit;
        this.ArcTolerance = arcTolerance;
        this.m_lowest.f512X = -1;
    }

    public void Clear() {
        this.m_polyNodes.m_Childs.clear();
        this.m_lowest.f512X = -1;
    }

    static int Round(double value) {
        return value < 0.0d ? (int) (value - 0.5d) : (int) (value + 0.5d);
    }

    public void AddPath(List<IntPoint> path, JoinType joinType, EndType endType) {
        int highI = path.size() - 1;
        if (highI >= 0) {
            PolyNode newNode = new PolyNode();
            newNode.m_jointype = joinType;
            newNode.m_endtype = endType;
            if (endType == EndType.etClosedLine || endType == EndType.etClosedPolygon) {
                while (highI > 0 && path.get(0) == path.get(highI)) {
                    highI--;
                }
            }
            newNode.m_polygon.add(path.get(0));
            int j = 0;
            int k = 0;
            for (int i = 1; i <= highI; i++) {
                if (newNode.m_polygon.get(j).Equals(path.get(i))) {
                    j++;
                    newNode.m_polygon.add(path.get(i));
                    IntPoint tempPtn01 = path.get(i);
                    IntPoint tempPtn02 = newNode.m_polygon.get(k);
                    if (tempPtn01.f513Y > tempPtn02.f513Y || (tempPtn01.f513Y == tempPtn02.f513Y && tempPtn01.f512X < tempPtn02.f512X)) {
                        k = j;
                    }
                }
            }
            if (endType != EndType.etClosedPolygon || j >= 2) {
                this.m_polyNodes.AddChild(newNode);
                if (endType != EndType.etClosedPolygon) {
                    return;
                }
                if (this.m_lowest.f512X < 0) {
                    this.m_lowest = new IntPoint(this.m_polyNodes.ChildCount() - 1, k);
                    return;
                }
                IntPoint ip = this.m_polyNodes.m_Childs.get(this.m_lowest.f512X).m_polygon.get(this.m_lowest.f513Y);
                IntPoint tempPtn022 = newNode.m_polygon.get(k);
                if (tempPtn022.f513Y > ip.f513Y || (tempPtn022.f513Y == ip.f513Y && tempPtn022.f512X < ip.f512X)) {
                    this.m_lowest = new IntPoint(this.m_polyNodes.ChildCount() - 1, k);
                }
            }
        }
    }

    public void AddPaths(List<List<IntPoint>> paths, JoinType joinType, EndType endType) {
        for (List<IntPoint> p : paths) {
            AddPath(p, joinType, endType);
        }
    }

    private void FixOrientations() {
        if (this.m_lowest.f512X < 0 || Clipper.Orientation(this.m_polyNodes.m_Childs.get(this.m_lowest.f512X).m_polygon)) {
            for (int i = 0; i < this.m_polyNodes.ChildCount(); i++) {
                PolyNode node = this.m_polyNodes.m_Childs.get(i);
                if (node.m_endtype == EndType.etClosedLine && !Clipper.Orientation(node.m_polygon)) {
                    Collections.reverse(node.m_polygon);
                }
            }
            return;
        }
        for (int i2 = 0; i2 < this.m_polyNodes.ChildCount(); i2++) {
            PolyNode node2 = this.m_polyNodes.m_Childs.get(i2);
            if (node2.m_endtype == EndType.etClosedPolygon || (node2.m_endtype == EndType.etClosedLine && Clipper.Orientation(node2.m_polygon))) {
                Collections.reverse(node2.m_polygon);
            }
        }
    }

    static DoublePoint GetUnitNormal(IntPoint pt1, IntPoint pt2) {
        double dx = (double) (pt2.f512X - pt1.f512X);
        double dy = (double) (pt2.f513Y - pt1.f513Y);
        if (dx == 0.0d && dy == 0.0d) {
            return new DoublePoint();
        }
        double f = 1.0d / Math.sqrt((dx * dx) + (dy * dy));
        return new DoublePoint(dy * f, -(dx * f));
    }

    private void DoOffset(double delta) {
        double y;
        this.m_destPolys = new ArrayList();
        this.m_delta = delta;
        if (ClipperBase.near_zero(delta)) {
            for (int i = 0; i < this.m_polyNodes.ChildCount(); i++) {
                PolyNode node = this.m_polyNodes.m_Childs.get(i);
                if (node.m_endtype == EndType.etClosedPolygon) {
                    this.m_destPolys.add(node.m_polygon);
                }
            }
            return;
        }
        if (this.MiterLimit > 2.0d) {
            this.m_miterLim = 2.0d / (this.MiterLimit * this.MiterLimit);
        } else {
            this.m_miterLim = 0.5d;
        }
        if (this.ArcTolerance <= 0.0d) {
            y = def_arc_tolerance;
        } else if (this.ArcTolerance > Math.abs(delta) * def_arc_tolerance) {
            y = Math.abs(delta) * def_arc_tolerance;
        } else {
            y = this.ArcTolerance;
        }
        double steps = 3.141592653589793d / Math.acos(1.0d - (y / Math.abs(delta)));
        this.m_sin = Math.sin(two_pi / steps);
        this.m_cos = Math.cos(two_pi / steps);
        this.m_StepsPerRad = steps / two_pi;
        if (delta < 0.0d) {
            this.m_sin = -this.m_sin;
        }
        for (int i2 = 0; i2 < this.m_polyNodes.ChildCount(); i2++) {
            PolyNode node2 = this.m_polyNodes.m_Childs.get(i2);
            this.m_srcPoly = node2.m_polygon;
            int len = this.m_srcPoly.size();
            if (len != 0 && (delta > 0.0d || (len >= 3 && node2.m_endtype == EndType.etClosedPolygon))) {
                this.m_destPoly = new ArrayList();
                if (len == 1) {
                    if (node2.m_jointype == JoinType.jtRound) {
                        double X = 1.0d;
                        double Y = 0.0d;
                        for (int j = 1; ((double) j) <= steps; j++) {
                            this.m_destPoly.add(new IntPoint(Round(((double) this.m_srcPoly.get(0).f512X) + (X * delta)), Round(((double) this.m_srcPoly.get(0).f513Y) + (Y * delta))));
                            X = (this.m_cos * X) - (this.m_sin * Y);
                            Y = (this.m_sin * X) + (this.m_cos * Y);
                        }
                    } else {
                        double X2 = -1.0d;
                        double Y2 = -1.0d;
                        for (int j2 = 0; j2 < 4; j2++) {
                            this.m_destPoly.add(new IntPoint(Round(((double) this.m_srcPoly.get(0).f512X) + (X2 * delta)), Round(((double) this.m_srcPoly.get(0).f513Y) + (Y2 * delta))));
                            if (X2 < 0.0d) {
                                X2 = 1.0d;
                            } else if (Y2 < 0.0d) {
                                Y2 = 1.0d;
                            } else {
                                X2 = -1.0d;
                            }
                        }
                    }
                    this.m_destPolys.add(this.m_destPoly);
                } else {
                    this.m_normals.clear();
                    for (int j3 = 0; j3 < len - 1; j3++) {
                        this.m_normals.add(GetUnitNormal(this.m_srcPoly.get(j3), this.m_srcPoly.get(j3 + 1)));
                    }
                    if (node2.m_endtype == EndType.etClosedLine || node2.m_endtype == EndType.etClosedPolygon) {
                        this.m_normals.add(GetUnitNormal(this.m_srcPoly.get(len - 1), this.m_srcPoly.get(0)));
                    } else {
                        this.m_normals.add(new DoublePoint(this.m_normals.get(len - 2)));
                    }
                    if (node2.m_endtype == EndType.etClosedPolygon) {
                        int k = len - 1;
                        for (int j4 = 0; j4 < len; j4++) {
                            OffsetPoint(j4, k, node2.m_jointype);
                        }
                        this.m_destPolys.add(this.m_destPoly);
                    } else if (node2.m_endtype == EndType.etClosedLine) {
                        int k2 = len - 1;
                        for (int j5 = 0; j5 < len; j5++) {
                            OffsetPoint(j5, k2, node2.m_jointype);
                        }
                        this.m_destPolys.add(this.m_destPoly);
                        this.m_destPoly = new ArrayList();
                        DoublePoint n = this.m_normals.get(len - 1);
                        for (int j6 = len - 1; j6 > 0; j6--) {
                            this.m_normals.add(j6, new DoublePoint(-this.m_normals.get(j6 - 1).f510X, -this.m_normals.get(j6 - 1).f511Y));
                        }
                        this.m_normals.add(0, new DoublePoint(-n.f510X, -n.f511Y));
                        for (int j7 = len - 1; j7 >= 0; j7--) {
                            OffsetPoint(j7, 0, node2.m_jointype);
                        }
                        this.m_destPolys.add(this.m_destPoly);
                    } else {
                        for (int j8 = 1; j8 < len - 1; j8++) {
                            OffsetPoint(j8, 0, node2.m_jointype);
                        }
                        if (node2.m_endtype == EndType.etOpenButt) {
                            int j9 = len - 1;
                            this.m_destPoly.add(new IntPoint(Round(((double) this.m_srcPoly.get(j9).f512X) + (this.m_normals.get(j9).f510X * delta)), Round(((double) this.m_srcPoly.get(j9).f513Y) + (this.m_normals.get(j9).f511Y * delta))));
                            this.m_destPoly.add(new IntPoint(Round(((double) this.m_srcPoly.get(j9).f512X) - (this.m_normals.get(j9).f510X * delta)), Round(((double) this.m_srcPoly.get(j9).f513Y) - (this.m_normals.get(j9).f511Y * delta))));
                        } else {
                            int j10 = len - 1;
                            int k3 = len - 2;
                            this.m_sinA = 0.0d;
                            this.m_normals.add(j10, new DoublePoint(-this.m_normals.get(j10).f510X, -this.m_normals.get(j10).f511Y));
                            if (node2.m_endtype == EndType.etOpenSquare) {
                                DoSquare(j10, k3);
                            } else {
                                DoRound(j10, k3);
                            }
                        }
                        for (int j11 = len - 1; j11 > 0; j11--) {
                            this.m_normals.add(j11, new DoublePoint(-this.m_normals.get(j11 - 1).f510X, -this.m_normals.get(j11 - 1).f511Y));
                        }
                        this.m_normals.add(0, new DoublePoint(-this.m_normals.get(1).f510X, -this.m_normals.get(1).f511Y));
                        int k4 = len - 1;
                        for (int j12 = k4 - 1; j12 > 0; j12--) {
                            OffsetPoint(j12, k4, node2.m_jointype);
                        }
                        if (node2.m_endtype == EndType.etOpenButt) {
                            this.m_destPoly.add(new IntPoint(Round(((double) this.m_srcPoly.get(0).f512X) - (this.m_normals.get(0).f510X * delta)), Round(((double) this.m_srcPoly.get(0).f513Y) - (this.m_normals.get(0).f511Y * delta))));
                            this.m_destPoly.add(new IntPoint(Round(((double) this.m_srcPoly.get(0).f512X) + (this.m_normals.get(0).f510X * delta)), Round(((double) this.m_srcPoly.get(0).f513Y) + (this.m_normals.get(0).f511Y * delta))));
                        } else {
                            this.m_sinA = 0.0d;
                            if (node2.m_endtype == EndType.etOpenSquare) {
                                DoSquare(0, 1);
                            } else {
                                DoRound(0, 1);
                            }
                        }
                        this.m_destPolys.add(this.m_destPoly);
                    }
                }
            }
        }
    }

    public void Execute(List<List<IntPoint>> solution, double delta) {
        solution.clear();
        FixOrientations();
        DoOffset(delta);
        Clipper clpr = new Clipper();
        clpr.AddPaths(this.m_destPolys, PolyType.ptSubject, true);
        if (delta > 0.0d) {
            clpr.Execute(ClipType.ctUnion, solution, PolyFillType.pftPositive, PolyFillType.pftPositive);
            return;
        }
        IntRect r = Clipper.GetBounds(this.m_destPolys);
        List<IntPoint> outer = new ArrayList<>();
        outer.add(new IntPoint(r.left - 10, r.bottom + 10));
        outer.add(new IntPoint(r.right + 10, r.bottom + 10));
        outer.add(new IntPoint(r.right + 10, r.top - 10));
        outer.add(new IntPoint(r.left - 10, r.top - 10));
        clpr.AddPath(outer, PolyType.ptSubject, true);
        clpr.ReverseSolution = true;
        clpr.Execute(ClipType.ctUnion, solution, PolyFillType.pftNegative, PolyFillType.pftNegative);
        if (solution.size() > 0) {
            solution.remove(0);
        }
    }

    public void Execute(PolyTree solution, double delta) {
        solution.Clear();
        FixOrientations();
        DoOffset(delta);
        Clipper clpr = new Clipper();
        clpr.AddPaths(this.m_destPolys, PolyType.ptSubject, true);
        if (delta > 0.0d) {
            clpr.Execute(ClipType.ctUnion, solution, PolyFillType.pftPositive, PolyFillType.pftPositive);
            return;
        }
        IntRect r = Clipper.GetBounds(this.m_destPolys);
        List<IntPoint> outer = new ArrayList<>();
        outer.add(new IntPoint(r.left - 10, r.bottom + 10));
        outer.add(new IntPoint(r.right + 10, r.bottom + 10));
        outer.add(new IntPoint(r.right + 10, r.top - 10));
        outer.add(new IntPoint(r.left - 10, r.top - 10));
        clpr.AddPath(outer, PolyType.ptSubject, true);
        clpr.ReverseSolution = true;
        clpr.Execute(ClipType.ctUnion, solution, PolyFillType.pftNegative, PolyFillType.pftNegative);
        if (solution.ChildCount() != 1 || ((PolyNode) solution.m_Childs.get(0)).ChildCount() <= 0) {
            solution.Clear();
            return;
        }
        PolyNode outerNode = (PolyNode) solution.m_Childs.get(0);
        solution.m_Childs.add(0, outerNode.m_Childs.get(0));
        ((PolyNode) solution.m_Childs.get(0)).m_Parent = solution;
        for (int i = 1; i < outerNode.ChildCount(); i++) {
            solution.AddChild(outerNode.m_Childs.get(i));
        }
    }

    /* access modifiers changed from: package-private */
    public void OffsetPoint(int j, int k, JoinType jointype) {
        this.m_sinA = (this.m_normals.get(k).f510X * this.m_normals.get(j).f511Y) - (this.m_normals.get(k).f511Y * this.m_normals.get(j).f510X);
        if (Math.abs(this.m_sinA * this.m_delta) < 1.0d) {
            if ((this.m_normals.get(k).f510X * this.m_normals.get(j).f510X) + (this.m_normals.get(k).f511Y * this.m_normals.get(j).f511Y) > 0.0d) {
                this.m_destPoly.add(new IntPoint(Round(((double) this.m_srcPoly.get(j).f512X) + (this.m_normals.get(k).f510X * this.m_delta)), Round(((double) this.m_srcPoly.get(j).f513Y) + (this.m_normals.get(k).f511Y * this.m_delta))));
                return;
            }
        } else if (this.m_sinA > 1.0d) {
            this.m_sinA = 1.0d;
        } else if (this.m_sinA < -1.0d) {
            this.m_sinA = -1.0d;
        }
        if (this.m_sinA * this.m_delta >= 0.0d) {
            switch ($SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$JoinType()[jointype.ordinal()]) {
                case 1:
                    DoSquare(j, k);
                    break;
                case 2:
                    DoRound(j, k);
                    break;
                case 3:
                    double r = 1.0d + (this.m_normals.get(k).f511Y * this.m_normals.get(j).f511Y) + (this.m_normals.get(j).f510X * this.m_normals.get(k).f510X);
                    if (r < this.m_miterLim) {
                        DoSquare(j, k);
                        break;
                    } else {
                        DoMiter(j, k, r);
                        break;
                    }
            }
        } else {
            this.m_destPoly.add(new IntPoint(Round(((double) this.m_srcPoly.get(j).f512X) + (this.m_normals.get(k).f510X * this.m_delta)), Round(((double) this.m_srcPoly.get(j).f513Y) + (this.m_normals.get(k).f511Y * this.m_delta))));
            this.m_destPoly.add(this.m_srcPoly.get(j));
            this.m_destPoly.add(new IntPoint(Round(((double) this.m_srcPoly.get(j).f512X) + (this.m_normals.get(j).f510X * this.m_delta)), Round(((double) this.m_srcPoly.get(j).f513Y) + (this.m_normals.get(j).f511Y * this.m_delta))));
        }
    }

    /* access modifiers changed from: package-private */
    public void DoSquare(int j, int k) {
        double dx = Math.tan(Math.atan2(this.m_sinA, (this.m_normals.get(j).f511Y * this.m_normals.get(k).f511Y) + (this.m_normals.get(k).f510X * this.m_normals.get(j).f510X)) / 4.0d);
        this.m_destPoly.add(new IntPoint(Round(((double) this.m_srcPoly.get(j).f512X) + (this.m_delta * (this.m_normals.get(k).f510X - (this.m_normals.get(k).f511Y * dx)))), Round(((double) this.m_srcPoly.get(j).f513Y) + (this.m_delta * (this.m_normals.get(k).f511Y + (this.m_normals.get(k).f510X * dx))))));
        this.m_destPoly.add(new IntPoint(Round(((double) this.m_srcPoly.get(j).f512X) + (this.m_delta * (this.m_normals.get(j).f510X + (this.m_normals.get(j).f511Y * dx)))), Round(((double) this.m_srcPoly.get(j).f513Y) + (this.m_delta * (this.m_normals.get(j).f511Y - (this.m_normals.get(j).f510X * dx))))));
    }

    /* access modifiers changed from: package-private */
    public void DoMiter(int j, int k, double r) {
        double q = this.m_delta / r;
        this.m_destPoly.add(new IntPoint(Round(((double) this.m_srcPoly.get(j).f512X) + ((this.m_normals.get(k).f510X + this.m_normals.get(j).f510X) * q)), Round(((double) this.m_srcPoly.get(j).f513Y) + ((this.m_normals.get(k).f511Y + this.m_normals.get(j).f511Y) * q))));
    }

    /* access modifiers changed from: package-private */
    public void DoRound(int j, int k) {
        DoublePoint tempKPtn = this.m_normals.get(k);
        DoublePoint tempJPtn = this.m_normals.get(j);
        int steps = Math.max(Round(this.m_StepsPerRad * Math.abs(Math.atan2(this.m_sinA, (tempKPtn.f510X * tempJPtn.f510X) + (tempKPtn.f511Y * tempJPtn.f511Y)))), 1);
        double X = tempKPtn.f510X;
        double Y = tempKPtn.f511Y;
        for (int i = 0; i < steps; i++) {
            this.m_destPoly.add(new IntPoint(Round(((double) this.m_srcPoly.get(j).f512X) + (this.m_delta * X)), Round(((double) this.m_srcPoly.get(j).f513Y) + (this.m_delta * Y))));
            X = (this.m_cos * X) - (this.m_sin * Y);
            Y = (this.m_sin * X) + (this.m_cos * Y);
        }
        this.m_destPoly.add(new IntPoint(Round(((double) this.m_srcPoly.get(j).f512X) + (tempJPtn.f510X * this.m_delta)), Round(((double) this.m_srcPoly.get(j).f513Y) + (tempJPtn.f511Y * this.m_delta))));
    }
}
