package  com.xzy.forestSystem.qihoo.jiagutracker;

import android.app.Application;
import  com.xzy.forestSystem.stub.StubApp;

@QVMProtect
public class TrackerMain {
    static {
        StubApp.interface11(3782);
    }

    public static native void init(Application application);
}
