package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgLine extends DwgObject {
    private double[] extrusion;

    /* renamed from: p1 */
    private double[] f487p1;

    /* renamed from: p2 */
    private double[] f488p2;
    private double thickness;
    private boolean zflag = false;

    public void readDwgLineV15(int[] data, int offset) throws Exception {
        double[] p1;
        double[] p2;
        double val;
        double x;
        double y;
        double z;
        Vector v = DwgUtil.testBit(data, readObjectHeaderV15(data, offset));
        int bitPos = ((Integer) v.get(0)).intValue();
        this.zflag = ((Boolean) v.get(1)).booleanValue();
        Vector v2 = DwgUtil.getRawDouble(data, bitPos);
        int bitPos2 = ((Integer) v2.get(0)).intValue();
        double x1 = ((Double) v2.get(1)).doubleValue();
        Vector v3 = DwgUtil.getDefaultDouble(data, bitPos2, x1);
        int bitPos3 = ((Integer) v3.get(0)).intValue();
        double x2 = ((Double) v3.get(1)).doubleValue();
        Vector v4 = DwgUtil.getRawDouble(data, bitPos3);
        int bitPos4 = ((Integer) v4.get(0)).intValue();
        double y1 = ((Double) v4.get(1)).doubleValue();
        Vector v5 = DwgUtil.getDefaultDouble(data, bitPos4, y1);
        int bitPos5 = ((Integer) v5.get(0)).intValue();
        double y2 = ((Double) v5.get(1)).doubleValue();
        if (!this.zflag) {
            Vector v6 = DwgUtil.getRawDouble(data, bitPos5);
            int bitPos6 = ((Integer) v6.get(0)).intValue();
            double z1 = ((Double) v6.get(1)).doubleValue();
            Vector v7 = DwgUtil.getDefaultDouble(data, bitPos6, z1);
            bitPos5 = ((Integer) v7.get(0)).intValue();
            p1 = new double[]{x1, y1, z1};
            p2 = new double[]{x2, y2, ((Double) v7.get(1)).doubleValue()};
        } else {
            p1 = new double[]{x1, y1};
            p2 = new double[]{x2, y2};
        }
        this.f487p1 = p1;
        this.f488p2 = p2;
        Vector v8 = DwgUtil.testBit(data, bitPos5);
        int bitPos7 = ((Integer) v8.get(0)).intValue();
        if (((Boolean) v8.get(1)).booleanValue()) {
            val = 0.0d;
        } else {
            Vector v9 = DwgUtil.getBitDouble(data, bitPos7);
            bitPos7 = ((Integer) v9.get(0)).intValue();
            val = ((Double) v9.get(1)).doubleValue();
        }
        this.thickness = val;
        Vector v10 = DwgUtil.testBit(data, bitPos7);
        int bitPos8 = ((Integer) v10.get(0)).intValue();
        if (((Boolean) v10.get(1)).booleanValue()) {
            y = 0.0d;
            x = 0.0d;
            z = 1.0d;
        } else {
            Vector v11 = DwgUtil.getBitDouble(data, bitPos8);
            int bitPos9 = ((Integer) v11.get(0)).intValue();
            x = ((Double) v11.get(1)).doubleValue();
            Vector v12 = DwgUtil.getBitDouble(data, bitPos9);
            int bitPos10 = ((Integer) v12.get(0)).intValue();
            y = ((Double) v12.get(1)).doubleValue();
            Vector v13 = DwgUtil.getBitDouble(data, bitPos10);
            bitPos8 = ((Integer) v13.get(0)).intValue();
            z = ((Double) v13.get(1)).doubleValue();
        }
        this.extrusion = new double[]{x, y, z};
        readObjectTailV15(data, bitPos8);
    }

    public double[] getP1() {
        return this.f487p1;
    }

    public void setP1(double[] p1) {
        this.f487p1 = p1;
    }

    public double[] getP2() {
        return this.f488p2;
    }

    public void setP2(double[] p2) {
        this.f488p2 = p2;
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

    public Object clone() {
        DwgLine dwgLine = new DwgLine();
        dwgLine.setType(this.type);
        dwgLine.setHandle(this.handle);
        dwgLine.setVersion(this.version);
        dwgLine.setMode(this.mode);
        dwgLine.setLayerHandle(this.layerHandle);
        dwgLine.setColor(this.color);
        dwgLine.setNumReactors(this.numReactors);
        dwgLine.setNoLinks(this.noLinks);
        dwgLine.setLinetypeFlags(this.linetypeFlags);
        dwgLine.setPlotstyleFlags(this.plotstyleFlags);
        dwgLine.setSizeInBits(this.sizeInBits);
        dwgLine.setExtendedData(this.extendedData);
        dwgLine.setGraphicData(this.graphicData);
        dwgLine.setP1(this.f487p1);
        dwgLine.setP2(this.f488p2);
        dwgLine.setThickness(this.thickness);
        dwgLine.setExtrusion(this.extrusion);
        return dwgLine;
    }

    public boolean isZflag() {
        return this.zflag;
    }

    public void setZflag(boolean zflag2) {
        this.zflag = zflag2;
    }
}
