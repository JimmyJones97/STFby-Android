package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.os.Handler;
import android.os.Message;
import com.xzy.forestSystem.baidu.speech.easr.easrNativeJni;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.FeatureLayer;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.LayerField;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.Coordinate_WGS1984;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;
import  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ExportToShp {
    public String CharacterCode = "GB2312";
    private boolean hasOutputTipDialog = false;
    private String m_ExportType = "";
    private CustomeProgressDialog progressDialog = null;
    Exception ex;
    private byte[] DoubleToBytes(double paramDouble) {
        byte[] arrayOfByte = new byte[8];
        long l = Double.doubleToLongBits(paramDouble);
        for (int i = 0; i < 8; i++) {
            arrayOfByte[i] = new Long(l).byteValue();
            l >>= 8;
        }
        return arrayOfByte;
    }

    private byte[] IntToBytes(int paramInt) {
        byte[] arrayOfByte = new byte[4];
        for (int i = 0; i < 4; i++) {
            arrayOfByte[3 - i] = (byte) (paramInt >> (24 - (i * 8)));
        }
        return arrayOfByte;
    }

    public void setBindProgressDialog(CustomeProgressDialog customeProgressDialog) {
        this.progressDialog = customeProgressDialog;
        this.hasOutputTipDialog = true;
    }

    public void setExportCoordType(String ExportCoordType) {
        this.m_ExportType = ExportCoordType;
    }

    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x02c9: APUT  
      (r6v0 'arrayOfObject' java.lang.Object[] A[D('arrayOfObject' java.lang.Object[])])
      (2 ??[int, float, short, byte, char])
      (wrap: java.lang.String : 0x02c5: INVOKE  (r38v8 java.lang.String) = 
      (wrap: java.lang.StringBuilder : 0x02c1: INVOKE  (r38v7 java.lang.StringBuilder) = 
      (wrap: java.lang.StringBuilder : 0x02bb: INVOKE  (r38v6 java.lang.StringBuilder) = 
      (wrap: java.lang.StringBuilder : 0x02ae: CONSTRUCTOR  (r38v5 java.lang.StringBuilder) = (" SYS_ID In (") call: java.lang.StringBuilder.<init>(java.lang.String):void type: CONSTRUCTOR)
      (wrap: java.lang.String : 0x02b7: INVOKE  (r39v3 java.lang.String) = (","), (r44v0 'SYS_IDList' java.util.List<java.lang.String> A[D('SYS_IDList' java.util.List<java.lang.String>)]) type: STATIC call:  com.xzy.forestSystem.gogisapi.Common.Common.CombineStrings(java.lang.String, java.util.List):java.lang.String)
     type: VIRTUAL call: java.lang.StringBuilder.append(java.lang.String):java.lang.StringBuilder)
      (")")
     type: VIRTUAL call: java.lang.StringBuilder.append(java.lang.String):java.lang.StringBuilder)
     type: VIRTUAL call: java.lang.StringBuilder.toString():java.lang.String)
     */
    private boolean ToDbf(DataSet paramDataset, String filepath, List<String> SYS_IDList) {
        boolean result = false;
        try {
            File tmpFile = new File(filepath);
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            FeatureLayer pLayer = PubVar._PubCommand.m_ProjectDB.GetLayerInDataSource(paramDataset.getDataSource().getName(), paramDataset.getName());
            if (pLayer != null) {
                List<String> tmpArrayList01 = new ArrayList<>();
                List<String> tmpArrayList02 = new ArrayList<>();
                int i = 0;
                for (LayerField tmpField : pLayer.GetFieldList()) {
                    String str1 = "";
                    if (tmpField.GetFieldType() == EFieldType.STRING) {
                        str1 = "C";
                        i += tmpField.GetFieldSize();
                    } else if (tmpField.GetFieldType() == EFieldType.DECIMAL) {
                        str1 = "F";
                        i += tmpField.GetFieldSize();
                    } else if (tmpField.GetFieldType() == EFieldType.DATETIME) {
                        str1 = "C";
                        i += 20;
                    } else if (tmpField.GetFieldType() == EFieldType.INTEGER) {
                        str1 = "N";
                        i += tmpField.GetFieldSize();
                    } else if (tmpField.GetFieldType() == EFieldType.BOOLEAN) {
                        str1 = "C";
                        i += 5;
                    }
                    tmpArrayList01.add(String.valueOf(tmpField.GetFieldName()) + "," + str1 + "," + tmpField.GetFieldSize());
                    tmpArrayList02.add(tmpField.GetDataFieldName());
                }
                Handler myHandler = null;
                if (this.hasOutputTipDialog) {
                    myHandler = this.progressDialog.myHandler;
                    Message msg = myHandler.obtainMessage();
                    msg.what = 5;
                    msg.obj = new Object[]{"开始读取属性数据...", 0};
                    myHandler.sendMessage(msg);
                }
                Object[] arrayOfObject = new Object[3];
                arrayOfObject[0] = Common.CombineStrings(",", tmpArrayList02);
                arrayOfObject[1] = paramDataset.getDataTableName();
                if (SYS_IDList == null) {
                    arrayOfObject[2] = " 1=1 ";
                } else {
                    arrayOfObject[2] = " SYS_ID In (" + Common.CombineStrings(",", SYS_IDList) + ")";
                }
                SQLiteReader tempReader = paramDataset.getDataSource().Query(String.format("select %1$s from %2$s Where SYS_STATUS=0 And %3$s order by SYS_ID", arrayOfObject));
                if (tempReader != null) {
                    int j = tmpArrayList01.size();
                    List<ArrayList> tmpArrayList03 = new ArrayList<>();
                    while (tempReader.Read()) {
                        ArrayList tmpArrayList05 = new ArrayList();
                        for (String str : tmpArrayList02) {
                            String tempValueStr = tempReader.GetString(str);
                            if (tempValueStr == null) {
                                tempValueStr = "";
                            }
                            tmpArrayList05.add(tempValueStr);
                        }
                        tmpArrayList03.add(tmpArrayList05);
                    }
                    tempReader.Close();
                    if (this.hasOutputTipDialog) {
                        Message msg2 = myHandler.obtainMessage();
                        msg2.what = 5;
                        msg2.obj = new Object[]{"开始导出属性数据...", 0};
                        myHandler.sendMessage(msg2);
                    }
                    RandomAccessFile localRandomAccessFile = new RandomAccessFile(tmpFile, "rw");
                    localRandomAccessFile.writeByte(3);
                    localRandomAccessFile.writeByte(13);
                    localRandomAccessFile.writeByte(7);
                    localRandomAccessFile.writeByte(9);
                    localRandomAccessFile.write(IntToBytes(tmpArrayList03.size()));
                    localRandomAccessFile.write(ShortToBytes((j * 32) + 33));
                    localRandomAccessFile.write(ShortToBytes(i + 1));
                    localRandomAccessFile.write(new byte[2]);
                    localRandomAccessFile.writeByte(0);
                    localRandomAccessFile.writeByte(0);
                    localRandomAccessFile.write(new byte[12]);
                    localRandomAccessFile.writeByte(0);
                    localRandomAccessFile.writeByte(0);
                    localRandomAccessFile.write(new byte[2]);
                    for (String str2 : tmpArrayList01) {
                        String[] arrayOfString = str2.split(",");
                        byte[] arrayOfByte1 = arrayOfString[0].getBytes(this.CharacterCode);
                        byte[] arrayOfByte2 = new byte[11];
                        if (arrayOfByte1.length <= 0) {
                            break;
                        }
                        int tmpLen = 11;
                        if (arrayOfByte1.length < 11) {
                            tmpLen = arrayOfByte1.length;
                        }
                        for (int tmpI = 0; tmpI < tmpLen; tmpI++) {
                            arrayOfByte2[tmpI] = arrayOfByte1[tmpI];
                        }
                        localRandomAccessFile.write(arrayOfByte2);
                        localRandomAccessFile.write(arrayOfString[1].getBytes()[0]);
                        localRandomAccessFile.write(new byte[4]);
                        localRandomAccessFile.writeByte(Integer.parseInt(arrayOfString[2]));
                        localRandomAccessFile.writeByte(0);
                        localRandomAccessFile.write(new byte[2]);
                        localRandomAccessFile.writeByte(0);
                        localRandomAccessFile.write(new byte[11]);
                    }
                    localRandomAccessFile.writeByte(13);
                    result = true;
                    if (tmpArrayList03.size() > 0) {
                        int tmpColsCount = tmpArrayList01.size();
                        int tid = 0;
                        int tmpOutputCount = tmpArrayList03.size();
                        for (ArrayList tmpArrayList04 : tmpArrayList03) {
                            tid++;
                            if (this.hasOutputTipDialog) {
                                Message msg3 = myHandler.obtainMessage();
                                msg3.what = 5;
                                msg3.obj = new Object[]{"开始导出属性数据 [" + tid + FileSelector_Dialog.sRoot + tmpOutputCount + "]", Integer.valueOf((tid * 100) / tmpOutputCount)};
                                myHandler.sendMessage(msg3);
                                if (this.progressDialog.isCancel) {
                                    return false;
                                }
                            }
                            localRandomAccessFile.writeByte(20);
                            for (int tmpJ = 0; tmpJ < tmpColsCount; tmpJ++) {
                                int tempLen2 = Integer.parseInt(tmpArrayList01.get(tmpJ).split(",")[2]);
                                byte[] arrayOfByte3 = ((String) tmpArrayList04.get(tmpJ)).getBytes(Charset.forName(this.CharacterCode));
                                byte[] arrayOfByte4 = new byte[tempLen2];
                                int tmpLen2 = tempLen2;
                                if (arrayOfByte3.length < tmpLen2) {
                                    tmpLen2 = arrayOfByte3.length;
                                }
                                for (int tmpI2 = 0; tmpI2 < tmpLen2; tmpI2++) {
                                    arrayOfByte4[tmpI2] = arrayOfByte3[tmpI2];
                                }
                                localRandomAccessFile.write(arrayOfByte4);
                            }
                        }
                        localRandomAccessFile.close();
                        if (this.hasOutputTipDialog) {
                            Message msg4 = myHandler.obtainMessage();
                            msg4.what = 5;
                            msg4.obj = new Object[]{"导出属性数据完成.", 100};
                            myHandler.sendMessage(msg4);
                        }
                        result = true;
                    }
                }
            }
        } catch (Exception e) {
        }
        return result;
    }

    public boolean ToPrj(String filepath) {
        String tempPrjMsg;
        try {
            if (this.m_ExportType.equals("WGS-84坐标")) {
                tempPrjMsg = "GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]]";
            } else {
                tempPrjMsg = PubVar.m_Workspace.GetCoorSystem().ToCoordSystemFileInfo();
            }
            try {
                if (tempPrjMsg.equals("")) {
                    return false;
                }
                String str2 = new String(tempPrjMsg.getBytes("unicode"), "unicode");
                try {
                    File tmpFile = new File(filepath);
                    if (tmpFile.exists()) {
                        tmpFile.delete();
                    }
                    FileWriter localFileWriter = new FileWriter(filepath);
                    BufferedWriter localBufferedWriter = new BufferedWriter(localFileWriter);
                    localBufferedWriter.write(str2);
                    localBufferedWriter.close();
                    localFileWriter.close();
                    return true;
                } catch (Exception e) {
                    return false;
                }
            } catch (Exception e2) {
                return false;
            }
        } catch (Exception e3) {
            return false;
        }
    }

    private boolean ToShp(DataSet pDataset, String filepath, List<String> SYS_IDList) {
        int tmpEndIndex;
        try {
            File tmpFile = new File(filepath);
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            if (SYS_IDList == null) {
                List<String> SYS_IDList2 = new ArrayList<>();
                try {
                    SQLiteReader localSQLiteDataReader = pDataset.getDataSource().Query("select SYS_ID from " + pDataset.getDataTableName() + " where SYS_STATUS=0 Order By SYS_ID");
                    if (localSQLiteDataReader != null) {
                        while (localSQLiteDataReader.Read()) {
                            SYS_IDList2.add(String.valueOf(localSQLiteDataReader.GetInt32(0)));
                        }
                        localSQLiteDataReader.Close();
                    }
                    SYS_IDList = SYS_IDList2;
                } catch (Exception e) {
                    ex = e;
                    Common.Log("ExportToShp", ex.getLocalizedMessage());
                    return false;
                }
            }
            if (SYS_IDList.size() == 0) {
                return false;
            }
            int tmpTotalLen = 50;
            int totalCount = SYS_IDList.size();
            EGeoLayerType tempGeoType = pDataset.getType();
            if (tempGeoType == EGeoLayerType.POINT) {
                tmpTotalLen = 50 + (totalCount * 14);
            } else if (tempGeoType != EGeoLayerType.NONE) {
                for (String tmpSYSID : SYS_IDList) {
                    AbstractGeometry tmpGeo = pDataset.GetGeometryBySYSID(tmpSYSID);
                    if (tmpGeo == null) {
                        tmpGeo = pDataset.QueryGeometryFromDBBy_SYSIDOnly(tmpSYSID);
                    }
                    if (tmpGeo != null) {
                        tmpTotalLen += (tmpGeo.GetNumberOfParts() * 2) + 26 + (tmpGeo.GetNumberOfPoints() * 8);
                    }
                }
            }
            Handler myHandler = null;
            if (this.hasOutputTipDialog) {
                myHandler = this.progressDialog.myHandler;
                Message msg = myHandler.obtainMessage();
                msg.what = 5;
                msg.obj = new Object[]{"开始导出几何数据...", 0};
                myHandler.sendMessage(msg);
            }
            boolean needConvert = false;
            if (this.m_ExportType.equals("WGS-84坐标")) {
                needConvert = true;
            }
            RandomAccessFile localRandomAccessFile = new RandomAccessFile(tmpFile, "rw");
            WriteShpHeader(pDataset.getType(), pDataset.GetExtendFromDB(), localRandomAccessFile, "SHP", totalCount, tmpTotalLen, true);
            if (totalCount <= 0) {
                localRandomAccessFile.close();
                return true;
            } else if (SYS_IDList.size() > 0) {
                int tid = 0;
                for (String tmpSYSID2 : SYS_IDList) {
                    AbstractGeometry tmpGeo2 = pDataset.GetGeometryBySYSID(tmpSYSID2);
                    if (tmpGeo2 == null) {
                        tmpGeo2 = pDataset.QueryGeometryFromDBBy_SYSIDOnly(tmpSYSID2);
                    }
                    if (tmpGeo2 != null) {
                        localRandomAccessFile.writeInt(tid + 1);
                        tid++;
                        if (this.hasOutputTipDialog) {
                            Message msg2 = myHandler.obtainMessage();
                            msg2.what = 5;
                            msg2.obj = new Object[]{"开始导出几何数据 [" + tid + FileSelector_Dialog.sRoot + totalCount + "]", Integer.valueOf((tid * 100) / totalCount)};
                            myHandler.sendMessage(msg2);
                            if (this.progressDialog.isCancel) {
                                return false;
                            }
                        }
                        if (tempGeoType == EGeoLayerType.POINT) {
                            localRandomAccessFile.writeInt(10);
                            localRandomAccessFile.write(IntToBytes(1));
                            Coordinate localCoordinate5 = tmpGeo2.GetAllCoordinateList().get(0);
                            double d9 = localCoordinate5.getX();
                            double d10 = localCoordinate5.getY();
                            if (needConvert) {
                                Coordinate tmpPointCoord = PubVar.m_Workspace.GetCoorSystem().ConvertXYToBL(localCoordinate5.getX(), localCoordinate5.getY());
                                d9 = tmpPointCoord.getGeoX();
                                d10 = tmpPointCoord.getGeoY();
                            }
                            localRandomAccessFile.write(DoubleToBytes(d9));
                            localRandomAccessFile.write(DoubleToBytes(d10));
                        } else if (tempGeoType == EGeoLayerType.POLYLINE) {
                            localRandomAccessFile.writeInt((tmpGeo2.GetNumberOfParts() * 2) + 22 + (tmpGeo2.GetNumberOfPoints() * 8));
                            localRandomAccessFile.write(IntToBytes(3));
                            Envelope localEnvelope = tmpGeo2.getEnvelope();
                            double d1 = localEnvelope.getMinX();
                            double d2 = localEnvelope.getMinY();
                            double d3 = localEnvelope.getMaxX();
                            double d4 = localEnvelope.getMaxY();
                            if (needConvert) {
                                Coordinate localCoordinate3 = PubVar.m_Workspace.GetCoorSystem().ConvertXYToBL(localEnvelope.getLeftTop().getX(), localEnvelope.getLeftTop().getY());
                                Coordinate localCoordinate4 = PubVar.m_Workspace.GetCoorSystem().ConvertXYToBL(localEnvelope.getRightBottom().getX(), localEnvelope.getRightBottom().getY());
                                d1 = localCoordinate3.getGeoX();
                                d2 = localCoordinate4.getGeoY();
                                d3 = localCoordinate4.getGeoX();
                                d4 = localCoordinate3.getGeoY();
                            }
                            localRandomAccessFile.write(DoubleToBytes(d1));
                            localRandomAccessFile.write(DoubleToBytes(d2));
                            localRandomAccessFile.write(DoubleToBytes(d3));
                            localRandomAccessFile.write(DoubleToBytes(d4));
                            int tmpNumParts = tmpGeo2.GetNumberOfParts();
                            localRandomAccessFile.write(IntToBytes(tmpNumParts));
                            localRandomAccessFile.write(IntToBytes(tmpGeo2.GetAllCoordinateList().size()));
                            if (tmpNumParts == 1) {
                                localRandomAccessFile.write(IntToBytes(0));
                            } else {
                                for (Integer num : tmpGeo2.GetPartIndex()) {
                                    localRandomAccessFile.write(IntToBytes(num.intValue()));
                                }
                            }
                            for (Coordinate tempCoord : tmpGeo2.GetAllCoordinateList()) {
                                double d5 = tempCoord.getX();
                                double d6 = tempCoord.getY();
                                if (needConvert) {
                                    Coordinate tmpPointCoord2 = PubVar.m_Workspace.GetCoorSystem().ConvertXYToBL(tempCoord.getX(), tempCoord.getY());
                                    d5 = tmpPointCoord2.getGeoX();
                                    d6 = tmpPointCoord2.getGeoY();
                                }
                                localRandomAccessFile.write(DoubleToBytes(d5));
                                localRandomAccessFile.write(DoubleToBytes(d6));
                            }
                        } else if (tempGeoType == EGeoLayerType.POLYGON) {
                            if (tid == 102) {
                                Common.Log("XX", String.valueOf(tid));
                            }
                            int tmpPointCount = tmpGeo2.GetAllCoordinateList().size();
                            localRandomAccessFile.writeInt((tmpGeo2.GetNumberOfParts() * 2) + 22 + ((tmpGeo2.GetNumberOfPoints() + tmpGeo2.GetNumberOfParts()) * 8));
                            localRandomAccessFile.write(IntToBytes(5));
                            Envelope localEnvelope2 = tmpGeo2.getEnvelope();
                            double d12 = localEnvelope2.getMinX();
                            double d22 = localEnvelope2.getMinY();
                            double d32 = localEnvelope2.getMaxX();
                            double d42 = localEnvelope2.getMaxY();
                            if (needConvert) {
                                Coordinate localCoordinate32 = PubVar.m_Workspace.GetCoorSystem().ConvertXYToBL(localEnvelope2.getLeftTop().getX(), localEnvelope2.getLeftTop().getY());
                                Coordinate localCoordinate42 = PubVar.m_Workspace.GetCoorSystem().ConvertXYToBL(localEnvelope2.getRightBottom().getX(), localEnvelope2.getRightBottom().getY());
                                d12 = localCoordinate32.getGeoX();
                                d22 = localCoordinate42.getGeoY();
                                d32 = localCoordinate42.getGeoX();
                                d42 = localCoordinate32.getGeoY();
                            }
                            localRandomAccessFile.write(DoubleToBytes(d12));
                            localRandomAccessFile.write(DoubleToBytes(d22));
                            localRandomAccessFile.write(DoubleToBytes(d32));
                            localRandomAccessFile.write(DoubleToBytes(d42));
                            int tmpNumParts2 = tmpGeo2.GetNumberOfParts();
                            localRandomAccessFile.write(IntToBytes(tmpNumParts2));
                            localRandomAccessFile.write(IntToBytes(tmpPointCount + tmpGeo2.GetNumberOfParts()));
                            if (tmpNumParts2 == 1) {
                                localRandomAccessFile.write(IntToBytes(0));
                            } else {
                                int tmpTid = 0;
                                for (Integer num2 : tmpGeo2.GetPartIndex()) {
                                    tmpTid++;
                                    localRandomAccessFile.write(IntToBytes(num2.intValue() + tmpTid));
                                }
                            }
                            int tmpStartIndex = 0;
                            List<Coordinate> tmpCoordList = tmpGeo2.GetAllCoordinateList();
                            for (int tmpI01 = 0; tmpI01 < tmpNumParts2; tmpI01++) {
                                if (tmpI01 == tmpNumParts2 - 1) {
                                    tmpEndIndex = tmpGeo2.GetNumberOfPoints();
                                } else {
                                    tmpEndIndex = tmpGeo2.GetPartIndex().get(tmpI01 + 1).intValue();
                                }
                                for (int tmpI02 = tmpStartIndex; tmpI02 < tmpEndIndex; tmpI02++) {
                                    Coordinate tempCoord2 = tmpCoordList.get(tmpI02);
                                    double tmpCoordX01 = tempCoord2.getX();
                                    double tmpCoordY01 = tempCoord2.getY();
                                    if (needConvert) {
                                        Coordinate tmpPointCoord3 = PubVar.m_Workspace.GetCoorSystem().ConvertXYToBL(tempCoord2.getX(), tempCoord2.getY());
                                        tmpCoordX01 = tmpPointCoord3.getGeoX();
                                        tmpCoordY01 = tmpPointCoord3.getGeoY();
                                    }
                                    localRandomAccessFile.write(DoubleToBytes(tmpCoordX01));
                                    localRandomAccessFile.write(DoubleToBytes(tmpCoordY01));
                                }
                                Coordinate tempCoord3 = tmpCoordList.get(tmpStartIndex);
                                double tmpCoordX012 = tempCoord3.getX();
                                double tmpCoordY012 = tempCoord3.getY();
                                if (needConvert) {
                                    Coordinate tmpPointCoord4 = PubVar.m_Workspace.GetCoorSystem().ConvertXYToBL(tempCoord3.getX(), tempCoord3.getY());
                                    tmpCoordX012 = tmpPointCoord4.getGeoX();
                                    tmpCoordY012 = tmpPointCoord4.getGeoY();
                                }
                                localRandomAccessFile.write(DoubleToBytes(tmpCoordX012));
                                localRandomAccessFile.write(DoubleToBytes(tmpCoordY012));
                                tmpStartIndex = tmpEndIndex;
                            }
                        }
                    } else if (this.hasOutputTipDialog) {
                        Message msg3 = myHandler.obtainMessage();
                        msg3.what = 5;
                        msg3.obj = new Object[]{"导出几何第[ " + tid + " ]个数据时错误.", Integer.valueOf((tid * 100) / totalCount)};
                        myHandler.sendMessage(msg3);
                    }
                }
                if (this.hasOutputTipDialog) {
                    Message msg4 = myHandler.obtainMessage();
                    msg4.what = 5;
                    msg4.obj = new Object[]{"导出几何数据完成.", 100};
                    myHandler.sendMessage(msg4);
                }
                return true;
            } else {
                if (this.hasOutputTipDialog) {
                    Message msg5 = myHandler.obtainMessage();
                    msg5.what = 5;
                    msg5.obj = new Object[]{"没有任何需要导出的数据.", 100};
                    myHandler.sendMessage(msg5);
                }
                return false;
            }
        } catch (Exception e2) {
            ex = e2;
            Common.Log("ExportToShp", ex.getLocalizedMessage());
            return false;
        }
    }

    private boolean ToShx(DataSet pDataset, String filepath, List<String> SYS_IDList) {
        int totalCount;
        int GetNumberOfParts;
        int GetNumberOfPoints;
        try {
            File tmpFile = new File(filepath);
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            if (SYS_IDList != null) {
                totalCount = SYS_IDList.size();
            } else {
                totalCount = pDataset.getValidTotalCount();
            }
            Handler myHandler = null;
            if (this.hasOutputTipDialog) {
                myHandler = this.progressDialog.myHandler;
                Message msg = myHandler.obtainMessage();
                msg.what = 5;
                msg.obj = new Object[]{"开始导出索引数据...", 0};
                myHandler.sendMessage(msg);
            }
            if (this.m_ExportType.equals("WGS-84坐标")) {
            }
            RandomAccessFile localRandomAccessFile = new RandomAccessFile(tmpFile, "rw");
            WriteShpHeader(pDataset.getType(), pDataset.GetExtendFromDB(), localRandomAccessFile, "SHX", totalCount, 0, true);
            if (totalCount <= 0) {
                localRandomAccessFile.close();
                return true;
            }
            if (pDataset.getType() == EGeoLayerType.POINT) {
                for (int j = 0; j < totalCount; j++) {
                    localRandomAccessFile.writeInt((j * 14) + 50);
                    localRandomAccessFile.writeInt(10);
                }
            } else if (pDataset.getType() != EGeoLayerType.NONE) {
                if (SYS_IDList == null) {
                    List<String> SYS_IDList2 = new ArrayList<>();
                    try {
                        SQLiteReader localSQLiteDataReader = pDataset.getDataSource().Query("select SYS_ID from " + pDataset.getDataTableName() + " where SYS_STATUS=0 ");
                        if (localSQLiteDataReader != null) {
                            while (localSQLiteDataReader.Read()) {
                                SYS_IDList2.add(String.valueOf(localSQLiteDataReader.GetInt32(0)));
                            }
                            localSQLiteDataReader.Close();
                        }
                        SYS_IDList = SYS_IDList2;
                    } catch (Exception e) {
                        return false;
                    }
                }
                if (SYS_IDList.size() > 0) {
                    int i = 0;
                    int j2 = 0;
                    boolean tmpIsPolygon = false;
                    if (pDataset.getType() == EGeoLayerType.POLYGON) {
                        tmpIsPolygon = true;
                    }
                    for (String tmpSYSID : SYS_IDList) {
                        AbstractGeometry tmpGeo = pDataset.GetGeometryBySYSID(tmpSYSID);
                        if (tmpGeo == null) {
                            tmpGeo = pDataset.QueryGeometryFromDBBy_SYSIDOnly(tmpSYSID);
                        }
                        if (tmpGeo != null) {
                            if (tmpIsPolygon) {
                                GetNumberOfParts = (tmpGeo.GetNumberOfParts() * 2) + 22;
                                GetNumberOfPoints = tmpGeo.GetNumberOfPoints() + tmpGeo.GetNumberOfParts();
                            } else {
                                GetNumberOfParts = (tmpGeo.GetNumberOfParts() * 2) + 22;
                                GetNumberOfPoints = tmpGeo.GetNumberOfPoints();
                            }
                            int tmpContentLength = GetNumberOfParts + (GetNumberOfPoints * 8);
                            localRandomAccessFile.writeInt((j2 * 4) + i + 50);
                            localRandomAccessFile.writeInt(tmpContentLength);
                            i += tmpContentLength;
                            j2++;
                        }
                    }
                }
                if (this.hasOutputTipDialog) {
                    Message msg2 = myHandler.obtainMessage();
                    msg2.what = 5;
                    msg2.obj = new Object[]{"导出索引数据完成.", 100};
                    myHandler.sendMessage(msg2);
                }
            }
            localRandomAccessFile.close();
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    private void WriteShpHeader(EGeoLayerType geoLayerType, Envelope extend, RandomAccessFile paramRandomAccessFile, String headType, int dataCount, int fileLen, boolean _NeedConsiderConvert) {
        try {
            paramRandomAccessFile.writeInt(9994);
            paramRandomAccessFile.writeInt(0);
            paramRandomAccessFile.writeInt(0);
            paramRandomAccessFile.writeInt(0);
            paramRandomAccessFile.writeInt(0);
            paramRandomAccessFile.writeInt(0);
            if (headType.equals("SHP")) {
                if (geoLayerType == EGeoLayerType.POINT) {
                    paramRandomAccessFile.writeInt((dataCount * 14) + 50);
                    paramRandomAccessFile.write(IntToBytes(easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX));
                    paramRandomAccessFile.write(IntToBytes(1));
                } else if (geoLayerType == EGeoLayerType.POLYLINE) {
                    paramRandomAccessFile.writeInt(fileLen);
                    paramRandomAccessFile.write(IntToBytes(easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX));
                    paramRandomAccessFile.write(IntToBytes(3));
                } else if (geoLayerType == EGeoLayerType.POLYGON) {
                    paramRandomAccessFile.writeInt(fileLen);
                    paramRandomAccessFile.write(IntToBytes(easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX));
                    paramRandomAccessFile.write(IntToBytes(5));
                }
            } else if (headType.equals("SHX")) {
                paramRandomAccessFile.writeInt((dataCount * 4) + 50);
                paramRandomAccessFile.write(IntToBytes(easrNativeJni.VERIFY_TEST_LICENSE_OK_PREFIX));
                if (geoLayerType == EGeoLayerType.POINT) {
                    paramRandomAccessFile.write(IntToBytes(1));
                }
                if (geoLayerType == EGeoLayerType.POLYLINE) {
                    paramRandomAccessFile.write(IntToBytes(3));
                }
                if (geoLayerType == EGeoLayerType.POLYGON) {
                    paramRandomAccessFile.write(IntToBytes(5));
                }
            }
            double d1 = extend.getMinX();
            double d2 = extend.getMinY();
            double d3 = extend.getMaxX();
            double d4 = extend.getMaxY();
            boolean tmpIsWGS84 = false;
            if (this.m_ExportType.equals("WGS-84坐标")) {
                tmpIsWGS84 = true;
            }
            double[] tmpXYZ = convertCoordinate(d1, d4, 0.0d, _NeedConsiderConvert, tmpIsWGS84);
            double d12 = tmpXYZ[0];
            double d42 = tmpXYZ[1];
            double[] tmpXYZ2 = convertCoordinate(d3, d2, 0.0d, _NeedConsiderConvert, tmpIsWGS84);
            double d32 = tmpXYZ2[0];
            double d22 = tmpXYZ2[1];
            paramRandomAccessFile.write(DoubleToBytes(d12));
            paramRandomAccessFile.write(DoubleToBytes(d22));
            paramRandomAccessFile.write(DoubleToBytes(d32));
            paramRandomAccessFile.write(DoubleToBytes(d42));
            paramRandomAccessFile.write(DoubleToBytes(0.0d));
            paramRandomAccessFile.write(DoubleToBytes(0.0d));
            paramRandomAccessFile.write(DoubleToBytes(0.0d));
            paramRandomAccessFile.write(DoubleToBytes(0.0d));
        } catch (Exception e) {
        }
    }

    public boolean Export(DataSet pDataset, String filepath, List<String> sys_IDList) {
        if (this.hasOutputTipDialog) {
            Handler myHandler = this.progressDialog.myHandler;
            Message msg = myHandler.obtainMessage();
            msg.what = 5;
            msg.obj = new Object[]{"开始导出坐标系数据...", 0};
            myHandler.sendMessage(msg);
        }
        ToPrj(String.valueOf(filepath) + ".prj");
        return ToDbf(pDataset, new StringBuilder(String.valueOf(filepath)).append(".dbf").toString(), sys_IDList) && ToShp(pDataset, new StringBuilder(String.valueOf(filepath)).append(".shp").toString(), sys_IDList) && ToShx(pDataset, new StringBuilder(String.valueOf(filepath)).append(".shx").toString(), sys_IDList);
    }

    public byte[] ShortToBytes(int paramInt) {
        byte[] arrayOfByte1 = new byte[4];
        for (int i = 0; i < 4; i++) {
            arrayOfByte1[i] = (byte) (paramInt >> (24 - (i * 8)));
        }
        return new byte[]{arrayOfByte1[3], arrayOfByte1[2]};
    }

    public boolean ExportToShp(String filepath, List<AbstractGeometry> geoList, Envelope extend, boolean _NeedConsiderConvert) {
        File tmpFile = new File(filepath);
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
        if (geoList.size() == 0) {
            return false;
        }
        int tmpTotalLen = 50;
        int totalCount = geoList.size();
        EGeoLayerType tempGeoType = geoList.get(0).GetType();
        if (tempGeoType == EGeoLayerType.POINT) {
            tmpTotalLen = 50 + (totalCount * 14);
        } else if (tempGeoType != EGeoLayerType.NONE) {
            for (AbstractGeometry tmpGeo : geoList) {
                if (tmpGeo != null) {
                    tmpTotalLen += (tmpGeo.GetNumberOfParts() * 2) + 26 + (tmpGeo.GetNumberOfPoints() * 8);
                }
            }
        }
        Handler myHandler = null;
        if (this.hasOutputTipDialog) {
            myHandler = this.progressDialog.myHandler;
            Message msg = myHandler.obtainMessage();
            msg.what = 5;
            msg.obj = new Object[]{"开始导出几何数据...", 0};
            myHandler.sendMessage(msg);
        }
        try {
            RandomAccessFile localRandomAccessFile = new RandomAccessFile(tmpFile, "rw");
            WriteShpHeader(tempGeoType, extend, localRandomAccessFile, "SHP", totalCount, tmpTotalLen, _NeedConsiderConvert);
            if (totalCount <= 0) {
                localRandomAccessFile.close();
                return true;
            }
            int tid = 0;
            boolean needConvert = false;
            boolean isWGS84 = false;
            if (_NeedConsiderConvert) {
                needConvert = true;
            }
            if (this.m_ExportType.equals("WGS-84坐标")) {
                isWGS84 = true;
            }
            for (AbstractGeometry tmpGeo2 : geoList) {
                if (tmpGeo2 != null) {
                    localRandomAccessFile.writeInt(tid + 1);
                    tid++;
                    if (this.hasOutputTipDialog) {
                        Message msg2 = myHandler.obtainMessage();
                        msg2.what = 5;
                        msg2.obj = new Object[]{"开始导出几何数据 [" + tid + FileSelector_Dialog.sRoot + totalCount + "]", Integer.valueOf((tid * 100) / totalCount)};
                        myHandler.sendMessage(msg2);
                        if (this.progressDialog.isCancel) {
                            return false;
                        }
                    }
                    if (tempGeoType == EGeoLayerType.POINT) {
                        localRandomAccessFile.writeInt(10);
                        localRandomAccessFile.write(IntToBytes(1));
                        double[] tmpXYZ = convertCoordinate(tmpGeo2.GetAllCoordinateList().get(0), needConvert, isWGS84);
                        localRandomAccessFile.write(DoubleToBytes(tmpXYZ[0]));
                        localRandomAccessFile.write(DoubleToBytes(tmpXYZ[1]));
                    } else if (tempGeoType == EGeoLayerType.POLYLINE || tempGeoType == EGeoLayerType.POLYGON) {
                        localRandomAccessFile.writeInt((tmpGeo2.GetAllCoordinateList().size() * 8) + 22 + 2);
                        if (tempGeoType == EGeoLayerType.POLYLINE) {
                            localRandomAccessFile.write(IntToBytes(3));
                        } else if (tempGeoType == EGeoLayerType.POLYGON) {
                            localRandomAccessFile.write(IntToBytes(5));
                        }
                        Envelope localEnvelope = tmpGeo2.getEnvelope();
                        double d1 = localEnvelope.getMinX();
                        double d2 = localEnvelope.getMinY();
                        double d3 = localEnvelope.getMaxX();
                        double d4 = localEnvelope.getMaxY();
                        double[] tmpXYZ2 = convertCoordinate(d1, d2, 0.0d, needConvert, isWGS84);
                        double d12 = tmpXYZ2[0];
                        double d22 = tmpXYZ2[1];
                        double[] tmpXYZ3 = convertCoordinate(d3, d4, 0.0d, needConvert, isWGS84);
                        double d32 = tmpXYZ3[0];
                        double d42 = tmpXYZ3[1];
                        localRandomAccessFile.write(DoubleToBytes(d12));
                        localRandomAccessFile.write(DoubleToBytes(d22));
                        localRandomAccessFile.write(DoubleToBytes(d32));
                        localRandomAccessFile.write(DoubleToBytes(d42));
                        int tmpNumParts = tmpGeo2.GetNumberOfParts();
                        localRandomAccessFile.write(IntToBytes(tmpNumParts));
                        int tmpNumPoints = tmpGeo2.GetAllCoordinateList().size();
                        if (tempGeoType == EGeoLayerType.POLYLINE) {
                            localRandomAccessFile.write(IntToBytes(tmpGeo2.GetAllCoordinateList().size()));
                        }
                        if (tempGeoType == EGeoLayerType.POLYGON) {
                            localRandomAccessFile.write(IntToBytes(tmpNumPoints));
                        }
                        if (tmpNumParts == 1) {
                            localRandomAccessFile.write(IntToBytes(0));
                        } else {
                            for (Integer num : tmpGeo2.GetPartIndex()) {
                                localRandomAccessFile.write(IntToBytes(num.intValue()));
                            }
                        }
                        for (Coordinate tempCoord : tmpGeo2.GetAllCoordinateList()) {
                            double[] tmpXYZ4 = convertCoordinate(tempCoord, needConvert, isWGS84);
                            localRandomAccessFile.write(DoubleToBytes(tmpXYZ4[0]));
                            localRandomAccessFile.write(DoubleToBytes(tmpXYZ4[1]));
                        }
                    }
                } else if (this.hasOutputTipDialog) {
                    Message msg3 = myHandler.obtainMessage();
                    msg3.what = 5;
                    msg3.obj = new Object[]{"导出几何第[ " + tid + " ]个数据时错误.", Integer.valueOf((tid * 100) / totalCount)};
                    myHandler.sendMessage(msg3);
                }
            }
            if (this.hasOutputTipDialog) {
                Message msg4 = myHandler.obtainMessage();
                msg4.what = 5;
                msg4.obj = new Object[]{"导出几何数据完成.", 100};
                myHandler.sendMessage(msg4);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean ExportToDbf(String filepath, List<LayerField> fields, List<ArrayList> tmpArrayList03) {
        boolean result = false;
        if (this.CharacterCode == null || this.CharacterCode.trim().length() == 0) {
            this.CharacterCode = "GB2312";
        }
        try {
            File tmpFile = new File(filepath);
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            List<String> tmpArrayList01 = new ArrayList<>();
            List<String> tmpArrayList02 = new ArrayList<>();
            int i = 0;
            for (LayerField tmpField : fields) {
                String str1 = "";
                if (tmpField.GetFieldType() == EFieldType.STRING) {
                    str1 = "C";
                    i += tmpField.GetFieldSize();
                } else if (tmpField.GetFieldType() == EFieldType.DECIMAL) {
                    str1 = "F";
                    i += tmpField.GetFieldSize();
                } else if (tmpField.GetFieldType() == EFieldType.DATETIME) {
                    str1 = "C";
                    i += 20;
                } else if (tmpField.GetFieldType() == EFieldType.INTEGER) {
                    str1 = "N";
                    i += tmpField.GetFieldSize();
                } else if (tmpField.GetFieldType() == EFieldType.BOOLEAN) {
                    str1 = "C";
                    i += 5;
                }
                tmpArrayList01.add(String.valueOf(tmpField.GetFieldName()) + "," + str1 + "," + tmpField.GetFieldSize());
                tmpArrayList02.add(tmpField.GetDataFieldName());
            }
            Handler myHandler = null;
            if (this.hasOutputTipDialog) {
                myHandler = this.progressDialog.myHandler;
                Message msg = myHandler.obtainMessage();
                msg.what = 5;
                msg.obj = new Object[]{"开始读取属性数据...", 0};
                myHandler.sendMessage(msg);
            }
            int j = tmpArrayList01.size();
            if (this.hasOutputTipDialog) {
                Message msg2 = myHandler.obtainMessage();
                msg2.what = 5;
                msg2.obj = new Object[]{"开始导出属性数据...", 0};
                myHandler.sendMessage(msg2);
            }
            RandomAccessFile localRandomAccessFile = new RandomAccessFile(tmpFile, "rw");
            localRandomAccessFile.writeByte(3);
            localRandomAccessFile.writeByte(13);
            localRandomAccessFile.writeByte(7);
            localRandomAccessFile.writeByte(9);
            localRandomAccessFile.write(IntToBytes(tmpArrayList03.size()));
            localRandomAccessFile.write(ShortToBytes((j * 32) + 33));
            localRandomAccessFile.write(ShortToBytes(i + 1));
            localRandomAccessFile.write(new byte[2]);
            localRandomAccessFile.writeByte(0);
            localRandomAccessFile.writeByte(0);
            localRandomAccessFile.write(new byte[12]);
            localRandomAccessFile.writeByte(0);
            localRandomAccessFile.writeByte(0);
            localRandomAccessFile.write(new byte[2]);
            for (String str : tmpArrayList01) {
                String[] arrayOfString = str.split(",");
                byte[] arrayOfByte1 = arrayOfString[0].getBytes(this.CharacterCode);
                byte[] arrayOfByte2 = new byte[11];
                if (arrayOfByte1.length <= 0) {
                    break;
                }
                int tmpLen = 11;
                if (arrayOfByte1.length < 11) {
                    tmpLen = arrayOfByte1.length;
                }
                for (int tmpI = 0; tmpI < tmpLen; tmpI++) {
                    arrayOfByte2[tmpI] = arrayOfByte1[tmpI];
                }
                localRandomAccessFile.write(arrayOfByte2);
                localRandomAccessFile.write(arrayOfString[1].getBytes()[0]);
                localRandomAccessFile.write(new byte[4]);
                localRandomAccessFile.writeByte(Integer.parseInt(arrayOfString[2]));
                localRandomAccessFile.writeByte(0);
                localRandomAccessFile.write(new byte[2]);
                localRandomAccessFile.writeByte(0);
                localRandomAccessFile.write(new byte[11]);
            }
            localRandomAccessFile.writeByte(13);
            result = true;
            if (tmpArrayList03.size() > 0) {
                int tmpColsCount = tmpArrayList01.size();
                int tid = 0;
                int tmpOutputCount = tmpArrayList03.size();
                for (ArrayList tmpArrayList04 : tmpArrayList03) {
                    tid++;
                    if (this.hasOutputTipDialog) {
                        Message msg3 = myHandler.obtainMessage();
                        msg3.what = 5;
                        msg3.obj = new Object[]{"开始导出属性数据 [" + tid + FileSelector_Dialog.sRoot + tmpOutputCount + "]", Integer.valueOf((tid * 100) / tmpOutputCount)};
                        myHandler.sendMessage(msg3);
                        if (this.progressDialog.isCancel) {
                            return false;
                        }
                    }
                    localRandomAccessFile.writeByte(20);
                    for (int tmpJ = 0; tmpJ < tmpColsCount; tmpJ++) {
                        int tempLen2 = Integer.parseInt(tmpArrayList01.get(tmpJ).split(",")[2]);
                        byte[] arrayOfByte3 = String.valueOf(tmpArrayList04.get(tmpJ)).getBytes(Charset.forName(this.CharacterCode));
                        byte[] arrayOfByte4 = new byte[tempLen2];
                        int tmpLen2 = tempLen2;
                        if (arrayOfByte3.length < tmpLen2) {
                            tmpLen2 = arrayOfByte3.length;
                        }
                        for (int tmpI2 = 0; tmpI2 < tmpLen2; tmpI2++) {
                            arrayOfByte4[tmpI2] = arrayOfByte3[tmpI2];
                        }
                        localRandomAccessFile.write(arrayOfByte4);
                    }
                }
                localRandomAccessFile.close();
                if (this.hasOutputTipDialog) {
                    Message msg4 = myHandler.obtainMessage();
                    msg4.what = 5;
                    msg4.obj = new Object[]{"导出属性数据完成.", 100};
                    myHandler.sendMessage(msg4);
                }
                result = true;
            }
        } catch (Exception e) {
        }
        return result;
    }

    public boolean ExportToShx(String filepath, List<AbstractGeometry> geoList, Envelope extend, boolean _NeedConsiderConvert) {
        try {
            File tmpFile = new File(filepath);
            if (tmpFile.exists()) {
                tmpFile.delete();
            }
            int totalCount = geoList.size();
            if (totalCount == 0) {
                return false;
            }
            Handler myHandler = null;
            if (this.hasOutputTipDialog) {
                myHandler = this.progressDialog.myHandler;
                Message msg = myHandler.obtainMessage();
                msg.what = 5;
                msg.obj = new Object[]{"开始导出索引数据...", 0};
                myHandler.sendMessage(msg);
            }
            EGeoLayerType tmpGeoType = geoList.get(0).GetType();
            RandomAccessFile localRandomAccessFile = new RandomAccessFile(tmpFile, "rw");
            WriteShpHeader(tmpGeoType, extend, localRandomAccessFile, "SHX", totalCount, 0, _NeedConsiderConvert);
            if (tmpGeoType == EGeoLayerType.POINT) {
                for (int j = 0; j < totalCount; j++) {
                    localRandomAccessFile.writeInt((j * 14) + 50);
                    localRandomAccessFile.writeInt(10);
                }
            } else if (tmpGeoType != EGeoLayerType.NONE) {
                int i = 0;
                for (AbstractGeometry tmpGeo : geoList) {
                    if (tmpGeo != null) {
                        int tmpContentLength = (tmpGeo.GetNumberOfParts() * 2) + 26 + (tmpGeo.GetNumberOfPoints() * 8);
                        localRandomAccessFile.writeInt(i + 0 + 50);
                        localRandomAccessFile.writeInt(tmpContentLength);
                        i += tmpContentLength;
                    }
                }
                if (this.hasOutputTipDialog) {
                    Message msg2 = myHandler.obtainMessage();
                    msg2.what = 5;
                    msg2.obj = new Object[]{"导出索引数据完成.", 100};
                    myHandler.sendMessage(msg2);
                }
            }
            localRandomAccessFile.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static double[] convertCoordinate(Coordinate coordinate, boolean needConvert, boolean isWGS84) {
        return convertCoordinate(coordinate.getX(), coordinate.getY(), coordinate.getZ(), needConvert, isWGS84);
    }

    private static double[] convertCoordinate(double x, double y, double z, boolean needConvert, boolean isWGS84) {
        if (needConvert) {
            if (isWGS84) {
                Coordinate localCoordinate3 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(x, y, z, Coordinate_WGS1984.Instance());
                x = localCoordinate3.getGeoX();
                y = localCoordinate3.getGeoY();
            } else {
                Coordinate localCoordinate32 = PubVar.m_Workspace.GetCoorSystem().ConvertBLtoXYWithTranslate(x, y, z, Coordinate_WGS1984.Instance());
                x = localCoordinate32.getX();
                y = localCoordinate32.getY();
            }
        }
        return new double[]{x, y, z};
    }
}
