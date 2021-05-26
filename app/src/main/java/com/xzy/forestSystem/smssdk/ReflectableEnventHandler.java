package com.xzy.forestSystem.smssdk;

import android.os.Handler;
import android.os.Message;

/* renamed from: cn.smssdk.ReflectableEnventHandler */
public class ReflectableEnventHandler extends EventHandler {

    /* renamed from: a */
    private int f5a;

    /* renamed from: b */
    private Handler.Callback f6b;

    /* renamed from: c */
    private int f7c;

    /* renamed from: d */
    private Handler.Callback f8d;

    /* renamed from: e */
    private int f9e;

    /* renamed from: f */
    private Handler.Callback f10f;

    /* renamed from: g */
    private int f11g;

    /* renamed from: h */
    private Handler.Callback f12h;

    public void setOnRegisterCallback(int i, Handler.Callback callback) {
        this.f5a = i;
        this.f6b = callback;
    }

    @Override // p003cn.smssdk.EventHandler
    public void onRegister() {
        if (this.f6b != null) {
            Message message = new Message();
            message.what = this.f5a;
            this.f6b.handleMessage(message);
        }
    }

    public void setBeforeEventCallback(int i, Handler.Callback callback) {
        this.f7c = i;
        this.f8d = callback;
    }

    @Override // p003cn.smssdk.EventHandler
    public void beforeEvent(int i, Object obj) {
        if (this.f8d != null) {
            Message message = new Message();
            message.what = this.f7c;
            message.obj = new Object[]{Integer.valueOf(i), obj};
            this.f8d.handleMessage(message);
        }
    }

    public void setAfterEventCallback(int i, Handler.Callback callback) {
        this.f9e = i;
        this.f10f = callback;
    }

    @Override // p003cn.smssdk.EventHandler
    public void afterEvent(int i, int i2, Object obj) {
        if (this.f10f != null) {
            Message message = new Message();
            message.what = this.f9e;
            message.obj = new Object[]{Integer.valueOf(i), Integer.valueOf(i2), obj};
            this.f10f.handleMessage(message);
        }
    }

    public void setOnUnregisterCallback(int i, Handler.Callback callback) {
        this.f11g = i;
        this.f12h = callback;
    }

    @Override // p003cn.smssdk.EventHandler
    public void onUnregister() {
        if (this.f12h != null) {
            Message message = new Message();
            message.what = this.f11g;
            this.f12h.handleMessage(message);
        }
    }
}
