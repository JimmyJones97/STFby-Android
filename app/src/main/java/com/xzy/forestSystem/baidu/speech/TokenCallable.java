package com.xzy.forestSystem.baidu.speech;

import androidx.core.os.EnvironmentCompat;

import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/* access modifiers changed from: package-private */
public class TokenCallable implements Callable<String> {
    private static final String TAG = "TokenCallable";
    private static final Logger logger = Logger.getLogger(TAG);
    private static String sOldKeySecretCode;
    private static String sOldToken;
    private static long sTokenExpireTime;
    private final HttpClient httpClient;
    private final HttpUriRequest httpRequest;
    private final String key;
    private final boolean mForceRefresh;
    private final String mKeySecretCode;
    private final String secret;

    public TokenCallable(HttpClient httpClient2, Map<String, Object> params) {
        try {
            this.key = (String) getParamOrThrow(params, "decoder-server.key");
            this.secret = (String) getParamOrThrow(params, "decoder-server.secret");
            this.mKeySecretCode = this.key + "_" + this.secret;
            if (Boolean.TRUE.equals(params.get("basic.refresh-token"))) {
                this.mForceRefresh = true;
            } else {
                this.mForceRefresh = false;
            }
            this.httpRequest = new HttpGet(String.format("https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials&client_id=%s&client_secret=%s", this.key, this.secret));
            this.httpClient = httpClient2;
        } catch (Exception e) {
            throw new IllegalArgumentException("can't create TokenCallable instance", e);
        }
    }

    @Override // java.util.concurrent.Callable
    public String call() throws Exception {
        boolean needRefresh = false;
        try {
            if (sOldToken == null) {
                needRefresh = true;
                logger.log(Level.INFO, String.format("token not found, so request token", new Object[0]));
            } else if (this.mForceRefresh) {
                needRefresh = true;
                logger.log(Level.INFO, String.format("need refresh token, so request token", new Object[0]));
            } else if (System.nanoTime() >= sTokenExpireTime) {
                needRefresh = true;
                logger.log(Level.INFO, String.format("token expired, so request token", new Object[0]));
            } else if (!sOldKeySecretCode.equals(this.mKeySecretCode)) {
                needRefresh = true;
                logger.log(Level.INFO, String.format("key or secret has been switched, so request token", new Object[0]));
            } else {
                logger.log(Level.INFO, String.format("token is available, skip request token", new Object[0]));
            }
            if (!needRefresh) {
                return sOldToken;
            }
            try {
                logger.log(Level.INFO, String.format("token requesting...", new Object[0]));
                String content = (String) this.httpClient.execute(this.httpRequest, new ResponseHandler<String>() { // from class: com.baidu.speech.TokenCallable.1
                    public String handleResponse(HttpResponse response) throws IOException {
                        StatusLine statusLine = response.getStatusLine();
                        if (statusLine.getStatusCode() >= 500) {
                            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
                        }
                        HttpEntity entity = response.getEntity();
                        if (entity == null) {
                            return null;
                        }
                        return EntityUtils.toString(entity, "utf-8");
                    }
                });
                logger.log(Level.INFO, String.format("GET %s \n\t%s", this.httpRequest.getURI(), content));
                JSONObject json = new JSONObject(content);
                if (!json.has("access_token")) {
                    throw new Exception("#5, Other client side errors. request token failed, error: " + json.optString("access_token", EnvironmentCompat.MEDIA_UNKNOWN) + ", desc: " + json.optString("error_description", EnvironmentCompat.MEDIA_UNKNOWN) + ", used AK=" + this.key + FileSelector_Dialog.sRoot + this.secret);
                }
                String token = json.getString("access_token");
                sTokenExpireTime = System.nanoTime() + (Math.min((long) json.getInt("expires_in"), 86400L) * 1000000000);
                sOldKeySecretCode = this.mKeySecretCode;
                sOldToken = token;
                logger.log(Level.INFO, String.format("GET %s \n\t%s", this.httpRequest.getURI(), content));
                return token;
            } catch (Exception e) {
                logger.log(Level.INFO, String.format("GET %s\n", this.httpRequest.getURI()), (Throwable) e);
                throw e;
            }
        } catch (SocketTimeoutException e2) {
            throw new Exception("#1, Network operation timed out. request token failed, socket timeout", e2);
        } catch (ConnectTimeoutException e3) {
            throw new Exception("#1, Network operation timed out. request token failed, connect timeout", e3);
        } catch (HttpResponseException e4) {
            if (e4.getStatusCode() >= 500) {
                throw new Exception("#4, Server sends error status. server exception", e4);
            }
            throw new Exception("#2, Other network related errors. request token failed", e4);
        } catch (ClientProtocolException e5) {
            throw new Exception("#2, Other network related errors. request token failed", e5);
        } catch (IOException e6) {
            throw new Exception("#2, Other network related errors. request token failed", e6);
        }
    }

    private Object getParamOrThrow(Map<String, Object> params, String key2) throws IOException {
        Object val = params.get(key2);
        if (val != null) {
            return val;
        }
        throw new IOException("Missing parameter " + key2);
    }
}
