package  com.xzy.forestSystem.gogisapi.Workspace;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Geodatabase.ProjectDatabase;
import java.util.ArrayList;
import java.util.List;

public class LayerWorkspace {
    private List<FeatureLayer> m_LayerList = new ArrayList();
    private ProjectDatabase m_ProjectDB = null;

    public void Dispose() {
        try {
            if (this.m_LayerList.size() > 0) {
                for (FeatureLayer tmpFeatureLayer : this.m_LayerList) {
                    if (tmpFeatureLayer != null) {
                        tmpFeatureLayer.Dispose();
                    }
                }
                this.m_LayerList.clear();
            }
        } catch (Exception e) {
        }
    }

    public boolean CheckLayerValid(String paramString) {
        if (GetLayerByID(paramString) != null) {
            return true;
        }
        Common.ShowDialog(PubVar._PubCommand.m_Context, "请选择有效的数据图层！");
        return false;
    }

    public List<FeatureLayer> CopyLayerList() {
        List<FeatureLayer> result = new ArrayList<>();
        for (FeatureLayer featureLayer : this.m_LayerList) {
            result.add(featureLayer.Clone());
        }
        return result;
    }

    public FeatureLayer GetLayerByID(String layerID) {
        if (this.m_LayerList.size() > 0) {
            for (FeatureLayer result : this.m_LayerList) {
                if (result != null && result.GetLayerID().equals(layerID)) {
                    return result;
                }
            }
        }
        return null;
    }

    public String GetLayerIDByName(String layerName) {
        for (FeatureLayer localv1_Layer : this.m_LayerList) {
            if (localv1_Layer.GetLayerName().equals(layerName)) {
                return localv1_Layer.GetLayerID();
            }
        }
        return "";
    }

    public List<FeatureLayer> GetLayerList() {
        return this.m_LayerList;
    }

    public int GetLayersCount() {
        if (this.m_LayerList != null) {
            return this.m_LayerList.size();
        }
        return 0;
    }

    public void LoadLayer() {
        this.m_LayerList.clear();
        SQLiteReader localSQLiteDataReader = this.m_ProjectDB.GetSQLiteDatabase().Query("select * from T_Layer order by SortID");
        if (localSQLiteDataReader != null) {
            String tmpDatasourceName = this.m_ProjectDB.GetProjectManage().GetProjectDataFileName();
            while (localSQLiteDataReader.Read()) {
                FeatureLayer tempLayer = new FeatureLayer();
                if (tempLayer.ReadLayerInfo(localSQLiteDataReader)) {
                    tempLayer.SetDataSourceName(tmpDatasourceName);
                    this.m_LayerList.add(tempLayer);
                }
            }
            localSQLiteDataReader.Close();
        }
    }

    public boolean SaveLayerFormLayerList(List<FeatureLayer> paramList) {
        this.m_LayerList.clear();
        if (paramList == null || paramList.size() <= 0) {
            return true;
        }
        for (FeatureLayer tempLayer : paramList) {
            this.m_LayerList.add(tempLayer);
        }
        return true;
    }

    public int GetLayerIndexFormLayerList(FeatureLayer layer) {
        int result = 0;
        if (this.m_LayerList != null && this.m_LayerList.size() > 0) {
            for (FeatureLayer tempLayer : this.m_LayerList) {
                if (tempLayer.equals(layer)) {
                    return result;
                }
                result++;
            }
        }
        return -1;
    }

    public boolean UpdateLayerInLayerList(FeatureLayer layer) {
        int tempIndex = GetLayerIndexFormLayerList(layer);
        if (tempIndex < 0) {
            return false;
        }
        this.m_LayerList.remove(tempIndex);
        this.m_LayerList.add(tempIndex, layer);
        return true;
    }

    public boolean AddNewLayer(FeatureLayer layer) {
        if (GetLayerByID(layer.GetLayerID()) != null) {
            return false;
        }
        this.m_LayerList.add(layer);
        return true;
    }

    public boolean InsertNewLayer(FeatureLayer layer, int index) {
        if (GetLayerByID(layer.GetLayerID()) != null) {
            return false;
        }
        if (index > this.m_LayerList.size()) {
            index = this.m_LayerList.size();
        }
        this.m_LayerList.add(index, layer);
        return true;
    }

    public void SetBindProject(ProjectDatabase tmpProject) {
        this.m_ProjectDB = tmpProject;
    }
}
