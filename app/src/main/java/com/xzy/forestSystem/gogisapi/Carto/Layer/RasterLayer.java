package  com.xzy.forestSystem.gogisapi.Carto.Layer;

import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import  com.xzy.forestSystem.gogisapi.System.AuthorizeTools;
import java.util.ArrayList;
import java.util.List;

public class RasterLayer {
    public String MapConfigString;
    public int StartLevel;
    private boolean _ConsiderTranslate;
    private String _CoorSytem;
    private EEditMode _EditMode;
    private List<Double> _FParas;
    private String _FromFilePath;
    private String _LayerID;
    private int _LayerIndex;
    private String _LayerName;
    private ERasterLayerType _LayerType;
    private double _MaxX;
    private double _MaxY;
    private double _MinX;
    private double _MinY;
    public double _OffsetX;
    public double _OffsetY;
    private String _Security;
    private int _Transparent;
    private double _VisiableScaleMax;
    private double _VisiableScaleMin;
    private boolean _Visible;

    public RasterLayer() {
        this._LayerID = "";
        this._LayerName = "";
        this._LayerType = ERasterLayerType.NONE;
        this._FromFilePath = "";
        this._Transparent = 255;
        this._Visible = true;
        this._VisiableScaleMin = 0.0d;
        this._VisiableScaleMax = 2.147483647E9d;
        this._LayerIndex = 0;
        this._EditMode = EEditMode.NONE;
        this._MinX = 0.0d;
        this._MaxX = 2.147483647E9d;
        this._MinY = 0.0d;
        this._MaxY = 2.147483647E9d;
        this._CoorSytem = "WebMercator";
        this._FParas = new ArrayList();
        this._Security = "";
        this._OffsetX = 0.0d;
        this._OffsetY = 0.0d;
        this._ConsiderTranslate = false;
        this.MapConfigString = "";
        this.StartLevel = 0;
        this._FParas = new ArrayList();
        for (int i = 1; i < 11; i++) {
            this._FParas.add(Double.valueOf(0.0d));
        }
    }

    public void Dispose() {
        this._FParas.clear();
    }

    public RasterLayer Clone() {
        RasterLayer tempLayer = new RasterLayer();
        CopyTo(tempLayer);
        return tempLayer;
    }

    public void CopyTo(RasterLayer tempLayer) {
        tempLayer._LayerID = this._LayerID;
        tempLayer._LayerType = this._LayerType;
        tempLayer._CoorSytem = this._CoorSytem;
        tempLayer._FromFilePath = this._FromFilePath;
        tempLayer._LayerIndex = this._LayerIndex;
        tempLayer._LayerName = this._LayerName;
        tempLayer._Visible = this._Visible;
        tempLayer._Transparent = this._Transparent;
        tempLayer._VisiableScaleMax = this._VisiableScaleMax;
        tempLayer._VisiableScaleMin = this._VisiableScaleMin;
        tempLayer._FParas = this._FParas;
        tempLayer._OffsetX = this._OffsetX;
        tempLayer._OffsetY = this._OffsetY;
        tempLayer._MinX = this._MinX;
        tempLayer._MaxX = this._MaxX;
        tempLayer._MinY = this._MinY;
        tempLayer._MaxY = this._MaxY;
        tempLayer._ConsiderTranslate = this._ConsiderTranslate;
        tempLayer.MapConfigString = this.MapConfigString;
        tempLayer.StartLevel = this.StartLevel;
    }

    public void CopyTo(XLayer tempLayer) {
        tempLayer.setLayerIndex(this._LayerIndex);
        tempLayer.setName(this._LayerName);
        tempLayer.setVisible(this._Visible);
        tempLayer.setTransparent(this._Transparent);
        tempLayer.setMaxScale(this._VisiableScaleMax);
        tempLayer.setMinScale(this._VisiableScaleMin);
        tempLayer.setMinX(this._MinX);
        tempLayer.setMaxX(this._MaxX);
        tempLayer.setMinY(this._MinY);
        tempLayer.setMaxY(this._MaxY);
    }

    public String GetLayerID() {
        return this._LayerID;
    }

    public void setLayerID(String layerID) {
        this._LayerID = layerID;
    }

    public String GetLayerName() {
        return this._LayerName;
    }

    public ERasterLayerType GetLayerType() {
        return this._LayerType;
    }

    public String getPassowrd() {
        return this._Security;
    }

