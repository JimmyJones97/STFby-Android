package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.util;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.Point2D;
import java.util.Vector;

public class ArcFromBulgeCalculator {
    private double acaba;
    private double aci;
    private double bulge;
    private Point2D center;
    private Point2D coord1;
    private Point2D coord2;
    private Point2D coordAux;

    /* renamed from: d */
    private double f497d;

    /* renamed from: dd */
    private double f498dd;
    private double empieza;
    private double radio;

    public ArcFromBulgeCalculator(Point2D p1, Point2D p2, double bulge2) {
        this.bulge = bulge2;
        if (bulge2 < 0.0d) {
            this.coord1 = p2;
            this.coord2 = p1;
        } else {
            this.coord1 = p1;
            this.coord2 = p2;
        }
        calParams();
    }

    private void calParams() {
        this.f497d = Math.sqrt(((this.coord2.getX() - this.coord1.getX()) * (this.coord2.getX() - this.coord1.getX())) + ((this.coord2.getY() - this.coord1.getY()) * (this.coord2.getY() - this.coord1.getY())));
        this.coordAux = new Point2D((this.coord1.getX() + this.coord2.getX()) / 2.0d, (this.coord1.getY() + this.coord2.getY()) / 2.0d);
        double alfa = Math.atan(Math.abs(this.bulge)) * 4.0d;
        double landa = alfa / 2.0d;
        this.f498dd = (this.f497d / 2.0d) / Math.tan(landa);
        this.radio = (this.f497d / 2.0d) / Math.sin(landa);
        this.aci = Math.atan((this.coord2.getX() - this.coord1.getX()) / (this.coord2.getY() - this.coord1.getY()));
        double d = (this.aci * 180.0d) / 3.141592653589793d;
        if (this.coord2.getY() > this.coord1.getY()) {
            this.aci += 3.141592653589793d;
            double aciDegree = (this.aci * 180.0d) / 3.141592653589793d;
        }
        this.center = new Point2D(this.coordAux.getX() + (this.f498dd * Math.sin(this.aci + 1.5707963267948966d)), this.coordAux.getY() + (this.f498dd * Math.cos(this.aci + 1.5707963267948966d)));
        calEA(alfa);
    }

    private void calEA(double alfa) {
        this.empieza = Math.atan2(this.coord1.getY() - this.center.getY(), this.coord1.getX() - this.center.getX());
        this.acaba = this.empieza + alfa;
        this.empieza = (this.empieza * 180.0d) / 3.141592653589793d;
        this.acaba = (this.acaba * 180.0d) / 3.141592653589793d;
    }

    public Vector getPoints(double inc) {
        Vector arc = new Vector();
        int iempieza = ((int) this.empieza) + 1;
        int iacaba = (int) this.acaba;
        if (this.empieza <= this.acaba) {
            addNode(arc, this.empieza);
            double angulo = (double) iempieza;
            while (angulo <= ((double) iacaba)) {
                addNode(arc, angulo);
                angulo += inc;
            }
            addNode(arc, this.acaba);
        } else {
            addNode(arc, this.empieza);
            double angulo2 = (double) iempieza;
            while (angulo2 <= 360.0d) {
                addNode(arc, angulo2);
                angulo2 += inc;
            }
            double angulo3 = 1.0d;
            while (angulo3 <= ((double) iacaba)) {
                addNode(arc, angulo3);
                angulo3 += inc;
            }
            addNode(arc, angulo3);
        }
        Point2D aux = (Point2D) arc.get(arc.size() - 1);
        Math.abs(aux.getX() - this.coord2.getX());
        Math.abs(aux.getY() - this.coord2.getY());
        return arc;
    }

    public Vector getCentralPoint() {
        Vector arc = new Vector();
        if (this.empieza <= this.acaba) {
            addNode(arc, (this.empieza + this.acaba) / 2.0d);
        } else {
            addNode(arc, this.empieza);
            double alfa = 360.0d - this.empieza;
            double mid = (alfa + this.acaba) / 2.0d;
            if (mid <= alfa) {
                addNode(arc, this.empieza + mid);
            } else {
                addNode(arc, mid - alfa);
            }
        }
        return arc;
    }

    private void addNode(Vector arc, double angulo) {
        arc.add(new Point2D(this.center.getX() + (this.radio * Math.cos((3.141592653589793d * angulo) / 180.0d)), this.center.getY() + (this.radio * Math.sin((3.141592653589793d * angulo) / 180.0d))));
    }
}
