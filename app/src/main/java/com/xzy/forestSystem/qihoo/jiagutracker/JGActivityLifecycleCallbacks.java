package  com.xzy.forestSystem.qihoo.jiagutracker;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import  com.xzy.forestSystem.stub.StubApp;

@QVMProtect
public class JGActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    private static JGActivityLifecycleCallbacks sInstance;
    private ViewWatcher mViewWatcher = null;

    static {
        StubApp.interface11(3778);
    }

    public static native JGActivityLifecycleCallbacks getInstance();

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public native void onActivityCreated(Activity activity, Bundle bundle);

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public native void onActivityDestroyed(Activity activity);

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public native void onActivityPaused(Activity activity);

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public native void onActivityResumed(Activity activity);

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public native void onActivitySaveInstanceState(Activity activity, Bundle bundle);

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public native void onActivityStarted(Activity activity);

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public native void onActivityStopped(Activity activity);

    public native void setViewWatcher(ViewWatcher viewWatcher);

    private JGActivityLifecycleCallbacks() {
    }
}
