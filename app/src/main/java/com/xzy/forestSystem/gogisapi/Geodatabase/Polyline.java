package  com.xzy.forestSystem.gogisapi.Geodatabase;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.BitConverter;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

/* compiled from: Shapefile */
class Polyline extends ShpObj {
    double[] Box = new double[4];
    int NumParts;
    int NumPoints;
    List<Integer> Parts;
    List<Point> Points;

    Polyline() {
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ShpObj
    public boolean ReadData(DataInputStream dis) {
        try {
            this.Box = new double[4];
            this.Parts = new ArrayList();
            this.Points = new ArrayList();
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
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ShpObj
    public void CalculateLength() {
        this.ContentLength = ((this.NumParts + this.NumPoints) * 2) + 22;
    }

    public List<Coordinate> GetCoordinates() {
        List<Coordinate> list = new ArrayList<>();
        if (this.Points != null) {
            for (Point tempPtn : this.Points) {
                list.add(new Coordinate(tempPtn.f478X, tempPtn.f479Y, 0.0d));
            }
        }
        return list;
    }

    public List<Coordinate> GetXYCoordinates() {
        List<Coordinate> list = new ArrayList<>();
        if (this.Points != null) {
            for (Point tempPtn : this.Points) {
                list.add(PubVar.m_Workspace.GetCoorSystem().ConvertBLToXY(tempPtn.f478X, tempPtn.f479Y));
            }
        }
        return list;
    }
}
