package  com.xzy.forestSystem.gogisapi.Carto;

import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import java.util.ArrayList;
import java.util.List;

public class MapCellIndex {
    private Envelope _BigCell;
    private int[] _TreeLevel = {64, 32, 16, 8, 4, 2, 1};
    private int _TreeLevelLength = 7;
    private double[] m_bigCellHNums = null;
    private double[] m_bigCellWNums = null;

    public List<Integer> CalCellIndex(Coordinate pCoordinate) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < this._TreeLevelLength; i++) {
            BasicValue localParam1 = new BasicValue();
            BasicValue localParam2 = new BasicValue();
            int cells = this._TreeLevel[i];
            CalCellIndexByPoint(pCoordinate.getX(), pCoordinate.getY(), cells, localParam1, localParam2);
            list.add(Integer.valueOf(((this._TreeLevelLength - i) * 100000000) + (localParam1.getInt() * cells) + localParam2.getInt()));
        }
        return list;
    }

    public List<Integer> CalCellIndexOfPoint(Coordinate pCoordinate) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < this._TreeLevelLength; i++) {
            BasicValue localParam1 = new BasicValue();
            BasicValue localParam2 = new BasicValue();
            CalCellIndexByPoint(pCoordinate.getX(), pCoordinate.getY(), this._TreeLevel[i], localParam1, localParam2);
            int tempInt01 = (this._TreeLevelLength - i) * 10000;
            list.add(Integer.valueOf(localParam1.getInt() + tempInt01));
            list.add(Integer.valueOf(localParam2.getInt() + tempInt01));
        }
        return list;
    }

    public String CalCellIndex(Envelope pEnve) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < this._TreeLevelLength; i++) {
            BasicValue localParam1 = new BasicValue();
            BasicValue localParam2 = new BasicValue();
            BasicValue localParam3 = new BasicValue();
            BasicValue localParam4 = new BasicValue();
            int cells = this._TreeLevel[i];
            CalCellIndexByPoint(pEnve.getLeftTop().getX(), pEnve.getLeftTop().getY(), cells, localParam1, localParam4);
            CalCellIndexByPoint(pEnve.getRightBottom().getX(), pEnve.getRightBottom().getY(), cells, localParam2, localParam3);
            int tempInt01 = (this._TreeLevelLength - i) * 10000;
            StringBuilder tempSB = new StringBuilder();
            tempSB.append("(RIndex>=");
            tempSB.append(localParam2.getInt() + tempInt01);
            tempSB.append(" and RIndex<=");
            tempSB.append(localParam1.getInt() + tempInt01);
            tempSB.append(" and CIndex>=");
            tempSB.append(localParam4.getInt() + tempInt01);
            tempSB.append(" and CIndex<=");
            tempSB.append(localParam3.getInt() + tempInt01);
            tempSB.append(")");
            list.add(tempSB.toString());
        }
        return Common.CombineStrings(" or ", list);
    }

    public List<Integer> CalCellIndexExtend(Envelope pEnve) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < this._TreeLevelLength; i++) {
            BasicValue localParam1 = new BasicValue();
            BasicValue localParam2 = new BasicValue();
            BasicValue localParam3 = new BasicValue();
            BasicValue localParam4 = new BasicValue();
            int cells = this._TreeLevel[i];
            CalCellIndexByPoint(pEnve.getLeftTop().getX(), pEnve.getLeftTop().getY(), cells, localParam1, localParam4);
            CalCellIndexByPoint(pEnve.getRightBottom().getX(), pEnve.getRightBottom().getY(), cells, localParam2, localParam3);
            int tempInt01 = (this._TreeLevelLength - i) * 10000;
            list.add(Integer.valueOf(localParam2.getInt() + tempInt01));
            list.add(Integer.valueOf(localParam1.getInt() + tempInt01));
            list.add(Integer.valueOf(localParam4.getInt() + tempInt01));
            list.add(Integer.valueOf(localParam3.getInt() + tempInt01));
        }
        return list;
    }

    private void CalCellIndexByPoint(double X, double Y, int Cells, BasicValue Row, BasicValue Col) {
        double num = getBigCell().getHeight() / ((double) Cells);
        double num2 = getBigCell().getWidth() / ((double) Cells);
        int tempRow = (int) ((Y - getBigCell().getMinY()) / num);
        int tempCol = (int) ((X - getBigCell().getMinX()) / num2);
        if (tempCol >= Cells) {
            tempCol = Cells - 1;
        } else if (tempCol < 0) {
            tempCol = 0;
        }
        if (tempRow >= Cells) {
            tempRow = Cells - 1;
        } else if (tempRow < 0) {
            tempRow = 0;
        }
        Row.setValue(tempRow);
        Col.setValue(tempCol);
    }

    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x0030: APUT  (r6v0 'rcIndex' int[] A[D('rcIndex' int[])]), (0 ??[int, short, byte, char]), (r8v2 'tempRow' int A[D('tempRow' int)]) */
    private int[] CalCellIndexByPoint(double X, double Y, int index, int Cells) {
        int[] rcIndex = new int[2];
        double num = this.m_bigCellHNums[index];
        double num2 = this.m_bigCellWNums[index];
        int tempRow = (int) ((Y - getBigCell().getMinY()) / num);
        int tempCol = (int) ((X - getBigCell().getMinX()) / num2);
        if (tempCol >= Cells) {
            tempCol = Cells - 1;
        } else if (tempCol < 0) {
            tempCol = 0;
        }
        if (tempRow >= Cells) {
            tempRow = Cells - 1;
        } else if (tempRow < 0) {
            tempRow = 0;
        }
        rcIndex[0] = tempRow;
        rcIndex[1] = tempCol;
        return rcIndex;
    }

    public String CalCellIndexOne(Envelope pEnve) {
        for (int i = 0; i < this._TreeLevelLength; i++) {
            BasicValue localParam1 = new BasicValue();
            BasicValue localParam2 = new BasicValue();
            BasicValue localParam3 = new BasicValue();
            BasicValue localParam4 = new BasicValue();
            int cells = this._TreeLevel[i];
            CalCellIndexByPoint(pEnve.getLeftTop().getX(), pEnve.getLeftTop().getY(), cells, localParam1, localParam4);
            CalCellIndexByPoint(pEnve.getRightBottom().getX(), pEnve.getRightBottom().getY(), cells, localParam2, localParam3);
            if (localParam1.getInt() == localParam2.getInt() && localParam3.getInt() == localParam4.getInt()) {
                return String.valueOf(((this._TreeLevelLength - i) * 10000) + localParam1.getInt()) + "," + (((this._TreeLevelLength - i) * 10000) + localParam3.getInt());
            }
        }
        return "0,0";
    }

    public int[] CalCellIndexsOne(Envelope pEnve) {
        for (int i = 0; i < this._TreeLevelLength; i++) {
            BasicValue localParam1 = new BasicValue();
            BasicValue localParam2 = new BasicValue();
            BasicValue localParam3 = new BasicValue();
            BasicValue localParam4 = new BasicValue();
            int cells = this._TreeLevel[i];
            CalCellIndexByPoint(pEnve.getLeftTop().getX(), pEnve.getLeftTop().getY(), cells, localParam1, localParam4);
            CalCellIndexByPoint(pEnve.getRightBottom().getX(), pEnve.getRightBottom().getY(), cells, localParam2, localParam3);
            if (localParam1.getInt() == localParam2.getInt() && localParam3.getInt() == localParam4.getInt()) {
                return new int[]{((this._TreeLevelLength - i) * 10000) + localParam1.getInt(), ((this._TreeLevelLength - i) * 10000) + localParam3.getInt()};
            }
        }
        return null;
    }

    public int[] CalCellIndexsOne(double minX, double minY, double maxX, double maxY) {
        for (int i = 0; i < this._TreeLevelLength; i++) {
            int cells = this._TreeLevel[i];
            int[] localParam14 = CalCellIndexByPoint(minX, maxY, i, cells);
            int[] localParam23 = CalCellIndexByPoint(maxX, minY, i, cells);
            if (localParam14[0] == localParam23[0] && localParam23[1] == localParam14[1]) {
                return new int[]{((this._TreeLevelLength - i) * 10000) + localParam14[0], ((this._TreeLevelLength - i) * 10000) + localParam23[1]};
            }
        }
        return null;
    }

    public Envelope getBigCell() {
        if (this._BigCell == null) {
            this._BigCell = new Envelope(7850000.0d, 5650000.0d, 1.48E7d, 1570000.0d);
        }
        return this._BigCell;
    }

    public Envelope getBigCell2() {
        return this._BigCell;
    }

    public void setBigCell(Envelope value) {
        this._BigCell = value;
        this.m_bigCellHNums = new double[this._TreeLevelLength];
        this.m_bigCellWNums = new double[this._TreeLevelLength];
        double tmpW = getBigCell().getWidth();
        double tmpH = getBigCell().getHeight();
        for (int i = 0; i < this._TreeLevelLength; i++) {
            this.m_bigCellHNums[i] = tmpH / ((double) this._TreeLevel[i]);
            this.m_bigCellWNums[i] = tmpW / ((double) this._TreeLevel[i]);
        }
    }
}
