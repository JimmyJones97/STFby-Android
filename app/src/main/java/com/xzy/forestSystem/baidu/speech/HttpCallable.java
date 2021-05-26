package com.xzy.forestSystem.baidu.speech;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPOutputStream;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONException;
import org.json.JSONObject;

/* access modifiers changed from: package-private */
public final class HttpCallable implements Callable<HttpCallable.Result> {
    private static final int ERR_NO_0 = 0;
    private static final int ERR_NO_3001 = -3001;
    private static final int ERR_NO_3002 = -3002;
    private static final int ERR_NO_3003 = -3003;
    private static final int ERR_NO_3004 = -3004;
    private static final int ERR_NO_3005 = -3005;
    private static final int ERR_NO_3006 = -3006;
    private static final String PARAMS_BASIC = "app, idx, rtn, glb, pdt, fun, ptc, enc";
    private static final String PARAMS_KEY_LEFT = "decoder-server.";
    private static final String PARAMS_PKG_FIRST_NLU_NO = "app, idx, rtn, glb, pdt, fun, ptc, enc, uid, ver, pfm";
    private static final String PARAMS_PKG_FIRST_NLU_YES = "app, idx, rtn, glb, pdt, fun, ptc, enc, uid, ver, pfm";
    private static final String PARAMS_PKG_FIRST_OPTIONAL = "tok, city_id, pam, prop_list, bua, cok, pu, frm, rsv";
    private static final String TAG = "HttpCallable";
    private static final Logger logger = Logger.getLogger(TAG);
    private final byte[] block;
    private final boolean debug;
    private BufferedWriter debugOutput = null;
    private final String glb;
    private final HttpClient httpClient;
    private final int idx;
    private final Map<String, Object> params;
    private long requestTime;
    private final int tid;

