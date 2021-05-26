package  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo;

import java.util.ArrayList;
import java.util.Iterator;

public abstract class Element {
    protected ArrayList data;
    protected ArrayList dataAcceptanceList;
    protected ArrayList elementAcceptanceList;
    protected ArrayList elements;
    public Data endTag;

    /* renamed from: s */
    public String f500s;
    public Data startTag;

    protected Element() {
        this.startTag = new Data(-10, 0);
        this.endTag = new Data(-10, 0);
        this.data = new ArrayList();
        this.elements = new ArrayList();
        this.dataAcceptanceList = new ArrayList();
        this.elementAcceptanceList = new ArrayList();
    }

    public Element(String s) {
        this.startTag = new Data(-10, 0);
        this.endTag = new Data(-10, 0);
        this.f500s = s;
    }

    public void AddElement(Element e) throws UnexpectedElement {
        if (IsAccepted(e)) {
            this.elements.add(e);
            return;
        }
        throw new UnexpectedElement();
    }

    public void InsertElement(int index, Element e) throws UnexpectedElement {
        if (IsAccepted(e)) {
            this.elements.add(index, e);
            return;
        }
        throw new UnexpectedElement();
    }

    public void RemoveElementAt(int index) {
        this.elements.remove(index);
    }

    public Element GetElement(int index) {
        return (Element) this.elements.get(index);
    }

    public int ElementCount() {
        return this.elements.size();
    }

    public void AddData(Data e) throws UnexpectedElement {
        if (IsAccepted(e)) {
            this.data.add(e);
            return;
        }
        throw new UnexpectedElement();
    }

    public void InsertData(int index, Data e) throws UnexpectedElement {
        if (IsAccepted(e)) {
            this.data.add(index, e);
            return;
        }
        throw new UnexpectedElement();
    }

    public void RemoveDataAt(int index) {
        this.data.remove(index);
    }

    public Data GetData(int index) {
        return (Data) this.data.get(index);
    }

    public int GetIndexFor(int code) {
        Iterator it = this.data.iterator();
        while (it.hasNext()) {
            Data d = (Data) it.next();
            if (d.code == code) {
                return this.data.indexOf(d);
            }
        }
        return -1;
    }

    public Data GetDataFor(int code) {
        Iterator it = this.data.iterator();
        while (it.hasNext()) {
            Data d = (Data) it.next();
            if (d.code == code) {
                return d;
            }
        }
        return new Data(-10, 0);
    }

    public int DataCount() {
        return this.data.size();
    }

    public boolean IsAccepted(Data d) {
        return this.dataAcceptanceList.contains(Integer.valueOf(d.code)) && isCorectData(d);
    }

    public boolean IsAccepted(Element e) {
        return true;
    }

    public boolean isCorectData(Data d) {
        if (d.code >= 290 && d.code <= 299) {
            return d.data instanceof Boolean;
        }
        if ((d.code >= 60 && d.code <= 79) || ((d.code >= 270 && d.code <= 289) || ((d.code >= 370 && d.code <= 389) || (d.code >= 170 && d.code <= 179)))) {
            return (d.data instanceof Short) || (d.data instanceof Long);
        }
        if ((d.code >= 90 && d.code <= 99) || d.code == 1071) {
            return d.data instanceof Integer;
        }
        if ((d.code >= 10 && d.code <= 59) || ((d.code >= 110 && d.code <= 149) || ((d.code >= 210 && d.code <= 239) || (d.code >= 1010 && d.code <= 1059)))) {
            return d.data instanceof Double;
        }
        if (d.code == 100 || d.code == 102 || d.code == 105 || d.code == 999 || ((d.code >= 300 && d.code <= 369) || ((d.code >= 390 && d.code <= 399) || (d.code >= 410 && d.code <= 419)))) {
            return (d.data instanceof String) && ((String) d.data).length() <= 255;
        }
        if ((d.code < 0 || d.code > 9) && (d.code < 1000 || d.code > 1009)) {
            return false;
        }
        return d.data instanceof String;
    }

    public String getName() {
        return ((Data) this.data.get(0)).data.toString();
    }

    public void AddReplace(int cod, Object o) throws UnexpectedElement {
        int ind = GetIndexFor(cod);
        if (ind == -1) {
            AddData(new Data(cod, o));
            return;
        }
        this.data.remove(ind);
        InsertData(ind, new Data(cod, o));
    }

    public void AddData(int cod, Object o) throws UnexpectedElement {
        AddData(new Data(cod, o));
    }
}
