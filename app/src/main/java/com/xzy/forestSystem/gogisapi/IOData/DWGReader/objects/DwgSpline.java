package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.lang.reflect.Array;
import java.util.Vector;

public class DwgSpline extends DwgObject {
    private double[] beginTanVector;
    private boolean closed;
    private double[][] controlPoints;
    private double controlTolerance;
    private int degree;
    private double[] endTanVector;
    private double[][] fitPoints;
    private double fitTolerance;
    private double[] knotPoints;
    private double knotTolerance;
    private boolean periodic;
    private boolean rational;
    private int scenario;
    private double[] weights;

    public void readDwgSplineV15(int[] data, int offset) throws Exception {
        Vector v = DwgUtil.getBitShort(data, readObjectHeaderV15(data, offset));
        int bitPos = ((Integer) v.get(0)).intValue();
        int sc = ((Integer) v.get(1)).intValue();
        this.scenario = sc;
        Vector v2 = DwgUtil.getBitShort(data, bitPos);
        int bitPos2 = ((Integer) v2.get(0)).intValue();
        this.degree = ((Integer) v2.get(1)).intValue();
        int knotsNumber = 0;
        int controlPointsNumber = 0;
        int fitPointsNumber = 0;
        boolean weight = false;
        if (sc == 2) {
            Vector v3 = DwgUtil.getBitDouble(data, bitPos2);
            int bitPos3 = ((Integer) v3.get(0)).intValue();
            this.fitTolerance = ((Double) v3.get(1)).doubleValue();
            Vector v4 = DwgUtil.getBitDouble(data, bitPos3);
            int bitPos4 = ((Integer) v4.get(0)).intValue();
            double x = ((Double) v4.get(1)).doubleValue();
            Vector v5 = DwgUtil.getBitDouble(data, bitPos4);
            int bitPos5 = ((Integer) v5.get(0)).intValue();
            double y = ((Double) v5.get(1)).doubleValue();
            Vector v6 = DwgUtil.getBitDouble(data, bitPos5);
            int bitPos6 = ((Integer) v6.get(0)).intValue();
            this.beginTanVector = new double[]{x, y, ((Double) v6.get(1)).doubleValue()};
            Vector v7 = DwgUtil.getBitDouble(data, bitPos6);
            int bitPos7 = ((Integer) v7.get(0)).intValue();
            double x2 = ((Double) v7.get(1)).doubleValue();
            Vector v8 = DwgUtil.getBitDouble(data, bitPos7);
            int bitPos8 = ((Integer) v8.get(0)).intValue();
            double y2 = ((Double) v8.get(1)).doubleValue();
            Vector v9 = DwgUtil.getBitDouble(data, bitPos8);
            int bitPos9 = ((Integer) v9.get(0)).intValue();
            this.endTanVector = new double[]{x2, y2, ((Double) v9.get(1)).doubleValue()};
            Vector v10 = DwgUtil.getBitShort(data, bitPos9);
            bitPos2 = ((Integer) v10.get(0)).intValue();
            fitPointsNumber = ((Integer) v10.get(1)).intValue();
        } else if (sc == 1) {
            Vector v11 = DwgUtil.testBit(data, bitPos2);
            int bitPos10 = ((Integer) v11.get(0)).intValue();
            this.rational = ((Boolean) v11.get(1)).booleanValue();
            Vector v12 = DwgUtil.testBit(data, bitPos10);
            int bitPos11 = ((Integer) v12.get(0)).intValue();
            this.closed = ((Boolean) v12.get(1)).booleanValue();
            Vector v13 = DwgUtil.testBit(data, bitPos11);
            int bitPos12 = ((Integer) v13.get(0)).intValue();
            this.periodic = ((Boolean) v13.get(1)).booleanValue();
            Vector v14 = DwgUtil.getBitDouble(data, bitPos12);
            int bitPos13 = ((Integer) v14.get(0)).intValue();
            this.knotTolerance = ((Double) v14.get(1)).doubleValue();
            Vector v15 = DwgUtil.getBitDouble(data, bitPos13);
            int bitPos14 = ((Integer) v15.get(0)).intValue();
            this.controlTolerance = ((Double) v15.get(1)).doubleValue();
            Vector v16 = DwgUtil.getBitLong(data, bitPos14);
            int bitPos15 = ((Integer) v16.get(0)).intValue();
            knotsNumber = ((Integer) v16.get(1)).intValue();
            Vector v17 = DwgUtil.getBitLong(data, bitPos15);
            int bitPos16 = ((Integer) v17.get(0)).intValue();
            controlPointsNumber = ((Integer) v17.get(1)).intValue();
            Vector v18 = DwgUtil.testBit(data, bitPos16);
            bitPos2 = ((Integer) v18.get(0)).intValue();
            weight = ((Boolean) v18.get(1)).booleanValue();
        }
        if (knotsNumber > 0) {
            double[] knotpts = new double[knotsNumber];
            for (int i = 0; i < knotsNumber; i++) {
                Vector v19 = DwgUtil.getBitDouble(data, bitPos2);
                bitPos2 = ((Integer) v19.get(0)).intValue();
                knotpts[i] = ((Double) v19.get(1)).doubleValue();
            }
            this.knotPoints = knotpts;
        }
        if (controlPointsNumber > 0) {
            double[][] ctrlpts = (double[][]) Array.newInstance(Double.TYPE, controlPointsNumber, 3);
            double[] weights2 = new double[controlPointsNumber];
            for (int i2 = 0; i2 < controlPointsNumber; i2++) {
                Vector v20 = DwgUtil.getBitDouble(data, bitPos2);
                int bitPos17 = ((Integer) v20.get(0)).intValue();
                double x3 = ((Double) v20.get(1)).doubleValue();
                Vector v21 = DwgUtil.getBitDouble(data, bitPos17);
                int bitPos18 = ((Integer) v21.get(0)).intValue();
                double y3 = ((Double) v21.get(1)).doubleValue();
                Vector v22 = DwgUtil.getBitDouble(data, bitPos18);
                bitPos2 = ((Integer) v22.get(0)).intValue();
                double z = ((Double) v22.get(1)).doubleValue();
                ctrlpts[i2][0] = x3;
                ctrlpts[i2][1] = y3;
                ctrlpts[i2][2] = z;
                if (weight) {
                    Vector v23 = DwgUtil.getBitDouble(data, bitPos2);
                    bitPos2 = ((Integer) v23.get(0)).intValue();
                    weights2[i2] = ((Double) v23.get(1)).doubleValue();
                }
            }
            this.controlPoints = ctrlpts;
            if (weight) {
                this.weights = weights2;
            }
        }
        if (fitPointsNumber > 0) {
            double[][] fitpts = (double[][]) Array.newInstance(Double.TYPE, fitPointsNumber, 3);
            for (int i3 = 0; i3 < fitPointsNumber; i3++) {
                Vector v24 = DwgUtil.getBitDouble(data, bitPos2);
                int bitPos19 = ((Integer) v24.get(0)).intValue();
                double x4 = ((Double) v24.get(1)).doubleValue();
                Vector v25 = DwgUtil.getBitDouble(data, bitPos19);
                int bitPos20 = ((Integer) v25.get(0)).intValue();
                double y4 = ((Double) v25.get(1)).doubleValue();
                Vector v26 = DwgUtil.getBitDouble(data, bitPos20);
                bitPos2 = ((Integer) v26.get(0)).intValue();
                double z2 = ((Double) v26.get(1)).doubleValue();
                fitpts[i3][0] = x4;
                fitpts[i3][1] = y4;
                fitpts[i3][2] = z2;
            }
            this.fitPoints = fitpts;
        }
        readObjectTailV15(data, bitPos2);
    }

