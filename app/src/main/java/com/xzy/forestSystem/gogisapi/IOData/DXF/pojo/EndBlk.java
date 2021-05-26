package  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo;

public class EndBlk extends Element {
    private Data startTag;

    public EndBlk() {
        int[] f = new int[6];
        f[1] = 5;
        f[2] = 8;
        f[3] = 102;
        f[4] = 330;
        f[5] = 100;
        this.dataAcceptanceList.add(f);
        this.startTag = new Data(0, "ENDBLK");
    }

    public EndBlk(Block b) throws UnexpectedElement {
        this();
        if (b.GetIndexFor(5) != -1) {
            AddData(b.GetDataFor(5));
        }
        if (b.GetIndexFor(8) != -1) {
            AddData(b.GetDataFor(8));
        }
    }
}
