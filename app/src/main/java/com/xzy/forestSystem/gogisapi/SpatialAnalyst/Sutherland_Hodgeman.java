package  com.xzy.forestSystem.gogisapi.SpatialAnalyst;

import android.graphics.Point;
import java.util.ArrayList;
import java.util.List;

public class Sutherland_Hodgeman {
    public static List<Point> Clip(List<Point> points, List<Point[]> vectors) {
        boolean flag;
        List<Point> result = new ArrayList<>();
        List<Point> cur = new ArrayList<>();
        int vectorsSize = vectors.size();
        int pointSize = points.size();
        Point S = points.get(pointSize - 1);
        for (int i = 0; i < pointSize; i++) {
            result.add(points.get(i));
        }
        for (int j = 0; j < vectorsSize; j++) {
            Point[] tmpVectors = vectors.get(j);
            if (isInside(S, tmpVectors[0], tmpVectors[1])) {
                flag = false;
            } else {
                flag = true;
            }
            int resultSize = result.size();
            for (int i2 = 0; i2 < resultSize; i2++) {
                if (isInside(result.get(i2), tmpVectors[0], tmpVectors[1])) {
                    if (flag) {
                        flag = false;
                        cur.add(Intersection(S, result.get(i2), tmpVectors[0], tmpVectors[1]));
                    }
                    cur.add(result.get(i2));
                } else if (!flag) {
                    flag = true;
                    cur.add(Intersection(S, result.get(i2), tmpVectors[0], tmpVectors[1]));
                }
                S = result.get(i2);
            }
            int resultLen = cur.size();
            result.clear();
            for (int i3 = 0; i3 < resultLen; i3++) {
                result.add(cur.get(i3));
            }
            cur.clear();
        }
        return result;
    }

    public static boolean isInside(Point p, Point vStart, Point vEnd) {
        return Multi(p, vStart, vEnd) >= 0.0d;
    }

    public static double Multi(Point p0, Point p1, Point p2) {
        return (double) (((p1.x - p0.x) * (p2.y - p0.y)) - ((p2.x - p0.x) * (p1.y - p0.y)));
    }

    public static Point Intersection(Point start0, Point end0, Point start1, Point end1) {
        return new Point((int) (((Multi(start0, end1, start1) * ((double) end0.x)) - (Multi(end0, end1, start1) * ((double) start0.x))) / (Multi(start0, end1, start1) - Multi(end0, end1, start1))), (int) (((Multi(start0, end1, start1) * ((double) end0.y)) - (Multi(end0, end1, start1) * ((double) start0.y))) / (Multi(start0, end1, start1) - Multi(end0, end1, start1))));
    }
}