    public boolean isClosed() {
        return this.closed;
    }

    public void setClosed(boolean closed2) {
        this.closed = closed2;
    }

    public double[][] getControlPoints() {
        return this.controlPoints;
    }

    public void setControlPoints(double[][] controlPoints2) {
        this.controlPoints = controlPoints2;
    }

    public double[][] getFitPoints() {
        return this.fitPoints;
    }

    public void setFitPoints(double[][] fitPoints2) {
        this.fitPoints = fitPoints2;
    }

    public double[] getKnotPoints() {
        return this.knotPoints;
    }

    public void setKnotPoints(double[] knotPoints2) {
        this.knotPoints = knotPoints2;
    }

    public int getScenario() {
        return this.scenario;
    }

    public void setScenario(int scenario2) {
        this.scenario = scenario2;
    }

    public Object clone() {
        DwgSpline dwgSpline = new DwgSpline();
        dwgSpline.setType(this.type);
        dwgSpline.setHandle(this.handle);
        dwgSpline.setVersion(this.version);
        dwgSpline.setMode(this.mode);
        dwgSpline.setLayerHandle(this.layerHandle);
        dwgSpline.setColor(this.color);
        dwgSpline.setNumReactors(this.numReactors);
        dwgSpline.setNoLinks(this.noLinks);
        dwgSpline.setLinetypeFlags(this.linetypeFlags);
        dwgSpline.setPlotstyleFlags(this.plotstyleFlags);
        dwgSpline.setSizeInBits(this.sizeInBits);
        dwgSpline.setExtendedData(this.extendedData);
        dwgSpline.setGraphicData(this.graphicData);
        dwgSpline.setScenario(this.scenario);
        dwgSpline.setDegree(this.degree);
        dwgSpline.setFitTolerance(this.fitTolerance);
        dwgSpline.setBeginTanVector(this.beginTanVector);
        dwgSpline.setEndTanVector(this.endTanVector);
        dwgSpline.setRational(this.rational);
        dwgSpline.setClosed(this.closed);
        dwgSpline.setPeriodic(this.periodic);
        dwgSpline.setKnotTolerance(this.knotTolerance);
        dwgSpline.setControlTolerance(this.controlTolerance);
        dwgSpline.setKnotPoints(this.knotPoints);
        dwgSpline.setControlPoints(this.controlPoints);
        dwgSpline.setWeights(this.weights);
        dwgSpline.setFitPoints(this.fitPoints);
        return dwgSpline;
    }

