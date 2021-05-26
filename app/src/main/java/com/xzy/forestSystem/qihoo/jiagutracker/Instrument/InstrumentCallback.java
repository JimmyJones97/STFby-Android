package  com.xzy.forestSystem.qihoo.jiagutracker.Instrument;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import  com.xzy.forestSystem.qihoo.jiagutracker.QVMProtect;
import  com.xzy.forestSystem.stub.StubApp;

@QVMProtect
public abstract class InstrumentCallback {
    static {
        StubApp.interface11(3776);
    }

    public native void afterOnCreate(Activity activity, Bundle bundle);

    public native void afterOnDestroy(Activity activity);

    public native void afterOnNewIntent(Activity activity, Intent intent);

    public native void afterOnPause(Activity activity);

    public native void afterOnResume(Activity activity);

    public native void afterOnStart(Activity activity);

    public native void afterOnStop(Activity activity);

    public native void beforeOnCreate(Activity activity, Bundle bundle);

    public native void beforeOnDestroy(Activity activity);

    public native void beforeOnNewActivity(ClassLoader classLoader, String str, Intent intent);

    public native void beforeOnNewIntent(Activity activity, Intent intent);

    public native void beforeOnPause(Activity activity);

    public native void beforeOnResume(Activity activity);

    public native void beforeOnStart(Activity activity);

    public native void beforeOnStop(Activity activity);
}
