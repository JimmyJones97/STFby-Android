package com.xzy.forestSystem.baidu.speech.easr.stat;

import android.content.Context;

public class StatThread extends Thread {
    private String mAppId;
    private Context mContext;

    public StatThread(Context context, String appId) {
        this.mContext = context;
        this.mAppId = appId;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        StatHelper.uploadStatDatas(this.mContext, this.mAppId);
        this.mContext = null;
    }
}
