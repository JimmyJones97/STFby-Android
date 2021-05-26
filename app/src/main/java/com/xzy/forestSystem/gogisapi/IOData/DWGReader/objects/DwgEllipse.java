package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgEllipse extends DwgObject {
    private double axisRatio;
    private double[] center;
    private double endAngle;
    private double[] extrusion;
    private double initAngle;
    private double[] majorAxisVector;

    public void readDwgEllipseV15(int[] data, int offset) throws Exception {
        Vector v = DwgUtil.getBitDouble(data, readObjectHeaderV15(data, offset));
        int bitPos = ((Integer) v.get(0)).intValue();
        double x = ((Double) v.get(1)).doubleValue();
        Vector v2 = DwgUtil.getBitDouble(data, bitPos);
        int bitPos2 = ((Integer) v2.get(0)).intValue();
        double y = ((Double) v2.get(1)).doubleValue();
        Vector v3 = DwgUtil.getBitDouble(data, bitPos2);
        int bitPos3 = ((Integer) v3.get(0)).intValue();
        this.center = new double[]{x, y, ((Double) v3.get(1)).doubleValue()};
        Vector v4 = DwgUtil.getBitDouble(data, bitPos3);
        int bitPos4 = ((Integer) v4.get(0)).intValue();
        double x2 = ((Double) v4.get(1)).doubleValue();
        Vector v5 = DwgUtil.getBitDouble(data, bitPos4);
        int bitPos5 = ((Integer) v5.get(0)).intValue();
        double y2 = ((Double) v5.get(1)).doubleValue();
        Vector v6 = DwgUtil.getBitDouble(data, bitPos5);
        int bitPos6 = ((Integer) v6.get(0)).intValue();
        this.majorAxisVector = new double[]{x2, y2, ((Double) v6.get(1)).doubleValue()};
        Vector v7 = DwgUtil.getBitDouble(data, bitPos6);
        int bitPos7 = ((Integer) v7.get(0)).intValue();
        double x3 = ((Double) v7.get(1)).doubleValue();
        Vector v8 = DwgUtil.getBitDouble(data, bitPos7);
        int bitPos8 = ((Integer) v8.get(0)).intValue();
        double y3 = ((Double) v8.get(1)).doubleValue();
        Vector v9 = DwgUtil.getBitDouble(data, bitPos8);
        int bitPos9 = ((Integer) v9.get(0)).intValue();
        this.extrusion = new double[]{x3, y3, ((Double) v9.get(1)).doubleValue()};
        Vector v10 = DwgUtil.getBitDouble(data, bitPos9);
        int bitPos10 = ((Integer) v10.get(0)).intValue();
        this.axisRatio = ((Double) v10.get(1)).doubleValue();
        Vector v11 = DwgUtil.getBitDouble(data, bitPos10);
        int bitPos11 = ((Integer) v11.get(0)).intValue();
        this.initAngle = ((Double) v11.get(1)).doubleValue();
        Vector v12 = DwgUtil.getBitDouble(data, bitPos11);
        int bitPos12 = ((Integer) v12.get(0)).intValue();
        this.endAngle = ((Double) v12.get(1)).doubleValue();
        readObjectTailV15(data, bitPos12);
    }

    public double getAxisRatio() {
        return this.axisRatio;
    }

    public void setAxisRatio(double axisRatio2) {
        this.axisRatio = axisRatio2;
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

    public double[] getMajorAxisVector() {
        return this.majorAxisVector;
    }

    public void setMajorAxisVector(double[] majorAxisVector2) {
        this.majorAxisVector = majorAxisVector2;
    }

    public double[] getExtrusion() {
        return this.extrusion;
    }

    public Object clone() {
        DwgEllipse dwgEllipse = new DwgEllipse();
        dwgEllipse.setType(this.type);
        dwgEllipse.setHandle(this.handle);
        dwgEllipse.setVersion(this.version);
        dwgEllipse.setMode(this.mode);
        dwgEllipse.setLayerHandle(this.layerHandle);
        dwgEllipse.setColor(this.color);
        dwgEllipse.setNumReactors(this.numReactors);
        dwgEllipse.setNoLinks(this.noLinks);
        dwgEllipse.setLinetypeFlags(this.linetypeFlags);
        dwgEllipse.setPlotstyleFlags(this.plotstyleFlags);
        dwgEllipse.setSizeInBits(this.sizeInBits);
        dwgEllipse.setExtendedData(this.extendedData);
        dwgEllipse.setGraphicData(this.graphicData);
        dwgEllipse.setCenter(this.center);
        dwgEllipse.setMajorAxisVector(this.majorAxisVector);
        dwgEllipse.setExtrusion(this.extrusion);
        dwgEllipse.setAxisRatio(this.axisRatio);
        dwgEllipse.setInitAngle(this.initAngle);
        dwgEllipse.setEndAngle(this.endAngle);
        return dwgEllipse;
    }

    public void setExtrusion(double[] extrusion2) {
        this.extrusion = extrusion2;
    }
}
