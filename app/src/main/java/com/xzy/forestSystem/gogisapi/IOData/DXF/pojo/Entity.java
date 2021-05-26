package  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo;

public class Entity extends Element {
    public Entity(String name, String layer) throws UnexpectedElement {
        int[] it = new int[16];
        it[1] = 5;
        it[2] = 102;
        it[3] = 330;
        it[4] = 360;
        it[5] = 100;
        it[6] = 67;
        it[7] = 410;
        it[8] = 8;
        it[9] = 6;
        it[10] = 62;
        it[11] = 370;
        it[12] = 48;
        it[13] = 60;
        it[14] = 92;
        it[15] = 310;
        for (int i : it) {
            this.dataAcceptanceList.add(Integer.valueOf(i));
        }
        this.startTag = new Data(0, name);
        AddData(new Data(8, layer));
    }

    public Entity() {
    }

    public String getLayer() {
        return (String) GetDataFor(8).data;
    }

    public void setLayer(String s) throws UnexpectedElement {
        AddReplace(8, s);
    }
}
