package  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class Writer {
    public static void Write(Document d, OutputStream s) throws IOException {
        OutputStreamWriter sr = new OutputStreamWriter(s);
        WriteHeader(d.header, sr);
        WriteTables(d.tables, sr);
        WriteBlocks(d.blocks, sr);
        WriteEntities(d.entities, sr);
        WriteData(new Data(0, "EOF"), sr);
        sr.close();
    }

    private static void WriteHeader(Header h, OutputStreamWriter sr) throws IOException {
        if (h != null) {
            WriteElement(h, sr);
        }
    }

    private static void WriteEntities(Entities e, OutputStreamWriter sr) throws IOException {
        if (e != null) {
            WriteElement(e, sr);
        }
    }

    private static void WriteBlocks(Blocks b, OutputStreamWriter sr) throws IOException {
        if (b != null) {
            WriteElement(b, sr);
        }
    }

    private static void WriteTables(Tables t, OutputStreamWriter sr) throws IOException {
        if (t != null) {
            WriteElement(t, sr);
        }
    }

    private static void WriteElement(Element e, OutputStreamWriter sr) throws IOException {
        if (e != null) {
            if (e.f500s != null) {
                sr.write(e.f500s);
                return;
            }
            WriteData(e.startTag, sr);
            for (int i = 0; i < e.DataCount(); i++) {
                WriteData(e.GetData(i), sr);
            }
            for (int i2 = 0; i2 < e.ElementCount(); i2++) {
                WriteElement(e.GetElement(i2), sr);
            }
            WriteData(e.endTag, sr);
        }
    }

    private static void WriteData(Data d, OutputStreamWriter sr) throws IOException {
        if (d.code != -10) {
            sr.write(new StringBuilder(String.valueOf(d.code)).toString());
            sr.write("\n");
            if (d.data instanceof String) {
                sr.write(((String) d.data).toString());
            } else {
                sr.write(new StringBuilder(String.valueOf(d.data.toString())).toString());
            }
            sr.write("\n");
        }
    }
}
