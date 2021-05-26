package  com.xzy.forestSystem.gogisapi.IOData.DXF.bean;

import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.Entity;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.UnexpectedElement;

public class LwPolyLine extends Entity {
    public String Elevation = "0";
    public int Flag;
    public String LName;
    public int PointCount;
    public String colornum;
    public double[] converxity;
    public String lwidth;
    public double[] pointx;
    public double[] pointy;

    public LwPolyLine() {
    }

    public LwPolyLine(String layer, int PointCount2, int Flag2) throws UnexpectedElement {
        super("LWPOLYLINE", layer);
        int[] it;
        for (int i : new int[]{5, 330, 100, 90, 70, 43, 10, 20, 38, 39, 91, 40, 41, 42, 210, 220, 230}) {
            this.dataAcceptanceList.add(Integer.valueOf(i));
        }
        AddReplace(100, "AcDbPolyline");
        AddReplace(90, Integer.valueOf(PointCount2));
        AddReplace(70, Short.valueOf((short) Flag2));
        AddReplace(43, Double.valueOf(0.0d));
        AddElement(new SeqEnd(layer));
    }

    public void AddVertex(Vertex v) throws UnexpectedElement {
        InsertElement(ElementCount() - 1, v);
    }

    public String toString() {
        String str = String.valueOf(this.LName) + " " + this.colornum + " " + this.lwidth + " " + this.PointCount + " " + this.Flag + " ";
        for (int i = 0; i < this.pointx.length; i++) {
            str = String.valueOf(str) + "(" + this.pointx[i] + "," + this.pointy[i] + ") ";
        }
        return str;
    }
}
