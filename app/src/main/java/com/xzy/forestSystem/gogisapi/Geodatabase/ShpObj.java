package  com.xzy.forestSystem.gogisapi.Geodatabase;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileOutputStream;

/* compiled from: Shapefile */
class ShpObj {
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
