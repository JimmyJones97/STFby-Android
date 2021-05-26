package com.xzy.forestSystem.mob.tools.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FilePart extends HTTPPart {
    private File file;

    public void setFile(File file2) {
        this.file = file2;
    }

    public void setFile(String path) {
        this.file = new File(path);
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.mob.tools.network.HTTPPart
    public InputStream getInputStream() throws Throwable {
        return new FileInputStream(this.file);
    }

    public String toString() {
        return this.file.toString();
    }

    /* access modifiers changed from: protected */
    @Override //  com.xzy.forestSystem.mob.tools.network.HTTPPart
    public long length() throws Throwable {
        return this.file.length();
    }
}
