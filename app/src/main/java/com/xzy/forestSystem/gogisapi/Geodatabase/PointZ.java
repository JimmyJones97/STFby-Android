package  com.xzy.forestSystem.gogisapi.Geodatabase;

import  com.xzy.forestSystem.gogisapi.Common.BitConverter;
import java.io.DataInputStream;

/* compiled from: Shapefile */
class PointZ extends ShpObj {

    /* renamed from: M */
    double f480M;

    /* renamed from: X */
    double f481X;

    /* renamed from: Y */
    double f482Y;

    /* renamed from: Z */
    double f483Z;

    PointZ() {
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ShpObj
    public boolean ReadData(DataInputStream dis) {
        try {
            this.RecordNumber = dis.readInt();
            this.ContentLength = dis.readInt();
            dis.readInt();
            this.f481X = BitConverter.readLittleDouble(dis);
            this.f482Y = BitConverter.readLittleDouble(dis);
            this.f483Z = BitConverter.readLittleDouble(dis);
            this.f480M = BitConverter.readLittleDouble(dis);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ShpObj
    public void CalculateLength() {
        this.ContentLength = 36;
    }
}
