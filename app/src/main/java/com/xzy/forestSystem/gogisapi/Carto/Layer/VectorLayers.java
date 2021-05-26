package  com.xzy.forestSystem.gogisapi.Carto.Layer;

import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSource;
import  com.xzy.forestSystem.gogisapi.Geodatabase.ProjectDatabase;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VectorLayers {
    private float m_CenterJX = 120.0f;
    private String m_CoorSystem = "";
    private float m_DaiHao = 40.0f;
    private float m_FenDuDai = 3.0f;
    private List<FeatureLayer> m_LayerList = new ArrayList();
    private String m_LayersName = "";
    private String m_Password = "";
    private String m_Path = "";
    private ProjectDatabase m_ProjectDB = null;
    private double m_biasX = 0.0d;
    private double m_biasY = 0.0d;

    public VectorLayers(String filepath, String password) {
        this.m_Path = filepath;
        this.m_Password = password;
        this.m_LayersName = this.m_Path.substring(this.m_Path.lastIndexOf(FileSelector_Dialog.sRoot) + 1);
    }

    public void Dispose() {
        if (this.m_LayerList.size() > 0) {
            for (FeatureLayer tmpFeatureLayer : this.m_LayerList) {
                if (tmpFeatureLayer != null) {
                    tmpFeatureLayer.Dispose();
                }
            }
            this.m_LayerList.clear();
        }
    }

    public String GetLayersPath() {
        return this.m_Path;
    }

    public String GetLayersName() {
        return this.m_LayersName;
    }

    public String GetPassword() {
        return this.m_Password;
    }

    public void ClearLayers() {
        this.m_LayerList.clear();
        DataSource pDataSource = PubVar.m_Workspace.GetDataSourceByUnEditing(this.m_Path);
        if (pDataSource != null) {
            pDataSource.getDatasets().clear();
        }
    }

    private void LoadLayersFormDataSource(DataSource dataSource) {
        int tempIndex;
        SQLiteReader tempSQLReader2;
        this.m_LayerList.clear();
        if (dataSource != null) {
            List<HashMap<String, Object>> tempHashList = new ArrayList<>();
            SQLiteReader tempSQLReader = PubVar._PubCommand.m_ProjectDB.GetSQLiteDatabase().Query("Select LayerID,F1,F2,Visible,Transparent,Fields From T_BKVectorLayer Where Path='" + this.m_Path + "' Order By SortID");
            if (tempSQLReader != null) {
                List<String> tempList = new ArrayList<>();
                boolean tempBool = true;
                while (tempSQLReader.Read()) {
                    tempList.add(tempSQLReader.GetString(0));
                    HashMap<String, Object> tempHash = new HashMap<>();
                    tempHash.put("D1", tempSQLReader.GetString(0));
                    tempHash.put("D2", tempSQLReader.GetString(3));
                    tempHash.put("D3", tempSQLReader.GetString(4));
                    String tmpObj = tempSQLReader.GetString(5);
                    if (tmpObj == null) {
                        tmpObj = "";
                    }
                    tempHash.put("D4", tmpObj.toString());
                    tempHashList.add(tempHash);
                    if (tempBool) {
                        String tempStr01 = tempSQLReader.GetString(1);
                        if (!tempStr01.equals("")) {
                            this.m_biasX = Double.parseDouble(tempStr01);
                            String tempStr012 = tempSQLReader.GetString(2);
                            if (!tempStr012.equals("")) {
                                this.m_biasY = Double.parseDouble(tempStr012);
                            }
                        }
                        tempBool = false;
                    }
                }
                tempSQLReader.Close();
                try {
                    SQLiteReader tempReader2 = dataSource.Query("Select CoorType,CenterJX,FDType,DH From MapInfo limit 1");
                    if (tempReader2 != null) {
                        if (tempReader2.Read()) {
                            this.m_CoorSystem = tempReader2.GetString(0);
                            this.m_CenterJX = Float.parseFloat(tempReader2.GetString(1));
                            this.m_FenDuDai = Float.parseFloat(tempReader2.GetString(2));
                            this.m_DaiHao = Float.parseFloat(tempReader2.GetString(3));
                        }
                        tempReader2.Close();
                    }
                } catch (Exception e) {
                }
                List<FeatureLayer> tempLayersList = new ArrayList<>();
                if (tempList.size() > 0 && (tempSQLReader2 = dataSource.Query("select * from T_Layer Where LayerID in ('" + Common.CombineStrings("','", tempList) + "')  order by SortID")) != null) {
                    while (tempSQLReader2.Read()) {
                        FeatureLayer tempLayer = new FeatureLayer();
                        tempLayer.SetEditable(false);
                        tempLayer.ReadLayerInfo(tempSQLReader2);
                        tempLayer.SetDataSourceName(dataSource.getName());
                        tempLayersList.add(tempLayer);
                    }
                    tempSQLReader2.Close();
                }
                if (tempHashList.size() > 0 && tempLayersList.size() > 0) {
                    for (HashMap<String, Object> tempHash2 : tempHashList) {
                        String tempLayerID = tempHash2.get("D1").toString();
                        if (!tempLayerID.equals("") && (tempIndex = findLayerIndexInList(tempLayersList, tempLayerID)) >= 0) {
                            FeatureLayer tmpLayer = tempLayersList.get(tempIndex);
                            tmpLayer.SetEditable(false);
                            tmpLayer.SetVisible(Boolean.parseBoolean(tempHash2.get("D2").toString()));
                            tmpLayer.SetTransparent(Integer.parseInt(tempHash2.get("D3").toString()));
                            String tempFieldVis = tempHash2.get("D4").toString();
                            if (tempFieldVis != null && !tempFieldVis.equals("")) {
                                tmpLayer.UpdateFieldsVisible(tempFieldVis);
                            }
                            tmpLayer.setTag(tmpLayer.GetLayerRenderData(dataSource.GetSQLiteDatabase(), tmpLayer.GetRenderType()));
                            this.m_LayerList.add(tmpLayer);
                        }
                    }
                }
            }
        }
    }

    private int findLayerIndexInList(List<FeatureLayer> list, String layerID) {
        int result = 0;
        for (FeatureLayer featureLayer : list) {
            if (featureLayer.GetLayerID().equals(layerID)) {
                return result;
            }
            result++;
        }
        return -1;
    }

    public List<FeatureLayer> GetLayerList() {
        return this.m_LayerList;
    }

    public void OpenDataSource() {
        if (PubVar.m_Workspace.OpenVectorsDataSource(this.m_Path, this.m_Password)) {
            LoadLayersFormDataSource(PubVar.m_Workspace.GetDataSourceByUnEditing(this.m_Path));
        }
    }

    public boolean SaveLayers() {
        if (!Common.CheckExistFile(this.m_Path) || this.m_LayerList.size() <= 0) {
            return false;
        }
        for (FeatureLayer tmpLayer : this.m_LayerList) {
            StringBuilder tempSB = new StringBuilder();
            tempSB.append("Replace Into T_BKVectorLayer (LayerID,Name,SortID,Path,Visible,Transparent,MinX,MinY,MaxX,MaxY,Security,Fields) Values ('");
            tempSB.append(String.valueOf(tmpLayer.GetLayerID()) + "','");
            tempSB.append(String.valueOf(tmpLayer.GetLayerName()) + "','");
            tempSB.append(String.valueOf(tmpLayer.GetLayerIndex()) + "','");
            tempSB.append(String.valueOf(this.m_Path) + "','");
            tempSB.append(String.valueOf(tmpLayer.GetVisible()) + "','");
            tempSB.append(String.valueOf(tmpLayer.GetTransparet()) + "','");
            tempSB.append(String.valueOf(tmpLayer.getMinX()) + "','");
            tempSB.append(String.valueOf(tmpLayer.getMinY()) + "','");
            tempSB.append(String.valueOf(tmpLayer.getMaxX()) + "','");
            tempSB.append(String.valueOf(tmpLayer.getMaxY()) + "','");
            tempSB.append(String.valueOf(this.m_Password) + "','");
            StringBuilder tempSB2 = new StringBuilder();
            for (LayerField layerField : tmpLayer.GetFieldList()) {
                if (tempSB2.length() > 0) {
                    tempSB2.append(",");
                }
                tempSB2.append(layerField.GetDataFieldName());
            }
            tempSB.append(String.valueOf(tempSB2.toString()) + "')");
            this.m_ProjectDB.GetSQLiteDatabase().ExecuteSQL(tempSB.toString());
        }
        return false;
    }

    public void SetLayersVisible(boolean visible) {
        int count = this.m_LayerList.size();
        for (int i = 0; i < count; i++) {
            PubVar._Map.getVectorGeoLayers().GetLayerByName(this.m_LayerList.get(i).GetLayerID()).setVisible(visible);
        }
    }

    public void SetBindProject(ProjectDatabase tmpProject) {
        this.m_ProjectDB = tmpProject;
    }

    public double getBiasX() {
        return this.m_biasX;
    }

    public double getBiasY() {
        return this.m_biasY;
    }

    public void setBiasX(double value) {
        this.m_biasX = value;
    }

    public void setBiasY(double value) {
        this.m_biasY = value;
    }

    public String getCoorSystem() {
        return this.m_CoorSystem;
    }

    public void SetCoorSystem(String _CoorSystem) {
        this.m_CoorSystem = _CoorSystem;
    }
}
