package  com.xzy.forestSystem.gogisapi.Geometry;

import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.MapCellIndex;
import  com.xzy.forestSystem.gogisapi.Display.ISymbol;
import  com.xzy.forestSystem.gogisapi.Edit.EGeometryStatus;
import java.util.ArrayList;
import java.util.List;

/* renamed from:  com.xzy.forestSystem.gogisapi.Geometry.Geometry */
public abstract class AbstractGeometry {
    int _ColIndex = 0;
    List<Coordinate> _CoorList = null;
    boolean _Edited = false;
    Envelope _Envelope;
    int _Index;
    boolean _IsSimple = true;
    String _LabelContent = "";
    String _OID;
    List<Integer> _PartIndex = new ArrayList();
    int _RowIndex = 0;
    String _SYSID;
    EGeometryStatus _Status = EGeometryStatus.NONE;
    ISymbol _Symbol = null;
    int _SysIDIndex = -1;
    Object _TAGObject = null;
    String _Tag = "";

    public abstract AbstractGeometry Clone();

    public abstract EGeometryType GetGeometryType();

    public abstract EGeoLayerType GetType();

    public abstract boolean HitTest(Coordinate coordinate, double d);

    public abstract boolean Offset(double d, double d2);

    public void Dispose2() {
        try {
            if (this._PartIndex.size() > 0) {
                this._PartIndex.clear();
            }
            this._TAGObject = null;
            if (this._CoorList != null && this._CoorList.size() > 0) {
                this._CoorList.clear();
            }
            this._CoorList = null;
        } catch (Exception e) {
        }
    }

    public void ResetData() {
        this._RowIndex = 0;
        this._ColIndex = 0;
        this._Envelope = null;
        this._Edited = true;
    }

    public void CalCellIndex(MapCellIndex m_MapCellIndex) {
        int[] tempRCIndex = m_MapCellIndex.CalCellIndexsOne(getEnvelope());
        if (tempRCIndex != null) {
            this._RowIndex = tempRCIndex[0];
            this._ColIndex = tempRCIndex[1];
        }
    }

    public Envelope CalEnvelope() {
        this._Envelope = null;
        int count = getItems().size();
        Coordinate localCoordinate = getItems().get(0);
        double tmpXMin = localCoordinate.getX();
        double tmpYMin = localCoordinate.getY();
        double tmpXMax = tmpXMin;
        double tmpYMax = tmpYMin;
        for (int i = 1; i < count; i++) {
            Coordinate localCoordinate2 = getItems().get(i);
            if (tmpXMin > localCoordinate2.getX()) {
                tmpXMin = localCoordinate2.getX();
            }
            if (tmpXMax < localCoordinate2.getX()) {
                tmpXMax = localCoordinate2.getX();
            }
            if (tmpYMin > localCoordinate2.getY()) {
                tmpYMin = localCoordinate2.getY();
            }
            if (tmpYMax < localCoordinate2.getY()) {
                tmpYMax = localCoordinate2.getY();
            }
        }
        this._Envelope = new Envelope(tmpXMin, tmpYMax, tmpXMax, tmpYMin);
        return this._Envelope;
    }

    public List<Coordinate> GetAllCoordinateList() {
        return getItems();
    }

    public boolean GetEdited() {
        return this._Edited;
    }

    public boolean IsNull() {
        if (this._CoorList == null || this._CoorList.size() == 0) {
            return true;
        }
        return false;
    }

    public void IsSimple(boolean paramBoolean) {
        this._IsSimple = paramBoolean;
    }

    public boolean IsSimple() {
        if (this._PartIndex.size() > 1) {
            this._IsSimple = false;
        } else {
            this._IsSimple = true;
        }
        return this._IsSimple;
    }

    public void SetAllCoordinateList(List<Coordinate> paramList) {
        setItems(paramList);
    }

    public void SetEdited(boolean paramBoolean) {
        this._Edited = paramBoolean;
    }

    public void SetNull() {
        this._CoorList = null;
    }

    public void UpdateCoordinate(double biasX, double biasY) {
        for (Coordinate localCoordinate : getItems()) {
            localCoordinate.setX(localCoordinate.getX() + biasX);
            localCoordinate.setY(localCoordinate.getY() + biasY);
        }
        UpdateEnvelope();
    }

