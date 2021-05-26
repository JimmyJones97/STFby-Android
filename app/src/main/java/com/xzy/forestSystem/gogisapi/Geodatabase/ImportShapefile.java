package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.os.Handler;
import android.os.Message;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import  com.xzy.forestSystem.gogisapi.Carto.MapCellIndex;
import  com.xzy.forestSystem.gogisapi.Common.BitConverter;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WGS1984;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.ProjectionCoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.WKTCoordinateSystem;
import  com.xzy.forestSystem.gogisapi.Edit.EEditMode;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImportShapefile {
    public String CharacterCode = "GB2312";
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
    String fileName = "导入Shp图层";
    private boolean needConvertCoord = false;

    public void setNeedConvertCoord(boolean needConvert) {
        this.needConvertCoord = needConvert;
    }

    public boolean Import(String filepath, FeatureLayer layer, CustomeProgressDialog progressDialog) {
        ShpObj tmpShpObj;
        List<Coordinate> tempGeoCoords;
        int tempCount2;
        List<Coordinate> tempGeoCoords2;
        int tempCount22;
        List<Coordinate> tempGeoCoords3;
        int tempCount23;
        boolean result = false;
        if (this.CharacterCode == null || this.CharacterCode.trim().length() == 0) {
            this.CharacterCode = "GB2312";
        }
        File tempFile = new File(filepath);
        if (tempFile.exists()) {
            try {
                this.fileName = tempFile.getName();
                double tmpBiasX = 0.0d;
                Handler myHandler = progressDialog.myHandler;
                String tempDBFPath = String.valueOf(filepath.substring(0, filepath.lastIndexOf(FileSelector_Dialog.sFolder))) + ".dbf";
                if (new File(tempDBFPath).exists()) {
                    try {
                        File tempFile3 = new File(String.valueOf(filepath.substring(0, filepath.lastIndexOf(FileSelector_Dialog.sFolder))) + ".prj");
                        if (tempFile3.exists()) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(tempFile3)));
                            StringBuilder tmpSB = new StringBuilder();
                            while (true) {
                                String templine = bufferedReader.readLine();
                                if (templine == null) {
                                    break;
                                }
                                tmpSB.append(templine);
                            }
                            bufferedReader.close();
                            if (tmpSB.length() > 0) {
                                WKTCoordinateSystem tmpWKTCoordinateSystem = new WKTCoordinateSystem();
                                tmpWKTCoordinateSystem.readWKTString(tmpSB.toString());
                                AbstractC0383CoordinateSystem tmpCoordinateSystem = tmpWKTCoordinateSystem.convertToCoordinateSystem();
                                if ((tmpCoordinateSystem instanceof ProjectionCoordinateSystem) && ((ProjectionCoordinateSystem) tmpCoordinateSystem).IsWithDaiHao()) {
                                    tmpBiasX = ((double) (-((ProjectionCoordinateSystem) tmpCoordinateSystem).getFenQu())) * 1000000.0d;
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                    Message msg = myHandler.obtainMessage();
                    msg.what = 5;
                    msg.obj = new Object[]{"开始导入数据,请稍候...", 0};
                    myHandler.sendMessage(msg);
                    DataInputStream disDBF = new DataInputStream(new FileInputStream(tempDBFPath));
                    disDBF.skipBytes(4);
                    int tempGeosCount = BitConverter.readLittleInt(disDBF);
                    short tmpHeadLen = BitConverter.readLittleShort(disDBF);
                    BitConverter.readLittleShort(disDBF);
                    int tempColumnsCount = (tmpHeadLen - 33) / 32;
                    disDBF.skipBytes(20);
                    List<Integer> fieldLength = new ArrayList<>();
                    List<LayerField> fieldsList = new ArrayList<>();
                    int tempFIDTid = 1;
                    for (int i = 0; i < tempColumnsCount; i++) {
                        byte[] tempBytes = new byte[11];
                        disDBF.read(tempBytes);
                        String tempFieldName = new String(tempBytes, this.CharacterCode);
                        byte tempByteType = disDBF.readByte();
                        disDBF.skipBytes(4);
                        byte tempByteLen = disDBF.readByte();
                        byte tempBytePrec = disDBF.readByte();
                        disDBF.skipBytes(14);
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
                            tempFIDTid++;
                            tempField.SetDataFieldName("F" + tempFIDTid);
                            tempField.SetFieldSize(tempByteLen & 255);
                            tempField.SetFieldDecimal(tempBytePrec & 255);
                            fieldsList.add(tempField);
                        } else {
                            tempFIDTid = tempFIDTid;
                        }
                    }
                    DataInputStream disShp = new DataInputStream(new FileInputStream(filepath));
                    disShp.skipBytes(24);
                    this.FileLength = disShp.readInt();
                    disShp.readInt();
                    this.ShapeType = BitConverter.readLittleInt(disShp);
                    this.Xmin = BitConverter.readLittleDouble(disShp) + tmpBiasX;
                    this.Ymin = BitConverter.readLittleDouble(disShp);
                    this.Xmax = BitConverter.readLittleDouble(disShp) + tmpBiasX;
                    this.Ymax = BitConverter.readLittleDouble(disShp);
                    this.Zmin = BitConverter.readLittleDouble(disShp);
                    this.Zmax = BitConverter.readLittleDouble(disShp);
                    this.Mmin = BitConverter.readLittleDouble(disShp);
                    this.Mmax = BitConverter.readLittleDouble(disShp);
                    if (disDBF.readByte() == 13) {
                        String tempLayerTypeName = "无";
                        if (this.ShapeType == 1 || this.ShapeType == 11) {
                            tempLayerTypeName = "点";
                        } else if (this.ShapeType == 3 || this.ShapeType == 13) {
                            tempLayerTypeName = "线";
                        } else if (this.ShapeType == 5 || this.ShapeType == 15) {
                            tempLayerTypeName = "面";
                        }
                        layer.SetLayerTypeName(tempLayerTypeName);
                        layer.SetMaxScale(2.147483647E9d);
                        layer.SetLabelScaleMax(2.147483647E9d);
                        int tempLayerIndex = PubVar._PubCommand.m_ProjectDB.GetLayerManage().GetLayersCount();
                        String tempLayerID = layer.GetLayerID();
                        PubVar.m_Workspace.GetDataSourceByEditing().CreateDataset(tempLayerID);
                        layer.SetEditMode(EEditMode.NEW);
                        layer.SetLayerIndex(tempLayerIndex);
                        layer.SetFieldList(fieldsList);
                        layer.SaveLayerInfo();
                        Message msg2 = myHandler.obtainMessage();
                        msg2.what = 5;
                        msg2.obj = new Object[]{"开始导入数据...", 0};
                        myHandler.sendMessage(msg2);
                        if (this.needConvertCoord) {
                            Coordinate tempCoord = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(new Coordinate(this.Xmin, this.Ymax), Coordinate_WGS1984.Instance());
                            this.Xmin = tempCoord.getX();
                            this.Ymax = tempCoord.getY();
                            Coordinate tempCoord2 = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(new Coordinate(this.Xmax, this.Ymin), Coordinate_WGS1984.Instance());
                            this.Xmax = tempCoord2.getX();
                            this.Ymin = tempCoord2.getY();
                        }
                        MapCellIndex tempMapCell = new MapCellIndex();
                        tempMapCell.setBigCell(new Envelope(this.Xmin, this.Ymax, this.Xmax, this.Ymin));
                        SQLiteDBHelper tempSqlDatabase = PubVar.m_Workspace.GetDataSourceByEditing()._EDatabase;
                        int tempHasFieldsCount = fieldsList.size();
                        int tmpGeoIndex = 0;
                        if (tempGeosCount > 1000) {
                            layer.SetMaxScale(25000.0d);
                            layer.SetLabelScaleMax(25000.0d);
                        }
                        tempSqlDatabase.BeginTransaction();
                        result = true;
                        try {
                            long tmpStartTime = System.currentTimeMillis();
                            for (int i2 = 0; i2 < tempGeosCount; i2++) {
                                boolean tmpBool01 = false;
                                AbstractGeometry tempGeometry = null;
                                try {
                                    BaseDataObject tempBaseData = new BaseDataObject();
                                    tempBaseData.SetBaseObjectRelateLayerID(tempLayerID);
                                    tempBaseData.SetSYS_TYPE(tempLayerTypeName);
                                    if (this.ShapeType == 1) {
                                        Point tmpShpObj2 = new Point();
                                        tmpShpObj2.BiasX = tmpBiasX;
                                        tmpBool01 = tmpShpObj2.ReadData(disShp);
                                        if (tmpBool01) {
                                            Coordinate tempCoord3 = new Coordinate(tmpShpObj2.f472X, tmpShpObj2.f473Y, 0.0d);
                                            if (this.needConvertCoord) {
                                                tempCoord3 = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(tempCoord3, Coordinate_WGS1984.Instance());
                                            }
                                            tempGeometry = new  com.xzy.forestSystem.gogisapi.Geometry.Point(tempCoord3);
                                        }
                                    } else if (this.ShapeType == 5) {
                                        Polygon tmpShpObj3 = new Polygon();
                                        tmpShpObj3.BiasX = tmpBiasX;
                                        tmpBool01 = tmpShpObj3.ReadData(disShp);
                                        if (tmpBool01) {
                                            AbstractGeometry tempGeometry2 = new  com.xzy.forestSystem.gogisapi.Geometry.Polygon();
                                            try {
                                                if (this.needConvertCoord) {
                                                    tempGeoCoords3 = tmpShpObj3.GetXYCoordinates();
                                                } else {
                                                    tempGeoCoords3 = tmpShpObj3.GetCoordinates();
                                                }
                                                if (tmpShpObj3.NumParts > 1) {
                                                    int tempTid = 0;
                                                    int tempCount = tmpShpObj3.NumParts;
                                                    for (int tempJ = 0; tempJ < tempCount; tempJ++) {
                                                        List<Coordinate> tempGeoCoords22 = new ArrayList<>();
                                                        if (tempJ != tempCount - 1) {
                                                            tempCount23 = ((Integer) tmpShpObj3.Parts.get(tempJ + 1)).intValue();
                                                        } else {
                                                            tempCount23 = tmpShpObj3.NumPoints;
                                                        }
                                                        for (int tempK = tempTid; tempK < tempCount23; tempK++) {
                                                            tempGeoCoords22.add(tempGeoCoords3.get(tempK));
                                                        }
                                                        tempGeoCoords22.remove(tempGeoCoords22.size() - 1);
                                                        tempGeometry2.AddPart(tempGeoCoords22);
                                                        tempTid = tempCount23;
                                                    }
                                                    tempGeometry = tempGeometry2;
                                                } else {
                                                    tempGeoCoords3.remove(tempGeoCoords3.size() - 1);
                                                    tempGeometry2.SetAllCoordinateList(tempGeoCoords3);
                                                    tempGeometry = tempGeometry2;
                                                }
                                            } catch (Exception e2) {
                                            }
                                        }
                                    } else if (this.ShapeType == 11) {
                                        PointZ tmpShpObj4 = new PointZ();
                                        tmpShpObj4.BiasX = tmpBiasX;
                                        tmpBool01 = tmpShpObj4.ReadData(disShp);
                                        if (tmpBool01) {
                                            Coordinate tempCoord4 = new Coordinate(tmpShpObj4.f475X, tmpShpObj4.f476Y, tmpShpObj4.f477Z);
                                            if (this.needConvertCoord) {
                                                tempCoord4 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tempCoord4, Coordinate_WGS1984.Instance());
                                            }
                                            tempGeometry = new  com.xzy.forestSystem.gogisapi.Geometry.Point(tempCoord4);
                                        }
                                    } else if (this.ShapeType == 3 || this.ShapeType == 13) {
                                        if (this.ShapeType == 3) {
                                            tmpShpObj = new Polyline();
                                        } else {
                                            tmpShpObj = new PolylineZ();
                                        }
                                        tmpShpObj.BiasX = tmpBiasX;
                                        tmpBool01 = tmpShpObj.ReadData(disShp);
                                        if (tmpBool01) {
                                            Polyline tempPoly = (Polyline) tmpShpObj;
                                            AbstractGeometry tempGeometry3 = new  com.xzy.forestSystem.gogisapi.Geometry.Polyline();
                                            if (this.needConvertCoord) {
                                                tempGeoCoords = tempPoly.GetXYCoordinates();
                                            } else {
                                                tempGeoCoords = tempPoly.GetCoordinates();
                                            }
                                            if (tempPoly.NumParts > 1) {
                                                int tempTid2 = 0;
                                                int tempCount3 = tempPoly.NumParts;
                                                for (int tempJ2 = 0; tempJ2 < tempCount3; tempJ2++) {
                                                    List<Coordinate> tempGeoCoords23 = new ArrayList<>();
                                                    if (tempJ2 != tempCount3 - 1) {
                                                        tempCount2 = tempPoly.Parts.get(tempJ2 + 1).intValue();
                                                    } else {
                                                        tempCount2 = tempPoly.NumPoints;
                                                    }
                                                    for (int tempK2 = tempTid2; tempK2 < tempCount2; tempK2++) {
                                                        tempGeoCoords23.add(tempGeoCoords.get(tempK2));
                                                    }
                                                    tempGeometry3.AddPart(tempGeoCoords23);
                                                    tempTid2 = tempCount2;
                                                }
                                                tempGeometry = tempGeometry3;
                                            } else {
                                                tempGeometry3.SetAllCoordinateList(tempGeoCoords);
                                                tempGeometry = tempGeometry3;
                                            }
                                        }
                                    } else if (this.ShapeType == 15) {
                                        PolygonZ tmpShpObj5 = new PolygonZ();
                                        tmpShpObj5.BiasX = tmpBiasX;
                                        tmpBool01 = tmpShpObj5.ReadData(disShp);
                                        if (tmpBool01) {
                                            AbstractGeometry tempGeometry4 = new  com.xzy.forestSystem.gogisapi.Geometry.Polygon();
                                            if (this.needConvertCoord) {
                                                tempGeoCoords2 = tmpShpObj5.GetXYCoordinates();
                                            } else {
                                                tempGeoCoords2 = tmpShpObj5.GetCoordinates();
                                            }
                                            if (tmpShpObj5.NumParts > 1) {
                                                int tempTid22 = 0;
                                                int tempCount4 = tmpShpObj5.NumParts;
                                                for (int tempJ3 = 0; tempJ3 < tempCount4; tempJ3++) {
                                                    List<Coordinate> tempGeoCoords24 = new ArrayList<>();
                                                    if (tempJ3 != tempCount4 - 1) {
                                                        tempCount22 = ((Integer) tmpShpObj5.Parts.get(tempJ3 + 1)).intValue();
                                                    } else {
                                                        tempCount22 = tmpShpObj5.NumPoints;
                                                    }
                                                    for (int tempK3 = tempTid22; tempK3 < tempCount22; tempK3++) {
                                                        tempGeoCoords24.add(tempGeoCoords2.get(tempK3));
                                                    }
                                                    tempGeometry4.AddPart(tempGeoCoords24);
                                                    tempTid22 = tempCount22;
                                                }
                                                tempGeometry = tempGeometry4;
                                            } else {
                                                tempGeometry4.SetAllCoordinateList(tempGeoCoords2);
                                                tempGeometry = tempGeometry4;
                                            }
                                        }
                                    }
                                    String[] tempFValues = new String[tempColumnsCount];
                                    disDBF.skipBytes(1);
                                    for (int j = 0; j < tempColumnsCount; j++) {
                                        byte[] tempBytes2 = new byte[fieldLength.get(j).intValue()];
                                        if (disDBF.read(tempBytes2) > 0) {
                                            String tempFieldValue = new String(tempBytes2, this.CharacterCode);
                                            if (j >= tempHasFieldsCount || fieldsList.get(j).GetFieldType() != EFieldType.DECIMAL) {
                                                tempFValues[j] = tempFieldValue.trim();
                                            } else {
                                                tempFValues[j] = String.valueOf(Double.parseDouble(tempFieldValue));
                                            }
                                        } else {
                                            tempFValues[j] = "";
                                        }
                                    }
                                    if (tmpBool01 && 1 != 0) {
                                        List<String> tempFieldsList = new ArrayList<>();
                                        tempFieldsList.add("SYS_STATUS='0'");
                                        tempFieldsList.add("SYS_OID='" + UUID.randomUUID().toString() + "'");
                                        tempFieldsList.add("SYS_TYPE='SHP'");
                                        int tempCount001 = tempFValues.length;
                                        for (int tempI = 0; tempI < tempCount001; tempI++) {
                                            String tmpStr01 = tempFValues[tempI];
                                            if (tmpStr01 == null) {
                                                tmpStr01 = "";
                                            }
                                            if (tmpStr01.contains("'")) {
                                                tmpStr01 = tmpStr01.replace("'", "''");
                                            }
                                            tempFieldsList.add("F" + (tempI + 1) + "='" + tmpStr01 + "'");
                                        }
                                        String tempSaveSQL = tempBaseData.GetSaveFieldsAndGeoToDbSQL(tempGeometry, tempFieldsList);
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
                                            String tmpNeedTimeStr = Common.SimplifyTime((((long) (tempGeosCount - tmpGeoIndex)) * (System.currentTimeMillis() - tmpStartTime)) / ((long) tmpGeoIndex));
                                            Message msg3 = myHandler.obtainMessage();
                                            msg3.what = 5;
                                            msg3.obj = new Object[]{"导入数据  [" + tmpGeoIndex + FileSelector_Dialog.sRoot + tempGeosCount + "],剩余时间约" + tmpNeedTimeStr, Integer.valueOf((tmpGeoIndex * 100) / tempGeosCount)};
                                            myHandler.sendMessage(msg3);
                                            if (progressDialog.isCancel) {
                                                break;
                                            }
                                        }
                                    }
                                } catch (Exception e3) {
                                }
                            }
                        } catch (Exception e4) {
                        }
                        tempSqlDatabase.SetTransactionSuccessful();
                        tempSqlDatabase.EndTransaction();
                    }
                }
            } catch (Exception e5) {
            }
        }
        return result;
    }

    /* access modifiers changed from: package-private */
    public class ShpObj {
        public double BiasX = 0.0d;
        public int ContentLength;
        public int RecordNumber;

        ShpObj() {
        }

        public void CalculateLength() {
        }

        public boolean ReadData(DataInputStream dis) throws EOFException {
            return false;
        }

        public boolean SaveData(FileOutputStream fo) {
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public class Point extends ShpObj {

        /* renamed from: X */
        double f472X;

        /* renamed from: Y */
        double f473Y;

        Point() {
            super();
        }

        @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.ShpObj
        public boolean ReadData(DataInputStream dis) throws EOFException {
            try {
                this.RecordNumber = dis.readInt();
                this.ContentLength = dis.readInt();
                dis.readInt();
                this.f472X = BitConverter.readLittleDouble(dis) + this.BiasX;
                this.f473Y = BitConverter.readLittleDouble(dis);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.ShpObj
        public void CalculateLength() {
            this.ContentLength = 10;
        }
    }

    /* access modifiers changed from: package-private */
    public class Polyline extends ShpObj {
        double[] Box = new double[4];
        int NumParts;
        int NumPoints;
        List<Integer> Parts;
        List<Point> Points;

        Polyline() {
            super();
        }

        @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.ShpObj
        public boolean ReadData(DataInputStream dis) {
            try {
                this.Box = new double[4];
                this.Parts = new ArrayList();
                this.Points = new ArrayList();
                this.RecordNumber = dis.readInt();
                this.ContentLength = dis.readInt();
                dis.readInt();
                this.Box[0] = BitConverter.readLittleDouble(dis) + this.BiasX;
                this.Box[1] = BitConverter.readLittleDouble(dis);
                this.Box[2] = BitConverter.readLittleDouble(dis) + this.BiasX;
                this.Box[3] = BitConverter.readLittleDouble(dis);
                this.NumParts = BitConverter.readLittleInt(dis);
                this.NumPoints = BitConverter.readLittleInt(dis);
                for (int i = 0; i < this.NumParts; i++) {
                    this.Parts.add(Integer.valueOf(BitConverter.readLittleInt(dis)));
                }
                for (int j = 0; j < this.NumPoints; j++) {
                    Point tempPtn = new Point();
                    tempPtn.f472X = BitConverter.readLittleDouble(dis) + this.BiasX;
                    tempPtn.f473Y = BitConverter.readLittleDouble(dis);
                    this.Points.add(tempPtn);
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.ShpObj
        public void CalculateLength() {
            this.ContentLength = ((this.NumParts + this.NumPoints) * 2) + 22;
        }

        public List<Coordinate> GetCoordinates() {
            List<Coordinate> list = new ArrayList<>();
            if (this.Points != null) {
                for (Point tempPtn : this.Points) {
                    list.add(new Coordinate(tempPtn.f472X, tempPtn.f473Y, 0.0d));
                }
            }
            return list;
        }

        public List<Coordinate> GetXYCoordinates() {
            List<Coordinate> list = new ArrayList<>();
            if (this.Points != null) {
                for (Point tempPtn : this.Points) {
                    list.add(PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(tempPtn.f472X, tempPtn.f473Y, 0.0d, Coordinate_WGS1984.Instance()));
                }
            }
            return list;
        }
    }

    /* access modifiers changed from: package-private */
    public class Polygon extends Polyline {
        Polygon() {
            super();
        }

        @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.Polyline,  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.ShpObj
        public boolean ReadData(DataInputStream dis) {
            return super.ReadData(dis);
        }

        @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.Polyline,  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.ShpObj
        public void CalculateLength() {
            super.CalculateLength();
        }
    }

    /* access modifiers changed from: package-private */
    public class PointZ extends ShpObj {

        /* renamed from: M */
        double f474M;

        /* renamed from: X */
        double f475X;

        /* renamed from: Y */
        double f476Y;

        /* renamed from: Z */
        double f477Z;

        PointZ() {
            super();
        }

        @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.ShpObj
        public boolean ReadData(DataInputStream dis) {
            try {
                this.RecordNumber = dis.readInt();
                this.ContentLength = dis.readInt();
                dis.readInt();
                this.f475X = BitConverter.readLittleDouble(dis) + this.BiasX;
                this.f476Y = BitConverter.readLittleDouble(dis);
                this.f477Z = BitConverter.readLittleDouble(dis);
                this.f474M = BitConverter.readLittleDouble(dis);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.ShpObj
        public void CalculateLength() {
            this.ContentLength = 36;
        }
    }

    /* access modifiers changed from: package-private */
    public class PolylineZ extends Polyline {
        List<Double> MArray;
        double MMax;
        double MMin;
        List<Double> ZArray;
        double ZMax;
        double ZMin;

        PolylineZ() {
            super();
        }

        @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.Polyline,  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.ShpObj
        public boolean ReadData(DataInputStream dis) {
            try {
                this.Box = new double[4];
                this.Parts = new ArrayList();
                this.Points = new ArrayList();
                this.ZArray = new ArrayList();
                this.MArray = new ArrayList();
                this.RecordNumber = dis.readInt();
                this.ContentLength = dis.readInt();
                dis.readInt();
                this.Box[0] = BitConverter.readLittleDouble(dis) + this.BiasX;
                this.Box[1] = BitConverter.readLittleDouble(dis);
                this.Box[2] = BitConverter.readLittleDouble(dis) + this.BiasX;
                this.Box[3] = BitConverter.readLittleDouble(dis);
                this.NumParts = BitConverter.readLittleInt(dis);
                this.NumPoints = BitConverter.readLittleInt(dis);
                for (int i = 0; i < this.NumParts; i++) {
                    this.Parts.add(Integer.valueOf(BitConverter.readLittleInt(dis)));
                }
                for (int j = 0; j < this.NumPoints; j++) {
                    Point tempPtn = new Point();
                    tempPtn.f472X = BitConverter.readLittleDouble(dis) + this.BiasX;
                    tempPtn.f473Y = BitConverter.readLittleDouble(dis);
                    this.Points.add(tempPtn);
                }
                this.ZMin = BitConverter.readLittleDouble(dis);
                this.ZMax = BitConverter.readLittleDouble(dis);
                for (int j2 = 0; j2 < this.NumPoints; j2++) {
                    this.ZArray.add(Double.valueOf(BitConverter.readLittleDouble(dis)));
                }
                this.MMin = BitConverter.readLittleDouble(dis);
                this.MMax = BitConverter.readLittleDouble(dis);
                for (int j3 = 0; j3 < this.NumPoints; j3++) {
                    this.MArray.add(Double.valueOf(BitConverter.readLittleDouble(dis)));
                }
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.Polyline,  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.ShpObj
        public void CalculateLength() {
            this.ContentLength = (this.NumParts * 2) + 38 + (this.NumPoints * 16);
        }
    }

    /* access modifiers changed from: package-private */
    public class PolygonZ extends PolylineZ {
        PolygonZ() {
            super();
        }

        @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.PolylineZ,  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.Polyline,  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.ShpObj
        public boolean ReadData(DataInputStream dis) {
            return super.ReadData(dis);
        }

        @Override //  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.PolylineZ,  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.Polyline,  com.xzy.forestSystem.gogisapi.Geodatabase.ImportShapefile.ShpObj
        public void CalculateLength() {
            super.CalculateLength();
        }
    }
}
