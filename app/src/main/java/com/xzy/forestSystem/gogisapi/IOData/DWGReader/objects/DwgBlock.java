package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgBlock extends DwgObject {
    private String name;

    public void readDwgBlockV15(int[] data, int offset) throws Exception {
        Vector v = DwgUtil.getTextString(data, readObjectHeaderV15(data, offset));
        int bitPos = ((Integer) v.get(0)).intValue();
        this.name = (String) v.get(1);
        readObjectTailV15(data, bitPos);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }
}
