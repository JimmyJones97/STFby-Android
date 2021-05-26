package  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo;

public class Table extends Element {
    public Table(String type) throws UnexpectedElement {
        int[] it = new int[9];
        it[1] = 2;
        it[2] = 5;
        it[3] = 102;
        it[4] = 360;
        it[5] = 102;
        it[6] = 330;
        it[7] = 100;
        it[8] = 70;
        for (int i : it) {
            this.dataAcceptanceList.add(Integer.valueOf(i));
        }
        AddReplace(2, type);
        this.startTag = new Data(0, "TABLE");
        this.endTag = new Data(0, "ENDTAB");
    }

    public void AddTableEntry(TableEntry te) throws UnexpectedElement {
        AddElement(te);
    }
}
