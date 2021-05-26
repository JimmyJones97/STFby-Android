package  com.xzy.forestSystem.gogisapi.SpatialAnalyst;

import java.util.ArrayList;
import java.util.List;

public class PolyNode {
    public boolean IsOpen;
    List<PolyNode> m_Childs = new ArrayList();
    int m_Index;
    PolyNode m_Parent;
    EndType m_endtype;
    JoinType m_jointype;
    List<IntPoint> m_polygon = new ArrayList();

    private boolean IsHoleNode() {
        boolean result = true;
        for (PolyNode node = this.m_Parent; node != null; node = node.m_Parent) {
            result = !result;
        }
        return result;
    }

    public int ChildCount() {
        return this.m_Childs.size();
    }

    public List<IntPoint> Contour() {
        return this.m_polygon;
    }

    /* access modifiers changed from: package-private */
    public void AddChild(PolyNode Child) {
        int cnt = this.m_Childs.size();
        this.m_Childs.add(Child);
        Child.m_Parent = this;
        Child.m_Index = cnt;
    }

    public PolyNode GetNext() {
        if (this.m_Childs.size() > 0) {
            return this.m_Childs.get(0);
        }
        return GetNextSiblingUp();
    }

    /* access modifiers changed from: package-private */
    public PolyNode GetNextSiblingUp() {
        if (this.m_Parent == null) {
            return null;
        }
        if (this.m_Index == this.m_Parent.m_Childs.size() - 1) {
            return this.m_Parent.GetNextSiblingUp();
        }
        return this.m_Parent.m_Childs.get(this.m_Index + 1);
    }

    public List<PolyNode> Childs() {
        return this.m_Childs;
    }

    public PolyNode Parent() {
        return this.m_Parent;
    }

    public boolean IsHole() {
        return IsHoleNode();
    }
}
