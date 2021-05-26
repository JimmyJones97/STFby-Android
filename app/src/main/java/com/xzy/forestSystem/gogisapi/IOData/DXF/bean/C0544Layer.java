package  com.xzy.forestSystem.gogisapi.IOData.DXF.bean;

import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.TableEntry;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.UnexpectedElement;

/* renamed from:  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.Layer */
public class C0544Layer extends TableEntry {
    public String colornum;
    public String lstyle;
    public String lwidth;
    public String name;

    public C0544Layer() {
    }

    public String toString() {
        return String.valueOf(this.name) + " " + this.colornum + " " + this.lstyle + " " + this.lwidth;
    }

    public C0544Layer(String name2, short i, String linetype) throws UnexpectedElement {
        super("LAYER");
        int[] it;
        this.name = name2;
        for (int i2 : new int[]{2, 70, 62, 6, 290, 370, 390, 61}) {
            this.dataAcceptanceList.add(Integer.valueOf(i2));
        }
        AddReplace(70, (short) 0);
        AddReplace(2, name2);
        AddReplace(62, Short.valueOf(i));
        AddReplace(61, (short) 0);
    }
}
