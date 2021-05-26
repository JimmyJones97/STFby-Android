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
import  com.xzy.forestSystem.gogisapi.Geometry.Point;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgLine;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgLwPolyline;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgMText;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgPoint;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgPolyline2D;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgPolyline3D;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgSpline;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgText;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.Point2D;
import  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImportDWGClass {
    double Xmax;
    double Xmin;
    double Ymax;
    double Ymin;

    public List<Coordinate> ConvertPoint2DToCoordsList(Point2D[] points) {
        List<Coordinate> result = new ArrayList<>();
        for (Point2D tmpPtn : points) {
            result.add(new Coordinate(tmpPtn.getX(), tmpPtn.getY()));
        }
        return result;
    }

    public void UpdateExtend(DwgObject dwgObject) {
        int i = 0;
        if (dwgObject != null) {
            try {
                if (dwgObject instanceof DwgPoint) {
                    double[] tmpCoord = ((DwgPoint) dwgObject).getPoint();
                    if (this.Xmin > tmpCoord[0]) {
                        this.Xmin = tmpCoord[0];
                    }
                    if (this.Xmax < tmpCoord[0]) {
                        this.Xmax = tmpCoord[0];
                    }
                    if (this.Ymin > tmpCoord[1]) {
                        this.Ymin = tmpCoord[1];
                    }
                    if (this.Ymax < tmpCoord[1]) {
                        this.Ymax = tmpCoord[1];
                    }
                } else if (dwgObject instanceof DwgLwPolyline) {
                    DwgLwPolyline tmpPLine = (DwgLwPolyline) dwgObject;
                    if (tmpPLine.getVertices() != null) {
                        Point2D[] tmpPtns = tmpPLine.getVertices();
                        int length = tmpPtns.length;
                        while (i < length) {
                            double[] tmpCoord2 = tmpPtns[i].getCoord();
                            if (this.Xmin > tmpCoord2[0]) {
                                this.Xmin = tmpCoord2[0];
                            }
                            if (this.Xmax < tmpCoord2[0]) {
                                this.Xmax = tmpCoord2[0];
                            }
                            if (this.Ymin > tmpCoord2[1]) {
                                this.Ymin = tmpCoord2[1];
                            }
                            if (this.Ymax < tmpCoord2[1]) {
                                this.Ymax = tmpCoord2[1];
                            }
                            i++;
                        }
                    }
                } else if (dwgObject instanceof DwgPolyline2D) {
                    DwgPolyline2D tmpPLine2 = (DwgPolyline2D) dwgObject;
                    if (tmpPLine2.getPts() != null) {
                        Point2D[] tmpPtns2 = tmpPLine2.getPts();
                        int length2 = tmpPtns2.length;
                        while (i < length2) {
                            double[] tmpCoord3 = tmpPtns2[i].getCoord();
                            if (this.Xmin > tmpCoord3[0]) {
                                this.Xmin = tmpCoord3[0];
                            }
                            if (this.Xmax < tmpCoord3[0]) {
                                this.Xmax = tmpCoord3[0];
                            }
                            if (this.Ymin > tmpCoord3[1]) {
                                this.Ymin = tmpCoord3[1];
                            }
                            if (this.Ymax < tmpCoord3[1]) {
                                this.Ymax = tmpCoord3[1];
                            }
                            i++;
                        }
                    }
                } else if (dwgObject instanceof DwgLine) {
                    DwgLine tmpPLine3 = (DwgLine) dwgObject;
                    if (tmpPLine3.getP1() != null && tmpPLine3.getP2() != null) {
                        double[] tmpCoord4 = tmpPLine3.getP1();
                        if (this.Xmin > tmpCoord4[0]) {
                            this.Xmin = tmpCoord4[0];
                        }
                        if (this.Xmax < tmpCoord4[0]) {
                            this.Xmax = tmpCoord4[0];
                        }
                        if (this.Ymin > tmpCoord4[1]) {
                            this.Ymin = tmpCoord4[1];
                        }
                        if (this.Ymax < tmpCoord4[1]) {
                            this.Ymax = tmpCoord4[1];
                        }
                        double[] tmpCoord5 = tmpPLine3.getP2();
                        if (this.Xmin > tmpCoord5[0]) {
                            this.Xmin = tmpCoord5[0];
                        }
                        if (this.Xmax < tmpCoord5[0]) {
                            this.Xmax = tmpCoord5[0];
                        }
                        if (this.Ymin > tmpCoord5[1]) {
                            this.Ymin = tmpCoord5[1];
                        }
                        if (this.Ymax < tmpCoord5[1]) {
                            this.Ymax = tmpCoord5[1];
                        }
                    }
                } else if (dwgObject instanceof DwgPolyline3D) {
                    DwgPolyline3D tmpPLine4 = (DwgPolyline3D) dwgObject;
                    if (tmpPLine4.getPts() != null) {
                        double[][] tmpPtns3 = tmpPLine4.getPts();
                        int length3 = tmpPtns3.length;
                        while (i < length3) {
                            double[] tmpCoord6 = tmpPtns3[i];
                            if (this.Xmin > tmpCoord6[0]) {
                                this.Xmin = tmpCoord6[0];
                            }
                            if (this.Xmax < tmpCoord6[0]) {
                                this.Xmax = tmpCoord6[0];
                            }
                            if (this.Ymin > tmpCoord6[1]) {
                                this.Ymin = tmpCoord6[1];
                            }
                            if (this.Ymax < tmpCoord6[1]) {
                                this.Ymax = tmpCoord6[1];
                            }
                            i++;
                        }
                    }
                } else if (dwgObject instanceof DwgSpline) {
                    DwgSpline tmpPLine5 = (DwgSpline) dwgObject;
                    if (tmpPLine5.getFitPoints() != null) {
                        double[][] tmpPtns4 = tmpPLine5.getFitPoints();
                        int length4 = tmpPtns4.length;
                        while (i < length4) {
                            double[] tmpCoord7 = tmpPtns4[i];
                            if (this.Xmin > tmpCoord7[0]) {
                                this.Xmin = tmpCoord7[0];
                            }
                            if (this.Xmax < tmpCoord7[0]) {
                                this.Xmax = tmpCoord7[0];
                            }
                            if (this.Ymin > tmpCoord7[1]) {
                                this.Ymin = tmpCoord7[1];
                            }
                            if (this.Ymax < tmpCoord7[1]) {
                                this.Ymax = tmpCoord7[1];
                            }
                            i++;
                        }
                    }
                } else if (dwgObject instanceof DwgMText) {
                    double[] tmpCoord8 = ((DwgMText) dwgObject).getInsertionPoint();
                    if (this.Xmin > tmpCoord8[0]) {
                        this.Xmin = tmpCoord8[0];
                    }
                    if (this.Xmax < tmpCoord8[0]) {
                        this.Xmax = tmpCoord8[0];
                    }
                    if (this.Ymin > tmpCoord8[1]) {
                        this.Ymin = tmpCoord8[1];
                    }
                    if (this.Ymax < tmpCoord8[1]) {
                        this.Ymax = tmpCoord8[1];
                    }
                } else if (dwgObject instanceof DwgText) {
                    double[] tmpCoord9 = ((DwgText) dwgObject).getInsertionPoint().getCoord();
                    if (this.Xmin > tmpCoord9[0]) {
                        this.Xmin = tmpCoord9[0];
                    }
                    if (this.Xmax < tmpCoord9[0]) {
                        this.Xmax = tmpCoord9[0];
                    }
                    if (this.Ymin > tmpCoord9[1]) {
                        this.Ymin = tmpCoord9[1];
                    }
                    if (this.Ymax < tmpCoord9[1]) {
                        this.Ymax = tmpCoord9[1];
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    public boolean Import(FeatureLayer layer, String layerName, int layerType, List<DwgObject> geoList, CustomeProgressDialog progressDialog) {
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
            if (layerType == 4) {
                LayerField tempField = new LayerField();
                tempField.SetFieldTypeName("字符串");
                tempField.SetFieldName("文字");
                int i = 1 + 1;
                tempField.SetDataFieldName("F1");
                fieldsList.add(tempField);
                layer.SetIfLabel(true);
                layer.SetLabelDataField("F1");
            } else {
                LayerField tempField2 = new LayerField();
                tempField2.SetFieldTypeName("字符串");
                tempField2.SetFieldName("高程");
                int i2 = 1 + 1;
                tempField2.SetDataFieldName("F1");
                fieldsList.add(tempField2);
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
            this.Ymin = Double.MAX_VALUE;
            this.Xmin = Double.MAX_VALUE;
            this.Ymax = Double.MIN_VALUE;
            this.Xmax = Double.MIN_VALUE;
            for (DwgObject dwgObject : geoList) {
                UpdateExtend(dwgObject);
            }
            tempMapCell.setBigCell(new Envelope(this.Xmin, this.Ymax, this.Xmax, this.Ymin));
            int tmpGeoIndex = 0;
            int tmpGeoIndexDone = 0;
            int tmpColumnsCount = fieldsList.size();
            int tempGeosCount = geoList.size();
            if (tempGeosCount > 500) {
                layer.SetMaxScale(25000.0d);
                layer.SetLabelScaleMax(25000.0d);
            } else {
                layer.SetMaxScale(2.147483647E9d);
                layer.SetLabelScaleMax(2.147483647E9d);
            }
            SQLiteDBHelper tempSqlDatabase = PubVar.m_Workspace.GetDataSourceByEditing()._EDatabase;
            long tmpStartTime = System.currentTimeMillis();
            for (DwgObject dwgObject2 : geoList) {
                tmpGeoIndex++;
                AbstractGeometry tempGeometry = null;
                String[] tempFValues = new String[tmpColumnsCount];
                if (layerType == 1) {
                    double[] tmpCoord = ((DwgPoint) dwgObject2).getPoint();
                    tempGeometry = new Point(tmpCoord[0], tmpCoord[1]);
                    tempFValues[0] = String.valueOf(tmpCoord[2]);
                } else if (layerType == 2) {
                    if (dwgObject2 instanceof DwgLwPolyline) {
                        DwgLwPolyline tmpPLine = (DwgLwPolyline) dwgObject2;
                        if (tmpPLine.getVertices() != null) {
                            List<Coordinate> tmpCoords = ConvertPoint2DToCoordsList(tmpPLine.getVertices());
                            if (tmpCoords.size() > 1) {
                                tempGeometry = new Polyline();
                                tempGeometry.SetAllCoordinateList(tmpCoords);
                                tempFValues[0] = String.valueOf(tmpPLine.getElevation());
                            }
                        }
                    } else if (dwgObject2 instanceof DwgPolyline2D) {
                        DwgPolyline2D tmpPLine2 = (DwgPolyline2D) dwgObject2;
                        if (tmpPLine2.getPts() != null) {
                            List<Coordinate> tmpCoords2 = ConvertPoint2DToCoordsList(tmpPLine2.getPts());
                            if (tmpCoords2.size() > 1) {
                                tempGeometry = new Polyline();
                                tempGeometry.SetAllCoordinateList(tmpCoords2);
                                tempFValues[0] = String.valueOf(tmpPLine2.getElevation());
                            }
                        }
                    } else if (dwgObject2 instanceof DwgLine) {
                        DwgLine tmpPLine3 = (DwgLine) dwgObject2;
                        if (!(tmpPLine3.getP1() == null || tmpPLine3.getP2() == null)) {
                            tempGeometry = new Polyline();
                            List<Coordinate> tmpCoords3 = new ArrayList<>();
                            tmpCoords3.add(new Coordinate(tmpPLine3.getP1()[0], tmpPLine3.getP1()[1]));
                            tmpCoords3.add(new Coordinate(tmpPLine3.getP2()[0], tmpPLine3.getP2()[1]));
                            tempGeometry.SetAllCoordinateList(tmpCoords3);
                            tempFValues[0] = "0";
                        }
                    }
                } else if (layerType == 3) {
                    if (dwgObject2 instanceof DwgLwPolyline) {
                        DwgLwPolyline tmpPLine4 = (DwgLwPolyline) dwgObject2;
                        if (tmpPLine4.getVertices() != null) {
                            List<Coordinate> tmpCoords4 = ConvertPoint2DToCoordsList(tmpPLine4.getVertices());
                            if (tmpCoords4.size() > 1) {
                                tempGeometry = new Polygon();
                                tempGeometry.SetAllCoordinateList(tmpCoords4);
                                tempFValues[0] = String.valueOf(tmpPLine4.getElevation());
                            }
                        }
                    } else if (dwgObject2 instanceof DwgPolyline2D) {
                        DwgPolyline2D tmpPLine5 = (DwgPolyline2D) dwgObject2;
                        if (tmpPLine5.getPts() != null) {
                            List<Coordinate> tmpCoords5 = ConvertPoint2DToCoordsList(tmpPLine5.getPts());
                            if (tmpCoords5.size() > 1) {
                                tempGeometry = new Polygon();
                                tempGeometry.SetAllCoordinateList(tmpCoords5);
                                tempFValues[0] = String.valueOf(tmpPLine5.getElevation());
                            }
                        }
                    }
                } else if (layerType == 4) {
                    if (dwgObject2 instanceof DwgMText) {
                        DwgMText tmpDwgPoint = (DwgMText) dwgObject2;
                        double[] tmpCoord2 = tmpDwgPoint.getInsertionPoint();
                        tempGeometry = new Point(tmpCoord2[0], tmpCoord2[1]);
                        tempFValues[0] = tmpDwgPoint.getText();
                    } else if (dwgObject2 instanceof DwgText) {
                        DwgText tmpDwgPoint2 = (DwgText) dwgObject2;
                        double[] tmpCoord3 = tmpDwgPoint2.getInsertionPoint().getCoord();
                        tempGeometry = new Point(tmpCoord3[0], tmpCoord3[1]);
                        tempFValues[0] = tmpDwgPoint2.getText();
                    }
                }
                if (tempGeometry != null) {
                    result = true;
                    BaseDataObject tempBaseData = new BaseDataObject();
                    tempBaseData.SetBaseObjectRelateLayerID(tempLayerID);
                    tempBaseData.SetSYS_TYPE(layerTypeName);
                    List<String> tempFieldsList = new ArrayList<>();
                    tempFieldsList.add("SYS_STATUS='0'");
                    tempFieldsList.add("SYS_OID='" + UUID.randomUUID().toString() + "'");
                    tempFieldsList.add("SYS_TYPE='DWG'");
                    int tempCount001 = tempFValues.length;
                    for (int tempI = 0; tempI < tempCount001; tempI++) {
                        tempFieldsList.add("F" + (tempI + 1) + "='" + tempFValues[tempI] + "'");
                    }
                    String tmpSaveSql = tempBaseData.GetSaveFieldsAndGeoToDbSQL(tempGeometry, tempFieldsList);
                    if (!(tmpSaveSql == null || tempGeometry == null)) {
                        byte[] tempGeoBytes = Common.ConvertGeoToBytes(tempGeometry);
                        if (tempGeoBytes != null && tempSqlDatabase.ExecuteSQL(tmpSaveSql, new Object[]{tempGeoBytes})) {
                            tmpGeoIndexDone++;
                            Envelope tempGeoExtend = tempGeometry.getEnvelope();
                            int[] tempRCIndex = tempMapCell.CalCellIndexsOne(tempGeoExtend);
                            if (tempRCIndex != null) {
                                tempSqlDatabase.ExecuteSQL("Insert Into " + tempLayerID + "_I (SYS_ID,RIndex,CIndex,MinX,MinY,MaxX,MaxY) values (" + tmpGeoIndexDone + "," + tempRCIndex[0] + "," + tempRCIndex[1] + "," + tempGeoExtend.getMinX() + "," + tempGeoExtend.getMinY() + "," + tempGeoExtend.getMaxX() + "," + tempGeoExtend.getMaxY() + ")");
                            }
                        }
                        String tmpNeedTimeStr = Common.SimplifyTime((((long) (tempGeosCount - tmpGeoIndex)) * (System.currentTimeMillis() - tmpStartTime)) / ((long) tmpGeoIndex));
                        Message msg2 = myHandler.obtainMessage();
                        msg2.what = 5;
                        msg2.obj = new Object[]{"导入数据  [" + tmpGeoIndex + FileSelector_Dialog.sRoot + tempGeosCount + "],剩余时间约" + tmpNeedTimeStr, Integer.valueOf((tmpGeoIndex * 100) / tempGeosCount)};
                        myHandler.sendMessage(msg2);
                        if (progressDialog.isCancel) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
        return result;
    }
}
