package  com.xzy.forestSystem.gogisapi.SpatialAnalyst;

import java.util.Comparator;

/* compiled from: IntersectNode */
class MyComparator implements Comparator<IntersectNode> {
    MyComparator() {
    }

    public int compare(IntersectNode node1, IntersectNode node2) {
        int i = node2.f514Pt.f513Y - node1.f514Pt.f513Y;
        if (i > 0) {
            return 1;
        }
        if (i < 0) {
            return -1;
        }
        return 0;
    }
}
