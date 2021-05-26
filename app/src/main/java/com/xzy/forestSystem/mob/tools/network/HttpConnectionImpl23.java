package com.xzy.forestSystem.mob.tools.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class HttpConnectionImpl23 implements HttpConnection {
    private HttpURLConnection conn;

    public HttpConnectionImpl23(HttpURLConnection conn2) {
        this.conn = conn2;
    }

    @Override //  com.xzy.forestSystem.mob.tools.network.HttpConnection
    public int getResponseCode() throws IOException {
        return this.conn.getResponseCode();
    }

    @Override //  com.xzy.forestSystem.mob.tools.network.HttpConnection
    public InputStream getInputStream() throws IOException {
        return this.conn.getInputStream();
    }

    @Override //  com.xzy.forestSystem.mob.tools.network.HttpConnection
    public InputStream getErrorStream() throws IOException {
        return this.conn.getErrorStream();
    }

    @Override //  com.xzy.forestSystem.mob.tools.network.HttpConnection
    public Map<String, List<String>> getHeaderFields() throws IOException {
        return this.conn.getHeaderFields();
    }
}
