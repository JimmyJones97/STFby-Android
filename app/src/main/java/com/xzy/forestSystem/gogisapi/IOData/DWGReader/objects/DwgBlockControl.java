package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgBlockControl extends DwgObject {
    private Vector code2Handles;
    private int modelSpaceHandle;
    private int nullHandle;
    private int paperSpaceHandle;

    public void readDwgBlockControlV15(int[] data, int offset) throws Exception {
        Vector v = DwgUtil.getBitLong(data, offset);
        int bitPos = ((Integer) v.get(0)).intValue();
        setNumReactors(((Integer) v.get(1)).intValue());
        Vector v2 = DwgUtil.getBitShort(data, bitPos);
        int bitPos2 = ((Integer) v2.get(0)).intValue();
        int Enum = ((Integer) v2.get(1)).intValue();
        Vector v3 = DwgUtil.getHandle(data, bitPos2);
        int bitPos3 = ((Integer) v3.get(0)).intValue();
        int[] handle = new int[(v3.size() - 1)];
        for (int i = 1; i < v3.size(); i++) {
            handle[i - 1] = ((Integer) v3.get(i)).intValue();
        }
        Vector handleVect = new Vector();
        for (int i2 : handle) {
            handleVect.add(new Integer(i2));
        }
        this.nullHandle = DwgUtil.handleBinToHandleInt(handleVect);
        Vector v4 = DwgUtil.getHandle(data, bitPos3);
        int bitPos4 = ((Integer) v4.get(0)).intValue();
        int[] handle2 = new int[(v4.size() - 1)];
        for (int i3 = 1; i3 < v4.size(); i3++) {
            handle2[i3 - 1] = ((Integer) v4.get(i3)).intValue();
        }
        if (Enum > 0) {
            Vector handles = new Vector();
            for (int i4 = 0; i4 < Enum; i4++) {
                Vector v5 = DwgUtil.getHandle(data, bitPos4);
                bitPos4 = ((Integer) v5.get(0)).intValue();
                int[] handle3 = new int[(v5.size() - 1)];
                for (int j = 1; j < v5.size(); j++) {
                    handle3[j - 1] = ((Integer) v5.get(j)).intValue();
                }
                handles.add(handle3);
            }
            this.code2Handles = handles;
        }
        Vector v6 = DwgUtil.getHandle(data, bitPos4);
        int bitPos5 = ((Integer) v6.get(0)).intValue();
        int[] handle4 = new int[(v6.size() - 1)];
        for (int j2 = 1; j2 < v6.size(); j2++) {
            handle4[j2 - 1] = ((Integer) v6.get(j2)).intValue();
        }
        Vector handleVect2 = new Vector();
        for (int i5 : handle4) {
            handleVect2.add(new Integer(i5));
        }
        this.modelSpaceHandle = DwgUtil.handleBinToHandleInt(handleVect2);
        Vector v7 = DwgUtil.getHandle(data, bitPos5);
        ((Integer) v7.get(0)).intValue();
        int[] handle5 = new int[(v7.size() - 1)];
        for (int j3 = 1; j3 < v7.size(); j3++) {
            handle5[j3 - 1] = ((Integer) v7.get(j3)).intValue();
        }
        Vector handleVect3 = new Vector();
        for (int i6 : handle5) {
            handleVect3.add(new Integer(i6));
        }
        this.paperSpaceHandle = DwgUtil.handleBinToHandleInt(handleVect3);
    }
}
