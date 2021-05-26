package  com.xzy.forestSystem.gogisapi.IOData.DXF.bean;

import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.Entity;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.UnexpectedElement;

public class Point extends Entity {
    public String LName;
    public double PointX;
    public double PointY;
    public double PointZ = 0.0d;
    public String colornum;
    public String lwidth;

    public Point() {
    }

    public String toString() {
        return String.valueOf(this.LName) + " (" + this.PointX + "," + this.PointY + ") " + this.colornum + " " + this.lwidth;
    }

    public Point(double x, double y, String layer) throws UnexpectedElement {
        super("POINT", layer);
        int[] it;
        this.PointX = x;
        this.PointY = y;
        for (int i : new int[]{10, 20, 30, 39, 210, 230, 50}) {
            this.dataAcceptanceList.add(Integer.valueOf(i));
        }
        AddReplace(10, Double.valueOf(x));
        AddReplace(20, Double.valueOf(y));
    }
}
