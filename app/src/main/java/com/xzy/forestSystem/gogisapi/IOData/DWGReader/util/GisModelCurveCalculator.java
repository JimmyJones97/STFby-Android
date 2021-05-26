package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.util;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.Point2D;
import java.util.Vector;

public class GisModelCurveCalculator {
    public static Point2D[] calculateGisModelCircle(Point2D c, double r) {
        Point2D[] pts = new Point2D[360];
        for (int angulo = 0; angulo < 360; angulo++) {
            pts[angulo] = new Point2D(c.getX(), c.getY());
            pts[angulo].setLocation(pts[angulo].getX() + (Math.sin((((double) angulo) * 3.141592653589793d) / 180.0d) * r), pts[angulo].getY() + (Math.cos((((double) angulo) * 3.141592653589793d) / 180.0d) * r));
        }
        return pts;
    }

    public static Point2D[] calculateGisModelEllipse(Point2D center, Point2D majorAxisVector, double axisRatio, double initAngle, double endAngle) {
        Point2D[] pts;
        Point2D majorPoint = new Point2D(center.getX() + majorAxisVector.getX(), center.getY() + majorAxisVector.getY());
        double orientation = Math.atan(majorAxisVector.getY() / majorAxisVector.getX());
        double semiMajorAxisLength = center.distance(majorPoint);
        double semiMinorAxisLength = semiMajorAxisLength * axisRatio;
        double eccentricity = Math.sqrt(1.0d - (Math.pow(semiMinorAxisLength, 2.0d) / Math.pow(semiMajorAxisLength, 2.0d)));
        int isa = (int) initAngle;
        int iea = (int) endAngle;
        if (initAngle <= endAngle) {
            pts = new Point2D[((iea - isa) + 2)];
            double r = semiMinorAxisLength / Math.sqrt(1.0d - (Math.pow(eccentricity, 2.0d) * Math.pow(Math.cos((3.141592653589793d * initAngle) / 180.0d), 2.0d)));
            double x = r * Math.cos((3.141592653589793d * initAngle) / 180.0d);
            double y = r * Math.sin((3.141592653589793d * initAngle) / 180.0d);
            pts[0] = new Point2D(center.getX() + ((Math.cos(orientation) * x) - (Math.sin(orientation) * y)), center.getY() + (Math.sin(orientation) * x) + (Math.cos(orientation) * y));
            for (int i = 1; i <= (iea - isa) + 1; i++) {
                double angulo = (double) (isa + i);
                double r2 = semiMinorAxisLength / Math.sqrt(1.0d - (Math.pow(eccentricity, 2.0d) * Math.pow(Math.cos((3.141592653589793d * angulo) / 180.0d), 2.0d)));
                double x2 = r2 * Math.cos((3.141592653589793d * angulo) / 180.0d);
                double y2 = r2 * Math.sin((3.141592653589793d * angulo) / 180.0d);
                pts[i] = new Point2D(center.getX() + ((Math.cos(orientation) * x2) - (Math.sin(orientation) * y2)), center.getY() + (Math.sin(orientation) * x2) + (Math.cos(orientation) * y2));
            }
            double r3 = semiMinorAxisLength / Math.sqrt(1.0d - (Math.pow(eccentricity, 2.0d) * Math.pow(Math.cos((3.141592653589793d * endAngle) / 180.0d), 2.0d)));
            double x3 = r3 * Math.cos((3.141592653589793d * endAngle) / 180.0d);
            double y3 = r3 * Math.sin((3.141592653589793d * endAngle) / 180.0d);
            pts[(iea - isa) + 1] = new Point2D(center.getX() + ((Math.cos(orientation) * x3) - (Math.sin(orientation) * y3)), center.getY() + (Math.sin(orientation) * x3) + (Math.cos(orientation) * y3));
        } else {
            pts = new Point2D[((360 - isa) + iea + 2)];
            double r4 = semiMinorAxisLength / Math.sqrt(1.0d - (Math.pow(eccentricity, 2.0d) * Math.pow(Math.cos((3.141592653589793d * initAngle) / 180.0d), 2.0d)));
            double x4 = r4 * Math.cos((3.141592653589793d * initAngle) / 180.0d);
            double y4 = r4 * Math.sin((3.141592653589793d * initAngle) / 180.0d);
            double cos = (Math.cos(orientation) * x4) - (Math.sin(orientation) * y4);
            double sin = (Math.sin(orientation) * x4) + (Math.cos(orientation) * y4);
            pts[0] = new Point2D(center.getX() + (Math.cos((3.141592653589793d * initAngle) / 180.0d) * r4), center.getY() + (Math.sin((3.141592653589793d * initAngle) / 180.0d) * r4));
            for (int i2 = 1; i2 <= 360 - isa; i2++) {
                double angulo2 = (double) (isa + i2);
                double r5 = semiMinorAxisLength / Math.sqrt(1.0d - (Math.pow(eccentricity, 2.0d) * Math.pow(Math.cos((3.141592653589793d * angulo2) / 180.0d), 2.0d)));
                double x5 = r5 * Math.cos((3.141592653589793d * angulo2) / 180.0d);
                double y5 = r5 * Math.sin((3.141592653589793d * angulo2) / 180.0d);
                pts[i2] = new Point2D(center.getX() + ((Math.cos(orientation) * x5) - (Math.sin(orientation) * y5)), center.getY() + (Math.sin(orientation) * x5) + (Math.cos(orientation) * y5));
            }
            for (int i3 = (360 - isa) + 1; i3 <= (360 - isa) + iea; i3++) {
                double angulo3 = (double) (i3 - (360 - isa));
                double r6 = semiMinorAxisLength / Math.sqrt(1.0d - (Math.pow(eccentricity, 2.0d) * Math.pow(Math.cos((3.141592653589793d * angulo3) / 180.0d), 2.0d)));
                double x6 = r6 * Math.cos((3.141592653589793d * angulo3) / 180.0d);
                double y6 = r6 * Math.sin((3.141592653589793d * angulo3) / 180.0d);
                pts[i3] = new Point2D(center.getX() + ((Math.cos(orientation) * x6) - (Math.sin(orientation) * y6)), center.getY() + (Math.sin(orientation) * x6) + (Math.cos(orientation) * y6));
            }
            double r7 = semiMinorAxisLength / Math.sqrt(1.0d - (Math.pow(eccentricity, 2.0d) * Math.pow(Math.cos((3.141592653589793d * endAngle) / 180.0d), 2.0d)));
            double x7 = r7 * Math.cos((3.141592653589793d * endAngle) / 180.0d);
            double y7 = r7 * Math.sin((3.141592653589793d * endAngle) / 180.0d);
            pts[(360 - isa) + iea + 1] = new Point2D(center.getX() + ((Math.cos(orientation) * x7) - (Math.sin(orientation) * y7)), center.getY() + (Math.sin(orientation) * x7) + (Math.cos(orientation) * y7));
        }
        return pts;
    }

