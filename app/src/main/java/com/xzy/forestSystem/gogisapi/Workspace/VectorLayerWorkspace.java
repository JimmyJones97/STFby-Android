package  com.xzy.forestSystem.gogisapi.Workspace;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoGroupLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.VectorLayers;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geodatabase.ProjectDatabase;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.System.AuthorizeTools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VectorLayerWorkspace {
    private List<FeatureLayer> _LayersList = new ArrayList();
    List<VectorLayers> m_BKVectorLayers = new ArrayList();
    private ProjectDatabase m_ProjectDB = null;

    public void Dispose() {
        try {
            if (this._LayersList.size() > 0) {
                for (FeatureLayer tmpFeatureLayer : this._LayersList) {
                    if (tmpFeatureLayer != null) {
                        tmpFeatureLayer.Dispose();
                    }
                }
                this._LayersList.clear();
            }
            if (this.m_BKVectorLayers.size() > 0) {
                for (VectorLayers tmpVectorLayers : this.m_BKVectorLayers) {
                    if (tmpVectorLayers != null) {
                        tmpVectorLayers.Dispose();
                    }
                }
                this.m_BKVectorLayers.clear();
            }
        } catch (Exception e) {
        }
    }

    public List<VectorLayers> getVectorLayers() {
        return this.m_BKVectorLayers;
    }

    public void Initial() {
        try {
            this._LayersList = new ArrayList();
            LayerRenderWorkspace tmpLayerRenderManage = PubVar._PubCommand.m_ProjectDB.GetLayerRenderManage();
            for (VectorLayers tempBKVLayers : GetBKVectorLayersList()) {
                String tempGroupName = tempBKVLayers.GetLayersPath();
                for (FeatureLayer tmpLayer01 : tempBKVLayers.GetLayerList()) {
                    tmpLayerRenderManage.RenderVectorLayerForNew(tmpLayer01, tempGroupName);
                    this._LayersList.add(tmpLayer01);
                }
                for (DataSet tempDataset : PubVar.m_Workspace.GetDataSourceByUnEditing(tempGroupName).getDatasets()) {
                    Envelope tempExtend = tempDataset.getBindGeoLayer().getExtend();
                    if (tempExtend != null) {
                        if (!(tempBKVLayers.getBiasX() == 0.0d && tempBKVLayers.getBiasY() == 0.0d)) {
                            tempExtend.SetOffset(tempBKVLayers.getBiasX(), tempBKVLayers.getBiasY());
                            tempDataset.SetOffset(tempBKVLayers.getBiasX(), tempBKVLayers.getBiasY());
                        }
                        tempDataset.getBindGeoLayer().SetExtend(tempExtend);
                        tempDataset.GetMapCellIndex().setBigCell(tempExtend);
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            SortLayers();
        }
    }

    public void SortLayers() {
        SQLiteReader tempReader;
        try {
            GeoGroupLayer tempGeoLayers = PubVar._Map.getVectorGeoLayers();
            if (tempGeoLayers.size() > 0 && (tempReader = this.m_ProjectDB.GetSQLiteDatabase().Query("select Path||','||LayerID AS MyFID from T_BKVectorLayer Order By SortID")) != null && tempGeoLayers.getList().size() > 0) {
                int tid = 0;
                while (tempReader.Read()) {
                    String tmpStr = tempReader.GetString(0);
                    BasicValue param = new BasicValue();
                    GeoLayer tempGeoLayer = tempGeoLayers.GetLayerByTag(tmpStr, param);
                    if (!(tempGeoLayer == null || tid == param.getInt())) {
                        tempGeoLayers.Remove(tempGeoLayer);
                        tempGeoLayers.AddGeoLayer(tempGeoLayer, tid);
                    }
                    tid++;
                }
                tempReader.Close();
            }
        } catch (Exception e) {
        }
    }

    private void ClearBKLayer() {
        try {
            this._LayersList.clear();
            if (this.m_BKVectorLayers.size() > 0) {
                PubVar._Map.ClearSelSelection();
                PubVar._Map.getVectorGeoLayers().RemoveAll();
                for (VectorLayers vectorLayers : this.m_BKVectorLayers) {
                    vectorLayers.ClearLayers();
                }
                this.m_BKVectorLayers.clear();
            }
        } catch (Exception e) {
        }
    }

    public List<VectorLayers> GetBKVectorLayersList() {
        return this.m_BKVectorLayers;
    }

    public VectorLayers GetBKVectorLayersByName(String path) {
        for (VectorLayers result : this.m_BKVectorLayers) {
            if (result.GetLayersPath().equals(path)) {
                return result;
            }
        }
        return null;
    }

    public void LoadLayers() {
        ClearBKLayer();
        this.m_BKVectorLayers = new ArrayList();
        try {
            SQLiteReader tempReader = this.m_ProjectDB.GetSQLiteDatabase().Query("select Path,Security from T_BKVectorLayer Group By Path");
            if (tempReader != null) {
                while (tempReader.Read()) {
                    String tmpPath = tempReader.GetString(0);
                    String tmpSecurity = tempReader.GetString(1);
                    if (tmpSecurity == null) {
                        tmpSecurity = "";
                    }
                    if (!tmpSecurity.equals("")) {
                        tmpSecurity = AuthorizeTools.DecryptExpandLayerKey(tmpSecurity);
                    }
                    VectorLayers tempLayers = new VectorLayers(tmpPath, tmpSecurity);
                    tempLayers.SetBindProject(this.m_ProjectDB);
                    this.m_BKVectorLayers.add(tempLayers);
                }
                tempReader.Close();
            }
        } catch (Exception ex) {
            Common.Log("LoadBKLayer", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    public void OpenDataSource() {
        try {
            for (VectorLayers tmpBKVectorLayers : this.m_BKVectorLayers) {
                tmpBKVectorLayers.OpenDataSource();
            }
        } catch (Exception e) {
        }
    }

    public boolean SaveLayers(GeoGroupLayer pGeoLayers) {
        if (pGeoLayers != null) {
            try {
                if (pGeoLayers.size() > 0) {
                    this.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL("Delete From T_BKVectorLayer");
                    StringBuilder tmpSQLSB = new StringBuilder();
                    int tid = 0;
                    for (GeoLayer pGeoLayer : pGeoLayers.getList()) {
                        if (pGeoLayer.getTag() != null) {
                            if (tmpSQLSB.length() > 0) {
                                tmpSQLSB.append(" UNION ");
                            }
                            String tmpPath = pGeoLayer.getTag().toString().split(",")[0];
                            VectorLayers tmpBKVLayers = GetBKVectorLayersByName(tmpPath);
                            tmpSQLSB.append("Insert Into T_BKVectorLayer (LayerID,Name,SortID,Type,Path,CoorSystem,Visible,Transparent,MinX,MinY,MaxX,MaxY,F1,F2,Security) Values (");
                            tmpSQLSB.append("'" + pGeoLayer.getName());
                            tmpSQLSB.append("','" + pGeoLayer.getLayerName());
                            tmpSQLSB.append("'," + tid);
                            tmpSQLSB.append(",'" + pGeoLayer.getGeoTypeName());
                            tmpSQLSB.append("','" + tmpPath);
                            tmpSQLSB.append("','" + tmpBKVLayers.getCoorSystem());
                            tmpSQLSB.append("','" + pGeoLayer.getVisible());
                            tmpSQLSB.append("','" + pGeoLayer.getTransparent());
                            tmpSQLSB.append("','" + pGeoLayer.getMinX());
                            tmpSQLSB.append("','" + pGeoLayer.getMinY());
                            tmpSQLSB.append("','" + pGeoLayer.getMaxX());
                            tmpSQLSB.append("','" + pGeoLayer.getMaxY());
                            tmpSQLSB.append("','" + tmpBKVLayers.getBiasX());
                            tmpSQLSB.append("','" + tmpBKVLayers.getBiasY());
                            String tmpEncryptCode = tmpBKVLayers.GetPassword();
                            if (!tmpEncryptCode.equals("")) {
                                tmpEncryptCode = AuthorizeTools.EncryptExpandLayerKey(tmpEncryptCode);
                            }
                            tmpSQLSB.append("','" + tmpEncryptCode);
                            tmpSQLSB.append("')");
                            tid++;
                        }
                    }
                    this.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL(tmpSQLSB.toString());
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    public boolean SaveLayers(List<HashMap<String, Object>> layersDataList) {
        if (layersDataList != null) {
            try {
                if (layersDataList.size() > 0) {
                    this.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL("Delete From T_BKVectorLayer");
                    StringBuilder tmpSQLSB = new StringBuilder();
                    int tid = 0;
                    tmpSQLSB.append("Insert Into T_BKVectorLayer (LayerID,Name,SortID,Type,Path,CoorSystem,Visible,Transparent,MinX,MinY,MaxX,MaxY,F1,F2,F3,Security) ");
                    boolean tempBool01 = false;
                    for (HashMap<String, Object> tempHash : layersDataList) {
                        if (tempHash != null) {
                            if (tempBool01) {
                                tmpSQLSB.append(" UNION ALL ");
                            }
                            tmpSQLSB.append(" Select ");
                            String tmpPath = tempHash.get("D11").toString();
                            VectorLayers tmpBKVLayers = GetBKVectorLayersByName(tmpPath);
                            tmpSQLSB.append("'" + tempHash.get("LayerID").toString());
                            tmpSQLSB.append("','" + tempHash.get("D12").toString());
                            tmpSQLSB.append("'," + tid);
                            tmpSQLSB.append(",'" + tempHash.get("D5").toString());
                            tmpSQLSB.append("','" + tmpPath);
                            tmpSQLSB.append("','" + tmpBKVLayers.getCoorSystem());
                            tmpSQLSB.append("','" + tempHash.get("D1").toString());
                            tmpSQLSB.append("','");
                            tmpSQLSB.append(255 - Integer.parseInt(tempHash.get("D3").toString()));
                            tmpSQLSB.append("','" + tempHash.get("D6").toString());
                            tmpSQLSB.append("','" + tempHash.get("D7").toString());
                            tmpSQLSB.append("','" + tempHash.get("D8").toString());
                            tmpSQLSB.append("','" + tempHash.get("D9").toString());
                            tmpSQLSB.append("','" + tmpBKVLayers.getBiasX());
                            tmpSQLSB.append("','" + tmpBKVLayers.getBiasY());
                            tmpSQLSB.append("','" + tempHash.get("IsEncrypt").toString());
                            String tmpEncryptCode = String.valueOf(tempHash.get("Security"));
                            if (!tmpEncryptCode.equals("")) {
                                tmpEncryptCode = AuthorizeTools.EncryptExpandLayerKey(tmpEncryptCode);
                            }
                            tmpSQLSB.append("','" + tmpEncryptCode);
                            tmpSQLSB.append("'");
                            tempBool01 = true;
                            tid++;
                        }
                    }
                    this.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL(tmpSQLSB.toString());
                    for (HashMap<String, Object> tempHash2 : layersDataList) {
                        if (tempHash2 != null && tempHash2.containsKey("D40")) {
                            String tempFieldVStr = String.valueOf(tempHash2.get("D40"));
                            StringBuilder tmpSQLSB2 = new StringBuilder();
                            tmpSQLSB2.append("Update T_BKVectorLayer Set Fields ='");
                            tmpSQLSB2.append(tempFieldVStr);
                            tmpSQLSB2.append("' Where LayerID='");
                            tmpSQLSB2.append(tempHash2.get("LayerID").toString());
                            tmpSQLSB2.append("' And Path='");
                            tmpSQLSB2.append(tempHash2.get("D11").toString());
                            tmpSQLSB2.append("'");
                            this.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL(tmpSQLSB2.toString());
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    public void SetBindProject(ProjectDatabase tmpProject) {
        this.m_ProjectDB = tmpProject;
    }

    public List<FeatureLayer> GetLayerList() {
        if (this._LayersList != null) {
            return this._LayersList;
        }
        this._LayersList = new ArrayList();
        List<VectorLayers> tempVectorLayersList = PubVar._PubCommand.m_ProjectDB.GetBKVectorLayerManage().m_BKVectorLayers;
        if (tempVectorLayersList.size() > 0) {
            for (VectorLayers tempBKVLayers : tempVectorLayersList) {
                if (tempBKVLayers != null) {
                    List<FeatureLayer> tempLayers = tempBKVLayers.GetLayerList();
                    if (tempLayers.size() > 0) {
                        for (FeatureLayer tmpLayer01 : tempLayers) {
                            if (tmpLayer01 != null) {
                                this._LayersList.add(tmpLayer01);
                            }
                        }
                    }
                }
            }
        }
        return this._LayersList;
    }

    public List<FeatureLayer> GetLayerListCopy() {
        List<FeatureLayer> result = new ArrayList<>();
        List<FeatureLayer> tmpList = GetLayerList();
        if (tmpList != null && tmpList.size() > 0) {
            for (FeatureLayer featureLayer : tmpList) {
                result.add(featureLayer.Clone());
            }
        }
        return result;
    }

    public FeatureLayer GetLayerByLayerID(String layerID) {
        for (FeatureLayer result : GetLayerList()) {
            if (result.GetLayerID().equals(layerID)) {
                return result;
            }
        }
        return null;
    }

    public GeoLayer FindGeoLayer(String Path, String LayerID) {
        String tempTag = String.valueOf(Path) + "," + LayerID;
        for (GeoLayer result : PubVar._Map.getVectorGeoLayers().getList()) {
            if (result.getTag() != null && result.getTag().toString().equals(tempTag)) {
                return result;
            }
        }
        return null;
    }

    public void RemoveGeoLayer(GeoLayer geoLayer) {
        String[] tempStrs;
        if (!(geoLayer.getTag() == null || (tempStrs = geoLayer.getTag().toString().split(",")) == null || tempStrs.length <= 1)) {
            this.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL("Delete From T_BKVectorLayer Where Path='" + tempStrs[0] + "' And LayerID='" + tempStrs[1] + "'");
            FeatureLayer pLayer = GetLayerByLayerID(tempStrs[1]);
            if (pLayer != null) {
                this._LayersList.remove(pLayer);
            }
        }
        PubVar._Map.getVectorGeoLayers().getList().remove(geoLayer);
    }
}
