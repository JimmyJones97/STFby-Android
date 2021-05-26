package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgPoint extends DwgObject {
    private double[] extrusion;
    private double[] point;
    private double thickness;
    private double xAxisAngle;

    public void readDwgPointV15(int[] data, int offset) throws Exception {
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
        this.point = new double[]{x2, y2, ((Double) v3.get(1)).doubleValue()};
        Vector v4 = DwgUtil.testBit(data, bitPos3);
        int bitPos4 = ((Integer) v4.get(0)).intValue();
        if (((Boolean) v4.get(1)).booleanValue()) {
            val = 0.0d;
        } else {
            Vector v5 = DwgUtil.getBitDouble(data, bitPos4);
            bitPos4 = ((Integer) v5.get(0)).intValue();
            val = ((Double) v5.get(1)).doubleValue();
        }
        this.thickness = val;
        Vector v6 = DwgUtil.testBit(data, bitPos4);
        int bitPos5 = ((Integer) v6.get(0)).intValue();
        if (((Boolean) v6.get(1)).booleanValue()) {
            y = 0.0d;
            x = 0.0d;
            z = 1.0d;
        } else {
            Vector v7 = DwgUtil.getBitDouble(data, bitPos5);
            int bitPos6 = ((Integer) v7.get(0)).intValue();
            x = ((Double) v7.get(1)).doubleValue();
            Vector v8 = DwgUtil.getBitDouble(data, bitPos6);
            int bitPos7 = ((Integer) v8.get(0)).intValue();
            y = ((Double) v8.get(1)).doubleValue();
            Vector v9 = DwgUtil.getBitDouble(data, bitPos7);
            bitPos5 = ((Integer) v9.get(0)).intValue();
            z = ((Double) v9.get(1)).doubleValue();
        }
        this.extrusion = new double[]{x, y, z};
        Vector v10 = DwgUtil.getBitDouble(data, bitPos5);
        int bitPos8 = ((Integer) v10.get(0)).intValue();
        this.xAxisAngle = ((Double) v10.get(1)).doubleValue();
        readObjectTailV15(data, bitPos8);
    }

    public double[] getPoint() {
        return this.point;
    }

    public void setPoint(double[] point2) {
        this.point = point2;
    }

    public double[] getExtrusion() {
        return this.extrusion;
    }

    public void setExtrusion(double[] extrusion2) {
        this.extrusion = extrusion2;
    }

    public double getThickness() {
        return this.thickness;
    }

    public void setThickness(double thickness2) {
        this.thickness = thickness2;
    }

    public double getXAxisAngle() {
        return this.xAxisAngle;
    }

    public void setXAxisAngle(double axisAngle) {
        this.xAxisAngle = axisAngle;
    }

    public Object clone() {
        DwgPoint dwgPoint = new DwgPoint();
        dwgPoint.setType(this.type);
        dwgPoint.setHandle(this.handle);
        dwgPoint.setVersion(this.version);
        dwgPoint.setMode(this.mode);
        dwgPoint.setLayerHandle(this.layerHandle);
        dwgPoint.setColor(this.color);
        dwgPoint.setNumReactors(this.numReactors);
        dwgPoint.setNoLinks(this.noLinks);
        dwgPoint.setLinetypeFlags(this.linetypeFlags);
        dwgPoint.setPlotstyleFlags(this.plotstyleFlags);
        dwgPoint.setSizeInBits(this.sizeInBits);
        dwgPoint.setExtendedData(this.extendedData);
        dwgPoint.setGraphicData(this.graphicData);
        dwgPoint.setPoint(this.point);
        dwgPoint.setThickness(this.thickness);
        dwgPoint.setXAxisAngle(this.xAxisAngle);
        dwgPoint.setExtrusion(this.extrusion);
        return dwgPoint;
    }
}
