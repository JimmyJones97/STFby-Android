package  com.xzy.forestSystem.gogisapi.IOData.DXF.bean;

import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.Entity;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.UnexpectedElement;

public class Line3D extends Entity {
    public Line3D(String layer, double xi, double yi, double zi, double xf, double yf, double zf) throws UnexpectedElement {
        super("LINE", layer);
        int[] it;
        for (int i : new int[]{39, 10, 20, 30, 11, 21, 31, 210, 220, 230}) {
            this.dataAcceptanceList.add(Integer.valueOf(i));
        }
        AddReplace(10, Double.valueOf(xi));
        AddReplace(20, Double.valueOf(yi));
        AddReplace(30, Double.valueOf(zi));
        AddReplace(11, Double.valueOf(xf));
        AddReplace(21, Double.valueOf(yf));
        AddReplace(31, Double.valueOf(zf));
    }

    public void setInitialPoint(double x, double y, double z) throws UnexpectedElement {
        AddReplace(10, Double.valueOf(x));
        AddReplace(20, Double.valueOf(x));
        AddReplace(30, Double.valueOf(z));
    }

    public void setFinalPoint(double x, double y, double z) throws UnexpectedElement {
        AddReplace(11, Double.valueOf(x));
        AddReplace(21, Double.valueOf(x));
        AddReplace(31, Double.valueOf(z));
    }
}
