package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;

public class DwgSeqend extends DwgObject {
    public void readDwgSeqendV15(int[] data, int offset) throws Exception {
        readObjectTailV15(data, readObjectHeaderV15(data, offset));
    }
}
