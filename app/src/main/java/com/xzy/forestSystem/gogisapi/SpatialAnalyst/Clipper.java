package  com.xzy.forestSystem.gogisapi.SpatialAnalyst;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Clipper extends ClipperBase {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$ClipType;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$Clipper$NodeType;
    private static /* synthetic */ int[] $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$PolyFillType;
    public boolean ReverseSolution;
    public boolean StrictlySimple;
    public final int ioPreserveCollinear;
    public final int ioReverseSolution;
    public final int ioStrictlySimple;
    private TEdge m_ActiveEdges;
    private PolyFillType m_ClipFillType;
    private ClipType m_ClipType;
    private boolean m_ExecuteLocked;
    private List<Join> m_GhostJoins;
    private List<IntersectNode> m_IntersectList;
    MyComparator m_IntersectNodeComparer;
    private List<Join> m_Joins;
    private List<OutRec> m_PolyOuts;
    private Scanbeam m_Scanbeam;
    private TEdge m_SortedEdges;
    private PolyFillType m_SubjFillType;
    private boolean m_UsingPolyTree;

    /* access modifiers changed from: package-private */
    public enum NodeType {
        ntAny,
        ntOpen,
        ntClosed
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$ClipType() {
        int[] iArr = $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$ClipType;
        if (iArr == null) {
            iArr = new int[ClipType.values().length];
            try {
                iArr[ClipType.ctDifference.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ClipType.ctIntersection.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ClipType.ctUnion.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ClipType.ctXor.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$ClipType = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$Clipper$NodeType() {
        int[] iArr = $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$Clipper$NodeType;
        if (iArr == null) {
            iArr = new int[NodeType.values().length];
            try {
                iArr[NodeType.ntAny.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[NodeType.ntClosed.ordinal()] = 3;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[NodeType.ntOpen.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$Clipper$NodeType = iArr;
        }
        return iArr;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$PolyFillType() {
        int[] iArr = $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$PolyFillType;
        if (iArr == null) {
            iArr = new int[PolyFillType.values().length];
            try {
                iArr[PolyFillType.pftEvenOdd.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[PolyFillType.pftNegative.ordinal()] = 4;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[PolyFillType.pftNonZero.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[PolyFillType.pftPositive.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$PolyFillType = iArr;
        }
        return iArr;
    }

    public Clipper() {
        this(0);
    }

    public Clipper(int InitOptions) {
        boolean z;
        boolean z2;
        boolean z3 = true;
        this.ioReverseSolution = 1;
        this.ioStrictlySimple = 2;
        this.ioPreserveCollinear = 4;
        this.m_Scanbeam = null;
        this.m_ActiveEdges = null;
        this.m_SortedEdges = null;
        this.m_IntersectList = new ArrayList();
        this.m_IntersectNodeComparer = new MyComparator();
        this.m_ExecuteLocked = false;
        this.m_UsingPolyTree = false;
        this.m_PolyOuts = new ArrayList();
        this.m_Joins = new ArrayList();
        this.m_GhostJoins = new ArrayList();
        if ((InitOptions & 1) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.ReverseSolution = z;
        if ((InitOptions & 2) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.StrictlySimple = z2;
        this.PreserveCollinear = (InitOptions & 4) == 0 ? false : z3;
    }

    /* access modifiers changed from: package-private */
    public void DisposeScanbeamList() {
        while (this.m_Scanbeam != null) {
            Scanbeam sb2 = this.m_Scanbeam.Next;
            this.m_Scanbeam = null;
            this.m_Scanbeam = sb2;
        }
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.gogisapi.SpatialAnalyst.ClipperBase
    public void Reset() {
        super.Reset();
        this.m_Scanbeam = null;
        this.m_ActiveEdges = null;
        this.m_SortedEdges = null;
        for (LocalMinima lm = this.m_MinimaList; lm != null; lm = lm.Next) {
            InsertScanbeam(lm.f515Y);
        }
    }

    private void InsertScanbeam(int Y) {
        if (this.m_Scanbeam == null) {
            this.m_Scanbeam = new Scanbeam();
            this.m_Scanbeam.Next = null;
            this.m_Scanbeam.f536Y = Y;
        } else if (Y > this.m_Scanbeam.f536Y) {
            Scanbeam newSb = new Scanbeam();
            newSb.f536Y = Y;
            newSb.Next = this.m_Scanbeam;
            this.m_Scanbeam = newSb;
        } else {
            Scanbeam sb2 = this.m_Scanbeam;
            while (sb2.Next != null && Y <= sb2.Next.f536Y) {
                sb2 = sb2.Next;
            }
            if (Y != sb2.f536Y) {
                Scanbeam newSb2 = new Scanbeam();
                newSb2.f536Y = Y;
                newSb2.Next = sb2.Next;
                sb2.Next = newSb2;
            }
        }
    }

    public boolean Execute(ClipType clipType, List<List<IntPoint>> solution, PolyFillType subjFillType, PolyFillType clipFillType) {
        boolean z = false;
        if (this.m_ExecuteLocked) {
            return false;
        }
        this.m_ExecuteLocked = true;
        solution.clear();
        this.m_SubjFillType = subjFillType;
        this.m_ClipFillType = clipFillType;
        this.m_ClipType = clipType;
        this.m_UsingPolyTree = false;
        try {
            boolean succeeded = ExecuteInternal();
            if (succeeded) {
                BuildResult(solution);
            }
            return succeeded;
        } finally {
            DisposeAllPolyPts();
            this.m_ExecuteLocked = z;
        }
    }

    public boolean Execute(ClipType clipType, PolyTree polytree, PolyFillType subjFillType, PolyFillType clipFillType) {
        boolean z = false;
        if (this.m_ExecuteLocked) {
            return false;
        }
        this.m_ExecuteLocked = true;
        this.m_SubjFillType = subjFillType;
        this.m_ClipFillType = clipFillType;
        this.m_ClipType = clipType;
        this.m_UsingPolyTree = true;
        try {
            boolean succeeded = ExecuteInternal();
            if (succeeded) {
                BuildResult2(polytree);
            }
            return succeeded;
        } finally {
            DisposeAllPolyPts();
            this.m_ExecuteLocked = z;
        }
    }

    public boolean Execute(ClipType clipType, List<List<IntPoint>> solution) {
        return Execute(clipType, solution, PolyFillType.pftEvenOdd, PolyFillType.pftEvenOdd);
    }

    public boolean Execute(ClipType clipType, PolyTree polytree) {
        return Execute(clipType, polytree, PolyFillType.pftEvenOdd, PolyFillType.pftEvenOdd);
    }

    /* access modifiers changed from: package-private */
    public void FixHoleLinkage(OutRec outRec) {
        if (outRec.FirstLeft == null) {
            return;
        }
        if (outRec.IsHole == outRec.FirstLeft.IsHole || outRec.FirstLeft.Pts == null) {
            OutRec orfl = outRec.FirstLeft;
            while (orfl != null && (orfl.IsHole == outRec.IsHole || orfl.Pts == null)) {
                orfl = orfl.FirstLeft;
            }
            outRec.FirstLeft = orfl;
        }
    }

    private boolean ExecuteInternal() {
        try {
            Reset();
            if (this.m_CurrentLM == null) {
                return false;
            }
            int botY = PopScanbeam();
            while (true) {
                InsertLocalMinimaIntoAEL(botY);
                this.m_GhostJoins.clear();
                ProcessHorizontals(false);
                if (this.m_Scanbeam == null) {
                    break;
                }
                int topY = PopScanbeam();
                if (ProcessIntersections(topY)) {
                    ProcessEdgesAtTopOfScanbeam(topY);
                    botY = topY;
                    if (this.m_Scanbeam == null && this.m_CurrentLM == null) {
                        break;
                    }
                } else {
                    this.m_Joins.clear();
                    this.m_GhostJoins.clear();
                    return false;
                }
            }
            for (int i = 0; i < this.m_PolyOuts.size(); i++) {
                OutRec outRec = this.m_PolyOuts.get(i);
                if (outRec.Pts != null && !outRec.IsOpen) {
                    if ((this.ReverseSolution ^ outRec.IsHole) == (Area(outRec) > 0.0d)) {
                        ReversePolyPtLinks(outRec.Pts);
                    }
                }
            }
            JoinCommonEdges();
            for (int i2 = 0; i2 < this.m_PolyOuts.size(); i2++) {
                OutRec outRec2 = this.m_PolyOuts.get(i2);
                if (outRec2.Pts != null && !outRec2.IsOpen) {
                    FixupOutPolygon(outRec2);
                }
            }
            if (this.StrictlySimple) {
                DoSimplePolygons();
            }
            this.m_Joins.clear();
            this.m_GhostJoins.clear();
            return true;
        } finally {
            this.m_Joins.clear();
            this.m_GhostJoins.clear();
        }
    }

    private int PopScanbeam() {
        int Y = this.m_Scanbeam.f536Y;
        this.m_Scanbeam = this.m_Scanbeam.Next;
        return Y;
    }

    private void DisposeAllPolyPts() {
        for (int i = 0; i < this.m_PolyOuts.size(); i++) {
            DisposeOutRec(i);
        }
        this.m_PolyOuts.clear();
    }

    /* access modifiers changed from: package-private */
    public void DisposeOutRec(int index) {
        this.m_PolyOuts.get(index).Pts = null;
    }

    private void AddJoin(OutPt Op1, OutPt Op2, IntPoint OffPt) {
        Join j = new Join();
        j.OutPt1 = Op1;
        j.OutPt2 = Op2;
        j.OffPt = OffPt;
        this.m_Joins.add(j);
    }

    private void AddGhostJoin(OutPt Op, IntPoint OffPt) {
        Join j = new Join();
        j.OutPt1 = Op;
        j.OffPt = OffPt;
        this.m_GhostJoins.add(j);
    }

    private void InsertLocalMinimaIntoAEL(int botY) {
        while (this.m_CurrentLM != null && this.m_CurrentLM.f515Y == botY) {
            TEdge lb = this.m_CurrentLM.LeftBound;
            TEdge rb = this.m_CurrentLM.RightBound;
            PopLocalMinima();
            OutPt Op1 = null;
            if (lb == null) {
                InsertEdgeIntoAEL(rb, null);
                SetWindingCount(rb);
                if (IsContributing(rb)) {
                    Op1 = AddOutPt(rb, rb.Bot);
                }
            } else if (rb == null) {
                InsertEdgeIntoAEL(lb, null);
                SetWindingCount(lb);
                if (IsContributing(lb)) {
                    Op1 = AddOutPt(lb, lb.Bot);
                }
                InsertScanbeam(lb.Top.f513Y);
            } else {
                InsertEdgeIntoAEL(lb, null);
                InsertEdgeIntoAEL(rb, lb);
                SetWindingCount(lb);
                rb.WindCnt = lb.WindCnt;
                rb.WindCnt2 = lb.WindCnt2;
                if (IsContributing(lb)) {
                    Op1 = AddLocalMinPoly(lb, rb, lb.Bot);
                }
                InsertScanbeam(lb.Top.f513Y);
            }
            if (rb != null) {
                if (IsHorizontal(rb)) {
                    AddEdgeToSEL(rb);
                } else {
                    InsertScanbeam(rb.Top.f513Y);
                }
            }
            if (!(lb == null || rb == null)) {
                if (Op1 != null && IsHorizontal(rb) && this.m_GhostJoins.size() > 0 && rb.WindDelta != 0) {
                    for (int i = 0; i < this.m_GhostJoins.size(); i++) {
                        Join j = this.m_GhostJoins.get(i);
                        if (HorzSegmentsOverlap(j.OutPt1.f535Pt.f512X, j.OffPt.f512X, rb.Bot.f512X, rb.Top.f512X)) {
                            AddJoin(j.OutPt1, Op1, j.OffPt);
                        }
                    }
                }
                if (lb.OutIdx >= 0 && lb.PrevInAEL != null && lb.PrevInAEL.Curr.f512X == lb.Bot.f512X && lb.PrevInAEL.OutIdx >= 0 && SlopesEqual(lb.PrevInAEL, lb, this.m_UseFullRange) && lb.WindDelta != 0 && lb.PrevInAEL.WindDelta != 0) {
                    AddJoin(Op1, AddOutPt(lb.PrevInAEL, lb.Bot), lb.Top);
                }
                if (lb.NextInAEL != rb) {
                    if (rb.OutIdx >= 0 && rb.PrevInAEL.OutIdx >= 0 && SlopesEqual(rb.PrevInAEL, rb, this.m_UseFullRange) && rb.WindDelta != 0 && rb.PrevInAEL.WindDelta != 0) {
                        AddJoin(Op1, AddOutPt(rb.PrevInAEL, rb.Bot), rb.Top);
                    }
                    TEdge e = lb.NextInAEL;
                    if (e != null) {
                        while (e != rb) {
                            IntersectEdges(rb, e, lb.Curr);
                            e = e.NextInAEL;
                        }
                    }
                }
            }
        }
    }

    private void InsertEdgeIntoAEL(TEdge edge, TEdge startEdge) {
        if (this.m_ActiveEdges == null) {
            edge.PrevInAEL = null;
            edge.NextInAEL = null;
            this.m_ActiveEdges = edge;
        } else if (startEdge != null || !E2InsertsBeforeE1(this.m_ActiveEdges, edge)) {
            if (startEdge == null) {
                startEdge = this.m_ActiveEdges;
            }
            while (startEdge.NextInAEL != null && !E2InsertsBeforeE1(startEdge.NextInAEL, edge)) {
                startEdge = startEdge.NextInAEL;
            }
            edge.NextInAEL = startEdge.NextInAEL;
            if (startEdge.NextInAEL != null) {
                startEdge.NextInAEL.PrevInAEL = edge;
            }
            edge.PrevInAEL = startEdge;
            startEdge.NextInAEL = edge;
        } else {
            edge.PrevInAEL = null;
            edge.NextInAEL = this.m_ActiveEdges;
            this.m_ActiveEdges.PrevInAEL = edge;
            this.m_ActiveEdges = edge;
        }
    }

    private boolean E2InsertsBeforeE1(TEdge e1, TEdge e2) {
        return e2.Curr.f512X == e1.Curr.f512X ? e2.Top.f513Y > e1.Top.f513Y ? e2.Top.f512X < TopX(e1, e2.Top.f513Y) : e1.Top.f512X > TopX(e2, e1.Top.f513Y) : e2.Curr.f512X < e1.Curr.f512X;
    }

    private boolean IsEvenOddFillType(TEdge edge) {
        return edge.PolyTyp == PolyType.ptSubject ? this.m_SubjFillType == PolyFillType.pftEvenOdd : this.m_ClipFillType == PolyFillType.pftEvenOdd;
    }

    private boolean IsEvenOddAltFillType(TEdge edge) {
        return edge.PolyTyp == PolyType.ptSubject ? this.m_ClipFillType == PolyFillType.pftEvenOdd : this.m_SubjFillType == PolyFillType.pftEvenOdd;
    }

    private boolean IsContributing(TEdge edge) {
        PolyFillType pft;
        PolyFillType pft2;
        if (edge.PolyTyp == PolyType.ptSubject) {
            pft = this.m_SubjFillType;
            pft2 = this.m_ClipFillType;
        } else {
            pft = this.m_ClipFillType;
            pft2 = this.m_SubjFillType;
        }
        switch ($SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$PolyFillType()[pft.ordinal()]) {
            case 1:
                if (edge.WindDelta == 0 && edge.WindCnt != 1) {
                    return false;
                }
            case 2:
                if (Math.abs(edge.WindCnt) != 1) {
                    return false;
                }
                break;
            case 3:
                if (edge.WindCnt != 1) {
                    return false;
                }
                break;
            default:
                if (edge.WindCnt != -1) {
                    return false;
                }
                break;
        }
        switch ($SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$ClipType()[this.m_ClipType.ordinal()]) {
            case 1:
                switch ($SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$PolyFillType()[pft2.ordinal()]) {
                    case 1:
                    case 2:
                        return edge.WindCnt2 != 0;
                    case 3:
                        return edge.WindCnt2 > 0;
                    default:
                        return edge.WindCnt2 < 0;
                }
            case 2:
                switch ($SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$PolyFillType()[pft2.ordinal()]) {
                    case 1:
                    case 2:
                        return edge.WindCnt2 == 0;
                    case 3:
                        return edge.WindCnt2 <= 0;
                    default:
                        return edge.WindCnt2 >= 0;
                }
            case 3:
                if (edge.PolyTyp == PolyType.ptSubject) {
                    switch ($SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$PolyFillType()[pft2.ordinal()]) {
                        case 1:
                        case 2:
                            return edge.WindCnt2 == 0;
                        case 3:
                            return edge.WindCnt2 <= 0;
                        default:
                            return edge.WindCnt2 >= 0;
                    }
                } else {
                    switch ($SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$PolyFillType()[pft2.ordinal()]) {
                        case 1:
                        case 2:
                            return edge.WindCnt2 != 0;
                        case 3:
                            return edge.WindCnt2 > 0;
                        default:
                            return edge.WindCnt2 < 0;
                    }
                }
            case 4:
                if (edge.WindDelta != 0) {
                    return true;
                }
                switch ($SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$PolyFillType()[pft2.ordinal()]) {
                    case 1:
                    case 2:
                        return edge.WindCnt2 == 0;
                    case 3:
                        return edge.WindCnt2 <= 0;
                    default:
                        return edge.WindCnt2 >= 0;
                }
            default:
                return true;
        }
    }

    private void SetWindingCount(TEdge edge) {
        TEdge e;
        TEdge e2 = edge.PrevInAEL;
        while (e2 != null && (e2.PolyTyp != edge.PolyTyp || e2.WindDelta == 0)) {
            e2 = e2.PrevInAEL;
        }
        if (e2 == null) {
            edge.WindCnt = edge.WindDelta == 0 ? 1 : edge.WindDelta;
            edge.WindCnt2 = 0;
            e = this.m_ActiveEdges;
        } else if (edge.WindDelta == 0 && this.m_ClipType != ClipType.ctUnion) {
            edge.WindCnt = 1;
            edge.WindCnt2 = e2.WindCnt2;
            e = e2.NextInAEL;
        } else if (IsEvenOddFillType(edge)) {
            if (edge.WindDelta == 0) {
                boolean Inside = true;
                for (TEdge e22 = e2.PrevInAEL; e22 != null; e22 = e22.PrevInAEL) {
                    if (e22.PolyTyp == e2.PolyTyp && e22.WindDelta != 0) {
                        Inside = !Inside;
                    }
                }
                edge.WindCnt = Inside ? 0 : 1;
            } else {
                edge.WindCnt = edge.WindDelta;
            }
            edge.WindCnt2 = e2.WindCnt2;
            e = e2.NextInAEL;
        } else {
            if (e2.WindCnt * e2.WindDelta < 0) {
                if (Math.abs(e2.WindCnt) <= 1) {
                    edge.WindCnt = edge.WindDelta == 0 ? 1 : edge.WindDelta;
                } else if (e2.WindDelta * edge.WindDelta < 0) {
                    edge.WindCnt = e2.WindCnt;
                } else {
                    edge.WindCnt = e2.WindCnt + edge.WindDelta;
                }
            } else if (edge.WindDelta == 0) {
                edge.WindCnt = e2.WindCnt < 0 ? e2.WindCnt - 1 : e2.WindCnt + 1;
            } else if (e2.WindDelta * edge.WindDelta < 0) {
                edge.WindCnt = e2.WindCnt;
            } else {
                edge.WindCnt = e2.WindCnt + edge.WindDelta;
            }
            edge.WindCnt2 = e2.WindCnt2;
            e = e2.NextInAEL;
        }
        if (IsEvenOddAltFillType(edge)) {
            while (e != edge) {
                if (e.WindDelta != 0) {
                    edge.WindCnt2 = edge.WindCnt2 == 0 ? 1 : 0;
                }
                e = e.NextInAEL;
            }
            return;
        }
        while (e != edge) {
            edge.WindCnt2 += e.WindDelta;
            e = e.NextInAEL;
        }
    }

    private void AddEdgeToSEL(TEdge edge) {
        if (this.m_SortedEdges == null) {
            this.m_SortedEdges = edge;
            edge.PrevInSEL = null;
            edge.NextInSEL = null;
            return;
        }
        edge.NextInSEL = this.m_SortedEdges;
        edge.PrevInSEL = null;
        this.m_SortedEdges.PrevInSEL = edge;
        this.m_SortedEdges = edge;
    }

    private void CopyAELToSEL() {
        TEdge e = this.m_ActiveEdges;
        this.m_SortedEdges = e;
        while (e != null) {
            e.PrevInSEL = e.PrevInAEL;
            e.NextInSEL = e.NextInAEL;
            e = e.NextInAEL;
        }
    }

    private void SwapPositionsInAEL(TEdge edge1, TEdge edge2) {
        if (edge1.NextInAEL != edge1.PrevInAEL && edge2.NextInAEL != edge2.PrevInAEL) {
            if (edge1.NextInAEL == edge2) {
                TEdge next = edge2.NextInAEL;
                if (next != null) {
                    next.PrevInAEL = edge1;
                }
                TEdge prev = edge1.PrevInAEL;
                if (prev != null) {
                    prev.NextInAEL = edge2;
                }
                edge2.PrevInAEL = prev;
                edge2.NextInAEL = edge1;
                edge1.PrevInAEL = edge2;
                edge1.NextInAEL = next;
            } else if (edge2.NextInAEL == edge1) {
                TEdge next2 = edge1.NextInAEL;
                if (next2 != null) {
                    next2.PrevInAEL = edge2;
                }
                TEdge prev2 = edge2.PrevInAEL;
                if (prev2 != null) {
                    prev2.NextInAEL = edge1;
                }
                edge1.PrevInAEL = prev2;
                edge1.NextInAEL = edge2;
                edge2.PrevInAEL = edge1;
                edge2.NextInAEL = next2;
            } else {
                TEdge next3 = edge1.NextInAEL;
                TEdge prev3 = edge1.PrevInAEL;
                edge1.NextInAEL = edge2.NextInAEL;
                if (edge1.NextInAEL != null) {
                    edge1.NextInAEL.PrevInAEL = edge1;
                }
                edge1.PrevInAEL = edge2.PrevInAEL;
                if (edge1.PrevInAEL != null) {
                    edge1.PrevInAEL.NextInAEL = edge1;
                }
                edge2.NextInAEL = next3;
                if (edge2.NextInAEL != null) {
                    edge2.NextInAEL.PrevInAEL = edge2;
                }
                edge2.PrevInAEL = prev3;
                if (edge2.PrevInAEL != null) {
                    edge2.PrevInAEL.NextInAEL = edge2;
                }
            }
            if (edge1.PrevInAEL == null) {
                this.m_ActiveEdges = edge1;
            } else if (edge2.PrevInAEL == null) {
                this.m_ActiveEdges = edge2;
            }
        }
    }

    private void SwapPositionsInSEL(TEdge edge1, TEdge edge2) {
        if (edge1.NextInSEL != null || edge1.PrevInSEL != null) {
            if (edge2.NextInSEL != null || edge2.PrevInSEL != null) {
                if (edge1.NextInSEL == edge2) {
                    TEdge next = edge2.NextInSEL;
                    if (next != null) {
                        next.PrevInSEL = edge1;
                    }
                    TEdge prev = edge1.PrevInSEL;
                    if (prev != null) {
                        prev.NextInSEL = edge2;
                    }
                    edge2.PrevInSEL = prev;
                    edge2.NextInSEL = edge1;
                    edge1.PrevInSEL = edge2;
                    edge1.NextInSEL = next;
                } else if (edge2.NextInSEL == edge1) {
                    TEdge next2 = edge1.NextInSEL;
                    if (next2 != null) {
                        next2.PrevInSEL = edge2;
                    }
                    TEdge prev2 = edge2.PrevInSEL;
                    if (prev2 != null) {
                        prev2.NextInSEL = edge1;
                    }
                    edge1.PrevInSEL = prev2;
                    edge1.NextInSEL = edge2;
                    edge2.PrevInSEL = edge1;
                    edge2.NextInSEL = next2;
                } else {
                    TEdge next3 = edge1.NextInSEL;
                    TEdge prev3 = edge1.PrevInSEL;
                    edge1.NextInSEL = edge2.NextInSEL;
                    if (edge1.NextInSEL != null) {
                        edge1.NextInSEL.PrevInSEL = edge1;
                    }
                    edge1.PrevInSEL = edge2.PrevInSEL;
                    if (edge1.PrevInSEL != null) {
                        edge1.PrevInSEL.NextInSEL = edge1;
                    }
                    edge2.NextInSEL = next3;
                    if (edge2.NextInSEL != null) {
                        edge2.NextInSEL.PrevInSEL = edge2;
                    }
                    edge2.PrevInSEL = prev3;
                    if (edge2.PrevInSEL != null) {
                        edge2.PrevInSEL.NextInSEL = edge2;
                    }
                }
                if (edge1.PrevInSEL == null) {
                    this.m_SortedEdges = edge1;
                } else if (edge2.PrevInSEL == null) {
                    this.m_SortedEdges = edge2;
                }
            }
        }
    }

    private void AddLocalMaxPoly(TEdge e1, TEdge e2, IntPoint pt) {
        AddOutPt(e1, pt);
        if (e2.WindDelta == 0) {
            AddOutPt(e2, pt);
        }
        if (e1.OutIdx == e2.OutIdx) {
            e1.OutIdx = -1;
            e2.OutIdx = -1;
        } else if (e1.OutIdx < e2.OutIdx) {
            AppendPolygon(e1, e2);
        } else {
            AppendPolygon(e2, e1);
        }
    }

    private OutPt AddLocalMinPoly(TEdge e1, TEdge e2, IntPoint pt) {
        OutPt result;
        TEdge prevE;
        TEdge e;
        if (IsHorizontal(e2) || e1.f537Dx > e2.f537Dx) {
            result = AddOutPt(e1, pt);
            e2.OutIdx = e1.OutIdx;
            e1.Side = EdgeSide.esLeft;
            e2.Side = EdgeSide.esRight;
            e = e1;
            if (e.PrevInAEL == e2) {
                prevE = e2.PrevInAEL;
            } else {
                prevE = e.PrevInAEL;
            }
        } else {
            result = AddOutPt(e2, pt);
            e1.OutIdx = e2.OutIdx;
            e1.Side = EdgeSide.esRight;
            e2.Side = EdgeSide.esLeft;
            e = e2;
            if (e.PrevInAEL == e1) {
                prevE = e1.PrevInAEL;
            } else {
                prevE = e.PrevInAEL;
            }
        }
        if (prevE != null && prevE.OutIdx >= 0 && TopX(prevE, pt.f513Y) == TopX(e, pt.f513Y) && SlopesEqual(e, prevE, this.m_UseFullRange) && e.WindDelta != 0 && prevE.WindDelta != 0) {
            AddJoin(result, AddOutPt(prevE, pt), e.Top);
        }
        return result;
    }

    private OutRec CreateOutRec() {
        OutRec result = new OutRec();
        result.Idx = -1;
        result.IsHole = false;
        result.IsOpen = false;
        result.FirstLeft = null;
        result.Pts = null;
        result.BottomPt = null;
        result.PolyNode = null;
        this.m_PolyOuts.add(result);
        result.Idx = this.m_PolyOuts.size() - 1;
        return result;
    }

    private OutPt AddOutPt(TEdge e, IntPoint pt) {
        boolean ToFront;
        boolean z = true;
        if (e.Side == EdgeSide.esLeft) {
            ToFront = true;
        } else {
            ToFront = false;
        }
        if (e.OutIdx < 0) {
            OutRec outRec = CreateOutRec();
            if (e.WindDelta != 0) {
                z = false;
            }
            outRec.IsOpen = z;
            OutPt newOp = new OutPt();
            outRec.Pts = newOp;
            newOp.Idx = outRec.Idx;
            newOp.f535Pt = pt;
            newOp.Next = newOp;
            newOp.Prev = newOp;
            if (!outRec.IsOpen) {
                SetHoleState(e, outRec);
            }
            e.OutIdx = outRec.Idx;
            return newOp;
        }
        OutRec outRec2 = this.m_PolyOuts.get(e.OutIdx);
        OutPt op = outRec2.Pts;
        if (ToFront && pt == op.f535Pt) {
            return op;
        }
        if (!ToFront && pt == op.Prev.f535Pt) {
            return op.Prev;
        }
        OutPt newOp2 = new OutPt();
        newOp2.Idx = outRec2.Idx;
        newOp2.f535Pt = pt;
        newOp2.Next = op;
        newOp2.Prev = op.Prev;
        newOp2.Prev.Next = newOp2;
        op.Prev = newOp2;
        if (!ToFront) {
            return newOp2;
        }
        outRec2.Pts = newOp2;
        return newOp2;
    }

    /* access modifiers changed from: package-private */
    public void SwapPoints(IntPoint pt1, IntPoint pt2) {
        new IntPoint(pt1);
    }

    private boolean HorzSegmentsOverlap(int seg1a, int seg1b, int seg2a, int seg2b) {
        if (seg1a > seg1b) {
            seg1a = seg1b;
            seg1b = seg1a;
        }
        if (seg2a > seg2b) {
            seg2a = seg2b;
            seg2b = seg2a;
        }
        return seg1a < seg2b && seg2a < seg1b;
    }

    private void SetHoleState(TEdge e, OutRec outRec) {
        boolean isHole = false;
        for (TEdge e2 = e.PrevInAEL; e2 != null; e2 = e2.PrevInAEL) {
            if (e2.OutIdx >= 0 && e2.WindDelta != 0) {
                isHole = !isHole;
                if (outRec.FirstLeft == null) {
                    outRec.FirstLeft = this.m_PolyOuts.get(e2.OutIdx);
                }
            }
        }
        if (isHole) {
            outRec.IsHole = true;
        }
    }

    private double GetDx(IntPoint pt1, IntPoint pt2) {
        if (pt1.f513Y == pt2.f513Y) {
            return -3.4E38d;
        }
        return ((double) (pt2.f512X - pt1.f512X)) / ((double) (pt2.f513Y - pt1.f513Y));
    }

    private boolean FirstIsBottomPt(OutPt btmPt1, OutPt btmPt2) {
        OutPt p = btmPt1.Prev;
        while (p.f535Pt == btmPt1.f535Pt && p != btmPt1) {
            p = p.Prev;
        }
        double dx1p = Math.abs(GetDx(btmPt1.f535Pt, p.f535Pt));
        OutPt p2 = btmPt1.Next;
        while (p2.f535Pt == btmPt1.f535Pt && p2 != btmPt1) {
            p2 = p2.Next;
        }
        double dx1n = Math.abs(GetDx(btmPt1.f535Pt, p2.f535Pt));
        OutPt p3 = btmPt2.Prev;
        while (p3.f535Pt == btmPt2.f535Pt && p3 != btmPt2) {
            p3 = p3.Prev;
        }
        double dx2p = Math.abs(GetDx(btmPt2.f535Pt, p3.f535Pt));
        OutPt p4 = btmPt2.Next;
        while (p4.f535Pt == btmPt2.f535Pt && p4 != btmPt2) {
            p4 = p4.Next;
        }
        double dx2n = Math.abs(GetDx(btmPt2.f535Pt, p4.f535Pt));
        if ((dx1p < dx2p || dx1p < dx2n) && (dx1n < dx2p || dx1n < dx2n)) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0048, code lost:
        if (FirstIsBottomPt(r1, r0) != false) goto L_0x004b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x004a, code lost:
        r5 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x004b, code lost:
        r0 = r0.Next;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0051, code lost:
        if (r0.f535Pt == r5.f535Pt) goto L_0x0007;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0053, code lost:
        r0 = r0.Next;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0005, code lost:
        if (r0 != null) goto L_0x0007;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0007, code lost:
        if (r0 != r1) goto L_0x0044;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0009, code lost:
        return r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private  com.xzy.forestSystem.gogisapi.SpatialAnalyst.OutPt GetBottomPt( com.xzy.forestSystem.gogisapi.SpatialAnalyst.OutPt r5) {
        /*
            r4 = this;
            r0 = 0
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.OutPt r1 = r5.Next
        L_0x0003:
            if (r1 != r5) goto L_0x000a
            if (r0 == 0) goto L_0x0009
        L_0x0007:
            if (r0 != r1) goto L_0x0044
        L_0x0009:
            return r5
        L_0x000a:
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.IntPoint r2 = r1.f535Pt
            int r2 = r2.f513Y
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.IntPoint r3 = r5.f535Pt
            int r3 = r3.f513Y
            if (r2 <= r3) goto L_0x0019
            r5 = r1
            r0 = 0
        L_0x0016:
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.OutPt r1 = r1.Next
            goto L_0x0003
        L_0x0019:
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.IntPoint r2 = r1.f535Pt
            int r2 = r2.f513Y
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.IntPoint r3 = r5.f535Pt
            int r3 = r3.f513Y
            if (r2 != r3) goto L_0x0016
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.IntPoint r2 = r1.f535Pt
            int r2 = r2.f512X
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.IntPoint r3 = r5.f535Pt
            int r3 = r3.f512X
            if (r2 > r3) goto L_0x0016
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.IntPoint r2 = r1.f535Pt
            int r2 = r2.f512X
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.IntPoint r3 = r5.f535Pt
            int r3 = r3.f512X
            if (r2 >= r3) goto L_0x003a
            r0 = 0
            r5 = r1
            goto L_0x0016
        L_0x003a:
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.OutPt r2 = r1.Next
            if (r2 == r5) goto L_0x0016
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.OutPt r2 = r1.Prev
            if (r2 == r5) goto L_0x0016
            r0 = r1
            goto L_0x0016
        L_0x0044:
            boolean r2 = r4.FirstIsBottomPt(r1, r0)
            if (r2 != 0) goto L_0x004b
            r5 = r0
        L_0x004b:
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.OutPt r0 = r0.Next
        L_0x004d:
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.IntPoint r2 = r0.f535Pt
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.IntPoint r3 = r5.f535Pt
            if (r2 == r3) goto L_0x0007
             com.xzy.forestSystem.gogisapi.SpatialAnalyst.OutPt r0 = r0.Next
            goto L_0x004d
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.gogisapi.SpatialAnalyst.Clipper.GetBottomPt( com.xzy.forestSystem.gogisapi.SpatialAnalyst.OutPt): com.xzy.forestSystem.gogisapi.SpatialAnalyst.OutPt");
    }

    private OutRec GetLowermostRec(OutRec outRec1, OutRec outRec2) {
        if (outRec1.BottomPt == null) {
            outRec1.BottomPt = GetBottomPt(outRec1.Pts);
        }
        if (outRec2.BottomPt == null) {
            outRec2.BottomPt = GetBottomPt(outRec2.Pts);
        }
        OutPt bPt1 = outRec1.BottomPt;
        OutPt bPt2 = outRec2.BottomPt;
        if (bPt1.f535Pt.f513Y > bPt2.f535Pt.f513Y) {
            return outRec1;
        }
        if (bPt1.f535Pt.f513Y < bPt2.f535Pt.f513Y) {
            return outRec2;
        }
        if (bPt1.f535Pt.f512X < bPt2.f535Pt.f512X) {
            return outRec1;
        }
        if (bPt1.f535Pt.f512X > bPt2.f535Pt.f512X) {
            return outRec2;
        }
        if (bPt1.Next == bPt1) {
            return outRec2;
        }
        return (bPt2.Next == bPt2 || FirstIsBottomPt(bPt1, bPt2)) ? outRec1 : outRec2;
    }

    /* access modifiers changed from: package-private */
    public boolean Param1RightOfParam2(OutRec outRec1, OutRec outRec2) {
        do {
            outRec1 = outRec1.FirstLeft;
            if (outRec1 == outRec2) {
                return true;
            }
        } while (outRec1 != null);
        return false;
    }

    private OutRec GetOutRec(int idx) {
        OutRec outrec = this.m_PolyOuts.get(idx);
        while (true) {
            OutRec outrec2 = outrec;
            if (outrec2 == this.m_PolyOuts.get(outrec2.Idx)) {
                return outrec2;
            }
            outrec = this.m_PolyOuts.get(outrec2.Idx);
        }
    }

    private void AppendPolygon(TEdge e1, TEdge e2) {
        OutRec holeStateRec;
        EdgeSide side;
        OutRec outRec1 = this.m_PolyOuts.get(e1.OutIdx);
        OutRec outRec2 = this.m_PolyOuts.get(e2.OutIdx);
        if (Param1RightOfParam2(outRec1, outRec2)) {
            holeStateRec = outRec2;
        } else if (Param1RightOfParam2(outRec2, outRec1)) {
            holeStateRec = outRec1;
        } else {
            holeStateRec = GetLowermostRec(outRec1, outRec2);
        }
        OutPt p1_lft = outRec1.Pts;
        OutPt p1_rt = p1_lft.Prev;
        OutPt p2_lft = outRec2.Pts;
        OutPt p2_rt = p2_lft.Prev;
        if (e1.Side == EdgeSide.esLeft) {
            if (e2.Side == EdgeSide.esLeft) {
                ReversePolyPtLinks(p2_lft);
                p2_lft.Next = p1_lft;
                p1_lft.Prev = p2_lft;
                p1_rt.Next = p2_rt;
                p2_rt.Prev = p1_rt;
                outRec1.Pts = p2_rt;
            } else {
                p2_rt.Next = p1_lft;
                p1_lft.Prev = p2_rt;
                p2_lft.Prev = p1_rt;
                p1_rt.Next = p2_lft;
                outRec1.Pts = p2_lft;
            }
            side = EdgeSide.esLeft;
        } else {
            if (e2.Side == EdgeSide.esRight) {
                ReversePolyPtLinks(p2_lft);
                p1_rt.Next = p2_rt;
                p2_rt.Prev = p1_rt;
                p2_lft.Next = p1_lft;
                p1_lft.Prev = p2_lft;
            } else {
                p1_rt.Next = p2_lft;
                p2_lft.Prev = p1_rt;
                p1_lft.Prev = p2_rt;
                p2_rt.Next = p1_lft;
            }
            side = EdgeSide.esRight;
        }
        outRec1.BottomPt = null;
        if (holeStateRec == outRec2) {
            if (outRec2.FirstLeft != outRec1) {
                outRec1.FirstLeft = outRec2.FirstLeft;
            }
            outRec1.IsHole = outRec2.IsHole;
        }
        outRec2.Pts = null;
        outRec2.BottomPt = null;
        outRec2.FirstLeft = outRec1;
        int OKIdx = e1.OutIdx;
        int ObsoleteIdx = e2.OutIdx;
        e1.OutIdx = -1;
        e2.OutIdx = -1;
        TEdge e = this.m_ActiveEdges;
        while (true) {
            if (e == null) {
                break;
            } else if (e.OutIdx == ObsoleteIdx) {
                e.OutIdx = OKIdx;
                e.Side = side;
                break;
            } else {
                e = e.NextInAEL;
            }
        }
        outRec2.Idx = outRec1.Idx;
    }

    private void ReversePolyPtLinks(OutPt pp) {
        if (pp != null) {
            OutPt pp1 = pp;
            do {
                OutPt pp2 = pp1.Next;
                pp1.Next = pp1.Prev;
                pp1.Prev = pp2;
                pp1 = pp2;
            } while (pp1 != pp);
        }
    }

    private static void SwapSides(TEdge edge1, TEdge edge2) {
        EdgeSide side = edge1.Side;
        edge1.Side = edge2.Side;
        edge2.Side = side;
    }

    private static void SwapPolyIndexes(TEdge edge1, TEdge edge2) {
        int outIdx = edge1.OutIdx;
        edge1.OutIdx = edge2.OutIdx;
        edge2.OutIdx = outIdx;
    }

    private void IntersectEdges(TEdge e1, TEdge e2, IntPoint pt) {
        PolyFillType e1FillType;
        PolyFillType e1FillType2;
        PolyFillType e2FillType;
        PolyFillType e2FillType2;
        int e1Wc;
        int e2Wc;
        int e1Wc2;
        int e2Wc2;
        boolean e1Contributing = e1.OutIdx >= 0;
        boolean e2Contributing = e2.OutIdx >= 0;
        if (e1.WindDelta != 0 && e2.WindDelta != 0) {
            if (e1.PolyTyp != e2.PolyTyp) {
                if (!IsEvenOddFillType(e2)) {
                    e1.WindCnt2 += e2.WindDelta;
                } else {
                    e1.WindCnt2 = e1.WindCnt2 == 0 ? 1 : 0;
                }
                if (!IsEvenOddFillType(e1)) {
                    e2.WindCnt2 -= e1.WindDelta;
                } else {
                    e2.WindCnt2 = e2.WindCnt2 == 0 ? 1 : 0;
                }
            } else if (IsEvenOddFillType(e1)) {
                int oldE1WindCnt = e1.WindCnt;
                e1.WindCnt = e2.WindCnt;
                e2.WindCnt = oldE1WindCnt;
            } else {
                if (e1.WindCnt + e2.WindDelta == 0) {
                    e1.WindCnt = -e1.WindCnt;
                } else {
                    e1.WindCnt += e2.WindDelta;
                }
                if (e2.WindCnt - e1.WindDelta == 0) {
                    e2.WindCnt = -e2.WindCnt;
                } else {
                    e2.WindCnt -= e1.WindDelta;
                }
            }
            if (e1.PolyTyp == PolyType.ptSubject) {
                e1FillType = this.m_SubjFillType;
                e1FillType2 = this.m_ClipFillType;
            } else {
                e1FillType = this.m_ClipFillType;
                e1FillType2 = this.m_SubjFillType;
            }
            if (e2.PolyTyp == PolyType.ptSubject) {
                e2FillType = this.m_SubjFillType;
                e2FillType2 = this.m_ClipFillType;
            } else {
                e2FillType = this.m_ClipFillType;
                e2FillType2 = this.m_SubjFillType;
            }
            switch ($SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$PolyFillType()[e1FillType.ordinal()]) {
                case 3:
                    e1Wc = e1.WindCnt;
                    break;
                case 4:
                    e1Wc = -e1.WindCnt;
                    break;
                default:
                    e1Wc = Math.abs(e1.WindCnt);
                    break;
            }
            switch ($SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$PolyFillType()[e2FillType.ordinal()]) {
                case 3:
                    e2Wc = e2.WindCnt;
                    break;
                case 4:
                    e2Wc = -e2.WindCnt;
                    break;
                default:
                    e2Wc = Math.abs(e2.WindCnt);
                    break;
            }
            if (!e1Contributing || !e2Contributing) {
                if (e1Contributing) {
                    if (e2Wc == 0 || e2Wc == 1) {
                        AddOutPt(e1, pt);
                        SwapSides(e1, e2);
                        SwapPolyIndexes(e1, e2);
                    }
                } else if (e2Contributing) {
                    if (e1Wc == 0 || e1Wc == 1) {
                        AddOutPt(e2, pt);
                        SwapSides(e1, e2);
                        SwapPolyIndexes(e1, e2);
                    }
                } else if (e1Wc != 0 && e1Wc != 1) {
                } else {
                    if (e2Wc == 0 || e2Wc == 1) {
                        switch ($SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$PolyFillType()[e1FillType2.ordinal()]) {
                            case 3:
                                e1Wc2 = e1.WindCnt2;
                                break;
                            case 4:
                                e1Wc2 = -e1.WindCnt2;
                                break;
                            default:
                                e1Wc2 = Math.abs(e1.WindCnt2);
                                break;
                        }
                        switch ($SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$PolyFillType()[e2FillType2.ordinal()]) {
                            case 3:
                                e2Wc2 = e2.WindCnt2;
                                break;
                            case 4:
                                e2Wc2 = -e2.WindCnt2;
                                break;
                            default:
                                e2Wc2 = Math.abs(e2.WindCnt2);
                                break;
                        }
                        if (e1.PolyTyp != e2.PolyTyp) {
                            AddLocalMinPoly(e1, e2, pt);
                        } else if (e1Wc == 1 && e2Wc == 1) {
                            switch ($SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$ClipType()[this.m_ClipType.ordinal()]) {
                                case 1:
                                    if (e1Wc2 > 0 && e2Wc2 > 0) {
                                        AddLocalMinPoly(e1, e2, pt);
                                        return;
                                    }
                                    return;
                                case 2:
                                    if (e1Wc2 <= 0 && e2Wc2 <= 0) {
                                        AddLocalMinPoly(e1, e2, pt);
                                        return;
                                    }
                                    return;
                                case 3:
                                    if ((e1.PolyTyp == PolyType.ptClip && e1Wc2 > 0 && e2Wc2 > 0) || (e1.PolyTyp == PolyType.ptSubject && e1Wc2 <= 0 && e2Wc2 <= 0)) {
                                        AddLocalMinPoly(e1, e2, pt);
                                        return;
                                    }
                                    return;
                                case 4:
                                    AddLocalMinPoly(e1, e2, pt);
                                    return;
                                default:
                                    return;
                            }
                        } else {
                            SwapSides(e1, e2);
                        }
                    }
                }
            } else if ((e1Wc == 0 || e1Wc == 1) && ((e2Wc == 0 || e2Wc == 1) && (e1.PolyTyp == e2.PolyTyp || this.m_ClipType == ClipType.ctXor))) {
                AddOutPt(e1, pt);
                AddOutPt(e2, pt);
                SwapSides(e1, e2);
                SwapPolyIndexes(e1, e2);
            } else {
                AddLocalMaxPoly(e1, e2, pt);
            }
        } else if (e1.WindDelta != 0 || e2.WindDelta != 0) {
            if (e1.PolyTyp == e2.PolyTyp && e1.WindDelta != e2.WindDelta && this.m_ClipType == ClipType.ctUnion) {
                if (e1.WindDelta == 0) {
                    if (e2Contributing) {
                        AddOutPt(e1, pt);
                        if (e1Contributing) {
                            e1.OutIdx = -1;
                        }
                    }
                } else if (e1Contributing) {
                    AddOutPt(e2, pt);
                    if (e2Contributing) {
                        e2.OutIdx = -1;
                    }
                }
            } else if (e1.PolyTyp == e2.PolyTyp) {
            } else {
                if (e1.WindDelta == 0 && Math.abs(e2.WindCnt) == 1 && (this.m_ClipType != ClipType.ctUnion || e2.WindCnt2 == 0)) {
                    AddOutPt(e1, pt);
                    if (e1Contributing) {
                        e1.OutIdx = -1;
                    }
                } else if (e2.WindDelta != 0 || Math.abs(e1.WindCnt) != 1) {
                } else {
                    if (this.m_ClipType != ClipType.ctUnion || e1.WindCnt2 == 0) {
                        AddOutPt(e2, pt);
                        if (e2Contributing) {
                            e2.OutIdx = -1;
                        }
                    }
                }
            }
        }
    }

    private void DeleteFromAEL(TEdge e) {
        TEdge AelPrev = e.PrevInAEL;
        TEdge AelNext = e.NextInAEL;
        if (AelPrev != null || AelNext != null || e == this.m_ActiveEdges) {
            if (AelPrev != null) {
                AelPrev.NextInAEL = AelNext;
            } else {
                this.m_ActiveEdges = AelNext;
            }
            if (AelNext != null) {
                AelNext.PrevInAEL = AelPrev;
            }
            e.NextInAEL = null;
            e.PrevInAEL = null;
        }
    }

    private void DeleteFromSEL(TEdge e) {
        TEdge SelPrev = e.PrevInSEL;
        TEdge SelNext = e.NextInSEL;
        if (SelPrev != null || SelNext != null || e == this.m_SortedEdges) {
            if (SelPrev != null) {
                SelPrev.NextInSEL = SelNext;
            } else {
                this.m_SortedEdges = SelNext;
            }
            if (SelNext != null) {
                SelNext.PrevInSEL = SelPrev;
            }
            e.NextInSEL = null;
            e.PrevInSEL = null;
        }
    }

    private void UpdateEdgeIntoAEL(TEdge e) {
        TEdge AelPrev = e.PrevInAEL;
        TEdge AelNext = e.NextInAEL;
        e.NextInLML.OutIdx = e.OutIdx;
        if (AelPrev != null) {
            AelPrev.NextInAEL = e.NextInLML;
        } else {
            this.m_ActiveEdges = e.NextInLML;
        }
        if (AelNext != null) {
            AelNext.PrevInAEL = e.NextInLML;
        }
        e.NextInLML.Side = e.Side;
        e.NextInLML.WindDelta = e.WindDelta;
        e.NextInLML.WindCnt = e.WindCnt;
        e.NextInLML.WindCnt2 = e.WindCnt2;
        TEdge e2 = e.NextInLML;
        e2.Curr = e2.Bot;
        e2.PrevInAEL = AelPrev;
        e2.NextInAEL = AelNext;
        if (!IsHorizontal(e2)) {
            InsertScanbeam(e2.Top.f513Y);
        }
    }

    private void ProcessHorizontals(boolean isTopOfScanbeam) {
        TEdge horzEdge = this.m_SortedEdges;
        while (horzEdge != null) {
            DeleteFromSEL(horzEdge);
            ProcessHorizontal(horzEdge, isTopOfScanbeam);
            horzEdge = this.m_SortedEdges;
        }
    }

    /* access modifiers changed from: package-private */
    public void GetHorzDirection(TEdge HorzEdge, Direction Dir, int[] LeftRight) {
        if (HorzEdge.Bot.f512X < HorzEdge.Top.f512X) {
            LeftRight[0] = HorzEdge.Bot.f512X;
            LeftRight[1] = HorzEdge.Top.f512X;
            Direction Dir2 = Direction.dLeftToRight;
            return;
        }
        LeftRight[0] = HorzEdge.Top.f512X;
        LeftRight[1] = HorzEdge.Bot.f512X;
        Direction Dir3 = Direction.dRightToLeft;
    }

    private void ProcessHorizontal(TEdge horzEdge, boolean isTopOfScanbeam) {
        Direction dir = Direction.dLeftToRight;
        int[] horzLeftRight = new int[2];
        GetHorzDirection(horzEdge, dir, horzLeftRight);
        TEdge eLastHorz = horzEdge;
        TEdge eMaxPair = null;
        while (eLastHorz.NextInLML != null && IsHorizontal(eLastHorz.NextInLML)) {
            eLastHorz = eLastHorz.NextInLML;
        }
        if (eLastHorz.NextInLML == null) {
            eMaxPair = GetMaximaPair(eLastHorz);
        }
        while (true) {
            boolean IsLastHorz = horzEdge == eLastHorz;
            TEdge e = GetNextInAEL(horzEdge, dir);
            while (e != null && (e.Curr.f512X != horzEdge.Top.f512X || horzEdge.NextInLML == null || e.f537Dx >= horzEdge.NextInLML.f537Dx)) {
                TEdge eNext = GetNextInAEL(e, dir);
                if ((dir != Direction.dLeftToRight || e.Curr.f512X > horzLeftRight[1]) && (dir != Direction.dRightToLeft || e.Curr.f512X < horzLeftRight[0])) {
                    if (dir == Direction.dLeftToRight) {
                        if (e.Curr.f512X >= horzLeftRight[0]) {
                            break;
                        }
                    }
                    if (dir == Direction.dRightToLeft && e.Curr.f512X <= horzLeftRight[0]) {
                        break;
                    }
                } else if (e != eMaxPair || !IsLastHorz) {
                    if (dir == Direction.dLeftToRight) {
                        IntersectEdges(horzEdge, e, new IntPoint(e.Curr.f512X, horzEdge.Curr.f513Y));
                    } else {
                        IntersectEdges(e, horzEdge, new IntPoint(e.Curr.f512X, horzEdge.Curr.f513Y));
                    }
                    SwapPositionsInAEL(horzEdge, e);
                } else {
                    if (horzEdge.OutIdx >= 0) {
                        OutPt op1 = AddOutPt(horzEdge, horzEdge.Top);
                        for (TEdge eNextHorz = this.m_SortedEdges; eNextHorz != null; eNextHorz = eNextHorz.NextInSEL) {
                            if (eNextHorz.OutIdx >= 0 && HorzSegmentsOverlap(horzEdge.Bot.f512X, horzEdge.Top.f512X, eNextHorz.Bot.f512X, eNextHorz.Top.f512X)) {
                                AddJoin(AddOutPt(eNextHorz, eNextHorz.Bot), op1, eNextHorz.Top);
                            }
                        }
                        AddGhostJoin(op1, horzEdge.Bot);
                        AddLocalMaxPoly(horzEdge, eMaxPair, horzEdge.Top);
                    }
                    DeleteFromAEL(horzEdge);
                    DeleteFromAEL(eMaxPair);
                    return;
                }
                e = eNext;
            }
            if (horzEdge.NextInLML == null || !IsHorizontal(horzEdge.NextInLML)) {
                break;
            }
            UpdateEdgeIntoAEL(horzEdge);
            if (horzEdge.OutIdx >= 0) {
                AddOutPt(horzEdge, horzEdge.Bot);
            }
            GetHorzDirection(horzEdge, dir, horzLeftRight);
        }
        if (horzEdge.NextInLML == null) {
            if (horzEdge.OutIdx >= 0) {
                AddOutPt(horzEdge, horzEdge.Top);
            }
            DeleteFromAEL(horzEdge);
        } else if (horzEdge.OutIdx >= 0) {
            OutPt op12 = AddOutPt(horzEdge, horzEdge.Top);
            if (isTopOfScanbeam) {
                AddGhostJoin(op12, horzEdge.Bot);
            }
            UpdateEdgeIntoAEL(horzEdge);
            if (horzEdge.WindDelta != 0) {
                TEdge ePrev = horzEdge.PrevInAEL;
                TEdge eNext2 = horzEdge.NextInAEL;
                if (ePrev != null && ePrev.Curr.f512X == horzEdge.Bot.f512X && ePrev.Curr.f513Y == horzEdge.Bot.f513Y && ePrev.WindDelta != 0 && ePrev.OutIdx >= 0 && ePrev.Curr.f513Y > ePrev.Top.f513Y && SlopesEqual(horzEdge, ePrev, this.m_UseFullRange)) {
                    AddJoin(op12, AddOutPt(ePrev, horzEdge.Bot), horzEdge.Top);
                } else if (eNext2 != null && eNext2.Curr.f512X == horzEdge.Bot.f512X && eNext2.Curr.f513Y == horzEdge.Bot.f513Y && eNext2.WindDelta != 0 && eNext2.OutIdx >= 0 && eNext2.Curr.f513Y > eNext2.Top.f513Y && SlopesEqual(horzEdge, eNext2, this.m_UseFullRange)) {
                    AddJoin(op12, AddOutPt(eNext2, horzEdge.Bot), horzEdge.Top);
                }
            }
        } else {
            UpdateEdgeIntoAEL(horzEdge);
        }
    }

    private TEdge GetNextInAEL(TEdge e, Direction Direction) {
        return Direction == Direction.dLeftToRight ? e.NextInAEL : e.PrevInAEL;
    }

    private boolean IsMinima(TEdge e) {
        return (e == null || e.Prev.NextInLML == e || e.Next.NextInLML == e) ? false : true;
    }

    private boolean IsMaxima(TEdge e, double Y) {
        return e != null && ((double) e.Top.f513Y) == Y && e.NextInLML == null;
    }

    private boolean IsIntermediate(TEdge e, double Y) {
        return ((double) e.Top.f513Y) == Y && e.NextInLML != null;
    }

    private TEdge GetMaximaPair(TEdge e) {
        TEdge result = null;
        if (e.Next.Top == e.Top && e.Next.NextInLML == null) {
            result = e.Next;
        } else if (e.Prev.Top == e.Top && e.Prev.NextInLML == null) {
            result = e.Prev;
        }
        if (result == null) {
            return result;
        }
        if (result.OutIdx == -2 || (result.NextInAEL == result.PrevInAEL && !IsHorizontal(result))) {
            return null;
        }
        return result;
    }

    private boolean ProcessIntersections(int topY) {
        if (this.m_ActiveEdges == null) {
            return true;
        }
        try {
            BuildIntersectList(topY);
            if (this.m_IntersectList.size() == 0) {
                return true;
            }
            if (this.m_IntersectList.size() != 1 && !FixupIntersectionOrder()) {
                return false;
            }
            ProcessIntersectList();
            this.m_SortedEdges = null;
            return true;
        } catch (Exception e) {
            this.m_SortedEdges = null;
            this.m_IntersectList.clear();
        }
        return false;
    }

    private void BuildIntersectList(int topY) {
        if (this.m_ActiveEdges != null) {
            TEdge e = this.m_ActiveEdges;
            this.m_SortedEdges = e;
            while (e != null) {
                e.PrevInSEL = e.PrevInAEL;
                e.NextInSEL = e.NextInAEL;
                e.Curr.f512X = TopX(e, topY);
                e = e.NextInAEL;
            }
            boolean isModified = true;
            while (isModified && this.m_SortedEdges != null) {
                isModified = false;
                TEdge e2 = this.m_SortedEdges;
                while (e2.NextInSEL != null) {
                    TEdge eNext = e2.NextInSEL;
                    if (e2.Curr.f512X > eNext.Curr.f512X) {
                        IntPoint pt = new IntPoint();
                        IntersectPoint(e2, eNext, pt);
                        IntersectNode newNode = new IntersectNode();
                        newNode.Edge1 = e2;
                        newNode.Edge2 = eNext;
                        newNode.f514Pt = pt;
                        this.m_IntersectList.add(newNode);
                        SwapPositionsInSEL(e2, eNext);
                        isModified = true;
                    } else {
                        e2 = eNext;
                    }
                }
                if (e2.PrevInSEL == null) {
                    break;
                }
                e2.PrevInSEL.NextInSEL = null;
            }
            this.m_SortedEdges = null;
        }
    }

    private boolean EdgesAdjacent(IntersectNode inode) {
        return inode.Edge1.NextInSEL == inode.Edge2 || inode.Edge1.PrevInSEL == inode.Edge2;
    }

    private static int IntersectNodeSort(IntersectNode node1, IntersectNode node2) {
        return node2.f514Pt.f513Y - node1.f514Pt.f513Y;
    }

    private boolean FixupIntersectionOrder() {
        Collections.sort(this.m_IntersectList, this.m_IntersectNodeComparer);
        CopyAELToSEL();
        int cnt = this.m_IntersectList.size();
        for (int i = 0; i < cnt; i++) {
            if (!EdgesAdjacent(this.m_IntersectList.get(i))) {
                int j = i + 1;
                while (j < cnt && !EdgesAdjacent(this.m_IntersectList.get(j))) {
                    j++;
                }
                if (j == cnt) {
                    return false;
                }
                this.m_IntersectList.add(i, this.m_IntersectList.get(j));
                this.m_IntersectList.add(j, this.m_IntersectList.get(i));
            }
            SwapPositionsInSEL(this.m_IntersectList.get(i).Edge1, this.m_IntersectList.get(i).Edge2);
        }
        return true;
    }

    private void ProcessIntersectList() {
        for (int i = 0; i < this.m_IntersectList.size(); i++) {
            IntersectNode iNode = this.m_IntersectList.get(i);
            IntersectEdges(iNode.Edge1, iNode.Edge2, iNode.f514Pt);
            SwapPositionsInAEL(iNode.Edge1, iNode.Edge2);
        }
        this.m_IntersectList.clear();
    }

    static int Round(double value) {
        return value < 0.0d ? (int) (value - 0.5d) : (int) (value + 0.5d);
    }

    private static int TopX(TEdge edge, int currentY) {
        if (currentY == edge.Top.f513Y) {
            return edge.Top.f512X;
        }
        return edge.Bot.f512X + Round(edge.f537Dx * ((double) (currentY - edge.Bot.f513Y)));
    }

    private void IntersectPoint(TEdge edge1, TEdge edge2, IntPoint ip) {
        if (edge1.f537Dx == edge2.f537Dx) {
            ip.f513Y = edge1.Curr.f513Y;
            ip.f512X = TopX(edge1, ip.f513Y);
            return;
        }
        if (edge1.Delta.f512X == 0) {
            ip.f512X = edge1.Bot.f512X;
            if (IsHorizontal(edge2)) {
                ip.f513Y = edge2.Bot.f513Y;
            } else {
                ip.f513Y = Round((((double) ip.f512X) / edge2.f537Dx) + (((double) edge2.Bot.f513Y) - (((double) edge2.Bot.f512X) / edge2.f537Dx)));
            }
        } else if (edge2.Delta.f512X == 0) {
            ip.f512X = edge2.Bot.f512X;
            if (IsHorizontal(edge1)) {
                ip.f513Y = edge1.Bot.f513Y;
            } else {
                ip.f513Y = Round((((double) ip.f512X) / edge1.f537Dx) + (((double) edge1.Bot.f513Y) - (((double) edge1.Bot.f512X) / edge1.f537Dx)));
            }
        } else {
            double b1 = ((double) edge1.Bot.f512X) - (((double) edge1.Bot.f513Y) * edge1.f537Dx);
            double b2 = ((double) edge2.Bot.f512X) - (((double) edge2.Bot.f513Y) * edge2.f537Dx);
            double q = (b2 - b1) / (edge1.f537Dx - edge2.f537Dx);
            ip.f513Y = Round(q);
            if (Math.abs(edge1.f537Dx) < Math.abs(edge2.f537Dx)) {
                ip.f512X = Round((edge1.f537Dx * q) + b1);
            } else {
                ip.f512X = Round((edge2.f537Dx * q) + b2);
            }
        }
        if (ip.f513Y < edge1.Top.f513Y || ip.f513Y < edge2.Top.f513Y) {
            if (edge1.Top.f513Y > edge2.Top.f513Y) {
                ip.f513Y = edge1.Top.f513Y;
            } else {
                ip.f513Y = edge2.Top.f513Y;
            }
            if (Math.abs(edge1.f537Dx) < Math.abs(edge2.f537Dx)) {
                ip.f512X = TopX(edge1, ip.f513Y);
            } else {
                ip.f512X = TopX(edge2, ip.f513Y);
            }
        }
        if (ip.f513Y > edge1.Curr.f513Y) {
            ip.f513Y = edge1.Curr.f513Y;
            if (Math.abs(edge1.f537Dx) > Math.abs(edge2.f537Dx)) {
                ip.f512X = TopX(edge2, ip.f513Y);
            } else {
                ip.f512X = TopX(edge1, ip.f513Y);
            }
        }
    }

    private void ProcessEdgesAtTopOfScanbeam(int topY) {
        TEdge e = this.m_ActiveEdges;
        while (e != null) {
            boolean IsMaximaEdge = IsMaxima(e, (double) topY);
            if (IsMaximaEdge) {
                TEdge eMaxPair = GetMaximaPair(e);
                IsMaximaEdge = eMaxPair == null || !IsHorizontal(eMaxPair);
            }
            if (IsMaximaEdge) {
                TEdge ePrev = e.PrevInAEL;
                DoMaxima(e);
                if (ePrev == null) {
                    e = this.m_ActiveEdges;
                } else {
                    e = ePrev.NextInAEL;
                }
            } else {
                if (!IsIntermediate(e, (double) topY) || !IsHorizontal(e.NextInLML)) {
                    e.Curr.f512X = TopX(e, topY);
                    e.Curr.f513Y = topY;
                } else {
                    UpdateEdgeIntoAEL(e);
                    if (e.OutIdx >= 0) {
                        AddOutPt(e, e.Bot);
                    }
                    AddEdgeToSEL(e);
                }
                if (this.StrictlySimple) {
                    TEdge ePrev2 = e.PrevInAEL;
                    if (e.OutIdx >= 0 && e.WindDelta != 0 && ePrev2 != null && ePrev2.OutIdx >= 0 && ePrev2.Curr.f512X == e.Curr.f512X && ePrev2.WindDelta != 0) {
                        IntPoint ip = new IntPoint(e.Curr);
                        AddJoin(AddOutPt(ePrev2, ip), AddOutPt(e, ip), ip);
                    }
                }
                e = e.NextInAEL;
            }
        }
        ProcessHorizontals(true);
        for (TEdge e2 = this.m_ActiveEdges; e2 != null; e2 = e2.NextInAEL) {
            if (IsIntermediate(e2, (double) topY)) {
                OutPt op = null;
                if (e2.OutIdx >= 0) {
                    op = AddOutPt(e2, e2.Top);
                }
                UpdateEdgeIntoAEL(e2);
                TEdge ePrev3 = e2.PrevInAEL;
                TEdge eNext = e2.NextInAEL;
                if (ePrev3 != null && ePrev3.Curr.f512X == e2.Bot.f512X && ePrev3.Curr.f513Y == e2.Bot.f513Y && op != null && ePrev3.OutIdx >= 0 && ePrev3.Curr.f513Y > ePrev3.Top.f513Y && SlopesEqual(e2, ePrev3, this.m_UseFullRange) && e2.WindDelta != 0 && ePrev3.WindDelta != 0) {
                    AddJoin(op, AddOutPt(ePrev3, e2.Bot), e2.Top);
                } else if (eNext != null && eNext.Curr.f512X == e2.Bot.f512X && eNext.Curr.f513Y == e2.Bot.f513Y && op != null && eNext.OutIdx >= 0 && eNext.Curr.f513Y > eNext.Top.f513Y && SlopesEqual(e2, eNext, this.m_UseFullRange) && e2.WindDelta != 0 && eNext.WindDelta != 0) {
                    AddJoin(op, AddOutPt(eNext, e2.Bot), e2.Top);
                }
            }
        }
    }

    private void DoMaxima(TEdge e) {
        TEdge eMaxPair = GetMaximaPair(e);
        if (eMaxPair == null) {
            if (e.OutIdx >= 0) {
                AddOutPt(e, e.Top);
            }
            DeleteFromAEL(e);
            return;
        }
        TEdge eNext = e.NextInAEL;
        while (eNext != null && eNext != eMaxPair) {
            IntersectEdges(e, eNext, e.Top);
            SwapPositionsInAEL(e, eNext);
            eNext = e.NextInAEL;
        }
        if (e.OutIdx == -1 && eMaxPair.OutIdx == -1) {
            DeleteFromAEL(e);
            DeleteFromAEL(eMaxPair);
        } else if (e.OutIdx >= 0 && eMaxPair.OutIdx >= 0) {
            if (e.OutIdx >= 0) {
                AddLocalMaxPoly(e, eMaxPair, e.Top);
            }
            DeleteFromAEL(e);
            DeleteFromAEL(eMaxPair);
        } else if (e.WindDelta == 0) {
            if (e.OutIdx >= 0) {
                AddOutPt(e, e.Top);
                e.OutIdx = -1;
            }
            DeleteFromAEL(e);
            if (eMaxPair.OutIdx >= 0) {
                AddOutPt(eMaxPair, e.Top);
                eMaxPair.OutIdx = -1;
            }
            DeleteFromAEL(eMaxPair);
        }
    }

    public static void ReversePaths(List<List<IntPoint>> polys) {
        for (List<IntPoint> poly : polys) {
            Collections.reverse(poly);
        }
    }

    public static boolean Orientation(List<IntPoint> poly) {
        return Area(poly) >= 0.0d;
    }

    private int PointCount(OutPt pts) {
        if (pts == null) {
            return 0;
        }
        int result = 0;
        OutPt p = pts;
        do {
            result++;
            p = p.Next;
        } while (p != pts);
        return result;
    }

    private void BuildResult(List<List<IntPoint>> polyg) {
        int cnt;
        polyg.clear();
        OutPt p = new OutPt();
        for (int i = 0; i < this.m_PolyOuts.size(); i++) {
            OutRec outRec = this.m_PolyOuts.get(i);
            if (outRec.Pts != null && (cnt = PointCount((p = outRec.Pts.Prev))) >= 2) {
                List<IntPoint> pg = new ArrayList<>();
                for (int j = 0; j < cnt; j++) {
                    pg.add(p.f535Pt);

                }
                polyg.add(pg);
            }
        }
    }

    private void BuildResult2(PolyTree polytree) {
        polytree.Clear();
        for (int i = 0; i < this.m_PolyOuts.size(); i++) {
            OutRec outRec = this.m_PolyOuts.get(i);
            int cnt = PointCount(outRec.Pts);
            if ((!outRec.IsOpen || cnt >= 2) && (outRec.IsOpen || cnt >= 3)) {
                FixHoleLinkage(outRec);
                PolyNode pn = new PolyNode();
                polytree.m_AllPolys.add(pn);
                outRec.PolyNode = pn;
                OutPt op = outRec.Pts.Prev;
                for (int j = 0; j < cnt; j++) {
                    pn.m_polygon.add(op.f535Pt);
                    op = op.Prev;
                }
            }
        }
        for (int i2 = 0; i2 < this.m_PolyOuts.size(); i2++) {
            OutRec outRec2 = this.m_PolyOuts.get(i2);
            if (outRec2.PolyNode != null) {
                if (outRec2.IsOpen) {
                    outRec2.PolyNode.IsOpen = true;
                    polytree.AddChild(outRec2.PolyNode);
                } else if (outRec2.FirstLeft == null || outRec2.FirstLeft.PolyNode == null) {
                    polytree.AddChild(outRec2.PolyNode);
                } else {
                    outRec2.FirstLeft.PolyNode.AddChild(outRec2.PolyNode);
                }
            }
        }
    }

    private void FixupOutPolygon(OutRec outRec) {
        OutPt lastOK = null;
        outRec.BottomPt = null;
        OutPt pp = outRec.Pts;
        while (pp.Prev != pp && pp.Prev != pp.Next) {
            if (pp.f535Pt == pp.Next.f535Pt || pp.f535Pt == pp.Prev.f535Pt || (SlopesEqual(pp.Prev.f535Pt, pp.f535Pt, pp.Next.f535Pt, this.m_UseFullRange) && (!this.PreserveCollinear || !Pt2IsBetweenPt1AndPt3(pp.Prev.f535Pt, pp.f535Pt, pp.Next.f535Pt)))) {
                lastOK = null;
                pp.Prev.Next = pp.Next;
                pp.Next.Prev = pp.Prev;
                pp = pp.Prev;
            } else if (pp == lastOK) {
                outRec.Pts = pp;
                return;
            } else {
                if (lastOK == null) {
                    lastOK = pp;
                }
                pp = pp.Next;
            }
        }
        outRec.Pts = null;
    }

    /* access modifiers changed from: package-private */
    public OutPt DupOutPt(OutPt outPt, boolean InsertAfter) {
        OutPt result = new OutPt();
        result.f535Pt = outPt.f535Pt;
        result.Idx = outPt.Idx;
        if (InsertAfter) {
            result.Next = outPt.Next;
            result.Prev = outPt;
            outPt.Next.Prev = result;
            outPt.Next = result;
        } else {
            result.Prev = outPt.Prev;
            result.Next = outPt;
            outPt.Prev.Next = result;
            outPt.Prev = result;
        }
        return result;
    }

    /* access modifiers changed from: package-private */
    public boolean GetOverlap(int a1, int a2, int b1, int b2, int[] LeftRight) {
        if (a1 < a2) {
            if (b1 < b2) {
                LeftRight[0] = Math.max(a1, b1);
                LeftRight[1] = Math.max(a2, b2);
            } else {
                LeftRight[0] = Math.max(a1, b2);
                LeftRight[1] = Math.max(a2, b1);
            }
        } else if (b1 < b2) {
            LeftRight[0] = Math.max(a2, b1);
            LeftRight[1] = Math.max(a1, b2);
        } else {
            LeftRight[0] = Math.max(a2, b2);
            LeftRight[1] = Math.max(a1, b1);
        }
        return LeftRight[0] < LeftRight[1];
    }

    /* access modifiers changed from: package-private */
    public boolean JoinHorz(OutPt op1, OutPt op1b, OutPt op2, OutPt op2b, IntPoint Pt, boolean DiscardLeft) {
        OutPt op1b2;
        OutPt op2b2;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5 = false;
        Direction Dir1 = op1.f535Pt.f512X > op1b.f535Pt.f512X ? Direction.dRightToLeft : Direction.dLeftToRight;
        Direction Dir2 = op2.f535Pt.f512X > op2b.f535Pt.f512X ? Direction.dRightToLeft : Direction.dLeftToRight;
        if (Dir1 == Dir2) {
            return false;
        }
        if (Dir1 == Direction.dLeftToRight) {
            while (op1.Next.f535Pt.f512X <= Pt.f512X && op1.Next.f535Pt.f512X >= op1.f535Pt.f512X && op1.Next.f535Pt.f513Y == Pt.f513Y) {
                op1 = op1.Next;
            }
            if (DiscardLeft && op1.f535Pt.f512X != Pt.f512X) {
                op1 = op1.Next;
            }
            if (DiscardLeft) {
                z3 = false;
            } else {
                z3 = true;
            }
            op1b2 = DupOutPt(op1, z3);
            if (op1b2.f535Pt != Pt) {
                op1 = op1b2;
                op1.f535Pt = Pt;
                if (DiscardLeft) {
                    z4 = false;
                } else {
                    z4 = true;
                }
                op1b2 = DupOutPt(op1, z4);
            }
        } else {
            while (op1.Next.f535Pt.f512X >= Pt.f512X && op1.Next.f535Pt.f512X <= op1.f535Pt.f512X && op1.Next.f535Pt.f513Y == Pt.f513Y) {
                op1 = op1.Next;
            }
            if (!DiscardLeft && op1.f535Pt.f512X != Pt.f512X) {
                op1 = op1.Next;
            }
            op1b2 = DupOutPt(op1, DiscardLeft);
            if (op1b2.f535Pt != Pt) {
                op1 = op1b2;
                op1.f535Pt = Pt;
                op1b2 = DupOutPt(op1, DiscardLeft);
            }
        }
        if (Dir2 == Direction.dLeftToRight) {
            while (op2.Next.f535Pt.f512X <= Pt.f512X && op2.Next.f535Pt.f512X >= op2.f535Pt.f512X && op2.Next.f535Pt.f513Y == Pt.f513Y) {
                op2 = op2.Next;
            }
            if (DiscardLeft && op2.f535Pt.f512X != Pt.f512X) {
                op2 = op2.Next;
            }
            if (DiscardLeft) {
                z = false;
            } else {
                z = true;
            }
            op2b2 = DupOutPt(op2, z);
            if (op2b2.f535Pt != Pt) {
                op2 = op2b2;
                op2.f535Pt = Pt;
                if (DiscardLeft) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                op2b2 = DupOutPt(op2, z2);
            }
        } else {
            while (op2.Next.f535Pt.f512X >= Pt.f512X && op2.Next.f535Pt.f512X <= op2.f535Pt.f512X && op2.Next.f535Pt.f513Y == Pt.f513Y) {
                op2 = op2.Next;
            }
            if (!DiscardLeft && op2.f535Pt.f512X != Pt.f512X) {
                op2 = op2.Next;
            }
            op2b2 = DupOutPt(op2, DiscardLeft);
            if (op2b2.f535Pt != Pt) {
                op2 = op2b2;
                op2.f535Pt = Pt;
                op2b2 = DupOutPt(op2, DiscardLeft);
            }
        }
        if (Dir1 == Direction.dLeftToRight) {
            z5 = true;
        }
        if (z5 == DiscardLeft) {
            op1.Prev = op2;
            op2.Next = op1;
            op1b2.Next = op2b2;
            op2b2.Prev = op1b2;
        } else {
            op1.Next = op2;
            op2.Prev = op1;
            op1b2.Prev = op2b2;
            op2b2.Next = op1b2;
        }
        return true;
    }

    private boolean JoinPoints(Join j, OutRec outRec1, OutRec outRec2) {
        IntPoint Pt;
        boolean DiscardLeftSide;
        OutPt op1 = j.OutPt1;
        OutPt op2 = j.OutPt2;
        boolean isHorizontal = j.OutPt1.f535Pt.f513Y == j.OffPt.f513Y;
        if (isHorizontal && j.OffPt == j.OutPt1.f535Pt && j.OffPt == j.OutPt2.f535Pt) {
            if (outRec1 != outRec2) {
                return false;
            }
            OutPt op1b = j.OutPt1.Next;
            while (op1b != op1 && op1b.f535Pt == j.OffPt) {
                op1b = op1b.Next;
            }
            boolean reverse1 = op1b.f535Pt.f513Y > j.OffPt.f513Y;
            OutPt op2b = j.OutPt2.Next;
            while (op2b != op2 && op2b.f535Pt == j.OffPt) {
                op2b = op2b.Next;
            }
            if (reverse1 == (op2b.f535Pt.f513Y > j.OffPt.f513Y)) {
                return false;
            }
            if (reverse1) {
                OutPt op1b2 = DupOutPt(op1, false);
                OutPt op2b2 = DupOutPt(op2, true);
                op1.Prev = op2;
                op2.Next = op1;
                op1b2.Next = op2b2;
                op2b2.Prev = op1b2;
                j.OutPt1 = op1;
                j.OutPt2 = op1b2;
                return true;
            }
            OutPt op1b3 = DupOutPt(op1, true);
            OutPt op2b3 = DupOutPt(op2, false);
            op1.Next = op2;
            op2.Prev = op1;
            op1b3.Prev = op2b3;
            op2b3.Next = op1b3;
            j.OutPt1 = op1;
            j.OutPt2 = op1b3;
            return true;
        } else if (isHorizontal) {
            OutPt op1b4 = op1;
            while (op1.Prev.f535Pt.f513Y == op1.f535Pt.f513Y && op1.Prev != op1b4 && op1.Prev != op2) {
                op1 = op1.Prev;
            }
            while (op1b4.Next.f535Pt.f513Y == op1b4.f535Pt.f513Y && op1b4.Next != op1 && op1b4.Next != op2) {
                op1b4 = op1b4.Next;
            }
            if (op1b4.Next == op1 || op1b4.Next == op2) {
                return false;
            }
            OutPt op2b4 = op2;
            while (op2.Prev.f535Pt.f513Y == op2.f535Pt.f513Y && op2.Prev != op2b4 && op2.Prev != op1b4) {
                op2 = op2.Prev;
            }
            while (op2b4.Next.f535Pt.f513Y == op2b4.f535Pt.f513Y && op2b4.Next != op2 && op2b4.Next != op1) {
                op2b4 = op2b4.Next;
            }
            if (op2b4.Next == op2 || op2b4.Next == op1) {
                return false;
            }
            int[] leftRight = new int[2];
            if (!GetOverlap(op1.f535Pt.f512X, op1b4.f535Pt.f512X, op2.f535Pt.f512X, op2b4.f535Pt.f512X, leftRight)) {
                return false;
            }
            if (op1.f535Pt.f512X >= leftRight[0] && op1.f535Pt.f512X <= leftRight[1]) {
                Pt = op1.f535Pt;
                if (op1.f535Pt.f512X > op1b4.f535Pt.f512X) {
                    DiscardLeftSide = true;
                } else {
                    DiscardLeftSide = false;
                }
            } else if (op2.f535Pt.f512X >= leftRight[0] && op2.f535Pt.f512X <= leftRight[1]) {
                Pt = op2.f535Pt;
                DiscardLeftSide = op2.f535Pt.f512X > op2b4.f535Pt.f512X;
            } else if (op1b4.f535Pt.f512X < leftRight[0] || op1b4.f535Pt.f512X > leftRight[1]) {
                Pt = op2b4.f535Pt;
                DiscardLeftSide = op2b4.f535Pt.f512X > op2.f535Pt.f512X;
            } else {
                Pt = op1b4.f535Pt;
                DiscardLeftSide = op1b4.f535Pt.f512X > op1.f535Pt.f512X;
            }
            j.OutPt1 = op1;
            j.OutPt2 = op2;
            return JoinHorz(op1, op1b4, op2, op2b4, Pt, DiscardLeftSide);
        } else {
            OutPt op1b5 = op1.Next;
            while (op1b5.f535Pt == op1.f535Pt && op1b5 != op1) {
                op1b5 = op1b5.Next;
            }
            boolean Reverse1 = op1b5.f535Pt.f513Y > op1.f535Pt.f513Y || !SlopesEqual(op1.f535Pt, op1b5.f535Pt, j.OffPt, this.m_UseFullRange);
            if (Reverse1) {
                op1b5 = op1.Prev;
                while (op1b5.f535Pt == op1.f535Pt && op1b5 != op1) {
                    op1b5 = op1b5.Prev;
                }
                if (op1b5.f535Pt.f513Y > op1.f535Pt.f513Y || !SlopesEqual(op1.f535Pt, op1b5.f535Pt, j.OffPt, this.m_UseFullRange)) {
                    return false;
                }
            }
            OutPt op2b5 = op2.Next;
            while (op2b5.f535Pt == op2.f535Pt && op2b5 != op2) {
                op2b5 = op2b5.Next;
            }
            boolean Reverse2 = op2b5.f535Pt.f513Y > op2.f535Pt.f513Y || !SlopesEqual(op2.f535Pt, op2b5.f535Pt, j.OffPt, this.m_UseFullRange);
            if (Reverse2) {
                op2b5 = op2.Prev;
                while (op2b5.f535Pt == op2.f535Pt && op2b5 != op2) {
                    op2b5 = op2b5.Prev;
                }
                if (op2b5.f535Pt.f513Y > op2.f535Pt.f513Y || !SlopesEqual(op2.f535Pt, op2b5.f535Pt, j.OffPt, this.m_UseFullRange)) {
                    return false;
                }
            }
            if (op1b5 == op1 || op2b5 == op2 || op1b5 == op2b5 || (outRec1 == outRec2 && Reverse1 == Reverse2)) {
                return false;
            }
            if (Reverse1) {
                OutPt op1b6 = DupOutPt(op1, false);
                OutPt op2b6 = DupOutPt(op2, true);
                op1.Prev = op2;
                op2.Next = op1;
                op1b6.Next = op2b6;
                op2b6.Prev = op1b6;
                j.OutPt1 = op1;
                j.OutPt2 = op1b6;
                return true;
            }
            OutPt op1b7 = DupOutPt(op1, true);
            OutPt op2b7 = DupOutPt(op2, false);
            op1.Next = op2;
            op2.Prev = op1;
            op1b7.Prev = op2b7;
            op2b7.Next = op1b7;
            j.OutPt1 = op1;
            j.OutPt2 = op1b7;
            return true;
        }
    }

    public static int PointInPolygon(IntPoint pt, List<IntPoint> path) {
        int result = 0;
        int cnt = path.size();
        if (cnt < 3) {
            return 0;
        }
        IntPoint ip = path.get(0);
        int i = 1;
        while (i <= cnt) {
            IntPoint ipNext = i == cnt ? path.get(0) : path.get(i);
            if (ipNext.f513Y == pt.f513Y) {
                if (ipNext.f512X != pt.f512X) {
                    if (ip.f513Y == pt.f513Y) {
                        if ((ipNext.f512X > pt.f512X) == (ip.f512X < pt.f512X)) {
                        }
                    }
                }
                return -1;
            }
            if ((ip.f513Y < pt.f513Y) != (ipNext.f513Y < pt.f513Y)) {
                if (ip.f512X >= pt.f512X) {
                    if (ipNext.f512X > pt.f512X) {
                        result = 1 - result;
                    } else {
                        double d = (((double) (ip.f512X - pt.f512X)) * ((double) (ipNext.f513Y - pt.f513Y))) - (((double) (ipNext.f512X - pt.f512X)) * ((double) (ip.f513Y - pt.f513Y)));
                        if (d == 0.0d) {
                            return -1;
                        }
                        if ((d > 0.0d) == (ipNext.f513Y > ip.f513Y)) {
                            result = 1 - result;
                        }
                    }
                } else if (ipNext.f512X > pt.f512X) {
                    double d2 = (((double) (ip.f512X - pt.f512X)) * ((double) (ipNext.f513Y - pt.f513Y))) - (((double) (ipNext.f512X - pt.f512X)) * ((double) (ip.f513Y - pt.f513Y)));
                    if (d2 == 0.0d) {
                        return -1;
                    }
                    if ((d2 > 0.0d) == (ipNext.f513Y > ip.f513Y)) {
                        result = 1 - result;
                    }
                } else {
                    continue;
                }
            }
            ip = ipNext;
            i++;
        }
        return result;
    }

    private static int PointInPolygon(IntPoint pt, OutPt op) {
        boolean z;
        boolean z2;
        int result = 0;
        int ptx = pt.f512X;
        int pty = pt.f513Y;
        int poly0x = op.f535Pt.f512X;
        int poly0y = op.f535Pt.f513Y;
        do {
            op = op.Next;
            int poly1x = op.f535Pt.f512X;
            int poly1y = op.f535Pt.f513Y;
            if (poly1y == pty) {
                if (poly1x != ptx) {
                    if (poly0y == pty) {
                        if (poly1x > ptx) {
                            z = true;
                        } else {
                            z = false;
                        }
                        if (poly0x < ptx) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        if (z == z2) {
                        }
                    }
                }
                return -1;
            }
            if ((poly0y < pty) != (poly1y < pty)) {
                if (poly0x >= ptx) {
                    if (poly1x > ptx) {
                        result = 1 - result;
                    } else {
                        double d = (((double) (poly0x - ptx)) * ((double) (poly1y - pty))) - (((double) (poly1x - ptx)) * ((double) (poly0y - pty)));
                        if (d == 0.0d) {
                            return -1;
                        }
                        if ((d > 0.0d) == (poly1y > poly0y)) {
                            result = 1 - result;
                        }
                    }
                } else if (poly1x > ptx) {
                    double d2 = (((double) (poly0x - ptx)) * ((double) (poly1y - pty))) - (((double) (poly1x - ptx)) * ((double) (poly0y - pty)));
                    if (d2 == 0.0d) {
                        return -1;
                    }
                    if ((d2 > 0.0d) == (poly1y > poly0y)) {
                        result = 1 - result;
                    }
                }
            }
            poly0x = poly1x;
            poly0y = poly1y;
        } while (op != op);
        return result;
    }

    private static boolean Poly2ContainsPoly1(OutPt outPt1, OutPt outPt2) {
        OutPt op = outPt1;
        do {
            int res = PointInPolygon(op.f535Pt, outPt2);
            if (res < 0) {
                op = op.Next;
            } else if (res > 0) {
                return true;
            } else {
                return false;
            }
        } while (op != outPt1);
        return true;
    }

    private void FixupFirstLefts1(OutRec OldOutRec, OutRec NewOutRec) {
        for (int i = 0; i < this.m_PolyOuts.size(); i++) {
            OutRec outRec = this.m_PolyOuts.get(i);
            if (outRec.Pts != null && outRec.FirstLeft != null && ParseFirstLeft(outRec.FirstLeft) == OldOutRec && Poly2ContainsPoly1(outRec.Pts, NewOutRec.Pts)) {
                outRec.FirstLeft = NewOutRec;
            }
        }
    }

    private void FixupFirstLefts2(OutRec OldOutRec, OutRec NewOutRec) {
        for (OutRec outRec : this.m_PolyOuts) {
            if (outRec.FirstLeft == OldOutRec) {
                outRec.FirstLeft = NewOutRec;
            }
        }
    }

    private static OutRec ParseFirstLeft(OutRec FirstLeft) {
        while (FirstLeft != null && FirstLeft.Pts == null) {
            FirstLeft = FirstLeft.FirstLeft;
        }
        return FirstLeft;
    }

    private void JoinCommonEdges() {
        OutRec holeStateRec;
        for (int i = 0; i < this.m_Joins.size(); i++) {
            Join join = this.m_Joins.get(i);
            OutRec outRec1 = GetOutRec(join.OutPt1.Idx);
            OutRec outRec2 = GetOutRec(join.OutPt2.Idx);
            if (!(outRec1.Pts == null || outRec2.Pts == null)) {
                if (outRec1 == outRec2) {
                    holeStateRec = outRec1;
                } else if (Param1RightOfParam2(outRec1, outRec2)) {
                    holeStateRec = outRec2;
                } else if (Param1RightOfParam2(outRec2, outRec1)) {
                    holeStateRec = outRec1;
                } else {
                    holeStateRec = GetLowermostRec(outRec1, outRec2);
                }
                if (JoinPoints(join, outRec1, outRec2)) {
                    if (outRec1 == outRec2) {
                        outRec1.Pts = join.OutPt1;
                        outRec1.BottomPt = null;
                        OutRec outRec22 = CreateOutRec();
                        outRec22.Pts = join.OutPt2;
                        UpdateOutPtIdxs(outRec22);
                        if (this.m_UsingPolyTree) {
                            for (int j = 0; j < this.m_PolyOuts.size() - 1; j++) {
                                OutRec oRec = this.m_PolyOuts.get(j);
                                if (oRec.Pts != null && ParseFirstLeft(oRec.FirstLeft) == outRec1 && oRec.IsHole != outRec1.IsHole && Poly2ContainsPoly1(oRec.Pts, join.OutPt2)) {
                                    oRec.FirstLeft = outRec22;
                                }
                            }
                        }
                        if (Poly2ContainsPoly1(outRec22.Pts, outRec1.Pts)) {
                            outRec22.IsHole = !outRec1.IsHole;
                            outRec22.FirstLeft = outRec1;
                            if (this.m_UsingPolyTree) {
                                FixupFirstLefts2(outRec22, outRec1);
                            }
                            if ((this.ReverseSolution ^ outRec22.IsHole) == (Area(outRec22) > 0.0d)) {
                                ReversePolyPtLinks(outRec22.Pts);
                            }
                        } else if (Poly2ContainsPoly1(outRec1.Pts, outRec22.Pts)) {
                            outRec22.IsHole = outRec1.IsHole;
                            outRec1.IsHole = !outRec22.IsHole;
                            outRec22.FirstLeft = outRec1.FirstLeft;
                            outRec1.FirstLeft = outRec22;
                            if (this.m_UsingPolyTree) {
                                FixupFirstLefts2(outRec1, outRec22);
                            }
                            if ((this.ReverseSolution ^ outRec1.IsHole) == (Area(outRec1) > 0.0d)) {
                                ReversePolyPtLinks(outRec1.Pts);
                            }
                        } else {
                            outRec22.IsHole = outRec1.IsHole;
                            outRec22.FirstLeft = outRec1.FirstLeft;
                            if (this.m_UsingPolyTree) {
                                FixupFirstLefts1(outRec1, outRec22);
                            }
                        }
                    } else {
                        outRec2.Pts = null;
                        outRec2.BottomPt = null;
                        outRec2.Idx = outRec1.Idx;
                        outRec1.IsHole = holeStateRec.IsHole;
                        if (holeStateRec == outRec2) {
                            outRec1.FirstLeft = outRec2.FirstLeft;
                        }
                        outRec2.FirstLeft = outRec1;
                        if (this.m_UsingPolyTree) {
                            FixupFirstLefts2(outRec2, outRec1);
                        }
                    }
                }
            }
        }
    }

    private void UpdateOutPtIdxs(OutRec outrec) {
        OutPt op = outrec.Pts;
        do {
            op.Idx = outrec.Idx;
            op = op.Prev;
        } while (op != outrec.Pts);
    }

    private void DoSimplePolygons() {
        boolean z;
        boolean z2;
        int i = 0;
        while (i < this.m_PolyOuts.size()) {
            int i2 = i + 1;
            OutRec outrec = this.m_PolyOuts.get(i);
            OutPt op = outrec.Pts;
            if (op != null) {
                if (outrec.IsOpen) {
                    i = i2;
                } else {
                    do {
                        OutPt op2 = op.Next;
                        while (op2 != outrec.Pts) {
                            if (!(op.f535Pt != op2.f535Pt || op2.Next == op || op2.Prev == op)) {
                                OutPt op3 = op.Prev;
                                OutPt op4 = op2.Prev;
                                op.Prev = op4;
                                op4.Next = op;
                                op2.Prev = op3;
                                op3.Next = op2;
                                outrec.Pts = op;
                                OutRec outrec2 = CreateOutRec();
                                outrec2.Pts = op2;
                                UpdateOutPtIdxs(outrec2);
                                if (Poly2ContainsPoly1(outrec2.Pts, outrec.Pts)) {
                                    if (outrec.IsHole) {
                                        z2 = false;
                                    } else {
                                        z2 = true;
                                    }
                                    outrec2.IsHole = z2;
                                    outrec2.FirstLeft = outrec;
                                    if (this.m_UsingPolyTree) {
                                        FixupFirstLefts2(outrec2, outrec);
                                    }
                                } else if (Poly2ContainsPoly1(outrec.Pts, outrec2.Pts)) {
                                    outrec2.IsHole = outrec.IsHole;
                                    if (outrec2.IsHole) {
                                        z = false;
                                    } else {
                                        z = true;
                                    }
                                    outrec.IsHole = z;
                                    outrec2.FirstLeft = outrec.FirstLeft;
                                    outrec.FirstLeft = outrec2;
                                    if (this.m_UsingPolyTree) {
                                        FixupFirstLefts2(outrec, outrec2);
                                    }
                                } else {
                                    outrec2.IsHole = outrec.IsHole;
                                    outrec2.FirstLeft = outrec.FirstLeft;
                                    if (this.m_UsingPolyTree) {
                                        FixupFirstLefts1(outrec, outrec2);
                                    }
                                }
                                op2 = op;
                            }
                            op2 = op2.Next;
                        }
                        op = op.Next;
                    } while (op != outrec.Pts);
                }
            }
            i = i2;
        }
    }

    public static double Area(List<IntPoint> poly) {
        int cnt = poly.size();
        if (cnt < 3) {
            return 0.0d;
        }
        double a = 0.0d;
        int j = cnt - 1;
        for (int i = 0; i < cnt; i++) {
            a += (((double) poly.get(j).f512X) + ((double) poly.get(i).f512X)) * (((double) poly.get(j).f513Y) - ((double) poly.get(i).f513Y));
            j = i;
        }
        return (-a) * 0.5d;
    }

    /* access modifiers changed from: package-private */
    public double Area(OutRec outRec) {
        OutPt op = outRec.Pts;
        if (op == null) {
            return 0.0d;
        }
        double a = 0.0d;
        do {
            a += ((double) (op.Prev.f535Pt.f512X + op.f535Pt.f512X)) * ((double) (op.Prev.f535Pt.f513Y - op.f535Pt.f513Y));
            op = op.Next;
        } while (op != outRec.Pts);
        return 0.5d * a;
    }

    public static List<List<IntPoint>> SimplifyPolygon(List<IntPoint> poly, PolyFillType fillType) {
        List<List<IntPoint>> result = new ArrayList<>();
        Clipper c = new Clipper();
        c.StrictlySimple = true;
        c.AddPath(poly, PolyType.ptSubject, true);
        c.Execute(ClipType.ctUnion, result, fillType, fillType);
        return result;
    }

    public static List<List<IntPoint>> SimplifyPolygons(List<List<IntPoint>> polys, PolyFillType fillType) {
        List<List<IntPoint>> result = new ArrayList<>();
        Clipper c = new Clipper();
        c.StrictlySimple = true;
        c.AddPaths(polys, PolyType.ptSubject, true);
        c.Execute(ClipType.ctUnion, result, fillType, fillType);
        return result;
    }

    private static double DistanceSqrd(IntPoint pt1, IntPoint pt2) {
        double dx = ((double) pt1.f512X) - ((double) pt2.f512X);
        double dy = ((double) pt1.f513Y) - ((double) pt2.f513Y);
        return (dx * dx) + (dy * dy);
    }

    private static double DistanceFromLineSqrd(IntPoint pt, IntPoint ln1, IntPoint ln2) {
        double A = (double) (ln1.f513Y - ln2.f513Y);
        double B = (double) (ln2.f512X - ln1.f512X);
        double C = ((((double) pt.f512X) * A) + (((double) pt.f513Y) * B)) - ((((double) ln1.f512X) * A) + (((double) ln1.f513Y) * B));
        return (C * C) / ((A * A) + (B * B));
    }

    private static boolean SlopesNearCollinear(IntPoint pt1, IntPoint pt2, IntPoint pt3, double distSqrd) {
        boolean z;
        boolean z2;
        boolean z3;
        if (Math.abs(pt1.f512X - pt2.f512X) > Math.abs(pt1.f513Y - pt2.f513Y)) {
            if ((pt1.f512X > pt2.f512X) == (pt1.f512X < pt3.f512X)) {
                return DistanceFromLineSqrd(pt1, pt2, pt3) < distSqrd;
            }
            if (pt2.f512X > pt1.f512X) {
                z3 = true;
            } else {
                z3 = false;
            }
            return z3 == (pt2.f512X < pt3.f512X) ? DistanceFromLineSqrd(pt2, pt1, pt3) < distSqrd : DistanceFromLineSqrd(pt3, pt1, pt2) < distSqrd;
        }
        if (pt1.f513Y > pt2.f513Y) {
            z = true;
        } else {
            z = false;
        }
        if (z == (pt1.f513Y < pt3.f513Y)) {
            return DistanceFromLineSqrd(pt1, pt2, pt3) < distSqrd;
        }
        if (pt2.f513Y > pt1.f513Y) {
            z2 = true;
        } else {
            z2 = false;
        }
        return z2 == (pt2.f513Y < pt3.f513Y) ? DistanceFromLineSqrd(pt2, pt1, pt3) < distSqrd : DistanceFromLineSqrd(pt3, pt1, pt2) < distSqrd;
    }

    private static boolean PointsAreClose(IntPoint pt1, IntPoint pt2, double distSqrd) {
        double dx = ((double) pt1.f512X) - ((double) pt2.f512X);
        double dy = ((double) pt1.f513Y) - ((double) pt2.f513Y);
        return (dx * dx) + (dy * dy) <= distSqrd;
    }

    private static OutPt ExcludeOp(OutPt op) {
        OutPt result = op.Prev;
        result.Next = op.Next;
        op.Next.Prev = result;
        result.Idx = 0;
        return result;
    }

    public static List<IntPoint> CleanPolygon(List<IntPoint> path, double distance) {
        int cnt = path.size();
        if (cnt == 0) {
            return new ArrayList();
        }
        OutPt[] outPts = new OutPt[cnt];
        for (int i = 0; i < cnt; i++) {
            outPts[i] = new OutPt();
        }
        for (int i2 = 0; i2 < cnt; i2++) {
            outPts[i2].f535Pt = path.get(i2);
            outPts[i2].Next = outPts[(i2 + 1) % cnt];
            outPts[i2].Next.Prev = outPts[i2];
            outPts[i2].Idx = 0;
        }
        double distSqrd = distance * distance;
        OutPt op = outPts[0];
        while (op.Idx == 0 && op.Next != op.Prev) {
            if (PointsAreClose(op.f535Pt, op.Prev.f535Pt, distSqrd)) {
                op = ExcludeOp(op);
                cnt--;
            } else if (PointsAreClose(op.Prev.f535Pt, op.Next.f535Pt, distSqrd)) {
                ExcludeOp(op.Next);
                op = ExcludeOp(op);
                cnt -= 2;
            } else if (SlopesNearCollinear(op.Prev.f535Pt, op.f535Pt, op.Next.f535Pt, distSqrd)) {
                op = ExcludeOp(op);
                cnt--;
            } else {
                op.Idx = 1;
                op = op.Next;
            }
        }
        if (cnt < 3) {
            cnt = 0;
        }
        List<IntPoint> result = new ArrayList<>();
        for (int i3 = 0; i3 < cnt; i3++) {
            result.add(op.f535Pt);
            op = op.Next;
        }
        return result;
    }

    public static List<List<IntPoint>> CleanPolygons(List<List<IntPoint>> polys, double distance) {
        List<List<IntPoint>> result = new ArrayList<>();
        for (int i = 0; i < polys.size(); i++) {
            result.add(CleanPolygon(polys.get(i), distance));
        }
        return result;
    }

    static List<List<IntPoint>> Minkowski(List<IntPoint> pattern, List<IntPoint> path, boolean IsSum, boolean IsClosed) {
        int delta = IsClosed ? 1 : 0;
        int polyCnt = pattern.size();
        int pathCnt = path.size();
        List<List<IntPoint>> result = new ArrayList<>();
        if (IsSum) {
            for (int i = 0; i < pathCnt; i++) {
                List<IntPoint> p = new ArrayList<>();
                IntPoint tempPtn = path.get(i);
                for (IntPoint ip : pattern) {
                    p.add(new IntPoint(tempPtn.f512X + ip.f512X, tempPtn.f513Y + ip.f513Y));
                }
                result.add(p);
            }
        } else {
            for (int i2 = 0; i2 < pathCnt; i2++) {
                List<IntPoint> p2 = new ArrayList<>();
                IntPoint tempPtn2 = path.get(i2);
                for (IntPoint ip2 : pattern) {
                    p2.add(new IntPoint(tempPtn2.f512X - ip2.f512X, tempPtn2.f513Y - ip2.f513Y));
                }
                result.add(p2);
            }
        }
        List<List<IntPoint>> quads = new ArrayList<>();
        for (int i3 = 0; i3 < (pathCnt - 1) + delta; i3++) {
            for (int j = 0; j < polyCnt; j++) {
                List<IntPoint> quad = new ArrayList<>();
                quad.add(result.get(i3 % pathCnt).get(j % polyCnt));
                quad.add(result.get((i3 + 1) % pathCnt).get(j % polyCnt));
                quad.add(result.get((i3 + 1) % pathCnt).get((j + 1) % polyCnt));
                quad.add(result.get(i3 % pathCnt).get((j + 1) % polyCnt));
                if (!Orientation(quad)) {
                    Collections.reverse(quad);
                }
                quads.add(quad);
            }
        }
        return quads;
    }

    public static List<List<IntPoint>> MinkowskiSum(List<IntPoint> pattern, List<IntPoint> path, boolean pathIsClosed) {
        List<List<IntPoint>> paths = Minkowski(pattern, path, true, pathIsClosed);
        Clipper c = new Clipper();
        c.AddPaths(paths, PolyType.ptSubject, true);
        c.Execute(ClipType.ctUnion, paths, PolyFillType.pftNonZero, PolyFillType.pftNonZero);
        return paths;
    }

    private static List<IntPoint> TranslatePath(List<IntPoint> path, IntPoint delta) {
        List<IntPoint> outPath = new ArrayList<>();
        for (int i = 0; i < path.size(); i++) {
            outPath.add(new IntPoint(delta.f512X + path.get(i).f512X, path.get(i).f513Y + delta.f513Y));
        }
        return outPath;
    }

    public static List<List<IntPoint>> MinkowskiSum2(List<IntPoint> pattern, List<List<IntPoint>> paths, boolean pathIsClosed) {
        List<List<IntPoint>> solution = new ArrayList<>();
        Clipper c = new Clipper();
        for (int i = 0; i < paths.size(); i++) {
            c.AddPaths(Minkowski(pattern, paths.get(i), true, pathIsClosed), PolyType.ptSubject, true);
            if (pathIsClosed) {
                c.AddPath(TranslatePath(paths.get(i), pattern.get(0)), PolyType.ptClip, true);
            }
        }
        c.Execute(ClipType.ctUnion, solution, PolyFillType.pftNonZero, PolyFillType.pftNonZero);
        return solution;
    }

    public static List<List<IntPoint>> MinkowskiDiff(List<IntPoint> poly1, List<IntPoint> poly2) {
        List<List<IntPoint>> paths = Minkowski(poly1, poly2, false, true);
        Clipper c = new Clipper();
        c.AddPaths(paths, PolyType.ptSubject, true);
        c.Execute(ClipType.ctUnion, paths, PolyFillType.pftNonZero, PolyFillType.pftNonZero);
        return paths;
    }

    public static List<List<IntPoint>> PolyTreeToPaths(PolyTree polytree) {
        List<List<IntPoint>> result = new ArrayList<>();
        AddPolyNodeToPaths(polytree, NodeType.ntAny, result);
        return result;
    }

    static void AddPolyNodeToPaths(PolyNode polynode, NodeType nt, List<List<IntPoint>> paths) {
        boolean match = true;
        switch ($SWITCH_TABLE$com$xzy$gogisapi$SpatialAnalyst$Clipper$NodeType()[nt.ordinal()]) {
            case 2:
                return;
            case 3:
                if (polynode.IsOpen) {
                    match = false;
                    break;
                } else {
                    match = true;
                    break;
                }
        }
        if (polynode.m_polygon.size() > 0 && match) {
            paths.add(polynode.m_polygon);
        }
        for (PolyNode pn : polynode.m_Childs) {
            AddPolyNodeToPaths(pn, nt, paths);
        }
    }

    public static List<List<IntPoint>> OpenPathsFromPolyTree(PolyTree polytree) {
        List<List<IntPoint>> result = new ArrayList<>();
        for (int i = 0; i < polytree.ChildCount(); i++) {
            if (((PolyNode) polytree.m_Childs.get(i)).IsOpen) {
                result.add(((PolyNode) polytree.m_Childs.get(i)).m_polygon);
            }
        }
        return result;
    }

    public static List<List<IntPoint>> ClosedPathsFromPolyTree(PolyTree polytree) {
        List<List<IntPoint>> result = new ArrayList<>();
        AddPolyNodeToPaths(polytree, NodeType.ntClosed, result);
        return result;
    }
}
