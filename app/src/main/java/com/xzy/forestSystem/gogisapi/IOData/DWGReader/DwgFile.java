package  com.xzy.forestSystem.gogisapi.IOData.DWGReader;

import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgArc;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgAttdef;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgAttrib;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgBlock;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgBlockControl;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgBlockHeader;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgCircle;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgEllipse;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgEndblk;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgInsert;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgLayer;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgLayerControl;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgLine;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgLinearDimension;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgLwPolyline;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgMText;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgPoint;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgPolyline2D;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgPolyline3D;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgSeqend;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgSolid;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgSpline;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgText;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgVertex2D;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.DwgVertex3D;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.objects.Point2D;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.util.AcadExtrusionCalculator;
import  com.xzy.forestSystem.gogisapi.IOData.DWGReader.util.GisModelCurveCalculator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Vector;

public class DwgFile {
    private boolean dwg3DFile;
    private Vector dwgClasses = new Vector();
    private Vector dwgObjectOffsets = new Vector();
    private Vector dwgObjects = new Vector();
    private DwgFileReader dwgReader;
    private Vector dwgSectionOffsets = new Vector();
    private String dwgVersion;
    private String fileName;
    private Vector layerTable;

    public DwgFile(String fileName2) {
        this.fileName = fileName2;
    }

    public boolean read(String[] outMsg) throws IOException {
        setDwgVersion();
        outMsg[0] = "";
        if (this.dwgVersion.equals("R15")) {
            this.dwgReader = new DwgFileV15Reader();
            return this.dwgReader.read(this);
        } else if (!this.dwgVersion.equals("Unknown")) {
            return false;
        } else {
            outMsg[0] = "不支持该版本DWG文件.";
            return false;
        }
    }

