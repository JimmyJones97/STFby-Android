package  com.xzy.forestSystem.qihoo.jiagutracker;

import android.app.Activity;
import android.os.Bundle;
import  com.xzy.forestSystem.qihoo.jiagutracker.Instrument.InstrumentCallback;
import  com.xzy.forestSystem.stub.StubApp;

@QVMProtect
public class JGInstrumentCallback extends InstrumentCallback {
    public static Activity sActivity;
    private static JGInstrumentCallback sInstance;
    private ViewWatcher mViewWatcher;

    static {
        StubApp.interface11(3779);
    }

    private native void addActivity(Activity activity);

    public static native JGInstrumentCallback getInstance();

    @Override //  com.xzy.forestSystem.qihoo.jiagutracker.Instrument.InstrumentCallback
    public native void afterOnCreate(Activity activity, Bundle bundle);

    @Override //  com.xzy.forestSystem.qihoo.jiagutracker.Instrument.InstrumentCallback
    public native void afterOnDestroy(Activity activity);

    @Override //  com.xzy.forestSystem.qihoo.jiagutracker.Instrument.InstrumentCallback
    public native void afterOnPause(Activity activity);

    @Override //  com.xzy.forestSystem.qihoo.jiagutracker.Instrument.InstrumentCallback
    public native void afterOnResume(Activity activity);

    @Override //  com.xzy.forestSystem.qihoo.jiagutracker.Instrument.InstrumentCallback
    public native void afterOnStart(Activity activity);

    @Override //  com.xzy.forestSystem.qihoo.jiagutracker.Instrument.InstrumentCallback
    public native void afterOnStop(Activity activity);

    @Override //  com.xzy.forestSystem.qihoo.jiagutracker.Instrument.InstrumentCallback
    public native void beforeOnCreate(Activity activity, Bundle bundle);

    @Override //  com.xzy.forestSystem.qihoo.jiagutracker.Instrument.InstrumentCallback
    public native void beforeOnPause(Activity activity);

    @Override //  com.xzy.forestSystem.qihoo.jiagutracker.Instrument.InstrumentCallback
    public native void beforeOnResume(Activity activity);

    @Override //  com.xzy.forestSystem.qihoo.jiagutracker.Instrument.InstrumentCallback
    public native void beforeOnStart(Activity activity);

    public native void setViewWatcher(ViewWatcher viewWatcher);
}
