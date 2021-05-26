package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;

public class DwgEndblk extends DwgObject {
    public void readDwgEndblkV15(int[] data, int offset) throws Exception {
        readObjectTailV15(data, readObjectHeaderV15(data, offset));
    }
}
