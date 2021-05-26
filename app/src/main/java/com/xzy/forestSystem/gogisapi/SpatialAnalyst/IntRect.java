package  com.xzy.forestSystem.gogisapi.SpatialAnalyst;

public class IntRect {
    public int bottom;
    public int left;
    public int right;
    public int top;

    public IntRect() {
        this.left = 0;
        this.top = 0;
        this.right = 0;
        this.bottom = 0;
    }

    public IntRect(int l, int t, int r, int b) {
        this.left = l;
        this.top = t;
        this.right = r;
        this.bottom = b;
    }

    public IntRect(IntRect ir) {
        this.left = ir.left;
        this.top = ir.top;
        this.right = ir.right;
        this.bottom = ir.bottom;
    }
}
