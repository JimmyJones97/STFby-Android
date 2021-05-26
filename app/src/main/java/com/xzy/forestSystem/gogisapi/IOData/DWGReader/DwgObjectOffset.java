package  com.xzy.forestSystem.gogisapi.IOData.DWGReader;

public class DwgObjectOffset {
    private int handle;
    private int offset;

    public DwgObjectOffset(int handle2, int offset2) {
        this.handle = handle2;
        this.offset = offset2;
    }

    public int getHandle() {
        return this.handle;
    }

    public int getOffset() {
        return this.offset;
    }
}