    public void setPassowrd(String password) {
        this._Security = password;
    }

    public String GetLayerTypeName() {
        if (this._LayerType == ERasterLayerType.NONE) {
            return "无";
        }
        if (this._LayerType == ERasterLayerType.FromFile) {
            return "文件";
        }
        if (this._LayerType == ERasterLayerType.Google_Satellite) {
            return "Google卫星地图";
        }
        if (this._LayerType == ERasterLayerType.Google_Street) {
            return "Google街道地图";
        }
        if (this._LayerType == ERasterLayerType.Google_Address) {
            return "地名注记";
        }
        if (this._LayerType == ERasterLayerType.Google_Terrain) {
            return "Google地形地图";
        }
        if (this._LayerType == ERasterLayerType.TianDiTu_Vector) {
            return "天地图矢量地图";
        }
        if (this._LayerType == ERasterLayerType.TianDiTu_Satellite) {
            return "天地图卫星地图";
        }
        if (this._LayerType == ERasterLayerType.TianDiTu_VectorLabel) {
            return "天地图矢量标注地图";
        }
        if (this._LayerType == ERasterLayerType.TianDiTu_SatelliteLabel) {
            return "天地图卫星标注地图";
        }
        if (this._LayerType == ERasterLayerType.ContourMap) {
            return "等高线地图";
        }
        if (this._LayerType == ERasterLayerType.NetTileLayer) {
            return "网络地图";
        }
        return "";
    }

    public int GetTransparet() {
        return this._Transparent;
    }

    public boolean GetVisible() {
        return this._Visible;
    }

    public void SetLayerName(String paramString) {
        this._LayerName = paramString;
        if (this._LayerType == ERasterLayerType.FromFile) {
            this._LayerID = String.valueOf(this._LayerName) + ";" + GetLayerTypeName() + ";" + this._FromFilePath;
        } else if (this._LayerType == ERasterLayerType.NetTileLayer) {
            this._LayerID = this._LayerName;
        } else {
            this._LayerID = GetLayerTypeName();
        }
    }

    public void SetLayerTypeName(String paramString) {
        if (paramString.equals("文件")) {
            this._LayerType = ERasterLayerType.FromFile;
        } else if (paramString.equals("Google卫星地图")) {
            this._LayerType = ERasterLayerType.Google_Satellite;
        } else if (paramString.equals("Google街道地图")) {
            this._LayerType = ERasterLayerType.Google_Street;
        } else if (paramString.equals("地名注记")) {
            this._LayerType = ERasterLayerType.Google_Address;
        } else if (paramString.equals("Google地形地图")) {
            this._LayerType = ERasterLayerType.Google_Terrain;
        } else if (paramString.equals("天地图矢量地图")) {
            this._LayerType = ERasterLayerType.TianDiTu_Vector;
        } else if (paramString.equals("天地图卫星地图")) {
            this._LayerType = ERasterLayerType.TianDiTu_Satellite;
        } else if (paramString.equals("天地图矢量标注地图")) {
            this._LayerType = ERasterLayerType.TianDiTu_VectorLabel;
        } else if (paramString.equals("天地图卫星标注地图")) {
            this._LayerType = ERasterLayerType.TianDiTu_SatelliteLabel;
        } else if (paramString.equals("等高线地图")) {
            this._LayerType = ERasterLayerType.ContourMap;
        } else if (paramString.equals("网络地图")) {
            this._LayerType = ERasterLayerType.NetTileLayer;
        }
        SetLayerName(this._LayerName);
    }

    public void SetTransparent(int paramInt) {
        this._Transparent = paramInt;
    }

    public void SetVisible(boolean paramBoolean) {
        this._Visible = paramBoolean;
    }

    public double GetMinScale() {
        return this._VisiableScaleMin;
    }

    public double GetMaxScale() {
        return this._VisiableScaleMax;
    }

    public void SetMinScale(double value) {
        this._VisiableScaleMin = value;
    }

    public void SetMaxScale(double value) {
        this._VisiableScaleMax = value;
    }

    public void SetLayerIndex(int layerIndex) {
        this._LayerIndex = layerIndex;
    }

    public int GetLayerIndex() {
        return this._LayerIndex;
    }

    public EEditMode GetEditMode() {
        return this._EditMode;
    }

    public void SetEditMode(EEditMode lkEditMode) {
        this._EditMode = lkEditMode;
    }

