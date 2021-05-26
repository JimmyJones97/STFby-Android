package  com.xzy.forestSystem.gogisapi.IOData.DXF.bean;

import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.Entity;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.UnexpectedElement;

public class Circle extends Entity {
    public String CenterX;
    public String CenterY;
    public String LName;
    public String Radius;
    public String lwidth;

    public Circle() {
    }

    public String toString() {
        return String.valueOf(this.LName) + " (" + this.CenterX + "," + this.CenterY + ") " + this.Radius + " " + this.lwidth;
    }

    public Circle(double x, double y, double radius, String layer) throws UnexpectedElement {
        super("CIRCLE", layer);
        int[] it;
        for (int i : new int[]{39, 10, 20, 30, 40, 210, 220, 230}) {
            this.dataAcceptanceList.add(Integer.valueOf(i));
        }
        AddReplace(10, Double.valueOf(x));
        AddReplace(20, Double.valueOf(y));
        AddReplace(40, Double.valueOf(radius));
    }
}
