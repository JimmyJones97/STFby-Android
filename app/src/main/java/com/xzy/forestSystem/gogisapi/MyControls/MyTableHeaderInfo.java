package  com.xzy.forestSystem.gogisapi.MyControls;

public class MyTableHeaderInfo {
    public int EndCol;
    public int EndRow;
    public boolean Frozen = false;
    public EMyTableColumnType ItemType = EMyTableColumnType.TEXT;
    public int StartCol;
    public int StartRow;
    public String Text;

    public MyTableHeaderInfo(String textStr, int startRow, int endRow, int startCol, int endCol, boolean isFrozen, EMyTableColumnType arg8) {
        this.Text = textStr;
        this.StartRow = startRow;
        this.EndRow = endRow;
        this.StartCol = startCol;
        this.EndCol = endCol;
        this.Frozen = isFrozen;
        this.ItemType = arg8;
    }
}
