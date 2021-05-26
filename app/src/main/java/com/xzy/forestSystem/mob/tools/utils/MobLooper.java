package com.xzy.forestSystem.mob.tools.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import  com.xzy.forestSystem.stub.StubApp;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;

public class MobLooper {
    private Context context;
    private boolean goingToStop;

    public MobLooper(Context context2) {
        this.context = StubApp.getOrigApplicationContext(context2.getApplicationContext());
    }

    public void start(Runnable task, long interval) {
        start(task, interval, 0);
    }

    public synchronized void start(final Runnable task, final long interval, long delay) {
        this.goingToStop = false;
        final Object lock = new Object();
        Intent intent = new Intent(getClass().getName() + FileSelector_Dialog.sFolder + SystemClock.elapsedRealtime());
        final PendingIntent sender = PendingIntent.getBroadcast(this.context, 0, intent, 0);
        final AlarmManager am = (AlarmManager) this.context.getSystemService("alarm");
        am.set(3, SystemClock.elapsedRealtime() + delay, sender);
        this.context.registerReceiver(new BroadcastReceiver() { // from class:  com.xzy.forestSystem.mob.tools.utils.MobLooper.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent2) {
                if (!MobLooper.this.goingToStop) {
                    new Thread() { // from class:  com.xzy.forestSystem.mob.tools.utils.MobLooper.1.1
                        @Override // java.lang.Thread, java.lang.Runnable
                        public void run() {
                            synchronized (lock) {
                                task.run();
                            }
                        }
                    }.start();
                    am.set(3, SystemClock.elapsedRealtime() + interval, sender);
                }
            }
        }, new IntentFilter(intent.getAction()));
    }

    public synchronized void stop() {
        this.goingToStop = true;
    }
}
