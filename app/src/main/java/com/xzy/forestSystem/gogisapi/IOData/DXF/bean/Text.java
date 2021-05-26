package  com.xzy.forestSystem.gogisapi.IOData.DXF.bean;

import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.Entity;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.UnexpectedElement;

public class Text extends Entity {
    public double Height;
    public String LName;
    public double PointX;
    public double PointY;
    public String Value;

    public Text() {
    }

    public String toString() {
        return String.valueOf(this.LName) + " (" + this.PointX + "," + this.PointY + ") " + this.Value + " " + this.Height;
    }

    public Text(String text, double x, double y, double height, String layer) throws UnexpectedElement {
        super("TEXT", layer);
        int[] it;
        for (int i : new int[]{39, 10, 20, 30, 40, 1, 50, 41, 51, 7, 71, 72, 11, 21, 31, 210, 220, 230, 73}) {
            this.dataAcceptanceList.add(Integer.valueOf(i));
        }
        AddReplace(10, Double.valueOf(x));
        AddReplace(20, Double.valueOf(y));
        AddReplace(1, text);
        AddReplace(40, Double.valueOf(height));
    }

    public void SetHorizontalJustification(short flag) throws UnexpectedElement {
        AddReplace(72, Short.valueOf(flag));
    }

    public void SetVerticalJustification(short flag) throws UnexpectedElement {
        AddReplace(73, Short.valueOf(flag));
    }

    public void SetSecoundAlignament(double x, double y) throws UnexpectedElement {
        AddReplace(11, Double.valueOf(x));
        AddReplace(21, Double.valueOf(y));
    }
}
