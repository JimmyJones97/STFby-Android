package com.xzy.forestSystem.mob.tools.network;

import android.content.Context;
import android.os.Build;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.utils.Data;
import  com.xzy.forestSystem.mob.tools.utils.Hashon;
import  com.xzy.forestSystem.mob.tools.utils.ReflectHelper;
import  com.xzy.forestSystem.mob.tools.utils.ResHelper;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;

public class NetworkHelper {
    public static int connectionTimeout;
    public static int readTimout;

    public static class NetworkTimeOut {
        public int connectionTimeout;
        public int readTimout;
    }

    public String httpGet(String url, ArrayList<KVPair<String>> values, ArrayList<KVPair<String>> headers, NetworkTimeOut timeout) throws Throwable {
        long time = System.currentTimeMillis();
        MobLog.getInstance().m64i("httpGet: " + url, new Object[0]);
        if (values != null) {
            String param = kvPairsToUrl(values);
            if (param.length() > 0) {
                url = url + "?" + param;
            }
        }
        HttpURLConnection conn = getConnection(url, timeout);
        if (headers != null) {
            Iterator<KVPair<String>> it = headers.iterator();
            while (it.hasNext()) {
                KVPair<String> header = it.next();
                conn.setRequestProperty(header.name, header.value);
            }
        }
        conn.connect();
        int status = conn.getResponseCode();
        if (status == 200) {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("utf-8")));
            for (String txt = br.readLine(); txt != null; txt = br.readLine()) {
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(txt);
            }
            br.close();
            conn.disconnect();
            String resp = sb.toString();
            MobLog.getInstance().m64i("use time: " + (System.currentTimeMillis() - time), new Object[0]);
            return resp;
        }
        StringBuilder sb2 = new StringBuilder();
        BufferedReader br2 = new BufferedReader(new InputStreamReader(conn.getErrorStream(), Charset.forName("utf-8")));
        for (String txt2 = br2.readLine(); txt2 != null; txt2 = br2.readLine()) {
            if (sb2.length() > 0) {
                sb2.append('\n');
            }
            sb2.append(txt2);
        }
        br2.close();
        conn.disconnect();
        HashMap<String, Object> errMap = new HashMap<>();
        errMap.put("error", sb2.toString());
        errMap.put("status", Integer.valueOf(status));
        throw new Throwable(new Hashon().fromHashMap(errMap));
    }

    public String downloadCache(Context context, String url, String cacheFolder, boolean skipIfCached, NetworkTimeOut timeout) throws Throwable {
        return downloadCache(context, url, cacheFolder, skipIfCached, timeout, null);
    }

    public String downloadCache(Context context, String url, String cacheFolder, boolean skipIfCached, NetworkTimeOut timeout, FileDownloadListener downloadListener) throws Throwable {
        List<String> headers;
        int dot;
        List<String> headers2;
        long time = System.currentTimeMillis();
        MobLog.getInstance().m64i("downloading: " + url, new Object[0]);
        if (skipIfCached) {
            File cache = new File(ResHelper.getCachePath(context, cacheFolder), Data.MD5(url));
            if (skipIfCached && cache.exists()) {
                MobLog.getInstance().m64i("use time: " + (System.currentTimeMillis() - time), new Object[0]);
                if (downloadListener != null) {
                    downloadListener.onProgress(100, cache.length(), cache.length());
                }
                return cache.getAbsolutePath();
            }
        }
        HttpURLConnection conn = getConnection(url, timeout);
        conn.connect();
        int status = conn.getResponseCode();
        if (status == 200) {
            String name = null;
            Map<String, List<String>> map = conn.getHeaderFields();
            if (!(map == null || (headers2 = map.get("Content-Disposition")) == null || headers2.size() <= 0)) {
                String[] parts = headers2.get(0).split(";");
                for (String part : parts) {
                    if (part.trim().startsWith("filename")) {
                        name = part.split("=")[1];
                        if (name.startsWith("\"") && name.endsWith("\"")) {
                            name = name.substring(1, name.length() - 1);
                        }
                    }
                }
            }
            if (name == null) {
                name = Data.MD5(url);
                if (!(map == null || (headers = map.get("Content-Type")) == null || headers.size() <= 0)) {
                    String value = headers.get(0);
                    String value2 = value == null ? "" : value.trim();
                    if (value2.startsWith("image/")) {
                        String type = value2.substring("image/".length());
                        StringBuilder append = new StringBuilder().append(name).append(FileSelector_Dialog.sFolder);
                        if ("jpeg".equals(type)) {
                            type = "jpg";
                        }
                        name = append.append(type).toString();
                    } else {
                        int index = url.lastIndexOf(47);
                        String lastPart = null;
                        if (index > 0) {
                            lastPart = url.substring(index + 1);
                        }
                        if (lastPart != null && lastPart.length() > 0 && (dot = lastPart.lastIndexOf(46)) > 0 && lastPart.length() - dot < 10) {
                            name = name + lastPart.substring(dot);
                        }
                    }
                }
            }
            File cache2 = new File(ResHelper.getCachePath(context, cacheFolder), name);
            if (!skipIfCached || !cache2.exists()) {
                if (!cache2.getParentFile().exists()) {
                    cache2.getParentFile().mkdirs();
                }
                if (cache2.exists()) {
                    cache2.delete();
                }
                if (downloadListener != null) {
                    try {
                        if (downloadListener.isCanceled()) {
                            return null;
                        }
                    } finally {
                        if (cache2.exists()) {
                            cache2.delete();
                        }
                    }
                }
                InputStream is = conn.getInputStream();
                int contentLength = conn.getContentLength();
                FileOutputStream fos = new FileOutputStream(cache2);
                byte[] buf = new byte[1024];
                int downloadLength = 0;
                for (int len = is.read(buf); len > 0; len = is.read(buf)) {
                    fos.write(buf, 0, len);
                    downloadLength += len;
                    if (downloadListener != null) {
                        downloadListener.onProgress(contentLength <= 0 ? 100 : (downloadLength * 100) / contentLength, (long) downloadLength, (long) contentLength);
                        if (downloadListener.isCanceled()) {
                            break;
                        }
                    }
                }
                if (downloadListener != null) {
                    if (downloadListener.isCanceled()) {
                        if (cache2.exists()) {
                            cache2.delete();
                        }
                        fos.flush();
                        is.close();
                        fos.close();
                        return null;
                    }
                    downloadListener.onProgress(100, cache2.length(), cache2.length());
                }
                fos.flush();
                is.close();
                fos.close();
                conn.disconnect();
                MobLog.getInstance().m64i("use time: " + (System.currentTimeMillis() - time), new Object[0]);
                return cache2.getAbsolutePath();
            }
            conn.disconnect();
            MobLog.getInstance().m64i("use time: " + (System.currentTimeMillis() - time), new Object[0]);
            if (downloadListener != null) {
                downloadListener.onProgress(100, cache2.length(), cache2.length());
            }
            return cache2.getAbsolutePath();
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), Charset.forName("utf-8")));
        for (String txt = br.readLine(); txt != null; txt = br.readLine()) {
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(txt);
        }
        br.close();
        conn.disconnect();
        HashMap<String, Object> errMap = new HashMap<>();
        errMap.put("error", sb.toString());
        errMap.put("status", Integer.valueOf(status));
        throw new Throwable(new Hashon().fromHashMap(errMap));
    }

    public void rawGet(String url, RawNetworkCallback callback, NetworkTimeOut timeout) throws Throwable {
        rawGet(url, (ArrayList<KVPair<String>>) null, callback, timeout);
    }

    public void rawGet(String url, ArrayList<KVPair<String>> headers, RawNetworkCallback callback, NetworkTimeOut timeout) throws Throwable {
        long time = System.currentTimeMillis();
        MobLog.getInstance().m64i("rawGet: " + url, new Object[0]);
        HttpURLConnection conn = getConnection(url, timeout);
        if (headers != null) {
            Iterator<KVPair<String>> it = headers.iterator();
            while (it.hasNext()) {
                KVPair<String> header = it.next();
                conn.setRequestProperty(header.name, header.value);
            }
        }
        conn.connect();
        int status = conn.getResponseCode();
        if (status == 200) {
            if (callback != null) {
                callback.onResponse(conn.getInputStream());
            }
            conn.disconnect();
            MobLog.getInstance().m64i("use time: " + (System.currentTimeMillis() - time), new Object[0]);
            return;
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), Charset.forName("utf-8")));
        for (String txt = br.readLine(); txt != null; txt = br.readLine()) {
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(txt);
        }
        br.close();
        conn.disconnect();
        HashMap<String, Object> errMap = new HashMap<>();
        errMap.put("error", sb.toString());
        errMap.put("status", Integer.valueOf(status));
        throw new Throwable(new Hashon().fromHashMap(errMap));
    }

    public void rawGet(String url, HttpResponseCallback callback, NetworkTimeOut timeout) throws Throwable {
        rawGet(url, (ArrayList<KVPair<String>>) null, callback, timeout);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0075, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0076, code lost:
        r0.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0079, code lost:
        throw r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void rawGet(java.lang.String r12, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r13,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback r14,  com.xzy.forestSystem.mob.tools.network.NetworkHelper.NetworkTimeOut r15) throws java.lang.Throwable {
        /*
            r11 = this;
            r10 = 0
            long r4 = java.lang.System.currentTimeMillis()
             com.xzy.forestSystem.mob.tools.log.NLog r3 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "rawGet: "
            java.lang.StringBuilder r6 = r6.append(r7)
            java.lang.StringBuilder r6 = r6.append(r12)
            java.lang.String r6 = r6.toString()
            java.lang.Object[] r7 = new java.lang.Object[r10]
            r3.m64i(r6, r7)
            java.net.HttpURLConnection r0 = r11.getConnection(r12, r15)
            if (r13 == 0) goto L_0x0041
            java.util.Iterator r6 = r13.iterator()
        L_0x002b:
            boolean r3 = r6.hasNext()
            if (r3 == 0) goto L_0x0041
            java.lang.Object r1 = r6.next()
             com.xzy.forestSystem.mob.tools.network.KVPair r1 = ( com.xzy.forestSystem.mob.tools.network.KVPair) r1
            java.lang.String r7 = r1.name
            T r3 = r1.value
            java.lang.String r3 = (java.lang.String) r3
            r0.setRequestProperty(r7, r3)
            goto L_0x002b
        L_0x0041:
            r0.connect()
            if (r14 == 0) goto L_0x007a
             com.xzy.forestSystem.mob.tools.network.HttpConnectionImpl23 r3 = new  com.xzy.forestSystem.mob.tools.network.HttpConnectionImpl23     // Catch:{ Throwable -> 0x0073 }
            r3.<init>(r0)     // Catch:{ Throwable -> 0x0073 }
            r14.onResponse(r3)     // Catch:{ Throwable -> 0x0073 }
            r0.disconnect()
        L_0x0051:
             com.xzy.forestSystem.mob.tools.log.NLog r3 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "use time: "
            java.lang.StringBuilder r6 = r6.append(r7)
            long r8 = java.lang.System.currentTimeMillis()
            long r8 = r8 - r4
            java.lang.StringBuilder r6 = r6.append(r8)
            java.lang.String r6 = r6.toString()
            java.lang.Object[] r7 = new java.lang.Object[r10]
            r3.m64i(r6, r7)
            return
        L_0x0073:
            r2 = move-exception
            throw r2     // Catch:{ all -> 0x0075 }
        L_0x0075:
            r3 = move-exception
            r0.disconnect()
            throw r3
        L_0x007a:
            r0.disconnect()
            goto L_0x0051
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.network.NetworkHelper.rawGet(java.lang.String, java.util.ArrayList,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback,  com.xzy.forestSystem.mob.tools.network.NetworkHelper$NetworkTimeOut):void");
    }

    public String jsonPost(String url, ArrayList<KVPair<String>> values, ArrayList<KVPair<String>> headers, NetworkTimeOut timeout) throws Throwable {
        final HashMap<String, String> map = new HashMap<>();
        jsonPost(url, values, headers, timeout, new HttpResponseCallback() { // from class:  com.xzy.forestSystem.mob.tools.network.NetworkHelper.1
            @Override //  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback
            public void onResponse(HttpConnection conn) throws Throwable {
                int status = conn.getResponseCode();
                if (status == 200 || status == 201) {
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("utf-8")));
                    for (String txt = br.readLine(); txt != null; txt = br.readLine()) {
                        if (sb.length() > 0) {
                            sb.append('\n');
                        }
                        sb.append(txt);
                    }
                    br.close();
                    map.put("res", sb.toString());
                    return;
                }
                StringBuilder sb2 = new StringBuilder();
                BufferedReader br2 = new BufferedReader(new InputStreamReader(conn.getErrorStream(), Charset.forName("utf-8")));
                for (String txt2 = br2.readLine(); txt2 != null; txt2 = br2.readLine()) {
                    if (sb2.length() > 0) {
                        sb2.append('\n');
                    }
                    sb2.append(txt2);
                }
                br2.close();
                HashMap<String, Object> errMap = new HashMap<>();
                errMap.put("error", sb2.toString());
                errMap.put("status", Integer.valueOf(status));
                throw new Throwable(new Hashon().fromHashMap(errMap));
            }
        });
        if (map.containsKey("res")) {
            return map.get("res");
        }
        return null;
    }

    public void jsonPost(String url, ArrayList<KVPair<String>> values, ArrayList<KVPair<String>> headers, NetworkTimeOut timeout, HttpResponseCallback callback) throws Throwable {
        HashMap<String, Object> valuesMap = new HashMap<>();
        Iterator<KVPair<String>> it = values.iterator();
        while (it.hasNext()) {
            KVPair<String> p = it.next();
            valuesMap.put(p.name, p.value);
        }
        jsonPost(url, valuesMap, headers, timeout, callback);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00d7, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x00d8, code lost:
        r5.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00db, code lost:
        throw r14;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void jsonPost(java.lang.String r19, java.util.HashMap<java.lang.String, java.lang.Object> r20, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r21,  com.xzy.forestSystem.mob.tools.network.NetworkHelper.NetworkTimeOut r22,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback r23) throws java.lang.Throwable {
        /*
            r18 = this;
            long r12 = java.lang.System.currentTimeMillis()
             com.xzy.forestSystem.mob.tools.log.NLog r14 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r16 = "jsonPost: "
            java.lang.StringBuilder r15 = r15.append(r16)
            r0 = r19
            java.lang.StringBuilder r15 = r15.append(r0)
            java.lang.String r15 = r15.toString()
            r16 = 0
            r0 = r16
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r16 = r0
            r14.m64i(r15, r16)
            r0 = r18
            r1 = r19
            r2 = r22
            java.net.HttpURLConnection r5 = r0.getConnection(r1, r2)
            r14 = 1
            r5.setDoOutput(r14)
            r14 = 0
            r5.setChunkedStreamingMode(r14)
            java.lang.String r14 = "content-type"
            java.lang.String r15 = "application/json"
            r5.setRequestProperty(r14, r15)
            if (r21 == 0) goto L_0x0061
            java.util.Iterator r15 = r21.iterator()
        L_0x0047:
            boolean r14 = r15.hasNext()
            if (r14 == 0) goto L_0x0061
            java.lang.Object r6 = r15.next()
             com.xzy.forestSystem.mob.tools.network.KVPair r6 = ( com.xzy.forestSystem.mob.tools.network.KVPair) r6
            java.lang.String r0 = r6.name
            r16 = r0
            T r14 = r6.value
            java.lang.String r14 = (java.lang.String) r14
            r0 = r16
            r5.setRequestProperty(r0, r14)
            goto L_0x0047
        L_0x0061:
             com.xzy.forestSystem.mob.tools.network.StringPart r10 = new  com.xzy.forestSystem.mob.tools.network.StringPart
            r10.<init>()
            if (r20 == 0) goto L_0x0076
             com.xzy.forestSystem.mob.tools.utils.Hashon r14 = new  com.xzy.forestSystem.mob.tools.utils.Hashon
            r14.<init>()
            r0 = r20
            java.lang.String r14 = r14.fromHashMap(r0)
            r10.append(r14)
        L_0x0076:
            r5.connect()
            java.io.OutputStream r9 = r5.getOutputStream()
            java.io.InputStream r7 = r10.toInputStream()
            r14 = 65536(0x10000, float:9.18355E-41)
            byte[] r4 = new byte[r14]
            int r8 = r7.read(r4)
        L_0x0089:
            if (r8 <= 0) goto L_0x0094
            r14 = 0
            r9.write(r4, r14, r8)
            int r8 = r7.read(r4)
            goto L_0x0089
        L_0x0094:
            r9.flush()
            r7.close()
            r9.close()
            if (r23 == 0) goto L_0x00dc
             com.xzy.forestSystem.mob.tools.network.HttpConnectionImpl23 r14 = new  com.xzy.forestSystem.mob.tools.network.HttpConnectionImpl23     // Catch:{ Throwable -> 0x00d5 }
            r14.<init>(r5)     // Catch:{ Throwable -> 0x00d5 }
            r0 = r23
            r0.onResponse(r14)     // Catch:{ Throwable -> 0x00d5 }
            r5.disconnect()
        L_0x00ac:
             com.xzy.forestSystem.mob.tools.log.NLog r14 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r16 = "use time: "
            java.lang.StringBuilder r15 = r15.append(r16)
            long r16 = java.lang.System.currentTimeMillis()
            long r16 = r16 - r12
            java.lang.StringBuilder r15 = r15.append(r16)
            java.lang.String r15 = r15.toString()
            r16 = 0
            r0 = r16
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r16 = r0
            r14.m64i(r15, r16)
            return
        L_0x00d5:
            r11 = move-exception
            throw r11     // Catch:{ all -> 0x00d7 }
        L_0x00d7:
            r14 = move-exception
            r5.disconnect()
            throw r14
        L_0x00dc:
            r5.disconnect()
            goto L_0x00ac
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.network.NetworkHelper.jsonPost(java.lang.String, java.util.HashMap, java.util.ArrayList,  com.xzy.forestSystem.mob.tools.network.NetworkHelper$NetworkTimeOut,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback):void");
    }

    public String httpPost(String url, ArrayList<KVPair<String>> values, KVPair<String> file, ArrayList<KVPair<String>> headers, NetworkTimeOut timeout) throws Throwable {
        return httpPost(url, values, file, headers, 0, timeout);
    }

    public String httpPost(String url, ArrayList<KVPair<String>> values, KVPair<String> file, ArrayList<KVPair<String>> headers, int chunkLength, NetworkTimeOut timeout) throws Throwable {
        ArrayList<KVPair<String>> files = new ArrayList<>();
        if (!(file == null || file.value == null || !new File((String) file.value).exists())) {
            files.add(file);
        }
        return httpPostFiles(url, values, files, headers, chunkLength, timeout);
    }

    public String httpPostFiles(String url, ArrayList<KVPair<String>> values, ArrayList<KVPair<String>> files, ArrayList<KVPair<String>> headers, NetworkTimeOut timeout) throws Throwable {
        return httpPostFiles(url, values, files, headers, 0, timeout);
    }

    public String httpPostFiles(String url, ArrayList<KVPair<String>> values, ArrayList<KVPair<String>> files, ArrayList<KVPair<String>> headers, int chunkLength, NetworkTimeOut timeout) throws Throwable {
        final HashMap<String, String> tmpMap = new HashMap<>();
        httpPost(url, values, files, headers, chunkLength, new HttpResponseCallback() { // from class:  com.xzy.forestSystem.mob.tools.network.NetworkHelper.2
            @Override //  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback
            public void onResponse(HttpConnection conn) throws Throwable {
                int status = conn.getResponseCode();
                if (status == 200 || status < 300) {
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("utf-8")));
                    for (String txt = br.readLine(); txt != null; txt = br.readLine()) {
                        if (sb.length() > 0) {
                            sb.append('\n');
                        }
                        sb.append(txt);
                    }
                    br.close();
                    tmpMap.put("resp", sb.toString());
                    return;
                }
                StringBuilder sb2 = new StringBuilder();
                BufferedReader br2 = new BufferedReader(new InputStreamReader(conn.getErrorStream(), Charset.forName("utf-8")));
                for (String txt2 = br2.readLine(); txt2 != null; txt2 = br2.readLine()) {
                    if (sb2.length() > 0) {
                        sb2.append('\n');
                    }
                    sb2.append(txt2);
                }
                br2.close();
                HashMap<String, Object> errMap = new HashMap<>();
                errMap.put("error", sb2.toString());
                errMap.put("status", Integer.valueOf(status));
                throw new Throwable(new Hashon().fromHashMap(errMap));
            }
        }, timeout);
        return tmpMap.get("resp");
    }

    public String httpPostFilesChecked(String url, ArrayList<KVPair<String>> values, byte[] data, ArrayList<KVPair<String>> headers, int chunkLength, NetworkTimeOut timeout) throws Throwable {
        final HashMap<String, String> tmpMap = new HashMap<>();
        httpPost(url, values, data, headers, chunkLength, new HttpResponseCallback() { // from class:  com.xzy.forestSystem.mob.tools.network.NetworkHelper.3
            @Override //  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback
            public void onResponse(HttpConnection conn) throws Throwable {
                int status = conn.getResponseCode();
                if (status == 200 || status < 300) {
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("utf-8")));
                    for (String txt = br.readLine(); txt != null; txt = br.readLine()) {
                        if (sb.length() > 0) {
                            sb.append('\n');
                        }
                        sb.append(txt);
                    }
                    br.close();
                    tmpMap.put("resp", sb.toString());
                    return;
                }
                StringBuilder sb2 = new StringBuilder();
                BufferedReader br2 = new BufferedReader(new InputStreamReader(conn.getErrorStream(), Charset.forName("utf-8")));
                for (String txt2 = br2.readLine(); txt2 != null; txt2 = br2.readLine()) {
                    if (sb2.length() > 0) {
                        sb2.append('\n');
                    }
                    sb2.append(txt2);
                }
                br2.close();
                HashMap<String, Object> errMap = new HashMap<>();
                errMap.put("error", sb2.toString());
                errMap.put("status", Integer.valueOf(status));
                throw new Throwable(new Hashon().fromHashMap(errMap));
            }
        }, timeout);
        return tmpMap.get("resp");
    }

    public String httpPost(String url, ArrayList<KVPair<String>> headers, int chunkLength, NetworkTimeOut timeout) throws Throwable {
        final HashMap<String, String> tmpMap = new HashMap<>();
        httpPost(url, headers, chunkLength, new HttpResponseCallback() { // from class:  com.xzy.forestSystem.mob.tools.network.NetworkHelper.4
            @Override //  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback
            public void onResponse(HttpConnection conn) throws Throwable {
                int status = conn.getResponseCode();
                if (status == 200 || status < 300) {
                    StringBuilder sb = new StringBuilder();
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("utf-8")));
                    for (String txt = br.readLine(); txt != null; txt = br.readLine()) {
                        if (sb.length() > 0) {
                            sb.append('\n');
                        }
                        sb.append(txt);
                    }
                    br.close();
                    tmpMap.put("resp", sb.toString());
                    return;
                }
                StringBuilder sb2 = new StringBuilder();
                BufferedReader br2 = new BufferedReader(new InputStreamReader(conn.getErrorStream(), Charset.forName("utf-8")));
                for (String txt2 = br2.readLine(); txt2 != null; txt2 = br2.readLine()) {
                    if (sb2.length() > 0) {
                        sb2.append('\n');
                    }
                    sb2.append(txt2);
                }
                br2.close();
                HashMap<String, Object> errMap = new HashMap<>();
                errMap.put("error", sb2.toString());
                errMap.put("status", Integer.valueOf(status));
                throw new Throwable(new Hashon().fromHashMap(errMap));
            }
        }, timeout);
        return tmpMap.get("resp");
    }

    public void httpPost(String url, ArrayList<KVPair<String>> values, ArrayList<KVPair<String>> files, ArrayList<KVPair<String>> headers, HttpResponseCallback callback, NetworkTimeOut timeout) throws Throwable {
        httpPost(url, values, files, headers, 0, callback, timeout);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00ec, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00ed, code lost:
        r5.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00f0, code lost:
        throw r14;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void httpPost(java.lang.String r19, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r20, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r21, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r22, int r23,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback r24,  com.xzy.forestSystem.mob.tools.network.NetworkHelper.NetworkTimeOut r25) throws java.lang.Throwable {
        /*
            r18 = this;
            long r12 = java.lang.System.currentTimeMillis()
             com.xzy.forestSystem.mob.tools.log.NLog r14 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r16 = "httpPost: "
            java.lang.StringBuilder r15 = r15.append(r16)
            r0 = r19
            java.lang.StringBuilder r15 = r15.append(r0)
            java.lang.String r15 = r15.toString()
            r16 = 0
            r0 = r16
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r16 = r0
            r14.m64i(r15, r16)
            r0 = r18
            r1 = r19
            r2 = r25
            java.net.HttpURLConnection r5 = r0.getConnection(r1, r2)
            r14 = 1
            r5.setDoOutput(r14)
            java.lang.String r14 = "Connection"
            java.lang.String r15 = "Keep-Alive"
            r5.setRequestProperty(r14, r15)
            if (r21 == 0) goto L_0x0078
            int r14 = r21.size()
            if (r14 <= 0) goto L_0x0078
            r0 = r18
            r1 = r19
            r2 = r20
            r3 = r21
             com.xzy.forestSystem.mob.tools.network.HTTPPart r10 = r0.getFilePostHTTPPart(r5, r1, r2, r3)
            if (r23 < 0) goto L_0x0058
            r0 = r23
            r5.setChunkedStreamingMode(r0)
        L_0x0058:
            if (r22 == 0) goto L_0x008b
            java.util.Iterator r15 = r22.iterator()
        L_0x005e:
            boolean r14 = r15.hasNext()
            if (r14 == 0) goto L_0x008b
            java.lang.Object r6 = r15.next()
             com.xzy.forestSystem.mob.tools.network.KVPair r6 = ( com.xzy.forestSystem.mob.tools.network.KVPair) r6
            java.lang.String r0 = r6.name
            r16 = r0
            T r14 = r6.value
            java.lang.String r14 = (java.lang.String) r14
            r0 = r16
            r5.setRequestProperty(r0, r14)
            goto L_0x005e
        L_0x0078:
            r0 = r18
            r1 = r19
            r2 = r20
             com.xzy.forestSystem.mob.tools.network.HTTPPart r10 = r0.getTextPostHTTPPart(r5, r1, r2)
            long r14 = r10.length()
            int r14 = (int) r14
            r5.setFixedLengthStreamingMode(r14)
            goto L_0x0058
        L_0x008b:
            r5.connect()
            java.io.OutputStream r9 = r5.getOutputStream()
            java.io.InputStream r7 = r10.toInputStream()
            r14 = 65536(0x10000, float:9.18355E-41)
            byte[] r4 = new byte[r14]
            int r8 = r7.read(r4)
        L_0x009e:
            if (r8 <= 0) goto L_0x00a9
            r14 = 0
            r9.write(r4, r14, r8)
            int r8 = r7.read(r4)
            goto L_0x009e
        L_0x00a9:
            r9.flush()
            r7.close()
            r9.close()
            if (r24 == 0) goto L_0x00f1
             com.xzy.forestSystem.mob.tools.network.HttpConnectionImpl23 r14 = new  com.xzy.forestSystem.mob.tools.network.HttpConnectionImpl23     // Catch:{ Throwable -> 0x00ea }
            r14.<init>(r5)     // Catch:{ Throwable -> 0x00ea }
            r0 = r24
            r0.onResponse(r14)     // Catch:{ Throwable -> 0x00ea }
            r5.disconnect()
        L_0x00c1:
             com.xzy.forestSystem.mob.tools.log.NLog r14 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r16 = "use time: "
            java.lang.StringBuilder r15 = r15.append(r16)
            long r16 = java.lang.System.currentTimeMillis()
            long r16 = r16 - r12
            java.lang.StringBuilder r15 = r15.append(r16)
            java.lang.String r15 = r15.toString()
            r16 = 0
            r0 = r16
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r16 = r0
            r14.m64i(r15, r16)
            return
        L_0x00ea:
            r11 = move-exception
            throw r11     // Catch:{ all -> 0x00ec }
        L_0x00ec:
            r14 = move-exception
            r5.disconnect()
            throw r14
        L_0x00f1:
            r5.disconnect()
            goto L_0x00c1
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.network.NetworkHelper.httpPost(java.lang.String, java.util.ArrayList, java.util.ArrayList, java.util.ArrayList, int,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback,  com.xzy.forestSystem.mob.tools.network.NetworkHelper$NetworkTimeOut):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00e9, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00ea, code lost:
        r5.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00ed, code lost:
        throw r14;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void httpPost(java.lang.String r19, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r20, byte[] r21, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r22, int r23,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback r24,  com.xzy.forestSystem.mob.tools.network.NetworkHelper.NetworkTimeOut r25) throws java.lang.Throwable {
        /*
            r18 = this;
            long r12 = java.lang.System.currentTimeMillis()
             com.xzy.forestSystem.mob.tools.log.NLog r14 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r16 = "httpPost: "
            java.lang.StringBuilder r15 = r15.append(r16)
            r0 = r19
            java.lang.StringBuilder r15 = r15.append(r0)
            java.lang.String r15 = r15.toString()
            r16 = 0
            r0 = r16
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r16 = r0
            r14.m64i(r15, r16)
            r0 = r18
            r1 = r19
            r2 = r25
            java.net.HttpURLConnection r5 = r0.getConnection(r1, r2)
            r14 = 1
            r5.setDoOutput(r14)
            java.lang.String r14 = "Connection"
            java.lang.String r15 = "Keep-Alive"
            r5.setRequestProperty(r14, r15)
            if (r21 == 0) goto L_0x0075
            r0 = r21
            int r14 = r0.length
            if (r14 <= 0) goto L_0x0075
            r0 = r18
            r1 = r19
            r2 = r21
             com.xzy.forestSystem.mob.tools.network.HTTPPart r10 = r0.getDataPostHttpPart(r5, r1, r2)
            if (r23 < 0) goto L_0x0055
            r0 = r23
            r5.setChunkedStreamingMode(r0)
        L_0x0055:
            if (r22 == 0) goto L_0x0088
            java.util.Iterator r15 = r22.iterator()
        L_0x005b:
            boolean r14 = r15.hasNext()
            if (r14 == 0) goto L_0x0088
            java.lang.Object r6 = r15.next()
             com.xzy.forestSystem.mob.tools.network.KVPair r6 = ( com.xzy.forestSystem.mob.tools.network.KVPair) r6
            java.lang.String r0 = r6.name
            r16 = r0
            T r14 = r6.value
            java.lang.String r14 = (java.lang.String) r14
            r0 = r16
            r5.setRequestProperty(r0, r14)
            goto L_0x005b
        L_0x0075:
            r0 = r18
            r1 = r19
            r2 = r20
             com.xzy.forestSystem.mob.tools.network.HTTPPart r10 = r0.getTextPostHTTPPart(r5, r1, r2)
            long r14 = r10.length()
            int r14 = (int) r14
            r5.setFixedLengthStreamingMode(r14)
            goto L_0x0055
        L_0x0088:
            r5.connect()
            java.io.OutputStream r9 = r5.getOutputStream()
            java.io.InputStream r7 = r10.toInputStream()
            r14 = 65536(0x10000, float:9.18355E-41)
            byte[] r4 = new byte[r14]
            int r8 = r7.read(r4)
        L_0x009b:
            if (r8 <= 0) goto L_0x00a6
            r14 = 0
            r9.write(r4, r14, r8)
            int r8 = r7.read(r4)
            goto L_0x009b
        L_0x00a6:
            r9.flush()
            r7.close()
            r9.close()
            if (r24 == 0) goto L_0x00ee
             com.xzy.forestSystem.mob.tools.network.HttpConnectionImpl23 r14 = new  com.xzy.forestSystem.mob.tools.network.HttpConnectionImpl23     // Catch:{ Throwable -> 0x00e7 }
            r14.<init>(r5)     // Catch:{ Throwable -> 0x00e7 }
            r0 = r24
            r0.onResponse(r14)     // Catch:{ Throwable -> 0x00e7 }
            r5.disconnect()
        L_0x00be:
             com.xzy.forestSystem.mob.tools.log.NLog r14 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r16 = "use time: "
            java.lang.StringBuilder r15 = r15.append(r16)
            long r16 = java.lang.System.currentTimeMillis()
            long r16 = r16 - r12
            java.lang.StringBuilder r15 = r15.append(r16)
            java.lang.String r15 = r15.toString()
            r16 = 0
            r0 = r16
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r16 = r0
            r14.m64i(r15, r16)
            return
        L_0x00e7:
            r11 = move-exception
            throw r11     // Catch:{ all -> 0x00e9 }
        L_0x00e9:
            r14 = move-exception
            r5.disconnect()
            throw r14
        L_0x00ee:
            r5.disconnect()
            goto L_0x00be
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.network.NetworkHelper.httpPost(java.lang.String, java.util.ArrayList, byte[], java.util.ArrayList, int,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback,  com.xzy.forestSystem.mob.tools.network.NetworkHelper$NetworkTimeOut):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x00c7, code lost:
        r14 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00c8, code lost:
        r5.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00cb, code lost:
        throw r14;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void httpPost(java.lang.String r19, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r20, int r21,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback r22,  com.xzy.forestSystem.mob.tools.network.NetworkHelper.NetworkTimeOut r23) throws java.lang.Throwable {
        /*
            r18 = this;
            long r12 = java.lang.System.currentTimeMillis()
             com.xzy.forestSystem.mob.tools.log.NLog r14 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r16 = "httpPost: "
            java.lang.StringBuilder r15 = r15.append(r16)
            r0 = r19
            java.lang.StringBuilder r15 = r15.append(r0)
            java.lang.String r15 = r15.toString()
            r16 = 0
            r0 = r16
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r16 = r0
            r14.m64i(r15, r16)
            r0 = r18
            r1 = r19
            r2 = r23
            java.net.HttpURLConnection r5 = r0.getConnection(r1, r2)
            r14 = 1
            r5.setDoOutput(r14)
            java.lang.String r14 = "Connection"
            java.lang.String r15 = "Keep-Alive"
            r5.setRequestProperty(r14, r15)
            if (r20 == 0) goto L_0x005d
            java.util.Iterator r15 = r20.iterator()
        L_0x0043:
            boolean r14 = r15.hasNext()
            if (r14 == 0) goto L_0x005d
            java.lang.Object r6 = r15.next()
             com.xzy.forestSystem.mob.tools.network.KVPair r6 = ( com.xzy.forestSystem.mob.tools.network.KVPair) r6
            java.lang.String r0 = r6.name
            r16 = r0
            T r14 = r6.value
            java.lang.String r14 = (java.lang.String) r14
            r0 = r16
            r5.setRequestProperty(r0, r14)
            goto L_0x0043
        L_0x005d:
             com.xzy.forestSystem.mob.tools.network.StringPart r10 = new  com.xzy.forestSystem.mob.tools.network.StringPart
            r10.<init>()
            r14 = 0
            r10.append(r14)
            r5.connect()
            java.io.OutputStream r9 = r5.getOutputStream()
            java.io.InputStream r7 = r10.toInputStream()
            r14 = 65536(0x10000, float:9.18355E-41)
            byte[] r4 = new byte[r14]
            int r8 = r7.read(r4)
        L_0x0079:
            if (r8 <= 0) goto L_0x0084
            r14 = 0
            r9.write(r4, r14, r8)
            int r8 = r7.read(r4)
            goto L_0x0079
        L_0x0084:
            r9.flush()
            r7.close()
            r9.close()
            if (r22 == 0) goto L_0x00cc
             com.xzy.forestSystem.mob.tools.network.HttpConnectionImpl23 r14 = new  com.xzy.forestSystem.mob.tools.network.HttpConnectionImpl23     // Catch:{ Throwable -> 0x00c5 }
            r14.<init>(r5)     // Catch:{ Throwable -> 0x00c5 }
            r0 = r22
            r0.onResponse(r14)     // Catch:{ Throwable -> 0x00c5 }
            r5.disconnect()
        L_0x009c:
             com.xzy.forestSystem.mob.tools.log.NLog r14 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            java.lang.StringBuilder r15 = new java.lang.StringBuilder
            r15.<init>()
            java.lang.String r16 = "use time: "
            java.lang.StringBuilder r15 = r15.append(r16)
            long r16 = java.lang.System.currentTimeMillis()
            long r16 = r16 - r12
            java.lang.StringBuilder r15 = r15.append(r16)
            java.lang.String r15 = r15.toString()
            r16 = 0
            r0 = r16
            java.lang.Object[] r0 = new java.lang.Object[r0]
            r16 = r0
            r14.m64i(r15, r16)
            return
        L_0x00c5:
            r11 = move-exception
            throw r11     // Catch:{ all -> 0x00c7 }
        L_0x00c7:
            r14 = move-exception
            r5.disconnect()
            throw r14
        L_0x00cc:
            r5.disconnect()
            goto L_0x009c
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.network.NetworkHelper.httpPost(java.lang.String, java.util.ArrayList, int,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback,  com.xzy.forestSystem.mob.tools.network.NetworkHelper$NetworkTimeOut):void");
    }

    private HTTPPart getDataPostHttpPart(HttpURLConnection conn, String url, byte[] data) throws Throwable {
        ByteArrayPart bytePart = new ByteArrayPart();
        bytePart.append(data);
        return bytePart;
    }

    private HTTPPart getFilePostHTTPPart(HttpURLConnection conn, String url, ArrayList<KVPair<String>> values, ArrayList<KVPair<String>> files) throws Throwable {
        String boundary = UUID.randomUUID().toString();
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        MultiPart mp = new MultiPart();
        StringPart sp = new StringPart();
        if (values != null) {
            Iterator<KVPair<String>> it = values.iterator();
            while (it.hasNext()) {
                KVPair<String> value = it.next();
                sp.append("--").append(boundary).append("\r\n");
                sp.append("Content-Disposition: form-data; name=\"").append(value.name).append("\"\r\n\r\n");
                sp.append(value.value).append("\r\n");
            }
        }
        mp.append(sp);
        Iterator<KVPair<String>> it2 = files.iterator();
        while (it2.hasNext()) {
            KVPair<String> file = it2.next();
            StringPart sp2 = new StringPart();
            File imageFile = new File((String) file.value);
            sp2.append("--").append(boundary).append("\r\n");
            sp2.append("Content-Disposition: form-data; name=\"").append(file.name).append("\"; filename=\"").append(imageFile.getName()).append("\"\r\n");
            String mime = URLConnection.getFileNameMap().getContentTypeFor(file.value);
            if (mime == null || mime.length() <= 0) {
                if (file.value.toLowerCase().endsWith("jpg") || file.value.toLowerCase().endsWith("jpeg")) {
                    mime = "image/jpeg";
                } else if (file.value.toLowerCase().endsWith("png")) {
                    mime = "image/png";
                } else if (file.value.toLowerCase().endsWith("gif")) {
                    mime = "image/gif";
                } else {
                    FileInputStream fis = new FileInputStream((String) file.value);
                    mime = URLConnection.guessContentTypeFromStream(fis);
                    fis.close();
                    if (mime == null || mime.length() <= 0) {
                        mime = "application/octet-stream";
                    }
                }
            }
            sp2.append("Content-Type: ").append(mime).append("\r\n\r\n");
            mp.append(sp2);
            FilePart fp = new FilePart();
            fp.setFile((String) file.value);
            mp.append(fp);
            StringPart sp3 = new StringPart();
            sp3.append("\r\n");
            mp.append(sp3);
        }
        StringPart sp4 = new StringPart();
        sp4.append("--").append(boundary).append("--\r\n");
        mp.append(sp4);
        return mp;
    }

    private HTTPPart getTextPostHTTPPart(HttpURLConnection conn, String url, ArrayList<KVPair<String>> values) throws Throwable {
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        StringPart sp = new StringPart();
        if (values != null) {
            sp.append(kvPairsToUrl(values));
        }
        return sp;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x00da, code lost:
        r20 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x00db, code lost:
        if (r9 != null) goto L_0x00dd;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        r9.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x00e0, code lost:
        r6.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00e3, code lost:
        throw r20;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void rawPost(java.lang.String r25, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r26,  com.xzy.forestSystem.mob.tools.network.HTTPPart r27,  com.xzy.forestSystem.mob.tools.network.RawNetworkCallback r28,  com.xzy.forestSystem.mob.tools.network.NetworkHelper.NetworkTimeOut r29) throws java.lang.Throwable {
        /*
        // Method dump skipped, instructions count: 349
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.network.NetworkHelper.rawPost(java.lang.String, java.util.ArrayList,  com.xzy.forestSystem.mob.tools.network.HTTPPart,  com.xzy.forestSystem.mob.tools.network.RawNetworkCallback,  com.xzy.forestSystem.mob.tools.network.NetworkHelper$NetworkTimeOut):void");
    }

    public void rawPost(String url, ArrayList<KVPair<String>> headers, HTTPPart data, HttpResponseCallback callback, NetworkTimeOut timeout) throws Throwable {
        rawPost(url, headers, data, 0, callback, timeout);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00b1, code lost:
        r11 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x00b2, code lost:
        r5.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x00b5, code lost:
        throw r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void rawPost(java.lang.String r19, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r20,  com.xzy.forestSystem.mob.tools.network.HTTPPart r21, int r22,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback r23,  com.xzy.forestSystem.mob.tools.network.NetworkHelper.NetworkTimeOut r24) throws java.lang.Throwable {
        /*
            r18 = this;
            long r12 = java.lang.System.currentTimeMillis()
             com.xzy.forestSystem.mob.tools.log.NLog r11 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = "rawpost: "
            java.lang.StringBuilder r14 = r14.append(r15)
            r0 = r19
            java.lang.StringBuilder r14 = r14.append(r0)
            java.lang.String r14 = r14.toString()
            r15 = 0
            java.lang.Object[] r15 = new java.lang.Object[r15]
            r11.m64i(r14, r15)
            r0 = r18
            r1 = r19
            r2 = r24
            java.net.HttpURLConnection r5 = r0.getConnection(r1, r2)
            r11 = 1
            r5.setDoOutput(r11)
            if (r22 < 0) goto L_0x0037
            r11 = 0
            r5.setChunkedStreamingMode(r11)
        L_0x0037:
            if (r20 == 0) goto L_0x0053
            java.util.Iterator r14 = r20.iterator()
        L_0x003d:
            boolean r11 = r14.hasNext()
            if (r11 == 0) goto L_0x0053
            java.lang.Object r6 = r14.next()
             com.xzy.forestSystem.mob.tools.network.KVPair r6 = ( com.xzy.forestSystem.mob.tools.network.KVPair) r6
            java.lang.String r15 = r6.name
            T r11 = r6.value
            java.lang.String r11 = (java.lang.String) r11
            r5.setRequestProperty(r15, r11)
            goto L_0x003d
        L_0x0053:
            r5.connect()
            java.io.OutputStream r9 = r5.getOutputStream()
            java.io.InputStream r7 = r21.toInputStream()
            r11 = 65536(0x10000, float:9.18355E-41)
            byte[] r4 = new byte[r11]
            int r8 = r7.read(r4)
        L_0x0066:
            if (r8 <= 0) goto L_0x0071
            r11 = 0
            r9.write(r4, r11, r8)
            int r8 = r7.read(r4)
            goto L_0x0066
        L_0x0071:
            r9.flush()
            r7.close()
            r9.close()
            if (r23 == 0) goto L_0x00b6
             com.xzy.forestSystem.mob.tools.network.HttpConnectionImpl23 r11 = new  com.xzy.forestSystem.mob.tools.network.HttpConnectionImpl23     // Catch:{ Throwable -> 0x00af }
            r11.<init>(r5)     // Catch:{ Throwable -> 0x00af }
            r0 = r23
            r0.onResponse(r11)     // Catch:{ Throwable -> 0x00af }
            r5.disconnect()
        L_0x0089:
             com.xzy.forestSystem.mob.tools.log.NLog r11 =  com.xzy.forestSystem.mob.tools.MobLog.getInstance()
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            r14.<init>()
            java.lang.String r15 = "use time: "
            java.lang.StringBuilder r14 = r14.append(r15)
            long r16 = java.lang.System.currentTimeMillis()
            long r16 = r16 - r12
            r0 = r16
            java.lang.StringBuilder r14 = r14.append(r0)
            java.lang.String r14 = r14.toString()
            r15 = 0
            java.lang.Object[] r15 = new java.lang.Object[r15]
            r11.m64i(r14, r15)
            return
        L_0x00af:
            r10 = move-exception
            throw r10     // Catch:{ all -> 0x00b1 }
        L_0x00b1:
            r11 = move-exception
            r5.disconnect()
            throw r11
        L_0x00b6:
            r5.disconnect()
            goto L_0x0089
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.network.NetworkHelper.rawPost(java.lang.String, java.util.ArrayList,  com.xzy.forestSystem.mob.tools.network.HTTPPart, int,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback,  com.xzy.forestSystem.mob.tools.network.NetworkHelper$NetworkTimeOut):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0103, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0104, code lost:
        r5.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0107, code lost:
        throw r13;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void getHttpPostResponse(java.lang.String r21, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r22,  com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String> r23, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r24,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback r25,  com.xzy.forestSystem.mob.tools.network.NetworkHelper.NetworkTimeOut r26) throws java.lang.Throwable {
        /*
        // Method dump skipped, instructions count: 268
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.network.NetworkHelper.getHttpPostResponse(java.lang.String, java.util.ArrayList,  com.xzy.forestSystem.mob.tools.network.KVPair, java.util.ArrayList,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback,  com.xzy.forestSystem.mob.tools.network.NetworkHelper$NetworkTimeOut):void");
    }

    public String httpPut(String url, ArrayList<KVPair<String>> values, KVPair<String> file, ArrayList<KVPair<String>> headers, NetworkTimeOut timeout) throws Throwable {
        return httpPut(url, values, file, headers, timeout, null);
    }

    public String httpPut(String url, ArrayList<KVPair<String>> values, KVPair<String> file, ArrayList<KVPair<String>> headers, NetworkTimeOut timeout, OnReadListener listener) throws Throwable {
        long time = System.currentTimeMillis();
        MobLog.getInstance().m64i("httpPut: " + url, new Object[0]);
        if (values != null) {
            String param = kvPairsToUrl(values);
            if (param.length() > 0) {
                url = url + "?" + param;
            }
        }
        HttpURLConnection conn = getConnection(url, timeout);
        conn.setDoOutput(true);
        conn.setChunkedStreamingMode(0);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/octet-stream");
        if (headers != null) {
            Iterator<KVPair<String>> it = headers.iterator();
            while (it.hasNext()) {
                KVPair<String> header = it.next();
                conn.setRequestProperty(header.name, header.value);
            }
        }
        conn.connect();
        OutputStream os = conn.getOutputStream();
        FilePart fp = new FilePart();
        if (listener != null) {
            fp.setOnReadListener(listener);
        }
        fp.setFile((String) file.value);
        InputStream is = fp.toInputStream();
        byte[] buf = new byte[65536];
        for (int len = is.read(buf); len > 0; len = is.read(buf)) {
            os.write(buf, 0, len);
        }
        os.flush();
        is.close();
        os.close();
        int status = conn.getResponseCode();
        if (status == 200 || status == 201) {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName("utf-8")));
            for (String txt = br.readLine(); txt != null; txt = br.readLine()) {
                if (sb.length() > 0) {
                    sb.append('\n');
                }
                sb.append(txt);
            }
            br.close();
            conn.disconnect();
            String resp = sb.toString();
            MobLog.getInstance().m64i("use time: " + (System.currentTimeMillis() - time), new Object[0]);
            return resp;
        }
        StringBuilder sb2 = new StringBuilder();
        BufferedReader br2 = new BufferedReader(new InputStreamReader(conn.getErrorStream(), Charset.forName("utf-8")));
        for (String txt2 = br2.readLine(); txt2 != null; txt2 = br2.readLine()) {
            if (sb2.length() > 0) {
                sb2.append('\n');
            }
            sb2.append(txt2);
        }
        br2.close();
        HashMap<String, Object> errMap = new HashMap<>();
        errMap.put("error", sb2.toString());
        errMap.put("status", Integer.valueOf(status));
        throw new Throwable(new Hashon().fromHashMap(errMap));
    }

    public ArrayList<KVPair<String[]>> httpHead(String url, ArrayList<KVPair<String>> values, KVPair<String> kVPair, ArrayList<KVPair<String>> headers, NetworkTimeOut timeout) throws Throwable {
        long time = System.currentTimeMillis();
        MobLog.getInstance().m64i("httpHead: " + url, new Object[0]);
        if (values != null) {
            String param = kvPairsToUrl(values);
            if (param.length() > 0) {
                url = url + "?" + param;
            }
        }
        HttpURLConnection conn = getConnection(url, timeout);
        conn.setRequestMethod("HEAD");
        if (headers != null) {
            Iterator<KVPair<String>> it = headers.iterator();
            while (it.hasNext()) {
                KVPair<String> header = it.next();
                conn.setRequestProperty(header.name, header.value);
            }
        }
        conn.connect();
        Map<String, List<String>> map = conn.getHeaderFields();
        ArrayList<KVPair<String[]>> list = new ArrayList<>();
        if (map != null) {
            for (Map.Entry<String, List<String>> ent : map.entrySet()) {
                List<String> value = ent.getValue();
                if (value == null) {
                    list.add(new KVPair<>(ent.getKey(), new String[0]));
                } else {
                    String[] hds = new String[value.size()];
                    for (int i = 0; i < hds.length; i++) {
                        hds[i] = value.get(i);
                    }
                    list.add(new KVPair<>(ent.getKey(), hds));
                }
            }
        }
        conn.disconnect();
        MobLog.getInstance().m64i("use time: " + (System.currentTimeMillis() - time), new Object[0]);
        return list;
    }

    public void httpPatch(String url, ArrayList<KVPair<String>> values, KVPair<String> file, long offset, ArrayList<KVPair<String>> headers, OnReadListener listener, HttpResponseCallback callback, NetworkTimeOut timeout) throws Throwable {
        if (Build.VERSION.SDK_INT >= 23) {
            httpPatchImpl23(url, values, file, offset, headers, listener, callback, timeout);
        } else {
            httpPatchImpl(url, values, file, offset, headers, listener, callback, timeout);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x01c7, code lost:
        r27 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x01c8, code lost:
        r5.getConnectionManager().shutdown();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x01cf, code lost:
        throw r27;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void httpPatchImpl(java.lang.String r33, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r34,  com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String> r35, long r36, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r38,  com.xzy.forestSystem.mob.tools.network.OnReadListener r39,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback r40,  com.xzy.forestSystem.mob.tools.network.NetworkHelper.NetworkTimeOut r41) throws java.lang.Throwable {
        /*
        // Method dump skipped, instructions count: 472
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.network.NetworkHelper.httpPatchImpl(java.lang.String, java.util.ArrayList,  com.xzy.forestSystem.mob.tools.network.KVPair, long, java.util.ArrayList,  com.xzy.forestSystem.mob.tools.network.OnReadListener,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback,  com.xzy.forestSystem.mob.tools.network.NetworkHelper$NetworkTimeOut):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x011a, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x011b, code lost:
        r5.disconnect();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x011e, code lost:
        throw r13;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void httpPatchImpl23(java.lang.String r21, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r22,  com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String> r23, long r24, java.util.ArrayList< com.xzy.forestSystem.mob.tools.network.KVPair<java.lang.String>> r26,  com.xzy.forestSystem.mob.tools.network.OnReadListener r27,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback r28,  com.xzy.forestSystem.mob.tools.network.NetworkHelper.NetworkTimeOut r29) throws java.lang.Throwable {
        /*
        // Method dump skipped, instructions count: 291
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.network.NetworkHelper.httpPatchImpl23(java.lang.String, java.util.ArrayList,  com.xzy.forestSystem.mob.tools.network.KVPair, long, java.util.ArrayList,  com.xzy.forestSystem.mob.tools.network.OnReadListener,  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback,  com.xzy.forestSystem.mob.tools.network.NetworkHelper$NetworkTimeOut):void");
    }

    private String kvPairsToUrl(ArrayList<KVPair<String>> values) throws Throwable {
        StringBuilder sb = new StringBuilder();
        Iterator<KVPair<String>> it = values.iterator();
        while (it.hasNext()) {
            KVPair<String> value = it.next();
            String encodedName = Data.urlEncode(value.name, "utf-8");
            String encodedValue = value.value != null ? Data.urlEncode(value.value, "utf-8") : "";
            if (sb.length() > 0) {
                sb.append('&');
            }
            sb.append(encodedName).append('=').append(encodedValue);
        }
        return sb.toString();
    }

    private HttpURLConnection getConnection(String urlStr, NetworkTimeOut timeout) throws Throwable {
        Object obj = null;
        int readTimout2;
        Object methods;
        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        String filedName = "methodTokens";
        boolean staticType = false;
        Object methods2 = null;
        if (0 != 0) {
            try {
                methods2 = ReflectHelper.getStaticField("HttpURLConnection", filedName);
            } catch (Throwable th) {
            }
        } else {
            methods2 = ReflectHelper.getInstanceField(conn, filedName);
        }
        if (methods2 == null) {
            filedName = "PERMITTED_USER_METHODS";
            staticType = true;
            if (1 != 0) {
                try {
                    methods = ReflectHelper.getStaticField("HttpURLConnection", filedName);
                } catch (Throwable th2) {
                    obj = methods2;
                }
            } else {
                methods = ReflectHelper.getInstanceField(conn, filedName);
            }
//            obj = methods;
        } else {
            obj = methods2;
        }
        if (obj != null) {
            String[] methodTokens = (String[]) obj;
            String[] myMethodTokens = new String[(methodTokens.length + 1)];
            System.arraycopy(methodTokens, 0, myMethodTokens, 0, methodTokens.length);
            myMethodTokens[methodTokens.length] = HttpPatch.METHOD_NAME;
            if (staticType) {
                ReflectHelper.setStaticField("HttpURLConnection", filedName, myMethodTokens);
            } else {
                ReflectHelper.setInstanceField(conn, filedName, myMethodTokens);
            }
        }
        if (Build.VERSION.SDK_INT < 8) {
            System.setProperty("http.keepAlive", "false");
        }
        if (conn instanceof HttpsURLConnection) {
            HostnameVerifier hostnameVerifier = SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
            HttpsURLConnection httpsConn = (HttpsURLConnection) conn;
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new SimpleX509TrustManager(null)}, new SecureRandom());
            httpsConn.setSSLSocketFactory(sc.getSocketFactory());
            httpsConn.setHostnameVerifier(hostnameVerifier);
        }
        int connectionTimeout2 = timeout == null ? connectionTimeout : timeout.connectionTimeout;
        if (connectionTimeout2 > 0) {
            conn.setConnectTimeout(connectionTimeout2);
        }
        if (timeout == null) {
            readTimout2 = readTimout;
        } else {
            readTimout2 = timeout.readTimout;
        }
        if (readTimout2 > 0) {
            conn.setReadTimeout(readTimout2);
        }
        return conn;
    }

    public static final class SimpleX509TrustManager implements X509TrustManager {
        private X509TrustManager standardTrustManager;

        public SimpleX509TrustManager(KeyStore keystore) {
            try {
                TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
                tmf.init(keystore);
                TrustManager[] trustManagers = tmf.getTrustManagers();
                if (trustManagers == null || trustManagers.length == 0) {
                    throw new NoSuchAlgorithmException("no trust manager found.");
                }
                this.standardTrustManager = (X509TrustManager) trustManagers[0];
            } catch (Exception e) {
                MobLog.getInstance().m70d("failed to initialize the standard trust manager: " + e.getMessage(), new Object[0]);
                this.standardTrustManager = null;
            }
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
        }

        @Override // javax.net.ssl.X509TrustManager
        public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {
            if (certificates == null) {
                throw new IllegalArgumentException("there were no certificates.");
            } else if (certificates.length == 1) {
                certificates[0].checkValidity();
            } else if (this.standardTrustManager != null) {
                this.standardTrustManager.checkServerTrusted(certificates, authType);
            } else {
                throw new CertificateException("there were one more certificates but no trust manager found.");
            }
        }

        @Override // javax.net.ssl.X509TrustManager
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
