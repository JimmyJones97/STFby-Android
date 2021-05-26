package com.xzy.forestSystem.baidu.speech;

import android.net.http.AndroidHttpClient;
import com.xzy.forestSystem.baidu.speech.HttpCallable;
import com.xzy.forestSystem.baidu.speech.Results;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.apache.http.params.HttpConnectionParams;

/* access modifiers changed from: package-private */
public class MulThreadDecoder extends AbsStreamDecoder {
    private static final int NETWORK_TIMEOUT = 8000;
    private AndroidHttpClient mClient;
    private ExecutorService mExecutorService = null;
    private final LinkedList<Future<HttpCallable.Result>> mFutureResults = new LinkedList<>();
    private int mIdx;
    private String tok;
    private String usingUrl;

    public MulThreadDecoder(Map<String, Object> params) throws IOException {
        super(null, params);
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.speech.AbsStreamDecoder
    public void onCreate() throws Exception {
        this.mExecutorService = Executors.newFixedThreadPool(3);
        synchronized (this) {
            if (this.mClient == null) {
                this.mClient = AndroidHttpClient.newInstance("");
            }
        }
        HttpConnectionParams.setConnectionTimeout(this.mClient.getParams(), 8000);
        HttpConnectionParams.setSoTimeout(this.mClient.getParams(), 8000);
        URL u = new URL((String) this.mParams.get("decoder-server.url"));
        try {
            this.usingUrl = new URL("http://" + InetAddress.getByName(u.getHost()).getHostAddress() + (u.getPort() > 0 ? ":" + u.getPort() : "") + u.getPath()).toString();
            if (((Boolean) this.mParams.get("decoder-server.auth")).booleanValue()) {
                this.tok = (String) this.mExecutorService.submit(new TokenCallable(this.mClient, this.mParams)).get();
            }
        } catch (UnknownHostException e) {
            throw new Exception("#2, Other network related errors., unknown host: " + u.getHost(), e);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x00b0  */
    @Override // com.baidu.speech.AbsStreamDecoder
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onExecute(int r20, java.io.InputStream[] r21) throws java.lang.Exception {
        /*
        // Method dump skipped, instructions count: 492
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.speech.MulThreadDecoder.onExecute(int, java.io.InputStream[]):void");
    }

    /* access modifiers changed from: protected */
    @Override // com.baidu.speech.AbsStreamDecoder
    public void onDestroy() throws Exception {
        this.mClient.close();
        if (this.mExecutorService != null) {
            this.mExecutorService.shutdownNow();
        }
    }

    private HttpCallable callable(int tid, HashMap<String, Object> params, byte[] buf, int off, int len, boolean eof) throws ExecutionException, InterruptedException {
        int i = this.mIdx + 1;
        this.mIdx = i;
        int httpIdx = i * (eof ? -1 : 1);
        byte[] block = new byte[len];
        System.arraycopy(buf, off, block, 0, len);
        return new HttpCallable(tid, this.mClient, block, params, this.mGlb, httpIdx);
    }

    private boolean needParseMoreResult() throws Exception {
        while (true) {
            Future<HttpCallable.Result> p = this.mFutureResults.peek();
            if (p == null || !p.isDone()) {
                break;
            }
            this.mFutureResults.remove(p);
            HttpCallable.Result s = p.get();
            Results.Result r = new Parser().parse(s.response);
            r.setId(s.tid);
            appendResult(r);
            if (r instanceof Results.FinalResult) {
                return false;
            }
            Thread.sleep(1);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void finalize() throws Throwable {
        super.finalize();
        if (!isClosed()) {
            close();
            throw new IllegalStateException("Leak found, " + getClass().getSimpleName() + " created and never closed");
        }
    }
}
