package  com.xzy.forestSystem.gogisapi.Geodatabase;

import java.io.DataInputStream;
import java.util.List;

/* compiled from: Shapefile */
class Polygon extends Polyline {
    double[] Box = new double[4];
    int NumParts;
    int NumPoints;
    List<Integer> Parts;
    List<Point> Points;

    Polygon() {
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.Polyline,  com.xzy.forestSystem.gogisapi.Geodatabase.ShpObj
    public boolean ReadData(DataInputStream dis) {
        return super.ReadData(dis);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.Polyline,  com.xzy.forestSystem.gogisapi.Geodatabase.ShpObj
    public void CalculateLength() {
        super.CalculateLength();
    }
}
