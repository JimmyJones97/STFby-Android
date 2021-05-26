package  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo;

import java.util.ArrayList;
import java.util.Iterator;

public class Block extends Element {
    private Data startTag;

    public Block() {
        ArrayList arrayList = this.dataAcceptanceList;
        int[] iArr = new int[14];
        iArr[1] = 5;
        iArr[2] = 102;
        iArr[3] = 330;
        iArr[4] = 100;
        iArr[5] = 8;
        iArr[6] = 70;
        iArr[7] = 10;
        iArr[8] = 20;
        iArr[9] = 30;
        iArr[10] = 3;
        iArr[11] = 1;
        iArr[12] = 4;
        iArr[13] = 2;
        arrayList.add(iArr);
        this.startTag = new Data(0, "BLOCK");
    }

    public Block(String s) {
        super(s);
    }

    public void SetEndBlk(EndBlk eb) {
        if (this.elements.size() > 0 && ((Element) this.elements.get(this.elements.size() - 1)).getName() == "ENDBLK") {
            this.elements.remove(this.data.size() - 1);
        }
        this.elements.add(eb);
    }

    public void AddEntity(Entity e) {
        if (this.data.size() == 0) {
            this.elements.add(e);
        } else {
            this.elements.add(this.elements.size() - 1, e);
        }
    }

    public void SetLayer(String l) {
        int ind = GetIndexFor(8);
        if (ind > -1) {
            this.data.remove(ind);
            this.data.add(ind, new Data(8, l));
            return;
        }
        this.data.add(new Data(8, l));
    }

    public void SetPosition(double x, double y, double z) {
        Data dx = null;
        Data dy = null;
        Data dz = null;
        boolean swx = false;
        boolean swy = false;
        boolean swz = false;
        Iterator it = this.data.iterator();
        while (it.hasNext()) {
            Data d = (Data) it.next();
            if (d.code == 10) {
                dx = d;
                swx = true;
            }
            if (d.code == 20) {
                dy = d;
                swy = true;
            }
            if (d.code == 30) {
                dz = d;
                swz = true;
            }
        }
        if (swx) {
            dx.data = Double.valueOf(x);
        } else {
            dx.code = 10;
            dx.data = Double.valueOf(x);
            this.data.add(dx);
        }
        if (swy) {
            dy.data = Double.valueOf(y);
        } else {
            dy.code = 20;
            dy.data = Double.valueOf(y);
            this.data.add(dy);
        }
        if (swz) {
            dz.data = Double.valueOf(z);
            return;
        }
        dy.code = 30;
        dy.data = Double.valueOf(y);
        this.data.add(dy);
    }

    public void SetName(String name) throws UnexpectedElement {
        AddReplace(2, name);
    }

    public void SetHandle(String handle) throws UnexpectedElement {
        AddReplace(5, handle);
    }

    public void SetFlag(short flag) throws UnexpectedElement {
        AddReplace(70, Short.valueOf(flag));
    }
}