    public static Point2D[] calculateGisModelArc(Point2D c, double r, double sa, double ea) {
        Point2D[] pts;
        int isa = (int) sa;
        int iea = (int) ea;
        if (sa <= ea) {
            pts = new Point2D[((iea - isa) + 2)];
            pts[0] = new Point2D(c.getX() + (Math.cos((3.141592653589793d * sa) / 180.0d) * r), c.getY() + (Math.sin((3.141592653589793d * sa) / 180.0d) * r));
            for (int i = 1; i <= (iea - isa) + 1; i++) {
                double angulo = (double) (isa + i);
                pts[i] = new Point2D(c.getX() + (Math.cos((3.141592653589793d * angulo) / 180.0d) * r), c.getY() + (Math.sin((3.141592653589793d * angulo) / 180.0d) * r));
            }
            pts[(iea - isa) + 1] = new Point2D(c.getX() + (Math.cos((3.141592653589793d * ea) / 180.0d) * r), c.getY() + (Math.sin((3.141592653589793d * ea) / 180.0d) * r));
        } else {
            pts = new Point2D[((360 - isa) + iea + 2)];
            pts[0] = new Point2D(c.getX() + (Math.cos((3.141592653589793d * sa) / 180.0d) * r), c.getY() + (Math.sin((3.141592653589793d * sa) / 180.0d) * r));
            for (int i2 = 1; i2 <= 360 - isa; i2++) {
                double angulo2 = (double) (isa + i2);
                pts[i2] = new Point2D(c.getX() + (Math.cos((3.141592653589793d * angulo2) / 180.0d) * r), c.getY() + (Math.sin((3.141592653589793d * angulo2) / 180.0d) * r));
            }
            for (int i3 = (360 - isa) + 1; i3 <= (360 - isa) + iea; i3++) {
                double angulo3 = (double) (i3 - (360 - isa));
                pts[i3] = new Point2D(c.getX() + (Math.cos((3.141592653589793d * angulo3) / 180.0d) * r), c.getY() + (Math.sin((3.141592653589793d * angulo3) / 180.0d) * r));
            }
            pts[(360 - isa) + iea + 1] = new Point2D(c.getX() + (Math.cos((3.141592653589793d * ea) / 180.0d) * r), c.getY() + (Math.sin((3.141592653589793d * ea) / 180.0d) * r));
        }
        return pts;
    }

    public static Point2D[] calculateGisModelBulge(Point2D[] newPts, double[] bulges) {
        Vector ptspol = new Vector();
        new Point2D();
        Point2D end = new Point2D();
        for (int j = 0; j < newPts.length; j++) {
            Point2D init = newPts[j];
            if (j != newPts.length - 1) {
                end = newPts[j + 1];
            }
            if (bulges[j] == 0.0d || j == newPts.length - 1 || (init.getX() == end.getX() && init.getY() == end.getY())) {
                ptspol.add(init);
            } else {
                Vector arc = new ArcFromBulgeCalculator(init, end, bulges[j]).getPoints(1.0d);
                if (bulges[j] < 0.0d) {
                    for (int k = arc.size() - 1; k >= 0; k--) {
                        ptspol.add(arc.get(k));
                    }
                    ptspol.remove(ptspol.size() - 1);
                } else {
                    for (int k2 = 0; k2 < arc.size(); k2++) {
                        ptspol.add(arc.get(k2));
                    }
                    ptspol.remove(ptspol.size() - 1);
                }
            }
        }
        Point2D[] points = new Point2D[ptspol.size()];
        for (int j2 = 0; j2 < ptspol.size(); j2++) {
            points[j2] = (Point2D) ptspol.get(j2);
        }
        return points;
    }
}
