package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgLayer extends DwgObject {
    private int color;
    private boolean flag64;
    private int flags;
    private int layerControlHandle;
    private int linetypeHandle;
    private String name;
    private int nullHandle;
    private int plotstyleHandle;
    private int xRefPlus;
    private boolean xdep;

    public void readDwgLayerV15(int[] data, int offset) throws Exception {
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
        Vector v6 = DwgUtil.getBitShort(data, bitPos5);
        int bitPos6 = ((Integer) v6.get(0)).intValue();
        this.flags = ((Integer) v6.get(1)).intValue();
        Vector v7 = DwgUtil.getBitShort(data, bitPos6);
        int bitPos7 = ((Integer) v7.get(0)).intValue();
        this.color = ((Integer) v7.get(1)).intValue();
        Vector v8 = DwgUtil.getHandle(data, bitPos7);
        int bitPos8 = ((Integer) v8.get(0)).intValue();
        int[] handle = new int[(v8.size() - 1)];
        for (int j = 1; j < v8.size(); j++) {
            handle[j - 1] = ((Integer) v8.get(j)).intValue();
        }
        Vector handleVect = new Vector();
        for (int i : handle) {
            handleVect.add(new Integer(i));
        }
        this.layerControlHandle = DwgUtil.handleBinToHandleInt(handleVect);
        for (int i2 = 0; i2 < numReactors; i2++) {
            Vector v9 = DwgUtil.getHandle(data, bitPos8);
            bitPos8 = ((Integer) v9.get(0)).intValue();
            int[] handle2 = new int[(v9.size() - 1)];
            for (int j2 = 1; j2 < v9.size(); j2++) {
                handle2[j2 - 1] = ((Integer) v9.get(j2)).intValue();
            }
        }
        Vector v10 = DwgUtil.getHandle(data, bitPos8);
        int bitPos9 = ((Integer) v10.get(0)).intValue();
        int[] handle3 = new int[(v10.size() - 1)];
        for (int j3 = 1; j3 < v10.size(); j3++) {
            handle3[j3 - 1] = ((Integer) v10.get(j3)).intValue();
        }
        Vector v11 = DwgUtil.getHandle(data, bitPos9);
        int bitPos10 = ((Integer) v11.get(0)).intValue();
        int[] handle4 = new int[(v11.size() - 1)];
        for (int j4 = 1; j4 < v11.size(); j4++) {
            handle4[j4 - 1] = ((Integer) v11.get(j4)).intValue();
        }
        Vector handleVect2 = new Vector();
        for (int i3 : handle4) {
            handleVect2.add(new Integer(i3));
        }
        this.nullHandle = DwgUtil.handleBinToHandleInt(handleVect2);
        Vector v12 = DwgUtil.getHandle(data, bitPos10);
        int bitPos11 = ((Integer) v12.get(0)).intValue();
        int[] handle5 = new int[(v12.size() - 1)];
        for (int j5 = 1; j5 < v12.size(); j5++) {
            handle5[j5 - 1] = ((Integer) v12.get(j5)).intValue();
        }
        Vector handleVect3 = new Vector();
        for (int i4 : handle5) {
            handleVect3.add(new Integer(i4));
        }
        this.plotstyleHandle = DwgUtil.handleBinToHandleInt(handleVect3);
        Vector v13 = DwgUtil.getHandle(data, bitPos11);
        ((Integer) v13.get(0)).intValue();
        int[] handle6 = new int[(v13.size() - 1)];
        for (int j6 = 1; j6 < v13.size(); j6++) {
            handle6[j6 - 1] = ((Integer) v13.get(j6)).intValue();
        }
        Vector handleVect4 = new Vector();
        for (int i5 : handle6) {
            handleVect4.add(new Integer(i5));
        }
        this.linetypeHandle = DwgUtil.handleBinToHandleInt(handleVect4);
    }

    @Override //  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject
    public int getColor() {
        return this.color;
    }

    @Override //  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject
    public void setColor(int color2) {
        this.color = color2;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }
}
