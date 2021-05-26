package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgLinearDimension extends DwgObject {
    private double actualMeasurement;
    private int anonBlockHandle;
    private int attachmentPoint;
    private double dimensionRotation;
    private int dimstyleHandle;
    private double elevation;
    private double extRotation;
    private double[] extrusion;
    private int flags;
    private double horizDir;
    private double insRotation;
    private double[] insScale;
    private double linespaceFactor;
    private int linespaceStyle;
    private double[] pt10;
    private Point2D pt12;
    private double[] pt13;
    private double[] pt14;
    private double rotation;
    private String text;
    private Point2D textMidpoint;

    public void readDwgLinearDimensionV15(int[] data, int offset) throws Exception {
        Vector v = DwgUtil.getBitDouble(data, readObjectHeaderV15(data, offset));
        int bitPos = ((Integer) v.get(0)).intValue();
        double x = ((Double) v.get(1)).doubleValue();
        Vector v2 = DwgUtil.getBitDouble(data, bitPos);
        int bitPos2 = ((Integer) v2.get(0)).intValue();
        double y = ((Double) v2.get(1)).doubleValue();
        Vector v3 = DwgUtil.getBitDouble(data, bitPos2);
        int bitPos3 = ((Integer) v3.get(0)).intValue();
        this.extrusion = new double[]{x, y, ((Double) v3.get(1)).doubleValue()};
        Vector v4 = DwgUtil.getRawDouble(data, bitPos3);
        int bitPos4 = ((Integer) v4.get(0)).intValue();
        double x2 = ((Double) v4.get(1)).doubleValue();
        Vector v5 = DwgUtil.getRawDouble(data, bitPos4);
        int bitPos5 = ((Integer) v5.get(0)).intValue();
        this.textMidpoint = new Point2D(x2, ((Double) v5.get(1)).doubleValue());
        Vector v6 = DwgUtil.getBitDouble(data, bitPos5);
        int bitPos6 = ((Integer) v6.get(0)).intValue();
        this.elevation = ((Double) v6.get(1)).doubleValue();
        Vector v7 = DwgUtil.getRawChar(data, bitPos6);
        int bitPos7 = ((Integer) v7.get(0)).intValue();
        this.flags = ((Integer) v7.get(1)).intValue();
        Vector v8 = DwgUtil.getTextString(data, bitPos7);
        int bitPos8 = ((Integer) v8.get(0)).intValue();
        this.text = (String) v8.get(1);
        Vector v9 = DwgUtil.getBitDouble(data, bitPos8);
        int bitPos9 = ((Integer) v9.get(0)).intValue();
        this.rotation = ((Double) v9.get(1)).doubleValue();
        Vector v10 = DwgUtil.getBitDouble(data, bitPos9);
        int bitPos10 = ((Integer) v10.get(0)).intValue();
        this.horizDir = ((Double) v10.get(1)).doubleValue();
        Vector v11 = DwgUtil.getBitDouble(data, bitPos10);
        int bitPos11 = ((Integer) v11.get(0)).intValue();
        double x3 = ((Double) v11.get(1)).doubleValue();
        Vector v12 = DwgUtil.getBitDouble(data, bitPos11);
        int bitPos12 = ((Integer) v12.get(0)).intValue();
        double y2 = ((Double) v12.get(1)).doubleValue();
        Vector v13 = DwgUtil.getBitDouble(data, bitPos12);
        int bitPos13 = ((Integer) v13.get(0)).intValue();
        this.insScale = new double[]{x3, y2, ((Double) v13.get(1)).doubleValue()};
        Vector v14 = DwgUtil.getBitDouble(data, bitPos13);
        int bitPos14 = ((Integer) v14.get(0)).intValue();
        this.insRotation = ((Double) v14.get(1)).doubleValue();
        Vector v15 = DwgUtil.getBitShort(data, bitPos14);
        int bitPos15 = ((Integer) v15.get(0)).intValue();
        this.attachmentPoint = ((Integer) v15.get(1)).intValue();
        Vector v16 = DwgUtil.getBitShort(data, bitPos15);
        int bitPos16 = ((Integer) v16.get(0)).intValue();
        this.linespaceStyle = ((Integer) v16.get(1)).intValue();
        Vector v17 = DwgUtil.getBitDouble(data, bitPos16);
        int bitPos17 = ((Integer) v17.get(0)).intValue();
        this.linespaceFactor = ((Double) v17.get(1)).doubleValue();
        Vector v18 = DwgUtil.getBitDouble(data, bitPos17);
        int bitPos18 = ((Integer) v18.get(0)).intValue();
        this.actualMeasurement = ((Double) v18.get(1)).doubleValue();
        Vector v19 = DwgUtil.getRawDouble(data, bitPos18);
        int bitPos19 = ((Integer) v19.get(0)).intValue();
        double x4 = ((Double) v19.get(1)).doubleValue();
        Vector v20 = DwgUtil.getRawDouble(data, bitPos19);
        int bitPos20 = ((Integer) v20.get(0)).intValue();
        ((Double) v20.get(1)).doubleValue();
        this.pt12 = new Point2D(x4, y2);
        Vector v21 = DwgUtil.getBitDouble(data, bitPos20);
        int bitPos21 = ((Integer) v21.get(0)).intValue();
        double x5 = ((Double) v21.get(1)).doubleValue();
        Vector v22 = DwgUtil.getBitDouble(data, bitPos21);
        int bitPos22 = ((Integer) v22.get(0)).intValue();
        double y3 = ((Double) v22.get(1)).doubleValue();
        Vector v23 = DwgUtil.getBitDouble(data, bitPos22);
        int bitPos23 = ((Integer) v23.get(0)).intValue();
        this.pt10 = new double[]{x5, y3, ((Double) v23.get(1)).doubleValue()};
        Vector v24 = DwgUtil.getBitDouble(data, bitPos23);
        int bitPos24 = ((Integer) v24.get(0)).intValue();
        double x6 = ((Double) v24.get(1)).doubleValue();
        Vector v25 = DwgUtil.getBitDouble(data, bitPos24);
        int bitPos25 = ((Integer) v25.get(0)).intValue();
        double y4 = ((Double) v25.get(1)).doubleValue();
        Vector v26 = DwgUtil.getBitDouble(data, bitPos25);
        int bitPos26 = ((Integer) v26.get(0)).intValue();
        this.pt13 = new double[]{x6, y4, ((Double) v26.get(1)).doubleValue()};
        Vector v27 = DwgUtil.getBitDouble(data, bitPos26);
        int bitPos27 = ((Integer) v27.get(0)).intValue();
        double x7 = ((Double) v27.get(1)).doubleValue();
        Vector v28 = DwgUtil.getBitDouble(data, bitPos27);
        int bitPos28 = ((Integer) v28.get(0)).intValue();
        double y5 = ((Double) v28.get(1)).doubleValue();
        Vector v29 = DwgUtil.getBitDouble(data, bitPos28);
        int bitPos29 = ((Integer) v29.get(0)).intValue();
        this.pt14 = new double[]{x7, y5, ((Double) v29.get(1)).doubleValue()};
        Vector v30 = DwgUtil.getBitDouble(data, bitPos29);
        int bitPos30 = ((Integer) v30.get(0)).intValue();
        this.extRotation = ((Double) v30.get(1)).doubleValue();
        Vector v31 = DwgUtil.getBitDouble(data, bitPos30);
        int bitPos31 = ((Integer) v31.get(0)).intValue();
        this.dimensionRotation = ((Double) v31.get(1)).doubleValue();
        Vector v32 = DwgUtil.getHandle(data, readObjectTailV15(data, bitPos31));
        int bitPos32 = ((Integer) v32.get(0)).intValue();
        int[] handle = new int[(v32.size() - 1)];
        for (int i = 1; i < v32.size(); i++) {
            handle[i - 1] = ((Integer) v32.get(i)).intValue();
        }
        Vector handleVect = new Vector();
        for (int i2 : handle) {
            handleVect.add(new Integer(i2));
        }
        this.dimstyleHandle = DwgUtil.handleBinToHandleInt(handleVect);
        Vector v33 = DwgUtil.getHandle(data, bitPos32);
        ((Integer) v33.get(0)).intValue();
        int[] handle2 = new int[(v33.size() - 1)];
        for (int i3 = 1; i3 < v33.size(); i3++) {
            handle2[i3 - 1] = ((Integer) v33.get(i3)).intValue();
        }
        Vector handleVect2 = new Vector();
        for (int i4 : handle2) {
            handleVect2.add(new Integer(i4));
        }
        this.anonBlockHandle = DwgUtil.handleBinToHandleInt(handleVect2);
    }

    public double getElevation() {
        return this.elevation;
    }

    public void setElevation(double elevation2) {
        this.elevation = elevation2;
    }

    public Object clone() {
        DwgLinearDimension dwgLinearDimension = new DwgLinearDimension();
        dwgLinearDimension.setType(this.type);
        dwgLinearDimension.setHandle(this.handle);
        dwgLinearDimension.setVersion(this.version);
        dwgLinearDimension.setMode(this.mode);
        dwgLinearDimension.setLayerHandle(this.layerHandle);
        dwgLinearDimension.setColor(this.color);
        dwgLinearDimension.setNumReactors(this.numReactors);
        dwgLinearDimension.setNoLinks(this.noLinks);
        dwgLinearDimension.setLinetypeFlags(this.linetypeFlags);
        dwgLinearDimension.setPlotstyleFlags(this.plotstyleFlags);
        dwgLinearDimension.setSizeInBits(this.sizeInBits);
        dwgLinearDimension.setExtendedData(this.extendedData);
        dwgLinearDimension.setGraphicData(this.graphicData);
        dwgLinearDimension.setTextMidpoint(this.textMidpoint);
        dwgLinearDimension.setElevation(this.elevation);
        dwgLinearDimension.setFlags(this.flags);
        dwgLinearDimension.setText(this.text);
        dwgLinearDimension.setRotation(this.rotation);
        dwgLinearDimension.setHorizDir(this.horizDir);
        dwgLinearDimension.setInsScale(this.insScale);
        dwgLinearDimension.setInsRotation(this.insRotation);
        dwgLinearDimension.setAttachmentPoint(this.attachmentPoint);
        dwgLinearDimension.setLinespaceStyle(this.linespaceStyle);
        dwgLinearDimension.setLinespaceFactor(this.linespaceFactor);
        dwgLinearDimension.setActualMeasurement(this.actualMeasurement);
        dwgLinearDimension.setPt12(this.pt12);
        dwgLinearDimension.setPt10(this.pt10);
        dwgLinearDimension.setPt13(this.pt13);
        dwgLinearDimension.setPt14(this.pt14);
        dwgLinearDimension.setExtRotation(this.extRotation);
        dwgLinearDimension.setDimensionRotation(this.dimensionRotation);
        dwgLinearDimension.setDimstyleHandle(this.dimstyleHandle);
        dwgLinearDimension.setAnonBlockHandle(this.anonBlockHandle);
        return dwgLinearDimension;
    }

    public double getActualMeasurement() {
        return this.actualMeasurement;
    }

    public void setActualMeasurement(double actualMeasurement2) {
        this.actualMeasurement = actualMeasurement2;
    }

    public int getAnonBlockHandle() {
        return this.anonBlockHandle;
    }

    public void setAnonBlockHandle(int anonBlockHandle2) {
        this.anonBlockHandle = anonBlockHandle2;
    }

    public int getAttachmentPoint() {
        return this.attachmentPoint;
    }

    public void setAttachmentPoint(int attachmentPoint2) {
        this.attachmentPoint = attachmentPoint2;
    }

    public double getDimensionRotation() {
        return this.dimensionRotation;
    }

    public void setDimensionRotation(double dimensionRotation2) {
        this.dimensionRotation = dimensionRotation2;
    }

    public int getDimstyleHandle() {
        return this.dimstyleHandle;
    }

    public void setDimstyleHandle(int dimstyleHandle2) {
        this.dimstyleHandle = dimstyleHandle2;
    }

    public double getExtRotation() {
        return this.extRotation;
    }

    public void setExtRotation(double extRotation2) {
        this.extRotation = extRotation2;
    }

    public double[] getExtrusion() {
        return this.extrusion;
    }

    public void setExtrusion(double[] extrusion2) {
        this.extrusion = extrusion2;
    }

    public int getFlags() {
        return this.flags;
    }

    public void setFlags(int flags2) {
        this.flags = flags2;
    }

    public double getHorizDir() {
        return this.horizDir;
    }

    public void setHorizDir(double horizDir2) {
        this.horizDir = horizDir2;
    }

    public double getInsRotation() {
        return this.insRotation;
    }

    public void setInsRotation(double insRotation2) {
        this.insRotation = insRotation2;
    }

    public double[] getInsScale() {
        return this.insScale;
    }

    public void setInsScale(double[] insScale2) {
        this.insScale = insScale2;
    }

    public double getLinespaceFactor() {
        return this.linespaceFactor;
    }

    public void setLinespaceFactor(double linespaceFactor2) {
        this.linespaceFactor = linespaceFactor2;
    }

    public int getLinespaceStyle() {
        return this.linespaceStyle;
    }

    public void setLinespaceStyle(int linespaceStyle2) {
        this.linespaceStyle = linespaceStyle2;
    }

    public double[] getPt10() {
        return this.pt10;
    }

    public void setPt10(double[] pt102) {
        this.pt10 = pt102;
    }

    public Point2D getPt12() {
        return this.pt12;
    }

    public void setPt12(Point2D pt122) {
        this.pt12 = pt122;
    }

    public double[] getPt13() {
        return this.pt13;
    }

    public void setPt13(double[] pt132) {
        this.pt13 = pt132;
    }

    public double[] getPt14() {
        return this.pt14;
    }

    public void setPt14(double[] pt142) {
        this.pt14 = pt142;
    }

    public double getRotation() {
        return this.rotation;
    }

    public void setRotation(double rotation2) {
        this.rotation = rotation2;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text2) {
        this.text = text2;
    }

    public Point2D getTextMidpoint() {
        return this.textMidpoint;
    }

    public void setTextMidpoint(Point2D textMidpoint2) {
        this.textMidpoint = textMidpoint2;
    }
}
