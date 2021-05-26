package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.util.TextToUnicodeConverter;
import java.util.Vector;

public class DwgText extends DwgObject {
    private Point2D alignmentPoint;
    private int dataFlag;
    private double elevation;
    private double[] extrusion;
    private int generation;
    private int halign;
    private double height;
    private Point2D insertionPoint;
    private double obliqueAngle;
    private double rotationAngle;
    private String text;
    private double thickness;
    private int valign;
    private double widthFactor;

    public void readDwgTextV15(int[] data, int offset) throws Exception {
        double x;
        double y;
        double z;
        double th;
        Vector v = DwgUtil.getRawChar(data, readObjectHeaderV15(data, offset));
        int bitPos = ((Integer) v.get(0)).intValue();
        int dflag = ((Integer) v.get(1)).intValue();
        this.dataFlag = dflag;
        if ((dflag & 1) == 0) {
            Vector v2 = DwgUtil.getRawDouble(data, bitPos);
            bitPos = ((Integer) v2.get(0)).intValue();
            this.elevation = ((Double) v2.get(1)).doubleValue();
        }
        Vector v3 = DwgUtil.getRawDouble(data, bitPos);
        int bitPos2 = ((Integer) v3.get(0)).intValue();
        double x1 = ((Double) v3.get(1)).doubleValue();
        Vector v4 = DwgUtil.getRawDouble(data, bitPos2);
        int bitPos3 = ((Integer) v4.get(0)).intValue();
        double y1 = ((Double) v4.get(1)).doubleValue();
        this.insertionPoint = new Point2D(x1, y1);
        if ((dflag & 2) == 0) {
            Vector v5 = DwgUtil.getDefaultDouble(data, bitPos3, x1);
            int bitPos4 = ((Integer) v5.get(0)).intValue();
            double xa = ((Double) v5.get(1)).doubleValue();
            Vector v6 = DwgUtil.getDefaultDouble(data, bitPos4, y1);
            bitPos3 = ((Integer) v6.get(0)).intValue();
            this.alignmentPoint = new Point2D(xa, ((Double) v6.get(1)).doubleValue());
        }
        Vector v7 = DwgUtil.testBit(data, bitPos3);
        int bitPos5 = ((Integer) v7.get(0)).intValue();
        if (((Boolean) v7.get(1)).booleanValue()) {
            x = 0.0d;
            y = 0.0d;
            z = 1.0d;
        } else {
            Vector v8 = DwgUtil.getBitDouble(data, bitPos5);
            int bitPos6 = ((Integer) v8.get(0)).intValue();
            x = ((Double) v8.get(1)).doubleValue();
            Vector v9 = DwgUtil.getBitDouble(data, bitPos6);
            int bitPos7 = ((Integer) v9.get(0)).intValue();
            y = ((Double) v9.get(1)).doubleValue();
            Vector v10 = DwgUtil.getBitDouble(data, bitPos7);
            bitPos5 = ((Integer) v10.get(0)).intValue();
            z = ((Double) v10.get(1)).doubleValue();
        }
        this.extrusion = new double[]{x, y, z};
        Vector v11 = DwgUtil.testBit(data, bitPos5);
        int bitPos8 = ((Integer) v11.get(0)).intValue();
        if (((Boolean) v11.get(1)).booleanValue()) {
            th = 0.0d;
        } else {
            Vector v12 = DwgUtil.getBitDouble(data, bitPos8);
            bitPos8 = ((Integer) v12.get(0)).intValue();
            th = ((Double) v12.get(1)).doubleValue();
        }
        this.thickness = th;
        if ((dflag & 4) == 0) {
            Vector v13 = DwgUtil.getRawDouble(data, bitPos8);
            bitPos8 = ((Integer) v13.get(0)).intValue();
            this.obliqueAngle = ((Double) v13.get(1)).doubleValue();
        }
        if ((dflag & 8) == 0) {
            Vector v14 = DwgUtil.getRawDouble(data, bitPos8);
            bitPos8 = ((Integer) v14.get(0)).intValue();
            this.rotationAngle = ((Double) v14.get(1)).doubleValue();
        }
        Vector v15 = DwgUtil.getRawDouble(data, bitPos8);
        int bitPos9 = ((Integer) v15.get(0)).intValue();
        this.height = ((Double) v15.get(1)).doubleValue();
        if ((dflag & 16) == 0) {
            Vector v16 = DwgUtil.getRawDouble(data, bitPos9);
            bitPos9 = ((Integer) v16.get(0)).intValue();
            this.widthFactor = ((Double) v16.get(1)).doubleValue();
        }
        Vector v17 = DwgUtil.getTextString(data, bitPos9);
        int bitPos10 = ((Integer) v17.get(0)).intValue();
        this.text = TextToUnicodeConverter.convertText((String) v17.get(1));
        if ((dflag & 32) == 0) {
            Vector v18 = DwgUtil.getBitShort(data, bitPos10);
            bitPos10 = ((Integer) v18.get(0)).intValue();
            this.generation = ((Integer) v18.get(1)).intValue();
        }
        if ((dflag & 64) == 0) {
            Vector v19 = DwgUtil.getBitShort(data, bitPos10);
            bitPos10 = ((Integer) v19.get(0)).intValue();
            this.halign = ((Integer) v19.get(1)).intValue();
        }
        if ((dflag & 128) == 0) {
            Vector v20 = DwgUtil.getBitShort(data, bitPos10);
            bitPos10 = ((Integer) v20.get(0)).intValue();
            this.valign = ((Integer) v20.get(1)).intValue();
        }
        readObjectTailV15(data, bitPos10);
    }

