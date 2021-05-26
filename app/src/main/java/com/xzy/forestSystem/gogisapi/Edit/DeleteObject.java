package  com.xzy.forestSystem.gogisapi.Edit;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.GeoLayer;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Geodatabase.DataSet;
import  com.xzy.forestSystem.gogisapi.Geodatabase.Selection;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import java.util.ArrayList;
import java.util.List;

public class DeleteObject {
    public boolean DeleteAllSelected() {
        boolean result = false;
        try {
            StringBuilder tempSB = new StringBuilder();
            int tid = -1;
            for (GeoLayer tempGeoLayer : PubVar._Map.getGeoLayers().getList()) {
                tid++;
                Selection tmpSelSelction = PubVar._Map.getFeatureSelectionByIndex(tid, false);
                if (tmpSelSelction != null && tmpSelSelction.getCount() > 0) {
                    DataSet pDataSet = tempGeoLayer.getDataset();
                    if (pDataSet.getDataSource().getEditing()) {
                        List<String> tempSeletedList = new ArrayList<>();
                        for (Integer num : tmpSelSelction.getGeometryIndexList()) {
                            AbstractGeometry tmpGeometry = pDataSet.GetGeometry(num.intValue());
                            if (tmpGeometry != null) {
                                tempSeletedList.add(tmpGeometry.GetSYS_ID());
                                tmpGeometry.setStatus(EGeometryStatus.DELETE);
                            }
                        }
                        if (tempSeletedList.size() > 0) {
                            pDataSet.Remove(tempSeletedList);
                            pDataSet.refreshAllGeometryIndex();
                            pDataSet.RefreshTotalCount();
                            result = true;
                        }
                    } else {
                        tempSB.append("图层【" + tempGeoLayer.getLayerName() + "】不能编辑.\r\n");
                    }
                }
            }
            if (tempSB.length() > 0) {
                Common.ShowDialog(tempSB.toString());
            }
        } catch (Exception e) {
        }
        return result;
    }

    public boolean DeleteX(GeoLayer localGeoLayer, List<String> sysIDList, boolean DeleteObjectTrue) {
        String tempSql;
        AbstractGeometry tempGeo;
        if (localGeoLayer == null || sysIDList.size() <= 0) {
            return false;
        }
        for (String tempSYSID : sysIDList) {
            if (!(tempSYSID == null || (tempGeo = localGeoLayer.getDataset().GetGeometryBySYSID(tempSYSID)) == null)) {
                tempGeo.setStatus(EGeometryStatus.DELETE);
            }
        }
        if (DeleteObjectTrue) {
            tempSql = "Delete FROM " + localGeoLayer.getDataset().getDataTableName() + " where SYS_ID in (" + Common.CombineStrings(",", sysIDList) + ")";
        } else {
            tempSql = "update " + localGeoLayer.getDataset().getDataTableName() + " set SYS_STATUS=1 where SYS_ID in (" + Common.CombineStrings(",", sysIDList) + ")";
            localGeoLayer.getDataset().setEdited(true);
        }
        boolean result = localGeoLayer.getDataset().getDataSource().ExcuteSQL(tempSql);
        localGeoLayer.getDataset().refreshAllGeometryIndex();
        localGeoLayer.getDataset().RefreshTotalCount();
        return result;
    }

    public boolean Delete(String layerID, List<String> sysOIDList, boolean DeleteObjectTrue) {
        if (sysOIDList == null || sysOIDList.size() <= 0) {
            Common.ShowDialog("请在可编辑图层中选择需要删除的实体！");
            return false;
        }
        GeoLayer localGeoLayer = PubVar._Map.getGeoLayers().GetLayerByName(layerID);
        if (localGeoLayer == null || !localGeoLayer.getDataset().getDataSource().getEditing()) {
            return false;
        }
        return DeleteX(localGeoLayer, sysOIDList, DeleteObjectTrue);
    }

    public static boolean DeleteGeoemtryInDataset(DataSet dataset, AbstractGeometry geoemtry) {
        geoemtry.SetEdited(true);
        geoemtry.setStatus(EGeometryStatus.DELETE);
        String tmpSql = "update " + dataset.getDataTableName() + " set SYS_STATUS=1 where SYS_ID =" + geoemtry.GetSYS_ID();
        dataset.setEdited(true);
        return dataset.getDataSource().ExcuteSQL(tmpSql);
    }

    public static boolean UnDeleteGeoemtryInDataset(DataSet dataset, AbstractGeometry geoemtry) {
        geoemtry.SetEdited(true);
        geoemtry.setStatus(EGeometryStatus.NONE);
        String tmpSql = "update " + dataset.getDataTableName() + " set SYS_STATUS=0 where SYS_ID =" + geoemtry.GetSYS_ID();
        dataset.setEdited(true);
        return dataset.getDataSource().ExcuteSQL(tmpSql);
    }
}
