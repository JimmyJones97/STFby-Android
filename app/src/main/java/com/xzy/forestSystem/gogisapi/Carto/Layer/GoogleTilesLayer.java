package  com.xzy.forestSystem.gogisapi.Carto.Layer;

import android.os.Handler;
import android.os.Message;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.CoordCorrect;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WebMercator;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import java.util.ArrayList;
import java.util.List;

public class GoogleTilesLayer extends XBaseTilesLayer {
    private static Handler DataFromServerHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.Carto.Layer.GoogleTilesLayer.1
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Object[] tmpObjs = (Object[]) msg.obj;
                String tmpStr = String.valueOf(tmpObjs[0]);
                if (tmpStr.equals("googlesatellite")) {
                    GoogleTilesLayer.GoogleMapURL_Satellite = String.valueOf(tmpObjs[1]);
                } else if (tmpStr.equals("googleterrain")) {
                    GoogleTilesLayer.GoogleMapURL_Terrain = String.valueOf(tmpObjs[1]);
                } else if (tmpStr.equals("googlestreet")) {
                    GoogleTilesLayer.GoogleMapURL_Street = String.valueOf(tmpObjs[1]);
                } else if (tmpStr.equals("googleaddress")) {
                    GoogleTilesLayer.GoogleMapURL_Address = String.valueOf(tmpObjs[1]);
                }
            }
        }
    };
    private static String GoogleMapURL_Address = "";
    private static String GoogleMapURL_Satellite = "";
    private static String GoogleMapURL_Street = "";
    private static String GoogleMapURL_Terrain = "";

    public GoogleTilesLayer() {
        initLayer();
        this._CacheFolder = String.valueOf(PubVar.m_SystemPath) + "/Map/OverMap";
        this._TableName = "g_Sat";
    }

    public static String GetGoogleMapURL_Street() {
        if (GoogleMapURL_Street.equals("")) {
            Common.GetParamFromServer("googlestreet", DataFromServerHandler);
        }
        if (GoogleMapURL_Street == null || GoogleMapURL_Street.equals("")) {
            GoogleMapURL_Street = "http://www.google.cn/maps/vt?lyrs=m@184&gl=cn&";
        }
        return GoogleMapURL_Street;
    }

    public static String GetGoogleMapURL_Terrain() {
        if (GoogleMapURL_Terrain.equals("")) {
            Common.GetParamFromServer("googleterrain", DataFromServerHandler);
        }
        if (GoogleMapURL_Terrain == null || GoogleMapURL_Terrain.equals("")) {
            GoogleMapURL_Terrain = "http://www.google.cn/maps/vt?lyrs=t@184&gl=cn&";
        }
        return GoogleMapURL_Terrain;
    }

    public static String GetGoogleMapURL_Satellite() {
        if (GoogleMapURL_Satellite.equals("")) {
            Common.GetParamFromServer("googlesatellite", DataFromServerHandler);
        }
        if (GoogleMapURL_Satellite == null || GoogleMapURL_Satellite.equals("") || !GoogleMapURL_Satellite.contains("http://")) {
            GoogleMapURL_Satellite = "http://www.google.cn/maps/vt?lyrs=s@184&gl=cn&";
        }
        return GoogleMapURL_Satellite;
    }

    public static String GetGoogleMapURL_Address() {
        if (GoogleMapURL_Address.equals("")) {
            Common.GetParamFromServer("googleaddress", DataFromServerHandler);
        }
        if (GoogleMapURL_Address == null || GoogleMapURL_Address.equals("")) {
            GoogleMapURL_Address = "http://mt3.google.cn/vt/imgtp=png32&lyrs=h@365000000&hl=zh-CN&gl=cn&s=Galil&";
        }
        return GoogleMapURL_Address;
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
        if (tileLayerType == ERasterLayerType.Google_Street) {
            return String.valueOf(GetGoogleMapURL_Street()) + "x=" + col + "&y=" + row + "&z=" + level;
        }
        if (tileLayerType == ERasterLayerType.Google_Terrain) {
            return String.valueOf(GetGoogleMapURL_Terrain()) + "x=" + col + "&y=" + row + "&z=" + level;
        }
        if (tileLayerType == ERasterLayerType.Google_Address) {
            return String.valueOf(GetGoogleMapURL_Address()) + "x=" + col + "&y=" + row + "&z=" + level;
        }
        return String.valueOf(GetGoogleMapURL_Satellite()) + "x=" + col + "&y=" + row + "&z=" + level;
    }

    /* access modifiers changed from: package-private */
    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public void calMapCorrect(Coordinate coord) {
        double[] tmpCoords = new double[2];
        CoordCorrect.transform(coord.getGeoY(), coord.getGeoX(), tmpCoords);
        this._OffsetLongitude = tmpCoords[1] - coord.getGeoX();
        this._OffsetLatitude = tmpCoords[0] - coord.getGeoY();
        coord.setGeoX(tmpCoords[1]);
        coord.setGeoY(tmpCoords[0]);
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.gogisapi.Carto.Layer.XBaseTilesLayer
    public void initLayer() {
        this._LevelScale = GetTileScaleInfo();
        this._TotalLevel = this._LevelScale.size();
        this._CoordinateSystem = new Coordinate_WebMercator();
        super.initLayer();
    }

    private void buildTileInfo() {
        double[] dArr = {1.40625d, 0.703125d, 0.3515625d, 0.17578125d, 0.087890625d, 0.0439453125d, 0.02197265625d, 0.010986328125d, 0.0054931640625d, 0.00274658203125d, 0.001373291015625d, 6.866455078125E-4d, 3.4332275390625E-4d, 1.71661376953125E-4d, 8.58306884765629E-5d, 4.29153442382814E-5d, 2.14576721191407E-5d, 1.07288360595703E-5d, 5.36441802978515E-6d, 2.68220901489258E-6d, 1.34110450744629E-6d};
        double[] dArr2 = {5.91657527591555E8d, 2.95828763795777E8d, 1.47914381897889E8d, 7.3957190948944E7d, 3.6978595474472E7d, 1.8489297737236E7d, 9244648.868618d, 4622324.434309d, 2311162.217155d, 1155581.108577d, 577790.554289d, 288895.277144d, 144447.638572d, 72223.819286d, 36111.909643d, 18055.954822d, 9027.977411d, 4513.988705d, 2256.994353d, 1128.497176d};
        int length = new double[]{156543.03392800014d, 78271.51696399994d, 39135.75848200009d, 19567.87924099992d, 9783.93962049996d, 4891.96981024998d, 2445.98490512499d, 1222.992452562495d, 611.49622628138d, 305.748113140558d, 152.874056570411d, 76.4370282850732d, 38.2185141425366d, 19.1092570712683d, 9.55462853563415d, 4.77731426794937d, 2.388657133974685d, 1.1943285668550503d, 0.5971642835598172d, 0.29858214164761665d}.length;
    }
}
