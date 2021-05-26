package  com.xzy.forestSystem.gogisapi.IOData.DXF.bean;

import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.TableEntry;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.UnexpectedElement;

public class LineType extends TableEntry {
    short nrElements;

    public LineType(String name, String description, double patternLength) throws UnexpectedElement {
        super("LTYPE");
        int[] it;
        for (int i : new int[]{2, 70, 3, 72, 73, 40, 49, 74, 75, 340, 46, 50, 44, 45, 9}) {
            this.dataAcceptanceList.add(Integer.valueOf(i));
        }
        AddReplace(2, name);
        AddData(70, (short) 0);
        AddData(3, description);
        AddData(72, (short) 65);
        this.nrElements = 0;
        AddReplace(73, Short.valueOf(this.nrElements));
        AddReplace(40, Double.valueOf(patternLength));
    }

    public void AddElement(double length) throws UnexpectedElement {
        AddData(49, Double.valueOf(length));
        this.nrElements = (short) (this.nrElements + 1);
        AddReplace(73, Short.valueOf(this.nrElements));
    }

    public void AddElement(double length, String text, boolean absoluteRotation, double rotation) throws UnexpectedElement {
        AddData(49, Double.valueOf(length));
        if (absoluteRotation) {
            AddData(74, (short) 3);
        } else {
            AddData(74, (short) 2);
        }
        AddData(75, (short) 0);
        AddData(50, Double.valueOf(rotation));
        AddData(9, text);
        this.nrElements = (short) (this.nrElements + 1);
        AddReplace(73, Short.valueOf(this.nrElements));
    }

    public void AddElement(double length, short shapeNumber, boolean absoluteRotation, double rotation) throws UnexpectedElement {
        AddData(49, Double.valueOf(length));
        if (absoluteRotation) {
            AddData(74, (short) 5);
        } else {
            AddData(74, (short) 4);
        }
        AddData(75, Short.valueOf(shapeNumber));
        if (rotation != -1000.0d) {
            AddData(50, Double.valueOf(rotation));
        }
        this.nrElements = (short) (this.nrElements + 1);
        AddReplace(73, Short.valueOf(this.nrElements));
    }
}
