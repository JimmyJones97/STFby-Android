package  com.xzy.forestSystem.gogisapi.IOData;

import android.os.Handler;
import android.os.Message;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import  com.xzy.forestSystem.gogisapi.Carto.MapCellIndex;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import  com.xzy.forestSystem.gogisapi.Geodatabase.BaseDataObject;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.DxfReader;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.Line;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.LwPolyLine;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.MText;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.Point;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.PolyLine;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.Text;
import  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ImportDXFClass {
    DxfReader DXFFileReader = null;
    int tempTotalGeosCount = 0;
    int tmpGeoIndex = 0;
    int tmpGeoIndexDone = 0;
    long tmpStartTime = 0;

    public ImportDXFClass(DxfReader dxfFileReader) {
        this.DXFFileReader = dxfFileReader;
    }

    private void InsertGeoemtry(String tempLayerID, String layerTypeName, AbstractGeometry tempGeometry, String[] tempFValues, MapCellIndex tempMapCell, SQLiteDBHelper tempSqlDatabase, Handler myHandler) {
        try {
            BaseDataObject tempBaseData = new BaseDataObject();
            tempBaseData.SetBaseObjectRelateLayerID(tempLayerID);
            tempBaseData.SetSYS_TYPE(layerTypeName);
            List<String> tempFieldsList = new ArrayList<>();
            tempFieldsList.add("SYS_STATUS='0'");
            tempFieldsList.add("SYS_OID='" + UUID.randomUUID().toString() + "'");
            tempFieldsList.add("SYS_TYPE='DXF'");
            int tempCount001 = tempFValues.length;
            for (int tempI = 0; tempI < tempCount001; tempI++) {
                tempFieldsList.add("F" + (tempI + 1) + "='" + tempFValues[tempI] + "'");
            }
            String tmpSaveSql = tempBaseData.GetSaveFieldsAndGeoToDbSQL(tempGeometry, tempFieldsList);
            if (!(tmpSaveSql == null || tempGeometry == null)) {
                byte[] tempGeoBytes = Common.ConvertGeoToBytes(tempGeometry);
                if (tempGeoBytes != null && tempSqlDatabase.ExecuteSQL(tmpSaveSql, new Object[]{tempGeoBytes})) {
                    this.tmpGeoIndexDone++;
                    Envelope tempGeoExtend = tempGeometry.getEnvelope();
                    int[] tempRCIndex = tempMapCell.CalCellIndexsOne(tempGeoExtend);
                    if (tempRCIndex != null) {
                        tempSqlDatabase.ExecuteSQL("Insert Into " + tempLayerID + "_I (SYS_ID,RIndex,CIndex,MinX,MinY,MaxX,MaxY) values (" + this.tmpGeoIndexDone + "," + tempRCIndex[0] + "," + tempRCIndex[1] + "," + tempGeoExtend.getMinX() + "," + tempGeoExtend.getMinY() + "," + tempGeoExtend.getMaxX() + "," + tempGeoExtend.getMaxY() + ")");
                    }
                }
                int tmpPrgValue = (this.tmpGeoIndex * 100) / this.tempTotalGeosCount;
                String tmpNeedTimeStr = Common.SimplifyTime((((long) (this.tempTotalGeosCount - this.tmpGeoIndex)) * (System.currentTimeMillis() - this.tmpStartTime)) / ((long) this.tmpGeoIndex));
                Message msg = myHandler.obtainMessage();
                msg.what = 5;
                msg.obj = new Object[]{"导入数据  [" + this.tmpGeoIndex + FileSelector_Dialog.sRoot + this.tempTotalGeosCount + "],剩余时间约" + tmpNeedTimeStr, Integer.valueOf(tmpPrgValue)};
                myHandler.sendMessage(msg);
            }
        } catch (Exception e) {
        }
    }

    public boolean Import(FeatureLayer layer, String layerName, int layerType, int totalCount, CustomeProgressDialog progressDialog) {
        boolean result = false;
        String layerTypeName = "";
        if (layerType == 1) {
            layerTypeName = "点";
        } else if (layerType == 2) {
            layerTypeName = "线";
        } else if (layerType == 3) {
            layerTypeName = "面";
        } else if (layerType == 4) {
            layerTypeName = "点";
        }
        try {
            List<LayerField> fieldsList = new ArrayList<>();
            LayerField tempField = new LayerField();
            tempField.SetFieldTypeName("字符串");
            tempField.SetFieldName("图层");
            int tempFIDTid = 1 + 1;
            tempField.SetDataFieldName("F1");
            fieldsList.add(tempField);
            if (layerType == 4) {
                LayerField tempField2 = new LayerField();
                tempField2.SetFieldTypeName("字符串");
                tempField2.SetFieldName("文字");
                int i = tempFIDTid + 1;
                tempField2.SetDataFieldName("F" + tempFIDTid);
                fieldsList.add(tempField2);
                layer.SetIfLabel(true);
                layer.SetLabelDataField("F2");
            } else {
                LayerField tempField3 = new LayerField();
                tempField3.SetFieldTypeName("字符串");
                tempField3.SetFieldName("高程");
                int i2 = tempFIDTid + 1;
                tempField3.SetDataFieldName("F" + tempFIDTid);
                fieldsList.add(tempField3);
            }
            layer.SetLayerName(layerName);
            layer.SetLayerTypeName(layerTypeName);
            layer.SetMaxScale(25000.0d);
            layer.SetLabelScaleMax(25000.0d);
            int tempLayerIndex = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayersCount();
            String tempLayerID = layer.GetLayerID();
            PubVar.m_Workspace.GetDataSourceByEditing().CreateDataset(tempLayerID);
            layer.SetEditMode(EEditMode.NEW);
            layer.SetLayerIndex(tempLayerIndex);
            layer.SetFieldList(fieldsList);
            layer.SaveLayerInfo();
            Handler myHandler = progressDialog.myHandler;
            Message msg = myHandler.obtainMessage();
            msg.what = 5;
            msg.obj = new Object[]{"开始导入数据...", 0};
            myHandler.sendMessage(msg);
            MapCellIndex tempMapCell = new MapCellIndex();
            tempMapCell.setBigCell(new Envelope(this.DXFFileReader.XMin(), this.DXFFileReader.YMax(), this.DXFFileReader.XMax(), this.DXFFileReader.YMin()));
            int tmpColumnsCount = fieldsList.size();
            this.tempTotalGeosCount = totalCount;
            if (this.tempTotalGeosCount > 500) {
                layer.SetMaxScale(25000.0d);
                layer.SetLabelScaleMax(25000.0d);
            } else {
                layer.SetMaxScale(2.147483647E9d);
                layer.SetLabelScaleMax(2.147483647E9d);
            }
            SQLiteDBHelper tempSqlDatabase = PubVar.m_Workspace.GetDataSourceByEditing()._EDatabase;
            this.tmpStartTime = System.currentTimeMillis();
            if (layerType == 1) {
                Iterator<Point> tmpIter = this.DXFFileReader.getPointList().iterator();
                while (tmpIter.hasNext()) {
                    this.tmpGeoIndex++;
                    Point tmpPtn = tmpIter.next();
                    String[] tempFValues = new String[tmpColumnsCount];
                    AbstractGeometry tempGeometry = new  com.xzy.forestSystem.gogisapi.Geometry.Point(tmpPtn.PointX, tmpPtn.PointY);
                    tempFValues[0] = String.valueOf(tmpPtn.LName);
                    tempFValues[0] = String.valueOf(tmpPtn.PointZ);
                    if (tempGeometry != null && !tempGeometry.IsNull()) {
                        result = true;
                        InsertGeoemtry(tempLayerID, layerTypeName, tempGeometry, tempFValues, tempMapCell, tempSqlDatabase, myHandler);
                        if (progressDialog.isCancel) {
                            return true;
                        }
                    }
                }
            } else if (layerType == 2) {
                Iterator<Line> tmpIter2 = this.DXFFileReader.getLineList().iterator();
                while (tmpIter2.hasNext()) {
                    this.tmpGeoIndex++;
                    Line tmpLine = tmpIter2.next();
                    AbstractGeometry tempGeometry2 = new Polyline();
                    List<Coordinate> tmpCoords = new ArrayList<>();
                    tmpCoords.add(new Coordinate(Double.parseDouble(tmpLine.StartX), Double.parseDouble(tmpLine.StartY)));
                    tmpCoords.add(new Coordinate(Double.parseDouble(tmpLine.EndX), Double.parseDouble(tmpLine.EndY)));
                    tempGeometry2.SetAllCoordinateList(tmpCoords);
                    String[] tempFValues2 = new String[tmpColumnsCount];
                    tempFValues2[0] = String.valueOf(tmpLine.LName);
                    tempFValues2[1] = "0";
                    if (tempGeometry2 != null && !tempGeometry2.IsNull()) {
                        result = true;
                        InsertGeoemtry(tempLayerID, layerTypeName, tempGeometry2, tempFValues2, tempMapCell, tempSqlDatabase, myHandler);
                    }
                    if (progressDialog.isCancel) {
                        return result;
                    }
                }
                Iterator<LwPolyLine> tmpIter02 = this.DXFFileReader.getLwpolylineList().iterator();
                while (tmpIter02.hasNext()) {
                    this.tmpGeoIndex++;
                    LwPolyLine tmpLine2 = tmpIter02.next();
                    AbstractGeometry tempGeometry3 = new Polyline();
                    List<Coordinate> tmpCoords2 = new ArrayList<>();
                    int count = tmpLine2.pointx.length;
                    for (int i3 = 0; i3 < count; i3++) {
                        tmpCoords2.add(new Coordinate(tmpLine2.pointx[i3], tmpLine2.pointy[i3]));
                    }
                    tempGeometry3.SetAllCoordinateList(tmpCoords2);
                    String[] tempFValues3 = new String[tmpColumnsCount];
                    tempFValues3[0] = String.valueOf(tmpLine2.LName);
                    tempFValues3[1] = tmpLine2.Elevation;
                    if (tempGeometry3 != null && !tempGeometry3.IsNull()) {
                        result = true;
                        InsertGeoemtry(tempLayerID, layerTypeName, tempGeometry3, tempFValues3, tempMapCell, tempSqlDatabase, myHandler);
                    }
                    if (progressDialog.isCancel) {
                        return result;
                    }
                }
                Iterator<PolyLine> tmpIter03 = this.DXFFileReader.getPolylineList().iterator();
                while (tmpIter03.hasNext()) {
                    this.tmpGeoIndex++;
                    PolyLine tmpLine3 = tmpIter03.next();
                    AbstractGeometry tempGeometry4 = new Polyline();
                    List<Coordinate> tmpCoords3 = new ArrayList<>();
                    Iterator<Point> it = tmpLine3.pointList.iterator();
                    while (it.hasNext()) {
                        Point tmpPtn2 = it.next();
                        tmpCoords3.add(new Coordinate(tmpPtn2.PointX, tmpPtn2.PointY));
                    }
                    tempGeometry4.SetAllCoordinateList(tmpCoords3);
                    String[] tempFValues4 = new String[tmpColumnsCount];
                    tempFValues4[0] = String.valueOf(tmpLine3.LName);
                    tempFValues4[1] = tmpLine3.Elevation;
                    if (tempGeometry4 != null && !tempGeometry4.IsNull()) {
                        result = true;
                        InsertGeoemtry(tempLayerID, layerTypeName, tempGeometry4, tempFValues4, tempMapCell, tempSqlDatabase, myHandler);
                    }
                    if (progressDialog.isCancel) {
                        return result;
                    }
                }
            } else if (layerType == 3) {
                Iterator<LwPolyLine> tmpIter022 = this.DXFFileReader.getLwpolylineList().iterator();
                while (tmpIter022.hasNext()) {
                    this.tmpGeoIndex++;
                    LwPolyLine tmpLine4 = tmpIter022.next();
                    if (tmpLine4.Flag == 1) {
                        AbstractGeometry tempGeometry5 = new Polyline();
                        List<Coordinate> tmpCoords4 = new ArrayList<>();
                        int count2 = tmpLine4.pointx.length;
                        for (int i4 = 0; i4 < count2; i4++) {
                            tmpCoords4.add(new Coordinate(tmpLine4.pointx[i4], tmpLine4.pointy[i4]));
                        }
                        tempGeometry5.SetAllCoordinateList(tmpCoords4);
                        String[] tempFValues5 = new String[tmpColumnsCount];
                        tempFValues5[0] = String.valueOf(tmpLine4.LName);
                        tempFValues5[1] = tmpLine4.Elevation;
                        if (tempGeometry5 != null && !tempGeometry5.IsNull()) {
                            result = true;
                            InsertGeoemtry(tempLayerID, layerTypeName, tempGeometry5, tempFValues5, tempMapCell, tempSqlDatabase, myHandler);
                        }
                        if (progressDialog.isCancel) {
                            return result;
                        }
                    }
                }
            } else if (layerType == 4) {
                Iterator<Text> tmpIter01 = this.DXFFileReader.getTextList().iterator();
                while (tmpIter01.hasNext()) {
                    this.tmpGeoIndex++;
                    Text tmpLine5 = tmpIter01.next();
                    AbstractGeometry tempGeometry6 = new  com.xzy.forestSystem.gogisapi.Geometry.Point(tmpLine5.PointX, tmpLine5.PointY);
                    String[] tempFValues6 = new String[tmpColumnsCount];
                    tempFValues6[0] = String.valueOf(tmpLine5.LName);
                    tempFValues6[1] = tmpLine5.Value;
                    if (tempGeometry6 != null && !tempGeometry6.IsNull()) {
                        result = true;
                        InsertGeoemtry(tempLayerID, layerTypeName, tempGeometry6, tempFValues6, tempMapCell, tempSqlDatabase, myHandler);
                    }
                    if (progressDialog.isCancel) {
                        return result;
                    }
                }
                Iterator<MText> tmpIter023 = this.DXFFileReader.getMTextList().iterator();
                while (tmpIter023.hasNext()) {
                    this.tmpGeoIndex++;
                    MText tmpLine6 = tmpIter023.next();
                    AbstractGeometry tempGeometry7 = new  com.xzy.forestSystem.gogisapi.Geometry.Point(tmpLine6.PointX, tmpLine6.PointY);
                    String[] tempFValues7 = new String[tmpColumnsCount];
                    tempFValues7[0] = String.valueOf(tmpLine6.LName);
                    tempFValues7[1] = tmpLine6.Value;
                    if (tempGeometry7 != null && !tempGeometry7.IsNull()) {
                        result = true;
                        InsertGeoemtry(tempLayerID, layerTypeName, tempGeometry7, tempFValues7, tempMapCell, tempSqlDatabase, myHandler);
                    }
                    if (progressDialog.isCancel) {
                        return result;
                    }
                }
            }
        } catch (Exception e) {
        }
        return result;
    }
}
