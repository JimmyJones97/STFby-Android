package  com.xzy.forestSystem.gogisapi.IOData.DXF.bean;

import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.Entity;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.UnexpectedElement;
import java.util.ArrayList;

public class PolyLine extends Entity {
    public String Elevation = "0.0";
    public int Flag;
    public String LName;
    public String colornum;
    public String lwidth;
    public ArrayList<Point> pointList = new ArrayList<>();

    public PolyLine(String layer, short pfg) throws UnexpectedElement {
        super("POLYLINE", layer);
        int[] it;
        for (int i : new int[]{66, 10, 20, 30, 39, 70, 40, 41, 71, 72, 73, 74, 75, 210, 220, 230, 370, 62}) {
            this.dataAcceptanceList.add(Integer.valueOf(i));
        }
        AddReplace(70, Short.valueOf(pfg));
        AddElement(new SeqEnd(layer));
        AddReplace(66, (short) 1);
    }

    public PolyLine() {
    }

    public void AddVertex(Vertex v) throws UnexpectedElement {
        InsertElement(ElementCount() - 1, v);
    }

    public void AddVertex(double x, double y) throws UnexpectedElement {
        AddVertex(new Vertex(x, y, getLayer()));
    }

    public void setLwidth(String lwidth2) throws UnexpectedElement {
        this.lwidth = lwidth2;
        AddReplace(62, Short.valueOf(lwidth2));
    }

    public void setColornum(String colornum2) throws UnexpectedElement {
        this.colornum = colornum2;
    }

    public String toString() {
        String s1 = String.valueOf(this.LName) + " " + this.colornum + " " + this.lwidth + " " + this.Flag + " ";
        for (int i = 0; i < this.pointList.size(); i++) {
            s1 = String.valueOf(s1) + "(" + this.pointList.get(i).PointX + "," + this.pointList.get(i).PointY + ") ";
        }
        return s1;
    }
}
