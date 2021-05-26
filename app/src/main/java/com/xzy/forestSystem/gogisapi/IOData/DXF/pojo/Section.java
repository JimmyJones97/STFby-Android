package  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo;

import java.util.ArrayList;

public class Section extends Element {
    public Section(String s) {
        this.startTag = new Data(0, "SECTION");
        this.endTag = new Data(0, "ENDSEC");
        this.data = new ArrayList();
        this.elements = new ArrayList();
        this.data.add(new Data(2, s));
    }
}
