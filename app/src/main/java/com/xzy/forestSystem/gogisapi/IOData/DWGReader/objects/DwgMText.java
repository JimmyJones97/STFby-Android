package  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgObject;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.DwgUtil;
import java.util.Vector;

public class DwgMText extends DwgObject {
    private int attachment;
    private int drawingDir;
    private double extHeight;
    private double extWidth;
    private double[] extrusion;
    private double height;
    private double[] insertionPoint;
    private double lineSpacingFactor;
    private int lineSpacingStyle;
    private int styleHandle;
    private String text;
    private double width;
    private double[] xAxisDirection;

    public void readDwgMTextV15(int[] data, int offset) throws Exception {
        Vector v = DwgUtil.getBitDouble(data, readObjectHeaderV15(data, offset));
        int bitPos = ((Integer) v.get(0)).intValue();
        double x = ((Double) v.get(1)).doubleValue();
        Vector v2 = DwgUtil.getBitDouble(data, bitPos);
        int bitPos2 = ((Integer) v2.get(0)).intValue();
        double y = ((Double) v2.get(1)).doubleValue();
        Vector v3 = DwgUtil.getBitDouble(data, bitPos2);
        int bitPos3 = ((Integer) v3.get(0)).intValue();
        this.insertionPoint = new double[]{x, y, ((Double) v3.get(1)).doubleValue()};
        Vector v4 = DwgUtil.getBitDouble(data, bitPos3);
        int bitPos4 = ((Integer) v4.get(0)).intValue();
        double x2 = ((Double) v4.get(1)).doubleValue();
        Vector v5 = DwgUtil.getBitDouble(data, bitPos4);
        int bitPos5 = ((Integer) v5.get(0)).intValue();
        double y2 = ((Double) v5.get(1)).doubleValue();
        Vector v6 = DwgUtil.getBitDouble(data, bitPos5);
        int bitPos6 = ((Integer) v6.get(0)).intValue();
        this.extrusion = new double[]{x2, y2, ((Double) v6.get(1)).doubleValue()};
        Vector v7 = DwgUtil.getBitDouble(data, bitPos6);
        int bitPos7 = ((Integer) v7.get(0)).intValue();
        double x3 = ((Double) v7.get(1)).doubleValue();
        Vector v8 = DwgUtil.getBitDouble(data, bitPos7);
        int bitPos8 = ((Integer) v8.get(0)).intValue();
        double y3 = ((Double) v8.get(1)).doubleValue();
        Vector v9 = DwgUtil.getBitDouble(data, bitPos8);
        int bitPos9 = ((Integer) v9.get(0)).intValue();
        this.xAxisDirection = new double[]{x3, y3, ((Double) v9.get(1)).doubleValue()};
        Vector v10 = DwgUtil.getBitDouble(data, bitPos9);
        int bitPos10 = ((Integer) v10.get(0)).intValue();
        this.width = ((Double) v10.get(1)).doubleValue();
        Vector v11 = DwgUtil.getBitDouble(data, bitPos10);
        int bitPos11 = ((Integer) v11.get(0)).intValue();
        this.height = ((Double) v11.get(1)).doubleValue();
        Vector v12 = DwgUtil.getBitShort(data, bitPos11);
        int bitPos12 = ((Integer) v12.get(0)).intValue();
        this.attachment = ((Integer) v12.get(1)).intValue();
        Vector v13 = DwgUtil.getBitShort(data, bitPos12);
        int bitPos13 = ((Integer) v13.get(0)).intValue();
        this.drawingDir = ((Integer) v13.get(1)).intValue();
        Vector v14 = DwgUtil.getBitDouble(data, bitPos13);
        int bitPos14 = ((Integer) v14.get(0)).intValue();
        this.extHeight = ((Double) v14.get(1)).doubleValue();
        Vector v15 = DwgUtil.getBitDouble(data, bitPos14);
        int bitPos15 = ((Integer) v15.get(0)).intValue();
        this.extWidth = ((Double) v15.get(1)).doubleValue();
        Vector v16 = DwgUtil.getTextString(data, bitPos15);
        int bitPos16 = ((Integer) v16.get(0)).intValue();
        this.text = new String(((String) v16.get(1)).getBytes(), "utf-8");
        Vector v17 = DwgUtil.getBitShort(data, bitPos16);
        int bitPos17 = ((Integer) v17.get(0)).intValue();
        this.lineSpacingStyle = ((Integer) v17.get(1)).intValue();
        Vector v18 = DwgUtil.getBitDouble(data, bitPos17);
        int bitPos18 = ((Integer) v18.get(0)).intValue();
        this.lineSpacingFactor = ((Double) v18.get(1)).doubleValue();
        Vector v19 = DwgUtil.testBit(data, bitPos18);
        int bitPos19 = ((Integer) v19.get(0)).intValue();
        ((Boolean) v19.get(1)).booleanValue();
        Vector v20 = DwgUtil.getHandle(data, readObjectTailV15(data, bitPos19));
        ((Integer) v20.get(0)).intValue();
        int[] handle = new int[(v20.size() - 1)];
        for (int j = 1; j < v20.size(); j++) {
            handle[j - 1] = ((Integer) v20.get(j)).intValue();
        }
        Vector handleVect = new Vector();
        for (int i : handle) {
            handleVect.add(new Integer(i));
        }
        this.styleHandle = DwgUtil.handleBinToHandleInt(handleVect);
    }

