package com.xzy.forestSystem.mob.tools.network;

public interface HttpResponseCallback {
    void onResponse(HttpConnection httpConnection) throws Throwable;
}
