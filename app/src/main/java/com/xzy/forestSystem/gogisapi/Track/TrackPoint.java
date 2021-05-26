package  com.xzy.forestSystem.gogisapi.Track;

import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Point;
import  com.xzy.forestSystem.gogisapi.Geometry.Polyline;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrackPoint implements Cloneable {
    double _Accuracy = 0.0d;
    double _Direction = 0.0d;
    double _Elevation = 0.0d;
    boolean _IsFirst = false;
    double _Latitude = 0.0d;
    double _Longitude = 0.0d;
    double _Speed = 0.0d;
    long _Time = 0;
    String _TimeStr = "";

    public void SaveData(boolean isFirst) {
        StringBuilder tempSB = new StringBuilder();
        Date tmpDate = new Date(this._Time);
        tempSB.append("Insert Into T");
        tempSB.append(String.format("%02d", Integer.valueOf(tmpDate.getMonth() + 1)));
        tempSB.append(" (Time,TimeStr,JD,WD,GC,SPEED,Direction,ACCURACY,F1) Values (");
        tempSB.append(this._Time);
        tempSB.append(",'");
        tempSB.append(this._TimeStr);
        tempSB.append("',");
        tempSB.append(this._Longitude);
        tempSB.append(",");
        tempSB.append(this._Latitude);
        tempSB.append(",");
        tempSB.append(this._Elevation);
        tempSB.append(",");
        tempSB.append(this._Speed);
        tempSB.append(",");
        tempSB.append(this._Direction);
        tempSB.append(",");
        tempSB.append(this._Accuracy);
        tempSB.append(",'");
        if (isFirst) {
            tempSB.append(1);
        } else {
            tempSB.append(0);
        }
        tempSB.append("')");
        PubVar._PubCommand.m_ProjectDB.GetTrackManage().ExcuteSQL(tempSB.toString());
    }

    @Override // java.lang.Object
    public TrackPoint clone() throws CloneNotSupportedException {
        return (TrackPoint) super.clone();
    }

    public static List<AbstractGeometry> ConvertToPoints(List<TrackPoint> list) {
        List<AbstractGeometry> result = new ArrayList<>();
        for (TrackPoint tempPtn : list) {
            Point tmpPtn2 = new Point(new Coordinate(tempPtn._Longitude, tempPtn._Latitude, Double.valueOf(tempPtn._Elevation), tempPtn._Longitude, tempPtn._Latitude));
            tmpPtn2.CalEnvelope();
            tmpPtn2.SetTAGObject(tempPtn);
            result.add(tmpPtn2);
        }
        return result;
    }

    public static List<AbstractGeometry> ConvertToPolylines(List<TrackPoint> list) {
        List<AbstractGeometry> result = new ArrayList<>();
        Polyline tmpPoly = new Polyline();
        String tmpStartTime = "";
        String tmpEndTime = "";
        for (TrackPoint tempPtn : list) {
            if (tempPtn._IsFirst) {
                if (tmpPoly.GetAllCoordinateList().size() > 0) {
                    tmpPoly.SetTAGObject(String.valueOf(tmpStartTime) + ";" + tmpEndTime);
                    tmpPoly.CalEnvelope();
                    result.add(tmpPoly);
                }
                tmpPoly = new Polyline();
                tmpStartTime = tempPtn._TimeStr;
            }
            tmpPoly.GetAllCoordinateList().add(new Coordinate(tempPtn._Longitude, tempPtn._Latitude, Double.valueOf(tempPtn._Elevation), tempPtn._Longitude, tempPtn._Latitude));
            tmpEndTime = tempPtn._TimeStr;
        }
        if (tmpPoly.GetAllCoordinateList().size() > 0) {
            tmpPoly.SetTAGObject(String.valueOf(tmpStartTime) + ";" + tmpEndTime);
            result.add(tmpPoly);
        }
        return result;
    }
}
