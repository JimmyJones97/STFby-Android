package  com.xzy.forestSystem.gogisapi.IOData.DWGReader;

import java.util.Vector;

public class DwgObject {
    protected int color;
    protected Vector extendedData;
    protected int graphicData;
    protected boolean graphicsFlag;
    protected int handle;
    protected int layerHandle;
    protected int layerHandleCode;
    protected int linetypeFlags;
    protected int mode;
    protected boolean noLinks;
    protected int numReactors;
    protected int plotstyleFlags;
    protected int sizeInBits;
    protected int subEntityHandle;
    protected int type;
    protected String version;
    protected int xDicObjHandle;

    public int readObjectHeaderV15(int[] data, int offset) throws Exception {
        setMode(((Integer) DwgUtil.getBits(data, 2, offset)).intValue());
        Vector v = DwgUtil.getBitLong(data, offset + 2);
        int bitPos = ((Integer) v.get(0)).intValue();
        setNumReactors(((Integer) v.get(1)).intValue());
        Vector v2 = DwgUtil.testBit(data, bitPos);
        int bitPos2 = ((Integer) v2.get(0)).intValue();
        setNoLinks(((Boolean) v2.get(1)).booleanValue());
        Vector v3 = DwgUtil.getBitShort(data, bitPos2);
        int bitPos3 = ((Integer) v3.get(0)).intValue();
        setColor(((Integer) v3.get(1)).intValue());
        Vector v4 = DwgUtil.getBitDouble(data, bitPos3);
        int bitPos4 = ((Integer) v4.get(0)).intValue();
        ((Double) v4.get(1)).floatValue();
        Integer num = (Integer) DwgUtil.getBits(data, 2, bitPos4);
        int bitPos5 = bitPos4 + 2;
        Integer num2 = (Integer) DwgUtil.getBits(data, 2, bitPos5);
        Vector v5 = DwgUtil.getBitShort(data, bitPos5 + 2);
        int bitPos6 = ((Integer) v5.get(0)).intValue();
        ((Integer) v5.get(1)).intValue();
        Vector v6 = DwgUtil.getRawChar(data, bitPos6);
        int bitPos7 = ((Integer) v6.get(0)).intValue();
        ((Integer) v6.get(1)).intValue();
        return bitPos7;
    }

    public int readObjectTailV15(int[] data, int offset) throws Exception {
        int bitPos = offset;
        if (getMode() == 0) {
            Vector v = DwgUtil.getHandle(data, bitPos);
            bitPos = ((Integer) v.get(0)).intValue();
            int[] sh = new int[(v.size() - 1)];
            for (int i = 1; i < v.size(); i++) {
                sh[i - 1] = ((Integer) v.get(i)).intValue();
            }
            Vector shv = new Vector();
            for (int i2 : sh) {
                shv.add(new Integer(i2));
            }
            setSubEntityHandle(DwgUtil.handleBinToHandleInt(shv));
        }
        for (int i3 = 0; i3 < getNumReactors(); i3++) {
            Vector v2 = DwgUtil.getHandle(data, bitPos);
            bitPos = ((Integer) v2.get(0)).intValue();
            int[] handle2 = new int[(v2.size() - 1)];
            for (int j = 1; j < v2.size(); j++) {
                handle2[j - 1] = ((Integer) v2.get(j)).intValue();
            }
        }
        Vector v3 = DwgUtil.getHandle(data, bitPos);
        int bitPos2 = ((Integer) v3.get(0)).intValue();
        int[] xh = new int[(v3.size() - 1)];
        for (int i4 = 1; i4 < v3.size(); i4++) {
            xh[i4 - 1] = ((Integer) v3.get(i4)).intValue();
        }
        Vector xhv = new Vector();
        for (int i5 : xh) {
            xhv.add(new Integer(i5));
        }
        setXDicObjHandle(DwgUtil.handleBinToHandleInt(xhv));
        Vector v4 = DwgUtil.getHandle(data, bitPos2);
        int bitPos3 = ((Integer) v4.get(0)).intValue();
        int[] lh = new int[(v4.size() - 1)];
        for (int i6 = 1; i6 < v4.size(); i6++) {
            lh[i6 - 1] = ((Integer) v4.get(i6)).intValue();
        }
        setLayerHandleCode(lh[0]);
        Vector lhv = new Vector();
        for (int i7 : lh) {
            lhv.add(new Integer(i7));
        }
        setLayerHandle(DwgUtil.handleBinToHandleInt(lhv));
        if (!isNoLinks()) {
            Vector v5 = DwgUtil.getHandle(data, bitPos3);
            int bitPos4 = ((Integer) v5.get(0)).intValue();
            int[] prev = new int[(v5.size() - 1)];
            for (int i8 = 1; i8 < v5.size(); i8++) {
                prev[i8 - 1] = ((Integer) v5.get(i8)).intValue();
            }
            Vector v6 = DwgUtil.getHandle(data, bitPos4);
            bitPos3 = ((Integer) v6.get(0)).intValue();
            int[] next = new int[(v6.size() - 1)];
            for (int i9 = 1; i9 < v6.size(); i9++) {
                next[i9 - 1] = ((Integer) v6.get(i9)).intValue();
            }
        }
        if (getLinetypeFlags() == 3) {
            Vector v7 = DwgUtil.getHandle(data, bitPos3);
            bitPos3 = ((Integer) v7.get(0)).intValue();
            int[] lth = new int[(v7.size() - 1)];
            for (int i10 = 1; i10 < v7.size(); i10++) {
                lth[i10 - 1] = ((Integer) v7.get(i10)).intValue();
            }
        }
        if (getPlotstyleFlags() == 3) {
            Vector v8 = DwgUtil.getHandle(data, bitPos3);
            bitPos3 = ((Integer) v8.get(0)).intValue();
            int[] pth = new int[(v8.size() - 1)];
            for (int i11 = 1; i11 < v8.size(); i11++) {
                pth[i11 - 1] = ((Integer) v8.get(i11)).intValue();
            }
        }
        return bitPos3;
    }

