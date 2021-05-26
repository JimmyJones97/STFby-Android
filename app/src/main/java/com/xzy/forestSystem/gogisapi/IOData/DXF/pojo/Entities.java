package  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo;

public class Entities extends Section {
    public Entities() {
        super("ENTITIES");
    }

    public void AddEntity(Entity e) throws UnexpectedElement {
        AddElement(e);
    }
}
