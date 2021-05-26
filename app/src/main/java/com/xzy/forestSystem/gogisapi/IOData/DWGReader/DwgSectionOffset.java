package  com.xzy.forestSystem.gogisapi.IOData.DWGReader;

public class DwgSectionOffset {
    private String key;
    private int seek;
    private int size;

    public DwgSectionOffset(String key2, int seek2, int size2) {
        this.key = key2;
        this.seek = seek2;
        this.size = size2;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key2) {
        this.key = key2;
    }

    public int getSeek() {
        return this.seek;
    }

    public void setSeek(int seek2) {
        this.seek = seek2;
    }
}
