package  com.xzy.forestSystem.gogisapi.IOData.DXF;

import  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.C0544Layer;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.Circle;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.Line;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.LwPolyLine;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.MText;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.Point;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.PolyLine;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.bean.Text;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DxfReader {
    private ArrayList CircleList = new ArrayList();
    private ArrayList LayerList = new ArrayList();
    private ArrayList LineList = new ArrayList();
    private ArrayList LwpolylineList = new ArrayList();
    private ArrayList MTextList = new ArrayList();
    private ArrayList PointList = new ArrayList();
    private ArrayList TextList = new ArrayList();

    /* renamed from: br */
    private BufferedReader f499br;
    private int count;
    private double leftx;
    private double lefty;
    private ArrayList polylineList = new ArrayList();
    private double rightx;
    private double righty;
    private String[] str = new String[2];

    public double XMax() {
        return this.rightx;
    }

    public double XMin() {
        return this.leftx;
    }

    public double YMax() {
        return this.righty;
    }

    public double YMin() {
        return this.lefty;
    }

    public ArrayList getPointList() {
        return this.PointList;
    }

    public ArrayList getLineList() {
        return this.LineList;
    }

    public ArrayList getLwpolylineList() {
        return this.LwpolylineList;
    }

    public ArrayList getTextList() {
        return this.TextList;
    }

    public ArrayList getMTextList() {
        return this.MTextList;
    }

    public ArrayList getPolylineList() {
        return this.polylineList;
    }

    public DxfReader(String filepath) {
        try {
            this.f499br = new BufferedReader(new FileReader(new File(filepath)));
            Read();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] ReadPair() {
        try {
            String code = this.f499br.readLine().trim();
            String codedata = this.f499br.readLine();
            this.count += 2;
            return new String[]{code, codedata};
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void Read() {
        try {
            String temp = this.f499br.readLine();
            while (temp != null) {
                this.str = new String[]{temp, this.f499br.readLine()};
                if (this.str[1].equals("SECTION") && this.str[1].equals("HEADER")) {
                    ReadHeader();
                }
                if (this.str[1].equals("TABLES")) {
                    ReadTable();
                }
                if (this.str[1].equals("ENTITIES")) {
                    ReadEntities();
                }
                temp = this.f499br.readLine();
            }
        } catch (IOException e) {
        }
    }

    public void ReadEntities() {
        while (!this.str[1].equals("ENDSEC")) {
            try {
                if (this.str[1].equals("LINE")) {
                    ReadLine();
                } else if (this.str[1].equals("CIRCLE")) {
                    ReadCircle();
                } else if (this.str[1].equals("POINT")) {
                    ReadPoint();
                } else if (this.str[1].equals("POLYLINE")) {
                    ReadPolyline();
                } else if (this.str[1].equals("LWPOLYLINE")) {
                    ReadLwpolyline();
                } else if (this.str[1].equals("TEXT")) {
                    ReadText();
                } else if (this.str[1].equals("MTEXT")) {
                    ReadMText();
                } else {
                    this.str = ReadPair();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private void ReadText() {
        Text newText = new Text();
        while (!this.str[1].equals("ENDSEC")) {
            this.str = ReadPair();
            if (this.str[0].equals("8")) {
                newText.LName = this.str[1];
            }
            if (this.str[0].equals("10")) {
                newText.PointX = Double.valueOf(this.str[1]).doubleValue();
            }
            if (this.str[0].equals("20")) {
                newText.PointY = Double.valueOf(this.str[1]).doubleValue();
            }
            if (this.str[0].equals("1")) {
                newText.Value = this.str[1];
            }
            if (this.str[0].equals("40")) {
                newText.Height = Double.valueOf(this.str[1]).doubleValue();
            }
            if (this.str[0].equals("0")) {
                this.TextList.add(newText);
                return;
            }
        }
    }

    private void ReadMText() {
        MText newText = new MText();
        while (!this.str[1].equals("ENDSEC")) {
            this.str = ReadPair();
            if (this.str[0].equals("8")) {
                newText.LName = this.str[1];
            }
            if (this.str[0].equals("10")) {
                newText.PointX = Double.valueOf(this.str[1]).doubleValue();
            }
            if (this.str[0].equals("20")) {
                newText.PointY = Double.valueOf(this.str[1]).doubleValue();
            }
            if (this.str[0].equals("1")) {
                newText.Value = this.str[1];
            }
            if (this.str[0].equals("0")) {
                this.MTextList.add(newText);
                return;
            }
        }
    }

    private void ReadPolyline() throws NumberFormatException, IOException {
        PolyLine newpolyLine = new PolyLine();
        boolean isPoly3D = false;
        while (!this.str[1].equals("ENDSEC")) {
            this.str = ReadPair();
            if (this.str[0].equals("8")) {
                newpolyLine.LName = this.str[1];
            }
            if (this.str[0].equals("370")) {
                newpolyLine.lwidth = this.str[1];
            }
            if (this.str[0].equals("62")) {
                newpolyLine.colornum = this.str[1];
            }
            if (this.str[0].equals("30")) {
                newpolyLine.Elevation = this.str[1];
            }
            if (this.str[0].equals("70")) {
                newpolyLine.Flag = Integer.parseInt(this.str[1].trim());
            }
            if (this.str[0].equals("100") && this.str[1].equals("AcDb3dPolyline")) {
                isPoly3D = true;
            }
            while (true) {
                if (this.str[0].equals("10")) {
                    double px = Double.valueOf(this.str[1]).doubleValue();
                    Point p = new Point();
                    this.str = ReadPair();
                    double py = Double.valueOf(this.str[1]).doubleValue();
                    p.PointX = px;
                    p.PointY = py;
                    this.str = ReadPair();
                    while (true) {
                        if (!this.str[0].equals("10")) {
                            this.str = ReadPair();
                            if (this.str[0].equals("70")) {
                                String tmpStr02 = this.str[1].trim();
                                if (isPoly3D) {
                                    if (tmpStr02.equals("32") || tmpStr02.equals("40")) {
                                        newpolyLine.pointList.add(p);
                                    }
                                } else if (tmpStr02.equals("8")) {
                                    newpolyLine.pointList.add(p);
                                }
                            } else if (this.str[0].equals("0") && this.str[1].equals("SEQEND")) {
                                this.polylineList.add(newpolyLine);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private void ReadLwpolyline() throws NumberFormatException, IOException {
        LwPolyLine newlw = new LwPolyLine();
        while (!this.str[1].equals("ENDSEC")) {
            this.str = ReadPair();
            if (this.str[0].equals("8")) {
                newlw.LName = this.str[1];
            }
            if (this.str[0].equals("370")) {
                newlw.lwidth = this.str[1];
            }
            if (this.str[0].equals("62")) {
                newlw.colornum = this.str[1];
            }
            if (this.str[0].equals("38")) {
                newlw.Elevation = this.str[1];
            }
            if (this.str[0].equals("90")) {
                newlw.PointCount = Integer.parseInt(this.str[1].trim());
            }
            if (this.str[0].equals("70")) {
                newlw.Flag = Integer.parseInt(this.str[1].trim());
            }
            if (this.str[0].equals("10") && newlw.PointCount > 0) {
                newlw.pointx = new double[newlw.PointCount];
                newlw.pointy = new double[newlw.PointCount];
                newlw.converxity = new double[newlw.PointCount];
                newlw.pointx[0] = Double.valueOf(this.str[1]).doubleValue();
                this.str = ReadPair();
                newlw.pointy[0] = Double.valueOf(this.str[1]).doubleValue();
                this.str = ReadPair();
                int tmpI = 1;
                while (tmpI < newlw.PointCount) {
                    if (this.str[0].equals("10")) {
                        newlw.pointx[tmpI] = Double.valueOf(this.str[1]).doubleValue();
                    } else if (this.str[0].equals("20")) {
                        newlw.pointy[tmpI] = Double.valueOf(this.str[1]).doubleValue();
                        tmpI++;
                    }
                    this.str = ReadPair();
                }
            }
            if (this.str[0].equals("0")) {
                this.LwpolylineList.add(newlw);
                return;
            }
        }
    }

    private void ReadPoint() {
        Point newPoint = new Point();
        while (!this.str[1].equals("ENDSEC")) {
            this.str = ReadPair();
            if (this.str[0].equals("8")) {
                newPoint.LName = this.str[1];
            }
            if (this.str[0].equals("10")) {
                newPoint.PointX = Double.valueOf(this.str[1]).doubleValue();
            }
            if (this.str[0].equals("20")) {
                newPoint.PointY = Double.valueOf(this.str[1]).doubleValue();
            }
            if (this.str[0].equals("30")) {
                newPoint.PointZ = Double.valueOf(this.str[1]).doubleValue();
            }
            if (this.str[0].equals("62")) {
                newPoint.colornum = this.str[1];
            }
            if (this.str[0].equals("370")) {
                newPoint.lwidth = this.str[1];
            }
            if (this.str[0].equals("0")) {
                this.PointList.add(newPoint);
                return;
            }
        }
    }

    private void ReadCircle() {
        Circle newcircle = new Circle();
        while (!this.str[1].equals("ENDSEC")) {
            this.str = ReadPair();
            if (this.str[0].equals("8")) {
                newcircle.LName = this.str[1];
            }
            if (this.str[0].equals("10")) {
                newcircle.CenterX = new StringBuilder(String.valueOf(this.str[1])).toString();
            }
            if (this.str[0].equals("20")) {
                newcircle.CenterY = new StringBuilder(String.valueOf(this.str[1])).toString();
            }
            if (this.str[0].equals("40")) {
                newcircle.Radius = new StringBuilder(String.valueOf(this.str[1])).toString();
            }
            if (this.str[0].equals("370")) {
                newcircle.lwidth = this.str[1];
            }
            if (this.str[0].equals("0")) {
                this.CircleList.add(newcircle);
                return;
            }
        }
    }

    public void ReadLine() {
        Line newline = new Line();
        while (!this.str[1].equals("ENDSEC")) {
            this.str = ReadPair();
            if (this.str[0].equals("8")) {
                newline.LName = this.str[1];
            }
            if (this.str[0].equals("10")) {
                newline.StartX = new StringBuilder(String.valueOf(this.str[1])).toString();
            }
            if (this.str[0].equals("20")) {
                newline.StartY = new StringBuilder(String.valueOf(this.str[1])).toString();
            }
            if (this.str[0].equals("11")) {
                newline.EndX = new StringBuilder(String.valueOf(this.str[1])).toString();
            }
            if (this.str[0].equals("21")) {
                newline.EndY = new StringBuilder(String.valueOf(this.str[1])).toString();
            }
            if (this.str[0].equals("62")) {
                newline.colornum = this.str[1];
            }
            if (this.str[0].equals("370")) {
                newline.lwidth = this.str[1];
            }
            if (this.str[0].equals("0")) {
                this.LineList.add(newline);
                return;
            }
        }
    }

    public void ReadTable() {
        while (!this.str[1].equals("ENDSEC")) {
            while (true) {
                if (this.str[0].equals("2") && this.str[1].equals("LAYER")) {
                    break;
                }
                this.str = ReadPair();
            }
            while (this.str[0].equals("2") && this.str[1].equals("LAYER")) {
                ReadLAYER();
            }
            while (!this.str[1].equals("ENDSEC")) {
                this.str = ReadPair();
            }
        }
    }

    public void ReadLAYER() {
        try {
            C0544Layer newlayer = new C0544Layer();
            while (!this.str[1].equals("ENDTAB")) {
                this.str = ReadPair();
                if (this.str[0].equals("2")) {
                    newlayer.name = this.str[1];
                }
                if (this.str[0].equals("62")) {
                    newlayer.colornum = this.str[1];
                }
                if (this.str[0].equals("6")) {
                    newlayer.lstyle = this.str[1];
                }
                if (this.str[0].equals("370")) {
                    newlayer.lwidth = this.str[1];
                }
                if (this.str[0].equals("2") && this.str[1].equals("LAYER")) {
                    this.LayerList.add(newlayer);
                    return;
                }
            }
            this.LayerList.add(newlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ReadHeader() {
        while (this.str[1] != "ENDSEC") {
            this.str = ReadPair();
            if (this.str[1].equals("$EXTMIN")) {
                this.str = ReadPair();
                this.leftx = Double.valueOf(this.str[1]).doubleValue();
                this.str = ReadPair();
                this.lefty = Double.valueOf(this.str[1]).doubleValue();
            }
            if (this.str[1].equals("$EXTMAX")) {
                this.str = ReadPair();
                this.rightx = Double.valueOf(this.str[1]).doubleValue();
                this.str = ReadPair();
                this.righty = Double.valueOf(this.str[1]).doubleValue();
            }
        }
    }
}
