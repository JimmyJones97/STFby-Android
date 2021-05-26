package  com.xzy.forestSystem.gogisapi.Geodatabase;

import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class ExportCoordinateToTXT {
    public boolean Export(String saveFilePath, List<Coordinate> coordinates) {
        OutputStreamWriter osw = null;
        try {
            OutputStreamWriter osw2 = new OutputStreamWriter(new FileOutputStream(new File(saveFilePath)), "gbk");
            int tid = 0;
            try {
                for (Coordinate tmpCoord : coordinates) {
                    if (tmpCoord != null) {
                        tid++;
                        osw2.write(String.valueOf(String.valueOf(tid)) + "," + String.valueOf(tmpCoord.getX()) + "," + String.valueOf(tmpCoord.getY()) + "," + String.valueOf(tmpCoord.getZ()));
                        osw2.write("\r\n");
                    }
                }
                osw2.flush();
                osw2.close();
                return true;
            } catch (Exception e) {
                osw = osw2;
                try {
                    osw.close();
                    return true;
                } catch (Exception e2) {
                    return true;
                }
            }
        } catch (Exception e3) {
            try {
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }
}