    public double getHeight() {
        return this.height;
    }

    public void setHeight(double height2) {
        this.height = height2;
    }

    public double[] getInsertionPoint() {
        return this.insertionPoint;
    }

    public void setInsertionPoint(double[] insertionPoint2) {
        this.insertionPoint = insertionPoint2;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text2) {
        this.text = text2;
    }

    public double getWidth() {
        return this.width;
    }

    public void setWidth(double width2) {
        this.width = width2;
    }

    public double[] getExtrusion() {
        return this.extrusion;
    }

    public Object clone() {
        DwgMText dwgMText = new DwgMText();
        dwgMText.setType(this.type);
        dwgMText.setHandle(this.handle);
        dwgMText.setVersion(this.version);
        dwgMText.setMode(this.mode);
        dwgMText.setLayerHandle(this.layerHandle);
        dwgMText.setColor(this.color);
        dwgMText.setNumReactors(this.numReactors);
        dwgMText.setNoLinks(this.noLinks);
        dwgMText.setLinetypeFlags(this.linetypeFlags);
        dwgMText.setPlotstyleFlags(this.plotstyleFlags);
        dwgMText.setSizeInBits(this.sizeInBits);
        dwgMText.setExtendedData(this.extendedData);
        dwgMText.setGraphicData(this.graphicData);
        dwgMText.setInsertionPoint(this.insertionPoint);
        dwgMText.setXAxisDirection(this.xAxisDirection);
        dwgMText.setExtrusion(this.extrusion);
        dwgMText.setWidth(this.width);
        dwgMText.setHeight(this.height);
        dwgMText.setAttachment(this.attachment);
        dwgMText.setDrawingDir(this.drawingDir);
        dwgMText.setExtHeight(this.extHeight);
        dwgMText.setExtWidth(this.extWidth);
        dwgMText.setText(this.text);
        dwgMText.setLineSpacingStyle(this.lineSpacingStyle);
        dwgMText.setLineSpacingFactor(this.lineSpacingFactor);
        dwgMText.setStyleHandle(this.styleHandle);
        return dwgMText;
    }

    public int getAttachment() {
        return this.attachment;
    }

    public void setAttachment(int attachment2) {
        this.attachment = attachment2;
    }

    public int getDrawingDir() {
        return this.drawingDir;
    }

    public void setDrawingDir(int drawingDir2) {
        this.drawingDir = drawingDir2;
    }

    public double getExtHeight() {
        return this.extHeight;
    }

    public void setExtHeight(double extHeight2) {
        this.extHeight = extHeight2;
    }

    public double getExtWidth() {
        return this.extWidth;
    }

    public void setExtWidth(double extWidth2) {
        this.extWidth = extWidth2;
    }

    public double getLineSpacingFactor() {
        return this.lineSpacingFactor;
    }

    public void setLineSpacingFactor(double lineSpacingFactor2) {
        this.lineSpacingFactor = lineSpacingFactor2;
    }

    public int getLineSpacingStyle() {
        return this.lineSpacingStyle;
    }

    public void setLineSpacingStyle(int lineSpacingStyle2) {
        this.lineSpacingStyle = lineSpacingStyle2;
    }

    public int getStyleHandle() {
        return this.styleHandle;
    }

    public void setStyleHandle(int styleHandle2) {
        this.styleHandle = styleHandle2;
    }

    public double[] getXAxisDirection() {
        return this.xAxisDirection;
    }

    public void setXAxisDirection(double[] axisDirection) {
        this.xAxisDirection = axisDirection;
    }

    public void setExtrusion(double[] extrusion2) {
        this.extrusion = extrusion2;
    }
}
