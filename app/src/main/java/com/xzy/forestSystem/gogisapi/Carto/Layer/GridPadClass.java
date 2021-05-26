package  com.xzy.forestSystem.gogisapi.Carto.Layer;

import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;
import  com.xzy.forestSystem.gogisapi.Geometry.Envelope;

class GridPadClass {
    public Envelope Extend;
    private double MaxX;
    private double MaxY;
    private double MinX;
    private double MinY;
    public int TileSize = 256;

    public String CalGridPosition(double paramDouble, Coordinate tmpCoord) {
        double d = paramDouble * ((double) this.TileSize);
        int i = ((int) ((this.MaxX - this.MinX) / d)) + 1;
        int j = ((int) ((this.MaxY - this.MinY) / d)) + 1;
        int k = (int) ((tmpCoord.getX() - this.MinX) / d);
        int l = (int) ((this.MaxY - tmpCoord.getY()) / d);
        if (k < 0) {
            k = 0;
        }
        if (l < 0) {
            l = 0;
        }
        if (k > i) {
            k = i;
        }
        if (l > j) {
            l = j;
        }
        return String.valueOf(k) + "," + l;
    }

    public boolean InCurrentView(Envelope paramEnvelope) {
        return paramEnvelope.Intersect(this.Extend);
    }

    public void SetExtend(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
        this.MinX = paramDouble1;
        this.MinY = paramDouble2;
        this.MaxX = paramDouble3;
        this.MaxY = paramDouble4;
        this.Extend = new Envelope(paramDouble1, paramDouble4, paramDouble3, paramDouble2);
    }
}
