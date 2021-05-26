package  com.xzy.forestSystem.gogisapi.Carto.Layer;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WebMercator;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.util.ArrayList;
import java.util.List;

public class ContourTilesLayer extends XBaseTilesLayer {
    private String m_ServerRandom = "a";

    public ContourTilesLayer() {
        initLayer();
        this._CacheFolder = String.valueOf(PubVar.m_SystemPath) + "/Map/ContourMap";
        this._TableName = "contour";
    }

    /* access modifiers changed from: package-private */
    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public List<Double> GetTileScaleInfo() {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            result.add(Double.valueOf((4.007501668557849E7d / Math.pow(2.0d, (double) i)) / 256.0d));
        }
        return result;
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public String CreateTileURL(ERasterLayerType tileLayerType, int level, int col, int row) {
        if (this.m_ServerRandom.equals("a")) {
            this.m_ServerRandom = "b";
        } else if (this.m_ServerRandom.equals("b")) {
            this.m_ServerRandom = "c";
        } else {
            this.m_ServerRandom = "a";
        }
        return "http://" + this.m_ServerRandom + ".tile.thunderforest.com/cycle/" + level + FileSelector_Dialog.sRoot + col + FileSelector_Dialog.sRoot + row + ".png";
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public void initLayer() {
        this._LevelScale = GetTileScaleInfo();
        this._TotalLevel = this._LevelScale.size();
        this._CoordinateSystem = new Coordinate_WebMercator();
        super.initLayer();
    }
}
