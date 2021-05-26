package  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo;

public class Document {
    public Blocks blocks;
    public Entities entities = new Entities();
    public Header header;
    public Tables tables;

    public void SetHeader(Header h) {
        this.header = h;
    }

    public void SetEntities(Entities e) {
        this.entities = e;
    }

    public void SetBlocks(Blocks b) {
        this.blocks = b;
    }

    public void SetTables(Tables t) {
        this.tables = t;
    }

    public void add(Entity e) throws UnexpectedElement {
        this.entities.AddEntity(e);
    }

    public void add(Block b) {
        this.blocks.addBlock(b);
    }
}
