package  com.xzy.forestSystem.gogisapi.Geometry;

public class Size {
    int _Height;
    int _Width;

    public Size(int width, int height) {
        this._Width = width;
        this._Height = height;
    }

    public int getHeight() {
        return this._Height;
    }

    public int getWidth() {
        return this._Width;
    }
}
