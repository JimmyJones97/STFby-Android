package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.lang.reflect.Array;
import java.util.Vector;

public class DwgLwPolyline extends DwgObject {
    private double[] bulges;
    private double constWidth;
    private double elevation;
    private int flag;
    private double[] normal;
    private double thickness;
    private Point2D[] vertices;
    private double[][] widths;

    public void readDwgLwPolylineV15(int[] data, int offset) throws Exception {
        Vector v = DwgUtil.getBitShort(data, readObjectHeaderV15(data, offset));
        int bitPos = ((Integer) v.get(0)).intValue();
        int flag2 = ((Integer) v.get(1)).intValue();
        this.flag = flag2;
        if (flag2 >= 0) {
            double constWidth2 = 0.0d;
            if ((flag2 & 4) > 0) {
                Vector v2 = DwgUtil.getBitDouble(data, bitPos);
                bitPos = ((Integer) v2.get(0)).intValue();
                constWidth2 = ((Double) v2.get(1)).doubleValue();
            }
            this.constWidth = constWidth2;
            double elev = 0.0d;
            if ((flag2 & 8) > 0) {
                Vector v3 = DwgUtil.getBitDouble(data, bitPos);
                bitPos = ((Integer) v3.get(0)).intValue();
                elev = ((Double) v3.get(1)).doubleValue();
            }
            this.elevation = elev;
            double thickness2 = 0.0d;
            if ((flag2 & 2) > 0) {
                Vector v4 = DwgUtil.getBitDouble(data, bitPos);
                bitPos = ((Integer) v4.get(0)).intValue();
                thickness2 = ((Double) v4.get(1)).doubleValue();
            }
            this.thickness = thickness2;
            double nx = 0.0d;
            double ny = 0.0d;
            double nz = 0.0d;
            if ((flag2 & 1) > 0) {
                Vector v5 = DwgUtil.getBitDouble(data, bitPos);
                int bitPos2 = ((Integer) v5.get(0)).intValue();
                nx = ((Double) v5.get(1)).doubleValue();
                Vector v6 = DwgUtil.getBitDouble(data, bitPos2);
                int bitPos3 = ((Integer) v6.get(0)).intValue();
                ny = ((Double) v6.get(1)).doubleValue();
                Vector v7 = DwgUtil.getBitDouble(data, bitPos3);
                bitPos = ((Integer) v7.get(0)).intValue();
                nz = ((Double) v7.get(1)).doubleValue();
            }
            this.normal = new double[]{nx, ny, nz};
            Vector v8 = DwgUtil.getBitLong(data, bitPos);
            int bitPos4 = ((Integer) v8.get(0)).intValue();
            int np = ((Integer) v8.get(1)).intValue();
            if (np > 0 && np < 10000) {
                long nb = 0;
                if ((flag2 & 16) > 0) {
                    Vector v9 = DwgUtil.getBitLong(data, bitPos4);
                    bitPos4 = ((Integer) v9.get(0)).intValue();
                    nb = (long) ((Integer) v9.get(1)).intValue();
                }
                long nw = 0;
                if ((flag2 & 32) > 0) {
                    Vector v10 = DwgUtil.getBitLong(data, bitPos4);
                    bitPos4 = ((Integer) v10.get(0)).intValue();
                    nw = (long) ((Integer) v10.get(1)).intValue();
                }
                Point2D[] vertices2 = new Point2D[np];
                Vector v11 = DwgUtil.getRawDouble(data, bitPos4);
                int bitPos5 = ((Integer) v11.get(0)).intValue();
                double vx = ((Double) v11.get(1)).doubleValue();
                Vector v12 = DwgUtil.getRawDouble(data, bitPos5);
                int bitPos6 = ((Integer) v12.get(0)).intValue();
                double vy = ((Double) v12.get(1)).doubleValue();
                vertices2[0] = new Point2D(vx, vy);
                for (int i = 1; i < np; i++) {
                    Vector v13 = DwgUtil.getDefaultDouble(data, bitPos6, vx);
                    int bitPos7 = ((Integer) v13.get(0)).intValue();
                    double x = ((Double) v13.get(1)).doubleValue();
                    Vector v14 = DwgUtil.getDefaultDouble(data, bitPos7, vy);
                    bitPos6 = ((Integer) v14.get(0)).intValue();
                    double y = ((Double) v14.get(1)).doubleValue();
                    vertices2[i] = new Point2D(x, y);
                    vx = x;
                    vy = y;
                }
                this.vertices = vertices2;
                double[] bulges2 = new double[0];
                if (nb > 0) {
                    bulges2 = new double[((int) nb)];
                    for (int i2 = 0; ((long) i2) < nb; i2++) {
                        Vector v15 = DwgUtil.getRawDouble(data, bitPos6);
                        bitPos6 = ((Integer) v15.get(0)).intValue();
                        bulges2[i2] = ((Double) v15.get(1)).doubleValue();
                    }
                } else if (nb == 0) {
                    bulges2 = new double[np];
                    for (int i3 = 0; i3 < np; i3++) {
                        bulges2[i3] = 0.0d;
                    }
                }
                this.bulges = bulges2;
                if (nw > 0) {
                    double[][] widths2 = (double[][]) Array.newInstance(Double.TYPE, (int) nw, 2);
                    for (int i4 = 0; ((long) i4) < nw; i4++) {
                        Vector v16 = DwgUtil.getBitDouble(data, bitPos6);
                        int bitPos8 = ((Integer) v16.get(0)).intValue();
                        double sw = ((Double) v16.get(1)).doubleValue();
                        Vector v17 = DwgUtil.getBitDouble(data, bitPos8);
                        bitPos6 = ((Integer) v17.get(0)).intValue();
                        double ew = ((Double) v17.get(1)).doubleValue();
                        widths2[i4][0] = sw;
                        widths2[i4][1] = ew;
                    }
                    this.widths = widths2;
                }
                readObjectTailV15(data, bitPos6);
            }
        }
    }

