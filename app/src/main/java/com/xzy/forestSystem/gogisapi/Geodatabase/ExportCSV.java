package  com.xzy.forestSystem.gogisapi.Geodatabase;

import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Polygon;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ExportCSV {
    String _DBPassword = "";
    String _ExportFieldNames = "";
    String _ExportFields = "";
    String _dbPath = "";
    String _keyField = "SYS_ID";
    String _keyFieldValues = "";
    String _outputPath = "";
    String _tableName = "";

    public ExportCSV() {
    }

    public ExportCSV(String outputPath, String dbPath, String tableName, String keyField, String keyFieldValues, String ExportFields, String ExportFieldNames, String databasePassowrd) {
        this._outputPath = outputPath;
        this._dbPath = dbPath;
        this._tableName = tableName;
        this._keyField = keyField;
        this._keyFieldValues = keyFieldValues;
        this._ExportFieldNames = ExportFieldNames;
        this._ExportFields = ExportFields;
        this._DBPassword = databasePassowrd;
    }

    public boolean Export() {
        boolean result = false;
        SQLiteDBHelper tempDatabase = new SQLiteDBHelper(this._dbPath, this._DBPassword);
        StringBuilder tempSB = new StringBuilder();
        tempSB.append("Select ");
        if (this._ExportFields == null || this._ExportFields.equals("")) {
            return false;
        }
        tempSB.append(this._ExportFields.replaceAll(",", "||','||"));
        tempSB.append(" AS MyValues");
        tempSB.append(" From ");
        tempSB.append(this._tableName);
        tempSB.append(" Where SYS_STATUS=0 And ");
        if (this._keyFieldValues == null || this._keyFieldValues.equals("")) {
            tempSB.append(" 1=1 ");
        } else {
            tempSB.append(this._keyField);
            tempSB.append(" in (" + this._keyFieldValues + ")");
        }
        SQLiteReader tempReader = tempDatabase.Query(tempSB.toString());
        if (tempReader != null) {
            OutputStreamWriter osw = null;
            try {
                OutputStreamWriter osw2 = new OutputStreamWriter(new FileOutputStream(new File(this._outputPath)), "gbk");
                try {
                    osw2.write(String.valueOf(this._ExportFieldNames) + "\r\n");
                    while (tempReader.Read()) {
                        String tempStr = tempReader.GetString(0);
                        if (tempStr == null) {
                            tempStr = "";
                        }
                        osw2.write(tempStr);
                        osw2.write("\r\n");
                    }
                    tempReader.Close();
                    osw2.flush();
                    osw2.close();
                } catch (Exception e) {
                    osw = osw2;
                    try {
                        osw.close();
                    } catch (Exception e2) {
                    }
                    result = true;
                    return result;
                }
            } catch (Exception e3) {
                try {
                    osw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                result = true;
                return result;
            }
            result = true;
        }
        return result;
    }

    public boolean ExportWithGeo(EGeoLayerType geoType, int GeoSptialType) throws IOException {
        boolean result = false;
        SQLiteDBHelper tempDatabase = new SQLiteDBHelper(this._dbPath, this._DBPassword);
        StringBuilder tempSB = new StringBuilder();
        tempSB.append("Select SYS_ID,SYS_GEO,");
        if (this._ExportFields == null || this._ExportFields.equals("")) {
            return false;
        }
        tempSB.append(this._ExportFields.replaceAll(",", "||','||"));
        tempSB.append(" AS MyValues");
        tempSB.append(" From ");
        tempSB.append(this._tableName);
        tempSB.append(" Where SYS_STATUS=0 And ");
        if (this._keyFieldValues == null || this._keyFieldValues.equals("")) {
            tempSB.append(" 1=1 ");
        } else {
            tempSB.append(this._keyField);
            tempSB.append(" in (" + this._keyFieldValues + ")");
        }
        SQLiteReader tempReader = tempDatabase.Query(tempSB.toString());
        if (tempReader != null) {
            OutputStreamWriter osw = null;
            try {
                OutputStreamWriter osw2 = new OutputStreamWriter(new FileOutputStream(new File(this._outputPath)), "gbk");
                try {
                    if (geoType == EGeoLayerType.POINT) {
                        osw2.write("ID,X,Y,Z," + this._ExportFieldNames + "\r\n");
                    } else if (geoType == EGeoLayerType.POLYLINE) {
                        osw2.write("ID,坐标数据,长度," + this._ExportFieldNames + "\r\n");
                    } else if (geoType == EGeoLayerType.POLYGON) {
                        osw2.write("ID,坐标数据,面积," + this._ExportFieldNames + "\r\n");
                    }
                    while (tempReader.Read()) {
                        String tmpSYSIDString = tempReader.GetString(0);
                        byte[] arrayOfByte = tempReader.GetBlob(1);
                        if (arrayOfByte != null) {
                            AbstractGeometry localGeometry = Common.ConvertBytesToGeometry(arrayOfByte, geoType, true, GeoSptialType);
                            if (!localGeometry.GetAllCoordinateList().get(0).IsValid()) {
                                String tmpLineString = tmpSYSIDString;
                                String tempStr = tempReader.GetString(2);
                                if (tempStr == null) {
                                    tempStr = "";
                                }
                                if (geoType == EGeoLayerType.POINT) {
                                    tmpLineString = String.valueOf(tmpLineString) + "," + localGeometry.ConvertCoordString(",");
                                } else if (geoType == EGeoLayerType.POLYLINE) {
                                    tmpLineString = String.valueOf(tmpLineString) + "," + localGeometry.ConvertCoordString(" ") + "," + String.format("%f", Double.valueOf(((Polyline) localGeometry).getLength(false)));
                                } else if (geoType == EGeoLayerType.POLYGON) {
                                    tmpLineString = String.valueOf(tmpLineString) + "," + localGeometry.ConvertCoordString(" ") + "," + String.format("%f", Double.valueOf(((Polygon) localGeometry).getArea(false)));
                                }
                                osw2.write(String.valueOf(tmpLineString) + "," + tempStr);
                                osw2.write("\r\n");
                            }
                        }
                    }
                    tempReader.Close();
                    osw2.flush();
                    osw2.close();
                } catch (Exception e) {
                    osw = osw2;
                    try {
                        osw.close();
                    } catch (Exception e2) {
                    }
                    result = true;
                    return result;
                }
            } catch (Exception e3) {
                osw.close();
                result = true;
                return result;
            }
            result = true;
        }
        return result;
    }
}
