package com.xzy.forestSystem.mob.tools.network;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;

public class HttpConnectionImpl implements HttpConnection {
    private HttpResponse response;

    public HttpConnectionImpl(HttpResponse httpResponse) {
        this.response = httpResponse;
    }

    @Override //  com.xzy.forestSystem.mob.tools.network.HttpConnection
    public int getResponseCode() throws IOException {
        return this.response.getStatusLine().getStatusCode();
    }

    @Override //  com.xzy.forestSystem.mob.tools.network.HttpConnection
    public InputStream getInputStream() throws IOException {
        return this.response.getEntity().getContent();
    }

    @Override //  com.xzy.forestSystem.mob.tools.network.HttpConnection
    public InputStream getErrorStream() throws IOException {
        return this.response.getEntity().getContent();
    }

    @Override //  com.xzy.forestSystem.mob.tools.network.HttpConnection
    public Map<String, List<String>> getHeaderFields() throws IOException {
        Map<String, List<String>> map = null;
        Header[] header = this.response.getAllHeaders();
        if (header != null) {
            map = new HashMap<>();
            for (Header h : header) {
                List<String> list = new ArrayList<>();
                map.put(h.getName(), list);
                String[] values = h.getValue().split(",");
                if (values != null) {
                    for (String v : values) {
                        list.add(v.trim());
                    }
                }
            }
        }
        return map;
    }
}
