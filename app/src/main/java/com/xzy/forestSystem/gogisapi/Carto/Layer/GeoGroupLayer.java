package  com.xzy.forestSystem.gogisapi.Carto.Layer;

import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import java.util.ArrayList;
import java.util.List;

public class GeoGroupLayer {
    private List<GeoLayer> List = new ArrayList();

    public void Dispose2() {
        if (this.List.size() > 0) {
            for (GeoLayer tmpGeoLayer : this.List) {
                if (tmpGeoLayer != null) {
                    tmpGeoLayer.Dispose2();
                }
            }
        }
        this.List.clear();
    }

    public void AddLayer(GeoLayer paramGeoLayer) {
        this.List.add(paramGeoLayer);
    }

    public GeoLayer GetLayerByIndex(int paramInt) {
        return this.List.get(paramInt);
    }

    public GeoLayer GetLayerByName(String layerID) {
        for (GeoLayer localGeoLayer : this.List) {
            if (localGeoLayer != null && localGeoLayer.getName().indexOf(layerID) >= 0) {
                return localGeoLayer;
            }
        }
        return null;
    }

    public GeoLayer GetLayerByLayerName(String layerName) {
        for (GeoLayer localGeoLayer : this.List) {
            if (localGeoLayer != null && localGeoLayer.getLayerName().equals(layerName)) {
                return localGeoLayer;
            }
        }
        return null;
    }

    public GeoLayer GetLayerByTag(String tagValue, BasicValue param) {
        int tid = 0;
        for (GeoLayer localGeoLayer : this.List) {
            if (localGeoLayer == null || localGeoLayer.getTag() == null || !localGeoLayer.getTag().toString().equals(tagValue)) {
                tid++;
            } else {
                param.setValue(tid);
                return localGeoLayer;
            }
        }
        return null;
    }

    public int IndexOf(GeoLayer paramGeoLayer) {
        return this.List.indexOf(paramGeoLayer);
    }

    public boolean MoveTo(String layerName, int layerIndex) {
        GeoLayer localGeoLayer = GetLayerByName(layerName);
        if (localGeoLayer == null) {
            return false;
        }
        this.List.remove(localGeoLayer);
        this.List.add(layerIndex, localGeoLayer);
        return true;
    }

    public void AddGeoLayer(GeoLayer geoLayer, int layerIndex) {
        this.List.add(layerIndex, geoLayer);
    }

    public void Remove(String paramString) {
        GeoLayer tempGeoLayer = GetLayerByName(paramString);
        if (tempGeoLayer != null) {
            this.List.remove(tempGeoLayer);
        }
    }

    public void Remove(GeoLayer paramGeoLayer) {
        this.List.remove(paramGeoLayer);
    }

    public void RemoveAt(int paramInt) {
        this.List.remove(paramInt);
    }

    public void RemoveAll() {
        this.List.clear();
    }

    public List<GeoLayer> getList() {
        return this.List;
    }

    public int size() {
        return this.List.size();
    }
}
