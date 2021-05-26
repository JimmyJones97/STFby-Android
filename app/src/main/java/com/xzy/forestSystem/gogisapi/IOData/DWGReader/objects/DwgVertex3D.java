package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgVertex3D extends DwgObject {
    private int flags;
    private double[] point;

    public void readDwgVertex3DV15(int[] data, int offset) throws Exception {
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
        readObjectTailV15(data, bitPos4);
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
