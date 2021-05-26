package  com.xzy.forestSystem.qihoo.jiagutracker;

import android.app.Instrumentation;
import  com.xzy.forestSystem.qihoo.jiagutracker.Instrument.InstrumentCallback;
import  com.xzy.forestSystem.stub.StubApp;
import java.util.Set;

@QVMProtect
public class InstrumentationTrack {
    private static InstrumentationTrack sInstance;
    public Set<InstrumentCallback> mCallbacks;
    private Instrumentation mInstrumentation;

    static {
        StubApp.interface11(3777);
    }

    private native boolean doHook();

    public static native InstrumentationTrack getInstance();

    public native boolean initInstrumentationTrack(Set<InstrumentCallback> set);

    private InstrumentationTrack() {
    }
}
