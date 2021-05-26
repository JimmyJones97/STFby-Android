package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.os.Handler;
import android.os.Message;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Carto.MapCellIndex;
import com.xzy.forestSystem.gogisapi.Common.BitConverter;
import com.xzy.forestSystem.gogisapi.Common.Common;
import com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Shapefile {
    int FileLength = 0;
    double Mmax;
    double Mmin;
    int ShapeType = 0;
    double Xmax;
    double Xmin;
    double Ymax;
    double Ymin;
    double Zmax;
    double Zmin;
    List<LayerField> fieldsList = null;
    List<String[]> fieldsValueArray = null;
    String fileName = "导入Shp图层";
    List<ShpObj> geosArray = null;
    private boolean needConvertCoord = true;

    public List<LayerField> getFieldsList() {
        return this.fieldsList;
    }

    public boolean ReadFile(String filepath, CustomeProgressDialog progressDialog) {
        boolean result = false;
        File tempFile = new File(filepath);
        if (tempFile.exists()) {
            try {
                this.fileName = tempFile.getName();
                Handler myHandler = progressDialog.myHandler;
                String tempDBFPath = String.valueOf(filepath.substring(0, filepath.lastIndexOf(FileSelector_Dialog.sFolder))) + ".dbf";
                if (new File(tempDBFPath).exists()) {
                    this.needConvertCoord = true;
                    try {
                        File tempFile3 = new File(String.valueOf(filepath.substring(0, filepath.lastIndexOf(FileSelector_Dialog.sFolder))) + ".prj");
                        if (tempFile3.exists()) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(tempFile3)));
                            while (true) {
                                String templine = bufferedReader.readLine();
                                if (templine == null) {
                                    break;
                                } else if (templine.contains("PROJCS")) {
                                    this.needConvertCoord = false;
                                }
                            }
                            bufferedReader.close();
                        }
                    } catch (Exception e) {
                    }
                    Message msg = myHandler.obtainMessage();
                    msg.what = 5;
                    msg.obj = new Object[]{"开始读取属性数据.", 0};
                    myHandler.sendMessage(msg);
                    if (progressDialog.isCancel) {
                        return false;
                    }
                    if (readDBF(tempDBFPath, true, progressDialog)) {
                        int tempGeosCount = this.fieldsValueArray.size();
                        DataInputStream dis = new DataInputStream(new FileInputStream(filepath));
                        Message msg2 = myHandler.obtainMessage();
                        msg2.what = 5;
                        msg2.obj = new Object[]{"开始读取几何数据.", 0};
                        myHandler.sendMessage(msg2);
                        boolean flag = true;
                        try {
                            this.geosArray = new ArrayList();
                            dis.skipBytes(24);
                            this.FileLength = dis.readInt();
                            dis.readInt();
                            this.ShapeType = BitConverter.readLittleInt(dis);
                            this.Xmin = BitConverter.readLittleDouble(dis);
                            this.Ymin = BitConverter.readLittleDouble(dis);
                            this.Xmax = BitConverter.readLittleDouble(dis);
                            this.Ymax = BitConverter.readLittleDouble(dis);
                            this.Zmin = BitConverter.readLittleDouble(dis);
                            this.Zmax = BitConverter.readLittleDouble(dis);
                            this.Mmin = BitConverter.readLittleDouble(dis);
                            this.Mmax = BitConverter.readLittleDouble(dis);
                            if (this.needConvertCoord && (this.Xmin < -180.0d || this.Xmax > 180.0d || this.Ymin < -90.0d || this.Ymax > 90.0d)) {
                                this.needConvertCoord = false;
                            }
                            int tempGeoIndex = 0;
                            int tmpLastPrgValue = 0;
                            if (this.ShapeType == 1) {
                                while (flag) {
                                    Point tempGeo = new Point();
                                    if (tempGeo.ReadData(dis)) {
                                        this.geosArray.add(tempGeo);
                                        tempGeoIndex++;
                                        int tmpPrgValue = (tempGeoIndex * 100) / tempGeosCount;
                                        if (tmpPrgValue != tmpLastPrgValue) {
                                            Message msg3 = myHandler.obtainMessage();
                                            msg3.what = 5;
                                            msg3.obj = new Object[]{"处理几何数据  [" + tempGeoIndex + FileSelector_Dialog.sRoot + tempGeosCount + "]", Integer.valueOf(tmpPrgValue)};
                                            myHandler.sendMessage(msg3);
                                            tmpLastPrgValue = tmpPrgValue;
                                            if (progressDialog.isCancel) {
                                                return false;
                                            }
                                        } else {
                                            continue;
                                        }
                                    } else {
                                        flag = false;
                                    }
                                }
                            } else if (this.ShapeType == 3) {
                                while (flag) {
                                    Polyline tempGeo2 = new Polyline();
                                    if (tempGeo2.ReadData(dis)) {
                                        this.geosArray.add(tempGeo2);
                                        tempGeoIndex++;
                                        Message msg4 = myHandler.obtainMessage();
                                        msg4.what = 5;
                                        msg4.obj = new Object[]{"处理几何数据  [" + tempGeoIndex + FileSelector_Dialog.sRoot + tempGeosCount + "]", Integer.valueOf((tempGeoIndex * 100) / tempGeosCount)};
                                        myHandler.sendMessage(msg4);
                                        if (progressDialog.isCancel) {
                                            dis.close();
                                            return false;
                                        }
                                    } else {
                                        flag = false;
                                    }
                                }
                            } else if (this.ShapeType == 5) {
                                while (flag) {
                                    Polygon tempGeo3 = new Polygon();
                                    if (tempGeo3.ReadData(dis)) {
                                        this.geosArray.add(tempGeo3);
                                        tempGeoIndex++;
                                        Message msg5 = myHandler.obtainMessage();
                                        msg5.what = 5;
                                        msg5.obj = new Object[]{"处理几何数据  [" + tempGeoIndex + FileSelector_Dialog.sRoot + tempGeosCount + "]", Integer.valueOf((tempGeoIndex * 100) / tempGeosCount)};
                                        myHandler.sendMessage(msg5);
                                        if (progressDialog.isCancel) {
                                            dis.close();
                                            return false;
                                        }
                                    } else {
                                        flag = false;
                                    }
                                }
                            } else if (this.ShapeType == 11) {
                                while (flag) {
                                    PointZ tempGeo4 = new PointZ();
                                    if (tempGeo4.ReadData(dis)) {
                                        this.geosArray.add(tempGeo4);
                                        tempGeoIndex++;
                                        Message msg6 = myHandler.obtainMessage();
                                        msg6.what = 5;
                                        msg6.obj = new Object[]{"处理几何数据  [" + tempGeoIndex + FileSelector_Dialog.sRoot + tempGeosCount + "]", Integer.valueOf((tempGeoIndex * 100) / tempGeosCount)};
                                        myHandler.sendMessage(msg6);
                                        if (progressDialog.isCancel) {
                                            dis.close();
                                            return false;
                                        }
                                    } else {
                                        flag = false;
                                    }
                                }
                            } else if (this.ShapeType == 13) {
                                while (flag) {
                                    PolylineZ tempGeo5 = new PolylineZ();
                                    if (tempGeo5.ReadData(dis)) {
                                        this.geosArray.add(tempGeo5);
                                        tempGeoIndex++;
                                        Message msg7 = myHandler.obtainMessage();
                                        msg7.what = 5;
                                        msg7.obj = new Object[]{"处理几何数据  [" + tempGeoIndex + FileSelector_Dialog.sRoot + tempGeosCount + "]", Integer.valueOf((tempGeoIndex * 100) / tempGeosCount)};
                                        myHandler.sendMessage(msg7);
                                        if (progressDialog.isCancel) {
                                            dis.close();
                                            return false;
                                        }
                                    } else {
                                        flag = false;
                                    }
                                }
                            } else if (this.ShapeType == 15) {
                                while (flag) {
                                    PolygonZ tempGeo6 = new PolygonZ();
                                    if (tempGeo6.ReadData(dis)) {
                                        this.geosArray.add(tempGeo6);
                                        tempGeoIndex++;
                                        Message msg8 = myHandler.obtainMessage();
                                        msg8.what = 5;
                                        msg8.obj = new Object[]{"处理几何数据  [" + tempGeoIndex + FileSelector_Dialog.sRoot + tempGeosCount + "]", Integer.valueOf((tempGeoIndex * 100) / tempGeosCount)};
                                        myHandler.sendMessage(msg8);
                                        if (progressDialog.isCancel) {
                                            dis.close();
                                            return false;
                                        }
                                    } else {
                                        flag = false;
                                    }
                                }
                            }
                            dis.close();
                        } catch (EOFException e2) {
                        } finally {
                            dis.close();
                        }
                        result = true;
                    }
                }
            } catch (Exception e3) {
            }
        }
        return result;
    }

    public boolean readDBF(String filepath, boolean readFieldsValue, CustomeProgressDialog progressDialog) {
        int tempTid;
        boolean result = false;
        this.fieldsList = new ArrayList();
        this.fieldsValueArray = new ArrayList();
        Handler myHandler = null;
        if (progressDialog != null) {
            myHandler = progressDialog.myHandler;
        }
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(filepath));
            try {
                dis.skipBytes(4);
                int tempCount = BitConverter.readLittleInt(dis);
                short tmpHeadLen = BitConverter.readLittleShort(dis);
                BitConverter.readLittleShort(dis);
                int tempColumnsCount = (tmpHeadLen - 33) / 32;
                dis.skipBytes(20);
                List<Integer> fieldLength = new ArrayList<>();
                int i = 0;
                int tempTid2 = 1;
                while (i < tempColumnsCount) {
                    byte[] tempBytes = new byte[11];
                    dis.read(tempBytes);
                    String tempFieldName = new String(tempBytes, "GB2312");
                    byte tempByteType = dis.readByte();
                    dis.skipBytes(4);
                    byte tempByteLen = dis.readByte();
                    byte tempBytePrec = dis.readByte();
                    dis.skipBytes(14);
                    fieldLength.add(Integer.valueOf(tempByteLen & 255));
                    LayerField tempField = new LayerField();
                    boolean tempTag = false;
                    if (tempByteType == 67) {
                        tempField.SetFieldTypeName("字符串");
                        tempTag = true;
                    } else if (tempByteType == 70) {
                        tempField.SetFieldTypeName("浮点型");
                        tempTag = true;
                    } else if (tempByteType == 78) {
                        if (tempBytePrec == 0) {
                            tempField.SetFieldTypeName("整型");
                        } else {
                            tempField.SetFieldTypeName("浮点型");
                        }
                        tempTag = true;
                    } else if (tempByteType == 68) {
                        tempField.SetFieldTypeName("日期型");
                        tempTag = true;
                    }
                    if (tempTag) {
                        tempField.SetFieldName(tempFieldName.trim());
                        tempTid = tempTid2 + 1;
                        tempField.SetDataFieldName("F" + tempTid2);
                        tempField.SetFieldSize(tempByteLen & 255);
                        tempField.SetFieldDecimal(tempBytePrec & 255);
                        this.fieldsList.add(tempField);
                    } else {
                        tempTid = tempTid2;
                    }
                    i++;
                    tempTid2 = tempTid;
                }
                if (readFieldsValue && dis.readByte() == 13) {
                    int tempHasFieldsCount = this.fieldsList.size();
                    int tmpLastPrgValue = 0;
                    for (int i2 = 0; i2 < tempCount; i2++) {
                        String[] tempFValues = new String[tempColumnsCount];
                        dis.skipBytes(1);
                        for (int j = 0; j < tempColumnsCount; j++) {
                            byte[] tempBytes2 = new byte[fieldLength.get(j).intValue()];
                            if (dis.read(tempBytes2) > 0) {
                                String tempFieldValue = new String(tempBytes2, "GB2312");
                                if (j >= tempHasFieldsCount || this.fieldsList.get(j).GetFieldType() != EFieldType.DECIMAL) {
                                    tempFValues[j] = tempFieldValue.trim();
                                } else {
                                    tempFValues[j] = String.valueOf(Double.parseDouble(tempFieldValue));
                                }
                            }
                        }
                        this.fieldsValueArray.add(tempFValues);
                        if (myHandler != null) {
                            int tmpPrgValue = ((i2 + 1) * 100) / tempCount;
                            if (tmpPrgValue != tmpLastPrgValue) {
                                Message msg = myHandler.obtainMessage();
                                msg.what = 5;
                                msg.obj = new Object[]{"处理属性数据  [" + (i2 + 1) + FileSelector_Dialog.sRoot + tempCount + "]", Integer.valueOf(tmpPrgValue)};
                                myHandler.sendMessage(msg);
                                tmpLastPrgValue = tmpPrgValue;
                                if (progressDialog.isCancel) {
                                    return false;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                }
                result = true;
                dis.close();
            } catch (EOFException e) {
            } finally {
                dis.close();
            }
        } catch (Exception e2) {
        }
        return result;
    }

    public boolean ImportFile(FeatureLayer layer, CustomeProgressDialog progressDialog) {
        List<Coordinate> tempGeoCoords;
        int tempCount2;
        List<Coordinate> tempGeoCoords2;
        int tempCount22;
        List<Coordinate> tempGeoCoords3;
        int tempCount23;
        String[] tempFieldValues;
        boolean result = false;
        if (layer != null) {
            try {
                if (this.geosArray != null && this.geosArray.size() > 0 && this.fieldsValueArray != null && this.fieldsValueArray.size() > 0) {
                    Handler myHandler = progressDialog.myHandler;
                    if (this.fileName.contains(FileSelector_Dialog.sFolder)) {
                        layer.SetLayerName(this.fileName.substring(0, this.fileName.lastIndexOf(FileSelector_Dialog.sFolder)));
                    } else {
                        layer.SetLayerName(this.fileName);
                    }
                    String tempLayerTypeName = "无";
                    if (this.ShapeType == 1 || this.ShapeType == 11) {
                        tempLayerTypeName = "点";
                    } else if (this.ShapeType == 3 || this.ShapeType == 13) {
                        tempLayerTypeName = "线";
                    } else if (this.ShapeType == 5 || this.ShapeType == 15) {
                        tempLayerTypeName = "面";
                    }
                    layer.SetLayerTypeName(tempLayerTypeName);
                    layer.SetMaxScale(25000.0d);
                    layer.SetLabelScaleMax(25000.0d);
                    int tempLayerIndex = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayersCount();
                    String tempLayerID = layer.GetLayerID();
                    PubVar.m_Workspace.GetDataSourceByEditing().CreateDataset(tempLayerID);
                    layer.SetEditMode(EEditMode.NEW);
                    layer.SetLayerIndex(tempLayerIndex);
                    layer.SetFieldList(this.fieldsList);
                    layer.SaveLayerInfo();
                    Message msg = myHandler.obtainMessage();
                    msg.what = 5;
                    msg.obj = new Object[]{"开始导入数据...", 0};
                    myHandler.sendMessage(msg);
                    if (this.needConvertCoord) {
                        Coordinate tempCoord = PubVar.m_Workspace.GetCoorSystem().ConvertBLToXY(new Coordinate(this.Xmin, this.Ymax));
                        this.Xmin = tempCoord.getX();
                        this.Ymax = tempCoord.getY();
                        Coordinate tempCoord2 = PubVar.m_Workspace.GetCoorSystem().ConvertBLToXY(new Coordinate(this.Xmax, this.Ymin));
                        this.Xmax = tempCoord2.getX();
                        this.Ymin = tempCoord2.getY();
                    }
                    MapCellIndex tempMapCell = new MapCellIndex();
                    tempMapCell.setBigCell(new Envelope(this.Xmin, this.Ymax, this.Xmax, this.Ymin));
                    SQLiteDBHelper tempSqlDatabase = PubVar.m_Workspace.GetDataSourceByEditing()._EDatabase;
                    tempSqlDatabase.BeginTransaction();
                    int tid = 0;
                    int tmpGeoIndex = 0;
                    try {
                        int tempGeosCount = this.geosArray.size();
                        int tmpLastPrgValue = 0;
                        for (ShpObj tempObj : this.geosArray) {
                            String tempSaveSQL = null;
                            AbstractGeometry tempGeometry = null;
                            List<String> tempFieldsList = new ArrayList<>();
                            tempFieldsList.add("SYS_STATUS='0'");
                            tempFieldsList.add("SYS_OID='" + UUID.randomUUID().toString() + "'");
                            tempFieldsList.add("SYS_TYPE='SHP'");
                            if (tid < this.fieldsValueArray.size() && (tempFieldValues = this.fieldsValueArray.get(tid)) != null) {
                                int tempCount001 = tempFieldValues.length;
                                for (int tempI = 0; tempI < tempCount001; tempI++) {
                                    tempFieldsList.add("F" + (tempI + 1) + "='" + tempFieldValues[tempI] + "'");
                                }
                            }
                            if (this.ShapeType == 1) {
                                Coordinate tempCoord3 = new Coordinate(((Point) tempObj).f478X, ((Point) tempObj).f479Y, 0.0d);
                                if (this.needConvertCoord) {
                                    tempCoord3 = PubVar.m_Workspace.GetCoorSystem().ConvertBLToXY(tempCoord3);
                                }
//                                tempGeometry = new Point(tempCoord3);
                                BaseDataObject tempBaseData = new BaseDataObject();
                                tempBaseData.SetBaseObjectRelateLayerID(tempLayerID);
                                tempBaseData.SetSYS_TYPE(tempLayerTypeName);
                                tempSaveSQL = tempBaseData.GetSaveFieldsAndGeoToDbSQL(tempGeometry, tempFieldsList);
                            } else if (this.ShapeType == 11) {
                                Coordinate tempCoord4 = new Coordinate(((PointZ) tempObj).f481X, ((PointZ) tempObj).f482Y, ((PointZ) tempObj).f483Z);
                                if (this.needConvertCoord) {
                                    tempCoord4 = PubVar.m_Workspace.GetCoorSystem().ConvertBLToXY(tempCoord4);
                                }
//                                tempGeometry = new Point(tempCoord4);
                                BaseDataObject tempBaseData2 = new BaseDataObject();
                                tempBaseData2.SetBaseObjectRelateLayerID(tempLayerID);
                                tempBaseData2.SetSYS_TYPE(tempLayerTypeName);
                                tempSaveSQL = tempBaseData2.GetSaveFieldsAndGeoToDbSQL(tempGeometry, tempFieldsList);
                            } else if (this.ShapeType == 3 || this.ShapeType == 13) {
                                Polyline tempPoly = (Polyline) tempObj;
//                                tempGeometry = new Polyline();
                                if (this.needConvertCoord) {
                                    tempGeoCoords = tempPoly.GetXYCoordinates();
                                } else {
                                    tempGeoCoords = tempPoly.GetCoordinates();
                                }
                                if (tempPoly.NumParts > 1) {
                                    int tempTid = 0;
                                    int tempCount = tempPoly.NumParts;
                                    for (int tempJ = 0; tempJ < tempCount; tempJ++) {
                                        List<Coordinate> tempGeoCoords22 = new ArrayList<>();
                                        if (tempJ != tempCount - 1) {
                                            tempCount2 = tempPoly.Parts.get(tempJ + 1).intValue();
                                        } else {
                                            tempCount2 = tempPoly.NumPoints;
                                        }
                                        for (int tempK = tempTid; tempK < tempCount2; tempK++) {
                                            tempGeoCoords22.add(tempGeoCoords.get(tempK));
                                        }
                                        tempGeometry.AddPart(tempGeoCoords22);
                                        tempTid = tempCount2;
                                    }
                                } else {
                                    tempGeometry.SetAllCoordinateList(tempGeoCoords);
                                }
                                BaseDataObject tempBaseData3 = new BaseDataObject();
                                tempBaseData3.SetBaseObjectRelateLayerID(tempLayerID);
                                tempBaseData3.SetSYS_TYPE(tempLayerTypeName);
                                tempSaveSQL = tempBaseData3.GetSaveFieldsAndGeoToDbSQL(tempGeometry, tempFieldsList);
                            } else if (this.ShapeType == 5) {
                                Polygon tempPoly2 = (Polygon) tempObj;
//                                tempGeometry = new Polygon();
                                if (this.needConvertCoord) {
                                    tempGeoCoords3 = tempPoly2.GetXYCoordinates();
                                } else {
                                    tempGeoCoords3 = tempPoly2.GetCoordinates();
                                }
                                if (tempPoly2.NumParts > 1) {
                                    int tempTid2 = 0;
                                    int tempCount3 = tempPoly2.NumParts;
                                    for (int tempJ2 = 0; tempJ2 < tempCount3; tempJ2++) {
                                        List<Coordinate> tempGeoCoords23 = new ArrayList<>();
                                        if (tempJ2 != tempCount3 - 1) {
                                            tempCount23 = tempPoly2.Parts.get(tempJ2 + 1).intValue();
                                        } else {
                                            tempCount23 = tempPoly2.NumPoints;
                                        }
                                        for (int tempK2 = tempTid2; tempK2 < tempCount23; tempK2++) {
                                            tempGeoCoords23.add(tempGeoCoords3.get(tempK2));
                                        }
                                        tempGeometry.AddPart(tempGeoCoords23);
                                        tempTid2 = tempCount23;
                                    }
                                } else {
                                    tempGeometry.SetAllCoordinateList(tempGeoCoords3);
                                }
                                BaseDataObject tempBaseData4 = new BaseDataObject();
                                tempBaseData4.SetBaseObjectRelateLayerID(tempLayerID);
                                tempBaseData4.SetSYS_TYPE(tempLayerTypeName);
                                tempSaveSQL = tempBaseData4.GetSaveFieldsAndGeoToDbSQL(tempGeometry, tempFieldsList);
                            } else if (this.ShapeType == 15) {
                                PolygonZ tempPoly3 = (PolygonZ) tempObj;
//                                tempGeometry = new Polygon();
                                if (this.needConvertCoord) {
                                    tempGeoCoords2 = tempPoly3.GetXYCoordinates();
                                } else {
                                    tempGeoCoords2 = tempPoly3.GetCoordinates();
                                }
                                if (tempPoly3.NumParts > 1) {
                                    int tempTid3 = 0;
                                    int tempCount4 = tempPoly3.NumParts;
                                    for (int tempJ3 = 0; tempJ3 < tempCount4; tempJ3++) {
                                        List<Coordinate> tempGeoCoords24 = new ArrayList<>();
                                        if (tempJ3 != tempCount4 - 1) {
                                            tempCount22 = ((Integer) tempPoly3.Parts.get(tempJ3 + 1)).intValue();
                                        } else {
                                            tempCount22 = tempPoly3.NumPoints;
                                        }
                                        for (int tempK3 = tempTid3; tempK3 < tempCount22; tempK3++) {
                                            tempGeoCoords24.add(tempGeoCoords2.get(tempK3));
                                        }
                                        tempGeometry.AddPart(tempGeoCoords24);
                                        tempTid3 = tempCount22;
                                    }
                                } else {
                                    tempGeometry.SetAllCoordinateList(tempGeoCoords2);
                                }
                                BaseDataObject tempBaseData5 = new BaseDataObject();
                                tempBaseData5.SetBaseObjectRelateLayerID(tempLayerID);
                                tempBaseData5.SetSYS_TYPE(tempLayerTypeName);
                                tempSaveSQL = tempBaseData5.GetSaveFieldsAndGeoToDbSQL(tempGeometry, tempFieldsList);
                            }
                            if (!(tempSaveSQL == null || tempGeometry == null)) {
                                byte[] tempGeoBytes = Common.ConvertGeoToBytes(tempGeometry);
                                if (tempGeoBytes != null && tempSqlDatabase.ExecuteSQL(tempSaveSQL, new Object[]{tempGeoBytes})) {
                                    tmpGeoIndex++;
                                    Envelope tempGeoExtend = tempGeometry.getEnvelope();
                                    int[] tempRCIndex = tempMapCell.CalCellIndexsOne(tempGeoExtend);
                                    if (tempRCIndex != null) {
                                        tempSqlDatabase.ExecuteSQL("Insert Into " + tempLayerID + "_I (SYS_ID,RIndex,CIndex,MinX,MinY,MaxX,MaxY) values (" + tmpGeoIndex + "," + tempRCIndex[0] + "," + tempRCIndex[1] + "," + tempGeoExtend.getMinX() + "," + tempGeoExtend.getMinY() + "," + tempGeoExtend.getMaxX() + "," + tempGeoExtend.getMaxY() + ")");
                                    }
                                }
                                int tmpPrgValue = ((tid + 1) * 100) / tempGeosCount;
                                if (tmpPrgValue != tmpLastPrgValue) {
                                    Message msg2 = myHandler.obtainMessage();
                                    msg2.what = 5;
                                    msg2.obj = new Object[]{"导入数据  [" + tid + FileSelector_Dialog.sRoot + tempGeosCount + "]", Integer.valueOf((tid * 100) / tempGeosCount)};
                                    myHandler.sendMessage(msg2);
                                    tmpLastPrgValue = tmpPrgValue;
                                    if (progressDialog.isCancel) {
                                        tempSqlDatabase.EndTransaction();
                                        return true;
                                    }
                                } else {
                                    continue;
                                }
                            }
                            tid++;
                        }
                        tempSqlDatabase.SetTransactionSuccessful();
                    } catch (Exception e) {
                    } finally {
                        tempSqlDatabase.EndTransaction();
                    }
                    result = true;
                }
            } catch (Exception e2) {
            }
        }
        return result;
    }
}
