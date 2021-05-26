package  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo;

public class TableEntry extends Element {
    public TableEntry() {
    }

    public TableEntry(String type) {
        int[] it = new int[7];
        it[1] = 5;
        it[2] = 105;
        it[3] = 102;
        it[4] = 330;
        it[5] = 360;
        it[6] = 100;
        for (int i : it) {
            this.dataAcceptanceList.add(Integer.valueOf(i));
        }
        this.startTag = new Data(0, type);
    }
}
