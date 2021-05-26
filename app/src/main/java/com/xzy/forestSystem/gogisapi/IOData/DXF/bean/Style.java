package  com.xzy.forestSystem.gogisapi.IOData.DXF.bean;

import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.TableEntry;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.UnexpectedElement;

public class Style extends TableEntry {
    public Style(String name, boolean shape, double height, double width, double obliqueAngle, boolean backward, boolean upsidedown, double lastHeightUsed, String primaryFontFile) throws UnexpectedElement {
        super("STYLE");
        int[] it;
        for (int i : new int[]{2, 70, 40, 41, 50, 71, 42, 3, 4}) {
            this.dataAcceptanceList.add(Integer.valueOf(i));
        }
        AddData(2, name);
        AddData(70, Short.valueOf(shape ? (short) 1 : 0));
        AddData(40, Double.valueOf(height));
        AddData(41, Double.valueOf(width));
        AddData(50, Double.valueOf(obliqueAngle));
        short c71 = backward ? (short) 2 : 0;
        AddData(71, Short.valueOf(upsidedown ? (short) (c71 + 4) : c71));
        if (height == 0.0d) {
            AddData(42, Double.valueOf(lastHeightUsed));
        }
        AddData(3, primaryFontFile);
    }
}
