package  com.xzy.forestSystem.mob.tools.network;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class MultiPart extends HTTPPart {
    private ArrayList<HTTPPart> parts = new ArrayList<>();

    public MultiPart append(HTTPPart part) throws Throwable {
        this.parts.add(part);
        return this;
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.mob.tools.network.HTTPPart
    public InputStream getInputStream() throws Throwable {
        MultiPartInputStream mpis = new MultiPartInputStream();
        Iterator<HTTPPart> it = this.parts.iterator();
        while (it.hasNext()) {
            mpis.addInputStream(it.next().getInputStream());
        }
        return mpis;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Iterator<HTTPPart> it = this.parts.iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.mob.tools.network.HTTPPart
    public long length() throws Throwable {
        long length = 0;
        Iterator<HTTPPart> it = this.parts.iterator();
        while (it.hasNext()) {
            length += it.next().length();
        }
        return length;
    }
}