    public void applyExtrusions() {
        for (int i = 0; i < this.dwgObjects.size(); i++) {
            DwgObject dwgObject = (DwgObject) this.dwgObjects.get(i);
            if (dwgObject instanceof DwgArc) {
                ((DwgArc) dwgObject).setCenter(AcadExtrusionCalculator.CalculateAcadExtrusion(((DwgArc) dwgObject).getCenter(), ((DwgArc) dwgObject).getExtrusion()));
            } else if (!(dwgObject instanceof DwgAttdef)) {
                if (dwgObject instanceof DwgAttrib) {
                    Point2D attribInsertionPoint = ((DwgAttrib) dwgObject).getInsertionPoint();
                    double[] attribInsertionPoint3D = AcadExtrusionCalculator.CalculateAcadExtrusion(new double[]{attribInsertionPoint.getX(), attribInsertionPoint.getY(), ((DwgAttrib) dwgObject).getElevation()}, ((DwgAttrib) dwgObject).getExtrusion());
                    ((DwgAttrib) dwgObject).setInsertionPoint(new Point2D(attribInsertionPoint3D[0], attribInsertionPoint3D[1]));
                    ((DwgAttrib) dwgObject).setElevation(attribInsertionPoint3D[2]);
                } else if (!(dwgObject instanceof DwgBlock) && !(dwgObject instanceof DwgBlockControl) && !(dwgObject instanceof DwgBlockHeader)) {
                    if (dwgObject instanceof DwgCircle) {
                        ((DwgCircle) dwgObject).setCenter(AcadExtrusionCalculator.CalculateAcadExtrusion(((DwgCircle) dwgObject).getCenter(), ((DwgCircle) dwgObject).getExtrusion()));
                    } else if (dwgObject instanceof DwgInsert) {
                        ((DwgInsert) dwgObject).setInsertionPoint(AcadExtrusionCalculator.CalculateAcadExtrusion(((DwgInsert) dwgObject).getInsertionPoint(), ((DwgInsert) dwgObject).getExtrusion()));
                    } else if (!(dwgObject instanceof DwgLayer) && !(dwgObject instanceof DwgLayerControl)) {
                        if (dwgObject instanceof DwgLine) {
                            double[] lineP1 = ((DwgLine) dwgObject).getP1();
                            double[] lineP2 = ((DwgLine) dwgObject).getP2();
                            if (((DwgLine) dwgObject).isZflag()) {
                                double[] lineP12 = {lineP1[0], lineP1[1], 0.0d};
                                lineP2 = new double[]{lineP2[0], lineP2[1], 0.0d};
                                lineP1 = lineP12;
                            }
                            double[] lineExt = ((DwgLine) dwgObject).getExtrusion();
                            double[] lineP13 = AcadExtrusionCalculator.CalculateAcadExtrusion(lineP1, lineExt);
                            double[] lineP22 = AcadExtrusionCalculator.CalculateAcadExtrusion(lineP2, lineExt);
                            ((DwgLine) dwgObject).setP1(lineP13);
                            ((DwgLine) dwgObject).setP2(lineP22);
                        } else if (!(dwgObject instanceof DwgLinearDimension)) {
                            if ((dwgObject instanceof DwgLwPolyline) && ((DwgLwPolyline) dwgObject).getVertices() != null) {
                                Point2D[] vertices = ((DwgLwPolyline) dwgObject).getVertices();
                                double[] lwPolylineExt = ((DwgLwPolyline) dwgObject).getNormal();
                                if (lwPolylineExt[0] == 0.0d && lwPolylineExt[1] == 0.0d && lwPolylineExt[2] == 0.0d) {
                                    lwPolylineExt[2] = 1.0d;
                                }
                                double elev = ((DwgLwPolyline) dwgObject).getElevation();
                                double[][] lwPolylinePoints3D = (double[][]) Array.newInstance(Double.TYPE, vertices.length, 3);
                                for (int j = 0; j < vertices.length; j++) {
                                    lwPolylinePoints3D[j][0] = vertices[j].getX();
                                    lwPolylinePoints3D[j][1] = vertices[j].getY();
                                    lwPolylinePoints3D[j][2] = elev;
                                    lwPolylinePoints3D[j] = AcadExtrusionCalculator.CalculateAcadExtrusion(lwPolylinePoints3D[j], lwPolylineExt);
                                }
                                ((DwgLwPolyline) dwgObject).setElevation(elev);
                                for (int j2 = 0; j2 < vertices.length; j2++) {
                                    vertices[j2] = new Point2D(lwPolylinePoints3D[j2][0], lwPolylinePoints3D[j2][1]);
                                }
                                ((DwgLwPolyline) dwgObject).setVertices(vertices);
                            } else if (dwgObject instanceof DwgMText) {
                                ((DwgMText) dwgObject).setInsertionPoint(AcadExtrusionCalculator.CalculateAcadExtrusion(((DwgMText) dwgObject).getInsertionPoint(), ((DwgMText) dwgObject).getExtrusion()));
                            } else if (dwgObject instanceof DwgPoint) {
                                ((DwgPoint) dwgObject).setPoint(AcadExtrusionCalculator.CalculateAcadExtrusion(((DwgPoint) dwgObject).getPoint(), ((DwgPoint) dwgObject).getExtrusion()));
                            } else if (dwgObject instanceof DwgSolid) {
                                double[] corner1 = ((DwgSolid) dwgObject).getCorner1();
                                double[] corner2 = ((DwgSolid) dwgObject).getCorner2();
                                double[] corner3 = ((DwgSolid) dwgObject).getCorner3();
                                double[] corner4 = ((DwgSolid) dwgObject).getCorner4();
                                ((DwgSolid) dwgObject).setCorner1(AcadExtrusionCalculator.CalculateAcadExtrusion(corner1, ((DwgSolid) dwgObject).getExtrusion()));
                                ((DwgSolid) dwgObject).setCorner2(corner2);
                                ((DwgSolid) dwgObject).setCorner3(corner3);
                                ((DwgSolid) dwgObject).setCorner4(corner4);
                            } else if (!(dwgObject instanceof DwgSpline)) {
                                if (dwgObject instanceof DwgText) {
                                    Point2D tpoint = ((DwgText) dwgObject).getInsertionPoint();
                                    double elev2 = ((DwgText) dwgObject).getElevation();
                                    double[] textPoint = AcadExtrusionCalculator.CalculateAcadExtrusion(new double[]{tpoint.getX(), tpoint.getY(), elev2}, ((DwgText) dwgObject).getExtrusion());
                                    ((DwgText) dwgObject).setInsertionPoint(new Point2D(textPoint[0], textPoint[1]));
                                    ((DwgText) dwgObject).setElevation(elev2);
                                } else if ((dwgObject instanceof DwgPolyline2D) && ((DwgPolyline2D) dwgObject).getPts() != null) {
                                    Point2D[] vertices2 = ((DwgPolyline2D) dwgObject).getPts();
                                    double[] polyline2DExt = ((DwgPolyline2D) dwgObject).getExtrusion();
                                    double elev3 = ((DwgPolyline2D) dwgObject).getElevation();
                                    double[][] polylinePoints3D = (double[][]) Array.newInstance(Double.TYPE, vertices2.length, 3);
                                    for (int j3 = 0; j3 < vertices2.length; j3++) {
                                        polylinePoints3D[j3][0] = vertices2[j3].getX();
                                        polylinePoints3D[j3][1] = vertices2[j3].getY();
                                        polylinePoints3D[j3][2] = elev3;
                                        polylinePoints3D[j3] = AcadExtrusionCalculator.CalculateAcadExtrusion(polylinePoints3D[j3], polyline2DExt);
                                    }
                                    ((DwgPolyline2D) dwgObject).setElevation(elev3);
                                    for (int j4 = 0; j4 < vertices2.length; j4++) {
                                        vertices2[j4] = new Point2D(polylinePoints3D[j4][0], polylinePoints3D[j4][1]);
                                    }
                                    ((DwgPolyline2D) dwgObject).setPts(vertices2);
                                } else if (!(dwgObject instanceof DwgPolyline3D) && !(dwgObject instanceof DwgVertex2D)) {
                                    boolean z = dwgObject instanceof DwgVertex3D;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void calculateGisModelDwgPolylines() {
        for (int i = 0; i < this.dwgObjects.size(); i++) {
            DwgObject pol = (DwgObject) this.dwgObjects.get(i);
            if (pol instanceof DwgPolyline2D) {
                int flags = ((DwgPolyline2D) pol).getFlags();
                int firstHandle = ((DwgPolyline2D) pol).getFirstVertexHandle();
                int lastHandle = ((DwgPolyline2D) pol).getLastVertexHandle();
                Vector pts = new Vector();
                Vector bulges = new Vector();
                double[] dArr = new double[3];
                for (int j = 0; j < this.dwgObjects.size(); j++) {
                    DwgObject firstVertex = (DwgObject) this.dwgObjects.get(j);
                    if ((firstVertex instanceof DwgVertex2D) && firstVertex.getHandle() == firstHandle) {
                        int k = 0;
                        while (true) {
                            DwgObject vertex = (DwgObject) this.dwgObjects.get(j + k);
                            int vHandle = vertex.getHandle();
                            if (vertex instanceof DwgVertex2D) {
                                double[] pt = ((DwgVertex2D) vertex).getPoint();
                                pts.add(new Point2D(pt[0], pt[1]));
                                bulges.add(new Double(((DwgVertex2D) vertex).getBulge()));
                                k++;
                                if (vHandle == lastHandle && (vertex instanceof DwgVertex2D)) {
                                    break;
                                }
                            } else if (vertex instanceof DwgSeqend) {
                                break;
                            }
                        }
                    }
                }
                if (pts.size() > 0) {
                    Point2D[] newPts = new Point2D[pts.size()];
                    if ((flags & 1) == 1) {
                        newPts = new Point2D[(pts.size() + 1)];
                        for (int j2 = 0; j2 < pts.size(); j2++) {
                            newPts[j2] = (Point2D) pts.get(j2);
                        }
                        newPts[pts.size()] = (Point2D) pts.get(0);
                        bulges.add(new Double(0.0d));
                    } else {
                        for (int j3 = 0; j3 < pts.size(); j3++) {
                            newPts[j3] = (Point2D) pts.get(j3);
                        }
                    }
                    double[] bs = new double[bulges.size()];
                    for (int j4 = 0; j4 < bulges.size(); j4++) {
                        bs[j4] = ((Double) bulges.get(j4)).doubleValue();
                    }
                    ((DwgPolyline2D) pol).setBulges(bs);
                    ((DwgPolyline2D) pol).setPts(GisModelCurveCalculator.calculateGisModelBulge(newPts, bs));
                }
            } else if (pol instanceof DwgPolyline3D) {
                int closedFlags = ((DwgPolyline3D) pol).getClosedFlags();
                int firstHandle2 = ((DwgPolyline3D) pol).getFirstVertexHandle();
                int lastHandle2 = ((DwgPolyline3D) pol).getLastVertexHandle();
                Vector pts2 = new Vector();
                double[] dArr2 = new double[3];
                for (int j5 = 0; j5 < this.dwgObjects.size(); j5++) {
                    DwgObject firstVertex2 = (DwgObject) this.dwgObjects.get(j5);
                    if ((firstVertex2 instanceof DwgVertex3D) && firstVertex2.getHandle() == firstHandle2) {
                        int k2 = 0;
                        while (true) {
                            DwgObject vertex2 = (DwgObject) this.dwgObjects.get(j5 + k2);
                            int vHandle2 = vertex2.getHandle();
                            if (vertex2 instanceof DwgVertex3D) {
                                double[] pt2 = ((DwgVertex3D) vertex2).getPoint();
                                pts2.add(new double[]{pt2[0], pt2[1], pt2[2]});
                                k2++;
                                if (vHandle2 == lastHandle2 && (vertex2 instanceof DwgVertex3D)) {
                                    break;
                                }
                            } else if (vertex2 instanceof DwgSeqend) {
                                break;
                            }
                        }
                    }
                }
                if (pts2.size() > 0) {
                    double[][] newPts2 = (double[][]) Array.newInstance(Double.TYPE, pts2.size(), 3);
                    if ((closedFlags & 1) == 1) {
                        newPts2 = (double[][]) Array.newInstance(Double.TYPE, pts2.size() + 1, 3);
                        for (int j6 = 0; j6 < pts2.size(); j6++) {
                            newPts2[j6][0] = ((double[]) pts2.get(j6))[0];
                            newPts2[j6][1] = ((double[]) pts2.get(j6))[1];
                            newPts2[j6][2] = ((double[]) pts2.get(j6))[2];
                        }
                        newPts2[pts2.size()][0] = ((double[]) pts2.get(0))[0];
                        newPts2[pts2.size()][1] = ((double[]) pts2.get(0))[1];
                        newPts2[pts2.size()][2] = ((double[]) pts2.get(0))[2];
                    } else {
                        for (int j7 = 0; j7 < pts2.size(); j7++) {
                            newPts2[j7][0] = ((double[]) pts2.get(j7))[0];
                            newPts2[j7][1] = ((double[]) pts2.get(j7))[1];
                            newPts2[j7][2] = ((double[]) pts2.get(j7))[2];
                        }
                    }
                    ((DwgPolyline3D) pol).setPts(newPts2);
                }
            } else if ((pol instanceof DwgLwPolyline) && ((DwgLwPolyline) pol).getVertices() != null) {
                int flags2 = ((DwgLwPolyline) pol).getFlag();
                Point2D[] pts3 = ((DwgLwPolyline) pol).getVertices();
                double[] bulges2 = ((DwgLwPolyline) pol).getBulges();
                Point2D[] newPts3 = new Point2D[pts3.length];
                double[] newBulges = new double[bulges2.length];
                if (flags2 == 512 || flags2 == 776 || flags2 == 768) {
                    newPts3 = new Point2D[(pts3.length + 1)];
                    newBulges = new double[(bulges2.length + 1)];
                    for (int j8 = 0; j8 < pts3.length; j8++) {
                        newPts3[j8] = pts3[j8];
                    }
                    newPts3[pts3.length] = pts3[0];
                    newBulges[pts3.length] = 0.0d;
                } else {
                    for (int j9 = 0; j9 < pts3.length; j9++) {
                        newPts3[j9] = pts3[j9];
                    }
                }
                if (pts3.length > 0) {
                    ((DwgLwPolyline) pol).setBulges(newBulges);
                    ((DwgLwPolyline) pol).setVertices(GisModelCurveCalculator.calculateGisModelBulge(newPts3, newBulges));
                }
            }
        }
    }

    public void calculateCadModelDwgPolylines() {
        for (int i = 0; i < this.dwgObjects.size(); i++) {
            DwgObject pol = (DwgObject) this.dwgObjects.get(i);
            if (pol instanceof DwgPolyline2D) {
                ((DwgPolyline2D) pol).getFlags();
                int firstHandle = ((DwgPolyline2D) pol).getFirstVertexHandle();
                int lastHandle = ((DwgPolyline2D) pol).getLastVertexHandle();
                Vector pts = new Vector();
                Vector bulges = new Vector();
                double[] dArr = new double[3];
                for (int j = 0; j < this.dwgObjects.size(); j++) {
                    DwgObject firstVertex = (DwgObject) this.dwgObjects.get(j);
                    if ((firstVertex instanceof DwgVertex2D) && firstVertex.getHandle() == firstHandle) {
                        int k = 0;
                        while (true) {
                            DwgObject vertex = (DwgObject) this.dwgObjects.get(j + k);
                            int vHandle = vertex.getHandle();
                            if (vertex instanceof DwgVertex2D) {
                                double[] pt = ((DwgVertex2D) vertex).getPoint();
                                pts.add(new Point2D(pt[0], pt[1]));
                                bulges.add(new Double(((DwgVertex2D) vertex).getBulge()));
                                k++;
                                if (vHandle == lastHandle && (vertex instanceof DwgVertex2D)) {
                                    break;
                                }
                            } else if (vertex instanceof DwgSeqend) {
                                break;
                            }
                        }
                    }
                }
                if (pts.size() > 0) {
                    double[] bs = new double[bulges.size()];
                    for (int j2 = 0; j2 < bulges.size(); j2++) {
                        bs[j2] = ((Double) bulges.get(j2)).doubleValue();
                    }
                    ((DwgPolyline2D) pol).setBulges(bs);
                    Point2D[] points = new Point2D[pts.size()];
                    for (int j3 = 0; j3 < pts.size(); j3++) {
                        points[j3] = (Point2D) pts.get(j3);
                    }
                    ((DwgPolyline2D) pol).setPts(points);
                }
            } else if (!(pol instanceof DwgPolyline3D) && (pol instanceof DwgLwPolyline)) {
                ((DwgLwPolyline) pol).getVertices();
            }
        }
    }

    public void blockManagement() {
        Vector dwgObjectsWithoutBlocks = new Vector();
        boolean addingToBlock = false;
        for (int i = 0; i < this.dwgObjects.size(); i++) {
            DwgObject entity = (DwgObject) this.dwgObjects.get(i);
            if ((entity instanceof DwgArc) && !addingToBlock) {
                dwgObjectsWithoutBlocks.add(entity);
            } else if ((entity instanceof DwgEllipse) && !addingToBlock) {
                dwgObjectsWithoutBlocks.add(entity);
            } else if ((entity instanceof DwgCircle) && !addingToBlock) {
                dwgObjectsWithoutBlocks.add(entity);
            } else if ((entity instanceof DwgPolyline2D) && !addingToBlock) {
                dwgObjectsWithoutBlocks.add(entity);
            } else if ((entity instanceof DwgPolyline3D) && !addingToBlock) {
                dwgObjectsWithoutBlocks.add(entity);
            } else if ((entity instanceof DwgLwPolyline) && !addingToBlock) {
                dwgObjectsWithoutBlocks.add(entity);
            } else if ((entity instanceof DwgSolid) && !addingToBlock) {
                dwgObjectsWithoutBlocks.add(entity);
            } else if ((entity instanceof DwgLine) && !addingToBlock) {
                dwgObjectsWithoutBlocks.add(entity);
            } else if ((entity instanceof DwgPoint) && !addingToBlock) {
                dwgObjectsWithoutBlocks.add(entity);
            } else if ((entity instanceof DwgMText) && !addingToBlock) {
                dwgObjectsWithoutBlocks.add(entity);
            } else if ((entity instanceof DwgText) && !addingToBlock) {
                dwgObjectsWithoutBlocks.add(entity);
            } else if ((entity instanceof DwgAttrib) && !addingToBlock) {
                dwgObjectsWithoutBlocks.add(entity);
            } else if ((entity instanceof DwgAttdef) && !addingToBlock) {
                dwgObjectsWithoutBlocks.add(entity);
            } else if (entity instanceof DwgBlock) {
                addingToBlock = true;
            } else if (entity instanceof DwgEndblk) {
                addingToBlock = false;
            } else if (entity instanceof DwgBlockHeader) {
                addingToBlock = true;
            } else if ((entity instanceof DwgInsert) && !addingToBlock) {
                double[] p = ((DwgInsert) entity).getInsertionPoint();
                manageInsert(new Point2D(p[0], p[1]), ((DwgInsert) entity).getScale(), ((DwgInsert) entity).getRotation(), ((DwgInsert) entity).getBlockHeaderHandle(), i, dwgObjectsWithoutBlocks);
            }
        }
        this.dwgObjects = dwgObjectsWithoutBlocks;
    }

    private void manageInsert(Point2D insPoint, double[] scale, double rot, int bHandle, int id, Vector dwgObjectsWithoutBlocks) {
        int iObjHandle;
        for (int i = 0; i < this.dwgObjects.size(); i++) {
            DwgObject obj = (DwgObject) this.dwgObjects.get(i);
            if ((obj instanceof DwgBlockHeader) && ((DwgBlockHeader) obj).getHandle() == bHandle) {
                double[] bPoint = ((DwgBlockHeader) obj).getBasePoint();
                String bname = ((DwgBlockHeader) obj).getName();
                if (!bname.startsWith("*")) {
                    int firstObjectHandle = ((DwgBlockHeader) obj).getFirstEntityHandle();
                    int lastObjectHandle = ((DwgBlockHeader) obj).getLastEntityHandle();
                    int j = 0;
                    while (true) {
                        if (j >= this.dwgObjects.size()) {
                            break;
                        }
                        DwgObject ent = (DwgObject) this.dwgObjects.get(j);
                        if ((ent instanceof DwgBlock) && bname.equals(((DwgBlock) ent).getName())) {
                            DwgBlock block = (DwgBlock) ent;
                            break;
                        }
                        j++;
                    }
                    for (int j2 = 0; j2 < this.dwgObjects.size(); j2++) {
                        DwgObject fObj = (DwgObject) this.dwgObjects.get(j2);
                        if (fObj != null && fObj.getHandle() == firstObjectHandle) {
                            int k = 0;
                            do {
                                DwgObject iObj = (DwgObject) this.dwgObjects.get(j2 + k);
                                iObjHandle = iObj.getHandle();
                                manageBlockEntity(iObj, bPoint, insPoint, scale, rot, id, dwgObjectsWithoutBlocks);
                                k++;
                            } while (iObjHandle != lastObjectHandle);
                        }
                    }
                    return;
                }
            }
        }
    }

    private void manageBlockEntity(DwgObject entity, double[] bPoint, Point2D insPoint, double[] scale, double rot, int id, Vector dwgObjectsWithoutBlocks) {
        if (entity instanceof DwgArc) {
            new DwgArc();
            double[] center = ((DwgArc) entity).getCenter();
            Point2D pointAux = new Point2D(center[0] - bPoint[0], center[1] - bPoint[1]);
            double[] transformedCenter = {insPoint.getX() + (pointAux.getX() * scale[0] * Math.cos(rot)) + (pointAux.getY() * scale[1] * -1.0d * Math.sin(rot)), insPoint.getY() + (pointAux.getX() * scale[0] * Math.sin(rot)) + (pointAux.getY() * scale[1] * Math.cos(rot)), center[2] * scale[2]};
            double transformedRadius = ((DwgArc) entity).getRadius() * scale[0];
            double initAngle = ((DwgArc) entity).getInitAngle();
            double endAngle = ((DwgArc) entity).getEndAngle();
            double transformedInitAngle = initAngle + rot;
            if (transformedInitAngle < 0.0d) {
                transformedInitAngle += 6.283185307179586d;
            } else if (transformedInitAngle > 6.283185307179586d) {
                transformedInitAngle -= 6.283185307179586d;
            }
            double transformedEndAngle = endAngle + rot;
            if (transformedEndAngle < 0.0d) {
                transformedEndAngle += 6.283185307179586d;
            } else if (transformedEndAngle > 6.283185307179586d) {
                transformedEndAngle -= 6.283185307179586d;
            }
            DwgArc transformedEntity = (DwgArc) ((DwgArc) entity).clone();
            transformedEntity.setCenter(transformedCenter);
            transformedEntity.setRadius(transformedRadius);
            transformedEntity.setInitAngle(transformedInitAngle);
            transformedEntity.setEndAngle(transformedEndAngle);
            dwgObjectsWithoutBlocks.add(transformedEntity);
        } else if (entity instanceof DwgCircle) {
            new DwgCircle();
            double[] center2 = ((DwgCircle) entity).getCenter();
            Point2D pointAux2 = new Point2D(center2[0] - bPoint[0], center2[1] - bPoint[1]);
            double[] transformedCenter2 = {insPoint.getX() + (pointAux2.getX() * scale[0] * Math.cos(rot)) + (pointAux2.getY() * scale[1] * -1.0d * Math.sin(rot)), insPoint.getY() + (pointAux2.getX() * scale[0] * Math.sin(rot)) + (pointAux2.getY() * scale[1] * Math.cos(rot)), center2[2] * scale[2]};
            DwgCircle transformedEntity2 = (DwgCircle) ((DwgCircle) entity).clone();
            transformedEntity2.setCenter(transformedCenter2);
            transformedEntity2.setRadius(((DwgCircle) entity).getRadius() * scale[0]);
            dwgObjectsWithoutBlocks.add(transformedEntity2);
        } else if (entity instanceof DwgEllipse) {
            new DwgEllipse();
            double[] center3 = ((DwgEllipse) entity).getCenter();
            Point2D pointAux3 = new Point2D(center3[0] - bPoint[0], center3[1] - bPoint[1]);
            double[] transformedCenter3 = {insPoint.getX() + (pointAux3.getX() * scale[0] * Math.cos(rot)) + (pointAux3.getY() * scale[1] * -1.0d * Math.sin(rot)), insPoint.getY() + (pointAux3.getX() * scale[0] * Math.sin(rot)) + (pointAux3.getY() * scale[1] * Math.cos(rot)), center3[2] * scale[2]};
            double[] majorAxisVector = ((DwgEllipse) entity).getMajorAxisVector();
            double[] transformedMajorAxisVector = {majorAxisVector[0] * scale[0], majorAxisVector[1] * scale[1], majorAxisVector[2] * scale[2]};
            double axisRatio = ((DwgEllipse) entity).getAxisRatio();
            double initAngle2 = ((DwgEllipse) entity).getInitAngle();
            double endAngle2 = ((DwgEllipse) entity).getEndAngle();
            double transformedInitAngle2 = initAngle2 + rot;
            if (transformedInitAngle2 < 0.0d) {
                transformedInitAngle2 += 6.283185307179586d;
            } else if (transformedInitAngle2 > 6.283185307179586d) {
                transformedInitAngle2 -= 6.283185307179586d;
            }
            double transformedEndAngle2 = endAngle2 + rot;
            if (transformedEndAngle2 < 0.0d) {
                transformedEndAngle2 += 6.283185307179586d;
            } else if (transformedEndAngle2 > 6.283185307179586d) {
                transformedEndAngle2 -= 6.283185307179586d;
            }
            DwgEllipse transformedEntity3 = (DwgEllipse) ((DwgEllipse) entity).clone();
            transformedEntity3.setCenter(transformedCenter3);
            transformedEntity3.setMajorAxisVector(transformedMajorAxisVector);
            transformedEntity3.setAxisRatio(axisRatio);
            transformedEntity3.setInitAngle(transformedInitAngle2);
            transformedEntity3.setEndAngle(transformedEndAngle2);
            dwgObjectsWithoutBlocks.add(transformedEntity3);
        } else if (entity instanceof DwgLine) {
            new DwgLine();
            double[] p1 = ((DwgLine) entity).getP1();
            double[] p2 = ((DwgLine) entity).getP2();
            Point2D pointAux4 = new Point2D(p1[0] - bPoint[0], p1[1] - bPoint[1]);
            double laX = insPoint.getX() + (pointAux4.getX() * scale[0] * Math.cos(rot)) + (pointAux4.getY() * scale[1] * -1.0d * Math.sin(rot));
            double laY = insPoint.getY() + (pointAux4.getX() * scale[0] * Math.sin(rot)) + (pointAux4.getY() * scale[1] * Math.cos(rot));
            double[] transformedP1 = ((DwgLine) entity).isZflag() ? new double[]{laX, laY, p1[2] * scale[2]} : new double[]{laX, laY};
            Point2D pointAux5 = new Point2D(p2[0] - bPoint[0], p2[1] - bPoint[1]);
            double laX2 = insPoint.getX() + (pointAux5.getX() * scale[0] * Math.cos(rot)) + (pointAux5.getY() * scale[1] * -1.0d * Math.sin(rot));
            double laY2 = insPoint.getY() + (pointAux5.getX() * scale[0] * Math.sin(rot)) + (pointAux5.getY() * scale[1] * Math.cos(rot));
            double[] transformedP2 = ((DwgLine) entity).isZflag() ? new double[]{laX2, laY2, p2[2] * scale[2]} : new double[]{laX2, laY2};
            DwgLine transformedEntity4 = (DwgLine) ((DwgLine) entity).clone();
            transformedEntity4.setP1(transformedP1);
            transformedEntity4.setP2(transformedP2);
            dwgObjectsWithoutBlocks.add(transformedEntity4);
        } else if (entity instanceof DwgLwPolyline) {
            new DwgLwPolyline();
            Point2D[] vertices = ((DwgLwPolyline) entity).getVertices();
            if (vertices != null) {
                Point2D[] transformedVertices = new Point2D[vertices.length];
                for (int i = 0; i < vertices.length; i++) {
                    Point2D pointAux6 = new Point2D(vertices[i].getX() - bPoint[0], vertices[i].getY() - bPoint[1]);
                    transformedVertices[i] = new Point2D(insPoint.getX() + (pointAux6.getX() * scale[0] * Math.cos(rot)) + (pointAux6.getY() * scale[1] * -1.0d * Math.sin(rot)), insPoint.getY() + (pointAux6.getX() * scale[0] * Math.sin(rot)) + (pointAux6.getY() * scale[1] * Math.cos(rot)));
                }
                DwgLwPolyline transformedEntity5 = (DwgLwPolyline) ((DwgLwPolyline) entity).clone();
                transformedEntity5.setVertices(transformedVertices);
                transformedEntity5.setElevation(((DwgLwPolyline) entity).getElevation() * scale[2]);
                dwgObjectsWithoutBlocks.add(transformedEntity5);
            }
        } else if (!(entity instanceof DwgMText) && !(entity instanceof DwgPoint)) {
            if (entity instanceof DwgPolyline2D) {
                new DwgPolyline2D();
                Point2D[] vertices2 = ((DwgPolyline2D) entity).getPts();
                if (vertices2 != null) {
                    Point2D[] transformedVertices2 = new Point2D[vertices2.length];
                    for (int i2 = 0; i2 < vertices2.length; i2++) {
                        Point2D pointAux7 = new Point2D(vertices2[i2].getX() - bPoint[0], vertices2[i2].getY() - bPoint[1]);
                        transformedVertices2[i2] = new Point2D(insPoint.getX() + (pointAux7.getX() * scale[0] * Math.cos(rot)) + (pointAux7.getY() * scale[1] * -1.0d * Math.sin(rot)), insPoint.getY() + (pointAux7.getX() * scale[0] * Math.sin(rot)) + (pointAux7.getY() * scale[1] * Math.cos(rot)));
                    }
                    DwgPolyline2D transformedEntity6 = (DwgPolyline2D) ((DwgPolyline2D) entity).clone();
                    transformedEntity6.setPts(transformedVertices2);
                    transformedEntity6.setElevation(((DwgPolyline2D) entity).getElevation() * scale[2]);
                    dwgObjectsWithoutBlocks.add(transformedEntity6);
                }
            } else if (!(entity instanceof DwgPolyline3D)) {
                if (entity instanceof DwgSolid) {
                    new DwgSolid();
                    double[] corner1 = ((DwgSolid) entity).getCorner1();
                    double[] corner2 = ((DwgSolid) entity).getCorner2();
                    double[] corner3 = ((DwgSolid) entity).getCorner3();
                    double[] corner4 = ((DwgSolid) entity).getCorner4();
                    Point2D pointAux8 = new Point2D(corner1[0] - bPoint[0], corner1[1] - bPoint[1]);
                    double[] transformedP12 = {insPoint.getX() + (pointAux8.getX() * scale[0] * Math.cos(rot)) + (pointAux8.getY() * scale[1] * -1.0d * Math.sin(rot)), insPoint.getY() + (pointAux8.getX() * scale[0] * Math.sin(rot)) + (pointAux8.getY() * scale[1] * Math.cos(rot))};
                    Point2D pointAux9 = new Point2D(corner2[0] - bPoint[0], corner2[1] - bPoint[1]);
                    double[] transformedP22 = {insPoint.getX() + (pointAux9.getX() * scale[0] * Math.cos(rot)) + (pointAux9.getY() * scale[1] * -1.0d * Math.sin(rot)), insPoint.getY() + (pointAux9.getX() * scale[0] * Math.sin(rot)) + (pointAux9.getY() * scale[1] * Math.cos(rot))};
                    Point2D pointAux10 = new Point2D(corner3[0] - bPoint[0], corner3[1] - bPoint[1]);
                    double[] transformedP3 = {insPoint.getX() + (pointAux10.getX() * scale[0] * Math.cos(rot)) + (pointAux10.getY() * scale[1] * -1.0d * Math.sin(rot)), insPoint.getY() + (pointAux10.getX() * scale[0] * Math.sin(rot)) + (pointAux10.getY() * scale[1] * Math.cos(rot))};
                    Point2D pointAux11 = new Point2D(corner4[0] - bPoint[0], corner4[1] - bPoint[1]);
                    double[] transformedP4 = {insPoint.getX() + (pointAux11.getX() * scale[0] * Math.cos(rot)) + (pointAux11.getY() * scale[1] * -1.0d * Math.sin(rot)), insPoint.getY() + (pointAux11.getX() * scale[0] * Math.sin(rot)) + (pointAux11.getY() * scale[1] * Math.cos(rot))};
                    DwgSolid transformedEntity7 = (DwgSolid) ((DwgSolid) entity).clone();
                    transformedEntity7.setCorner1(transformedP12);
                    transformedEntity7.setCorner2(transformedP22);
                    transformedEntity7.setCorner3(transformedP3);
                    transformedEntity7.setCorner4(transformedP4);
                    transformedEntity7.setElevation(((DwgSolid) entity).getElevation() * scale[2]);
                    dwgObjectsWithoutBlocks.add(transformedEntity7);
                } else if (!(entity instanceof DwgSpline) && !(entity instanceof DwgText) && (entity instanceof DwgInsert)) {
                    new DwgInsert();
                    double[] p = ((DwgInsert) entity).getInsertionPoint();
                    Point2D point = new Point2D(p[0], p[1]);
                    double[] newScale = ((DwgInsert) entity).getScale();
                    double newRot = ((DwgInsert) entity).getRotation();
                    int newBlockHandle = ((DwgInsert) entity).getBlockHeaderHandle();
                    Point2D pointAux12 = new Point2D(point.getX() - bPoint[0], point.getY() - bPoint[1]);
                    double laX3 = insPoint.getX() + (pointAux12.getX() * scale[0] * Math.cos(rot)) + (pointAux12.getY() * scale[1] * -1.0d * Math.sin(rot));
                    double laY3 = insPoint.getY() + (pointAux12.getX() * scale[0] * Math.sin(rot)) + (pointAux12.getY() * scale[1] * Math.cos(rot));
                    double d = p[2] * scale[2];
                    Point2D newInsPoint = new Point2D(laX3, laY3);
                    double[] newScale2 = {scale[0] * newScale[0], scale[1] * newScale[1], scale[2] * newScale[2]};
                    double newRot2 = newRot + rot;
                    if (newRot2 < 0.0d) {
                        newRot2 += 6.283185307179586d;
                    } else if (newRot2 > 6.283185307179586d) {
                        newRot2 -= 6.283185307179586d;
                    }
                    manageInsert(newInsPoint, newScale2, newRot2, newBlockHandle, id, dwgObjectsWithoutBlocks);
                }
            }
        }
    }

    public void initializeLayerTable() {
        this.layerTable = new Vector();
        for (int i = 0; i < this.dwgObjects.size(); i++) {
            DwgObject obj = (DwgObject) this.dwgObjects.get(i);
            if (obj instanceof DwgLayer) {
                Vector layerTableRecord = new Vector();
                layerTableRecord.add(new Integer(obj.getHandle()));
                layerTableRecord.add(((DwgLayer) obj).getName());
                layerTableRecord.add(new Integer(((DwgLayer) obj).getColor()));
                this.layerTable.add(layerTableRecord);
            }
        }
    }

    public String getLayerName(DwgObject entity) {
        String layerName = "";
        int layer = entity.getLayerHandle();
        for (int j = 0; j < this.layerTable.size(); j++) {
            Vector layerTableRecord = (Vector) this.layerTable.get(j);
            if (((Integer) layerTableRecord.get(0)).intValue() == layer) {
                layerName = (String) layerTableRecord.get(1);
            }
        }
        return layerName;
    }

    public HashMap<Integer, String> GetLayerIndexNameDict() {
        HashMap<Integer, String> result = new HashMap<>();
        if (this.layerTable != null) {
            for (int j = 0; j < this.layerTable.size(); j++) {
                Vector layerTableRecord = (Vector) this.layerTable.get(j);
                result.put(Integer.valueOf(((Integer) layerTableRecord.get(0)).intValue()), (String) layerTableRecord.get(1));
            }
        }
        return result;
    }

    public int getColorByLayer(DwgObject entity) {
        int colorByLayer = 0;
        int layer = entity.getLayerHandle();
        for (int j = 0; j < this.layerTable.size(); j++) {
            Vector layerTableRecord = (Vector) this.layerTable.get(j);
            if (((Integer) layerTableRecord.get(0)).intValue() == layer) {
                colorByLayer = ((Integer) layerTableRecord.get(2)).intValue();
            }
        }
        return colorByLayer;
    }

    private void setDwgVersion() throws IOException {
        String version;
        FileChannel fileChannel = new FileInputStream(new File(this.fileName)).getChannel();
        ByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
        String versionString = readDwgVersion(ByteBuffer.wrap(new byte[]{byteBuffer.get(0), byteBuffer.get(1), byteBuffer.get(2), byteBuffer.get(3), byteBuffer.get(4), byteBuffer.get(5)}));
        if (versionString.equals("AC1009")) {
            version = new String("R12");
        } else if (versionString.equals("AC1010")) {
            version = new String("R12+");
        } else if (versionString.equals("AC1012")) {
            version = new String("R13");
        } else if (versionString.equals("AC1014")) {
            version = new String("R14");
        } else if (versionString.equals("AC1015")) {
            version = new String("R15");
        } else {
            version = new String("Unknown");
        }
        this.dwgVersion = version;
    }

    private String readDwgVersion(ByteBuffer versionBuffer) {
        String[] bs = new String[versionBuffer.capacity()];
        String sv = "";
        for (int i = 0; i < versionBuffer.capacity(); i++) {
            bs[i] = new String(new byte[]{versionBuffer.get(i)});
            sv = String.valueOf(sv) + bs[i];
        }
        return sv;
    }

    public void testDwg3D() {
        double[][] pts;
        double[][] pts2;
        for (int i = 0; i < this.dwgObjects.size(); i++) {
            DwgObject obj = (DwgObject) this.dwgObjects.get(i);
            if (obj instanceof DwgArc) {
                if (((DwgArc) obj).getCenter()[2] != 0.0d) {
                    this.dwg3DFile = true;
                }
            } else if (obj instanceof DwgAttrib) {
                if (((DwgAttrib) obj).getElevation() != 0.0d) {
                    this.dwg3DFile = true;
                }
            } else if (obj instanceof DwgBlockHeader) {
                if (((DwgBlockHeader) obj).getBasePoint()[2] != 0.0d) {
                    this.dwg3DFile = true;
                }
            } else if (obj instanceof DwgCircle) {
                if (((DwgCircle) obj).getCenter()[2] != 0.0d) {
                    this.dwg3DFile = true;
                }
            } else if (obj instanceof DwgEllipse) {
                if (((DwgEllipse) obj).getCenter()[2] != 0.0d) {
                    this.dwg3DFile = true;
                }
            } else if (obj instanceof DwgInsert) {
                if (((DwgInsert) obj).getInsertionPoint()[2] != 0.0d) {
                    this.dwg3DFile = true;
                }
            } else if (obj instanceof DwgLine) {
                if (!((DwgLine) obj).isZflag()) {
                    double z1 = ((DwgLine) obj).getP1()[2];
                    double z2 = ((DwgLine) obj).getP2()[2];
                    if (z1 != 0.0d || z2 != 0.0d) {
                        this.dwg3DFile = true;
                    }
                }
            } else if (obj instanceof DwgLwPolyline) {
                if (((DwgLwPolyline) obj).getElevation() != 0.0d) {
                    this.dwg3DFile = true;
                }
            } else if (obj instanceof DwgMText) {
                if (((DwgMText) obj).getInsertionPoint()[2] != 0.0d) {
                    this.dwg3DFile = true;
                }
            } else if (obj instanceof DwgPoint) {
                if (((DwgPoint) obj).getPoint()[2] != 0.0d) {
                    this.dwg3DFile = true;
                }
            } else if (obj instanceof DwgPolyline2D) {
                if (((DwgPolyline2D) obj).getElevation() != 0.0d) {
                    this.dwg3DFile = true;
                }
            } else if (obj instanceof DwgPolyline3D) {
                if (((DwgPolyline3D) obj).getPts() != null) {
                    for (double[] dArr : ((DwgPolyline3D) obj).getPts()) {
                        if (dArr[2] != 0.0d) {
                            this.dwg3DFile = true;
                        }
                    }
                }
            } else if (obj instanceof DwgSolid) {
                if (((DwgSolid) obj).getElevation() != 0.0d) {
                    this.dwg3DFile = true;
                }
            } else if (obj instanceof DwgSpline) {
                for (double[] dArr2 : ((DwgSpline) obj).getControlPoints()) {
                    if (dArr2[2] != 0.0d) {
                        this.dwg3DFile = true;
                    }
                }
            } else if ((obj instanceof DwgText) && ((DwgText) obj).getElevation() != 0.0d) {
                this.dwg3DFile = true;
            }
        }
    }

    public void addDwgSectionOffset(String key, int seek, int size) {
        this.dwgSectionOffsets.add(new DwgSectionOffset(key, seek, size));
    }

    public int getDwgSectionOffset(String key) {
        for (int i = 0; i < this.dwgSectionOffsets.size(); i++) {
            DwgSectionOffset dso = (DwgSectionOffset) this.dwgSectionOffsets.get(i);
            if (key.equals(dso.getKey())) {
                return dso.getSeek();
            }
        }
        return 0;
    }

    public void addDwgObjectOffset(int handle, int offset) {
        this.dwgObjectOffsets.add(new DwgObjectOffset(handle, offset));
    }

    public void addDwgObject(DwgObject dwgObject) {
        this.dwgObjects.add(dwgObject);
    }

    public void addDwgClass(DwgClass dwgClass) {
        this.dwgClasses.add(dwgClass);
    }

    public Vector getDwgObjectOffsets() {
        return this.dwgObjectOffsets;
    }

    public Vector getDwgObjects() {
        return this.dwgObjects;
    }

    public String getFileName() {
        return this.fileName;
    }

    public boolean isDwg3DFile() {
        return this.dwg3DFile;
    }

    public void setDwg3DFile(boolean dwg3DFile2) {
        this.dwg3DFile = dwg3DFile2;
    }
}
