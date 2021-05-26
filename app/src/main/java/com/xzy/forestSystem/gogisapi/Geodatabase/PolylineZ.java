package  com.xzy.forestSystem.gogisapi.Geodatabase;

import  com.xzy.forestSystem.gogisapi.Common.BitConverter;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

/* compiled from: Shapefile */
class PolylineZ extends Polyline {
    List<Double> MArray;
    double MMax;
    double MMin;
    List<Double> ZArray;
    double ZMax;
    double ZMin;

    PolylineZ() {
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.Polyline,  com.xzy.forestSystem.gogisapi.Geodatabase.ShpObj
    public boolean ReadData(DataInputStream dis) {
        try {
            this.Box = new double[4];
            this.Parts = new ArrayList();
            this.Points = new ArrayList();
            this.ZArray = new ArrayList();
            this.MArray = new ArrayList();
            this.RecordNumber = dis.readInt();
            this.ContentLength = dis.readInt();
            dis.readInt();
            this.Box[0] = BitConverter.readLittleDouble(dis);
            this.Box[1] = BitConverter.readLittleDouble(dis);
            this.Box[2] = BitConverter.readLittleDouble(dis);
            this.Box[3] = BitConverter.readLittleDouble(dis);
            this.NumParts = BitConverter.readLittleInt(dis);
            this.NumPoints = BitConverter.readLittleInt(dis);
            for (int i = 0; i < this.NumParts; i++) {
                this.Parts.add(Integer.valueOf(BitConverter.readLittleInt(dis)));
            }
            for (int j = 0; j < this.NumPoints; j++) {
                Point tempPtn = new Point();
                tempPtn.f478X = BitConverter.readLittleDouble(dis);
                tempPtn.f479Y = BitConverter.readLittleDouble(dis);
                this.Points.add(tempPtn);
            }
            this.ZMin = BitConverter.readLittleDouble(dis);
            this.ZMax = BitConverter.readLittleDouble(dis);
            for (int j2 = 0; j2 < this.NumPoints; j2++) {
                this.ZArray.add(Double.valueOf(BitConverter.readLittleDouble(dis)));
            }
            this.MMin = BitConverter.readLittleDouble(dis);
            this.MMax = BitConverter.readLittleDouble(dis);
            for (int j3 = 0; j3 < this.NumPoints; j3++) {
                this.MArray.add(Double.valueOf(BitConverter.readLittleDouble(dis)));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.Polyline,  com.xzy.forestSystem.gogisapi.Geodatabase.ShpObj
    public void CalculateLength() {
        this.ContentLength = (this.NumParts * 2) + 38 + (this.NumPoints * 16);
    }
}
