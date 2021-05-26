package  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo;

public class Blocks extends Section {
    public Blocks() {
        super("BLOCKS");
    }

    public void addBlock(Block b) {
        this.elements.add(b);
    }
}
