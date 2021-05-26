package  com.xzy.forestSystem.gogisapi.Display;

/* compiled from: TextPositionAdjust */
/* access modifiers changed from: package-private */
public class RectangleF {
    public float Height = 0.0f;
    public float Width = 0.0f;

    /* renamed from: X */
    public float f467X = 0.0f;

    /* renamed from: Y */
    public float f468Y = 0.0f;

    public RectangleF() {
    }

    public RectangleF(float x, float y, float w, float h) {
        this.f467X = x;
        this.f468Y = y;
        this.Width = w;
        this.Height = h;
    }

    public boolean IntersectsWith(RectangleF rect) {
        if (this.f467X > rect.f467X + rect.Width || this.f467X + this.Width < rect.f467X || this.f468Y > rect.f468Y + rect.Height || this.f468Y + this.Height < rect.f468Y) {
            return false;
        }
        return true;
    }
}
