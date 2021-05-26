package  com.xzy.forestSystem.gogisapi.Workspace;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.ELayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Display.IRender;
import  com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSource;

public class LayerRenderWorkspace {
    private boolean RenderLayer(FeatureLayer layer, EEditMode paramlkEditMode) {
        GeoLayer localGeoLayer;
        if (paramlkEditMode == EEditMode.NEW) {
            DataSource localDataSource = PubVar.m_Workspace.GetDataSourceByEditing();
            DataSet localDataset = new DataSet(localDataSource);
            localDataset.setName(layer.GetLayerID());
            localDataset.setLayerName(layer.GetLayerName());
            localDataset.setType(layer.GetLayerType());
            localDataSource.getDatasets().add(localDataset);
            GeoLayer localGeoLayer2 = new GeoLayer();
            localGeoLayer2.setLayerType(ELayerType.FEATURE);
            localGeoLayer2.setDataset(localDataset);
            localGeoLayer2.setName(localDataset.getName());
            localGeoLayer2.setType(localDataset.getType());
            localDataset.setBindGeoLayer(localGeoLayer2);
            PubVar._Map.getGeoLayers().AddLayer(localGeoLayer2);
            RenderLayer(layer, EEditMode.EDIT);
            return true;
        } else if (paramlkEditMode != EEditMode.EDIT || (localGeoLayer = PubVar._Map.getGeoLayers().GetLayerByName(layer.GetLayerID())) == null) {
            return true;
        } else {
            localGeoLayer.UpdateFromLayer(layer);
            RenderLayerForGeoLayer(localGeoLayer, layer);
            return true;
        }
    }

    private void RenderLayerForGeoLayer(GeoLayer geoLayer, FeatureLayer layer) {
        geoLayer.setVisible(layer.GetVisible());
        if (geoLayer.getRender() == null) {
            geoLayer.setRender(IRender.CreateRender(geoLayer, layer.GetRenderType()));
        }
        geoLayer.getRender().LoadSymbol(layer);
        geoLayer.getDataset().UpdateAllGeometrysSymbol();
    }

    public boolean RenderVectorLayerForNew(FeatureLayer layer, String groupName) {
        DataSource tempDataSource = PubVar.m_Workspace.GetDataSourceByUnEditing(groupName);
        if (tempDataSource == null) {
            return false;
        }
        DataSet localDataset = tempDataSource.GetDatasetByName(layer.GetLayerID());
        if (localDataset == null) {
            localDataset = new DataSet(tempDataSource);
            localDataset.setName(layer.GetLayerID());
            localDataset.setType(layer.GetLayerType());
            tempDataSource.getDatasets().add(localDataset);
        }
        GeoLayer localGeoLayer = new GeoLayer();
        localGeoLayer.setLayerType(ELayerType.VECTOR);
        localGeoLayer.setDataset(localDataset);
        localGeoLayer.setName(localDataset.getName());
        localGeoLayer.setType(localDataset.getType());
        localGeoLayer.setRenderType(layer.GetRenderType());
        localGeoLayer.UpdateFromLayer(layer);
        localGeoLayer.SetTag(String.valueOf(groupName) + "," + layer.GetLayerID());
        localDataset.setBindGeoLayer(localGeoLayer);
        PubVar._Map.getVectorGeoLayers().AddLayer(localGeoLayer);
        RenderVectorLayerForGeoLayer(localGeoLayer, layer);
        return true;
    }

    private void RenderVectorLayerForGeoLayer(GeoLayer geoLayer, FeatureLayer layer) {
        geoLayer.setVisible(layer.GetVisible());
        if (geoLayer.getRender() == null) {
            geoLayer.setRender(IRender.CreateRender(geoLayer, layer.GetRenderType()));
        }
        geoLayer.getRender().LoadSymbol(layer, (Object[]) layer.getTag());
        geoLayer.getDataset().UpdateAllGeometrysSymbol();
    }

    public boolean RenderLayerForNew(FeatureLayer pLayer) {
        return RenderLayer(pLayer, EEditMode.NEW);
    }

    public boolean RenderLayerForUpdate(FeatureLayer pLayer) {
        return RenderLayer(pLayer, EEditMode.EDIT);
    }

    public void RenderLayerForUpdateAllLabel(FeatureLayer pLayer) {
        DataSource localDataSource = PubVar.m_Workspace.GetDataSourceByEditing();
        DataSet localDataset = localDataSource.GetDatasetByName(pLayer.GetLayerID());
        SQLiteReader localSQLiteDataReader = localDataSource.Query("select SYS_ID," + pLayer.GetLabelDataField() + " from " + pLayer.GetLayerID());
        if (localSQLiteDataReader != null) {
            while (localSQLiteDataReader.Read()) {
                String str1 = localSQLiteDataReader.GetString("SYS_ID");
                localDataset.GetGeometryFromDIndex(Integer.parseInt(str1)).setTag(localSQLiteDataReader.GetString(pLayer.GetLabelDataField()));
            }
            localSQLiteDataReader.Close();
        }
    }
}
