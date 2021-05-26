package com.xzy.forestSystem.mob.tools.network;

import  com.xzy.forestSystem.mob.tools.utils.Data;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ByteArrayPart extends HTTPPart {
    private BufferedByteArrayOutputStream buffer;

    public ByteArrayPart append(byte[] array) throws Throwable {
        if (this.buffer == null) {
            this.buffer = new BufferedByteArrayOutputStream(array.length);
        }
        this.buffer.write(array);
        this.buffer.flush();
        return this;
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.mob.tools.network.HTTPPart
    public InputStream getInputStream() throws Throwable {
        if (this.buffer == null) {
            return new ByteArrayInputStream(new byte[0]);
        }
        byte[] body = this.buffer.getBuffer();
        if (body == null || this.buffer.size() <= 0) {
            return new ByteArrayInputStream(new byte[0]);
        }
        return new ByteArrayInputStream(body, 0, this.buffer.size());
    }

    public String toString() {
        byte[] body;
        if (this.buffer == null || (body = this.buffer.getBuffer()) == null) {
            return null;
        }
        return Data.byteToHex(body, 0, this.buffer.size());
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.mob.tools.network.HTTPPart
    public long length() throws Throwable {
        if (this.buffer == null) {
            return 0;
        }
        return (long) this.buffer.size();
    }
}
