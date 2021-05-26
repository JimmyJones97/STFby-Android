package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgArc extends DwgObject {
    private double[] center;
    private double endAngle;
    private double[] extrusion;
    private double initAngle;
    private double radius;
    private double thickness;

    public void readDwgArcV15(int[] data, int offset) throws Exception {
        double val;
        double x;
        double y;
        double z;
        Vector v = DwgUtil.getBitDouble(data, readObjectHeaderV15(data, offset));
        int bitPos = ((Integer) v.get(0)).intValue();
        double x2 = ((Double) v.get(1)).doubleValue();
        Vector v2 = DwgUtil.getBitDouble(data, bitPos);
        int bitPos2 = ((Integer) v2.get(0)).intValue();
        double y2 = ((Double) v2.get(1)).doubleValue();
        Vector v3 = DwgUtil.getBitDouble(data, bitPos2);
        int bitPos3 = ((Integer) v3.get(0)).intValue();
        this.center = new double[]{x2, y2, ((Double) v3.get(1)).doubleValue()};
        Vector v4 = DwgUtil.getBitDouble(data, bitPos3);
        int bitPos4 = ((Integer) v4.get(0)).intValue();
        this.radius = ((Double) v4.get(1)).doubleValue();
        Vector v5 = DwgUtil.testBit(data, bitPos4);
        int bitPos5 = ((Integer) v5.get(0)).intValue();
        if (((Boolean) v5.get(1)).booleanValue()) {
            val = 0.0d;
        } else {
            Vector v6 = DwgUtil.getBitDouble(data, bitPos5);
            bitPos5 = ((Integer) v6.get(0)).intValue();
            val = ((Double) v6.get(1)).doubleValue();
        }
        this.thickness = val;
        Vector v7 = DwgUtil.testBit(data, bitPos5);
        int bitPos6 = ((Integer) v7.get(0)).intValue();
        if (((Boolean) v7.get(1)).booleanValue()) {
            y = 0.0d;
            x = 0.0d;
            z = 1.0d;
        } else {
            Vector v8 = DwgUtil.getBitDouble(data, bitPos6);
            int bitPos7 = ((Integer) v8.get(0)).intValue();
            x = ((Double) v8.get(1)).doubleValue();
            Vector v9 = DwgUtil.getBitDouble(data, bitPos7);
            int bitPos8 = ((Integer) v9.get(0)).intValue();
            y = ((Double) v9.get(1)).doubleValue();
            Vector v10 = DwgUtil.getBitDouble(data, bitPos8);
            bitPos6 = ((Integer) v10.get(0)).intValue();
            z = ((Double) v10.get(1)).doubleValue();
        }
        this.extrusion = new double[]{x, y, z};
        Vector v11 = DwgUtil.getBitDouble(data, bitPos6);
        int bitPos9 = ((Integer) v11.get(0)).intValue();
        this.initAngle = ((Double) v11.get(1)).doubleValue();
        Vector v12 = DwgUtil.getBitDouble(data, bitPos9);
        int bitPos10 = ((Integer) v12.get(0)).intValue();
        this.endAngle = ((Double) v12.get(1)).doubleValue();
        readObjectTailV15(data, bitPos10);
    }

    public double[] getCenter() {
        return this.center;
    }

    public void setCenter(double[] center2) {
        this.center = center2;
    }

    public double getEndAngle() {
        return this.endAngle;
    }

    public void setEndAngle(double endAngle2) {
        this.endAngle = endAngle2;
    }

    public double getInitAngle() {
        return this.initAngle;
    }

    public void setInitAngle(double initAngle2) {
        this.initAngle = initAngle2;
    }

    public double getRadius() {
        return this.radius;
    }

    public void setRadius(double radius2) {
        this.radius = radius2;
    }

    public double[] getExtrusion() {
        return this.extrusion;
    }

    public Object clone() {
        DwgArc dwgArc = new DwgArc();
        dwgArc.setType(this.type);
        dwgArc.setHandle(this.handle);
        dwgArc.setVersion(this.version);
        dwgArc.setMode(this.mode);
        dwgArc.setLayerHandle(this.layerHandle);
        dwgArc.setColor(this.color);
        dwgArc.setNumReactors(this.numReactors);
        dwgArc.setNoLinks(this.noLinks);
        dwgArc.setLinetypeFlags(this.linetypeFlags);
        dwgArc.setPlotstyleFlags(this.plotstyleFlags);
        dwgArc.setSizeInBits(this.sizeInBits);
        dwgArc.setExtendedData(this.extendedData);
        dwgArc.setGraphicData(this.graphicData);
        dwgArc.setCenter(this.center);
        dwgArc.setRadius(this.radius);
        dwgArc.setThickness(this.thickness);
        dwgArc.setExtrusion(this.extrusion);
        dwgArc.setInitAngle(this.initAngle);
        dwgArc.setEndAngle(this.endAngle);
        return dwgArc;
    }

    public double getThickness() {
        return this.thickness;
    }

    public void setThickness(double thickness2) {
        this.thickness = thickness2;
    }

    public void setExtrusion(double[] extrusion2) {
        this.extrusion = extrusion2;
    }
}
