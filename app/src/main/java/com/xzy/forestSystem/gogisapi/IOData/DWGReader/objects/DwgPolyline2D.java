package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgPolyline2D extends DwgObject {
    private double[] bulges;
    private int curveType;
    private double elevation;
    private double endWidth;
    private double[] extrusion;
    private int firstVertexHandle;
    private int flags;
    private double initWidth;
    private int lastVertexHandle;
    private Point2D[] pts;
    private int seqendHandle;
    private double thickness;

    public void readDwgPolyline2DV15(int[] data, int offset) throws Exception {
        double ex;
        double ey;
        double ez;
        Vector v = DwgUtil.getBitShort(data, readObjectHeaderV15(data, offset));
        int bitPos = ((Integer) v.get(0)).intValue();
        this.flags = ((Integer) v.get(1)).intValue();
        Vector v2 = DwgUtil.getBitShort(data, bitPos);
        int bitPos2 = ((Integer) v2.get(0)).intValue();
        this.curveType = ((Integer) v2.get(1)).intValue();
        Vector v3 = DwgUtil.getBitDouble(data, bitPos2);
        int bitPos3 = ((Integer) v3.get(0)).intValue();
        this.initWidth = ((Double) v3.get(1)).doubleValue();
        Vector v4 = DwgUtil.getBitDouble(data, bitPos3);
        int bitPos4 = ((Integer) v4.get(0)).intValue();
        this.endWidth = ((Double) v4.get(1)).doubleValue();
        Vector v5 = DwgUtil.testBit(data, bitPos4);
        int bitPos5 = ((Integer) v5.get(0)).intValue();
        double th = 0.0d;
        if (!((Boolean) v5.get(1)).booleanValue()) {
            Vector v6 = DwgUtil.getBitDouble(data, bitPos5);
            bitPos5 = ((Integer) v6.get(0)).intValue();
            th = ((Double) v6.get(1)).doubleValue();
        }
        this.thickness = th;
        Vector v7 = DwgUtil.getBitDouble(data, bitPos5);
        int bitPos6 = ((Integer) v7.get(0)).intValue();
        this.elevation = ((Double) v7.get(1)).doubleValue();
        Vector v8 = DwgUtil.testBit(data, bitPos6);
        int bitPos7 = ((Integer) v8.get(0)).intValue();
        if (((Boolean) v8.get(1)).booleanValue()) {
            ex = 0.0d;
            ey = 0.0d;
            ez = 1.0d;
        } else {
            Vector v9 = DwgUtil.getBitDouble(data, bitPos7);
            int bitPos8 = ((Integer) v9.get(0)).intValue();
            ex = ((Double) v9.get(1)).doubleValue();
            Vector v10 = DwgUtil.getBitDouble(data, bitPos8);
            int bitPos9 = ((Integer) v10.get(0)).intValue();
            ey = ((Double) v10.get(1)).doubleValue();
            Vector v11 = DwgUtil.getBitDouble(data, bitPos9);
            bitPos7 = ((Integer) v11.get(0)).intValue();
            ez = ((Double) v11.get(1)).doubleValue();
        }
        this.extrusion = new double[]{ex, ey, ez};
        Vector v12 = DwgUtil.getHandle(data, readObjectTailV15(data, bitPos7));
        int bitPos10 = ((Integer) v12.get(0)).intValue();
        int[] handle = new int[(v12.size() - 1)];
        for (int i = 1; i < v12.size(); i++) {
            handle[i - 1] = ((Integer) v12.get(i)).intValue();
        }
        Vector handleVect = new Vector();
        for (int i2 = 0; i2 < handle.length; i2++) {
            handleVect.add(new Integer(handle[i2]));
        }
        this.firstVertexHandle = DwgUtil.handleBinToHandleInt(handleVect);
        Vector v13 = DwgUtil.getHandle(data, bitPos10);
        int bitPos11 = ((Integer) v13.get(0)).intValue();
        int[] handle2 = new int[(v13.size() - 1)];
        for (int i3 = 1; i3 < v13.size(); i3++) {
            handle2[i3 - 1] = ((Integer) v13.get(i3)).intValue();
        }
        Vector handleVect2 = new Vector();
        for (int i4 = 0; i4 < handle2.length; i4++) {
            handleVect2.add(new Integer(handle2[i4]));
        }
        this.lastVertexHandle = DwgUtil.handleBinToHandleInt(handleVect2);
        Vector v14 = DwgUtil.getHandle(data, bitPos11);
        ((Integer) v14.get(0)).intValue();
        int[] handle3 = new int[(v14.size() - 1)];
        for (int i5 = 1; i5 < v14.size(); i5++) {
            handle3[i5 - 1] = ((Integer) v14.get(i5)).intValue();
        }
        Vector handleVect3 = new Vector();
        for (int i6 = 0; i6 < handle3.length; i6++) {
            handleVect3.add(new Integer(handle3[i6]));
        }
        this.seqendHandle = DwgUtil.handleBinToHandleInt(handleVect3);
    }

    public int getFirstVertexHandle() {
        return this.firstVertexHandle;
    }

    public void setFirstVertexHandle(int firstVertexHandle2) {
        this.firstVertexHandle = firstVertexHandle2;
    }

    public int getFlags() {
        return this.flags;
    }

    public void setFlags(int flags2) {
        this.flags = flags2;
    }

    public int getLastVertexHandle() {
        return this.lastVertexHandle;
    }

    public void setLastVertexHandle(int lastVertexHandle2) {
        this.lastVertexHandle = lastVertexHandle2;
    }

    public Point2D[] getPts() {
        return this.pts;
    }

    public void setPts(Point2D[] pts2) {
        this.pts = pts2;
    }

    public double[] getBulges() {
        return this.bulges;
    }

    public void setBulges(double[] bulges2) {
        this.bulges = bulges2;
    }

    public double getInitWidth() {
        return this.initWidth;
    }

    public void setInitWidth(double initWidth2) {
        this.initWidth = initWidth2;
    }

    public int getSeqendHandle() {
        return this.seqendHandle;
    }

    public void setSeqendHandle(int seqendHandle2) {
        this.seqendHandle = seqendHandle2;
    }

    public double getThickness() {
        return this.thickness;
    }

    public void setThickness(double thickness2) {
        this.thickness = thickness2;
    }

    public int getCurveType() {
        return this.curveType;
    }

    public void setCurveType(int curveType2) {
        this.curveType = curveType2;
    }

    public double getElevation() {
        return this.elevation;
    }

    public void setElevation(double elevation2) {
        this.elevation = elevation2;
    }

    public double getEndWidth() {
        return this.endWidth;
    }

    public void setEndWidth(double endWidth2) {
        this.endWidth = endWidth2;
    }

    public double[] getExtrusion() {
        return this.extrusion;
    }

    public void setExtrusion(double[] extrusion2) {
        this.extrusion = extrusion2;
    }

    public Object clone() {
        DwgPolyline2D dwgPolyline2D = new DwgPolyline2D();
        dwgPolyline2D.setType(this.type);
        dwgPolyline2D.setHandle(this.handle);
        dwgPolyline2D.setVersion(this.version);
        dwgPolyline2D.setMode(this.mode);
        dwgPolyline2D.setLayerHandle(this.layerHandle);
        dwgPolyline2D.setColor(this.color);
        dwgPolyline2D.setNumReactors(this.numReactors);
        dwgPolyline2D.setNoLinks(this.noLinks);
        dwgPolyline2D.setLinetypeFlags(this.linetypeFlags);
        dwgPolyline2D.setPlotstyleFlags(this.plotstyleFlags);
        dwgPolyline2D.setSizeInBits(this.sizeInBits);
        dwgPolyline2D.setExtendedData(this.extendedData);
        dwgPolyline2D.setGraphicData(this.graphicData);
        dwgPolyline2D.setFlags(this.flags);
        dwgPolyline2D.setCurveType(this.curveType);
        dwgPolyline2D.setInitWidth(this.initWidth);
        dwgPolyline2D.setEndWidth(this.endWidth);
        dwgPolyline2D.setThickness(this.thickness);
        dwgPolyline2D.setElevation(this.elevation);
        dwgPolyline2D.setExtrusion(this.extrusion);
        dwgPolyline2D.setFirstVertexHandle(this.firstVertexHandle);
        dwgPolyline2D.setLastVertexHandle(this.lastVertexHandle);
        dwgPolyline2D.setSeqendHandle(this.seqendHandle);
        dwgPolyline2D.setPts(this.pts);
        dwgPolyline2D.setBulges(this.bulges);
        return dwgPolyline2D;
    }
}
