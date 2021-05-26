package  com.xzy.forestSystem.gogisapi.Geodatabase;

import  com.xzy.forestSystem.gogisapi.Display.ISymbol;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.EGeoDisplayType;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Selection {
    private DataSet _Dataset = null;
    private List<Integer> _GeometryIndexList = new ArrayList();
    public String _LayerName = "";
    private ISymbol _Style = null;
    public EGeoDisplayType _Type = EGeoDisplayType.NONE;
    private Envelope localEnvelope = null;

    public boolean Add(int objectIndex) {
        if (getGeometryIndexList().indexOf(Integer.valueOf(objectIndex)) < 0) {
            getGeometryIndexList().add(Integer.valueOf(objectIndex));
        }
        return true;
    }

    public boolean AddBySysID(String sysID) {
        AbstractGeometry tmpGeometry;
        if (this._Dataset == null || (tmpGeometry = this._Dataset.GetGeometryBySYSID(sysID)) == null) {
            return false;
        }
        return Add(tmpGeometry.getIndex());
    }

    public boolean AddWithNotRepeat(int objectIndex) {
        getGeometryIndexList().add(Integer.valueOf(objectIndex));
        return true;
    }

    public Selection Clone() {
        Selection tmpSel = new Selection();
        tmpSel._Dataset = this._Dataset;
        tmpSel._Type = this._Type;
        tmpSel._Style = this._Style;
        tmpSel._LayerName = this._LayerName;
        for (Integer num : this._GeometryIndexList) {
            tmpSel._GeometryIndexList.add(Integer.valueOf(num.intValue()));
        }
        return tmpSel;
    }

    public boolean Add(AbstractGeometry paramGeometry) {
        return Add(paramGeometry.getIndex());
    }

    public boolean AddWithNotRepeat(AbstractGeometry paramGeometry) {
        return AddWithNotRepeat(paramGeometry.getIndex());
    }

    public boolean FromRecordset() {
        return true;
    }

    public boolean Remove(int index) {
        return getGeometryIndexList().remove(Integer.valueOf(index));
    }

    public boolean RemoveBySysID(int SysID) {
        AbstractGeometry tempGeo;
        if (this._Dataset == null || (tempGeo = this._Dataset.GetGeometryBySYSID(String.valueOf(SysID))) == null) {
            return false;
        }
        return Remove(tempGeo.getIndex());
    }

    public boolean Remove(AbstractGeometry paramGeometry) {
        if (paramGeometry != null) {
            return Remove(paramGeometry.getIndex());
        }
        return true;
    }

    public boolean RemoveAll() {
        this._GeometryIndexList.clear();
        return true;
    }

    public int getCount() {
        return this._GeometryIndexList.size();
    }

    public DataSet getDataset() {
        return this._Dataset;
    }

    public Envelope getEnvelope() {
        if (getGeometryIndexList().size() == 0) {
            this.localEnvelope = null;
            return this.localEnvelope;
        }
        Envelope localEnvelope2 = this._Dataset.GetGeometry(getGeometryIndexList().get(0).intValue()).getEnvelope();
        int i = 1;
        while (true) {
            if (i < getGeometryIndexList().size()) {
            }
            localEnvelope2 = localEnvelope2.MergeEnvelope(this._Dataset.GetGeometry(getGeometryIndexList().get(i).intValue()).getEnvelope());
            i++;
        }
    }

    public List<Integer> getGeometryIndexList() {
        return this._GeometryIndexList;
    }

    public List<String> getSYSIDList() {
        List<String> result = new ArrayList<>();
        if (this._GeometryIndexList.size() > 0) {
            for (Integer num : this._GeometryIndexList) {
                AbstractGeometry tempGeo = this._Dataset.GetGeometry(num.intValue());
                if (tempGeo != null) {
                    result.add(tempGeo.GetSYS_ID());
                }
            }
        }
        return result;
    }

    public void RefreshtGeometryIndexList() {
        ArrayList<Integer> result = new ArrayList<>();
        for (Integer tempIndex : this._GeometryIndexList) {
            if (Collections.frequency(result, tempIndex) < 1) {
                result.add(tempIndex);
            }
        }
        this._GeometryIndexList = result;
    }

    public ISymbol getStyle() {
        return this._Style;
    }

    public EGeoDisplayType getType() {
        return this._Type;
    }

    public void setDataset(DataSet paramDataset) {
        this._Dataset = paramDataset;
    }

    public void setStyle(ISymbol paramISymbol) {
        this._Style = paramISymbol;
    }

    public void setType(EGeoDisplayType paramlkSelectionType) {
        this._Type = paramlkSelectionType;
    }
}
