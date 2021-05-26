package  com.xzy.forestSystem.qihoo.jiagutracker.Instrument;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import  com.xzy.forestSystem.qihoo.jiagutracker.QVMProtect;
import  com.xzy.forestSystem.stub.StubApp;
import java.util.Set;

@QVMProtect
public class CustomInstrumentation extends Instrumentation {
    private final Set<InstrumentCallback> mCallbacks;
    private final Instrumentation mInstrumentation;

    static {
        StubApp.interface11(3775);
    }

    private native void callActivityOnCreateProxy(Activity activity, Bundle bundle, boolean z);

    private native void newActivityProxy(ClassLoader classLoader, String str, Intent intent);

    private native void onDestroyProxy(Activity activity, boolean z);

    private native void onNewIntentProxy(Activity activity, Intent intent, boolean z);

    private native void onPauseProxy(Activity activity, boolean z);

    private native void onResumeProxy(Activity activity, boolean z);

    private native void onStartProxy(Activity activity, boolean z);

    private native void onStopProxy(Activity activity, boolean z);

    @Override // android.app.Instrumentation
    public native void callActivityOnCreate(Activity activity, Bundle bundle);

    @Override // android.app.Instrumentation
    public native void callActivityOnDestroy(Activity activity);

    @Override // android.app.Instrumentation
    public native void callActivityOnNewIntent(Activity activity, Intent intent);

    @Override // android.app.Instrumentation
    public native void callActivityOnPause(Activity activity);

    @Override // android.app.Instrumentation
    public native void callActivityOnRestart(Activity activity);

    @Override // android.app.Instrumentation
    public native void callActivityOnRestoreInstanceState(Activity activity, Bundle bundle);

    @Override // android.app.Instrumentation
    public native void callActivityOnResume(Activity activity);

    @Override // android.app.Instrumentation
    public native void callActivityOnSaveInstanceState(Activity activity, Bundle bundle);

    @Override // android.app.Instrumentation
    public native void callActivityOnStart(Activity activity);

    @Override // android.app.Instrumentation
    public native void callActivityOnStop(Activity activity);

    @Override // android.app.Instrumentation
    public native Activity newActivity(ClassLoader classLoader, String str, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException;

    public CustomInstrumentation(Instrumentation instrumentation, Set<InstrumentCallback> set) {
        this.mInstrumentation = instrumentation;
        this.mCallbacks = set;
    }
}