    public void UpdateEnvelope() {
        this._Envelope = CalEnvelope();
    }

    public Envelope getEnvelope() {
        if (this._Envelope == null) {
            UpdateEnvelope();
        }
        return this._Envelope;
    }

    public String getOID() {
        return this._OID;
    }

    public int getIndex() {
        return this._Index;
    }

    /* access modifiers changed from: protected */
    public List<Coordinate> getItems() {
        if (this._CoorList == null) {
            this._CoorList = new ArrayList();
        }
        return this._CoorList;
    }

    public EGeometryStatus getStatus() {
        return this._Status;
    }

    public ISymbol getSymbol() {
        return this._Symbol;
    }

    public String getTag() {
        return this._Tag;
    }

    public void setEnvelope(Envelope paramEnvelope) {
        this._Envelope = paramEnvelope;
    }

    public void setOID(String paramString) {
        this._OID = paramString;
    }

    public void setIndex(int paramInt) {
        this._Index = paramInt;
    }

    /* access modifiers changed from: protected */
    public void setItems(List<Coordinate> paramList) {
        this._CoorList = paramList;
    }

    public void setStatus(EGeometryStatus paramlkGeometryStatus) {
        this._Status = paramlkGeometryStatus;
    }

    public void setSymbol(ISymbol paramISymbol) {
        this._Symbol = paramISymbol;
    }

    public void setTag(String paramString) {
        this._Tag = paramString;
    }

    public void SetSYS_ID(String SYS_ID) {
        this._SYSID = SYS_ID;
        if (this._SYSID != null && !this._SYSID.equals("")) {
            this._SysIDIndex = Integer.parseInt(SYS_ID);
        }
    }

    public String GetSYS_ID() {
        return this._SYSID;
    }

    public int GetSYS_IDIndex() {
        return this._SysIDIndex;
    }

    public void AddPart(List<Coordinate> paramList) {
        if (this._PartIndex.size() == 0) {
            setItems(paramList);
            this._PartIndex.add(0);
            IsSimple(true);
            return;
        }
        this._PartIndex.add(Integer.valueOf(getItems().size()));
        for (Coordinate coordinate : paramList) {
            this._CoorList.add(coordinate);
        }
        IsSimple(false);
    }

    public void SetPartIndex(List<Integer> partIndex) {
        this._PartIndex = partIndex;
    }

    public int GetNumberOfPoints() {
        return GetAllCoordinateList().size();
    }

    public int GetNumberOfParts() {
        if (this._PartIndex.size() > 1) {
            return this._PartIndex.size();
        }
        return 1;
    }

    public List<Integer> GetPartIndex() {
        return this._PartIndex;
    }

    public void setLabelContent(String labelContent) {
        this._LabelContent = labelContent;
    }

    public String getLabelContent() {
        return this._LabelContent;
    }

    public void SetRowIndex(int index) {
        this._RowIndex = index;
    }

    public void SetColIndex(int index) {
        this._ColIndex = index;
    }

    public int GetRowIndex() {
        return this._RowIndex;
    }

    public int GetColIndex() {
        return this._ColIndex;
    }

    public int getCoordsCount() {
        return getItems().size();
    }

    public void SetTAGObject(Object object) {
        this._TAGObject = object;
    }

    public Object getTAGObject() {
        return this._TAGObject;
    }

    public Coordinate getPoint(int index) {
        return GetAllCoordinateList().get(index);
    }

    public String ConvertCoordString(String splitChar) {
        StringBuilder tmpSB = new StringBuilder();
        if (this._CoorList != null && this._CoorList.size() > 0) {
            int tmpI = 0;
            for (Coordinate tmpCoordinate : this._CoorList) {
                tmpI++;
                if (tmpI > 1) {
                    tmpSB.append(splitChar);
                }
                tmpSB.append(String.format("%.3f", Double.valueOf(tmpCoordinate.getX())));
                tmpSB.append(splitChar);
                tmpSB.append(String.format("%.3f", Double.valueOf(tmpCoordinate.getY())));
                tmpSB.append(splitChar);
                tmpSB.append(String.format("%.3f", Double.valueOf(tmpCoordinate.getZ())));
            }
        }
        return tmpSB.toString();
    }
}