    public HttpCallable(int tid2, HttpClient httpClient2, byte[] block2, Map<String, Object> params2, String glb2, int idx2) {
        this.tid = tid2;
        this.block = block2;
        this.glb = glb2;
        this.idx = idx2;
        HashMap<String, Object> paramsCopy = new HashMap<>(params2);
        paramsCopy.put("decoder-server.glb", glb2);
        paramsCopy.put("decoder-server.idx", Integer.valueOf(idx2));
        this.params = paramsCopy;
        this.httpClient = httpClient2;
        this.debug = Boolean.TRUE.equals(params2.get("debug.debug"));
        String output_dir = (String) params2.get("debug.output-dir");
        String runtimeName = (String) params2.get("basic.runtime-name");
        String taskName = (String) params2.get("basic.task-name");
        if (this.debug && output_dir != null) {
            try {
                File dest = new File(output_dir, runtimeName);
                dest.mkdirs();
                this.debugOutput = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(dest, taskName + "_idx=" + idx2 + ".http.txt"))));
            } catch (FileNotFoundException e) {
                throw new UnsupportedOperationException("open debug failed", e);
            }
        }
        if (this.debugOutput != null) {
            try {
                this.debugOutput.write("==== TASK ====\r\n");
                this.debugOutput.write(String.format("size：\t %s\r\n", Integer.valueOf(block2.length)));
                this.debugOutput.write(String.format("glb：\t%s\r\n", glb2));
                this.debugOutput.write(String.format("idx：\t%s\r\n", Integer.valueOf(idx2)));
                this.debugOutput.write(String.format("\r\n", new Object[0]));
                this.debugOutput.flush();
            } catch (IOException e2) {
                throw new UnsupportedOperationException("write log failed", e2);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public HttpUriRequest readyRequest() {
        try {
            JSONObject obj = generateParams(this.params);
            String jsonStr = obj.toString();
            logger.info("request: " + obj.toString());
            byte[] gzipJson = gZip(jsonStr.getBytes("utf-8"));
            gzipJson[0] = 117;
            gzipJson[1] = 123;
            byte[] start = "\r\n----BD**VR++LIB\r\n".getBytes();
            byte[] end = "\r\n----BD**VR++LIB--\r\n".getBytes();
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            data.write(start);
            data.write(gzipJson);
            data.write(start);
            data.write(this.block);
            data.write(end);
            ByteArrayEntity bae = new ByteArrayEntity(data.toByteArray());
            bae.setContentType("multipart/form-data; boundary=--BD**VR++LIB");
            HttpPost post = new HttpPost((String) getParamOrThrow(this.params, "decoder-server.url"));
            if (this.debug) {
                TreeMap<String, Object> tree = new TreeMap<>();
                Iterator<String> it = obj.keys();
                while (it.hasNext()) {
                    String key = it.next();
                    tree.put(key, obj.get(key));
                }
                for (Map.Entry<String, Object> entity : tree.entrySet()) {
                    post.setHeader("debug-" + entity.getKey(), entity.getValue() + "");
                }
            }
            post.setEntity(bae);
            if (this.debugOutput != null) {
                try {
                    this.debugOutput.write("--------\r\nREQUEST INFO\r\n");
                    this.debugOutput.write(String.format("%s：\t %s\r\n", post.getMethod(), post.getURI()));
                    this.debugOutput.write(String.format("%s\r\n", obj.toString(4)));
                    this.debugOutput.flush();
                } catch (IOException e) {
                    throw new UnsupportedOperationException("write log failed", e);
                }
            }
            return post;
        } catch (Exception e2) {
            throw new IllegalArgumentException("can't create HttpCallable instance", e2);
        }
    }

    public boolean isReady() {
        return this.requestTime > 0 && System.currentTimeMillis() - this.requestTime > 10;
    }

    /* JADX INFO: finally extract failed */
    @Override // java.util.concurrent.Callable
    public Result call() throws Exception {
        HttpUriRequest httpRequest = readyRequest();
        try {
            Result result = new Result(this.tid, _call(httpRequest));
            if (this.debugOutput != null) {
                this.debugOutput.close();
            }
            return result;
        } catch (Exception e) {
            if (this.debugOutput != null) {
                try {
                    this.debugOutput.write("--------\r\nRESPONSE INFO\r\n");
                    ByteArrayOutputStream ooo = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(ooo);
                    e.printStackTrace(ps);
                    ps.close();
                    this.debugOutput.write(String.format("%s\r\n", ooo.toString()));
                    this.debugOutput.flush();
                } catch (IOException e2) {
                    throw new UnsupportedOperationException("write log failed", e);
                }
            }
            logger.log(Level.INFO, String.format("POST %s\n", httpRequest.getURI()), (Throwable) e);
            throw e;
        } catch (Throwable th) {
            if (this.debugOutput != null) {
                this.debugOutput.close();
            }
            throw th;
        }
    }

    private void checkData(String content) throws Exception {
        try {
            new JSONObject(content);
            String error = "";
            Matcher m = Pattern.compile("\"err_no\": ?(-?\\d{1,5})?").matcher(content);
            if (m.find()) {
                Matcher m2 = Pattern.compile("\"error\": ?\"(.*?)\"").matcher(content);
                if (m2.find()) {
                    error = m2.group(1);
                }
                int err_no = Integer.parseInt(m.group(1));
                switch (err_no) {
                    case ERR_NO_3006 /* -3006 */:
                    case ERR_NO_3005 /* -3005 */:
                        throw new Exception("#7, No recognition result matched., reason: " + err_no + ", " + error + ", idx " + this.idx + ",sn " + this.glb);
                    case ERR_NO_3004 /* -3004 */:
                    case ERR_NO_3003 /* -3003 */:
                    case ERR_NO_3002 /* -3002 */:
                    case ERR_NO_3001 /* -3001 */:
                        throw new Exception("#4, Server sends error status., reason: " + err_no + ", " + error + ", idx " + this.idx + ",sn " + this.glb);
                    case 0:
                        return;
                    default:
                        throw new Exception("#4, Server sends error status., reason: " + err_no + ", " + error + ", idx " + this.idx + ",sn " + this.glb);
                }
            }
        } catch (JSONException e) {
            throw new Exception("#4, Server sends error status., parse json failed, " + content, e);
        }
    }

    /* JADX DEBUG: Can't convert new array creation: APUT found in different block: 0x004a: APUT  (r7v2 java.lang.Object[]), (0 ??[int, short, byte, char]), (r4v14 java.lang.String) */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x006e, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0095, code lost:
        throw new java.lang.Exception("#1, Network operation timed out., asr failed, socket timeout, sn=" + r10.glb + ", idx=" + r10.idx, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x009c, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x00c3, code lost:
        throw new java.lang.Exception("#1, Network operation timed out., asr failed, connect timeout, sn=" + r10.glb + ", idx=" + r10.idx, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x00c4, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x00eb, code lost:
        throw new java.lang.Exception("#2, Other network related errors., asr failed, sn=" + r10.glb + ", idx=" + r10.idx, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x00ec, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0113, code lost:
        throw new java.lang.Exception("#2, Other network related errors., asr failed, sn=" + r10.glb + ", idx=" + r10.idx, r3);
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x006e A[ExcHandler: SocketTimeoutException (r3v3 'e' java.net.SocketTimeoutException A[CUSTOM_DECLARE]), Splitter:B:0:0x0000] */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x009c A[ExcHandler: ConnectTimeoutException (r3v2 'e' org.apache.http.conn.ConnectTimeoutException A[CUSTOM_DECLARE]), Splitter:B:0:0x0000] */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00c4 A[ExcHandler: ClientProtocolException (r3v1 'e' org.apache.http.client.ClientProtocolException A[CUSTOM_DECLARE]), Splitter:B:0:0x0000] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String _call(org.apache.http.client.methods.HttpUriRequest r11) throws java.lang.Exception {
        /*
        // Method dump skipped, instructions count: 276
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.speech.HttpCallable._call(org.apache.http.client.methods.HttpUriRequest):java.lang.String");
    }

    private JSONObject generateParams(Map<String, Object> params2) throws IOException, JSONException {
        JSONObject obj = new JSONObject();
        int idx2 = ((Integer) getParamOrThrow(params2, "decoder-server.idx")).intValue();
        for (String tmp : (1 == Math.abs(idx2) ? 305 == ((Integer) getParamOrThrow(params2, "decoder-server.ptc")).intValue() ? "app, idx, rtn, glb, pdt, fun, ptc, enc, uid, ver, pfm" : "app, idx, rtn, glb, pdt, fun, ptc, enc, uid, ver, pfm" : PARAMS_BASIC).split(",")) {
            String key = tmp.trim();
            obj.put(key, getParamOrThrow(params2, PARAMS_KEY_LEFT + key));
        }
        if (1 == Math.abs(idx2)) {
            for (String tmp2 : PARAMS_PKG_FIRST_OPTIONAL.split(",")) {
                String key2 = tmp2.trim();
                Object val = params2.get(PARAMS_KEY_LEFT + key2);
                if (val != null) {
                    obj.put(key2, "" + val);
                }
            }
        }
        return obj;
    }

    private Object getParamOrThrow(Map<String, Object> params2, String key) throws IOException {
        Object val = params2.get(key);
        if (val != null) {
            return val;
        }
        throw new IOException("Missing parameter " + key);
    }

    private static byte[] gZip(byte[] data) {
        byte[] b = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(data);
            gzip.finish();
            gzip.close();
            b = bos.toByteArray();
            bos.close();
            return b;
        } catch (Exception ex) {
            ex.printStackTrace();
            return b;
        }
    }

    public static class Result {
        public String response = null;
        public int tid = -1;

        public Result(int tid2, String response2) {
            this.tid = tid2;
            this.response = response2;
        }
    }
}
