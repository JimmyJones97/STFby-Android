package  com.xzy.forestSystem.gogisapi.Common;

import  com.xzy.forestSystem.gogisapi.Geometry.Coordinate;

public class CommonMath {
    public static double abs(double d) {
        return Math.abs(d);
    }

    public static float abs(float d) {
        return Math.abs(d);
    }

    public static int abs(int d) {
        return Math.abs(d);
    }

    public static long abs(long d) {
        return Math.abs(d);
    }

    public static double acos(double d) {
        return Math.toDegrees(Math.acos(d));
    }

    public static double asin(double d) {
        return Math.toDegrees(Math.asin(d));
    }

    public static double atan(double d) {
        return Math.toDegrees(Math.atan(d));
    }

    public static double cbrt(double d) {
        return Math.cbrt(d);
    }

    public static double cos(double d) {
        return Math.cos(Math.toRadians(d));
    }

    public static double exp(double d) {
        return Math.exp(d);
    }

    /* renamed from: ln */
    public static double m4ln(double d) {
        return Math.log(d);
    }

    public static double log(double d) {
        return Math.log10(d);
    }

    public static double pow(double a, double b) {
        return Math.pow(a, b);
    }

    public static double sin(double d) {
        return Math.sin(Math.toRadians(d));
    }

    public static double sqrt(double d) {
        return Math.sqrt(d);
    }

    public static double tan(double d) {
        return Math.tan(Math.toRadians(d));
    }

    public static double getE() {
        return 2.718281828459045d;
    }

    public static double getPI() {
        return 3.141592653589793d;
    }

    public static double PointToSegDist(double x, double y, double x1, double y1, double x2, double y2, Coordinate outIntersectPtn) {
        double cross = ((x2 - x1) * (x - x1)) + ((y2 - y1) * (y - y1));
        if (cross <= 0.0d) {
            outIntersectPtn.setX(x1);
            outIntersectPtn.setY(y1);
            return Math.sqrt(((x - x1) * (x - x1)) + ((y - y1) * (y - y1)));
        }
        double d2 = ((x2 - x1) * (x2 - x1)) + ((y2 - y1) * (y2 - y1));
        if (cross >= d2) {
            outIntersectPtn.setX(x2);
            outIntersectPtn.setY(y2);
            return Math.sqrt(((x - x2) * (x - x2)) + ((y - y2) * (y - y2)));
        }
        double r = cross / d2;
        double px = x1 + ((x2 - x1) * r);
        double py = y1 + ((y2 - y1) * r);
        outIntersectPtn.setX(px);
        outIntersectPtn.setY(py);
        return Math.sqrt(((x - px) * (x - px)) + ((py - y) * (py - y)));
    }

    public static double[] Cal3MatrixInv(double[] values) {
        double tmpA = 1.0d / (((((values[0] * values[4]) * values[8]) + ((values[1] * values[5]) * values[6])) + ((values[2] * values[3]) * values[7])) - ((((values[2] * values[4]) * values[6]) + ((values[0] * values[5]) * values[7])) + ((values[1] * values[3]) * values[8])));
        return new double[]{tmpA * ((values[4] * values[8]) - (values[5] * values[7])), tmpA * (((-values[1]) * values[8]) + (values[2] * values[7])), tmpA * ((values[1] * values[5]) - (values[4] * values[2])), tmpA * (((-values[3]) * values[8]) + (values[5] * values[6])), tmpA * ((values[0] * values[8]) - (values[2] * values[6])), tmpA * (((-values[0]) * values[5]) + (values[2] * values[3])), tmpA * ((values[3] * values[7]) - (values[4] * values[6])), tmpA * (((-values[0]) * values[7]) + (values[1] * values[6])), tmpA * ((values[0] * values[4]) - (values[1] * values[3]))};
    }

    public static boolean onSegment(Coordinate p, Coordinate q, Coordinate r) {
        if (q.getX() > Math.max(p.getX(), r.getX()) || q.getX() < Math.min(p.getX(), r.getX()) || q.getY() > Math.max(p.getY(), r.getY()) || q.getY() < Math.min(p.getY(), r.getY())) {
            return false;
        }
        return true;
    }

    public static int orientation(Coordinate p, Coordinate q, Coordinate r) {
        double val = ((q.getY() - p.getY()) * (r.getX() - q.getX())) - ((q.getX() - p.getX()) * (r.getY() - q.getY()));
        if (val == 0.0d) {
            return 0;
        }
        return val > 0.0d ? 1 : 2;
    }

    public static boolean JudgeLineSegIntersect(Coordinate p1, Coordinate q1, Coordinate p2, Coordinate q2) {
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);
        if (o1 != o2 && o3 != o4) {
            return true;
        }
        if (o1 == 0 && onSegment(p1, p2, q1)) {
            return true;
        }
        if (o2 == 0 && onSegment(p1, q2, q1)) {
            return true;
        }
        if (o3 == 0 && onSegment(p2, p1, q2)) {
            return true;
        }
        if (o4 != 0 || !onSegment(p2, q1, q2)) {
            return false;
        }
        return true;
    }

    public static double multi(Coordinate p1, Coordinate p2, Coordinate p0) {
        return ((p1.getX() - p0.getX()) * (p2.getY() - p0.getY())) - ((p2.getX() - p0.getX()) * (p1.getY() - p0.getY()));
    }

    public static Coordinate CalLineSegIntersection(Coordinate p1, Coordinate q1, Coordinate p2, Coordinate q2) {
        return new Coordinate(((multi(q2, q1, p1) * p2.getX()) - (multi(p2, q1, p1) * q2.getX())) / (multi(q2, q1, p1) - multi(p2, q1, p1)), ((multi(q2, q1, p1) * p2.getY()) - (multi(p2, q1, p1) * q2.getY())) / (multi(q2, q1, p1) - multi(p2, q1, p1)));
    }
}