    public double[] getBeginTanVector() {
        return this.beginTanVector;
    }

    public void setBeginTanVector(double[] beginTanVector2) {
        this.beginTanVector = beginTanVector2;
    }

    public double getControlTolerance() {
        return this.controlTolerance;
    }

    public void setControlTolerance(double controlTolerance2) {
        this.controlTolerance = controlTolerance2;
    }

    public int getDegree() {
        return this.degree;
    }

    public void setDegree(int degree2) {
        this.degree = degree2;
    }

    public double[] getEndTanVector() {
        return this.endTanVector;
    }

    public void setEndTanVector(double[] endTanVector2) {
        this.endTanVector = endTanVector2;
    }

    public double getFitTolerance() {
        return this.fitTolerance;
    }

    public void setFitTolerance(double fitTolerance2) {
        this.fitTolerance = fitTolerance2;
    }

    public double getKnotTolerance() {
        return this.knotTolerance;
    }

    public void setKnotTolerance(double knotTolerance2) {
        this.knotTolerance = knotTolerance2;
    }

    public boolean isPeriodic() {
        return this.periodic;
    }

    public void setPeriodic(boolean periodic2) {
        this.periodic = periodic2;
    }

    public boolean isRational() {
        return this.rational;
    }

    public void setRational(boolean rational2) {
        this.rational = rational2;
    }

    public double[] getWeights() {
        return this.weights;
    }

    public void setWeights(double[] weights2) {
        this.weights = weights2;
    }
}
