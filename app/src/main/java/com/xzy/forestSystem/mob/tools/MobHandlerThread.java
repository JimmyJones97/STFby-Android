package com.xzy.forestSystem.mob.tools;

import android.os.Looper;
import android.os.Process;

public class MobHandlerThread extends Thread {
    private Looper looper;
    private int priority;
    private int tid;

    public MobHandlerThread() {
        this.tid = -1;
        this.priority = 0;
    }

    public MobHandlerThread(int priority2) {
        this.tid = -1;
        this.priority = priority2;
    }

    /* access modifiers changed from: protected */
    public void onLooperPrepared() {
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        this.tid = Process.myTid();
        Looper.prepare();
        synchronized (this) {
            this.looper = Looper.myLooper();
            notifyAll();
        }
        Process.setThreadPriority(this.priority);
        onLooperPrepared();
        Looper.loop();
        this.tid = -1;
    }

    public Looper getLooper() {
        if (!isAlive()) {
            return null;
        }
        synchronized (this) {
            while (isAlive() && this.looper == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return this.looper;
    }

    public boolean quit() {
        Looper looper2 = getLooper();
        if (looper2 == null) {
            return false;
        }
        looper2.quit();
        return true;
    }

    public int getThreadId() {
        return this.tid;
    }
}
