package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgVertex2D extends DwgObject {
    private double bulge;
    private double endWidth;
    private int flags;
    private double initWidth;
    private double[] point;
    private double tangentDir;

    public void readDwgVertex2DV15(int[] data, int offset) throws Exception {
        double ew;
        Vector v = DwgUtil.getRawChar(data, readObjectHeaderV15(data, offset));
        int bitPos = ((Integer) v.get(0)).intValue();
        this.flags = ((Integer) v.get(1)).intValue();
        Vector v2 = DwgUtil.getBitDouble(data, bitPos);
        int bitPos2 = ((Integer) v2.get(0)).intValue();
        double x = ((Double) v2.get(1)).doubleValue();
        Vector v3 = DwgUtil.getBitDouble(data, bitPos2);
        int bitPos3 = ((Integer) v3.get(0)).intValue();
        double y = ((Double) v3.get(1)).doubleValue();
        Vector v4 = DwgUtil.getBitDouble(data, bitPos3);
        int bitPos4 = ((Integer) v4.get(0)).intValue();
        double z = ((Double) v4.get(1)).doubleValue();
        double[] dArr = {x, y, z};
        this.point = new double[]{x, y, z};
        Vector v5 = DwgUtil.getBitDouble(data, bitPos4);
        int bitPos5 = ((Integer) v5.get(0)).intValue();
        double sw = ((Double) v5.get(1)).doubleValue();
        if (sw < 0.0d) {
            ew = Math.abs(sw);
            sw = ew;
        } else {
            Vector v6 = DwgUtil.getBitDouble(data, bitPos5);
            bitPos5 = ((Integer) v6.get(0)).intValue();
            ew = ((Double) v6.get(1)).doubleValue();
        }
        this.initWidth = sw;
        this.endWidth = ew;
        Vector v7 = DwgUtil.getBitDouble(data, bitPos5);
        int bitPos6 = ((Integer) v7.get(0)).intValue();
        this.bulge = ((Double) v7.get(1)).doubleValue();
        Vector v8 = DwgUtil.getBitDouble(data, bitPos6);
        int bitPos7 = ((Integer) v8.get(0)).intValue();
        this.tangentDir = ((Double) v8.get(1)).doubleValue();
        readObjectTailV15(data, bitPos7);
    }

    public double getBulge() {
        return this.bulge;
    }

    public void setBulge(double bulge2) {
        this.bulge = bulge2;
    }

    public int getFlags() {
        return this.flags;
    }

    public void setFlags(int flags2) {
        this.flags = flags2;
    }

    public double[] getPoint() {
        return this.point;
    }

    public void setPoint(double[] point2) {
        this.point = point2;
    }
}
