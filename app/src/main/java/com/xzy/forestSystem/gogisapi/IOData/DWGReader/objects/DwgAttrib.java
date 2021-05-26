package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgAttrib extends DwgObject {
    private Point2D alignmentPoint;
    private int dataFlag;
    private double elevation;
    private double[] extrusion;
    private int fieldLength;
    private int flags;
    private int generation;
    private int halign;
    private double height;
    private Point2D insertionPoint;
    private double obliqueAngle;
    private String prompt;
    private double rotationAngle;
    private int styleHandle;
    private String tag;
    private String text;
    private double thickness;
    private int valign;
    private double widthFactor;

    public void readDwgAttribV15(int[] data, int offset) throws Exception {
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
        double x2 = 0.0d;
        double y2 = 0.0d;
        if ((dflag & 2) == 0) {
            Vector v5 = DwgUtil.getDefaultDouble(data, bitPos3, x1);
            int bitPos4 = ((Integer) v5.get(0)).intValue();
            x2 = ((Double) v5.get(1)).doubleValue();
            Vector v6 = DwgUtil.getDefaultDouble(data, bitPos4, y1);
            bitPos3 = ((Integer) v6.get(0)).intValue();
            y2 = ((Double) v6.get(1)).doubleValue();
        }
        this.alignmentPoint = new Point2D(x2, y2);
        Vector v7 = DwgUtil.testBit(data, bitPos3);
        int bitPos5 = ((Integer) v7.get(0)).intValue();
        if (((Boolean) v7.get(1)).booleanValue()) {
            y = 0.0d;
            x = 0.0d;
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
        this.text = (String) v17.get(1);
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
        Vector v21 = DwgUtil.getTextString(data, bitPos10);
        int bitPos11 = ((Integer) v21.get(0)).intValue();
        this.tag = (String) v21.get(1);
        Vector v22 = DwgUtil.getBitShort(data, bitPos11);
        int bitPos12 = ((Integer) v22.get(0)).intValue();
        this.fieldLength = ((Integer) v22.get(1)).intValue();
        Vector v23 = DwgUtil.getRawChar(data, bitPos12);
        int bitPos13 = ((Integer) v23.get(0)).intValue();
        this.flags = ((Integer) v23.get(1)).intValue();
        Vector v24 = DwgUtil.getHandle(data, readObjectTailV15(data, bitPos13));
        ((Integer) v24.get(0)).intValue();
        int[] handle = new int[(v24.size() - 1)];
        for (int j = 1; j < v24.size(); j++) {
            handle[j - 1] = ((Integer) v24.get(j)).intValue();
        }
        Vector handleVect = new Vector();
        for (int i = 0; i < handle.length; i++) {
            handleVect.add(new Integer(handle[i]));
        }
        this.styleHandle = DwgUtil.handleBinToHandleInt(handleVect);
    }

    public double getElevation() {
        return this.elevation;
    }

    public void setElevation(double elevation2) {
        this.elevation = elevation2;
    }

    public double[] getExtrusion() {
        return this.extrusion;
    }

    public Point2D getInsertionPoint() {
        return this.insertionPoint;
    }

    public void setInsertionPoint(Point2D insertionPoint2) {
        this.insertionPoint = insertionPoint2;
    }

    public Object clone() {
        DwgAttrib dwgAttrib = new DwgAttrib();
        dwgAttrib.setType(this.type);
        dwgAttrib.setHandle(this.handle);
        dwgAttrib.setVersion(this.version);
        dwgAttrib.setMode(this.mode);
        dwgAttrib.setLayerHandle(this.layerHandle);
        dwgAttrib.setColor(this.color);
        dwgAttrib.setNumReactors(this.numReactors);
        dwgAttrib.setNoLinks(this.noLinks);
        dwgAttrib.setLinetypeFlags(this.linetypeFlags);
        dwgAttrib.setPlotstyleFlags(this.plotstyleFlags);
        dwgAttrib.setSizeInBits(this.sizeInBits);
        dwgAttrib.setExtendedData(this.extendedData);
        dwgAttrib.setGraphicData(this.graphicData);
        dwgAttrib.setDataFlag(this.dataFlag);
        dwgAttrib.setElevation(this.elevation);
        dwgAttrib.setInsertionPoint(this.insertionPoint);
        dwgAttrib.setAlignmentPoint(this.alignmentPoint);
        dwgAttrib.setExtrusion(this.extrusion);
        dwgAttrib.setThickness(this.thickness);
        dwgAttrib.setObliqueAngle(this.obliqueAngle);
        dwgAttrib.setRotationAngle(this.rotationAngle);
        dwgAttrib.setHeight(this.height);
        dwgAttrib.setWidthFactor(this.widthFactor);
        dwgAttrib.setText(this.text);
        dwgAttrib.setGeneration(this.generation);
        dwgAttrib.setHalign(this.halign);
        dwgAttrib.setValign(this.valign);
        dwgAttrib.setTag(this.tag);
        dwgAttrib.setFieldLength(this.fieldLength);
        dwgAttrib.setFlags(this.flags);
        dwgAttrib.setPrompt(this.prompt);
        dwgAttrib.setStyleHandle(this.styleHandle);
        return dwgAttrib;
    }

    public Point2D getAlignmentPoint() {
        return this.alignmentPoint;
    }

    public void setAlignmentPoint(Point2D alignmentPoint2) {
        this.alignmentPoint = alignmentPoint2;
    }

    public int getDataFlag() {
        return this.dataFlag;
    }

    public void setDataFlag(int dataFlag2) {
        this.dataFlag = dataFlag2;
    }

    public int getFieldLength() {
        return this.fieldLength;
    }

    public void setFieldLength(int fieldLength2) {
        this.fieldLength = fieldLength2;
    }

    public int getFlags() {
        return this.flags;
    }

    public void setFlags(int flags2) {
        this.flags = flags2;
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

    public double getHeight() {
        return this.height;
    }

    public void setHeight(double height2) {
        this.height = height2;
    }

    public double getObliqueAngle() {
        return this.obliqueAngle;
    }

    public void setObliqueAngle(double obliqueAngle2) {
        this.obliqueAngle = obliqueAngle2;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public void setPrompt(String prompt2) {
        this.prompt = prompt2;
    }

    public double getRotationAngle() {
        return this.rotationAngle;
    }

    public void setRotationAngle(double rotationAngle2) {
        this.rotationAngle = rotationAngle2;
    }

    public int getStyleHandle() {
        return this.styleHandle;
    }

    public void setStyleHandle(int styleHandle2) {
        this.styleHandle = styleHandle2;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text2) {
        this.text = text2;
    }

    public double getThickness() {
        return this.thickness;
    }

    public void setThickness(double thickness2) {
        this.thickness = thickness2;
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
