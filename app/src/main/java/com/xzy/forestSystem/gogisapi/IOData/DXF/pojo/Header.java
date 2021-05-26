package  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo;

import java.util.Iterator;

public class Header extends Section {
    public Header() {
        super("HEADER");
    }

    public int VariableCount() {
        return this.elements.size();
    }

    public Variable getVariable(int index) {
        return (Variable) this.elements.get(index);
    }

    public Object valueOf(String varName) {
        Iterator it = this.elements.iterator();
        while (it.hasNext()) {
            Variable v = (Variable) it.next();
            if (v.getVarName() == varName) {
                return v.getValue();
            }
        }
        return null;
    }

    public void addVariable(Variable v) {
        this.elements.add(v);
    }
}
