package  com.xzy.forestSystem.gogisapi.Carto.Layer;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WebMercator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TianDiTuTilesLayer extends XBaseTilesLayer {
    public TianDiTuTilesLayer() {
        initLayer();
        this._CacheFolder = String.valueOf(PubVar.m_SystemPath) + "/Map/TianDiTuMap";
        this._TableName = "TDT_IMG";
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public void initLayer() {
        this._LevelScale = GetTileScaleInfo();
        this._TotalLevel = this._LevelScale.size();
        this._CoordinateSystem = new Coordinate_WebMercator();
        this._CoordinateSystem.SetA(6378137.0d);
        this._CoordinateSystem.SetB(6356752.31414d);
        this._CoordinateSystem.Setf(298.257222101d);
        super.initLayer();
    }

    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public String CreateTileURL(ERasterLayerType tileLayerType, int level, int col, int row) {
        StringBuilder url = new StringBuilder("http://t");
        url.append(new Random().nextInt(6) + 1);
        if (tileLayerType == ERasterLayerType.TianDiTu_Vector) {
            url.append(".tianditu.com/DataServer?T=vec_w&X=").append(col).append("&Y=").append(row).append("&L=").append(level);
        } else if (tileLayerType == ERasterLayerType.TianDiTu_VectorLabel) {
            url.append(".tianditu.com/DataServer?T=cva_w&X=").append(col).append("&Y=").append(row).append("&L=").append(level);
        } else if (tileLayerType == ERasterLayerType.TianDiTu_Satellite) {
            url.append(".tianditu.com/DataServer?T=img_w&X=").append(col).append("&Y=").append(row).append("&L=").append(level);
        } else if (tileLayerType == ERasterLayerType.TianDiTu_SatelliteLabel) {
            url.append(".tianditu.com/DataServer?T=img_w&X=").append(col).append("&Y=").append(row).append("&L=").append(level);
        }
        return url.toString();
    }

    /* access modifiers changed from: package-private */
    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public List<Double> GetTileScaleInfo() {
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < 22; i++) {
            result.add(Double.valueOf((4.007501668557849E7d / Math.pow(2.0d, (double) i)) / 256.0d));
        }
        return result;
    }

    private void buildTileInfo() {
        double[] dArr = {1.40625d, 0.703125d, 0.3515625d, 0.17578125d, 0.087890625d, 0.0439453125d, 0.02197265625d, 0.010986328125d, 0.0054931640625d, 0.00274658203125d, 0.001373291015625d, 6.866455078125E-4d, 3.4332275390625E-4d, 1.71661376953125E-4d, 8.58306884765629E-5d, 4.29153442382814E-5d, 2.14576721191407E-5d, 1.07288360595703E-5d, 5.36441802978515E-6d, 2.68220901489258E-6d, 1.34110450744629E-6d};
        double[] dArr2 = {4.0E8d, 2.954975985708346E8d, 1.47748799285417E8d, 7.38743996427087E7d, 3.69371998213544E7d, 1.84685999106772E7d, 9234299.95533859d, 4617149.97766929d, 2308574.98883465d, 1154287.49441732d, 577143.747208662d, 288571.873604331d, 144285.936802165d, 72142.9684010827d, 36071.4842005414d, 18035.7421002707d, 9017.87105013534d, 4508.93552506767d, 2254.467762533835d, 1127.2338812669175d, 563.61694d};
    }
}
