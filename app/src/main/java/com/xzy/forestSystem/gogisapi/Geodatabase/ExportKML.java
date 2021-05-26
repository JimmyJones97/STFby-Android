package  com.xzy.forestSystem.gogisapi.Geodatabase;

import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteReader;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExportKML {
    private static Exception e;
    String _DBPassword = "";
    String _DataFolder = "";
    String _ExportFieldNames = "";
    String _ExportFields = "";
    String _LayerName = "";
    String _OutputFolder = "";
    String _dbPath = "";
    String _keyField = "SYS_ID";
    String _keyFieldValues = "";
    String _savePath = "";
    String _tableName = "";
    public ExportKML(String outputPath, String dbPath, String layerName, String tableName, String keyField, String keyFieldValues, String ExportFields, String databasePassowrd, String dataFolder, String outputFolder) {
        this._savePath = outputPath;
        this._dbPath = dbPath;
        this._LayerName = layerName;
        this._tableName = tableName;
        this._keyField = keyField;
        this._keyFieldValues = keyFieldValues;
        this._ExportFields = ExportFields;
        this._DBPassword = databasePassowrd;
        this._DataFolder = dataFolder;
        this._OutputFolder = outputFolder;
    }

    public boolean Export(EGeoLayerType geoType) throws IOException {
        String[] tmpStrs01;
        String[] tmpStrs012;
        String[] tmpStrs013;
        boolean result = false;
        SQLiteDBHelper tempDatabase = new SQLiteDBHelper(this._dbPath, this._DBPassword);
        StringBuilder tempSB = new StringBuilder();
        tempSB.append("Select SYS_ID,SYS_GEO,SYS_LABEL,SYS_PHOTO,SYS_BZ1,");
        if (this._ExportFields == null || this._ExportFields.equals("")) {
            return false;
        }
        tempSB.append(this._ExportFields.replaceAll(",", "||','||"));
        tempSB.append(" AS MyValues");
        tempSB.append(" From ");
        tempSB.append(this._tableName);
        tempSB.append(" Where ");
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
                List<String> tmpSavePhotoList = new ArrayList<>();
                OutputStreamWriter osw2 = new OutputStreamWriter(new FileOutputStream(new File(this._savePath)), "utf-8");
                try {
                    osw2.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    osw2.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">");
                    osw2.write("<Document>");
                    osw2.write("<name>" + this._LayerName + ".kml</name>");
                    osw2.write("<StyleMap id=\"m_ylw-pushpin0\">");
                    osw2.write("<Pair>");
                    osw2.write("<key>normal</key>");
                    osw2.write("<styleUrl>#s_ylw-pushpin0</styleUrl>");
                    osw2.write("</Pair>");
                    osw2.write("<Pair>");
                    osw2.write("<key>highlight</key>");
                    osw2.write("<styleUrl>#s_ylw-pushpin_hl</styleUrl>");
                    osw2.write("</Pair>");
                    osw2.write("</StyleMap>");
                    osw2.write("<Style id=\"s_ylw-pushpin_hl\">");
                    osw2.write("<IconStyle>");
                    osw2.write("<scale>1.3</scale>");
                    osw2.write("<Icon>");
                    osw2.write("<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>");
                    osw2.write("</Icon>");
                    osw2.write("<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>");
                    osw2.write("</IconStyle>");
                    osw2.write("</Style>");
                    osw2.write("<Style id=\"s_ylw-pushpin0\">");
                    osw2.write("<IconStyle>");
                    osw2.write("<scale>1.1</scale>");
                    osw2.write("<Icon>");
                    osw2.write("<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>");
                    osw2.write("</Icon>");
                    osw2.write("<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>");
                    osw2.write("</IconStyle>");
                    osw2.write("</Style>\r\n");
                    while (tempReader.Read()) {
                        String tmpSYSIDString = tempReader.GetString(0);
                        byte[] arrayOfByte = tempReader.GetBlob(1);
                        if (arrayOfByte != null) {
                            AbstractGeometry localGeometry = Common.ConvertBytesToGeometry(arrayOfByte, geoType, true, 0);
                            if (!localGeometry.GetAllCoordinateList().get(0).IsValid()) {
                                if (tempReader.GetString(2) == null) {
                                }
                                String tmpLabel = tempReader.GetString(2);
                                String tmpPhoto = tempReader.GetString(3);
                                tempReader.GetString(4);
                                String tmpFidValues = tempReader.GetString(5);
                                if (geoType == EGeoLayerType.POINT) {
                                    if (tmpLabel == null || tmpLabel.length() == 0) {
                                        tmpLabel = "点" + tmpSYSIDString;
                                    }
                                    osw2.write("<Placemark>");
                                    osw2.write("<name>" + tmpLabel + "</name>");
                                    StringBuilder tmpDescription = new StringBuilder();
                                    if (tmpFidValues != null) {
                                        tmpDescription.append(tmpFidValues);
                                    }
                                    if (tmpPhoto != null && tmpPhoto.length() > 0 && (tmpStrs01 = tmpPhoto.split(";")) != null && tmpStrs01.length > 0) {
                                        int length = tmpStrs01.length;
                                        for (int i = 0; i < length; i++) {
                                            String tmpStr01String = tmpStrs01[i];
                                            if (tmpStr01String.contains(".jpg")) {
                                                tmpSavePhotoList.add(tmpStr01String);
                                                tmpDescription.append("<img style=\"max-width:500px;\" src=\"Photo/" + tmpStr01String + "\">");
                                            }
                                        }
                                    }
                                    osw2.write("<description><![CDATA[" + tmpDescription.toString() + "]]></description>");
                                    Coordinate tmpCoordinate02 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(localGeometry.GetAllCoordinateList().get(0), PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                                    osw2.write("<LookAt>");
                                    osw2.write("<longitude>" + String.format("%f", Double.valueOf(tmpCoordinate02.getGeoX())) + "</longitude>");
                                    osw2.write("<latitude>" + String.format("%f", Double.valueOf(tmpCoordinate02.getGeoY())) + "</latitude>");
                                    osw2.write("<altitude>0</altitude>");
                                    osw2.write("<heading>0</heading>");
                                    osw2.write("<tilt>0</tilt>");
                                    osw2.write("<range>1000</range>");
                                    osw2.write("<gx:altitudeMode>relativeToSeaFloor</gx:altitudeMode>");
                                    osw2.write("</LookAt>");
                                    osw2.write("<styleUrl>#m_ylw-pushpin0</styleUrl>");
                                    osw2.write("<Point>");
                                    osw2.write("<gx:drawOrder>1</gx:drawOrder>");
                                    osw2.write("<coordinates>" + String.format("%f", Double.valueOf(tmpCoordinate02.getGeoX())) + "," + String.format("%f", Double.valueOf(tmpCoordinate02.getGeoY())) + "," + String.format("%f", Double.valueOf(tmpCoordinate02.getZ())) + "</coordinates>");
                                    osw2.write("</Point>");
                                    osw2.write("</Placemark>\r\n");
                                } else if (geoType == EGeoLayerType.POLYLINE) {
                                    if (tmpLabel == null || tmpLabel.length() == 0) {
                                        tmpLabel = "线" + tmpSYSIDString;
                                    }
                                    osw2.write("<Placemark>");
                                    osw2.write("<name>" + tmpLabel + "</name>");
                                    StringBuilder tmpDescription2 = new StringBuilder();
                                    if (tmpFidValues != null) {
                                        tmpDescription2.append(tmpFidValues);
                                    }
                                    if (tmpPhoto != null && tmpPhoto.length() > 0 && (tmpStrs012 = tmpPhoto.split(";")) != null && tmpStrs012.length > 0) {
                                        int length2 = tmpStrs012.length;
                                        for (int i2 = 0; i2 < length2; i2++) {
                                            String tmpStr01String2 = tmpStrs012[i2];
                                            if (tmpStr01String2.contains(".jpg")) {
                                                tmpSavePhotoList.add(tmpStr01String2);
                                                tmpDescription2.append("<img style=\"max-width:500px;\" src=\"Photo/" + tmpStr01String2 + "\">");
                                            }
                                        }
                                    }
                                    osw2.write("<description><![CDATA[" + tmpDescription2.toString() + "]]></description>");
                                    osw2.write("<LineString>");
                                    osw2.write("<tessellate>1</tessellate>");
                                    osw2.write("<coordinates>");
                                    HashMap<String, Coordinate> tmpExtraHash = null;
                                    Iterator<Coordinate> tmpIterator = localGeometry.GetAllCoordinateList().iterator();
                                    if (tmpIterator != null) {
                                        StringBuilder tmpCoordStringBuilder = new StringBuilder();
                                        while (tmpIterator.hasNext()) {
                                            Coordinate tmpCoordinate022 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tmpIterator.next(), PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                                            tmpCoordStringBuilder.append(String.format("%f", Double.valueOf(tmpCoordinate022.getGeoX())));
                                            tmpCoordStringBuilder.append(",");
                                            tmpCoordStringBuilder.append(String.format("%f", Double.valueOf(tmpCoordinate022.getGeoY())));
                                            tmpCoordStringBuilder.append(",");
                                            tmpCoordStringBuilder.append(String.format("%f", Double.valueOf(tmpCoordinate022.getZ())));
                                            tmpCoordStringBuilder.append(" ");
                                        }
                                        osw2.write(tmpCoordStringBuilder.toString());
                                    }
                                    osw2.write("</coordinates>");
                                    osw2.write("</LineString>");
                                    osw2.write("</Placemark>\r\n");
                                    if (0 != 0 && tmpExtraHash.size() > 0) {
                                        for (Map.Entry<String, Coordinate> tmpEntry : tmpExtraHash.entrySet()) {
                                            String tmpStr03String = createPointString(tmpEntry.getKey(), tmpEntry.getValue());
                                            if (tmpStr03String.length() > 0) {
                                                osw2.write(String.valueOf(tmpStr03String) + "\r\n");
                                            }
                                        }
                                    }
                                } else if (geoType == EGeoLayerType.POLYGON) {
                                    if (tmpLabel == null || tmpLabel.length() == 0) {
                                        tmpLabel = "面" + tmpSYSIDString;
                                    }
                                    osw2.write("<Placemark>");
                                    osw2.write("<name>" + tmpLabel + "</name>");
                                    StringBuilder tmpDescription3 = new StringBuilder();
                                    if (tmpFidValues != null) {
                                        tmpDescription3.append(tmpFidValues);
                                    }
                                    if (tmpPhoto != null && tmpPhoto.length() > 0 && (tmpStrs013 = tmpPhoto.split(";")) != null && tmpStrs013.length > 0) {
                                        int length3 = tmpStrs013.length;
                                        for (int i3 = 0; i3 < length3; i3++) {
                                            String tmpStr01String3 = tmpStrs013[i3];
                                            if (tmpStr01String3.contains(".jpg")) {
                                                tmpSavePhotoList.add(tmpStr01String3);
                                                tmpDescription3.append("<img style=\"max-width:500px;\" src=\"Photo/" + tmpStr01String3 + "\">");
                                            }
                                        }
                                    }
                                    osw2.write("<description><![CDATA[" + tmpDescription3.toString() + "]]></description>");
                                    osw2.write("<Polygon>");
                                    osw2.write("<tessellate>1</tessellate>");
                                    osw2.write("<outerBoundaryIs>");
                                    osw2.write("<LinearRing>");
                                    osw2.write("<coordinates>");
                                    StringBuilder tmpCoordStringBuilder2 = new StringBuilder();
                                    for (Coordinate tmpCoordinate : localGeometry.GetAllCoordinateList()) {
                                        Coordinate tmpCoordinate023 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(tmpCoordinate, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
                                        tmpCoordStringBuilder2.append(String.format("%f", Double.valueOf(tmpCoordinate023.getGeoX())));
                                        tmpCoordStringBuilder2.append(",");
                                        tmpCoordStringBuilder2.append(String.format("%f", Double.valueOf(tmpCoordinate023.getGeoY())));
                                        tmpCoordStringBuilder2.append(",");
                                        tmpCoordStringBuilder2.append(String.format("%f", Double.valueOf(tmpCoordinate023.getZ())));
                                        tmpCoordStringBuilder2.append(" ");
                                    }
                                    osw2.write(tmpCoordStringBuilder2.toString());
                                    osw2.write("</coordinates>");
                                    osw2.write("</LinearRing>");
                                    osw2.write("</outerBoundaryIs>");
                                    osw2.write("</Polygon>");
                                    osw2.write("</Placemark>\r\n");
                                }
                            }
                        }
                    }
                    tempReader.Close();
                    osw2.write("</Document>");
                    osw2.write("</kml>");
                    osw2.flush();
                    osw2.close();
                    if (tmpSavePhotoList.size() > 0) {
                        String tempPhotoFolder = String.valueOf(this._DataFolder) + "/Photo/";
                        String tempOutputPhotoFolder = String.valueOf(this._OutputFolder) + "/Photo/";
                        if (!Common.ExistFolder(tempOutputPhotoFolder)) {
                            new File(tempOutputPhotoFolder).mkdir();
                        }
                        for (String tmpFileName : tmpSavePhotoList) {
                            Common.CopyFile(String.valueOf(tempPhotoFolder) + tmpFileName, String.valueOf(tempOutputPhotoFolder) + tmpFileName);
                        }
                    }
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

    private static String createPointString(String pointName, Coordinate coordinate) {
        StringBuilder tmpStringBuilder = new StringBuilder();
        tmpStringBuilder.append("<Placemark>");
        tmpStringBuilder.append("<name>" + pointName + "</name>");
        Coordinate tmpCoordinate02 = PubVar.m_Workspace.GetCoorSystem().ConvertXYtoBLWithTranslate(coordinate, PubVar._PubCommand.m_GpsLocation.GetCoordinateSystem());
        tmpStringBuilder.append("<LookAt>");
        tmpStringBuilder.append("<longitude>" + String.format("%f", Double.valueOf(tmpCoordinate02.getGeoX())) + "</longitude>");
        tmpStringBuilder.append("<latitude>" + String.format("%f", Double.valueOf(tmpCoordinate02.getGeoY())) + "</latitude>");
        tmpStringBuilder.append("<altitude>0</altitude>");
        tmpStringBuilder.append("<heading>0</heading>");
        tmpStringBuilder.append("<tilt>0</tilt>");
        tmpStringBuilder.append("<range>1000</range>");
        tmpStringBuilder.append("<gx:altitudeMode>relativeToSeaFloor</gx:altitudeMode>");
        tmpStringBuilder.append("</LookAt>");
        tmpStringBuilder.append("<styleUrl>#m_ylw-pushpin0</styleUrl>");
        tmpStringBuilder.append("<Point>");
        tmpStringBuilder.append("<gx:drawOrder>1</gx:drawOrder>");
        tmpStringBuilder.append("<coordinates>" + String.format("%f", Double.valueOf(tmpCoordinate02.getGeoX())) + "," + String.format("%f", Double.valueOf(tmpCoordinate02.getGeoY())) + "," + String.format("%f", Double.valueOf(tmpCoordinate02.getZ())) + "</coordinates>");
        tmpStringBuilder.append("</Point>");
        tmpStringBuilder.append("</Placemark>\r\n");
        return tmpStringBuilder.toString();
    }

    public static boolean ExportKML(String savePath, String title, AbstractGeometry geometry) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File(savePath)), "utf-8");
            try {
                osw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                osw.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">");
                osw.write("<Document>");
                osw.write("<name>" + title + ".kml</name>");
                osw.write("<StyleMap id=\"m_ylw-pushpin0\">");
                osw.write("<Pair>");
                osw.write("<key>normal</key>");
                osw.write("<styleUrl>#s_ylw-pushpin0</styleUrl>");
                osw.write("</Pair>");
                osw.write("<Pair>");
                osw.write("<key>highlight</key>");
                osw.write("<styleUrl>#s_ylw-pushpin_hl</styleUrl>");
                osw.write("</Pair>");
                osw.write("</StyleMap>");
                osw.write("<Style id=\"s_ylw-pushpin_hl\">");
                osw.write("<IconStyle>");
                osw.write("<scale>1.3</scale>");
                osw.write("<Icon>");
                osw.write("<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>");
                osw.write("</Icon>");
                osw.write("<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>");
                osw.write("</IconStyle>");
                osw.write("</Style>");
                osw.write("<Style id=\"s_ylw-pushpin0\">");
                osw.write("<IconStyle>");
                osw.write("<scale>1.1</scale>");
                osw.write("<Icon>");
                osw.write("<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>");
                osw.write("</Icon>");
                osw.write("<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>");
                osw.write("</IconStyle>");
                osw.write("</Style>\r\n");
                if (geometry.GetType() == EGeoLayerType.POINT) {
                    osw.write("<Placemark>");
                    osw.write("<name>" + ("点[" + title + "]") + "</name>");
                    osw.write("<description><![CDATA[" + new StringBuilder().toString() + "]]></description>");
                    Coordinate tmpCoordinate = geometry.GetAllCoordinateList().get(0);
                    osw.write("<LookAt>");
                    osw.write("<longitude>" + String.format("%f", Double.valueOf(tmpCoordinate.getGeoX())) + "</longitude>");
                    osw.write("<latitude>" + String.format("%f", Double.valueOf(tmpCoordinate.getGeoY())) + "</latitude>");
                    osw.write("<altitude>0</altitude>");
                    osw.write("<heading>0</heading>");
                    osw.write("<tilt>0</tilt>");
                    osw.write("<range>1000</range>");
                    osw.write("<gx:altitudeMode>relativeToSeaFloor</gx:altitudeMode>");
                    osw.write("</LookAt>");
                    osw.write("<styleUrl>#m_ylw-pushpin0</styleUrl>");
                    osw.write("<Point>");
                    osw.write("<gx:drawOrder>1</gx:drawOrder>");
                    osw.write("<coordinates>" + String.format("%f", Double.valueOf(tmpCoordinate.getGeoX())) + "," + String.format("%f", Double.valueOf(tmpCoordinate.getGeoY())) + "," + String.format("%f", Double.valueOf(tmpCoordinate.getZ())) + "</coordinates>");
                    osw.write("</Point>");
                    osw.write("</Placemark>\r\n");
                } else if (geometry.GetType() == EGeoLayerType.POLYLINE) {
                    osw.write("<Placemark>");
                    osw.write("<name>" + ("线[" + title + "]") + "</name>");
                    osw.write("<description><![CDATA[" + new StringBuilder().toString() + "]]></description>");
                    osw.write("<LineString>");
                    osw.write("<tessellate>1</tessellate>");
                    osw.write("<coordinates>");
                    HashMap<String, Coordinate> tmpExtraHash = null;
                    StringBuilder tmpCoordStringBuilder = new StringBuilder();
                    for (Coordinate tmpCoordinate2 : geometry.GetAllCoordinateList()) {
                        tmpCoordStringBuilder.append(String.format("%f", Double.valueOf(tmpCoordinate2.getGeoX())));
                        tmpCoordStringBuilder.append(",");
                        tmpCoordStringBuilder.append(String.format("%f", Double.valueOf(tmpCoordinate2.getGeoY())));
                        tmpCoordStringBuilder.append(",");
                        tmpCoordStringBuilder.append(String.format("%f", Double.valueOf(tmpCoordinate2.getZ())));
                        tmpCoordStringBuilder.append(" ");
                    }
                    osw.write(tmpCoordStringBuilder.toString());
                    osw.write("</coordinates>");
                    osw.write("</LineString>");
                    osw.write("</Placemark>\r\n");
                    if (0 != 0 && tmpExtraHash.size() > 0) {
                        for (Map.Entry<String, Coordinate> tmpEntry : tmpExtraHash.entrySet()) {
                            String tmpStr03String = createPointString(tmpEntry.getKey(), tmpEntry.getValue());
                            if (tmpStr03String.length() > 0) {
                                osw.write(String.valueOf(tmpStr03String) + "\r\n");
                            }
                        }
                    }
                } else if (geometry.GetType() == EGeoLayerType.POLYGON) {
                    osw.write("<Placemark>");
                    osw.write("<name>" + ("面[" + title + "]") + "</name>");
                    osw.write("<description><![CDATA[" + new StringBuilder().toString() + "]]></description>");
                    osw.write("<Polygon>");
                    osw.write("<tessellate>1</tessellate>");
                    osw.write("<outerBoundaryIs>");
                    osw.write("<LinearRing>");
                    osw.write("<coordinates>");
                    StringBuilder tmpCoordStringBuilder2 = new StringBuilder();
                    for (Coordinate tmpCoordinate3 : geometry.GetAllCoordinateList()) {
                        tmpCoordStringBuilder2.append(String.format("%f", Double.valueOf(tmpCoordinate3.getGeoX())));
                        tmpCoordStringBuilder2.append(",");
                        tmpCoordStringBuilder2.append(String.format("%f", Double.valueOf(tmpCoordinate3.getGeoY())));
                        tmpCoordStringBuilder2.append(",");
                        tmpCoordStringBuilder2.append(String.format("%f", Double.valueOf(tmpCoordinate3.getZ())));
                        tmpCoordStringBuilder2.append(" ");
                    }
                    osw.write(tmpCoordStringBuilder2.toString());
                    osw.write("</coordinates>");
                    osw.write("</LinearRing>");
                    osw.write("</outerBoundaryIs>");
                    osw.write("</Polygon>");
                    osw.write("</Placemark>\r\n");
                }
                osw.write("</Document>");
                osw.write("</kml>");
                osw.flush();
                osw.close();
                return true;
            } catch (Exception e) {
                e = e;
                Common.ShowToast("错误:" + e.getLocalizedMessage());
                return true;
            }
        } catch (Exception e2) {
            e = e2;
            Common.ShowToast("错误:" + e.getLocalizedMessage());
            return true;
        }
    }

    public static boolean ExportKMLFile(String savePath, String title, List<AbstractGeometry> geometryList) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(new File(savePath)), "utf-8");
            try {
                osw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                osw.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">");
                osw.write("<Document>");
                osw.write("<name>" + title + ".kml</name>");
                osw.write("<StyleMap id=\"m_ylw-pushpin0\">");
                osw.write("<Pair>");
                osw.write("<key>normal</key>");
                osw.write("<styleUrl>#s_ylw-pushpin0</styleUrl>");
                osw.write("</Pair>");
                osw.write("<Pair>");
                osw.write("<key>highlight</key>");
                osw.write("<styleUrl>#s_ylw-pushpin_hl</styleUrl>");
                osw.write("</Pair>");
                osw.write("</StyleMap>");
                osw.write("<Style id=\"s_ylw-pushpin_hl\">");
                osw.write("<IconStyle>");
                osw.write("<scale>1.3</scale>");
                osw.write("<Icon>");
                osw.write("<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>");
                osw.write("</Icon>");
                osw.write("<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>");
                osw.write("</IconStyle>");
                osw.write("</Style>");
                osw.write("<Style id=\"s_ylw-pushpin0\">");
                osw.write("<IconStyle>");
                osw.write("<scale>1.1</scale>");
                osw.write("<Icon>");
                osw.write("<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>");
                osw.write("</Icon>");
                osw.write("<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>");
                osw.write("</IconStyle>");
                osw.write("</Style>\r\n");
                if (geometryList != null && geometryList.size() > 0) {
                    for (AbstractGeometry geometry : geometryList) {
                        if (geometry != null) {
                            if (geometry.GetType() == EGeoLayerType.POINT) {
                                osw.write("<Placemark>");
                                osw.write("<name>" + ("点[" + title + "]") + "</name>");
                                osw.write("<description><![CDATA[" + new StringBuilder().toString() + "]]></description>");
                                Coordinate tmpCoordinate = geometry.GetAllCoordinateList().get(0);
                                osw.write("<LookAt>");
                                osw.write("<longitude>" + String.format("%f", Double.valueOf(tmpCoordinate.getGeoX())) + "</longitude>");
                                osw.write("<latitude>" + String.format("%f", Double.valueOf(tmpCoordinate.getGeoY())) + "</latitude>");
                                osw.write("<altitude>0</altitude>");
                                osw.write("<heading>0</heading>");
                                osw.write("<tilt>0</tilt>");
                                osw.write("<range>1000</range>");
                                osw.write("<gx:altitudeMode>relativeToSeaFloor</gx:altitudeMode>");
                                osw.write("</LookAt>");
                                osw.write("<styleUrl>#m_ylw-pushpin0</styleUrl>");
                                osw.write("<Point>");
                                osw.write("<gx:drawOrder>1</gx:drawOrder>");
                                osw.write("<coordinates>" + String.format("%f", Double.valueOf(tmpCoordinate.getGeoX())) + "," + String.format("%f", Double.valueOf(tmpCoordinate.getGeoY())) + "," + String.format("%f", Double.valueOf(tmpCoordinate.getZ())) + "</coordinates>");
                                osw.write("</Point>");
                                osw.write("</Placemark>\r\n");
                            } else if (geometry.GetType() == EGeoLayerType.POLYLINE) {
                                osw.write("<Placemark>");
                                osw.write("<name>" + ("线[" + title + "]") + "</name>");
                                osw.write("<description><![CDATA[" + new StringBuilder().toString() + "]]></description>");
                                osw.write("<LineString>");
                                osw.write("<tessellate>1</tessellate>");
                                osw.write("<coordinates>");
                                HashMap<String, Coordinate> tmpExtraHash = null;
                                StringBuilder tmpCoordStringBuilder = new StringBuilder();
                                for (Coordinate tmpCoordinate2 : geometry.GetAllCoordinateList()) {
                                    tmpCoordStringBuilder.append(String.format("%f", Double.valueOf(tmpCoordinate2.getGeoX())));
                                    tmpCoordStringBuilder.append(",");
                                    tmpCoordStringBuilder.append(String.format("%f", Double.valueOf(tmpCoordinate2.getGeoY())));
                                    tmpCoordStringBuilder.append(",");
                                    tmpCoordStringBuilder.append(String.format("%f", Double.valueOf(tmpCoordinate2.getZ())));
                                    tmpCoordStringBuilder.append(" ");
                                }
                                osw.write(tmpCoordStringBuilder.toString());
                                osw.write("</coordinates>");
                                osw.write("</LineString>");
                                osw.write("</Placemark>\r\n");
                                if (0 != 0 && tmpExtraHash.size() > 0) {
                                    for (Map.Entry<String, Coordinate> tmpEntry : tmpExtraHash.entrySet()) {
                                        String tmpStr03String = createPointString(tmpEntry.getKey(), tmpEntry.getValue());
                                        if (tmpStr03String.length() > 0) {
                                            osw.write(String.valueOf(tmpStr03String) + "\r\n");
                                        }
                                    }
                                }
                            } else if (geometry.GetType() == EGeoLayerType.POLYGON) {
                                osw.write("<Placemark>");
                                osw.write("<name>" + ("面[" + title + "]") + "</name>");
                                osw.write("<description><![CDATA[" + new StringBuilder().toString() + "]]></description>");
                                osw.write("<Polygon>");
                                osw.write("<tessellate>1</tessellate>");
                                osw.write("<outerBoundaryIs>");
                                osw.write("<LinearRing>");
                                osw.write("<coordinates>");
                                StringBuilder tmpCoordStringBuilder2 = new StringBuilder();
                                for (Coordinate tmpCoordinate3 : geometry.GetAllCoordinateList()) {
                                    tmpCoordStringBuilder2.append(String.format("%f", Double.valueOf(tmpCoordinate3.getGeoX())));
                                    tmpCoordStringBuilder2.append(",");
                                    tmpCoordStringBuilder2.append(String.format("%f", Double.valueOf(tmpCoordinate3.getGeoY())));
                                    tmpCoordStringBuilder2.append(",");
                                    tmpCoordStringBuilder2.append(String.format("%f", Double.valueOf(tmpCoordinate3.getZ())));
                                    tmpCoordStringBuilder2.append(" ");
                                }
                                osw.write(tmpCoordStringBuilder2.toString());
                                osw.write("</coordinates>");
                                osw.write("</LinearRing>");
                                osw.write("</outerBoundaryIs>");
                                osw.write("</Polygon>");
                                osw.write("</Placemark>\r\n");
                            }
                        }
                    }
                }
                osw.write("</Document>");
                osw.write("</kml>");
                osw.flush();
                osw.close();
                return true;
            } catch (Exception e) {
                e = e;
                Common.ShowToast("错误:" + e.getLocalizedMessage());
                return true;
            }
        } catch (Exception e2) {
            e = e2;
            Common.ShowToast("错误:" + e.getLocalizedMessage());
            return true;
        }
    }
}
