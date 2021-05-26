package  com.xzy.forestSystem.gogisapi.MyControls;

import java.util.List;

public class MyTableHeader {
    public List<MyTableHeaderInfo> m_HeaderInfoList = null;
    public List<Integer> m_HeaderWidthList = null;
    public boolean m_IsFrozen = true;
    public List<Integer> m_ItemsRelativeHeightList = null;

    public void SetHeaderInfoList(List<MyTableHeaderInfo> paramList) {
        this.m_HeaderInfoList = paramList;
    }

    public void SetHeaderWidthList(List<Integer> paramList) {
        this.m_HeaderWidthList = paramList;
    }

    public void SetItemsRelativeHeightList(List<Integer> paramList) {
        this.m_ItemsRelativeHeightList = paramList;
    }
}
