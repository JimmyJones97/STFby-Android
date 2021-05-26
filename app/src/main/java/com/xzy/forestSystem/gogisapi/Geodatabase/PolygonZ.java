package  com.xzy.forestSystem.gogisapi.Geodatabase;

import java.io.DataInputStream;

/* compiled from: Shapefile */
class PolygonZ extends PolylineZ {
    PolygonZ() {
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.PolylineZ,  com.xzy.forestSystem.gogisapi.Geodatabase.Polyline,  com.xzy.forestSystem.gogisapi.Geodatabase.ShpObj
    public boolean ReadData(DataInputStream dis) {
        return super.ReadData(dis);
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.PolylineZ,  com.xzy.forestSystem.gogisapi.Geodatabase.Polyline,  com.xzy.forestSystem.gogisapi.Geodatabase.ShpObj
    public void CalculateLength() {
        super.CalculateLength();
    }
}
