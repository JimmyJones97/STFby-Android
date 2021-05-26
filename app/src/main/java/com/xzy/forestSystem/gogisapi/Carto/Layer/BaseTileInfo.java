package  com.xzy.forestSystem.gogisapi.Carto.Layer;

import android.graphics.Bitmap;
import java.io.Serializable;

public class BaseTileInfo implements Serializable {
    public int Col = 0;
    byte[] Data = null;
    public int HasDownloadTime = 0;
    public int Level = 0;
    public int Row = 0;
    public float ScreenXMax = 0.0f;
    public float ScreenXMin = 0.0f;
    public float ScreenYMax = 0.0f;
    public float ScreenYMin = 0.0f;
    public Bitmap TileBitmap = null;
    private String TileName = "";
    public XLayer TilesLayer = null;
    public String Url = "";

    public BaseTileInfo(int _col, int _row, int _level) {
        this.Col = _col;
        this.Row = _row;
        this.Level = _level;
        this.TileName = String.valueOf(_row) + "@" + _col + "@" + _level;
    }

    public BaseTileInfo(String tileName) {
        SetTileName(tileName);
    }

    public String GetTileName() {
        return this.TileName;
    }

    public void SetTileName(String tileName) {
        String[] tempStrs = tileName.split("@");
        if (tempStrs != null && tempStrs.length > 2) {
            this.Col = Integer.valueOf(tempStrs[0]).intValue();
            this.Row = Integer.valueOf(tempStrs[1]).intValue();
            this.Level = Integer.valueOf(tempStrs[2]).intValue();
        }
        this.TileName = tileName;
    }

    public float getScreenWidth() {
        return this.ScreenXMax - this.ScreenXMin;
    }

    public float getScreenHeight() {
        return this.ScreenYMax - this.ScreenYMin;
    }

    public void setData(byte[] data) {
        this.Data = data;
    }

    public byte[] getData() {
        return this.Data;
    }
}
