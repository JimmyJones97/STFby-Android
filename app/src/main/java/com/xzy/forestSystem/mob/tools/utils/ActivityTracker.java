package com.xzy.forestSystem.mob.tools.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.stub.StubApp;
import java.util.HashSet;
import java.util.Iterator;

public class ActivityTracker {
    private static ActivityTracker instance;
    private HashSet<Tracker> trackers = new HashSet<>();

    public interface Tracker {
        void onCreated(Activity activity, Bundle bundle);

        void onDestroyed(Activity activity);

        void onPaused(Activity activity);

        void onResumed(Activity activity);

        void onSaveInstanceState(Activity activity, Bundle bundle);

        void onStarted(Activity activity);

        void onStopped(Activity activity);
    }

    private ActivityTracker(Context context) {
        if (Build.VERSION.SDK_INT >= 14) {
            initLevel14(context);
        } else {
            init(context);
        }
    }

    public static synchronized ActivityTracker getInstance(Context context) {
        ActivityTracker activityTracker;
        synchronized (ActivityTracker.class) {
            if (instance == null) {
                instance = new ActivityTracker(context);
            }
            activityTracker = instance;
        }
        return activityTracker;
    }

    private void initLevel14(Context context) {
        ((Application) StubApp.getOrigApplicationContext(context.getApplicationContext())).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() { // from class:  com.xzy.forestSystem.mob.tools.utils.ActivityTracker.1
            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ActivityTracker.this.onCreated(activity, savedInstanceState);
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityStarted(Activity activity) {
                ActivityTracker.this.onStarted(activity);
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityResumed(Activity activity) {
                ActivityTracker.this.onResumed(activity);
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityPaused(Activity activity) {
                ActivityTracker.this.onPaused(activity);
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityStopped(Activity activity) {
                ActivityTracker.this.onStopped(activity);
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityDestroyed(Activity activity) {
                ActivityTracker.this.onDestroyed(activity);
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                ActivityTracker.this.onSaveInstanceState(activity, outState);
            }
        });
    }

    private void init(Context context) {
        try {
            ReflectHelper.setInstanceField(DeviceHelper.getInstance(context).currentActivityThread(), "mInstrumentation", new Instrumentation() { // from class:  com.xzy.forestSystem.mob.tools.utils.ActivityTracker.2
                @Override // android.app.Instrumentation
                public void callActivityOnCreate(Activity activity, Bundle icicle) {
                    super.callActivityOnCreate(activity, icicle);
                    ActivityTracker.this.onCreated(activity, icicle);
                }

                @Override // android.app.Instrumentation
                public void callActivityOnStart(Activity activity) {
                    super.callActivityOnStart(activity);
                    ActivityTracker.this.onStarted(activity);
                }

                @Override // android.app.Instrumentation
                public void callActivityOnResume(Activity activity) {
                    super.callActivityOnResume(activity);
                    ActivityTracker.this.onResumed(activity);
                }

                @Override // android.app.Instrumentation
                public void callActivityOnPause(Activity activity) {
                    super.callActivityOnPause(activity);
                    ActivityTracker.this.onPaused(activity);
                }

                @Override // android.app.Instrumentation
                public void callActivityOnStop(Activity activity) {
                    super.callActivityOnStop(activity);
                    ActivityTracker.this.onStopped(activity);
                }

                @Override // android.app.Instrumentation
                public void callActivityOnDestroy(Activity activity) {
                    super.callActivityOnDestroy(activity);
                    ActivityTracker.this.onDestroyed(activity);
                }

                @Override // android.app.Instrumentation
                public void callActivityOnSaveInstanceState(Activity activity, Bundle outState) {
                    super.callActivityOnSaveInstanceState(activity, outState);
                    ActivityTracker.this.onSaveInstanceState(activity, outState);
                }
            });
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
        }
    }

    public synchronized void addTracker(Tracker tracker) {
        this.trackers.add(tracker);
    }

    public synchronized void removeTracker(Tracker tracker) {
        this.trackers.remove(tracker);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void onCreated(Activity activity, Bundle savedInstanceState) {
        Iterator<Tracker> it = this.trackers.iterator();
        while (it.hasNext()) {
            Tracker t = it.next();
            if (t != null) {
                t.onCreated(activity, savedInstanceState);
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void onStarted(Activity activity) {
        Iterator<Tracker> it = this.trackers.iterator();
        while (it.hasNext()) {
            Tracker t = it.next();
            if (t != null) {
                t.onStarted(activity);
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void onResumed(Activity activity) {
        Iterator<Tracker> it = this.trackers.iterator();
        while (it.hasNext()) {
            Tracker t = it.next();
            if (t != null) {
                t.onResumed(activity);
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void onPaused(Activity activity) {
        Iterator<Tracker> it = this.trackers.iterator();
        while (it.hasNext()) {
            Tracker t = it.next();
            if (t != null) {
                t.onPaused(activity);
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void onStopped(Activity activity) {
        Iterator<Tracker> it = this.trackers.iterator();
        while (it.hasNext()) {
            Tracker t = it.next();
            if (t != null) {
                t.onStopped(activity);
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void onDestroyed(Activity activity) {
        Iterator<Tracker> it = this.trackers.iterator();
        while (it.hasNext()) {
            Tracker t = it.next();
            if (t != null) {
                t.onDestroyed(activity);
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void onSaveInstanceState(Activity activity, Bundle outState) {
        Iterator<Tracker> it = this.trackers.iterator();
        while (it.hasNext()) {
            Tracker t = it.next();
            if (t != null) {
                t.onSaveInstanceState(activity, outState);
            }
        }
    }
}
