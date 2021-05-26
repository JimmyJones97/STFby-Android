package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgCircle extends DwgObject {
    private double[] center;
    private double[] extrusion;
    private double radius;
    private double thickness;

    public void readDwgCircleV15(int[] data, int offset) throws Exception {
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
        readObjectTailV15(data, bitPos6);
    }

    public double[] getCenter() {
        return this.center;
    }

    public void setCenter(double[] center2) {
        this.center = center2;
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
        DwgCircle dwgCircle = new DwgCircle();
        dwgCircle.setType(this.type);
        dwgCircle.setHandle(this.handle);
        dwgCircle.setVersion(this.version);
        dwgCircle.setMode(this.mode);
        dwgCircle.setLayerHandle(this.layerHandle);
        dwgCircle.setColor(this.color);
        dwgCircle.setNumReactors(this.numReactors);
        dwgCircle.setNoLinks(this.noLinks);
        dwgCircle.setLinetypeFlags(this.linetypeFlags);
        dwgCircle.setPlotstyleFlags(this.plotstyleFlags);
        dwgCircle.setSizeInBits(this.sizeInBits);
        dwgCircle.setExtendedData(this.extendedData);
        dwgCircle.setGraphicData(this.graphicData);
        dwgCircle.setCenter(this.center);
        dwgCircle.setRadius(this.radius);
        dwgCircle.setThickness(this.thickness);
        dwgCircle.setExtrusion(this.extrusion);
        return dwgCircle;
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
