package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgBlockHeader extends DwgObject {
    private boolean anonymous;
    private double[] basePoint;
    private boolean blkIsXRef;
    private int blockControlHandle;
    private String blockDescription;
    private int blockEntityHandle;
    private int endBlkEntityHandle;
    private int firstEntityHandle;
    private boolean flag64;
    private boolean hasAttrs;
    private Vector insertHandles;
    private int lastEntityHandle;
    private int layoutHandle;
    private boolean loaded;
    private String name;
    private int nullHandle;
    private Vector objects = new Vector();
    private int previewData;
    private boolean xRefOverLaid;
    private String xRefPName;
    private int xRefPlus;
    private boolean xdep;

    public void readDwgBlockHeaderV15(int[] data, int offset) throws Exception {
        Vector v = DwgUtil.getBitLong(data, offset);
        int bitPos = ((Integer) v.get(0)).intValue();
        int numReactors = ((Integer) v.get(1)).intValue();
        setNumReactors(numReactors);
        Vector v2 = DwgUtil.getTextString(data, bitPos);
        int bitPos2 = ((Integer) v2.get(0)).intValue();
        this.name = (String) v2.get(1);
        Vector v3 = DwgUtil.testBit(data, bitPos2);
        int bitPos3 = ((Integer) v3.get(0)).intValue();
        this.flag64 = ((Boolean) v3.get(1)).booleanValue();
        Vector v4 = DwgUtil.getBitShort(data, bitPos3);
        int bitPos4 = ((Integer) v4.get(0)).intValue();
        this.xRefPlus = ((Integer) v4.get(1)).intValue();
        Vector v5 = DwgUtil.testBit(data, bitPos4);
        int bitPos5 = ((Integer) v5.get(0)).intValue();
        this.xdep = ((Boolean) v5.get(1)).booleanValue();
        Vector v6 = DwgUtil.testBit(data, bitPos5);
        int bitPos6 = ((Integer) v6.get(0)).intValue();
        this.anonymous = ((Boolean) v6.get(1)).booleanValue();
        Vector v7 = DwgUtil.testBit(data, bitPos6);
        int bitPos7 = ((Integer) v7.get(0)).intValue();
        this.hasAttrs = ((Boolean) v7.get(1)).booleanValue();
        Vector v8 = DwgUtil.testBit(data, bitPos7);
        int bitPos8 = ((Integer) v8.get(0)).intValue();
        boolean bxref = ((Boolean) v8.get(1)).booleanValue();
        this.blkIsXRef = bxref;
        Vector v9 = DwgUtil.testBit(data, bitPos8);
        int bitPos9 = ((Integer) v9.get(0)).intValue();
        boolean xover = ((Boolean) v9.get(1)).booleanValue();
        this.xRefOverLaid = xover;
        Vector v10 = DwgUtil.testBit(data, bitPos9);
        int bitPos10 = ((Integer) v10.get(0)).intValue();
        this.loaded = ((Boolean) v10.get(1)).booleanValue();
        Vector v11 = DwgUtil.getBitDouble(data, bitPos10);
        int bitPos11 = ((Integer) v11.get(0)).intValue();
        double bx = ((Double) v11.get(1)).doubleValue();
        Vector v12 = DwgUtil.getBitDouble(data, bitPos11);
        int bitPos12 = ((Integer) v12.get(0)).intValue();
        double by = ((Double) v12.get(1)).doubleValue();
        Vector v13 = DwgUtil.getBitDouble(data, bitPos12);
        int bitPos13 = ((Integer) v13.get(0)).intValue();
        this.basePoint = new double[]{bx, by, ((Double) v13.get(1)).doubleValue()};
        Vector v14 = DwgUtil.getTextString(data, bitPos13);
        int bitPos14 = ((Integer) v14.get(0)).intValue();
        this.xRefPName = (String) v14.get(1);
        int icount = 0;
        while (true) {
            Vector v15 = DwgUtil.getRawChar(data, bitPos14);
            bitPos14 = ((Integer) v15.get(0)).intValue();
            if (((Integer) v15.get(1)).intValue() == 0) {
                break;
            }
            icount++;
        }
        Vector v16 = DwgUtil.getTextString(data, bitPos14);
        int bitPos15 = ((Integer) v16.get(0)).intValue();
        this.blockDescription = (String) v16.get(1);
        Vector v17 = DwgUtil.getBitLong(data, bitPos15);
        int bitPos16 = ((Integer) v17.get(0)).intValue();
        int pdsize = ((Integer) v17.get(1)).intValue();
        if (pdsize > 0) {
            bitPos16 += pdsize + icount;
        }
        Vector v18 = DwgUtil.getHandle(data, bitPos16);
        int bitPos17 = ((Integer) v18.get(0)).intValue();
        int[] handle = new int[(v18.size() - 1)];
        for (int j = 1; j < v18.size(); j++) {
            handle[j - 1] = ((Integer) v18.get(j)).intValue();
        }
        Vector handleVect = new Vector();
        for (int i = 0; i < handle.length; i++) {
            handleVect.add(new Integer(handle[i]));
        }
        this.blockControlHandle = DwgUtil.handleBinToHandleInt(handleVect);
        for (int i2 = 0; i2 < numReactors; i2++) {
            Vector v19 = DwgUtil.getHandle(data, bitPos17);
            bitPos17 = ((Integer) v19.get(0)).intValue();
            int[] handle2 = new int[(v19.size() - 1)];
            for (int j2 = 1; j2 < v19.size(); j2++) {
                handle2[j2 - 1] = ((Integer) v19.get(j2)).intValue();
            }
        }
        Vector v20 = DwgUtil.getHandle(data, bitPos17);
        int bitPos18 = ((Integer) v20.get(0)).intValue();
        int[] handle3 = new int[(v20.size() - 1)];
        for (int j3 = 1; j3 < v20.size(); j3++) {
            handle3[j3 - 1] = ((Integer) v20.get(j3)).intValue();
        }
        Vector v21 = DwgUtil.getHandle(data, bitPos18);
        int bitPos19 = ((Integer) v21.get(0)).intValue();
        int[] handle4 = new int[(v21.size() - 1)];
        for (int j4 = 1; j4 < v21.size(); j4++) {
            handle4[j4 - 1] = ((Integer) v21.get(j4)).intValue();
        }
        Vector handleVect2 = new Vector();
        for (int i3 = 0; i3 < handle4.length; i3++) {
            handleVect2.add(new Integer(handle4[i3]));
        }
        this.nullHandle = DwgUtil.handleBinToHandleInt(handleVect2);
        Vector v22 = DwgUtil.getHandle(data, bitPos19);
        int bitPos20 = ((Integer) v22.get(0)).intValue();
        int[] handle5 = new int[(v22.size() - 1)];
        for (int j5 = 1; j5 < v22.size(); j5++) {
            handle5[j5 - 1] = ((Integer) v22.get(j5)).intValue();
        }
        Vector handleVect3 = new Vector();
        for (int i4 = 0; i4 < handle5.length; i4++) {
            handleVect3.add(new Integer(handle5[i4]));
        }
        this.blockEntityHandle = DwgUtil.handleBinToHandleInt(handleVect3);
        if (!bxref && !xover) {
            Vector v23 = DwgUtil.getHandle(data, bitPos20);
            int bitPos21 = ((Integer) v23.get(0)).intValue();
            int[] handle6 = new int[(v23.size() - 1)];
            for (int j6 = 1; j6 < v23.size(); j6++) {
                handle6[j6 - 1] = ((Integer) v23.get(j6)).intValue();
            }
            Vector handleVect4 = new Vector();
            for (int i5 = 0; i5 < handle6.length; i5++) {
                handleVect4.add(new Integer(handle6[i5]));
            }
            this.firstEntityHandle = DwgUtil.handleBinToHandleInt(handleVect4);
            Vector v24 = DwgUtil.getHandle(data, bitPos21);
            bitPos20 = ((Integer) v24.get(0)).intValue();
            int[] handle7 = new int[(v24.size() - 1)];
            for (int j7 = 1; j7 < v24.size(); j7++) {
                handle7[j7 - 1] = ((Integer) v24.get(j7)).intValue();
            }
            Vector handleVect5 = new Vector();
            for (int i6 = 0; i6 < handle7.length; i6++) {
                handleVect5.add(new Integer(handle7[i6]));
            }
            this.lastEntityHandle = DwgUtil.handleBinToHandleInt(handleVect5);
        }
        Vector v25 = DwgUtil.getHandle(data, bitPos20);
        int bitPos22 = ((Integer) v25.get(0)).intValue();
        int[] handle8 = new int[(v25.size() - 1)];
        for (int j8 = 1; j8 < v25.size(); j8++) {
            handle8[j8 - 1] = ((Integer) v25.get(j8)).intValue();
        }
        Vector handleVect6 = new Vector();
        for (int i7 = 0; i7 < handle8.length; i7++) {
            handleVect6.add(new Integer(handle8[i7]));
        }
        this.endBlkEntityHandle = DwgUtil.handleBinToHandleInt(handleVect6);
        if (icount > 0) {
            Vector handles = new Vector();
            for (int i8 = 0; i8 < icount; i8++) {
                Vector v26 = DwgUtil.getHandle(data, bitPos22);
                bitPos22 = ((Integer) v26.get(0)).intValue();
                int[] handle9 = new int[(v26.size() - 1)];
                for (int j9 = 1; j9 < v26.size(); j9++) {
                    handle9[j9 - 1] = ((Integer) v26.get(j9)).intValue();
                }
                handles.add(handle9);
            }
            this.insertHandles = handles;
        }
        Vector v27 = DwgUtil.getHandle(data, bitPos22);
        ((Integer) v27.get(0)).intValue();
        int[] handle10 = new int[(v27.size() - 1)];
        for (int j10 = 1; j10 < v27.size(); j10++) {
            handle10[j10 - 1] = ((Integer) v27.get(j10)).intValue();
        }
        Vector handleVect7 = new Vector();
        for (int i9 = 0; i9 < handle10.length; i9++) {
            handleVect7.add(new Integer(handle10[i9]));
        }
        this.layoutHandle = DwgUtil.handleBinToHandleInt(handleVect7);
    }

    public double[] getBasePoint() {
        return this.basePoint;
    }

    public void setBasePoint(double[] basePoint2) {
        this.basePoint = basePoint2;
    }

    public int getFirstEntityHandle() {
        return this.firstEntityHandle;
    }

    public void setFirstEntityHandle(int firstEntityHandle2) {
        this.firstEntityHandle = firstEntityHandle2;
    }

    public int getLastEntityHandle() {
        return this.lastEntityHandle;
    }

    public void setLastEntityHandle(int lastEntityHandle2) {
        this.lastEntityHandle = lastEntityHandle2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public int getBlockEntityHandle() {
        return this.blockEntityHandle;
    }

    public void setBlockEntityHandle(int blockEntityHandle2) {
        this.blockEntityHandle = blockEntityHandle2;
    }

    public boolean isAnonymous() {
        return this.anonymous;
    }

    public void setAnonymous(boolean anonymous2) {
        this.anonymous = anonymous2;
    }

    public boolean isBlkIsXRef() {
        return this.blkIsXRef;
    }

    public void setBlkIsXRef(boolean blkIsXRef2) {
        this.blkIsXRef = blkIsXRef2;
    }

    public int getBlockControlHandle() {
        return this.blockControlHandle;
    }

    public void setBlockControlHandle(int blockControlHandle2) {
        this.blockControlHandle = blockControlHandle2;
    }

    public String getBlockDescription() {
        return this.blockDescription;
    }

    public void setBlockDescription(String blockDescription2) {
        this.blockDescription = blockDescription2;
    }

    public int getEndBlkEntityHandle() {
        return this.endBlkEntityHandle;
    }

    public void setEndBlkEntityHandle(int endBlkEntityHandle2) {
        this.endBlkEntityHandle = endBlkEntityHandle2;
    }

    public boolean isFlag64() {
        return this.flag64;
    }

    public void setFlag64(boolean flag642) {
        this.flag64 = flag642;
    }

    public boolean isHasAttrs() {
        return this.hasAttrs;
    }

    public void setHasAttrs(boolean hasAttrs2) {
        this.hasAttrs = hasAttrs2;
    }

    public Vector getInsertHandles() {
        return this.insertHandles;
    }

    public void setInsertHandles(Vector insertHandles2) {
        this.insertHandles = insertHandles2;
    }

    public int getLayoutHandle() {
        return this.layoutHandle;
    }

    public void setLayoutHandle(int layoutHandle2) {
        this.layoutHandle = layoutHandle2;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void setLoaded(boolean loaded2) {
        this.loaded = loaded2;
    }

    public int getNullHandle() {
        return this.nullHandle;
    }

    public void setNullHandle(int nullHandle2) {
        this.nullHandle = nullHandle2;
    }

    public int getPreviewData() {
        return this.previewData;
    }

    public void setPreviewData(int previewData2) {
        this.previewData = previewData2;
    }

    public boolean isXdep() {
        return this.xdep;
    }

    public void setXdep(boolean xdep2) {
        this.xdep = xdep2;
    }

    public boolean isXRefOverLaid() {
        return this.xRefOverLaid;
    }

    public void setXRefOverLaid(boolean refOverLaid) {
        this.xRefOverLaid = refOverLaid;
    }

    public int getXRefPlus() {
        return this.xRefPlus;
    }

    public void setXRefPlus(int refPlus) {
        this.xRefPlus = refPlus;
    }

    public String getXRefPName() {
        return this.xRefPName;
    }

    public void setXRefPName(String refPName) {
        this.xRefPName = refPName;
    }

    public Vector getObjects() {
        return this.objects;
    }

    public void setObjects(Vector objects2) {
        this.objects = objects2;
    }

    public void addObject(DwgObject object) {
        this.objects.add(object);
    }
}
