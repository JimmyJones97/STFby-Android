package  com.xzy.forestSystem.gogisapi.Geodatabase;

import  com.xzy.forestSystem.gogisapi.Common.BitConverter;
import com.xzy.forestSystem.gogisapi.Geometry.Coordinate;

import java.io.DataInputStream;
import java.io.EOFException;

/* compiled from: Shapefile */
class Point extends ShpObj {

    /* renamed from: X */
    double f478X;

    /* renamed from: Y */
    double f479Y;


    @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ShpObj
    public boolean ReadData(DataInputStream dis) throws EOFException {
        try {
            this.RecordNumber = dis.readInt();
            this.ContentLength = dis.readInt();
            dis.readInt();
            this.f478X = BitConverter.readLittleDouble(dis);
            this.f479Y = BitConverter.readLittleDouble(dis);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ShpObj
    public void CalculateLength() {
        this.ContentLength = 10;
    }
}
