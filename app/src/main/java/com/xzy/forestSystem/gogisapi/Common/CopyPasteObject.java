package  com.xzy.forestSystem.gogisapi.Common;

import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import java.util.Date;

public class CopyPasteObject {
    private Date m_CopyTime = new Date();
    private ECopyPasteType m_ECopyPasteType = ECopyPasteType.None;
    private Object m_Value = null;

    public void Copy(ECopyPasteType ECopyPasteType, Object value) {
        this.m_ECopyPasteType = ECopyPasteType;
        Copy(value);
    }

    public ECopyPasteType getECopyPasteType() {
        return this.m_ECopyPasteType;
    }

    public void Copy(Object value) {
        this.m_Value = value;
        this.m_CopyTime = new Date();
    }

    public Object getCopyObject() {
        return this.m_Value;
    }

    public Date getCopyTime() {
        return this.m_CopyTime;
    }

    public void CopyAttribute(FeatureLayer layer, String SYSID) {
        Copy(ECopyPasteType.Attribute, new Object[]{layer.GetLayerName(), layer.GetDataSourceName(), layer.GetLayerID(), SYSID});
    }
}