    public int getDataFlag() {
        return this.dataFlag;
    }

    public void setDataFlag(int dataFlag2) {
        this.dataFlag = dataFlag2;
    }

    public double getHeight() {
        return this.height;
    }

    public void setHeight(double height2) {
        this.height = height2;
    }

    public Point2D getInsertionPoint() {
        return this.insertionPoint;
    }

    public void setInsertionPoint(Point2D insertionPoint2) {
        this.insertionPoint = insertionPoint2;
    }

    public double getRotationAngle() {
        return this.rotationAngle;
    }

    public void setRotationAngle(double rotationAngle2) {
        this.rotationAngle = rotationAngle2;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text2) {
        this.text = text2;
    }

    public double getElevation() {
        return this.elevation;
    }

    public void setElevation(double elevation2) {
        this.elevation = elevation2;
    }

    public double getThickness() {
        return this.thickness;
    }

    public void setThickness(double thickness2) {
        this.thickness = thickness2;
    }

    public double[] getExtrusion() {
        return this.extrusion;
    }

    public Object clone() {
        DwgText dwgText = new DwgText();
        dwgText.setType(this.type);
        dwgText.setHandle(this.handle);
        dwgText.setVersion(this.version);
        dwgText.setMode(this.mode);
        dwgText.setLayerHandle(this.layerHandle);
        dwgText.setColor(this.color);
        dwgText.setNumReactors(this.numReactors);
        dwgText.setNoLinks(this.noLinks);
        dwgText.setLinetypeFlags(this.linetypeFlags);
        dwgText.setPlotstyleFlags(this.plotstyleFlags);
        dwgText.setSizeInBits(this.sizeInBits);
        dwgText.setExtendedData(this.extendedData);
        dwgText.setGraphicData(this.graphicData);
        dwgText.setDataFlag(this.dataFlag);
        dwgText.setElevation(this.elevation);
        dwgText.setInsertionPoint(this.insertionPoint);
        dwgText.setAlignmentPoint(this.alignmentPoint);
        dwgText.setExtrusion(this.extrusion);
        dwgText.setThickness(this.thickness);
        dwgText.setObliqueAngle(this.obliqueAngle);
        dwgText.setRotationAngle(this.rotationAngle);
        dwgText.setHeight(this.height);
        dwgText.setWidthFactor(this.widthFactor);
        dwgText.setText(this.text);
        dwgText.setGeneration(this.generation);
        dwgText.setHalign(this.halign);
        dwgText.setValign(this.valign);
        return dwgText;
    }

    public Point2D getAlignmentPoint() {
        return this.alignmentPoint;
    }

    public void setAlignmentPoint(Point2D alignmentPoint2) {
        this.alignmentPoint = alignmentPoint2;
    }

    public int getGeneration() {
        return this.generation;
    }

    public void setGeneration(int generation2) {
        this.generation = generation2;
    }

    public int getHalign() {
        return this.halign;
    }

    public void setHalign(int halign2) {
        this.halign = halign2;
    }

    public double getObliqueAngle() {
        return this.obliqueAngle;
    }

    public void setObliqueAngle(double obliqueAngle2) {
        this.obliqueAngle = obliqueAngle2;
    }

    public int getValign() {
        return this.valign;
    }

    public void setValign(int valign2) {
        this.valign = valign2;
    }

    public double getWidthFactor() {
        return this.widthFactor;
    }

    public void setWidthFactor(double widthFactor2) {
        this.widthFactor = widthFactor2;
    }

    public void setExtrusion(double[] extrusion2) {
        this.extrusion = extrusion2;
    }
}
