package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgPolyline3D extends DwgObject {
    private double[] bulges;
    private int closedFlags;
    private int firstVertexHandle;
    private int lastVertexHandle;
    private double[][] pts;
    private int seqendHandle;
    private int splineFlags;

    public void readDwgPolyline3DV15(int[] data, int offset) throws Exception {
        Vector v = DwgUtil.getRawChar(data, readObjectHeaderV15(data, offset));
        int bitPos = ((Integer) v.get(0)).intValue();
        this.splineFlags = ((Integer) v.get(1)).intValue();
        Vector v2 = DwgUtil.getRawChar(data, bitPos);
        int bitPos2 = ((Integer) v2.get(0)).intValue();
        this.closedFlags = ((Integer) v2.get(1)).intValue();
        Vector v3 = DwgUtil.getHandle(data, readObjectTailV15(data, bitPos2));
        int bitPos3 = ((Integer) v3.get(0)).intValue();
        int[] handle = new int[(v3.size() - 1)];
        for (int i = 1; i < v3.size(); i++) {
            handle[i - 1] = ((Integer) v3.get(i)).intValue();
        }
        Vector handleVect = new Vector();
        for (int i2 : handle) {
            handleVect.add(new Integer(i2));
        }
        this.firstVertexHandle = DwgUtil.handleBinToHandleInt(handleVect);
        Vector v4 = DwgUtil.getHandle(data, bitPos3);
        int bitPos4 = ((Integer) v4.get(0)).intValue();
        int[] handle2 = new int[(v4.size() - 1)];
        for (int i3 = 1; i3 < v4.size(); i3++) {
            handle2[i3 - 1] = ((Integer) v4.get(i3)).intValue();
        }
        Vector handleVect2 = new Vector();
        for (int i4 : handle2) {
            handleVect2.add(new Integer(i4));
        }
        this.lastVertexHandle = DwgUtil.handleBinToHandleInt(handleVect2);
        Vector v5 = DwgUtil.getHandle(data, bitPos4);
        ((Integer) v5.get(0)).intValue();
        int[] handle3 = new int[(v5.size() - 1)];
        for (int i5 = 1; i5 < v5.size(); i5++) {
            handle3[i5 - 1] = ((Integer) v5.get(i5)).intValue();
        }
        Vector handleVect3 = new Vector();
        for (int i6 : handle3) {
            handleVect3.add(new Integer(i6));
        }
        this.seqendHandle = DwgUtil.handleBinToHandleInt(handleVect3);
    }

    public int getClosedFlags() {
        return this.closedFlags;
    }

    public void setClosedFlags(int closedFlags2) {
        this.closedFlags = closedFlags2;
    }

    public int getFirstVertexHandle() {
        return this.firstVertexHandle;
    }

    public void setFirstVertexHandle(int firstVertexHandle2) {
        this.firstVertexHandle = firstVertexHandle2;
    }

    public int getLastVertexHandle() {
        return this.lastVertexHandle;
    }

    public void setLastVertexHandle(int lastVertexHandle2) {
        this.lastVertexHandle = lastVertexHandle2;
    }

    public double[][] getPts() {
        return this.pts;
    }

    public void setPts(double[][] pts2) {
        this.pts = pts2;
    }

    public double[] getBulges() {
        return this.bulges;
    }

    public void setBulges(double[] bulges2) {
        this.bulges = bulges2;
    }

    public Object clone() {
        DwgPolyline3D dwgPolyline3D = new DwgPolyline3D();
        dwgPolyline3D.setType(this.type);
        dwgPolyline3D.setHandle(this.handle);
        dwgPolyline3D.setVersion(this.version);
        dwgPolyline3D.setMode(this.mode);
        dwgPolyline3D.setLayerHandle(this.layerHandle);
        dwgPolyline3D.setColor(this.color);
        dwgPolyline3D.setNumReactors(this.numReactors);
        dwgPolyline3D.setNoLinks(this.noLinks);
        dwgPolyline3D.setLinetypeFlags(this.linetypeFlags);
        dwgPolyline3D.setPlotstyleFlags(this.plotstyleFlags);
        dwgPolyline3D.setSizeInBits(this.sizeInBits);
        dwgPolyline3D.setExtendedData(this.extendedData);
        dwgPolyline3D.setGraphicData(this.graphicData);
        dwgPolyline3D.setSplineFlags(this.splineFlags);
        dwgPolyline3D.setClosedFlags(this.closedFlags);
        dwgPolyline3D.setFirstVertexHandle(this.firstVertexHandle);
        dwgPolyline3D.setLastVertexHandle(this.lastVertexHandle);
        dwgPolyline3D.setSeqendHandle(this.seqendHandle);
        dwgPolyline3D.setPts(this.pts);
        dwgPolyline3D.setBulges(this.bulges);
        return dwgPolyline3D;
    }

    public int getSeqendHandle() {
        return this.seqendHandle;
    }

    public void setSeqendHandle(int seqendHandle2) {
        this.seqendHandle = seqendHandle2;
    }

    public int getSplineFlags() {
        return this.splineFlags;
    }

    public void setSplineFlags(int splineFlags2) {
        this.splineFlags = splineFlags2;
    }
}
