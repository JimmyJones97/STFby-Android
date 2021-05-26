package  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo;

public class Variable extends Element {
    private Data endTag = new Data(0, 0);
    private Data startTag = new Data(0, 0);

    public Variable(String nume, int dataType, Object val) {
        this.data.add(new Data(9, nume));
        this.data.add(new Data(dataType, val));
    }

    public String getVarName() {
        return (String) ((Data) this.data.get(0)).data;
    }

    public Object getValue() {
        return ((Data) this.data.get(1)).data;
    }
}
