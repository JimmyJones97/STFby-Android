package  com.xzy.forestSystem.gogisapi.IOData.DXF.bean;

import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.Data;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.Entity;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.UnexpectedElement;

public class Insert extends Entity {
    public Insert(String block, double x, double y, String layer) throws UnexpectedElement {
        super("INSERT", layer);
        int[] it;
        for (int i : new int[]{66, 2, 10, 20, 30, 41, 42, 43, 50, 70, 71, 44, 45, 210, 220, 230}) {
            this.dataAcceptanceList.add(Integer.valueOf(i));
        }
        AddData(new Data(2, block));
        AddData(new Data(10, Double.valueOf(x)));
        AddData(new Data(20, Double.valueOf(y)));
    }
}
