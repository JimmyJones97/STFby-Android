package  com.xzy.forestSystem.gogisapi.Workspace;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.ContourTilesLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.ERasterLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GoogleTilesLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.NetTileLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.RasterLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.TianDiTuTilesLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.XRasterFileLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Geodatabase.ProjectDatabase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RasterLayerWorkspace {
    private List<RasterLayer> m_LayerList = new ArrayList();
    private ProjectDatabase m_ProjectDB = null;

    public void Dispose() {
        try {
            if (this.m_LayerList.size() > 0) {
                for (RasterLayer tmpFeatureLayer : this.m_LayerList) {
                    if (tmpFeatureLayer != null) {
                        tmpFeatureLayer.Dispose();
                    }
                }
                this.m_LayerList.clear();
            }
        } catch (Exception e) {
        }
    }

    public List<RasterLayer> CopyLayerList() {
        List<RasterLayer> result = new ArrayList<>();
        for (RasterLayer rasterLayer : this.m_LayerList) {
            result.add(rasterLayer.Clone());
        }
        return result;
    }

    public List<RasterLayer> GetLayerList() {
        return this.m_LayerList;
    }

    public RasterLayer GetLayerByLayerIndex(int layerIndex) {
        if (layerIndex < 0 || layerIndex >= this.m_LayerList.size()) {
            return null;
        }
        return this.m_LayerList.get(layerIndex);
    }

    private void ClearLayers() {
        this.m_LayerList.clear();
    }

    public void LoadLayers() {
        this.m_LayerList.clear();
        try {
            SQLiteReader localSQLiteDataReader = this.m_ProjectDB.GetSQLiteDatabase().Query("select * from T_RasterLayer Order By SortID");
            if (localSQLiteDataReader != null) {
                while (localSQLiteDataReader.Read()) {
                    RasterLayer tempLayer = new RasterLayer();
                    if (tempLayer.ReadLayerInfo(localSQLiteDataReader)) {
                        this.m_LayerList.add(tempLayer);
                    }
                }
                localSQLiteDataReader.Close();
            }
        } catch (Exception ex) {
            Common.Log("LoadLayer", "错误:" + ex.toString() + "-->" + ex.getMessage());
        }
    }

    public static XLayer CreateRenderLayer(RasterLayer layer) {
        XLayer result = null;
        if (layer == null) {
            return null;
        }
        if (layer.GetLayerType() == ERasterLayerType.FromFile) {
            XLayer result2 = new XRasterFileLayer();
            ((XRasterFileLayer) result2).InitialDataAuthority(layer.getPassowrd());
            ((XRasterFileLayer) result2).ReadLayer(layer);
            result2.setLayerIndex(layer.GetLayerIndex());
            return result2;
        }
        if (layer.GetLayerType() == ERasterLayerType.Google_Satellite) {
            result = new GoogleTilesLayer();
            ((XBaseTilesLayer) result).SetTileType(ERasterLayerType.Google_Satellite);
        } else if (layer.GetLayerType() == ERasterLayerType.Google_Street) {
            result = new GoogleTilesLayer();
            ((XBaseTilesLayer) result).SetTileType(ERasterLayerType.Google_Street);
        } else if (layer.GetLayerType() == ERasterLayerType.Google_Terrain) {
            result = new GoogleTilesLayer();
            ((XBaseTilesLayer) result).SetTileType(ERasterLayerType.Google_Terrain);
        } else if (layer.GetLayerType() == ERasterLayerType.Google_Address) {
            result = new GoogleTilesLayer();
            ((XBaseTilesLayer) result).SetTileType(ERasterLayerType.Google_Address);
        } else if (layer.GetLayerType() == ERasterLayerType.TianDiTu_Satellite) {
            result = new TianDiTuTilesLayer();
            ((XBaseTilesLayer) result).SetTileType(ERasterLayerType.TianDiTu_Satellite);
        } else if (layer.GetLayerType() == ERasterLayerType.TianDiTu_SatelliteLabel) {
            result = new TianDiTuTilesLayer();
            ((XBaseTilesLayer) result).SetTileType(ERasterLayerType.TianDiTu_SatelliteLabel);
        } else if (layer.GetLayerType() == ERasterLayerType.TianDiTu_Vector) {
            result = new TianDiTuTilesLayer();
            ((XBaseTilesLayer) result).SetTileType(ERasterLayerType.TianDiTu_Vector);
        } else if (layer.GetLayerType() == ERasterLayerType.TianDiTu_VectorLabel) {
            result = new TianDiTuTilesLayer();
            ((XBaseTilesLayer) result).SetTileType(ERasterLayerType.TianDiTu_VectorLabel);
        } else if (layer.GetLayerType() == ERasterLayerType.ContourMap) {
            result = new ContourTilesLayer();
            ((XBaseTilesLayer) result).SetTileType(ERasterLayerType.ContourMap);
        } else if (layer.GetLayerType() == ERasterLayerType.NetTileLayer) {
            result = new NetTileLayer();
            ((NetTileLayer) result).initLayer(layer);
        }
        if (result != null) {
            ((XBaseTilesLayer) result).setOffsetX(layer._OffsetX);
            ((XBaseTilesLayer) result).setOffsetY(layer._OffsetY);
            ((XBaseTilesLayer) result).setIsConsiderTranslate(layer.getIsConsiderTranslate());
        }
        if (result == null) {
            return result;
        }
        layer.CopyTo(result);
        return result;
    }

    public boolean RenderLayerForAdd(RasterLayer layer) {
        XLayer tempLayer = CreateRenderLayer(layer);
        if (tempLayer == null) {
            return false;
        }
        PubVar._Map.getRasterLayers().add(tempLayer);
        return true;
    }

    public boolean RenderLayerForAdd(RasterLayer layer, int layerIndex) {
        XLayer tempLayer = CreateRenderLayer(layer);
        if (tempLayer == null) {
            return false;
        }
        PubVar._Map.getRasterLayers().add(layerIndex, tempLayer);
        return true;
    }

    public boolean AddRasterLayer(RasterLayer layer) {
        boolean result = false;
        boolean tempTag = true;
        SQLiteReader tempSqlReader = this.m_ProjectDB.GetSQLiteDatabase().Query("Select Id From T_RasterLayer Where Type='" + layer.GetLayerTypeName() + "' And Path='" + layer.GetFilePath() + "' And Name='" + layer.GetLayerName() + "'");
        if (tempSqlReader != null) {
            if (tempSqlReader.Read()) {
                tempTag = false;
            }
            tempSqlReader.Close();
        }
        if (tempTag && (result = layer.SaveLayerInfo())) {
            this.m_LayerList.add(layer);
        }
        return result;
    }

    public boolean AddRasterLayer(RasterLayer layer, int layerIndex) {
        boolean result = false;
        boolean tempTag = true;
        SQLiteReader tempSqlReader = this.m_ProjectDB.GetSQLiteDatabase().Query("Select Id From T_RasterLayer Where Type='" + layer.GetLayerTypeName() + "' And Path='" + layer.GetFilePath() + "' And Name='" + layer.GetLayerName() + "'");
        if (tempSqlReader != null) {
            if (tempSqlReader.Read()) {
                tempTag = false;
            }
            tempSqlReader.Close();
        }
        if (tempTag && (result = layer.SaveLayerInfo())) {
            this.m_LayerList.add(layerIndex, layer);
        }
        return result;
    }

    public boolean DeleteRasterLayer(RasterLayer layer) {
        boolean result = false;
        if (this.m_LayerList.size() > 0) {
            String tempStrInfo = String.valueOf(layer.GetLayerTypeName()) + "-" + layer.GetFilePath() + "-" + layer.GetLayerName();
            Iterator<RasterLayer> tempIter = this.m_LayerList.iterator();
            while (true) {
                if (!tempIter.hasNext()) {
                    break;
                }
                RasterLayer tempLayer = tempIter.next();
                if (tempStrInfo.equals(String.valueOf(tempLayer.GetLayerTypeName()) + "-" + tempLayer.GetFilePath() + "-" + tempLayer.GetLayerName())) {
                    result = this.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL("Delete From T_RasterLayer Where Type='" + tempLayer.GetLayerTypeName() + "' And Path='" + tempLayer.GetFilePath() + "' And Name='" + tempLayer.GetLayerName() + "'");
                    if (result) {
                        this.m_LayerList.remove(tempLayer);
                    }
                }
            }
        }
        return result;
    }

    public boolean SaveLayers() {
        if (!this.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL("delete from T_RasterLayer")) {
            return false;
        }
        if (this.m_LayerList.size() > 0) {
            for (RasterLayer tempLayer : this.m_LayerList) {
                tempLayer.SaveLayerInfo();
            }
        }
        return true;
    }

    public boolean SaveLayerFormLayerList(List<RasterLayer> paramList) {
        this.m_LayerList.clear();
        if (paramList == null || paramList.size() <= 0) {
            return true;
        }
        for (RasterLayer tempLayer : paramList) {
            this.m_LayerList.add(tempLayer);
        }
        return true;
    }

    public void SetBindProject(ProjectDatabase tmpProject) {
        this.m_ProjectDB = tmpProject;
    }
}
