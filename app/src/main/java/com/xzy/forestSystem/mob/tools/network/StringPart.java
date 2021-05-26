package  com.xzy.forestSystem.mob.tools.network;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class StringPart extends HTTPPart {

    /* renamed from: sb */
    private StringBuilder f381sb = new StringBuilder();

    public StringPart append(String text) {
        this.f381sb.append(text);
        return this;
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.mob.tools.network.HTTPPart
    public InputStream getInputStream() throws Throwable {
        return new ByteArrayInputStream(this.f381sb.toString().getBytes("utf-8"));
    }

    public String toString() {
        return this.f381sb.toString();
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.mob.tools.network.HTTPPart
    public long length() throws Throwable {
        return (long) this.f381sb.toString().getBytes("utf-8").length;
    }
}