    public double[] getBulges() {
        return this.bulges;
    }

    public void setBulges(double[] bulges2) {
        this.bulges = bulges2;
    }

    public int getFlag() {
        return this.flag;
    }

    public void setFlag(int flag2) {
        this.flag = flag2;
    }

    public Point2D[] getVertices() {
        return this.vertices;
    }

    public void setVertices(Point2D[] vertices2) {
        this.vertices = vertices2;
    }

    public double getElevation() {
        return this.elevation;
    }

    public void setElevation(double elevation2) {
        this.elevation = elevation2;
    }

    public double[] getNormal() {
        return this.normal;
    }

    public Object clone() {
        DwgLwPolyline dwgLwPolyline = new DwgLwPolyline();
        dwgLwPolyline.setType(this.type);
        dwgLwPolyline.setHandle(this.handle);
        dwgLwPolyline.setVersion(this.version);
        dwgLwPolyline.setMode(this.mode);
        dwgLwPolyline.setLayerHandle(this.layerHandle);
        dwgLwPolyline.setColor(this.color);
        dwgLwPolyline.setNumReactors(this.numReactors);
        dwgLwPolyline.setNoLinks(this.noLinks);
        dwgLwPolyline.setLinetypeFlags(this.linetypeFlags);
        dwgLwPolyline.setPlotstyleFlags(this.plotstyleFlags);
        dwgLwPolyline.setSizeInBits(this.sizeInBits);
        dwgLwPolyline.setExtendedData(this.extendedData);
        dwgLwPolyline.setGraphicData(this.graphicData);
        dwgLwPolyline.setFlag(this.flag);
        dwgLwPolyline.setElevation(this.elevation);
        dwgLwPolyline.setConstWidth(this.constWidth);
        dwgLwPolyline.setThickness(this.thickness);
        dwgLwPolyline.setNormal(this.normal);
        dwgLwPolyline.setVertices(this.vertices);
        dwgLwPolyline.setBulges(this.bulges);
        dwgLwPolyline.setWidths(this.widths);
        return dwgLwPolyline;
    }

    public double getConstWidth() {
        return this.constWidth;
    }

    public void setConstWidth(double constWidth2) {
        this.constWidth = constWidth2;
    }

    public double getThickness() {
        return this.thickness;
    }

    public void setThickness(double thickness2) {
        this.thickness = thickness2;
    }

    public double[][] getWidths() {
        return this.widths;
    }

    public void setWidths(double[][] widths2) {
        this.widths = widths2;
    }

    public void setNormal(double[] normal2) {
        this.normal = normal2;
    }
}
