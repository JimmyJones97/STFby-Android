package  com.xzy.forestSystem.gogisapi.IOData.DXF.bean;

import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.Entity;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.UnexpectedElement;

public class Arc extends Entity {
    public String CenterX;
    public String CenterY;
    public String EAngle;
    public String LName;
    public String Radius;
    public String SAngle;
    public String lwidth;

    public Arc() {
    }

    public Arc(double x, double y, double radius, double startAngle, double endAngle, String layer) throws UnexpectedElement {
        super("ARC", layer);
        int[] it;
        for (int i : new int[]{39, 10, 20, 30, 40, 100, 50, 51, 210, 220, 230}) {
            this.dataAcceptanceList.add(Integer.valueOf(i));
        }
        AddReplace(10, Double.valueOf(x));
        AddReplace(20, Double.valueOf(y));
        AddReplace(40, Double.valueOf(radius));
        AddReplace(50, Double.valueOf(startAngle));
        AddReplace(51, Double.valueOf(endAngle));
    }
}
