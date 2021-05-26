package com.xzy.forestSystem.smssdk.net;

import  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

/* renamed from: cn.smssdk.net.HttpResponseCallbackImp */
public class HttpResponseCallbackImp implements HttpResponseCallback {

    /* renamed from: a */
    private HashMap<String, Object> f123a;

    public HttpResponseCallbackImp(HashMap<String, Object> hashMap) {
        this.f123a = hashMap;
    }

    public void handleInput(InputStream inputStream) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[65536];
        int read = inputStream.read(bArr);
        while (read > 0) {
            byteArrayOutputStream.write(bArr, 0, read);
            read = inputStream.read(bArr);
        }
        byteArrayOutputStream.flush();
        this.f123a.put("bResp", byteArrayOutputStream.toByteArray());
        byteArrayOutputStream.close();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0095, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0096, code lost:
        if (r1 != null) goto L_0x0098;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:33:?, code lost:
        r1.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:34:0x009b, code lost:
        throw r0;
     */
    @Override //  com.xzy.forestSystem.mob.tools.network.HttpResponseCallback
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onResponse( com.xzy.forestSystem.mob.tools.network.HttpConnection r7) throws Throwable {
        /*
            r6 = this;
            r1 = 0
            int r3 = r7.getResponseCode()
            java.util.HashMap<java.lang.String, java.lang.Object> r0 = r6.f123a
            java.lang.String r2 = "httpStatus"
            java.lang.Integer r4 = java.lang.Integer.valueOf(r3)
            r0.put(r2, r4)
            java.util.Map r0 = r7.getHeaderFields()
            java.lang.String r2 = "sign"
            java.lang.Object r0 = r0.get(r2)
            java.util.List r0 = (java.util.List) r0
            if (r0 == 0) goto L_0x0031
            int r2 = r0.size()
            if (r2 <= 0) goto L_0x0031
            java.lang.Object r0 = r0.get(r1)
            java.lang.String r0 = (java.lang.String) r0
            java.util.HashMap<java.lang.String, java.lang.Object> r2 = r6.f123a
            java.lang.String r4 = "sign"
            r2.put(r4, r0)
        L_0x0031:
            java.util.Map r0 = r7.getHeaderFields()
            java.lang.String r2 = "Content-Length"
            java.lang.Object r0 = r0.get(r2)
            java.util.List r0 = (java.util.List) r0
            if (r0 == 0) goto L_0x0052
            int r2 = r0.size()
            if (r2 <= 0) goto L_0x0052
            java.lang.Object r0 = r0.get(r1)
            java.lang.String r0 = (java.lang.String) r0
            java.util.HashMap<java.lang.String, java.lang.Object> r2 = r6.f123a
            java.lang.String r4 = "Content-Length"
            r2.put(r4, r0)
        L_0x0052:
            java.util.Map r0 = r7.getHeaderFields()
            java.lang.String r2 = "zip"
            java.lang.Object r0 = r0.get(r2)
            java.util.List r0 = (java.util.List) r0
            if (r0 == 0) goto L_0x0080
            int r2 = r0.size()
            if (r2 <= 0) goto L_0x0080
            java.lang.Object r0 = r0.get(r1)
            java.lang.String r0 = (java.lang.String) r0
            java.util.HashMap<java.lang.String, java.lang.Object> r2 = r6.f123a
            java.lang.String r4 = "zip"
            java.lang.String r5 = "1"
            boolean r0 = r5.equals(r0)
            if (r0 == 0) goto L_0x0091
            r0 = 1
        L_0x0079:
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            r2.put(r4, r0)
        L_0x0080:
            r0 = 200(0xc8, float:2.8E-43)
            if (r3 != r0) goto L_0x009c
            java.io.InputStream r1 = r7.getInputStream()
            r6.handleInput(r1)     // Catch:{ Throwable -> 0x0093 }
            if (r1 == 0) goto L_0x0090
            r1.close()     // Catch:{ Throwable -> 0x00d9 }
        L_0x0090:
            return
        L_0x0091:
            r0 = r1
            goto L_0x0079
        L_0x0093:
            r0 = move-exception
            throw r0     // Catch:{ all -> 0x0095 }
        L_0x0095:
            r0 = move-exception
            if (r1 == 0) goto L_0x009b
            r1.close()     // Catch:{ Throwable -> 0x00db }
        L_0x009b:
            throw r0
        L_0x009c:
            java.util.Map r0 = r7.getHeaderFields()
            java.lang.String r2 = "msg"
            java.lang.Object r0 = r0.get(r2)
            java.util.List r0 = (java.util.List) r0
            r2 = 0
            if (r0 == 0) goto L_0x00dd
            int r4 = r0.size()
            if (r4 <= 0) goto L_0x00dd
            java.lang.Object r0 = r0.get(r1)
            java.lang.String r0 = (java.lang.String) r0
        L_0x00b7:
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
            java.lang.String r2 = "error"
            r1.put(r2, r0)
            java.lang.String r0 = "status"
            java.lang.Integer r2 = java.lang.Integer.valueOf(r3)
            r1.put(r0, r2)
            java.lang.Throwable r0 = new java.lang.Throwable
             com.xzy.forestSystem.mob.tools.utils.Hashon r2 = new  com.xzy.forestSystem.mob.tools.utils.Hashon
            r2.<init>()
            java.lang.String r1 = r2.fromHashMap(r1)
            r0.<init>(r1)
            throw r0
        L_0x00d9:
            r0 = move-exception
            goto L_0x0090
        L_0x00db:
            r1 = move-exception
            goto L_0x009b
        L_0x00dd:
            r0 = r2
            goto L_0x00b7
        */
        throw new UnsupportedOperationException("Method not decompiled: p003cn.smssdk.net.HttpResponseCallbackImp.onResponse( com.xzy.forestSystem.mob.tools.network.HttpConnection):void");
    }
}
