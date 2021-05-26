package  com.xzy.forestSystem.qihoo.jiagutracker;

import  com.xzy.forestSystem.qihoo.jiagutracker.utils.CircularBuffer;
import  com.xzy.forestSystem.stub.StubApp;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

@QVMProtect
public class TrackDataManager {
    private static boolean INIT_Flag = false;
    private static CircularBuffer<Object> mCircularBuffer;
    private static JSONObject mShotScreen;

    private static native JSONArray getArray(List<ViewInfo> list);

    private static native JSONObject getJsonObject(ViewInfo viewInfo);

    public static native String getString();

    public static native String[] getTrackData();

    public static native void init();

    public static native JSONObject parse(List<ViewInfo> list, String str, int i, int i2, ViewInfo viewInfo);

    public static native boolean putElement(String str, String str2, String str3, String str4);

    static {
        StubApp.interface11(3781);
    }
}
