package  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Clip {
    private static final int ABOVE = 0;
    private static final int BELOW = 1;
    private static final int CLIP = 0;
    private static final boolean DEBUG = false;
    public static double GPC_EPSILON = 2.220446049250313E-16d;
    public static final double GPC_EPSILON_Default = 2.220446049250313E-16d;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int SUBJ = 1;

    private Clip() {
    }

    public static Poly intersection(Poly p1, Poly p2, Class<? extends Poly> polyClass) {
        return clip(OperationType.GPC_INT, p1, p2, polyClass);
    }

    public static Poly union(Poly p1, Poly p2, Class<? extends Poly> polyClass) {
        return clip(OperationType.GPC_UNION, p1, p2, polyClass);
    }

    public static Poly xor(Poly p1, Poly p2, Class<? extends Poly> polyClass) {
        return clip(OperationType.GPC_XOR, p1, p2, polyClass);
    }

    public static Poly difference(Poly p1, Poly p2, Class<? extends Poly> polyClass) {
        return clip(OperationType.GPC_DIFF, p1, p2, polyClass);
    }

    public static Poly intersection(Poly p1, Poly p2) {
        return clip(OperationType.GPC_INT, p1, p2, PolyDefault.class);
    }

    public static Poly union(Poly p1, Poly p2) {
        return clip(OperationType.GPC_UNION, p1, p2, PolyDefault.class);
    }

    public static Poly xor(Poly p1, Poly p2) {
        return clip(OperationType.GPC_XOR, p1, p2, PolyDefault.class);
    }

    public static Poly difference(Poly p1, Poly p2) {
        return clip(OperationType.GPC_DIFF, p1, p2, PolyDefault.class);
    }

    /* access modifiers changed from: private */
    public static Poly createNewPoly(Class<? extends Poly> polyClass) {
        try {
            return (Poly) polyClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:140:0x042a  */
    /* JADX WARNING: Removed duplicated region for block: B:143:0x043e  */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x04f4  */
    /* JADX WARNING: Removed duplicated region for block: B:237:0x0657  */
    /* JADX WARNING: Removed duplicated region for block: B:240:0x066b  */
    /* JADX WARNING: Removed duplicated region for block: B:252:0x069e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly clip( com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Clip.OperationType r62,  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly r63,  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly r64, java.lang.Class<? extends  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly> r65) {
        /*
        // Method dump skipped, instructions count: 4206
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Clip.clip( com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Clip$OperationType,  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly,  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly, java.lang.Class): com.xzy.forestSystem.gogisapi.SpatialAnalyst.Operation.Poly");
    }

    /* renamed from: EQ */
    private static boolean m3EQ(double a, double b) {
        if (Math.abs(a - b) <= GPC_EPSILON) {
            return true;
        }
        return DEBUG;
    }

    /* access modifiers changed from: private */
    public static int PREV_INDEX(int i, int n) {
        return ((i - 1) + n) % n;
    }

    /* access modifiers changed from: private */
    public static int NEXT_INDEX(int i, int n) {
        return (i + 1) % n;
    }

    private static boolean OPTIMAL(Poly p, int i) {
        if (p.getY(PREV_INDEX(i, p.getNumPoints())) == p.getY(i) && p.getY(NEXT_INDEX(i, p.getNumPoints())) == p.getY(i)) {
            return DEBUG;
        }
        return true;
    }

    private static Rectangle2D[] create_contour_bboxes(Poly p) {
        Rectangle2D[] box = new Rectangle2D[p.getNumInnerPoly()];
        for (int c = 0; c < p.getNumInnerPoly(); c++) {
            box[c] = p.getInnerPoly(c).getBounds();
        }
        return box;
    }

    private static void minimax_test(Poly subj, Poly clip, OperationType op) {
        Rectangle2D[] s_bbox = create_contour_bboxes(subj);
        Rectangle2D[] c_bbox = create_contour_bboxes(clip);
        int subj_num_poly = subj.getNumInnerPoly();
        int clip_num_poly = clip.getNumInnerPoly();
        boolean[][] o_table = (boolean[][]) Array.newInstance(Boolean.TYPE, subj_num_poly, clip_num_poly);
        for (int s = 0; s < subj_num_poly; s++) {
            for (int c = 0; c < clip_num_poly; c++) {
                o_table[s][c] = (s_bbox[s].getMaxX() < c_bbox[c].getMinX() || s_bbox[s].getMinX() > c_bbox[c].getMaxX() || s_bbox[s].getMaxY() < c_bbox[c].getMinY() || s_bbox[s].getMinY() > c_bbox[c].getMaxY()) ? DEBUG : true;
            }
        }
        for (int c2 = 0; c2 < clip_num_poly; c2++) {
            boolean overlap = DEBUG;
            int s2 = 0;
            while (!overlap && s2 < subj_num_poly) {
                overlap = o_table[s2][c2];
                s2++;
            }
            if (!overlap) {
                clip.setContributing(c2, DEBUG);
            }
        }
        if (op == OperationType.GPC_INT) {
            for (int s3 = 0; s3 < subj_num_poly; s3++) {
                boolean overlap2 = DEBUG;
                int c3 = 0;
                while (!overlap2 && c3 < clip_num_poly) {
                    overlap2 = o_table[s3][c3];
                    c3++;
                }
                if (!overlap2) {
                    subj.setContributing(s3, DEBUG);
                }
            }
        }
    }

    private static LmtNode bound_list(LmtTable lmt_table, double y) {
        if (lmt_table.top_node == null) {
            lmt_table.top_node = new LmtNode(y);
            return lmt_table.top_node;
        }
        LmtNode prev = null;
        LmtNode node = lmt_table.top_node;
        boolean done = DEBUG;
        while (!done) {
            if (y < node.f523y) {
                node = new LmtNode(y);
                node.next = node;
                if (prev == null) {
                    lmt_table.top_node = node;
                } else {
                    prev.next = node;
                }
                done = true;
            } else if (y <= node.f523y) {
                done = true;
            } else if (node.next == null) {
                node.next = new LmtNode(y);
                node = node.next;
                done = true;
            } else {
                prev = node;
                node = node.next;
            }
        }
        return node;
    }

    private static void insert_bound(LmtNode lmt_node, EdgeNode e) {
        if (lmt_node.first_bound == null) {
            lmt_node.first_bound = e;
            return;
        }
        boolean done = DEBUG;
        EdgeNode prev_bound = null;
        EdgeNode current_bound = lmt_node.first_bound;
        while (!done) {
            if (e.bot.f531x < current_bound.bot.f531x) {
                if (prev_bound == null) {
                    lmt_node.first_bound = e;
                } else {
                    prev_bound.next_bound = e;
                }
                e.next_bound = current_bound;
                done = true;
            } else if (e.bot.f531x == current_bound.bot.f531x) {
                if (e.f516dx < current_bound.f516dx) {
                    if (prev_bound == null) {
                        lmt_node.first_bound = e;
                    } else {
                        prev_bound.next_bound = e;
                    }
                    e.next_bound = current_bound;
                    done = true;
                } else if (current_bound.next_bound == null) {
                    current_bound.next_bound = e;
                    done = true;
                } else {
                    prev_bound = current_bound;
                    current_bound = current_bound.next_bound;
                }
            } else if (current_bound.next_bound == null) {
                current_bound.next_bound = e;
                done = true;
            } else {
                prev_bound = current_bound;
                current_bound = current_bound.next_bound;
            }
        }
    }

    private static void add_edge_to_aet(AetTree aet, EdgeNode edge) {
        if (aet.top_node == null) {
            aet.top_node = edge;
            edge.prev = null;
            edge.next = null;
            return;
        }
        EdgeNode current_edge = aet.top_node;
        EdgeNode prev = null;
        boolean done = DEBUG;
        while (!done) {
            if (edge.f517xb < current_edge.f517xb) {
                edge.prev = prev;
                edge.next = current_edge;
                current_edge.prev = edge;
                if (prev == null) {
                    aet.top_node = edge;
                } else {
                    prev.next = edge;
                }
                done = true;
            } else if (edge.f517xb != current_edge.f517xb) {
                prev = current_edge;
                if (current_edge.next == null) {
                    current_edge.next = edge;
                    edge.prev = current_edge;
                    edge.next = null;
                    done = true;
                } else {
                    current_edge = current_edge.next;
                }
            } else if (edge.f516dx < current_edge.f516dx) {
                edge.prev = prev;
                edge.next = current_edge;
                current_edge.prev = edge;
                if (prev == null) {
                    aet.top_node = edge;
                } else {
                    prev.next = edge;
                }
                done = true;
            } else {
                prev = current_edge;
                if (current_edge.next == null) {
                    current_edge.next = edge;
                    edge.prev = current_edge;
                    edge.next = null;
                    done = true;
                } else {
                    current_edge = current_edge.next;
                }
            }
        }
    }

    private static void add_to_sbtree(ScanBeamTreeEntries sbte, double y) {
        if (sbte.sb_tree == null) {
            sbte.sb_tree = new ScanBeamTree(y);
            sbte.sbt_entries++;
            return;
        }
        ScanBeamTree tree_node = sbte.sb_tree;
        boolean done = DEBUG;
        while (!done) {
            if (tree_node.f525y > y) {
                if (tree_node.less == null) {
                    tree_node.less = new ScanBeamTree(y);
                    sbte.sbt_entries++;
                    done = true;
                } else {
                    tree_node = tree_node.less;
                }
            } else if (tree_node.f525y >= y) {
                done = true;
            } else if (tree_node.more == null) {
                tree_node.more = new ScanBeamTree(y);
                sbte.sbt_entries++;
                done = true;
            } else {
                tree_node = tree_node.more;
            }
        }
    }

    private static EdgeTable build_lmt(LmtTable lmt_table, ScanBeamTreeEntries sbte, Poly p, int type, OperationType op) {
        EdgeTable edge_table = new EdgeTable(null);
        for (int c = 0; c < p.getNumInnerPoly(); c++) {
            Poly ip = p.getInnerPoly(c);
            if (!ip.isContributing(0)) {
                ip.setContributing(0, true);
            } else {
                int num_vertices = 0;
                int e_index = 0;
                edge_table = new EdgeTable(null);
                for (int i = 0; i < ip.getNumPoints(); i++) {
                    if (OPTIMAL(ip, i)) {
                        edge_table.addNode(ip.getX(i), ip.getY(i));
                        add_to_sbtree(sbte, ip.getY(i));
                        num_vertices++;
                    }
                }
                for (int min = 0; min < num_vertices; min++) {
                    if (edge_table.FWD_MIN(min)) {
                        int num_edges = 1;
                        for (int max = NEXT_INDEX(min, num_vertices); edge_table.NOT_FMAX(max); max = NEXT_INDEX(max, num_vertices)) {
                            num_edges++;
                        }
                        int v = min;
                        EdgeNode e = edge_table.getNode(e_index);
                        e.bstate[1] = BundleState.UNBUNDLED;
                        e.bundle[1][0] = 0;
                        e.bundle[1][1] = 0;
                        int i2 = 0;
                        while (i2 < num_edges) {
                            EdgeNode ei = edge_table.getNode(e_index + i2);
                            EdgeNode ev = edge_table.getNode(v);
                            ei.f517xb = ev.vertex.f531x;
                            ei.bot.f531x = ev.vertex.f531x;
                            ei.bot.f532y = ev.vertex.f532y;
                            v = NEXT_INDEX(v, num_vertices);
                            EdgeNode ev2 = edge_table.getNode(v);
                            ei.top.f531x = ev2.vertex.f531x;
                            ei.top.f532y = ev2.vertex.f532y;
                            ei.f516dx = (ev2.vertex.f531x - ei.bot.f531x) / (ei.top.f532y - ei.bot.f532y);
                            ei.type = type;
                            ei.outp[0] = null;
                            ei.outp[1] = null;
                            ei.next = null;
                            ei.prev = null;
                            ei.succ = (num_edges <= 1 || i2 >= num_edges + -1) ? null : edge_table.getNode(e_index + i2 + 1);
                            ei.next_bound = null;
                            ei.bside[0] = op == OperationType.GPC_DIFF ? 1 : 0;
                            ei.bside[1] = 0;
                            i2++;
                        }
                        insert_bound(bound_list(lmt_table, edge_table.getNode(min).vertex.f532y), e);
                        e_index += num_edges;
                    }
                }
                for (int min2 = 0; min2 < num_vertices; min2++) {
                    if (edge_table.REV_MIN(min2)) {
                        int num_edges2 = 1;
                        for (int max2 = PREV_INDEX(min2, num_vertices); edge_table.NOT_RMAX(max2); max2 = PREV_INDEX(max2, num_vertices)) {
                            num_edges2++;
                        }
                        int v2 = min2;
                        EdgeNode e2 = edge_table.getNode(e_index);
                        e2.bstate[1] = BundleState.UNBUNDLED;
                        e2.bundle[1][0] = 0;
                        e2.bundle[1][1] = 0;
                        int i3 = 0;
                        while (i3 < num_edges2) {
                            EdgeNode ei2 = edge_table.getNode(e_index + i3);
                            EdgeNode ev3 = edge_table.getNode(v2);
                            ei2.f517xb = ev3.vertex.f531x;
                            ei2.bot.f531x = ev3.vertex.f531x;
                            ei2.bot.f532y = ev3.vertex.f532y;
                            v2 = PREV_INDEX(v2, num_vertices);
                            EdgeNode ev4 = edge_table.getNode(v2);
                            ei2.top.f531x = ev4.vertex.f531x;
                            ei2.top.f532y = ev4.vertex.f532y;
                            ei2.f516dx = (ev4.vertex.f531x - ei2.bot.f531x) / (ei2.top.f532y - ei2.bot.f532y);
                            ei2.type = type;
                            ei2.outp[0] = null;
                            ei2.outp[1] = null;
                            ei2.next = null;
                            ei2.prev = null;
                            ei2.succ = (num_edges2 <= 1 || i3 >= num_edges2 + -1) ? null : edge_table.getNode(e_index + i3 + 1);
                            ei2.next_bound = null;
                            ei2.bside[0] = op == OperationType.GPC_DIFF ? 1 : 0;
                            ei2.bside[1] = 0;
                            i3++;
                        }
                        insert_bound(bound_list(lmt_table, edge_table.getNode(min2).vertex.f532y), e2);
                        e_index += num_edges2;
                    }
                }
            }
        }
        return edge_table;
    }

    /* access modifiers changed from: private */
    public static StNode add_st_edge(StNode st, ItNodeTable it, EdgeNode edge, double dy) {
        if (st == null) {
            return new StNode(edge, null);
        }
        double den = (st.f528xt - st.f527xb) - (edge.f518xt - edge.f517xb);
        if (edge.f518xt >= st.f528xt || edge.f516dx == st.f526dx || Math.abs(den) <= GPC_EPSILON) {
            return new StNode(edge, st);
        }
        double r = (edge.f517xb - st.f527xb) / den;
        it.top_node = add_intersection(it.top_node, st.edge, edge, st.f527xb + ((st.f528xt - st.f527xb) * r), r * dy);
        st.prev = add_st_edge(st.prev, it, edge, dy);
        return st;
    }

    private static ItNode add_intersection(ItNode it_node, EdgeNode edge0, EdgeNode edge1, double x, double y) {
        if (it_node == null) {
            return new ItNode(edge0, edge1, x, y, null);
        }
        if (it_node.point.f532y > y) {
            return new ItNode(edge0, edge1, x, y, it_node);
        }
        it_node.next = add_intersection(it_node.next, edge0, edge1, x, y);
        return it_node;
    }

    /* access modifiers changed from: private */
    public static class OperationType {
        public static final OperationType GPC_DIFF = new OperationType("Difference");
        public static final OperationType GPC_INT = new OperationType("Intersection");
        public static final OperationType GPC_UNION = new OperationType("Union");
        public static final OperationType GPC_XOR = new OperationType("Exclusive or");
        private String m_Type;

        private OperationType(String type) {
            this.m_Type = type;
        }

        public String toString() {
            return this.m_Type;
        }
    }

    /* access modifiers changed from: private */
    public static class VertexType {
        public static final int ELI = 2;
        public static final int EMM = 9;
        public static final int EMN = 8;
        public static final int EMX = 1;
        public static final int ERI = 4;
        public static final int ILI = 11;
        public static final int IMM = 6;
        public static final int IMN = 7;
        public static final int IMX = 14;
        public static final int IRI = 13;
        public static final int LED = 10;
        public static final int RED = 5;

        private VertexType() {
        }

        public static int getType(int tr, int tl, int br, int bl) {
            return (tl << 1) + tr + (br << 2) + (bl << 3);
        }
    }

    /* access modifiers changed from: private */
    public static class HState {

        /* renamed from: BH */
        public static final int f519BH = 1;

        /* renamed from: NH */
        public static final int f520NH = 0;

        /* renamed from: TH */
        public static final int f521TH = 2;
        public static final int[][] next_h_state;

        private HState() {
        }

        static {
            int[] iArr = new int[6];
            iArr[0] = 1;
            iArr[1] = 2;
            iArr[2] = 2;
            iArr[3] = 1;
            int[] iArr2 = new int[6];
            iArr2[4] = 2;
            iArr2[5] = 2;
            int[] iArr3 = new int[6];
            iArr3[4] = 1;
            iArr3[5] = 1;
            next_h_state = new int[][]{iArr, iArr2, iArr3};
        }
    }

    /* access modifiers changed from: private */
    public static class BundleState {
        public static final BundleState BUNDLE_HEAD = new BundleState("BUNDLE_HEAD");
        public static final BundleState BUNDLE_TAIL = new BundleState("BUNDLE_TAIL");
        public static final BundleState UNBUNDLED = new BundleState("UNBUNDLED");
        private String m_State;

        private BundleState(String state) {
            this.m_State = state;
        }

        public String toString() {
            return this.m_State;
        }
    }

    /* access modifiers changed from: private */
    public static class VertexNode {
        VertexNode next = null;

        /* renamed from: x */
        double f529x;

        /* renamed from: y */
        double f530y;

        public VertexNode(double x, double y) {
            this.f529x = x;
            this.f530y = y;
        }
    }

    /* access modifiers changed from: private */
    public static class PolygonNode {
        int active;
        boolean hole;
        PolygonNode next;
        PolygonNode proxy;

        /* renamed from: v */
        VertexNode[] f524v = new VertexNode[2];

        public PolygonNode(PolygonNode next2, double x, double y) {
            VertexNode vn = new VertexNode(x, y);
            this.f524v[0] = vn;
            this.f524v[1] = vn;
            this.next = next2;
            this.proxy = this;
            this.active = 1;
        }

        public void add_right(double x, double y) {
            VertexNode nv = new VertexNode(x, y);
            this.proxy.f524v[1].next = nv;
            this.proxy.f524v[1] = nv;
        }

        public void add_left(double x, double y) {
            VertexNode nv = new VertexNode(x, y);
            nv.next = this.proxy.f524v[0];
            this.proxy.f524v[0] = nv;
        }
    }

    /* access modifiers changed from: private */
    public static class TopPolygonNode {
        PolygonNode top_node;

        private TopPolygonNode() {
            this.top_node = null;
        }

        /* synthetic */ TopPolygonNode(TopPolygonNode topPolygonNode) {
            this();
        }

        public PolygonNode add_local_min(double x, double y) {
            this.top_node = new PolygonNode(this.top_node, x, y);
            return this.top_node;
        }

        public void merge_left(PolygonNode p, PolygonNode q) {
            q.proxy.hole = true;
            if (p.proxy != q.proxy) {
                p.proxy.f524v[1].next = q.proxy.f524v[0];
                q.proxy.f524v[0] = p.proxy.f524v[0];
                PolygonNode target = p.proxy;
                for (PolygonNode node = this.top_node; node != null; node = node.next) {
                    if (node.proxy == target) {
                        node.active = 0;
                        node.proxy = q.proxy;
                    }
                }
            }
        }

        public void merge_right(PolygonNode p, PolygonNode q) {
            q.proxy.hole = Clip.DEBUG;
            if (p.proxy != q.proxy) {
                q.proxy.f524v[1].next = p.proxy.f524v[0];
                q.proxy.f524v[1] = p.proxy.f524v[1];
                PolygonNode target = p.proxy;
                for (PolygonNode node = this.top_node; node != null; node = node.next) {
                    if (node.proxy == target) {
                        node.active = 0;
                        node.proxy = q.proxy;
                    }
                }
            }
        }

        public int count_contours() {
            int nc = 0;
            for (PolygonNode polygon = this.top_node; polygon != null; polygon = polygon.next) {
                if (polygon.active != 0) {
                    int nv = 0;
                    for (VertexNode v = polygon.proxy.f524v[0]; v != null; v = v.next) {
                        nv++;
                    }
                    if (nv > 2) {
                        polygon.active = nv;
                        nc++;
                    } else {
                        polygon.active = 0;
                    }
                }
            }
            return nc;
        }

        public Poly getResult(Class<? extends Poly> polyClass) {
            Poly result = Clip.createNewPoly(polyClass);
            int num_contours = count_contours();
            if (num_contours > 0) {
                int c = 0;
                PolygonNode poly_node = this.top_node;
                while (poly_node != null) {
                    PolygonNode npoly_node = poly_node.next;
                    if (poly_node.active != 0) {
                        Poly poly = result;
                        if (num_contours > 1) {
                            poly = Clip.createNewPoly(polyClass);
                        }
                        if (poly_node.proxy.hole) {
                            poly.setIsHole(poly_node.proxy.hole);
                        }
                        for (VertexNode vtx = poly_node.proxy.f524v[0]; vtx != null; vtx = vtx.next) {
                            poly.add(vtx.f529x, vtx.f530y);
                        }
                        if (num_contours > 1) {
                            result.add(poly);
                        }
                        c++;
                    }
                    poly_node = npoly_node;
                }
                result = Clip.createNewPoly(polyClass);
                for (int i = 0; i < result.getNumInnerPoly(); i++) {
                    Poly inner = result.getInnerPoly(i);
                    if (!inner.isHole()) {
                        result.add(inner);
                    }
                }
                for (int i2 = 0; i2 < result.getNumInnerPoly(); i2++) {
                    Poly inner2 = result.getInnerPoly(i2);
                    if (inner2.isHole()) {
                        result.add(inner2);
                    }
                }
            }
            return result;
        }

        public void print() {
            int c = 0;
            PolygonNode poly_node = this.top_node;
            while (poly_node != null) {
                PolygonNode npoly_node = poly_node.next;
                if (poly_node.active != 0) {
                    for (VertexNode vtx = poly_node.proxy.f524v[0]; vtx != null; vtx = vtx.next) {
                    }
                    c++;
                }
                poly_node = npoly_node;
            }
        }
    }

    /* access modifiers changed from: private */
    public static class EdgeNode {
        Point2D bot;
        int[] bside;
        BundleState[] bstate;
        int[][] bundle;

        /* renamed from: dx */
        double f516dx;
        EdgeNode next;
        EdgeNode next_bound;
        PolygonNode[] outp;
        EdgeNode prev;
        EdgeNode succ;
        Point2D top;
        int type;
        Point2D vertex;

        /* renamed from: xb */
        double f517xb;

        /* renamed from: xt */
        double f518xt;

        private EdgeNode() {
            this.vertex = new Point2D();
            this.bot = new Point2D();
            this.top = new Point2D();
            this.bundle = (int[][]) Array.newInstance(Integer.TYPE, 2, 2);
            this.bside = new int[2];
            this.bstate = new BundleState[2];
            this.outp = new PolygonNode[2];
        }

        /* synthetic */ EdgeNode(EdgeNode edgeNode) {
            this();
        }
    }

    /* access modifiers changed from: private */
    public static class AetTree {
        EdgeNode top_node;

        private AetTree() {
        }

        /* synthetic */ AetTree(AetTree aetTree) {
            this();
        }

        public void print() {
            for (EdgeNode edge = this.top_node; edge != null; edge = edge.next) {
            }
        }
    }

    /* access modifiers changed from: private */
    public static class EdgeTable {
        private List<EdgeNode> m_List;

        private EdgeTable() {
            this.m_List = new ArrayList();
        }

        /* synthetic */ EdgeTable(EdgeTable edgeTable) {
            this();
        }

        public void addNode(double x, double y) {
            EdgeNode node = new EdgeNode(null);
            node.vertex.f531x = x;
            node.vertex.f532y = y;
            this.m_List.add(node);
        }

        public EdgeNode getNode(int index) {
            return this.m_List.get(index);
        }

        public boolean FWD_MIN(int i) {
            EdgeNode next = this.m_List.get(Clip.NEXT_INDEX(i, this.m_List.size()));
            EdgeNode ith = this.m_List.get(i);
            if (this.m_List.get(Clip.PREV_INDEX(i, this.m_List.size())).vertex.getY() < ith.vertex.getY() || next.vertex.getY() <= ith.vertex.getY()) {
                return Clip.DEBUG;
            }
            return true;
        }

        public boolean NOT_FMAX(int i) {
            if (this.m_List.get(Clip.NEXT_INDEX(i, this.m_List.size())).vertex.getY() > this.m_List.get(i).vertex.getY()) {
                return true;
            }
            return Clip.DEBUG;
        }

        public boolean REV_MIN(int i) {
            EdgeNode next = this.m_List.get(Clip.NEXT_INDEX(i, this.m_List.size()));
            EdgeNode ith = this.m_List.get(i);
            if (this.m_List.get(Clip.PREV_INDEX(i, this.m_List.size())).vertex.getY() <= ith.vertex.getY() || next.vertex.getY() < ith.vertex.getY()) {
                return Clip.DEBUG;
            }
            return true;
        }

        public boolean NOT_RMAX(int i) {
            if (this.m_List.get(Clip.PREV_INDEX(i, this.m_List.size())).vertex.getY() > this.m_List.get(i).vertex.getY()) {
                return true;
            }
            return Clip.DEBUG;
        }
    }

    /* access modifiers changed from: private */
    public static class LmtNode {
        EdgeNode first_bound;
        LmtNode next;

        /* renamed from: y */
        double f523y;

        public LmtNode(double yvalue) {
            this.f523y = yvalue;
        }
    }

    /* access modifiers changed from: private */
    public static class LmtTable {
        LmtNode top_node;

        private LmtTable() {
        }

        /* synthetic */ LmtTable(LmtTable lmtTable) {
            this();
        }

        public void print() {
            int n = 0;
            for (LmtNode lmt = this.top_node; lmt != null; lmt = lmt.next) {
                for (EdgeNode edge = lmt.first_bound; edge != null; edge = edge.next_bound) {
                }
                n++;
            }
        }
    }

    /* access modifiers changed from: private */
    public static class ScanBeamTree {
        ScanBeamTree less;
        ScanBeamTree more;

        /* renamed from: y */
        double f525y;

        public ScanBeamTree(double yvalue) {
            this.f525y = yvalue;
        }
    }

    /* access modifiers changed from: private */
    public static class ScanBeamTreeEntries {
        ScanBeamTree sb_tree;
        int sbt_entries;

        private ScanBeamTreeEntries() {
        }

        /* synthetic */ ScanBeamTreeEntries(ScanBeamTreeEntries scanBeamTreeEntries) {
            this();
        }

        public double[] build_sbt() {
            double[] sbt = new double[this.sbt_entries];
            if (inner_build_sbt(0, sbt, this.sb_tree) == this.sbt_entries) {
                return sbt;
            }
            throw new IllegalStateException("Something went wrong buildign sbt from tree.");
        }

        private int inner_build_sbt(int entries, double[] sbt, ScanBeamTree sbt_node) {
            if (sbt_node.less != null) {
                entries = inner_build_sbt(entries, sbt, sbt_node.less);
            }
            sbt[entries] = sbt_node.f525y;
            int entries2 = entries + 1;
            if (sbt_node.more != null) {
                return inner_build_sbt(entries2, sbt, sbt_node.more);
            }
            return entries2;
        }
    }

    /* access modifiers changed from: private */
    public static class ItNode {

        /* renamed from: ie */
        EdgeNode[] f522ie = new EdgeNode[2];
        ItNode next;
        Point2D point = new Point2D();

        public ItNode(EdgeNode edge0, EdgeNode edge1, double x, double y, ItNode next2) {
            this.f522ie[0] = edge0;
            this.f522ie[1] = edge1;
            this.point.f531x = x;
            this.point.f532y = y;
            this.next = next2;
        }
    }

    /* access modifiers changed from: private */
    public static class ItNodeTable {
        ItNode top_node;

        private ItNodeTable() {
        }

        /* synthetic */ ItNodeTable(ItNodeTable itNodeTable) {
            this();
        }

        public void build_intersection_table(AetTree aet, double dy) {
            StNode st = null;
            for (EdgeNode edge = aet.top_node; edge != null; edge = edge.next) {
                if (edge.bstate[0] == BundleState.BUNDLE_HEAD || edge.bundle[0][0] != 0 || edge.bundle[0][1] != 0) {
                    st = Clip.add_st_edge(st, this, edge, dy);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static class StNode {

        /* renamed from: dx */
        double f526dx;
        EdgeNode edge;
        StNode prev;

        /* renamed from: xb */
        double f527xb;

        /* renamed from: xt */
        double f528xt;

        public StNode(EdgeNode edge2, StNode prev2) {
            this.edge = edge2;
            this.f527xb = edge2.f517xb;
            this.f528xt = edge2.f518xt;
            this.f526dx = edge2.f516dx;
            this.prev = prev2;
        }
    }

    private static void print_sbt(double[] sbt) {
        for (int i = 0; i < sbt.length; i++) {
        }
    }
}
