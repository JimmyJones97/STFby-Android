package  com.xzy.forestSystem.gogisapi.SpatialAnalyst;

/* access modifiers changed from: package-private */
public class TEdge {
    IntPoint Bot;
    IntPoint Curr;
    IntPoint Delta;

    /* renamed from: Dx */
    double f537Dx;
    TEdge Next;
    TEdge NextInAEL;
    TEdge NextInLML;
    TEdge NextInSEL;
    int OutIdx;
    PolyType PolyTyp;
    TEdge Prev;
    TEdge PrevInAEL;
    TEdge PrevInSEL;
    EdgeSide Side;
    IntPoint Top;
    int WindCnt;
    int WindCnt2;
    int WindDelta;

    TEdge() {
    }
}