    public void SetFilePath(String filePath) {
        this._FromFilePath = filePath;
        this._LayerID = String.valueOf(this._LayerName) + ";" + GetLayerTypeName() + ";" + this._FromFilePath;
    }

    public String GetFilePath() {
        return this._FromFilePath;
    }

    public void SetCoorSystem(String _coorSytem) {
        this._CoorSytem = _coorSytem;
    }

    public String GetCoorSystem() {
        return this._CoorSytem;
    }

    public List<Double> GetFParas() {
        return this._FParas;
    }

    public void SetFParas(List<Double> value) {
        this._FParas = value;
    }

    public boolean ReadLayerInfo(SQLiteReader sqlReader) {
        try {
            String tempStr = sqlReader.GetString("SortID");
            if (tempStr != null && !tempStr.equals("")) {
                this._LayerIndex = Integer.parseInt(tempStr);
            }
            String tempStr2 = sqlReader.GetString("Name");
            if (tempStr2 != null && !tempStr2.equals("")) {
                this._LayerName = sqlReader.GetString("Name");
            }
            SetLayerTypeName(sqlReader.GetString("Type"));
            String tempStr3 = sqlReader.GetString("Path");
            if (tempStr3 != null && !tempStr3.equals("")) {
                this._FromFilePath = tempStr3;
            }
            String tempStr4 = sqlReader.GetString("MinX");
            if (tempStr4 != null && !tempStr4.equals("")) {
                this._MinX = Double.parseDouble(tempStr4);
            }
            String tempStr5 = sqlReader.GetString("MinY");
            if (tempStr5 != null && !tempStr5.equals("")) {
                this._MinY = Double.parseDouble(tempStr5);
            }
            String tempStr6 = sqlReader.GetString("MaxX");
            if (tempStr6 != null && !tempStr6.equals("")) {
                this._MaxX = Double.parseDouble(tempStr6);
            }
            String tempStr7 = sqlReader.GetString("MaxY");
            if (tempStr7 != null && !tempStr7.equals("")) {
                this._MaxY = Double.parseDouble(tempStr7);
            }
            String tempStr8 = sqlReader.GetString("CoorSystem");
            if (tempStr8 != null && !tempStr8.equals("")) {
                this._CoorSytem = tempStr8;
            }
            String tempStr9 = sqlReader.GetString("Transparent");
            if (tempStr9 != null && !tempStr9.equals("")) {
                this._Transparent = Integer.parseInt(tempStr9);
            }
            String tempStr10 = sqlReader.GetString("Visible");
            if (tempStr10 != null && !tempStr10.equals("")) {
                this._Visible = Boolean.parseBoolean(tempStr10);
            }
            List<Double> tempFParas = new ArrayList<>();
            for (int i = 1; i < 11; i++) {
                String tempStr11 = sqlReader.GetString("F" + i);
                if (tempStr11 == null || tempStr11.equals("")) {
                    tempFParas.add(Double.valueOf(0.0d));
                } else {
                    tempFParas.add(Double.valueOf(Double.parseDouble(tempStr11)));
                }
            }
            SetFParas(tempFParas);
            String tmpStr02 = sqlReader.GetString("ConsiderTranslate");
            if (tmpStr02 == "" || !tmpStr02.toLowerCase().trim().equals("true")) {
                this._ConsiderTranslate = false;
            } else {
                this._ConsiderTranslate = true;
            }
            String tempStr12 = sqlReader.GetString("MapConfig");
            if (tempStr12 != null && !tempStr12.equals("")) {
                this.MapConfigString = tempStr12;
            }
            String tempStr13 = sqlReader.GetString("StartLevel");
            if (tempStr13 != null && !tempStr13.equals("")) {
                this.StartLevel = Integer.parseInt(tempStr13);
            }
            String tempStr14 = sqlReader.GetString("BiasX");
            if (tempStr14 != null && !tempStr14.equals("")) {
                this._OffsetX = Double.parseDouble(tempStr14);
            }
            String tempStr15 = sqlReader.GetString("BiasY");
            if (tempStr15 != null && !tempStr15.equals("")) {
                this._OffsetY = Double.parseDouble(tempStr15);
            }
            this._Security = sqlReader.GetString("Security");
            if (this._Security != null && !this._Security.equals("")) {
                this._Security = AuthorizeTools.DecryptExpandLayerKey(this._Security);
            }
            SetLayerName(this._LayerName);
            return true;
        } catch (Exception ex) {
            Common.Log("ReadLayerInfo", "错误:" + ex.toString() + "-->" + ex.getMessage());
            return false;
        }
    }

