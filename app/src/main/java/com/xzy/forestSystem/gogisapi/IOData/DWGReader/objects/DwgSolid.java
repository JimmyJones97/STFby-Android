package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgSolid extends DwgObject {
    private double[] corner1;
    private double[] corner2;
    private double[] corner3;
    private double[] corner4;
    private double elevation;
    private double[] extrusion;
    private double thickness;

    public void readDwgSolidV15(int[] data, int offset) throws Exception {
        double val;
        double x;
        double y;
        double z;
        Vector v = DwgUtil.testBit(data, readObjectHeaderV15(data, offset));
        int bitPos = ((Integer) v.get(0)).intValue();
        if (((Boolean) v.get(1)).booleanValue()) {
            val = 0.0d;
        } else {
            Vector v2 = DwgUtil.getBitDouble(data, bitPos);
            bitPos = ((Integer) v2.get(0)).intValue();
            val = ((Double) v2.get(1)).doubleValue();
        }
        this.thickness = val;
        Vector v3 = DwgUtil.getBitDouble(data, bitPos);
        int bitPos2 = ((Integer) v3.get(0)).intValue();
        double val2 = ((Double) v3.get(1)).doubleValue();
        this.elevation = val2;
        Vector v4 = DwgUtil.getRawDouble(data, bitPos2);
        int bitPos3 = ((Integer) v4.get(0)).intValue();
        double x2 = ((Double) v4.get(1)).doubleValue();
        Vector v5 = DwgUtil.getRawDouble(data, bitPos3);
        int bitPos4 = ((Integer) v5.get(0)).intValue();
        this.corner1 = new double[]{x2, ((Double) v5.get(1)).doubleValue(), val2};
        Vector v6 = DwgUtil.getRawDouble(data, bitPos4);
        int bitPos5 = ((Integer) v6.get(0)).intValue();
        double x3 = ((Double) v6.get(1)).doubleValue();
        Vector v7 = DwgUtil.getRawDouble(data, bitPos5);
        int bitPos6 = ((Integer) v7.get(0)).intValue();
        this.corner2 = new double[]{x3, ((Double) v7.get(1)).doubleValue(), val2};
        Vector v8 = DwgUtil.getRawDouble(data, bitPos6);
        int bitPos7 = ((Integer) v8.get(0)).intValue();
        double x4 = ((Double) v8.get(1)).doubleValue();
        Vector v9 = DwgUtil.getRawDouble(data, bitPos7);
        int bitPos8 = ((Integer) v9.get(0)).intValue();
        this.corner3 = new double[]{x4, ((Double) v9.get(1)).doubleValue(), val2};
        Vector v10 = DwgUtil.getRawDouble(data, bitPos8);
        int bitPos9 = ((Integer) v10.get(0)).intValue();
        double x5 = ((Double) v10.get(1)).doubleValue();
        Vector v11 = DwgUtil.getRawDouble(data, bitPos9);
        int bitPos10 = ((Integer) v11.get(0)).intValue();
        this.corner4 = new double[]{x5, ((Double) v11.get(1)).doubleValue(), val2};
        Vector v12 = DwgUtil.testBit(data, bitPos10);
        int bitPos11 = ((Integer) v12.get(0)).intValue();
        if (((Boolean) v12.get(1)).booleanValue()) {
            y = 0.0d;
            x = 0.0d;
            z = 1.0d;
        } else {
            Vector v13 = DwgUtil.getBitDouble(data, bitPos11);
            int bitPos12 = ((Integer) v13.get(0)).intValue();
            x = ((Double) v13.get(1)).doubleValue();
            Vector v14 = DwgUtil.getBitDouble(data, bitPos12);
            int bitPos13 = ((Integer) v14.get(0)).intValue();
            y = ((Double) v14.get(1)).doubleValue();
            Vector v15 = DwgUtil.getBitDouble(data, bitPos13);
            bitPos11 = ((Integer) v15.get(0)).intValue();
            z = ((Double) v15.get(1)).doubleValue();
        }
        this.extrusion = new double[]{x, y, z};
        readObjectTailV15(data, bitPos11);
    }

    public double[] getCorner1() {
        return this.corner1;
    }

    public void setCorner1(double[] corner12) {
        this.corner1 = corner12;
    }

    public double[] getCorner2() {
        return this.corner2;
    }

    public void setCorner2(double[] corner22) {
        this.corner2 = corner22;
    }

    public double[] getCorner3() {
        return this.corner3;
    }

    public void setCorner3(double[] corner32) {
        this.corner3 = corner32;
    }

    public double[] getCorner4() {
        return this.corner4;
    }

    public void setCorner4(double[] corner42) {
        this.corner4 = corner42;
    }

    public double getElevation() {
        return this.elevation;
    }

    public void setElevation(double elevation2) {
        this.elevation = elevation2;
    }

    public double[] getExtrusion() {
        return this.extrusion;
    }

    public Object clone() {
        DwgSolid dwgSolid = new DwgSolid();
        dwgSolid.setType(this.type);
        dwgSolid.setHandle(this.handle);
        dwgSolid.setVersion(this.version);
        dwgSolid.setMode(this.mode);
        dwgSolid.setLayerHandle(this.layerHandle);
        dwgSolid.setColor(this.color);
        dwgSolid.setNumReactors(this.numReactors);
        dwgSolid.setNoLinks(this.noLinks);
        dwgSolid.setLinetypeFlags(this.linetypeFlags);
        dwgSolid.setPlotstyleFlags(this.plotstyleFlags);
        dwgSolid.setSizeInBits(this.sizeInBits);
        dwgSolid.setExtendedData(this.extendedData);
        dwgSolid.setGraphicData(this.graphicData);
        dwgSolid.setThickness(this.thickness);
        dwgSolid.setElevation(this.elevation);
        dwgSolid.setCorner1(this.corner1);
        dwgSolid.setCorner2(this.corner2);
        dwgSolid.setCorner3(this.corner3);
        dwgSolid.setCorner4(this.corner4);
        dwgSolid.setExtrusion(this.extrusion);
        return dwgSolid;
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
