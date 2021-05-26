package  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo;

public class Tables extends Section {
    public Tables() {
        super("TABLES");
    }

    public void addTable(Table t) throws UnexpectedElement {
        AddElement(t);
    }
}
