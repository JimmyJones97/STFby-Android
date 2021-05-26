package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.util;

public class AcadExtrusionCalculator {
    public static double[] CalculateAcadExtrusion(double[] coord_in, double[] xtru) {
        double dvx1;
        double dvx2;
        double dvx3;
        double aux1 = Math.abs(xtru[0]);
        double aux2 = Math.abs(xtru[1]);
        double dxt0 = coord_in[0];
        double dyt0 = coord_in[1];
        double dzt0 = coord_in[2];
        double xtruX = xtru[0];
        double xtruY = xtru[1];
        double xtruZ = xtru[2];
        if (aux1 >= 0.015625d || aux2 >= 0.015625d) {
            double dmod = Math.sqrt((xtruY * xtruY) + (xtruX * xtruX));
            dvx1 = (-xtruY) / dmod;
            dvx2 = xtruX / dmod;
            dvx3 = 0.0d;
        } else {
            double dmod2 = Math.sqrt((xtruZ * xtruZ) + (xtruX * xtruX));
            dvx1 = xtruZ / dmod2;
            dvx2 = 0.0d;
            dvx3 = (-xtruX) / dmod2;
        }
        double dvy1 = (xtruY * dvx3) - (xtruZ * dvx2);
        double dvy2 = (xtruZ * dvx1) - (xtruX * dvx3);
        double dvy3 = (xtruX * dvx2) - (xtruY * dvx1);
        double dmod3 = Math.sqrt((dvy1 * dvy1) + (dvy2 * dvy2) + (dvy3 * dvy3));
        return new double[]{(dvx1 * dxt0) + ((dvy1 / dmod3) * dyt0) + (xtruX * dzt0), (dvx2 * dxt0) + ((dvy2 / dmod3) * dyt0) + (xtruY * dzt0), (dvx3 * dxt0) + ((dvy3 / dmod3) * dyt0) + (xtruZ * dzt0)};
    }
}
