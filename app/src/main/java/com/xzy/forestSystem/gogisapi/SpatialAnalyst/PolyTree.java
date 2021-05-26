package  com.xzy.forestSystem.gogisapi.SpatialAnalyst;

import java.util.ArrayList;
import java.util.List;

public class PolyTree extends PolyNode {
    List<PolyNode> m_AllPolys = new ArrayList();

    /* access modifiers changed from: protected */
    public void finalize() {
        Clear();
    }

    public void Clear() {
        this.m_AllPolys.clear();
        this.m_Childs.clear();
    }

    public PolyNode GetFirst() {
        if (this.m_Childs.size() > 0) {
            return (PolyNode) this.m_Childs.get(0);
        }
        return null;
    }

    public int Total() {
        int result = this.m_AllPolys.size();
        if (result <= 0 || this.m_Childs.get(0) == this.m_AllPolys.get(0)) {
            return result;
        }
        return result - 1;
    }
}
