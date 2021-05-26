package  com.xzy.forestSystem.gogisapi.IOData.DXF.bean;

import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.Entity;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.UnexpectedElement;

public class Vertex extends Entity {
    public Vertex(double x, double y, String layer) throws UnexpectedElement {
        super("VERTEX", layer);
        int[] it;
        for (int i : new int[]{10, 20, 30, 70, 40, 41, 42, 50, 71, 72, 73, 74}) {
            this.dataAcceptanceList.add(Integer.valueOf(i));
        }
        AddReplace(10, Double.valueOf(x));
        AddReplace(20, Double.valueOf(y));
    }
}