    public int getSizeInBits() {
        return this.sizeInBits;
    }

    public void setSizeInBits(int sizeInBits2) {
        this.sizeInBits = sizeInBits2;
    }

    public Vector getExtendedData() {
        return this.extendedData;
    }

    public void setExtendedData(Vector extendedData2) {
        this.extendedData = extendedData2;
    }

    public int getGraphicData() {
        return this.graphicData;
    }

    public void setGraphicData(int graphicData2) {
        this.graphicData = graphicData2;
    }

    public String getVersion() {
        return this.version;
    }

    public void setLinetypeFlags(int linetypeFlags2) {
        this.linetypeFlags = linetypeFlags2;
    }

    public void setPlotstyleFlags(int plotstyleFlags2) {
        this.plotstyleFlags = plotstyleFlags2;
    }

    public int getSubEntityHandle() {
        return this.subEntityHandle;
    }

    public void setSubEntityHandle(int subEntityHandle2) {
        this.subEntityHandle = subEntityHandle2;
    }

    public int getXDicObjHandle() {
        return this.xDicObjHandle;
    }

    public void setXDicObjHandle(int dicObjHandle) {
        this.xDicObjHandle = dicObjHandle;
    }

    public int getLayerHandleCode() {
        return this.layerHandleCode;
    }

    public void setLayerHandleCode(int layerHandleCode2) {
        this.layerHandleCode = layerHandleCode2;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color2) {
        this.color = color2;
    }

    public int getHandle() {
        return this.handle;
    }

    public void setHandle(int handle2) {
        this.handle = handle2;
    }

    public int getLayerHandle() {
        return this.layerHandle;
    }

    public void setLayerHandle(int layerHandle2) {
        this.layerHandle = layerHandle2;
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode2) {
        this.mode = mode2;
    }

    public boolean isNoLinks() {
        return this.noLinks;
    }

    public void setNoLinks(boolean noLinks2) {
        this.noLinks = noLinks2;
    }

    public int getNumReactors() {
        return this.numReactors;
    }

    public void setNumReactors(int numReactors2) {
        this.numReactors = numReactors2;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type2) {
        this.type = type2;
    }

    public int getLinetypeFlags() {
        return this.linetypeFlags;
    }

    public int getPlotstyleFlags() {
        return this.plotstyleFlags;
    }

    public void setVersion(String version2) {
        this.version = version2;
    }

    public boolean isGraphicsFlag() {
        return this.graphicsFlag;
    }

    public void setGraphicsFlag(boolean graphicsFlag2) {
        this.graphicsFlag = graphicsFlag2;
    }
}
