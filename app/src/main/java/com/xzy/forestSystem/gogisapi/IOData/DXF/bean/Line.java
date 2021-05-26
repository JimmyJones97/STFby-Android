package  com.xzy.forestSystem.gogisapi.IOData.DXF.bean;

import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.Entity;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.UnexpectedElement;

public class Line extends Entity {
    public String EndX;
    public String EndY;
    public String LName;
    public String StartX;
    public String StartY;
    public String colornum;
    public String lwidth;

    public Line() {
    }

    public String toString() {
        return String.valueOf(this.LName) + " (" + this.StartX + "," + this.StartY + ") (" + this.EndX + "," + this.EndY + ") " + this.colornum + " " + this.lwidth;
    }

    public Line(String layer, double xi, double yi, double xf, double yf) throws UnexpectedElement {
        super("LINE", layer);
        int[] it;
        this.LName = layer;
        for (int i : new int[]{39, 10, 20, 30, 11, 21, 31, 210, 220, 230}) {
            this.dataAcceptanceList.add(Integer.valueOf(i));
        }
        AddReplace(10, Double.valueOf(xi));
        AddReplace(20, Double.valueOf(yi));
        AddReplace(11, Double.valueOf(xf));
        AddReplace(21, Double.valueOf(yf));
    }

    public void setInitialPoint(double x, double y) throws UnexpectedElement {
        AddReplace(10, Double.valueOf(x));
        AddReplace(20, Double.valueOf(x));
    }

    public void setFinalPoint(double x, double y) throws UnexpectedElement {
        AddReplace(11, Double.valueOf(x));
        AddReplace(21, Double.valueOf(x));
    }
}
