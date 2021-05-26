package  com.xzy.forestSystem.gogisapi.SpatialAnalyst;

import java.util.ArrayList;
import java.util.List;

public class ClipperBase {
    protected static final double horizontal = -3.4E38d;
    protected static final double tolerance = 1.0E-20d;
    public boolean PreserveCollinear;
    protected final int Skip = -2;
    protected final int Unassigned = -1;
    public final int hiRange = 32767;
    public final int loRange = 32767;
    LocalMinima m_CurrentLM = null;
    boolean m_HasOpenPaths = false;
    LocalMinima m_MinimaList = null;
    boolean m_UseFullRange = false;
    List<List<TEdge>> m_edges = new ArrayList();

    static boolean near_zero(double val) {
        return val > -1.0E-20d && val < tolerance;
    }

    public void SwapIntPointX(IntPoint val1, IntPoint val2) {
        int tmp = val1.f512X;
        val1.f512X = val2.f512X;
        val2.f512X = tmp;
    }

    static boolean IsHorizontal(TEdge e) {
        return e.Delta.f513Y == 0;
    }

    /* access modifiers changed from: package-private */
    public boolean PointIsVertex(IntPoint pt, OutPt pp) {
        OutPt pp2 = pp;
        while (pp2.f535Pt != pt) {
            pp2 = pp2.Next;
            if (pp2 == pp) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean PointOnLineSegment(IntPoint pt, IntPoint linePt1, IntPoint linePt2, boolean UseFullRange) {
        boolean z;
        boolean z2;
        boolean z3;
        if (UseFullRange) {
            if (!((pt.f512X == linePt1.f512X && pt.f513Y == linePt1.f513Y) || (pt.f512X == linePt2.f512X && pt.f513Y == linePt2.f513Y))) {
                if ((pt.f512X > linePt1.f512X) != (pt.f512X < linePt2.f512X)) {
                    return false;
                }
                if (pt.f513Y > linePt1.f513Y) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                if (z3 != (pt.f513Y < linePt2.f513Y)) {
                    return false;
                }
            }
            return true;
        }
        if (!((pt.f512X == linePt1.f512X && pt.f513Y == linePt1.f513Y) || (pt.f512X == linePt2.f512X && pt.f513Y == linePt2.f513Y))) {
            if (pt.f512X > linePt1.f512X) {
                z = true;
            } else {
                z = false;
            }
            if (z != (pt.f512X < linePt2.f512X)) {
                return false;
            }
            if (pt.f513Y > linePt1.f513Y) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (!(z2 == (pt.f513Y < linePt2.f513Y) && (pt.f512X - linePt1.f512X) * (linePt2.f513Y - linePt1.f513Y) == (linePt2.f512X - linePt1.f512X) * (pt.f513Y - linePt1.f513Y))) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean PointOnPolygon(IntPoint pt, OutPt pp, boolean UseFullRange) {
        OutPt pp2 = pp;
        while (!PointOnLineSegment(pt, pp2.f535Pt, pp2.Next.f535Pt, UseFullRange)) {
            pp2 = pp2.Next;
            if (pp2 == pp) {
                return false;
            }
        }
        return true;
    }

    static boolean SlopesEqual(TEdge e1, TEdge e2, boolean UseFullRange) {
        return e1.Delta.f513Y * e2.Delta.f512X == e1.Delta.f512X * e2.Delta.f513Y;
    }

    protected static boolean SlopesEqual(IntPoint pt1, IntPoint pt2, IntPoint pt3, boolean UseFullRange) {
        return ((pt1.f513Y - pt2.f513Y) * (pt2.f512X - pt3.f512X)) - ((pt1.f512X - pt2.f512X) * (pt2.f513Y - pt3.f513Y)) == 0;
    }

    protected static boolean SlopesEqual(IntPoint pt1, IntPoint pt2, IntPoint pt3, IntPoint pt4, boolean UseFullRange) {
        return ((pt1.f513Y - pt2.f513Y) * (pt3.f512X - pt4.f512X)) - ((pt1.f512X - pt2.f512X) * (pt3.f513Y - pt4.f513Y)) == 0;
    }

    ClipperBase() {
    }

    public void Clear() {
        DisposeLocalMinimaList();
        for (int i = 0; i < this.m_edges.size(); i++) {
            this.m_edges.get(i).clear();
        }
        this.m_edges.clear();
        this.m_UseFullRange = false;
        this.m_HasOpenPaths = false;
    }

    private void DisposeLocalMinimaList() {
        while (this.m_MinimaList != null) {
            LocalMinima tmpLm = this.m_MinimaList.Next;
            this.m_MinimaList = null;
            this.m_MinimaList = tmpLm;
        }
        this.m_CurrentLM = null;
    }

    /* access modifiers changed from: package-private */
    public void RangeTest(IntPoint Pt, boolean useFullRange) {
        if (useFullRange) {
            if (Pt.f512X > 32767 || Pt.f513Y > 32767 || (-Pt.f512X) > 32767 || (-Pt.f513Y) <= 32767) {
            }
        } else if (Pt.f512X > 32767 || Pt.f513Y > 32767 || (-Pt.f512X) > 32767 || (-Pt.f513Y) > 32767) {
            RangeTest(Pt, true);
        }
    }

    private void InitEdge(TEdge e, TEdge eNext, TEdge ePrev, IntPoint pt) {
        e.Next = eNext;
        e.Prev = ePrev;
        e.Curr = pt;
        e.OutIdx = -1;
    }

    private void InitEdge2(TEdge e, PolyType polyType) {
        if (e.Curr.f513Y >= e.Next.Curr.f513Y) {
            e.Bot = e.Curr;
            e.Top = e.Next.Curr;
        } else {
            e.Top = e.Curr;
            e.Bot = e.Next.Curr;
        }
        SetDx(e);
        e.PolyTyp = polyType;
    }

    private TEdge FindNextLocMin(TEdge E) {
        while (true) {
            if (E.Bot != E.Prev.Bot || E.Curr == E.Top) {
                E = E.Next;
            } else if (E.f537Dx != horizontal && E.Prev.f537Dx != horizontal) {
                return E;
            } else {
                while (E.Prev.f537Dx == horizontal) {
                    E = E.Prev;
                }
                while (E.f537Dx == horizontal) {
                    E = E.Next;
                }
                if (E.Top.f513Y != E.Prev.Bot.f513Y) {
                    return E.Prev.Bot.f512X < E.Bot.f512X ? E : E;
                }
            }
        }
    }

    private TEdge ProcessBound(TEdge E, boolean LeftBoundIsForward) {
        TEdge Result;
        TEdge EStart;
        TEdge E2;
        TEdge Result2;
        TEdge Result3 = E;
        if (Result3.OutIdx == -2) {
            TEdge E3 = Result3;
            if (LeftBoundIsForward) {
                while (E3.Top.f513Y == E3.Next.Bot.f513Y) {
                    E3 = E3.Next;
                }
                while (E3 != Result3 && E3.f537Dx == horizontal) {
                    E3 = E3.Prev;
                }
            } else {
                while (E3.Top.f513Y == E3.Prev.Bot.f513Y) {
                    E3 = E3.Prev;
                }
                while (E3 != Result3 && E3.f537Dx == horizontal) {
                    E3 = E3.Next;
                }
            }
            if (E3 != Result3) {
                if (LeftBoundIsForward) {
                    E2 = Result3.Next;
                } else {
                    E2 = Result3.Prev;
                }
                LocalMinima locMin = new LocalMinima();
                locMin.Next = null;
                locMin.f515Y = E2.Bot.f513Y;
                locMin.LeftBound = null;
                locMin.RightBound = E2;
                E2.WindDelta = 0;
                Result2 = ProcessBound(E2, LeftBoundIsForward);
                InsertLocalMinima(locMin);
            } else if (LeftBoundIsForward) {
                Result2 = E3.Next;
            } else {
                Result2 = E3.Prev;
            }
            return Result2;
        }
        if (E.f537Dx == horizontal) {
            if (LeftBoundIsForward) {
                EStart = E.Prev;
            } else {
                EStart = E.Next;
            }
            if (EStart.OutIdx != -2) {
                if (EStart.f537Dx == horizontal) {
                    if (!(EStart.Bot.f512X == E.Bot.f512X || EStart.Top.f512X == E.Bot.f512X)) {
                        ReverseHorizontal(E);
                    }
                } else if (EStart.Bot.f512X != E.Bot.f512X) {
                    ReverseHorizontal(E);
                }
            }
        }
        if (LeftBoundIsForward) {
            while (Result3.Top.f513Y == Result3.Next.Bot.f513Y && Result3.Next.OutIdx != -2) {
                Result3 = Result3.Next;
            }
            if (Result3.f537Dx == horizontal && Result3.Next.OutIdx != -2) {
                TEdge Horz = Result3;
                while (Horz.Prev.f537Dx == horizontal) {
                    Horz = Horz.Prev;
                }
                if (Horz.Prev.Top.f512X == Result3.Next.Top.f512X) {
                    if (!LeftBoundIsForward) {
                        Result3 = Horz.Prev;
                    }
                } else if (Horz.Prev.Top.f512X > Result3.Next.Top.f512X) {
                    Result3 = Horz.Prev;
                }
            }
            while (E != Result3) {
                E.NextInLML = E.Next;
                if (!(E.f537Dx != horizontal || E == E || E.Bot.f512X == E.Prev.Top.f512X)) {
                    ReverseHorizontal(E);
                }
                E = E.Next;
            }
            if (!(E.f537Dx != horizontal || E == E || E.Bot.f512X == E.Prev.Top.f512X)) {
                ReverseHorizontal(E);
            }
            Result = Result3.Next;
        } else {
            while (Result3.Top.f513Y == Result3.Prev.Bot.f513Y && Result3.Prev.OutIdx != -2) {
                Result3 = Result3.Prev;
            }
            if (Result3.f537Dx == horizontal && Result3.Prev.OutIdx != -2) {
                TEdge Horz2 = Result3;
                while (Horz2.Next.f537Dx == horizontal) {
                    Horz2 = Horz2.Next;
                }
                if (Horz2.Next.Top.f512X == Result3.Prev.Top.f512X) {
                    if (!LeftBoundIsForward) {
                        Result3 = Horz2.Next;
                    }
                } else if (Horz2.Next.Top.f512X > Result3.Prev.Top.f512X) {
                    Result3 = Horz2.Next;
                }
            }
            while (E != Result3) {
                E.NextInLML = E.Prev;
                if (!(E.f537Dx != horizontal || E == E || E.Bot.f512X == E.Next.Top.f512X)) {
                    ReverseHorizontal(E);
                }
                E = E.Prev;
            }
            if (!(E.f537Dx != horizontal || E == E || E.Bot.f512X == E.Next.Top.f512X)) {
                ReverseHorizontal(E);
            }
            Result = Result3.Prev;
        }
        return Result;
    }

    public boolean AddPath(List<IntPoint> pg, PolyType polyType, boolean Closed) {
        boolean leftBoundIsForward;
        int highI = pg.size() - 1;
        if (Closed) {
            while (highI > 0 && pg.get(highI).Equals(pg.get(0))) {
                highI--;
            }
        }
        while (highI > 0 && pg.get(highI).Equals(pg.get(highI - 1))) {
            highI--;
        }
        if ((Closed && highI < 2) || (!Closed && highI < 1)) {
            return false;
        }
        List<TEdge> edges = new ArrayList<>();
        for (int i = 0; i <= highI; i++) {
            edges.add(new TEdge());
        }
        boolean IsFlat = true;
        edges.get(1).Curr = pg.get(1);
        RangeTest(pg.get(0), this.m_UseFullRange);
        RangeTest(pg.get(highI), this.m_UseFullRange);
        InitEdge(edges.get(0), edges.get(1), edges.get(highI), pg.get(0));
        InitEdge(edges.get(highI), edges.get(0), edges.get(highI - 1), pg.get(highI));
        for (int i2 = highI - 1; i2 >= 1; i2--) {
            RangeTest(pg.get(i2), this.m_UseFullRange);
            InitEdge(edges.get(i2), edges.get(i2 + 1), edges.get(i2 - 1), pg.get(i2));
        }
        TEdge eStart = edges.get(0);
        TEdge E = eStart;
        TEdge eLoopStop = eStart;
        while (true) {
            if (E.Curr != E.Next.Curr || (!Closed && E.Next == eStart)) {
                if (E.Prev == E.Next) {
                    break;
                } else if (!Closed || !SlopesEqual(E.Prev.Curr, E.Curr, E.Next.Curr, this.m_UseFullRange) || (this.PreserveCollinear && Pt2IsBetweenPt1AndPt3(E.Prev.Curr, E.Curr, E.Next.Curr))) {
                    E = E.Next;
                    if (E != eLoopStop) {
                        if (!Closed && E.Next == eStart) {
                            break;
                        }
                    } else {
                        break;
                    }
                } else {
                    if (E == eStart) {
                        eStart = E.Next;
                    }
                    E = RemoveEdge(E).Prev;
                    eLoopStop = E;
                }
            } else if (E == E.Next) {
                break;
            } else {
                if (E == eStart) {
                    eStart = E.Next;
                }
                E = RemoveEdge(E);
                eLoopStop = E;
            }
        }
        if ((!Closed && E == E.Next) || (Closed && E.Prev == E.Next)) {
            return false;
        }
        if (!Closed) {
            this.m_HasOpenPaths = true;
            eStart.Prev.OutIdx = -2;
        }
        TEdge E2 = eStart;
        do {
            InitEdge2(E2, polyType);
            E2 = E2.Next;
            if (IsFlat && E2.Curr.f513Y != eStart.Curr.f513Y) {
                IsFlat = false;
                continue;
            }
        } while (E2 != eStart);
        if (!IsFlat) {
            this.m_edges.add(edges);
            TEdge EMin = null;
            if (E2.Prev.Bot == E2.Prev.Top) {
                E2 = E2.Next;
            }
            while (true) {
                TEdge E3 = FindNextLocMin(E2);
                if (E3 == EMin) {
                    return true;
                }
                if (EMin == null) {
                    EMin = E3;
                }
                LocalMinima locMin = new LocalMinima();
                locMin.Next = null;
                locMin.f515Y = E3.Bot.f513Y;
                if (E3.f537Dx < E3.Prev.f537Dx) {
                    locMin.LeftBound = E3.Prev;
                    locMin.RightBound = E3;
                    leftBoundIsForward = false;
                } else {
                    locMin.LeftBound = E3;
                    locMin.RightBound = E3.Prev;
                    leftBoundIsForward = true;
                }
                locMin.LeftBound.Side = EdgeSide.esLeft;
                locMin.RightBound.Side = EdgeSide.esRight;
                if (!Closed) {
                    locMin.LeftBound.WindDelta = 0;
                } else if (locMin.LeftBound.Next == locMin.RightBound) {
                    locMin.LeftBound.WindDelta = -1;
                } else {
                    locMin.LeftBound.WindDelta = 1;
                }
                locMin.RightBound.WindDelta = -locMin.LeftBound.WindDelta;
                E2 = ProcessBound(locMin.LeftBound, leftBoundIsForward);
                if (E2.OutIdx == -2) {
                    E2 = ProcessBound(E2, leftBoundIsForward);
                }
                TEdge E22 = ProcessBound(locMin.RightBound, !leftBoundIsForward);
                if (E22.OutIdx == -2) {
                    E22 = ProcessBound(E22, !leftBoundIsForward);
                }
                if (locMin.LeftBound.OutIdx == -2) {
                    locMin.LeftBound = null;
                } else if (locMin.RightBound.OutIdx == -2) {
                    locMin.RightBound = null;
                }
                InsertLocalMinima(locMin);
                if (!leftBoundIsForward) {
                    E2 = E22;
                }
            }
        } else if (Closed) {
            return false;
        } else {
            E2.Prev.OutIdx = -2;
            if (E2.Prev.Bot.f512X < E2.Prev.Top.f512X) {
                ReverseHorizontal(E2.Prev);
            }
            LocalMinima locMin2 = new LocalMinima();
            locMin2.Next = null;
            locMin2.f515Y = E2.Bot.f513Y;
            locMin2.LeftBound = null;
            locMin2.RightBound = E2;
            locMin2.RightBound.Side = EdgeSide.esRight;
            locMin2.RightBound.WindDelta = 0;
            while (E2.Next.OutIdx != -2) {
                E2.NextInLML = E2.Next;
                if (E2.Bot.f512X != E2.Prev.Top.f512X) {
                    ReverseHorizontal(E2);
                }
                E2 = E2.Next;
            }
            InsertLocalMinima(locMin2);
            this.m_edges.add(edges);
            return true;
        }
    }

    public boolean AddPaths(List<List<IntPoint>> ppg, PolyType polyType, boolean closed) {
        boolean result = false;
        for (int i = 0; i < ppg.size(); i++) {
            if (AddPath(ppg.get(i), polyType, closed)) {
                result = true;
            }
        }
        return result;
    }

    /* access modifiers changed from: package-private */
    public boolean Pt2IsBetweenPt1AndPt3(IntPoint pt1, IntPoint pt2, IntPoint pt3) {
        if (pt1 == pt3 || pt1 == pt2 || pt3 == pt2) {
            return false;
        }
        if (pt1.f512X != pt3.f512X) {
            return (pt2.f512X > pt1.f512X) == (pt2.f512X < pt3.f512X);
        }
        return (pt2.f513Y > pt1.f513Y) == (pt2.f513Y < pt3.f513Y);
    }

    /* access modifiers changed from: package-private */
    public TEdge RemoveEdge(TEdge e) {
        e.Prev.Next = e.Next;
        e.Next.Prev = e.Prev;
        TEdge result = e.Next;
        e.Prev = null;
        return result;
    }

    private void SetDx(TEdge e) {
        e.Delta.f512X = e.Top.f512X - e.Bot.f512X;
        e.Delta.f513Y = e.Top.f513Y - e.Bot.f513Y;
        if (e.Delta.f513Y == 0) {
            e.f537Dx = horizontal;
        } else {
            e.f537Dx = ((double) e.Delta.f512X) / ((double) e.Delta.f513Y);
        }
    }

    private void InsertLocalMinima(LocalMinima newLm) {
        if (this.m_MinimaList == null) {
            this.m_MinimaList = newLm;
        } else if (newLm.f515Y >= this.m_MinimaList.f515Y) {
            newLm.Next = this.m_MinimaList;
            this.m_MinimaList = newLm;
        } else {
            LocalMinima tmpLm = this.m_MinimaList;
            while (tmpLm.Next != null && newLm.f515Y < tmpLm.Next.f515Y) {
                tmpLm = tmpLm.Next;
            }
            newLm.Next = tmpLm.Next;
            tmpLm.Next = newLm;
        }
    }

    /* access modifiers changed from: protected */
    public void PopLocalMinima() {
        if (this.m_CurrentLM != null) {
            this.m_CurrentLM = this.m_CurrentLM.Next;
        }
    }

    private void ReverseHorizontal(TEdge e) {
        SwapIntPointX(e.Top, e.Bot);
    }

    /* access modifiers changed from: protected */
    public void Reset() {
        this.m_CurrentLM = this.m_MinimaList;
        if (this.m_CurrentLM != null) {
            for (LocalMinima lm = this.m_MinimaList; lm != null; lm = lm.Next) {
                TEdge e = lm.LeftBound;
                if (e != null) {
                    e.Curr = e.Bot;
                    e.Side = EdgeSide.esLeft;
                    e.OutIdx = -1;
                }
                TEdge e2 = lm.RightBound;
                if (e2 != null) {
                    e2.Curr = e2.Bot;
                    e2.Side = EdgeSide.esRight;
                    e2.OutIdx = -1;
                }
            }
        }
    }

    public static IntRect GetBounds(List<List<IntPoint>> paths) {
        int i = 0;
        int cnt = paths.size();
        while (i < cnt && paths.get(i).size() == 0) {
            i++;
        }
        if (i == cnt) {
            return new IntRect(0, 0, 0, 0);
        }
        IntRect result = new IntRect();
        result.left = paths.get(i).get(0).f512X;
        result.right = result.left;
        result.top = paths.get(i).get(0).f513Y;
        result.bottom = result.top;
        while (i < cnt) {
            for (int j = 0; j < paths.get(i).size(); j++) {
                IntPoint tempPtn = paths.get(i).get(j);
                if (tempPtn.f512X < result.left) {
                    result.left = tempPtn.f512X;
                } else if (tempPtn.f512X > result.right) {
                    result.right = tempPtn.f512X;
                }
                if (tempPtn.f513Y < result.top) {
                    result.top = tempPtn.f513Y;
                } else if (tempPtn.f513Y > result.bottom) {
                    result.bottom = tempPtn.f513Y;
                }
            }
            i++;
        }
        return result;
    }
}