    public boolean SaveLayerInfo() {
        String tempSql;
        try {
            String tempLayerTypeName = GetLayerTypeName();
            Object[] tempValues = new Object[27];
            tempValues[0] = this._LayerName;
            tempValues[1] = tempLayerTypeName;
            tempValues[2] = this._FromFilePath;
            tempValues[3] = Double.valueOf(this._MinX);
            tempValues[4] = Double.valueOf(this._MinY);
            tempValues[5] = Double.valueOf(this._MaxX);
            tempValues[6] = Double.valueOf(this._MaxY);
            tempValues[7] = this._CoorSytem;
            tempValues[8] = Integer.valueOf(this._Transparent);
            tempValues[9] = Integer.valueOf(this._LayerIndex);
            tempValues[10] = Boolean.valueOf(this._Visible);
            for (int i = 0; i < 10; i++) {
                tempValues[i + 11] = this._FParas.get(i);
            }
            String tmpEncryptCode = "";
            if (!this._Security.equals("")) {
                tmpEncryptCode = AuthorizeTools.EncryptExpandLayerKey(this._Security);
            }
            tempValues[21] = tmpEncryptCode;
            tempValues[22] = Boolean.valueOf(this._ConsiderTranslate);
            tempValues[23] = this.MapConfigString;
            tempValues[24] = Integer.valueOf(this.StartLevel);
            tempValues[25] = Double.valueOf(this._OffsetX);
            tempValues[26] = Double.valueOf(this._OffsetY);
            boolean isExist = false;
            SQLiteReader sqlReader = PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().Query("Select Id From T_RasterLayer Where Type='" + tempLayerTypeName + "' And Path='" + this._FromFilePath + "'  And Name='" + this._LayerName + "'");
            if (sqlReader != null) {
                if (sqlReader.Read()) {
                    isExist = true;
                }
                sqlReader.Close();
            }
            if (isExist) {
                tempSql = String.format("Update T_RasterLayer Set Type='%2$s',Path='%3$s',MinX=%4$s,MinY=%5$s,MaxX=%6$s,MaxY=%7$s,CoorSystem='%8$s',Transparent='%9$s',SortID='%10$s',Visible='%11$s',F1='%12$s',F2='%13$s',F3='%14$s',F4='%15$s',F5='%16$s',F6='%17$s',F7='%18$s',F8='%19$s',F9='%20$s',F10='%21$s',Security='%22$s',ConsiderTranslate='%23$s',MapConfig='%24$s',StartLevel=%25$s,BiasX=%26$s,BiasY=%27$s Where Type='%2$s' And Path='%3$s' And Name='%1$s'", tempValues);
            } else {
                tempSql = String.format("insert into T_RasterLayer (Name,Type,Path,MinX,MinY,MaxX,MaxY,CoorSystem,Transparent,SortID,Visible,F1,F2,F3,F4,F5,F6,F7,F8,F9,F10,Security,ConsiderTranslate,MapConfig,StartLevel,BiasX,BiasY) values ('%1$s','%2$s','%3$s',%4$s,%5$s,%6$s,%7$s,'%8$s','%9$s','%10$s','%11$s','%12$s','%13$s','%14$s','%15$s','%16$s','%17$s','%18$s','%19$s','%20$s','%21$s','%22$s','%23$s','%24$s',%25$s,%26$s,%27$s)", tempValues);
            }
            return PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL(tempSql);
        } catch (Exception ex) {
            Common.Log("RasterLayer-SaveLayerInfo", "错误:" + ex.toString() + "-->" + ex.getMessage());
            return false;
        }
    }

    public void setOffsetX(double value) {
        this._OffsetX = value;
        if (this._FParas.size() > 0) {
            this._FParas.set(0, Double.valueOf(value));
        }
    }

    public void setOffsetY(double value) {
        this._OffsetY = value;
        if (this._FParas.size() > 1) {
            this._FParas.set(1, Double.valueOf(value));
        }
    }

    public double getOffsetX() {
        return this._OffsetX;
    }

    public double getOffsetY() {
        return this._OffsetY;
    }

    public boolean getIsConsiderTranslate() {
        return this._ConsiderTranslate;
    }

    public void setIsConsiderTranslate(boolean considerTranslate) {
        this._ConsiderTranslate = considerTranslate;
    }
}
